// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserInsertDialog.java

package dyna.framework.editor.user;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.framework.editor.user:
//            UserInformation, GroupInformation, RoleInformation, UserManager

public class UserInsertDialog extends JDialog
    implements ActionListener
{

    public UserInsertDialog(UserManager userManager, Object contentPanel, DefaultMutableTreeNode parent)
    {
        super(userManager, true);
        buttonPanel = null;
        toolBarBoxLayout = null;
        registButton = null;
        exitButton = null;
        groupInformation = null;
        userInformation = null;
        roleInformation = null;
        this.parent = null;
        this.parent = parent;
        if(contentPanel instanceof UserInformation)
            userInformation = (UserInformation)contentPanel;
        else
        if(contentPanel instanceof GroupInformation)
            groupInformation = (GroupInformation)contentPanel;
        else
        if(contentPanel instanceof RoleInformation)
            roleInformation = (RoleInformation)contentPanel;
        initialize();
    }

    public void initialize()
    {
        setLocation(300, 200);
        setSize(400, 400);
        setTitle("Insert");
        registButton = new JButton();
        registButton.setToolTipText("Regist");
        registButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        registButton.setText("Ok");
        registButton.setMargin(new Insets(0, 0, 0, 0));
        registButton.setActionCommand("Regist");
        registButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(registButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createHorizontalGlue());
        getContentPane().add(buttonPanel, "North");
        if(userInformation != null)
            getContentPane().add(userInformation, "Center");
        else
        if(groupInformation != null)
            getContentPane().add(groupInformation, "Center");
        else
        if(roleInformation != null)
            getContentPane().add(roleInformation, "Center");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitButton_actionPerformed(null);
            }

            public void windowOpened(WindowEvent windowEvent)
            {
                if(userInformation != null)
                    userInformation.setVisibleScrollPane(false);
                else
                if(groupInformation != null)
                    groupInformation.setVisibleScrollPane(false);
                else
                if(roleInformation != null)
                    roleInformation.setVisibleScrollPane(false);
            }

        });
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            exitButton_actionPerformed(null);
        else
        if(command.equals("Regist"))
            registButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void registButton_actionPerformed(ActionEvent e)
    {
        if(userInformation != null)
            userInformation.createUser();
        else
        if(groupInformation != null)
            groupInformation.createGroup();
        else
            roleInformation.createRole();
        dispose();
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        registButton.removeActionListener(this);
        exitButton.removeActionListener(this);
        dispose();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    private JPanel buttonPanel;
    private BoxLayout toolBarBoxLayout;
    private JButton registButton;
    private JButton exitButton;
    private GroupInformation groupInformation;
    private UserInformation userInformation;
    private RoleInformation roleInformation;
    private DefaultMutableTreeNode parent;



}
