# littleca

##项目介绍
littleca是一个基于BC的小型ca库,支持ecc,rsa,dsa,sm2的证书签发,加密,解密,签名,验签操作
对openssl友好,支持openssl的pem文件
littleca-core 提供基本操作  
littleca-ui 提供界面操作 x509v3 cer证书颁发,个人证书p12颁发,pkcs#8 公私钥文件生成  
littleca-tsa 时间戳服务

[little-auth](little-auth/little-auth.md) 基于littleca的认证服务  

[littleca-shell](littleca-shell/littleca-shell.md)  基于openssl 和keytool的证书签发服务  


littleca-ui演示网址:[演示网址](https://caui.taoyuanx.com/)
littleca-shell-ui演示网址:[演示网址](https://cashellui.taoyuanx.com/)

## 安装教程

### 快速自建ca
1. 访问示例网址  
参见项目简介

2. docker 安装
构建文件:参见docs/docker  

docker 仓库:  
https://hub.docker.com/repository/docker/dushitaoyuan/littleca_shell  
https://hub.docker.com/repository/docker/dushitaoyuan/littleca  
```shell script
# 启动 littleca-ui
docker pull dushitaoyuan/littleca
docker run    -p 8080:8080  -v /home/mycerts:/home/mycerts -v /home/java/logs:/home/java/logs   -d dushitaoyuan/littleca 


# 启动 littleca-shell-ui
docker pull dushitaoyuan/littleca_shell
# 基于keytool生成
docker run -p 8081:8081 -e shellType=java -v /home/myca:/home/ca -v /home/mycerts:/home/mycerts -v /home/java/logs:/home/java/logs   -d dushitaoyuan/littleca_shell
#基于openssl
docker run -p 8082:8081 -e shellType=openssl -v /home/myca1:/home/ca -v /home/mycerts1:/home/mycerts -v /home/java/logs1:/home/java/logs   -d dushitaoyuan/littleca_shell


```


### 二次开发

1. 下载编译成jar包
2. 使用示例
参见src/test/main/java/example

### 特别鸣谢

1. gmhelper https://github.com/ZZMarquis/gmhelper 
开源

2. algorithmNation https://github.com/xiaoshuaishuai319/algorithmNation/tree/master/src/main/java/cn/xsshome/algorithmNation/util
(付费开源)
3. doubleca http://www.doubleca.com/test_toIndexPage.action
( 不开源) 功能完善

## 备注
本类库 基于项目使用,需求实现,鄙人才疏学浅,对于数学算法缺乏一定的理解能力,希望不会有太大偏差

