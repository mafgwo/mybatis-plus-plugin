package com.econ.platformcloud.plugins.mybatisplus.generate;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.GroupTwo;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Select;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * <p>
 * Select 代码生成器
 * </p>
 *
 * @author yanglin jobob
 * @since 2018-07-30
 */
public class SelectGenerator extends AbstractStatementGenerator {

    public SelectGenerator(@NotNull String... patterns) {
        super(patterns);
    }

    @NotNull
    @Override
    protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
        Select select = mapper.addSelect();
        setupResultType(method, select);
        return select;
    }

    private void setupResultType(PsiMethod method, Select select) {
        Optional<PsiClass> clazz = getSelectResultType(method);
        if (clazz.isPresent()) {
            //设置xmlMapper的返回值
            select.getResultType().setValue(clazz.get());
        }
    }

    @NotNull
    @Override
    public String getId() {
        return "SelectGenerator";
    }

    @NotNull
    @Override
    public String getDisplayText() {
        return "Select Statement";
    }
}
