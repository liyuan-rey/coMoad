// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActionAllList.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.client.UIManagement;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction

public class ActionAllList extends JDialog
    implements ActionListener
{

    public ActionAllList(JFrame parentFrame)
    {
        super(parentFrame, false);
        dos = null;
        this.parentFrame = null;
        tableScrPane = null;
        modelTable = null;
        toolBarBoxLayout = null;
        buttonToolBar = null;
        exitButton = null;
        modelDataAL = null;
        dataModel = null;
        try
        {
            this.parentFrame = parentFrame;
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            getAllModel();
            initialize();
            makeModelTable();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        setSize(557, 400);
        setLocation(112, 84);
        setTitle("Action List");
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        buttonToolBar = new JPanel();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setLayout(toolBarBoxLayout);
        exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        exitButton.setMargin(new Insets(0, 5, 0, 5));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        exitButton.setDoubleBuffered(true);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(Box.createHorizontalStrut(10));
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
        columnNames.add("Ouid");
        columnNames.add("Name");
        columnNames.add("Description");
        columnNames.add("Return type");
        columnNames.add("Return type class");
        columnNames.add("Scope");
        columnNames.add("Call type");
        columnNames.add("Leaf Action");
        columnNames.add("Query");
        columnNames.add("Visible");
        columnNames.add("Title");
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(100));
        columnWidths.add(new Integer(30));
        columnWidths.add(new Integer(50));
        columnWidths.add(new Integer(30));
        columnWidths.add(new Integer(30));
        columnWidths.add(new Integer(22));
        columnWidths.add(new Integer(22));
        columnWidths.add(new Integer(22));
        columnWidths.add(new Integer(80));
        for(int i = 0; i < modelDataAL.size(); i++)
        {
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("ouid"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("name"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("description"));
            dataDetailInfo.add((Byte)((DOSChangeable)modelDataAL.get(i)).get("return.type"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("return.type.ouid@class"));
            dataDetailInfo.add((Byte)((DOSChangeable)modelDataAL.get(i)).get("scope"));
            dataDetailInfo.add((Byte)((DOSChangeable)modelDataAL.get(i)).get("call.type"));
            dataDetailInfo.add((Boolean)((DOSChangeable)modelDataAL.get(i)).get("is.leaf"));
            dataDetailInfo.add((Boolean)((DOSChangeable)modelDataAL.get(i)).get("is.query"));
            dataDetailInfo.add((Boolean)((DOSChangeable)modelDataAL.get(i)).get("is.visible"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("title"));
            dataModel.add(dataDetailInfo.clone());
            dataDetailInfo.clear();
        }

        modelTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        modelTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
            10
        });
        modelTable.setIndexColumn(0);
        modelTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        tableScrPane.setViewportView(modelTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
    }

    public void getAllModel()
    {
        try
        {
            modelDataAL = dos.listAction();
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
        dispose();
    }

    private DOS dos;
    private JFrame parentFrame;
    private UIManagement newUI;
    private JScrollPane tableScrPane;
    private Table modelTable;
    private BoxLayout toolBarBoxLayout;
    private JPanel buttonToolBar;
    private JButton exitButton;
    private ArrayList modelDataAL;
    private ArrayList dataModel;
}
