// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSAssociation.java

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
//            DOSObject, DOSChangeable, DOSClass, DOSField

public class DOSAssociation extends DOSObject
{

    public DOSAssociation()
    {
        type = null;
        _class = null;
        datasourceId = null;
        code = null;
        end1_class = null;
        end1_name = null;
        end1_multiplicity_from = null;
        end1_multiplicity_to = null;
        end1_isNavigable = null;
        end1_isOrdered = null;
        end1_orderKeyField = null;
        end1_changeable = null;
        end1_cascadeRelease = null;
        end1_cascadeClone = null;
        end2_class = null;
        end2_name = null;
        end2_multiplicity_from = null;
        end2_multiplicity_to = null;
        end2_isNavigable = null;
        end2_isOrdered = null;
        end2_orderKeyField = null;
        end2_changeable = null;
        end2_cascadeRelease = null;
        end2_cascadeClone = null;
    }

    public static String createAssociation(DOSChangeable associationDefinition, DTM dtm, HashMap classMap, HashMap fieldMap, HashMap associationMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        HashMap checkDup = null;
        DOSClass dosClass = null;
        String returnString = null;
        long returnLong = 0L;
        String tempString = null;
        long tempLong = 0L;
        int rows = 0;
        int i = 0;
        if(associationDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + associationDefinition);
        valueMap = associationDefinition.getValueMap();
        if(valueMap == null || valueMap.get("name") == null || valueMap.get("type") == null || valueMap.get("code") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        dosClass = (DOSClass)classMap.get((String)valueMap.get("ouid@class"));
        if(dosClass != null && Utils.getBoolean(dosClass.versionable))
            throw new IIPRequestException("Versionable class can not to use Association class.");
        dosClass = null;
        if(!Utils.isNullString((String)valueMap.get("ouid@class")))
            i++;
        if(!Utils.isNullString((String)valueMap.get("end1.ouid@class")))
            i++;
        if(!Utils.isNullString((String)valueMap.get("end2.ouid@class")))
            i++;
        if(i == 0)
            throw new IIPRequestException("Least one link needed.");
        i = 0;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long returnLong = DOSObject.generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dosasso (ouid,name,des,type,dosclas,clas$1,name$1,mltpfrom$1,mltpto$1,navi$1,ordr$1,chng$1,rls$1,clon$1,clas$2,name$2,mltpfrom$2,mltpto$2,navi$2,ordr$2,chng$2,rls$2,clon$2,dsid,code ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            Utils.setByte(stat, 4, (Byte)valueMap.get("type"));
            String tempString = (String)valueMap.get("ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(5, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(5, 2);
            tempString = (String)valueMap.get("end1.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(6, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(6, 2);
            stat.setString(7, (String)valueMap.get("end1.name"));
            Utils.setInt(stat, 8, (Integer)valueMap.get("end1.multiplicity.from"));
            Utils.setInt(stat, 9, (Integer)valueMap.get("end1.multiplicity.to"));
            Utils.setBoolean(stat, 10, (Boolean)valueMap.get("end1.is.navigable"));
            Utils.setBoolean(stat, 11, (Boolean)valueMap.get("end1.is.ordered"));
            Utils.setByte(stat, 12, (Byte)valueMap.get("end1.changeable"));
            Utils.setBoolean(stat, 13, (Boolean)valueMap.get("end1.cascade.release"));
            Utils.setBoolean(stat, 14, (Boolean)valueMap.get("end1.cascade.clone"));
            tempString = (String)valueMap.get("end2.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(15, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(15, 2);
            stat.setString(16, (String)valueMap.get("end2.name"));
            Utils.setInt(stat, 17, (Integer)valueMap.get("end2.multiplicity.from"));
            Utils.setInt(stat, 18, (Integer)valueMap.get("end2.multiplicity.to"));
            Utils.setBoolean(stat, 19, (Boolean)valueMap.get("end2.is.navigable"));
            Utils.setBoolean(stat, 20, (Boolean)valueMap.get("end2.is.ordered"));
            Utils.setByte(stat, 21, (Byte)valueMap.get("end2.changeable"));
            Utils.setBoolean(stat, 22, (Boolean)valueMap.get("end2.cascade.release"));
            Utils.setBoolean(stat, 23, (Boolean)valueMap.get("end2.cascade.clone"));
            stat.setString(24, (String)valueMap.get("datasource.id"));
            stat.setString(25, (String)valueMap.get("code"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            DOSAssociation dosAssociation = new DOSAssociation();
            dosAssociation.OUID = returnLong;
            dosAssociation.name = (String)valueMap.get("name");
            dosAssociation.description = (String)valueMap.get("description");
            dosAssociation.type = (Byte)valueMap.get("type");
            dosAssociation._class = (DOSClass)classMap.get((String)valueMap.get("ouid@class"));
            dosAssociation.datasourceId = (String)valueMap.get("datasource.id");
            dosAssociation.code = (String)valueMap.get("code");
            dosAssociation.end1_class = (DOSClass)classMap.get((String)valueMap.get("end1.ouid@class"));
            dosAssociation.end1_name = (String)valueMap.get("end1.name");
            dosAssociation.end1_multiplicity_from = (Integer)valueMap.get("end1.multiplicity.from");
            dosAssociation.end1_multiplicity_to = (Integer)valueMap.get("end1.multiplicity.to");
            dosAssociation.end1_isNavigable = (Boolean)valueMap.get("end1.is.navigable");
            dosAssociation.end1_isOrdered = (Boolean)valueMap.get("end1.is.ordered");
            dosAssociation.end1_changeable = (Byte)valueMap.get("end1.changeable");
            dosAssociation.end1_cascadeRelease = (Boolean)valueMap.get("end1.cascade.release");
            dosAssociation.end1_cascadeClone = (Boolean)valueMap.get("end1.cascade.clone");
            dosAssociation.end2_class = (DOSClass)classMap.get((String)valueMap.get("end2.ouid@class"));
            dosAssociation.end2_name = (String)valueMap.get("end2.name");
            dosAssociation.end2_multiplicity_from = (Integer)valueMap.get("end2.multiplicity.from");
            dosAssociation.end2_multiplicity_to = (Integer)valueMap.get("end2.multiplicity.to");
            dosAssociation.end2_isNavigable = (Boolean)valueMap.get("end2.is.navigable");
            dosAssociation.end2_isOrdered = (Boolean)valueMap.get("end2.is.ordered");
            dosAssociation.end2_changeable = (Byte)valueMap.get("end2.changeable");
            dosAssociation.end2_cascadeRelease = (Boolean)valueMap.get("end2.cascade.release");
            dosAssociation.end2_cascadeClone = (Boolean)valueMap.get("end2.cascade.clone");
            associationMap.put(returnString, dosAssociation);
            if(dosAssociation._class != null)
                dosAssociation._class.isAssociationClass = Utils.True;
            ArrayList arrayList = null;
            LinkedList tempList = null;
            Iterator listKey = null;
            int i = 0;
            arrayList = (ArrayList)valueMap.get("end1.array$ouid@field");
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosassokey (dosasso, end, indx, dosfld ) values (?, ?, ?, ? ) ");
                i = 0;
                tempList = new LinkedList();
                for(listKey = arrayList.iterator(); listKey.hasNext(); tempList.add(fieldMap.get(tempString)))
                {
                    i++;
                    tempString = (String)listKey.next();
                    stat.setLong(1, returnLong);
                    stat.setInt(2, 1);
                    stat.setInt(3, i);
                    stat.setLong(4, Utils.convertOuidToLong(tempString));
                    stat.executeUpdate();
                }

                stat.close();
                stat = null;
                dosAssociation.end1_orderKeyField = tempList;
                listKey = null;
                arrayList = null;
                tempList = null;
            }
            arrayList = (ArrayList)valueMap.get("end2.array$ouid@field");
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosassokey (dosasso, end, indx, dosfld ) values (?, ?, ?, ? ) ");
                i = 0;
                tempList = new LinkedList();
                for(listKey = arrayList.iterator(); listKey.hasNext(); tempList.add(fieldMap.get(tempString)))
                {
                    i++;
                    tempString = (String)listKey.next();
                    stat.setLong(1, returnLong);
                    stat.setInt(2, 2);
                    stat.setInt(3, i);
                    stat.setLong(4, Utils.convertOuidToLong(tempString));
                    stat.executeUpdate();
                }

                stat.close();
                stat = null;
                dosAssociation.end2_orderKeyField = tempList;
                listKey = null;
                arrayList = null;
                tempList = null;
            }
            con.commit();
            con.close();
            xc.close();
            DOSClass dosClass = dosAssociation._class;
            if(dosClass != null)
            {
                tempList = dosClass.end0List;
                if(tempList == null)
                {
                    tempList = new LinkedList();
                    dosClass.end0List = tempList;
                }
                HashMap checkDup = new HashMap();
                for(listKey = tempList.iterator(); listKey.hasNext(); checkDup.put(Long.toHexString(((DOSAssociation)listKey.next()).OUID), "1"));
                if(checkDup.get(Long.toHexString(dosAssociation.OUID)) == null)
                    tempList.add(dosAssociation);
                checkDup.clear();
                checkDup = null;
                tempList = null;
                dosClass = null;
                listKey = null;
            }
            dosClass = dosAssociation.end1_class;
            if(dosClass != null)
            {
                tempList = dosClass.end1List;
                if(tempList == null)
                {
                    tempList = new LinkedList();
                    dosClass.end1List = tempList;
                }
                HashMap checkDup = new HashMap();
                for(listKey = tempList.iterator(); listKey.hasNext(); checkDup.put(Long.toHexString(((DOSAssociation)listKey.next()).OUID), "1"));
                if(checkDup.get(Long.toHexString(dosAssociation.OUID)) == null)
                    tempList.add(dosAssociation);
                checkDup.clear();
                checkDup = null;
                tempList = null;
                dosClass = null;
                listKey = null;
            }
            dosClass = dosAssociation.end2_class;
            if(dosClass != null)
            {
                tempList = dosClass.end2List;
                if(tempList == null)
                {
                    tempList = new LinkedList();
                    dosClass.end2List = tempList;
                }
                HashMap checkDup = new HashMap();
                for(listKey = tempList.iterator(); listKey.hasNext(); checkDup.put(Long.toHexString(((DOSAssociation)listKey.next()).OUID), "1"));
                if(checkDup.get(Long.toHexString(dosAssociation.OUID)) == null)
                    tempList.add(dosAssociation);
                checkDup.clear();
                checkDup = null;
                tempList = null;
                dosClass = null;
                listKey = null;
            }
            valueMap = null;
            dosAssociation = null;
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

    public static void removeAssociation(String ouid, DTM dtm, HashMap associationMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        DOSAssociation dosAssociation = null;
        DOSClass dosClass = null;
        LinkedList tempList = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from dosassokey where dosasso=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosasso where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            dosAssociation = (DOSAssociation)associationMap.remove(ouid);
            dosClass = dosAssociation._class;
            if(dosClass != null)
            {
                tempList = dosClass.end0List;
                if(tempList != null && tempList.size() > 0)
                    tempList.remove(dosAssociation);
                tempList = null;
                dosClass = null;
            }
            dosClass = dosAssociation.end1_class;
            if(dosClass != null)
            {
                tempList = dosClass.end1List;
                if(tempList != null && tempList.size() > 0)
                    tempList.remove(dosAssociation);
                tempList = null;
                dosClass = null;
            }
            dosClass = dosAssociation.end2_class;
            if(dosClass != null)
            {
                tempList = dosClass.end2List;
                if(tempList != null && tempList.size() > 0)
                    tempList.remove(dosAssociation);
                tempList = null;
                dosClass = null;
            }
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

    public static DOSChangeable getAssociation(String ouid, HashMap associationMap)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSAssociation dosAssociation = (DOSAssociation)associationMap.get(ouid);
        ArrayList arrayList = null;
        LinkedList linkedList = null;
        Iterator listKey = null;
        long tempLong = 0L;
        if(dosAssociation != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosAssociation.name);
            returnObject.put("description", dosAssociation.description);
            returnObject.put("type", dosAssociation.type);
            if(dosAssociation._class != null)
                returnObject.put("ouid@class", Long.toHexString(dosAssociation._class.OUID));
            returnObject.put("datasource.id", dosAssociation.datasourceId);
            returnObject.put("code", dosAssociation.code);
            if(dosAssociation.end1_class != null)
                returnObject.put("end1.ouid@class", Long.toHexString(dosAssociation.end1_class.OUID));
            returnObject.put("end1.name", dosAssociation.end1_name);
            returnObject.put("end1.multiplicity.from", dosAssociation.end1_multiplicity_from);
            returnObject.put("end1.multiplicity.to", dosAssociation.end1_multiplicity_to);
            returnObject.put("end1.is.navigable", dosAssociation.end1_isNavigable);
            returnObject.put("end1.is.ordered", dosAssociation.end1_isOrdered);
            linkedList = dosAssociation.end1_orderKeyField;
            if(linkedList != null && linkedList.size() > 0)
            {
                arrayList = new ArrayList(linkedList.size());
                for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                returnObject.put("end1.array$ouid@field", arrayList);
                arrayList = null;
                linkedList = null;
            }
            returnObject.put("end1.changeable", dosAssociation.end1_changeable);
            returnObject.put("end1.cascade.release", dosAssociation.end1_cascadeRelease);
            returnObject.put("end1.cascade.clone", dosAssociation.end1_cascadeClone);
            if(dosAssociation.end2_class != null)
                returnObject.put("end2.ouid@class", Long.toHexString(dosAssociation.end2_class.OUID));
            returnObject.put("end2.name", dosAssociation.end2_name);
            returnObject.put("end2.multiplicity.from", dosAssociation.end2_multiplicity_from);
            returnObject.put("end2.multiplicity.to", dosAssociation.end2_multiplicity_to);
            returnObject.put("end2.is.navigable", dosAssociation.end2_isNavigable);
            returnObject.put("end2.is.ordered", dosAssociation.end2_isOrdered);
            linkedList = dosAssociation.end2_orderKeyField;
            if(linkedList != null && linkedList.size() > 0)
            {
                arrayList = new ArrayList(linkedList.size());
                for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                returnObject.put("end2.array$ouid@field", arrayList);
                arrayList = null;
                linkedList = null;
            }
            returnObject.put("end2.changeable", dosAssociation.end2_changeable);
            returnObject.put("end2.cascade.release", dosAssociation.end2_cascadeRelease);
            returnObject.put("end2.cascade.clone", dosAssociation.end2_cascadeClone);
            dosAssociation = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public static void setAssociation(DOSChangeable associationDefinition, DTM dtm, HashMap classMap, HashMap fieldMap, HashMap associationMap)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        DOSClass dosClass = null;
        java.util.TreeMap treeMap = null;
        if(associationDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + associationDefinition);
        HashMap valueMap = associationDefinition.getValueMap();
        String tempString = null;
        ArrayList arrayList = null;
        LinkedList tempList = null;
        Iterator listKey = null;
        int i = 0;
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null || valueMap.get("type") == null || valueMap.get("code") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        dosClass = (DOSClass)classMap.get((String)valueMap.get("ouid@class"));
        if(dosClass != null && Utils.getBoolean(dosClass.versionable))
            throw new IIPRequestException("Versionable class can not to use Association class.");
        if(!Utils.isNullString((String)valueMap.get("ouid@class")))
            i++;
        if(!Utils.isNullString((String)valueMap.get("end1.ouid@class")))
            i++;
        if(!Utils.isNullString((String)valueMap.get("end2.ouid@class")))
            i++;
        if(i == 0)
            throw new IIPRequestException("Least one link needed.");
        i = 0;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dosasso set name=?, des=?, type=?, dosclas=?, clas$1=?, name$1=?, mltpfrom$1=?, mltpto$1=?, navi$1=?, ordr$1=?, chng$1=?, clas$2=?, name$2=?, mltpfrom$2=?, mltpto$2=?, navi$2=?, ordr$2=?, chng$2=?, dsid=?, code=?, rls$1=?, clon$1=?, rls$2=?, clon$2=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            Utils.setByte(stat, 3, (Byte)valueMap.get("type"));
            tempString = (String)valueMap.get("ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(4, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(4, 2);
            tempString = (String)valueMap.get("end1.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(5, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(5, 2);
            stat.setString(6, (String)valueMap.get("end1.name"));
            Utils.setInt(stat, 7, (Integer)valueMap.get("end1.multiplicity.from"));
            Utils.setInt(stat, 8, (Integer)valueMap.get("end1.multiplicity.to"));
            Utils.setBoolean(stat, 9, (Boolean)valueMap.get("end1.is.navigable"));
            Utils.setBoolean(stat, 10, (Boolean)valueMap.get("end1.is.ordered"));
            Utils.setByte(stat, 11, (Byte)valueMap.get("end1.changeable"));
            tempString = (String)valueMap.get("end2.ouid@class");
            if(!Utils.isNullString(tempString))
                stat.setLong(12, Utils.convertOuidToLong(tempString));
            else
                stat.setNull(12, 2);
            stat.setString(13, (String)valueMap.get("end2.name"));
            Utils.setInt(stat, 14, (Integer)valueMap.get("end2.multiplicity.from"));
            Utils.setInt(stat, 15, (Integer)valueMap.get("end2.multiplicity.to"));
            Utils.setBoolean(stat, 16, (Boolean)valueMap.get("end2.is.navigable"));
            Utils.setBoolean(stat, 17, (Boolean)valueMap.get("end2.is.ordered"));
            Utils.setByte(stat, 18, (Byte)valueMap.get("end2.changeable"));
            stat.setString(19, (String)valueMap.get("datasource.id"));
            stat.setString(20, (String)valueMap.get("code"));
            Utils.setBoolean(stat, 21, (Boolean)valueMap.get("end1.cascade.release"));
            Utils.setBoolean(stat, 22, (Boolean)valueMap.get("end1.cascade.clone"));
            Utils.setBoolean(stat, 23, (Boolean)valueMap.get("end2.cascade.release"));
            Utils.setBoolean(stat, 24, (Boolean)valueMap.get("end2.cascade.clone"));
            stat.setLong(25, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            DOSAssociation dosAssociation = (DOSAssociation)associationMap.get(ouid);
            if(dosAssociation == null)
            {
                dosAssociation = new DOSAssociation();
                dosAssociation.OUID = ouidLong;
                associationMap.put(ouid, dosAssociation);
            }
            String nameString = dosAssociation.name;
            DOSClass oldClass = dosAssociation._class;
            dosAssociation.name = (String)valueMap.get("name");
            dosAssociation.description = (String)valueMap.get("description");
            dosAssociation.type = (Byte)valueMap.get("type");
            dosAssociation._class = (DOSClass)classMap.get((String)valueMap.get("ouid@class"));
            dosAssociation.datasourceId = (String)valueMap.get("datasource.id");
            dosAssociation.code = (String)valueMap.get("code");
            dosAssociation.end1_class = (DOSClass)classMap.get((String)valueMap.get("end1.ouid@class"));
            dosAssociation.end1_name = (String)valueMap.get("end1.name");
            dosAssociation.end1_multiplicity_from = (Integer)valueMap.get("end1.multiplicity.from");
            dosAssociation.end1_multiplicity_to = (Integer)valueMap.get("end1.multiplicity.to");
            dosAssociation.end1_isNavigable = (Boolean)valueMap.get("end1.is.navigable");
            dosAssociation.end1_isOrdered = (Boolean)valueMap.get("end1.is.ordered");
            dosAssociation.end1_changeable = (Byte)valueMap.get("end1.changeable");
            dosAssociation.end1_cascadeRelease = (Boolean)valueMap.get("end1.cascade.release");
            dosAssociation.end1_cascadeClone = (Boolean)valueMap.get("end1.cascade.clone");
            dosAssociation.end2_class = (DOSClass)classMap.get((String)valueMap.get("end2.ouid@class"));
            dosAssociation.end2_name = (String)valueMap.get("end2.name");
            dosAssociation.end2_multiplicity_from = (Integer)valueMap.get("end2.multiplicity.from");
            dosAssociation.end2_multiplicity_to = (Integer)valueMap.get("end2.multiplicity.to");
            dosAssociation.end2_isNavigable = (Boolean)valueMap.get("end2.is.navigable");
            dosAssociation.end2_isOrdered = (Boolean)valueMap.get("end2.is.ordered");
            dosAssociation.end2_changeable = (Byte)valueMap.get("end2.changeable");
            dosAssociation.end2_cascadeRelease = (Boolean)valueMap.get("end2.cascade.release");
            dosAssociation.end2_cascadeClone = (Boolean)valueMap.get("end2.cascade.clone");
            if(dosAssociation._class != null)
                dosAssociation._class.isAssociationClass = Utils.True;
            else
            if(oldClass != null)
            {
                oldClass.isAssociationClass = Utils.False;
                oldClass = null;
            }
            stat = con.prepareStatement("delete from dosassokey where dosasso=? ");
            stat.setLong(1, ouidLong);
            stat.executeUpdate();
            stat.close();
            stat = null;
            arrayList = (ArrayList)valueMap.get("end1.array$ouid@field");
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosassokey (dosasso, end, indx, dosfld ) values (?, ?, ?, ? ) ");
                i = 0;
                tempList = new LinkedList();
                for(listKey = arrayList.iterator(); listKey.hasNext(); tempList.add(fieldMap.get(tempString)))
                {
                    i++;
                    tempString = (String)listKey.next();
                    stat.setLong(1, ouidLong);
                    stat.setInt(2, 1);
                    stat.setInt(3, i);
                    stat.setLong(4, Utils.convertOuidToLong(tempString));
                    stat.executeUpdate();
                }

                stat.close();
                stat = null;
                dosAssociation.end1_orderKeyField = tempList;
                listKey = null;
                arrayList = null;
                tempList = null;
            }
            arrayList = (ArrayList)valueMap.get("end2.array$ouid@field");
            if(arrayList != null && arrayList.size() > 0)
            {
                stat = con.prepareStatement("insert into dosassokey (dosasso, end, indx, dosfld ) values (?, ?, ?, ? ) ");
                i = 0;
                tempList = new LinkedList();
                for(listKey = arrayList.iterator(); listKey.hasNext(); tempList.add(fieldMap.get(tempString)))
                {
                    i++;
                    tempString = (String)listKey.next();
                    stat.setLong(1, ouidLong);
                    stat.setInt(2, 2);
                    stat.setInt(3, i);
                    stat.setLong(4, Utils.convertOuidToLong(tempString));
                    stat.executeUpdate();
                }

                stat.close();
                stat = null;
                dosAssociation.end2_orderKeyField = tempList;
                listKey = null;
                arrayList = null;
                tempList = null;
            }
            con.commit();
            con.close();
            xc.close();
            treeMap = null;
            dosClass = null;
            dosAssociation = null;
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

    public static ArrayList listAssociation(DTM dtm, HashMap classMap, HashMap fieldMap, HashMap associationMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSAssociation dosAssociation = null;
        ArrayList returnList = null;
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        LinkedList linkedList = null;
        LinkedList linkedList2 = null;
        Iterator listKey = null;
        DOSChangeable associationData = null;
        String tempString = null;
        long tempLong = 0L;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        if(associationMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = associationMap.keySet().iterator(); mapKey.hasNext();)
            {
                associationData = new DOSChangeable();
                dosAssociation = (DOSAssociation)associationMap.get(mapKey.next());
                associationData.put("ouid", Long.toHexString(dosAssociation.OUID));
                associationData.put("name", dosAssociation.name);
                associationData.put("description", dosAssociation.description);
                associationData.put("type", dosAssociation.type);
                if(dosAssociation._class != null)
                    associationData.put("ouid@class", Long.toHexString(dosAssociation._class.OUID));
                associationData.put("datasource.id", dosAssociation.datasourceId);
                associationData.put("code", dosAssociation.code);
                if(dosAssociation.end1_class != null)
                    associationData.put("end1.ouid@class", Long.toHexString(dosAssociation.end1_class.OUID));
                associationData.put("end1.name", dosAssociation.end1_name);
                associationData.put("end1.multiplicity.from", dosAssociation.end1_multiplicity_from);
                associationData.put("end1.multiplicity.to", dosAssociation.end1_multiplicity_to);
                associationData.put("end1.is.navigable", dosAssociation.end1_isNavigable);
                associationData.put("end1.is.ordered", dosAssociation.end1_isOrdered);
                linkedList = dosAssociation.end1_orderKeyField;
                if(linkedList != null && linkedList.size() > 0)
                {
                    arrayList = new ArrayList(linkedList.size());
                    for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                    associationData.put("end1.array$ouid@field", arrayList);
                    arrayList = null;
                    linkedList = null;
                }
                associationData.put("end1.changeable", dosAssociation.end1_changeable);
                associationData.put("end1.cascade.release", dosAssociation.end1_cascadeRelease);
                associationData.put("end1.cascade.clone", dosAssociation.end1_cascadeClone);
                if(dosAssociation.end2_class != null)
                    associationData.put("end2.ouid@class", Long.toHexString(dosAssociation.end2_class.OUID));
                associationData.put("end2.name", dosAssociation.end2_name);
                associationData.put("end2.multiplicity.from", dosAssociation.end2_multiplicity_from);
                associationData.put("end2.multiplicity.to", dosAssociation.end2_multiplicity_to);
                associationData.put("end2.is.navigable", dosAssociation.end2_isNavigable);
                associationData.put("end2.is.ordered", dosAssociation.end2_isOrdered);
                linkedList = dosAssociation.end2_orderKeyField;
                if(linkedList != null && linkedList.size() > 0)
                {
                    arrayList = new ArrayList(linkedList.size());
                    for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                    associationData.put("end2.array$ouid@field", arrayList);
                    arrayList = null;
                    linkedList = null;
                }
                associationData.put("end2.changeable", dosAssociation.end2_changeable);
                associationData.put("end2.cascade.release", dosAssociation.end2_cascadeRelease);
                associationData.put("end2.cascade.clone", dosAssociation.end2_cascadeClone);
                returnList.add(associationData);
                associationData = null;
                dosAssociation = null;
            }

            mapKey = null;
            associationData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select da.ouid, da.name, da.des, da.dosclas, da.type, da.clas$1, da.name$1, da.mltpfrom$1, da.mltpto$1, da.navi$1, da.ordr$1, da.chng$1, da.clas$2, da.name$2, da.mltpfrom$2, da.mltpto$2, da.navi$2, da.ordr$2, da.chng$2, da.dsid, da.code, da.rls$1, da.clon$1, da.rls$2, da.clon$2 from dosasso da ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            DOSChangeable associationData;
            while(rs.next()) 
            {
                associationData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                associationData.put("ouid", ouid);
                associationData.put("name", rs.getString(2));
                associationData.put("description", rs.getString(3));
                associationData.put("ouid@class", Long.toHexString(rs.getLong(4)));
                associationData.put("type", Utils.getByte(rs, 5));
                long tempLong = rs.getLong(6);
                if(tempLong > 0L)
                    associationData.put("end1.ouid@class", Long.toHexString(tempLong));
                associationData.put("end1.name", rs.getString(7));
                associationData.put("end1.multiplicity.from", Utils.getInteger(rs, 8));
                associationData.put("end1.multiplicity.to", Utils.getInteger(rs, 9));
                associationData.put("end1.is.navigable", Utils.getBoolean(rs, 10));
                associationData.put("end1.is.ordered", Utils.getBoolean(rs, 11));
                associationData.put("end1.changeable", Utils.getByte(rs, 12));
                tempLong = rs.getLong(13);
                if(tempLong > 0L)
                    associationData.put("end2.ouid@class", Long.toHexString(tempLong));
                associationData.put("end2.name", rs.getString(14));
                associationData.put("end2.multiplicity.from", Utils.getInteger(rs, 15));
                associationData.put("end2.multiplicity.to", Utils.getInteger(rs, 16));
                associationData.put("end2.is.navigable", Utils.getBoolean(rs, 17));
                associationData.put("end2.is.ordered", Utils.getBoolean(rs, 18));
                associationData.put("end2.changeable", Utils.getByte(rs, 19));
                associationData.put("datasource.id", rs.getString(20));
                associationData.put("code", rs.getString(21));
                associationData.put("end1.cascade.release", Utils.getBoolean(rs, 22));
                associationData.put("end1.cascade.clone", Utils.getBoolean(rs, 23));
                associationData.put("end2.cascade.release", Utils.getBoolean(rs, 24));
                associationData.put("end2.cascade.clone", Utils.getBoolean(rs, 25));
                returnList.add(associationData);
                DOSAssociation dosAssociation = new DOSAssociation();
                dosAssociation.OUID = ouidLong;
                dosAssociation.name = (String)associationData.get("name");
                dosAssociation.description = (String)associationData.get("description");
                dosAssociation.type = (Byte)associationData.get("type");
                dosAssociation._class = (DOSClass)classMap.get((String)associationData.get("ouid@class"));
                dosAssociation.datasourceId = (String)associationData.get("datasource.id");
                dosAssociation.code = (String)associationData.get("code");
                dosAssociation.end1_class = (DOSClass)classMap.get((String)associationData.get("end1.ouid@class"));
                dosAssociation.end1_name = (String)associationData.get("end1.name");
                dosAssociation.end1_multiplicity_from = (Integer)associationData.get("end1.multiplicity.from");
                dosAssociation.end1_multiplicity_to = (Integer)associationData.get("end1.multiplicity.to");
                dosAssociation.end1_isNavigable = (Boolean)associationData.get("end1.is.navigable");
                dosAssociation.end1_isOrdered = (Boolean)associationData.get("end1.is.ordered");
                dosAssociation.end1_changeable = (Byte)associationData.get("end1.changeable");
                dosAssociation.end1_cascadeRelease = (Boolean)associationData.get("end1.cascade.release");
                dosAssociation.end1_cascadeClone = (Boolean)associationData.get("end1.cascade.clone");
                dosAssociation.end2_class = (DOSClass)classMap.get((String)associationData.get("end2.ouid@class"));
                dosAssociation.end2_name = (String)associationData.get("end2.name");
                dosAssociation.end2_multiplicity_from = (Integer)associationData.get("end2.multiplicity.from");
                dosAssociation.end2_multiplicity_to = (Integer)associationData.get("end2.multiplicity.to");
                dosAssociation.end2_isNavigable = (Boolean)associationData.get("end2.is.navigable");
                dosAssociation.end2_isOrdered = (Boolean)associationData.get("end2.is.ordered");
                dosAssociation.end2_changeable = (Byte)associationData.get("end2.changeable");
                dosAssociation.end2_cascadeRelease = (Boolean)associationData.get("end2.cascade.release");
                dosAssociation.end2_cascadeClone = (Boolean)associationData.get("end2.cascade.clone");
                associationMap.put(ouid, dosAssociation);
                dosAssociation = null;
                associationData = null;
            }
            associationData = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select dak.end, dak.dosfld from dosassokey dak where dak.dosasso=? order by dak.end,dak.indx ");
            int end = 0;
            Iterator listKey;
            for(listKey = returnList.iterator(); listKey.hasNext();)
            {
                associationData = (DOSChangeable)listKey.next();
                DOSAssociation dosAssociation = (DOSAssociation)associationMap.get((String)associationData.get("ouid"));
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                LinkedList linkedList = new LinkedList();
                LinkedList linkedList2 = new LinkedList();
                stat.setLong(1, Utils.convertOuidToLong((String)associationData.get("ouid")));
                for(rs = stat.executeQuery(); rs.next();)
                {
                    end = rs.getInt(1);
                    long tempLong = rs.getLong(2);
                    if(end == 1)
                    {
                        String tempString = Long.toHexString(tempLong);
                        arrayList.add(tempString);
                        linkedList.add((DOSField)fieldMap.get(tempString));
                    } else
                    if(end == 2)
                    {
                        String tempString = Long.toHexString(tempLong);
                        arrayList2.add(tempString);
                        linkedList2.add((DOSField)fieldMap.get(tempString));
                    }
                }

                rs.close();
                rs = null;
                if(arrayList.size() > 0)
                {
                    associationData.put("end1.array$ouid@field", arrayList);
                    dosAssociation.end1_orderKeyField = linkedList;
                }
                if(arrayList2.size() > 0)
                {
                    associationData.put("end2.array$ouid@field", arrayList2);
                    dosAssociation.end2_orderKeyField = linkedList2;
                }
                arrayList = null;
                arrayList2 = null;
                linkedList = null;
                linkedList2 = null;
                associationData = null;
                dosAssociation = null;
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

    public Byte type;
    public DOSClass _class;
    public String datasourceId;
    public String code;
    public DOSClass end1_class;
    public String end1_name;
    public Integer end1_multiplicity_from;
    public Integer end1_multiplicity_to;
    public Boolean end1_isNavigable;
    public Boolean end1_isOrdered;
    public LinkedList end1_orderKeyField;
    public Byte end1_changeable;
    public Boolean end1_cascadeRelease;
    public Boolean end1_cascadeClone;
    public DOSClass end2_class;
    public String end2_name;
    public Integer end2_multiplicity_from;
    public Integer end2_multiplicity_to;
    public Boolean end2_isNavigable;
    public Boolean end2_isOrdered;
    public LinkedList end2_orderKeyField;
    public Byte end2_changeable;
    public Boolean end2_cascadeRelease;
    public Boolean end2_cascadeClone;
}
