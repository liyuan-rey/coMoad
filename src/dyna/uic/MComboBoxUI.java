// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MComboBoxUI.java

package dyna.uic;

import com.jgoodies.plaf.LookUtils;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

// Referenced classes of package dyna.uic:
//            MComboBoxButton

public final class MComboBoxUI extends MetalComboBoxUI
{
    private class PlasticComboPopup extends javax.swing.plaf.metal.MetalComboBoxUI.MetalComboPopup
    {

        protected void configureList()
        {
            configureList();
            list.setForeground(UIManager.getColor("MenuItem.foreground"));
            list.setBackground(UIManager.getColor("MenuItem.background"));
        }

        protected void configureScroller()
        {
            configureScroller();
            scroller.getVerticalScrollBar().putClientProperty("JScrollBar.isFreeStanding", Boolean.FALSE);
        }

        PlasticComboPopup(JComboBox combo)
        {
            MetalComboPopup(MComboBoxUI.this, combo);
        }
    }


    public MComboBoxUI()
    {
    }

    public static ComponentUI createUI(JComponent b)
    {
        return new MComboBoxUI();
    }

    protected ComboBoxEditor createEditor()
    {
        return new UIResource();
    }

    protected ComboPopup createPopup()
    {
        return new PlasticComboPopup(comboBox);
    }

    public Dimension getMinimumSize(JComponent c)
    {
        if(!isMinimumSizeDirty)
            return new Dimension(cachedMinimumSize);
        Dimension size = null;
        if(!comboBox.isEditable() && arrowButton != null && (arrowButton instanceof MComboBoxButton))
        {
            MComboBoxButton button = (MComboBoxButton)arrowButton;
            Insets buttonInsets = button.getInsets();
            Insets insets = comboBox.getInsets();
            size = getDisplaySize();
            size.height += LookUtils.isLowRes ? 0 : 2;
            size.width += insets.left + insets.right;
            size.width += buttonInsets.left + buttonInsets.right;
            size.width += buttonInsets.right + button.getComboIcon().getIconWidth();
            size.height += insets.top + insets.bottom;
            size.height += buttonInsets.top + buttonInsets.bottom;
        } else
        if(comboBox.isEditable() && arrowButton != null && editor != null)
        {
            size = getDisplaySize();
            Insets insets = comboBox.getInsets();
            size.height += insets.top + insets.bottom;
        } else
        {
            size = getMinimumSize(c);
        }
        cachedMinimumSize.setSize(size.width, size.height);
        isMinimumSizeDirty = false;
        return new Dimension(cachedMinimumSize);
    }

    protected JButton createArrowButton()
    {
        return new MComboBoxButton(comboBox, comboBoxButtonIcon, comboBox.isEditable(), currentValuePane, listBox);
    }

    protected LayoutManager createLayoutManager()
    {
        return new PlasticComboBoxLayoutManager();
    }

    public PropertyChangeListener createPropertyChangeListener()
    {
        return new PlasticPropertyChangeListener();
    }

    private static Icon comboBoxButtonIcon = new ComboBoxButtonIcon();







}
