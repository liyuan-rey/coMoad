// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Shutdown.java

package dyna.util;

import dyna.framework.Client;
import dyna.framework.service.SMS;
import java.io.PrintStream;

public class Shutdown
{

    public Shutdown()
    {
    }

    public static void main(String args[])
    {
        Client client = null;
        SMS sms = null;
        try
        {
            client = new Client();
            sms = (SMS)Client.client.getServiceInstance("DF30SMS1");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        System.err.println();
        System.err.println("[SMS] Shutdown process initialized.");
        try
        {
            sms.shutdown();
        }
        catch(Exception exception) { }
        System.err.println("[SMS] Shutdown process finished. Thanks.");
        System.exit(0);
    }
}
