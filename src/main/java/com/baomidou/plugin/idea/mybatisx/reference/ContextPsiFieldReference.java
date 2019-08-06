package com.baomidou.plugin.idea.mybatisx.reference;

import com.google.common.base.Optional;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import com.baomidou.plugin.idea.mybatisx.dom.MapperBacktrackingUtils;
import com.baomidou.plugin.idea.mybatisx.service.PlusJavaService;
import com.baomidou.plugin.idea.mybatisx.util.JavaUtils;
import com.baomidou.plugin.idea.mybatisx.util.MybatisConstants;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class ContextPsiFieldReference extends PsiReferenceBase<XmlAttributeValue> {

    protected ContextReferenceSetResolver resolver;

    protected int index;

    public ContextPsiFieldReference(XmlAttributeValue element, TextRange range, int index) {
        super(element, range, false);
        this.index = index;
        resolver = ReferenceSetResolverFactory.createPsiFieldResolver(element);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public PsiElement resolve() {
        Optional<PsiElement> resolved = resolver.resolve(index);
        return resolved.orNull();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        // <result column="batch" property="batch" /> property 提示
        Optional<PsiClass> clazz = getTargetClazz();
        if (clazz.isPresent()) {
           return JavaUtils.findSettablePsiFields(clazz.get());
        }else {
            return PsiReference.EMPTY_ARRAY;
        }
    }

    @SuppressWarnings("unchecked")
    private Optional<PsiClass> getTargetClazz() {
        if (getElement().getValue().contains(MybatisConstants.DOT_SEPARATOR)) {
            int ind = 0 == index ? 0 : index - 1;
            Optional<PsiElement> resolved = resolver.resolve(ind);
            if (resolved.isPresent()) {
                return PlusJavaService.getInstance(myElement.getProject()).getReferenceClazzOfPsiField(resolved.get());
            }
        } else {
            // show suggest   <id column="id" property="id"/> 的  property 自动提示
            return MapperBacktrackingUtils.getPropertyClazz(myElement);
        }
        return Optional.absent();
    }

    public ContextReferenceSetResolver getResolver() {
        return resolver;
    }

    public void setResolver(ContextReferenceSetResolver resolver) {
        this.resolver = resolver;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
