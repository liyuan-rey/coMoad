// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcessAuthoritySetting.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, LogIn, PrincipalSelect

public class ProcessAuthoritySetting extends JFrame
{
    class ListenerCommon extends WindowAdapter
        implements ActionListener, MouseListener, ItemListener
    {

        public void windowOpened(WindowEvent e)
        {
            refreshButton_actionPerformed(null);
        }

        public void windowClosing(WindowEvent e)
        {
            closeEvent();
        }

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            Object source = e.getSource();
            if(command.equals("Save"))
                saveButton_actionPerformed(e);
            else
            if(command.equals("Refresh"))
                refreshButton_actionPerformed(e);
            else
            if(command.equals("Add"))
                addButton_actionPerformed(e);
            else
            if(command.equals("Remove"))
                removeButton_actionPerformed(e);
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
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

        public void itemStateChanged(ItemEvent e)
        {
            if(selectedRowData == null)
                return;
            Object source = e.getItemSelectable();
            int state = e.getStateChange();
            int j = 0;
            if(source == aArrayCheckBox[0] && state == 1)
            {
                dArrayCheckBox[0].setSelected(false);
                for(int i = 1; i < statusNames.length; i++)
                    if(aArrayCheckBox[i].isEnabled())
                        aArrayCheckBox[i].setSelected(true);

            } else
            if(source != aArrayCheckBox[0] || state != 2)
                if(source == dArrayCheckBox[0] && state == 1)
                {
                    aArrayCheckBox[0].setSelected(false);
                    for(int i = 1; i < statusNames.length; i++)
                        if(dArrayCheckBox[i].isEnabled())
                            dArrayCheckBox[i].setSelected(true);

                } else
                {
                    String scope = null;
                    String id = null;
                    HashMap principalMap = null;
                    HashMap principalDataMap = null;
                    HashMap permissionMap = null;
                    HashMap permissionDataMap = null;
                    for(int i = 1; i < statusNames.length; i++)
                    {
                        if(!tableSelectionChanged)
                        {
                            scope = (String)selectedRowData.get(0);
                            id = (String)selectedRowData.get(1);
                            principalMap = (HashMap)scopeMap.get(scope);
                            principalDataMap = (HashMap)principalMap.get(id);
                            permissionMap = (HashMap)principalDataMap.get("permission");
                            if(permissionMap.containsKey(statusKeys[i]))
                            {
                                permissionDataMap = (HashMap)permissionMap.get(statusKeys[i]);
                            } else
                            {
                                permissionDataMap = new HashMap();
                                permissionDataMap.put("allow", "F");
                                permissionDataMap.put("deny", "F");
                                permissionDataMap.put("isgrantable", "F");
                                permissionDataMap.put("description", "P");
                            }
                        }
                        if(source == aArrayCheckBox[i] && state == 1)
                        {
                            dArrayCheckBox[i].setSelected(false);
                            for(j = 1; j < statusNames.length; j++)
                                if(aArrayCheckBox[j].isEnabled() && !aArrayCheckBox[j].isSelected())
                                    break;

                            if(j == statusNames.length)
                                aArrayCheckBox[0].setSelected(true);
                            if(!tableSelectionChanged)
                            {
                                permissionDataMap.put("allow", "T");
                                permissionMap.put(statusKeys[i], permissionDataMap);
                            }
                        } else
                        if(source == aArrayCheckBox[i] && state == 2)
                        {
                            aArrayCheckBox[0].setSelected(false);
                            if(!tableSelectionChanged)
                            {
                                permissionDataMap.put("allow", "F");
                                permissionMap.put(statusKeys[i], permissionDataMap);
                            }
                        } else
                        if(source == dArrayCheckBox[i] && state == 1)
                        {
                            aArrayCheckBox[i].setSelected(false);
                            for(j = 1; j < statusNames.length; j++)
                                if(dArrayCheckBox[j].isEnabled() && !dArrayCheckBox[j].isSelected())
                                    break;

                            if(j == statusNames.length)
                                dArrayCheckBox[0].setSelected(true);
                            if(!tableSelectionChanged)
                            {
                                permissionDataMap.put("deny", "T");
                                permissionMap.put(statusKeys[i], permissionDataMap);
                            }
                        } else
                        if(source == dArrayCheckBox[i] && state == 2)
                        {
                            dArrayCheckBox[0].setSelected(false);
                            if(!tableSelectionChanged)
                            {
                                permissionDataMap.put("deny", "F");
                                permissionMap.put(statusKeys[i], permissionDataMap);
                            }
                        }
                    }

                }
        }

        ListenerCommon()
        {
        }
    }

