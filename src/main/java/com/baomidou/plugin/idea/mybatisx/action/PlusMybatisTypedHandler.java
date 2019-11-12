package com.baomidou.plugin.idea.mybatisx.action;

import org.jetbrains.annotations.NotNull;

import com.baomidou.plugin.idea.mybatisx.util.DomUtils;
import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.AppUIExecutor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

/**
 * xml 文件修改提示
 * @author yanglin
 */
public class PlusMybatisTypedHandler extends TypedHandlerDelegate {

    @NotNull
    @Override
    public Result checkAutoPopup(char charTyped, @NotNull final Project project, @NotNull final Editor editor, @NotNull PsiFile file) {
        if (charTyped == '.' && DomUtils.isMybatisFile(file)) {
            autoPopupParameter(project, editor);
            return Result.STOP;
        }
        return super.checkAutoPopup(charTyped, project, editor, file);
    }

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull final Project project, @NotNull final Editor editor, @NotNull PsiFile file) {
        int index = editor.getCaretModel().getOffset() - 2;
        PsiFile topLevelFile = InjectedLanguageManager.getInstance(project).getTopLevelFile(file);
//        PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(file);
        boolean parameterCase = c == '{' &&
                index >= 0 &&
                editor.getDocument().getText().charAt(index) == '#';

        boolean mybatisFile = DomUtils.isMybatisFile(topLevelFile);

        if (parameterCase && mybatisFile) {
            autoPopupParameter(project, editor);
            return Result.STOP;
        } else if (!parameterCase && mybatisFile) {
            return Result.STOP;
        }
        return super.charTyped(c, project, editor, file);
    }

    private static void autoPopupParameter(final Project project, final Editor editor) {
        AppUIExecutor.onUiThread().later().withDocumentsCommitted(project).inTransaction(project).execute(new Runnable() {
            @Override
            public void run() {
                if (PsiDocumentManager.getInstance(project).isCommitted(editor.getDocument())) {
                    new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project, editor, 1);
                }
            }
        });
    }

}
