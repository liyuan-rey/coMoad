// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CAD.java

package dyna.adapter;

import dyna.framework.client.*;
import dyna.util.CADIntegrationUtils;
import dyna.util.Session;
import java.io.File;
import java.util.HashMap;

// Referenced classes of package dyna.adapter:
//            Adapter

public class CAD extends Adapter
{

    public CAD()
    {
    }

    public String displayLogin(String sessionId)
    {
        Session session = getSession(sessionId);
        String returnValue = null;
        if(session == null)
            return "-ERR Session not found.";
        String property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        session.removeProperty("dynapdm.login");
        LogIn login = new LogIn();
        login.setSession(session);
        login.setVisible(true);
        login.toFront();
        for(; returnValue == null; returnValue = (String)session.getProperty("dynapdm.login"))
            try
            {
                Thread.sleep(500L);
            }
            catch(InterruptedException interruptedexception) { }

        if(returnValue.equals("true"))
        {
            if(session.getProperty("dynapdm.part.insert.status") == null)
                session.setProperty("dynapdm.part.insert.status", "closed");
            if(session.getProperty("dynapdm.part.search.status") == null)
                session.setProperty("dynapdm.part.search.status", "closed");
            if(session.getProperty("dynapdm.drawing.search.status") == null)
                session.setProperty("dynapdm.drawing.search.status", "closed");
            if(session.getProperty("dynapdm.assembly") == null)
                session.setProperty("dynapdm.assembly", "false");
            return "+OK User " + session.getProperty("dynapdm.user.id") + " logged in.";
        } else
        {
            return "-ERR Log-in canceled.";
        }
    }

    public String displayModelSelect(String sessionId)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-ERR Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
        {
            return "-ERR Not logged in.";
        } else
        {
            ModelSelectWindow modelSelect = new ModelSelectWindow(null);
            modelSelect.setVisible(true);
            modelSelect.toFront();
            return "+OK.";
        }
    }

    public String displaySearch(String sessionId, String cadType)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-ERR Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
            return "-ERR Not logged in.";
        Search4CADIntegration search4CADIntegration = null;
        search4CADIntegration = (Search4CADIntegration)session.getProperty("dynapdm.part.search.instance");
        if(search4CADIntegration == null)
            search4CADIntegration = new Search4CADIntegration(session, cadType);
        search4CADIntegration.setVisible(true);
        search4CADIntegration.toFront();
        session.setProperty("dynapdm.part.search.status", "open");
        session.setProperty("dynapdm.part.search.instance", search4CADIntegration);
        DynaMOAD.startFromCAD = true;
        return "+OK.";
    }

    public String writeAttributeInitFile(String sessionId, String CADType)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-ERR Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
        {
            return "-ERR Not logged in.";
        } else
        {
            CADIntegrationUtils cadUtils = new CADIntegrationUtils(Integer.parseInt(CADType), null);
            cadUtils.writeInitFile(1, "attribute.ini", null);
            return "+OK.";
        }
    }

    public String displayModelInsert(String sessionId, String CADType)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-ERR Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
        {
            return "-ERR Not logged in.";
        } else
        {
            PartInsert4CADIntegration modelInsert = new PartInsert4CADIntegration(null, session, "modelinsert.ini", Integer.parseInt(CADType));
            modelInsert.setVisible(true);
            modelInsert.toFront();
            return "+OK.";
        }
    }

    public String displayCheckIn(String sessionId, String iniPath, String CADType, String wrkType)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-ERR Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
        {
            return "-ERR Not logged in.";
        } else
        {
            PartInsert4CADIntegration modelInsert = new PartInsert4CADIntegration(null, session, iniPath, Integer.parseInt(CADType));
            modelInsert.setWorkType(Integer.parseInt(wrkType));
            modelInsert.setVisible(true);
            modelInsert.toFront();
            return "+OK.";
        }
    }

    public String displayCheckIn(String sessionId, String filePath, String CADType)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-ERR Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-ERR Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
            return "-ERR Not logged in.";
        String fileName = (String)session.getProperty("dynapdm.cadintegration.drawing");
        if(fileName == null)
            return "-ERR Not checked-out.(1)";
        HashMap checkOutFileInfo = (HashMap)session.getProperty("dynapdm.cadintegration.checkoutfileinfo");
        if(checkOutFileInfo == null)
            return "-ERR Not checked-out.(2)";
        String checkedOutFilePath = (String)checkOutFileInfo.get("md$description");
        File checkedOutFile = new File(checkedOutFilePath);
        File checkInFile = new File(filePath);
        if(!checkedOutFile.getName().equalsIgnoreCase(checkInFile.getName()))
        {
            return "-ERR File name is different from checked-out file.";
        } else
        {
            CheckIn checkIn = new CheckIn(null, false, checkOutFileInfo);
            checkIn.setSession(session);
            checkIn.setPreselectedFilePath(filePath);
            checkIn.setVisible(true);
            return "+OK.";
        }
    }

    public String displayPartInsert(String sessionId, String CADType)
    {
        Session session = getSession(sessionId);
        String property = null;
        if(session == null)
            return "-Err Session not found.";
        property = (String)session.getProperty("current.status");
        if(property == null || property.equals("close"))
            return "-Err Session not opened.";
        property = (String)session.getProperty("dynapdm.login");
        if(property == null || property.equals("false"))
        {
            return "-Err Not logged in.";
        } else
        {
            CADIntegrationUtils cadUtils = new CADIntegrationUtils(Integer.parseInt(CADType), null);
            UIGeneration uiGeneration = new UIGeneration(null, cadUtils.cadFileClassOuid);
            uiGeneration.setSession(session);
            uiGeneration.show();
            uiGeneration.toFront();
            session.setProperty("dynapdm.part.insert.status", "open");
            DynaMOAD.startFromCAD = true;
            return "+OK.";
        }
    }

    public String saveAssemblyStructure(String sessionId)
    {
        return "+OK.";
    }
}