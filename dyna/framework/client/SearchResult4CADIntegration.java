// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SearchResult4CADIntegration.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.ACL;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTable;
import dyna.uic.JTreeTable;
import dyna.uic.MInternalFrame;
import dyna.uic.Table;
import dyna.uic.TableSortDecorator;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, ClientUtil, Search4CADIntegration, LogIn, 
//            UIManagement, SearchCondition4CADIntegration, UIBuilder, UIGeneration, 
//            CreateProcess, CheckOut, CheckIn, AdvancedSearchResult

public class SearchResult4CADIntegration extends JPanel
    implements ActionListener, MouseListener
{
    class PopupMenu extends MouseAdapter
    {

        public void mouseClicked(MouseEvent e)
        {
            int row = searchResultTable.getTable().getSelectedRow();
            String ouidRow = searchResultTable.getSelectedOuidRow(row);
            if(ouidRow != null)
                searchResultTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        }

        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isRightMouseButton(e) && !e.isShiftDown() && !e.isControlDown() && !e.isAltDown())
            {
                Point point = new Point(e.getX(), e.getY());
                if(e.getSource() instanceof JTreeTable)
                {
                    int index = ((JTreeTable)e.getSource()).rowAtPoint(point);
                    ((DefaultListSelectionModel)((JTreeTable)e.getSource()).getSelectionModel()).setSelectionInterval(index, index);
                } else
                if(e.getSource() instanceof DynaTable)
                {
                    int index = ((DynaTable)e.getSource()).rowAtPoint(point);
                    ((DefaultListSelectionModel)((DynaTable)e.getSource()).getSelectionModel()).setSelectionInterval(index, index);
                }
            }
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger() && SwingUtilities.isRightMouseButton(e) && !e.isControlDown())
            {
                Table table = searchResultTable;
                int selectedRows[] = table.getTable().getSelectedRows();
                int row = searchResultTable.getTable().getSelectedRow();
                String ouidRow = searchResultTable.getSelectedOuidRow(row);
                if(searchResultTable.getSelectedRowNumber() > -1)
                {
                    ArrayList ouidList = getSelectedOuids();
                    String instanceOuid = (String)ouidList.get(ouidList.size() - 1);
                    try
                    {
                        classOuid = dos.getClassOuid(instanceOuid);
                        if(Utils.isNullString(classOuid))
                            return;
                        ArrayList assoList = UIGeneration.listAssociations(classOuid);
                        if(Utils.isNullArrayList(assoList))
                            assoList = UIGeneration.listAssociations(classOuid, Boolean.FALSE);
                        popup = UIBuilder.createPopupMenuForSearchResult(instanceOuid, ouidList, assoList, e.getComponent(), e.getX(), e.getY(), SearchResult4CADIntegration.this, modelOuid);
                    }
                    catch(IIPRequestException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }
        }

        PopupMenu()
        {
        }
    }


    public SearchResult4CADIntegration(JFrame parentFrame)
    {
        wfm = null;
        clientUtil = null;
        popup = null;
        orderOfOuid = new DOSChangeable();
        _session = null;
        handCursor = new Cursor(12);
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        dss = DynaMOAD.dss;
        wfm = DynaMOAD.wfm;
        acl = DynaMOAD.acl;
        newUI = DynaMOAD.newUI;
        this.parentFrame = parentFrame;
        clientUtil = new ClientUtil();
        setBackground(new Color(223, 216, 206));
        try
        {
            modelOuid = dos.getWorkingModel();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initialize()
    {
        resultBorderLayout = new BorderLayout();
        setLayout(resultBorderLayout);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        if(searchResultTable != null)
            tableScrPane.setViewportView(searchResultTable.getTable());
        tableScrPane.getViewport().setBackground(new Color(223, 216, 206));
        add(tableScrPane, "Center");
        if(searchResultTable != null)
        {
            MouseListener PmousePopup = new PopupMenu();
            searchResultTable.getTable().addMouseListener(PmousePopup);
            decorator = new TableSortDecorator(searchResultTable.getTable().getModel());
            tableScrPane.addMouseListener(PmousePopup);
            orderOfOuid = searchResultTable.setOuidOrder();
        }
        if(parentFrame instanceof Search4CADIntegration)
        {
            ((Search4CADIntegration)parentFrame).searchResultFrame.setContent(this);
            ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
        }
    }

    public void makeSearchResultTable(String lcClassOuid)
    {
        try
        {
            classOuid = lcClassOuid;
            removeAll();
            tableScrPane = null;
            updateUI();
            DOSChangeable fieldData = null;
            String fieldOuid = null;
            String fieldCode = null;
            String fieldTitle = null;
            ArrayList fieldOuidList = null;
            int size = 0;
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
            if(fieldColumn != null)
            {
                size = fieldColumn.size();
                selectedRows = new int[size];
                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                fieldOuidList = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    String test = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)fieldColumn.get(i));
                    fieldData = dos.getField(test);
                    fieldOuid = (String)fieldData.get("ouid");
                    fieldCode = (String)fieldData.get("code");
                    fieldTitle = DynaMOAD.getMSRString(fieldCode, (String)fieldData.get("title"), 0);
                    fieldOuidList.add(fieldOuid);
                    columnName.add(fieldTitle);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            } else
            {
                ArrayList fieldList = makeDefaultFieldTable(classOuid);
                if(fieldList == null)
                    return;
                size = fieldList.size();
                selectedRows = new int[size];
                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                fieldOuidList = new ArrayList();
                for(int i = 0; i < size; i++)
                {
                    fieldData = (DOSChangeable)fieldList.get(i);
                    fieldOuid = (String)fieldData.get("ouid");
                    fieldCode = (String)fieldData.get("code");
                    fieldTitle = DynaMOAD.getMSRString(fieldCode, (String)fieldData.get("title"), 0);
                    fieldOuidList.add(fieldOuid);
                    columnName.add(fieldTitle);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            }
            searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), -1, 79);
            searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            searchResultTable.setColumnSequence(selectedRows);
            searchResultTable.setIndexColumn(0);
            searchResultTable.getTable().addMouseListener(this);
            searchResultTable.getTable().setCursor(handCursor);
            initialize();
            if(parentFrame instanceof Search4CADIntegration)
                ((Search4CADIntegration)parentFrame).searchCondition4CADIntegration.setSearchResultField(fieldOuidList);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void makeSearchResultTableForWorkSpace(String lcClassOuid)
    {
        try
        {
            classOuidForWorkSpace = lcClassOuid;
            searchData = new ArrayList();
            columnName = new ArrayList();
            columnWidth = new ArrayList();
            if(classOuidForWorkSpace.equals("Recent"))
            {
                removeAll();
                tableScrPane = null;
                updateUI();
                ArrayList recentList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT");
                if(recentList != null)
                {
                    for(int i = 0; i < recentList.size(); i++)
                    {
                        String recentOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                        if(recentOuid != null)
                        {
                            DOSChangeable getDC = dos.get(recentOuid);
                            if(getDC != null)
                            {
                                String tmpStr = dos.getClassOuid(recentOuid);
                                DOSChangeable getClassInfo = dos.getClass(tmpStr);
                                ArrayList searchDataClone = new ArrayList();
                                searchDataClone.add(getDC.get("ouid"));
                                searchDataClone.add(getClassInfo.get("title"));
                                searchDataClone.add(getDC.get("md$number"));
                                searchDataClone.add(getDC.get("md$user"));
                                searchData.add(searchDataClone.clone());
                                searchDataClone = null;
                                getDC.clear();
                                getClassInfo.clear();
                            }
                        }
                    }

                }
            } else
            if(classOuidForWorkSpace.equals("Favorites"))
            {
                removeAll();
                tableScrPane = null;
                updateUI();
                ArrayList favoriteList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE");
                if(favoriteList != null)
                {
                    for(int i = 0; i < favoriteList.size(); i++)
                    {
                        String recentOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE/" + (new Integer(i + 1)).toString());
                        DOSChangeable getDC = dos.get(recentOuid);
                        String tmpStr = dos.getClassOuid(recentOuid);
                        DOSChangeable getClassInfo = dos.getClass(tmpStr);
                        ArrayList searchDataClone = new ArrayList();
                        searchDataClone.add(getDC.get("ouid"));
                        searchDataClone.add(getClassInfo.get("title"));
                        searchDataClone.add(getDC.get("md$number"));
                        searchDataClone.add(getDC.get("md$user"));
                        searchData.add(searchDataClone.clone());
                        getDC.clear();
                        getClassInfo.clear();
                    }

                }
            } else
            if(classOuidForWorkSpace.equals("Inbox"))
            {
                removeAll();
                tableScrPane = null;
                updateUI();
                ArrayList inboxList = wfm.listInboxProcessByUser(LogIn.userID);
                DOSChangeable aRow = null;
                ArrayList searchDataClone = new ArrayList();
                if(inboxList != null)
                {
                    for(int i = 0; i < inboxList.size(); i++)
                    {
                        aRow = (DOSChangeable)inboxList.get(i);
                        searchDataClone.add(aRow.get("ouid@process"));
                        searchDataClone.add(aRow.get("ouid@object"));
                        searchDataClone.add(aRow.get("name@process"));
                        searchDataClone.add(aRow.get("md$number"));
                        searchDataClone.add(aRow.get("md$description"));
                        searchDataClone.add(aRow.get("ouid@workflow.status"));
                        searchDataClone.add(aRow.get("name@workflow.status"));
                        searchDataClone.add(aRow.get("date.receipt"));
                        searchDataClone.add(aRow.get("name.user") + " (" + aRow.get("id.user") + ")");
                        searchDataClone.add(aRow.get("date.created"));
                        searchDataClone.add(aRow.get("date.limit.to.finish"));
                        searchDataClone.add(aRow.get("name@activity"));
                        searchDataClone.add(aRow.get("read"));
                        searchDataClone.add(aRow.get("date.closed"));
                        searchDataClone.add(aRow.get("finished"));
                        searchData.add(searchDataClone.clone());
                        searchDataClone.clear();
                    }

                    inboxList.clear();
                    inboxList = null;
                }
            } else
            if(classOuidForWorkSpace.equals("Sent"))
            {
                removeAll();
                tableScrPane = null;
                updateUI();
                ArrayList sentList = wfm.listSentProcessByUser(LogIn.userID);
                DOSChangeable aRow = null;
                ArrayList searchDataClone = new ArrayList();
                if(sentList != null)
                {
                    for(int i = 0; i < sentList.size(); i++)
                    {
                        aRow = (DOSChangeable)sentList.get(i);
                        searchDataClone.add(aRow.get("ouid@process"));
                        searchDataClone.add(aRow.get("ouid@object"));
                        searchDataClone.add(aRow.get("name@process"));
                        searchDataClone.add(aRow.get("md$number"));
                        searchDataClone.add(aRow.get("md$description"));
                        searchDataClone.add(aRow.get("ouid@workflow.status"));
                        searchDataClone.add(aRow.get("name@workflow.status"));
                        searchDataClone.add(aRow.get("date.sent"));
                        searchDataClone.add(aRow.get("name.user") + " (" + aRow.get("id.user") + ")");
                        searchDataClone.add(aRow.get("date.created"));
                        searchDataClone.add(aRow.get("date.closed"));
                        searchDataClone.add(aRow.get("finished"));
                        searchData.add(searchDataClone.clone());
                        searchDataClone.clear();
                    }

                    sentList.clear();
                    sentList = null;
                }
            } else
            if(classOuidForWorkSpace.equals("Completed"))
            {
                removeAll();
                tableScrPane = null;
                updateUI();
                ArrayList inboxList = wfm.listCompletedProcessByUser(LogIn.userID);
                DOSChangeable aRow = null;
                ArrayList searchDataClone = new ArrayList();
                if(inboxList != null)
                {
                    for(int i = 0; i < inboxList.size(); i++)
                    {
                        aRow = (DOSChangeable)inboxList.get(i);
                        searchDataClone.add(aRow.get("ouid@process"));
                        searchDataClone.add(aRow.get("ouid@object"));
                        searchDataClone.add(aRow.get("name@process"));
                        searchDataClone.add(aRow.get("md$number"));
                        searchDataClone.add(aRow.get("md$description"));
                        searchDataClone.add(aRow.get("ouid@workflow.status"));
                        searchDataClone.add(aRow.get("name@workflow.status"));
                        searchDataClone.add(aRow.get("date.receipt"));
                        searchDataClone.add(aRow.get("name.user") + " (" + aRow.get("id.user") + ")");
                        searchDataClone.add(aRow.get("date.created"));
                        searchDataClone.add(aRow.get("date.limit.to.finish"));
                        searchDataClone.add(aRow.get("name@activity"));
                        searchDataClone.add(aRow.get("read"));
                        searchDataClone.add(aRow.get("date.closed"));
                        searchDataClone.add(aRow.get("finished"));
                        searchData.add(searchDataClone.clone());
                        searchDataClone.clear();
                    }

                    inboxList.clear();
                    inboxList = null;
                }
            }
            if(classOuidForWorkSpace.equals("Favorites") || classOuidForWorkSpace.equals("Recent"))
            {
                if(searchData.size() > 0)
                {
                    columnName.add(DynaMOAD.getMSRString("WRD_0028", "Class", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0025", "User", 3));
                    selectedRows = new int[3];
                    for(int i = 0; i < 3; i++)
                    {
                        columnWidth.add(new Integer(100));
                        selectedRows[i] = i + 1;
                    }

                    searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
                    searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                    searchResultTable.setColumnSequence(selectedRows);
                    searchResultTable.setIndexColumn(0);
                    searchResultTable.getTable().addMouseListener(this);
                    searchResultTable.getTable().setCursor(handCursor);
                    initialize();
                }
                if(parentFrame instanceof Search4CADIntegration)
                    ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (" + searchData.size() + ")");
            } else
            if(classOuidForWorkSpace.equals("Inbox"))
            {
                if(searchData.size() > 0)
                {
                    columnName.add(DynaMOAD.getMSRString("WRD_0029", "Read", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0030", "Activity Finished", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0031", "Process", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0032", "Activity", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0033", "Responsible", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0034", "Created", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0035", "Receipt", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0036", "Due Date", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0037", "Finished", 3));
                    selectedRows = (new int[] {
                        12, 14, 2, 11, 3, 4, 8, 6, 9, 7, 
                        10, 13
                    });
                    columnWidth.add(new Integer(20));
                    columnWidth.add(new Integer(20));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, -1);
                    searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                    searchResultTable.setColumnSequence(selectedRows);
                    searchResultTable.setIndexColumn(0);
                    searchResultTable.getTable().addMouseListener(this);
                    searchResultTable.getTable().setCursor(handCursor);
                    initialize();
                }
                if(parentFrame instanceof Search4CADIntegration)
                    ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (" + searchData.size() + ")");
            } else
            if(classOuidForWorkSpace.equals("Sent"))
            {
                if(searchData.size() > 0)
                {
                    columnName.add(DynaMOAD.getMSRString("WRD_0038", "Process Finished", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0031", "Process", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0033", "Responsible", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0034", "Created", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0039", "Sent", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0037", "Finished", 3));
                    selectedRows = (new int[] {
                        11, 2, 3, 4, 8, 6, 9, 7, 10
                    });
                    columnWidth.add(new Integer(20));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, -1);
                    searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                    searchResultTable.setColumnSequence(selectedRows);
                    searchResultTable.setIndexColumn(0);
                    searchResultTable.getTable().addMouseListener(this);
                    searchResultTable.getTable().setCursor(handCursor);
                    initialize();
                }
                if(parentFrame instanceof Search4CADIntegration)
                    ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (" + searchData.size() + ")");
            } else
            if(classOuidForWorkSpace.equals("Completed"))
            {
                if(searchData.size() > 0)
                {
                    columnName.add(DynaMOAD.getMSRString("WRD_0029", "Read", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0030", "Activity Finished", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0031", "Process", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0032", "Activity", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0033", "Responsible", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0034", "Created", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0035", "Receipt", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0036", "Due Date", 3));
                    columnName.add(DynaMOAD.getMSRString("WRD_0037", "Finished", 3));
                    selectedRows = (new int[] {
                        12, 14, 2, 11, 3, 4, 8, 6, 9, 7, 
                        10, 13
                    });
                    columnWidth.add(new Integer(20));
                    columnWidth.add(new Integer(20));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(70));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    columnWidth.add(new Integer(80));
                    searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, -1);
                    searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
                    searchResultTable.setColumnSequence(selectedRows);
                    searchResultTable.setIndexColumn(0);
                    searchResultTable.getTable().addMouseListener(this);
                    searchResultTable.getTable().setCursor(handCursor);
                    initialize();
                }
                if(parentFrame instanceof Search4CADIntegration)
                    ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (" + searchData.size() + ")");
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setTableInfo(String lcClassOuid)
    {
        try
        {
            searchData = new ArrayList();
            columnName = new ArrayList();
            columnWidth = new ArrayList();
            ArrayList fieldListTmp = new ArrayList();
            fieldListTmp = UIBuilder.getDefaultFieldList(dos, lcClassOuid);
            Utils.sort(fieldListTmp);
            DOSChangeable fieldDOS = new DOSChangeable();
            int selectRow[] = new int[fieldListTmp.size()];
            for(int i = 0; i < fieldListTmp.size(); i++)
            {
                fieldDOS = (DOSChangeable)fieldListTmp.get(i);
                columnName.add(DynaMOAD.getMSRString((String)fieldDOS.get("code"), (String)fieldDOS.get("title"), 0));
                columnWidth.add(new Integer(80));
                selectRow[i] = i;
            }

            if(tableScrPane != null)
            {
                tableScrPane.getViewport().removeAll();
                removeAll();
                updateUI();
            }
            searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), -1, 79);
            searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            searchResultTable.setColumnSequence(selectRow);
            searchResultTable.setIndexColumn(0);
            searchResultTable.getTable().addMouseListener(this);
            searchResultTable.getTable().setCursor(handCursor);
            initialize();
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setTableInfo(DOSChangeable information, int size, int selectedRows[])
    {
        searchData = new ArrayList();
        columnName = new ArrayList();
        columnWidth = new ArrayList();
        String tmpStr = "";
        for(int i = 0; i < size; i++)
        {
            tmpStr = (String)information.get((new Integer(i)).toString());
            columnName.add(DynaMOAD.getMSRString(tmpStr, tmpStr, 3));
            columnWidth.add(new Integer(80));
        }

        if(tableScrPane != null)
        {
            tableScrPane.getViewport().removeAll();
            removeAll();
            updateUI();
        }
        searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), -1, 79);
        searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        searchResultTable.setColumnSequence(selectedRows);
        searchResultTable.setIndexColumn(0);
        searchResultTable.getTable().addMouseListener(this);
        searchResultTable.getTable().setCursor(handCursor);
        initialize();
    }

    public void setResultData(ArrayList resultData)
    {
        searchData.clear();
        ArrayList tmpList = new ArrayList();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                ArrayList tmpList2 = (ArrayList)resultData.get(i);
                for(int j = 0; j < tmpList2.size(); j++)
                    tmpList.add(tmpList2.get(j));

                searchData.add(tmpList.clone());
                tmpList.clear();
            }

        }
        searchResultTable.changeTableData();
        if(parentFrame instanceof Search4CADIntegration)
        {
            int pageNum = ((Search4CADIntegration)parentFrame).searchCondition4CADIntegration.getPageNumber();
            int cntPerPage = ((Search4CADIntegration)parentFrame).searchCondition4CADIntegration.getCountPerPage();
            if(resultData == null)
            {
                ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (0)");
                ((Search4CADIntegration)parentFrame).setEnabledPreviousSearchButton(false);
                ((Search4CADIntegration)parentFrame).setEnabledNextSearchButton(false);
            } else
            {
                ((Search4CADIntegration)parentFrame).searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (" + ((pageNum - 1) * cntPerPage + 1) + " ~ " + ((pageNum - 1) * cntPerPage + resultData.size()) + ")");
                if(cntPerPage <= 0 || pageNum == 1)
                    ((Search4CADIntegration)parentFrame).setEnabledPreviousSearchButton(false);
                else
                    ((Search4CADIntegration)parentFrame).setEnabledPreviousSearchButton(true);
                if(cntPerPage <= 0 || resultData.size() < cntPerPage)
                    ((Search4CADIntegration)parentFrame).setEnabledNextSearchButton(false);
                else
                    ((Search4CADIntegration)parentFrame).setEnabledNextSearchButton(true);
            }
        }
    }

    public ArrayList makeDefaultFieldTable(String lcClassOuid)
    {
        try
        {
            ArrayList fieldList = new ArrayList();
            DOSChangeable tempDos = null;
            ArrayList tmpList = UIBuilder.getDefaultFieldList(dos, lcClassOuid);
            if(tmpList == null)
                return null;
            Utils.sort(tmpList);
            for(int i = 0; i < tmpList.size(); i++)
            {
                tempDos = (DOSChangeable)tmpList.get(i);
                if(Utils.getBoolean((Boolean)tempDos.get("is.visible")))
                    fieldList.add(tempDos);
            }

            return fieldList;
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        return null;
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            int row = searchResultTable.getTable().getSelectedRow();
            String ouidRow = searchResultTable.getSelectedOuidRow(row);
            if(!Utils.isNullString(ouidRow))
            {
                searchResultTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                this.selectOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
            } else
            {
                this.selectOuid = (String)((ArrayList)searchData.get((new Integer(row)).intValue())).get(0);
            }
            classOuid = dos.getClassOuid(this.selectOuid);
            String command = actionEvent.getActionCommand();
            if(command.equals("Information"))
            {
                String selectClassOuid = dos.getClassOuid(this.selectOuid);
                UIGeneration uiGeneration = new UIGeneration(this, selectClassOuid, this.selectOuid, 1);
                uiGeneration.setSession(_session);
                Object returnObject = Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.before"), dss, uiGeneration);
                if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                {
                    uiGeneration.windowClosing(null);
                } else
                {
                    uiGeneration.setVisible(true);
                    Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.after"), dss, uiGeneration);
                    ArrayList recentList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT");
                    nds.addNode("::", "WORKSPACE", "RECENT", "");
                    nds.addNode("::/WORKSPACE", LogIn.userID, "RECENT", "");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID, modelOuid, "RECENT", "");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid, "RECENT", "RECENT", "");
                    if(recentList != null)
                    {
                        if(recentList.size() < 5)
                        {
                            boolean isExist = false;
                            for(int i = 0; i < recentList.size(); i++)
                            {
                                String hoardedOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                if(hoardedOuid.equals(this.selectOuid))
                                    isExist = true;
                            }

                            if(!isExist)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", (new Integer(recentList.size() + 1)).toString(), "RECENT", this.selectOuid);
                        } else
                        if(recentList.size() == 5)
                        {
                            boolean isExist = false;
                            for(int i = 0; i < recentList.size(); i++)
                            {
                                String hoardedOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                if(hoardedOuid.equals(this.selectOuid))
                                    isExist = true;
                            }

                            if(!isExist)
                            {
                                for(int i = 0; i < recentList.size() - 1; i++)
                                {
                                    String tmpStr = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 2)).toString());
                                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", (new Integer(i + 1)).toString(), "RECENT", tmpStr);
                                }

                                nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(recentList.size())).toString());
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", "5", "RECENT", this.selectOuid);
                            }
                        }
                    } else
                    {
                        nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", "1", "RECENT", this.selectOuid);
                    }
                }
            } else
            if(command.startsWith("tree view"))
            {
                String tabName = command.substring(command.indexOf('/') + 1);
                String selectClassOuid = dos.getClassOuid(this.selectOuid);
                UIGeneration uiGeneration = new UIGeneration(this, selectClassOuid, this.selectOuid, 1);
                uiGeneration.setSession(_session);
                boolean results = uiGeneration.setTreeLinkView(tabName);
                if(!results)
                {
                    uiGeneration.setVisible(false);
                    uiGeneration.dispose();
                    uiGeneration = null;
                    return;
                }
                Object returnObject = Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.before"), dss, uiGeneration);
                if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                {
                    uiGeneration.windowClosing(null);
                } else
                {
                    uiGeneration.setVisible(true);
                    Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.after"), dss, uiGeneration);
                    ArrayList recentList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT");
                    nds.addNode("::", "WORKSPACE", "RECENT", "");
                    nds.addNode("::/WORKSPACE", LogIn.userID, "RECENT", "");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID, modelOuid, "RECENT", "");
                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid, "RECENT", "RECENT", "");
                    if(recentList != null)
                    {
                        if(recentList.size() < 5)
                        {
                            boolean isExist = false;
                            for(int i = 0; i < recentList.size(); i++)
                            {
                                String hoardedOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                if(hoardedOuid.equals(this.selectOuid))
                                    isExist = true;
                            }

                            if(!isExist)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", (new Integer(recentList.size() + 1)).toString(), "RECENT", this.selectOuid);
                        } else
                        if(recentList.size() == 5)
                        {
                            boolean isExist = false;
                            for(int i = 0; i < recentList.size(); i++)
                            {
                                String hoardedOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                if(hoardedOuid.equals(this.selectOuid))
                                    isExist = true;
                            }

                            if(!isExist)
                            {
                                for(int i = 0; i < recentList.size() - 1; i++)
                                {
                                    String tmpStr = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 2)).toString());
                                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", (new Integer(i + 1)).toString(), "RECENT", tmpStr);
                                }

                                nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(recentList.size())).toString());
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", "5", "RECENT", this.selectOuid);
                            }
                        }
                    } else
                    {
                        nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", "1", "RECENT", this.selectOuid);
                    }
                }
            } else
            if(command.startsWith("MNU_DELETE"))
            {
                java.util.List list = Utils.tokenizeMessage(command, '^');
                if(list.size() > 0)
                    command = (String)list.get(0);
                if(list.size() > 1)
                    this.selectOuid = (String)list.get(1);
                else
                    this.selectOuid = "";
                try
                {
                    int res = JOptionPane.showConfirmDialog(null, DynaMOAD.getMSRString("QST_0003", "Selected object will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 3), 0);
                    if(res != 0)
                        return;
                    if((parentFrame instanceof Search4CADIntegration) && ((Search4CADIntegration)parentFrame).searchCondition4CADIntegration.isPrivateWorkSpace)
                        if(classOuidForWorkSpace.equals("Recent"))
                        {
                            String recentKey = nds.findKeyValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", this.selectOuid);
                            nds.removeNode(recentKey);
                            ArrayList recentList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT");
                            for(int i = 0; i < recentList.size(); i++)
                            {
                                String tmpStr = (new Integer(i + 1)).toString();
                                if(!((String)recentList.get(i)).equals(tmpStr))
                                {
                                    String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (String)recentList.get(i));
                                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", tmpStr, "RECENT", value);
                                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (String)recentList.get(i));
                                }
                            }

                            makeSearchResultTableForWorkSpace("Recent");
                            searchResultTable.changeTableData();
                        } else
                        {
                            String recentKey = nds.findKeyValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE", this.selectOuid);
                            nds.removeNode(recentKey);
                            ArrayList recentList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE");
                            for(int i = 0; i < recentList.size(); i++)
                            {
                                String tmpStr = (new Integer(i + 1)).toString();
                                if(!((String)recentList.get(i)).equals(tmpStr))
                                {
                                    String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE/" + (String)recentList.get(i));
                                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE", tmpStr, "FAVORITE", value);
                                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE/" + (String)recentList.get(i));
                                }
                            }

                            makeSearchResultTableForWorkSpace("Favorites");
                            searchResultTable.changeTableData();
                        }
                    String selectedClassOuid = DynaMOAD.dos.getClassOuid(this.selectOuid);
                    Object returnObject = Utils.executeScriptFile(DynaMOAD.dos.getEventName(selectedClassOuid, "remove.before"), DynaMOAD.dss, this.selectOuid);
                    if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                    {
                        DynaMOAD.dos.remove(this.selectOuid);
                        Utils.executeScriptFile(DynaMOAD.dos.getEventName(selectedClassOuid, "remove.after"), DynaMOAD.dss, this.selectOuid);
                        if(parentFrame instanceof Search4CADIntegration)
                            ((Search4CADIntegration)parentFrame).searchCondition4CADIntegration.searchButton_actionPerformed(actionEvent);
                    }
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
            } else
            if(command.equals("copy"))
            {
                Table table = searchResultTable;
                int selectedRows[] = table.getTable().getSelectedRows();
                if(selectedRows == null && selectedRows.length < 1)
                    return;
                StringBuffer sb = new StringBuffer();
                String instanceOuid = null;
                ouidRow = null;
                try
                {
                    sb.append("[DynaMOAD Object];");
                    sb.append(selectedRows.length);
                    for(int n = 0; n < selectedRows.length; n++)
                    {
                        ouidRow = table.getSelectedOuidRow(selectedRows[n]);
                        if(ouidRow != null)
                            table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        if(ouidRow != null)
                            instanceOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
                        else
                            instanceOuid = (String)((ArrayList)searchData.get((new Integer(selectedRows[n])).intValue())).get(0);
                        sb.append(';');
                        sb.append(instanceOuid);
                    }

                    if(sb.length() > 0)
                        Utils.copyToClipboard(sb.toString());
                }
                catch(Exception re)
                {
                    re.printStackTrace();
                }
                selectedRows = (int[])null;
                table = null;
            } else
            if(command.startsWith("process/"))
            {
                java.util.List tokens = Utils.tokenizeMessage(command, '/');
                String procOuid = (String)tokens.get(1);
                ArrayList ouidList = getSelectedOuids();
                CreateProcess createProcess = new CreateProcess(0, procOuid, ouidList);
                createProcess = null;
                ouidList = null;
            } else
            if(command.startsWith("file/open"))
            {
                try
                {
                    java.util.List tempList = Utils.tokenizeMessage(command, '^');
                    HashMap fileInfoMap = new HashMap();
                    fileInfoMap.put("md$ouid", (String)tempList.get(1));
                    fileInfoMap.put("md$version", (String)tempList.get(2));
                    fileInfoMap.put("md$path", (String)tempList.get(3));
                    fileInfoMap.put("md$filetype.id", (String)tempList.get(4));
                    fileInfoMap.put("md$description", (String)tempList.get(5));
                    fileInfoMap.put("md$filetype.description", (String)tempList.get(6));
                    fileInfoMap.put("md$index", Integer.valueOf((String)tempList.get(7)));
                    CheckOut checkedOut = new CheckOut(null, false, fileInfoMap);
                    String fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
                    File downLoadFile = new File((String)fileInfoMap.get("md$description"));
                    String workingDirectory = System.getProperty("user.dir") + fileSeperator + "tmp" + fileSeperator + downLoadFile.getName();
                    checkedOut.setSession(_session);
                    checkedOut.setPreselectedFilePath(workingDirectory);
                    checkedOut.checkOutCheckBox.setSelected(false);
                    checkedOut.downloadCheckBox.setSelected(true);
                    checkedOut.invokeCheckBox.setSelected(true);
                    checkedOut.setReadOnlyModel(true);
                    checkedOut.processButton.doClick();
                    checkedOut.setSize(0, 0);
                    checkedOut.setVisible(true);
                    tempList.clear();
                    tempList = null;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(parentFrame instanceof Search4CADIntegration)
                    ((Search4CADIntegration)parentFrame).windowClosing(null);
            } else
            if(command.startsWith("file/checkout"))
            {
                try
                {
                    java.util.List tempList = Utils.tokenizeMessage(command, '^');
                    HashMap fileInfoMap = new HashMap();
                    fileInfoMap.put("md$ouid", (String)tempList.get(1));
                    fileInfoMap.put("md$version", (String)tempList.get(2));
                    fileInfoMap.put("md$path", (String)tempList.get(3));
                    fileInfoMap.put("md$filetype.id", (String)tempList.get(4));
                    fileInfoMap.put("md$description", (String)tempList.get(5));
                    fileInfoMap.put("md$filetype.description", (String)tempList.get(6));
                    fileInfoMap.put("md$index", Integer.valueOf((String)tempList.get(7)));
                    CheckOut checkedOut = new CheckOut(null, false, fileInfoMap);
                    checkedOut.setSession(_session);
                    checkedOut.setVisible(true);
                    tempList.clear();
                    tempList = null;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(parentFrame instanceof Search4CADIntegration)
                    ((Search4CADIntegration)parentFrame).windowClosing(null);
            } else
            if(command.startsWith("file/checkin"))
                try
                {
                    java.util.List tempList = Utils.tokenizeMessage(command, '^');
                    HashMap fileInfoMap = new HashMap();
                    fileInfoMap.put("md$ouid", (String)tempList.get(1));
                    fileInfoMap.put("md$version", (String)tempList.get(2));
                    fileInfoMap.put("md$path", (String)tempList.get(3));
                    fileInfoMap.put("md$filetype.id", (String)tempList.get(4));
                    fileInfoMap.put("md$description", (String)tempList.get(5));
                    fileInfoMap.put("md$filetype.description", (String)tempList.get(6));
                    fileInfoMap.put("md$index", Integer.valueOf((String)tempList.get(7)));
                    CheckIn checkIn = new CheckIn(null, false, fileInfoMap);
                    checkIn.setSession(_session);
                    checkIn.setVisible(true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            else
            if(command.startsWith("file/cancel_checkout"))
                try
                {
                    java.util.List tempList = Utils.tokenizeMessage(command, '^');
                    HashMap fileInfoMap = new HashMap();
                    fileInfoMap.put("md$ouid", (String)tempList.get(1));
                    fileInfoMap.put("md$version", (String)tempList.get(2));
                    fileInfoMap.put("md$path", (String)tempList.get(3));
                    fileInfoMap.put("md$filetype.id", (String)tempList.get(4));
                    fileInfoMap.put("md$description", (String)tempList.get(5));
                    fileInfoMap.put("md$filetype.description", (String)tempList.get(6));
                    fileInfoMap.put("md$index", Integer.valueOf((String)tempList.get(7)));
                    dos.cancelCheckoutFile((String)fileInfoMap.get("md$ouid"), fileInfoMap);
                    if(_session != null && (((Search4CADIntegration)parentFrame).cadType.equals("4") || ((Search4CADIntegration)parentFrame).cadType.equals("5")))
                    {
                        _session.setProperty("dynapdm.cadintegration.drawing", null);
                        _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                    }
                }
                catch(Exception ee)
                {
                    ee.printStackTrace();
                }
            else
            if(command.startsWith("action$"))
            {
                String scriptName = dos.getActionScriptName(command.substring(7));
                ArrayList ouidList = getSelectedOuids();
                if(!Utils.isNullString(scriptName))
                    Utils.executeScriptFile(scriptName, dss, ouidList);
            } else
            if(command.equals("file/multi_checkout"))
            {
                ArrayList ouidList = getSelectedOuids();
                ArrayList fileInfoList = new ArrayList();
                ArrayList fileList = null;
                HashMap file = null;
                String selectOuid = null;
                String checkedOutDate = null;
                String checkedInDate = null;
                String status = null;
                boolean isUpdatable = false;
                for(int i = 0; i < ouidList.size(); i++)
                {
                    selectOuid = (String)ouidList.get(i);
                    fileList = dos.listFile(selectOuid);
                    status = dos.getStatus(selectOuid);
                    isUpdatable = LogIn.isAdmin || acl.hasPermission(dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                    if(!Utils.isNullArrayList(fileList) && isUpdatable)
                    {
                        for(int j = 0; j < fileList.size(); j++)
                        {
                            file = (HashMap)fileList.get(j);
                            if("CRT".equals(status) || "WIP".equals(status))
                                isUpdatable = isUpdatable;
                            else
                                isUpdatable = false;
                            if(isUpdatable)
                            {
                                checkedOutDate = (String)file.get("md$checkedout.date");
                                checkedInDate = (String)file.get("md$checkedin.date");
                                if(!Utils.isNullString(checkedOutDate) && "null".equals(checkedOutDate))
                                    checkedOutDate = null;
                                if(!Utils.isNullString(checkedInDate) && "null".equals(checkedInDate))
                                    checkedInDate = null;
                                if(Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate))
                                    fileInfoList.add(file);
                                else
                                if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) <= 0)
                                    fileInfoList.add(file);
                            }
                        }

                    }
                }

                CheckOut checkedOut = new CheckOut(null, false, fileInfoList);
                checkedOut.setSession(_session);
                checkedOut.setVisible(true);
            } else
            if(command.equals("file/multi_checkin"))
            {
                ArrayList ouidList = getSelectedOuids();
                ArrayList fileInfoList = new ArrayList();
                ArrayList fileList = null;
                HashMap file = null;
                String selectOuid = null;
                String checkedOutDate = null;
                String checkedInDate = null;
                String status = null;
                boolean isUpdatable = false;
                for(int i = 0; i < ouidList.size(); i++)
                {
                    selectOuid = (String)ouidList.get(i);
                    fileList = dos.listFile(selectOuid);
                    status = dos.getStatus(selectOuid);
                    isUpdatable = LogIn.isAdmin || acl.hasPermission(dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                    if(!Utils.isNullArrayList(fileList) && isUpdatable)
                    {
                        for(int j = 0; j < fileList.size(); j++)
                        {
                            file = (HashMap)fileList.get(j);
                            if("CRT".equals(status) || "WIP".equals(status))
                                isUpdatable = isUpdatable;
                            else
                                isUpdatable = false;
                            if(isUpdatable)
                            {
                                checkedOutDate = (String)file.get("md$checkedout.date");
                                checkedInDate = (String)file.get("md$checkedin.date");
                                if(!Utils.isNullString(checkedOutDate) && "null".equals(checkedOutDate))
                                    checkedOutDate = null;
                                if(!Utils.isNullString(checkedInDate) && "null".equals(checkedInDate))
                                    checkedInDate = null;
                                if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) > 0)
                                    fileInfoList.add(file);
                            }
                        }

                    }
                }

                CheckIn checkIn = new CheckIn(null, false, fileInfoList);
                checkIn.setSession(_session);
                checkIn.setVisible(true);
            } else
            if(command.equals("file/multi_cancel_checkout"))
            {
                ArrayList ouidList = getSelectedOuids();
                ArrayList fileList = null;
                HashMap file = null;
                String selectOuid = null;
                String checkedOutDate = null;
                String checkedInDate = null;
                String status = null;
                boolean isUpdatable = false;
                for(int i = 0; i < ouidList.size(); i++)
                {
                    selectOuid = (String)ouidList.get(i);
                    fileList = dos.listFile(selectOuid);
                    status = dos.getStatus(selectOuid);
                    isUpdatable = LogIn.isAdmin || acl.hasPermission(dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                    if(!Utils.isNullArrayList(fileList) && isUpdatable)
                    {
                        for(int j = 0; j < fileList.size(); j++)
                        {
                            file = (HashMap)fileList.get(j);
                            if("CRT".equals(status) || "WIP".equals(status))
                                isUpdatable = isUpdatable;
                            else
                                isUpdatable = false;
                            if(isUpdatable)
                            {
                                checkedOutDate = (String)file.get("md$checkedout.date");
                                checkedInDate = (String)file.get("md$checkedin.date");
                                if(!Utils.isNullString(checkedOutDate) && "null".equals(checkedOutDate))
                                    checkedOutDate = null;
                                if(!Utils.isNullString(checkedInDate) && "null".equals(checkedInDate))
                                    checkedInDate = null;
                                if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) > 0)
                                    dos.cancelCheckoutFile((String)file.get("md$ouid"), file);
                            }
                        }

                    }
                }

                if(_session != null && (((Search4CADIntegration)parentFrame).cadType.equals("4") || ((Search4CADIntegration)parentFrame).cadType.equals("5")))
                {
                    _session.setProperty("dynapdm.cadintegration.drawing", null);
                    _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                }
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
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
        try
        {
            int row = searchResultTable.getTable().getSelectedRow();
            String ouidRow = searchResultTable.getSelectedOuidRow(row);
            if(ouidRow != null)
            {
                searchResultTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                selectOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
            } else
            {
                selectOuid = (String)((ArrayList)searchData.get((new Integer(row)).intValue())).get(0);
            }
            selectOuid = selectOuid.trim();
            classOuid = dos.getClassOuid(selectOuid);
            if(mouseEvent.getClickCount() == 2 && selectOuid != null)
                if(selectOuid.startsWith("wf$ipro"))
                {
                    String objectOuid = null;
                    if(ouidRow != null)
                        objectOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(1);
                    else
                        objectOuid = (String)((ArrayList)searchData.get((new Integer(row)).intValue())).get(1);
                    CreateProcess createProcess = new CreateProcess(1, selectOuid, objectOuid);
                    createProcess = null;
                } else
                {
                    boolean chk = false;
                    if(!LogIn.isAdmin)
                        chk = acl.hasPermission(classOuid, selectOuid, "READ", LogIn.userID);
                    else
                        chk = true;
                    if(chk)
                    {
                        String selectClassOuid = dos.getClassOuid(selectOuid);
                        UIGeneration uiGeneration = new UIGeneration(this, selectClassOuid, selectOuid, 1);
                        uiGeneration.setSession(_session);
                        Object returnObject = Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.before"), dss, uiGeneration);
                        if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                        {
                            uiGeneration.windowClosing(null);
                        } else
                        {
                            uiGeneration.setVisible(true);
                            Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.after"), dss, uiGeneration);
                            ArrayList recentList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT");
                            nds.addNode("::", "WORKSPACE", "RECENT", "");
                            nds.addNode("::/WORKSPACE", LogIn.userID, "RECENT", "");
                            nds.addNode("::/WORKSPACE/" + LogIn.userID, modelOuid, "RECENT", "");
                            nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid, "RECENT", "RECENT", "");
                            if(recentList != null)
                            {
                                if(recentList.size() < 5)
                                {
                                    boolean isExist = false;
                                    for(int i = 0; i < recentList.size(); i++)
                                    {
                                        String hoardedOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                        if(hoardedOuid.equals(selectOuid))
                                            isExist = true;
                                    }

                                    if(!isExist)
                                        nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", (new Integer(recentList.size() + 1)).toString(), "RECENT", selectOuid);
                                } else
                                if(recentList.size() == 5)
                                {
                                    boolean isExist = false;
                                    for(int i = 0; i < recentList.size(); i++)
                                    {
                                        String hoardedOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                        if(hoardedOuid.equals(selectOuid))
                                            isExist = true;
                                    }

                                    if(!isExist)
                                    {
                                        for(int i = 0; i < recentList.size() - 1; i++)
                                        {
                                            String tmpStr = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 2)).toString());
                                            nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(i + 1)).toString());
                                            nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", (new Integer(i + 1)).toString(), "RECENT", tmpStr);
                                        }

                                        nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT/" + (new Integer(recentList.size())).toString());
                                        nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", "5", "RECENT", selectOuid);
                                    }
                                }
                            } else
                            {
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT", "1", "RECENT", selectOuid);
                            }
                        }
                    }
                }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void setSession(Session session)
    {
        _session = session;
    }

    public void makeSearchResultTableForAdvancedFilter(String objectOuid)
    {
        try
        {
            ArrayList lcInputList = null;
            ArrayList lcRowList = null;
            String lcClassOuid = null;
            HashMap lcFieldMap = null;
            HashMap lcFilterMap = null;
            ArrayList lcFilterList = null;
            String filterKey = null;
            String initKey = null;
            String resultKey = null;
            ArrayList lcClassList = null;
            ArrayList lcResultFieldList = null;
            String lcResultFieldValue = null;
            ArrayList lcFieldList = null;
            ArrayList lcConditionList = null;
            HashMap lcConditionMap = null;
            filterKey = nds.getValue(objectOuid);
            initKey = filterKey + "/CONDITION";
            resultKey = filterKey + "/RESULTFIELD";
            lcClassList = nds.getChildNodeList(initKey);
            if(lcClassList != null)
            {
                lcInputList = new ArrayList();
                for(int i = 0; i < lcClassList.size(); i++)
                {
                    lcRowList = new ArrayList();
                    lcClassOuid = nds.getValue(initKey + "/" + (String)lcClassList.get(i));
                    lcFieldMap = new HashMap();
                    lcResultFieldList = nds.getChildNodeList(resultKey);
                    if(lcResultFieldList != null)
                    {
                        for(int j = 0; j < lcResultFieldList.size(); j++)
                        {
                            lcResultFieldValue = nds.getValue(resultKey + "/" + (String)lcResultFieldList.get(j));
                            java.util.List tokens = Utils.tokenizeMessage(lcResultFieldValue, '/');
                            if(((String)tokens.get(0)).equals((String)lcClassList.get(i)))
                                lcFieldMap.put((String)tokens.get(1), new Integer(j + 1));
                        }

                    }
                    lcFilterMap = new HashMap();
                    lcFieldList = nds.getChildNodeList(initKey + "/" + (String)lcClassList.get(i));
                    if(lcFieldList != null)
                    {
                        for(int j = 0; j < lcFieldList.size(); j++)
                        {
                            lcConditionList = nds.getChildNodeList(initKey + "/" + (String)lcClassList.get(i) + "/" + (String)lcFieldList.get(j));
                            if(lcConditionList != null)
                            {
                                lcFilterList = new ArrayList();
                                for(int k = 0; k < lcConditionList.size(); k++)
                                {
                                    lcConditionMap = nds.getNode(initKey + "/" + (String)lcClassList.get(i) + "/" + (String)lcFieldList.get(j) + "/" + (String)lcConditionList.get(k));
                                    for(int m = 0; m < lcClassList.size(); m++)
                                    {
                                        java.util.List tokens = Utils.tokenizeMessage((String)lcConditionMap.get("value"), '.');
                                        String lcTmpStr = null;
                                        if(((String)lcClassList.get(m)).equals((String)tokens.get(0)))
                                        {
                                            lcTmpStr = (new Integer(m)).toString() + "." + (String)tokens.get(1) + "." + (String)tokens.get(2);
                                            lcConditionMap.put("value", lcTmpStr);
                                        }
                                    }

                                    lcFilterList.add(lcConditionMap);
                                }

                            }
                            lcFilterMap.put((String)lcFieldList.get(j), lcFilterList);
                            lcFilterList = null;
                        }

                    }
                    lcRowList.add(lcClassOuid);
                    lcRowList.add(lcFieldMap);
                    lcRowList.add(lcFilterMap);
                    lcClassOuid = null;
                    lcFieldMap = null;
                    lcFilterMap = null;
                    lcInputList.add(lcRowList);
                    lcRowList = null;
                }

            }
            ArrayList resultList = dos.advancedList(lcInputList);
            AdvancedSearchResult advancedSearchResult = new AdvancedSearchResult(initKey, resultKey);
            advancedSearchResult.makeAdvancedSearchResultTable();
            advancedSearchResult.setResultData(resultList);
            removeAll();
            resultBorderLayout = new BorderLayout();
            setLayout(resultBorderLayout);
            add(advancedSearchResult, "Center");
            updateUI();
        }
        catch(IIPRequestException e)
        {
            System.out.println(e);
        }
    }

    private ArrayList getSelectedOuids()
    {
        Table table = searchResultTable;
        int selectedRows[] = table.getTable().getSelectedRows();
        String instanceOuid = null;
        String ouidRow = null;
        ArrayList ouidList = null;
        if(selectedRows == null && selectedRows.length < 1)
            return null;
        ouidList = new ArrayList();
        for(int n = 0; n < selectedRows.length; n++)
        {
            ouidRow = table.getSelectedOuidRow(selectedRows[n]);
            if(ouidRow != null)
                table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
            if(ouidRow != null)
                instanceOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
            else
                instanceOuid = (String)((ArrayList)searchData.get((new Integer(selectedRows[n])).intValue())).get(0);
            ouidList.add(instanceOuid);
        }

        if(ouidList.size() == 0)
            ouidList = null;
        return ouidList;
    }

    private DOS dos;
    private NDS nds;
    private DSS dss;
    private ACL acl;
    private WFM wfm;
    private UIManagement newUI;
    public JFrame parentFrame;
    private TableSortDecorator decorator;
    private BorderLayout resultBorderLayout;
    public Table searchResultTable;
    public JScrollPane tableScrPane;
    private ArrayList searchData;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private String selectOuid;
    private String modelOuid;
    private String classOuid;
    private int selectedRows[];
    private String classOuidForWorkSpace;
    private ClientUtil clientUtil;
    private JPopupMenu popup;
    private DOSChangeable orderOfOuid;
    private Session _session;
    private Cursor handCursor;
    final int MODIFY_MODE = 1;






}