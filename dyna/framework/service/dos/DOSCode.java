// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSCode.java

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
//            DOSObject, DOSChangeable, DOSModel, DOSCodeItem

public class DOSCode extends DOSObject
{

    public DOSCode()
    {
        codeItemMap = null;
        _model = null;
        isAutoGeneratable = null;
        prefix = null;
        suffixLength = null;
        initValue = null;
        increment = null;
        hierarchy = null;
        filter = null;
        navigator = null;
        indirect = null;
        visualType = null;
        codeItemMap = new TreeMap();
    }

    public static String createCode(String modelOuid, DOSChangeable codeDefinition, DTM dtm, HashMap modelMap, HashMap dosCodeMap)
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
        if(codeDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + codeDefinition);
        valueMap = codeDefinition.getValueMap();
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
            stat = con.prepareStatement("insert into doscod (ouid, name, des, dosmod, autogen, prefix, len, inival, inc, hier, filt, navi, indr, vistyp ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong(modelOuid));
            Utils.setBoolean(stat, 5, (Boolean)valueMap.get("is.autogeneratable"));
            stat.setString(6, (String)valueMap.get("prefix"));
            stat.setInt(7, ((Integer)valueMap.get("suffixlength")).intValue());
            stat.setInt(8, ((Integer)valueMap.get("initvalue")).intValue());
            stat.setInt(9, ((Integer)valueMap.get("increment")).intValue());
            Utils.setBoolean(stat, 10, (Boolean)valueMap.get("is.hierarchy"));
            stat.setString(11, (String)valueMap.get("filter"));
            Utils.setBoolean(stat, 12, (Boolean)valueMap.get("is.navigator"));
            Utils.setBoolean(stat, 13, (Boolean)valueMap.get("is.indirect"));
            if(valueMap.get("visual.type") == null)
                valueMap.put("visual.type", new Integer(1));
            Utils.setInt(stat, 14, (Integer)valueMap.get("visual.type"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCode dosCode = new DOSCode();
            dosCode.OUID = returnLong;
            dosCode.name = (String)valueMap.get("name");
            dosCode.description = (String)valueMap.get("description");
            dosCode._model = (DOSModel)modelMap.get((String)valueMap.get("ouid@model"));
            dosCode.isAutoGeneratable = (Boolean)valueMap.get("is.autogeneratable");
            dosCode.prefix = (String)valueMap.get("prefix");
            dosCode.suffixLength = (Integer)valueMap.get("suffixlength");
            dosCode.initValue = (Integer)valueMap.get("initvalue");
            dosCode.increment = (Integer)valueMap.get("increment");
            dosCode.hierarchy = (Boolean)valueMap.get("is.hierarchy");
            dosCode.filter = (String)valueMap.get("filter");
            dosCode.navigator = (Boolean)valueMap.get("is.navigator");
            dosCode.indirect = (Boolean)valueMap.get("is.indirect");
            dosCode.visualType = (Integer)valueMap.get("visual.type");
            dosCodeMap.put(returnString, dosCode);
            DOSModel dosModel = (DOSModel)modelMap.get(modelOuid);
            if(dosModel != null)
            {
                TreeMap codeMap = dosModel.codeMap;
                if(codeMap == null)
                {
                    codeMap = new TreeMap();
                    dosModel.codeMap = codeMap;
                }
                codeMap.put(dosCode.name, dosCode);
                codeMap = null;
                dosCode = null;
            }
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

    public static void removeCode(String ouid, DTM dtm, HashMap codeMap)
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
            stat = con.prepareStatement("delete from doscod where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCode dosCode = (DOSCode)codeMap.remove(ouid);
            DOSModel dosModel = dosCode._model;
            if(dosModel != null && dosModel.codeMap != null && dosModel.codeMap.size() > 0)
                dosModel.codeMap.remove(dosCode.name);
            dosModel = null;
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

    public static DOSChangeable getCode(String ouid, HashMap codeMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSCode dosCode = (DOSCode)codeMap.get(ouid);
        if(dosCode != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosCode.name);
            returnObject.put("description", dosCode.description);
            DOSModel dosModel = dosCode._model;
            if(dosModel != null)
                returnObject.put("ouid@model", Long.toHexString(dosCode._model.OUID));
            dosModel = null;
            returnObject.put("is.autogeneratable", dosCode.isAutoGeneratable);
            returnObject.put("prefix", dosCode.prefix);
            returnObject.put("suffixlength", dosCode.suffixLength);
            returnObject.put("initvalue", dosCode.initValue);
            returnObject.put("increment", dosCode.increment);
            returnObject.put("is.hierarchy", dosCode.hierarchy);
            returnObject.put("filter", dosCode.filter);
            returnObject.put("is.navigator", dosCode.navigator);
            returnObject.put("is.indirect", dosCode.indirect);
            returnObject.put("visual.type", dosCode.visualType);
            dosCode = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setCode(DOSChangeable codeDefinition, DTM dtm, HashMap codeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        String ouid;
        HashMap valueMap;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ouid = null;
        if(codeDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + codeDefinition);
        valueMap = codeDefinition.getValueMap();
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update doscod set name=?, des=?, dosmod=?, autogen=?, prefix=?, len=?, inival=?, inc=?, hier=?, filt=?, navi=?, indr=?, vistyp=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("ouid@model")));
            Utils.setBoolean(stat, 4, (Boolean)valueMap.get("is.autogeneratable"));
            if(((Boolean)valueMap.get("is.autogeneratable")).booleanValue())
            {
                stat.setString(5, (String)valueMap.get("prefix"));
                stat.setInt(6, ((Integer)valueMap.get("suffixlength")).intValue());
                stat.setInt(7, ((Integer)valueMap.get("initvalue")).intValue());
                stat.setInt(8, ((Integer)valueMap.get("increment")).intValue());
            } else
            {
                stat.setString(5, "");
                stat.setInt(6, 0);
                stat.setInt(7, 0);
                stat.setInt(8, 0);
            }
            Utils.setBoolean(stat, 9, (Boolean)valueMap.get("is.hierarchy"));
            stat.setString(10, (String)valueMap.get("filter"));
            Utils.setBoolean(stat, 11, (Boolean)valueMap.get("is.navigator"));
            Utils.setBoolean(stat, 12, (Boolean)valueMap.get("is.indirect"));
            if(valueMap.get("visual.type") == null)
                valueMap.put("visual.type", new Integer(1));
            Utils.setInt(stat, 13, (Integer)valueMap.get("visual.type"));
            stat.setLong(14, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSCode dosCode = (DOSCode)codeMap.get(ouid);
            if(dosCode == null)
            {
                dosCode = new DOSCode();
                dosCode.OUID = ouidLong;
                codeMap.put(ouid, dosCode);
            }
            String oldName = dosCode.name;
            dosCode.name = (String)valueMap.get("name");
            dosCode.description = (String)valueMap.get("description");
            dosCode._model = (DOSModel)codeMap.get((String)valueMap.get("ouid@model"));
            dosCode.isAutoGeneratable = (Boolean)valueMap.get("is.autogeneratable");
            dosCode.prefix = (String)valueMap.get("prefix");
            dosCode.suffixLength = (Integer)valueMap.get("suffixlength");
            dosCode.initValue = (Integer)valueMap.get("initvalue");
            dosCode.increment = (Integer)valueMap.get("increment");
            dosCode.hierarchy = (Boolean)valueMap.get("is.hierarchy");
            dosCode.filter = (String)valueMap.get("filter");
            dosCode.navigator = (Boolean)valueMap.get("is.navigator");
            dosCode.indirect = (Boolean)valueMap.get("is.indirect");
            dosCode.visualType = (Integer)valueMap.get("visual.type");
            DOSModel dosModel = dosCode._model;
            if(dosModel != null && dosModel.codeMap != null && dosModel.codeMap.size() > 0)
            {
                dosModel.codeMap.remove(oldName);
                dosModel.codeMap.put(dosCode.name, dosCode);
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
        DOSChangeable rootItem = DOSCodeItem.getCodeItemRoot(ouid, dtm, codeMap, dosCodeItemMap);
        if(rootItem == null)
        {
            return;
        } else
        {
            DOSCodeItem.setCodeItemHelper1((String)rootItem.get("ouid"), (String)valueMap.get("filter"), dosCodeItemMap);
            return;
        }
    }

    public static ArrayList listCode(String modelOuid, DTM dtm, HashMap dosModelMap, HashMap dosCodeMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSModel dosModel;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        dosModel = null;
        DOSCode dosCode = null;
        ArrayList returnList = null;
        TreeMap tempMap = null;
        TreeMap codeMap = null;
        DOSChangeable codeData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        dosModel = (DOSModel)dosModelMap.get(modelOuid);
        if(dosModel == null)
            throw new IIPRequestException("No such model.");
        if(dosModelMap.size() > 0 && dosCodeMap.size() > 0)
        {
            codeMap = dosModel.codeMap;
            if(codeMap != null && codeMap.size() > 0)
            {
                returnList = new ArrayList();
                Iterator mapKey;
                for(mapKey = codeMap.keySet().iterator(); mapKey.hasNext();)
                {
                    codeData = new DOSChangeable();
                    dosCode = (DOSCode)codeMap.get(mapKey.next());
                    codeData.put("ouid", Long.toHexString(dosCode.OUID));
                    codeData.put("name", dosCode.name);
                    codeData.put("description", dosCode.description);
                    DOSModel tempDosModel = dosCode._model;
                    if(tempDosModel != null)
                        codeData.put("ouid@model", Long.toHexString(tempDosModel.OUID));
                    tempDosModel = null;
                    codeData.put("is.autogeneratable", dosCode.isAutoGeneratable);
                    codeData.put("prefix", dosCode.prefix);
                    codeData.put("suffixlength", dosCode.suffixLength);
                    codeData.put("initvalue", dosCode.initValue);
                    codeData.put("increment", dosCode.increment);
                    codeData.put("is.hierarchy", dosCode.hierarchy);
                    codeData.put("filter", dosCode.filter);
                    codeData.put("is.navigator", dosCode.navigator);
                    codeData.put("is.indirect", dosCode.indirect);
                    codeData.put("visual.type", dosCode.visualType);
                    returnList.add(codeData);
                    codeData = null;
                    dosCode = null;
                }

                mapKey = null;
                codeData = null;
                codeMap = null;
                return returnList;
            }
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dc.ouid from doscod dc where dc.dosmod=? order by dc.name ");
            stat.setLong(1, Utils.convertOuidToLong(modelOuid));
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            TreeMap codeMap = dosModel.codeMap;
            if(codeMap == null)
            {
                codeMap = new TreeMap();
                dosModel.codeMap = codeMap;
            }
            DOSCode dosCode;
            DOSChangeable codeData;
            while(rs.next()) 
            {
                dosCode = (DOSCode)dosCodeMap.get(Long.toHexString(rs.getLong(1)));
                if(dosCode != null)
                {
                    codeData = new DOSChangeable();
                    codeData.put("ouid", Long.toHexString(dosCode.OUID));
                    codeData.put("name", dosCode.name);
                    codeData.put("description", dosCode.description);
                    DOSModel tempDosModel = dosCode._model;
                    if(tempDosModel != null)
                        codeData.put("ouid@model", Long.toHexString(tempDosModel.OUID));
                    tempDosModel = null;
                    codeData.put("is.autogeneratable", dosCode.isAutoGeneratable);
                    codeData.put("prefix", dosCode.prefix);
                    codeData.put("suffixlength", dosCode.suffixLength);
                    codeData.put("initvalue", dosCode.initValue);
                    codeData.put("increment", dosCode.increment);
                    codeData.put("is.hierarchy", dosCode.hierarchy);
                    codeData.put("filter", dosCode.filter);
                    codeData.put("is.navigator", dosCode.navigator);
                    codeData.put("is.indirect", dosCode.indirect);
                    codeData.put("visual.type", dosCode.visualType);
                    returnList.add(codeData);
                    codeMap.put(dosCode.name, dosCode);
                    codeData = null;
                    dosCode = null;
                }
            }
            dosModel.codeMap = codeMap;
            codeData = null;
            codeMap = null;
            dosCode = null;
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

    public static ArrayList listAllCode(DTM dtm, HashMap dosModelMap, HashMap dosCodeMap, HashMap dosCodeItemMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSCode dosCode = null;
        ArrayList returnList = null;
        DOSChangeable codeData = null;
        DOSChangeable rootItem = null;
        long ouidLong = 0L;
        String ouid = null;
        DOSModel dosModel = null;
        TreeMap tmpCodeMap = null;
        if(dosCodeMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = dosCodeMap.keySet().iterator(); mapKey.hasNext();)
            {
                codeData = new DOSChangeable();
                dosCode = (DOSCode)dosCodeMap.get(mapKey.next());
                if(dosCode.codeItemMap == null || dosCode.codeItemMap.isEmpty())
                    DOSCodeItem.listCodeItem(Long.toHexString(dosCode.OUID), dtm, dosCodeMap, dosCodeItemMap);
                codeData.put("ouid", Long.toHexString(dosCode.OUID));
                codeData.put("name", dosCode.name);
                codeData.put("description", dosCode.description);
                dosModel = dosCode._model;
                if(dosModel != null)
                    codeData.put("ouid@model", Long.toHexString(dosModel.OUID));
                codeData.put("is.autogeneratable", dosCode.isAutoGeneratable);
                codeData.put("prefix", dosCode.prefix);
                codeData.put("suffixlength", dosCode.suffixLength);
                codeData.put("initvalue", dosCode.initValue);
                codeData.put("increment", dosCode.increment);
                codeData.put("is.hierarchy", dosCode.hierarchy);
                codeData.put("filter", dosCode.filter);
                codeData.put("is.navigator", dosCode.navigator);
                codeData.put("is.indirect", dosCode.indirect);
                codeData.put("visual.type", dosCode.visualType);
                rootItem = DOSCodeItem.getCodeItemRoot((String)codeData.get("ouid"), dtm, dosCodeMap, dosCodeItemMap);
                if(rootItem != null)
                {
                    DOSCodeItem.setCodeItemHelper1((String)rootItem.get("ouid"), dosCode.filter, dosCodeItemMap);
                    rootItem = null;
                }
                codeData = null;
                dosCode = null;
                dosModel = null;
            }

            mapKey = null;
            codeData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dc.ouid, dc.name, dc.des, dc.dosmod, dc.autogen, dc.prefix, dc.len, dc.inival, dc.inc, dc.hier, dc.filt, dc.navi, dc.indr, dc.vistyp from doscod dc ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSChangeable codeData;
            while(rs.next()) 
            {
                codeData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                codeData.put("ouid", ouid);
                codeData.put("name", rs.getString(2));
                codeData.put("description", rs.getString(3));
                codeData.put("ouid@model", Long.toHexString(rs.getLong(4)));
                codeData.put("is.autogeneratable", Utils.getBoolean(rs, 5));
                codeData.put("prefix", rs.getString(6));
                codeData.put("suffixlength", Utils.getInteger(rs, 7));
                codeData.put("initvalue", Utils.getInteger(rs, 8));
                codeData.put("increment", Utils.getInteger(rs, 9));
                codeData.put("is.hierarchy", Utils.getBoolean(rs, 10));
                codeData.put("filter", rs.getString(11));
                codeData.put("is.navigator", Utils.getBoolean(rs, 12));
                codeData.put("is.indirect", Utils.getBoolean(rs, 13));
                codeData.put("visual.type", Utils.getInteger(rs, 14));
                returnList.add(codeData);
                DOSCode dosCode = new DOSCode();
                dosCode.OUID = ouidLong;
                dosCode.name = (String)codeData.get("name");
                dosCode.description = (String)codeData.get("description");
                dosCode._model = (DOSModel)dosModelMap.get((String)codeData.get("ouid@model"));
                dosCode.isAutoGeneratable = (Boolean)codeData.get("is.autogeneratable");
                dosCode.prefix = (String)codeData.get("prefix");
                dosCode.suffixLength = (Integer)codeData.get("suffixlength");
                dosCode.initValue = (Integer)codeData.get("initvalue");
                dosCode.increment = (Integer)codeData.get("increment");
                dosCode.hierarchy = (Boolean)codeData.get("is.hierarchy");
                dosCode.filter = (String)codeData.get("filter");
                dosCode.navigator = (Boolean)codeData.get("is.navigator");
                dosCode.indirect = (Boolean)codeData.get("is.indirect");
                dosCode.visualType = (Integer)codeData.get("visual.type");
                dosCodeMap.put(ouid, dosCode);
                if(dosModelMap.size() > 0)
                {
                    DOSModel dosModel = dosCode._model;
                    TreeMap tmpCodeMap = dosModel.codeMap;
                    if(tmpCodeMap == null)
                    {
                        tmpCodeMap = new TreeMap();
                        dosModel.codeMap = tmpCodeMap;
                    }
                    tmpCodeMap.put(dosCode.name, dosCode);
                    tmpCodeMap = null;
                    dosModel = null;
                }
                dosCode = null;
                codeData = null;
            }
            codeData = null;
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

    public static void getSQLPhraseForHierarchyCode(String codeItemOuid, HashMap codeMap, HashMap codeItemMap, StringBuffer sb)
        throws IIPRequestException
    {
        if(Utils.isNullString(codeItemOuid) || codeMap == null || codeItemMap == null)
            return;
        DOSCodeItem startCodeItem = (DOSCodeItem)codeItemMap.get(codeItemOuid);
        if(startCodeItem == null)
            return;
        if(startCodeItem._code == null)
            return;
        if(!Utils.getBoolean(startCodeItem._code.hierarchy))
        {
            sb.append('=');
            sb.append(Utils.convertOuidToLong(codeItemOuid));
            startCodeItem = null;
            return;
        } else
        {
            sb.append(" in (");
            sb.append(Utils.convertOuidToLong(codeItemOuid));
            getSQLPhraseForHierarchyCodeHelper(codeItemOuid, codeMap, codeItemMap, sb);
            sb.append(")");
            startCodeItem = null;
            return;
        }
    }

    private static void getSQLPhraseForHierarchyCodeHelper(String codeItemOuid, HashMap codeMap, HashMap codeItemMap, StringBuffer sb)
    {
        if(Utils.isNullString(codeItemOuid))
            return;
        DOSCodeItem dosCodeItem = (DOSCodeItem)codeItemMap.get(codeItemOuid);
        if(dosCodeItem == null)
            return;
        TreeMap children = dosCodeItem.children;
        dosCodeItem = null;
        if(children == null || children.isEmpty())
            return;
        Iterator childKey;
        for(childKey = children.keySet().iterator(); childKey.hasNext();)
        {
            dosCodeItem = (DOSCodeItem)children.get(childKey.next());
            sb.append(',');
            sb.append(dosCodeItem.OUID);
            getSQLPhraseForHierarchyCodeHelper(Long.toHexString(dosCodeItem.OUID), codeMap, codeItemMap, sb);
            dosCodeItem = null;
        }

        childKey = null;
    }

    public TreeMap codeItemMap;
    public DOSModel _model;
    public Boolean isAutoGeneratable;
    public String prefix;
    public Integer suffixLength;
    public Integer initValue;
    public Integer increment;
    public Boolean hierarchy;
    public String filter;
    public Boolean navigator;
    public Boolean indirect;
    public Integer visualType;
}
