package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.ChessUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 1. GameDialog.form custom Create resolve chessPanel's creation problem
 * chess
 */
public class GameDialog extends JFrame {
    private final ChessUtil chessUtil = ChessUtil.singleInstance();
    private JPanel chessPanel;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel currentPlayerLabel;

    public GameDialog() {
        setTitle("90s Game");
        chessUtil.initChess();
        this.getContentPane().add(contentPane);

        chessPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final int x = e.getX();
                final int y = e.getY();
                chessUtil.putChess(x, y);
                currentPlayerLabel.setText("turn " + chessUtil.getCurrentPlayer() + " to chess");
                chessPanel.updateUI();
                System.out.println("click");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        setContentPane(contentPane);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    public static void main(String[] args) {
        GameDialog dialog = new GameDialog();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(800, 600);
        //        System.exit(0);

    }

    private void onOK() {
        // add your code here
        // start game
        final Graphics g = chessPanel.getGraphics();
        chessUtil.startGame(g);
        chessPanel.updateUI();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        chessPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                // If not the line, UI not update
                super.paintComponent(g);

                chessUtil.updateChess(g);
            }

        };

    }
}
