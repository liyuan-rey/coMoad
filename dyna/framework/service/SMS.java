// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SMS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import java.util.HashMap;

public interface SMS
    extends Service
{

    public abstract void ping();

    public abstract void shutdown()
        throws IIPRequestException;

    public abstract HashMap getAllConnectionInformation()
        throws IIPRequestException;

    public abstract HashMap getConnectionInformationByUser(String s)
        throws IIPRequestException;

    public abstract ArrayList getServiceInformation()
        throws IIPRequestException;
}
