// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AUS.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;
import java.util.ArrayList;
import java.util.HashMap;

public interface AUS
    extends Service
{

    public abstract void ping();

    public abstract String createUser(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeUser(String s)
        throws IIPRequestException;

    public abstract HashMap getUser(String s)
        throws IIPRequestException;

    public abstract boolean setUser(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listUser()
        throws IIPRequestException;

    public abstract boolean linkUserToGroup(String s, String s1)
        throws IIPRequestException;

    public abstract boolean unlinkUserFromGroup(String s, String s1)
        throws IIPRequestException;

    public abstract boolean linkRoleToUser(String s, String s1)
        throws IIPRequestException;

    public abstract boolean unlinkRoleFromUser(String s, String s1)
        throws IIPRequestException;

    public abstract ArrayList listGroupOfUser(String s)
        throws IIPRequestException;

    public abstract ArrayList listRoleOfUser(String s)
        throws IIPRequestException;

    public abstract String getUid(String s)
        throws IIPRequestException;

    public abstract boolean isExistUser(String s)
        throws IIPRequestException;

    public abstract String createGroup(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeGroup(String s)
        throws IIPRequestException;

    public abstract HashMap getGroup(String s)
        throws IIPRequestException;

    public abstract boolean setGroup(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listGroup()
        throws IIPRequestException;

    public abstract boolean setMembersOfGroup(ArrayList arraylist)
        throws IIPRequestException;

    public abstract ArrayList listMembersOfGroup(String s)
        throws IIPRequestException;

    public abstract String getGid(String s)
        throws IIPRequestException;

    public abstract boolean isGroup(String s)
        throws IIPRequestException;

    public abstract boolean isExistGroup(String s)
        throws IIPRequestException;

    public abstract String createRole(HashMap hashmap)
        throws IIPRequestException;

    public abstract boolean removeRole(String s)
        throws IIPRequestException;

    public abstract HashMap getRole(String s)
        throws IIPRequestException;

    public abstract boolean setRole(HashMap hashmap)
        throws IIPRequestException;

    public abstract ArrayList listRole()
        throws IIPRequestException;

    public abstract ArrayList listUsersOfRole(String s)
        throws IIPRequestException;

    public abstract boolean setRolesOfUser(ArrayList arraylist)
        throws IIPRequestException;

    public abstract boolean isExistRole(String s)
        throws IIPRequestException;

    public abstract boolean login(String s, String s1)
        throws IIPRequestException;

    public abstract boolean changePassword(String s, String s1, String s2)
        throws IIPRequestException;

    public abstract boolean resetPassword(String s)
        throws IIPRequestException;

    public abstract boolean changeUserStatus(String s, String s1)
        throws IIPRequestException;

    public abstract boolean logout(String s)
        throws IIPRequestException;

    public abstract boolean hasRole(String s, String s1)
        throws IIPRequestException;
}
