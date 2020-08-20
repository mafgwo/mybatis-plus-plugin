package com.econ.platformcloud.plugins.mybatisplus.dom.converter;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.intellij.psi.PsiMethod;
import com.intellij.util.xml.ConvertContext;
import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.econ.platformcloud.plugins.mybatisplus.util.MapperUtils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class DaoMethodConverter extends AbstractConverterAdaptor<PsiMethod> {

    @Nullable
    @Override
    public PsiMethod fromString(@Nullable @NonNls String id, ConvertContext context) {
        Mapper mapper = MapperUtils.getMapper(context.getInvocationElement());
        return JavaUtils.findMethod(context.getProject(), MapperUtils.getNamespace(mapper), id).orNull();
    }

}
