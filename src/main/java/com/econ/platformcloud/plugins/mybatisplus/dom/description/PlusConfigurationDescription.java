package com.econ.platformcloud.plugins.mybatisplus.dom.description;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.Configuration;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import com.econ.platformcloud.plugins.mybatisplus.util.DomUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class PlusConfigurationDescription extends DomFileDescription<Configuration> {

    public PlusConfigurationDescription() {
        super(Configuration.class, "configuration");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        return DomUtils.isMybatisConfigurationFile(file);
    }

}
