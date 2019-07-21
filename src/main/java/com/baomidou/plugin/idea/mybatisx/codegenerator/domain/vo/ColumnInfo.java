package com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo;


/**
 * 列的数据信息
 */

public class ColumnInfo {

    /** 数据库字段名称 **/
    private String columnName;

    /** 允许空值 **/
    private String isNullable;

    /** 数据库字段类型 **/
    private String columnType;

    /** 数据库字段注释 **/
    private String columnComment;

    /** 数据库字段键类型 **/
    private String columnKey;

    /** 额外的参数 **/
    private String extra;

    /** 查询 1:模糊 2：精确 **/
    private String columnQuery;

    /** 是否在列表显示 **/
    private String columnShow;

    @Override
    public String toString() {
        return "ColumnInfo{" +
            "columnName=" + columnName +
            ", isNullable=" + isNullable +
            ", columnType=" + columnType +
            ", columnComment=" + columnComment +
            ", columnKey=" + columnKey +
            ", extra=" + extra +
            ", columnQuery='" + columnQuery + '\'' +
            ", columnShow='" + columnShow + '\'' +
            '}';
    }

    public ColumnInfo() {
    }

    public ColumnInfo(String columnName, String isNullable, String columnType, String columnComment, String columnKey, String extra, String columnQuery, String columnShow) {
        this.columnName = columnName;
        this.isNullable = isNullable;
        this.columnType = columnType;
        this.columnComment = columnComment;
        this.columnKey = columnKey;
        this.extra = extra;
        this.columnQuery = columnQuery;
        this.columnShow = columnShow;
    }

    public String getColumnName() {
        return columnName;
    }

    public ColumnInfo setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public ColumnInfo setIsNullable(String isNullable) {
        this.isNullable = isNullable;
        return this;
    }

    public String getColumnType() {
        return columnType;
    }

    public ColumnInfo setColumnType(String columnType) {
        this.columnType = columnType;
        return this;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public ColumnInfo setColumnComment(String columnComment) {
        this.columnComment = columnComment;
        return this;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public ColumnInfo setColumnKey(String columnKey) {
        this.columnKey = columnKey;
        return this;
    }

    public String getExtra() {
        return extra;
    }

    public ColumnInfo setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public ColumnInfo setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
        return this;
    }

    public String getColumnShow() {
        return columnShow;
    }

    public ColumnInfo setColumnShow(String columnShow) {
        this.columnShow = columnShow;
        return this;
    }
}
