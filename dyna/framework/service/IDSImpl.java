// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IDSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.server.*;
import java.util.ArrayList;

// Referenced classes of package dyna.framework.service:
//            IDS, DSS

public class IDSImpl extends ServiceServer
    implements IDS
{

    public IDSImpl()
    {
        dss = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dss = (DSS)getServiceInstance("DF30DSS1");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList getUpdateFileList()
        throws IIPRequestException
    {
        if(dss == null)
            return null;
        else
            return dss.listFile("/ids");
    }

    private static final String DSS_PATH = "/ids";
    private DSS dss;
}
