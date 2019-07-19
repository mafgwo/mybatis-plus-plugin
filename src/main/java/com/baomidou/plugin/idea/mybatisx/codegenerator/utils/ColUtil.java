package com.baomidou.plugin.idea.mybatisx.codegenerator.utils;

import org.apache.commons.configuration.*;

/**
 * sql字段转java
 *
 */
public class ColUtil {

    /**
     * 转换mysql数据类型为java数据类型
     * @param type
     * @return
     */
    public static String cloToJava(String type){
        Configuration config = getConfig();
        return config.getString(type,"unknowType");
    }

    /**
     * 获取配置信息
     */
    public static PropertiesConfiguration getConfig() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        // 数据库类型转换成java类型
        config.addProperty("tinyint","Integer");
        config.addProperty("smallint","Integer");
        config.addProperty("mediumint","Integer");
        config.addProperty("int","Integer");
        config.addProperty("integer","Integer");

        config.addProperty("bigint","Long");

        config.addProperty("float","Float");

        config.addProperty("double","Double");

        config.addProperty("decimal","BigDecimal");

        config.addProperty("bit","Boolean");

        config.addProperty("char","String");
        config.addProperty("varchar","String");
        config.addProperty("tinytext","String");
        config.addProperty("text","String");
        config.addProperty("mediumtext","String");
        config.addProperty("longtext","String");

        config.addProperty("date","Timestamp");
        config.addProperty("datetime","Timestamp");
        config.addProperty("timestamp","Timestamp");
        return config;
        /*try {
            return new PropertiesConfiguration("generator.properties" );
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;*/
    }
}
