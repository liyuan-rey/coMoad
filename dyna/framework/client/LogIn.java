// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LogIn.java

package dyna.framework.client;

import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.uic.SplashWindow;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, MainFrame, UIManagement

public class LogIn extends JFrame
    implements ActionListener, WindowListener
{
    public class ShutdownHook extends Thread
    {

        public void run()
        {
            if(!Utils.isNullString(LogIn.userID))
                try
                {
                    DynaMOAD.aus.logout(LogIn.userID);
                    DynaMOAD.dfw.releaseLicense("DP-BASE");
                }
                catch(Exception exception) { }
        }

        public ShutdownHook()
        {
        }
    }


    public LogIn()
    {
        dos = null;
        dtm = null;
        aus = null;
        msr = null;
        session = null;
        shutdownHook = null;
        try
        {
            dos = DynaMOAD.dos;
            dtm = DynaMOAD.dtm;
            aus = DynaMOAD.aus;
            msr = DynaMOAD.msr;
            newUI = DynaMOAD.newUI;
            DynaMOAD.stringRepository = msr.getAllStgrep("");
            initialize();
            addWindowListener(this);
            if(DynaMOAD.splashWindow != null)
                DynaMOAD.splashWindow.setStatusText("Copyright (c) 2003-2004, Innovative PLM Solutions LTD.");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.err.println(ex);
        }
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("DynaMOAD Login");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(668, 426);
        setResizable(false);
        if(!System.getProperty("java.specification.version").startsWith("1.2") && !System.getProperty("java.specification.version").startsWith("1.3"))
            setUndecorated(true);
        Dimension frameSize = getSize();
        if(frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if(frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        getContentPane().setLayout(null);
        imageLabel = new JLabel();
        imageLabel.setOpaque(true);
        imageLabel.setRequestFocusEnabled(false);
        imageLabel.setIcon(new ImageIcon(getClass().getResource("/icons/Login0.gif")));
        imageLabel.setIconTextGap(0);
        imageLabel.setBounds(0, 0, 668, 426);
        productImageLabel = new JLabel();
        productImageLabel.setRequestFocusEnabled(false);
        productImageLabel.setIcon(new ImageIcon(getClass().getResource("/icons/ProductImage.gif")));
        productImageLabel.setIconTextGap(0);
        productImageLabel.setHorizontalAlignment(2);
        productImageLabel.setVerticalAlignment(1);
        productImageLabel.setBounds(0, 0, 668, 426);
        userIDLabel = new JLabel(DynaMOAD.getMSRString("WRD_0064", "User ID", 0));
        userIDLabel.setRequestFocusEnabled(false);
        userIDLabel.setBounds(407, 156, 90, 18);
        userIDLabel.setForeground(new Color(27, 12, 128));
        userIDTextField = new JTextField();
        userIDTextField.setRequestFocusEnabled(true);
        userIDTextField.setBounds(482, 154, 80, 22);
        passwordLabel = new JLabel(DynaMOAD.getMSRString("WRD_0065", "Password", 0));
        passwordLabel.setRequestFocusEnabled(false);
        passwordLabel.setBounds(407, 181, 90, 18);
        passwordLabel.setForeground(new Color(27, 12, 128));
        passwordTextField = new JPasswordField();
        passwordTextField.setRequestFocusEnabled(true);
        passwordTextField.setBounds(482, 179, 80, 22);
        passwordTextField.setActionCommand("password");
        passwordTextField.addActionListener(this);
        okButton = new JButton();
        okButton.setRequestFocusEnabled(true);
        okButton.setToolTipText("Login");
        okButton.setText(DynaMOAD.getMSRString("WRD_0066", "login", 0));
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.setBounds(569, 179, 60, 22);
        okButton.setActionCommand("Login");
        okButton.addActionListener(this);
        getContentPane().add(userIDLabel);
        getContentPane().add(userIDTextField);
        getContentPane().add(passwordLabel);
        getContentPane().add(passwordTextField);
        getContentPane().add(okButton);
        getContentPane().add(productImageLabel);
        getContentPane().add(imageLabel);
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    public void closeEvent()
    {
        dispose();
        try
        {
            DynaMOAD.dfw.releaseLicense("DP-BASE");
        }
        catch(Exception exception) { }
        if(!DynaMOAD.isDeamonMode)
            System.exit(0);
        else
        if(session != null)
            session.setProperty("dynapdm.login", "false");
    }

    public void OKEvent()
    {
        try
        {
            if(!Utils.isNullString(userIDTextField.getText()) && !Utils.isNullString(passwordTextField.getText()))
            {
                if(session != null)
                {
                    aus.logout(userIDTextField.getText());
                    DynaMOAD.dfw.releaseLicense("DP-BASE");
                }
                boolean validID = aus.login(userIDTextField.getText(), passwordTextField.getText());
                userID = userIDTextField.getText();
                HashMap user = aus.getUser(userID);
                userName = (String)user.get("name");
                user.clear();
                user = null;
                isAdmin = userID.equals("SYSTEM.ADMINISTRATOR") || userID.equals("SYSTEM.INTERNAL");
                if(!isAdmin)
                    isAdmin = aus.hasRole(userID, "SYSTEM.ADMINISTRATOR");
                if(validID)
                {
                    if(shutdownHook == null)
                        shutdownHook = new ShutdownHook();
                    else
                        Runtime.getRuntime().removeShutdownHook(shutdownHook);
                    Runtime.getRuntime().addShutdownHook(shutdownHook);
                    mainFrame = new MainFrame();
                    if(!DynaMOAD.isDeamonMode)
                    {
                        mainFrame.setVisible(true);
                    } else
                    {
                        mainFrame.setVisible(false);
                        mainFrame.dispose();
                        mainFrame = null;
                    }
                    dispose();
                    if(DynaMOAD.splashWindow != null)
                    {
                        DynaMOAD.splashWindow.startClose();
                        DynaMOAD.splashWindow = null;
                    }
                    if(session != null)
                    {
                        session.setProperty("dynapdm.login", "true");
                        session.setProperty("dynapdm.user.id", userID);
                    }
                } else
                {
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_A001", "Incorrect Password.", 0), "Error", 0);
                    if(!isVisible())
                        System.exit(-1);
                }
            }
        }
        catch(IIPRequestException ie)
        {
            String tempString = ie.toString();
            if(!Utils.isNullString(tempString) && tempString.indexOf("User ID not exists:") > -1)
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_A002", "User ID not exists.", 0), "Error", 0);
            else
                System.err.println(ie);
        }
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            closeEvent();
        else
        if(command.equals("Close"))
            closeEvent();
        else
        if(command.equals("Login"))
            OKEvent();
        else
        if(command.equals("password"))
            okButton.doClick();
    }

    public void windowClosing(WindowEvent e)
    {
        closeEvent();
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent e)
    {
        userIDTextField.requestFocus();
    }

    private DOS dos;
    private DTM dtm;
    private AUS aus;
    private MSR msr;
    private UIManagement newUI;
    private MainFrame mainFrame;
    private BorderLayout mainFrameBorderLayout;
    private JLabel imageLabel;
    private JLabel productImageLabel;
    private JLabel userIDLabel;
    private JLabel passwordLabel;
    private JButton okButton;
    public JTextField userIDTextField;
    public JPasswordField passwordTextField;
    public static String userID = "";
    public static String userName = "";
    public static boolean isAdmin = false;
    private Session session;
    private ShutdownHook shutdownHook;

}