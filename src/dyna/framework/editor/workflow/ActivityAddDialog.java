// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActivityAddDialog.java

package dyna.framework.editor.workflow;

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.service.WFM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DialogReturnCallback;
import dyna.util.Utils;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            ActivityPanel

public class ActivityAddDialog extends JDialog
    implements ActionListener, WindowListener
{

    public ActivityAddDialog(JFrame frame, WFM wfm, boolean modal)
    {
        super(frame, modal);
        this.wfm = null;
        activityPanel = null;
        callback = null;
        toolBar = null;
        saveButton = null;
        scrollPane = null;
        this.wfm = wfm;
        setFont(frame.getFont());
        initialize();
    }

    public void initialize()
    {
        toolBar = new JToolBar();
        saveButton = new JButton();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        setTitle("New Activity");
        addWindowListener(this);
        toolBar.putClientProperty("jgoodies.headerStyle", HeaderStyle.BOTH);
        getContentPane().add(toolBar, "North");
        saveButton.setText("Save");
        saveButton.setFont(getFont());
        saveButton.setIcon(new ImageIcon("icons/Save.gif"));
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        toolBar.add(saveButton);
        activityPanel = new ActivityPanel();
        scrollPane.setViewportView(activityPanel);
        getContentPane().add(scrollPane, "Center");
        pack();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = evt.getActionCommand();
        if(command == null)
            windowClosing(null);
        else
        if(command.equals("save"))
        {
            if(activityPanel == null || callback == null)
                return;
            DOSChangeable dosObject = activityPanel.getValue();
            String activityDefinitionOuid = null;
            try
            {
                activityDefinitionOuid = wfm.createActivityDefinition(dosObject);
            }
            catch(Exception e)
            {
                callback.setDialogReturnValue(null);
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                dosObject = null;
                return;
            }
            if(Utils.isNullString(activityDefinitionOuid))
            {
                callback.setDialogReturnValue(null);
                JOptionPane.showMessageDialog(this, "Can not create a activity definition.", "Error Message", 0);
                dosObject = null;
                return;
            }
            dosObject.put("ouid", activityDefinitionOuid);
            dosObject.put("object.type", "add.activity");
            callback.setDialogReturnValue(dosObject);
            dosObject = null;
            windowClosing(null);
        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        removeWindowListener(this);
        activityPanel = null;
        wfm = null;
        callback = null;
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

    public void setValue(DOSChangeable object)
    {
        if(activityPanel == null || object == null)
        {
            return;
        } else
        {
            activityPanel.setValue(object);
            return;
        }
    }

    public void setDialogReturnCallback(DialogReturnCallback callback)
    {
        if(activityPanel != null)
            this.callback = callback;
    }

    private WFM wfm;
    private ActivityPanel activityPanel;
    private DialogReturnCallback callback;
    private JToolBar toolBar;
    private JButton saveButton;
    private JScrollPane scrollPane;
}
