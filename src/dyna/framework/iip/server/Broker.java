// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Broker.java

package dyna.framework.iip.server;

import dyna.framework.iip.IIP;
import dyna.framework.iip.IIPRequestException;
import dyna.util.Utils;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;

// Referenced classes of package dyna.framework.iip.server:
//            ServiceBroker, ServiceRecord

public class Broker
    implements Runnable
{

    public Broker(ServiceBroker serviceBroker, HashMap clientAddressMap, HashMap clientContextMap)
    {
        socket = null;
        sb = null;
        this.clientAddressMap = null;
        this.clientContextMap = null;
        sb = serviceBroker;
        this.clientAddressMap = clientAddressMap;
        this.clientContextMap = clientContextMap;
    }

    public synchronized void setSocket(Socket socket)
    {
        if(socket != null)
            this.socket = socket;
    }

    public synchronized void wakeup()
    {
        notify();
    }

    private void responseRequest(DataOutputStream out, Object data)
        throws IOException
    {
        if(data == null)
        {
            out.writeByte(0);
        } else
        {
            out.writeByte(1);
            out.writeInt(1);
            IIP.dataWriter(out, data);
        }
        out.flush();
    }

    private void responseFail(DataOutputStream out, Object data)
        throws IOException
    {
        out.writeByte(-1);
        if(data == null)
        {
            out.writeInt(0);
        } else
        {
            out.writeInt(1);
            IIP.dataWriter(out, data);
        }
        out.flush();
    }

    public synchronized void run()
    {
        DataInputStream in;
        DataOutputStream out;
        String str;
        InetAddress clientInetAddress;
        String clientAddress;
        Object argumentValueArray[];
        HashMap clientContext;
        in = null;
        out = null;
        str = null;
        clientInetAddress = null;
        clientAddress = null;
        argumentValueArray = (Object[])null;
        ServiceRecord serviceRecord = null;
        Object returnedObject = null;
        clientContext = null;
        int argumentNumber = 0;
_L2:
        InterruptedException e;
        try
        {
            do
            {
label0:
                {
                    wait();
                    if(socket != null)
                        break label0;
                }
            } while(true);
        }
        finally
        {
            ServiceRecord serviceRecord = null;
            Object returnedObject = null;
            InetAddress tempInetAddress = (InetAddress)clientAddressMap.remove(Thread.currentThread());
            if(tempInetAddress != null && !clientAddressMap.containsValue(tempInetAddress))
                clientContextMap.remove(tempInetAddress.getHostAddress());
            if(in != null)
                try
                {
                    in.close();
                    out.close();
                    socket.close();
                }
                catch(IOException ioexception1) { }
            in = null;
            out = null;
            socket = null;
            sb.returnBroker(this);
        }
        clientInetAddress = socket.getInetAddress();
        clientAddress = clientInetAddress.getHostAddress();
        clientAddressMap.put(Thread.currentThread(), clientInetAddress);
        clientContext = (HashMap)clientContextMap.get(clientAddress);
        if(clientContext == null)
        {
            clientContext = new HashMap();
            clientContextMap.put(clientAddress, clientContext);
        }
        clientContext.put("inet.address", clientInetAddress);
        clientContext.put("last.connection.date", new Date());
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 8192));
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 8192));
        while((str = in.readUTF()) != null) 
        {
            ServiceRecord serviceRecord = sb.getServiceRecord(str);
            int argumentNumber;
            if(serviceRecord != null)
            {
                str = in.readUTF();
                str = in.readUTF();
                argumentNumber = in.readByte();
            } else
            {
                if(str.equals("DF30LKUP"))
                {
                    str = in.readUTF();
                    serviceRecord = sb.getServiceRecord(str);
                    if(serviceRecord == null)
                    {
                        out.writeByte(-1);
                    } else
                    {
                        out.writeByte(1);
                        out.writeUTF(serviceRecord.brokerId);
                        out.writeUTF(serviceRecord.serviceInterface.getName());
                    }
                    out.flush();
                    break;
                }
                responseFail(out, "INVALIDSERVICE");
                continue;
            }
            if(argumentNumber > 0)
                argumentValueArray = new Object[argumentNumber];
            for(int i = 0; i < argumentNumber; i++)
                argumentValueArray[i] = IIP.dataReader(in);

            serviceRecord.runState++;
            Object returnedObject = Utils.invokeMethod(serviceRecord.service, str, ((Object) (argumentValueArray)));
            serviceRecord.runState--;
            serviceRecord.runCount++;
            serviceRecord = null;
            if(argumentValueArray != null)
                argumentValueArray = (Object[])null;
            if(returnedObject instanceof IIPRequestException)
                responseFail(out, returnedObject);
            else
            if(returnedObject instanceof Exception)
                responseFail(out, returnedObject);
            else
                responseRequest(out, returnedObject);
            returnedObject = null;
        }
          goto _L1
        e;
        System.err.println(e);
          goto _L1
        e;
        if(in != null)
            try
            {
                in.close();
                out.close();
                socket.close();
            }
            catch(IOException ioexception) { }
        in = null;
        out = null;
        socket = null;
          goto _L1
        e;
        System.err.println(e);
          goto _L1
        e;
        System.err.println(e);
          goto _L1
        e;
        System.err.println(e);
          goto _L1
        e;
        System.err.println(e);
_L1:
          goto _L2
    }

    private Socket socket;
    private ServiceBroker sb;
    private HashMap clientAddressMap;
    private HashMap clientContextMap;
}
