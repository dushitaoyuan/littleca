# littleca-shell

## 项目介绍
littleca-shell-core:  
 基于openssl 和keytool的证书生成工具包  
littleca-shel-ui :  
    配套界面  

1. 目前只支持rsa
2. 修改脚本可支持更多算法
3. shell无交互设计,参数由上层应用封装
### openssl 配置设置

1. 准备
```shell
mkdir myca
cd myca
mkdir newcerts certs crl
touch index.txt serial
echo 01 > serial
```
2. 创建根证书
```shell
1.生成 ca私钥
openssl genrsa -out private/cakey.pem 2048
2. 对ca 证书进行自签名 根证书自签名
openssl req -new -x509 -key private/cakey.pem -out cacert.pem

```
3. 配置
```editorconfig
# ca目录配置
dir		= g://openssl/ca		# Where everything is kept

# 客户端证书和根证书属性匹配规则
 For the CA policy
[ policy_match ]
countryName		= match
stateOrProvinceName	= optional
organizationName	= optional
organizationalUnitName	= optional
commonName		= supplied
emailAddress		= optional

# For the 'anything' policy
# At this point in time, you must list all acceptable 'object'
# types.
[ policy_anything ]
countryName		= optional
stateOrProvinceName	= optional
localityName		= optional
organizationName	= optional
organizationalUnitName	= optional
commonName		= supplied
emailAddress		= optional



将policy_match 修改为optional  match 表示属性必须一致

```

### 生成脚本和配置

1. 示例配置
docs/openssl  
    - windows  
    - linux  
2. 脚本   
docs/shell  
    - windows  
    - linux 
cert_shell.xxx 交互型脚本(按照提示输入参数)
cert_shell_auto.xxx 自动化脚本(需入参)


### api设计

1. login 登录
2. cert 签发
3. downCertZip 下载

