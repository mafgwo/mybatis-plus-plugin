package com.baomidou.plugin.idea.mybatisx.codegenerator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

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

    private String projectPath;

    public MyFreemarkerTemplateEngine(String projectPath){
        this.projectPath = projectPath;
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
}
