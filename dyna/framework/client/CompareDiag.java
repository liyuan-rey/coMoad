// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CompareDiag.java

package dyna.framework.client;


public class CompareDiag
{

    public CompareDiag()
    {
        isChanged = false;
        isNewItem = false;
        isDataChanged = false;
        row = -1;
        otherRow = -1;
        row = -1;
        otherRow = -1;
    }

    public CompareDiag(int row)
    {
        isChanged = false;
        isNewItem = false;
        isDataChanged = false;
        this.row = -1;
        otherRow = -1;
        this.row = row;
        otherRow = -1;
    }

    public CompareDiag(int row, int otherRow)
    {
        isChanged = false;
        isNewItem = false;
        isDataChanged = false;
        this.row = -1;
        this.otherRow = -1;
        this.row = row;
        this.otherRow = otherRow;
    }

    boolean isChanged;
    boolean isNewItem;
    boolean isDataChanged;
    int row;
    int otherRow;
}