package com.econ.platformcloud.plugins.mybatisplus.dom.model;

import com.econ.platformcloud.plugins.mybatisplus.dom.converter.SqlConverter;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * @author yanglin
 */
public interface Include extends DomElement {

    @Attribute("refid")
    @Convert(SqlConverter.class)
    GenericAttributeValue<XmlTag> getRefId();

}
