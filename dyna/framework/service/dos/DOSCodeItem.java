// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSCodeItem.java

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
//            DOSObject, DOSChangeable, DOSCode

public class DOSCodeItem extends DOSObject
{

    public DOSCodeItem()
    {
        _code = null;
        codeItemID = null;
        parent = null;
        children = null;
        parentOuid = 0L;
        isRoot = false;
        filter = null;
        isNullFilter = false;
    }

    public static String createCodeItem(String codeOuid, DOSChangeable codeItemDefinition, DTM dtm, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSChangeable codeData;
        String maxCodItm;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        String returnString = null;
        String parentString = null;
        Boolean returnBoolean = null;
        long returnLong = 0L;
        int rows = 0;
        codeData = null;
        maxCodItm = null;
        if(codeItemDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + codeItemDefinition);
        valueMap = codeItemDefinition.getValueMap();
        if(Utils.isNullString(codeOuid) || valueMap == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dc.autogen,        dc.prefix,        dc.len,        dc.inival,        dc.inc, \t   max(dci.coditm)  from doscoditm dci, doscod dc where dci.doscod(+)= dc.ouid   and dc.ouid = ? group by dc.autogen, dc.prefix, dc.len, dc.inival, dc.inc ");
            stat.setLong(1, Utils.convertOuidToLong(codeOuid));
            ResultSet rs = stat.executeQuery();
            if(rs.next())
            {
                codeData = new DOSChangeable();
                codeData.put("is.autogeneratable", Utils.getBoolean(rs, 1));
                codeData.put("prefix", rs.getString(2));
                codeData.put("suffixlength", Utils.getInteger(rs, 3));
                codeData.put("initvalue", Utils.getInteger(rs, 4));
                codeData.put("increment", Utils.getInteger(rs, 5));
                maxCodItm = rs.getString(6);
            }
            stat.close();
            stat = null;
            if(((Boolean)codeData.get("is.autogeneratable")).booleanValue())
            {
                int suffixVal;
                if(maxCodItm == null || maxCodItm.equals(""))
                {
                    suffixVal = ((Integer)codeData.get("initvalue")).intValue();
                } else
                {
                    int prefixlen = ((String)codeData.get("prefix")).length();
                    suffixVal = Integer.parseInt(maxCodItm.substring(prefixlen, maxCodItm.length())) + ((Integer)codeData.get("increment")).intValue();
                }
                StringBuffer tmpStr = (new StringBuffer()).append((String)codeData.get("prefix"));
                int tmpVal = ((Integer)codeData.get("suffixlength")).intValue() - String.valueOf(suffixVal).length();
                for(int i = 0; i < tmpVal; i++)
                    tmpStr.append("0");

                tmpStr.append(String.valueOf(suffixVal));
                valueMap.put("codeitemid", new String(tmpStr));
            }
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into doscoditm (ouid, name, des, doscod, coditm, parn, filt ) values (?, ?, ?, ?, ?, ?, ? ) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong(codeOuid));
            stat.setString(5, (String)valueMap.get("codeitemid"));
            String parentString = (String)valueMap.get("parent");
            if(Utils.isNullString(parentString))
                stat.setNull(6, 2);
            else
                stat.setLong(6, Utils.convertOuidToLong(parentString));
            stat.setString(7, (String)valueMap.get("filter"));
            int rows = stat.executeUpdate();
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCodeItem dosCodeItem = new DOSCodeItem();
            dosCodeItem.OUID = returnLong;
            dosCodeItem.name = (String)valueMap.get("name");
            dosCodeItem.description = (String)valueMap.get("description");
            dosCodeItem._code = (DOSCode)dosCodeMap.get((String)valueMap.get("ouid@code"));
            dosCodeItem.codeItemID = (String)valueMap.get("codeitemid");
            dosCodeItem.parent = (DOSCodeItem)dosCodeItemMap.get(parentString);
            dosCodeItem.filter = (String)valueMap.get("filter");
            dosCodeItem.isNullFilter = Utils.isNullString(dosCodeItem.filter);
            dosCodeItemMap.put(returnString, dosCodeItem);
            DOSCode dosCode = (DOSCode)dosCodeMap.get(codeOuid);
            if(dosCode != null)
            {
                TreeMap codeItemMap = dosCode.codeItemMap;
                if(codeItemMap == null)
                {
                    codeItemMap = new TreeMap();
                    dosCode.codeItemMap = codeItemMap;
                }
                codeItemMap.put(dosCodeItem.codeItemID, dosCodeItem);
                codeItemMap = null;
            }
            dosCode = null;
            valueMap = null;
            if(dosCodeItem.parent != null)
            {
                TreeMap children = dosCodeItem.parent.children;
                if(children == null)
                {
                    children = new TreeMap();
                    dosCodeItem.parent.children = children;
                }
                children.put(dosCodeItem.codeItemID, dosCodeItem);
                children = null;
            }
            dosCodeItem = null;
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

    public static void removeCodeItem(String ouid, DTM dtm, HashMap dosCodeItemMap)
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
            stat = con.prepareStatement("delete from doscoditm where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCodeItem dosCodeItem = (DOSCodeItem)dosCodeItemMap.remove(ouid);
            DOSCode dosCode = dosCodeItem._code;
            if(dosCode != null && dosCode.codeItemMap != null && dosCode.codeItemMap.size() > 0)
                dosCode.codeItemMap.remove(dosCodeItem.codeItemID);
            dosCode = null;
            if(dosCodeItem.parent != null && dosCodeItem.parent.children != null)
                dosCodeItem.parent.children.remove(dosCodeItem.codeItemID);
            dosCodeItem = null;
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

    public static void removeCodeItemInCode(String codeouid, DTM dtm, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(codeouid);
        int rows = 0;
        if(Utils.isNullString(codeouid))
            throw new IIPRequestException("Parameter value is null : codeouid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from doscoditm where doscod=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCode dosCode = (DOSCode)dosCodeMap.get(codeouid);
            if(dosCodeMap.size() > 0 && dosCodeItemMap.size() > 0)
            {
                TreeMap codeItemMap = dosCode.codeItemMap;
                if(codeItemMap != null && codeItemMap.size() > 0)
                {
                    DOSChangeable codeItemData = null;
                    DOSCodeItem dosCodeItem = null;
                    ArrayList arrayList = new ArrayList();
                    for(Iterator mapKey = codeItemMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        codeItemData = new DOSChangeable();
                        dosCodeItem = (DOSCodeItem)codeItemMap.get(mapKey.next());
                        codeItemData.put("ouid", Long.toHexString(dosCodeItem.OUID));
                        codeItemData.put("name", dosCodeItem.name);
                        arrayList.add(codeItemData);
                        codeItemData = null;
                        dosCodeItem = null;
                    }

                    for(int i = 0; i < arrayList.size(); i++)
                    {
                        dosCodeItemMap.remove(((DOSChangeable)arrayList.get(i)).get("ouid"));
                        codeItemMap.remove(((DOSChangeable)arrayList.get(i)).get("name"));
                    }

                }
            }
            dosCode = null;
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

    public static DOSChangeable getCodeItem(String ouid, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSCodeItem dosCodeItem = (DOSCodeItem)dosCodeItemMap.get(ouid);
        if(dosCodeItem != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosCodeItem.name);
            returnObject.put("description", dosCodeItem.description);
            DOSCode dosCode = dosCodeItem._code;
            if(dosCode != null)
                returnObject.put("ouid@code", Long.toHexString(dosCodeItem._code.OUID));
            dosCode = null;
            returnObject.put("codeitemid", dosCodeItem.codeItemID);
            if(dosCodeItem.parent != null)
                returnObject.put("parent", Long.toHexString(dosCodeItem.parent.OUID));
            returnObject.put("filter", dosCodeItem.filter);
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setCodeItem(DOSChangeable codeItemDefinition, DTM dtm, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        DOSCode dosCode;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        dosCode = null;
        if(codeItemDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + codeItemDefinition);
        HashMap valueMap = codeItemDefinition.getValueMap();
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update doscoditm set name=?, des=?, doscod=?, coditm=?, parn=?, filt=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("ouid@code")));
            stat.setString(4, (String)valueMap.get("codeitemid"));
            String parentString = (String)valueMap.get("parent");
            if(Utils.isNullString(parentString))
                stat.setNull(5, 2);
            else
                stat.setLong(5, Utils.convertOuidToLong(parentString));
            stat.setString(6, (String)valueMap.get("filter"));
            stat.setLong(7, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCodeItem dosCodeItem = (DOSCodeItem)dosCodeItemMap.get(ouid);
            if(dosCodeItem == null)
            {
                dosCodeItem = new DOSCodeItem();
                dosCodeItem.OUID = ouidLong;
                dosCodeItemMap.put(ouid, dosCodeItem);
            }
            String oldName = dosCodeItem.codeItemID;
            dosCodeItem.name = (String)valueMap.get("name");
            dosCodeItem.description = (String)valueMap.get("description");
            dosCodeItem._code = (DOSCode)dosCodeMap.get((String)valueMap.get("ouid@code"));
            dosCodeItem.codeItemID = (String)valueMap.get("codeitemid");
            dosCodeItem.parent = (DOSCodeItem)dosCodeItemMap.get(parentString);
            dosCodeItem.filter = (String)valueMap.get("filter");
            dosCodeItem.isNullFilter = Utils.isNullString(dosCodeItem.filter);
            dosCode = dosCodeItem._code;
            if(dosCode.codeItemMap != null && dosCode.codeItemMap.size() > 0)
            {
                dosCode.codeItemMap.remove(oldName);
                dosCode.codeItemMap.put(dosCodeItem.codeItemID, dosCodeItem);
            }
            if(dosCodeItem.parent != null && dosCodeItem.parent.children != null && !dosCodeItem.parent.children.isEmpty())
            {
                dosCodeItem.parent.children.remove(oldName);
                dosCodeItem.parent.children.put(dosCodeItem.codeItemID, dosCodeItem);
            }
            dosCodeItem = null;
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        if(dosCode != null)
        {
            DOSChangeable rootItem = getCodeItemRoot(Long.toHexString(dosCode.OUID), dtm, dosCodeMap, dosCodeItemMap);
            if(rootItem == null)
            {
                dosCode = null;
                rootItem = null;
                return;
            }
            setCodeItemHelper1((String)rootItem.get("ouid"), dosCode.filter, dosCodeItemMap);
            rootItem = null;
            dosCode = null;
        }
        return;
    }

    public static ArrayList listCodeItem(String codeOuid, DTM dtm, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSCode dosCode;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        dosCode = null;
        DOSCodeItem dosCodeItem = null;
        ArrayList returnList = null;
        TreeMap tempMap = null;
        DOSChangeable codeItemData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        dosCode = (DOSCode)dosCodeMap.get(codeOuid);
        if(dosCode == null)
            throw new IIPRequestException("No such code.");
        if(dosCodeMap.size() > 0 && dosCodeItemMap.size() > 0)
        {
            TreeMap codeItemMap = dosCode.codeItemMap;
            if(codeItemMap != null && codeItemMap.size() > 0)
            {
                returnList = new ArrayList();
                Iterator mapKey;
                for(mapKey = codeItemMap.keySet().iterator(); mapKey.hasNext();)
                {
                    codeItemData = new DOSChangeable();
                    dosCodeItem = (DOSCodeItem)codeItemMap.get(mapKey.next());
                    codeItemData.put("ouid", Long.toHexString(dosCodeItem.OUID));
                    codeItemData.put("name", dosCodeItem.name);
                    codeItemData.put("description", dosCodeItem.description);
                    codeItemData.put("ouid@code", codeOuid);
                    codeItemData.put("codeitemid", dosCodeItem.codeItemID);
                    codeItemData.put("index", dosCodeItem.codeItemID);
                    if(dosCodeItem.parent != null)
                        codeItemData.put("parent", Long.toHexString(dosCodeItem.parent.OUID));
                    codeItemData.put("filter", dosCodeItem.filter);
                    returnList.add(codeItemData);
                    codeItemData = null;
                    dosCodeItem = null;
                }

                mapKey = null;
                codeItemData = null;
                codeItemMap = null;
                Utils.sort(returnList);
                return returnList;
            }
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dci.ouid from doscoditm dci where dci.doscod=? order by dci.coditm ");
            stat.setLong(1, Utils.convertOuidToLong(codeOuid));
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            TreeMap tempMap = new TreeMap();
            DOSCodeItem dosCodeItem;
            DOSChangeable codeItemData;
            while(rs.next()) 
            {
                codeItemData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                dosCodeItem = (DOSCodeItem)dosCodeItemMap.get(ouid);
                codeItemData.put("ouid", ouid);
                codeItemData.put("name", dosCodeItem.name);
                codeItemData.put("description", dosCodeItem.description);
                codeItemData.put("ouid@code", codeOuid);
                codeItemData.put("codeitemid", dosCodeItem.codeItemID);
                if(dosCodeItem.parent != null)
                    codeItemData.put("parent", Long.toHexString(dosCodeItem.parent.OUID));
                codeItemData.put("filter", dosCodeItem.filter);
                returnList.add(codeItemData);
                tempMap.put(dosCodeItem.codeItemID, dosCodeItem);
                if(dosCodeItem.parent != null)
                {
                    TreeMap children = dosCodeItem.parent.children;
                    if(children == null)
                    {
                        children = new TreeMap();
                        dosCodeItem.parent.children = children;
                    }
                    children.put(dosCodeItem.codeItemID, dosCodeItem);
                    children = null;
                }
                dosCodeItem = null;
                codeItemData = null;
            }
            dosCode.codeItemMap = tempMap;
            codeItemData = null;
            tempMap = null;
            dosCodeItem = null;
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

    public static ArrayList listAllCodeItem(DTM dtm, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSCodeItem dosCodeItem = null;
        ArrayList returnList = null;
        DOSChangeable codeItemData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        if(dosCodeItemMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = dosCodeItemMap.keySet().iterator(); mapKey.hasNext();)
            {
                codeItemData = new DOSChangeable();
                dosCodeItem = (DOSCodeItem)dosCodeItemMap.get(mapKey.next());
                codeItemData.put("ouid", Long.toHexString(dosCodeItem.OUID));
                codeItemData.put("name", dosCodeItem.name);
                codeItemData.put("description", dosCodeItem.description);
                DOSCode dosCode = dosCodeItem._code;
                if(dosCode != null)
                    codeItemData.put("ouid@code", Long.toHexString(dosCode.OUID));
                codeItemData.put("codeitemid", dosCodeItem.codeItemID);
                if(dosCodeItem.parent != null)
                    codeItemData.put("parent", Long.toHexString(dosCodeItem.parent.OUID));
                else
                if(dosCodeItem.parentOuid > 0L && !dosCodeItem.isRoot)
                {
                    DOSCodeItem parentItem = (DOSCodeItem)dosCodeItemMap.get(Long.toHexString(dosCodeItem.parentOuid));
                    dosCodeItem.parent = parentItem;
                    codeItemData.put("parent", Long.toHexString(dosCodeItem.parent.OUID));
                }
                codeItemData.put("filter", dosCodeItem.filter);
                codeItemData = null;
                dosCodeItem = null;
                dosCode = null;
            }

            mapKey = null;
            codeItemData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dci.ouid, dci.name, dci.des, dci.doscod, dci.coditm, dci.parn, dci.filt from doscoditm dci ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            Long parentOuid = null;
            DOSChangeable codeItemData;
            while(rs.next()) 
            {
                codeItemData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                codeItemData.put("ouid", ouid);
                codeItemData.put("name", rs.getString(2));
                codeItemData.put("description", rs.getString(3));
                codeItemData.put("ouid@code", Long.toHexString(rs.getLong(4)));
                codeItemData.put("codeitemid", rs.getString(5));
                parentOuid = Utils.getLong(rs, 6);
                if(parentOuid != null)
                    codeItemData.put("parent", Long.toHexString(parentOuid.longValue()));
                codeItemData.put("filter", rs.getString(7));
                returnList.add(codeItemData);
                DOSCodeItem dosCodeItem = new DOSCodeItem();
                dosCodeItem.OUID = ouidLong;
                dosCodeItem.name = (String)codeItemData.get("name");
                dosCodeItem.description = (String)codeItemData.get("description");
                dosCodeItem._code = (DOSCode)dosCodeMap.get((String)codeItemData.get("ouid@code"));
                dosCodeItem.codeItemID = (String)codeItemData.get("codeitemid");
                if(parentOuid != null)
                {
                    dosCodeItem.parent = (DOSCodeItem)dosCodeItemMap.get(Long.toHexString(parentOuid.longValue()));
                    dosCodeItem.parentOuid = parentOuid.longValue();
                    dosCodeItem.isRoot = false;
                } else
                {
                    dosCodeItem.isRoot = true;
                }
                dosCodeItem.filter = (String)codeItemData.get("filter");
                dosCodeItem.isNullFilter = Utils.isNullString(dosCodeItem.filter);
                dosCodeItemMap.put(ouid, dosCodeItem);
                dosCodeItem = null;
                codeItemData = null;
            }
            codeItemData = null;
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

    public static DOSChangeable getCodeItemRoot(String codeOuid, DTM dtm, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(codeOuid))
            throw new IIPRequestException("Parameter value is null : codeOuid");
        DOSCode dosCode = (DOSCode)dosCodeMap.get(codeOuid);
        DOSCodeItem dosCodeItem = null;
        TreeMap codeItemMap = dosCode.codeItemMap;
        if(codeItemMap == null)
            listCodeItem(codeOuid, dtm, dosCodeMap, dosCodeItemMap);
        codeItemMap = dosCode.codeItemMap;
        if(codeItemMap == null)
            return null;
        Iterator itemKey;
        for(itemKey = codeItemMap.keySet().iterator(); itemKey.hasNext();)
        {
            dosCodeItem = (DOSCodeItem)codeItemMap.get(itemKey.next());
            if(dosCodeItem != null && dosCodeItem.parent == null)
            {
                returnObject = new DOSChangeable();
                returnObject.put("ouid", Long.toHexString(dosCodeItem.OUID));
                returnObject.put("name", dosCodeItem.name);
                returnObject.put("description", dosCodeItem.description);
                dosCode = dosCodeItem._code;
                if(dosCode != null)
                    returnObject.put("ouid@code", Long.toHexString(dosCodeItem._code.OUID));
                dosCode = null;
                returnObject.put("codeitemid", dosCodeItem.codeItemID);
                returnObject.put("filter", dosCodeItem.filter);
                break;
            }
        }

        itemKey = null;
        return returnObject;
    }

    public static ArrayList getCodeItemChildren(String codeItemOuid, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        DOSCodeItem dosCodeItem = null;
        DOSCodeItem tempCodeItem = null;
        ArrayList returnList = null;
        TreeMap tempMap = null;
        DOSChangeable codeItemData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        dosCodeItem = (DOSCodeItem)dosCodeItemMap.get(codeItemOuid);
        if(dosCodeItem == null)
            throw new IIPRequestException("No such code item.");
        if(dosCodeMap.size() > 0 && dosCodeItemMap.size() > 0)
        {
            TreeMap codeItemMap = dosCodeItem.children;
            if(codeItemMap != null && codeItemMap.size() > 0)
            {
                returnList = new ArrayList();
                Iterator mapKey;
                for(mapKey = codeItemMap.keySet().iterator(); mapKey.hasNext();)
                {
                    codeItemData = new DOSChangeable();
                    tempCodeItem = (DOSCodeItem)codeItemMap.get(mapKey.next());
                    codeItemData.put("ouid", Long.toHexString(tempCodeItem.OUID));
                    codeItemData.put("name", tempCodeItem.name);
                    codeItemData.put("description", tempCodeItem.description);
                    codeItemData.put("ouid@code", Long.toHexString(tempCodeItem._code.OUID));
                    codeItemData.put("codeitemid", tempCodeItem.codeItemID);
                    codeItemData.put("index", tempCodeItem.codeItemID);
                    if(tempCodeItem.parent != null)
                        codeItemData.put("parent", Long.toHexString(tempCodeItem.parent.OUID));
                    codeItemData.put("filter", tempCodeItem.filter);
                    returnList.add(codeItemData);
                    codeItemData = null;
                    tempCodeItem = null;
                }

                mapKey = null;
                codeItemData = null;
                codeItemMap = null;
                Utils.sort(returnList);
                return returnList;
            }
        }
        return null;
    }

    public static DOSChangeable getCodeItemParent(String codeItemOuid, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        DOSCodeItem dosCodeItem = null;
        DOSCodeItem tempCodeItem = null;
        DOSChangeable codeItemData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        dosCodeItem = (DOSCodeItem)dosCodeItemMap.get(codeItemOuid);
        if(dosCodeItem == null)
            throw new IIPRequestException("No such code item.");
        if(dosCodeMap.size() > 0 && dosCodeItemMap.size() > 0 && dosCodeItem.parent != null)
        {
            codeItemData = new DOSChangeable();
            tempCodeItem = (DOSCodeItem)dosCodeItemMap.get(Long.toHexString(dosCodeItem.parent.OUID));
            codeItemData.put("ouid", Long.toHexString(tempCodeItem.OUID));
            codeItemData.put("name", tempCodeItem.name);
            codeItemData.put("description", tempCodeItem.description);
            codeItemData.put("ouid@code", Long.toHexString(tempCodeItem._code.OUID));
            codeItemData.put("codeitemid", tempCodeItem.codeItemID);
            if(tempCodeItem.parent != null)
                codeItemData.put("parent", Long.toHexString(tempCodeItem.parent.OUID));
            codeItemData.put("filter", tempCodeItem.filter);
            codeItemData = null;
        }
        return null;
    }

    public static void setCodeItemHelper1(String codeItemOuid, String defaultFilter, HashMap codeItemMap)
    {
        if(Utils.isNullString(codeItemOuid))
            return;
        DOSCodeItem startCodeItem = (DOSCodeItem)codeItemMap.get(codeItemOuid);
        if(startCodeItem == null)
            return;
        TreeMap children = startCodeItem.children;
        if(children == null)
            return;
        DOSCodeItem dosCodeItem = null;
        String filter = startCodeItem.filter;
        if(Utils.isNullString(filter))
            filter = defaultFilter;
        Iterator childKey;
        for(childKey = children.keySet().iterator(); childKey.hasNext();)
        {
            dosCodeItem = (DOSCodeItem)children.get(childKey.next());
            if(dosCodeItem == null)
                return;
            if(Utils.isNullString(dosCodeItem.filter) || dosCodeItem.isNullFilter)
            {
                dosCodeItem.isNullFilter = true;
                dosCodeItem.filter = filter;
            } else
            {
                dosCodeItem.isNullFilter = false;
            }
            setCodeItemHelper1(Long.toHexString(dosCodeItem.OUID), filter, codeItemMap);
            dosCodeItem = null;
        }

        childKey = null;
    }

    public DOSCode _code;
    public String codeItemID;
    public DOSCodeItem parent;
    public TreeMap children;
    public long parentOuid;
    public boolean isRoot;
    public String filter;
    public boolean isNullFilter;
}
