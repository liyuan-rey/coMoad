// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IDS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;

public interface IDS
    extends Service
{

    public abstract void ping();

    public abstract ArrayList getUpdateFileList()
        throws IIPRequestException;
}
