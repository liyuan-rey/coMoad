// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectModelingDefinition.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ManageTreeInterface, ObjectModelingConstruction, ObjectModelDefaultTreeCellRenderer, ObjectModelDefaultTreeCellEditor, 
//            PackageInformation, ObjectInsertFrame, ClassInformation, FieldInformation, 
//            ActionInformation, FieldGroupInformation, PackageSelection

public class ObjectModelingDefinition extends JPanel
    implements ManageTreeInterface
{
    class MyTreeModelListener
        implements TreeModelListener
    {

        public void treeNodesChanged(TreeModelEvent e)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getTreePath().getLastPathComponent();
            try
            {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)node.getChildAt(index);
            }
            catch(NullPointerException nullpointerexception) { }
            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }

        public void treeNodesInserted(TreeModelEvent treemodelevent)
        {
        }

        public void treeNodesRemoved(TreeModelEvent treemodelevent)
        {
        }

        public void treeStructureChanged(TreeModelEvent treemodelevent)
        {
        }

        MyTreeModelListener()
        {
        }
    }

    class ObjectTreeSelectionListener
        implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent treeselectionevent)
        {
        }

        ObjectTreeSelectionListener()
        {
        }
    }

    class WBSMouseListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isRightMouseButton(e))
                tree.setSelectionPath(tree.getClosestPathForLocation(e.getX(), e.getY()));
        }

        WBSMouseListener()
        {
        }
    }

    class PopupListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(tree == null)
                return;
            node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            sharePackageMenu.setVisible(false);
            unlinkPackageMenu.setVisible(false);
            deleteSeparator.setVisible(false);
            deleteCascadeMenu.setVisible(false);
            if(node.getLevel() == 0)
            {
                addMenuAdd.setEnabled(true);
                copyMenu.setEnabled(false);
                graphAddMenu.setEnabled(false);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Package");
                sharePackageMenu.setVisible(true);
            } else
            if(node.getLevel() == 1)
            {
                addMenuAdd.setEnabled(true);
                copyMenu.setEnabled(true);
                graphAddMenu.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Class");
                deleteMenu.setVisible(true);
                unlinkPackageMenu.setVisible(true);
            } else
            if(node.getLevel() == 2)
            {
                addMenuAdd.setEnabled(false);
                addMenuAdd.setVisible(false);
                addMenuAdd.setText("Add");
                deleteMenu.setVisible(true);
                deleteSeparator.setVisible(true);
                deleteCascadeMenu.setVisible(true);
            } else
            if(node.getLevel() == 3 && tmpdata.getDescription().equals("Action Folder"))
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Action");
                deleteMenu.setVisible(false);
            } else
            if(node.getLevel() == 3 && tmpdata.getDescription().equals("Field Folder"))
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Field");
                deleteMenu.setVisible(false);
            } else
            if(node.getLevel() == 3 && tmpdata.getDescription().equals("FGroup Folder"))
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Field Group");
                deleteMenu.setVisible(false);
            } else
            if(node.getLevel() == 4)
            {
                addMenuAdd.setEnabled(false);
                addMenuAdd.setVisible(false);
                addMenuAdd.setText("Add");
                deleteMenu.setVisible(true);
            }
            showPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            showPopup(e);
        }

        private void showPopup(MouseEvent e)
        {
            if(e.getClickCount() == 1 && e.isPopupTrigger())
                if(contentFrame instanceof ObjectModelingConstruction)
                {
                    Dimension framesize = ((ObjectModelingConstruction)contentFrame).getSize();
                    Dimension popupsize = popupMenu.getSize();
                    int dividerLocation = ((ObjectModelingConstruction)contentFrame).splitPane.getDividerLocation();
                    if(popupsize.width <= 0 || popupsize.height <= 0)
                        popupsize = new Dimension(107, 100);
                    Point point = SwingUtilities.convertPoint(e.getComponent(), e.getX(), e.getY(), (ObjectModelingConstruction)contentFrame);
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

        PopupListener()
        {
            node = null;
        }
    }

    class TreeMouseListener extends MouseAdapter
    {

        public void mouseClicked(MouseEvent mouseevent)
        {
        }

        public void mousePressed(MouseEvent evt)
        {
            tree_mouseClicked(evt);
            getSelectedOuid();
        }

        TreeMouseListener()
        {
        }
    }

    class CellEditorCommon
        implements CellEditorListener
    {

        public void editingStopped(ChangeEvent changeevent)
        {
        }

        public void editingCanceled(ChangeEvent changeevent)
        {
        }

        CellEditorCommon()
        {
        }
    }

    class ListenerCommon
        implements ActionListener
    {

        public void actionPerformed(ActionEvent evt)
        {
            String command = evt.getActionCommand();
            if(command.equals("InformationMenu"))
                informationMenu_ActionPerformed(evt);
            else
            if(command.equals("AddMenu"))
                addMenu_ActionPerformed(evt);
            else
            if(command.equals("CopyMenu"))
                copyMenu_ActionPerformed(evt);
            else
            if(command.equals("DeleteMenu"))
                deleteMenu_ActionPerformed(evt);
            else
            if(command.equals("DeleteCascadeMenu"))
                deleteMenu_ActionPerformed(evt);
            else
            if(command.equals("PasteMenu"))
                pasteMenu_ActionPerformed(evt);
            else
            if(command.equals("GraphAddMenu"))
                graphAddMenu_ActionPerformed(evt);
            else
            if(command.equals("SharePackage"))
            {
                PackageSelection packageSelectWnd = new PackageSelection((ObjectModelingConstruction)contentFrame);
                packageSelectWnd.setVisible(true);
            } else
            if(command.equals("UnlinkPackage"))
                try
                {
                    String workingModelOuid = dos.getWorkingModel();
                    DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                    if(selnode == null)
                        return;
                    TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
                    dos.unlinkPackageAndModel(workingModelOuid, codedata.getOuid());
                    DOSChangeable dosModel = dos.getModel(workingModelOuid);
                    String ouid = (String)dosModel.get("ouid");
                    String name = (String)dosModel.get("name");
                    TreeNodeObject rootNode = new TreeNodeObject(ouid, name, "Model");
                    if(contentFrame instanceof ObjectModelingConstruction)
                    {
                        ((ObjectModelingConstruction)contentFrame).makeObjectSetTree(rootNode, 1);
                        ((ObjectModelingConstruction)contentFrame).displayInformation(ouid, "", 0, 0);
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(command.equals("search"))
                searchButton_ActionPerformed(evt);
        }

        ListenerCommon()
        {
        }
    }


    public ObjectModelingDefinition(Object obj)
    {
        isAddPopup = false;
        waitCursor = new Cursor(3);
        copyNode = null;
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        treeViewScrPane = UIFactory.createStrippedScrollPane(null);
        copyData = null;
        tree = null;
        topNode = null;
        treeModel = null;
        topPartCode = null;
        tempData = new Vector(20, 5);
        contentFrame = null;
        packageInfoDC = null;
        classListInfoDC = null;
        codeListInfoDC = null;
        classInfoDC = null;
        fieldInfoDC = null;
        actionInfoDC = null;
        fieldGroupInfoDC = null;
        dos = null;
        topnode = null;
        classSelectedOuid = "";
        classSelectedOuidTemp = "";
        fieldSelectedOuid = "";
        fieldSelectedOuidTemp = "";
        actionSelectedOuid = "";
        actionSelectedOuidTemp = "";
        fieldGroupSelectedOuid = "";
        fieldGroupSelectedOuidTemp = "";
        contentFrame = obj;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            initialize();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectModelingDefinition(Object obj, TreeNodeObject nodeObj)
    {
        isAddPopup = false;
        waitCursor = new Cursor(3);
        copyNode = null;
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        treeViewScrPane = UIFactory.createStrippedScrollPane(null);
        copyData = null;
        tree = null;
        topNode = null;
        treeModel = null;
        topPartCode = null;
        tempData = new Vector(20, 5);
        contentFrame = null;
        packageInfoDC = null;
        classListInfoDC = null;
        codeListInfoDC = null;
        classInfoDC = null;
        fieldInfoDC = null;
        actionInfoDC = null;
        fieldGroupInfoDC = null;
        dos = null;
        topnode = null;
        classSelectedOuid = "";
        classSelectedOuidTemp = "";
        fieldSelectedOuid = "";
        fieldSelectedOuidTemp = "";
        actionSelectedOuid = "";
        actionSelectedOuidTemp = "";
        fieldGroupSelectedOuid = "";
        fieldGroupSelectedOuidTemp = "";
        contentFrame = obj;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            topNode = new DefaultMutableTreeNode(nodeObj);
            setTopNode(nodeObj);
            setClassTreeExpand(topNode);
            initialize();
            treeViewScrPane.setSize(200, 200);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectModelingDefinition(Object obj, TreeNodeObject nodeObj, String classOuid, int option)
    {
        isAddPopup = false;
        waitCursor = new Cursor(3);
        copyNode = null;
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        treeViewScrPane = UIFactory.createStrippedScrollPane(null);
        copyData = null;
        tree = null;
        topNode = null;
        treeModel = null;
        topPartCode = null;
        tempData = new Vector(20, 5);
        contentFrame = null;
        packageInfoDC = null;
        classListInfoDC = null;
        codeListInfoDC = null;
        classInfoDC = null;
        fieldInfoDC = null;
        actionInfoDC = null;
        fieldGroupInfoDC = null;
        dos = null;
        topnode = null;
        classSelectedOuid = "";
        classSelectedOuidTemp = "";
        fieldSelectedOuid = "";
        fieldSelectedOuidTemp = "";
        actionSelectedOuid = "";
        actionSelectedOuidTemp = "";
        fieldGroupSelectedOuid = "";
        fieldGroupSelectedOuidTemp = "";
        contentFrame = obj;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            topNode = new DefaultMutableTreeNode(nodeObj);
            setTopNode(nodeObj);
            setFieldAndActionTreeExpand(topNode, classOuid, option);
            initialize();
            treeViewScrPane.setSize(200, 200);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectModelingDefinition(Object obj, JTree treeObj)
    {
        isAddPopup = false;
        waitCursor = new Cursor(3);
        copyNode = null;
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        treeViewScrPane = UIFactory.createStrippedScrollPane(null);
        copyData = null;
        tree = null;
        topNode = null;
        treeModel = null;
        topPartCode = null;
        tempData = new Vector(20, 5);
        contentFrame = null;
        packageInfoDC = null;
        classListInfoDC = null;
        codeListInfoDC = null;
        classInfoDC = null;
        fieldInfoDC = null;
        actionInfoDC = null;
        fieldGroupInfoDC = null;
        dos = null;
        topnode = null;
        classSelectedOuid = "";
        classSelectedOuidTemp = "";
        fieldSelectedOuid = "";
        fieldSelectedOuidTemp = "";
        actionSelectedOuid = "";
        actionSelectedOuidTemp = "";
        fieldGroupSelectedOuid = "";
        fieldGroupSelectedOuidTemp = "";
        try
        {
            contentFrame = obj;
            tree = treeObj;
            initialize();
            setTree();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectModelingDefinition(Object obj, TreeNodeObject nodeObj, String code)
    {
        isAddPopup = false;
        waitCursor = new Cursor(3);
        copyNode = null;
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        treeViewScrPane = UIFactory.createStrippedScrollPane(null);
        copyData = null;
        tree = null;
        topNode = null;
        treeModel = null;
        topPartCode = null;
        tempData = new Vector(20, 5);
        contentFrame = null;
        packageInfoDC = null;
        classListInfoDC = null;
        codeListInfoDC = null;
        classInfoDC = null;
        fieldInfoDC = null;
        actionInfoDC = null;
        fieldGroupInfoDC = null;
        dos = null;
        topnode = null;
        classSelectedOuid = "";
        classSelectedOuidTemp = "";
        fieldSelectedOuid = "";
        fieldSelectedOuidTemp = "";
        actionSelectedOuid = "";
        actionSelectedOuidTemp = "";
        fieldGroupSelectedOuid = "";
        fieldGroupSelectedOuidTemp = "";
        contentFrame = obj;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            topNode = new DefaultMutableTreeNode(nodeObj);
            setTopNode(nodeObj);
            setCodeTreeExpand(topNode);
            initialize();
            treeViewScrPane.setSize(200, 200);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void createPopupMenu()
    {
        if(!isAddPopup)
        {
            addMenuAdd.setText("\uCD94\uAC00");
            informationMenu = new JMenuItem();
            informationMenu.setText("Information");
            informationMenu.setActionCommand("InformationMenu");
            informationMenu.addActionListener(listenerCommon);
            addMenu = new JMenuItem();
            addMenu.setText("Add");
            addMenu.setActionCommand("AddMenu");
            addMenu.addActionListener(listenerCommon);
            copyMenu = new JMenuItem();
            copyMenu.setText("Copy");
            copyMenu.setActionCommand("CopyMenu");
            copyMenu.addActionListener(listenerCommon);
            pasteMenu = new JMenuItem();
            pasteMenu.setText("Paste");
            pasteMenu.setActionCommand("PasteMenu");
            pasteMenu.addActionListener(listenerCommon);
            deleteMenu = new JMenuItem();
            deleteMenu.setText("Delete");
            deleteMenu.setActionCommand("DeleteMenu");
            deleteMenu.addActionListener(listenerCommon);
            graphAddMenu = new JMenuItem();
            graphAddMenu.setText("\uADF8\uB9BC\uC0DD\uC131");
            graphAddMenu.setActionCommand("GraphAddMenu");
            graphAddMenu.addActionListener(listenerCommon);
            sharePackageMenu = new JMenuItem();
            sharePackageMenu.setText("Package Share");
            sharePackageMenu.setActionCommand("SharePackage");
            sharePackageMenu.addActionListener(listenerCommon);
            unlinkPackageMenu = new JMenuItem();
            unlinkPackageMenu.setText("Cancel Package Sahre");
            unlinkPackageMenu.setActionCommand("UnlinkPackage");
            unlinkPackageMenu.addActionListener(listenerCommon);
            deleteSeparator = new JSeparator(0);
            deleteCascadeMenu = new JMenuItem();
            deleteCascadeMenu.setText("Delete(Cascade)");
            deleteCascadeMenu.setActionCommand("DeleteCascadeMenu");
            deleteCascadeMenu.addActionListener(listenerCommon);
            popupMenu.add(informationMenu);
            addMenuAdd.add(addMenu);
            popupMenu.add(addMenuAdd);
            popupMenu.add(sharePackageMenu);
            popupMenu.add(unlinkPackageMenu);
            popupMenu.add(new JSeparator());
            popupMenu.add(deleteMenu);
            popupMenu.add(deleteSeparator);
            popupMenu.add(deleteCascadeMenu);
            isAddPopup = true;
        }
        java.awt.event.MouseListener popupListener = new PopupListener();
        tree.addMouseListener(popupListener);
    }

    public String getCopyData()
    {
        return null;
    }

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    void setInsertNodeChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    boolean isAdmin()
    {
        return true;
    }

    public DefaultMutableTreeNode getSelectedNode()
    {
        if(tree == null)
            return null;
        else
            return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }

    public DefaultMutableTreeNode setInsertNode(DefaultMutableTreeNode parentnode, DefaultMutableTreeNode childnode, int i)
    {
        if(parentnode.getChildCount() < i || i < 0)
        {
            return null;
        } else
        {
            DefaultTreeModel treemodel = (DefaultTreeModel)tree.getModel();
            treemodel.insertNodeInto(childnode, parentnode, i);
            tree.scrollPathToVisible(new TreePath(childnode.getPath()));
            return childnode;
        }
    }

    String getStepForStatus(String processoid)
    {
        return null;
    }

    public JTree getTree()
    {
        return tree;
    }

    public void setTopNode(TreeNodeObject topnode)
    {
        this.topnode = topnode;
        ObjectTreeSelectionListener treeSelListener = new ObjectTreeSelectionListener();
        WBSMouseListener wbsMouseListener = new WBSMouseListener();
        if(tree != null)
        {
            tree.removeTreeSelectionListener(treeSelListener);
            tree.removeMouseListener(wbsMouseListener);
            tree.removeAll();
            tree = null;
        }
        topNode = new DefaultMutableTreeNode(topnode);
        treeModel = new DefaultTreeModel(topNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        makeTree();
        tree.addTreeSelectionListener(treeSelListener);
        tree.addMouseListener(wbsMouseListener);
    }

    public void setTree()
    {
        if(tree == null)
        {
            return;
        } else
        {
            ObjectModelDefaultTreeCellRenderer renderer = new ObjectModelDefaultTreeCellRenderer();
            ObjectModelDefaultTreeCellEditor editor = new ObjectModelDefaultTreeCellEditor(tree, renderer);
            tree.setCellRenderer(renderer);
            tree.getSelectionModel().setSelectionMode(1);
            tree.putClientProperty("JTree.lineStyle", "Angled");
            treeViewScrPane.setViewportView(tree);
            return;
        }
    }

    public void makeTree()
    {
        if(topNode == null)
            return;
        TreeMouseListener treeMouseListener = new TreeMouseListener();
        tree.addMouseListener(treeMouseListener);
        ObjectTreeSelectionListener treeSelListener = new ObjectTreeSelectionListener();
        WBSMouseListener wbsMouseListener = new WBSMouseListener();
        tree.addTreeSelectionListener(treeSelListener);
        tree.addMouseListener(wbsMouseListener);
        if(contentFrame instanceof ObjectModelingConstruction)
            createPopupMenu();
        setTree();
    }

    public void setFullTreeExpand(DefaultMutableTreeNode node)
    {
        if(node == null)
        {
            return;
        } else
        {
            setExpand(node);
            tree.setSelectionRow(0);
            return;
        }
    }

    public void setClassTreeExpand(DefaultMutableTreeNode node)
    {
        if(node == null)
        {
            return;
        } else
        {
            setExpandClass(node);
            tree.setSelectionRow(0);
            return;
        }
    }

    public void setFieldAndActionTreeExpand(DefaultMutableTreeNode node, String classOuid, int option)
    {
        if(node == null)
        {
            return;
        } else
        {
            setExpandFieldAndAction(node, classOuid, option);
            tree.setSelectionRow(0);
            return;
        }
    }

    public void setCodeTreeExpand(DefaultMutableTreeNode node)
    {
        if(node == null)
        {
            return;
        } else
        {
            setExpandCode(node);
            tree.setSelectionRow(0);
            return;
        }
    }

    private void initialize()
        throws Exception
    {
        listenerCommon = new ListenerCommon();
        setLayout(new BorderLayout());
        treeViewScrPane.setViewportView(tree);
        makeTree();
        if(contentFrame instanceof ObjectModelingConstruction)
        {
            add(treeViewScrPane, "Center");
        } else
        {
            makeSearchConditionPanel();
            splitPane = new BorderlessSplitPane(0, treeViewScrPane, searchConditionPanel);
            splitPane.setDividerSize(5);
            splitPane.setResizeWeight(1.0D);
            splitPane.setOneTouchExpandable(true);
            splitPane.setContinuousLayout(true);
            add(splitPane, "Center");
        }
    }

    private void setExpandClass(DefaultMutableTreeNode node)
    {
        String ouid = "";
        ArrayList classListAll = new ArrayList();
        classListInfoDC = new DOSChangeable();
        try
        {
            ArrayList packageListAL = dos.listPackage();
            for(int i = 0; i < packageListAL.size(); i++)
            {
                ArrayList classListAL = dos.listClassInPackage((String)((DOSChangeable)packageListAL.get(i)).get("ouid"));
                for(int j = 0; j < classListAL.size(); j++)
                    classListAll.add(classListAL.get(j));

            }

            if(classListAll != null && !classListAll.isEmpty())
            {
                for(int i = 0; i < classListAll.size(); i++)
                {
                    classListInfoDC = (DOSChangeable)classListAll.get(i);
                    TreeNodeObject codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Class");
                    DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(node, childnode);
                    tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void setExpandFieldAndAction(DefaultMutableTreeNode node, String classOuid, int option)
    {
        String ouid = "";
        TreeNodeObject codedata = null;
        ArrayList classListAL = new ArrayList();
        classListInfoDC = new DOSChangeable();
        DefaultMutableTreeNode childnode = null;
        try
        {
            if(option == 1)
                classListAL = dos.listFieldInClass(classOuid);
            else
            if(option == 2)
                classListAL = dos.listActionInClass(classOuid);
            else
            if(option == 3)
                classListAL = dos.listFieldInClass(classOuid);
            if(classListAL != null && !classListAL.isEmpty())
            {
                for(int i = 0; i < classListAL.size(); i++)
                {
                    classListInfoDC = (DOSChangeable)classListAL.get(i);
                    if(option == 1)
                    {
                        if(((String)classListInfoDC.get("ouid@class")).equals(classOuid))
                            codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Field");
                        else
                            codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Inherited Field");
                    } else
                    if(option == 2)
                    {
                        if(((String)classListInfoDC.get("ouid@class")).equals(classOuid))
                            codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Action");
                        else
                            codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Inherited Action");
                    } else
                    if(option == 3)
                    {
                        Object typeObject = classListInfoDC.get("type");
                        if(typeObject != null && (typeObject instanceof Byte) && ((Byte)typeObject).byteValue() != 24)
                            continue;
                        if(((String)classListInfoDC.get("ouid@class")).equals(classOuid))
                            codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Field");
                        else
                            codedata = new TreeNodeObject((String)classListInfoDC.get("ouid"), (String)classListInfoDC.get("name"), "Inherited Field");
                    }
                    childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(node, childnode);
                    tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void setExpandCode(DefaultMutableTreeNode node)
    {
        String ouid = "";
        codeListInfoDC = new DOSChangeable();
        try
        {
            ArrayList codeListAL = dos.listCode(dos.getWorkingModel());
            if(codeListAL != null && !codeListAL.isEmpty())
            {
                for(int i = 0; i < codeListAL.size(); i++)
                {
                    codeListInfoDC = (DOSChangeable)codeListAL.get(i);
                    TreeNodeObject codedata = new TreeNodeObject((String)codeListInfoDC.get("ouid"), (String)codeListInfoDC.get("name"), "Code");
                    DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(node, childnode);
                    tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void setExpand(DefaultMutableTreeNode node)
    {
        String ouid = "";
        Vector Locv = new Vector();
        Vector tmpVct = new Vector();
        ArrayList packageListAL = new ArrayList();
        packageInfoDC = new DOSChangeable();
        try
        {
            packageListAL = dos.listPackage();
            if(packageListAL != null && !packageListAL.isEmpty())
            {
                for(int i = 0; i < packageListAL.size(); i++)
                {
                    ouid = (String)((DOSChangeable)packageListAL.get(i)).get("ouid");
                    packageInfoDC = dos.getPackage(ouid);
                    if(packageInfoDC != null)
                    {
                        TreeNodeObject codedata = new TreeNodeObject((String)packageInfoDC.get("ouid"), (String)packageInfoDC.get("name"), "Package");
                        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                        setInsertNode(node, childnode);
                        tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                        setExpand(childnode, ouid);
                    }
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void setExpand(DefaultMutableTreeNode node, String packageOuid)
    {
        Vector Locv = new Vector();
        Vector tmpVct = new Vector();
        ArrayList classListAL = new ArrayList();
        classInfoDC = new DOSChangeable();
        try
        {
            classListAL = dos.listClassInPackage(packageOuid);
            if(classListAL != null && !classListAL.isEmpty())
            {
                for(int i = 0; i < classListAL.size(); i++)
                {
                    TreeNodeObject codedata = new TreeNodeObject((String)((DOSChangeable)classListAL.get(i)).get("ouid"), (String)((DOSChangeable)classListAL.get(i)).get("name"), "Class");
                    DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(node, childnode);
                    if(((String)((DOSChangeable)classListAL.get(i)).get("ouid")).equals(classSelectedOuid))
                    {
                        tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                        classSelectedOuidTemp = classSelectedOuid;
                        classSelectedOuid = "";
                    }
                    setExpand(childnode, packageOuid, (String)((DOSChangeable)classListAL.get(i)).get("ouid"));
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void setExpand(DefaultMutableTreeNode node, String packageOuid, String classOuid)
    {
        String ouid = "";
        Vector Locv = new Vector();
        Vector tmpVct = new Vector();
        ArrayList fieldListAL = new ArrayList();
        ArrayList actionAL = new ArrayList();
        ArrayList fieldGroupAL = new ArrayList();
        fieldInfoDC = new DOSChangeable();
        actionInfoDC = new DOSChangeable();
        fieldGroupInfoDC = new DOSChangeable();
        try
        {
            fieldListAL = dos.listFieldInClass(classOuid);
            actionAL = dos.listActionInClass(classOuid);
            fieldGroupAL = dos.listFieldGroupInClass(classOuid);
            TreeNodeObject codedata = new TreeNodeObject(classOuid, "Field", "Field Folder");
            DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(codedata);
            setInsertNode(node, fieldNode);
            codedata = new TreeNodeObject(classOuid, "Action", "Action Folder");
            DefaultMutableTreeNode actionNode = new DefaultMutableTreeNode(codedata);
            setInsertNode(node, actionNode);
            codedata = new TreeNodeObject(classOuid, "Field Group", "FGroup Folder");
            DefaultMutableTreeNode filedGroupNode = new DefaultMutableTreeNode(codedata);
            setInsertNode(node, filedGroupNode);
            if(fieldListAL != null && !fieldListAL.isEmpty())
            {
                for(int i = 0; i < fieldListAL.size(); i++)
                {
                    fieldInfoDC = (DOSChangeable)fieldListAL.get(i);
                    if(((String)fieldInfoDC.get("ouid@class")).equals(classOuid))
                        codedata = new TreeNodeObject((String)fieldInfoDC.get("ouid"), (String)fieldInfoDC.get("name"), "Field");
                    else
                        codedata = new TreeNodeObject((String)fieldInfoDC.get("ouid"), (String)fieldInfoDC.get("name"), "Inherited Field");
                    DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(fieldNode, childnode);
                    if(((String)fieldInfoDC.get("ouid")).equals(fieldSelectedOuid))
                    {
                        tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                        fieldSelectedOuidTemp = fieldSelectedOuid;
                        fieldSelectedOuid = "";
                    }
                }

            }
            if(actionAL != null && !actionAL.isEmpty())
            {
                for(int i = 0; i < actionAL.size(); i++)
                {
                    actionInfoDC = (DOSChangeable)actionAL.get(i);
                    if(((String)actionInfoDC.get("ouid@class")).equals(classOuid))
                    {
                        codedata = new TreeNodeObject((String)actionInfoDC.get("ouid"), (String)actionInfoDC.get("name"), "Action");
                    } else
                    {
                        if(Utils.getBoolean((Boolean)actionInfoDC.get("is.leaf")))
                            continue;
                        codedata = new TreeNodeObject((String)actionInfoDC.get("ouid"), (String)actionInfoDC.get("name"), "Inherited Action");
                    }
                    DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(actionNode, childnode);
                    if(((String)actionInfoDC.get("ouid")).equals(actionSelectedOuid))
                    {
                        tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                        actionSelectedOuidTemp = actionSelectedOuid;
                        actionSelectedOuid = "";
                    }
                }

            }
            if(fieldGroupAL != null && !fieldGroupAL.isEmpty())
            {
                for(int i = 0; i < fieldGroupAL.size(); i++)
                {
                    fieldGroupInfoDC = (DOSChangeable)fieldGroupAL.get(i);
                    if(((String)fieldGroupInfoDC.get("ouid@class")).equals(classOuid))
                        codedata = new TreeNodeObject((String)fieldGroupInfoDC.get("ouid"), (String)fieldGroupInfoDC.get("name"), "FGroup");
                    else
                        codedata = new TreeNodeObject((String)fieldGroupInfoDC.get("ouid"), (String)fieldGroupInfoDC.get("name"), "Inherited FGroup");
                    DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                    setInsertNode(filedGroupNode, childnode);
                    if(((String)fieldGroupInfoDC.get("ouid")).equals(fieldGroupSelectedOuid))
                    {
                        tree.scrollPathToVisible(new TreePath(childnode.getPath()));
                        fieldGroupSelectedOuidTemp = fieldGroupSelectedOuid;
                        fieldGroupSelectedOuid = "";
                    }
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void informationMenu_ActionPerformed(ActionEvent e)
    {
        DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(selnode == null)
            return;
        TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
        if(contentFrame instanceof ObjectModelingConstruction)
            ((ObjectModelingConstruction)contentFrame).displayInformation(codedata.getOuid(), codedata.getDescription(), selnode.getLevel(), ((ObjectModelingConstruction)contentFrame).splitPane.getDividerLocation());
    }

    private void addMenu_ActionPerformed(ActionEvent e)
    {
        addObject("New");
    }

    private void copyMenu_ActionPerformed(ActionEvent e)
    {
        if(copyNode != null)
            copyNode = null;
        copyNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        TreeNodeObject copydata = (TreeNodeObject)copyNode.getUserObject();
    }

    private void graphAddMenu_ActionPerformed(ActionEvent actionevent)
    {
    }

    private void pasteMenu_ActionPerformed(ActionEvent e)
    {
        if(copyNode != null)
        {
            String pasteoid = getCopyData();
            TreeNodeObject copydata = (TreeNodeObject)copyNode.getUserObject();
            DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreeNodeObject seldata = (TreeNodeObject)selnode.getUserObject();
            DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode)selnode.getRoot();
            TreeNodeObject rootdata = (TreeNodeObject)rootnode.getUserObject();
            if(selnode.getLevel() == 0)
            {
                System.out.println(copydata.getOuid());
                return;
            }
            DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(copydata);
            setInsertNode(selnode, newnode, selnode.getChildCount());
        }
    }

    public void deleteAction(ActionEvent e)
    {
        deleteMenu_ActionPerformed(e);
    }

    private void deleteMenu_ActionPerformed(ActionEvent e)
    {
        try
        {
            DefaultTreeModel treemodel = (DefaultTreeModel)tree.getModel();
            DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode)treemodel.getRoot();
            DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
            if(selnode == null)
                return;
            Object option[] = {
                "Yes", "No"
            };
            int res = JOptionPane.showOptionDialog(this, "Selected object will be deleted. Are you sure?", "Confirmation", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[1]);
            if(res != 0)
                return;
            String command = e.getActionCommand();
            if(selnode.getLevel() == 0)
            {
                dos.removeModel(codedata.getOuid());
                tree = null;
                treeViewScrPane.setViewportView(null);
                dos.releaseWorkingModel();
            } else
            if(selnode.getLevel() == 1)
            {
                dos.removePackage(codedata.getOuid());
                treemodel.removeNodeFromParent(selnode);
            } else
            if(selnode.getLevel() == 2)
            {
                if(command.equals("DeleteMenu"))
                {
                    dos.removeClass(codedata.getOuid());
                    treemodel.removeNodeFromParent(selnode);
                } else
                if(command.equals("DeleteCascadeMenu"))
                {
                    dos.removeClassCascade(codedata.getOuid());
                    ObjectModelingConstruction.getInstance().reloadModel();
                }
            } else
            if(selnode.getLevel() == 4 && codedata.getDescription().equals("Field"))
            {
                dos.removeField(codedata.getOuid());
                treemodel.removeNodeFromParent(selnode);
            } else
            if(selnode.getLevel() == 4 && codedata.getDescription().equals("Action"))
            {
                dos.removeAction(codedata.getOuid());
                treemodel.removeNodeFromParent(selnode);
            } else
            if(selnode.getLevel() == 4 && codedata.getDescription().equals("FGroup"))
            {
                dos.removeFieldGroup(codedata.getOuid());
                treemodel.removeNodeFromParent(selnode);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void searchButton_ActionPerformed(ActionEvent e)
    {
        TreePath treePath = null;
        DefaultMutableTreeNode node = null;
        TreeNodeObject nodeObject = null;
        String str = nameTextField.getText();
        if(!Utils.isNullString(str))
        {
            int findCnt = 0;
            int index = 0;
            int rowCnt = tree.getRowCount();
            int selRows[] = tree.getSelectionRows();
            if(selRows != null && selRows.length > 0)
                index = selRows[selRows.length - 1] + 1;
            do
            {
                if(findCnt == rowCnt)
                {
                    tree.setSelectionRow(0);
                    tree.scrollRowToVisible(0);
                    JOptionPane.showMessageDialog(this, "No data found.", "Information", 1);
                    break;
                }
                if(index == rowCnt)
                    index = 0;
                treePath = tree.getPathForRow(index);
                node = (DefaultMutableTreeNode)treePath.getLastPathComponent();
                nodeObject = (TreeNodeObject)node.getUserObject();
                if(nodeObject.getName().indexOf(str) >= 0)
                {
                    tree.setSelectionPath(treePath);
                    tree.scrollPathToVisible(treePath);
                    break;
                }
                index++;
                findCnt++;
            } while(true);
        }
    }

    private void tree_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() != 3)
        {
            DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(selnode == null)
                return;
            TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
            if(contentFrame instanceof ObjectModelingConstruction)
                ((ObjectModelingConstruction)contentFrame).displayInformation(codedata.getOuid(), codedata.getDescription(), selnode.getLevel(), ((ObjectModelingConstruction)contentFrame).splitPane.getDividerLocation());
        }
    }

    public DefaultMutableTreeNode addObject(Object child)
    {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();
        if(parentPath == null)
            parentNode = topNode;
        else
            parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
        if(parentNode.getLevel() < 4)
            return addObject(parentNode, child, true);
        else
            return null;
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible)
    {
        TreeNodeObject codedata = null;
        codedata = (TreeNodeObject)parent.getUserObject();
        if(parent.getLevel() == 0)
        {
            PackageInformation newPackageInfo = new PackageInformation((ObjectModelingConstruction)contentFrame);
            ObjectInsertFrame insertFrame = new ObjectInsertFrame(newPackageInfo, this, parent);
            insertFrame.show();
        } else
        if(parent.getLevel() == 1)
        {
            ClassInformation newClassInfo = new ClassInformation((ObjectModelingConstruction)contentFrame, codedata.getOuid());
            ObjectInsertFrame insertFrame = new ObjectInsertFrame(newClassInfo, this, parent);
            insertFrame.show();
        } else
        if(parent.getLevel() == 3 && codedata.getDescription().equals("Field Folder"))
        {
            FieldInformation newFieldInfo = new FieldInformation((ObjectModelingConstruction)contentFrame, codedata.getOuid());
            ObjectInsertFrame insertFrame = new ObjectInsertFrame(newFieldInfo, this, parent);
            insertFrame.show();
        } else
        if(parent.getLevel() == 3 && codedata.getDescription().equals("Action Folder"))
        {
            ActionInformation newActionInfo = new ActionInformation((ObjectModelingConstruction)contentFrame, codedata.getOuid());
            ObjectInsertFrame insertFrame = new ObjectInsertFrame(newActionInfo, this, parent);
            insertFrame.show();
        } else
        if(parent.getLevel() == 3 && codedata.getDescription().equals("FGroup Folder"))
        {
            FieldGroupInformation newFieldGroupInfo = new FieldGroupInformation((ObjectModelingConstruction)contentFrame, codedata.getOuid());
            ObjectInsertFrame insertFrame = new ObjectInsertFrame(newFieldGroupInfo, this, parent);
            insertFrame.show();
        }
        return null;
    }

    public void addTreeDrawing(TreeNodeObject codedata, DefaultMutableTreeNode parent)
    {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(codedata);
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        UIUtils.scrollTreePathToVisible(tree, parent);
    }

    public void addTreeDrawing(String packageOuid, TreeNodeObject classData, DefaultMutableTreeNode parent)
    {
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(classData);
        setInsertNode(parent, child);
        setExpand(child, packageOuid, classData.getOuid());
        UIUtils.scrollTreePathToVisible(tree, child);
    }

    public ArrayList getSelectedNodeInfo()
    {
        ArrayList nodeAL = new ArrayList();
        DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(selnode == null)
        {
            return null;
        } else
        {
            TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
            nodeAL.add(codedata.getOuid());
            return nodeAL;
        }
    }

    public void getSelectedOuid()
    {
        DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(selnode != null)
        {
            classSelectedOuid = classSelectedOuidTemp;
            fieldSelectedOuid = fieldSelectedOuidTemp;
            actionSelectedOuid = actionSelectedOuidTemp;
            fieldGroupSelectedOuid = fieldGroupSelectedOuidTemp;
            TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
            if(codedata.getDescription().equals("Class"))
                classSelectedOuid = codedata.getOuid();
            else
            if(codedata.getDescription().equals("Field"))
                fieldSelectedOuid = codedata.getOuid();
            else
            if(codedata.getDescription().equals("Action"))
                actionSelectedOuid = codedata.getOuid();
            else
            if(codedata.getDescription().equals("FGroup"))
                fieldGroupSelectedOuid = codedata.getOuid();
        } else
        {
            classSelectedOuid = classSelectedOuidTemp;
            fieldSelectedOuid = fieldSelectedOuidTemp;
            actionSelectedOuid = actionSelectedOuidTemp;
            fieldGroupSelectedOuid = fieldGroupSelectedOuidTemp;
        }
    }

    public void treeRefresh()
    {
        if(contentFrame instanceof ObjectModelingConstruction)
        {
            TreePath selectedPath = tree.getSelectionPath();
            getSelectedOuid();
            ((ObjectModelingConstruction)contentFrame).makeObjectSetTree(topnode, 1);
            tree.setSelectionPath(selectedPath);
        }
    }

    public void makeSearchConditionPanel()
    {
        nameTextField = new DynaTextField();
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(50);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        searchButton = new JButton();
        searchButton.setActionCommand("search");
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.addActionListener(listenerCommon);
        searchConditionPanel = new JPanel();
        searchConditionPanel.setLayout(new BoxLayout(searchConditionPanel, 0));
        searchConditionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Search Condition"));
        searchConditionPanel.add(nameTextField);
        searchConditionPanel.add(searchButton);
        searchConditionPanel.add(Box.createHorizontalStrut(5));
    }

    public void createPackageForSolidworks()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for Solidworks");
            setInfoDC.put("description", "Package for Solidworks CAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Solidworks File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(false));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "");
            setInfoDC.put("title", "Solidworks File");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            setInfoDC.put("is.abstract", Boolean.TRUE);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "Information");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid4);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            ArrayList superClassList = new ArrayList();
            superClassList.add(fileClassOuid);
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Solidworks Assembly");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "SLD_ASSEMBLY");
            setInfoDC.put("title", "Solidworks Assembly");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            productClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", productClassOuid);
            productClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Solidworks Part");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "SLD_PART");
            setInfoDC.put("title", "Solidworks Part");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            partClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", partClassOuid);
            partClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Solidworks Drawing");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "SLD_DRAWING");
            setInfoDC.put("title", "Solidworks Drawing");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            drawingClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", drawingClassOuid);
            drawingClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            tempClass = dos.getClassWithName(tempOuid, "FStructure");
            if(tempClass == null)
                return;
            tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Solidworks PS");
            setInfoDC.put("description", "Solidworks Part Structure");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(false));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "SLD_PS");
            setInfoDC.put("title", "Solidworks PS");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            psClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", psClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Quantity");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("type", new Byte((byte)6));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "QTY");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "\uC218\uB7C9");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid1 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid1);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "Information");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$sequence");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid1);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "SldAsso_Asm_Asm");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "SLDASSO_ASM_ASM");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", productClassOuid);
            setInfoDC.put("end2.name", productClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "SldAsso_Asm_Prt");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "SLDASSO_ASM_PRT");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", partClassOuid);
            setInfoDC.put("end2.name", partClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "SldAsso_Asm_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "SLDASSO_ASM_DRW");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "SldAsso_Prt_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "SLDASSO_PRT_DRW");
            setInfoDC.put("end1.ouid@class", partClassOuid);
            setInfoDC.put("end1.name", partClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void createPackageForCatiaV4()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldOuid5 = "";
            String fieldOuid6 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for CATIA V4");
            setInfoDC.put("description", "Package for CATIA V4 CAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "CATIA V4 File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "CATIAV4_FILE");
            setInfoDC.put("title", "CATIA V4 File");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", tempList);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Is_3D");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)1));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(4));
            setInfoDC.put("code", "IS3D");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Is 3D");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Is_2D");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)1));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(5));
            setInfoDC.put("code", "IS2D");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Is 2D");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid5 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid5);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid6 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid6);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "Information");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            fieldOuidList.add(fieldOuid4);
            fieldOuidList.add(fieldOuid5);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid6);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void createPackageForAutoCAD()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldOuid5 = "";
            String fieldOuid6 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for AutoCAD");
            setInfoDC.put("description", "Package for AutoCAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "AutoCAD File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "AutoCAD_File");
            setInfoDC.put("title", "AutoCAD");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", tempList);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Is_3D");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)1));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(4));
            setInfoDC.put("code", "IS3D");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(false));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Is 3D");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Is_2D");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)1));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", "true");
            setInfoDC.put("index", new Integer(5));
            setInfoDC.put("code", "IS2D");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(false));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Is 2D");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid5 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid5);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid6 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid6);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "Information");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            fieldOuidList.add(fieldOuid4);
            fieldOuidList.add(fieldOuid5);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid6);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void createPackageForCatiaV5()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for CATIA V5");
            setInfoDC.put("description", "Package for CATIA V5 CAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "CATIA V5 File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(false));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "");
            setInfoDC.put("title", "CATIA V5 File");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            setInfoDC.put("is.abstract", Boolean.TRUE);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid4);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            ArrayList superClassList = new ArrayList();
            superClassList.add(fileClassOuid);
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "CATIA V5 Assembly");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "CATV5_ASSEMBLY");
            setInfoDC.put("title", "CATIA V5 Assembly");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            productClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", productClassOuid);
            productClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "CATIA V5 Part");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "CATV5_PART");
            setInfoDC.put("title", "CATIA V5 Part");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            partClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", partClassOuid);
            partClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "CATIA V5 Drawing");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "CATV5_DRAWING");
            setInfoDC.put("title", "CATIA V5 Drawing");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            drawingClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", drawingClassOuid);
            drawingClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            tempClass = dos.getClassWithName(tempOuid, "FStructure");
            if(tempClass == null)
                return;
            tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "CATIA V5 PS");
            setInfoDC.put("description", "CATIA V5 Part Structure");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(false));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "CATIAV5_PS");
            setInfoDC.put("title", "CATIA V5 PS");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            psClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", psClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Quantity");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("type", new Byte((byte)6));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "QTY");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "\uC218\uB7C9");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid1 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid1);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$sequence");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid1);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "CatV5Asso_Asm_Asm");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "CATV5ASSO_ASM_ASM");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", productClassOuid);
            setInfoDC.put("end2.name", productClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "CatV5Asso_Asm_Prt");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "CATV5ASSO_ASM_PRT");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", partClassOuid);
            setInfoDC.put("end2.name", partClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "CatV5Asso_Asm_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "CATV5ASSO_ASM_DRW");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "CatV5Asso_Prt_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "CATV5ASSO_PRT_DRW");
            setInfoDC.put("end1.ouid@class", partClassOuid);
            setInfoDC.put("end1.name", partClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void createPackageForUG()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for Unigraphics");
            setInfoDC.put("description", "Package for Unigraphics CAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Unigraphics File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(false));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "");
            setInfoDC.put("title", "Unigraphics File");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            setInfoDC.put("is.abstract", Boolean.TRUE);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid4);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            ArrayList superClassList = new ArrayList();
            superClassList.add(fileClassOuid);
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Unigraphics Assembly");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "UG_ASSEMBLY");
            setInfoDC.put("title", "Unigraphics Assembly");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            productClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", productClassOuid);
            productClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Unigraphics Part");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "UG_PART");
            setInfoDC.put("title", "Unigraphics Part");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            partClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", partClassOuid);
            partClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            tempClass = dos.getClassWithName(tempOuid, "FStructure");
            if(tempClass == null)
                return;
            tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Unigraphics PS");
            setInfoDC.put("description", "Unigraphics Part Structure");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(false));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "UG_PS");
            setInfoDC.put("title", "Unigraphics PS");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            psClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", psClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Quantity");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("type", new Byte((byte)6));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "QTY");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "\uC218\uB7C9");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid1 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid1);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$sequence");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid1);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "UGAsso_Asm_Asm");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "UGASSO_ASM_ASM");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", productClassOuid);
            setInfoDC.put("end2.name", productClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "UGAsso_Asm_Prt");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "UGASSO_ASM_PRT");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", partClassOuid);
            setInfoDC.put("end2.name", partClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void createPackageForProE()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for ProEngineer");
            setInfoDC.put("description", "Package for ProEngineer CAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "ProEngineer File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(false));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "");
            setInfoDC.put("title", "ProEngineer File");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            setInfoDC.put("is.abstract", Boolean.TRUE);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid4);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            ArrayList superClassList = new ArrayList();
            superClassList.add(fileClassOuid);
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "ProEngineer Assembly");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "ProE_ASSEMBLY");
            setInfoDC.put("title", "ProEngineer Assembly");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            productClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", productClassOuid);
            productClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "ProEngineer Part");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "ProE_PART");
            setInfoDC.put("title", "ProEngineer Part");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            partClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", partClassOuid);
            partClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "ProEngineer Drawing");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "ProE_DRAWING");
            setInfoDC.put("title", "ProEngineer Drawing");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            drawingClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", drawingClassOuid);
            drawingClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            tempClass = dos.getClassWithName(tempOuid, "FStructure");
            if(tempClass == null)
                return;
            tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "ProEngineer PS");
            setInfoDC.put("description", "ProEngineer Part Structure");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(false));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "ProE_PS");
            setInfoDC.put("title", "ProEngineer PS");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            psClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", psClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Quantity");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("type", new Byte((byte)6));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "QTY");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "\uC218\uB7C9");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid1 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid1);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$sequence");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid1);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "ProEAsso_Asm_Asm");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "ProEASSO_ASM_ASM");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", productClassOuid);
            setInfoDC.put("end2.name", productClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "ProEAsso_Asm_Prt");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "ProEASSO_ASM_PRT");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", partClassOuid);
            setInfoDC.put("end2.name", partClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "ProEAsso_Asm_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "ProEASSO_ASM_DRW");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "ProEAsso_Prt_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "ProEASSO_PRT_DRW");
            setInfoDC.put("end1.ouid@class", partClassOuid);
            setInfoDC.put("end1.name", partClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void createPackageForIDEAS()
    {
        try
        {
            String packageOuid = "";
            String fileClassOuid = "";
            String productClassOuid = "";
            String productClassName = "";
            String partClassOuid = "";
            String partClassName = "";
            String drawingClassOuid = "";
            String drawingClassName = "";
            String psClassOuid = "";
            String fieldOuid1 = "";
            String fieldOuid2 = "";
            String fieldOuid3 = "";
            String fieldOuid4 = "";
            String fieldGroupOuid = "";
            String assoOuid = "";
            DOSChangeable setInfoDC = null;
            TreeNodeObject nodeData = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultMutableTreeNode packageNode = null;
            DefaultMutableTreeNode classNode = null;
            DefaultMutableTreeNode fieldFolderNode = null;
            DefaultMutableTreeNode actionFolderNode = null;
            DefaultMutableTreeNode fieldGroupFolderNode = null;
            DefaultMutableTreeNode fieldNode = null;
            DefaultMutableTreeNode fieldGroupNode = null;
            rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", packageOuid);
            setInfoDC.put("name", "Package for I-DEAS");
            setInfoDC.put("description", "Package for ProEngineer CAD Integration");
            packageOuid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", packageOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Package");
            packageNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(packageNode, rootNode, rootNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            DOSChangeable tempPackage = dos.getPackageWithName("Foundation");
            if(tempPackage == null)
                return;
            String tempOuid = (String)tempPackage.get("ouid");
            DOSChangeable tempClass = dos.getClassWithName(tempOuid, "FCADFile");
            if(tempClass == null)
                return;
            LinkedList tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            tempPackage = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "I-DEAS File");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(false));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "");
            setInfoDC.put("title", "I-DEAS File");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            setInfoDC.put("is.abstract", Boolean.TRUE);
            fileClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", fileClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Size");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(2));
            setInfoDC.put("code", "DRW_SIZE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Size");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid2 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid2);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Scale");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)24));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(3));
            setInfoDC.put("code", "DRW_SCALE");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "Scale");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid3 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid3);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("type", new Byte((byte)13));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "REMARKS");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "remarks");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", new Integer(450));
            setInfoDC.put("height", new Integer(250));
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", new Integer(1));
            setInfoDC.put("is.title.visible", new Boolean(false));
            setInfoDC.put("icon", null);
            fieldOuid4 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid4);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            ArrayList fieldOuidList = new ArrayList();
            DOSChangeable tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "vf$version");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "name");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawing_number");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingType");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "drawingSource");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid2);
            fieldOuidList.add(fieldOuid3);
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$status");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$user");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$cdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$mdate");
            fieldOuidList.add((String)tempField.get("ouid"));
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Remarks");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", fileClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("title", "\uBE44\uACE0");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            fieldOuidList.add(fieldOuid4);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            ArrayList superClassList = new ArrayList();
            superClassList.add(fileClassOuid);
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "I-DEAS Assembly");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "IDEAS_ASSEMBLY");
            setInfoDC.put("title", "I-DEAS Assembly");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            productClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", productClassOuid);
            productClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "I-DEAS Part");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "IDEAS_PART");
            setInfoDC.put("title", "I-DEAS Part");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            partClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", partClassOuid);
            partClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "I-DEAS Drawing");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(true));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "IDEAS_DRAWING");
            setInfoDC.put("title", "I-DEAS Drawing");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(true));
            setInfoDC.put("array$ouid.superclass", superClassList);
            drawingClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", drawingClassOuid);
            drawingClassName = (String)setInfoDC.get("name");
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            tempClass = dos.getClassWithName(tempOuid, "FStructure");
            if(tempClass == null)
                return;
            tempList = new LinkedList();
            tempList.add(tempClass.get("ouid"));
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "I-DEAS PS");
            setInfoDC.put("description", "I-DEAS Part Structure");
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(false));
            setInfoDC.put("is.leaf", new Boolean(true));
            setInfoDC.put("is.versionable", new Boolean(false));
            setInfoDC.put("capacity", null);
            setInfoDC.put("code", "IDEAS_PS");
            setInfoDC.put("title", "I-DEAS PS");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("icon", "");
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("is.xa.object", null);
            setInfoDC.put("is.filecontrol", new Boolean(false));
            setInfoDC.put("array$ouid.superclass", tempList);
            psClassOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", psClassOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Class");
            classNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(classNode, packageNode, packageNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field", "Field Folder");
            fieldFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Action", "Action Folder");
            actionFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(actionFolderNode, classNode, classNode.getChildCount());
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), "Field Group", "FGroup Folder");
            fieldGroupFolderNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupFolderNode, classNode, classNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Quantity");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("type", new Byte((byte)6));
            setInfoDC.put("type.ouid@class", null);
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("code", "QTY");
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", "\uC218\uB7C9");
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("column", new Integer(0));
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuid1 = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid1);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "Field");
            fieldNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldNode, fieldFolderNode, fieldFolderNode.getChildCount());
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            setInfoDC.put("name", "Information");
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("index", new Integer(1));
            setInfoDC.put("is.mandatory", new Boolean(true));
            setInfoDC.put("title", "\uC815\uBCF4");
            setInfoDC.put("tooltip", "");
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            fieldOuidList = new ArrayList();
            tempField = dos.getFieldWithName((String)tempClass.get("ouid"), "md$sequence");
            fieldOuidList.add((String)tempField.get("ouid"));
            fieldOuidList.add(fieldOuid1);
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", null);
            fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            setInfoDC.put("ouid", fieldGroupOuid);
            nodeData = new TreeNodeObject((String)setInfoDC.get("ouid"), (String)setInfoDC.get("name"), "FGroup");
            fieldGroupNode = new DefaultMutableTreeNode(nodeData);
            treeModel.insertNodeInto(fieldGroupNode, fieldGroupFolderNode, fieldGroupFolderNode.getChildCount());
            fieldOuidList = null;
            nodeData = null;
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "I-DEAS_Asm_Asm");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "IDEAS_ASM_ASM");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", productClassOuid);
            setInfoDC.put("end2.name", productClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "I-DEAS_Asm_Prt");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "IDEAS_ASM_PRT");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", partClassOuid);
            setInfoDC.put("end2.name", partClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "I-DEAS_Asm_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "IDEAS_ASM_DRW");
            setInfoDC.put("end1.ouid@class", productClassOuid);
            setInfoDC.put("end1.name", productClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
            setInfoDC = new DOSChangeable();
            setInfoDC.put("name", "I-DEAS_Prt_Drw");
            setInfoDC.put("type", new Byte((byte)3));
            setInfoDC.put("description", "\uC81C\uD488\uAD6C\uC870");
            setInfoDC.put("ouid@class", psClassOuid);
            setInfoDC.put("datasource.id", "default");
            setInfoDC.put("code", "IDEAS_PRT_DRW");
            setInfoDC.put("end1.ouid@class", partClassOuid);
            setInfoDC.put("end1.name", partClassName);
            setInfoDC.put("end1.multiplicity.from", null);
            setInfoDC.put("end1.multiplicity.to", null);
            setInfoDC.put("end1.is.navigable", null);
            setInfoDC.put("end1.is.ordered", null);
            setInfoDC.put("end1.changeable", null);
            setInfoDC.put("end2.ouid@class", drawingClassOuid);
            setInfoDC.put("end2.name", drawingClassName);
            setInfoDC.put("end2.multiplicity.from", null);
            setInfoDC.put("end2.multiplicity.to", null);
            setInfoDC.put("end2.is.navigable", null);
            setInfoDC.put("end2.is.ordered", null);
            setInfoDC.put("end2.changeable", null);
            assoOuid = dos.createAssociation(setInfoDC);
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    final String INFO_MENU = "Information";
    final String COPY_MENU = "Copy";
    final String PASTE_MENU = "Paste";
    final String ADD_MENU = "Add";
    final String DELETE_MENU = "Delete";
    final String GRAPH_MENU = "\uADF8\uB9BC\uC0DD\uC131";
    final String SHARE_MENU = "Package Share";
    final String UNLINK_MENU = "Cancel Package Sahre";
    final String DELETECASCADE_MENU = "Delete(Cascade)";
    boolean isAddPopup;
    Cursor waitCursor;
    DefaultMutableTreeNode copyNode;
    JPopupMenu popupMenu;
    JMenu addMenuAdd;
    JMenuItem informationMenu;
    JMenuItem addMenu;
    JMenuItem copyMenu;
    JMenuItem pasteMenu;
    JMenuItem deleteMenu;
    JMenuItem graphAddMenu;
    JMenuItem cutMenu;
    JMenuItem replaceMenu;
    JMenuItem insertIntoMenu;
    JMenuItem sharePackageMenu;
    JMenuItem unlinkPackageMenu;
    JSeparator deleteSeparator;
    JMenuItem deleteCascadeMenu;
    JScrollPane treeViewScrPane;
    TreeNodeObject copyData;
    JTree tree;
    DefaultMutableTreeNode topNode;
    DefaultTreeModel treeModel;
    TreeNodeObject topPartCode;
    Vector tempData;
    ListenerCommon listenerCommon;
    Object contentFrame;
    DOSChangeable packageInfoDC;
    DOSChangeable classListInfoDC;
    DOSChangeable codeListInfoDC;
    DOSChangeable classInfoDC;
    DOSChangeable fieldInfoDC;
    DOSChangeable actionInfoDC;
    DOSChangeable fieldGroupInfoDC;
    private DOS dos;
    TreeNodeObject topnode;
    String classSelectedOuid;
    String classSelectedOuidTemp;
    String fieldSelectedOuid;
    String fieldSelectedOuidTemp;
    String actionSelectedOuid;
    String actionSelectedOuidTemp;
    String fieldGroupSelectedOuid;
    String fieldGroupSelectedOuidTemp;
    JPanel searchConditionPanel;
    DynaTextField nameTextField;
    JButton searchButton;
    JSplitPane splitPane;









}
