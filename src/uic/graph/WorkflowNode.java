// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkflowNode.java

package dyna.uic.graph;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import dyna.uic.DynaTheme;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.plaf.ColorUIResource;

// Referenced classes of package dyna.uic.graph:
//            AbstractNode, Graph

public class WorkflowNode extends AbstractNode
{

    public WorkflowNode(String oid, String identifier, String name, int mode, boolean selected, boolean changed, ImageIcon icon, 
            Object userData)
    {
        g2 = null;
        _icon = null;
        _iconX = 0;
        _iconY = 0;
        _iconRectangle = null;
        _iconRectangleN = null;
        _textRectangle = null;
        _iconMode = false;
        _colorBackup = null;
        _fontBackup = null;
        setOid(oid);
        setIdentifier(identifier);
        setName(name);
        setMode(mode);
        setSelected(selected);
        setChanged(changed);
        _icon = icon;
        setUserData(userData);
        setMaximumSize(new Dimension(300, 150));
        setMinimumSize(new Dimension(145, 50));
        if(_defaultIcon == null)
        {
            _defaultIcon = new ImageIcon("icons/GenericActivity.gif");
            _startIcon = new ImageIcon("icons/StartActivity.gif");
            _finishIcon = new ImageIcon("icons/FinishActivity.gif");
            _subIcon = new ImageIcon("icons/SubWorkFlowActivity.gif");
            _lockIcon = new ImageIcon("icons/LockActivity.gif");
            _unlockIcon = new ImageIcon("icons/UnlockActivity.gif");
            _changeIcon = new ImageIcon("icons/ChangeObjectStatusActivity.gif");
            _blockIcon = new ImageIcon("icons/InlineBlockActivity.gif");
            _reviewIcon = new ImageIcon("icons/ReviewActivity.gif");
            _approvalIcon = new ImageIcon("icons/ApprovalActivity.gif");
            _routeIcon = new ImageIcon("icons/RouteActivity.gif");
            _libIcon = new ImageIcon("icons/LibraryActivity.gif");
            _programIcon = new ImageIcon("icons/ProgramActivity.gif");
            _notificationIcon = new ImageIcon("icons/NotificationActivity.gif");
            _emailIcon = new ImageIcon("icons/EmailNotificationActivity.gif");
            _textRenderer = new JLabel();
            _textRenderer.setFont(Graph.defaultFonts[0]);
            _textRenderer.setForeground(Color.black);
            _textRenderer.setVerticalAlignment(1);
            _selectionForeground = new Color(153, 153, 204);
            _propertiesToBeDisplay = new ArrayList();
            _propertiesToBeDisplay.add("date.closed");
        }
    }

    public WorkflowNode(String oid, String identifier, String name, Object userData)
    {
        this(oid, identifier, name, 1, false, false, null, userData);
    }

    public WorkflowNode(String oid, String identifier, String name)
    {
        this(oid, identifier, name, 1, false, false, null, null);
    }

    public WorkflowNode(String oid, String identifier)
    {
        this(oid, identifier, null, 1, false, false, null, null);
    }

    public WorkflowNode()
    {
        this(null, null, null, 1, false, false, null, null);
    }

    private void computeCoordinates()
    {
        Dimension _size = new Dimension();
        int n = 2;
        if(!_iconMode)
        {
            _iconX = getX() + 6;
            _iconY = getY() + 6;
        } else
        {
            _iconX = getX();
            _iconY = getY();
        }
        _iconRectangle = new Rectangle(_iconX - 1, _iconY - 1, _icon.getIconWidth() + 1, _icon.getIconHeight() + 1);
        _iconRectangleN = new Rectangle(_iconX - 2, _iconY - 2, _icon.getIconWidth() + 3, _icon.getIconHeight() + 3);
        if(!_iconMode)
        {
            getSize(_size);
            n += _propertiesToBeDisplay.size();
            if(Graph.defaultFontMetrics == null)
                _size.height = 5 + 18 * n + 5;
            else
                _size.height = 5 + Graph.defaultFontMetrics[0].getHeight() * n + 5;
            setSize(_size);
        } else
        {
            _size.width = _iconRectangleN.width;
            _size.height = _iconRectangleN.height;
            setSize(_size);
        }
        computeTextRectangle();
    }

