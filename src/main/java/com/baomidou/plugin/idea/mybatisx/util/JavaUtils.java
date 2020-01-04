package com.baomidou.plugin.idea.mybatisx.util;

import com.baomidou.plugin.idea.mybatisx.annotation.Annotation;
import com.baomidou.plugin.idea.mybatisx.dom.model.IdDomElement;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.intellij.psi.util.PropertyUtilBase.*;

/**
 * @author yanglin
 */
public final class JavaUtils {

    private JavaUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isModelClazz(@Nullable PsiClass clazz) {
        return null != clazz && !clazz.isAnnotationType() && !clazz.isInterface() && !clazz.isEnum() && clazz.isValid();
    }

    @NotNull
    public static Optional<PsiField> findSettablePsiField(@NotNull PsiClass clazz, @Nullable String propertyName) {
        PsiMethod propertySetter = MyPropertyUtil.findPropertySetter(clazz, propertyName, false, true);
        return null == propertySetter ? Optional.absent() : Optional.fromNullable(MyPropertyUtil.findPropertyFieldByMember(clazz, propertySetter));
    }

    @Nullable
    public static String getPropertyName(@NotNull PsiMethod method) {
        if (isSimplePropertyGetter(method)) {
            return getPropertyNameByGetter(method);
        }
        if (isSimplePropertySetter(method)) {
            return getPropertyNameBySetter(method);
        }
        return null;
    }

    @NotNull
    public static PsiField[] findSettablePsiFields(@NotNull PsiClass clazz) {
        PsiMethod[] methods = clazz.getAllMethods();
        List<PsiField> fields = Lists.newArrayList();
        for (PsiMethod method : methods) {
            if (MyPropertyUtil.isSimplePropertySetter(clazz, method)) {
                Optional<PsiField> psiField = findSettablePsiField(clazz, MyPropertyUtil.getPropertyName(clazz, method));
                if (psiField.isPresent()) {
                    fields.add(psiField.get());
                }
            }
        }
        return fields.toArray(new PsiField[fields.size()]);
    }

    public static boolean isElementWithinInterface(@Nullable PsiElement element) {
        if (element instanceof PsiClass && ((PsiClass) element).isInterface()) {
            return true;
        }
        PsiClass type = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        return Optional.fromNullable(type).isPresent() && type.isInterface();
    }

    @NotNull
    public static java.util.Optional<PsiClass> findClazz(@NotNull Project project, @NotNull String clazzName) {
        return java.util.Optional.ofNullable(JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project)));
    }

    @NotNull
    public static Optional<PsiMethod> findMethod(@NotNull Project project, @Nullable String clazzName, @Nullable String methodName) {
        if ((clazzName == null || StringUtils.isBlank(clazzName))
            || StringUtils.isBlank(methodName)) {
            return Optional.absent();
        }

        java.util.Optional<PsiClass> clazz = findClazz(project, clazzName);
        if (clazz.isPresent()) {
            PsiMethod[] methods = clazz.get().findMethodsByName(methodName, true);
            return ArrayUtils.isEmpty(methods) ? Optional.absent() : Optional.of(methods[0]);
        }
        return Optional.absent();
    }

    @NotNull
    public static Optional<PsiMethod> findMethod(@NotNull Project project, @NotNull IdDomElement element) {
        return findMethod(project, MapperUtils.getNamespace(element), MapperUtils.getId(element));
    }

    public static boolean isAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return null != modifierList && null != modifierList.findAnnotation(annotation.getQualifiedName());
    }

    @NotNull
    public static Optional<PsiAnnotation> getPsiAnnotation(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return null == modifierList ? Optional.absent() : Optional.fromNullable(modifierList.findAnnotation(annotation.getQualifiedName()));
    }

    @NotNull
    public static Optional<PsiAnnotationMemberValue> getAnnotationAttributeValue(@NotNull PsiModifierListOwner target,
                                                                                 @NotNull Annotation annotation,
                                                                                 @NotNull String attrName) {
        if (!isAnnotationPresent(target, annotation)) {
            return Optional.absent();
        }
        Optional<PsiAnnotation> psiAnnotation = getPsiAnnotation(target, annotation);
        return psiAnnotation.isPresent() ? Optional.fromNullable(psiAnnotation.get().findAttributeValue(attrName)) : Optional.absent();
    }

    @NotNull
    public static Optional<PsiAnnotationMemberValue> getAnnotationValue(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        return getAnnotationAttributeValue(target, annotation, "value");
    }

    public static Optional<String> getAnnotationValueText(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        Optional<PsiAnnotationMemberValue> annotationValue = getAnnotationValue(target, annotation);
        return annotationValue.isPresent() ? Optional.of(annotationValue.get().getText().replaceAll("\"", "")) : Optional.absent();
    }

    public static boolean isAnyAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (isAnnotationPresent(target, annotation)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllParameterWithAnnotation(@NotNull PsiMethod method, @NotNull Annotation annotation) {
        PsiParameter[] parameters = method.getParameterList().getParameters();
        for (PsiParameter parameter : parameters) {
            if (!isAnnotationPresent(parameter, annotation)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasImportClazz(@NotNull PsiJavaFile file, @NotNull String clazzName) {
        PsiImportList importList = file.getImportList();
        if (null == importList) {
            return false;
        }
        PsiImportStatement[] statements = importList.getImportStatements();
        for (PsiImportStatement tmp : statements) {
            if (null != tmp && Objects.equals(tmp.getQualifiedName(), clazzName)) {
                return true;
            }
        }
        return false;
    }

}
