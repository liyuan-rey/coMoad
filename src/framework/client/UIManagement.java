// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIManagement.java

package dyna.framework.client;

import dyna.util.Utils;
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.LinkedList;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

public class UIManagement extends MetalTheme
{

    public UIManagement()
    {
        acceleratorForegroundColor = MetalLookAndFeel.getAcceleratorForeground();
        acceleratorSelectedForegroundColor = MetalLookAndFeel.getAcceleratorSelectedForeground();
        controlColor = MetalLookAndFeel.getControl();
        controlDarkShadowColor = MetalLookAndFeel.getControlDarkShadow();
        controlDisabledColor = MetalLookAndFeel.getControlDisabled();
        controlHighlightColor = MetalLookAndFeel.getControlHighlight();
        controlInfoColor = MetalLookAndFeel.getControlInfo();
        controlShadowColor = MetalLookAndFeel.getControlShadow();
        controlTextColor = MetalLookAndFeel.getControlTextColor();
        desktopColor = MetalLookAndFeel.getDesktopColor();
        focusColor = MetalLookAndFeel.getFocusColor();
        highlightedTextColor = MetalLookAndFeel.getHighlightedTextColor();
        inactiveControlTextColor = MetalLookAndFeel.getInactiveControlTextColor();
        inactiveSystemTextColor = MetalLookAndFeel.getInactiveSystemTextColor();
        menuBackgroundColor = MetalLookAndFeel.getMenuBackground();
        menuDisabledForegroundColor = MetalLookAndFeel.getMenuDisabledForeground();
        menuForegroundColor = MetalLookAndFeel.getMenuForeground();
        menuSelectedBackgroundColor = MetalLookAndFeel.getMenuSelectedBackground();
        menuSelectedForegroundColor = MetalLookAndFeel.getMenuSelectedForeground();
        primaryControlColor = MetalLookAndFeel.getPrimaryControl();
        primaryControlDarkShadowColor = MetalLookAndFeel.getPrimaryControlDarkShadow();
        primaryControlHighlightColor = MetalLookAndFeel.getPrimaryControlHighlight();
        primaryControlInfoColor = MetalLookAndFeel.getPrimaryControlInfo();
        primaryControlShadowColor = MetalLookAndFeel.getPrimaryControlShadow();
        separatorBackgroundColor = MetalLookAndFeel.getSeparatorBackground();
        separatorForegroundColor = MetalLookAndFeel.getSeparatorForeground();
        systemTextColor = MetalLookAndFeel.getSystemTextColor();
        textHighlightColor = MetalLookAndFeel.getTextHighlightColor();
        userTextColor = MetalLookAndFeel.getUserTextColor();
        windowBackgroundColor = MetalLookAndFeel.getWindowBackground();
        windowTitleBackgroundColor = MetalLookAndFeel.getWindowTitleBackground();
        windowTitleForegroundColor = MetalLookAndFeel.getWindowTitleForeground();
        windowTitleInactiveBackgroundColor = MetalLookAndFeel.getWindowTitleInactiveBackground();
        windowTitleInactiveForegroundColor = MetalLookAndFeel.getWindowTitleInactiveForeground();
        blackColor = MetalLookAndFeel.getBlack();
        whiteColor = MetalLookAndFeel.getWhite();
        getColorProperty();
    }

    public FontUIResource getControlTextFont()
    {
        FontUIResource controlFontResource = new FontUIResource(new Font("dialog", 0, 11));
        return controlFontResource;
    }

    public FontUIResource getMenuTextFont()
    {
        FontUIResource menuFontResource = new FontUIResource(new Font("dialog", 0, 11));
        return menuFontResource;
    }

    public FontUIResource getSubTextFont()
    {
        FontUIResource subFontResource = new FontUIResource(new Font("dialog", 0, 11));
        return subFontResource;
    }

    public FontUIResource getSystemTextFont()
    {
        FontUIResource systemFontResource = new FontUIResource(new Font("dialog", 0, 11));
        return systemFontResource;
    }

    public FontUIResource getUserTextFont()
    {
        FontUIResource userFontResource = new FontUIResource(new Font("dialog", 0, 12));
        return userFontResource;
    }

    public FontUIResource getWindowTitleFont()
    {
        FontUIResource windowTitleFontResource = new FontUIResource(new Font("dialog", 2, 13));
        return windowTitleFontResource;
    }

