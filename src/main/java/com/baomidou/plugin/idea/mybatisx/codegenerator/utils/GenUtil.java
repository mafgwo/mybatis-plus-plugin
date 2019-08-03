package com.baomidou.plugin.idea.mybatisx.codegenerator.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.plugin.idea.mybatisx.codegenerator.MyFreemarkerTemplateEngine;
import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.GenConfig;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.*;

/**
 * 代码生成
 */
public class GenUtil {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIGDECIMAL = "BigDecimal";

    private static final String PK = "PRI";

    private static final String EXTRA = "auto_increment";

    /**
     * 获取后端代码模板名称
     *
     * @return
     */
    public static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");
        templateNames.add("Dto");
        templateNames.add("Mapper");
        templateNames.add("Repository");
        templateNames.add("Service");
        templateNames.add("ServiceImpl");
        templateNames.add("QueryCriteria");
        templateNames.add("Controller");
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     *
     * @return
     */
    public static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("api");
        templateNames.add("index");
        templateNames.add("eForm");
        return templateNames;
    }

    public static void main(String[] args) {

        String tableName = "user";
        GenConfig genConfig = new GenConfig();
        genConfig.setId(1L);
        genConfig.setPack("org.py.modules.ShowTableInfo");
        genConfig.setModuleName("my-system");
        genConfig.setPath("D:\\tempfile\\");
        genConfig.setApiPath("D:\\tempfile\\api");
        genConfig.setRootFolder("D:\\tempfile");
        genConfig.setAuthor("py");
        genConfig.setCover(false);

        List<ColumnInfo> columnInfos = new ArrayList<>();
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setColumnName("id");
        columnInfo.setColumnType("bigint");
        columnInfo.setColumnKey("PRI");
        columnInfo.setExtra("auto_increment");
        columnInfo.setColumnShow("true");

        columnInfo.setColumnComment("123");
        columnInfos.add(columnInfo);
        GenUtil.generatorCode(tableName, genConfig);
    }

    public static void generatorCode(String tableName, GenConfig genConfig) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
        String projectPath = genConfig.getRootFolder();
        gc.setOutputDir(projectPath + File.separator + genConfig.getModuleName() + "/src/main/java");
        gc.setAuthor(genConfig.getAuthor());
        gc.setOpen(false);
        gc.setFileOverride(genConfig.isCover());
        // 实体属性 Swagger2 注解
        gc.setSwagger2(genConfig.isSwagger());
        // 设置基础resultMap
        gc.setBaseResultMap(genConfig.isResultmap());
        // 是否在xml中添加二级缓存配置
        gc.setEnableCache(genConfig.isEnableCache());
