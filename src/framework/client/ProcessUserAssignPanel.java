// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcessUserAssignPanel.java

package dyna.framework.client;

import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.WFM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.uic.graph.Node;
import dyna.util.Utils;
import java.awt.Cursor;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, CreateProcess, ProcessWorkflowPanel

public class ProcessUserAssignPanel extends MInternalFrame
    implements ActionListener, MouseListener
{

    public ProcessUserAssignPanel()
    {
        super(DynaMOAD.getMSRString("WRD_0144", "Assign User", 3));
        createProcess = null;
        handCursor = new Cursor(12);
        selectedActivityOuid = null;
        selectedActivity = null;
        mainToolBar = null;
        closeViewButton = null;
        tabPane = null;
        templatePane = null;
        userPane = null;
        groupTable = null;
        groupList = new ArrayList();
        userTable = null;
        userList = new ArrayList();
        initComponents();
    }

    private void initComponents()
    {
        tabPane = new JTabbedPane();
        tabPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        tabPane.setTabPlacement(3);
        mainToolBar = new ExtToolBar("assign user contents");
        mainToolBar.setOpaque(false);
        closeViewButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/close_view.gif")));
        closeViewButton.setActionCommand("closeViewButton");
        closeViewButton.addActionListener(this);
        closeViewButton.setOpaque(false);
        mainToolBar.add(closeViewButton);
        setToolBar(mainToolBar);
        templatePane = buildTemplatePane();
        userPane = buildUserPane();
        tabPane.addTab(DynaMOAD.getMSRString("WRD_0025", "User", 3), userPane);
        setContent(tabPane);
    }

    public void setCreateProcess(CreateProcess instance)
    {
        createProcess = instance;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("closeViewButton") && createProcess != null)
            createProcess.setEnableAssignView(false);
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(groupTable.getTable()))
        {
            if(groupTable.getTable().getSelectedRowCount() > 0)
                refreshUserList();
        } else
        if(source.equals(userTable.getTable()))
        {
            if(createProcess == null || groupTable.getTable().getSelectedRowCount() == 0)
                return;
            if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e))
            {
                ProcessWorkflowPanel panel = (ProcessWorkflowPanel)createProcess.getParticipantsPanel();
                if(panel != null && panel.isUserAssignableActivity(panel.getSelectedNode()))
                {
                    ArrayList tempList1 = new ArrayList();
                    String userId = null;
                    int row = userTable.getTable().getSelectedRow();
                    String ouidRow = userTable.getSelectedOuidRow(row);
                    if(Utils.isNullString(ouidRow))
                        userId = (String)((ArrayList)userList.get(row)).get(0);
                    else
                        userId = (String)((ArrayList)userList.get((new Integer(ouidRow)).intValue())).get(0);
                    tempList1.add(userId);
                    panel.setDialogReturnValue(tempList1);
                    tempList1.clear();
                    tempList1 = null;
                    panel = null;
                }
            }
        }
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private JScrollPane buildTemplatePane()
    {
        JScrollPane scrollPane = null;
        scrollPane = UIFactory.createStrippedScrollPane(null);
        return scrollPane;
    }

    private JSplitPane buildUserPane()
    {
        JSplitPane splitPane = null;
        splitPane = UIFactory.createStrippedSplitPane(1, buildGroupTable(), buildUserTable(), 0.44999998807907104D);
        splitPane.setDividerSize(2);
        return splitPane;
    }

    private JScrollPane buildGroupTable()
    {
        JScrollPane scrollPane = UIFactory.createStrippedScrollPane(null);
        ArrayList headerList = new ArrayList();
        ArrayList columnList = new ArrayList();
        headerList.add(DynaMOAD.getMSRString("WRD_0148", "Group ID", 3));
        headerList.add(DynaMOAD.getMSRString("WRD_0103", "Name", 3));
        columnList.add(new Integer(80));
        columnList.add(new Integer(160));
        groupTable = new Table(groupList, headerList, columnList, 0);
        groupTable.setIndexColumn(0);
        groupTable.setColumnSequence(new int[] {
            0, 1
        });
        groupTable.getTable().addMouseListener(this);
        groupTable.getTable().setCursor(handCursor);
        scrollPane.setViewportView(groupTable.getTable());
        scrollPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        refreshGroupList();
        return scrollPane;
    }

    private void refreshGroupList()
    {
        ArrayList tempList = null;
        ArrayList tempList2 = null;
        HashMap tempMap = null;
        String tempString = null;
        groupList.clear();
        try
        {
            tempList = DynaMOAD.aus.listGroup();
            if(Utils.isNullArrayList(tempList))
                return;
            Iterator listKey1;
            for(listKey1 = tempList.iterator(); listKey1.hasNext();)
            {
                tempString = (String)listKey1.next();
                if(!Utils.isNullString(tempString) && !tempString.startsWith("SYSTEM."))
                {
                    tempMap = DynaMOAD.aus.getGroup(tempString);
                    if("CRT".equals(tempMap.get("status")))
                    {
                        tempList2 = new ArrayList();
                        tempList2.add(tempString);
                        tempList2.add(tempMap.get("description"));
                        groupList.add(tempList2);
                        tempList2 = null;
                    }
                    tempMap.clear();
                    tempMap = null;
                }
            }

            listKey1 = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        groupTable.changeTableData();
    }

    private JScrollPane buildUserTable()
    {
        JScrollPane scrollPane = UIFactory.createStrippedScrollPane(null);
        ArrayList headerList = new ArrayList();
        ArrayList columnList = new ArrayList();
        headerList.add(DynaMOAD.getMSRString("userID", "User ID", 3));
        headerList.add(DynaMOAD.getMSRString("WRD_0103", "Name", 3));
        headerList.add(DynaMOAD.getMSRString("WRD_0147", "Description", 3));
        columnList.add(new Integer(80));
        columnList.add(new Integer(80));
        columnList.add(new Integer(120));
        userTable = new Table(userList, headerList, columnList, 0);
        userTable.setIndexColumn(0);
        userTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        userTable.getTable().addMouseListener(this);
        userTable.getTable().setCursor(handCursor);
        scrollPane.setViewportView(userTable.getTable());
        scrollPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        return scrollPane;
    }

    public void refreshUserList()
    {
        ArrayList tempList = null;
        ArrayList tempList2 = null;
        HashMap tempMap = null;
        String tempString = null;
        String groupId = null;
        ArrayList roleUserList = null;
        userList.clear();
        try
        {
            if(groupTable.getTable().getSelectedRowCount() == 0)
            {
                userTable.changeTableData();
                return;
            }
            int row = groupTable.getTable().getSelectedRow();
            String ouidRow = groupTable.getSelectedOuidRow(row);
            if(ouidRow == null)
                groupId = (String)((ArrayList)groupList.get(row)).get(0);
            else
                groupId = (String)((ArrayList)groupList.get((new Integer(ouidRow)).intValue())).get(0);
            tempList = DynaMOAD.aus.listMembersOfGroup(groupId);
            if(Utils.isNullArrayList(tempList))
                return;
            Node node = ((ProcessWorkflowPanel)createProcess.getParticipantsPanel()).getSelectedNode();
            if(node == null)
                return;
            selectedActivity = DynaMOAD.wfm.getActivity(node.getOid());
            String type = (String)selectedActivity.get("type");
            if(type.equals("120"))
            {
                roleUserList = DynaMOAD.aus.listUsersOfRole("SYSTEM.REVIEW");
                for(int i = 0; i < roleUserList.size(); i++)
                    if(!tempList.contains(roleUserList.get(i)))
                    {
                        roleUserList.remove(i);
                        i--;
                    }

            } else
            if(type.equals("130"))
            {
                roleUserList = DynaMOAD.aus.listUsersOfRole("SYSTEM.APPROVAL");
                for(int i = 0; i < roleUserList.size(); i++)
                    if(!tempList.contains(roleUserList.get(i)))
                    {
                        roleUserList.remove(i);
                        i--;
                    }

            } else
            {
                roleUserList = tempList;
            }
            Iterator listKey1;
            for(listKey1 = roleUserList.iterator(); listKey1.hasNext();)
            {
                tempString = (String)listKey1.next();
                if(!Utils.isNullString(tempString) && !tempString.startsWith("SYSTEM.") && DynaMOAD.aus.isExistUser(tempString))
                {
                    tempMap = DynaMOAD.aus.getUser(tempString);
                    if("CRT".equals(tempMap.get("status")))
                    {
                        tempList2 = new ArrayList();
                        tempList2.add(tempString);
                        tempList2.add(tempMap.get("name"));
                        tempList2.add(tempMap.get("description"));
                        userList.add(tempList2);
                        tempList2 = null;
                    }
                    tempMap.clear();
                    tempMap = null;
                }
            }

            listKey1 = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        userTable.changeTableData();
    }

    private CreateProcess createProcess;
    private Cursor handCursor;
    private String selectedActivityOuid;
    private DOSChangeable selectedActivity;
    private JToolBar mainToolBar;
    private JButton closeViewButton;
    private JTabbedPane tabPane;
    private JScrollPane templatePane;
    private JSplitPane userPane;
    private Table groupTable;
    private ArrayList groupList;
    private Table userTable;
    private ArrayList userList;
}