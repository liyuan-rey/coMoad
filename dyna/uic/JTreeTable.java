// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JTreeTable.java

package dyna.uic;

import dyna.framework.client.*;
import dyna.framework.service.DOS;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;
import javax.swing.tree.*;

// Referenced classes of package dyna.uic:
//            TreeTableModelAdapter, TreeTableModel

public class JTreeTable extends JTable
{
    public class TreeTableCellRenderer extends JTree
        implements TableCellRenderer
    {

        public void updateUI()
        {
            updateUI();
            TreeCellRenderer tcr = getCellRenderer();
            if(tcr instanceof DefaultTreeCellRenderer)
            {
                DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
            }
        }

        public void setRowHeight(int rowHeight)
        {
            if(rowHeight > 0)
            {
                setRowHeight(rowHeight);
                if(JTreeTable.this != null && getRowHeight() != rowHeight)
                    JTreeTable.this.setRowHeight(getRowHeight());
            }
        }

        public void setBounds(int x, int y, int w, int h)
        {
            setBounds(x, 0, w, getHeight());
        }

        public void paint(Graphics g)
        {
            g.translate(0, -visibleRow * getRowHeight());
            paint(g);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            int pathCount = getPathForRow(row).getPathCount();
            if(isSelected)
            {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else
            {
                switch(pathCount)
                {
                case 1: // '\001'
                    setBackground(color1);
                    break;

                case 2: // '\002'
                    setBackground(color2);
                    break;

                case 3: // '\003'
                    setBackground(color3);
                    break;

                case 4: // '\004'
                    setBackground(color4);
                    break;

                default:
                    setBackground(Color.white);
                    break;
                }
                setForeground(table.getForeground());
            }
            visibleRow = row;
            return this;
        }

        protected int visibleRow;

        public TreeTableCellRenderer(TreeModel model)
        {
            JTree(model);
        }
    }

    public class TreeTableCellEditor extends AbstractCellEditor
        implements TableCellEditor
    {

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c)
        {
            return tree;
        }

        public boolean isCellEditable(EventObject e)
        {
            if(e instanceof MouseEvent)
            {
                for(int counter = getColumnCount() - 1; counter >= 0; counter--)
                {
                    if(getColumnClass(counter) != dyna.uic.TreeTableModel.class)
                        continue;
                    MouseEvent me = (MouseEvent)e;
                    MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
                    tree.dispatchEvent(newME);
                    break;
                }

            }
            return false;
        }

        public Object getCellEditorValue()
        {
            return null;
        }

        public TreeTableCellEditor()
        {
        }
    }

    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
    {
        class ListSelectionHandler
            implements ListSelectionListener
        {

            public void valueChanged(ListSelectionEvent e)
            {
                updateSelectedPathsFromSelectedRows();
            }

            ListSelectionHandler()
            {
            }
        }


        ListSelectionModel getListSelectionModel()
        {
            return listSelectionModel;
        }

        public void resetRowSelection()
        {
            if(updatingListSelectionModel)
                break MISSING_BLOCK_LABEL_36;
            updatingListSelectionModel = true;
            try
            {
                resetRowSelection();
            }
            finally
            {
                updatingListSelectionModel = false;
            }
        }

        protected ListSelectionListener createListSelectionListener()
        {
            return new ListSelectionHandler();
        }

        protected void updateSelectedPathsFromSelectedRows()
        {
            if(updatingListSelectionModel)
                break MISSING_BLOCK_LABEL_113;
            updatingListSelectionModel = true;
            try
            {
                int min = listSelectionModel.getMinSelectionIndex();
                int max = listSelectionModel.getMaxSelectionIndex();
                clearSelection();
                if(min != -1 && max != -1)
                {
                    for(int counter = min; counter <= max; counter++)
                        if(listSelectionModel.isSelectedIndex(counter))
                        {
                            TreePath selPath = tree.getPathForRow(counter);
                            if(selPath != null)
                                addSelectionPath(selPath);
                        }

                }
            }
            finally
            {
                updatingListSelectionModel = false;
            }
        }

        protected boolean updatingListSelectionModel;

        public ListToTreeSelectionModelWrapper()
        {
            getListSelectionModel().addListSelectionListener(createListSelectionListener());
        }
    }

