// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MButtonUI.java

package dyna.uic;

import com.jgoodies.plaf.LookUtils;
import com.jgoodies.plaf.common.ButtonMarginListener;
import java.awt.*;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

// Referenced classes of package dyna.uic:
//            DynaTheme

public class MButtonUI extends MetalButtonUI
{

    public MButtonUI()
    {
    }

    public static ComponentUI createUI(JComponent b)
    {
        return INSTANCE;
    }

    public void installDefaults(AbstractButton b)
    {
        installDefaults(b);
        LookUtils.installNarrowMargin(b, getPropertyPrefix());
        borderPaintsFocus = Boolean.TRUE.equals(UIManager.get("Button.borderPaintsFocus"));
    }

    public void installListeners(AbstractButton b)
    {
        installListeners(b);
        PropertyChangeListener listener = new ButtonMarginListener(getPropertyPrefix());
        b.putClientProperty("jgoodies.buttonMarginListener", listener);
        b.addPropertyChangeListener("jgoodies.isNarrow", listener);
    }

    public void uninstallListeners(AbstractButton b)
    {
        uninstallListeners(b);
        PropertyChangeListener listener = (PropertyChangeListener)b.getClientProperty("jgoodies.buttonMarginListener");
        b.removePropertyChangeListener(listener);
    }

    public void update(Graphics g, JComponent c)
    {
        AbstractButton b = (AbstractButton)c;
        if(c.isOpaque())
            if(isToolBarButton(b))
                c.setOpaque(false);
            else
            if(b.isContentAreaFilled())
            {
                int x = 2;
                int y = 2;
                int w = c.getWidth() - 4;
                int h = c.getHeight() - 4;
                g.setColor(buttonBackgroundColor);
                g.fillRect(x, y, w, h);
                if(c.isEnabled())
                {
                    Graphics2D g2 = (Graphics2D)g;
                    java.awt.Paint storedPaint = g2.getPaint();
                    g2.setPaint(new GradientPaint(x, y, DynaTheme.BRIGHT_FINISH, x, y + h, DynaTheme.BRIGHT_BEGIN));
                    g2.fillRect(x, y, w, h);
                    x = 0;
                    y = 0;
                    w += 4;
                    h += 4;
                    g.setColor(topShadowColor);
                    g.drawLine(x + 2, y + 1, (x + w) - 3, y + 1);
                    g.setColor(bottomShadowColor);
                    g.drawLine(x + 2, (y + h) - 2, (x + w) - 3, (y + h) - 2);
                    g.setColor(leftShadowColor);
                    g.drawLine(x + 1, y + 2, x + 1, (y + h) - 3);
                    g.setColor(rightShadowColor);
                    g.drawLine((x + w) - 2, y + 2, (x + w) - 2, (y + h) - 3);
                    g2.setPaint(storedPaint);
                    g2 = null;
                } else
                {
                    x = 0;
                    y = 0;
                    w += 4;
                    h += 4;
                    g.drawLine(x + 2, y + 1, (x + w) - 3, y + 1);
                    g.drawLine(x + 2, (y + h) - 2, (x + w) - 3, (y + h) - 2);
                    g.drawLine(x + 1, y + 2, x + 1, (y + h) - 3);
                    g.drawLine((x + w) - 2, y + 2, (x + w) - 2, (y + h) - 3);
                }
            }
        paint(g, c);
    }

    protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect)
    {
        if(borderPaintsFocus)
        {
            return;
        } else
        {
            boolean isDefault = (b instanceof JButton) && ((JButton)b).isDefaultButton();
            int topLeftInset = isDefault ? 3 : 2;
            int width = b.getWidth() - 1 - topLeftInset * 2;
            int height = b.getHeight() - 1 - topLeftInset * 2;
            g.setColor(getFocusColor());
            g.drawRect(topLeftInset, topLeftInset, width - 1, height - 1);
            return;
        }
    }

    protected boolean isToolBarButton(AbstractButton b)
    {
        Container parent = b.getParent();
        return parent != null && ((parent instanceof JToolBar) || (parent.getParent() instanceof JToolBar));
    }

    private static final Color buttonBackgroundColor = new Color(223, 216, 206);
    private static Color topShadowColor = new Color(255, 255, 253);
    private static Color leftShadowColor = new Color(244, 240, 229);
    private static Color rightShadowColor = new Color(215, 207, 186);
    private static Color bottomShadowColor = new Color(211, 206, 174);
    private static final MButtonUI INSTANCE = new MButtonUI();
    private boolean borderPaintsFocus;

}
