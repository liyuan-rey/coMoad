// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CommandAgent.java

package dyna.util;

import dyna.adapter.Adapter;
import dyna.framework.Client;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.*;

// Referenced classes of package dyna.util:
//            XMLUtil, Utils, CommandInterpreter

public class CommandAgent extends Thread
{

    public CommandAgent()
    {
        _serverSocket = null;
        _socket = null;
        _isOnService = true;
        accessIdentifier = 0L;
        adapterParam = null;
        adapterList = null;
    }

    public synchronized void setAccessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        try
        {
            _serverSocket = new ServerSocket(5558);
        }
        catch(ConnectException e)
        {
            System.out.println(e);
        }
        catch(BindException e)
        {
            System.out.println(e);
        }
        catch(SocketException e)
        {
            System.out.println(e);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        start();
        System.out.println(" [initiated]");
        loadAdapter();
    }

    public synchronized void setOnService(boolean isOnService)
    {
        _isOnService = isOnService;
    }

    public synchronized boolean getOnService()
    {
        return _isOnService;
    }

    private void loadAdapter()
    {
        adapterParam = XMLUtil.parse("adapter");
        if(adapterParam == null)
        {
            System.err.println("  [ERROR] Invalid adapter configuration file.");
            System.exit(-1);
        }
        System.out.println("Load Interface Adapter...");
        adapterList = (LinkedList)Utils.tokenizeMessage((String)adapterParam.get("adapter-list"), ' ');
        String serviceElement = null;
        for(Iterator iterator = adapterList.iterator(); iterator.hasNext();)
        {
            serviceElement = (String)iterator.next();
            if(!Utils.isNullString(serviceElement))
                try
                {
                    Class serviceClass = Class.forName((String)adapterParam.get(serviceElement + ".class"));
                    Object adapter = serviceClass.newInstance();
                    if(!(adapter instanceof Adapter))
                        throw new ClassCastException("A Service class must extends class, 'dyna.adapter.Adapter'.");
                    System.out.print("  ");
                    ((Adapter)adapter).init(Client.client.getSessionManager(accessIdentifier), (String)adapterParam.get(serviceElement + ".name"), (String)adapterParam.get(serviceElement + ".desc"));
                    System.out.println();
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
        }

    }

    public void run()
    {
        if(_serverSocket == null)
            return;
        while(_isOnService) 
        {
            _socket = _serverSocket.accept();
            (new CommandInterpreter(_socket, accessIdentifier)).start();
        }
        _serverSocket.close();
        break MISSING_BLOCK_LABEL_65;
        IOException e;
        e;
        System.out.println(e);
    }

    public static final int LOCAL_PORT = 5558;
    private ServerSocket _serverSocket;
    private Socket _socket;
    private boolean _isOnService;
    private long accessIdentifier;
    private HashMap adapterParam;
    private LinkedList adapterList;
}
