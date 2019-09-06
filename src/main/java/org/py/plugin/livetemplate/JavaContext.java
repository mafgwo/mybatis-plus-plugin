package org.py.plugin.livetemplate;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class JavaContext extends TemplateContextType {

    protected JavaContext() {
        super("JAVA", "Java");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        return file.getName().endsWith(".java");
    }
}
