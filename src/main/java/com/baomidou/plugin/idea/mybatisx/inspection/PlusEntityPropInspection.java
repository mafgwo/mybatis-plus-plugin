package com.baomidou.plugin.idea.mybatisx.inspection;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.spring.contexts.model.LocalAnnotationModelImpl;
import com.intellij.spring.contexts.model.LocalModel;
import com.intellij.spring.model.CommonSpringBean;
import com.intellij.spring.model.SpringBeanPointer;
import com.intellij.spring.model.custom.CustomLocalComponentsDiscoverer;
import com.intellij.spring.model.jam.stereotype.CustomSpringComponent;
import com.intellij.spring.model.jam.stereotype.SpringConfiguration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Spring注入Mapper的Bean
 */
public class PlusEntityPropInspection extends CustomLocalComponentsDiscoverer {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(PlusEntityPropInspection.class);

    private void getVirtualFile(VirtualFile virtualFile, Project project, Set<CommonSpringBean> psiClassSet) {
        if (!Optional.ofNullable(virtualFile).isPresent()) {
            return;
        }
        if (virtualFile.isDirectory()) {
            VirtualFile[] children = virtualFile.getChildren();
            for (VirtualFile file : children) {
                getVirtualFile(file, project, psiClassSet);
            }
        } else {
            PsiJavaFile psiDaoFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (psiDaoFile != null) {
                PsiClass psiClass = psiDaoFile.getClasses()[0];
                // 找到是mybatis的类
                if (Optional.ofNullable(psiClass.getName()).isPresent() && psiClass.getName().contains("Mapper")) {
                    psiClassSet.add(new CustomSpringComponent(psiClass));
                }
            }
        }
    }

    @NotNull
    @Override
    public Collection<CommonSpringBean> getCustomComponents(@NotNull LocalModel localModel) {
        if (!(localModel instanceof LocalAnnotationModelImpl)) {
            return new HashSet<>();
        }
        boolean isAddBean = false;
        LocalAnnotationModelImpl localAnnotationModel = (LocalAnnotationModelImpl) localModel;
        Collection<SpringBeanPointer> allCommonBeans = localAnnotationModel.getLocalBeans();
        for (SpringBeanPointer springBeanPointer : allCommonBeans) {
            if (springBeanPointer.getSpringBean() instanceof SpringConfiguration) {
                SpringConfiguration springConfiguration = (SpringConfiguration) springBeanPointer.getSpringBean();
                if (null != springConfiguration.getPsiAnnotation()
                    && null != springConfiguration.getPsiAnnotation().getQualifiedName()
                    && springConfiguration.getPsiAnnotation().getQualifiedName().contains("SpringBootApplication")) {
                    isAddBean = true;
                    break;
                }
            }
        }
        if (!isAddBean) {
            return new HashSet<>();
        }

        if (!Optional.ofNullable(localModel.getModule()).isPresent()
            || !Optional.ofNullable(localModel.getModule().getModuleFile()).isPresent()) {
            return new HashSet<>();
        }
        VirtualFile virtualFile = localModel.getModule().getModuleFile().getParent();
        VirtualFile daoFile = virtualFile.findFileByRelativePath("src/main/java/");
        Set<CommonSpringBean> psiClassSet = new HashSet<>();
        if (null != daoFile) {
            getVirtualFile(daoFile, localModel.getModule().getProject(), psiClassSet);
        }
        return psiClassSet;
    }
}
