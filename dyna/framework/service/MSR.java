// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MSR.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import dyna.framework.service.dos.DOSChangeable;
import java.util.ArrayList;
import java.util.HashMap;

public interface MSR
    extends Service
{

    public abstract void ping();

    public abstract String createStgRep(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeStgrep(String s, String s1)
        throws IIPRequestException;

    public abstract DOSChangeable getStgrep(String s, String s1)
        throws IIPRequestException;

    public abstract void setStgrep(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList listStgrep(String s)
        throws IIPRequestException;

    public abstract HashMap getAllStgrep(String s)
        throws IIPRequestException;

    public abstract ArrayList listAllStgrep()
        throws IIPRequestException;

    public abstract void importStgrep(String s)
        throws IIPRequestException;

    public abstract void setCurrentLocale(String s)
        throws IIPRequestException;

    public abstract String getStgrepString(String s, String s1)
        throws IIPRequestException;

    public abstract String getStgrepString(String s, String s1, String s2)
        throws IIPRequestException;
}
