// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MComboBoxButton.java

package dyna.uic;

import com.jgoodies.plaf.LookUtils;
import java.awt.*;
import javax.swing.*;

// Referenced classes of package dyna.uic:
//            DynaTheme

final class MComboBoxButton extends JButton
{

    MComboBoxButton(JComboBox comboBox, Icon comboIcon, boolean iconOnly, CellRendererPane rendererPane, JList listBox)
    {
        JButton("");
        this.iconOnly = false;
        setModel(new DefaultButtonModel() {

            public void setArmed(boolean armed)
            {
                setArmed(isPressed() || armed);
            }

        });
        this.comboBox = comboBox;
        this.comboIcon = comboIcon;
        this.iconOnly = iconOnly;
        this.rendererPane = rendererPane;
        this.listBox = listBox;
        setEnabled(comboBox.isEnabled());
        setRequestFocusEnabled(comboBox.isEnabled());
        setBorder(UIManager.getBorder("ComboBox.arrowButtonBorder"));
        setMargin(new Insets(0, 2, 0, 3));
        borderPaintsFocus = Boolean.TRUE.equals(UIManager.get("ComboBox.borderPaintsFocus"));
    }

    public JComboBox getComboBox()
    {
        return comboBox;
    }

    public void setComboBox(JComboBox cb)
    {
        comboBox = cb;
    }

    public Icon getComboIcon()
    {
        return comboIcon;
    }

    public void setComboIcon(Icon i)
    {
        comboIcon = i;
    }

    public boolean isIconOnly()
    {
        return iconOnly;
    }

    public void setIconOnly(boolean b)
    {
        iconOnly = b;
    }

    public boolean isFocusTraversable()
    {
        return LookUtils.IS_BEFORE_14 && !comboBox.isEditable() && comboBox.isEnabled();
    }

    public void setEnabled(boolean enabled)
    {
        setEnabled(enabled);
        if(enabled)
        {
            setBackground(comboBox.getBackground());
            setForeground(comboBox.getForeground());
        } else
        {
            setBackground(UIManager.getColor("ComboBox.disabledBackground"));
            setForeground(UIManager.getColor("ComboBox.disabledForeground"));
        }
    }

    public void paintComponent(Graphics g)
    {
        paintComponent(g);
        boolean leftToRight = comboBox.getComponentOrientation().isLeftToRight();
        Insets insets = getInsets();
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        if(height <= 0 || width <= 0)
            return;
        int left = insets.left;
        int top = insets.top;
        int right = left + (width - 1);
        int iconWidth = 0;
        int iconLeft = leftToRight ? right : left;
        if(comboIcon != null)
        {
            iconWidth = comboIcon.getIconWidth();
            int iconHeight = comboIcon.getIconHeight();
            int iconTop;
            if(iconOnly)
            {
                iconLeft = (getWidth() - iconWidth) / 2;
                iconTop = (getHeight() - iconHeight) / 2;
            } else
            {
                if(leftToRight)
                    iconLeft = (left + (width - 1)) - iconWidth;
                else
                    iconLeft = left;
                iconTop = (getHeight() - iconHeight) / 2;
            }
            comboIcon.paintIcon(this, g, iconLeft, iconTop);
        }
        if(!iconOnly && comboBox != null)
        {
            ListCellRenderer renderer = comboBox.getRenderer();
            boolean renderPressed = getModel().isPressed();
            Component c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, renderPressed, false);
            c.setFont(rendererPane.getFont());
            if(model.isArmed() && model.isPressed())
            {
                if(isOpaque())
                    c.setBackground(UIManager.getColor("Button.select"));
                c.setForeground(comboBox.getForeground());
            } else
            if(!comboBox.isEnabled())
            {
                if(isOpaque())
                    c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
                c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            } else
            {
                c.setForeground(comboBox.getForeground());
                c.setBackground(comboBox.getBackground());
            }
            int cWidth = width - (insets.right + iconWidth);
            boolean shouldValidate = c instanceof JPanel;
            int x = leftToRight ? left : left + iconWidth;
            int myHeight = getHeight() - 2 - 3 - (LookUtils.IS_BEFORE_14 ? 2 : 1);
            if(!(c instanceof JComponent))
            {
                rendererPane.paintComponent(g, c, this, x, top + 1, cWidth, myHeight, shouldValidate);
            } else
            {
                JComponent component = (JComponent)c;
                boolean hasBeenOpaque = component.isOpaque();
                component.setOpaque(false);
                rendererPane.paintComponent(g, c, this, x, top + 1, cWidth, myHeight, shouldValidate);
                component.setOpaque(hasBeenOpaque);
            }
        }
        if(comboIcon != null)
        {
            boolean hasFocus = LookUtils.IS_BEFORE_14 ? hasFocus() : comboBox.hasFocus();
            if(!borderPaintsFocus && hasFocus)
            {
                g.setColor(DynaTheme.focusColor);
                int x = 2;
                int y = 2;
                int w = getWidth() - 2 - 3;
                int h = getHeight() - 2 - 3;
                g.drawRect(x, y, w - 1, h - 1);
            }
        }
    }

    private static Color buttonBorderColor = new Color(143, 141, 146);
    private static Color leftShadowColor = new Color(244, 240, 229);
    private static Color rightShadowColor = new Color(215, 207, 186);
    private static final int LEFT_INSET = 2;
    private static final int RIGHT_INSET = 3;
    private final JList listBox;
    private final CellRendererPane rendererPane;
    private JComboBox comboBox;
    private Icon comboIcon;
    protected boolean iconOnly;
    private boolean borderPaintsFocus;

}
