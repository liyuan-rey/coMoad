// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JDBCDatasource.java

package dyna.framework.service.dts;

import dyna.framework.service.NDS;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service.dts:
//            AbstractDatasource

public class JDBCDatasource extends AbstractDatasource
{

    public JDBCDatasource()
    {
        classPath = null;
        connectionString = null;
        user = null;
        password = null;
    }

    public JDBCDatasource(HashMap source)
    {
        classPath = null;
        connectionString = null;
        user = null;
        password = null;
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

    public String classPath;
    public String connectionString;
    public String user;
    public String password;
}
