// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CodeManager.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.DynaMOAD;
import dyna.framework.client.LogIn;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelDefaultTreeCellRenderer, CodeInformation, ObjectInsertFrame, CodeItemInformation

public class CodeManager extends JFrame
    implements ActionListener, MouseListener
{
    class PopupListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(codeTree == null)
                return;
            node = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
            if(node == null)
                return;
            TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
            if(node.getLevel() == 0)
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Code");
                deleteMenu.setVisible(false);
            } else
            if(node.getLevel() == 1)
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Code Item");
                deleteMenu.setVisible(true);
            }
            if(!isAdmin())
            {
                addMenuAdd.setEnabled(false);
                deleteMenu.setEnabled(false);
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
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

        DefaultMutableTreeNode node;

        PopupListener()
        {
            node = null;
        }
    }

    class CodeTreeListener
        implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent e)
        {
            DefaultMutableTreeNode selnode = null;
            TreeNodeObject codedata = null;
            if(e.getSource().equals(codeTree))
            {
                selnode = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
                if(selnode == null || selnode.getLevel() == 0)
                {
                    displayInformation(null);
                } else
                {
                    codedata = (TreeNodeObject)selnode.getUserObject();
                    selectedCodeOuid = codedata.getOuid();
                    if(!selnode.isRoot())
                        displayInformation(selectedCodeOuid);
                }
            } else
            if(e.getSource().equals(hTree) && codeItemInformation != null)
            {
                selnode = (DefaultMutableTreeNode)hTree.getLastSelectedPathComponent();
                if(selnode != null)
                {
                    codedata = (TreeNodeObject)selnode.getUserObject();
                    codeItemInformation.setParentItemOuid(codedata.getOuid());
                    populateHierarchyTreeNode(selnode, false);
                    codeItemInformation.selectItemByOuid(codedata.getOuid());
                }
            }
        }

        CodeTreeListener()
        {
        }
    }

    class DynaComboBooleanInstance extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            return setBooleanComboBox();
        }

        DynaComboBooleanInstance()
        {
        }
    }


    public CodeManager(Object parent, DOS dos)
    {
        aus = null;
        leftSplitPane = null;
        renderer = null;
        hTreeScrollPane = null;
        hTree = null;
        imageCode = new ImageIcon("icons/Code.gif");
        imageAssociation = new ImageIcon("icons/Association.gif");
        imageCodeItem = new ImageIcon("icons/execution.gif");
        modeOfCodeItem = false;
        isAutoGenComboDataLoader = new DynaComboBooleanInstance();
        isAutoGenComboModel = new DynaComboBoxModel(isAutoGenComboDataLoader);
        popupListener = null;
        treeSelectionListener = null;
        try
        {
            this.dos = dos;
            aus = DynaMOAD.aus;
            modelOuid = this.dos.getWorkingModel();
            modelInfoDC = new DOSChangeable();
            if(modelOuid != null && !modelOuid.equals(""))
                modelInfoDC = this.dos.getModel(modelOuid);
            initialize();
            myInstance = this;
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        System.out.println("Modeler - CodeManager");
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("Code Manager");
        setLocation(112, 84);
        setSize(600, 450);
        mainButtonToolBar = new JToolBar();
        codeSplitPane = new JSplitPane(1);
        codeSplitPane.setDividerSize(5);
        codeSplitPane.setDividerLocation(200);
        codeTreeScrPane = UIFactory.createStrippedScrollPane(null);
        codeTreeScrPane.setSize(200, 200);
        codeInfoScrPane = UIFactory.createStrippedScrollPane(null);
        codeItemInfoScrPane = UIFactory.createStrippedScrollPane(null);
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        modifyButton = new JButton();
        modifyButton.setToolTipText("Code Modify");
        modifyButton.setMargin(new Insets(0, 0, 0, 0));
        modifyButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        modifyButton.setActionCommand("Modify");
        modifyButton.addActionListener(this);
        deleteButton = new JButton();
        deleteButton.setToolTipText("Code Delete");
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteButton.setActionCommand("Delete");
        deleteButton.addActionListener(this);
        mainButtonToolBar.add(modifyButton);
        mainButtonToolBar.add(deleteButton);
        makeTree();
        mainTabbedPane.setPreferredSize(new Dimension(450, 400));
        mainTabbedPane.addTab("Code", imageCode, codeInfoScrPane, "Code Information");
        mainTabbedPane.addTab("CodeItem", imageCodeItem, codeItemInfoScrPane, "CodeItem Information");
        codeSplitPane.add(codeTreeScrPane, "left");
        codeSplitPane.add(mainTabbedPane, "right");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainButtonToolBar, "North");
        getContentPane().add(codeSplitPane, "Center");
        if(!isAdmin())
        {
            modifyButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    public boolean isAdmin()
    {
        if(Utils.isNullString(LogIn.userID))
            return true;
        boolean isadmin = false;
        try
        {
            isadmin = aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR");
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return isadmin;
    }

    public void makeTree()
    {
        try
        {
            topNodeObj = new TreeNodeObject((String)modelInfoDC.get("ouid"), (String)modelInfoDC.get("name"), (String)modelInfoDC.get("description"));
            topNode = new DefaultMutableTreeNode(topNodeObj);
            treeModel = new DefaultTreeModel(topNode);
            codeTree = new JTree(treeModel);
            if(treeSelectionListener == null)
                treeSelectionListener = new CodeTreeListener();
            codeTree.addTreeSelectionListener(treeSelectionListener);
            createPopupMenu();
            if(codeTree == null)
                return;
            if(renderer == null)
                renderer = new ObjectModelDefaultTreeCellRenderer();
            codeTree.setCellRenderer(renderer);
            codeTree.getSelectionModel().setSelectionMode(1);
            codeTree.putClientProperty("JTree.lineStyle", "Angled");
            codeTreeScrPane.setViewportView(codeTree);
            setExpand(topNode);
            codeTree.setSelectionRow(1);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void createPopupMenu()
    {
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        addMenuAdd.setText("Add");
        addMenu = new JMenuItem();
        addMenu.setText("Add");
        addMenu.setActionCommand("AddMenu");
        addMenu.addActionListener(this);
        deleteMenu = new JMenuItem();
        deleteMenu.setText("Delete");
        deleteMenu.setActionCommand("Delete");
        deleteMenu.addActionListener(this);
        addMenuAdd.add(addMenu);
        popupMenu.add(addMenuAdd);
        popupMenu.add(new JSeparator());
        popupMenu.add(deleteMenu);
        popupListener = new PopupListener();
        codeTree.addMouseListener(popupListener);
    }

    private void setExpand(DefaultMutableTreeNode node)
    {
        String ouid = "";
        ArrayList codeListAL = new ArrayList();
        codeInfoDC = new DOSChangeable();
        try
        {
            codeListAL = dos.listCode(modelOuid);
            if(codeListAL != null && !codeListAL.isEmpty())
            {
                for(int i = 0; i < codeListAL.size(); i++)
                {
                    ouid = (String)((DOSChangeable)codeListAL.get(i)).get("ouid");
                    codeInfoDC = dos.getCode(ouid);
                    if(codeInfoDC != null)
                    {
                        TreeNodeObject codedata = new TreeNodeObject((String)codeInfoDC.get("ouid"), (String)codeInfoDC.get("name"), (String)codeInfoDC.get("description"));
                        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                        setInsertNode(node, childnode);
                        codeTree.scrollPathToVisible(new TreePath(childnode.getPath()));
                    }
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)codeTree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    public void setModeOfCodeItemDlg(boolean mode)
    {
        modeOfCodeItem = mode;
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("AddMenu"))
        {
            DefaultMutableTreeNode parentNode = null;
            TreePath parentPath = codeTree.getSelectionPath();
            if(parentPath == null)
                parentNode = topNode;
            else
                parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
            if(parentNode.getLevel() == 0)
            {
                CodeInformation newCodeInfo = new CodeInformation(dos, modelOuid);
                ObjectInsertFrame insertFrame = new ObjectInsertFrame(newCodeInfo, this, parentNode);
                insertFrame.show();
            } else
            {
                mainTabbedPane.setSelectedIndex(1);
                codeItemInformation.clearCodeItemField();
            }
        } else
        if(command.equals("Delete"))
        {
            if(selectedCodeOuid != null && !Utils.isNullString(selectedCodeOuid))
            {
                boolean rv = codeInformation.removeCodeInformation();
                if(rv)
                {
                    DefaultTreeModel model = (DefaultTreeModel)codeTree.getModel();
                    DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
                    model.removeNodeFromParent(selnode);
                }
            }
        } else
        if(command.equals("Modify"))
            try
            {
                if(selectedCodeOuid != null && !Utils.isNullString(selectedCodeOuid))
                {
                    codeInformation.setCodeInformation();
                    DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
                    DOSChangeable codeInfoDC = dos.getCode(selectedCodeOuid);
                    if(codeInfoDC != null)
                    {
                        TreeNodeObject codedata = new TreeNodeObject((String)codeInfoDC.get("ouid"), (String)codeInfoDC.get("name"), (String)codeInfoDC.get("description"));
                        selnode.setUserObject(codedata);
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
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

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private void populateHierarchyTreeNode(DefaultMutableTreeNode treeNode, boolean forceMode)
    {
        if(treeNode == null)
            return;
        TreeNodeObject codedata = null;
        codedata = (TreeNodeObject)treeNode.getUserObject();
        codeItemInformation.setParentItemOuid(codedata.getOuid());
        if(codedata.isPopulated() && !forceMode)
            return;
        if(forceMode)
            treeNode.removeAllChildren();
        DefaultMutableTreeNode itemNode = null;
        TreeNodeObject itemNodeObject = null;
        DOSChangeable codeItem = null;
        try
        {
            ArrayList itemChildren = dos.getCodeItemChildren(codedata.getOuid());
            if(Utils.isNullArrayList(itemChildren))
                return;
            Iterator itemKey;
            for(itemKey = itemChildren.iterator(); itemKey.hasNext();)
            {
                codeItem = (DOSChangeable)itemKey.next();
                if(codeItem == null)
                    return;
                itemNodeObject = new TreeNodeObject((String)codeItem.get("ouid"), (String)codeItem.get("name"), (String)codeItem.get("description"));
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
                hTree.fireTreeExpanded(new TreePath(treeNode.getPath()));
            else
                hTree.fireTreeCollapsed(new TreePath(treeNode.getPath()));
        }
        catch(IIPRequestException e1)
        {
            e1.printStackTrace();
        }
    }

    public void populateHierarchyTreeNode()
    {
        if(hTree == null)
            return;
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)hTree.getLastSelectedPathComponent();
        if(treeNode == null)
        {
            return;
        } else
        {
            populateHierarchyTreeNode(treeNode, true);
            hTree.expandPath(new TreePath(treeNode.getPath()));
            hTree.updateUI();
            return;
        }
    }

    public ArrayList setBooleanComboBox()
    {
        ArrayList booleanAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        booleanAL.add(new Boolean(true));
        booleanAL.add("true");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        booleanAL.add(new Boolean(false));
        booleanAL.add("false");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        return returnAL;
    }

    public void displayInformation(String codeOuid)
    {
        try
        {
            if(codeInformation == null)
            {
                codeInformation = new CodeInformation(dos, modelOuid);
                codeItemInformation = new CodeItemInformation(dos);
                codeInfoScrPane.setViewportView(codeInformation);
                codeItemInfoScrPane.setViewportView(codeItemInformation);
            }
            if(codeOuid != null)
            {
                DOSChangeable codeInfoDC = new DOSChangeable();
                codeInfoDC = dos.getCode(codeOuid);
                codeInformation.setCodeInfoField(codeInfoDC);
                codeItemInformation.setCodeOuid((String)codeInfoDC.get("ouid"));
                setLeftSplitPane(codeInfoDC);
            } else
            {
                this.codeInfoDC = null;
                codeInformation.setCodeInfoField(null);
                codeItemInformation.setCodeOuid(null);
            }
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void getSelectedOuid()
    {
        DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
        TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
        selectedCodeOuid = codedata.getOuid();
    }

    public JTree getTree()
    {
        return codeTree;
    }

    public void setLeftSplitPane(DOSChangeable dosCode)
    {
        if(leftSplitPane == null)
        {
            leftSplitPane = new JSplitPane(0);
            hTreeScrollPane = UIFactory.createStrippedScrollPane(null);
            leftSplitPane.setBottomComponent(hTreeScrollPane);
            getClass();
            leftSplitPane.setDividerLocation(200);
            getClass();
            leftSplitPane.setDividerSize(5);
        }
        if(dosCode == null)
            return;
        boolean isHierarchy = false;
        int oldDividerLocation = codeSplitPane.getDividerLocation();
        int oldDividerLocation2 = leftSplitPane.getDividerLocation();
        if(oldDividerLocation > 0)
        {
            oldDividerLocation = oldDividerLocation;
        } else
        {
            getClass();
            oldDividerLocation = 200;
        }
        if(oldDividerLocation2 > 0)
        {
            oldDividerLocation2 = oldDividerLocation2;
        } else
        {
            getClass();
            oldDividerLocation2 = 200;
        }
        if(dosCode.get("is.hierarchy") == null)
            isHierarchy = false;
        else
            isHierarchy = ((Boolean)dosCode.get("is.hierarchy")).booleanValue();
        if(isHierarchy)
        {
            leftSplitPane.setDividerLocation(oldDividerLocation2);
            hTreeScrollPane.setViewportView(makeHierarchyTree(dosCode));
            MUtils.replaceComponentOfSplitWithSplit(codeSplitPane, 0, hTreeScrollPane, leftSplitPane, 1);
        } else
        {
            hTreeScrollPane.setViewportView(null);
            MUtils.removeSplitComponentFromContainer(codeSplitPane, leftSplitPane, 1);
        }
        if(codeItemInformation != null)
        {
            codeItemInformation.setHierarchy(isHierarchy);
            codeItemInformation.setParentItemOuid(null);
        }
        codeSplitPane.setDividerLocation(oldDividerLocation);
    }

    private JTree makeHierarchyTree(DOSChangeable dosCode)
    {
        if(dosCode == null)
            return null;
        try
        {
            String rootItemOuid = null;
            DOSChangeable rootCodeItem = dos.getCodeItemRoot((String)dosCode.get("ouid"));
            if(rootCodeItem == null)
            {
                rootCodeItem = new DOSChangeable();
                rootCodeItem.put("name", dosCode.get("name"));
                rootCodeItem.put("description", "Root Item of Tree. Do not delete me.");
                rootCodeItem.put("codeitemid", dosCode.get("name"));
                rootItemOuid = dos.createCodeItem((String)dosCode.get("ouid"), rootCodeItem);
                rootCodeItem.clear();
                rootCodeItem = null;
                if(rootItemOuid == null)
                    return null;
                rootCodeItem = dos.getCodeItem(rootItemOuid);
                if(rootCodeItem == null)
                    return null;
            }
            TreeNodeObject rootNodeObject = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultTreeModel hTreeModel = null;
            rootNodeObject = new TreeNodeObject((String)rootCodeItem.get("ouid"), (String)rootCodeItem.get("name"), (String)rootCodeItem.get("description"));
            rootNode = new DefaultMutableTreeNode(rootNodeObject);
            hTreeModel = new DefaultTreeModel(rootNode);
            hTree = new JTree(hTreeModel);
            if(treeSelectionListener == null)
                treeSelectionListener = new CodeTreeListener();
            hTree.addTreeSelectionListener(treeSelectionListener);
            renderer = new ObjectModelDefaultTreeCellRenderer();
            hTree.setCellRenderer(renderer);
            hTree.getSelectionModel().setSelectionMode(1);
            hTree.putClientProperty("JTree.lineStyle", "Angled");
            hTreeScrollPane.setViewportView(hTree);
            populateHierarchyTreeNode(rootNode, true);
            hTree.updateUI();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        return hTree;
    }

    public static CodeManager getInstance()
    {
        return myInstance;
    }

    private static CodeManager myInstance = null;
    private final int DIVIDERSIZE = 5;
    private final int DIVIDERLOCATION = 200;
    private final int TITLE_WIDTH = 130;
    private DOS dos;
    private AUS aus;
    private JToolBar mainButtonToolBar;
    private JSplitPane codeSplitPane;
    private JScrollPane codeInfoScrPane;
    private JScrollPane codeItemInfoScrPane;
    private JTabbedPane mainTabbedPane;
    private DynaTextField nameInfoTextField;
    private DynaTextField descInfoTextField;
    private DynaComboBox isAutoGenInfoComboBox;
    private DynaTextField prefixInfoTextField;
    private DynaTextField suffixLenInfoTextField;
    private DynaTextField iniValInfoTextField;
    private DynaTextField incInfoTextField;
    private JButton modifyButton;
    private JButton deleteButton;
    private JSplitPane leftSplitPane;
    private JScrollPane codeTreeScrPane;
    private JTree codeTree;
    private DefaultMutableTreeNode topNode;
    private DefaultTreeModel treeModel;
    private TreeNodeObject topNodeObj;
    private ObjectModelDefaultTreeCellRenderer renderer;
    private JScrollPane hTreeScrollPane;
    private JTree hTree;
    private JScrollPane codeItemTableScrPane;
    private Table codeItemTable;
    private ArrayList codeItemData;
    private ArrayList codeItemColumnName;
    private ArrayList codeItemColumnWidth;
    private JPopupMenu popupMenu;
    private JMenu addMenuAdd;
    private JMenuItem informationMenu;
    private JMenuItem addMenu;
    private JMenuItem deleteMenu;
    private JDialog insertDlg;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel modelLabel;
    private JLabel prefixLabel;
    private DynaTextField nameTextField;
    private DynaTextField descTextField;
    private DynaComboBox isAutoGenComboBox;
    private DynaTextField prefixTextField;
    private DynaTextField suffixLenTextField;
    private DynaTextField iniValTextField;
    private DynaTextField incTextField;
    private JButton okButtonInsertDlg;
    private JButton cancelButtonInsertDlg;
    private JDialog insertCodeItemDlg;
    private JLabel nameOfCodeItemLabel;
    private JLabel descOfCodeItemLabel;
    private JTextField nameOfCodeItemTextField;
    private JTextField descOfCodeItemTextField;
    private JButton okButtonInsertDlgOfCodeItem;
    private JButton cancelButtonInsertDlgOfCodeItem;
    private CodeInformation codeInformation;
    private CodeItemInformation codeItemInformation;
    private ImageIcon imageCode;
    private ImageIcon imageAssociation;
    private ImageIcon imageCodeItem;
    private String modelOuid;
    private DOSChangeable modelInfoDC;
    private DOSChangeable codeInfoDC;
    private String selectedCodeOuid;
    private String selectOuid;
    private String codeItemID;
    private boolean modeOfCodeItem;
    private DynaComboBoxDataLoader isAutoGenComboDataLoader;
    private DynaComboBoxModel isAutoGenComboModel;
    private MouseListener popupListener;
    private TreeSelectionListener treeSelectionListener;
    final String INFO_MENU = "Information";
    final String ADD_MENU = "Add";
    final String DELETE_MENU = "Delete";










}
