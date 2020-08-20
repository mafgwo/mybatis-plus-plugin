package com.econ.platformcloud.plugins.mybatisplus.locator;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class PackageLocateStrategy extends AbstractLocateStrategy {

    private AbstractPackageProvider provider = new MapperXmlPackageProvider();

    @Override
    public boolean apply(@NotNull PsiClass clazz) {
        String packageName = ((PsiJavaFile) clazz.getContainingFile()).getPackageName();
        PsiPackage pkg = JavaPsiFacade.getInstance(clazz.getProject()).findPackage(packageName);
        return provider.getPackages(clazz.getProject()).stream().anyMatch(tmp -> tmp.equals(pkg));
    }

}
