// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SearchCondition4CADIntegration.java

package dyna.framework.client;

import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.PopupButton;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.ACL;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.NDS;
import dyna.framework.service.WFM;
import dyna.framework.service.WKS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaDateChooser;
import dyna.uic.MInternalFrame;
import dyna.uic.TreeNodeObject;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Referenced classes of package dyna.framework.client:
//            CodeChooserUser, DynaMOAD, ClientUtil, LogIn, 
//            DFDefaultTreeCellRenderer, UIManagement, Search4CADIntegration, UIBuilder, 
//            UIGeneration, FilterDialogForList, AuthoritySetting, ProcessAuthoritySetting, 
//            SmallSearchDialog, CodeSelectDialog, CheckOut, CheckIn, 
//            CreateProcess, SearchResult4CADIntegration

public class SearchCondition4CADIntegration extends JPanel
    implements ActionListener, ChangeListener, TreeSelectionListener, CodeChooserUser
{
    class PopupListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(e.getSource().equals(SearchCondition4CADIntegration.this.tree) || e.getSource().equals(hcodeTree))
            {
                ((Search4CADIntegration)contentFrame).setEnabledPreviousSearchButton(false);
                ((Search4CADIntegration)contentFrame).setEnabledNextSearchButton(false);
                String classOuid = null;
                JTree tree = getCurrentTree();
                int mode = 0;
                if(tree.equals(SearchCondition4CADIntegration.this.tree))
                    mode = 1;
                else
                if(tree.equals(hcodeTree))
                    mode = 2;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                if(node == null)
                    return;
                TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
                if(mode == 1)
                    classOuid = tmpdata.getOuid();
                else
                if(mode == 2)
                {
                    LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                    classOuid = (String)tempList.get(1);
                    tempList.clear();
                    tempList = null;
                }
                try
                {
                    classDC = dos.getClass(classOuid);
                    if(classDC != null && classDC.getValueMap().containsKey("is.versionable") && Utils.getBoolean((Boolean)classDC.getValueMap().get("is.versionable")))
                        versionableClass = true;
                    else
                        versionableClass = false;
                    classOuidForFilter = classOuid;
                    if(node.getLevel() != 0)
                    {
                        advancedPanelInit();
                        ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTable(classOuid);
                    } else
                    {
                        buttonPanel = new JPanel();
                        buttonPanel.setLayout(null);
                        buttonPanel.setBackground(UIManagement.panelBackGround);
                        buttonPanel.setPreferredSize(new Dimension(270, 45));
                        searchConditionPanel = new JPanel();
                        searchConditionPanel.setLayout(null);
                        searchConditionPanel.setBackground(UIManagement.panelBackGround);
                        searchConditionPanel.setMinimumSize(new Dimension(270, 254));
                        searchConditionPanel.setMaximumSize(new Dimension(270, 254));
                        searchConditionPanel.setPreferredSize(new Dimension(270, 254));
                        searchConditionScrPane.setViewportView(searchConditionPanel);
                        searchConditionScrPane.setColumnHeaderView(buttonPanel);
                        if(((Search4CADIntegration)contentFrame).searchResult4CADIntegration.tableScrPane != null)
                        {
                            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.tableScrPane.removeAll();
                            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.updateUI();
                        }
                    }
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
            } else
            if(e.getSource().equals(workSpaceTree))
            {
                if(workSpaceTree == null)
                    return;
                if(e.getClickCount() >= 2)
                {
                    this.node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                    if(this.node != null)
                    {
                        TreeNodeObject object = (TreeNodeObject)this.node.getUserObject();
                        if("LISTFILTER".equals(object.getDescription()))
                            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTableForAdvancedFilter(object.getOuid());
                        else
                            information_actionPerformed(null);
                    }
                } else
                {
                    this.node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                    selectedNode = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                    if(this.node != null)
                    {
                        TreeNodeObject object = (TreeNodeObject)this.node.getUserObject();
                        showPopupWorkSpace(e);
                        if(object.getOuid().equals("Recent") || object.getOuid().equals("Sent") || object.getOuid().equals("Completed"))
                            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTableForWorkSpace(object.getOuid());
                        else
                        if(object.getOuid().equals("Inbox"))
                        {
                            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTableForWorkSpace(object.getOuid());
                            try
                            {
                                ArrayList inboxList = DynaMOAD.wfm.listInboxProcessByUser(LogIn.userID);
                                object.setName(DynaMOAD.getMSRString("WRD_0052", "Inbox", 3) + " (" + Integer.toString(inboxList.size()) + ")");
                                this.node.setUserObject(object);
                            }
                            catch(IIPRequestException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                        object = null;
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            if(!e.getSource().equals(tree) && !e.getSource().equals(hcodeTree))
            {
                if(node == null)
                    return;
                TreeNodeObject object = (TreeNodeObject)node.getUserObject();
                showPopupWorkSpace(e);
                if(object.getOuid().equals("Recent") || object.getOuid().equals("Sent") || object.getOuid().equals("Inbox") || object.getOuid().equals("Completed"))
                    ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTableForWorkSpace(object.getOuid());
                object = null;
            }
        }

        private void showPopupWorkSpace(MouseEvent e)
        {
            boolean willShow = false;
            if(e.getClickCount() == 1 && e.isPopupTrigger())
            {
                this.node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                selectedNode = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                if(this.node != null)
                {
                    TreeNodeObject object = (TreeNodeObject)this.node.getUserObject();
                    if(object.getOuid().equals("Recent") || object.getOuid().equals("Sent") || object.getOuid().equals("Inbox") || object.getOuid().equals("Completed"))
                    {
                        popupMenuForWorkSpace.removeAll();
                        popupMenuForWorkSpace.add(deleteMenuForWorkSpace);
                        willShow = true;
                    } else
                    if(object.getDescription() != null && object.getDescription().equals("FOLDER"))
                    {
                        String ouid = object.getOuid();
                        if(ouid.indexOf("/PRIVATE/") > 0 && ouid.indexOf("/FOLDER", ouid.indexOf("/PRIVATE/")) > 0)
                        {
                            popupMenuForWorkSpace.removeAll();
                            popupMenuForWorkSpace.add(addFolderMenuItem);
                            popupMenuForWorkSpace.add(new JSeparator());
                            popupMenuForWorkSpace.add(copyMenuItem);
                            if(Utils.isClipboardContainDOSObjects(this))
                                popupMenuForWorkSpace.add(pasteMenuItem);
                            if(!ouid.endsWith("/My Folder"))
                            {
                                popupMenuForWorkSpace.add(new JSeparator());
                                popupMenuForWorkSpace.add(removeFolderMenuItem);
                            }
                            willShow = true;
                        } else
                        if(ouid.indexOf("/PUBLIC/FOLDER") > 0)
                            try
                            {
                                if(!DynaMOAD.aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR"))
                                {
                                    popupMenuForWorkSpace.removeAll();
                                    popupMenuForWorkSpace.add(copyMenuItem);
                                    willShow = true;
                                } else
                                {
                                    popupMenuForWorkSpace.removeAll();
                                    popupMenuForWorkSpace.add(addFolderMenuItem);
                                    popupMenuForWorkSpace.add(new JSeparator());
                                    popupMenuForWorkSpace.add(copyMenuItem);
                                    if(Utils.isClipboardContainDOSObjects(this))
                                        popupMenuForWorkSpace.add(pasteMenuItem);
                                    popupMenuForWorkSpace.add(new JSeparator());
                                    popupMenuForWorkSpace.add(removeFolderMenuItem);
                                    willShow = true;
                                }
                            }
                            catch(Exception re)
                            {
                                willShow = false;
                            }
                    } else
                    if(object.getDescription() != null && object.getDescription().equals("FOLDERLINK"))
                    {
                        String ouid = object.getOuid();
                        if(ouid.indexOf("/PRIVATE/") > 0 && ouid.indexOf("/FOLDER", ouid.indexOf("/PRIVATE/")) > 0)
                        {
                            popupMenuForWorkSpace.removeAll();
                            if(!ouid.endsWith("/My Folder"))
                            {
                                popupMenuForWorkSpace.removeAll();
                                popupMenuForWorkSpace.add(removeFolderMenuItem);
                                willShow = true;
                            } else
                            {
                                willShow = false;
                            }
                        } else
                        if(ouid.indexOf("/PUBLIC/FOLDER") > 0)
                            try
                            {
                                if(!DynaMOAD.aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR"))
                                {
                                    willShow = false;
                                } else
                                {
                                    popupMenuForWorkSpace.removeAll();
                                    popupMenuForWorkSpace.add(removeFolderMenuItem);
                                    willShow = true;
                                }
                            }
                            catch(Exception re)
                            {
                                willShow = false;
                            }
                    } else
                    if(object.getDescription() != null && object.getDescription().equals("OBJECT"))
                    {
                        String ouid = object.getOuid();
                        if(ouid.indexOf("/PRIVATE/") > 0 && ouid.indexOf("/FOLDER", ouid.indexOf("/PRIVATE/")) > 0)
                        {
                            ArrayList ouidList = getSelectedOuidsForWorkSpaceTree();
                            String objectOuid = (String)ouidList.get(0);
                            int cnt = ouidList.size();
                            if(cnt < 1)
                                return;
                            if(!showPopupWorkSpaceHelper(objectOuid))
                            {
                                actionPerformed(new ActionEvent(SearchCondition4CADIntegration.this, 0, "MNU_DELETE^"));
                                return;
                            }
                            willShow = false;
                            try
                            {
                                classOuid = dos.getClassOuid(objectOuid);
                                if(Utils.isNullString(classOuid))
                                    return;
                                ArrayList assoList = UIGeneration.listAssociations(classOuid);
                                if(Utils.isNullArrayList(assoList))
                                    assoList = UIGeneration.listAssociations(classOuid, Boolean.FALSE);
                                popupMenuForWorkSpace = UIBuilder.createPopupMenuForSearchResult(objectOuid, ouidList, assoList, e.getComponent(), e.getX(), e.getY(), SearchCondition4CADIntegration.this, modelOuid);
                            }
                            catch(IIPRequestException e1)
                            {
                                e1.printStackTrace();
                            }
                        } else
                        if(ouid.indexOf("/PUBLIC/FOLDER") > 0)
                        {
                            String objectOuid = null;
                            DefaultMutableTreeNode node = null;
                            TreeNodeObject nodeObject = null;
                            TreePath paths[] = workSpaceTree.getSelectionPaths();
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
                                        if(!"OBJECT".equals(nodeObject.getDescription()))
                                        {
                                            node = null;
                                            nodeObject = null;
                                        } else
                                        {
                                            objectOuid = nodeObject.getOuid();
                                            java.util.List list = Utils.tokenizeMessage(objectOuid, '^');
                                            if(list != null && list.size() > 0)
                                            {
                                                objectOuid = (String)list.get(2);
                                                list.clear();
                                                list = null;
                                            }
                                            if(!Utils.isNullString(objectOuid))
                                                ouidList.add(objectOuid);
                                        }
                                }
                            }

                            int cnt = ouidList.size();
                            if(cnt < 1)
                                return;
                            if(!showPopupWorkSpaceHelper(ouid))
                            {
                                actionPerformed(new ActionEvent(SearchCondition4CADIntegration.this, 0, "MNU_DELETE^"));
                                return;
                            }
                            willShow = false;
                            try
                            {
                                classOuid = dos.getClassOuid(objectOuid);
                                if(Utils.isNullString(classOuid))
                                    return;
                                ArrayList assoList = UIGeneration.listAssociations(classOuid);
                                if(Utils.isNullArrayList(assoList))
                                    assoList = UIGeneration.listAssociations(classOuid, Boolean.FALSE);
                                popupMenuForWorkSpace = UIBuilder.createPopupMenuForSearchResult(objectOuid, ouidList, assoList, e.getComponent(), e.getX(), e.getY(), SearchCondition4CADIntegration.this, modelOuid);
                                if(!DynaMOAD.aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR"))
                                {
                                    Component components[] = popupMenuForWorkSpace.getComponents();
                                    for(int i = 0; i < components.length; i++)
                                        if(!Utils.isNullString(components[i].getName()) && components[i].getName().equals("Delete"))
                                            components[i].setEnabled(false);

                                }
                            }
                            catch(IIPRequestException e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                    } else
                    if(object.getOuid() != null && object.getOuid().equals("PrivateWorkSpace"))
                    {
                        popupMenuForWorkSpace.removeAll();
                        popupMenuForWorkSpace.add(addPrivateFolderMenuItem);
                        try
                        {
                            if(DynaMOAD.aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR"))
                            {
                                popupMenuForWorkSpace.add(new JSeparator());
                                popupMenuForWorkSpace.add(addPublicFolderMenuItem);
                            }
                            willShow = true;
                        }
                        catch(Exception ie)
                        {
                            ie.printStackTrace();
                            willShow = false;
                        }
                    } else
                    if(object.getDescription() != null && object.getDescription().equals("LISTFILTER"))
                    {
                        popupMenuForWorkSpace.removeAll();
                        popupMenuForWorkSpace.add(removeFilterMenuItem);
                        willShow = true;
                    } else
                    {
                        willShow = false;
                    }
                    object = null;
                }
                if(!willShow)
                    return;
                if(contentFrame instanceof Search4CADIntegration)
                {
                    Dimension framesize = ((Search4CADIntegration)contentFrame).getSize();
                    Dimension popupsize = popupMenuForWorkSpace.getSize();
                    int dividerLocation = ((Search4CADIntegration)contentFrame).mainSplitPane.getDividerLocation();
                    if(popupsize.width <= 0 || popupsize.height <= 0)
                        popupsize = new Dimension(107, 100);
                    Point point = SwingUtilities.convertPoint(e.getComponent(), e.getX(), e.getY(), (Search4CADIntegration)contentFrame);
                    int x = e.getX();
                    int y = e.getY();
                    if(popupsize.width + point.x >= dividerLocation)
                        x -= popupsize.width;
                    if(popupsize.height + point.y >= framesize.height)
                        y -= popupsize.height;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                    if(node == null)
                        return;
                    TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                    if(nodeObject == null)
                        return;
                    String objectOuid = nodeObject.getOuid();
                    java.util.List list = Utils.tokenizeMessage(objectOuid, '^');
                    if(list != null && list.size() > 2)
                    {
                        objectOuid = (String)list.get(2);
                        list.clear();
                        list = null;
                    }
                    if(Utils.isNullString(objectOuid))
                        return;
                    refreshActionMenu(popupMenuForWorkSpace, objectOuid);
                    popupMenuForWorkSpace.show(e.getComponent(), x, y);
                } else
                {
                    popupMenuForWorkSpace.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        private boolean showPopupWorkSpaceHelper(String ouid)
        {
            if(Utils.isNullString(ouid))
                return false;
            try
            {
                if(dos.get(ouid) == null)
                    return false;
            }
            catch(Exception e)
            {
                return false;
            }
            return true;
        }

        DefaultMutableTreeNode node;
        DOSChangeable classDC;

        PopupListener()
        {
            node = null;
            classDC = null;
        }
    }

    class PopupPositionMouseListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(e.getSource().equals(tree))
            {
                if(SwingUtilities.isRightMouseButton(e))
                    tree.setSelectionPath(tree.getClosestPathForLocation(e.getX(), e.getY()));
            } else
            if(e.getSource().equals(workSpaceTree) && SwingUtilities.isRightMouseButton(e))
            {
                TreePath treePath = workSpaceTree.getClosestPathForLocation(e.getX(), e.getY());
                TreePath paths[] = workSpaceTree.getSelectionPaths();
                int i = 0;
                if(paths == null)
                    return;
                for(; i < paths.length; i++)
                {
                    if(!paths[i].equals(treePath))
                        continue;
                    i = -1;
                    break;
                }

                paths = (TreePath[])null;
                if(i != -1)
                    workSpaceTree.setSelectionPath(treePath);
            }
        }

        PopupPositionMouseListener()
        {
        }
    }


    public SearchCondition4CADIntegration(Search4CADIntegration main)
    {
        clientUtil = null;
        treeRenderer = null;
        modelOuid = "";
        classOuid = "";
        hoardData = new DOSChangeable();
        fieldListForSearchResult = new ArrayList();
        selectedNode = new DefaultMutableTreeNode();
        fieldList = null;
        searchConditionData = new DOSChangeable();
        dateChooserButtonList = null;
        selectButtonList = null;
        searchConditionMap = null;
        classOuidForFilter = "";
        filterDialog = null;
        versionableClass = false;
        selectCnt = 0;
        filterList = null;
        popupListener = null;
        pageNumber = 1;
        countPerPage = -1;
        isPrivateWorkSpace = true;
        actionMenuCheck = null;
        NDS_CODE = null;
        contentFrame = main;
        try
        {
            dos = DynaMOAD.dos;
            nds = DynaMOAD.nds;
            acl = DynaMOAD.acl;
            dtm = DynaMOAD.dtm;
            dss = DynaMOAD.dss;
            wfm = DynaMOAD.wfm;
            clientUtil = new ClientUtil();
            newUI = DynaMOAD.newUI;
            modelOuid = dos.getWorkingModel();
            initialize();
            NDS_CODE = nds.getSubSet("CODE");
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        DynaMOAD.searchCondition4CADIntegration = this;
    }

    public void setSession(Session session)
    {
        _session = session;
    }

    public void setWorkSpace()
    {
        TreeNodeObject workSpaceRootObject = new TreeNodeObject("PrivateWorkSpace", LogIn.userID, "");
        DefaultMutableTreeNode workSpaceTopNode = new DefaultMutableTreeNode(workSpaceRootObject);
        DefaultTreeModel workSpaceTreeModel = new DefaultTreeModel(workSpaceTopNode);
        if(treeRenderer == null)
            treeRenderer = new DFDefaultTreeCellRenderer();
        workSpaceTree = new JTree(workSpaceTreeModel);
        workSpaceScrPane.setViewportView(workSpaceTree);
        TreeNodeObject childNodeData = null;
        try
        {
            inboxList = wfm.listInboxProcessByUser(LogIn.userID);
            if(inboxList == null)
                childNodeData = new TreeNodeObject("Inbox", DynaMOAD.getMSRString("WRD_0052", "Inbox", 3) + " (0) ", "Inbox");
            else
                childNodeData = new TreeNodeObject("Inbox", DynaMOAD.getMSRString("WRD_0052", "Inbox", 3) + " (" + inboxList.size() + ")", "Inbox");
            inboxList.clear();
            inboxList = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(childNodeData);
        setInsertNodeForWorkSpace(workSpaceTopNode, childnode);
        childNodeData = null;
        childnode = null;
        childNodeData = new TreeNodeObject("Sent", DynaMOAD.getMSRString("WRD_0053", "Sent", 3), "Sent");
        childnode = new DefaultMutableTreeNode(childNodeData);
        setInsertNodeForWorkSpace(workSpaceTopNode, childnode);
        childNodeData = null;
        childnode = null;
        childNodeData = new TreeNodeObject("Completed", DynaMOAD.getMSRString("WRD_0054", "Completed", 3), "Completed");
        childnode = new DefaultMutableTreeNode(childNodeData);
        setInsertNodeForWorkSpace(workSpaceTopNode, childnode);
        childNodeData = null;
        childnode = null;
        childNodeData = new TreeNodeObject("Recent", DynaMOAD.getMSRString("WRD_0055", "Recent", 3), "Recent");
        childnode = new DefaultMutableTreeNode(childNodeData);
        setInsertNodeForWorkSpace(workSpaceTopNode, childnode);
        childNodeData = null;
        try
        {
            HashMap node = null;
            node = new HashMap();
            node.put("workspace.type", "PRIVATE");
            node.put("node.type", "FOLDER");
            node.put("parent", "/");
            node.put("name", "My Folder");
            node.put("value", "DEFAULT_PRIVATE_FOLDER");
            DynaMOAD.wks.createNode(node);
            node.clear();
            node = null;
            addFolders(workSpaceTopNode, "PUBLIC", "FOLDER", "/");
            addFolders(workSpaceTopNode, "PRIVATE", "FOLDER", "/");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        workSpaceTree.scrollPathToVisible(new TreePath(childnode.getPath()));
        workSpaceTree.setCellRenderer(treeRenderer);
    }

    public void addFolders(DefaultMutableTreeNode node, String workspaceType, String nodeType, String path)
    {
        HashMap parentNode = null;
        HashMap childNode = null;
        ArrayList list = null;
        parentNode = new HashMap();
        if(Utils.isNullString(path))
        {
            TreeNodeObject object = (TreeNodeObject)node.getUserObject();
            path = object.getOuid();
            path = path.substring(path.indexOf("/FOLDER/") + 7);
            object = null;
            if(Utils.isNullString(path))
                return;
        }
        parentNode.put("workspace.type", workspaceType);
        parentNode.put("node.type", nodeType);
        parentNode.put("path", path);
        try
        {
            list = DynaMOAD.wks.getChildNodes(parentNode);
        }
        catch(Exception e)
        {
            parentNode.clear();
            parentNode = null;
            e.printStackTrace();
            return;
        }
        parentNode.clear();
        parentNode = null;
        try
        {
            if(!Utils.isNullArrayList(list))
            {
                DefaultMutableTreeNode treeNode = null;
                for(Iterator listKey = list.iterator(); listKey.hasNext();)
                {
                    childNode = (HashMap)listKey.next();
                    if(childNode.get("type").equals("OBJECT"))
                        treeNode = new DefaultMutableTreeNode(new TreeNodeObject((String)childNode.get("parent") + "/" + (String)childNode.get("name") + "^" + dos.getClassOuid((String)childNode.get("value")) + "^" + childNode.get("value"), (String)childNode.get("name"), (String)childNode.get("type")));
                    else
                        treeNode = new DefaultMutableTreeNode(new TreeNodeObject((String)childNode.get("parent") + "/" + (String)childNode.get("name"), (String)childNode.get("name"), (String)childNode.get("type")));
                    setInsertNodeForWorkSpace(node, treeNode);
                    addFolders(treeNode, workspaceType, (String)childNode.get("type"), null);
                    childNode = null;
                    treeNode = null;
                }

                list.clear();
                list = null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setTree()
    {
        TreeNodeObject nodeObj = new TreeNodeObject("top01", "Top", "test");
        topNode = new DefaultMutableTreeNode(nodeObj);
        treeModel = new DefaultTreeModel(topNode);
        tree = new JTree(treeModel);
    }

    public void initialize()
    {
        conditionBorderLayout = new BorderLayout();
        setLayout(conditionBorderLayout);
        workSpaceScrPane = UIFactory.createStrippedScrollPane(null);
        workSpaceScrPane.setViewportView(workSpaceTree);
        workSpaceScrPane.getViewport().setBackground(Color.white);
        treeScrPane = UIFactory.createStrippedScrollPane(null);
        treeScrPane.setViewportView(tree);
        treeScrPane.getViewport().setBackground(Color.white);
        workspaceFrame = new MInternalFrame(DynaMOAD.getMSRString("WRD_0048", "Navigator", 3));
        treeTabbedPane = new JTabbedPane();
        treeTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        treeTabbedPane.setTabPlacement(3);
        treeTabbedPane.addTab(DynaMOAD.getMSRString("WRD_0045", "Workspace", 0), new ImageIcon("icons/SelectWindow32.gif"), workSpaceScrPane);
        treeTabbedPane.addTab(DynaMOAD.getMSRString("WRD_0028", "Class", 3), new ImageIcon("icons/ClassTree.gif"), treeScrPane);
        treeTabbedPane.setSelectedIndex(1);
        treeTabbedPane.addChangeListener(this);
        workspaceFrame.add(treeTabbedPane);
        searchButton = new JButton();
        searchButton.setBackground(new Color(236, 233, 216));
        searchButton.setText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton.setActionCommand("search");
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.addActionListener(this);
        conditionSaveButton = new JButton();
        conditionSaveButton.setBackground(new Color(236, 233, 216));
        conditionSaveButton.setText(DynaMOAD.getMSRString("WRD_0015", "Filter", 3));
        conditionSaveButton.setActionCommand("Filter Save");
        conditionSaveButton.setMargin(new Insets(0, 0, 0, 0));
        conditionSaveButton.setIcon(new ImageIcon(getClass().getResource("/icons/ConditionSave.gif")));
        conditionSaveButton.addActionListener(this);
        clearButton = new JButton();
        clearButton.setBackground(new Color(236, 233, 216));
        clearButton.setText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clearButton.setActionCommand("Clear");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.addActionListener(this);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(UIManagement.panelBackGround);
        buttonPanel.setPreferredSize(new Dimension(270, 45));
        searchConditionPanel = new JPanel();
        searchConditionPanel.setLayout(null);
        searchConditionPanel.setBackground(UIManagement.panelBackGround);
        searchConditionPanel.setMinimumSize(new Dimension(270, 254));
        searchConditionPanel.setMaximumSize(new Dimension(270, 254));
        searchConditionPanel.setPreferredSize(new Dimension(270, 254));
        searchConditionScrPane = UIFactory.createStrippedScrollPane(null);
        searchConditionScrPane.setViewportView(searchConditionPanel);
        searchConditionScrPane.setColumnHeaderView(buttonPanel);
        searchConditionFrame = new MInternalFrame(DynaMOAD.getMSRString("WRD_0047", "Search Condition", 3));
        searchConditionFrame.add(searchConditionScrPane);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        conditionSplitPane = new BorderlessSplitPane(0, workspaceFrame, searchConditionFrame);
        conditionSplitPane.setDividerSize(5);
        conditionSplitPane.setDividerLocation(screenSize.height / 2 - 72);
        add(conditionSplitPane, "Center");
    }

    public JTree getTree()
    {
        return tree;
    }

    public void setTopNode(TreeNodeObject topnode)
    {
        this.topnode = topnode;
        if(tree != null)
            tree = null;
        topNode = new DefaultMutableTreeNode(topnode);
        treeModel = new DefaultTreeModel(topNode);
        tree = new JTree(treeModel);
        createPopupMenu();
        if(treeRenderer == null)
            treeRenderer = new DFDefaultTreeCellRenderer();
        tree.setCellRenderer(treeRenderer);
        tree.getSelectionModel().setSelectionMode(1);
        treeScrPane.setViewportView(tree);
    }

    public void setFullTreeExpand(DefaultMutableTreeNode node, String modelOuid)
    {
        if(node == null)
        {
            return;
        } else
        {
            this.modelOuid = modelOuid;
            setExpand(node);
            tree.setSelectionRow(0);
            return;
        }
    }

    private void setExpand(DefaultMutableTreeNode node)
    {
        String ouid = "";
        TreeNodeObject nodeData = null;
        DefaultMutableTreeNode childnode = null;
        boolean isLeaf = false;
        try
        {
            if(((Search4CADIntegration)contentFrame).cadType.equals("0") || ((Search4CADIntegration)contentFrame).cadType.equals("3") || ((Search4CADIntegration)contentFrame).cadType.equals("4") || ((Search4CADIntegration)contentFrame).cadType.equals("5"))
            {
                DOSChangeable packageFoundation = dos.getPackageWithName("Foundation");
                DOSChangeable classFCADFile = dos.getClassWithName((String)packageFoundation.get("ouid"), "FCADFile");
                isLeaf = ((Boolean)classFCADFile.get("is.leaf")).booleanValue();
                ouid = (String)classFCADFile.get("ouid");
                nodeData = new TreeNodeObject(ouid, (String)classFCADFile.get("title"), "Class");
                childnode = new DefaultMutableTreeNode(nodeData);
                setInsertNode(node, childnode);
                setExpand(childnode, ouid);
                if(!isLeaf && childnode.getChildCount() == 0)
                    childnode.removeFromParent();
                tree.scrollPathToVisible(new TreePath(childnode.getPath()));
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    private void setExpand(DefaultMutableTreeNode node, String classOuid)
    {
        this.classOuid = classOuid;
        String ouid = "";
        ArrayList subClassListAL = null;
        DOSChangeable classInfoDC = null;
        TreeNodeObject nodeData = null;
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
                    String packageOuid = (String)classInfoDC.get("ouid@package");
                    DOSChangeable aPackage = dos.getPackage(packageOuid);
                    String packageName = (String)aPackage.get("name");
                    if(((Search4CADIntegration)contentFrame).cadType.equals("0") && packageName.equals("Package for Solidworks") || ((Search4CADIntegration)contentFrame).cadType.equals("3") && packageName.equals("Package for InventorV6") || ((Search4CADIntegration)contentFrame).cadType.equals("4") && packageName.equals("Package for CATIA V4") || ((Search4CADIntegration)contentFrame).cadType.equals("5") && packageName.equals("Package for AutoCAD"))
                    {
                        nodeData = new TreeNodeObject(ouid, (String)classInfoDC.get("title"), "Class");
                        childnode = new DefaultMutableTreeNode(nodeData);
                        setInsertNode(node, childnode);
                        setExpand(childnode, ouid);
                        if(!isLeaf && childnode.getChildCount() == 0)
                            childnode.removeFromParent();
                    }
                }

            } else
            {
                return;
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    void setInsertNodeForWorkSpace(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)workSpaceTree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    public void createPopupMenu()
    {
        if(popupListener == null)
            popupListener = new PopupListener();
        tree.addMouseListener(popupListener);
        MouseListener popupPositionListener = new PopupPositionMouseListener();
        tree.addMouseListener(popupPositionListener);
        popupMenuForWorkSpace = new JPopupMenu();
        deleteMenuForWorkSpace = new JMenuItem();
        deleteMenuForWorkSpace.setText(DynaMOAD.getMSRString("WRD_0131", "Clear", 3));
        deleteMenuForWorkSpace.setIcon(new ImageIcon("icons/Clear.gif"));
        deleteMenuForWorkSpace.setActionCommand("DeleteMenuForWorkSpace");
        deleteMenuForWorkSpace.addActionListener(this);
        addFolderMenuItem = new JMenuItem();
        addFolderMenuItem.setText(DynaMOAD.getMSRString("WRD_0128", "Create a new folder...", 3));
        addFolderMenuItem.setIcon(new ImageIcon("icons/defaultFolder.gif"));
        addFolderMenuItem.setActionCommand("add.folder");
        addFolderMenuItem.addActionListener(this);
        addPrivateFolderMenuItem = new JMenuItem();
        addPrivateFolderMenuItem.setText(DynaMOAD.getMSRString("WRD_0129", "Create a new private folder...", 3));
        addPrivateFolderMenuItem.setIcon(new ImageIcon("icons/defaultFolder.gif"));
        addPrivateFolderMenuItem.setActionCommand("add.folder.private");
        addPrivateFolderMenuItem.addActionListener(this);
        addSharedFolderMenuItem = new JMenuItem();
        addSharedFolderMenuItem.setText(DynaMOAD.getMSRString("WRD_0130", "Create a new shared folder...", 3));
        addSharedFolderMenuItem.setIcon(new ImageIcon("icons/defaultFolder.gif"));
        addSharedFolderMenuItem.setActionCommand("add.folder.shared");
        addSharedFolderMenuItem.addActionListener(this);
        addPublicFolderMenuItem = new JMenuItem();
        addPublicFolderMenuItem.setText(DynaMOAD.getMSRString("WRD_0130", "Create a new public folder...", 3));
        addPublicFolderMenuItem.setIcon(new ImageIcon("icons/defaultFolder.gif"));
        addPublicFolderMenuItem.setActionCommand("add.folder.public");
        addPublicFolderMenuItem.addActionListener(this);
        removeFolderMenuItem = new JMenuItem();
        removeFolderMenuItem.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        removeFolderMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        removeFolderMenuItem.setActionCommand("remove.folder");
        removeFolderMenuItem.addActionListener(this);
        removeFilterMenuItem = new JMenuItem();
        removeFilterMenuItem.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        removeFilterMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        removeFilterMenuItem.setActionCommand("remove.filter");
        removeFilterMenuItem.addActionListener(this);
        copyMenuItem = new JMenuItem();
        copyMenuItem.setText(DynaMOAD.getMSRString("WRD_0009", "Copy", 3));
        copyMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyMenuItem.setActionCommand("copy");
        copyMenuItem.addActionListener(this);
        pasteMenuItem = new JMenuItem();
        pasteMenuItem.setText(DynaMOAD.getMSRString("WRD_0010", "Paste Link", 3));
        pasteMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteMenuItem.setActionCommand("pasteLink.object");
        pasteMenuItem.addActionListener(this);
        if(popupListener == null)
            popupListener = new PopupListener();
        workSpaceTree.addMouseListener(popupListener);
        MouseListener popupList = new PopupPositionMouseListener();
        workSpaceTree.addMouseListener(popupList);
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
    }

    public void advancedPanelInit()
    {
        if(this.tree == null)
            return;
        try
        {
            selectButtonList = new ArrayList();
            dateChooserButtonList = new ArrayList();
            String classOuid = null;
            JTree tree = getCurrentTree();
            int mode = 0;
            if(tree.equals(this.tree))
                mode = 1;
            else
            if(tree.equals(hcodeTree))
                mode = 2;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(mode == 1)
                classOuid = tmpdata.getOuid();
            else
            if(mode == 2)
            {
                LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                classOuid = (String)tempList.get(1);
                tempList.clear();
                tempList = null;
            }
            buttonPanel = new JPanel();
            buttonPanel.setLayout(null);
            buttonPanel.setPreferredSize(new Dimension(270, 45));
            buttonPanel.setBackground(UIManagement.panelBackGround);
            searchConditionPanel = new JPanel();
            searchConditionPanel.setLayout(null);
            searchConditionPanel.setBackground(UIManagement.panelBackGround);
            searchConditionPanel.setMinimumSize(new Dimension(270, 254));
            searchConditionPanel.setMaximumSize(new Dimension(270, 254));
            searchConditionPanel.setPreferredSize(new Dimension(270, 254));
            if(node.getLevel() != 0)
            {
                buttonPanel.add(searchButton);
                buttonPanel.add(conditionSaveButton);
                buttonPanel.add(clearButton);
            }
            searchConditionScrPane.setViewportView(searchConditionPanel);
            searchConditionScrPane.setColumnHeaderView(buttonPanel);
            ArrayList tmpFieldList = UIBuilder.getDefaultFieldList(dos, classOuid);
            DOSChangeable classInfo = dos.getClass(classOuid);
            DOSChangeable tmpDOS = new DOSChangeable();
            if(tmpFieldList == null)
                return;
            if(node.getLevel() != 0 && Utils.getBoolean((Boolean)classInfo.get("is.versionable")))
            {
                tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                tmpDOS.put("name", "Version Type");
                tmpDOS.put("ouid", "version.condition.type");
                tmpDOS.put("index", new Integer(-985));
                tmpDOS.put("type", new Byte((byte)13));
                tmpDOS.put("is.visible", Boolean.TRUE);
                tmpFieldList.add(2, tmpDOS);
                tmpDOS = null;
                tmpDOS = new DOSChangeable();
            }
            Utils.sort(tmpFieldList);
            fieldList = new ArrayList();
            ArrayList searchConditionNds = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid);
            if(searchConditionNds == null)
                fieldList = (ArrayList)tmpFieldList.clone();
            else
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
                    fieldList.add(fieldData);
                }

            }
            int y_pos = 0;
            if(fieldList != null)
            {
                initStatus();
                String division01 = "Last Released";
                String division02 = "Work In Progress";
                String division05 = "All Version";
                String division03 = "Created Date";
                String division04 = "Modified Date";
                String versionType[] = {
                    division01, division02, division05, division03, division04
                };
                String trueStr = "true";
                String falseStr = "false";
                String trueOrFalse[] = {
                    "", trueStr, falseStr
                };
                HashMap setDatas = (HashMap)searchConditionData.get(classOuid);
                makeFilterComboBox();
                listFilterLabel = new JLabel(DynaMOAD.getMSRString("WRD_0015", "Filter", 3));
                listFilterLabel.setBounds(10, 46, 100, 20);
                listFilterComboBox.setBounds(110, 46, 160, 20);
                searchConditionPanel.add(listFilterLabel);
                searchConditionPanel.add(listFilterComboBox);
                boolean tmpBoolean = false;
                boolean versionBoolean = false;
                Boolean isVisible = null;
                String fieldOuid = null;
                String fieldName = null;
                String fieldCode = null;
                Byte fieldType = null;
                int i = -1;
                for(int j = 0; j < fieldList.size(); j++)
                {
                    DOSChangeable fieldData = (DOSChangeable)fieldList.get(j);
                    if(fieldData != null)
                    {
                        isVisible = (Boolean)fieldData.get("is.visible");
                        if(!Utils.getBoolean(isVisible))
                        {
                            fieldData = null;
                        } else
                        {
                            i++;
                            JLabel advancedTestLabel = new JLabel();
                            JTextField advancedTestTextField = new JTextField();
                            JButton dateSelectButton = new JButton();
                            JButton selectButtonByType = new JButton();
                            fieldOuid = (String)fieldData.get("ouid");
                            fieldName = (String)fieldData.get("name");
                            fieldType = (Byte)fieldData.get("type");
                            fieldCode = (String)fieldData.get("code");
                            if(fieldCode == null)
                                fieldCode = "";
                            advancedTestLabel = new JLabel(DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0));
                            if(versionBoolean)
                                advancedTestLabel.setBounds(10, 69 + 23 * (i + 2), 100, 20);
                            else
                                advancedTestLabel.setBounds(10, 69 + 23 * i, 100, 20);
                            searchConditionPanel.add(advancedTestLabel);
                            if(!fieldCode.equals("md$status") && !fieldOuid.equals("version.condition.type") && fieldType.byteValue() != 1)
                            {
                                if(!fieldOuid.equals("md$cdate") && !fieldOuid.equals("md$mdate") && fieldType.byteValue() != 22 && fieldType.byteValue() != 21)
                                {
                                    if(fieldType.byteValue() == 16 || fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                                    {
                                        if(versionBoolean)
                                            advancedTestTextField.setBounds(110, 69 + 23 * (i + 2), 143, 20);
                                        else
                                            advancedTestTextField.setBounds(110, 69 + 23 * i, 143, 20);
                                    } else
                                    if(versionBoolean)
                                        advancedTestTextField.setBounds(110, 69 + 23 * (i + 2), 160, 20);
                                    else
                                        advancedTestTextField.setBounds(110, 69 + 23 * i, 160, 20);
                                    searchConditionPanel.add(advancedTestTextField);
                                    hoardData.put(fieldName, advancedTestTextField);
                                    hoardData.put(fieldName + "^ouid", fieldOuid);
                                    if(fieldType.byteValue() == 16 || fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                                    {
                                        selectCnt++;
                                        if(versionBoolean)
                                            selectButtonByType.setBounds(252, 69 + 23 * (i + 2), 18, 20);
                                        else
                                            selectButtonByType.setBounds(252, 69 + 23 * i, 18, 20);
                                        selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
                                        if(fieldType.byteValue() == 16)
                                            selectButtonByType.setActionCommand("ObjectSelectButtonClick_" + selectCnt + "_" + fieldName + "/" + (String)fieldData.get("type.ouid@class") + "/" + fieldName);
                                        else
                                        if(fieldType.byteValue() == 24)
                                            selectButtonByType.setActionCommand("CodeSelectButtonClick_" + selectCnt + "_" + fieldName + "/" + (String)fieldData.get("type.ouid@class") + "/" + fieldName);
                                        else
                                        if(fieldType.byteValue() == 25)
                                            selectButtonByType.setActionCommand("CodeReferenceSelectButtonClick_" + selectCnt + "_" + fieldName + "/" + fieldOuid + "/" + fieldName);
                                        selectButtonByType.addActionListener(this);
                                        selectButtonList.add(selectButtonByType.getActionCommand());
                                        searchConditionPanel.add(selectButtonByType);
                                    }
                                    if(setDatas != null)
                                        if(fieldType.byteValue() == 16)
                                        {
                                            String data = (String)setDatas.get((String)fieldData.get("ouid"));
                                            if(!Utils.isNullString(data))
                                            {
                                                DOSChangeable instanceData = dos.get(data);
                                                if(instanceData != null)
                                                    advancedTestTextField.setText((String)instanceData.get("md$number"));
                                                else
                                                    advancedTestTextField.setText("");
                                            } else
                                            {
                                                advancedTestTextField.setText("");
                                            }
                                        } else
                                        if(fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                                        {
                                            String data = (String)setDatas.get((String)fieldData.get("ouid"));
                                            if(!Utils.isNullString(data))
                                            {
                                                DOSChangeable codeItemData = dos.getCodeItem(data);
                                                if(codeItemData != null)
                                                    advancedTestTextField.setText((String)codeItemData.get("name") + " [" + (String)codeItemData.get("codeitemid") + "]");
                                                else
                                                    advancedTestTextField.setText("");
                                            } else
                                            {
                                                advancedTestTextField.setText("");
                                            }
                                        } else
                                        {
                                            advancedTestTextField.setText((String)setDatas.get(fieldOuid));
                                        }
                                } else
                                {
                                    if(versionBoolean)
                                        advancedTestTextField.setBounds(110, 69 + 23 * (i + 2), 137, 20);
                                    else
                                        advancedTestTextField.setBounds(110, 69 + 23 * i, 137, 20);
                                    searchConditionPanel.add(advancedTestTextField);
                                    hoardData.put(fieldName, advancedTestTextField);
                                    if(versionBoolean)
                                        dateSelectButton.setBounds(250, 69 + 23 * (i + 2), 20, 20);
                                    else
                                        dateSelectButton.setBounds(250, 69 + 23 * i, 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("DateChooserButtonClick_" + fieldName);
                                    dateSelectButton.addActionListener(this);
                                    dateChooserButtonList.add(fieldName);
                                    searchConditionPanel.add(dateSelectButton);
                                    if(setDatas != null)
                                        advancedTestTextField.setText((String)setDatas.get(fieldOuid));
                                }
                            } else
                            {
                                JComboBox advancedComboBox;
                                if(fieldCode.equals("md$status"))
                                {
                                    advancedComboBox = new JComboBox(statusArray);
                                    if(setDatas != null)
                                    {
                                        String values = DynaMOAD.getMSRString((String)setDatas.get(fieldOuid), (String)setDatas.get(fieldOuid), 0);
                                        advancedComboBox.setSelectedItem(values);
                                    }
                                } else
                                if(fieldOuid.equals("version.condition.type"))
                                {
                                    advancedComboBox = new JComboBox(versionType);
                                    advancedComboBox.setSelectedItem("Work In Progress");
                                    advancedComboBox.setActionCommand(fieldName);
                                    advancedComboBox.addActionListener(this);
                                    tmpBoolean = true;
                                    advancedTestLabel = new JLabel("From");
                                    advancedTestLabel.setBounds(10, 69 + 23 * (i + 1), 100, 20);
                                    searchConditionPanel.add(advancedTestLabel);
                                    advancedTestLabel = new JLabel("To");
                                    advancedTestLabel.setBounds(10, 69 + 23 * (i + 2), 100, 20);
                                    searchConditionPanel.add(advancedTestLabel);
                                    advancedTestTextField.setBounds(110, 69 + 23 * (i + 1), 137, 20);
                                    advancedTestTextField.setEnabled(false);
                                    advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                    searchConditionPanel.add(advancedTestTextField);
                                    hoardData.put("Version Date From", advancedTestTextField);
                                    dateSelectButton.setEnabled(false);
                                    dateSelectButton.setBounds(250, 69 + 23 * (i + 1), 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date From");
                                    dateSelectButton.addActionListener(this);
                                    hoardData.put("VersionDateFromButton", dateSelectButton);
                                    dateChooserButtonList.add("Version Date From");
                                    searchConditionPanel.add(dateSelectButton);
                                    if(setDatas != null)
                                        advancedTestTextField.setText((String)setDatas.get("version.condition.date.from"));
                                    advancedTestTextField = new JTextField();
                                    advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                    advancedTestTextField.setEnabled(false);
                                    advancedTestTextField.setBounds(110, 69 + 23 * (i + 2), 137, 20);
                                    searchConditionPanel.add(advancedTestTextField);
                                    hoardData.put("Version Date To", advancedTestTextField);
                                    dateSelectButton = new JButton();
                                    dateSelectButton.setEnabled(false);
                                    dateSelectButton.setBounds(250, 69 + 23 * (i + 2), 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date To");
                                    dateSelectButton.addActionListener(this);
                                    hoardData.put("VersionDateToButton", dateSelectButton);
                                    dateChooserButtonList.add("Version Date To");
                                    searchConditionPanel.add(dateSelectButton);
                                    if(setDatas != null)
                                        advancedTestTextField.setText((String)setDatas.get("version.condition.date.to"));
                                    if(setDatas != null)
                                    {
                                        String selectedStr = (String)setDatas.get((String)fieldData.get("ouid"));
                                        if(selectedStr != null)
                                        {
                                            String values = "";
                                            if(selectedStr.equals("released"))
                                                values = "Last Released";
                                            else
                                            if(selectedStr.equals("wip"))
                                                values = "Work In Progress";
                                            else
                                            if(selectedStr.equals("all"))
                                                values = "All Version";
                                            else
                                            if(selectedStr.equals("cdate"))
                                                values = "Created Date";
                                            else
                                                values = "Modified Date";
                                            advancedComboBox.setSelectedItem(values);
                                        }
                                    }
                                } else
                                if(fieldType.byteValue() == 1)
                                {
                                    advancedComboBox = new JComboBox(trueOrFalse);
                                    if(setDatas != null)
                                    {
                                        Boolean selectedStr = (Boolean)setDatas.get(fieldOuid);
                                        if(selectedStr != null)
                                        {
                                            String values = "";
                                            if(selectedStr.booleanValue())
                                                values = "true";
                                            else
                                                values = "false";
                                            advancedComboBox.setSelectedItem(values);
                                        }
                                    }
                                } else
                                {
                                    advancedComboBox = new JComboBox();
                                }
                                if(versionBoolean)
                                    advancedComboBox.setBounds(110, 69 + 23 * (i + 2), 160, 20);
                                else
                                    advancedComboBox.setBounds(110, 69 + 23 * i, 160, 20);
                                searchConditionPanel.add(advancedComboBox);
                                hoardData.put(fieldName, advancedComboBox);
                                if(tmpBoolean)
                                    versionBoolean = true;
                            }
                        }
                    }
                }

                i++;
                countPerPageLabel = new JLabel(DynaMOAD.getMSRString("WRD_0161", "Rows Restriction", 3));
                if(versionBoolean)
                    countPerPageLabel.setBounds(10, 69 + 23 * (i + 2), 100, 20);
                else
                    countPerPageLabel.setBounds(10, 69 + 23 * i, 100, 20);
                searchConditionPanel.add(countPerPageLabel);
                countPerPageTextField = new JTextField();
                if(versionBoolean)
                    countPerPageTextField.setBounds(110, 69 + 23 * (i + 2), 160, 20);
                else
                    countPerPageTextField.setBounds(110, 69 + 23 * i, 160, 20);
                searchConditionPanel.add(countPerPageTextField);
                if(setDatas != null)
                {
                    String data = (String)setDatas.get("search.result.count");
                    if(!Utils.isNullString(data))
                    {
                        countPerPageTextField.setText(data);
                        countPerPage = Integer.parseInt(data);
                    } else
                    {
                        countPerPageTextField.setText("");
                        countPerPage = 1000;
                    }
                }
                if(versionBoolean)
                    y_pos = 69 + 23 * (i + 4);
                else
                    y_pos = 69 + 23 * (i + 2);
            }
            searchButton.setBounds(15, 10, 76, 24);
            conditionSaveButton.setBounds(101, 10, 76, 24);
            clearButton.setBounds(187, 10, 76, 24);
            searchConditionPanel.setMinimumSize(new Dimension(270, y_pos));
            searchConditionPanel.setMaximumSize(new Dimension(270, y_pos));
            searchConditionPanel.setPreferredSize(new Dimension(270, y_pos));
            updateUI();
            if(!LogIn.isAdmin)
            {
                boolean chk = false;
                chk = acl.hasPermission(classOuid, "0", "SCAN", LogIn.userID);
                if(chk)
                    searchButton.setEnabled(true);
                else
                    searchButton.setEnabled(false);
            } else
            {
                searchButton.setEnabled(true);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public DOSChangeable getSearchConditionForCADIntegration()
    {
        DOSChangeable returnDC = new DOSChangeable();
        return returnDC;
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        try
        {
            if(hoardData.get(sign) instanceof JTextField)
            {
                String fieldOuid = (String)hoardData.get(sign + "^ouid");
                DOSChangeable fieldData = null;
                if(fieldOuid != null)
                {
                    String classOuid = null;
                    JTree tree = getCurrentTree();
                    int mode = 0;
                    if(tree.equals(this.tree))
                        mode = 1;
                    else
                    if(tree.equals(hcodeTree))
                        mode = 2;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                    if(node == null)
                        return;
                    TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
                    if(mode == 1)
                        classOuid = tmpdata.getOuid();
                    else
                    if(mode == 2)
                    {
                        LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                        classOuid = (String)tempList.get(1);
                        tempList.clear();
                        tempList = null;
                    }
                    HashMap tmpMap = (HashMap)searchConditionData.get(classOuid);
                    if(tmpMap == null)
                        tmpMap = new HashMap();
                    for(int i = 0; i < fieldList.size(); i++)
                    {
                        fieldData = (DOSChangeable)fieldList.get(i);
                        if(!fieldData.get("ouid").equals(fieldOuid))
                            continue;
                        if(((Byte)fieldData.get("type")).byteValue() == 16)
                        {
                            DOSChangeable dosObject = dos.get(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText((String)dosObject.get("md$number"));
                            tmpMap.put(fieldOuid, (String)dosObject.get("ouid"));
                        } else
                        if(((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                        {
                            DOSChangeable dosCodeItem = dos.getCodeItem(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText(dosCodeItem.get("name") + " [" + dosCodeItem.get("codeitemid") + "]");
                            tmpMap.put(fieldOuid, (String)dosCodeItem.get("ouid"));
                        } else
                        {
                            ((JTextField)hoardData.get(sign)).setText(selectOuid);
                            tmpMap.put(fieldOuid, selectOuid);
                        }
                        break;
                    }

                    searchConditionData.put(classOuid, tmpMap);
                } else
                {
                    ((JTextField)hoardData.get(sign)).setText(selectOuid);
                }
            } else
            if(hoardData.get(sign) instanceof JComboBox)
                if(sign.equals("md$status"))
                {
                    String values = DynaMOAD.getMSRString(selectOuid, selectOuid, 0);
                    ((JComboBox)hoardData.get(sign)).setSelectedItem(values);
                } else
                if(sign.equals("Version Type"))
                {
                    if(selectOuid != null)
                    {
                        String values = "";
                        if(selectOuid.equals("released"))
                            values = "Last Released";
                        else
                        if(selectOuid.equals("wip"))
                            values = "Work In Progress";
                        else
                        if(selectOuid.equals("all"))
                            values = "All Version";
                        else
                        if(selectOuid.equals("cdate"))
                            values = "Created Date";
                        else
                            values = "Modified Date";
                        ((JComboBox)hoardData.get(sign)).setSelectedItem(values);
                    }
                } else
                {
                    ((JComboBox)hoardData.get(sign)).setSelectedItem(selectOuid);
                }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setConditionButtonEnabled(boolean tmpBoolean)
    {
        conditionSaveButton.setEnabled(tmpBoolean);
    }

    public void setResultButtonEnabled(boolean tmpBoolean)
    {
        clearButton.setEnabled(tmpBoolean);
    }

    public void setFilterInLink(HashMap searchCondition)
    {
        searchConditionMap = searchCondition;
    }

    public void setFilterDialogToNull()
    {
        filterDialog = null;
    }

    public void clearAllFieldValue()
    {
        listFilterComboBox.setSelectedIndex(-1);
        clearFieldValue();
        searchConditionMap = null;
    }

    private void clearFieldValue()
    {
        for(int i = 0; i < fieldList.size(); i++)
        {
            DOSChangeable fieldInfo = (DOSChangeable)fieldList.get(i);
            if(fieldInfo != null)
            {
                String title = (String)fieldInfo.get("name");
                if(hoardData.get(title) instanceof JTextField)
                    ((JTextField)hoardData.get(title)).setText("");
                else
                if(hoardData.get(title) instanceof JComboBox)
                    ((JComboBox)hoardData.get(title)).setSelectedIndex(-1);
                else
                    System.out.println(title + " / " + hoardData.get(title));
            }
        }

        if(hoardData.get("Version Date From") != null)
            ((JTextField)hoardData.get("Version Date From")).setText("");
        if(hoardData.get("Version Date To") != null)
            ((JTextField)hoardData.get("Version Date To")).setText("");
        countPerPageTextField.setText("");
    }

    public void makeFilterComboBox()
    {
        try
        {
            String tmpClassOuid = null;
            ArrayList classList = null;
            ArrayList pubFieldColumn = null;
            ArrayList fieldColumn = null;
            HashMap itemMap = null;
            filterList = new ArrayList();
            classList = dos.listAllSuperClassOuid(classOuidForFilter);
            if(classList == null)
                classList = new ArrayList();
            classList.add(0, classOuidForFilter);
            for(int i = 0; i < classList.size(); i++)
            {
                tmpClassOuid = (String)classList.get(i);
                pubFieldColumn = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + tmpClassOuid);
                fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + tmpClassOuid);
                if(pubFieldColumn != null)
                {
                    for(int j = 0; j < pubFieldColumn.size(); j++)
                    {
                        itemMap = new HashMap();
                        itemMap.put("name", (String)pubFieldColumn.get(j));
                        itemMap.put("option", "PUBLIC");
                        itemMap.put("classouid", tmpClassOuid);
                        if(!filterList.contains(itemMap))
                            filterList.add(itemMap.clone());
                        itemMap.clear();
                        itemMap = null;
                    }

                }
                if(fieldColumn != null)
                {
                    for(int j = 0; j < fieldColumn.size(); j++)
                    {
                        itemMap = new HashMap();
                        itemMap.put("name", (String)fieldColumn.get(j));
                        itemMap.put("option", "PRIVATE");
                        itemMap.put("classouid", tmpClassOuid);
                        if(!filterList.contains(itemMap))
                            filterList.add(itemMap.clone());
                        itemMap.clear();
                        itemMap = null;
                    }

                }
            }

            if(filterList.size() < 1)
            {
                listFilterComboBox = new JComboBox();
            } else
            {
                String filterArray[] = new String[filterList.size() + 1];
                filterArray[0] = "";
                for(int j = 0; j < filterList.size(); j++)
                {
                    itemMap = (HashMap)filterList.get(j);
                    filterArray[j + 1] = itemMap.get("name") + " [" + itemMap.get("option") + "]";
                }

                listFilterComboBox = new JComboBox(filterArray);
            }
            listFilterComboBox.setActionCommand("List Combo");
            listFilterComboBox.addActionListener(this);
            searchConditionMap = null;
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("New Object"))
        {
            String classOuid = null;
            JTree tree = getCurrentTree();
            int mode = 0;
            if(tree.equals(this.tree))
                mode = 1;
            else
            if(tree.equals(hcodeTree))
                mode = 2;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(mode == 1)
                classOuid = tmpdata.getOuid();
            else
            if(mode == 2)
            {
                LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                classOuid = (String)tempList.get(1);
                tempList.clear();
                tempList = null;
            }
            UIGeneration uiGeneration = new UIGeneration(this, classOuid);
            uiGeneration.setSession(_session);
        } else
        if(command.equals("search") && fieldList != null)
        {
            setPageNumber(1);
            searchButton_actionPerformed(e);
        } else
        if(command.equals("DeleteMenuForWorkSpace"))
            try
            {
                if(workSpaceTree == null)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                if(node != null)
                {
                    TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
                    if(tmpdata.getOuid().equals("Recent"))
                    {
                        boolean isDelete = nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/RECENT");
                        if(isDelete)
                        {
                            String tmpStr = "";
                            tmpStr = DynaMOAD.getMSRString("WRD_0002", "Delete", 3);
                            Object args[] = {
                                tmpStr
                            };
                            tmpStr = DynaMOAD.getMSRString("INF_0001", "", 0);
                            MessageFormat form = new MessageFormat(tmpStr);
                            String messageStr = form.format(((Object) (args)));
                            String titleStr = DynaMOAD.getMSRString("WRD_0004", "Information", 3);
                            JOptionPane.showMessageDialog((Search4CADIntegration)contentFrame, messageStr, titleStr, 1);
                        }
                    } else
                    {
                        boolean isDelete = nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/" + modelOuid + "/FAVORITE");
                        if(isDelete)
                        {
                            String tmpStr = "";
                            tmpStr = DynaMOAD.getMSRString("WRD_0002", "Delete", 3);
                            Object args[] = {
                                tmpStr
                            };
                            tmpStr = DynaMOAD.getMSRString("INF_0001", "", 0);
                            MessageFormat form = new MessageFormat(tmpStr);
                            String messageStr = form.format(((Object) (args)));
                            String titleStr = DynaMOAD.getMSRString("WRD_0004", "Information", 3);
                            JOptionPane.showMessageDialog((Search4CADIntegration)contentFrame, messageStr, titleStr, 1);
                        }
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("Version Type"))
        {
            if(hoardData.get("Version Type") == null || ((JComboBox)hoardData.get("Version Type")).getSelectedItem() == null)
                return;
            if(((String)((JComboBox)hoardData.get("Version Type")).getSelectedItem()).equals("Created Date") || ((String)((JComboBox)hoardData.get("Version Type")).getSelectedItem()).equals("Modified Date"))
            {
                ((JTextField)hoardData.get("Version Date From")).setEnabled(true);
                ((JTextField)hoardData.get("Version Date From")).setBackground(Color.white);
                ((JTextField)hoardData.get("Version Date To")).setEnabled(true);
                ((JTextField)hoardData.get("Version Date To")).setBackground(Color.white);
                ((JButton)hoardData.get("VersionDateFromButton")).setEnabled(true);
                ((JButton)hoardData.get("VersionDateToButton")).setEnabled(true);
            } else
            {
                ((JTextField)hoardData.get("Version Date From")).setEnabled(false);
                ((JTextField)hoardData.get("Version Date From")).setBackground(UIManagement.textBoxNotEditableBack);
                ((JTextField)hoardData.get("Version Date To")).setEnabled(false);
                ((JTextField)hoardData.get("Version Date To")).setBackground(UIManagement.textBoxNotEditableBack);
                ((JButton)hoardData.get("VersionDateFromButton")).setEnabled(false);
                ((JButton)hoardData.get("VersionDateToButton")).setEnabled(false);
            }
        } else
        if(command.equals("Filter Save"))
        {
            if(filterDialog == null)
            {
                filterDialog = new FilterDialogForList((Search4CADIntegration)contentFrame, this, (Component)e.getSource(), false, classOuidForFilter);
                filterDialog.setVisible(true);
            } else
            {
                filterDialog.toFront();
            }
        } else
        if(command.equals("Clear"))
            clearAllFieldValue();
        else
        if(command.equals("List Combo"))
            try
            {
                clearFieldValue();
                HashMap searchCondition = new HashMap();
                int index = listFilterComboBox.getSelectedIndex();
                if(index < 1)
                {
                    searchConditionMap = null;
                } else
                {
                    HashMap selectedItemMap = (HashMap)filterList.get(index - 1);
                    if(((String)selectedItemMap.get("option")).equals("PUBLIC"))
                    {
                        ArrayList filterField = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name"));
                        for(int i = 0; i < filterField.size(); i++)
                        {
                            String filterID = (String)filterField.get(i);
                            String value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name") + "/" + filterID);
                            searchCondition.put(filterID, value);
                            searchConditionMap = searchCondition;
                        }

                    } else
                    if(((String)selectedItemMap.get("option")).equals("PRIVATE"))
                    {
                        ArrayList filterField = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name"));
                        for(int i = 0; i < filterField.size(); i++)
                        {
                            String filterID = (String)filterField.get(i);
                            String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name") + "/" + filterID);
                            searchCondition.put(filterID, value);
                            searchConditionMap = searchCondition;
                        }

                    }
                }
                if(index > 0)
                    searchButton.doClick();
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("SetAuthority1"))
        {
            String classOuid = null;
            JTree tree = getCurrentTree();
            int mode = 0;
            if(tree.equals(this.tree))
                mode = 1;
            else
            if(tree.equals(hcodeTree))
                mode = 2;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(mode == 1)
                classOuid = tmpdata.getOuid();
            else
            if(mode == 2)
            {
                LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                classOuid = (String)tempList.get(1);
                tempList.clear();
                tempList = null;
            }
            AuthoritySetting AuthSet = new AuthoritySetting(this, DynaMOAD.getMSRString("WRD_0109", "Authority Setting", 3), classOuid, "");
            AuthSet.setVisible(true);
        } else
        if(command.startsWith("SetProcessAuthority"))
        {
            String classOuid = null;
            JTree tree = getCurrentTree();
            int mode = 0;
            if(tree.equals(this.tree))
                mode = 1;
            else
            if(tree.equals(hcodeTree))
                mode = 2;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(mode == 1)
                classOuid = tmpdata.getOuid();
            else
            if(mode == 2)
            {
                LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                classOuid = (String)tempList.get(1);
                tempList.clear();
                tempList = null;
            }
            String procOuid = command.substring(command.indexOf('/') + 1);
            ProcessAuthoritySetting PAuthSet = new ProcessAuthoritySetting(this, DynaMOAD.getMSRString("WRD_0110", "Process Authority Setting", 3), classOuid, procOuid);
            PAuthSet.setVisible(true);
        } else
        if(command.equals("add.folder"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
            if(nodeObject == null)
                return;
            String folderName = JOptionPane.showInputDialog(workSpaceTree, DynaMOAD.getMSRString("INF_0008", "Folder name: ", 3));
            if(Utils.isNullString(folderName))
                return;
            DefaultMutableTreeNode treeNode = null;
            String path = nodeObject.getOuid();
            HashMap folderNode = new HashMap();
            boolean result = false;
            try
            {
                folderNode.put("node.type", "FOLDER");
                folderNode.put("parent", path.substring(path.lastIndexOf("/FOLDER/") + 7));
                folderNode.put("name", folderName);
                folderNode.put("value", path);
                if(nodeObject.getOuid().indexOf("/PRIVATE/") > 0)
                    folderNode.put("workspace.type", "PRIVATE");
                else
                if(nodeObject.getOuid().indexOf("/PUBLIC/FOLDER/") > 0)
                {
                    folderNode.put("workspace.type", "PUBLIC");
                } else
                {
                    folderNode.clear();
                    folderNode = null;
                    return;
                }
                result = DynaMOAD.wks.createNode(folderNode);
            }
            catch(Exception re)
            {
                re.printStackTrace();
            }
            if(result)
            {
                treeNode = new DefaultMutableTreeNode(new TreeNodeObject(path + "/" + (String)folderNode.get("name"), (String)folderNode.get("name"), (String)folderNode.get("node.type")));
                setInsertNodeForWorkSpace(node, treeNode);
            } else
            {
                String tmpStr = "";
                tmpStr = DynaMOAD.getMSRString("WRD_0008", "Create", 3);
                Object args[] = {
                    tmpStr
                };
                tmpStr = DynaMOAD.getMSRString("WRN_0001", "", 0);
                MessageFormat form = new MessageFormat(tmpStr);
                String messageStr = form.format(((Object) (args)));
                String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
            }
            folderNode.clear();
            folderNode = null;
        } else
        if(command.equals("add.folder.private"))
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getModel().getRoot();
            if(node == null)
                return;
            TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
            if(nodeObject == null)
                return;
            String folderName = JOptionPane.showInputDialog(workSpaceTree, DynaMOAD.getMSRString("INF_0008", "Folder name: ", 3));
            DefaultMutableTreeNode treeNode = null;
            String path = null;
            HashMap folderNode = new HashMap();
            boolean result = false;
            path = "::/WORKSPACE/" + modelOuid + "/" + "PRIVATE" + "/" + LogIn.userID + "/" + "FOLDER";
            try
            {
                folderNode.put("workspace.type", "PRIVATE");
                folderNode.put("node.type", "FOLDER");
                folderNode.put("parent", "/");
                folderNode.put("name", folderName);
                folderNode.put("value", path);
                result = DynaMOAD.wks.createNode(folderNode);
            }
            catch(Exception re)
            {
                re.printStackTrace();
            }
            if(result)
            {
                treeNode = new DefaultMutableTreeNode(new TreeNodeObject(path + "/" + (String)folderNode.get("name"), (String)folderNode.get("name"), (String)folderNode.get("node.type")));
                setInsertNodeForWorkSpace(node, treeNode);
            } else
            {
                String tmpStr = "";
                tmpStr = DynaMOAD.getMSRString("WRD_0008", "Create", 3);
                Object args[] = {
                    tmpStr
                };
                tmpStr = DynaMOAD.getMSRString("WRN_0001", "", 0);
                MessageFormat form = new MessageFormat(tmpStr);
                String messageStr = form.format(((Object) (args)));
                String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
            }
            folderNode.clear();
            folderNode = null;
        } else
        if(!command.equals("add.folder.shared"))
            if(command.equals("add.folder.public"))
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getModel().getRoot();
                if(node == null)
                    return;
                TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                if(nodeObject == null)
                    return;
                String folderName = JOptionPane.showInputDialog(workSpaceTree, DynaMOAD.getMSRString("INF_0008", "Folder name: ", 3));
                DefaultMutableTreeNode treeNode = null;
                String path = null;
                HashMap folderNode = new HashMap();
                boolean result = false;
                path = "::/WORKSPACE/" + modelOuid + "/" + "PUBLIC" + "/" + "FOLDER";
                try
                {
                    folderNode.put("workspace.type", "PUBLIC");
                    folderNode.put("node.type", "FOLDER");
                    folderNode.put("parent", "/");
                    folderNode.put("name", folderName);
                    folderNode.put("value", path);
                    result = DynaMOAD.wks.createNode(folderNode);
                }
                catch(Exception re)
                {
                    re.printStackTrace();
                }
                if(result)
                {
                    treeNode = new DefaultMutableTreeNode(new TreeNodeObject(path + "/" + (String)folderNode.get("name"), (String)folderNode.get("name"), (String)folderNode.get("node.type")));
                    setInsertNodeForWorkSpace(node, treeNode);
                } else
                {
                    String tmpStr = "";
                    tmpStr = DynaMOAD.getMSRString("WRD_0008", "Create", 3);
                    Object args[] = {
                        tmpStr
                    };
                    tmpStr = DynaMOAD.getMSRString("WRN_0001", "", 0);
                    MessageFormat form = new MessageFormat(tmpStr);
                    String messageStr = form.format(((Object) (args)));
                    String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                    JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
                }
                folderNode.clear();
                folderNode = null;
            } else
            if(command.equals("remove.folder"))
            {
                int selected = JOptionPane.showConfirmDialog(null, DynaMOAD.getMSRString("QST_0001", "Selected folder will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0006", "Question", 3), 0, 3);
                if(selected != 0)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                if(node == null)
                    return;
                TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                if(nodeObject == null)
                    return;
                String path = nodeObject.getOuid();
                HashMap folderNode = new HashMap();
                boolean result = false;
                path = path.substring(path.lastIndexOf("/FOLDER/") + 7);
                try
                {
                    folderNode.put("node.type", "FOLDER");
                    folderNode.put("path", path);
                    if(nodeObject.getOuid().indexOf("/PRIVATE/") > 0)
                        folderNode.put("workspace.type", "PRIVATE");
                    else
                    if(nodeObject.getOuid().indexOf("/PUBLIC/FOLDER/") > 0)
                    {
                        folderNode.put("workspace.type", "PUBLIC");
                    } else
                    {
                        folderNode.clear();
                        folderNode = null;
                        return;
                    }
                    result = DynaMOAD.wks.removeNode(folderNode);
                }
                catch(Exception re)
                {
                    re.printStackTrace();
                }
                if(result)
                {
                    node.removeFromParent();
                    workSpaceTree.updateUI();
                    node = null;
                } else
                {
                    String tmpStr = "";
                    tmpStr = DynaMOAD.getMSRString("WRD_0002", "Delete", 3);
                    Object args[] = {
                        tmpStr
                    };
                    tmpStr = DynaMOAD.getMSRString("WRN_0001", "", 0);
                    MessageFormat form = new MessageFormat(tmpStr);
                    String messageStr = form.format(((Object) (args)));
                    String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                    JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
                }
            } else
            if(command.startsWith("MNU_DELETE"))
            {
                if(!command.equals("MNU_DELETE^"))
                {
                    int selected = JOptionPane.showConfirmDialog(null, DynaMOAD.getMSRString("QST_0002", "Selected link(s) of object will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0006", "Question", 3), 0, 3);
                    if(selected != 0)
                        return;
                }
                TreePath paths[] = workSpaceTree.getSelectionPaths();
                if(paths == null)
                    return;
                DefaultMutableTreeNode node = null;
                String path = null;
                HashMap folderNode = null;
                boolean result = false;
                for(int i = 0; i < paths.length; i++)
                {
                    node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    if(node == null)
                        return;
                    TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                    if(nodeObject == null)
                        return;
                    path = nodeObject.getOuid();
                    folderNode = new HashMap();
                    result = false;
                    path = path.substring(path.lastIndexOf("/FOLDER/") + 7);
                    java.util.List list = Utils.tokenizeMessageWithNoTrim(path, '^');
                    if(list != null && list.size() > 1)
                    {
                        path = (String)list.get(0);
                        list.clear();
                        list = null;
                    }
                    try
                    {
                        folderNode.put("node.type", "OBJECT");
                        folderNode.put("path", path);
                        if(nodeObject.getOuid().indexOf("/PRIVATE/") > 0)
                            folderNode.put("workspace.type", "PRIVATE");
                        else
                        if(nodeObject.getOuid().indexOf("/PUBLIC/FOLDER/") > 0)
                        {
                            folderNode.put("workspace.type", "PUBLIC");
                        } else
                        {
                            folderNode.clear();
                            folderNode = null;
                            return;
                        }
                        result = DynaMOAD.wks.removeNode(folderNode);
                    }
                    catch(Exception re)
                    {
                        re.printStackTrace();
                    }
                    if(result)
                    {
                        node.removeFromParent();
                        node = null;
                    } else
                    {
                        String tmpStr = "";
                        tmpStr = DynaMOAD.getMSRString("WRD_0002", "Delete", 3);
                        Object args[] = {
                            tmpStr
                        };
                        tmpStr = DynaMOAD.getMSRString("WRN_0002", "", 0);
                        MessageFormat form = new MessageFormat(tmpStr);
                        String messageStr = form.format(((Object) (args)));
                        String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                        JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
                    }
                }

                workSpaceTree.updateUI();
            } else
            if(command.equals("Information"))
                information_actionPerformed(e);
            else
            if(command.equals("copy"))
            {
                TreePath paths[] = workSpaceTree.getSelectionPaths();
                if(paths == null)
                    return;
                DefaultMutableTreeNode node = null;
                TreeNodeObject nodeObject = null;
                StringBuffer sb = new StringBuffer();
                String instanceOuid = null;
                try
                {
                    sb.append("[DynaMOAD Folder];");
                    sb.append(paths.length);
                    for(int n = 0; n < paths.length; n++)
                    {
                        node = (DefaultMutableTreeNode)paths[n].getLastPathComponent();
                        if(node != null)
                        {
                            nodeObject = (TreeNodeObject)node.getUserObject();
                            if(nodeObject == null)
                            {
                                nodeObject = null;
                            } else
                            {
                                instanceOuid = nodeObject.getOuid();
                                sb.append(';');
                                sb.append(instanceOuid);
                                nodeObject = null;
                                node = null;
                            }
                        }
                    }

                    if(sb.length() > 0)
                        Utils.copyToClipboard(sb.toString());
                }
                catch(Exception re)
                {
                    re.printStackTrace();
                }
                sb = null;
                paths = (TreePath[])null;
            } else
            if(command.equals("pasteLink.object"))
            {
                if(!Utils.isClipboardContainDOSObjects(this))
                    return;
                String clipString = Utils.pasteFromClipboard(this);
                java.util.List list = Utils.tokenizeMessageWithNoTrim(clipString, ';');
                if(list == null || list.size() < 3)
                    return;
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                if(treeNode == null)
                    return;
                TreeNodeObject nodeObject = (TreeNodeObject)treeNode.getUserObject();
                if(nodeObject == null)
                    return;
                String workspaceType = null;
                String path = nodeObject.getOuid();
                String parentPath = null;
                boolean isFolder = false;
                boolean isObject = false;
                boolean isFilter = false;
                if(path.indexOf("/PUBLIC/FOLDER") > 0)
                    workspaceType = "PUBLIC";
                else
                if(path.indexOf("/PRIVATE/") > 0 && path.indexOf("/FOLDER", path.indexOf("/PRIVATE/")) > 0)
                    workspaceType = "PRIVATE";
                else
                if(path.indexOf("/SHARED/") > 0 && path.indexOf("/FOLDER", parentPath.indexOf("/SHARED/")) > 0)
                    workspaceType = "SHARED";
                else
                    workspaceType = null;
                if(clipString.startsWith("[DynaMOAD Object];"))
                    isObject = true;
                else
                if(clipString.startsWith("[DynaMOAD Folder];"))
                    isFolder = true;
                else
                if(clipString.startsWith("[DynaMOAD Advanced Filter];"))
                    isFilter = true;
                parentPath = path.substring(path.lastIndexOf("/FOLDER/") + 7);
                HashMap folderNode = new HashMap();
                DOSChangeable dos = null;
                java.util.List folderList = null;
                boolean result = false;
                String instanceOuid = null;
                String folderName = null;
                String filterName = null;
                try
                {
                    for(int n = 2; n < list.size(); n++)
                    {
                        instanceOuid = (String)list.get(n);
                        result = false;
                        if(isObject)
                        {
                            dos = this.dos.get(instanceOuid);
                            if(dos == null)
                                continue;
                            folderNode.put("workspace.type", workspaceType);
                            folderNode.put("node.type", "OBJECT");
                            folderNode.put("parent", parentPath);
                            if(dos.get("vf$version") == null)
                                folderNode.put("name", "[" + dos.get("md$number") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                            else
                                folderNode.put("name", "[" + dos.get("md$number") + "/" + dos.get("vf$version") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                            folderNode.put("value", instanceOuid);
                            result = DynaMOAD.wks.createNode(folderNode);
                            folderNode.clear();
                            dos.clear();
                            dos = null;
                        } else
                        if(isFolder)
                        {
                            folderList = Utils.tokenizeMessageWithNoTrim(instanceOuid, '^');
                            if(folderList.size() == 3)
                            {
                                dos = this.dos.get((String)folderList.get(2));
                                if(dos == null)
                                    continue;
                                folderNode.put("workspace.type", workspaceType);
                                folderNode.put("node.type", "OBJECT");
                                folderNode.put("parent", parentPath);
                                if(dos.get("vf$version") == null)
                                    folderNode.put("name", "[" + dos.get("md$number") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                                else
                                    folderNode.put("name", "[" + dos.get("md$number") + "/" + dos.get("vf$version") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                                folderNode.put("value", folderList.get(2));
                                result = DynaMOAD.wks.createNode(folderNode);
                                folderNode.clear();
                                dos.clear();
                                dos = null;
                            } else
                            if(folderList.size() == 1)
                            {
                                folderName = (String)folderList.get(0);
                                folderName = folderName.substring(folderName.lastIndexOf("/FOLDER/") + 7);
                                folderName = folderName.substring(folderName.lastIndexOf('/') + 1);
                                if(parentPath.indexOf(folderName) > 0)
                                    continue;
                                folderNode.put("workspace.type", workspaceType);
                                folderNode.put("node.type", "FOLDERLINK");
                                folderNode.put("parent", parentPath);
                                folderNode.put("name", folderName);
                                folderNode.put("value", folderList.get(0));
                                result = DynaMOAD.wks.createNode(folderNode);
                                if(!result)
                                {
                                    String tmpStr = "";
                                    tmpStr = DynaMOAD.getMSRString("WRD_0008", "Create", 3);
                                    Object args[] = {
                                        tmpStr
                                    };
                                    tmpStr = DynaMOAD.getMSRString("WRN_0001", "", 0);
                                    MessageFormat form = new MessageFormat(tmpStr);
                                    String messageStr = form.format(((Object) (args)));
                                    String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                                    JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
                                }
                                folderNode.clear();
                            }
                            folderList.clear();
                            folderList = null;
                        } else
                        if(isFilter)
                        {
                            java.util.List tokens = Utils.tokenizeMessageWithNoTrim(instanceOuid, '/');
                            filterName = (String)tokens.get(tokens.size() - 1);
                            folderNode.put("workspace.type", workspaceType);
                            folderNode.put("node.type", "LISTFILTER");
                            folderNode.put("parent", parentPath);
                            folderNode.put("name", filterName);
                            folderNode.put("value", instanceOuid);
                            result = DynaMOAD.wks.createNode(folderNode);
                            folderNode.clear();
                            filterName = null;
                        }
                        if(result)
                        {
                            treeNode.removeAllChildren();
                            DynaMOAD.searchCondition4CADIntegration.addFolders(treeNode, workspaceType, "FOLDER", null);
                        }
                    }

                }
                catch(Exception re)
                {
                    re.printStackTrace();
                }
                DynaMOAD.searchCondition4CADIntegration.workSpaceTree.updateUI();
                folderNode = null;
            } else
            if(command.startsWith("ObjectSelectButtonClick"))
            {
                for(int i = 0; i < selectButtonList.size(); i++)
                    if(command.equals(selectButtonList.get(i)))
                    {
                        java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                        String classOuid2 = (String)testList.get(1);
                        if(classOuid2 != null)
                        {
                            SmallSearchDialog newSmall = new SmallSearchDialog(this, (Component)e.getSource(), true, classOuid2, command);
                            newSmall.setVisible(true);
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
                        java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                        String codeOuid = (String)testList.get(1);
                        if(codeOuid != null && !codeOuid.equals("null"))
                        {
                            CodeSelectDialog newSmall = new CodeSelectDialog((JFrame)contentFrame, (Component)e.getSource(), true, codeOuid, command);
                            newSmall.setParentFrame(this);
                            newSmall.setVisible(true);
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
                        java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                        String codeOuid = (String)testList.get(1);
                        if(Utils.isNullString(codeOuid))
                            return;
                        String classOuid = null;
                        JTree tree = getCurrentTree();
                        int mode = 0;
                        if(tree.equals(this.tree))
                            mode = 1;
                        else
                        if(tree.equals(hcodeTree))
                            mode = 2;
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                        if(node == null)
                            return;
                        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
                        if(mode == 1)
                            classOuid = tmpdata.getOuid();
                        else
                        if(mode == 2)
                        {
                            LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                            classOuid = (String)tempList.get(1);
                            tempList.clear();
                            tempList = null;
                        }
                        if(Utils.isNullString(codeOuid))
                            return;
                        try
                        {
                            DOSChangeable instanceData = new DOSChangeable();
                            if(searchConditionData != null)
                                instanceData.setValueMap((HashMap)searchConditionData.get(classOuid));
                            codeOuid = DynaMOAD.dos.lookupCodeFromSelectionTable(codeOuid, instanceData);
                            if(Utils.isNullString(codeOuid))
                                return;
                            CodeSelectDialog newSmall = new CodeSelectDialog((JFrame)contentFrame, (Component)e.getSource(), true, codeOuid, command);
                            newSmall.setParentFrame(this);
                            newSmall.setVisible(true);
                        }
                        catch(Exception ee)
                        {
                            ee.printStackTrace();
                        }
                    }

            } else
            if(command.startsWith("tree view"))
            {
                String tabName = command.substring(command.indexOf('/') + 1);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                if(node == null)
                    return;
                TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                if(nodeObject == null)
                    return;
                if(!"OBJECT".equals(nodeObject.getDescription()))
                {
                    node = null;
                    nodeObject = null;
                    return;
                }
                String selectOuid = nodeObject.getOuid();
                java.util.List list = Utils.tokenizeMessage(selectOuid, '^');
                if(list != null && list.size() > 0)
                {
                    selectOuid = (String)list.get(2);
                    list.clear();
                    list = null;
                }
                if(Utils.isNullString(selectOuid))
                    return;
                try
                {
                    String selectClassOuid = this.dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(this, selectClassOuid, selectOuid, 1);
                    uiGeneration.setSession(_session);
                    boolean results = uiGeneration.setTreeLinkView(tabName);
                    if(!results)
                    {
                        uiGeneration.setVisible(false);
                        uiGeneration.dispose();
                        uiGeneration = null;
                        return;
                    }
                    Object returnObject = Utils.executeScriptFile(this.dos.getEventName(selectClassOuid, "read.before"), dss, uiGeneration);
                    if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                    {
                        uiGeneration.windowClosing(null);
                    } else
                    {
                        uiGeneration.setVisible(true);
                        Utils.executeScriptFile(this.dos.getEventName(selectClassOuid, "read.after"), dss, uiGeneration);
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
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
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
                catch(Exception ee)
                {
                    ee.printStackTrace();
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
                catch(Exception ee)
                {
                    ee.printStackTrace();
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
                catch(Exception ee)
                {
                    ee.printStackTrace();
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
                    if(_session != null && (((Search4CADIntegration)contentFrame).cadType.equals("4") || ((Search4CADIntegration)contentFrame).cadType.equals("5")))
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
            if(command.startsWith("action$"))
                try
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
                    DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();
                    if(node == null && node2 == null)
                        return;
                    String selectOuid = null;
                    if(node != null)
                    {
                        TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                        if(nodeObject == null)
                        {
                            if("OBJECT".equals(nodeObject.getDescription()))
                            {
                                selectOuid = nodeObject.getOuid();
                                java.util.List list = Utils.tokenizeMessage(selectOuid, '^');
                                if(list != null && list.size() > 0)
                                {
                                    selectOuid = (String)list.get(2);
                                    list.clear();
                                    list = null;
                                }
                            }
                            node = null;
                            nodeObject = null;
                        }
                    }
                    if(node2 != null)
                    {
                        TreeNodeObject nodeObject = (TreeNodeObject)node2.getUserObject();
                        if(nodeObject != null)
                        {
                            selectOuid = nodeObject.getOuid();
                            java.util.List list = Utils.tokenizeMessage(selectOuid, '^');
                            if(list != null && list.size() > 2)
                            {
                                selectOuid = (String)list.get(2);
                                list.clear();
                                list = null;
                            }
                        }
                    }
                    if(Utils.isNullString(selectOuid))
                        return;
                    String scriptName = this.dos.getActionScriptName(command.substring(7));
                    if(!Utils.isNullString(scriptName))
                        Utils.executeScriptFile(scriptName, dss, selectOuid);
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
            else
            if(command.startsWith("DateChooserButtonClick"))
            {
                int tmpInt = -1;
                for(int i = 0; i < dateChooserButtonList.size(); i++)
                {
                    if(!command.endsWith((String)dateChooserButtonList.get(i)))
                        continue;
                    tmpInt = i;
                    break;
                }

                if(tmpInt > -1)
                {
                    String fieldName = (String)dateChooserButtonList.get(tmpInt);
                    if(fieldName != null)
                    {
                        new DynaDateChooser((Frame)contentFrame);
                        java.util.Date date = DynaDateChooser.showDialog((Component)e.getSource(), null);
                        if(date != null)
                            setFieldInDateField(sdfYMD.format(date), fieldName);
                        else
                            setFieldInDateField("", fieldName);
                    } else
                    {
                        System.out.println("type class ouid is Null");
                    }
                }
            } else
            if(command.startsWith("hcodeSelectMenu^"))
            {
                LinkedList tempList = (LinkedList)Utils.tokenizeMessage(command, '^');
                if(tempList.size() < 2)
                    return;
                hcodeTreeScrollPane.setViewportView(null);
                hcodeTreeScrollPane.setViewportView(setClassificationCode((String)tempList.get(1)));
                tempList.clear();
                tempList = null;
            } else
            if(command.equals("remove.filter"))
            {
                int selected = JOptionPane.showConfirmDialog(null, DynaMOAD.getMSRString("QST_0002", "Selected link(s) of object will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0006", "Question", 3), 0, 3);
                if(selected != 0)
                    return;
                TreePath paths[] = workSpaceTree.getSelectionPaths();
                if(paths == null)
                    return;
                DefaultMutableTreeNode node = null;
                String path = null;
                HashMap folderNode = null;
                boolean result = false;
                for(int i = 0; i < paths.length; i++)
                {
                    node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                    if(node == null)
                        return;
                    TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
                    if(nodeObject == null)
                        return;
                    path = nodeObject.getOuid();
                    folderNode = new HashMap();
                    result = false;
                    path = path.substring(path.lastIndexOf("/FOLDER/") + 7);
                    try
                    {
                        folderNode.put("node.type", "OBJECT");
                        folderNode.put("path", path);
                        if(nodeObject.getOuid().indexOf("/PRIVATE/") > 0)
                            folderNode.put("workspace.type", "PRIVATE");
                        else
                        if(nodeObject.getOuid().indexOf("/PUBLIC/FOLDER/") > 0)
                        {
                            folderNode.put("workspace.type", "PUBLIC");
                        } else
                        {
                            folderNode.clear();
                            folderNode = null;
                            return;
                        }
                        result = DynaMOAD.wks.removeNode(folderNode);
                    }
                    catch(Exception re)
                    {
                        re.printStackTrace();
                    }
                    if(result)
                    {
                        node.removeFromParent();
                        node = null;
                    } else
                    {
                        String tmpStr = "";
                        tmpStr = DynaMOAD.getMSRString("WRD_0002", "Delete", 3);
                        Object args[] = {
                            tmpStr
                        };
                        tmpStr = DynaMOAD.getMSRString("WRN_0002", "", 0);
                        MessageFormat form = new MessageFormat(tmpStr);
                        String messageStr = form.format(((Object) (args)));
                        String titleStr = DynaMOAD.getMSRString("WRD_0005", "Warning", 3);
                        JOptionPane.showMessageDialog(null, messageStr, titleStr, 2);
                    }
                }

                workSpaceTree.updateUI();
            } else
            if(command.equals("file/multi_checkout"))
                try
                {
                    ArrayList ouidList = getSelectedOuidsForWorkSpaceTree();
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
                        fileList = this.dos.listFile(selectOuid);
                        status = this.dos.getStatus(selectOuid);
                        isUpdatable = LogIn.isAdmin || acl.hasPermission(this.dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
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
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(command.equals("file/multi_checkin"))
                try
                {
                    ArrayList ouidList = getSelectedOuidsForWorkSpaceTree();
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
                        fileList = this.dos.listFile(selectOuid);
                        status = this.dos.getStatus(selectOuid);
                        isUpdatable = LogIn.isAdmin || acl.hasPermission(this.dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
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
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(command.equals("file/multi_cancel_checkout"))
                try
                {
                    ArrayList ouidList = getSelectedOuidsForWorkSpaceTree();
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
                        fileList = this.dos.listFile(selectOuid);
                        status = this.dos.getStatus(selectOuid);
                        isUpdatable = LogIn.isAdmin || acl.hasPermission(this.dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
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
                                        this.dos.cancelCheckoutFile((String)file.get("md$ouid"), file);
                                }
                            }

                        }
                    }

                    if(_session != null && (((Search4CADIntegration)contentFrame).cadType.equals("4") || ((Search4CADIntegration)contentFrame).cadType.equals("5")))
                    {
                        _session.setProperty("dynapdm.cadintegration.drawing", null);
                        _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
    }

    public void information_actionPerformed(ActionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject nodeObject = (TreeNodeObject)node.getUserObject();
        if(nodeObject == null)
            return;
        if(!"OBJECT".equals(nodeObject.getDescription()))
        {
            node = null;
            nodeObject = null;
            return;
        }
        String objectOuid = nodeObject.getOuid();
        java.util.List list = Utils.tokenizeMessage(objectOuid, '^');
        if(list != null && list.size() > 0)
        {
            objectOuid = (String)list.get(2);
            list.clear();
            list = null;
        }
        if(Utils.isNullString(objectOuid))
            return;
        DOSChangeable getDC = null;
        try
        {
            getDC = dos.get(objectOuid);
        }
        catch(Exception ie)
        {
            actionPerformed(new ActionEvent(this, 0, "MNU_DELETE^"));
            return;
        }
        if(getDC == null)
        {
            actionPerformed(new ActionEvent(this, 0, "MNU_DELETE^"));
            return;
        }
        try
        {
            String selectClassOuid = dos.getClassOuid(objectOuid);
            UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, objectOuid, 1);
            uiGeneration.setSession(_session);
            uiGeneration.setVisible(true);
            uiGeneration = null;
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        node = null;
        nodeObject = null;
    }

    public void searchButton_actionPerformed(ActionEvent e)
    {
        treeTabbedPane.setSelectedIndex(1);
        String classOuid = null;
        JTree tree = getCurrentTree();
        int mode = 0;
        if(tree.equals(this.tree))
            mode = 1;
        else
        if(tree.equals(hcodeTree))
            mode = 2;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
        if(mode == 1)
            classOuid = tmpdata.getOuid();
        else
        if(mode == 2)
        {
            LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
            classOuid = (String)tempList.get(1);
            tempList.clear();
            tempList = null;
        }
        try
        {
            HashMap searchCondition = (HashMap)searchConditionData.get(classOuid);
            if(searchCondition == null)
                searchCondition = new HashMap();
            if(!Utils.isNullString(countPerPageTextField.getText()))
            {
                searchCondition.put("search.result.count", countPerPageTextField.getText());
                countPerPage = Integer.parseInt(countPerPageTextField.getText());
            } else
            {
                searchCondition.put("search.result.count", "1000");
                countPerPage = 1000;
            }
            searchCondition.put("search.result.page", String.valueOf(pageNumber));
            for(int i = 0; i < fieldList.size(); i++)
            {
                DOSChangeable fieldData = (DOSChangeable)fieldList.get(i);
                if(fieldData == null || fieldData.getValueMap() == null || fieldData.getValueMap().size() == 0)
                {
                    fieldData = null;
                    continue;
                }
                if(!Utils.getBoolean((Boolean)fieldData.get("is.visible")))
                {
                    fieldData = null;
                    continue;
                }
                String text;
                if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type"))
                {
                    getClass();
                    if(((Byte)fieldData.get("type")).byteValue() != 1)
                    {
                        text = (String)fieldData.get("name");
                        if(text != null && (JTextField)hoardData.get(text) != null)
                        {
                            text = ((JTextField)hoardData.get(text)).getText();
                            if(Utils.isNullString(text))
                                searchCondition.remove((String)fieldData.get("ouid"));
                            else
                            if(((String)fieldData.get("ouid")).equals("md$number") && !text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), text);
                            else
                            if(((String)fieldData.get("ouid")).equals("md$cdate") && !text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), text);
                            else
                            if(((String)fieldData.get("ouid")).equals("md$mdate") && !text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), text);
                            else
                            if(((String)fieldData.get("ouid")).equals("md$user") && !text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), text);
                            else
                            if((((Byte)fieldData.get("type")).byteValue() != 16 && ((Byte)fieldData.get("type")).byteValue() != 24 && ((Byte)fieldData.get("type")).byteValue() != 25 || text.equals("")) && !text.equals(""))
                                searchCondition.put((String)fieldData.get("ouid"), text);
                        }
                        continue;
                    }
                }
                text = (String)fieldData.get("name");
                if(text != null && (JComboBox)hoardData.get(text) != null)
                {
                    text = (String)((JComboBox)hoardData.get(text)).getSelectedItem();
                    if(Utils.isNullString(text))
                        searchCondition.remove((String)fieldData.get("ouid"));
                    else
                    if(fieldData.get("type").toString().equals("1"))
                    {
                        if(text.equals("true"))
                            searchCondition.put((String)fieldData.get("ouid"), new Boolean(true));
                        else
                            searchCondition.put((String)fieldData.get("ouid"), new Boolean(false));
                    } else
                    if("md$status".equals(fieldData.get("code")))
                        searchCondition.put((String)fieldData.get("ouid"), statusValueMap.get(text));
                    else
                    if(text.equals("Last Released"))
                        searchCondition.put((String)fieldData.get("ouid"), "released");
                    else
                    if(text.equals("Work In Progress"))
                        searchCondition.put((String)fieldData.get("ouid"), "wip");
                    else
                    if(text.equals("All Version"))
                        searchCondition.put((String)fieldData.get("ouid"), "all");
                    else
                    if(text.equals("Created Date"))
                    {
                        searchCondition.put((String)fieldData.get("ouid"), "cdate");
                        searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                        searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                    } else
                    if(text.equals("Modified Date"))
                    {
                        searchCondition.put((String)fieldData.get("ouid"), "mdate");
                        searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                        searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                    }
                }
            }

            searchConditionData.put(classOuid, searchCondition);
            ArrayList testList = null;
            if(searchConditionMap == null)
            {
                if(versionableClass && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                    searchCondition.put("version.condition.type", "wip");
                if(mode == 2 && !Utils.isNullArrayList(fieldList))
                {
                    DOSChangeable simpleField = null;
                    DOSChangeable dosField = null;
                    DOSChangeable dosCode = null;
                    String codeOuid = null;
                    String fieldOuid = null;
                    java.util.List tmpList = null;
                    for(int i = 0; i < fieldList.size(); i++)
                    {
                        simpleField = (DOSChangeable)fieldList.get(i);
                        if(Utils.getByte((Byte)simpleField.get("type")) == 24)
                        {
                            fieldOuid = (String)simpleField.get("ouid");
                            dosField = dos.getField(fieldOuid);
                            codeOuid = (String)dosField.get("type.ouid@class");
                            if(!Utils.isNullString(codeOuid))
                                dosCode = dos.getCode(codeOuid);
                            dosField = null;
                            if(dosCode == null)
                            {
                                simpleField = null;
                                continue;
                            }
                            if(Utils.getBoolean((Boolean)dosCode.get("is.hierarchy")))
                            {
                                tmpList = Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                                if(tmpList == null || tmpList.size() < 3 || codeOuid == null)
                                {
                                    simpleField = null;
                                    tmpList = null;
                                    dosCode = null;
                                    continue;
                                }
                                if(codeOuid.equals(tmpList.get(2)))
                                    searchCondition.put(fieldOuid, tmpList.get(0));
                                tmpList = null;
                            }
                        }
                        simpleField = null;
                    }

                }
                testList = dos.list(classOuid, fieldListForSearchResult, searchCondition);
            } else
            {
                if(versionableClass && searchConditionMap.size() > 0 && !searchConditionMap.containsKey("version.condition.type"))
                    searchConditionMap.put("version.condition.type", "wip");
                if(mode == 2 && !Utils.isNullArrayList(fieldList))
                {
                    DOSChangeable simpleField = null;
                    DOSChangeable dosField = null;
                    DOSChangeable dosCode = null;
                    String codeOuid = null;
                    String fieldOuid = null;
                    java.util.List tmpList = null;
                    for(int i = 0; i < fieldList.size(); i++)
                    {
                        simpleField = (DOSChangeable)fieldList.get(i);
                        if(Utils.getByte((Byte)simpleField.get("type")) == 24)
                        {
                            fieldOuid = (String)simpleField.get("ouid");
                            dosField = dos.getField(fieldOuid);
                            codeOuid = (String)dosField.get("type.ouid@class");
                            if(!Utils.isNullString(codeOuid))
                                dosCode = dos.getCode(codeOuid);
                            dosField = null;
                            if(dosCode == null)
                            {
                                simpleField = null;
                                continue;
                            }
                            if(Utils.getBoolean((Boolean)dosCode.get("is.hierarchy")))
                            {
                                tmpList = Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                                if(tmpList == null || tmpList.size() < 3)
                                {
                                    simpleField = null;
                                    tmpList = null;
                                    dosCode = null;
                                    continue;
                                }
                                if(codeOuid.equals(tmpList.get(2)))
                                    searchConditionMap.put(fieldOuid, tmpList.get(0));
                                tmpList = null;
                            }
                        }
                        simpleField = null;
                    }

                }
                testList = dos.list(classOuid, fieldListForSearchResult, searchConditionMap);
            }
            if(((Search4CADIntegration)contentFrame).searchResult4CADIntegration.searchResultTable == null)
                ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.setTableInfo(classOuid);
            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.setResultData(testList);
            ((Search4CADIntegration)contentFrame).setActionButtonList(UIBuilder.createActionToolbarButtons(classOuid, (Search4CADIntegration)contentFrame));
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public JTree getCurrentTree()
    {
        JScrollPane scrollPane = (JScrollPane)treeTabbedPane.getSelectedComponent();
        if(scrollPane != null)
            return (JTree)scrollPane.getViewport().getView();
        else
            return null;
    }

    public void stateChanged(ChangeEvent e)
    {
        workspaceFrame.setToolBar(null);
        ((Search4CADIntegration)contentFrame).setEnabledPreviousSearchButton(false);
        ((Search4CADIntegration)contentFrame).setEnabledNextSearchButton(false);
        if(treeTabbedPane.getSelectedComponent().equals(hcodeTreeScrollPane))
        {
            workspaceFrame.setToolBar(hcodeToolBar);
            if(hcodeTree == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)hcodeTree.getLastSelectedPathComponent();
            if(node == null)
            {
                clearSearchConditionPanel();
                return;
            }
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(node.getLevel() != 0)
            {
                advancedPanelInit();
                java.util.List list = Utils.tokenizeMessage(tmpdata.getOuid(), '^');
                if(list == null)
                    return;
                if(list.size() > 1)
                    ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTable((String)list.get(1));
                list.clear();
                list = null;
            } else
            {
                clearSearchConditionPanel();
            }
        } else
        if(treeTabbedPane.getSelectedComponent().equals(workSpaceScrPane))
        {
            isPrivateWorkSpace = true;
            if(workSpaceTree == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)workSpaceTree.getLastSelectedPathComponent();
            TreeNodeObject tmpdata = null;
            String nodeType = null;
            if(node != null)
            {
                tmpdata = (TreeNodeObject)node.getUserObject();
                nodeType = tmpdata.getDescription();
                if(!"FOLDER".equals(nodeType) && !"OBJECT".equals(nodeType) && !"FOLDERLINK".equals(nodeType) && !"LINKFILTER".equals(nodeType) && !"LISTFILTER".equals(nodeType))
                    ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTableForWorkSpace(tmpdata.getOuid());
            }
            tmpdata = null;
            node = null;
            clearSearchConditionPanel();
        } else
        if(treeTabbedPane.getSelectedComponent().equals(treeScrPane))
        {
            isPrivateWorkSpace = false;
            if(tree == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node == null)
            {
                clearSearchConditionPanel();
                return;
            }
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(node.getLevel() != 0)
            {
                advancedPanelInit();
                ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.makeSearchResultTable(tmpdata.getOuid());
            } else
            {
                clearSearchConditionPanel();
            }
        }
    }

    public void setSearchResultField(ArrayList fields)
    {
        fieldListForSearchResult = fields;
    }

    public void clearSearchConditionPanel()
    {
        buttonPanel = null;
        searchConditionPanel = null;
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(UIManagement.panelBackGround);
        buttonPanel.setPreferredSize(new Dimension(270, 45));
        searchConditionPanel = new JPanel();
        searchConditionPanel.setLayout(null);
        searchConditionPanel.setBackground(UIManagement.panelBackGround);
        searchConditionPanel.setMinimumSize(new Dimension(270, 254));
        searchConditionPanel.setMaximumSize(new Dimension(270, 254));
        searchConditionPanel.setPreferredSize(new Dimension(270, 254));
        searchConditionScrPane.setViewportView(searchConditionPanel);
        searchConditionScrPane.setColumnHeaderView(buttonPanel);
        if(((Search4CADIntegration)contentFrame).searchResult4CADIntegration.tableScrPane != null)
        {
            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.tableScrPane.removeAll();
            ((Search4CADIntegration)contentFrame).searchResult4CADIntegration.updateUI();
        }
    }

    public void setFieldInObject(String sign, String selectOuid, String number)
    {
        String classOuid = null;
        JTree tree = getCurrentTree();
        int mode = 0;
        if(tree.equals(this.tree))
            mode = 1;
        else
        if(tree.equals(hcodeTree))
            mode = 2;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
        if(mode == 1)
            classOuid = tmpdata.getOuid();
        else
        if(mode == 2)
        {
            LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
            classOuid = (String)tempList.get(1);
            tempList.clear();
            tempList = null;
        }
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '/');
        String mapId = (String)list.get(2);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(number);
        HashMap tmpMap = (HashMap)searchConditionData.get(classOuid);
        if(tmpMap == null)
            tmpMap = new HashMap();
        tmpMap.put((String)hoardData.get(mapId + "^ouid"), selectOuid);
        searchConditionData.put(classOuid, tmpMap);
        field = null;
        list.clear();
        list = null;
    }

    public void setFieldInCode(String sign, String selectOuid, String name)
    {
        String classOuid = null;
        JTree tree = getCurrentTree();
        int mode = 0;
        if(tree.equals(this.tree))
            mode = 1;
        else
        if(tree.equals(hcodeTree))
            mode = 2;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
        if(mode == 1)
            classOuid = tmpdata.getOuid();
        else
        if(mode == 2)
        {
            LinkedList tempList = (LinkedList)Utils.tokenizeMessage(tmpdata.getOuid(), '^');
            classOuid = (String)tempList.get(1);
            tempList.clear();
            tempList = null;
        }
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '/');
        String mapId = (String)Utils.tokenizeMessage((String)list.get(2), '^').get(0);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(name);
        HashMap tmpMap = (HashMap)searchConditionData.get(classOuid);
        if(tmpMap == null)
            tmpMap = new HashMap();
        tmpMap.put((String)hoardData.get(mapId + "^ouid"), selectOuid);
        searchConditionData.put(classOuid, tmpMap);
        field = null;
        list.clear();
        list = null;
    }

    private void refreshActionMenu(JPopupMenu popup, String ouid)
    {
        if(actionMenuCheck == null)
            actionMenuCheck = new LinkedList();
        if(Utils.isNullString(ouid))
            return;
        ArrayList fieldGroupList = null;
        ArrayList actionList = null;
        Iterator actionKey = null;
        DOSChangeable object = null;
        DOSChangeable action = null;
        String tmpClassOuid = null;
        String tempString = null;
        JMenu tempMenu = null;
        JMenuItem tempMenuItem = null;
        try
        {
            if(ouid.indexOf('$') > 0)
                tmpClassOuid = dos.getClassOuid(ouid);
            else
                tmpClassOuid = ouid;
            if(Utils.isNullString(tmpClassOuid))
                return;
            if(dos.getClass(ouid) == null)
                return;
            if(tmpClassOuid.startsWith("::"))
                return;
            if("PrivateWorkSpace".equals(tmpClassOuid) || "Sent".equals(tmpClassOuid) || "Inbox".equals(tmpClassOuid) || "Completed".equals(tmpClassOuid) || "Recent".equals(tmpClassOuid))
                return;
            fieldGroupList = dos.listFieldGroupInClass(tmpClassOuid);
            if(Utils.isNullArrayList(fieldGroupList))
                return;
            if(actionMenuCheck != null && actionMenuCheck.size() > 0)
            {
                for(int x = 0; x < actionMenuCheck.size(); x++)
                {
                    tempMenu = (JMenu)actionMenuCheck.get(x);
                    popup.remove(tempMenu);
                    Component components[] = tempMenu.getComponents();
                    for(int y = 0; y < components.length; y++)
                    {
                        tempMenuItem = (JMenuItem)components[y];
                        tempMenuItem.removeActionListener(this);
                    }

                    components = (Component[])null;
                    tempMenu.removeAll();
                    tempMenu = null;
                }

                actionMenuCheck.clear();
            }
            Iterator fieldGroupKey;
            for(fieldGroupKey = fieldGroupList.iterator(); fieldGroupKey.hasNext();)
            {
                object = (DOSChangeable)fieldGroupKey.next();
                if(object != null)
                {
                    tempString = (String)object.get("name");
                    if(!Utils.isNullString(tempString))
                    {
                        if(!tempString.startsWith("popedupmenu$"))
                            fieldGroupKey.remove();
                        object = null;
                    }
                }
            }

            fieldGroupKey = null;
            if(Utils.isNullArrayList(fieldGroupList))
                return;
            Object separator = popup.getComponent(popup.getComponentCount() - 1);
            if(!(separator instanceof JSeparator))
                popup.add(new JSeparator());
            separator = null;
            for(fieldGroupKey = fieldGroupList.iterator(); fieldGroupKey.hasNext();)
            {
                object = (DOSChangeable)fieldGroupKey.next();
                actionList = (ArrayList)object.get("array$ouid@action");
                if(!Utils.isNullArrayList(actionList))
                {
                    tempMenu = new JMenu((String)object.get("title"));
                    for(actionKey = actionList.iterator(); actionKey.hasNext();)
                    {
                        tempString = (String)actionKey.next();
                        action = dos.getAction(tempString);
                        if(action != null)
                        {
                            tempMenuItem = new JMenuItem((String)action.get("title"));
                            tempMenuItem.setActionCommand("action$" + tempString);
                            tempMenuItem.setToolTipText((String)action.get("description"));
                            tempMenuItem.addActionListener(this);
                            tempMenu.add(tempMenuItem);
                            tempString = null;
                        }
                    }

                    actionKey = null;
                    popup.add(tempMenu);
                    actionMenuCheck.add(tempMenu);
                    tempMenu = null;
                    actionList.clear();
                    actionList = null;
                    object = null;
                }
            }

            fieldGroupList.clear();
            fieldGroupList = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    public void makeHierarchyCodeTree(String modelOuid)
    {
        if(hcodeTreeScrollPane == null)
        {
            hcodeTreeScrollPane = UIFactory.createStrippedScrollPane(null);
            hcodeSelectButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/classificationSelect.gif")));
            hcodeSelectButton.setActionCommand("hcodeSelectButton");
            hcodeSelectButton.addActionListener(this);
            hcodeSelectPopupMenu = new JPopupMenu();
            hcodeSelectPopupButton = new PopupButton(hcodeSelectButton, hcodeSelectPopupMenu);
            hcodeToolBar = new ExtToolBar("hierarchy code tool bar");
            hcodeToolBar.add(hcodeSelectPopupButton);
        }
        if(Utils.isNullString(this.modelOuid))
            return;
        try
        {
            ArrayList hcodeList = dos.listCode(modelOuid);
            if(Utils.isNullArrayList(hcodeList))
            {
                treeTabbedPane.remove(hcodeTreeScrollPane);
                workspaceFrame.setToolBar(null);
                if(hcodeTree != null)
                    hcodeTree.removeTreeSelectionListener(this);
                return;
            }
            DOSChangeable dosCode = null;
            ArrayList classificationList = new ArrayList();
            Iterator listKey;
            for(listKey = hcodeList.iterator(); listKey.hasNext();)
            {
                dosCode = (DOSChangeable)listKey.next();
                if(Utils.getBoolean((Boolean)dosCode.get("is.navigator")))
                    classificationList.add(dosCode);
                dosCode = null;
            }

            listKey = null;
            if(Utils.isNullArrayList(classificationList))
            {
                treeTabbedPane.remove(hcodeTreeScrollPane);
                workspaceFrame.setToolBar(null);
                if(hcodeTree != null)
                    hcodeTree.removeTreeSelectionListener(this);
                return;
            }
            hcodeTree = setClassificationCode((String)((DOSChangeable)classificationList.get(0)).get("ouid"));
            hcodeTreeScrollPane.setViewportView(hcodeTree);
            treeTabbedPane.addTab(DynaMOAD.getMSRString("WRD_0044", "Classification", 0), new ImageIcon("icons/classification.gif"), hcodeTreeScrollPane);
            if(hcodeSelectPopupMenu != null)
            {
                hcodeSelectPopupMenu.removeAll();
                for(listKey = classificationList.iterator(); listKey.hasNext();)
                {
                    dosCode = (DOSChangeable)listKey.next();
                    hcodeSelectMenu = new JMenuItem((String)dosCode.get("name"));
                    hcodeSelectMenu.setToolTipText((String)dosCode.get("description"));
                    hcodeSelectMenu.addActionListener(this);
                    hcodeSelectMenu.setActionCommand("hcodeSelectMenu^" + (String)dosCode.get("ouid"));
                    hcodeSelectPopupMenu.add(hcodeSelectMenu);
                    hcodeSelectMenu = null;
                    dosCode = null;
                }

                listKey = null;
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    public JTree setClassificationCode(String codeOuid)
    {
        if(Utils.isNullString(codeOuid))
            return null;
        try
        {
            if(hcodeTree != null)
            {
                hcodeTree.removeTreeSelectionListener(this);
                hcodeTree.removeMouseListener(popupListener);
                hcodeTree = null;
            }
            DOSChangeable rootItem = dos.getCodeItemRoot(codeOuid);
            if(rootItem == null)
                return null;
            TreeNodeObject rootObject = new TreeNodeObject((String)rootItem.get("ouid") + "^" + (String)rootItem.get("filter") + "^" + (String)rootItem.get("ouid@code"), (String)rootItem.get("name"), "Class");
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootObject);
            hcodeTree = new JTree(rootNode);
            hcodeTree.addTreeSelectionListener(this);
            if(treeRenderer == null)
                treeRenderer = new DFDefaultTreeCellRenderer();
            hcodeTree.setCellRenderer(treeRenderer);
            if(popupListener == null)
                popupListener = new PopupListener();
            hcodeTree.addMouseListener(popupListener);
            populateHierarchyTreeNode(rootNode, true);
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        return hcodeTree;
    }

    private void populateHierarchyTreeNode(DefaultMutableTreeNode treeNode, boolean forceMode)
    {
        if(treeNode == null || hcodeTree == null)
            return;
        TreeNodeObject codedata = (TreeNodeObject)treeNode.getUserObject();
        if(codedata.isPopulated() && !forceMode)
            return;
        if(forceMode)
            treeNode.removeAllChildren();
        DefaultMutableTreeNode itemNode = null;
        TreeNodeObject itemNodeObject = null;
        DOSChangeable codeItem = null;
        try
        {
            LinkedList tempList = (LinkedList)Utils.tokenizeMessage(codedata.getOuid(), '^');
            ArrayList itemChildren = dos.getCodeItemChildren((String)tempList.get(0));
            if(Utils.isNullArrayList(itemChildren))
                return;
            Iterator itemKey;
            for(itemKey = itemChildren.iterator(); itemKey.hasNext();)
            {
                codeItem = (DOSChangeable)itemKey.next();
                if(codeItem == null)
                    return;
                itemNodeObject = new TreeNodeObject((String)codeItem.get("ouid") + "^" + (String)codeItem.get("filter") + "^" + (String)codeItem.get("ouid@code"), (String)codeItem.get("name"), "Class");
                itemNode = new DefaultMutableTreeNode(itemNodeObject);
                treeNode.add(itemNode);
                itemNode = null;
                itemNodeObject = null;
                codeItem = null;
            }

            itemKey = null;
            itemChildren.clear();
            itemChildren = null;
            codedata.setPopulated(true);
            if(treeNode.getLevel() == 0)
                hcodeTree.fireTreeExpanded(new TreePath(treeNode.getPath()));
            else
                hcodeTree.fireTreeCollapsed(new TreePath(treeNode.getPath()));
        }
        catch(IIPRequestException e1)
        {
            e1.printStackTrace();
        }
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode selnode = null;
        if(e.getSource().equals(hcodeTree))
        {
            selnode = (DefaultMutableTreeNode)hcodeTree.getLastSelectedPathComponent();
            if(selnode == null)
                return;
            populateHierarchyTreeNode(selnode, false);
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

    public int getCountPerPage()
    {
        return countPerPage;
    }

    private ArrayList getSelectedOuidsForWorkSpaceTree()
    {
        String objectOuid = null;
        DefaultMutableTreeNode node = null;
        TreeNodeObject nodeObject = null;
        TreePath paths[] = workSpaceTree.getSelectionPaths();
        if(paths == null)
            return null;
        ArrayList ouidList = new ArrayList(paths.length);
        for(int i = 0; i < paths.length; i++)
        {
            node = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
            if(node != null)
            {
                nodeObject = (TreeNodeObject)node.getUserObject();
                if(nodeObject != null)
                    if(!"OBJECT".equals(nodeObject.getDescription()))
                    {
                        node = null;
                        nodeObject = null;
                    } else
                    {
                        objectOuid = nodeObject.getOuid();
                        java.util.List list = Utils.tokenizeMessage(objectOuid, '^');
                        if(list != null && list.size() > 0)
                        {
                            objectOuid = (String)list.get(2);
                            list.clear();
                            list = null;
                        }
                        if(!Utils.isNullString(objectOuid))
                            ouidList.add(objectOuid);
                    }
            }
        }

        if(ouidList.size() == 0)
            ouidList = null;
        return ouidList;
    }

    private DOS dos;
    private NDS nds;
    private ACL acl;
    private DTM dtm;
    private UIManagement newUI;
    private DSS dss;
    private WFM wfm;
    private ClientUtil clientUtil;
    final int MODIFY_MODE = 1;
    private BorderLayout conditionBorderLayout;
    private JSplitPane conditionSplitPane;
    private MInternalFrame workspaceFrame;
    public JTabbedPane treeTabbedPane;
    private JScrollPane workSpaceScrPane;
    private JScrollPane treeScrPane;
    private JScrollPane hcodeTreeScrollPane;
    public JTree workSpaceTree;
    private JTree tree;
    private JTree hcodeTree;
    private JToolBar hcodeToolBar;
    private PopupButton hcodeSelectPopupButton;
    private JButton hcodeSelectButton;
    private JPopupMenu hcodeSelectPopupMenu;
    private JMenuItem hcodeSelectMenu;
    private DFDefaultTreeCellRenderer treeRenderer;
    private DefaultMutableTreeNode topNode;
    public DefaultTreeModel treeModel;
    private TreeNodeObject topnode;
    private MInternalFrame searchConditionFrame;
    private JScrollPane searchConditionScrPane;
    private JPanel buttonPanel;
    private JPanel searchConditionPanel;
    private JButton searchButton;
    private JButton conditionSaveButton;
    private JButton clearButton;
    private JLabel listFilterLabel;
    private JComboBox listFilterComboBox;
    private JLabel countPerPageLabel;
    private JTextField countPerPageTextField;
    private JPopupMenu popupMenuForWorkSpace;
    private JMenuItem deleteMenuForWorkSpace;
    private JMenuItem addFolderMenuItem;
    private JMenuItem addPrivateFolderMenuItem;
    private JMenuItem addSharedFolderMenuItem;
    private JMenuItem addPublicFolderMenuItem;
    private JMenuItem removeFolderMenuItem;
    private JMenuItem removeFilterMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private String modelOuid;
    private String classOuid;
    public Object contentFrame;
    private DOSChangeable hoardData;
    private ArrayList fieldListForSearchResult;
    public DefaultMutableTreeNode selectedNode;
    private ArrayList fieldList;
    private DOSChangeable searchConditionData;
    private ArrayList dateChooserButtonList;
    private ArrayList selectButtonList;
    private HashMap searchConditionMap;
    private String classOuidForFilter;
    private FilterDialogForList filterDialog;
    private boolean versionableClass;
    private Session _session;
    private int selectCnt;
    private ArrayList filterList;
    private MouseListener popupListener;
    private int pageNumber;
    private int countPerPage;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    public boolean isPrivateWorkSpace;
    public ArrayList inboxList;
    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;
    private LinkedList actionMenuCheck;
    private final String DELETE_MENU = DynaMOAD.getMSRString("WRD_0002", "Delete", 3);
    private final int init_xcord = 10;
    private final int init_ycord = 35;
    private final int button_xcord = 15;
    private final int titleWidth = 100;
    private final int totalWidth = 260;
    private final int fieldHeight = 20;
    private final int dateButtonWidth = 20;
    private final int offset = 3;
    private final int buttonOffset = 10;
    private final int condition_xsize = 270;
    private final int condition_ysize = 254;
    private final int buttonWidth = 76;
    private final int smallButtonWidth = 76;
    private final int buttonHeight = 24;
    private String NDS_CODE;
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_UTF = 13;
    private final byte DATATYPE_BOOLEAN = 1;


























}