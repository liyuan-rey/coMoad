// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DTMImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.server.*;
import dyna.util.*;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import org.xml.sax.Attributes;

// Referenced classes of package dyna.framework.service:
//            DTM

public class DTMImpl extends ServiceServer
    implements DTM, XMLImportable
{
    private class Dxid
    {

        public String toString()
        {
            return new String("{" + Gxid + ", " + Bxid + ", " + status + "}");
        }

        public String Gxid;
        public String Bxid;
        public int status;

        public Dxid(String Gxid, String Bxid)
        {
            this.Gxid = null;
            this.Bxid = null;
            status = -1;
            this.Gxid = Gxid;
            this.Bxid = Bxid;
        }
    }

    private class LogicalPooledConnection
        implements PooledConnection
    {

        public LogicalConnection getLogicalConnection()
        {
            return con;
        }

        public void close()
            throws SQLException
        {
            if(isClosed)
                return;
            isClosed = true;
            HashMap context = getClientContext();
            if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
                context = null;
            if(context != null)
            {
                String mode = (String)context.get("dtm.transaction.mode");
                if(mode == null || mode.equals("single"))
                {
                    HashMap tempMap = (HashMap)context.get("dtm.connection.map");
                    int counter = 0;
                    if(tempMap != null)
                    {
                        counter = Utils.getInt((Integer)tempMap.get(datasource.id + ".counter"));
                        tempMap.put(datasource.id + ".counter", new Integer(counter - 1));
                        if(counter > 1)
                        {
                            tempMap = null;
                            return;
                        }
                        tempMap.remove(datasource.id);
                        tempMap.remove(datasource.id + ".counter");
                        tempMap = null;
                    }
                    context.remove("dtm.trasaction.mode");
                    String xid = (String)context.remove("dtm.bxid");
                    if(xid != null)
                        DTMImpl.transactionMap.remove(xid);
                    con.setXid(null);
                }
                context = null;
            }
            datasource.releasePooledConnection(this);
        }

        public void addConnectionEventListener(ConnectionEventListener connectioneventlistener)
        {
        }

        public void removeConnectionEventListener(ConnectionEventListener connectioneventlistener)
        {
        }

        public Connection getConnection()
            throws SQLException
        {
            isClosed = false;
            HashMap context = getClientContext();
            if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
                context = null;
            if(context != null)
            {
                String mode = (String)context.get("dtm.transaction.mode");
                String bxid = (String)context.get("dtm.bxid");
                String gxid = null;
                Dxid xid = null;
                if(!Utils.isNullString(bxid))
                {
                    xid = (Dxid)DTMImpl.transactionMap.get(bxid);
                    if(xid != null)
                    {
                        gxid = xid.Gxid;
                        if(mode != null && mode.equals("distributed"))
                            xid = newBxid(gxid);
                    }
                } else
                {
                    xid = (Dxid)DTMImpl.transactionMap.get((String)context.get("dtm.bxid.single"));
                }
                if(xid == null)
                    return con;
                DTMImpl.transactionMap.put(xid.Bxid, xid);
                context.put("dtm.bxid", xid.Bxid);
                con.setXid(xid);
                xid.status = 1;
                if(mode != null && mode.equals("distributed"))
                    con.setSavepoint2(xid.Bxid);
                xid = null;
                context = null;
            }
            return con;
        }

        private LogicalConnection con;
        private Datasource datasource;
        private boolean isClosed;

        public LogicalPooledConnection(Connection con, Datasource ds)
            throws SQLException
        {
            this.con = null;
            datasource = null;
            isClosed = true;
            this.con = new LogicalConnection(con);
            datasource = ds;
        }
    }

    private class LogicalConnection
        implements Connection
    {

        public Connection getPhysicalConnection()
        {
            return con;
        }

        public void setXid(Dxid xid)
        {
            this.xid = xid;
        }

        public Dxid getXid()
        {
            return xid;
        }

        public void setSavepoint2(String savepoint)
            throws SQLException
        {
            if(xid == null || xid.status != 1 && xid.status != 5 && xid.status != 7)
            {
                return;
            } else
            {
                Statement stat = con.createStatement();
                stat.executeUpdate("savepoint " + savepoint);
                stat.close();
                stat = null;
                return;
            }
        }

        public void setTransactionIsolation(int param)
            throws SQLException
        {
            con.setTransactionIsolation(param);
        }

        public boolean getAutoCommit()
            throws SQLException
        {
            return con.getAutoCommit();
        }

        public void close()
            throws SQLException
        {
            HashMap context = getClientContext();
            if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
                context = null;
            if(context != null)
            {
                if(xid == null)
                    throw new SQLException("DTM: Internal fatal error: Xid not assigned to connection.");
                String mode = (String)context.get("dtm.transaction.mode");
                if(mode != null && mode.equals("distributed") && xid.status != 3)
                {
                    Statement stat = con.createStatement();
                    stat.executeUpdate("rollback to savepoint " + xid.Bxid);
                    stat.close();
                    stat = null;
                }
            }
            isClosed = true;
        }

        public void commit()
            throws SQLException
        {
            HashMap context = getClientContext();
            if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
                context = null;
            if(context != null)
            {
                if(xid == null)
                    throw new SQLException("DTM: Internal fatal error: Xid not assigned to connection.");
                String mode = (String)context.get("dtm.transaction.mode");
                if(mode != null && mode.equals("distributed"))
                    if(xid.status == 3)
                    {
                        throw new SQLException("DTM: prepare: Prepared distributed transaction branch.");
                    } else
                    {
                        xid.status = 3;
                        return;
                    }
                xid.status = 5;
            } else
            if(xid != null)
                xid.status = 5;
            con.commit();
        }

        public boolean isClosed()
            throws SQLException
        {
            return isClosed;
        }

        public void setCatalog(String str)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
            {
                throw new SQLException("DTM: setCatalog: Not active distributed transaction.");
            } else
            {
                con.setCatalog(str);
                return;
            }
        }

        public boolean isReadOnly()
            throws SQLException
        {
            return con.isReadOnly();
        }

        public void rollback()
            throws SQLException
        {
            HashMap context = getClientContext();
            if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
                context = null;
            if(context != null)
            {
                if(xid == null)
                    throw new SQLException("DTM: Internal fatal error: Xid not assigned to connection.");
                if(xid.status == 3)
                {
                    throw new SQLException("DTM: rollback: Prepared distributed transaction branch.");
                } else
                {
                    xid.status = 6;
                    Statement stat = con.createStatement();
                    stat.executeUpdate("rollback to savepoint " + xid.Bxid);
                    stat.close();
                    stat = null;
                    xid.status = 7;
                    return;
                }
            } else
            {
                con.rollback();
                return;
            }
        }

        public String getCatalog()
            throws SQLException
        {
            return con.getCatalog();
        }

        public PreparedStatement prepareStatement(String str, int param, int param2)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: prepareStatement: Not active distributed transaction.");
            else
                return con.prepareStatement(str, param, param2);
        }

        public void setAutoCommit(boolean param)
            throws SQLException
        {
            con.setAutoCommit(param);
        }

        public CallableStatement prepareCall(String str)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: prepareCall: Not active distributed transaction.");
            else
                return con.prepareCall(str);
        }

        public void setTypeMap(Map map)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
            {
                throw new SQLException("DTM: setTypeMap: Not active distributed transaction.");
            } else
            {
                con.setTypeMap(map);
                return;
            }
        }

        public Statement createStatement(int param, int param1)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: createStatement: Not active distributed transaction.");
            else
                return con.createStatement(param, param1);
        }

        public Statement createStatement()
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: createStatement: Not active distributed transaction.");
            else
                return con.createStatement();
        }

        public String nativeSQL(String str)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: nativeSQL: Not active distributed transaction.");
            else
                return con.nativeSQL(str);
        }

        public CallableStatement prepareCall(String str, int param, int param2)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: prepareCall: Not active distributed transaction.");
            else
                return con.prepareCall(str, param, param2);
        }

        public SQLWarning getWarnings()
            throws SQLException
        {
            return con.getWarnings();
        }

        public PreparedStatement prepareStatement(String str)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
                throw new SQLException("DTM: prepareStatement: Not active distributed transaction.");
            else
                return con.prepareStatement(str);
        }

        public void setReadOnly(boolean param)
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
            {
                throw new SQLException("DTM: setReadOnly: Not active distributed transaction.");
            } else
            {
                con.setReadOnly(param);
                return;
            }
        }

        public Map getTypeMap()
            throws SQLException
        {
            return con.getTypeMap();
        }

        public int getTransactionIsolation()
            throws SQLException
        {
            return getTransactionIsolation();
        }

        public DatabaseMetaData getMetaData()
            throws SQLException
        {
            return getMetaData();
        }

        public void clearWarnings()
            throws SQLException
        {
            if(xid != null && xid.status != 1 && xid.status != 5 && xid.status != 7)
            {
                throw new SQLException("DTM: clearWarnings: Not active distributed transaction.");
            } else
            {
                con.clearWarnings();
                return;
            }
        }

        public int getHoldability()
            throws SQLException
        {
            return con.getHoldability();
        }

        public void setHoldability(int holdability)
            throws SQLException
        {
            con.setHoldability(holdability);
        }

        public Savepoint setSavepoint()
            throws SQLException
        {
            return con.setSavepoint();
        }

        public Savepoint setSavepoint(String name)
            throws SQLException
        {
            return con.setSavepoint(name);
        }

        public void releaseSavepoint(Savepoint savepoint)
            throws SQLException
        {
            con.releaseSavepoint(savepoint);
        }

        public void rollback(Savepoint savepoint)
            throws SQLException
        {
            con.rollback(savepoint);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException
        {
            return con.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException
        {
            return con.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException
        {
            return con.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException
        {
            return con.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int columnIndexes[])
            throws SQLException
        {
            return con.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, String columnNames[])
            throws SQLException
        {
            return con.prepareStatement(sql, columnNames);
        }

        private Connection con;
        private boolean isClosed;
        private Dxid xid;

        public LogicalConnection(Connection con)
        {
            this.con = null;
            isClosed = true;
            xid = null;
            this.con = con;
            try
            {
                this.con.setAutoCommit(false);
                isClosed = this.con.isClosed();
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }
    }

    private class Datasource
    {

        public void clear()
        {
            id = null;
            driver = null;
            url = null;
            user = null;
            password = null;
        }

        public void init()
        {
            synchronized(this)
            {
                try
                {
                    Class.forName(driver);
                    databasedriver = DriverManager.getDriver(url);
                }
                catch(ClassNotFoundException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
                catch(SQLException e)
                {
                    System.err.println(e);
                    System.exit(-1);
                }
            }
            cacheIdle = new LinkedList();
            cacheRun = new LinkedList();
            if(minLimit > maxLimit)
                maxLimit = minLimit;
            PooledConnection ccon = null;
            try
            {
                for(int i = 0; i < minLimit; i++)
                {
                    System.out.print('.');
                    ccon = new LogicalPooledConnection(databasedriver.connect(url, info), this);
                    cacheIdle.addLast(ccon);
                    ccon = null;
                }

            }
            catch(Exception e)
            {
                System.err.println(e);
                System.exit(-1);
            }
        }

        public PooledConnection getPooledConnection()
        {
            PooledConnection ccon = null;
            synchronized(this)
            {
                if(cacheIdle.size() == 0 && cacheRun.size() < maxLimit)
                    try
                    {
                        ccon = new LogicalPooledConnection(databasedriver.connect(url, info), this);
                        cacheRun.addLast(ccon);
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                        System.exit(-1);
                    }
                else
                if(cacheIdle.size() > 0)
                {
                    ccon = (PooledConnection)cacheIdle.removeFirst();
                    cacheRun.addLast(ccon);
                }
            }
            return ccon;
        }

        public void releasePooledConnection(PooledConnection ccon)
        {
            if(ccon == null)
                return;
            synchronized(this)
            {
                if(cacheRun.remove(ccon))
                    cacheIdle.addLast(ccon);
            }
        }

        String id;
        String driver;
        String url;
        String user;
        String password;
        Properties info;
        int minLimit;
        int maxLimit;
        LinkedList cacheIdle;
        LinkedList cacheRun;
        Driver databasedriver;

        public Datasource()
        {
            id = null;
            driver = null;
            url = null;
            user = null;
            password = null;
            info = null;
            minLimit = 3;
            maxLimit = 10;
            cacheIdle = null;
            cacheRun = null;
            databasedriver = null;
        }

        public Datasource(Datasource ds)
        {
            id = null;
            driver = null;
            url = null;
            user = null;
            password = null;
            info = null;
            minLimit = 3;
            maxLimit = 10;
            cacheIdle = null;
            cacheRun = null;
            databasedriver = null;
            id = new String(ds.id);
            driver = new String(ds.driver);
            url = new String(ds.url);
            user = new String(ds.user);
            password = new String(ds.password);
            minLimit = ds.minLimit;
            maxLimit = ds.maxLimit;
            info = new Properties();
            info.setProperty("user", user);
            info.setProperty("password", password);
        }
    }


    public DTMImpl()
    {
        datasourceMap = null;
        isReady = false;
        tempDatasource = null;
        lockObject = null;
        xid = 0L;
        datasourceMap = new HashMap();
        transactionMap = new HashMap();
        lockObject = new String();
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        XMLImport("dtm");
        Datasource ds = null;
        Iterator keys;
        for(keys = datasourceMap.values().iterator(); keys.hasNext();)
        {
            ds = (Datasource)keys.next();
            ds.init();
            ds = null;
        }

        keys = null;
    }

    public PooledConnection getPooledConnection(String datasourceId)
        throws IIPRequestException
    {
        if(Utils.isNullString(datasourceId))
            throw new IIPRequestException("Not defined datasource: " + datasourceId);
        Datasource ds = (Datasource)datasourceMap.get(datasourceId);
        if(ds == null)
            throw new IIPRequestException("Not defined datasource: " + datasourceId);
        if(ds.info == null)
            ds.init();
        PooledConnection ccon = null;
        HashMap context = getClientContext();
        if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
            context = null;
        if(context == null)
        {
            ccon = ds.getPooledConnection();
        } else
        {
            String bxid = (String)context.get("dtm.bxid");
            HashMap tempMap = (HashMap)context.get("dtm.connection.map");
            if(tempMap == null)
            {
                tempMap = new HashMap();
                context.put("dtm.connection.map", tempMap);
            }
            if(Utils.isNullString(bxid))
            {
                Dxid xid = newGxid();
                context.put("dtm.bxid.single", xid.Bxid);
                context.put("dtm.transaction.mode", "single");
                transactionMap.put(xid.Bxid, xid);
                datasourceId = datasourceId + xid.Bxid;
                xid = null;
            }
            int counter = 0;
            counter = Utils.getInt((Integer)tempMap.get(datasourceId + ".counter"));
            tempMap.put(datasourceId + ".counter", new Integer(counter + 1));
            context = null;
            ccon = (PooledConnection)tempMap.get(datasourceId);
            if(ccon == null)
            {
                ccon = ds.getPooledConnection();
                tempMap.put(datasourceId, ccon);
            }
            tempMap = null;
        }
        ds = null;
        return ccon;
    }

    public Dxid newGxid()
    {
        Dxid returnXid = null;
        synchronized(lockObject)
        {
            xid++;
            returnXid = new Dxid("GX$" + Long.toHexString(System.currentTimeMillis()) + "$" + Long.toHexString(xid), "BX$" + Long.toHexString(System.currentTimeMillis()) + "$" + Long.toHexString(xid));
        }
        return returnXid;
    }

    public Dxid newBxid(String xid)
    {
        if(Utils.isNullString(xid))
            return null;
        Dxid returnXid = null;
        synchronized(lockObject)
        {
            this.xid++;
            returnXid = new Dxid(xid, "BX$" + Long.toHexString(System.currentTimeMillis()) + "$" + Long.toHexString(this.xid));
        }
        return returnXid;
    }

    public Dxid newBxid(Dxid xid)
    {
        if(xid == null)
            return null;
        else
            return newBxid(xid.Gxid);
    }

    public ArrayList listDatasourceId()
        throws IIPRequestException
    {
        ArrayList arrayList = null;
        Datasource ds = null;
        if(datasourceMap == null || datasourceMap.size() == 0)
            return null;
        arrayList = new ArrayList(datasourceMap.size());
        Iterator keys;
        for(keys = datasourceMap.values().iterator(); keys.hasNext();)
        {
            ds = (Datasource)keys.next();
            if(ds.id.equals("system"))
            {
                ds = null;
            } else
            {
                arrayList.add(new String(ds.id));
                ds = null;
            }
        }

        keys = null;
        return arrayList;
    }

    public String beginTransaction()
        throws IIPRequestException
    {
        String returnValue = null;
        HashMap context = getClientContext();
        if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
            context = null;
        if(context != null)
        {
            Dxid xid = null;
            String bxid = (String)context.get("dtm.bxid");
            if(!Utils.isNullString(bxid))
                throw new IIPRequestException("DTM: beginTransaction: Transaction exists.");
            xid = newGxid();
            xid.status = 3;
            transactionMap.put(xid.Bxid, xid);
            context.put("dtm.bxid", xid.Bxid);
            context.put("dtm.transaction.mode", "distributed");
            returnValue = xid.Bxid;
            xid = null;
            context = null;
        }
        return returnValue;
    }

    public void endTransaction(String xid)
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
            context = null;
        if(context != null)
        {
            Dxid dxid = null;
            String gxid = null;
            String bxid = (String)context.get("dtm.bxid");
            String tempXid = null;
            HashMap connectionMap = null;
            Iterator mapKey = null;
            LogicalConnection con = null;
            dxid = (Dxid)transactionMap.get(bxid);
            if(dxid == null)
                throw new IIPRequestException("DTM: endTransaction: Xid not exists.");
            gxid = dxid.Gxid;
            dxid = null;
            connectionMap = (HashMap)context.get("dtm.connection.map");
            if(connectionMap != null && connectionMap.size() > 0)
            {
                for(mapKey = connectionMap.keySet().iterator(); mapKey.hasNext();)
                {
                    con = ((LogicalPooledConnection)connectionMap.get((String)mapKey.next())).getLogicalConnection();
                    dxid = con.getXid();
                    con = null;
                    if(dxid != null && dxid.status != 3)
                        throw new IIPRequestException("DTM: endTransaction: Not prepared transaction branch exists.");
                    dxid = null;
                }

                mapKey = null;
                connectionMap.clear();
            }
            context.remove("dtm.connection.map");
            for(mapKey = transactionMap.keySet().iterator(); mapKey.hasNext();)
            {
                tempXid = (String)mapKey.next();
                dxid = (Dxid)transactionMap.get(tempXid);
                if(dxid.Gxid.equals(gxid))
                    mapKey.remove();
                dxid = null;
            }

            context.remove("dtm.bxid");
            context.remove("dtm.transaction.mode");
            dxid = null;
            context = null;
        }
    }

    public void commitTransaction(String xid)
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
            context = null;
        if(context != null)
        {
            Dxid dxid = null;
            String bxid = (String)context.get("dtm.bxid");
            HashMap connectionMap = null;
            Iterator mapKey = null;
            LogicalConnection con = null;
            Connection pcon = null;
            dxid = (Dxid)transactionMap.get(bxid);
            if(dxid == null)
                throw new IIPRequestException("DTM: commitTransaction: Xid not exists.");
            dxid = null;
            connectionMap = (HashMap)context.get("dtm.connection.map");
            if(connectionMap != null && connectionMap.size() > 0)
            {
                for(mapKey = connectionMap.keySet().iterator(); mapKey.hasNext();)
                {
                    con = ((LogicalPooledConnection)connectionMap.get((String)mapKey.next())).getLogicalConnection();
                    dxid = con.getXid();
                    con = null;
                    if(dxid != null && dxid.status != 3)
                        throw new IIPRequestException("DTM: commitTransaction: Not prepared transaction branch exists.");
                }

                mapKey = null;
                dxid = (Dxid)transactionMap.get(bxid);
                dxid.status = 4;
                try
                {
                    for(mapKey = connectionMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        con = ((LogicalPooledConnection)connectionMap.get((String)mapKey.next())).getLogicalConnection();
                        pcon = con.getPhysicalConnection();
                        con = null;
                        pcon.commit();
                        pcon = null;
                    }

                }
                catch(SQLException e)
                {
                    throw new IIPRequestException("DTM: FATAL ERROR: SQL error occured in progress 2nd phase commit.\r\n" + e.fillInStackTrace().toString());
                }
                dxid.status = 5;
                dxid = null;
                mapKey = null;
                connectionMap.clear();
            }
            context = null;
        }
    }

    public void rollbackTransaction(String xid)
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(!context.containsKey("dtm.bxid") && !context.containsKey("dtm.bxid.single"))
            context = null;
        if(context != null)
        {
            Dxid dxid = null;
            String bxid = (String)context.get("dtm.bxid");
            HashMap connectionMap = null;
            Iterator mapKey = null;
            LogicalConnection con = null;
            Connection pcon = null;
            dxid = (Dxid)transactionMap.get(bxid);
            if(dxid == null)
                throw new IIPRequestException("DTM: rollbackTransaction: Xid not exists.");
            connectionMap = (HashMap)context.get("dtm.connection.map");
            if(connectionMap != null && connectionMap.size() > 0)
            {
                dxid.status = 6;
                try
                {
                    for(mapKey = connectionMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        con = ((LogicalPooledConnection)connectionMap.get((String)mapKey.next())).getLogicalConnection();
                        pcon = con.getPhysicalConnection();
                        con = null;
                        pcon.rollback();
                        pcon = null;
                    }

                }
                catch(SQLException e)
                {
                    throw new IIPRequestException("DTM: FATAL ERROR: SQL error occured in progress 2nd phase commit.\r\n" + e.fillInStackTrace().toString());
                }
                dxid.status = 7;
                mapKey = null;
                connectionMap.clear();
            }
            dxid = null;
            context = null;
        }
    }

    public void cancelTransaction(String s)
        throws IIPRequestException
    {
    }

    public void startImport()
    {
        isReady = false;
        if(tempDatasource == null)
            tempDatasource = new Datasource();
    }

    public void startElement(String name, Attributes attrs)
    {
        tempDatasource.clear();
    }

    public void endElement(String name)
    {
        Datasource datasource = new Datasource(tempDatasource);
        datasourceMap.put(datasource.id, datasource);
        datasource = null;
    }

    public void setElementData(String name, String value)
    {
        if(name.equals("datasource.id"))
            tempDatasource.id = new String(value);
        else
        if(name.equals("datasource.driver"))
            tempDatasource.driver = new String(value);
        else
        if(name.equals("datasource.url"))
            tempDatasource.url = new String(value);
        else
        if(name.equals("datasource.user"))
            tempDatasource.user = new String(value);
        else
        if(name.equals("datasource.password"))
            tempDatasource.password = new String(value);
        else
        if(name.equals("datasource.cache.minlimit"))
            try
            {
                tempDatasource.minLimit = Integer.parseInt(value);
            }
            catch(NumberFormatException e)
            {
                System.err.println(e);
                System.exit(-1);
            }
        else
        if(name.equals("datasource.cache.maxlimit"))
            try
            {
                tempDatasource.maxLimit = Integer.parseInt(value);
            }
            catch(NumberFormatException e)
            {
                System.err.println(e);
                System.exit(-1);
            }
    }

    public void endImport()
    {
        isReady = true;
    }

    public void XMLImport(String fileName)
    {
        XMLUtil.XMLImport(fileName, this, "datasource");
    }

    private HashMap datasourceMap;
    private static HashMap transactionMap = null;
    private boolean isReady;
    private Datasource tempDatasource;
    private Object lockObject;
    private long xid;


}
