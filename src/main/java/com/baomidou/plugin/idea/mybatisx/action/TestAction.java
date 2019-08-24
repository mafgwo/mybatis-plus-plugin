package com.baomidou.plugin.idea.mybatisx.action;

import com.baomidou.plugin.idea.mybatisx.codegenerator.view.ShowTableInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 *
 */
public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        String projectRoot = e.getProject().getBasePath();
        ShowTableInfo dialog = new ShowTableInfo(projectRoot);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
//        dialog.setAlwaysOnTop(true);
        dialog.setSize(1000,600);
    }
}
