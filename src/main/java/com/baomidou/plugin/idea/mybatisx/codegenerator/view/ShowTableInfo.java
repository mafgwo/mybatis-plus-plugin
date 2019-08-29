package com.baomidou.plugin.idea.mybatisx.codegenerator.view;

import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.GEN_CONFIG;
import static com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MybatisConst.IDTYPES;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.baomidou.plugin.idea.mybatisx.codegenerator.MysqlUtil;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.GenConfig;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.IdTypeObj;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.GenUtil;
import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.JBColor;

public class ShowTableInfo extends JFrame {
    private JPanel contentPane;
    private JButton showColumn;
    private JButton codeGenerator;
    private JTable tableInfo;
    private JTextField authorTextField;
    private JTextField mymoduleTextField;
    private JTextField myPackTextField;
    private JCheckBox isOver;
    private JLabel myModule;
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
    private JCheckBox isEnableCacheCheckBox;
    private JCheckBox isBaseColumnCheckBox;
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

        for (int i = 0; i < tableInfoList.size(); i++) {
            TableInfo tableInfo = tableInfoList.get(i);
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
        // 字体颜色
        tableInfo.setForeground(JBColor.BLACK);
        // 字体样式
        tableInfo.setFont(new Font(null, Font.PLAIN, 14));
        // 选中后字体颜色
        tableInfo.setSelectionForeground(JBColor.DARK_GRAY);
        // 选中后字体背景
        tableInfo.setSelectionBackground(JBColor.LIGHT_GRAY);
        // 网格颜色
        tableInfo.setGridColor(JBColor.GRAY);

        // 设置表头
        // 设置表头名称字体样式
        tableInfo.getTableHeader().setFont(new Font(null, Font.BOLD, 14));
        // 设置表头名称字体颜色
        tableInfo.getTableHeader().setForeground(JBColor.RED);
        // 设置不允许手动改变列宽
        tableInfo.getTableHeader().setResizingAllowed(false);
        // 设置不允许拖动重新排序各列
        tableInfo.getTableHeader().setReorderingAllowed(false);

        // 设置滚动面板视口大小（超过该大小的行数据，需要拖动滚动条才能看到）
        tableInfo.setPreferredScrollableViewportSize(new Dimension(600, 300));
        tableInfo.setModel(tableModel);
        showColumn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tableInfo.getSelectedRows();
                if (selectedRows.length <= 0) {
                    Messages.showInfoMessage("select one line！", "Mybatis Plus");
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
                    Messages.showInfoMessage("select one line！", "Mybatis Plus");
                    return;
                }
                for (int selectedRow : selectedRows) {
                    String tableName = (String) ShowTableInfo.this.tableInfo.getValueAt(selectedRow, 0);
                    DoCodeGenerator(tableName, genConfig);
                }
                VirtualFileManager.getInstance().syncRefresh();

                Messages.showInfoMessage("generator successful！", "Mybatis Plus");
            }
        });
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveMybatisPlusGlobalConst();
                Messages.showInfoMessage("save successful！", "Mybatis Plus");
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
        restControllerCheckBox.setSelected(genConfig.isRestController());
        resultMapCheckBox.setSelected(genConfig.isResultMap());
        isFillCheckBox.setSelected(genConfig.isFill());
        isEnableCacheCheckBox.setSelected(genConfig.isEnableCache());
        isBaseColumnCheckBox.setSelected(genConfig.isBaseColumnList());

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
        genConfig.setRestController(restControllerCheckBox.isSelected());
        genConfig.setResultMap(resultMapCheckBox.isSelected());
        genConfig.setFill(isFillCheckBox.isSelected());
        genConfig.setEnableCache(isEnableCacheCheckBox.isSelected());
        genConfig.setBaseColumnList(isBaseColumnCheckBox.isSelected());

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
    }
}
