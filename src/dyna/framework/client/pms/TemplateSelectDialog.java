// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TemplateSelectDialog.java

package dyna.framework.client.pms;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.DynaMOAD;
import dyna.framework.client.UIManagement;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.Table;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client.pms:
//            TemplateCloneWindow

public class TemplateSelectDialog extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public TemplateSelectDialog(JFrame parentFrame)
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
        classOuid = null;
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
        setTitle(DynaMOAD.getMSRString("WRD_0149", "Template", 0) + " " + DynaMOAD.getMSRString("WRD_0018", "Select", 0));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
        setResizable(false);
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
        columnNames.add(DynaMOAD.getMSRString("WRD_0079", "Code", 3));
        columnNames.add(DynaMOAD.getMSRString("name", "Name", 3));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(132));
        if(modelDataAL != null)
        {
            for(int i = 0; i < modelDataAL.size(); i++)
            {
                dataDetailInfo.add((String)((ArrayList)modelDataAL.get(i)).get(0));
                dataDetailInfo.add((String)((ArrayList)modelDataAL.get(i)).get(1));
                dataDetailInfo.add((String)((ArrayList)modelDataAL.get(i)).get(2));
                dataModel.add(dataDetailInfo.clone());
                dataDetailInfo.clear();
            }

        }
        modelTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        modelTable.setColumnSequence(new int[] {
            1, 2
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
            String packageOuid = null;
            DOSChangeable dosPackage = dos.getPackageWithName("Project");
            if(dosPackage == null)
                return;
            packageOuid = (String)dosPackage.get("ouid");
            if(packageOuid == null)
                return;
            DOSChangeable dosClass = dos.getClassWithName(packageOuid, "ProjectTemplate");
            if(dosClass == null)
                return;
            classOuid = (String)dosClass.get("ouid");
            ArrayList fields = new ArrayList();
            fields.add("86054380");
            fields.add("86054381");
            modelDataAL = dos.list(classOuid, fields);
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
            String name = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(1);
            String description = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(2);
            if(parentFrame instanceof TemplateCloneWindow)
                ((TemplateCloneWindow)parentFrame).setTemplate(ouid, description + " (" + name + ")");
        }
        exitButton_actionPerformed(null);
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
        if(mouseEvent.getClickCount() != 2)
            return;
        int selectedRow = modelTable.getTable().getSelectedRow();
        String ouidRow = modelTable.getSelectedOuidRow(selectedRow);
        if(selectedRow > -1)
        {
            String ouid = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(0);
            String name = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(1);
            String description = (String)((ArrayList)dataModel.get((new Integer(ouidRow)).intValue())).get(2);
            if(parentFrame instanceof TemplateCloneWindow)
                ((TemplateCloneWindow)parentFrame).setTemplate(ouid, description + " (" + name + ")");
        }
        exitButton_actionPerformed(null);
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
    private String classOuid;

}
