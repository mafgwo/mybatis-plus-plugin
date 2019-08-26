package com.baomidou.plugin.idea.mybatisx.codegenerator.setting;

import com.baomidou.plugin.idea.mybatisx.generate.AbstractGenerateModel;
import com.baomidou.plugin.idea.mybatisx.generate.AbstractStatementGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

import static com.baomidou.plugin.idea.mybatisx.generate.AbstractStatementGenerator.*;

/**
 *
 */
@State(
    name = "MybatisSettings",
    storages = @Storage(value = "$APP_CONFIG$/mybatis-plus-config.xml"))
public class MysqlSetting implements PersistentStateComponent<Element> {

    private AbstractGenerateModel statementAbstractGenerateModel;

    private Gson gson = new Gson();

    private Type gsonTypeToken = new TypeToken<Set<String>>() {
    }.getType();

    public MysqlSetting() {
        statementAbstractGenerateModel = AbstractGenerateModel.START_WITH_MODEL;
    }

    public static MysqlSetting getInstance() {
        return ServiceManager.getService(MysqlSetting.class);
    }

    /**
     * 保存到文件
     */
    @Nullable
    @Override
    public Element getState() {
        Element element = new Element("MybatisSettings");
        element.setAttribute(INSERT_GENERATOR.getId(), gson.toJson(INSERT_GENERATOR.getPatterns()));
        element.setAttribute(DELETE_GENERATOR.getId(), gson.toJson(DELETE_GENERATOR.getPatterns()));
        element.setAttribute(UPDATE_GENERATOR.getId(), gson.toJson(UPDATE_GENERATOR.getPatterns()));
        element.setAttribute(SELECT_GENERATOR.getId(), gson.toJson(SELECT_GENERATOR.getPatterns()));
        element.setAttribute("statementAbstractGenerateModel", String.valueOf(statementAbstractGenerateModel.getIdentifier()));
        return element;
    }

    /**
     * 从文件读取数据
     *
     * @param state
     */
    @Override
    public void loadState(Element state) {
        loadState(state, INSERT_GENERATOR);
        loadState(state, DELETE_GENERATOR);
        loadState(state, UPDATE_GENERATOR);
        loadState(state, SELECT_GENERATOR);
        statementAbstractGenerateModel = AbstractGenerateModel.getInstance(state.getAttributeValue("statementAbstractGenerateModel"));
    }

    private void loadState(Element state, AbstractStatementGenerator generator) {
        String attribute = state.getAttributeValue(generator.getId());
        if (null != attribute) {
            generator.setPatterns((Set<String>) gson.fromJson(attribute, gsonTypeToken));
        }
    }

    public AbstractGenerateModel getStatementAbstractGenerateModel() {
        return statementAbstractGenerateModel;
    }

    public void setStatementAbstractGenerateModel(AbstractGenerateModel statementAbstractGenerateModel) {
        this.statementAbstractGenerateModel = statementAbstractGenerateModel;
    }
}
