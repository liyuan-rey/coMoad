// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSClassDatasource.java

package dyna.framework.service.dts;

import dyna.framework.service.NDS;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service.dts:
//            AbstractDatasource

public class DOSClassDatasource extends AbstractDatasource
{

    public DOSClassDatasource()
    {
        modelOuid = null;
        packageOuid = null;
        classOuid = null;
        isLeafClass = false;
    }

    public DOSClassDatasource(HashMap source)
    {
        modelOuid = null;
        packageOuid = null;
        classOuid = null;
        isLeafClass = false;
    }

    public boolean checkRequiredFields()
    {
        return false;
    }

    public boolean writeOneRow()
    {
        return false;
    }

    public void transfer()
    {
    }

    public void set(NDS nds1)
    {
    }

    public void add(NDS nds1)
    {
    }

    public String modelOuid;
    public String packageOuid;
    public String classOuid;
    public boolean isLeafClass;
}
