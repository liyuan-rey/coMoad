// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NDSNode.java

package dyna.framework.service.nds;

import java.io.Serializable;
import java.util.Map;

public class NDSNode
    implements Serializable
{

    public NDSNode()
    {
    }

    public String key;
    public String type;
    public Object value;
    public String description;
    public Map children;
    public NDSNode parentNode;
    public Map attributes;
    public int depth;
    public String path;
}
