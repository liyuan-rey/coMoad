// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ACL.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import dyna.framework.service.dos.DOSChangeable;
import java.util.*;

public interface ACL
    extends Service
{

    public abstract void ping();

    public abstract boolean createACL(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract void removeACL(String s, String s1, String s2, String s3, String s4)
        throws IIPRequestException;

    public abstract DOSChangeable getACL(String s, String s1, String s2, String s3, String s4)
        throws IIPRequestException;

    public abstract void setACL(DOSChangeable doschangeable)
        throws IIPRequestException;

    public abstract ArrayList retrieveACL(String s, HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList retrievePrivateACL4Grant(String s, String s1, String s2, Boolean boolean1)
        throws IIPRequestException;

    public abstract ArrayList retrieveACL4Grant(String s, String s1, String s2, Boolean boolean1)
        throws IIPRequestException;

    public abstract HashSet getAllowedUserSet(HashSet hashset, String s, String s1, String s2, String s3, String s4)
        throws IIPRequestException;

    public abstract boolean hasPermission(String s, String s1, String s2, String s3)
        throws IIPRequestException;

    public abstract HashSet getAllowedUserSet4Process(HashSet hashset, String s, String s1, String s2, String s3, String s4)
        throws IIPRequestException;

    public abstract boolean hasPermission4Process(String s, String s1, String s2, String s3, String s4)
        throws IIPRequestException;
}
