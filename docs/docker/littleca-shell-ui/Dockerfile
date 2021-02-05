#java 环境准备
FROM dushitaoyuan/alpine-oracle-java8:jre
# 设置时区
ENV TZ='Asia/Shanghai'
ENV shellType="openssl"

RUN mkdir -p /home/java \
 /home/ca /home/myca /home/mycerts && apk add openssl

VOLUME ["/home/ca","/home/mycerts","/home/java/logs"]

COPY ca/* /home/myca/
ADD littleca-shell-ui-1.0-SNAPSHOT.jar /home/java
ADD jar.sh /home/java/
RUN dos2unix /home/java/jar.sh

#启动
EXPOSE 8081

ENTRYPOINT ["sh","-c","/home/java/jar.sh start -Dspring.profiles.active=$shellType"]
