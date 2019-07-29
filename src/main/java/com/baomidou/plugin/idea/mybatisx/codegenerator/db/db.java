package com.baomidou.plugin.idea.mybatisx.codegenerator.db;

import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;

import java.util.List;

public interface db {

    List<TableInfo> getTableInfo();

    List<ColumnInfo> getColumns(String tableName);

    void testConnect();
}
