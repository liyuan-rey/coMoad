// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSCodeDatasource.java

package dyna.framework.service.dts;

import dyna.framework.service.NDS;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service.dts:
//            AbstractDatasource

public class DOSCodeDatasource extends AbstractDatasource
{

    public DOSCodeDatasource(HashMap source)
    {
        isHierarchyCode = false;
        codeOuid = null;
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

    public boolean isHierarchyCode;
    public String codeOuid;
}
