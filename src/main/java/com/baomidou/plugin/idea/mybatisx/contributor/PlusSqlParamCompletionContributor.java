package com.baomidou.plugin.idea.mybatisx.contributor;

import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.baomidou.plugin.idea.mybatisx.util.DomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  xml 里的 sql #{auto completion}
 */
public class PlusSqlParamCompletionContributor extends CompletionContributor {

    private Logger log = LoggerFactory.getLogger(PlusSqlParamCompletionContributor.class);

    private Map<String,List<String>> suggestMaps = new HashMap<>();
    public PlusSqlParamCompletionContributor(){
        List<TableInfo> tableInfos = MysqlUtil.getInstance().getTableInfo();
        log.info("PlusSqlParamCompletionContributor:"+tableInfos);
        for (TableInfo tableInfo : tableInfos) {
            List<ColumnInfo> columns = MysqlUtil.getInstance().getColumns(tableInfo.getTableName());
            List<String> suggests = new ArrayList<>();
            for (ColumnInfo column : columns) {
                suggests.add(column.getColumnName());
                log.info("column" + column.getColumnName());
            }
            String tableName = tableInfo.getTableName().replaceAll("_","").toLowerCase();
            suggestMaps.put(tableName, suggests);
            log.info("tableName" + tableName);
        }
    }

    @Override
    public void fillCompletionVariants(CompletionParameters parameters, final CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        PsiElement position = parameters.getPosition();

//        PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(position);
        PsiFile topLevelFile = InjectedLanguageManager.getInstance(position.getProject()).getTopLevelFile(position);
        if (DomUtils.isMybatisFile(topLevelFile)) {
            if (shouldAddElement(position.getContainingFile(), parameters.getOffset())) {
                process(topLevelFile, result, position);
            }
        }
    }

    private void process(PsiFile xmlFile, CompletionResultSet resultSet, PsiElement position) {
        int mapperIndex = xmlFile.getName().indexOf("Mapper");
        String tableName = xmlFile.getName().substring(0, mapperIndex);
        List<String> suggests = suggestMaps.get(tableName.toLowerCase());
        if (null != suggests) {
            for (String suggest : suggests) {
                log.info("suggest:" + suggest);
                resultSet.addElement(LookupElementBuilder.create(suggest));
            }
        }
    }

    private boolean shouldAddElement(PsiFile file, int offset) {
        String text = file.getText();
        for (int i = offset - 1; i > 0; i--) {
            char c = text.charAt(i);
            if (c == '{' && text.charAt(i - 1) == '#') {
                return true;
            }
        }
        return false;
    }
}
