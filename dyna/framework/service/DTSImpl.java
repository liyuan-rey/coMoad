// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DTSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dts.DataTransferRule;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service:
//            DTS, NDS

public class DTSImpl extends ServiceServer
    implements DTS
{

    public DTSImpl()
    {
        nds = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            nds = (NDS)getServiceInstance("DF30NDS1");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public boolean addRule(HashMap rule)
        throws IIPRequestException
    {
        return DataTransferRule.addRule(nds, rule);
    }

    public boolean removeRule(String name)
        throws IIPRequestException
    {
        return DataTransferRule.removeRule(nds, name);
    }

    public HashMap getRule(String name)
        throws IIPRequestException
    {
        return DataTransferRule.getRule(nds, name);
    }

    public boolean setRule(HashMap rule)
        throws IIPRequestException
    {
        return DataTransferRule.setRule(nds, rule);
    }

    public ArrayList listRule()
        throws IIPRequestException
    {
        return DataTransferRule.listRule(nds);
    }

    public ArrayList listLoadedRule()
        throws IIPRequestException
    {
        return null;
    }

    public boolean loadRule(String name)
        throws IIPRequestException
    {
        return false;
    }

    public boolean unloadRule(String name)
        throws IIPRequestException
    {
        return false;
    }

    public boolean executeRule(String name)
        throws IIPRequestException
    {
        return DataTransferRule.executeRule(nds, name);
    }

    public NDS nds;
    public final String NDS_BASE = "::/DTS_SYSTEM_DIRECTORY";
    public final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    public final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
