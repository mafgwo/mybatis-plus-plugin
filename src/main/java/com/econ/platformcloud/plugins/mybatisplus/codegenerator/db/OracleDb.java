package com.econ.platformcloud.plugins.mybatisplus.codegenerator.db;

import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.vo.ColumnInfo;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.vo.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleDb extends BaseDb {
    private Logger logger = LoggerFactory.getLogger(OracleDb.class);

    @Override
    String getTableSql() {
        String username = getUsername().toUpperCase();
        return "SELECT table_name,table_type,comments FROM ALL_TAB_COMMENTS WHERE OWNER='" + username + "' ";
    }

    @Override
    String getColumnSql(String tableName) {
        String username = getUsername().toUpperCase();
        tableName = tableName.toUpperCase();
        return "SELECT A.NULLABLE,A.COLUMN_NAME, CASE WHEN A.DATA_TYPE='NUMBER' THEN (CASE WHEN A.DATA_PRECISION IS NULL THEN A.DATA_TYPE WHEN NVL(A.DATA_SCALE, 0) > 0 THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')' ELSE A.DATA_TYPE||'('||A.DATA_PRECISION||')' END) ELSE A.DATA_TYPE END DATA_TYPE, B.COMMENTS,DECODE(C.POSITION, '1', 'PRI') KEY FROM ALL_TAB_COLUMNS A  " +
            "INNER JOIN ALL_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME AND B.OWNER = '" + username + "' " +
            "LEFT JOIN ALL_CONSTRAINTS D ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' AND D.OWNER = '" + username + "' " +
            "LEFT JOIN ALL_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME AND C.COLUMN_NAME=A.COLUMN_NAME AND C.OWNER = '" + username + "'WHERE A.OWNER = '" + username + "' AND A.TABLE_NAME = '" + tableName + "' ORDER BY A.COLUMN_ID";
    }

    @Override
    public String dbName() {
        return "oracle";
    }

    @Override
    public List<TableInfo> getTableInfo() {
        List<TableInfo> tableInfos = new ArrayList<>();
        Connection conn;
        Statement stmt;
        try {
            Class.forName(getJdbcDriver());
            conn = DriverManager.getConnection(getDbUrl(), getUsername(), getPassword());
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getTableSql());
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                // todo 增加一下其他的信息
                String remark = rs.getString("comments");
                tableInfos.add(new TableInfo()
                    .setTableName(tableName)
                    .setCreateTime("")
                    .setEngine("")
                    .setCoding("")
                    .setRemark(remark == null ? "" : remark)
                );
            }
            stmt.close();
            conn.close();
            return tableInfos;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        Connection conn;
        Statement stmt;
        try {
            Class.forName(getJdbcDriver());
            conn = DriverManager.getConnection(getDbUrl(), getUsername(), getPassword());
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getColumnSql(tableName));
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String isNullable = rs.getString("NULLABLE");
                String dataType = rs.getString("DATA_TYPE");
                String columnComment = rs.getString("COMMENTS");
                String columnKey = rs.getString("key");
                // todo 增加一下其他的信息
                columnInfoList.add(new ColumnInfo().setColumnName(columnName)
                    .setIsNullable(isNullable)
                    .setColumnType(dataType)
                    .setColumnComment(columnComment)
                    .setColumnKey(columnKey)
                    .setExtra(""));
            }
            stmt.close();
            conn.close();
            return columnInfoList;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    @Override
    public String getJdbcDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
