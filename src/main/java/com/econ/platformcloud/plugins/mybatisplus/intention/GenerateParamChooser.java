package com.econ.platformcloud.plugins.mybatisplus.intention;

import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.econ.platformcloud.plugins.mybatisplus.annotation.Annotation;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateParamChooser extends AbstractJavaFileIntentionChooser {

    public static final AbstractJavaFileIntentionChooser INSTANCE = new GenerateParamChooser();

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
        PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        return (null != parameter && !JavaUtils.isAnnotationPresent(parameter, Annotation.PARAM)) ||
                (null != method && !JavaUtils.isAllParameterWithAnnotation(method, Annotation.PARAM));
    }
}
