// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RoleInformation.java

package dyna.framework.editor.user;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.uic.DynaTextField;
import dyna.uic.Table;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.editor.user:
//            UserManager, SmallSearchForUserManager

public class RoleInformation extends JPanel
    implements ActionListener, MouseListener
{

    public RoleInformation(UserManager parent, boolean mode)
    {
        newUI = DynaMOAD.newUI;
        aus = null;
        mainBorderLayout = null;
        mainPanel = null;
        idTextField = null;
        descriptionTextField = null;
        statusTextField = null;
        cdateTextField = null;
        lmdateTextField = null;
        dummyLabel = null;
        buttonPanel = null;
        okButton = null;
        pwChangeButton = null;
        groupInfoMode = false;
        this.parent = null;
        pnUsers = null;
        usersScrollPane = null;
        usersTable = null;
        usersColumn = null;
        usersData = null;
        deleteMessage = "\uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?";
        this.parent = parent;
        groupInfoMode = mode;
        aus = UserManager.aus;
        initialize();
        setFieldEnabled(false);
    }

    public void initialize()
    {
        setSize(new Dimension(600, 400));
        idTextField = new DynaTextField();
        idTextField.setMandatory(true);
        idTextField.setTitleText("Role ID");
        idTextField.setVisible(true);
        idTextField.setTitleWidth(150);
        idTextField.setTitleVisible(true);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Description");
        descriptionTextField.setVisible(true);
        descriptionTextField.setTitleWidth(150);
        descriptionTextField.setTitleVisible(true);
        statusTextField = new DynaTextField();
        statusTextField.setMandatory(false);
        statusTextField.setTitleText("Status");
        statusTextField.setTitleWidth(150);
        statusTextField.setTitleVisible(true);
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
        usersTableDefine();
        usersScrollPane = UIFactory.createStrippedScrollPane(null);
        usersScrollPane.getViewport().add(usersTable.getTable());
        usersScrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        usersScrollPane.setMinimumSize(new Dimension(300, 300));
        usersScrollPane.setPreferredSize(new Dimension(300, 400));
        usersScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Users"));
        usersScrollPane.addMouseListener(this);
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
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(descriptionTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(statusTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(cdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(lmdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(usersScrollPane, gridBagCon);
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(idTextField);
        mainPanel.add(descriptionTextField);
        mainPanel.add(statusTextField);
        mainPanel.add(cdateTextField);
        mainPanel.add(lmdateTextField);
        mainPanel.add(usersScrollPane);
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
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalGlue());
        mainBorderLayout = new BorderLayout();
        setLayout(mainBorderLayout);
        if(groupInfoMode)
            add(buttonPanel, "North");
        add(mainPanel, "Center");
        popup = new JPopupMenu();
        menuLinkUser = new JMenuItem();
        menuLinkUser.setText("Link User");
        menuLinkUser.setIcon(new ImageIcon("icons/Link.gif"));
        menuLinkUser.setActionCommand("Link User");
        menuLinkUser.addActionListener(this);
        menuUnlinkUser = new JMenuItem();
        menuUnlinkUser.setText("Unlink User");
        menuUnlinkUser.setIcon(new ImageIcon("icons/Unlink.gif"));
        menuUnlinkUser.setActionCommand("Unlink User");
        menuUnlinkUser.addActionListener(this);
        popup.add(menuLinkUser);
        popup.add(menuUnlinkUser);
        if(!isAdmin())
        {
            okButton.setEnabled(false);
            pwChangeButton.setEnabled(false);
            menuLinkUser.setEnabled(false);
            menuUnlinkUser.setEnabled(false);
        }
    }

    public void setData(HashMap roleInfo)
    {
        idTextField.setText((String)roleInfo.get("id"));
        descriptionTextField.setText((String)roleInfo.get("description"));
        statusTextField.setText((String)roleInfo.get("status"));
        cdateTextField.setText((String)roleInfo.get("cdate"));
        lmdateTextField.setText((String)roleInfo.get("lmdate"));
        idTextField.setEnabled(false);
        setTableData((String)roleInfo.get("id"));
    }

    public void setFieldEnabled(boolean mode)
    {
        if(!mode)
        {
            cdateTextField.setEditable(false);
            lmdateTextField.setEditable(false);
        }
    }

    public void createRole()
    {
        HashMap roleInfo = new HashMap();
        roleInfo.put("roleId", idTextField.getText());
        roleInfo.put("description", descriptionTextField.getText());
        roleInfo.put("status", statusTextField.getText());
        roleInfo.put("cdate", cdateTextField.getText());
        roleInfo.put("lmdate", lmdateTextField.getText());
        try
        {
            if(!groupInfoMode)
            {
                if(aus.isExistRole((String)roleInfo.get("roleId")))
                {
                    JOptionPane.showMessageDialog(this, "\uC774\uBBF8 \uC874\uC7AC\uD558\uBBC0\uB85C \uB4F1\uB85D\uD560 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4.", "WARNING", 2);
                    return;
                }
                aus.createRole(roleInfo);
                parent.refreshRolesData();
            } else
            {
                aus.setRole(roleInfo);
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
            HashMap roleInfo = new HashMap();
            roleInfo.put("roleId", idTextField.getText());
            roleInfo.put("description", descriptionTextField.getText());
            roleInfo.put("status", statusTextField.getText());
            roleInfo.put("cdate", cdateTextField.getText());
            roleInfo.put("lmdate", lmdateTextField.getText());
            try
            {
                if(!groupInfoMode)
                    aus.createRole(roleInfo);
                else
                    aus.setRole(roleInfo);
                parent.refreshRolesData();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(command.equals("Link User"))
        {
            SmallSearchForUserManager smallSearch = new SmallSearchForUserManager((UserManager)JOptionPane.getFrameForComponent(this), "role", "user", idTextField.getText());
            smallSearch.setVisible(true);
            setTableData(idTextField.getText());
            usersTable.getTable().clearSelection();
        } else
        if(command.equals("Unlink User"))
        {
            if(usersTable.getTable().getSelectedRowCount() < 1)
                return;
            Object option[] = {
                "Yes", "No"
            };
            int res = JOptionPane.showOptionDialog(this, deleteMessage, "QUESTION", 0, 3, new ImageIcon("icons/Question32.gif"), option, option[1]);
            if(res != 0)
                return;
            int rows[] = usersTable.getTable().getSelectedRows();
            try
            {
                for(int i = 0; i < rows.length; i++)
                    aus.unlinkRoleFromUser(idTextField.getText(), (String)((ArrayList)usersData.get(rows[i])).get(0));

                setTableData(idTextField.getText());
                usersTable.getTable().clearSelection();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
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
        if(SwingUtilities.isRightMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown() && cdateTextField.getText() != null && !cdateTextField.getText().trim().equals(""))
            popup.show(evt.getComponent(), evt.getX(), evt.getY());
    }

    public void usersTableDefine()
    {
        setTableHeader();
        usersTable = new Table(usersData, (ArrayList)usersColumn.get(0), (ArrayList)usersColumn.get(1), 1);
        usersTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        usersTable.setIndexColumn(0);
        usersTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        usersTable.getTable().addMouseListener(this);
    }

    public void setTableHeader()
    {
        usersColumn = new ArrayList();
        usersData = new ArrayList();
        ArrayList colname = new ArrayList();
        ArrayList colwidth = new ArrayList();
        colname.add("User ID");
        colname.add("Name");
        colname.add("Primary Group");
        colname.add("Status");
        colname.add("Description");
        colname.trimToSize();
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.trimToSize();
        usersColumn.add(colname);
        usersColumn.add(colwidth);
        usersColumn.trimToSize();
    }

    public void setTableData(String rid)
    {
        ArrayList tmp = new ArrayList();
        usersData.clear();
        usersTable.changeTableData();
        try
        {
            ArrayList usersObj = aus.listUsersOfRole(rid);
            if(usersObj == null)
                return;
            for(int i = 0; i < usersObj.size(); i++)
                if(aus.isExistUser((String)usersObj.get(i)))
                {
                    HashMap userinfo = aus.getUser((String)usersObj.get(i));
                    userinfo.put("id", (String)usersObj.get(i));
                    tmp.add(userinfo.get("id"));
                    tmp.add(userinfo.get("name"));
                    tmp.add(userinfo.get("primarygroup"));
                    tmp.add(userinfo.get("status"));
                    tmp.add(userinfo.get("description"));
                    usersData.add(tmp.clone());
                    tmp.clear();
                } else
                {
                    System.out.println("ERROR : " + (String)usersObj.get(i) + " is not exist");
                }

            usersTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setVisibleScrollPane(boolean isvisible)
    {
        usersScrollPane.setVisible(isvisible);
        if(!isvisible)
            mainPanel.add(dummyLabel);
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

    public void setMenuEnable(boolean mode)
    {
        okButton.setEnabled(mode);
        menuLinkUser.setEnabled(mode);
        menuUnlinkUser.setEnabled(mode);
    }

    private UIManagement newUI;
    private AUS aus;
    private BorderLayout mainBorderLayout;
    private JPanel mainPanel;
    private DynaTextField idTextField;
    private DynaTextField descriptionTextField;
    private DynaTextField statusTextField;
    private DynaTextField cdateTextField;
    private DynaTextField lmdateTextField;
    private JLabel dummyLabel;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton pwChangeButton;
    private final int titleTextWidth = 150;
    private boolean groupInfoMode;
    private UserManager parent;
    private JPanel pnUsers;
    public JScrollPane usersScrollPane;
    private Table usersTable;
    private ArrayList usersColumn;
    private ArrayList usersData;
    private JPopupMenu popup;
    private JMenuItem menuLinkUser;
    private JMenuItem menuUnlinkUser;
    private String deleteMessage;
}
