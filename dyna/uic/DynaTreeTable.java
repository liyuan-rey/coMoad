// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaTreeTable.java

package dyna.uic;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.*;

// Referenced classes of package dyna.uic:
//            DynaComponent, DynaTreeTableModelAdapter, DynaTreeTableModel

public class DynaTreeTable extends JTable
    implements DynaComponent
{
    public class DynaTreeTableCellRenderer extends JTree
        implements TableCellRenderer
    {

        public void updateUI()
        {
            updateUI();
            javax.swing.tree.TreeCellRenderer tcr = getCellRenderer();
            if(tcr instanceof DefaultTreeCellRenderer)
            {
                DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
                dtcr.setBorderSelectionColor(null);
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
            }
        }

        public void setRowHeight(int rowHeight)
        {
            if(rowHeight > 0)
            {
                setRowHeight(rowHeight + (!System.getProperty("java.vendor").equals("IBM Corporation") || !System.getProperty("java.version").equals("1.1.8") ? 0 : getIntercellSpacing().height));
                if(DynaTreeTable.this != null && getRowHeight() != rowHeight)
                    DynaTreeTable.this.setRowHeight(getRowHeight());
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
            if(isSelected)
                setBackground(table.getSelectionBackground());
            else
                setBackground(table.getBackground());
            visibleRow = row;
            return this;
        }

        protected int visibleRow;
        private final Color evenRowColor = new Color(255, 255, 223);

        public DynaTreeTableCellRenderer(TreeModel model)
        {
            JTree(model);
        }
    }

    public class DynaTreeTableCellEditor extends AbstractCellEditor
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
                    if(getColumnClass(counter) != dyna.uic.DynaTreeTableModel.class)
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

        public DynaTreeTableCellEditor()
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
                            javax.swing.tree.TreePath selPath = tree.getPathForRow(counter);
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


    public DynaTreeTable(DynaTreeTableModel dynaTreeTableModel)
    {
        tree = null;
        tree = new DynaTreeTableCellRenderer(dynaTreeTableModel);
        setModel(new DynaTreeTableModelAdapter(dynaTreeTableModel, tree));
        ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
        tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());
        setDefaultRenderer(dyna.uic.DynaTreeTableModel.class, tree);
        setDefaultEditor(dyna.uic.DynaTreeTableModel.class, new DynaTreeTableCellEditor());
        if(tree.getRowHeight() < 1)
        {
            setRowHeight(18);
        } else
        {
            setRowHeight(getRowHeight() + 1);
            setRowHeight(getRowHeight() - 1);
        }
    }

    public void updateUI()
    {
        updateUI();
        if(tree != null)
            tree.updateUI();
        LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
    }

    public int getEditingRow()
    {
        return getColumnClass(editingColumn) != dyna.uic.DynaTreeTableModel.class ? editingRow : -1;
    }

    public void setRowHeight(int rowHeight)
    {
        setRowHeight(rowHeight);
        if(tree != null && tree.getRowHeight() != rowHeight)
            tree.setRowHeight(getRowHeight());
    }

    public JTree getTree()
    {
        return tree;
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

    private static final String PROP_FIELD_PROPERTY = "field";
    private static final String PROP_IDENTIFIER_PROPERTY = "identifier";
    private static final String PROP_TITLETEXT_PROPERTY = "titleText";
    private static final String PROP_EDITABLE_PROPERTY = "editable";
    private static final String PROP_MANDATORY_PROPERTY = "mandatory";
    private static final String PROP_DATATYPE_PROPERTY = "dataType";
    private static final String PROP_DATASIZE_PROPERTY = "dataSize";
    private String field;
    private String identifier;
    private boolean mandatory;
    private boolean editable;
    private String dataType;
    private double dataSize;
    private String titleText;
    protected DynaTreeTableCellRenderer tree;
}
