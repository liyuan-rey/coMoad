// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OLM.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import java.util.HashMap;

public interface OLM
    extends Service
{

    public abstract void ping();

    public abstract boolean create(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean remove(String s)
        throws IIPRequestException;

    public abstract boolean remove(String s, String s1)
        throws IIPRequestException;

    public abstract HashMap get(String s, String s1)
        throws IIPRequestException;

    public abstract HashMap get(String s)
        throws IIPRequestException;

    public abstract boolean set(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList list(String s)
        throws IIPRequestException;

    public abstract ArrayList list()
        throws IIPRequestException;

    public abstract ArrayList listClass()
        throws IIPRequestException;

    public abstract boolean link(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean unlink(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listLink(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList listLink(String s)
        throws IIPRequestException;
}
