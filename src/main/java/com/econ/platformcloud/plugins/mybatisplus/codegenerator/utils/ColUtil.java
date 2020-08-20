package com.econ.platformcloud.plugins.mybatisplus.codegenerator.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * sql字段转java
 */
public class ColUtil {

    /**
     * 转换mysql数据类型为java数据类型
     */
    public static String columnToJava(String type) {
        Configuration config = getConfig();
        return config.getString(type, "unknownType");
    }

    /**
     * 获取配置信息
     */
    private static PropertiesConfiguration getConfig() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        // 数据库类型转换成java类型
        config.addProperty("tinyint", "Integer");
        config.addProperty("smallint", "Integer");
        config.addProperty("mediumint", "Integer");
        config.addProperty("int", "Integer");
        config.addProperty("integer", "Integer");

        config.addProperty("bigint", "Long");

        config.addProperty("float", "Float");

        config.addProperty("double", "Double");

        config.addProperty("decimal", "BigDecimal");

        config.addProperty("bit", "Boolean");

        config.addProperty("char", "String");
        config.addProperty("varchar", "String");
        config.addProperty("tinytext", "String");
        config.addProperty("text", "String");
        config.addProperty("mediumtext", "String");
        config.addProperty("longtext", "String");

        config.addProperty("date", "Timestamp");
        config.addProperty("datetime", "Timestamp");
        config.addProperty("timestamp", "Timestamp");
        return config;
    }
}
