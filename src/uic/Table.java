// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Table.java

package dyna.uic;

import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

// Referenced classes of package dyna.uic:
//            DynaTable, TableSortDecorator

public class Table
    implements ListSelectionListener, ComponentListener
{
    class TableModel extends AbstractTableModel
    {

        public void setUneditableColumns(int col[])
        {
            uneditableColumns = (int[])col.clone();
        }

        public boolean isCellEditable(int row, int col)
        {
            if(isCADWindows)
            {
                if(!Utils.getBoolean((Boolean)((ArrayList)data.get(row)).get(0)))
                    return false;
                if(col < 5 || col == 9 || col == 11 || col == 12 || col == 7 || col == 8)
                    return false;
                else
                    return isEditable;
            }
            if(uneditableColumns == null || uneditableColumns.length == 0)
                return isEditable;
            for(int i = 0; i < uneditableColumns.length; i++)
                if(uneditableColumns[i] == col)
                    return false;

            return isEditable;
        }

        public int getColumnCount()
        {
            return columnNames.size();
        }

        public int getRowCount()
        {
            if(Utils.isNullArrayList(data))
                return 0;
            else
                return data.size();
        }

        public String getColumnName(int col)
        {
            return (String)columnNames.get(col);
        }

        public Object getValueAt(int row, int col)
        {
            int index = 0;
            if((index = columnSequence[col]) == -1)
                return null;
            Object obj = ((ArrayList)data.get(row)).get(index);
            if(obj instanceof ImageIcon)
            {
                ImageIcon image = (ImageIcon)((ArrayList)data.get(row)).get(index);
                return image;
            }
            if(obj == null)
                obj = new String("");
            return obj;
        }

        public void setValueAt(Object aValue, int row, int col)
        {
            int index = 0;
            if((index = columnSequence[col]) == -1)
            {
                return;
            } else
            {
                ((ArrayList)data.get(row)).set(index, aValue);
                return;
            }
        }

        public void setColumnSequence(int a[])
        {
            columnSequence = (int[])a.clone();
            a.getClass();
            getColumnCount();
        }

        public Class getColumnClass(int c)
        {
            return getValueAt(0, c).getClass();
        }

        public void changeValue(String s, ArrayList vt)
        {
            int sr = selectRow(s);
            if(sr > -1)
            {
                for(int i = 0; i < ((ArrayList)data.get(sr)).size(); i++)
                    ((ArrayList)data.get(sr)).set(i, vt.get(i));

                changeData();
            }
        }

        public void changeValue(Number nu, Boolean t, int k)
        {
            int sr = selectRowNum(nu, k);
            if(sr > -1)
                ((ArrayList)data.get(sr)).set(1, t);
            changeData();
        }

        public void changeValue(Integer nu, int k, int row)
        {
            ((ArrayList)data.get(row)).set(k, nu);
            changeData();
        }

        public void changeValue(String s, String t, int k)
        {
            int sr = selectRow(s);
            if(sr > -1)
                ((ArrayList)data.get(sr)).set(k, t);
            changeData();
        }

        public void removeValue(String s)
        {
            int sr = selectRow(s);
            if(sr > -1)
            {
                data.remove(sr);
                changeData();
            }
        }

        public void removeValue(String s, int i)
        {
            int sr = selectRow(s, i);
            if(sr > -1)
            {
                data.remove(sr);
                changeData();
            }
        }

        public void removeValue(Number j, int i)
        {
            int sr = selectRowNum(j, i);
            if(sr > -1)
            {
                data.remove(sr);
                changeData();
            }
        }

        public int selectRow(String s, int i)
        {
            int select = -1;
            for(int j = 0; j < data.size(); j++)
                if(((ArrayList)data.get(j)).get(i).equals(s))
                {
                    select = j;
                    return select;
                }

            return select;
        }

        public int selectRowNum(Number j, int i)
        {
            int selec = -1;
            for(int k = 0; k < data.size(); k++)
                if((Number)((ArrayList)data.get(k)).get(i) == j)
                {
                    selec = k;
                    return selec;
                }

            return selec;
        }

        public int selectRow(String s)
        {
            int select = -1;
            for(int i = 0; i < data.size(); i++)
                if(((ArrayList)data.get(i)).get(0).equals(s))
                {
                    select = i;
                    return select;
                }

            return select;
        }

        public void changeData()
        {
            fireTableDataChanged();
        }

        public void setEditable(boolean bl)
        {
            isEditable = bl;
        }

        public ArrayList data;
        public ArrayList columnNames;
        private int columnSequence[];
        private int uneditableColumns[];


        TableModel()
        {
            columnSequence = null;
            uneditableColumns = null;
        }
    }


    public Table()
    {
        isEditable = false;
        isCADWindows = false;
        decorator = null;
        header = null;
        selRow = -1;
        sortable = true;
        try
        {
            initialize();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void initialize()
        throws Exception
    {
        selRows = -1;
        data = new ArrayList();
        selectMode = MULTI_SELECT;
        selectedIndex = new String();
        selectedRow = new ArrayList();
        selectedIndices = new ArrayList();
        selectedRows = new ArrayList();
        indexColumn = -1;
        ALLOW_COLUMN_SELECTION = false;
        ALLOW_ROW_SELECTION = true;
        totalColumnSize = 0;
        currentWidth = 0;
        isAdjusted = false;
        listSelectModel = null;
        tableModel = new TableModel();
        table = new DynaTable();
        table.setAutoResizeMode(0);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.addComponentListener(this);
    }

    public Table(ArrayList dataList, ArrayList columnNameList, ArrayList columnWidth, int selection, int tableWidth, boolean sortable)
    {
        this();
        int columnSize[] = new int[columnWidth.size()];
        for(int i = 0; i < columnWidth.size(); i++)
        {
            columnSize[i] = Utils.getInt((Integer)columnWidth.get(i));
            totalColumnSize += columnSize[i];
        }

        if(totalColumnSize < tableWidth)
        {
            for(int i = 0; i < columnWidth.size(); i++)
            {
                columnSize[i] = (int)((float)tableWidth * ((float)columnSize[i] / (float)totalColumnSize));
                columnWidth.set(i, new Integer(columnSize[i]));
            }

        }
        selectMode = selection;
        tableModel.data = dataList;
        tableModel.columnNames = columnNameList;
        table.setModel(tableModel);
        int columnNameSize = columnNameList.size();
        listSelectModel = table.getSelectionModel();
        setSelectMode(selectMode);
        listSelectModel.addListSelectionListener(this);
        this.sortable = sortable;
        if(sortable)
        {
            decorator = new TableSortDecorator(table.getModel());
            table.setModel(decorator);
        }
        header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                TableColumnModel tcm = table.getColumnModel();
                int vc = tcm.getColumnIndexAtX(e.getX());
                int mc = table.convertColumnIndexToModel(vc);
                int columnWidth = 0;
                int cap = header.columnAtPoint(new Point(e.getX(), e.getY()));
                if(header.columnAtPoint(new Point(e.getX() - 3, e.getY())) == cap - 1 && header.columnAtPoint(new Point(e.getX() + 2, e.getY())) == cap && e.getClickCount() == 2)
                {
                    int realViewIndex = getTable().convertColumnIndexToModel(cap - 1);
                    columnWidth = setWidthOfSelectedColumn(tableModel.columnSequence[realViewIndex], tableModel.data);
                    tcm.getColumn(cap - 1).setPreferredWidth(columnWidth);
                } else
                if(header.columnAtPoint(new Point(e.getX() - 2, e.getY())) == cap && header.columnAtPoint(new Point(e.getX() + 3, e.getY())) == cap + 1 && e.getClickCount() == 2)
                {
                    int realViewIndex = getTable().convertColumnIndexToModel(cap);
                    columnWidth = setWidthOfSelectedColumn(tableModel.columnSequence[realViewIndex], tableModel.data);
                    tcm.getColumn(cap).setPreferredWidth(columnWidth);
                } else
                if(header.columnAtPoint(new Point(e.getX() - 3, e.getY())) == cap && header.columnAtPoint(new Point(e.getX() + 3, e.getY())) == -1 && e.getClickCount() == 2)
                {
                    int realViewIndex = getTable().convertColumnIndexToModel(cap);
                    columnWidth = setWidthOfSelectedColumn(tableModel.columnSequence[realViewIndex], tableModel.data);
                    tcm.getColumn(cap).setPreferredWidth(columnWidth);
                } else
                if(sortable && header.columnAtPoint(new Point(e.getX() - 3, e.getY())) == cap && header.columnAtPoint(new Point(e.getX() + 3, e.getY())) == cap && e.getClickCount() == 1)
                {
                    if(selRow < 0)
                        selRow = table.getSelectedRow();
                    int newRow = -1;
                    decorator.sort(mc);
                    decorator.setSortingOrder(mc);
                    DOSChangeable dosOrder = decorator.ouidOrder;
                    for(int j = 0; j < tableModel.data.size(); j++)
                    {
                        if(selRow != (new Integer((String)dosOrder.get((new Integer(j)).toString()))).intValue())
                            continue;
                        newRow = j;
                        break;
                    }

                    if(newRow > -1)
                        table.setRowSelectionInterval(newRow, newRow);
                }
            }

        });
        changeColumnWidth(columnNameSize, columnWidth);
        table.repaint();
    }

    public Table(ArrayList dataList, ArrayList columnNameList, ArrayList columnWidth, int selection, int tableWidth)
    {
        this(dataList, columnNameList, columnWidth, selection, tableWidth, true);
    }

    public Table(ArrayList datalist, ArrayList columnNamelist, ArrayList columnWidth, int Selection)
    {
        this(datalist, columnNamelist, columnWidth, Selection, -1, true);
    }

    public int setWidthOfSelectedColumn(int column, ArrayList dataList)
    {
        int length = 0;
        Graphics g = getTable().getGraphics();
        FontMetrics fm = g.getFontMetrics();
        for(int i = 0; i < dataList.size(); i++)
            try
            {
                if(((ArrayList)dataList.get(i)).get(column) != null && ((ArrayList)dataList.get(i)).get(column).getClass().equals(Class.forName("java.lang.String")) && length < fm.stringWidth((String)((ArrayList)dataList.get(i)).get(column)))
                    length = fm.stringWidth((String)((ArrayList)dataList.get(i)).get(column));
            }
            catch(Exception e)
            {
                System.err.println(e);
            }

        if(length == 0)
            length = 10;
        else
            length += 5;
        fm = null;
        g = null;
        return length;
    }

    public void setSelectMode(int selection)
    {
        switch(selection)
        {
        case 0: // '\0'
            listSelectModel.setSelectionMode(0);
            break;

        case 1: // '\001'
            listSelectModel.setSelectionMode(2);
            break;
        }
    }

    public JTable getTable()
    {
        return table;
    }

    public ArrayList getList()
    {
        if(tableModel == null)
            return null;
        else
            return tableModel.data;
    }

    public void setValueAt(Object newValue, int row, int col)
    {
        tableModel.setValueAt(newValue, row, col);
    }

    public void setIndexColumn(int column)
    {
        indexColumn = column;
    }

    public void setEditable(boolean bl)
    {
        isEditable = bl;
    }

    public void setCADWindows(boolean b)
    {
        isCADWindows = b;
    }

    public boolean getCADWindows()
    {
        return isCADWindows;
    }

    public void changeTableData()
    {
        tableModel.changeData();
    }

    public void changeTableData(String indexValue, ArrayList newData)
    {
        tableModel.changeValue(indexValue, newData);
    }

    public void changeTableData(String indexValue, String newValue, int column)
    {
        tableModel.changeValue(indexValue, newValue, column);
    }

    public void changeTableData(Number indexNumber, Boolean newValue, int column)
    {
        tableModel.changeValue(indexNumber, newValue, column);
    }

    public void changeTableData(Integer newValue, int row, int column)
    {
        tableModel.changeValue(newValue, row, column);
    }

    public void removeTableRow(String indexValue)
    {
        tableModel.removeValue(indexValue);
    }

    public void removeTableRow(String indexValue, int column)
    {
        tableModel.removeValue(indexValue, column);
    }

    public void removeTableRow(Number indexValue, int column)
    {
        tableModel.removeValue(indexValue, column);
    }

    public void changeColumnWidth(int columnCount, ArrayList columnWidth)
    {
        TableColumn column = null;
        for(int i = 0; i < columnCount; i++)
        {
            column = table.getColumnModel().getColumn(i);
            int wid = Utils.getInt((Integer)columnWidth.get(i));
            column.setPreferredWidth(wid);
        }

    }

    public void valueChanged(ListSelectionEvent e)
    {
        if(!e.getValueIsAdjusting())
            switch(selectMode)
            {
            default:
                break;

            case 0: // '\0'
                this.selRows = table.getSelectedRow();
                if(this.selRows > -1)
                    try
                    {
                        selectedIndex = (String)((ArrayList)tableModel.data.get(this.selRows)).get(indexColumn);
                        selectedRow.clear();
                        for(int t = 0; t < ((ArrayList)tableModel.data.get(this.selRows)).size(); t++)
                            selectedRow.add(((ArrayList)tableModel.data.get(this.selRows)).get(t));

                    }
                    catch(Exception exception) { }
                break;

            case 1: // '\001'
                int selRows[] = table.getSelectedRows();
                if(listSelectModel.isSelectionEmpty())
                    break;
                selectedIndices.clear();
                selectedRows.clear();
                for(int i = 0; i < selRows.length; i++)
                    if(selRows[i] >= 0 && selRows[i] <= table.getRowCount() - 1)
                    {
                        selectedIndices.add(((ArrayList)tableModel.data.get(selRows[i])).get(indexColumn));
                        selectedRows.add((ArrayList)tableModel.data.get(selRows[i]));
                    }

                break;
            }
    }

    public ArrayList getSelectedIndices()
    {
        return selectedIndices;
    }

    public String getSelectedIndex()
    {
        return selectedIndex;
    }

    public ArrayList getSelectedRow()
    {
        return selectedRow;
    }

    public ArrayList getSelectedRows()
    {
        return selectedRows;
    }

    public ArrayList getColumn(int column)
    {
        ArrayList tmp = new ArrayList();
        int rowCount = table.getRowCount();
        for(int i = 0; i < rowCount; i++)
            tmp.add(table.getValueAt(i, column));

        return tmp;
    }

    public void setData(ArrayList datalist)
    {
        if(tableModel == null)
            return;
        tableModel.data = datalist;
        if(datalist == null)
        {
            return;
        } else
        {
            tableModel.fireTableRowsInserted(0, datalist.size() - 1);
            return;
        }
    }

    public int getSelectedRowNumber()
    {
        selRows = table.getSelectedRow();
        return selRows;
    }

    public void setColumnSequence(int seq[])
    {
        tableModel.setColumnSequence(seq);
    }

    public void setUneditableColumns(int col[])
    {
        tableModel.setUneditableColumns(col);
    }

    public String getSelectedOuidRow(int row)
    {
        DOSChangeable ouidOrder = setOuidOrder();
        String ouidRow = (String)ouidOrder.get((new Integer(row)).toString());
        return ouidRow;
    }

    public ArrayList getSelectedOuidRows(int rows[])
    {
        ArrayList selectedOuidList = new ArrayList();
        for(int i = 0; i < rows.length; i++)
        {
            DOSChangeable ouidOrder = setOuidOrder();
            String ouidRow = (String)ouidOrder.get((new Integer(rows[i])).toString());
            selectedOuidList.add(ouidRow);
        }

        return selectedOuidList;
    }

    private void fitSize()
    {
        if(isAdjusted)
            return;
        isAdjusted = true;
        currentWidth = table.getWidth();
        if(currentWidth < 1)
            return;
        if(currentWidth - 15 > totalColumnSize)
            table.setAutoResizeMode(4);
        else
            table.setAutoResizeMode(0);
    }

    public void componentResized(ComponentEvent e)
    {
        fitSize();
    }

    public void componentHidden(ComponentEvent componentevent)
    {
    }

    public void componentMoved(ComponentEvent componentevent)
    {
    }

    public void componentShown(ComponentEvent componentevent)
    {
    }

    public DOSChangeable setOuidOrder()
    {
        return decorator.ouidOrder;
    }

    public void setOrderForRowSelection(int row)
    {
        selRow = row;
    }

    public static int SINGLE_SELECT = 0;
    public static int MULTI_SELECT = -1;
    public boolean isEditable;
    public boolean isCADWindows;
    protected int selRows;
    protected ArrayList data;
    protected int selectMode;
    protected String selectedIndex;
    protected ArrayList selectedRow;
    protected ArrayList selectedIndices;
    protected ArrayList selectedRows;
    protected int indexColumn;
    protected JTable table;
    protected boolean ALLOW_COLUMN_SELECTION;
    protected boolean ALLOW_ROW_SELECTION;
    protected int totalColumnSize;
    protected int currentWidth;
    protected boolean isAdjusted;
    protected ListSelectionModel listSelectModel;
    protected TableModel tableModel;
    private TableSortDecorator decorator;
    private JTableHeader header;
    private int selRow;
    private boolean sortable;






}