    public ColorUIResource getPrimary1()
    {
        ColorUIResource primary1Color = new ColorUIResource(new Color(172, 189, 206));
        return primary1Color;
    }

    public ColorUIResource getPrimary2()
    {
        ColorUIResource primary2Color = new ColorUIResource(new Color(172, 189, 206));
        return primary2Color;
    }

    public ColorUIResource getPrimary3()
    {
        ColorUIResource primary3Color = new ColorUIResource(new Color(172, 189, 206));
        return primary3Color;
    }

    public ColorUIResource getSecondary1()
    {
        ColorUIResource secondary1Color = new ColorUIResource(new Color(172, 189, 206));
        return secondary1Color;
    }

    public ColorUIResource getSecondary2()
    {
        ColorUIResource secondary2Color = new ColorUIResource(new Color(172, 189, 206));
        return secondary2Color;
    }

    public ColorUIResource getSecondary3()
    {
        ColorUIResource secondary3Color = new ColorUIResource(new Color(172, 189, 206));
        return secondary3Color;
    }

    public String getName()
    {
        return null;
    }

    public ColorUIResource getAcceleratorForeground()
    {
        return acceleratorForegroundColor;
    }

    public ColorUIResource getAcceleratorSelectedForeground()
    {
        return acceleratorSelectedForegroundColor;
    }

    public ColorUIResource getControl()
    {
        return controlColor;
    }

    public ColorUIResource getControlDarkShadow()
    {
        return controlDarkShadowColor;
    }

    public ColorUIResource getControlDisabled()
    {
        return controlDisabledColor;
    }

    public ColorUIResource getControlHighlight()
    {
        return controlHighlightColor;
    }

    public ColorUIResource getControlInfo()
    {
        return controlInfoColor;
    }

    public ColorUIResource getControlShadow()
    {
        return controlShadowColor;
    }

    public ColorUIResource getControlTextColor()
    {
        return controlTextColor;
    }

    public ColorUIResource getDesktopColor()
    {
        return desktopColor;
    }

    public ColorUIResource getFocusColor()
    {
        return focusColor;
    }

    public ColorUIResource getHighlightedTextColor()
    {
        return highlightedTextColor;
    }

    public ColorUIResource getInactiveControlTextColor()
    {
        return inactiveControlTextColor;
    }

    public ColorUIResource getInactiveSystemTextColor()
    {
        return inactiveSystemTextColor;
    }

    public ColorUIResource getMenuBackground()
    {
        return menuBackgroundColor;
    }

    public ColorUIResource getMenuDisabledForeground()
    {
        return menuDisabledForegroundColor;
    }

    public ColorUIResource getMenuForeground()
    {
        return menuForegroundColor;
    }

    public ColorUIResource getMenuSelectedBackground()
    {
        return menuSelectedBackgroundColor;
    }

    public ColorUIResource getMenuSelectedForeground()
    {
        return menuSelectedForegroundColor;
    }

    public ColorUIResource getPrimaryControl()
    {
        return primaryControlColor;
    }

    public ColorUIResource getPrimaryControlDarkShadow()
    {
        return primaryControlDarkShadowColor;
    }

    public ColorUIResource getPrimaryControlHighlight()
    {
        return primaryControlHighlightColor;
    }

    public ColorUIResource getPrimaryControlInfo()
    {
        return primaryControlInfoColor;
    }

    public ColorUIResource getPrimaryControlShadow()
    {
        return primaryControlShadowColor;
    }

    public ColorUIResource getSeparatorBackground()
    {
        return separatorBackgroundColor;
    }

    public ColorUIResource getSeparatorForeground()
    {
        return separatorForegroundColor;
    }

    public ColorUIResource getSystemTextColor()
    {
        return systemTextColor;
    }

    public ColorUIResource getTextHighlightColor()
    {
        return textHighlightColor;
    }

    public ColorUIResource getUserTextColor()
    {
        return userTextColor;
    }

    public ColorUIResource getWindowBackground()
    {
        return windowBackgroundColor;
    }

    public ColorUIResource getWindowTitleBackground()
    {
        return windowTitleBackgroundColor;
    }

    public ColorUIResource getWindowTitleForeground()
    {
        return windowTitleForegroundColor;
    }

