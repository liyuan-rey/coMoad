// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DTSFactory.java

package dyna.framework.service.dts;

import dyna.util.Utils;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service.dts:
//            CSVDatasource, XLSDatasource, JDBCDatasource, ODBCDatasource, 
//            DOSClassDatasource, DOSCodeDatasource, DOSClassReferenceDatasource, AbstractDatasource

public class DTSFactory
{

    public DTSFactory()
    {
    }

    public static AbstractDatasource createDatasource(HashMap source)
    {
        if(source == null || source.isEmpty())
            return null;
        String datasourceType = (String)source.get("datasourceType");
        if(Utils.isNullString(datasourceType))
            return null;
        if("CSVDatasource".equals(datasourceType))
            return new CSVDatasource(source);
        if("XLSDatasource".equals(datasourceType))
            return new XLSDatasource(source);
        if("JDBCDatasource".equals(datasourceType))
            return new JDBCDatasource(source);
        if("ODBCDatasource".equals(datasourceType))
            return new ODBCDatasource(source);
        if("DOSClasDatasource".equals(datasourceType))
            return new DOSClassDatasource(source);
        if("DOSCodeDatasource".equals(datasourceType))
            return new DOSCodeDatasource(source);
        if("DOSClassReferenceDatasource".equals(datasourceType))
            return new DOSClassReferenceDatasource(source);
        else
            return null;
    }
}
