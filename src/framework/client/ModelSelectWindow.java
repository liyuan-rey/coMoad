// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ModelSelectWindow.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, MainFrame

public class ModelSelectWindow extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public ModelSelectWindow(JFrame parentFrame)
    {
        super(parentFrame, false);
        dos = null;
        dss = null;
        this.parentFrame = null;
        handCursor = new Cursor(12);
        tableScrPane = null;
        modelTable = null;
        toolBarBoxLayout = null;
        buttonToolBar = null;
        selectButton = null;
        exitButton = null;
        modelDataAL = null;
        dataModel = null;
        try
        {
            this.parentFrame = parentFrame;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            newUI = DynaMOAD.newUI;
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        getAllModel();
        initialize();
        makeModelTable();
    }

    public void initialize()
    {
        setSize(300, 200);
        setTitle(DynaMOAD.getMSRString("WRD_0152", "Model List", 3));
        addWindowListener(this);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        buttonToolBar = new JPanel();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setLayout(toolBarBoxLayout);
        selectButton = new JButton(DynaMOAD.getMSRString("WRD_0018", "ok", 1));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setMargin(new Insets(0, 5, 0, 5));
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        exitButton = new JButton(DynaMOAD.getMSRString("WRD_0012", "Cancel", 3));
        exitButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Exit", 3));
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        exitButton.setDoubleBuffered(true);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(selectButton);
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
        columnNames.add(DynaMOAD.getMSRString("modelID", "Ouid", 3));
        columnNames.add(DynaMOAD.getMSRString("name", "Name", 3));
        columnNames.add(DynaMOAD.getMSRString("description", "Description", 3));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(132));
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
        modelTable.getTable().addMouseListener(this);
        modelTable.getTable().setCursor(handCursor);
        tableScrPane.setViewportView(modelTable.getTable());
    }

    public void getAllModel()
    {
        try
        {
            modelDataAL = dos.listModel();
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
        if(selectedRow > -1)
        {
            String ouid = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(0);
            modelOuid = ouid;
            try
            {
                String oldModel = DynaMOAD.dos.getWorkingModel();
                if(oldModel != null)
                    try
                    {
                        Utils.executeScriptFile(dos.getEventName(oldModel, "model.closed"), dss);
                    }
                    catch(IIPRequestException ie)
                    {
                        System.err.println(ie);
                    }
                DynaMOAD.dos.setWorkingModel(ouid);
            }
            catch(Exception ee)
            {
                System.err.println(ee);
            }
            String name = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(1);
            String description = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(2);
            TreeNodeObject rootNode = new TreeNodeObject(ouid, name, description);
            if(parentFrame instanceof MainFrame)
            {
                ((MainFrame)parentFrame).makeObjectSetTree(rootNode, 1);
                ((MainFrame)parentFrame).loadScripts();
            }
            exitButton_actionPerformed(e);
            if(parentFrame instanceof MainFrame)
                ((MainFrame)parentFrame).setConfigurationMenuEnable(false);
            try
            {
                Utils.executeScriptFile(dos.getEventName(ouid, "model.opened"), dss);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        }
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        selectButton.removeActionListener(this);
        exitButton.removeActionListener(this);
        removeWindowListener(this);
        dispose();
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        selectButton.removeActionListener(this);
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

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        int row = modelTable.getTable().getSelectedRow();
        String ouidRow = modelTable.getSelectedOuidRow(row);
        modelTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        if(mouseEvent.getClickCount() == 2 && row > -1)
        {
            String ouid = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(0);
            modelOuid = ouid;
            try
            {
                String oldModel = DynaMOAD.dos.getWorkingModel();
                if(oldModel != null)
                    try
                    {
                        Utils.executeScriptFile(dos.getEventName(oldModel, "model.closed"), dss);
                    }
                    catch(IIPRequestException ie)
                    {
                        System.err.println(ie);
                    }
                DynaMOAD.dos.setWorkingModel(ouid);
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
            String name = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(1);
            String description = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(2);
            TreeNodeObject rootNode = new TreeNodeObject(ouid, name, description);
            if(parentFrame instanceof MainFrame)
            {
                ((MainFrame)parentFrame).makeObjectSetTree(rootNode, 1);
                ((MainFrame)parentFrame).loadScripts();
            }
            selectButton.removeActionListener(this);
            exitButton.removeActionListener(this);
            removeWindowListener(this);
            dispose();
            if(parentFrame instanceof MainFrame)
                ((MainFrame)parentFrame).setConfigurationMenuEnable(false);
            try
            {
                Utils.executeScriptFile(dos.getEventName(ouid, "model.opened"), dss);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private DOS dos;
    private DSS dss;
    private JFrame parentFrame;
    private UIManagement newUI;
    private Cursor handCursor;
    private JScrollPane tableScrPane;
    private Table modelTable;
    private BoxLayout toolBarBoxLayout;
    private JPanel buttonToolBar;
    private JButton selectButton;
    private JButton exitButton;
    private ArrayList modelDataAL;
    private ArrayList dataModel;
    public static String modelOuid = "";

}