// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaComboBox.java

package dyna.uic;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;

// Referenced classes of package dyna.uic:
//            DynaComponent, DynaConstants

public class DynaComboBox extends JPanel
    implements DynaComponent
{

    public DynaComboBox()
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel("", 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setFont(font);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(comboBox == null)
            comboBox = new JComboBox();
        comboBox.setDoubleBuffered(true);
        add(comboBox);
        title.setVisible(false);
        setMandatory(false);
        determineIcon();
    }

    public DynaComboBox(ComboBoxModel aModel, String field, String identifier, String name, String titleText, int titleWidth, boolean titleVisible, 
            boolean mandatory, boolean editable, String dataType, double dataSize)
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel("", 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setFont(font);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(comboBox != null)
            comboBox.setModel(aModel);
        else
            comboBox = new JComboBox(aModel);
        comboBox.setDoubleBuffered(true);
        add(comboBox);
        title.setSize(titleWidth, getHeight());
        title.setVisible(titleVisible);
        this.field = field;
        this.identifier = identifier;
        setName(name);
        setMandatory(mandatory);
        setEditable(editable);
        this.dataType = dataType;
        this.dataSize = dataSize;
        determineIcon();
    }

    public DynaComboBox(ComboBoxModel aModel, String titleText)
    {
        this(aModel, null, null, null, titleText, -1, true, false, true, "String", 0.0D);
    }

    public DynaComboBox(ComboBoxModel aModel)
    {
        this(aModel, null, null, null, "", -1, true, false, true, "String", 0.0D);
    }

    public DynaComboBox(Object items[], String field, String identifier, String name, String titleText, int titleWidth, boolean titleVisible, 
            boolean mandatory, boolean editable, String dataType, double dataSize)
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel("", 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setFont(font);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(comboBox != null)
            comboBox.setModel(new DefaultComboBoxModel(items));
        else
            comboBox = new JComboBox(items);
        comboBox.setDoubleBuffered(true);
        add(comboBox);
        title.setSize(titleWidth, getHeight());
        title.setVisible(titleVisible);
        this.field = field;
        this.identifier = identifier;
        setName(name);
        setMandatory(mandatory);
        setEditable(editable);
        this.dataType = dataType;
        this.dataSize = dataSize;
        determineIcon();
    }

    public DynaComboBox(Object items[], String titleText)
    {
        this(items, null, null, null, titleText, -1, true, false, true, "String", 0.0D);
    }

    public DynaComboBox(Object items[])
    {
        this(items, null, null, null, "", -1, true, false, true, "String", 0.0D);
    }

    public DynaComboBox(Vector items, String field, String identifier, String name, String titleText, int titleWidth, boolean titleVisible, 
            boolean mandatory, boolean editable, String dataType, double dataSize)
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel("", 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setFont(font);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(comboBox != null)
            comboBox.setModel(new DefaultComboBoxModel(items));
        else
            comboBox = new JComboBox(items);
        comboBox.setDoubleBuffered(true);
        add(comboBox);
        title.setSize(titleWidth, getHeight());
        title.setVisible(titleVisible);
        this.field = field;
        this.identifier = identifier;
        setName(name);
        setMandatory(mandatory);
        setEditable(editable);
        this.dataType = dataType;
        this.dataSize = dataSize;
        determineIcon();
    }

    public DynaComboBox(Vector items, String titleText)
    {
        this(items, null, null, null, titleText, -1, true, false, true, "String", 0.0D);
    }

    public DynaComboBox(Vector items)
    {
        this(items, null, null, null, "", -1, true, false, true, "String", 0.0D);
    }

    public void setBounds(Rectangle r)
    {
        Rectangle rectangle = new Rectangle(r);
        rectangle.width = title.getWidth();
        if(rectangle.width < 0)
            rectangle.width = r.width >> 1;
        title.setBounds(rectangle);
        Dimension dimension = title.getSize();
        dimension.height = 1;
        title.setMinimumSize(dimension);
        title.setPreferredSize(dimension);
        super.setBounds(r);
    }

    public void setBounds(int x, int y, int w, int h)
    {
        int w1 = title.getWidth();
        if(w1 < 0)
            w1 = w >> 1;
        title.setBounds(x, y, w1, h);
        Dimension dimension = title.getSize();
        dimension.height = 21;
        title.setMinimumSize(dimension);
        title.setPreferredSize(dimension);
        title.setMaximumSize(dimension);
        dimension = comboBox.getSize();
        int w2 = dimension.width;
        if(w2 + w1 + 10 != w)
        {
            dimension.width = w - w1 - 10;
            dimension.height = dimension.height != 0 ? dimension.height : 21;
            comboBox.setMaximumSize(dimension);
            comboBox.setPreferredSize(dimension);
        }
        super.setBounds(x, y, w, h);
    }

    public void setSize(Dimension d)
    {
        Dimension dimension = new Dimension(d);
        dimension.width = title.getWidth();
        if(dimension.width < 0)
            dimension.width = d.width >> 1;
        title.setSize(dimension);
        dimension.height = 1;
        title.setMinimumSize(dimension);
        title.setPreferredSize(dimension);
        super.setSize(d);
    }

    public void setSize(int w, int h)
    {
        int w1 = title.getWidth();
        if(w1 < 0)
            w1 = w >> 1;
        title.setSize(w1, h);
        Dimension dimension = title.getSize();
        dimension.height = 21;
        title.setMinimumSize(dimension);
        title.setPreferredSize(dimension);
        title.setMaximumSize(dimension);
        dimension = comboBox.getSize();
        int w2 = dimension.width;
        if(w2 + w1 + 10 != w)
        {
            dimension.width = w - w1 - 10;
            dimension.height = dimension.height != 0 ? dimension.height : 21;
            comboBox.setMaximumSize(dimension);
            comboBox.setPreferredSize(dimension);
        }
        super.setSize(w, h);
    }

    public JLabel getTitle()
    {
        return title;
    }

    public void setTitle(JLabel title)
    {
        JLabel oldTitle = this.title;
        String oldTitleText = title != null ? title.getText() : null;
        this.title = title;
        if(title != null)
            add(title);
        else
            remove(title);
        firePropertyChange("title", oldTitle, title);
        firePropertyChange("titleText", oldTitleText, title.getText());
    }

    public int getTitleWidth()
    {
        return title != null ? title.getWidth() : -1;
    }

    public void setTitleWidth(int titleWidth)
    {
        if(titleWidth < 0)
        {
            return;
        } else
        {
            int OldTitleWidth = title.getWidth();
            title.setSize(titleWidth, getHeight());
            firePropertyChange("titleWidth", OldTitleWidth, titleWidth);
            return;
        }
    }

    public boolean isTitleVisible()
    {
        return title.isVisible();
    }

    public void setTitleVisible(boolean titleVisible)
    {
        boolean oldTitleVisible = title.isVisible();
        title.setVisible(titleVisible);
        firePropertyChange("titleVisible", oldTitleVisible, titleVisible);
    }

    public JComboBox getComboBox()
    {
        return comboBox;
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
        return title != null ? title.getText() : null;
    }

    public void setTitleText(String titleText)
    {
        String oldTitleText = null;
        oldTitleText = title.getText();
        title.setText(titleText);
        firePropertyChange("dataType", oldTitleText, titleText);
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory(boolean mandatory)
    {
        boolean oldMandatory = this.mandatory;
        this.mandatory = mandatory;
        determineIcon();
        firePropertyChange("mandatory", oldMandatory, mandatory);
    }

    public boolean isEditable()
    {
        return comboBox.isEditable();
    }

    public void setEditable(boolean editable)
    {
        boolean oldEditable = comboBox.isEditable();
        comboBox.setEditable(editable);
        determineIcon();
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

    public ImageIcon getIcon()
    {
        return icon;
    }

    public void setIcon(ImageIcon icon)
    {
        ImageIcon oldIcon = this.icon;
        this.icon = icon;
        determineIcon();
        firePropertyChange("icon", oldIcon, icon);
    }

    public boolean isEditableIconVisible()
    {
        return editableIconVisible;
    }

    public void setEditableIconVisible(boolean editableIconVisible)
    {
        boolean oldEditableIconVisible = this.editableIconVisible;
        this.editableIconVisible = editableIconVisible;
        determineIcon();
        firePropertyChange("editableIconVisible", oldEditableIconVisible, editableIconVisible);
    }

    private void determineIcon()
    {
        if(icon != null)
            title.setIcon(icon);
        else
        if(mandatory)
        {
            if(DynaConstants.IMAGEICON_MANDATORY == null)
                DynaConstants.IMAGEICON_MANDATORY = new ImageIcon(getClass().getResource("/icons/Mandatory16.gif"));
            title.setIcon(DynaConstants.IMAGEICON_MANDATORY);
        } else
        if(editableIconVisible && isEditable())
        {
            if(DynaConstants.IMAGEICON_EDITABLE == null)
                DynaConstants.IMAGEICON_EDITABLE = new ImageIcon(getClass().getResource("/icons/Editable16.gif"));
            title.setIcon(DynaConstants.IMAGEICON_EDITABLE);
        } else
        {
            title.setIcon(null);
        }
    }

    public void setModel(ComboBoxModel aModel)
    {
        comboBox.setModel(aModel);
    }

    public ComboBoxModel getModel()
    {
        return comboBox.getModel();
    }

    public void setMaximumRowCount(int count)
    {
        comboBox.setMaximumRowCount(count);
    }

    public int getMaximumRowCount()
    {
        return getMaximumRowCount();
    }

    public void setRenderer(ListCellRenderer aRenderer)
    {
        comboBox.setRenderer(aRenderer);
    }

    public ListCellRenderer getRenderer()
    {
        return comboBox.getRenderer();
    }

    public void setEditor(ComboBoxEditor anEditor)
    {
        comboBox.setEditor(anEditor);
    }

    public ComboBoxEditor getEditor()
    {
        return comboBox.getEditor();
    }

    public void setSelectedItem(Object anObject)
    {
        comboBox.setSelectedItem(anObject);
    }

    public Object getSelectedItem()
    {
        return comboBox.getSelectedItem();
    }

    public void setSelectedIndex(int anIndex)
    {
        comboBox.setSelectedIndex(anIndex);
    }

    public int getSelectedIndex()
    {
        return comboBox.getSelectedIndex();
    }

    public void addItem(Object anObject)
    {
        comboBox.addItem(anObject);
    }

    public void insertItemAt(Object anObject, int index)
    {
        comboBox.insertItemAt(anObject, index);
    }

    public void removeItem(Object anObject)
    {
        comboBox.removeItem(anObject);
    }

    public void removeItemAt(int anIndex)
    {
        comboBox.removeItemAt(anIndex);
    }

    public void removeAllItems()
    {
        comboBox.removeAllItems();
    }

    public void showPopup()
    {
        comboBox.showPopup();
    }

    public void hidePopup()
    {
        comboBox.hidePopup();
    }

    public void setPopupVisible(boolean v)
    {
        comboBox.setPopupVisible(v);
    }

    public boolean isPopupVisible()
    {
        return comboBox.isPopupVisible();
    }

    public void addItemListener(ItemListener aListener)
    {
        if(comboBox == null)
            comboBox = new JComboBox();
        comboBox.addItemListener(aListener);
    }

    public void removeItemListener(ItemListener aListener)
    {
        comboBox.removeItemListener(aListener);
    }

    public void addActionListener(ActionListener l)
    {
        if(comboBox == null)
            comboBox = new JComboBox();
        comboBox.addActionListener(l);
    }

    public void removeActionListener(ActionListener l)
    {
        comboBox.removeActionListener(l);
    }

    public void setActionCommand(String aCommand)
    {
        comboBox.setActionCommand(aCommand);
    }

    public String getActionCommand()
    {
        return comboBox.getActionCommand();
    }

    public Object[] getSelectedObjects()
    {
        return comboBox.getSelectedObjects();
    }

    public boolean selectWithKeyChar(char keyChar)
    {
        return comboBox.selectWithKeyChar(keyChar);
    }

    public void intervalAdded(ListDataEvent e)
    {
        comboBox.intervalAdded(e);
    }

    public void intervalRemoved(ListDataEvent e)
    {
        comboBox.intervalRemoved(e);
    }

    public void setEnabled(boolean b)
    {
        comboBox.setEnabled(b);
    }

    public void configureEditor(ComboBoxEditor anEditor, Object anItem)
    {
        comboBox.configureEditor(anEditor, anItem);
    }

    public void processKeyEvent(KeyEvent e)
    {
        comboBox.processKeyEvent(e);
    }

    public void setKeySelectionManager(javax.swing.JComboBox.KeySelectionManager aManager)
    {
        comboBox.setKeySelectionManager(aManager);
    }

    public javax.swing.JComboBox.KeySelectionManager getKeySelectionManager()
    {
        return comboBox.getKeySelectionManager();
    }

    public int getItemCount()
    {
        return comboBox.getItemCount();
    }

    public Object getItemAt(int index)
    {
        return comboBox.getItemAt(index);
    }

    public AccessibleContext getAccessibleContext()
    {
        return comboBox.getAccessibleContext();
    }

    public void addFocusListener(FocusListener l)
    {
        if(comboBox == null)
            comboBox = new JComboBox();
        comboBox.addFocusListener(l);
    }

    public void removeFocusListener(FocusListener l)
    {
        comboBox.removeFocusListener(l);
    }

    public boolean isManagingFocus()
    {
        return comboBox.isManagingFocus();
    }

    public boolean isFocusTraversable()
    {
        return comboBox.isFocusTraversable();
    }

    public boolean isFoucsCycleRoot()
    {
        return comboBox.isFocusCycleRoot();
    }

    public boolean requestDefaultFocus()
    {
        return comboBox.requestDefaultFocus();
    }

    public void setRequestFocusEnable(boolean flag)
    {
        comboBox.setRequestFocusEnabled(flag);
    }

    public boolean isRequestFocusEnabled()
    {
        return comboBox.isRequestFocusEnabled();
    }

    public void requestFocus()
    {
        comboBox.requestFocus();
    }

    public Component getNextFocusableComponent()
    {
        return comboBox.getNextFocusableComponent();
    }

    public void setNextFocusableComponent(Component component)
    {
        comboBox.setNextFocusableComponent(component);
    }

    public void addMouseListener(MouseListener l)
    {
        comboBox.addMouseListener(l);
    }

    public void removeMouseListener(MouseListener l)
    {
        comboBox.removeMouseListener(l);
    }

    public void setFont(Font font)
    {
        comboBox.setFont(font);
    }

    public Font getFont()
    {
        return comboBox.getFont();
    }

    private static final String PROP_TITLE_PROPERTY = "title";
    private static final String PROP_TITLEWIDTH_PROPERTY = "titleWidth";
    private static final String PROP_TITLEVISIBLE_PROPERTY = "titleVisible";
    private static final String PROP_COMBOBOX_PROPERTY = "comboBox";
    private static final String PROP_FIELD_PROPERTY = "field";
    private static final String PROP_IDENTIFIER_PROPERTY = "identifier";
    private static final String PROP_TITLETEXT_PROPERTY = "titleText";
    private static final String PROP_EDITABLE_PROPERTY = "editable";
    private static final String PROP_MANDATORY_PROPERTY = "mandatory";
    private static final String PROP_DATATYPE_PROPERTY = "dataType";
    private static final String PROP_DATASIZE_PROPERTY = "dataSize";
    private static final String PROP_ICON_PROPERTY = "icon";
    private static final String PROP_EDITABLE_ICON_VISIBLE_PROPERTY = "editableIconVisible";
    private static Border border = BorderFactory.createEmptyBorder(2, 5, 1, 5);
    private static Font font = new Font("Dialog", 0, 12);
    private static Dimension maximumDimension = new Dimension(32767, 24);
    private static Dimension minimumDimension = new Dimension(1, 24);
    private int tempWidth1;
    private JLabel title;
    private JComboBox comboBox;
    private String field;
    private String identifier;
    private boolean mandatory;
    private String dataType;
    private double dataSize;
    private ImageIcon icon;
    private boolean editableIconVisible;

    static 
    {
        UIManager.put("ComboBox.disabledForeground", Color.black);
    }
}
