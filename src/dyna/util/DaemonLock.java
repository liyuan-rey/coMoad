// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DaemonLock.java

package dyna.util;


public class DaemonLock
    implements Runnable
{

    public DaemonLock()
    {
        lockCount = 0;
    }

    public synchronized void acquire()
    {
        if(lockCount++ == 0)
        {
            Thread t = new Thread(this);
            t.setDaemon(false);
            t.start();
        }
    }

    public synchronized void release()
    {
        if(--lockCount == 0)
            notify();
    }

    public synchronized void run()
    {
        while(lockCount != 0) 
            try
            {
                wait();
            }
            catch(InterruptedException interruptedexception) { }
    }

    private int lockCount;
}