    class InternalCellRenderer extends JLabel
        implements TableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            int pathCount = JTreeTable.this.tree.getPathForRow(row).getPathCount();
            setToolTip(value);
            if(isSelected)
            {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else
            {
                switch(pathCount)
                {
                case 1: // '\001'
                    setBackground(color1);
                    break;

                case 2: // '\002'
                    setBackground(color2);
                    break;

                case 3: // '\003'
                    setBackground(color3);
                    break;

                case 4: // '\004'
                    setBackground(color4);
                    break;

                default:
                    setBackground(Color.white);
                    break;
                }
                setForeground(table.getForeground());
            }
            setText("");
            setIcon(null);
            if(value instanceof String)
            {
                setHorizontalAlignment(2);
                setText((String)value);
            } else
            if(value instanceof Boolean)
            {
                setHorizontalAlignment(0);
                if(Utils.getBoolean((Boolean)value))
                    setIcon(trueIcon);
                else
                    setIcon(falseIcon);
            } else
            if((value instanceof Number) && value != null)
            {
                setHorizontalAlignment(4);
                setText(value.toString());
            }
            return this;
        }

        public void setToolTip(Object value)
        {
            setToolTipText(value != null ? value.toString() : "");
        }

        ImageIcon trueIcon;
        ImageIcon falseIcon;
        public TreeTableCellRenderer tree;

