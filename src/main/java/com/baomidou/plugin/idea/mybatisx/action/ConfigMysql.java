package com.baomidou.plugin.idea.mybatisx.action;

import com.baomidou.plugin.idea.mybatisx.codegenerator.view.DBInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ConfigMysql extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        DBInfo dialog = new DBInfo();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(500,300);
    }
}
