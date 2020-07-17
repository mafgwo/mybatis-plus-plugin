package com.baomidou.plugin.idea.mybatisx.codegenerator.db;

import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.intellij.openapi.ui.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDb {

    private Logger logger = LoggerFactory.getLogger(BaseDb.class);

    private String username;
    private String password;
    private String dbUrl;

    abstract String getTableSql();

    abstract String getColumnSql(String tableName);

    public abstract String dbName();

    public abstract List<TableInfo> getTableInfo();

    public abstract List<ColumnInfo> getColumns(String tableName);

    public void testConnect() {
        String result = "test successful！";
        try {
            Class.forName(getJdbcDriver());
            logger.info("connect db...");
            DriverManager.getConnection(getDbUrl(), getUsername(), getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            result = e.getMessage();
            logger.error(e.toString());
        }
        Messages.showInfoMessage(result, "Mybatis Plus");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract String getJdbcDriver();

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
}