    private void computeTextRectangle()
    {
        if(!_iconMode)
            _textRectangle = new Rectangle(_iconRectangle.x + _iconRectangle.width + 5, _iconY, getWidth() - _iconRectangle.width - 10, getHeight() - 10);
        else
            _textRectangle = new Rectangle(_iconRectangle.x - (50 - _iconRectangle.width / 2), _iconRectangle.y + _iconRectangle.height + 5, 100, 21);
    }

    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
        if(_icon == null)
            _icon = _defaultIcon;
        computeCoordinates();
    }

    public void setIcon(ImageIcon icon)
    {
        _icon = icon;
        computeCoordinates();
    }

    public ImageIcon getIcon()
    {
        return _icon;
    }

    public void setPropertiesToBeDisplay(ArrayList list)
    {
        _propertiesToBeDisplay = list;
    }

    public ArrayList getPropertiesToBeDisplay()
    {
        return _propertiesToBeDisplay;
    }

    public void clearPropertiesToBeDisplay()
    {
        _propertiesToBeDisplay.clear();
    }

    public void setIconMode(boolean mode)
    {
        _iconMode = mode;
    }

    public boolean isIconMode()
    {
        return _iconMode;
    }

    public void paint(Graphics g)
    {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        g2 = (Graphics2D)g;
        _colorBackup = g2.getColor();
        _fontBackup = g2.getFont();
        int fontHeight = Graph.defaultFontMetrics[0].getHeight();
        g2.translate(x, y);
        if(isSelected())
        {
            g2.setColor(highlitedBox);
            g2.fillRoundRect(-6, -6, w + 12, h + 12, 10, 10);
            g2.fillRoundRect(-5, -5, w + 10, h + 10, 10, 10);
            g2.fillRoundRect(-4, -4, w + 8, h + 8, 10, 10);
            g2.fillRoundRect(-3, -3, w + 6, h + 6, 10, 10);
            g2.fillRoundRect(-2, -2, w + 4, h + 4, 10, 10);
            g2.fillRoundRect(-1, -1, w + 2, h + 2, 10, 10);
        }
        if(!_iconMode)
        {
            if(getMode() != 9)
            {
                g2.setColor(titleBackground);
                g2.fillRect(0, 0, w - 3, fontHeight + 3);
                g.setColor(darkShadow);
                g.drawLine(0, fontHeight + 2, w - 3, fontHeight + 2);
            }
            if(getMode() == 9)
            {
                g2.setColor(Graph.colors[getMode()]);
                g2.fillRect(0, 0, w, h);
                g2.setColor(darkShadow);
                g.drawLine(0, fontHeight + 2, w, fontHeight + 2);
                g2.drawRect(0, 0, w, h);
            } else
            {
                g2.setColor(Graph.colors[getMode()]);
                g2.fillRect(0, fontHeight + 3, w - 3, h - fontHeight - 6);
                g.setColor(darkShadow);
                g.drawRect(0, 0, w - 3, h - 3);
                g.setColor(highlight);
                g.drawLine(1, 1, w - 4, 1);
                g.drawLine(1, 1, 1, h - 4);
                g.setColor(lightShadow);
                g.drawLine(w - 2, 1, w - 2, h - 2);
                g.drawLine(1, h - 2, w - 3, h - 2);
                g.setColor(lighterShadow);
                g.drawLine(w - 1, 2, w - 1, h - 2);
                g.drawLine(2, h - 1, w - 2, h - 1);
            }
        }
        if(!_iconMode)
        {
            g2.drawImage(_icon.getImage(), 6, fontHeight + 8, null);
            g2.translate(10, 3);
        } else
        {
            g2.drawImage(_icon.getImage(), 3, 3, null);
            g2.translate(-(_textRectangle.width / 2 - _iconRectangleN.width / 2 - 2), _iconRectangle.height + 5);
        }
        _textRenderer.setFont(Graph.defaultFonts[0]);
        _textRenderer.setBounds(_textRectangle);
        int totalHeight = 0;
        if(!_iconMode)
        {
            _textRenderer.setText(getIdentifier());
            if(getMode() == 9)
                _textRenderer.setForeground(Color.black);
            else
                _textRenderer.setForeground(titleForeground);
            _textRectangle.width = _textRectangle.width + 20;
            _textRenderer.setBounds(_textRectangle);
            _textRenderer.paint(g2);
            totalHeight += fontHeight;
            _textRectangle.width = _textRectangle.width - 20;
            _textRenderer.setBounds(_textRectangle);
            _textRenderer.setText(getName());
            _textRenderer.setForeground(Color.black);
            g2.translate(_iconRectangle.width, fontHeight);
            _textRenderer.paint(g2);
            totalHeight += fontHeight;
            _textRenderer.setText((String)getProperty((String)_propertiesToBeDisplay.get(0)));
            g2.translate(0, fontHeight);
            _textRenderer.paint(g2);
            g2.translate(-(_textRectangle.x - x), -totalHeight - 3);
        } else
        {
            _textRenderer.setHorizontalAlignment(0);
            _textRenderer.setText(getName());
            _textRenderer.paint(g);
            _textRenderer.setHorizontalAlignment(2);
            g2.translate(_textRectangle.width / 2 - _iconRectangleN.width / 2 - 2, -totalHeight - 5 - _iconRectangle.height);
        }
        g2.translate(-x, -y);
        g2.setFont(_fontBackup);
        g2.setColor(_colorBackup);
    }

    public void setProperty(String name, Object value)
    {
        super.setProperty(name, value);
        if(name != null && name.equals("type") && value != null)
        {
            if(value.equals("100"))
                _icon = _defaultIcon;
            else
            if(value.equals("110"))
                _icon = _notificationIcon;
            else
            if(value.equals("120"))
                _icon = _reviewIcon;
            else
            if(value.equals("130"))
                _icon = _approvalIcon;
            else
            if(value.equals("140"))
                _icon = _programIcon;
            else
            if(value.equals("150"))
                _icon = _libIcon;
            else
            if(value.equals("160"))
                _icon = _blockIcon;
            else
            if(value.equals("170"))
                _icon = _routeIcon;
            else
            if(value.equals("180"))
                _icon = _startIcon;
            else
            if(value.equals("190"))
                _icon = _finishIcon;
            else
            if(value.equals("200"))
                _icon = _subIcon;
            else
            if(value.equals("210"))
                _icon = _lockIcon;
            else
            if(value.equals("220"))
                _icon = _unlockIcon;
            else
            if(value.equals("230"))
                _icon = _emailIcon;
            else
            if(value.equals("240"))
                _icon = _changeIcon;
            computeCoordinates();
        }
    }

    Graphics2D g2;
    private static ImageIcon _defaultIcon = null;
    private static ImageIcon _startIcon = null;
    private static ImageIcon _finishIcon = null;
    private static ImageIcon _subIcon = null;
    private static ImageIcon _lockIcon = null;
    private static ImageIcon _unlockIcon = null;
    private static ImageIcon _changeIcon = null;
    private static ImageIcon _programIcon = null;
    private static ImageIcon _libIcon = null;
    private static ImageIcon _notificationIcon = null;
    private static ImageIcon _emailIcon = null;
    private static ImageIcon _blockIcon = null;
    private static ImageIcon _routeIcon = null;
    private static ImageIcon _reviewIcon = null;
    private static ImageIcon _approvalIcon = null;
    private static ArrayList _propertiesToBeDisplay = null;
    private static JLabel _textRenderer = null;
    private static Color _selectionForeground = null;
    private static final Insets NORMAL_INSETS = new Insets(2, 2, 3, 3);
    private static final Insets MAXIMIZED_INSETS = new Insets(2, 2, 2, 2);
    private static final int ALPHA1 = 150;
    private static final int ALPHA2 = 50;
    private static final Color background = PlasticLookAndFeel.getDesktopColor();
    private static final Color highlight = PlasticLookAndFeel.getControlHighlight();
    private static final Color darkShadow;
    private static final Color primaryHighlight = PlasticLookAndFeel.getPrimaryControlHighlight();
    private static final Color titleForeground;
    private static final Color titleBackground;
    private static final Color lightShadow;
    private static final Color lighterShadow;
    private static final Color highlitedBox = new Color(PlasticLookAndFeel.getFocusColor().getRed(), PlasticLookAndFeel.getFocusColor().getGreen(), PlasticLookAndFeel.getFocusColor().getBlue(), 30);
    private static final Color lightArrow;
    private ImageIcon _icon;
    private int _iconX;
    private int _iconY;
    private Rectangle _iconRectangle;
    private Rectangle _iconRectangleN;
    private Rectangle _textRectangle;
    private boolean _iconMode;
    private Color _colorBackup;
    private Font _fontBackup;

    static 
    {
        darkShadow = PlasticLookAndFeel.getControlDarkShadow();
        titleForeground = Color.white;
        titleBackground = DynaTheme.primary1;
        lightShadow = new Color(darkShadow.getRed(), darkShadow.getGreen(), darkShadow.getBlue(), 150);
        lighterShadow = new Color(darkShadow.getRed(), darkShadow.getGreen(), darkShadow.getBlue(), 50);
        lightArrow = new Color(titleBackground.getRed(), titleBackground.getGreen(), titleBackground.getBlue(), 50);
    }
}
