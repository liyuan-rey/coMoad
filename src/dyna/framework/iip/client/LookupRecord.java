// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LookupRecord.java

package dyna.framework.iip.client;

import dyna.framework.iip.Service;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class LookupRecord
{

    public LookupRecord()
    {
        serviceId = null;
        brokerId = null;
        ipAddress = null;
        port = 0;
        socket = null;
        in = null;
        out = null;
        serviceInterface = null;
        service = null;
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            System.err.println(e);
        }
        return null;
    }

    public String serviceId;
    public String brokerId;
    public InetAddress ipAddress;
    public int port;
    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;
    public Class serviceInterface;
    public Service service;
}
