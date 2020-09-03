# 演示项目

![Java CI with Maven](https://github.com/xfally/spring-demo/workflows/Java%20CI%20with%20Maven/badge.svg)

## 运行项目

拉取maven依赖后，执行mvn命令或使用idea直接运行即可。

常用命令有：

- `mvn clean`：清理项目
- `mvn compile`：编译项目
- `mvn package`：编译项目并打jar包
- `mvn install`：部署jar包到本地maven仓库，构建docker镜像到本地（可用`docker images`查看生成的镜像）
- `mvn deploy`（暂不支持）：推送jar包到远程maven仓库，推送docker镜像到远程registry

## 目录说明

```
.
├── db                                                // 数据库脚本（DDL）
│   ├── db1-create-data.sql
│   ├── db1-create-db.sql
│   ├── db1-create-schema.sql                             // ORM采用JPA时，不使用！
│   ├── db2-create-data.sql
│   ├── db2-create-db.sql
│   └── db2-create-schema.sql                             // ORM采用JPA时，不使用！
├── demo.iml                                          // IDE工程配置文件，这里个人用的IDE是IDEA
├── docker-compose.yml                                // docker-compose配置
├── Dockerfile                                        // docker配置
├── keycloak                                          // SSO服务
├── log
│   └── demo.log
├── pom.xml                                           // maven工程配置
├── README.md
├── src                                               // 源码目录
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo
│   │   │               ├── aspect                        // 切面（@Aspect）
│   │   │               ├── common                        // “公共通用”模块，后续考虑独立jar包
│   │   │               ├── config                        // 配置（@Configuration）
│   │   │               ├── controller                    // Controller
│   │   │               ├── DemoApplication.java          // Main主入口
│   │   │               ├── dao                           // DAO层，含entity和repository
│   │   │               ├── interceptor                   // 拦截器（HandlerInterceptor）
│   │   │               ├── model                         // Model/VO
│   │   │               └── service                       // Service
│   │   └── resources                                 // 资源目录
│   │       ├── application.properties                    // springboot配置文件
│   │       ├── log4j.properties                          // log配置
│   │       └── static                                    // 存放页面静态资源，前后端分离后，不再直接使用该目录
│   │           └── index.html
│   └── test
└── target                                            // 产物目录
    ├── classes                                           // 前后端分离后，编译时，可自动将前端页面直接拷贝到该目录下的static子目录
    ├── demo-0.0.1-SNAPSHOT.jar
    ├── demo-0.0.1-SNAPSHOT.jar.original
    ├── docker
    ├── generated-sources
    ├── generated-test-sources
    ├── maven-archiver
    ├── maven-status
    ├── surefire-reports
    └── test-classes
```


## 二次开发建议

0. 将各处demo关键字或文件名，修改为具体项目的英文名；
1. 根据需要裁剪或调整pom.xml、application.properties、Dockerfile、docker-compose.yml等配置文件；
2. 本项目默认采用JPA/Hibernate映射ORM（想使用Mybatis-plus，请切换到dev-mbp分支），并在application.properties中配置了scheme自动创建；
3. 当项目规模增大后，可以考虑按功能模块拆分，每个模块都包含controller、service、DAO等；（注意此时需要将“第3点”产生的文件，手动移动到对应模块的包路径下）
4. 当项目规模非常大时，则应考虑拆分为多个微服务；（注意此时需考虑数据库分库或共享问题）
5. 本项目给了多数据源如何配置连接的示例，如ds1、ds2，通常业务不会涉及这种场景，可删除ds2与相关aspect拦截配置；
6. 本项目默认启用了redis查询结果缓存（application.properties中可设置被动的自动过期清理时间）并主动使用`@CacheEvict`小心地剔除脏数据;
7. 考虑到尽量简单处理最少的实体类，本项目建议controller层使用VO，service和dao使用DO；
8. 本项目application.properties默认启用外部SSO服务（keycloak），若以项目预置的docker-compose方式运行，需外挂数据库，可参考keycloak目录下的数据库创建脚本，先创建其所需数据库。

