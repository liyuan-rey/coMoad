// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AUSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.util.Utils;
import java.io.PrintStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package dyna.framework.service:
//            AUS, NDS

public class AUSImpl extends ServiceServer
    implements AUS
{
    private class LoginCleanUp extends Thread
    {

        public void run()
        {
            try
            {
                Iterator loginKey = null;
                HashMap context = null;
                String userId = null;
                do
                {
                    Thread.sleep(60000L);
                    for(loginKey = loginCheck.keySet().iterator(); loginKey.hasNext();)
                    {
                        userId = (String)loginKey.next();
                        context = (HashMap)loginCheck.get(userId);
                        if(!isConnectedAddress((InetAddress)context.get("client.inet.address")))
                        {
                            loginCheck.remove(userId);
                            System.out.println("[login information cleared: " + userId + "]");
                        }
                        context = null;
                    }

                    loginKey = null;
                } while(true);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        LoginCleanUp()
        {
        }
    }


    public AUSImpl()
    {
        nds = null;
        loginCheck = null;
        loginCheck = new HashMap();
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            nds = (NDS)getServiceInstance("DF30NDS1");
            getClass();
            if(nds.getValue("::/AUS_SYSTEM_DIRECTORY") == null)
            {
                nds.addNode("::", "AUS_SYSTEM_DIRECTORY", "AUS", "10000");
                getClass();
                nds.addNode("::/AUS_SYSTEM_DIRECTORY", "USER", "AUS", "USER");
                getClass();
                nds.addNode("::/AUS_SYSTEM_DIRECTORY", "USEROUIDMAP", "AUS", "USEROUIDMAP");
                getClass();
                nds.addNode("::/AUS_SYSTEM_DIRECTORY", "GROUP", "AUS", "GROUP");
                getClass();
                nds.addNode("::/AUS_SYSTEM_DIRECTORY", "GROUPOUIDMAP", "AUS", "GROUPOUIDMAP");
                getClass();
                nds.addNode("::/AUS_SYSTEM_DIRECTORY", "ROLE", "AUS", "ROLE");
                getClass();
                nds.addNode("::/AUS_SYSTEM_DIRECTORY", "ROLEOUIDMAP", "AUS", "ROLEOUIDMAP");
                HashMap tempData = new HashMap();
                getClass();
                tempData.put("roleId", "SYSTEM.INTERNAL");
                tempData.put("description", "System Internal");
                createRole(tempData);
                tempData.clear();
                getClass();
                tempData.put("groupId", "SYSTEM.INTERNAL");
                tempData.put("description", "System Internal");
                createGroup(tempData);
                tempData.clear();
                getClass();
                tempData.put("userId", "SYSTEM.INTERNAL");
                getClass();
                tempData.put("password", "CIES." + "SYSTEM.INTERNAL");
                getClass();
                tempData.put("primarygroup", "SYSTEM.INTERNAL");
                tempData.put("name", "System Internal");
                tempData.put("description", "System Internal");
                createUser(tempData);
                tempData.clear();
                getClass();
                getClass();
                linkRoleToUser("SYSTEM.INTERNAL", "SYSTEM.INTERNAL");
                getClass();
                tempData.put("roleId", "SYSTEM.ADMINISTRATOR");
                tempData.put("description", "System Administrator");
                createRole(tempData);
                tempData.clear();
                getClass();
                tempData.put("groupId", "SYSTEM.ADMINISTRATOR");
                tempData.put("description", "System Administrator");
                createGroup(tempData);
                tempData.clear();
                getClass();
                tempData.put("userId", "SYSTEM.ADMINISTRATOR");
                getClass();
                tempData.put("password", "SYSTEM.ADMINISTRATOR");
                getClass();
                tempData.put("primarygroup", "SYSTEM.ADMINISTRATOR");
                tempData.put("name", "System Administrator");
                tempData.put("description", "System Administrator");
                createUser(tempData);
                getClass();
                getClass();
                linkRoleToUser("SYSTEM.ADMINISTRATOR", "SYSTEM.ADMINISTRATOR");
                tempData.clear();
                getClass();
                tempData.put("roleId", "SYSTEM.REVIEW");
                tempData.put("description", "System Review");
                createRole(tempData);
                tempData.clear();
                getClass();
                tempData.put("roleId", "SYSTEM.APPROVAL");
                tempData.put("description", "System Approval");
                createRole(tempData);
                tempData.clear();
                tempData = null;
            }
            LoginCleanUp loginCleanUp = new LoginCleanUp();
            loginCleanUp.start();
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    private String generateOuid()
        throws IIPRequestException
    {
        long tempLong = 0L;
        String tempString = null;
        getClass();
        tempString = nds.getValue("::/AUS_SYSTEM_DIRECTORY");
        try
        {
            tempLong = Long.parseLong(tempString);
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
            throw new IIPRequestException("generateOuid ()\r\n" + e);
        }
        tempLong++;
        tempString = Long.toString(tempLong);
        getClass();
        nds.setValue("::/AUS_SYSTEM_DIRECTORY", tempString);
        return tempString;
    }

    public String createUser(HashMap user)
        throws IIPRequestException
    {
        if(user == null || user.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        String ouid = null;
        tempString = (String)user.get("userId");
        if(Utils.isNullString(tempString) || Utils.isNullString((String)user.get("password")))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + user);
        getClass();
        tempString2 = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + tempString;
        if(nds.getValue(tempString2) != null)
            throw new IIPRequestException("User ID exsits: " + tempString);
        ouid = generateOuid();
        getClass();
        if(!nds.addNode("::/AUS_SYSTEM_DIRECTORY/USER", tempString, "USER", ouid))
            throw new IIPRequestException("Can not register user Id: " + tempString);
        getClass();
        nds.addNode("::/AUS_SYSTEM_DIRECTORY/USEROUIDMAP", ouid, "USER", tempString);
        nds.addArgument(tempString2, "password", (String)user.get("password"));
        tempString = (String)user.get("name");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "name", tempString);
        tempString = (String)user.get("description");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "description", tempString);
        tempString = (String)user.get("primarygroup");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "primarygroup", tempString);
        tempString = (String)user.get("status");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "status", tempString);
        else
            nds.addArgument(tempString2, "status", "CRT");
        tempString = sdf2.format(new Date());
        nds.addArgument(tempString2, "cdate", tempString);
        nds.addArgument(tempString2, "lmdate", tempString);
        tempString = (String)user.get("duedate");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "duedate", tempString);
        user.clear();
        user = null;
        return ouid;
    }

    public boolean setUser(HashMap user)
        throws IIPRequestException
    {
        if(user == null || user.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        tempString = (String)user.get("userId");
        if(Utils.isNullString(tempString) || Utils.isNullString((String)user.get("password")))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + user);
        getClass();
        tempString2 = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + tempString;
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("User ID not exsits: " + tempString);
        tempString = (String)user.get("name");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "name") == null)
                nds.addArgument(tempString2, "name", tempString);
            else
                nds.setArgument(tempString2, "name", tempString);
        } else
        {
            nds.removeArgument(tempString2, "name");
        }
        tempString = (String)user.get("password");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "password") == null)
                nds.addArgument(tempString2, "password", tempString);
            else
                nds.setArgument(tempString2, "password", tempString);
        } else
        {
            nds.removeArgument(tempString2, "password");
        }
        tempString = (String)user.get("description");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "description") == null)
                nds.addArgument(tempString2, "description", tempString);
            else
                nds.setArgument(tempString2, "description", tempString);
        } else
        {
            nds.removeArgument(tempString2, "description");
        }
        tempString = (String)user.get("primarygroup");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "primarygroup") == null)
                nds.addArgument(tempString2, "primarygroup", tempString);
            else
                nds.setArgument(tempString2, "primarygroup", tempString);
        } else
        {
            nds.removeArgument(tempString2, "primarygroup");
        }
        tempString = sdf2.format(new Date());
        if(nds.getArgument(tempString2, "lmdate") == null)
            nds.addArgument(tempString2, "lmdate", tempString);
        else
            nds.setArgument(tempString2, "lmdate", tempString);
        tempString = (String)user.get("duedate");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "duedate") == null)
                nds.addArgument(tempString2, "duedate", tempString);
            else
                nds.setArgument(tempString2, "duedate", tempString);
        } else
        {
            nds.removeArgument(tempString2, "duedate");
        }
        user.clear();
        user = null;
        return true;
    }

    public boolean removeUser(String userId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        if(Utils.isNullString(userId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : userId");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            throw new IIPRequestException("User ID not exsits: " + userId);
        } else
        {
            nds.removeNode(tempString);
            getClass();
            nds.removeNode("::/AUS_SYSTEM_DIRECTORY/USEROUIDMAP" + "/" + ouid);
            return true;
        }
    }

    public HashMap getUser(String userId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        if(Utils.isNullString(userId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : userId");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("User ID not exsits: " + userId);
        else
            return nds.getArguments(tempString);
    }

    public ArrayList listUser()
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/USER");
    }

    public boolean linkUserToGroup(String userId, String groupId)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId) || Utils.isNullString(groupId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : userId=" + userId + ",groupId=" + groupId);
        String tempString = null;
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/GROUP";
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            getClass();
            nds.addNode("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "GROUP", "USER", "GROUP");
        }
        if(Utils.isNullString(nds.getValue(tempString + "/" + groupId)))
            nds.addNode(tempString, groupId, "USER", "GROUP");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId + "/MEMBER";
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            getClass();
            nds.addNode("::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId, "MEMBER", "GROUP", "MEMBER");
        }
        if(Utils.isNullString(nds.getValue(tempString + "/" + userId)))
            nds.addNode(tempString, userId, "GROUP", "MEMBER");
        return true;
    }

    public boolean unlinkUserFromGroup(String userId, String groupId)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId) || Utils.isNullString(groupId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : userId=" + userId + ",groupId=" + groupId);
        String tempString = null;
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/GROUP";
        if(Utils.isNullString(nds.getValue(tempString)))
            return false;
        if(Utils.isNullString(nds.getValue(tempString + "/" + groupId)))
            return false;
        nds.removeNode(tempString + "/" + groupId);
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId + "/MEMBER";
        if(Utils.isNullString(nds.getValue(tempString)))
            return false;
        if(Utils.isNullString(nds.getValue(tempString + "/" + userId)))
        {
            return false;
        } else
        {
            nds.removeNode(tempString + "/" + userId);
            return true;
        }
    }

    public boolean linkRoleToUser(String roleId, String userId)
        throws IIPRequestException
    {
        if(Utils.isNullString(roleId) || Utils.isNullString(userId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : roleId=" + roleId + ",userId=" + userId);
        String tempString = null;
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId + "/USER";
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            getClass();
            nds.addNode("::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId, "USER", "ROLE", "USER");
        }
        if(Utils.isNullString(nds.getValue(tempString + "/" + userId)))
            nds.addNode(tempString, userId, "ROLE", "USER");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/ROLE";
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            getClass();
            nds.addNode("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "ROLE", "USER", "ROLE");
        }
        if(Utils.isNullString(nds.getValue(tempString + "/" + roleId)))
            nds.addNode(tempString, roleId, "USER", "ROLE");
        return true;
    }

    public boolean unlinkRoleFromUser(String roleId, String userId)
        throws IIPRequestException
    {
        if(Utils.isNullString(roleId) || Utils.isNullString(userId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : roleId=" + roleId + ",userId=" + userId);
        String tempString = null;
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId + "/USER";
        if(Utils.isNullString(nds.getValue(tempString)))
            return false;
        if(Utils.isNullString(nds.getValue(tempString + "/" + userId)))
            return false;
        nds.removeNode(tempString + "/" + userId);
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/ROLE";
        if(Utils.isNullString(nds.getValue(tempString)))
            return false;
        if(Utils.isNullString(nds.getValue(tempString + "/" + roleId)))
        {
            return false;
        } else
        {
            nds.removeNode(tempString + "/" + roleId);
            return true;
        }
    }

    public ArrayList listGroupOfUser(String userId)
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/GROUP");
    }

    public ArrayList listRoleOfUser(String userId)
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/ROLE");
    }

    public String getUid(String userId)
        throws IIPRequestException
    {
        getClass();
        return nds.getValue("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId);
    }

    public boolean isExistUser(String userId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        if(Utils.isNullString(userId))
            return false;
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId;
        ouid = nds.getValue(tempString);
        return !Utils.isNullString(ouid);
    }

    public String createGroup(HashMap group)
        throws IIPRequestException
    {
        if(group == null || group.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        String ouid = null;
        tempString = (String)group.get("groupId");
        if(Utils.isNullString(tempString))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + group);
        getClass();
        tempString2 = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + tempString;
        if(nds.getValue(tempString2) != null)
            throw new IIPRequestException("Group ID exsits: " + tempString);
        ouid = generateOuid();
        getClass();
        if(!nds.addNode("::/AUS_SYSTEM_DIRECTORY/GROUP", tempString, "GROUP", ouid))
            throw new IIPRequestException("Can not register group Id: " + tempString);
        getClass();
        nds.addNode("::/AUS_SYSTEM_DIRECTORY/GROUPOUIDMAP", ouid, "GROUP", tempString);
        tempString = (String)group.get("description");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "description", tempString);
        tempString = (String)group.get("status");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "status", tempString);
        else
            nds.addArgument(tempString2, "status", "CRT");
        tempString = sdf2.format(new Date());
        nds.addArgument(tempString2, "cdate", tempString);
        nds.addArgument(tempString2, "lmdate", tempString);
        group.clear();
        group = null;
        return ouid;
    }

    public HashMap getGroup(String groupId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        HashMap group = null;
        if(Utils.isNullString(groupId))
            throw new IIPRequestException("Miss out mandatory parameter(s) :groupId");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            throw new IIPRequestException("Group ID not exsits: " + tempString);
        } else
        {
            group = nds.getArguments(tempString);
            return group;
        }
    }

    public boolean removeGroup(String groupId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        if(Utils.isNullString(groupId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : groupId");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            throw new IIPRequestException("Group ID not exsits: " + tempString);
        } else
        {
            nds.removeNode(tempString);
            getClass();
            nds.removeNode("::/AUS_SYSTEM_DIRECTORY/GROUPOUIDMAP" + "/" + ouid);
            return true;
        }
    }

    public boolean setGroup(HashMap group)
        throws IIPRequestException
    {
        if(group == null || group.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        tempString = (String)group.get("groupId");
        getClass();
        tempString2 = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + tempString;
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("Group ID not exsits: " + tempString);
        tempString = (String)group.get("description");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "description") == null)
                nds.addArgument(tempString2, "description", tempString);
            else
                nds.setArgument(tempString2, "description", tempString);
        } else
        {
            nds.removeArgument(tempString2, "description");
        }
        tempString = sdf2.format(new Date());
        if(nds.getArgument(tempString2, "lmdate") == null)
            nds.addArgument(tempString2, "lmdate", tempString);
        else
            nds.setArgument(tempString2, "lmdate", tempString);
        group.clear();
        group = null;
        return true;
    }

    public ArrayList listGroup()
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/GROUP");
    }

    public boolean setMembersOfGroup(ArrayList userList)
        throws IIPRequestException
    {
        return false;
    }

    public ArrayList listMembersOfGroup(String groupId)
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId + "/MEMBER");
    }

    public String getGid(String userId)
        throws IIPRequestException
    {
        getClass();
        return nds.getValue("::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + userId);
    }

    public boolean isGroup(String groupId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        boolean isgroup = true;
        if(Utils.isNullString(groupId))
        {
            isgroup = false;
            return isgroup;
        }
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            isgroup = false;
            return isgroup;
        } else
        {
            return isgroup;
        }
    }

    public boolean isExistGroup(String groupId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        boolean isgroup = true;
        if(Utils.isNullString(groupId))
        {
            isgroup = false;
            return isgroup;
        }
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/GROUP" + "/" + groupId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            isgroup = false;
            return isgroup;
        } else
        {
            return isgroup;
        }
    }

    public String createRole(HashMap role)
        throws IIPRequestException
    {
        if(role == null || role.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        String ouid = null;
        tempString = (String)role.get("roleId");
        if(Utils.isNullString(tempString))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + role);
        getClass();
        tempString2 = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + tempString;
        if(nds.getValue(tempString2) != null)
            throw new IIPRequestException("Group ID exsits: " + tempString);
        ouid = generateOuid();
        getClass();
        if(!nds.addNode("::/AUS_SYSTEM_DIRECTORY/ROLE", tempString, "ROLE", ouid))
            throw new IIPRequestException("Can not register role Id: " + tempString);
        getClass();
        nds.addNode("::/AUS_SYSTEM_DIRECTORY/ROLEOUIDMAP", ouid, "ROLE", tempString);
        tempString = (String)role.get("description");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "description", tempString);
        tempString = (String)role.get("status");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "status", tempString);
        else
            nds.addArgument(tempString2, "status", "CRT");
        tempString = sdf2.format(new Date());
        nds.addArgument(tempString2, "cdate", tempString);
        nds.addArgument(tempString2, "lmdate", tempString);
        role.clear();
        role = null;
        return ouid;
    }

    public boolean setRole(HashMap role)
        throws IIPRequestException
    {
        if(role == null || role.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        tempString = (String)role.get("roleId");
        if(Utils.isNullString(tempString))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + role);
        getClass();
        tempString2 = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + tempString;
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("Role ID not exsits: " + tempString);
        tempString = (String)role.get("description");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "description") == null)
                nds.addArgument(tempString2, "description", tempString);
            else
                nds.setArgument(tempString2, "description", tempString);
        } else
        {
            nds.removeArgument(tempString2, "description");
        }
        tempString = sdf2.format(new Date());
        if(nds.getArgument(tempString2, "lmdate") == null)
            nds.addArgument(tempString2, "lmdate", tempString);
        else
            nds.setArgument(tempString2, "lmdate", tempString);
        role.clear();
        role = null;
        return true;
    }

    public HashMap getRole(String roleId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        HashMap role = null;
        if(Utils.isNullString(roleId))
            throw new IIPRequestException("Miss out mandatory parameter(s) :roleId");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            throw new IIPRequestException("Group ID not exsits: " + tempString);
        } else
        {
            role = nds.getArguments(tempString);
            return role;
        }
    }

    public boolean removeRole(String roleId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        if(Utils.isNullString(roleId))
            throw new IIPRequestException("Miss out mandatory parameter(s) : roleId");
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId;
        ouid = nds.getValue(tempString);
        if(Utils.isNullString(ouid))
        {
            throw new IIPRequestException("Role ID not exsits: " + tempString);
        } else
        {
            nds.removeNode(tempString);
            getClass();
            nds.removeNode("::/AUS_SYSTEM_DIRECTORY/ROLEOUIDMAP" + "/" + ouid);
            return true;
        }
    }

    public boolean setRolesOfUser(ArrayList roleList)
        throws IIPRequestException
    {
        return false;
    }

    public ArrayList listRole()
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/ROLE");
    }

    public ArrayList listUsersOfRole(String roleId)
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId + "/USER");
    }

    public boolean isExistRole(String roleId)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        if(Utils.isNullString(roleId))
            return false;
        getClass();
        tempString = "::/AUS_SYSTEM_DIRECTORY/ROLE" + "/" + roleId;
        ouid = nds.getValue(tempString);
        return !Utils.isNullString(ouid);
    }

    public boolean changePassword(String userId, String oldPassword, String newPassword)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId) || Utils.isNullString(oldPassword) || Utils.isNullString(newPassword))
            throw new IIPRequestException("Miss out mandatory parameter: userId=" + userId + "oldPassword=?, newPassword=?");
        String uid = null;
        String tempString = null;
        String loginTime = null;
        getClass();
        uid = nds.getValue("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId);
        if(Utils.isNullString(uid))
            throw new IIPRequestException("User ID not exists: " + userId);
        getClass();
        tempString = nds.getArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "password");
        try
        {
            loginTime = sdf2.format(new Date());
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        if(!oldPassword.equals(tempString))
        {
            return false;
        } else
        {
            getClass();
            nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "password", newPassword);
            getClass();
            nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "lmdate", loginTime);
            return true;
        }
    }

    public boolean login(String userId, String password)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId) || Utils.isNullString(password))
            throw new IIPRequestException("Miss out mandatory parameter: userId=" + userId + "password=?");
        boolean isLoggedIn = false;
        String tempString = null;
        String loginTime = null;
        String uid = null;
        int count = 0;
        getClass();
        uid = nds.getValue("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId);
        if(Utils.isNullString(uid))
            throw new IIPRequestException("User ID not exists: " + userId);
        HashMap context = null;
        if(loginCheck.containsKey(userId))
        {
            context = (HashMap)loginCheck.get(userId);
            if(context != null)
            {
                InetAddress tempInetAddress = (InetAddress)context.get("inet.address");
                HashMap tempContext = getClientContext(tempInetAddress);
                if(tempContext != null && userId.equals(tempContext.get("userId")))
                    isLoggedIn = true;
                else
                    loginCheck.remove(userId);
            }
        }
        if(isLoggedIn)
        {
            getClass();
            if(!hasRole(userId, "SYSTEM.INTERNAL"))
            {
                getClass();
                if(!hasRole(userId, "SYSTEM.ADMINISTRATOR"))
                    throw new IIPRequestException("Already logged in.");
            }
        }
        getClass();
        tempString = nds.getArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "password");
        try
        {
            loginTime = sdf2.format(new Date());
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        if(!password.equals(tempString))
        {
            getClass();
            if(!nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "lfdate", loginTime))
            {
                getClass();
                nds.addArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "lfdate", loginTime);
            }
            getClass();
            count = Utils.getInt(Utils.getInteger(nds.getArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "failcount")));
            count++;
            getClass();
            if(!nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "failcount", Integer.toString(count)))
            {
                getClass();
                nds.addArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "failcount", Integer.toString(count));
            }
            return false;
        }
        context = getClientContext();
        context.put("userId", userId);
        context.put("uid", uid);
        context.put("time.login", loginTime);
        context.put("client.inet.address", getClientInetAddress());
        getClass();
        if(!nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "lldate", loginTime))
        {
            getClass();
            nds.addArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "lldate", loginTime);
        }
        getClass();
        count = Utils.getInt(Utils.getInteger(nds.getArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "logincount")));
        count++;
        getClass();
        if(!nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "logincount", Integer.toString(count)))
        {
            getClass();
            nds.addArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "logincount", Integer.toString(count));
        }
        loginCheck.put(userId, context);
        context = null;
        return true;
    }

    public boolean resetPassword(String userId)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId))
            throw new IIPRequestException("Miss out mandatory parameter: userId=" + userId);
        String loginTime = null;
        String uid = null;
        getClass();
        uid = nds.getValue("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId);
        if(Utils.isNullString(uid))
            throw new IIPRequestException("User ID not exists: " + userId);
        try
        {
            loginTime = sdf2.format(new Date());
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
        getClass();
        nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "password", userId);
        getClass();
        nds.setArgument("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId, "lmdate", loginTime);
        return true;
    }

    public boolean changeUserStatus(String userId, String status)
        throws IIPRequestException
    {
        return false;
    }

    public boolean logout(String userId)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId))
            throw new IIPRequestException("Miss out mandatory parameter: userId=" + userId);
        String uid = null;
        HashMap tempContext = null;
        HashMap context = null;
        getClass();
        uid = nds.getValue("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId);
        if(Utils.isNullString(uid))
            throw new IIPRequestException("User ID not exists: " + userId);
        tempContext = (HashMap)loginCheck.get(userId);
        if(tempContext == null)
            return false;
        context = getClientContext();
        if(!tempContext.equals(context))
        {
            tempContext = null;
            context = null;
            return false;
        } else
        {
            context.remove("userId");
            context.remove("uid");
            context.remove("time.login");
            loginCheck.remove(userId);
            return true;
        }
    }

    public boolean hasRole(String userId, String roleId)
        throws IIPRequestException
    {
        if(Utils.isNullString(userId) || Utils.isNullString(roleId))
            throw new IIPRequestException("Miss out mandatory parameter: userId=" + userId + ", roleId=" + roleId);
        getClass();
        if(nds.getValue("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId) == null)
            throw new IIPRequestException("User ID not exists: " + userId);
        ArrayList roleList = null;
        getClass();
        roleList = nds.getChildNodeList("::/AUS_SYSTEM_DIRECTORY/USER" + "/" + userId + "/ROLE");
        String tempString = null;
        if(roleList != null && roleList.size() > 0)
        {
            Iterator listKey;
            for(listKey = roleList.iterator(); listKey.hasNext();)
            {
                tempString = (String)listKey.next();
                if(roleId.equals(tempString))
                {
                    listKey = null;
                    roleList.clear();
                    roleList = null;
                    return true;
                }
            }

            listKey = null;
            roleList.clear();
            roleList = null;
        }
        return false;
    }

    private final String NDS_BASE = "::/AUS_SYSTEM_DIRECTORY";
    private final String NDS_USER = "::/AUS_SYSTEM_DIRECTORY/USER";
    private final String NDS_USEROUIDMAP = "::/AUS_SYSTEM_DIRECTORY/USEROUIDMAP";
    private final String NDS_GROUP = "::/AUS_SYSTEM_DIRECTORY/GROUP";
    private final String NDS_GROUPOUIDMAP = "::/AUS_SYSTEM_DIRECTORY/GROUPOUIDMAP";
    private final String NDS_ROLE = "::/AUS_SYSTEM_DIRECTORY/ROLE";
    private final String NDS_ROLEOUIDMAP = "::/AUS_SYSTEM_DIRECTORY/ROLEOUIDMAP";
    private NDS nds;
    private final String ROLE_SYSTEM_INTERNAL = "SYSTEM.INTERNAL";
    private final String ROLE_SYSTEM_ADMINISTRATOR = "SYSTEM.ADMINISTRATOR";
    private final String ROLE_SYSTEM_REVIEW = "SYSTEM.REVIEW";
    private final String ROLE_SYSTEM_APPROVAL = "SYSTEM.APPROVAL";
    private final String GROUP_SYSTEM_INTERNAL = "SYSTEM.INTERNAL";
    private final String GROUP_SYSTEM_ADMINISTRATOR = "SYSTEM.ADMINISTRATOR";
    private final String USER_SYSTEM_INTERNAL = "SYSTEM.INTERNAL";
    private final String USER_SYSTEM_ADMINISTRATOR = "SYSTEM.ADMINISTRATOR";
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private HashMap loginCheck;

}
