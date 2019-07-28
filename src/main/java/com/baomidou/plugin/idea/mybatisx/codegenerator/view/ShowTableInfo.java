package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.GenConfig;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.IdTypeObj;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.GenUtil;
import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.*;
import static com.baomidou.plugin.idea.mybatisx.generate.AbstractStatementGenerator.INSERT_GENERATOR;

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
    private JTextField entityTextField;
    private JTextField mapperTextField;
    private JTextField controllerTextField;
    private JTextField serviceTextField;
    private JTextField serviceImplTextField;
    private JCheckBox lombokCheckBox;
    private JCheckBox restControllerCheckBox;
    private JCheckBox resultMapCheckBox;
    private JCheckBox isFillCheckBox;
    private JComboBox idTypecomboBox;
    private JCheckBox swaggerCheckBox;
//    private JTextField templatesTextField;
    private JCheckBox isEnableCacheCheckBox;
    private JCheckBox isBaseColumnCheckBox;
    private JButton button1;
    List<TableInfo> tableInfoList = null;
    private String projectFilePath;

    public ShowTableInfo(String projectFilePath) {
        this.projectFilePath = projectFilePath;

        setContentPane(contentPane);
        getRootPane().setDefaultButton(showColumn);

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
            @Override
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
            @Override
            public void actionPerformed(ActionEvent e) {

                saveMybatisPlusGlobalConst();

                Gson gson = new Gson();
                String configJson = PropertiesComponent.getInstance().getValue(GEN_CONFIG);
                GenConfig genConfig = gson.fromJson(configJson, GenConfig.class);
                // 设置项目根目录
                genConfig.setRootFolder(projectFilePath);

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
                saveMybatisPlusGlobalConst();
                Messages.showInfoMessage("save successful！", "mybatis plus");
            }
        });

        // 初始化
        idTypecomboBox.removeAllItems();
        for (IdTypeObj idTypeObj : IDTYPES) {
            idTypecomboBox.addItem(idTypeObj.getRemark());
        }
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
        Gson gson = new Gson();
        String configJson = PropertiesComponent.getInstance().getValue(GEN_CONFIG);
        GenConfig genConfig = gson.fromJson(configJson, GenConfig.class);
        if (null == genConfig) {
            genConfig = new GenConfig();
            String configJson2 = gson.toJson(genConfig);
            PropertiesComponent.getInstance().setValue(GEN_CONFIG, configJson2);
        }

        mymoduleTextField.setText(genConfig.getModuleName());

        String myPackage = genConfig.getPack();
        myPackTextField.setText(myPackage);

        String author = genConfig.getAuthor();
        authorTextField.setText(author);

        isOver.setSelected(genConfig.isCover());
        lombokCheckBox.setSelected(genConfig.isLombok());
        swaggerCheckBox.setSelected(genConfig.isSwagger());
        restControllerCheckBox.setSelected(genConfig.isRestcontroller());
        resultMapCheckBox.setSelected(genConfig.isResultmap());
        isFillCheckBox.setSelected(genConfig.isFill());
        isEnableCacheCheckBox.setSelected(genConfig.isEnableCache());
        isBaseColumnCheckBox.setSelected(genConfig.isBaseColumnList());

//        templatesTextField.setText(genConfig.getTemplatePath());
        entityTextField.setText(genConfig.getEntityName());
        mapperTextField.setText(genConfig.getMapperName());
        controllerTextField.setText(genConfig.getControllerName());
        serviceTextField.setText(genConfig.getServiceName());
        serviceImplTextField.setText(genConfig.getServiceImplName());

        // 设置id type
        int index = genConfig.getIdtype();
        idTypecomboBox.setSelectedIndex(index);

    }

    private void saveMybatisPlusGlobalConst() {
        GenConfig genConfig = new GenConfig();
        genConfig.setModuleName(mymoduleTextField.getText());
        genConfig.setPack(myPackTextField.getText());
        genConfig.setAuthor(authorTextField.getText());
        genConfig.setCover(isOver.isSelected());

        genConfig.setLombok(lombokCheckBox.isSelected());
        genConfig.setSwagger(swaggerCheckBox.isSelected());
        genConfig.setRestcontroller(restControllerCheckBox.isSelected());
        genConfig.setResultmap(resultMapCheckBox.isSelected());
        genConfig.setFill(isFillCheckBox.isSelected());
        genConfig.setEnableCache(isEnableCacheCheckBox.isSelected());
        genConfig.setBaseColumnList(isBaseColumnCheckBox.isSelected());

//        genConfig.setTemplatePath(templatesTextField.getText());
        genConfig.setEntityName(entityTextField.getText());
        genConfig.setMapperName(mapperTextField.getText());
        genConfig.setControllerName(controllerTextField.getText());
        genConfig.setServiceName(serviceTextField.getText());
        genConfig.setServiceImplName(serviceImplTextField.getText());

        genConfig.setIdtype(idTypecomboBox.getSelectedIndex());
        Gson gson = new Gson();
        String configJson = gson.toJson(genConfig);
        PropertiesComponent.getInstance().setValue(GEN_CONFIG, configJson);


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
