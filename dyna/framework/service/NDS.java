// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NDS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import java.util.HashMap;

public interface NDS
    extends Service
{

    public abstract void ping();

    public abstract boolean addNode(String s, String s1, String s2, String s3)
        throws IIPRequestException;

    public abstract boolean removeNode(String s)
        throws IIPRequestException;

    public abstract HashMap getNode(String s)
        throws IIPRequestException;

    public abstract String getValue(String s)
        throws IIPRequestException;

    public abstract boolean setValue(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList getChildNodeList(String s)
        throws IIPRequestException;

    public abstract ArrayList getChildNodes(String s)
        throws IIPRequestException;

    public abstract boolean renameNode(String s, String s1)
        throws IIPRequestException;

    public abstract boolean addArgument(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract boolean removeArgument(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList getArgumentNameList(String s)
        throws IIPRequestException;

    public abstract ArrayList getArgumentValueList(String s)
        throws IIPRequestException;

    public abstract HashMap getArguments(String s)
        throws IIPRequestException;

    public abstract String getArgument(String s, String s1)
        throws IIPRequestException;

    public abstract boolean setArgument(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract boolean addSubSet(String s, String s1)
        throws IIPRequestException;

    public abstract boolean setSubSet(String s, String s1)
        throws IIPRequestException;

    public abstract String getSubSet(String s)
        throws IIPRequestException;

    public abstract String findKeyValue(String s, String s1)
        throws IIPRequestException;

    public static final String NDS_TYPE_VALUE = "value";
}
