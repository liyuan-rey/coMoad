// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WKS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import java.util.HashMap;

public interface WKS
    extends Service
{

    public abstract void ping();

    public abstract boolean createNode(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeNode(HashMap hashmap)
        throws IIPRequestException;

    public abstract String getNodeValue(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean setNode(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList getChildNodes(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean createSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract HashMap getSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean setSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean addMemberToSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeMemberFromSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList getMembersOfSharedWorkspace(HashMap hashmap)
        throws IIPRequestException;

    public abstract void setPrivateDefaultFolder(String s, String s1)
        throws IIPRequestException;

    public abstract String getPrivateDefaultFolder(String s)
        throws IIPRequestException;

    public static final String NODE_FOLDER = "FOLDER";
    public static final String NODE_FOLDERLINK = "FOLDERLINK";
    public static final String NODE_OBJECT = "OBJECT";
    public static final String NODE_LISTFILTER = "LISTFILTER";
    public static final String NODE_LINKFILTER = "LINKFILTER";
    public static final String WORKSPACE_PRIVATE = "PRIVATE";
    public static final String WORKSPACE_SHARED = "SHARED";
    public static final String WORKSPACE_PUBLIC = "PUBLIC";
}
