// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserManagerTreeModel.java

package dyna.framework.editor.user;

import dyna.framework.service.dos.DOSChangeable;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.user:
//            UserManagerTreeNode

public class UserManagerTreeModel extends DefaultTreeModel
{

    public UserManagerTreeModel(TreeNode rootNode)
    {
        super(new UserManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public UserManagerTreeModel(TreeNode rootNode, boolean asksAllowsChildren)
    {
        super(new UserManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()), asksAllowsChildren);
    }

    public UserManagerTreeModel(DOSChangeable rootNode)
    {
        super(new UserManagerTreeNode(rootNode));
    }

    public UserManagerTreeModel(DOSChangeable rootNode, boolean asksAllowsChildren)
    {
        super(new UserManagerTreeNode(rootNode), asksAllowsChildren);
    }

    public void setRoot(TreeNode rootNode)
    {
        super.setRoot(new UserManagerTreeNode((DOSChangeable)((DefaultMutableTreeNode)rootNode).getUserObject()));
    }

    public void setRoot(DOSChangeable rootNode)
    {
        super.setRoot(new UserManagerTreeNode(rootNode));
    }
}
