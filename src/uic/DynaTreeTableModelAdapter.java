// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaTreeTableModelAdapter.java

package dyna.uic;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.uic:
//            DynaTreeTableModel

public class DynaTreeTableModelAdapter extends AbstractTableModel
{

    public DynaTreeTableModelAdapter(DynaTreeTableModel dynaTreeTableModel, JTree tree)
    {
        this.tree = null;
        this.dynaTreeTableModel = null;
        treeExpansionListener = null;
        treeModelListener = null;
        this.tree = tree;
        this.dynaTreeTableModel = dynaTreeTableModel;
        treeExpansionListener = new TreeExpansionListener() {

            public void treeExpanded(TreeExpansionEvent event)
            {
                fireTableDataChanged();
            }

            public void treeCollapsed(TreeExpansionEvent event)
            {
                fireTableDataChanged();
            }

        };
        tree.addTreeExpansionListener(treeExpansionListener);
        treeModelListener = new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e)
            {
                delayedFireTableDataChanged();
            }

            public void treeNodesInserted(TreeModelEvent e)
            {
                delayedFireTableDataChanged();
            }

            public void treeNodesRemoved(TreeModelEvent e)
            {
                delayedFireTableDataChanged();
            }

            public void treeStructureChanged(TreeModelEvent e)
            {
                delayedFireTableDataChanged();
            }

        };
        dynaTreeTableModel.addTreeModelListener(treeModelListener);
    }

    public int getColumnCount()
    {
        return dynaTreeTableModel.getColumnCount();
    }

    public String getColumnName(int column)
    {
        return dynaTreeTableModel.getColumnName(column);
    }

    public Class getColumnClass(int column)
    {
        return dynaTreeTableModel.getColumnClass(column);
    }

    public int getRowCount()
    {
        return tree.getRowCount();
    }

    protected Object nodeForRow(int row)
    {
        TreePath treePath = tree.getPathForRow(row);
        return treePath.getLastPathComponent();
    }

    public Object getValueAt(int row, int column)
    {
        return dynaTreeTableModel.getValueAt(nodeForRow(row), column);
    }

    public boolean isCellEditable(int row, int column)
    {
        return dynaTreeTableModel.isCellEditable(nodeForRow(row), column);
    }

    public void setValueAt(Object value, int row, int column)
    {
        dynaTreeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    protected void delayedFireTableDataChanged()
    {
        SwingUtilities.invokeLater(new Runnable() {

            public void run()
            {
                fireTableDataChanged();
            }

        });
    }

    JTree tree;
    DynaTreeTableModel dynaTreeTableModel;
    public TreeExpansionListener treeExpansionListener;
    public TreeModelListener treeModelListener;
}
