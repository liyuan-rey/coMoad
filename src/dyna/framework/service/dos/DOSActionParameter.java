// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSActionParameter.java

package dyna.framework.service.dos;


// Referenced classes of package dyna.framework.service.dos:
//            DOSAction, DOSField

public class DOSActionParameter
{

    public DOSActionParameter()
    {
        name = null;
        action = null;
        type = null;
        defaultValue = null;
        fieldMapping = null;
    }

    public String name;
    public DOSAction action;
    public Byte type;
    public Object defaultValue;
    public DOSField fieldMapping;
}
