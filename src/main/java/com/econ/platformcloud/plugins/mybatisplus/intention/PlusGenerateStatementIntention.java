package com.econ.platformcloud.plugins.mybatisplus.intention;

import com.econ.platformcloud.plugins.mybatisplus.generate.AbstractStatementGenerator;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class PlusGenerateStatementIntention extends AbstractGenericIntention {

    public PlusGenerateStatementIntention() {
        super(GenerateStatementChooser.INSTANCE);
    }

    @NotNull
    @Override
    public String getText() {
        return "[Mybatis] Generate new statement";
    }

    @Override
    public void invoke(@NotNull final Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        AbstractStatementGenerator.applyGenerate(PsiTreeUtil.getParentOfType(element, PsiMethod.class));
    }

}
