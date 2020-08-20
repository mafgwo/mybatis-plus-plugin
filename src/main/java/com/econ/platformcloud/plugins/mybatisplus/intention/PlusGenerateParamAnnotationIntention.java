package com.econ.platformcloud.plugins.mybatisplus.intention;

import com.econ.platformcloud.plugins.mybatisplus.service.PlusAnnotationService;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class PlusGenerateParamAnnotationIntention extends AbstractGenericIntention {

    public PlusGenerateParamAnnotationIntention() {
        super(GenerateParamChooser.INSTANCE);
    }

    @NotNull
    @Override
    public String getText() {
        return "[Mybatis] Generate @Param";
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        PsiParameter parameter = PsiTreeUtil.getParentOfType(element, PsiParameter.class);
        PlusAnnotationService plusAnnotationService = PlusAnnotationService.getInstance(project);
        if (null != parameter) {
            plusAnnotationService.addAnnotationWithParameterName(parameter);
        } else {
            PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
            if (null != method) {
                plusAnnotationService.addAnnotationWithParameterNameForMethodParameters(method);
            }
        }
    }

}
