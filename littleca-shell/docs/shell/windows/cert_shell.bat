@echo on
::-- openssl �����ļ���ַ
set OPENSSL_CONF=g:\openssl\ca\openssl.cnf
::-- openssl ca˽Կ��ַ �������䷢֤��ǩ��
set BASE_CA_PRI="g:\openssl\ca\cacert.pem"
::-- openssl  ���ɵĿͻ���֤��ϵ���ļ��洢��ַ
set BASE_FOLDERNAME=client


set BASE_ALL_PATH="g:\openssl"

echo ������ �ͻ���x509֤�� ���� subject  example "/C=CN/ST=xx/L=xx/O=xxx/OU=xxx/CN=*.xx.com" 
echo C ���Ҵ��� ST ʡ�� L ���� O ��֯ OU ��λ CN ������example:*.baidu.com��

echo ��ʡ�м������:
echo BJ�D�����У�SH�D�Ϻ��У�TJ�D����У�CQ�D�����У�HE�D�ӱ�ʡ��SX�Dɽ��ʡ��NM�D���ɹ���������LN�D����ʡ��
echo JL�D����ʡ��HL�D������ʡ��JS�D����ʡ��ZJ�D�㽭ʡ��AH�D����ʡ��FJ�D����ʡ��JX�D����ʡ��SD�Dɽ��ʡ��HA�D����ʡ��
echo HB�D����ʡ��HN�D����ʡ��GD�D�㶫ʡ��GX�D����׳���������� HI�D����ʡ�� SC�D�Ĵ�ʡ��GZ�D����ʡ��YN�D����ʡ��
echo XZ�D������������SN�D����ʡ��GS�D����ʡ��QH�D�ຣʡ��NX�D���Ļ�����������XJ�D�½�ά�������������TW�D̨��ʡ��HK�D����ر���������MO�D�����ر�������

set /p C=���Ҵ���(����:�й�CN):
set /p ST=ʡ��(����:����BJ):
set /p L=����(����:����BJ):
set /p O=��֯(����:baidu):
set /p OU=��λ(����:baidu):
set /p CN=����(����:*.baidu.com):
set subject="/C=%C%/ST=%ST%/L=%L%/O=%O%/OU=%OU%/CN=%CN%"


call :create %subject%

echo "�������,�ļ��ѱ�����,%BASE_ALL_PATH%\clientĿ¼��"

pause

exit





:create
cd %BASE_ALL_PATH%

if not exist %BASE_FOLDERNAME% ( mkdir %BASE_FOLDERNAME% )

cd %BASE_FOLDERNAME%
set fileDir="%ST%_%L%_%O%"
if not exist %fileDir% (mkdir %fileDir%)
cd %fileDir%
::-- ����˽Կ
openssl genrsa  -out client_private.pem 2048
::-- ���ɹ�Կ
openssl rsa -in client_private.pem -pubout -out client_public_key.pem 

::-- ��������֤��
openssl req -new  -key client_private.pem -out client_req.pem -subj %1

::-- ��������֤������x509��Կ֤��
openssl ca -in client_req.pem -out client_public_key.pem -days 3650 -batch
 
::-- �ϲ���Կ֤���˽Կ����p12����֤��
openssl pkcs12 -export -in client_public_key.pem -inkey client_private.pem -out client.p12 -certfile  %BASE_CA_PRI% -password  pass:123456


::--  ˽Կת pkcs8��ʽ .net��ȡ����
openssl pkcs8 -topk8 -nocrypt -in client_private.pem -out client_pkcs8_key.pem


goto:eof
