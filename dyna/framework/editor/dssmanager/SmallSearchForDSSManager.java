// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SmallSearchForDSSManager.java

package dyna.framework.editor.dssmanager;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.DSS;
import dyna.uic.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.dssmanager:
//            DSSManager, DSSInformation

public class SmallSearchForDSSManager extends JDialog
    implements ActionListener, MouseListener, WindowListener
{

    public SmallSearchForDSSManager(DSSManager parent, DSSInformation dssInfo, String objectType, String objectId)
    {
        super(parent, "Select Window", false);
        aus = null;
        dss = null;
        this.dssInfo = null;
        dialogBorderLayout = null;
        mainPanel = null;
        tableScrPane = null;
        tableSmallSearch = null;
        buttonToolBar = null;
        selectButton = null;
        closeButton = null;
        searchDataList = null;
        columnNameList = null;
        columnWidthList = null;
        selectedRows = null;
        this.objectId = null;
        this.objectType = null;
        this.dssInfo = dssInfo;
        this.objectType = objectType;
        this.objectId = objectId;
        aus = DSSManager.aus;
        dss = DSSManager.dss;
        makeSearchTable();
        initialize();
        setTableData(objectType);
        UIUtils.setLocationRelativeTo(this, parent);
    }

    public void makeSearchTable()
    {
        searchDataList = new ArrayList();
        columnNameList = new ArrayList();
        columnWidthList = new ArrayList();
        columnNameList.add("Storage Id");
        columnNameList.add("Base Directory");
        columnNameList.add("Description");
        columnWidthList.add(new Integer(200));
        columnWidthList.add(new Integer(200));
        columnWidthList.add(new Integer(200));
        tableSmallSearch = new Table(searchDataList, columnNameList, columnWidthList, 0);
        tableSmallSearch.setColumnSequence(new int[] {
            0, 1, 2
        });
        tableSmallSearch.setIndexColumn(0);
        tableSmallSearch.getTable().addMouseListener(this);
    }

    public void initialize()
    {
        selectButton = new JButton();
        selectButton.setText("Select");
        selectButton.setIcon(new ImageIcon("icons/Select.gif"));
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(this);
        closeButton = new JButton();
        closeButton.setText("Close");
        closeButton.setIcon(new ImageIcon("icons/Exit.gif"));
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        buttonToolBar = new JToolBar();
        buttonToolBar.add(selectButton);
        buttonToolBar.add(closeButton);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane.setViewportView(tableSmallSearch.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        dialogBorderLayout = new BorderLayout();
        getContentPane().setLayout(dialogBorderLayout);
        getContentPane().add(buttonToolBar, "North");
        getContentPane().add(tableScrPane, "Center");
        setSize(300, 400);
        addWindowListener(this);
    }

    public void setTableData(String type)
    {
        if(type.equals("storage.id"))
            try
            {
                ArrayList tempList = dss.listStorageId();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap storageInfo = dss.getStorage((String)tempList.get(i));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add((String)storageInfo.get("base.directory.path"));
                    tempList2.add((String)storageInfo.get("description"));
                    searchDataList.add(tempList2.clone());
                    tempList2.clear();
                }

                tableSmallSearch.changeTableData();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
    }

    public void closeEvent()
    {
        selectButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        tableSmallSearch.getTable().removeMouseListener(this);
        removeWindowListener(this);
        dispose();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = evt.getActionCommand();
        if(command == null)
            closeEvent();
        else
        if(command.equals("Select"))
        {
            String storageId = (String)selectedRows.get(0);
            dssInfo.descriptionTextField.setText(storageId);
            closeEvent();
        } else
        if(command.equals("Close"))
            closeEvent();
    }

    public void windowClosing(WindowEvent windowevent)
    {
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
        closeEvent();
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void mousePressed(MouseEvent mouseEvent)
    {
        selectedRows = tableSmallSearch.getSelectedRow();
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

    private AUS aus;
    private DSS dss;
    private DSSInformation dssInfo;
    private BorderLayout dialogBorderLayout;
    private JPanel mainPanel;
    private JScrollPane tableScrPane;
    private Table tableSmallSearch;
    private JToolBar buttonToolBar;
    private JButton selectButton;
    private JButton closeButton;
    private ArrayList searchDataList;
    private ArrayList columnNameList;
    private ArrayList columnWidthList;
    private ArrayList selectedRows;
    private String objectId;
    private String objectType;
}
