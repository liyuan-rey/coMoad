// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MenuItemRenderer.java

package dyna.uic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.View;

// Referenced classes of package dyna.uic:
//            MinimumSizedIcon, MinimumSizedCheckIcon, DynaTheme

public final class MenuItemRenderer
{
    private static class NullIcon
        implements Icon
    {

        public int getIconWidth()
        {
            return 0;
        }

        public int getIconHeight()
        {
            return 0;
        }

        public void paintIcon(Component component, Graphics g1, int i, int j)
        {
        }

        NullIcon()
        {
        }
    }


    public MenuItemRenderer(JMenuItem menuItem, boolean iconBorderEnabled, Font acceleratorFont, Color selectionForeground, Color disabledForeground, Color acceleratorForeground, Color acceleratorSelectionForeground)
    {
        this.menuItem = menuItem;
        this.iconBorderEnabled = iconBorderEnabled;
        this.acceleratorFont = acceleratorFont;
        this.selectionForeground = selectionForeground;
        this.disabledForeground = disabledForeground;
        this.acceleratorForeground = acceleratorForeground;
        this.acceleratorSelectionForeground = acceleratorSelectionForeground;
    }

    private Icon getIcon(JMenuItem aMenuItem, Icon defaultIcon)
    {
        Icon icon = aMenuItem.getIcon();
        if(icon == null)
            return defaultIcon;
        ButtonModel model = aMenuItem.getModel();
        if(!model.isEnabled())
            return model.isSelected() ? aMenuItem.getDisabledSelectedIcon() : aMenuItem.getDisabledIcon();
        if(model.isPressed() && model.isArmed())
        {
            Icon pressedIcon = aMenuItem.getPressedIcon();
            return pressedIcon == null ? icon : pressedIcon;
        }
        if(model.isSelected())
        {
            Icon selectedIcon = aMenuItem.getSelectedIcon();
            return selectedIcon == null ? icon : selectedIcon;
        } else
        {
            return icon;
        }
    }

    private boolean hasCustomIcon()
    {
        return getIcon(menuItem, null) != null;
    }

    private Icon getWrappedIcon(Icon icon)
    {
        if(hideIcons())
            return NO_ICON;
        if(icon == null)
            return fillerIcon;
        else
            return ((Icon) (!iconBorderEnabled || !hasCustomIcon() ? new MinimumSizedIcon(icon) : new MinimumSizedCheckIcon(icon, menuItem)));
    }

    private void resetRects()
    {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        acceleratorRect.setBounds(zeroRect);
        checkIconRect.setBounds(zeroRect);
        arrowIconRect.setBounds(zeroRect);
        viewRect.setBounds(0, 0, 32767, 32767);
        r.setBounds(zeroRect);
    }

