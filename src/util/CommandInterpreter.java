// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CommandInterpreter.java

package dyna.util;

import dyna.framework.Client;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

// Referenced classes of package dyna.util:
//            SessionManager, Session, Utils

public class CommandInterpreter extends Thread
{

    private static synchronized void hiddenInitializer()
    {
        _coreCommands.put("Session.createSession", new Integer(1));
        _coreCommands.put("Session.removeSession", new Integer(1));
        _coreCommands.put("Session.openSession", new Integer(1));
        _coreCommands.put("Session.closeSession", new Integer(1));
        _coreCommands.put("Session.clearSession", new Integer(1));
        _coreCommands.put("Session.getIdentifier", new Integer(0));
        _coreCommands.put("Session.setIdentifier", new Integer(1));
        _coreCommands.put("Session.setService", new Integer(1));
        _coreCommands.put("Session.getService", new Integer(0));
        _coreCommands.put("Session.getProperty", new Integer(1));
        _coreCommands.put("Session.setProperty", new Integer(2));
        _coreCommands.put("Session.getQueuedProperty", new Integer(1));
        _coreCommands.put("Session.dequeueQueuedProperty", new Integer(1));
        _coreCommands.put("Session.enqueueQueuedProperty", new Integer(2));
    }

    public static synchronized void addServiceObject(String serviceName, Object serviceObject)
    {
        _serviceTable.put(serviceName, serviceObject);
    }

    public CommandInterpreter(Socket socket, long accessIdentifier)
    {
        _streamTokenizer = null;
        _streamSourceType = -1;
        _streamSource = null;
        token = 0;
        _isStreamAvailable = false;
        _sessionId = null;
        _bufferedReader = null;
        _bufferedWriter = null;
        sessionManager = null;
        sessionManager = Client.client.getSessionManager(accessIdentifier);
        _streamSourceType = 1;
        _streamSource = socket;
        InetAddress inetAddress = socket.getInetAddress();
        int port = socket.getPort();
        try
        {
            _bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            _bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            _bufferedWriter.write("// Connected from '" + inetAddress.getHostAddress() + ":" + port + "'.");
            _bufferedWriter.newLine();
            _isStreamAvailable = true;
        }
        catch(IOException e)
        {
            System.err.println("CommandInterpreter: " + e.getLocalizedMessage());
            try
            {
                _bufferedReader.close();
                _bufferedReader = null;
            }
            catch(IOException ioexception) { }
            try
            {
                _bufferedWriter.close();
                _bufferedWriter = null;
            }
            catch(IOException ioexception1) { }
            try
            {
                ((Socket)_streamSource).close();
            }
            catch(IOException ioexception2) { }
        }
    }

    public synchronized String createSession(String sessionId)
    {
        String returnValue = sessionManager.createSession(sessionId);
        if(returnValue == null)
            return "-ERR Session not created.";
        else
            return "+OK New session created.\r\n" + returnValue + "\r\n.";
    }

    public synchronized String removeSession(String sessionId)
    {
        if(sessionManager.getSession(sessionId) == null)
        {
            return "-ERR Sessio not found.";
        } else
        {
            sessionManager.removeSession(sessionId);
            return "+OK Session removed.";
        }
    }

