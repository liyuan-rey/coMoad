// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DTMUtil.java

package dyna.util;

import dyna.framework.iip.IIPRequestException;
import java.sql.*;
import javax.sql.PooledConnection;

public class DTMUtil
{

    public DTMUtil()
    {
    }

    public static void closeStatementSafely(Statement stat)
    {
        if(stat != null)
        {
            try
            {
                stat.close();
            }
            catch(Exception exception) { }
            stat = null;
        }
    }

    public static void closeConnectionSafely(Connection con)
    {
        if(con != null)
        {
            try
            {
                con.close();
            }
            catch(Exception exception) { }
            con = null;
        }
    }

    public static void closePooledConnectionSafely(PooledConnection xc)
    {
        if(xc != null)
        {
            try
            {
                xc.close();
            }
            catch(Exception exception) { }
            xc = null;
        }
    }

    public static void closeSafely(Statement stat, Connection con, PooledConnection xc)
    {
        if(stat != null)
        {
            try
            {
                stat.close();
            }
            catch(Exception exception) { }
            stat = null;
        }
        if(con != null)
        {
            try
            {
                con.close();
            }
            catch(Exception exception1) { }
            con = null;
        }
        if(xc != null)
        {
            try
            {
                xc.close();
            }
            catch(Exception exception2) { }
            xc = null;
        }
    }

    public static void sqlExceptionHelper(Connection con, SQLException e)
        throws IIPRequestException
    {
        if(con != null)
            try
            {
                con.rollback();
            }
            catch(SQLException sqlexception) { }
        if(e != null)
        {
            e.printStackTrace();
            throw new IIPRequestException(e.toString());
        } else
        {
            return;
        }
    }
}
