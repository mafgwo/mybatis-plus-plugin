package com.econ.platformcloud.plugins.mybatisplus.action;

import com.econ.platformcloud.plugins.mybatisplus.codegenerator.view.DBInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ConfigDatabase extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        DBInfo dialog = new DBInfo();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(500, 300);
    }
}
