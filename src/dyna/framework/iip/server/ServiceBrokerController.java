// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceBrokerController.java

package dyna.framework.iip.server;

import dyna.framework.Server;
import dyna.util.ThreadPool;
import dyna.util.Utils;
import java.io.*;
import java.net.*;
import java.util.*;

// Referenced classes of package dyna.framework.iip.server:
//            BrokerRecord, ServiceRecord

public class ServiceBrokerController
    implements Runnable
{

    public ServiceBrokerController()
    {
        server = null;
        threadPool = null;
        acessIdentifier = 0L;
        serverSocket = null;
        doRunMainLoop = true;
        brokerTable = null;
        serviceTable = null;
        server = Server.server;
    }

    public synchronized void setAcessIdentifier(long accessIdentifier)
    {
        acessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        threadPool = server.getThreadPool(acessIdentifier);
        try
        {
            serverSocket = new ServerSocket(Server.sbcPort);
        }
        catch(IOException e)
        {
            System.out.println(e);
            System.exit(-1);
        }
        brokerTable = new HashMap();
        serviceTable = new HashMap();
        threadPool.addRequest(this);
        System.out.println(" [initiated]");
    }

    public void addServiceBroker(BrokerRecord brokerRecord)
    {
        if(brokerRecord == null)
            return;
        brokerTable.put(brokerRecord.brokerId, brokerRecord);
        Iterator brokerKeys = null;
        String brokerKey = null;
        for(brokerKeys = brokerTable.keySet().iterator(); brokerKeys.hasNext(); replicateService(brokerKey))
            brokerKey = (String)brokerKeys.next();

        brokerKeys = null;
    }

    public void removeServiceBroker(String brokerId)
    {
        if(Utils.isNullString(brokerId))
        {
            return;
        } else
        {
            brokerTable.remove(brokerId);
            return;
        }
    }

    public void serviceChanged(String brokerId, HashMap serviceRecords)
    {
        Iterator serviceKeys = null;
        String serviceKey = null;
        if(serviceRecords == null)
            return;
        for(serviceKeys = serviceRecords.keySet().iterator(); serviceKeys.hasNext();)
        {
            serviceKey = (String)serviceKeys.next();
            if(!serviceTable.containsKey(serviceKey))
                serviceTable.put(serviceKey, serviceRecords.get(serviceKey));
        }

        serviceKeys = null;
        if(brokerTable == null || brokerTable.size() < 2)
            return;
        else
            return;
    }

    private void replicateService(String brokerKey)
    {
        BrokerRecord brokerRecord = (BrokerRecord)brokerTable.get(brokerKey);
        if(brokerRecord == null)
            return;
        else
            return;
    }

    private void sbc_helper1(DataOutputStream out, String brokerId)
    {
        ServiceRecord serviceRecord = null;
        Iterator serviceKeys = null;
        String serviceKey = null;
        try
        {
            for(serviceKeys = serviceTable.keySet().iterator(); serviceKeys.hasNext();)
            {
                serviceKey = (String)serviceKeys.next();
                serviceRecord = (ServiceRecord)serviceTable.get(serviceKey);
                if(serviceRecord != null && !brokerId.equals(serviceRecord.brokerId))
                {
                    out.writeUTF(serviceRecord.serviceId);
                    out.writeUTF(serviceRecord.brokerId);
                    out.writeUTF(serviceRecord.name);
                    out.writeUTF(serviceRecord.description);
                    out.writeUTF(serviceRecord.status);
                    out.writeLong(serviceRecord.startTime);
                    out.writeUTF(serviceRecord.serviceInterfaceURI);
                    out.writeUTF(serviceRecord.serviceClassURI);
                    out.writeUTF(serviceRecord.ipAddress);
                    out.writeInt(serviceRecord.port);
                }
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        serviceKeys = null;
    }

    public synchronized void run()
    {
        Socket socket = null;
        try
        {
            while(doRunMainLoop) 
            {
                socket = serverSocket.accept();
                response(socket);
                socket = null;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void response(Socket socket)
    {
        try
        {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String request = in.readUTF();
            if(request.equals("@SBC_ADD_BROKER"))
            {
                String brokerId = in.readUTF();
                int port = in.readInt();
                BrokerRecord brokerRecord = new BrokerRecord();
                brokerRecord.brokerId = brokerId;
                brokerRecord.InetAddress = socket.getInetAddress();
                brokerRecord.port = port;
                addServiceBroker(brokerRecord);
                System.out.println("Service Broker [" + brokerId + "] connected from " + brokerRecord.InetAddress.getHostAddress() + ":" + port);
                DataOutputStream out = null;
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("DF30SRPL");
                sbc_helper1(out, brokerId);
                out.writeUTF("@SBC_END_OF_DATA");
                in.close();
                in = null;
                out.close();
                out = null;
                socket.close();
                socket = null;
                brokerRecord = null;
            } else
            if(request.equals("@SBC_SERVICE_CHANGED"))
            {
                String brokerId = in.readUTF();
                String serviceId = null;
                HashMap serviceRecords = new HashMap();
                ServiceRecord serviceRecord = null;
                do
                {
                    serviceRecord = new ServiceRecord();
                    serviceId = in.readUTF();
                    if("@SBC_END_OF_DATA".equals(serviceId))
                        break;
                    serviceRecord.brokerId = brokerId;
                    serviceRecord.serviceId = serviceId;
                    serviceRecord.name = in.readUTF();
                    serviceRecord.description = in.readUTF();
                    serviceRecord.status = in.readUTF();
                    serviceRecord.startTime = in.readLong();
                    serviceRecord.serviceInterfaceURI = in.readUTF();
                    serviceRecord.serviceClassURI = in.readUTF();
                    serviceRecord.ipAddress = in.readUTF();
                    serviceRecord.port = in.readInt();
                    serviceRecords.put(serviceId, serviceRecord);
                } while(true);
                in.close();
                in = null;
                socket.close();
                socket = null;
                serviceChanged(brokerId, serviceRecords);
                serviceRecords = null;
                serviceRecord = null;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public BrokerRecord getBrokerRecord(String brokerId)
    {
        return (BrokerRecord)brokerTable.get(brokerId);
    }

    private Server server;
    private ThreadPool threadPool;
    private long acessIdentifier;
    private ServerSocket serverSocket;
    private boolean doRunMainLoop;
    private HashMap brokerTable;
    private HashMap serviceTable;
    public static final String ADD_BROKER = "@SBC_ADD_BROKER";
    public static final String SERVICE_CHANGED = "@SBC_SERVICE_CHANGED";
    public static final String END_OF_DATA = "@SBC_END_OF_DATA";
}
