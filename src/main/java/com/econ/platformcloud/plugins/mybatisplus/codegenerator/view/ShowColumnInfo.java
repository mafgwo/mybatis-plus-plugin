package com.econ.platformcloud.plugins.mybatisplus.codegenerator.view;

import com.econ.platformcloud.plugins.mybatisplus.codegenerator.MysqlUtil;
import com.econ.platformcloud.plugins.mybatisplus.codegenerator.domain.vo.ColumnInfo;
import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ShowColumnInfo extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableColumn;

    public ShowColumnInfo(String tableName) {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        List<ColumnInfo> columnInfoList = MysqlUtil.getInstance().getColumns(tableName);

        // 表格所有行数据
        Object[][] rowData = new Object[columnInfoList.size()][];
        //循环遍历所有行
        for (int i = 0; i < columnInfoList.size(); i++) {
            //每行的列数
            ColumnInfo columnInfo = columnInfoList.get(i);
            String[] tableInfoArr = {
                columnInfo.getColumnName(),
                columnInfo.getIsNullable(),
                columnInfo.getColumnType(),
                columnInfo.getColumnComment(),
                columnInfo.getColumnKey(),
                columnInfo.getExtra()
            };
            rowData[i] = tableInfoArr;
        }

        String[] columnNames = new String[]{"field name", "allow be empty", "field type", "reamrk", "columnKey", "extra"};
//        Object[][] rowData = {{"1","2","3","4","5"}};
        DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);

        // 设置表格内容颜色
        tableColumn.setForeground(JBColor.BLACK);                   // 字体颜色
        tableColumn.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
        tableColumn.setSelectionForeground(JBColor.DARK_GRAY);      // 选中后字体颜色
        tableColumn.setSelectionBackground(JBColor.LIGHT_GRAY);     // 选中后字体背景
        tableColumn.setGridColor(JBColor.GRAY);                     // 网格颜色

        // 设置表头
        tableColumn.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
        tableColumn.getTableHeader().setForeground(JBColor.RED);                // 设置表头名称字体颜色
        tableColumn.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
        tableColumn.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        tableColumn.setPreferredScrollableViewportSize(new Dimension(300, 300));
        tableColumn.setModel(tableModel);

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
        ShowColumnInfo dialog = new ShowColumnInfo("menu");
        dialog.pack();
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(800, 600);
//        System.exit(0);
    }
}
