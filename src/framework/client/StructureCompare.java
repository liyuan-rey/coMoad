// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StructureCompare.java

package dyna.framework.client;

import dyna.uic.JTreeTable;
import dyna.uic.TreeNodeObject;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.framework.client:
//            CompareDiag

public class StructureCompare
{

    public StructureCompare(JTreeTable leftTreeTable, JTreeTable rightTreeTable)
    {
        this.leftTreeTable = null;
        this.rightTreeTable = null;
        leftTree = null;
        rightTree = null;
        leftDiagList = null;
        rightDiagList = null;
        diag = null;
        this.leftTreeTable = leftTreeTable;
        this.rightTreeTable = rightTreeTable;
    }

    public void run()
    {
        if(leftTreeTable == null || rightTreeTable == null)
        {
            return;
        } else
        {
            leftDiagList = initDiagList(leftDiagList, leftTreeTable.getRowCount());
            rightDiagList = initDiagList(rightDiagList, rightTreeTable.getRowCount());
            compareSegment(1, leftTreeTable.getRowCount() - 1, 1, rightTreeTable.getRowCount() - 1);
            return;
        }
    }

    private void compareSegment(int xMin, int xMax, int yMin, int yMax)
    {
        int xMid = -1;
        int xFMin = xMin;
        int xBMin = -1;
        int xFMax = -1;
        int xBMax = xMax;
        int xOffset = -1;
        int yOffset = -1;
        TreeNodeObject xNode = null;
        TreeNodeObject yNode = null;
        System.out.println(xMin + ", " + xMax + ", " + yMin + ", " + yMax);
        for(; xFMin < xBMax && yMin < yMax; yMin++)
        {
            leftTreeTable.tree.getClass();
            leftTreeTable.tree.getPathForRow(xFMin);
            leftTreeTable.tree.getPathForRow(xFMin).getLastPathComponent();
            xNode = (TreeNodeObject)((DefaultMutableTreeNode)leftTreeTable.tree.getPathForRow(xFMin).getLastPathComponent()).getUserObject();
            yNode = (TreeNodeObject)((DefaultMutableTreeNode)rightTreeTable.tree.getPathForRow(yMin).getLastPathComponent()).getUserObject();
            if(!compare(xNode, yNode, xFMin, yMin))
                break;
            xFMin++;
        }

        for(; xBMax > xFMin && yMax > yMin; yMax--)
        {
            xNode = (TreeNodeObject)((DefaultMutableTreeNode)leftTreeTable.tree.getPathForRow(xBMax).getLastPathComponent()).getUserObject();
            yNode = (TreeNodeObject)((DefaultMutableTreeNode)rightTreeTable.tree.getPathForRow(yMax).getLastPathComponent()).getUserObject();
            if(!compare(xNode, yNode, xBMax, yMax))
                break;
            xBMax--;
        }

        System.out.println("xFMin: " + xFMin + ", xBMax: " + xBMax + ", yMin: " + yMin + ", yMax: " + yMax);
        if(xMax == xFMin && xMin == xBMax)
        {
            System.out.println("[SAME]");
            return;
        }
        if(xFMin == xBMax)
        {
            System.out.println("[END OF LEFT TREE]");
            for(int i = yMin; i < yMax; i++)
            {
                diag = (CompareDiag)rightDiagList.get(i);
                diag.row = i;
                diag.otherRow = xFMin;
                diag.isChanged = true;
                diag.isDataChanged = false;
                diag.isNewItem = false;
            }

            return;
        } else
        {
            compareSegment(xFMin, xBMax, yMin, yMax);
            return;
        }
    }

    private boolean compare(TreeNodeObject xNode, TreeNodeObject yNode, int xRow, int yRow)
    {
        if(xNode == null || yNode == null)
            return false;
        return xNode.getOuid().equals(yNode.getOuid());
    }

    public ArrayList getLeftDiagList()
    {
        return leftDiagList;
    }

    public ArrayList getRightDiagList()
    {
        return rightDiagList;
    }

    private ArrayList initDiagList(ArrayList diagList, int count)
    {
        if(diagList == null)
            diagList = new ArrayList(count);
        else
            diagList.clear();
        for(int i = 0; i < count; i++)
            diagList.add(new CompareDiag(i));

        return diagList;
    }

    private JTreeTable leftTreeTable;
    private JTreeTable rightTreeTable;
    private JTree leftTree;
    private JTree rightTree;
    private ArrayList leftDiagList;
    private ArrayList rightDiagList;
    private CompareDiag diag;
}