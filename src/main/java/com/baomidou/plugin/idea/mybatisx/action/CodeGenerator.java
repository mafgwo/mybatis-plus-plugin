package com.baomidou.plugin.idea.mybatisx.action;

import com.baomidou.plugin.idea.mybatisx.codegenerator.view.ShowTableInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class CodeGenerator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        String projectRoot = event.getProject().getBasePath();
        ShowTableInfo dialog = new ShowTableInfo(projectRoot);
        dialog.pack();
        dialog.setVisible(true);
//        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(1000, 600);
    }
}
