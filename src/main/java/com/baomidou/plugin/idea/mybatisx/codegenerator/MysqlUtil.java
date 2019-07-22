package com.baomidou.plugin.idea.mybatisx.codegenerator;

import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst;
import com.intellij.ide.util.PropertiesComponent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.jdbcDrivers;

public class MysqlUtil {

    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
//    private static String jdbcDriver = "";
//    private static String dbUrl = "";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //private String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //private String DB_URL = "jdbc:mysql://localhost:3306/unionfund?useSSL=false&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
//    private static String username = "root";
//    private static String password = "123456";

    public int getJdbcDriver() {
        int jdbcDriverIndex = PropertiesComponent.getInstance().getInt(MybatisConst.MYBATISPLUS_JDBCDRIVER, 0);
        return jdbcDriverIndex;
    }



    public String getDbUrl() {
        String dbUrl = PropertiesComponent.getInstance().getValue(MybatisConst.MYBATISPLUS_DBURL);
        return dbUrl;
    }



    public String getUsername() {
        String username = PropertiesComponent.getInstance().getValue(MybatisConst.MYBATISPLUS_USERNAME);
        return username;
    }

    public String getPassword() {
        String password = PropertiesComponent.getInstance().getValue(MybatisConst.MYBATISPLUS_PASSWORD);
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
            // 注册 JDBC 驱动
            Class.forName(jdbcDrivers[getJdbcDriver()]);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(getDbUrl(),getUsername(),getPassword());
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            // 完成后关闭
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            // 注册 JDBC 驱动
            Class.forName(jdbcDrivers[getJdbcDriver()]);
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(getDbUrl(),getUsername(),getPassword());
            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            // 完成后关闭
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean testConnect() {
        // 注册 JDBC 驱动
        try {
            Class.forName(jdbcDrivers[getJdbcDriver()]);
            // 打开链接
            System.out.println("connect mysql...");
            DriverManager.getConnection(getDbUrl(),getUsername(),getPassword());
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
