// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MScrollBarUI.java

package dyna.uic;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;

// Referenced classes of package dyna.uic:
//            MArrowButton, MUtils, DynaTheme

public final class MScrollBarUI extends MetalScrollBarUI
{
    final class MBumps
        implements Icon
    {

        private BumpBuffer getBuffer(GraphicsConfiguration gc, Color aTopColor, Color aShadowColor, Color aBackColor)
        {
            if(buffer != null && buffer.hasSameConfiguration(gc, aTopColor, aShadowColor, aBackColor))
                return buffer;
            BumpBuffer result = null;
            for(Iterator elements = buffers.iterator(); elements.hasNext();)
            {
                BumpBuffer aBuffer = (BumpBuffer)elements.next();
                if(aBuffer.hasSameConfiguration(gc, aTopColor, aShadowColor, aBackColor))
                {
                    result = aBuffer;
                    break;
                }
            }

            if(result == null)
            {
                result = new BumpBuffer(gc, topColor, shadowColor, backColor);
                buffers.add(result);
            }
            return result;
        }

        public void setBumpArea(Dimension bumpArea)
        {
            setBumpArea(bumpArea.width, bumpArea.height);
        }

        public void setBumpArea(int width, int height)
        {
            xBumps = width / 2;
            yBumps = height / 2;
        }

        public void setBumpColors(Color newTopColor, Color newShadowColor, Color newBackColor)
        {
            topColor = newTopColor;
            shadowColor = newShadowColor;
            backColor = newBackColor;
        }

        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            GraphicsConfiguration gc = (g instanceof Graphics2D) ? ((Graphics2D)g).getDeviceConfiguration() : null;
            buffer = getBuffer(gc, topColor, shadowColor, backColor);
            int bufferWidth = buffer.getImageSize().width;
            int bufferHeight = buffer.getImageSize().height;
            int iconWidth = getIconWidth();
            int iconHeight = getIconHeight();
            int x2 = x + iconWidth;
            int y2 = y + iconHeight;
            int savex = x;
            for(; y < y2; y += bufferHeight)
            {
                int h = Math.min(y2 - y, bufferHeight);
                for(x = savex; x < x2; x += bufferWidth)
                {
                    int w = Math.min(x2 - x, bufferWidth);
                    g.drawImage(buffer.getImage(), x, y, x + w, y + h, 0, 0, w, h, null);
                }

            }

        }

        public int getIconWidth()
        {
            return xBumps * 2;
        }

        public int getIconHeight()
        {
            return yBumps * 2;
        }

        protected int xBumps;
        protected int yBumps;
        protected Color topColor;
        protected Color shadowColor;
        protected Color backColor;
        protected ArrayList buffers;
        protected BumpBuffer buffer;

        public MBumps(Dimension bumpArea)
        {
            MBumps(bumpArea.width, bumpArea.height);
        }

        public MBumps(int width, int height)
        {
            MBumps(width, height, ((Color) (PlasticLookAndFeel.getPrimaryControlHighlight())), ((Color) (PlasticLookAndFeel.getPrimaryControlDarkShadow())), ((Color) (PlasticLookAndFeel.getPrimaryControlShadow())));
        }

