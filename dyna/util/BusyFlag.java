// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BusyFlag.java

package dyna.util;


public class BusyFlag
{

    public BusyFlag()
    {
        busyflag = null;
        busycount = 0;
    }

    public synchronized void getBusyFlag()
    {
        while(!tryGetBusyFlag()) 
            try
            {
                wait();
            }
            catch(Exception exception) { }
    }

    public synchronized boolean tryGetBusyFlag()
    {
        if(busyflag == null)
        {
            busyflag = Thread.currentThread();
            busycount = 1;
            return true;
        }
        if(busyflag == Thread.currentThread())
        {
            busycount++;
            return true;
        } else
        {
            return false;
        }
    }

    public synchronized void freeBusyFlag()
    {
        if(getBusyFlagOwner() == Thread.currentThread())
        {
            busycount--;
            if(busycount == 0)
            {
                busyflag = null;
                notify();
            }
        }
    }

    public synchronized Thread getBusyFlagOwner()
    {
        return busyflag;
    }

    protected Thread busyflag;
    protected int busycount;
}
