// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkflowTreeModel.java

package dyna.framework.editor.workflow;

import dyna.framework.service.dos.DOSChangeable;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            WorkflowTreeNode

public class WorkflowTreeModel extends DefaultTreeModel
{

    public WorkflowTreeModel(TreeNode rootNode)
    {
        super(new WorkflowTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public WorkflowTreeModel(TreeNode rootNode, boolean asksAllowsChildren)
    {
        super(new WorkflowTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()), asksAllowsChildren);
    }

    public WorkflowTreeModel(DOSChangeable rootNode)
    {
        super(new WorkflowTreeNode(rootNode));
    }

    public WorkflowTreeModel(DOSChangeable rootNode, boolean asksAllowsChildren)
    {
        super(new WorkflowTreeNode(rootNode), asksAllowsChildren);
    }

    public void setRoot(TreeNode rootNode)
    {
        super.setRoot(new WorkflowTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public void setRoot(DOSChangeable rootNode)
    {
        super.setRoot(new WorkflowTreeNode(rootNode));
    }
}
