FROM java:8
MAINTAINER pax coolwinding@gmail.com
WORKDIR /
COPY spring-demo-0.5.4.jar /app.jar
# 挂载配置目录，本地持久化
# springboot会按以下顺序去寻找配置文件
# 1. “当前目录”的/config文件夹下
# 2. “当前目录”下
# 3. classpath的/config文件夹下
# 4. classpath下
# 在生产环境，将定制的properties/yml文件拷贝到此目录，重启容器即可生效。
VOLUME /config
# 挂载数据目录，本地持久化
VOLUME /data
# 挂载日志目录，避免污染镜像
VOLUME /log
# 挂载临时目录，避免污染镜像
VOLUME /tmp
EXPOSE 8080
CMD ["java", "-version"]
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
