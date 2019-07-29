package com.baomidou.plugin.idea.mybatisx.codegenerator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.plugin.idea.mybatisx.codegenerator.db.BaseDb;
import com.baomidou.plugin.idea.mybatisx.codegenerator.db.MysqlDb;
import com.baomidou.plugin.idea.mybatisx.codegenerator.db.OracleDb;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst;
import com.intellij.ide.util.PropertiesComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.dbTypeDriver;

public class MysqlUtil {

    private Logger logger = LoggerFactory.getLogger(MysqlUtil.class);

    private BaseDb baseDb;

    private MysqlUtil(){
       resetDbInfo();
    }

    public void resetDbInfo() {
        int dbType = PropertiesComponent.getInstance().getInt(MybatisConst.PLUS_DBTYPE,0);
        if ("mysql".equals(dbTypeDriver[dbType].getName())) {
            baseDb = new MysqlDb();
            baseDb.setDbUrl(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_DBURL));
            baseDb.setUsername(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_USERNAME));
            baseDb.setPassword(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_PASSWORD));
        } else if ("oracle".equals(dbTypeDriver[dbType].getName())) {
            baseDb = new OracleDb();
            baseDb.setDbUrl(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_DBURL));
            baseDb.setUsername(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_USERNAME));
            baseDb.setPassword(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_PASSWORD));
        }
    }

    private  static MysqlUtil mysqlUtil;

    public static MysqlUtil getInstance() {
        if (mysqlUtil == null) {
            mysqlUtil = new MysqlUtil();
        }
        return mysqlUtil;
    }

    public static void main(String[] args) {
//        List<TableInfo> tableInfoList = MysqlUtil.getInstance().getTableInfo();
//        tableInfoList.forEach(item->{
//            System.out.println(item);
//        });
//        List<ColumnInfo> columnInfoList = MysqlUtil.getInstance().getColumns("unit");
//        columnInfoList.forEach(item->{
//            System.out.println(item);
//        });

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("123456");
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/mysql?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
        dataSourceConfig.getConn();
        dataSourceConfig.getDbQuery().tablesSql();
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
