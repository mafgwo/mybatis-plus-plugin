package com.econ.platformcloud.plugins.mybatisplus.dom.model;

import com.econ.platformcloud.plugins.mybatisplus.dom.converter.AliasConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.SubTagList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author yanglin
 */
public interface ParameterMap extends IdDomElement {

    @NotNull
    @Attribute("type")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getType();

    @SubTagList("parameter")
    List<Parameter> getParameters();

}
