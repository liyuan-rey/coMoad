// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SessionManager.java

package dyna.util;

import java.io.PrintStream;
import java.util.HashMap;

// Referenced classes of package dyna.util:
//            Session

public class SessionManager
{
    private class SessionSync extends Thread
    {

        public long getMillis()
        {
            return _millis;
        }

        public synchronized void setMillis(long millis)
        {
            _millis = millis;
        }

        public synchronized void sync()
        {
        }

        public void run()
        {
            do
                try
                {
                    Thread.sleep(_millis);
                    sync();
                }
                catch(InterruptedException interruptedexception)
                {
                    return;
                }
            while(true);
        }

        private long _millis;

        public SessionSync(long millis)
        {
            _millis = 0x927c0L;
            _millis = millis;
        }

        public SessionSync()
        {
            this(0x927c0L);
        }
    }


    public SessionManager()
    {
        _sessionPool = null;
        _sessionSync = null;
        accessIdentifier = 0L;
    }

    public synchronized void setAccessIdentifier(long accessIdentifier)
    {
        this.accessIdentifier = accessIdentifier;
    }

    public synchronized void init()
    {
        _sessionPool = new HashMap();
        _sessionSync = new SessionSync();
        _sessionSync.start();
        System.out.println(" [initiated]");
    }

    public synchronized Object getSession(String identifier)
    {
        return _sessionPool.get(identifier);
    }

    public synchronized String createSession(String identifier)
    {
        String sessionId = identifier;
        Session session = (Session)_sessionPool.get(identifier);
        if(session != null)
            sessionId = identifier + '@' + Long.toHexString(System.currentTimeMillis());
        _sessionPool.put(sessionId, new Session(sessionId));
        return sessionId;
    }

    public synchronized void removeSession(String identifier)
    {
        if(_sessionPool.get(identifier) == null)
        {
            return;
        } else
        {
            _sessionPool.remove(identifier);
            return;
        }
    }

    public synchronized void sync()
    {
        _sessionSync.sync();
    }

    private HashMap _sessionPool;
    private SessionSync _sessionSync;
    private long accessIdentifier;
    private static final long MILLIS_DEFAULT = 0x927c0L;
}
