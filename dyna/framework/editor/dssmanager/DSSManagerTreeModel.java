// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSSManagerTreeModel.java

package dyna.framework.editor.dssmanager;

import dyna.framework.service.dos.DOSChangeable;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.dssmanager:
//            DSSManagerTreeNode

public class DSSManagerTreeModel extends DefaultTreeModel
{

    public DSSManagerTreeModel(TreeNode rootNode)
    {
        super(new DSSManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public DSSManagerTreeModel(TreeNode rootNode, boolean asksAllowsChildren)
    {
        super(new DSSManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()), asksAllowsChildren);
    }

    public DSSManagerTreeModel(DOSChangeable rootNode)
    {
        super(new DSSManagerTreeNode(rootNode));
    }

    public DSSManagerTreeModel(DOSChangeable rootNode, boolean asksAllowsChildren)
    {
        super(new DSSManagerTreeNode(rootNode), asksAllowsChildren);
    }

    public void setRoot(TreeNode rootNode)
    {
        super.setRoot(new DSSManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public void setRoot(DOSChangeable rootNode)
    {
        super.setRoot(new DSSManagerTreeNode(rootNode));
    }
}
