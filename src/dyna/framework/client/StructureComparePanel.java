// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StructureComparePanel.java

package dyna.framework.client;

import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.service.DOS;
import dyna.uic.JTreeTable;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            StructureCompare, DynaMOAD, UIGeneration, UIBuilder, 
//            ListenerBuilder, CompareDiag

public class StructureComparePanel extends JPanel
    implements ComponentListener, AdjustmentListener
{
    private class DiffGraphPanel extends JPanel
    {

        public void setOffsetPosition(int p1, int p2)
        {
            offset1 = p1;
            offset2 = p2;
        }

        public void setLeftOffsetPosition(int p)
        {
            offset1 = p;
        }

        public void setRightOffsetPosition(int p)
        {
            offset2 = p;
        }

        public void paint(Graphics g)
        {
            super.paint(g);
            if(StructureComparePanel.this.leftDiagList == null || StructureComparePanel.this.rightDiagList == null)
                return;
            leftDiagList = StructureComparePanel.this.leftDiagList;
            rightDiagList = StructureComparePanel.this.rightDiagList;
            leftPainter.clearRectList();
            rightPainter.clearRectList();
            int w = getWidth();
            int h = getHeight();
            int leftW = leftTreeScroll.getWidth();
            int rightW = rightTreeScroll.getWidth() - rightTreeScroll.getVerticalScrollBar().getWidth() - 1;
            int countTotal = treeTableRight.getRowCount();
            int rowHeight = treeTableRight.getRowHeight();
            int headerHeight = scrollPaneRight.getColumnHeader().getHeight();
            int x1 = 0;
            int x2 = w;
            int xMid = w / 2;
            int y1 = headerHeight;
            int y2 = y1;
            Graphics2D g2 = (Graphics2D)g;
            int i = 0;
            int j = 0;
            for(; i < leftDiagList.size(); i++)
            {
                leftDiag = (CompareDiag)leftDiagList.get(i);
                rightDiag = (CompareDiag)rightDiagList.get(j);
                if(leftDiag.otherRow == -1 && rightDiag.otherRow == -1)
                {
                    j++;
                } else
                {
                    y1 = (i * rowHeight + headerHeight) - offset1;
                    y2 = ((leftDiag.otherRow != -1 ? leftDiag.otherRow : rightDiag.row) * rowHeight + headerHeight) - offset2;
                    curve.setCurve(x1, y1, xMid, y1, xMid, y2, x2, y2);
                    g2.draw(curve);
                    rect1 = new Rectangle();
                    rect2 = new Rectangle();
                    rect1.x = 0;
                    rect1.y = i * rowHeight;
                    rect2.x = 0;
                    rect2.y = (leftDiag.otherRow != -1 ? leftDiag.otherRow : rightDiag.row) * rowHeight;
                    for(j = rightDiag.otherRow; leftDiag.row == rightDiag.otherRow && j < rightDiagList.size(); rightDiag = (CompareDiag)rightDiagList.get(j))
                        j++;

                    y1 = (i * rowHeight + headerHeight) - offset1;
                    y2 = (j * rowHeight + headerHeight) - offset2;
                    curve.setCurve(x1, y1, xMid, y1, xMid, y2, x2, y2);
                    g2.draw(curve);
                    rect1.width = leftTree.getWidth();
                    rect1.height = i * rowHeight - rect1.y;
                    rect2.width = rightTree.getWidth();
                    rect2.height = j * rowHeight - rect2.y;
                    leftPainter.addRect(rect1);
                    rightPainter.addRect(rect2);
                }
            }

            g2 = null;
        }

        JScrollPane scrollPaneLeft;
        JTreeTable treeTableLeft;
        JScrollPane scrollPaneRight;
        JTreeTable treeTableRight;
        CubicCurve2D curve;
        ArrayList leftDiagList;
        ArrayList rightDiagList;
        CompareDiag leftDiag;
        CompareDiag rightDiag;
        Rectangle rect1;
        Rectangle rect2;
        int offset1;
        int offset2;

        DiffGraphPanel()
        {
            scrollPaneLeft = leftTreeScroll;
            treeTableLeft = leftTree;
            scrollPaneRight = rightTreeScroll;
            treeTableRight = rightTree;
            curve = new java.awt.geom.CubicCurve2D.Double();
            leftDiagList = null;
            rightDiagList = null;
            leftDiag = null;
            rightDiag = null;
            rect1 = null;
            rect2 = null;
            offset1 = 0;
            offset2 = 0;
        }
    }

    private class ExtraPainter extends JLabel
    {

        public void setRectList(ArrayList rectList)
        {
            this.rectList = rectList;
        }

        public void addRect(Rectangle rect)
        {
            if(rectList == null)
                rectList = new ArrayList();
            rectList.add(rect);
        }

        public void clearRectList()
        {
            if(rectList == null)
                rectList = new ArrayList();
            rectList.clear();
        }

        public void paint(Graphics g)
        {
            if(Utils.isNullArrayList(rectList))
                return;
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(diffColor);
            for(int i = 0; i < rectList.size(); i++)
            {
                rect = (Rectangle)rectList.get(i);
                g2.fillRect(rect.x, rect.y, rect.width, rect.height);
                g2.setColor(Color.black);
                g2.drawLine(rect.x, rect.y, rect.width, rect.y);
                g2.drawLine(rect.x, rect.y + rect.height, rect.width, rect.y + rect.height);
            }

            g2 = null;
        }

        public void repaint(long l, int i, int j, int k, int i1)
        {
        }

        private ArrayList rectList;
        Rectangle rect;
        Color diffColor;

        public ExtraPainter()
        {
            rectList = null;
            rect = null;
            diffColor = new Color(250, 250, 0, 32);
        }
    }


    public StructureComparePanel(String leftOuid, String rightOuid)
    {
        this.leftOuid = null;
        this.rightOuid = null;
        leftScrollBar = null;
        rightScrollBar = null;
        structureCompare = null;
        leftDiagList = null;
        rightDiagList = null;
        mainSplit = null;
        diffTreeScroll = null;
        diffTree = null;
        diffSplit1 = null;
        diffSplit2 = null;
        leftTreeScroll = null;
        leftTree = null;
        rightTreeScroll = null;
        rightTree = null;
        diffGraphPanel = null;
        leftPainter = null;
        rightPainter = null;
        this.leftOuid = leftOuid;
        this.rightOuid = rightOuid;
        initialize();
        structureCompare = new StructureCompare(leftTree, rightTree);
        structureCompare.run();
        leftDiagList = structureCompare.getLeftDiagList();
        rightDiagList = structureCompare.getRightDiagList();
    }

    private void initialize()
    {
        String classOuid = null;
        setLayout(new BorderLayout(2, 2));
        mainSplit = new BorderlessSplitPane(0, null, null);
        add(mainSplit);
        diffTree = new JTree();
        diffTreeScroll = UIFactory.createStrippedScrollPane(diffTree);
        diffSplit1 = new BorderlessSplitPane(1, null, null);
        diffSplit1.setDividerSize(0);
        mainSplit.setBottomComponent(diffSplit1);
        diffSplit2 = new BorderlessSplitPane(1, null, null);
        diffSplit2.setDividerSize(0);
        diffSplit1.setRightComponent(diffSplit2);
        leftTreeScroll = UIFactory.createStrippedScrollPane(null);
        leftTreeScroll.setVerticalScrollBarPolicy(21);
        diffSplit1.setLeftComponent(leftTreeScroll);
        leftPainter = new ExtraPainter();
        try
        {
            classOuid = DynaMOAD.dos.getClassOuid(leftOuid);
            leftTree = UIBuilder.createLinkTableTree(DynaMOAD.dos, leftOuid, classOuid, DynaMOAD.dos.get(leftOuid), 0, UIGeneration.listAssociations(classOuid), null);
            leftTree.addTreeSelectionListener(ListenerBuilder.getObjectStructureTreeSelectoinListener(leftOuid, null, 0, leftTree));
            leftTree.addComponentListener(this);
            leftTree.setExtraPainter(leftPainter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        leftTreeScroll.setViewportView(leftTree);
        rightTreeScroll = UIFactory.createStrippedScrollPane(null);
        diffSplit2.setRightComponent(rightTreeScroll);
        rightPainter = new ExtraPainter();
        try
        {
            classOuid = DynaMOAD.dos.getClassOuid(rightOuid);
            rightTree = UIBuilder.createLinkTableTree(DynaMOAD.dos, rightOuid, classOuid, DynaMOAD.dos.get(rightOuid), 0, UIGeneration.listAssociations(classOuid), null);
            rightTree.addTreeSelectionListener(ListenerBuilder.getObjectStructureTreeSelectoinListener(rightOuid, null, 0, rightTree));
            rightTree.addComponentListener(this);
            rightTree.setExtraPainter(rightPainter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        rightTreeScroll.setViewportView(rightTree);
        if(leftTree != null)
            UIUtils.expandTreeFullLevel(leftTree.tree);
        if(rightTree != null)
            UIUtils.expandTreeFullLevel(rightTree.tree);
        diffGraphPanel = new DiffGraphPanel();
        diffSplit2.setLeftComponent(diffGraphPanel);
        addComponentListener(this);
    }

    public void setLeftOuid(String ouid)
    {
        leftOuid = ouid;
    }

    public void setRightOuid(String ouid)
    {
        rightOuid = ouid;
    }

    public void componentHidden(ComponentEvent componentevent)
    {
    }

    public void componentMoved(ComponentEvent componentevent)
    {
    }

    public void componentResized(ComponentEvent e)
    {
        if(e.getSource() instanceof JPanel)
        {
            if(diffSplit1 == null || diffSplit2 == null)
                return;
            Dimension dim = getSize();
            if(dim.width > 32)
            {
                diffSplit1.setDividerLocation((int)((double)dim.width * 0.5D) - 15);
                diffSplit2.setDividerLocation(30);
            }
            JScrollBar scrollBar = leftTreeScroll.getVerticalScrollBar();
            if(scrollBar != null)
            {
                if(leftScrollBar == null)
                {
                    leftScrollBar = scrollBar;
                    leftScrollBar.addAdjustmentListener(this);
                }
            } else
            if(leftScrollBar != null)
            {
                leftScrollBar.removeAdjustmentListener(this);
                leftScrollBar = null;
            }
            scrollBar = rightTreeScroll.getVerticalScrollBar();
            if(scrollBar != null)
            {
                if(rightScrollBar == null)
                {
                    rightScrollBar = scrollBar;
                    rightScrollBar.addAdjustmentListener(this);
                }
            } else
            if(rightScrollBar != null)
            {
                rightScrollBar.removeAdjustmentListener(this);
                rightScrollBar = null;
            }
            scrollBar = null;
        } else
        {
            if(structureCompare == null)
                return;
            structureCompare.run();
            leftDiagList = structureCompare.getLeftDiagList();
            rightDiagList = structureCompare.getRightDiagList();
            if(diffGraphPanel == null)
                return;
            diffGraphPanel.repaint();
        }
    }

    public void componentShown(ComponentEvent componentevent)
    {
    }

    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(diffGraphPanel != null)
        {
            if(e.getSource() == leftTreeScroll.getVerticalScrollBar())
                diffGraphPanel.setLeftOffsetPosition(leftTreeScroll.getVerticalScrollBar().getValue());
            else
            if(e.getSource() == rightTreeScroll.getVerticalScrollBar())
                diffGraphPanel.setRightOffsetPosition(rightTreeScroll.getVerticalScrollBar().getValue());
            diffGraphPanel.repaint();
        }
    }

    private String leftOuid;
    private String rightOuid;
    private JScrollBar leftScrollBar;
    private JScrollBar rightScrollBar;
    private StructureCompare structureCompare;
    private ArrayList leftDiagList;
    private ArrayList rightDiagList;
    private JSplitPane mainSplit;
    private JScrollPane diffTreeScroll;
    private JTree diffTree;
    private JSplitPane diffSplit1;
    private JSplitPane diffSplit2;
    private JScrollPane leftTreeScroll;
    private JTreeTable leftTree;
    private JScrollPane rightTreeScroll;
    private JTreeTable rightTree;
    private DiffGraphPanel diffGraphPanel;
    private ExtraPainter leftPainter;
    private ExtraPainter rightPainter;








}