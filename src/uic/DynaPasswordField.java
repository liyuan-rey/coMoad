// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaPasswordField.java

package dyna.uic;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretListener;
import javax.swing.text.*;

// Referenced classes of package dyna.uic:
//            DynaComponent, DynaConstants

public class DynaPasswordField extends JComponent
    implements DynaComponent
{

    public DynaPasswordField()
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
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(textField == null)
        {
            textField = new JPasswordField();
            textField.setDoubleBuffered(true);
            textField.setText("");
        }
        add(textField);
        title.setVisible(false);
        setMandatory(false);
        setEditable(true);
        determineIcon();
    }

    public DynaPasswordField(Document doc, String text, int columns, String field, String identifier, String name, String titleText, 
            int titleWidth, boolean titleVisible, boolean mandatory, boolean editable, String dataType, double dataSize)
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel(titleText, 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(textField != null)
        {
            textField.setDocument(doc);
            textField.setText(text);
            textField.setColumns(columns);
            textField.setDoubleBuffered(true);
        } else
        {
            textField = new JPasswordField(doc, text, columns);
            textField.setDoubleBuffered(true);
        }
        add(textField);
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

    public DynaPasswordField(Document doc, String text, int columns, String titleText)
    {
        this(doc, text, columns, null, null, null, titleText, -1, true, false, true, "String", 0.0D);
    }

    public DynaPasswordField(Document doc, String text, int columns)
    {
        this(doc, text, columns, null, null, null, "", -1, false, false, true, "String", 0.0D);
    }

    public DynaPasswordField(int columns, String field, String identifier, String name, String titleText, int titleWidth, boolean titleVisible, 
            boolean mandatory, boolean editable, String dataType, double dataSize)
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel(titleText, 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(textField != null)
        {
            textField.setColumns(columns);
            textField.setDoubleBuffered(true);
        } else
        {
            textField = new JPasswordField(columns);
            textField.setDoubleBuffered(true);
        }
        add(textField);
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

    public DynaPasswordField(int columns, String titleText)
    {
        this(columns, null, null, null, titleText, -1, true, false, true, "String", 0.0D);
    }

    public DynaPasswordField(int columns)
    {
        this(columns, null, null, null, "", -1, false, false, true, "String", 0.0D);
    }

    public DynaPasswordField(String text, String field, String identifier, String name, String titleText, int titleWidth, boolean titleVisible, 
            boolean mandatory, boolean editable, String dataType, double dataSize)
    {
        tempWidth1 = 0;
        icon = null;
        editableIconVisible = false;
        setLayout(new BoxLayout(this, 0));
        setDoubleBuffered(true);
        title = new JLabel(titleText, 2);
        title.setHorizontalTextPosition(2);
        title.setVerticalTextPosition(0);
        title.setDoubleBuffered(true);
        setBorder(border);
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(textField != null)
        {
            textField.setText(text);
            textField.setDoubleBuffered(true);
        } else
        {
            textField = new JPasswordField(text);
            textField.setDoubleBuffered(true);
        }
        add(textField);
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

    public DynaPasswordField(String text, String titleText)
    {
        this(text, null, null, null, titleText, -1, true, false, true, "String", 0.0D);
    }

    public DynaPasswordField(String text)
    {
        this(text, null, null, null, "", -1, false, false, true, "String", 0.0D);
    }

    public DynaPasswordField(String text, int columns)
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
        setMaximumSize(maximumDimension);
        setMinimumSize(minimumDimension);
        setPreferredSize(minimumDimension);
        add(title);
        if(textField != null)
        {
            textField.setText(text);
            textField.setColumns(columns);
            textField.setDoubleBuffered(true);
        }
        add(textField);
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
        dimension.height = 1;
        title.setMinimumSize(dimension);
        title.setPreferredSize(dimension);
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
        dimension.height = 1;
        title.setMinimumSize(dimension);
        title.setPreferredSize(dimension);
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

    public JPasswordField getTextField()
    {
        return textField;
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
        return textField.isEditable();
    }

    public void setEditable(boolean editable)
    {
        boolean oldEditable = textField.isEditable();
        textField.setEditable(editable);
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

    public void addActionListener(ActionListener l)
    {
        textField.addActionListener(l);
    }

    public AccessibleContext getAccessibleContext()
    {
        return textField.getAccessibleContext();
    }

    public Action[] getActions()
    {
        return textField.getActions();
    }

    public int getColumns()
    {
        return textField.getColumns();
    }

    public int getHorizontalAlignment()
    {
        return textField.getHorizontalAlignment();
    }

    public BoundedRangeModel getHorizontalVisibility()
    {
        return textField.getHorizontalVisibility();
    }

    public int getScrollOffset()
    {
        return textField.getScrollOffset();
    }

    public void postActionEvent()
    {
        textField.postActionEvent();
    }

    public void removeActionListener(ActionListener l)
    {
        textField.removeActionListener(l);
    }

    public void scrollToVisible(Rectangle r)
    {
        textField.scrollRectToVisible(r);
    }

    public void setActionCommand(String command)
    {
        textField.setActionCommand(command);
    }

    public void setColumns(int columns)
    {
        textField.setColumns(columns);
    }

    public void setHorizontalAlignment(int alignment)
    {
        textField.setHorizontalAlignment(alignment);
    }

    public void setScrollOffset(int scrollOffset)
    {
        textField.setScrollOffset(scrollOffset);
    }

    public void addCaretListener(CaretListener listener)
    {
        textField.addCaretListener(listener);
    }

    public void removeCaretListener(CaretListener listener)
    {
        textField.removeCaretListener(listener);
    }

    public void setDocument(Document doc)
    {
        textField.setDocument(doc);
    }

    public Document getDocument()
    {
        return textField.getDocument();
    }

    public void setMargin(Insets m)
    {
        textField.setMargin(m);
    }

    public Insets getMargin()
    {
        return textField.getMargin();
    }

    public Caret getCaret()
    {
        return textField.getCaret();
    }

    public void setCaret(Caret c)
    {
        textField.setCaret(c);
    }

    public Highlighter getHighlighter()
    {
        return textField.getHighlighter();
    }

    public void setHighlighter(Highlighter h)
    {
        textField.setHighlighter(h);
    }

    public void setKeymap(Keymap map)
    {
        textField.setKeymap(map);
    }

    public Keymap getKeymap()
    {
        return textField.getKeymap();
    }

    public static Keymap addKeymap(String nm, Keymap parent)
    {
        return JTextComponent.addKeymap(nm, parent);
    }

    public static Keymap removeKeymap(String nm)
    {
        return JTextComponent.removeKeymap(nm);
    }

    public static Keymap getKeymap(String nm)
    {
        return JTextComponent.getKeymap(nm);
    }

    public static void loadKeymap(Keymap map, javax.swing.text.JTextComponent.KeyBinding bindings[], Action actions[])
    {
        JTextComponent.loadKeymap(map, bindings, actions);
    }

    public Color getCaretColor()
    {
        return textField.getCaretColor();
    }

    public void setCaretColor(Color c)
    {
        textField.setCaretColor(c);
    }

    public Color getSelectionColor()
    {
        return textField.getSelectionColor();
    }

    public Color getSelectedTextColor()
    {
        return textField.getSelectedTextColor();
    }

    public void setSelectedTextColor(Color c)
    {
        textField.setSelectedTextColor(c);
    }

    public Color getDisabledTextColor()
    {
        return textField.getDisabledTextColor();
    }

    public void setDisabledTextColor(Color c)
    {
        textField.setDisabledTextColor(c);
    }

    public void replaceSelection(String content)
    {
        textField.replaceSelection(content);
    }

    public Rectangle modelToView(int pos)
        throws BadLocationException
    {
        return textField.modelToView(pos);
    }

    public int viewToModel(Point pt)
    {
        return textField.viewToModel(pt);
    }

    public void cut()
    {
        textField.cut();
    }

    public void copy()
    {
        textField.copy();
    }

    public void paste()
    {
        textField.paste();
    }

    public void moveCaretPosition(int pos)
    {
        textField.moveCaretPosition(pos);
    }

    public void setFocusAccelerator(char aKey)
    {
        textField.setFocusAccelerator(aKey);
    }

    public char getFocusAccelerator()
    {
        return textField.getFocusAccelerator();
    }

    public void read(Reader in, Object desc)
        throws IOException
    {
        textField.read(in, desc);
    }

    public void write(Writer out)
        throws IOException
    {
        textField.write(out);
    }

    public void setEnabled(boolean b)
    {
        textField.setEnabled(b);
    }

    public void addFocusListener(FocusListener l)
    {
        if(textField == null)
            textField = new JPasswordField();
        textField.addFocusListener(l);
    }

    public void removeFocusListener(FocusListener l)
    {
        textField.removeFocusListener(l);
    }

    public boolean isManagingFocus()
    {
        return textField.isManagingFocus();
    }

    public boolean isFocusTraversable()
    {
        return textField.isFocusTraversable();
    }

    public boolean isFoucsCycleRoot()
    {
        return textField.isFocusCycleRoot();
    }

    public boolean requestDefaultFocus()
    {
        return textField.requestDefaultFocus();
    }

    public void setRequestFocusEnable(boolean flag)
    {
        textField.setRequestFocusEnabled(flag);
    }

    public boolean isRequestFocusEnabled()
    {
        return textField.isRequestFocusEnabled();
    }

    public void requestFocus()
    {
        textField.requestFocus();
    }

    public Component getNextFocusableComponent()
    {
        return textField.getNextFocusableComponent();
    }

    public void setNextFocusableComponent(Component component)
    {
        textField.setNextFocusableComponent(component);
    }

    public void setCaretPosition(int position)
    {
        textField.setCaretPosition(position);
    }

    public void grabFocus()
    {
        textField.grabFocus();
    }

    public boolean hasFocus()
    {
        return textField.hasFocus();
    }

    public int getCaretPosition()
    {
        return textField.getCaretPosition();
    }

    public void setText(String t)
    {
        textField.setText(t);
    }

    public int getSelectionStart()
    {
        return textField.getSelectionStart();
    }

    public void setSelectionStart(int selectionStart)
    {
        textField.setSelectionStart(selectionStart);
    }

    public int getSelectionEnd()
    {
        return textField.getSelectionEnd();
    }

    public void setSelectionEnd(int selectionEnd)
    {
        textField.setSelectionEnd(selectionEnd);
    }

    public void select(int selectionStart, int selectionEnd)
    {
        textField.select(selectionStart, selectionEnd);
    }

    public void selectAll()
    {
        textField.selectAll();
    }

    public void addMouseListener(MouseListener l)
    {
        textField.addMouseListener(l);
    }

    public void removeMouseListener(MouseListener l)
    {
        textField.removeMouseListener(l);
    }

    public void setFont(Font font)
    {
        textField.setFont(font);
    }

    public Font getFont()
    {
        return textField.getFont();
    }

    public void addKeyListener(KeyListener l)
    {
        if(textField == null)
            textField = new JPasswordField();
        textField.addKeyListener(l);
    }

    public void removeKeyListener(KeyListener l)
    {
        textField.removeKeyListener(l);
    }

    public boolean echoCharIsSet()
    {
        return textField.echoCharIsSet();
    }

    public char getEchoChar()
    {
        return textField.getEchoChar();
    }

    public char[] getPassword()
    {
        return textField.getPassword();
    }

    public String getUIClassID()
    {
        return textField.getUIClassID();
    }

    public void setEchoChar(char c)
    {
        setEchoChar(c);
    }

    private static final String PROP_TITLE_PROPERTY = "title";
    private static final String PROP_TITLEWIDTH_PROPERTY = "titleWidth";
    private static final String PROP_TITLEVISIBLE_PROPERTY = "titleVisible";
    private static final String PROP_TEXTFIELD_PROPERTY = "textField";
    private static final String PROP_FIELD_PROPERTY = "field";
    private static final String PROP_IDENTIFIER_PROPERTY = "identifier";
    private static final String PROP_TITLETEXT_PROPERTY = "titleText";
    private static final String PROP_EDITABLE_PROPERTY = "editable";
    private static final String PROP_MANDATORY_PROPERTY = "mandatory";
    private static final String PROP_DATATYPE_PROPERTY = "dataType";
    private static final String PROP_DATASIZE_PROPERTY = "dataSize";
    private static final String PROP_ICON_PROPERTY = "icon";
    private static final String PROP_EDITABLE_ICON_VISIBLE_PROPERTY = "editableIconVisible";
    public static final String FOCUS_ACCELERATOR_KEY = "focusAcceleratorKey";
    public static final String DEFAULT_KEYMAP = "default";
    public static final String notifyAction = "notify-field-accept";
    private static Border border = BorderFactory.createEmptyBorder(2, 5, 1, 5);
    private static Dimension minimumDimension = new Dimension(1, 24);
    private static Dimension maximumDimension = new Dimension(32767, 24);
    private int tempWidth1;
    private JLabel title;
    private JPasswordField textField;
    private String field;
    private String identifier;
    private boolean mandatory;
    private String dataType;
    private double dataSize;
    private ImageIcon icon;
    private boolean editableIconVisible;

}
