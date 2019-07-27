package com.baomidou.plugin.idea.mybatisx.codegenerator;

import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.jdbcDrivers;

public class MysqlUtil {

    Logger logger = LoggerFactory.getLogger(MysqlUtil.class);

    private MysqlUtil(){}

    public int getJdbcDriver() {
        int jdbcDriverIndex = PropertiesComponent.getInstance().getInt(MybatisConst.PLUS_JDBCDRIVER, 0);
        return jdbcDriverIndex;
    }

    public String getDbUrl() {
        String dbUrl = PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_DBURL);
        return dbUrl;
    }

    public String getUsername() {
        String username = PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_USERNAME);
        return username;
    }

    public String getPassword() {
        String password = PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_PASSWORD);
        return password;
    }

    private  static MysqlUtil mysqlUtil;

    public static MysqlUtil getInstance() {
        if (mysqlUtil == null) {
            mysqlUtil = new MysqlUtil();
        }
        return mysqlUtil;
    }

    public static void main(String[] args) {
        List<TableInfo> tableInfoList = MysqlUtil.getInstance().getTableInfo();
        tableInfoList.forEach(item->{
            System.out.println(item);
        });
        List<ColumnInfo> columnInfoList = MysqlUtil.getInstance().getColumns("unit");
        columnInfoList.forEach(item->{
            System.out.println(item);
        });
    }

    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
            "where table_name = '"+tableName+"' and table_schema = (select database()) order by ordinal_position";
        Connection conn;
        Statement stmt;
        try {
            Class.forName(jdbcDrivers[getJdbcDriver()]);
            conn = DriverManager.getConnection(getDbUrl(),getUsername(),getPassword());
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String columnName  = rs.getString("column_name");
                String isNullable = rs.getString("is_nullable");
                String dataType = rs.getString("data_type");
                String columnComment = rs.getString("column_comment");
                String columnKey = rs.getString("column_key");
                String extra = rs.getString("extra");
                columnInfoList.add(new ColumnInfo().setColumnName(columnName)
                    .setIsNullable(isNullable)
                    .setColumnType(dataType)
                    .setColumnComment(columnComment)
                    .setColumnKey(columnKey)
                    .setExtra(extra) );

            }
            stmt.close();
            conn.close();
            return columnInfoList;
        } catch (SQLException e) {
            logger.error(e.toString());
        } catch (ClassNotFoundException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    public List<TableInfo> getTableInfo() {
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables\n" +
            "where table_schema = (select database())";
        List<TableInfo> tableInfos = new ArrayList<>();
        Connection conn;
        Statement stmt;
        try {
            Class.forName(jdbcDrivers[getJdbcDriver()]);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(getDbUrl(),getUsername(),getPassword());
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                String tableName  = rs.getString("table_name");
                String createTime = rs.getString("create_time");
                String engine = rs.getString("engine");
                String coding = rs.getString("table_collation");
                String remark = rs.getString("table_comment");
                tableInfos.add(new TableInfo()
                    .setTableName(tableName)
                    .setCreateTime(createTime)
                    .setEngine(engine)
                    .setCoding(coding)
                    .setRemark(remark)
                );
            }
            stmt.close();
            conn.close();
            return tableInfos;
        } catch (SQLException e) {
            logger.error(e.toString());
        } catch (ClassNotFoundException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    public void testConnect() {
        String result = "test successful！";
        try {
            Class.forName(jdbcDrivers[getJdbcDriver()]);
            logger.info("connect mysql...");
            DriverManager.getConnection(getDbUrl(),getUsername(),getPassword());
        } catch (ClassNotFoundException e) {
            result = e.getMessage();
            logger.error(e.toString());
        } catch (SQLException e) {
            result = e.getMessage();
            logger.error(e.toString());
        }
        Messages.showInfoMessage(result, "mybatis plus");
    }
}
