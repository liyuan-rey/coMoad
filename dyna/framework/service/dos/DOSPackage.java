// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSPackage.java

package dyna.framework.service.dos;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DTM;
import dyna.util.DTMUtil;
import dyna.util.Utils;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service.dos:
//            DOSObject, DOSChangeable, DOSModel

public class DOSPackage extends DOSObject
{

    public DOSPackage()
    {
        modelMap = null;
        classMap = null;
        isGlobal = false;
        modelMap = new TreeMap();
        classMap = new TreeMap();
    }

    public static String createPackage(String modelOuid, DOSChangeable packageDefinition, DTM dtm, HashMap dosPackageMap, HashMap modelMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        String returnString = null;
        long returnLong = 0L;
        int rows = 0;
        Boolean isGlobal = null;
        if(packageDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + packageDefinition);
        valueMap = packageDefinition.getValueMap();
        if(Utils.isNullString(modelOuid) || valueMap == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dospkg (ouid,name,global,des) values (?,?,?,?) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            Boolean isGlobal = (Boolean)valueMap.get("isGlobal");
            if(isGlobal == null)
                isGlobal = Boolean.FALSE;
            Utils.setBoolean(stat, 3, isGlobal);
            stat.setString(4, (String)valueMap.get("description"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("insert into dosmodpkg (dosmod, dospkg ) values ( ?, ? ) ");
            stat.setLong(1, Utils.convertOuidToLong(modelOuid));
            stat.setLong(2, returnLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSPackage dosPackage = new DOSPackage();
            dosPackage.OUID = returnLong;
            dosPackage.name = (String)valueMap.get("name");
            dosPackage.isGlobal = Utils.getBoolean(isGlobal);
            dosPackage.description = (String)valueMap.get("description");
            dosPackageMap.put(returnString, dosPackage);
            TreeMap packageMap = null;
            DOSModel dosModel = (DOSModel)modelMap.get(modelOuid);
            if(dosModel != null)
            {
                packageMap = dosModel.packageMap;
                if(packageMap == null)
                {
                    packageMap = new TreeMap();
                    dosModel.packageMap = packageMap;
                }
                packageMap.put(dosPackage.name, dosPackage);
                packageMap = null;
                packageMap = dosPackage.modelMap;
                if(packageMap == null)
                {
                    packageMap = new TreeMap();
                    dosPackage.modelMap = packageMap;
                }
                packageMap.put(dosModel.name, dosModel);
                packageMap = null;
                dosModel = null;
            }
            if(Boolean.TRUE.equals(isGlobal))
            {
                for(Iterator modelKey = modelMap.keySet().iterator(); modelKey.hasNext();)
                {
                    dosModel = (DOSModel)modelMap.get(modelKey.next());
                    packageMap = dosModel.packageMap;
                    if(packageMap != null)
                        packageMap.put(dosPackage.name, dosPackage);
                    dosModel = null;
                    packageMap = null;
                }

                Object obj = null;
            }
            dosPackage = null;
            valueMap = null;
            s = returnString;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        if(con != null)
            try
            {
                con.rollback();
            }
            catch(SQLException sqlexception) { }
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public TreeMap modelMap;
    public TreeMap classMap;
    public boolean isGlobal;
}
