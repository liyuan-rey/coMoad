// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Node.java

package dyna.uic.graph;

import java.awt.*;
import java.util.ArrayList;

// Referenced classes of package dyna.uic.graph:
//            Element, Edge

public interface Node
    extends Element
{

    public abstract void setMaximumSize(Dimension dimension);

    public abstract Dimension getMaximumSize();

    public abstract void setMinimumSize(Dimension dimension);

    public abstract Dimension getMinimumSize();

    public abstract void setPreferredSize(Dimension dimension);

    public abstract Dimension getPreferredSize();

    public abstract Rectangle getBounds();

    public abstract void setSize(Dimension dimension);

    public abstract void setLocation(int i, int j);

    public abstract void setLocation(Point point);

    public abstract void grow(int i, int j);

    public abstract void grow(Dimension dimension);

    public abstract boolean intersects(Rectangle rectangle);

    public abstract void add(int i, int j);

    public abstract void add(Point point);

    public abstract void setChanged(boolean flag);

    public abstract boolean isChanged();

    public abstract void addEdge1(Edge edge);

    public abstract void addEdge2(Edge edge);

    public abstract void deleteEdge1(Edge edge);

    public abstract void deleteEdge2(Edge edge);

    public abstract void deleteAllEdges();

    public abstract ArrayList getEdges1();

    public abstract ArrayList getEdges2();

    public abstract int countEdges1();

    public abstract int countEdges2();
}
