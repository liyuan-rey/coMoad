// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserInformation.java

package dyna.framework.editor.user;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.uic.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.editor.user:
//            UserManager, PasswordChangeDialog, SmallSearchForUserManager

public class UserInformation extends JPanel
    implements ActionListener, MouseListener
{
    class StatusDataLoader extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            return setStatusDefine();
        }

        StatusDataLoader()
        {
        }
    }


    public ArrayList setStatusDefine()
    {
        ArrayList LocStatus = new ArrayList();
        ArrayList cbStatus = new ArrayList();
        LocStatus.add("CRT");
        LocStatus.add("Created");
        cbStatus.add(LocStatus.clone());
        LocStatus.clear();
        LocStatus.add("OBS");
        LocStatus.add("Obsoleted");
        cbStatus.add(LocStatus.clone());
        LocStatus.clear();
        return cbStatus;
    }

    public UserInformation(UserManager parent, boolean mode)
    {
        newUI = DynaMOAD.newUI;
        aus = null;
        mainBorderLayout = null;
        mainPanel = null;
        idTextField = null;
        passwordTextField = null;
        nameTextField = null;
        descriptionTextField = null;
        primarygroupTextField = null;
        statusComboBox = null;
        statusModel = null;
        cdateTextField = null;
        lmdateTextField = null;
        lldateTextField = null;
        lfdateTextField = null;
        duedateTextField = null;
        failcountTextField = null;
        logincountTextField = null;
        dummyLabel = null;
        buttonPanel = null;
        okButton = null;
        pwChangeButton = null;
        userInfoMode = false;
        this.parent = null;
        splitPane = null;
        rolesScrollPane = null;
        rolesTable = null;
        rolesData = null;
        groupsScrollPane = null;
        groupsTable = null;
        groupsColumn = null;
        groupsData = null;
        authorityMessage = "\uAD8C\uD55C\uC774 \uC5C6\uC2B5\uB2C8\uB2E4.";
        deleteMessage = "\uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?";
        this.parent = parent;
        userInfoMode = mode;
        aus = UserManager.aus;
        initialize();
        setFieldEnabled(false);
    }

    public void initialize()
    {
        idTextField = new DynaTextField();
        idTextField.setMandatory(true);
        idTextField.setTitleText("ID");
        idTextField.setVisible(true);
        idTextField.setTitleWidth(150);
        idTextField.setTitleVisible(true);
        passwordTextField = new DynaTextField();
        passwordTextField.setMandatory(true);
        passwordTextField.setTitleText("Password");
        passwordTextField.setVisible(true);
        passwordTextField.setTitleWidth(150);
        passwordTextField.setTitleVisible(true);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(false);
        nameTextField.setTitleText("Name");
        nameTextField.setVisible(true);
        nameTextField.setTitleWidth(150);
        nameTextField.setTitleVisible(true);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Description");
        descriptionTextField.setVisible(true);
        descriptionTextField.setTitleWidth(150);
        descriptionTextField.setTitleVisible(true);
        primarygroupTextField = new DynaTextField();
        primarygroupTextField.setMandatory(false);
        primarygroupTextField.setTitleText("Primary group");
        primarygroupTextField.setTitleWidth(150);
        primarygroupTextField.setTitleVisible(true);
        StatusDataLoader statusDataLoader = new StatusDataLoader();
        statusModel = new DynaComboBoxModel(statusDataLoader);
        statusComboBox = new DynaComboBox();
        statusComboBox.setModel(statusModel);
        statusComboBox.setTitleText("Status");
        statusComboBox.setTitleWidth(150);
        statusComboBox.setTitleVisible(true);
        statusComboBox.setMandatory(false);
        statusComboBox = new DynaComboBox();
        statusComboBox.setMandatory(false);
        statusComboBox.setTitleText("Status");
        statusComboBox.setTitleWidth(150);
        statusComboBox.setTitleVisible(true);
        statusComboBox.setEditable(false);
        statusComboBox.setModel(statusModel);
        statusModel.enableDataLoad();
        cdateTextField = new DynaTextField();
        cdateTextField.setMandatory(false);
        cdateTextField.setTitleText("Created date");
        cdateTextField.setTitleWidth(150);
        cdateTextField.setTitleVisible(true);
        lmdateTextField = new DynaTextField();
        lmdateTextField.setMandatory(false);
        lmdateTextField.setTitleText("Last modified date");
        lmdateTextField.setTitleWidth(150);
        lmdateTextField.setTitleVisible(true);
        lldateTextField = new DynaTextField();
        lldateTextField.setMandatory(false);
        lldateTextField.setTitleText("Last logged-in date");
        lldateTextField.setTitleWidth(150);
        lldateTextField.setTitleVisible(true);
        lfdateTextField = new DynaTextField();
        lfdateTextField.setMandatory(false);
        lfdateTextField.setTitleText("Last failure date");
        lfdateTextField.setTitleWidth(150);
        lfdateTextField.setTitleVisible(true);
        duedateTextField = new DynaTextField();
        duedateTextField.setMandatory(false);
        duedateTextField.setTitleText("Due date");
        duedateTextField.setTitleWidth(150);
        duedateTextField.setTitleVisible(true);
        failcountTextField = new DynaTextField();
        failcountTextField.setMandatory(false);
        failcountTextField.setTitleText("Fail count");
        failcountTextField.setTitleWidth(150);
        failcountTextField.setTitleVisible(true);
        logincountTextField = new DynaTextField();
        logincountTextField.setMandatory(false);
        logincountTextField.setTitleText("Login count");
        logincountTextField.setTitleWidth(150);
        logincountTextField.setTitleVisible(true);
        splitPane = new JSplitPane(1);
        splitPane.setDividerSize(10);
        splitPane.setDividerLocation(350);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Roles/Groups"));
        setTableHeader();
        rolesTableDefine();
        rolesScrollPane = UIFactory.createStrippedScrollPane(null);
        rolesScrollPane.getViewport().add(rolesTable.getTable());
        rolesScrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        rolesScrollPane.setMinimumSize(new Dimension(200, 250));
        rolesScrollPane.setPreferredSize(new Dimension(200, 500));
        rolesScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Roles"));
        rolesScrollPane.addMouseListener(this);
        groupsTableDefine();
        groupsScrollPane = UIFactory.createStrippedScrollPane(null);
        groupsScrollPane.getViewport().add(groupsTable.getTable());
        groupsScrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        groupsScrollPane.setMinimumSize(new Dimension(200, 250));
        groupsScrollPane.setPreferredSize(new Dimension(200, 500));
        groupsScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Groups"));
        groupsScrollPane.addMouseListener(this);
        splitPane.add(rolesScrollPane);
        splitPane.add(groupsScrollPane);
        dummyLabel = new JLabel("");
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 2;
        gridBagCon.anchor = 12;
        mainPanel = new JPanel();
        mainPanel.setLayout(gridBag);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(idTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(nameTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(descriptionTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(primarygroupTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(statusComboBox, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(cdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 7;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(lmdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 8;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(lldateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 9;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(lfdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 10;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(duedateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 11;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(failcountTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 12;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(logincountTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 13;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 10;
        gridBag.setConstraints(splitPane, gridBagCon);
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(idTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(descriptionTextField);
        mainPanel.add(primarygroupTextField);
        mainPanel.add(statusComboBox);
        mainPanel.add(cdateTextField);
        mainPanel.add(lfdateTextField);
        mainPanel.add(lldateTextField);
        mainPanel.add(lmdateTextField);
        mainPanel.add(duedateTextField);
        mainPanel.add(failcountTextField);
        mainPanel.add(logincountTextField);
        mainPanel.add(splitPane);
        okButton = new JButton();
        okButton.setText("Save");
        okButton.setIcon(new ImageIcon("icons/Save.gif"));
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        okButton.setMargin(new Insets(0, 0, 0, 0));
        pwChangeButton = new JButton();
        pwChangeButton.setText("Password Change");
        pwChangeButton.setActionCommand("Password Change");
        pwChangeButton.addActionListener(this);
        pwChangeButton.setIcon(new ImageIcon("icons/Login.gif"));
        pwChangeButton.setMargin(new Insets(0, 0, 0, 0));
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(pwChangeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        mainBorderLayout = new BorderLayout();
        setLayout(mainBorderLayout);
        if(userInfoMode)
            add(buttonPanel, "North");
        add(mainPanel, "Center");
        popup1 = new JPopupMenu();
        menuLinkRole = new JMenuItem();
        menuLinkRole.setText("Link Role");
        menuLinkRole.setIcon(new ImageIcon("icons/Link.gif"));
        menuLinkRole.setActionCommand("Link Role");
        menuLinkRole.addActionListener(this);
        menuUnlinkRole = new JMenuItem();
        menuUnlinkRole.setText("Unlink Role");
        menuUnlinkRole.setIcon(new ImageIcon("icons/Unlink.gif"));
        menuUnlinkRole.setActionCommand("Unlink Role");
        menuUnlinkRole.addActionListener(this);
        popup1.add(menuLinkRole);
        popup1.add(menuUnlinkRole);
        popup2 = new JPopupMenu();
        menuLinkGroup = new JMenuItem();
        menuLinkGroup.setText("Link Group");
        menuLinkGroup.setIcon(new ImageIcon("icons/Link.gif"));
        menuLinkGroup.setActionCommand("Link Group");
        menuLinkGroup.addActionListener(this);
        menuUnlinkGroup = new JMenuItem();
        menuUnlinkGroup.setText("Unlink Group");
        menuUnlinkGroup.setIcon(new ImageIcon("icons/Unlink.gif"));
        menuUnlinkGroup.setActionCommand("Unlink Group");
        menuUnlinkGroup.addActionListener(this);
        popup2.add(menuLinkGroup);
        popup2.add(menuUnlinkGroup);
        if(!isAdmin())
        {
            menuLinkGroup.setEnabled(false);
            menuUnlinkGroup.setEnabled(false);
            menuLinkRole.setEnabled(false);
            menuUnlinkRole.setEnabled(false);
        }
    }

    public void setData(HashMap userInfo)
    {
        idTextField.setText((String)userInfo.get("id"));
        passwordTextField.setText((String)userInfo.get("password"));
        nameTextField.setText((String)userInfo.get("name"));
        descriptionTextField.setText((String)userInfo.get("description"));
        primarygroupTextField.setText((String)userInfo.get("primarygroup"));
        statusModel.setSelectedItemByOID((String)userInfo.get("status"));
        statusComboBox.updateUI();
        cdateTextField.setText((String)userInfo.get("cdate"));
        lfdateTextField.setText((String)userInfo.get("lfdate"));
        lldateTextField.setText((String)userInfo.get("lldate"));
        lmdateTextField.setText((String)userInfo.get("lmdate"));
        failcountTextField.setText((String)userInfo.get("failcount"));
        logincountTextField.setText((String)userInfo.get("logincount"));
        idTextField.setEnabled(false);
        setGroupTableData((String)userInfo.get("id"));
        setRoleTableData((String)userInfo.get("id"));
    }

    public void setVisibleScrollPane(boolean isvisible)
    {
        splitPane.setVisible(isvisible);
        if(!isvisible)
            mainPanel.add(dummyLabel);
    }

    public void setFieldEnabled(boolean mode)
    {
        if(!mode)
        {
            cdateTextField.setEditable(false);
            lfdateTextField.setEditable(false);
            lldateTextField.setEditable(false);
            lmdateTextField.setEditable(false);
            failcountTextField.setEditable(false);
            logincountTextField.setEditable(false);
        }
    }

    public void createUser()
    {
        HashMap userInfo = new HashMap();
        userInfo.put("userId", idTextField.getText());
        userInfo.put("password", idTextField.getText());
        userInfo.put("description", descriptionTextField.getText());
        userInfo.put("name", nameTextField.getText());
        userInfo.put("primarygroup", primarygroupTextField.getText());
        userInfo.put("status", (String)statusModel.getSelectedOID());
        userInfo.put("cdate", cdateTextField.getText());
        userInfo.put("lfdate", lfdateTextField.getText());
        userInfo.put("lmdate", lmdateTextField.getText());
        userInfo.put("lldate", lldateTextField.getText());
        userInfo.put("failcount", failcountTextField.getText());
        userInfo.put("logincount", logincountTextField.getText());
        try
        {
            if(!userInfoMode)
            {
                if(aus.isExistUser((String)userInfo.get("userId")))
                {
                    JOptionPane.showMessageDialog(this, "Already exsists.", "WARNING", 2);
                    return;
                }
                aus.createUser(userInfo);
                parent.refreshUsersData();
            } else
            {
                if((String)statusModel.getSelectedOID() != null)
                    aus.changeUserStatus(idTextField.getText(), (String)statusModel.getSelectedOID());
                aus.setUser(userInfo);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Ok"))
        {
            HashMap userInfo = new HashMap();
            userInfo.put("userId", idTextField.getText());
            userInfo.put("password", passwordTextField.getText());
            userInfo.put("description", descriptionTextField.getText());
            userInfo.put("name", nameTextField.getText());
            userInfo.put("primarygroup", primarygroupTextField.getText());
            userInfo.put("status", (String)statusModel.getSelectedOID());
            userInfo.put("cdate", cdateTextField.getText());
            userInfo.put("lfdate", lfdateTextField.getText());
            userInfo.put("lmdate", lmdateTextField.getText());
            userInfo.put("lldate", lldateTextField.getText());
            userInfo.put("failcount", failcountTextField.getText());
            userInfo.put("logincount", logincountTextField.getText());
            try
            {
                if(idTextField.getText().equals(LogIn.userID) || isAdmin())
                {
                    if(!userInfoMode)
                    {
                        aus.createUser(userInfo);
                    } else
                    {
                        if((String)statusModel.getSelectedOID() != null)
                            aus.changeUserStatus(idTextField.getText(), (String)statusModel.getSelectedOID());
                        aus.setUser(userInfo);
                    }
                    parent.refreshUsersData();
                } else
                {
                    JOptionPane.showMessageDialog(this, authorityMessage, "ERROR", 0);
                    return;
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(command.equals("Password Change"))
        {
            if(idTextField.getText().equals(LogIn.userID) || isAdmin())
            {
                PasswordChangeDialog pwDialog = new PasswordChangeDialog(parent, idTextField.getText());
                pwDialog.show();
            } else
            {
                JOptionPane.showMessageDialog(this, authorityMessage, "ERROR", 0);
                return;
            }
        } else
        if(command.equals("Link Role"))
        {
            SmallSearchForUserManager smallSearch = new SmallSearchForUserManager((UserManager)JOptionPane.getFrameForComponent(this), "user", "role", idTextField.getText());
            smallSearch.setVisible(true);
            setRoleTableData(idTextField.getText());
            rolesTable.getTable().clearSelection();
        } else
        if(command.equals("Unlink Role"))
        {
            System.out.println("unlink Role");
            if(rolesTable.getTable().getSelectedRowCount() < 1)
                return;
            Object option[] = {
                "Yes", "No"
            };
            int res = JOptionPane.showOptionDialog(this, deleteMessage, "QUESTION", 0, 3, new ImageIcon("icons/Question32.gif"), option, option[1]);
            if(res != 0)
                return;
            int rows[] = rolesTable.getTable().getSelectedRows();
            try
            {
                for(int i = 0; i < rows.length; i++)
                    aus.unlinkRoleFromUser((String)((ArrayList)rolesData.get(rows[i])).get(0), idTextField.getText());

                setRoleTableData(idTextField.getText());
                rolesTable.getTable().clearSelection();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(command.equals("Link Group"))
        {
            SmallSearchForUserManager smallSearch = new SmallSearchForUserManager((UserManager)JOptionPane.getFrameForComponent(this), "user", "group", idTextField.getText());
            smallSearch.setVisible(true);
            setGroupTableData(idTextField.getText());
            groupsTable.getTable().clearSelection();
        } else
        if(command.equals("Unlink Group"))
        {
            System.out.println("unlink Group");
            if(groupsTable.getTable().getSelectedRowCount() < 1)
                return;
            Object option[] = {
                "Yes", "No"
            };
            int res = JOptionPane.showOptionDialog(this, deleteMessage, "QUESTION", 0, 3, new ImageIcon("icons/Question32.gif"), option, option[1]);
            if(res != 0)
                return;
            int rows[] = groupsTable.getTable().getSelectedRows();
            try
            {
                for(int i = 0; i < rows.length; i++)
                    aus.unlinkUserFromGroup(idTextField.getText(), (String)((ArrayList)groupsData.get(rows[i])).get(0));

                setGroupTableData(idTextField.getText());
                groupsTable.getTable().clearSelection();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        }
    }

    public void groupsTableDefine()
    {
        groupsTable = new Table(groupsData, (ArrayList)groupsColumn.get(0), (ArrayList)groupsColumn.get(1), 1);
        groupsTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        groupsTable.setIndexColumn(0);
        groupsTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        groupsTable.getTable().addMouseListener(this);
    }

    public void setTableHeader()
    {
        groupsColumn = new ArrayList();
        groupsData = new ArrayList();
        rolesData = new ArrayList();
        ArrayList colname = new ArrayList();
        ArrayList colwidth = new ArrayList();
        colname.add("ID");
        colname.add("Description");
        colname.add("Status");
        colname.add("Create Date");
        colname.add("Modified Date");
        colname.trimToSize();
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.trimToSize();
        groupsColumn.add(colname);
        groupsColumn.add(colwidth);
        groupsColumn.trimToSize();
    }

    public void setGroupTableData(String uid)
    {
        ArrayList tmp = new ArrayList();
        groupsData.clear();
        groupsTable.changeTableData();
        try
        {
            ArrayList groupsObj = aus.listGroupOfUser(uid);
            if(groupsObj == null)
                return;
            for(int i = 0; i < groupsObj.size(); i++)
                if(aus.isExistGroup((String)groupsObj.get(i)))
                {
                    HashMap groupinfo = aus.getGroup((String)groupsObj.get(i));
                    groupinfo.put("id", (String)groupsObj.get(i));
                    tmp.add(groupinfo.get("id"));
                    tmp.add(groupinfo.get("description"));
                    tmp.add(groupinfo.get("status"));
                    tmp.add(groupinfo.get("cdate"));
                    tmp.add(groupinfo.get("lmdate"));
                    groupsData.add(tmp.clone());
                    tmp.clear();
                } else
                {
                    System.out.println("ERROR : " + (String)groupsObj.get(i) + " is not exist");
                }

            groupsTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void rolesTableDefine()
    {
        rolesTable = new Table(rolesData, (ArrayList)groupsColumn.get(0), (ArrayList)groupsColumn.get(1), 1);
        rolesTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        rolesTable.setIndexColumn(0);
        rolesTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        rolesTable.getTable().addMouseListener(this);
    }

    public void setRoleTableData(String uid)
    {
        ArrayList tmp = new ArrayList();
        rolesData.clear();
        rolesTable.changeTableData();
        try
        {
            ArrayList rolesObj = aus.listRoleOfUser(uid);
            if(rolesObj == null)
                return;
            for(int i = 0; i < rolesObj.size(); i++)
                if(aus.isExistRole((String)rolesObj.get(i)))
                {
                    HashMap roleinfo = aus.getRole((String)rolesObj.get(i));
                    if(roleinfo != null)
                    {
                        roleinfo.put("id", (String)rolesObj.get(i));
                        tmp.add(roleinfo.get("id"));
                        tmp.add(roleinfo.get("description"));
                        tmp.add(roleinfo.get("status"));
                        tmp.add(roleinfo.get("cdate"));
                        tmp.add(roleinfo.get("lmdate"));
                        rolesData.add(tmp.clone());
                        tmp.clear();
                    }
                } else
                {
                    System.out.println("ERROR : " + (String)rolesObj.get(i) + " is not exist");
                }

            rolesTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent evt)
    {
        if((evt.getSource().equals(rolesScrollPane) || evt.getSource().equals(rolesTable.getTable())) && SwingUtilities.isRightMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown() && cdateTextField.getText() != null && !cdateTextField.getText().trim().equals(""))
            popup1.show(evt.getComponent(), evt.getX(), evt.getY());
        if((evt.getSource().equals(groupsScrollPane) || evt.getSource().equals(groupsTable.getTable())) && SwingUtilities.isRightMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown() && cdateTextField.getText() != null && !cdateTextField.getText().trim().equals(""))
            popup2.show(evt.getComponent(), evt.getX(), evt.getY());
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
        if(!isadmin && parent.callObject == null)
            isadmin = true;
        return isadmin;
    }

    public void setMenuEnable(boolean mode)
    {
        okButton.setEnabled(mode);
        menuLinkRole.setEnabled(mode);
        menuUnlinkRole.setEnabled(mode);
        menuLinkGroup.setEnabled(mode);
        menuUnlinkGroup.setEnabled(mode);
    }

    private UIManagement newUI;
    private AUS aus;
    private BorderLayout mainBorderLayout;
    private JPanel mainPanel;
    private DynaTextField idTextField;
    private DynaTextField passwordTextField;
    private DynaTextField nameTextField;
    private DynaTextField descriptionTextField;
    private DynaTextField primarygroupTextField;
    private DynaComboBox statusComboBox;
    private DynaComboBoxModel statusModel;
    private DynaTextField cdateTextField;
    private DynaTextField lmdateTextField;
    private DynaTextField lldateTextField;
    private DynaTextField lfdateTextField;
    private DynaTextField duedateTextField;
    private DynaTextField failcountTextField;
    private DynaTextField logincountTextField;
    private JLabel dummyLabel;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton pwChangeButton;
    private final int titleTextWidth = 150;
    private boolean userInfoMode;
    private UserManager parent;
    public JSplitPane splitPane;
    private JScrollPane rolesScrollPane;
    private Table rolesTable;
    private ArrayList rolesData;
    private JScrollPane groupsScrollPane;
    private Table groupsTable;
    private ArrayList groupsColumn;
    private ArrayList groupsData;
    private JPopupMenu popup1;
    private JMenuItem menuLinkRole;
    private JMenuItem menuUnlinkRole;
    private JPopupMenu popup2;
    private JMenuItem menuLinkGroup;
    private JMenuItem menuUnlinkGroup;
    private String authorityMessage;
    private String deleteMessage;
}
