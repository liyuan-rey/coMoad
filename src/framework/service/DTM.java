// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DTM.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import javax.sql.PooledConnection;

public interface DTM
    extends Service
{

    public abstract void ping();

    public abstract PooledConnection getPooledConnection(String s)
        throws IIPRequestException;

    public abstract String beginTransaction()
        throws IIPRequestException;

    public abstract void endTransaction(String s)
        throws IIPRequestException;

    public abstract void commitTransaction(String s)
        throws IIPRequestException;

    public abstract void rollbackTransaction(String s)
        throws IIPRequestException;

    public abstract void cancelTransaction(String s)
        throws IIPRequestException;

    public abstract ArrayList listDatasourceId()
        throws IIPRequestException;

    public static final int DTM_UNKNOWN = -1;
    public static final int DTM_NO_TRANSACTION = 0;
    public static final int DTM_ACTIVE = 1;
    public static final int DTM_PREPARING = 2;
    public static final int DTM_PREPARED = 3;
    public static final int DTM_COMMITTING = 4;
    public static final int DTM_COMMITED = 5;
    public static final int DTM_ROLLING_BACK = 6;
    public static final int DTM_ROLLEDBACK = 7;
}
