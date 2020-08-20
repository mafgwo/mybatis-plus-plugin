package com.econ.platformcloud.plugins.mybatisplus.generate;

import com.econ.platformcloud.plugins.mybatisplus.dom.model.GroupTwo;
import com.econ.platformcloud.plugins.mybatisplus.dom.model.Mapper;
import com.econ.platformcloud.plugins.mybatisplus.service.PlusEditorService;
import com.econ.platformcloud.plugins.mybatisplus.service.PlusJavaService;
import com.econ.platformcloud.plugins.mybatisplus.setting.MybatisPlusSetting;
import com.econ.platformcloud.plugins.mybatisplus.ui.ListSelectionListener;
import com.econ.platformcloud.plugins.mybatisplus.ui.UiComponentFacade;
import com.econ.platformcloud.plugins.mybatisplus.util.CollectionUtils;
import com.econ.platformcloud.plugins.mybatisplus.util.JavaUtils;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors.CollectProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * <p>
 * 抽象 Statement 代码生成器
 * </p>
 *
 * @author jobob
 * @since 2018-07-30
 */
public abstract class AbstractStatementGenerator {

    public static final AbstractStatementGenerator UPDATE_GENERATOR = new UpdateGenerator("update", "modify", "set");

    public static final AbstractStatementGenerator SELECT_GENERATOR = new SelectGenerator("select", "get", "look", "find", "list", "search", "count", "query");

    public static final AbstractStatementGenerator DELETE_GENERATOR = new DeleteGenerator("del", "cancel");

    public static final AbstractStatementGenerator INSERT_GENERATOR = new InsertGenerator("insert", "add", "new");

    private static final Set<AbstractStatementGenerator> ALL = ImmutableSet.of(UPDATE_GENERATOR, SELECT_GENERATOR, DELETE_GENERATOR, INSERT_GENERATOR);

    private static final Function<Mapper, String> FUN = new Function<Mapper, String>() {
        @Override
        public String apply(Mapper mapper) {
            VirtualFile vf = Objects.requireNonNull(mapper.getXmlTag()).getContainingFile().getVirtualFile();
            if (null == vf) {
                return "";
            }
            return vf.getCanonicalPath();
        }
    };

    public static Optional<PsiClass> getSelectResultType(@Nullable PsiMethod method) {
        if (null == method) {
            return Optional.empty();
        }
        PsiType returnType = method.getReturnType();
        if (returnType instanceof PsiPrimitiveType && !PsiType.VOID.equals(returnType)) {
            return JavaUtils.findClazz(method.getProject(), Objects.requireNonNull(((PsiPrimitiveType) returnType).getBoxedTypeName()));
        } else if (returnType instanceof PsiClassReferenceType) {
            PsiClassReferenceType type = (PsiClassReferenceType) returnType;
            if (type.hasParameters()) {
                PsiType[] parameters = type.getParameters();
                if (parameters.length == 1) {
                    type = (PsiClassReferenceType) parameters[0];
                }
            }
            return Optional.ofNullable(type.resolve());
        }
        return Optional.empty();
    }

    private static void doGenerate(@NotNull final AbstractStatementGenerator generator, @NotNull final PsiMethod method) {
        WriteCommandAction.writeCommandAction(method.getProject()).run(() -> {
            generator.execute(method);
        });
        //1
//        @SuppressWarnings("unchecked")
//        ThrowableRunnable<RuntimeException> throwableRunnable = new MyThrowableRunnable(generator, method);
//        WriteCommandAction.writeCommandAction(method.getProject()).run(throwableRunnable);
        // @Deprecated give up
        /*(new WriteCommandAction.Simple(method.getProject(), new PsiFile[]{method.getContainingFile()}) {
            @Override
            protected void run() throws Throwable {
                generator.execute(method);
            }
        }).execute();*/
    }