        public InternalCellRenderer()
        {
            trueIcon = new ImageIcon("icons/true16.gif");
            falseIcon = new ImageIcon("icons/false16.gif");
            tree = null;
            setOpaque(true);
            FontUIResource userFontRsc = newUI.getUserTextFont();
            setFont(new Font(userFontRsc.getName(), 0, userFontRsc.getSize()));
            tree = JTreeTable.this.tree;
        }
    }

    class InternalTreeRenderer extends DFDefaultTreeCellRenderer
        implements TreeCellRenderer
    {

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean isExpaneded, boolean isLeaf, int row, boolean hasFocus)
        {
            int pathCount = 0;
            getTreeCellRendererComponent(tree, value, isSelected, isExpaneded, isLeaf, row, hasFocus);
            setToolTip(value);
            if(!isSelected && row >= 0 && tree != null)
            {
                if(tree.getPathForRow(row) == null)
                {
                    setBackgroundNonSelectionColor(color1);
                    return this;
                }
                pathCount = tree.getPathForRow(row).getPathCount();
                switch(pathCount)
                {
                case 1: // '\001'
                    setBackgroundNonSelectionColor(color1);
                    break;

                case 2: // '\002'
                    setBackgroundNonSelectionColor(color2);
                    break;

                case 3: // '\003'
                    setBackgroundNonSelectionColor(color3);
                    break;

                case 4: // '\004'
                    setBackgroundNonSelectionColor(color4);
                    break;

                default:
                    setBackgroundNonSelectionColor(Color.white);
                    break;
                }
            }
            return this;
        }

        public void setToolTip(Object value)
        {
            setToolTipText(value != null ? value.toString() : "");
        }

        InternalTreeRenderer()
        {
        }
    }


    public JTreeTable(TreeTableModel treeTableModel)
    {
        internalTreeRenderer = null;
        internalCellRenderer = null;
        color1 = new Color(236, 223, 214);
        color2 = new Color(239, 233, 217);
        color3 = new Color(255, 255, 255);
        color4 = new Color(255, 255, 255);
        headerColor = new Color(217, 213, 214);
        gridColor = new Color(239, 233, 217);
        shadowBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray);
        extraPainter = null;
        header = null;
        dos = DynaMOAD.dos;
        this.treeTableModel = treeTableModel;
        newUI = DynaMOAD.newUI;
        if(newUI == null)
            newUI = new UIManagement();
        color1 = UIManagement.treeLevelOneColor;
        color2 = UIManagement.treeLevelTwoColor;
        color3 = UIManagement.treeLevelThreeColor;
        color4 = UIManagement.treeLevelFourColor;
        headerColor = UIManagement.tableheaderColor;
        gridColor = UIManagement.tableGridColor;
        tree = new TreeTableCellRenderer(treeTableModel);
        if(internalTreeRenderer == null)
            internalTreeRenderer = new InternalTreeRenderer();
        tree.setCellRenderer(internalTreeRenderer);
        setModel(new TreeTableModelAdapter(treeTableModel, tree));
        ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
        tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());
        tree.setRowHeight(getRowHeight());
        setDefaultRenderer(dyna.uic.TreeTableModel.class, tree);
        setDefaultEditor(dyna.uic.TreeTableModel.class, new TreeTableCellEditor());
        setAutoResizeMode(0);
        setShowHorizontalLines(false);
        setIntercellSpacing(new Dimension(1, 0));
        setGridColor(gridColor);
        changeColumnWidth(treeTableModel.getColumnCount(), treeTableModel.getColumnWidth());
        getTableHeader().setBackground(headerColor);
        if(internalCellRenderer == null)
            internalCellRenderer = new InternalCellRenderer();
        setDefaultRenderer(java.lang.String.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Boolean.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Integer.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Long.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Short.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Float.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Double.class, internalCellRenderer);
        setDefaultRenderer(java.lang.Byte.class, internalCellRenderer);
        setDefaultRenderer(java.math.BigInteger.class, internalCellRenderer);
        setDefaultRenderer(java.math.BigDecimal.class, internalCellRenderer);
        if(tree.getRowHeight() < 21)
            setRowHeight(21);
        else
            setRowHeight(getRowHeight());
        header = getTableHeader();
        header.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                TableColumnModel tcm = getColumnModel();
                int columnWidth = 0;
                int cap = header.columnAtPoint(new Point(e.getX(), e.getY()));
                if(header.columnAtPoint(new Point(e.getX() - 3, e.getY())) == cap - 1 && header.columnAtPoint(new Point(e.getX() + 2, e.getY())) == cap && e.getClickCount() == 2)
                {
                    int realViewIndex = convertColumnIndexToModel(cap - 1);
                    columnWidth = setWidthOfSelectedColumn(realViewIndex, getModel());
                    tcm.getColumn(cap - 1).setPreferredWidth(columnWidth);
                } else
                if(header.columnAtPoint(new Point(e.getX() - 2, e.getY())) == cap && header.columnAtPoint(new Point(e.getX() + 3, e.getY())) == cap + 1 && e.getClickCount() == 2)
                {
                    int realViewIndex = convertColumnIndexToModel(cap);
                    columnWidth = setWidthOfSelectedColumn(realViewIndex, getModel());
                    tcm.getColumn(cap).setPreferredWidth(columnWidth);
                } else
                if(header.columnAtPoint(new Point(e.getX() - 3, e.getY())) == cap && header.columnAtPoint(new Point(e.getX() + 3, e.getY())) == -1 && e.getClickCount() == 2)
                {
                    int realViewIndex = convertColumnIndexToModel(cap);
                    columnWidth = setWidthOfSelectedColumn(realViewIndex, getModel());
                    tcm.getColumn(cap).setPreferredWidth(columnWidth);
                } else
                if(header.columnAtPoint(new Point(e.getX() - 3, e.getY())) == cap && header.columnAtPoint(new Point(e.getX() + 3, e.getY())) == cap)
                    e.getClickCount();
            }

        });
    }

    public int setWidthOfSelectedColumn(int column, TableModel dataModel)
    {
        int length = 0;
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        for(int i = 0; i < dataModel.getRowCount(); i++)
            try
            {
                dataModel.getValueAt(i, column);
                if(dataModel.getValueAt(i, column) != null && dataModel.getValueAt(i, column).getClass().equals(Class.forName("java.lang.String")) && length < fm.stringWidth((String)dataModel.getValueAt(i, column)))
                    length = fm.stringWidth((String)dataModel.getValueAt(i, column));
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

    public void changeColumnWidth(int columnCount, ArrayList columnWidth)
    {
        TableColumn column = null;
        for(int i = 0; i < columnCount; i++)
        {
            column = getColumnModel().getColumn(i);
            int wid = Utils.getInt((Integer)columnWidth.get(i));
            column.setPreferredWidth(wid);
        }

    }

    public void updateUI()
    {
        updateUI();
        if(tree != null)
            tree.updateUI();
    }

    public int getEditingRow()
    {
        return getColumnClass(editingColumn) != dyna.uic.TreeTableModel.class ? editingRow : -1;
    }

    public void setRowHeight(int rowHeight)
    {
        setRowHeight(rowHeight);
        if(tree != null && tree.getRowHeight() != rowHeight)
            tree.setRowHeight(getRowHeight());
    }

    public TreeTableModel getTreeTableModel()
    {
        return treeTableModel;
    }

    public void addTreeSelectionListener(TreeSelectionListener tsl)
    {
        tree.addTreeSelectionListener(tsl);
    }

    public void removeTreeSelectionListener(TreeSelectionListener tsl)
    {
        tree.removeTreeSelectionListener(tsl);
    }

    public void setExtraPainter(JComponent comp)
    {
        extraPainter = comp;
    }

    public void paint(Graphics g)
    {
        paint(g);
        if(extraPainter != null)
            extraPainter.paint(g);
    }

    public void reapint(long time, int x, int y, int w, int h)
    {
        repaint(time, x, y, w, h);
        if(extraPainter != null)
            extraPainter.repaint(time, x, y, w, h);
    }

    private DOS dos;
    public TreeTableCellRenderer tree;
    private TreeTableModel treeTableModel;
    private UIManagement newUI;
    private InternalTreeRenderer internalTreeRenderer;
    private InternalCellRenderer internalCellRenderer;
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    private Color headerColor;
    private Color gridColor;
    private Border shadowBorder;
    private JComponent extraPainter;
    private JTableHeader header;






}
