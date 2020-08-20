package com.econ.platformcloud.plugins.mybatisplus.codegenerator.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.DbTypeDriver;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.IdTypeObj;

public interface MybatisConst {
    String PLUS_DBURL = "mybatisplus_dbUrl";
    String PLUS_DBTYPE = "mybatisplus_dbtype";
    String PLUS_USERNAME = "mybatisplus_username";
    String PLUS_PASSWORD = "mybatisplus_userpp";

    String GEN_CONFIG = "genconfig";

    IdTypeObj[] IDTYPES = new IdTypeObj[]{
        new IdTypeObj(IdType.AUTO, "AUTO(ID自增)"),
        new IdTypeObj(IdType.NONE, "NONE"),
        new IdTypeObj(IdType.INPUT, "INPUT(用户输入ID)"),
        new IdTypeObj(IdType.ID_WORKER, "ID_WORKER(全局唯一ID)"),
        new IdTypeObj(IdType.UUID, "UUID(全局唯一ID )"),
        new IdTypeObj(IdType.ID_WORKER_STR, "ID_WORKER_STR(字符串全局唯一ID)")
    };

    DbTypeDriver[] DB_TYPE_DRIVERS = new DbTypeDriver[]{
        new DbTypeDriver(DbType.MYSQL, "mysql"),
        new DbTypeDriver(DbType.ORACLE, "oracle")
    };


}
