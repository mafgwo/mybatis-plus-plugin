package com.econ.platformcloud.plugins.mybatisplus.codegenerator.view;

import com.econ.platformcloud.plugins.mybatisplus.codegenerator.MysqlUtil;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.DbTypeDriver;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.utils.MybatisConst;
import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.*;

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
            @Override
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

        for (DbTypeDriver dbTypeDriver : MybatisConst.DB_TYPE_DRIVERS) {
            jdbcDriverComboBox.addItem(dbTypeDriver.getName());
        }
        setMysqlFieldText();
    }

    private void setMysqlFieldText() {
        String dbUrl = PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_DBURL, "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC");
        dbUrlTextField.setText(dbUrl);

        int jdbcDriverIndex = PropertiesComponent.getInstance().getInt(MybatisConst.PLUS_DBTYPE, 0);
        jdbcDriverComboBox.setSelectedIndex(jdbcDriverIndex);

        String username = PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_USERNAME, "root");
        userTextField.setText(username);

        passwordField1.setText(PropertiesComponent.getInstance().getValue(MybatisConst.PLUS_PASSWORD, ""));
    }

    private void saveMybatisPlusGlobalConst() {
        PropertiesComponent.getInstance().setValue(MybatisConst.PLUS_DBURL, dbUrlTextField.getText());
        PropertiesComponent.getInstance().setValue(MybatisConst.PLUS_DBTYPE, jdbcDriverComboBox.getSelectedIndex(), 0);
        PropertiesComponent.getInstance().setValue(MybatisConst.PLUS_USERNAME, userTextField.getText());
        PropertiesComponent.getInstance().setValue(MybatisConst.PLUS_PASSWORD, String.valueOf(passwordField1.getPassword()));
        MysqlUtil.getInstance().resetDbInfo();
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
        dialog.setSize(800, 600);
//        System.exit(0);
    }
}
