// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ObjectSelectDialog.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
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
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, LogIn, AuthoritySearch, 
//            UIGeneration

public class ObjectSelectDialog extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public ObjectSelectDialog(Frame frame, Object parent, boolean modal, String classouid)
    {
        super(frame, modal);
        handCursor = new Cursor(12);
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        searchConditionPanel = new JPanel();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        resultTableScrPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        searchButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        dateButton = new JButton();
        fieldComboBox = new JComboBox();
        comparatorComboBox = null;
        statusComboBox = null;
        booleanComboBox = null;
        valueTextField = new JTextField();
        resultTable = new Table();
        fields = null;
        columnNames = null;
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        newUI = DynaMOAD.newUI;
        searchData = null;
        columnName = null;
        columnWidth = null;
        fieldList = null;
        fieldListForSearch = new ArrayList();
        NDS_CODE = null;
        hoardData = new DOSChangeable();
        searchConditionData = new DOSChangeable();
        fieldSearchList = new ArrayList();
        signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        modelOuid = "";
        classOuid = "";
        selectedOuid = "";
        selectedNumber = "";
        dateChooserButtonList = new ArrayList();
        uiGenration = null;
        jd = new JDialog(this, "Search Date Range");
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        okButton = new JButton();
        clearButton = new JButton();
        parentFr = frame;
        parentWindow = parent;
        classOuid = classouid;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComponents();
        advancedPanelInit();
        makeSearchResultTable();
        pack();
        System.out.println("ObjectSelectDialog");
    }

    private void initComponents()
    {
        addWindowListener(this);
        searchButton.setActionCommand("search");
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.setToolTipText("Search");
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.addActionListener(this);
        searchButton.setDoubleBuffered(true);
        dateButton.setActionCommand("date");
        dateButton.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateButton.setToolTipText("Date select");
        dateButton.setMargin(new Insets(0, 0, 0, 0));
        dateButton.addActionListener(this);
        dateButton.setDoubleBuffered(true);
        selectButton.setActionCommand("select");
        selectButton.setText("Select");
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setToolTipText("Select");
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        closeButton.setActionCommand("close");
        closeButton.setText("Close");
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        closeButton.setToolTipText("Close");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.setBackground(UIManagement.panelBackGround);
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(10));
        toolPanel.add(closeButton);
        initComparator();
        comparatorComboBox.setSelectedItem("work in progress");
        comparatorComboBox.setDoubleBuffered(true);
        comparatorComboBox.setBackground(UIManagement.panelBackGround);
        comparatorComboBox.setBorder(UIManagement.borderTextBoxEditable);
        comparatorComboBox.setPreferredSize(new Dimension(100, 10));
        comparatorComboBox.setActionCommand("version type combo");
        comparatorComboBox.addActionListener(this);
        fieldComboBox.setDoubleBuffered(true);
        fieldComboBox.setBackground(UIManagement.panelBackGround);
        fieldComboBox.setBorder(UIManagement.borderTextBoxEditable);
        fieldComboBox.setPreferredSize(new Dimension(100, 10));
        fieldComboBox.setActionCommand("fieldCombo");
        fieldComboBox.addActionListener(this);
        initStatus();
        statusComboBox.setDoubleBuffered(true);
        statusComboBox.setBackground(UIManagement.panelBackGround);
        statusComboBox.setBorder(UIManagement.borderTextBoxEditable);
        statusComboBox.setPreferredSize(new Dimension(100, 10));
        initBooleanComboBox();
        booleanComboBox.setDoubleBuffered(true);
        booleanComboBox.setBackground(UIManagement.panelBackGround);
        booleanComboBox.setBorder(UIManagement.borderTextBoxEditable);
        booleanComboBox.setPreferredSize(new Dimension(100, 10));
        valueTextField.setDoubleBuffered(true);
        valueTextField.setPreferredSize(new Dimension(100, 10));
        valueTextField.setBorder(UIManagement.borderTextBoxEditable);
        optionPanel.setLayout(new BoxLayout(optionPanel, 0));
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.setDoubleBuffered(true);
        try
        {
            classInfo = dos.getClass(classOuid);
            if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                optionPanel.add(comparatorComboBox);
            optionPanel.add(Box.createHorizontalStrut(5));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        optionPanel.add(fieldComboBox);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(valueTextField);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(searchButton);
        resultTableScrPane.setViewportView(resultTable.getTable());
        resultTableScrPane.setDoubleBuffered(true);
        resultTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setDoubleBuffered(true);
        centerPanel.add(optionPanel, "North");
        centerPanel.add(resultTableScrPane, "Center");
        conditionScrPane.getViewport().add(searchConditionPanel, null);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setDoubleBuffered(true);
        mainPanel.setPreferredSize(new Dimension(500, 300));
        mainPanel.add(toolPanel, "South");
        mainPanel.add(conditionScrPane, "West");
        mainPanel.add(centerPanel, "Center");
        setTitle("Object Select");
        getContentPane().add(mainPanel);
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
        okButton.setBounds(100, 85, 40, 40);
        okButton.setToolTipText("OK");
        okButton.setActionCommand("ok");
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok32.gif")));
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.addActionListener(this);
        okButton.setDoubleBuffered(true);
        clearButton.setBounds(145, 85, 40, 40);
        clearButton.setToolTipText("CLEAR");
        clearButton.setActionCommand("clear");
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear32.gif")));
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.addActionListener(this);
        clearButton.setDoubleBuffered(true);
    }

    private void initComparator()
    {
        String division01 = "last released";
        String division02 = "work in progress";
        String division03 = "created date";
        String division04 = "modified date";
        String versionType[] = {
            division01, division02, division03, division04
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

    public void makeSearchResultTable()
    {
        try
        {
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
            this.fieldList = new ArrayList();
            this.fieldList = dos.listFieldInClass(classOuid);
            if(this.fieldList != null)
            {
                classInfo = new DOSChangeable();
                classInfo = dos.getClass(classOuid);
                DOSChangeable tmpDOS = new DOSChangeable();
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    tmpDOS.put("title", "version type");
                    tmpDOS.put("ouid", "version.condition.type");
                    tmpDOS.put("description", "");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("is.visible", Boolean.TRUE);
                    this.fieldList.add(2, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                }
            }
            fieldListForSearch.clear();
            fieldComboBox.removeAllItems();
            for(int i = 0; i < this.fieldList.size(); i++)
            {
                DOSChangeable fieldData = (DOSChangeable)this.fieldList.get(i);
                if(!((String)fieldData.get("ouid")).equals("version.condition.type"))
                {
                    fieldListForSearch.add((String)fieldData.get("ouid"));
                    fieldComboBox.addItem(fieldData.get("title"));
                    orderOfCombo.add(fieldData.get("ouid"));
                }
            }

            DOSChangeable selectedResult = new DOSChangeable();
            int size = 0;
            if(fieldColumn != null)
            {
                for(int i = 0; i < fieldColumn.size(); i++)
                {
                    String test = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)fieldColumn.get(i));
                    selectedResult.put((new Integer(size)).toString(), Utils.tokenizeMessage(test, '/').get(1));
                    size++;
                }

                selectedRows = new int[size];
                for(int i = 0; i < fieldColumn.size(); i++)
                {
                    String test = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)fieldColumn.get(i));
                    Integer index = new Integer((String)Utils.tokenizeMessage(test, '/').get(0));
                    selectedRows[i] = index.intValue();
                }

                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    columnName.add((String)selectedResult.get((new Integer(i)).toString()));
                    columnWidth.add(new Integer(80));
                }

                resultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
                resultTable.setColumnSequence(selectedRows);
                resultTable.setIndexColumn(0);
                resultTable.getTable().setCursor(handCursor);
                resultTable.getTable().addMouseListener(this);
                resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                resultTableScrPane.setViewportView(resultTable.getTable());
                resultTableScrPane.getViewport().updateUI();
            } else
            {
                ArrayList fieldList = makeAllFieldTable();
                for(int i = 0; i < fieldList.size(); i++)
                {
                    String test = (String)fieldList.get(i);
                    selectedResult.put((new Integer(size)).toString(), Utils.tokenizeMessage(test, '/').get(1));
                    size++;
                }

                selectedRows = new int[size];
                for(int i = 0; i < fieldList.size(); i++)
                {
                    String test = (String)fieldList.get(i);
                    Integer index = new Integer((String)Utils.tokenizeMessage(test, '/').get(0));
                    selectedRows[i] = index.intValue();
                }

                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    columnName.add((String)selectedResult.get((new Integer(i)).toString()));
                    columnWidth.add(new Integer(80));
                }

                resultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
                resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                resultTable.setColumnSequence(selectedRows);
                resultTable.setIndexColumn(0);
                resultTable.getTable().setCursor(handCursor);
                resultTable.getTable().addMouseListener(this);
                resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                resultTableScrPane.setViewportView(resultTable.getTable());
                resultTableScrPane.getViewport().updateUI();
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setResultData(ArrayList resultData)
    {
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
        selectButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        removeMouseListener(this);
        setVisible(false);
        dispose();
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        ((JTextField)hoardData.get(sign)).setText(selectOuid);
    }

    public void advancedPanelInit()
    {
        try
        {
            searchConditionPanel.setLayout(null);
            searchConditionPanel.setMinimumSize(new Dimension(200, 270));
            searchConditionPanel.setMaximumSize(new Dimension(200, 270));
            searchConditionPanel.setPreferredSize(new Dimension(200, 270));
            searchConditionPanel.setBackground(UIManagement.panelBackGround);
            conditionScrPane.getViewport().add(searchConditionPanel, null);
            fieldList = new ArrayList();
            fieldOuidList = new ArrayList();
            ArrayList searchConditionNds = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid);
            if(searchConditionNds != null)
            {
                for(int k = 0; k < searchConditionNds.size(); k++)
                {
                    String test = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid + "/" + (String)searchConditionNds.get(k));
                    DOSChangeable selectedResult = new DOSChangeable();
                    selectedResult.put("ouid", Utils.tokenizeMessage(test, '/').get(1));
                    selectedResult.put("title", Utils.tokenizeMessage(test, '/').get(0));
                    if(!((String)Utils.tokenizeMessage(test, '/').get(2)).equals("null"))
                        selectedResult.put("type", new Byte((String)Utils.tokenizeMessage(test, '/').get(2)));
                    else
                        selectedResult.put("type", new Byte((byte)13));
                    fieldList.add(selectedResult);
                    fieldSearchList.add(selectedResult);
                }

            } else
            if(searchConditionNds == null)
            {
                fieldList = dos.listFieldInClass(classOuid);
                DOSChangeable classInfo = new DOSChangeable();
                classInfo = dos.getClass(classOuid);
                DOSChangeable tmpDOS = new DOSChangeable();
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    tmpDOS.put("title", "version type");
                    tmpDOS.put("ouid", "version.condition.type");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("type", new Byte((byte)13));
                    fieldList.add(2, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                }
            }
            fieldOuidList = dos.listFieldInClass(classOuid);
            int y_pos = 0;
            if(fieldList != null)
            {
                DOSChangeable classInfo = new DOSChangeable();
                classInfo = dos.getClass(classOuid);
                DOSChangeable tmpDOS = new DOSChangeable();
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    tmpDOS.put("title", "version type");
                    tmpDOS.put("ouid", "version.condition.type");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("description", "");
                    fieldOuidList.add(2, tmpDOS);
                    tmpDOS = null;
                    tmpDOS = new DOSChangeable();
                }
                fieldListForSearch.clear();
                initStatus();
                String division01 = "last released";
                String division02 = "work in progress";
                String division03 = "created date";
                String division04 = "modified date";
                String versionType[] = {
                    "", division01, division02, division03, division04
                };
                String trueStr = "true";
                String falseStr = "false";
                String trueOrFalse[] = {
                    "", trueStr, falseStr
                };
                HashMap setDatas = (HashMap)searchConditionData.get(classOuid);
                boolean tmpBoolean = false;
                boolean versionBoolean = false;
                for(int i = 0; i < fieldList.size(); i++)
                {
                    JLabel advancedTestLabel = new JLabel();
                    JTextField advancedTestTextField = new JTextField();
                    JButton dateSelectButton = new JButton();
                    DOSChangeable fieldData = (DOSChangeable)fieldList.get(i);
                    advancedTestLabel = new JLabel((String)fieldData.get("title"));
                    if(versionBoolean)
                        advancedTestLabel.setBounds(10, 10 + 23 * (i + 2), 70, 20);
                    else
                        advancedTestLabel.setBounds(10, 10 + 23 * i, 70, 20);
                    searchConditionPanel.add(advancedTestLabel);
                    if(!((String)fieldData.get("ouid")).equals("md$status") && !((String)fieldData.get("ouid")).equals("version.condition.type") && ((Byte)fieldData.get("type")).byteValue() != 1)
                    {
                        if(!((String)fieldData.get("ouid")).equals("md$cdate") && !((String)fieldData.get("ouid")).equals("md$mdate") && ((Byte)fieldData.get("type")).byteValue() != 22)
                        {
                            advancedTestTextField.setBorder(UIManagement.borderTextBoxNotEditable);
                            if(versionBoolean)
                                advancedTestTextField.setBounds(80, 10 + 23 * (i + 2), 110, 20);
                            else
                                advancedTestTextField.setBounds(80, 10 + 23 * i, 110, 20);
                            searchConditionPanel.add(advancedTestTextField);
                            hoardData.put((String)fieldData.get("title"), advancedTestTextField);
                            if(setDatas != null)
                                advancedTestTextField.setText((String)setDatas.get((String)fieldData.get("ouid")));
                        } else
                        {
                            advancedTestTextField.setBorder(UIManagement.borderTextBoxNotEditable);
                            if(versionBoolean)
                                advancedTestTextField.setBounds(80, 10 + 23 * (i + 2), 87, 20);
                            else
                                advancedTestTextField.setBounds(80, 10 + 23 * i, 87, 20);
                            searchConditionPanel.add(advancedTestTextField);
                            hoardData.put((String)fieldData.get("title"), advancedTestTextField);
                            dateSelectButton.setBorder(UIManagement.borderTextBoxNotEditable);
                            if(versionBoolean)
                                dateSelectButton.setBounds(170, 10 + 23 * (i + 2), 20, 20);
                            else
                                dateSelectButton.setBounds(170, 10 + 23 * i, 20, 20);
                            dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                            dateSelectButton.setActionCommand((String)fieldData.get("title"));
                            dateSelectButton.addActionListener(this);
                            dateChooserButtonList.add((String)fieldData.get("title"));
                            searchConditionPanel.add(dateSelectButton);
                            if(setDatas != null)
                                advancedTestTextField.setText((String)setDatas.get((String)fieldData.get("ouid")));
                        }
                    } else
                    {
                        JComboBox advancedComboBox;
                        if(((String)fieldData.get("ouid")).equals("md$status"))
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
                            advancedComboBox.setActionCommand((String)fieldData.get("title"));
                            advancedComboBox.addActionListener(this);
                            tmpBoolean = true;
                            advancedTestLabel = new JLabel("From");
                            advancedTestLabel.setBounds(10, 10 + 23 * (i + 1), 70, 20);
                            searchConditionPanel.add(advancedTestLabel);
                            advancedTestLabel = new JLabel("To");
                            advancedTestLabel.setBounds(10, 10 + 23 * (i + 2), 70, 20);
                            searchConditionPanel.add(advancedTestLabel);
                            advancedTestTextField.setBorder(UIManagement.borderTextBoxNotEditable);
                            advancedTestTextField.setBounds(80, 10 + 23 * (i + 1), 87, 20);
                            advancedTestTextField.setEnabled(false);
                            advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                            searchConditionPanel.add(advancedTestTextField);
                            hoardData.put("VersionTypeFrom", advancedTestTextField);
                            dateSelectButton.setBorder(UIManagement.borderTextBoxNotEditable);
                            dateSelectButton.setEnabled(false);
                            dateSelectButton.setBounds(170, 10 + 23 * (i + 1), 20, 20);
                            dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                            dateSelectButton.setActionCommand("VersionTypeFrom");
                            dateSelectButton.addActionListener(this);
                            hoardData.put("VersionTypeFromButton", dateSelectButton);
                            dateChooserButtonList.add("VersionTypeFrom");
                            searchConditionPanel.add(dateSelectButton);
                            if(setDatas != null)
                                advancedTestTextField.setText((String)setDatas.get((String)fieldData.get("ouid")));
                            advancedTestTextField = new JTextField();
                            advancedTestTextField.setBorder(UIManagement.borderTextBoxNotEditable);
                            advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                            advancedTestTextField.setEnabled(false);
                            advancedTestTextField.setBounds(80, 10 + 23 * (i + 2), 87, 20);
                            searchConditionPanel.add(advancedTestTextField);
                            hoardData.put("VersionTypeTo", advancedTestTextField);
                            dateSelectButton = new JButton();
                            dateSelectButton.setBorder(UIManagement.borderTextBoxNotEditable);
                            dateSelectButton.setEnabled(false);
                            dateSelectButton.setBounds(170, 10 + 23 * (i + 2), 20, 20);
                            dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                            dateSelectButton.setActionCommand("VersionTypeTo");
                            dateSelectButton.addActionListener(this);
                            hoardData.put("VersionTypeToButton", dateSelectButton);
                            dateChooserButtonList.add("VersionTypeTo");
                            searchConditionPanel.add(dateSelectButton);
                            if(setDatas != null)
                                advancedTestTextField.setText((String)setDatas.get((String)fieldData.get("ouid")));
                            if(setDatas != null)
                            {
                                String selectedStr = (String)setDatas.get((String)fieldData.get("ouid"));
                                if(selectedStr != null)
                                {
                                    String values = "";
                                    if(selectedStr.equals("released"))
                                        values = "last released";
                                    else
                                    if(selectedStr.equals("wip"))
                                        values = "work in progress";
                                    else
                                    if(selectedStr.equals("cdate"))
                                        values = "created date";
                                    else
                                        values = "modified data";
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
                        advancedComboBox.setBackground(UIManagement.panelBackGround);
                        advancedComboBox.setBorder(UIManagement.borderTextBoxNotEditable);
                        if(versionBoolean)
                            advancedComboBox.setBounds(80, 10 + 23 * (i + 2), 110, 20);
                        else
                            advancedComboBox.setBounds(80, 10 + 23 * i, 110, 20);
                        searchConditionPanel.add(advancedComboBox);
                        hoardData.put((String)fieldData.get("title"), advancedComboBox);
                        if(tmpBoolean)
                            versionBoolean = true;
                    }
                    if(versionBoolean)
                        y_pos = 10 + 23 * (i + 4);
                    else
                        y_pos = 10 + 23 * (i + 2);
                }

            }
            for(int i = 0; i < fieldOuidList.size(); i++)
            {
                DOSChangeable fieldData = (DOSChangeable)fieldOuidList.get(i);
                if(!((String)fieldData.get("ouid")).equals("version.condition.type"))
                    fieldListForSearch.add((String)fieldData.get("ouid"));
            }

            searchConditionPanel.setMinimumSize(new Dimension(200, y_pos));
            searchConditionPanel.setMaximumSize(new Dimension(200, y_pos));
            searchConditionPanel.setPreferredSize(new Dimension(200, y_pos));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public ArrayList makeAllFieldTable()
    {
        try
        {
            ArrayList tmpFieldData = new ArrayList();
            ArrayList fieldList = new ArrayList();
            ArrayList tmpList = new ArrayList();
            tmpList = dos.listFieldInClass(classOuid);
            for(int i = 0; i < tmpList.size(); i++)
            {
                DOSChangeable testDos = (DOSChangeable)tmpList.get(i);
                fieldList.add((String)testDos.get("title"));
            }

            DOSChangeable classInfo = new DOSChangeable();
            classInfo = dos.getClass(classOuid);
            String tmpStr = "";
            if(!Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
            {
                tmpStr = "ouid";
                fieldList.add(0, tmpStr);
                tmpStr = "number";
                fieldList.add(1, tmpStr);
                tmpStr = "description";
                fieldList.add(2, tmpStr);
                tmpStr = "status";
                fieldList.add(3, tmpStr);
                tmpStr = "user";
                fieldList.add(4, tmpStr);
                tmpStr = "created date";
                fieldList.add(5, tmpStr);
                tmpStr = "modified date";
                fieldList.add(6, tmpStr);
            } else
            {
                tmpStr = "ouid";
                fieldList.add(0, tmpStr);
                tmpStr = "number";
                fieldList.add(1, tmpStr);
                tmpStr = "description";
                fieldList.add(2, tmpStr);
                tmpStr = "version";
                fieldList.add(3, tmpStr);
                tmpStr = "status";
                fieldList.add(4, tmpStr);
                tmpStr = "user";
                fieldList.add(5, tmpStr);
                tmpStr = "created date";
                fieldList.add(6, tmpStr);
                tmpStr = "modified date";
                fieldList.add(7, tmpStr);
            }
            for(int i = 1; i < fieldList.size(); i++)
                tmpFieldData.add((new Integer(i)).toString() + "^" + (String)fieldList.get(i));

            return tmpFieldData;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return null;
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
                HashMap searchCondition = new HashMap();
                int row = fieldComboBox.getSelectedIndex();
                String comboOuid = (String)orderOfCombo.get(row);
                DOSChangeable fieldInfo = dos.getField(comboOuid);
                if(comboOuid.equals("md$status"))
                {
                    if(!Utils.isNullString(statusComboBox.getSelectedItem().toString()))
                    {
                        String text = statusComboBox.getSelectedItem().toString();
                        searchCondition.put("md$status", statusValueMap.get(text));
                    }
                } else
                if(fieldInfo != null && fieldInfo.get("type").toString().equals((new Byte((byte)1)).toString()))
                    searchCondition.put(comboOuid, new Boolean(booleanComboBox.getSelectedItem().toString()));
                if(!Utils.isNullString(valueTextField.getText()))
                    searchCondition.put(comboOuid, valueTextField.getText());
                String selectedStr = (String)comparatorComboBox.getSelectedItem();
                String values = "";
                if(selectedStr.equals("last released"))
                    values = "released";
                else
                if(selectedStr.equals("work in progress"))
                    values = "wip";
                else
                if(selectedStr.equals("created date"))
                    values = "cdate";
                else
                    values = "mdate";
                if(selectedStr.equals("created date") || selectedStr.equals("modified date"))
                {
                    searchCondition.put("version.condition.date.from", FromField.getText());
                    searchCondition.put("version.condition.date.to", ToField.getText());
                }
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                    searchCondition.put("version.condition.type", values);
                ArrayList testList = dos.list(this.classOuid, fieldListForSearch, searchCondition);
                searchCondition.clear();
                setResultData(testList);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("select"))
            selectButton_actionPerformed();
        else
        if(command.equals("version type combo"))
        {
            String selectedItem = (String)comparatorComboBox.getSelectedItem();
            if(selectedItem.equals("created date") || selectedItem.equals("modified date"))
            {
                jd.getContentPane().setLayout(null);
                jd.setBackground(Color.blue);
                UIUtils.setLocationRelativeTo(jd, searchButton);
                jd.setResizable(false);
                jd.setSize(200, 160);
                jd.getContentPane().add(FromLabel);
                jd.getContentPane().add(FromField);
                jd.getContentPane().add(date_chooser);
                jd.getContentPane().add(ToLabel);
                jd.getContentPane().add(ToField);
                jd.getContentPane().add(date_chooser2);
                jd.getContentPane().add(okButton);
                jd.getContentPane().add(clearButton);
                jd.show();
            } else
            {
                jd.dispose();
            }
        } else
        if(command.equals("date"))
        {
            new DynaDateChooser(parentFr);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                setConditionField(sdfYMD.format(date));
            else
                setConditionField("");
        } else
        if(command.equals("date1"))
        {
            new DynaDateChooser(parentFr);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                FromField.setText(sdfYMD.format(date));
            else
                FromField.setText("");
        } else
        if(command.equals("date2"))
        {
            new DynaDateChooser(parentFr);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                ToField.setText(sdfYMD.format(date));
            else
                ToField.setText("");
        } else
        if(command.equals("ok"))
            jd.dispose();
        else
        if(command.equals("clear"))
        {
            ToField.setText("");
            FromField.setText("");
        } else
        if(command.equals("fieldCombo"))
        {
            if(((String)fieldComboBox.getSelectedItem()).equals("created date") || ((String)fieldComboBox.getSelectedItem()).equals("modified date"))
            {
                optionPanel.remove(searchButton);
                optionPanel.add(dateButton);
                optionPanel.add(searchButton);
                optionPanel.updateUI();
            } else
            {
                optionPanel.remove(dateButton);
                optionPanel.updateUI();
            }
            if(fieldComboBox.getSelectedItem().toString().equals("status"))
            {
                optionPanel.add(statusComboBox, 6);
                valueTextField.setEditable(false);
                optionPanel.updateUI();
            } else
            {
                optionPanel.remove(statusComboBox);
                valueTextField.setEditable(true);
                optionPanel.updateUI();
            }
            try
            {
                int row = fieldComboBox.getSelectedIndex();
                DOSChangeable fieldInfo = new DOSChangeable();
                if(orderOfCombo.size() > 0)
                {
                    String comboOuid = (String)orderOfCombo.get(row);
                    fieldInfo = dos.getField(comboOuid);
                    if(fieldInfo != null && fieldInfo.get("type").toString().equals((new Byte((byte)1)).toString()))
                    {
                        optionPanel.add(booleanComboBox, 6);
                        valueTextField.setEditable(false);
                        optionPanel.updateUI();
                    } else
                    {
                        optionPanel.remove(booleanComboBox);
                        valueTextField.setEditable(true);
                        optionPanel.updateUI();
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(!command.equals("statusComboBox"))
        {
            int tmpInt = 0;
            for(int i = 0; i < dateChooserButtonList.size(); i++)
                if(command.equals(dateChooserButtonList.get(i)))
                    tmpInt = i;

            String classOuid = (String)dateChooserButtonList.get(tmpInt);
            if(classOuid != null)
            {
                new DynaDateChooser((Frame)parentWindow);
                java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
                if(date != null)
                    setFieldInDateField(sdfYMD.format(date), (String)dateChooserButtonList.get(tmpInt));
                else
                    setFieldInDateField("", (String)dateChooserButtonList.get(tmpInt));
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

    public void mousePressed(MouseEvent mouseEvent)
    {
        int row = resultTable.getTable().getSelectedRow();
        String ouidRow = resultTable.getSelectedOuidRow(row);
        if(ouidRow != null)
        {
            selectedOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
            selectedNumber = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(1);
        } else
        {
            selectedOuid = (String)((ArrayList)searchData.get(row)).get(0);
            selectedNumber = (String)((ArrayList)searchData.get(row)).get(1);
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getClickCount() == 2)
            selectButton_actionPerformed();
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private void selectButton_actionPerformed()
    {
        ((AuthoritySearch)parentWindow).setObject(selectedOuid, selectedNumber);
        closeDialog(null);
    }

    private Cursor handCursor;
    private JPanel mainPanel;
    private JPanel toolPanel;
    private JPanel searchConditionPanel;
    private JPanel centerPanel;
    private JPanel optionPanel;
    private JScrollPane resultTableScrPane;
    private JScrollPane conditionScrPane;
    private JButton searchButton;
    private JButton selectButton;
    private JButton closeButton;
    private JButton dateButton;
    private JComboBox fieldComboBox;
    private JComboBox comparatorComboBox;
    private JComboBox statusComboBox;
    private JComboBox booleanComboBox;
    private JTextField valueTextField;
    private Table resultTable;
    private static Hashtable comparator = null;
    private static Font font = new Font("Dialog", 0, 10);
    private ArrayList fields;
    private ArrayList columnNames;
    private NDS nds;
    private DOS dos;
    private DSS dss;
    private UIManagement newUI;
    private int selectedRows[];
    private ArrayList searchData;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private ArrayList fieldList;
    private ArrayList fieldListForSearch;
    private String NDS_CODE;
    final int init_xcord = 10;
    final int init_ycord = 0;
    final int button_xcord = 15;
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
    private DOSChangeable searchConditionData;
    private ArrayList fieldSearchList;
    private String signStr;
    private Frame parentFr;
    private ArrayList orderOfCombo;
    private DOSChangeable classInfo;
    private JPopupMenu popupMenu;
    private JMenu addMenuAdd;
    private JMenuItem informationMenu;
    private JMenuItem addMenu;
    private JMenuItem copyMenu;
    private JMenuItem pasteMenu;
    private JMenuItem deleteMenu;
    private JMenuItem graphAddMenu;
    private JMenuItem cutMenu;
    private JMenuItem replaceMenu;
    private JMenuItem insertIntoMenu;
    private String modelOuid;
    private String classOuid;
    private String selectedOuid;
    private String selectedNumber;
    private Object parentWindow;
    private ArrayList fieldOuidList;
    private ArrayList dateChooserButtonList;
    private UIGeneration uiGenration;
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_UTF = 13;
    private final byte DATATYPE_BOOLEAN = 1;
    private final int dateButtonWidth = 20;
    private final int REGISTER_MODE = 0;
    private final int MODIFY_MODE = 1;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private JDialog jd;
    private JLabel FromLabel;
    private JLabel ToLabel;
    private JTextField FromField;
    private JTextField ToField;
    private JButton date_chooser;
    private JButton date_chooser2;
    private JButton okButton;
    private JButton clearButton;
    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;

}