    /**
     * javaMapper 生成 xmlMapper
     *
     * @param method 方法
     */
    public static void applyGenerate(@Nullable final PsiMethod method) {
        if (null == method) {
            return;
        }
        final Project project = method.getProject();
        final AbstractStatementGenerator[] generators = getGenerators(method);
        if (1 == generators.length) {
            generators[0].execute(method);
        } else {
            JBPopupFactory.getInstance().createListPopup(
                new BaseListPopupStep<AbstractStatementGenerator>("[ Statement type for method: " + method.getName() + "]", generators) {
                    @Override
                    public PopupStep onChosen(AbstractStatementGenerator selectedValue, boolean finalChoice) {
                        PopupStep popupStep =
                            this.doFinalStep(new Runnable() {
                                @Override
                                public void run() {
                                    WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                                        @Override
                                        public void run() {
                                            AbstractStatementGenerator.doGenerate((AbstractStatementGenerator) selectedValue, method);
                                        }
                                    });
                                }
                            });
                        return popupStep;
                    }
                }
            ).showInFocusCenter();
        }
    }

    @NotNull
    private static AbstractStatementGenerator[] getGenerators(@NotNull PsiMethod method) {
        AbstractGenerateModel model = MybatisPlusSetting.getInstance().getStatementAbstractGenerateModel();
        String target = method.getName();
        List<AbstractStatementGenerator> result = Lists.newArrayList();
        for (AbstractStatementGenerator generator : ALL) {
            if (model.matchesAny(generator.getPatterns(), target)) {
                result.add(generator);
            }
        }
        return CollectionUtils.isNotEmpty(result) ? result.toArray(new AbstractStatementGenerator[0]) : ALL.toArray(new AbstractStatementGenerator[0]);
    }

    private Set<String> patterns;

    public AbstractStatementGenerator(@NotNull String... patterns) {
        this.patterns = Sets.newHashSet(patterns);
    }

    private void execute(@NotNull final PsiMethod method) {
        PsiClass psiClass = method.getContainingClass();
        if (null == psiClass) {
            return;
        }
        CollectProcessor<Mapper> processor = new CollectProcessor<>();
        PlusJavaService.getInstance(method.getProject()).process(psiClass, processor);
        final List<Mapper> mappers = Lists.newArrayList(processor.getResults());
        if (1 == mappers.size()) {
            setupTag(method, (Mapper) Iterables.getOnlyElement(mappers, (Object) null));
        } else if (mappers.size() > 1) {
            Collection<String> paths = Collections2.transform(mappers, FUN);
            UiComponentFacade.getInstance(method.getProject()).showListPopup("Choose target mapper xml to generate", new ListSelectionListener() {
                @Override
                public void selected(int index) {
                    setupTag(method, mappers.get(index));
                }

                @Override
                public boolean isWriteAction() {
                    return true;
                }
            }, paths.toArray(new String[0]));
        }
    }

    private void setupTag(PsiMethod method, Mapper mapper) {
        GroupTwo target = getTarget(mapper, method);
        target.getId().setStringValue(method.getName());
        PsiParameter[] parameters = method.getParameterList().getParameters();
        if (parameters.length == 1) {
            //Integer id  getType() 参数值类型=Integer,  getName() 参数值=id
            String canonicalText = parameters[0].getType().getCanonicalText();
            target.getParameterType().setStringValue(canonicalText);
        }
        target.setValue(" ");
        XmlTag tag = target.getXmlTag();
        int offset = Objects.requireNonNull(tag).getTextOffset() + tag.getTextLength() - tag.getName().length() + 1;
        PlusEditorService plusEditorService = PlusEditorService.getInstance(method.getProject());
        plusEditorService.format(tag.getContainingFile(), tag);
        //跳到xml位置
        plusEditorService.scrollTo(tag, offset);
    }

    @Override
    public String toString() {
        return this.getDisplayText();
    }

    @NotNull
    protected abstract GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method);

    @NotNull
    public abstract String getId();

    @NotNull
    public abstract String getDisplayText();

    public Set<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractStatementGenerator that = (AbstractStatementGenerator) o;
        return Objects.equals(patterns, that.patterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patterns);
    }
}

/*class MyThrowableRunnable implements ThrowableRunnable {
    private AbstractStatementGenerator generator;
    private PsiMethod method;

    MyThrowableRunnable(@NotNull final AbstractStatementGenerator generator, @NotNull final PsiMethod method) {
        this.generator = generator;
        this.method = method;
    }

    @Override
    public void run() {
        generator.execute(method);
    }
}*/
