// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MWindowLocationManager.java

package dyna.uic;

import com.jgoodies.swing.BorderlessSplitPane;
import java.awt.*;
import javax.swing.*;

// Referenced classes of package dyna.uic:
//            MInternalFrame, MUtils

public class MWindowLocationManager
{

    public MWindowLocationManager(RootPaneContainer rootPane)
    {
        topComponent = null;
        centerComponent = null;
        leftComponent = null;
        bottomComponent = null;
        topTabbedPane = null;
        centerTabbedPane = null;
        leftTabbedPane = null;
        bottomTabbedPane = null;
        leftSplitPane = null;
        topSplitPane = null;
        bottomSplitPane = null;
        this.rootPane = null;
        this.rootPane = rootPane;
        uiInitialize();
    }

    private void uiInitialize()
    {
        if(rootPane == null)
            return;
        if(!(rootPane.getContentPane().getLayout() instanceof BorderLayout))
            rootPane.getContentPane().setLayout(new BorderLayout());
    }

    public void setFrame(RootPaneContainer rootPane)
    {
        this.rootPane = rootPane;
        uiInitialize();
    }

    public void clear()
    {
        topComponent = null;
        centerComponent = null;
        leftComponent = null;
        bottomComponent = null;
        leftSplitPane = null;
        topSplitPane = null;
        bottomSplitPane = null;
        rootPane = null;
    }

    public void add(int location, JComponent comp, String title, Icon icon, String tooltip)
    {
        switch(location)
        {
        case 1: // '\001'
            addTop(comp, title, icon, tooltip);
            break;

        case 2: // '\002'
            addCenter(comp, title, icon, tooltip);
            break;

        case 3: // '\003'
            addLeft(comp, title, icon, tooltip);
            break;

        case 4: // '\004'
            addBottom(comp, title, icon, tooltip);
            break;
        }
    }

