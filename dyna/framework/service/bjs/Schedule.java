// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Schedule.java

package dyna.framework.service.bjs;

import dyna.util.Utils;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

public class Schedule
    implements Runnable
{

    public Schedule()
    {
        job = null;
        targetObject = null;
        classPath = null;
        methodName = null;
        arguments = null;
        executeAt = null;
        isTemporary = true;
    }

    public void run()
    {
        if(targetObject != null && methodName != null)
        {
            String returnValue = (String)Utils.invokeMethod(targetObject, methodName, arguments);
            if(returnValue != null)
                System.out.println(returnValue);
        }
    }

    public long OUID;
    public Runnable job;
    public Object targetObject;
    public String classPath;
    public String methodName;
    public ArrayList arguments;
    public Date executeAt;
    public long interval;
    public int count;
    public boolean isTemporary;
}
