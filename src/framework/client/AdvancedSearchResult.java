// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AdvancedSearchResult.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.ACL;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.msr.MSRStgrep;
import dyna.uic.Table;
import dyna.uic.TableSortDecorator;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, ClientUtil

public class AdvancedSearchResult extends JPanel
{

    public AdvancedSearchResult(String lcInitKey, String lcResultKey)
    {
        wfm = null;
        msrStgrep = null;
        initKey = null;
        resultKey = null;
        clientUtil = null;
        popup = null;
        infoMenuItem = null;
        treeLinkViewMenu = null;
        fileMenu = null;
        processMenu = null;
        makeWIPMenuItem = null;
        favoriteMenuItem = null;
        deleteMenuItem = null;
        setAuthMenuItem = null;
        copyMenuItem = null;
        orderOfOuid = new DOSChangeable();
        handCursor = new Cursor(12);
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        dss = DynaMOAD.dss;
        wfm = DynaMOAD.wfm;
        acl = DynaMOAD.acl;
        newUI = DynaMOAD.newUI;
        initKey = lcInitKey;
        resultKey = lcResultKey;
        try
        {
            modelOuid = dos.getWorkingModel();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void makeAdvancedSearchResultTable()
    {
        try
        {
            ArrayList lcResultFieldList = null;
            String lcResultFieldValue = null;
            String lcClassOuid = null;
            String lcFieldOuid = null;
            String tmpStr = null;
            searchData = new ArrayList();
            columnName = new ArrayList();
            columnWidth = new ArrayList();
            lcResultFieldList = nds.getChildNodeList(resultKey);
            if(lcResultFieldList != null)
            {
                selectedRows = new int[lcResultFieldList.size()];
                for(int i = 0; i < lcResultFieldList.size(); i++)
                {
                    lcResultFieldValue = nds.getValue(resultKey + "/" + (String)lcResultFieldList.get(i));
                    java.util.List tokens = Utils.tokenizeMessage(lcResultFieldValue, '/');
                    lcClassOuid = nds.getValue(initKey + "/" + (String)tokens.get(0));
                    lcFieldOuid = (String)tokens.get(1);
                    DOSChangeable dosClass = dos.getClass(lcClassOuid);
                    DOSChangeable dosField = dos.getField(lcFieldOuid);
                    tmpStr = (String)dosClass.get("name");
                    if(dosField != null)
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0);
                    else
                    if(lcFieldOuid.equals("md$number"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0020", "Number", 3);
                    else
                    if(lcFieldOuid.equals("md$description"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0021", "Description", 3);
                    else
                    if(lcFieldOuid.equals("vf$version"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0023", "Version", 3);
                    else
                    if(lcFieldOuid.equals("md$status"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0024", "Status", 3);
                    else
                    if(lcFieldOuid.equals("md$user"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0025", "User", 3);
                    else
                    if(lcFieldOuid.equals("md$cdate"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0026", "Created Date", 3);
                    else
                    if(lcFieldOuid.equals("md$mdate"))
                        tmpStr = tmpStr + "." + DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3);
                    columnName.add(tmpStr);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            }
            searchResultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), -1, 79);
            searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            searchResultTable.setColumnSequence(selectedRows);
            searchResultTable.setIndexColumn(0);
            searchResultTable.getTable().setCursor(handCursor);
            tableScrPane = UIFactory.createStrippedScrollPane(null);
            tableScrPane.setViewportView(searchResultTable.getTable());
            setLayout(new BorderLayout());
            add(tableScrPane, "Center");
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setResultData(ArrayList resultData)
    {
        ArrayList tmpList1 = null;
        ArrayList tmpList2 = null;
        searchData.clear();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                tmpList1 = (ArrayList)resultData.get(i);
                tmpList2 = new ArrayList();
                for(int j = 0; j < tmpList1.size(); j++)
                    tmpList2.add(tmpList1.get(j));

                searchData.add(tmpList2);
                tmpList2 = null;
            }

        }
        searchResultTable.changeTableData();
    }

    final int MODIFY_MODE = 1;
    private DOS dos;
    private NDS nds;
    private DSS dss;
    private ACL acl;
    private WFM wfm;
    private UIManagement newUI;
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
    private MSRStgrep msrStgrep;
    private String initKey;
    private String resultKey;
    private ClientUtil clientUtil;
    private JPopupMenu popup;
    private JMenuItem infoMenuItem;
    private JMenu treeLinkViewMenu;
    private JMenu fileMenu;
    private JMenu processMenu;
    private JMenuItem makeWIPMenuItem;
    private JMenuItem favoriteMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem setAuthMenuItem;
    private JMenuItem copyMenuItem;
    private DOSChangeable orderOfOuid;
    private Cursor handCursor;
}