// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Element.java

package dyna.uic.graph;

import java.awt.*;

public interface Element
{

    public abstract void setMode(int i);

    public abstract int getMode();

    public abstract void setSelected(boolean flag);

    public abstract boolean isSelected();

    public abstract void setOid(String s);

    public abstract String getOid();

    public abstract void setIdentifier(String s);

    public abstract String getIdentifier();

    public abstract void setName(String s);

    public abstract String getName();

    public abstract void setUserData(Object obj);

    public abstract Object getUserData();

    public abstract Point getLocation(Point point);

    public abstract int getX();

    public abstract int getY();

    public abstract Dimension getSize(Dimension dimension);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getCenterX();

    public abstract int getCenterY();

    public abstract Point getCenterLocation(Point point);

    public abstract boolean contains(int i, int j);

    public abstract boolean contains(Point point);

    public abstract void translate(int i, int j);

    public abstract void translate(Point point);

    public abstract Object getProperty(String s);

    public abstract void setProperty(String s, Object obj);

    public abstract void paint(Graphics g);

    public abstract void update(Graphics g);

    public static final int MODE_NONE = 1;
    public static final int MODE_NORMAL = 2;
    public static final int MODE_HIGHLIT = 3;
    public static final int MODE_WARN = 4;
    public static final int MODE_DIMM = 5;
    public static final int MODE_BEFORE = 6;
    public static final int MODE_AFTER = 7;
    public static final int MODE_ACCEPT = 8;
    public static final int MODE_ABORTED = 9;
}
