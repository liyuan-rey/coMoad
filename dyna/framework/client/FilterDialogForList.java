// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FilterDialogForList.java

package dyna.framework.client;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaDateChooser;
import dyna.uic.Table;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            CodeChooserUser, DynaMOAD, UIManagement, LogIn, 
//            SearchCondition4CADIntegration, SearchCondition, SearchConditionPanel, SmallSearchDialog, 
//            CodeSelectDialog

public class FilterDialogForList extends JDialog
    implements ActionListener, ItemListener, WindowListener, CodeChooserUser
{
    class PopupLink extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            Object source = e.getSource();
            if(source.equals(resultTable.getTable()))
                maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            Object source = e.getSource();
            if(source.equals(resultTable.getTable()))
                maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            Object source = e.getSource();
            if(e.isPopupTrigger() && source.equals(resultTable.getTable()))
                filterPopupMenu.show(e.getComponent(), e.getX(), e.getY());
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


    public FilterDialogForList(Frame parent, JPanel searchPanel, Component comp, boolean modal, String classOuid)
    {
        super(parent, modal);
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        clearButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        saveButton = new JButton();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        filterNameLabel = new JLabel(DynaMOAD.getMSRString("WRD_0119", "Filter Name", 3));
        resultTable = new ResultTable();
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
        searchData = null;
        columnName = null;
        columnWidth = null;
        modelOuid = "";
        ouid = "";
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        fieldSearchList = new ArrayList();
        dateChooserButtonList = null;
        selectButtonList = null;
        searchCondition = null;
        searchConditionData = new HashMap();
        fieldInfoList = null;
        parentSearchCondition = null;
        selectCnt = 0;
        mainSplitPane = new JSplitPane();
        layout = new FormLayout("left:max(50dlu;p),4dlu,75dlu:grow(1),0dlu,12dlu", "");
        builder = new DefaultFormBuilder(layout);
        optionLabel = null;
        rdoBtnPanel = null;
        buttonGroup = null;
        publicRdoBtn = null;
        privateRdoBtn = null;
        parentSearchCondition = searchPanel;
        ouid = classOuid;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        makeSearchResultTable(ouid);
        initStatus();
        initComponents();
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
        setLinkFilterToTable();
        createPopup();
        System.out.println("FilterDialogForList");
    }

    public FilterDialogForList(Dialog parent, JPanel searchPanel, Component comp, boolean modal, String classOuid)
    {
        super(parent, modal);
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        clearButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        saveButton = new JButton();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        filterNameLabel = new JLabel(DynaMOAD.getMSRString("WRD_0119", "Filter Name", 3));
        resultTable = new ResultTable();
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
        searchData = null;
        columnName = null;
        columnWidth = null;
        modelOuid = "";
        ouid = "";
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        fieldSearchList = new ArrayList();
        dateChooserButtonList = null;
        selectButtonList = null;
        searchCondition = null;
        searchConditionData = new HashMap();
        fieldInfoList = null;
        parentSearchCondition = null;
        selectCnt = 0;
        mainSplitPane = new JSplitPane();
        layout = new FormLayout("left:max(50dlu;p),4dlu,75dlu:grow(1),0dlu,12dlu", "");
        builder = new DefaultFormBuilder(layout);
        optionLabel = null;
        rdoBtnPanel = null;
        buttonGroup = null;
        publicRdoBtn = null;
        privateRdoBtn = null;
        parentSearchCondition = searchPanel;
        ouid = classOuid;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        makeSearchResultTable(ouid);
        initStatus();
        initComponents();
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
        setLinkFilterToTable();
        createPopup();
        System.out.println("FilterDialogForList");
    }

    private void initComponents()
    {
        clearButton.setActionCommand("clear");
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.addActionListener(this);
        clearButton.setDoubleBuffered(true);
        dateButton.setActionCommand("date");
        dateButton.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateButton.setToolTipText("Date select");
        dateButton.setMargin(new Insets(0, 0, 0, 0));
        dateButton.addActionListener(this);
        dateButton.setDoubleBuffered(true);
        selectButton.setActionCommand("select");
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        saveButton.setActionCommand("save");
        saveButton.setText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.addActionListener(this);
        saveButton.setDoubleBuffered(true);
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolPanel.setBackground(UIManagement.panelBackGround);
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(5));
        toolPanel.add(saveButton);
        toolPanel.add(Box.createHorizontalStrut(5));
        toolPanel.add(closeButton);
        publicRdoBtn = new JRadioButton(DynaMOAD.getMSRString("WRD_0115", "PUBLIC", 1));
        publicRdoBtn.setBackground(UIManagement.panelBackGround);
        publicRdoBtn.addItemListener(this);
        publicRdoBtn.setSelected(true);
        privateRdoBtn = new JRadioButton(DynaMOAD.getMSRString("WRD_0116", "PRIVATE", 1));
        privateRdoBtn.setBackground(UIManagement.panelBackGround);
        privateRdoBtn.addItemListener(this);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(publicRdoBtn);
        buttonGroup.add(privateRdoBtn);
        optionLabel = new JLabel(DynaMOAD.getMSRString("WRD_0117", "Ownership", 3));
        rdoBtnPanel = new JPanel();
        rdoBtnPanel.setBorder(UIManagement.borderPanel);
        rdoBtnPanel.setBackground(UIManagement.panelBackGround);
        rdoBtnPanel.setLayout(new BoxLayout(rdoBtnPanel, 0));
        rdoBtnPanel.add(Box.createHorizontalStrut(5));
        rdoBtnPanel.add(publicRdoBtn);
        rdoBtnPanel.add(Box.createHorizontalGlue());
        rdoBtnPanel.add(privateRdoBtn);
        rdoBtnPanel.add(Box.createHorizontalStrut(5));
        builder.setDefaultDialogBorder();
        builder.getPanel().setBackground(UIManagement.panelBackGround);
        builder.append(optionLabel);
        builder.append(rdoBtnPanel, 3);
        builder.nextLine();
        builder.appendSeparator(DynaMOAD.getMSRString("WRD_0118", "Search Condition", 3));
        if(!LogIn.isAdmin)
            privateRdoBtn.setSelected(true);
        valueTextField.setDoubleBuffered(true);
        optionPanel.setLayout(new BoxLayout(optionPanel, 0));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(filterNameLabel);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(valueTextField);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.setDoubleBuffered(true);
        optionPanel.add(clearButton);
        scrollPane.setViewportView(resultTable.getTable());
        scrollPane.setDoubleBuffered(true);
        scrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(optionPanel, "North");
        centerPanel.add(scrollPane, "Center");
        centerPanel.setDoubleBuffered(true);
        makeSearchConditionPanel();
        conditionScrPane.getViewport().add(builder.getPanel());
        conditionScrPane.setPreferredSize(new Dimension(300, 400));
        mainPanel.setLayout(borderLO);
        mainPanel.add(toolPanel, "South");
        mainPanel.add(centerPanel, "Center");
        mainPanel.setDoubleBuffered(true);
        mainPanel.setPreferredSize(new Dimension(500, 400));
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setLeftComponent(conditionScrPane);
        mainSplitPane.setRightComponent(mainPanel);
        setTitle(DynaMOAD.getMSRString("WRD_0120", "Link Filter", 3));
        getContentPane().add(mainSplitPane);
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

    public void setClassID(String classID)
    {
        this.classID = classID;
    }

    public void setClassChangable(boolean changable)
    {
        classChangable = changable;
    }

    public void setDefaultField(String field)
    {
        defaultField = field;
    }

    public void setDefaultOperator(String operator)
    {
        defaultOperator = operator;
    }

    public void setDefaultSearchValue(String value)
    {
        defaultSearchValue = value;
    }

    public void setSearchSQL(String sql)
    {
        searchSQL = sql;
    }

    public void setResultSetter(String setter)
    {
        resultSetter = setter;
    }

    public void setResultSetterObject(Object object)
    {
        resultSetterObject = object;
    }

    public String mappingToFiledList(String ouid)
    {
        if(ouid == null)
            return null;
        String retunrString = null;
        for(int i = 0; i < fieldSearchList.size(); i++)
        {
            DOSChangeable fieldInfo = (DOSChangeable)fieldSearchList.get(i);
            if(!ouid.equals((String)fieldInfo.get("ouid")))
                continue;
            retunrString = (String)fieldInfo.get("name");
            break;
        }

        return retunrString;
    }

    public void setLinkFilterToTable()
    {
        try
        {
            ArrayList pubFieldColumn = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid);
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid);
            ArrayList valueList = new ArrayList();
            Boolean booleanForTable = null;
            if(pubFieldColumn == null && fieldColumn == null)
                return;
            searchData.clear();
            if(pubFieldColumn != null)
            {
                for(int i = 0; i < pubFieldColumn.size(); i++)
                {
                    ArrayList pubFilterValue = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + (String)pubFieldColumn.get(i));
                    if(pubFilterValue == null)
                        return;
                    valueList.add(0, "PUBLIC");
                    valueList.add(1, (String)pubFieldColumn.get(i));
                    for(int k = 0; k < pubFilterValue.size(); k++)
                    {
                        for(int j = 0; j < fieldInfoList.size(); j++)
                        {
                            DOSChangeable dosInfoList = (DOSChangeable)fieldInfoList.get(j);
                            String ouidStr = (String)pubFilterValue.get(k);
                            if(ouidStr.equals((String)dosInfoList.get("ouid")))
                            {
                                String value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + (String)pubFieldColumn.get(i) + "/" + ouidStr);
                                if("md$status".equals(dosInfoList.get("code")))
                                {
                                    String tmpStr = (String)statusCodeMap.get(value);
                                    value = tmpStr;
                                } else
                                if(((String)dosInfoList.get("ouid")).equals("version.condition.type"))
                                {
                                    String tmpStr = "";
                                    if(value.equals("wip"))
                                        tmpStr = "Work In Progress";
                                    else
                                    if(value.equals("released"))
                                        tmpStr = "Last Released";
                                    else
                                    if(value.equals("all"))
                                        tmpStr = "All Version";
                                    else
                                    if(value.equals("cdate"))
                                        tmpStr = "Created Date";
                                    else
                                        tmpStr = "Modified Date";
                                    value = tmpStr;
                                }
                                if(dosInfoList.get("type") != null)
                                {
                                    getClass();
                                    if(((Byte)dosInfoList.get("type")).byteValue() == 1)
                                        booleanForTable = new Boolean(value);
                                }
                                if(k != 0)
                                {
                                    if(booleanForTable == null)
                                        valueList.set(j + 2, value);
                                    else
                                        valueList.set(j + 2, booleanForTable);
                                    booleanForTable = null;
                                } else
                                {
                                    if(booleanForTable == null)
                                        valueList.add(j + 2, value);
                                    else
                                        valueList.add(j + 2, booleanForTable);
                                    booleanForTable = null;
                                }
                            } else
                            if(k == 0)
                                valueList.add(j + 2, null);
                        }

                    }

                    searchData.add(valueList.clone());
                    valueList.clear();
                }

            }
            if(fieldColumn != null)
            {
                valueList = new ArrayList();
                for(int i = 0; i < fieldColumn.size(); i++)
                {
                    ArrayList filterValue = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + (String)fieldColumn.get(i));
                    if(filterValue == null)
                        return;
                    valueList.add(0, "PRIVATE");
                    valueList.add(1, (String)fieldColumn.get(i));
                    for(int k = 0; k < filterValue.size(); k++)
                    {
                        for(int j = 0; j < fieldInfoList.size(); j++)
                        {
                            DOSChangeable dosInfoList = (DOSChangeable)fieldInfoList.get(j);
                            String ouidStr = (String)filterValue.get(k);
                            if(ouidStr.equals((String)dosInfoList.get("ouid")))
                            {
                                String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + (String)fieldColumn.get(i) + "/" + ouidStr);
                                if("md$status".equals(dosInfoList.get("code")))
                                {
                                    String tmpStr = (String)statusCodeMap.get(value);
                                    value = tmpStr;
                                } else
                                if(((String)dosInfoList.get("ouid")).equals("version.condition.type"))
                                {
                                    String tmpStr = "";
                                    if(value.equals("wip"))
                                        tmpStr = "Work In Progress";
                                    else
                                    if(value.equals("released"))
                                        tmpStr = "Last Released";
                                    else
                                    if(value.equals("all"))
                                        tmpStr = "All Version";
                                    else
                                    if(value.equals("cdate"))
                                        tmpStr = "Created Date";
                                    else
                                        tmpStr = "Modified Date";
                                    value = tmpStr;
                                }
                                if(dosInfoList.get("type") != null)
                                {
                                    getClass();
                                    if(((Byte)dosInfoList.get("type")).byteValue() == 1)
                                        booleanForTable = new Boolean(value);
                                }
                                if(k != 0)
                                {
                                    if(booleanForTable == null)
                                        valueList.set(j + 2, value);
                                    else
                                        valueList.set(j + 2, booleanForTable);
                                    booleanForTable = null;
                                } else
                                {
                                    if(booleanForTable == null)
                                        valueList.add(j + 2, value);
                                    else
                                        valueList.add(j + 2, booleanForTable);
                                    booleanForTable = null;
                                }
                            } else
                            if(k == 0)
                                valueList.add(j + 2, null);
                        }

                    }

                    searchData.add(valueList.clone());
                    valueList.clear();
                }

            }
            resultTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void makeSearchResultTable(String classOuid)
    {
        try
        {
            ArrayList tableFieldList = new ArrayList();
            fieldInfoList = dos.listFieldInClass(classOuid);
            if(fieldInfoList != null)
            {
                Utils.sort(fieldInfoList);
                for(int i = 0; i < fieldInfoList.size(); i++)
                {
                    DOSChangeable testDos = (DOSChangeable)fieldInfoList.get(i);
                    tableFieldList.add(DynaMOAD.getMSRString((String)testDos.get("code"), (String)testDos.get("title"), 0));
                }

                DOSChangeable classInfo = dos.getClass(classOuid);
                DOSChangeable tmpDOS = null;
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    tmpDOS = new DOSChangeable();
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                    tmpDOS.put("ouid", "version.condition.type");
                    tmpDOS.put("name", "Version Type");
                    tmpDOS.put("description", "");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("type", new Byte((byte)13));
                    tmpDOS.put("is.visible", Boolean.TRUE);
                    fieldInfoList.add(2, tmpDOS);
                    tableFieldList.add(2, DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0041", "Version Date From", 3));
                    tmpDOS.put("ouid", "version.condition.date.from");
                    tmpDOS.put("name", "Version Date From");
                    tmpDOS.put("description", "");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("type", new Byte((byte)13));
                    tmpDOS.put("is.visible", Boolean.TRUE);
                    fieldInfoList.add(3, tmpDOS);
                    tableFieldList.add(3, DynaMOAD.getMSRString("WRD_0041", "Version Date From", 3));
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0042", "Version Date To", 3));
                    tmpDOS.put("ouid", "version.condition.date.to");
                    tmpDOS.put("name", "Version Date To");
                    tmpDOS.put("description", "");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("type", new Byte((byte)13));
                    tmpDOS.put("is.visible", Boolean.TRUE);
                    fieldInfoList.add(4, tmpDOS);
                    tableFieldList.add(4, DynaMOAD.getMSRString("WRD_0042", "Version Date To", 3));
                    tmpDOS = null;
                }
            }
            searchData = new ArrayList();
            columnName = new ArrayList();
            columnWidth = new ArrayList();
            selectedRows = new int[tableFieldList.size() + 2];
            columnName.add(DynaMOAD.getMSRString("ownership", "Ownership", 3));
            columnWidth.add(new Integer(80));
            selectedRows[0] = 0;
            columnName.add(DynaMOAD.getMSRString("filterName", "Filter Name", 3));
            columnWidth.add(new Integer(80));
            selectedRows[1] = 1;
            for(int i = 0; i < tableFieldList.size(); i++)
            {
                columnName.add((String)tableFieldList.get(i));
                columnWidth.add(new Integer(80));
                selectedRows[i + 2] = i + 2;
            }

            resultTable = new ResultTable(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
            resultTable.setColumnSequence(selectedRows);
            resultTable.setIndexColumn(0);
            resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setResultData(ArrayList resultData)
    {
        if(searchData != null)
            searchData.clear();
        ArrayList tmpList = new ArrayList();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                ArrayList tmpList2 = (ArrayList)resultData.get(i);
                for(int j = 0; j < tmpList2.size(); j++)
                    tmpList.add(tmpList2.get(j));

                searchData.add(tmpList.clone());
                tmpList.clear();
            }

        }
        resultTable.changeTableData();
    }

    public void makeSearchConditionPanel()
    {
        try
        {
            selectButtonList = new ArrayList();
            dateChooserButtonList = new ArrayList();
            fieldSearchList = dos.listFieldInClass(ouid);
            if(fieldSearchList == null)
                return;
            DOSChangeable classInfo = dos.getClass(ouid);
            int i = 0;
            DOSChangeable tmpDOS = new DOSChangeable();
            if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
            {
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                tmpDOS.put("name", "Version Type");
                tmpDOS.put("ouid", "version.condition.type");
                tmpDOS.put("description", "");
                tmpDOS.put("index", new Integer(-985));
                tmpDOS.put("type", new Byte((byte)13));
                tmpDOS.put("is.visible", Boolean.TRUE);
                fieldSearchList.add(2, tmpDOS);
                tmpDOS = null;
            }
            Utils.sort(fieldSearchList);
            String division01 = "Last Released";
            String division02 = "Work In Progress";
            String division05 = "All Version";
            String division03 = "Created Date";
            String division04 = "Modified Date";
            String versionType[] = {
                "", division01, division02, division05, division03, division04
            };
            String trueStr = "true";
            String falseStr = "false";
            String trueOrFalse[] = {
                "", trueStr, falseStr
            };
            Boolean isVisible = null;
            i = -1;
            for(int j = 0; j < fieldSearchList.size(); j++)
            {
                DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(j);
                if(fieldData != null)
                {
                    isVisible = (Boolean)fieldData.get("is.visible");
                    if(Boolean.FALSE.equals(isVisible))
                    {
                        fieldData = null;
                    } else
                    {
                        i++;
                        JLabel advancedTestLabel = new JLabel();
                        JTextField advancedTestTextField = new JTextField();
                        JButton dateSelectButton = new JButton();
                        dateSelectButton.setMargin(new Insets(0, 0, 0, 0));
                        JButton selectButtonByType = new JButton();
                        selectButtonByType.setMargin(new Insets(0, 0, 0, 0));
                        advancedTestLabel = new JLabel(DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0));
                        if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type") && ((Byte)fieldData.get("type")).byteValue() != 1)
                        {
                            if(!((String)fieldData.get("ouid")).equals("md$cdate") && !((String)fieldData.get("ouid")).equals("md$mdate") && ((Byte)fieldData.get("type")).byteValue() != 22 && ((Byte)fieldData.get("type")).byteValue() != 21)
                            {
                                hoardData.put((String)fieldData.get("name"), advancedTestTextField);
                                hoardData.put((String)fieldData.get("name") + "^ouid", (String)fieldData.get("ouid"));
                                if(((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                                {
                                    selectCnt++;
                                    selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
                                    selectButtonByType.setMinimumSize(new Dimension(18, 22));
                                    selectButtonByType.setPreferredSize(new Dimension(18, 22));
                                    if(((Byte)fieldData.get("type")).byteValue() == 16)
                                        selectButtonByType.setActionCommand("ObjectSelectButtonClick_" + selectCnt + "_" + (String)fieldData.get("name") + "/" + (String)fieldData.get("type.ouid@class") + "/" + (String)fieldData.get("name"));
                                    else
                                    if(((Byte)fieldData.get("type")).byteValue() == 24)
                                        selectButtonByType.setActionCommand("CodeSelectButtonClick_" + selectCnt + "_" + (String)fieldData.get("name") + "/" + (String)fieldData.get("type.ouid@class") + "/" + (String)fieldData.get("name"));
                                    else
                                    if(((Byte)fieldData.get("type")).byteValue() == 25)
                                        selectButtonByType.setActionCommand("CodeReferenceSelectButtonClick_" + selectCnt + "_" + (String)fieldData.get("name") + "/" + fieldData.get("ouid") + "/" + (String)fieldData.get("name"));
                                    selectButtonByType.addActionListener(this);
                                    selectButtonList.add(selectButtonByType.getActionCommand());
                                    builder.append(advancedTestLabel, advancedTestTextField, selectButtonByType);
                                    builder.nextLine();
                                } else
                                {
                                    builder.append(advancedTestLabel);
                                    builder.append(advancedTestTextField, 3);
                                    builder.nextLine();
                                }
                            } else
                            {
                                hoardData.put((String)fieldData.get("name"), advancedTestTextField);
                                dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                dateSelectButton.setActionCommand("DateChooserButtonClick_" + (String)fieldData.get("name"));
                                dateSelectButton.addActionListener(this);
                                dateChooserButtonList.add((String)fieldData.get("name"));
                                builder.append(advancedTestLabel, advancedTestTextField, dateSelectButton);
                                builder.nextLine();
                            }
                        } else
                        {
                            JComboBox advancedComboBox;
                            if("md$status".equals(fieldData.get("code")))
                            {
                                advancedComboBox = new JComboBox(statusArray);
                                builder.append(advancedTestLabel);
                                builder.append(advancedComboBox, 3);
                                builder.nextLine();
                            } else
                            if(((String)fieldData.get("ouid")).equals("version.condition.type"))
                            {
                                advancedComboBox = new JComboBox(versionType);
                                advancedComboBox.setSelectedItem("Work In Progress");
                                advancedComboBox.setActionCommand((String)fieldData.get("name"));
                                advancedComboBox.addActionListener(this);
                                builder.append(advancedTestLabel);
                                builder.append(advancedComboBox, 3);
                                builder.nextLine();
                                advancedTestLabel = new JLabel("From");
                                advancedTestTextField.setEnabled(false);
                                advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                hoardData.put("Version Date From", advancedTestTextField);
                                dateSelectButton.setEnabled(false);
                                dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date From");
                                dateSelectButton.addActionListener(this);
                                hoardData.put("VersionDateFromButton", dateSelectButton);
                                dateChooserButtonList.add("Version Date From");
                                builder.append(advancedTestLabel, advancedTestTextField, dateSelectButton);
                                builder.nextLine();
                                advancedTestLabel = new JLabel("To");
                                advancedTestTextField = new JTextField();
                                advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                advancedTestTextField.setEnabled(false);
                                hoardData.put("Version Date To", advancedTestTextField);
                                dateSelectButton = new JButton();
                                dateSelectButton.setEnabled(false);
                                dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date To");
                                dateSelectButton.addActionListener(this);
                                hoardData.put("VersionDateToButton", dateSelectButton);
                                dateChooserButtonList.add("Version Date To");
                                builder.append(advancedTestLabel, advancedTestTextField, dateSelectButton);
                                builder.nextLine();
                            } else
                            if(((Byte)fieldData.get("type")).byteValue() == 1)
                            {
                                advancedComboBox = new JComboBox(trueOrFalse);
                                builder.append(advancedTestLabel);
                                builder.append(advancedComboBox, 3);
                                builder.nextLine();
                            } else
                            {
                                advancedComboBox = new JComboBox();
                                builder.append(advancedTestLabel);
                                builder.append(advancedComboBox, 3);
                                builder.nextLine();
                            }
                            hoardData.put((String)fieldData.get("name"), advancedComboBox);
                        }
                    }
                }
            }

            if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
            {
                tmpDOS = new DOSChangeable();
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0041", "Version Date From", 3));
                tmpDOS.put("name", "Version Date From");
                tmpDOS.put("ouid", "version.condition.date.from");
                tmpDOS.put("type", new Byte((byte)13));
                fieldSearchList.add(tmpDOS);
                tmpDOS = null;
                tmpDOS = new DOSChangeable();
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0042", "Version Date To", 3));
                tmpDOS.put("name", "Version Date To");
                tmpDOS.put("ouid", "version.condition.date.to");
                tmpDOS.put("type", new Byte((byte)13));
                fieldSearchList.add(tmpDOS);
                tmpDOS = null;
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
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
        removeWindowListener(this);
        clearButton.removeActionListener(this);
        dateButton.removeActionListener(this);
        selectButton.removeActionListener(this);
        saveButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        publicRdoBtn.removeItemListener(this);
        privateRdoBtn.removeItemListener(this);
        setVisible(false);
        dispose();
        if(parentSearchCondition instanceof SearchCondition4CADIntegration)
            ((SearchCondition4CADIntegration)parentSearchCondition).setFilterDialogToNull();
    }

    public void clearAllFieldValue()
    {
        for(int i = 0; i < fieldSearchList.size(); i++)
        {
            DOSChangeable fieldInfo = (DOSChangeable)fieldSearchList.get(i);
            String title = (String)fieldInfo.get("name");
            if(hoardData.get(title) instanceof JTextField)
                ((JTextField)hoardData.get(title)).setText("");
            else
            if(hoardData.get(title) instanceof JComboBox)
                ((JComboBox)hoardData.get(title)).setSelectedIndex(-1);
        }

    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        try
        {
            if(hoardData.get(sign) instanceof JTextField)
            {
                String fieldOuid = (String)hoardData.get(sign + "^ouid");
                DOSChangeable fieldData = null;
                if(fieldOuid != null)
                {
                    for(int i = 0; i < fieldSearchList.size(); i++)
                    {
                        fieldData = (DOSChangeable)fieldSearchList.get(i);
                        if(!fieldData.get("ouid").equals(fieldOuid))
                            continue;
                        if(((Byte)fieldData.get("type")).byteValue() == 16)
                        {
                            DOSChangeable dosObject = dos.get(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText((String)dosObject.get("md$number"));
                            searchConditionData.put(fieldOuid, (String)dosObject.get("ouid"));
                            break;
                        }
                        if(((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                        {
                            DOSChangeable dosCodeItem = dos.getCodeItem(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText(dosCodeItem.get("name") + " [" + dosCodeItem.get("codeitemid") + "]");
                            searchConditionData.put(fieldOuid, dosCodeItem.get("ouid"));
                            break;
                        }
                        ((JTextField)hoardData.get(sign)).setText(selectOuid);
                    }

                } else
                {
                    ((JTextField)hoardData.get(sign)).setText(selectOuid);
                }
            } else
            if(hoardData.get(sign) instanceof JComboBox)
                if(sign.equals("md$status"))
                {
                    String values = (String)statusCodeMap.get(selectOuid);
                    ((JComboBox)hoardData.get(sign)).setSelectedItem(values);
                } else
                if(sign.equals("Version Type"))
                {
                    if(selectOuid != null)
                    {
                        String values = "";
                        if(selectOuid.equals("released"))
                            values = "Last Released";
                        else
                        if(selectOuid.equals("wip"))
                            values = "Work In Progress";
                        else
                        if(selectOuid.equals("all"))
                            values = "All Version";
                        else
                        if(selectOuid.equals("cdate"))
                            values = "Created Date";
                        else
                            values = "Modified Date";
                        ((JComboBox)hoardData.get(sign)).setSelectedItem(values);
                    }
                } else
                {
                    ((JComboBox)hoardData.get(sign)).setSelectedItem(selectOuid);
                }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setConditionField(String str)
    {
        valueTextField.setText(str);
    }

    public void createPopup()
    {
        filterPopupMenu = new JPopupMenu();
        menuSelect = new JMenuItem();
        menuSelect.setText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        menuSelect.setActionCommand("select");
        menuSelect.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
        menuSelect.setAlignmentX(0.0F);
        filterPopupMenu.add(menuSelect);
        menuSelect.addActionListener(this);
        menuDelete = new JMenuItem();
        menuDelete.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        menuDelete.setActionCommand("delete");
        menuDelete.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        menuDelete.setAlignmentX(0.0F);
        filterPopupMenu.add(menuDelete);
        menuDelete.addActionListener(this);
        java.awt.event.MouseListener LinkMousePopup = new PopupLink();
        if(resultTable != null)
            resultTable.getTable().addMouseListener(LinkMousePopup);
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = evt.getActionCommand();
        if(command == null)
            windowClosing(null);
        else
        if(command.equals("close"))
            windowClosing(null);
        else
        if(command.equals("select"))
        {
            searchCondition = new HashMap();
            for(int i = 0; i < fieldSearchList.size(); i++)
            {
                DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(i);
                String text;
                if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type"))
                {
                    getClass();
                    if(((Byte)fieldData.get("type")).byteValue() != 1)
                    {
                        text = (String)fieldData.get("name");
                        if(text != null && (JTextField)hoardData.get(text) != null)
                        {
                            text = ((JTextField)hoardData.get(text)).getText();
                            if(Utils.isNullString(text))
                                searchCondition.remove((String)fieldData.get("ouid"));
                            else
                            if((((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25) && !text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), searchConditionData.get((String)fieldData.get("ouid")));
                            else
                            if(!text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), text);
                        }
                        continue;
                    }
                }
                text = (String)fieldData.get("name");
                if(text != null && (JComboBox)hoardData.get(text) != null)
                {
                    text = (String)((JComboBox)hoardData.get(text)).getSelectedItem();
                    if(Utils.isNullString(text))
                        searchCondition.remove((String)fieldData.get("ouid"));
                    else
                    if(((Byte)fieldData.get("type")).equals(new Byte("1")))
                    {
                        if(text.equals("true"))
                            searchCondition.put((String)fieldData.get("ouid"), new Boolean(true));
                        else
                            searchCondition.put((String)fieldData.get("ouid"), new Boolean(false));
                    } else
                    if("md$status".equals(fieldData.get("code")))
                    {
                        String value = (String)statusValueMap.get(text);
                        searchCondition.put((String)fieldData.get("ouid"), value);
                    } else
                    if(text.equals("Last Released"))
                        searchCondition.put((String)fieldData.get("ouid"), "released");
                    else
                    if(text.equals("Work In Progress"))
                        searchCondition.put((String)fieldData.get("ouid"), "wip");
                    else
                    if(text.equals("All Version"))
                        searchCondition.put((String)fieldData.get("ouid"), "all");
                    else
                    if(text.equals("Created Date"))
                    {
                        searchCondition.put((String)fieldData.get("ouid"), "cdate");
                        searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                        searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                    } else
                    if(text.equals("Modified Date"))
                    {
                        searchCondition.put((String)fieldData.get("ouid"), "mdate");
                        searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                        searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                    }
                }
            }

            if(parentSearchCondition instanceof SearchCondition)
                ((SearchCondition)parentSearchCondition).setFilterInLink(searchCondition);
            else
            if(parentSearchCondition instanceof SearchCondition4CADIntegration)
                ((SearchCondition4CADIntegration)parentSearchCondition).setFilterInLink(searchCondition);
            else
            if(parentSearchCondition instanceof SearchConditionPanel)
                ((SearchConditionPanel)parentSearchCondition).setFilterInLink(searchCondition);
            windowClosing(null);
        } else
        if(command.equals("date"))
        {
            new DynaDateChooser(this);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                setConditionField(sdfYMD.format(date));
            else
                setConditionField("");
        } else
        if(command.equals("save"))
            try
            {
                searchCondition = new HashMap();
                for(int i = 0; i < fieldSearchList.size(); i++)
                {
                    DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(i);
                    if(fieldData == null || fieldData.getValueMap() == null || fieldData.getValueMap().size() == 0)
                    {
                        fieldData = null;
                        continue;
                    }
                    String text;
                    if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type"))
                    {
                        getClass();
                        if(((Byte)fieldData.get("type")).byteValue() != 1)
                        {
                            text = (String)fieldData.get("name");
                            if(text != null && (JTextField)hoardData.get(text) != null)
                            {
                                text = ((JTextField)hoardData.get(text)).getText();
                                if(Utils.isNullString(text))
                                    searchCondition.remove((String)fieldData.get("ouid"));
                                else
                                if((((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25) && !text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), searchConditionData.get((String)fieldData.get("ouid")));
                                else
                                if(!text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), text);
                            }
                            continue;
                        }
                    }
                    text = (String)fieldData.get("name");
                    if(text != null && (JComboBox)hoardData.get(text) != null)
                    {
                        text = (String)((JComboBox)hoardData.get(text)).getSelectedItem();
                        if(Utils.isNullString(text))
                            searchCondition.remove((String)fieldData.get("ouid"));
                        else
                        if(((Byte)fieldData.get("type")).equals(new Byte("1")))
                        {
                            if(text.equals("true"))
                                searchCondition.put((String)fieldData.get("ouid"), new Boolean(true));
                            else
                                searchCondition.put((String)fieldData.get("ouid"), new Boolean(false));
                        } else
                        if("md$status".equals(fieldData.get("code")))
                        {
                            String value = (String)statusValueMap.get(text);
                            searchCondition.put((String)fieldData.get("ouid"), value);
                        } else
                        if(text.equals("Last Released"))
                            searchCondition.put((String)fieldData.get("ouid"), "released");
                        else
                        if(text.equals("Work In Progress"))
                            searchCondition.put((String)fieldData.get("ouid"), "wip");
                        else
                        if(text.equals("All Version"))
                            searchCondition.put((String)fieldData.get("ouid"), "all");
                        else
                        if(text.equals("Created Date"))
                        {
                            searchCondition.put((String)fieldData.get("ouid"), "cdate");
                            searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                            searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                        } else
                        if(text.equals("Modified Date"))
                        {
                            searchCondition.put((String)fieldData.get("ouid"), "mdate");
                            searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                            searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                        }
                    }
                }

                if(Utils.isNullString(valueTextField.getText()) || searchCondition.size() == 0)
                {
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0007", "Input Filter Name.", 3), DynaMOAD.getMSRString("WRD_0004", "Information Message", 3), 1);
                    return;
                }
                if(publicRdoBtn.isSelected())
                {
                    nds.removeNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + valueTextField.getText());
                    nds.addNode("::", "WORKSPACE", "LINKFILTER", "");
                    nds.addNode("::/WORKSPACE", modelOuid, "LINKFILTER", "MODELOUID");
                    nds.addNode("::/WORKSPACE/" + modelOuid, "PUBLIC", "LINKFILTER", "PUBLIC");
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC", "LISTFILTER", "LINKFILTER", "LISTFILTER");
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER", ouid, "LINKFILTER", ouid);
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid, valueTextField.getText(), "LINKFILTER", valueTextField.getText());
                    for(int i = 0; i < fieldSearchList.size(); i++)
                    {
                        DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(i);
                        if(searchCondition.get((String)fieldData.get("ouid")) != null)
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof String)
                                nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", (String)searchCondition.get((String)fieldData.get("ouid")));
                            else
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof Boolean)
                                nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", ((Boolean)searchCondition.get((String)fieldData.get("ouid"))).toString());
                    }

                    setLinkFilterToTable();
                } else
                if(privateRdoBtn.isSelected())
                {
                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + valueTextField.getText());
                    nds.addNode("::", "WORKSPACE", "LINKFILTER", "");
                    nds.addNode("::/WORKSPACE", LogIn.userID, "LINKFILTER", "USERID");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID, "LISTFILTER", "LINKFILTER", "LISTFILTER");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER", ouid, "LINKFILTER", ouid);
                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid, valueTextField.getText(), "LINKFILTER", valueTextField.getText());
                    for(int i = 0; i < fieldSearchList.size(); i++)
                    {
                        DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(i);
                        if(searchCondition.get((String)fieldData.get("ouid")) != null)
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof String)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", (String)searchCondition.get((String)fieldData.get("ouid")));
                            else
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof Boolean)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", ((Boolean)searchCondition.get((String)fieldData.get("ouid"))).toString());
                    }

                    setLinkFilterToTable();
                }
                if(parentSearchCondition instanceof SearchCondition)
                    ((SearchCondition)parentSearchCondition).stateChanged(null);
                else
                if(parentSearchCondition instanceof SearchCondition4CADIntegration)
                    ((SearchCondition4CADIntegration)parentSearchCondition).stateChanged(null);
                else
                if(parentSearchCondition instanceof SearchConditionPanel)
                    ((SearchConditionPanel)parentSearchCondition).clearAllFieldValue();
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0002", "Modification Complete.", 0), DynaMOAD.getMSRString("WRD_0004", "Information", 3), 1);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("clear"))
            clearAllFieldValue();
        else
        if(command.equals("Version Type"))
        {
            if(hoardData.get("Version Type") == null || ((JComboBox)hoardData.get("Version Type")).getSelectedItem() == null)
                return;
            if(((String)((JComboBox)hoardData.get("Version Type")).getSelectedItem()).equals("Created Date") || ((String)((JComboBox)hoardData.get("Version Type")).getSelectedItem()).equals("Modified Date"))
            {
                ((JTextField)hoardData.get("Version Date From")).setEnabled(true);
                ((JTextField)hoardData.get("Version Date From")).setBackground(Color.white);
                ((JTextField)hoardData.get("Version Date To")).setEnabled(true);
                ((JTextField)hoardData.get("Version Date To")).setBackground(Color.white);
                ((JButton)hoardData.get("VersionDateFromButton")).setEnabled(true);
                ((JButton)hoardData.get("VersionDateToButton")).setEnabled(true);
            } else
            {
                ((JTextField)hoardData.get("Version Date From")).setEnabled(false);
                ((JTextField)hoardData.get("Version Date From")).setBackground(UIManagement.textBoxNotEditableBack);
                ((JTextField)hoardData.get("Version Date To")).setEnabled(false);
                ((JTextField)hoardData.get("Version Date To")).setBackground(UIManagement.textBoxNotEditableBack);
                ((JButton)hoardData.get("VersionDateFromButton")).setEnabled(false);
                ((JButton)hoardData.get("VersionDateToButton")).setEnabled(false);
            }
        } else
        if(command.equals("delete"))
            try
            {
                if(Utils.isNullString(valueTextField.getText()))
                    return;
                Object option[] = {
                    "\uC608", "\uC544\uB2C8\uC624"
                };
                int res = JOptionPane.showOptionDialog(this, "\uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "\uC0AD\uC81C", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[1]);
                if(res != 0)
                    return;
                if(publicRdoBtn.isSelected())
                    nds.removeNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + valueTextField.getText());
                else
                if(privateRdoBtn.isSelected())
                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + valueTextField.getText());
                clearAllFieldValue();
                searchData.clear();
                resultTable.changeTableData();
                setLinkFilterToTable();
                if(parentSearchCondition instanceof SearchCondition)
                    ((SearchCondition)parentSearchCondition).stateChanged(null);
                else
                if(parentSearchCondition instanceof SearchCondition4CADIntegration)
                    ((SearchCondition4CADIntegration)parentSearchCondition).stateChanged(null);
                else
                if(parentSearchCondition instanceof SearchConditionPanel)
                    ((SearchConditionPanel)parentSearchCondition).clearAllFieldValue();
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0013", "Deletion complete.", 0), DynaMOAD.getMSRString("WRD_0004", "Information", 3), 1);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.startsWith("ObjectSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                    String classOuid2 = (String)testList.get(1);
                    if(classOuid2 != null)
                    {
                        SmallSearchDialog newSmall = new SmallSearchDialog(this, (Component)evt.getSource(), true, classOuid2, command);
                        newSmall.setVisible(true);
                    } else
                    {
                        System.out.println("type class ouid is Null");
                    }
                }

        } else
        if(command.startsWith("CodeSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                    String codeOuid = (String)testList.get(1);
                    if(codeOuid != null && !codeOuid.equals("null"))
                    {
                        CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)evt.getSource(), true, codeOuid, command);
                        newSmall.setVisible(true);
                    } else
                    {
                        System.out.println("code ouid is Null");
                    }
                }

        } else
        if(command.startsWith("CodeReferenceSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                    String codeOuid = (String)testList.get(1);
                    if(Utils.isNullString(codeOuid))
                        return;
                    codeOuid = (String)searchConditionData.get(codeOuid);
                    if(Utils.isNullString(codeOuid))
                        return;
                    try
                    {
                        DOSChangeable code = dos.getCodeItem(codeOuid);
                        if(code == null)
                            return;
                        codeOuid = (String)code.get("description");
                        code.clear();
                        code = null;
                        if(Utils.isNullString(codeOuid))
                            return;
                        code = dos.getCodeWithName(codeOuid);
                        codeOuid = (String)code.get("ouid");
                        code.clear();
                        code = null;
                        if(Utils.isNullString(codeOuid))
                            return;
                    }
                    catch(IIPRequestException ie)
                    {
                        System.err.println(ie);
                    }
                    if(codeOuid != null && !codeOuid.equals("null"))
                    {
                        CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)evt.getSource(), true, codeOuid, command);
                        newSmall.setVisible(true);
                    } else
                    {
                        System.out.println("code ouid is Null");
                    }
                }

        } else
        if(command.startsWith("DateChooserButtonClick"))
        {
            int tmpInt = -1;
            for(int i = 0; i < dateChooserButtonList.size(); i++)
            {
                if(!command.endsWith((String)dateChooserButtonList.get(i)))
                    continue;
                tmpInt = i;
                break;
            }

            if(tmpInt > -1)
            {
                String fieldName = (String)dateChooserButtonList.get(tmpInt);
                if(fieldName != null)
                {
                    new DynaDateChooser(this);
                    java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                    if(date != null)
                        setFieldInDateField(sdfYMD.format(date), fieldName);
                    else
                        setFieldInDateField("", fieldName);
                } else
                {
                    System.out.println("type class ouid is Null");
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getItemSelectable();
        int state = e.getStateChange();
        if(source == publicRdoBtn && state == 1)
        {
            if(LogIn.isAdmin)
            {
                saveButton.setEnabled(true);
                if(resultTable.getTable().getSelectedRowCount() > 0)
                    menuDelete.setEnabled(true);
            } else
            {
                saveButton.setEnabled(false);
                if(resultTable.getTable().getSelectedRowCount() > 0)
                    menuDelete.setEnabled(false);
            }
        } else
        {
            saveButton.setEnabled(true);
            if(resultTable.getTable().getSelectedRowCount() > 0)
                menuDelete.setEnabled(true);
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

    public void setFieldInObject(String sign, String selectOuid, String number)
    {
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '/');
        String mapId = (String)list.get(2);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(number);
        searchConditionData.put((String)hoardData.get(mapId + "^ouid"), selectOuid);
        field = null;
        list.clear();
        list = null;
    }

    public void setFieldInCode(String sign, String selectOuid, String name)
    {
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '/');
        String mapId = (String)Utils.tokenizeMessage((String)list.get(2), '^').get(0);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(name);
        searchConditionData.put((String)hoardData.get(mapId + "^ouid"), selectOuid);
        field = null;
        list.clear();
        list = null;
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
                    if(table.equals(resultTable))
                    {
                        if(parentSearchCondition instanceof SearchCondition4CADIntegration)
                            ((SearchCondition4CADIntegration)parentSearchCondition).clearAllFieldValue();
                        else
                        if(parentSearchCondition instanceof SearchConditionPanel)
                            ((SearchConditionPanel)parentSearchCondition).clearAllFieldValue();
                        clearAllFieldValue();
                        valueTextField.setText((String)selectedData.get(1));
                        String initKey = null;
                        if(((String)selectedData.get(0)).equals("PUBLIC"))
                        {
                            initKey = "::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + ouid + "/" + (String)selectedData.get(1);
                            publicRdoBtn.setSelected(true);
                        } else
                        if(((String)selectedData.get(0)).equals("PRIVATE"))
                        {
                            initKey = "::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + ouid + "/" + (String)selectedData.get(1);
                            privateRdoBtn.setSelected(true);
                        }
                        ArrayList filterValue = nds.getChildNodeList(initKey);
                        for(int i = 0; i < filterValue.size(); i++)
                        {
                            String value = nds.getValue(initKey + "/" + (String)filterValue.get(i));
                            if(parentSearchCondition instanceof SearchCondition4CADIntegration)
                                ((SearchCondition4CADIntegration)parentSearchCondition).setFieldInDateField(value, mappingToFiledList((String)filterValue.get(i)));
                            else
                            if(parentSearchCondition instanceof SearchConditionPanel)
                                ((SearchConditionPanel)parentSearchCondition).setFieldInDateField(value, mappingToFiledList((String)filterValue.get(i)));
                            setFieldInDateField(value, mappingToFiledList((String)filterValue.get(i)));
                        }

                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        }
    }

    private JPanel mainPanel;
    private JPanel toolPanel;
    private JButton clearButton;
    private JButton selectButton;
    private JButton closeButton;
    private JButton dateButton;
    private JButton saveButton;
    private JPanel centerPanel;
    private JPanel optionPanel;
    private JTextField valueTextField;
    private JScrollPane scrollPane;
    private JScrollPane conditionScrPane;
    private JLabel filterNameLabel;
    private ResultTable resultTable;
    private JPopupMenu filterPopupMenu;
    private JMenuItem menuSelect;
    private JMenuItem menuDelete;
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
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_UTF = 13;
    private final byte DATATYPE_BOOLEAN = 1;
    private ArrayList dateChooserButtonList;
    private ArrayList selectButtonList;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private HashMap searchCondition;
    private HashMap searchConditionData;
    private ArrayList fieldInfoList;
    private JPanel parentSearchCondition;
    private int selectCnt;
    final int title_width = 100;
    final int title_height = 20;
    final int DIV_SIZE = 300;
    private JSplitPane mainSplitPane;
    private FormLayout layout;
    private DefaultFormBuilder builder;
    private JLabel optionLabel;
    private JPanel rdoBtnPanel;
    private ButtonGroup buttonGroup;
    private JRadioButton publicRdoBtn;
    private JRadioButton privateRdoBtn;
    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;




}