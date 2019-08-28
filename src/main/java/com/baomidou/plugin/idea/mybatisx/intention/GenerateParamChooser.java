package com.baomidou.plugin.idea.mybatisx.intention;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.baomidou.plugin.idea.mybatisx.annotation.Annotation;
import com.baomidou.plugin.idea.mybatisx.util.JavaUtils;

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
