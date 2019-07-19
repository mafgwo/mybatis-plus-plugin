package com.baomidou.plugin.idea.mybatisx.action;

import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.GenConfig;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.GenUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // todo 1 获取数据库 eladmin 读取数据库信息
        //  配置生成的位置
        //  修改ftl文件
        String tableName = "unit";
        GenConfig genConfig = new GenConfig();
        genConfig.setId(1L);
        genConfig.setPack("me.zhengjie.modules.test");
        genConfig.setModuleName("eladmin-system");
//        genConfig.setPath("E:\\workspace\\me\\front\\eladmin-qt\\src\\views\\test");
//        genConfig.setApiPath("E:\\workspace\\me\\front\\eladmin-qt\\src\\api");
        genConfig.setAuthor("hj");
        genConfig.setCover(false);

        List<ColumnInfo> columnInfos = new ArrayList<>();
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setColumnName("id");
        columnInfo.setColumnType("bigint");
        columnInfo.setColumnKey("PRI");
        columnInfo.setExtra("auto_increment");
        columnInfo.setColumnShow("true");
        columnInfo.setColumnComment("123");
        columnInfo.setIsNullable("NO");
        columnInfos.add(columnInfo);

        try {
            GenUtil.generatorCode(columnInfos,genConfig,tableName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
