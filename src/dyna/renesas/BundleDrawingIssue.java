// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BundleDrawingIssue.java

package dyna.renesas;

import dyna.framework.Server;
import dyna.framework.service.*;
import java.io.PrintStream;
import java.util.Hashtable;

public class BundleDrawingIssue
{

    public BundleDrawingIssue(DOS dos, DSS dss, NDS nds)
    {
        dos_ = dos;
        dss_ = dss;
        nds_ = nds;
    }

    public boolean out(String orderNumber, String orderFileName, boolean flag)
    {
        System.out.println("flag:" + flag);
        return true;
    }

    public boolean out(String drawingNo, String orderNumber, Hashtable sheetSize)
    {
        drawingNo = drawingNo.trim();
        try
        {
            nds_ = (NDS)Server.getServiceInstance("DF30NDS1");
            System.out.println(nds_ + ":");
        }
        catch(Exception e)
        {
            System.out.println("StatusChange : EC ouid get error");
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    public boolean out(String orderNumber, String orderFileName)
    {
        System.out.println("---> Start Method 3");
        System.out.println("orderNumber:" + orderNumber);
        System.out.println("orderFileName:" + orderFileName);
        return true;
    }

    private DOS dos_;
    private DSS dss_;
    private NDS nds_;
}
