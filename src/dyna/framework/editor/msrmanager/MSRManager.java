// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MSRManager.java

package dyna.framework.editor.msrmanager;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookMode;
import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.plaf.plastic.theme.ExperienceBlue;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.framework.editor.msrmanager:
//            MSRManagerTreeCellRenderer, MSRManagerTreeModel, MSRInformation, MSRManagerTreeNode, 
//            MSRInsertDialog

public class MSRManager extends JFrame
    implements ActionListener, WindowListener, MouseListener, TreeSelectionListener, ChangeListener
{

    public MSRManager()
    {
        dssInformation = null;
        mainSplitPane = null;
        dssTreeScrPane = null;
        dssTree = null;
        storageTreeScrPane = null;
        storageTree = null;
        treeTabbedPane = null;
        groupTableScrPane = null;
        groupTable = null;
        userTableScrPane = null;
        userTable = null;
        groupButtonToolBar = null;
        groupSearchButton = null;
        groupInsertButton = null;
        groupDeleteButton = null;
        importButton = null;
        groupDataList = null;
        groupColumnName = null;
        groupColumnWidth = null;
        userDataList = null;
        userColumnName = null;
        userColumnWidth = null;
        dssId = "";
        userId = "";
        roleId = "";
        groupIdInUserTree = "";
        dosRootNode = null;
        nodeChildCount = new DOSChangeable();
        localeStr = null;
        makeTable();
        setTableData();
        initialize();
        setLocation(112, 84);
        setSize(800, 600);
        setVisible(true);
        addWindowListener(this);
    }

    public static void main(String args[])
    {
        try
        {
            LookUtils.setLookAndTheme(new PlasticXPLookAndFeel(), new ExperienceBlue());
            FontUtils.initFontDefaults(UIManager.getLookAndFeelDefaults(), new Font("dialog", 0, 12), new Font("dialog", 1, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 11), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12));
            Options.setGlobalFontSizeHints(FontSizeHints.MIXED);
            Options.setDefaultIconSize(new Dimension(18, 18));
            ClearLookManager.setPolicy("com.jgoodies.clearlook.DefaultClearLookPolicy");
            ClearLookManager.setMode(ClearLookMode.valueOf("ON"));
            dfw = new Client();
            aus = (AUS)dfw.getServiceInstance("DF30AUS1");
            dss = (DSS)dfw.getServiceInstance("DF30DSS1");
            nds = (NDS)dfw.getServiceInstance("DF30NDS1");
            msr = (MSR)dfw.getServiceInstance("DF30MSR1");
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        new MSRManager();
    }

    public void makeTable()
    {
        groupDataList = new ArrayList();
        groupColumnName = new ArrayList();
        groupColumnWidth = new ArrayList();
        groupColumnName.add("Locale");
        groupColumnName.add("Id");
        groupColumnName.add("String value");
        groupColumnWidth.add(new Integer(100));
        groupColumnWidth.add(new Integer(100));
        groupColumnWidth.add(new Integer(100));
        groupTable = new Table(groupDataList, groupColumnName, groupColumnWidth, 0, 100);
        groupTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        groupTable.getTable().addMouseListener(this);
    }

    public void setTableData()
    {
        try
        {
            groupDataList.clear();
            if(Utils.isNullString(localeStr))
                return;
            ArrayList stgList = msr.listStgrep(localeStr);
            for(int i = 0; i < stgList.size(); i++)
            {
                ArrayList tmpList = new ArrayList();
                DOSChangeable stgDos = (DOSChangeable)stgList.get(i);
                tmpList.add(stgDos.get("loc").toString());
                tmpList.add(stgDos.get("id").toString());
                tmpList.add(stgDos.get("stg").toString());
                groupDataList.add(tmpList.clone());
            }

            groupTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("MSR Manager - DynaMOAD");
        dosRootNode = new DOSChangeable();
        dosRootNode.put("ouid", "MSR");
        dosRootNode.put("name", "MSR");
        dosRootNode.put("description", "MSR");
        dosRootNode.put("object.type", "group");
        dssTree = new JTree();
        dssTree.setFont(getFont());
        dssTree.setCellRenderer(new MSRManagerTreeCellRenderer());
        dssTree.addTreeSelectionListener(this);
        dssTree.addMouseListener(this);
        dssTree.setModel(new MSRManagerTreeModel(dosRootNode));
        dssTreeScrPane = new JScrollPane();
        dssTreeScrPane.setViewportView(dssTree);
        dosRootNode = new DOSChangeable();
        dosRootNode.put("ouid", "Storage Id");
        dosRootNode.put("name", "Storage Id");
        dosRootNode.put("description", "Storage Id");
        dosRootNode.put("object.type", "user");
        storageTree = new JTree();
        storageTree.setFont(getFont());
        storageTree.setCellRenderer(new MSRManagerTreeCellRenderer());
        storageTree.addTreeSelectionListener(this);
        storageTree.addMouseListener(this);
        storageTree.setModel(new MSRManagerTreeModel(dosRootNode));
        storageTreeScrPane = UIFactory.createStrippedScrollPane(null);
        storageTreeScrPane.setViewportView(storageTree);
        treeTabbedPane = new JTabbedPane();
        treeTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        treeTabbedPane.setTabPlacement(3);
        treeTabbedPane.addTab("MSR List", new ImageIcon("icons/Group.gif"), dssTreeScrPane);
        treeTabbedPane.addChangeListener(this);
        groupTableScrPane = UIFactory.createStrippedScrollPane(null);
        groupTableScrPane.setViewportView(groupTable.getTable());
        groupTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        dssInformation = new MSRInformation(this, true);
        mainSplitPane = new JSplitPane();
        mainSplitPane.setLeftComponent(treeTabbedPane);
        mainSplitPane.setRightComponent(groupTableScrPane);
        mainSplitPane.setDividerLocation(250);
        mainSplitPane.setOneTouchExpandable(true);
        groupSearchButton = new JButton();
        groupSearchButton.setText("Search");
        groupSearchButton.setActionCommand("Search");
        groupSearchButton.addActionListener(this);
        groupInsertButton = new JButton();
        groupInsertButton.setText("Add");
        groupInsertButton.setActionCommand("Add");
        groupInsertButton.addActionListener(this);
        groupDeleteButton = new JButton();
        groupDeleteButton.setText("Remove");
        groupDeleteButton.setActionCommand("Remove");
        groupDeleteButton.addActionListener(this);
        importButton = new JButton();
        importButton.setText("Import");
        importButton.setActionCommand("Import");
        importButton.addActionListener(this);
        groupButtonToolBar = new JToolBar();
        groupButtonToolBar.add(Box.createHorizontalGlue());
        groupButtonToolBar.add(importButton);
        groupButtonToolBar.add(groupInsertButton);
        groupButtonToolBar.add(groupDeleteButton);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(groupButtonToolBar, "North");
        getContentPane().add(mainSplitPane, "Center");
        popupDSS = new JPopupMenu();
        menuInfoDSS = new JMenuItem();
        menuInfoDSS.setText("Information");
        menuInfoDSS.setIcon(new ImageIcon("icons/Information.gif"));
        menuInfoDSS.setActionCommand("Group Information");
        menuInfoDSS.addActionListener(this);
        menuAddDSS = new JMenuItem();
        menuAddDSS.setText("Add");
        menuAddDSS.setIcon(new ImageIcon("icons/Paste.gif"));
        menuAddDSS.setActionCommand("Add");
        menuAddDSS.addActionListener(this);
        menuDeleteDSS = new JMenuItem();
        menuDeleteDSS.setText("Delete");
        menuDeleteDSS.setIcon(new ImageIcon("icons/Delete.gif"));
        menuDeleteDSS.setActionCommand("DSS Unmount");
        menuDeleteDSS.addActionListener(this);
        menuRefreshDSS = new JMenuItem();
        menuRefreshDSS.setText("Refresh");
        menuRefreshDSS.setIcon(new ImageIcon("icons/Refresh.gif"));
        menuRefreshDSS.setActionCommand("Group Refresh");
        menuRefreshDSS.addActionListener(this);
        menuMountStorage = new JMenuItem();
        menuMountStorage.setText("Link User");
        menuMountStorage.setIcon(new ImageIcon("icons/Link.gif"));
        menuMountStorage.setActionCommand("Link User");
        menuMountStorage.addActionListener(this);
        popupDSS.add(menuAddDSS);
        popup = new JPopupMenu();
        menuInfo = new JMenuItem();
        menuInfo.setText("Information");
        menuInfo.setIcon(new ImageIcon("icons/Information.gif"));
        menuInfo.setActionCommand("User Information");
        menuInfo.addActionListener(this);
        menuAdd = new JMenuItem();
        menuAdd.setText("Add");
        menuAdd.setIcon(new ImageIcon("icons/Paste.gif"));
        menuAdd.setActionCommand("Add");
        menuAdd.addActionListener(this);
        menuDelete = new JMenuItem();
        menuDelete.setText("Delete");
        menuDelete.setIcon(new ImageIcon("icons/Delete.gif"));
        menuDelete.setActionCommand("Remove");
        menuDelete.addActionListener(this);
        menuLinkRole = new JMenuItem();
        menuLinkRole.setText("Link Role");
        menuLinkRole.setIcon(new ImageIcon("icons/Link.gif"));
        menuLinkRole.setActionCommand("Link Role");
        menuLinkRole.addActionListener(this);
        menuUnlinkGroup = new JMenuItem();
        menuUnlinkGroup.setText("Unlink Group");
        menuUnlinkGroup.setIcon(new ImageIcon("icons/Unlink.gif"));
        menuUnlinkGroup.setActionCommand("Unlink Group");
        menuUnlinkGroup.addActionListener(this);
        menuRefresh = new JMenuItem();
        menuRefresh.setText("Refresh");
        menuRefresh.setIcon(new ImageIcon("icons/Refresh.gif"));
        menuRefresh.setActionCommand("User Refresh");
        menuRefresh.addActionListener(this);
        popup.add(menuAdd);
        popup.add(menuDelete);
        popup.add(menuRefresh);
    }

    public void setUserResultData(ArrayList resultData)
    {
        userDataList.clear();
        ArrayList tmpList = new ArrayList();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                String tmpStr = (String)resultData.get(i);
                tmpList.add(tmpStr);
                tmpList.add("");
                userDataList.add(tmpList.clone());
                tmpList.clear();
            }

        }
        userTable.changeTableData();
    }

    public void addStorageTreeNode(String user)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)storageTree.getSelectionPath().getLastPathComponent();
        DOSChangeable tempObject = new DOSChangeable();
        tempObject.put("ouid", user);
        tempObject.put("name", user);
        tempObject.put("description", user);
        tempObject.put("object.type", "user");
        node.add(new MSRManagerTreeNode(tempObject));
        tempObject = null;
        storageTree.updateUI();
    }

    public void addDSSTreeNode(String dss)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)dssTree.getSelectionPath().getLastPathComponent();
        DOSChangeable tempObject = new DOSChangeable();
        tempObject.put("ouid", dss);
        tempObject.put("name", dss);
        tempObject.put("description", dss);
        tempObject.put("object.type", "group");
        node.add(new MSRManagerTreeNode(tempObject));
        tempObject = null;
        dssTree.updateUI();
    }

    public void initUserTree(ArrayList arraylist)
    {
    }

    public Table getTable()
    {
        return groupTable;
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Add"))
        {
            if(treeTabbedPane.getSelectedComponent().equals(dssTreeScrPane))
            {
                MSRInformation dssInformation = new MSRInformation(this, false);
                dssInformation.setLocaleField(localeStr);
                MSRInsertDialog groupFrame = new MSRInsertDialog(this, dssInformation, null);
                groupFrame.setVisible(true);
            }
        } else
        if(command.equals("Delete"))
        {
            if(!Utils.isNullString(userId))
                try
                {
                    boolean isDelete = aus.removeUser(userId);
                    if(isDelete)
                    {
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)storageTree.getLastSelectedPathComponent();
                        selectedNode.removeFromParent();
                        storageTree.updateUI();
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("DSS Unmount"))
        {
            if(!Utils.isNullString(dssId))
                try
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)dssTree.getSelectionPath().getLastPathComponent();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                    boolean isDelete = dss.unmountStorage(dssId);
                    if(isDelete)
                    {
                        parentNode.remove(node);
                        DOSChangeable dosParent = (DOSChangeable)parentNode.getUserObject();
                        dosParent.clear("is.populated");
                        dssTree.updateUI();
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("User Refresh"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)storageTree.getSelectionPath().getLastPathComponent();
            TreePath selectedPath = storageTree.getSelectionPath();
            storageTree.setSelectionInterval(-1, -1);
            storageTree.updateUI();
            storageTree.expandPath(selectedPath);
        } else
        if(!command.equals("Group Refresh"))
            if(command.equals("Unlink Group"))
                try
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)storageTree.getSelectionPath().getLastPathComponent();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
                    boolean unlink = aus.unlinkUserFromGroup(userId, groupIdInUserTree);
                    if(unlink)
                    {
                        parentNode.remove(node);
                        DOSChangeable dosParent = (DOSChangeable)parentNode.getUserObject();
                        dosParent.clear("is.populated");
                        storageTree.updateUI();
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(command.equals("Remove"))
                try
                {
                    int selRow = groupTable.getTable().getSelectedRow();
                    String ouidRow = groupTable.getSelectedOuidRow(selRow);
                    String locStr = null;
                    String idStr = null;
                    if(ouidRow != null)
                    {
                        groupTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                        locStr = ((ArrayList)groupDataList.get((new Integer(ouidRow)).intValue())).get(0).toString();
                        idStr = ((ArrayList)groupDataList.get((new Integer(ouidRow)).intValue())).get(1).toString();
                    } else
                    {
                        locStr = ((ArrayList)groupDataList.get(selRow)).get(0).toString();
                        idStr = ((ArrayList)groupDataList.get(selRow)).get(1).toString();
                    }
                    msr.removeStgrep(locStr, idStr);
                    setTableData();
                    groupTable.changeTableData();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(command.equals("Import"))
                try
                {
                    JFileChooser fileChooser = new JFileChooser();
                    String filePath = "";
                    String fileName = "";
                    fileChooser.addChoosableFileFilter(new SoftWareFilter("xml"));
                    int returnVal = fileChooser.showDialog(this, "Open");
                    if(returnVal == 0)
                    {
                        File file = fileChooser.getSelectedFile();
                        if(file == null || !file.isFile())
                            return;
                        filePath = file.getAbsolutePath();
                        fileName = filePath.substring(0, filePath.indexOf('.'));
                    }
                    msr.importStgrep(fileName);
                    setTableData();
                    groupTable.changeTableData();
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        dispose();
        System.exit(0);
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
        dispose();
        System.exit(0);
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void mousePressed(MouseEvent evt)
    {
        if(evt.getSource().equals(storageTree))
        {
            if(SwingUtilities.isRightMouseButton(evt))
                storageTree.setSelectionPath(storageTree.getClosestPathForLocation(evt.getX(), evt.getY()));
        } else
        if(evt.getSource().equals(dssTree) && SwingUtilities.isRightMouseButton(evt))
            dssTree.setSelectionPath(dssTree.getClosestPathForLocation(evt.getX(), evt.getY()));
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent evt)
    {
        if(evt.getSource().equals(storageTree))
        {
            if(evt.getClickCount() == 1 && evt.isPopupTrigger())
            {
                TreePath treePath = storageTree.getSelectionPath();
                MSRManagerTreeNode node = (MSRManagerTreeNode)treePath.getLastPathComponent();
                DOSChangeable dosObject = (DOSChangeable)node.getUserObject();
                if(dosObject == null)
                    return;
                String objectType = (String)dosObject.get("object.type");
                Dimension frameSize = getSize();
                if(node.getLevel() == 0)
                {
                    popup.add(menuAdd);
                    popup.remove(menuDelete);
                } else
                if(node.getLevel() == 1)
                {
                    popup.remove(menuAdd);
                    popup.add(menuDelete);
                } else
                if(node.getLevel() == 2)
                {
                    popup.remove(menuAdd);
                    popup.remove(menuDelete);
                }
                Dimension popupSize = popup.getSize();
                int dividerLocation = mainSplitPane.getDividerLocation();
                Point point = SwingUtilities.convertPoint(evt.getComponent(), evt.getX(), evt.getY(), this);
                int x = evt.getX();
                int y = evt.getY();
                if(popupSize.width + point.x >= dividerLocation)
                    x -= popupSize.width;
                if(popupSize.height + point.y >= frameSize.height)
                    y -= popupSize.height;
                popup.show(evt.getComponent(), x, y);
            }
        } else
        if(evt.getSource().equals(dssTree) && evt.getClickCount() == 1 && evt.isPopupTrigger())
        {
            TreePath treePath = dssTree.getSelectionPath();
            MSRManagerTreeNode node = (MSRManagerTreeNode)treePath.getLastPathComponent();
            DOSChangeable dosObject = (DOSChangeable)node.getUserObject();
            if(dosObject == null)
                return;
            String objectType = (String)dosObject.get("object.type");
            dssId = (String)dosObject.get("name");
            Dimension frameSize = getSize();
            if(node.getLevel() == 0)
            {
                popupDSS.add(menuAddDSS);
                popupDSS.remove(menuDeleteDSS);
                popupDSS.remove(menuRefreshDSS);
            } else
            {
                popupDSS.remove(menuAddDSS);
                popupDSS.add(menuDeleteDSS);
                popupDSS.add(menuRefreshDSS);
            }
            Dimension popupSize = popupDSS.getSize();
            int dividerLocation = mainSplitPane.getDividerLocation();
            Point point = SwingUtilities.convertPoint(evt.getComponent(), evt.getX(), evt.getY(), this);
            int x = evt.getX();
            int y = evt.getY();
            if(popupSize.width + point.x >= dividerLocation)
                x -= popupSize.width;
            if(popupSize.height + point.y >= frameSize.height)
                y -= popupSize.height;
            popupDSS.show(evt.getComponent(), x, y);
        }
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        Object source = mouseEvent.getSource();
        if(source.equals(groupTable.getTable()) && mouseEvent.getClickCount() == 2)
        {
            int selRow = groupTable.getTable().getSelectedRow();
            String ouidRow = groupTable.getSelectedOuidRow(selRow);
            HashMap groupInfo = new HashMap();
            if(ouidRow != null)
            {
                groupTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                groupInfo.put("loc", ((ArrayList)groupDataList.get((new Integer(ouidRow)).intValue())).get(0).toString());
                groupInfo.put("id", ((ArrayList)groupDataList.get((new Integer(ouidRow)).intValue())).get(1).toString());
                groupInfo.put("stg", ((ArrayList)groupDataList.get((new Integer(ouidRow)).intValue())).get(2).toString());
            } else
            {
                groupInfo.put("loc", ((ArrayList)groupDataList.get(selRow)).get(0).toString());
                groupInfo.put("id", ((ArrayList)groupDataList.get(selRow)).get(1).toString());
                groupInfo.put("stg", ((ArrayList)groupDataList.get(selRow)).get(2).toString());
            }
            MSRInformation dssInformation = new MSRInformation(this, true);
            dssInformation.setData(groupInfo);
            MSRInsertDialog groupFrame = new MSRInsertDialog(this, dssInformation, null);
            groupFrame.setVisible(true);
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void valueChanged(TreeSelectionEvent tse)
    {
        if(tse.getSource().equals(dssTree))
        {
            try
            {
                TreePath path = tse.getPath();
                if(path == null)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                DOSChangeable dosObject = (DOSChangeable)node.getUserObject();
                if(dosObject == null)
                    return;
                String objectType = (String)dosObject.get("object.type");
                dssId = (String)dosObject.get("name");
                objectType.equals("model");
                if(node.getLevel() == 1 && !Utils.isNullString(dssId))
                {
                    localeStr = ((DOSChangeable)node.getUserObject()).get("name").toString();
                    ArrayList stgList = msr.listStgrep(localeStr);
                    groupDataList.clear();
                    for(int i = 0; i < stgList.size(); i++)
                    {
                        ArrayList tmpList = new ArrayList();
                        DOSChangeable stgDos = (DOSChangeable)stgList.get(i);
                        tmpList.add(stgDos.get("loc").toString());
                        tmpList.add(stgDos.get("id").toString());
                        tmpList.add(stgDos.get("stg").toString());
                        groupDataList.add(tmpList.clone());
                    }

                    groupTable.changeTableData();
                }
                if(dosObject.get("is.populated") != null)
                    return;
                ArrayList arrayList = null;
                Iterator listKey = null;
                DOSChangeable tempObject = null;
                MSRManagerTreeNode treeNode = null;
                String tempString = null;
                if(objectType.equals("group"))
                {
                    if(node.getLevel() == 0)
                    {
                        arrayList = new ArrayList();
                        arrayList.add("ko");
                        arrayList.add("en");
                        arrayList.add("jp");
                        arrayList.add("zh");
                        if(Utils.isNullArrayList(arrayList))
                            return;
                        dosObject.put("is.populated", Utils.True);
                        for(listKey = arrayList.iterator(); listKey.hasNext();)
                        {
                            tempObject = new DOSChangeable();
                            String keyVal = (String)listKey.next();
                            tempObject.put("ouid", keyVal);
                            tempObject.put("name", keyVal);
                            tempObject.put("description", keyVal);
                            tempObject.put("object.type", "group");
                            treeNode = new MSRManagerTreeNode(tempObject);
                            node.add(treeNode);
                            treeNode = null;
                            tempObject = null;
                        }

                    }
                    listKey = null;
                    arrayList = null;
                }
                if(node.getLevel() == 0 || node.getLevel() == 1)
                    dssTree.fireTreeExpanded(path);
                else
                    dssTree.fireTreeCollapsed(path);
                node = null;
                dosObject = null;
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
            dssTreeScrPane.updateUI();
        }
    }

    public void stateChanged(ChangeEvent changeEvent)
    {
        if(treeTabbedPane.getSelectedIndex() == 0)
        {
            mainSplitPane.setRightComponent(dssInformation);
            mainSplitPane.setDividerLocation(250);
        }
    }

    public static Client dfw = null;
    public static AUS aus = null;
    public static DSS dss = null;
    public static NDS nds = null;
    public static MSR msr = null;
    private MSRInformation dssInformation;
    private JSplitPane mainSplitPane;
    private JScrollPane dssTreeScrPane;
    private JTree dssTree;
    private JScrollPane storageTreeScrPane;
    private JTree storageTree;
    private JTabbedPane treeTabbedPane;
    private JScrollPane groupTableScrPane;
    private Table groupTable;
    private JScrollPane userTableScrPane;
    private Table userTable;
    private JToolBar groupButtonToolBar;
    private JButton groupSearchButton;
    private JButton groupInsertButton;
    private JButton groupDeleteButton;
    private JButton importButton;
    private JPopupMenu popupDSS;
    private JMenuItem menuInfoDSS;
    private JMenuItem menuAddDSS;
    private JMenuItem menuDeleteDSS;
    private JMenuItem menuRefreshDSS;
    private JMenuItem menuMountStorage;
    private JPopupMenu popup;
    private JMenuItem menuInfo;
    private JMenuItem menuAdd;
    private JMenuItem menuDelete;
    private JMenuItem menuRefresh;
    private JMenuItem menuLinkRole;
    private JMenuItem menuUnlinkGroup;
    private ArrayList groupDataList;
    private ArrayList groupColumnName;
    private ArrayList groupColumnWidth;
    private ArrayList userDataList;
    private ArrayList userColumnName;
    private ArrayList userColumnWidth;
    private String dssId;
    public String userId;
    private String roleId;
    private String groupIdInUserTree;
    private DOSChangeable dosRootNode;
    private DOSChangeable nodeChildCount;
    private String localeStr;

}
