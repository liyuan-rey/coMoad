// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MToolBarUI.java

package dyna.uic;

import com.jgoodies.plaf.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.*;

// Referenced classes of package dyna.uic:
//            DynaTheme, MUtils

public final class MToolBarUI extends MetalToolBarUI
{
    private static class RolloverButtonBorder extends javax.swing.plaf.metal.MetalBorders.ButtonBorder
    {

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            AbstractButton b = (AbstractButton)c;
            ButtonModel model = b.getModel();
            if(!model.isEnabled())
                return;
            if(!(c instanceof JToggleButton))
            {
                if(model.isRollover() && (!model.isPressed() || model.isArmed()))
                    super.paintBorder(c, g, x, y, w, h);
                return;
            }
            if(model.isRollover())
            {
                if(model.isPressed() && model.isArmed())
                    MUtils.drawPressed3DBorder(g, x, y, w, h);
                else
                    MUtils.drawFlush3DBorder(g, x, y, w, h);
            } else
            if(model.isSelected())
                MUtils.drawDark3DBorder(g, x, y, w, h);
        }

        public Insets getBorderInsets(Component c)
        {
            return INSETS_3;
        }

        private static final Insets INSETS_3 = new Insets(3, 3, 3, 3);


        RolloverButtonBorder()
        {
        }
    }


    public MToolBarUI()
    {
        myRolloverBorder = createRolloverBorder();
    }

    public static ComponentUI createUI(JComponent b)
    {
        return new MToolBarUI();
    }

    protected Border createRolloverBorder()
    {
        if(myRolloverBorder == null)
            myRolloverBorder = new javax.swing.plaf.BorderUIResource.CompoundBorderUIResource(new RolloverButtonBorder(), new javax.swing.plaf.basic.BasicBorders.MarginBorder());
        return myRolloverBorder;
    }

    protected void setBorderToRollover(Component c)
    {
        if(LookUtils.IS_BEFORE_14)
        {
            setBorderToRollover13(c);
            return;
        }
        if(c instanceof AbstractButton)
            super.setBorderToRollover(c);
        else
        if(c instanceof Container)
        {
            Container cont = (Container)c;
            for(int i = 0; i < cont.getComponentCount(); i++)
                super.setBorderToRollover(cont.getComponent(i));

        }
    }

    private void setBorderToRollover13(Component c)
    {
        if(c instanceof AbstractButton)
        {
            AbstractButton b = (AbstractButton)c;
            Object ui = b.getUI();
            if((ui instanceof MetalButtonUI) || (ui instanceof MetalToggleButtonUI))
                b.setBorder(myRolloverBorder);
            b.setRolloverEnabled(true);
        } else
        if(c instanceof Container)
        {
            Container cont = (Container)c;
            for(int i = 0; i < cont.getComponentCount(); i++)
                setBorderToRollover13(cont.getComponent(i));

        }
    }

    protected void installDefaults()
    {
        super.installDefaults();
        installSpecialBorder();
    }

    protected void installListeners()
    {
        super.installListeners();
        listener = createBorderStyleListener();
        toolBar.addPropertyChangeListener(listener);
    }

    protected void uninstallListeners()
    {
        toolBar.removePropertyChangeListener(listener);
        super.uninstallListeners();
    }

    private PropertyChangeListener createBorderStyleListener()
    {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e)
            {
                String prop = e.getPropertyName();
                if(prop.equals("jgoodies.headerStyle") || prop.equals("Plastic.borderStyle"))
                    installSpecialBorder();
            }

        };
    }

    private void installSpecialBorder()
    {
        BorderStyle borderStyle = BorderStyle.from(toolBar, "Plastic.borderStyle");
        String suffix;
        if(borderStyle == BorderStyle.EMPTY)
            suffix = "emptyBorder";
        else
        if(borderStyle == BorderStyle.ETCHED)
            suffix = "etchedBorder";
        else
        if(borderStyle == BorderStyle.SEPARATOR)
        {
            suffix = "separatorBorder";
        } else
        {
            HeaderStyle headerStyle = HeaderStyle.from(toolBar);
            if(headerStyle == HeaderStyle.BOTH)
                suffix = "headerBorder";
            else
            if(headerStyle == HeaderStyle.SINGLE)
                suffix = "etchedBorder";
            else
                return;
        }
        LookAndFeel.installBorder(toolBar, "ToolBar." + suffix);
    }

    public void update(Graphics g, JComponent c)
    {
        int width = c.getWidth();
        int height = c.getHeight();
        Graphics2D g2 = (Graphics2D)g;
        java.awt.Paint storedPaint = g2.getPaint();
        if(c.isOpaque())
        {
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, width, height);
            g2.setPaint(new GradientPaint(0.0F, 0.0F, DynaTheme.LT_BRIGHT_FINISH, 0.0F, 2.0F, DynaTheme.BRIGHT_BEGIN));
            g2.fillRect(0, 0, width, 2);
            g2.setPaint(new GradientPaint(0.0F, height - 5, DynaTheme.DARK_BEGIN, 0.0F, height, DynaTheme.DARK_FINISH));
            g2.fillRect(0, height - 5, width, 5);
        }
        g2.setPaint(storedPaint);
        g2 = null;
        paint(g, c);
    }

    private static final String PROPERTY_PREFIX = "ToolBar.";
    private Border myRolloverBorder;
    private final Color backgroundColor = new Color(223, 216, 206);
    private PropertyChangeListener listener;

}
