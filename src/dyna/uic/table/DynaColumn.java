// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaColumn.java

package dyna.uic.table;


public class DynaColumn
{

    public DynaColumn(String identifier, String name, String field)
    {
        this.identifier = null;
        this.name = null;
        this.field = null;
        width = 100;
        valueGrouping = false;
        filterActivated = false;
        filterComparator = -1;
        filterCompareValue = null;
        if(identifier != null)
            this.identifier = new String(identifier);
        if(name != null)
            this.name = new String(name);
        if(field != null)
            this.field = new String(field);
    }

    public DynaColumn(String identifier, String name, String field, int width)
    {
        this.identifier = null;
        this.name = null;
        this.field = null;
        this.width = 100;
        valueGrouping = false;
        filterActivated = false;
        filterComparator = -1;
        filterCompareValue = null;
        if(identifier != null)
            this.identifier = new String(identifier);
        if(name != null)
            this.name = new String(name);
        if(field != null)
            this.field = new String(field);
        this.width = width;
    }

    public String getIdentifier()
    {
        if(identifier != null)
            return new String(identifier);
        else
            return null;
    }

    public void setIdentifier(String identifier)
    {
        if(identifier != null)
            this.identifier = new String(identifier);
        else
            this.identifier = null;
    }

    public String getName()
    {
        if(name != null)
            return new String(name);
        else
            return null;
    }

    public void setName(String name)
    {
        if(name != null)
            this.name = new String(name);
        else
            this.name = null;
    }

    public String getField()
    {
        if(field != null)
            return new String(field);
        else
            return null;
    }

    public void setField(String field)
    {
        if(field != null)
            this.field = new String(field);
        else
            this.field = null;
    }

    public boolean isValueGrouped()
    {
        return valueGrouping;
    }

    public void setValueGrouped(boolean flag)
    {
        valueGrouping = flag;
    }

    public boolean isFilterActivated()
    {
        return filterActivated;
    }

    public void setFilterActivated(boolean flag)
    {
        filterActivated = flag;
    }

    public int getFilterComparator()
    {
        return filterComparator;
    }

    public void setFilterComparator(int filterComparator)
    {
        this.filterComparator = filterComparator;
    }

    public String getFilterCompareValue()
    {
        if(filterCompareValue != null)
            return filterCompareValue;
        else
            return null;
    }

    public void setFilterCompareValue(String filterCompareValue)
    {
        if(filterCompareValue != null)
            this.filterCompareValue = new String(filterCompareValue);
        else
            this.filterCompareValue = null;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    private String identifier;
    private String name;
    private String field;
    private int width;
    private boolean valueGrouping;
    private boolean filterActivated;
    private int filterComparator;
    private String filterCompareValue;
}
