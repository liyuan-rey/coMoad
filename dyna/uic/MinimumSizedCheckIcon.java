// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MinimumSizedCheckIcon.java

package dyna.uic;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.*;

// Referenced classes of package dyna.uic:
//            MinimumSizedIcon

public final class MinimumSizedCheckIcon extends MinimumSizedIcon
{

    public MinimumSizedCheckIcon(Icon icon, JMenuItem menuItem)
    {
        MinimumSizedIcon(icon);
        this.menuItem = menuItem;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        paintState(g, x, y);
        paintIcon(c, g, x, y);
    }

    private void paintState(Graphics g, int x, int y)
    {
        ButtonModel model = menuItem.getModel();
        int w = getIconWidth();
        int h = getIconHeight();
        g.translate(x, y);
        if(model.isSelected() || model.isArmed())
        {
            java.awt.Color background = model.isArmed() ? UIManager.getColor("MenuItem.background") : UIManager.getColor("ScrollBar.track");
            java.awt.Color upColor = UIManager.getColor("controlLtHighlight");
            java.awt.Color downColor = UIManager.getColor("controlDkShadow");
            g.setColor(background);
            g.fillRect(0, 0, w, h);
            g.setColor(model.isSelected() ? downColor : upColor);
            g.drawLine(0, 0, w - 2, 0);
            g.drawLine(0, 0, 0, h - 2);
            g.setColor(model.isSelected() ? upColor : downColor);
            g.drawLine(0, h - 1, w - 1, h - 1);
            g.drawLine(w - 1, 0, w - 1, h - 1);
        }
        g.translate(-x, -y);
        g.setColor(UIManager.getColor("textText"));
    }

    private final JMenuItem menuItem;
}
