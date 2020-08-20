package com.econ.platformcloud.plugins.mybatisplus.dom.model;

import com.econ.platformcloud.plugins.mybatisplus.dom.converter.ResultMapConverter;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public interface ResultMapGroup extends DomElement {

    @NotNull
    @Attribute("resultMap")
    @Convert(ResultMapConverter.class)
    GenericAttributeValue<XmlTag> getResultMap();
}
