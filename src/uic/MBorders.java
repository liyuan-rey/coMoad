// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MBorders.java

package dyna.uic;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

// Referenced classes of package dyna.uic:
//            MUtils, MComboBoxButton

public class MBorders
{
    private static class MButtonBorder extends AbstractBorder
        implements UIResource
    {

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            AbstractButton button = (AbstractButton)c;
            ButtonModel model = button.getModel();
            if(!model.isEnabled())
            {
                MUtils.drawDisabledButtonBorder(g, x, y, w, h);
                return;
            }
            boolean isPressed = model.isPressed() && model.isArmed();
            boolean isDefault = (button instanceof JButton) && ((JButton)button).isDefaultButton();
            boolean isFocused = button.isFocusPainted() && button.hasFocus();
            if(isPressed)
                MUtils.drawPressedButtonBorder(g, x, y, w, h);
            else
            if(isDefault)
                MUtils.drawDefaultButtonBorder(g, x, y, w, h);
            else
            if(isFocused)
                MUtils.drawFocusedButtonBorder(g, x, y, w, h);
            else
                MUtils.drawPlainButtonBorder(g, x, y, w, h);
        }

        public Insets getBorderInsets(Component c)
        {
            return INSETS;
        }

        public Insets getBorderInsets(Component c, Insets newInsets)
        {
            newInsets.top = INSETS.top;
            newInsets.left = INSETS.left;
            newInsets.bottom = INSETS.bottom;
            newInsets.right = INSETS.right;
            return newInsets;
        }

        protected static final Insets INSETS = new Insets(3, 3, 3, 3);


        MButtonBorder()
        {
        }
    }

    private static class MComboBoxArrowButtonBorder extends AbstractBorder
        implements UIResource
    {

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            MComboBoxButton button = (MComboBoxButton)c;
            JComboBox comboBox = button.getComboBox();
            ButtonModel model = button.getModel();
            if(!model.isEnabled())
            {
                MUtils.drawDisabledButtonBorder(g, x, y, w, h);
                MUtils.drawComboBoxIconSeperatar(g, w - 19, y, h);
            } else
            {
                boolean isPressed = model.isPressed() && model.isArmed();
                boolean isFocused = LookUtils.IS_BEFORE_14 ? button.hasFocus() : comboBox.hasFocus();
                if(isPressed)
                    MUtils.drawPressedButtonBorder(g, x, y, w, h);
                else
                if(isFocused)
                    MUtils.drawFocusedButtonBorder(g, x, y, w, h);
                else
                    MUtils.drawPlainButtonBorder(g, x, y, w, h);
                MUtils.drawComboBoxIconSeperatar(g, w - 19, y, h);
            }
            if(comboBox.isEditable())
            {
                g.setColor(model.isEnabled() ? ((java.awt.Color) (PlasticLookAndFeel.getControlDarkShadow())) : ((java.awt.Color) (MetalLookAndFeel.getControlShadow())));
                g.fillRect(x, y, 1, 1);
                g.fillRect(x, (y + h) - 1, 1, 1);
            }
        }

        public Insets getBorderInsets(Component c)
        {
            return INSETS;
        }

        protected static final Insets INSETS = new Insets(2, 2, 2, 2);


        MComboBoxArrowButtonBorder()
        {
        }
    }


    public MBorders()
    {
    }

    static Border getButtonBorder()
    {
        if(buttonBorder == null)
            buttonBorder = new CompoundBorderUIResource(new MButtonBorder(), new MarginBorder());
        return buttonBorder;
    }

    static Border getComboBoxArrowButtonBorder()
    {
        if(comboBoxArrowButtonBorder == null)
            comboBoxArrowButtonBorder = new CompoundBorderUIResource(new MComboBoxArrowButtonBorder(), new MarginBorder());
        return comboBoxArrowButtonBorder;
    }

    private static Border buttonBorder = null;
    private static Border comboBoxArrowButtonBorder;

}
