// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessAddDialog.java

package dyna.framework.editor.workflow;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.service.WFM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DialogReturnCallback;
import dyna.util.Utils;
import java.awt.Container;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            ProcessPanel

public class ProcessAddDialog extends JDialog
    implements ActionListener, WindowListener
{

    public ProcessAddDialog(JFrame frame, WFM wfm, boolean modal)
    {
        super(frame, modal);
        this.wfm = null;
        processPanel = null;
        callback = null;
        modelOuid = null;
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
        setTitle("New Process");
        addWindowListener(this);
        getContentPane().add(toolBar, "North");
        saveButton.setText("Save");
        saveButton.setFont(getFont());
        saveButton.setIcon(new ImageIcon("icons/Save.gif"));
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        toolBar.add(saveButton);
        processPanel = new ProcessPanel();
        scrollPane.setViewportView(processPanel);
        getContentPane().add(scrollPane, "Center");
        pack();
    }

    public void setModelOuid(String ouid)
    {
        modelOuid = ouid;
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
            if(processPanel == null || callback == null)
                return;
            DOSChangeable dosObject = processPanel.getValue();
            DOSChangeable processDefinition = null;
            String processDefinitionOuid = null;
            String identifier = null;
            ArrayList processList = null;
            if(modelOuid == null || dosObject == null)
                return;
            try
            {
                identifier = (String)dosObject.get("identifier");
                if(Utils.isNullString(identifier))
                    return;
                processList = wfm.listProcessDefinitionOfModel(modelOuid);
                if(processList != null && !processList.isEmpty())
                {
                    for(int i = 0; i < processList.size(); i++)
                    {
                        processDefinitionOuid = (String)processList.get(i);
                        processDefinition = wfm.getProcessDefinition(processDefinitionOuid);
                        if(processDefinition != null && identifier.equals(processDefinition.get("identifier")))
                            throw new Exception("Identifier duplicated.");
                    }

                }
                processDefinitionOuid = wfm.createProcessDefinition(dosObject);
            }
            catch(Exception e)
            {
                callback.setDialogReturnValue(null);
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                dosObject = null;
                return;
            }
            if(Utils.isNullString(processDefinitionOuid))
            {
                callback.setDialogReturnValue(null);
                JOptionPane.showMessageDialog(this, "Can not create a process definition.", "Error Message", 0);
                dosObject = null;
                return;
            }
            dosObject.put("ouid", processDefinitionOuid);
            callback.setDialogReturnValue(dosObject);
            dosObject = null;
            windowClosing(null);
        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        removeWindowListener(this);
        processPanel = null;
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

    public void setDialogReturnCallback(DialogReturnCallback callback)
    {
        if(processPanel != null)
            this.callback = callback;
    }

    private WFM wfm;
    private ProcessPanel processPanel;
    private DialogReturnCallback callback;
    private String modelOuid;
    private JToolBar toolBar;
    private JButton saveButton;
    private JScrollPane scrollPane;
}
