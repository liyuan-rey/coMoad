// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PrincipalSelect.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.service.*;
import dyna.uic.Table;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, AuthoritySetting, ProcessAuthoritySetting, 
//            UserObjectSelect

public class PrincipalSelect extends JDialog
{
    class ListenerCommon extends WindowAdapter
        implements ActionListener, MouseListener, ItemListener
    {

        public void windowOpened(WindowEvent e)
        {
            clearButton_actionPerformed(null);
        }

        public void windowClosing(WindowEvent e)
        {
            closeEvent();
        }

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            Object source = e.getSource();
            if(command.equals("Select"))
                okButton_actionPerformed(e);
            else
            if(command.equals("Clear"))
                clearButton_actionPerformed(e);
        }

        public void mouseClicked(MouseEvent e)
        {
            Object source = e.getSource();
            if(source.equals(groupScrPane))
                groupScrPane_mouseClicked(e);
            else
            if(source.equals(userScrPane))
                userScrPane_mouseClicked(e);
            else
            if(source.equals(roleScrPane))
                roleScrPane_mouseClicked(e);
            else
            if(!source.equals(groupTable.getTable()) && !source.equals(userTable.getTable()))
                source.equals(roleTable.getTable());
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
            Object source = e.getItemSelectable();
            int state = e.getStateChange();
            int j = 0;
            if(source == othersChkBox && state == 1)
                ownerChkBox.setSelected(false);
            else
            if(source == ownerChkBox && state == 1)
                othersChkBox.setSelected(false);
        }

