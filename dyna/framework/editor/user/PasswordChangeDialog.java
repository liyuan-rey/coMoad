// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PasswordChangeDialog.java

package dyna.framework.editor.user;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.uic.UIUtils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.user:
//            UserManager

public class PasswordChangeDialog extends JDialog
    implements ActionListener, WindowListener
{

    public PasswordChangeDialog(UserManager parent, String userId)
    {
        super(parent, "Password Change", true);
        aus = null;
        dialogBorderLayout = null;
        mainPanel = null;
        userIdLabel = null;
        oldPasswordLabel = null;
        newPasswordLabel = null;
        userIdTextField = null;
        oldPasswordField = null;
        newPasswordField = null;
        buttonPanel = null;
        resetButton = null;
        okButton = null;
        closeButton = null;
        this.userId = "";
        this.parent = null;
        this.userId = userId;
        this.parent = parent;
        aus = UserManager.aus;
        initialize();
        setSize(300, 200);
        setResizable(false);
        UIUtils.setLocationRelativeTo(this, parent);
    }

    public void initialize()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        userIdLabel = new JLabel("User ID");
        userIdLabel.setBounds(10, 10, 100, 21);
        userIdTextField = new JTextField();
        userIdTextField.setEditable(false);
        userIdTextField.setBounds(120, 10, 160, 21);
        userIdTextField.setText(userId);
        oldPasswordLabel = new JLabel("Old Password");
        oldPasswordLabel.setBounds(10, 36, 100, 21);
        oldPasswordField = new JPasswordField();
        oldPasswordField.setBounds(120, 36, 160, 21);
        newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setBounds(10, 62, 100, 21);
        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(120, 62, 160, 21);
        mainPanel.add(userIdLabel);
        mainPanel.add(userIdTextField);
        mainPanel.add(oldPasswordLabel);
        mainPanel.add(oldPasswordField);
        mainPanel.add(newPasswordLabel);
        mainPanel.add(newPasswordField);
        resetButton = new JButton();
        resetButton.setText("Reset");
        resetButton.setIcon(new ImageIcon("icons/Refresh.gif"));
        resetButton.setActionCommand("Reset");
        resetButton.addActionListener(this);
        resetButton.setMargin(new Insets(0, 0, 0, 0));
        okButton = new JButton();
        okButton.setText("Ok");
        okButton.setIcon(new ImageIcon("icons/Ok.gif"));
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        okButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton = new JButton();
        closeButton.setText("Close");
        closeButton.setIcon(new ImageIcon("icons/Exit.gif"));
        closeButton.setActionCommand("Exit");
        closeButton.addActionListener(this);
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        dialogBorderLayout = new BorderLayout();
        getContentPane().setLayout(dialogBorderLayout);
        getContentPane().add(buttonPanel, "North");
        getContentPane().add(mainPanel, "Center");
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void closeEvent()
    {
        resetButton.removeActionListener(this);
        okButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        dispose();
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            String command = actionEvent.getActionCommand();
            if(command == null)
                closeEvent();
            else
            if(command.equals("Reset"))
            {
                boolean tmpBoolean = aus.resetPassword(userId);
                System.out.println("Reset : " + tmpBoolean);
                if(tmpBoolean)
                {
                    closeEvent();
                    JOptionPane.showMessageDialog(parent, "\uBCC0\uACBD\uB418\uC5C8\uC2B5\uB2C8\uB2E4", "Information", 1);
                } else
                {
                    JOptionPane.showMessageDialog(parent, "\uBCC0\uACBD\uD560 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4. \uAD00\uB9AC\uC790\uC5D0\uAC8C \uBB38\uC758\uD558\uC2DC\uAE30 \uBC14\uB78D\uB2C8\uB2E4.", "ERROR", 0);
                }
            } else
            if(command.equals("Ok"))
            {
                boolean tmpBoolean = aus.changePassword(userId, oldPasswordField.getText(), newPasswordField.getText());
                System.out.println("Ok : " + tmpBoolean);
                if(tmpBoolean)
                {
                    closeEvent();
                    JOptionPane.showMessageDialog(parent, "\uBCC0\uACBD\uB418\uC5C8\uC2B5\uB2C8\uB2E4", "Information", 1);
                } else
                {
                    JOptionPane.showMessageDialog(parent, "\uBCC0\uACBD\uD560 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4.", "ERROR", 0);
                }
            } else
            if(command.equals("Exit"))
                closeEvent();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
            JOptionPane.showMessageDialog(parent, "\uAD00\uB9AC\uC790\uC5D0\uAC8C \uBB38\uC758\uD558\uC2DC\uAE30 \uBC14\uB78D\uB2C8\uB2E4.\n" + ie, "ERROR", 0);
        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        closeEvent();
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
        closeEvent();
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    private AUS aus;
    private BorderLayout dialogBorderLayout;
    private JPanel mainPanel;
    private JLabel userIdLabel;
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;
    private JTextField userIdTextField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPanel buttonPanel;
    private JButton resetButton;
    private JButton okButton;
    private JButton closeButton;
    private String userId;
    private UserManager parent;
}
