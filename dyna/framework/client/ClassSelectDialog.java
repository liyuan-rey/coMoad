// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ClassSelectDialog.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.TreeNodeObject;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, MainFrame, DFDefaultTreeCellRenderer, AuthoritySearch, 
//            AdvancedFilterDialogForList, UIManagement

public class ClassSelectDialog extends JDialog
{
    class ListenerCommon extends WindowAdapter
        implements ActionListener, MouseListener
    {

        public void windowClosing(WindowEvent e)
        {
            closeDialog();
        }

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if(command.equals("Select"))
                selectButtonActionPerformed(e);
            else
            if(command.equals("Close"))
                closeButtonActionPerformed(e);
        }

        public void mouseClicked(MouseEvent e)
        {
            Object source = e.getSource();
            source.equals(classTree);
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

        ListenerCommon()
        {
        }
    }


    public ClassSelectDialog(Frame frame, Object parent, boolean modal)
    {
        super(frame, modal);
        newUI = null;
        dos = null;
        modelOuid = null;
        parentWindow = null;
        listenerCommon = null;
        parentWindow = parent;
        dos = DynaMOAD.dos;
        newUI = DynaMOAD.newUI;
        listenerCommon = new ListenerCommon();
        initComponents();
        System.out.println("ClassSelectDialog");
    }

    private void initComponents()
    {
        setDefaultCloseOperation(2);
        setTitle(DynaMOAD.getMSRString("WRD_0104", "Class Select", 3));
        setResizable(false);
        addWindowListener(listenerCommon);
        selectButton = new JButton();
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(listenerCommon);
        closeButton = new JButton();
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(listenerCommon);
        buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(closeButton);
        setTree();
        treeScrPane = UIFactory.createStrippedScrollPane(null);
        treeScrPane.setPreferredSize(new Dimension(250, 250));
        treeScrPane.setViewportView(classTree);
        getContentPane().add(treeScrPane, "North");
        getContentPane().add(buttonPanel, "Center");
        pack();
    }

    public void setTree()
    {
        TreeNodeObject nodeData = MainFrame.rootnodedata;
        modelOuid = nodeData.getOuid();
        topNode = new DefaultMutableTreeNode(nodeData);
        treeModel = new DefaultTreeModel(topNode);
        classTree = new JTree(treeModel);
        classTree.setCellRenderer(new DFDefaultTreeCellRenderer());
        classTree.getSelectionModel().setSelectionMode(1);
        classTree.putClientProperty("JTree.lineStyle", "Angled");
        setExpand(topNode);
    }

    private void setExpand(DefaultMutableTreeNode node)
    {
        String ouid = "";
        TreeNodeObject nodeData = null;
        ArrayList rootClassList = new ArrayList();
        DOSChangeable classInfoDC = new DOSChangeable();
        DefaultMutableTreeNode childnode = null;
        boolean isLeaf = false;
        try
        {
            rootClassList = dos.listRootClassInModel(modelOuid);
            if(rootClassList != null && !rootClassList.isEmpty())
            {
                for(int i = 0; i < rootClassList.size(); i++)
                {
                    classInfoDC = (DOSChangeable)rootClassList.get(i);
                    isLeaf = ((Boolean)classInfoDC.get("is.leaf")).booleanValue();
                    ouid = (String)classInfoDC.get("ouid");
                    nodeData = new TreeNodeObject(ouid, (String)classInfoDC.get("title"), "Class");
                    childnode = new DefaultMutableTreeNode(nodeData);
                    setInsertNode(node, childnode);
                    setExpand(childnode, ouid);
                    if(!isLeaf && childnode.getChildCount() == 0)
                        childnode.removeFromParent();
                    classTree.scrollPathToVisible(new TreePath(childnode.getPath()));
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
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

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)classTree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    private void closeButtonActionPerformed(ActionEvent evt)
    {
        if(parentWindow instanceof AuthoritySearch)
            ((AuthoritySearch)parentWindow).setClass(null, null);
        else
        if(parentWindow instanceof AdvancedFilterDialogForList)
            ((AdvancedFilterDialogForList)parentWindow).setClass(null, null);
        closeDialog();
    }

    private void selectButtonActionPerformed(ActionEvent evt)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)classTree.getLastSelectedPathComponent();
        if(node == null)
            return;
        TreeNodeObject tmpdata = (TreeNodeObject)node.getUserObject();
        if(parentWindow instanceof AuthoritySearch)
            ((AuthoritySearch)parentWindow).setClass(tmpdata.getOuid(), tmpdata.getName());
        else
        if(parentWindow instanceof AdvancedFilterDialogForList)
            ((AdvancedFilterDialogForList)parentWindow).setClass(tmpdata.getOuid(), tmpdata.getName());
        closeDialog();
    }

    private void closeDialog()
    {
        selectButton.removeActionListener(listenerCommon);
        closeButton.removeActionListener(listenerCommon);
        removeWindowListener(listenerCommon);
        setVisible(false);
        dispose();
    }

    private UIManagement newUI;
    private DOS dos;
    private String modelOuid;
    private Object parentWindow;
    private ListenerCommon listenerCommon;
    private JPanel buttonPanel;
    private JButton selectButton;
    private JButton closeButton;
    private JScrollPane treeScrPane;
    private JTree classTree;
    private DefaultMutableTreeNode topNode;
    private DefaultTreeModel treeModel;




}