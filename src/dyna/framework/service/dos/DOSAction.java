// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSAction.java

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
//            DOSObject, DOSChangeable, DOSClass, DOSActionParameter, 
//            DOSField, DOSFieldGroup

public class DOSAction extends DOSObject
{

    public DOSAction()
    {
        _class = null;
        returnType = null;
        returnTypeClass = null;
        scope = null;
        callType = null;
        isLeaf = null;
        isQuery = null;
        actionParameterList = null;
        isVisible = null;
        title = null;
        mnemonic = null;
        accelerator = null;
        icon = null;
        icon32 = null;
        icon24 = null;
    }

    public static String createAction(DOSChangeable actionDefinition, DTM dtm, HashMap dosClassMap, HashMap dosFieldMap, HashMap dosActionMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        TreeMap treeMap = null;
        DOSClass dosClass = null;
        LinkedList parmList = null;
        LinkedList tempList = null;
        Integer tempInteger = null;
        Boolean tempBoolean = null;
        String returnString = null;
        String tempString = null;
        long returnLong = 0L;
        int rows = 0;
        if(actionDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + actionDefinition);
        valueMap = actionDefinition.getValueMap();
        if(valueMap == null || valueMap.get("name") == null || valueMap.get("ouid@class") == null || valueMap.get("return.type") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dosact (ouid,name,des,dosclas,rtrntype,rtrntypeclas,scop,calltype,leaf,query,visb,tit,mnem,accl,icon, icon32, icon24 ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong((String)valueMap.get("ouid@class")));
            Utils.setByte(stat, 5, (Byte)valueMap.get("return.type"));
            String tempString = (String)valueMap.get("return.type.ouid@class");
            if(tempString != null)
                stat.setLong(6, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(6, 2);
            Utils.setByte(stat, 7, (Byte)valueMap.get("scope"));
            Utils.setByte(stat, 8, (Byte)valueMap.get("call.type"));
            Utils.setBoolean(stat, 9, (Boolean)valueMap.get("is.leaf"));
            Utils.setBoolean(stat, 10, (Boolean)valueMap.get("is.query"));
            Utils.setBoolean(stat, 11, (Boolean)valueMap.get("is.visible"));
            stat.setString(12, (String)valueMap.get("title"));
            stat.setString(13, (String)valueMap.get("mnemonic"));
            stat.setString(14, (String)valueMap.get("accelerator"));
            stat.setString(15, (String)valueMap.get("icon"));
            stat.setString(16, (String)valueMap.get("icon32"));
            stat.setString(17, (String)valueMap.get("icon24"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            DOSAction dosAction = new DOSAction();
            dosAction.OUID = returnLong;
            dosAction.name = (String)valueMap.get("name");
            dosAction.description = (String)valueMap.get("description");
            dosAction._class = (DOSClass)dosClassMap.get((String)valueMap.get("ouid@class"));
            dosAction.returnType = (Byte)valueMap.get("return.type");
            dosAction.returnTypeClass = (DOSClass)dosClassMap.get((String)valueMap.get("return.type.ouid@class"));
            dosAction.scope = (Byte)valueMap.get("scope");
            dosAction.callType = (Byte)valueMap.get("call.type");
            dosAction.isLeaf = (Boolean)valueMap.get("is.leaf");
            dosAction.isQuery = (Boolean)valueMap.get("is.query");
            dosAction.isVisible = (Boolean)valueMap.get("is.visible");
            dosAction.title = (String)valueMap.get("title");
            dosAction.mnemonic = (String)valueMap.get("mnemonic");
            dosAction.accelerator = (String)valueMap.get("accelerator");
            dosAction.icon = (String)valueMap.get("icon");
            dosAction.icon32 = (String)valueMap.get("icon32");
            dosAction.icon24 = (String)valueMap.get("icon24");
            dosActionMap.put(returnString, dosAction);
            LinkedList parmList = (LinkedList)valueMap.get("array$ActionParameter");
            if(parmList != null && parmList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosactparm (dosact, name, type, dfltval, dosfld ) values (?, ?, ?, ?, ? ) ");
                int i = 0;
                DOSActionParameter dosActionParm = null;
                DOSChangeable dosChangeable = null;
                LinkedList tempList = new LinkedList();
                Iterator parmKey;
                for(parmKey = parmList.iterator(); parmKey.hasNext();)
                {
                    i++;
                    dosChangeable = (DOSChangeable)parmKey.next();
                    stat.setLong(1, returnLong);
                    stat.setString(2, (String)dosChangeable.get("name"));
                    Utils.setByte(stat, 3, (Byte)dosChangeable.get("type"));
                    stat.setString(4, (String)dosChangeable.get("default.value"));
                    tempString = (String)dosChangeable.get("ouid@field.map");
                    if(!Utils.isNullString(tempString))
                        stat.setLong(5, Utils.convertOuidToLong(tempString));
                    else
                        stat.setNull(5, 2);
                    stat.executeUpdate();
                    dosActionParm = new DOSActionParameter();
                    dosActionParm.action = (DOSAction)dosActionMap.get(returnString);
                    dosActionParm.name = (String)dosChangeable.get("name");
                    dosActionParm.type = (Byte)dosChangeable.get("type");
                    dosActionParm.defaultValue = dosChangeable.get("default.value");
                    dosActionParm.fieldMapping = (DOSField)dosFieldMap.get((String)dosChangeable.get("ouid@field.map"));
                    tempList.add(dosActionParm);
                    dosActionParm = null;
                }

                stat.close();
                stat = null;
                dosAction.actionParameterList = tempList;
                parmKey = null;
                tempList = null;
            }
            con.commit();
            con.close();
            xc.close();
            DOSClass dosClass = dosAction._class;
            TreeMap treeMap;
            if(dosClass != null)
            {
                treeMap = dosClass.actionMap;
                if(treeMap == null)
                {
                    treeMap = new TreeMap();
                    dosClass.actionMap = treeMap;
                }
                treeMap.put(dosAction.name, dosAction);
            }
            treeMap = null;
            dosClass = null;
            dosAction = null;
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

    public static void removeAction(String ouid, DTM dtm, NDS nds, HashMap dosActionMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        DOSClass dosClass = null;
        DOSFieldGroup dosFieldGroup = null;
        TreeMap treeMap = null;
        LinkedList linkedList = null;
        Iterator treeKey = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from dosfldgrpact where dosact=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosact where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSAction dosAction = (DOSAction)dosActionMap.remove(ouid);
            dosClass = dosAction._class;
            if(dosClass != null)
            {
                treeMap = dosClass.actionMap;
                if(treeMap == null)
                {
                    treeMap = new TreeMap();
                    dosClass.actionMap = treeMap;
                }
                treeMap.remove(dosAction.name);
                treeMap = dosClass.fieldGroupMap;
                if(treeMap != null)
                {
                    for(treeKey = treeMap.keySet().iterator(); treeKey.hasNext();)
                    {
                        dosFieldGroup = (DOSFieldGroup)treeMap.get(treeKey.next());
                        if(dosFieldGroup != null)
                        {
                            linkedList = dosFieldGroup.fieldList;
                            if(linkedList != null)
                            {
                                linkedList = dosFieldGroup.actionList;
                                linkedList.remove(dosAction);
                            }
                        }
                    }

                    treeKey = null;
                }
            }
            treeMap = null;
            dosClass = null;
            dosAction = null;
            dosFieldGroup = null;
            removeActionScript(ouid, nds);
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

    public static DOSChangeable getAction(String ouid, HashMap dosActionMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSAction dosAction = (DOSAction)dosActionMap.get(ouid);
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList parmList = null;
        LinkedList tempList = null;
        DOSActionParameter dosActionParm = null;
        DOSChangeable dosChangeable = null;
        long tempLong = 0L;
        if(dosAction != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosAction.name);
            returnObject.put("description", dosAction.description);
            dosClass = dosAction._class;
            if(dosClass != null)
            {
                returnObject.put("ouid@class", Long.toHexString(dosAction._class.OUID));
                dosClass = null;
            }
            if(dosAction.actionParameterList != null && dosAction.actionParameterList.size() > 0)
            {
                parmList = new ArrayList(dosAction.actionParameterList.size());
                Iterator parmKey;
                for(parmKey = dosAction.actionParameterList.iterator(); parmKey.hasNext();)
                {
                    dosActionParm = (DOSActionParameter)parmKey.next();
                    dosChangeable = new DOSChangeable();
                    dosChangeable.put("ouid@action", Long.toHexString(dosAction.OUID));
                    dosChangeable.put("name", dosActionParm.name);
                    dosChangeable.put("type", dosActionParm.type);
                    dosChangeable.put("default.value", dosActionParm.defaultValue);
                    dosField = dosActionParm.fieldMapping;
                    if(dosField != null)
                    {
                        dosChangeable.put("ouid@field.map", Long.toHexString(dosField.OUID));
                        dosField = null;
                    }
                    parmList.add(dosChangeable);
                    dosChangeable = null;
                }

                returnObject.put("array$ActionParameter", parmList);
                parmKey = null;
                dosActionParm = null;
                parmList = null;
            }
            returnObject.put("return.type", dosAction.returnType);
            dosClass = dosAction.returnTypeClass;
            if(dosClass != null)
            {
                returnObject.put("return.type.ouid@class", Long.toHexString(dosClass.OUID));
                dosClass = null;
            }
            returnObject.put("scope", dosAction.scope);
            returnObject.put("call.type", dosAction.callType);
            returnObject.put("is.leaf", dosAction.isLeaf);
            returnObject.put("is.query", dosAction.isQuery);
            returnObject.put("is.visible", dosAction.isVisible);
            returnObject.put("title", dosAction.title);
            returnObject.put("mnemonic", dosAction.mnemonic);
            returnObject.put("accelerator", dosAction.accelerator);
            returnObject.put("icon", dosAction.icon);
            returnObject.put("icon32", dosAction.icon32);
            returnObject.put("icon24", dosAction.icon24);
            dosAction = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setAction(DOSChangeable actionDefinition, DTM dtm, HashMap dosClassMap, HashMap dosFieldMap, HashMap dosActionMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        if(actionDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + actionDefinition);
        HashMap valueMap = actionDefinition.getValueMap();
        DOSClass dosClass = null;
        TreeMap treeMap = null;
        ArrayList parmList = null;
        LinkedList tempList = null;
        String tempString = null;
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null || valueMap.get("ouid@class") == null || valueMap.get("return.type") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dosact set name=?, des=?, dosclas=?, rtrntype=?, rtrntypeclas=?, scop=?, calltype=?, leaf=?, query=?, visb=?, tit=?, mnem=?, accl=?, icon=?, icon32=?, icon24=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("ouid@class")));
            Utils.setByte(stat, 4, (Byte)valueMap.get("return.type"));
            tempString = (String)valueMap.get("return.type.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(5, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(5, 2);
            Utils.setByte(stat, 6, (Byte)valueMap.get("scope"));
            Utils.setByte(stat, 7, (Byte)valueMap.get("call.type"));
            Utils.setBoolean(stat, 8, (Boolean)valueMap.get("is.leaf"));
            Utils.setBoolean(stat, 9, (Boolean)valueMap.get("is.query"));
            Utils.setBoolean(stat, 10, (Boolean)valueMap.get("is.visible"));
            stat.setString(11, (String)valueMap.get("title"));
            stat.setString(12, (String)valueMap.get("mnemonic"));
            stat.setString(13, (String)valueMap.get("accelerator"));
            stat.setString(14, (String)valueMap.get("icon"));
            stat.setString(15, (String)valueMap.get("icon32"));
            stat.setString(16, (String)valueMap.get("icon24"));
            stat.setLong(17, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            DOSAction dosAction = (DOSAction)dosActionMap.get(ouid);
            if(dosAction == null)
            {
                dosAction = new DOSAction();
                dosAction.OUID = ouidLong;
            }
            String nameString = dosAction.name;
            dosAction.OUID = ouidLong;
            dosAction.name = (String)valueMap.get("name");
            dosAction.description = (String)valueMap.get("description");
            dosAction._class = (DOSClass)dosClassMap.get((String)valueMap.get("ouid@class"));
            dosAction.returnType = (Byte)valueMap.get("return.type");
            dosAction.returnTypeClass = (DOSClass)dosClassMap.get((String)valueMap.get("return.type.ouid@class"));
            dosAction.scope = (Byte)valueMap.get("scope");
            dosAction.callType = (Byte)valueMap.get("call.type");
            dosAction.isLeaf = (Boolean)valueMap.get("is.leaf");
            dosAction.isQuery = (Boolean)valueMap.get("is.query");
            dosAction.isVisible = (Boolean)valueMap.get("is.visible");
            dosAction.title = (String)valueMap.get("title");
            dosAction.mnemonic = (String)valueMap.get("mnemonic");
            dosAction.accelerator = (String)valueMap.get("accelerator");
            dosAction.icon = (String)valueMap.get("icon");
            dosAction.icon32 = (String)valueMap.get("icon32");
            dosAction.icon24 = (String)valueMap.get("icon24");
            dosActionMap.put(ouid, dosAction);
            parmList = (ArrayList)valueMap.get("array$ActionParameter");
            stat = con.prepareStatement("delete from dosactparm where dosact=? ");
            stat.setLong(1, ouidLong);
            stat.executeUpdate();
            stat.close();
            stat = null;
            if(parmList != null && parmList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosactparm (dosact, name, type, dfltval, dosfld ) values (?, ?, ?, ?, ? ) ");
                int i = 0;
                DOSActionParameter dosActionParm = null;
                DOSChangeable dosChangeable = null;
                tempList = new LinkedList();
                for(Iterator parmKey = parmList.iterator(); parmKey.hasNext();)
                {
                    i++;
                    dosChangeable = (DOSChangeable)parmKey.next();
                    stat.setLong(1, Utils.convertOuidToLong((String)dosChangeable.get("ouid@action")));
                    stat.setString(2, (String)dosChangeable.get("name"));
                    Utils.setByte(stat, 3, (Byte)dosChangeable.get("type"));
                    stat.setString(4, (String)dosChangeable.get("default.value"));
                    tempString = (String)dosChangeable.get("ouid@field.map");
                    if(!Utils.isNullString(tempString))
                        stat.setLong(5, Utils.convertOuidToLong(tempString));
                    else
                        stat.setNull(5, 2);
                    stat.executeUpdate();
                    dosActionParm = new DOSActionParameter();
                    dosActionParm.action = (DOSAction)dosActionMap.get((String)dosChangeable.get("ouid@action"));
                    dosActionParm.name = (String)dosChangeable.get("name");
                    dosActionParm.type = (Byte)dosChangeable.get("type");
                    dosActionParm.defaultValue = dosChangeable.get("default.value");
                    dosActionParm.fieldMapping = (DOSField)dosFieldMap.get((String)dosChangeable.get("ouid@field.map"));
                    tempList.add(dosActionParm);
                    dosActionParm = null;
                }

                stat.close();
                stat = null;
                Object obj = null;
            }
            if(dosAction.actionParameterList != null)
            {
                dosAction.actionParameterList.clear();
                dosAction.actionParameterList = null;
            }
            dosAction.actionParameterList = tempList;
            tempList = null;
            con.commit();
            con.close();
            xc.close();
            dosClass = dosAction._class;
            if(dosClass != null)
            {
                treeMap = dosClass.actionMap;
                if(treeMap == null)
                {
                    treeMap = new TreeMap();
                    dosClass.actionMap = treeMap;
                }
                treeMap.remove(nameString);
                treeMap.put(dosAction.name, dosAction);
            }
            treeMap = null;
            dosClass = null;
            dosAction = null;
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

    public static String getClassOuidOfAction(String ouid, DTM dtm, HashMap dosActionMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnString;
        long ouidLong;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnString = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSAction dosAction = (DOSAction)dosActionMap.get(ouid);
        long tempLong = 0L;
        if(dosAction != null && dosAction._class != null)
        {
            dosAction = null;
            return Long.toHexString(dosAction._class.OUID);
        }
        ouidLong = Utils.convertOuidToLong(ouid);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select da.dosact from dosact da where da.ouid=? ");
            stat.setLong(1, ouidLong);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnString = Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            DOSAction dosAction = null;
            con.commit();
            con.close();
            xc.close();
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
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public static ArrayList listAction(DTM dtm, HashMap dosClassMap, HashMap dosFieldMap, HashMap dosActionMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String ouid;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        DOSAction dosAction = null;
        DOSActionParameter dosActionParm = null;
        ArrayList returnList = null;
        ArrayList parmList = null;
        LinkedList tempList = null;
        DOSChangeable actionData = null;
        DOSChangeable dosChangeable = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        ouid = null;
        int rows = 0;
        if(dosActionMap.size() > 0)
        {
            returnList = new ArrayList();
            dosClass = null;
            Iterator mapKey;
            for(mapKey = dosActionMap.keySet().iterator(); mapKey.hasNext();)
            {
                actionData = new DOSChangeable();
                dosAction = (DOSAction)dosActionMap.get(mapKey.next());
                actionData.put("ouid", Long.toHexString(dosAction.OUID));
                actionData.put("name", dosAction.name);
                actionData.put("description", dosAction.description);
                dosClass = dosAction._class;
                if(dosClass != null)
                    actionData.put("ouid@class", Long.toHexString(dosClass.OUID));
                if(dosAction.actionParameterList != null && dosAction.actionParameterList.size() > 0)
                {
                    parmList = new ArrayList(dosAction.actionParameterList.size());
                    Iterator parmKey;
                    for(parmKey = dosAction.actionParameterList.iterator(); parmKey.hasNext();)
                    {
                        dosActionParm = (DOSActionParameter)parmKey.next();
                        dosChangeable = new DOSChangeable();
                        dosChangeable.put("ouid@action", Long.toHexString(dosAction.OUID));
                        dosChangeable.put("name", dosActionParm.name);
                        dosChangeable.put("type", dosActionParm.type);
                        dosChangeable.put("default.value", dosActionParm.defaultValue);
                        dosField = dosActionParm.fieldMapping;
                        if(dosField != null)
                        {
                            dosChangeable.put("ouid@field.map", Long.toHexString(dosField.OUID));
                            dosField = null;
                        }
                        parmList.add(dosChangeable);
                        dosChangeable = null;
                    }

                    actionData.put("array$ActionParameter", parmList);
                    parmKey = null;
                    dosActionParm = null;
                    parmList = null;
                }
                actionData.put("return.type", dosAction.returnType);
                dosClass = dosAction.returnTypeClass;
                if(dosClass != null)
                {
                    actionData.put("return.type.ouid@class", Long.toHexString(dosClass.OUID));
                    dosClass = null;
                }
                actionData.put("scope", dosAction.scope);
                actionData.put("call.type", dosAction.callType);
                actionData.put("is.leaf", dosAction.isLeaf);
                actionData.put("is.query", dosAction.isQuery);
                actionData.put("is.visible", dosAction.isVisible);
                actionData.put("title", dosAction.title);
                actionData.put("mnemonic", dosAction.mnemonic);
                actionData.put("accelerator", dosAction.accelerator);
                actionData.put("icon", dosAction.icon);
                actionData.put("icon32", dosAction.icon32);
                actionData.put("icon24", dosAction.icon24);
                returnList.add(actionData);
                actionData = null;
                dosAction = null;
                dosClass = null;
            }

            mapKey = null;
            actionData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select da.ouid, da.name, da.des, da.dosclas, da.rtrntype, da.rtrntypeclas, da.scop, da.calltype, da.leaf, da.query, da.visb, da.tit, da.mnem, da.accl, da.icon, da.icon32, da.icon24 from dosact da ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSAction dosAction;
            DOSChangeable actionData;
            for(; rs.next(); dosActionMap.put(ouid, dosAction))
            {
                actionData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                ouid = Long.toHexString(ouidLong);
                actionData.put("ouid", ouid);
                actionData.put("name", rs.getString(2));
                actionData.put("description", rs.getString(3));
                actionData.put("ouid@class", Long.toHexString(rs.getLong(4)));
                actionData.put("return.type", Utils.getByte(rs, 5));
                long tempLong = rs.getLong(6);
                if(tempLong > 0L)
                    actionData.put("return.type.ouid@class", Long.toHexString(tempLong));
                actionData.put("scope", Utils.getByte(rs, 7));
                actionData.put("call.type", Utils.getByte(rs, 8));
                actionData.put("is.leaf", Utils.getBoolean(rs, 9));
                actionData.put("is.query", Utils.getBoolean(rs, 10));
                actionData.put("is.visible", Utils.getBoolean(rs, 11));
                actionData.put("title", rs.getString(12));
                actionData.put("mnemonic", rs.getString(13));
                actionData.put("accelerator", rs.getString(14));
                actionData.put("icon", rs.getString(15));
                actionData.put("icon32", rs.getString(16));
                actionData.put("icon24", rs.getString(17));
                returnList.add(actionData);
                dosAction = new DOSAction();
                dosAction.OUID = ouidLong;
                dosAction.name = (String)actionData.get("name");
                dosAction.description = (String)actionData.get("description");
                dosAction._class = (DOSClass)dosClassMap.get((String)actionData.get("ouid@class"));
                dosAction.returnType = (Byte)actionData.get("return.type");
                dosAction.returnTypeClass = (DOSClass)dosClassMap.get((String)actionData.get("return.type.ouid@class"));
                dosAction.scope = (Byte)actionData.get("scope");
                dosAction.callType = (Byte)actionData.get("call.type");
                dosAction.isLeaf = (Boolean)actionData.get("is.leaf");
                dosAction.isQuery = (Boolean)actionData.get("is.query");
                dosAction.isVisible = (Boolean)actionData.get("is.visible");
                dosAction.title = (String)actionData.get("title");
                dosAction.mnemonic = (String)actionData.get("mnemonic");
                dosAction.accelerator = (String)actionData.get("accelerator");
                dosAction.icon = (String)actionData.get("icon");
                dosAction.icon32 = (String)actionData.get("icon32");
                dosAction.icon24 = (String)actionData.get("icon24");
            }

            actionData = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select dap.name, dap.type, dap.dfltval, dap.dosfld from dosactparm dap where dap.dosact=? order by dap.dosact, dap.name ");
            for(Iterator actionKey = returnList.iterator(); actionKey.hasNext();)
            {
                actionData = (DOSChangeable)actionKey.next();
                DOSAction dosAction = (DOSAction)dosActionMap.get(ouid);
                ouid = (String)actionData.get("ouid");
                long ouidLong = Utils.convertOuidToLong(ouid);
                stat.setLong(1, ouidLong);
                rs = stat.executeQuery();
                ArrayList parmList = new ArrayList();
                LinkedList tempList = new LinkedList();
                while(rs.next()) 
                {
                    DOSChangeable dosChangeable = new DOSChangeable();
                    dosChangeable.put("ouid@action", ouid);
                    dosChangeable.put("name", rs.getString(1));
                    dosChangeable.put("type", Utils.getByte(rs, 2));
                    dosChangeable.put("default.value", rs.getString(3));
                    long tempLong = rs.getLong(4);
                    if(tempLong > 0L)
                        dosChangeable.put("ouid@field.map", Long.toHexString(tempLong));
                    parmList.add(dosChangeable);
                    DOSActionParameter dosActionParm = new DOSActionParameter();
                    dosActionParm.action = dosAction;
                    dosActionParm.name = (String)dosChangeable.get("name");
                    dosActionParm.type = (Byte)dosChangeable.get("type");
                    dosActionParm.defaultValue = dosChangeable.get("default.value");
                    dosActionParm.fieldMapping = (DOSField)dosFieldMap.get((String)dosChangeable.get("ouid@field.map"));
                    tempList.add(dosActionParm);
                    dosChangeable = null;
                    dosActionParm = null;
                }
                if(dosAction.actionParameterList != null)
                {
                    dosAction.actionParameterList.clear();
                    dosAction.actionParameterList = null;
                }
                dosAction.actionParameterList = tempList;
                actionData.put("array$ActionParameter", parmList);
                parmList = null;
                tempList = null;
                dosAction = null;
                actionData = null;
                rs.close();
                rs = null;
            }

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

    public static boolean removeActionScript(String ouid, NDS nds)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String scriptName = nds.getValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
        List tmpList = Utils.tokenizeMessage(scriptName, '.');
        String extType = null;
        if(((String)tmpList.get(1)).equals("py"))
            extType = "Python";
        else
        if(((String)tmpList.get(1)).equals("tcl"))
            extType = "Tcl";
        else
        if(((String)tmpList.get(1)).equals("java"))
            extType = "Beanshell";
        boolean result = nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
        nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/ACTION/" + ouid);
        ArrayList list = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/ACTION");
        if(Utils.isNullArrayList(list))
        {
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/EVENT");
            list = null;
        }
        return result;
    }

    public DOSClass _class;
    public Byte returnType;
    public DOSClass returnTypeClass;
    public Byte scope;
    public Byte callType;
    public Boolean isLeaf;
    public Boolean isQuery;
    public LinkedList actionParameterList;
    public Boolean isVisible;
    public String title;
    public String mnemonic;
    public String accelerator;
    public String icon;
    public String icon32;
    public String icon24;
}
