// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TreeTableModelAdapter.java

package dyna.uic;

import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.uic:
//            TreeTableModel

public class TreeTableModelAdapter extends AbstractTableModel
{

    public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree)
    {
        this.tree = tree;
        this.treeTableModel = treeTableModel;
        tree.addTreeExpansionListener(new TreeExpansionListener() {

            public void treeExpanded(TreeExpansionEvent event)
            {
                fireTableDataChanged();
            }

            public void treeCollapsed(TreeExpansionEvent event)
            {
                fireTableDataChanged();
            }

        });
    }

    public int getColumnCount()
    {
        return treeTableModel.getColumnCount();
    }

    public String getColumnName(int column)
    {
        return treeTableModel.getColumnName(column);
    }

    public ArrayList getColumnWidth()
    {
        return treeTableModel.getColumnWidth();
    }

    public Class getColumnClass(int column)
    {
        return treeTableModel.getColumnClass(column);
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
        return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    public boolean isCellEditable(int row, int column)
    {
        return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    public void setValueAt(Object value, int row, int column)
    {
        treeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    JTree tree;
    TreeTableModel treeTableModel;
}
