// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaMenuUI.java

package dyna.uic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuUI;

// Referenced classes of package dyna.uic:
//            MenuItemRenderer

public final class DynaMenuUI extends BasicMenuUI
{

    public DynaMenuUI()
    {
        propertyPrefix = "Menu";
    }

    public static ComponentUI createUI(JComponent b)
    {
        return new DynaMenuUI();
    }

    protected void installDefaults()
    {
        super.installDefaults();
        if(arrowIcon == null || (arrowIcon instanceof UIResource))
            arrowIcon = UIManager.getIcon("Menu.arrowIcon");
        renderer = new MenuItemRenderer(menuItem, false, acceleratorFont, selectionForeground, disabledForeground, acceleratorForeground, acceleratorSelectionForeground);
        Integer gap = (Integer)UIManager.get(getPropertyPrefix() + ".textIconGap");
        defaultTextIconGap = gap == null ? 2 : gap.intValue();
    }

    protected void uninstallDefaults()
    {
        super.uninstallDefaults();
        renderer = null;
    }

    protected String getPropertyPrefix()
    {
        return propertyPrefix;
    }

    protected Dimension getPreferredMenuItemSize(JComponent c, Icon aCheckIcon, Icon anArrowIcon, int textIconGap)
    {
        if(isSubMenu(menuItem))
        {
            ensureSubMenuInstalled();
            return renderer.getPreferredMenuItemSize(c, aCheckIcon, anArrowIcon, textIconGap);
        } else
        {
            return super.getPreferredMenuItemSize(c, aCheckIcon, anArrowIcon, textIconGap);
        }
    }

    protected void paintMenuItem(Graphics g, JComponent c, Icon aCheckIcon, Icon anArrowIcon, Color background, Color foreground, int textIconGap)
    {
        JMenuItem b = (JMenuItem)c;
        if(((JMenu)menuItem).isTopLevelMenu())
        {
            b.setOpaque(false);
            if(b.getModel().isSelected())
            {
                int menuWidth = menuItem.getWidth();
                int menuHeight = menuItem.getHeight();
                Color oldColor = g.getColor();
                g.setColor(background);
                g.fillRect(0, 0, menuWidth, menuHeight);
                g.setColor(oldColor);
            }
        }
        renderer.paintMenuItem(g, c, aCheckIcon, anArrowIcon, background, foreground, textIconGap);
    }

    private void ensureSubMenuInstalled()
    {
        if(propertyPrefix.equals("MenuItem"))
        {
            return;
        } else
        {
            uninstallRolloverListener();
            uninstallDefaults();
            propertyPrefix = "MenuItem";
            installDefaults();
            return;
        }
    }

    protected void installListeners()
    {
        super.installListeners();
        mouseListener = createRolloverListener();
        menuItem.addMouseListener(mouseListener);
    }

    protected void uninstallListeners()
    {
        super.uninstallListeners();
        uninstallRolloverListener();
    }

    private void uninstallRolloverListener()
    {
        if(mouseListener != null)
        {
            menuItem.removeMouseListener(mouseListener);
            mouseListener = null;
        }
    }

    protected MouseListener createRolloverListener()
    {
        return new MouseAdapter() {

            public void mouseEntered(MouseEvent e)
            {
                AbstractButton b = (AbstractButton)e.getSource();
                b.getModel().setRollover(true);
            }

            public void mouseExited(MouseEvent e)
            {
                AbstractButton b = (AbstractButton)e.getSource();
                b.getModel().setRollover(false);
            }

        };
    }

    private boolean isSubMenu(JMenuItem aMenuItem)
    {
        return !((JMenu)aMenuItem).isTopLevelMenu();
    }

    private static final String MENU_PROPERTY_PREFIX = "Menu";
    private static final String SUBMENU_PROPERTY_PREFIX = "MenuItem";
    private String propertyPrefix;
    private MenuItemRenderer renderer;
    private MouseListener mouseListener;
}
