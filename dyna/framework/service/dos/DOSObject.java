// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSObject.java

package dyna.framework.service.dos;

import java.sql.*;

public class DOSObject
{

    public DOSObject()
    {
        OUID = 0L;
        name = null;
        description = null;
    }

    public static long generateOUID(Connection con)
        throws SQLException
    {
        long returnValue = 0L;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int rows = 0;
        if(con == null)
            return 0L;
        stat = con.prepareStatement("update ouid set ouid=(select decode(ouid,0,2147483649,ouid+1) from ouid) ");
        rows = stat.executeUpdate();
        stat.close();
        stat = null;
        stat = con.prepareStatement("select ouid from ouid ");
        rs = stat.executeQuery();
        if(rs.next())
            returnValue = rs.getLong(1);
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        return returnValue;
    }

    public long OUID;
    public String name;
    public String description;
}
