// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSSManager.java

package dyna.framework.editor.dssmanager;

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
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.framework.editor.dssmanager:
//            DSSManagerTreeCellRenderer, DSSManagerTreeModel, StorageInformation, DSSInformation, 
//            DSSManagerTreeNode, DSSInsertDialog

public class DSSManager extends JFrame
    implements ActionListener, WindowListener, MouseListener, TreeSelectionListener, ChangeListener
{

    public DSSManager()
    {
        storageInformation = null;
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
        setFont(new Font("dialog", 0, 11));
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
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        new DSSManager();
    }

    public void makeTable()
    {
        groupDataList = new ArrayList();
        groupColumnName = new ArrayList();
        groupColumnWidth = new ArrayList();
        groupColumnName.add("Name");
        groupColumnName.add("Description");
        groupColumnWidth.add(new Integer(100));
        groupColumnWidth.add(new Integer(100));
        groupTable = new Table(groupDataList, groupColumnName, groupColumnWidth, 0, 100);
        groupTable.setColumnSequence(new int[] {
            0, 1
        });
        userDataList = new ArrayList();
        userColumnName = new ArrayList();
        userColumnWidth = new ArrayList();
        userColumnName.add("Name");
        userColumnName.add("Description");
        userColumnWidth.add(new Integer(100));
        userColumnWidth.add(new Integer(100));
        userTable = new Table(userDataList, userColumnName, userColumnWidth, 0, 100);
        userTable.setColumnSequence(new int[] {
            0, 1
        });
        userTable.getTable().addMouseListener(this);
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("DSS Manager - DynaMOAD");
        dosRootNode = new DOSChangeable();
        dosRootNode.put("ouid", "DSS");
        dosRootNode.put("name", "DSS");
        dosRootNode.put("description", "DSS");
        dosRootNode.put("object.type", "group");
        dssTree = new JTree();
        dssTree.setFont(getFont());
        dssTree.setCellRenderer(new DSSManagerTreeCellRenderer());
        dssTree.addTreeSelectionListener(this);
        dssTree.addMouseListener(this);
        dssTree.setModel(new DSSManagerTreeModel(dosRootNode));
        dssTreeScrPane = UIFactory.createStrippedScrollPane(null);
        dssTreeScrPane.setViewportView(dssTree);
        dosRootNode = new DOSChangeable();
        dosRootNode.put("ouid", "Storage Id");
        dosRootNode.put("name", "Storage Id");
        dosRootNode.put("description", "Storage Id");
        dosRootNode.put("object.type", "user");
        storageTree = new JTree();
        storageTree.setFont(getFont());
        storageTree.setCellRenderer(new DSSManagerTreeCellRenderer());
        storageTree.addTreeSelectionListener(this);
        storageTree.addMouseListener(this);
        storageTree.setModel(new DSSManagerTreeModel(dosRootNode));
        storageTreeScrPane = UIFactory.createStrippedScrollPane(null);
        storageTreeScrPane.setViewportView(storageTree);
        treeTabbedPane = new JTabbedPane();
        treeTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        treeTabbedPane.setTabPlacement(3);
        treeTabbedPane.addTab("DSS List", new ImageIcon("icons/Group.gif"), dssTreeScrPane);
        treeTabbedPane.addTab("Storage Id List", new ImageIcon("icons/User.gif"), storageTreeScrPane);
        treeTabbedPane.addChangeListener(this);
        storageInformation = new StorageInformation(this, true);
        dssInformation = new DSSInformation(this, true);
        mainSplitPane = new JSplitPane();
        mainSplitPane.setLeftComponent(treeTabbedPane);
        mainSplitPane.setRightComponent(dssInformation);
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
        groupButtonToolBar = new JToolBar();
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
        popupDSS.add(menuDeleteDSS);
        popupDSS.add(menuRefreshDSS);
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
        node.add(new DSSManagerTreeNode(tempObject));
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
        node.add(new DSSManagerTreeNode(tempObject));
        tempObject = null;
        dssTree.updateUI();
    }

    public void initUserTree(ArrayList arraylist)
    {
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Add"))
        {
            if(treeTabbedPane.getSelectedComponent().equals(dssTreeScrPane))
            {
                DSSInformation dssInformation = new DSSInformation(this, false);
                DSSInsertDialog groupFrame = new DSSInsertDialog(this, dssInformation, null);
                groupFrame.setVisible(true);
            } else
            if(treeTabbedPane.getSelectedComponent().equals(storageTreeScrPane))
            {
                StorageInformation storageInformation = new StorageInformation(this, false);
                DSSInsertDialog groupFrame = new DSSInsertDialog(this, storageInformation, null);
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
        if(command.equals("User Information"))
        {
            if(!Utils.isNullString(userId))
                try
                {
                    HashMap groupInfo = dss.getStorage(dssId);
                    groupInfo.put("storage.id", dssId);
                    this.storageInformation.setData(groupInfo);
                    this.storageInformation.setVisible(true);
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
        if(command.equals("Group Information"))
        {
            if(!Utils.isNullString(dssId))
                try
                {
                    HashMap groupInfo = dss.getStorage(dssId);
                    groupInfo.put("storage.id", dssId);
                    this.storageInformation.setData(groupInfo);
                    this.storageInformation.setVisible(true);
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
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
                    if(!treeTabbedPane.getSelectedComponent().equals(dssTreeScrPane) && treeTabbedPane.getSelectedComponent().equals(storageTreeScrPane) && !Utils.isNullString(userId))
                    {
                        boolean isDelete = dss.removeStorage(userId);
                        if(isDelete)
                            JOptionPane.showMessageDialog(this, "\uC0AD\uC81C\uB418\uC5C8\uC2B5\uB2C8\uB2E4", "Information", 1);
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)storageTree.getLastSelectedPathComponent();
                        selectedNode.removeFromParent();
                        storageTree.updateUI();
                    }
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
                DSSManagerTreeNode node = (DSSManagerTreeNode)treePath.getLastPathComponent();
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
            DSSManagerTreeNode node = (DSSManagerTreeNode)treePath.getLastPathComponent();
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

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void valueChanged(TreeSelectionEvent tse)
    {
        if(tse.getSource().equals(storageTree))
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
                if(node.getLevel() == 1)
                {
                    userId = (String)dosObject.get("name");
                    if(!Utils.isNullString(userId))
                        try
                        {
                            HashMap groupInfo = dss.getStorage(userId);
                            groupInfo.put("storage.id", userId);
                            storageInformation.setData(groupInfo);
                            storageInformation.setVisible(true);
                        }
                        catch(IIPRequestException ie)
                        {
                            System.err.println(ie);
                        }
                }
                if(dosObject.get("is.populated") != null)
                    return;
                ArrayList arrayList = null;
                Iterator listKey = null;
                DOSChangeable tempObject = null;
                DSSManagerTreeNode treeNode = null;
                String tempString = null;
                if(objectType.equals("user"))
                    if(node.getLevel() == 0)
                    {
                        arrayList = dss.listStorageId();
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
                            tempObject.put("object.type", "user");
                            treeNode = new DSSManagerTreeNode(tempObject);
                            node.add(treeNode);
                            treeNode = null;
                            tempObject = null;
                        }

                        listKey = null;
                        arrayList = null;
                    } else
                    if(node.getLevel() == 1)
                    {
                        arrayList = aus.listGroupOfUser(userId);
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
                            tempObject.put("object.type", "process");
                            treeNode = new DSSManagerTreeNode(tempObject);
                            node.add(treeNode);
                            treeNode = null;
                            tempObject = null;
                        }

                        arrayList = null;
                    }
                if(node.getLevel() == 0 || node.getLevel() == 1)
                    storageTree.fireTreeExpanded(path);
                else
                    storageTree.fireTreeCollapsed(path);
                node = null;
                dosObject = null;
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
            storageTreeScrPane.updateUI();
        } else
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
                    HashMap dssInfo = new HashMap();
                    dssInfo.put("id", dssId);
                    dssInfo.put("storage.id", dss.getStorageIdOfMount((String)((DOSChangeable)node.getUserObject()).get("name")));
                    dssInformation.setData(dssInfo);
                    dssInformation.setVisible(true);
                }
                if(dosObject.get("is.populated") != null)
                    return;
                ArrayList arrayList = null;
                Iterator listKey = null;
                DOSChangeable tempObject = null;
                DSSManagerTreeNode treeNode = null;
                String tempString = null;
                if(objectType.equals("group"))
                {
                    if(node.getLevel() == 0)
                    {
                        arrayList = dss.listMount();
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
                            treeNode = new DSSManagerTreeNode(tempObject);
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
        } else
        if(treeTabbedPane.getSelectedIndex() == 1)
        {
            mainSplitPane.setRightComponent(storageInformation);
            mainSplitPane.setDividerLocation(250);
        }
    }

    public static Client dfw = null;
    public static AUS aus = null;
    public static DSS dss = null;
    public static NDS nds = null;
    private StorageInformation storageInformation;
    private DSSInformation dssInformation;
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

}
