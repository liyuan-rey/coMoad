// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FieldChooser.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.uic.Table;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, GanttChartSetting

public class FieldChooser extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public FieldChooser(Dialog owner, Component comp, String ouid, ArrayList assoTableInfo, GanttChartSetting parent)
    {
        super(owner, "Field Chooser", true);
        classOuid = "";
        setSize(280, 300);
        setResizable(false);
        setDefaultCloseOperation(2);
        addWindowListener(this);
        try
        {
            this.parent = parent;
            this.comp = comp;
            classOuid = ouid;
            this.assoTableInfo = assoTableInfo;
            newUI = DynaMOAD.newUI;
            makeAllFieldTable();
            initialize();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        allFieldScrPane = UIFactory.createStrippedScrollPane(null);
        allFieldScrPane.getViewport().add(allFieldTable.getTable());
        allFieldScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        buttonToolBar = new JToolBar();
        selectionButton = new JButton();
        selectionButton.setText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectionButton.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
        selectionButton.setMargin(new Insets(0, 0, 0, 0));
        selectionButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectionButton.setActionCommand("Select");
        selectionButton.addActionListener(this);
        selectionButton.setDoubleBuffered(true);
        closeButton = new JButton();
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        buttonToolBar.add(Box.createHorizontalStrut(120));
        buttonToolBar.add(selectionButton);
        buttonToolBar.add(Box.createHorizontalStrut(10));
        buttonToolBar.add(closeButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(allFieldScrPane, "Center");
        getContentPane().add(buttonToolBar, "South");
    }

    public void makeAllFieldTable()
    {
        allFieldData = new ArrayList();
        allFieldColumnNames = new ArrayList();
        allFieldColumnWidths = new ArrayList();
        allFieldColumnNames.add(DynaMOAD.getMSRString("WRD_A001", "Title", 0));
        allFieldColumnNames.add(DynaMOAD.getMSRString("WRD_A002", "Description", 0));
        allFieldColumnNames.add(DynaMOAD.getMSRString("WRD_A003", "Type", 0));
        allFieldColumnWidths.add(new Integer(80));
        allFieldColumnWidths.add(new Integer(80));
        allFieldColumnWidths.add(new Integer(80));
        allFieldData = (ArrayList)assoTableInfo.clone();
        allFieldTable = new Table(allFieldData, (ArrayList)allFieldColumnNames.clone(), (ArrayList)allFieldColumnWidths.clone(), 1);
        allFieldTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        allFieldTable.getTable().setRowSelectionAllowed(true);
        allFieldTable.getTable().addMouseListener(this);
        allFieldTable.setIndexColumn(0);
        allFieldTable.setSelectMode(0);
    }

    public void closeEvent()
    {
        selectionButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        dispose();
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command == null)
            closeEvent();
        else
        if(command.equals("Select"))
        {
            if(allFieldTable.getTable().getSelectedRowCount() <= 0)
                return;
            ArrayList selectedRowData = allFieldTable.getSelectedRows();
            parent.setFieldData(comp, (ArrayList)selectedRowData.get(0));
            closeEvent();
        } else
        if(command.equals("Close"))
            closeEvent();
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
        removeWindowListener(this);
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

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private UIManagement newUI;
    private JToolBar buttonToolBar;
    private JButton selectionButton;
    private JButton closeButton;
    private Table allFieldTable;
    private JScrollPane allFieldScrPane;
    private String classOuid;
    private ArrayList assoTableInfo;
    private final int columnWidth = 80;
    private ArrayList allFieldData;
    private ArrayList allFieldColumnNames;
    private ArrayList allFieldColumnWidths;
    private ArrayList selectedData;
    private ArrayList selectedColumnNames;
    private ArrayList selectedColumnWidths;
    private GanttChartSetting parent;
    private Component comp;
}