// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BJS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import dyna.framework.service.bjs.Schedule;
import dyna.framework.service.dos.DOSChangeable;
import java.util.ArrayList;
import java.util.Date;

public interface BJS
    extends Service
{

    public abstract String addPermanent(Schedule schedule)
        throws IIPRequestException;

    public abstract String add(Schedule schedule)
        throws IIPRequestException;

    public abstract String add(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removePermanent(Schedule schedule)
        throws IIPRequestException;

    public abstract void remove(Runnable runnable);

    public abstract void remove(Schedule schedule)
        throws IIPRequestException;

    public abstract void remove(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract long runJobs()
        throws IIPRequestException;

    public abstract void invoke(Runnable runnable)
        throws IIPRequestException;

    public abstract void invokeIn(Runnable runnable, long l)
        throws IIPRequestException;

    public abstract void invokeInAndRepeat(Runnable runnable, long l, long l1)
        throws IIPRequestException;

    public abstract void invokeInAndRepeat(Runnable runnable, long l, long l1, int i)
        throws IIPRequestException;

    public abstract void invokeAt(Runnable runnable, Date date)
        throws IIPRequestException;

    public abstract void invokeAtAndRepeat(Runnable runnable, Date date, long l)
        throws IIPRequestException;

    public abstract void invokeAtAndRepeat(Runnable runnable, Date date, long l, int i)
        throws IIPRequestException;

    public abstract void invokeAtAndRepeatPermanent(Runnable runnable, Date date, long l, int i)
        throws IIPRequestException;

    public abstract void invokeAtNextDOW(Runnable runnable, Date date, int i)
        throws IIPRequestException;

    public abstract void cancel(Runnable runnable)
        throws IIPRequestException;

    public abstract ArrayList retrieveJobSchedule()
        throws IIPRequestException;

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
}
