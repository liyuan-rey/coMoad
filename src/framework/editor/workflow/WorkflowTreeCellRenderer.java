// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkflowTreeCellRenderer.java

package dyna.framework.editor.workflow;

import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class WorkflowTreeCellRenderer extends DefaultTreeCellRenderer
{

    public WorkflowTreeCellRenderer()
    {
        iconMap = null;
        iconMap = new HashMap();
        getClass();
        iconMap.put("model", new ImageIcon("icons/Model.gif"));
        getClass();
        iconMap.put("process", new ImageIcon("icons/Process.gif"));
        getClass();
        iconMap.put("activity", new ImageIcon("icons/Activity.gif"));
        getClass();
        iconMap.put("library", new ImageIcon("icons/Model.gif"));
        getClass();
        iconMap.put("application", new ImageIcon("icons/Model.gif"));
        getClass();
        iconMap.put("wait", new ImageIcon("icons/Sandglass.gif"));
        getClass();
        iconMap.put("unknown", new ImageIcon("icons/Class.gif"));
        setClosedIcon(null);
        setOpenIcon(null);
        setLeafIcon(null);
        setDisabledIcon(null);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expended, boolean leaf, int row, boolean hasFocus)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        if(node == null)
            return null;
        Component retValue = super.getTreeCellRendererComponent(tree, value, sel, expended, leaf, row, hasFocus);
        Object userObject = node.getUserObject();
        DOSChangeable dosObject = null;
        String objectType = null;
        if(userObject instanceof DOSChangeable)
        {
            dosObject = (DOSChangeable)userObject;
            objectType = (String)dosObject.get("object.type");
            if(Utils.isNullString(objectType))
            {
                getClass();
                setIcon((ImageIcon)iconMap.get("unknown"));
                dosObject = null;
                userObject = null;
                return retValue;
            }
            setIcon((ImageIcon)iconMap.get(objectType));
            dosObject = null;
        } else
        if(userObject instanceof Boolean)
        {
            getClass();
            setIcon((ImageIcon)iconMap.get("wait"));
        } else
        {
            getClass();
            setIcon((ImageIcon)iconMap.get("unknown"));
        }
        userObject = null;
        return retValue;
    }

    private final String modelIcon = "model";
    private final String processIcon = "process";
    private final String activityIcon = "activity";
    private final String libraryIcon = "library";
    private final String applicationIcon = "application";
    private final String waitIcon = "wait";
    private final String unknownIcon = "unknown";
    private HashMap iconMap;
}
