// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Edge.java

package dyna.uic.graph;

import java.awt.Point;
import java.util.Observer;

// Referenced classes of package dyna.uic.graph:
//            Element, Node

public interface Edge
    extends Element, Observer
{

    public abstract void addNode(int i, Node node);

    public abstract void deleteNode(int i);

    public abstract Node getNode(int i);

    public abstract void updateGeometry();

    public abstract Point getEndLocation(int i);

    public static final int END1 = 0;
    public static final int END2 = 1;
}
