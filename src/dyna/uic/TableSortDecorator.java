// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TableSortDecorator.java

package dyna.uic;

import dyna.framework.service.dos.DOSChangeable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TableSortDecorator
    implements TableModel, TableModelListener
{
    private class StackItem
    {

        public int left;
        public int right;

        StackItem()
        {
        }
    }


    public TableSortDecorator(TableModel model)
    {
        ouidOrder = new DOSChangeable();
        tmpDOS = new DOSChangeable();
        isDescend = true;
        err1 = new IllegalArgumentException("stack overflow in QuickSort");
        realModel = model;
        realModel.addTableModelListener(this);
        allocate();
    }

    public void addTableModelListener(TableModelListener l)
    {
        realModel.addTableModelListener(l);
    }

    public Class getColumnClass(int columnIndex)
    {
        return realModel.getColumnClass(columnIndex);
    }

    public int getColumnCount()
    {
        return realModel.getColumnCount();
    }

    public String getColumnName(int columnIndex)
    {
        return realModel.getColumnName(columnIndex);
    }

    public int getRowCount()
    {
        return realModel.getRowCount();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return realModel.isCellEditable(rowIndex, columnIndex);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        realModel.removeTableModelListener(l);
    }

    public Object getValueAt(int row, int column)
    {
        return getRealModel().getValueAt(indexes[row], column);
    }

    public void setValueAt(Object aValue, int row, int column)
    {
        getRealModel().setValueAt(aValue, indexes[row], column);
    }

    protected TableModel getRealModel()
    {
        return realModel;
    }

    public void tableChanged(TableModelEvent e)
    {
        allocate();
    }

    public void sort(int column)
    {
        if(tmpDOS.get("column") == null)
            tmpDOS.put("column", (new Integer(column)).toString());
        else
        if(((String)tmpDOS.get("column")).equals((new Integer(column)).toString()) && isDescend)
            isDescend = false;
        else
        if(((String)tmpDOS.get("column")).equals((new Integer(column)).toString()) && !isDescend)
            isDescend = true;
        else
            isDescend = true;
        int rowCount = getRowCount();
        int stackSize = 32;
        StackItem stack[] = new StackItem[32];
        for(int n = 0; n < 32; n++)
            stack[n] = new StackItem();

        int stackPtr = 0;
        int Threshold = 7;
        int l = 0;
        int r = indexes.length - 1;
        do
        {
            while(r > l) 
            {
                int scanl;
                int scanr;
                int pivot;
                if(r - l > 7)
                {
                    int mid = (l + r) / 2;
                    if(isDescend && compare(indexes[mid], indexes[l], column) < 0 || !isDescend && compare(indexes[mid], indexes[l], column) > 0)
                        swap(mid, l);
                    if(isDescend && compare(indexes[r], indexes[l], column) < 0 || !isDescend && compare(indexes[r], indexes[l], column) > 0)
                        swap(r, l);
                    if(isDescend && compare(indexes[r], indexes[mid], column) < 0 || !isDescend && compare(indexes[r], indexes[mid], column) > 0)
                        swap(mid, r);
                    pivot = r - 1;
                    swap(mid, pivot);
                    scanl = l + 1;
                    scanr = r - 2;
                } else
                {
                    pivot = r;
                    scanl = l;
                    scanr = r - 1;
                }
                do
                {
                    while((isDescend && compare(indexes[scanl], indexes[pivot], column) < 0 || !isDescend && compare(indexes[scanl], indexes[pivot], column) > 0) && scanl < r) 
                        scanl++;
                    for(; (isDescend && compare(indexes[pivot], indexes[scanr], column) < 0 || !isDescend && compare(indexes[pivot], indexes[scanr], column) > 0) && scanr > l; scanr--);
                    if(scanl >= scanr)
                        break;
                    swap(scanl, scanr);
                    if(scanl < r)
                        scanl++;
                    if(scanr > l)
                        scanr--;
                } while(true);
                swap(scanl, pivot);
                int lsize = scanl - l;
                int rsize = r - scanl;
                if(lsize > rsize)
                {
                    if(lsize != 1)
                    {
                        if(++stackPtr == 32)
                            throw err1;
                        stack[stackPtr].left = l;
                        stack[stackPtr].right = scanl - 1;
                    }
                    if(rsize == 0)
                        break;
                    l = scanl + 1;
                } else
                {
                    if(rsize != 1)
                    {
                        if(++stackPtr == 32)
                            throw err1;
                        stack[stackPtr].left = scanl + 1;
                        stack[stackPtr].right = r;
                    }
                    if(lsize == 0)
                        break;
                    r = scanl - 1;
                }
            }
            if(stackPtr != 0)
            {
                l = stack[stackPtr].left;
                r = stack[stackPtr].right;
                stackPtr--;
            } else
            {
                return;
            }
        } while(true);
    }

    private void swap(int i, int j)
    {
        int tmp = indexes[i];
        ouidOrder.put((new Integer(i)).toString(), (new Integer(indexes[j])).toString());
        ouidOrder.put((new Integer(j)).toString(), (new Integer(tmp)).toString());
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    private int compare(int i, int j, int column)
    {
        TableModel realModel = getRealModel();
        Object io = realModel.getValueAt(i, column);
        Object jo = realModel.getValueAt(j, column);
        int c = jo.toString().compareTo(io.toString());
        return c >= 0 ? ((int) (c <= 0 ? 0 : 1)) : -1;
    }

    private void allocate()
    {
        indexes = new int[getRowCount()];
        for(int i = 0; i < indexes.length; i++)
        {
            indexes[i] = i;
            ouidOrder.put((new Integer(i)).toString(), (new Integer(i)).toString());
        }

    }

    public void setSortingOrder(int mc)
    {
        tmpDOS.put("column", (new Integer(mc)).toString());
    }

    private TableModel realModel;
    private int indexes[];
    public DOSChangeable ouidOrder;
    private DOSChangeable tmpDOS;
    private boolean isDescend;
    private IllegalArgumentException err1;
}
