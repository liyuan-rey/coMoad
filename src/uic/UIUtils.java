// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIUtils.java

package dyna.uic;

import java.applet.Applet;
import java.awt.*;
import java.util.Enumeration;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.*;

// Referenced classes of package dyna.uic:
//            TreeNodeObject, DynaTheme

public final class UIUtils
{
    private static class HorizontalTubePanel extends JPanel
    {

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(!isOpaque())
            {
                return;
            } else
            {
                int width = getWidth();
                int height = (int)((float)getHeight() * 0.5F);
                Graphics2D g2 = (Graphics2D)g;
                java.awt.Paint storedPaint = g2.getPaint();
                g2.setPaint(new GradientPaint(0.0F, 0.0F, DynaTheme.DARK_FINISH, 0.0F, height, DynaTheme.DARK_BEGIN));
                g2.fillRect(0, 0, width, height);
                g2.setPaint(new GradientPaint(0.0F, height, DynaTheme.BRIGHT_BEGIN, 0.0F, height + height, DynaTheme.BRIGHT_FINISH));
                g2.fillRect(0, height, width, height);
                g2.setPaint(storedPaint);
                g2 = null;
                return;
            }
        }

        HorizontalTubePanel(LayoutManager lm, Color background)
        {
            super(lm);
            setBackground(background);
        }
    }


    public UIUtils()
    {
    }

    public static JPanel createHorizontalTubePanel(LayoutManager lm, Color background)
    {
        return new HorizontalTubePanel(lm, background);
    }

    public static void setLocationRelativeTo(Window source, Component c)
    {
        Container root = null;
        if(source == null)
            return;
        if(c != null)
            if((c instanceof Window) || (c instanceof Applet))
            {
                root = (Container)c;
            } else
            {
                for(Container parent = c.getParent(); parent != null; parent = parent.getParent())
                {
                    if(!(parent instanceof Window) && !(parent instanceof Applet))
                        continue;
                    root = parent;
                    break;
                }

            }
        if(c != null && !c.isShowing() || root == null || !root.isShowing())
        {
            Dimension paneSize = source.getSize();
            Dimension screenSize = source.getToolkit().getScreenSize();
            source.setLocation((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
        } else
        {
            Dimension invokerSize = c.getSize();
            Point invokerScreenLocation;
            if(root instanceof Applet)
            {
                invokerScreenLocation = c.getLocationOnScreen();
            } else
            {
                invokerScreenLocation = new Point(0, 0);
                for(Component tc = c; tc != null; tc = tc.getParent())
                {
                    Point tcl = tc.getLocation();
                    invokerScreenLocation.x += tcl.x;
                    invokerScreenLocation.y += tcl.y;
                    if(tc == root)
                        break;
                }

            }
            Rectangle windowBounds = source.getBounds();
            Dimension ss = source.getToolkit().getScreenSize();
            int dx = 0;
            int dy = 0;
            int temp = 50;
            if(invokerScreenLocation.x + (invokerSize.width >> 1) < ss.width >> 1)
            {
                dx = invokerScreenLocation.x + invokerSize.width;
                if(dx + windowBounds.width > ss.width - temp)
                    dx = ss.width - temp - windowBounds.width;
            } else
            {
                dx = invokerScreenLocation.x - windowBounds.width;
                if(dx < 100)
                    dx = 100;
            }
            dy = invokerScreenLocation.y + (invokerSize.height - windowBounds.height >> 1);
            if(dy + windowBounds.height > ss.height - temp)
                dy = ss.height - temp - windowBounds.height;
            else
            if(dy < 100)
                dy = 100;
            source.setLocation(dx, dy);
        }
    }

    public static DefaultMutableTreeNode getRealSelectedNode(JTree tree, DefaultMutableTreeNode node)
    {
        if(tree == null || node == null)
            return null;
        Object path[] = node.getUserObjectPath();
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
        DefaultMutableTreeNode childNode = null;
        int j = 0;
        int c2 = 0;
        String name = null;
        int i = 1;
        for(int count = path.length; i < count; i++)
        {
            name = ((TreeNodeObject)path[i]).getName();
            j = 0;
            for(c2 = treeNode.getChildCount(); j < c2; j++)
            {
                childNode = (DefaultMutableTreeNode)treeNode.getChildAt(j);
                if(childNode == null || !childNode.toString().equals(name))
                    continue;
                treeNode = childNode;
                break;
            }

        }

        return treeNode;
    }

    public static DefaultMutableTreeNode expandTreeLevel(JTree tree, DefaultMutableTreeNode node, int levelLimit)
    {
        if(tree == null || node == null)
            return null;
        DefaultMutableTreeNode rootNode = getRealSelectedNode(tree, node);
        DefaultMutableTreeNode treeNode = rootNode;
        DefaultMutableTreeNode childNode = null;
        TreePath treePath = null;
        Enumeration enum = null;
        int level = 0;
        if(levelLimit < 1)
            levelLimit = 200;
        else
            levelLimit--;
        Stack stack = new Stack();
        stack.push(treeNode);
        while(!stack.isEmpty()) 
        {
            treeNode = (DefaultMutableTreeNode)stack.pop();
            treePath = new TreePath(treeNode.getPath());
            tree.setSelectionPath(treePath);
            tree.expandPath(treePath);
            treePath = null;
            level = treeNode.getLevel();
            if(level < levelLimit)
            {
                for(enum = treeNode.children(); enum.hasMoreElements();)
                {
                    childNode = (DefaultMutableTreeNode)enum.nextElement();
                    stack.push(childNode);
                    childNode = null;
                }

                enum = null;
            }
        }
        tree.setSelectionPath(new TreePath(node.getPath()));
        return rootNode;
    }

    public static void expandTreeLevel(JTree tree, int levelLimit)
    {
        if(tree == null)
        {
            return;
        } else
        {
            expandTreeLevel(tree, (DefaultMutableTreeNode)tree.getModel().getRoot(), levelLimit);
            return;
        }
    }

    public static DefaultMutableTreeNode expandTree1Level(JTree tree, DefaultMutableTreeNode node)
    {
        if(tree == null || node == null)
        {
            return null;
        } else
        {
            DefaultMutableTreeNode treeNode = expandTreeLevel(tree, node, 1);
            return treeNode;
        }
    }

    public static void expandTree1Level(JTree tree)
    {
        if(tree == null)
        {
            return;
        } else
        {
            expandTree1Level(tree, (DefaultMutableTreeNode)tree.getModel().getRoot());
            return;
        }
    }

    public static DefaultMutableTreeNode expandTreeFullLevel(JTree tree, DefaultMutableTreeNode node)
    {
        if(tree == null || node == null)
        {
            return null;
        } else
        {
            DefaultMutableTreeNode treeNode = expandTreeLevel(tree, node, 0);
            return treeNode;
        }
    }

    public static void expandTreeFullLevel(JTree tree)
    {
        if(tree == null)
        {
            return;
        } else
        {
            expandTreeFullLevel(tree, (DefaultMutableTreeNode)tree.getModel().getRoot());
            return;
        }
    }

    public static void scrollTreePathToVisible(JTree tree, DefaultMutableTreeNode node)
    {
        if(tree == null || node == null)
        {
            return;
        } else
        {
            DefaultMutableTreeNode treeNode = expandTree1Level(tree, node);
            tree.scrollPathToVisible(new TreePath(treeNode.getPath()));
            return;
        }
    }
}
