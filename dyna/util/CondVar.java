// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CondVar.java

package dyna.util;


// Referenced classes of package dyna.util:
//            BusyFlag

public class CondVar
{

    public CondVar()
    {
        this(new BusyFlag());
    }

    public CondVar(BusyFlag sv)
    {
        SyncVar = sv;
    }

    public void cvWait()
        throws InterruptedException
    {
        cvTimedWait(SyncVar, 0);
    }

    public void cvWait(BusyFlag sv)
        throws InterruptedException
    {
        cvTimedWait(sv, 0);
    }

    public void cvTimedWait(int millis)
        throws InterruptedException
    {
        cvTimedWait(SyncVar, millis);
    }

    public void cvTimedWait(BusyFlag sv, int millis)
        throws InterruptedException
    {
        int i = 0;
        InterruptedException errex = null;
        synchronized(this)
        {
            if(sv.getBusyFlagOwner() != Thread.currentThread())
                throw new IllegalMonitorStateException("current thread not owner");
            for(; sv.getBusyFlagOwner() == Thread.currentThread(); sv.freeBusyFlag())
                i++;

            try
            {
                if(millis == 0)
                    wait();
                else
                    wait(millis);
            }
            catch(InterruptedException iex)
            {
                errex = iex;
            }
        }
        for(; i > 0; i--)
            sv.getBusyFlag();

        if(errex != null)
            throw errex;
        else
            return;
    }

    public void cvSignal()
    {
        cvSignal(SyncVar);
    }

    public synchronized void cvSignal(BusyFlag sv)
    {
        if(sv.getBusyFlagOwner() != Thread.currentThread())
        {
            throw new IllegalMonitorStateException("current thread not owner");
        } else
        {
            notify();
            return;
        }
    }

    public void cvBroadcast()
    {
        cvBroadcast(SyncVar);
    }

    public synchronized void cvBroadcast(BusyFlag sv)
    {
        if(sv.getBusyFlagOwner() != Thread.currentThread())
        {
            throw new IllegalMonitorStateException("current thread not owner");
        } else
        {
            notifyAll();
            return;
        }
    }

    private BusyFlag SyncVar;
}
