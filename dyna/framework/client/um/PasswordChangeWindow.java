// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PasswordChangeWindow.java

package dyna.framework.client.um;

import dyna.framework.client.DynaMOAD;
import dyna.framework.client.UIGeneration;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import javax.swing.*;

public class PasswordChangeWindow extends JDialog
    implements ActionListener, WindowListener
{

    public PasswordChangeWindow(Frame ownerFrame)
    {
        super(ownerFrame, false);
        dos = null;
        dss = null;
        parentFrame = null;
        userObject = null;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        parentFrame = ownerFrame;
        initialize();
    }

    private void initialize()
    {
        setSize(250, 170);
        setTitle(DynaMOAD.getMSRString("WRD_0156", "Password Change", 3));
        addWindowListener(this);
        newPasswordLabel = new JLabel(DynaMOAD.getMSRString("WRD_0154", "New Password", 3));
        newPasswordField = new JPasswordField();
        newPasswordField.setEchoChar('*');
        confirmPasswordLabel = new JLabel(DynaMOAD.getMSRString("WRD_0155", "Confirm Password", 3));
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setEchoChar('*');
        okButton = new JButton(DynaMOAD.getMSRString("WRD_0019", "OK", 1));
        okButton.setToolTipText(DynaMOAD.getMSRString("WRD_0019", "OK", 1));
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        okButton.addActionListener(this);
        okButton.setActionCommand("ok");
        okButton.setDoubleBuffered(true);
        cancelButton = new JButton(DynaMOAD.getMSRString("WRD_0012", "Cancel", 3));
        cancelButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Cancel", 3));
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("cancel");
        cancelButton.setDoubleBuffered(true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weighty = 0.0D;
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.0D;
        gbc.insets = new Insets(5, 5, 5, 0);
        mainPanel.add(newPasswordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0D;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(newPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0D;
        gbc.insets = new Insets(0, 5, 5, 0);
        mainPanel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0D;
        gbc.insets = new Insets(0, 5, 5, 5);
        mainPanel.add(confirmPasswordField, gbc);
        buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, "Center");
        getContentPane().add(buttonPanel, "South");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
    }

    public void setUser(DOSChangeable object)
    {
        userObject = object;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("ok"))
            okButton_actionPerformed(e);
        else
        if(command.equals("cancel"))
            windowClosing(null);
    }

    private void okButton_actionPerformed(ActionEvent e)
    {
        if(userObject == null)
            return;
        try
        {
            String ouid = (String)userObject.get("ouid");
            classOuid = dos.getClassOuid(ouid);
            char newPassword[] = newPasswordField.getPassword();
            char confirmPassword[] = confirmPasswordField.getPassword();
            boolean isSame = true;
            isSame = true;
            if(newPassword.length != confirmPassword.length)
            {
                isSame = false;
            } else
            {
                for(int i = 0; i < newPassword.length; i++)
                {
                    if(newPassword[i] == confirmPassword[i])
                        continue;
                    isSame = false;
                    break;
                }

            }
            if(isSame)
            {
                userObject.put("password", new String(newPassword));
                dos.set(userObject);
                userObject.setOriginalValueMap(userObject.getValueMap());
                if(parentFrame instanceof UIGeneration)
                    ((UIGeneration)parentFrame).setData(userObject);
                Utils.executeScriptFile(dos.getEventName(classOuid, "update.after"), dss, parentFrame);
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0002", "Data Modified.", 3), DynaMOAD.getMSRString("INF_0003", "Information Message", 3), 1);
            } else
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0004", "The Password is wrong.", 0), DynaMOAD.getMSRString("WRD_0005", "Warning", 3), 2);
                return;
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void windowClosing(WindowEvent e)
    {
        okButton.removeActionListener(this);
        cancelButton.removeActionListener(this);
        removeWindowListener(this);
        dispose();
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

    private JPanel mainPanel;
    private JLabel newPasswordLabel;
    private JPasswordField newPasswordField;
    private JLabel confirmPasswordLabel;
    private JPasswordField confirmPasswordField;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;
    private DOS dos;
    private DSS dss;
    private Frame parentFrame;
    private DOSChangeable userObject;
    private String classOuid;
}
