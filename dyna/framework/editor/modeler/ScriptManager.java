// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScriptManager.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.util.Utils;
import dyna.util.notepad.Notepad;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, ObjectModelDefaultTreeCellRenderer

public class ScriptManager extends JFrame
    implements ActionListener, MouseListener
{
    class CodeTreeListener
        implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent e)
        {
            DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)scriptTree.getLastSelectedPathComponent();
            if(selnode == null || selnode.getLevel() == 0)
            {
                displayInformation(null, null);
                newButton.setEnabled(false);
                modifyButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else
            if(selnode.getLevel() == 1)
            {
                displayInformation(null, null);
                newButton.setEnabled(true);
                modifyButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else
            if(selnode.getLevel() == 2)
            {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selnode.getParent();
                TreeNodeObject nodeData = (TreeNodeObject)parent.getUserObject();
                String extType = nodeData.getOuid();
                nodeData = (TreeNodeObject)selnode.getUserObject();
                String scriptName = nodeData.getOuid();
                displayInformation(scriptName, extType);
                newButton.setEnabled(false);
                modifyButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        }

        CodeTreeListener()
        {
        }
    }


    public ScriptManager(Object parent)
    {
        treeSelectionListener = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            dss = (DSS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DSS1");
            modelOuid = dos.getWorkingModel();
            modelInfoDC = new DOSChangeable();
            if(modelOuid != null && !modelOuid.equals(""))
                modelInfoDC = dos.getModel(modelOuid);
            initialize();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("Script Manager");
        setLocation(112, 84);
        mainButtonToolBar = new JToolBar();
        scriptSplitPane = new JSplitPane(1);
        scriptSplitPane.setDividerSize(3);
        scriptSplitPane.setDividerLocation(250);
        codeTreeScrPane = UIFactory.createStrippedScrollPane(null);
        newButton = new JButton();
        newButton.setToolTipText("New");
        newButton.setMargin(new Insets(0, 0, 0, 0));
        newButton.setIcon(new ImageIcon(getClass().getResource("/icons/New.gif")));
        newButton.setActionCommand("New");
        newButton.addActionListener(this);
        modifyButton = new JButton();
        modifyButton.setToolTipText("Modify");
        modifyButton.setMargin(new Insets(0, 0, 0, 0));
        modifyButton.setIcon(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
        modifyButton.setActionCommand("Modify");
        modifyButton.addActionListener(this);
        deleteButton = new JButton();
        deleteButton.setToolTipText("Delete");
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteButton.setActionCommand("Delete");
        deleteButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        mainButtonToolBar.add(newButton);
        mainButtonToolBar.add(modifyButton);
        mainButtonToolBar.add(deleteButton);
        mainButtonToolBar.add(exitButton);
        makeTable();
        makeTree();
        scriptInfoTableScrPane = UIFactory.createStrippedScrollPane(null);
        scriptInfoTableScrPane.setViewportView(scriptInfoTable.getTable());
        scriptInfoTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        scriptSplitPane.add(codeTreeScrPane, "left");
        scriptSplitPane.add(scriptInfoTableScrPane, "right");
        scriptSplitPane.setPreferredSize(new Dimension(550, 400));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainButtonToolBar, "North");
        getContentPane().add(scriptSplitPane, "Center");
    }

    public void makeTree()
    {
        try
        {
            topNodeObj = new TreeNodeObject("Script", "Script", "Script");
            topNode = new DefaultMutableTreeNode(topNodeObj);
            treeModel = new DefaultTreeModel(topNode);
            scriptTree = new JTree(treeModel);
            treeSelectionListener = new CodeTreeListener();
            scriptTree.addTreeSelectionListener(treeSelectionListener);
            if(scriptTree == null)
                return;
            ObjectModelDefaultTreeCellRenderer renderer = new ObjectModelDefaultTreeCellRenderer();
            scriptTree.setCellRenderer(renderer);
            scriptTree.getSelectionModel().setSelectionMode(1);
            scriptTree.putClientProperty("JTree.lineStyle", "Angled");
            codeTreeScrPane.setViewportView(scriptTree);
            setExpand(topNode);
            scriptTree.setSelectionRow(1);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    private void setExpand(DefaultMutableTreeNode node)
    {
        String scriptName = "";
        scriptName = "Python";
        TreeNodeObject scriptdata = new TreeNodeObject("Python", "Python", "py");
        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(scriptdata);
        setInsertNode(node, childnode);
        scriptName = "Tcl";
        scriptdata = new TreeNodeObject("Tcl", "Tcl", "tcl");
        childnode = new DefaultMutableTreeNode(scriptdata);
        setInsertNode(node, childnode);
        scriptName = "Beanshell";
        scriptdata = new TreeNodeObject("Beanshell", "Beanshell", "java");
        childnode = new DefaultMutableTreeNode(scriptdata);
        setInsertNode(node, childnode);
        scriptTree.scrollPathToVisible(new TreePath(childnode.getPath()));
        setExpand(childnode, scriptName);
    }

    private void setExpand(DefaultMutableTreeNode node, String scriptName)
    {
        ArrayList scriptListAL = new ArrayList();
        try
        {
            scriptListAL = dss.listFile("/script");
            if(scriptListAL != null && !scriptListAL.isEmpty())
            {
                for(int i = 0; i < scriptListAL.size(); i++)
                {
                    java.util.List testList = Utils.tokenizeMessage((String)scriptListAL.get(i), '.');
                    if(scriptName.equals("Python") && ((String)testList.get(1)).equals("py") || scriptName.equals("Tcl") && ((String)testList.get(1)).equals("tcl") || scriptName.equals("Beanshell") && ((String)testList.get(1)).equals("java"))
                    {
                        TreeNodeObject scriptdata = new TreeNodeObject((String)testList.get(0), (String)testList.get(0), (String)testList.get(1));
                        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(scriptdata);
                        setInsertNode(node, childnode);
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
        DefaultTreeModel model = (DefaultTreeModel)scriptTree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    public void makeTable()
    {
        scriptInfoData = new ArrayList();
        scriptInfoColumnName = new ArrayList();
        scriptInfoColumnWidth = new ArrayList();
        scriptInfoColumnName.add("Classification");
        scriptInfoColumnName.add("Ouid");
        scriptInfoColumnName.add("EventType");
        scriptInfoColumnWidth.add(new Integer(90));
        scriptInfoColumnWidth.add(new Integer(90));
        scriptInfoColumnWidth.add(new Integer(90));
        scriptInfoTable = new Table(scriptInfoData, (ArrayList)scriptInfoColumnName.clone(), (ArrayList)scriptInfoColumnWidth.clone(), 0);
        scriptInfoTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        scriptInfoTable.setIndexColumn(0);
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Add"))
        {
            DefaultMutableTreeNode parentNode = null;
            TreePath parentPath = scriptTree.getSelectionPath();
            if(parentPath == null)
                parentNode = topNode;
            else
                parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
        } else
        if(command.equals("Delete"))
            try
            {
                DefaultTreeModel model = (DefaultTreeModel)scriptTree.getModel();
                DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)scriptTree.getLastSelectedPathComponent();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selnode.getParent();
                TreeNodeObject nodeData = (TreeNodeObject)parent.getUserObject();
                String extType = nodeData.getOuid();
                nodeData = (TreeNodeObject)selnode.getUserObject();
                String scriptName = nodeData.getOuid();
                model.removeNodeFromParent(selnode);
                dos.removeScript(extType, scriptName);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("Modify"))
        {
            Notepad newEditor = null;
            try
            {
                DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)scriptTree.getLastSelectedPathComponent();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selnode.getParent();
                TreeNodeObject nodeData = (TreeNodeObject)parent.getUserObject();
                String extType = nodeData.getOuid();
                nodeData = (TreeNodeObject)selnode.getUserObject();
                String scriptName = nodeData.getOuid();
                if(extType.equals("Python"))
                    scriptName = scriptName + ".py";
                else
                if(extType.equals("Tcl"))
                    scriptName = scriptName + ".tcl";
                else
                if(extType.equals("Beanshell"))
                    scriptName = scriptName + ".java";
                dss.downloadFileForce("/script/" + scriptName, "tmp/" + scriptName, null);
                File newScriptFile = new File("tmp/" + scriptName);
                if(newScriptFile.exists())
                    newEditor = new Notepad(newScriptFile);
                else
                    newEditor = new Notepad(null);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        if(command.equals("Exit"))
            closeEvent();
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

    public void windowClosing(WindowEvent windowEvent)
    {
        closeEvent();
    }

    public void closeEvent()
    {
        newButton.removeActionListener(this);
        modifyButton.removeActionListener(this);
        deleteButton.removeActionListener(this);
        exitButton.removeActionListener(this);
        scriptInfoTable.getTable().removeMouseListener(this);
        dispose();
    }

    public void displayInformation(String scriptName, String extType)
    {
        try
        {
            scriptInfoData.clear();
            if(scriptName != null)
            {
                ArrayList scriptInfoList = dos.listObjectUseScript(extType, scriptName);
                for(int i = 0; i < scriptInfoList.size(); i++)
                {
                    ArrayList tempAL = new ArrayList();
                    tempAL.add(((DOSChangeable)scriptInfoList.get(i)).get("classification"));
                    tempAL.add(((DOSChangeable)scriptInfoList.get(i)).get("ouid"));
                    tempAL.add(((DOSChangeable)scriptInfoList.get(i)).get("event.type"));
                    scriptInfoData.add(tempAL.clone());
                    tempAL.clear();
                }

            }
            scriptInfoTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    private final int DIVIDERSIZE = 3;
    private final int DIVIDERLOCATION = 250;
    private final int TITLE_WIDTH = 130;
    private DOS dos;
    private DSS dss;
    private JToolBar mainButtonToolBar;
    private JSplitPane scriptSplitPane;
    private JButton newButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JScrollPane codeTreeScrPane;
    private JTree scriptTree;
    private DefaultMutableTreeNode topNode;
    private DefaultTreeModel treeModel;
    private TreeNodeObject topNodeObj;
    private JScrollPane scriptInfoTableScrPane;
    private Table scriptInfoTable;
    private ArrayList scriptInfoData;
    private ArrayList scriptInfoColumnName;
    private ArrayList scriptInfoColumnWidth;
    private JPopupMenu popupMenu;
    private JMenu addMenu;
    private JMenuItem modifyMenuItem;
    private JMenuItem addMenuItem;
    private JMenuItem deleteMenuItem;
    private String modelOuid;
    private DOSChangeable modelInfoDC;
    private DOSChangeable codeInfoDC;
    private TreeSelectionListener treeSelectionListener;
    final String MODIFY_MENU = "\uC218\uC815";
    final String ADD_MENU = "\uCD94\uAC00";
    final String DELETE_MENU = "\uC0AD\uC81C";




}
