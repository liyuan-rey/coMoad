// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceBroker.java

package dyna.framework.iip.server;

import dyna.framework.Server;
import dyna.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

// Referenced classes of package dyna.framework.iip.server:
//            Broker, BrokerRecord, ServiceBrokerController, ServiceRecord, 
//            ServiceServer, RequestDispatcher

public class ServiceBroker
    implements Runnable
{
    private class LookupResponse
        implements Runnable
    {

        public synchronized void run()
        {
            try
            {
                multicastSocket.joinGroup(multicastGroup);
                while(doRunLookupResponse) 
                {
                    datagramPacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
                    multicastSocket.receive(datagramPacket);
                    String s = new String(datagramPacket.getData());
                    if(!s.startsWith("DF30LKUP"))
                    {
                        s = null;
                        continue;
                    }
                    List tokenList = Utils.tokenizeMessage(s, '^');
                    Iterator tokens = tokenList.iterator();
                    tokens.next();
                    ServiceRecord service = (ServiceRecord)serviceTable.get(tokens.next());
                    if(service == null)
                    {
                        tokenList.clear();
                        tokenList = null;
                        continue;
                    }
                    clientAddress = InetAddress.getByName((String)tokens.next());
                    port = 0;
                    BrokerRecord broker = null;
                    if(sbc != null)
                        broker = sbc.getBrokerRecord(service.brokerId);
                    try
                    {
                        port = Integer.parseInt((String)tokens.next());
                    }
                    catch(NumberFormatException numberformatexception) { }
                    requestId = (String)tokens.next();
                    tokens = null;
                    tokenList.clear();
                    tokenList = null;
                    if(port == 0 || sbc != null && broker == null)
                        continue;
                    if(service.serviceInterface == null && service.serviceInterfaceURI != null && broker != null && broker.InetAddress != null)
                    {
                        sndBuffer = ("DF30RPLY^" + service.brokerId + '^' + service.serviceId + '^' + broker.InetAddress.getHostAddress() + '^' + broker.port + '^' + service.serviceInterfaceURI + '^' + requestId).getBytes();
                    } else
                    {
                        if(service.serviceInterface == null || !brokerId.equals(service.brokerId))
                            continue;
                        sndBuffer = ("DF30RPLY^" + service.brokerId + '^' + service.serviceId + '^' + InetAddress.getLocalHost().getHostAddress() + '^' + tcpPort + '^' + service.serviceInterface.getName() + '^' + requestId).getBytes();
                    }
                    datagramPacket = new DatagramPacket(sndBuffer, sndBuffer.length, clientAddress, port);
                    datagramSocket.send(datagramPacket);
                    service.lookupCount++;
                    service = null;
                    datagramPacket = null;
                }
            }
            catch(IOException e)
            {
                System.err.println(e);
                System.exit(-1);
            }
            finally
            {
                try
                {
                    multicastSocket.leaveGroup(multicastGroup);
                }
                catch(IOException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
                multicastSocket.close();
                multicastSocket = null;
                System.err.println("Service Broker: look-up service closed.");
            }
            return;
        }

        private InetAddress clientAddress;
        private int port;
        private String requestId;

        public LookupResponse()
        {
            clientAddress = null;
            requestId = null;
        }
    }


    public ServiceBroker(boolean standaloneMode, ServiceBrokerController sbc)
    {
        multicastGroupString = "224.0.0.2";
        multicastPortNumber = 4000;
        brokerId = "BRKR";
        server = null;
        this.sbc = null;
        threadPool = null;
        lookupResponse = null;
        accessIdentifier = 0L;
        isBrokerControllerEnabled = false;
        isStandaloneMode = false;
        doRunMainLoop = true;
        doRunLookupResponse = true;
        numberOfBroker = 0;
        serviceTable = null;
        serviceParm = null;
        clientAddressMap = null;
        clientContextMap = null;
        serviceList = null;
        inBrokerPool = null;
        outBrokerPool = null;
        multicastSocket = null;
        serverSocket = null;
        datagramSocket = null;
        datagramPacket = null;
        multicastGroup = null;
        multicastPort = 0;
        sndBuffer = null;
        rcvBuffer = new byte[128];
        tcpPort = 0;
        server = Server.server;
        isStandaloneMode = standaloneMode;
        this.sbc = sbc;
    }

    public synchronized void setAcessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void setID(String id)
    {
        brokerId = brokerId + id;
    }

    public String getBrokerId()
    {
        return brokerId;
    }

    public synchronized void init()
    {
        isBrokerControllerEnabled = server.isBrokerControllerEnabled(accessIdentifier);
        multicastGroupString = server.getProperty(accessIdentifier, "iip.multicast.group");
        try
        {
            multicastPortNumber = Integer.parseInt(server.getProperty(accessIdentifier, "iip.multicast.port"));
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
        }
        serviceTable = new HashMap();
        clientAddressMap = new HashMap();
        clientContextMap = new HashMap();
        threadPool = server.getThreadPool(accessIdentifier);
        numberOfBroker = server.getNumberOfBroker(accessIdentifier);
        if(isStandaloneMode)
            System.out.print(" (Standalone mode)");
        System.out.print(" (Broker: " + numberOfBroker + ")");
        outBrokerPool = Collections.synchronizedList(new LinkedList());
        inBrokerPool = Collections.synchronizedList(new LinkedList());
        for(int i = 0; i < numberOfBroker; i++)
        {
            outBrokerPool.add(new Broker(this, clientAddressMap, clientContextMap));
            threadPool.addRequest((Broker)outBrokerPool.get(i));
        }

        try
        {
            multicastGroup = InetAddress.getByName(multicastGroupString);
            multicastPort = multicastPortNumber;
            multicastSocket = new MulticastSocket(multicastPort);
            datagramSocket = new DatagramSocket();
            serverSocket = new ServerSocket(Server.sbPort);
            tcpPort = serverSocket.getLocalPort();
        }
        catch(UnknownHostException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        threadPool.addRequest(this);
        lookupResponse = new LookupResponse();
        threadPool.addRequest(lookupResponse);
        serviceParm = XMLUtil.parse("service");
        if(serviceParm == null)
        {
            System.err.println("  [ERROR] Invalid service configuration file.");
            System.exit(-1);
        }
        regiterServiceBrokerToController();
        System.out.println(" [initiated]");
    }

    private void regiterServiceBrokerToController()
    {
        if(!isStandaloneMode)
        {
            BrokerRecord brokerRecord = null;
            if(isBrokerControllerEnabled)
            {
                brokerRecord = new BrokerRecord();
                brokerRecord.brokerId = brokerId;
                brokerRecord.InetAddress = serverSocket.getInetAddress();
                brokerRecord.port = tcpPort;
                sbc.addServiceBroker(brokerRecord);
            } else
            if(Server.sbcInetAddress != null)
                try
                {
                    Socket socket = new Socket(Server.sbcInetAddress, Server.sbcPort);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    out.writeUTF("@SBC_ADD_BROKER");
                    out.writeUTF(brokerId);
                    out.writeInt(tcpPort);
                    out.flush();
                    String str = in.readUTF();
                    if(str.equals("DF30SRPL"))
                    {
                        String serviceId = null;
                        ServiceRecord serviceRecord = null;
                        do
                        {
                            serviceRecord = new ServiceRecord();
                            serviceId = in.readUTF();
                            if("@SBC_END_OF_DATA".equals(serviceId))
                                break;
                            serviceRecord.serviceId = serviceId;
                            serviceRecord.brokerId = in.readUTF();
                            serviceRecord.name = in.readUTF();
                            serviceRecord.description = in.readUTF();
                            serviceRecord.status = in.readUTF();
                            serviceRecord.startTime = in.readLong();
                            serviceRecord.serviceInterfaceURI = in.readUTF();
                            serviceRecord.serviceClassURI = in.readUTF();
                            serviceRecord.ipAddress = in.readUTF();
                            serviceRecord.port = in.readInt();
                            if(getServiceRecord(serviceId) != null)
                            {
                                serviceRecord = null;
                            } else
                            {
                                addServiceRecord(serviceRecord);
                                serviceRecord = null;
                            }
                        } while(true);
                    } else
                    {
                        in.readUTF();
                    }
                    in.close();
                    in = null;
                    out.close();
                    out = null;
                    socket.close();
                    socket = null;
                }
                catch(IOException e)
                {
                    Server.sbcInetAddress = null;
                    Server.sbcPort = -1;
                    e.printStackTrace();
                }
            brokerRecord = null;
        }
    }

    public synchronized void loadServices()
    {
        System.out.println("Load IIP Service...");
        serviceList = Utils.tokenizeMessage((String)serviceParm.get("service-list"), ' ');
        String serviceElement = null;
        ServiceRecord record = null;
        for(Iterator iterator = serviceList.iterator(); iterator.hasNext();)
        {
            serviceElement = (String)iterator.next();
            if(!Utils.isNullString(serviceElement))
            {
                record = new ServiceRecord();
                record.serviceId = (String)serviceParm.get(serviceElement + ".id");
                record.name = (String)serviceParm.get(serviceElement + ".name");
                record.description = (String)serviceParm.get(serviceElement + ".desc");
                record.brokerId = brokerId;
                try
                {
                    record.serviceClassURI = (String)serviceParm.get(serviceElement + ".class");
                    record.serviceInterfaceURI = (String)serviceParm.get(serviceElement + ".interface");
                    record.serviceInterface = Class.forName(record.serviceInterfaceURI);
                    Class serviceClass = Class.forName(record.serviceClassURI);
                    Object service = serviceClass.newInstance();
                    if(!(service instanceof ServiceServer))
                        throw new ClassCastException("A Service class must extends class, 'Service'.");
                    record.ipAddress = InetAddress.getLocalHost().getHostAddress();
                    record.port = tcpPort;
                    System.out.print("  ");
                    ((ServiceServer)service).init(this, record, accessIdentifier);
                    System.out.println();
                    record = null;
                }
                catch(ClassNotFoundException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
                catch(InstantiationException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
                catch(IllegalAccessException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
                catch(UnknownHostException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
            }
        }

        if(isBrokerControllerEnabled)
            sbc.serviceChanged(brokerId, serviceTable);
        else
        if(Server.sbcInetAddress != null)
            try
            {
                Socket socket = new Socket(Server.sbcInetAddress, Server.sbcPort);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("@SBC_SERVICE_CHANGED");
                out.writeUTF(brokerId);
                sbc_helper1(out);
                out.writeUTF("@SBC_END_OF_DATA");
                out.close();
                out = null;
                socket.close();
                socket = null;
            }
            catch(IOException e)
            {
                Server.sbcInetAddress = null;
                Server.sbcPort = -1;
                System.err.println(e);
            }
    }

    private void sbc_helper1(DataOutputStream out)
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
                if(serviceRecord != null && brokerId.equals(serviceRecord.brokerId))
                {
                    out.writeUTF(serviceRecord.serviceId);
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

    public synchronized void addServiceRecord(ServiceRecord serviceRecord)
    {
        if(serviceRecord != null && serviceTable != null && serviceRecord.serviceId != null && serviceRecord.brokerId != null)
            serviceTable.put(serviceRecord.serviceId, serviceRecord);
    }

    public synchronized ServiceRecord getServiceRecord(String serviceId)
    {
        if(serviceId != null && serviceTable != null)
            return (ServiceRecord)serviceTable.get(serviceId);
        else
            return null;
    }

    public synchronized void removeServiceRecord(String serviceId)
    {
        if(serviceId != null && serviceTable != null)
            serviceTable.remove(serviceId);
    }

    public void returnBroker(Broker broker)
    {
        if(broker == null || inBrokerPool == null || outBrokerPool == null)
            return;
        if(inBrokerPool.remove(broker))
            outBrokerPool.add(broker);
    }

    public boolean isLocalService(ServiceRecord serviceRecord)
    {
        if(serviceRecord == null || serviceRecord.brokerId == null || serviceRecord.brokerId.equals(""))
            return false;
        else
            return brokerId.equals(serviceRecord.brokerId);
    }

    public RequestDispatcher getRequestDispatcher(long accessIdentifier)
    {
        return server.getRequestDispatcher(accessIdentifier);
    }

    public InetAddress getClientInetAdress(Thread thread)
    {
        return (InetAddress)clientAddressMap.get(thread);
    }

    public HashMap getClientContext(Thread thread)
    {
        InetAddress inet = (InetAddress)clientAddressMap.get(thread);
        if(inet != null)
            return (HashMap)clientContextMap.get(inet.getHostAddress());
        else
            return null;
    }

    public HashMap getClientContext(InetAddress inetAddress)
    {
        if(inetAddress != null)
            return (HashMap)clientContextMap.get(inetAddress.getHostAddress());
        else
            return null;
    }

    public void setServerContext(Object key, HashMap context)
    {
        clientContextMap.put(key, context);
    }

    public HashMap getServerContext(Object key)
    {
        return (HashMap)clientContextMap.get(key);
    }

    public boolean isConnectedClient(InetAddress inetAddress)
    {
        return clientAddressMap.containsValue(inetAddress);
    }

    public void run()
    {
        Socket socket = null;
        Broker broker = null;
        try
        {
            while(doRunMainLoop) 
            {
                socket = serverSocket.accept();
                if(!outBrokerPool.isEmpty())
                    broker = (Broker)outBrokerPool.remove(0);
                else
                    broker = null;
                while(broker == null) 
                {
                    Thread.sleep(50L);
                    System.out.print('+');
                    if(!outBrokerPool.isEmpty())
                    {
                        System.out.print('F');
                        broker = (Broker)outBrokerPool.remove(0);
                    } else
                    {
                        System.out.print('T');
                        broker = null;
                    }
                    System.out.print(outBrokerPool.size());
                }
                inBrokerPool.add(broker);
                broker.setSocket(socket);
                broker.wakeup();
            }
        }
        catch(InterruptedException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        System.err.println("Service Broker: main service closed.");
    }

    public synchronized void shutdown()
    {
        long runCount = 0L;
        long lookupCount = 0L;
        System.err.println("Service Broker: look-up service closing.");
        doRunLookupResponse = false;
        System.err.println("Service Broker: main service closing.");
        doRunMainLoop = false;
        if(serviceTable == null || serviceTable.isEmpty())
        {
            System.err.println("Service Broker: no IIP service loaded.");
            System.exit(0);
        }
        String serviceId = null;
        ServiceRecord sr = null;
        Iterator serviceKey;
        for(serviceKey = serviceTable.keySet().iterator(); serviceKey.hasNext();)
        {
            serviceId = (String)serviceKey.next();
            if(!Utils.isNullString(serviceId) && !serviceId.startsWith("DF30SMS"))
            {
                sr = (ServiceRecord)serviceTable.get(serviceId);
                System.err.print("[" + sr.name + "] service closing ");
                try
                {
                    wait(200L);
                    System.err.print('.');
                    while(sr.runState > 0L) 
                    {
                        wait(200L);
                        System.err.print('.');
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                System.err.println();
                lookupCount += sr.lookupCount;
                runCount += sr.runCount;
                serviceKey.remove();
                System.err.println("[" + sr.name + "] service closed. lookup count: " + sr.lookupCount + ", run count: " + sr.runCount);
                serviceId = null;
            }
        }

        serviceKey = null;
        System.err.println("Service Broker: lookup count: " + lookupCount + ", run count: " + runCount);
        System.err.println("Service Broker: all IIP service closed.");
    }

    private String multicastGroupString;
    private int multicastPortNumber;
    private String brokerId;
    private Server server;
    private ServiceBrokerController sbc;
    private ThreadPool threadPool;
    private LookupResponse lookupResponse;
    private long accessIdentifier;
    private boolean isBrokerControllerEnabled;
    private boolean isStandaloneMode;
    private boolean doRunMainLoop;
    private boolean doRunLookupResponse;
    private int numberOfBroker;
    private HashMap serviceTable;
    private HashMap serviceParm;
    private HashMap clientAddressMap;
    private HashMap clientContextMap;
    private List serviceList;
    private List inBrokerPool;
    private List outBrokerPool;
    private MulticastSocket multicastSocket;
    private ServerSocket serverSocket;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private InetAddress multicastGroup;
    private int multicastPort;
    private byte sndBuffer[];
    private byte rcvBuffer[];
    private int tcpPort;














}
