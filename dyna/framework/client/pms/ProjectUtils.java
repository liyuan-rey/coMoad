// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectUtils.java

package dyna.framework.client.pms;

import dyna.framework.client.DynaMOAD;
import dyna.framework.client.UIGeneration;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.util.*;

public class ProjectUtils
{

    public ProjectUtils()
    {
    }

    private static void initialize()
    {
        try
        {
            DOSChangeable dosPackage = dos.getPackageWithName("Project");
            if(dosPackage == null)
                return;
            pPackageOuid = (String)dosPackage.get("ouid");
            DOSChangeable dosClass = dos.getClassWithName(pPackageOuid, "Project");
            if(dosClass == null)
                return;
            pClassOuid = (String)dosClass.get("ouid");
            dosClass = dos.getClassWithName(pPackageOuid, "ProjectTemplate");
            if(dosClass == null)
                return;
            ptClassOuid = (String)dosClass.get("ouid");
            pAssoList = UIGeneration.listAssociations(pClassOuid);
            ptAssoList = UIGeneration.listAssociations(ptClassOuid);
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList getColumnsOfProjectStateSearch(String ouid)
    {
        String ptClassOuid = ouid;
        String tmpOuid = null;
        String tmpNumber = null;
        ArrayList fields = new ArrayList();
        ArrayList list = null;
        ArrayList subList = null;
        ArrayList returnList = new ArrayList();
        ArrayList rowList = null;
        LinkedList stack = new LinkedList();
        HashMap checkDup = new HashMap();
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
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        tmpOuid = ptClassOuid;
        while(!Utils.isNullString(tmpOuid)) 
            try
            {
                list = dos.listLinkFrom(tmpOuid, fields);
                if(list == null)
                {
                    if(stack.isEmpty())
                        break;
                    tmpOuid = (String)stack.removeLast();
                    tmpNumber = (String)checkDup.get(tmpOuid);
                    rowList = new ArrayList();
                    rowList.add(tmpOuid);
                    rowList.add(tmpNumber);
                    returnList.add(rowList);
                    rowList = null;
                    continue;
                }
                if(returnList.size() > 0)
                {
                    rowList = new ArrayList();
                    rowList.add(tmpOuid);
                    rowList.add(tmpNumber);
                    if(returnList.contains(rowList))
                        returnList.remove(rowList);
                }
                for(int i = list.size() - 1; i > -1; i--)
                {
                    subList = (ArrayList)list.get(i);
                    tmpOuid = (String)subList.get(0);
                    tmpNumber = (String)subList.get(1);
                    if(checkDup.containsKey(tmpOuid))
                    {
                        tmpOuid = null;
                    } else
                    {
                        stack.add(tmpOuid);
                        checkDup.put(tmpOuid, tmpNumber);
                        tmpOuid = null;
                        tmpNumber = null;
                    }
                }

                if(stack.isEmpty())
                    break;
                tmpOuid = (String)stack.removeLast();
                tmpNumber = (String)checkDup.get(tmpOuid);
                rowList = new ArrayList();
                rowList.add(tmpOuid);
                rowList.add(tmpNumber);
                returnList.add(rowList);
                rowList = null;
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public static String makeSQLForProjectStateSearch(ArrayList columns, String projectId, String projectName, String statusId)
    {
        if(Utils.isNullArrayList(columns))
            return null;
        String returnSQL = null;
        StringBuffer selectClause = new StringBuffer();
        StringBuffer fromClause = new StringBuffer();
        StringBuffer whereClause = new StringBuffer();
        ArrayList tmpList = null;
        selectClause.append("select b.p_vf$identity, b.p_vf$ouid, decode(x.no,1,b.p_md$number,null), decode(x.no,1,max(b.p_md$desc),null),");
        fromClause.append(" from (select");
        for(int i = 0; i < columns.size(); i++)
        {
            tmpList = (ArrayList)columns.get(i);
            selectClause.append(" to_char(to_date(max(decode(x.no, '1', decode(a.a" + (i + 1) + ", b.t1_md$number, b.t1_finishplandate, b.t2_md$number, b.t2_finishplandate, b.t3_md$number, b.t3_finishplandate, null), decode(a.a" + (i + 1) + ", b.t1_md$number, b.t1_finisheddate, b.t2_md$number, b.t2_finisheddate, b.t3_md$number, b.t3_finisheddate, null))), 'YYYYMMDDHH24MISS'), 'YYYY-MM-DD') a" + (i + 1));
            fromClause.append(" '" + (String)tmpList.get(1) + "' a" + (i + 1));
            if(i < columns.size() - 1)
            {
                selectClause.append(",");
                fromClause.append(",");
            }
        }

        fromClause.append(" from dual) A,");
        fromClause.append(" (select p.vf$identity p_vf$identity, p.vf$ouid p_vf$ouid, p.md$number p_md$number, p.md$desc p_md$desc,");
        fromClause.append(" t1.vf$identity t1_vf$identity, t1.vf$ouid t1_vf$ouid, t1.md$number t1_md$number, t1.md$desc t1_md$desc, t1.finishplandate t1_finishplandate, t1.finisheddate t1_finisheddate, t1.startplandate t1_startplandate, t1.starteddate t1_starteddate,");
        fromClause.append(" t2.vf$identity t2_vf$identity, t2.vf$ouid t2_vf$ouid, t2.md$number t2_md$number, t2.md$desc t2_md$desc, t2.finishplandate t2_finishplandate, t2.finisheddate t2_finisheddate, t2.startplandate t2_startplandate, t2.starteddate t2_starteddate,");
        fromClause.append(" t3.vf$identity t3_vf$identity, t3.vf$ouid t3_vf$ouid, t3.md$number t3_md$number, t3.md$desc t3_md$desc, t3.finishplandate t3_finishplandate, t3.finisheddate t3_finisheddate, t3.startplandate t3_startplandate, t3.starteddate t3_starteddate");
        fromClause.append(" from");
        fromClause.append(" (select vf$identity, max(vf$ouid) vf$ouid, max(md$number) md$number, max(md$desc) md$desc from project$vf where md$number like '");
        if(Utils.isNullString(projectId))
        {
            fromClause.append("%'");
        } else
        {
            fromClause.append(projectId.trim());
            fromClause.append("%'");
        }
        if(!Utils.isNullString(projectName))
        {
            fromClause.append(" and md$desc like '%");
            fromClause.append(projectName);
            fromClause.append("%'");
        }
        if(!Utils.isNullString(statusId))
        {
            fromClause.append(" and md$status='");
            fromClause.append(statusId);
            fromClause.append("'");
        }
        fromClause.append(" group by vf$identity) p,");
        fromClause.append(" taskofproject$as top, task$vf t1, taskoftask$as tot1, task$vf t2, taskoftask$as tot2, task$vf t3");
        fromClause.append(" where t3.vf$ouid(+) = tot2.as$end2 and t2.vf$ouid(+) = tot1.as$end2 and t1.vf$ouid(+) = top.as$end2 and tot2.as$end1(+) = tot1.as$end2 and tot1.as$end1(+) = top.as$end2 and top.as$end1 = p.vf$ouid) B,");
        fromClause.append(" (select '1' no from dual union all select '2' no from dual) X");
        whereClause.append(" group by b.p_vf$identity, b.p_vf$ouid, b.p_md$number, x.no");
        returnSQL = selectClause.append(fromClause.append(whereClause)).toString();
        return returnSQL;
    }

    public static ArrayList projectStateSearch(String sql)
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
