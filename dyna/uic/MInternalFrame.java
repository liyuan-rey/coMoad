// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MInternalFrame.java

package dyna.uic;

import com.jgoodies.util.Utilities;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

// Referenced classes of package dyna.uic:
//            DynaTheme

public class MInternalFrame extends JPanel
{
    private static class RaisedHeaderBorder extends AbstractBorder
    {

        public Insets getBorderInsets(Component c)
        {
            return INSETS;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            g.translate(x, y);
            g.setColor(new Color(127, 161, 196));
            g.fillRect(0, 0, w, 1);
            g.translate(-x, -y);
        }

        private static final Insets INSETS = new Insets(1, 0, 1, 0);


        RaisedHeaderBorder()
        {
        }
    }

    private static class ShadowBorder extends AbstractBorder
    {

        public Insets getBorderInsets(Component c)
        {
            return INSETS;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            Color shadow = UIManager.getColor("controlShadow");
            Color lighterShadow = new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 70);
            g.translate(x, y);
            g.setColor(new Color(131, 135, 136));
            g.fillRect(2, 0, w - 4, 1);
            g.fillRect(0, 2, 1, h - 3);
            g.fillRect(w - 1, 2, 1, h - 3);
            g.fillRect(1, h - 1, w - 2, 1);
            g.setColor(new Color(126, 137, 143));
            g.fillRect(1, 1, 1, 1);
            g.fillRect(w - 2, 1, 1, 1);
            g.setColor(new Color(190, 191, 185));
            g.fillRect(2, h - 2, w - 4, 1);
            g.fillRect(0, h - 1, 1, 1);
            g.fillRect(w - 1, h - 1, 1, 1);
            g.fillRect(w - 2, 0, 1, 1);
            g.fillRect(w - 1, 1, 1, 1);
            g.fillRect(1, 0, 1, 1);
            g.fillRect(0, 1, 1, 1);
            g.setColor(lighterShadow);
            g.fillRect(2, 1, w - 4, 1);
            g.fillRect(1, 2, 1, h - 4);
            g.fillRect(w - 2, 2, 1, h - 4);
            g.translate(-x, -y);
        }

        private static final Insets INSETS = new Insets(2, 2, 2, 2);


        ShadowBorder()
        {
        }
    }

    private static class GradientPanel extends JPanel
    {

        public void paintComponent(Graphics g)
        {
            paintComponent(g);
            if(!isOpaque())
            {
                return;
            } else
            {
                int width = getWidth();
                int height = getHeight();
                Graphics2D g2 = (Graphics2D)g;
                java.awt.Paint storedPaint = g2.getPaint();
                g2.setPaint(new GradientPaint(0.0F, 0.0F, DynaTheme.BRIGHT_BEGIN, 0.0F, height, DynaTheme.LT_BRIGHT_FINISH));
                g2.fillRect(0, 0, width, height);
                g.setColor(new Color(109, 133, 157));
                g.fillRect(0, height - 1, width, 1);
                g2.setPaint(storedPaint);
                g2 = null;
                return;
            }
        }

        GradientPanel(LayoutManager lm, Color background)
        {
            JPanel(lm);
            setBackground(background);
        }
    }


    public MInternalFrame(Icon frameIcon, String title, JToolBar bar, JComponent content)
    {
        JPanel(new BorderLayout());
        isSelected = false;
        titleLabel = new JLabel(title, frameIcon, 10);
        setBackground(new Color(223, 216, 206));
        JPanel top = null;
        top = buildHeader(titleLabel, bar);
        add(top, "North");
        if(content != null)
            setContent(content);
        setBorder(new ShadowBorder());
        setSelected(true);
        if(title == null)
            top.setVisible(false);
        updateHeader();
    }

    public MInternalFrame(String title, JToolBar bar, JComponent c)
    {
        MInternalFrame(null, title, bar, c);
    }

    public MInternalFrame(Icon icon, String title)
    {
        MInternalFrame(icon, title, null, null);
    }

    public MInternalFrame(String title)
    {
        MInternalFrame(null, title, null, null);
    }

    public MInternalFrame()
    {
        MInternalFrame(null, null, null, null);
    }

    public Icon getFrameIcon()
    {
        return titleLabel.getIcon();
    }

    public void setFrameIcon(Icon icon)
    {
        titleLabel.setIcon(icon);
    }

    public String getTitle()
    {
        return titleLabel.getText();
    }

    public void setTitle(String text)
    {
        titleLabel.setText(text);
    }

    public void setToolBar(JToolBar newToolBar)
    {
        JToolBar oldToolBar = null;
        if(gradientPanel.getComponentCount() > 1)
            oldToolBar = (JToolBar)gradientPanel.getComponent(1);
        if(oldToolBar == newToolBar)
            return;
        if(oldToolBar != null)
            gradientPanel.remove(oldToolBar);
        if(newToolBar != null)
        {
            newToolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            gradientPanel.add(newToolBar, "East");
            newToolBar.setOpaque(false);
        }
        updateHeader();
    }

    public Component getContent()
    {
        return hasContent() ? getComponent(1) : null;
    }

    public void setContent(Component content)
    {
        if(hasContent())
            remove(getContent());
        add(content, "Center");
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
        updateHeader();
    }

    private JPanel buildHeader(JLabel label, JToolBar bar)
    {
        gradientPanel = new GradientPanel(new BorderLayout(), getHeaderBackground());
        label.setOpaque(false);
        gradientPanel.add(label, "West");
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 1));
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(gradientPanel, "Center");
        setToolBar(bar);
        headerPanel.setBorder(new RaisedHeaderBorder());
        headerPanel.setOpaque(false);
        return headerPanel;
    }

    private void updateHeader()
    {
        if(titleLabel == null || titleLabel.getText() == null || titleLabel.getText().equals(""))
        {
            return;
        } else
        {
            gradientPanel.setBackground(getHeaderBackground());
            gradientPanel.setOpaque(isSelected());
            titleLabel.setForeground(getTextForeground(isSelected()));
            headerPanel.repaint();
            return;
        }
    }

    public void updateUI()
    {
        updateUI();
        if(titleLabel != null)
            updateHeader();
    }

    private boolean hasContent()
    {
        return getComponentCount() > 1;
    }

    protected Color getTextForeground(boolean selected)
    {
        Color c = UIManager.getColor(selected ? "SimpleInternalFrame.activeTitleForeground" : "SimpleInternalFrame.inactiveTitleForeground");
        if(c != null)
            return c;
        else
            return UIManager.getColor(selected ? "InternalFrame.activeTitleForeground" : "Label.foreground");
    }

    protected Color getHeaderBackground()
    {
        Color c = UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
        if(c != null)
            return c;
        if(Utilities.IS_WINDOWS_XP && !Utilities.IS_BEFORE_14)
            c = UIManager.getColor("InternalFrame.activeTitleGradient");
        return c == null ? UIManager.getColor("InternalFrame.activeTitleBackground") : c;
    }

    private JLabel titleLabel;
    private GradientPanel gradientPanel;
    private JPanel headerPanel;
    private boolean isSelected;
}
