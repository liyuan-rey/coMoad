// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BrokerRecord.java

package dyna.framework.iip.server;

import java.net.InetAddress;

public class BrokerRecord
{

    public BrokerRecord()
    {
        InetAddress = null;
        port = 0;
        brokerId = null;
        status = null;
        startTime = 0L;
        lookupCount = 0L;
        lookupFailCount = 0L;
        responseTime = 0L;
        lastResponseTime = 0L;
        timeInterval = 0L;
        serviceCount = 0L;
        replicationCount = 0L;
    }

    public InetAddress InetAddress;
    public int port;
    public String brokerId;
    public String status;
    public long startTime;
    public long lookupCount;
    public long lookupFailCount;
    public long responseTime;
    public long lastResponseTime;
    public long timeInterval;
    public long serviceCount;
    public long replicationCount;
}
