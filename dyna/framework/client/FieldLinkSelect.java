// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FieldLinkSelect.java

package dyna.framework.client;

import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.ACL;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaDateChooser;
import dyna.uic.MInternalFrame;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, UIBuilder, LogIn, 
//            DFDefaultTreeCellRenderer, UIGeneration, FilterDialogForList, SearchConditionPanel, 
//            AssoClassUI, MainFrame

public class FieldLinkSelect extends JDialog
    implements ActionListener, WindowListener, MouseListener, TreeSelectionListener
{
    class PopupListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent mouseevent)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
            if(tree == null)
                return;
            node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            selectedClassOuid = tmpdata.getOuid();
            try
            {
                classDC = dos.getClass(tmpdata.getOuid());
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
            if(node == null || classDC == null)
                return;
            boolean permit = false;
            if(!node.isRoot() && node.isLeaf() && classDC.get("is.leaf") != null && Utils.getBoolean((Boolean)classDC.get("is.leaf")) && !Utils.getBoolean((Boolean)classDC.get("is.abstract")))
                permit = true;
            if(!LogIn.isAdmin)
                try
                {
                    permit = DynaMOAD.acl.hasPermission(tmpdata.getOuid(), "0", "CREATE", LogIn.userID) & permit;
                }
                catch(IIPRequestException e1)
                {
                    e1.printStackTrace();
                }
            else
                permit = permit;
            addMenu.setEnabled(permit);
            showPopup(e);
        }

        private void showPopup(MouseEvent e)
        {
            if(e.getClickCount() == 1 && e.isPopupTrigger())
                if(contentFrame instanceof MainFrame)
                {
                    Dimension framesize = ((MainFrame)contentFrame).getSize();
                    Dimension popupsize = popupMenu.getSize();
                    int dividerLocation = ((MainFrame)contentFrame).mainSplitPane.getDividerLocation();
                    if(popupsize.width <= 0 || popupsize.height <= 0)
                        popupsize = new Dimension(107, 100);
                    Point point = SwingUtilities.convertPoint(e.getComponent(), e.getX(), e.getY(), (MainFrame)contentFrame);
                    int x = e.getX();
                    int y = e.getY();
                    if(popupsize.width + point.x >= dividerLocation)
                        x -= popupsize.width;
                    if(popupsize.height + point.y >= framesize.height)
                        y -= popupsize.height;
                    popupMenu.show(e.getComponent(), x, y);
                } else
                {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
        }

        DefaultMutableTreeNode node;
        DOSChangeable classDC;

        PopupListener()
        {
            node = null;
            classDC = null;
        }
    }

    class ResultTable extends Table
    {

        public void valueChanged(ListSelectionEvent e)
        {
            super.valueChanged(e);
            ResultTable_valueChanged(this, e);
        }

        public ResultTable()
        {
        }

        public ResultTable(ArrayList dataList, ArrayList columnNameList, ArrayList columnWidth, int selection, int tableWidth)
        {
            super(dataList, columnNameList, columnWidth, selection, tableWidth);
        }

        public ResultTable(ArrayList datalist, ArrayList columnNamelist, ArrayList columnWidth, int Selection)
        {
            super(datalist, columnNamelist, columnWidth, Selection);
        }
    }


    public FieldLinkSelect(Frame parent, boolean modal, String ouid, TreeNodeObject selectedObject)
    {
        super(parent, modal);
        isdebugmode = false;
        handCursor = new Cursor(12);
        mainSplitPane = null;
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        rightPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        dateButton = new JButton();
        objectButton = new JButton();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        selectButton = new JButton();
        closeButton = new JButton();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = null;
        resultTable = new ResultTable();
        instanceOuid = null;
        associationOuid = null;
        searchResultFrame = null;
        searchResultToolBar = null;
        previousSearchButton = null;
        nextSearchButton = null;
        leftSplitPane = null;
        treeFrame = null;
        searchConditionFrame = null;
        searchConditionToolBar = null;
        optionFrame = null;
        filterDialog = null;
        searchButton2 = null;
        filterButton = null;
        clearButton = null;
        closeFrameButton = null;
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        searchData = null;
        columnName = null;
        columnWidth = null;
        fieldListForSearchCondition = null;
        classFieldList = null;
        this.ouid = "";
        fieldListForSearchResult = null;
        NDS_CODE = null;
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        selectionMode = 0;
        modelOuid = "";
        selectedClassOuid = "";
        end1Ouid = "";
        end2OuidList = new ArrayList();
        pageNumber = 1;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        isNotAssociationCase = false;
        returnValue = null;
        container = null;
        parentFr = parent;
        this.ouid = ouid;
        isNotAssociationCase = true;
        end1Ouid = selectedObject.getOuid();
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        setSize(800, 400);
        selectionMode = 1;
        rootnodedata = selectedObject;
        setTopNode(rootnodedata);
        DefaultTreeModel treemodel = (DefaultTreeModel)getTree().getModel();
        setFullTreeExpand((DefaultMutableTreeNode)treemodel.getRoot());
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treemodel.getRoot();
        if(rootNode != null)
        {
            DefaultMutableTreeNode firstNode = null;
            try
            {
                firstNode = (DefaultMutableTreeNode)rootNode.getFirstChild();
            }
            catch(NoSuchElementException e)
            {
                System.err.println(e);
            }
            if(firstNode != null)
            {
                UIUtils.scrollTreePathToVisible(tree, firstNode);
                firstNode = null;
            }
        }
        rootNode = null;
        treeModel = null;
        UIUtils.setLocationRelativeTo(this, parent);
    }

    public FieldLinkSelect(Frame parent, boolean modal, String ouid, String instanceOuid, String associationOuid)
    {
        super(parent, modal);
        isdebugmode = false;
        handCursor = new Cursor(12);
        mainSplitPane = null;
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        rightPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        dateButton = new JButton();
        objectButton = new JButton();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        selectButton = new JButton();
        closeButton = new JButton();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = null;
        resultTable = new ResultTable();
        this.instanceOuid = null;
        this.associationOuid = null;
        searchResultFrame = null;
        searchResultToolBar = null;
        previousSearchButton = null;
        nextSearchButton = null;
        leftSplitPane = null;
        treeFrame = null;
        searchConditionFrame = null;
        searchConditionToolBar = null;
        optionFrame = null;
        filterDialog = null;
        searchButton2 = null;
        filterButton = null;
        clearButton = null;
        closeFrameButton = null;
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        searchData = null;
        columnName = null;
        columnWidth = null;
        fieldListForSearchCondition = null;
        classFieldList = null;
        this.ouid = "";
        fieldListForSearchResult = null;
        NDS_CODE = null;
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        selectionMode = 0;
        modelOuid = "";
        selectedClassOuid = "";
        end1Ouid = "";
        end2OuidList = new ArrayList();
        pageNumber = 1;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        isNotAssociationCase = false;
        returnValue = null;
        container = null;
        parentFr = parent;
        this.ouid = ouid;
        end1Ouid = instanceOuid;
        this.instanceOuid = instanceOuid;
        this.associationOuid = associationOuid;
        isNotAssociationCase = false;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        setSize(800, 400);
        selectionMode = 1;
        rootnodedata = new TreeNodeObject(instanceOuid, "Class", "");
        setTopNode(rootnodedata);
        DefaultTreeModel treemodel = (DefaultTreeModel)getTree().getModel();
        setFullTreeExpand((DefaultMutableTreeNode)treemodel.getRoot());
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treemodel.getRoot();
        if(rootNode != null)
        {
            DefaultMutableTreeNode firstNode = null;
            try
            {
                firstNode = (DefaultMutableTreeNode)rootNode.getFirstChild();
            }
            catch(NoSuchElementException e)
            {
                System.err.println(e);
            }
            if(firstNode != null)
            {
                UIUtils.scrollTreePathToVisible(tree, firstNode);
                firstNode = null;
            }
        }
        rootNode = null;
        treeModel = null;
        UIUtils.setLocationRelativeTo(this, parent);
    }

    public static String[] showDialog(Frame parent, Component comp, String classOuid, String signStr)
    {
        FieldLinkSelect fls = new FieldLinkSelect(parent, comp, classOuid, signStr);
        fls.setVisible(true);
        return fls.returnValue;
    }

    public FieldLinkSelect(Frame parent, Component comp, String classOuid, String signStr)
    {
        super(parent, true);
        isdebugmode = false;
        handCursor = new Cursor(12);
        mainSplitPane = null;
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        rightPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        dateButton = new JButton();
        objectButton = new JButton();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        selectButton = new JButton();
        closeButton = new JButton();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = null;
        resultTable = new ResultTable();
        instanceOuid = null;
        associationOuid = null;
        searchResultFrame = null;
        searchResultToolBar = null;
        previousSearchButton = null;
        nextSearchButton = null;
        leftSplitPane = null;
        treeFrame = null;
        searchConditionFrame = null;
        searchConditionToolBar = null;
        optionFrame = null;
        filterDialog = null;
        searchButton2 = null;
        filterButton = null;
        clearButton = null;
        closeFrameButton = null;
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        searchData = null;
        columnName = null;
        columnWidth = null;
        fieldListForSearchCondition = null;
        classFieldList = null;
        ouid = "";
        fieldListForSearchResult = null;
        NDS_CODE = null;
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        this.signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        selectionMode = 0;
        modelOuid = "";
        selectedClassOuid = "";
        end1Ouid = "";
        end2OuidList = new ArrayList();
        pageNumber = 1;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        isNotAssociationCase = false;
        returnValue = null;
        container = null;
        parentFr = parent;
        ouid = classOuid;
        this.comp = comp;
        isNotAssociationCase = false;
        this.signStr = signStr;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        setTitle(DynaMOAD.getMSRString("WRD_0084", "Item ", 3) + " " + DynaMOAD.getMSRString("WRD_0018", "Selection", 3));
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        setSize(800, 400);
        selectionMode = 0;
        rootnodedata = new TreeNodeObject(classOuid, "Class", "");
        setTopNode(rootnodedata);
        DefaultTreeModel treemodel = (DefaultTreeModel)getTree().getModel();
        setFullTreeExpand2((DefaultMutableTreeNode)treemodel.getRoot());
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treemodel.getRoot();
        if(rootNode != null)
        {
            DefaultMutableTreeNode firstNode = null;
            try
            {
                firstNode = (DefaultMutableTreeNode)rootNode.getFirstChild();
            }
            catch(NoSuchElementException e)
            {
                System.err.println(e);
            }
            if(firstNode != null)
            {
                UIUtils.scrollTreePathToVisible(tree, firstNode);
                firstNode = null;
            }
        }
        rootNode = null;
        treeModel = null;
        UIUtils.setLocationRelativeTo(this, parent);
    }

    public static String[] showDialog(Dialog parent, Component comp, String classOuid, String signStr)
    {
        FieldLinkSelect fls = new FieldLinkSelect(parent, comp, classOuid, signStr);
        fls.setVisible(true);
        return fls.returnValue;
    }

    public FieldLinkSelect(Dialog parent, Component comp, String classOuid, String signStr)
    {
        super(parent, true);
        isdebugmode = false;
        handCursor = new Cursor(12);
        mainSplitPane = null;
        mainPanel = new JPanel();
        toolPanel = new JPanel();
        rightPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        dateButton = new JButton();
        objectButton = new JButton();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        comparatorComboBox = new JComboBox();
        statusComboBox = new JComboBox();
        booleanComboBox = new JComboBox();
        valueTextField = new JTextField();
        selectButton = new JButton();
        closeButton = new JButton();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = null;
        resultTable = new ResultTable();
        instanceOuid = null;
        associationOuid = null;
        searchResultFrame = null;
        searchResultToolBar = null;
        previousSearchButton = null;
        nextSearchButton = null;
        leftSplitPane = null;
        treeFrame = null;
        searchConditionFrame = null;
        searchConditionToolBar = null;
        optionFrame = null;
        filterDialog = null;
        searchButton2 = null;
        filterButton = null;
        clearButton = null;
        closeFrameButton = null;
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        searchData = null;
        columnName = null;
        columnWidth = null;
        fieldListForSearchCondition = null;
        classFieldList = null;
        ouid = "";
        fieldListForSearchResult = null;
        NDS_CODE = null;
        DETAIL = 0;
        searchConditionData = new DOSChangeable();
        this.signStr = null;
        parentFr = null;
        orderOfCombo = new ArrayList();
        classInfo = null;
        selectionMode = 0;
        modelOuid = "";
        selectedClassOuid = "";
        end1Ouid = "";
        end2OuidList = new ArrayList();
        pageNumber = 1;
        jd = new JDialog(this, DynaMOAD.getMSRString("WRD_0158", "Search Date Range", 3));
        FromLabel = new JLabel("From");
        ToLabel = new JLabel("To");
        FromField = new JTextField();
        ToField = new JTextField();
        date_chooser = new JButton();
        date_chooser2 = new JButton();
        ok = new JButton();
        clear = new JButton();
        isNotAssociationCase = false;
        returnValue = null;
        container = null;
        parentFr = parent;
        ouid = classOuid;
        this.comp = comp;
        isNotAssociationCase = false;
        this.signStr = signStr;
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            modelOuid = dos.getWorkingModel();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        initComparator();
        initStatus();
        initBooleanComboBox();
        initComponents();
        setTitle(DynaMOAD.getMSRString("WRD_0084", "Item ", 3) + " " + DynaMOAD.getMSRString("WRD_0018", "Selection", 3));
        comparatorComboBox.setSelectedItem("Work In Progress");
        pack();
        setSize(800, 400);
        selectionMode = 0;
        rootnodedata = new TreeNodeObject(classOuid, "Class", "");
        setTopNode(rootnodedata);
        DefaultTreeModel treemodel = (DefaultTreeModel)getTree().getModel();
        setFullTreeExpand2((DefaultMutableTreeNode)treemodel.getRoot());
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treemodel.getRoot();
        if(rootNode != null)
        {
            DefaultMutableTreeNode firstNode = null;
            try
            {
                firstNode = (DefaultMutableTreeNode)rootNode.getFirstChild();
            }
            catch(NoSuchElementException e)
            {
                System.err.println(e);
            }
            if(firstNode != null)
            {
                UIUtils.scrollTreePathToVisible(tree, firstNode);
                firstNode = null;
            }
        }
        rootNode = null;
        treeModel = null;
        UIUtils.setLocationRelativeTo(this, parent);
    }

    private void initComponents()
    {
        treeScrPane = UIFactory.createStrippedScrollPane(null);
        treeScrPane.setViewportView(tree);
        treeScrPane.getViewport().setBackground(UIManagement.panelBackGround);
        treeFrame = new MInternalFrame();
        treeFrame.add(treeScrPane);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane.setViewportView(searchConditionPanel);
        searchButton2 = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton2.setActionCommand("search");
        searchButton2.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton2.addActionListener(this);
        filterButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/ConditionSave.gif")));
        filterButton.setActionCommand("filterSave");
        filterButton.setToolTipText(DynaMOAD.getMSRString("WRD_0015", "Filter", 3));
        filterButton.addActionListener(this);
        clearButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.setActionCommand("clearCondition");
        clearButton.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clearButton.addActionListener(this);
        closeFrameButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/close.gif")));
        closeFrameButton.setActionCommand("closeFrame");
        closeFrameButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeFrameButton.addActionListener(this);
        searchConditionToolBar = new ExtToolBar("Search Condition ToolBar");
        searchConditionToolBar.setLayout(new BoxLayout(searchConditionToolBar, 0));
        searchConditionToolBar.add(searchButton2);
        searchConditionToolBar.add(filterButton);
        searchConditionToolBar.add(clearButton);
        searchConditionToolBar.add(Box.createHorizontalStrut(15));
        searchConditionToolBar.add(closeFrameButton);
        searchConditionFrame = new MInternalFrame(DynaMOAD.getMSRString("WRD_0047", "Search Condition", 3));
        searchConditionFrame.add(conditionScrPane);
        searchConditionFrame.setToolBar(searchConditionToolBar);
        leftSplitPane = new BorderlessSplitPane(0, treeFrame, null);
        leftSplitPane.setDividerSize(0);
        leftSplitPane.setDividerLocation(100);
        leftSplitPane.setOneTouchExpandable(false);
        detailButton.setActionCommand("detail");
        detailButton.setIcon(new ImageIcon(getClass().getResource("/icons/detail.gif")));
        detailButton.setToolTipText(DynaMOAD.getMSRString("WRD_0017", "Detail", 3));
        detailButton.setMargin(new Insets(0, 0, 0, 0));
        detailButton.addActionListener(this);
        detailButton.setDoubleBuffered(true);
        searchButton.setActionCommand("search");
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.addActionListener(this);
        searchButton.setDoubleBuffered(true);
        dateButton.setActionCommand("date");
        dateButton.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateButton.setToolTipText("Date select");
        dateButton.setMargin(new Insets(0, 0, 0, 0));
        dateButton.addActionListener(this);
        dateButton.setDoubleBuffered(true);
        objectButton.setActionCommand("object");
        objectButton.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
        objectButton.setToolTipText("Object Select");
        objectButton.setMargin(new Insets(0, 0, 0, 0));
        objectButton.addActionListener(this);
        objectButton.setDoubleBuffered(true);
        comparatorComboBox.setDoubleBuffered(true);
        comparatorComboBox.setBackground(UIManagement.panelBackGround);
        comparatorComboBox.setBorder(UIManagement.borderTextBoxEditable);
        comparatorComboBox.setPreferredSize(new Dimension(100, 10));
        comparatorComboBox.setActionCommand("version type combo");
        comparatorComboBox.addActionListener(this);
        fieldComboBox.setDoubleBuffered(true);
        fieldComboBox.setBackground(UIManagement.panelBackGround);
        fieldComboBox.setBorder(UIManagement.borderTextBoxEditable);
        fieldComboBox.setPreferredSize(new Dimension(100, 10));
        fieldComboBox.setActionCommand("fieldCombo");
        fieldComboBox.addActionListener(this);
        statusComboBox.setDoubleBuffered(true);
        statusComboBox.setBackground(UIManagement.panelBackGround);
        statusComboBox.setBorder(UIManagement.borderTextBoxEditable);
        statusComboBox.setPreferredSize(new Dimension(100, 10));
        booleanComboBox.setDoubleBuffered(true);
        booleanComboBox.setBackground(UIManagement.panelBackGround);
        booleanComboBox.setBorder(UIManagement.borderTextBoxEditable);
        booleanComboBox.setPreferredSize(new Dimension(100, 10));
        valueTextField.setDoubleBuffered(true);
        valueTextField.setPreferredSize(new Dimension(100, 10));
        optionPanel.setLayout(new BoxLayout(optionPanel, 0));
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        optionPanel.setDoubleBuffered(true);
        optionPanel.add(detailButton);
        optionPanel.add(Box.createHorizontalStrut(5));
        try
        {
            classInfo = dos.getClass(ouid);
            if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
            {
                optionPanel.add(comparatorComboBox);
                optionPanel.add(Box.createHorizontalStrut(5));
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        optionPanel.add(fieldComboBox);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(valueTextField);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.add(searchButton);
        optionFrame = new MInternalFrame();
        optionFrame.add(optionPanel);
        scrollPane.setViewportView(resultTable.getTable());
        scrollPane.setDoubleBuffered(true);
        scrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        previousSearchButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Rewind16.gif")));
        previousSearchButton.setActionCommand("previousSearch");
        previousSearchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0159", "Previous Page", 3));
        previousSearchButton.addActionListener(this);
        previousSearchButton.setEnabled(false);
        nextSearchButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/FastForward16.gif")));
        nextSearchButton.setActionCommand("nextSearch");
        nextSearchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0160", "Next Page", 3));
        nextSearchButton.addActionListener(this);
        nextSearchButton.setEnabled(false);
        searchResultToolBar = new ExtToolBar("Search Result ToolBar");
        searchResultToolBar.add(previousSearchButton);
        searchResultToolBar.add(nextSearchButton);
        searchResultFrame = new MInternalFrame(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
        searchResultFrame.add(scrollPane);
        searchResultFrame.setToolBar(searchResultToolBar);
        selectButton.setActionCommand("Save");
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(5));
        toolPanel.add(closeButton);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setDoubleBuffered(true);
        rightPanel.setPreferredSize(new Dimension(500, 300));
        rightPanel.add(optionFrame, "North");
        rightPanel.add(searchResultFrame, "Center");
        mainSplitPane = new BorderlessSplitPane(1, leftSplitPane, rightPanel);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setDividerLocation(150);
        mainSplitPane.setOneTouchExpandable(true);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setDoubleBuffered(true);
        mainPanel.add(mainSplitPane, "Center");
        mainPanel.add(toolPanel, "South");
        setTitle(DynaMOAD.getMSRString("WRD_0077", "Link", 3));
        addWindowListener(this);
        getContentPane().add(mainPanel);
        FromLabel.setBounds(20, 20, 35, 21);
        FromField.setBounds(new Rectangle(55, 20, 112, 21));
        date_chooser.setBounds(170, 20, 20, 20);
        date_chooser.setToolTipText("Date");
        date_chooser.setActionCommand("date1");
        date_chooser.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
        date_chooser.setMargin(new Insets(0, 0, 0, 0));
        date_chooser.addActionListener(this);
        date_chooser.setDoubleBuffered(true);
        ToLabel.setBounds(20, 45, 35, 21);
        ToField.setBounds(new Rectangle(55, 45, 112, 21));
        date_chooser2.setBounds(170, 45, 20, 20);
        date_chooser2.setToolTipText("Date");
        date_chooser2.setActionCommand("date2");
        date_chooser2.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        date_chooser2.setMargin(new Insets(0, 0, 0, 0));
        date_chooser2.addActionListener(this);
        date_chooser2.setDoubleBuffered(true);
        ok.setBounds(55, 85, 60, 25);
        ok.setText(DynaMOAD.getMSRString("WRD_0019", "OK", 1));
        ok.setToolTipText(DynaMOAD.getMSRString("WRD_0019", "OK", 1));
        ok.setActionCommand("ok");
        ok.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        ok.setMargin(new Insets(0, 0, 0, 0));
        ok.addActionListener(this);
        ok.setDoubleBuffered(true);
        clear.setBounds(123, 85, 70, 25);
        clear.setText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clear.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clear.setActionCommand("clear");
        clear.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clear.setMargin(new Insets(0, 0, 0, 0));
        clear.addActionListener(this);
        clear.setDoubleBuffered(true);
    }

    public FieldLinkSelect getThis()
    {
        return this;
    }

    private void initComparator()
    {
        String division01 = "Last Released";
        String division02 = "Work In Progress";
        String division05 = "All Version";
        String division03 = "Created Date";
        String division04 = "Modified Date";
        String versionType[] = {
            division01, division02, division05, division03, division04
        };
        comparatorComboBox = new JComboBox(versionType);
    }

    private void initStatus()
    {
        statusArray = new String[12];
        statusArray[0] = "";
        statusArray[1] = DynaMOAD.getMSRString("AP1", "approving", 0);
        statusArray[2] = DynaMOAD.getMSRString("AP2", "approved", 0);
        statusArray[3] = DynaMOAD.getMSRString("CKI", "checked-in", 0);
        statusArray[4] = DynaMOAD.getMSRString("CKO", "checked-out", 0);
        statusArray[5] = DynaMOAD.getMSRString("CRT", "created", 0);
        statusArray[6] = DynaMOAD.getMSRString("OBS", "obsoleted", 0);
        statusArray[7] = DynaMOAD.getMSRString("REJ", "rejected", 0);
        statusArray[8] = DynaMOAD.getMSRString("RLS", "released", 0);
        statusArray[9] = DynaMOAD.getMSRString("RV1", "reviewing", 0);
        statusArray[10] = DynaMOAD.getMSRString("RV2", "reviewed", 0);
        statusArray[11] = DynaMOAD.getMSRString("WIP", "work in progress", 0);
        statusCodeMap = new HashMap();
        statusCodeMap.put("AP1", statusArray[1]);
        statusCodeMap.put("AP2", statusArray[2]);
        statusCodeMap.put("CKI", statusArray[3]);
        statusCodeMap.put("CKO", statusArray[4]);
        statusCodeMap.put("CRT", statusArray[5]);
        statusCodeMap.put("OBS", statusArray[6]);
        statusCodeMap.put("REJ", statusArray[7]);
        statusCodeMap.put("RLS", statusArray[8]);
        statusCodeMap.put("RV1", statusArray[9]);
        statusCodeMap.put("RV2", statusArray[10]);
        statusCodeMap.put("WIP", statusArray[11]);
        statusValueMap = new HashMap();
        statusValueMap.put(statusArray[1], "AP1");
        statusValueMap.put(statusArray[2], "AP2");
        statusValueMap.put(statusArray[3], "CKI");
        statusValueMap.put(statusArray[4], "CKO");
        statusValueMap.put(statusArray[5], "CRT");
        statusValueMap.put(statusArray[6], "OBS");
        statusValueMap.put(statusArray[7], "REJ");
        statusValueMap.put(statusArray[8], "RLS");
        statusValueMap.put(statusArray[9], "RV1");
        statusValueMap.put(statusArray[10], "RV2");
        statusValueMap.put(statusArray[11], "WIP");
        statusComboBox = new JComboBox(statusArray);
    }

    private void initBooleanComboBox()
    {
        String trueStr = "true";
        String falseStr = "false";
        String trueOrFalse[] = {
            "", trueStr, falseStr
        };
        booleanComboBox = new JComboBox(trueOrFalse);
    }

    public void setClassID(String classID)
    {
        this.classID = classID;
    }

    public void setClassChangable(boolean changable)
    {
        classChangable = changable;
    }

    public void setDefaultField(String field)
    {
        defaultField = field;
    }

    public void setDefaultOperator(String operator)
    {
        defaultOperator = operator;
    }

    public void setDefaultSearchValue(String value)
    {
        defaultSearchValue = value;
    }

    public void setSearchSQL(String sql)
    {
        searchSQL = sql;
    }

    public void setResultSetter(String setter)
    {
        resultSetter = setter;
    }

    public void setResultSetterObject(Object object)
    {
        resultSetterObject = object;
    }

    public void makeFieldComboBox(String classOuid)
    {
        try
        {
            Boolean isVisible = null;
            fieldComboBox.removeAllItems();
            orderOfCombo.clear();
            if(Utils.isNullString(classOuid))
                return;
            classInfo = dos.getClass(classOuid);
            classFieldList = UIBuilder.getDefaultFieldList(dos, classOuid);
            if(Utils.isNullArrayList(classFieldList))
                return;
            Utils.sort(classFieldList);
            fieldListForSearchCondition = new ArrayList();
            ArrayList searchConditionNds = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid);
            if(searchConditionNds == null)
            {
                fieldListForSearchCondition = (ArrayList)classFieldList.clone();
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                {
                    DOSChangeable tmpDOS = new DOSChangeable();
                    tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                    tmpDOS.put("name", "Version Type");
                    tmpDOS.put("ouid", "version.condition.type");
                    tmpDOS.put("index", new Integer(-985));
                    tmpDOS.put("type", new Byte((byte)13));
                    tmpDOS.put("is.visible", Boolean.TRUE);
                    fieldListForSearchCondition.add(2, tmpDOS);
                    tmpDOS = null;
                }
            } else
            if(searchConditionNds != null)
            {
                String fieldOuid = null;
                DOSChangeable fieldData = null;
                for(int k = 0; k < searchConditionNds.size(); k++)
                {
                    fieldOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid + "/" + (String)searchConditionNds.get(k));
                    if(fieldOuid.equals("version.condition.type"))
                    {
                        fieldData = new DOSChangeable();
                        fieldData.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                        fieldData.put("name", "Version Type");
                        fieldData.put("ouid", fieldOuid);
                        fieldData.put("index", new Integer(-985));
                        fieldData.put("type", new Byte((byte)13));
                        fieldData.put("is.visible", Boolean.TRUE);
                    } else
                    {
                        fieldData = dos.getField(fieldOuid);
                    }
                    fieldListForSearchCondition.add(fieldData);
                }

            }
            if(fieldListForSearchCondition != null)
            {
                for(int j = 0; j < fieldListForSearchCondition.size(); j++)
                {
                    DOSChangeable fieldData = (DOSChangeable)fieldListForSearchCondition.get(j);
                    if(fieldData != null)
                    {
                        isVisible = (Boolean)fieldData.get("is.visible");
                        if(Boolean.FALSE.equals(isVisible))
                            fieldData = null;
                        else
                        if(!((String)fieldData.get("ouid")).equals("version.condition.type"))
                        {
                            fieldComboBox.addItem(DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0));
                            orderOfCombo.add(fieldData.get("ouid"));
                        }
                    }
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void makeSearchResultTable(String classOuid)
    {
        try
        {
            int size = 0;
            String fieldOuid = null;
            String fieldCode = null;
            String fieldTitle = null;
            DOSChangeable fieldData = null;
            ArrayList fieldOuidList = null;
            ArrayList fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
            if(fieldColumn != null)
            {
                fieldOuidList = new ArrayList();
                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                size = fieldColumn.size();
                selectedRows = new int[size];
                for(int i = 0; i < size; i++)
                {
                    fieldOuid = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)fieldColumn.get(i));
                    fieldData = dos.getField(fieldOuid);
                    fieldCode = (String)fieldData.get("code");
                    fieldTitle = DynaMOAD.getMSRString(fieldCode, (String)fieldData.get("title"), 0);
                    fieldOuidList.add(fieldOuid);
                    columnName.add(fieldTitle);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            } else
            {
                ArrayList tableFieldList = makeDefaultFieldTable(classOuid);
                if(tableFieldList == null)
                    return;
                fieldOuidList = new ArrayList();
                searchData = new ArrayList();
                columnName = new ArrayList();
                columnWidth = new ArrayList();
                size = tableFieldList.size();
                selectedRows = new int[size];
                for(int i = 0; i < size; i++)
                {
                    fieldData = (DOSChangeable)tableFieldList.get(i);
                    fieldOuid = (String)fieldData.get("ouid");
                    fieldCode = (String)fieldData.get("code");
                    fieldTitle = DynaMOAD.getMSRString(fieldCode, (String)fieldData.get("title"), 0);
                    fieldOuidList.add(fieldOuid);
                    columnName.add(fieldTitle);
                    columnWidth.add(new Integer(80));
                    selectedRows[i] = i + 1;
                }

            }
            resultTable = new ResultTable(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), selectionMode, 79);
            resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            resultTable.setColumnSequence(selectedRows);
            resultTable.setIndexColumn(0);
            resultTable.getTable().setCursor(handCursor);
            resultTable.getTable().addMouseListener(this);
            resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            scrollPane.setViewportView(resultTable.getTable());
            scrollPane.getViewport().updateUI();
            fieldListForSearchResult = fieldOuidList;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
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
        resultTable.changeTableData();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public JTree getTree()
    {
        return tree;
    }

    public void setTopNode(TreeNodeObject topnode)
    {
        if(tree != null)
            tree = null;
        topNode = new DefaultMutableTreeNode(topnode);
        treeModel = new DefaultTreeModel(topNode);
        tree = new JTree(treeModel);
        createPopupMenu();
        DFDefaultTreeCellRenderer renderer = new DFDefaultTreeCellRenderer();
        tree.setCellRenderer(renderer);
        tree.getSelectionModel().setSelectionMode(1);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.addTreeSelectionListener(this);
        treeScrPane.setViewportView(tree);
    }

    public void setFullTreeExpand(DefaultMutableTreeNode node)
    {
        if(node == null)
        {
            return;
        } else
        {
            setAssociationClassExpand(node);
            tree.setSelectionRow(0);
            return;
        }
    }

    private void setAssociationClassExpand(DefaultMutableTreeNode node)
    {
        String ouid = ((TreeNodeObject)node.getUserObject()).getOuid();
        String classOuid = null;
        ArrayList superClassList = null;
        ArrayList assoList = null;
        ArrayList classList = null;
        Iterator assoKey = null;
        DOSChangeable dosAssociation = null;
        TreeNodeObject nodeData = null;
        DOSChangeable classInfoDC = null;
        DefaultMutableTreeNode childnode = null;
        boolean isLeaf = false;
        try
        {
            assoList = new ArrayList();
            classList = new ArrayList();
            String realClassOuid = null;
            if(instanceOuid == null)
                realClassOuid = (String)Utils.tokenizeMessage(ouid, '^').get(1);
            else
                realClassOuid = this.ouid;
            superClassList = dos.listAllSuperClassOuid(realClassOuid);
            if(superClassList == null)
                superClassList = new ArrayList();
            superClassList.add(realClassOuid);
            for(int i = 0; i < superClassList.size(); i++)
            {
                ArrayList tmpAssoList = dos.listAssociationOfClass((String)superClassList.get(i));
                if(tmpAssoList == null)
                {
                    tmpAssoList = null;
                } else
                {
                    for(int j = 0; j < tmpAssoList.size(); j++)
                        if(!assoList.contains(tmpAssoList.get(j)))
                            if(associationOuid != null)
                            {
                                if(((DOSChangeable)tmpAssoList.get(j)).get("ouid").equals(associationOuid))
                                    assoList.add(tmpAssoList.get(j));
                            } else
                            {
                                assoList.add(tmpAssoList.get(j));
                            }

                    tmpAssoList = null;
                }
            }

            byte assoType = 0;
            assoKey = assoList.iterator();
            while(assoKey.hasNext()) 
            {
                dosAssociation = (DOSChangeable)assoKey.next();
                assoType = Utils.getByte((Byte)dosAssociation.get("type"));
                if(instanceOuid == null && assoType != 1)
                {
                    classOuid = (String)dosAssociation.get("end2.ouid@class");
                    if(Utils.isNullString(classOuid) || classList.contains(classOuid))
                    {
                        dosAssociation.clear();
                        dosAssociation = null;
                        continue;
                    }
                    if(superClassList.contains((String)dosAssociation.get("end1.ouid@class")))
                        classList.add(classOuid);
                } else
                if(instanceOuid != null && assoType == 1)
                {
                    classOuid = (String)dosAssociation.get("end2.ouid@class");
                    if(!Utils.isNullString(classOuid) && !classList.contains(classOuid) && superClassList.contains((String)dosAssociation.get("end1.ouid@class")))
                        classList.add(classOuid);
                    classOuid = (String)dosAssociation.get("end1.ouid@class");
                    if(!Utils.isNullString(classOuid) && !classList.contains(classOuid) && superClassList.contains((String)dosAssociation.get("end2.ouid@class")))
                        classList.add(classOuid);
                }
                dosAssociation.clear();
                dosAssociation = null;
            }
            assoKey = null;
            assoList.clear();
            assoList = null;
            if(classList.size() == 0)
            {
                classList = null;
                return;
            }
            if(classList != null && !classList.isEmpty())
            {
                for(int i = 0; i < classList.size(); i++)
                {
                    String ouidStr = (String)classList.get(i);
                    classInfoDC = dos.getClass(ouidStr);
                    isLeaf = ((Boolean)classInfoDC.get("is.leaf")).booleanValue();
                    nodeData = new TreeNodeObject((String)classInfoDC.get("ouid"), (String)classInfoDC.get("title"), "Class");
                    childnode = new DefaultMutableTreeNode(nodeData);
                    setInsertNode(node, childnode);
                    setExpand(childnode, ouidStr);
                    if(!isLeaf && childnode.getChildCount() == 0)
                        childnode.removeFromParent();
                    tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                }

            }
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    private void setExpand(DefaultMutableTreeNode node, String classOuid)
    {
        String ouid = "";
        TreeNodeObject nodeData = null;
        ArrayList subClassListAL = null;
        DOSChangeable classInfoDC = null;
        DefaultMutableTreeNode childnode = null;
        boolean isLeaf = false;
        try
        {
            subClassListAL = dos.listSubClassInModel(modelOuid, classOuid);
            if(subClassListAL != null && !subClassListAL.isEmpty())
            {
                for(int i = 0; i < subClassListAL.size(); i++)
                {
                    classInfoDC = (DOSChangeable)subClassListAL.get(i);
                    isLeaf = ((Boolean)classInfoDC.get("is.leaf")).booleanValue();
                    ouid = (String)classInfoDC.get("ouid");
                    nodeData = new TreeNodeObject(ouid, (String)classInfoDC.get("title"), "Class");
                    childnode = new DefaultMutableTreeNode(nodeData);
                    setInsertNode(node, childnode);
                    setExpand(childnode, ouid);
                    if(!isLeaf && childnode.getChildCount() == 0)
                        childnode.removeFromParent();
                }

            } else
            {
                return;
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setFullTreeExpand2(DefaultMutableTreeNode node)
    {
        if(node == null)
        {
            return;
        } else
        {
            setExpand2(node);
            tree.setSelectionRow(0);
            return;
        }
    }

    private void setExpand2(DefaultMutableTreeNode node)
    {
        String ouid = "";
        TreeNodeObject nodeData = null;
        DOSChangeable rootClass = null;
        DefaultMutableTreeNode childnode = null;
        boolean isLeaf = false;
        try
        {
            rootClass = dos.getClass(this.ouid);
            if(rootClass != null)
            {
                isLeaf = ((Boolean)rootClass.get("is.leaf")).booleanValue();
                nodeData = new TreeNodeObject((String)rootClass.get("ouid"), (String)rootClass.get("title"), "Class");
                childnode = new DefaultMutableTreeNode(nodeData);
                setInsertNode(node, childnode);
                setExpand(childnode, this.ouid);
                if(!isLeaf && childnode.getChildCount() == 0)
                    childnode.removeFromParent();
                tree.scrollPathToVisible(new TreePath(childnode.getPath()));
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    public void createPopupMenu()
    {
        popupMenu = new JPopupMenu();
        informationMenu = new JMenuItem();
        addMenu = new JMenuItem();
        copyMenu = new JMenuItem();
        pasteMenu = new JMenuItem();
        deleteMenu = new JMenuItem();
        graphAddMenu = new JMenuItem();
        cutMenu = new JMenuItem();
        replaceMenu = new JMenuItem();
        insertIntoMenu = new JMenuItem();
        addMenu.setText(DynaMOAD.getMSRString("WRD_0076", "Add a new object...", 3));
        addMenu.setActionCommand("Add");
        addMenu.setIcon(new ImageIcon(getClass().getResource("/icons/add_att.gif")));
        addMenu.setAlignmentX(0.0F);
        addMenu.addActionListener(this);
        popupMenu.add(addMenu);
        MouseListener popupListener = new PopupListener();
        tree.addMouseListener(popupListener);
    }

    private void closeDialog(WindowEvent evt)
    {
        searchButton.removeActionListener(this);
        detailButton.removeActionListener(this);
        selectButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        removeMouseListener(this);
        setVisible(false);
        dispose();
    }

    public ArrayList makeDefaultFieldTable(String classOuid)
    {
        ArrayList tableFieldList = new ArrayList();
        ArrayList tmpList = null;
        try
        {
            tmpList = UIBuilder.getDefaultFieldList(dos, classOuid);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        DOSChangeable tempDos = null;
        for(int i = 0; i < tmpList.size(); i++)
        {
            tempDos = (DOSChangeable)tmpList.get(i);
            if(Utils.getBoolean((Boolean)tempDos.get("is.visible")))
                tableFieldList.add(tempDos);
        }

        return tableFieldList;
    }

    public void setConditionField(String str)
    {
        valueTextField.setText(str);
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = evt.getActionCommand();
        if(command == null)
            closeDialog(null);
        else
        if(command.equals("close"))
        {
            if(instanceOuid == null && (parentFr instanceof UIGeneration))
                ((UIGeneration)parentFr).refreshLinkTable(rootnodedata);
            else
            if(associationOuid != null && (parentFr instanceof UIGeneration))
                ((UIGeneration)parentFr).refreshAssociationTable(associationOuid);
            closeDialog(null);
        } else
        if(command.equals("Add"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(signStr == null)
            {
                UIGeneration uiGeneration = new UIGeneration(null, tmpdata.getOuid(), end1Ouid, null);
                uiGeneration = null;
            } else
            {
                UIGeneration uiGeneration = new UIGeneration(null, tmpdata.getOuid(), signStr, (Frame)parentFr);
                getThis().dispose();
                uiGeneration.toFront();
                uiGeneration = null;
            }
        } else
        if(command.equals("search"))
        {
            pageNumber = 1;
            searchButton_actionPerformed(evt);
        } else
        if(command.equals("previousSearch"))
        {
            pageNumber--;
            searchButton_actionPerformed(evt);
        } else
        if(command.equals("nextSearch"))
        {
            pageNumber++;
            searchButton_actionPerformed(evt);
        } else
        if(command.equals("detail"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(node.getLevel() != 0)
                if(DETAIL == 0)
                {
                    DETAIL = 1;
                    leftSplitPane.setDividerSize(5);
                    leftSplitPane.setDividerLocation(100);
                    leftSplitPane.setBottomComponent(searchConditionFrame);
                    mainSplitPane.setDividerLocation(300);
                    optionFrame.setVisible(false);
                } else
                {
                    DETAIL = 0;
                    leftSplitPane.setDividerSize(0);
                    leftSplitPane.setBottomComponent(null);
                    mainSplitPane.setDividerLocation(150);
                    optionFrame.setVisible(true);
                }
        } else
        if(command.equals("filterSave"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(filterDialog == null)
            {
                if(container == null)
                    for(container = getParent(); !(container instanceof Frame) && !(container instanceof Dialog); container = container.getParent());
                if(container instanceof Frame)
                    filterDialog = new FilterDialogForList((Frame)container, searchConditionPanel, (Component)evt.getSource(), true, tmpdata.getOuid());
                else
                if(container instanceof Dialog)
                    filterDialog = new FilterDialogForList((Dialog)container, searchConditionPanel, (Component)evt.getSource(), true, tmpdata.getOuid());
                filterDialog.setVisible(true);
                filterDialog = null;
            } else
            {
                filterDialog.toFront();
            }
        } else
        if(command.equals("clearCondition"))
            searchConditionPanel.clearAllFieldValue();
        else
        if(command.equals("closeFrame"))
        {
            DETAIL = 0;
            leftSplitPane.setDividerSize(0);
            leftSplitPane.setBottomComponent(null);
            mainSplitPane.setDividerLocation(150);
            optionFrame.setVisible(true);
        } else
        if(command.equals("Save"))
            selectButton_actionPerformed(evt);
        else
        if(command.equals("version type combo"))
        {
            String selectedItem = (String)comparatorComboBox.getSelectedItem();
            if(selectedItem.equals("Created Date") || selectedItem.equals("Modified Date"))
            {
                jd.getContentPane().setLayout(null);
                jd.setBackground(Color.blue);
                UIUtils.setLocationRelativeTo(jd, comparatorComboBox);
                jd.setResizable(false);
                jd.setSize(FRAME_SIZE);
                jd.getContentPane().add(FromLabel);
                jd.getContentPane().add(FromField);
                jd.getContentPane().add(date_chooser);
                jd.getContentPane().add(ToLabel);
                jd.getContentPane().add(ToField);
                jd.getContentPane().add(date_chooser2);
                jd.getContentPane().add(ok);
                jd.getContentPane().add(clear);
                jd.show();
            } else
            {
                jd.dispose();
            }
        } else
        if(command.equals("date"))
        {
            new DynaDateChooser(this);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                setConditionField(sdfYMD.format(date));
            else
                setConditionField("");
        } else
        if(command.equals("date1"))
        {
            new DynaDateChooser(this);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                FromField.setText(sdfYMD.format(date));
            else
                FromField.setText("");
        } else
        if(command.equals("date2"))
        {
            new DynaDateChooser(this);
            java.util.Date date = DynaDateChooser.showDialog((Component)evt.getSource(), null);
            if(date != null)
                ToField.setText(sdfYMD.format(date));
            else
                ToField.setText("");
        } else
        if(command.equals("ok"))
        {
            try
            {
                HashMap searchCondition = (HashMap)searchConditionData.get(ouid);
                if(searchCondition == null)
                    searchCondition = new HashMap();
                String selectedStr = (String)comparatorComboBox.getSelectedItem();
                if(selectedStr.equals("Created Date"))
                {
                    searchCondition.put("version.condition.type", "cdate");
                    searchCondition.put("version.condition.date.from", FromField.getText());
                    searchCondition.put("version.condition.date.to", ToField.getText());
                } else
                if(selectedStr.equals("Modified Date"))
                {
                    searchCondition.put("version.condition.type", "mdate");
                    searchCondition.put("version.condition.date.from", FromField.getText());
                    searchCondition.put("version.condition.date.to", ToField.getText());
                }
                searchConditionData.put(selectedClassOuid, searchCondition);
                if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                    searchCondition.put("version.condition.type", "wip");
                searchCondition.put("search.result.count", "1000");
                searchCondition.put("search.result.page", String.valueOf(pageNumber));
                ArrayList testList = dos.list(selectedClassOuid, fieldListForSearchResult, searchCondition);
                setResultData(testList);
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
            jd.dispose();
        } else
        if(command.equals("clear"))
        {
            ToField.setText("");
            FromField.setText("");
        } else
        if(command.equals("fieldCombo"))
            try
            {
                int row = fieldComboBox.getSelectedIndex();
                if(row > -1)
                {
                    DOSChangeable fieldInfo = new DOSChangeable();
                    if(orderOfCombo.size() > 0)
                    {
                        String comboOuid = (String)orderOfCombo.get(row);
                        fieldInfo = dos.getField(comboOuid);
                        statusComboBox.setSelectedIndex(-1);
                        booleanComboBox.setSelectedIndex(-1);
                        valueTextField.setText("");
                        optionPanel.removeAll();
                        optionPanel.add(detailButton);
                        optionPanel.add(Box.createHorizontalStrut(5));
                        if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
                        {
                            optionPanel.add(comparatorComboBox);
                            optionPanel.add(Box.createHorizontalStrut(5));
                        }
                        optionPanel.add(fieldComboBox);
                        optionPanel.add(Box.createHorizontalStrut(5));
                        if(fieldInfo != null && (((Byte)fieldInfo.get("type")).byteValue() == 22 || ((Byte)fieldInfo.get("type")).byteValue() == 21) && ("md$cdate".equals((String)fieldInfo.get("code")) || "md$mdate".equals((String)fieldInfo.get("code"))))
                        {
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(dateButton);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            optionPanel.updateUI();
                        } else
                        if("md$status".equals(fieldInfo.get("code")))
                        {
                            optionPanel.add(statusComboBox);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            valueTextField.setEditable(false);
                            optionPanel.updateUI();
                        } else
                        if(fieldInfo != null && ((Byte)fieldInfo.get("type")).byteValue() == 1)
                        {
                            optionPanel.add(booleanComboBox);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            valueTextField.setEditable(false);
                            optionPanel.updateUI();
                        } else
                        {
                            optionPanel.add(valueTextField);
                            optionPanel.add(Box.createHorizontalStrut(5));
                            optionPanel.add(searchButton);
                            valueTextField.setEditable(true);
                            optionPanel.updateUI();
                        }
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
            command.equals("statusComboBox");
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        closeDialog(windowEvent);
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

    public void mousePressed(MouseEvent mouseEvent)
    {
        int row = resultTable.getTable().getSelectedRow();
        String ouidRow = resultTable.getSelectedOuidRow(row);
        String selectedOuid = "";
        if(ouidRow != null)
            selectedOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
        else
            selectedOuid = (String)((ArrayList)searchData.get(row)).get(0);
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        int row = resultTable.getTable().getSelectedRow();
        String ouidRow = resultTable.getSelectedOuidRow(row);
        String selectedOuid = "";
        if(ouidRow != null)
            selectedOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
        else
            selectedOuid = (String)((ArrayList)searchData.get(row)).get(0);
        if(mouseEvent.getClickCount() == 2)
            selectButton_actionPerformed(null);
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void searchButton_actionPerformed(ActionEvent e)
    {
        try
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node.getLevel() != 0)
            {
                HashMap searchCondition = null;
                if(DETAIL == 1)
                {
                    searchCondition = searchConditionPanel.getCondition();
                    if(searchCondition == null)
                        return;
                    searchConditionData.put(selectedClassOuid, searchCondition);
                    searchCondition.put("search.result.page", String.valueOf(pageNumber));
                } else
                if(DETAIL == 0)
                {
                    searchCondition = new HashMap();
                    int row = fieldComboBox.getSelectedIndex();
                    String comboOuid = (String)orderOfCombo.get(row);
                    DOSChangeable fieldInfo = dos.getField(comboOuid);
                    if("md$status".equals(fieldInfo.get("code")))
                    {
                        if(!Utils.isNullString(statusComboBox.getSelectedItem().toString()))
                        {
                            String text = statusComboBox.getSelectedItem().toString();
                            searchCondition.put(comboOuid, statusValueMap.get(text));
                        }
                    } else
                    if(fieldInfo != null && fieldInfo.get("type").toString().equals((new Byte((byte)1)).toString()))
                        searchCondition.put(comboOuid, new Boolean(booleanComboBox.getSelectedItem().toString()));
                    else
                    if(!Utils.isNullString(valueTextField.getText()))
                        searchCondition.put(comboOuid, valueTextField.getText());
                    String selectedStr = (String)comparatorComboBox.getSelectedItem();
                    if(selectedStr.equals("Last Released"))
                        searchCondition.put("version.condition.type", "released");
                    else
                    if(selectedStr.equals("Work In Progress"))
                        searchCondition.put("version.condition.type", "wip");
                    else
                    if(selectedStr.equals("All Version"))
                        searchCondition.put("version.condition.type", "all");
                    else
                    if(selectedStr.equals("Created Date"))
                    {
                        searchCondition.put("version.condition.type", "cdate");
                        searchCondition.put("version.condition.date.from", FromField.getText());
                        searchCondition.put("version.condition.date.to", ToField.getText());
                    } else
                    if(selectedStr.equals("Modified Date"))
                    {
                        searchCondition.put("version.condition.type", "mdate");
                        searchCondition.put("version.condition.date.from", FromField.getText());
                        searchCondition.put("version.condition.date.to", ToField.getText());
                    }
                    if(Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                        searchCondition.put("version.condition.type", "wip");
                    if(!Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && !searchCondition.isEmpty() && searchCondition.containsKey("version.condition.type"))
                        searchCondition.remove("version.condition.type");
                    searchCondition.put("search.result.count", "1000");
                    searchCondition.put("search.result.page", String.valueOf(pageNumber));
                }
                ArrayList testList = dos.list(selectedClassOuid, fieldListForSearchResult, searchCondition);
                setResultData(testList);
                int cntPerPage = searchConditionPanel.getCountPerPage();
                if(testList == null)
                {
                    searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (0)");
                    previousSearchButton.setEnabled(false);
                    nextSearchButton.setEnabled(false);
                } else
                {
                    searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + " (" + ((pageNumber - 1) * cntPerPage + 1) + " ~ " + ((pageNumber - 1) * cntPerPage + testList.size()) + ")");
                    if(cntPerPage <= 0 || pageNumber == 1)
                        previousSearchButton.setEnabled(false);
                    else
                        previousSearchButton.setEnabled(true);
                    if(cntPerPage <= 0 || testList.size() < cntPerPage)
                        nextSearchButton.setEnabled(false);
                    else
                        nextSearchButton.setEnabled(true);
                }
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void selectButton_actionPerformed(ActionEvent e)
    {
        try
        {
            if(signStr != null)
            {
                int row = resultTable.getTable().getSelectedRow();
                String ouidRow = resultTable.getSelectedOuidRow(row);
                String selectOuid = "";
                String selectNumber = "";
                if(row > -1)
                {
                    if(ouidRow != null)
                    {
                        selectOuid = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(0);
                        selectNumber = (String)((ArrayList)searchData.get((new Integer(ouidRow)).intValue())).get(1);
                    } else
                    {
                        selectOuid = (String)((ArrayList)searchData.get(row)).get(0);
                        selectNumber = (String)((ArrayList)searchData.get(row)).get(1);
                    }
                    if(selectOuid != null)
                    {
                        returnValue = new String[2];
                        returnValue[0] = selectOuid;
                        returnValue[1] = selectNumber;
                    }
                } else
                {
                    returnValue = new String[2];
                    returnValue[0] = null;
                    returnValue[1] = "";
                }
                closeDialog(null);
            } else
            if(!end1Ouid.equals("") && !Utils.isNullArrayList(end2OuidList))
            {
                String clsOuidEnd1 = dos.getClassOuid((String)Utils.tokenizeMessage(end1Ouid, '^').get(0));
                String clsOuidEnd2 = dos.getClassOuid((String)end2OuidList.get(0));
                if(instanceOuid == null)
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
                        if(!superClassList1.contains((String)dosAssociation.get("end1.ouid@class")) || !superClassList2.contains((String)dosAssociation.get("end2.ouid@class")))
                            continue;
                        for(int j = 0; j < end2OuidList.size(); j++)
                            if(Utils.isNullString((String)dosAssociation.get("ouid@class")))
                            {
                                HashMap tempMap = new HashMap();
                                tempMap.put("end1", (String)Utils.tokenizeMessage(end1Ouid, '^').get(0));
                                tempMap.put("end2", end2OuidList.get(j));
                                Object returnObject = Utils.executeScriptFile(dos.getEventName(clsOuidEnd1, "link.before"), dss, tempMap);
                                if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                                {
                                    dos.link((String)Utils.tokenizeMessage(end1Ouid, '^').get(0), (String)end2OuidList.get(j));
                                    Utils.executeScriptFile(dos.getEventName(selectedClassOuid, "link.after"), dss);
                                }
                                tempMap.clear();
                                tempMap = null;
                            } else
                            {
                                DOSChangeable dosClass = dos.getClass((String)dosAssociation.get("ouid@class"));
                                ArrayList fieldGroupInfo = dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                                ArrayList endOidList = new ArrayList();
                                endOidList.add((String)Utils.tokenizeMessage(end1Ouid, '^').get(0));
                                endOidList.add(end2OuidList.get(j));
                                AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), null, 0, endOidList, (String)dosAssociation.get("ouid"));
                                AssoClsGeneration.setVisible(true);
                            }

                        break;
                    }

                    superClassList2.clear();
                    superClassList2 = null;
                } else
                if(associationOuid != null)
                {
                    DOSChangeable dosAssociation = dos.getAssociation(associationOuid);
                    if(dosAssociation == null)
                        return;
                    ArrayList superClassList1 = dos.listAllSuperClassOuid(clsOuidEnd1);
                    if(superClassList1 == null)
                        superClassList1 = new ArrayList();
                    superClassList1.add(0, clsOuidEnd1);
                    ArrayList superClassList2 = dos.listAllSuperClassOuid(clsOuidEnd2);
                    if(superClassList2 == null)
                        superClassList2 = new ArrayList();
                    superClassList2.add(0, clsOuidEnd2);
                    String assoEnd1 = (String)dosAssociation.get("end1.ouid@class");
                    String assoEnd2 = (String)dosAssociation.get("end2.ouid@class");
                    if(superClassList1.contains(assoEnd1) && superClassList2.contains(assoEnd2))
                    {
                        for(int i = 0; i < end2OuidList.size(); i++)
                            if(Utils.isNullString((String)dosAssociation.get("ouid@class")))
                            {
                                HashMap tempMap = new HashMap();
                                tempMap.put("end1", end1Ouid);
                                tempMap.put("end2", (String)end2OuidList.get(i));
                                System.out.println("[link before] 3");
                                Object returnObject = Utils.executeScriptFile(dos.getEventName(clsOuidEnd1, "link.before"), dss, tempMap);
                                if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                                {
                                    dos.link(end1Ouid, (String)end2OuidList.get(i));
                                    Utils.executeScriptFile(dos.getEventName(selectedClassOuid, "link.after"), dss);
                                }
                                tempMap.clear();
                                tempMap = null;
                            } else
                            {
                                DOSChangeable dosClass = dos.getClass((String)dosAssociation.get("ouid@class"));
                                ArrayList fieldGroupInfo = dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                                ArrayList endOidList = new ArrayList();
                                endOidList.add(end1Ouid);
                                endOidList.add((String)end2OuidList.get(i));
                                AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), null, 0, endOidList, (String)dosAssociation.get("ouid"));
                                AssoClsGeneration.setVisible(true);
                            }

                    } else
                    if(superClassList1.contains(assoEnd2) && superClassList2.contains(assoEnd1))
                    {
                        for(int i = 0; i < end2OuidList.size(); i++)
                            if(Utils.isNullString((String)dosAssociation.get("ouid@class")))
                            {
                                HashMap tempMap = new HashMap();
                                tempMap.put("end1", end1Ouid);
                                tempMap.put("end2", (String)end2OuidList.get(i));
                                System.out.println("[link before] 4");
                                Object returnObject = Utils.executeScriptFile(dos.getEventName(clsOuidEnd1, "link.before"), dss, tempMap);
                                if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                                {
                                    System.out.println("dos.link3");
                                    dos.link((String)end2OuidList.get(i), end1Ouid);
                                    Utils.executeScriptFile(dos.getEventName(selectedClassOuid, "link.after"), dss);
                                }
                                tempMap.clear();
                                tempMap = null;
                            } else
                            {
                                DOSChangeable dosClass = dos.getClass((String)dosAssociation.get("ouid@class"));
                                ArrayList fieldGroupInfo = dos.listFieldGroupInClass((String)dosClass.get("ouid"));
                                ArrayList endOidList = new ArrayList();
                                endOidList.add((String)end2OuidList.get(i));
                                endOidList.add(end1Ouid);
                                AssoClassUI AssoClsGeneration = new AssoClassUI(fieldGroupInfo, (String)dosClass.get("ouid"), null, 0, endOidList, (String)dosAssociation.get("ouid"));
                                AssoClsGeneration.setVisible(true);
                            }

                    }
                    dosAssociation.clear();
                    dosAssociation = null;
                }
                if(instanceOuid == null && (parentFr instanceof UIGeneration))
                    ((UIGeneration)parentFr).refreshLinkTable(rootnodedata);
                else
                if(associationOuid != null && (parentFr instanceof UIGeneration))
                    ((UIGeneration)parentFr).refreshAssociationTable(associationOuid);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode node = null;
        if(tree == null)
            return;
        node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
        selectedClassOuid = tmpdata.getOuid();
        if(node.getLevel() != 0)
        {
            makeFieldComboBox(selectedClassOuid);
            HashMap searchCondition = (HashMap)searchConditionData.get(selectedClassOuid);
            searchConditionPanel = new SearchConditionPanel(this, modelOuid, selectedClassOuid, fieldListForSearchCondition, searchCondition);
            conditionScrPane.setViewportView(searchConditionPanel);
            searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
            previousSearchButton.setEnabled(false);
            nextSearchButton.setEnabled(false);
            makeSearchResultTable(selectedClassOuid);
        } else
        {
            makeFieldComboBox(null);
            searchConditionPanel = null;
            conditionScrPane.setViewportView(null);
            searchResultFrame.setTitle(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
            previousSearchButton.setEnabled(false);
            nextSearchButton.setEnabled(false);
            makeSearchResultTable(null);
        }
    }

    private void ResultTable_valueChanged(ResultTable table, ListSelectionEvent e)
    {
        if(!e.getValueIsAdjusting())
        {
            ListSelectionModel listSelectionModel = table.getTable().getSelectionModel();
            if(listSelectionModel.getMinSelectionIndex() > -1 && listSelectionModel.getMaxSelectionIndex() > -1 && table.equals(resultTable))
            {
                end2OuidList.clear();
                int rows[] = table.getTable().getSelectedRows();
                for(int i = 0; i < rows.length; i++)
                {
                    String ouidRow = table.getSelectedOuidRow(rows[i]);
                    ArrayList selectedData = null;
                    if(ouidRow != null)
                    {
                        table.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        selectedData = (ArrayList)table.getList().get(Integer.parseInt(ouidRow));
                    } else
                    {
                        selectedData = (ArrayList)table.getList().get(rows[i]);
                    }
                    end2OuidList.add((String)selectedData.get(0));
                }

            }
        }
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public void setPageNumber(int n)
    {
        pageNumber = n;
    }

    public void setFilterDialogToNull()
    {
        filterDialog = null;
    }

    private boolean isdebugmode;
    private Cursor handCursor;
    private JSplitPane mainSplitPane;
    private JPanel mainPanel;
    private JPanel toolPanel;
    private JPanel rightPanel;
    private JButton searchButton;
    private JButton detailButton;
    private JButton dateButton;
    private JButton objectButton;
    private JPanel optionPanel;
    private JComboBox fieldComboBox;
    private JComboBox comparatorComboBox;
    private JComboBox statusComboBox;
    private JComboBox booleanComboBox;
    private JTextField valueTextField;
    private JButton selectButton;
    private JButton closeButton;
    private JScrollPane scrollPane;
    private JScrollPane conditionScrPane;
    private ResultTable resultTable;
    private String instanceOuid;
    private String associationOuid;
    private MInternalFrame searchResultFrame;
    private JToolBar searchResultToolBar;
    private JButton previousSearchButton;
    private JButton nextSearchButton;
    private JSplitPane leftSplitPane;
    private MInternalFrame treeFrame;
    private MInternalFrame searchConditionFrame;
    private JToolBar searchConditionToolBar;
    private MInternalFrame optionFrame;
    private FilterDialogForList filterDialog;
    private JButton searchButton2;
    private JButton filterButton;
    private JButton clearButton;
    private JButton closeFrameButton;
    private static Dimension FRAME_SIZE = new Dimension(220, 145);
    private String classID;
    private boolean classChangable;
    private String defaultField;
    private String defaultOperator;
    private String defaultSearchValue;
    private String searchSQL;
    private String resultSetter;
    private Object resultSetterObject;
    private NDS nds;
    private DOS dos;
    private DSS dss;
    private int selectedRows[];
    private ArrayList searchData;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private ArrayList fieldListForSearchCondition;
    private ArrayList classFieldList;
    private String ouid;
    private ArrayList fieldListForSearchResult;
    private String NDS_CODE;
    final int init_xcord = 10;
    final int init_ycord = 0;
    final int button_xcord = 15;
    final int titleWidth = 100;
    final int totalWidth = 260;
    final int fieldHeight = 20;
    final int offset = 3;
    final int buttonOffset = 10;
    final int condition_xsize = 270;
    final int condition_ysize = 254;
    final int buttonWidth = 76;
    final int buttonHeight = 0;
    final int title_width = 100;
    final int title_height = 20;
    final int smallButtonWidth = 76;
    private int DETAIL;
    private DOSChangeable searchConditionData;
    private String signStr;
    private Object parentFr;
    private ArrayList orderOfCombo;
    private DOSChangeable classInfo;
    private int selectionMode;
    private JPopupMenu popupMenu;
    private JMenuItem informationMenu;
    private JMenuItem addMenu;
    private JMenuItem copyMenu;
    private JMenuItem pasteMenu;
    private JMenuItem deleteMenu;
    private JMenuItem graphAddMenu;
    private JMenuItem cutMenu;
    private JMenuItem replaceMenu;
    private JMenuItem insertIntoMenu;
    private String modelOuid;
    private Object contentFrame;
    private JScrollPane treeScrPane;
    private JTree tree;
    private DefaultMutableTreeNode topNode;
    public DefaultTreeModel treeModel;
    public TreeNodeObject rootnodedata;
    private String selectedClassOuid;
    private SearchConditionPanel searchConditionPanel;
    private String end1Ouid;
    private ArrayList end2OuidList;
    private final byte DATATYPE_UTF = 13;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    final int REGISTER_MODE = 0;
    final int MODIFY_MODE = 1;
    private int pageNumber;
    private JDialog jd;
    private JLabel FromLabel;
    private JLabel ToLabel;
    private JTextField FromField;
    private JTextField ToField;
    private JButton date_chooser;
    private JButton date_chooser2;
    private JButton ok;
    private JButton clear;
    private boolean isNotAssociationCase;
    private String returnValue[];
    private Container container;
    private Component comp;
    String statusArray[];
    HashMap statusCodeMap;
    HashMap statusValueMap;








}