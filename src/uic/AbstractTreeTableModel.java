// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractTreeTableModel.java

package dyna.uic;

import javax.swing.event.*;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.uic:
//            TreeTableModel

public abstract class AbstractTreeTableModel
    implements TreeTableModel
{

    public AbstractTreeTableModel(Object root)
    {
        listenerList = new EventListenerList();
        this.root = root;
    }

    public Object getRoot()
    {
        return root;
    }

    public boolean isLeaf(Object node)
    {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath treepath, Object obj)
    {
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        for(int i = 0; i < getChildCount(parent); i++)
            if(getChild(parent, i).equals(child))
                return i;

        return -1;
    }

    public void addTreeModelListener(TreeModelListener l)
    {
        listenerList.add(javax.swing.event.TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
        listenerList.remove(javax.swing.event.TreeModelListener.class, l);
    }

    protected void fireTreeNodesChanged(Object source, Object path[], int childIndices[], Object children[])
    {
        Object listeners[] = listenerList.getListenerList();
        TreeModelEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
            if(listeners[i] == javax.swing.event.TreeModelListener.class)
            {
                if(e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener)listeners[i + 1]).treeNodesChanged(e);
            }

    }

    protected void fireTreeNodesInserted(Object source, Object path[], int childIndices[], Object children[])
    {
        Object listeners[] = listenerList.getListenerList();
        TreeModelEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
            if(listeners[i] == javax.swing.event.TreeModelListener.class)
            {
                if(e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener)listeners[i + 1]).treeNodesInserted(e);
            }

    }

    protected void fireTreeNodesRemoved(Object source, Object path[], int childIndices[], Object children[])
    {
        Object listeners[] = listenerList.getListenerList();
        TreeModelEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
            if(listeners[i] == javax.swing.event.TreeModelListener.class)
            {
                if(e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener)listeners[i + 1]).treeNodesRemoved(e);
            }

    }

    protected void fireTreeStructureChanged(Object source, Object path[], int childIndices[], Object children[])
    {
        Object listeners[] = listenerList.getListenerList();
        TreeModelEvent e = null;
        for(int i = listeners.length - 2; i >= 0; i -= 2)
            if(listeners[i] == javax.swing.event.TreeModelListener.class)
            {
                if(e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener)listeners[i + 1]).treeStructureChanged(e);
            }

    }

    public Class getColumnClass(int column)
    {
        return java.lang.Object.class;
    }

    public boolean isCellEditable(Object node, int column)
    {
        return getColumnClass(column) == dyna.uic.TreeTableModel.class;
    }

    public void setValueAt(Object obj, Object obj1, int i)
    {
    }

    protected Object root;
    protected EventListenerList listenerList;
}
