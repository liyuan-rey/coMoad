// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModelSelection.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.client.UIManagement;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction

public class ModelSelection extends JDialog
    implements ActionListener
{

    public ModelSelection(ObjectModelingConstruction mainFrame)
    {
        super(mainFrame, false);
        dos = null;
        this.mainFrame = null;
        handCursor = new Cursor(12);
        tableScrPane = null;
        modelTable = null;
        modelDataAL = null;
        mainPanel = null;
        toolBarBoxLayout = null;
        selectButton = null;
        buttonToolBar = null;
        exitButton = null;
        dataModel = null;
        try
        {
            this.mainFrame = mainFrame;
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            getAllModel();
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
        setSize(251, 180);
        setLocation(112, 84);
        setTitle("Model List");
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        buttonToolBar = new JPanel();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setLayout(toolBarBoxLayout);
        selectButton = new JButton();
        selectButton.setToolTipText("Select");
        selectButton.setText("Select");
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setMargin(new Insets(0, 2, 0, 2));
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setMargin(new Insets(0, 10, 0, 10));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(Box.createHorizontalStrut(10));
        buttonToolBar.add(selectButton);
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "North");
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
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        for(int i = 0; i < modelDataAL.size(); i++)
        {
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("ouid"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("name"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("description"));
            dataModel.add(dataDetailInfo.clone());
            dataDetailInfo.clear();
        }

        modelTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        modelTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        modelTable.setIndexColumn(0);
        modelTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        modelTable.getTable().setCursor(handCursor);
        tableScrPane.setViewportView(modelTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
    }

    public void getAllModel()
    {
        try
        {
            ArrayList packageDataAL = new ArrayList();
            packageDataAL = dos.listAllPackage();
            modelDataAL = dos.listModel();
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
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            exitButton_actionPerformed(e);
        else
        if(command.equals("Select"))
            selectButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void selectButton_actionPerformed(ActionEvent e)
    {
        int selectedRow = modelTable.getTable().getSelectedRow();
        String ouidRow = modelTable.getSelectedOuidRow(selectedRow);
        String ouid = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(0);
        String name = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(1);
        mainFrame.reloadModel(ouid, name);
        mainFrame.displayInformation(ouid, "", 0, 0);
        dispose();
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        exitButton.removeActionListener(this);
        dispose();
    }

    private DOS dos;
    private ObjectModelingConstruction mainFrame;
    private UIManagement newUI;
    private Cursor handCursor;
    private JScrollPane tableScrPane;
    private Table modelTable;
    private ArrayList modelDataAL;
    private JPanel mainPanel;
    private BoxLayout toolBarBoxLayout;
    private JButton selectButton;
    private JPanel buttonToolBar;
    private JButton exitButton;
    private ArrayList dataModel;
}
