// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BJSImpl.java

package dyna.framework.service;

import dyna.framework.Server;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.bjs.Schedule;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.*;
import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service:
//            BJS, DOS, DTM

public class BJSImpl extends ServiceServer
    implements BJS, Runnable
{

    public BJSImpl()
    {
        dos = null;
        dtm = null;
        dlock = new DaemonLock();
        jobs = new ArrayList(100);
        sdf1 = new SimpleDateFormat("yyyyMMdd");
        sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf4 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        server = Server.server;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dos = (DOS)getServiceInstance("DF30DOS1");
            dtm = (DTM)getServiceInstance("DF30DTM1");
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
        }
        tp = server.getThreadPool(accessIdentifier);
        Thread js = new Thread(this);
        js.setDaemon(false);
        js.start();
    }

    public String addPermanent(Schedule job)
        throws IIPRequestException
    {
        String returnString;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int rows = 0;
        long returnLong = 0L;
        returnString = null;
        dlock.acquire();
        try
        {
            do
            {
                xc = dtm.getPooledConnection("system");
                con = xc.getConnection();
                if(con != null)
                    break;
                try
                {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
            } while(true);
            returnLong = dos.generateOUID();
            returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into bjsch (ouid,exetime,interval,cnt,claspath,method) values (?, to_char(sysdate,'YYYYMMDDHH24miss'), ?, ?, ?, ?)");
            stat.setLong(1, returnLong);
            stat.setLong(2, job.interval);
            stat.setInt(3, job.count);
            stat.setString(4, job.classPath);
            stat.setString(5, job.methodName);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            ArrayList argumentList = job.arguments;
            if(argumentList != null && argumentList.size() > 0)
            {
                for(int j = 0; j < argumentList.size(); j++)
                {
                    stat = con.prepareStatement("insert into bjscharg (bjsch, seq, val) values (?, ?, ?) ");
                    stat.setLong(1, returnLong);
                    stat.setInt(2, j);
                    stat.setString(3, (String)argumentList.get(j));
                    rows = stat.executeUpdate();
                    stat.close();
                    stat = null;
                }

            }
            stat = con.prepareStatement("select exetime from bjsch where ouid = ? ");
            stat.setLong(1, returnLong);
            for(rs = stat.executeQuery(); rs.next();)
            {
                job.OUID = returnLong;
                job.executeAt = sdf3.parse(rs.getString(1));
            }

            con.commit();
            con.close();
            xc.close();
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
        }
        catch(Exception e)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        jobs.add(job);
        notify();
        return returnString;
    }

    public synchronized String add(Schedule job)
        throws IIPRequestException
    {
        if(job.isTemporary)
        {
            dlock.acquire();
            jobs.add(job);
            notify();
            return null;
        } else
        {
            String ouid = addPermanent(job);
            return ouid;
        }
    }

    public String add(DOSChangeable scheduleData)
        throws IIPRequestException
    {
        Schedule job = new Schedule();
        if(!Utils.isNullString((String)scheduleData.get("ouid")))
            job.OUID = Utils.convertOuidToLong((String)scheduleData.get("ouid"));
        if(scheduleData.get("interval") != null)
            job.interval = ((Long)scheduleData.get("interval")).longValue();
        if(scheduleData.get("count") == null)
            job.count = -1;
        else
            job.count = ((Integer)scheduleData.get("count")).intValue();
        job.classPath = (String)scheduleData.get("classpath");
        job.methodName = (String)scheduleData.get("method");
        if(scheduleData.get("is.temporary") == null)
            job.isTemporary = true;
        else
            job.isTemporary = ((Boolean)scheduleData.get("is.temporary")).booleanValue();
        job.arguments = (ArrayList)scheduleData.get("arguments");
        job.executeAt = (Date)scheduleData.get("execute.at");
        job.job = (Runnable)scheduleData.get("job");
        job.targetObject = scheduleData.get("targetobject");
        String ouid = add(job);
        return ouid;
    }

    public synchronized void removePermanent(Schedule jn)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int rows = 0;
        try
        {
            do
            {
                xc = dtm.getPooledConnection("system");
                con = xc.getConnection();
                if(con != null)
                    break;
                try
                {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
            } while(true);
            stat = con.prepareStatement("delete from bjsch where ouid=? ");
            stat.setLong(1, jn.OUID);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        for(int i = 0; i < jobs.size(); i++)
        {
            if(((Schedule)jobs.get(i)).job != jn.job)
                continue;
            jobs.remove(i);
            dlock.release();
            break;
        }

        return;
    }

    public synchronized void remove(Runnable job)
    {
        for(int i = 0; i < jobs.size(); i++)
        {
            if(((Schedule)jobs.get(i)).job != job)
                continue;
            jobs.remove(i);
            dlock.release();
            break;
        }

    }

    public void remove(Schedule jn)
        throws IIPRequestException
    {
        if(jn.isTemporary)
            remove(jn.job);
        else
            removePermanent(jn);
    }

    public synchronized void remove(DOSChangeable scheduleData)
        throws IIPRequestException
    {
        Schedule jn = null;
        for(int i = 0; i < jobs.size(); i++)
        {
            jn = (Schedule)jobs.get(i);
            if(Utils.convertOuidToLong((String)scheduleData.get("ouid")) == jn.OUID)
                remove(jn);
        }

    }

    private Schedule updateSchedule(Schedule jn)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int rows = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(jn.executeAt);
        do
            if(jn.interval == -1L)
            {
                cal.add(2, 1);
                jn.executeAt = cal.getTime();
            } else
            if(jn.interval == -2L)
            {
                cal.add(1, 1);
                jn.executeAt = cal.getTime();
            } else
            {
                jn.executeAt = new Date(jn.executeAt.getTime() + jn.interval);
            }
        while(jn.executeAt.getTime() < System.currentTimeMillis());
        jn.count = jn.count != -1 ? jn.count - 1 : -1;
        if(jn.isTemporary || jn.count == 0)
            break MISSING_BLOCK_LABEL_368;
        try
        {
            do
            {
                xc = dtm.getPooledConnection("system");
                con = xc.getConnection();
                if(con != null)
                    break;
                try
                {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
            } while(true);
            stat = con.prepareStatement("update bjsch set exetime=to_char(?,'YYYYMMDDHH24miss'), cnt=? where ouid=? ");
            stat.setTimestamp(1, new Timestamp(jn.executeAt.getTime()));
            stat.setInt(2, jn.count);
            stat.setLong(3, jn.OUID);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
        }
        catch(Exception e1)
        {
            System.out.println(e1);
            System.exit(-1);
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return jn.count == 0 ? null : jn;
    }

    public synchronized long runJobs()
        throws IIPRequestException
    {
        long minDiff = 0x7fffffffffffffffL;
        long now = System.currentTimeMillis();
        for(int i = 0; i < jobs.size();)
        {
            Schedule jn = (Schedule)jobs.get(i);
            if(jn.executeAt.getTime() <= now)
            {
                if(jn.job == null)
                {
                    Class targetClass = null;
                    Object targetObject = null;
                    try
                    {
                        targetClass = Class.forName(jn.classPath);
                        targetObject = targetClass.newInstance();
                        jn.targetObject = targetObject;
                        jn.job = jn;
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                        jn.job = null;
                    }
                }
                if(tp != null && jn.job != null)
                    tp.addRequest(jn.job);
                else
                if(jn.job != null)
                {
                    Thread jt = new Thread(jn.job);
                    jt.setDaemon(false);
                    jt.start();
                }
                if(updateSchedule(jn) == null)
                {
                    jobs.remove(i);
                    dlock.release();
                    remove(jn);
                }
            } else
            {
                long diff = jn.executeAt.getTime() - now;
                minDiff = Math.min(diff, minDiff);
                i++;
            }
        }

        return minDiff;
    }

    public synchronized void run()
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        int rows = 0;
        try
        {
            do
            {
                xc = dtm.getPooledConnection("system");
                con = xc.getConnection();
                if(con != null)
                    break;
                try
                {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e);
                }
            } while(true);
            stat = con.prepareStatement("select to_date(js.exetime,'YYYYMMDDHH24miss'), js.interval, js.cnt, js.claspath, js.method, js.ouid from bjsch js ");
            rs = stat.executeQuery();
            Schedule aJob = null;
            ArrayList args = new ArrayList();
            for(; rs.next(); args.clear())
            {
                aJob = new Schedule();
                aJob.executeAt = new Date(rs.getTimestamp(1).getTime());
                aJob.interval = rs.getLong(2);
                aJob.count = rs.getInt(3);
                aJob.classPath = rs.getString(4);
                aJob.methodName = rs.getString(5);
                aJob.isTemporary = false;
                aJob.OUID = rs.getLong(6);
                stat2 = con.prepareStatement("select jsa.val from bjscharg jsa where jsa.bjsch=? ");
                stat2.setLong(1, aJob.OUID);
                for(rs2 = stat2.executeQuery(); rs2.next(); args.add(rs2.getString(1)));
                rs2.close();
                rs2 = null;
                stat2.close();
                stat2 = null;
                args.trimToSize();
                aJob.arguments = (ArrayList)args.clone();
                jobs.add(aJob);
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
        }
        catch(SQLException e)
        {
            if(con != null)
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
            if(e != null)
                System.err.println(e);
        }
        catch(Exception e1)
        {
            System.out.println(e1);
            System.exit(-1);
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        try
        {
            wait(60000L);
        }
        catch(Exception exception) { }
        do
            try
            {
                long waitTime = runJobs();
                wait(waitTime);
            }
            catch(Exception exception1) { }
        while(true);
    }

    public void invoke(Runnable job)
        throws IIPRequestException
    {
        invokeIn(job, 0L);
    }

    public void invokeIn(Runnable job, long millis)
        throws IIPRequestException
    {
        invokeInAndRepeat(job, millis, 1000L, 1);
    }

    public void invokeInAndRepeat(Runnable job, long millis, long repeat)
        throws IIPRequestException
    {
        invokeInAndRepeat(job, millis, repeat, -1);
    }

    public void invokeInAndRepeat(Runnable job, long millis, long repeat, int count)
        throws IIPRequestException
    {
        Date when = new Date(System.currentTimeMillis() + millis);
        invokeAtAndRepeat(job, when, repeat, count);
    }

    public void invokeAt(Runnable job, Date when)
        throws IIPRequestException
    {
        invokeAtAndRepeat(job, when, 1000L, 1);
    }

    public void invokeAtAndRepeat(Runnable job, Date when, long repeat)
        throws IIPRequestException
    {
        invokeAtAndRepeat(job, when, repeat, -1);
    }

    public void invokeAtAndRepeat(Runnable job, Date when, long repeat, int count)
        throws IIPRequestException
    {
        Schedule jn = new Schedule();
        jn.job = job;
        jn.executeAt = when;
        jn.interval = repeat;
        jn.count = count;
        add(jn);
    }

    public void invokeAtAndRepeatPermanent(Runnable job, Date when, long repeat, int count)
        throws IIPRequestException
    {
        Schedule jn = new Schedule();
        jn.job = job;
        jn.executeAt = when;
        jn.interval = repeat;
        jn.count = count;
        jn.isTemporary = false;
        addPermanent(jn);
    }

    public void invokeAtNextDOW(Runnable job, Date when, int DOW)
        throws IIPRequestException
    {
        Calendar target = Calendar.getInstance();
        target.setTime(when);
        for(; target.get(7) != DOW; target.add(5, 1));
        invokeAt(job, target.getTime());
    }

    public void cancel(Runnable job)
    {
        remove(job);
    }

    public ArrayList retrieveJobSchedule()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        int rows = 0;
        long returnLong = 0L;
        String returnString = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            ArrayList returnList = new ArrayList();
            ArrayList scheduleData = null;
            ArrayList arguments = null;
            stat = con.prepareStatement("select ouid,     exetime,     interval,     cnt,     claspath,     method from bjsch order by ouid ");
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                scheduleData = new ArrayList();
                long returnLong = rs.getLong(1);
                scheduleData.add(Long.toHexString(returnLong));
                scheduleData.add(new Long(rs.getLong(3)));
                scheduleData.add(new Integer(rs.getInt(4)));
                scheduleData.add(rs.getString(5));
                scheduleData.add(rs.getString(6));
                PreparedStatement stat2 = con.prepareStatement("select val from bjscharg where bjsch = ? ");
                stat2.setLong(1, returnLong);
                ResultSet rs2 = stat2.executeQuery();
                arguments = new ArrayList();
                for(; rs2.next(); arguments.add(rs2.getString(1)));
                scheduleData.add(arguments);
                arguments = null;
                rs2.close();
                rs2 = null;
                stat2.close();
                stat2 = null;
                returnList.add(scheduleData);
                scheduleData = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            returnList.trimToSize();
            arraylist = returnList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public static final int ONCE = 1;
    public static final int FOREVER = -1;
    public static final long MINUTELY = 60000L;
    public static final long TEN_MINUTELY = 0x927c0L;
    public static final long QUARTER_HOURLY = 0xdbba0L;
    public static final long HALF_HOURLY = 0x1b7740L;
    public static final long HOURLY = 0x36ee80L;
    public static final long QUARTER_DAILY = 0x1499700L;
    public static final long HALF_DAILY = 0x2932e00L;
    public static final long DAILY = 0x5265c00L;
    public static final long TWO_DAILY = 0xa4cb800L;
    public static final long WEEKLY = 0x240c8400L;
    public static final long MONTHLY = -1L;
    public static final long YEARLY = -2L;
    private DOS dos;
    private DTM dtm;
    private Server server;
    private ThreadPool tp;
    private DaemonLock dlock;
    private ArrayList jobs;
    private SimpleDateFormat sdf1;
    private SimpleDateFormat sdf2;
    private SimpleDateFormat sdf3;
    private SimpleDateFormat sdf4;
}
