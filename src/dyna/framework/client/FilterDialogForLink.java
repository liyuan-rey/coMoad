// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FilterDialogForLink.java

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            CodeChooserUser, DynaMOAD, UIManagement, LogIn, 
//            UIGeneration, SmallSearchDialog, CodeSelectDialog

public class FilterDialogForLink extends JDialog
    implements ActionListener, ItemListener, WindowListener, CodeChooserUser
{
    class PopupLink extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger())
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


    public FilterDialogForLink(Frame parent, Component comp, boolean modal, String ouid, String instanceOuid, int mode, ArrayList assoList)
    {
        super(parent, modal);
        mainSplitPane = new JSplitPane();
        mainPanel = new JPanel();
        toolBar = new JToolBar();
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
        this.ouid = "";
        NDS_CODE = null;
        selectCnt = 0;
        hoardData = new DOSChangeable();
        fieldSearchList = new ArrayList();
        this.instanceOuid = null;
        parentFr = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        searchCondition = null;
        searchConditionData = new HashMap();
        fieldInfoList = null;
        assoTableInfo = null;
        end2OuidList = new ArrayList();
        isEnd2Versionable = false;
        existsAssociationClass = false;
        layout = new FormLayout("right:max(40dlu;p),4dlu,85dlu:grow(1),0dlu,12dlu", "");
        builder = new DefaultFormBuilder(layout);
        ownershipLabel = null;
        rdoBtnPanel = null;
        buttonGroup = null;
        publicRdoBtn = null;
        privateRdoBtn = null;
        isviewLabel = null;
        isviewChkBox = null;
        parentFr = parent;
        this.ouid = ouid;
        this.instanceOuid = instanceOuid;
        getEnd2ClassOuid();
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
            if(assoList == null)
                return;
            ArrayList classList = new ArrayList();
            DOSChangeable dosAssociation = null;
            String classOuid = null;
            Iterator iteratorKey;
            for(iteratorKey = assoList.iterator(); iteratorKey.hasNext();)
            {
                dosAssociation = (DOSChangeable)iteratorKey.next();
                if(mode == 0)
                    classOuid = (String)dosAssociation.get("end2.ouid@class");
                else
                    classOuid = (String)dosAssociation.get("end1.ouid@class");
                if(Utils.isNullString(classOuid) || classList.contains(classOuid))
                {
                    dosAssociation = null;
                } else
                {
                    classList.add(classOuid);
                    classOuid = (String)dosAssociation.get("ouid@class");
                    if(Utils.isNullString(classOuid) || classList.contains(classOuid))
                    {
                        dosAssociation = null;
                    } else
                    {
                        classList.add(classOuid);
                        dosAssociation = null;
                    }
                }
            }

            iteratorKey = null;
            if(classList.size() == 0)
            {
                classList = null;
                return;
            }
            ArrayList fieldList = new ArrayList();
            ArrayList tmpFieldList = new ArrayList();
            for(iteratorKey = classList.iterator(); iteratorKey.hasNext();)
            {
                classOuid = (String)iteratorKey.next();
                tmpFieldList = dos.listFieldInClass(classOuid);
                if(tmpFieldList != null && tmpFieldList.size() != 0)
                {
                    fieldList.addAll(tmpFieldList);
                    tmpFieldList = null;
                }
            }

            iteratorKey = null;
            Utils.sort(fieldList);
            assoTableInfo = new ArrayList();
            ArrayList columnOuidList = new ArrayList();
            DOSChangeable dosField = null;
            Boolean isVisible = null;
            iteratorKey = fieldList.iterator();
            while(iteratorKey.hasNext()) 
            {
                dosField = (DOSChangeable)iteratorKey.next();
                isVisible = (Boolean)dosField.get("is.visible");
                if(isVisible == null || Utils.getBoolean(isVisible))
                {
                    if(columnOuidList.contains(dosField.get("ouid")))
                    {
                        dosField = null;
                        continue;
                    }
                    columnOuidList.add((String)dosField.get("ouid"));
                    assoTableInfo.add(dosField);
                }
                dosField = null;
            }
            iteratorKey = null;
            fieldList.clear();
            fieldList = null;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        makeSearchResultTable(ouid);
        initComponents();
        pack();
        setSize(600, 450);
        UIUtils.setLocationRelativeTo(this, comp);
        setLinkFilterToTable();
        createPopup();
        System.out.println("FilterDialogForLink");
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

    private void getEnd2ClassOuid()
    {
        try
        {
            HashMap assoMap = dos.listEffectiveAssociation(ouid, Boolean.TRUE, "composition");
            DOSChangeable dosAssociation = null;
            if(assoMap == null || assoMap.isEmpty())
                return;
            for(Iterator assoKey = assoMap.values().iterator(); assoKey.hasNext(); end2OuidList.add((String)dosAssociation.get("end2.ouid@class")))
                dosAssociation = (DOSChangeable)assoKey.next();

            for(int i = 0; i < end2OuidList.size(); i++)
            {
                DOSChangeable end2classInfo = new DOSChangeable();
                end2classInfo = dos.getClass((String)end2OuidList.get(i));
                if(Utils.getBoolean((Boolean)end2classInfo.get("is.versionable")))
                    isEnd2Versionable = true;
            }

        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void initComponents()
    {
        addWindowListener(this);
        mainSplitPane.setDividerLocation(320);
        mainSplitPane.setOneTouchExpandable(true);
        mainPanel.setLayout(borderLO);
        toolBar.add(Box.createHorizontalGlue());
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.setBackground(UIManagement.panelBackGround);
        clearButton.setActionCommand("clear");
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "clear", 3));
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.addActionListener(this);
        clearButton.setDoubleBuffered(true);
        toolBar.add(clearButton);
        dateButton.setActionCommand("date");
        dateButton.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateButton.setToolTipText("Date select");
        dateButton.setMargin(new Insets(0, 0, 0, 0));
        dateButton.addActionListener(this);
        dateButton.setDoubleBuffered(true);
        selectButton.setActionCommand("select");
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        toolBar.add(selectButton);
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(10));
        saveButton.setActionCommand("save");
        saveButton.setText(DynaMOAD.getMSRString("WRD_0011", "save", 3));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "save", 3));
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.addActionListener(this);
        saveButton.setDoubleBuffered(true);
        toolBar.add(saveButton);
        toolPanel.add(saveButton);
        toolPanel.add(Box.createHorizontalStrut(10));
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolBar.add(closeButton);
        toolBar.setDoubleBuffered(true);
        toolBar.setBackground(UIManagement.panelBackGround);
        toolPanel.add(closeButton);
        mainPanel.add(toolPanel, "South");
        isviewChkBox = new JCheckBox();
        isviewChkBox.setBackground(UIManagement.panelBackGround);
        publicRdoBtn = new JRadioButton(DynaMOAD.getMSRString("WRD_0115", "PUBLIC", 3));
        publicRdoBtn.setBackground(UIManagement.panelBackGround);
        publicRdoBtn.addItemListener(this);
        publicRdoBtn.setSelected(true);
        privateRdoBtn = new JRadioButton(DynaMOAD.getMSRString("WRD_0116", "PRIVATE", 3));
        privateRdoBtn.setBackground(UIManagement.panelBackGround);
        privateRdoBtn.addItemListener(this);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(publicRdoBtn);
        buttonGroup.add(privateRdoBtn);
        ownershipLabel = new JLabel(DynaMOAD.getMSRString("WRD_0117", "Ownership", 3));
        ownershipLabel.setPreferredSize(new Dimension(100, 18));
        ownershipLabel.setMinimumSize(new Dimension(100, 18));
        ownershipLabel.setMaximumSize(new Dimension(100, 18));
        rdoBtnPanel = new JPanel();
        rdoBtnPanel.setBorder(UIManagement.borderPanel);
        rdoBtnPanel.setBackground(UIManagement.panelBackGround);
        rdoBtnPanel.setLayout(new BoxLayout(rdoBtnPanel, 0));
        rdoBtnPanel.add(Box.createHorizontalStrut(5));
        rdoBtnPanel.add(publicRdoBtn);
        rdoBtnPanel.add(Box.createHorizontalGlue());
        rdoBtnPanel.add(privateRdoBtn);
        rdoBtnPanel.add(Box.createHorizontalStrut(5));
        isviewLabel = new JLabel(DynaMOAD.getMSRString("WRD_0132", "is View", 3));
        isviewLabel.setPreferredSize(new Dimension(100, 18));
        isviewLabel.setMinimumSize(new Dimension(100, 18));
        isviewLabel.setMaximumSize(new Dimension(100, 18));
        builder.setDefaultDialogBorder();
        builder.getPanel().setBackground(UIManagement.panelBackGround);
        builder.append(ownershipLabel, rdoBtnPanel);
        builder.nextLine();
        builder.append(isviewLabel, isviewChkBox);
        builder.nextLine();
        builder.appendSeparator(DynaMOAD.getMSRString("WRD_0118", "Search Condition", 3));
        if(!LogIn.isAdmin)
            privateRdoBtn.setSelected(true);
        centerPanel.setLayout(new BorderLayout());
        optionPanel.setLayout(new BoxLayout(optionPanel, 0));
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(filterNameLabel);
        optionPanel.add(Box.createHorizontalStrut(5));
        valueTextField.setDoubleBuffered(true);
        valueTextField.setPreferredSize(new Dimension(100, 10));
        optionPanel.add(valueTextField);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.setDoubleBuffered(true);
        optionPanel.add(clearButton);
        centerPanel.add(optionPanel, "North");
        scrollPane.setViewportView(resultTable.getTable());
        scrollPane.setDoubleBuffered(true);
        scrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        centerPanel.add(scrollPane, "Center");
        centerPanel.setDoubleBuffered(true);
        makeSearchConditionPanel();
        conditionScrPane.getViewport().add(builder.getPanel());
        mainPanel.add(centerPanel, "Center");
        mainPanel.setDoubleBuffered(true);
        mainPanel.setPreferredSize(new Dimension(500, 270));
        mainSplitPane.setLeftComponent(conditionScrPane);
        mainSplitPane.setRightComponent(mainPanel);
        setTitle(DynaMOAD.getMSRString("WRD_0120", "Link Filter", 3));
        getContentPane().add(mainSplitPane);
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
            ArrayList pubFieldColumn = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid);
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid);
            ArrayList valueList = new ArrayList();
            Boolean booleanForTable = null;
            if(pubFieldColumn == null && fieldColumn == null)
                return;
            searchData.clear();
            if(pubFieldColumn != null)
            {
                for(int i = 0; i < pubFieldColumn.size(); i++)
                {
                    ArrayList pubFilterValue = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + (String)pubFieldColumn.get(i));
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
                                String value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + (String)pubFieldColumn.get(i) + "/" + ouidStr);
                                if("md$status".equals(dosInfoList.get("code")))
                                    value = DynaMOAD.getMSRString(value, value, 0);
                                else
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
                    ArrayList filterValue = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + (String)fieldColumn.get(i));
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
                                String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + (String)fieldColumn.get(i) + "/" + ouidStr);
                                if("md$status".equals(dosInfoList.get("code")))
                                    value = DynaMOAD.getMSRString(value, value, 0);
                                else
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
        ArrayList tableFieldList = new ArrayList();
        fieldInfoList = (ArrayList)assoTableInfo.clone();
        DOSChangeable tmpDOS = null;
        for(int i = 0; i < fieldInfoList.size(); i++)
        {
            tmpDOS = (DOSChangeable)fieldInfoList.get(i);
            tableFieldList.add(DynaMOAD.getMSRString((String)tmpDOS.get("code"), (String)tmpDOS.get("title"), 0));
            if(tmpDOS.get("code").equals("md$sequence"))
                existsAssociationClass = true;
            tmpDOS = null;
        }

        if(fieldInfoList != null)
        {
            tmpDOS = new DOSChangeable();
            int i = 0;
            if(isEnd2Versionable)
            {
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                tmpDOS.put("name", "Version Type");
                tmpDOS.put("ouid", "version.condition.type");
                tmpDOS.put("description", "");
                tmpDOS.put("index", new Integer(-985));
                tmpDOS.put("type", new Byte((byte)13));
                tmpDOS.put("is.visible", Boolean.TRUE);
                fieldInfoList.add(i, tmpDOS);
                tableFieldList.add(i, DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                tmpDOS = null;
                tmpDOS = new DOSChangeable();
                i++;
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0041", "Version Date From", 3));
                tmpDOS.put("name", "Version Date From");
                tmpDOS.put("ouid", "version.condition.date.from");
                tmpDOS.put("description", "");
                fieldInfoList.add(i, tmpDOS);
                tableFieldList.add(i, DynaMOAD.getMSRString("WRD_0041", "Version Date From", 3));
                tmpDOS = null;
                tmpDOS = new DOSChangeable();
                i++;
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0042", "Version Date To", 3));
                tmpDOS.put("name", "Version Date To");
                tmpDOS.put("ouid", "version.condition.date.to");
                tmpDOS.put("description", "");
                fieldInfoList.add(i, tmpDOS);
                tableFieldList.add(i, DynaMOAD.getMSRString("WRD_0042", "Version Date To", 3));
                tmpDOS = null;
                tmpDOS = new DOSChangeable();
                i++;
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
        selectButtonList = new ArrayList();
        dateChooserButtonList = new ArrayList();
        fieldSearchList = (ArrayList)assoTableInfo.clone();
        DOSChangeable tmpDOS = null;
        for(int i = 0; i < fieldSearchList.size(); i++)
        {
            tmpDOS = (DOSChangeable)fieldSearchList.get(i);
            tmpDOS.put("title", DynaMOAD.getMSRString((String)tmpDOS.get("code"), (String)tmpDOS.get("title"), 0));
            fieldSearchList.set(i, tmpDOS);
            tmpDOS = null;
        }

        tmpDOS = new DOSChangeable();
        if(isEnd2Versionable)
        {
            tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
            tmpDOS.put("name", "Version Type");
            tmpDOS.put("ouid", "version.condition.type");
            tmpDOS.put("index", new Integer(-985));
            tmpDOS.put("type", new Byte((byte)13));
            tmpDOS.put("is.visible", Boolean.TRUE);
            fieldSearchList.add(2, tmpDOS);
            tmpDOS = null;
        }
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
        boolean tmpBoolean = false;
        boolean versionBoolean = false;
        Boolean isVisible = null;
        int i = -1;
        for(int j = 0; j < fieldSearchList.size(); j++)
        {
            DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(j);
            if(fieldData != null)
            {
                isVisible = (Boolean)fieldData.get("is.visible");
                if(Boolean.FALSE.equals(isVisible))
                {
                    fieldData.clear();
                    fieldData = null;
                } else
                {
                    i++;
                    JLabel advancedTestLabel = new JLabel();
                    JTextField advancedTestTextField = new JTextField();
                    JButton dateSelectButton = new JButton();
                    JButton selectButtonByType = new JButton();
                    advancedTestLabel = new JLabel((String)fieldData.get("title"));
                    if(versionBoolean)
                        advancedTestLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
                    else
                        advancedTestLabel.setBounds(10, 10 + 23 * i, 100, 20);
                    advancedTestLabel.setPreferredSize(new Dimension(100, 18));
                    advancedTestLabel.setMinimumSize(new Dimension(100, 18));
                    advancedTestLabel.setMaximumSize(new Dimension(100, 18));
                    if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type") && ((Byte)fieldData.get("type")).byteValue() != 1)
                    {
                        if(!((String)fieldData.get("ouid")).equals("md$cdate") && !((String)fieldData.get("ouid")).equals("md$mdate") && ((Byte)fieldData.get("type")).byteValue() != 22 && ((Byte)fieldData.get("type")).byteValue() != 21)
                        {
                            if(((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                            {
                                if(versionBoolean)
                                    advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 197, 20);
                                else
                                    advancedTestTextField.setBounds(110, 10 + 23 * i, 197, 20);
                            } else
                            if(versionBoolean)
                                advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 220, 20);
                            else
                                advancedTestTextField.setBounds(110, 10 + 23 * i, 220, 20);
                            hoardData.put((String)fieldData.get("name"), advancedTestTextField);
                            hoardData.put((String)fieldData.get("name") + "^ouid", (String)fieldData.get("ouid"));
                            if(((Byte)fieldData.get("type")).byteValue() == 16 || ((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                            {
                                selectCnt++;
                                if(versionBoolean)
                                    selectButtonByType.setBounds(310, 10 + 23 * (i + 2), 20, 20);
                                else
                                    selectButtonByType.setBounds(310, 10 + 23 * i, 20, 20);
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
                                builder.append(advancedTestLabel, advancedTestTextField);
                                builder.nextLine();
                            }
                        } else
                        {
                            if(versionBoolean)
                                advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 197, 20);
                            else
                                advancedTestTextField.setBounds(110, 10 + 23 * i, 197, 20);
                            hoardData.put((String)fieldData.get("name"), advancedTestTextField);
                            if(versionBoolean)
                                dateSelectButton.setBounds(310, 10 + 23 * (i + 2), 20, 20);
                            else
                                dateSelectButton.setBounds(310, 10 + 23 * i, 20, 20);
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
                            builder.append(advancedTestLabel, advancedComboBox);
                            builder.nextLine();
                        } else
                        if(((String)fieldData.get("ouid")).equals("version.condition.type"))
                        {
                            advancedComboBox = new JComboBox(versionType);
                            advancedComboBox.setSelectedItem("Work In Progress");
                            advancedComboBox.setActionCommand((String)fieldData.get("name"));
                            advancedComboBox.addActionListener(this);
                            tmpBoolean = true;
                            if(versionBoolean)
                                advancedComboBox.setBounds(110, 10 + 23 * (i + 2), 220, 20);
                            else
                                advancedComboBox.setBounds(110, 10 + 23 * i, 220, 20);
                            builder.append(advancedTestLabel, advancedComboBox);
                            builder.nextLine();
                            advancedTestLabel = new JLabel("From");
                            advancedTestLabel.setBounds(10, 10 + 23 * (i + 1), 100, 20);
                            advancedTestLabel.setPreferredSize(new Dimension(100, 18));
                            advancedTestLabel.setMinimumSize(new Dimension(100, 18));
                            advancedTestLabel.setMaximumSize(new Dimension(100, 18));
                            advancedTestTextField.setBounds(110, 10 + 23 * (i + 1), 197, 20);
                            advancedTestTextField.setEnabled(false);
                            advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                            hoardData.put("Version Date From", advancedTestTextField);
                            dateSelectButton.setEnabled(false);
                            dateSelectButton.setBounds(310, 10 + 23 * (i + 1), 20, 20);
                            dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                            dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date From");
                            dateSelectButton.addActionListener(this);
                            hoardData.put("VersionDateFromButton", dateSelectButton);
                            dateChooserButtonList.add("Version Date From");
                            builder.append(advancedTestLabel, advancedTestTextField, dateSelectButton);
                            builder.nextLine();
                            advancedTestLabel = new JLabel("To");
                            advancedTestLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
                            advancedTestLabel.setPreferredSize(new Dimension(100, 18));
                            advancedTestLabel.setMinimumSize(new Dimension(100, 18));
                            advancedTestLabel.setMaximumSize(new Dimension(100, 18));
                            advancedTestTextField = new JTextField();
                            advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                            advancedTestTextField.setEnabled(false);
                            advancedTestTextField.setBounds(110, 10 + 23 * (i + 2), 197, 20);
                            hoardData.put("Version Date To", advancedTestTextField);
                            dateSelectButton = new JButton();
                            dateSelectButton.setEnabled(false);
                            dateSelectButton.setBounds(310, 10 + 23 * (i + 2), 20, 20);
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
                            if(versionBoolean)
                                advancedComboBox.setBounds(110, 10 + 23 * (i + 2), 220, 20);
                            else
                                advancedComboBox.setBounds(110, 10 + 23 * i, 220, 20);
                            builder.append(advancedTestLabel, advancedComboBox);
                            builder.nextLine();
                        } else
                        {
                            advancedComboBox = new JComboBox();
                            if(versionBoolean)
                                advancedComboBox.setBounds(110, 10 + 23 * (i + 2), 220, 20);
                            else
                                advancedComboBox.setBounds(110, 10 + 23 * i, 220, 20);
                            builder.append(advancedTestLabel, advancedComboBox);
                            builder.nextLine();
                        }
                        hoardData.put((String)fieldData.get("name"), advancedComboBox);
                        if(tmpBoolean)
                            versionBoolean = true;
                    }
                }
            }
        }

        if(isEnd2Versionable)
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
            tmpDOS = new DOSChangeable();
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
        ((UIGeneration)parentFr).setFilterDialogToNull();
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
                        } else
                        if(((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                        {
                            DOSChangeable dosCodeItem = dos.getCodeItem(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText(dosCodeItem.get("name") + " [" + dosCodeItem.get("codeitemid") + "]");
                            searchConditionData.put(fieldOuid, dosCodeItem.get("ouid"));
                        } else
                        {
                            ((JTextField)hoardData.get(sign)).setText(selectOuid);
                            searchConditionData.put(fieldOuid, selectOuid);
                        }
                        break;
                    }

                } else
                {
                    ((JTextField)hoardData.get(sign)).setText(selectOuid);
                }
            } else
            if(hoardData.get(sign) instanceof JComboBox)
                if(sign.equals("md$status"))
                {
                    String values = DynaMOAD.getMSRString(selectOuid, selectOuid, 0);
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
        menuSelect.setText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        menuSelect.setActionCommand("select");
        menuSelect.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
        menuSelect.setAlignmentX(0.0F);
        filterPopupMenu.add(menuSelect);
        menuSelect.addActionListener(this);
        menuDelete = new JMenuItem();
        menuDelete.setText(DynaMOAD.getMSRString("WRD_0002", "delete", 3));
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
            closeDialog(null);
        else
        if(command.equals("close"))
            closeDialog(null);
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

            if(isEnd2Versionable && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                searchCondition.put("version.condition.type", "wip");
            ((UIGeneration)parentFr).setFilterInLink(searchCondition);
            ((UIGeneration)parentFr).refreshLinkTable();
            ((UIGeneration)parentFr).linkFilterComboBox.setSelectedItem(valueTextField.getText());
            dispose();
            ((UIGeneration)parentFr).setFilterDialogToNull();
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

                if(isEnd2Versionable && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                    searchCondition.put("version.condition.type", "wip");
                if(Utils.isNullString(valueTextField.getText()) || searchCondition.size() == 0)
                {
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0007", "Input Filter Name.", 3), DynaMOAD.getMSRString("INF_0003", "Information Message", 3), 1);
                    return;
                }
                if(publicRdoBtn.isSelected())
                {
                    nds.removeNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + valueTextField.getText());
                    nds.addNode("::", "WORKSPACE", "LINKFILTER", "");
                    nds.addNode("::/WORKSPACE", modelOuid, "LINKFILTER", "MODELOUID");
                    nds.addNode("::/WORKSPACE/" + modelOuid, "PUBLIC", "LINKFILTER", "PUBLIC");
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC", "FILTER", "LINKFILTER", "FILTER");
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER", ouid, "LINKFILTER", ouid);
                    nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid, valueTextField.getText(), "LINKFILTER", valueTextField.getText());
                    if(isviewChkBox.isSelected())
                        nds.addArgument("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + valueTextField.getText(), "isview", "T");
                    else
                        nds.addArgument("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + valueTextField.getText(), "isview", "F");
                    for(int i = 0; i < fieldSearchList.size(); i++)
                    {
                        DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(i);
                        if(searchCondition.get((String)fieldData.get("ouid")) != null)
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof String)
                                nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", (String)searchCondition.get((String)fieldData.get("ouid")));
                            else
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof Boolean)
                                nds.addNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", ((Boolean)searchCondition.get((String)fieldData.get("ouid"))).toString());
                    }

                    setLinkFilterToTable();
                } else
                if(privateRdoBtn.isSelected())
                {
                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + valueTextField.getText());
                    nds.addNode("::", "WORKSPACE", "LINKFILTER", "");
                    nds.addNode("::/WORKSPACE", LogIn.userID, "LINKFILTER", "USERID");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID, "FILTER", "LINKFILTER", "FILTER");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/FILTER", ouid, "LINKFILTER", ouid);
                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid, valueTextField.getText(), "LINKFILTER", valueTextField.getText());
                    for(int i = 0; i < fieldSearchList.size(); i++)
                    {
                        DOSChangeable fieldData = (DOSChangeable)fieldSearchList.get(i);
                        if(searchCondition.get((String)fieldData.get("ouid")) != null)
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof String)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", (String)searchCondition.get((String)fieldData.get("ouid")));
                            else
                            if(searchCondition.get((String)fieldData.get("ouid")) instanceof Boolean)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + valueTextField.getText(), (String)fieldData.get("ouid"), "LINKFILTER", ((Boolean)searchCondition.get((String)fieldData.get("ouid"))).toString());
                    }

                    setLinkFilterToTable();
                }
                ((UIGeneration)parentFr).rebuildToolBar();
                JOptionPane.showMessageDialog(this, "\uC800\uC7A5\uB418\uC5C8\uC2B5\uB2C8\uB2E4", "\uC800\uC7A5 \uC644\uB8CC", 1);
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
                    nds.removeNode("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + valueTextField.getText());
                else
                if(privateRdoBtn.isSelected())
                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + valueTextField.getText());
                clearAllFieldValue();
                searchData.clear();
                resultTable.changeTableData();
                setLinkFilterToTable();
                ((UIGeneration)parentFr).rebuildToolBar();
                JOptionPane.showMessageDialog(this, "\uC0AD\uC81C\uB418\uC5C8\uC2B5\uB2C8\uB2E4.", "\uC0AD\uC81C \uC644\uB8CC", 1);
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
                isviewChkBox.setEnabled(true);
                saveButton.setEnabled(true);
                if(resultTable.getTable().getSelectedRowCount() > 0)
                    menuDelete.setEnabled(true);
            } else
            {
                isviewChkBox.setEnabled(false);
                isviewChkBox.setSelected(false);
                saveButton.setEnabled(false);
                if(resultTable.getTable().getSelectedRowCount() > 0)
                    menuDelete.setEnabled(false);
            }
        } else
        {
            isviewChkBox.setEnabled(false);
            isviewChkBox.setSelected(false);
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
                    clearAllFieldValue();
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
                    valueTextField.setText((String)selectedData.get(1));
                    if(((String)selectedData.get(0)).equals("PUBLIC"))
                    {
                        publicRdoBtn.setSelected(true);
                        String filterArgument = nds.getArgument("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + (String)selectedData.get(1), "isview");
                        if(!Utils.isNullString(filterArgument) && filterArgument.equals("T"))
                            isviewChkBox.setSelected(true);
                        else
                            isviewChkBox.setSelected(false);
                        ArrayList filterValue = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + (String)selectedData.get(1));
                        for(int i = 0; i < filterValue.size(); i++)
                        {
                            String value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + ouid + "/" + (String)selectedData.get(1) + "/" + (String)filterValue.get(i));
                            setFieldInDateField(value, mappingToFiledList((String)filterValue.get(i)));
                        }

                    } else
                    if(((String)selectedData.get(0)).equals("PRIVATE"))
                    {
                        privateRdoBtn.setSelected(true);
                        ArrayList filterValue = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + (String)selectedData.get(1));
                        for(int i = 0; i < filterValue.size(); i++)
                        {
                            String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + ouid + "/" + (String)selectedData.get(1) + "/" + (String)filterValue.get(i));
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

    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;
    private JSplitPane mainSplitPane;
    private JPanel mainPanel;
    private JToolBar toolBar;
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
    private int selectCnt;
    final int init_xcord = 10;
    final int init_ycord = 0;
    final int DIV_SIZE = 320;
    final int titleWidth = 100;
    final int totalWidth = 320;
    final int fieldHeight = 20;
    final int offset = 3;
    final int buttonHeight = 0;
    final int title_width = 100;
    final int title_height = 18;
    private DOSChangeable hoardData;
    private ArrayList fieldSearchList;
    private String instanceOuid;
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
    private ArrayList assoTableInfo;
    private ArrayList end2OuidList;
    private boolean isEnd2Versionable;
    private boolean existsAssociationClass;
    private FormLayout layout;
    private DefaultFormBuilder builder;
    private JLabel ownershipLabel;
    private JPanel rdoBtnPanel;
    private ButtonGroup buttonGroup;
    private JRadioButton publicRdoBtn;
    private JRadioButton privateRdoBtn;
    private JLabel isviewLabel;
    private JCheckBox isviewChkBox;



}