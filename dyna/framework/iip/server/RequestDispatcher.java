// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RequestDispatcher.java

package dyna.framework.iip.server;

import dyna.framework.Server;
import dyna.framework.iip.*;
import dyna.util.Utils;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.util.HashMap;

// Referenced classes of package dyna.framework.iip.server:
//            ServiceBroker, ServiceRecord, Requester

public class RequestDispatcher
    implements InvocationHandler
{

    public RequestDispatcher()
    {
        server = null;
        sb = null;
        requester = null;
        accessIdentifier = 0L;
        interfaceCache = null;
        server = Server.server;
    }

    public synchronized void setAcessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        interfaceCache = new HashMap();
        sb = server.getServiceBroker(accessIdentifier);
        requester = server.getRequester(accessIdentifier);
        System.out.println(" [initiated]");
    }

    public Service getServiceInstance(String serviceId)
        throws ServiceNotFoundException
    {
        if(Utils.isNullString(serviceId))
            throw new ServiceNotFoundException(serviceId);
        ServiceRecord serviceRecord = sb.getServiceRecord(serviceId);
        if(serviceRecord == null)
            throw new ServiceNotFoundException(serviceId);
        interfaceCache.put(serviceRecord.serviceInterface, serviceRecord);
        Service service = null;
        if(serviceRecord.service == null)
        {
            Class proxyInterfaces[] = {
                serviceRecord.serviceInterface
            };
            service = (Service)Proxy.newProxyInstance(serviceRecord.serviceInterface.getClassLoader(), proxyInterfaces, this);
            proxyInterfaces = (Class[])null;
            serviceRecord.service = service;
        } else
        {
            service = serviceRecord.service;
        }
        serviceRecord = null;
        return service;
    }

    public Object invoke(Object proxy, Method method, Object args[])
        throws Throwable
    {
        Class interfaceClass = method.getDeclaringClass();
        ServiceRecord serviceRecord = (ServiceRecord)interfaceCache.get(interfaceClass);
        if(serviceRecord == null)
            throw new ServiceNotFoundException(interfaceClass.getName());
        interfaceClass = null;
        Object returnedObject = null;
        System.out.println("[invoke] " + method);
        if(sb.isLocalService(serviceRecord))
            returnedObject = method.invoke(serviceRecord.service, args);
        else
            returnedObject = requester.request(serviceRecord.serviceId, method.getName(), ((Object) (args)));
        if(returnedObject instanceof Exception)
        {
            throw new IIPRequestException(method.getName() + " at " + serviceRecord.serviceId + "\n" + returnedObject.toString());
        } else
        {
            serviceRecord = null;
            return returnedObject;
        }
    }

    private Server server;
    private ServiceBroker sb;
    private Requester requester;
    private long accessIdentifier;
    private HashMap interfaceCache;
}
