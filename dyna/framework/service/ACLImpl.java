// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ACLImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.DTMUtil;
import dyna.util.Utils;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service:
//            ACL, DTM, DOS, AUS, 
//            WFM

public class ACLImpl extends ServiceServer
    implements ACL
{

    public ACLImpl()
    {
        dtm = null;
        aus = null;
        dos = null;
        wfm = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dtm = (DTM)getServiceInstance("DF30DTM1");
            dos = (DOS)getServiceInstance("DF30DOS1");
            aus = (AUS)getServiceInstance("DF30AUS1");
            wfm = (WFM)getServiceInstance("DF30WFM1");
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public boolean createACL(DOSChangeable aclDefinition)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap valueMap;
        xc = null;
        con = null;
        stat = null;
        if(aclDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + aclDefinition);
        valueMap = aclDefinition.getValueMap();
        if(valueMap == null || Utils.isNullString((String)valueMap.get("targetclassouid")) || Utils.isNullString((String)valueMap.get("permission")) || Utils.isNullString((String)valueMap.get("permissionscope")) || Utils.isNullString((String)valueMap.get("userobject")))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        SQLException e;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into acl (tgtclsouid, tgtobjouid, permission, perscp, usrobj, pertyp, grantable, des) values (?, ?, ?, ?, ?, ?, ?, ?)");
            stat.setLong(1, Utils.convertOuidToLong((String)valueMap.get("targetclassouid")));
            if(Utils.isNullString((String)valueMap.get("targetobjectouid")))
                stat.setString(2, "0");
            else
                stat.setString(2, (String)valueMap.get("targetobjectouid"));
            stat.setString(3, (String)valueMap.get("permission"));
            stat.setString(4, (String)valueMap.get("permissionscope"));
            stat.setString(5, (String)valueMap.get("userobject"));
            stat.setString(6, (String)valueMap.get("permissiontype"));
            stat.setString(7, (String)valueMap.get("isgrantable"));
            stat.setString(8, (String)valueMap.get("description"));
            stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return true;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return false;
    }

    public void removeACL(String targetClassOuid, String targetObjectOuid, String permission, String permissionScope, String userObject)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(permission))
            throw new IIPRequestException("Parameter value is null : permission");
        if(Utils.isNullString(permissionScope))
            throw new IIPRequestException("Parameter value is null : permissionScope");
        if(Utils.isNullString(userObject))
            throw new IIPRequestException("Parameter value is null : userObject");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete acl where tgtclsouid = ? and  tgtobjouid = ? and  permission = ? and  perscp = ? and  usrobj = ?");
            stat.setLong(1, Utils.convertOuidToLong(targetClassOuid));
            if(Utils.isNullString(targetObjectOuid))
                stat.setString(2, "0");
            else
                stat.setString(2, targetObjectOuid);
            stat.setString(3, permission);
            stat.setString(4, permissionScope);
            stat.setString(5, userObject);
            stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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

    public DOSChangeable getACL(String targetClassOuid, String targetObjectOuid, String permission, String permissionScope, String userObject)
        throws IIPRequestException
    {
        DOSChangeable returnObject;
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        returnObject = null;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(permission))
            throw new IIPRequestException("Parameter value is null : permission");
        if(Utils.isNullString(permissionScope))
            throw new IIPRequestException("Parameter value is null : permissionScope");
        if(Utils.isNullString(userObject))
            throw new IIPRequestException("Parameter value is null : userObject");
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select tgtclsouid, tgtobjouic, permission, perscp, usrobj, pertyp, grantable, des from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = ? and usrobj = ?");
            stat.setLong(1, Utils.convertOuidToLong(targetClassOuid));
            if(Utils.isNullString(targetObjectOuid))
                stat.setString(2, "0");
            else
                stat.setString(2, targetObjectOuid);
            stat.setString(3, permission);
            stat.setString(4, permissionScope);
            stat.setString(5, userObject);
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next(); returnObject.put("description", rs.getString(8)))
            {
                returnObject = new DOSChangeable();
                returnObject.put("targetclassouid", Long.toHexString(rs.getLong(1)));
                returnObject.put("targetobjectouid", rs.getString(2));
                returnObject.put("permission", rs.getString(3));
                returnObject.put("permissionscope", rs.getString(4));
                returnObject.put("userobject", rs.getString(5));
                returnObject.put("permissiontype", rs.getString(6));
                returnObject.put("isgrantable", rs.getString(7));
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            doschangeable = returnObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setACL(DOSChangeable aclDefinition)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        if(aclDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + aclDefinition);
        HashMap valueMap = aclDefinition.getValueMap();
        if(valueMap == null || Utils.isNullString((String)valueMap.get("targetclassouid")) || Utils.isNullString((String)valueMap.get("permission")) || Utils.isNullString((String)valueMap.get("permissionscope")) || Utils.isNullString((String)valueMap.get("userobject")))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update acl set pertyp = ?, grantable = ?, des = ? where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = ? and usrobj = ?");
            stat.setString(1, (String)valueMap.get("permissiontype"));
            stat.setString(2, (String)valueMap.get("isgrantable"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong((String)valueMap.get("targetclassouid")));
            if(Utils.isNullString((String)valueMap.get("targetobjectouid")))
                stat.setString(5, "0");
            else
                stat.setString(5, (String)valueMap.get("targetobjectouid"));
            stat.setString(6, (String)valueMap.get("permission"));
            stat.setString(7, (String)valueMap.get("permissionscope"));
            stat.setString(8, (String)valueMap.get("userobject"));
            stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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

    public ArrayList retrieveACL(String modelOuid, HashMap conditions)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String whereclause;
        DOSChangeable returnObject = null;
        ArrayList returnList = null;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        whereclause = "";
        if(conditions.get("description") != null)
        {
            if(conditions.get("description").equals("P"))
            {
                if(Utils.isNullString(whereclause))
                    whereclause = " where a.des = '" + (String)conditions.get("permission") + "' ";
                else
                    whereclause = whereclause + " and a.des = '" + (String)conditions.get("permission") + "' ";
            } else
            if(Utils.isNullString(whereclause))
                whereclause = " where a.des is null ";
            else
                whereclause = whereclause + " and a.des is null ";
        } else
        if(Utils.isNullString(whereclause))
            whereclause = " where a.des is null ";
        else
            whereclause = whereclause + " and a.des is null ";
        if(conditions.get("targetclassouid") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.tgtclsouid = to_number('" + Utils.convertOuidToLong((String)conditions.get("targetclassouid")) + "') ";
            else
                whereclause = whereclause + " and a.tgtclsouid = to_number('" + Utils.convertOuidToLong((String)conditions.get("targetclassouid")) + "') ";
        if(conditions.get("targetobjectouid") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.tgtobjouid = '" + (String)conditions.get("targetobjectouid") + "' ";
            else
                whereclause = whereclause + " and a.tgtobjouid = '" + (String)conditions.get("targetobjectouid") + "' ";
        if(conditions.get("permission") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.permission = '" + (String)conditions.get("permission") + "' ";
            else
                whereclause = whereclause + " and a.permission = '" + (String)conditions.get("permission") + "' ";
        if(conditions.get("permissionscope") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.perscp = '" + (String)conditions.get("permissionscope") + "' ";
            else
                whereclause = whereclause + " and a.perscp = '" + (String)conditions.get("permissionscope") + "' ";
        if(conditions.get("groupname") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.usrobj like '%" + (String)conditions.get("groupname") + "%' ";
            else
                whereclause = whereclause + " and a.usrobj like '%" + (String)conditions.get("groupname") + "%' ";
        if(conditions.get("rolename") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.usrobj like '%" + (String)conditions.get("rolename") + "%' ";
            else
                whereclause = whereclause + " and a.usrobj like '%" + (String)conditions.get("rolename") + "%' ";
        conditions.get("username");
        if(conditions.get("userid") != null)
            if(Utils.isNullString(whereclause))
                whereclause = " where a.usrobj like '%" + (String)conditions.get("userid") + "%' ";
            else
                whereclause = whereclause + " and a.usrobj like '%" + (String)conditions.get("userid") + "%' ";
        if(conditions.get("permissiontype") != null)
        {
            String pertyp = ((String)conditions.get("permissiontype")).equals("ALLOW") ? "A" : "D";
            if(Utils.isNullString(whereclause))
                whereclause = " where a.pertyp = '" + pertyp + "' ";
            else
                whereclause = whereclause + " and a.pertyp = '" + pertyp + "' ";
        }
        if(conditions.get("isgrantable") != null)
        {
            String grantable = ((Boolean)conditions.get("isgrantable")).booleanValue() ? "T" : "F";
            if(Utils.isNullString(whereclause))
                whereclause = " where a.grantable = '" + grantable + "' ";
            else
                whereclause = whereclause + " and a.grantable = '" + grantable + "' ";
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select a.tgtclsouid, a.tgtobjouid, a.permission, a.perscp, a.usrobj, a.pertyp, a.grantable, a.des from acl a, dosclas dc, dosmodpkg dmp, dosmod dm " + whereclause + "and a.tgtclsouid = dc.ouid " + "and dc.dospkg = dmp.dospkg " + "and dmp.dosmod = dm.ouid " + "and dm.ouid = ? " + "order by a.tgtclsouid, a.tgtobjouid, a.perscp, a.usrobj ");
            stat.setLong(1, Utils.convertOuidToLong(modelOuid));
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable returnObject = new DOSChangeable();
                returnObject.put("targetclassouid", Long.toHexString(rs.getLong(1)));
                returnObject.put("targetobjectouid", rs.getString(2));
                returnObject.put("permission", rs.getString(3));
                returnObject.put("permissionscope", rs.getString(4));
                returnObject.put("userobject", rs.getString(5));
                returnObject.put("permissiontype", rs.getString(6));
                returnObject.put("isgrantable", Utils.getBoolean(rs, 7));
                returnObject.put("description", rs.getString(8));
                returnList.add(returnObject);
                returnObject = null;
            }
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

    public ArrayList retrievePrivateACL4Grant(String targetClassOuid, String targetObjectOuid, String userObject, Boolean isadmin)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSChangeable returnObject = null;
        ArrayList returnList = null;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(userObject))
            throw new IIPRequestException("Parameter value is null : userObject");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long targetClassOuidLong = Utils.convertOuidToLong(targetClassOuid);
            if(isadmin.booleanValue())
            {
                stat = con.prepareStatement("select tgtclsouid, tgtobjouid, permission, perscp, usrobj, pertyp, grantable, des from acl where tgtclsouid = ? and tgtobjouid = ? order by tgtclsouid, tgtobjouid, perscp, usrobj ");
                stat.setLong(1, targetClassOuidLong);
                if(Utils.isNullString(targetObjectOuid))
                    stat.setString(2, "0");
                else
                    stat.setString(2, targetObjectOuid);
            } else
            {
                ArrayList listGroup = aus.listGroupOfUser(userObject);
                ArrayList listRole = aus.listRoleOfUser(userObject);
                DOSChangeable objectData = null;
                if(!Utils.isNullString(targetObjectOuid) && !targetObjectOuid.equals("0"))
                    objectData = dos.get(targetObjectOuid);
                StringBuffer listGroupSb = new StringBuffer();
                StringBuffer listRoleSb = new StringBuffer();
                String ownerStr = null;
                if(listGroup != null)
                {
                    for(int i = 0; i < listGroup.size(); i++)
                    {
                        if(i > 0)
                            listGroupSb.append(", ");
                        listGroupSb.append("'" + (String)listGroup.get(i) + "'");
                    }

                }
                if(listRole != null)
                {
                    for(int i = 0; i < listRole.size(); i++)
                    {
                        if(i > 0)
                            listRoleSb.append(", ");
                        listRoleSb.append("'" + (String)listRole.get(i) + "'");
                    }

                }
                if(objectData != null)
                {
                    ownerStr = (String)objectData.get("md$user");
                    ownerStr = ownerStr.substring(ownerStr.indexOf('(') + 1, ownerStr.length() - 1);
                }
                if(Utils.isNullString(targetObjectOuid))
                    targetObjectOuid = "0";
                StringBuffer sb1 = new StringBuffer();
                sb1.append("select permission from acl where tgtclsouid = ");
                sb1.append(targetClassOuidLong);
                sb1.append(" and tgtobjouid = '");
                sb1.append(targetObjectOuid);
                sb1.append("' and perscp = 'OTHERS' and grantable = 'T' minus ");
                sb1.append("select permission from acl where tgtclsouid = ");
                sb1.append(targetClassOuidLong);
                sb1.append(" and tgtobjouid = '");
                sb1.append(targetObjectOuid);
                sb1.append("' and perscp = 'OTHERS' and grantable = 'F' ");
                if(listGroupSb.length() > 0)
                {
                    sb1.append("union all ");
                    sb1.append("select permission from acl where tgtclsouid = ");
                    sb1.append(targetClassOuidLong);
                    sb1.append(" and tgtobjouid = '");
                    sb1.append(targetObjectOuid);
                    sb1.append("' and perscp = 'GROUP' and usrobj in (");
                    sb1.append(listGroupSb);
                    sb1.append(") and grantable = 'T' minus ");
                    sb1.append("select permission from acl where tgtclsouid = ");
                    sb1.append(targetClassOuidLong);
                    sb1.append(" and tgtobjouid = '");
                    sb1.append(targetObjectOuid);
                    sb1.append("' and perscp = 'GROUP' and usrobj in (");
                    sb1.append(listGroupSb);
                    sb1.append(") and grantable = 'F' ");
                }
                if(listRoleSb.length() > 0)
                {
                    sb1.append("union all ");
                    sb1.append("select permission from acl where tgtclsouid = ");
                    sb1.append(targetClassOuidLong);
                    sb1.append(" and tgtobjouid = '");
                    sb1.append(targetObjectOuid);
                    sb1.append("' and perscp = 'ROLE' and usrobj in (");
                    sb1.append(listRoleSb);
                    sb1.append(") and grantable = 'T' minus ");
                    sb1.append("select permission from acl where tgtclsouid = ");
                    sb1.append(targetClassOuidLong);
                    sb1.append(" and tgtobjouid = '");
                    sb1.append(targetObjectOuid);
                    sb1.append("' and perscp = 'ROLE' and usrobj in (");
                    sb1.append(listRoleSb);
                    sb1.append(") and grantable = 'F' ");
                }
                sb1.append("union all ");
                sb1.append("select permission from acl where tgtclsouid = ");
                sb1.append(targetClassOuidLong);
                sb1.append(" and tgtobjouid = '");
                sb1.append(targetObjectOuid);
                sb1.append("' and perscp = 'USER' and usrobj = '");
                sb1.append(userObject);
                sb1.append("' and grantable = 'T' minus ");
                sb1.append("select permission from acl where tgtclsouid = ");
                sb1.append(targetClassOuidLong);
                sb1.append(" and tgtobjouid = '");
                sb1.append(targetObjectOuid);
                sb1.append("' and perscp = 'USER' and usrobj = '");
                sb1.append(userObject);
                sb1.append("' and grantable = 'F' ");
                if(ownerStr != null && ownerStr.equals(userObject))
                {
                    sb1.append("union all ");
                    sb1.append("select permission from acl where tgtclsouid = ");
                    sb1.append(targetClassOuidLong);
                    sb1.append(" and tgtobjouid = '");
                    sb1.append(targetObjectOuid);
                    sb1.append("' and perscp = 'OWNER' and usrobj = 'Owner' and grantable = 'T' minus ");
                    sb1.append("select permission from acl where tgtclsouid = ");
                    sb1.append(targetClassOuidLong);
                    sb1.append(" and tgtobjouid = '");
                    sb1.append(targetObjectOuid);
                    sb1.append("' and perscp = 'OWNER' and usrobj = 'Owner' and grantable = 'F' ");
                }
                StringBuffer sb = new StringBuffer();
                sb.append("select tgtclsouid, tgtobjouid, permission, perscp, usrobj, pertyp, grantable, des ");
                sb.append("from acl ");
                sb.append("where permission in (");
                sb.append(sb1);
                sb.append(") and tgtclsouid = ");
                sb.append(targetClassOuidLong);
                sb.append(" and tgtobjouid = '");
                sb.append(targetObjectOuid);
                sb.append("' order by tgtclsouid, tgtobjouid, perscp, usrobj ");
                stat = con.prepareStatement(sb.toString());
            }
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable returnObject = new DOSChangeable();
                returnObject.put("targetclassouid", Long.toHexString(rs.getLong(1)));
                returnObject.put("targetobjectouid", rs.getString(2));
                returnObject.put("permission", rs.getString(3));
                returnObject.put("permissionscope", rs.getString(4));
                returnObject.put("userobject", rs.getString(5));
                returnObject.put("permissiontype", rs.getString(6));
                returnObject.put("isgrantable", rs.getString(7));
                returnObject.put("description", rs.getString(8));
                returnObject.put("inherited", "F");
                returnList.add(returnObject);
                returnObject = null;
            }
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

    public ArrayList retrieveACL4Grant(String targetClassOuid, String targetObjectOuid, String userObject, Boolean isadmin)
        throws IIPRequestException
    {
        ArrayList returnList = null;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(userObject))
            throw new IIPRequestException("Parameter value is null : userObject");
        returnList = new ArrayList();
        ArrayList tempList1 = null;
        ArrayList tempList2 = null;
        Iterator iKey1 = null;
        Iterator iKey2 = null;
        DOSChangeable obj1 = null;
        DOSChangeable obj2 = null;
        boolean isNew = false;
        ArrayList superClass = dos.listAllSuperClassOuid(targetClassOuid);
        if(superClass != null)
        {
            for(int i = superClass.size() - 1; i >= 0; i--)
            {
                tempList1 = retrievePrivateACL4Grant((String)superClass.get(i), "0", userObject, isadmin);
                tempList2 = (ArrayList)returnList.clone();
                if(!Utils.isNullArrayList(tempList1))
                    for(iKey1 = tempList1.iterator(); iKey1.hasNext();)
                    {
                        isNew = true;
                        obj1 = (DOSChangeable)iKey1.next();
                        if(!Utils.isNullArrayList(tempList2))
                            for(iKey2 = tempList2.iterator(); iKey2.hasNext();)
                            {
                                obj2 = (DOSChangeable)iKey2.next();
                                if(obj1.get("permission").equals(obj2.get("permission")) && obj1.get("permissionscope").equals(obj2.get("permissionscope")) && obj1.get("userobject").equals(obj2.get("userobject")))
                                {
                                    isNew = false;
                                    break;
                                }
                            }

                        if(!isNew)
                            returnList.remove(returnList.indexOf(obj2));
                        if(Utils.isNullString(targetObjectOuid) || targetObjectOuid.equals("0") || !obj1.get("permission").equals("CREATE") && !obj1.get("permission").equals("SCAN"))
                        {
                            obj1.put("inherited", "T");
                            returnList.add(obj1);
                        }
                    }

            }

        }
        tempList1 = retrievePrivateACL4Grant(targetClassOuid, "0", userObject, isadmin);
        tempList2 = (ArrayList)returnList.clone();
        if(!Utils.isNullArrayList(tempList1))
            for(iKey1 = tempList1.iterator(); iKey1.hasNext();)
            {
                isNew = true;
                obj1 = (DOSChangeable)iKey1.next();
                if(!Utils.isNullArrayList(tempList2))
                    for(iKey2 = tempList2.iterator(); iKey2.hasNext();)
                    {
                        obj2 = (DOSChangeable)iKey2.next();
                        if(obj1.get("permission").equals(obj2.get("permission")) && obj1.get("permissionscope").equals(obj2.get("permissionscope")) && obj1.get("userobject").equals(obj2.get("userobject")))
                        {
                            isNew = false;
                            break;
                        }
                    }

                if(!isNew)
                    returnList.remove(returnList.indexOf(obj2));
                if(!Utils.isNullString(targetObjectOuid) && !targetObjectOuid.equals("0"))
                {
                    if(!obj1.get("permission").equals("CREATE") && !obj1.get("permission").equals("SCAN"))
                    {
                        obj1.put("inherited", "T");
                        returnList.add(obj1);
                    }
                } else
                {
                    obj1.put("inherited", "F");
                    returnList.add(obj1);
                }
            }

        if(!Utils.isNullString(targetObjectOuid) && !targetObjectOuid.equals("0"))
        {
            tempList1 = retrievePrivateACL4Grant(targetClassOuid, targetObjectOuid, userObject, isadmin);
            tempList2 = (ArrayList)returnList.clone();
            if(!Utils.isNullArrayList(tempList1))
                for(iKey1 = tempList1.iterator(); iKey1.hasNext(); returnList.add(obj1))
                {
                    isNew = true;
                    obj1 = (DOSChangeable)iKey1.next();
                    if(!Utils.isNullArrayList(tempList2))
                        for(iKey2 = tempList2.iterator(); iKey2.hasNext();)
                        {
                            obj2 = (DOSChangeable)iKey2.next();
                            if(obj1.get("permission").equals(obj2.get("permission")) && obj1.get("permissionscope").equals(obj2.get("permissionscope")) && obj1.get("userobject").equals(obj2.get("userobject")))
                            {
                                isNew = false;
                                break;
                            }
                        }

                    if(!isNew)
                        returnList.remove(returnList.indexOf(obj2));
                    obj1.put("inherited", "F");
                }

        }
        return returnList;
    }

    public HashSet getAllowedUserSet(HashSet beforeAllowSet, String targetClassOuid, String targetObjectOuid, String objectOuid, String permission, String userID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(permission))
            throw new IIPRequestException("Parameter value is null : permission");
        if(Utils.isNullString(userID))
            throw new IIPRequestException("Parameter value is null : userID");
        SQLException e;
        HashSet hashset;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'OTHERS' and des is null union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'GROUP' and des is null union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'ROLE' and des is null union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'OWNER' and des is null union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'USER' and des is null");
            for(int i = 1; i < 16; i++)
            {
                stat.setLong(i, Utils.convertOuidToLong(targetClassOuid));
                i++;
                if(Utils.isNullString(targetObjectOuid))
                    stat.setString(i, "0");
                else
                    stat.setString(i, targetObjectOuid);
                i++;
                stat.setString(i, permission);
            }

            ResultSet rs = stat.executeQuery();
            HashSet allowSet = new HashSet();
            HashSet denySet = new HashSet();
            String tmpPermissionScope = null;
            String oldPermissionScope = null;
            String tmpUserObject = null;
            String tmpPermissionType = null;
            if(beforeAllowSet != null && beforeAllowSet.size() > 0)
                allowSet.addAll(beforeAllowSet);
            while(rs.next()) 
            {
                oldPermissionScope = tmpPermissionScope;
                tmpPermissionScope = rs.getString(1);
                tmpUserObject = rs.getString(2);
                tmpPermissionType = rs.getString(3);
                if(tmpPermissionScope.equals("OTHERS"))
                {
                    if(tmpPermissionType.equals("A"))
                        allowSet.add(userID);
                } else
                if(tmpPermissionScope.equals("GROUP"))
                {
                    ArrayList userList = aus.listMembersOfGroup(tmpUserObject);
                    for(int i = 0; i < userList.size(); i++)
                        if(tmpPermissionType.equals("A"))
                            allowSet.add((String)userList.get(i));
                        else
                            denySet.add((String)userList.get(i));

                } else
                if(tmpPermissionScope.equals("ROLE"))
                {
                    if(oldPermissionScope != null && !oldPermissionScope.equals(tmpPermissionScope))
                    {
                        allowSet.removeAll(denySet);
                        denySet.clear();
                    }
                    ArrayList userList = aus.listUsersOfRole(tmpUserObject);
                    for(int i = 0; i < userList.size(); i++)
                        if(tmpPermissionType.equals("A"))
                            allowSet.add((String)userList.get(i));
                        else
                            denySet.add((String)userList.get(i));

                } else
                if(tmpPermissionScope.equals("OWNER"))
                {
                    if(oldPermissionScope != null && !oldPermissionScope.equals(tmpPermissionScope))
                    {
                        allowSet.removeAll(denySet);
                        denySet.clear();
                    }
                    if(!Utils.isNullString(objectOuid) && !objectOuid.equals("0"))
                    {
                        DOSChangeable dosObj = dos.get(objectOuid);
                        tmpUserObject = (String)dosObj.get("md$user");
                        tmpUserObject = tmpUserObject.substring(tmpUserObject.indexOf('(') + 1, tmpUserObject.indexOf(')'));
                        if(tmpPermissionType.equals("A"))
                            allowSet.add(tmpUserObject);
                        else
                            denySet.add(tmpUserObject);
                    }
                } else
                if(tmpPermissionScope.equals("USER"))
                {
                    if(oldPermissionScope != null && !oldPermissionScope.equals(tmpPermissionScope))
                    {
                        allowSet.removeAll(denySet);
                        denySet.clear();
                    }
                    if(tmpPermissionType.equals("A"))
                        allowSet.add(tmpUserObject);
                    else
                        denySet.add(tmpUserObject);
                }
            }
            allowSet.removeAll(denySet);
            denySet.clear();
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            hashset = allowSet;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return hashset;
        e;
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public boolean hasPermission(String targetClassOuid, String targetObjectOuid, String permission, String userID)
        throws IIPRequestException
    {
        boolean result = false;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(permission))
            throw new IIPRequestException("Parameter value is null : permission");
        if(Utils.isNullString(userID))
            throw new IIPRequestException("Parameter value is null : userID");
        HashSet resultAllowSet = null;
        HashSet inputAllowSet = new HashSet();
        ArrayList superClass = dos.listAllSuperClassOuid(targetClassOuid);
        if(superClass != null)
        {
            for(int i = superClass.size() - 1; i >= 0; i--)
            {
                resultAllowSet = getAllowedUserSet(inputAllowSet, (String)superClass.get(i), "0", targetObjectOuid, permission, userID);
                inputAllowSet.clear();
                inputAllowSet.addAll(resultAllowSet);
            }

        }
        resultAllowSet = getAllowedUserSet(inputAllowSet, targetClassOuid, "0", targetObjectOuid, permission, userID);
        inputAllowSet.clear();
        inputAllowSet.addAll(resultAllowSet);
        if(!Utils.isNullString(targetObjectOuid) && !targetObjectOuid.equals("0"))
        {
            resultAllowSet = getAllowedUserSet(inputAllowSet, targetClassOuid, targetObjectOuid, targetObjectOuid, permission, userID);
            inputAllowSet.clear();
            inputAllowSet.addAll(resultAllowSet);
        }
        if(resultAllowSet.contains(userID))
            result = true;
        return result;
    }

    public HashSet getAllowedUserSet4Process(HashSet beforeAllowSet, String targetClassOuid, String targetObjectOuid, String targetProcessOuid, String permission, String userID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(targetObjectOuid))
            throw new IIPRequestException("Parameter value is null : targetObjectOuid");
        if(Utils.isNullString(targetProcessOuid))
            throw new IIPRequestException("Parameter value is null : targetProcessOuid");
        if(Utils.isNullString(permission))
            throw new IIPRequestException("Parameter value is null : permission");
        if(Utils.isNullString(userID))
            throw new IIPRequestException("Parameter value is null : userID");
        SQLException e;
        HashSet hashset;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'OTHERS' and des = 'P' union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'GROUP' and des = 'P' union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'ROLE' and des = 'P' union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'OWNER' and des = 'P' union all select perscp, usrobj, pertyp from acl where tgtclsouid = ? and tgtobjouid = ? and permission = ? and perscp = 'USER' and des = 'P'");
            for(int i = 1; i < 16; i++)
            {
                stat.setLong(i, Utils.convertOuidToLong(targetClassOuid));
                i++;
                stat.setString(i, targetProcessOuid);
                i++;
                stat.setString(i, permission);
            }

            ResultSet rs = stat.executeQuery();
            HashSet allowSet = new HashSet();
            HashSet denySet = new HashSet();
            String tmpPermissionScope = null;
            String oldPermissionScope = null;
            String tmpUserObject = null;
            String tmpPermissionType = null;
            if(beforeAllowSet != null && beforeAllowSet.size() > 0)
                allowSet.addAll(beforeAllowSet);
            while(rs.next()) 
            {
                oldPermissionScope = tmpPermissionScope;
                tmpPermissionScope = rs.getString(1);
                tmpUserObject = rs.getString(2);
                tmpPermissionType = rs.getString(3);
                if(tmpPermissionScope.equals("OTHERS"))
                {
                    if(tmpPermissionType.equals("A"))
                        allowSet.add(userID);
                } else
                if(tmpPermissionScope.equals("GROUP"))
                {
                    ArrayList userList = aus.listMembersOfGroup(tmpUserObject);
                    for(int i = 0; i < userList.size(); i++)
                        if(tmpPermissionType.equals("A"))
                            allowSet.add((String)userList.get(i));
                        else
                            denySet.add((String)userList.get(i));

                } else
                if(tmpPermissionScope.equals("ROLE"))
                {
                    if(oldPermissionScope != null && !oldPermissionScope.equals(tmpPermissionScope))
                    {
                        allowSet.removeAll(denySet);
                        denySet.clear();
                    }
                    ArrayList userList = aus.listUsersOfRole(tmpUserObject);
                    for(int i = 0; i < userList.size(); i++)
                        if(tmpPermissionType.equals("A"))
                            allowSet.add((String)userList.get(i));
                        else
                            denySet.add((String)userList.get(i));

                } else
                if(tmpPermissionScope.equals("OWNER"))
                {
                    if(oldPermissionScope != null && !oldPermissionScope.equals(tmpPermissionScope))
                    {
                        allowSet.removeAll(denySet);
                        denySet.clear();
                    }
                    DOSChangeable dosObj = dos.get(targetObjectOuid);
                    tmpUserObject = (String)dosObj.get("md$user");
                    tmpUserObject = tmpUserObject.substring(tmpUserObject.indexOf('(') + 1, tmpUserObject.indexOf(')'));
                    if(tmpPermissionType.equals("A"))
                        allowSet.add(tmpUserObject);
                    else
                        denySet.add(tmpUserObject);
                } else
                if(tmpPermissionScope.equals("USER"))
                {
                    if(oldPermissionScope != null && !oldPermissionScope.equals(tmpPermissionScope))
                    {
                        allowSet.removeAll(denySet);
                        denySet.clear();
                    }
                    if(tmpPermissionType.equals("A"))
                        allowSet.add(tmpUserObject);
                    else
                        denySet.add(tmpUserObject);
                }
            }
            allowSet.removeAll(denySet);
            denySet.clear();
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            hashset = allowSet;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return hashset;
        e;
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public boolean hasPermission4Process(String targetClassOuid, String targetObjectOuid, String targetProcessOuid, String permission, String userID)
        throws IIPRequestException
    {
        boolean result = false;
        if(Utils.isNullString(targetClassOuid))
            throw new IIPRequestException("Parameter value is null : targetClassOuid");
        if(Utils.isNullString(targetObjectOuid))
            throw new IIPRequestException("Parameter value is null : targetObjectOuid");
        if(Utils.isNullString(targetProcessOuid))
            throw new IIPRequestException("Parameter value is null : targetProcessOuid");
        if(Utils.isNullString(permission))
            throw new IIPRequestException("Parameter value is null : permission");
        if(Utils.isNullString(userID))
            throw new IIPRequestException("Parameter value is null : userID");
        HashSet resultAllowSet = null;
        HashSet inputAllowSet = new HashSet();
        ArrayList superClass = dos.listAllSuperClassOuid(targetClassOuid);
        if(superClass != null)
        {
            for(int i = superClass.size() - 1; i >= 0; i--)
            {
                resultAllowSet = getAllowedUserSet4Process(inputAllowSet, (String)superClass.get(i), targetObjectOuid, targetProcessOuid, permission, userID);
                inputAllowSet.clear();
                inputAllowSet.addAll(resultAllowSet);
            }

        }
        resultAllowSet = getAllowedUserSet4Process(inputAllowSet, targetClassOuid, targetObjectOuid, targetProcessOuid, permission, userID);
        inputAllowSet.clear();
        inputAllowSet.addAll(resultAllowSet);
        if(resultAllowSet.contains(userID))
            result = true;
        return result;
    }

    private DTM dtm;
    private AUS aus;
    private DOS dos;
    private WFM wfm;
}
