// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSModel.java

package dyna.framework.service.dos;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DTM;
import dyna.framework.service.NDS;
import dyna.util.DTMUtil;
import dyna.util.Utils;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service.dos:
//            DOSObject, DOSChangeable

public class DOSModel extends DOSObject
{

    public DOSModel()
    {
        packageMap = null;
        codeMap = null;
        packageMap = new TreeMap();
        codeMap = new TreeMap();
    }

    public static String createModel(DOSChangeable modelDefinition, DTM dtm, HashMap modelMap)
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
        if(modelDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + modelDefinition);
        valueMap = modelDefinition.getValueMap();
        if(valueMap == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dosmod (ouid,name,des) values (?,?,?) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSModel dosModel = new DOSModel();
            dosModel.OUID = returnLong;
            dosModel.name = (String)valueMap.get("name");
            dosModel.description = (String)valueMap.get("description");
            modelMap.put(returnString, dosModel);
            dosModel = null;
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

    public static void removeModel(String ouid, DTM dtm, NDS nds, HashMap modelMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from dosmodpkg where dospkg=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosmod where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            modelMap.remove(ouid);
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
        }
        catch(SQLException e)
        {
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
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return;
    }

    public static DOSChangeable getModel(String ouid, HashMap modelMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSModel dosModel = (DOSModel)modelMap.get(ouid);
        if(dosModel != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosModel.name);
            returnObject.put("description", dosModel.description);
            dosModel = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setModel(DOSChangeable modelDefinition, DTM dtm, HashMap modelMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        if(modelDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + modelDefinition);
        HashMap valueMap = modelDefinition.getValueMap();
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dosmod set name=?, des=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSModel dosModel = (DOSModel)modelMap.get(ouid);
            if(dosModel == null)
            {
                dosModel = new DOSModel();
                dosModel.OUID = ouidLong;
                modelMap.put(ouid, dosModel);
            }
            dosModel.name = (String)valueMap.get("name");
            dosModel.description = (String)valueMap.get("description");
            dosModel = null;
        }
        catch(SQLException e)
        {
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
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return;
    }

    public static ArrayList listModel(DTM dtm, HashMap modelMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSModel dosModel = null;
        ArrayList returnList = null;
        DOSChangeable modelData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        if(modelMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = modelMap.keySet().iterator(); mapKey.hasNext();)
            {
                modelData = new DOSChangeable();
                dosModel = (DOSModel)modelMap.get(mapKey.next());
                modelData.put("ouid", Long.toHexString(dosModel.OUID));
                modelData.put("name", dosModel.name);
                modelData.put("description", dosModel.description);
                returnList.add(modelData);
                modelData = null;
                dosModel = null;
            }

            mapKey = null;
            modelData = null;
            Utils.sort(returnList);
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dm.ouid, dm.name, dm.des from dosmod dm ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSChangeable modelData;
            while(rs.next()) 
            {
                modelData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                modelData.put("ouid", ouid);
                modelData.put("name", rs.getString(2));
                modelData.put("description", rs.getString(3));
                returnList.add(modelData);
                DOSModel dosModel = new DOSModel();
                dosModel.OUID = ouidLong;
                dosModel.name = (String)modelData.get("name");
                dosModel.description = (String)modelData.get("description");
                modelMap.put(ouid, dosModel);
                dosModel = null;
                modelData = null;
            }
            modelData = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            Utils.sort(returnList);
            arraylist = returnList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public TreeMap packageMap;
    public TreeMap codeMap;
}
