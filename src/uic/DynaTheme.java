// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaTheme.java

package dyna.uic;

import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.theme.ExperienceBlue;
import dyna.framework.client.UIManagement;
import java.awt.*;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

// Referenced classes of package dyna.uic:
//            MBorders

public class DynaTheme extends ExperienceBlue
{

    public DynaTheme()
    {
        new UIManagement();
        Options.setGlobalFontSizeHints(FontSizeHints.MIXED);
        Options.setDefaultIconSize(new Dimension(18, 18));
        UIDefaults uiDefaults = UIManager.getDefaults();
        uiDefaults.put("TabbedPane.selected", new ColorUIResource(223, 216, 206));
        uiDefaults.put("TabbedPaneUI", "dyna.uic.MTabbedPaneUI");
        uiDefaults.put("MenuUI", "dyna.uic.DynaMenuUI");
        uiDefaults.put("MenuItemUI", "dyna.uic.DynaMenuItemUI");
        uiDefaults.put("MenuBarUI", "dyna.uic.MMenuBarUI");
        uiDefaults.put("ToolBarUI", "dyna.uic.MToolBarUI");
        uiDefaults.put("ButtonUI", "dyna.uic.MButtonUI");
        uiDefaults.put("Button.border", MBorders.getButtonBorder());
        uiDefaults.put("ComboBoxUI", "dyna.uic.MComboBoxUI");
        uiDefaults.put("ComboBox.arrowButtonBorder", MBorders.getComboBoxArrowButtonBorder());
        uiDefaults.put("ScrollBarUI", "dyna.uic.MScrollBarUI");
    }

    public String getName()
    {
        return "DynaTheme";
    }

    protected ColorUIResource getPrimary1()
    {
        return primary1;
    }

    protected ColorUIResource getSecondary1()
    {
        return secondary1;
    }

    protected ColorUIResource getSecondary2()
    {
        return secondary2;
    }

    protected ColorUIResource getSecondary3()
    {
        return secondary3;
    }

    public ColorUIResource getFocusColor()
    {
        return focusColor;
    }

    public static Color getTransparentColor(Color color, int alpha)
    {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static final Color DARK_BEGIN = new Color(0, 0, 0, 0);
    public static final Color DARK_FINISH = new Color(0, 0, 0, 64);
    public static final Color LT_DARK_FINISH = new Color(0, 0, 0, 32);
    public static final Color BRIGHT_BEGIN = new Color(255, 255, 255, 0);
    public static final Color BRIGHT_FINISH = new Color(255, 255, 255, 128);
    public static final Color LT_BRIGHT_FINISH = new Color(255, 255, 255, 64);
    public static final String BORDER_STYLE_KEY = "Plastic.borderStyle";
    public static final Font mediumPlainFont = new Font("dialog", 0, 11);
    public static final Font smallPlainFont = new Font("dialog", 0, 10);
    public static final Color treeLevelOneColor = new Color(202, 209, 236);
    public static final Color treeLevelTwoColor = new Color(215, 226, 252);
    public static final Color treeLevelThreeColor = new Color(228, 235, 249);
    public static final Color treeLevelFourColor = new Color(241, 244, 251);
    public static final Color tableheaderColor = new Color(232, 233, 227);
    public static final Color tableGridColor = new Color(213, 211, 214);
    public static final Color panelBackgroundColor = new Color(222, 229, 239);
    public static final Color tableHilightedColor2 = new Color(231, 219, 197);
    public static final ColorUIResource focusColor = new ColorUIResource(245, 165, 16);
    public static final ColorUIResource primary1 = new ColorUIResource(123, 153, 190);
    private static final ColorUIResource secondary1 = new ColorUIResource(128, 128, 128);
    private static final ColorUIResource secondary2 = new ColorUIResource(148, 144, 140);
    private static final ColorUIResource secondary3 = new ColorUIResource(240, 240, 228);

}
