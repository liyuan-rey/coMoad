// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MArrowButton.java

package dyna.uic;

import java.awt.*;
import javax.swing.ButtonModel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalScrollButton;

// Referenced classes of package dyna.uic:
//            MUtils

final class MArrowButton extends MetalScrollButton
{

    public MArrowButton(int direction, int width, boolean freeStanding)
    {
        MetalScrollButton(direction, width, freeStanding);
        shadowColor = UIManager.getColor("ScrollBar.darkShadow");
        highlightColor = UIManager.getColor("ScrollBar.highlight");
        isFreeStanding = freeStanding;
    }

    public void setFreeStanding(boolean freeStanding)
    {
        setFreeStanding(freeStanding);
        isFreeStanding = freeStanding;
    }

    public void paint(Graphics g)
    {
        boolean leftToRight = getComponentOrientation().isLeftToRight();
        boolean isEnabled = getParent().isEnabled();
        boolean isPressed = getModel().isPressed();
        Color arrowColor = isEnabled ? ((Color) (MetalLookAndFeel.getControlInfo())) : ((Color) (MetalLookAndFeel.getControlDisabled()));
        int width = getWidth();
        int height = getHeight();
        int w = width;
        int h = height;
        int arrowHeight = (height + 1) / 4;
        g.setColor(((Color) (isPressed ? ((Color) (MetalLookAndFeel.getControlShadow())) : getBackground())));
        g.fillRect(0, 0, width, height);
        if(getDirection() == 1)
            paintNorth(g, leftToRight, isEnabled, arrowColor, isPressed, width, height, w, h, arrowHeight);
        else
        if(getDirection() == 5)
            paintSouth(g, leftToRight, isEnabled, arrowColor, isPressed, width, height, w, h, arrowHeight);
        else
        if(getDirection() == 3)
            paintEast(g, isEnabled, arrowColor, isPressed, width, height, w, h, arrowHeight);
        else
        if(getDirection() == 7)
            paintWest(g, isEnabled, arrowColor, isPressed, width, height, w, h, arrowHeight);
        paint3D(g);
    }

    private void paintWest(Graphics g, boolean isEnabled, Color arrowColor, boolean isPressed, int width, int height, int w, 
            int h, int arrowHeight)
    {
        if(!isFreeStanding)
        {
            height += 2;
            width++;
            g.translate(-1, 0);
        }
        g.setColor(arrowColor);
        int startX = ((w + 1) - arrowHeight) / 2;
        int startY = h / 2;
        for(int line = 0; line < arrowHeight; line++)
            g.drawLine(startX + line, startY - line, startX + line, startY + line + 1);

        if(isEnabled)
        {
            g.setColor(highlightColor);
            if(!isPressed)
            {
                g.drawLine(1, 1, width - 1, 1);
                g.drawLine(1, 1, 1, height - 3);
            }
            g.drawLine(1, height - 1, width - 1, height - 1);
            g.setColor(shadowColor);
            g.drawLine(0, 0, width - 1, 0);
            g.drawLine(0, 0, 0, height - 2);
            g.drawLine(2, height - 2, width - 1, height - 2);
        } else
        {
            MUtils.drawDisabledBorder(g, 0, 0, width + 1, height);
        }
        if(!isFreeStanding)
        {
            height -= 2;
            width--;
            g.translate(1, 0);
        }
    }

    private void paintEast(Graphics g, boolean isEnabled, Color arrowColor, boolean isPressed, int width, int height, int w, 
            int h, int arrowHeight)
    {
        if(!isFreeStanding)
        {
            height += 2;
            width++;
        }
        g.setColor(arrowColor);
        int startX = (((w + 1) - arrowHeight) / 2 + arrowHeight) - 1;
        int startY = h / 2;
        for(int line = 0; line < arrowHeight; line++)
            g.drawLine(startX - line, startY - line, startX - line, startY + line + 1);

        if(isEnabled)
        {
            g.setColor(highlightColor);
            if(!isPressed)
            {
                g.drawLine(0, 1, width - 3, 1);
                g.drawLine(0, 1, 0, height - 3);
            }
            g.drawLine(width - 1, 1, width - 1, height - 1);
            g.drawLine(0, height - 1, width - 1, height - 1);
            g.setColor(shadowColor);
            g.drawLine(0, 0, width - 2, 0);
            g.drawLine(width - 2, 1, width - 2, height - 2);
            g.drawLine(0, height - 2, width - 2, height - 2);
        } else
        {
            MUtils.drawDisabledBorder(g, -1, 0, width + 1, height);
        }
        if(!isFreeStanding)
        {
            height -= 2;
            width--;
        }
    }

