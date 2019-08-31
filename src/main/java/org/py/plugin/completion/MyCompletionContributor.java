package org.py.plugin.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.profiler.ui.flamechart.FlameChartColors;
import com.intellij.psi.impl.source.tree.JavaElementType;
import com.intellij.util.ProcessingContext;
import com.sun.istack.NotNull;

public class MyCompletionContributor extends CompletionContributor {
    public MyCompletionContributor(){
        extend( CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage( JavaLanguage.INSTANCE ),
            new CompletionProvider<CompletionParameters>()
            {
                @Override
                public void addCompletions(@NotNull CompletionParameters parameters,
                                           ProcessingContext context,
                                           @NotNull CompletionResultSet resultSet )
                {
//                    resultSet.addElement( LookupElementBuilder.create( "Hello" ).bold() );
                }
            }
        );
    }
}
