// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MinimumSizedIcon.java

package dyna.uic;

import com.jgoodies.plaf.Options;
import java.awt.*;
import javax.swing.Icon;

public class MinimumSizedIcon
    implements Icon
{

    public MinimumSizedIcon()
    {
        MinimumSizedIcon(null);
    }

    public MinimumSizedIcon(Icon icon)
    {
        Dimension minimumSize = Options.getDefaultIconSize();
        this.icon = icon;
        int iconWidth = icon != null ? icon.getIconWidth() : 0;
        int iconHeight = icon != null ? icon.getIconHeight() : 0;
        width = Math.max(iconWidth, Math.max(20, minimumSize.width));
        height = Math.max(iconHeight, Math.max(20, minimumSize.height));
        xOffset = Math.max(0, (width - iconWidth) / 2);
        yOffset = Math.max(0, (height - iconHeight) / 2);
    }

    public int getIconHeight()
    {
        return height;
    }

    public int getIconWidth()
    {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        if(icon != null)
            icon.paintIcon(c, g, x + xOffset, y + yOffset);
    }

    private final Icon icon;
    private final int width;
    private final int height;
    private final int xOffset;
    private final int yOffset;
}
