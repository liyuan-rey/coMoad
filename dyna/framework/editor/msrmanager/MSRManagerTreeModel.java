// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MSRManagerTreeModel.java

package dyna.framework.editor.msrmanager;

import dyna.framework.service.dos.DOSChangeable;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.msrmanager:
//            MSRManagerTreeNode

public class MSRManagerTreeModel extends DefaultTreeModel
{

    public MSRManagerTreeModel(TreeNode rootNode)
    {
        super(new MSRManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public MSRManagerTreeModel(TreeNode rootNode, boolean asksAllowsChildren)
    {
        super(new MSRManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()), asksAllowsChildren);
    }

    public MSRManagerTreeModel(DOSChangeable rootNode)
    {
        super(new MSRManagerTreeNode(rootNode));
    }

    public MSRManagerTreeModel(DOSChangeable rootNode, boolean asksAllowsChildren)
    {
        super(new MSRManagerTreeNode(rootNode), asksAllowsChildren);
    }

    public void setRoot(TreeNode rootNode)
    {
        super.setRoot(new MSRManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public void setRoot(DOSChangeable rootNode)
    {
        super.setRoot(new MSRManagerTreeNode(rootNode));
    }
}
