// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Session.java

package dyna.util;

import java.util.HashMap;
import java.util.LinkedList;

public class Session
{

    public Session(String identifier)
    {
        _identifier = null;
        _properties = null;
        _queuedProperties = null;
        _identifier = identifier;
        _properties = new HashMap();
        _queuedProperties = new HashMap();
    }

    public String getIdentifier()
    {
        return _identifier;
    }

    public synchronized void setIdentifier(String identifier)
    {
        _identifier = identifier;
    }

    public Object getProperty(String name)
    {
        return _properties.get(name);
    }

    public void setProperty(String name, Object property)
    {
        if(property == null)
            _properties.remove(name);
        else
            _properties.put(name, property);
    }

    public void removeProperty(String name)
    {
        _properties.remove(name);
    }

    public synchronized Object getQueuedProperty(String name)
    {
        LinkedList queuedProperty = (LinkedList)_queuedProperties.get(name);
        if(queuedProperty == null || queuedProperty.size() == 0)
            return null;
        else
            return queuedProperty.getFirst();
    }

    public synchronized Object dequeueQueuedProperty(String name)
    {
        Object returnValue = null;
        LinkedList queuedProperty = (LinkedList)_queuedProperties.get(name);
        if(queuedProperty == null || queuedProperty.size() == 0)
        {
            return null;
        } else
        {
            returnValue = queuedProperty.getFirst();
            queuedProperty.removeFirst();
            return returnValue;
        }
    }

    public synchronized void enqueueQueuedProperty(String name, Object property)
    {
        LinkedList queuedProperty = (LinkedList)_queuedProperties.get(name);
        if(queuedProperty == null)
        {
            queuedProperty = new LinkedList();
            _queuedProperties.put(name, queuedProperty);
        }
        queuedProperty.addLast(property);
    }

    private String _identifier;
    private HashMap _properties;
    private HashMap _queuedProperties;
}
