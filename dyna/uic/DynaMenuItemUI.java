// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaMenuItemUI.java

package dyna.uic;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

// Referenced classes of package dyna.uic:
//            MenuItemRenderer

public class DynaMenuItemUI extends BasicMenuItemUI
{

    public DynaMenuItemUI()
    {
    }

    public static ComponentUI createUI(JComponent b)
    {
        return new DynaMenuItemUI();
    }

    protected void installDefaults()
    {
        super.installDefaults();
        renderer = new MenuItemRenderer(menuItem, iconBorderEnabled(), acceleratorFont, selectionForeground, disabledForeground, acceleratorForeground, acceleratorSelectionForeground);
        Integer gap = (Integer)UIManager.get(getPropertyPrefix() + ".textIconGap");
        defaultTextIconGap = gap == null ? 2 : gap.intValue();
    }

    protected boolean iconBorderEnabled()
    {
        return false;
    }

    protected void uninstallDefaults()
    {
        super.uninstallDefaults();
        renderer = null;
    }

    protected Dimension getPreferredMenuItemSize(JComponent c, Icon aCheckIcon, Icon anArrowIcon, int textIconGap)
    {
        Dimension size = renderer.getPreferredMenuItemSize(c, aCheckIcon, anArrowIcon, textIconGap);
        int width = Math.max(80, size.width);
        int height = size.height;
        return new Dimension(width, height);
    }

    protected void paintMenuItem(Graphics g, JComponent c, Icon aCheckIcon, Icon anArrowIcon, Color background, Color foreground, int textIconGap)
    {
        renderer.paintMenuItem(g, c, aCheckIcon, anArrowIcon, background, foreground, textIconGap);
    }

    private static final int MINIMUM_WIDTH = 80;
    private MenuItemRenderer renderer;
}
