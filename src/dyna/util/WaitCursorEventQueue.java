// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WaitCursorEventQueue.java

package dyna.util;

import java.awt.*;
import javax.swing.SwingUtilities;

public class WaitCursorEventQueue extends EventQueue
{
    private class WaitCursorTimer extends Thread
    {

        synchronized void startTimer(Object source)
        {
            this.source = source;
            notify();
        }

        synchronized void stopTimer()
        {
            if(parent == null)
            {
                interrupt();
            } else
            {
                parent.setCursor(null);
                parent = null;
            }
        }

        public synchronized void run()
        {
            do
                try
                {
                    do
                    {
                        wait();
                        wait(delay);
                        if(source instanceof Component)
                            parent = SwingUtilities.getRoot((Component)source);
                        else
                        if(source instanceof MenuComponent)
                        {
                            java.awt.MenuContainer mParent = ((MenuComponent)source).getParent();
                            if(mParent instanceof Component)
                                parent = SwingUtilities.getRoot((Component)mParent);
                        }
                    } while(parent == null || !parent.isShowing());
                    parent.setCursor(Cursor.getPredefinedCursor(3));
                }
                catch(InterruptedException interruptedexception) { }
            while(true);
        }

        private Object source;
        private Component parent;

        WaitCursorTimer()
        {
        }
    }


    public WaitCursorEventQueue(int delay)
    {
        this.delay = delay;
        waitTimer = new WaitCursorTimer();
        waitTimer.setDaemon(true);
        waitTimer.start();
    }

    protected void dispatchEvent(AWTEvent event)
    {
        waitTimer.startTimer(event.getSource());
        try
        {
            super.dispatchEvent(event);
        }
        finally
        {
            waitTimer.stopTimer();
        }
        return;
    }

    private int delay;
    private WaitCursorTimer waitTimer;

}
