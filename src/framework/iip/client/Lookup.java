// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Lookup.java

package dyna.framework.iip.client;

import dyna.framework.Client;
import dyna.util.BusyFlag;
import dyna.util.Utils;
import java.io.*;
import java.net.*;
import java.util.*;

// Referenced classes of package dyna.framework.iip.client:
//            LookupRecord

public class Lookup
{

    public Lookup()
    {
        multicastGroupString = "224.0.0.2";
        multicastPortNumber = 4000;
        lookupCountMax = 20;
        client = null;
        accessIdentifier = 0L;
        lookupTable = null;
        multicastSocket = null;
        multicastGroup = null;
        multicastPort = 0;
        datagramSocket = null;
        datagramPort = 0;
        datagramPacket = null;
        replyPacket = null;
        socket = null;
        sndBuffer = null;
        rcvBuffer = null;
        in = null;
        out = null;
        lookupRecord = null;
        lookupCount = 20;
        lookupFlag = null;
        serviceId = null;
        client = Client.client;
    }

    public synchronized void setAccessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        multicastGroupString = client.getProperty(accessIdentifier, "iip.multicast.group");
        try
        {
            multicastPortNumber = Integer.parseInt(client.getProperty(accessIdentifier, "iip.multicast.port"));
            lookupCountMax = Integer.parseInt(client.getProperty(accessIdentifier, "iip.lookup.timeout"));
            lookupCount = lookupCountMax;
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
        }
        try
        {
            multicastGroup = InetAddress.getByName(multicastGroupString);
            multicastPort = multicastPortNumber;
            multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.setTimeToLive(1);
            multicastSocket.joinGroup(multicastGroup);
            datagramSocket = new DatagramSocket();
            datagramPort = datagramSocket.getLocalPort();
            datagramSocket.setSoTimeout(500);
            rcvBuffer = new byte[256];
            replyPacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
            lookupTable = new HashMap();
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
        lookupFlag = new BusyFlag();
        System.out.println(" [initiated]");
    }

    public void removeLookupRecord(String serviceId)
    {
        if(!Utils.isNullString(serviceId))
            lookupTable.remove(serviceId);
    }

    public LookupRecord lookupService(String serviceId)
    {
        if(serviceId == null)
            return null;
        lookupFlag.getBusyFlag();
        lookupRecord = (LookupRecord)lookupTable.get(serviceId);
        if(lookupRecord != null)
        {
            lookupFlag.freeBusyFlag();
            return lookupRecord;
        }
        this.serviceId = new String(serviceId);
        String requestId = Long.toHexString(System.currentTimeMillis());
        String repliedRequestId = null;
        String s = null;
        byte lookupResult = 0;
        try
        {
            if(Client.sbInetAddress == null)
            {
                sndBuffer = ("DF30LKUP^" + serviceId + '^' + InetAddress.getLocalHost().getHostAddress() + '^' + datagramPort + '^' + requestId).getBytes();
                datagramPacket = new DatagramPacket(sndBuffer, sndBuffer.length, multicastGroup, multicastPort);
                while(lookupCount > 0) 
                {
                    multicastSocket.send(datagramPacket);
                    try
                    {
                        datagramSocket.receive(replyPacket);
                        s = (new String(replyPacket.getData())).trim();
                        if(s.startsWith("DF30RPLY") && s.endsWith(requestId))
                            lookupCount = -1;
                        else
                            s = null;
                    }
                    catch(InterruptedIOException interruptedioexception) { }
                    if(lookupCount >= 0)
                    {
                        try
                        {
                            datagramSocket.receive(replyPacket);
                            s = (new String(replyPacket.getData())).trim();
                            if(s.startsWith("DF30RPLY") && s.endsWith(requestId))
                                lookupCount = -1;
                            else
                                s = null;
                        }
                        catch(InterruptedIOException interruptedioexception1) { }
                        rcvBuffer = new byte[256];
                        replyPacket.setData(rcvBuffer);
                        lookupCount--;
                    }
                }
            } else
            {
                socket = new Socket(Client.sbInetAddress, Client.sbPort);
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 8192));
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 8192));
                out.writeUTF("DF30LKUP");
                out.writeUTF(serviceId);
                out.flush();
                lookupResult = in.readByte();
                if(lookupResult == 1)
                {
                    lookupRecord = new LookupRecord();
                    lookupRecord.brokerId = in.readUTF();
                    lookupRecord.serviceId = serviceId;
                    lookupRecord.ipAddress = Client.sbInetAddress;
                    lookupRecord.port = Client.sbPort;
                    lookupRecord.serviceInterface = Class.forName(in.readUTF());
                    lookupTable.put(lookupRecord.serviceId, lookupRecord);
                    lookupCount = -1;
                } else
                if(lookupResult == -1)
                    lookupRecord = null;
                out.close();
                in.close();
                socket.close();
                socket = null;
            }
            if(lookupCount == 0)
                lookupRecord = null;
            else
            if(lookupCount < 0 && Client.sbInetAddress == null)
            {
                List tokenList = Utils.tokenizeMessage(s, '^');
                lookupRecord = new LookupRecord();
                Iterator tokens = tokenList.iterator();
                tokens.next();
                lookupRecord.brokerId = (String)tokens.next();
                lookupRecord.serviceId = (String)tokens.next();
                lookupRecord.ipAddress = InetAddress.getByName((String)tokens.next());
                lookupRecord.port = Integer.parseInt((String)tokens.next());
                lookupRecord.serviceInterface = Class.forName((String)tokens.next());
                repliedRequestId = (String)tokens.next();
                tokens = null;
                if(requestId.equals(repliedRequestId))
                    lookupTable.put(lookupRecord.serviceId, lookupRecord);
                else
                    lookupRecord = null;
                tokenList.clear();
                tokenList = null;
            }
            lookupCount = lookupCountMax;
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
        catch(ClassNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        lookupFlag.freeBusyFlag();
        if(lookupRecord != null)
            return lookupRecord;
        else
            return null;
    }

    private String multicastGroupString;
    private int multicastPortNumber;
    private int lookupCountMax;
    private Client client;
    private long accessIdentifier;
    private HashMap lookupTable;
    private MulticastSocket multicastSocket;
    private InetAddress multicastGroup;
    private int multicastPort;
    private DatagramSocket datagramSocket;
    private int datagramPort;
    private DatagramPacket datagramPacket;
    private DatagramPacket replyPacket;
    private Socket socket;
    private byte sndBuffer[];
    private byte rcvBuffer[];
    private DataInputStream in;
    private DataOutputStream out;
    private LookupRecord lookupRecord;
    private int lookupCount;
    private BusyFlag lookupFlag;
    private String serviceId;
}
