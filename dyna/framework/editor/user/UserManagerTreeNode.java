// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserManagerTreeNode.java

package dyna.framework.editor.user;

import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import javax.swing.tree.DefaultMutableTreeNode;

public class UserManagerTreeNode extends DefaultMutableTreeNode
{

    public UserManagerTreeNode()
    {
    }

    public UserManagerTreeNode(Object userObject)
    {
        super(userObject);
    }

    public String toString()
    {
        if(userObject == null)
            return "Unknown";
        if(userObject instanceof Boolean)
            return "Please wait...";
        DOSChangeable dosObject = (DOSChangeable)userObject;
        if(!Utils.isNullString((String)dosObject.get("identifier")))
            return (String)dosObject.get("name") + " [" + (String)dosObject.get("identifier") + "]";
        else
            return (String)dosObject.get("name");
    }
}
