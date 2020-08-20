package com.econ.platformcloud.plugins.mybatisplus.dom.converter;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.IdDomElement;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.intellij.util.xml.ConvertContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author yanglin
 */
public class SqlConverter extends AbstractIdBasedTagConverter {

    @NotNull
    @Override
    public Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper, ConvertContext context) {
        return mapper.getSqls();
    }

}
