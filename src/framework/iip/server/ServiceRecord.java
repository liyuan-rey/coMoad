// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServiceRecord.java

package dyna.framework.iip.server;

import dyna.framework.iip.Service;

public class ServiceRecord
{

    public ServiceRecord()
    {
        serviceId = null;
        brokerId = null;
        status = null;
        startTime = 0L;
        lookupCount = 0L;
        lookupFailCount = 0L;
        runCount = 0L;
        runState = 0L;
        name = null;
        description = null;
        service = null;
        serviceInterface = null;
        serviceClassURI = null;
        serviceInterfaceURI = null;
        ipAddress = null;
        port = 0;
    }

    public String serviceId;
    public String brokerId;
    public String status;
    public long startTime;
    public long lookupCount;
    public long lookupFailCount;
    public long runCount;
    public long runState;
    public String name;
    public String description;
    public Service service;
    public Class serviceInterface;
    public String serviceClassURI;
    public String serviceInterfaceURI;
    public String ipAddress;
    public int port;
}