//        时间类型对应策略
//        gc.setDateType()
//        开启 baseColumnList
        gc.setBaseColumnList(genConfig.isBaseColumnList());
        //设置主键id
        gc.setIdType(IDTYPES[genConfig.getIdtype()].getIdType());

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(MysqlUtil.getInstance().getDbUrl());
//         dsc.setSchemaName("public");
        dsc.setDriverName(MysqlUtil.getInstance().getJdbcDriver());
        dsc.setUsername(MysqlUtil.getInstance().getUsername());
        dsc.setPassword(MysqlUtil.getInstance().getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // 在pack下的文件，现在不要设置null值
        pc.setModuleName(null);
        pc.setParent(genConfig.getPack());
        // 配置输出的包名
        pc.setEntity(genConfig.getEntityName());
        pc.setMapper(genConfig.getMapperName());
        pc.setController(genConfig.getControllerName());
        pc.setService(genConfig.getServiceName());
        pc.setServiceImpl(genConfig.getServiceImplName());
        String xmlName = "mapper";
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 1 从用户读取自定义模板文件
        //

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        String templatePath = "/templates";
        String mapperTemplatePath = templatePath + "/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(mapperTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String result = projectPath + "/" + genConfig.getModuleName() + "/src/main/resources/" + xmlName + "/"
                    + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                return result;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        templateConfig.setEntity(templatePath + "/entity.java");
        templateConfig.setMapper(templatePath + "/mapper.java");
        templateConfig.setEntityKt(templatePath + "/entity.kt");
        templateConfig.setService(templatePath + "/service.java");
        templateConfig.setService(templatePath + "/service.java");
        templateConfig.setServiceImpl(templatePath + "/serviceImpl.java");
        templateConfig.setController(templatePath + "/controller.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        // entity 是否使用lombok
        strategy.setEntityLombokModel(genConfig.isLombok());
        // 是否使用restController
        strategy.setRestControllerStyle(genConfig.isRestcontroller());
        // 公共父类
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(tableName);
        strategy.setControllerMappingHyphenStyle(true);
        // 表前缀
        strategy.setTablePrefix(pc.getModuleName() + "_");
        // 是否使用自动填充
        if (genConfig.isFill()) {
            List<TableFill> tableFillList = new ArrayList<>();
            tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
            tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
            strategy.setTableFillList(tableFillList);
        }
        // 乐观锁
//        strategy.setVersionFieldName("version_name");

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new MyFreemarkerTemplateEngine(projectPath));
        mpg.execute();
    }

    /**
     * 生成代码
     *
     * @param columnInfos 表元数据
     * @param genConfig   生成代码的参数配置，如包路径，作者
     */
    public static void generatorCode123(String tableName, List<ColumnInfo> columnInfos, GenConfig genConfig) throws IOException {
        Map<String, Object> map = new HashMap();
        map.put("package", genConfig.getPack());
        map.put("moduleName", genConfig.getModuleName());
        map.put("author", genConfig.getAuthor());
        map.put("date", LocalDate.now().toString());
        map.put("tableName", tableName);
        String className = MyStringUtils.toCapitalizeCamelCase(tableName);
        String changeClassName = MyStringUtils.toCamelCase(tableName);

        // 判断是否去除表前缀
        if (MyStringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = MyStringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(tableName, genConfig.getPrefix()));
            changeClassName = MyStringUtils.toCamelCase(StrUtil.removePrefix(tableName, genConfig.getPrefix()));
        }
        map.put("className", className);
        map.put("upperCaseClassName", className.toUpperCase());
        map.put("changeClassName", changeClassName);
        map.put("hasTimestamp", false);
        map.put("hasBigDecimal", false);
        map.put("hasQuery", false);
        map.put("auto", false);

        List<Map<String, Object>> columns = new ArrayList<>();
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        for (ColumnInfo column : columnInfos) {
            Map<String, Object> listMap = new HashMap();
            listMap.put("columnComment", column.getColumnComment());
            listMap.put("columnKey", column.getColumnKey());

            String colType = ColUtil.columnToJava(column.getColumnType().toString());
            String changeColumnName = MyStringUtils.toCamelCase(column.getColumnName().toString());
            String capitalColumnName = MyStringUtils.toCapitalizeCamelCase(column.getColumnName().toString());
            if (PK.equals(column.getColumnKey())) {
                map.put("pkColumnType", colType);
                map.put("pkChangeColName", changeColumnName);
                map.put("pkCapitalColName", capitalColumnName);
            }
            if (TIMESTAMP.equals(colType)) {
                map.put("hasTimestamp", true);
            }
            if (BIGDECIMAL.equals(colType)) {
                map.put("hasBigDecimal", true);
            }
            if (EXTRA.equals(column.getExtra())) {
                map.put("auto", true);
            }
            listMap.put("columnType", colType);
            listMap.put("columnName", column.getColumnName());
            listMap.put("isNullable", column.getIsNullable());
            listMap.put("columnShow", column.getColumnShow());
            listMap.put("changeColumnName", changeColumnName);
            listMap.put("capitalColumnName", capitalColumnName);

            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!MyStringUtils.isBlank(column.getColumnQuery())) {
                listMap.put("columnQuery", column.getColumnQuery());
                map.put("hasQuery", true);
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns", columns);
        map.put("queryColumns", queryColumns);
//        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));

        Configuration configuration = new Configuration();
        configuration.setClassLoaderForTemplateLoading(configuration.getClass().getClassLoader(), "template");
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {

            Template template = configuration.getTemplate("generator/admin/" + templateName + ".ftl");


            String filePath = getAdminFilePath(templateName, genConfig, className);

            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.isCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }

        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
//            Template template = engine.getTemplate("generator/front/"+templateName+".ftl");
            String filePath = getFrontFilePath(templateName, genConfig, map.get("changeClassName").toString());
            Template template = configuration.getTemplate("generator/front/" + templateName + ".ftl");

            File file = new File(filePath);

            // 如果非覆盖生成
            if (!genConfig.isCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }
    }


    /**
     * 定义后端文件路径以及名称
     */
    public static String getAdminFilePath(String templateName, GenConfig genConfig, String className) {
        String projectPath = genConfig.getRootFolder() + File.separator + genConfig.getModuleName();

        String packagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;

//        genConfig.setPath(packagePath);

        if (!MyStringUtils.isEmpty(genConfig.getPack())) {
            packagePath += genConfig.getPack().replace(".", File.separator) + File.separator;
        }

        if ("Entity".equals(templateName)) {
            return packagePath + "domain" + File.separator + className + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "rest" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("Dto".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "DTO.java";
        }

        if ("QueryCriteria".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "QueryCriteria.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "service" + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }

        if ("Repository".equals(templateName)) {
            return packagePath + "repository" + File.separator + className + "Repository.java";
        }

        return null;
    }

    /**
     * 定义前端文件路径以及名称
     */
    public static String getFrontFilePath(String templateName, GenConfig genConfig, String apiName) {
        String path = MyStringUtils.isEmpty(genConfig.getPath()) ? genConfig.getApiPath() : genConfig.getPath();

        if ("api".equals(templateName)) {
            return genConfig.getApiPath() + File.separator + apiName + ".js";
        }

        if ("index".equals(templateName)) {
            return path + File.separator + "index.vue";
        }

        if ("eForm".equals(templateName)) {
            return path + File.separator + File.separator + "form.vue";
        }
        return null;
    }

    public static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.process(map, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
