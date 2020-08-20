package com.econ.platformcloud.plugins.mybatisplus.intention;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateMapperChooser extends AbstractJavaFileIntentionChooser {

    public static final AbstractJavaFileIntentionChooser INSTANCE = new GenerateMapperChooser();

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
        if (isPositionOfInterfaceDeclaration(element)) {
            PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            if (null != clazz) {
                return !isTargetPresentInXml(clazz);
            }
        }
        return false;
    }

}
