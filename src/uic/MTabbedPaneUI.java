// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MTabbedPaneUI.java

package dyna.uic;

import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.plaf.Options;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

// Referenced classes of package dyna.uic:
//            DynaTheme

public final class MTabbedPaneUI extends MetalTabbedPaneUI
{
    public class TabSelectionHandler
        implements ChangeListener
    {

        public void stateChanged(ChangeEvent e)
        {
            JTabbedPane tabbedPane = (JTabbedPane)e.getSource();
            tabbedPane.revalidate();
            tabbedPane.repaint();
        }

        public TabSelectionHandler()
        {
        }
    }

    private static abstract class AbstractRenderer
    {

        private static AbstractRenderer createRenderer(JTabbedPane tabPane)
        {
            switch(tabPane.getTabPlacement())
            {
            case 1: // '\001'
                return new TopRenderer(tabPane);

            case 3: // '\003'
                return new BottomRenderer(tabPane);

            case 2: // '\002'
                return new LeftRenderer(tabPane);

            case 4: // '\004'
                return new RightRenderer(tabPane);
            }
            return new TopRenderer(tabPane);
        }

        private static AbstractRenderer createEmbeddedRenderer(JTabbedPane tabPane)
        {
            switch(tabPane.getTabPlacement())
            {
            case 1: // '\001'
                return new TopEmbeddedRenderer(tabPane);

            case 3: // '\003'
                return new BottomEmbeddedRenderer(tabPane);

            case 2: // '\002'
                return new LeftEmbeddedRenderer(tabPane);

            case 4: // '\004'
                return new RightEmbeddedRenderer(tabPane);
            }
            return new TopEmbeddedRenderer(tabPane);
        }

        private void initColors()
        {
            shadowColor = UIManager.getColor("TabbedPane.shadow");
            darkShadow = UIManager.getColor("TabbedPane.darkShadow");
            selectColor = UIManager.getColor("TabbedPane.selected");
            focus = UIManager.getColor("TabbedPane.focus");
            selectHighlight = UIManager.getColor("TabbedPane.selectHighlight");
            lightHighlight = UIManager.getColor("TabbedPane.highlight");
            selectLight = new Color((2 * selectColor.getRed() + selectHighlight.getRed()) / 3, (2 * selectColor.getGreen() + selectHighlight.getGreen()) / 3, (2 * selectColor.getBlue() + selectHighlight.getBlue()) / 3);
        }

        protected boolean isFirstDisplayedTab(int tabIndex, int position, int paneBorder)
        {
            return tabIndex == 0;
        }

        protected Insets getTabAreaInsets(Insets defaultInsets)
        {
            return defaultInsets;
        }

        protected Insets getContentBorderInsets(Insets defaultInsets)
        {
            return defaultInsets;
        }

        protected int getTabLabelShiftX(int tabIndex, boolean isSelected)
        {
            return 0;
        }

        protected int getTabLabelShiftY(int tabIndex, boolean isSelected)
        {
            return 0;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return tabRunOverlay;
        }

        protected boolean shouldPadTabRun(int run, boolean aPriori)
        {
            return aPriori;
        }

        protected int getTabRunIndent(int run)
        {
            return 0;
        }

        protected abstract Insets getTabInsets(int i, Insets insets);

        protected abstract void paintFocusIndicator(Graphics g, Rectangle arectangle[], int i, Rectangle rectangle, Rectangle rectangle1, boolean flag);

        protected abstract void paintTabBackground(Graphics g, int i, int j, int k, int l, int i1, boolean flag);

        protected abstract void paintTabBorder(Graphics g, int i, int j, int k, int l, int i1, boolean flag);

        protected Insets getSelectedTabPadInsets()
        {
            return EMPTY_INSETS;
        }

        protected void paintContentBorderTopEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            if(isContentBorderPainted)
            {
                g.setColor(MTabbedPaneUI.MARK_CONTENT_BORDERS ? MTabbedPaneUI.MARK_CONTENT_BORDER_COLOR : selectHighlight);
                g.fillRect(x, y, w - 1, 1);
            }
        }

