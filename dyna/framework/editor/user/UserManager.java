// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserManager.java

package dyna.framework.editor.user;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookMode;
import com.jgoodies.plaf.FontUtils;
import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.client.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.editor.user:
//            GroupInformation, UserInformation, RoleInformation, UserInsertDialog, 
//            SmallSearchForUserManager

public class UserManager extends JFrame
    implements ActionListener, WindowListener, MouseListener, ChangeListener
{

    public UserManager()
    {
        groupInformation = null;
        userInformation = null;
        roleInformation = null;
        mainSplitPane = null;
        groupsTableScrPane = null;
        usersTableScrPane = null;
        rolesTableScrPane = null;
        tableTabbedPane = null;
        userTableScrPane = null;
        usersTable = null;
        groupsTable = null;
        rolesTable = null;
        usersColumn = null;
        usersData = null;
        rolesColumn = null;
        rolesData = null;
        groupsColumn = null;
        groupsData = null;
        groupButtonPanel = null;
        groupSearchButton = null;
        groupInsertButton = null;
        groupDeleteButton = null;
        callObject = null;
        groupDataList = null;
        groupColumnName = null;
        groupColumnWidth = null;
        userDataList = null;
        userColumnName = null;
        userColumnWidth = null;
        groupId = "";
        userId = "";
        roleId = "";
        groupIdInUserTree = "";
        dosRootNode = null;
        nodeChildCount = new DOSChangeable();
        usedMessage = "\uC774\uBBF8 \uC0AC\uC6A9\uC911\uC774\uBBC0\uB85C \uC0AD\uC81C\uD560 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4.";
        deleteMessage = "\uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?";
        handCursor = new Cursor(12);
        newUI = DynaMOAD.newUI;
        STATUS_CRT = "CRT";
        STATUS_OBS = "OBS";
        STATUS_CKI = "CKI";
        STATUS_CKO = "CKO";
        STATUS_RLS = "RLS";
        STATUS_WIP = "WIP";
        STATUS_RV1 = "RV1";
        STATUS_RV2 = "RV2";
        STATUS_AP1 = "AP1";
        STATUS_AP2 = "AP2";
        STATUS_REJ = "REJ";
        setFont(new Font("dialog", 0, 11));
        initialize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 25);
        setLocation(0, 0);
        setVisible(true);
        addWindowListener(this);
    }

    public UserManager(Object obj)
    {
        groupInformation = null;
        userInformation = null;
        roleInformation = null;
        mainSplitPane = null;
        groupsTableScrPane = null;
        usersTableScrPane = null;
        rolesTableScrPane = null;
        tableTabbedPane = null;
        userTableScrPane = null;
        usersTable = null;
        groupsTable = null;
        rolesTable = null;
        usersColumn = null;
        usersData = null;
        rolesColumn = null;
        rolesData = null;
        groupsColumn = null;
        groupsData = null;
        groupButtonPanel = null;
        groupSearchButton = null;
        groupInsertButton = null;
        groupDeleteButton = null;
        callObject = null;
        groupDataList = null;
        groupColumnName = null;
        groupColumnWidth = null;
        userDataList = null;
        userColumnName = null;
        userColumnWidth = null;
        groupId = "";
        userId = "";
        roleId = "";
        groupIdInUserTree = "";
        dosRootNode = null;
        nodeChildCount = new DOSChangeable();
        usedMessage = "\uC774\uBBF8 \uC0AC\uC6A9\uC911\uC774\uBBC0\uB85C \uC0AD\uC81C\uD560 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4.";
        deleteMessage = "\uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?";
        handCursor = new Cursor(12);
        newUI = DynaMOAD.newUI;
        STATUS_CRT = "CRT";
        STATUS_OBS = "OBS";
        STATUS_CKI = "CKI";
        STATUS_CKO = "CKO";
        STATUS_RLS = "RLS";
        STATUS_WIP = "WIP";
        STATUS_RV1 = "RV1";
        STATUS_RV2 = "RV2";
        STATUS_AP1 = "AP1";
        STATUS_AP2 = "AP2";
        STATUS_REJ = "REJ";
        aus = DynaMOAD.aus;
        callObject = obj;
        setFont(new Font("dialog", 0, 11));
        initialize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 25);
        setLocation(0, 0);
        setVisible(true);
        addWindowListener(this);
    }

    public static void main(String args[])
    {
        try
        {
            dfw = new Client();
            aus = (AUS)dfw.getServiceInstance("DF30AUS1");
            LookUtils.setLookAndTheme(new PlasticXPLookAndFeel(), new DynaTheme());
            DynaMOAD.newUI = new UIManagement();
            FontUtils.initFontDefaults(UIManager.getLookAndFeelDefaults(), new Font("dialog", 0, 12), new Font("dialog", 1, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 11), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12));
            ClearLookManager.setPolicy("com.jgoodies.clearlook.DefaultClearLookPolicy");
            ClearLookManager.setMode(ClearLookMode.valueOf("ON"));
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        new UserManager();
    }

    public void makeTable()
    {
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("User Manager - DynaMOAD");
        dosRootNode = new DOSChangeable();
        dosRootNode.put("ouid", "Group");
        dosRootNode.put("name", "Group");
        dosRootNode.put("description", "Group");
        dosRootNode.put("object.type", "group");
        setTableHeader();
        groupsTableDefine();
        groupsTableScrPane = UIFactory.createStrippedScrollPane(null);
        groupsTableScrPane.getViewport().add(groupsTable.getTable());
        groupsTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        groupsTableScrPane.addMouseListener(this);
        usersTableDefine();
        usersTableScrPane = UIFactory.createStrippedScrollPane(null);
        usersTableScrPane.getViewport().add(usersTable.getTable());
        usersTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        usersTableScrPane.addMouseListener(this);
        rolesTableDefine();
        rolesTableScrPane = UIFactory.createStrippedScrollPane(null);
        rolesTableScrPane.getViewport().add(rolesTable.getTable());
        rolesTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        rolesTableScrPane.addMouseListener(this);
        tableTabbedPane = new JTabbedPane();
        tableTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        tableTabbedPane.setTabPlacement(3);
        tableTabbedPane.addTab("Group", new ImageIcon("icons/Group.gif"), groupsTableScrPane);
        tableTabbedPane.addTab("User", new ImageIcon("icons/User.gif"), usersTableScrPane);
        tableTabbedPane.addTab("Role", new ImageIcon("icons/Role.gif"), rolesTableScrPane);
        tableTabbedPane.addChangeListener(this);
        groupInformation = new GroupInformation(this, true);
        userInformation = new UserInformation(this, true);
        roleInformation = new RoleInformation(this, true);
        mainSplitPane = new JSplitPane();
        mainSplitPane.setLeftComponent(tableTabbedPane);
        mainSplitPane.setRightComponent(groupInformation);
        mainSplitPane.setDividerLocation(250);
        mainSplitPane.setOneTouchExpandable(true);
        groupSearchButton = new JButton();
        groupSearchButton.setText("Search");
        groupSearchButton.setActionCommand("Search");
        groupSearchButton.addActionListener(this);
        groupInsertButton = new JButton();
        groupInsertButton.setText("Add");
        groupInsertButton.setActionCommand("Add");
        groupInsertButton.addActionListener(this);
        groupInsertButton.setIcon(new ImageIcon("icons/add_att.gif"));
        groupInsertButton.setMargin(new Insets(0, 0, 0, 0));
        if(callObject != null && !isAdmin())
            groupInsertButton.setEnabled(false);
        groupDeleteButton = new JButton();
        groupDeleteButton.setText("Remove");
        groupDeleteButton.setActionCommand("Remove");
        groupDeleteButton.addActionListener(this);
        groupButtonPanel = new JPanel();
        groupButtonPanel.setBorder(BorderFactory.createEtchedBorder());
        groupButtonPanel.setLayout(new BoxLayout(groupButtonPanel, 0));
        groupButtonPanel.add(Box.createHorizontalStrut(5));
        groupButtonPanel.add(groupInsertButton);
        groupButtonPanel.add(Box.createHorizontalGlue());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(groupButtonPanel, "North");
        getContentPane().add(mainSplitPane, "Center");
        popupGroup = new JPopupMenu();
        menuInfoGroup = new JMenuItem();
        menuInfoGroup.setText("Information");
        menuInfoGroup.setIcon(new ImageIcon("icons/Information.gif"));
        menuInfoGroup.setActionCommand("Group Information");
        menuInfoGroup.addActionListener(this);
        menuAddGroup = new JMenuItem();
        menuAddGroup.setText("Add");
        menuAddGroup.setIcon(new ImageIcon("icons/Paste.gif"));
        menuAddGroup.setActionCommand("Add");
        menuAddGroup.addActionListener(this);
        menuDeleteGroup = new JMenuItem();
        menuDeleteGroup.setText("Delete");
        menuDeleteGroup.setIcon(new ImageIcon("icons/Delete.gif"));
        menuDeleteGroup.setActionCommand("Group Delete");
        menuDeleteGroup.addActionListener(this);
        menuRefreshGroup = new JMenuItem();
        menuRefreshGroup.setText("Refresh");
        menuRefreshGroup.setIcon(new ImageIcon("icons/Refresh.gif"));
        menuRefreshGroup.setActionCommand("Group Refresh");
        menuRefreshGroup.addActionListener(this);
        menuLinkUser = new JMenuItem();
        menuLinkUser.setText("Link User");
        menuLinkUser.setIcon(new ImageIcon("icons/Link.gif"));
        menuLinkUser.setActionCommand("Link User");
        menuLinkUser.addActionListener(this);
        popupGroup.add(menuInfoGroup);
        popupGroup.add(menuAddGroup);
        popupGroup.add(menuDeleteGroup);
        popupGroup.add(menuRefreshGroup);
        popup = new JPopupMenu();
        menuInfo = new JMenuItem();
        menuInfo.setText("Information");
        menuInfo.setIcon(new ImageIcon("icons/Information.gif"));
        menuInfo.setActionCommand("User Information");
        menuInfo.addActionListener(this);
        menuAdd = new JMenuItem();
        menuAdd.setText("Add");
        menuAdd.setIcon(new ImageIcon("icons/Paste.gif"));
        menuAdd.setActionCommand("Add");
        menuAdd.addActionListener(this);
        menuDelete = new JMenuItem();
        menuDelete.setText("Delete");
        menuDelete.setIcon(new ImageIcon("icons/Delete.gif"));
        menuDelete.setActionCommand("Delete");
        menuDelete.addActionListener(this);
        menuLinkRole = new JMenuItem();
        menuLinkRole.setText("Link Role");
        menuLinkRole.setIcon(new ImageIcon("icons/Link.gif"));
        menuLinkRole.setActionCommand("Link Role");
        menuLinkRole.addActionListener(this);
        menuUnlinkGroup = new JMenuItem();
        menuUnlinkGroup.setText("Unlink Group");
        menuUnlinkGroup.setIcon(new ImageIcon("icons/Unlink.gif"));
        menuUnlinkGroup.setActionCommand("Unlink Group");
        menuUnlinkGroup.addActionListener(this);
        menuRefresh = new JMenuItem();
        menuRefresh.setText("Refresh");
        menuRefresh.setIcon(new ImageIcon("icons/Refresh.gif"));
        menuRefresh.setActionCommand("User Refresh");
        menuRefresh.addActionListener(this);
        popup.add(menuInfo);
        popup.add(menuAdd);
        popup.add(menuDelete);
        popup.add(menuRefresh);
        popupRole = new JPopupMenu();
        menuInfoRole = new JMenuItem();
        menuInfoRole.setText("Information");
        menuInfoRole.setIcon(new ImageIcon("icons/Information.gif"));
        menuInfoRole.setActionCommand("Role Information");
        menuInfoRole.addActionListener(this);
        menuAddRole = new JMenuItem();
        menuAddRole.setText("Add");
        menuAddRole.setIcon(new ImageIcon("icons/Paste.gif"));
        menuAddRole.setActionCommand("Add");
        menuAddRole.addActionListener(this);
        menuDeleteRole = new JMenuItem();
        menuDeleteRole.setText("Delete");
        menuDeleteRole.setIcon(new ImageIcon("icons/Delete.gif"));
        menuDeleteRole.setActionCommand("Role Delete");
        menuDeleteRole.addActionListener(this);
        menuRefreshRole = new JMenuItem();
        menuRefreshRole.setText("Refresh");
        menuRefreshRole.setIcon(new ImageIcon("icons/Refresh.gif"));
        menuRefreshRole.setActionCommand("Role Refresh");
        menuRefreshRole.addActionListener(this);
        popupRole.add(menuInfoRole);
        popupRole.add(menuAddRole);
        popupRole.add(menuDeleteRole);
        popupRole.add(menuRefreshRole);
        if(callObject != null && !isAdmin())
        {
            menuAddGroup.setEnabled(false);
            menuDeleteGroup.setEnabled(false);
            menuLinkUser.setEnabled(false);
            menuAdd.setEnabled(false);
            menuUnlinkGroup.setEnabled(false);
            menuAddRole.setEnabled(false);
            menuDeleteRole.setEnabled(false);
        }
        tableTabbedPane.setSelectedIndex(1);
    }

    public void usersTableDefine()
    {
        usersTable = new Table(usersData, (ArrayList)usersColumn.get(0), (ArrayList)usersColumn.get(1), 0);
        usersTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        usersTable.setIndexColumn(0);
        usersTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        usersTable.getTable().setCursor(handCursor);
        usersTable.getTable().addMouseListener(this);
    }

    public void rolesTableDefine()
    {
        rolesTable = new Table(rolesData, (ArrayList)rolesColumn.get(0), (ArrayList)rolesColumn.get(1), 0);
        rolesTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        rolesTable.setIndexColumn(0);
        rolesTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        rolesTable.getTable().setCursor(handCursor);
        rolesTable.getTable().addMouseListener(this);
    }

    public void groupsTableDefine()
    {
        groupsTable = new Table(groupsData, (ArrayList)groupsColumn.get(0), (ArrayList)groupsColumn.get(1), 0);
        groupsTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        groupsTable.setIndexColumn(0);
        groupsTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        groupsTable.getTable().setCursor(handCursor);
        groupsTable.getTable().addMouseListener(this);
    }

    public void setTableHeader()
    {
        usersColumn = new ArrayList();
        groupsColumn = new ArrayList();
        rolesColumn = new ArrayList();
        usersData = new ArrayList();
        groupsData = new ArrayList();
        rolesData = new ArrayList();
        ArrayList colname = new ArrayList();
        ArrayList colwidth = new ArrayList();
        colname.add("ID");
        colname.add("Name");
        colname.add("Status");
        colname.add("Description");
        colname.trimToSize();
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.trimToSize();
        usersColumn.add(colname);
        usersColumn.add(colwidth);
        usersColumn.trimToSize();
        groupsColumn.add(colname);
        groupsColumn.add(colwidth);
        groupsColumn.trimToSize();
        rolesColumn.add(colname);
        rolesColumn.add(colwidth);
        rolesColumn.trimToSize();
    }

    public void refreshUsersData()
    {
        ArrayList tmp = new ArrayList();
        usersData.clear();
        usersTable.changeTableData();
        boolean listup = false;
        try
        {
            ArrayList usersObj = aus.listUser();
            if(usersObj == null)
                return;
            if(callObject == null || isAdmin())
                listup = true;
            for(int i = 0; i < usersObj.size(); i++)
            {
                HashMap userinfo = aus.getUser((String)usersObj.get(i));
                if(userinfo != null)
                    if(!listup && ((String)usersObj.get(i)).equals(LogIn.userID))
                    {
                        userinfo.put("id", (String)usersObj.get(i));
                        tmp.add(userinfo.get("id"));
                        tmp.add(userinfo.get("name"));
                        tmp.add(userinfo.get("status"));
                        tmp.add(userinfo.get("description"));
                        usersData.add(tmp.clone());
                        tmp.clear();
                    } else
                    if(listup)
                    {
                        userinfo.put("id", (String)usersObj.get(i));
                        tmp.add(userinfo.get("id"));
                        tmp.add(userinfo.get("name"));
                        tmp.add(userinfo.get("status"));
                        tmp.add(userinfo.get("description"));
                        usersData.add(tmp.clone());
                        tmp.clear();
                    }
            }

            usersObj.clear();
            tmp.clear();
            usersTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void refreshGroupsData()
    {
        System.out.println("refreshGroupData");
        ArrayList tmp = new ArrayList();
        groupsData.clear();
        groupsTable.changeTableData();
        boolean listup = false;
        if(callObject == null || isAdmin())
            listup = true;
        if(!listup)
            return;
        try
        {
            ArrayList groupsObj = aus.listGroup();
            if(groupsObj == null)
                return;
            for(int i = 0; i < groupsObj.size(); i++)
            {
                HashMap groupinfo = aus.getGroup((String)groupsObj.get(i));
                groupinfo.put("id", (String)groupsObj.get(i));
                System.out.println((String)groupsObj.get(i));
                tmp.add(groupinfo.get("id"));
                tmp.add(groupinfo.get("name"));
                tmp.add(groupinfo.get("status"));
                tmp.add(groupinfo.get("description"));
                groupsData.add(tmp.clone());
                tmp.clear();
            }

            groupsObj.clear();
            tmp.clear();
            groupsTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void refreshRolesData()
    {
        System.out.println("refreshRoleData");
        ArrayList tmp = new ArrayList();
        rolesData.clear();
        rolesTable.changeTableData();
        boolean listup = false;
        if(callObject == null || isAdmin())
            listup = true;
        if(!listup)
            return;
        try
        {
            ArrayList rolesObj = aus.listRole();
            if(rolesObj == null)
                return;
            for(int i = 0; i < rolesObj.size(); i++)
            {
                HashMap roleinfo = aus.getRole((String)rolesObj.get(i));
                if(roleinfo != null)
                {
                    roleinfo.put("id", (String)rolesObj.get(i));
                    tmp.add(roleinfo.get("id"));
                    tmp.add(roleinfo.get("name"));
                    tmp.add(roleinfo.get("status"));
                    tmp.add(roleinfo.get("description"));
                    rolesData.add(tmp.clone());
                    tmp.clear();
                }
            }

            rolesObj.clear();
            tmp.clear();
            rolesTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setUserResultData(ArrayList resultData)
    {
        userDataList.clear();
        ArrayList tmpList = new ArrayList();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                String tmpStr = (String)resultData.get(i);
                tmpList.add(tmpStr);
                tmpList.add("");
                userDataList.add(tmpList.clone());
                tmpList.clear();
            }

        }
        usersTable.changeTableData();
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Add"))
        {
            if(tableTabbedPane.getSelectedComponent().equals(groupsTableScrPane))
            {
                GroupInformation groupInformation = new GroupInformation(this, false);
                UserInsertDialog groupFrame = new UserInsertDialog(this, groupInformation, null);
                groupFrame.setVisible(true);
                refreshGroupsData();
            } else
            if(tableTabbedPane.getSelectedComponent().equals(usersTableScrPane))
            {
                UserInformation userInformation = new UserInformation(this, false);
                UserInsertDialog userFrame = new UserInsertDialog(this, userInformation, null);
                userFrame.setVisible(true);
                refreshUsersData();
            } else
            if(tableTabbedPane.getSelectedComponent().equals(rolesTableScrPane))
            {
                RoleInformation roleInformation = new RoleInformation(this, false);
                UserInsertDialog roleFrame = new UserInsertDialog(this, roleInformation, null);
                roleFrame.setVisible(true);
                refreshRolesData();
            }
        } else
        if(command.equals("Delete"))
        {
            System.out.println("User Delete");
            if(usersTable.getTable().getSelectedRowCount() != 1)
                return;
            ArrayList sdata = new ArrayList();
            sdata = (ArrayList)usersData.get(usersTable.getTable().getSelectedRow());
            userId = (String)sdata.get(0);
            if(!Utils.isNullString(userId))
                try
                {
                    ArrayList groupsObj = aus.listGroupOfUser(userId);
                    ArrayList rolesObj = aus.listRoleOfUser(userId);
                    if(groupsObj != null && groupsObj.size() > 0)
                    {
                        JOptionPane.showMessageDialog(this, usedMessage, "ERROR", 0);
                        return;
                    }
                    if(rolesObj != null && rolesObj.size() > 0)
                    {
                        JOptionPane.showMessageDialog(this, usedMessage, "ERROR", 0);
                        return;
                    }
                    Object option[] = {
                        "Yes", "No"
                    };
                    int res = JOptionPane.showOptionDialog(this, deleteMessage, "QUESTION", 0, 3, new ImageIcon("icons/Question32.gif"), option, option[1]);
                    if(res != 0)
                        return;
                    boolean isDelete = aus.removeUser(userId);
                    if(isDelete)
                        refreshUsersData();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("Group Delete"))
        {
            System.out.println("Group Delete");
            if(groupsTable.getTable().getSelectedRowCount() != 1)
                return;
            ArrayList sdata = new ArrayList();
            sdata = (ArrayList)groupsData.get(groupsTable.getTable().getSelectedRow());
            groupId = (String)sdata.get(0);
            if(!Utils.isNullString(groupId))
                try
                {
                    ArrayList usersObj = aus.listMembersOfGroup(groupId);
                    if(usersObj != null && usersObj.size() > 0)
                    {
                        JOptionPane.showMessageDialog(this, usedMessage, "ERROR", 0);
                        return;
                    }
                    Object option[] = {
                        "Yes", "No"
                    };
                    int res = JOptionPane.showOptionDialog(this, deleteMessage, "QUESTION", 0, 3, new ImageIcon("icons/Question32.gif"), option, option[1]);
                    if(res != 0)
                        return;
                    boolean isDelete = aus.removeGroup(groupId);
                    if(isDelete)
                        refreshGroupsData();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("Role Delete"))
        {
            System.out.println("Group Delete");
            if(rolesTable.getTable().getSelectedRowCount() != 1)
                return;
            ArrayList sdata = new ArrayList();
            sdata = (ArrayList)rolesData.get(rolesTable.getTable().getSelectedRow());
            roleId = (String)sdata.get(0);
            if(!Utils.isNullString(roleId))
                try
                {
                    ArrayList usersObj = aus.listUsersOfRole(roleId);
                    if(usersObj != null && usersObj.size() > 0)
                    {
                        JOptionPane.showMessageDialog(this, usedMessage, "ERROR", 0);
                        return;
                    }
                    Object option[] = {
                        "Yes", "No"
                    };
                    int res = JOptionPane.showOptionDialog(this, deleteMessage, "QUESTION", 0, 3, new ImageIcon("icons/Question32.gif"), option, option[1]);
                    if(res != 0)
                        return;
                    boolean isDelete = aus.removeRole(roleId);
                    if(isDelete)
                        refreshRolesData();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("User Information"))
        {
            if(!Utils.isNullString(userId))
                try
                {
                    HashMap userInfo = aus.getUser(userId);
                    userInfo.put("id", userId);
                    this.userInformation.setData(userInfo);
                    this.userInformation.setVisible(true);
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("User Refresh"))
            refreshUsersData();
        else
        if(command.equals("Group Information"))
        {
            if(!Utils.isNullString(groupId))
                try
                {
                    HashMap groupInfo = aus.getGroup(groupId);
                    groupInfo.put("id", groupId);
                    this.groupInformation.setData(groupInfo);
                    this.groupInformation.setVisible(true);
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("Group Refresh"))
            refreshGroupsData();
        else
        if(command.equals("Role Refresh"))
            refreshRolesData();
        else
        if(command.equals("Link User"))
        {
            SmallSearchForUserManager smallSearch = new SmallSearchForUserManager(this, "group", "user", groupId);
            smallSearch.setVisible(true);
        } else
        if(command.equals("Link Role"))
        {
            SmallSearchForUserManager smallSearch = new SmallSearchForUserManager(this, "user", "role", userId);
            smallSearch.setVisible(true);
        } else
        {
            command.equals("Unlink Group");
        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        dispose();
        removeWindowListener(this);
        if(callObject == null)
            System.exit(0);
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

    public void windowClosed(WindowEvent windowEvent)
    {
        dispose();
        removeWindowListener(this);
        if(callObject == null)
            System.exit(0);
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void mousePressed(MouseEvent evt)
    {
        if(evt.getSource().equals(usersTable.getTable()) || evt.getSource().equals(usersTableScrPane))
        {
            if(SwingUtilities.isRightMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown())
                popup.show(evt.getComponent(), evt.getX(), evt.getY());
            try
            {
                if(usersTable.getTable().getSelectedRowCount() != 1)
                    return;
                ArrayList sdata = new ArrayList();
                sdata = (ArrayList)usersData.get(usersTable.getTable().getSelectedRow());
                HashMap info = aus.getUser((String)sdata.get(0));
                info.put("id", (String)sdata.get(0));
                userInformation.setData(info);
                if(callObject == null || isAdmin())
                    userInformation.setMenuEnable(true);
                userInformation.setVisible(true);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(evt.getSource().equals(groupsTable.getTable()) || evt.getSource().equals(groupsTableScrPane))
        {
            if(SwingUtilities.isRightMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown())
                popupGroup.show(evt.getComponent(), evt.getX(), evt.getY());
            try
            {
                if(groupsTable.getTable().getSelectedRowCount() != 1)
                    return;
                ArrayList sdata = new ArrayList();
                sdata = (ArrayList)groupsData.get(groupsTable.getTable().getSelectedRow());
                HashMap info = aus.getGroup((String)sdata.get(0));
                info.put("id", (String)sdata.get(0));
                groupInformation.setData(info);
                if(callObject == null || isAdmin())
                    groupInformation.setMenuEnable(true);
                groupInformation.setVisible(true);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(evt.getSource().equals(rolesTable.getTable()) || evt.getSource().equals(rolesTableScrPane))
        {
            if(SwingUtilities.isRightMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown())
                popupRole.show(evt.getComponent(), evt.getX(), evt.getY());
            try
            {
                if(rolesTable.getTable().getSelectedRowCount() != 1)
                    return;
                ArrayList sdata = new ArrayList();
                sdata = (ArrayList)rolesData.get(rolesTable.getTable().getSelectedRow());
                HashMap info = aus.getRole((String)sdata.get(0));
                info.put("id", (String)sdata.get(0));
                roleInformation.setData(info);
                if(callObject == null || isAdmin())
                    roleInformation.setMenuEnable(true);
                roleInformation.setVisible(true);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent evt)
    {
        if(evt.getSource().equals(usersTable))
        {
            if(evt.getClickCount() == 1 && evt.isPopupTrigger())
            {
                Dimension popupSize = popup.getSize();
                int dividerLocation = mainSplitPane.getDividerLocation();
                Dimension frameSize = getSize();
                Point point = SwingUtilities.convertPoint(evt.getComponent(), evt.getX(), evt.getY(), this);
                int x = evt.getX();
                int y = evt.getY();
                if(popupSize.width + point.x >= dividerLocation)
                    x -= popupSize.width;
                if(popupSize.height + point.y >= frameSize.height)
                    y -= popupSize.height;
                popup.show(evt.getComponent(), x, y);
            }
        } else
        if(evt.getSource().equals(groupsTable))
        {
            if(evt.getClickCount() == 1 && evt.isPopupTrigger())
            {
                Dimension popupSize = popupGroup.getSize();
                int dividerLocation = mainSplitPane.getDividerLocation();
                Dimension frameSize = getSize();
                Point point = SwingUtilities.convertPoint(evt.getComponent(), evt.getX(), evt.getY(), this);
                int x = evt.getX();
                int y = evt.getY();
                if(popupSize.width + point.x >= dividerLocation)
                    x -= popupSize.width;
                if(popupSize.height + point.y >= frameSize.height)
                    y -= popupSize.height;
                popupGroup.show(evt.getComponent(), x, y);
            }
        } else
        if(evt.getSource().equals(rolesTable) && evt.getClickCount() == 1 && evt.isPopupTrigger())
        {
            Dimension popupSize = popupRole.getSize();
            int dividerLocation = mainSplitPane.getDividerLocation();
            Dimension frameSize = getSize();
            Point point = SwingUtilities.convertPoint(evt.getComponent(), evt.getX(), evt.getY(), this);
            int x = evt.getX();
            int y = evt.getY();
            if(popupSize.width + point.x >= dividerLocation)
                x -= popupSize.width;
            if(popupSize.height + point.y >= frameSize.height)
                y -= popupSize.height;
            popupRole.show(evt.getComponent(), x, y);
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void stateChanged(ChangeEvent changeEvent)
    {
        if(tableTabbedPane.getSelectedIndex() == 0)
        {
            mainSplitPane.setRightComponent(groupInformation);
            refreshGroupsData();
        } else
        if(tableTabbedPane.getSelectedIndex() == 1)
        {
            mainSplitPane.setRightComponent(userInformation);
            refreshUsersData();
        } else
        if(tableTabbedPane.getSelectedIndex() == 2)
        {
            mainSplitPane.setRightComponent(roleInformation);
            refreshRolesData();
        }
    }

    public boolean isAdmin()
    {
        boolean isadmin = false;
        try
        {
            isadmin = aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR");
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return isadmin;
    }

    public static Client dfw = null;
    public static AUS aus = null;
    private GroupInformation groupInformation;
    private UserInformation userInformation;
    private RoleInformation roleInformation;
    private JSplitPane mainSplitPane;
    private JScrollPane groupsTableScrPane;
    private JScrollPane usersTableScrPane;
    private JScrollPane rolesTableScrPane;
    private JTabbedPane tableTabbedPane;
    private JScrollPane userTableScrPane;
    private Table usersTable;
    private Table groupsTable;
    private Table rolesTable;
    private ArrayList usersColumn;
    private ArrayList usersData;
    private ArrayList rolesColumn;
    private ArrayList rolesData;
    private ArrayList groupsColumn;
    private ArrayList groupsData;
    private JPanel groupButtonPanel;
    private JButton groupSearchButton;
    private JButton groupInsertButton;
    private JButton groupDeleteButton;
    public Object callObject;
    private JPopupMenu popupGroup;
    private JMenuItem menuInfoGroup;
    private JMenuItem menuAddGroup;
    private JMenuItem menuDeleteGroup;
    private JMenuItem menuRefreshGroup;
    private JMenuItem menuLinkUser;
    private JMenuItem menuUnlinkUser;
    private JPopupMenu popup;
    private JMenuItem menuInfo;
    private JMenuItem menuAdd;
    private JMenuItem menuDelete;
    private JMenuItem menuRefresh;
    private JMenuItem menuLinkRole;
    private JMenuItem menuUnlinkGroup;
    private JPopupMenu popupRole;
    private JMenuItem menuInfoRole;
    private JMenuItem menuAddRole;
    private JMenuItem menuDeleteRole;
    private JMenuItem menuRefreshRole;
    private ArrayList groupDataList;
    private ArrayList groupColumnName;
    private ArrayList groupColumnWidth;
    private ArrayList userDataList;
    private ArrayList userColumnName;
    private ArrayList userColumnWidth;
    private String groupId;
    public String userId;
    private String roleId;
    private String groupIdInUserTree;
    private DOSChangeable dosRootNode;
    private DOSChangeable nodeChildCount;
    private String usedMessage;
    private String deleteMessage;
    Cursor handCursor;
    private UIManagement newUI;
    private String STATUS_CRT;
    private String STATUS_OBS;
    private String STATUS_CKI;
    private String STATUS_CKO;
    private String STATUS_RLS;
    private String STATUS_WIP;
    private String STATUS_RV1;
    private String STATUS_RV2;
    private String STATUS_AP1;
    private String STATUS_AP2;
    private String STATUS_REJ;

}
