// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NDSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.nds.NDSNode;
import dyna.util.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.PooledConnection;
import org.xml.sax.Attributes;

// Referenced classes of package dyna.framework.service:
//            NDS, DTM

public class NDSImpl extends ServiceServer
    implements NDS, XMLImportable
{

    public NDSImpl()
    {
        subSet = null;
        rootNode = null;
        allNodes = null;
        isReady = false;
        tempParent = null;
        tempKey = null;
        tempType = null;
        tempValue = null;
        dtm = null;
        subSet = Collections.synchronizedMap(new HashMap());
        allNodes = Collections.synchronizedMap(new HashMap());
        rootNode = new NDSNode();
        rootNode.key = "::";
        rootNode.type = "root";
        rootNode.value = null;
        rootNode.children = null;
        rootNode.parentNode = null;
        subSet.put(rootNode.key, rootNode);
        allNodes.put(rootNode.key, rootNode);
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dtm = (DTM)getServiceInstance("DF30DTM1");
            System.out.print('.');
            System.out.print("(" + loadFromDatabase() + " nodes)");
            System.out.print('.');
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY"))
                addNode("::", "CODE_SYSTEM_DIRECTORY", "CODE", "CODE");
            if(!subSet.containsKey("CODE"))
            {
                getClass();
                addSubSet("CODE", "::/CODE_SYSTEM_DIRECTORY");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY", "STATUS", "CODE", "CODE");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/CRT"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "CRT", "CODE", "created");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/CRT", "ouid", "1001");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/OBS"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "OBS", "CODE", "obsoleted");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/OBS", "ouid", "1002");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/CKI"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "CKI", "CODE", "checked in");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/CKI", "ouid", "1003");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/CKO"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "CKO", "CODE", "checked out");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/CKO", "ouid", "1004");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/RLS"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "RLS", "CODE", "released");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/RLS", "ouid", "1005");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/WIP"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "WIP", "CODE", "work in progress");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/WIP", "ouid", "1006");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/RV1"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "RV1", "CODE", "reviewing");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/RV1", "ouid", "1007");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/RV2"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "RV2", "CODE", "reviewed");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/RV2", "ouid", "1008");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/AP1"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "AP1", "CODE", "approving");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/AP1", "ouid", "1009");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/AP2"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "AP2", "CODE", "approved");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/AP2", "ouid", "1010");
            }
            getClass();
            if(!allNodes.containsKey("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/REJ"))
            {
                getClass();
                addNode("::/CODE_SYSTEM_DIRECTORY/STATUS", "REJ", "CODE", "rejected");
                getClass();
                addArgument("::/CODE_SYSTEM_DIRECTORY/STATUS" + "/REJ", "ouid", "1011");
            }
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        XMLImport("conf/nds");
    }

    private void addNodeForLoad(String parent, String key, String type, Object value)
        throws IIPRequestException
    {
        NDSNode parentNode = null;
        NDSNode node = null;
        if(Utils.isNullString(key) || Utils.isNullString(type))
            return;
        if(Utils.isNullString(parent))
            parentNode = rootNode;
        else
            parentNode = (NDSNode)allNodes.get(parent);
        if(parentNode == null)
            return;
        if(parentNode.children != null)
        {
            node = (NDSNode)parentNode.children.get(key);
            if(node != null)
                return;
        }
        node = new NDSNode();
        node.parentNode = parentNode;
        node.key = key;
        node.type = type;
        node.value = value;
        node.path = parent;
        synchronized(this)
        {
            if(parentNode.children == null)
                parentNode.children = Collections.synchronizedSortedMap(new TreeMap());
            parentNode.children.put(node.key, node);
            allNodes.put(node.path + '/' + node.key, node);
        }
        node = null;
        parentNode = null;
    }

    public boolean addNode(String parent, String key, String type, String value)
        throws IIPRequestException
    {
        NDSNode parentNode;
        parentNode = null;
        NDSNode node = null;
        if(Utils.isNullString(key) || Utils.isNullString(type))
            return false;
        if(Utils.isNullString(parent))
            parentNode = rootNode;
        else
            parentNode = (NDSNode)allNodes.get(parent);
        if(parentNode == null)
            return false;
        if(parentNode.children != null)
        {
            node = (NDSNode)parentNode.children.get(key);
            if(node != null)
                return false;
        }
        NDSImpl ndsimpl = this;
        JVM INSTR monitorenter ;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into ndsnode (path,parent,key,type,des) values (?,?,?,?,?) ");
            stat.setString(1, parent + '/' + key);
            stat.setString(2, parent);
            stat.setString(3, key);
            stat.setString(4, type);
            stat.setString(5, value);
            stat.executeUpdate();
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
        NDSNode node = new NDSNode();
        node.parentNode = parentNode;
        node.key = key;
        node.type = type;
        node.value = value;
        node.path = parent;
        if(parentNode.children == null)
            parentNode.children = Collections.synchronizedSortedMap(new TreeMap());
        parentNode.children.put(node.key, node);
        allNodes.put(node.path + '/' + node.key, node);
          goto _L1
        ndsimpl;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        NDSNode node = null;
        parentNode = null;
        return true;
    }

    public boolean removeNode(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return false;
        NDSNode node = null;
        NDSImpl ndsimpl = this;
        JVM INSTR monitorenter ;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from ndsattr where path like ?||'%' ");
            stat.setString(1, path);
            stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from ndsnode where path like ?||'%' ");
            stat.setString(1, path);
            stat.executeUpdate();
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
        node = (NDSNode)allNodes.remove(path);
        if(node != null && node.parentNode != null)
            break MISSING_BLOCK_LABEL_223;
        return false;
        node.parentNode.children.remove(node.key);
        if(node.parentNode.children.isEmpty())
            node.parentNode.children = null;
        ndsimpl;
        JVM INSTR monitorexit ;
          goto _L1
        ndsimpl;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        return true;
    }

    public HashMap getNode(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
        {
            return null;
        } else
        {
            HashMap nodeMap = new HashMap();
            nodeMap.put("name", node.key);
            nodeMap.put("type", node.type);
            nodeMap.put("value", node.value);
            return nodeMap;
        }
    }

    public String getValue(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
            return null;
        else
            return (String)node.value;
    }

    public boolean setValue(String path, String value)
        throws IIPRequestException
    {
        NDSNode node;
        if(Utils.isNullString(path))
            return false;
        node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
            return false;
        NDSImpl ndsimpl = this;
        JVM INSTR monitorenter ;
        int rows;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        rows = 0;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update ndsnode set des=? where path=? ");
            stat.setString(1, value);
            stat.setString(2, path);
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
        if(rows > 0)
        {
            node.value = value;
            break MISSING_BLOCK_LABEL_207;
        }
        return false;
        ndsimpl;
        JVM INSTR monitorexit ;
          goto _L1
        ndsimpl;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        return true;
    }

    public ArrayList getChildNodeList(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        ArrayList returnList = null;
        if(node == null || node.children == null)
            return null;
        returnList = new ArrayList();
        for(Iterator children = node.children.keySet().iterator(); children.hasNext(); returnList.add(children.next()));
        return returnList;
    }

    public ArrayList getChildNodes(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        NDSNode childNode = null;
        HashMap nodeMap = null;
        ArrayList returnList = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null || node.children == null)
            return null;
        returnList = new ArrayList();
        for(Iterator children = node.children.keySet().iterator(); children.hasNext();)
        {
            childNode = (NDSNode)node.children.get(children.next());
            nodeMap = new HashMap();
            nodeMap.put("name", childNode.key);
            nodeMap.put("type", childNode.type);
            nodeMap.put("value", childNode.value);
            nodeMap.put("description", childNode.description);
            returnList.add(nodeMap);
            childNode = null;
            nodeMap = null;
        }

        return returnList;
    }

    private void addArgumentForLoad(String path, String name, Object value)
        throws IIPRequestException
    {
        if(Utils.isNullString(path) || Utils.isNullString(name))
            return;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
            return;
        synchronized(this)
        {
            if(node.attributes == null)
                node.attributes = Collections.synchronizedMap(new TreeMap());
            node.attributes.put(name, value);
        }
    }

    public boolean addArgument(String path, String name, String value)
        throws IIPRequestException
    {
        NDSNode node;
        if(Utils.isNullString(path) || Utils.isNullString(name))
            return false;
        node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
            return false;
        NDSImpl ndsimpl = this;
        JVM INSTR monitorenter ;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into ndsattr (path,key,des) values (?,?,?) ");
            stat.setString(1, path);
            stat.setString(2, name);
            stat.setString(3, value);
            stat.executeUpdate();
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
        if(node.attributes == null)
            node.attributes = Collections.synchronizedMap(new TreeMap());
        node.attributes.put(name, value);
          goto _L1
        ndsimpl;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        return true;
    }

    public boolean removeArgument(String path, String name)
        throws IIPRequestException
    {
        NDSNode node;
        if(Utils.isNullString(path) || Utils.isNullString(name))
            return false;
        node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null || node.attributes == null)
            return false;
        NDSImpl ndsimpl = this;
        JVM INSTR monitorenter ;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from ndsattr where path=? and key=? ");
            stat.setString(1, path);
            stat.setString(2, name);
            stat.executeUpdate();
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
        node.attributes.remove(name);
        if(node.attributes.isEmpty())
            node.attributes = null;
          goto _L1
        ndsimpl;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        return true;
    }

    public String getArgument(String path, String name)
        throws IIPRequestException
    {
        if(Utils.isNullString(path) || Utils.isNullString(name))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null || node.attributes == null)
            return null;
        else
            return (String)node.attributes.get(name);
    }

    public boolean setArgument(String path, String name, String value)
        throws IIPRequestException
    {
        NDSNode node;
        int rows;
        if(Utils.isNullString(path) || Utils.isNullString(name))
            return false;
        node = null;
        node = (NDSNode)allNodes.get(path);
        if(node == null || node.attributes == null)
            return false;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        rows = 0;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update ndsattr set des=? where path=? and key=? ");
            stat.setString(1, value);
            stat.setString(2, path);
            stat.setString(3, name);
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
        if(rows > 0)
            node.attributes.put(name, value);
        else
            return false;
        return true;
    }

    public ArrayList getArgumentNameList(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        ArrayList returnList = null;
        if(node == null || node.attributes == null)
            return null;
        returnList = new ArrayList();
        for(Iterator attributes = node.attributes.keySet().iterator(); attributes.hasNext(); returnList.add(attributes.next()));
        return returnList;
    }

    public ArrayList getArgumentValueList(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        ArrayList returnList = null;
        if(node == null || node.attributes == null)
            return null;
        returnList = new ArrayList();
        for(Iterator attributes = node.attributes.keySet().iterator(); attributes.hasNext(); returnList.add(node.attributes.get(attributes.next())));
        return returnList;
    }

    public HashMap getArguments(String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path))
            return null;
        NDSNode node = null;
        node = (NDSNode)allNodes.get(path);
        HashMap returnMap = null;
        if(node == null || node.attributes == null || node.attributes.size() == 0)
        {
            return null;
        } else
        {
            returnMap = new HashMap(node.attributes);
            return returnMap;
        }
    }

    public boolean renameNode(String path, String newKey)
        throws IIPRequestException
    {
        String oldKey;
        NDSNode node;
        NDSNode parentNode;
        if(Utils.isNullString(path))
            return false;
        if(allNodes.containsKey(newKey))
            return false;
        oldKey = null;
        node = null;
        parentNode = null;
        node = (NDSNode)allNodes.get(path);
        oldKey = node.key;
        node.key = newKey;
        allNodes.remove(oldKey);
        allNodes.put(newKey, node);
        parentNode = node.parentNode;
        if(parentNode == null)
            return false;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update ndsnode set path=?, key=? where path=? ");
            stat.setString(1, parentNode.path + '/' + newKey);
            stat.setString(2, newKey);
            stat.setString(3, path);
            stat.executeUpdate();
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
        parentNode.children.remove(oldKey);
        parentNode.children.put(newKey, node);
        node = null;
        parentNode = null;
        return true;
    }

    public boolean addSubSetForLoad(String key, String path)
        throws IIPRequestException
    {
        if(Utils.isNullString(path) || Utils.isNullString(key))
            return false;
        NDSNode node = (NDSNode)allNodes.get(path);
        if(node == null)
        {
            return false;
        } else
        {
            subSet.put(key, node);
            node = null;
            return true;
        }
    }

    public boolean addSubSet(String key, String path)
        throws IIPRequestException
    {
        NDSNode node;
        if(Utils.isNullString(path) || Utils.isNullString(key))
            return false;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
            return false;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into ndsset (key,path) values (?,?) ");
            stat.setString(1, key);
            stat.setString(2, path);
            stat.executeUpdate();
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
        subSet.put(key, node);
        node = null;
        return true;
    }

    public boolean setSubSet(String key, String path)
        throws IIPRequestException
    {
        NDSNode node;
        if(Utils.isNullString(path) || Utils.isNullString(key))
            return false;
        node = (NDSNode)allNodes.get(path);
        if(node == null)
            return false;
        if(!subSet.containsKey(key))
            return false;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update ndsset set path=? where key=? ");
            stat.setString(1, path);
            stat.setString(2, key);
            stat.executeUpdate();
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
        subSet.put(key, node);
        node = null;
        return true;
    }

    public String getSubSet(String key)
        throws IIPRequestException
    {
        if(Utils.isNullString(key))
            return null;
        NDSNode node = (NDSNode)subSet.get(key);
        if(node == null)
            return null;
        else
            return node.path + "/" + node.key;
    }

    public String findKeyValue(String basePath, String value)
        throws IIPRequestException
    {
        if(Utils.isNullString(basePath) || Utils.isNullString(value))
            return null;
        NDSNode node = (NDSNode)allNodes.get(basePath);
        NDSNode tempNode = null;
        if(node == null || node.children == null)
            return null;
        String tempString = null;
        Map children = node.children;
        Iterator mapKey = children.keySet().iterator();
        node = null;
        while(mapKey.hasNext()) 
        {
            tempString = (String)mapKey.next();
            tempNode = (NDSNode)children.get(tempString);
            if(tempNode.value.equals(value))
            {
                tempString = tempNode.path + '/' + tempNode.key;
                tempNode = null;
                mapKey = null;
                children = null;
                return tempString;
            }
            tempNode = null;
            tempString = null;
        }
        mapKey = null;
        children = null;
        return null;
    }

    public void startImport()
    {
        isReady = false;
    }

    public void startElement(String name, Attributes attrs)
    {
        tempParent = null;
        tempKey = null;
        tempType = null;
        tempValue = null;
    }

    public void endElement(String name)
    {
        try
        {
            if(!addNode(tempParent, tempKey, tempType, tempValue))
                setValue(tempParent + "/" + tempKey, tempValue);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public void setElementData(String name, String value)
    {
        if(name.equals("node.path"))
            tempParent = new String(value);
        else
        if(name.equals("node.key"))
            tempKey = new String(value);
        else
        if(name.equals("node.type"))
            tempType = new String(value);
        else
        if(name.equals("node.value"))
            tempValue = new String(value);
    }

    public void endImport()
    {
        isReady = true;
    }

    public void XMLImport(String fileName)
    {
        File tempFile = new File(fileName + ".xml");
        if(!tempFile.canRead())
        {
            return;
        } else
        {
            XMLUtil.XMLImport(fileName, this, "node");
            return;
        }
    }

    public void save()
    {
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("nds.dat")));
            out.writeObject(rootNode);
            out.writeObject(allNodes);
            out.writeObject(subSet);
            out.flush();
            out.close();
            out = null;
        }
        catch(FileNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public void load()
    {
        try
        {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("nds.dat")));
            rootNode = (NDSNode)in.readObject();
            allNodes = (Map)in.readObject();
            subSet = (Map)in.readObject();
            in.close();
            in = null;
        }
        catch(ClassNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(FileNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }

    public int loadFromDatabase()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        int rows;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        rows = 0;
        SQLException e;
        int i;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select no.parent, no.key, no.type, no.des from ndsnode no order by no.parent,no.key");
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                addNodeForLoad(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                rows++;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select na.path, na.key, na.des from ndsattr na order by na.path,na.key");
            for(rs = stat.executeQuery(); rs.next(); addArgumentForLoad(rs.getString(1), rs.getString(2), rs.getString(3)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select ns.key, ns.path from ndsset ns order by ns.key");
            for(rs = stat.executeQuery(); rs.next(); addSubSetForLoad(rs.getString(1), rs.getString(2)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            i = rows;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return i;
        e;
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return rows;
    }

    private Map subSet;
    private NDSNode rootNode;
    private Map allNodes;
    private boolean isReady;
    private String tempParent;
    private String tempKey;
    private String tempType;
    private String tempValue;
    private DTM dtm;
    private final String CODE_BASE = "::/CODE_SYSTEM_DIRECTORY";
    private final String CODE_STATUS = "::/CODE_SYSTEM_DIRECTORY/STATUS";
}
