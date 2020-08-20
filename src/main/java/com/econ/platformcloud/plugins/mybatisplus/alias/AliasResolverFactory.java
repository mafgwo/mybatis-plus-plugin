package com.econ.platformcloud.plugins.mybatisplus.alias;

import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AliasResolverFactory {

    @NotNull
    public static AbstractAliasResolver createInnerAliasResolver(@NotNull Project project) {
        return new InnerAliasResolver(project);
    }

    @NotNull
    public static AbstractAliasResolver createAnnotationResolver(@NotNull Project project) {
        return new AnnotationAliasResolver(project);
    }

    @NotNull
    public static AbstractAliasResolver createBeanResolver(@NotNull Project project) {
        return new BeanAliasResolver(project);
    }

    @NotNull
    public static AbstractAliasResolver createConfigPackageResolver(@NotNull Project project) {
        return new ConfigPackageAliasResolver(project);
    }

    @NotNull
    public static AbstractAliasResolver createSingleAliasResolver(@NotNull Project project) {
        return new SingleAliasResolver(project);
    }
}