    class PrincipalTable extends Table
    {

        public void valueChanged(ListSelectionEvent e)
        {
            super.valueChanged(e);
            principalTable_valueChanged(this, e);
        }

        public PrincipalTable()
        {
        }

        public PrincipalTable(ArrayList dataList, ArrayList columnNameList, ArrayList columnWidth, int selection, int tableWidth)
        {
            super(dataList, columnNameList, columnWidth, selection, tableWidth);
        }

        public PrincipalTable(ArrayList datalist, ArrayList columnNamelist, ArrayList columnWidth, int Selection)
        {
            super(datalist, columnNamelist, columnWidth, Selection);
        }
    }


    public ProcessAuthoritySetting(Object parent, String windowTitle, String tgtclsouid, String tgtprocouid)
    {
        super(windowTitle);
        handCursor = new Cursor(12);
        STA_FULL = null;
        STA_AP1 = null;
        STA_AP2 = null;
        STA_CKI = null;
        STA_CKO = null;
        STA_CRT = null;
        STA_OBS = null;
        STA_REJ = null;
        STA_RLS = null;
        STA_RV1 = null;
        STA_RV2 = null;
        STA_WIP = null;
        statusNames = new String[12];
        statusKeys = new String[12];
        NDS_CODE = null;
        aclList = null;
        scopeMap = null;
        selectedRowData = null;
        targetClassOuid = null;
        targetProcessOuid = null;
        title = null;
        tableSelectionChanged = false;
        grantablePermission = null;
        tableData = null;
        columns = null;
        buttonToolBar = null;
        saveButton = null;
        refreshButton = null;
        titleLabel = null;
        principalPanel = null;
        tableScrPane = null;
        principalTable = null;
        addButton = null;
        removeButton = null;
        permissionPanel = null;
        namePanel = null;
        boxPanel = null;
        nameLabel = null;
        arrayLabel = null;
        allowLabel = null;
        aArrayCheckBox = null;
        denyLabel = null;
        dArrayCheckBox = null;
        newUI = null;
        acl = null;
        aus = null;
        dos = null;
        nds = null;
        wfm = null;
        listenerCommon = null;
        targetClassOuid = tgtclsouid;
        if(!Utils.isNullString(tgtprocouid))
            targetProcessOuid = tgtprocouid;
        else
            targetProcessOuid = "0";
        newUI = DynaMOAD.newUI;
        acl = DynaMOAD.acl;
        aus = DynaMOAD.aus;
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        wfm = DynaMOAD.wfm;
        listenerCommon = new ListenerCommon();
        setLocation(200, 100);
        setSize(450, 550);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        addWindowListener(listenerCommon);
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            STA_FULL = "FULL STATUS";
            STA_AP1 = DynaMOAD.getMSRString("AP1", "approving", 0);
            STA_AP2 = DynaMOAD.getMSRString("AP2", "approved", 0);
            STA_CKI = DynaMOAD.getMSRString("CKI", "checked-in", 0);
            STA_CKO = DynaMOAD.getMSRString("CKO", "checked-out", 0);
            STA_CRT = DynaMOAD.getMSRString("CRT", "created", 0);
            STA_OBS = DynaMOAD.getMSRString("OBS", "obsoleted", 0);
            STA_REJ = DynaMOAD.getMSRString("REJ", "rejected", 0);
            STA_RLS = DynaMOAD.getMSRString("RLS", "released", 0);
            STA_RV1 = DynaMOAD.getMSRString("RV1", "reviewing", 0);
            STA_RV2 = DynaMOAD.getMSRString("RV2", "reviewed", 0);
            STA_WIP = DynaMOAD.getMSRString("WIP", "work in progress", 0);
            statusNames[0] = STA_FULL;
            statusNames[1] = STA_AP1;
            statusNames[2] = STA_AP2;
            statusNames[3] = STA_CKI;
            statusNames[4] = STA_CKO;
            statusNames[5] = STA_CRT;
            statusNames[6] = STA_OBS;
            statusNames[7] = STA_REJ;
            statusNames[8] = STA_RLS;
            statusNames[9] = STA_RV1;
            statusNames[10] = STA_RV2;
            statusNames[11] = STA_WIP;
            statusKeys[0] = "";
            statusKeys[1] = "AP1";
            statusKeys[2] = "AP2";
            statusKeys[3] = "CKI";
            statusKeys[4] = "CKO";
            statusKeys[5] = "CRT";
            statusKeys[6] = "OBS";
            statusKeys[7] = "REJ";
            statusKeys[8] = "RLS";
            statusKeys[9] = "RV1";
            statusKeys[10] = "RV2";
            statusKeys[11] = "WIP";
            DOSChangeable classInfo = dos.getClass(targetClassOuid);
            String tmpStr = (String)classInfo.get("title");
            if(!Utils.isNullString(tgtprocouid))
            {
                DOSChangeable processDef = wfm.getProcessDefinition(tgtprocouid);
                tmpStr = tmpStr + " - " + DynaMOAD.getMSRString((String)processDef.get("name"), (String)processDef.get("name"), 0) + " [" + (String)processDef.get("identifier") + "]";
            }
            title = tmpStr;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initialize();
    }

