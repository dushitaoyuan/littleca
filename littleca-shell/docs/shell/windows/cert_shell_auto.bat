@echo off


set "functionName=%1"

set "createCertDir=%2"
::-- openssl  subject
set "subject=%3"
::-- cert����
set "cert_password=%4"


set "rsa_bit_num=%5"

set "cert_expire_days=%6"




if not exist %createCertDir% ( mkdir %createCertDir% )


echo "agrs " functionName=%functionName% createCertDir=%createCertDir%   subject=%subject% rsa_bit_num=%rsa_bit_num%    cert_password=%cert_password%  cert_expire_days=%cert_expire_days%


if  %functionName% == openssl_create (
	if "1" NEQ "%7" (
		::-- openssl �����ļ���ַ
		set OPENSSL_CONF=%7
	) 
 	::-- openssl ca˽Կ��ַ �������䷢֤��ǩ��
	set BASE_CA_PRI=%8
		
	call :openssl_create 	
   
)




if  %functionName% == java_create (
   call :java_create 
)

  

::  pause


exit


::-- openssl ����
:openssl_create



echo  OPENSSL_CONF=%OPENSSL_CONF%  BASE_CA_PRI=%BASE_CA_PRI%  

cd /d%createCertDir%


::-- ����˽Կ
openssl genrsa  -out client_private.pem %rsa_bit_num%
::--echo "genrsa client_private.pem"

::-- ���ɹ�Կ
openssl rsa -in client_private.pem -pubout -out client_public_key.pem 

::--echo "genrsa client_public_key.pem"
::-- ��������֤��
openssl req -new  -key client_private.pem -out client_req.pem -subj %subject%

::--echo "genrsa client_req.pem"


::-- ��������֤������x509��Կ֤��
openssl ca -in client_req.pem -out client_public_key.pem -days %cert_expire_days% -batch
 
::--echo "genrsa client_public_key.pem"

::-- �ϲ���Կ֤���˽Կ����p12����֤��
openssl pkcs12 -export -in client_public_key.pem -inkey client_private.pem -out client.p12 -certfile  %BASE_CA_PRI% -password  pass:%cert_password%

::-- echo "genrsa client.p12"
::--  ˽Կת pkcs8��ʽ 
openssl pkcs8 -topk8 -nocrypt -in client_private.pem -out client_pkcs8_key.pem

::--echo "genrsa client_pkcs8_key.pem"


goto:eof


::-- keytool ����

:java_create

cd /d%createCertDir%


keytool -genkey -v -alias client -keyalg RSA -keysize %rsa_bit_num% -storetype pkcs12 -keystore client.p12 -dname %subject% -validity %cert_expire_days% -storepass %cert_password% -keypass  %cert_password%

 

keytool -export -alias client -file client.crt -storetype pkcs12  -keystore client.p12 -storepass  %cert_password%

goto:eof