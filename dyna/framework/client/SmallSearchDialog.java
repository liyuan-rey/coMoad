// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SmallSearchDialog.java

package dyna.framework.client;

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
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            CodeChooserUser, DynaMOAD, UIManagement, LogIn, 
//            UIBuilder, AssoClassUI, AuthoritySearch, SearchCondition, 
//            SearchCondition4CADIntegration, FilterDialogForList, FilterDialogForLink, AdvancedFilterDialogForList, 
//            CodeSelectDialog

public class SmallSearchDialog extends JDialog
    implements ActionListener, WindowListener, MouseListener, CodeChooserUser
{

    public SmallSearchDialog(Frame parent, Component comp, boolean modal, String ouid, String signStr)
    {
        super(parent, modal);
        handCursor = new Cursor(12);
        mainSplitPane = new JSplitPane();
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        resultTable = new Table();
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
        fieldListForSearchCondition = null;
        classFieldList = null;
        this.ouid = "";
        fieldListForSearchResult = new ArrayList();
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        this.signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        selectCnt = 0;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        parentFr = parent;
        this.ouid = ouid;
        this.signStr = signStr;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            classInfo = dos.getClass(this.ouid);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        makeSearchConditionPanel();
        makeSearchResultTable(this.ouid);
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
    }

    public SmallSearchDialog(JPanel parent, Component comp, boolean modal, String ouid, String signStr)
    {
        super(null, modal);
        handCursor = new Cursor(12);
        mainSplitPane = new JSplitPane();
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        resultTable = new Table();
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
        fieldListForSearchCondition = null;
        classFieldList = null;
        this.ouid = "";
        fieldListForSearchResult = new ArrayList();
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        this.signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        selectCnt = 0;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        parentFr = parent;
        this.ouid = ouid;
        this.signStr = signStr;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            classInfo = dos.getClass(this.ouid);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        makeSearchConditionPanel();
        makeSearchResultTable(this.ouid);
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
    }

    public SmallSearchDialog(Dialog parent, Component comp, boolean modal, String ouid, String signStr)
    {
        super(parent, modal);
        handCursor = new Cursor(12);
        mainSplitPane = new JSplitPane();
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        resultTable = new Table();
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
        fieldListForSearchCondition = null;
        classFieldList = null;
        this.ouid = "";
        fieldListForSearchResult = new ArrayList();
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        this.signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        selectCnt = 0;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        parentFr = parent;
        this.ouid = ouid;
        this.signStr = signStr;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            classInfo = dos.getClass(this.ouid);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        makeSearchConditionPanel();
        makeSearchResultTable(this.ouid);
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
        System.out.println("SmallSearchDialog");
    }

    private void initComponents()
    {
        leftPanel.setLayout(null);
        leftPanel.setBackground(UIManagement.panelBackGround);
        leftPanel.setMinimumSize(new Dimension(260, 300));
        leftPanel.setMaximumSize(new Dimension(260, 300));
        leftPanel.setPreferredSize(new Dimension(260, 300));
        conditionScrPane.setPreferredSize(new Dimension(260, 300));
        conditionScrPane.setViewportView(leftPanel);
        searchButton.setActionCommand("search");
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.addActionListener(this);
        searchButton.setDoubleBuffered(true);
        detailButton.setActionCommand("detail");
        detailButton.setIcon(new ImageIcon(getClass().getResource("/icons/detail.gif")));
        detailButton.setToolTipText(DynaMOAD.getMSRString("WRD_0017", "Detail", 3));
        detailButton.setMargin(new Insets(0, 0, 0, 0));
        detailButton.addActionListener(this);
        detailButton.setDoubleBuffered(true);
        dateButton.setActionCommand("date");
        dateButton.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateButton.setToolTipText(DynaMOAD.getMSRString("WRD_0153", "Date select", 3));
        dateButton.setMargin(new Insets(0, 0, 0, 0));
        dateButton.addActionListener(this);
        dateButton.setDoubleBuffered(true);
        comparatorComboBox.setDoubleBuffered(true);
        comparatorComboBox.setBackground(UIManagement.panelBackGround);
        comparatorComboBox.setBorder(UIManagement.borderTextBoxEditable);
        comparatorComboBox.setPreferredSize(new Dimension(100, 10));
        comparatorComboBox.setActionCommand("version type combo");
        comparatorComboBox.addActionListener(this);
        fieldComboBox.setDoubleBuffered(true);
        fieldComboBox.setBackground(UIManagement.panelBackGround);
        fieldComboBox.setBorder(UIManagement.borderTextBoxEditable);
        fieldComboBox.setPreferredSize(new Dimension(80, 10));
        fieldComboBox.setActionCommand("fieldCombo");
        fieldComboBox.addActionListener(this);
        statusComboBox.setDoubleBuffered(true);
        statusComboBox.setBackground(UIManagement.panelBackGround);
        statusComboBox.setBorder(UIManagement.borderTextBoxEditable);
        statusComboBox.setPreferredSize(new Dimension(100, 10));
        statusComboBox.setActionCommand("statusComboBox");
        statusComboBox.addActionListener(this);
        valueTextField.setDoubleBuffered(true);
        valueTextField.setPreferredSize(new Dimension(100, 10));
        optionPanel.setLayout(new BoxLayout(optionPanel, 0));
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.setDoubleBuffered(true);
        optionPanel.add(detailButton);
        optionPanel.add(Box.createHorizontalStrut(5));
        if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
        {
            optionPanel.add(comparatorComboBox);
            optionPanel.add(Box.createHorizontalStrut(5));
        }
        optionPanel.add(fieldComboBox);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(valueTextField);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(searchButton);
        scrollPane.setViewportView(resultTable.getTable());
        scrollPane.setDoubleBuffered(true);
        scrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setDoubleBuffered(true);
        centerPanel.add(optionPanel, "North");
        centerPanel.add(scrollPane, "Center");
        selectButton.setActionCommand("select");
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.setBackground(UIManagement.panelBackGround);
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(10));
        toolPanel.add(closeButton);
        mainPanel.setLayout(borderLO);
        mainPanel.setDoubleBuffered(true);
        mainPanel.setPreferredSize(new Dimension(400, 300));
        mainPanel.add(centerPanel, "Center");
        mainPanel.add(toolPanel, "South");
        mainSplitPane.setDividerLocation(0);
        mainSplitPane.setLeftComponent(conditionScrPane);
        mainSplitPane.setRightComponent(mainPanel);
        setTitle(DynaMOAD.getMSRString("WRD_0084", "Item ", 3) + " " + DynaMOAD.getMSRString("WRD_0018", "Selection", 3));
        addWindowListener(this);
        getContentPane().add(mainSplitPane);
        FromLabel.setBounds(20, 20, 35, 21);
        FromField.setBounds(new Rectangle(55, 20, 112, 21));
        date_chooser.setBounds(170, 20, 20, 20);
        date_chooser.setToolTipText("Date");
        date_chooser.setActionCommand("date1");
        date_chooser.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
        date_chooser.setMargin(new Insets(0, 0, 0, 0));
        date_chooser.addActionListener(this);
        date_chooser.setDoubleBuffered(true);
        ToLabel.setBounds(20, 45, 35, 21);
        ToField.setBounds(new Rectangle(55, 45, 112, 21));
        date_chooser2.setBounds(170, 45, 20, 20);
        date_chooser2.setToolTipText("Date");
        date_chooser2.setActionCommand("date2");
        date_chooser2.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        date_chooser2.setMargin(new Insets(0, 0, 0, 0));
        date_chooser2.addActionListener(this);
        date_chooser2.setDoubleBuffered(true);
        ok.setBounds(55, 85, 60, 25);
        ok.setToolTipText(DynaMOAD.getMSRString("WRD_0019", "OK", 1));
        ok.setText(DynaMOAD.getMSRString("WRD_0019", "OK", 1));
        ok.setActionCommand("ok");
        ok.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        ok.setMargin(new Insets(0, 0, 0, 0));
        ok.addActionListener(this);
        ok.setDoubleBuffered(true);
        clear.setBounds(123, 85, 70, 25);
        clear.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clear.setText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clear.setActionCommand("clear");
        clear.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clear.setMargin(new Insets(0, 0, 0, 0));
        clear.addActionListener(this);
        clear.setDoubleBuffered(true);
    }

    private void initComparator()
    {
        String division01 = "Last Released";
        String division02 = "Work In Progress";
        String division05 = "All Version";
        String division03 = "Created Date";
        String division04 = "Modified Date";
        String versionType[] = {
            division01, division02, division05, division03, division04
        };
        comparatorComboBox = new JComboBox(versionType);
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
        statusComboBox = new JComboBox(statusArray);
    }

    private void initBooleanComboBox()
    {
        String trueStr = "true";
        String falseStr = "false";
        String trueOrFalse[] = {
            "", trueStr, falseStr
        };
        booleanComboBox = new JComboBox(trueOrFalse);
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

    public void makeSearchResultTable(String classOuid)
    {
        try
        {
            int size = 0;
            String fieldOuid = null;
            String fieldCode = null;
            String fieldTitle = null;
            DOSChangeable fieldData = null;
            ArrayList fieldOuidList = null;
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
            if(fieldColumn != null)
            {
                fieldOuidList = new ArrayList();
                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                size = fieldColumn.size();
                selectedRows = new int[size];
                for(int i = 0; i < size; i++)
                {
                    fieldOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)fieldColumn.get(i));
                    fieldData = dos.getField(fieldOuid);
                    fieldCode = (String)fieldData.get("code");
                    fieldTitle = DynaMOAD.getMSRString(fieldCode, (String)fieldData.get("title"), 0);
                    fieldOuidList.add(fieldOuid);
                    columnName.add(fieldTitle);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            } else
            {
                ArrayList tableFieldList = makeDefaultFieldTable(classOuid);
                if(tableFieldList == null)
                    return;
                fieldOuidList = new ArrayList();
                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                size = tableFieldList.size();
                selectedRows = new int[size];
                for(int i = 0; i < size; i++)
                {
                    fieldData = (DOSChangeable)tableFieldList.get(i);
                    fieldOuid = (String)fieldData.get("ouid");
                    fieldCode = (String)fieldData.get("code");
                    fieldTitle = DynaMOAD.getMSRString(fieldCode, (String)fieldData.get("title"), 0);
                    fieldOuidList.add(fieldOuid);
                    columnName.add(fieldTitle);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            }
            resultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
            resultTable.setColumnSequence(selectedRows);
            resultTable.setIndexColumn(0);
            resultTable.getTable().setCursor(handCursor);
            resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            resultTable.getTable().addMouseListener(this);
            scrollPane.setViewportView(resultTable.getTable());
            fieldListForSearchResult = fieldOuidList;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public ArrayList makeDefaultFieldTable(String classOuid)
    {
        ArrayList tableFieldList = new ArrayList();
        ArrayList tmpList = (ArrayList)classFieldList.clone();
        DOSChangeable tempDos = null;
        for(int i = 0; i < tmpList.size(); i++)
        {
            tempDos = (DOSChangeable)tmpList.get(i);
            if(Utils.getBoolean((Boolean)tempDos.get("is.visible")))
                tableFieldList.add(tempDos);
        }

        return tableFieldList;
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
            classFieldList = UIBuilder.getDefaultFieldList(dos, ouid);
            if(classFieldList == null)
                return;
            Utils.sort(classFieldList);
            fieldListForSearchCondition = new ArrayList();
            ArrayList searchConditionNds = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + ouid);
            if(searchConditionNds == null)
            {
                fieldListForSearchCondition = (ArrayList)classFieldList.clone();
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    DOSChangeable tmpDOS = new DOSChangeable();
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                    tmpDOS.put("name", "Version Type");
                    tmpDOS.put("ouid", "version.condition.type");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("type", new Byte((byte)13));
                    tmpDOS.put("is.visible", Boolean.TRUE);
                    fieldListForSearchCondition.add(2, tmpDOS);
                    tmpDOS = null;
                }
            } else
            if(searchConditionNds != null)
            {
                String fieldOuid = null;
                DOSChangeable fieldData = null;
                for(int k = 0; k < searchConditionNds.size(); k++)
                {
                    fieldOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + ouid + "/" + (String)searchConditionNds.get(k));
                    if(fieldOuid.equals("version.condition.type"))
                    {
                        fieldData = new DOSChangeable();
                        fieldData.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                        fieldData.put("name", "Version Type");
                        fieldData.put("ouid", fieldOuid);
                        fieldData.put("index", new Integer(-985));
                        fieldData.put("type", new Byte((byte)13));
                        fieldData.put("is.visible", Boolean.TRUE);
                    } else
                    {
                        fieldData = dos.getField(fieldOuid);
                    }
                    fieldListForSearchCondition.add(fieldData);
                }

            }
            int y_pos = 0;
            if(fieldListForSearchCondition != null)
            {
                initStatus();
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
                HashMap setDatas = (HashMap)searchConditionData.get(ouid);
                boolean tmpBoolean = false;
                boolean versionBoolean = false;
                Boolean isVisible = null;
                int i = -1;
                fieldComboBox.removeAllItems();
                orderOfCombo.clear();
                for(int j = 0; j < fieldListForSearchCondition.size(); j++)
                {
                    DOSChangeable fieldData = (DOSChangeable)fieldListForSearchCondition.get(j);
                    if(fieldData != null)
                    {
                        isVisible = (Boolean)fieldData.get("is.visible");
                        if(Boolean.FALSE.equals(isVisible))
                        {
                            fieldData = null;
                        } else
                        {
                            if(!((String)fieldData.get("ouid")).equals("version.condition.type"))
                            {
                                fieldComboBox.addItem(DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0));
                                orderOfCombo.add(fieldData.get("ouid"));
                            }
                            i++;
                            JLabel advancedTestLabel = new JLabel();
                            JTextField advancedTestTextField = new JTextField();
                            JButton dateSelectButton = new JButton();
                            JButton selectButtonByType = new JButton();
                            advancedTestLabel = new JLabel(DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0));
                            if(versionBoolean)
                                advancedTestLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
                            else
                                advancedTestLabel.setBounds(10, 10 + 23 * i, 100, 20);
                            leftPanel.add(advancedTestLabel);
                            advancedTestLabel.setPreferredSize(new Dimension(100, 20));
                            advancedTestLabel.setMinimumSize(new Dimension(100, 20));
                            advancedTestLabel.setMaximumSize(new Dimension(100, 20));
                            if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type") && ((Byte)fieldData.get("type")).byteValue() != 1)
                            {
                                if(!((String)fieldData.get("ouid")).equals("md$cdate") && !((String)fieldData.get("ouid")).equals("md$mdate") && ((Byte)fieldData.get("type")).byteValue() != 22 && ((Byte)fieldData.get("type")).byteValue() != 21)
                                {
                                    if(((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                                    {
                                        if(versionBoolean)
                                            advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 123, 20);
                                        else
                                            advancedTestTextField.setBounds(110, 10 + 23 * i, 123, 20);
                                    } else
                                    if(versionBoolean)
                                        advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 140, 20);
                                    else
                                        advancedTestTextField.setBounds(110, 10 + 23 * i, 140, 20);
                                    leftPanel.add(advancedTestTextField);
                                    hoardData.put((String)fieldData.get("name"), advancedTestTextField);
                                    hoardData.put((String)fieldData.get("name") + "^ouid", (String)fieldData.get("ouid"));
                                    if(((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                                    {
                                        selectCnt++;
                                        if(versionBoolean)
                                            selectButtonByType.setBounds(232, 10 + 23 * (i + 2), 18, 20);
                                        else
                                            selectButtonByType.setBounds(232, 10 + 23 * i, 18, 20);
                                        selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
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
                                        leftPanel.add(selectButtonByType);
                                    }
                                    if(setDatas != null)
                                        if(((Byte)fieldData.get("type")).byteValue() == 16)
                                        {
                                            String data = (String)setDatas.get((String)fieldData.get("ouid"));
                                            if(!Utils.isNullString(data))
                                            {
                                                DOSChangeable instanceData = dos.get(data);
                                                if(instanceData != null)
                                                    advancedTestTextField.setText((String)instanceData.get("md$number"));
                                                else
                                                    advancedTestTextField.setText("");
                                            } else
                                            {
                                                advancedTestTextField.setText("");
                                            }
                                        } else
                                        if(((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                                        {
                                            String data = (String)setDatas.get((String)fieldData.get("ouid"));
                                            if(!Utils.isNullString(data))
                                            {
                                                DOSChangeable codeItemData = dos.getCodeItem(data);
                                                if(codeItemData != null)
                                                    advancedTestTextField.setText((String)codeItemData.get("name") + " [" + (String)codeItemData.get("codeitemid") + "]");
                                                else
                                                    advancedTestTextField.setText("");
                                            } else
                                            {
                                                advancedTestTextField.setText("");
                                            }
                                        } else
                                        {
                                            advancedTestTextField.setText((String)setDatas.get((String)fieldData.get("ouid")));
                                        }
                                } else
                                {
                                    if(versionBoolean)
                                        advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 117, 20);
                                    else
                                        advancedTestTextField.setBounds(110, 10 + 23 * i, 117, 20);
                                    leftPanel.add(advancedTestTextField);
                                    hoardData.put((String)fieldData.get("name"), advancedTestTextField);
                                    if(versionBoolean)
                                        dateSelectButton.setBounds(230, 10 + 23 * (i + 2), 20, 20);
                                    else
                                        dateSelectButton.setBounds(230, 10 + 23 * i, 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand((String)fieldData.get("name"));
                                    dateSelectButton.addActionListener(this);
                                    dateChooserButtonList.add((String)fieldData.get("name"));
                                    leftPanel.add(dateSelectButton);
                                    if(setDatas != null)
                                        advancedTestTextField.setText((String)setDatas.get((String)fieldData.get("ouid")));
                                }
                            } else
                            {
                                JComboBox advancedComboBox;
                                if("md$status".equals(fieldData.get("code")))
                                {
                                    advancedComboBox = new JComboBox(statusArray);
                                    if(setDatas != null)
                                    {
                                        String values = DynaMOAD.getMSRString((String)setDatas.get((String)fieldData.get("ouid")), (String)setDatas.get((String)fieldData.get("ouid")), 0);
                                        advancedComboBox.setSelectedItem(values);
                                    }
                                } else
                                if(((String)fieldData.get("ouid")).equals("version.condition.type"))
                                {
                                    advancedComboBox = new JComboBox(versionType);
                                    advancedComboBox.setSelectedItem("Work In Progress");
                                    advancedComboBox.setActionCommand((String)fieldData.get("name"));
                                    advancedComboBox.addActionListener(this);
                                    tmpBoolean = true;
                                    advancedTestLabel = new JLabel("From");
                                    advancedTestLabel.setBounds(10, 10 + 23 * (i + 1), 100, 20);
                                    leftPanel.add(advancedTestLabel);
                                    advancedTestTextField.setBounds(110, 10 + 23 * (i + 1), 117, 20);
                                    advancedTestTextField.setEnabled(false);
                                    advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                    leftPanel.add(advancedTestTextField);
                                    hoardData.put("Version Date From", advancedTestTextField);
                                    dateSelectButton.setEnabled(false);
                                    dateSelectButton.setBounds(230, 10 + 23 * (i + 1), 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("Version Date From");
                                    dateSelectButton.addActionListener(this);
                                    hoardData.put("VersionDateFromButton", dateSelectButton);
                                    dateChooserButtonList.add("Version Date From");
                                    leftPanel.add(dateSelectButton);
                                    if(setDatas != null)
                                        advancedTestTextField.setText((String)setDatas.get("version.condition.date.from"));
                                    advancedTestLabel = new JLabel("To");
                                    advancedTestLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
                                    leftPanel.add(advancedTestLabel);
                                    advancedTestTextField = new JTextField();
                                    advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                    advancedTestTextField.setEnabled(false);
                                    advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 117, 20);
                                    leftPanel.add(advancedTestTextField);
                                    hoardData.put("Version Date To", advancedTestTextField);
                                    dateSelectButton = new JButton();
                                    dateSelectButton.setEnabled(false);
                                    dateSelectButton.setBounds(230, 10 + 23 * (i + 2), 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("Version Date To");
                                    dateSelectButton.addActionListener(this);
                                    hoardData.put("VersionDateToButton", dateSelectButton);
                                    dateChooserButtonList.add("Version Date To");
                                    leftPanel.add(dateSelectButton);
                                    if(setDatas != null)
                                        advancedTestTextField.setText((String)setDatas.get("version.condition.date.to"));
                                    if(setDatas != null)
                                    {
                                        String selectedStr = (String)setDatas.get((String)fieldData.get("ouid"));
                                        if(selectedStr != null)
                                        {
                                            String values = "";
                                            if(selectedStr.equals("released"))
                                                values = "Last Released";
                                            else
                                            if(selectedStr.equals("wip"))
                                                values = "Work In Progress";
                                            else
                                            if(selectedStr.equals("all"))
                                                values = "All Version";
                                            else
                                            if(selectedStr.equals("cdate"))
                                                values = "Created Date";
                                            else
                                                values = "Modified Date";
                                            advancedComboBox.setSelectedItem(values);
                                        }
                                    }
                                } else
                                if(((Byte)fieldData.get("type")).byteValue() == 1)
                                {
                                    advancedComboBox = new JComboBox(trueOrFalse);
                                    if(setDatas != null)
                                    {
                                        Boolean selectedStr = (Boolean)setDatas.get((String)fieldData.get("ouid"));
                                        if(selectedStr != null)
                                        {
                                            String values = "";
                                            if(selectedStr.booleanValue())
                                                values = "true";
                                            else
                                                values = "false";
                                            advancedComboBox.setSelectedItem(values);
                                        }
                                    }
                                } else
                                {
                                    advancedComboBox = new JComboBox();
                                }
                                if(versionBoolean)
                                    advancedComboBox.setBounds(110, 10 + 23 * (i + 2), 140, 20);
                                else
                                    advancedComboBox.setBounds(110, 10 + 23 * i, 140, 20);
                                leftPanel.add(advancedComboBox);
                                hoardData.put((String)fieldData.get("name"), advancedComboBox);
                                if(tmpBoolean)
                                    versionBoolean = true;
                            }
                            if(versionBoolean)
                                y_pos = 10 + 23 * (i + 4);
                            else
                                y_pos = 10 + 23 * (i + 2);
                        }
                    }
                }

            }
            leftPanel.setMinimumSize(new Dimension(260, y_pos));
            leftPanel.setMaximumSize(new Dimension(260, y_pos));
            leftPanel.setPreferredSize(new Dimension(260, y_pos));
            conditionScrPane.setPreferredSize(new Dimension(260, 300));
            conditionScrPane.setViewportView(leftPanel);
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
        searchButton.removeActionListener(this);
        detailButton.removeActionListener(this);
        selectButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        setVisible(false);
        dispose();
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        ((JTextField)hoardData.get(sign)).setText(selectOuid);
    }

    public void setConditionField(String str)
    {
        valueTextField.setText(str);
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
        if(command.equals("search"))
            try
            {
                if(DETAIL == 1)
                {
                    HashMap searchCondition = (HashMap)searchConditionData.get(ouid);
                    if(searchCondition == null)
                        searchCondition = new HashMap();
                    for(int i = 0; i < fieldListForSearchCondition.size(); i++)
                    {
                        DOSChangeable fieldData = (DOSChangeable)fieldListForSearchCondition.get(i);
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
                                    if(((String)fieldData.get("ouid")).equals("md$number") && !text.equals(""))
                                        searchCondition.put((String)fieldData.get("ouid"), text);
                                    else
                                    if(((String)fieldData.get("ouid")).equals("md$cdate") && !text.equals(""))
                                        searchCondition.put((String)fieldData.get("ouid"), text);
                                    else
                                    if(((String)fieldData.get("ouid")).equals("md$mdate") && !text.equals(""))
                                        searchCondition.put((String)fieldData.get("ouid"), text);
                                    else
                                    if(((String)fieldData.get("ouid")).equals("md$user") && !text.equals(""))
                                        searchCondition.put((String)fieldData.get("ouid"), text);
                                    else
                                    if((((Byte)fieldData.get("type")).byteValue() != 16 && ((Byte)fieldData.get("type")).byteValue() != 24 && ((Byte)fieldData.get("type")).byteValue() != 25 || text.equals("")) && !text.equals(""))
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
                                searchCondition.put((String)fieldData.get("ouid"), statusValueMap.get(text));
                            else
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

                    searchConditionData.put(ouid, searchCondition);
                    if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                        searchCondition.put("version.condition.type", "wip");
                    searchCondition.put("search.result.count", "-1");
                    ArrayList testList = dos.list(ouid, fieldListForSearchResult, searchCondition);
                    setResultData(testList);
                } else
                if(DETAIL == 0)
                {
                    HashMap searchCondition = new HashMap();
                    int row = fieldComboBox.getSelectedIndex();
                    String comboOuid = (String)orderOfCombo.get(row);
                    DOSChangeable fieldInfo = dos.getField(comboOuid);
                    if("md$status".equals(fieldInfo.get("code")))
                    {
                        if(!Utils.isNullString(statusComboBox.getSelectedItem().toString()))
                        {
                            String text = statusComboBox.getSelectedItem().toString();
                            searchCondition.put(comboOuid, statusValueMap.get(text));
                        }
                    } else
                    if(fieldInfo != null && fieldInfo.get("type").toString().equals((new Byte((byte)1)).toString()))
                        searchCondition.put(comboOuid, new Boolean(booleanComboBox.getSelectedItem().toString()));
                    else
                    if(!Utils.isNullString(valueTextField.getText()))
                        searchCondition.put(comboOuid, valueTextField.getText());
                    String selectedStr = (String)comparatorComboBox.getSelectedItem();
                    String values = "";
                    if(selectedStr.equals("Last Released"))
                        searchCondition.put("version.condition.type", "released");
                    else
                    if(selectedStr.equals("Work In Progress"))
                        searchCondition.put("version.condition.type", "wip");
                    else
                    if(selectedStr.equals("All Version"))
                        searchCondition.put("version.condition.type", "all");
                    else
                    if(selectedStr.equals("Created Date"))
                    {
                        searchCondition.put("version.condition.type", "cdate");
                        searchCondition.put("version.condition.date.from", FromField.getText());
                        searchCondition.put("version.condition.date.to", ToField.getText());
                    } else
                    if(selectedStr.equals("Modified Date"))
                    {
                        searchCondition.put("version.condition.type", "mdate");
                        searchCondition.put("version.condition.date.from", FromField.getText());
                        searchCondition.put("version.condition.date.to", ToField.getText());
                    }
                    if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                        searchCondition.put("version.condition.type", "wip");
                    if(!Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && !searchCondition.isEmpty() && searchCondition.containsKey("version.condition.type"))
                        searchCondition.remove("version.condition.type");
                    searchCondition.put("search.result.count", "-1");
                    ArrayList testList = dos.list(ouid, fieldListForSearchResult, searchCondition);
                    searchCondition.clear();
                    setResultData(testList);
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("detail"))
        {
            if(DETAIL == 0)
            {
                DETAIL = 1;
                mainSplitPane.setDividerLocation(270);
                mainSplitPane.setLeftComponent(conditionScrPane);
                mainSplitPane.updateUI();
            } else
            {
                DETAIL = 0;
                mainSplitPane.setDividerLocation(0);
                mainSplitPane.remove(conditionScrPane);
                mainSplitPane.updateUI();
            }
        } else
        if(command.equals("select"))
        {
            int row = resultTable.getTable().getSelectedRow();
            String ouidRow = resultTable.getSelectedOuidRow(row);
            String selectOuid = "";
            String selectNumber = "";
            if(row > -1)
            {
                if(ouidRow != null)
                {
                    selectOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
                    selectNumber = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(1);
                } else
                {
                    selectOuid = (String)((ArrayList)searchData.get(row)).get(0);
                    selectNumber = (String)((ArrayList)searchData.get(row)).get(1);
                }
                if(selectOuid != null)
                {
                    if(parentFr instanceof AssoClassUI)
                        ((AssoClassUI)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    else
                    if(parentFr instanceof AuthoritySearch)
                        ((AuthoritySearch)parentFr).setObject(selectOuid, selectNumber);
                    else
                    if(parentFr instanceof SearchCondition)
                        ((SearchCondition)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    else
                    if(parentFr instanceof SearchCondition4CADIntegration)
                        ((SearchCondition4CADIntegration)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    else
                    if(parentFr instanceof FilterDialogForList)
                        ((FilterDialogForList)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    else
                    if(parentFr instanceof FilterDialogForLink)
                        ((FilterDialogForLink)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    else
                    if(parentFr instanceof AdvancedFilterDialogForList)
                        ((AdvancedFilterDialogForList)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    else
                    if(parentFr instanceof SmallSearchDialog)
                        ((SmallSearchDialog)parentFr).setFieldInObject(signStr, selectOuid, selectNumber);
                    closeDialog(null);
                }
            } else
            {
                if(parentFr instanceof AssoClassUI)
                    ((AssoClassUI)parentFr).setFieldInObject(signStr, null, "");
                else
                if(parentFr instanceof AuthoritySearch)
                    ((AuthoritySearch)parentFr).setObject(null, "");
                else
                if(parentFr instanceof SearchCondition)
                    ((SearchCondition)parentFr).setFieldInObject(signStr, null, "");
                else
                if(parentFr instanceof SearchCondition4CADIntegration)
                    ((SearchCondition4CADIntegration)parentFr).setFieldInObject(signStr, null, "");
                else
                if(parentFr instanceof FilterDialogForList)
                    ((FilterDialogForList)parentFr).setFieldInObject(signStr, null, "");
                else
                if(parentFr instanceof FilterDialogForLink)
                    ((FilterDialogForLink)parentFr).setFieldInObject(signStr, null, "");
                else
                if(parentFr instanceof AdvancedFilterDialogForList)
                    ((AdvancedFilterDialogForList)parentFr).setFieldInObject(signStr, null, "");
                else
                if(parentFr instanceof SmallSearchDialog)
                    ((SmallSearchDialog)parentFr).setFieldInObject(signStr, null, "");
                closeDialog(null);
            }
        } else
        if(command.equals("fieldCombo"))
            try
            {
                int row = fieldComboBox.getSelectedIndex();
                if(row > -1)
                {
                    DOSChangeable fieldInfo = new DOSChangeable();
                    if(orderOfCombo.size() > 0)
                    {
                        String comboOuid = (String)orderOfCombo.get(row);
                        fieldInfo = dos.getField(comboOuid);
                        optionPanel.removeAll();
                        optionPanel.add(detailButton);
                        optionPanel.add(Box.createHorizontalStrut(5));
                        if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                        {
                            optionPanel.add(comparatorComboBox);
                            optionPanel.add(Box.createHorizontalStrut(5));
                        }
                        optionPanel.add(fieldComboBox);
                        optionPanel.add(Box.createHorizontalStrut(5));
                        if(comboOuid.equals("md$cdate") || comboOuid.equals("md$mdate") || fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 22)
                        {
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(dateButton);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            optionPanel.updateUI();
                        } else
                        if("md$status".equals(fieldInfo.get("code")))
                        {
                            optionPanel.add(statusComboBox);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            valueTextField.setEditable(false);
                            optionPanel.updateUI();
                        } else
                        if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 1)
                        {
                            optionPanel.add(booleanComboBox);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            valueTextField.setEditable(false);
                            optionPanel.updateUI();
                        } else
                        {
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            valueTextField.setEditable(true);
                            optionPanel.updateUI();
                        }
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(!command.equals("statusComboBox"))
            if(command.equals("date"))
            {
                DynaDateChooser newDateChooser = new DynaDateChooser(this);
                java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                if(date != null)
                    setConditionField(sdfYMD.format(date));
                else
                    setConditionField("");
                newDateChooser = null;
            } else
            if(command.equals("Version Type"))
            {
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
            if(command.equals("version type combo"))
            {
                String selectedItem = (String)comparatorComboBox.getSelectedItem();
                if(selectedItem.equals("Created Date") || selectedItem.equals("Modified Date"))
                {
                    jd.getContentPane().setLayout(null);
                    jd.setBackground(Color.blue);
                    UIUtils.setLocationRelativeTo(jd, this);
                    jd.setResizable(false);
                    jd.setSize(220, 145);
                    jd.getContentPane().add(FromLabel);
                    jd.getContentPane().add(FromField);
                    jd.getContentPane().add(date_chooser);
                    jd.getContentPane().add(ToLabel);
                    jd.getContentPane().add(ToField);
                    jd.getContentPane().add(date_chooser2);
                    jd.getContentPane().add(ok);
                    jd.getContentPane().add(clear);
                    jd.show();
                } else
                {
                    jd.dispose();
                }
            } else
            if(command.equals("date"))
            {
                DynaDateChooser newDateChooser = new DynaDateChooser(this);
                java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                if(date != null)
                    setConditionField(sdfYMD.format(date));
                else
                    setConditionField("");
                newDateChooser = null;
            } else
            if(command.equals("date1"))
            {
                DynaDateChooser newDateChooser = new DynaDateChooser(this);
                java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                if(date != null)
                    FromField.setText(sdfYMD.format(date));
                else
                    FromField.setText("");
                newDateChooser = null;
            } else
            if(command.equals("date2"))
            {
                DynaDateChooser newDateChooser = new DynaDateChooser(this);
                java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                if(date != null)
                    ToField.setText(sdfYMD.format(date));
                else
                    ToField.setText("");
                newDateChooser = null;
            } else
            if(command.equals("ok"))
            {
                try
                {
                    HashMap searchCondition = (HashMap)searchConditionData.get(ouid);
                    if(searchCondition == null)
                        searchCondition = new HashMap();
                    String selectedStr = (String)comparatorComboBox.getSelectedItem();
                    if(selectedStr.equals("Created Date"))
                    {
                        searchCondition.put("version.condition.type", "cdate");
                        searchCondition.put("version.condition.date.from", FromField.getText());
                        searchCondition.put("version.condition.date.to", ToField.getText());
                    } else
                    if(selectedStr.equals("Modified Date"))
                    {
                        searchCondition.put("version.condition.type", "mdate");
                        searchCondition.put("version.condition.date.from", FromField.getText());
                        searchCondition.put("version.condition.date.to", ToField.getText());
                    }
                    searchConditionData.put(ouid, searchCondition);
                    if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                        searchCondition.put("version.condition.type", "wip");
                    searchCondition.put("search.result.count", "-1");
                    ArrayList testList = dos.list(ouid, fieldListForSearchResult, searchCondition);
                    setResultData(testList);
                }
                catch(IIPRequestException e)
                {
                    System.err.println(e);
                }
                jd.dispose();
            } else
            if(command.equals("clear"))
            {
                ToField.setText("");
                FromField.setText("");
            } else
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
                        if(Utils.isNullString(codeOuid))
                            return;
                        try
                        {
                            DOSChangeable instanceData = new DOSChangeable();
                            if(searchConditionData != null)
                                instanceData.setValueMap((HashMap)searchConditionData.get(ouid));
                            codeOuid = DynaMOAD.dos.lookupCodeFromSelectionTable(codeOuid, instanceData);
                            if(Utils.isNullString(codeOuid))
                                return;
                            CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)evt.getSource(), true, codeOuid, command);
                            newSmall.setParentFrame(this);
                            newSmall.setVisible(true);
                        }
                        catch(Exception ee)
                        {
                            ee.printStackTrace();
                        }
                    }

            } else
            {
                int tmpInt = 0;
                for(int i = 0; i < dateChooserButtonList.size(); i++)
                    if(command.equals(dateChooserButtonList.get(i)))
                        tmpInt = i;

                String classOuid = (String)dateChooserButtonList.get(tmpInt);
                if(classOuid != null)
                {
                    DynaDateChooser newDateChooser = new DynaDateChooser(this);
                    java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                    if(date != null)
                        setFieldInDateField(sdfYMD.format(date), (String)dateChooserButtonList.get(tmpInt));
                    else
                        setFieldInDateField("", (String)dateChooserButtonList.get(tmpInt));
                    newDateChooser = null;
                } else
                {
                    System.out.println("type class ouid is Null");
                }
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

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getSource() == resultTable.getTable() && mouseEvent.getClickCount() == 2)
            selectButton.doClick();
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void setFieldInObject(String sign, String selectOuid, String number)
    {
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '/');
        String mapId = (String)list.get(2);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(number);
        HashMap tmpMap = (HashMap)searchConditionData.get(ouid);
        if(tmpMap == null)
            tmpMap = new HashMap();
        tmpMap.put((String)hoardData.get(mapId + "^ouid"), selectOuid);
        searchConditionData.put(ouid, tmpMap);
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
        HashMap tmpMap = (HashMap)searchConditionData.get(ouid);
        if(tmpMap == null)
            tmpMap = new HashMap();
        tmpMap.put((String)hoardData.get(mapId + "^ouid"), selectOuid);
        searchConditionData.put(ouid, tmpMap);
        field = null;
        list.clear();
        list = null;
    }

    private Cursor handCursor;
    private JSplitPane mainSplitPane;
    private JPanel mainPanel;
    private JPanel toolPanel;
    private JButton searchButton;
    private JButton detailButton;
    private JButton selectButton;
    private JButton closeButton;
    private JButton dateButton;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel optionPanel;
    private JComboBox fieldComboBox;
    private JComboBox comparatorComboBox;
    private JComboBox statusComboBox;
    private JComboBox booleanComboBox;
    private JTextField valueTextField;
    private JScrollPane scrollPane;
    private JScrollPane conditionScrPane;
    private Table resultTable;
    final int title_width = 100;
    final int title_height = 20;
    final int DIV_SIZE = 270;
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
    private ArrayList fieldListForSearchCondition;
    private ArrayList classFieldList;
    private String ouid;
    private ArrayList fieldListForSearchResult;
    private String NDS_CODE;
    final int init_xcord = 10;
    final int init_ycord = 0;
    final int button_xcord = 15;
    final int titleWidth = 100;
    final int totalWidth = 240;
    final int fieldHeight = 20;
    final int offset = 3;
    final int buttonOffset = 10;
    final int condition_xsize = 260;
    final int condition_ysize = 300;
    final int buttonWidth = 76;
    final int buttonHeight = 0;
    private DOSChangeable hoardData;
    private int DETAIL;
    private DOSChangeable searchConditionData;
    private String signStr;
    private Object parentFr;
    private ArrayList orderOfCombo;
    private DOSChangeable classInfo;
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_UTF = 13;
    private final byte DATATYPE_BOOLEAN = 1;
    private final int dateButtonWidth = 20;
    private ArrayList dateChooserButtonList;
    private ArrayList selectButtonList;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private int selectCnt;
    private JDialog jd;
    private JLabel FromLabel;
    private JLabel ToLabel;
    private JTextField FromField;
    private JTextField ToField;
    private JButton date_chooser;
    private JButton date_chooser2;
    private JButton ok;
    private JButton clear;
    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;

}