// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectInsertFrame.java

package dyna.framework.editor.modeler;

import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.TreeNodeObject;
import dyna.uic.UIUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.framework.editor.modeler:
//            PackageInformation, ClassInformation, FieldGroupInformation, FieldInformation, 
//            ActionInformation, CodeInformation, ModelInformation, ObjectModelingConstruction, 
//            ObjectModelingDefinition, CodeManager, ActionParameterInformation

public class ObjectInsertFrame extends JFrame
    implements ActionListener
{

    public ObjectInsertFrame(Object contentPanel, ObjectModelingDefinition parentDefinition, DefaultMutableTreeNode parent)
    {
        buttonToolBar = null;
        toolBarBoxLayout = null;
        registButton = null;
        exitButton = null;
        modelInfoInsert = null;
        packageInfoInsert = null;
        classInfoInsert = null;
        fieldGroupInfoInsert = null;
        fieldInfoInsert = null;
        actionInfoInsert = null;
        actionParameterInfoInsert = null;
        codeInfoInsert = null;
        this.parentDefinition = null;
        parentConstruction = null;
        parentManager = null;
        this.parent = null;
        this.parentDefinition = parentDefinition;
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        this.parent = parent;
        if(contentPanel instanceof PackageInformation)
            packageInfoInsert = (PackageInformation)contentPanel;
        else
        if(contentPanel instanceof ClassInformation)
            classInfoInsert = (ClassInformation)contentPanel;
        else
        if(contentPanel instanceof FieldGroupInformation)
            fieldGroupInfoInsert = (FieldGroupInformation)contentPanel;
        else
        if(contentPanel instanceof FieldInformation)
            fieldInfoInsert = (FieldInformation)contentPanel;
        else
        if(contentPanel instanceof ActionInformation)
            actionInfoInsert = (ActionInformation)contentPanel;
        else
        if(contentPanel instanceof CodeInformation)
            codeInfoInsert = (CodeInformation)contentPanel;
        initialize();
    }

    public ObjectInsertFrame(Object contentPanel, CodeManager parentManager, DefaultMutableTreeNode parent)
    {
        buttonToolBar = null;
        toolBarBoxLayout = null;
        registButton = null;
        exitButton = null;
        modelInfoInsert = null;
        packageInfoInsert = null;
        classInfoInsert = null;
        fieldGroupInfoInsert = null;
        fieldInfoInsert = null;
        actionInfoInsert = null;
        actionParameterInfoInsert = null;
        codeInfoInsert = null;
        parentDefinition = null;
        parentConstruction = null;
        this.parentManager = null;
        this.parent = null;
        this.parentManager = parentManager;
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        this.parent = parent;
        if(contentPanel instanceof CodeInformation)
            codeInfoInsert = (CodeInformation)contentPanel;
        initialize();
    }

    public ObjectInsertFrame(ObjectModelingConstruction parentConstruction)
    {
        buttonToolBar = null;
        toolBarBoxLayout = null;
        registButton = null;
        exitButton = null;
        modelInfoInsert = null;
        packageInfoInsert = null;
        classInfoInsert = null;
        fieldGroupInfoInsert = null;
        fieldInfoInsert = null;
        actionInfoInsert = null;
        actionParameterInfoInsert = null;
        codeInfoInsert = null;
        parentDefinition = null;
        this.parentConstruction = null;
        parentManager = null;
        parent = null;
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        this.parentConstruction = parentConstruction;
        modelInfoInsert = new ModelInformation(this.parentConstruction);
        initialize();
    }

    public void initialize()
    {
        setLocation(300, 200);
        setSize(400, 400);
        setTitle("Insert");
        buttonToolBar = new JToolBar();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar.setLayout(toolBarBoxLayout);
        registButton = new JButton();
        registButton.setToolTipText("Regist");
        registButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        registButton.setText("Ok");
        registButton.setMargin(new Insets(0, 0, 0, 0));
        registButton.setActionCommand("Regist");
        registButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar.add(registButton);
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "North");
        if(parentConstruction != null)
            getContentPane().add(modelInfoInsert, "Center");
        else
        if(packageInfoInsert != null)
            getContentPane().add(packageInfoInsert, "Center");
        else
        if(classInfoInsert != null)
            getContentPane().add(classInfoInsert, "Center");
        else
        if(fieldGroupInfoInsert != null)
            getContentPane().add(fieldGroupInfoInsert, "Center");
        else
        if(fieldInfoInsert != null)
            getContentPane().add(fieldInfoInsert, "Center");
        else
        if(actionInfoInsert != null)
            getContentPane().add(actionInfoInsert, "Center");
        else
        if(actionParameterInfoInsert != null)
            getContentPane().add(actionParameterInfoInsert, "Center");
        else
        if(codeInfoInsert != null)
            getContentPane().add(codeInfoInsert, "Center");
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("Regist"))
            registButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void registButton_actionPerformed(ActionEvent e)
    {
        if(modelInfoInsert != null)
        {
            DOSChangeable createInfo = modelInfoInsert.createInformation();
            if(createInfo != null)
            {
                TreeNodeObject rootNode = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), "Model");
                parentConstruction.makeObjectSetTree(rootNode, 1);
                parentConstruction.displayInformation((String)createInfo.get("ouid"), "", 0, 0);
                dispose();
            }
        } else
        if(packageInfoInsert != null)
        {
            if(!ObjectModelingConstruction.validateToken())
                ObjectModelingConstruction.getInstance().reloadModel();
            DOSChangeable createInfo = packageInfoInsert.createInformation();
            if(createInfo != null)
            {
                TreeNodeObject codedata = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), "Package");
                parent = UIUtils.getRealSelectedNode(parentDefinition.getTree(), parent);
                parentDefinition.addTreeDrawing(codedata, parent);
                dispose();
            }
        } else
        if(classInfoInsert != null)
        {
            ArrayList folderList = new ArrayList();
            if(!ObjectModelingConstruction.validateToken())
                ObjectModelingConstruction.getInstance().reloadModel();
            DOSChangeable createInfo = classInfoInsert.createInformation();
            if(createInfo != null)
            {
                TreeNodeObject codedata = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), "Class");
                parent = UIUtils.getRealSelectedNode(parentDefinition.getTree(), parent);
                parentDefinition.addTreeDrawing((String)createInfo.get("ouid@package"), codedata, parent);
                dispose();
            }
        } else
        if(fieldGroupInfoInsert != null)
        {
            if(fieldGroupInfoInsert.mandatoryFieldCheck())
            {
                if(!ObjectModelingConstruction.validateToken())
                    ObjectModelingConstruction.getInstance().reloadModel();
                DOSChangeable createInfo = fieldGroupInfoInsert.createInformation();
                if(createInfo != null)
                {
                    TreeNodeObject codedata = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), "FGroup");
                    parent = UIUtils.getRealSelectedNode(parentDefinition.getTree(), parent);
                    parentDefinition.addTreeDrawing(codedata, parent);
                    dispose();
                }
            } else
            {
                JOptionPane.showMessageDialog(this, "Check the mandatory fields.", "Error", 0);
                return;
            }
        } else
        if(fieldInfoInsert != null)
        {
            if(fieldInfoInsert.mandatoryFieldCheck())
            {
                if(!ObjectModelingConstruction.validateToken())
                    ObjectModelingConstruction.getInstance().reloadModel();
                DOSChangeable createInfo = fieldInfoInsert.createInformation();
                if(createInfo != null)
                {
                    TreeNodeObject codedata = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), "Field");
                    parent = UIUtils.getRealSelectedNode(parentDefinition.getTree(), parent);
                    parentDefinition.addTreeDrawing(codedata, parent);
                    dispose();
                }
            } else
            {
                JOptionPane.showMessageDialog(this, "Check the mandatory fields.", "Error", 0);
                return;
            }
        } else
        if(actionInfoInsert != null)
        {
            if(actionInfoInsert.mandatoryFieldCheck())
            {
                if(!ObjectModelingConstruction.validateToken())
                    ObjectModelingConstruction.getInstance().reloadModel();
                DOSChangeable createInfo = actionInfoInsert.createInformation();
                if(createInfo != null)
                {
                    TreeNodeObject codedata = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), "Action");
                    parent = UIUtils.getRealSelectedNode(parentDefinition.getTree(), parent);
                    parentDefinition.addTreeDrawing(codedata, parent);
                    dispose();
                }
            } else
            {
                JOptionPane.showMessageDialog(this, "Check the mandatory fields.", "Error", 0);
                return;
            }
        } else
        if(codeInfoInsert != null)
        {
            DOSChangeable createInfo = codeInfoInsert.createCodeInformation();
            if(createInfo != null)
            {
                TreeNodeObject codedata = new TreeNodeObject((String)createInfo.get("ouid"), (String)createInfo.get("name"), (String)createInfo.get("description"));
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(codedata);
                if(parentDefinition != null)
                    parent = UIUtils.getRealSelectedNode(parentDefinition.getTree(), parent);
                parentManager.setInsertNode(parent, child);
                UIUtils.scrollTreePathToVisible(parentManager.getTree(), child);
                dispose();
            }
        }
        ObjectModelingConstruction.resetToken();
        ObjectModelingConstruction.validateToken();
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        dispose();
    }

    private JToolBar buttonToolBar;
    private BoxLayout toolBarBoxLayout;
    private JButton registButton;
    private JButton exitButton;
    private ModelInformation modelInfoInsert;
    private PackageInformation packageInfoInsert;
    private ClassInformation classInfoInsert;
    private FieldGroupInformation fieldGroupInfoInsert;
    private FieldInformation fieldInfoInsert;
    private ActionInformation actionInfoInsert;
    private ActionParameterInformation actionParameterInfoInsert;
    private CodeInformation codeInfoInsert;
    private ObjectModelingDefinition parentDefinition;
    private ObjectModelingConstruction parentConstruction;
    private CodeManager parentManager;
    private DefaultMutableTreeNode parent;
}
