// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaComponent.java

package dyna.uic;


public interface DynaComponent
{

    public abstract String getField();

    public abstract void setField(String s);

    public abstract String getIdentifier();

    public abstract void setIdentifier(String s);

    public abstract String getName();

    public abstract void setName(String s);

    public abstract String getTitleText();

    public abstract void setTitleText(String s);

    public abstract boolean isMandatory();

    public abstract void setMandatory(boolean flag);

    public abstract boolean isEditable();

    public abstract void setEditable(boolean flag);

    public abstract String getDataType();

    public abstract void setDataType(String s);

    public abstract double getDataSize();

    public abstract void setDataSize(double d);
}