    public Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap)
    {
        JMenuItem b = (JMenuItem)c;
        String text = b.getText();
        KeyStroke accelerator = b.getAccelerator();
        String acceleratorText = "";
        if(accelerator != null)
        {
            int modifiers = accelerator.getModifiers();
            if(modifiers > 0)
            {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText = acceleratorText + acceleratorDelimiter;
            }
            int keyCode = accelerator.getKeyCode();
            if(keyCode != 0)
                acceleratorText = acceleratorText + KeyEvent.getKeyText(keyCode);
            else
                acceleratorText = acceleratorText + accelerator.getKeyChar();
        }
        Font font = b.getFont();
        FontMetrics fm = b.getFontMetrics(font);
        FontMetrics fmAccel = b.getFontMetrics(acceleratorFont);
        resetRects();
        Icon wrappedIcon = getWrappedIcon(getIcon(menuItem, checkIcon));
        layoutMenuItem(fm, text, fmAccel, acceleratorText, null, wrappedIcon, arrowIcon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, text != null ? defaultTextIconGap : 0, defaultTextIconGap);
        r.setBounds(textRect);
        r = SwingUtilities.computeUnion(iconRect.x, iconRect.y, iconRect.width, iconRect.height, r);
        java.awt.Container parent = menuItem.getParent();
        if(parent != null && (parent instanceof JComponent) && (!(menuItem instanceof JMenu) || !((JMenu)menuItem).isTopLevelMenu()))
        {
            JComponent p = (JComponent)parent;
            Integer maxTextWidth = (Integer)p.getClientProperty("maxTextWidth");
            Integer maxAccWidth = (Integer)p.getClientProperty("maxAccWidth");
            int maxTextValue = maxTextWidth == null ? 0 : maxTextWidth.intValue();
            int maxAccValue = maxAccWidth == null ? 0 : maxAccWidth.intValue();
            if(r.width < maxTextValue)
                r.width = maxTextValue;
            else
                p.putClientProperty("maxTextWidth", new Integer(r.width));
            if(acceleratorRect.width > maxAccValue)
            {
                maxAccValue = acceleratorRect.width;
                p.putClientProperty("maxAccWidth", new Integer(acceleratorRect.width));
            }
            r.width += maxAccValue;
            r.width += defaultTextIconGap;
        }
        if(useCheckAndArrow())
        {
            r.width += checkIconRect.width;
            r.width += defaultTextIconGap;
            r.width += defaultTextIconGap;
            r.width += arrowIconRect.width;
        }
        r.width += 2 * defaultTextIconGap;
        Insets insets = b.getInsets();
        if(insets != null)
        {
            r.width += insets.left + insets.right;
            r.height += insets.top + insets.bottom;
        }
        if(r.height % 2 == 1)
            r.height++;
        return r.getSize();
    }

    public void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap)
    {
        JMenuItem b = (JMenuItem)c;
        ButtonModel model = b.getModel();
        int menuWidth = b.getWidth();
        int menuHeight = b.getHeight();
        Insets i = c.getInsets();
        resetRects();
        viewRect.setBounds(0, 0, menuWidth, menuHeight);
        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= i.right + viewRect.x;
        viewRect.height -= i.bottom + viewRect.y;
        Font holdf = g.getFont();
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        FontMetrics fmAccel = g.getFontMetrics(acceleratorFont);
        KeyStroke accelerator = b.getAccelerator();
        String acceleratorText = "";
        if(accelerator != null)
        {
            int modifiers = accelerator.getModifiers();
            if(modifiers > 0)
            {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText = acceleratorText + acceleratorDelimiter;
            }
            int keyCode = accelerator.getKeyCode();
            if(keyCode != 0)
                acceleratorText = acceleratorText + KeyEvent.getKeyText(keyCode);
            else
                acceleratorText = acceleratorText + accelerator.getKeyChar();
        }
        Icon wrappedIcon = getWrappedIcon(getIcon(menuItem, checkIcon));
        String text = layoutMenuItem(fm, b.getText(), fmAccel, acceleratorText, null, wrappedIcon, arrowIcon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, b.getText() != null ? defaultTextIconGap : 0, defaultTextIconGap);
        paintBackground(g, b, background);
        Color holdc = g.getColor();
        if(model.isArmed() || (c instanceof JMenu) && model.isSelected())
            g.setColor(foreground);
        wrappedIcon.paintIcon(c, g, checkIconRect.x, checkIconRect.y);
        g.setColor(holdc);
        if(text != null)
        {
            View v = (View)c.getClientProperty("html");
            if(v != null)
                v.paint(g, textRect);
            else
                paintText(g, b, textRect, text);
        }
        if(acceleratorText != null && !acceleratorText.equals(""))
        {
            int accOffset = 0;
            java.awt.Container parent = menuItem.getParent();
            if(parent != null && (parent instanceof JComponent))
            {
                JComponent p = (JComponent)parent;
                Integer maxValueInt = (Integer)p.getClientProperty("maxAccWidth");
                int maxValue = maxValueInt == null ? acceleratorRect.width : maxValueInt.intValue();
                accOffset = maxValue - acceleratorRect.width;
            }
            g.setFont(acceleratorFont);
            if(!model.isEnabled())
            {
                if(disabledForeground != null)
                {
                    g.setColor(disabledForeground);
                    BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
                } else
                {
                    g.setColor(b.getBackground().brighter());
                    BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
                    g.setColor(b.getBackground().darker());
                    BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset - 1, (acceleratorRect.y + fmAccel.getAscent()) - 1);
                }
            } else
            {
                if(model.isArmed() || (c instanceof JMenu) && model.isSelected())
                    g.setColor(acceleratorSelectionForeground);
                else
                    g.setColor(acceleratorForeground);
                BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
            }
        }
        if(arrowIcon != null)
        {
            if(model.isArmed() || (c instanceof JMenu) && model.isSelected())
                g.setColor(foreground);
            if(useCheckAndArrow())
                arrowIcon.paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
        }
        g.setColor(holdc);
        g.setFont(holdf);
    }

    private String layoutMenuItem(FontMetrics fm, String text, FontMetrics fmAccel, String acceleratorText, Icon icon, Icon checkIcon, Icon arrowIcon, 
            int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewRectangle, Rectangle iconRectangle, Rectangle textRectangle, 
            Rectangle acceleratorRectangle, Rectangle checkIconRectangle, Rectangle arrowIconRectangle, int textIconGap, int menuItemGap)
    {
        SwingUtilities.layoutCompoundLabel(menuItem, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewRectangle, iconRectangle, textRectangle, textIconGap);
        if(acceleratorText == null || acceleratorText.equals(""))
        {
            acceleratorRectangle.width = acceleratorRectangle.height = 0;
            acceleratorText = "";
        } else
        {
            acceleratorRectangle.width = SwingUtilities.computeStringWidth(fmAccel, acceleratorText);
            acceleratorRectangle.height = fmAccel.getHeight();
        }
        boolean useCheckAndArrow = useCheckAndArrow();
        if(useCheckAndArrow)
        {
            if(checkIcon != null)
            {
                checkIconRectangle.width = checkIcon.getIconWidth();
                checkIconRectangle.height = checkIcon.getIconHeight();
            } else
            {
                checkIconRectangle.width = checkIconRectangle.height = 0;
            }
            if(arrowIcon != null)
            {
                arrowIconRectangle.width = arrowIcon.getIconWidth();
                arrowIconRectangle.height = arrowIcon.getIconHeight();
            } else
            {
                arrowIconRectangle.width = arrowIconRectangle.height = 0;
            }
        }
        Rectangle labelRect = iconRectangle.union(textRectangle);
        if(isLeftToRight(menuItem))
        {
            textRectangle.x += menuItemGap;
            iconRectangle.x += menuItemGap;
            acceleratorRectangle.x = (viewRectangle.x + viewRectangle.width) - arrowIconRectangle.width - menuItemGap - acceleratorRectangle.width;
            if(useCheckAndArrow)
            {
                checkIconRectangle.x = viewRectangle.x;
                textRectangle.x += menuItemGap + checkIconRectangle.width;
                iconRectangle.x += menuItemGap + checkIconRectangle.width;
                arrowIconRectangle.x = (viewRectangle.x + viewRectangle.width) - menuItemGap - arrowIconRectangle.width;
            }
        } else
        {
            textRectangle.x -= menuItemGap;
            iconRectangle.x -= menuItemGap;
            acceleratorRectangle.x = viewRectangle.x + arrowIconRectangle.width + menuItemGap;
            if(useCheckAndArrow)
            {
                checkIconRectangle.x = (viewRectangle.x + viewRectangle.width) - checkIconRectangle.width;
                textRectangle.x -= menuItemGap + checkIconRectangle.width;
                iconRectangle.x -= menuItemGap + checkIconRectangle.width;
                arrowIconRectangle.x = viewRectangle.x + menuItemGap;
            }
        }
        acceleratorRectangle.y = (labelRect.y + labelRect.height / 2) - acceleratorRectangle.height / 2;
        if(useCheckAndArrow)
        {
            arrowIconRectangle.y = (labelRect.y + labelRect.height / 2) - arrowIconRectangle.height / 2;
            checkIconRectangle.y = (labelRect.y + labelRect.height / 2) - checkIconRectangle.height / 2;
        }
        return text;
    }

    private boolean useCheckAndArrow()
    {
        boolean isTopLevelMenu = (menuItem instanceof JMenu) && ((JMenu)menuItem).isTopLevelMenu();
        return !isTopLevelMenu;
    }

    private boolean isLeftToRight(Component c)
    {
        return c.getComponentOrientation().isLeftToRight();
    }

    public void paintBackground(Graphics g, JMenuItem aMenuItem, Color bgColor)
    {
        ButtonModel model = aMenuItem.getModel();
        if(aMenuItem.isOpaque())
        {
            int menuWidth = aMenuItem.getWidth();
            int menuHeight = aMenuItem.getHeight();
            boolean isSelected = model.isArmed() || (aMenuItem instanceof JMenu) && model.isSelected();
            Color oldColor = g.getColor();
            if(isSelected)
            {
                g.setColor(bgColor);
                g.fillRect(0, 0, menuWidth, menuHeight);
            } else
            {
                Graphics2D g2 = (Graphics2D)g;
                java.awt.Paint storedPaint = g2.getPaint();
                g2.setColor(aMenuItem.getBackground());
                g2.fillRect(0, 0, menuWidth, menuHeight);
                if(aMenuItem.isEnabled())
                {
                    Color blueColor = DynaTheme.primary1;
                    g2.setPaint(new GradientPaint(0.0F, 0.0F, DynaTheme.getTransparentColor(blueColor, 64), 10F, 0.0F, DynaTheme.getTransparentColor(blueColor, 128)));
                    g2.fillRect(0, 0, 10, menuHeight);
                    g2.setPaint(new GradientPaint(10F, 0.0F, DynaTheme.getTransparentColor(blueColor, 128), 20F, 0.0F, DynaTheme.getTransparentColor(blueColor, 192)));
                    g2.fillRect(10, 0, 10, menuHeight);
                } else
                {
                    g2.setPaint(new GradientPaint(0.0F, 0.0F, DynaTheme.getTransparentColor(Color.black, 16), 10F, 0.0F, DynaTheme.getTransparentColor(Color.black, 32)));
                    g2.fillRect(0, 0, 10, menuHeight);
                    g2.setPaint(new GradientPaint(10F, 0.0F, DynaTheme.getTransparentColor(Color.black, 32), 20F, 0.0F, DynaTheme.getTransparentColor(Color.black, 64)));
                    g2.fillRect(10, 0, 10, menuHeight);
                }
                g2.setPaint(storedPaint);
                g2 = null;
            }
            g.setColor(oldColor);
        }
    }

    public void paintText(Graphics g, JMenuItem aMenuItem, Rectangle textRectangle, String text)
    {
        ButtonModel model = aMenuItem.getModel();
        FontMetrics fm = g.getFontMetrics();
        int mnemIndex = getDisplayedMnemonicIndex(aMenuItem);
        if(!model.isEnabled())
        {
            if(UIManager.get("MenuItem.disabledForeground") instanceof Color)
            {
                g.setColor(UIManager.getColor("MenuItem.disabledForeground"));
                drawStringUnderlineCharAt(g, text, mnemIndex, textRectangle.x, textRectangle.y + fm.getAscent());
            } else
            {
                g.setColor(aMenuItem.getBackground().brighter());
                drawStringUnderlineCharAt(g, text, mnemIndex, textRectangle.x, textRectangle.y + fm.getAscent());
                g.setColor(aMenuItem.getBackground().darker());
                drawStringUnderlineCharAt(g, text, mnemIndex, textRectangle.x - 1, (textRectangle.y + fm.getAscent()) - 1);
            }
        } else
        {
            if(model.isArmed() || (aMenuItem instanceof JMenu) && model.isSelected())
                g.setColor(selectionForeground);
            drawStringUnderlineCharAt(g, text, mnemIndex, textRectangle.x, textRectangle.y + fm.getAscent());
        }
    }

    public static void drawStringUnderlineCharAt(Graphics g, String text, int underlinedIndex, int x, int y)
    {
        g.drawString(text, x, y);
        if(underlinedIndex >= 0 && underlinedIndex < text.length())
        {
            FontMetrics fm = g.getFontMetrics();
            int underlineRectX = x + fm.stringWidth(text.substring(0, underlinedIndex));
            int underlineRectY = y;
            int underlineRectWidth = fm.charWidth(text.charAt(underlinedIndex));
            int underlineRectHeight = 1;
            g.fillRect(underlineRectX, (underlineRectY + fm.getDescent()) - 1, underlineRectWidth, underlineRectHeight);
        }
    }

    private static int getDisplayedMnemonicIndex(JMenuItem menuItem)
    {
        try
        {
            Method method = javax.swing.AbstractButton.class.getMethod("getDisplayedMnemonicIndex", new Class[0]);
            Integer result = (Integer)method.invoke(menuItem, new Object[0]);
            return result.intValue();
        }
        catch(NoSuchMethodException nosuchmethodexception) { }
        catch(InvocationTargetException invocationtargetexception) { }
        catch(IllegalAccessException illegalaccessexception) { }
        Object value = menuItem.getClientProperty("displayedMnemonicIndex");
        return value == null || !(value instanceof Integer) ? findDisplayedMnemonicIndex(menuItem.getText(), menuItem.getMnemonic()) : ((Integer)value).intValue();
    }

    private static int findDisplayedMnemonicIndex(String text, int mnemonic)
    {
        if(text == null || mnemonic == 0)
            return -1;
        char uc = Character.toUpperCase((char)mnemonic);
        char lc = Character.toLowerCase((char)mnemonic);
        int uci = text.indexOf(uc);
        int lci = text.indexOf(lc);
        if(uci == -1)
            return lci;
        if(lci == -1)
            return uci;
        else
            return lci >= uci ? uci : lci;
    }

    private boolean hideIcons()
    {
        Component parent = menuItem.getParent();
        if(!(parent instanceof JPopupMenu))
            return false;
        JPopupMenu popupMenu = (JPopupMenu)parent;
        Object value = popupMenu.getClientProperty("jgoodies.noIcons");
        if(value == null)
        {
            Component invoker = popupMenu.getInvoker();
            if(invoker != null && (invoker instanceof JMenu))
                value = ((JMenu)invoker).getClientProperty("jgoodies.noIcons");
        }
        return Boolean.TRUE.equals(value);
    }

    protected static final String HTML_KEY = "html";
    private static final String MAX_TEXT_WIDTH = "maxTextWidth";
    private static final String MAX_ACC_WIDTH = "maxAccWidth";
    private static final Icon NO_ICON = new NullIcon();
    static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);
    static Rectangle iconRect = new Rectangle();
    static Rectangle textRect = new Rectangle();
    static Rectangle acceleratorRect = new Rectangle();
    static Rectangle checkIconRect = new Rectangle();
    static Rectangle arrowIconRect = new Rectangle();
    static Rectangle viewRect = new Rectangle(32767, 32767);
    static Rectangle r = new Rectangle();
    private final JMenuItem menuItem;
    private final boolean iconBorderEnabled;
    private final Font acceleratorFont;
    private final Color selectionForeground;
    private final Color disabledForeground;
    private final Color acceleratorForeground;
    private final Color acceleratorSelectionForeground;
    private final String acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
    private final Icon fillerIcon = new MinimumSizedIcon();

}
