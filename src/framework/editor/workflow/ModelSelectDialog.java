// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModelSelectDialog.java

package dyna.framework.editor.workflow;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

public class ModelSelectDialog extends JDialog
    implements ActionListener, WindowListener
{

    public ModelSelectDialog(JFrame frame, DOS dos, boolean modal)
    {
        super(frame, modal);
        this.dos = null;
        callback = null;
        tableScrPane = null;
        modelTable = null;
        buttonToolBar = null;
        selectButton = null;
        exitButton = null;
        modelDataAL = null;
        dataModel = null;
        this.dos = dos;
        setFont(frame.getFont());
        getAllModel();
        initialize();
        makeModelTable();
    }

    public void initialize()
    {
        setSize(300, 200);
        setTitle("Model List");
        addWindowListener(this);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        buttonToolBar = new JToolBar();
        buttonToolBar.setFloatable(false);
        Dimension buttonSize = new Dimension(70, 25);
        selectButton = new JButton("Select");
        selectButton.setIcon(new ImageIcon("icons/Ok.gif"));
        selectButton.setActionCommand("Select");
        selectButton.setFont(getFont());
        selectButton.setMinimumSize(buttonSize);
        selectButton.setPreferredSize(buttonSize);
        selectButton.addActionListener(this);
        exitButton = new JButton("Cancel");
        exitButton.setIcon(new ImageIcon("icons/Cancel.gif"));
        exitButton.setActionCommand("Exit");
        exitButton.setFont(getFont());
        exitButton.setMinimumSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        exitButton.addActionListener(this);
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
        ArrayList dataDetailInfo = null;
        ArrayList columnNames = new ArrayList();
        ArrayList columnWidths = new ArrayList();
        columnNames.add("OUID");
        columnNames.add("Name");
        columnNames.add("Description");
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        DOSChangeable dosObject = null;
        for(int i = 0; i < modelDataAL.size(); i++)
        {
            dosObject = (DOSChangeable)modelDataAL.get(i);
            dataDetailInfo = new ArrayList(4);
            dataDetailInfo.add((String)dosObject.get("ouid"));
            dataDetailInfo.add((String)dosObject.get("name"));
            dataDetailInfo.add((String)dosObject.get("description"));
            dataDetailInfo.add(dosObject);
            dosObject.put("object.type", "model");
            dataModel.add(dataDetailInfo);
            dataDetailInfo = null;
            dosObject = null;
        }

        modelTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        modelTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        modelTable.setIndexColumn(0);
        tableScrPane.setViewportView(modelTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
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
        if(command == null)
            windowClosing(null);
        else
        if(command.equals("Select"))
            selectButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            windowClosing(null);
    }

    public void selectButton_actionPerformed(ActionEvent e)
    {
        int selectedRow = modelTable.getTable().getSelectedRow();
        if(selectedRow > -1 && callback != null)
        {
            callback.setDialogReturnValue(((ArrayList)dataModel.get(selectedRow)).get(3));
            windowClosing(null);
        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        selectButton.removeActionListener(this);
        exitButton.removeActionListener(this);
        removeWindowListener(this);
        callback = null;
        dos = null;
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
        this.callback = callback;
    }

    private DOS dos;
    private DialogReturnCallback callback;
    private JScrollPane tableScrPane;
    private Table modelTable;
    private JToolBar buttonToolBar;
    private JButton selectButton;
    private JButton exitButton;
    private ArrayList modelDataAL;
    private ArrayList dataModel;
}
