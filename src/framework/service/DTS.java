// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DTS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import java.util.HashMap;

public interface DTS
    extends Service
{

    public abstract boolean addRule(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeRule(String s)
        throws IIPRequestException;

    public abstract HashMap getRule(String s)
        throws IIPRequestException;

    public abstract boolean setRule(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listRule()
        throws IIPRequestException;

    public abstract ArrayList listLoadedRule()
        throws IIPRequestException;

    public abstract boolean loadRule(String s)
        throws IIPRequestException;

    public abstract boolean unloadRule(String s)
        throws IIPRequestException;

    public abstract boolean executeRule(String s)
        throws IIPRequestException;
}
