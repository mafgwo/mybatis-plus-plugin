package com.econ.platformcloud.plugins.mybatisplus.codegenerator;

import com.econ.platformcloud.plugins.mybatisplus.codegenerator.db.BaseDb;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.db.MysqlDb;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.db.OracleDb;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.vo.ColumnInfo;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.vo.TableInfo;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.utils.MybatisConst;
import com.intellij.ide.util.PropertiesComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MysqlUtil {

    private Logger logger = LoggerFactory.getLogger(MysqlUtil.class);

    private BaseDb baseDb;

    private MysqlUtil() {
        resetDbInfo();
    }

    public void resetDbInfo() {
        int dbType = PropertiesComponent.getInstance().getInt(MybatisConst.PLUS_DBTYPE, 0);
        if ("mysql".equals(MybatisConst.DB_TYPE_DRIVERS[dbType].getName())) {
            baseDb = new MysqlDb();
            baseDb.setDbUrl(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_DBURL));
            baseDb.setUsername(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_USERNAME));
            baseDb.setPassword(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_PASSWORD));
        } else if ("oracle".equals(MybatisConst.DB_TYPE_DRIVERS[dbType].getName())) {
            baseDb = new OracleDb();
            baseDb.setDbUrl(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_DBURL));
            baseDb.setUsername(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_USERNAME));
            baseDb.setPassword(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_PASSWORD));
        }
    }

    private static MysqlUtil mysqlUtil;

    public static MysqlUtil getInstance() {
        if (mysqlUtil == null) {
            mysqlUtil = new MysqlUtil();
        }
        return mysqlUtil;
    }

    public List<TableInfo> getTableInfo() {
        return baseDb.getTableInfo();
    }

    public List<ColumnInfo> getColumns(String tableName) {
        return baseDb.getColumns(tableName);
    }

    public String getDbUrl() {
        return baseDb.getDbUrl();
    }

    public String getJdbcDriver() {
        return baseDb.getJdbcDriver();
    }

    public String getUsername() {
        return baseDb.getUsername();
    }

    public String getPassword() {
        return baseDb.getPassword();
    }

    public void testConnect() {
        baseDb.testConnect();
    }
}
