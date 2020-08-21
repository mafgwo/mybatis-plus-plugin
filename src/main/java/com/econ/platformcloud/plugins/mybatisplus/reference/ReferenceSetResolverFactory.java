package com.econ.platformcloud.plugins.mybatisplus.reference;

import com.intellij.psi.xml.XmlAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public final class ReferenceSetResolverFactory {

    private ReferenceSetResolverFactory() {
        throw new UnsupportedOperationException();
    }

    public static <F extends XmlAttributeValue> AbstractContextReferenceSetResolver createPsiFieldResolver(@NotNull F target) {
        return new PsiFieldReferenceSetResolverAbstract(target);
    }

}