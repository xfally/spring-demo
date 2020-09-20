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
│   ├── db0-mongo
│   ├── db1-mysql
│   ├── db2-postgres
│   └── keycloak
├── docker-compose.yml                                // docker-compose配置
├── Dockerfile                                        // docker配置
├── HOWTO-mongo-rs.md                                 // HOWTO文档：如何基于docker搭建mongo集群
├── log
│   └── spring-demo.log
├── pom.xml                                           // maven工程配置
├── README.md                                         // 本文档
├── spring-demo.iml                                    // IDEA工程配置文件
├── src                                               // 源码目录
│   ├── main
│   │   ├── java
│   │   │   └── io/github/xfally
│   │   │       └── spring
│   │   │           └── demo
│   │   │               ├── aspect                            // 切面（@Aspect）
│   │   │               ├── common                            // “公共通用”模块，后续考虑独立jar包
│   │   │               ├── config                            // 配置（@Configuration）
│   │   │               ├── controller                        // Controller
│   │   │               ├── MainApplication.java              // Main主入口
│   │   │               ├── dao                               // DAO层，含entity和mapper
│   │   │               ├── interceptor                       // 拦截器（HandlerInterceptor）
│   │   │               ├── model                             // Model/VO
│   │   │               ├── service                           // Service
│   │   │               └── tool                              // 工具
│   │   └── resources                                     // 资源目录
│   │       ├── application.properties                        // springboot配置文件
│   │       ├── log4j.properties                              // log配置
│   │       └── static                                        // 存放页面静态资源，前后端分离后，不再直接使用该目录
│   │           └── index.html                                    // 示例页面
│   └── test                                              // 测试用例
└── target                                            // 产物目录
    ├── classes                                           // 前后端分离后，编译时，可自动将前端页面直接拷贝到该目录下的static子目录
    ├── spring-demo-0.0.1-SNAPSHOT.jar
    ├── spring-demo-0.0.1-SNAPSHOT.jar.original
    ├── docker                                            // docker打包临时目录
    ├── generated-sources
    ├── generated-test-sources
    ├── maven-archiver
    ├── maven-status
    ├── surefire-reports
    └── test-classes
```


## 开发建议

1. 将各处`io.github.xfally.spring.demo`关键字、文件名以及包路径，修改为具体项目的英文名；
2. 根据需要裁剪或调整pom.xml、application.properties、Dockerfile、docker-compose.yml等配置文件；
3. 至少执行db目录下的DDL脚本，创建空数据库；
4. 针对RDB（如MySQL、PostgresDB），本项目默认采用JPA/Hibernate映射ORM（想使用Mybatis-plus，请切换到dev-mbp分支），并在application.properties中配置了scheme自动创建；
5. 针对NoSQL（如MongoDB），本项目默认采用JPA映射ORM；
6. 由于MongoDB需要启用集群，才能支持事务回滚，故项目预置的docker-compose.yml启用了“复制集”集群，详见HOWTO-mongo-rs.md文档说明；
7. 由于启用了MongoDB事务机制，故JPA无法自动创建数据库中的集合，需参考mongo-create-schema.js脚本手动创建集合；
8. 当项目规模增大后，可以考虑按功能模块拆分，每个模块都包含controller、service、DAO等；（注意此时需要将“第3点”产生的文件，手动移动到对应模块的包路径下）
9. 当项目规模非常大时，则应考虑拆分为多个微服务；（注意此时需考虑数据库分库或共享问题）
10. 本项目给了多数据源如何配置连接的示例，如ds1、ds2，通常业务不会涉及这种场景，可删除其中一个ds与相关aspect拦截配置；
11. 本项目默认启用了redis查询结果缓存（application.properties中可设置被动的自动过期清理时间）并主动使用`@CacheEvict`小心地剔除脏数据;
12. 考虑到尽量简单处理最少的实体类，本项目建议controller层使用VO，service和dao使用DO；
13. 本项目application.properties默认启用外部SSO服务（keycloak），若以项目预置的docker-compose方式运行，需外挂数据库，可参考db/keycloak目录下的DDL脚本，创建其所需数据库；
14. 启动keycloak后，请用keycloak的admin账号登录其管理员控制台页面，新建realm和client实例（可用import的方式导入db/keycloak目录下的realm-export.json文件，新建实例）；
15. 本项目keycloak默认采用credentials认证方式，请在管理员控制台页面，生成或查看本client的secret值，然后更新application.properties中的`keycloak.credentials.secret`。

