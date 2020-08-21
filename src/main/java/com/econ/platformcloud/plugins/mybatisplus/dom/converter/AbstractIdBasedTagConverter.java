package com.econ.platformcloud.plugins.mybatisplus.dom.converter;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.IdDomElement;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.econ.platformcloud.plugins.mybatisplus.util.MapperUtils;
import com.econ.platformcloud.plugins.mybatisplus.util.MybatisConstants;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yanglin
 */
public abstract class AbstractIdBasedTagConverter extends AbstractConverterAdaptor<XmlAttributeValue> implements CustomReferenceConverter<XmlAttributeValue> {

    private final boolean crossMapperSupported;

    public AbstractIdBasedTagConverter() {
        this(true);
    }

    protected AbstractIdBasedTagConverter(boolean crossMapperSupported) {
        this.crossMapperSupported = crossMapperSupported;
    }

    @Nullable
    @Override
    public XmlAttributeValue fromString(@Nullable @NonNls String value, ConvertContext context) {
        return matchIdDomElement(selectStrategy(context).getValue(), value, context).orNull();
    }

    @NotNull
    private Optional<XmlAttributeValue> matchIdDomElement(Collection<? extends IdDomElement> idDomElements, String value, ConvertContext context) {
        Mapper contextMapper = MapperUtils.getMapper(context.getInvocationElement());
        for (IdDomElement idDomElement : idDomElements) {
            if (MapperUtils.getIdSignature(idDomElement).equals(value) ||
                MapperUtils.getIdSignature(idDomElement, contextMapper).equals(value)) {
                return Optional.of(idDomElement.getId().getXmlAttributeValue());
            }
        }
        return Optional.absent();
    }

    @Nullable
    @Override
    public String toString(@Nullable XmlAttributeValue xmlAttribute, ConvertContext context) {
        DomElement domElement = DomUtil.getDomElement(xmlAttribute.getParent().getParent());
        if (!(domElement instanceof IdDomElement)) {
            return null;
        }
        Mapper contextMapper = MapperUtils.getMapper(context.getInvocationElement());
        return MapperUtils.getIdSignature((IdDomElement) domElement, contextMapper);
    }

    private AbstractTraverseStrategy selectStrategy(ConvertContext context) {
        return crossMapperSupported ? new CrossMapperStrategy(context) : new InsideMapperStrategy(context);
    }

    /**
     * @param mapper  mapper in the project, null if {@link #crossMapperSupported} is false
     * @param context the dom convert context
     */
    @NotNull
    public abstract Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper, ConvertContext context);

    private abstract class AbstractTraverseStrategy {
        protected ConvertContext context;

        AbstractTraverseStrategy(@NotNull ConvertContext context) {
            this.context = context;
        }

        public abstract Collection<? extends IdDomElement> getValue();
    }

    private class InsideMapperStrategy extends AbstractTraverseStrategy {

        InsideMapperStrategy(@NotNull ConvertContext context) {
            super(context);
        }

        @Override
        public Collection<? extends IdDomElement> getValue() {
            return getComparisons(null, context);
        }

    }

    private class CrossMapperStrategy extends AbstractTraverseStrategy {

        CrossMapperStrategy(@NotNull ConvertContext context) {
            super(context);
        }

        @Override
        public Collection<? extends IdDomElement> getValue() {
            // 1
            List<IdDomElement> result = Lists.newArrayList();
            for (Mapper mapper : MapperUtils.findMappers(context.getProject())) {
                result.addAll(getComparisons(mapper, context));
            }
            return result;
        }

    }

    @NotNull
    @Override
    public PsiReference[] createReferences(GenericDomValue<XmlAttributeValue> value, PsiElement element, ConvertContext context) {
        return PsiClassConverter.createJavaClassReferenceProvider(value, null, new ValueReferenceProvider(context)).getReferencesByElement(element);
    }

    private class ValueReferenceProvider extends JavaClassReferenceProvider {

        private ConvertContext context;

        private ValueReferenceProvider(ConvertContext context) {
            this.context = context;
        }

        @Nullable
        @Override
        public GlobalSearchScope getScope(Project project) {
            return GlobalSearchScope.allScope(project);
        }

        /**
         * It looks like hacking here, as it's a little hard to handle so many different cases as JetBrains does
         */
        @NotNull
        @Override
        public PsiReference[] getReferencesByString(String text, @NotNull PsiElement position, int offsetInPosition) {
            List<PsiReference> refs = Lists.newArrayList(super.getReferencesByString(text, position, offsetInPosition));
            ValueReference vr = new ValueReference(position, getTextRange(position), context, text);
            if (!refs.isEmpty() && 0 != vr.getVariants().length) {
                refs.remove(refs.size() - 1);
                refs.add(vr);
            }
            return refs.toArray(new PsiReference[refs.size()]);
        }

        private TextRange getTextRange(PsiElement element) {
            String text = element.getText();
            int index = text.lastIndexOf(MybatisConstants.DOT_SEPARATOR);
            return -1 == index ? ElementManipulators.getValueTextRange(element) : TextRange.create(text.substring(0, index).length() + 1, text.length() - 1);
        }
    }

    private class ValueReference extends PsiReferenceBase<PsiElement> {

        private ConvertContext context;
        private String text;

        ValueReference(@NotNull PsiElement element, TextRange rng, ConvertContext context, String text) {
            super(element, rng, false);
            this.context = context;
            this.text = text;
        }

        @Nullable
        @Override
        public PsiElement resolve() {
            return AbstractIdBasedTagConverter.this.fromString(text, context);
        }

        @NotNull
        @Override
        public Object[] getVariants() {
            Set<String> res = getElement().getText().contains(MybatisConstants.DOT_SEPARATOR) ? setupContextIdSignature() : setupGlobalIdSignature();
            return res.toArray(new String[res.size()]);
        }

        private Set<String> setupContextIdSignature() {
            Set<String> res = Sets.newHashSet();
            String ns = text.substring(0, text.lastIndexOf(MybatisConstants.DOT_SEPARATOR));
            for (IdDomElement ele : selectStrategy(context).getValue()) {
                if (MapperUtils.getNamespace(ele).equals(ns)) {
                    res.add(MapperUtils.getId(ele));
                }
            }
            return res;
        }

        private Set<String> setupGlobalIdSignature() {
            Mapper contextMapper = MapperUtils.getMapper(context.getInvocationElement());
            Collection<? extends IdDomElement> idDomElements = selectStrategy(context).getValue();
            Set<String> res = new HashSet<String>(idDomElements.size());
            for (IdDomElement ele : idDomElements) {
                res.add(MapperUtils.getIdSignature(ele, contextMapper));
            }
            return res;
        }

    }

}