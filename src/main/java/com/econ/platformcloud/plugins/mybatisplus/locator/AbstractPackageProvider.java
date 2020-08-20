package com.econ.platformcloud.plugins.mybatisplus.locator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author yanglin
 */
public abstract class AbstractPackageProvider {

    @NotNull
    public abstract Set<PsiPackage> getPackages(@NotNull Project project);

}
