// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OLMImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.util.Utils;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package dyna.framework.service:
//            OLM, NDS, DOS, DTM, 
//            AUS, DSS

public class OLMImpl extends ServiceServer
    implements OLM
{

    public OLMImpl()
    {
        dtm = null;
        nds = null;
        aus = null;
        dss = null;
        dos = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            nds = (NDS)getServiceInstance("DF30NDS1");
            dos = (DOS)getServiceInstance("DF30DOS1");
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY") == null)
                nds.addNode("::", "OLM_SYSTEM_DIRECTORY", "OLM", "");
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT") == null)
            {
                getClass();
                nds.addNode("::/OLM_SYSTEM_DIRECTORY", "DEFAULT", "OLM", "");
            }
            HashMap state = null;
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CRT") == null)
            {
                state = new HashMap();
                state.put("identifier", "CRT");
                state.put("description", "created");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKO") == null)
            {
                state = new HashMap();
                state.put("identifier", "CKO");
                state.put("description", "checked out");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKI") == null)
            {
                state = new HashMap();
                state.put("identifier", "CKI");
                state.put("description", "checked in");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RV1") == null)
            {
                state = new HashMap();
                state.put("identifier", "RV1");
                state.put("description", "reviewing");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RV2") == null)
            {
                state = new HashMap();
                state.put("identifier", "RV2");
                state.put("description", "reviewed");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP1") == null)
            {
                state = new HashMap();
                state.put("identifier", "AP1");
                state.put("description", "approving");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP2") == null)
            {
                state = new HashMap();
                state.put("identifier", "AP2");
                state.put("description", "approved");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/WIP") == null)
            {
                state = new HashMap();
                state.put("identifier", "WIP");
                state.put("description", "work in progress");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RLS") == null)
            {
                state = new HashMap();
                state.put("identifier", "RLS");
                state.put("description", "released");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/REJ") == null)
            {
                state = new HashMap();
                state.put("identifier", "REJ");
                state.put("description", "rejected");
                create(state);
                state.clear();
                state = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/OBS") == null)
            {
                state = new HashMap();
                state.put("identifier", "OBS");
                state.put("description", "obsoleted");
                create(state);
                state.clear();
                state = null;
            }
            HashMap link = null;
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CRT/CKO") == null)
            {
                link = new HashMap();
                link.put("from.id", "CRT");
                link.put("to.id", "CKO");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CRT/RV1") == null)
            {
                link = new HashMap();
                link.put("from.id", "CRT");
                link.put("to.id", "RV1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CRT/AP1") == null)
            {
                link = new HashMap();
                link.put("from.id", "CRT");
                link.put("to.id", "AP1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKO/CKI") == null)
            {
                link = new HashMap();
                link.put("from.id", "CKO");
                link.put("to.id", "CKI");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKO/CRT") == null)
            {
                link = new HashMap();
                link.put("from.id", "CKO");
                link.put("to.id", "CRT");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKO/WIP") == null)
            {
                link = new HashMap();
                link.put("from.id", "CKO");
                link.put("to.id", "WIP");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKI/CKO") == null)
            {
                link = new HashMap();
                link.put("from.id", "CKI");
                link.put("to.id", "CKO");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKI/RV1") == null)
            {
                link = new HashMap();
                link.put("from.id", "CKI");
                link.put("to.id", "RV1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/CKI/AP1") == null)
            {
                link = new HashMap();
                link.put("from.id", "CKI");
                link.put("to.id", "AP1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RVI/RV2") == null)
            {
                link = new HashMap();
                link.put("from.id", "RV1");
                link.put("to.id", "RV2");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RVI/REJ") == null)
            {
                link = new HashMap();
                link.put("from.id", "RV1");
                link.put("to.id", "REJ");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RVI/RV1") == null)
            {
                link = new HashMap();
                link.put("from.id", "RV1");
                link.put("to.id", "RV1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RV2/AP1") == null)
            {
                link = new HashMap();
                link.put("from.id", "RV2");
                link.put("to.id", "AP1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/RV2/WIP") == null)
            {
                link = new HashMap();
                link.put("from.id", "RV2");
                link.put("to.id", "WIP");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP1/AP2") == null)
            {
                link = new HashMap();
                link.put("from.id", "AP1");
                link.put("to.id", "AP2");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP1/REJ") == null)
            {
                link = new HashMap();
                link.put("from.id", "AP1");
                link.put("to.id", "REJ");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP2/RLS") == null)
            {
                link = new HashMap();
                link.put("from.id", "AP2");
                link.put("to.id", "RLS");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP2/OBS") == null)
            {
                link = new HashMap();
                link.put("from.id", "AP2");
                link.put("to.id", "OBS");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/AP2/WIP") == null)
            {
                link = new HashMap();
                link.put("from.id", "AP2");
                link.put("to.id", "WIP");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/WIP/CKO") == null)
            {
                link = new HashMap();
                link.put("from.id", "WIP");
                link.put("to.id", "CKO");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/WIP/RV1") == null)
            {
                link = new HashMap();
                link.put("from.id", "WIP");
                link.put("to.id", "RV1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/WIP/AP1") == null)
            {
                link = new HashMap();
                link.put("from.id", "WIP");
                link.put("to.id", "AP1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/REJ/RV1") == null)
            {
                link = new HashMap();
                link.put("from.id", "REJ");
                link.put("to.id", "RV1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/REJ/AP1") == null)
            {
                link = new HashMap();
                link.put("from.id", "REJ");
                link.put("to.id", "AP1");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/REJ/WIP") == null)
            {
                link = new HashMap();
                link.put("from.id", "REJ");
                link.put("to.id", "WIP");
                link(link);
                link.clear();
                link = null;
            }
            getClass();
            if(nds.getValue("::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/REJ/CKI") == null)
            {
                link = new HashMap();
                link.put("from.id", "REJ");
                link.put("to.id", "CKI");
                link(link);
                link.clear();
                link = null;
            }
            dos.setOLMSerivceInstance(this);
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

    public boolean create(HashMap state)
        throws IIPRequestException
    {
        if(state == null || state.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String identifier = null;
        String classOuid = null;
        String tempString = null;
        String tempString2 = null;
        identifier = (String)state.get("identifier");
        if(Utils.isNullString(identifier))
            throw new IIPRequestException("Miss out mandatory field: identifier");
        classOuid = (String)state.get("ouid@class");
        if(Utils.isNullString(classOuid))
        {
            getClass();
            tempString2 = "::/OLM_SYSTEM_DIRECTORY/DEFAULT";
        } else
        {
            getClass();
            tempString2 = "::/OLM_SYSTEM_DIRECTORY" + "/" + classOuid;
        }
        tempString = nds.getValue(tempString2 + "/" + identifier);
        if(!Utils.isNullString(tempString))
            return false;
        nds.addNode(tempString2, identifier, "OLM.STATE", identifier);
        tempString2 = tempString2 + "/" + identifier;
        tempString = (String)state.get("description");
        if(!Utils.isNullString(tempString))
            nds.addArgument(tempString2, "description", tempString);
        tempString = sdf2.format(new Date());
        nds.addArgument(tempString2, "cdate", tempString);
        nds.addArgument(tempString2, "lmdate", tempString);
        return true;
    }

    public boolean remove(String classOuid, String identifier)
        throws IIPRequestException
    {
        String tempString = null;
        if(Utils.isNullString(identifier))
            throw new IIPRequestException("Miss out mandatory parameter: identifier");
        if(Utils.isNullString(classOuid))
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/" + identifier;
        } else
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY" + "/" + classOuid + "/" + identifier;
        }
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            throw new IIPRequestException("Storage not exsits: " + identifier);
        } else
        {
            nds.removeNode(tempString);
            return true;
        }
    }

    public boolean remove(String identifier)
        throws IIPRequestException
    {
        return remove("", identifier);
    }

    public HashMap get(String classOuid, String identifier)
        throws IIPRequestException
    {
        if(Utils.isNullString(identifier))
            throw new IIPRequestException("Miss out mandatory parameter: identifier");
        if(nds.getValue(identifier) == null)
            return null;
        else
            return nds.getArguments(identifier);
    }

    public HashMap get(String identifier)
        throws IIPRequestException
    {
        return get("", identifier);
    }

    public boolean set(HashMap state)
        throws IIPRequestException
    {
        if(state == null || state.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String identifier = null;
        String tempString = null;
        String tempString2 = null;
        identifier = (String)state.get("identifier");
        if(Utils.isNullString(identifier))
            throw new IIPRequestException("Miss out mandatory parameter: identifier");
        tempString = (String)state.get("ouid@class");
        if(Utils.isNullString(tempString))
        {
            getClass();
            tempString2 = "::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/" + identifier;
        } else
        {
            getClass();
            tempString2 = "::/OLM_SYSTEM_DIRECTORY" + "/" + tempString + "/" + identifier;
        }
        if(nds.getValue(tempString2) == null)
            throw new IIPRequestException("State not exists: " + tempString);
        tempString = (String)state.get("description");
        if(!Utils.isNullString(tempString))
        {
            if(nds.getArgument(tempString2, "description") == null)
                nds.addArgument(tempString2, "description", tempString);
            else
                nds.setArgument(tempString2, "description", tempString);
            nds.setValue(tempString2, tempString);
        } else
        {
            nds.removeArgument(tempString2, "description");
        }
        tempString = sdf2.format(new Date());
        if(nds.getArgument(tempString2, "mdate") == null)
            nds.addArgument(tempString2, "mdate", tempString);
        else
            nds.setArgument(tempString2, "mdate", tempString);
        state = null;
        return true;
    }

    public ArrayList list(String classOuid)
        throws IIPRequestException
    {
        String tempString = null;
        ArrayList returnList = new ArrayList();
        if(Utils.isNullString(classOuid))
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY/DEFAULT";
        } else
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY" + "/" + classOuid;
        }
        returnList = nds.getChildNodes(tempString);
        return returnList;
    }

    public ArrayList list()
        throws IIPRequestException
    {
        return list("");
    }

    public ArrayList listClass()
        throws IIPRequestException
    {
        getClass();
        return nds.getChildNodeList("::/OLM_SYSTEM_DIRECTORY");
    }

    public boolean link(HashMap link)
        throws IIPRequestException
    {
        if(link == null || link.size() == 0)
            throw new IIPRequestException("Null arguments.");
        String fromIdentifier = null;
        String toIdentifier = null;
        String classOuid = null;
        String tempString = null;
        String tempString2 = null;
        fromIdentifier = (String)link.get("from.id");
        if(Utils.isNullString(fromIdentifier))
            throw new IIPRequestException("Miss out mandatory field: from.id");
        toIdentifier = (String)link.get("to.id");
        if(Utils.isNullString(toIdentifier))
            throw new IIPRequestException("Miss out mandatory field: to.id");
        classOuid = (String)link.get("ouid@class");
        if(Utils.isNullString(classOuid))
        {
            getClass();
            tempString2 = "::/OLM_SYSTEM_DIRECTORY/DEFAULT";
        } else
        {
            getClass();
            tempString2 = "::/OLM_SYSTEM_DIRECTORY" + "/" + classOuid;
        }
        tempString = nds.getValue(tempString2 + "/" + fromIdentifier + "/" + toIdentifier);
        if(!Utils.isNullString(tempString))
        {
            return false;
        } else
        {
            tempString2 = tempString2 + "/" + fromIdentifier;
            nds.addNode(tempString2, toIdentifier, "OLM.LINK", fromIdentifier);
            tempString2 = tempString2 + "/" + toIdentifier;
            tempString = sdf2.format(new Date());
            nds.addArgument(tempString2, "cdate", tempString);
            return true;
        }
    }

    public boolean unlink(HashMap link)
        throws IIPRequestException
    {
        String tempString = null;
        String fromIdentifier = null;
        String toIdentifier = null;
        String classOuid = null;
        fromIdentifier = (String)link.get("from.id");
        if(Utils.isNullString(fromIdentifier))
            throw new IIPRequestException("Miss out mandatory parameter: from.id");
        toIdentifier = (String)link.get("to.id");
        if(Utils.isNullString(toIdentifier))
            throw new IIPRequestException("Miss out mandatory parameter: to.id");
        classOuid = (String)link.get("ouid@class");
        if(Utils.isNullString(classOuid))
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/" + fromIdentifier + "/" + toIdentifier;
        } else
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY" + "/" + classOuid + "/" + fromIdentifier + "/" + toIdentifier;
        }
        if(Utils.isNullString(nds.getValue(tempString)))
        {
            throw new IIPRequestException("Link not exsits: " + fromIdentifier + " -> " + toIdentifier);
        } else
        {
            nds.removeNode(tempString);
            return true;
        }
    }

    public ArrayList listLink(String classOuid, String identifier)
        throws IIPRequestException
    {
        if(Utils.isNullString(identifier))
            throw new IIPRequestException("Miss out mandatory parameter: identifier");
        String tempString = null;
        ArrayList returnList = new ArrayList();
        if(Utils.isNullString(classOuid))
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY/DEFAULT" + "/" + identifier;
        } else
        {
            getClass();
            tempString = "::/OLM_SYSTEM_DIRECTORY" + "/" + classOuid + "/" + identifier;
        }
        returnList = nds.getChildNodes(tempString);
        return returnList;
    }

    public ArrayList listLink(String identifier)
        throws IIPRequestException
    {
        return listLink("", identifier);
    }

    private DTM dtm;
    private NDS nds;
    private AUS aus;
    private DSS dss;
    private DOS dos;
    private final String NDS_BASE = "::/OLM_SYSTEM_DIRECTORY";
    private final String NDS_DEFAULT = "::/OLM_SYSTEM_DIRECTORY/DEFAULT";
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
