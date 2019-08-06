package com.baomidou.plugin.idea.mybatisx.util;

import com.intellij.openapi.util.Comparing;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.TypeConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PropertyUtilBase.*;


public class MyPropertyUtil {
    public static boolean isSimplePropertySetter(@NotNull PsiClass clazz, PsiMethod method) {
        if (method == null) {
            return false;
        }
        if (method.isConstructor()) {
            return false;
        }
        String methodName = method.getName();
        if (!isSetterName(methodName)) {
            return false;
        }
        if (method.getParameterList().getParametersCount() != 1) {
            return false;
        }
        final PsiType returnType = method.getReturnType();
        if (returnType == null || PsiType.VOID.equals(returnType)) {
            return true;
        }
        String className = "";
        if (returnType instanceof PsiClassReferenceType) {
            className = ((PsiClassReferenceType) returnType).getClassName();
        } else if (returnType instanceof PsiImmediateClassType) {
            className = ((PsiImmediateClassType) returnType).getClassName();
        }
        if (className.equals(clazz.getName())) {
            return true;
        }

        return Comparing.equal(PsiUtil.resolveClassInType(TypeConversionUtil.erasure(returnType)), method.getContainingClass());
    }

    @Nullable
    public static String getPropertyName(@NotNull PsiClass clazz, @NotNull PsiMethod method) {
        if (isSimplePropertyGetter(method)) {
            return getPropertyNameByGetter(method);
        }
        if (isSimplePropertySetter(clazz, method)) {
            return getPropertyNameBySetter(method);
        }
        return null;
    }

    public static PsiField findPropertyFieldByMember(@NotNull PsiClass clazz, PsiMethod psiMember) {
        if (psiMember instanceof PsiField) {
            return (PsiField)psiMember;
        }

        if (psiMember != null) {
            final PsiType returnType = psiMember.getReturnType();
            if (returnType == null) {
                return null;
            }
            final PsiCodeBlock body = psiMember.getBody();
            final PsiStatement[] statements = body == null ? null : body.getStatements();
            final PsiStatement statement = statements == null || statements.length < 1 ? null : statements[0];
            final PsiElement target;
            String className = "";
            if (returnType instanceof PsiClassReferenceType) {
                className = ((PsiClassReferenceType) returnType).getClassName();
            } else {
                className =  returnType.getPresentableText();
            }
            if (PsiType.VOID.equals(returnType)|| className.equals(clazz.getName())) {
                final PsiExpression expression =
                    statement instanceof PsiExpressionStatement ? ((PsiExpressionStatement)statement).getExpression() : null;
                target = expression instanceof PsiAssignmentExpression ? ((PsiAssignmentExpression)expression).getLExpression() : null;
            }
            else {
                target = statement instanceof PsiReturnStatement ? ((PsiReturnStatement)statement).getReturnValue() : null;
            }
            final PsiElement resolved = target instanceof PsiReferenceExpression ? ((PsiReferenceExpression)target).resolve() : null;
            if (resolved instanceof PsiField) {
                final PsiField field = (PsiField)resolved;
                PsiClass memberClass = psiMember.getContainingClass();
                PsiClass fieldClass = field.getContainingClass();
                if (memberClass != null && fieldClass != null && (memberClass == fieldClass || memberClass.isInheritor(fieldClass, true))) {
                    return field;
                }
            }
        }
        return null;
    }


    @Nullable
    public static PsiMethod findPropertySetter(PsiClass aClass,
                                               @NotNull String propertyName,
                                               boolean isStatic,
                                               boolean checkSuperClasses) {
        if (aClass == null) {
            return null;
        }
        String setterName = suggestSetterName(propertyName);

        PsiMethod[] methods = aClass.findMethodsByName(setterName, checkSuperClasses);

        for (PsiMethod method : methods) {
            if (method.hasModifierProperty(PsiModifier.STATIC) != isStatic) {
                continue;
            }

            if (isSimplePropertySetter(aClass,method)) {
                if (getPropertyNameBySetter(method).equals(propertyName)) {
                    return method;
                }
            }
        }

        return null;
    }
}
