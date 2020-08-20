package com.econ.platformcloud.plugins.mybatisplus.provider;

import java.util.Collection;

import com.econ.platformcloud.plugins.mybatisplus.service.PlusJavaService;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.IdDomElement;
import com.econ.platformcloud.plugins.mybatisplus.util.Icons;
import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.Collections2;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.CommonProcessors;

/**
 * @author yanglin
 * mapper.xml文件到mapper.java
 */
public class PlusMapperLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element instanceof PsiNameIdentifierOwner && JavaUtils.isElementWithinInterface(element)) {
            CommonProcessors.CollectProcessor<IdDomElement> processor = new CommonProcessors.CollectProcessor<IdDomElement>();
            PlusJavaService.getInstance(element.getProject()).process(element, processor);
            Collection<IdDomElement> results = processor.getResults();
            if (!results.isEmpty()) {
                NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(Icons.MAPPER_LINE_MARKER_ICON)
                        .setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTargets(Collections2.transform(results, domElement -> domElement.getXmlTag()))
                        .setTooltipTitle("Navigation to target in mapper xml");
                result.add(builder.createLineMarkerInfo(((PsiNameIdentifierOwner) element).getNameIdentifier()));
            }
        }
    }
}
