// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceServer.java

package dyna.framework.iip.server;

import dyna.framework.iip.*;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.HashMap;

// Referenced classes of package dyna.framework.iip.server:
//            ServiceRecord, ServiceBroker, RequestDispatcher

public class ServiceServer
    implements Service
{

    public ServiceServer()
    {
        sb = null;
        sr = null;
        dispatcher = null;
        accessIdentifier = 0L;
        timeToken = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        System.out.print(serviceRecord.name + " : " + serviceRecord.description);
        sb = serviceBroker;
        sr = serviceRecord;
        this.accessIdentifier = accessIdentifier;
        sr.startTime = System.currentTimeMillis();
        sr.status = "initiated";
        sr.service = this;
        sb.addServiceRecord(sr);
        System.out.print(" [initiated]");
    }

    public void ping()
    {
    }

    public Service getServiceInstance(String serviceId)
        throws ServiceNotFoundException
    {
        if(dispatcher == null)
            dispatcher = sb.getRequestDispatcher(accessIdentifier);
        return dispatcher.getServiceInstance(serviceId);
    }

    public InetAddress getClientInetAddress()
    {
        return sb.getClientInetAdress(Thread.currentThread());
    }

    public HashMap getClientContext()
    {
        HashMap context = sb.getClientContext(Thread.currentThread());
        if(context == null)
        {
            context = sb.getServerContext("server");
            if(context == null)
            {
                context = new HashMap();
                context.put("locale", "en");
                context.put("userId", "SYSTEM.INTERNAL");
                sb.setServerContext("server", context);
            }
        }
        return context;
    }

    public HashMap getClientContext(InetAddress inetAddress)
    {
        HashMap context = sb.getClientContext(inetAddress);
        if(context == null)
        {
            context = sb.getServerContext("server");
            if(context == null)
            {
                context = new HashMap();
                context.put("locale", "en");
                context.put("userId", "SYSTEM.INTERNAL");
                sb.setServerContext("server", context);
            }
        }
        return context;
    }

    public boolean isConnectedAddress(InetAddress inetAddress)
    {
        return sb.isConnectedClient(inetAddress);
    }

    public String getBrokerId()
    {
        return sb.getBrokerId();
    }

    public String getTimeToken()
        throws IIPRequestException
    {
        if(timeToken == null)
            updateTimeToken();
        return timeToken;
    }

    protected void updateTimeToken()
    {
        timeToken = Long.toHexString(System.currentTimeMillis() + accessIdentifier);
    }

    private ServiceBroker sb;
    private ServiceRecord sr;
    private RequestDispatcher dispatcher;
    private long accessIdentifier;
    private String timeToken;
}