    public synchronized String openSession(String sessionId)
    {
        Session session = (Session)sessionManager.getSession(sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
        {
            session.setProperty("current.status", "open");
            _sessionId = sessionId;
        } else
        if(session.getProperty("current.status").equals("open"))
        {
            _sessionId = sessionId;
            return "+OK Session '" + sessionId + "' re-opened.";
        }
        return "+OK Session '" + sessionId + "' opened.";
    }

    public synchronized String closeSession(String sessionId)
    {
        Session session = (Session)sessionManager.getSession(sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("open"))
        {
            session.setProperty("current.status", new String("close"));
            _sessionId = sessionId;
        } else
        if(session.getProperty("current.status").equals("close"))
            return "-ERR Session already closed.";
        return "+OK Session '" + sessionId + "' closed.";
    }

    public synchronized String clearSession(String sessionId)
    {
        return "+ERR Not implemented.";
    }

    public synchronized String getIdentifier()
    {
        if(_sessionId == null)
            return "-ERR Session not opend.";
        else
            return "+OK\r\n" + _sessionId + "\r\n.";
    }

    public synchronized String setIdentifier(String sessionId)
    {
        return "+ERR Not implemented.";
    }

    public synchronized String getService()
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("closed"))
            return "-ERR Session not opend.";
        if(session.getProperty("current.service") == null)
            return "-ERR Service not specified.";
        else
            return "+OK\r\n" + session.getProperty("current.service") + "\r\n.";
    }

    public synchronized String setService(String serviceName)
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("close"))
            return "-ERR Session not opend.";
        if(_serviceTable.get(serviceName) == null)
        {
            return "-ERR Service not found.";
        } else
        {
            session.setProperty("current.service", new String(serviceName));
            return "+OK Current service changed to '" + serviceName + "'.";
        }
    }

    public synchronized String getProperty(String propertyName)
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("closed"))
            return "-ERR Session not opend.";
        if(session.getProperty(propertyName) == null)
            return "-ERR Property not found.";
        else
            return "+OK\r\n" + session.getProperty(propertyName) + "\r\n.";
    }

    public synchronized String setProperty(String propertyName, String value)
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("close"))
        {
            return "-ERR Session not opend.";
        } else
        {
            session.setProperty(propertyName, new String(value));
            return "+OK Property " + propertyName + "'s value changed to '" + value + "'.";
        }
    }

    public synchronized String getQueuedProperty(String propertyName)
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("closed"))
            return "-ERR Session not opend.";
        if(session.getQueuedProperty(propertyName) == null)
            return "-ERR QueuedProperty not found.";
        else
            return "+OK\r\n" + session.getQueuedProperty(propertyName) + "\r\n.";
    }

    public synchronized String dequeueQueuedProperty(String propertyName)
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("closed"))
            return "-ERR Session not opend.";
        if(session.getQueuedProperty(propertyName) == null)
            return "-ERR QueuedProperty not found.";
        else
            return "+OK\r\n" + session.dequeueQueuedProperty(propertyName) + "\r\n.";
    }

    public synchronized String enqueueQueuedProperty(String propertyName, String value)
    {
        if(_sessionId == null)
            return "-ERR Session not opened.";
        Session session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
            return "-ERR Session not found.";
        if(session.getProperty("current.status") == null)
            return "-ERR Session not opend.";
        if(session.getProperty("current.status").equals("close"))
        {
            return "-ERR Session not opend.";
        } else
        {
            session.enqueueQueuedProperty(propertyName, new String(value));
            return "+OK QueuedProperty " + propertyName + "'s value '" + value + "' enqueued.";
        }
    }

    public void run()
    {
        String commandString;
        ArrayList commandArguments;
        String inputString = null;
        commandString = null;
        commandArguments = new ArrayList();
        if(!_isStreamAvailable)
            return;
        _bufferedWriter.write("// DynaMOAD v.1.0.0 - Command invocation agent");
        _bufferedWriter.newLine();
        _bufferedWriter.write("// (C) Copyright Innovative PLM Solutions, LTD. 2003.");
        _bufferedWriter.newLine();
        _bufferedWriter.flush();
        _streamTokenizer = new StreamTokenizer(_bufferedReader);
        _streamTokenizer.resetSyntax();
        _streamTokenizer.lowerCaseMode(false);
        _streamTokenizer.slashSlashComments(true);
        _streamTokenizer.whitespaceChars(0, 31);
        _streamTokenizer.wordChars(32, 58);
        _streamTokenizer.wordChars(60, 126);
        _streamTokenizer.wordChars(128, 65535);
        _bufferedWriter.write("+OK - Welcome.");
        _bufferedWriter.newLine();
        _bufferedWriter.flush();
          goto _L1
_L10:
        token = _streamTokenizer.nextToken();
        token;
        JVM INSTR tableswitch -3 -1: default 896
    //                   -3 220
    //                   -2 896
    //                   -1 212;
           goto _L2 _L3 _L2 _L4
_L2:
        break; /* Loop/switch isn't completed */
_L4:
        _isStreamAvailable = false;
        break; /* Loop/switch isn't completed */
_L3:
        String inputString = _streamTokenizer.sval;
        if(inputString.equals("exit"))
        {
            _isStreamAvailable = false;
            break; /* Loop/switch isn't completed */
        }
        commandString = inputString;
        if(_coreCommands.containsKey(inputString))
        {
            for(int i = ((Integer)_coreCommands.get(inputString)).intValue(); i >= 0; i--)
            {
                token = _streamTokenizer.nextToken();
                if(i == 0)
                {
                    if((char)token == ';')
                    {
                        String returnValue = (String)Utils.invokeMethod(this, commandString.substring(8), commandArguments);
                        if(returnValue == null)
                            returnValue = "-ERR Error occured during command invocation.";
                        _bufferedWriter.write(returnValue);
                        _bufferedWriter.newLine();
                        _bufferedWriter.flush();
                        commandString = null;
                        commandArguments.clear();
                    } else
                    {
                        commandString = null;
                        commandArguments.clear();
                        _bufferedWriter.write("-ERR Too many arguments");
                        _bufferedWriter.newLine();
                        _bufferedWriter.flush();
                    }
                    break; /* Loop/switch isn't completed */
                }
                if((char)token == ';')
                {
                    commandString = null;
                    commandArguments.clear();
                    _bufferedWriter.write("-ERR Need " + i + " more argument" + (i != 1 ? "s." : "."));
                    _bufferedWriter.newLine();
                    _bufferedWriter.flush();
                }
                switch(token)
                {
                case -3: 
                    commandArguments.add(new String(_streamTokenizer.sval));
                    break;

                case -2: 
                    commandArguments.add(String.valueOf(_streamTokenizer.nval));
                    break;
                }
            }

            break; /* Loop/switch isn't completed */
        }
        if(_sessionId == null) goto _L6; else goto _L5
_L5:
        Session session;
        session = (Session)sessionManager.getSession(_sessionId);
        if(session == null)
        {
            commandString = null;
            commandArguments.clear();
            _bufferedWriter.write("-ERR Session not found.");
            _bufferedWriter.newLine();
            _bufferedWriter.flush();
            break; /* Loop/switch isn't completed */
        }
        if(session.getProperty("current.service") == null)
        {
            commandString = null;
            commandArguments.clear();
            _bufferedWriter.write("-ERR Service not specified.");
            _bufferedWriter.newLine();
            _bufferedWriter.flush();
            break; /* Loop/switch isn't completed */
        }
_L8:
        token = _streamTokenizer.nextToken();
        if((char)token == ';')
        {
            String returnValue = null;
            Object returnObject = Utils.invokeMethod(_serviceTable.get(session.getProperty("current.service")), commandString, commandArguments);
            if(returnObject == null)
                returnValue = "-ERR Error occured during command invocation.";
            else
            if(returnObject instanceof String)
                returnValue = (String)returnObject;
            else
                returnValue = "-ERR " + returnObject.toString();
            _bufferedWriter.write(returnValue);
            _bufferedWriter.newLine();
            _bufferedWriter.flush();
            commandString = null;
            commandArguments.clear();
            break; /* Loop/switch isn't completed */
        }
        switch(token)
        {
        case -3: 
            commandArguments.add(new String(_streamTokenizer.sval));
            break;

        case -2: 
            commandArguments.add(String.valueOf(_streamTokenizer.nval));
            break;
        }
        continue; /* Loop/switch isn't completed */
_L6:
        commandString = null;
        commandArguments.clear();
        _bufferedWriter.write("-ERR Command not found.");
        _bufferedWriter.newLine();
        _bufferedWriter.flush();
        break; /* Loop/switch isn't completed */
        if(true) goto _L8; else goto _L7
_L7:
_L1:
        if(_isStreamAvailable) goto _L10; else goto _L9
_L9:
        _bufferedWriter.write("+OK Goodbye.");
        _bufferedWriter.newLine();
        _bufferedWriter.flush();
        break MISSING_BLOCK_LABEL_1079;
        IOException e;
        e;
        System.err.println("CommandInterpreter.run(): " + e.getLocalizedMessage());
        break MISSING_BLOCK_LABEL_1079;
        e;
        System.err.println(e);
        break MISSING_BLOCK_LABEL_1079;
        local;
        try
        {
            _bufferedReader.close();
            _bufferedReader = null;
        }
        catch(IOException ioexception) { }
        try
        {
            _bufferedWriter.close();
            _bufferedWriter = null;
        }
        catch(IOException ioexception1) { }
        if(_streamSource != null)
            switch(_streamSourceType)
            {
            case 1: // '\001'
                try
                {
                    ((Socket)_streamSource).close();
                }
                catch(IOException ioexception2) { }
                break;
            }
        JVM INSTR ret 7;
        return;
    }

    private static Hashtable _coreCommands = null;
    private static Hashtable _serviceTable = null;
    public static final int SOURCE_SOCKET = 1;
    public static final int SOURCE_STRING = 2;
    public static final int SOURCE_FILE = 3;
    private StreamTokenizer _streamTokenizer;
    private int _streamSourceType;
    private Object _streamSource;
    private int token;
    private boolean _isStreamAvailable;
    private String _sessionId;
    private BufferedReader _bufferedReader;
    private BufferedWriter _bufferedWriter;
    private SessionManager sessionManager;

    static 
    {
        _coreCommands = new Hashtable();
        _serviceTable = new Hashtable();
        hiddenInitializer();
    }
}