        protected void paintContentBorderBottomEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            if(isContentBorderPainted)
            {
                g.setColor(MTabbedPaneUI.MARK_CONTENT_BORDERS ? MTabbedPaneUI.MARK_CONTENT_BORDER_COLOR : darkShadow);
                g.fillRect(x, (y + h) - 1, w - 1, 1);
            }
        }

        protected void paintContentBorderLeftEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            if(isContentBorderPainted)
            {
                g.setColor(MTabbedPaneUI.MARK_CONTENT_BORDERS ? MTabbedPaneUI.MARK_CONTENT_BORDER_COLOR : selectHighlight);
                g.fillRect(x, y, 1, h - 1);
            }
        }

        protected void paintContentBorderRightEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            if(isContentBorderPainted)
            {
                g.setColor(MTabbedPaneUI.MARK_CONTENT_BORDERS ? MTabbedPaneUI.MARK_CONTENT_BORDER_COLOR : darkShadow);
                g.fillRect((x + w) - 1, y, 1, h);
            }
        }

        protected static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
        protected static final Insets NORTH_INSETS = new Insets(1, 0, 0, 0);
        protected static final Insets WEST_INSETS = new Insets(0, 1, 0, 0);
        protected static final Insets SOUTH_INSETS = new Insets(0, 0, 1, 0);
        protected static final Insets EAST_INSETS = new Insets(0, 0, 0, 1);
        protected final JTabbedPane tabPane;
        protected final int tabPlacement;
        protected Color shadowColor;
        protected Color darkShadow;
        protected Color selectColor;
        protected Color selectLight;
        protected Color selectHighlight;
        protected Color lightHighlight;
        protected Color focus;




        AbstractRenderer(JTabbedPane tabPane)
        {
            initColors();
            this.tabPane = tabPane;
            tabPlacement = tabPane.getTabPlacement();
        }
    }

    private static class BottomEmbeddedRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets insets)
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected Insets getContentBorderInsets(Insets defaultInsets)
        {
            return AbstractRenderer.SOUTH_INSETS;
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left, tabInsets.bottom, tabInsets.right);
        }

        protected void paintFocusIndicator(Graphics g1, Rectangle arectangle[], int i, Rectangle rectangle, Rectangle rectangle1, boolean flag)
        {
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.setColor(selectColor);
            g.fillRect(x, y, w + 1, h);
            if(isSelected)
            {
                Graphics2D g2 = (Graphics2D)g;
                int width = w + 1;
                int height = (int)((float)h * 0.5F);
                java.awt.Paint storedPaint = g2.getPaint();
                g2.setPaint(new GradientPaint(x, y, DynaTheme.BRIGHT_FINISH, x, y + height, DynaTheme.BRIGHT_BEGIN));
                g2.fillRect(x, y, width, height);
                g2.setPaint(new GradientPaint(x, y + height, DynaTheme.DARK_BEGIN, x, y + height + height, DynaTheme.DARK_FINISH));
                g2.fillRect(x, y + height, width, height);
                g2.setPaint(storedPaint);
                g2 = null;
            }
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int bottom = h;
            int right = w + 1;
            g.translate(x, y);
            if(isFirstDisplayedTab(tabIndex, x, tabPane.getBounds().x))
            {
                if(isSelected)
                {
                    g.setColor(shadowColor);
                    g.fillRect(right, 0, 1, bottom - 1);
                    g.fillRect(right - 1, bottom - 1, 1, 1);
                }
            } else
            if(isSelected)
            {
                g.setColor(shadowColor);
                g.fillRect(0, 0, 1, bottom - 1);
                g.fillRect(1, bottom - 1, 1, 1);
                g.fillRect(right, 0, 1, bottom - 1);
                g.fillRect(right - 1, bottom - 1, 1, 1);
            } else
            {
                g.setColor(shadowColor);
                g.fillRect(1, h / 2, 1, h - h / 2);
            }
            g.translate(-x, -y);
        }

        protected void paintContentBorderBottomEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            g.setColor(shadowColor);
            g.fillRect(x, (y + h) - 1, w, 1);
        }

        BottomEmbeddedRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static final class BottomRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets defaultInsets)
        {
            return new Insets(defaultInsets.top, defaultInsets.left + 5, defaultInsets.bottom, defaultInsets.right);
        }

        protected int getTabLabelShiftY(int tabIndex, boolean isSelected)
        {
            return isSelected ? 0 : -1;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return tabRunOverlay - 2;
        }

        protected int getTabRunIndent(int run)
        {
            return 6 * run;
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.SOUTH_INSETS;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left - 2, tabInsets.bottom, tabInsets.right - 2);
        }

        protected void paintFocusIndicator(Graphics g, Rectangle rects[], int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
        {
            if(!tabPane.hasFocus() || !isSelected)
            {
                return;
            } else
            {
                Rectangle tabRect = rects[tabIndex];
                int top = tabRect.y;
                int left = tabRect.x + 6;
                int height = tabRect.height - 3;
                int width = tabRect.width - 12;
                g.setColor(focus);
                g.drawRect(left, top, width, height);
                return;
            }
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.setColor(selectColor);
            g.fillRect(x, y, w, h);
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int bottom = h - 1;
            int right = w + 4;
            g.translate(x - 3, y);
            g.setColor(selectHighlight);
            g.fillRect(0, 0, 1, 2);
            g.drawLine(0, 2, 4, bottom - 4);
            g.fillRect(5, bottom - 3, 1, 2);
            g.fillRect(6, bottom - 1, 1, 1);
            g.fillRect(7, bottom, 1, 1);
            g.setColor(darkShadow);
            g.fillRect(8, bottom, right - 13, 1);
            g.drawLine(right + 1, 0, right - 3, bottom - 4);
            g.fillRect(right - 4, bottom - 3, 1, 2);
            g.fillRect(right - 5, bottom - 1, 1, 1);
            g.translate(-x + 3, -y);
        }

        protected void paintContentBorderBottomEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            int bottom = (y + h) - 1;
            int right = (x + w) - 1;
            g.translate(x, bottom);
            if(drawBroken && selRect.x >= x && selRect.x <= x + w)
            {
                g.setColor(darkShadow);
                g.fillRect(0, 0, selRect.x - x - 2, 1);
                if(selRect.x + selRect.width < (x + w) - 2)
                {
                    g.setColor(darkShadow);
                    g.fillRect((selRect.x + selRect.width + 2) - x, 0, right - selRect.x - selRect.width - 2, 1);
                }
            } else
            {
                g.setColor(darkShadow);
                g.fillRect(0, 0, w - 1, 1);
            }
            g.translate(-x, -bottom);
        }

        BottomRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static class LeftEmbeddedRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets insets)
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected Insets getContentBorderInsets(Insets defaultInsets)
        {
            return AbstractRenderer.WEST_INSETS;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return 0;
        }

        protected boolean shouldPadTabRun(int run, boolean aPriori)
        {
            return false;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left, tabInsets.bottom, tabInsets.right);
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected void paintFocusIndicator(Graphics g1, Rectangle arectangle[], int i, Rectangle rectangle, Rectangle rectangle1, boolean flag)
        {
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.setColor(selectColor);
            g.fillRect(x, y, w, h);
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int bottom = h;
            int right = w;
            g.translate(x, y);
            if(isFirstDisplayedTab(tabIndex, y, tabPane.getBounds().y))
            {
                if(isSelected)
                {
                    g.setColor(selectHighlight);
                    g.fillRect(0, 0, right, 1);
                    g.fillRect(0, 0, 1, bottom - 1);
                    g.fillRect(1, bottom - 1, right - 1, 1);
                    g.setColor(shadowColor);
                    g.fillRect(0, bottom - 1, 1, 1);
                    g.fillRect(1, bottom, right - 1, 1);
                }
            } else
            if(isSelected)
            {
                g.setColor(selectHighlight);
                g.fillRect(1, 1, right - 1, 1);
                g.fillRect(0, 2, 1, bottom - 2);
                g.fillRect(1, bottom - 1, right - 1, 1);
                g.setColor(shadowColor);
                g.fillRect(1, 0, right - 1, 1);
                g.fillRect(0, 1, 1, 1);
                g.fillRect(0, bottom - 1, 1, 1);
                g.fillRect(1, bottom, right - 1, 1);
            } else
            {
                g.setColor(shadowColor);
                g.fillRect(0, 0, right / 3, 1);
            }
            g.translate(-x, -y);
        }

        protected void paintContentBorderLeftEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            g.setColor(shadowColor);
            g.fillRect(x, y, 1, h);
        }

        LeftEmbeddedRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static class LeftRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets defaultInsets)
        {
            return new Insets(defaultInsets.top + 4, defaultInsets.left, defaultInsets.bottom, defaultInsets.right);
        }

        protected int getTabLabelShiftX(int tabIndex, boolean isSelected)
        {
            return 1;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return 1;
        }

        protected boolean shouldPadTabRun(int run, boolean aPriori)
        {
            return false;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left - 5, tabInsets.bottom + 1, tabInsets.right - 5);
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.WEST_INSETS;
        }

        protected void paintFocusIndicator(Graphics g, Rectangle rects[], int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
        {
            if(!tabPane.hasFocus() || !isSelected)
            {
                return;
            } else
            {
                Rectangle tabRect = rects[tabIndex];
                int top = tabRect.y + 2;
                int left = tabRect.x + 3;
                int height = tabRect.height - 5;
                int width = tabRect.width - 6;
                g.setColor(focus);
                g.drawRect(left, top, width, height);
                return;
            }
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            if(!isSelected)
            {
                g.setColor(selectLight);
                g.fillRect(x + 1, y + 1, w - 1, h - 2);
            } else
            {
                g.setColor(selectColor);
                g.fillRect(x + 1, y + 1, w - 3, h - 2);
            }
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int bottom = h - 1;
            int left = 0;
            g.translate(x, y);
            g.setColor(selectHighlight);
            g.fillRect(left + 2, 0, w - 2 - left, 1);
            g.fillRect(left + 1, 1, 1, 1);
            g.fillRect(left, 2, 1, bottom - 3);
            g.setColor(darkShadow);
            g.fillRect(left + 1, bottom - 1, 1, 1);
            g.fillRect(left + 2, bottom, w - 2 - left, 1);
            g.translate(-x, -y);
        }

        protected void paintContentBorderLeftEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            g.setColor(selectHighlight);
            if(drawBroken && selRect.y >= y && selRect.y <= y + h)
            {
                g.fillRect(x, y, 1, (selRect.y + 1) - y);
                if(selRect.y + selRect.height < (y + h) - 2)
                    g.fillRect(x, (selRect.y + selRect.height) - 1, 1, (y + h) - selRect.y - selRect.height);
            } else
            {
                g.fillRect(x, y, 1, h - 1);
            }
        }

        LeftRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static class RightEmbeddedRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets insets)
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected Insets getContentBorderInsets(Insets defaultInsets)
        {
            return AbstractRenderer.EAST_INSETS;
        }

        protected int getTabRunIndent(int run)
        {
            return 4 * run;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return 0;
        }

        protected boolean shouldPadTabRun(int run, boolean aPriori)
        {
            return false;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left, tabInsets.bottom, tabInsets.right);
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected void paintFocusIndicator(Graphics g1, Rectangle arectangle[], int i, Rectangle rectangle, Rectangle rectangle1, boolean flag)
        {
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.setColor(selectColor);
            g.fillRect(x, y, w, h);
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int bottom = h;
            int right = w - 1;
            g.translate(x + 1, y);
            if(isFirstDisplayedTab(tabIndex, y, tabPane.getBounds().y))
            {
                if(isSelected)
                {
                    g.setColor(shadowColor);
                    g.fillRect(right - 1, bottom - 1, 1, 1);
                    g.fillRect(0, bottom, right - 1, 1);
                    g.setColor(selectHighlight);
                    g.fillRect(0, 0, right - 1, 1);
                    g.fillRect(right - 1, 0, 1, bottom - 1);
                    g.fillRect(0, bottom - 1, right - 1, 1);
                }
            } else
            if(isSelected)
            {
                g.setColor(shadowColor);
                g.fillRect(0, -1, right - 1, 1);
                g.fillRect(right - 1, 0, 1, 1);
                g.fillRect(right - 1, bottom - 1, 1, 1);
                g.fillRect(0, bottom, right - 1, 1);
                g.setColor(selectHighlight);
                g.fillRect(0, 0, right - 1, 1);
                g.fillRect(right - 1, 1, 1, bottom - 2);
                g.fillRect(0, bottom - 1, right - 1, 1);
            } else
            {
                g.setColor(shadowColor);
                g.fillRect((2 * right) / 3, 0, right / 3, 1);
            }
            g.translate(-x - 1, -y);
        }

        protected void paintContentBorderRightEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            g.setColor(shadowColor);
            g.fillRect((x + w) - 1, y, 1, h);
        }

        RightEmbeddedRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static class RightRenderer extends AbstractRenderer
    {

        protected int getTabLabelShiftX(int tabIndex, boolean isSelected)
        {
            return 1;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return 1;
        }

        protected boolean shouldPadTabRun(int run, boolean aPriori)
        {
            return false;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left - 5, tabInsets.bottom + 1, tabInsets.right - 5);
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.EAST_INSETS;
        }

        protected void paintFocusIndicator(Graphics g, Rectangle rects[], int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
        {
            if(!tabPane.hasFocus() || !isSelected)
            {
                return;
            } else
            {
                Rectangle tabRect = rects[tabIndex];
                int top = tabRect.y + 2;
                int left = tabRect.x + 3;
                int height = tabRect.height - 5;
                int width = tabRect.width - 6;
                g.setColor(focus);
                g.drawRect(left, top, width, height);
                return;
            }
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            if(!isSelected)
            {
                g.setColor(selectLight);
                g.fillRect(x, y, w, h);
            } else
            {
                g.setColor(selectColor);
                g.fillRect(x + 2, y, w - 2, h);
            }
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int bottom = h - 1;
            int right = w;
            g.translate(x, y);
            g.setColor(selectHighlight);
            g.fillRect(0, 0, right - 1, 1);
            g.setColor(darkShadow);
            g.fillRect(right - 1, 1, 1, 1);
            g.fillRect(right, 2, 1, bottom - 3);
            g.fillRect(right - 1, bottom - 1, 1, 1);
            g.fillRect(0, bottom, right - 1, 1);
            g.translate(-x, -y);
        }

        protected void paintContentBorderRightEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            g.setColor(darkShadow);
            if(drawBroken && selRect.y >= y && selRect.y <= y + h)
            {
                g.fillRect((x + w) - 1, y, 1, selRect.y - y);
                if(selRect.y + selRect.height < (y + h) - 2)
                    g.fillRect((x + w) - 1, selRect.y + selRect.height, 1, (y + h) - selRect.y - selRect.height);
            } else
            {
                g.fillRect((x + w) - 1, y, 1, h - 1);
            }
        }

        RightRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static class TopEmbeddedRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets insets)
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected Insets getContentBorderInsets(Insets defaultInsets)
        {
            return AbstractRenderer.NORTH_INSETS;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top, tabInsets.left + 1, tabInsets.bottom, tabInsets.right);
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.EMPTY_INSETS;
        }

        protected void paintFocusIndicator(Graphics g1, Rectangle arectangle[], int i, Rectangle rectangle, Rectangle rectangle1, boolean flag)
        {
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            if(isSelected)
            {
                Graphics2D g2 = (Graphics2D)g;
                java.awt.Paint storedPaint = g2.getPaint();
                g2.setColor(MTabbedPaneUI.selectedTopTabColor);
                g.fillRect(x, y, w, h);
                g2.setPaint(new GradientPaint(x, y, DynaTheme.BRIGHT_BEGIN, x, y + h, DynaTheme.LT_BRIGHT_FINISH));
                g2.fillRect(x, y, w, h);
                g2.setPaint(storedPaint);
                g2 = null;
            } else
            {
                g.setColor(selectColor);
                g.fillRect(x, y, w, h);
            }
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.translate(x, y);
            int right = w;
            int bottom = h;
            if(isFirstDisplayedTab(tabIndex, x, tabPane.getBounds().x))
            {
                if(isSelected)
                {
                    g.setColor(shadowColor);
                    g.fillRect(right - 1, 0, 1, 1);
                    g.fillRect(right, 1, 1, bottom);
                }
            } else
            if(isSelected)
            {
                g.setColor(shadowColor);
                g.fillRect(0, 1, 1, bottom - 1);
                g.fillRect(1, 0, 1, 1);
                g.fillRect(right - 1, 0, 1, 1);
                g.fillRect(right, 1, 1, bottom);
            } else
            {
                g.setColor(shadowColor);
                g.fillRect(0, 0, 1, (bottom + 2) - bottom / 2);
            }
            g.translate(-x, -y);
        }

        protected void paintContentBorderTopEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            g.setColor(shadowColor);
            g.fillRect(x, y, w, 1);
        }

        TopEmbeddedRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }

    private static class TopRenderer extends AbstractRenderer
    {

        protected Insets getTabAreaInsets(Insets defaultInsets)
        {
            return new Insets(defaultInsets.top, defaultInsets.left + 4, defaultInsets.bottom, defaultInsets.right);
        }

        protected int getTabLabelShiftY(int tabIndex, boolean isSelected)
        {
            return isSelected ? -1 : 0;
        }

        protected int getTabRunOverlay(int tabRunOverlay)
        {
            return tabRunOverlay - 2;
        }

        protected int getTabRunIndent(int run)
        {
            return 6 * run;
        }

        protected Insets getSelectedTabPadInsets()
        {
            return AbstractRenderer.NORTH_INSETS;
        }

        protected Insets getTabInsets(int tabIndex, Insets tabInsets)
        {
            return new Insets(tabInsets.top - 1, tabInsets.left - 4, tabInsets.bottom, tabInsets.right - 4);
        }

        protected void paintFocusIndicator(Graphics g, Rectangle rects[], int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
        {
            if(!tabPane.hasFocus() || !isSelected)
            {
                return;
            } else
            {
                Rectangle tabRect = rects[tabIndex];
                int top = tabRect.y + 1;
                int left = tabRect.x + 4;
                int height = tabRect.height - 3;
                int width = tabRect.width - 9;
                g.setColor(focus);
                g.drawRect(left, top, width, height);
                return;
            }
        }

        protected void paintTabBackground(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            int sel = isSelected ? 0 : 1;
            g.setColor(selectColor);
            g.fillRect(x, y + sel, w, h / 2);
            g.fillRect(x - 1, y + sel + h / 2, w + 2, h - h / 2);
        }

        protected void paintTabBorder(Graphics g, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.translate(x - 4, y);
            int top = 0;
            int right = w + 6;
            g.setColor(selectHighlight);
            g.drawLine(1, h - 1, 4, top + 4);
            g.fillRect(5, top + 2, 1, 2);
            g.fillRect(6, top + 1, 1, 1);
            g.fillRect(7, top, right - 12, 1);
            g.setColor(darkShadow);
            g.drawLine(right, h - 1, right - 3, top + 4);
            g.fillRect(right - 4, top + 2, 1, 2);
            g.fillRect(right - 5, top + 1, 1, 1);
            g.translate(-x + 4, -y);
        }

        protected void paintContentBorderTopEdge(Graphics g, int x, int y, int w, int h, boolean drawBroken, Rectangle selRect, 
                boolean isContentBorderPainted)
        {
            int right = (x + w) - 1;
            int top = y;
            g.setColor(selectHighlight);
            if(drawBroken && selRect.x >= x && selRect.x <= x + w)
            {
                g.fillRect(x, top, selRect.x - 2 - x, 1);
                if(selRect.x + selRect.width < (x + w) - 2)
                    g.fillRect(selRect.x + selRect.width + 2, top, right - 2 - selRect.x - selRect.width, 1);
                else
                    g.fillRect((x + w) - 2, top, 1, 1);
            } else
            {
                g.fillRect(x, top, w - 1, 1);
            }
        }

        TopRenderer(JTabbedPane tabPane)
        {
            AbstractRenderer(tabPane);
        }
    }


    public MTabbedPaneUI()
    {
        clearLookSuggestsNoContentBorder = false;
    }

    public static ComponentUI createUI(JComponent tabPane)
    {
        return new MTabbedPaneUI();
    }

    public void installUI(JComponent c)
    {
        installUI(c);
        embeddedTabs = (Boolean)c.getClientProperty("jgoodies.embeddedTabs");
        noContentBorder = (Boolean)c.getClientProperty("jgoodies.noContentBorder");
        renderer = createRenderer(tabPane);
    }

    public void uninstallUI(JComponent c)
    {
        renderer = null;
        uninstallUI(c);
    }

    private boolean hasNoContentBorder()
    {
        return noContentBorder != null ? noContentBorder.booleanValue() : clearLookSuggestsNoContentBorder();
    }

    private boolean hasEmbeddedTabs()
    {
        return embeddedTabs != null ? embeddedTabs.booleanValue() : false;
    }

    private boolean clearLookSuggestsNoContentBorder()
    {
        return clearLookSuggestsNoContentBorder;
    }

    private AbstractRenderer createRenderer(JTabbedPane tabbedPane)
    {
        return hasEmbeddedTabs() ? AbstractRenderer.createEmbeddedRenderer(tabbedPane) : AbstractRenderer.createRenderer(tabPane);
    }

    private void checkBorderReplacement(JTabbedPane tabbedPane)
    {
        javax.swing.border.Border newBorder = ClearLookManager.replaceBorder(tabbedPane);
        java.awt.Container parent = tabbedPane.getParent();
        if(parent != null && (parent instanceof JSplitPane))
            newBorder = null;
        clearLookSuggestsNoContentBorder = newBorder != null;
    }

    protected PropertyChangeListener createPropertyChangeListener()
    {
        return new MyPropertyChangeHandler();
    }

    protected ChangeListener createChangeListener()
    {
        return new TabSelectionHandler();
    }

    private void doLayout()
    {
        TabbedPaneLayout layout = (TabbedPaneLayout)tabPane.getLayout();
        layout.calculateLayoutInfo();
        tabPane.repaint();
    }

    private void tabPlacementChanged()
    {
        renderer = createRenderer(tabPane);
        doLayout();
    }

    private void embeddedTabsPropertyChanged(Boolean newValue)
    {
        embeddedTabs = newValue;
        renderer = createRenderer(tabPane);
        doLayout();
    }

    private void noContentBorderPropertyChanged(Boolean newValue)
    {
        noContentBorder = newValue;
        tabPane.repaint();
    }

    private void ensureCurrentLayout()
    {
        if(!tabPane.isValid())
            tabPane.validate();
        if(!tabPane.isValid())
        {
            TabbedPaneLayout layout = (TabbedPaneLayout)tabPane.getLayout();
            layout.calculateLayoutInfo();
        }
    }

    public void paint(Graphics g, JComponent c)
    {
        int selectedIndex = tabPane.getSelectedIndex();
        int tabPlacement = tabPane.getTabPlacement();
        int tabCount = tabPane.getTabCount();
        ensureCurrentLayout();
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();
        Rectangle clipRect = g.getClipBounds();
        for(int i = runCount - 1; i >= 0; i--)
        {
            int start = tabRuns[i];
            int next = tabRuns[i != runCount - 1 ? i + 1 : 0];
            int end = next == 0 ? tabCount - 1 : next - 1;
            for(int j = end; j >= start; j--)
                if(rects[j].intersects(clipRect))
                    paintTab(g, tabPlacement, rects, j, iconRect, textRect);

        }

        if(selectedIndex >= 0)
        {
            if(selectedIndex >= rects.length)
                System.out.println("Caution");
            if(rects[selectedIndex].intersects(clipRect))
                paintTab(g, tabPlacement, rects, selectedIndex, iconRect, textRect);
        }
        paintContentBorder(g, tabPlacement, selectedIndex);
    }

    protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect, 
            Rectangle textRect, boolean isSelected)
    {
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
        Rectangle calcRectangle = new Rectangle(tabRect);
        if(isSelected)
        {
            Insets calcInsets = getSelectedTabPadInsets(tabPlacement);
            calcRectangle.x += calcInsets.left;
            calcRectangle.y += calcInsets.top;
            calcRectangle.width -= calcInsets.left + calcInsets.right;
            calcRectangle.height -= calcInsets.bottom + calcInsets.top;
        }
        int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
        if((tabPlacement == 4 || tabPlacement == 2) && icon != null && title != null && !title.equals(""))
        {
            SwingUtilities.layoutCompoundLabel(tabPane, metrics, title, icon, 0, 2, 0, 11, calcRectangle, iconRect, textRect, textIconGap);
            xNudge += 4;
        } else
        {
            SwingUtilities.layoutCompoundLabel(tabPane, metrics, title, icon, 0, 0, 0, 11, calcRectangle, iconRect, textRect, textIconGap);
            iconRect.y += calcRectangle.height % 2;
        }
        iconRect.x += xNudge;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

    protected Icon getIconForTab(int tabIndex)
    {
        String title = tabPane.getTitleAt(tabIndex);
        boolean hasTitle = title != null && title.length() > 0;
        return isTabIconsEnabled || !hasTitle ? super.getIconForTab(tabIndex) : null;
    }

    protected LayoutManager createLayoutManager()
    {
        return new TabbedPaneLayout();
    }

    protected boolean isTabInFirstRun(int tabIndex)
    {
        return getRunForTab(tabPane.getTabCount(), tabIndex) == 0;
    }

    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex)
    {
        int width = tabPane.getWidth();
        int height = tabPane.getHeight();
        Insets insets = tabPane.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;
        switch(tabPlacement)
        {
        case 2: // '\002'
            x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
            w -= x - insets.left;
            break;

        case 4: // '\004'
            w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
            break;

        case 3: // '\003'
            h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            break;

        case 1: // '\001'
        default:
            y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            h -= y - insets.top;
            break;
        }
        g.setColor(selectColor != null ? selectColor : tabPane.getBackground());
        g.fillRect(x, y, w, h);
        Rectangle selRect = selectedIndex >= 0 ? getTabBounds(tabPane, selectedIndex) : null;
        boolean drawBroken = selectedIndex >= 0 && isTabInFirstRun(selectedIndex);
        boolean isContentBorderPainted = !hasNoContentBorder();
        renderer.paintContentBorderTopEdge(g, x, y, w, h, drawBroken, selRect, isContentBorderPainted);
        renderer.paintContentBorderLeftEdge(g, x, y, w, h, drawBroken, selRect, isContentBorderPainted);
        renderer.paintContentBorderBottomEdge(g, x, y, w, h, drawBroken, selRect, isContentBorderPainted);
        renderer.paintContentBorderRightEdge(g, x, y, w, h, drawBroken, selRect, isContentBorderPainted);
    }

    protected Insets getContentBorderInsets(int tabPlacement)
    {
        return renderer.getContentBorderInsets(super.getContentBorderInsets(tabPlacement));
    }

    protected Insets getTabAreaInsets(int tabPlacement)
    {
        return renderer.getTabAreaInsets(super.getTabAreaInsets(tabPlacement));
    }

    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected)
    {
        return renderer.getTabLabelShiftX(tabIndex, isSelected);
    }

    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected)
    {
        return renderer.getTabLabelShiftY(tabIndex, isSelected);
    }

    protected int getTabRunOverlay(int tabPlacement)
    {
        return renderer.getTabRunOverlay(tabRunOverlay);
    }

    protected boolean shouldPadTabRun(int tabPlacement, int run)
    {
        return renderer.shouldPadTabRun(run, super.shouldPadTabRun(tabPlacement, run));
    }

    protected int getTabRunIndent(int tabPlacement, int run)
    {
        return renderer.getTabRunIndent(run);
    }

    protected Insets getTabInsets(int tabPlacement, int tabIndex)
    {
        return renderer.getTabInsets(tabIndex, tabInsets);
    }

    protected Insets getSelectedTabPadInsets(int tabPlacement)
    {
        return renderer.getSelectedTabPadInsets();
    }

    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle rectangles[], int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
    {
        renderer.paintFocusIndicator(g, rectangles, tabIndex, iconRect, textRect, isSelected);
    }

    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, 
            boolean isSelected)
    {
        renderer.paintTabBackground(g, tabIndex, x, y, w, h, isSelected);
    }

    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, 
            boolean isSelected)
    {
        renderer.paintTabBorder(g, tabIndex, x, y, w, h, isSelected);
    }

    protected boolean shouldRotateTabRuns(int tabPlacement)
    {
        return false;
    }

    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, 
            boolean isSelected)
    {
        g.setFont(font);
        if(tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex))
        {
            if(isSelected && tabPlacement == 1)
                g.setColor(Color.white);
            else
                g.setColor(tabPane.getForegroundAt(tabIndex));
            BasicGraphicsUtils.drawString(g, title, 0, textRect.x, textRect.y + metrics.getAscent());
        } else
        {
            g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
            BasicGraphicsUtils.drawString(g, title, 0, textRect.x, textRect.y + metrics.getAscent());
            g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
            BasicGraphicsUtils.drawString(g, title, 0, textRect.x - 1, (textRect.y + metrics.getAscent()) - 1);
        }
    }

    private static Color selectedTopTabColor = UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
    public static final String MARK_CONTENT_BORDERS_KEY = "markContentBorders";
    private static final boolean MARK_CONTENT_BORDERS = System.getProperty("markContentBorders", "").equalsIgnoreCase("true");
    private static boolean isTabIconsEnabled = Options.isTabIconsEnabled();
    private static Color MARK_CONTENT_BORDER_COLOR;
    private Boolean noContentBorder;
    private Boolean embeddedTabs;
    private boolean clearLookSuggestsNoContentBorder;
    private AbstractRenderer renderer;

    static 
    {
        MARK_CONTENT_BORDER_COLOR = Color.magenta;
    }
























}
