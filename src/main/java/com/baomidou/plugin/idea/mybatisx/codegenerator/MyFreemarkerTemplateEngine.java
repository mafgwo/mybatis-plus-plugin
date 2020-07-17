package com.baomidou.plugin.idea.mybatisx.codegenerator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.GenConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.generator.config.ConstVal.*;

/**
 * 只能读取外部的文件，要提供全部文件，不然不能找到
 */
public class MyFreemarkerTemplateEngine extends AbstractTemplateEngine {
    /**
     * 从用户读取自定义模板文件
     */
    private Configuration configurationOut;
    /**
     * 读取系统的模板文件
     */
    private Configuration configurationSelf;

    private final String projectPath;
    private final GenConfig genConfig;

    public MyFreemarkerTemplateEngine(String projectPath, GenConfig genConfig) {
        this.projectPath = projectPath;
        this.genConfig = genConfig;
    }

    @Override
    public MyFreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        configurationOut = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configurationOut.setDefaultEncoding(ConstVal.UTF8);
        try {
            configurationOut.setDirectoryForTemplateLoading(new File(projectPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 自带的
        configurationSelf = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configurationSelf.setDefaultEncoding(ConstVal.UTF8);
        configurationSelf.setClassForTemplateLoading(FreemarkerTemplateEngine.class, StringPool.SLASH);

        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = null;
        try {
            template = configurationOut.getTemplate(templatePath);
        } catch (Exception e) {
            logger.debug("没有外部模板:" + templatePath + ";  文件:" + outputFile);
            logger.debug(e.toString());
        }
        if (template != null) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
            }
        } else { // 自带的
            template = configurationSelf.getTemplate(templatePath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
            }
        }

        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }

    /**
     * 输出 java xml 文件
     */
    @Override
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initMap();
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    // gen mapper.xml
                    if (genConfig.isMapper()) {
                        if (CollectionUtils.isNotEmpty(focList)) {
                            for (FileOutConfig foc : focList) {
                                if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                    writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                                }
                            }
                        }
                    }
                }
                String entityName = tableInfo.getEntityName();
                // Mp.java
                if (genConfig.isEntity()) {
                    if (null != entityName && null != pathInfo.get(ENTITY_PATH)) {
                        String entityFile = String.format((pathInfo.get(ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                        if (isCreate(FileType.ENTITY, entityFile)) {
                            writer(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                        }
                    }
                }

                // MpMapper.java
                if (genConfig.isMapper()) {
                    if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                        String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                        if (isCreate(FileType.MAPPER, mapperFile)) {
                            writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
                        }
                    }
                    // MpMapper.xml
                    if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                        String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                        if (isCreate(FileType.XML, xmlFile)) {
                            writer(objectMap, templateFilePath(template.getXml()), xmlFile);
                        }
                    }
                }
                // IMpService.java
                if (genConfig.isService()) {
                    if (null != tableInfo.getServiceName() && null != pathInfo.get(SERVICE_PATH)) {
                        String serviceFile = String.format((pathInfo.get(SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                        if (isCreate(FileType.SERVICE, serviceFile)) {
                            writer(objectMap, templateFilePath(template.getService()), serviceFile);
                        }
                    }
                }
                // MpServiceImpl.java
                if (genConfig.isServiceImpl()) {
                    if (null != tableInfo.getServiceImplName() && null != pathInfo.get(SERVICE_IMPL_PATH)) {
                        String implFile = String.format((pathInfo.get(SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                        if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                            writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                        }
                    }
                }
                // MpController.java
                if (genConfig.isController()) {
                    if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                        String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                        if (isCreate(FileType.CONTROLLER, controllerFile)) {
                            writer(objectMap, templateFilePath(template.getController()), controllerFile);
                        }
                    }
                }
            }
        } catch (
            Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

    /**
     * 处理输出目录
     */
    @Override
    public AbstractTemplateEngine mkdirs() {
        Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            if (key.equals(ENTITY_PATH) && !genConfig.isEntity()) {
                return;
            }
            if (key.equals(SERVICE_PATH) && !genConfig.isService()) {
                return;
            }
            if (key.equals(SERVICE_IMPL_PATH) && !genConfig.isServiceImpl()) {
                return;
            }
            if (key.equals(CONTROLLER_PATH) && !genConfig.isController()) {
                return;
            }
            if ((key.equals(MAPPER_PATH)|| key.equals(XML_PATH)) && !genConfig.isMapper()) {
                return;
            }

            File dir = new File(value);
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    logger.debug("创建目录： [" + value + "]");
                }
            }
        });
        return this;
    }
}
