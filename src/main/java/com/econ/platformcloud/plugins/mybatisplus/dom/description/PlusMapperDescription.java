package com.econ.platformcloud.plugins.mybatisplus.dom.description;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.econ.platformcloud.plugins.mybatisplus.util.DomUtils;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * mapper.xml 文件属性提示
 * </p>
 *
 * @author yanglin jobob
 * @since 2018-07-30
 */
public class PlusMapperDescription extends DomFileDescription<Mapper> {

    public PlusMapperDescription() {
        super(Mapper.class, "mapper");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        return DomUtils.isMybatisFile(file);
    }

}
