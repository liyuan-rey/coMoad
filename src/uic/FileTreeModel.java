// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileTreeModel.java

package dyna.uic;

import dyna.framework.client.DynaMOAD;
import dyna.framework.service.DOS;
import dyna.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.uic:
//            AbstractTreeTableModel, TreeTableModel, TreeNodeObject

public class FileTreeModel extends AbstractTreeTableModel
    implements TreeTableModel
{

    public FileTreeModel(DefaultMutableTreeNode rootNode)
    {
        AbstractTreeTableModel(rootNode);
        valueList = new ArrayList();
        nodeOuid = "";
        columnOuidList = null;
        columnWidthList = null;
        treeNode = rootNode;
        dos = DynaMOAD.dos;
        if(treeNode == null)
        {
            return;
        } else
        {
            ArrayList columnNameList = null;
            ArrayList columnClassList = null;
            columnNameList = new ArrayList();
            columnWidthList = new ArrayList();
            columnClassList = new ArrayList();
            columnNameList.add(DynaMOAD.getMSRString("file", "File", 3));
            columnWidthList.add(new Integer(170));
            columnClassList.add(dyna.uic.TreeTableModel.class);
            columnNameList.add(DynaMOAD.getMSRString("description", "Description", 3));
            columnWidthList.add(new Integer(80));
            columnClassList.add(java.lang.String.class);
            columnNameList.add(DynaMOAD.getMSRString("vf$version", "Version", 3));
            columnWidthList.add(new Integer(30));
            columnClassList.add(java.lang.String.class);
            columnNameList.add(DynaMOAD.getMSRString("md$checkedin.date", "Checked-In Time", 3));
            columnWidthList.add(new Integer(110));
            columnClassList.add(java.lang.String.class);
            columnNameList.add(DynaMOAD.getMSRString("md$checkedout.date", "Checked-Out Time", 3));
            columnWidthList.add(new Integer(110));
            columnClassList.add(java.lang.String.class);
            columnNameList.add(DynaMOAD.getMSRString("md$user", "User", 3));
            columnWidthList.add(new Integer(60));
            columnClassList.add(java.lang.String.class);
            cNames = new String[columnNameList.size()];
            columnNameList.toArray(cNames);
            cWidth = new Integer[columnWidthList.size()];
            columnWidthList.toArray(cWidth);
            cTypes = new Class[columnClassList.size()];
            columnClassList.toArray(cTypes);
            columnNameList.clear();
            columnNameList = null;
            columnClassList.clear();
            columnClassList = null;
            return;
        }
    }

    protected DefaultMutableTreeNode getNode(Object node)
    {
        return (DefaultMutableTreeNode)node;
    }

    protected Object[] getChildren(Object node)
    {
        int size = ((DefaultMutableTreeNode)node).getChildCount();
        children = new Object[size];
        for(int i = 0; i < ((DefaultMutableTreeNode)node).getChildCount(); i++)
        {
            javax.swing.tree.TreeNode test = ((DefaultMutableTreeNode)node).getChildAt(i);
            children[i] = test;
        }

        return children;
    }

    public int getChildCount(Object node)
    {
        Object children[] = getChildren(node);
        return children != null ? children.length : 0;
    }

    public Object getChild(Object node, int i)
    {
        return getChildren(node)[i];
    }

    public int getColumnCount()
    {
        return cNames.length;
    }

    public String getColumnName(int column)
    {
        return cNames[column];
    }

    public ArrayList getColumnWidth()
    {
        return columnWidthList;
    }

    public Class getColumnClass(int column)
    {
        return cTypes[column];
    }

    public Object getValueAt(Object node, int column)
    {
        DefaultMutableTreeNode treeNode = getNode(node);
        HashMap row = (HashMap)((TreeNodeObject)treeNode.getUserObject()).getLawData();
        if(row == null || row.size() == 0)
            return null;
        ArrayList values = new ArrayList();
        String userId = null;
        values.add(row.get("md$ouid"));
        values.add(row.get("md$filetype.id"));
        values.add(row.get("md$description"));
        values.add(row.get("md$version"));
        values.add(row.get("md$checkedin.date"));
        values.add(row.get("md$checkedout.date"));
        userId = (String)row.get("md$user.id");
        if(!Utils.isNullString(userId))
            values.add(row.get("md$user.name") + " (" + row.get("md$user.id") + ")");
        else
            values.add(null);
        if(values.size() > column)
            return values.get(column + 1);
        else
            return null;
    }

    public String toString()
    {
        return treeNode.toString();
    }

    protected Object[] getChildren()
    {
        return children;
    }

    protected static String cNames[] = null;
    protected static Integer cWidth[] = null;
    protected static Class cTypes[] = null;
    public static final Integer ZERO = new Integer(0);
    private DefaultMutableTreeNode treeNode;
    private Object children[];
    private ArrayList columnValue;
    private Object treeNodeObject;
    private DOS dos;
    private ArrayList resultList;
    public ArrayList valueList;
    public String nodeOuid;
    private ArrayList columnOuidList;
    private ArrayList columnWidthList;

}
