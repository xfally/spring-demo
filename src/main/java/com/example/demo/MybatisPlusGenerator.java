package com.example.demo;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author pax
 */
public class MybatisPlusGenerator {
    private static final String AUTHOR = "pax";
    private static final String DS_URL = "jdbc:mysql://localhost:3306/com_example_demo?useUnicode=true&useSSL=false&character_set_server=utf8mb4";
    private static final String DS_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DS_USER_NAME = "root";
    private static final String DS_PASSWORD = "123456";
    private static final String PKG_PARENT = "com.example.demo";
    private static final String[] TABLE_LIST = {"t_user"};

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //=== 全局配置 ===//
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(AUTHOR);
        // 是否覆盖文件
        gc.setFileOverride(false);
        // 生成后自动打开文件
        //gc.setOpen(false);
        // 实体属性采用 Swagger2 注解
        gc.setSwagger2(true);
        // 主键策略
        //gc.setIdType(IdType.AUTO);
        mpg.setGlobalConfig(gc);

        //=== 数据源配置 ===//
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DS_URL);
        //dsc.setSchemaName("public");
        dsc.setDriverName(DS_DRIVER_NAME);
        dsc.setUsername(DS_USER_NAME);
        dsc.setPassword(DS_PASSWORD);
        /*
        dsc.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(GlobalConfig gc, String fieldType) {
                System.out.println("转换类型：" + fieldType);
                if (fieldType.toLowerCase().contains("tinyint")) {
                    return DbColumnType.BOOLEAN;
                }
                return (DbColumnType) super.processTypeConvert(gc, fieldType);
            }
        });
        */
        mpg.setDataSource(dsc);

        //=== 映射策略配置 ===//
        StrategyConfig strategy = new StrategyConfig();
        // 表名映射策略（下划线转驼峰）
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 列名映射策略（下划线转驼峰）
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 支持自动生成注解（如`@ApiModel`，`@ApiModelProperty`，`@TableId`，`@TableField`等）
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 支持Lombok格式
        strategy.setEntityLombokModel(true);
        // 支持Rest风格
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        // 扫描哪些表
        strategy.setInclude(TABLE_LIST);
        strategy.setControllerMappingHyphenStyle(true);
        // 根据表名来建对应的类名，比如表名是`t_user`，这里设置前缀是`t_`，生成的类名就是`User`（会自动删除此处设置的前缀）
        strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);

        //=== 包配置 ===//
        PackageConfig pc = new PackageConfig();
        // 公共包路径
        pc.setParent(PKG_PARENT);
        // 模块名，比如公共包路径是`com.example.demo`，如果模块名是`xxx`，则会在`com.example.demo.xxx`下生成controller和service等包
        //pc.setModuleName("xxx");
        // Controller层包名
        pc.setController("controller");
        // 实体包名
        pc.setEntity("entity");
        // DAO层包名
        pc.setMapper("mapper");
        // Service层包名
        pc.setService("service");
        // Service实现层包名
        pc.setServiceImpl("service.impl");
        // XML映射的存储路径（包名）
        pc.setXml("mapper.xml");
        mpg.setPackageInfo(pc);

        //=== 模板引擎配置 ===//
        // 本工程采用前后端分离，后端不涉及视图渲染，不用关心templates文件。
        // 默认是Velocity
        //mpg.setTemplateEngine(new VelocityTemplateEngine());
        //mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }
}