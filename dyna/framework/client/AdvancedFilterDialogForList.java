// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AdvancedFilterDialogForList.java

package dyna.framework.client;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaDateChooser;
import dyna.uic.MInternalFrame;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Referenced classes of package dyna.framework.client:
//            CodeChooserUser, DynaMOAD, UIManagement, LogIn, 
//            SmallSearchDialog, CodeSelectDialog, AdvancedSearchResultSet, ClassSelectDialog, 
//            AdvancedSearchResult, DFDefaultTreeCellRenderer

public class AdvancedFilterDialogForList extends JDialog
    implements ActionListener, WindowListener, MouseListener, TreeSelectionListener, CodeChooserUser
{
    class PopupLink extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            Object source = e.getSource();
            if(source.equals(filterListTree))
                maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            Object source = e.getSource();
            if(source.equals(filterListTree))
                maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            Object source = e.getSource();
            if(e.isPopupTrigger() && source.equals(filterListTree))
            {
                if(filterListTree.getSelectionPath() == null)
                    return;
                int treeLevel = ((DefaultMutableTreeNode)filterListTree.getSelectionPath().getLastPathComponent()).getLevel();
                if(treeLevel == 0)
                    return;
                if(treeLevel == 1)
                {
                    treePopupMenu.removeAll();
                    treePopupMenu.add(addMenuItem);
                    treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
                } else
                if(treeLevel == 2)
                {
                    treePopupMenu.removeAll();
                    treePopupMenu.add(copyMenuItem);
                    treePopupMenu.add(deleteMenuItem);
                    treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        PopupLink()
        {
        }
    }

    class ResultTable extends Table
    {

        public void valueChanged(ListSelectionEvent e)
        {
            super.valueChanged(e);
            ResultTable_valueChanged(this, e);
        }

        public ResultTable()
        {
        }

        public ResultTable(ArrayList dataList, ArrayList columnNameList, ArrayList columnWidth, int selection, int tableWidth)
        {
            super(dataList, columnNameList, columnWidth, selection, tableWidth);
        }

        public ResultTable(ArrayList datalist, ArrayList columnNamelist, ArrayList columnWidth, int Selection)
        {
            super(datalist, columnNamelist, columnWidth, Selection);
        }
    }

    class ClassListCellRenderer extends JLabel
        implements ListCellRenderer
    {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if(value != null && ((ArrayList)value).get(0) != null && ((ArrayList)value).get(2) != null)
                setText(((ArrayList)value).get(0).toString() + " - " + ((ArrayList)value).get(2).toString());
            setBackground(isSelected ? list.getSelectionBackground() : Color.white);
            setForeground(isSelected ? list.getSelectionForeground() : Color.black);
            return this;
        }

        public ClassListCellRenderer()
        {
            setOpaque(true);
        }
    }

    class FieldListCellRenderer extends JLabel
        implements ListCellRenderer
    {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if(value != null && ((ArrayList)value).get(1) != null)
                setText(((ArrayList)value).get(1).toString());
            setBackground(isSelected ? list.getSelectionBackground() : Color.white);
            setForeground(isSelected ? list.getSelectionForeground() : Color.black);
            return this;
        }

        public FieldListCellRenderer()
        {
            setOpaque(true);
        }
    }


    public AdvancedFilterDialogForList(Frame parent, Component comp, boolean modal)
    {
        super(parent, modal);
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        clearButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        saveButton = new JButton();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        filterNameLabel = new JLabel("Filter Name");
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        borderLO = new BorderLayout();
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        newUI = DynaMOAD.newUI;
        searchData = null;
        columnName = null;
        columnWidth = null;
        modelOuid = "";
        ouid = "";
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        fieldSearchList = new ArrayList();
        parentFr = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        searchCondition = null;
        searchConditionData = new HashMap();
        fieldInfoList = null;
        selectCnt = 0;
        layout = new FormLayout("left:max(50dlu;p),4dlu,75dlu:grow(1),4dlu,12dlu", "");
        optionLabel = null;
        rdoBtnPanel = null;
        buttonGroup = null;
        publicRdoBtn = null;
        privateRdoBtn = null;
        advancedHSplitPane = null;
        advancedVSplitPane = null;
        filterListFrame = null;
        filterListTree = null;
        filterListTreeScrPane = null;
        classListFrame = null;
        classListPanel = null;
        classListScrPane = null;
        classListList = null;
        classListRenderer = null;
        classListListModel = null;
        buttonPanel2 = null;
        addClassButton = null;
        deleteClassButton = null;
        filterDetailInfoFrame = null;
        filterDetailInfoPanel = null;
        fieldListPanel = null;
        selectedClassOuid = null;
        addClassOuid = null;
        addClassName = null;
        classLabel = null;
        classListComboBox = null;
        classListComboBoxModel = null;
        classLabel2 = null;
        classListComboBox2 = null;
        classListComboBoxModel2 = null;
        fieldLabel = null;
        fieldComboBox = null;
        fieldListRenderer = null;
        fieldComboBoxModel = null;
        fieldLabel2 = null;
        fieldComboBox2 = null;
        fieldComboBoxModel2 = null;
        operatorLabel = null;
        operatorComboBox = null;
        operatorInputButton = null;
        operandPanel = null;
        cOperandLabel = null;
        cOperandTextField = null;
        cOperand = null;
        statusComboBox = null;
        booleanComboBox = null;
        dateSelectButton2 = null;
        objectSelectButton = null;
        cOperandInputButton = null;
        vOperandInputButton = null;
        conditionLabel = null;
        conditionTextField = null;
        conditionDelButton = null;
        aFilterListData = null;
        aFilterListTable = null;
        aFilterListTableScrPane = null;
        buttonPanel = null;
        settingButton = null;
        saveButton2 = null;
        deleteButton = null;
        closeButton2 = null;
        excuteButton = null;
        cc = new CellConstraints();
        parentFr = parent;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initStatus();
        initComponents();
        initBooleanComboBox();
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
        createPopup();
        System.out.println("AdvancedFilterDialogForList");
    }

    private void initComponents()
    {
        classLabel = new JLabel("Class");
        classListRenderer = new ClassListCellRenderer();
        classListComboBoxModel = new DefaultComboBoxModel();
        classListComboBox = new JComboBox(classListComboBoxModel);
        classListComboBox.setDoubleBuffered(true);
        classListComboBox.setActionCommand("classCombo");
        classListComboBox.addActionListener(this);
        classListComboBox.setRenderer(classListRenderer);
        classLabel2 = new JLabel("Class");
        classListComboBoxModel2 = new DefaultComboBoxModel();
        classListComboBox2 = new JComboBox(classListComboBoxModel2);
        classListComboBox2.setDoubleBuffered(true);
        classListComboBox2.setActionCommand("classCombo2");
        classListComboBox2.addActionListener(this);
        classListComboBox2.setRenderer(classListRenderer);
        fieldLabel = new JLabel("Fields");
        fieldListRenderer = new FieldListCellRenderer();
        fieldComboBoxModel = new DefaultComboBoxModel();
        fieldComboBox = new JComboBox(fieldComboBoxModel);
        fieldComboBox.setDoubleBuffered(true);
        fieldComboBox.setActionCommand("fieldCombo");
        fieldComboBox.addActionListener(this);
        fieldComboBox.setRenderer(fieldListRenderer);
        fieldLabel2 = new JLabel("Fields");
        fieldComboBoxModel2 = new DefaultComboBoxModel();
        fieldComboBox2 = new JComboBox(fieldComboBoxModel2);
        fieldComboBox2.setDoubleBuffered(true);
        fieldComboBox2.setActionCommand("fieldCombo2");
        fieldComboBox2.addActionListener(this);
        fieldComboBox2.setRenderer(fieldListRenderer);
        operatorLabel = new JLabel("Operator");
        operatorComboBox = new JComboBox();
        operatorComboBox.setDoubleBuffered(true);
        operatorComboBox.addItem("=");
        operatorComboBox.addItem("<");
        operatorComboBox.addItem("<=");
        operatorComboBox.addItem(">");
        operatorComboBox.addItem(">=");
        operatorComboBox.addItem("<>");
        operatorComboBox.addItem("..");
        operatorComboBox.addItem("&");
        operatorComboBox.addItem("|");
        operatorInputButton = new JButton(new ImageIcon(getClass().getResource("/icons/add_att.gif")));
        operatorInputButton.setMargin(new Insets(0, 0, 0, 0));
        operatorInputButton.setToolTipText("Input");
        operatorInputButton.setActionCommand("input operator");
        operatorInputButton.addActionListener(this);
        operandPanel = new JPanel();
        javax.swing.border.Border eBorder = BorderFactory.createEtchedBorder(1);
        javax.swing.border.TitledBorder tBorder = BorderFactory.createTitledBorder(eBorder, "Operand", 1, 2, operandPanel.getFont(), operandPanel.getForeground());
        operandPanel.setBorder(tBorder);
        operandPanel.setBackground(UIManagement.panelBackGround);
        cOperandLabel = new JLabel("Constant");
        cOperandTextField = new JTextField();
        cOperandTextField.setDoubleBuffered(true);
        statusComboBox = new JComboBox(statusArray);
        statusComboBox.setDoubleBuffered(true);
        dateSelectButton2 = new JButton(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateSelectButton2.setActionCommand("date2");
        dateSelectButton2.setToolTipText("Date select");
        dateSelectButton2.setMargin(new Insets(0, 0, 0, 0));
        dateSelectButton2.addActionListener(this);
        dateSelectButton2.setDoubleBuffered(true);
        objectSelectButton = new JButton(new ImageIcon(getClass().getResource("/icons/SmallSearchButton.gif")));
        objectSelectButton.setMargin(new Insets(0, 0, 0, 0));
        objectSelectButton.addActionListener(this);
        objectSelectButton.setDoubleBuffered(true);
        cOperandInputButton = new JButton(new ImageIcon(getClass().getResource("/icons/add_att.gif")));
        cOperandInputButton.setMargin(new Insets(0, 0, 0, 0));
        cOperandInputButton.setToolTipText("Input");
        cOperandInputButton.setActionCommand("input constant operand");
        cOperandInputButton.addActionListener(this);
        vOperandInputButton = new JButton(new ImageIcon(getClass().getResource("/icons/add_att.gif")));
        vOperandInputButton.setMargin(new Insets(0, 0, 0, 0));
        vOperandInputButton.setToolTipText("Input");
        vOperandInputButton.setActionCommand("input variable operand");
        vOperandInputButton.addActionListener(this);
        conditionLabel = new JLabel("Condition");
        conditionTextField = new JTextField();
        conditionTextField.setDoubleBuffered(true);
        conditionTextField.setEnabled(false);
        conditionTextField.setBackground(UIManagement.textBoxNotEditableBack);
        conditionDelButton = new JButton(new ImageIcon(getClass().getResource("/icons/clear.gif")));
        conditionDelButton.setMargin(new Insets(0, 0, 0, 0));
        conditionDelButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        conditionDelButton.setActionCommand("delete condition item");
        conditionDelButton.addActionListener(this);
        FormLayout formLayout = new FormLayout("left:max(40dlu;p),4dlu,75dlu:grow(1),4dlu,12dlu,4dlu,12dlu,20dlu,left:max(40dlu;p),4dlu,59dlu:grow(1),4dlu,12dlu", "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
        fieldListPanel = new PanelBuilder(formLayout);
        fieldListPanel.setDefaultDialogBorder();
        fieldListPanel.getPanel().setBackground(UIManagement.panelBackGround);
        fieldListPanel.add(classLabel, cc.xy(1, 1));
        fieldListPanel.add(classListComboBox, cc.xywh(3, 1, 5, 1));
        fieldListPanel.add(fieldLabel, cc.xy(9, 1));
        fieldListPanel.add(fieldComboBox, cc.xywh(11, 1, 3, 1));
        fieldListPanel.add(operatorLabel, cc.xy(1, 3));
        fieldListPanel.add(operatorComboBox, cc.xy(3, 5));
        fieldListPanel.add(operatorInputButton, cc.xywh(5, 3, 3, 1));
        fieldListPanel.addSeparator("Operand", cc.xywh(1, 5, 13, 1));
        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
        fieldListPanel.add(cOperandTextField, cc.xywh(3, 7, 3, 1));
        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
        fieldListPanel.add(classLabel2, cc.xy(1, 9));
        fieldListPanel.add(classListComboBox2, cc.xywh(3, 9, 5, 1));
        fieldListPanel.add(fieldLabel2, cc.xy(9, 9));
        fieldListPanel.add(fieldComboBox2, cc.xy(11, 9));
        fieldListPanel.add(vOperandInputButton, cc.xy(13, 9));
        fieldListPanel.addSeparator("Condition", cc.xywh(1, 11, 13, 1));
        fieldListPanel.add(conditionLabel, cc.xy(1, 13));
        fieldListPanel.add(conditionTextField, cc.xywh(3, 13, 9, 1));
        fieldListPanel.add(conditionDelButton, cc.xy(13, 13));
        makeFilterListTree();
        filterListTreeScrPane = UIFactory.createStrippedScrollPane(null);
        filterListTreeScrPane.setViewportView(filterListTree);
        filterListTreeScrPane.setPreferredSize(new Dimension(200, 400));
        makeAdvancedFilterListTable();
        aFilterListTableScrPane = UIFactory.createStrippedScrollPane(null);
        aFilterListTableScrPane.setViewportView(aFilterListTable.getTable());
        aFilterListTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        settingButton = new JButton();
        settingButton.setText(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
        settingButton.setMargin(new Insets(0, 0, 0, 0));
        settingButton.setIcon(new ImageIcon(getClass().getResource("/icons/Properties16.gif")));
        settingButton.setToolTipText("Search Result Setting");
        settingButton.setActionCommand("setting search result");
        settingButton.addActionListener(this);
        settingButton.setDoubleBuffered(true);
        saveButton2 = new JButton();
        saveButton2.setText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton2.setMargin(new Insets(0, 0, 0, 0));
        saveButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton2.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton2.setActionCommand("save condition");
        saveButton2.addActionListener(this);
        saveButton2.setDoubleBuffered(true);
        deleteButton = new JButton();
        deleteButton.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        deleteButton.setActionCommand("delete condition");
        deleteButton.addActionListener(this);
        deleteButton.setDoubleBuffered(true);
        closeButton2 = new JButton();
        closeButton2.setText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton2.setMargin(new Insets(0, 0, 0, 0));
        closeButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton2.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton2.setActionCommand("close");
        closeButton2.addActionListener(this);
        closeButton2.setDoubleBuffered(true);
        excuteButton = new JButton();
        excuteButton.setText("\uC2E4\uD589");
        excuteButton.setMargin(new Insets(0, 0, 0, 0));
        excuteButton.setActionCommand("excute advanced search");
        excuteButton.addActionListener(this);
        excuteButton.setDoubleBuffered(true);
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.setBackground(UIManagement.panelBackGround);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(excuteButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(settingButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(saveButton2);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(closeButton2);
        filterDetailInfoPanel = new JPanel();
        filterDetailInfoPanel.setLayout(new BorderLayout());
        filterDetailInfoPanel.add(fieldListPanel.getPanel(), "North");
        filterDetailInfoPanel.add(aFilterListTableScrPane, "Center");
        filterDetailInfoPanel.add(buttonPanel, "South");
        filterListFrame = new MInternalFrame("Advanced Filter List");
        filterListFrame.add(filterListTreeScrPane);
        classListListModel = new DefaultListModel();
        classListList = new JList(classListListModel);
        classListList.setSelectionMode(0);
        classListList.setSelectedIndex(-1);
        classListList.setCellRenderer(classListRenderer);
        classListScrPane = UIFactory.createStrippedScrollPane(null);
        classListScrPane.setViewportView(classListList);
        addClassButton = new JButton();
        addClassButton.setText("Add");
        addClassButton.setMargin(new Insets(0, 0, 0, 0));
        addClassButton.setIcon(new ImageIcon(getClass().getResource("/icons/add_att.gif")));
        addClassButton.setToolTipText("Add");
        addClassButton.setActionCommand("add class");
        addClassButton.addActionListener(this);
        addClassButton.setDoubleBuffered(true);
        deleteClassButton = new JButton();
        deleteClassButton.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        deleteClassButton.setMargin(new Insets(0, 0, 0, 0));
        deleteClassButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteClassButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        deleteClassButton.setActionCommand("delete class");
        deleteClassButton.addActionListener(this);
        deleteClassButton.setDoubleBuffered(true);
        buttonPanel2 = new JPanel();
        buttonPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2, 0));
        buttonPanel2.setBackground(UIManagement.panelBackGround);
        buttonPanel2.add(Box.createHorizontalGlue());
        buttonPanel2.add(addClassButton);
        buttonPanel2.add(Box.createHorizontalStrut(5));
        buttonPanel2.add(deleteClassButton);
        buttonPanel2.add(Box.createHorizontalGlue());
        classListPanel = new JPanel();
        classListPanel.setLayout(new BorderLayout());
        classListPanel.add(classListScrPane, "Center");
        classListPanel.add(buttonPanel2, "South");
        classListFrame = new MInternalFrame("Search Class List");
        filterDetailInfoFrame = new MInternalFrame("Detail Info");
        advancedVSplitPane = new BorderlessSplitPane(0, filterListFrame, classListFrame);
        advancedVSplitPane.setDividerSize(4);
        advancedVSplitPane.setDividerLocation(250);
        advancedHSplitPane = new BorderlessSplitPane(1, advancedVSplitPane, filterDetailInfoFrame);
        advancedHSplitPane.setDividerSize(4);
        advancedHSplitPane.setDividerLocation(200);
        advancedHSplitPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 2));
        advancedHSplitPane.setPreferredSize(new Dimension(800, 500));
        setTitle("Advanced Link Filter");
        getContentPane().add(advancedHSplitPane);
        addWindowListener(this);
    }

    private void initStatus()
    {
        statusArray = new String[12];
        statusArray[0] = "";
        statusArray[1] = DynaMOAD.getMSRString("AP1", "approving", 0);
        statusArray[2] = DynaMOAD.getMSRString("AP2", "approved", 0);
        statusArray[3] = DynaMOAD.getMSRString("CKI", "checked-in", 0);
        statusArray[4] = DynaMOAD.getMSRString("CKO", "checked-out", 0);
        statusArray[5] = DynaMOAD.getMSRString("CRT", "created", 0);
        statusArray[6] = DynaMOAD.getMSRString("OBS", "obsoleted", 0);
        statusArray[7] = DynaMOAD.getMSRString("REJ", "rejected", 0);
        statusArray[8] = DynaMOAD.getMSRString("RLS", "released", 0);
        statusArray[9] = DynaMOAD.getMSRString("RV1", "reviewing", 0);
        statusArray[10] = DynaMOAD.getMSRString("RV2", "reviewed", 0);
        statusArray[11] = DynaMOAD.getMSRString("WIP", "work in progress", 0);
        statusCodeMap = new HashMap();
        statusCodeMap.put("AP1", statusArray[1]);
        statusCodeMap.put("AP2", statusArray[2]);
        statusCodeMap.put("CKI", statusArray[3]);
        statusCodeMap.put("CKO", statusArray[4]);
        statusCodeMap.put("CRT", statusArray[5]);
        statusCodeMap.put("OBS", statusArray[6]);
        statusCodeMap.put("REJ", statusArray[7]);
        statusCodeMap.put("RLS", statusArray[8]);
        statusCodeMap.put("RV1", statusArray[9]);
        statusCodeMap.put("RV2", statusArray[10]);
        statusCodeMap.put("WIP", statusArray[11]);
        statusValueMap = new HashMap();
        statusValueMap.put(statusArray[1], "AP1");
        statusValueMap.put(statusArray[2], "AP2");
        statusValueMap.put(statusArray[3], "CKI");
        statusValueMap.put(statusArray[4], "CKO");
        statusValueMap.put(statusArray[5], "CRT");
        statusValueMap.put(statusArray[6], "OBS");
        statusValueMap.put(statusArray[7], "REJ");
        statusValueMap.put(statusArray[8], "RLS");
        statusValueMap.put(statusArray[9], "RV1");
        statusValueMap.put(statusArray[10], "RV2");
        statusValueMap.put(statusArray[11], "WIP");
    }

    private void initBooleanComboBox()
    {
        String trueStr = "true";
        String falseStr = "false";
        String trueOrFalse[] = {
            "", trueStr, falseStr
        };
        booleanComboBox = new JComboBox(trueOrFalse);
        booleanComboBox.setDoubleBuffered(true);
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    private void closeDialog(WindowEvent evt)
    {
        filterListTree.removeMouseListener(this);
        filterListTree.removeTreeSelectionListener(this);
        classListComboBox.removeActionListener(this);
        classListComboBox2.removeActionListener(this);
        fieldComboBox.removeActionListener(this);
        fieldComboBox2.removeActionListener(this);
        operatorInputButton.removeActionListener(this);
        dateSelectButton2.removeActionListener(this);
        objectSelectButton.removeActionListener(this);
        cOperandInputButton.removeActionListener(this);
        vOperandInputButton.removeActionListener(this);
        conditionDelButton.removeActionListener(this);
        settingButton.removeActionListener(this);
        saveButton2.removeActionListener(this);
        deleteButton.removeActionListener(this);
        closeButton2.removeActionListener(this);
        excuteButton.removeActionListener(this);
        addClassButton.removeActionListener(this);
        deleteClassButton.removeActionListener(this);
        removeWindowListener(this);
        setVisible(false);
        dispose();
    }

    public void setConditionField(String str)
    {
        valueTextField.setText(str);
    }

    public void createPopup()
    {
        MouseListener linkMousePopup = new PopupLink();
        treePopupMenu = new JPopupMenu();
        addMenuItem = new JMenuItem();
        addMenuItem.setText("Add");
        addMenuItem.setActionCommand("add advanced filter");
        addMenuItem.addActionListener(this);
        copyMenuItem = new JMenuItem();
        copyMenuItem.setText(DynaMOAD.getMSRString("WRD_0009", "Copy", 3));
        copyMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyMenuItem.setActionCommand("copy advanced filter");
        copyMenuItem.addActionListener(this);
        deleteMenuItem = new JMenuItem();
        deleteMenuItem.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        deleteMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        deleteMenuItem.setActionCommand("delete advanced filter");
        deleteMenuItem.addActionListener(this);
        treePopupMenu.add(addMenuItem);
        treePopupMenu.add(copyMenuItem);
        treePopupMenu.add(deleteMenuItem);
        if(filterListTree != null)
            filterListTree.addMouseListener(linkMousePopup);
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = evt.getActionCommand();
        if(command == null)
            closeDialog(null);
        else
        if(command.equals("close"))
            closeDialog(null);
        else
        if(command.equals("add advanced filter"))
        {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
            if(parentNode == null)
                return;
            TreeNodeObject tmpData = (TreeNodeObject)parentNode.getUserObject();
            String filterName = JOptionPane.showInputDialog(filterListTree, "Filter name: ");
            if(Utils.isNullString(filterName))
                return;
            boolean result = false;
            try
            {
                if(tmpData.getOuid().equals("PUBLIC"))
                {
                    nds.addNode("::", "WORKSPACE", "LINKFILTER", "");
                    nds.addNode("::/WORKSPACE", modelOuid, "LINKFILTER", "MODELOUID");
                    nds.addNode("::/WORKSPACE/" + modelOuid, "PUBLIC", "LINKFILTER", "PUBLIC");
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC", "ADVANCEDLISTFILTER", "LINKFILTER", "ADVANCEDLISTFILTER");
                    result = nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER", filterName, "LINKFILTER", filterName);
                } else
                if(tmpData.getOuid().equals("PRIVATE"))
                {
                    nds.addNode("::", "WORKSPACE", "LINKFILTER", "");
                    nds.addNode("::/WORKSPACE", LogIn.userID, "LINKFILTER", "USERID");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID, "ADVANCEDLISTFILTER", "LINKFILTER", "ADVANCEDLISTFILTER");
                    result = nds.addNode("::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER", filterName, "LINKFILTER", filterName);
                }
            }
            catch(IIPRequestException e)
            {
                System.out.println(e);
            }
            if(result)
            {
                TreeNodeObject nodeObject = new TreeNodeObject(filterName, filterName, "AdvanceFilter");
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeObject);
                setInsertNode(filterListTree, parentNode, childNode);
                TreePath treePath = new TreePath(childNode.getPath());
                filterListTree.scrollPathToVisible(treePath);
                filterListTree.setSelectionPath(treePath);
            }
        } else
        if(command.equals("copy advanced filter"))
        {
            TreePath paths[] = filterListTree.getSelectionPaths();
            if(paths == null)
                return;
            DefaultMutableTreeNode node = null;
            DefaultMutableTreeNode parentNode = null;
            TreeNodeObject nodeObject = null;
            TreeNodeObject parentNodeObject = null;
            StringBuffer sb = new StringBuffer();
            String initKey = null;
            try
            {
                sb.append("[DynaMOAD Advanced Filter];");
                sb.append(paths.length);
                for(int n = 0; n < paths.length; n++)
                {
                    node = (DefaultMutableTreeNode)paths[n].getLastPathComponent();
                    if(node != null)
                    {
                        nodeObject = (TreeNodeObject)node.getUserObject();
                        if(nodeObject != null)
                        {
                            parentNode = (DefaultMutableTreeNode)node.getParent();
                            parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
                            if(parentNodeObject.getOuid().equals("PUBLIC"))
                                initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid();
                            else
                            if(parentNodeObject.getOuid().equals("PRIVATE"))
                                initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid();
                            sb.append(';');
                            sb.append(initKey);
                        }
                    }
                }

                if(sb.length() > 0)
                    Utils.copyToClipboard(sb.toString());
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        } else
        if(command.equals("delete advanced filter"))
        {
            String tmpStr = "";
            tmpStr = DynaMOAD.getMSRString("WRD_0015", "Filter", 3);
            Object args[] = {
                tmpStr
            };
            tmpStr = DynaMOAD.getMSRString("QST_0004", "", 0);
            MessageFormat form = new MessageFormat(tmpStr);
            String messageStr = form.format(((Object) (args)));
            String titleStr = DynaMOAD.getMSRString("WRD_0006", "Question", 3);
            int selected = JOptionPane.showConfirmDialog(null, messageStr, titleStr, 0, 3);
            if(selected != 0)
                return;
            TreePath paths[] = filterListTree.getSelectionPaths();
            if(paths == null)
                return;
            DefaultMutableTreeNode node = null;
            TreeNodeObject nodeObject = null;
            DefaultMutableTreeNode parentNode = null;
            TreeNodeObject parentNodeObject = null;
            boolean result = false;
            for(int i = 0; i < paths.length; i++)
            {
                node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                if(node == null)
                    return;
                nodeObject = (TreeNodeObject)node.getUserObject();
                if(nodeObject == null)
                    return;
                parentNode = (DefaultMutableTreeNode)node.getParent();
                parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
                try
                {
                    if(parentNodeObject.getOuid().equals("PUBLIC"))
                        result = nds.removeNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid());
                    else
                    if(parentNodeObject.getOuid().equals("PRIVATE"))
                        result = nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid());
                }
                catch(IIPRequestException e)
                {
                    System.out.println(e);
                }
                if(result)
                    node.removeFromParent();
            }

            filterListTree.updateUI();
        } else
        if(command.equals("classCombo"))
        {
            int index = classListComboBox.getSelectedIndex();
            if(index > -1)
            {
                ArrayList tmpList = (ArrayList)classListComboBoxModel.getElementAt(index);
                selectedClassOuid = (String)tmpList.get(1);
                updateFieldList(fieldComboBox, selectedClassOuid);
                classListComboBoxModel2.removeAllElements();
                for(int i = 0; i < classListComboBoxModel.getSize(); i++)
                    if(i != index)
                        classListComboBoxModel2.addElement(classListComboBoxModel.getElementAt(i));

            }
        } else
        if(command.equals("classCombo2"))
        {
            int index = classListComboBox2.getSelectedIndex();
            if(index > -1)
            {
                ArrayList tmpList = (ArrayList)classListComboBoxModel2.getElementAt(index);
                updateFieldList(fieldComboBox2, (String)tmpList.get(1));
            }
        } else
        if(command.equals("input operator"))
        {
            if(conditionList == null)
                conditionList = new ArrayList();
            int row = fieldComboBox.getSelectedIndex();
            if(row > -1)
            {
                ArrayList dataList = (ArrayList)fieldComboBox.getSelectedItem();
                String fieldOuid = (String)dataList.get(0);
                String selectedItem = (String)operatorComboBox.getSelectedItem();
                if(Utils.isNullString(selectedItem))
                    return;
                HashMap tmpMap = new HashMap();
                tmpMap.put("value", selectedItem);
                tmpMap.put("type", "LINKFILTER.OPR");
                conditionList.add(tmpMap);
                tmpMap = null;
                setConditionTextField(fieldOuid);
            }
        } else
        if(command.equals("input constant operand"))
            try
            {
                if(conditionList == null)
                    conditionList = new ArrayList();
                int row = fieldComboBox.getSelectedIndex();
                if(row > -1)
                {
                    ArrayList dataList = (ArrayList)fieldComboBox.getSelectedItem();
                    String fieldOuid = (String)dataList.get(0);
                    DOSChangeable fieldInfo = dos.getField(fieldOuid);
                    if(fieldOuid.equals("md$status"))
                    {
                        String selectedItem = (String)statusComboBox.getSelectedItem();
                        if(Utils.isNullString(selectedItem))
                            return;
                        String value = (String)statusValueMap.get(selectedItem);
                        HashMap tmpMap = new HashMap();
                        tmpMap.put("value", value);
                        tmpMap.put("type", "LINKFILTER.OPD.CONST");
                        conditionList.add(tmpMap);
                        tmpMap = null;
                        setConditionTextField(fieldOuid);
                    } else
                    if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 1)
                    {
                        String selectedItem = (String)booleanComboBox.getSelectedItem();
                        if(Utils.isNullString(selectedItem))
                            return;
                        HashMap tmpMap = new HashMap();
                        tmpMap.put("value", selectedItem);
                        tmpMap.put("type", "LINKFILTER.OPD.CONST");
                        conditionList.add(tmpMap);
                        tmpMap = null;
                        setConditionTextField(fieldOuid);
                    } else
                    if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 16)
                    {
                        String inputStr = cOperand;
                        if(Utils.isNullString(inputStr))
                            return;
                        HashMap tmpMap = new HashMap();
                        tmpMap.put("value", inputStr);
                        tmpMap.put("type", "LINKFILTER.OPD.CONST");
                        conditionList.add(tmpMap);
                        tmpMap = null;
                        setConditionTextField(fieldOuid);
                    } else
                    if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 24)
                    {
                        String inputStr = cOperand;
                        if(Utils.isNullString(inputStr))
                            return;
                        HashMap tmpMap = new HashMap();
                        tmpMap.put("value", inputStr);
                        tmpMap.put("type", "LINKFILTER.OPD.CONST");
                        conditionList.add(tmpMap);
                        tmpMap = null;
                        setConditionTextField(fieldOuid);
                    } else
                    {
                        String inputStr = cOperandTextField.getText();
                        if(Utils.isNullString(inputStr))
                            return;
                        HashMap tmpMap = new HashMap();
                        tmpMap.put("value", inputStr);
                        tmpMap.put("type", "LINKFILTER.OPD.CONST");
                        conditionList.add(tmpMap);
                        tmpMap = null;
                        setConditionTextField(fieldOuid);
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("input variable operand"))
            try
            {
                if(conditionList == null)
                    conditionList = new ArrayList();
                int row = fieldComboBox.getSelectedIndex();
                if(row > -1)
                {
                    ArrayList dataList = (ArrayList)fieldComboBox.getSelectedItem();
                    String fieldOuid = (String)dataList.get(0);
                    DOSChangeable fieldInfo = dos.getField(fieldOuid);
                    int row2 = fieldComboBox2.getSelectedIndex();
                    if(row2 > -1)
                    {
                        ArrayList operandClass = (ArrayList)classListComboBox2.getSelectedItem();
                        ArrayList operandField = (ArrayList)fieldComboBox2.getSelectedItem();
                        HashMap tmpMap = new HashMap();
                        tmpMap.put("value", operandClass.get(0) + "." + operandClass.get(1) + "." + operandField.get(0));
                        tmpMap.put("type", "LINKFILTER.OPD.VARIABLE");
                        conditionList.add(tmpMap);
                        tmpMap = null;
                        setConditionTextField(fieldOuid);
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("delete condition item"))
        {
            if(conditionList == null || conditionList.size() == 0)
                return;
            int row = fieldComboBox.getSelectedIndex();
            if(row > -1)
            {
                ArrayList dataList = (ArrayList)fieldComboBox.getSelectedItem();
                String fieldOuid = (String)dataList.get(0);
                conditionList.remove(conditionList.size() - 1);
                setConditionTextField(fieldOuid);
            }
        } else
        if(command.equals("save condition"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
            if(parentNode == null)
                return;
            TreeNodeObject parentNodeData = (TreeNodeObject)parentNode.getUserObject();
            try
            {
                boolean result = false;
                String initKey = null;
                String index = null;
                if(parentNodeData.getOuid().equals("PUBLIC"))
                    initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeData.getOuid() + "/CONDITION";
                else
                if(parentNodeData.getOuid().equals("PRIVATE"))
                    initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeData.getOuid() + "/CONDITION";
                ArrayList selectedClassItem = (ArrayList)classListComboBoxModel.getSelectedItem();
                String selectedClassIndex = (String)selectedClassItem.get(0);
                nds.removeNode(initKey + "/" + selectedClassIndex + "/" + (String)((ArrayList)fieldComboBox.getSelectedItem()).get(0));
                nds.addNode(initKey, selectedClassIndex, "LINKFILTER", selectedClassOuid);
                nds.addNode(initKey + "/" + selectedClassIndex, (String)((ArrayList)fieldComboBox.getSelectedItem()).get(0), "LINKFILTER", (String)((ArrayList)fieldComboBox.getSelectedItem()).get(0));
                for(int i = 0; i < conditionList.size(); i++)
                {
                    if(i < 10)
                        index = "0" + (new Integer(i)).toString();
                    else
                        index = (new Integer(i)).toString();
                    result = nds.addNode(initKey + "/" + selectedClassIndex + "/" + (String)((ArrayList)fieldComboBox.getSelectedItem()).get(0), index, (String)((HashMap)conditionList.get(i)).get("type"), (String)((HashMap)conditionList.get(i)).get("value"));
                }

            }
            catch(IIPRequestException e)
            {
                System.out.println(e);
            }
            filterListTree_mouseClicked();
        } else
        if(command.equals("delete condition"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
            if(parentNode == null)
                return;
            TreeNodeObject parentNodeData = (TreeNodeObject)parentNode.getUserObject();
            int row = aFilterListTable.getTable().getSelectedRow();
            if(row > -1)
            {
                String ouidRow = aFilterListTable.getSelectedOuidRow(row);
                ArrayList selectedData = null;
                if(ouidRow != null)
                {
                    aFilterListTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                    selectedData = (ArrayList)aFilterListTable.getList().get((new Integer(ouidRow)).intValue());
                } else
                {
                    selectedData = (ArrayList)aFilterListTable.getList().get(row);
                }
                String initKey = null;
                if(parentNodeData.getOuid().equals("PUBLIC"))
                    initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeData.getOuid() + "/CONDITION";
                else
                if(parentNodeData.getOuid().equals("PRIVATE"))
                    initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeData.getOuid() + "/CONDITION";
                try
                {
                    boolean result = false;
                    result = nds.removeNode(initKey + "/" + (String)selectedData.get(0) + "/" + (String)selectedData.get(3));
                }
                catch(IIPRequestException e)
                {
                    System.out.println(e);
                }
                filterListTree_mouseClicked();
            }
        } else
        if(command.equals("fieldCombo"))
            try
            {
                int row = fieldComboBox.getSelectedIndex();
                if(row > -1)
                {
                    ArrayList dataList = (ArrayList)fieldComboBox.getSelectedItem();
                    String fieldOuid = (String)dataList.get(0);
                    DOSChangeable fieldInfo = dos.getField(fieldOuid);
                    fieldListPanel.getPanel().removeAll();
                    fieldListPanel.add(classLabel, cc.xy(1, 1));
                    fieldListPanel.add(classListComboBox, cc.xywh(3, 1, 5, 1));
                    fieldListPanel.add(fieldLabel, cc.xy(9, 1));
                    fieldListPanel.add(fieldComboBox, cc.xywh(11, 1, 3, 1));
                    fieldListPanel.add(operatorLabel, cc.xy(1, 3));
                    fieldListPanel.add(operatorComboBox, cc.xywh(3, 3, 3, 1));
                    fieldListPanel.add(operatorInputButton, cc.xy(7, 3));
                    fieldListPanel.addSeparator("Operand", cc.xywh(1, 5, 13, 1));
                    if(fieldOuid.equals("md$cdate") || fieldOuid.equals("md$mdate") || fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 22)
                    {
                        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
                        fieldListPanel.add(cOperandTextField, cc.xy(3, 7));
                        fieldListPanel.add(dateSelectButton2, cc.xy(5, 7));
                        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
                        cOperandTextField.setEditable(false);
                    } else
                    if(fieldOuid.equals("md$status"))
                    {
                        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
                        fieldListPanel.add(statusComboBox, cc.xywh(3, 7, 3, 1));
                        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
                    } else
                    if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 1)
                    {
                        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
                        fieldListPanel.add(booleanComboBox, cc.xywh(3, 7, 3, 1));
                        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
                    } else
                    if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 16)
                    {
                        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
                        fieldListPanel.add(cOperandTextField, cc.xy(3, 7));
                        fieldListPanel.add(objectSelectButton, cc.xy(5, 7));
                        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
                        cOperandTextField.setEditable(false);
                        objectSelectButton.setActionCommand("object select/" + (String)fieldInfo.get("type.ouid@class"));
                        objectSelectButton.setToolTipText("Object select");
                    } else
                    if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 24)
                    {
                        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
                        fieldListPanel.add(cOperandTextField, cc.xy(3, 7));
                        fieldListPanel.add(objectSelectButton, cc.xy(5, 7));
                        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
                        cOperandTextField.setEditable(false);
                        objectSelectButton.setActionCommand("code select/" + (String)fieldInfo.get("type.ouid@class"));
                        objectSelectButton.setToolTipText("Code select");
                    } else
                    {
                        fieldListPanel.add(cOperandLabel, cc.xy(1, 7));
                        fieldListPanel.add(cOperandTextField, cc.xywh(3, 7, 3, 1));
                        fieldListPanel.add(cOperandInputButton, cc.xy(7, 7));
                        cOperandTextField.setEditable(true);
                    }
                    fieldListPanel.add(classLabel2, cc.xy(1, 9));
                    fieldListPanel.add(classListComboBox2, cc.xywh(3, 9, 5, 1));
                    fieldListPanel.add(fieldLabel2, cc.xy(9, 9));
                    fieldListPanel.add(fieldComboBox2, cc.xy(11, 9));
                    fieldListPanel.add(vOperandInputButton, cc.xy(13, 9));
                    fieldListPanel.addSeparator("Total Condition", cc.xywh(1, 11, 13, 1));
                    fieldListPanel.add(conditionLabel, cc.xy(1, 13));
                    fieldListPanel.add(conditionTextField, cc.xywh(3, 13, 9, 1));
                    fieldListPanel.add(conditionDelButton, cc.xy(13, 13));
                    fieldListPanel.getPanel().updateUI();
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(!command.equals("fieldCombo2"))
            if(command.equals("date2"))
            {
                DynaDateChooser newDateChooser = new DynaDateChooser(this);
                java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                if(date != null)
                    cOperandTextField.setText(sdfYMD.format(date));
                else
                    cOperandTextField.setText("");
                newDateChooser = null;
            } else
            if(command.startsWith("object select/"))
            {
                java.util.List testList = Utils.tokenizeMessage(command, '/');
                String lcClassOuid = (String)testList.get(1);
                if(lcClassOuid != null)
                {
                    SmallSearchDialog newSmall = new SmallSearchDialog(this, (Component)evt.getSource(), true, lcClassOuid, null);
                    newSmall.setVisible(true);
                } else
                {
                    System.out.println("type class ouid is Null");
                }
            } else
            if(command.startsWith("code select/"))
            {
                java.util.List testList = Utils.tokenizeMessage(command, '/');
                String lcCodeOuid = (String)testList.get(1);
                if(lcCodeOuid != null && !lcCodeOuid.equals("null"))
                {
                    CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)evt.getSource(), true, lcCodeOuid, null);
                    newSmall.setVisible(true);
                } else
                {
                    System.out.println("code ouid is Null");
                }
            } else
            if(command.equals("setting search result"))
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
                if(node == null)
                    return;
                TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                if(parentNode == null)
                    return;
                TreeNodeObject parentNodeData = (TreeNodeObject)parentNode.getUserObject();
                String initKey = null;
                if(parentNodeData.getOuid().equals("PUBLIC"))
                    initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeData.getOuid();
                else
                if(parentNodeData.getOuid().equals("PRIVATE"))
                    initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeData.getOuid();
                AdvancedSearchResultSet searchResultSet = new AdvancedSearchResultSet(this, classListComboBoxModel, initKey);
            } else
            if(command.equals("add class"))
            {
                ClassSelectDialog classSelectDialog = new ClassSelectDialog(null, this, true);
                classSelectDialog.show();
                if(!Utils.isNullString(addClassOuid))
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
                    if(node == null)
                        return;
                    TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                    if(parentNode == null)
                        return;
                    TreeNodeObject parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
                    boolean result = false;
                    String initKey = null;
                    int nextIndex = 0;
                    String index = null;
                    try
                    {
                        if(parentNodeObject.getOuid().equals("PUBLIC"))
                            initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid();
                        else
                        if(parentNodeObject.getOuid().equals("PRIVATE"))
                            initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid();
                        nds.addNode(initKey, "CONDITION", "LINKFILTER", "CONDITION");
                        ArrayList childList = nds.getChildNodeList(initKey + "/CONDITION");
                        if(childList != null)
                            nextIndex = Integer.parseInt((String)childList.get(childList.size() - 1)) + 1;
                        if(nextIndex < 10)
                            index = "0" + (new Integer(nextIndex)).toString();
                        else
                            index = (new Integer(nextIndex)).toString();
                        result = nds.addNode(initKey + "/CONDITION", index, "LINKFILTER", addClassOuid);
                    }
                    catch(IIPRequestException e)
                    {
                        System.out.println(e);
                    }
                    if(result)
                    {
                        ArrayList data = new ArrayList();
                        data.add(index);
                        data.add(addClassOuid);
                        data.add(addClassName);
                        classListListModel.addElement(data);
                        classListComboBoxModel.addElement(data);
                        data = null;
                    }
                }
            } else
            if(command.equals("delete class"))
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
                if(node == null)
                    return;
                TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                if(parentNode == null)
                    return;
                TreeNodeObject parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
                int index = classListList.getSelectedIndex();
                if(index > -1)
                {
                    ArrayList tmpList = (ArrayList)classListListModel.getElementAt(index);
                    boolean result = false;
                    try
                    {
                        String initKey = null;
                        String resultKey = null;
                        if(parentNodeObject.getOuid().equals("PUBLIC"))
                        {
                            initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION/" + tmpList.get(0);
                            resultKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/RESULTFIELD";
                        } else
                        if(parentNodeObject.getOuid().equals("PRIVATE"))
                        {
                            initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION/" + tmpList.get(0);
                            resultKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/RESULTFIELD";
                        }
                        result = nds.removeNode(initKey);
                        ArrayList resultList = nds.getChildNodeList(resultKey);
                        String resultValue = null;
                        if(resultList != null)
                        {
                            for(int i = 0; i < resultList.size(); i++)
                            {
                                resultValue = nds.getValue(resultKey + "/" + (String)resultList.get(i));
                                if(resultValue.startsWith((String)tmpList.get(0)))
                                    nds.removeNode(resultKey + "/" + (String)resultList.get(i));
                            }

                        }
                    }
                    catch(IIPRequestException e)
                    {
                        System.out.println(e);
                    }
                    if(result)
                    {
                        classListListModel.remove(index);
                        classListComboBoxModel.removeElementAt(index);
                    }
                }
            } else
            if(command.equals("excute advanced search"))
                try
                {
                    ArrayList lcInputList = null;
                    ArrayList lcRowList = null;
                    String lcClassOuid = null;
                    HashMap lcFieldMap = null;
                    HashMap lcFilterMap = null;
                    ArrayList lcFilterList = null;
                    String initKey = null;
                    String resultKey = null;
                    ArrayList lcClassList = null;
                    ArrayList lcResultFieldList = null;
                    String lcResultFieldValue = null;
                    ArrayList lcFieldList = null;
                    ArrayList lcConditionList = null;
                    HashMap lcConditionMap = null;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
                    if(node == null)
                        return;
                    TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                    if(parentNode == null)
                        return;
                    TreeNodeObject parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
                    if(parentNodeObject.getOuid().equals("PUBLIC"))
                    {
                        initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION";
                        resultKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/RESULTFIELD";
                    } else
                    if(parentNodeObject.getOuid().equals("PRIVATE"))
                    {
                        initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION";
                        resultKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/RESULTFIELD";
                    }
                    lcClassList = nds.getChildNodeList(initKey);
                    if(lcClassList != null)
                    {
                        lcInputList = new ArrayList();
                        for(int i = 0; i < lcClassList.size(); i++)
                        {
                            lcRowList = new ArrayList();
                            lcClassOuid = nds.getValue(initKey + "/" + (String)lcClassList.get(i));
                            lcFieldMap = new HashMap();
                            lcResultFieldList = nds.getChildNodeList(resultKey);
                            if(lcResultFieldList != null)
                            {
                                for(int j = 0; j < lcResultFieldList.size(); j++)
                                {
                                    lcResultFieldValue = nds.getValue(resultKey + "/" + (String)lcResultFieldList.get(j));
                                    java.util.List tokens = Utils.tokenizeMessage(lcResultFieldValue, '/');
                                    if(((String)tokens.get(0)).equals((String)lcClassList.get(i)))
                                        lcFieldMap.put((String)tokens.get(1), new Integer(j + 1));
                                }

                            }
                            lcFilterMap = new HashMap();
                            lcFieldList = nds.getChildNodeList(initKey + "/" + (String)lcClassList.get(i));
                            if(lcFieldList != null)
                            {
                                for(int j = 0; j < lcFieldList.size(); j++)
                                {
                                    lcConditionList = nds.getChildNodeList(initKey + "/" + (String)lcClassList.get(i) + "/" + (String)lcFieldList.get(j));
                                    if(lcConditionList != null)
                                    {
                                        lcFilterList = new ArrayList();
                                        for(int k = 0; k < lcConditionList.size(); k++)
                                        {
                                            lcConditionMap = nds.getNode(initKey + "/" + (String)lcClassList.get(i) + "/" + (String)lcFieldList.get(j) + "/" + (String)lcConditionList.get(k));
                                            for(int m = 0; m < lcClassList.size(); m++)
                                            {
                                                java.util.List tokens = Utils.tokenizeMessage((String)lcConditionMap.get("value"), '.');
                                                String lcTmpStr = null;
                                                if(((String)lcClassList.get(m)).equals((String)tokens.get(0)))
                                                {
                                                    lcTmpStr = (new Integer(m)).toString() + "." + (String)tokens.get(1) + "." + (String)tokens.get(2);
                                                    lcConditionMap.put("value", lcTmpStr);
                                                }
                                            }

                                            lcFilterList.add(lcConditionMap);
                                        }

                                    }
                                    lcFilterMap.put((String)lcFieldList.get(j), lcFilterList);
                                    lcFilterList = null;
                                }

                            }
                            lcRowList.add(lcClassOuid);
                            lcRowList.add(lcFieldMap);
                            lcRowList.add(lcFilterMap);
                            lcClassOuid = null;
                            lcFieldMap = null;
                            lcFilterMap = null;
                            lcInputList.add(lcRowList);
                            lcRowList = null;
                        }

                    }
                    ArrayList resultList = dos.advancedList(lcInputList);
                    AdvancedSearchResult advancedSearchResult = new AdvancedSearchResult(initKey, resultKey);
                    advancedSearchResult.makeAdvancedSearchResultTable();
                    advancedSearchResult.setResultData(resultList);
                    MInternalFrame searchResultFrame = new MInternalFrame("Advanced Search Result");
                    searchResultFrame.add(advancedSearchResult);
                    JDialog resultDialog = new JDialog(this, "Advanced Search", true);
                    resultDialog.getContentPane().add(searchResultFrame);
                    resultDialog.setSize(600, 400);
                    resultDialog.setVisible(true);
                }
                catch(IIPRequestException e)
                {
                    System.out.println(e);
                }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        closeDialog(windowEvent);
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(filterListTree))
            filterListTree_mouseClicked();
        else
            source.equals(aFilterListTable);
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        Object source = e.getSource();
        if(source.equals(filterListTree))
            filterListTree_mouseClicked();
    }

    public void filterListTree_mouseClicked()
    {
        if(filterListTree == null)
            return;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
        if(node.getLevel() == 2)
        {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
            if(parentNode == null)
                return;
            TreeNodeObject parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
            clearFieldListPanel();
            aFilterListData.clear();
            try
            {
                String initKey = null;
                ArrayList aFilterList = null;
                ArrayList aFilterClassList = null;
                String aFilterClassValue = null;
                ArrayList aFilterFieldList = null;
                ArrayList aFilterConditionItemList = null;
                HashMap aFilterConditionItemValue = null;
                StringBuffer tmpStrBuff = null;
                ArrayList tmpList = null;
                if(parentNodeObject.getOuid().equals("PUBLIC"))
                    initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION";
                else
                if(parentNodeObject.getOuid().equals("PRIVATE"))
                    initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION";
                classListListModel.clear();
                classListComboBoxModel.removeAllElements();
                aFilterClassList = nds.getChildNodeList(initKey);
                if(aFilterClassList != null)
                {
                    for(int i = 0; i < aFilterClassList.size(); i++)
                    {
                        aFilterClassValue = nds.getValue(initKey + "/" + (String)aFilterClassList.get(i));
                        DOSChangeable dosClass = dos.getClass(aFilterClassValue);
                        tmpList = new ArrayList();
                        tmpList.add((String)aFilterClassList.get(i));
                        tmpList.add((String)dosClass.get("ouid"));
                        tmpList.add((String)dosClass.get("name"));
                        classListListModel.addElement(tmpList);
                        classListComboBoxModel.addElement(tmpList);
                        tmpList = null;
                        aFilterFieldList = nds.getChildNodeList(initKey + "/" + (String)aFilterClassList.get(i));
                        if(aFilterFieldList != null)
                        {
                            for(int j = 0; j < aFilterFieldList.size(); j++)
                            {
                                DOSChangeable dosField = dos.getField((String)aFilterFieldList.get(j));
                                aFilterConditionItemList = nds.getChildNodeList(initKey + "/" + (String)aFilterClassList.get(i) + "/" + (String)aFilterFieldList.get(j));
                                if(aFilterConditionItemList != null)
                                {
                                    tmpStrBuff = new StringBuffer();
                                    for(int k = 0; k < aFilterConditionItemList.size(); k++)
                                    {
                                        aFilterConditionItemValue = nds.getNode(initKey + "/" + (String)aFilterClassList.get(i) + "/" + (String)aFilterFieldList.get(j) + "/" + (String)aFilterConditionItemList.get(k));
                                        if(((String)aFilterConditionItemValue.get("type")).equals("LINKFILTER.OPR"))
                                            tmpStrBuff.append((String)aFilterConditionItemValue.get("value"));
                                        else
                                        if(((String)aFilterConditionItemValue.get("type")).equals("LINKFILTER.OPD.VARIABLE"))
                                        {
                                            java.util.List tokens = Utils.tokenizeMessage((String)aFilterConditionItemValue.get("value"), '.');
                                            tmpStrBuff.append((String)tokens.get(0) + ".");
                                            DOSChangeable dosClass2 = dos.getClass((String)tokens.get(1));
                                            tmpStrBuff.append((String)dosClass2.get("name") + ".");
                                            DOSChangeable dosField2 = dos.getClass((String)tokens.get(2));
                                            if(dosField2 != null)
                                                tmpStrBuff.append(DynaMOAD.getMSRString((String)dosField2.get("code"), (String)dosField2.get("title"), 0));
                                            else
                                            if(((String)tokens.get(2)).equals("md$number"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                                            else
                                            if(((String)tokens.get(2)).equals("md$description"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                                            else
                                            if(((String)tokens.get(2)).equals("vf$version"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0023", "Version", 3));
                                            else
                                            if(((String)tokens.get(2)).equals("md$status"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                                            else
                                            if(((String)tokens.get(2)).equals("md$user"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0025", "User", 3));
                                            else
                                            if(((String)tokens.get(2)).equals("md$cdate"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                                            else
                                            if(((String)tokens.get(2)).equals("md$mdate"))
                                                tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                                        } else
                                        {
                                            DOSChangeable fieldInfo = dos.getField((String)aFilterFieldList.get(j));
                                            if(((String)aFilterFieldList.get(j)).equals("md$status"))
                                                tmpStrBuff.append(statusCodeMap.get((String)aFilterConditionItemValue.get("value")));
                                            else
                                            if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 1)
                                                tmpStrBuff.append((String)aFilterConditionItemValue.get("value"));
                                            else
                                            if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 16)
                                            {
                                                DOSChangeable dosInstance = dos.get((String)aFilterConditionItemValue.get("value"));
                                                tmpStrBuff.append((String)dosInstance.get("md$number"));
                                            } else
                                            if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 24)
                                            {
                                                DOSChangeable dosCodeItem = dos.getCodeItem((String)aFilterConditionItemValue.get("value"));
                                                tmpStrBuff.append((String)dosCodeItem.get("name") + " [" + (String)dosCodeItem.get("codeitemid") + "]");
                                            } else
                                            {
                                                tmpStrBuff.append((String)aFilterConditionItemValue.get("value"));
                                            }
                                        }
                                    }

                                    tmpList = new ArrayList();
                                    tmpList.add((String)aFilterClassList.get(i));
                                    tmpList.add(aFilterClassValue);
                                    tmpList.add(dosClass.get("name"));
                                    tmpList.add((String)aFilterFieldList.get(j));
                                    if(dosField != null)
                                        tmpList.add(DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("md$number"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("md$description"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("vf$version"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0023", "Version", 3));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("md$status"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("md$user"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0025", "User", 3));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("md$cdate"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                                    else
                                    if(((String)aFilterFieldList.get(j)).equals("md$mdate"))
                                        tmpList.add(DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                                    tmpList.add(tmpStrBuff.toString());
                                    aFilterListData.add(tmpList.clone());
                                    tmpList = null;
                                    tmpStrBuff = null;
                                }
                            }

                        }
                    }

                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
            aFilterListTable.changeTableData();
            filterDetailInfoFrame.add(filterDetailInfoPanel);
            filterDetailInfoFrame.updateUI();
            classListFrame.add(classListPanel);
            classListFrame.updateUI();
        } else
        {
            filterDetailInfoFrame.remove(filterDetailInfoPanel);
            filterDetailInfoFrame.updateUI();
            classListFrame.remove(classListPanel);
            classListFrame.updateUI();
        }
    }

    public void setFieldInObject(String strSign, String selectOuid, String number)
    {
        cOperandTextField.setText(number);
        cOperand = selectOuid;
    }

    public void setFieldInCode(String strSign, String selectOuid, String name)
    {
        cOperandTextField.setText(name);
        cOperand = selectOuid;
    }

    private void ResultTable_valueChanged(ResultTable table, ListSelectionEvent e)
    {
        if(!e.getValueIsAdjusting())
        {
            ListSelectionModel listSelectionModel = table.getTable().getSelectionModel();
            if(listSelectionModel.getMinSelectionIndex() > -1 && listSelectionModel.getMaxSelectionIndex() > -1)
                try
                {
                    int row = table.getTable().getSelectedRow();
                    String ouidRow = table.getSelectedOuidRow(row);
                    ArrayList selectedData = null;
                    if(ouidRow != null)
                    {
                        table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        selectedData = (ArrayList)table.getList().get((new Integer(ouidRow)).intValue());
                    } else
                    {
                        selectedData = (ArrayList)table.getList().get(row);
                    }
                    if(table.equals(aFilterListTable))
                    {
                        clearFieldListPanel();
                        selectedClassOuid = (String)selectedData.get(1);
                        ArrayList tmpList = new ArrayList();
                        tmpList.add((String)selectedData.get(0));
                        tmpList.add((String)selectedData.get(1));
                        tmpList.add((String)selectedData.get(2));
                        classListComboBox.setSelectedItem(tmpList);
                        tmpList = null;
                        updateFieldList(fieldComboBox, selectedClassOuid);
                        tmpList = new ArrayList();
                        tmpList.add((String)selectedData.get(3));
                        tmpList.add((String)selectedData.get(4));
                        fieldComboBox.setSelectedItem(tmpList);
                        tmpList = null;
                        operatorComboBox.setSelectedIndex(-1);
                        cOperandTextField.setText("");
                        conditionTextField.setText((String)selectedData.get(5));
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)filterListTree.getLastSelectedPathComponent();
                        if(node == null)
                            return;
                        TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                        if(node.getLevel() == 2)
                        {
                            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                            if(parentNode == null)
                                return;
                            TreeNodeObject parentNodeObject = (TreeNodeObject)parentNode.getUserObject();
                            String initKey = null;
                            if(parentNodeObject.getOuid().equals("PUBLIC"))
                                initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION/" + (String)selectedData.get(0) + "/" + (String)((ArrayList)fieldComboBox.getSelectedItem()).get(0);
                            else
                            if(parentNodeObject.getOuid().equals("PRIVATE"))
                                initKey = "::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER/" + nodeObject.getOuid() + "/CONDITION/" + (String)selectedData.get(0) + "/" + (String)((ArrayList)fieldComboBox.getSelectedItem()).get(0);
                            ArrayList aFilterConditionItemList = nds.getChildNodeList(initKey);
                            HashMap valueMap = null;
                            HashMap lcConditionMap = null;
                            if(conditionList == null)
                                conditionList = new ArrayList();
                            else
                                conditionList.clear();
                            if(aFilterConditionItemList != null)
                            {
                                for(int k = 0; k < aFilterConditionItemList.size(); k++)
                                {
                                    valueMap = nds.getNode(initKey + "/" + (String)aFilterConditionItemList.get(k));
                                    lcConditionMap = new HashMap();
                                    lcConditionMap.put("value", (String)valueMap.get("value"));
                                    lcConditionMap.put("type", (String)valueMap.get("type"));
                                    conditionList.add(lcConditionMap);
                                    lcConditionMap = null;
                                }

                            }
                        }
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        }
    }

    private void makeFilterListTree()
    {
        TreeNodeObject nodeData = new TreeNodeObject("Advanced Filter", "Advanced Filter", "Advanced Filter");
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(nodeData);
        DefaultTreeModel treeModel = new DefaultTreeModel(topNode);
        DefaultMutableTreeNode childnode = null;
        filterListTree = new JTree(treeModel);
        filterListTree.setCellRenderer(new DFDefaultTreeCellRenderer());
        filterListTree.getSelectionModel().setSelectionMode(1);
        nodeData = new TreeNodeObject("PUBLIC", "PUBLIC", "PUBLIC");
        childnode = new DefaultMutableTreeNode(nodeData);
        setInsertNode(filterListTree, topNode, childnode);
        setExpandFilterListTree(childnode);
        nodeData = new TreeNodeObject("PRIVATE", "PRIVATE", "PRIVATE");
        childnode = new DefaultMutableTreeNode(nodeData);
        setInsertNode(filterListTree, topNode, childnode);
        setExpandFilterListTree(childnode);
        filterListTree.setSelectionRow(0);
        filterListTree.addMouseListener(this);
        filterListTree.addTreeSelectionListener(this);
    }

    public void makeAdvancedFilterListTable()
    {
        aFilterListData = new ArrayList();
        columnName = new ArrayList();
        columnWidth = new ArrayList();
        columnName.add("Index");
        columnName.add("Class Name");
        columnName.add("Field Name");
        columnName.add("Condition");
        columnWidth.add(new Integer(50));
        columnWidth.add(new Integer(80));
        columnWidth.add(new Integer(100));
        columnWidth.add(new Integer(350));
        aFilterListTable = new ResultTable(aFilterListData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
        aFilterListTable.setColumnSequence(new int[] {
            0, 2, 4, 5
        });
        aFilterListTable.setIndexColumn(0);
        aFilterListTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
    }

    private void setExpandFilterListTree(DefaultMutableTreeNode node)
    {
        ArrayList advancedFilters = null;
        DefaultMutableTreeNode childnode = null;
        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
        try
        {
            if(tmpdata.getOuid().equals("PUBLIC"))
                advancedFilters = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/ADVANCEDLISTFILTER");
            else
            if(tmpdata.getOuid().equals("PRIVATE"))
                advancedFilters = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/ADVANCEDLISTFILTER");
        }
        catch(IIPRequestException e)
        {
            System.out.println(e);
        }
        if(advancedFilters != null)
        {
            for(int i = 0; i < advancedFilters.size(); i++)
            {
                tmpdata = new TreeNodeObject((String)advancedFilters.get(i), (String)advancedFilters.get(i), "AdvancedFilter");
                childnode = new DefaultMutableTreeNode(tmpdata);
                setInsertNode(filterListTree, node, childnode);
                TreePath treePath = new TreePath(childnode.getPath());
                filterListTree.scrollPathToVisible(treePath);
            }

        }
    }

    private void setInsertNode(JTree tree, DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    public void updateFieldList(JComboBox comboBox, String sClassOuid)
    {
        if(sClassOuid == null)
            return;
        try
        {
            ArrayList fieldList = dos.listFieldInClass(sClassOuid);
            if(fieldList != null)
            {
                DOSChangeable classInfo = dos.getClass(sClassOuid);
                DOSChangeable tmpDOS = new DOSChangeable();
                int i = 0;
                if(!Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    tmpDOS.put("ouid", "md$number");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                    tmpDOS.put("ouid", "md$description");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                    tmpDOS.put("ouid", "md$status");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0025", "User", 3));
                    tmpDOS.put("ouid", "md$user");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                    tmpDOS.put("ouid", "md$cdate");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                    tmpDOS.put("ouid", "md$mdate");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                } else
                {
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    tmpDOS.put("ouid", "md$number");
                    tmpDOS.put("description", "");
                    fieldList.add(0, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                    tmpDOS.put("ouid", "md$description");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0023", "Version", 3));
                    tmpDOS.put("ouid", "vf$version");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                    tmpDOS.put("ouid", "md$status");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0025", "User", 3));
                    tmpDOS.put("ouid", "md$user");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                    tmpDOS.put("ouid", "md$cdate");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    i++;
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                    tmpDOS.put("ouid", "md$mdate");
                    tmpDOS.put("description", "");
                    fieldList.add(i, tmpDOS);
                    tmpDOS = null;
                    i++;
                }
            }
            DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel)comboBox.getModel();
            comboBoxModel.removeAllElements();
            ArrayList dataList = null;
            for(int i = 0; i < fieldList.size(); i++)
            {
                DOSChangeable testDos = (DOSChangeable)fieldList.get(i);
                testDos.put("title", DynaMOAD.getMSRString((String)testDos.get("code"), (String)testDos.get("title"), 0));
                fieldList.set(i, testDos);
                dataList = new ArrayList();
                dataList.add(testDos.get("ouid"));
                dataList.add(testDos.get("title"));
                comboBoxModel.addElement(dataList);
                dataList = null;
            }

        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setClass(String ouid, String name)
    {
        addClassOuid = ouid;
        addClassName = name;
    }

    private void clearFieldListPanel()
    {
        selectedClassOuid = "";
        classListComboBox.setSelectedIndex(-1);
        fieldComboBox.setSelectedIndex(-1);
        operatorComboBox.setSelectedIndex(-1);
        cOperandTextField.setText("");
        classListComboBox2.setSelectedIndex(-1);
        fieldComboBox2.setSelectedIndex(-1);
        conditionTextField.setText("");
        if(conditionList != null)
            conditionList.clear();
    }

    private void setConditionTextField(String fieldOuid)
    {
        try
        {
            DOSChangeable fieldInfo = dos.getField(fieldOuid);
            StringBuffer tmpStrBuff = new StringBuffer();
            String conditionItem = null;
            String conditionType = null;
            for(int i = 0; i < conditionList.size(); i++)
            {
                conditionItem = (String)((HashMap)conditionList.get(i)).get("value");
                conditionType = (String)((HashMap)conditionList.get(i)).get("type");
                if(conditionType.equals("LINKFILTER.OPR"))
                    tmpStrBuff.append(conditionItem);
                else
                if(conditionType.equals("LINKFILTER.OPD.VARIABLE"))
                {
                    java.util.List tokens = Utils.tokenizeMessage(conditionItem, '.');
                    tmpStrBuff.append((String)tokens.get(0) + ".");
                    DOSChangeable dosClass = dos.getClass((String)tokens.get(1));
                    tmpStrBuff.append((String)dosClass.get("name") + ".");
                    DOSChangeable dosField = dos.getClass((String)tokens.get(2));
                    if(dosField != null)
                        tmpStrBuff.append(DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0));
                    else
                    if(((String)tokens.get(2)).equals("md$number"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    else
                    if(((String)tokens.get(2)).equals("md$description"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                    else
                    if(((String)tokens.get(2)).equals("vf$version"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0023", "Version", 3));
                    else
                    if(((String)tokens.get(2)).equals("md$status"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                    else
                    if(((String)tokens.get(2)).equals("md$user"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0025", "User", 3));
                    else
                    if(((String)tokens.get(2)).equals("md$cdate"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                    else
                    if(((String)tokens.get(2)).equals("md$mdate"))
                        tmpStrBuff.append(DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                } else
                if(fieldOuid.equals("md$status"))
                    tmpStrBuff.append((String)statusCodeMap.get(conditionItem));
                else
                if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 1)
                    tmpStrBuff.append(conditionItem);
                else
                if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 16)
                {
                    DOSChangeable dosInstance = dos.get(conditionItem);
                    tmpStrBuff.append((String)dosInstance.get("md$number"));
                } else
                if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 24)
                {
                    DOSChangeable dosCodeItem = dos.getCodeItem(conditionItem);
                    tmpStrBuff.append((String)dosCodeItem.get("name") + " [" + (String)dosCodeItem.get("codeitemid") + "]");
                } else
                {
                    tmpStrBuff.append(conditionItem);
                }
            }

            conditionTextField.setText(tmpStrBuff.toString());
        }
        catch(IIPRequestException e)
        {
            System.out.println(e);
        }
    }

    private JPanel mainPanel;
    private JPanel toolPanel;
    private JButton clearButton;
    private JButton selectButton;
    private JButton closeButton;
    private JButton dateButton;
    private JButton saveButton;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel optionPanel;
    private JTextField valueTextField;
    private JScrollPane scrollPane;
    private JLabel filterNameLabel;
    private static Hashtable comparator = null;
    private static Font font = new Font("Dialog", 0, 10);
    private String classID;
    private boolean classChangable;
    private String defaultField;
    private String defaultOperator;
    private String defaultSearchValue;
    private String searchSQL;
    private String resultSetter;
    private Object resultSetterObject;
    private BorderLayout borderLO;
    private NDS nds;
    private DOS dos;
    private UIManagement newUI;
    private int selectedRows[];
    private ArrayList searchData;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private String modelOuid;
    private String ouid;
    private String NDS_CODE;
    final int init_xcord = 10;
    final int init_ycord = 0;
    final int titleWidth = 70;
    final int totalWidth = 180;
    final int fieldHeight = 20;
    final int offset = 3;
    final int buttonOffset = 10;
    final int condition_xsize = 200;
    final int condition_ysize = 270;
    final int buttonWidth = 76;
    final int buttonHeight = 0;
    private DOSChangeable hoardData;
    private ArrayList fieldSearchList;
    private Frame parentFr;
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_UTF = 13;
    private final byte DATATYPE_BOOLEAN = 1;
    private final int dateButtonWidth = 20;
    private ArrayList dateChooserButtonList;
    private ArrayList selectButtonList;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private HashMap searchCondition;
    private HashMap searchConditionData;
    private ArrayList fieldInfoList;
    private int selectCnt;
    final int title_width = 100;
    final int title_height = 20;
    final int DIV_SIZE = 300;
    private FormLayout layout;
    private JLabel optionLabel;
    private JPanel rdoBtnPanel;
    private ButtonGroup buttonGroup;
    private JRadioButton publicRdoBtn;
    private JRadioButton privateRdoBtn;
    private JSplitPane advancedHSplitPane;
    private JSplitPane advancedVSplitPane;
    private MInternalFrame filterListFrame;
    private JTree filterListTree;
    private JScrollPane filterListTreeScrPane;
    private MInternalFrame classListFrame;
    private JPanel classListPanel;
    private JScrollPane classListScrPane;
    private JList classListList;
    private ClassListCellRenderer classListRenderer;
    private DefaultListModel classListListModel;
    private JPanel buttonPanel2;
    private JButton addClassButton;
    private JButton deleteClassButton;
    private MInternalFrame filterDetailInfoFrame;
    private JPanel filterDetailInfoPanel;
    private PanelBuilder fieldListPanel;
    private String selectedClassOuid;
    private String addClassOuid;
    private String addClassName;
    private JLabel classLabel;
    private JComboBox classListComboBox;
    private DefaultComboBoxModel classListComboBoxModel;
    private JLabel classLabel2;
    private JComboBox classListComboBox2;
    private DefaultComboBoxModel classListComboBoxModel2;
    private JLabel fieldLabel;
    private JComboBox fieldComboBox;
    private FieldListCellRenderer fieldListRenderer;
    private DefaultComboBoxModel fieldComboBoxModel;
    private JLabel fieldLabel2;
    private JComboBox fieldComboBox2;
    private DefaultComboBoxModel fieldComboBoxModel2;
    private JLabel operatorLabel;
    private JComboBox operatorComboBox;
    private JButton operatorInputButton;
    private JPanel operandPanel;
    private JLabel cOperandLabel;
    private JTextField cOperandTextField;
    private String cOperand;
    private JComboBox statusComboBox;
    private JComboBox booleanComboBox;
    private JButton dateSelectButton2;
    private JButton objectSelectButton;
    private JButton cOperandInputButton;
    private JButton vOperandInputButton;
    private JLabel conditionLabel;
    private JTextField conditionTextField;
    private JButton conditionDelButton;
    private ArrayList aFilterListData;
    private ResultTable aFilterListTable;
    private JScrollPane aFilterListTableScrPane;
    private JPanel buttonPanel;
    private JButton settingButton;
    private JButton saveButton2;
    private JButton deleteButton;
    private JButton closeButton2;
    private JButton excuteButton;
    private JPopupMenu treePopupMenu;
    private JMenuItem addMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem copyMenuItem;
    private ArrayList conditionList;
    private CellConstraints cc;
    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;







}