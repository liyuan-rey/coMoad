// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SMSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.server.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

// Referenced classes of package dyna.framework.service:
//            SMS

public class SMSImpl extends ServiceServer
    implements SMS
{

    public SMSImpl()
    {
        accessIdentifier = 0L;
        sb = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        sb = serviceBroker;
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void shutdown()
        throws IIPRequestException
    {
        if(sb == null)
            return;
        System.err.println();
        System.err.println("[SMS] Shutdown process initialized.");
        sb.shutdown();
        System.err.println();
        System.err.println("[SMS] Shutdown process finished. Thanks.");
        try
        {
            wait(10000L);
        }
        catch(Exception exception) { }
        System.exit(0);
    }

    public HashMap getAllConnectionInformation()
        throws IIPRequestException
    {
        return null;
    }

    public HashMap getConnectionInformationByUser(String userId)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList getServiceInformation()
        throws IIPRequestException
    {
        return null;
    }

    private long accessIdentifier;
    private ServiceBroker sb;
}
