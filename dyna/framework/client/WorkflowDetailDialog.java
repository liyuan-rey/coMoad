// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkflowDetailDialog.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.uic.Table;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement

public class WorkflowDetailDialog extends JDialog
    implements ActionListener, WindowListener
{

    public WorkflowDetailDialog(JPanel parentPanel, String activityOuid)
    {
        super((JFrame)JOptionPane.getFrameForComponent(parentPanel), false);
        dos = null;
        dss = null;
        this.parentPanel = null;
        handCursor = new Cursor(12);
        tableScrPane = null;
        modelTable = null;
        toolBarBoxLayout = null;
        buttonToolBar = null;
        exitButton = null;
        modelDataAL = null;
        dataModel = null;
        this.activityOuid = null;
        try
        {
            this.parentPanel = parentPanel;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            this.activityOuid = activityOuid;
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        getAllModel();
        initialize();
        makeModelTable();
        UIUtils.setLocationRelativeTo(this, parentPanel);
    }

    public void initialize()
    {
        setSize(300, 200);
        setTitle(DynaMOAD.getMSRString("WRD_0139", "Approval", 3));
        addWindowListener(this);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        buttonToolBar = new JPanel();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setLayout(toolBarBoxLayout);
        exitButton = new JButton(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(exitButton);
        getContentPane().add(tableScrPane, "Center");
        getContentPane().add(buttonToolBar, "South");
    }

    public void makeModelTable()
    {
        dataModel = new ArrayList();
        ArrayList dataDetailInfo = new ArrayList();
        ArrayList columnNames = new ArrayList();
        ArrayList columnWidths = new ArrayList();
        columnNames.add(DynaMOAD.getMSRString("WRD_0029", "Read", 3));
        columnNames.add(DynaMOAD.getMSRString("WRD_0139", "Closed", 3));
        columnNames.add(DynaMOAD.getMSRString("WRD_0025", "User", 3));
        columnNames.add(DynaMOAD.getMSRString("WRD_0088", "Closed Date", 3));
        columnWidths.add(new Integer(20));
        columnWidths.add(new Integer(20));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        if(modelDataAL != null)
        {
            HashMap aRow = null;
            for(int i = 0; i < modelDataAL.size(); i++)
            {
                aRow = (HashMap)modelDataAL.get(i);
                dataDetailInfo.add(aRow.get("read"));
                dataDetailInfo.add(aRow.get("closed"));
                dataDetailInfo.add(aRow.get("performer.name") + " (" + aRow.get("performer") + ")");
                dataDetailInfo.add(aRow.get("time.closed"));
                aRow = null;
                dataModel.add(dataDetailInfo.clone());
                dataDetailInfo.clear();
            }

        }
        modelTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        modelTable.setColumnSequence(new int[] {
            0, 1, 2, 3
        });
        modelTable.setIndexColumn(0);
        tableScrPane.setViewportView(modelTable.getTable());
    }

    public void getAllModel()
    {
        try
        {
            if(Utils.isNullString(activityOuid))
            {
                modelDataAL = null;
                return;
            }
            modelDataAL = DynaMOAD.wfm.getMultipleActivities(activityOuid);
        }
        catch(IIPRequestException ie)
        {
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
        if(e.getActionCommand() == null)
            exitButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        exitButton.removeActionListener(this);
        removeWindowListener(this);
        dispose();
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        exitButton.removeActionListener(this);
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

    private DOS dos;
    private DSS dss;
    private JPanel parentPanel;
    private Cursor handCursor;
    private JScrollPane tableScrPane;
    private Table modelTable;
    private BoxLayout toolBarBoxLayout;
    private JPanel buttonToolBar;
    private JButton exitButton;
    private ArrayList modelDataAL;
    private ArrayList dataModel;
    private String activityOuid;
}
