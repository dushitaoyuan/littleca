#!/bin/bash
#生成证书路径
createCertDir=$2

subject=$3
# cert密码
cert_password=$4


rsa_bit_num=$5

cert_expire_days=$6

openssl_cnf=$7

if [ ! -d  $createCertDir ]; then
 mkdir -p $createCertDir
 fi


if  [ "1" != "$openssl_cnf" ]; then
 # 设置 openssl 配置环境变量
 export  OPENSSL_CONF=$openssl_cnf
 echo "openssl.cnf=$OPENSSL_CONF"
 fi

BASE_CA_PRI=$8


function openssl_create () {


cd $createCertDir

#生成私钥
openssl genrsa  -out client_private.pem $rsa_bit_num
# 生成公钥
openssl rsa -in client_private.pem -pubout -out client_public_key.pem 

#生成请求证书
openssl req -utf8 -new  -key client_private.pem -out client_req.pem -subj $subject

#根据请求证书生成x509公钥证书
openssl ca -in client_req.pem -out client_public_key.pem -days $cert_expire_days -batch
 
#合并公钥证书和私钥生成p12个人证书
openssl pkcs12 -export -in client_public_key.pem -inkey client_private.pem -out client.p12 -certfile  $BASE_CA_PRI -password  pass:$cert_password


# 私钥转 pkcs8格式
openssl pkcs8 -topk8 -nocrypt -in client_private.pem -out client_pkcs8_key.pem
}

function java_create() {
   cd $createCertDir

   keytool -genkey -v -alias client -keyalg RSA -keysize $rsa_bit_num -storetype pkcs12 -keystore client.p12 -dname $subject -validity $cert_expire_days -storepass $cert_password -keypass  $cert_password

   keytool -export -alias client -file client.crt -storetype pkcs12  -keystore client.p12 -storepass  $cert_password

}



case $1 in
    "openssl_create" ) openssl_create;;
    "java_create" ) java_create;;
esac
 
exit 0
