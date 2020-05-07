package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.ChessUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * chess
 */


public class GameDialog extends JDialog {
    private final JPanel chessPanel;
    ChessUtil chessUtil = ChessUtil.singleInstance();
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    public GameDialog() {
        chessUtil.initChess();
        setTitle("My Shapes");
        chessPanel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                chessUtil.updateChess(g);
            }
        };
        this.getContentPane().add(chessPanel);


        chessPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final int x = e.getX();
                final int y = e.getY();
                chessUtil.putChess(x, y);
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
        setContentPane(chessPanel);
        setModal(true);
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


    }

    public static void main(String[] args) {
        GameDialog dialog = new GameDialog();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(1000, 800);

        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - dialog.getWidth()) / 2;
        final int y = (screenSize.height - dialog.getHeight()) / 2;
        dialog.setLocation(x, y);
        //        System.exit(0);

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
