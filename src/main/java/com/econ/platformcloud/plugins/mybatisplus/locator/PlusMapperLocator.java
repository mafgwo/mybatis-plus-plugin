package com.econ.platformcloud.plugins.mybatisplus.locator;

import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class PlusMapperLocator {

    public static AbstractLocateStrategy dfltLocateStrategy = new PackageLocateStrategy();

    public static PlusMapperLocator getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, PlusMapperLocator.class);
    }

    public boolean process(@Nullable PsiMethod method) {
        return null != method && process(method.getContainingClass());
    }

    public boolean process(@Nullable PsiClass clazz) {
        return null != clazz && JavaUtils.isElementWithinInterface(clazz) && dfltLocateStrategy.apply(clazz);
    }

}
