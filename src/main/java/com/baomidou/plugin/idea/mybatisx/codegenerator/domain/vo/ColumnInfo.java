package com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo;


/**
 * 列的数据信息
 */

public class ColumnInfo {

    /** 数据库字段名称 **/
    private Object columnName;

    /** 允许空值 **/
    private Object isNullable;

    /** 数据库字段类型 **/
    private Object columnType;

    /** 数据库字段注释 **/
    private Object columnComment;

    /** 数据库字段键类型 **/
    private Object columnKey;

    /** 额外的参数 **/
    private Object extra;

    /** 查询 1:模糊 2：精确 **/
    private String columnQuery;

    /** 是否在列表显示 **/
    private String columnShow;

    public ColumnInfo() {
    }

    public ColumnInfo(Object columnName, Object isNullable, Object columnType, Object columnComment, Object columnKey, Object extra, String columnQuery, String columnShow) {
        this.columnName = columnName;
        this.isNullable = isNullable;
        this.columnType = columnType;
        this.columnComment = columnComment;
        this.columnKey = columnKey;
        this.extra = extra;
        this.columnQuery = columnQuery;
        this.columnShow = columnShow;
    }

    public Object getColumnName() {
        return columnName;
    }

    public void setColumnName(Object columnName) {
        this.columnName = columnName;
    }

    public Object getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(Object isNullable) {
        this.isNullable = isNullable;
    }

    public Object getColumnType() {
        return columnType;
    }

    public void setColumnType(Object columnType) {
        this.columnType = columnType;
    }

    public Object getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(Object columnComment) {
        this.columnComment = columnComment;
    }

    public Object getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(Object columnKey) {
        this.columnKey = columnKey;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public String getColumnQuery() {
        return columnQuery;
    }

    public void setColumnQuery(String columnQuery) {
        this.columnQuery = columnQuery;
    }

    public String getColumnShow() {
        return columnShow;
    }

    public void setColumnShow(String columnShow) {
        this.columnShow = columnShow;
    }
}