    public ColorUIResource getWindowTitleInactiveBackground()
    {
        return windowTitleInactiveBackgroundColor;
    }

    public ColorUIResource getWindowTitleInactiveForeground()
    {
        return windowTitleInactiveForegroundColor;
    }

    protected ColorUIResource getBlack()
    {
        return blackColor;
    }

    protected ColorUIResource getWhite()
    {
        return whiteColor;
    }

    public void setPanelColor()
    {
        UIManager.put("Panel.background", new Color(172, 189, 206));
        UIManager.put("Panel.foreground", Color.lightGray);
        UIManager.put("TabbedPane.tabAreaBackground", Color.cyan);
        UIManager.put("TabbedPane.foreground", Color.cyan);
        UIManager.put("TabbedPane.background", Color.red);
        UIManager.put("TabbedPane.selected", Color.orange);
        UIManager.put("Table.background", Color.cyan);
        UIManager.put("Table.focusCellBackground", Color.cyan);
        UIManager.put("Table.focusCellForeground", Color.cyan);
        UIManager.put("Table.foreground", Color.cyan);
        UIManager.put("Table.gridColor", Color.cyan);
        UIManager.put("Table.selectionBackground", Color.orange);
        UIManager.put("Table.selectionForeground", Color.black);
        UIManager.put("ScrollPane.background", Color.red);
        UIManager.put("ScrollPane.foreground", Color.blue);
        UIManager.put("Menu.background", Color.orange);
        UIManager.put("Menu.foreground", Color.black);
        UIManager.put("MenuBar.background", Color.orange);
        UIManager.put("MenuBar.foreground", Color.black);
        UIManager.put("MenuItem.background", Color.orange);
        UIManager.put("MenuItem.foreground", Color.black);
        UIManager.put("SplitPane.background", Color.orange);
        UIManager.put("SplitPane.foreground", Color.black);
        UIManager.put("ToolBar.background", Color.orange);
        UIManager.put("ToolBar.dockingBackground", Color.darkGray);
        UIManager.put("ToolBar.dockingForeground", Color.lightGray);
        UIManager.put("ToolBar.floatingBackground", Color.pink);
        UIManager.put("ToolBar.floatingForeground", Color.blue);
        UIManager.put("ToolBar.foreground", Color.orange);
        UIManager.put("Viewport.background", Color.orange);
        UIManager.put("Viewport.foreground", Color.black);
    }

    public void getColorProperty()
    {
        try
        {
            colorProperties = new Properties();
            FileInputStream fileInput = new FileInputStream("./dyna/framework/client/color.properties");
            colorProperties.load(fileInput);
            fileInput.close();
            fileInput = null;
            toolbarBackgroundColor = new Color(20, 39, 116);
            windowBackGround = stringToColor("windowBackGround");
            panelBackGround = stringToColor("panelBackGround");
            panelForeGround = stringToColor("panelForeGround");
            groupBackGround = stringToColor("groupBackGround");
            groupForeGround = stringToColor("groupForeGround");
            textBoxEditableBack = stringToColor("textBoxEditableBack");
            textBoxEditableFore = stringToColor("textBoxEditableFore");
            textBoxNotEditableBack = stringToColor("textBoxNotEditableBack");
            textBoxNotEditableFore = stringToColor("textBoxNotEditableFore");
            textBoxMandatoryBack = stringToColor("textBoxMandatoryBack");
            textBoxMandatoryFore = stringToColor("textBoxMandatoryFore");
            treeLevelOneColor = stringToColor("treeLevelOneColor");
            treeLevelTwoColor = stringToColor("treeLevelTwoColor");
            treeLevelThreeColor = stringToColor("treeLevelThreeColor");
            treeLevelFourColor = stringToColor("treeLevelFourColor");
            tableheaderColor = stringToColor("tableheaderColor");
            tableGridColor = stringToColor("tableGridColor");
            borderWindow = BorderFactory.createMatteBorder(2, 2, 2, 2, stringToColor("borderWindow"));
            borderPanel = BorderFactory.createMatteBorder(2, 2, 2, 2, stringToColor("borderPanel"));
            borderGroup = BorderFactory.createMatteBorder(2, 2, 2, 2, stringToColor("borderGroup"));
            borderGroupTitle = BorderFactory.createMatteBorder(2, 2, 2, 2, stringToColor("borderGroupTitle"));
            borderTextBoxEditable = BorderFactory.createMatteBorder(1, 1, 1, 1, stringToColor("borderTextBoxEditable"));
            borderTextBoxNotEditable = BorderFactory.createMatteBorder(1, 1, 1, 1, stringToColor("borderTextBoxNotEditable"));
            borderTextBoxMandatory = BorderFactory.createMatteBorder(1, 1, 1, 1, stringToColor("borderTextBoxMandatory"));
        }
        catch(IOException ioe)
        {
            System.out.println(ioe);
        }
    }

