// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSFieldGroup.java

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
//            DOSObject, DOSChangeable, DOSClass, DOSField, 
//            DOSAction

public class DOSFieldGroup extends DOSObject
{

    public DOSFieldGroup()
    {
        _class = null;
        index = null;
        fieldList = null;
        actionList = null;
        isMandatory = null;
        title = null;
        toolTip = null;
        icon = null;
        isVisible = null;
        layout = null;
        location = null;
    }

    public static String createFieldGroup(DOSChangeable fieldGroupDefinition, DTM dtm, HashMap classMap, HashMap fieldMap, HashMap actionMap, HashMap fieldGroupMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        DOSField dosField = null;
        DOSAction dosAction = null;
        DOSClass dosClass = null;
        TreeMap treeMap = null;
        ArrayList arrayList = null;
        LinkedList linkedList = null;
        Integer tempInteger = null;
        Boolean tempBoolean = null;
        String returnString = null;
        String tempString = null;
        long returnLong = 0L;
        int rows = 0;
        if(fieldGroupDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldGroupDefinition);
        valueMap = fieldGroupDefinition.getValueMap();
        if(valueMap == null || valueMap.get("name") == null || valueMap.get("ouid@class") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dosfldgrp (ouid,name,des,dosclas,indx,mand,tit,tooltip,visb,layo,icon, loc ) values (?,?,?,?,?,?,?,?,?,?,?,nvl(?, 2)) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong((String)valueMap.get("ouid@class")));
            Utils.setInt(stat, 5, (Integer)valueMap.get("index"));
            Utils.setBoolean(stat, 6, (Boolean)valueMap.get("is.mandatory"));
            stat.setString(7, (String)valueMap.get("title"));
            stat.setString(8, (String)valueMap.get("tooltip"));
            Utils.setBoolean(stat, 9, (Boolean)valueMap.get("is.visible"));
            Utils.setByte(stat, 10, (Byte)valueMap.get("layout"));
            stat.setString(11, (String)valueMap.get("icon"));
            Utils.setInt(stat, 12, (Integer)valueMap.get("location"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            DOSFieldGroup dosFieldGroup = new DOSFieldGroup();
            dosFieldGroup.OUID = returnLong;
            dosFieldGroup.name = (String)valueMap.get("name");
            dosFieldGroup.description = (String)valueMap.get("description");
            dosFieldGroup._class = (DOSClass)classMap.get((String)valueMap.get("ouid@class"));
            dosFieldGroup.index = (Integer)valueMap.get("index");
            dosFieldGroup.isMandatory = (Boolean)valueMap.get("is.mandatory");
            dosFieldGroup.title = (String)valueMap.get("title");
            dosFieldGroup.toolTip = (String)valueMap.get("tooltip");
            dosFieldGroup.isVisible = (Boolean)valueMap.get("is.visible");
            dosFieldGroup.layout = (Byte)valueMap.get("layout");
            dosFieldGroup.icon = (String)valueMap.get("icon");
            dosFieldGroup.location = (Integer)valueMap.get("location");
            if(dosFieldGroup.location == null)
                dosFieldGroup.location = LOCATION_CENTER;
            fieldGroupMap.put(returnString, dosFieldGroup);
            long tempLong = 0L;
            ArrayList arrayList = (ArrayList)valueMap.get("array$ouid@field");
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosfldgrpfld (dosfldgrp, dosfld) values (?, ? ) ");
                LinkedList linkedList = new LinkedList();
                for(Iterator listKey = arrayList.iterator(); listKey.hasNext(); stat.executeUpdate())
                {
                    String tempString = (String)listKey.next();
                    tempLong = Utils.convertOuidToLong(tempString);
                    stat.setLong(1, returnLong);
                    stat.setLong(2, tempLong);
                    linkedList.add(fieldMap.get(tempString));
                }

                stat.close();
                stat = null;
                dosFieldGroup.fieldList = linkedList;
                linkedList = null;
            }
            arrayList = null;
            arrayList = (ArrayList)valueMap.get("array$ouid@action");
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosfldgrpact (dosfldgrp, dosact) values (?, ? ) ");
                LinkedList linkedList = new LinkedList();
                for(Iterator listKey = arrayList.iterator(); listKey.hasNext(); stat.executeUpdate())
                {
                    String tempString = (String)listKey.next();
                    tempLong = Utils.convertOuidToLong(tempString);
                    stat.setLong(1, returnLong);
                    stat.setLong(2, tempLong);
                    linkedList.add(actionMap.get(tempString));
                }

                stat.close();
                stat = null;
                dosFieldGroup.actionList = linkedList;
                linkedList = null;
            }
            arrayList = null;
            con.commit();
            con.close();
            xc.close();
            DOSClass dosClass = dosFieldGroup._class;
            TreeMap treeMap;
            if(dosClass != null)
            {
                treeMap = dosClass.fieldGroupMap;
                if(treeMap == null)
                {
                    treeMap = new TreeMap();
                    dosClass.fieldGroupMap = treeMap;
                }
                treeMap.put(dosFieldGroup.name, dosFieldGroup);
            }
            treeMap = null;
            dosClass = null;
            dosFieldGroup = null;
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

    public static void removeFieldGroup(String ouid, DTM dtm, NDS nds, HashMap fieldGroupMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        DOSFieldGroup dosFieldGroup = null;
        DOSClass dosClass = null;
        TreeMap treeMap = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from dosfldgrpfld where dosfldgrp=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosfldgrpact where dosfldgrp=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosfldgrp where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            dosFieldGroup = (DOSFieldGroup)fieldGroupMap.remove(ouid);
            if(dosFieldGroup != null)
            {
                dosClass = dosFieldGroup._class;
                if(dosClass != null)
                {
                    treeMap = dosClass.fieldGroupMap;
                    if(treeMap != null)
                        treeMap.remove(dosFieldGroup.name);
                }
            }
            treeMap = null;
            dosClass = null;
            dosFieldGroup = null;
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD/" + ouid);
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

    public static DOSChangeable getFieldGroup(String ouid, HashMap fieldGroupMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSFieldGroup dosFieldGroup = (DOSFieldGroup)fieldGroupMap.get(ouid);
        DOSField dosField = null;
        DOSAction dosAction = null;
        long tempLong = 0L;
        ArrayList arrayList = null;
        LinkedList linkedList = null;
        Iterator listKey = null;
        if(dosFieldGroup != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosFieldGroup.name);
            returnObject.put("description", dosFieldGroup.description);
            if(dosFieldGroup._class != null)
                returnObject.put("ouid@class", Long.toHexString(dosFieldGroup._class.OUID));
            linkedList = dosFieldGroup.fieldList;
            if(linkedList != null && linkedList.size() > 0)
            {
                arrayList = new ArrayList();
                for(listKey = linkedList.iterator(); listKey.hasNext();)
                {
                    dosField = (DOSField)listKey.next();
                    if(dosField != null)
                        arrayList.add(Long.toHexString(dosField.OUID));
                    dosField = null;
                }

                returnObject.put("array$ouid@field", arrayList);
                arrayList = null;
            }
            linkedList = dosFieldGroup.actionList;
            if(linkedList != null && linkedList.size() > 0)
            {
                arrayList = new ArrayList();
                for(listKey = linkedList.iterator(); listKey.hasNext();)
                {
                    dosAction = (DOSAction)listKey.next();
                    if(dosAction != null)
                        arrayList.add(Long.toHexString(dosAction.OUID));
                    dosAction = null;
                }

                returnObject.put("array$ouid@action", arrayList);
                arrayList = null;
            }
            returnObject.put("index", dosFieldGroup.index);
            returnObject.put("is.mandatory", dosFieldGroup.isMandatory);
            returnObject.put("title", dosFieldGroup.title);
            returnObject.put("tooltip", dosFieldGroup.toolTip);
            returnObject.put("is.visible", dosFieldGroup.isVisible);
            returnObject.put("layout", dosFieldGroup.layout);
            returnObject.put("icon", dosFieldGroup.icon);
            returnObject.put("location", dosFieldGroup.location);
            dosFieldGroup = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setFieldGroup(DOSChangeable fieldGroupDefinition, DTM dtm, NDS nds, HashMap classMap, HashMap fieldMap, HashMap actionMap, HashMap fieldGroupMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        DOSAction dosAction = null;
        TreeMap treeMap = null;
        if(fieldGroupDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldGroupDefinition);
        HashMap valueMap = fieldGroupDefinition.getValueMap();
        String tempString = null;
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null || valueMap.get("ouid@class") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dosfldgrp set name=?, des=?, dosclas=?, indx=?, mand=?, tit=?, tooltip=?, visb=?, layo=?, icon=?, loc=nvl(?,2) where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("ouid@class")));
            Utils.setInt(stat, 4, (Integer)valueMap.get("index"));
            Utils.setBoolean(stat, 5, (Boolean)valueMap.get("is.mandatory"));
            stat.setString(6, (String)valueMap.get("title"));
            stat.setString(7, (String)valueMap.get("tooltip"));
            Utils.setBoolean(stat, 8, (Boolean)valueMap.get("is.visible"));
            Utils.setByte(stat, 9, (Byte)valueMap.get("layout"));
            stat.setString(10, (String)valueMap.get("icon"));
            Utils.setInt(stat, 11, (Integer)valueMap.get("location"));
            stat.setLong(12, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            DOSFieldGroup dosFieldGroup = (DOSFieldGroup)fieldGroupMap.get(ouid);
            if(dosFieldGroup == null)
            {
                dosFieldGroup = new DOSFieldGroup();
                dosFieldGroup.OUID = ouidLong;
            }
            String nameString = dosFieldGroup.name;
            dosFieldGroup.OUID = ouidLong;
            dosFieldGroup.name = (String)valueMap.get("name");
            dosFieldGroup.description = (String)valueMap.get("description");
            dosFieldGroup._class = (DOSClass)classMap.get((String)valueMap.get("ouid@class"));
            dosFieldGroup.index = (Integer)valueMap.get("index");
            dosFieldGroup.isMandatory = (Boolean)valueMap.get("is.mandatory");
            dosFieldGroup.title = (String)valueMap.get("title");
            dosFieldGroup.toolTip = (String)valueMap.get("tooltip");
            dosFieldGroup.isVisible = (Boolean)valueMap.get("is.visible");
            dosFieldGroup.layout = (Byte)valueMap.get("layout");
            dosFieldGroup.icon = (String)valueMap.get("icon");
            dosFieldGroup.location = (Integer)valueMap.get("location");
            if(dosFieldGroup.location == null)
                dosFieldGroup.location = LOCATION_CENTER;
            fieldGroupMap.put(ouid, dosFieldGroup);
            ArrayList arrayList = (ArrayList)valueMap.get("array$ouid@field");
            LinkedList linkedList = null;
            long tempLong = 0L;
            stat = con.prepareStatement("delete from dosfldgrpfld where dosfldgrp=? ");
            stat.setLong(1, ouidLong);
            stat.executeUpdate();
            stat.close();
            stat = null;
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosfldgrpfld (dosfldgrp, dosfld) values (?, ? ) ");
                linkedList = new LinkedList();
                for(Iterator listKey = arrayList.iterator(); listKey.hasNext(); stat.executeUpdate())
                {
                    tempString = (String)listKey.next();
                    tempLong = Utils.convertOuidToLong(tempString);
                    stat.setLong(1, ouidLong);
                    stat.setLong(2, tempLong);
                    linkedList.add(fieldMap.get(tempString));
                }

                stat.close();
                stat = null;
            }
            dosFieldGroup.fieldList = linkedList;
            linkedList = null;
            arrayList = null;
            arrayList = (ArrayList)valueMap.get("array$ouid@action");
            stat = con.prepareStatement("delete from dosfldgrpact where dosfldgrp=? ");
            stat.setLong(1, ouidLong);
            stat.executeUpdate();
            stat.close();
            stat = null;
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosfldgrpact (dosfldgrp, dosact) values (?, ? ) ");
                linkedList = new LinkedList();
                for(Iterator listKey = arrayList.iterator(); listKey.hasNext(); stat.executeUpdate())
                {
                    tempString = (String)listKey.next();
                    tempLong = Utils.convertOuidToLong(tempString);
                    stat.setLong(1, ouidLong);
                    stat.setLong(2, tempLong);
                    linkedList.add(actionMap.get(tempString));
                }

                stat.close();
                stat = null;
            }
            dosFieldGroup.actionList = linkedList;
            linkedList = null;
            arrayList = null;
            arrayList = (ArrayList)valueMap.get("array$ouid@unused.inherited.field");
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD/" + ouid);
            if(arrayList != null && arrayList.size() > 0)
            {
                nds.addNode("::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD", ouid, "DOS.INHERIT", "DOS.INHERIT");
                for(Iterator listKey = arrayList.iterator(); listKey.hasNext(); nds.addNode("::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD/" + ouid, tempString, "DOS.INHERIT", tempString))
                    tempString = (String)listKey.next();

            }
            con.commit();
            con.close();
            xc.close();
            dosClass = dosFieldGroup._class;
            if(dosClass != null)
            {
                treeMap = dosClass.fieldGroupMap;
                if(treeMap == null)
                {
                    treeMap = new TreeMap();
                    dosClass.fieldGroupMap = treeMap;
                }
                treeMap.remove(nameString);
                treeMap.put(dosFieldGroup.name, dosFieldGroup);
            }
            treeMap = null;
            dosClass = null;
            dosFieldGroup = null;
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

    public static ArrayList listFieldGroup(DTM dtm, HashMap classMap, HashMap fieldMap, HashMap actionMap, HashMap fieldGroupMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSFieldGroup dosFieldGroup = null;
        DOSAction dosAction = null;
        DOSField dosField = null;
        ArrayList returnList = null;
        LinkedList linkedList = null;
        ArrayList arrayList = null;
        Iterator listKey = null;
        Iterator mapKey = null;
        DOSChangeable fieldGroupData = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        String ouid = null;
        int rows = 0;
        if(fieldGroupMap.size() > 0)
        {
            returnList = new ArrayList();
            for(mapKey = fieldGroupMap.keySet().iterator(); mapKey.hasNext();)
            {
                fieldGroupData = new DOSChangeable();
                dosFieldGroup = (DOSFieldGroup)fieldGroupMap.get(mapKey.next());
                fieldGroupData.put("ouid", Long.toHexString(dosFieldGroup.OUID));
                fieldGroupData.put("name", dosFieldGroup.name);
                fieldGroupData.put("description", dosFieldGroup.description);
                if(dosFieldGroup._class != null)
                    fieldGroupData.put("ouid@class", Long.toHexString(dosFieldGroup._class.OUID));
                linkedList = dosFieldGroup.fieldList;
                if(linkedList != null && linkedList.size() > 0)
                {
                    arrayList = new ArrayList();
                    for(listKey = linkedList.iterator(); listKey.hasNext();)
                    {
                        dosField = (DOSField)listKey.next();
                        if(dosField != null)
                            arrayList.add(Long.toHexString(dosField.OUID));
                        dosField = null;
                    }

                    fieldGroupData.put("array$ouid@field", arrayList);
                    arrayList = null;
                }
                linkedList = dosFieldGroup.actionList;
                if(linkedList != null && linkedList.size() > 0)
                {
                    arrayList = new ArrayList();
                    for(listKey = linkedList.iterator(); listKey.hasNext();)
                    {
                        dosAction = (DOSAction)listKey.next();
                        if(dosAction != null)
                            arrayList.add(Long.toHexString(dosAction.OUID));
                        dosAction = null;
                    }

                    fieldGroupData.put("array$ouid@action", arrayList);
                    arrayList = null;
                }
                fieldGroupData.put("index", dosFieldGroup.index);
                fieldGroupData.put("is.mandatory", dosFieldGroup.isMandatory);
                fieldGroupData.put("title", dosFieldGroup.title);
                fieldGroupData.put("tooltip", dosFieldGroup.toolTip);
                fieldGroupData.put("is.visible", dosFieldGroup.isVisible);
                fieldGroupData.put("layout", dosFieldGroup.layout);
                fieldGroupData.put("icon", dosFieldGroup.icon);
                fieldGroupData.put("location", dosFieldGroup.location);
                returnList.add(fieldGroupData);
                fieldGroupData = null;
                dosFieldGroup = null;
            }

            mapKey = null;
            fieldGroupData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dfg.ouid, dfg.name, dfg.des, dfg.dosclas, dfg.indx, dfg.mand, dfg.tit, dfg.tooltip, dfg.visb, dfg.layo, dfg.icon, dfg.loc from dosfldgrp dfg ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable fieldGroupData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                fieldGroupData.put("ouid", ouid);
                fieldGroupData.put("name", rs.getString(2));
                fieldGroupData.put("description", rs.getString(3));
                fieldGroupData.put("ouid@class", Long.toHexString(rs.getLong(4)));
                fieldGroupData.put("index", Utils.getInteger(rs, 5));
                fieldGroupData.put("is.mandatory", Utils.getBoolean(rs, 6));
                fieldGroupData.put("title", rs.getString(7));
                fieldGroupData.put("tooltip", rs.getString(8));
                fieldGroupData.put("is.visible", Utils.getBoolean(rs, 9));
                fieldGroupData.put("layout", Utils.getByte(rs, 10));
                fieldGroupData.put("icon", rs.getString(11));
                fieldGroupData.put("location", Utils.getInteger(rs, 12));
                returnList.add(fieldGroupData);
                DOSFieldGroup dosFieldGroup = new DOSFieldGroup();
                dosFieldGroup.OUID = ouidLong;
                dosFieldGroup.name = (String)fieldGroupData.get("name");
                dosFieldGroup.description = (String)fieldGroupData.get("description");
                dosFieldGroup._class = (DOSClass)classMap.get((String)fieldGroupData.get("ouid@class"));
                dosFieldGroup.index = (Integer)fieldGroupData.get("index");
                dosFieldGroup.isMandatory = (Boolean)fieldGroupData.get("is.mandatory");
                dosFieldGroup.title = (String)fieldGroupData.get("title");
                dosFieldGroup.toolTip = (String)fieldGroupData.get("tooltip");
                dosFieldGroup.isVisible = (Boolean)fieldGroupData.get("is.visible");
                dosFieldGroup.layout = (Byte)fieldGroupData.get("layout");
                dosFieldGroup.icon = (String)fieldGroupData.get("icon");
                dosFieldGroup.location = (Integer)fieldGroupData.get("location");
                fieldGroupMap.put(ouid, dosFieldGroup);
                dosFieldGroup = null;
                fieldGroupData = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select df.ouid from dosfldgrpfld dfgf, dosfld df where df.ouid=dfgf.dosfld and dfgf.dosfldgrp=? order by df.indx,df.name ");
            Iterator listKey;
            for(listKey = returnList.iterator(); listKey.hasNext();)
            {
                DOSChangeable fieldGroupData = (DOSChangeable)listKey.next();
                String ouid = (String)fieldGroupData.get("ouid");
                long ouidLong = Utils.convertOuidToLong(ouid);
                DOSFieldGroup dosFieldGroup = (DOSFieldGroup)fieldGroupMap.get(ouid);
                LinkedList linkedList = new LinkedList();
                ArrayList arrayList = new ArrayList();
                stat.setLong(1, ouidLong);
                for(rs = stat.executeQuery(); rs.next();)
                {
                    long tempLong = rs.getLong(1);
                    ouid = Long.toHexString(tempLong);
                    DOSField dosField = (DOSField)fieldMap.get(ouid);
                    if(dosField != null)
                    {
                        arrayList.add(ouid);
                        linkedList.add(dosField);
                    }
                    dosField = null;
                }

                if(arrayList.size() > 0)
                {
                    fieldGroupData.put("array$ouid@field", arrayList);
                    dosFieldGroup.fieldList = linkedList;
                }
                arrayList = null;
                linkedList = null;
                fieldGroupData = null;
                dosFieldGroup = null;
                rs.close();
                rs = null;
            }

            listKey = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select da.ouid from dosfldgrpact dfga, dosact da where da.ouid=dfga.dosact and dfga.dosfldgrp=? order by da.name ");
            for(listKey = returnList.iterator(); listKey.hasNext();)
            {
                DOSChangeable fieldGroupData = (DOSChangeable)listKey.next();
                String ouid = (String)fieldGroupData.get("ouid");
                long ouidLong = Utils.convertOuidToLong(ouid);
                DOSFieldGroup dosFieldGroup = (DOSFieldGroup)fieldGroupMap.get(ouid);
                LinkedList linkedList = new LinkedList();
                ArrayList arrayList = new ArrayList();
                stat.setLong(1, ouidLong);
                for(rs = stat.executeQuery(); rs.next();)
                {
                    long tempLong = rs.getLong(1);
                    ouid = Long.toHexString(tempLong);
                    DOSAction dosAction = (DOSAction)actionMap.get(ouid);
                    if(dosAction != null)
                    {
                        arrayList.add(ouid);
                        linkedList.add(dosAction);
                    }
                    dosAction = null;
                }

                if(arrayList.size() > 0)
                {
                    fieldGroupData.put("array$ouid@action", arrayList);
                    dosFieldGroup.actionList = linkedList;
                }
                arrayList = null;
                linkedList = null;
                fieldGroupData = null;
                dosFieldGroup = null;
                rs.close();
                rs = null;
            }

            listKey = null;
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

    public DOSClass _class;
    public Integer index;
    public LinkedList fieldList;
    public LinkedList actionList;
    public Boolean isMandatory;
    public String title;
    public String toolTip;
    public String icon;
    public Boolean isVisible;
    public Byte layout;
    public Integer location;
    public static final Integer LOCATION_TOP = new Integer(1);
    public static final Integer LOCATION_CENTER = new Integer(2);
    public static final Integer LOCATION_LEFT = new Integer(3);
    public static final Integer LOCATION_BOTTOM = new Integer(4);
    public static final Integer LOCATION_POPUP = new Integer(100);
    public static final Integer LOCATION_RESULT = new Integer(110);

}
