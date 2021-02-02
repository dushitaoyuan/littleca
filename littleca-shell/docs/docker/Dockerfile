#java 环境准备
FROM dushitaoyuan/alpine-oracle-java8:jre
# 设置时区
ENV TZ='Asia/Shanghai'

# java 部署目录
RUN mkdir -p /home/java
#openssl配置
RUN mkdir -p /home/ca /home/myca /home/mycerts
RUN  apk add openssl

COPY ca/* /home/myca/


ENV shellType="openssl"

VOLUME ["/home/ca","/home/mycerts","/home/java/logs"]

ADD littleca-shell-ui-1.0-SNAPSHOT.jar /home/java
ADD jar.sh /home/java/
RUN dos2unix /home/java/jar.sh

#启动
EXPOSE 8081

ENTRYPOINT ["sh","-c","/home/java/jar.sh start -Dspring.profiles.active=$shellType"]
