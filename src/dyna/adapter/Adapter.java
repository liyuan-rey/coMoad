// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:30
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Adapter.java

package dyna.adapter;

import dyna.util.*;
import java.io.PrintStream;

public class Adapter
{

    public Adapter()
    {
        sessionManager = null;
    }

    public void init(SessionManager sessionManager, String serviceName, String serviceDescription)
    {
        System.out.print(serviceDescription);
        this.sessionManager = sessionManager;
        CommandInterpreter.addServiceObject(serviceName, this);
        System.out.print(" [initiated]");
    }

    public Session getSession(String identifier)
    {
        return (Session)sessionManager.getSession(identifier);
    }

    private SessionManager sessionManager;
}