// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MComboBoxModelObject.java

package dyna.uic;


public class MComboBoxModelObject
{

    public MComboBoxModelObject(String ouid, String code, String name, String description)
    {
        this.ouid = null;
        this.code = null;
        this.name = null;
        this.description = null;
        this.ouid = ouid;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getOuid()
    {
        return ouid;
    }

    public void setCode(String string)
    {
        code = string;
    }

    public void setDescription(String string)
    {
        description = string;
    }

    public void setName(String string)
    {
        name = string;
    }

    public void setOuid(String string)
    {
        ouid = string;
    }

    public String toString()
    {
        if(code == null)
            return name;
        else
            return name + " [" + code + "]";
    }

    private String ouid;
    private String code;
    private String name;
    private String description;
}
