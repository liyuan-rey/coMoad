// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WKSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.util.Utils;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package dyna.framework.service:
//            WKS, NDS

public class WKSImpl extends ServiceServer
    implements WKS
{

    public WKSImpl()
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
            if(nds.getValue("::/WORKSPACE") == null)
                nds.addNode("::", "WORKSPACE", "WORKSPACE", "");
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
        tempString = nds.getValue("::/WORKSPACE");
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
        nds.setValue("::/WORKSPACE", tempString);
        return tempString;
    }

    public boolean createNode(HashMap node)
        throws IIPRequestException
    {
        if(node == null || node.size() == 0)
            throw new IIPRequestException("Null arguments.");
        HashMap context = null;
        String modelOuid = null;
        String userId = null;
        String groupId = null;
        String path = null;
        String nodeType = null;
        String workspaceType = null;
        String parent = null;
        String name = null;
        String value = null;
        boolean result = false;
        context = getClientContext();
        modelOuid = (String)context.get("working.model");
        if(Utils.isNullString(modelOuid))
        {
            context = null;
            throw new IIPRequestException("Working model not selected.");
        }
        nodeType = (String)node.get("node.type");
        if(Utils.isNullString(nodeType))
        {
            context = null;
            throw new IIPRequestException("Miss out mandatory parameter(s) : node.type");
        }
        workspaceType = (String)node.get("workspace.type");
        if(Utils.isNullString(workspaceType))
        {
            context = null;
            throw new IIPRequestException("Miss out mandatory parameter(s) : workspace.type");
        }
        parent = (String)node.get("parent");
        if(Utils.isNullString(parent))
        {
            context = null;
            throw new IIPRequestException("Miss out mandatory parameter(s) : parent");
        }
        name = (String)node.get("name");
        if(Utils.isNullString(name))
        {
            context = null;
            throw new IIPRequestException("Miss out mandatory parameter(s) : name");
        }
        value = (String)node.get("value");
        if(Utils.isNullString(value))
        {
            context = null;
            throw new IIPRequestException("Miss out mandatory parameter(s) : value");
        }
        getClass();
        nds.addNode("::/WORKSPACE", modelOuid, "WORKSPACE", modelOuid);
        getClass();
        path = "::/WORKSPACE" + "/" + modelOuid;
        if(workspaceType.equals("PRIVATE"))
        {
            nds.addNode(path, workspaceType, workspaceType, "");
            path = path + "/" + workspaceType;
            userId = (String)context.get("userId");
            nds.addNode(path, userId, workspaceType, "");
            path = path + "/" + userId;
        } else
        if(workspaceType.equals("SHARED"))
        {
            nds.addNode(path, workspaceType, workspaceType, "");
            path = path + "/" + workspaceType;
            groupId = (String)node.get("group.id");
            if(Utils.isNullString(groupId))
                throw new IIPRequestException("Miss out mandatory parameter(s) : " + groupId);
            nds.addNode(path, groupId, workspaceType, "");
            path = path + "/" + groupId;
        } else
        if(workspaceType.equals("PUBLIC"))
        {
            nds.addNode(path, workspaceType, workspaceType, "");
            path = path + "/" + workspaceType;
        } else
        {
            context = null;
            throw new IIPRequestException("Invalid workspace type: " + workspaceType);
        }
        if(parent.equals("/"))
            parent = "";
        if(nodeType.equals("FOLDER"))
        {
            nds.addNode(path, nodeType, nodeType, "");
            path = path + "/" + nodeType;
            path = path + parent;
            result = nds.addNode(path, name, nodeType, value);
            if(!result)
                return false;
        } else
        if(nodeType.equals("FOLDERLINK"))
        {
            nds.addNode(path, nodeType, nodeType, "");
            path = path + "/" + "FOLDER";
            path = path + parent;
            result = nds.addNode(path, name, nodeType, value);
            if(!result)
                return false;
        } else
        if(nodeType.equals("OBJECT"))
        {
            nds.addNode(path, nodeType, nodeType, "");
            path = path + "/" + "FOLDER";
            path = path + parent;
            result = nds.addNode(path, name, nodeType, value);
            if(!result)
                return false;
        } else
        if(nodeType.equals("LISTFILTER"))
        {
            nds.addNode(path, nodeType, nodeType, "");
            path = path + "/" + "FOLDER";
            path = path + parent;
            result = nds.addNode(path, name, nodeType, value);
            if(!result)
                return false;
        } else
        if(nodeType.equals("LINKFILTER"))
        {
            nds.addNode(path, nodeType, nodeType, "");
            path = path + "/" + "FOLDER";
            path = path + parent;
            result = nds.addNode(path, name, nodeType, value);
            if(!result)
                return false;
        } else
        {
            context = null;
            throw new IIPRequestException("Invalid node type: " + nodeType);
        }
        context = null;
        node.clear();
        node = null;
        return result;
    }

    public boolean setNode(HashMap node)
        throws IIPRequestException
    {
        if(node == null || node.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String tempString = null;
        String tempString2 = null;
        String ouid = null;
        tempString = (String)node.get("path");
        if(Utils.isNullString(tempString) || Utils.isNullString((String)node.get("password")))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + node);
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("User ID not exsits: " + tempString);
        tempString = (String)node.get("name");
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
        tempString = (String)node.get("description");
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
        tempString = (String)node.get("primarygroup");
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
        tempString = (String)node.get("duedate");
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
        node.clear();
        node = null;
        return true;
    }

    public boolean removeNode(HashMap node)
        throws IIPRequestException
    {
        String path = null;
        String workspaceType = null;
        String groupId = null;
        path = (String)node.get("path");
        workspaceType = (String)node.get("workspace.type");
        Utils.checkMandatoryString(path, "path");
        Utils.checkMandatoryString(workspaceType, "workspaceType");
        HashMap context = getClientContext();
        String fullpath = null;
        getClass();
        fullpath = "::/WORKSPACE" + "/" + context.get("working.model") + "/" + workspaceType;
        if(workspaceType.equals("PRIVATE"))
            fullpath = fullpath + "/" + context.get("userId") + "/";
        else
        if(workspaceType.equals("SHARED"))
        {
            groupId = (String)node.get("group.id");
            if(Utils.isNullString(groupId))
                Utils.checkMandatoryString(groupId, "groupId");
            fullpath = fullpath + "/" + groupId + "/";
        } else
        if(workspaceType.equals("PUBLIC"))
            fullpath = fullpath + "/";
        if(node.get("node.type").equals("OBJECT"))
            fullpath = fullpath + "FOLDER";
        else
            fullpath = fullpath + node.get("node.type");
        context = null;
        node.clear();
        node = null;
        if(!path.equals("/"))
            fullpath = fullpath + path;
        try
        {
            return nds.removeNode(fullpath);
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public String getNodeValue(HashMap node)
        throws IIPRequestException
    {
        String tempString = null;
        String ouid = null;
        return null;
    }

    public ArrayList getChildNodes(HashMap node)
        throws IIPRequestException
    {
        String path = null;
        String workspaceType = null;
        String groupId = null;
        path = (String)node.get("path");
        workspaceType = (String)node.get("workspace.type");
        Utils.checkMandatoryString(path, "path");
        Utils.checkMandatoryString(workspaceType, "workspaceType");
        HashMap context = getClientContext();
        String fullpath = null;
        getClass();
        fullpath = "::/WORKSPACE" + "/" + context.get("working.model") + "/" + workspaceType;
        if(workspaceType.equals("PRIVATE"))
            fullpath = fullpath + "/" + context.get("userId") + "/";
        else
        if(workspaceType.equals("SHARED"))
        {
            groupId = (String)node.get("group.id");
            if(Utils.isNullString(groupId))
                Utils.checkMandatoryString(groupId, "groupId");
            fullpath = fullpath + "/" + groupId + "/";
        } else
        if(workspaceType.equals("PUBLIC"))
            fullpath = fullpath + "/";
        if(node.get("node.type").equals("FOLDERLINK"))
            fullpath = fullpath + "FOLDER";
        else
            fullpath = fullpath + node.get("node.type");
        context = null;
        if(!path.equals("/"))
            fullpath = fullpath + path;
        if(node.get("node.type").equals("FOLDERLINK"))
            fullpath = nds.getValue(fullpath);
        node.clear();
        node = null;
        String name = null;
        ArrayList list = nds.getChildNodes(fullpath);
        if(list == null)
            return null;
        for(int i = 0; i < list.size(); i++)
        {
            node = (HashMap)list.get(i);
            node.put("parent", fullpath);
            node.put("workspace.type", workspaceType);
            node = null;
        }

        return list;
    }

    public boolean createSharedWorkspace(HashMap workspace)
        throws IIPRequestException
    {
        return false;
    }

    public boolean removeSharedWorkspace(HashMap workspace)
        throws IIPRequestException
    {
        return false;
    }

    public HashMap getSharedWorkspace(HashMap workspace)
        throws IIPRequestException
    {
        return null;
    }

    public boolean setSharedWorkspace(HashMap workspace)
        throws IIPRequestException
    {
        return false;
    }

    public boolean addMemberToSharedWorkspace(HashMap member)
        throws IIPRequestException
    {
        return false;
    }

    public ArrayList getMembersOfSharedWorkspace(HashMap workspace)
        throws IIPRequestException
    {
        return null;
    }

    public boolean removeMemberFromSharedWorkspace(HashMap member)
        throws IIPRequestException
    {
        return false;
    }

    public void setPrivateDefaultFolder(String userId, String path)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(userId, "userId");
        Utils.checkMandatoryString(path, "path");
        getClass();
        String fullPath = "::/WORKSPACE" + "/" + userId;
        getClass();
        nds.addNode("::/WORKSPACE", userId, "Folder", userId);
        nds.removeArgument(fullPath, "defaultFolder");
        nds.addArgument(fullPath, "defaultFolder", path);
    }

    public String getPrivateDefaultFolder(String userId)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(userId, "userId");
        getClass();
        return nds.getArgument("::/WORKSPACE" + "/" + userId, "defaultFolder");
    }

    private final String NDS_BASE = "::/WORKSPACE";
    private NDS nds;
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private HashMap loginCheck;
}
