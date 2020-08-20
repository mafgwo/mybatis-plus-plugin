package com.econ.platformcloud.plugins.mybatisplus.dom.model;

import com.econ.platformcloud.plugins.mybatisplus.dom.converter.AliasConverter;
import com.econ.platformcloud.plugins.mybatisplus.dom.converter.ResultMapConverter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface ResultMap extends GroupFour, IdDomElement {

    @NotNull
    @Attribute("extends")
    @Convert(ResultMapConverter.class)
    GenericAttributeValue<XmlAttributeValue> getExtends();

    @NotNull
    @Attribute("type")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getType();

}
