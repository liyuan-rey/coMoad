// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LinkTreeModel.java

package dyna.uic;

import dyna.framework.client.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.uic:
//            AbstractTreeTableModel, TreeTableModel, TreeNodeObject

public class LinkTreeModel extends AbstractTreeTableModel
    implements TreeTableModel
{

    public LinkTreeModel(DefaultMutableTreeNode rootNode, ArrayList assoList, int mode)
    {
        AbstractTreeTableModel(rootNode);
        cNames = null;
        cTypes = null;
        valueList = new ArrayList();
        nodeOuid = "";
        columnOuidList = null;
        columnWidthList = null;
        columnList = null;
        this.mode = 0;
        treeNode = rootNode;
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        this.mode = mode;
        if(treeNode == null)
            return;
        String classOuid = null;
        String fieldOuid = null;
        String fieldCode = null;
        ArrayList classList = null;
        ArrayList fieldList = null;
        ArrayList tmpFieldList = null;
        ArrayList columnNameList = null;
        ArrayList columnClassList = null;
        Iterator assoKey = null;
        Iterator classKey = null;
        Iterator fieldKey = null;
        DOSChangeable dosAssociation = null;
        DOSChangeable dosField = null;
        Boolean isVisible = null;
        byte dataType = 0;
        try
        {
            if(assoList == null)
                return;
            classList = new ArrayList();
            for(assoKey = assoList.iterator(); assoKey.hasNext();)
            {
                dosAssociation = (DOSChangeable)assoKey.next();
                if(this.mode == 0)
                    classOuid = (String)dosAssociation.get("end2.ouid@class");
                else
                    classOuid = (String)dosAssociation.get("end1.ouid@class");
                if(Utils.isNullString(classOuid) || classList.contains(classOuid))
                {
                    dosAssociation = null;
                } else
                {
                    classList.add(classOuid);
                    classOuid = (String)dosAssociation.get("ouid@class");
                    if(Utils.isNullString(classOuid) || classList.contains(classOuid))
                    {
                        dosAssociation = null;
                    } else
                    {
                        classList.add(classOuid);
                        dosAssociation = null;
                    }
                }
            }

            assoKey = null;
            if(classList.size() == 0)
            {
                classList = null;
                return;
            }
            columnOuidList = new ArrayList();
            columnNameList = new ArrayList();
            columnWidthList = new ArrayList();
            columnClassList = new ArrayList();
            columnList = new ArrayList();
            fieldList = new ArrayList();
            String value = null;
            for(classKey = classList.iterator(); classKey.hasNext();)
            {
                classOuid = (String)classKey.next();
                tmpFieldList = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
                if(tmpFieldList != null)
                {
                    for(int i = 0; i < tmpFieldList.size(); i++)
                    {
                        value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid + "/" + (String)tmpFieldList.get(i));
                        dosField = dos.getField(value);
                        fieldList.add(dosField);
                        dosField = null;
                    }

                } else
                {
                    tmpFieldList = UIBuilder.getDefaultFieldList(dos, classOuid);
                    if(tmpFieldList != null && tmpFieldList.size() != 0)
                    {
                        fieldList.addAll(tmpFieldList);
                        tmpFieldList = null;
                    }
                }
            }

            classKey = null;
            Utils.sort(fieldList);
            fieldKey = fieldList.iterator();
            while(fieldKey.hasNext()) 
            {
                dosField = (DOSChangeable)fieldKey.next();
                isVisible = (Boolean)dosField.get("is.visible");
                if(isVisible == null || Utils.getBoolean(isVisible))
                {
                    if(columnOuidList.contains(dosField.get("ouid")))
                    {
                        dosField = null;
                        continue;
                    }
                    fieldOuid = (String)dosField.get("ouid");
                    fieldCode = (String)dosField.get("code");
                    columnOuidList.add(fieldOuid);
                    columnNameList.add(DynaMOAD.getMSRString(fieldCode, (String)dosField.get("title"), 0));
                    columnList.add(dosField);
                    if(fieldCode.equals("md$number"))
                    {
                        columnWidthList.add(new Integer(120));
                        columnClassList.add(dyna.uic.TreeTableModel.class);
                        dosField = null;
                        continue;
                    }
                    columnWidthList.add(new Integer(80));
                    dataType = Utils.getByte((Byte)dosField.get("type"));
                    switch(dataType)
                    {
                    case 1: // '\001'
                        columnClassList.add(java.lang.Boolean.class);
                        break;

                    case 2: // '\002'
                        columnClassList.add(java.lang.Byte.class);
                        break;

                    case 3: // '\003'
                        columnClassList.add(java.lang.Character.class);
                        break;

                    case 4: // '\004'
                        columnClassList.add(java.lang.Double.class);
                        break;

                    case 5: // '\005'
                        columnClassList.add(java.lang.Float.class);
                        break;

                    case 6: // '\006'
                        columnClassList.add(java.lang.Integer.class);
                        break;

                    case 7: // '\007'
                        columnClassList.add(java.lang.Long.class);
                        break;

                    case 8: // '\b'
                        columnClassList.add(java.lang.Short.class);
                        break;

                    default:
                        columnClassList.add(java.lang.String.class);
                        break;
                    }
                }
                dosField = null;
            }
            fieldKey = null;
            fieldList.clear();
            fieldList = null;
            cNames = new String[columnNameList.size()];
            columnNameList.toArray(cNames);
            cTypes = new Class[columnClassList.size()];
            columnClassList.toArray(cTypes);
            columnNameList.clear();
            columnNameList = null;
            columnClassList.clear();
            columnClassList = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        catch(Exception e)
        {
            System.err.println(e);
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
        ArrayList row = (ArrayList)((TreeNodeObject)treeNode.getUserObject()).getLawData();
        if(row == null || row.size() == 0)
            return null;
        if(row.size() > column)
            return row.get(column + 1);
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

    public ArrayList getColumnOuidList()
    {
        return columnOuidList;
    }

    public String[] getColumnNameArray()
    {
        return cNames;
    }

    public Class[] getColumnTypeArray()
    {
        return cTypes;
    }

    public ArrayList getColumnFieldList()
    {
        return columnList;
    }

    protected String cNames[];
    protected Class cTypes[];
    public static final Integer ZERO = new Integer(0);
    private DefaultMutableTreeNode treeNode;
    private Object children[];
    private DOS dos;
    private NDS nds;
    public ArrayList valueList;
    public String nodeOuid;
    private ArrayList columnOuidList;
    private ArrayList columnWidthList;
    private ArrayList columnList;
    private int mode;

}
