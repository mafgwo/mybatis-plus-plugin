package com.econ.platformcloud.plugins.mybatisplus.service;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.IdDomElement;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.econ.platformcloud.plugins.mybatisplus.util.MapperUtils;
import com.google.common.base.Optional;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class PlusJavaService {

    private Project project;

    private JavaPsiFacade javaPsiFacade;

    private PlusEditorService plusEditorService;

    public PlusJavaService(Project project) {
        this.project = project;
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
        this.plusEditorService = PlusEditorService.getInstance(project);
    }

    public static PlusJavaService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, PlusJavaService.class);
    }

    public Optional<PsiClass> getReferenceClazzOfPsiField(@NotNull PsiElement field) {
        if (!(field instanceof PsiField)) {
            return Optional.absent();
        }
        PsiType type = ((PsiField) field).getType();
        return type instanceof PsiClassReferenceType ? Optional.fromNullable(((PsiClassReferenceType) type).resolve()) : Optional.<PsiClass>absent();
    }

    public Optional<DomElement> findStatement(@Nullable PsiMethod method) {
        CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<DomElement>();
        process(method, processor);
        return processor.isFound() ? Optional.fromNullable(processor.getFoundValue()) : Optional.<DomElement>absent();
    }

    @SuppressWarnings("unchecked")
    public void process(@NotNull PsiMethod psiMethod, @NotNull Processor<IdDomElement> processor) {
        PsiClass psiClass = psiMethod.getContainingClass();
        if (null == psiClass) {
            return;
        }
        String id = psiClass.getQualifiedName() + "." + psiMethod.getName();
        for (Mapper mapper : MapperUtils.findMappers(psiMethod.getProject())) {
            for (IdDomElement idDomElement : mapper.getDaoElements()) {
                if (MapperUtils.getIdSignature(idDomElement).equals(id)) {
                    processor.process(idDomElement);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void process(@NotNull PsiClass clazz, @NotNull Processor<Mapper> processor) {
        // TIP: 这里有错误异常
        String ns = clazz.getQualifiedName();
        for (Mapper mapper : MapperUtils.findMappers(clazz.getProject())) {
            if (MapperUtils.getNamespace(mapper).equals(ns)) {
                processor.process(mapper);
            }
        }
    }

    public void process(@NotNull PsiElement target, @NotNull Processor processor) {
        if (target instanceof PsiMethod) {
            process((PsiMethod) target, processor);
        } else if (target instanceof PsiClass) {
            //  转化为类错误
//             process((PsiClass) target, processor);
        }
    }

    public <T> Optional<T> findWithFindFirstProcessor(@NotNull PsiElement target) {
        CommonProcessors.FindFirstProcessor<T> processor = new CommonProcessors.FindFirstProcessor<T>();
        process(target, processor);
        return Optional.fromNullable(processor.getFoundValue());
    }

    public void importClazz(PsiJavaFile file, String clazzName) {
        if (!JavaUtils.hasImportClazz(file, clazzName)) {
            java.util.Optional<PsiClass> clazz = JavaUtils.findClazz(project, clazzName);
            PsiImportList importList = file.getImportList();
            if (clazz.isPresent() && null != importList) {
                PsiElementFactory elementFactory = javaPsiFacade.getElementFactory();
                PsiImportStatement statement = elementFactory.createImportStatement(clazz.get());
                importList.add(statement);
                plusEditorService.format(file, statement);
            }
        }
    }
}

