// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaTableCellColor.java

package dyna.uic.table;

import dyna.uic.DynaTable;
import java.awt.Color;

public interface DynaTableCellColor
{

    public abstract Color getBackground(DynaTable dynatable, int i, int j);

    public abstract Color getForeground(DynaTable dynatable, int i, int j);
}