        public MBumps(int width, int height, Color newTopColor, Color newShadowColor, Color newBackColor)
        {
            buffers = new ArrayList();
            setBumpArea(width, height);
            setBumpColors(newTopColor, newShadowColor, newBackColor);
        }
    }

    final class BumpBuffer
    {

        public boolean hasSameConfiguration(GraphicsConfiguration aGC, Color aTopColor, Color aShadowColor, Color aBackColor)
        {
            if(gc != null)
            {
                if(!gc.equals(aGC))
                    return false;
            } else
            if(aGC != null)
                return false;
            return topColor.equals(aTopColor) && shadowColor.equals(aShadowColor) && backColor.equals(aBackColor);
        }

        public Image getImage()
        {
            return image;
        }

        public Dimension getImageSize()
        {
            return imageSize;
        }

        private void fillBumpBuffer()
        {
            Graphics g = image.getGraphics();
            g.setColor(backColor);
            g.fillRect(0, 0, 64, 64);
            g.setColor(topColor);
            for(int x = 0; x < 64; x += 4)
            {
                for(int y = 0; y < 64; y += 4)
                {
                    g.drawLine(x, y, x, y);
                    g.drawLine(x + 2, y + 2, x + 2, y + 2);
                }

            }

            g.setColor(shadowColor);
            for(int x = 0; x < 64; x += 4)
            {
                for(int y = 0; y < 64; y += 4)
                {
                    g.drawLine(x + 1, y + 1, x + 1, y + 1);
                    g.drawLine(x + 3, y + 3, x + 3, y + 3);
                }

            }

            g.dispose();
        }

        private void createImage()
        {
            if(gc != null)
            {
                image = gc.createCompatibleImage(64, 64);
            } else
            {
                int cmap[] = {
                    backColor.getRGB(), topColor.getRGB(), shadowColor.getRGB()
                };
                IndexColorModel icm = new IndexColorModel(8, 3, cmap, 0, false, -1, 0);
                image = new BufferedImage(64, 64, 13, icm);
            }
        }

        static final int IMAGE_SIZE = 64;
        Dimension imageSize;
        transient Image image;
        Color topColor;
        Color shadowColor;
        Color backColor;
        private GraphicsConfiguration gc;

        public BumpBuffer(GraphicsConfiguration gc, Color aTopColor, Color aShadowColor, Color aBackColor)
        {
            imageSize = new Dimension(64, 64);
            this.gc = gc;
            topColor = aTopColor;
            shadowColor = aShadowColor;
            backColor = aBackColor;
            createImage();
            fillBumpBuffer();
        }
    }


    public MScrollBarUI()
    {
    }

    public static ComponentUI createUI(JComponent b)
    {
        return new MScrollBarUI();
    }

    protected void installDefaults()
    {
        installDefaults();
        bumps = new MBumps(10, 10, thumbHighlightColor, thumbShadow, thumbColor);
    }

    protected JButton createDecreaseButton(int orientation)
    {
        decreaseButton = new MArrowButton(orientation, scrollBarWidth, isFreeStanding);
        return decreaseButton;
    }

    protected JButton createIncreaseButton(int orientation)
    {
        increaseButton = new MArrowButton(orientation, scrollBarWidth, isFreeStanding);
        return increaseButton;
    }

    protected void configureScrollBarColors()
    {
        configureScrollBarColors();
        shadowColor = UIManager.getColor("ScrollBar.shadow");
        highlightColor = UIManager.getColor("ScrollBar.highlight");
        darkShadowColor = UIManager.getColor("ScrollBar.darkShadow");
        thumbColor = UIManager.getColor("ScrollBar.thumb");
        thumbShadow = UIManager.getColor("ScrollBar.thumbShadow");
        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
        lightShadowColor = new Color(201, 199, 187);
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
    {
        g.translate(trackBounds.x, trackBounds.y);
        boolean leftToRight = c.getComponentOrientation().isLeftToRight();
        if(scrollbar.getOrientation() == 1)
        {
            if(!isFreeStanding)
                if(!leftToRight)
                {
                    trackBounds.width++;
                    g.translate(-1, 0);
                } else
                {
                    trackBounds.width += 2;
                }
            if(c.isEnabled())
            {
                g.setColor(lightShadowColor);
                g.drawLine(0, 0, 0, trackBounds.height - 1);
                g.drawLine(trackBounds.width - 2, 0, trackBounds.width - 2, trackBounds.height - 1);
                g.setColor(darkShadowColor);
                g.drawLine(1, trackBounds.height - 1, trackBounds.width - 2, trackBounds.height - 1);
                g.drawLine(1, 0, trackBounds.width - 2, 0);
                g.setColor(shadowColor);
                g.drawLine(1, 1, trackBounds.width - 3, 1);
            } else
            {
                MUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height);
            }
            if(!isFreeStanding)
                if(!leftToRight)
                {
                    trackBounds.width--;
                    g.translate(1, 0);
                } else
                {
                    trackBounds.width -= 2;
                }
        } else
        {
            if(!isFreeStanding)
                trackBounds.height += 2;
            g.setColor(shadowColor);
            g.drawLine(0, 0, trackBounds.width - 1, 0);
            if(c.isEnabled())
            {
                g.setColor(lightShadowColor);
                g.drawLine(0, 0, trackBounds.width - 1, 0);
                g.drawLine(0, trackBounds.height - 2, trackBounds.width - 1, trackBounds.height - 2);
                g.setColor(darkShadowColor);
                g.drawLine(0, 1, 0, trackBounds.height - 2);
                g.drawLine(trackBounds.width - 1, 1, trackBounds.width - 1, trackBounds.height - 2);
                g.setColor(shadowColor);
                g.drawLine(1, 1, 1, trackBounds.height - 3);
            } else
            {
                MUtils.drawDisabledBorder(g, 0, 0, trackBounds.width, trackBounds.height);
            }
            if(!isFreeStanding)
                trackBounds.height -= 2;
        }
        g.translate(-trackBounds.x, -trackBounds.y);
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
    {
        if(!c.isEnabled())
            return;
        boolean leftToRight = c.getComponentOrientation().isLeftToRight();
        g.translate(thumbBounds.x, thumbBounds.y);
        if(scrollbar.getOrientation() == 1)
        {
            if(!isFreeStanding)
                if(!leftToRight)
                {
                    thumbBounds.width++;
                    g.translate(-1, 0);
                } else
                {
                    thumbBounds.width += 2;
                }
            int width = thumbBounds.width;
            int height = thumbBounds.height;
            g.setColor(thumbColor);
            g.fillRect(0, 0, width - 2, height - 1);
            Graphics2D g2 = (Graphics2D)g;
            java.awt.Paint storedPaint = g2.getPaint();
            g2.setPaint(new GradientPaint(1.0F, 1.0F, DynaTheme.BRIGHT_FINISH, (int)((double)width * 0.59999999999999998D), 1.0F, DynaTheme.BRIGHT_BEGIN));
            g2.fillRect(1, 1, width - 2, height - 2);
            g2.setPaint(storedPaint);
            g2 = null;
            MUtils.drawPlainButtonBorder(g, 0, 0, width - 1, height);
            g.setColor(thumbHighlightColor);
            g.drawLine(2, 1, width - 4, 1);
            paintBumps(g, c, 3, 4, thumbBounds.width - 6, thumbBounds.height - 7);
            if(!isFreeStanding)
                if(!leftToRight)
                {
                    thumbBounds.width--;
                    g.translate(1, 0);
                } else
                {
                    thumbBounds.width -= 2;
                }
        } else
        {
            if(!isFreeStanding)
                thumbBounds.height += 2;
            int width = thumbBounds.width;
            int height = thumbBounds.height;
            g.setColor(thumbColor);
            g.fillRect(0, 0, width - 2, height - 1);
            Graphics2D g2 = (Graphics2D)g;
            java.awt.Paint storedPaint = g2.getPaint();
            g2.setPaint(new GradientPaint(1.0F, 1.0F, DynaTheme.BRIGHT_FINISH, 1.0F, (int)((double)height * 0.59999999999999998D), DynaTheme.BRIGHT_BEGIN));
            g2.fillRect(1, 1, width - 2, height - 2);
            g2.setPaint(storedPaint);
            g2 = null;
            MUtils.drawPlainButtonBorder(g, 0, 0, width, height - 1);
            g.setColor(thumbHighlightColor);
            g.drawLine(width - 2, 2, width - 2, height - 4);
            g.drawLine(1, 2, 1, height - 4);
            paintBumps(g, c, 4, 3, thumbBounds.width - 7, thumbBounds.height - 6);
            if(!isFreeStanding)
                thumbBounds.height -= 2;
        }
        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    private void paintBumps(Graphics g, JComponent c, int x, int y, int width, int height)
    {
        if(!useNarrowBumps())
        {
            bumps.setBumpArea(width, height);
            bumps.paintIcon(c, g, x, y);
        } else
        {
            int MAX_WIDTH = UIManager.getInt("ScrollBar.maxBumpsWidth");
            int myWidth = Math.min(MAX_WIDTH, width);
            int myHeight = Math.min(MAX_WIDTH, height);
            int myX = x + (width - myWidth) / 2;
            int myY = y + (height - myHeight) / 2;
            bumps.setBumpArea(myWidth, myHeight);
            bumps.paintIcon(c, g, myX, myY);
        }
    }

    private boolean useNarrowBumps()
    {
        Object value = UIManager.get("ScrollBar.maxBumpsWidth");
        return value != null && (value instanceof Integer);
    }

    private static final String PROPERTY_PREFIX = "ScrollBar.";
    public static final String MAX_BUMPS_WIDTH_KEY = "ScrollBar.maxBumpsWidth";
    private static Color shadowColor;
    private static Color highlightColor;
    private static Color darkShadowColor;
    private static Color thumbColor;
    private static Color thumbShadow;
    private static Color thumbHighlightColor;
    private static Color lightShadowColor;
    private MBumps bumps;
}
