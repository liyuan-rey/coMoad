// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptSelection.java

package dyna.framework.editor.workflow;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;

// Referenced classes of package dyna.framework.editor.workflow:
//            WorkflowModeler, EventInformation

public class ScriptSelection extends JDialog
    implements ActionListener
{

    public ScriptSelection(Object parent, String ext)
    {
        super(new JFrame(), true);
        dos = null;
        dss = null;
        this.parent = null;
        fileType = null;
        scriptTable = null;
        scriptDataAL = null;
        mainPanel = null;
        buttonToolBar = null;
        toolBarBoxLayout = null;
        selectButton = null;
        exitButton = null;
        tableScrPane = null;
        dataModel = null;
        setLocation(200, 300);
        try
        {
            this.parent = parent;
            fileType = ext;
            dos = WorkflowModeler.dos;
            dss = WorkflowModeler.dss;
            getAllScript();
            initialize();
            makeModelTable();
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public void initialize()
    {
        setSize(300, 200);
        setTitle("Model List");
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        buttonToolBar = new JToolBar();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar.setLayout(toolBarBoxLayout);
        selectButton = new JButton();
        selectButton.setToolTipText("Ok");
        selectButton.setText("Ok");
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.setActionCommand("Ok");
        selectButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(selectButton);
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "North");
        getContentPane().add(tableScrPane, "Center");
    }

    public void makeModelTable()
    {
        dataModel = new ArrayList();
        ArrayList dataDetailInfo = new ArrayList();
        ArrayList columnNames = new ArrayList();
        ArrayList columnWidths = new ArrayList();
        columnNames.add("Name");
        columnNames.add("Ext");
        columnWidths.add(new Integer(220));
        columnWidths.add(new Integer(50));
        if(scriptDataAL != null)
        {
            for(int i = 0; i < scriptDataAL.size(); i++)
            {
                java.util.List testList = Utils.tokenizeMessage((String)scriptDataAL.get(i), '.');
                if(Utils.isNullString(fileType) || ((String)testList.get(1)).equals(fileType))
                {
                    dataDetailInfo.add((String)testList.get(0));
                    dataDetailInfo.add((String)testList.get(1));
                    dataModel.add(dataDetailInfo.clone());
                    dataDetailInfo.clear();
                }
            }

        }
        scriptTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        scriptTable.setColumnSequence(new int[] {
            0, 1
        });
        scriptTable.setIndexColumn(0);
        tableScrPane.setViewportView(scriptTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
    }

    public void getAllScript()
    {
        try
        {
            scriptDataAL = dss.listFile("/script");
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            exitButton_actionPerformed(e);
        else
        if(command.equals("Ok"))
            okButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void okButton_actionPerformed(ActionEvent e)
    {
        int selectedRow = scriptTable.getTable().getSelectedRow();
        String ouidRow = scriptTable.getSelectedOuidRow(selectedRow);
        String name = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(0);
        if(parent instanceof EventInformation)
            ((EventInformation)parent).setNameTextField(name);
        dispose();
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        dispose();
    }

    private DOS dos;
    private DSS dss;
    private Object parent;
    private String fileType;
    private Table scriptTable;
    private ArrayList scriptDataAL;
    private JPanel mainPanel;
    private JToolBar buttonToolBar;
    private BoxLayout toolBarBoxLayout;
    private JButton selectButton;
    private JButton exitButton;
    private JScrollPane tableScrPane;
    private ArrayList dataModel;
}
