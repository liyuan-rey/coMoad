// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AuthoritySetting.java

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

public class AuthoritySetting extends JFrame
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
                gArrayCheckBox[0].setEnabled(true);
                for(int i = 1; i < permissionNames.length; i++)
                    if(aArrayCheckBox[i].isEnabled())
                        aArrayCheckBox[i].setSelected(true);

            } else
            if(source == aArrayCheckBox[0] && state == 2)
            {
                gArrayCheckBox[0].setSelected(false);
                gArrayCheckBox[0].setEnabled(false);
            } else
            if(source == dArrayCheckBox[0] && state == 1)
            {
                aArrayCheckBox[0].setSelected(false);
                for(int i = 1; i < permissionNames.length; i++)
                    if(dArrayCheckBox[i].isEnabled())
                        dArrayCheckBox[i].setSelected(true);

            } else
            if(source == gArrayCheckBox[0] && state == 1)
            {
                for(int i = 1; i < permissionNames.length; i++)
                    if(gArrayCheckBox[i].isEnabled())
                        gArrayCheckBox[i].setSelected(true);

            } else
            {
                String scope = null;
                String id = null;
                HashMap principalMap = null;
                HashMap principalDataMap = null;
                HashMap permissionMap = null;
                HashMap permissionDataMap = null;
                for(int i = 1; i < permissionNames.length; i++)
                {
                    if(!tableSelectionChanged)
                    {
                        scope = (String)selectedRowData.get(0);
                        id = (String)selectedRowData.get(1);
                        principalMap = (HashMap)scopeMap.get(scope);
                        principalDataMap = (HashMap)principalMap.get(id);
                        permissionMap = (HashMap)principalDataMap.get("permission");
                        if(permissionMap.containsKey(arrayLabel[i].getText()))
                        {
                            permissionDataMap = (HashMap)permissionMap.get(arrayLabel[i].getText());
                        } else
                        {
                            permissionDataMap = new HashMap();
                            permissionDataMap.put("allow", "F");
                            permissionDataMap.put("deny", "F");
                            permissionDataMap.put("isgrantable", "F");
                            permissionDataMap.put("description", "");
                        }
                    }
                    if(source == aArrayCheckBox[i] && state == 1)
                    {
                        dArrayCheckBox[i].setSelected(false);
                        gArrayCheckBox[i].setEnabled(true);
                        for(j = 1; j < permissionNames.length; j++)
                            if(aArrayCheckBox[j].isEnabled() && !aArrayCheckBox[j].isSelected())
                                break;

                        if(j == permissionNames.length)
                            aArrayCheckBox[0].setSelected(true);
                        if(!tableSelectionChanged)
                        {
                            permissionDataMap.put("allow", "T");
                            permissionMap.put(arrayLabel[i].getText(), permissionDataMap);
                        }
                    } else
                    if(source == aArrayCheckBox[i] && state == 2)
                    {
                        aArrayCheckBox[0].setSelected(false);
                        gArrayCheckBox[i].setSelected(false);
                        gArrayCheckBox[i].setEnabled(false);
                        if(!tableSelectionChanged)
                        {
                            permissionDataMap.put("allow", "F");
                            permissionMap.put(arrayLabel[i].getText(), permissionDataMap);
                        }
                    } else
                    if(source == dArrayCheckBox[i] && state == 1)
                    {
                        aArrayCheckBox[i].setSelected(false);
                        for(j = 1; j < permissionNames.length; j++)
                            if(dArrayCheckBox[j].isEnabled() && !dArrayCheckBox[j].isSelected())
                                break;

                        if(j == permissionNames.length)
                            dArrayCheckBox[0].setSelected(true);
                        if(!tableSelectionChanged)
                        {
                            permissionDataMap.put("deny", "T");
                            permissionMap.put(arrayLabel[i].getText(), permissionDataMap);
                        }
                    } else
                    if(source == dArrayCheckBox[i] && state == 2)
                    {
                        dArrayCheckBox[0].setSelected(false);
                        if(!tableSelectionChanged)
                        {
                            permissionDataMap.put("deny", "F");
                            permissionMap.put(arrayLabel[i].getText(), permissionDataMap);
                        }
                    } else
                    if(source == gArrayCheckBox[i] && state == 1)
                    {
                        for(j = 1; j < permissionNames.length; j++)
                            if(gArrayCheckBox[j].isEnabled() && !gArrayCheckBox[j].isSelected())
                                break;

                        if(j == permissionNames.length)
                            gArrayCheckBox[0].setSelected(true);
                        if(!tableSelectionChanged)
                        {
                            permissionDataMap.put("isgrantable", "T");
                            permissionMap.put(arrayLabel[i].getText(), permissionDataMap);
                        }
                    } else
                    if(source == gArrayCheckBox[i] && state == 2)
                    {
                        gArrayCheckBox[0].setSelected(false);
                        if(!tableSelectionChanged)
                        {
                            permissionDataMap.put("isgrantable", "F");
                            permissionMap.put(arrayLabel[i].getText(), permissionDataMap);
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


    public AuthoritySetting(Object parent, String windowTitle, String tgtclsouid, String tgtobjouid)
    {
        super(windowTitle);
        handCursor = new Cursor(12);
        aclList = null;
        scopeMap = null;
        selectedRowData = null;
        targetClassOuid = null;
        targetObjectOuid = null;
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
        grantLabel = null;
        gArrayCheckBox = null;
        newUI = null;
        acl = null;
        aus = null;
        dos = null;
        listenerCommon = null;
        targetClassOuid = tgtclsouid;
        if(!Utils.isNullString(tgtobjouid))
            targetObjectOuid = tgtobjouid;
        else
            targetObjectOuid = "0";
        newUI = DynaMOAD.newUI;
        acl = DynaMOAD.acl;
        aus = DynaMOAD.aus;
        dos = DynaMOAD.dos;
        listenerCommon = new ListenerCommon();
        setLocation(200, 100);
        setSize(450, 500);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        addWindowListener(listenerCommon);
        try
        {
            DOSChangeable classInfo = dos.getClass(targetClassOuid);
            String tmpStr = (String)classInfo.get("title");
            if(!Utils.isNullString(tgtobjouid))
            {
                DOSChangeable getDC = dos.get(tgtobjouid);
                tmpStr = tmpStr + " [" + (String)getDC.get("md$number") + "]";
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
        principalPanel.setBorder(BorderFactory.createTitledBorder(UIManagement.borderPanel, DynaMOAD.getMSRString("WRD_0100", "Principal", 3)));
        principalPanel.add(tableScrPane);
        principalPanel.add(addButton);
        principalPanel.add(removeButton);
        arrayLabel = new JLabel[permissionNames.length];
        aArrayCheckBox = new JCheckBox[permissionNames.length];
        dArrayCheckBox = new JCheckBox[permissionNames.length];
        gArrayCheckBox = new JCheckBox[permissionNames.length];
        for(int i = 0; i < permissionNames.length; i++)
        {
            arrayLabel[i] = new JLabel(permissionNames[i]);
            aArrayCheckBox[i] = new JCheckBox();
            aArrayCheckBox[i].addItemListener(listenerCommon);
            dArrayCheckBox[i] = new JCheckBox();
            dArrayCheckBox[i].addItemListener(listenerCommon);
            gArrayCheckBox[i] = new JCheckBox();
            gArrayCheckBox[i].addItemListener(listenerCommon);
        }

        nameLabel = new JLabel("");
        namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        namePanel.setBackground(UIManagement.panelBackGround);
        namePanel.setLayout(new GridLayout(0, 1));
        namePanel.add(nameLabel);
        for(int i = 0; i < permissionNames.length; i++)
            namePanel.add(arrayLabel[i]);

        allowLabel = new JLabel("Allow", 2);
        denyLabel = new JLabel("Deny", 2);
        grantLabel = new JLabel("Grantable", 2);
        boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(0, 3));
        boxPanel.setBackground(UIManagement.panelBackGround);
        boxPanel.add(allowLabel);
        boxPanel.add(denyLabel);
        boxPanel.add(grantLabel);
        for(int i = 0; i < permissionNames.length; i++)
        {
            boxPanel.add(aArrayCheckBox[i]);
            boxPanel.add(dArrayCheckBox[i]);
            boxPanel.add(gArrayCheckBox[i]);
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
        for(int i = 0; i < permissionNames.length; i++)
        {
            aArrayCheckBox[i].removeItemListener(listenerCommon);
            dArrayCheckBox[i].removeItemListener(listenerCommon);
            gArrayCheckBox[i].removeItemListener(listenerCommon);
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
                    dosAcl.put("targetobjectouid", targetObjectOuid);
                    dosAcl.put("permission", pName);
                    dosAcl.put("permissionscope", (String)aObj.get(0));
                    dosAcl.put("userobject", (String)aObj.get(1));
                    dosAcl.put("permissiontype", ((String)permissionDataMap.get("allow")).equals("T") ? "A" : "D");
                    dosAcl.put("isgrantable", ((String)permissionDataMap.get("isgrantable")).equals("T") ? "T" : "F");
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
            aclList = acl.retrievePrivateACL4Grant(targetClassOuid, targetObjectOuid, LogIn.userID, new Boolean(LogIn.isAdmin));
            if(aclList != null)
            {
                if(LogIn.isAdmin)
                {
                    for(int i = 0; i < permissionNames.length; i++)
                        grantablePermission.add(permissionNames[i]);

                }
                HashMap principalMap = null;
                HashMap principalDataMap = null;
                HashMap permissionMap = null;
                HashMap permissionDataMap = null;
                Iterator iKey = aclList.iterator();
                ArrayList aObj = null;
                String id = null;
                String name = null;
                while(iKey.hasNext()) 
                {
                    DOSChangeable aclData = (DOSChangeable)iKey.next();
                    if(!LogIn.isAdmin && !grantablePermission.contains((String)aclData.get("permission")))
                        grantablePermission.add((String)aclData.get("permission"));
                    if(((String)aclData.get("permissionscope")).equals("GROUP"))
                    {
                        HashMap grpObj = aus.getGroup((String)aclData.get("userobject"));
                        if(grpObj == null)
                            continue;
                        id = (String)aclData.get("userobject");
                        name = (String)grpObj.get("description");
                    } else
                    if(((String)aclData.get("permissionscope")).equals("ROLE"))
                    {
                        HashMap rolObj = aus.getRole((String)aclData.get("userobject"));
                        if(rolObj == null)
                            continue;
                        id = (String)aclData.get("userobject");
                        name = (String)rolObj.get("description");
                    } else
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
                            permissionDataMap.put("isgrantable", ((String)aclData.get("isgrantable")).equals("T") ? "T" : "F");
                            permissionDataMap.put("description", (String)aclData.get("description"));
                            permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                        } else
                        {
                            permissionDataMap = new HashMap();
                            permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                            permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                            permissionDataMap.put("isgrantable", ((String)aclData.get("isgrantable")).equals("T") ? "T" : "F");
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
                        permissionDataMap.put("isgrantable", ((String)aclData.get("isgrantable")).equals("T") ? "T" : "F");
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
        for(int i = 0; i < permissionNames.length; i++)
        {
            aArrayCheckBox[i].setSelected(false);
            dArrayCheckBox[i].setSelected(false);
            gArrayCheckBox[i].setSelected(false);
            if(!Utils.isNullString(targetClassOuid) && !Utils.isNullString(targetObjectOuid) && !targetObjectOuid.equals("0") && (arrayLabel[i].getText().equals("CREATE") || arrayLabel[i].getText().equals("SCAN")))
            {
                arrayLabel[i].setEnabled(false);
                aArrayCheckBox[i].setEnabled(false);
                dArrayCheckBox[i].setEnabled(false);
            } else
            if(i == 0 || grantablePermission.contains(arrayLabel[i].getText()))
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
            gArrayCheckBox[i].setEnabled(false);
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
                for(int i = 0; i < permissionNames.length; i++)
                    if(permissionMap.containsKey(arrayLabel[i].getText()))
                    {
                        HashMap permissionDataMap = (HashMap)permissionMap.get(arrayLabel[i].getText());
                        if(permissionDataMap != null)
                        {
                            String allow = (String)permissionDataMap.get("allow");
                            String deny = (String)permissionDataMap.get("deny");
                            String grantable = (String)permissionDataMap.get("isgrantable");
                            if(!Utils.isNullString(allow) && allow.equals("T"))
                            {
                                aArrayCheckBox[i].setSelected(true);
                                if(!Utils.isNullString(grantable) && grantable.equals("T"))
                                    gArrayCheckBox[i].setSelected(true);
                                else
                                    gArrayCheckBox[i].setSelected(false);
                            } else
                            if(!Utils.isNullString(deny) && deny.equals("T"))
                                dArrayCheckBox[i].setSelected(true);
                        }
                    } else
                    {
                        aArrayCheckBox[i].setSelected(false);
                        dArrayCheckBox[i].setSelected(false);
                        gArrayCheckBox[i].setSelected(false);
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
    private final int FRAME_YSIZE = 500;
    private final int FRAME_XLOC = 200;
    private final int FRAME_YLOC = 100;
    private final String PER_FULL = "FULL PERMISSION";
    private final String PER_CREATE = "CREATE";
    private final String PER_UPDATE = "UPDATE";
    private final String PER_READ = "READ";
    private final String PER_DELETE = "DELETE";
    private final String PER_SCAN = "SCAN";
    private final String PER_PRINT = "PRINT";
    private final String PER_LOCK = "LOCK / UNLOCK";
    private final String PER_LINK = "LINK / UNLINK";
    private final String PER_CHECKIN = "CHECK-IN / CHECK-OUT";
    private final String permissionNames[] = {
        "FULL PERMISSION", "CREATE", "UPDATE", "READ", "DELETE", "SCAN", "PRINT", "LOCK / UNLOCK", "LINK / UNLINK", "CHECK-IN / CHECK-OUT"
    };
    private ArrayList aclList;
    private HashMap scopeMap;
    private ArrayList selectedRowData;
    private String targetClassOuid;
    private String targetObjectOuid;
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
    private JLabel grantLabel;
    private JCheckBox gArrayCheckBox[];
    private UIManagement newUI;
    private ACL acl;
    private AUS aus;
    private DOS dos;
    private ListenerCommon listenerCommon;








}