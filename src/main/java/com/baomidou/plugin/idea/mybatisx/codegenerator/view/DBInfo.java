package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.IdTypeObj;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.*;

public class DBInfo extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField dbUrlTextField;
    private JTextField userTextField;
    private JButton testConnect;
    private JLabel jdbcDriver;
    private JLabel dbUrl;
    private JPasswordField passwordField1;
    private JComboBox<String> jdbcDriverComboBox;

    public DBInfo() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        testConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                saveMybatisPlusGlobalConst();
                MysqlUtil.getInstance().testConnect();

            }


        });
        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveMybatisPlusGlobalConst();
            }
        });

        for (String driver : jdbcDrivers) {
            jdbcDriverComboBox.addItem(driver);
        }
        setMysqlFieldText();
    }

    private void setMysqlFieldText() {
        String dbUrl = PropertiesComponent.getInstance().getValue(PLUS_DBURL, "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC");
        dbUrlTextField.setText(dbUrl);

        int jdbcDriverIndex = PropertiesComponent.getInstance().getInt(PLUS_JDBCDRIVER,0);
        jdbcDriverComboBox.setSelectedIndex(jdbcDriverIndex);

        String username = PropertiesComponent.getInstance().getValue(PLUS_USERNAME,"root");
        userTextField.setText(username);

        passwordField1.setText(PropertiesComponent.getInstance().getValue(PLUS_PASSWORD,""));
    }

    private void saveMybatisPlusGlobalConst() {
        PropertiesComponent.getInstance().setValue(PLUS_DBURL,dbUrlTextField.getText());
        PropertiesComponent.getInstance().setValue(PLUS_JDBCDRIVER,jdbcDriverComboBox.getSelectedIndex(),0);
        PropertiesComponent.getInstance().setValue(PLUS_USERNAME,userTextField.getText());
        PropertiesComponent.getInstance().setValue(PLUS_PASSWORD, String.valueOf(passwordField1.getPassword()));
    }

    private void onOK() {
        // add your code here
        saveMybatisPlusGlobalConst();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DBInfo dialog = new DBInfo();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(800,600);
//        System.exit(0);
    }
}
