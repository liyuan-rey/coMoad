// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EAPUtils.java

package dyna.framework.client.eap;

import dyna.framework.client.DynaMOAD;
import dyna.framework.client.UIGeneration;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.util.*;

public class EAPUtils
{

    public EAPUtils()
    {
    }

    private static void initialize()
    {
        try
        {
            DOSChangeable dosPackage = dos.getPackageWithName("ChangeControl");
            if(dosPackage == null)
                return;
            pPackageOuid = (String)dosPackage.get("ouid");
            DOSChangeable dosClass = dos.getClassWithName(pPackageOuid, "ChangeOrder");
            if(dosClass == null)
                return;
            pClassOuid = (String)dosClass.get("ouid");
            dosClass = dos.getClassWithName(pPackageOuid, "ApplyScheduleTemplate");
            if(dosClass == null)
                return;
            ptClassOuid = (String)dosClass.get("ouid");
            pAssoList = UIGeneration.listAssociations(pClassOuid, Boolean.TRUE, "association");
            ptAssoList = UIGeneration.listAssociations(ptClassOuid, Boolean.TRUE, "association");
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList getColumnsOfEAPSearch(String ouid)
    {
        String ptClassOuid = ouid;
        String tmpOuid = null;
        String tmpNumber = null;
        ArrayList fields = new ArrayList();
        ArrayList list = null;
        ArrayList subList = null;
        ArrayList returnList = new ArrayList();
        ArrayList rowList = null;
        initialize();
        try
        {
            DOSChangeable dosAssociation = null;
            DOSChangeable dosClass = null;
            DOSChangeable dosField = null;
            ArrayList fieldList = null;
            String end2ClassOuid = null;
            Iterator assoKey = ptAssoList.iterator();
            if(assoKey.hasNext())
            {
                dosAssociation = (DOSChangeable)assoKey.next();
                end2ClassOuid = (String)dosAssociation.get("end2.ouid@class");
                dosClass = dos.getClass(end2ClassOuid);
                dosField = dos.getFieldWithName(end2ClassOuid, "md$number");
                fields.add(dosField.get("ouid"));
                dosField = dos.getFieldWithName(end2ClassOuid, "md$description");
                fields.add(dosField.get("ouid"));
            }
            assoKey = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        tmpOuid = ptClassOuid;
        try
        {
            HashMap filter = new HashMap();
            filter.put("list.mode", "association");
            list = dos.listLinkFrom(tmpOuid, fields, filter);
            if(list == null)
                return null;
            for(int i = 0; i < list.size(); i++)
            {
                rowList = new ArrayList();
                rowList.add(((ArrayList)list.get(i)).get(0));
                rowList.add(((ArrayList)list.get(i)).get(2));
                returnList.add(rowList);
                rowList = null;
            }

        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public static String makeSQLForEAPSearch(ArrayList columns, String eoNumber, String eoName, String statusId)
    {
        if(Utils.isNullArrayList(columns))
            return null;
        String returnSQL = null;
        StringBuffer selectClause = new StringBuffer();
        StringBuffer fromClause = new StringBuffer();
        StringBuffer whereClause = new StringBuffer();
        ArrayList tmpList = null;
        selectClause.append("select b.p_sf$ouid, decode(x.no,1,b.p_md$number,null), decode(x.no,1,max(b.p_md$desc),null),");
        fromClause.append(" from (select");
        for(int i = 0; i < columns.size(); i++)
        {
            tmpList = (ArrayList)columns.get(i);
            selectClause.append(" to_char(to_date(max(decode(x.no, '1', decode(a.a" + (i + 1) + ", b.t1_md$desc, b.t1_plandate, null), decode(a.a" + (i + 1) + ", b.t1_md$desc, b.t1_finishdate, null))), 'YYYYMMDDHH24MISS'), 'YYYY-MM-DD') a" + (i + 1));
            fromClause.append(" '" + (String)tmpList.get(1) + "' a" + (i + 1));
            if(i < columns.size() - 1)
            {
                selectClause.append(",");
                fromClause.append(",");
            }
        }

        fromClause.append(" from dual) A,");
        fromClause.append(" (select p.sf$ouid p_sf$ouid, p.md$number p_md$number, p.md$desc p_md$desc,");
        fromClause.append(" t1.sf$ouid t1_sf$ouid, t1.md$number t1_md$number, t1.md$desc t1_md$desc, t1.plandate t1_plandate, t1.finishdate t1_finishdate ");
        fromClause.append(" from");
        fromClause.append(" (select sf$ouid, md$number, md$desc from changeorder$sf where md$number like '");
        if(Utils.isNullString(eoNumber))
        {
            fromClause.append("%'");
        } else
        {
            fromClause.append(eoNumber.trim());
            fromClause.append("%'");
        }
        if(!Utils.isNullString(eoName))
        {
            fromClause.append(" and md$desc like '%");
            fromClause.append(eoName);
            fromClause.append("%'");
        }
        if(!Utils.isNullString(statusId))
        {
            fromClause.append(" and md$status='");
            fromClause.append(statusId);
            fromClause.append("'");
        }
        fromClause.append(") p,");
        fromClause.append(" asso_applyschedule$as top, changeapplyschedule$sf t1 ");
        fromClause.append(" where t1.sf$ouid(+) = top.as$end2 and top.as$end1 = p.sf$ouid) B,");
        fromClause.append(" (select '1' no from dual union all select '2' no from dual) X");
        whereClause.append(" group by b.p_sf$ouid, b.p_md$number, x.no");
        returnSQL = selectClause.append(fromClause.append(whereClause)).toString();
        return returnSQL;
    }

    public static ArrayList eapSearch(String sql)
    {
        DOS dos = DynaMOAD.dos;
        ArrayList returnList = new ArrayList();
        try
        {
            returnList = dos.executeQuery(sql, "default", "0");
            if(Utils.isNullArrayList(returnList))
                return null;
            else
                return returnList;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static DOS dos;
    private static String pPackageOuid;
    private static String pClassOuid;
    private static String ptClassOuid;
    private static ArrayList pAssoList;
    private static ArrayList ptAssoList;

    static 
    {
        dos = DynaMOAD.dos;
    }
}
