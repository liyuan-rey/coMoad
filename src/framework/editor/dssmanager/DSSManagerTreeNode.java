// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSSManagerTreeNode.java

package dyna.framework.editor.dssmanager;

import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import javax.swing.tree.DefaultMutableTreeNode;

public class DSSManagerTreeNode extends DefaultMutableTreeNode
{

    public DSSManagerTreeNode()
    {
    }

    public DSSManagerTreeNode(Object userObject)
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
