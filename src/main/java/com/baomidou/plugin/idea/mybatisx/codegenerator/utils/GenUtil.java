package com.baomidou.plugin.idea.mybatisx.codegenerator.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
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

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.jdbcDrivers;

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
        gc.setFileOverride(genConfig.getCover());
        gc.setSwagger2(true); // 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(MysqlUtil.getInstance().getDbUrl());
        // dsc.setSchemaName("public");
        dsc.setDriverName(jdbcDrivers[MysqlUtil.getInstance().getJdbcDriver()]);
        dsc.setUsername(MysqlUtil.getInstance().getUsername());
        dsc.setPassword(MysqlUtil.getInstance().getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(""); // 在pack下的文件，现在不要
        pc.setParent(genConfig.getPack());
        pc.setXml(genConfig.getPack());
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // todo 从用户读取自定义模板文件

        // todo 文件浏览

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
//        String templatePath = "D:/tempfile/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
               String result =  projectPath  + "/" + genConfig.getModuleName() + "/src/main/resources/mapper/"
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
        // templateConfig.setService();
        // templateConfig.setController();
//        String xmlPath =  projectPath  + File.separator + genConfig.getModuleName() + "/src/main/resources/mapper/";
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(tableName);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    /**
     * 生成代码
     * @param columnInfos 表元数据
     * @param genConfig 生成代码的参数配置，如包路径，作者
     */
    public static void generatorCode123(String tableName, List<ColumnInfo> columnInfos, GenConfig genConfig) throws IOException {
        Map<String,Object> map = new HashMap();
        map.put("package",genConfig.getPack());
        map.put("moduleName",genConfig.getModuleName());
        map.put("author",genConfig.getAuthor());
        map.put("date", LocalDate.now().toString());
        map.put("tableName",tableName);
        String className = StringUtils.toCapitalizeCamelCase(tableName);
        String changeClassName = StringUtils.toCamelCase(tableName);

        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(tableName,genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(tableName,genConfig.getPrefix()));
        }
        map.put("className", className);
        map.put("upperCaseClassName", className.toUpperCase());
        map.put("changeClassName", changeClassName);
        map.put("hasTimestamp",false);
        map.put("hasBigDecimal",false);
        map.put("hasQuery",false);
        map.put("auto",false);

        List<Map<String,Object>> columns = new ArrayList<>();
        List<Map<String,Object>> queryColumns = new ArrayList<>();
        for (ColumnInfo column : columnInfos) {
            Map<String,Object> listMap = new HashMap();
            listMap.put("columnComment",column.getColumnComment());
            listMap.put("columnKey",column.getColumnKey());

            String colType = ColUtil.cloToJava(column.getColumnType().toString());
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName().toString());
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName().toString());
            if(PK.equals(column.getColumnKey())){
                map.put("pkColumnType",colType);
                map.put("pkChangeColName",changeColumnName);
                map.put("pkCapitalColName",capitalColumnName);
            }
            if(TIMESTAMP.equals(colType)){
                map.put("hasTimestamp",true);
            }
            if(BIGDECIMAL.equals(colType)){
                map.put("hasBigDecimal",true);
            }
            if(EXTRA.equals(column.getExtra())){
                map.put("auto",true);
            }
            listMap.put("columnType",colType);
            listMap.put("columnName",column.getColumnName());
            listMap.put("isNullable",column.getIsNullable());
            listMap.put("columnShow",column.getColumnShow());
            listMap.put("changeColumnName",changeColumnName);
            listMap.put("capitalColumnName",capitalColumnName);

            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if(!StringUtils.isBlank(column.getColumnQuery())){
                listMap.put("columnQuery",column.getColumnQuery());
                map.put("hasQuery",true);
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns",columns);
        map.put("queryColumns",queryColumns);
//        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));

        Configuration configuration = new Configuration();
        configuration.setClassLoaderForTemplateLoading(configuration.getClass().getClassLoader(),"template");
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {

            Template template = configuration.getTemplate("generator/admin/"+templateName+".ftl");


            String filePath = getAdminFilePath(templateName, genConfig, className);

            File file = new File(filePath);

            // 如果非覆盖生成
            if(!genConfig.getCover() && FileUtil.exist(file)){
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }

        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
//            Template template = engine.getTemplate("generator/front/"+templateName+".ftl");
            String filePath = getFrontFilePath(templateName,genConfig,map.get("changeClassName").toString());
            Template template = configuration.getTemplate("generator/front/"+templateName+".ftl");

            File file = new File(filePath);

            // 如果非覆盖生成
            if(!genConfig.getCover() && FileUtil.exist(file)){
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

        String packagePath = projectPath + File.separator + "src" +File.separator+ "main" + File.separator + "java" + File.separator;

//        genConfig.setPath(packagePath);

        if (!StringUtils.isEmpty(genConfig.getPack())) {
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
        String path = StringUtils.isEmpty(genConfig.getPath()) ?  genConfig.getApiPath() : genConfig.getPath();

        if ("api".equals(templateName)) {
            return genConfig.getApiPath() + File.separator + apiName + ".js";
        }

        if ("index".equals(templateName)) {
            return path  + File.separator + "index.vue";
        }

        if ("eForm".equals(templateName)) {
            return path  + File.separator + File.separator + "form.vue";
        }
        return null;
    }

    public static void genFile(File file, Template template, Map<String,Object> map) throws IOException {
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