    public void addTop(JComponent comp, String title, Icon icon, String tooltip)
    {
        if(topComponent != null)
        {
            JTabbedPane tabbedPane = (JTabbedPane)topComponent.getContent();
            if(tabbedPane != null)
                addComponentToTabbedPane(tabbedPane, title, icon, comp, tooltip);
            return;
        }
        topComponent = makeTabbedFrame(comp, title, icon, tooltip);
        if(centerComponent == null && leftComponent == null && bottomComponent == null)
            rootPane.getContentPane().add(topComponent, "Center");
        else
        if(centerComponent != null && leftComponent == null && bottomComponent == null)
        {
            makeTopSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, topComponent, topSplitPane, 0);
            topSplitPane.setBottomComponent(centerComponent);
            topSplitPane.setDividerLocation(topComponent.getPreferredSize().height);
        } else
        if(centerComponent == null && leftComponent != null && bottomComponent == null)
        {
            makeLeftSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, topComponent, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        } else
        if(centerComponent == null && leftComponent == null && bottomComponent != null)
        {
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, topComponent, bottomSplitPane, 0);
            bottomSplitPane.setBottomComponent(bottomComponent);
        } else
        if(centerComponent != null && leftComponent != null && bottomComponent == null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, topComponent, topSplitPane, 0);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
            topSplitPane.setDividerLocation(topComponent.getPreferredSize().height);
        } else
        if(centerComponent != null && leftComponent == null && bottomComponent != null)
        {
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomSplitPane, topSplitPane, 1);
            topSplitPane.setTopComponent(topComponent);
        } else
        if(centerComponent == null && leftComponent != null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, topComponent, bottomSplitPane, 0);
        } else
        if(centerComponent != null && leftComponent != null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, bottomSplitPane, topSplitPane, 1);
            topSplitPane.setTopComponent(topComponent);
        }
    }

    public void addCenter(JComponent comp, String title, Icon icon, String tooltip)
    {
        if(centerComponent != null)
        {
            JTabbedPane tabbedPane = (JTabbedPane)centerComponent.getContent();
            if(tabbedPane != null)
                addComponentToTabbedPane(tabbedPane, title, icon, comp, tooltip);
            return;
        }
        centerComponent = makeTabbedFrame(comp, title, icon, tooltip);
        if(topComponent == null && leftComponent == null && bottomComponent == null)
            rootPane.getContentPane().add(centerComponent, "Center");
        else
        if(topComponent != null && leftComponent == null && bottomComponent == null)
        {
            makeTopSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, centerComponent, topSplitPane, 1);
            topSplitPane.setTopComponent(topComponent);
            topSplitPane.setDividerLocation(topComponent.getPreferredSize().height);
        } else
        if(topComponent == null && leftComponent != null && bottomComponent == null)
        {
            makeLeftSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, centerComponent, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        } else
        if(topComponent == null && leftComponent == null && bottomComponent != null)
        {
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, centerComponent, bottomSplitPane, 0);
            bottomSplitPane.setBottomComponent(bottomComponent);
        } else
        if(topComponent != null && leftComponent != null && bottomComponent == null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, centerComponent, topSplitPane, 1);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
            topSplitPane.setDividerLocation(topComponent.getPreferredSize().height);
        } else
        if(topComponent != null && leftComponent == null && bottomComponent != null)
        {
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomSplitPane, topSplitPane, 1);
            topSplitPane.setTopComponent(topComponent);
            bottomSplitPane.setTopComponent(centerComponent);
        } else
        if(topComponent == null && leftComponent != null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, centerComponent, bottomSplitPane, 0);
        } else
        if(topComponent != null && leftComponent != null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, bottomSplitPane, topSplitPane, 1);
            bottomSplitPane.setTopComponent(centerComponent);
        }
    }

    public void addLeft(JComponent comp, String title, Icon icon, String tooltip)
    {
        if(leftComponent != null)
        {
            JTabbedPane tabbedPane = (JTabbedPane)leftComponent.getContent();
            if(tabbedPane != null)
                addComponentToTabbedPane(tabbedPane, title, icon, comp, tooltip);
            return;
        }
        leftComponent = makeTabbedFrame(comp, title, icon, tooltip);
        if(topComponent == null && centerComponent == null && bottomComponent == null)
            rootPane.getContentPane().add(leftComponent, "Center");
        else
        if(topComponent != null && centerComponent == null && bottomComponent == null)
        {
            makeLeftSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, leftComponent, leftSplitPane, 0);
            leftSplitPane.setRightComponent(topComponent);
        } else
        if(topComponent == null && centerComponent != null && bottomComponent == null)
        {
            makeLeftSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, leftComponent, leftSplitPane, 0);
            leftSplitPane.setRightComponent(centerComponent);
        } else
        if(topComponent == null && centerComponent == null && bottomComponent != null)
        {
            makeLeftSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, leftComponent, leftSplitPane, 0);
            leftSplitPane.setRightComponent(bottomComponent);
        } else
        if(topComponent != null && centerComponent != null && bottomComponent == null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, topSplitPane, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        } else
        if(topComponent != null && centerComponent == null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomSplitPane, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        } else
        if(topComponent == null && centerComponent != null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomSplitPane, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        } else
        if(topComponent != null && centerComponent != null && bottomComponent != null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, topSplitPane, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        }
    }

    public void addBottom(JComponent comp, String title, Icon icon, String tooltip)
    {
        if(bottomComponent != null)
        {
            JTabbedPane tabbedPane = (JTabbedPane)bottomComponent.getContent();
            if(tabbedPane != null)
                addComponentToTabbedPane(tabbedPane, title, icon, comp, tooltip);
            return;
        }
        bottomComponent = makeTabbedFrame(comp, title, icon, tooltip);
        if(topComponent == null && leftComponent == null && centerComponent == null)
            rootPane.getContentPane().add(bottomComponent, "North");
        else
        if(topComponent != null && leftComponent == null && centerComponent == null)
        {
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomComponent, bottomSplitPane, 1);
            bottomSplitPane.setTopComponent(topComponent);
        } else
        if(topComponent == null && leftComponent == null && centerComponent != null)
        {
            makeBottomSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomComponent, bottomSplitPane, 1);
            bottomSplitPane.setTopComponent(centerComponent);
        } else
        if(topComponent == null && leftComponent != null && centerComponent == null)
        {
            makeLeftSplitPane();
            MUtils.replaceComponentOfPaneWithSplit(rootPane, bottomComponent, leftSplitPane, 1);
            leftSplitPane.setLeftComponent(leftComponent);
            leftSplitPane.setDividerLocation(leftComponent.getPreferredSize().width);
        } else
        if(topComponent != null && leftComponent == null && centerComponent != null)
        {
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(topSplitPane, 1, bottomComponent, bottomSplitPane, 1);
        } else
        if(topComponent != null && leftComponent != null && centerComponent == null)
        {
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(topSplitPane, 1, bottomSplitPane, bottomSplitPane, 1);
        } else
        if(topComponent == null && leftComponent != null && centerComponent != null)
        {
            makeTopSplitPane();
            makeLeftSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(leftSplitPane, 1, bottomComponent, topSplitPane, 1);
            topSplitPane.setTopComponent(topComponent);
        } else
        if(topComponent != null && leftComponent != null && centerComponent != null)
        {
            makeLeftSplitPane();
            makeTopSplitPane();
            makeBottomSplitPane();
            MUtils.replaceComponentOfSplitWithSplit(topSplitPane, 1, bottomComponent, bottomSplitPane, 1);
        }
    }

    public JComponent get(int location, int index)
    {
        switch(location)
        {
        case 1: // '\001'
            return getTop(index);

        case 2: // '\002'
            return getCenter(index);

        case 3: // '\003'
            return getLeft(index);

        case 4: // '\004'
            return getBottom(index);
        }
        return null;
    }

    public JComponent get(int location)
    {
        return get(location, 0);
    }

    public JComponent getTop(int index)
    {
        return get(topComponent, index);
    }

    public JComponent getCenter(int index)
    {
        return get(centerComponent, index);
    }

    public JComponent getLeft(int index)
    {
        return get(leftComponent, index);
    }

    public JComponent getBottom(int index)
    {
        return get(bottomComponent, index);
    }

    private JComponent get(MInternalFrame internalFrame, int index)
    {
        if(internalFrame == null || index < 0)
            return null;
        JTabbedPane tabbedPane = (JTabbedPane)internalFrame.getContent();
        if(tabbedPane == null)
            return null;
        if(tabbedPane.getTabCount() < index)
            return null;
        else
            return (JComponent)tabbedPane.getComponentAt(index);
    }

    public JComponent get(int location, String title)
    {
        switch(location)
        {
        case 1: // '\001'
            return getTop(title);

        case 2: // '\002'
            return getCenter(title);

        case 3: // '\003'
            return getLeft(title);

        case 4: // '\004'
            return getBottom(title);
        }
        return null;
    }

    public JComponent getTop(String title)
    {
        return get(topComponent, title);
    }

    public JComponent getCenter(String title)
    {
        return get(centerComponent, title);
    }

    public JComponent getLeft(String title)
    {
        return get(leftComponent, title);
    }

    public JComponent getBottom(String title)
    {
        return get(bottomComponent, title);
    }

    private JComponent get(MInternalFrame internalFrame, String title)
    {
        if(internalFrame == null || title == null)
            return null;
        JTabbedPane tabbedPane = (JTabbedPane)internalFrame.getContent();
        if(tabbedPane == null)
            return null;
        if(tabbedPane.getTabCount() < 1)
            return null;
        for(int index = 0; index < tabbedPane.getTabCount(); index++)
            if(title.equals(tabbedPane.getTitleAt(index)))
                return (JComponent)tabbedPane.getComponentAt(index);

        return null;
    }

    public JSplitPane getLeftSplitPane()
    {
        return leftSplitPane;
    }

    public JSplitPane getTopSplitPane()
    {
        return topSplitPane;
    }

    public JSplitPane getBottomSplitPane()
    {
        return bottomSplitPane;
    }

    public JTabbedPane getTopTabbedPane()
    {
        if(topComponent == null)
        {
            return null;
        } else
        {
            JTabbedPane tabbedPane = (JTabbedPane)topComponent.getContent();
            return tabbedPane;
        }
    }

    public JTabbedPane getCenterTabbedPane()
    {
        if(centerComponent == null)
        {
            return null;
        } else
        {
            JTabbedPane tabbedPane = (JTabbedPane)centerComponent.getContent();
            return tabbedPane;
        }
    }

    public JTabbedPane getLeftTabbedPane()
    {
        if(leftComponent == null)
        {
            return null;
        } else
        {
            JTabbedPane tabbedPane = (JTabbedPane)leftComponent.getContent();
            return tabbedPane;
        }
    }

    public JTabbedPane getBottomTabbedPane()
    {
        if(bottomComponent == null)
        {
            return null;
        } else
        {
            JTabbedPane tabbedPane = (JTabbedPane)bottomComponent.getContent();
            return tabbedPane;
        }
    }

    private MInternalFrame makeTabbedFrame(JComponent comp, String title, Icon icon, String tooltip)
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        MInternalFrame internalFrame = new MInternalFrame(null, null, tabbedPane);
        addComponentToTabbedPane(tabbedPane, title, icon, comp, tooltip);
        tabbedPane = null;
        return internalFrame;
    }

    private void addComponentToTabbedPane(JTabbedPane tab, String title, Icon icon, JComponent comp, String tooltip)
    {
        if(tab == null)
            return;
        tab.addTab(title, icon, comp, tooltip);
        Dimension dim1 = tab.getPreferredSize();
        Dimension dim2 = tab.getMaximumSize();
        Dimension dim3 = null;
        Dimension dim4 = null;
        int maxWidth = 0;
        int maxHeight = 0;
        int width = 0;
        int height = 0;
        width = dim1.width <= 1600 ? dim1.width : 0;
        maxWidth = width;
        width = dim2.width <= 1600 ? dim2.width : 0;
        maxWidth = Math.max(width, maxWidth);
        height = dim1.height <= 1200 ? dim1.height : 0;
        maxHeight = height;
        height = dim2.height <= 1200 ? dim2.height : 0;
        maxHeight = Math.max(height, maxHeight);
        if(comp != null)
        {
            dim3 = comp.getPreferredSize();
            dim4 = comp.getMaximumSize();
            width = dim3.width <= 1600 ? dim3.width : 0;
            maxWidth = Math.max(width, maxWidth);
            width = dim4.width <= 1600 ? dim4.width : 0;
            maxWidth = Math.max(width, maxWidth);
            height = dim3.height <= 1200 ? dim3.height : 0;
            maxHeight = Math.max(height, maxHeight);
            height = dim4.height <= 1200 ? dim4.height : 0;
            maxHeight = Math.max(height, maxHeight);
        }
        dim1 = null;
        dim2 = null;
        dim3 = null;
        dim4 = null;
        dim1 = new Dimension(maxWidth, maxHeight);
        tab.setMaximumSize(dim1);
        tab.setPreferredSize(dim1);
        dim1 = null;
    }

    private void makeLeftSplitPane()
    {
        if(leftSplitPane == null)
        {
            leftSplitPane = new BorderlessSplitPane(1, null, null);
            leftSplitPane.setDividerSize(4);
            leftSplitPane.setDividerLocation(1.0D);
        }
    }

    private void makeTopSplitPane()
    {
        if(topSplitPane == null)
        {
            topSplitPane = new BorderlessSplitPane(0, null, null);
            topSplitPane.setDividerSize(4);
            topSplitPane.setDividerLocation(1.0D);
        }
    }

    private void makeBottomSplitPane()
    {
        if(bottomSplitPane == null)
        {
            bottomSplitPane = new BorderlessSplitPane(0, null, null);
            bottomSplitPane.setDividerSize(4);
            bottomSplitPane.setDividerLocation(1.0D);
        }
    }

    public static final int LOCATION_TOP = 1;
    public static final int LOCATION_CENTER = 2;
    public static final int LOCATION_LEFT = 3;
    public static final int LOCATION_BOTTOM = 4;
    public static final int LOCATION_POPUP = 100;
    public static final int LOCATION_RESULT = 110;
    private MInternalFrame topComponent;
    private MInternalFrame centerComponent;
    private MInternalFrame leftComponent;
    private MInternalFrame bottomComponent;
    private JTabbedPane topTabbedPane;
    private JTabbedPane centerTabbedPane;
    private JTabbedPane leftTabbedPane;
    private JTabbedPane bottomTabbedPane;
    private JSplitPane leftSplitPane;
    private JSplitPane topSplitPane;
    private JSplitPane bottomSplitPane;
    private RootPaneContainer rootPane;
}