        ListenerCommon()
        {
        }
    }


    public PrincipalSelect(Frame frame, Object parent, String title, boolean modal)
    {
        super(frame, title, modal);
        handCursor = new Cursor(12);
        buttonPanel = null;
        okButton = null;
        clearButton = null;
        principalPanel = null;
        groupScrPane = null;
        userScrPane = null;
        roleScrPane = null;
        groupTable = null;
        userTable = null;
        roleTable = null;
        groupDataVct = null;
        userDataVct = null;
        roleDataVct = null;
        column = null;
        groupLabel = null;
        userLabel = null;
        roleLabel = null;
        othersChkBox = null;
        ownerChkBox = null;
        parentWindow = null;
        authorityTable = null;
        authViewData = null;
        newUI = null;
        acl = null;
        aus = null;
        dos = null;
        listenerCommon = null;
        parentWindow = parent;
        newUI = DynaMOAD.newUI;
        acl = DynaMOAD.acl;
        aus = DynaMOAD.aus;
        dos = DynaMOAD.dos;
        listenerCommon = new ListenerCommon();
        setLocation(200, 100);
        setSize(400, 300);
        addWindowListener(listenerCommon);
        initialize();
    }

    private void initialize()
    {
        okButton = new JButton(DynaMOAD.getMSRString("WRD_0018", "select", 3), new ImageIcon(getClass().getResource("/icons/SelectAllRow.gif")));
        okButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        okButton.setActionCommand("Select");
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.addActionListener(listenerCommon);
        clearButton = new JButton(DynaMOAD.getMSRString("WRD_0016", "clear", 3), new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "clear", 3));
        clearButton.setActionCommand("Clear");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.addActionListener(listenerCommon);
        buttonPanel = new JPanel();
        buttonPanel.setBackground(UIManagement.panelBackGround);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        othersChkBox = new JCheckBox(DynaMOAD.getMSRString("WRD_0113", "Others Permit", 3));
        othersChkBox.setBorder(UIManagement.borderTextBoxNotEditable);
        othersChkBox.setBackground(UIManagement.panelBackGround);
        othersChkBox.addItemListener(listenerCommon);
        ownerChkBox = new JCheckBox(DynaMOAD.getMSRString("WRD_0114", "Owner Only", 3));
        ownerChkBox.setBorder(UIManagement.borderTextBoxEditable);
        ownerChkBox.setBackground(UIManagement.panelBackGround);
        ownerChkBox.addItemListener(listenerCommon);
        column = new ArrayList();
        groupTableDefine();
        groupScrPane = UIFactory.createStrippedScrollPane(null);
        groupScrPane.setViewportView(groupTable.getTable());
        groupScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        groupScrPane.setPreferredSize(new Dimension(100, 170));
        groupScrPane.addMouseListener(listenerCommon);
        groupScrPane.setCursor(handCursor);
        roleTableDefine();
        roleScrPane = UIFactory.createStrippedScrollPane(null);
        roleScrPane.setViewportView(roleTable.getTable());
        roleScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        roleScrPane.setPreferredSize(new Dimension(100, 170));
        roleScrPane.addMouseListener(listenerCommon);
        roleScrPane.setCursor(handCursor);
        userTableDefine();
        userScrPane = UIFactory.createStrippedScrollPane(null);
        userScrPane.setViewportView(userTable.getTable());
        userScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        userScrPane.setPreferredSize(new Dimension(100, 170));
        userScrPane.addMouseListener(listenerCommon);
        userScrPane.setCursor(handCursor);
        authViewData = new ArrayList();
        groupLabel = new JLabel(DynaMOAD.getMSRString("WRD_0111", "Group", 3), new ImageIcon(getClass().getResource("/icons/Group.gif")), 2);
        roleLabel = new JLabel(DynaMOAD.getMSRString("WRD_0112", "Role", 3), new ImageIcon(getClass().getResource("/icons/Role.gif")), 2);
        userLabel = new JLabel(DynaMOAD.getMSRString("WRD_0025", "User", 3), new ImageIcon(getClass().getResource("/icons/User.gif")), 2);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 18;
        gridBagCon.fill = 0;
        gridBagCon.insets = new Insets(5, 5, 5, 0);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(othersChkBox, gridBagCon);
        gridBagCon.insets = new Insets(5, 5, 5, 0);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(ownerChkBox, gridBagCon);
        gridBagCon.fill = 1;
        gridBagCon.insets = new Insets(5, 5, 5, 0);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(groupLabel, gridBagCon);
        gridBagCon.insets = new Insets(5, 5, 5, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(groupScrPane, gridBagCon);
        gridBagCon.insets = new Insets(5, 5, 5, 0);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(roleLabel, gridBagCon);
        gridBagCon.insets = new Insets(5, 5, 5, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(roleScrPane, gridBagCon);
        gridBagCon.insets = new Insets(5, 5, 5, 5);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.ipady = 0;
        gridBag.setConstraints(userLabel, gridBagCon);
        gridBagCon.insets = new Insets(5, 5, 5, 5);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(userScrPane, gridBagCon);
        principalPanel = new JPanel();
        principalPanel.setBackground(UIManagement.panelBackGround);
        principalPanel.setLayout(gridBag);
        principalPanel.add(othersChkBox);
        principalPanel.add(ownerChkBox);
        principalPanel.add(groupLabel);
        principalPanel.add(groupScrPane);
        principalPanel.add(userLabel);
        principalPanel.add(userScrPane);
        principalPanel.add(roleLabel);
        principalPanel.add(roleScrPane);
        getContentPane().add(principalPanel, "North");
        getContentPane().add(buttonPanel, "Center");
    }

    public void groupTableDefine()
    {
        groupDataVct = new ArrayList();
        setTableHeader(0);
        groupTable = new Table(groupDataVct, (ArrayList)column.get(0), (ArrayList)column.get(1), 1);
        groupTable.setColumnSequence(new int[] {
            0, 2
        });
        groupTable.setIndexColumn(0);
        groupTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        groupTable.getTable().setCursor(handCursor);
        groupTable.getTable().addMouseListener(listenerCommon);
    }

    public void roleTableDefine()
    {
        roleDataVct = new ArrayList();
        setTableHeader(1);
        roleTable = new Table(roleDataVct, (ArrayList)column.get(0), (ArrayList)column.get(1), 1);
        roleTable.setColumnSequence(new int[] {
            0, 2
        });
        roleTable.setIndexColumn(0);
        roleTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        roleTable.getTable().setCursor(handCursor);
        roleTable.getTable().addMouseListener(listenerCommon);
    }

    public void userTableDefine()
    {
        userDataVct = new ArrayList();
        setTableHeader(2);
        userTable = new Table(userDataVct, (ArrayList)column.get(0), (ArrayList)column.get(1), 1);
        userTable.setColumnSequence(new int[] {
            0, 8, 2
        });
        userTable.setIndexColumn(0);
        userTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        userTable.getTable().setCursor(handCursor);
        userTable.getTable().addMouseListener(listenerCommon);
    }

    public ArrayList setTableHeader(int opt)
    {
        ArrayList colname = new ArrayList();
        ArrayList colwidth = new ArrayList();
        column.clear();
        switch(opt)
        {
        case 0: // '\0'
            colname.add(DynaMOAD.getMSRString("name", "Name", 3));
            colname.add(DynaMOAD.getMSRString("description", "Desc", 3));
            column.trimToSize();
            colwidth.add(new Integer(70));
            colwidth.add(new Integer(70));
            colwidth.trimToSize();
            break;

        case 1: // '\001'
            colname.add(DynaMOAD.getMSRString("name", "Name", 3));
            colname.add(DynaMOAD.getMSRString("description", "Desc", 3));
            column.trimToSize();
            colwidth.add(new Integer(70));
            colwidth.add(new Integer(70));
            colwidth.trimToSize();
            break;

        case 2: // '\002'
            colname.add("ID");
            colname.add(DynaMOAD.getMSRString("name", "Name", 3));
            colname.add(DynaMOAD.getMSRString("description", "Desc", 3));
            column.trimToSize();
            colwidth.add(new Integer(70));
            colwidth.add(new Integer(70));
            colwidth.add(new Integer(70));
            colwidth.trimToSize();
            break;
        }
        column.add(colname);
        column.add(colwidth);
        column.trimToSize();
        return column;
    }

    public void setGroupData(ArrayList vt)
    {
        groupDataVct.clear();
        for(int i = 0; i < vt.size(); i++)
            groupDataVct.add(vt.get(i));

        groupTable.changeTableData();
    }

    public void setRoleData(ArrayList vt)
    {
        roleDataVct.clear();
        for(int i = 0; i < vt.size(); i++)
            roleDataVct.add(vt.get(i));

        roleTable.changeTableData();
    }

    public void setUserData(ArrayList vt)
    {
        userDataVct.clear();
        for(int i = 0; i < vt.size(); i++)
            userDataVct.add(vt.get(i));

        userTable.changeTableData();
    }

    public void closeEvent()
    {
        okButton.removeActionListener(listenerCommon);
        clearButton.removeActionListener(listenerCommon);
        groupScrPane.removeMouseListener(listenerCommon);
        groupTable.getTable().removeMouseListener(listenerCommon);
        roleScrPane.removeMouseListener(listenerCommon);
        roleTable.getTable().removeMouseListener(listenerCommon);
        userScrPane.removeMouseListener(listenerCommon);
        userTable.getTable().removeMouseListener(listenerCommon);
        removeWindowListener(listenerCommon);
        dispose();
    }

    void okButton_actionPerformed(ActionEvent e)
    {
        ArrayList newPrincipalList = new ArrayList();
        ArrayList tmpList = null;
        if(othersChkBox.isSelected())
        {
            tmpList = new ArrayList();
            tmpList.add("OTHERS");
            tmpList.add("Others");
            tmpList.add("Others");
            newPrincipalList.add(tmpList);
        }
        if(ownerChkBox.isSelected())
        {
            tmpList = new ArrayList();
            tmpList.add("OWNER");
            tmpList.add("Owner");
            tmpList.add("Owner");
            newPrincipalList.add(tmpList);
        }
        ArrayList aObj = null;
        for(Iterator iKey = groupDataVct.iterator(); iKey.hasNext(); newPrincipalList.add(tmpList))
        {
            aObj = (ArrayList)iKey.next();
            tmpList = new ArrayList();
            tmpList.add("GROUP");
            tmpList.add(aObj.get(0));
            tmpList.add(aObj.get(0));
        }

        for(Iterator iKey = roleDataVct.iterator(); iKey.hasNext(); newPrincipalList.add(tmpList))
        {
            aObj = (ArrayList)iKey.next();
            tmpList = new ArrayList();
            tmpList.add("ROLE");
            tmpList.add(aObj.get(0));
            tmpList.add(aObj.get(0));
        }

        for(Iterator iKey = userDataVct.iterator(); iKey.hasNext(); newPrincipalList.add(tmpList))
        {
            aObj = (ArrayList)iKey.next();
            tmpList = new ArrayList();
            tmpList.add("USER");
            tmpList.add(aObj.get(0));
            tmpList.add(aObj.get(8));
        }

        if(parentWindow instanceof AuthoritySetting)
            ((AuthoritySetting)parentWindow).setTableData(newPrincipalList);
        else
        if(parentWindow instanceof ProcessAuthoritySetting)
            ((ProcessAuthoritySetting)parentWindow).setTableData(newPrincipalList);
        newPrincipalList.clear();
        closeEvent();
    }

    void clearButton_actionPerformed(ActionEvent e)
    {
        othersChkBox.setSelected(false);
        ownerChkBox.setSelected(false);
        groupDataVct.clear();
        roleDataVct.clear();
        userDataVct.clear();
        groupTable.changeTableData();
        roleTable.changeTableData();
        userTable.changeTableData();
    }

    void groupScrPane_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && groupScrPane.isEnabled())
            getGroupSelect();
    }

    void groupTable_mouseClicked(MouseEvent e)
    {
        int selRow = -1;
        int selCol = -1;
        if(e.getClickCount() == 1 && groupTable.getTable().getSelectedRowCount() > 0 && (groupTable.getTable().getSelectedColumn() == 1 || groupTable.getTable().getSelectedColumn() == 2))
        {
            int row = groupTable.getTable().getSelectedRow();
            selRow = (new Integer(groupTable.getSelectedOuidRow(row))).intValue();
            Boolean selBool = (Boolean)((ArrayList)groupDataVct.get(selRow)).get(5);
            ((ArrayList)groupDataVct.get(selRow)).set(5, new Boolean(!selBool.booleanValue()));
            ((ArrayList)groupDataVct.get(selRow)).set(6, new Boolean(selBool.booleanValue()));
            groupTable.changeTableData();
        } else
        if(e.getClickCount() == 2)
            getGroupSelect();
    }

    void getGroupSelect()
    {
        UserObjectSelect groupSelect = new UserObjectSelect(this, this, DynaMOAD.getMSRString("WRD_0105", "Group Selection", 3), false, 0);
        groupSelect.selectDataFun(groupDataVct);
        groupSelect.makeSelectedTable();
        groupSelect.show();
    }

    void roleScrPane_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && roleScrPane.isEnabled())
            getRoleSelect();
    }

    void roleTable_mouseClicked(MouseEvent e)
    {
        int selRow = -1;
        int selCol = -1;
        if(e.getClickCount() == 1 && roleTable.getTable().getSelectedRowCount() > 0 && (roleTable.getTable().getSelectedColumn() == 1 || roleTable.getTable().getSelectedColumn() == 2))
        {
            int row = roleTable.getTable().getSelectedRow();
            selRow = (new Integer(roleTable.getSelectedOuidRow(row))).intValue();
            Boolean selBool = (Boolean)((ArrayList)roleDataVct.get(selRow)).get(5);
            ((ArrayList)roleDataVct.get(selRow)).set(5, new Boolean(!selBool.booleanValue()));
            ((ArrayList)roleDataVct.get(selRow)).set(6, new Boolean(selBool.booleanValue()));
            roleTable.changeTableData();
        } else
        if(e.getClickCount() == 2)
            getRoleSelect();
    }

    void getRoleSelect()
    {
        UserObjectSelect groupSelect = new UserObjectSelect(this, this, DynaMOAD.getMSRString("WRD_0106", "Role Selection", 3), false, 1);
        groupSelect.selectDataFun(roleDataVct);
        groupSelect.makeSelectedTable();
        groupSelect.show();
    }

    void userScrPane_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && userScrPane.isEnabled())
            getUserSelect();
    }

    void userTable_mouseClicked(MouseEvent e)
    {
        int selRow = -1;
        int selCol = -1;
        if(e.getClickCount() == 1 && userTable.getTable().getSelectedRowCount() > 0 && (userTable.getTable().getSelectedColumn() == 2 || userTable.getTable().getSelectedColumn() == 3))
        {
            int row = userTable.getTable().getSelectedRow();
            selRow = (new Integer(userTable.getSelectedOuidRow(row))).intValue();
            Boolean selBool = (Boolean)((ArrayList)userDataVct.get(selRow)).get(13);
            ((ArrayList)userDataVct.get(selRow)).set(13, new Boolean(!selBool.booleanValue()));
            ((ArrayList)userDataVct.get(selRow)).set(14, new Boolean(selBool.booleanValue()));
            userTable.changeTableData();
        } else
        if(e.getClickCount() == 2)
            getUserSelect();
    }

    void getUserSelect()
    {
        UserObjectSelect groupSelect = new UserObjectSelect(this, this, DynaMOAD.getMSRString("WRD_0107", "User Selection", 3), false, 2);
        groupSelect.selectDataFun(userDataVct);
        groupSelect.makeSelectedTable();
        groupSelect.show();
    }

    Cursor handCursor;
    private final int FRAME_XSIZE = 400;
    private final int FRAME_YSIZE = 300;
    private final int FRAME_XLOC = 200;
    private final int FRAME_YLOC = 100;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton clearButton;
    private JPanel principalPanel;
    private JScrollPane groupScrPane;
    private JScrollPane userScrPane;
    private JScrollPane roleScrPane;
    private Table groupTable;
    private Table userTable;
    private Table roleTable;
    private ArrayList groupDataVct;
    private ArrayList userDataVct;
    private ArrayList roleDataVct;
    private ArrayList column;
    private JLabel groupLabel;
    private JLabel userLabel;
    private JLabel roleLabel;
    private JCheckBox othersChkBox;
    private JCheckBox ownerChkBox;
    private Object parentWindow;
    private Table authorityTable;
    private ArrayList authViewData;
    private UIManagement newUI;
    private ACL acl;
    private AUS aus;
    private DOS dos;
    private ListenerCommon listenerCommon;








}