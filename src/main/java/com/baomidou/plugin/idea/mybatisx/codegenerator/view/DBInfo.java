package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.StringUtils;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.*;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.*;

public class DBInfo extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField dbUrlTextField;
    private JTextField jdbcDriverTextField;
    private JTextField userTextField;
    private JButton testConnect;
    private JLabel jdbcDriver;
    private JCheckBox savePwdCheckBox;
    private JLabel dbUrl;
    private JPasswordField passwordField1;

    public DBInfo() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        testConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                setMybatisPlusGlobalConst();
                boolean isConnect = MysqlUtil.getInstance().testConnect();
                if (isConnect) {
                    Messages.showInfoMessage("测试成功！", "mybatis plus");
                }else {
                    Messages.showInfoMessage("测试失败！", "mybatis plus");
                }
            }


        });
        buttonOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setMybatisPlusGlobalConst();
            }
        });

       setMysqlFieldText();
    }

    private void setMysqlFieldText() {
        String dbUrl = !StringUtils.isEmpty(PropertiesComponent.getInstance().getValue(MYBATISPLUS_DBURL))?
            PropertiesComponent.getInstance().getValue(MYBATISPLUS_DBURL): "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC";
        dbUrlTextField.setText(dbUrl);

        String jdbcDriver = !StringUtils.isEmpty(PropertiesComponent.getInstance().getValue(MYBATISPLUS_JDBCDRIVER))?
            PropertiesComponent.getInstance().getValue(MYBATISPLUS_JDBCDRIVER) : "com.mysql.jdbc.Driver";
        jdbcDriverTextField.setText(jdbcDriver);

        String username = !StringUtils.isEmpty(PropertiesComponent.getInstance().getValue(MYBATISPLUS_USERNAME))?
            PropertiesComponent.getInstance().getValue(MYBATISPLUS_USERNAME) : "root";
        userTextField.setText(username);

        passwordField1.setText(PropertiesComponent.getInstance().getValue(MYBATISPLUS_PASSWORD));
    }

    private void setMybatisPlusGlobalConst() {
        PropertiesComponent.getInstance().setValue("mybatisplus_dbUrl",dbUrlTextField.getText());
        PropertiesComponent.getInstance().setValue("mybatisplus_jdbcDriver",jdbcDriverTextField.getText());
        PropertiesComponent.getInstance().setValue("mybatisplus_username",userTextField.getText());
        PropertiesComponent.getInstance().setValue("mybatisplus_password", String.valueOf(passwordField1.getPassword()));
    }

    private void onOK() {
        // add your code here
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
