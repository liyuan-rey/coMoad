// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PCS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;

public interface PCS
    extends Service
{

    public abstract void ping();

    public abstract ArrayList getBackFileList()
        throws IIPRequestException;

    public abstract void fileCheck(Socket socket, BufferedReader bufferedreader)
        throws IIPRequestException;
}
