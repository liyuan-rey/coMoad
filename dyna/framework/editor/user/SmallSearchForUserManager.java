// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SmallSearchForUserManager.java

package dyna.framework.editor.user;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.uic.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.user:
//            UserManager

public class SmallSearchForUserManager extends JDialog
    implements ActionListener, MouseListener, WindowListener
{

    public SmallSearchForUserManager(UserManager parent, String sourceType, String targetType, String targetObjectId)
    {
        super(parent, "Select Window", true);
        aus = null;
        selectedObjs = new ArrayList();
        dialogBorderLayout = null;
        mainPanel = null;
        tableScrPane = null;
        tableSmallSearch = null;
        buttonPanel = null;
        selectButton = null;
        closeButton = null;
        searchDataList = null;
        columnNameList = null;
        columnWidthList = null;
        selectedRows = null;
        this.sourceType = null;
        this.targetType = null;
        this.targetObjectId = null;
        this.parent = null;
        this.parent = parent;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.targetObjectId = targetObjectId;
        aus = UserManager.aus;
        makeSearchTable();
        initialize();
        setTableData(targetType);
        UIUtils.setLocationRelativeTo(this, parent);
    }

    public void makeSearchTable()
    {
        searchDataList = new ArrayList();
        columnNameList = new ArrayList();
        columnWidthList = new ArrayList();
        columnNameList.add("ID");
        columnNameList.add("Name/Status");
        columnNameList.add("Description");
        columnWidthList.add(new Integer(160));
        columnWidthList.add(new Integer(160));
        columnWidthList.add(new Integer(100));
        tableSmallSearch = new Table(searchDataList, columnNameList, columnWidthList, 1);
        tableSmallSearch.setColumnSequence(new int[] {
            1, 2, 3
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
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(selectButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(closeButton);
        buttonPanel.add(Box.createHorizontalGlue());
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane.setViewportView(tableSmallSearch.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        dialogBorderLayout = new BorderLayout();
        getContentPane().setLayout(dialogBorderLayout);
        getContentPane().add(buttonPanel, "North");
        getContentPane().add(tableScrPane, "Center");
        setSize(300, 400);
        addWindowListener(this);
    }

    public void setTableData(String type)
    {
        if(type.equals("user"))
            try
            {
                ArrayList tempList = aus.listUser();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap userInfo = aus.getUser((String)tempList.get(i));
                    tempList2.add((String)userInfo.get("ouid"));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add((String)userInfo.get("name"));
                    tempList2.add((String)userInfo.get("description"));
                    searchDataList.add(tempList2.clone());
                    tempList2.clear();
                }

                tableSmallSearch.changeTableData();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(type.equals("role"))
            try
            {
                ArrayList tempList = aus.listRole();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap roleInfo = aus.getRole((String)tempList.get(i));
                    tempList2.add((String)roleInfo.get("ouid"));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add((String)roleInfo.get("status"));
                    tempList2.add((String)roleInfo.get("description"));
                    searchDataList.add(tempList2.clone());
                    tempList2.clear();
                }

                tableSmallSearch.changeTableData();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(type.equals("group"))
            try
            {
                ArrayList tempList = aus.listGroup();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap groupInfo = aus.getGroup((String)tempList.get(i));
                    tempList2.add((String)groupInfo.get("ouid"));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add((String)groupInfo.get("status"));
                    tempList2.add((String)groupInfo.get("description"));
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
        System.out.println("SmallSearchForUserManager : actionPerformed start");
        String command = evt.getActionCommand();
        if(command == null)
            closeEvent();
        else
        if(command.equals("Select"))
        {
            if(sourceType.equals("user") && selectedRows != null)
                try
                {
                    if(targetObjectId == null)
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                            selectedObjs.add(((ArrayList)selectedRows.get(i)).get(1));

                    } else
                    if(targetType.equals("role") && selectedRows != null)
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                        {
                            String roleId = (String)((ArrayList)selectedRows.get(i)).get(1);
                            boolean flag = aus.linkRoleToUser(roleId, targetObjectId);
                        }

                    } else
                    if(targetType.equals("group") && selectedRows != null)
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                        {
                            String groupId = (String)((ArrayList)selectedRows.get(i)).get(1);
                            boolean flag1 = aus.linkUserToGroup(targetObjectId, groupId);
                        }

                    }
                    closeEvent();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(sourceType.equals("group"))
                try
                {
                    if(targetObjectId == null)
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                            selectedObjs.add(((ArrayList)selectedRows.get(i)).get(1));

                    } else
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                        {
                            String userId = (String)((ArrayList)selectedRows.get(i)).get(1);
                            boolean flag2 = aus.linkUserToGroup(userId, targetObjectId);
                        }

                    }
                    closeEvent();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(sourceType.equals("role"))
                try
                {
                    if(targetObjectId == null)
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                            selectedObjs.add(((ArrayList)selectedRows.get(i)).get(1));

                    } else
                    {
                        for(int i = 0; i < selectedRows.size(); i++)
                        {
                            String userId = (String)((ArrayList)selectedRows.get(i)).get(1);
                            boolean flag3 = aus.linkRoleToUser(targetObjectId, userId);
                        }

                    }
                    closeEvent();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("Close"))
            closeEvent();
    }

    public ArrayList getSelectedObject()
    {
        return selectedObjs;
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
        selectedRows = tableSmallSearch.getSelectedRows();
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
    private ArrayList selectedObjs;
    private BorderLayout dialogBorderLayout;
    private JPanel mainPanel;
    private JScrollPane tableScrPane;
    private Table tableSmallSearch;
    private JPanel buttonPanel;
    private JButton selectButton;
    private JButton closeButton;
    private ArrayList searchDataList;
    private ArrayList columnNameList;
    private ArrayList columnWidthList;
    private ArrayList selectedRows;
    private String sourceType;
    private String targetType;
    private String targetObjectId;
    private UserManager parent;
}
