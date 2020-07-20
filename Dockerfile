FROM java:8
MAINTAINER pax coolwinding@gmail.com
WORKDIR /
COPY demo-0.3.1.jar /app.jar
# springboot会按以下顺序去寻找配置文件
# 1. “当前目录”的/config文件夹下
# 2. “当前目录”下
# 3. classpath的/config文件夹下
# 4. classpath下
# 这里将config目录挂载，可以在生产环境，将定制的properties/yml文件拷贝到这个目录，重启容器即可生效。
VOLUME /config
# 挂载可变目录，避免污染镜像
VOLUME /log
# 挂载可变目录，避免污染镜像
VOLUME /tmp
EXPOSE 8080
CMD ["java", "-version"]
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
