// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataTransferRule.java

package dyna.framework.service.dts;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.NDS;
import dyna.util.Utils;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package dyna.framework.service.dts:
//            AbstractDatasource, DTSFactory

public class DataTransferRule
{

    public DataTransferRule()
    {
        name = null;
        description = null;
        createdTime = null;
        modifiedTime = null;
        inputSource = null;
        outputSource = null;
        fieldMap = null;
    }

    public void add(NDS nds1)
    {
    }

    public void get(NDS nds1)
    {
    }

    public void set(NDS nds1)
    {
    }

    public void remove(NDS nds1)
    {
    }

    public void transfer()
    {
        inputSource.transfer();
    }

    public static boolean addRule(NDS nds, HashMap rule)
        throws IIPRequestException
    {
        if(rule == null || rule.isEmpty())
            throw new IIPRequestException("Null arguments.");
        DataTransferRule dtr = null;
        String tempString = null;
        String tempString2 = null;
        String ouid = null;
        tempString = (String)rule.get("name");
        if(Utils.isNullString(tempString))
        {
            throw new IIPRequestException("Miss out mandatory parameter(s) : name");
        } else
        {
            Date now = new Date();
            dtr = new DataTransferRule();
            dtr.name = tempString;
            dtr.description = (String)rule.get("description");
            dtr.createdTime = sdf2.format(now);
            dtr.inputSource = DTSFactory.createDatasource((HashMap)rule.get("inputSource"));
            dtr.inputSource = DTSFactory.createDatasource((HashMap)rule.get("outputSource"));
            dtr.add(nds);
            dtr = null;
            return false;
        }
    }

    public static boolean removeRule(NDS nds, String name)
        throws IIPRequestException
    {
        return false;
    }

    public static HashMap getRule(NDS nds, String name)
        throws IIPRequestException
    {
        return null;
    }

    public static boolean setRule(NDS nds, HashMap rule)
        throws IIPRequestException
    {
        return false;
    }

    public static ArrayList listRule(NDS nds)
        throws IIPRequestException
    {
        return null;
    }

    public static boolean executeRule(NDS nds, String name)
        throws IIPRequestException
    {
        return false;
    }

    public String name;
    public String description;
    public String createdTime;
    public String modifiedTime;
    public AbstractDatasource inputSource;
    public AbstractDatasource outputSource;
    public HashMap fieldMap;
    public static final String NDS_BASE = "::/DTS_SYSTEM_DIRECTORY";
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
