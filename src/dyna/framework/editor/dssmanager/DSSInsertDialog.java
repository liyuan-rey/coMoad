// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSSInsertDialog.java

package dyna.framework.editor.dssmanager;

import java.awt.Container;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.framework.editor.dssmanager:
//            StorageInformation, DSSInformation, DSSManager

public class DSSInsertDialog extends JDialog
    implements ActionListener
{

    public DSSInsertDialog(DSSManager userManager, Object contentPanel, DefaultMutableTreeNode parent)
    {
        super(userManager, false);
        buttonToolBar = null;
        toolBarBoxLayout = null;
        registButton = null;
        exitButton = null;
        storageInformation = null;
        dssInformation = null;
        this.parent = null;
        this.parent = parent;
        if(contentPanel instanceof StorageInformation)
            storageInformation = (StorageInformation)contentPanel;
        else
        if(contentPanel instanceof DSSInformation)
            dssInformation = (DSSInformation)contentPanel;
        initialize();
    }

    public void initialize()
    {
        setLocation(300, 200);
        setSize(400, 400);
        setTitle("Insert");
        buttonToolBar = new JToolBar();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar.setLayout(toolBarBoxLayout);
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
        buttonToolBar.add(registButton);
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "North");
        if(storageInformation != null)
            getContentPane().add(storageInformation, "Center");
        else
        if(dssInformation != null)
            getContentPane().add(dssInformation, "Center");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitButton_actionPerformed(null);
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
        if(storageInformation != null)
            storageInformation.createGroup();
        else
        if(dssInformation != null)
            dssInformation.createGroup();
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

    private JToolBar buttonToolBar;
    private BoxLayout toolBarBoxLayout;
    private JButton registButton;
    private JButton exitButton;
    private StorageInformation storageInformation;
    private DSSInformation dssInformation;
    private DefaultMutableTreeNode parent;
}
