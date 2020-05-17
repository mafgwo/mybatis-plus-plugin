package com.baomidou.plugin.idea.mybatisx.action;

import com.baomidou.plugin.idea.mybatisx.codegenerator.view.GameDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class GameAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GameDialog dialog = new GameDialog();
        dialog.pack();
        dialog.setVisible(true);
//        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(1000, 800);
    }
}
