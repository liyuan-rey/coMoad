// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TreeNodeObject.java

package dyna.uic;

import java.util.ArrayList;

public class TreeNodeObject
{

    public TreeNodeObject(String ouid, String name, String description)
    {
        this.ouid = null;
        this.name = null;
        this.description = null;
        object = null;
        isPopulated = false;
        this.ouid = ouid;
        this.name = name;
        this.description = description;
    }

    public String getOuid()
    {
        return ouid;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public ArrayList getData()
    {
        ArrayList LocVct = new ArrayList(3);
        LocVct.add(ouid);
        LocVct.add(name);
        LocVct.add(description);
        LocVct.trimToSize();
        return LocVct;
    }

    public String toString()
    {
        return name;
    }

    public boolean isPopulated()
    {
        return isPopulated;
    }

    public void setPopulated(boolean isPopulated)
    {
        this.isPopulated = isPopulated;
    }

    public Object getLawData()
    {
        return object;
    }

    public void setLawData(Object object)
    {
        this.object = object;
    }

    String ouid;
    String name;
    String description;
    private Object object;
    private boolean isPopulated;
}
