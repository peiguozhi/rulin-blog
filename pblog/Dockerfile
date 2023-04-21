# Docker image for springboot file run
# VERSION 0.0.1
# Author: 程序儒
# 基础镜像使用java
FROM openjdk:8
# 作者
MAINTAINER codescholar
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为xx.jar
ADD rulin-blog.jar app.jar

CMD ["--spring.profiles.active=prod"]

EXPOSE 8888

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]