// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSField.java

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
//            DOSObject, DOSChangeable, DOSClass, DOSCode, 
//            DOSFieldGroup, DOSPackage

public class DOSField extends DOSObject
{

    public DOSField()
    {
        _class = null;
        type = null;
        typeClass = null;
        typeCode = null;
        multiplicity_from = null;
        multiplicity_to = null;
        changeable = null;
        scope = null;
        initialValue = null;
        index = null;
        code = null;
        isMandatory = null;
        isVisible = null;
        size = null;
        title = null;
        tooltip = null;
        width = null;
        height = null;
        titleWidth = null;
        isTitleVisible = null;
        icon = null;
        column = null;
        referencedFieldOuid = 0L;
    }

    public static String createField(DOSChangeable fieldDefinition, DTM dtm, DOSPackage foundationPackage, HashMap dosClassMap, HashMap dosCodeMap, HashMap dosFieldMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String tempString;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        TreeMap treeMap = null;
        DOSClass dosClass = null;
        Integer tempInteger = null;
        Boolean tempBoolean = null;
        String returnString = null;
        tempString = null;
        long returnLong = 0L;
        int rows = 0;
        if(fieldDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldDefinition);
        valueMap = fieldDefinition.getValueMap();
        if(valueMap == null || valueMap.get("name") == null || valueMap.get("ouid@class") == null || valueMap.get("type") == null || valueMap.get("code") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        tempString = (String)valueMap.get("code");
        tempString = tempString.toLowerCase();
        dosClass = (DOSClass)dosClassMap.get(valueMap.get("ouid@class"));
        if(dosClass != null && dosClass._package == foundationPackage && (tempString.equals("vf$ouid") || tempString.equals("vf$identity") || tempString.equals("vf$version") || tempString.equals("sf$ouid") || tempString.equals("md$ouid") || tempString.equals("md$user") || tempString.equals("md$status") || tempString.equals("md$cdate") || tempString.equals("md$mdate") || tempString.equals("md$number") || tempString.equals("md$description") || tempString.equals("md$desc") || tempString.equals("as$end1") || tempString.equals("as$end2")))
            throw new IIPRequestException("Reserved word used, 'code': " + tempString);
        dosClass = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            valueMap.put("code", tempString);
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dosfld (ouid,name,des,dosclas,type,typeclas,multfrom,multto,chng,scop,initval,indx,code,mand,visb,lim,tit,tooltip,wdth,hght,titwdth,titvisb,icon, clm ) values (?,?,?,?,?,?,?,?,?,?,?,nvl(?,1),?,?,?,?,?,?,?,?,?,?,?,nvl(?,0) ) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong((String)valueMap.get("ouid@class")));
            Utils.setByte(stat, 5, (Byte)valueMap.get("type"));
            tempString = (String)valueMap.get("type.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(6, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(6, 2);
            Utils.setInt(stat, 7, (Integer)valueMap.get("multiplicity.from"));
            Utils.setInt(stat, 8, (Integer)valueMap.get("multiplicity.to"));
            Utils.setByte(stat, 9, (Byte)valueMap.get("changeable"));
            Utils.setByte(stat, 10, (Byte)valueMap.get("scope"));
            stat.setString(11, (String)valueMap.get("initial.value"));
            Utils.setInt(stat, 12, (Integer)valueMap.get("index"));
            stat.setString(13, (String)valueMap.get("code"));
            Utils.setBoolean(stat, 14, (Boolean)valueMap.get("is.mandatory"));
            Utils.setBoolean(stat, 15, (Boolean)valueMap.get("is.visible"));
            Utils.setDouble(stat, 16, (Double)valueMap.get("size"));
            stat.setString(17, (String)valueMap.get("title"));
            stat.setString(18, (String)valueMap.get("tooltip"));
            Utils.setInt(stat, 19, (Integer)valueMap.get("width"));
            Utils.setInt(stat, 20, (Integer)valueMap.get("height"));
            Utils.setInt(stat, 21, (Integer)valueMap.get("title.width"));
            Utils.setBoolean(stat, 22, (Boolean)valueMap.get("is.title.visible"));
            stat.setString(23, (String)valueMap.get("icon"));
            Utils.setInt(stat, 24, (Integer)valueMap.get("column"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSField dosField = new DOSField();
            dosField.OUID = returnLong;
            dosField.name = (String)valueMap.get("name");
            dosField.description = (String)valueMap.get("description");
            dosField._class = (DOSClass)dosClassMap.get((String)valueMap.get("ouid@class"));
            dosField.type = (Byte)valueMap.get("type");
            if(dosField.type.byteValue() == 16)
            {
                if(Utils.isNullString((String)valueMap.get("type.ouid@class")))
                    dosField.typeClass = null;
                else
                    dosField.typeClass = (DOSClass)dosClassMap.get((String)valueMap.get("type.ouid@class"));
            } else
            if(dosField.type.byteValue() == 24)
            {
                if(Utils.isNullString((String)valueMap.get("type.ouid@class")))
                    dosField.typeCode = null;
                else
                    dosField.typeCode = (DOSCode)dosCodeMap.get((String)valueMap.get("type.ouid@class"));
            } else
            if(dosField.type.byteValue() == 25)
                if(Utils.isNullString((String)valueMap.get("type.ouid@class")))
                    dosField.referencedFieldOuid = 0L;
                else
                    dosField.referencedFieldOuid = Utils.convertOuidToLong((String)valueMap.get("type.ouid@class"));
            dosField.multiplicity_from = (Integer)valueMap.get("multiplicity.from");
            dosField.multiplicity_to = (Integer)valueMap.get("multiplicity.to");
            dosField.changeable = (Byte)valueMap.get("changeable");
            dosField.scope = (Byte)valueMap.get("scope");
            dosField.initialValue = valueMap.get("initial.value");
            dosField.index = (Integer)valueMap.get("index");
            dosField.code = (String)valueMap.get("code");
            dosField.isMandatory = (Boolean)valueMap.get("is.mandatory");
            dosField.isVisible = (Boolean)valueMap.get("is.visible");
            dosField.size = (Double)valueMap.get("size");
            dosField.title = (String)valueMap.get("title");
            dosField.tooltip = (String)valueMap.get("tooltip");
            dosField.width = (Integer)valueMap.get("width");
            dosField.height = (Integer)valueMap.get("height");
            dosField.titleWidth = (Integer)valueMap.get("title.width");
            dosField.isTitleVisible = (Boolean)valueMap.get("is.title.visible");
            dosField.icon = (String)valueMap.get("icon");
            dosField.column = (Integer)valueMap.get("column");
            if(dosField.index == null)
                dosField.index = new Integer(1);
            dosFieldMap.put(returnString, dosField);
            DOSClass dosClass = dosField._class;
            TreeMap treeMap;
            if(dosClass != null)
            {
                treeMap = dosClass.fieldMap;
                if(treeMap == null)
                {
                    treeMap = new TreeMap();
                    dosClass.fieldMap = treeMap;
                }
                treeMap.put(dosField.name, dosField);
            }
            treeMap = null;
            dosClass = null;
            dosField = null;
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

    public static void removeField(String ouid, DTM dtm, HashMap dosFieldMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        DOSField dosField = null;
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
            stat = con.prepareStatement("delete from dosfldgrpfld where dosfld=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosfld where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            dosField = (DOSField)dosFieldMap.remove(ouid);
            if(dosField != null)
            {
                dosClass = dosField._class;
                if(dosClass != null)
                {
                    treeMap = dosClass.fieldMap;
                    if(treeMap != null)
                        treeMap.remove(dosField.name);
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
                                    linkedList = dosFieldGroup.fieldList;
                                    linkedList.remove(dosField);
                                }
                            }
                        }

                        treeKey = null;
                    }
                }
            }
            linkedList = null;
            treeMap = null;
            dosClass = null;
            dosField = null;
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

    public static DOSChangeable getField(String ouid, HashMap dosFieldMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSField dosField = (DOSField)dosFieldMap.get(ouid);
        long tempLong = 0L;
        if(dosField != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosField.name);
            returnObject.put("description", dosField.description);
            if(dosField._class != null)
                returnObject.put("ouid@class", Long.toHexString(dosField._class.OUID));
            returnObject.put("type", dosField.type);
            if(dosField.typeClass != null)
                returnObject.put("type.ouid@class", Long.toHexString(dosField.typeClass.OUID));
            if(dosField.typeCode != null)
                returnObject.put("type.ouid@class", Long.toHexString(dosField.typeCode.OUID));
            if(dosField.referencedFieldOuid != 0L)
                returnObject.put("type.ouid@class", Long.toHexString(dosField.referencedFieldOuid));
            returnObject.put("multiplicity.from", dosField.multiplicity_from);
            returnObject.put("multiplicity.to", dosField.multiplicity_to);
            returnObject.put("changeable", dosField.changeable);
            returnObject.put("scope", dosField.scope);
            returnObject.put("initial.value", dosField.initialValue);
            returnObject.put("index", dosField.index);
            returnObject.put("code", dosField.code);
            returnObject.put("is.mandatory", dosField.isMandatory);
            returnObject.put("is.visible", dosField.isVisible);
            returnObject.put("size", dosField.size);
            returnObject.put("title", dosField.title);
            returnObject.put("tooltip", dosField.tooltip);
            returnObject.put("width", dosField.width);
            returnObject.put("height", dosField.height);
            returnObject.put("title.width", dosField.titleWidth);
            returnObject.put("is.title.visible", dosField.isTitleVisible);
            returnObject.put("icon", dosField.icon);
            returnObject.put("column", dosField.column);
            dosField = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setField(DOSChangeable fieldDefinition, DTM dtm, DOSPackage foundationPackage, HashMap dosClassMap, HashMap dosCodeMap, HashMap dosFieldMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        DOSClass dosClass = null;
        TreeMap treeMap = null;
        if(fieldDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldDefinition);
        HashMap valueMap = fieldDefinition.getValueMap();
        String tempString = null;
        byte type = 0;
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null || valueMap.get("ouid@class") == null || valueMap.get("type") == null || valueMap.get("code") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        tempString = ((String)valueMap.get("code")).toLowerCase();
        dosClass = (DOSClass)dosClassMap.get(valueMap.get("ouid@class"));
        if(dosClass != null && dosClass._package == foundationPackage && (tempString.equals("vf$ouid") || tempString.equals("vf$identity") || tempString.equals("vf$version") || tempString.equals("sf$ouid") || tempString.equals("md$ouid") || tempString.equals("md$user") || tempString.equals("md$status") || tempString.equals("md$cdate") || tempString.equals("md$mdate") || tempString.equals("md$number") || tempString.equals("as$end1") || tempString.equals("as$end2")))
            throw new IIPRequestException("Reserved word used, 'code': " + tempString);
        dosClass = null;
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        type = Utils.getByte((Byte)valueMap.get("type"));
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dosfld set name=?, des=?, dosclas=?, type=?, typeclas=?, multfrom=?, multto=?, chng=?, scop=?, initval=?, indx=nvl(?,1), code=?, mand=?, visb=?, lim=?, tit=?, tooltip=?, wdth=?, hght=?, titwdth=?, titvisb=?, icon=?, clm=nvl(?,0) where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("ouid@class")));
            Utils.setByte(stat, 4, (Byte)valueMap.get("type"));
            tempString = (String)valueMap.get("type.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(5, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(5, 2);
            Utils.setInt(stat, 6, (Integer)valueMap.get("multiplicity.from"));
            Utils.setInt(stat, 7, (Integer)valueMap.get("multiplicity.to"));
            Utils.setByte(stat, 8, (Byte)valueMap.get("changeable"));
            Utils.setByte(stat, 9, (Byte)valueMap.get("scope"));
            stat.setString(10, (String)valueMap.get("initial.value"));
            Utils.setInt(stat, 11, (Integer)valueMap.get("index"));
            stat.setString(12, (String)valueMap.get("code"));
            Utils.setBoolean(stat, 13, (Boolean)valueMap.get("is.mandatory"));
            Utils.setBoolean(stat, 14, (Boolean)valueMap.get("is.visible"));
            Utils.setDouble(stat, 15, (Double)valueMap.get("size"));
            stat.setString(16, (String)valueMap.get("title"));
            stat.setString(17, (String)valueMap.get("tooltip"));
            Utils.setInt(stat, 18, (Integer)valueMap.get("width"));
            Utils.setInt(stat, 19, (Integer)valueMap.get("height"));
            Utils.setInt(stat, 20, (Integer)valueMap.get("title.width"));
            Utils.setBoolean(stat, 21, (Boolean)valueMap.get("is.title.visible"));
            stat.setString(22, (String)valueMap.get("icon"));
            Utils.setInt(stat, 23, (Integer)valueMap.get("column"));
            stat.setLong(24, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSField dosField = (DOSField)dosFieldMap.get(ouid);
            if(dosField == null)
            {
                dosField = new DOSField();
                dosField.OUID = ouidLong;
                dosFieldMap.put(ouid, dosField);
            }
            String nameString = dosField.name;
            dosField.name = (String)valueMap.get("name");
            dosField.description = (String)valueMap.get("description");
            dosField._class = (DOSClass)dosClassMap.get((String)valueMap.get("ouid@class"));
            dosField.type = (Byte)valueMap.get("type");
            if(dosField.type.byteValue() == 16)
            {
                if(Utils.isNullString((String)valueMap.get("type.ouid@class")))
                    dosField.typeClass = null;
                else
                    dosField.typeClass = (DOSClass)dosClassMap.get((String)valueMap.get("type.ouid@class"));
            } else
            if(dosField.type.byteValue() == 24)
            {
                if(Utils.isNullString((String)valueMap.get("type.ouid@class")))
                    dosField.typeCode = null;
                else
                    dosField.typeCode = (DOSCode)dosCodeMap.get((String)valueMap.get("type.ouid@class"));
            } else
            if(dosField.type.byteValue() == 25)
                if(Utils.isNullString((String)valueMap.get("type.ouid@class")))
                    dosField.referencedFieldOuid = 0L;
                else
                    dosField.referencedFieldOuid = Utils.convertOuidToLong((String)valueMap.get("type.ouid@class"));
            dosField.multiplicity_from = (Integer)valueMap.get("multiplicity.from");
            dosField.multiplicity_to = (Integer)valueMap.get("multiplicity.to");
            dosField.changeable = (Byte)valueMap.get("changeable");
            dosField.scope = (Byte)valueMap.get("scope");
            dosField.initialValue = valueMap.get("initial.value");
            dosField.index = (Integer)valueMap.get("index");
            dosField.code = (String)valueMap.get("code");
            dosField.isMandatory = (Boolean)valueMap.get("is.mandatory");
            dosField.isVisible = (Boolean)valueMap.get("is.visible");
            dosField.size = (Double)valueMap.get("size");
            dosField.title = (String)valueMap.get("title");
            dosField.tooltip = (String)valueMap.get("tooltip");
            dosField.width = (Integer)valueMap.get("width");
            dosField.height = (Integer)valueMap.get("height");
            dosField.titleWidth = (Integer)valueMap.get("title.width");
            dosField.isTitleVisible = (Boolean)valueMap.get("is.title.visible");
            dosField.icon = (String)valueMap.get("icon");
            dosField.column = (Integer)valueMap.get("column");
            if(dosField.index == null)
                dosField.index = new Integer(1);
            dosClass = dosField._class;
            if(dosClass != null)
            {
                treeMap = dosClass.fieldMap;
                if(treeMap != null)
                {
                    treeMap.remove(nameString);
                    treeMap.put(dosField.name, dosField);
                }
            }
            treeMap = null;
            dosClass = null;
            dosField = null;
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

    public static String getClassOuidOfField(String ouid, DTM dtm, HashMap dosFieldMap)
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
        DOSField dosField = (DOSField)dosFieldMap.get(ouid);
        long tempLong = 0L;
        if(dosField != null && dosField._class != null)
        {
            dosField = null;
            return Long.toHexString(dosField._class.OUID);
        }
        ouidLong = Utils.convertOuidToLong(ouid);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select df.dosclas from dosfld df where df.ouid=? ");
            stat.setLong(1, ouidLong);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnString = Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            DOSField dosField = null;
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

    public static ArrayList listField(DTM dtm, HashMap dosClassMap, HashMap dosCodeMap, HashMap dosFieldMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSField dosField = null;
        ArrayList returnList = null;
        DOSChangeable fieldData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        if(dosFieldMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = dosFieldMap.keySet().iterator(); mapKey.hasNext();)
            {
                fieldData = new DOSChangeable();
                dosField = (DOSField)dosFieldMap.get(mapKey.next());
                fieldData.put("ouid", Long.toHexString(dosField.OUID));
                fieldData.put("name", dosField.name);
                fieldData.put("description", dosField.description);
                if(dosField._class != null)
                    fieldData.put("ouid@class", Long.toHexString(dosField._class.OUID));
                fieldData.put("type", dosField.type);
                if(dosField.typeClass != null)
                    fieldData.put("type.ouid@class", Long.toHexString(dosField.typeClass.OUID));
                if(dosField.typeCode != null)
                    fieldData.put("type.ouid@class", Long.toHexString(dosField.typeCode.OUID));
                if(dosField.referencedFieldOuid != 0L)
                    fieldData.put("type.ouid@class", Long.toHexString(dosField.referencedFieldOuid));
                fieldData.put("multiplicity.from", dosField.multiplicity_from);
                fieldData.put("multiplicity.to", dosField.multiplicity_to);
                fieldData.put("changeable", dosField.changeable);
                fieldData.put("scope", dosField.scope);
                fieldData.put("initial.value", dosField.initialValue);
                fieldData.put("index", dosField.index);
                fieldData.put("code", dosField.code);
                fieldData.put("is.mandatory", dosField.isMandatory);
                fieldData.put("is.visible", dosField.isVisible);
                fieldData.put("size", dosField.size);
                fieldData.put("title", dosField.title);
                fieldData.put("tooltip", dosField.tooltip);
                fieldData.put("width", dosField.width);
                fieldData.put("height", dosField.height);
                fieldData.put("title.width", dosField.titleWidth);
                fieldData.put("is.title.visible", dosField.isTitleVisible);
                fieldData.put("icon", dosField.icon);
                fieldData.put("column", dosField.column);
                returnList.add(fieldData);
                fieldData = null;
                dosField = null;
            }

            mapKey = null;
            fieldData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select df.ouid, df.name, df.des, df.dosclas, df.type, df.typeclas, df.multfrom, df.multto, df.chng, df.scop, df.initval, df.indx, df.code, df.mand, df.visb, df.lim, df.tit, df.tooltip, df.wdth, df.hght, df.titwdth, df.titvisb, df.icon, df.clm from dosfld df ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSChangeable fieldData;
            while(rs.next()) 
            {
                fieldData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                fieldData.put("ouid", ouid);
                fieldData.put("name", rs.getString(2));
                fieldData.put("description", rs.getString(3));
                fieldData.put("ouid@class", Long.toHexString(rs.getLong(4)));
                fieldData.put("type", Utils.getByte(rs, 5));
                fieldData.put("type.ouid@class", Long.toHexString(rs.getLong(6)));
                fieldData.put("multiplicity.from", Utils.getInteger(rs, 7));
                fieldData.put("multiplicity.to", Utils.getInteger(rs, 8));
                fieldData.put("changeable", Utils.getByte(rs, 9));
                fieldData.put("scope", Utils.getByte(rs, 10));
                fieldData.put("initial.value", rs.getString(11));
                fieldData.put("index", Utils.getInteger(rs, 12));
                fieldData.put("code", rs.getString(13));
                fieldData.put("is.mandatory", Utils.getBoolean(rs, 14));
                fieldData.put("is.visible", Utils.getBoolean(rs, 15));
                fieldData.put("size", Utils.getDouble(rs, 16));
                fieldData.put("title", rs.getString(17));
                fieldData.put("tooltip", rs.getString(18));
                fieldData.put("width", Utils.getInteger(rs, 19));
                fieldData.put("height", Utils.getInteger(rs, 20));
                fieldData.put("title.width", Utils.getInteger(rs, 21));
                fieldData.put("is.title.visible", Utils.getBoolean(rs, 22));
                fieldData.put("icon", rs.getString(23));
                fieldData.put("column", Utils.getInteger(rs, 24));
                returnList.add(fieldData);
                DOSField dosField = new DOSField();
                dosField.OUID = ouidLong;
                dosField.name = (String)fieldData.get("name");
                dosField.description = (String)fieldData.get("description");
                dosField._class = (DOSClass)dosClassMap.get((String)fieldData.get("ouid@class"));
                dosField.type = (Byte)fieldData.get("type");
                if(dosField.type.byteValue() == 16)
                    dosField.typeClass = (DOSClass)dosClassMap.get((String)fieldData.get("type.ouid@class"));
                if(dosField.type.byteValue() == 24)
                    dosField.typeCode = (DOSCode)dosCodeMap.get((String)fieldData.get("type.ouid@class"));
                if(dosField.type.byteValue() == 25)
                    dosField.referencedFieldOuid = Utils.convertOuidToLong((String)fieldData.get("type.ouid@class"));
                dosField.multiplicity_from = (Integer)fieldData.get("multiplicity.from");
                dosField.multiplicity_to = (Integer)fieldData.get("multiplicity.to");
                dosField.changeable = (Byte)fieldData.get("changeable");
                dosField.scope = (Byte)fieldData.get("scope");
                dosField.initialValue = fieldData.get("initial.value");
                dosField.index = (Integer)fieldData.get("index");
                dosField.code = (String)fieldData.get("code");
                dosField.isMandatory = (Boolean)fieldData.get("is.mandatory");
                dosField.isVisible = (Boolean)fieldData.get("is.visible");
                dosField.size = (Double)fieldData.get("size");
                dosField.title = (String)fieldData.get("title");
                dosField.tooltip = (String)fieldData.get("tooltip");
                dosField.width = (Integer)fieldData.get("width");
                dosField.height = (Integer)fieldData.get("height");
                dosField.titleWidth = (Integer)fieldData.get("title.width");
                dosField.isTitleVisible = (Boolean)fieldData.get("is.title.visible");
                dosField.icon = (String)fieldData.get("icon");
                dosField.column = (Integer)fieldData.get("column");
                dosFieldMap.put(ouid, dosField);
                dosField = null;
                fieldData = null;
            }
            fieldData = null;
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

    public static void setCodeSelectionTable(String fieldOuid, ArrayList selectionTable, HashMap fieldMap, NDS nds)
        throws IIPRequestException
    {
        if(Utils.isNullString(fieldOuid) || fieldMap == null || nds == null)
            return;
        if(!createNdsBase(nds))
            return;
        if(!fieldMap.containsKey(fieldOuid))
            return;
        nds.removeNode("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid);
        if(Utils.isNullArrayList(selectionTable))
            return;
        boolean result = false;
        result = nds.addNode("::/DOS_SYSTEM_DIRECTORY/CODESELECTION", fieldOuid, "CODESELECTION", fieldOuid);
        if(!result)
            return;
        HashMap tempMap = null;
        String number = null;
        String codeOuid = null;
        HashMap fieldTable = null;
        String tempFieldOuid = null;
        String codeItemOuid = null;
        String initialOuid = null;
        Iterator fieldKey = null;
        Iterator tableKey;
        for(tableKey = selectionTable.iterator(); tableKey.hasNext();)
        {
            tempMap = (HashMap)tableKey.next();
            number = (String)tempMap.get("number");
            codeOuid = (String)tempMap.get("code");
            if(!Utils.isNullString(codeOuid))
                if("visual.type.of.combobox".equals(number))
                {
                    nds.addArgument("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid, "visual.type.of.combobox", codeOuid);
                } else
                {
                    initialOuid = (String)tempMap.get("initial");
                    result = nds.addNode("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid, number, "CODESELECTION", codeOuid);
                    if(result)
                    {
                        if(!Utils.isNullString(initialOuid))
                            nds.addArgument("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid + "/" + number, "initial", initialOuid);
                        fieldTable = (HashMap)tempMap.get("fieldMap");
                        if(fieldTable != null && !fieldTable.isEmpty())
                        {
                            for(fieldKey = fieldTable.keySet().iterator(); fieldKey.hasNext(); nds.addNode("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid + "/" + number, tempFieldOuid, "CODESELECTION", codeItemOuid))
                            {
                                tempFieldOuid = (String)fieldKey.next();
                                codeItemOuid = (String)fieldTable.get(tempFieldOuid);
                                if(Utils.isNullString(codeItemOuid))
                                    codeItemOuid = "";
                            }

                            fieldKey = null;
                            tempMap = null;
                        }
                    }
                }
        }

        tableKey = null;
    }

    public static ArrayList getCodeSelectionTable(String fieldOuid, HashMap fieldMap, NDS nds)
        throws IIPRequestException
    {
        if(Utils.isNullString(fieldOuid) || fieldMap == null || nds == null)
            return null;
        if(!createNdsBase(nds))
            return null;
        if(!fieldMap.containsKey(fieldOuid))
            return null;
        ArrayList tempList = nds.getChildNodes("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid);
        if(Utils.isNullArrayList(tempList))
            return null;
        ArrayList resultList = new ArrayList();
        HashMap aNode = null;
        String initialOuid = null;
        HashMap fieldTable = null;
        ArrayList fieldList = null;
        HashMap tableNode = null;
        HashMap tempFieldMap = null;
        Iterator fieldKey = null;
        Iterator tableKey;
        for(tableKey = tempList.iterator(); tableKey.hasNext();)
        {
            tableNode = (HashMap)tableKey.next();
            if(tableNode != null)
            {
                initialOuid = nds.getArgument("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid + "/" + (String)tableNode.get("name"), "initial");
                fieldList = nds.getChildNodes("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid + "/" + (String)tableNode.get("name"));
                if(!Utils.isNullArrayList(fieldList))
                {
                    fieldTable = new HashMap();
                    for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
                    {
                        tempFieldMap = (HashMap)fieldKey.next();
                        if(tempFieldMap != null && !tempFieldMap.isEmpty() && tempFieldMap.containsKey("name"))
                            fieldTable.put(tempFieldMap.get("name"), tempFieldMap.get("value"));
                    }

                    fieldKey = null;
                    if(fieldTable != null && !fieldTable.isEmpty())
                    {
                        aNode = new HashMap();
                        aNode.put("number", tableNode.get("name"));
                        aNode.put("code", tableNode.get("value"));
                        aNode.put("initial", initialOuid);
                        aNode.put("fieldMap", fieldTable);
                        resultList.add(aNode);
                        aNode = null;
                        fieldTable = null;
                        tableNode = null;
                    }
                }
            }
        }

        tableKey = null;
        aNode = new HashMap();
        aNode.put("number", "visual.type.of.combobox");
        aNode.put("code", nds.getArgument("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid, "visual.type.of.combobox"));
        resultList.add(aNode);
        return resultList;
    }

    public static boolean isComboBoxMatrixCode(NDS nds, String fieldOuid)
        throws IIPRequestException
    {
        return Utils.getBoolean(new Boolean(nds.getArgument("::/DOS_SYSTEM_DIRECTORY/CODESELECTION/" + fieldOuid, "visual.type.of.combobox")), false);
    }

    public static String lookupCodeFromSelectionTable(String fieldOuid, DOSChangeable instance, ArrayList selectionTable, HashMap fieldMap, HashMap codeMap, HashMap codeItemMap)
        throws IIPRequestException
    {
        if(Utils.isNullString(fieldOuid) || instance == null || selectionTable == null || fieldMap == null || codeMap == null || codeItemMap == null)
            return null;
        if(!fieldMap.containsKey(fieldOuid))
            return null;
        DOSCode dosCode = null;
        DOSCodeItem dosCodeItem = null;
        DOSField dosField = null;
        HashMap tempFieldMap = null;
        HashMap aNode = null;
        String tempFieldOuid = null;
        String codeItemOuid = null;
        String tempCodeItemOuid = null;
        String codeOuid = null;
        boolean isSuitable = false;
        Iterator fieldKey = null;
        Iterator tableKey = selectionTable.iterator();
        while(tableKey.hasNext()) 
        {
            aNode = (HashMap)tableKey.next();
            if(aNode == null || aNode.isEmpty())
                continue;
            codeOuid = (String)aNode.get("code");
            tempFieldMap = (HashMap)aNode.get("fieldMap");
            if(tempFieldMap == null || tempFieldMap.isEmpty())
                continue;
            isSuitable = true;
            fieldKey = tempFieldMap.keySet().iterator();
            while(fieldKey.hasNext()) 
            {
                tempFieldOuid = (String)fieldKey.next();
                tempCodeItemOuid = (String)tempFieldMap.get(tempFieldOuid);
                if(Utils.isNullString(tempCodeItemOuid))
                    continue;
                dosField = (DOSField)fieldMap.get(tempFieldOuid);
                if(dosField == null)
                {
                    isSuitable = false;
                    break;
                }
                codeItemOuid = (String)instance.get(dosField.name);
                if(Utils.isNullString(codeItemOuid))
                {
                    codeItemOuid = (String)instance.get(Long.toString(dosField.OUID, 16));
                    if(Utils.isNullString(codeItemOuid))
                    {
                        isSuitable = false;
                        break;
                    }
                }
                if(codeItemOuid.equals(tempCodeItemOuid))
                    continue;
                isSuitable = false;
                break;
            }
            fieldKey = null;
            if(isSuitable)
                break;
            aNode = null;
        }
        tableKey = null;
        if(isSuitable && !Utils.isNullString(codeOuid))
            return codeOuid;
        else
            return null;
    }

    public static String lookupCodeFromSelectionTable(String fieldOuid, String relatedFieldOuid, DOSChangeable instance, ArrayList selectionTable, HashMap fieldMap, HashMap codeMap, HashMap codeItemMap)
        throws IIPRequestException
    {
        if(Utils.isNullString(fieldOuid) || instance == null || selectionTable == null || fieldMap == null || codeMap == null || codeItemMap == null)
            return null;
        if(!fieldMap.containsKey(fieldOuid))
            return null;
        DOSCode dosCode = null;
        DOSCodeItem dosCodeItem = null;
        DOSField dosField = null;
        HashMap tempFieldMap = null;
        HashMap aNode = null;
        String tempFieldOuid = null;
        String codeItemOuid = null;
        String tempCodeItemOuid = null;
        String codeOuid = null;
        boolean isSuitable = false;
        boolean foundRelatedOuid = false;
        Iterator fieldKey = null;
        Iterator tableKey = selectionTable.iterator();
        while(tableKey.hasNext()) 
        {
            aNode = (HashMap)tableKey.next();
            if(aNode == null || aNode.isEmpty())
                continue;
            codeOuid = (String)aNode.get("code");
            tempFieldMap = (HashMap)aNode.get("fieldMap");
            if(tempFieldMap == null || tempFieldMap.isEmpty())
                continue;
            isSuitable = true;
            fieldKey = tempFieldMap.keySet().iterator();
            while(fieldKey.hasNext()) 
            {
                tempFieldOuid = (String)fieldKey.next();
                if(tempFieldOuid.equals(relatedFieldOuid))
                    foundRelatedOuid = true;
                tempCodeItemOuid = (String)tempFieldMap.get(tempFieldOuid);
                if(Utils.isNullString(tempCodeItemOuid))
                    continue;
                dosField = (DOSField)fieldMap.get(tempFieldOuid);
                if(dosField == null)
                {
                    isSuitable = false;
                    break;
                }
                codeItemOuid = (String)instance.get(dosField.name);
                if(Utils.isNullString(codeItemOuid))
                {
                    codeItemOuid = (String)instance.get(Long.toString(dosField.OUID, 16));
                    if(Utils.isNullString(codeItemOuid))
                    {
                        isSuitable = false;
                        break;
                    }
                }
                if(codeItemOuid.equals(tempCodeItemOuid))
                    continue;
                isSuitable = false;
                break;
            }
            fieldKey = null;
            if(isSuitable)
                break;
            aNode = null;
        }
        tableKey = null;
        if(isSuitable && !Utils.isNullString(codeOuid) && foundRelatedOuid)
            return codeOuid;
        else
            return null;
    }

    public static String lookupInitialCodeFromSelectionTable(String fieldOuid, DOSChangeable instance, ArrayList selectionTable, HashMap fieldMap, HashMap codeMap, HashMap codeItemMap)
        throws IIPRequestException
    {
        if(Utils.isNullString(fieldOuid) || instance == null || selectionTable == null || fieldMap == null || codeMap == null || codeItemMap == null)
            return null;
        if(!fieldMap.containsKey(fieldOuid))
            return null;
        DOSCode dosCode = null;
        DOSCodeItem dosCodeItem = null;
        DOSField dosField = null;
        HashMap tempFieldMap = null;
        HashMap aNode = null;
        String tempFieldOuid = null;
        String codeItemOuid = null;
        String tempCodeItemOuid = null;
        String initialOuid = null;
        String codeOuid = null;
        boolean isSuitable = false;
        Iterator fieldKey = null;
        Iterator tableKey = selectionTable.iterator();
        while(tableKey.hasNext()) 
        {
            aNode = (HashMap)tableKey.next();
            if(aNode == null || aNode.isEmpty())
                continue;
            codeOuid = (String)aNode.get("code");
            initialOuid = (String)aNode.get("initial");
            tempFieldMap = (HashMap)aNode.get("fieldMap");
            if(tempFieldMap == null || tempFieldMap.isEmpty())
                continue;
            isSuitable = true;
            fieldKey = tempFieldMap.keySet().iterator();
            while(fieldKey.hasNext()) 
            {
                tempFieldOuid = (String)fieldKey.next();
                tempCodeItemOuid = (String)tempFieldMap.get(tempFieldOuid);
                if(Utils.isNullString(tempCodeItemOuid))
                    continue;
                dosField = (DOSField)fieldMap.get(tempFieldOuid);
                if(dosField == null)
                {
                    isSuitable = false;
                    break;
                }
                codeItemOuid = (String)instance.get(dosField.name);
                if(Utils.isNullString(codeItemOuid))
                {
                    codeItemOuid = (String)instance.get(Long.toString(dosField.OUID, 16));
                    if(Utils.isNullString(codeItemOuid))
                    {
                        isSuitable = false;
                        break;
                    }
                }
                if(codeItemOuid.equals(tempCodeItemOuid))
                    continue;
                isSuitable = false;
                break;
            }
            fieldKey = null;
            if(isSuitable)
                break;
            aNode = null;
        }
        tableKey = null;
        if(isSuitable && !Utils.isNullString(initialOuid))
            return initialOuid;
        else
            return null;
    }

    private static boolean createNdsBase(NDS nds)
        throws IIPRequestException
    {
        if(nds.getValue("::/DOS_SYSTEM_DIRECTORY/CODESELECTION") == null)
            return nds.addNode("::/DOS_SYSTEM_DIRECTORY", "CODESELECTION", "CODESELECTION", "CODESELECTION");
        else
            return true;
    }

    public DOSClass _class;
    public Byte type;
    public DOSClass typeClass;
    public DOSCode typeCode;
    public Integer multiplicity_from;
    public Integer multiplicity_to;
    public Byte changeable;
    public Byte scope;
    public Object initialValue;
    public Integer index;
    public String code;
    public Boolean isMandatory;
    public Boolean isVisible;
    public Double size;
    public String title;
    public String tooltip;
    public Integer width;
    public Integer height;
    public Integer titleWidth;
    public Boolean isTitleVisible;
    public String icon;
    public Integer column;
    public long referencedFieldOuid;
    private static final String DOS_NDS_BASE = "::/DOS_SYSTEM_DIRECTORY";
    private static final String NDS_KEY = "CODESELECTION";
    private static final String NDS_BASE = "::/DOS_SYSTEM_DIRECTORY/CODESELECTION";
}