    private void paintSouth(Graphics g, boolean leftToRight, boolean isEnabled, Color arrowColor, boolean isPressed, int width, int height, 
            int w, int h, int arrowHeight)
    {
        if(!isFreeStanding)
        {
            height++;
            if(!leftToRight)
            {
                width++;
                g.translate(-1, 0);
            } else
            {
                width += 2;
            }
        }
        g.setColor(arrowColor);
        int startY = (((h + 1) - arrowHeight) / 2 + arrowHeight) - 1;
        int startX = w / 2;
        for(int line = 0; line < arrowHeight; line++)
            g.drawLine(startX - line, startY - line, startX + line + 1, startY - line);

        if(isEnabled)
        {
            g.setColor(highlightColor);
            if(!isPressed)
            {
                g.drawLine(1, 0, width - 3, 0);
                g.drawLine(1, 0, 1, height - 3);
            }
            g.drawLine(1, height - 1, width - 1, height - 1);
            g.drawLine(width - 1, 0, width - 1, height - 1);
            g.setColor(shadowColor);
            g.drawLine(0, 0, 0, height - 2);
            g.drawLine(width - 2, 0, width - 2, height - 2);
            g.drawLine(1, height - 2, width - 2, height - 2);
        } else
        {
            MUtils.drawDisabledBorder(g, 0, -1, width, height + 1);
        }
        if(!isFreeStanding)
        {
            height--;
            if(!leftToRight)
            {
                width--;
                g.translate(1, 0);
            } else
            {
                width -= 2;
            }
        }
    }

    private void paintNorth(Graphics g, boolean leftToRight, boolean isEnabled, Color arrowColor, boolean isPressed, int width, int height, 
            int w, int h, int arrowHeight)
    {
        if(!isFreeStanding)
        {
            height++;
            g.translate(0, -1);
            if(!leftToRight)
            {
                width++;
                g.translate(-1, 0);
            } else
            {
                width += 2;
            }
        }
        g.setColor(arrowColor);
        int startY = ((h + 1) - arrowHeight) / 2;
        int startX = w / 2;
        for(int line = 0; line < arrowHeight; line++)
            g.drawLine(startX - line, startY + line, startX + line + 1, startY + line);

        if(isEnabled)
        {
            g.setColor(highlightColor);
            if(!isPressed)
            {
                g.drawLine(1, 1, width - 3, 1);
                g.drawLine(1, 1, 1, height - 1);
            }
            g.drawLine(width - 1, 1, width - 1, height - 1);
            g.setColor(shadowColor);
            g.drawLine(0, 0, width - 2, 0);
            g.drawLine(0, 0, 0, height - 1);
            g.drawLine(width - 2, 2, width - 2, height - 1);
        } else
        {
            MUtils.drawDisabledBorder(g, 0, 0, width, height + 1);
        }
        if(!isFreeStanding)
        {
            height--;
            g.translate(0, 1);
            if(!leftToRight)
            {
                width--;
                g.translate(1, 0);
            } else
            {
                width -= 2;
            }
        }
    }

    private void paint3D(Graphics g)
    {
        ButtonModel buttonModel = getModel();
        if(buttonModel.isArmed() && buttonModel.isPressed() || buttonModel.isSelected())
            return;
        int width = getWidth();
        int height = getHeight();
        if(getDirection() == 3)
            width -= 2;
        else
        if(getDirection() == 5)
            height -= 2;
        Rectangle r = new Rectangle(1, 1, width, height);
        boolean isHorizontal = getDirection() == 3 || getDirection() == 7;
        MUtils.addLight3DEffect(g, r, isHorizontal);
    }

    private static Color shadowColor;
    private static Color highlightColor;
    private boolean isFreeStanding;
}
