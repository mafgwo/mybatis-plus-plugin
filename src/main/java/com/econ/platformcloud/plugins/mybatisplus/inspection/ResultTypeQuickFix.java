package com.econ.platformcloud.plugins.mybatisplus.inspection;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.GenericAttributeValue;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Select;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * ResultType 检查
 * </p>
 *
 * @author yanglin
 * @since 2018-07-30
 */
public class ResultTypeQuickFix extends AbstractGenericQuickFix {

    private Select select;
    private PsiClass target;

    public ResultTypeQuickFix(@NotNull Select select, @NotNull PsiClass target) {
        this.select = select;
        this.target = target;
    }

    @NotNull
    @Override
    public String getName() {
        return "Correct resultType";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        GenericAttributeValue<PsiClass> resultType = select.getResultType();
        resultType.setValue(target);
    }

    @NotNull
    public PsiClass getTarget() {
        return target;
    }

    public void setTarget(@NotNull PsiClass target) {
        this.target = target;
    }

    @NotNull
    public Select getSelect() {
        return select;
    }

    public void setSelect(@NotNull Select select) {
        this.select = select;
    }
}
