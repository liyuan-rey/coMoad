// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIGeneration.java

package dyna.framework.client;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtLabel;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.PopupButton;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.ACL;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.WKS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.DynaDateChooser;
import dyna.uic.DynaTable;
import dyna.uic.DynaTheme;
import dyna.uic.FileTreeModel;
import dyna.uic.JTreeTable;
import dyna.uic.LinkTreeModel;
import dyna.uic.MComboBoxModelObject;
import dyna.uic.MWindowLocationManager;
import dyna.uic.SoftWareFilter;
import dyna.uic.SwingWorker;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.uic.TreeTableModel;
import dyna.uic.UIUtils;
import dyna.uic.table.DynaTableCellColor;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIBuilder, GanttChartPanel, LogIn, 
//            SearchCondition, SearchCondition4CADIntegration, FieldLinkSelect, FilterDialogForLink, 
//            GanttChartSetting, CheckIn, Search4CADIntegration, SearchResult4CADIntegration, 
//            CheckOut, AssoClassUI, CreateProcess, CodeSelectDialog, 
//            MultiSelectDialog, NetworkEditorPanel, ClientUtil, UIManagement, 
//            FileTransferDialog

public class UIGeneration extends JFrame
    implements ActionListener, ChangeListener, WindowListener, MouseListener
{
    private class historyColor
        implements DynaTableCellColor
    {

        public Color getBackground(DynaTable table, int row, int column)
        {
            if(((ArrayList)historyData.get(row)).get(0).equals(instanceOuid))
                return DynaTheme.tableHilightedColor2;
            if(row % 2 == 0)
                return table.getBackground();
            else
                return evenRowColor;
        }

        public Color getForeground(DynaTable table, int row, int column)
        {
            return table.getForeground();
        }

        private Color evenRowColor;

        historyColor()
        {
            evenRowColor = new Color(230, 235, 213);
        }
    }

    private class LinkListener
        implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent tse)
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                ArrayList rows = null;
                TreePath path = tse.getPath();
                if(path == null)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
                if(nodeData.isPopulated())
                    return;
                if(searchConditionMap != null && !searchConditionMap.containsKey("version.condition.type"))
                    searchConditionMap.put("version.condition.type", "wip");
                ArrayList tmpColumnOuidList = ((LinkTreeModel)linkTreeTable.getTreeTableModel()).getColumnOuidList();
                if(PROCESSING_MODE == 0)
                {
                    System.out.println("LinkListener : PROCESSING_MODE == 0 : listLinkFrom " + (searchConditionMap == null));
                    if(searchConditionMap == null)
                    {
                        rows = dos.listLinkFrom((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList);
                    } else
                    {
                        searchConditionMap.remove("list.mode");
                        searchConditionMap.remove("ouid@association.class");
                        rows = dos.listLinkFrom((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList, searchConditionMap);
                    }
                } else
                if(PROCESSING_MODE == 1)
                {
                    System.out.println("LinkListener : PROCESSING_MODE == 1 : listLinkTo " + (searchConditionMap == null));
                    if(searchConditionMap == null)
                    {
                        rows = dos.listLinkTo((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList);
                    } else
                    {
                        searchConditionMap.remove("list.mode");
                        searchConditionMap.remove("ouid@association.class");
                        rows = dos.listLinkTo((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList, searchConditionMap);
                    }
                }
                if(tmpColumnOuidList != null)
                {
                    childColumnOuidList = tmpColumnOuidList;
                    tmpColumnOuidList = null;
                }
                if(rows == null || rows.size() == 0)
                {
                    nodeData.setPopulated(true);
                    return;
                }
                ArrayList row = null;
                TreeNodeObject childNodeData = null;
                Iterator rowsKey;
                for(rowsKey = rows.iterator(); rowsKey.hasNext();)
                {
                    row = (ArrayList)rowsKey.next();
                    childNodeData = new TreeNodeObject((String)row.get(0) + "^" + dos.getClassOuid((String)row.get(0)) + "^" + row.get(row.size() - 1), (String)row.get(1), "Class");
                    childNodeData.setLawData(row);
                    node.add(new DefaultMutableTreeNode(childNodeData));
                    childNodeData = null;
                    row = null;
                }

                rowsKey = null;
                rows.clear();
                rows = null;
                nodeData.setPopulated(true);
                if(node.getLevel() == 0)
                    linkTreeTable.tree.fireTreeExpanded(path);
                else
                    linkTreeTable.tree.fireTreeCollapsed(path);
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        }

        LinkListener()
        {
        }
    }

    private class FileListener
        implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent tse)
        {
            try
            {
                TreePath path = tse.getPath();
                if(path == null)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
                if(nodeData.isPopulated())
                    return;
                ArrayList rows = dos.listFile(instanceOuid);
                if(rows == null || rows.size() == 0)
                {
                    nodeData.setPopulated(true);
                    return;
                }
                if(node.getLevel() == 0)
                {
                    HashMap row = null;
                    TreeNodeObject childNodeData = null;
                    TreeNodeObject fileTypeNodeData = null;
                    String fileType = null;
                    DOSChangeable fileTypeSave = new DOSChangeable();
                    DefaultMutableTreeNode fileTypeNode = null;
                    int counter = 0;
                    Iterator rowsKey;
                    for(rowsKey = rows.iterator(); rowsKey.hasNext();)
                    {
                        row = (HashMap)rowsKey.next();
                        fileType = (String)row.get("md$filetype.id");
                        if(counter == 0)
                            fileTypeSave.putOriginalValue("fileType", fileType);
                        fileTypeSave.put("fileType", fileType);
                        childNodeData = new TreeNodeObject((String)row.get("md$ouid"), (String)row.get("md$filetype.description") + " " + ((Integer)row.get("md$index")).toString(), (String)row.get("md$version") + "^" + (String)row.get("md$path") + "^" + (String)row.get("md$filetype.id") + "^" + (String)row.get("md$checkedout.date") + "^" + (String)row.get("md$checkedin.date"));
                        if(fileTypeSave.isChanged("fileType") || counter == 0)
                        {
                            fileTypeSave.putOriginalValue("fileType", fileType);
                            fileTypeNodeData = new TreeNodeObject((String)row.get("md$filetype.id"), (String)row.get("md$filetype.description"), (String)row.get("md$filetype.id"));
                            fileTypeNode = new DefaultMutableTreeNode(fileTypeNodeData);
                            node.add(fileTypeNode);
                        }
                        childNodeData.setLawData(row);
                        fileTypeNode.add(new DefaultMutableTreeNode(childNodeData));
                        childNodeData = null;
                        row = null;
                        counter++;
                    }

                    rowsKey = null;
                    rows.clear();
                    rows = null;
                    nodeData.setPopulated(true);
                    fileTreeTable.tree.fireTreeExpanded(path);
                }
                if(node.getLevel() == 2)
                    fileReadOnlyButton.setEnabled(true);
                else
                    fileReadOnlyButton.setEnabled(false);
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        }

        FileListener()
        {
        }
    }

    private class ExpansionListener
        implements TreeExpansionListener
    {

        public void treeCollapsed(TreeExpansionEvent tee)
        {
            if(treeOption == 1 && chartPanel.isVisible())
                setChartData();
        }

        public void treeExpanded(TreeExpansionEvent tee)
        {
            if(treeOption == 1 && chartPanel.isVisible())
                setChartData();
        }

        ExpansionListener()
        {
        }
    }

    class PopupLink extends MouseAdapter
    {

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

        public void mouseClicked(MouseEvent e)
        {
            Object source = e.getSource();
            if(processTable != null && source.equals(processTable.getTable()))
            {
                int row = processTable.getTable().getSelectedRow();
                if(row >= 0)
                {
                    if(e.getClickCount() == 2)
                        processOpenButton.doClick();
                    else
                        processOpenButton.setEnabled(true);
                } else
                {
                    processOpenButton.setEnabled(false);
                }
            } else
            if(historyTable != null && source.equals(historyTable.getTable()))
            {
                int row = historyTable.getTable().getSelectedRow();
                if(row >= 0)
                {
                    if(e.getClickCount() == 2)
                        historyDetailButton.doClick();
                    else
                        historyDetailButton.setEnabled(true);
                } else
                {
                    historyDetailButton.setEnabled(false);
                }
            }
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger())
                if(e.getSource().equals(linkTreeTable))
                {
                    if(linkTreeTable.tree.getSelectionPath() == null)
                        return;
                    String end1Ouid = ((TreeNodeObject)linkRootNode.getUserObject()).getOuid();
                    String end2Ouid = ((TreeNodeObject)((DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getOuid();
                    end1Ouid = (String)Utils.tokenizeMessage(end1Ouid, '^').get(0);
                    end2Ouid = (String)Utils.tokenizeMessage(end2Ouid, '^').get(0);
                    DefaultMutableTreeNode node = null;
                    TreeNodeObject nodeObject = null;
                    String selectedOuid = null;
                    TreePath paths[] = linkTreeTable.tree.getSelectionPaths();
                    if(paths == null)
                        return;
                    ArrayList ouidList = new ArrayList(paths.length);
                    for(int i = 0; i < paths.length; i++)
                    {
                        node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                        if(node != null)
                        {
                            nodeObject = (TreeNodeObject)node.getUserObject();
                            if(nodeObject != null)
                            {
                                selectedOuid = nodeObject.getOuid();
                                selectedOuid = (String)Utils.tokenizeMessage(selectedOuid, '^').get(0);
                                ouidList.add(selectedOuid);
                            }
                        }
                    }

                    try
                    {
                        linkPopupMenu = UIBuilder.createPopupMenuForStructureTree(end1Ouid, end2Ouid, ouidList, assoList, e.getComponent(), e.getX(), e.getY(), UIGeneration.this, modelOuid);
                    }
                    catch(Exception ie)
                    {
                        ie.printStackTrace();
                    }
                } else
                if(e.getSource().equals(fileTreeTable))
                {
                    String tmpStr = ((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getDescription();
                    java.util.List tmpList = Utils.tokenizeMessage(tmpStr, '^');
                    boolean isUpdatable = false;
                    String status = null;
                    try
                    {
                        status = DynaMOAD.dos.getStatus(instanceOuid);
                        if("CRT".equals(status) || "WIP".equals(status) || "REJ".equals(status))
                            isUpdatable = true;
                    }
                    catch(Exception ee)
                    {
                        ee.printStackTrace();
                    }
                    if(tmpList.size() == 5)
                    {
                        String checkedOutDate = (String)Utils.tokenizeMessage(tmpStr, '^').get(3);
                        String checkedInDate = (String)Utils.tokenizeMessage(tmpStr, '^').get(4);
                        if(isUpdatable)
                        {
                            if(!Utils.isNullString(checkedOutDate) && "null".equals(checkedOutDate))
                                checkedOutDate = null;
                            if(!Utils.isNullString(checkedInDate) && "null".equals(checkedInDate))
                                checkedInDate = null;
                            if(Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate))
                            {
                                menuFileCheckOut.setEnabled(true);
                                menuFileCheckIn.setEnabled(false);
                                menuFileCheckOutCancel.setEnabled(false);
                            } else
                            if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) > 0)
                            {
                                menuFileCheckOut.setEnabled(false);
                                menuFileCheckIn.setEnabled(true);
                                menuFileCheckOutCancel.setEnabled(true);
                            } else
                            if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) <= 0)
                            {
                                menuFileCheckOut.setEnabled(true);
                                menuFileCheckIn.setEnabled(false);
                                menuFileCheckOutCancel.setEnabled(false);
                            } else
                            {
                                menuFileCheckOut.setEnabled(false);
                                menuFileCheckIn.setEnabled(false);
                                menuFileCheckOutCancel.setEnabled(false);
                            }
                        } else
                        {
                            menuFileCheckOut.setEnabled(false);
                            menuFileCheckIn.setEnabled(false);
                            menuFileCheckOutCancel.setEnabled(false);
                            menuFileDetach.setEnabled(false);
                        }
                        filePopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
        }

        PopupLink()
        {
        }
    }

    class FileCallBack
        implements FileTransferCallback
    {

        public void transferBytes(int bytes)
        {
            ftd.addSize(bytes);
        }

        FileCallBack()
        {
        }
    }

    class AssociationPopupMenu extends MouseAdapter
    {

        public void mouseClicked(MouseEvent e)
        {
            assoTable = (Table)associationTableMap.get(assoOuid);
            if(assoTable == null)
                return;
            int row = assoTable.getTable().getSelectedRow();
            String ouidRow = assoTable.getSelectedOuidRow(row);
            if(ouidRow != null)
                assoTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
            if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && ouidRow != null && assoTable.getSelectedRowNumber() > -1)
                try
                {
                    String selectOuid = (String)((ArrayList)assoTable.getList().get((new Integer(ouidRow)).intValue())).get(0);
                    String selectClassOuid = dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(searchResultPanel, selectClassOuid, selectOuid, 1);
                    uiGeneration.setSession(UIGeneration._session);
                    uiGeneration.setVisible(true);
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
        }

        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger() && !e.isControlDown())
            {
                assoTable = (Table)associationTableMap.get(assoOuid);
                if(assoTable == null)
                    return;
                int selectedRows[] = assoTable.getTable().getSelectedRows();
                int row = assoTable.getTable().getSelectedRow();
                String ouidRow = assoTable.getSelectedOuidRow(row);
                if(assoTable.getSelectedRowNumber() > -1)
                {
                    String selectedOuid = null;
                    String selectedClassOuid = null;
                    ArrayList ouidList = null;
                    if(selectedRows == null && selectedRows.length < 1)
                        return;
                    ouidList = new ArrayList();
                    for(int n = 0; n < selectedRows.length; n++)
                    {
                        ouidRow = assoTable.getSelectedOuidRow(selectedRows[n]);
                        if(ouidRow != null)
                            assoTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        if(ouidRow != null)
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(ouidRow)).intValue())).get(0);
                        else
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(selectedRows[n])).intValue())).get(0);
                        ouidList.add(selectedOuid);
                    }

                    try
                    {
                        selectedClassOuid = dos.getClassOuid(selectedOuid);
                        if(Utils.isNullString(selectedClassOuid))
                            return;
                        ArrayList assoList = UIGeneration.listAssociations(selectedClassOuid);
                        if(Utils.isNullArrayList(assoList))
                            assoList = UIGeneration.listAssociations(selectedClassOuid, Boolean.FALSE);
                        UIBuilder.createPopupMenuForAssociationTable(instanceOuid, instanceOuid, ouidList, assoList, e.getComponent(), e.getX(), e.getY(), UIGeneration.this, modelOuid, assoOuid);
                    }
                    catch(IIPRequestException ie)
                    {
                        ie.printStackTrace();
                    }
                }
            }
        }

        Table assoTable;
        String assoOuid;

        public AssociationPopupMenu(String associationOuid)
        {
            assoTable = null;
            assoOuid = null;
            assoOuid = associationOuid;
        }
    }


    public UIGeneration(JPanel searchConditionPanel, String ouid)
    {
        isdebugmode = true;
        mwlm = null;
        statusCode = null;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        chartTableHeaderRenderer = null;
        handCursor = new Cursor(12);
        ftd = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        linkListener = null;
        expansionListener = null;
        fileListener = null;
        fileModel = null;
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        modelOuid = "";
        classOuid = "";
        classInfo = null;
        uiMode = 0;
        instanceOuid = "";
        linkRootNode = null;
        fileRootNode = null;
        hasChild = false;
        searchConditionMap = null;
        getDOSGlobal = null;
        filterDialog = null;
        titleValue = "";
        processList = null;
        chartFieldOuid = null;
        chartFieldData = null;
        columnOuidList = null;
        columnList = null;
        chartFieldTitleList = null;
        chartFieldDescList = null;
        chartFieldTypeList = null;
        chartFieldOuidList = null;
        treeOption = 0;
        defaultRowHeight = 21;
        assoList = null;
        childColumnOuidList = null;
        linkedInstanceOuid = null;
        fieldGroupNameToTitleMap = null;
        collectionMap = new HashMap();
        tabIndexForObjectTree = -1;
        tabIndexForFile = -1;
        tabIndexForVersion = -1;
        tabIndexForProcess = -1;
        comp = null;
        files = null;
        networkFieldOuid = null;
        networkFieldData = null;
        associationMap = null;
        associationTableMap = null;
        treeViewConditionMap = new HashMap();
        closeAfterAdd = false;
        PROCESSING_MODE = 0;
        try
        {
            this.searchConditionPanel = searchConditionPanel;
            classOuid = ouid;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            wfm = DynaMOAD.wfm;
            aus = DynaMOAD.aus;
            wks = DynaMOAD.wks;
            acl = DynaMOAD.acl;
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            groupList = dos.listFieldGroupInClass(ouid);
            getDOSGlobal = getDOS();
            addWindowListener(this);
            initialize();
            setInitData();
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0049", "Regist", 3));
            saveButton.setIcon(new ImageIcon("icons/Registry.gif"));
            saveButton.setEnabled(true);
            setTitle((String)classInfo.get("title") + " " + DynaMOAD.getMSRString("WRD_0049", "Add", 3));
            Object returnObject = Utils.executeScriptFile(dos.getEventName(classOuid, "add.init"), dss, this);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                windowClosing(null);
            else
                setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public UIGeneration(JPanel searchResultPanel, String ouid, String instanceOuid)
    {
        isdebugmode = true;
        mwlm = null;
        statusCode = null;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        chartTableHeaderRenderer = null;
        handCursor = new Cursor(12);
        ftd = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        linkListener = null;
        expansionListener = null;
        fileListener = null;
        fileModel = null;
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        modelOuid = "";
        classOuid = "";
        classInfo = null;
        uiMode = 0;
        this.instanceOuid = "";
        linkRootNode = null;
        fileRootNode = null;
        hasChild = false;
        searchConditionMap = null;
        getDOSGlobal = null;
        filterDialog = null;
        titleValue = "";
        processList = null;
        chartFieldOuid = null;
        chartFieldData = null;
        columnOuidList = null;
        columnList = null;
        chartFieldTitleList = null;
        chartFieldDescList = null;
        chartFieldTypeList = null;
        chartFieldOuidList = null;
        treeOption = 0;
        defaultRowHeight = 21;
        assoList = null;
        childColumnOuidList = null;
        linkedInstanceOuid = null;
        fieldGroupNameToTitleMap = null;
        collectionMap = new HashMap();
        tabIndexForObjectTree = -1;
        tabIndexForFile = -1;
        tabIndexForVersion = -1;
        tabIndexForProcess = -1;
        comp = null;
        files = null;
        networkFieldOuid = null;
        networkFieldData = null;
        associationMap = null;
        associationTableMap = null;
        treeViewConditionMap = new HashMap();
        closeAfterAdd = false;
        PROCESSING_MODE = 0;
        try
        {
            this.searchResultPanel = searchResultPanel;
            classOuid = ouid;
            this.instanceOuid = instanceOuid;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            wfm = DynaMOAD.wfm;
            aus = DynaMOAD.aus;
            wks = DynaMOAD.wks;
            acl = DynaMOAD.acl;
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            groupList = dos.listFieldGroupInClass(ouid);
            getDOSGlobal = getDOS();
            addWindowListener(this);
            initialize();
            getDOSGlobal.clear("ouid");
            getDOSGlobal.clear("md$number");
            getDOSGlobal.clear("vf$version");
            getDOSGlobal.clear("md$status");
            getDOSGlobal.clear("md$user");
            getDOSGlobal.clear("md$cdate");
            getDOSGlobal.clear("md$mdate");
            setData(getDOSGlobal);
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0049", "Regist", 3));
            saveButton.setIcon(new ImageIcon("icons/Registry.gif"));
            saveButton.setEnabled(true);
            setTitle((String)classInfo.get("title") + " " + DynaMOAD.getMSRString("WRD_0049", "Add", 3));
            Object returnObject = Utils.executeScriptFile(dos.getEventName(classOuid, "add.init"), dss, this);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                windowClosing(null);
            else
                setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public UIGeneration(JPanel searchConditionPanel, String ouid, String linkedinstanceouid, Component comp)
    {
        isdebugmode = true;
        mwlm = null;
        statusCode = null;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        chartTableHeaderRenderer = null;
        handCursor = new Cursor(12);
        ftd = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        linkListener = null;
        expansionListener = null;
        fileListener = null;
        fileModel = null;
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        modelOuid = "";
        classOuid = "";
        classInfo = null;
        uiMode = 0;
        instanceOuid = "";
        linkRootNode = null;
        fileRootNode = null;
        hasChild = false;
        searchConditionMap = null;
        getDOSGlobal = null;
        filterDialog = null;
        titleValue = "";
        processList = null;
        chartFieldOuid = null;
        chartFieldData = null;
        columnOuidList = null;
        columnList = null;
        chartFieldTitleList = null;
        chartFieldDescList = null;
        chartFieldTypeList = null;
        chartFieldOuidList = null;
        treeOption = 0;
        defaultRowHeight = 21;
        assoList = null;
        childColumnOuidList = null;
        linkedInstanceOuid = null;
        fieldGroupNameToTitleMap = null;
        collectionMap = new HashMap();
        tabIndexForObjectTree = -1;
        tabIndexForFile = -1;
        tabIndexForVersion = -1;
        tabIndexForProcess = -1;
        this.comp = null;
        files = null;
        networkFieldOuid = null;
        networkFieldData = null;
        associationMap = null;
        associationTableMap = null;
        treeViewConditionMap = new HashMap();
        closeAfterAdd = false;
        PROCESSING_MODE = 0;
        try
        {
            System.out.println("linkedInstanceOuid = " + linkedInstanceOuid);
            this.searchConditionPanel = searchConditionPanel;
            classOuid = ouid;
            linkedInstanceOuid = linkedinstanceouid;
            this.comp = comp;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            wfm = DynaMOAD.wfm;
            aus = DynaMOAD.aus;
            wks = DynaMOAD.wks;
            acl = DynaMOAD.acl;
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            groupList = dos.listFieldGroupInClass(ouid);
            getDOSGlobal = getDOS();
            addWindowListener(this);
            initialize();
            setInitData();
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0049", "Regist", 3));
            saveButton.setIcon(new ImageIcon("icons/Registry.gif"));
            saveButton.setEnabled(true);
            setTitle((String)classInfo.get("title") + " " + DynaMOAD.getMSRString("WRD_0049", "Add", 3));
            Object returnObject = Utils.executeScriptFile(dos.getEventName(classOuid, "add.init"), dss, this);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                windowClosing(null);
            else
                setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public UIGeneration(JPanel searchConditionPanel, String ouid, String linkedinstanceouid, String instanceOuid, Component comp)
    {
        isdebugmode = true;
        mwlm = null;
        statusCode = null;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        chartTableHeaderRenderer = null;
        handCursor = new Cursor(12);
        ftd = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        linkListener = null;
        expansionListener = null;
        fileListener = null;
        fileModel = null;
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        modelOuid = "";
        classOuid = "";
        classInfo = null;
        uiMode = 0;
        this.instanceOuid = "";
        linkRootNode = null;
        fileRootNode = null;
        hasChild = false;
        searchConditionMap = null;
        getDOSGlobal = null;
        filterDialog = null;
        titleValue = "";
        processList = null;
        chartFieldOuid = null;
        chartFieldData = null;
        columnOuidList = null;
        columnList = null;
        chartFieldTitleList = null;
        chartFieldDescList = null;
        chartFieldTypeList = null;
        chartFieldOuidList = null;
        treeOption = 0;
        defaultRowHeight = 21;
        assoList = null;
        childColumnOuidList = null;
        linkedInstanceOuid = null;
        fieldGroupNameToTitleMap = null;
        collectionMap = new HashMap();
        tabIndexForObjectTree = -1;
        tabIndexForFile = -1;
        tabIndexForVersion = -1;
        tabIndexForProcess = -1;
        this.comp = null;
        files = null;
        networkFieldOuid = null;
        networkFieldData = null;
        associationMap = null;
        associationTableMap = null;
        treeViewConditionMap = new HashMap();
        closeAfterAdd = false;
        PROCESSING_MODE = 0;
        try
        {
            System.out.println("linkedInstanceOuid = " + linkedInstanceOuid);
            this.searchConditionPanel = searchConditionPanel;
            classOuid = ouid;
            linkedInstanceOuid = linkedinstanceouid;
            this.instanceOuid = instanceOuid;
            this.comp = comp;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            wfm = DynaMOAD.wfm;
            aus = DynaMOAD.aus;
            wks = DynaMOAD.wks;
            acl = DynaMOAD.acl;
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            groupList = dos.listFieldGroupInClass(ouid);
            getDOSGlobal = getDOS();
            addWindowListener(this);
            initialize();
            getDOSGlobal.clear("ouid");
            getDOSGlobal.clear("md$number");
            getDOSGlobal.clear("vf$version");
            getDOSGlobal.clear("md$status");
            getDOSGlobal.clear("md$user");
            getDOSGlobal.clear("md$cdate");
            getDOSGlobal.clear("md$mdate");
            setData(getDOSGlobal);
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0049", "Regist", 3));
            saveButton.setIcon(new ImageIcon("icons/Registry.gif"));
            saveButton.setEnabled(true);
            setTitle((String)classInfo.get("title") + " " + DynaMOAD.getMSRString("WRD_0049", "Add", 3));
            Object returnObject = Utils.executeScriptFile(dos.getEventName(classOuid, "add.init"), dss, this);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                windowClosing(null);
            else
                setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public UIGeneration(JPanel searchResultPanel, String ouid, String instanceOuid, int mode)
    {
        isdebugmode = true;
        mwlm = null;
        statusCode = null;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        chartTableHeaderRenderer = null;
        handCursor = new Cursor(12);
        ftd = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        linkListener = null;
        expansionListener = null;
        fileListener = null;
        fileModel = null;
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        modelOuid = "";
        classOuid = "";
        classInfo = null;
        uiMode = 0;
        this.instanceOuid = "";
        linkRootNode = null;
        fileRootNode = null;
        hasChild = false;
        searchConditionMap = null;
        getDOSGlobal = null;
        filterDialog = null;
        titleValue = "";
        processList = null;
        chartFieldOuid = null;
        chartFieldData = null;
        columnOuidList = null;
        columnList = null;
        chartFieldTitleList = null;
        chartFieldDescList = null;
        chartFieldTypeList = null;
        chartFieldOuidList = null;
        treeOption = 0;
        defaultRowHeight = 21;
        assoList = null;
        childColumnOuidList = null;
        linkedInstanceOuid = null;
        fieldGroupNameToTitleMap = null;
        collectionMap = new HashMap();
        tabIndexForObjectTree = -1;
        tabIndexForFile = -1;
        tabIndexForVersion = -1;
        tabIndexForProcess = -1;
        comp = null;
        files = null;
        networkFieldOuid = null;
        networkFieldData = null;
        associationMap = null;
        associationTableMap = null;
        treeViewConditionMap = new HashMap();
        closeAfterAdd = false;
        PROCESSING_MODE = 0;
        try
        {
            uiMode = mode;
            this.searchResultPanel = searchResultPanel;
            this.instanceOuid = instanceOuid;
            searchConditionPanel = DynaMOAD.searchCondition;
            classOuid = ouid;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            wfm = DynaMOAD.wfm;
            aus = DynaMOAD.aus;
            wks = DynaMOAD.wks;
            acl = DynaMOAD.acl;
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            groupList = dos.listFieldGroupInClass(ouid);
            getDOSGlobal = getDOS();
            addWindowListener(this);
            refreshPermissionValues();
            initialize();
            setData(getDOSGlobal);
            setTitle((String)classInfo.get("title") + (String)getDOSGlobal.get("md$number"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public UIGeneration(JPanel searchResultPanel, String ouid, String instanceOuid, int mode, DefaultMutableTreeNode selectedNode, UIGeneration tmpUIGeneration, String sessionProperty)
    {
        isdebugmode = true;
        mwlm = null;
        statusCode = null;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        chartTableHeaderRenderer = null;
        handCursor = new Cursor(12);
        ftd = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        linkListener = null;
        expansionListener = null;
        fileListener = null;
        fileModel = null;
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        modelOuid = "";
        classOuid = "";
        classInfo = null;
        uiMode = 0;
        this.instanceOuid = "";
        linkRootNode = null;
        fileRootNode = null;
        hasChild = false;
        searchConditionMap = null;
        getDOSGlobal = null;
        filterDialog = null;
        titleValue = "";
        processList = null;
        chartFieldOuid = null;
        chartFieldData = null;
        columnOuidList = null;
        columnList = null;
        chartFieldTitleList = null;
        chartFieldDescList = null;
        chartFieldTypeList = null;
        chartFieldOuidList = null;
        treeOption = 0;
        defaultRowHeight = 21;
        assoList = null;
        childColumnOuidList = null;
        linkedInstanceOuid = null;
        fieldGroupNameToTitleMap = null;
        collectionMap = new HashMap();
        tabIndexForObjectTree = -1;
        tabIndexForFile = -1;
        tabIndexForVersion = -1;
        tabIndexForProcess = -1;
        comp = null;
        files = null;
        networkFieldOuid = null;
        networkFieldData = null;
        associationMap = null;
        associationTableMap = null;
        treeViewConditionMap = new HashMap();
        closeAfterAdd = false;
        PROCESSING_MODE = 0;
        try
        {
            uiMode = mode;
            this.searchResultPanel = searchResultPanel;
            this.instanceOuid = instanceOuid;
            searchConditionPanel = DynaMOAD.searchCondition;
            classOuid = ouid;
            tmpSelectedNode = selectedNode;
            this.sessionProperty = sessionProperty;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            wfm = DynaMOAD.wfm;
            aus = DynaMOAD.aus;
            wks = DynaMOAD.wks;
            acl = DynaMOAD.acl;
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            groupList = dos.listFieldGroupInClass(ouid);
            getDOSGlobal = getDOS();
            addWindowListener(this);
            refreshPermissionValues();
            initialize();
            setData(getDOSGlobal);
            setTitle((String)classInfo.get("title") + (String)getDOSGlobal.get("md$number"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initialize()
    {
        actionButtonList = new ArrayList();
        selectButtonList = new ArrayList();
        dateChooserButtonList = new ArrayList();
        Boolean isVisible = null;
        mwlm = new MWindowLocationManager(this);
        try
        {
            getContentPane().setLayout(new BorderLayout(3, 3));
            mainMenuBar = new JMenuBar();
            mainMenuBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
            setJMenuBar(mainMenuBar);
            mainMenu = new JMenu(" ");
            mainMenu.setEnabled(false);
            mainMenu.setFont(DynaTheme.mediumPlainFont);
            mainMenuBar.add(mainMenu);
            mainToolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
            mainToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
            openButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Favorite.gif")));
            openButton.setToolTipText(DynaMOAD.getMSRString("QST_0006", "Add to My Folder", 3));
            openButton.setActionCommand("Open");
            openButton.addActionListener(this);
            saveButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "save", 3));
            saveButton.setActionCommand("Save");
            saveButton.addActionListener(this);
            saveButton.setEnabled(hasUpdatePermission);
            hProcessSelectButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Process.gif")));
            hProcessSelectButton.setActionCommand("hProcessSelectButton");
            hProcessSelectButton.addActionListener(this);
            hProcessSelectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0058", "Create a Process", 3));
            hProcessSelectPopupMenu = new JPopupMenu();
            hProcessSelectPopupButton = new PopupButton(hProcessSelectButton, hProcessSelectPopupMenu);
            mainToolBar.add(saveButton);
            mainToolBar.add(openButton);
            mainToolBar.add(hProcessSelectPopupButton);
            initToolbarProcessButton();
            classInfo = getClassInfo(classOuid);
            Utils.sort(groupList);
            if(fieldGroupNameToTitleMap == null)
                fieldGroupNameToTitleMap = new HashMap();
            for(int i = 0; i < groupList.size(); i++)
            {
                DOSChangeable fieldGroupDC = (DOSChangeable)groupList.get(i);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    Integer viewLocationInteger = (Integer)fieldGroupDC.get("location");
                    if(viewLocationInteger == null)
                        viewLocationInteger = new Integer(2);
                    fieldGroupNameToTitleMap.put(fieldGroupDC.get("name"), fieldGroupDC.get("title"));
                    groupPanel = UIBuilder.createFieldGroupPanel(dos, hoardData, fieldGroupDC, actionButtonList, selectButtonData, selectButtonList, dateChooserButtonData, dateChooserButtonList, collectionMap, fieldAndFieldGroupMap, this, this);
                    mwlm.add(viewLocationInteger.intValue(), groupPanel, (String)fieldGroupDC.get("title"), null, (String)fieldGroupDC.get("description"));
                    groupPanel = null;
                }
            }

            refreshInitialValufOfCodeField();
            getClass();
            if(uiMode != 0)
            {
                makeAssociationTabs();
                assoList = listAssociations(classOuid);
                if(!Utils.isNullArrayList(assoList))
                {
                    PROCESSING_MODE = 0;
                } else
                {
                    assoList = listAssociations(classOuid, Boolean.FALSE);
                    if(!Utils.isNullArrayList(assoList))
                        PROCESSING_MODE = 1;
                }
                if(!Utils.isNullArrayList(assoList))
                {
                    DOSChangeable dosAssociation = (DOSChangeable)assoList.get(0);
                    String linkViewTitleString = "Object Tree";
                    if(dosAssociation != null)
                        linkViewTitleString = (String)dosAssociation.get("description");
                    mwlm.addCenter(linkViewPane, linkViewTitleString, null, linkViewTitleString);
                    tabIndexForObjectTree = mwlm.getCenterTabbedPane().getTabCount() - 1;
                }
                if(Utils.getBoolean((Boolean)classInfo.get("is.filecontrol")))
                {
                    mwlm.addCenter(filePanel, DynaMOAD.getMSRString("WRD_0057", "File", 3), null, DynaMOAD.getMSRString("WRD_0057", "File", 3));
                    tabIndexForFile = mwlm.getCenterTabbedPane().getTabCount() - 1;
                }
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    historyData = dos.getListVersionHistory(instanceOuid);
                    if(!Utils.isNullArrayList(historyData))
                    {
                        mwlm.addCenter(historyPanel, DynaMOAD.getMSRString("WRD_0063", "Version", 3), null, DynaMOAD.getMSRString("WRD_0063", "Version History", 3));
                        tabIndexForVersion = mwlm.getCenterTabbedPane().getTabCount() - 1;
                    }
                }
                processList = wfm.listProcessByObject(instanceOuid);
                if(!Utils.isNullArrayList(processList))
                {
                    mwlm.addCenter(processPanel, DynaMOAD.getMSRString("WRD_0031", "Process", 3), null, DynaMOAD.getMSRString("WRD_0031", "Process List", 3));
                    tabIndexForProcess = mwlm.getCenterTabbedPane().getTabCount() - 1;
                }
            }
            getContentPane().add(mainToolBar, "North");
            getContentPane().add(DynaMOAD.buildStatusBar(statusField), "South");
            if(mwlm.getCenterTabbedPane() != null)
                mwlm.getCenterTabbedPane().addChangeListener(this);
            int width = 0;
            int height = 0;
            int heightCount = 0;
            JTabbedPane tempTabbedPane = null;
            Dimension dimension = null;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            tempTabbedPane = mwlm.getCenterTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = dimension.width + 5;
                height = dimension.height;
                heightCount++;
            }
            tempTabbedPane = mwlm.getTopTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = Math.max(dimension.width + 5, width);
                height += dimension.height;
                heightCount++;
            }
            tempTabbedPane = mwlm.getBottomTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = Math.max(dimension.width + 5, width);
                height += dimension.height;
                heightCount++;
            }
            tempTabbedPane = mwlm.getLeftTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = width + dimension.width + 5;
                height = Math.max(dimension.height, height);
            }
            dimension = null;
            heightCount--;
            height = height + 140 + heightCount * 32;
            width = Math.min(width += 10, screenSize.width);
            height = Math.min(height, screenSize.height);
            width = Math.max(width, 400);
            height = Math.max(height, 300);
            setSize(width, height);
            setIcon((String)classInfo.get("ouid"));
            setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void initToolbarProcessButton()
        throws IIPRequestException
    {
        if(Utils.isNullString(instanceOuid))
        {
            hProcessSelectPopupButton.setVisible(false);
            return;
        }
        ArrayList ouidList = new ArrayList(1);
        ouidList.add(instanceOuid);
        JMenu processMenu = new JMenu();
        UIBuilder.createProcessPopupMenuItems(processMenu, ouidList, DynaMOAD.dos, DynaMOAD.wfm, DynaMOAD.acl, this);
        if(!processMenu.isEnabled())
        {
            hProcessSelectPopupButton.setVisible(false);
            return;
        }
        int cnt = processMenu.getMenuComponentCount();
        if(cnt > 0)
        {
            for(int i = 0; i < cnt; i++)
                hProcessSelectPopupMenu.add(processMenu.getMenuComponent(0));

        }
        hProcessSelectPopupButton.setVisible(true);
    }

    public static ArrayList listAssociations(String ouid, Boolean direction, String mode)
    {
        ArrayList resultList = null;
        HashMap assoMap = null;
        try
        {
            assoMap = DynaMOAD.dos.listEffectiveAssociation(ouid, direction, mode);
            if(assoMap == null)
                return null;
            resultList = new ArrayList(assoMap.values());
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList listAssociations(String ouid, Boolean direction)
    {
        return listAssociations(ouid, direction, "composition");
    }

    public static ArrayList listAssociations(String ouid)
    {
        return listAssociations(ouid, Boolean.TRUE, "composition");
    }

    public void makeLinkTable(int processMode)
    {
        try
        {
            linkTreeTable = UIBuilder.createLinkTableTree(dos, instanceOuid, classOuid, getDOSGlobal, processMode, assoList, searchConditionMap);
            if(linkTreeTable == null)
            {
                linkTableScrPane.setViewportView(null);
                linkTableScrPane.updateUI();
                return;
            }
            linkListener = new LinkListener();
            linkTreeTable.addTreeSelectionListener(linkListener);
            linkRootNode = (DefaultMutableTreeNode)linkTreeTable.tree.getModel().getRoot();
            UIUtils.expandTree1Level(linkTreeTable.tree);
            LinkTreeModel linkModel = (LinkTreeModel)linkTreeTable.getTreeTableModel();
            columnOuidList = linkModel.getColumnOuidList();
            DOSChangeable dosField = null;
            columnList = linkModel.getColumnFieldList();
            if(!Utils.isNullArrayList(columnList))
            {
                chartFieldTitleList = new ArrayList();
                chartFieldDescList = new ArrayList();
                chartFieldTypeList = new ArrayList();
                chartFieldOuidList = new ArrayList();
                String typename = null;
                Iterator listKey;
                for(listKey = columnList.iterator(); listKey.hasNext();)
                {
                    dosField = (DOSChangeable)listKey.next();
                    if(dosField != null)
                    {
                        typename = fieldTypeNumToName(Utils.getByte((Byte)dosField.get("type")));
                        if(typename.equals("date") || typename.equals("date time"))
                        {
                            chartFieldTitleList.add(dosField.get("title"));
                            chartFieldDescList.add(dosField.get("description"));
                            chartFieldTypeList.add(fieldTypeNumToName(Utils.getByte((Byte)dosField.get("type"))));
                            chartFieldOuidList.add(dosField.get("ouid"));
                        }
                        dosField = null;
                    }
                }

                listKey = null;
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void makeFileTable()
    {
        getClass();
        if(uiMode == 1)
        {
            fileOpenButton.setEnabled(hasUpdatePermission);
            DOSChangeable getDC = getDOSGlobal;
            TreeNodeObject treeNodeData = new TreeNodeObject(instanceOuid, (String)getDC.get("md$number"), "Root");
            fileRootNode = new DefaultMutableTreeNode(treeNodeData);
            fileModel = new FileTreeModel(fileRootNode);
            fileTreeTable = new JTreeTable(fileModel);
            fileListener = new FileListener();
            fileTreeTable.addTreeSelectionListener(fileListener);
            fileTreeTable.getTableHeader().setBackground(DynaTheme.tableheaderColor);
            TableColumn tc;
            for(Enumeration enum = fileTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
                tc = (TableColumn)enum.nextElement();

            UIUtils.expandTree1Level(fileTreeTable.tree);
        }
    }

    public void makeHistoryTable()
    {
        classInfo = getClassInfo(classOuid);
        if(classInfo == null || !Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
            return;
        historyTableColumnNames = new ArrayList();
        historyTableColumnWidths = new ArrayList();
        historyTableColumnNames.add(DynaMOAD.getMSRString("md$number", "Number", 3));
        historyTableColumnNames.add(DynaMOAD.getMSRString("vf$version", "Version", 3));
        historyTableColumnNames.add(DynaMOAD.getMSRString("md$desc", "Description", 3));
        historyTableColumnNames.add(DynaMOAD.getMSRString("md$user", "User", 3));
        historyTableColumnNames.add(DynaMOAD.getMSRString("md$status", "Status", 3));
        historyTableColumnNames.add(DynaMOAD.getMSRString("md$mdate", "Modified Date", 3));
        historyTableColumnWidths.add(new Integer(80));
        historyTableColumnWidths.add(new Integer(30));
        historyTableColumnWidths.add(new Integer(160));
        historyTableColumnWidths.add(new Integer(100));
        historyTableColumnWidths.add(new Integer(100));
        historyTableColumnWidths.add(new Integer(80));
        historyTable = new Table(historyData, (ArrayList)historyTableColumnNames.clone(), (ArrayList)historyTableColumnWidths.clone(), 0, 240);
        historyTable.setColumnSequence(new int[] {
            1, 2, 3, 4, 5, 6
        });
        historyTable.setIndexColumn(0);
        historyTable.getTable().getTableHeader().setBackground(DynaTheme.tableheaderColor);
        historyTable.getTable().setCursor(handCursor);
        ((DynaTable)historyTable.getTable()).setCellColor(new historyColor());
        TableColumn tc;
        for(Enumeration enum = historyTable.getTable().getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
            tc = (TableColumn)enum.nextElement();

    }

    public void makeProcessTable()
    {
        if(processList == null || processList.size() == 0)
        {
            processList = null;
            return;
        }
        processTableColumnNames = new ArrayList();
        processTableColumnWidths = new ArrayList();
        processData = new ArrayList();
        DOSChangeable aProcess = null;
        ArrayList tmpArray = new ArrayList();
        for(Iterator processKey = processList.iterator(); processKey.hasNext(); tmpArray.clear())
        {
            aProcess = (DOSChangeable)processKey.next();
            tmpArray.add(aProcess.get("ouid"));
            tmpArray.add(DynaMOAD.getMSRString((String)aProcess.get("name"), (String)aProcess.get("name"), 0) + " [" + aProcess.get("identifier") + "]");
            tmpArray.add(aProcess.get("description"));
            tmpArray.add(aProcess.get("name$responsible"));
            tmpArray.add(aProcess.get("name@workflow.status"));
            tmpArray.add(aProcess.get("time.created"));
            tmpArray.add(aProcess.get("time.started"));
            tmpArray.add(aProcess.get("time.limit.to.finish"));
            tmpArray.add(aProcess.get("time.closed"));
            processData.add(tmpArray.clone());
        }

        tmpArray = null;
        processTableColumnNames.add(DynaMOAD.getMSRString("WRD_0140", "Process ID", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("name", "Name", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("description", "Description", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("name@responsible", "Responsible", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("md$status", "Status", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("time.created", "Created Time", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("time.started", "Started Time", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("time.limit.to.finish", "Due Time", 3));
        processTableColumnNames.add(DynaMOAD.getMSRString("time.started", "Closed Time", 3));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(80));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(100));
        processTableColumnWidths.add(new Integer(100));
        processTable = new Table(processData, (ArrayList)processTableColumnNames.clone(), (ArrayList)processTableColumnWidths.clone(), 0, 240);
        System.out.println("processTable creation");
        processTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8
        });
        processTable.setIndexColumn(0);
        processTable.getTable().getTableHeader().setBackground(DynaTheme.tableheaderColor);
        processTable.getTable().setCursor(handCursor);
        TableColumn tc;
        for(Enumeration enum = processTable.getTable().getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
            tc = (TableColumn)enum.nextElement();

    }

    public DOSChangeable setModifyData()
    {
        try
        {
            for(int i = 0; i < groupList.size(); i++)
            {
                DOSChangeable fieldGroupDC = (DOSChangeable)groupList.get(i);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
                    ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
                    if(inheritedFields != null)
                    {
                        if(fieldList == null)
                            fieldList = new ArrayList();
                        ArrayList tmpFieldList = new ArrayList();
                        DOSChangeable tmpData = null;
                        for(int j = 0; j < inheritedFields.size(); j++)
                        {
                            tmpData = (DOSChangeable)inheritedFields.get(j);
                            if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                                tmpFieldList.add((String)tmpData.get("ouid"));
                        }

                        if(tmpFieldList.size() > 0)
                            fieldList.addAll(0, tmpFieldList);
                    }
                    if(fieldList != null && fieldList.size() != 0)
                    {
                        byte changeable = 0;
                        Object object = null;
                        JScrollPane scrollPane = null;
                        DynaTable table = null;
                        JComponent uiComponent = null;
                        for(int j = 0; j < fieldList.size(); j++)
                        {
                            fieldUIData = dos.getField((String)fieldList.get(j));
                            String txtFieldStr = (String)fieldUIData.get("name") + (String)fieldGroupDC.get("name");
                            String fldNamStr = (String)fieldUIData.get("name");
                            byte type = ((Byte)fieldUIData.get("type")).byteValue();
                            changeable = Utils.getByte((Byte)fieldUIData.get("changeable"));
                            uiComponent = (JComponent)hoardData.get(txtFieldStr);
                            if(uiComponent != null && (uiComponent instanceof JTextField))
                            {
                                if(type == 24 || type == 25 || type == 16)
                                {
                                    ((JTextField)uiComponent).setEditable(false);
                                    ((JTextField)uiComponent).setBackground(DynaTheme.panelBackgroundColor);
                                } else
                                {
                                    String stringValue = ((JTextField)uiComponent).getText();
                                    if(type == 4)
                                        getDOSGlobal.put(fldNamStr, Utils.getDouble(stringValue));
                                    else
                                    if(type == 6)
                                        getDOSGlobal.put(fldNamStr, Utils.getInteger(stringValue));
                                    else
                                    if(type == 2)
                                        getDOSGlobal.put(fldNamStr, Utils.getByte(stringValue));
                                    else
                                    if(type == 7)
                                        getDOSGlobal.put(fldNamStr, Utils.getLong(stringValue));
                                    else
                                    if(type == 8)
                                        getDOSGlobal.put(fldNamStr, Utils.getShort(stringValue));
                                    else
                                    if(type == 5)
                                        getDOSGlobal.put(fldNamStr, Utils.getFloat(stringValue));
                                    else
                                    if(Utils.isNullString(stringValue))
                                        getDOSGlobal.put(fldNamStr, null);
                                    else
                                        getDOSGlobal.put(fldNamStr, stringValue);
                                    if(changeable == 4 || changeable == 3 && !Utils.isNullString(stringValue))
                                    {
                                        ((JTextField)uiComponent).setEditable(false);
                                        ((JTextField)uiComponent).setBackground(DynaTheme.panelBackgroundColor);
                                    } else
                                    {
                                        ((JTextField)uiComponent).setEditable(true);
                                    }
                                }
                            } else
                            if(uiComponent != null && (uiComponent instanceof JComboBox))
                            {
                                if(type == 1)
                                {
                                    if(Utils.isNullString((String)((JComboBox)uiComponent).getSelectedItem()))
                                        getDOSGlobal.put(fldNamStr, null);
                                    else
                                    if(((String)((JComboBox)uiComponent).getSelectedItem()).equals("True"))
                                        getDOSGlobal.put(fldNamStr, new Boolean(true));
                                    else
                                        getDOSGlobal.put(fldNamStr, new Boolean(false));
                                } else
                                if(type == 24 || type == 25)
                                    if(((JComboBox)uiComponent).getSelectedItem() == null)
                                    {
                                        getDOSGlobal.put(fldNamStr, null);
                                    } else
                                    {
                                        Object comboObject = ((JComboBox)uiComponent).getSelectedItem();
                                        if(comboObject instanceof MComboBoxModelObject)
                                            getDOSGlobal.put(fldNamStr, ((MComboBoxModelObject)comboObject).getOuid());
                                        comboObject = null;
                                    }
                                if(changeable == 4 || changeable == 3 && ((JComboBox)uiComponent).getSelectedItem() != null)
                                    ((JComboBox)uiComponent).setEnabled(false);
                                else
                                    ((JComboBox)uiComponent).setEnabled(true);
                            } else
                            if(uiComponent != null && (uiComponent instanceof JScrollPane))
                            {
                                scrollPane = (JScrollPane)uiComponent;
                                object = scrollPane.getViewport().getView();
                                if(object instanceof JTextArea)
                                {
                                    if(!Utils.isNullString(((JTextArea)object).getText()))
                                        getDOSGlobal.put(fldNamStr, ((JTextArea)object).getText());
                                    else
                                        getDOSGlobal.put(fldNamStr, null);
                                    if(changeable == 4 || changeable == 3 && Utils.isNullString(((JTextArea)object).getText()))
                                    {
                                        ((JTextArea)object).setEditable(false);
                                        ((JTextArea)object).setEnabled(false);
                                    } else
                                    {
                                        ((JTextArea)object).setEditable(true);
                                        ((JTextArea)object).setEnabled(true);
                                    }
                                } else
                                if(object instanceof DynaTable)
                                {
                                    table = (DynaTable)object;
                                    getCollectionTable(fieldUIData, fieldGroupDC, getDOSGlobal);
                                    if(changeable == 4 || changeable == 3 && table.getRowCount() > 0)
                                        table.setEnabled(false);
                                    else
                                        table.setEnabled(true);
                                }
                            }
                        }

                    }
                }
            }

            getDOSGlobal.setClassOuid(classOuid);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        return getDOSGlobal;
    }

    public void setFieldInObject(String sign, String selectOuid, String number)
    {
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '^');
        String mapId = (String)list.get(2);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(number);
        field.setCaretPosition(0);
        field.setActionCommand("DATATYPE_OBJECT^" + selectOuid);
        if(!Utils.isNullString(selectOuid))
            field.setCursor(Cursor.getPredefinedCursor(12));
        getDOSGlobal.put((String)hoardData.get("name@@" + mapId), selectOuid);
        field = null;
        list.clear();
        list = null;
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        ((JTextField)dateChooserButtonData.get(sign)).setText(selectOuid);
        ((JTextField)dateChooserButtonData.get(sign)).setCaretPosition(0);
    }

    private void setInitData()
    {
        HashMap tmpMap = hoardData.getValueMap();
        Iterator iterator = tmpMap.keySet().iterator();
        String mapKey = null;
        String tmpKey = null;
        String tmpVal = null;
        while(iterator.hasNext()) 
        {
            mapKey = (String)iterator.next();
            if(mapKey.startsWith("initvalue@@"))
            {
                tmpKey = mapKey.substring(11);
                tmpVal = (String)tmpMap.get(mapKey);
                getDOSGlobal.put(tmpKey, tmpVal);
            }
        }
    }

    public void setClassificationData(HashMap data)
    {
        if(data == null || data.isEmpty())
            return;
        String key = null;
        String value = null;
        DOSChangeable tempInfo = new DOSChangeable();
        DOSChangeable dosField = null;
        DOSChangeable codeItem = null;
        try
        {
            Iterator dataKey;
            for(dataKey = data.keySet().iterator(); dataKey.hasNext();)
            {
                key = (String)dataKey.next();
                dosField = DynaMOAD.dos.getField(key);
                if(dosField != null)
                {
                    value = (String)data.get(key);
                    if(!Utils.isNullString(value))
                    {
                        codeItem = DynaMOAD.dos.getCodeItem(value);
                        if(codeItem != null)
                        {
                            tempInfo.put((String)dosField.get("name"), data.get(key));
                            tempInfo.put("name@" + (String)dosField.get("name"), codeItem.get("name"));
                        }
                    }
                }
            }

            dataKey = null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        setData(tempInfo);
    }

    public void setData(DOSChangeable info)
    {
        try
        {
            hoardData.put("in.refresh", "");
            DOSChangeable classInfo = getClassInfo(classOuid);
            classInfo = null;
            for(int i = 0; i < groupList.size(); i++)
            {
                DOSChangeable fieldGroupDC = (DOSChangeable)groupList.get(i);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
                    ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
                    if(inheritedFields != null)
                    {
                        if(fieldList == null)
                            fieldList = new ArrayList();
                        ArrayList tmpFieldList = new ArrayList();
                        DOSChangeable tmpData = null;
                        for(int j = 0; j < inheritedFields.size(); j++)
                        {
                            tmpData = (DOSChangeable)inheritedFields.get(j);
                            if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                                tmpFieldList.add((String)tmpData.get("ouid"));
                        }

                        if(tmpFieldList.size() > 0)
                            fieldList.addAll(0, tmpFieldList);
                    }
                    if(fieldList != null && fieldList.size() != 0)
                    {
                        JScrollPane scrollPane = null;
                        Object object = null;
                        byte changeable = 0;
                        JComponent uiComponent = null;
                        for(int j = 0; j < fieldList.size(); j++)
                        {
                            fieldUIData = dos.getField((String)fieldList.get(j));
                            changeable = Utils.getByte((Byte)fieldUIData.get("changeable"));
                            String tmpStr = (String)fieldUIData.get("name") + (String)fieldGroupDC.get("name");
                            String fldNamStr = (String)fieldUIData.get("name");
                            byte type = ((Byte)fieldUIData.get("type")).byteValue();
                            uiComponent = (JComponent)hoardData.get(tmpStr);
                            if(uiComponent != null && (uiComponent instanceof JTextField))
                            {
                                if(type == 24 || type == 25 || type == 16)
                                {
                                    ((JTextField)uiComponent).setText((String)info.get("name@" + fldNamStr));
                                    ((JTextField)uiComponent).setActionCommand("DATATYPE_OBJECT^" + (String)info.get(fldNamStr));
                                    if(type == 16 && !Utils.isNullString((String)info.get(fldNamStr)))
                                        ((JTextField)uiComponent).setCursor(Cursor.getPredefinedCursor(12));
                                    else
                                        ((JTextField)uiComponent).setCursor(Cursor.getPredefinedCursor(0));
                                } else
                                {
                                    if(type == 2 || type == 4 || type == 5 || type == 6 || type == 7 || type == 8)
                                    {
                                        if(info.get(fldNamStr) == null)
                                            ((JTextField)uiComponent).setText("");
                                        else
                                            ((JTextField)uiComponent).setText(((Number)info.get(fldNamStr)).toString());
                                    } else
                                    {
                                        ((JTextField)uiComponent).setText((String)info.get(fldNamStr));
                                    }
                                    if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                    {
                                        ((JTextField)uiComponent).setEditable(false);
                                        ((JTextField)uiComponent).setBackground(DynaTheme.panelBackgroundColor);
                                    } else
                                    {
                                        ((JTextField)uiComponent).setEditable(true);
                                    }
                                }
                                ((JTextField)uiComponent).setCaretPosition(0);
                            } else
                            if(uiComponent != null && (uiComponent instanceof JComboBox))
                            {
                                String comboItem = "";
                                if(type == 24 || type == 25)
                                {
                                    Object comboObject = ((JComboBox)uiComponent).getModel();
                                    if(comboObject instanceof DefaultComboBoxModel)
                                    {
                                        DefaultComboBoxModel comboModel = (DefaultComboBoxModel)comboObject;
                                        String codeOuid = (String)info.get(fldNamStr);
                                        int n = comboModel.getSize();
                                        for(int m = 0; m < n && codeOuid != null; m++)
                                        {
                                            comboObject = comboModel.getElementAt(m);
                                            if((comboObject instanceof MComboBoxModelObject) && codeOuid.equals(((MComboBoxModelObject)comboObject).getOuid()))
                                            {
                                                ((JComboBox)uiComponent).setSelectedIndex(m);
                                                break;
                                            }
                                            comboObject = null;
                                        }

                                    }
                                    comboObject = null;
                                } else
                                if(type == 1)
                                {
                                    if((Boolean)info.get(fldNamStr) != null)
                                    {
                                        if(Utils.getBoolean((Boolean)info.get(fldNamStr)))
                                            comboItem = "True";
                                        else
                                            comboItem = "False";
                                    } else
                                    {
                                        comboItem = "";
                                    }
                                    ((JComboBox)uiComponent).setSelectedItem(comboItem);
                                }
                                if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                    ((JComboBox)uiComponent).setEnabled(false);
                                else
                                    ((JComboBox)uiComponent).setEnabled(true);
                            } else
                            if(uiComponent != null && (uiComponent instanceof JScrollPane))
                            {
                                scrollPane = (JScrollPane)uiComponent;
                                object = scrollPane.getViewport().getView();
                                if(object instanceof DynaTable)
                                {
                                    setCollectionTable(fieldUIData, fieldGroupDC, getDOSGlobal);
                                    if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                        ((DynaTable)object).setEnabled(false);
                                    else
                                        ((DynaTable)object).setEnabled(true);
                                } else
                                if(object instanceof JTextArea)
                                {
                                    ((JTextArea)object).setText((String)info.get(fldNamStr));
                                    ((JTextArea)object).setCaretPosition(0);
                                    if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                    {
                                        ((JTextArea)object).setEditable(false);
                                        ((JTextArea)object).setEnabled(false);
                                    } else
                                    {
                                        ((JTextArea)object).setEditable(true);
                                        ((JTextArea)object).setEnabled(true);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        hoardData.put("in.refresh", null);
    }

    public void setCollectionTable(DOSChangeable fieldData, DOSChangeable groupData, DOSChangeable data)
    {
        Table innerTable = (Table)hoardData.get((String)fieldData.get("name") + (String)groupData.get("name") + "innerTable");
        ArrayList dataList = innerTable.getList();
        if(dataList == null)
            return;
        ArrayList infoList = (ArrayList)data.get((String)fieldData.get("name"));
        if(infoList == null || infoList.size() == 0)
        {
            dataList.clear();
            innerTable.changeTableData();
            return;
        }
        ArrayList tempList = null;
        byte dataType = Utils.getByte((Byte)fieldData.get("type"));
        if(dataType == 24 || dataType == 25 || dataType == 16)
        {
            innerTable.setData((ArrayList)infoList.clone());
        } else
        {
            dataList.clear();
            for(Iterator infoKey = infoList.iterator(); infoKey.hasNext();)
            {
                tempList = new ArrayList();
                tempList.add(infoKey.next());
                dataList.add(tempList);
                tempList = null;
            }

        }
        innerTable.changeTableData();
    }

    public void getCollectionTable(DOSChangeable fieldData, DOSChangeable groupData, DOSChangeable data)
    {
        Table innerTable = (Table)hoardData.get((String)fieldData.get("name") + (String)groupData.get("name") + "innerTable");
        ArrayList dataList = innerTable.getList();
        ArrayList tempList = null;
        ArrayList infoList = (ArrayList)data.get((String)fieldData.get("name"));
        if(infoList != null)
            infoList = null;
        if(dataList != null)
        {
            byte dataType = Utils.getByte((Byte)fieldData.get("type"));
            if(dataType == 24 || dataType == 16 || dataType == 25)
            {
                tempList = (ArrayList)dataList.clone();
            } else
            {
                tempList = new ArrayList();
                for(int x = 0; x < dataList.size(); x++)
                    tempList.add(((ArrayList)dataList.get(x)).get(0));

            }
        }
        data.put((String)fieldData.get("name"), tempList);
    }

    public void fileTreeRefresh()
    {
        try
        {
            fileRootNode.removeAllChildren();
            fileTreeTable.updateUI();
            TreeNodeObject nodeData = (TreeNodeObject)fileRootNode.getUserObject();
            ArrayList rows = dos.listFile(instanceOuid);
            HashMap row = null;
            TreeNodeObject childNodeData = null;
            TreeNodeObject fileTypeNodeData = null;
            String fileType = null;
            DOSChangeable fileTypeSave = new DOSChangeable();
            DefaultMutableTreeNode fileTypeNode = null;
            int counter = 0;
            Iterator rowsKey;
            for(rowsKey = rows.iterator(); rowsKey.hasNext();)
            {
                row = (HashMap)rowsKey.next();
                fileType = (String)row.get("md$filetype.id");
                if(counter == 0)
                    fileTypeSave.putOriginalValue("fileType", fileType);
                fileTypeSave.put("fileType", fileType);
                childNodeData = new TreeNodeObject((String)row.get("md$ouid"), (String)row.get("md$filetype.description") + " " + ((Integer)row.get("md$index")).toString(), (String)row.get("md$version") + "^" + (String)row.get("md$path") + "^" + (String)row.get("md$filetype.id") + "^" + (String)row.get("md$checkedout.date") + "^" + (String)row.get("md$checkedin.date"));
                if(fileTypeSave.isChanged("fileType") || counter == 0)
                {
                    fileTypeSave.putOriginalValue("fileType", fileType);
                    fileTypeNodeData = new TreeNodeObject((String)row.get("md$filetype.id"), (String)row.get("md$filetype.description"), (String)row.get("md$filetype.id"));
                    fileTypeNode = new DefaultMutableTreeNode(fileTypeNodeData);
                    fileRootNode.add(fileTypeNode);
                }
                childNodeData.setLawData(row);
                fileTypeNode.add(new DefaultMutableTreeNode(childNodeData));
                childNodeData = null;
                row = null;
                counter++;
            }

            rowsKey = null;
            rows.clear();
            rows = null;
            nodeData.setPopulated(true);
            fileTreeTable.updateUI();
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void fileTreeRefresh(String nodeOid)
    {
        try
        {
            fileRootNode.removeAllChildren();
            fileTreeTable.updateUI();
            ArrayList rows = dos.listFile(instanceOuid);
            HashMap row = null;
            TreeNodeObject childNodeData = null;
            TreeNodeObject fileTypeNodeData = null;
            String fileType = null;
            DOSChangeable fileTypeSave = new DOSChangeable();
            DefaultMutableTreeNode fileTypeNode = null;
            int counter = 0;
            Iterator rowsKey = rows.iterator();
            TreePath path = null;
            while(rowsKey.hasNext()) 
            {
                row = (HashMap)rowsKey.next();
                fileType = (String)row.get("md$filetype.id");
                if(counter == 0)
                    fileTypeSave.putOriginalValue("fileType", fileType);
                fileTypeSave.put("fileType", fileType);
                childNodeData = new TreeNodeObject((String)row.get("md$ouid"), (String)row.get("md$filetype.description") + " " + ((Integer)row.get("md$index")).toString(), (String)row.get("md$version") + "^" + (String)row.get("md$path") + "^" + (String)row.get("md$filetype.id") + "^" + (String)row.get("md$checkedout.date") + "^" + (String)row.get("md$checkedin.date"));
                if(fileTypeSave.isChanged("fileType") || counter == 0)
                {
                    fileTypeSave.putOriginalValue("fileType", fileType);
                    fileTypeNodeData = new TreeNodeObject((String)row.get("md$filetype.id"), (String)row.get("md$filetype.description"), (String)row.get("md$filetype.id"));
                    fileTypeNode = new DefaultMutableTreeNode(fileTypeNodeData);
                    fileRootNode.add(fileTypeNode);
                }
                childNodeData.setLawData(row);
                fileTypeNode.add(new DefaultMutableTreeNode(childNodeData));
                if(childNodeData.toString().equals(nodeOid.toString()))
                    path = new TreePath(fileTypeNode.getPath());
                childNodeData = null;
                row = null;
                counter++;
            }
            rowsKey = null;
            rows.clear();
            rows = null;
            fileTreeTable.updateUI();
            fileTreeTable.tree.fireTreeExpanded(path);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void refreshLinkTable(DefaultMutableTreeNode node)
    {
        try
        {
            if(node == null)
                return;
            if(node.equals(linkRootNode))
                return;
            refreshLinkTable(linkRootNode);
            node.removeAllChildren();
            linkTreeTable.updateUI();
            if(Utils.isNullString(instanceOuid))
                return;
            ArrayList rows = null;
            TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
            if(searchConditionMap != null && !searchConditionMap.containsKey("version.condition.type"))
                searchConditionMap.put("version.condition.type", "wip");
            ArrayList tmpColumnOuidList = ((LinkTreeModel)linkTreeTable.getTreeTableModel()).getColumnOuidList();
            if(PROCESSING_MODE == 0)
            {
                System.out.println("refreshLinkTable:PROCESSING_MODE = 0 : listLinkFrom");
                if(searchConditionMap == null)
                    rows = dos.listLinkFrom((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList);
                else
                    rows = dos.listLinkFrom((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList, searchConditionMap);
            } else
            if(PROCESSING_MODE == 1)
            {
                System.out.println("refreshLinkTable:PROCESSING_MODE = 0 : listLinkTo");
                if(searchConditionMap == null)
                    rows = dos.listLinkTo((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList);
                else
                    rows = dos.listLinkTo((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList, searchConditionMap);
            }
            if(tmpColumnOuidList != null)
                tmpColumnOuidList = null;
            DefaultMutableTreeNode childNode = null;
            if(rows != null && rows.size() > 0)
            {
                ArrayList row = null;
                TreeNodeObject childNodeData = null;
                Iterator rowsKey;
                for(rowsKey = rows.iterator(); rowsKey.hasNext();)
                {
                    row = (ArrayList)rowsKey.next();
                    childNodeData = new TreeNodeObject((String)row.get(0) + "^" + dos.getClassOuid((String)row.get(0)) + "^" + row.get(row.size() - 1), (String)row.get(1), "Class");
                    childNodeData.setLawData(row);
                    childNode = new DefaultMutableTreeNode(childNodeData);
                    node.add(childNode);
                    childNodeData = null;
                    row = null;
                }

                rowsKey = null;
                rows.clear();
                rows = null;
                nodeData.setPopulated(true);
                UIUtils.expandTree1Level(linkTreeTable.tree, node);
                linkTreeTable.updateUI();
            }
            if(linkSplitPane.getRightComponent() == null)
            {
                linkTreeTable.setRowHeight(defaultRowHeight);
                linkTreeTable.getTableHeader().setBackground(DynaTheme.tableheaderColor);
                TableColumn tc;
                for(Enumeration enum = linkTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
                    tc = (TableColumn)enum.nextElement();

            } else
            {
                linkTreeTable.setRowHeight(chartPanel.getRowHeight());
                linkTreeTable.getTableHeader().setBackground(DynaTheme.tableheaderColor);
                if(chartTableHeaderRenderer == null)
                    chartTableHeaderRenderer = UIBuilder.getHeaderRenderer(chartPanel.getHeaderHeight());
                TableColumn tc;
                for(Enumeration enum = linkTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(chartTableHeaderRenderer))
                    tc = (TableColumn)enum.nextElement();

            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void refreshLinkTable()
    {
        refreshLinkTable(linkRootNode);
    }

    public void refreshLinkTable(TreeNodeObject nodeData)
    {
        if(nodeData == null)
        {
            refreshLinkTable(linkRootNode);
            return;
        }
        if(linkTreeTable == null)
            return;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
        TreeNodeObject selectedObject = (TreeNodeObject)node.getUserObject();
        if(!selectedObject.equals(nodeData))
        {
            refreshLinkTable(linkRootNode);
            return;
        } else
        {
            refreshLinkTable(node);
            return;
        }
    }

    public void createPopup()
    {
        getClass();
        if(uiMode == 1)
        {
            MouseListener LinkMousePopup = new PopupLink();
            if(linkTreeTable != null)
                linkTreeTable.addMouseListener(LinkMousePopup);
            filePopupMenu = new JPopupMenu();
            menuFileDetach = new JMenuItem();
            menuFileDetach.setText(DynaMOAD.getMSRString("WRD_0067", "File Delete", 3));
            menuFileDetach.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
            menuFileDetach.setActionCommand("Detach");
            menuFileDetach.addActionListener(this);
            menuFileCheckIn = new JMenuItem();
            menuFileCheckIn.setText(DynaMOAD.getMSRString("WRD_0061", "Check-In", 0));
            menuFileCheckIn.setIcon(new ImageIcon(getClass().getResource("/icons/CheckIn.gif")));
            menuFileCheckIn.setActionCommand("Check-In");
            menuFileCheckIn.addActionListener(this);
            menuFileCheckOut = new JMenuItem();
            menuFileCheckOut.setText(DynaMOAD.getMSRString("WRD_0060", "Check-Out", 0));
            menuFileCheckOut.setIcon(new ImageIcon(getClass().getResource("/icons/CheckOut.gif")));
            menuFileCheckOut.setActionCommand("Check-Out");
            menuFileCheckOut.addActionListener(this);
            menuFileCheckOutCancel = new JMenuItem();
            menuFileCheckOutCancel.setText(DynaMOAD.getMSRString("WRD_0166", "Check-Out Cancel", 0));
            menuFileCheckOutCancel.setActionCommand("Check-Out Cancel");
            menuFileCheckOutCancel.addActionListener(this);
            menuFileReadOnly = new JMenuItem();
            menuFileReadOnly.setText(DynaMOAD.getMSRString("WRD_0062", "Read", 3));
            menuFileReadOnly.setIcon(new ImageIcon(getClass().getResource("/icons/executionView.gif")));
            menuFileReadOnly.setActionCommand("Read Only");
            menuFileReadOnly.addActionListener(this);
            filePopupMenu.add(menuFileReadOnly);
            filePopupMenu.add(new JSeparator());
            filePopupMenu.add(menuFileCheckIn);
            filePopupMenu.add(menuFileCheckOut);
            filePopupMenu.add(menuFileCheckOutCancel);
            filePopupMenu.add(new JSeparator());
            filePopupMenu.add(menuFileDetach);
            if(fileTreeTable != null)
                fileTreeTable.addMouseListener(LinkMousePopup);
            if(processTable != null)
                processTable.getTable().addMouseListener(LinkMousePopup);
            if(historyTable != null)
                historyTable.getTable().addMouseListener(LinkMousePopup);
        }
    }

    public void makeFilterComboBoxForLink()
    {
        try
        {
            ArrayList pubFieldColumn = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid);
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + classOuid);
            if(pubFieldColumn == null && fieldColumn == null)
            {
                linkFilterComboBox = new JComboBox();
                linkFilterComboBox.setActionCommand("Link Combo");
                linkFilterComboBox.setMaximumSize(new Dimension(120, 22));
                linkFilterComboBox.addActionListener(this);
                return;
            }
            Vector filterVector = new Vector();
            filterVector.addElement(null);
            Vector itemVector = null;
            if(pubFieldColumn != null)
            {
                for(int i = 0; i < pubFieldColumn.size(); i++)
                {
                    itemVector = new Vector();
                    itemVector.addElement((String)pubFieldColumn.get(i));
                    itemVector.addElement("PUBLIC");
                    filterVector.addElement(itemVector.clone());
                    itemVector.removeAllElements();
                }

            }
            if(fieldColumn != null)
            {
                for(int i = 0; i < fieldColumn.size(); i++)
                {
                    itemVector = new Vector();
                    itemVector.addElement((String)fieldColumn.get(i));
                    itemVector.addElement("PRIVATE");
                    filterVector.addElement(itemVector.clone());
                    itemVector.removeAllElements();
                }

            }
            linkFilterComboBox = new JComboBox(filterVector);
            linkFilterComboBox.setActionCommand("Link Combo");
            linkFilterComboBox.setMaximumSize(new Dimension(120, 22));
            linkFilterComboBox.addActionListener(this);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setFilterDialogToNull()
    {
        filterDialog = null;
    }

    public void rebuildToolBar()
    {
        linkToolBar.removeAll();
        makeFilterComboBoxForLink();
        linkFilterComboBox.setActionCommand("Link Combo");
        linkFilterComboBox.setMaximumSize(new Dimension(120, 22));
        linkFilterComboBox.addActionListener(this);
        linkToolBar.add(linkSaveButton);
        linkToolBar.add(linkFilterButton);
        linkToolBar.add(linkFilterComboBox);
        linkToolBar.add(Box.createHorizontalStrut(5));
        linkToolBar.add(linkOpenButton);
        linkCompositePopupButton.addTo(linkToolBar);
        linkToolBar.add(linkChartButton);
        if(LogIn.isAdmin)
            linkToolBar.add(chartSettingButton);
        linkToolBar.add(Box.createHorizontalStrut(120));
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Open"))
        {
            if(uiMode == 1)
            {
                int res = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0007", "Add to My Folder", 3), DynaMOAD.getMSRString("QST_0006", "Add to My Folder", 3), 0);
                if(res != 0)
                    return;
                try
                {
                    HashMap folderNode = new HashMap();
                    DOSChangeable dos = getDOSGlobal;
                    boolean result = false;
                    folderNode.put("workspace.type", "PRIVATE");
                    folderNode.put("node.type", "OBJECT");
                    folderNode.put("parent", "/My Folder");
                    if(dos.get("vf$version") == null)
                        folderNode.put("name", "[" + dos.get("md$number") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                    else
                        folderNode.put("name", "[" + dos.get("md$number") + "/" + dos.get("vf$version") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                    folderNode.put("value", instanceOuid);
                    result = wks.createNode(folderNode);
                    result = true;
                    folderNode.clear();
                    folderNode = null;
                    dos = null;
                    if(result && searchConditionPanel != null)
                    {
                        TreeModel treeModel = null;
                        if(searchConditionPanel instanceof SearchCondition)
                            treeModel = DynaMOAD.searchCondition.workSpaceTree.getModel();
                        else
                        if(searchConditionPanel instanceof SearchCondition4CADIntegration)
                            treeModel = DynaMOAD.searchCondition4CADIntegration.workSpaceTree.getModel();
                        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
                        DefaultMutableTreeNode childNode = null;
                        int count = rootNode.getChildCount();
                        for(int i = 0; i < count; i++)
                        {
                            childNode = (DefaultMutableTreeNode)rootNode.getChildAt(i);
                            if(!"My Folder".equals(childNode.toString()))
                                continue;
                            childNode.removeAllChildren();
                            if(searchConditionPanel instanceof SearchCondition)
                            {
                                DynaMOAD.searchCondition.addFolders(childNode, "PRIVATE", "FOLDER", null);
                                DynaMOAD.searchCondition.workSpaceTree.updateUI();
                            } else
                            if(searchConditionPanel instanceof SearchCondition4CADIntegration)
                            {
                                DynaMOAD.searchCondition4CADIntegration.addFolders(childNode, "PRIVATE", "FOLDER", null);
                                DynaMOAD.searchCondition4CADIntegration.workSpaceTree.updateUI();
                            }
                            break;
                        }

                    }
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
            }
        } else
        if(command.equals("Close"))
            windowClosing(null);
        else
        if(command.startsWith("Save"))
            try
            {
                if(uiMode == 0)
                {
                    System.out.println("regist mode");
                    Object returnObject = Utils.executeScriptFile(this.dos.getEventName(classOuid, "add.before"), dss, this);
                    if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                    {
                        instanceOuid = this.dos.add(setModifyData());
                        DOSChangeable getDC = this.dos.get(instanceOuid);
                        getDOSGlobal = getDC;
                        Utils.executeScriptFile(this.dos.getEventName(classOuid, "add.after"), dss, this);
                        uiMode = 1;
                        setData(getDC);
                        setTitle((String)classInfo.get("title") + " " + getDC.get("md$number"));
                        if(closeAfterAdd)
                        {
                            windowClosing(null);
                            return;
                        }
                        if(!Utils.isNullString(linkedInstanceOuid) && !Utils.isNullString(instanceOuid))
                        {
                            if(comp != null)
                                ((UIGeneration)comp).setFieldInObject(linkedInstanceOuid, instanceOuid, (String)getDC.get("md$number"));
                            else
                                link(classOuid, linkedInstanceOuid, instanceOuid, null);
                            windowClosing(null);
                            return;
                        }
                        classInfo = getClassInfo(classOuid);
                        refreshPermissionValues();
                        makeAssociationTabs();
                        assoList = listAssociations(classOuid);
                        if(!Utils.isNullArrayList(assoList))
                        {
                            PROCESSING_MODE = 0;
                        } else
                        {
                            assoList = listAssociations(classOuid, Boolean.FALSE);
                            if(!Utils.isNullArrayList(assoList))
                                PROCESSING_MODE = 1;
                        }
                        if(!Utils.isNullArrayList(assoList))
                        {
                            DOSChangeable dosAssociation = (DOSChangeable)assoList.get(0);
                            String linkViewTitleString = "Object Tree";
                            if(dosAssociation != null)
                                linkViewTitleString = (String)dosAssociation.get("description");
                            mwlm.addCenter(linkViewPane, linkViewTitleString, null, linkViewTitleString);
                            tabIndexForObjectTree = mwlm.getCenterTabbedPane().getTabCount() - 1;
                        }
                        if(Utils.getBoolean((Boolean)classInfo.get("is.filecontrol")))
                        {
                            mwlm.addCenter(filePanel, DynaMOAD.getMSRString("WRD_0057", "File", 3), null, DynaMOAD.getMSRString("WRD_0057", "File", 3));
                            tabIndexForFile = mwlm.getCenterTabbedPane().getTabCount() - 1;
                        }
                        if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                        {
                            historyData = this.dos.getListVersionHistory(instanceOuid);
                            if(!Utils.isNullArrayList(historyData))
                            {
                                mwlm.addCenter(historyPanel, DynaMOAD.getMSRString("WRD_0063", "Version", 3), null, DynaMOAD.getMSRString("WRD_0063", "Version History", 3));
                                tabIndexForVersion = mwlm.getCenterTabbedPane().getTabCount() - 1;
                            }
                        }
                        processList = wfm.listProcessByObject(instanceOuid);
                        if(!Utils.isNullArrayList(processList))
                        {
                            mwlm.addCenter(processPanel, DynaMOAD.getMSRString("WRD_0031", "Process", 3), null, DynaMOAD.getMSRString("WRD_0031", "Process List", 3));
                            tabIndexForProcess = mwlm.getCenterTabbedPane().getTabCount() - 1;
                        }
                        refreshPermissionValues();
                        saveButton.setToolTipText("Modify");
                        saveButton.setIcon(new ImageIcon("icons/Modification.gif"));
                        saveButton.setEnabled(hasUpdatePermission);
                        initToolbarProcessButton();
                    }
                } else
                if(uiMode == 1)
                {
                    setModifyData();
                    if(getDOSGlobal.isChanged())
                    {
                        Object returnObject = Utils.executeScriptFile(this.dos.getEventName(classOuid, "update.before"), dss, this);
                        if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                        {
                            this.dos.set(getDOSGlobal);
                            getDOSGlobal.setOriginalValueMap(getDOSGlobal.getValueMap());
                            Utils.executeScriptFile(this.dos.getEventName(classOuid, "update.after"), dss, this);
                            JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0002", "Data Modified.", 3), DynaMOAD.getMSRString("INF_0003", "Information Message", 3), 1);
                            setTitle((String)classInfo.get("title") + "  " + (String)getDOSGlobal.get("md$number"));
                            if(sessionProperty != null)
                            {
                                tmpUIGeneration = (UIGeneration)_session.getProperty(sessionProperty);
                                tmpUIGeneration.chartView();
                            }
                        }
                    } else
                    {
                        System.out.println("no changed");
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("Link"))
        {
            if(linkTreeTable != null && linkTreeTable.tree.getSelectionPath() != null)
                try
                {
                    TreeNodeObject selectedObject = (TreeNodeObject)((DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject();
                    String tmpStr = selectedObject.getOuid();
                    String treeTableSelectedOuid = (String)Utils.tokenizeMessage(tmpStr, '^').get(0);
                    String selectedClassouid = this.dos.getClassOuid(treeTableSelectedOuid);
                    if(treeTableSelectedOuid != null)
                    {
                        FieldLinkSelect newSearchResult = new FieldLinkSelect(this, false, selectedClassouid, selectedObject);
                        newSearchResult.setVisible(true);
                    }
                }
                catch(IIPRequestException e)
                {
                    System.err.println(e);
                }
        } else
        if(command.startsWith("copyLink.object"))
        {
            DefaultMutableTreeNode node = null;
            TreeNodeObject nodeObject = null;
            String tmpStr = null;
            StringBuffer sb = new StringBuffer();
            String objectOuid = null;
            String assoOuid = null;
            TreePath paths[] = linkTreeTable.tree.getSelectionPaths();
            if(paths == null)
                return;
            sb.append("[DynaMOAD Linked Object];");
            sb.append(paths.length);
            java.util.List list = null;
            for(int n = 0; n < paths.length; n++)
            {
                node = (DefaultMutableTreeNode)paths[n].getLastPathComponent();
                if(node != null)
                {
                    nodeObject = (TreeNodeObject)node.getUserObject();
                    if(nodeObject != null)
                    {
                        tmpStr = nodeObject.getOuid();
                        objectOuid = (String)Utils.tokenizeMessage(tmpStr, '^').get(0);
                        sb.append(';');
                        sb.append(objectOuid);
                        list = Utils.tokenizeMessage(command, '^');
                        if(list.size() > 1)
                        {
                            sb.append('^');
                            sb.append((String)Utils.tokenizeMessage(tmpStr, '^').get(2));
                        }
                    }
                }
            }

            if(sb.length() > 0)
                Utils.copyToClipboard(sb.toString());
        } else
        if(command.equals("pasteLink.object"))
        {
            String tmpStr = ((TreeNodeObject)((DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getOuid();
            String selectedOuid = (String)Utils.tokenizeMessage(tmpStr, '^').get(0);
            String selectedClassOuid = null;
            try
            {
                selectedClassOuid = this.dos.getClassOuid(selectedOuid);
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
            if(Utils.isNullString(selectedOuid))
            {
                System.out.println("Selected Ouid is null!!!");
                return;
            }
            if(!Utils.isClipboardContainDOSObjects(this))
                return;
            String clipString = Utils.pasteFromClipboard(this);
            java.util.List list = Utils.tokenizeMessageWithNoTrim(clipString, ';');
            if(list == null || list.size() < 3)
                return;
            System.out.println("Clipboard String = " + clipString);
            String objectOuid = null;
            String assoOuid = null;
            ArrayList ouids = new ArrayList();
            ArrayList assoOuids = new ArrayList();
            if(clipString.startsWith("[DynaMOAD Object];"))
            {
                System.out.println("Clipboard Source is SearchResult");
                for(int n = 2; n < list.size(); n++)
                {
                    objectOuid = (String)list.get(n);
                    ouids.add(objectOuid);
                    assoOuids.add(null);
                    System.out.println("Clipboard instance ouid = " + objectOuid);
                }

            } else
            if(clipString.startsWith("[DynaMOAD Folder];"))
            {
                System.out.println("Clipboard source is Workspace");
                for(int n = 2; n < list.size(); n++)
                {
                    String tmpString = (String)list.get(n);
                    java.util.List test = Utils.tokenizeMessageWithNoTrim(tmpString, '^');
                    if(test.size() > 2)
                    {
                        objectOuid = (String)test.get(2);
                        ouids.add(objectOuid);
                        assoOuids.add(null);
                        System.out.println("test instance ouid = " + objectOuid);
                    }
                }

            } else
            if(clipString.startsWith("[DynaMOAD Linked Object];"))
            {
                System.out.println("Clipboard source is Object Tree");
                for(int n = 2; n < list.size(); n++)
                {
                    String tmpString = (String)list.get(n);
                    java.util.List test = Utils.tokenizeMessageWithNoTrim(tmpString, '^');
                    if(test.size() > 1)
                    {
                        objectOuid = (String)test.get(0);
                        assoOuid = (String)test.get(1);
                    } else
                    {
                        objectOuid = (String)test.get(0);
                        assoOuid = null;
                    }
                    ouids.add(objectOuid);
                    assoOuids.add(assoOuid);
                    System.out.println("Clipboard instance ouid = " + objectOuid);
                }

            }
            if(ouids != null && !ouids.isEmpty())
            {
                for(int i = 0; i < ouids.size(); i++)
                {
                    link(selectedClassOuid, selectedOuid, (String)ouids.get(i), (String)assoOuids.get(i));
                    System.out.println("source = " + selectedOuid + " : ouids = " + ouids.get(i) + " : assoOuids = " + assoOuids.get(i));
                }

            }
        } else
        if(command.equals("Composite of"))
        {
            treeOption = 0;
            if(linkRootNode != null)
            {
                if(PROCESSING_MODE != 0)
                {
                    assoList = null;
                    assoList = listAssociations(classOuid);
                    PROCESSING_MODE = 0;
                }
                linkRootNode.removeAllChildren();
                linkTreeTable = null;
                linkListener = null;
                expansionListener = null;
                if(assoList == null)
                {
                    linkTableScrPane.setViewportView(linkTreeTable);
                    linkTableScrPane.updateUI();
                    return;
                }
                TreeNodeObject nodeData = (TreeNodeObject)linkRootNode.getUserObject();
                nodeData.setPopulated(false);
                makeLinkTable(PROCESSING_MODE);
                if(linkChartScrPane.isVisible())
                    linkTableScrPane.setVerticalScrollBar(linkChartScrPane.getVerticalScrollBar());
                createPopup();
                linkTableScrPane.setViewportView(linkTreeTable);
                linkTableScrPane.updateUI();
                linkSplitPane.setRightComponent(null);
            }
            linkTreeTable.setRowHeight(defaultRowHeight);
            linkCompositeButton.setIcon(new ImageIcon(getClass().getResource("/icons/CompositeOf.gif")));
            linkCompositeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0126", "Composite of", 0));
            linkCompositeButton.setActionCommand("Composite of");
        } else
        if(command.equals("Inverse Process"))
        {
            if(linkRootNode != null)
            {
                if(PROCESSING_MODE != 1)
                {
                    assoList = null;
                    assoList = listAssociations(classOuid, Boolean.FALSE);
                    PROCESSING_MODE = 1;
                }
                linkRootNode.removeAllChildren();
                linkTreeTable = null;
                linkListener = null;
                expansionListener = null;
                if(assoList == null)
                {
                    linkTableScrPane.setViewportView(linkTreeTable);
                    linkTableScrPane.updateUI();
                    linkSplitPane.setRightComponent(null);
                    return;
                }
                TreeNodeObject nodeData = (TreeNodeObject)linkRootNode.getUserObject();
                nodeData.setPopulated(false);
                makeLinkTable(PROCESSING_MODE);
                if(linkChartScrPane.isVisible())
                    linkTableScrPane.setVerticalScrollBar(linkChartScrPane.getVerticalScrollBar());
                createPopup();
                linkTableScrPane.setViewportView(linkTreeTable);
                linkTableScrPane.updateUI();
                linkSplitPane.setRightComponent(null);
            }
            linkTreeTable.setRowHeight(defaultRowHeight);
            linkTreeTable.getTableHeader().setBackground(DynaTheme.tableheaderColor);
            TableColumn tc;
            for(Enumeration enum = linkTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
                tc = (TableColumn)enum.nextElement();

            linkCompositeButton.setIcon(new ImageIcon(getClass().getResource("/icons/InverseProcess.gif")));
            linkCompositeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0127", "Where used", 0));
            linkCompositeButton.setActionCommand("Inverse Process");
        } else
        if(command.equals("Link Filter"))
        {
            if(filterDialog == null)
            {
                filterDialog = new FilterDialogForLink(this, (Component)actionEvent.getSource(), false, classOuid, instanceOuid, PROCESSING_MODE, assoList);
                filterDialog.setVisible(true);
            } else
            {
                filterDialog.toFront();
            }
        } else
        if(command.equals("Chart View"))
        {
            if(_session == null)
                _session = new Session("chartSession");
            sessionProperty = instanceOuid;
            if(_session.getProperty(sessionProperty) == null)
                _session.setProperty(sessionProperty, this);
            linkChartScrPane.setVerticalScrollBar(linkTableScrPane.getVerticalScrollBar());
            chartView();
        } else
        if(command.equals("Chart Setting"))
        {
            ArrayList listLocal = new ArrayList();
            for(int i = 0; i < chartFieldTitleList.size(); i++)
            {
                ArrayList rowList = new ArrayList();
                rowList.add(chartFieldTitleList.get(i));
                rowList.add(chartFieldDescList.get(i));
                rowList.add(chartFieldTypeList.get(i));
                rowList.add(chartFieldOuidList.get(i));
                listLocal.add(rowList.clone());
            }

            GanttChartSetting gcs = new GanttChartSetting(this, classOuid, listLocal);
            UIUtils.setLocationRelativeTo(gcs, this);
            gcs.setVisible(true);
        } else
        if(command.equals("networkView"))
            networkView();
        else
        if(command.equals("File Open"))
        {
            String defaultFolder = null;
            try
            {
                defaultFolder = DynaMOAD.wks.getPrivateDefaultFolder(LogIn.userID);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            JFileChooser fileChooser = null;
            if(Utils.isNullString(defaultFolder))
                fileChooser = new JFileChooser();
            else
                fileChooser = new JFileChooser(defaultFolder);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.addChoosableFileFilter(new SoftWareFilter(""));
            int returnVal = fileChooser.showDialog(this, DynaMOAD.getMSRString("WRD_0083", "File Attach", 0));
            if(returnVal == 0)
            {
                files = fileChooser.getSelectedFiles();
                Runnable batchUpload = new Runnable() {

                    public void run()
                    {
                        if(UIGeneration.this.files == null)
                            return;
                        files = UIGeneration.this.files;
                        String filePath = "";
                        File file = null;
                        HashMap fileInfo = null;
                        String serverPath = null;
                        try
                        {
                            for(int i = 0; i < files.length; i++)
                            {
                                file = files[i];
                                if(file != null)
                                {
                                    filePath = file.getAbsolutePath();
                                    if(file == null || !file.isFile())
                                        return;
                                    File baseFile = new File(filePath);
                                    String fileName = baseFile.getName();
                                    int index = fileName.lastIndexOf('.');
                                    if(index < 0)
                                        return;
                                    index++;
                                    String extension = fileName.substring(index);
                                    String CADName = (String)dos.getClass(classOuid).get("name");
                                    fileInfo = new HashMap();
                                    if(extension.equals("prt"))
                                    {
                                        if(CADName.equals("Unigraphics Assembly"))
                                            fileInfo.put("md$filetype.id", "ug-assembly");
                                        else
                                        if(CADName.equals("Unigraphics Part"))
                                            fileInfo.put("md$filetype.id", "ug-part");
                                        else
                                            fileInfo.put("md$filetype.id", dss.getFileTypeId(filePath.toLowerCase()));
                                    } else
                                    {
                                        fileInfo.put("md$filetype.id", dss.getFileTypeId(filePath.toLowerCase()));
                                    }
                                    fileInfo.put("md$des", filePath);
                                    fileInfo.put("md$ouid", instanceOuid);
                                    Object returnObject = Utils.executeScriptFile(dos.getEventName(dos.getClassOuid(instanceOuid), "file.attach.before"), dss, fileInfo);
                                    if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                                        return;
                                    serverPath = dos.attachFile(instanceOuid, fileInfo);
                                    if(ftd == null)
                                        ftd = new FileTransferDialog(UIGeneration.this, false);
                                    ftd.setMaximumSize((new Long(file.length())).intValue());
                                    UIUtils.setLocationRelativeTo(ftd, UIGeneration.this);
                                    ftd.setVisible(true);
                                    upload(serverPath, filePath, new FileCallBack());
                                    Utils.executeScriptFile(dos.getEventName(dos.getClassOuid(instanceOuid), "file.attach.after"), dss, fileInfo);
                                }
                            }

                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        fileTreeRefresh();
                    }

                    File files[];

            
            {
                files = null;
            }
                };
                (new Thread(batchUpload)).start();
            }
        } else
        if(command.equals("Detach"))
            try
            {
                int res = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_B422", "File\uC744 \uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0), 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")));
                if(res != 0)
                    return;
                DefaultMutableTreeNode detachNode = (DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent();
                String treeTableSelectedOuid = ((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getOuid();
                if(treeTableSelectedOuid == null || detachNode.getLevel() == 0)
                    return;
                String tempStr = ((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getDescription();
                java.util.List tempList = Utils.tokenizeMessage(tempStr, '^');
                String treeTableSelectedVersion = (String)tempList.get(0);
                String treeTableSelectedPath = (String)tempList.get(1);
                HashMap fileInfo = new HashMap();
                fileInfo.put("md$ouid", treeTableSelectedOuid);
                fileInfo.put("md$path", treeTableSelectedPath);
                fileInfo.put("md$version", treeTableSelectedVersion);
                Object returnObject = Utils.executeScriptFile(this.dos.getEventName(this.dos.getClassOuid(instanceOuid), "file.detach.before"), dss, fileInfo);
                if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                    return;
                boolean isDetach = this.dos.detachFile(instanceOuid, fileInfo);
                Utils.executeScriptFile(this.dos.getEventName(this.dos.getClassOuid(instanceOuid), "file.detach.after"), dss, fileInfo);
                DefaultMutableTreeNode parentTree = (DefaultMutableTreeNode)detachNode.getParent();
                parentTree.remove(detachNode);
                if(parentTree.getChildCount() == 0)
                    fileRootNode.remove(parentTree);
                fileTreeTable.updateUI();
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("Check-In"))
        {
            CheckIn checkIn = new CheckIn(this, false, ((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getLawData());
            checkIn.setSession(_session);
            checkIn.setVisible(true);
        } else
        if(command.equals("Check-Out Cancel"))
            try
            {
                HashMap fileInfoMap = (HashMap)((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getLawData();
                this.dos.cancelCheckoutFile((String)fileInfoMap.get("md$ouid"), fileInfoMap);
                fileTreeRefresh((String)fileInfoMap.get("md$filetype.description") + " " + (Integer)fileInfoMap.get("md$index"));
                if(searchConditionPanel != null && (searchConditionPanel instanceof SearchCondition4CADIntegration))
                {
                    if(_session != null && (((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).cadType.equals("4") || ((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).cadType.equals("5")))
                    {
                        _session.setProperty("dynapdm.cadintegration.drawing", null);
                        _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                    }
                } else
                if(searchResultPanel != null && (searchResultPanel instanceof SearchResult4CADIntegration) && _session != null && (((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).cadType.equals("4") || ((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).cadType.equals("5")))
                {
                    _session.setProperty("dynapdm.cadintegration.drawing", null);
                    _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("Check-Out"))
        {
            CheckOut checkedOut = new CheckOut(this, false, ((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getLawData());
            checkedOut.setSession(_session);
            checkedOut.setVisible(true);
            if(DynaMOAD.startFromCAD)
            {
                if((searchResultPanel instanceof SearchResult4CADIntegration) && searchResultPanel != null)
                    ((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).windowClosing(null);
                else
                if((searchConditionPanel instanceof SearchCondition4CADIntegration) && searchConditionPanel != null)
                    ((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).windowClosing(null);
                windowClosing(null);
            }
        } else
        if(command.equals("Read Only"))
        {
            HashMap tmpMap = (HashMap)((TreeNodeObject)((DefaultMutableTreeNode)fileTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getLawData();
            CheckOut checkedOut = new CheckOut(this, false, tmpMap);
            String fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
            File downLoadFile = new File((String)tmpMap.get("md$description"));
            String workingDirectory = System.getProperty("user.dir") + fileSeperator + "tmp" + fileSeperator + downLoadFile.getName();
            checkedOut.setSession(_session);
            checkedOut.setPreselectedFilePath(workingDirectory);
            checkedOut.checkOutCheckBox.setSelected(false);
            checkedOut.downloadCheckBox.setSelected(true);
            checkedOut.invokeCheckBox.setSelected(true);
            checkedOut.setReadOnlyModel(true);
            checkedOut.processButton.doClick();
            checkedOut.setVisible(false);
            checkedOut = null;
            if(DynaMOAD.startFromCAD)
            {
                if((searchResultPanel instanceof SearchResult4CADIntegration) && searchResultPanel != null)
                    ((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).windowClosing(null);
                else
                if((searchConditionPanel instanceof SearchCondition4CADIntegration) && searchConditionPanel != null)
                    ((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).windowClosing(null);
                windowClosing(null);
            }
        } else
        if(command.equals("Unlink"))
        {
            int res = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0002", "Selected link(s) of object will be deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0), 0);
            if(res != 0)
                return;
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
            if(parentNode == null)
                return;
            TreeNodeObject selectedObject = (TreeNodeObject)selectedNode.getUserObject();
            TreeNodeObject parentObject = (TreeNodeObject)parentNode.getUserObject();
            String end1 = (String)Utils.tokenizeMessage(parentObject.getOuid(), '^').get(0);
            String end2 = (String)Utils.tokenizeMessage(selectedObject.getOuid(), '^').get(0);
            try
            {
                String clsOuidEnd1 = this.dos.getClassOuid(end1);
                String clsOuidEnd2 = this.dos.getClassOuid(end2);
                ArrayList superClassList1 = this.dos.listAllSuperClassOuid(clsOuidEnd1);
                if(superClassList1 == null)
                    superClassList1 = new ArrayList();
                superClassList1.add(0, clsOuidEnd1);
                ArrayList superClassList2 = this.dos.listAllSuperClassOuid(clsOuidEnd2);
                if(superClassList2 == null)
                    superClassList2 = new ArrayList();
                superClassList2.add(0, clsOuidEnd2);
                ArrayList listAsso = listAssociations(clsOuidEnd1);
                if(listAsso != null)
                {
                    for(int i = 0; i < listAsso.size(); i++)
                    {
                        DOSChangeable dosAssociation = (DOSChangeable)listAsso.get(i);
                        if(superClassList1.contains((String)dosAssociation.get("end1.ouid@class")) && superClassList2.contains((String)dosAssociation.get("end2.ouid@class")))
                        {
                            HashMap tempMap = new HashMap();
                            tempMap.put("end1", end1);
                            tempMap.put("end2", end2);
                            Object returnObject = Utils.executeScriptFile(this.dos.getEventName(clsOuidEnd1, "unlink.before"), dss, tempMap);
                            if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                            {
                                if(Utils.isNullString((String)dosAssociation.get("ouid@class")))
                                {
                                    this.dos.unlink(end1, end2);
                                } else
                                {
                                    String assoInstanceOuid = (String)Utils.tokenizeMessage(selectedObject.getOuid(), '^').get(2);
                                    this.dos.unlink(end1, end2, assoInstanceOuid);
                                }
                                Utils.executeScriptFile(this.dos.getEventName(clsOuidEnd1, "unlink.after"), dss, tempMap);
                            }
                            tempMap.clear();
                            tempMap = null;
                        }
                    }

                }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
            parentNode.remove(selectedNode);
            refreshLinkTable(parentNode);
        } else
        if(command.equals("AssoInfo"))
            try
            {
                String end1Ouid = ((TreeNodeObject)linkRootNode.getUserObject()).getOuid();
                String end2Ouid = ((TreeNodeObject)((DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent()).getUserObject()).getOuid();
                String clsOuidEnd1 = this.dos.getClassOuid((String)Utils.tokenizeMessage(end1Ouid, '^').get(0));
                String clsOuidEnd2 = this.dos.getClassOuid(end2Ouid);
                ArrayList superClassList1 = this.dos.listAllSuperClassOuid(clsOuidEnd1);
                if(superClassList1 == null)
                    superClassList1 = new ArrayList();
                superClassList1.add(0, clsOuidEnd1);
                ArrayList superClassList2 = this.dos.listAllSuperClassOuid(clsOuidEnd2);
                if(superClassList2 == null)
                    superClassList2 = new ArrayList();
                superClassList2.add(0, clsOuidEnd2);
                ArrayList listAsso = assoList;
                for(int i = 0; i < listAsso.size(); i++)
                {
                    DOSChangeable dosAssociation = (DOSChangeable)listAsso.get(i);
                    if(!superClassList1.contains((String)dosAssociation.get("end1.ouid@class")) || !superClassList2.contains((String)dosAssociation.get("end2.ouid@class")) || Utils.isNullString((String)dosAssociation.get("ouid@class")))
                        continue;
                    DOSChangeable dosClass = this.dos.getClass((String)dosAssociation.get("ouid@class"));
                    ArrayList fieldGroupInfo = this.dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                    Utils.sort(fieldGroupInfo);
                    ArrayList endOidList = new ArrayList();
                    endOidList.add((String)Utils.tokenizeMessage(end1Ouid, '^').get(0));
                    endOidList.add(end2Ouid);
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
                    TreeNodeObject selectedObject = (TreeNodeObject)selectedNode.getUserObject();
                    String assoOuid = (String)Utils.tokenizeMessage(selectedObject.getOuid(), '^').get(2);
                    AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), assoOuid, 1, endOidList, (String)dosAssociation.get("ouid"));
                    DOSChangeable getAssoInfo = this.dos.get(assoOuid);
                    AssoClsGeneration.setData(getAssoInfo);
                    AssoClsGeneration.setVisible(true);
                    break;
                }

            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("InfoInLink"))
            try
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
                TreeNodeObject selectedObject = (TreeNodeObject)selectedNode.getUserObject();
                if(selectedNode.getLevel() == 0)
                    return;
                String selectOuid = (String)Utils.tokenizeMessage(selectedObject.getOuid(), '^').get(0);
                String selectClassOuid = this.dos.getClassOuid(selectOuid);
                UIGeneration uiGeneration = new UIGeneration(searchResultPanel, selectClassOuid, selectOuid, 1, tmpSelectedNode, this, instanceOuid);
                uiGeneration.setSession(_session);
                uiGeneration.setVisible(true);
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
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("Link Combo"))
            try
            {
                HashMap searchCondition = new HashMap();
                Vector selectedItem = (Vector)linkFilterComboBox.getSelectedItem();
                if(selectedItem == null)
                    searchConditionMap = null;
                else
                if(((String)selectedItem.elementAt(1)).equals("PUBLIC"))
                {
                    ArrayList filterField = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid + "/" + selectedItem.elementAt(0));
                    for(int i = 0; i < filterField.size(); i++)
                    {
                        String filterID = (String)filterField.get(i);
                        String value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid + "/" + selectedItem.elementAt(0) + "/" + filterID);
                        searchCondition.put(filterID, value);
                        searchConditionMap = searchCondition;
                        treeViewConditionMap.put("Default", searchCondition);
                    }

                } else
                if(((String)selectedItem.elementAt(1)).equals("PRIVATE"))
                {
                    ArrayList filterField = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + classOuid + "/" + selectedItem.elementAt(0));
                    for(int i = 0; i < filterField.size(); i++)
                    {
                        String filterID = (String)filterField.get(i);
                        String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/FILTER/" + classOuid + "/" + selectedItem.elementAt(0) + "/" + filterID);
                        searchCondition.put(filterID, value);
                        searchConditionMap = searchCondition;
                        treeViewConditionMap.put("Default", searchCondition);
                    }

                }
                refreshLinkTable(linkRootNode);
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("processDetail"))
        {
            int row = processTable.getTable().getSelectedRow();
            String ouidRow = null;
            CreateProcess createProcess = null;
            if(row >= 0)
            {
                ouidRow = processTable.getSelectedOuidRow(row);
                createProcess = new CreateProcess(1, (String)((ArrayList)processData.get((new Integer(ouidRow)).intValue())).get(0), instanceOuid);
                createProcess = null;
            }
        } else
        if(command.startsWith("ObjectSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '^');
                    String classOuid2 = (String)testList.get(1);
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(!Utils.isNullString(classOuid2))
                    {
                        String values[] = FieldLinkSelect.showDialog(this, (Component)actionEvent.getSource(), classOuid2, command);
                        if(values != null)
                        {
                            field.setText(values[1]);
                            field.setCaretPosition(0);
                            field.setActionCommand("DATATYPE_OBJECT^" + values[0]);
                            if(!Utils.isNullString(values[0]))
                                field.setCursor(Cursor.getPredefinedCursor(12));
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), values[0]);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), values[1]);
                        }
                    } else
                    {
                        System.out.println("type class ouid is Null");
                    }
                }

        } else
        if(command.startsWith("CodeSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '^');
                    String codeOuid = (String)testList.get(1);
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(!Utils.isNullString(codeOuid))
                    {
                        String values[] = CodeSelectDialog.showDialog(this, (Component)actionEvent.getSource(), true, codeOuid, command);
                        if(values != null)
                        {
                            field.setText(values[2]);
                            field.setCaretPosition(0);
                            if(!Utils.isNullString(values[1]))
                                field.setCursor(Cursor.getPredefinedCursor(12));
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), values[1]);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), values[2]);
                        }
                    } else
                    {
                        System.out.println("code ouid is Null");
                    }
                }

        } else
        if(command.startsWith("CodeReferenceSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '^');
                    String fieldOuid = (String)testList.get(1);
                    String codeOuid = null;
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(Utils.isNullString(fieldOuid))
                        return;
                    try
                    {
                        codeOuid = DynaMOAD.dos.lookupCodeFromSelectionTable(fieldOuid, getInstanceData());
                        if(Utils.isNullString(codeOuid))
                            return;
                        DOSChangeable code = this.dos.getCode(codeOuid);
                        if(code == null)
                            return;
                        code = null;
                    }
                    catch(IIPRequestException e)
                    {
                        e.printStackTrace();
                    }
                    if(!Utils.isNullString(codeOuid))
                    {
                        String values[] = CodeSelectDialog.showDialog(this, (Component)actionEvent.getSource(), true, codeOuid, command);
                        if(values != null)
                        {
                            field.setText(values[2]);
                            field.setCaretPosition(0);
                            if(!Utils.isNullString(values[1]))
                                field.setCursor(Cursor.getPredefinedCursor(12));
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), values[1]);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), values[2]);
                        }
                    } else
                    {
                        System.out.println("code ouid is Null");
                    }
                }

        } else
        if(command.startsWith("DateChooserButtonClick"))
        {
            for(int i = 0; i < dateChooserButtonList.size(); i++)
                if(command.equals(dateChooserButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)dateChooserButtonList.get(i), '^');
                    String classOuid2 = (String)testList.get(1);
                    if(classOuid2 != null)
                    {
                        new DynaDateChooser(this);
                        Date date = DynaDateChooser.showDialog((Component)actionEvent.getSource(), null);
                        if(date != null)
                            setFieldInDateField(sdfYMD.format(date), (String)dateChooserButtonList.get(i));
                        else
                            setFieldInDateField("", (String)dateChooserButtonList.get(i));
                    } else
                    {
                        System.out.println("type class ouid is Null");
                    }
                }

        } else
        if(command.startsWith("ActionButtonClick"))
        {
            for(int i = 0; i < actionButtonList.size(); i++)
                if(command.equals(((ArrayList)actionButtonList.get(i)).get(1)))
                    try
                    {
                        Utils.executeScriptFile(this.dos.getActionScriptName((String)((ArrayList)actionButtonList.get(i)).get(0)), dss, this);
                    }
                    catch(IIPRequestException ie)
                    {
                        ie.printStackTrace();
                    }

        } else
        if(command.startsWith("assoAdd/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
            FieldLinkSelect newSearchResult = new FieldLinkSelect(this, false, classOuid, instanceOuid, associationOuid);
            newSearchResult.setVisible(true);
        } else
        if(command.startsWith("assoRemove/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                ArrayList data = table.getList();
                int row = table.getTable().getSelectedRow();
                String ouidRow = null;
                if(row >= 0)
                {
                    ouidRow = table.getSelectedOuidRow(row);
                    ArrayList selectedRowData = (ArrayList)data.get((new Integer(ouidRow)).intValue());
                    String selectOuid = (String)selectedRowData.get(0);
                    DOSChangeable dosAsso = this.dos.getAssociation(associationOuid);
                    if(dosAsso == null)
                        return;
                    String assoClassOuid = (String)dosAsso.get("ouid@class");
                    ArrayList superClassList = this.dos.listAllSuperClassOuid(classOuid);
                    superClassList.add(classOuid);
                    if(superClassList.contains(dosAsso.get("end1.ouid@class")))
                    {
                        HashMap tempMap = new HashMap();
                        tempMap.put("end1", instanceOuid);
                        tempMap.put("end2", selectOuid);
                        Object returnObject = Utils.executeScriptFile(this.dos.getEventName(classOuid, "unlink.before"), dss, tempMap);
                        if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                        {
                            if(Utils.isNullString(assoClassOuid))
                            {
                                this.dos.unlink(instanceOuid, selectOuid);
                            } else
                            {
                                String assoInstanceOuid = (String)selectedRowData.get(selectedRowData.size() - 1);
                                this.dos.unlink(instanceOuid, selectOuid, assoInstanceOuid);
                            }
                            Utils.executeScriptFile(this.dos.getEventName(classOuid, "unlink.after"), dss, tempMap);
                        }
                        tempMap.clear();
                        tempMap = null;
                    } else
                    if(superClassList.contains(dosAsso.get("end2.ouid@class")))
                    {
                        HashMap tempMap = new HashMap();
                        tempMap.put("end1", selectOuid);
                        tempMap.put("end2", instanceOuid);
                        Object returnObject = Utils.executeScriptFile(this.dos.getEventName(classOuid, "unlink.before"), dss, tempMap);
                        if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                        {
                            if(Utils.isNullString(assoClassOuid))
                            {
                                this.dos.unlink(selectOuid, instanceOuid);
                            } else
                            {
                                String assoInstanceOuid = (String)selectedRowData.get(selectedRowData.size() - 1);
                                this.dos.unlink(selectOuid, instanceOuid, assoInstanceOuid);
                            }
                            Utils.executeScriptFile(this.dos.getEventName(classOuid, "unlink.after"), dss, tempMap);
                        }
                        tempMap.clear();
                        tempMap = null;
                    }
                    dosAsso.clear();
                    dosAsso = null;
                    refreshAssociationTable(associationOuid);
                }
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        } else
        if(command.startsWith("assoRefresh/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
            refreshAssociationTable(associationOuid);
        } else
        if(command.startsWith("assoCopy/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                ArrayList data = table.getList();
                int selectedRows[] = table.getTable().getSelectedRows();
                if(selectedRows == null && selectedRows.length < 1)
                    return;
                StringBuffer sb = new StringBuffer();
                String objectOuid = null;
                String ouidRow = null;
                sb.append("[DynaMOAD Linked Object];");
                sb.append(selectedRows.length);
                for(int n = 0; n < selectedRows.length; n++)
                {
                    ouidRow = table.getSelectedOuidRow(selectedRows[n]);
                    ArrayList rowData = (ArrayList)data.get((new Integer(ouidRow)).intValue());
                    if(ouidRow != null)
                        table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                    if(ouidRow != null)
                        objectOuid = (String)rowData.get(0);
                    else
                        objectOuid = (String)((ArrayList)data.get((new Integer(selectedRows[n])).intValue())).get(0);
                    sb.append(';');
                    sb.append(objectOuid);
                    DOSChangeable dosAssociation = this.dos.getAssociation(associationOuid);
                    if(!Utils.isNullString((String)dosAssociation.get("ouid@class")))
                    {
                        String assoOuid = (String)rowData.get(rowData.size() - 1);
                        sb.append('^');
                        sb.append(assoOuid);
                    }
                }

                if(sb.length() > 0)
                    Utils.copyToClipboard(sb.toString());
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        } else
        if(command.startsWith("assoPaste/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            if(!Utils.isClipboardContainDOSObjects(this))
                return;
            String clipString = Utils.pasteFromClipboard(this);
            java.util.List list = Utils.tokenizeMessageWithNoTrim(clipString, ';');
            if(list == null || list.size() < 3)
                return;
            System.out.println("Clipboard String = " + clipString);
            String objectOuid = null;
            String assoOuid = null;
            ArrayList ouids = new ArrayList();
            ArrayList assoOuids = new ArrayList();
            if(clipString.startsWith("[DynaMOAD Object];"))
            {
                System.out.println("Clipboard Source is SearchResult");
                for(int n = 2; n < list.size(); n++)
                {
                    objectOuid = (String)list.get(n);
                    ouids.add(objectOuid);
                    assoOuids.add(null);
                    System.out.println("Clipboard instance ouid = " + objectOuid);
                }

            } else
            if(clipString.startsWith("[DynaMOAD Folder];"))
            {
                System.out.println("Clipboard source is Workspace");
                for(int n = 2; n < list.size(); n++)
                {
                    String tmpString = (String)list.get(n);
                    java.util.List test = Utils.tokenizeMessageWithNoTrim(tmpString, '^');
                    if(test.size() > 2)
                    {
                        objectOuid = (String)test.get(2);
                        ouids.add(objectOuid);
                        assoOuids.add(null);
                        System.out.println("test instance ouid = " + objectOuid);
                    }
                }

            } else
            if(clipString.startsWith("[DynaMOAD Linked Object];"))
            {
                System.out.println("Clipboard source is Object Tree");
                for(int n = 2; n < list.size(); n++)
                {
                    String tmpString = (String)list.get(n);
                    java.util.List test = Utils.tokenizeMessageWithNoTrim(tmpString, '^');
                    if(test.size() > 1)
                    {
                        objectOuid = (String)test.get(0);
                        assoOuid = (String)test.get(1);
                    } else
                    {
                        objectOuid = (String)test.get(0);
                        assoOuid = null;
                    }
                    ouids.add(objectOuid);
                    assoOuids.add(assoOuid);
                    System.out.println("Clipboard instance ouid = " + objectOuid);
                }

            }
            if(ouids != null && !ouids.isEmpty())
            {
                for(int i = 0; i < ouids.size(); i++)
                {
                    link(classOuid, instanceOuid, (String)ouids.get(i), (String)assoOuids.get(i));
                    System.out.println("source = " + instanceOuid + " : ouids = " + ouids.get(i) + " : assoOuids = " + assoOuids.get(i));
                }

            }
        } else
        if(command.startsWith("assoDetail/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                ArrayList data = table.getList();
                int row = table.getTable().getSelectedRow();
                String ouidRow = null;
                if(row >= 0)
                {
                    ouidRow = table.getSelectedOuidRow(row);
                    String selectOuid = (String)((ArrayList)data.get((new Integer(ouidRow)).intValue())).get(0);
                    String selectClassOuid = this.dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(searchResultPanel, selectClassOuid, selectOuid, 1);
                    uiGeneration.setSession(_session);
                    uiGeneration.setVisible(true);
                }
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        } else
        if(command.startsWith("assoAssoDetail/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                ArrayList data = table.getList();
                int row = table.getTable().getSelectedRow();
                String ouidRow = null;
                if(row >= 0)
                {
                    ouidRow = table.getSelectedOuidRow(row);
                    ArrayList rowData = (ArrayList)data.get((new Integer(ouidRow)).intValue());
                    String end1Ouid = instanceOuid;
                    String end2Ouid = (String)rowData.get(0);
                    DOSChangeable dosAssociation = this.dos.getAssociation(associationOuid);
                    if(!Utils.isNullString((String)dosAssociation.get("ouid@class")))
                    {
                        DOSChangeable dosClass = this.dos.getClass((String)dosAssociation.get("ouid@class"));
                        ArrayList fieldGroupInfo = this.dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                        Utils.sort(fieldGroupInfo);
                        ArrayList endOidList = new ArrayList();
                        endOidList.add(end1Ouid);
                        endOidList.add(end2Ouid);
                        String assoOuid = (String)rowData.get(rowData.size() - 1);
                        AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), null, 1, endOidList, (String)dosAssociation.get("ouid"));
                        DOSChangeable getAssoInfo = this.dos.get(assoOuid);
                        AssoClsGeneration.setData(getAssoInfo);
                        AssoClsGeneration.setVisible(true);
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        } else
        if(command.startsWith("selectAllButton/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                table.getTable().selectAll();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } else
        if(command.startsWith("clearSelectionButton/"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '/').get(1);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                table.getTable().clearSelection();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } else
        if(command.startsWith("action^"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                String associationOuid = (String)Utils.tokenizeMessage(command, '^').get(2);
                Table table = (Table)associationTableMap.get(associationOuid);
                if(table == null)
                    return;
                ArrayList data = table.getList();
                int selectedRows[] = table.getTable().getSelectedRows();
                if(selectedRows == null && selectedRows.length < 1)
                    return;
                String objectOuid = null;
                String ouidRow = null;
                ArrayList resultList = new ArrayList();
                for(int n = 0; n < selectedRows.length; n++)
                {
                    ouidRow = table.getSelectedOuidRow(selectedRows[n]);
                    ArrayList rowData = (ArrayList)data.get((new Integer(ouidRow)).intValue());
                    if(ouidRow != null)
                        table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                    if(ouidRow != null)
                        objectOuid = (String)rowData.get(0);
                    else
                        objectOuid = (String)((ArrayList)data.get((new Integer(selectedRows[n])).intValue())).get(0);
                    resultList.add(objectOuid);
                }

                if(Utils.isNullArrayList(resultList))
                    resultList = null;
                HashMap map = new HashMap();
                map.put("list", resultList);
                map.put("ouid", instanceOuid);
                String actionOuid = (String)Utils.tokenizeMessage(command, '^').get(1);
                String filePath = this.dos.getActionScriptName(actionOuid);
                Utils.executeScriptFile(filePath, DynaMOAD.dss, map);
                resultList = null;
                map = null;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        } else
        if(command.equals("historyDetailButton"))
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                if(historyTable == null)
                    return;
                Table table = historyTable;
                ArrayList data = table.getList();
                if(table == null)
                    return;
                int row = table.getTable().getSelectedRow();
                String ouidRow = null;
                if(row >= 0)
                {
                    ouidRow = table.getSelectedOuidRow(row);
                    String selectOuid = (String)((ArrayList)data.get((new Integer(ouidRow)).intValue())).get(0);
                    String selectClassOuid = this.dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(searchResultPanel, selectClassOuid, selectOuid, 1);
                    uiGeneration.setSession(_session);
                    uiGeneration.setVisible(true);
                }
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        } else
        if(command.equals("MNU_EXPAND_ONE_LINK"))
        {
            if(linkTreeTable == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
            if(node != null)
                UIUtils.expandTree1Level(linkTreeTable.tree, node);
            else
                UIUtils.expandTree1Level(linkTreeTable.tree);
        } else
        if(command.equals("MNU_EXPAND_FULL_LINK"))
        {
            if(linkTreeTable == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
            if(node != null)
                UIUtils.expandTreeFullLevel(linkTreeTable.tree, node);
            else
                UIUtils.expandTreeFullLevel(linkTreeTable.tree);
        } else
        if(command.equals("MNU_EXPAND_LEVEL_LINK"))
        {
            if(linkTreeTable == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
            int level = 0;
            String inputValue = JOptionPane.showInputDialog(linkTableScrPane, DynaMOAD.getMSRString("QST_0005", "Input level number:", 3));
            try
            {
                level = Utils.getInt(new Integer(inputValue));
            }
            catch(Exception e)
            {
                level = 1;
            }
            if(node != null)
                UIUtils.expandTreeLevel(linkTreeTable.tree, node, level);
            else
                UIUtils.expandTreeLevel(linkTreeTable.tree, level);
        } else
        if(command.startsWith("file/open"))
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
                checkedOut.setVisible(false);
                checkedOut = null;
                tempList.clear();
                tempList = null;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        else
        if(command.startsWith("file/checkout"))
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
        else
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
                this.dos.cancelCheckoutFile((String)fileInfoMap.get("md$ouid"), fileInfoMap);
                if(searchConditionPanel != null && (searchConditionPanel instanceof SearchCondition4CADIntegration))
                {
                    if(_session != null && (((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).cadType.equals("4") || ((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).cadType.equals("5")))
                    {
                        _session.setProperty("dynapdm.cadintegration.drawing", null);
                        _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                    }
                } else
                if(searchResultPanel != null && (searchResultPanel instanceof SearchResult4CADIntegration) && _session != null && (((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).cadType.equals("4") || ((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).cadType.equals("5")))
                {
                    _session.setProperty("dynapdm.cadintegration.drawing", null);
                    _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        else
        if(command.startsWith("process/"))
        {
            java.util.List tokens = Utils.tokenizeMessage(command, '/');
            String procOuid = (String)tokens.get(1);
            int cnt = tokens.size();
            ArrayList ouidList = new ArrayList(cnt - 2);
            for(int i = 2; i < cnt; i++)
                ouidList.add((String)tokens.get(i));

            if(ouidList.isEmpty())
                return;
            CreateProcess createProcess = new CreateProcess(0, procOuid, ouidList);
            createProcess = null;
            ouidList = null;
        } else
        if(command.equals("MNU_ADD_NEW"))
        {
            if(linkTreeTable != null && linkTreeTable.tree.getSelectionPath() != null)
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)linkTreeTable.tree.getSelectionPath().getLastPathComponent();
                if(selectedNode == null)
                    return;
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectedNode.getParent();
                if(parentNode == null)
                    return;
                String tmpStr = ((TreeNodeObject)selectedNode.getUserObject()).getOuid();
                String treeTableSelectedOuid = (String)Utils.tokenizeMessage(tmpStr, '^').get(0);
                tmpStr = ((TreeNodeObject)parentNode.getUserObject()).getOuid();
                String parentOuid = (String)Utils.tokenizeMessage(tmpStr, '^').get(0);
                if(treeTableSelectedOuid != null)
                    try
                    {
                        String selectedClassOuid = this.dos.getClassOuid(treeTableSelectedOuid);
                        UIGeneration uiGeneration = new UIGeneration(null, selectedClassOuid, parentOuid, ((Component) (null)));
                        uiGeneration = null;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        } else
        if(command.startsWith("MNU_ADD_NEW_ASSO"))
        {
            java.util.List list = Utils.tokenizeMessage(command, '^');
            if(list == null || list.size() < 2)
                return;
            String assoOuid = (String)list.get(1);
            if(Utils.isNullString(assoOuid))
                return;
            Table assoTable = (Table)associationTableMap.get(assoOuid);
            if(assoTable == null)
                return;
            ArrayList data = assoTable.getList();
            int row = assoTable.getTable().getSelectedRow();
            String ouidRow = null;
            if(row >= 0)
            {
                ouidRow = assoTable.getSelectedOuidRow(row);
                String selectOuid = (String)((ArrayList)data.get((new Integer(ouidRow)).intValue())).get(0);
                try
                {
                    String selectedClassOuid = this.dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(null, selectedClassOuid, instanceOuid, ((Component) (null)));
                    uiGeneration = null;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        } else
        if(command.equals("searchResultButton"))
        {
            if(linkTreeTable == null)
                return;
            JTreeTable table = linkTreeTable;
            if(table == null || table.getRowCount() == 0)
                return;
            String fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
            JFileChooser fileChooser = null;
            SoftWareFilter softwareFilter = null;
            Object object = null;
            File selectedFile = null;
            File preselectedFile = new File(System.getProperty("user.home") + fileSeperator + DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + ".csv");
            fileChooser = new JFileChooser(System.getProperty("user.home"));
            DOSChangeable softwareInfo = new DOSChangeable();
            softwareInfo.put("EXTENSION", "csv");
            softwareInfo.put("NAME", "Comma Separated Value File (*.csv)");
            softwareFilter = new SoftWareFilter(softwareInfo);
            fileChooser.addChoosableFileFilter(softwareFilter);
            if(preselectedFile != null)
                fileChooser.setSelectedFile(preselectedFile);
            preselectedFile = null;
            if(fileChooser.showSaveDialog(linkTreeTable) == 0)
            {
                selectedFile = fileChooser.getSelectedFile();
                fileChooser = null;
                try
                {
                    if(!selectedFile.exists())
                        selectedFile.createNewFile();
                    if(!selectedFile.canWrite())
                        throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify the file.", 0));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));
                    int rows = table.getRowCount();
                    int columns = table.getColumnCount();
                    String str = null;
                    TreePath path = null;
                    DefaultMutableTreeNode node = null;
                    bw.write("\"Level\",");
                    bw.write(34);
                    for(int j = 0; j < columns; j++)
                    {
                        bw.write(table.getColumnName(j));
                        if(j < columns - 1)
                            bw.write("\",\"");
                    }

                    bw.write(34);
                    bw.newLine();
                    for(int i = 0; i < rows; i++)
                        if(table.tree != null)
                        {
                            path = table.tree.getPathForRow(i);
                            if(path != null)
                            {
                                node = (DefaultMutableTreeNode)path.getLastPathComponent();
                                if(node != null)
                                {
                                    bw.write(34);
                                    bw.write(Integer.toString(node.getLevel()));
                                    bw.write("\",");
                                    bw.write(34);
                                    for(int j = 0; j < columns; j++)
                                    {
                                        object = table.getValueAt(i, j);
                                        if(object != null)
                                            bw.write(object.toString());
                                        if(j < columns - 1)
                                            bw.write("\",\"");
                                    }

                                    bw.write(34);
                                    if(i < rows - 1)
                                        bw.newLine();
                                }
                            }
                        }

                    bw.flush();
                    bw.close();
                    bw = null;
                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }
            }
        } else
        if(command.startsWith("file/multi_checkout"))
        {
            ArrayList ouidList = null;
            String selectedOuid = null;
            if(command.startsWith("file/multi_checkout^"))
            {
                Table assoTable = (Table)associationTableMap.get(command.substring("file/multi_checkout^".length()));
                if(assoTable == null)
                    return;
                int selectedRows[] = assoTable.getTable().getSelectedRows();
                int row = assoTable.getTable().getSelectedRow();
                String ouidRow = assoTable.getSelectedOuidRow(row);
                if(assoTable.getSelectedRowNumber() > -1)
                {
                    if(selectedRows == null && selectedRows.length < 1)
                        return;
                    ouidList = new ArrayList();
                    for(int n = 0; n < selectedRows.length; n++)
                    {
                        ouidRow = assoTable.getSelectedOuidRow(selectedRows[n]);
                        if(ouidRow != null)
                            assoTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        if(ouidRow != null)
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(ouidRow)).intValue())).get(0);
                        else
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(selectedRows[n])).intValue())).get(0);
                        ouidList.add(selectedOuid);
                    }

                }
            } else
            {
                DefaultMutableTreeNode node = null;
                TreeNodeObject nodeObject = null;
                TreePath paths[] = linkTreeTable.tree.getSelectionPaths();
                if(paths == null)
                    return;
                ouidList = new ArrayList(paths.length);
                for(int i = 0; i < paths.length; i++)
                {
                    node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    if(node != null)
                    {
                        nodeObject = (TreeNodeObject)node.getUserObject();
                        if(nodeObject != null)
                        {
                            selectedOuid = nodeObject.getOuid();
                            selectedOuid = (String)Utils.tokenizeMessage(selectedOuid, '^').get(0);
                            ouidList.add(selectedOuid);
                        }
                    }
                }

            }
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
                try
                {
                    selectOuid = (String)ouidList.get(i);
                    fileList = this.dos.listFile(selectOuid);
                    status = this.dos.getStatus(selectOuid);
                    isUpdatable = LogIn.isAdmin || acl.hasPermission(this.dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
                if(!Utils.isNullArrayList(fileList) && isUpdatable)
                {
                    for(int j = 0; j < fileList.size(); j++)
                    {
                        file = (HashMap)fileList.get(j);
                        if("CRT".equals(status) || "WIP".equals(status) || "REJ".equals(status))
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
            checkedOut.setVisible(true);
        } else
        if(command.startsWith("file/multi_checkin"))
        {
            ArrayList ouidList = null;
            String selectedOuid = null;
            if(command.startsWith("file/multi_checkin^"))
            {
                Table assoTable = (Table)associationTableMap.get(command.substring("file/multi_checkin^".length()));
                if(assoTable == null)
                    return;
                int selectedRows[] = assoTable.getTable().getSelectedRows();
                int row = assoTable.getTable().getSelectedRow();
                String ouidRow = assoTable.getSelectedOuidRow(row);
                if(assoTable.getSelectedRowNumber() > -1)
                {
                    if(selectedRows == null && selectedRows.length < 1)
                        return;
                    ouidList = new ArrayList();
                    for(int n = 0; n < selectedRows.length; n++)
                    {
                        ouidRow = assoTable.getSelectedOuidRow(selectedRows[n]);
                        if(ouidRow != null)
                            assoTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        if(ouidRow != null)
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(ouidRow)).intValue())).get(0);
                        else
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(selectedRows[n])).intValue())).get(0);
                        ouidList.add(selectedOuid);
                    }

                }
            } else
            {
                DefaultMutableTreeNode node = null;
                TreeNodeObject nodeObject = null;
                TreePath paths[] = linkTreeTable.tree.getSelectionPaths();
                if(paths == null)
                    return;
                ouidList = new ArrayList(paths.length);
                for(int i = 0; i < paths.length; i++)
                {
                    node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    if(node != null)
                    {
                        nodeObject = (TreeNodeObject)node.getUserObject();
                        if(nodeObject != null)
                        {
                            selectedOuid = nodeObject.getOuid();
                            selectedOuid = (String)Utils.tokenizeMessage(selectedOuid, '^').get(0);
                            ouidList.add(selectedOuid);
                        }
                    }
                }

            }
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
                try
                {
                    selectOuid = (String)ouidList.get(i);
                    fileList = this.dos.listFile(selectOuid);
                    status = this.dos.getStatus(selectOuid);
                    isUpdatable = LogIn.isAdmin || acl.hasPermission(this.dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
                if(!Utils.isNullArrayList(fileList) && isUpdatable)
                {
                    for(int j = 0; j < fileList.size(); j++)
                    {
                        file = (HashMap)fileList.get(j);
                        if("CRT".equals(status) || "WIP".equals(status) || "REJ".equals(status))
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
            checkIn.setVisible(true);
        } else
        if(command.startsWith("file/multi_cancel_checkout"))
        {
            ArrayList ouidList = null;
            String selectedOuid = null;
            if(command.startsWith("file/multi_cancel_checkout^"))
            {
                Table assoTable = (Table)associationTableMap.get(command.substring("file/multi_cancel_checkout^".length()));
                if(assoTable == null)
                    return;
                int selectedRows[] = assoTable.getTable().getSelectedRows();
                int row = assoTable.getTable().getSelectedRow();
                String ouidRow = assoTable.getSelectedOuidRow(row);
                if(assoTable.getSelectedRowNumber() > -1)
                {
                    if(selectedRows == null && selectedRows.length < 1)
                        return;
                    ouidList = new ArrayList();
                    for(int n = 0; n < selectedRows.length; n++)
                    {
                        ouidRow = assoTable.getSelectedOuidRow(selectedRows[n]);
                        if(ouidRow != null)
                            assoTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        if(ouidRow != null)
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(ouidRow)).intValue())).get(0);
                        else
                            selectedOuid = (String)((ArrayList)assoTable.getList().get((new Integer(selectedRows[n])).intValue())).get(0);
                        ouidList.add(selectedOuid);
                    }

                }
            } else
            {
                DefaultMutableTreeNode node = null;
                TreeNodeObject nodeObject = null;
                TreePath paths[] = linkTreeTable.tree.getSelectionPaths();
                if(paths == null)
                    return;
                ouidList = new ArrayList(paths.length);
                for(int i = 0; i < paths.length; i++)
                {
                    node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    if(node != null)
                    {
                        nodeObject = (TreeNodeObject)node.getUserObject();
                        if(nodeObject != null)
                        {
                            selectedOuid = nodeObject.getOuid();
                            selectedOuid = (String)Utils.tokenizeMessage(selectedOuid, '^').get(0);
                            ouidList.add(selectedOuid);
                        }
                    }
                }

            }
            ArrayList fileList = null;
            HashMap file = null;
            String selectOuid = null;
            String checkedOutDate = null;
            String checkedInDate = null;
            String status = null;
            boolean isUpdatable = false;
            for(int i = 0; i < ouidList.size(); i++)
                try
                {
                    selectOuid = (String)ouidList.get(i);
                    fileList = this.dos.listFile(selectOuid);
                    status = this.dos.getStatus(selectOuid);
                    isUpdatable = LogIn.isAdmin || acl.hasPermission(this.dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                    if(!Utils.isNullArrayList(fileList) && isUpdatable)
                    {
                        for(int j = 0; j < fileList.size(); j++)
                        {
                            file = (HashMap)fileList.get(j);
                            if("CRT".equals(status) || "WIP".equals(status) || "REJ".equals(status))
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
                                    this.dos.cancelCheckoutFile((String)file.get("md$ouid"), file);
                            }
                        }

                    }
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }

            if(searchConditionPanel != null && (searchConditionPanel instanceof SearchCondition4CADIntegration))
            {
                if(_session != null && (((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).cadType.equals("4") || ((Search4CADIntegration)((SearchCondition4CADIntegration)searchConditionPanel).contentFrame).cadType.equals("5")))
                {
                    _session.setProperty("dynapdm.cadintegration.drawing", null);
                    _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                }
            } else
            if(searchResultPanel != null && (searchResultPanel instanceof SearchResult4CADIntegration) && _session != null && (((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).cadType.equals("4") || ((Search4CADIntegration)((SearchResult4CADIntegration)searchResultPanel).parentFrame).cadType.equals("5")))
            {
                _session.setProperty("dynapdm.cadintegration.drawing", null);
                _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
            }
        } else
        if(command.startsWith("fieldComboBox^"))
        {
            java.util.List list = Utils.tokenizeMessage(command, '^');
            if(list == null || list.size() < 2)
                return;
            refreshInitialValufOfCodeField((String)list.get(1));
        }
    }

    public void refreshInitialValufOfCodeField()
    {
        if(Utils.isNullArrayList(groupList))
            return;
        if(hoardData == null)
            return;
        if(hoardData.get("in.refresh") != null)
            return;
        hoardData.put("in.refresh", "");
        DOSChangeable fieldGroupDC = null;
        try
        {
            for(int i = 0; i < groupList.size(); i++)
            {
                fieldGroupDC = (DOSChangeable)groupList.get(0);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    UIBuilder.refreshFieldGroupPanel(DynaMOAD.dos, hoardData, fieldGroupDC, fieldAndFieldGroupMap, getDOSGlobal);
                    fieldGroupDC = null;
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        hoardData.put("in.refresh", null);
    }

    public void refreshInitialValufOfCodeField(String fieldOuid)
    {
        if(Utils.isNullArrayList(groupList))
            return;
        if(hoardData == null)
            return;
        if(hoardData.get("in.refresh") != null)
            return;
        hoardData.put("in.refresh", "");
        DOSChangeable fieldGroupDC = null;
        try
        {
            for(int i = 0; i < groupList.size(); i++)
            {
                fieldGroupDC = (DOSChangeable)groupList.get(0);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    UIBuilder.refreshFieldGroupPanel(fieldOuid, DynaMOAD.dos, hoardData, fieldGroupDC, fieldAndFieldGroupMap, getDOSGlobal);
                    fieldGroupDC = null;
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        hoardData.put("in.refresh", null);
    }

    public void stateChanged(ChangeEvent ce)
    {
        Object source = ce.getSource();
        if(source.equals(mwlm.getCenterTabbedPane()))
        {
            JTabbedPane fieldGroupPane = mwlm.getCenterTabbedPane();
            int selIndex = fieldGroupPane.getSelectedIndex();
            if(selIndex == tabIndexForObjectTree)
            {
                if(linkPanel == null)
                {
                    makeLinkPanel();
                    fieldGroupPane.setComponentAt(selIndex, linkViewPane);
                }
            } else
            if(selIndex == tabIndexForFile)
            {
                if(filePanel == null)
                {
                    makeFilePanel();
                    fieldGroupPane.setComponentAt(selIndex, filePanel);
                }
            } else
            if(selIndex == tabIndexForVersion)
            {
                if(historyPanel == null)
                {
                    makeHistoryPanel();
                    fieldGroupPane.setComponentAt(selIndex, historyPanel);
                }
            } else
            if(selIndex == tabIndexForProcess && processPanel == null)
            {
                makeProcessPanel();
                fieldGroupPane.setComponentAt(selIndex, processPanel);
            }
        } else
        if(source.equals(linkViewPane))
        {
            int i = linkViewPane.getSelectedIndex();
            if(i < 0)
                return;
            searchConditionMap = (HashMap)treeViewConditionMap.get(linkViewPane.getTitleAt(i));
        }
    }

    public void link(String lcClassOuid, String end1, String end2, String assoOuid)
    {
        if(Utils.isNullString(end1) || Utils.isNullString(end2))
        {
            System.out.println("end1 or end2 is null");
            return;
        }
        try
        {
            String clsOuidEnd1 = dos.getClassOuid((String)Utils.tokenizeMessage(end1, '^').get(0));
            String clsOuidEnd2 = dos.getClassOuid(end2);
            String associationOuid = null;
            if(instanceOuid != null)
            {
                ArrayList superClassList1 = dos.listAllSuperClassOuid(clsOuidEnd1);
                ArrayList assoList = new ArrayList();
                if(superClassList1 == null)
                    superClassList1 = new ArrayList();
                superClassList1.add(clsOuidEnd1);
                for(int i = 0; i < superClassList1.size(); i++)
                {
                    ArrayList tmpAssoList = dos.listAssociationOfClass((String)superClassList1.get(i));
                    if(tmpAssoList == null)
                    {
                        tmpAssoList = null;
                    } else
                    {
                        for(int j = 0; j < tmpAssoList.size(); j++)
                            if(!assoList.contains(tmpAssoList.get(j)))
                                assoList.add(tmpAssoList.get(j));

                        tmpAssoList = null;
                    }
                }

                ArrayList superClassList2 = dos.listAllSuperClassOuid(clsOuidEnd2);
                if(superClassList2 == null)
                    superClassList2 = new ArrayList();
                superClassList2.add(clsOuidEnd2);
                for(int i = 0; i < assoList.size(); i++)
                {
                    DOSChangeable dosAssociation = (DOSChangeable)assoList.get(i);
                    associationOuid = (String)dosAssociation.get("ouid");
                    if((!superClassList1.contains((String)dosAssociation.get("end1.ouid@class")) || !superClassList2.contains((String)dosAssociation.get("end2.ouid@class"))) && (!superClassList1.contains((String)dosAssociation.get("end2.ouid@class")) || !superClassList2.contains((String)dosAssociation.get("end1.ouid@class"))))
                        continue;
                    if(superClassList1.contains((String)dosAssociation.get("end2.ouid@class")) && superClassList2.contains((String)dosAssociation.get("end1.ouid@class")) && !dosAssociation.get("end2.ouid@class").equals(dosAssociation.get("end1.ouid@class")))
                    {
                        String tempString = (String)Utils.tokenizeMessage(end1, '^').get(0);
                        end1 = end2;
                        end2 = tempString;
                    }
                    if(Utils.isNullString((String)dosAssociation.get("ouid@class")))
                    {
                        HashMap tempMap = new HashMap();
                        tempMap.put("end1", Utils.tokenizeMessage(end1, '^').get(0));
                        tempMap.put("end2", end2);
                        System.out.println("[link before] 5");
                        Object returnObject = Utils.executeScriptFile(dos.getEventName(lcClassOuid, "link.before"), dss, tempMap);
                        if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                        {
                            dos.link((String)Utils.tokenizeMessage(end1, '^').get(0), end2);
                            Utils.executeScriptFile(dos.getEventName(lcClassOuid, "link.after"), dss, tempMap);
                        }
                        tempMap.clear();
                        tempMap = null;
                    } else
                    if(assoOuid == null)
                    {
                        DOSChangeable dosClass = dos.getClass((String)dosAssociation.get("ouid@class"));
                        ArrayList fieldGroupInfo = dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                        ArrayList endOidList = new ArrayList();
                        endOidList.add((String)Utils.tokenizeMessage(end1, '^').get(0));
                        endOidList.add(end2);
                        AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), null, 0, endOidList, (String)dosAssociation.get("ouid"));
                        AssoClsGeneration.setVisible(true);
                    } else
                    {
                        DOSChangeable dosClass = dos.getClass((String)dosAssociation.get("ouid@class"));
                        ArrayList fieldGroupInfo = dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                        ArrayList endOidList = new ArrayList();
                        endOidList.add((String)Utils.tokenizeMessage(end1, '^').get(0));
                        endOidList.add(end2);
                        AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), null, 0, endOidList, (String)dosAssociation.get("ouid"));
                        DOSChangeable getAssoInfo = dos.get(assoOuid);
                        AssoClsGeneration.setData(getAssoInfo);
                        AssoClsGeneration.setVisible(false);
                        AssoClsGeneration.saveButtonClick();
                    }
                    break;
                }

                superClassList2.clear();
                superClassList2 = null;
            }
            refreshLinkTable();
            refreshAssociationTable(associationOuid);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        mwlm.clear();
        mwlm = null;
        if(linkTreeTable != null && linkListener != null)
            linkTreeTable.removeTreeSelectionListener(linkListener);
        if(linkTreeTable != null && expansionListener != null)
            linkTreeTable.tree.removeTreeExpansionListener(expansionListener);
        if(fileTreeTable != null && fileListener != null)
            fileTreeTable.removeTreeSelectionListener(fileListener);
        removeWindowListener(this);
        openButton.removeActionListener(this);
        saveButton.removeActionListener(this);
        if(linkPanel != null)
        {
            linkOpenButton.removeActionListener(this);
            linkChartButton.removeActionListener(this);
            chartSettingButton.removeActionListener(this);
        }
        if(filePanel != null)
        {
            fileOpenButton.removeActionListener(this);
            fileReadOnlyButton.removeActionListener(this);
            menuFileDetach.removeActionListener(this);
            menuFileCheckIn.removeActionListener(this);
            menuFileCheckOut.removeActionListener(this);
            menuFileCheckOutCancel.removeActionListener(this);
            menuFileReadOnly.removeActionListener(this);
        }
        if(historyPanel != null)
            historyDetailButton.removeActionListener(this);
        if(_session != null)
            _session.setProperty("dynapdm.part.insert.status", "closed");
        DynaMOAD.startFromCAD = false;
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

    public void upload(String serverPath, String clientPath, FileTransferCallback callback)
    {
        _serverPath = serverPath;
        _clientPath = clientPath;
        _ftc = callback;
        SwingWorker worker = new SwingWorker() {

            public Object construct()
            {
                boolean result = false;
                try
                {
                    result = dss.uploadFile(serverPath, clientPath, ftc);
                }
                catch(IIPRequestException e)
                {
                    e.printStackTrace();
                }
                return new Boolean(result);
            }

            public void finished()
            {
                ftd.setVisible(false);
                ftd.dispose();
                fileTreeRefresh();
            }

            String serverPath;
            String clientPath;
            FileTransferCallback ftc;

            
            {
                serverPath = _serverPath;
                clientPath = _clientPath;
                ftc = _ftc;
            }
        };
        worker.start();
    }

    public void setFilterInLink(HashMap searchCondition)
    {
        searchConditionMap = searchCondition;
    }

    public DOSChangeable getDOS()
    {
        try
        {
            DOSChangeable getDC = null;
            if(!Utils.isNullString(instanceOuid))
                getDC = dos.get(instanceOuid);
            else
                getDC = new DOSChangeable();
            return getDC;
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        return null;
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        MultiSelectDialog msd = null;
        DynaTable table = null;
        Object object = null;
        Table xTable = null;
        if(mouseEvent.getClickCount() != 2 || !SwingUtilities.isLeftMouseButton(mouseEvent))
            return;
        if(mouseEvent.getSource() instanceof JTextField)
        {
            object = mouseEvent.getComponent();
            String keyString = (String)Utils.getMapKeyByValue(hoardData.getValueMap(), object);
            if(Utils.isNullString(keyString))
                return;
            String selectOuid = (String)getDOSGlobal.get((String)hoardData.get("name@@" + keyString));
            if(Utils.isNullString(selectOuid))
                return;
            try
            {
                String selectClassOuid = DynaMOAD.dos.getClassOuid(selectOuid);
                UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, selectOuid, 1);
                uiGeneration.setSession(_session);
                uiGeneration.setVisible(true);
                uiGeneration = null;
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        } else
        if(collectionMap != null)
        {
            object = mouseEvent.getComponent();
            if(object instanceof DynaTable)
                table = (DynaTable)object;
            else
            if(object instanceof JScrollPane)
                table = (DynaTable)((JScrollPane)object).getViewport().getView();
            xTable = (Table)collectionMap.get(table.hashCode() + "Table");
            DOSChangeable fieldData = (DOSChangeable)collectionMap.get(table);
            byte dataType = Utils.getByte((Byte)fieldData.get("type"));
            ArrayList tableData = xTable.getList();
            ArrayList tables = (ArrayList)collectionMap.get((String)fieldData.get("name") + "TableData");
            msd = new MultiSelectDialog(this, true, fieldData, xTable.getList());
            msd.setVisible(true);
            msd.dispose();
            msd = null;
            for(int i = 0; i < tables.size(); i++)
                ((Table)tables.get(i)).changeTableData();

            ArrayList returnedList = xTable.getList();
            ArrayList inputList = new ArrayList();
            ArrayList tmpList = null;
            if(dataType != 16 && dataType != 24 && dataType != 25)
            {
                if(returnedList != null)
                {
                    for(int x = 0; x < returnedList.size(); x++)
                    {
                        tmpList = new ArrayList();
                        tmpList.add(returnedList.get(x));
                        inputList.add(tmpList);
                        tmpList = null;
                    }

                }
                getDOSGlobal.put((String)fieldData.get("name"), inputList);
            } else
            {
                getDOSGlobal.put((String)fieldData.get("name"), returnedList.clone());
            }
            xTable = null;
            fieldData = null;
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public String fieldTypeNumToName(byte typenum)
    {
        String resultStr;
        switch(typenum)
        {
        case 2: // '\002'
            resultStr = "byte";
            break;

        case 3: // '\003'
            resultStr = "char";
            break;

        case 4: // '\004'
            resultStr = "double";
            break;

        case 5: // '\005'
            resultStr = "float";
            break;

        case 6: // '\006'
            resultStr = "int";
            break;

        case 7: // '\007'
            resultStr = "long";
            break;

        case 8: // '\b'
            resultStr = "short";
            break;

        case 13: // '\r'
            resultStr = "String";
            break;

        case 16: // '\020'
            resultStr = "object";
            break;

        case 21: // '\025'
            resultStr = "date time";
            break;

        case 22: // '\026'
            resultStr = "date";
            break;

        case 23: // '\027'
            resultStr = "time";
            break;

        case 24: // '\030'
            resultStr = "Code";
            break;

        case 25: // '\031'
            resultStr = "Code (Field Referenced)";
            break;

        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 14: // '\016'
        case 15: // '\017'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        case 20: // '\024'
        default:
            resultStr = "";
            break;
        }
        return resultStr;
    }

    public void initializeChartData()
    {
        try
        {
            chartFieldOuid = new ArrayList();
            String classPUID = classOuid;
            ArrayList tmpList1 = new ArrayList();
            ArrayList fieldList = nds.getChildNodeList("::/WORKSPACE/DATEBASECHARTFIELD/" + classPUID);
            if(fieldList != null)
            {
                for(int i = 0; i < fieldList.size(); i++)
                {
                    String val = nds.getValue("::/WORKSPACE/DATEBASECHARTFIELD/" + classPUID + "/" + (String)fieldList.get(i));
                    String bFieldOuid = (String)Utils.tokenizeMessage(val, '/').get(0);
                    DOSChangeable bFieldObject = dos.getField(bFieldOuid);
                    if(bFieldObject != null)
                        tmpList1.add(bFieldObject.get("ouid"));
                    String eFieldOuid = (String)Utils.tokenizeMessage(val, '/').get(1);
                    DOSChangeable eFieldObject = dos.getField(eFieldOuid);
                    if(eFieldObject != null)
                        tmpList1.add(eFieldObject.get("ouid"));
                    tmpList1.add(new Color(Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(2)), Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(3)), Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(4))));
                    chartFieldOuid.add(tmpList1.clone());
                    tmpList1.clear();
                }

                tmpList1 = null;
            } else
            {
                String fieldOuid = null;
                String fieldName = null;
                DOSChangeable fieldObject = null;
                String startPlanDate = null;
                String finishPlanDate = null;
                String startedDate = null;
                String finishedDate = null;
                for(int i = 0; i < columnOuidList.size(); i++)
                {
                    fieldOuid = (String)columnOuidList.get(i);
                    fieldObject = dos.getField(fieldOuid);
                    if(fieldObject != null)
                    {
                        fieldName = (String)fieldObject.get("name");
                        if(fieldName.equals("startPlanDate"))
                            startPlanDate = fieldOuid;
                        else
                        if(fieldName.equals("finishPlanDate"))
                            finishPlanDate = fieldOuid;
                        else
                        if(fieldName.equals("startedDate"))
                            startedDate = fieldOuid;
                        else
                        if(fieldName.equals("finishedDate"))
                            finishedDate = fieldOuid;
                    }
                }

                if(startPlanDate != null && finishPlanDate != null && startedDate != null && finishedDate != null)
                {
                    chartFieldOuid.add("PMS");
                    tmpList1 = new ArrayList();
                    tmpList1.add(startPlanDate);
                    tmpList1.add(finishPlanDate);
                    tmpList1.add(null);
                    chartFieldOuid.add(tmpList1.clone());
                    tmpList1.clear();
                    tmpList1 = null;
                    tmpList1 = new ArrayList();
                    tmpList1.add(startedDate);
                    tmpList1.add(finishedDate);
                    tmpList1.add(null);
                    chartFieldOuid.add(tmpList1.clone());
                    tmpList1.clear();
                    tmpList1 = null;
                }
            }
            if(chartFieldOuid == null || chartFieldOuid.size() == 0)
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0011", "Gantt chart not yet configured. First, configure gantt chart.", 0), DynaMOAD.getMSRString("WRD_0004", "Information", 0), 1);
                return;
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setChartData()
    {
        boolean isPMS = false;
        String datePlanned = null;
        String dateStarted = null;
        String dateClosed = null;
        ArrayList tmpList = new ArrayList();
        int barCnt = 0;
        ArrayList tmpList1 = new ArrayList();
        ArrayList tmpList2 = new ArrayList();
        chartFieldData = new ArrayList();
        String minDate = null;
        String tempMinDate = null;
        Calendar tmpCalendar = Calendar.getInstance();
        int intYear = tmpCalendar.get(1);
        int intMonth = tmpCalendar.get(2) + 1;
        DefaultMutableTreeNode selectedNode = null;
        TreeNodeObject selectedObject = null;
        String selectOuid = null;
        for(int i = 0; i < linkTreeTable.getRowCount(); i++)
        {
            barCnt = 0;
            selectedNode = (DefaultMutableTreeNode)linkTreeTable.tree.getPathForRow(i).getLastPathComponent();
            selectedObject = (TreeNodeObject)selectedNode.getUserObject();
            selectOuid = (String)Utils.tokenizeMessage(selectedObject.getOuid(), '^').get(0);
            if(chartFieldOuid != null)
            {
                for(int j = 0; j < chartFieldOuid.size(); j++)
                    if(j == 0 && (chartFieldOuid.get(j) instanceof String) && ((String)chartFieldOuid.get(j)).equals("PMS"))
                    {
                        isPMS = true;
                    } else
                    {
                        tmpList = (ArrayList)chartFieldOuid.get(j);
                        int index1;
                        int index2;
                        if(childColumnOuidList == null)
                        {
                            index1 = columnOuidList.indexOf(tmpList.get(0));
                            index2 = columnOuidList.indexOf(tmpList.get(1));
                        } else
                        {
                            index1 = childColumnOuidList.indexOf(tmpList.get(0));
                            index2 = childColumnOuidList.indexOf(tmpList.get(1));
                        }
                        try
                        {
                            if(index1 > 0 && index2 > 0)
                            {
                                tmpList1.add((String)linkTreeTable.getValueAt(i, index1));
                                tmpList1.add((String)linkTreeTable.getValueAt(i, index2));
                                if(isPMS)
                                {
                                    if(j == 1)
                                    {
                                        datePlanned = (String)linkTreeTable.getValueAt(i, index2);
                                        tmpList1.add(new Integer(1));
                                    } else
                                    if(j == 2)
                                    {
                                        dateStarted = (String)linkTreeTable.getValueAt(i, index1);
                                        dateClosed = (String)linkTreeTable.getValueAt(i, index2);
                                        if(Utils.isNullString(dateStarted) && !Utils.isNullString(dateClosed))
                                            tmpList1.add(new Integer(5));
                                        else
                                        if(Utils.isNullString(dateStarted))
                                            tmpList1.add(new Integer(2));
                                        else
                                        if(!Utils.isNullString(datePlanned) && Utils.isNullString(dateClosed) && datePlanned.compareTo(sdfYMD.format(new Date())) < 0)
                                            tmpList1.add(new Integer(4));
                                        else
                                        if(Utils.isNullString(dateClosed))
                                            tmpList1.add(new Integer(3));
                                        else
                                        if(!Utils.isNullString(datePlanned) && !Utils.isNullString(dateClosed))
                                        {
                                            if(datePlanned.compareTo(dateClosed) < 0)
                                                tmpList1.add(new Integer(7));
                                            else
                                            if(datePlanned.compareTo(dateClosed) > 0)
                                                tmpList1.add(new Integer(6));
                                            else
                                                tmpList1.add(new Integer(5));
                                        } else
                                        if(Utils.isNullString(datePlanned) && !Utils.isNullString(dateClosed))
                                            tmpList1.add(new Integer(5));
                                        else
                                            tmpList1.add(new Integer(1));
                                    }
                                } else
                                {
                                    tmpList1.add(new Integer(0));
                                }
                                tmpList1.add((Color)tmpList.get(2));
                                tempMinDate = (String)linkTreeTable.getValueAt(i, index1);
                                tmpList1.add(selectOuid);
                                tmpList1.add(selectedNode);
                                tmpList1.add(instanceOuid);
                                if(!Utils.isNullString(tempMinDate))
                                {
                                    java.util.List dateList = Utils.tokenizeMessageWithNoTrim(tempMinDate, '-');
                                    if(dateList != null && dateList.size() > 0)
                                    {
                                        tempMinDate = (String)dateList.get(0) + (String)dateList.get(1) + (String)dateList.get(2);
                                        try
                                        {
                                            if(!Utils.isNullString(minDate))
                                            {
                                                if(Utils.getInt(Utils.getInteger(tempMinDate)) < Utils.getInt(Utils.getInteger(minDate)))
                                                {
                                                    minDate = tempMinDate;
                                                    intYear = Utils.getInt(Utils.getInteger((String)dateList.get(0)));
                                                    intMonth = Utils.getInt(Utils.getInteger((String)dateList.get(1)));
                                                }
                                            } else
                                            {
                                                minDate = tempMinDate;
                                                intYear = Utils.getInt(Utils.getInteger((String)dateList.get(0)));
                                                intMonth = Utils.getInt(Utils.getInteger((String)dateList.get(1)));
                                            }
                                        }
                                        catch(Exception exception) { }
                                    }
                                }
                                tmpList2.add(tmpList1.clone());
                            } else
                            {
                                tmpList1.add(null);
                                tmpList1.add(null);
                                tmpList1.add(new Integer(0));
                                tmpList1.add((Color)tmpList.get(2));
                                tmpList1.add(null);
                                tmpList2.add(tmpList1.clone());
                            }
                        }
                        catch(Exception ex)
                        {
                            tmpList1.add(null);
                            tmpList1.add(null);
                            tmpList1.add(new Integer(0));
                            tmpList1.add((Color)tmpList.get(2));
                            tmpList1.add(null);
                            tmpList1.add(null);
                            tmpList2.add(tmpList1.clone());
                        }
                        tmpList1.clear();
                        barCnt++;
                    }

                chartFieldData.add(tmpList2.clone());
                tmpList2.clear();
            }
        }

        chartPanel.setData(chartFieldData, barCnt, linkTreeTable.getRowCount(), intYear, intMonth);
        chartPanel.updateUI();
    }

    public void initializeNetworkEditor(int num)
    {
        try
        {
            networkFieldOuid = new ArrayList();
            String classPUID = classOuid;
            if(num > 0)
            {
                DOSChangeable dosAssociation = null;
                for(Iterator assoKey = assoList.iterator(); assoKey.hasNext();)
                {
                    dosAssociation = (DOSChangeable)assoKey.next();
                    classPUID = (String)dosAssociation.get("end2.ouid@class");
                }

            }
            ArrayList tmpList1 = new ArrayList();
            ArrayList fieldList = null;
            DOSChangeable dosField = dos.getFieldWithName(classPUID, "x");
            if(dosField != null)
            {
                if(fieldList == null)
                    fieldList = new ArrayList();
                fieldList.add(dosField.get("ouid"));
            }
            dosField = dos.getFieldWithName(classPUID, "y");
            if(dosField != null)
            {
                if(fieldList == null)
                    fieldList = new ArrayList();
                fieldList.add(dosField.get("ouid"));
            }
            dosField = dos.getFieldWithName(classPUID, "finishPlanDate");
            if(dosField != null)
            {
                if(fieldList == null)
                    fieldList = new ArrayList();
                fieldList.add(dosField.get("ouid"));
            }
            dosField = dos.getFieldWithName(classPUID, "finishedDate");
            if(dosField != null)
            {
                if(fieldList == null)
                    fieldList = new ArrayList();
                fieldList.add(dosField.get("ouid"));
            }
            dosField = dos.getFieldWithName(classPUID, "startedDate");
            if(dosField != null)
            {
                if(fieldList == null)
                    fieldList = new ArrayList();
                fieldList.add(dosField.get("ouid"));
            }
            if(fieldList != null && fieldList.size() > 1)
            {
                networkFieldOuid.add(fieldList.clone());
            } else
            {
                JOptionPane.showMessageDialog(this, "Required field not exists.", DynaMOAD.getMSRString("WRD_0004", "Information", 0), 1);
                return;
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setNetworkData()
    {
        ArrayList tmpList = new ArrayList();
        int barCnt = 1;
        ArrayList tmpList1 = new ArrayList();
        networkFieldData = new ArrayList();
        DefaultMutableTreeNode selectedNode = null;
        TreeNodeObject selectedObject = null;
        String selectOuid = null;
        Integer tempInteger = null;
        for(int i = 1; i < linkTreeTable.getRowCount(); i++)
        {
            initializeNetworkEditor(i);
            Iterator chartKey = networkFieldOuid.iterator();
            selectedNode = (DefaultMutableTreeNode)linkTreeTable.tree.getPathForRow(i).getLastPathComponent();
            if(selectedNode.isLeaf())
            {
                selectedObject = (TreeNodeObject)selectedNode.getUserObject();
                selectOuid = (String)Utils.tokenizeMessage(selectedObject.getOuid(), '^').get(0);
                for(; chartKey.hasNext(); tmpList1.clear())
                {
                    tmpList = (ArrayList)chartKey.next();
                    int index1;
                    int index2;
                    int index3;
                    int index4;
                    int index5;
                    if(childColumnOuidList == null)
                    {
                        index1 = columnOuidList.indexOf(tmpList.get(0));
                        index2 = columnOuidList.indexOf(tmpList.get(1));
                        index3 = columnOuidList.indexOf(tmpList.get(2));
                        index4 = columnOuidList.indexOf(tmpList.get(3));
                        index5 = columnOuidList.indexOf(tmpList.get(4));
                    } else
                    {
                        index1 = childColumnOuidList.indexOf(tmpList.get(0));
                        index2 = childColumnOuidList.indexOf(tmpList.get(1));
                        index3 = childColumnOuidList.indexOf(tmpList.get(2));
                        index4 = childColumnOuidList.indexOf(tmpList.get(3));
                        index5 = childColumnOuidList.indexOf(tmpList.get(4));
                    }
                    try
                    {
                        if(index1 > 0 && index2 > 0)
                        {
                            tempInteger = (Integer)linkTreeTable.getValueAt(i, index1);
                            if(tempInteger == null)
                                tempInteger = new Integer(5);
                            tmpList1.add(tempInteger);
                            tempInteger = (Integer)linkTreeTable.getValueAt(i, index2);
                            if(tempInteger == null)
                            {
                                tempInteger = new Integer(barCnt * 60 + 5);
                                barCnt++;
                            }
                            tmpList1.add(tempInteger);
                            tmpList1.add(selectOuid);
                            tmpList1.add(selectedObject);
                            tmpList1.add(instanceOuid);
                            tmpList1.add(linkTreeTable.getValueAt(i, index3));
                            tmpList1.add(linkTreeTable.getValueAt(i, index4));
                            tmpList1.add(linkTreeTable.getValueAt(i, index5));
                            networkFieldData.add(tmpList1.clone());
                        } else
                        {
                            tmpList1.add(new Integer(5));
                            tmpList1.add(new Integer(barCnt * 60 + 5));
                            tmpList1.add(selectOuid);
                            tmpList1.add(selectedObject);
                            tmpList1.add(instanceOuid);
                            tmpList1.add(linkTreeTable.getValueAt(i, index3));
                            tmpList1.add(linkTreeTable.getValueAt(i, index4));
                            tmpList1.add(linkTreeTable.getValueAt(i, index5));
                            networkFieldData.add(tmpList1.clone());
                            barCnt++;
                        }
                    }
                    catch(Exception ex)
                    {
                        tmpList1.add(new Integer(5));
                        tmpList1.add(new Integer(barCnt * 60 + 5));
                        tmpList1.add(selectOuid);
                        tmpList1.add(selectedObject);
                        tmpList1.add(instanceOuid);
                        tmpList1.add(linkTreeTable.getValueAt(i, index3));
                        tmpList1.add(linkTreeTable.getValueAt(i, index4));
                        tmpList1.add(linkTreeTable.getValueAt(i, index5));
                        networkFieldData.add(tmpList1.clone());
                        barCnt++;
                    }
                }

            }
        }

        networkPanel.setData(networkFieldData);
        networkPanel.run();
    }

    public void resizeTreeRowHeight()
    {
        linkTreeTable.setRowHeight(chartPanel.getRowHeight());
    }

    public void chartView()
    {
        try
        {
            treeOption = 1;
            if(_session == null)
                _session = new Session("chartSession");
            if(linkRootNode != null)
            {
                linkRootNode.removeAllChildren();
                linkTreeTable = null;
                linkListener = null;
                expansionListener = null;
                if(assoList == null)
                {
                    linkTableScrPane.setViewportView(linkTreeTable);
                    linkTableScrPane.updateUI();
                    return;
                }
                TreeNodeObject nodeData = (TreeNodeObject)linkRootNode.getUserObject();
                nodeData.setPopulated(false);
                makeLinkTable(PROCESSING_MODE);
                expansionListener = new ExpansionListener();
                linkTreeTable.tree.addTreeExpansionListener(expansionListener);
                createPopup();
                UIUtils.expandTreeFullLevel(linkTreeTable.tree);
                initializeChartData();
                setChartData();
                linkTableScrPane.setViewportView(linkTreeTable);
                linkTableScrPane.updateUI();
                linkSplitPane.setRightComponent(linkChartScrPane);
                linkSplitPane.setDividerLocation(200);
                PROCESSING_MODE = 0;
            }
            linkTreeTable.setRowHeight(chartPanel.getRowHeight());
            if(chartTableHeaderRenderer == null)
                chartTableHeaderRenderer = UIBuilder.getHeaderRenderer(chartPanel.getHeaderHeight());
            TableColumn tc;
            for(Enumeration enum = linkTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(chartTableHeaderRenderer))
                tc = (TableColumn)enum.nextElement();

        }
        catch(RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public void networkView()
    {
        try
        {
            treeOption = 1;
            if(linkRootNode != null)
            {
                linkRootNode.removeAllChildren();
                linkTreeTable = null;
                linkListener = null;
                if(assoList == null)
                {
                    linkTableScrPane.setViewportView(linkTreeTable);
                    linkTableScrPane.updateUI();
                    return;
                }
                TreeNodeObject nodeData = (TreeNodeObject)linkRootNode.getUserObject();
                nodeData.setPopulated(false);
                makeLinkTable(PROCESSING_MODE);
                createPopup();
                UIUtils.expandTreeFullLevel(linkTreeTable.tree);
                setNetworkData();
                linkTableScrPane.setViewportView(linkTreeTable);
                linkTableScrPane.updateUI();
                linkSplitPane.setRightComponent(networkPanel);
                linkSplitPane.setDividerLocation(200);
                networkPanel.loadWorkflow(instanceOuid);
                PROCESSING_MODE = 0;
            }
        }
        catch(RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public boolean setTreeLinkView(String subTabName)
    {
        if(Utils.isNullArrayList(assoList))
            return false;
        JTabbedPane fieldGroupPane = mwlm.getCenterTabbedPane();
        int tabCnt = fieldGroupPane.getTabCount();
        if(tabIndexForObjectTree >= 0)
            fieldGroupPane.setSelectedIndex(tabIndexForObjectTree);
        tabCnt = linkViewPane.getTabCount();
        for(int i = 0; i < tabCnt; i++)
        {
            if(!linkViewPane.getTitleAt(i).equals(subTabName))
                continue;
            linkViewPane.setSelectedIndex(i);
            break;
        }

        return true;
    }

    private void makeAssociationTabs()
    {
        DOSChangeable dosAssociation = null;
        ArrayList superClassOuidList = null;
        HashMap assoTabMap = null;
        ArrayList assoTabList = null;
        Iterator assoTabKey = null;
        String assoOuid = null;
        String tabTitle = null;
        String targetClassOuid = null;
        int end = 0;
        try
        {
            assoTabMap = dos.listEffectiveAssociation(classOuid, Boolean.FALSE, "association");
            if(assoTabMap == null || assoTabMap.size() == 0)
                return;
            superClassOuidList = dos.listAllSuperClassOuid(classOuid);
            if(superClassOuidList == null)
                superClassOuidList = new ArrayList();
            superClassOuidList.add(0, classOuid);
            if(associationMap == null)
                associationMap = new HashMap();
            assoTabList = new ArrayList();
            for(assoTabKey = assoTabMap.keySet().iterator(); assoTabKey.hasNext();)
            {
                dosAssociation = (DOSChangeable)assoTabMap.get(assoTabKey.next());
                if(dosAssociation != null)
                    assoTabList.add(dosAssociation);
            }

            assoTabKey = null;
            Utils.sort(assoTabList);
            for(assoTabKey = assoTabList.iterator(); assoTabKey.hasNext();)
            {
                dosAssociation = (DOSChangeable)assoTabKey.next();
                assoOuid = (String)dosAssociation.get("ouid");
                if(associationMap.containsKey(assoOuid))
                {
                    dosAssociation = null;
                } else
                {
                    tabTitle = null;
                    targetClassOuid = (String)dosAssociation.get("end1.ouid@class");
                    if(superClassOuidList.contains(targetClassOuid))
                    {
                        tabTitle = (String)dosAssociation.get("end2.name");
                        end = 2;
                    } else
                    {
                        targetClassOuid = (String)dosAssociation.get("end2.ouid@class");
                        if(superClassOuidList.contains(targetClassOuid))
                        {
                            tabTitle = (String)dosAssociation.get("end1.name");
                            end = 1;
                        }
                    }
                    if(tabTitle != null)
                    {
                        mwlm.addCenter(makeAssociationPanel(assoOuid, dosAssociation, end), tabTitle, null, tabTitle);
                        associationMap.put(assoOuid, dosAssociation);
                        dosAssociation = null;
                    }
                }
            }

            assoTabKey = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    private JPanel makeAssociationPanel(String assoOuid, DOSChangeable dosAssociation, int end)
    {
        JPanel assoPanel = new JPanel();
        JToolBar assoToolBar = new ExtToolBar(assoOuid, HeaderStyle.BOTH);
        JButton assoDetailButton = new ToolBarButton(new ImageIcon("icons/Open.gif"));
        JButton assoAssoDetailButton = new ToolBarButton(new ImageIcon("icons/LinkDetail.gif"));
        JButton assoAddButton = new ToolBarButton(new ImageIcon("icons/add_att.gif"));
        JButton assoRemoveButton = new ToolBarButton(new ImageIcon("icons/remove_att.gif"));
        JButton assoRefreshButton = new ToolBarButton(new ImageIcon("icons/Refresh.gif"));
        JButton assoCopyButton = new ToolBarButton(new ImageIcon("icons/Copy.gif"));
        JButton assoPasteButton = new ToolBarButton(new ImageIcon("icons/Paste.gif"));
        JButton assoSelectAllButton = new ToolBarButton(new ImageIcon("icons/SelectAll.gif"));
        JButton assoClearSelectionButton = new ToolBarButton(new ImageIcon("icons/ClearSelection.gif"));
        JScrollPane assoScrollPane = UIFactory.createStrippedScrollPane(null);
        Table table = null;
        HashMap filter = null;
        ArrayList results = null;
        LinkedList classList = null;
        String tmpClassOuid = null;
        if(associationTableMap == null)
            associationTableMap = new HashMap();
        assoPanel.setLayout(new BorderLayout());
        assoToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        assoToolBar.add(assoDetailButton);
        assoToolBar.add(assoAssoDetailButton);
        assoToolBar.add(Box.createHorizontalStrut(5));
        assoToolBar.add(assoAddButton);
        assoToolBar.add(assoRemoveButton);
        assoToolBar.add(Box.createHorizontalStrut(5));
        assoToolBar.add(assoRefreshButton);
        assoToolBar.add(Box.createHorizontalStrut(5));
        assoToolBar.add(assoCopyButton);
        assoToolBar.add(assoPasteButton);
        assoToolBar.add(Box.createGlue());
        assoToolBar.add(assoSelectAllButton);
        assoToolBar.add(assoClearSelectionButton);
        assoPanel.add(assoToolBar, "North");
        assoPanel.add(assoScrollPane, "Center");
        assoDetailButton.setToolTipText(DynaMOAD.getMSRString("WRD_0074", "Detail...", 3));
        assoDetailButton.setActionCommand("assoDetail/" + assoOuid);
        assoDetailButton.addActionListener(this);
        assoAssoDetailButton.setToolTipText(DynaMOAD.getMSRString("WRD_0075", "Association Detail...", 3));
        assoAssoDetailButton.setActionCommand("assoAssoDetail/" + assoOuid);
        assoAssoDetailButton.addActionListener(this);
        assoAddButton.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Add object link...", 3));
        assoAddButton.setActionCommand("assoAdd/" + assoOuid);
        assoAddButton.addActionListener(this);
        assoRemoveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Remove object link", 3));
        assoRemoveButton.setActionCommand("assoRemove/" + assoOuid);
        assoRemoveButton.addActionListener(this);
        assoRefreshButton.setToolTipText(DynaMOAD.getMSRString("WRD_0013", "refresh", 3));
        assoRefreshButton.setActionCommand("assoRefresh/" + assoOuid);
        assoRefreshButton.addActionListener(this);
        assoCopyButton.setToolTipText(DynaMOAD.getMSRString("WRD_0009", "copy", 3));
        assoCopyButton.setActionCommand("assoCopy/" + assoOuid);
        assoCopyButton.addActionListener(this);
        assoPasteButton.setToolTipText(DynaMOAD.getMSRString("WRD_0010", "paste", 3));
        assoPasteButton.setActionCommand("assoPaste/" + assoOuid);
        assoPasteButton.addActionListener(this);
        assoSelectAllButton.setToolTipText(DynaMOAD.getMSRString("MNU_SELECTALL_SEARCHRESULT", "Select All", 3));
        assoSelectAllButton.setActionCommand("selectAllButton/" + assoOuid);
        assoSelectAllButton.addActionListener(this);
        assoClearSelectionButton.setToolTipText(DynaMOAD.getMSRString("MNU_CLEARSELECTION_SEARCHRESULT", "Clear Selection", 3));
        assoClearSelectionButton.setActionCommand("clearSelectionButton/" + assoOuid);
        assoClearSelectionButton.addActionListener(this);
        assoAddButton.setEnabled(hasLinkPermission);
        assoRemoveButton.setEnabled(hasLinkPermission);
        assoPasteButton.setEnabled(hasLinkPermission);
        classList = new LinkedList();
        if(end == 1)
            classList.add(dosAssociation.get("end1.ouid@class"));
        else
        if(end == 2)
            classList.add(dosAssociation.get("end2.ouid@class"));
        tmpClassOuid = (String)dosAssociation.get("ouid@class");
        if(tmpClassOuid != null && (!Utils.isNullString(tmpClassOuid) || !classList.contains(tmpClassOuid)))
            classList.add(tmpClassOuid);
        if(classList.size() > 0)
        {
            ArrayList actionButtonList = UIBuilder.createActionToolbarButtons((String)classList.get(0), this, assoOuid);
            if(!Utils.isNullArrayList(actionButtonList))
            {
                assoToolBar.add(Box.createHorizontalStrut(10));
                JButton actionButton = null;
                for(Iterator actionKey = actionButtonList.iterator(); actionKey.hasNext();)
                {
                    actionButton = (JButton)actionKey.next();
                    assoToolBar.add(actionButton);
                    actionButton = null;
                }

            }
        }
        ArrayList fieldList = null;
        ArrayList tmpFieldList = null;
        ArrayList tmpColumnOuidList = null;
        ArrayList tmpColumnNameList = null;
        ArrayList tmpColumnWidthList = null;
        Iterator classKey = null;
        Iterator fieldKey = null;
        DOSChangeable dosField = null;
        Boolean isVisible = null;
        String value = null;
        tmpColumnOuidList = new ArrayList();
        tmpColumnNameList = new ArrayList();
        tmpColumnWidthList = new ArrayList();
        try
        {
            fieldList = new ArrayList();
            for(classKey = classList.iterator(); classKey.hasNext();)
            {
                tmpClassOuid = (String)classKey.next();
                tmpFieldList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
                if(tmpFieldList != null)
                {
                    for(int i = 0; i < tmpFieldList.size(); i++)
                    {
                        value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)tmpFieldList.get(i));
                        dosField = dos.getField(value);
                        fieldList.add(dosField);
                        dosField = null;
                    }

                } else
                {
                    tmpFieldList = UIBuilder.getDefaultFieldList(dos, tmpClassOuid);
                    if(tmpFieldList != null && tmpFieldList.size() != 0)
                    {
                        fieldList.addAll(tmpFieldList);
                        tmpFieldList = null;
                    }
                }
            }

            classKey = null;
            Utils.sort(fieldList);
            fieldKey = fieldList.iterator();
            while(fieldKey.hasNext()) 
            {
                dosField = (DOSChangeable)fieldKey.next();
                isVisible = (Boolean)dosField.get("is.visible");
                if(isVisible == null || Utils.getBoolean(isVisible))
                {
                    if(tmpColumnOuidList.contains(dosField.get("ouid")))
                    {
                        dosField.clear();
                        dosField = null;
                        continue;
                    }
                    tmpColumnOuidList.add(dosField.get("ouid"));
                    tmpColumnNameList.add(DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0));
                    tmpColumnWidthList.add(new Integer(80));
                }
                dosField.clear();
                dosField = null;
            }
            fieldKey = null;
            fieldList.clear();
            fieldList = null;
            if(Utils.isNullString(instanceOuid))
            {
                results = null;
            } else
            {
                filter = new HashMap();
                filter.put("list.mode", "association");
                filter.put("ouid@association.class", assoOuid);
                if(PROCESSING_MODE == 0)
                {
                    System.out.println("makeAssociationPanel : PROCESSING_MODE = 0 : listLinkFrom");
                    results = dos.listLinkFrom(instanceOuid, tmpColumnOuidList, filter);
                } else
                {
                    System.out.println("makeAssociationPanel : PROCESSING_MODE != 0 : listLinkTo");
                    results = dos.listLinkTo(instanceOuid, tmpColumnOuidList, filter);
                }
                filter.clear();
                filter = null;
            }
            int sequences[] = new int[tmpColumnNameList.size()];
            for(int x = 0; x < tmpColumnNameList.size(); x++)
                sequences[x] = x + 1;

            table = new Table(results, tmpColumnNameList, tmpColumnWidthList, 1, 120);
            table.setColumnSequence(sequences);
            table.setIndexColumn(0);
            associationTableMap.put(assoOuid, table);
            table.getTable().addMouseListener(new AssociationPopupMenu(assoOuid));
            table.getTable().setCursor(handCursor);
            assoScrollPane.setViewportView(table.getTable());
            assoScrollPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
            Dimension dimension = new Dimension(300, 150);
            assoPanel.setMinimumSize(dimension);
            assoPanel.setPreferredSize(dimension);
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        classList.clear();
        classList = null;
        return assoPanel;
    }

    public void refreshAssociationTable(String associationOuid)
    {
        HashMap filter = null;
        ArrayList results = null;
        ArrayList superClassOuidList = null;
        LinkedList classList = null;
        String tmpClassOuid = null;
        String targetClassOuid = null;
        DOSChangeable dosAssociation = null;
        dosAssociation = (DOSChangeable)associationMap.get(associationOuid);
        if(dosAssociation != null)
        {
            classList = new LinkedList();
            try
            {
                superClassOuidList = dos.listAllSuperClassOuid(classOuid);
                if(superClassOuidList == null)
                    superClassOuidList = new ArrayList();
                superClassOuidList.add(0, classOuid);
                targetClassOuid = (String)dosAssociation.get("end1.ouid@class");
                if(superClassOuidList.contains(targetClassOuid))
                {
                    classList.add(dosAssociation.get("end2.ouid@class"));
                } else
                {
                    targetClassOuid = (String)dosAssociation.get("end2.ouid@class");
                    if(superClassOuidList.contains(targetClassOuid))
                        classList.add(dosAssociation.get("end1.ouid@class"));
                }
                tmpClassOuid = (String)dosAssociation.get("ouid@class");
                if(tmpClassOuid != null && (!Utils.isNullString(tmpClassOuid) || !classList.contains(tmpClassOuid)))
                    classList.add(tmpClassOuid);
                ArrayList fieldList = null;
                ArrayList tmpFieldList = null;
                ArrayList tmpColumnOuidList = null;
                ArrayList tmpColumnNameList = null;
                ArrayList tmpColumnWidthList = null;
                Iterator classKey = null;
                Iterator fieldKey = null;
                DOSChangeable dosField = null;
                Boolean isVisible = null;
                tmpColumnOuidList = new ArrayList();
                tmpColumnNameList = new ArrayList();
                tmpColumnWidthList = new ArrayList();
                fieldList = new ArrayList();
                String value = null;
                for(classKey = classList.iterator(); classKey.hasNext();)
                {
                    tmpClassOuid = (String)classKey.next();
                    tmpFieldList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
                    if(tmpFieldList != null)
                    {
                        for(int i = 0; i < tmpFieldList.size(); i++)
                        {
                            value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)tmpFieldList.get(i));
                            dosField = dos.getField(value);
                            fieldList.add(dosField);
                            dosField = null;
                        }

                    } else
                    {
                        tmpFieldList = UIBuilder.getDefaultFieldList(dos, tmpClassOuid);
                        if(tmpFieldList != null && tmpFieldList.size() != 0)
                        {
                            fieldList.addAll(tmpFieldList);
                            tmpFieldList = null;
                        }
                    }
                }

                classKey = null;
                Utils.sort(fieldList);
                fieldKey = fieldList.iterator();
                while(fieldKey.hasNext()) 
                {
                    dosField = (DOSChangeable)fieldKey.next();
                    isVisible = (Boolean)dosField.get("is.visible");
                    if(isVisible == null || Utils.getBoolean(isVisible))
                    {
                        if(tmpColumnOuidList.contains(dosField.get("ouid")))
                        {
                            dosField.clear();
                            dosField = null;
                            continue;
                        }
                        tmpColumnOuidList.add(dosField.get("ouid"));
                        tmpColumnNameList.add(DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0));
                        tmpColumnWidthList.add(new Integer(80));
                    }
                    dosField.clear();
                    dosField = null;
                }
                fieldKey = null;
                fieldList.clear();
                fieldList = null;
                filter = new HashMap();
                filter.put("list.mode", "association");
                filter.put("ouid@association.class", associationOuid);
                System.out.println("refreshAssociationTable : PROCESSING_MODE = 0 : listLinkFrom");
                results = dos.listLinkFrom(instanceOuid, tmpColumnOuidList, filter);
                filter.clear();
                filter = null;
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        }
        Table table = (Table)associationTableMap.get(associationOuid);
        ArrayList originalList = null;
        if(table != null)
            originalList = table.getList();
        else
            return;
        if(originalList != null)
        {
            originalList.clear();
            if(results != null)
            {
                for(int x = 0; x < results.size(); x++)
                    originalList.add(results.get(x));

                results.clear();
                results = null;
            }
        } else
        if(results != null)
        {
            originalList = new ArrayList();
            for(int x = 0; x < results.size(); x++)
                originalList.add(results.get(x));

            results.clear();
            results = null;
            table.setData(originalList);
        }
        table.changeTableData();
        table = null;
    }

    private void refreshPermissionValues()
    {
        try
        {
            statusCode = dos.getStatus(instanceOuid);
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        if(statusCode == null)
            return;
        hasUpdatePermission = false;
        hasDeletePermission = false;
        hasLinkPermission = false;
        hasFileUpdatePermission = false;
        hasPrintPermission = false;
        if("WIP".equals(statusCode) || "CRT".equals(statusCode) || "REJ".equals(statusCode))
            if(LogIn.isAdmin)
            {
                hasUpdatePermission = true;
                hasDeletePermission = true;
                hasLinkPermission = true;
                hasFileUpdatePermission = true;
                hasPrintPermission = true;
            } else
            {
                try
                {
                    hasUpdatePermission = acl.hasPermission(classOuid, instanceOuid, "UPDATE", LogIn.userID);
                    hasDeletePermission = acl.hasPermission(classOuid, instanceOuid, "DELETE", LogIn.userID);
                    hasLinkPermission = acl.hasPermission(classOuid, instanceOuid, "LINK / UNLINK", LogIn.userID);
                    hasFileUpdatePermission = acl.hasPermission(classOuid, instanceOuid, "CHECK-IN / CHECK-OUT", LogIn.userID);
                    hasPrintPermission = acl.hasPermission(classOuid, instanceOuid, "PRINT", LogIn.userID);
                }
                catch(IIPRequestException e)
                {
                    e.printStackTrace();
                }
            }
    }

    public void setSession(Session session)
    {
        _session = session;
    }

    public void setFieldGroupEnabled(String name, boolean enable)
    {
        if(fieldGroupNameToTitleMap == null || fieldGroupNameToTitleMap.isEmpty())
            return;
        String title = (String)fieldGroupNameToTitleMap.get(name);
        if(Utils.isNullString(title))
            return;
        String titleTemp = null;
        JTabbedPane fieldGroupPane = mwlm.getCenterTabbedPane();
        int count = fieldGroupPane.getTabCount();
        int i;
        for(i = 0; i < count; i++)
        {
            titleTemp = fieldGroupPane.getTitleAt(i);
            if(title.equals(titleTemp))
                break;
            titleTemp = null;
        }

        if(!Utils.isNullString(titleTemp))
            fieldGroupPane.setEnabledAt(i, enable);
    }

    public void makeLinkPanel()
    {
        linkSaveButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/SaveAs16.gif")));
        linkSaveButton.setToolTipText(DynaMOAD.getMSRString("MNU_SAVE_SEARCHRESULT", "Save as...", 3));
        linkSaveButton.setActionCommand("searchResultButton");
        linkSaveButton.addActionListener(this);
        linkOpenButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/add_att.gif")));
        linkOpenButton.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Add a object...", 3));
        linkOpenButton.setActionCommand("Link");
        linkOpenButton.addActionListener(this);
        linkOpenButton.setEnabled(hasLinkPermission);
        if(PROCESSING_MODE == 0)
        {
            linkCompositeButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/CompositeOf.gif")));
            linkCompositeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0126", "Composite of", 0));
            linkCompositeButton.setActionCommand("Composite of");
        } else
        {
            linkCompositeButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/InverseProcess.gif")));
            linkCompositeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0127", "Where used", 0));
            linkCompositeButton.setActionCommand("Inverse Process");
        }
        linkCompositeButton.addActionListener(this);
        linkCompositeOfMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0126", "Composite of", 0));
        linkCompositeOfMenuItem.setActionCommand("Composite of");
        linkCompositeOfMenuItem.setIcon(new ImageIcon(getClass().getResource("/icons/CompositeOf.gif")));
        linkCompositeOfMenuItem.addActionListener(this);
        linkWhereUsedMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0127", "Where used", 0));
        linkWhereUsedMenuItem.setActionCommand("Inverse Process");
        linkWhereUsedMenuItem.setIcon(new ImageIcon(getClass().getResource("/icons/InverseProcess.gif")));
        linkWhereUsedMenuItem.addActionListener(this);
        linkCompositePopupMenu = new JPopupMenu();
        linkCompositePopupMenu.add(linkCompositeOfMenuItem);
        linkCompositePopupMenu.add(linkWhereUsedMenuItem);
        linkCompositePopupButton = new PopupButton(linkCompositeButton, linkCompositePopupMenu);
        linkFilterButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/filter.gif")));
        linkFilterButton.setText(DynaMOAD.getMSRString("WRD_0015", "filter", 3));
        linkFilterButton.setToolTipText(DynaMOAD.getMSRString("WRD_0015", "filter", 3));
        linkFilterButton.setActionCommand("Link Filter");
        linkFilterButton.addActionListener(this);
        makeFilterComboBoxForLink();
        linkChartButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/ChartView.gif")));
        linkChartButton.setToolTipText(DynaMOAD.getMSRString("WRD_0085", "Chart View", 3));
        linkChartButton.setActionCommand("Chart View");
        linkChartButton.addActionListener(this);
        chartSettingButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/ChartViewSetting.gif")));
        chartSettingButton.setToolTipText(DynaMOAD.getMSRString("WRD_0086", "Chart Setting", 3));
        chartSettingButton.setActionCommand("Chart Setting");
        chartSettingButton.addActionListener(this);
        networkButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/clone.gif")));
        networkButton.setActionCommand("networkView");
        networkButton.addActionListener(this);
        linkToolBar = new ExtToolBar("TreeToolBar", HeaderStyle.BOTH);
        linkToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        linkToolBar.add(linkSaveButton);
        linkToolBar.add(linkFilterButton);
        linkToolBar.add(linkFilterComboBox);
        linkToolBar.add(Box.createHorizontalStrut(5));
        linkToolBar.add(linkOpenButton);
        linkCompositePopupButton.addTo(linkToolBar);
        linkToolBar.add(linkChartButton);
        if(LogIn.isAdmin)
            linkToolBar.add(chartSettingButton);
        linkToolBar.add(networkButton);
        linkToolBar.add(Box.createHorizontalStrut(120));
        makeLinkTable(PROCESSING_MODE);
        linkTableScrPane = UIFactory.createStrippedScrollPane(null);
        linkTableScrPane.setViewportView(linkTreeTable);
        linkTableScrPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
        chartPanel = new GanttChartPanel(this, 0, chartFieldData, 0, 0);
        linkChartScrPane = UIFactory.createStrippedScrollPane(null);
        linkChartScrPane.createVerticalScrollBar();
        linkChartScrPane.setViewportView(chartPanel);
        linkChartScrPane.setColumnHeaderView(chartPanel.getChartHeader());
        linkSplitPane = new BorderlessSplitPane(1, linkTableScrPane, null);
        linkSplitPane.setDividerSize(3);
        linkSplitPane.setDividerLocation(200);
        networkPanel = new NetworkEditorPanel(instanceOuid, saveButton.isEnabled());
        linkPanel = new JPanel();
        linkPanelBorderLayout = new BorderLayout();
        linkPanel.setLayout(linkPanelBorderLayout);
        linkPanel.add(linkToolBar, "North");
        linkPanel.add(linkSplitPane, "Center");
        linkViewPane = new JTabbedPane();
        linkViewPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        linkViewPane.setTabPlacement(3);
        linkViewPane.addChangeListener(this);
        linkViewPane.addTab("Default", linkPanel);
        makeViewTabs();
        createPopup();
    }

    private void makeFilePanel()
    {
        fileOpenButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/FileAttach.gif")));
        fileOpenButton.setToolTipText(DynaMOAD.getMSRString("WRD_0083", "File attach", 3));
        fileOpenButton.setActionCommand("File Open");
        fileOpenButton.addActionListener(this);
        fileReadOnlyButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/executionView.gif")));
        fileReadOnlyButton.setEnabled(false);
        fileReadOnlyButton.setToolTipText(DynaMOAD.getMSRString("WRD_0062", "Read", 3));
        fileReadOnlyButton.setActionCommand("Read Only");
        fileReadOnlyButton.addActionListener(this);
        fileToolBar = new ExtToolBar("fileToolBar", HeaderStyle.BOTH);
        fileToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        fileToolBar.add(fileOpenButton);
        fileToolBar.add(fileReadOnlyButton);
        makeFileTable();
        fileTableScrPane = UIFactory.createStrippedScrollPane(null);
        fileTableScrPane.setViewportView(fileTreeTable);
        fileTableScrPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
        filePanel = new JPanel();
        filePanelBorderLayout = new BorderLayout();
        filePanel.setLayout(filePanelBorderLayout);
        filePanel.add(fileToolBar, "North");
        filePanel.add(fileTableScrPane, "Center");
        createPopup();
    }

    private void makeHistoryPanel()
    {
        historyDetailButton = new ToolBarButton(new ImageIcon("icons/Open.gif"));
        historyDetailButton.setToolTipText(DynaMOAD.getMSRString("WRD_0074", "Detail", 3));
        historyDetailButton.setEnabled(false);
        historyDetailButton.setActionCommand("historyDetailButton");
        historyDetailButton.addActionListener(this);
        historyToolBar = new ExtToolBar("historyToolBar", HeaderStyle.BOTH);
        historyToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        historyToolBar.add(historyDetailButton);
        makeHistoryTable();
        historyTableScrPane = UIFactory.createStrippedScrollPane(null);
        historyTableScrPane.setViewportView(historyTable.getTable());
        historyTableScrPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
        historyPanel = new JPanel();
        historyPanelBorderLayout = new BorderLayout();
        historyPanel.setLayout(historyPanelBorderLayout);
        historyPanel.add(historyToolBar, "North");
        historyPanel.add(historyTableScrPane, "Center");
        createPopup();
    }

    private void makeProcessPanel()
    {
        processOpenButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        processOpenButton.setText(DynaMOAD.getMSRString("WRD_0074", "Detail", 3));
        processOpenButton.setEnabled(false);
        processOpenButton.setToolTipText(DynaMOAD.getMSRString("WRD_0074", "Detail", 3));
        processOpenButton.setActionCommand("processDetail");
        processOpenButton.addActionListener(this);
        processToolBar = new ExtToolBar("processToolBar", HeaderStyle.BOTH);
        processToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        processToolBar.add(processOpenButton);
        makeProcessTable();
        processTableScrPane = UIFactory.createStrippedScrollPane(null);
        processTableScrPane.setViewportView(processTable.getTable());
        processTableScrPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
        processPanel = new JPanel();
        processPanel.setLayout(new BorderLayout());
        processPanel.add(processToolBar, "North");
        processPanel.add(processTableScrPane, "Center");
        createPopup();
    }

    private void makeViewTabs()
    {
        ArrayList lcLinkTableColumnNames = new ArrayList();
        ArrayList lcLinkTableColumnWidths = new ArrayList();
        ArrayList lcLinkData = new ArrayList();
        ArrayList classList = null;
        ArrayList fieldList = null;
        ArrayList tmpFieldList = null;
        Iterator assoKey = null;
        Iterator classKey = null;
        Iterator fieldKey = null;
        DOSChangeable dosAssociation = null;
        DOSChangeable dosField = null;
        Boolean isVisible = null;
        byte dataType = 0;
        boolean existsAssociationClass = false;
        String tmpClassOuid = null;
        String ouidStr = null;
        try
        {
            if(assoList == null)
                return;
            getClass();
            if(uiMode == 1)
                ouidStr = dos.getClassOuid(instanceOuid);
            else
                ouidStr = classOuid;
            classList = new ArrayList();
            for(assoKey = assoList.iterator(); assoKey.hasNext();)
            {
                dosAssociation = (DOSChangeable)assoKey.next();
                if(PROCESSING_MODE == 0)
                    tmpClassOuid = (String)dosAssociation.get("end2.ouid@class");
                else
                    tmpClassOuid = (String)dosAssociation.get("end1.ouid@class");
                if(Utils.isNullString(tmpClassOuid) || classList.contains(tmpClassOuid))
                {
                    dosAssociation = null;
                } else
                {
                    classList.add(tmpClassOuid);
                    tmpClassOuid = (String)dosAssociation.get("ouid@class");
                    if(Utils.isNullString(tmpClassOuid) || classList.contains(tmpClassOuid))
                    {
                        dosAssociation = null;
                    } else
                    {
                        classList.add(tmpClassOuid);
                        existsAssociationClass = true;
                        dosAssociation = null;
                    }
                }
            }

            assoKey = null;
            if(classList.size() == 0)
            {
                classList = null;
                return;
            }
            ArrayList pubFieldColumn = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid);
            String filterArgument = null;
            ArrayList filterField = null;
            String filterID = null;
            String value = null;
            if(pubFieldColumn != null)
            {
                for(int i = 0; i < pubFieldColumn.size(); i++)
                {
                    HashMap lcSearchCondition = new HashMap();
                    filterArgument = nds.getArgument("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid + "/" + pubFieldColumn.get(i), "isview");
                    if(!Utils.isNullString(filterArgument) && !filterArgument.equals("F"))
                    {
                        filterField = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid + "/" + pubFieldColumn.get(i));
                        for(int j = 0; j < filterField.size(); j++)
                        {
                            filterID = (String)filterField.get(j);
                            value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid + "/" + pubFieldColumn.get(i) + "/" + filterID);
                            lcSearchCondition.put(filterID, value);
                        }

                        if(!lcSearchCondition.containsKey("version.condition.type"))
                            lcSearchCondition.put("version.condition.type", "wip");
                        ArrayList lcColumnOuidList = new ArrayList();
                        ArrayList lcColumnNameList = new ArrayList();
                        ArrayList lcColumnClassList = new ArrayList();
                        ArrayList lcColumnTypeList = new ArrayList();
                        ArrayList lcColumnList = new ArrayList();
                        fieldList = new ArrayList();
                        for(classKey = classList.iterator(); classKey.hasNext();)
                        {
                            tmpClassOuid = (String)classKey.next();
                            tmpFieldList = UIBuilder.getDefaultFieldList(dos, tmpClassOuid);
                            if(tmpFieldList != null && tmpFieldList.size() != 0)
                            {
                                fieldList.addAll(tmpFieldList);
                                tmpFieldList = null;
                            }
                        }

                        classKey = null;
                        Utils.sort(fieldList);
                        fieldKey = fieldList.iterator();
                        while(fieldKey.hasNext()) 
                        {
                            dosField = (DOSChangeable)fieldKey.next();
                            isVisible = (Boolean)dosField.get("is.visible");
                            if(isVisible == null || Utils.getBoolean(isVisible))
                            {
                                if(lcColumnOuidList.contains(dosField.get("ouid")))
                                {
                                    dosField = null;
                                    continue;
                                }
                                lcColumnOuidList.add(dosField.get("ouid"));
                                titleValue = DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0);
                                lcColumnNameList.add(titleValue);
                                lcColumnTypeList.add(dosField.get("type"));
                                lcColumnList.add(dosField);
                                dataType = Utils.getByte((Byte)dosField.get("type"));
                                switch(dataType)
                                {
                                case 1: // '\001'
                                    lcColumnClassList.add(java.lang.Boolean.class);
                                    break;

                                case 2: // '\002'
                                    lcColumnClassList.add(java.lang.Byte.class);
                                    break;

                                case 3: // '\003'
                                    lcColumnClassList.add(java.lang.Character.class);
                                    break;

                                case 4: // '\004'
                                    lcColumnClassList.add(java.lang.Double.class);
                                    break;

                                case 5: // '\005'
                                    lcColumnClassList.add(java.lang.Float.class);
                                    break;

                                case 6: // '\006'
                                    lcColumnClassList.add(java.lang.Integer.class);
                                    break;

                                case 7: // '\007'
                                    lcColumnClassList.add(java.lang.Long.class);
                                    break;

                                case 8: // '\b'
                                    lcColumnClassList.add(java.lang.Short.class);
                                    break;

                                default:
                                    lcColumnClassList.add(java.lang.String.class);
                                    break;
                                }
                            }
                            dosField = null;
                        }
                        fieldKey = null;
                        fieldList.clear();
                        fieldList = null;
                        cNames = new String[lcColumnNameList.size()];
                        lcColumnNameList.toArray(cNames);
                        cTypes = new Class[lcColumnClassList.size()];
                        lcColumnClassList.toArray(cTypes);
                        selectedRows = new int[lcColumnNameList.size()];
                        for(int j = 0; j < lcColumnNameList.size(); j++)
                        {
                            lcLinkTableColumnNames.add((String)lcColumnNameList.get(j));
                            lcLinkTableColumnWidths.add(new Integer(80));
                            selectedRows[j] = j + 1;
                        }

                        lcColumnNameList.clear();
                        lcColumnNameList = null;
                        lcColumnClassList.clear();
                        lcColumnClassList = null;
                        DOSChangeable getDC = getDOSGlobal;
                        TreeNodeObject treeNodeData = new TreeNodeObject(instanceOuid + "^" + ouidStr, (String)getDC.get("md$number"), "Class");
                        DefaultMutableTreeNode lcLinkRootNode = new DefaultMutableTreeNode(treeNodeData);
                        ArrayList rootData = new ArrayList(selectedRows.length);
                        rootData.add((String)getDC.get("ouid"));
                        rootData.add((String)getDC.get("md$number"));
                        if(existsAssociationClass)
                            rootData.add("");
                        for(int j = 0; j < lcLinkTableColumnNames.size() - 1; j++)
                            rootData.add("");

                        lcLinkData.add(rootData.clone());
                        LinkTreeModel lcLinkModel = new LinkTreeModel(lcLinkRootNode, assoList, PROCESSING_MODE);
                        JTreeTable lcLinkTreeTable = new JTreeTable(lcLinkModel);
                        LinkListener lcLinkListener = new LinkListener();
                        lcLinkTreeTable.addTreeSelectionListener(lcLinkListener);
                        ArrayList tmpColumnOuidList = lcLinkModel.getColumnOuidList();
                        lcLinkTreeTable.getTableHeader().setBackground(DynaTheme.tableheaderColor);
                        TableColumn tc;
                        for(Enumeration enum = lcLinkTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
                            tc = (TableColumn)enum.nextElement();

                        getClass();
                        if(uiMode == 1)
                        {
                            ArrayList rows = null;
                            rows = dos.listLinkFrom(instanceOuid, tmpColumnOuidList, lcSearchCondition);
                            ArrayList row = null;
                            if(rows != null)
                            {
                                hasChild = true;
                                for(Iterator rowsKey = rows.iterator(); rowsKey.hasNext();)
                                {
                                    row = (ArrayList)rowsKey.next();
                                    lcLinkData.add(row.clone());
                                    row = null;
                                }

                            }
                        }
                        UIUtils.expandTree1Level(lcLinkTreeTable.tree);
                        JScrollPane lcLinkTableScrPane = UIFactory.createStrippedScrollPane(null);
                        lcLinkTableScrPane.setViewportView(lcLinkTreeTable);
                        lcLinkTableScrPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
                        lcLinkTableScrPane.updateUI();
                        linkViewPane.addTab((String)pubFieldColumn.get(i), lcLinkTableScrPane);
                        treeViewConditionMap.put((String)pubFieldColumn.get(i), lcSearchCondition);
                    }
                }

            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    private boolean isViewableFieldGroup(DOSChangeable fieldGroup)
    {
        if(fieldGroup == null)
            return false;
        Integer viewLocationInteger = (Integer)fieldGroup.get("location");
        int viewLocation;
        if(viewLocationInteger == null)
            viewLocation = 2;
        else
            viewLocation = viewLocationInteger.intValue();
        if(viewLocation != 1 && viewLocation != 2 && viewLocation != 3 && viewLocation != 4)
            return false;
        Boolean isVisible = (Boolean)fieldGroup.get("is.visible");
        return isVisible != null && isVisible.booleanValue();
    }

    private DOSChangeable getClassInfo(String classOuid)
    {
        if(Utils.isNullString(classOuid))
            return null;
        try
        {
            if(classInfo != null)
            {
                if(classOuid.equals(classInfo.get("ouid")))
                    return classInfo;
                classInfo = null;
            }
            classInfo = dos.getClass(classOuid);
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
            return null;
        }
        return classInfo;
    }

    public void setIcon(String classOuid)
    {
        ImageIcon imageIcon = ClientUtil.getImageIcon(classOuid);
        java.awt.Image image = null;
        if(imageIcon == null)
        {
            image = Toolkit.getDefaultToolkit().getImage("icons/Class.gif");
        } else
        {
            image = imageIcon.getImage();
            imageIcon = null;
        }
        setIconImage(image);
        image = null;
    }

    public DOSChangeable getInstanceData()
    {
        if(uiMode == 0)
        {
            setModifyData();
            return getDOSGlobal;
        }
        if(getDOSGlobal == null || getDOSGlobal.getValueMap() == null || getDOSGlobal.getValueMap().isEmpty())
            getDOS();
        return getDOSGlobal;
    }

    public void closeAfterAdd()
    {
        closeAfterAdd = true;
    }

    private String sessionProperty;
    private UIGeneration tmpUIGeneration;
    private DefaultMutableTreeNode tmpSelectedNode;
    private boolean isdebugmode;
    private DOS dos;
    private DSS dss;
    private NDS nds;
    private WFM wfm;
    private AUS aus;
    private WKS wks;
    private ACL acl;
    private UIManagement newUI;
    private JPanel searchConditionPanel;
    private JPanel searchResultPanel;
    private MWindowLocationManager mwlm;
    private String statusCode;
    private boolean hasUpdatePermission;
    private boolean hasDeletePermission;
    private boolean hasLinkPermission;
    private boolean hasFileUpdatePermission;
    private boolean hasPrintPermission;
    private String _serverPath;
    private String _clientPath;
    private FileTransferCallback _ftc;
    private DefaultTableCellRenderer chartTableHeaderRenderer;
    private Cursor handCursor;
    private FileTransferDialog ftd;
    private JMenuBar mainMenuBar;
    private JMenu mainMenu;
    private JToolBar mainToolBar;
    private JButton openButton;
    private JButton saveButton;
    private PopupButton hProcessSelectPopupButton;
    private JButton hProcessSelectButton;
    private JPopupMenu hProcessSelectPopupMenu;
    private JLabel statusField;
    private JPanel groupPanel;
    private JToolBar linkToolBar;
    private JButton linkOpenButton;
    private JButton linkSaveButton;
    private PopupButton linkCompositePopupButton;
    private JButton linkCompositeButton;
    private JPopupMenu linkCompositePopupMenu;
    private JMenuItem linkCompositeOfMenuItem;
    private JMenuItem linkWhereUsedMenuItem;
    private JButton linkFilterButton;
    public JComboBox linkFilterComboBox;
    private JButton linkChartButton;
    private JButton chartSettingButton;
    private JButton networkButton;
    private BorderLayout linkPanelBorderLayout;
    private JPanel linkPanel;
    private GanttChartPanel chartPanel;
    private NetworkEditorPanel networkPanel;
    private JSplitPane linkSplitPane;
    private JScrollPane linkTableScrPane;
    private JScrollPane linkChartScrPane;
    public JTreeTable linkTreeTable;
    private TreeSelectionListener linkListener;
    private TreeExpansionListener expansionListener;
    private JTabbedPane linkViewPane;
    private JToolBar fileToolBar;
    private JButton fileOpenButton;
    private JButton fileReadOnlyButton;
    private BorderLayout filePanelBorderLayout;
    private JPanel filePanel;
    private JScrollPane fileTableScrPane;
    private JTreeTable fileTreeTable;
    private TreeSelectionListener fileListener;
    private TreeTableModel fileModel;
    private JToolBar historyToolBar;
    private JButton historyDetailButton;
    private BorderLayout historyPanelBorderLayout;
    private JPanel historyPanel;
    private Table historyTable;
    private JScrollPane historyTableScrPane;
    private JToolBar processToolBar;
    private JButton processOpenButton;
    private JPanel processPanel;
    private Table processTable;
    private JScrollPane processTableScrPane;
    private JPopupMenu linkPopupMenu;
    private JPopupMenu filePopupMenu;
    private JMenuItem menuFileDetach;
    private JMenuItem menuFileCheckIn;
    private JMenuItem menuFileCheckOut;
    private JMenuItem menuFileReadOnly;
    private JMenuItem menuFileCheckOutCancel;
    private ArrayList groupList;
    private DOSChangeable fieldUIData;
    private DOSChangeable hoardData;
    private HashMap fieldAndFieldGroupMap;
    private ArrayList actionButtonList;
    private DOSChangeable selectButtonData;
    private ArrayList selectButtonList;
    private DOSChangeable dateChooserButtonData;
    private ArrayList dateChooserButtonList;
    public String modelOuid;
    public String classOuid;
    private DOSChangeable classInfo;
    private int uiMode;
    public String instanceOuid;
    private DefaultMutableTreeNode linkRootNode;
    private DefaultMutableTreeNode fileRootNode;
    private boolean hasChild;
    private HashMap searchConditionMap;
    private DOSChangeable getDOSGlobal;
    private FilterDialogForLink filterDialog;
    private String titleValue;
    private ArrayList processList;
    private ArrayList chartFieldOuid;
    private ArrayList chartFieldData;
    private int selectedRows[];
    private ArrayList historyTableColumnNames;
    private ArrayList historyTableColumnWidths;
    private ArrayList historyData;
    private ArrayList processTableColumnNames;
    private ArrayList processTableColumnWidths;
    private ArrayList processData;
    private ArrayList columnOuidList;
    private ArrayList columnList;
    private ArrayList chartFieldTitleList;
    private ArrayList chartFieldDescList;
    private ArrayList chartFieldTypeList;
    private ArrayList chartFieldOuidList;
    private int treeOption;
    private int defaultRowHeight;
    protected static String cNames[] = null;
    protected static Class cTypes[] = null;
    private ArrayList assoList;
    private ArrayList childColumnOuidList;
    static Session _session = null;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private String linkedInstanceOuid;
    private HashMap fieldGroupNameToTitleMap;
    private HashMap collectionMap;
    private int tabIndexForObjectTree;
    private int tabIndexForFile;
    private int tabIndexForVersion;
    private int tabIndexForProcess;
    private Component comp;
    private File files[];
    private ArrayList networkFieldOuid;
    public ArrayList networkFieldData;
    private HashMap associationMap;
    private HashMap associationTableMap;
    private HashMap treeViewConditionMap;
    private boolean closeAfterAdd;
    final int REGISTER_MODE = 0;
    final int MODIFY_MODE = 1;
    final int offset = 4;
    final boolean inverse_process = false;
    private int PROCESSING_MODE;
    private final int MINIMUM_WIDTH = 400;
    private final int MINIMUM_HEIGHT = 300;
    private final int WINDOW_HEIGHT_FACTOR = 140;
    private final int FRAME_HEIGHT_FACTOR = 32;































}
