// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TreeTableModel.java

package dyna.uic;

import java.util.ArrayList;
import javax.swing.tree.TreeModel;

public interface TreeTableModel
    extends TreeModel
{

    public abstract int getColumnCount();

    public abstract String getColumnName(int i);

    public abstract ArrayList getColumnWidth();

    public abstract Class getColumnClass(int i);

    public abstract Object getValueAt(Object obj, int i);

    public abstract boolean isCellEditable(Object obj, int i);

    public abstract void setValueAt(Object obj, Object obj1, int i);
}
