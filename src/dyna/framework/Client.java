// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Client.java

package dyna.framework;

import dyna.framework.iip.Service;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.client.Lookup;
import dyna.framework.iip.client.LookupRecord;
import dyna.framework.iip.client.RequestDispatcher;
import dyna.framework.iip.client.Requester;
import dyna.util.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
    public class Licenser extends Thread
    {

        public void run()
        {
            do
                try
                {
                    check();
                    Thread.sleep(0x493e0L);
                }
                catch(InterruptedException ie)
                {
                    System.out.println(ie);
                }
            while(true);
        }

        public synchronized void check()
        {
            String aProduct = null;
            for(Iterator products = licenses.keySet().iterator(); products.hasNext();)
            {
                aProduct = (String)products.next();
                if(aProduct.startsWith("DP-"))
                    maintain(aProduct);
            }

        }

        public void maintain(String aProduct)
        {
            Socket serverSocket = null;
            DataInputStream dis = null;
            PrintStream ps = null;
            boolean finished = false;
            String product = null;
            String module = null;
            String version = null;
            String type = null;
            String key = null;
            String server = null;
            String port = null;
            Integer portInteger = null;
            String flag = null;
            String tempString = null;
            product = (String)licenses.get("license." + aProduct + ".product");
            module = (String)licenses.get("license." + aProduct + ".module");
            version = (String)licenses.get("license." + aProduct + ".version");
            type = (String)licenses.get("license." + aProduct + ".type");
            key = (String)licenses.get("license." + aProduct + ".key");
            server = (String)licenses.get("license." + aProduct + ".server");
            port = (String)licenses.get("license." + aProduct + ".port");
            flag = (String)licenses.get(aProduct);
            if(flag.equals("L") || flag.equals("F"))
                return;
            if(port != null)
                portInteger = new Integer(port);
            try
            {
                if(type != null && type.startsWith("01") && key != null)
                {
                    licenses.put(aProduct, "T");
                    server = "[NODELOCK]";
                    port = "[FILE]";
                } else
                if(server != null)
                {
                    serverSocket = new Socket(server, portInteger.intValue());
                    dis = new DataInputStream(serverSocket.getInputStream());
                    ps = new PrintStream(serverSocket.getOutputStream());
                    if(flag.equals("I"))
                        ps.println("requestLicense");
                    else
                    if(flag.equals("T"))
                        ps.println("maintainLicense");
                    else
                    if(flag.equals("R"))
                        ps.println("releaseLicense");
                    ps.println(product);
                    ps.println(module);
                    ps.println(version);
                    tempString = dis.readLine();
                    if(tempString != null && tempString.startsWith("+"))
                    {
                        if(flag.equals("I"))
                        {
                            tempString = dis.readLine();
                            tempString = dis.readLine();
                            tempString = dis.readLine();
                            licenses.put(aProduct, "T");
                        }
                    } else
                    {
                        licenses.put(aProduct, "F");
                    }
                    ps.close();
                    ps = null;
                    dis.close();
                    dis = null;
                    serverSocket.close();
                    serverSocket = null;
                } else
                {
                    licenses.put(aProduct, "F");
                }
                System.out.println(aProduct + " " + flag + " = " + product + "-" + module + "[" + version + "] " + server + ":" + port + " " + new Date());
            }
            catch(UnknownHostException uhe)
            {
                System.out.println(uhe);
            }
            catch(IOException ioe)
            {
                System.out.println(ioe);
            }
        }

        private final char HEX_DIGITS[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'A', 'B', 'C', 'D', 'E', 'F'
        };
        final int interval = 0x493e0;
        final String propertyFile = "License.properties";
        final String licenseHeader = "license.";
        final String licenseProductList = "license.product";
        final String licenseProduct = ".product";
        final String licenseModule = ".module";
        final String licenseVersion = ".version";
        final String licenseServer = ".server";
        final String licensePort = ".port";
        final String licenseType = ".type";
        final String licenseKey = ".key";
        final String requestLicense = "requestLicense";
        final String maintainLicense = "maintainLicense";
        final String releaseLicense = "releaseLicense";
        Properties prop;

        public Licenser()
        {
            prop = null;
            System.out.print("License ");
            prop = new Properties();
            FileInputStream tempStream = null;
            String tempString = null;
            try
            {
                tempStream = new FileInputStream("conf/License.properties");
                prop.load(tempStream);
                for(Enumeration enum = prop.keys(); enum.hasMoreElements(); licenses.put(tempString, prop.getProperty(tempString)))
                    tempString = (String)enum.nextElement();

                tempStream.close();
                tempStream = null;
            }
            catch(FileNotFoundException fnfe)
            {
                System.out.println(fnfe);
            }
            catch(IOException ioe)
            {
                System.out.println(ioe);
            }
            tempString = prop.getProperty("license.product");
            String tempString2 = null;
            int i = 0;
            int j = 0;
            int stringSize = 0;
            if(tempString != null)
                stringSize = tempString.length();
            for(; i < stringSize; i = j + 1)
            {
                j = tempString.indexOf(',', i);
                if(j < 0)
                    j = stringSize;
                System.out.print("[" + tempString.substring(i, j) + "] ");
                licenses.put(tempString.substring(i, j), "L");
            }

        }
    }


    public Client()
    {
        licenses = new HashMap();
        daemonLock = null;
        threadPool = null;
        lookup = null;
        requester = null;
        dispatcher = null;
        sessionManager = null;
        commandAgent = null;
        licenser = null;
        String tempString = null;
        if(client != null)
            return;
        client = this;
        System.out.println();
        System.out.println("coMoad Client v.1.0.0 (Enterprise Edition)");
        System.out.println("Copyright (c) 2004, EESIN Information Technology Ltd."); 
        System.out.println();
        System.out.println("Start initialization.....");
        ACCESS_IDENTIFIER = System.currentTimeMillis();
        parms = XMLUtil.parse("client");
        if(parms == null)
        {
            System.err.println("[ERROR] Invalid configuration file.");
            System.exit(-1);
        }
        NUMBER_OF_THREAD_POOL = Integer.parseInt((String)parms.get("share.threadpool.number"));
        tempString = (String)parms.get("iip.sb.inet.address");
        if(!Utils.isNullString(tempString))
            try
            {
                sbInetAddress = InetAddress.getByName(tempString);
            }
            catch(UnknownHostException e)
            {
                System.err.println(e);
                sbInetAddress = null;
            }
        else
            sbInetAddress = null;
        tempString = (String)parms.get("iip.sb.inet.port");
        if(Utils.isNullString(tempString))
            sbPort = 0;
        else
            sbPort = Integer.parseInt(tempString);
        System.out.print("Thread Pool");
        if(NUMBER_OF_THREAD_POOL > 0)
        {
            System.out.print(" (Cache size: " + NUMBER_OF_THREAD_POOL + ")");
            threadPool = new ThreadPool(NUMBER_OF_THREAD_POOL);
        } else
        {
            System.out.print(" (Laziness mode)");
            threadPool = new ThreadPool(0);
        }
        System.out.println(" [ok]");
        System.out.print("Server Look-up Manager");
        lookup = new Lookup();
        lookup.setAccessIdentifier(ACCESS_IDENTIFIER);
        lookup.init();
        isLookupEnabled = true;
        System.out.print("Intelligent Service Requester");
        requester = new Requester();
        requester.setAccessIdentifier(ACCESS_IDENTIFIER);
        requester.init();
        isRequesterEnabled = true;
        System.out.print("Dynamic Request Dispatcher");
        dispatcher = new RequestDispatcher();
        dispatcher.setAccessIdentifier(ACCESS_IDENTIFIER);
        dispatcher.init();
        isDispatcherEnabled = true;
        System.out.print("Local Session Manager");
        sessionManager = new SessionManager();
        sessionManager.setAccessIdentifier(ACCESS_IDENTIFIER);
        sessionManager.init();
        isLocalSessionEnabled = true;
        System.out.print("Command Invocation Agent");
        commandAgent = new CommandAgent();
        commandAgent.setAccessIdentifier(ACCESS_IDENTIFIER);
        commandAgent.init();
        licenser = new Licenser();
        licenser.start();
        daemonLock = new DaemonLock();
        daemonLock.acquire();
        locale = (String)parms.get("locale");
        System.out.println();
        System.out.println("Initialization complete.");
    }

    public synchronized ThreadPool getThreadPool(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return threadPool;
        else
            return null;
    }

    public String getProperty(long accessIdentifier, String propertyName)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER && parms != null)
            return (String)parms.get(propertyName);
        else
            return null;
    }

    public LookupRecord lookupService(long accessIdentifier, String serviceId)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER && !Utils.isNullString(serviceId))
            return lookup.lookupService(serviceId);
        else
            return null;
    }

    public void removeLookupRecord(long accessIdentifier, String serviceId)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER && !Utils.isNullString(serviceId))
            lookup.removeLookupRecord(serviceId);
    }

    public Requester getRequester(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return requester;
        else
            return null;
    }

    public Service getServiceInstance(long accessIdentifier, String serviceId)
        throws ServiceNotFoundException
    {
        if(accessIdentifier == ACCESS_IDENTIFIER && !Utils.isNullString(serviceId))
            return dispatcher.getServiceInstance(serviceId);
        else
            return null;
    }

    public Service getServiceInstance(String serviceId)
        throws ServiceNotFoundException
    {
        if(isDispatcherEnabled && isLookupEnabled && isLookupEnabled)
            return dispatcher.getServiceInstance(serviceId);
        else
            return null;
    }

    public SessionManager getSessionManager(long accessIdentifier)
    {
        if(accessIdentifier == ACCESS_IDENTIFIER)
            return sessionManager;
        else
            return null;
    }

    public boolean getLicense(String product)
    {
        String tempString = (String)licenses.get(product);
        if(tempString != null && tempString.equals("L"))
        {
            licenses.put(product, "I");
            licenser.maintain(product);
            tempString = (String)licenses.get(product);
            if(tempString != null && tempString.equals("T"))
                return true;
        } else
        if(tempString != null && tempString.equals("T"))
            return true;
        return false;
    }

    public void releaseLicense(String product)
    {
        String tempString = (String)licenses.get(product);
        if(tempString != null && tempString.equals("T"))
        {
            licenses.put(product, "R");
            licenser.maintain(product);
        }
    }

    public static void main(String args[])
    {
        new Client();
    }

    HashMap licenses;
    public static Client client = null;
    private DaemonLock daemonLock;
    private ThreadPool threadPool;
    private Lookup lookup;
    private Requester requester;
    private RequestDispatcher dispatcher;
    private SessionManager sessionManager;
    private CommandAgent commandAgent;
    private Licenser licenser;
    private static HashMap parms = null;
    private static int NUMBER_OF_THREAD_POOL = 16;
    private static long ACCESS_IDENTIFIER = 0L;
    private static boolean isLookupEnabled = false;
    private static boolean isRequesterEnabled = false;
    private static boolean isDispatcherEnabled = false;
    private static boolean isLocalSessionEnabled = false;
    public static InetAddress sbInetAddress = null;
    public static int sbPort = 0;
    public static String locale = null;

}