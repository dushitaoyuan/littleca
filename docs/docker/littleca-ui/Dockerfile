#java 环境准备
FROM dushitaoyuan/alpine-oracle-java8:jre
# 设置时区
ENV TZ='Asia/Shanghai'

# java 部署目录
RUN mkdir -p /home/java /home/initCerts

VOLUME ["/home/mycerts","/home/java/logs"]


ADD littleca-ui-1.0-SNAPSHOT.jar /home/java
ADD jar.sh /home/java/
COPY initCerts/* /home/initCerts/
RUN dos2unix /home/java/jar.sh
#启动
EXPOSE 8080

ENTRYPOINT ["sh","-c","/home/java/jar.sh start -Dspring.profiles.active=prod"]
