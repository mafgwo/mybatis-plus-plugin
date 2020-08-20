package com.econ.platformcloud.plugins.mybatisplus.inspection;

import com.econ.platformcloud.plugins.mybatisplus.annotation.Annotation;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Select;
import com.econ.platformcloud.plugins.mybatisplus.generate.AbstractStatementGenerator;
import com.econ.platformcloud.plugins.mybatisplus.locator.PlusMapperLocator;
import com.econ.platformcloud.plugins.mybatisplus.service.PlusJavaService;
import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglin
 */
public class MapperPlusMethodInspection extends AbstractMapperInspection {

    /**
     * 检查方法
     *
     * @param method
     * @param manager
     * @param isOnTheFly
     * @return
     */
    @Nullable
    @Override
    public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!PlusMapperLocator.getInstance(method.getProject()).process(method)
            || JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES)) {
            return EMPTY_ARRAY;
        }
        List<ProblemDescriptor> res = createProblemDescriptors(method, manager, isOnTheFly);
        return res.toArray(new ProblemDescriptor[res.size()]);
    }

    private List<ProblemDescriptor> createProblemDescriptors(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
        ArrayList<ProblemDescriptor> res = Lists.newArrayList();
        Optional<ProblemDescriptor> p1 = checkStatementExists(method, manager, isOnTheFly);
        if (p1.isPresent()) {
            res.add(p1.get());
        }
        Optional<ProblemDescriptor> p2 = checkResultType(method, manager, isOnTheFly);
        if (p2.isPresent()) {
            res.add(p2.get());
        }
        return res;
    }

    /**
     * 检查返回类型是否正确
     *
     * @param method
     * @param manager
     * @param isOnTheFly
     * @return
     */
    private Optional<ProblemDescriptor> checkResultType(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
        Optional<DomElement> ele = PlusJavaService.getInstance(method.getProject()).findStatement(method);
        if (ele.isPresent()) {
            DomElement domElement = ele.get();
            if (domElement instanceof Select) {
                Select select = (Select) domElement;
                java.util.Optional<PsiClass> target = AbstractStatementGenerator.getSelectResultType(method);
                PsiClass clazz = select.getResultType().getValue();
                PsiIdentifier ide = method.getNameIdentifier();
                if (null != ide && null == select.getResultMap().getValue()) {
                    if (target.isPresent() && (null == clazz || !target.get().equals(clazz))) {
                        return Optional.of(manager.createProblemDescriptor(ide, "Result type not match for select id=\"#ref\"",
                            new ResultTypeQuickFix(select, target.get()), ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
                    } else if (!target.isPresent() && null != clazz) {
                        return Optional.of(manager.createProblemDescriptor(ide, "Result type not match for select id=\"#ref\"",
                            (LocalQuickFix) null, ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
                    }
                }
            }
        }
        return Optional.absent();
    }

    /**
     * 检查声明是否存在
     *
     * @param method
     * @param manager
     * @param isOnTheFly
     * @return
     */
    private Optional<ProblemDescriptor> checkStatementExists(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
        PsiIdentifier ide = method.getNameIdentifier();
        if (!PlusJavaService.getInstance(method.getProject()).findStatement(method).isPresent() && null != ide) {
            return Optional.of(manager.createProblemDescriptor(ide, "Statement with id=\"#ref\" not defined in mapper xml",
                new StatementNotExistsQuickFix(method), ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
        }
        return Optional.absent();
    }

}
