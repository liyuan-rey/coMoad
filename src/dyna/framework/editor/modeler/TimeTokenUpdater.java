// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeTokenUpdater.java

package dyna.framework.editor.modeler;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;

// Referenced classes of package dyna.framework.editor.modeler:
//            TimeTokenUpdate

public class TimeTokenUpdater
    implements Runnable
{

    public TimeTokenUpdater(DOS dos)
    {
        container = null;
        this.dos = null;
        this.dos = dos;
    }

    public void setTimeTokenContainer(TimeTokenUpdate container)
    {
        synchronized(this)
        {
            this.container = container;
        }
    }

    public synchronized void run()
    {
        String tempToken = null;
          goto _L1
_L3:
        try
        {
label0:
            {
                tempToken = dos.getTimeToken();
                synchronized(container.getClass())
                {
                    if(container != null)
                        break label0;
                }
                break; /* Loop/switch isn't completed */
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        break MISSING_BLOCK_LABEL_63;
        container.updateToken(tempToken);
        class1;
        JVM INSTR monitorexit ;
        break MISSING_BLOCK_LABEL_63;
        try
        {
            wait(0x493e0L);
        }
        catch(InterruptedException e1)
        {
            e1.printStackTrace();
        }
_L1:
        if(container != null && dos != null) goto _L3; else goto _L2
_L2:
    }

    private TimeTokenUpdate container;
    private DOS dos;
}
