// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MUtils.java

package dyna.uic;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import java.awt.*;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

// Referenced classes of package dyna.uic:
//            DynaTheme

public class MUtils
{
    public static class ComboBoxButtonIcon
        implements Icon, Serializable
    {

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            JComponent component = (JComponent)c;
            int iconWidth = getIconWidth();
            g.translate(x, y);
            g.setColor(component.isEnabled() ? ((Color) (MetalLookAndFeel.getControlInfo())) : ((Color) (MetalLookAndFeel.getControlShadow())));
            g.drawLine(0, 0, iconWidth - 1, 0);
            g.drawLine(1, 1, 1 + (iconWidth - 3), 1);
            g.drawLine(2, 2, 2 + (iconWidth - 5), 2);
            g.drawLine(3, 3, 3 + (iconWidth - 7), 3);
            g.translate(-x, -y);
        }

        public int getIconWidth()
        {
            return 7;
        }

        public int getIconHeight()
        {
            return 4;
        }

        public ComboBoxButtonIcon()
        {
        }
    }


    public MUtils()
    {
    }

    public static void replaceComponentOfPaneWithSplit(RootPaneContainer pane, JComponent comp, JSplitPane newSplit, int n)
    {
        if(pane == null || comp == null || newSplit == null || n < 0 || n > 1)
            return;
        Component tmpComp = pane.getContentPane().getComponent(pane.getContentPane().getComponentCount() - 1);
        pane.getContentPane().remove(pane.getContentPane().getComponentCount() - 1);
        pane.getContentPane().add(newSplit, "Center");
        if(newSplit.getOrientation() == 1)
        {
            if(n == 0)
            {
                if(newSplit != comp)
                    newSplit.setLeftComponent(comp);
                if(newSplit != tmpComp)
                    newSplit.setRightComponent(tmpComp);
            } else
            {
                if(newSplit != tmpComp)
                    newSplit.setLeftComponent(tmpComp);
                if(newSplit != comp)
                    newSplit.setRightComponent(comp);
            }
        } else
        if(n == 0)
        {
            if(newSplit != comp)
                newSplit.setTopComponent(comp);
            if(newSplit != tmpComp)
                newSplit.setBottomComponent(tmpComp);
        } else
        {
            if(newSplit != tmpComp)
                newSplit.setTopComponent(tmpComp);
            if(newSplit != comp)
                newSplit.setBottomComponent(comp);
        }
        tmpComp = null;
    }

    public static void replaceComponentOfSplitWithSplit(JSplitPane originSplit, int x, JComponent comp, JSplitPane newSplit, int n)
    {
        if(originSplit == null || comp == null || newSplit == null || n < 0 || n > 1 || x < 0 || x > 1)
            return;
        Component tmpComp = null;
        if(originSplit.getOrientation() == 1)
        {
            if(x == 0)
            {
                tmpComp = originSplit.getLeftComponent();
                originSplit.setLeftComponent(null);
                originSplit.setLeftComponent(newSplit);
            } else
            {
                tmpComp = originSplit.getRightComponent();
                originSplit.setRightComponent(null);
                originSplit.setRightComponent(newSplit);
            }
        } else
        if(x == 0)
        {
            tmpComp = originSplit.getTopComponent();
            originSplit.setTopComponent(null);
            originSplit.setTopComponent(newSplit);
        } else
        {
            tmpComp = originSplit.getBottomComponent();
            originSplit.setBottomComponent(null);
            originSplit.setBottomComponent(newSplit);
        }
        if(newSplit.getOrientation() == 1)
        {
            if(n == 0)
            {
                if(newSplit != comp)
                    newSplit.setLeftComponent(comp);
                if(newSplit != tmpComp)
                    newSplit.setRightComponent(tmpComp);
            } else
            {
                if(newSplit != tmpComp)
                    newSplit.setLeftComponent(tmpComp);
                if(newSplit != comp)
                    newSplit.setRightComponent(comp);
            }
        } else
        if(n == 0)
        {
            if(newSplit != comp)
                newSplit.setTopComponent(comp);
            if(newSplit != tmpComp)
                newSplit.setBottomComponent(tmpComp);
        } else
        {
            if(newSplit != tmpComp)
                newSplit.setTopComponent(tmpComp);
            if(newSplit != comp)
                newSplit.setBottomComponent(comp);
        }
        tmpComp = null;
    }

    public static void replaceComponentOfContainerWithSplit(Container container, JComponent comp, JSplitPane newSplit, int n)
    {
        if(container == null || comp == null || newSplit == null || n < 0 || n > 1)
            return;
        Component tmpComp = container.getComponent(container.getComponentCount() - 1);
        container.remove(container.getComponentCount() - 1);
        container.add(newSplit, "Center");
        if(newSplit.getOrientation() == 1)
        {
            if(n == 0)
            {
                if(newSplit != comp)
                    newSplit.setLeftComponent(comp);
                if(newSplit != tmpComp)
                    newSplit.setRightComponent(tmpComp);
            } else
            {
                if(newSplit != tmpComp)
                    newSplit.setLeftComponent(tmpComp);
                if(newSplit != comp)
                    newSplit.setRightComponent(comp);
            }
        } else
        if(n == 0)
        {
            if(newSplit != comp)
                newSplit.setTopComponent(comp);
            if(newSplit != tmpComp)
                newSplit.setBottomComponent(tmpComp);
        } else
        {
            if(newSplit != tmpComp)
                newSplit.setTopComponent(tmpComp);
            if(newSplit != comp)
                newSplit.setBottomComponent(comp);
        }
        tmpComp = null;
    }

    public static void removeSplitComponentFromContainer(Container container, JSplitPane split, int n)
    {
        if(container == null || split == null || n < 0 || n > 1)
            return;
        Component tmpComp = null;
        if(split.getOrientation() == 1)
        {
            if(n == 1)
                tmpComp = split.getLeftComponent();
            else
                tmpComp = split.getRightComponent();
            split.setLeftComponent(null);
            split.setRightComponent(null);
        } else
        {
            if(n == 1)
                tmpComp = split.getTopComponent();
            else
                tmpComp = split.getBottomComponent();
            split.setTopComponent(null);
            split.setBottomComponent(null);
        }
        if(container instanceof JSplitPane)
        {
            JSplitPane parentSplit = (JSplitPane)container;
            if(parentSplit.getOrientation() == 1)
            {
                if(split == parentSplit.getLeftComponent())
                {
                    parentSplit.setLeftComponent(null);
                    parentSplit.setLeftComponent(tmpComp);
                } else
                if(split == parentSplit.getRightComponent())
                {
                    parentSplit.setRightComponent(null);
                    parentSplit.setRightComponent(tmpComp);
                }
            } else
            if(split == parentSplit.getTopComponent())
            {
                parentSplit.setTopComponent(null);
                parentSplit.setTopComponent(tmpComp);
            } else
            if(split == parentSplit.getRightComponent())
            {
                parentSplit.setBottomComponent(null);
                parentSplit.setBottomComponent(tmpComp);
            }
            parentSplit = null;
        } else
        {
            container.remove(split);
            container.add(tmpComp);
        }
        tmpComp = null;
    }

    static void drawDisabledButtonBorder(Graphics g, int x, int y, int w, int h)
    {
        drawPlainButtonBorder(g, x, y, w, h);
    }

    static void drawRect(Graphics g, int x, int y, int w, int h)
    {
        g.fillRect(x, y, w + 1, 1);
        g.fillRect(x, y + 1, 1, h);
        g.fillRect(x + 1, y + h, w, 1);
        g.fillRect(x + w, y + 1, 1, h);
    }

    static void drawPressedButtonBorder(Graphics g, int x, int y, int w, int h)
    {
        drawPlainButtonBorder(g, x, y, w, h);
        Color darkColor = translucentColor(PlasticLookAndFeel.getControlDarkShadow(), 128);
        Color lightColor = translucentColor(PlasticLookAndFeel.getControlHighlight(), 80);
        g.translate(x, y);
        g.setColor(darkColor);
        g.fillRect(2, 1, w - 4, 1);
        g.setColor(lightColor);
        g.fillRect(2, h - 2, w - 4, 1);
        g.translate(-x, -y);
    }

    static void drawPlainButtonBorder(Graphics g, int x, int y, int w, int h)
    {
        g.setColor(buttonBorderColor);
        g.drawLine(x + 2, y, (x + w) - 3, y);
        g.drawLine(x + 2, (y + h) - 1, (x + w) - 3, (y + h) - 1);
        g.drawLine(x, y + 2, x, (y + h) - 3);
        g.drawLine((x + w) - 1, y + 2, (x + w) - 1, (y + h) - 3);
        g.fillRect(x + 1, y + 1, 1, 1);
        g.fillRect(x + 1, (y + h) - 2, 1, 1);
        g.fillRect((x + w) - 2, y + 1, 1, 1);
        g.fillRect((x + w) - 2, (y + h) - 2, 1, 1);
        g.setColor(buttonBorderCornerColor);
        g.fillRect(x + 1, y, 1, 1);
        g.fillRect(x, y + 1, 1, 1);
        g.fillRect(x + 1, (y + h) - 1, 1, 1);
        g.fillRect(x, (y + h) - 2, 1, 1);
        g.fillRect((x + w) - 2, y, 1, 1);
        g.fillRect((x + w) - 1, y + 1, 1, 1);
        g.fillRect((x + w) - 2, (y + h) - 1, 1, 1);
        g.fillRect((x + w) - 1, (y + h) - 2, 1, 1);
    }

    private static Color translucentColor(Color baseColor, int alpha)
    {
        return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
    }

    static void drawDefaultButtonBorder(Graphics g, int x, int y, int w, int h)
    {
        drawPlainButtonBorder(g, x, y, w, h);
        drawInnerButtonDecoration(g, x, y, w, h, PlasticLookAndFeel.getPrimaryControlDarkShadow());
    }

    private static void drawInnerButtonDecoration(Graphics g, int x, int y, int w, int h, Color baseColor)
    {
        Color lightColor = translucentColor(baseColor, 90);
        Color mediumColor = translucentColor(baseColor, 120);
        Color darkColor = translucentColor(baseColor, 200);
        g.translate(x, y);
        g.setColor(lightColor);
        g.fillRect(2, 1, w - 4, 1);
        g.setColor(mediumColor);
        g.fillRect(1, 2, 1, h - 4);
        g.fillRect(w - 2, 2, 1, h - 4);
        drawRect(g, 2, 2, w - 5, h - 5);
        g.setColor(darkColor);
        g.fillRect(2, h - 2, w - 4, 1);
        g.translate(-x, -y);
    }

    static void drawFocusedButtonBorder(Graphics g, int x, int y, int w, int h)
    {
        drawPlainButtonBorder(g, x, y, w, h);
        drawInnerButtonDecoration(g, x, y, w, h, PlasticLookAndFeel.getFocusColor());
    }

    static void drawComboBoxIconSeperatar(Graphics g, int x, int y, int h)
    {
        g.setColor(rightShadowColor);
        g.drawLine(x, y + 1, x, h - 2);
        g.setColor(buttonBorderColor);
        g.drawLine(x + 1, y, x + 1, h);
        g.setColor(leftShadowColor);
        g.drawLine(x + 2, y + 1, x + 2, h - 2);
    }

    static void drawDisabledBorder(Graphics g, int x, int y, int w, int h)
    {
        g.setColor(MetalLookAndFeel.getControlShadow());
        drawRect(g, x, y, w - 1, h - 1);
    }

    static void addLight3DEffect(Graphics g, Rectangle r, boolean isHorizontal)
    {
        Color ltBrightenStop = UIManager.getColor("Plastic.ltBrightenStop");
        if(ltBrightenStop == null)
            ltBrightenStop = DynaTheme.LT_BRIGHTEN_STOP;
        add3DEffect(g, r, isHorizontal, DynaTheme.BRIGHTEN_START, ltBrightenStop, DynaTheme.DARKEN_START, DynaTheme.LT_DARKEN_STOP);
    }

    private static void add3DEffect(Graphics g, Rectangle r, boolean isHorizontal, Color startC0, Color stopC0, Color startC1, Color stopC1)
    {
        Graphics2D g2 = (Graphics2D)g;
        int xb0;
        int yb0;
        int xb1;
        int yb1;
        int xd0;
        int yd0;
        int xd1;
        int yd1;
        int width;
        int height;
        if(isHorizontal)
        {
            width = r.width;
            height = (int)((float)r.height * 0.5F);
            xb0 = r.x;
            yb0 = r.y;
            xb1 = xb0;
            yb1 = yb0 + height;
            xd0 = xb1;
            yd0 = yb1;
            xd1 = xd0;
            yd1 = r.y + r.height;
        } else
        {
            width = (int)((float)r.width * 0.5F);
            height = r.height;
            xb0 = r.x;
            yb0 = r.y;
            xb1 = xb0 + width;
            yb1 = yb0;
            xd0 = xb1;
            yd0 = yb0;
            xd1 = r.x + r.width;
            yd1 = yd0;
        }
        g2.setPaint(new GradientPaint(xb0, yb0, stopC0, xb1, yb1, startC0));
        g2.fillRect(r.x, r.y, width, height);
        g2.setPaint(new GradientPaint(xd0, yd0, startC1, xd1, yd1, stopC1));
        g2.fillRect(xd0, yd0, width, height);
    }

    static void drawPressed3DBorder(Graphics g, int x, int y, int w, int h)
    {
        g.translate(x, y);
        drawFlush3DBorder(g, 0, 0, w, h);
        g.setColor(MetalLookAndFeel.getControlShadow());
        g.drawLine(1, 1, 1, h - 3);
        g.drawLine(1, 1, w - 3, 1);
        g.translate(-x, -y);
    }

    static void drawFlush3DBorder(Graphics g, int x, int y, int w, int h)
    {
        g.translate(x, y);
        g.setColor(PlasticLookAndFeel.getControlHighlight());
        g.drawRect(1, 1, w - 2, h - 2);
        g.drawLine(0, h - 1, 0, h - 1);
        g.drawLine(w - 1, 0, w - 1, 0);
        g.setColor(PlasticLookAndFeel.getControlDarkShadow());
        g.drawRect(0, 0, w - 2, h - 2);
        g.translate(-x, -y);
    }

    static void drawDark3DBorder(Graphics g, int x, int y, int w, int h)
    {
        drawFlush3DBorder(g, x, y, w, h);
        g.setColor(PlasticLookAndFeel.getControl());
        g.drawLine(x + 1, y + 1, 1, h - 3);
        g.drawLine(y + 1, y + 1, w - 3, 1);
    }

    private static Color buttonBorderColor = new Color(143, 141, 146);
    private static Color buttonBorderCornerColor = new Color(189, 189, 189);
    private static Color leftShadowColor = new Color(244, 240, 229);
    private static Color rightShadowColor = new Color(215, 207, 186);

}
