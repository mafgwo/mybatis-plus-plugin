package com.econ.platformcloud.plugins.mybatisplus.generate;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.GroupTwo;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Insert 代码生成器
 * </p>
 *
 * @author yanglin jobob
 * @since 2018-07-30
 */
public class InsertGenerator extends AbstractStatementGenerator {

    public InsertGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
        return mapper.addInsert();
    }

    @NotNull
    @Override
    public String getId() {
        return "InsertGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Insert Statement";
    }
}
