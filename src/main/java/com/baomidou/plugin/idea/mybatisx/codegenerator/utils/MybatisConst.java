package com.baomidou.plugin.idea.mybatisx.codegenerator.utils;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.IdTypeObj;

public interface MybatisConst {
    String PLUS_DBURL = "mybatisplus_dbUrl";
    String PLUS_JDBCDRIVER = "mybatisplus_jdbcDriver";
    String PLUS_USERNAME = "mybatisplus_username";
    String PLUS_PASSWORD = "mybatisplus_password";
//
//    String PLUS_FRONT_PATH = "mybatisplus_front_path";
//    String PLUS_FRONT_API_PATH= "mybatisplus_api_path";
//    String PLUS_MODULE = "mybatisplus_module";
//    String PLUS_PACKAGE= "mybatisplus_package";
//    String PLUS_AUTHOR= "mybatisplus_author";
//    String PLUS_IS_OVER= "mybatisplus_is_over";
//
//    String PLUS_IS_LOMBOK = "mybatisplus_is_lombok";
//    String PLUS_IS_SWAGGER = "mybatisplus_is_swagger";
//    String PLUS_IS_RESTCONTROLLER = "mybatisplus_is_restcontroller";
//    String PLUS_IS_RESULTMAP = "mybatisplus_is_resultmap";
//    String PLUS_IS_FILL = "mybatisplus_is_fill";
//
//    String PLUS_ENTITY_NAME = "mybatisplus_entity";
//    String PLUS_MAPPER_NAME = "mybatisplus_mapper";
//    String PLUS_CONTROLLER_NAME = "mybatisplus_controller";
//    String PLUS_SERVICE_NAME = "mybatisplus_service";
//    String PLUS_SERVICE_IMPL_NAME = "mybatisplus_service_impl";
//
//    String PLUS_IDTYPE= "mybatisplus_idtype";


    String GEN_CONFIG = "genconfig";


    IdTypeObj[] IDTYPES = new IdTypeObj[]{
        new IdTypeObj(IdType.AUTO, "AUTO(ID自增)"),
        new IdTypeObj(IdType.NONE, "NONE"),
        new IdTypeObj(IdType.INPUT, "INPUT(用户输入ID)"),
        new IdTypeObj(IdType.ID_WORKER, "ID_WORKER(全局唯一ID)"),
        new IdTypeObj(IdType.UUID, "UUID(全局唯一ID )"),
        new IdTypeObj(IdType.ID_WORKER_STR, "ID_WORKER_STR(字符串全局唯一ID)")
    };

    String[] jdbcDrivers = new String[]{
        "com.mysql.jdbc.Driver",
        "com.mysql.cj.jdbc.Driver",
        "oracle.jdbc.driver.OracleDriver"
    };

}
