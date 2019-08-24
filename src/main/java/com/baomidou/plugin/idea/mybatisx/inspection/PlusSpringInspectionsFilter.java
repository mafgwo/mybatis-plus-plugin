package com.baomidou.plugin.idea.mybatisx.inspection;

import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * todo spring自带代码检测过滤
 * mapper使用@Autowire注解时忽略错误
 */
public class PlusSpringInspectionsFilter implements InspectionSuppressor {

    List<String> suppressedList = new ArrayList<>();

    PlusSpringInspectionsFilter(){
//        suppressedList.add("SpringJavaInjectionPointsAutowiringInspection");
    }

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        for (String suppress : suppressedList) {
            if (toolId.equals(suppress)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public SuppressQuickFix[] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return new SuppressQuickFix[0];
    }
}