    public Color stringToColor(String codeName)
    {
        Color returnColor = new Color(204, 204, 204);
        try
        {
            LinkedList list = (LinkedList)Utils.tokenizeMessage(colorProperties.getProperty(codeName), ',');
            if(list == null)
                return returnColor;
            Integer r = new Integer((String)list.get(0));
            Integer g = new Integer((String)list.get(1));
            Integer b = new Integer((String)list.get(2));
            returnColor = new Color(r.intValue(), g.intValue(), b.intValue());
        }
        catch(NumberFormatException nfe)
        {
            System.err.println(nfe);
        }
        catch(IndexOutOfBoundsException ibe)
        {
            System.err.println(ibe);
        }
        catch(NullPointerException npe)
        {
            System.err.println(npe);
        }
        return returnColor;
    }

    Properties colorProperties;
    public static Border borderWindow;
    public static Border borderPanel;
    public static Border borderGroup;
    public static Border borderGroupTitle;
    public static Border borderTextBoxEditable;
    public static Border borderTextBoxNotEditable;
    public static Border borderTextBoxMandatory;
    public static Color windowBackGround;
    public static Color panelBackGround;
    public static Color panelForeGround;
    public static Color groupBackGround;
    public static Color groupForeGround;
    public static Color groupTitleBackGround;
    public static Color groupTitleForeGround;
    public static Color textBoxEditableBack;
    public static Color textBoxEditableFore;
    public static Color textBoxNotEditableBack;
    public static Color textBoxNotEditableFore;
    public static Color textBoxMandatoryBack;
    public static Color textBoxMandatoryFore;
    public static Color treeLevelOneColor;
    public static Color treeLevelTwoColor;
    public static Color treeLevelThreeColor;
    public static Color treeLevelFourColor;
    public static Color tableheaderColor;
    public static Color tableGridColor;
    public static Color toolbarBackgroundColor;
    public ColorUIResource acceleratorForegroundColor;
    public ColorUIResource acceleratorSelectedForegroundColor;
    public ColorUIResource controlColor;
    public ColorUIResource controlDarkShadowColor;
    public ColorUIResource controlDisabledColor;
    public ColorUIResource controlHighlightColor;
    public ColorUIResource controlInfoColor;
    public ColorUIResource controlShadowColor;
    public ColorUIResource controlTextColor;
    public ColorUIResource desktopColor;
    public ColorUIResource focusColor;
    public ColorUIResource highlightedTextColor;
    public ColorUIResource inactiveControlTextColor;
    public ColorUIResource inactiveSystemTextColor;
    public ColorUIResource menuBackgroundColor;
    public ColorUIResource menuDisabledForegroundColor;
    public ColorUIResource menuForegroundColor;
    public ColorUIResource menuSelectedBackgroundColor;
    public ColorUIResource menuSelectedForegroundColor;
    public ColorUIResource primaryControlColor;
    public ColorUIResource primaryControlDarkShadowColor;
    public ColorUIResource primaryControlHighlightColor;
    public ColorUIResource primaryControlInfoColor;
    public ColorUIResource primaryControlShadowColor;
    public ColorUIResource separatorBackgroundColor;
    public ColorUIResource separatorForegroundColor;
    public ColorUIResource systemTextColor;
    public ColorUIResource textHighlightColor;
    public ColorUIResource userTextColor;
    public ColorUIResource windowBackgroundColor;
    public ColorUIResource windowTitleBackgroundColor;
    public ColorUIResource windowTitleForegroundColor;
    public ColorUIResource windowTitleInactiveBackgroundColor;
    public ColorUIResource windowTitleInactiveForegroundColor;
    protected ColorUIResource blackColor;
    protected ColorUIResource whiteColor;
}
