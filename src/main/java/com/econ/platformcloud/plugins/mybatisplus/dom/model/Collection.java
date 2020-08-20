package com.econ.platformcloud.plugins.mybatisplus.dom.model;

import com.econ.platformcloud.plugins.mybatisplus.dom.converter.AliasConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface Collection extends GroupFour, ResultMapGroup, PropertyGroup {

    @NotNull
    @Attribute("ofType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getOfType();

}
