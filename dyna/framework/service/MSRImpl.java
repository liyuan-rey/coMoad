// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MSRImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.msr.MSRStgrep;
import dyna.util.*;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;
import javax.sql.PooledConnection;
import org.xml.sax.Attributes;

// Referenced classes of package dyna.framework.service:
//            MSR, DTM, NDS

public class MSRImpl extends ServiceServer
    implements MSR, XMLImportable
{

    public MSRImpl()
    {
        dtm = null;
        nds = null;
        stgRepMap = null;
        isReady = false;
        tempLoc = null;
        tempID = null;
        tempStg = null;
        stgRepMap = new HashMap();
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dtm = (DTM)getServiceInstance("DF30DTM1");
            nds = (NDS)getServiceInstance("DF30NDS1");
            listAllStgrep();
            System.out.print('.');
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public String createStgRep(DOSChangeable stgRepDefinition)
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
        if(stgRepDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + stgRepDefinition);
        valueMap = stgRepDefinition.getValueMap();
        if(valueMap == null || valueMap.get("loc") == null || valueMap.get("id") == null || valueMap.get("stg") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into msrstgrep (loc,id,stg) values (?,?,?) ");
            stat.setString(1, (String)valueMap.get("loc"));
            stat.setString(2, (String)valueMap.get("id"));
            stat.setString(3, (String)valueMap.get("stg"));
            stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            String returnString = (String)valueMap.get("loc") + "_" + (String)valueMap.get("id");
            MSRStgrep msrStgrep = new MSRStgrep();
            msrStgrep.loc = (String)valueMap.get("loc");
            msrStgrep.id = (String)valueMap.get("id");
            msrStgrep.stg = (String)valueMap.get("stg");
            stgRepMap.put(returnString, msrStgrep);
            msrStgrep = null;
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
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeStgrep(String loc, String id)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        if(Utils.isNullString(loc) || Utils.isNullString(id))
            throw new IIPRequestException("Parameter value is null : loc (or) id");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from msrstgrep where loc=? and  id=? ");
            stat.setString(1, loc);
            stat.setString(2, id);
            stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            stgRepMap.remove(loc + "_" + id);
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
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

    public DOSChangeable getStgrep(String loc, String id)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        if(Utils.isNullString(loc) || Utils.isNullString(id))
            throw new IIPRequestException("Parameter value is null : loc (or) id");
        MSRStgrep msrStgrep = (MSRStgrep)stgRepMap.get(loc + "_" + id);
        if(msrStgrep != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("loc", msrStgrep.loc);
            returnObject.put("id", msrStgrep.id);
            returnObject.put("stg", msrStgrep.stg);
            msrStgrep = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public void setStgrep(DOSChangeable stgRepDefinition)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        if(stgRepDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + stgRepDefinition);
        HashMap valueMap = stgRepDefinition.getValueMap();
        if(valueMap == null || valueMap.get("loc") == null || valueMap.get("id") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update msrstgrep set stg=? where loc=? and id=? ");
            stat.setString(1, (String)valueMap.get("stg"));
            stat.setString(2, (String)valueMap.get("loc"));
            stat.setString(3, (String)valueMap.get("id"));
            stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            ouid = (String)valueMap.get("loc") + "_" + (String)valueMap.get("id");
            MSRStgrep msrStgrep = (MSRStgrep)stgRepMap.get(ouid);
            if(msrStgrep == null)
            {
                msrStgrep = new MSRStgrep();
                msrStgrep.loc = (String)valueMap.get("loc");
                msrStgrep.id = (String)valueMap.get("id");
                msrStgrep.stg = (String)valueMap.get("stg");
                stgRepMap.put(ouid, msrStgrep);
            }
            msrStgrep.loc = (String)valueMap.get("loc");
            msrStgrep.id = (String)valueMap.get("id");
            msrStgrep.stg = (String)valueMap.get("stg");
            msrStgrep = null;
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
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

    public ArrayList listStgrep(String loc)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        MSRStgrep msrStgrep = null;
        ArrayList returnList = null;
        DOSChangeable stgrepData = null;
        String ouid = null;
        if(Utils.isNullString(loc))
            throw new IIPRequestException("Parameter value is null : loc");
        if(stgRepMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = stgRepMap.keySet().iterator(); mapKey.hasNext();)
            {
                stgrepData = new DOSChangeable();
                String mapKeyStr = mapKey.next().toString();
                if(mapKeyStr.startsWith(loc))
                {
                    msrStgrep = (MSRStgrep)stgRepMap.get(mapKeyStr);
                    stgrepData.put("loc", msrStgrep.loc);
                    stgrepData.put("id", msrStgrep.id);
                    stgrepData.put("stg", msrStgrep.stg);
                    returnList.add(stgrepData);
                    stgrepData = null;
                    msrStgrep = null;
                }
            }

            mapKey = null;
            stgrepData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select msr.loc, msr.id, msr.stg from msrstgrep msr where msr.loc=? ");
            stat.setString(1, loc);
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSChangeable stgrepData;
            while(rs.next()) 
            {
                stgrepData = new DOSChangeable();
                String ouid = rs.getString(1) + "_" + rs.getString(2);
                stgrepData.put("loc", rs.getString(1));
                stgrepData.put("id", rs.getString(2));
                stgrepData.put("stg", rs.getString(3));
                returnList.add(stgrepData);
                MSRStgrep msrStgrep = new MSRStgrep();
                msrStgrep.loc = (String)stgrepData.get("loc");
                msrStgrep.id = (String)stgrepData.get("id");
                msrStgrep.stg = (String)stgrepData.get("stg");
                stgRepMap.put(ouid, msrStgrep);
                msrStgrep = null;
                stgrepData = null;
            }
            stgrepData = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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

    public ArrayList listAllStgrep()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        MSRStgrep msrStgrep = null;
        ArrayList returnList = null;
        DOSChangeable stgrepData = null;
        if(stgRepMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = stgRepMap.keySet().iterator(); mapKey.hasNext();)
            {
                stgrepData = new DOSChangeable();
                msrStgrep = (MSRStgrep)stgRepMap.get(mapKey.next());
                stgrepData.put("loc", msrStgrep.loc);
                stgrepData.put("id", msrStgrep.id);
                stgrepData.put("stg", msrStgrep.stg);
                returnList.add(stgrepData);
                stgrepData = null;
                msrStgrep = null;
            }

            mapKey = null;
            stgrepData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select msr.loc, msr.id, msr.stg from msrstgrep msr ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSChangeable stgrepData;
            while(rs.next()) 
            {
                stgrepData = new DOSChangeable();
                stgrepData.put("loc", rs.getString(1));
                stgrepData.put("id", rs.getString(2));
                stgrepData.put("stg", rs.getString(3));
                returnList.add(stgrepData);
                MSRStgrep msrStgrep = new MSRStgrep();
                msrStgrep.loc = (String)stgrepData.get("loc");
                msrStgrep.id = (String)stgrepData.get("id");
                msrStgrep.stg = (String)stgrepData.get("stg");
                stgRepMap.put((String)stgrepData.get("loc") + "_" + (String)stgrepData.get("id"), msrStgrep);
                msrStgrep = null;
                stgrepData = null;
            }
            stgrepData = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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

    public HashMap getAllStgrep(String loc)
        throws IIPRequestException
    {
        return stgRepMap;
    }

    public void importStgrep(String fileName)
        throws IIPRequestException
    {
        XMLImport(fileName);
    }

    public void setCurrentLocale(String locale)
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(context == null)
        {
            return;
        } else
        {
            context.put("locale", locale);
            context = null;
            return;
        }
    }

    public String getStgrepString(String loc, String id)
        throws IIPRequestException
    {
        DOSChangeable obj = getStgrep(loc, id);
        if(obj == null)
            return null;
        else
            return (String)obj.get("stg");
    }

    public String getStgrepString(String loc, String id, String defaultString)
        throws IIPRequestException
    {
        DOSChangeable obj = getStgrep(loc, id);
        if(obj == null)
            return defaultString;
        else
            return (String)obj.get("stg");
    }

    public void startImport()
    {
        isReady = false;
    }

    public void startElement(String name, Attributes attrs)
    {
        tempLoc = null;
        tempID = null;
        tempStg = null;
    }

    public void endElement(String name)
    {
        try
        {
            DOSChangeable inputData = new DOSChangeable();
            inputData.put("loc", tempLoc);
            inputData.put("id", tempID);
            inputData.put("stg", tempStg);
            createStgRep(inputData);
            inputData = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public void setElementData(String name, String value)
    {
        if(name.equals("item.locale"))
            tempLoc = new String(value);
        else
        if(name.equals("item.id"))
            tempID = new String(value);
        else
        if(name.equals("item.string"))
            tempStg = new String(value);
    }

    public void endImport()
    {
        isReady = true;
    }

    public void XMLImport(String fileName)
    {
        XMLUtil.XMLImport(fileName, this, "item");
    }

    private DTM dtm;
    private NDS nds;
    private HashMap stgRepMap;
    private boolean isReady;
    private String tempLoc;
    private String tempID;
    private String tempStg;
}
