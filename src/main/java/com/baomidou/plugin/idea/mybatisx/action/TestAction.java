package com.baomidou.plugin.idea.mybatisx.action;

import com.baomidou.plugin.idea.mybatisx.codegenerator.view.DBInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.view.ShowTableInfo;
import com.intellij.database.actions.DatabaseObjectRefactoring;
import com.intellij.database.model.DasObject;
import com.intellij.database.model.DasTable;
import com.intellij.database.model.DatabaseSystem;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbDataSourceImpl;
import com.intellij.database.psi.DbElement;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.util.DasUtil;
import com.intellij.database.util.DbSqlUtil;
import com.intellij.database.util.DbUtil;
import com.intellij.database.view.DatabaseView;
import com.intellij.database.view.ui.AbstractDbRefactoringDialog;
import com.intellij.database.view.ui.DbTableDialog;
import com.intellij.ide.actions.ActivateToolWindowAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.SystemIndependent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * 一个线上的测试动作，主要用于测试一些骚操作。
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/08/03 08:55
 */
public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        String projectRoot = e.getProject().getBasePath();
        ShowTableInfo dialog = new ShowTableInfo(projectRoot);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(800,600);
//        Messages.showInfoMessage("测试完毕！", "一个温馨的提示框跟你说：");
    }
}
