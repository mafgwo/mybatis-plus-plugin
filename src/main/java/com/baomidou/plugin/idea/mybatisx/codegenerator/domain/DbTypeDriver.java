package com.baomidou.plugin.idea.mybatisx.codegenerator.domain;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * db type and driver
 */
public class DbTypeDriver {
    private DbType dbType;
    private String name;
    public DbTypeDriver(DbType dbType,String name){
        this.dbType = dbType;
        this.name = name;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
