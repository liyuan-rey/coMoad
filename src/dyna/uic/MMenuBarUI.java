// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MMenuBarUI.java

package dyna.uic;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;

// Referenced classes of package dyna.uic:
//            DynaTheme

public final class MMenuBarUI extends BasicMenuBarUI
{

    public MMenuBarUI()
    {
    }

    public static ComponentUI createUI(JComponent b)
    {
        return new MMenuBarUI();
    }

    protected void installDefaults()
    {
        installDefaults();
        installSpecialBorder();
    }

    protected void installListeners()
    {
        installListeners();
        listener = createBorderStyleListener();
        menuBar.addPropertyChangeListener(listener);
    }

    protected void uninstallListeners()
    {
        menuBar.removePropertyChangeListener(listener);
        uninstallListeners();
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

    public void installSpecialBorder()
    {
        BorderStyle borderStyle = BorderStyle.from(menuBar, "Plastic.borderStyle");
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
            HeaderStyle headerStyle = HeaderStyle.from(menuBar);
            if(headerStyle == HeaderStyle.BOTH)
                suffix = "headerBorder";
            else
            if(headerStyle == HeaderStyle.SINGLE)
                suffix = "etchedBorder";
            else
                return;
        }
        LookAndFeel.installBorder(menuBar, "MenuBar." + suffix);
    }

    public void update(Graphics g, JComponent c)
    {
        int width = c.getWidth();
        int height = c.getHeight();
        Graphics2D g2 = (Graphics2D)g;
        java.awt.Paint storedPaint = g2.getPaint();
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, width, height);
        g2.setPaint(new GradientPaint(0.0F, 0.0F, DynaTheme.BRIGHT_FINISH, 0.0F, 5F, DynaTheme.BRIGHT_BEGIN));
        g2.fillRect(0, 0, width, 5);
        g2.setPaint(new GradientPaint(0.0F, height - 6, DynaTheme.DARK_BEGIN, 0.0F, height, DynaTheme.LT_DARK_FINISH));
        g2.fillRect(0, height - 6, width, 6);
        g2.setPaint(storedPaint);
        g2 = null;
        paint(g, c);
    }

    private static final Color backgroundColor = new Color(237, 205, 96);
    private PropertyChangeListener listener;

}