    private void initialize()
    {
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        saveButton = new JButton();
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "save", 3));
        saveButton.setActionCommand("Save");
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.addActionListener(listenerCommon);
        refreshButton = new JButton();
        refreshButton.setToolTipText(DynaMOAD.getMSRString("WRD_0013", "refresh", 3));
        refreshButton.setActionCommand("Refresh");
        refreshButton.setIcon(new ImageIcon(getClass().getResource("/icons/Refresh.gif")));
        refreshButton.setMargin(new Insets(0, 0, 0, 0));
        refreshButton.addActionListener(listenerCommon);
        buttonToolBar = new JToolBar();
        buttonToolBar.add(saveButton);
        buttonToolBar.add(refreshButton);
        titleLabel = new JLabel(title, 2);
        titleLabel.setFont(new Font("Dialog", 2, 17));
        titleLabel.setBackground(Color.white);
        titleLabel.setOpaque(true);
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        principalTableDefine();
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane.setViewportView(principalTable.getTable());
        tableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        tableScrPane.setPreferredSize(new Dimension(300, 120));
        addButton = new JButton(DynaMOAD.getMSRString("WRD_0001", "Add", 3), new ImageIcon(getClass().getResource("")));
        addButton.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Add", 3));
        addButton.setActionCommand("Add");
        addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.addActionListener(listenerCommon);
        addButton.setPreferredSize(new Dimension(70, 25));
        removeButton = new JButton(DynaMOAD.getMSRString("WRD_0002", "Remove", 3), new ImageIcon(getClass().getResource("")));
        removeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Remove", 3));
        removeButton.setActionCommand("Remove");
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.addActionListener(listenerCommon);
        removeButton.setPreferredSize(new Dimension(70, 25));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 3;
        gridBagCon.insets = new Insets(5, 10, 10, 10);
        gridBag.setConstraints(tableScrPane, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 0, 5, 10);
        gridBag.setConstraints(addButton, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 0, 5, 10);
        gridBag.setConstraints(removeButton, gridBagCon);
        principalPanel = new JPanel();
        principalPanel.setLayout(gridBag);
        principalPanel.setBorder(BorderFactory.createTitledBorder(UIManagement.borderPanel, DynaMOAD.getMSRString("WRD_0098", "Principal", 3)));
        principalPanel.add(tableScrPane);
        principalPanel.add(addButton);
        principalPanel.add(removeButton);
        arrayLabel = new JLabel[statusNames.length];
        aArrayCheckBox = new JCheckBox[statusNames.length];
        dArrayCheckBox = new JCheckBox[statusNames.length];
        for(int i = 0; i < statusNames.length; i++)
        {
            arrayLabel[i] = new JLabel(statusNames[i]);
            aArrayCheckBox[i] = new JCheckBox();
            aArrayCheckBox[i].addItemListener(listenerCommon);
            dArrayCheckBox[i] = new JCheckBox();
            dArrayCheckBox[i].addItemListener(listenerCommon);
        }

        nameLabel = new JLabel("");
        namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        namePanel.setBackground(UIManagement.panelBackGround);
        namePanel.setLayout(new GridLayout(0, 1));
        namePanel.add(nameLabel);
        for(int i = 0; i < statusNames.length; i++)
            namePanel.add(arrayLabel[i]);

        allowLabel = new JLabel("Allow", 2);
        denyLabel = new JLabel("Deny", 2);
        boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(0, 2));
        boxPanel.setBackground(UIManagement.panelBackGround);
        boxPanel.add(allowLabel);
        boxPanel.add(denyLabel);
        for(int i = 0; i < statusNames.length; i++)
        {
            boxPanel.add(aArrayCheckBox[i]);
            boxPanel.add(dArrayCheckBox[i]);
        }

        permissionPanel = new JPanel();
        permissionPanel.setBorder(BorderFactory.createTitledBorder(UIManagement.borderGroup, DynaMOAD.getMSRString("WRD_0051", "Permission", 3)));
        permissionPanel.setLayout(new BoxLayout(permissionPanel, 0));
        permissionPanel.setBackground(UIManagement.panelBackGround);
        permissionPanel.add(Box.createHorizontalGlue());
        permissionPanel.add(namePanel);
        permissionPanel.add(boxPanel);
        permissionPanel.add(Box.createHorizontalGlue());
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBag.setConstraints(buttonToolBar, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBag.setConstraints(titleLabel, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBag.setConstraints(principalPanel, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBag.setConstraints(permissionPanel, gridBagCon);
        getContentPane().setLayout(gridBag);
        getContentPane().add(buttonToolBar);
        getContentPane().add(titleLabel);
        getContentPane().add(principalPanel);
        getContentPane().add(permissionPanel);
        refreshButton_actionPerformed(null);
    }

    public void principalTableDefine()
    {
        tableData = new ArrayList();
        ArrayList columnName = new ArrayList();
        ArrayList columnWidth = new ArrayList();
        columnName.add(DynaMOAD.getMSRString("scope", "Scope", 3));
        columnName.add("ID");
        columnName.add(DynaMOAD.getMSRString("name", "Name", 3));
        columnName.trimToSize();
        columnWidth.add(new Integer(90));
        columnWidth.add(new Integer(90));
        columnWidth.add(new Integer(90));
        columnWidth.trimToSize();
        principalTable = new PrincipalTable(tableData, columnName, columnWidth, 0);
        principalTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        principalTable.setIndexColumn(0);
        principalTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        principalTable.getTable().setCursor(handCursor);
        principalTable.getTable().addMouseListener(listenerCommon);
    }

    public void setTableData(ArrayList vt)
    {
        for(int i = 0; i < vt.size(); i++)
            if(!scopeMap.containsKey(((ArrayList)vt.get(i)).get(0)))
            {
                tableData.add(vt.get(i));
                HashMap principalDataMap = new HashMap();
                HashMap permissionMap = new HashMap();
                principalDataMap.put("name", ((ArrayList)vt.get(i)).get(2));
                principalDataMap.put("permission", permissionMap);
                HashMap principalMap = new HashMap();
                principalMap.put(((ArrayList)vt.get(i)).get(1), principalDataMap);
                scopeMap.put(((ArrayList)vt.get(i)).get(0), principalMap);
            } else
            {
                HashMap principalMap = (HashMap)scopeMap.get(((ArrayList)vt.get(i)).get(0));
                if(!principalMap.containsKey(((ArrayList)vt.get(i)).get(1)))
                {
                    tableData.add(vt.get(i));
                    HashMap principalDataMap = new HashMap();
                    HashMap permissionMap = new HashMap();
                    principalDataMap.put("name", ((ArrayList)vt.get(i)).get(2));
                    principalDataMap.put("permission", permissionMap);
                    principalMap = (HashMap)scopeMap.get(((ArrayList)vt.get(i)).get(0));
                    principalMap.put(((ArrayList)vt.get(i)).get(1), principalDataMap);
                }
            }

        principalTable.changeTableData();
        principalTable.getTable().setRowSelectionInterval(tableData.size() - 1, tableData.size() - 1);
    }

    public void closeEvent()
    {
        saveButton.removeActionListener(listenerCommon);
        refreshButton.removeActionListener(listenerCommon);
        addButton.removeActionListener(listenerCommon);
        removeButton.removeActionListener(listenerCommon);
        principalTable.getTable().removeMouseListener(listenerCommon);
        for(int i = 0; i < statusNames.length; i++)
        {
            aArrayCheckBox[i].removeItemListener(listenerCommon);
            dArrayCheckBox[i].removeItemListener(listenerCommon);
        }

        removeWindowListener(listenerCommon);
        dispose();
    }

    void saveButton_actionPerformed(ActionEvent e)
    {
        if(Utils.isNullString(targetClassOuid))
            return;
        if(aclList == null)
            aclList = new ArrayList();
        ArrayList newAclList = new ArrayList();
        DOSChangeable dosAcl = null;
        ArrayList aObj = null;
        for(Iterator iKey = tableData.iterator(); iKey.hasNext();)
        {
            aObj = (ArrayList)iKey.next();
            HashMap principalMap = (HashMap)scopeMap.get((String)aObj.get(0));
            HashMap principalDataMap = (HashMap)principalMap.get((String)aObj.get(1));
            HashMap permissionMap = (HashMap)principalDataMap.get("permission");
            HashMap permissionDataMap = null;
            for(Iterator mapKey = permissionMap.keySet().iterator(); mapKey.hasNext();)
            {
                String pName = (String)mapKey.next();
                permissionDataMap = (HashMap)permissionMap.get(pName);
                if(((String)permissionDataMap.get("allow")).equals("T") || ((String)permissionDataMap.get("deny")).equals("T") || ((String)permissionDataMap.get("isgrantable")).equals("T"))
                {
                    dosAcl = new DOSChangeable();
                    dosAcl.put("targetclassouid", targetClassOuid);
                    dosAcl.put("targetobjectouid", targetProcessOuid);
                    dosAcl.put("permission", pName);
                    dosAcl.put("permissionscope", (String)aObj.get(0));
                    dosAcl.put("userobject", (String)aObj.get(1));
                    dosAcl.put("permissiontype", ((String)permissionDataMap.get("allow")).equals("T") ? "A" : "D");
                    dosAcl.put("isgrantable", "F");
                    dosAcl.put("description", (String)permissionDataMap.get("description"));
                    newAclList.add(dosAcl);
                }
            }

        }

        DOSChangeable acl1 = null;
        DOSChangeable acl2 = null;
        Iterator iKey1 = null;
        Iterator iKey2 = null;
        ArrayList createList = new ArrayList();
        ArrayList updateList = new ArrayList();
        ArrayList removeList = null;
        for(iKey1 = newAclList.iterator(); iKey1.hasNext();)
        {
            boolean isCreate = true;
            acl1 = (DOSChangeable)iKey1.next();
            for(iKey2 = aclList.iterator(); iKey2.hasNext();)
            {
                acl2 = (DOSChangeable)iKey2.next();
                if(acl1.get("targetclassouid").equals(acl2.get("targetclassouid")) && acl1.get("targetobjectouid").equals(acl2.get("targetobjectouid")) && acl1.get("permission").equals(acl2.get("permission")) && acl1.get("permissionscope").equals(acl2.get("permissionscope")) && acl1.get("userobject").equals(acl2.get("userobject")))
                {
                    acl1.setOriginalValueMap(acl2.getValueMap());
                    aclList.remove(aclList.indexOf(acl2));
                    isCreate = false;
                    break;
                }
            }

            if(isCreate)
                createList.add(acl1);
            else
                updateList.add(acl1);
        }

        removeList = (ArrayList)aclList.clone();
        try
        {
            for(Iterator iKey = createList.iterator(); iKey.hasNext(); acl.createACL(dosAcl))
                dosAcl = (DOSChangeable)iKey.next();

            for(Iterator iKey = updateList.iterator(); iKey.hasNext();)
            {
                dosAcl = (DOSChangeable)iKey.next();
                if(dosAcl.isChanged())
                    acl.setACL(dosAcl);
            }

            for(Iterator iKey = removeList.iterator(); iKey.hasNext(); acl.removeACL((String)dosAcl.get("targetclassouid"), (String)dosAcl.get("targetobjectouid"), (String)dosAcl.get("permission"), (String)dosAcl.get("permissionscope"), (String)dosAcl.get("userobject")))
                dosAcl = (DOSChangeable)iKey.next();

            refreshButton_actionPerformed(null);
            String tmpStr = "";
            tmpStr = DynaMOAD.getMSRString("WRD_0011", "save", 3);
            Object args[] = {
                tmpStr
            };
            tmpStr = DynaMOAD.getMSRString("INF_0001", "", 0);
            MessageFormat form = new MessageFormat(tmpStr);
            String messageStr = form.format(((Object) (args)));
            String titleStr = DynaMOAD.getMSRString("WRD_0004", "information", 3);
            JOptionPane.showMessageDialog(this, messageStr, titleStr, 1);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        finally
        {
            createList.clear();
            updateList.clear();
            removeList.clear();
        }
        return;
    }

    void refreshButton_actionPerformed(ActionEvent e)
    {
        tableSelectionChanged = true;
        getACLData();
        principalTable.changeTableData();
        initializeCheckBoxes();
        tableSelectionChanged = false;
    }

    void getACLData()
    {
        try
        {
            if(aclList != null)
            {
                aclList.clear();
                tableData.clear();
            }
            if(grantablePermission == null)
                grantablePermission = new ArrayList();
            else
                grantablePermission.clear();
            if(scopeMap == null)
                scopeMap = new HashMap();
            else
                scopeMap.clear();
            aclList = acl.retrievePrivateACL4Grant(targetClassOuid, targetProcessOuid, LogIn.userID, new Boolean(LogIn.isAdmin));
            if(aclList != null)
            {
                if(LogIn.isAdmin)
                {
                    for(int i = 0; i < statusNames.length; i++)
                        grantablePermission.add(statusKeys[i]);

                }
                HashMap principalMap = null;
                HashMap principalDataMap = null;
                HashMap permissionMap = null;
                HashMap permissionDataMap = null;
                Iterator iKey = aclList.iterator();
                ArrayList aObj = null;
                while(iKey.hasNext()) 
                {
                    DOSChangeable aclData = (DOSChangeable)iKey.next();
                    if(!LogIn.isAdmin && ((String)aclData.get("permissionscope")).equals("USER") && ((String)aclData.get("userobject")).equals(LogIn.userID))
                        grantablePermission.add((String)aclData.get("permission"));
                    String id = null;
                    String name = null;
                    if(((String)aclData.get("permissionscope")).equals("USER"))
                    {
                        HashMap usrObj = aus.getUser((String)aclData.get("userobject"));
                        id = (String)aclData.get("userobject");
                        name = (String)usrObj.get("name");
                    } else
                    {
                        id = (String)aclData.get("userobject");
                        name = (String)aclData.get("userobject");
                    }
                    if(scopeMap.containsKey((String)aclData.get("permissionscope")))
                    {
                        principalMap = (HashMap)scopeMap.get((String)aclData.get("permissionscope"));
                        if(principalMap.containsKey(id))
                        {
                            principalDataMap = (HashMap)principalMap.get(id);
                            permissionMap = (HashMap)principalDataMap.get("permission");
                            permissionDataMap = new HashMap();
                            permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                            permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                            permissionDataMap.put("isgrantable", "F");
                            permissionDataMap.put("description", (String)aclData.get("description"));
                            permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                        } else
                        {
                            permissionDataMap = new HashMap();
                            permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                            permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                            permissionDataMap.put("isgrantable", "F");
                            permissionDataMap.put("description", (String)aclData.get("description"));
                            permissionMap = new HashMap();
                            permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                            principalDataMap = new HashMap();
                            principalDataMap.put("name", name);
                            principalDataMap.put("permission", permissionMap);
                            principalMap.put(id, principalDataMap);
                            aObj = new ArrayList();
                            aObj.add((String)aclData.get("permissionscope"));
                            aObj.add(id);
                            aObj.add(name);
                            tableData.add(aObj);
                        }
                    } else
                    {
                        permissionDataMap = new HashMap();
                        permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                        permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                        permissionDataMap.put("isgrantable", "F");
                        permissionDataMap.put("description", (String)aclData.get("description"));
                        permissionMap = new HashMap();
                        permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                        principalDataMap = new HashMap();
                        principalDataMap.put("name", name);
                        principalDataMap.put("permission", permissionMap);
                        principalMap = new HashMap();
                        principalMap.put(id, principalDataMap);
                        scopeMap.put((String)aclData.get("permissionscope"), principalMap);
                        aObj = new ArrayList();
                        aObj.add((String)aclData.get("permissionscope"));
                        aObj.add(id);
                        aObj.add(name);
                        tableData.add(aObj);
                    }
                }
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void initializeCheckBoxes()
    {
        for(int i = 0; i < statusNames.length; i++)
        {
            aArrayCheckBox[i].setSelected(false);
            dArrayCheckBox[i].setSelected(false);
            if(i == 0 || grantablePermission.contains(statusKeys[i]))
            {
                arrayLabel[i].setEnabled(true);
                aArrayCheckBox[i].setEnabled(true);
                dArrayCheckBox[i].setEnabled(true);
            } else
            {
                arrayLabel[i].setEnabled(false);
                aArrayCheckBox[i].setEnabled(false);
                dArrayCheckBox[i].setEnabled(false);
            }
        }

    }

    void addButton_actionPerformed(ActionEvent e)
    {
        PrincipalSelect principalSelect = new PrincipalSelect(this, this, DynaMOAD.getMSRString("WRD_0105", "Group Selection", 3), false);
        principalSelect.show();
    }

    void removeButton_actionPerformed(ActionEvent e)
    {
        tableSelectionChanged = true;
        int row = tableData.indexOf(selectedRowData);
        if(row > -1)
        {
            tableData.remove(row);
            String scope = (String)selectedRowData.get(0);
            String id = (String)selectedRowData.get(1);
            HashMap principalMap = (HashMap)scopeMap.get(scope);
            principalMap.remove(id);
        }
        principalTable.changeTableData();
        principalTable.getTable().clearSelection();
        initializeCheckBoxes();
        tableSelectionChanged = false;
    }

    void principalTable_valueChanged(PrincipalTable table, ListSelectionEvent e)
    {
        tableSelectionChanged = true;
        if(!e.getValueIsAdjusting())
        {
            ListSelectionModel listSelectionModel = table.getTable().getSelectionModel();
            if(listSelectionModel.getMinSelectionIndex() > -1 && listSelectionModel.getMaxSelectionIndex() > -1)
            {
                int selRow = table.getTable().getSelectedRow();
                String ouidRow = table.getSelectedOuidRow(selRow);
                String scope = null;
                String id = null;
                if(ouidRow != null)
                {
                    table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                    selectedRowData = (ArrayList)table.getList().get((new Integer(ouidRow)).intValue());
                    scope = selectedRowData.get(0).toString();
                    id = selectedRowData.get(1).toString();
                } else
                {
                    selectedRowData = (ArrayList)table.getList().get(selRow);
                    scope = selectedRowData.get(0).toString();
                    id = selectedRowData.get(1).toString();
                }
                HashMap principalMap = (HashMap)scopeMap.get(scope);
                HashMap principalDataMap = (HashMap)principalMap.get(id);
                HashMap permissionMap = (HashMap)principalDataMap.get("permission");
                for(int i = 0; i < statusNames.length; i++)
                    if(permissionMap.containsKey(statusKeys[i]))
                    {
                        HashMap permissionDataMap = (HashMap)permissionMap.get(statusKeys[i]);
                        if(permissionDataMap != null)
                        {
                            String allow = (String)permissionDataMap.get("allow");
                            String deny = (String)permissionDataMap.get("deny");
                            if(!Utils.isNullString(allow) && allow.equals("T"))
                                aArrayCheckBox[i].setSelected(true);
                            else
                            if(!Utils.isNullString(deny) && deny.equals("T"))
                                dArrayCheckBox[i].setSelected(true);
                        }
                    } else
                    {
                        aArrayCheckBox[i].setSelected(false);
                        dArrayCheckBox[i].setSelected(false);
                    }

            } else
            {
                initializeCheckBoxes();
            }
        }
        tableSelectionChanged = false;
    }

    Cursor handCursor;
    private final int FRAME_XSIZE = 450;
    private final int FRAME_YSIZE = 550;
    private final int FRAME_XLOC = 200;
    private final int FRAME_YLOC = 100;
    private final String PER_CREATE = "CREATE";
    private final String PER_SCAN = "SCAN";
    private String STA_FULL;
    private String STA_AP1;
    private String STA_AP2;
    private String STA_CKI;
    private String STA_CKO;
    private String STA_CRT;
    private String STA_OBS;
    private String STA_REJ;
    private String STA_RLS;
    private String STA_RV1;
    private String STA_RV2;
    private String STA_WIP;
    private String statusNames[];
    private String statusKeys[];
    private String NDS_CODE;
    private ArrayList aclList;
    private HashMap scopeMap;
    private ArrayList selectedRowData;
    private String targetClassOuid;
    private String targetProcessOuid;
    private String title;
    private boolean tableSelectionChanged;
    private ArrayList grantablePermission;
    private ArrayList tableData;
    private ArrayList columns;
    private JToolBar buttonToolBar;
    private JButton saveButton;
    private JButton refreshButton;
    private JLabel titleLabel;
    private JPanel principalPanel;
    private JScrollPane tableScrPane;
    private PrincipalTable principalTable;
    private JButton addButton;
    private JButton removeButton;
    private JPanel permissionPanel;
    private JPanel namePanel;
    private JPanel boxPanel;
    private JLabel nameLabel;
    private JLabel arrayLabel[];
    private JLabel allowLabel;
    private JCheckBox aArrayCheckBox[];
    private JLabel denyLabel;
    private JCheckBox dArrayCheckBox[];
    private UIManagement newUI;
    private ACL acl;
    private AUS aus;
    private DOS dos;
    private NDS nds;
    private WFM wfm;
    private ListenerCommon listenerCommon;







}