// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Requester.java

package dyna.framework.iip.client;

import dyna.framework.Client;
import dyna.framework.iip.*;
import dyna.util.Utils;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package dyna.framework.iip.client:
//            LookupRecord

public class Requester
{

    public Requester()
    {
        client = null;
        accessIdentifier = 0L;
        client = Client.client;
    }

    public synchronized void setAccessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        System.out.println(" [initiated]");
    }

    private LookupRecord lookup(String serviceId)
    {
        LookupRecord lookupRecord = client.lookupService(accessIdentifier, serviceId);
        if(lookupRecord == null)
            return null;
        while(lookupRecord.socket == null) 
            try
            {
                lookupRecord.socket = new Socket(lookupRecord.ipAddress, lookupRecord.port);
                lookupRecord.out = new DataOutputStream(new BufferedOutputStream(lookupRecord.socket.getOutputStream(), 8192));
                lookupRecord.in = new DataInputStream(new BufferedInputStream(lookupRecord.socket.getInputStream(), 8192));
            }
            catch(IOException e)
            {
                client.removeLookupRecord(accessIdentifier, serviceId);
                lookupRecord = client.lookupService(accessIdentifier, serviceId);
            }
        return lookupRecord;
    }

    public synchronized Object request(String serviceId, String requestName, Object arguments)
        throws ServiceNotFoundException, IIPRequestException
    {
        if(Utils.isNullString(serviceId) || Utils.isNullString(requestName))
            return null;
        LookupRecord lookupRecord = null;
        boolean success = false;
        Object returnObject = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        byte returnState = 0;
        int returnCount = 0;
        int i = 0;
        int size = 0;
        while(!success) 
        {
            lookupRecord = lookup(serviceId);
            if(lookupRecord == null)
                break;
            out = lookupRecord.out;
            in = lookupRecord.in;
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
                client.removeLookupRecord(accessIdentifier, serviceId);
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
        if(!success)
        {
            in = null;
            out = null;
            lookupRecord = null;
            throw new ServiceNotFoundException(requestName + " at " + serviceId);
        }
        if(returnState == -1)
        {
            in = null;
            out = null;
            lookupRecord = null;
            throw new IIPRequestException(requestName + " at " + serviceId + "\n" + returnObject);
        } else
        {
            in = null;
            out = null;
            lookupRecord = null;
            return returnObject;
        }
    }

    private Client client;
    private long accessIdentifier;
}
