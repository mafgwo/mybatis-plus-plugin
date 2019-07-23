package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.GenConfig;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.GenUtil;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.*;

public class ShowTableInfo extends JFrame {
    private JPanel contentPane;
    private JButton showColumn;
    private JButton codeGenerator;
    private JTable tableInfo;
//    private JTextField frontPathTextField;
    private JTextField authorTextField;
    private JTextField mymoduleTextField;
    private JTextField myPackTextField;
    private JCheckBox isOver;
    private JLabel myModule;
//    private JTextField apiPathTextField;
    private JButton saveButton;
    List<TableInfo> tableInfoList = null;
    private String projectFilePath;

    public ShowTableInfo(String projectFilePath) {
        this.projectFilePath = projectFilePath;

        setContentPane(contentPane);
        getRootPane().setDefaultButton(showColumn);

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

        tableInfoList = MysqlUtil.getInstance().getTableInfo();

        // 表格所有行数据
        Object[][] rowData = new Object[tableInfoList.size()][];

        for (int i = 0; i < tableInfoList.size(); i++) {//循环遍历所有行
            TableInfo tableInfo = tableInfoList.get(i);//每行的列数
            String[] tableInfoArr = {
                tableInfo.getTableName(),
                tableInfo.getCreateTime(),
                tableInfo.getEngine(),
                tableInfo.getCoding(),
                tableInfo.getRemark()
            };
            rowData[i] = tableInfoArr;
        }

        String[] columnNames = new String[]{"table name", "create time", "engine", "coding", "remark"};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);

        // 设置表格内容颜色
        tableInfo.setForeground(Color.BLACK);                   // 字体颜色
        tableInfo.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        tableInfo.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
        tableInfo.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
        tableInfo.setGridColor(Color.GRAY);                     // 网格颜色

        // 设置表头
        tableInfo.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        tableInfo.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
        tableInfo.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        tableInfo.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        tableInfo.setPreferredScrollableViewportSize(new Dimension(600, 300));
        tableInfo.setModel(tableModel);
        showColumn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tableInfo.getSelectedRows();
                if (selectedRows.length <= 0) {
                    Messages.showInfoMessage("select one line！", "mybatis plus");
                    return;
                }
                for (int selectedRow : selectedRows) {
//                    TableInfo tableInfo = tableInfoList.get(selectedRow);
                    String tableName = (String) ShowTableInfo.this.tableInfo.getValueAt(selectedRow, 0);
                    ShowColumnInfo dialog = new ShowColumnInfo(tableName);
                    dialog.pack();
                    dialog.setVisible(true);
                    dialog.setLocationRelativeTo(null);
                    dialog.setSize(800, 600);
                }
            }
        });
        codeGenerator.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenConfig genConfig = new GenConfig();
                genConfig.setRootFolder(projectFilePath);
//                genConfig.setPath(frontPathTextField.getText());
                genConfig.setId(1L);
                genConfig.setPack(myPackTextField.getText());
//                genConfig.setApiPath(apiPathTextField.getText());
                genConfig.setModuleName(mymoduleTextField.getText());

                genConfig.setAuthor(authorTextField.getText());
                boolean isOver2 = isOver.isSelected();
                genConfig.setCover(isOver2);

                int[] selectedRows = tableInfo.getSelectedRows();
                if (selectedRows.length <= 0) {
                    Messages.showInfoMessage("select one line！", "mybatis plus");
                    return;
                }
                for (int selectedRow : selectedRows) {
                    String tableName = (String) ShowTableInfo.this.tableInfo.getValueAt(selectedRow, 0);
//                    List<ColumnInfo> columnInfoList = MysqlUtil.getInstance().getColumns(tableName);
                    DoCodeGenerator(tableName, genConfig);
                }
                Messages.showInfoMessage("generator successful！", "mybatis plus");
            }
        });
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setMybatisPlusGlobalConst();
                Messages.showInfoMessage("save successful！", "mybatis plus");
            }
        });

        setMysqlFieldText();
    }

    public void DoCodeGenerator(String tableName, GenConfig genConfig) {
        //
        // 获取数据库 读取数据库信息
        //  配置生成的位置
        //  修改ftl文件
       GenUtil.generatorCode(tableName, genConfig);

    }

    private void setMysqlFieldText() {
        String frontPath = PropertiesComponent.getInstance().getValue(MYBATISPLUS_FRONT_PATH,"D:\\tempfile\\front");
//        frontPathTextField.setText(frontPath);

        String apiPath = PropertiesComponent.getInstance().getValue(MYBATISPLUS_FRONT_API_PATH,"D:\\tempfile\\front\\api");
//        apiPathTextField.setText(apiPath);

        String module = PropertiesComponent.getInstance().getValue(MYBATISPLUS_MODULE, "mybmodule");
        mymoduleTextField.setText(module);

        String myPackage = PropertiesComponent.getInstance().getValue(MYBATISPLUS_PACKAGE, "org.py.mybmodule.submodule");
        myPackTextField.setText(myPackage);

        String author = PropertiesComponent.getInstance().getValue(MYBATISPLUS_AUTHOR, "author");
        authorTextField.setText(author);

        isOver.setSelected(PropertiesComponent.getInstance().getBoolean(MYBATISPLUS_IS_OVER, false));
    }

    private void setMybatisPlusGlobalConst() {
//        PropertiesComponent.getInstance().setValue(MYBATISPLUS_FRONT_PATH, frontPathTextField.getText());
//        PropertiesComponent.getInstance().setValue(MYBATISPLUS_FRONT_API_PATH, apiPathTextField.getText());
        PropertiesComponent.getInstance().setValue(MYBATISPLUS_MODULE, mymoduleTextField.getText());
        PropertiesComponent.getInstance().setValue(MYBATISPLUS_PACKAGE, myPackTextField.getText());
        PropertiesComponent.getInstance().setValue(MYBATISPLUS_AUTHOR, authorTextField.getText());
        PropertiesComponent.getInstance().setValue(MYBATISPLUS_IS_OVER, isOver.isSelected());
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        ShowTableInfo dialog = new ShowTableInfo("");
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(800, 600);
//        System.exit(0);
    }
}
