#!/bin/sh
APP_DIR="/home/java/"
cd $APP_DIR
JAR_NAME=`ls ./ | grep .jar$`
JAVA_OPS="-server $2"

#启动程序
function start() {
   initOpenssl
   cd $APP_DIR
   java $JAVA_OPS -jar  $JAR_NAME
}

#初始化openssl 默认配置
function initOpenssl() {
    count=`ls -A /home/ca|wc -w`
    if [ $count == 0 ];then
     echo "init openssl"
     cp -r /home/myca/* /home/ca
     cd /home/ca
     mkdir -p private  newcerts certs crl crlnumber
     touch index.txt serial
     echo 01 >serial
     echo "init finshed"
    fi
}
# 判断进程是否存活
function isAlive() {
# alpine 容器查询 进程id方法
    local pid=`ps aux | grep JAR_NAME |grep -v "grep" | grep  $JAR_NAME | awk '{ print $1 }'`
    if [ -z $pid ];then
       echo 0
    else
      echo $pid
    fi
}


case $1 in
    "start" ) start;;
    "isAlive" ) isAlive;;
    * ) echo "$0 start"
        echo "$0 isAlive"
esac
exit 0
