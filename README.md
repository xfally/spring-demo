# 演示项目

![Java CI with Maven](https://github.com/xfally/spring-demo/workflows/Java%20CI%20with%20Maven/badge.svg)

## 运行项目

拉取maven依赖后，执行mvn命令或使用idea直接运行即可。

## 目录说明

```
.
├── db                                                // 数据库脚本（DDL）
│   ├── db1-create-data.sql
│   ├── db1-create-db.sql
│   ├── db1-create-schema.sql
│   ├── db2-create-data.sql
│   ├── db2-create-db.sql
│   └── db2-create-schema.sql
├── demo.iml                                          // IDE工程配置文件，这里个人用的IDE是IDEA
├── docker-compose.yml                                // docker-compose配置
├── Dockerfile                                        // docker配置
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
│   │   │               ├── entity                        // Entity/DO
│   │   │               ├── interceptor                   // 拦截器（HandlerInterceptor）
│   │   │               ├── mapper                        // Mapper/DAO
│   │   │               ├── model                         // Model/VO
│   │   │               ├── service                       // Service
│   │   │               └── tool                          // 工具
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
2. 本项目采用Mybatis-Plus映射ORM，可运行`tool/MybatisPlusGenerator.java`自动生产DAO层以及相关业务代码（controller、service、mapper、entity...）；
3. 当项目规模增大后，可以考虑按功能模块拆分，每个模块都包含controller、service、DAO等；（注意此时需要将“第3点”产生的文件，手动移动到对应模块的包路径下）
4. 当项目规模非常大时，则应考虑拆分为多个微服务；（注意此时需考虑数据库分库或共享问题）

