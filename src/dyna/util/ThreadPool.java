// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ThreadPool.java

package dyna.util;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

// Referenced classes of package dyna.util:
//            BusyFlag, CondVar

public class ThreadPool
{
    class ThreadPoolRequest
    {

        Runnable target;
        Object lock;

        ThreadPoolRequest(Runnable t, Object l)
        {
            target = t;
            lock = l;
        }
    }

    class ThreadPoolThread extends Thread
    {

        public void run()
        {
_L3:
            parent.cvFlag.getBusyFlag();
            while(obj == null && shouldRun) 
            {
                try
                {
                    obj = (ThreadPoolRequest)parent.objects.getFirst();
                    parent.objects.removeFirst();
                }
                catch(NoSuchElementException nsee)
                {
                    obj = null;
                }
                catch(ClassCastException cce)
                {
                    System.err.println("Unexpected data");
                    obj = null;
                }
                if(obj == null)
                    try
                    {
                        parent.cvAvailable.cvWait();
                    }
                    catch(InterruptedException ie)
                    {
                        return;
                    }
            }
              goto _L1
            local1;
            parent.cvFlag.freeBusyFlag();
            JVM INSTR ret 2;
_L1:
            if(!shouldRun)
                return;
            obj.target.run();
            try
            {
                parent.cvFlag.getBusyFlag();
                nObjects--;
                if(nObjects == 0)
                    parent.cvEmpty.cvSignal();
            }
            finally
            {
                parent.cvFlag.freeBusyFlag();
            }
            if(obj.lock != null)
                synchronized(obj.lock)
                {
                    obj.lock.notify();
                }
            obj = null;
            if(shouldRun) goto _L3; else goto _L2
_L2:
        }

        ThreadPool parent;
        volatile boolean shouldRun;
        ThreadPoolRequest obj;

        ThreadPoolThread(ThreadPool parent, int i)
        {
            super("ThreadPoolThread " + i);
            shouldRun = true;
            obj = null;
            this.parent = parent;
        }
    }


    public ThreadPool(int n)
    {
        nObjects = 0;
        terminated = false;
        cvFlag = new BusyFlag();
        cvAvailable = new CondVar(cvFlag);
        cvEmpty = new CondVar(cvFlag);
        objects = new LinkedList();
        poolThreads = new ThreadPoolThread[n];
        for(int i = 0; i < n; i++)
        {
            poolThreads[i] = new ThreadPoolThread(this, i);
            poolThreads[i].start();
        }

    }

    private void add(Runnable target, Object lock)
    {
        try
        {
            cvFlag.getBusyFlag();
            if(terminated)
                throw new IllegalStateException("Thread pool has shut down");
            objects.addLast(new ThreadPoolRequest(target, lock));
            nObjects++;
            cvAvailable.cvSignal();
        }
        finally
        {
            cvFlag.freeBusyFlag();
        }
        return;
    }

    public void addRequest(Runnable target)
    {
        add(target, null);
    }

    public void addRequestAndWait(Runnable target)
        throws InterruptedException
    {
        Object lock = new Object();
        synchronized(lock)
        {
            add(target, lock);
            lock.wait();
        }
    }

    public void waitForAll(boolean terminate)
        throws InterruptedException
    {
        try
        {
            cvFlag.getBusyFlag();
            while(nObjects != 0) 
                cvEmpty.cvWait();
            if(terminate)
            {
                for(int i = 0; i < poolThreads.length; i++)
                    poolThreads[i].shouldRun = false;

                cvAvailable.cvBroadcast();
                terminated = true;
            }
        }
        finally
        {
            cvFlag.freeBusyFlag();
        }
        return;
    }

    public void waitForAll()
        throws InterruptedException
    {
        waitForAll(false);
    }

    LinkedList objects;
    int nObjects;
    CondVar cvAvailable;
    CondVar cvEmpty;
    BusyFlag cvFlag;
    ThreadPoolThread poolThreads[];
    boolean terminated;
}
