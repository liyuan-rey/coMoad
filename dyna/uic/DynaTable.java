// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaTable.java

package dyna.uic;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import dyna.uic.table.DynaColumn;
import dyna.uic.table.DynaTableCellColor;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

// Referenced classes of package dyna.uic:
//            DynaComponent, DynaTheme

public class DynaTable extends JTable
    implements DynaComponent, MouseListener, AncestorListener
{
    private class DefaultCellColor
        implements DynaTableCellColor
    {

        public Color getBackground(DynaTable table, int row, int column)
        {
            if(row % 2 == 0)
                return table.getBackground();
            else
                return evenRowColor;
        }

        public Color getForeground(DynaTable table, int row, int column)
        {
            return table.getForeground();
        }

        DefaultCellColor()
        {
        }
    }

    private class CellRenderer extends DefaultTableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            setToolTip(value);
            if(isSelected)
            {
                setBackground(getSelectionBackground());
                setForeground(getSelectionForeground());
            } else
            if(!isSelected && cellColor != null && customCellColorEnabled)
            {
                setBackground(cellColor.getBackground((DynaTable)table, row, column));
                setForeground(cellColor.getForeground((DynaTable)table, row, column));
            } else
            {
                setBackground(getBackground());
            }
            setText("");
            setIcon(null);
            if(columns != null && value != null && row > 0 && columns.size() > column && ((DynaColumn)columns.get(column)).isValueGrouped() && getValueAt(row - 1, column) != null && getValueAt(row - 1, column).equals(value))
            {
                setText("");
                setBackground(new Color(210, 210, 210));
            } else
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
            if(value instanceof Number)
            {
                if(value != null)
                {
                    setHorizontalAlignment(4);
                    setText(value.toString());
                }
            } else
            if(!(value instanceof Date) && (value instanceof Color))
            {
                setBackground((Color)value);
                setText("R=" + ((Color)value).getRed() + "," + "G=" + ((Color)value).getGreen() + "," + "B=" + ((Color)value).getBlue());
            }
            return this;
        }

        public void setToolTip(Object value)
        {
            setToolTipText(value != null && !value.equals("") ? value.toString() : null);
        }

        ImageIcon trueIcon;
        ImageIcon falseIcon;

        public CellRenderer()
        {
            trueIcon = new ImageIcon("icons/True16.gif");
            falseIcon = new ImageIcon("icons/False16.gif");
            setFont(font);
        }
    }

    private class HeaderRenderer extends DefaultTableCellRenderer
        implements TableColumnModelListener
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setOpaque(false);
            setHorizontalAlignment(0);
            return this;
        }

        public void columnAdded(TableColumnModelEvent p1)
        {
            if(p1.getSource() instanceof TableColumnModel)
                ((TableColumnModel)p1.getSource()).getColumn(p1.getToIndex()).setHeaderRenderer(headerRenderer);
        }

        public void columnMarginChanged(ChangeEvent changeevent)
        {
        }

        public void columnMoved(TableColumnModelEvent p1)
        {
            if(columns != null && (p1.getSource() instanceof TableColumnModel))
            {
                Object tempObject = columns.get(p1.getToIndex());
                columns.set(p1.getToIndex(), columns.get(p1.getFromIndex()));
                columns.set(p1.getFromIndex(), tempObject);
            }
        }

        public void columnSelectionChanged(ListSelectionEvent listselectionevent)
        {
        }

        public void columnRemoved(TableColumnModelEvent tablecolumnmodelevent)
        {
        }

        public void paint(Graphics g)
        {
            rect = getBounds();
            Rectangle r = rect;
            r.x = 1;
            r.y = 1;
            r.width = r.width - 2;
            r.height = r.height - 1;
            Graphics2D g2 = (Graphics2D)g;
            int width = r.width;
            int height = (int)((float)r.height * 0.5F);
            java.awt.Paint storedPaint = g2.getPaint();
            g2.setPaint(new GradientPaint(r.x, r.y, DynaTheme.BRIGHT_FINISH, r.x, height, DynaTheme.BRIGHT_BEGIN));
            g2.fillRect(r.x, r.y, width, height);
            g2.setPaint(new GradientPaint(r.x, r.y + height, DynaTheme.DARK_BEGIN, r.x, r.y + height + height, DynaTheme.DARK_FINISH));
            g2.fillRect(r.x, r.y + height, width, height);
            g2.setPaint(storedPaint);
            r.width = r.width + 2;
            r.height = r.height + 1;
            g2.setColor(PlasticLookAndFeel.getControlDarkShadow());
            g2.drawLine(1, 0, r.width - 2, 0);
            g2.drawLine(1, r.height - 1, r.width - 2, r.height - 1);
            g2.drawLine(0, 1, 0, r.height - 2);
            g2.drawLine(r.width - 1, 1, r.width - 1, r.height - 2);
            g2 = null;
            super.paint(g);
        }

        private Rectangle rect;

        public HeaderRenderer()
        {
            rect = null;
        }
    }

    private class ColumnFilterDialog extends JDialog
        implements ActionListener
    {

        private void initComponents()
        {
            buttonPanel = new JPanel();
            okButton = new JButton();
            cancelButton = new JButton();
            centerPanel = new JPanel();
            optionCheckBox = new JCheckBox();
            conditionPanel = new JPanel();
            conditionComboBox = new JComboBox();
            conditionTextField = new JTextField();
            buttonPanel.setDoubleBuffered(true);
            centerPanel.setDoubleBuffered(true);
            conditionPanel.setDoubleBuffered(true);
            setDoubleBuffered(true);
            setTitle("\uD544\uD130: " + filter.getName());
            setDefaultCloseOperation(2);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent evt)
                {
                    closeDialog(evt);
                }

            });
            buttonPanel.setLayout(new FlowLayout(2, 3, 3));
            okButton.setText("\uD655\uC778");
            okButton.setDefaultCapable(true);
            okButton.setActionCommand("ok");
            okButton.addActionListener(actionListener);
            buttonPanel.add(okButton);
            cancelButton.setText("\uCDE8\uC18C");
            cancelButton.setDefaultCapable(false);
            cancelButton.setActionCommand("cancel");
            cancelButton.addActionListener(actionListener);
            buttonPanel.add(cancelButton);
            getContentPane().add(buttonPanel, "South");
            centerPanel.setLayout(new FlowLayout(0, 5, 5));
            optionCheckBox.setFont(new Font("Dialog", 0, 12));
            optionCheckBox.setText("\uD544\uD130 \uD65C\uC131\uD654");
            centerPanel.add(optionCheckBox);
            getContentPane().add(centerPanel, "Center");
            conditionPanel.setLayout(new FlowLayout(0, 5, 5));
            conditionComboBox.setMaximumRowCount(3);
            conditionComboBox.setFont(new Font("Dialog", 0, 12));
            conditionComboBox.setPreferredSize(new Dimension(110, 21));
            conditionComboBox.setMinimumSize(new Dimension(110, 21));
            conditionPanel.add(conditionComboBox);
            conditionTextField.setPreferredSize(new Dimension(96, 21));
            conditionTextField.setMinimumSize(new Dimension(8, 21));
            conditionPanel.add(conditionTextField);
            getContentPane().add(conditionPanel, "North");
        }

        private void closeDialog(WindowEvent evt)
        {
            okButton.removeActionListener(actionListener);
            cancelButton.removeActionListener(actionListener);
            actionListener = null;
            filter = null;
            columnFilterDialog = null;
            setVisible(false);
            dispose();
        }

        public void actionPerformed(ActionEvent evt)
        {
            if(evt.getActionCommand().equals("ok"))
            {
                filter.setFilterActivated(optionCheckBox.isSelected());
                filter.setFilterComparator(conditionComboBox.getSelectedIndex());
                filter.setFilterCompareValue(conditionTextField.getText());
                closeDialog(null);
            } else
            if(evt.getActionCommand().equals("cancel"))
                closeDialog(null);
        }

        private JPanel buttonPanel;
        private JButton okButton;
        private JButton cancelButton;
        private JPanel centerPanel;
        private JCheckBox optionCheckBox;
        private JPanel conditionPanel;
        private JComboBox conditionComboBox;
        private JTextField conditionTextField;
        private ActionListener actionListener;
        private DynaColumn filter;


        public ColumnFilterDialog(DynaColumn filter)
        {
            super(null, true);
            actionListener = null;
            this.filter = null;
            this.filter = filter;
            if(DynaTable.conditions == null)
            {
                DynaTable.conditions = new ArrayList();
                DynaTable.conditions.add("=");
                DynaTable.conditions.add("<>");
                DynaTable.conditions.add("\uD3EC\uD568");
                DynaTable.conditions.add("\uD3EC\uD568\uD558\uC9C0 \uC54A\uC74C");
                DynaTable.conditions.add("\uC2DC\uC791");
                DynaTable.conditions.add("\uC885\uB8CC");
            }
            actionListener = this;
            initComponents();
            for(Iterator conditions = DynaTable.conditions.iterator(); conditions.hasNext(); conditionComboBox.addItem(conditions.next()));
            optionCheckBox.setSelected(filter.isFilterActivated());
            conditionComboBox.setSelectedIndex(filter.getFilterComparator());
            conditionTextField.setText(filter.getFilterCompareValue());
            pack();
        }
    }


    public DynaTable()
    {
        rowHeader = null;
        evenRowColor = null;
        customCellColorEnabled = true;
        cellColor = null;
        renderer = null;
        headerRenderer = null;
        columns = null;
        columnFilterDialog = null;
        font = null;
        setBackground(getBackground());
        cellColor = new DefaultCellColor();
        renderer = new CellRenderer();
        setDefaultRenderer(java.lang.Object.class, renderer);
        setDefaultRenderer(java.lang.Boolean.class, renderer);
        setDefaultRenderer(java.lang.Integer.class, renderer);
        setDefaultRenderer(java.lang.Long.class, renderer);
        setDefaultRenderer(java.lang.Short.class, renderer);
        setDefaultRenderer(java.lang.Float.class, renderer);
        setDefaultRenderer(java.lang.Double.class, renderer);
        setDefaultRenderer(java.lang.Byte.class, renderer);
        setDefaultRenderer(java.math.BigInteger.class, renderer);
        setDefaultRenderer(java.math.BigDecimal.class, renderer);
        headerRenderer = new HeaderRenderer();
        getColumnModel().addColumnModelListener((TableColumnModelListener)headerRenderer);
        getTableHeader().setUpdateTableInRealTime(false);
        setRowHeight(20);
        setGridColor(new Color(213, 211, 214));
        setShowHorizontalLines(false);
        getTableHeader().addMouseListener(this);
        font = new Font("dialog", 0, 11);
        addAncestorListener(this);
    }

    public void setBackground(Color color)
    {
        setBackground(color);
        evenRowColor = new Color(237, 237, 249);
    }

    public void setEvenColor(Color color)
    {
        evenRowColor = new Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public boolean isCustomCellColorEnabled()
    {
        return customCellColorEnabled;
    }

    public void setCustomCellColorEnabled(boolean enabled)
    {
        customCellColorEnabled = enabled;
    }

    public void setCellColor(DynaTableCellColor cellColor)
    {
        this.cellColor = cellColor;
    }

    public boolean isValueGroupedColumn(int index)
    {
label0:
        {
            synchronized(this)
            {
                if(columns != null && columns.size() - 1 >= index)
                    break label0;
            }
            return false;
        }
        ((DynaColumn)columns.get(index)).isValueGrouped();
        dynatable;
        JVM INSTR monitorexit ;
        return;
    }

    public void setValueGroupedColumn(int index, boolean value)
    {
        synchronized(this)
        {
            if(columns == null)
                columns = new ArrayList();
            if(columns.size() < getColumnCount())
            {
                DynaColumn column = null;
                TableColumnModel columnModel = getColumnModel();
                for(int i = columns.size(); i < getColumnCount(); i++)
                {
                    column = new DynaColumn((String)columnModel.getColumn(i).getIdentifier(), (String)columnModel.getColumn(i).getHeaderValue(), null);
                    columns.add(column);
                }

            }
            ((DynaColumn)columns.get(index)).setValueGrouped(value);
        }
    }

    public void setColumns(ArrayList columns)
    {
        synchronized(this)
        {
            if(this.columns != null)
            {
                this.columns.clear();
                this.columns = null;
            }
            this.columns = columns;
        }
    }

    public ArrayList getColumns()
    {
        DynaTable dynatable = this;
        JVM INSTR monitorenter ;
        return columns;
        dynatable;
        JVM INSTR monitorexit ;
        throw ;
    }

    public DynaColumn getColumn(int index)
    {
        DynaTable dynatable = this;
        JVM INSTR monitorenter ;
        if(columns != null && columns.size() - 1 >= index)
            return (DynaColumn)columns.get(index);
        dynatable;
        JVM INSTR monitorexit ;
          goto _L1
        dynatable;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        return null;
    }

    public void setColumn(int index, DynaColumn column)
    {
        synchronized(this)
        {
            if(columns == null)
                columns = new ArrayList();
            if(columns.size() < getColumnCount())
            {
                DynaColumn dummyColumn = null;
                TableColumnModel columnModel = getColumnModel();
                for(int i = columns.size(); i < getColumnCount(); i++)
                {
                    dummyColumn = new DynaColumn((String)columnModel.getColumn(i).getIdentifier(), (String)columnModel.getColumn(i).getHeaderValue(), (String)columnModel.getColumn(i).getIdentifier());
                    columns.add(dummyColumn);
                }

            }
            columns.set(index, column);
        }
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        String oldField = this.field;
        this.field = field;
        firePropertyChange("field", oldField, field);
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        String oldIdentifier = this.identifier;
        this.identifier = identifier;
        firePropertyChange("identifier", oldIdentifier, identifier);
    }

    public String getTitleText()
    {
        return titleText;
    }

    public void setTitleText(String titleText)
    {
        String oldTitleText = this.titleText;
        this.titleText = titleText;
        firePropertyChange("titleText", oldTitleText, titleText);
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory(boolean mandatory)
    {
        boolean oldMandatory = this.mandatory;
        this.mandatory = mandatory;
        firePropertyChange("mandatory", oldMandatory, mandatory);
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        boolean oldEditable = this.editable;
        this.editable = editable;
        firePropertyChange("editable", oldEditable, editable);
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        String oldDataType = this.dataType;
        this.dataType = dataType;
        firePropertyChange("dataType", oldDataType, dataType);
    }

    public double getDataSize()
    {
        return dataSize;
    }

    public void setDataSize(double dataSize)
    {
        double oldDataSize = this.dataSize;
        this.dataSize = dataSize;
        firePropertyChange("dataSize", oldDataSize, dataSize);
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public RowHeader getRowHeader()
    {
        if(rowHeader == null)
            rowHeader = new RowHeader(this);
        return rowHeader;
    }

    public void valueChanged(ListSelectionEvent e)
    {
        valueChanged(e);
        if(rowHeader != null)
            rowHeader.repaint();
    }

    public void ancestorAdded(AncestorEvent event)
    {
        JComponent comp = (JComponent)event.getSource();
        if(comp == null)
            return;
        do
        {
            comp = (JComponent)comp.getParent();
            if(comp == null)
                return;
        } while(!(comp instanceof JScrollPane));
        if(comp instanceof JScrollPane)
            ((JScrollPane)comp).setRowHeaderView(getRowHeader());
    }

    public void ancestorMoved(AncestorEvent ancestorevent)
    {
    }

    public void ancestorRemoved(AncestorEvent ancestorevent)
    {
    }

    private static final String PROP_FIELD_PROPERTY = "field";
    private static final String PROP_IDENTIFIER_PROPERTY = "identifier";
    private static final String PROP_TITLETEXT_PROPERTY = "titleText";
    private static final String PROP_EDITABLE_PROPERTY = "editable";
    private static final String PROP_MANDATORY_PROPERTY = "mandatory";
    private static final String PROP_DATATYPE_PROPERTY = "dataType";
    private static final String PROP_DATASIZE_PROPERTY = "dataSize";
    private RowHeader rowHeader;
    public static final int DEFAULT_ROWHEIGHT = 20;
    private static ArrayList conditions = null;
    private String field;
    private String identifier;
    private boolean mandatory;
    private boolean editable;
    private String dataType;
    private double dataSize;
    private String titleText;
    private Color evenRowColor;
    private boolean customCellColorEnabled;
    private DynaTableCellColor cellColor;
    private CellRenderer renderer;
    private TableCellRenderer headerRenderer;
    private ArrayList columns;
    private ColumnFilterDialog columnFilterDialog;
    private Font font;











}
