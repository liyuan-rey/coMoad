// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ODBCDatasource.java

package dyna.framework.service.dts;

import java.util.HashMap;

// Referenced classes of package dyna.framework.service.dts:
//            JDBCDatasource

public class ODBCDatasource extends JDBCDatasource
{

    public ODBCDatasource(HashMap source)
    {
        datasourceName = null;
        attributes = null;
        classPath = "sun.jdbc.odbc.JdbcOdbcDriver";
    }

    public String datasourceName;
    public String attributes;
    private String classPath;
}
