package com.econ.platformcloud.plugins.mybatisplus.contributor;

import com.econ.platformcloud.plugins.mybatisplus.annotation.Annotation;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.IdDomElement;
import com.econ.platformcloud.plugins.mybatisplus.util.Icons;
import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.econ.platformcloud.plugins.mybatisplus.util.MybatisConstants;
import com.google.common.base.Optional;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yanglin
 */
public class PlusXmlParamContributor extends CompletionContributor {

    private static final Logger logger = LoggerFactory.getLogger(PlusXmlParamContributor.class);

    public PlusXmlParamContributor() {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement().inside(PlatformPatterns.get("select")),
            new CompletionProvider<CompletionParameters>() {
                @Override
                public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
//                        resultSet.addElement(LookupElementBuilder.create("Hello I am completion"));
//                        resultSet.addElement(LookupElementBuilder.create("OK i am 2"));
//                        PsiElement position = parameters.getPosition();
//                        addElementForPsiParameter(position.getProject(), result, MapperUtils.findParentIdDomElement(position).orNull());
                }
            });
    }

    public static void addElementForPsiParameter(@NotNull Project project, @NotNull CompletionResultSet result, @Nullable IdDomElement element) {
        if (null == element) {
            return;
        }
        PsiMethod psiMethod = JavaUtils.findMethod(project, element).orNull();

        if (null == psiMethod) {
            logger.info("psiMethod null");
            return;
        }

        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();

        for (PsiParameter parameter : psiParameters) {
            Optional<String> valueText = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
            if (valueText.isPresent()) {
                LookupElementBuilder builder = LookupElementBuilder.create(valueText.get()).withIcon(Icons.PARAM_COMPLETION_ICON);
                result.addElement(PrioritizedLookupElement.withPriority(builder, MybatisConstants.PRIORITY));
            }
        }
    }
}
