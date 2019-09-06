package org.py.plugin.livetemplate.xml;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class XmlContext extends TemplateContextType {

    protected XmlContext() {
        super("SQL", "Sql");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        return file.getName().endsWith(".xml");
    }
}
