// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Requester.java

package dyna.framework.iip.server;

import dyna.framework.Server;
import dyna.framework.iip.*;
import dyna.util.Utils;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

// Referenced classes of package dyna.framework.iip.server:
//            ServiceBroker, ServiceRecord

public class Requester
{

    public Requester()
    {
        server = null;
        accessIdentifier = 0L;
        sb = null;
        server = Server.server;
    }

    public synchronized void setAccessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        socketCache = new HashMap();
        sb = server.getServiceBroker(accessIdentifier);
        System.out.println(" [initiated]");
    }

    private void lookup(String serviceId)
    {
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        ServiceRecord service = null;
        service = sb.getServiceRecord(serviceId);
        if(!socketCache.containsKey(serviceId))
        {
            try
            {
                socket = new Socket(service.ipAddress, service.port);
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 8192));
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 8192));
            }
            catch(IOException e)
            {
                System.err.println(e);
            }
            socketCache.put(serviceId, socket);
            socketCache.put(serviceId + ".out", out);
            socketCache.put(serviceId + ".in", in);
        }
    }

    public synchronized Object request(String serviceId, String requestName, Object arguments)
        throws ServiceNotFoundException, IIPRequestException
    {
        if(Utils.isNullString(serviceId) || Utils.isNullString(requestName))
            return null;
        boolean success = false;
        Object returnObject = null;
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        byte returnState = 0;
        int returnCount = 0;
        int i = 0;
        int size = 0;
        ServiceRecord service = null;
        service = sb.getServiceRecord(serviceId);
        try
        {
            while(!success) 
            {
                socket = new Socket(service.ipAddress, service.port);
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 8192));
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 8192));
                socketCache.put(serviceId, socket);
                socketCache.put(serviceId + ".out", out);
                socketCache.put(serviceId + ".in", in);
                try
                {
                    out.writeUTF(serviceId);
                    out.writeUTF("");
                    out.writeUTF(requestName);
                    if(arguments != null)
                    {
                        if(arguments instanceof List)
                        {
                            List list = (List)arguments;
                            size = list.size();
                            out.writeByte(size);
                            Iterator args;
                            for(args = list.iterator(); args.hasNext(); IIP.dataWriter(out, args.next()));
                            args = null;
                            list = null;
                        } else
                        if(arguments instanceof Object[])
                        {
                            Object array[] = (Object[])arguments;
                            size = array.length;
                            out.writeByte(size);
                            for(i = 0; i < array.length; i++)
                                IIP.dataWriter(out, array[i]);

                            array = (Object[])null;
                        }
                    } else
                    {
                        out.writeByte(size);
                    }
                    out.flush();
                    returnState = in.readByte();
                    switch(returnState)
                    {
                    case 0: // '\0'
                        returnCount = 0;
                        break;

                    case -1: 
                    case 1: // '\001'
                        returnCount = in.readInt();
                        break;
                    }
                    if(returnCount > 0)
                        returnObject = IIP.dataReader(in);
                    else
                        returnObject = null;
                    success = true;
                }
                catch(IOException e)
                {
                    socketCache.remove(serviceId);
                    socketCache.remove(serviceId + ".in");
                    socketCache.remove(serviceId + ".out");
                }
                catch(ClassNotFoundException e)
                {
                    System.err.println(e);
                }
                catch(InstantiationException e)
                {
                    System.err.println(e);
                }
                catch(IllegalAccessException e)
                {
                    System.err.println(e);
                }
            }
        }
        catch(UnknownHostException e1)
        {
            e1.printStackTrace();
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }
        if(!success)
        {
            in = null;
            out = null;
            socket = null;
            throw new ServiceNotFoundException(requestName + " at " + serviceId);
        }
        if(returnState == -1)
        {
            in = null;
            out = null;
            socket = null;
            throw new IIPRequestException(requestName + " at " + serviceId + "\n" + returnObject);
        } else
        {
            in = null;
            out = null;
            socket = null;
            return returnObject;
        }
    }

    private Server server;
    private long accessIdentifier;
    private ServiceBroker sb;
    private static HashMap socketCache = null;

}
