package com.econ.platformcloud.plugins.mybatisplus.dom.model;

import com.econ.platformcloud.plugins.mybatisplus.dom.converter.PropertyConverter;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface PropertyGroup extends DomElement {

    @Attribute("property")
    @Convert(PropertyConverter.class)
    GenericAttributeValue<XmlAttributeValue> getProperty();
}
