// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Server.java

package dyna.framework;

import dyna.framework.iip.Service;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.RequestDispatcher;
import dyna.framework.iip.server.Requester;
import dyna.framework.iip.server.ServiceBroker;
import dyna.framework.iip.server.ServiceBrokerController;
import dyna.util.*;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Server
{

    public Server()
    {
        daemonLock = null;
        threadPool = null;
        brokerController = null;
        serviceBroker = null;
        requester = null;
        dispatcher = null;
        String tempString = null;
        server = this;
        System.out.println();
        System.out.println("DynaMOAD Server v.1.0.0 (Enterprise Edition)");
        System.out.println("Copyright (c) 2003-2004, Innovative PLM Solutions LTD. All rights reserved.");
        System.out.println();
        System.out.println("Start initialization.....");
        ACCESS_IDENTIFIER = System.currentTimeMillis();
        parms = XMLUtil.parse("server");
        if(parms == null)
        {
            System.err.println("[ERROR] Invalid configuration file.");
            System.exit(-1);
        }
        NUMBER_OF_THREAD_POOL = Integer.parseInt((String)parms.get("share.threadpool.number"));
        NUMBER_OF_BROKER = Integer.parseInt((String)parms.get("iip.sb.pool.number"));
        IS_BROKER_CONTROLLER_LOAD = Boolean.valueOf((String)parms.get("iip.sbc.load")).booleanValue();
        SERVICE_BROKER_ID = (String)parms.get("iip.sb.id");
        tempString = (String)parms.get("iip.sb.standalone");
        if(Utils.isNullString(tempString))
            IS_STANDALONE_MODE = false;
        else
            IS_STANDALONE_MODE = Boolean.valueOf(tempString).booleanValue();
        tempString = (String)parms.get("iip.sbc.inet.address");
        if(!Utils.isNullString(tempString))
            try
            {
                sbcInetAddress = InetAddress.getByName(tempString);
            }
            catch(UnknownHostException e)
            {
                System.err.println(e);
                sbcInetAddress = null;
            }
        else
            sbcInetAddress = null;
        tempString = (String)parms.get("iip.sbc.inet.port");
        if(Utils.isNullString(tempString))
            sbcPort = 0;
        else
            sbcPort = Integer.parseInt(tempString);
        tempString = (String)parms.get("iip.sb.inet.port");
        if(Utils.isNullString(tempString))
            sbPort = 0;
        else
            sbPort = Integer.parseInt(tempString);
        tempString = (String)parms.get("mailserver");
        if(!Utils.isNullString(tempString))
            mailServer = tempString;
        System.out.print("Thread Pool");
        if(NUMBER_OF_THREAD_POOL > 0)
        {
            System.out.print(" (Cache size: " + NUMBER_OF_THREAD_POOL + ")");
            threadPool = new ThreadPool(NUMBER_OF_THREAD_POOL);
        } else
        {
            System.out.print(" (Laziness Mode)");
            threadPool = new ThreadPool(0);
        }
        System.out.println(" [ok]");
        System.out.print("Service Broker Controller");
        if(IS_BROKER_CONTROLLER_LOAD)
        {
            brokerController = new ServiceBrokerController();
            brokerController.setAcessIdentifier(ACCESS_IDENTIFIER);
            brokerController.init();
            isBrokerControllerEnabled = true;
        } else
        {
            isBrokerControllerEnabled = false;
            System.out.println(" [NOT ENABLED]");
        }
        System.out.print("Service Broker");
        serviceBroker = new ServiceBroker(IS_STANDALONE_MODE, brokerController);
        serviceBroker.setAcessIdentifier(ACCESS_IDENTIFIER);
        serviceBroker.setID(SERVICE_BROKER_ID);
        serviceBroker.init();
        System.out.print("Intelligent Service Requester");
        requester = new Requester();
        requester.setAccessIdentifier(ACCESS_IDENTIFIER);
        requester.init();
        System.out.print("Dynamic Request Dispatcher");
        dispatcher = new RequestDispatcher();
        dispatcher.setAcessIdentifier(ACCESS_IDENTIFIER);
        dispatcher.init();
        serviceBroker.loadServices();
        isServiceBrokerEnabled = true;
        daemonLock = new DaemonLock();
        daemonLock.acquire();
        System.out.println();
        System.out.println("DynaFramework server: Ready to service");
    }

    public static void main(String args[])
    {
        new Server();
    }

    public synchronized boolean isBrokerControllerEnabled(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return isBrokerControllerEnabled;
        else
            return false;
    }

    public synchronized ThreadPool getThreadPool(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return threadPool;
        else
            return null;
    }

    public synchronized int getNumberOfBroker(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return NUMBER_OF_BROKER;
        else
            return 0;
    }

    public String getProperty(long accessIdentifier, String propertyName)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER && parms != null)
            return (String)parms.get(propertyName);
        else
            return null;
    }

    public ServiceBroker getServiceBroker(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return serviceBroker;
        else
            return null;
    }

    public Requester getRequester(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return requester;
        else
            return null;
    }

    public RequestDispatcher getRequestDispatcher(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return dispatcher;
        else
            return null;
    }

    public static Service getServiceInstance(String serviceId)
        throws ServiceNotFoundException
    {
        if(server == null || server.dispatcher == null)
            return null;
        else
            return server.dispatcher.getServiceInstance(serviceId);
    }

    public static Server server = null;
    private DaemonLock daemonLock;
    private ThreadPool threadPool;
    private ServiceBrokerController brokerController;
    private ServiceBroker serviceBroker;
    private Requester requester;
    private RequestDispatcher dispatcher;
    private static boolean isBrokerControllerEnabled = false;
    private static boolean isServiceBrokerEnabled = false;
    private static HashMap parms = null;
    private static int NUMBER_OF_THREAD_POOL = 128;
    private static int NUMBER_OF_BROKER = 32;
    private static boolean IS_BROKER_CONTROLLER_LOAD = false;
    private static boolean IS_STANDALONE_MODE = false;
    private static long ACCESS_IDENTIFIER = 0L;
    private static String SERVICE_BROKER_ID = null;
    public static InetAddress sbcInetAddress = null;
    public static int sbcPort = 0;
    public static InetAddress sbInetAddress = null;
    public static int sbPort = 0;
    public static String mailServer = null;

}