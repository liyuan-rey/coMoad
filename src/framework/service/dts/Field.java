// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Field.java

package dyna.framework.service.dts;


public class Field
{

    public Field()
    {
        name = null;
        dataType = 0;
        dataTyleClass = 0L;
        isMandatory = false;
        defaultValue = null;
        filterString = null;
    }

    public String name;
    public byte dataType;
    public long dataTyleClass;
    public boolean isMandatory;
    public String defaultValue;
    public String filterString;
}
