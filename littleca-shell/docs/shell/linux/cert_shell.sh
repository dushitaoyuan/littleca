#!/bin/bash


#openssl ca私钥地址 用作给颁发证书签名
BASE_CA_PRI="/opt/myca/cacert.pem"
# openssl  生成的客户端证书系列文件存储地址
BASE_FOLDERNAME="client"
#生成的所有证书存储的基础目录
BASE_ALL_PATH="/opt/myca/testcer/"

# 输入描述
function  desc () { 
        
printf  "C 国家代码 ST 省份 L 城市 O 组织 OU 单位 CN 域名（example:*.baidu.com"
printf "各省市简称如下:"
printf "BJ―北京市；SH―上海市；TJ―天津市；CQ―重庆市；HE―河北省；SX―山西省；NM―内蒙古自治区；LN―辽宁省"
printf "JL―吉林省；HL―黑龙江省；JS―江苏省；ZJ―浙江省；AH―安徽省；FJ―福建省；JX―江西省；SD―山东省；HA―河南省"
printf "HB―湖北省；HN―湖南省；GD―广东省；GX―广西壮族自治区； HI―海南省； SC―四川省；GZ―贵州省；YN―云南省"
printf "XZ―西藏自治区；SN―陕西省；GS―甘肃省；QH―青海省；NX―宁夏回族自治区；XJ―新疆维吾尔族自治区；TW―台湾省；HK―香港特别行政区；MO―澳门特别行政区"

}  


function create () {

printf  "C 国家代码 ST 省份 L 城市 O 组织 OU 单位 CN 域名（example:*.baidu.com\n"
printf "各省市简称如下:\n" 
printf "BJ―北京市；SH―上海市；TJ―天津市；CQ―重庆市；HE―河北省；SX―山西省；NM―内蒙古自治区；LN―辽宁省\n"
printf "JL―吉林省；HL―黑龙江省；JS―江苏省；ZJ―浙江省；AH―安徽省；FJ―福建省；JX―江西省；SD―山东省；HA―河南省\n"
printf "HB―湖北省；HN―湖南省；GD―广东省；GX―广西壮族自治区； HI―海南省； SC―四川省；GZ―贵州省；YN―云南省\n"
printf "XZ―西藏自治区；SN―陕西省；GS―甘肃省；QH―青海省；NX―宁夏回族自治区；XJ―新疆维吾尔族自治区；TW―台湾省；HK―香港特别行政区；MO―澳门特别行政区\n"



if [ ! -d  $BASE_ALL_PATH ]; then
 mkdir -p $BASE_ALL_PATH
 fi

cd $BASE_ALL_PATH 
 
if [ ! -d  $BASE_FOLDERNAME ]; then
 mkdir -p $BASE_FOLDERNAME
 fi

cd $BASE_FOLDERNAME

 


printf "请依次输入,C=国家代码(例如:中国CN),ST=省份(例如:北京BJ),L=城市(例如:北京BJ),O=组织(例如:baidu):,OU=单位(例如:baidu):,CN=域名(例如:*.baidu.com):并以空格间隔\n"
read C ST L O OU CN

subject="/C="${C}"/ST="${ST}"/L="${L}"/O="${O}"/OU="${OU}"/CN="${CN}


echo "证书主题:"${subject}



# 拼接 证书存放地址
fileDir=${ST}_${L}_${O}

if [ ! -d  $fileDir ]; then
 mkdir -p $fileDir
 fi

cd  ${fileDir}

echo "生成文件存储路径为:"$(pwd) 



#生成私钥
openssl genrsa  -out client_private.pem 2048
# 生成公钥
openssl rsa -in client_private.pem -pubout -out client_public_key.pem 

#生成请求证书
openssl req -utf8 -new -days 3650 -key client_private.pem -out client_req.pem -subj $subject

#根据请求证书生成x509公钥证书
openssl ca -in client_req.pem -out client_public_key.pem -days 3650 -batch
 
#合并公钥证书和私钥生成p12个人证书
openssl pkcs12 -export -in client_public_key.pem -inkey client_private.pem -out client.p12 -certfile  $BASE_CA_PRI -password  pass:123456


# 私钥转 pkcs8格式
openssl pkcs8 -topk8 -nocrypt -in client_private.pem -out client_pkcs8_key.pem
}



case $1 in
    "create" ) create;;
esac
 
exit 0
