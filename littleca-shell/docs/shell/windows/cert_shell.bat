@echo on
::-- openssl 配置文件地址
set OPENSSL_CONF=g:\openssl\ca\openssl.cnf
::-- openssl ca私钥地址 用作给颁发证书签名
set BASE_CA_PRI="g:\openssl\ca\cacert.pem"
::-- openssl  生成的客户端证书系列文件存储地址
set BASE_FOLDERNAME=client


set BASE_ALL_PATH="g:\openssl"

echo 请输入 客户端x509证书 主题 subject  example "/C=CN/ST=xx/L=xx/O=xxx/OU=xxx/CN=*.xx.com" 
echo C 国家代码 ST 省份 L 城市 O 组织 OU 单位 CN 域名（example:*.baidu.com）

echo 各省市简称如下:
echo BJ―北京市；SH―上海市；TJ―天津市；CQ―重庆市；HE―河北省；SX―山西省；NM―内蒙古自治区；LN―辽宁省；
echo JL―吉林省；HL―黑龙江省；JS―江苏省；ZJ―浙江省；AH―安徽省；FJ―福建省；JX―江西省；SD―山东省；HA―河南省；
echo HB―湖北省；HN―湖南省；GD―广东省；GX―广西壮族自治区； HI―海南省； SC―四川省；GZ―贵州省；YN―云南省；
echo XZ―西藏自治区；SN―陕西省；GS―甘肃省；QH―青海省；NX―宁夏回族自治区；XJ―新疆维吾尔族自治区；TW―台湾省；HK―香港特别行政区；MO―澳门特别行政区

set /p C=国家代码(例如:中国CN):
set /p ST=省份(例如:北京BJ):
set /p L=城市(例如:北京BJ):
set /p O=组织(例如:baidu):
set /p OU=单位(例如:baidu):
set /p CN=域名(例如:*.baidu.com):
set subject="/C=%C%/ST=%ST%/L=%L%/O=%O%/OU=%OU%/CN=%CN%"


call :create %subject%

echo "生成完成,文件已保存在,%BASE_ALL_PATH%\client目录下"

pause

exit





:create
cd %BASE_ALL_PATH%

if not exist %BASE_FOLDERNAME% ( mkdir %BASE_FOLDERNAME% )

cd %BASE_FOLDERNAME%
set fileDir="%ST%_%L%_%O%"
if not exist %fileDir% (mkdir %fileDir%)
cd %fileDir%
::-- 生成私钥
openssl genrsa  -out client_private.pem 2048
::-- 生成公钥
openssl rsa -in client_private.pem -pubout -out client_public_key.pem 

::-- 生成请求证书
openssl req -new -days 3650 -key client_private.pem -out client_req.pem -subj %1

::-- 根据请求证书生成x509公钥证书
openssl ca -in client_req.pem -out client_public_key.pem -days 3650 -batch
 
::-- 合并公钥证书和私钥生成p12个人证书
openssl pkcs12 -export -in client_public_key.pem -inkey client_private.pem -out client.p12 -certfile  %BASE_CA_PRI% -password  pass:123456


::--  私钥转 pkcs8格式 .net读取方便
openssl pkcs8 -topk8 -nocrypt -in client_private.pem -out client_pkcs8_key.pem


goto:eof
