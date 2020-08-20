package com.econ.platformcloud.plugins.mybatisplus.generate;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.GroupTwo;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * 删除生成器
 * </p>
 *
 * @author yanglin jobob
 * @since 2018-07-30
 */
public class DeleteGenerator extends AbstractStatementGenerator {

    public DeleteGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
        return mapper.addDelete();
    }

    @NotNull
    @Override
    public String getId() {
        return "DeleteGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Delete Statement";
    }

}
