// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PackageSelection.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, ObjectModelDefaultTreeCellRenderer

public class PackageSelection extends JDialog
    implements ActionListener
{

    public PackageSelection(ObjectModelingConstruction mainFrame)
    {
        super(mainFrame, false);
        dos = null;
        this.mainFrame = null;
        modelTable = null;
        modelDataAL = null;
        mainPanel = null;
        buttonToolBar = null;
        toolBarBoxLayout = null;
        selectButton = null;
        exitButton = null;
        treeViewScrPane = UIFactory.createStrippedScrollPane(null);
        dataModel = null;
        tree = null;
        topNode = null;
        treeModel = null;
        try
        {
            this.mainFrame = mainFrame;
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            getAllModel();
            setTopNode(new TreeNodeObject("Model", "Model", "Model"));
            setTree();
            initialize();
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public void setTopNode(TreeNodeObject rootNode)
    {
        topNode = new DefaultMutableTreeNode(rootNode);
        treeModel = new DefaultTreeModel(topNode);
        tree = new JTree(treeModel);
    }

    public void setTree()
    {
        try
        {
            String workingModelOuid = dos.getWorkingModel();
            ObjectModelDefaultTreeCellRenderer renderer = new ObjectModelDefaultTreeCellRenderer();
            tree.setCellRenderer(renderer);
            for(int i = 0; i < modelDataAL.size(); i++)
            {
                DefaultMutableTreeNode modelTreeNode = null;
                DOSChangeable modelInfo = (DOSChangeable)modelDataAL.get(i);
                String ouid = (String)modelInfo.get("ouid");
                if(!ouid.equals(workingModelOuid))
                {
                    TreeNodeObject modelObject = new TreeNodeObject((String)modelInfo.get("ouid"), (String)modelInfo.get("name"), (String)modelInfo.get("description"));
                    modelTreeNode = new DefaultMutableTreeNode(modelObject);
                    setInsertNode(topNode, modelTreeNode);
                    tree.scrollPathToVisible(new TreePath(modelTreeNode.getPath()));
                    setExpand(modelTreeNode, ouid);
                }
            }

            treeViewScrPane.setViewportView(tree);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setExpand(DefaultMutableTreeNode modelTreeNode, String modelOuid)
    {
        try
        {
            ArrayList packageList = dos.listPackage(modelOuid);
            for(int i = 0; i < packageList.size(); i++)
            {
                DefaultMutableTreeNode packageTreeNode = null;
                DOSChangeable packageInfo = (DOSChangeable)packageList.get(i);
                TreeNodeObject packageObject = new TreeNodeObject((String)packageInfo.get("ouid"), (String)packageInfo.get("name"), "Package");
                packageTreeNode = new DefaultMutableTreeNode(packageObject);
                setInsertNode(modelTreeNode, packageTreeNode);
                tree.scrollPathToVisible(new TreePath(packageTreeNode.getPath()));
            }

        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    public void initialize()
    {
        setSize(300, 400);
        setTitle("Package Link");
        treeViewScrPane.setViewportView(tree);
        buttonToolBar = new JToolBar();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar.setLayout(toolBarBoxLayout);
        selectButton = new JButton();
        selectButton.setToolTipText("Select");
        selectButton.setText("Select");
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(selectButton);
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "North");
        getContentPane().add(treeViewScrPane, "Center");
    }

    public void getAllModel()
    {
        try
        {
            ArrayList packageDataAL = new ArrayList();
            modelDataAL = dos.listModel();
            System.out.println(modelDataAL);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            exitButton_actionPerformed(e);
        else
        if(command.equals("Select"))
            selectButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void selectButton_actionPerformed(ActionEvent e)
    {
        try
        {
            DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(selnode != null && selnode.getLevel() == 2)
            {
                TreeNodeObject selectedObject = (TreeNodeObject)selnode.getUserObject();
                String workingModelOuid = dos.getWorkingModel();
                String packageOuid = selectedObject.getOuid();
                dos.linkPackageAndModel(workingModelOuid, packageOuid);
                DOSChangeable dosModel = dos.getModel(workingModelOuid);
                String ouid = (String)dosModel.get("ouid");
                String name = (String)dosModel.get("name");
                String description = (String)dosModel.get("description");
                TreeNodeObject rootNode = new TreeNodeObject(ouid, name, description);
                mainFrame.makeObjectSetTree(rootNode, 1);
                mainFrame.displayInformation(ouid, "", 0, 0);
                dispose();
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        dispose();
    }

    private DOS dos;
    private ObjectModelingConstruction mainFrame;
    private Table modelTable;
    private ArrayList modelDataAL;
    private JPanel mainPanel;
    private JToolBar buttonToolBar;
    private BoxLayout toolBarBoxLayout;
    private JButton selectButton;
    private JButton exitButton;
    private JScrollPane treeViewScrPane;
    private ArrayList dataModel;
    private JTree tree;
    private DefaultMutableTreeNode topNode;
    private DefaultTreeModel treeModel;
}
