// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WFMImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.DTMUtil;
import dyna.util.Utils;
import java.io.PrintStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service:
//            WFM, DTM, NDS, AUS, 
//            DOS, DSS

public class WFMImpl extends ServiceServer
    implements WFM
{

    public WFMImpl()
    {
        unitPriority = null;
        unitCost = null;
        modelOuid = null;
        dtm = null;
        nds = null;
        aus = null;
        dos = null;
        dss = null;
        NDS_CODE = null;
        calendar = new GregorianCalendar();
        modelMap = null;
        processMap = null;
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dtm = (DTM)getServiceInstance("DF30DTM1");
            nds = (NDS)getServiceInstance("DF30NDS1");
            aus = (AUS)getServiceInstance("DF30AUS1");
            dos = (DOS)getServiceInstance("DF30DOS1");
            dss = (DSS)getServiceInstance("DF30DSS1");
            getClass();
            if(nds.getValue("::/WFM_SYSTEM_DIRECTORY") == null)
            {
                nds.addNode("::", "WFM_SYSTEM_DIRECTORY", "WFM", "");
                getClass();
                nds.addNode("::/WFM_SYSTEM_DIRECTORY", "MODEL", "WFM", "MODEL");
            }
            getClass();
            if(nds.getValue("::/WFM_SYSTEM_DIRECTORY/STATUSMAP") == null)
            {
                getClass();
                nds.addNode("::/WFM_SYSTEM_DIRECTORY", "STATUSMAP", "WFM.STATUSMAP", "STATUSMAP");
            }
            getModelDefinition();
            loadProcessMap();
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

    public DOSChangeable getModelDefinition()
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        return null;
    }

    public ArrayList getCurrentProcesses()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$ipro p where p.clotim is null and p.pri>0 and p.strtim>' ' ");
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList();
            for(; rs.next(); resultList.add(rs.getString(1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String createParticipantDefinition(DOSChangeable participant)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(participant, "identifier");
        Utils.checkMandatoryString(participant, "type");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dpct@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dpct (ouid, id, name, des, typ) values (?, ?, ?, ?, ?) ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setString(i, (String)participant.get("identifier"));
            i++;
            stat.setString(i, (String)participant.get("name"));
            i++;
            stat.setString(i, (String)participant.get("description"));
            i++;
            stat.setString(i, (String)participant.get("type"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeParticipantDefinition(String participantOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(participantOUID, "participantOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dpct where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(participantOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getParticipantDefinition(String participantOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(participantOUID, "participantOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.id, p.name, p.des, p.typ from wf$dpct p where p.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(participantOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", participantOUID);
                dosObject.put("identifier", rs.getString(1));
                dosObject.put("name", rs.getString(2));
                dosObject.put("description", rs.getString(3));
                dosObject.put("type", rs.getString(4));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setParticipantDefinition(DOSChangeable participant)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(participant, "ouid");
        Utils.checkMandatoryString(participant, "identifier");
        Utils.checkMandatoryString(participant, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dpct set id=?, name=?, des=?, typ=? where ouid=? ");
            int i = 1;
            stat.setString(i, (String)participant.get("identifier"));
            i++;
            stat.setString(i, (String)participant.get("name"));
            i++;
            stat.setString(i, (String)participant.get("description"));
            i++;
            stat.setString(i, (String)participant.get("type"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)participant.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String lookupParticipantByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(p.ouid) from wf$dpct p where p.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dpct@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupParticipantByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList resultList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$dpct p where p.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList(5);
            for(; rs.next(); resultList.add("wf$dpct@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            resultList.trimToSize();
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String createApplicationDefinition(DOSChangeable application)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(application, "identifier");
        Utils.checkMandatoryString(application, "application");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dapl@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dapl (ouid, id, name, des, path) values (?, ?, ?, ?, ?) ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setString(i, (String)application.get("identifier"));
            i++;
            stat.setString(i, (String)application.get("name"));
            i++;
            stat.setString(i, (String)application.get("description"));
            i++;
            stat.setString(i, (String)application.get("file.path"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeApplicationDefinition(String applicationOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(applicationOUID, "applicationOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dapl where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(applicationOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getApplicationDefinition(String applicationOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(applicationOUID, "applicationOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.id, x.name, x.des, x.path from wf$dapl x where x.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(applicationOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", applicationOUID);
                dosObject.put("identifier", rs.getString(1));
                dosObject.put("name", rs.getString(2));
                dosObject.put("description", rs.getString(3));
                dosObject.put("file.path", rs.getString(4));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setApplicationDefinition(DOSChangeable application)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(application, "ouid");
        Utils.checkMandatoryString(application, "identifier");
        Utils.checkMandatoryString(application, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dapl set id=?, name=?, des=?, path=? where ouid=? ");
            int i = 1;
            stat.setString(i, (String)application.get("identifier"));
            i++;
            stat.setString(i, (String)application.get("name"));
            i++;
            stat.setString(i, (String)application.get("description"));
            i++;
            stat.setString(i, (String)application.get("path"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)application.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String lookupApplicationByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(x.ouid) from wf$dapl x where x.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dapl@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupApplicationByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList returnValue = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.ouid from wf$dapl x where x.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList returnValue = new ArrayList();
            for(; rs.next(); returnValue.add("wf$dapl@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            returnValue.trimToSize();
            con.commit();
            arraylist = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setActivityApplication(String activityOUID, ArrayList applications)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dactapl where wf$dactouid=? ");
            long activityRealOuid = Utils.getRealLongOuid(activityOUID);
            int i = 1;
            stat.setLong(i, activityRealOuid);
            i++;
            int rows = stat.executeUpdate();
            if(rows < 0)
                rows = 0;
            if(!Utils.isNullArrayList(applications))
            {
                stat.close();
                stat = null;
                stat = con.prepareStatement("insert into wf$dactapl (wf$dactouid,wf$daplouid) values (?,?) ");
                i = 1;
                stat.setLong(i, activityRealOuid);
                i++;
                for(Iterator apps = applications.iterator(); apps.hasNext();)
                {
                    stat.setLong(i, Utils.getRealLongOuid((String)apps.next()));
                    rows += stat.executeUpdate();
                }

            }
            stat.close();
            stat = null;
            if(rows > -1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public ArrayList getApplicationInvokeParameters(String activityOUID, String applicationOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        Utils.checkMandatoryString(applicationOUID, "applicationOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList resultList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select i.seq, i.typ, di.val from (select dd.ouid, ap.seq, dd.typ from wf$daplpar ap, wf$ddta dd where dd.ouid=ap.wf$ddtaouid and ap.wf$daplouid=? ) i, wf$idta di, wf$iact ai where di.wf$ddtaouid=i.ouid and ai.ouid=? and di.wf$iproouid=ai.wf$iproouid order by i.seq ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(applicationOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList();
            String string = null;
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("type", rs.getString(1));
                dosObject.put("value", rs.getString(2));
                resultList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList invokeApplication(String applicationOUID, ArrayList parameters)
        throws IIPRequestException
    {
        return null;
    }

    public String createProcedureDefinition(DOSChangeable procedure)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(procedure, "identifier");
        Utils.checkMandatoryString(procedure, "type");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dlib@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dlib (ouid, id, typ, name, rsl, des) values (?, ?, ?, ?, ?, ?) ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setString(i, (String)procedure.get("identifier"));
            i++;
            stat.setString(i, (String)procedure.get("type"));
            i++;
            stat.setString(i, (String)procedure.get("name"));
            i++;
            stat.setString(i, (String)procedure.get("result.datatype"));
            i++;
            stat.setString(i, (String)procedure.get("description"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeProcedureDefinition(String procedureOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(procedureOUID, "procedureOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dlib where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(procedureOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getProcedureDefinition(String procedureOUID)
        throws IIPRequestException
    {
        return null;
    }

    public void setProcedureDefinition(DOSChangeable procedure)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(procedure, "ouid");
        Utils.checkMandatoryString(procedure, "identifier");
        Utils.checkMandatoryString(procedure, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dlib set id=?, typ=?, name=?, rsl=?, des=? where ouid=? ");
            int i = 1;
            stat.setString(i, (String)procedure.get("identifier"));
            i++;
            stat.setString(i, (String)procedure.get("type"));
            i++;
            stat.setString(i, (String)procedure.get("name"));
            i++;
            stat.setString(i, (String)procedure.get("result.datatype"));
            i++;
            stat.setString(i, (String)procedure.get("description"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)procedure.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String lookupProcedureByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(p.ouid) from wf$dlib p where p.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dlib@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupProcedureByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList returnList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$dlib p where p.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            for(; rs.next(); returnList.add("wf$dlib@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            returnList.trimToSize();
            con.commit();
            arraylist = returnList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setProcedureInvokeParameters(String s, String s1, ArrayList arraylist)
        throws IIPRequestException
    {
    }

    public ArrayList getProcedureInvokeParameters(String activityOUID, String procedureOUID)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList invokeProcedure(String procedureOUID, ArrayList parameters)
        throws IIPRequestException
    {
        return null;
    }

    public String createDataDefinition(DOSChangeable data)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(data, "identifier");
        Utils.checkMandatoryString(data, "length");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$ddta@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$ddta (ouid, id, name, des, typ, len, val) values (?, ?, ?, ?, ?, ?, ?) ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setString(i, (String)data.get("identifier"));
            i++;
            stat.setString(i, (String)data.get("name"));
            i++;
            stat.setString(i, (String)data.get("description"));
            i++;
            stat.setString(i, (String)data.get("type"));
            i++;
            Utils.setInt(stat, i, (Integer)data.get("length"));
            i++;
            stat.setString(i, (String)data.get("default.value"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeDataDefinition(String dataOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(dataOUID, "dataOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$ddta where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(dataOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getDataDefinition(String dataOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(dataOUID, "dataOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.id, x.name, x.des, x.typ, x.len, x.val from wf$ddta x where x.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(dataOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", dataOUID);
                dosObject.put("identifier", rs.getString(1));
                dosObject.put("name", rs.getString(2));
                dosObject.put("description", rs.getString(3));
                dosObject.put("type", rs.getString(4));
                dosObject.put("length", Utils.getInteger(rs, 5));
                dosObject.put("default.value", rs.getString(6));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setDataDefinition(DOSChangeable data)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(data, "ouid");
        Utils.checkMandatoryString(data, "identifier");
        Utils.checkMandatoryString(data, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$ddta set id=?, name=?, des=?, typ=?, len=?, val=? where ouid=? ");
            int i = 1;
            Number number = null;
            stat.setString(i, (String)data.get("identifier"));
            i++;
            stat.setString(i, (String)data.get("name"));
            i++;
            stat.setString(i, (String)data.get("description"));
            i++;
            stat.setString(i, (String)data.get("type"));
            i++;
            Utils.setInt(stat, i, (Integer)data.get("length"));
            i++;
            stat.setString(i, (String)data.get("default.value"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)data.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String lookupDataByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(p.ouid) from wf$ddta p where p.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$ddta@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupDataByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList resultList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$ddta p where p.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList();
            for(; rs.next(); resultList.add("wf$ddta@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            resultList.trimToSize();
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getObjectParameter(String objectOUID, String dataOUID)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList getObjectParameters(String objectOUID)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList getExtendedAttribute(String objectOUID, String dataOUID)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList getExtendedAttributes(String objectOUID)
        throws IIPRequestException
    {
        return null;
    }

    public void createSimulationDataDefinition(DOSChangeable simulationData)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(simulationData, "ouid");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into wf$dsim (ouid, dur, cst, wrktim, wattim) values (?, ?, ?, ?, ?) ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid((String)simulationData.get("ouid")));
            i++;
            Utils.setInt(stat, i, (Integer)simulationData.get("duration"));
            i++;
            stat.setString(i, (String)simulationData.get("cost"));
            i++;
            Utils.setInt(stat, i, (Integer)simulationData.get("time.working"));
            i++;
            Utils.setInt(stat, i, (Integer)simulationData.get("time.waiting"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public void removeSimulationDataDefinition(String simulationDataOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(simulationDataOUID, "simulationDataOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dsim where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(simulationDataOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getSimulationDataDefinition(String objectOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(objectOUID, "objectOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.dur, x.cst, x.wrktim, x.wattim from wf$dsim x where x.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(objectOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", objectOUID);
                dosObject.put("duration", Utils.getInteger(rs, 1));
                dosObject.put("cost", rs.getString(2));
                dosObject.put("time.working", Utils.getInteger(rs, 3));
                dosObject.put("time.waiting", Utils.getInteger(rs, 4));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setSimulationDataDefinition(DOSChangeable simulationData)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(simulationData, "simulationData");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dsim set dur=?, cst=?, wrktim=?, wattim=? where ouid=? ");
            int i = 1;
            Utils.setInt(stat, i, (Integer)simulationData.get("duration"));
            i++;
            stat.setString(i, (String)simulationData.get("cost"));
            i++;
            Utils.setInt(stat, i, (Integer)simulationData.get("time.working"));
            i++;
            Utils.setInt(stat, i, (Integer)simulationData.get("time.waiting"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)simulationData.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public DOSChangeable getObjectSimulationData(String objectOUID)
        throws IIPRequestException
    {
        return null;
    }

    public void setObjectSimulationData(String s)
        throws IIPRequestException
    {
    }

    public String createProcessDefinition(DOSChangeable process)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(process, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dpro@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dpro (ouid, id, name, des, uod, aut, ver, chs, cdp, coukey, pblsta, vlf, vlt, cls, pri, lim) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), ?, ?, ?) ");
            int i = 1;
            String string = null;
            stat.setLong(i, realOuid);
            i++;
            stat.setString(i, (String)process.get("identifier"));
            i++;
            stat.setString(i, (String)process.get("name"));
            i++;
            stat.setString(i, (String)process.get("description"));
            i++;
            stat.setString(i, (String)process.get("duration.unit"));
            i++;
            stat.setString(i, (String)process.get("author"));
            i++;
            stat.setString(i, (String)process.get("version"));
            i++;
            stat.setString(i, (String)process.get("character.set"));
            i++;
            stat.setString(i, (String)process.get("code.page"));
            i++;
            stat.setString(i, (String)process.get("country.key"));
            i++;
            stat.setString(i, (String)process.get("publication.status"));
            i++;
            stat.setString(i, (String)process.get("date.valid.from"));
            i++;
            stat.setString(i, (String)process.get("date.valid.to"));
            i++;
            stat.setString(i, (String)process.get("classification"));
            i++;
            Utils.setInt(stat, i, (Integer)process.get("priority"));
            i++;
            Utils.setInt(stat, i, (Integer)process.get("limit"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            DOSChangeable activity = new DOSChangeable();
            activity.put("ouid@process.definition", newOUID);
            activity.put("identifier", "START");
            activity.put("name", "Start");
            activity.put("type", "180");
            activity.put("limit", new Integer(0));
            activity.put("mode.start", "A");
            activity.put("mode.finish", "A");
            activity.put("priority", new Integer(0));
            activity.put("instantiation", new Integer(0));
            activity.put("join", "A");
            activity.put("split", "A");
            activity.put("x", new Integer(10));
            activity.put("y", new Integer(20));
            activity.put("w", new Integer(0));
            activity.put("h", new Integer(0));
            activity.put("is.container", Utils.False);
            createActivityDefinition(activity);
            activity.clear();
            activity = null;
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeProcessDefinition(String processOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOUID, "processOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = Utils.getRealLongOuid(processOUID);
            stat = con.prepareStatement("delete wf$dactact where actouid1=? ");
            stat.setLong(1, realOuid);
            stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete wf$dpro where ouid=? ");
            stat.setLong(1, realOuid);
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getProcessDefinition(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.id, x.name, x.des, x.uod, x.aut, x.ver, x.chs, x.cdp, x.coukey, x.pblsta, to_char(to_date(x.vlf,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.vlt,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), x.cls, x.pri, x.lim from wf$dpro x where x.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", processOUID);
                dosObject.put("identifier", rs.getString(1));
                dosObject.put("name", rs.getString(2));
                dosObject.put("description", rs.getString(3));
                dosObject.put("duration.unit", rs.getString(4));
                dosObject.put("author", rs.getString(5));
                dosObject.put("version", rs.getString(6));
                dosObject.put("character.set", rs.getString(7));
                dosObject.put("code.page", rs.getString(8));
                dosObject.put("country.key", rs.getString(9));
                dosObject.put("publication.status", rs.getString(10));
                dosObject.put("date.valid.from", rs.getString(11));
                dosObject.put("date.valid.to", rs.getString(12));
                dosObject.put("classification", rs.getString(13));
                dosObject.put("priority", Utils.getInteger(rs, 14));
                dosObject.put("limit", Utils.getInteger(rs, 15));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setProcessDefinition(DOSChangeable process)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(process, "ouid");
        Utils.checkMandatoryString(process, "identifier");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dpro set id=?, name=?, des=?, uod=?, aut=?, ver=?, chs=?, cdp=?, coukey=?, pblsta=?, vlf=to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), vlt=to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), cls=?, pri=?, lim=? where ouid=? ");
            int i = 1;
            stat.setString(i, (String)process.get("identifier"));
            i++;
            stat.setString(i, (String)process.get("name"));
            i++;
            stat.setString(i, (String)process.get("description"));
            i++;
            stat.setString(i, (String)process.get("duration.unit"));
            i++;
            stat.setString(i, (String)process.get("author"));
            i++;
            stat.setString(i, (String)process.get("version"));
            i++;
            stat.setString(i, (String)process.get("character.set"));
            i++;
            stat.setString(i, (String)process.get("code.page"));
            i++;
            stat.setString(i, (String)process.get("country.key"));
            i++;
            stat.setString(i, (String)process.get("publication.status"));
            i++;
            stat.setString(i, (String)process.get("date.valid.from"));
            i++;
            stat.setString(i, (String)process.get("date.valid.to"));
            i++;
            stat.setString(i, (String)process.get("classification"));
            i++;
            Utils.setInt(stat, i, (Integer)process.get("priority"));
            i++;
            Utils.setInt(stat, i, (Integer)process.get("limit"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)process.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String createProcess(DOSChangeable process)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ArrayList ouidList;
        long pdOuid;
        Utils.checkMandatoryString(process, "ouid@process.definition");
        Object objectOuids = process.get("ouid@object");
        if(objectOuids == null)
            throw new IIPRequestException("Miss out mandatory parameter: ouid@object");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ouidList = null;
        String instanceOuid = null;
        pdOuid = Utils.getRealLongOuid((String)process.get("ouid@process.definition"));
        if(objectOuids instanceof String)
        {
            ouidList = new ArrayList();
            ouidList.add(objectOuids);
        } else
        if(objectOuids instanceof ArrayList)
            ouidList = (ArrayList)objectOuids;
        ouidList.add(0, dos);
        Object returnObject = Utils.executeScriptFile(dos.getEventName((String)process.get("ouid@process.definition"), "start.before"), dss, ouidList, true);
        if((returnObject instanceof Boolean) && returnObject.equals(Boolean.FALSE))
            return null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            ArrayList acts = null;
            DOSChangeable dosObject = null;
            long realOuid = dos.generateOUID();
            String newOUID = "wf$ipro@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$ipro (ouid, wf$dproouid, wf$staouid, pri, cretim, limtim, plntim, wf$pct) select ?, ?, 100, nvl(pri,0), to_char(sysdate,'YYYYMMDDHH24MISS'), to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), ? from wf$dpro where ouid=? ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, pdOuid);
            i++;
            stat.setString(i, (String)process.get("time.limit.to.finish"));
            i++;
            stat.setString(i, (String)process.get("time.plan.to.start"));
            i++;
            stat.setString(i, (String)process.get("responsible"));
            i++;
            stat.setLong(i, pdOuid);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            stat = con.prepareStatement("insert into wf$dos (wf$iproouid, wf$dosouid) values (?,?)");
            stat.setLong(1, realOuid);
            ouidList.remove(0);
            Iterator ouidKey;
            for(ouidKey = ouidList.iterator(); ouidKey.hasNext();)
            {
                String instanceOuid = (String)ouidKey.next();
                stat.setString(2, instanceOuid);
                rows = stat.executeUpdate();
            }

            ouidKey = null;
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception2) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            stat = con.prepareStatement("select x.ouid from wf$dact x where x.wf$dproouid=? ");
            stat.setLong(1, pdOuid);
            acts = new ArrayList();
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next(); acts.add(Utils.getLongToHexString(rs, 1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            dosObject = new DOSChangeable();
            dosObject.put("ouid@process", newOUID);
            String activityDefinition = null;
            Iterator enum;
            for(enum = acts.iterator(); enum.hasNext(); createActivity(dosObject))
            {
                activityDefinition = "wf$dact@" + (String)enum.next();
                dosObject.put("ouid@activity.definition", activityDefinition);
                dosObject.put("responsible", process.get("responsible"));
            }

            dosObject.clear();
            dosObject = null;
            enum = null;
            acts.clear();
            acts = null;
            ouidList.add(0, newOUID);
            Object returnObject = Utils.executeScriptFile(dos.getEventName((String)process.get("ouid@process.definition"), "start.after"), dss, ouidList, true);
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeProcess(String processOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOUID, "processOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = Utils.getRealLongOuid(processOUID);
            stat = con.prepareStatement("delete wf$ipro where ouid=? ");
            stat.setLong(1, realOuid);
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getProcess(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSChangeable dosObject = null;
        ArrayList ouidList = null;
        long realOuid = 0L;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            long realOuid = Utils.getRealLongOuid(processOUID);
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select y.id, y.name, y.des, y.uod, to_char(to_date(y.vlf,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(y.vlt,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), y.cls, x.pri, y.lim, x.wf$staouid, to_char(to_date(x.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.strtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.limtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.plntim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), z.name, x.wf$pct, y.ouid from wf$ipro x, wf$dpro y, wf$sta z where y.ouid=x.wf$dproouid and z.ouid=x.wf$staouid and x.ouid=? ");
            stat.setLong(1, realOuid);
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", processOUID);
                dosObject.put("identifier", rs.getString(1));
                dosObject.put("name", rs.getString(2));
                dosObject.put("description", rs.getString(3));
                dosObject.put("duration.unit", rs.getString(4));
                dosObject.put("date.valid.from", rs.getString(5));
                dosObject.put("date.valid.to", rs.getString(6));
                dosObject.put("classification", rs.getString(7));
                dosObject.put("priority", Utils.getInteger(rs, 8));
                dosObject.put("limit", Utils.getInteger(rs, 9));
                dosObject.put("ouid@workflow.status", rs.getString(10));
                dosObject.put("time.created", rs.getString(11));
                dosObject.put("time.started", rs.getString(12));
                dosObject.put("time.closed", rs.getString(13));
                dosObject.put("time.limit.to.finish", rs.getString(14));
                dosObject.put("time.plan.to.start", rs.getString(15));
                dosObject.put("name@workflow.status", rs.getString(16));
                dosObject.put("responsible", rs.getString(17));
                dosObject.put("ouid@process.definition", "wf$dpro@" + Utils.getLongToHexString(rs, 18));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select a.wf$dosouid from wf$dos a where wf$iproouid=?");
            stat.setLong(1, realOuid);
            ArrayList ouidList = new ArrayList();
            for(rs = stat.executeQuery(); rs.next(); ouidList.add(rs.getString(1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            if(ouidList.size() < 1)
                ouidList = null;
            dosObject.put("ouid@object", ouidList);
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setProcess(DOSChangeable process)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(process, "ouid");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        Object objectOuids = process.get("ouid@object");
        ArrayList ouidList = null;
        String instanceOuid = null;
        long realOuid = 0L;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$ipro set limtim=to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), plntim=to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS') where ouid=? ");
            int i = 1;
            realOuid = Utils.getRealLongOuid((String)process.get("ouid"));
            stat.setString(i, (String)process.get("time.limit.to.finish"));
            i++;
            stat.setString(i, (String)process.get("time.plan.to.start"));
            i++;
            stat.setLong(i, realOuid);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows != 1)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            stat = con.prepareStatement("delete from wf$dos where wf$iproouid=?");
            stat.setLong(1, realOuid);
            stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("insert into wf$dos (wf$iproouid, wf$dosouid) values (?,?)");
            stat.setLong(1, realOuid);
            if(objectOuids instanceof String)
            {
                ouidList = new ArrayList();
                ouidList.add(objectOuids);
            } else
            if(objectOuids instanceof ArrayList)
                ouidList = (ArrayList)objectOuids;
            Iterator ouidKey;
            for(ouidKey = ouidList.iterator(); ouidKey.hasNext();)
            {
                instanceOuid = (String)ouidKey.next();
                stat.setString(2, instanceOuid);
                rows = stat.executeUpdate();
            }

            ouidKey = null;
            ouidList.clear();
            ouidList = null;
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public ArrayList listProcessByObject(String objectOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(objectOUID, "objectOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList results = null;
        DOSChangeable aProcess = null;
        HashMap user = null;
        String tempString = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.ouid, y.id, y.name, y.des, y.uod, to_char(to_date(y.vlf,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(y.vlt,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), y.cls, x.pri, y.lim, x.wf$staouid, to_char(to_date(x.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.strtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.limtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), to_char(to_date(x.plntim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), z.name, x.wf$pct from wf$dos p, wf$ipro x, wf$dpro y, wf$sta z where p.wf$dosouid=? and x.ouid=p.wf$iproouid and y.ouid=x.wf$dproouid and z.ouid=x.wf$staouid ");
            stat.setString(1, objectOUID);
            ResultSet rs = stat.executeQuery();
            ArrayList results = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable aProcess = new DOSChangeable();
                aProcess.put("ouid", "wf$ipro@" + Utils.getLongToHexString(rs, 1));
                aProcess.put("identifier", rs.getString(2));
                aProcess.put("name", rs.getString(3));
                aProcess.put("description", rs.getString(4));
                aProcess.put("duration.unit", rs.getString(5));
                aProcess.put("date.valid.from", rs.getString(6));
                aProcess.put("date.valid.to", rs.getString(7));
                aProcess.put("classification", rs.getString(8));
                aProcess.put("priority", Utils.getInteger(rs, 9));
                aProcess.put("limit", Utils.getInteger(rs, 10));
                aProcess.put("ouid@workflow.status", rs.getString(11));
                aProcess.put("time.created", rs.getString(12));
                aProcess.put("time.started", rs.getString(13));
                aProcess.put("time.closed", rs.getString(14));
                aProcess.put("time.limit.to.finish", rs.getString(15));
                aProcess.put("time.plan.to.start", rs.getString(16));
                aProcess.put("name@workflow.status", rs.getString(17));
                String tempString = rs.getString(18);
                if(!Utils.isNullString(tempString))
                {
                    HashMap user = aus.getUser(tempString);
                    if(user != null)
                    {
                        aProcess.put("name$responsible", user.get("name") + " (" + tempString + ")");
                        user.clear();
                        user = null;
                    }
                }
                results.add(aProcess);
                aProcess = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            arraylist = results;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String lookupProcessByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(p.ouid) from wf$dpro p where p.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dpro@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupProcessByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList resultList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$dpro p where p.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList();
            for(; rs.next(); resultList.add("wf$dpro@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            resultList.trimToSize();
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getProcessActivities(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        boolean isInstance;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        isInstance = processOUID.startsWith("wf$ipro@");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isInstance)
                stat = con.prepareStatement("select ia.ouid, da.wf$acttypouid, da.id, da.name from wf$iact ia, wf$dact da where ia.wf$iproouid=? and da.ouid=ia.wf$dactouid order by da.id ");
            else
                stat = con.prepareStatement("select da.ouid, da.wf$acttypouid, da.id, da.name from wf$dact da where da.wf$dproouid=? order by da.id ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            if(isInstance)
                while(rs.next()) 
                {
                    DOSChangeable dosObject = new DOSChangeable();
                    dosObject.put("ouid", "wf$iact@" + Long.toHexString(rs.getLong(1)));
                    dosObject.put("type", rs.getString(2));
                    dosObject.put("identifier", rs.getString(3));
                    dosObject.put("name", rs.getString(4));
                    rowArrayList.add(dosObject);
                    dosObject = null;
                }
            else
                while(rs.next()) 
                {
                    DOSChangeable dosObject = new DOSChangeable();
                    dosObject.put("ouid", "wf$dact@" + Long.toHexString(rs.getLong(1)));
                    dosObject.put("type", rs.getString(2));
                    dosObject.put("identifier", rs.getString(3));
                    dosObject.put("name", rs.getString(4));
                    rowArrayList.add(dosObject);
                    dosObject = null;
                }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void startProcess(String processOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOUID, "ouid");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long realOuidProcess = Utils.getRealLongOuid(processOUID);
        long realOuidActivity = 0L;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select ia.ouid from wf$iact ia, wf$dact da, wf$ipro ip where ia.wf$iproouid=ip.ouid and da.ouid=ia.wf$dactouid and da.wf$acttypouid=180 and ip.ouid=? ");
            stat.setLong(1, realOuidProcess);
            rs = stat.executeQuery();
            String activityOUID = null;
            if(rs.next())
            {
                realOuidActivity = rs.getLong(1);
                activityOUID = "wf$iact@" + Long.toHexString(realOuidActivity);
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("update wf$ipro set wf$staouid=110, strtim=to_char(sysdate,'YYYYMMDDHH24MISS') where ouid=? ");
            int i = 1;
            stat.setLong(i, realOuidProcess);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            startActivity(activityOUID);
            finishActivity(activityOUID);
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
        return;
    }

    public void finishProcess(String processOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOUID, "processOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        boolean isProcess = processOUID.startsWith("wf$ipro@");
        boolean isActivity = processOUID.startsWith("wf$iact@");
        DOSChangeable procDef = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isProcess)
                stat = con.prepareStatement("update wf$ipro set wf$staouid=130, clotim=to_char(sysdate,'YYYYMMDDHH24MISS') where ouid=? ");
            else
            if(isActivity)
            {
                stat = con.prepareStatement("select wf$iproouid from wf$iact where ouid=? ");
                stat.setLong(1, Utils.getRealLongOuid(processOUID));
                rs = stat.executeQuery();
                if(rs.next())
                    processOUID = "wf$ipro@" + Long.toHexString(rs.getLong(1));
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                stat = con.prepareStatement("update wf$ipro set wf$staouid=130, clotim=to_char(sysdate,'YYYYMMDDHH24MISS') where ouid=? ");
            }
            procDef = getProcess(processOUID);
            stat2 = con.prepareStatement("select d.wf$dosouid from wf$dos d where d.wf$iproouid=?");
            stat2.setLong(1, Utils.getRealLongOuid(processOUID));
            ArrayList ouidList = new ArrayList();
            ouidList.add(processOUID);
            for(rs = stat2.executeQuery(); rs.next(); ouidList.add(rs.getString(1)));
            rs.close();
            rs = null;
            stat2.close();
            stat2 = null;
            Object returnObject = Utils.executeScriptFile(dos.getEventName((String)procDef.get("ouid@process.definition"), "finish.before"), dss, ouidList, true);
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(processOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 1)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            stat = con.prepareStatement("update wf$iact set wf$staouid=160, clotim=to_char(sysdate,'YYYYMMDDHH24MISS') where wf$iproouid=? and wf$staouid=110 ");
            i = 1;
            stat.setLong(i, Utils.getRealLongOuid(processOUID));
            i++;
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows > -1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            returnObject = Utils.executeScriptFile(dos.getEventName((String)procDef.get("ouid@process.definition"), "finish.after"), dss, ouidList, true);
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
        }
        finally
        {
            if(stat2 != null)
            {
                try
                {
                    stat2.close();
                }
                catch(SQLException sqlexception2) { }
                stat2 = null;
            }
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return;
    }

    public void resumeProcess(String processOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOUID, "processOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long realOuidProcess = Utils.getRealLongOuid(processOUID);
        long realOuidActivity = 0L;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select ia.ouid from wf$iact ia where ia.wf$staouid=120 and ia.wf$iproouid=? and rownum=1 ");
            stat.setLong(1, realOuidProcess);
            rs = stat.executeQuery();
            String activityOUID = null;
            if(rs.next())
            {
                realOuidActivity = rs.getLong(1);
                activityOUID = "wf$iact@" + Long.toHexString(realOuidActivity);
            } else
            {
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not find suspended activity.");
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("update wf$ipro set wf$staouid=110 where ouid=? and wf$staouid in (120,170) ");
            int i = 1;
            stat.setLong(i, realOuidProcess);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            finishActivity(activityOUID);
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
        return;
    }

    public void refreshProcessStatus(String s)
        throws IIPRequestException
    {
    }

    public String getProcessStatus(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select wf$staouid from wf$ipro where ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = rs.getString(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setProcessStatus(String processOUID, String statusOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOUID, "processOUID");
        Utils.checkMandatoryString(statusOUID, "statusOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$ipro set wf$staouid=? where ouid=? ");
            int i = 1;
            stat.setString(i, statusOUID);
            i++;
            stat.setLong(i, Utils.getRealLongOuid(processOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public ArrayList getCurrentActivities(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select ia.ouid, da.wf$acttypouid, da.id, da.name from wf$iact ia, wf$dact da where ia.wf$iproouid=? and da.ouid=ia.wf$dactouid and ia.strtim>' ' and ia.clotim is null and ia.wf$staouid in (100,110,150) order by da.id ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid", "wf$iact@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("type", rs.getString(2));
                dosObject.put("identifier", rs.getString(3));
                dosObject.put("name", rs.getString(4));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getCurrentActivitiesByPerfomer(String processOUID, String userId)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(processOUID, "processOUID");
        Utils.checkMandatoryString(userId, "userId");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.ouid, x.wf$acttypouid, x.id, x.name from (select ia.ouid, da.wf$acttypouid, da.id, da.name from wf$iact ia, wf$dact da where ia.wf$iproouid=? and ia.wf$dpctouid=? and da.ouid=ia.wf$dactouid and ia.strtim>' ' and ((ia.clotim is null and ia.wf$staouid in (100,110,150)) or (ia.clotim>' ' and ia.wf$staouid=130 and da.finmod='A' and da.wf$acttypouid=110)) union select ia.ouid, da.wf$acttypouid, da.id, da.name from wf$iact ia, wf$iacts ias, wf$dact da where ia.wf$iproouid=? and ias.wf$iactouid=ia.ouid and ias.wf$dpctouid=? and da.ouid=ia.wf$dactouid and ia.strtim>' ' and ((ias.clotim is null and ia.wf$staouid in (100,110,150)) or (ias.clotim>' ' and ia.wf$staouid=130 and da.finmod='A' and da.wf$acttypouid=110)) ) x order by x.id ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            stat.setString(2, userId);
            stat.setLong(3, Utils.getRealLongOuid(processOUID));
            stat.setString(4, userId);
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid", "wf$iact@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("type", rs.getString(2));
                dosObject.put("identifier", rs.getString(3));
                dosObject.put("name", rs.getString(4));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getProcessesByType(String processTypeOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(processTypeOUID, "processTypeOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select wp.ouid, wp.id, wp.name from wf$dpro wp where wp.cls=? order by wp.name");
            stat.setString(1, processTypeOUID);
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid", "wf$dpro@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("identifier", rs.getString(2));
                dosObject.put("name", rs.getString(3));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getProcessActivities4Graph(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        boolean isDefinition;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        isDefinition = processOUID.startsWith("wf$dpro@");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
                stat = con.prepareStatement("select a.ouid, a.id, a.name, null, null, a.x, a.y, 0, null, a.wf$acttypouid, '' from wf$dact a where a.wf$dproouid=? and (a.x>0 or a.y>0) order by a.id");
            else
                stat = con.prepareStatement("select ouid, id, name, limtim, clotim, x, y, wf$staouid, wf$dactouid, wf$acttypouid, wf$dpctouid, (select 'T' from wf$iacts where wf$iactouid=ouid and rownum=1) from (select y.ouid, max(x.id) as id, max(x.name) as name, to_char(to_date(max(y.limtim),'YYYYMMDDHH24miss'),'YYYY-MM-DD') as limtim, to_char(to_date(max(y.clotim),'YYYYMMDDHH24miss'),'YYYY-MM-DD') as clotim, max(x.x) as x, max(x.y) as y, max(y.wf$staouid) as wf$staouid, max(x.ouid) as wf$dactouid, max(x.wf$acttypouid) as wf$acttypouid, max(y.wf$dpctouid) as wf$dpctouid from wf$dact x, wf$iact y where x.ouid=y.wf$dactouid and y.wf$iproouid=? and (x.x>0 or x.y>0) group by y.ouid )order by id");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            if(isDefinition)
                while(rs.next()) 
                {
                    DOSChangeable dosObject = new DOSChangeable();
                    dosObject.put("ouid", "wf$dact@" + Long.toHexString(rs.getLong(1)));
                    dosObject.put("identifier", rs.getString(2));
                    dosObject.put("name", rs.getString(3));
                    dosObject.put("time.limit.to.finish", rs.getString(4));
                    dosObject.put("time.closed", rs.getString(5));
                    dosObject.put("x", Utils.getInteger(rs, 6));
                    dosObject.put("y", Utils.getInteger(rs, 7));
                    dosObject.put("type", rs.getString(10));
                    rowArrayList.add(dosObject);
                    dosObject = null;
                }
            else
                while(rs.next()) 
                {
                    DOSChangeable dosObject = new DOSChangeable();
                    dosObject.put("ouid", "wf$iact@" + Long.toHexString(rs.getLong(1)));
                    dosObject.put("identifier", rs.getString(2));
                    dosObject.put("name", rs.getString(3));
                    dosObject.put("time.limit.to.finish", rs.getString(4));
                    dosObject.put("time.closed", rs.getString(5));
                    dosObject.put("x", Utils.getInteger(rs, 6));
                    dosObject.put("y", Utils.getInteger(rs, 7));
                    dosObject.put("ouid@workflow.status", rs.getString(8));
                    dosObject.put("ouid@activity.definition", "wf$dact@" + Long.toHexString(rs.getLong(9)));
                    dosObject.put("type", rs.getString(10));
                    dosObject.put("performer", rs.getString(11));
                    dosObject.put("is.multiple", rs.getString(12));
                    rowArrayList.add(dosObject);
                    dosObject = null;
                }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getProcessTransitions4Graph(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        boolean isDefinition;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        isDefinition = processOUID.startsWith("wf$dpro@");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
                stat = con.prepareStatement("select x.ouid, x.id, x.name, x.actouid, x.actouid2, x.typ from wf$dtrs x, wf$dact a where x.wf$dproouid=? and a.ouid=x.actouid2 order by a.id ");
            else
                stat = con.prepareStatement("select i.ouid, i.id, i.name, i.actouid, i.actouid2, i.typ from (select x.ouid, max(x.id) as id, max(x.name) as name, max(y.ouid) as actouid, max(z.ouid) as actouid2, max(x.typ) as typ, max(z.wf$dactouid) as wf$dactouid2 from wf$dtrs x, wf$iact y, wf$iact z, wf$ipro ip where y.wf$iproouid=ip.ouid and y.wf$dactouid=x.actouid and z.wf$iproouid=ip.ouid and z.wf$dactouid=x.actouid2 and x.wf$dproouid=ip.wf$dproouid and ip.ouid=? group by x.ouid ) i, wf$dact a where a.ouid=i.wf$dactouid2 order by a.id ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            if(isDefinition)
                while(rs.next()) 
                {
                    DOSChangeable dosObject = new DOSChangeable();
                    dosObject.put("ouid", "wf$dtrs@" + Long.toHexString(rs.getLong(1)));
                    dosObject.put("identifier", rs.getString(2));
                    dosObject.put("name", rs.getString(3));
                    dosObject.put("ouid@act1", "wf$dact@" + Long.toHexString(rs.getLong(4)));
                    dosObject.put("ouid@act2", "wf$dact@" + Long.toHexString(rs.getLong(5)));
                    dosObject.put("type", rs.getString(6));
                    rowArrayList.add(dosObject);
                    dosObject = null;
                }
            else
                while(rs.next()) 
                {
                    DOSChangeable dosObject = new DOSChangeable();
                    dosObject.put("ouid", "wf$dtrs@" + Long.toHexString(rs.getLong(1)));
                    dosObject.put("identifier", rs.getString(2));
                    dosObject.put("name", rs.getString(3));
                    dosObject.put("ouid@act1", "wf$iact@" + Long.toHexString(rs.getLong(4)));
                    dosObject.put("ouid@act2", "wf$iact@" + Long.toHexString(rs.getLong(5)));
                    dosObject.put("type", rs.getString(6));
                    rowArrayList.add(dosObject);
                    dosObject = null;
                }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String getDefinitionOfProcess(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.wf$dproouid from wf$ipro p where p.ouid=? and rownum=1 ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = rs.getString(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String createActivityDefinition(DOSChangeable activity)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activity, "ouid@process.definition");
        Utils.checkMandatoryString(activity, "identifier");
        Utils.checkMandatoryString(activity, "type");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dact@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dact (ouid, wf$dproouid, id, name, des, wf$acttypouid, lim, wf$pctouid, strmod, finmod, pri, doc, ico, ins, lop, jon, spl, x, y, w, h, cmt) values (?, ?, ?, ?, ?, ?, nvl(?,0), ?, nvl(?,'A'), nvl(?,'A'), nvl(?,1), ?, ?, nvl(?,1), ?, nvl(?,'A'), nvl(?,'A'), ?, ?, ?, ?, ?) ");
            int i = 1;
            Number number = null;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)activity.get("ouid@process.definition")));
            i++;
            stat.setString(i, (String)activity.get("identifier"));
            i++;
            stat.setString(i, (String)activity.get("name"));
            i++;
            stat.setString(i, (String)activity.get("description"));
            i++;
            stat.setString(i, (String)activity.get("type"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("limit"));
            i++;
            stat.setString(i, (String)activity.get("participant"));
            i++;
            stat.setString(i, (String)activity.get("mode.start"));
            i++;
            stat.setString(i, (String)activity.get("mode.finish"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("priority"));
            i++;
            stat.setString(i, (String)activity.get("document"));
            i++;
            stat.setString(i, (String)activity.get("icon"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("instantiation"));
            i++;
            stat.setString(i, (String)activity.get("loop"));
            i++;
            stat.setString(i, (String)activity.get("join"));
            i++;
            stat.setString(i, (String)activity.get("split"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("x"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("y"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("w"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("h"));
            i++;
            Utils.setBoolean(stat, i, (Boolean)activity.get("is.container"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeActivityDefinition(String activityOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long realOuid = Utils.getRealLongOuid(activityOUID);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from wf$dact where ouid in (select actouid2 from wf$dactact connect by prior actouid2=actouid1 start with actouid1=?) ");
            stat.setLong(1, realOuid);
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete wf$dact where ouid=? ");
            stat.setLong(1, realOuid);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getActivityDefinition(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.wf$dproouid, x.id, x.name, x.des, x.wf$acttypouid, x.lim, x.wf$pctouid, x.strmod, x.finmod, x.pri, x.doc, x.ico, x.ins, x.lop, x.jon, x.spl, x.x, x.y, x.w, x.h, x.cmt, z.id, z.name, at.des from wf$dact x, wf$dpro z, wf$acttyp at where z.ouid=x.wf$dproouid and at.ouid=x.wf$acttypouid and x.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            String string = null;
            if(rs.next())
            {
                dosObject.put("ouid", activityOUID);
                dosObject.put("ouid@process.definition", "wf$dpro@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("identifier", rs.getString(2));
                dosObject.put("name", rs.getString(3));
                dosObject.put("description", rs.getString(4));
                dosObject.put("ouid@type", rs.getString(5));
                dosObject.put("limit", Utils.getInteger(rs, 6));
                dosObject.put("participant", rs.getString(7));
                dosObject.put("mode.start", rs.getString(8));
                dosObject.put("mode.finish", rs.getString(9));
                dosObject.put("priority", Utils.getInteger(rs, 10));
                dosObject.put("document", rs.getString(11));
                dosObject.put("icon", rs.getString(12));
                dosObject.put("instantiation", Utils.getInteger(rs, 13));
                dosObject.put("loop", rs.getString(14));
                dosObject.put("join", rs.getString(15));
                dosObject.put("split", rs.getString(16));
                dosObject.put("x", Utils.getInteger(rs, 17));
                dosObject.put("y", Utils.getInteger(rs, 18));
                dosObject.put("w", Utils.getInteger(rs, 19));
                dosObject.put("h", Utils.getInteger(rs, 20));
                dosObject.put("is.container", Utils.getBoolean(rs, 21));
                dosObject.put("process.identifier", rs.getString(22));
                dosObject.put("process.name", rs.getString(23));
                dosObject.put("type", rs.getString(24));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setActivityDefinition(DOSChangeable activity)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activity, "ouid");
        Utils.checkMandatoryString(activity, "ouid@process.definition");
        Utils.checkMandatoryString(activity, "identifier");
        Utils.checkMandatoryString(activity, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dact set wf$dproouid=?, id=?, name=?, des=?, wf$acttypouid=?, lim=?, wf$pctouid=?, strmod=?, finmod=?, pri=?, doc=?, ico=?, ins=?, lop=?, jon=?, spl=?, x=?, y=?, w=?, h=?, cmt=? where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid((String)activity.get("ouid@process.definition")));
            i++;
            stat.setString(i, (String)activity.get("identifier"));
            i++;
            stat.setString(i, (String)activity.get("name"));
            i++;
            stat.setString(i, (String)activity.get("description"));
            i++;
            stat.setString(i, (String)activity.get("type"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("limit"));
            i++;
            stat.setString(i, (String)activity.get("participant"));
            i++;
            stat.setString(i, (String)activity.get("mode.start"));
            i++;
            stat.setString(i, (String)activity.get("mode.finish"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("priority"));
            i++;
            stat.setString(i, (String)activity.get("document"));
            i++;
            stat.setString(i, (String)activity.get("icon"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("instantiation"));
            i++;
            stat.setString(i, (String)activity.get("loop"));
            i++;
            stat.setString(i, (String)activity.get("join"));
            i++;
            stat.setString(i, (String)activity.get("split"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("x"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("y"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("w"));
            i++;
            Utils.setInt(stat, i, (Integer)activity.get("h"));
            i++;
            Utils.setBoolean(stat, i, (Boolean)activity.get("is.container"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)activity.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String createActivity(DOSChangeable activity)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activity, "ouid@activity.definition");
        Utils.checkMandatoryString(activity, "ouid@process");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$iact@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            ArrayList works = null;
            DOSChangeable aWork = null;
            String ouidDefinition = (String)activity.get("ouid@activity.definition");
            long realOuidDefinition = Utils.getRealLongOuid(ouidDefinition);
            long realOuidProcess = Utils.getRealLongOuid((String)activity.get("ouid@process"));
            stat = con.prepareStatement("insert into wf$iact (ouid, wf$iproouid, wf$dactouid, wf$staouid, pri, cretim, limtim, plntim, wf$dpctouid) select ?, ?, ouid, 100, nvl(pri,0), to_char(sysdate,'YYYYMMDDHH24MISS'), to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS'), decode(wf$acttypouid,180,?,nvl(?,wf$pctouid)) from wf$dact where ouid=? ");
            int i = 1;
            String string = null;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, realOuidProcess);
            i++;
            stat.setString(i, (String)activity.get("time.limit.to.finish"));
            i++;
            stat.setString(i, (String)activity.get("time.plan.to.start"));
            i++;
            stat.setString(i, (String)activity.get("responsible"));
            i++;
            stat.setString(i, (String)activity.get("performer"));
            i++;
            stat.setLong(i, realOuidDefinition);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            stat = con.prepareStatement("select ouid from wf$dwrk where wf$dactouid=? ");
            stat.setLong(1, realOuidDefinition);
            works = new ArrayList();
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next(); works.add("wf$dwrk@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.close();
            con = null;
            xc.close();
            xc = null;
            DOSChangeable subProcess = getSubProcess(ouidDefinition);
            if(subProcess != null)
            {
                DOSChangeable processCreate = new DOSChangeable();
                String processOUID = (String)subProcess.get("ouid@process");
                processCreate.put("ouid@process", processOUID);
                String newSubProcessOUID = createProcess(processCreate);
                processCreate.clear();
                processCreate = null;
                setSubProcess(newOUID, newSubProcessOUID, null);
                subProcess.clear();
                subProcess = null;
            }
            aWork = new DOSChangeable();
            aWork.put("ouid@activity", newOUID);
            for(Iterator enum = works.iterator(); enum.hasNext(); createWorkItem(aWork))
                aWork.put("ouid@work.definition", enum.next());

            aWork.clear();
            aWork = null;
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void createMultipleActivities(DOSChangeable activity)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activity, "ouid@activity");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            ArrayList works = null;
            DOSChangeable aWork = null;
            String ouid = (String)activity.get("ouid@activity");
            long realOuid = Utils.getRealLongOuid(ouid);
            stat = con.prepareStatement("delete from wf$iacts where wf$iactouid=?");
            stat.setLong(1, realOuid);
            stat.executeUpdate();
            stat.close();
            stat = null;
            int rows = 0;
            String performer = null;
            ArrayList performers = (ArrayList)activity.get("performers");
            if(!Utils.isNullArrayList(performers))
            {
                stat = con.prepareStatement("insert into wf$iacts (wf$iactouid, wf$dpctouid) values (?,?)");
                stat.setLong(1, realOuid);
                for(Iterator performerKey = performers.iterator(); performerKey.hasNext();)
                {
                    performer = (String)performerKey.next();
                    if(!Utils.isNullString(performer))
                    {
                        stat.setString(2, performer);
                        rows += stat.executeUpdate();
                    }
                }

                stat.close();
                stat = null;
            }
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
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
        return;
    }

    public ArrayList getMultipleActivities(String activityOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOuid, "activityOuid");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList activities = null;
        HashMap act = null;
        HashMap user = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = Utils.getRealLongOuid(activityOuid);
            int rows = 0;
            String performer = null;
            stat = con.prepareStatement("select ias.wf$dpctouid, ias.read, decode(ias.clotim,null,null,'T'), to_char(to_date(ias.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') from wf$iacts ias where ias.wf$iactouid=? ");
            stat.setLong(1, realOuid);
            ArrayList activities = new ArrayList();
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                HashMap act = new HashMap();
                act.put("performer", rs.getString(1));
                act.put("read", Utils.getBoolean(rs, 2));
                act.put("closed", Utils.getBoolean(rs, 3));
                act.put("time.closed", rs.getString(4));
                HashMap user = aus.getUser((String)act.get("performer"));
                act.put("performer.name", user.get("name"));
                user.clear();
                user = null;
                activities.add(act);
                act = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            arraylist = activities;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeActivity(String s)
        throws IIPRequestException
    {
    }

    public DOSChangeable getActivity(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select i.wf$iproouid, x.id, x.name, x.des, x.wf$acttypouid, x.lim, i.wf$dpctouid, x.strmod, x.finmod, i.pri, x.doc, x.ico, x.ins, x.lop, x.jon, x.spl, x.x, x.y, x.w, x.h, x.cmt, zz.id, zz.name, i.wf$staouid, i.cretim, i.strtim, i.clotim, i.limtim, i.plntim, x.ouid from wf$dact x, wf$ipro z, wf$dpro zz, wf$iact i where x.ouid=i.wf$dactouid and z.ouid=i.wf$iproouid and zz.ouid=z.wf$dproouid and i.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            String string = null;
            if(rs.next())
            {
                dosObject.put("ouid", activityOUID);
                dosObject.put("ouid@process", "wf$ipro@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("identifier", rs.getString(2));
                dosObject.put("name", rs.getString(3));
                dosObject.put("description", rs.getString(4));
                dosObject.put("type", rs.getString(5));
                dosObject.put("limit", Utils.getInteger(rs, 6));
                dosObject.put("performer", rs.getString(7));
                dosObject.put("mode.start", rs.getString(8));
                dosObject.put("mode.finish", rs.getString(9));
                dosObject.put("priority", Utils.getInteger(rs, 10));
                dosObject.put("document", rs.getString(11));
                dosObject.put("icon", rs.getString(12));
                dosObject.put("instantiation", Utils.getInteger(rs, 13));
                dosObject.put("loop", rs.getString(14));
                dosObject.put("join", rs.getString(15));
                dosObject.put("split", rs.getString(16));
                dosObject.put("x", Utils.getInteger(rs, 17));
                dosObject.put("y", Utils.getInteger(rs, 18));
                dosObject.put("w", Utils.getInteger(rs, 19));
                dosObject.put("h", Utils.getInteger(rs, 20));
                dosObject.put("is.container", Utils.getBoolean(rs, 21));
                dosObject.put("process.identifier", rs.getString(22));
                dosObject.put("process.name", rs.getString(23));
                dosObject.put("ouid@workflow.status", rs.getString(24));
                dosObject.put("time.created", rs.getString(25));
                dosObject.put("time.started", rs.getString(26));
                dosObject.put("time.closed", rs.getString(27));
                dosObject.put("time.limit.to.finish", rs.getString(28));
                dosObject.put("time.plan.to.start", rs.getString(29));
                dosObject.put("ouid@activity.definition", "wf$dact@" + Long.toHexString(rs.getLong(30)));
                if(dosObject.get("performer") != null)
                {
                    HashMap userData = aus.getUser((String)dosObject.get("performer"));
                    if(userData != null)
                    {
                        dosObject.put("performer.name", userData.get("name"));
                        userData.clear();
                        userData = null;
                    }
                }
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setActivity(DOSChangeable activity)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activity, "ouid");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String userId = null;
        HashMap context = null;
        long realOuid = Utils.getRealLongOuid((String)activity.get("ouid"));
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iact set wf$dpctouid=?, limtim=?, plntim=? where ouid=? ");
            int i = 1;
            String string = null;
            userId = (String)activity.get("performer");
            if(Utils.isNullString(userId))
            {
                context = getClientContext();
                if(context != null)
                {
                    userId = (String)context.get("userId");
                    context = null;
                }
            }
            stat.setString(i, userId);
            i++;
            string = (String)activity.get("time.limit.to.finish");
            if(Utils.isNullString(string))
            {
                stat.setString(i, null);
                i++;
            } else
            {
                calendar.setTime(sdf2.parse(string));
                calendar.set(10, 23);
                calendar.set(12, 59);
                calendar.set(13, 59);
                stat.setString(i, sdf3.format(calendar.getTime()));
                i++;
            }
            string = (String)activity.get("time.plan.to.start");
            if(Utils.isNullString(string))
            {
                stat.setString(i, null);
                i++;
            } else
            {
                calendar.setTime(sdf2.parse(string));
                calendar.set(10, 23);
                calendar.set(12, 59);
                calendar.set(13, 59);
                stat.setString(i, sdf3.format(calendar.getTime()));
                i++;
            }
            stat.setLong(i, realOuid);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                stat = con.prepareStatement("update wf$dact set wf$acttypouid=? where ouid=(select x.wf$dactouid from wf$iact x where x.ouid=? ) ");
                i = 1;
                stat.setString(i, (String)activity.get("type"));
                i++;
                stat.setLong(i, realOuid);
                i++;
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                if(rows == 1)
                {
                    con.commit();
                } else
                {
                    try
                    {
                        con.rollback();
                    }
                    catch(SQLException sqlexception1) { }
                    throw new IIPRequestException("Can not modify a object.");
                }
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception2) { }
                throw new IIPRequestException("Can not modify a object.");
            }
        }
        catch(ParseException e)
        {
            try
            {
                con.rollback();
            }
            catch(SQLException sqlexception) { }
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
        return;
    }

    public String lookupActivityByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(p.ouid) from wf$dact p where p.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dact@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupActivityByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList resultList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$dact p where p.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList();
            for(; rs.next(); resultList.add("wf$dpro@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            resultList.trimToSize();
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void startActivity(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ResultSet rs;
        long realOuid;
        String activityDefinitionOuid;
        String processInstanceOuid;
        String status;
        String finishMode;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        rs = null;
        realOuid = Utils.getRealLongOuid(activityOUID);
        activityDefinitionOuid = null;
        processInstanceOuid = null;
        status = null;
        finishMode = null;
        String objectOUID;
        SQLException e;
        DOSChangeable subProcess;
        String type;
        String performer;
        int i;
        ArrayList ouidList;
        Object returnObject;
        String typeOUID;
        String processOUID;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select 'T' from wf$iact ia, wf$dtrs dt, wf$iact ia2, wf$dact da where dt.actouid2=ia.wf$dactouid and dt.typ in ('N','A') and ia2.wf$dactouid=dt.actouid  and da.ouid=ia.wf$dactouid and da.jon='A' and ia2.wf$iproouid=ia.wf$iproouid and ia2.clotim is null and ia.ouid=? ");
            stat.setLong(1, realOuid);
            rs = stat.executeQuery();
            if(rs.next())
            {
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                return;
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        objectOUID = null;
        subProcess = null;
        type = null;
        performer = null;
        stat = con.prepareStatement("select da.wf$acttypouid, ia.wf$dpctouid, da.ouid, ia.wf$iproouid, ia.wf$staouid, da.finmod from wf$dact da, wf$iact ia where da.ouid=ia.wf$dactouid and ia.ouid=? ");
        i = 1;
        stat.setLong(i, realOuid);
        i++;
        rs = stat.executeQuery();
        if(rs.next())
        {
            type = rs.getString(1);
            performer = rs.getString(2);
            activityDefinitionOuid = "wf$dact@" + Utils.getLongToHexString(rs, 3);
            processInstanceOuid = "wf$ipro@" + Utils.getLongToHexString(rs, 4);
            status = rs.getString(5);
            finishMode = rs.getString(6);
        }
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        stat = con.prepareStatement("select d.wf$dosouid from wf$iact ia, wf$dos d where ia.ouid=? and d.wf$iproouid=ia.wf$iproouid");
        stat.setLong(1, realOuid);
        ouidList = new ArrayList();
        ouidList.add(activityOUID);
        for(rs = stat.executeQuery(); rs.next(); ouidList.add(rs.getString(1)));
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        returnObject = Utils.executeScriptFile(dos.getEventName(activityDefinitionOuid, "start.before"), dss, ouidList, true);
        if(returnObject != null && (returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
        {
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            markActivityForInboxControl(activityOUID, "J");
            finishActivity(activityOUID);
            return;
        }
        stat = con.prepareStatement("update wf$iact set strtim=to_char(sysdate,'YYYYMMDDHH24MISS'), clotim=null, wf$staouid=110 where ouid=? and wf$staouid<>160 ");
        i = 1;
        stat.setLong(i, realOuid);
        i++;
        int rows = stat.executeUpdate();
        stat.close();
        stat = null;
        if(rows > -1)
        {
            con.commit();
        } else
        {
            try
            {
                con.rollback();
            }
            catch(SQLException sqlexception) { }
            throw new IIPRequestException("Can not modify a object.");
        }
        if(type.equals("100") || type.equals("200"))
        {
            stat = con.prepareStatement("select wf$dwrkouid from wf$iwrk where wf$iactouid=? ");
            stat.setLong(1, realOuid);
            rs = stat.executeQuery();
            ArrayList works = new ArrayList();
            for(; rs.next(); works.add(rs.getString(1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.close();
            con = null;
            xc.close();
            xc = null;
            if(works.size() > 0)
            {
                for(Iterator enum = works.iterator(); enum.hasNext(); startWorkItem((String)enum.next(), activityOUID));
                works.clear();
                works = null;
            }
        }
        if(!type.equals("200"))
            break MISSING_BLOCK_LABEL_896;
        if(con != null)
        {
            con.close();
            con = null;
            xc.close();
            xc = null;
        }
        typeOUID = null;
        processOUID = null;
        subProcess = getSubProcess(activityOUID);
        if(subProcess == null)
            break MISSING_BLOCK_LABEL_873;
        typeOUID = (String)subProcess.get("type");
        processOUID = (String)subProcess.get("ouid@process");
        if(Utils.isNullString(processOUID) || Utils.isNullString(typeOUID))
        {
            returnObject = Utils.executeScriptFile(dos.getEventName(activityDefinitionOuid, "start.after"), dss, ouidList, true);
            return;
        }
        startProcess(processOUID);
        subProcess.clear();
        subProcess = null;
        if(typeOUID.equals("130"))
            finishActivity(activityOUID);
        if(type.equals("170") || type.equals("190"))
            finishActivity(activityOUID);
        if(type.equals("180"))
        {
            stat = con.prepareStatement("update wf$iact set wf$staouid='100', clotim=null, strtim=null, read=null, closed=null where wf$iproouid=(select a.wf$iproouid from wf$iact a where ouid=? ) and clotim is not null ");
            stat.setLong(1, realOuid);
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows > 0)
            {
                stat = con.prepareStatement("update wf$ipro set wf$staouid='120', clotim=null where ouid=(select a.wf$iproouid from wf$iact a where ouid=? )");
                stat.setLong(1, realOuid);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("update wf$iact set wf$staouid='120' where ouid=? ");
                stat.setLong(1, realOuid);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("update wf$iwrk set wf$staouid='100', tgtouid=null where wf$iactouid=? ");
                stat.setLong(1, realOuid);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("update wf$iact set read=null, closed=null where wf$iproouid=(select a.wf$iproouid from wf$iact a where ouid=? ) ");
                stat.setLong(1, realOuid);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("update wf$iacts set read=null, closed=null where wf$iactouid in (select b.ouid from wf$iact a, wf$iact b where a.ouid=? and b.wf$iproouid=a.wf$iproouid) ");
                stat.setLong(1, realOuid);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
            }
            con.commit();
        }
        if(type.equals("210"))
        {
            stat = con.prepareStatement("insert into wf$lock (ouid,wf$iproouid) select d.wf$dosouid, ia.wf$iproouid from wf$iact ia, wf$dos d where ia.ouid=? and d.wf$iproouid=ia.wf$iproouid and not exists (select 'T' from wf$lock l where l.ouid=d.wf$dosouid )");
            stat.setLong(1, realOuid);
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 1)
            {
                stat = con.prepareStatement("update wf$iact set strtim=null, wf$staouid=100 where ouid=? and clotim is null ");
                stat.setLong(1, realOuid);
                stat.executeUpdate();
                stat = null;
                stat = con.prepareStatement("update wf$ipro set wf$staouid=120 where ouid=(select ia.wf$iproouid from wf$iact ia where ia.ouid=? and rownum=1 )");
                stat.setLong(1, realOuid);
                stat.executeUpdate();
                stat = null;
                con.commit();
                throw new IIPRequestException("Already Locked Object.");
            }
            con.commit();
            finishActivity(activityOUID);
        } else
        if(type.equals("220"))
        {
            stat = con.prepareStatement("delete from wf$lock where ouid in (select d.wf$dosouid from wf$iact ia, wf$dos d where ia.ouid=? and d.wf$iproouid=ia.wf$iproouid ) ");
            stat.setLong(1, realOuid);
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            finishActivity(activityOUID);
        } else
        if(type.equals("240"))
        {
            ArrayList changeStatusList = listActivityStatus(activityDefinitionOuid);
            ArrayList tempList = null;
            HashMap changeStatusMap = null;
            String toId = null;
            if(!Utils.isNullArrayList(changeStatusList))
            {
                changeStatusMap = new HashMap();
                for(int x = 0; x < changeStatusList.size(); x++)
                {
                    tempList = (ArrayList)changeStatusList.get(x);
                    changeStatusMap.put(tempList.get(0) + "^" + tempList.get(1), tempList.get(2));
                    tempList = null;
                }

                changeStatusList.clear();
                changeStatusList = null;
                DOSChangeable processInstance = getProcess(processInstanceOuid);
                tempList = (ArrayList)processInstance.get("ouid@object");
                processInstance.clear();
                processInstance = null;
                for(int x = 0; x < tempList.size(); x++)
                {
                    toId = (String)changeStatusMap.get(dos.getClassOuid((String)tempList.get(x)) + "^" + dos.getStatus((String)tempList.get(x)));
                    if(Utils.isNullString(toId))
                        toId = (String)changeStatusMap.get("DEFAULT^" + dos.getStatus((String)tempList.get(x)));
                    if(!Utils.isNullString(toId))
                        dos.setStatus((String)tempList.get(x), toId);
                }

                tempList.clear();
                tempList = null;
                changeStatusMap.clear();
                changeStatusMap = null;
            }
            finishActivity(activityOUID);
        }
        if(type.equals("120") || type.equals("130"))
        {
            if(!Utils.isNullString(status) && status.equals("160"))
            {
                markActivityForInboxControl(activityOUID, "A");
                finishActivity(activityOUID);
            }
        } else
        if(type.equals("110") && "A".equals(finishMode))
            finishActivity(activityOUID);
        con.close();
        con = null;
        xc.close();
        xc = null;
        returnObject = Utils.executeScriptFile(dos.getEventName(activityDefinitionOuid, "start.after"), dss, ouidList, true);
        break MISSING_BLOCK_LABEL_1920;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return;
    }

    public void finishActivity(String activityOUID, String transitionOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            DOSChangeable actDef = getActivityDefinition(activityOUID);
            long realOuid = Utils.getRealLongOuid(activityOUID);
            boolean doEscalation = false;
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select d.wf$dosouid from wf$iact ia, wf$dos d where ia.ouid=? and d.wf$iproouid=ia.wf$iproouid");
            stat.setLong(1, realOuid);
            ArrayList ouidList = new ArrayList();
            ouidList.add(activityOUID);
            for(rs = stat.executeQuery(); rs.next(); ouidList.add(rs.getString(1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            Object returnObject = Utils.executeScriptFile(dos.getEventName((String)actDef.get("ouid"), "finish.before"), dss, ouidList, true);
            String closed = null;
            String status = null;
            int i = 0;
            String userId = null;
            HashMap context = getClientContext();
            if(context != null)
            {
                userId = (String)context.get("userId");
                context = null;
            }
            stat = con.prepareStatement("select ia.closed from wf$iact ia where ia.ouid=? ");
            stat.setLong(1, realOuid);
            rs = stat.executeQuery();
            if(rs.next())
                closed = rs.getString(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            if(!"R".equals(closed))
            {
                stat = con.prepareStatement("update wf$iacts set clotim=to_char(sysdate,'YYYYMMDDHH24MISS') where wf$iactouid=? and wf$dpctouid=? and clotim is null ");
                i = 1;
                stat.setLong(i, realOuid);
                i++;
                stat.setString(i, userId);
                int rows = stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("update wf$iact set wf$staouid=decode(wf$staouid,160,160,130), clotim=to_char(sysdate,'YYYYMMDDHH24MISS') where ouid=? and clotim is null and (not exists (select 'T' from wf$iacts x where x.wf$iactouid=ouid and clotim is null) or exists (select 'T' from wf$dact y where y.ouid=wf$dactouid and finmod='A'))");
                i = 1;
                stat.setLong(i, realOuid);
                i++;
                int rows2 = stat.executeUpdate();
                stat.close();
                stat = null;
                if(rows2 > 0)
                {
                    stat = con.prepareStatement("update wf$iacts set clotim=to_char(sysdate,'YYYYMMDDHH24MISS'), closed='A' where wf$iactouid=? and clotim is null ");
                    i = 1;
                    stat.setLong(i, realOuid);
                    stat.executeUpdate();
                    stat.close();
                    stat = null;
                }
                if(rows + rows2 > 0)
                {
                    con.commit();
                } else
                {
                    try
                    {
                        con.rollback();
                    }
                    catch(SQLException sqlexception1) { }
                    throw new IIPRequestException("Can not modify a object.");
                }
                stat = con.prepareStatement("select 'T' from wf$iact ia, wf$iact ia2 where ia.wf$iproouid=ia2.wf$iproouid and ia.clotim is null and rownum=1 and ia2.ouid=? ");
                stat.setLong(1, realOuid);
                rs = stat.executeQuery();
                if(rs.next())
                {
                    if(rs.getString(1) != null)
                        doEscalation = false;
                } else
                {
                    doEscalation = true;
                }
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                stat = con.prepareStatement("select 'T' from wf$iact ia, wf$dact da where ia.ouid=? and da.ouid=ia.wf$dactouid and da.wf$acttypouid=190 ");
                stat.setLong(1, realOuid);
                rs = stat.executeQuery();
                if(rs.next() && rs.getString(1) != null)
                    doEscalation = true;
                rs.close();
                rs = null;
                stat.close();
                stat = null;
            } else
            {
                stat = con.prepareStatement("update wf$iact set wf$staouid=100, strtim=null where ouid=? and clotim is null ");
                i = 1;
                stat.setLong(i, realOuid);
                i++;
                int rows = stat.executeUpdate();
                stat.close();
                stat = null;
                if(rows == 1)
                {
                    con.commit();
                } else
                {
                    try
                    {
                        con.rollback();
                    }
                    catch(SQLException sqlexception) { }
                    throw new IIPRequestException("Can not modify a object.");
                }
            }
            String tempString = null;
            ArrayList nextActivities = getNextActivities(activityOUID);
            DOSChangeable aRecord = null;
            for(Iterator enum = nextActivities.iterator(); enum.hasNext();)
            {
                aRecord = (DOSChangeable)enum.next();
                tempString = (String)aRecord.get("mode.start");
                if(tempString == null || tempString.equals("A"))
                {
                    tempString = (String)aRecord.get("type@transition");
                    if(tempString.equals("A"))
                    {
                        if(tempString.equals(closed))
                            startActivity((String)aRecord.get("ouid"));
                    } else
                    if(tempString.equals("R"))
                    {
                        if(tempString.equals(closed))
                            if(Utils.isNullString(transitionOUID))
                                startActivity((String)aRecord.get("ouid"));
                            else
                            if(!Utils.isNullString(transitionOUID) && aRecord.get("ouid@transition").equals(Long.toString(Utils.getRealLongOuid(transitionOUID))))
                                startActivity((String)aRecord.get("ouid"));
                    } else
                    if(tempString.equals("N"))
                        if(Utils.isNullString(closed) || "A".equals(closed))
                            startActivity((String)aRecord.get("ouid"));
                        else
                        if(!Utils.isNullString(closed) && "R".equals(closed))
                        {
                            markActivityForInboxControl((String)aRecord.get("ouid"), "J");
                            startActivity((String)aRecord.get("ouid"));
                        }
                }
                aRecord = null;
            }

            nextActivities = null;
            returnObject = Utils.executeScriptFile(dos.getEventName((String)actDef.get("ouid"), "finish.after"), dss, ouidList, true);
            if(doEscalation)
                finishProcess(activityOUID);
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
        return;
    }

    public void finishActivity(String activityOUID)
        throws IIPRequestException
    {
        finishActivity(activityOUID, "");
    }

    public String getActivityStatus(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select wf$staouid from wf$iact where ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = rs.getString(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setActivityStatus(String activityOUID, String statusOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        Utils.checkMandatoryString(statusOUID, "statusOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iact set wf$staouid=? where ouid=? ");
            int i = 1;
            stat.setString(i, statusOUID);
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public void markActivityForInboxControl(String activityOUID, String value)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        Utils.checkMandatoryString(value, "value");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        PreparedStatement stat2 = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            String userId = null;
            HashMap context = getClientContext();
            if(context != null)
            {
                userId = (String)context.get("userId");
                context = null;
            }
            if(value.equals("R"))
            {
                stat = con.prepareStatement("update wf$iact set read='T' where ouid=? ");
                stat2 = con.prepareStatement("update wf$iacts set read='T' where wf$iactouid=? and wf$dpctouid=? ");
            } else
            if(value.equals("C"))
            {
                stat = con.prepareStatement("update wf$iact set closed='T' where ouid=? ");
                stat2 = con.prepareStatement("update wf$iacts set closed='T' where wf$iactouid=? and wf$dpctouid=? ");
            } else
            if(value.equals("A"))
            {
                stat = con.prepareStatement("update wf$iact set closed='A' where ouid=? ");
                stat2 = con.prepareStatement("update wf$iacts set closed='A' where wf$iactouid=? and wf$dpctouid=? ");
            } else
            if(value.equals("J"))
            {
                stat = con.prepareStatement("update wf$iact set closed='R' where ouid=? ");
                stat2 = con.prepareStatement("update wf$iacts set closed='R' where wf$iactouid=? and wf$dpctouid=? ");
            } else
            {
                stat = con.prepareStatement("update wf$iact set read=null, closed=null where ouid=? ");
                stat2 = con.prepareStatement("update wf$iacts set read=null, closed=null where wf$iactouid=? and wf$dpctouid=? ");
            }
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat2.setLong(1, Utils.getRealLongOuid(activityOUID));
            stat2.setString(2, userId);
            rows += stat2.executeUpdate();
            stat2.close();
            stat2 = null;
            if(rows > 0)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public DOSChangeable getSubworkflow(String activityOUID)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList getSplitTransitions(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        long realOuid;
        boolean isDefinition;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        realOuid = Utils.getRealLongOuid(activityOUID);
        isDefinition = activityOUID.startsWith("wf$dact@");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
                stat = con.prepareStatement("select dt.ouid, dt.wf$dproouid, dt.id, dt.actouid, dt.actouid2, dt.name, dt.des, dt.typ, dp.id, dp.name, da1.id, da1.name, da2.id, da2.name, da1.wf$acttypouid, da2.wf$acttypouid from wf$dtrs dt, wf$dact da1, wf$dact da2, wf$dpro dp where da1.ouid=dt.actouid and da2.ouid=dt.actouid2 and dp.ouid=dt.wf$dproouid and dt.actouid=? ");
            else
                stat = con.prepareStatement("select dt.ouid, ip.ouid, dt.id, ia.ouid, ia2.ouid, dt.name, dt.des, dt.typ, dp.id, dp.name, da.id, da.name, da2.id, da2.name, da.wf$acttypouid, da2.wf$acttypouid from wf$iact ia, wf$dact da, wf$dtrs dt, wf$iact ia2, wf$dact da2, wf$dpro dp, wf$ipro ip where dt.actouid=ia.wf$dactouid and da.ouid=ia.wf$dactouid and ia2.wf$dactouid=dt.actouid2  and da2.ouid=dt.actouid2 and ia2.wf$iproouid=ia.wf$iproouid and ip.ouid=ia.wf$iproouid and dp.ouid=ip.wf$dproouid and ia.ouid=? ");
            stat.setLong(1, realOuid);
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid", "wf$dtrs@" + Utils.getLongToHexString(rs, 1));
                if(isDefinition)
                    dosObject.put("ouid@process.definition", "wf$dpro@" + Utils.getLongToHexString(rs, 2));
                else
                    dosObject.put("ouid@process.definition", "wf$ipro@" + Utils.getLongToHexString(rs, 2));
                dosObject.put("identifier", rs.getString(3));
                if(isDefinition)
                {
                    dosObject.put("ouid@from.activity", "wf$dact@" + Utils.getLongToHexString(rs, 4));
                    dosObject.put("ouid@to.activity", "wf$dact@" + Utils.getLongToHexString(rs, 5));
                } else
                {
                    dosObject.put("ouid@from.activity", "wf$iact@" + Utils.getLongToHexString(rs, 4));
                    dosObject.put("ouid@to.activity", "wf$iact@" + Utils.getLongToHexString(rs, 5));
                }
                dosObject.put("name", rs.getString(6));
                dosObject.put("description", rs.getString(7));
                dosObject.put("type", rs.getString(8));
                dosObject.put("identifier@process.definition", rs.getString(9));
                dosObject.put("name@process.definition", rs.getString(10));
                dosObject.put("identifier@from.activity", rs.getString(11));
                dosObject.put("name@from.activity", rs.getString(12));
                dosObject.put("identifier@to.activity", rs.getString(13));
                dosObject.put("name@to.activity", rs.getString(14));
                dosObject.put("type@from.activity", rs.getString(15));
                dosObject.put("type@to.activity", rs.getString(16));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getJoinTransitions(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        long realOuid;
        boolean isDefinition;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        realOuid = Utils.getRealLongOuid(activityOUID);
        isDefinition = activityOUID.startsWith("wf$dact@");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
                stat = con.prepareStatement("select dt.ouid, dt.wf$dproouid, dt.id, dt.actouid, dt.actouid2, dt.name, dt.des, dt.typ, dp.id, dp.name, da1.id, da1.name, da2.id, da2.name, da1.wf$acttypouid, da2.wf$acttypouid from wf$dtrs dt, wf$dact da1, wf$dact da2, wf$dpro dp where da1.ouid=dt.actouid and da2.ouid=dt.actouid2 and dp.ouid=dt.wf$dproouid and dt.actouid2=? ");
            else
                stat = con.prepareStatement("select dt.ouid, ip.ouid, dt.id, ia.ouid, ia2.ouid, dt.name, dt.des, dt.typ, dp.id, dp.name, da.id, da.name, da2.id, da2.name, da.wf$acttypouid, da2.wf$acttypouid from wf$iact ia, wf$dact da, wf$dtrs dt, wf$iact ia2, wf$dact da2, wf$dpro dp, wf$ipro ip where dt.actouid=ia.wf$dactouid and da.ouid=ia.wf$dactouid and ia2.wf$dactouid=dt.actouid2  and da2.ouid=dt.actouid2 and ia2.wf$iproouid=ia.wf$iproouid and ip.ouid=ia.wf$iproouid and dp.ouid=ip.wf$dproouid and ia2.ouid=? ");
            stat.setLong(1, realOuid);
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid", "wf$dtrs@" + Utils.getLongToHexString(rs, 1));
                if(isDefinition)
                    dosObject.put("ouid@process.definition", "wf$dpro@" + Utils.getLongToHexString(rs, 2));
                else
                    dosObject.put("ouid@process.definition", "wf$ipro@" + Utils.getLongToHexString(rs, 2));
                dosObject.put("identifier", rs.getString(3));
                if(isDefinition)
                {
                    dosObject.put("ouid@from.activity", "wf$dact@" + Utils.getLongToHexString(rs, 4));
                    dosObject.put("ouid@to.activity", "wf$dact@" + Utils.getLongToHexString(rs, 5));
                } else
                {
                    dosObject.put("ouid@from.activity", "wf$iact@" + Utils.getLongToHexString(rs, 4));
                    dosObject.put("ouid@to.activity", "wf$iact@" + Utils.getLongToHexString(rs, 5));
                }
                dosObject.put("name", rs.getString(6));
                dosObject.put("description", rs.getString(7));
                dosObject.put("type", rs.getString(8));
                dosObject.put("identifier@process.definition", rs.getString(9));
                dosObject.put("name@process.definition", rs.getString(10));
                dosObject.put("identifier@from.activity", rs.getString(11));
                dosObject.put("name@from.activity", rs.getString(12));
                dosObject.put("identifier@to.activity", rs.getString(13));
                dosObject.put("name@to.activity", rs.getString(14));
                dosObject.put("type@from.activity", rs.getString(15));
                dosObject.put("type@to.activity", rs.getString(16));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getNextActivities(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        long realOuid;
        boolean isDefinition;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        realOuid = Utils.getRealLongOuid(activityOUID);
        isDefinition = activityOUID.startsWith("wf$dact@");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
                stat = con.prepareStatement("select da2.ouid, dt.ouid, da2.wf$acttypouid, da2.strmod, da2.finmod, dt.typ from wf$dact da, wf$dtrs dt, wf$dact da2 where dt.actouid=da.ouid and da2.ouid=dt.actouid2 and da2.wf$dproouid=da.wf$dproouid and da.ouid=? ");
            else
                stat = con.prepareStatement("select ia2.ouid, dt.ouid, da2.wf$acttypouid, da2.strmod, da2.finmod, dt.typ from wf$iact ia, wf$dtrs dt, wf$iact ia2, wf$dact da2 where dt.actouid=ia.wf$dactouid and ia2.wf$dactouid=dt.actouid2  and da2.ouid=dt.actouid2 and ia2.wf$iproouid=ia.wf$iproouid and ia.ouid=? ");
            stat.setLong(1, realOuid);
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                if(isDefinition)
                    dosObject.put("ouid", "wf$dact@" + Utils.getLongToHexString(rs, 1));
                else
                    dosObject.put("ouid", "wf$iact@" + Utils.getLongToHexString(rs, 1));
                dosObject.put("ouid@transition", rs.getString(2));
                dosObject.put("type", rs.getString(3));
                dosObject.put("mode.start", rs.getString(4));
                dosObject.put("mode.finish", rs.getString(5));
                dosObject.put("type@transition", rs.getString(6));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public boolean determineLoopCondition(String activityOUID)
        throws IIPRequestException
    {
        return false;
    }

    public DOSChangeable getActivityApplication(String activityOUID)
        throws IIPRequestException
    {
        return null;
    }

    public void setApplicationInvokeParameters(String s, String s1, ArrayList arraylist)
        throws IIPRequestException
    {
    }

    public void setActivityProcedure(String s, ArrayList arraylist)
        throws IIPRequestException
    {
    }

    public DOSChangeable getActivityProcedure(String activityOUID)
        throws IIPRequestException
    {
        return null;
    }

    public String lookupActivityByDataValue(String dataIdentifier, String dataValue)
        throws IIPRequestException
    {
        return null;
    }

    public void setActivityDimension(String activityOUID, Integer x, Integer y, Integer w, Integer h)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(activityOUID.startsWith("wf$dact@"))
                stat = con.prepareStatement("update wf$dact set x=?, y=?, w=?, h=? where ouid=? ");
            else
            if(activityOUID.startsWith("wf$iact@"))
                stat = con.prepareStatement("update wf$dact set x=?, y=?, w=?, h=? where ouid=(select wf$dactouid from wf$iact where ouid=?) ");
            int i = 1;
            stat.setInt(i, Utils.getInt(x));
            i++;
            stat.setInt(i, Utils.getInt(y));
            i++;
            stat.setInt(i, Utils.getInt(w));
            i++;
            stat.setInt(i, Utils.getInt(h));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String getDefinitionOfActivity(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select a.wf$dactouid from wf$iact a where a.ouid=? and rownum=1 ");
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dact@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String createTransition(DOSChangeable transition)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(transition, "ouid@process");
        Utils.checkMandatoryString(transition, "identifier");
        Utils.checkMandatoryString(transition, "ouid@act1");
        Utils.checkMandatoryString(transition, "ouid@act2");
        Utils.checkMandatoryString(transition, "type");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dtrs@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            String ouidProcess = (String)transition.get("ouid@process");
            long realOuidProcess = Utils.getRealLongOuid(ouidProcess);
            boolean isDefinition = ouidProcess.startsWith("wf$dpro@");
            int i = 1;
            if(isDefinition)
            {
                stat = con.prepareStatement("insert into wf$dtrs (ouid, wf$dproouid, id, actouid, actouid2, name, des, typ) values (?, ?, ?, ?, ?, ?, ?, ?) ");
                stat.setLong(i, realOuid);
                i++;
                stat.setLong(i, realOuidProcess);
                i++;
                stat.setString(i, (String)transition.get("identifier"));
                i++;
                stat.setLong(i, Utils.getRealLongOuid((String)transition.get("ouid@act1")));
                i++;
                stat.setLong(i, Utils.getRealLongOuid((String)transition.get("ouid@act2")));
                i++;
                stat.setString(i, (String)transition.get("name"));
                i++;
                stat.setString(i, (String)transition.get("description"));
                i++;
                stat.setString(i, (String)transition.get("type"));
                i++;
            } else
            {
                stat = con.prepareStatement("insert into wf$dtrs (ouid, wf$dproouid, id, actouid, actouid2, name, des, typ) select ?, wf$dproouid, ?, ?, ?, ?, ?, ? from wf$ipro where ouid=? ");
                stat.setLong(i, realOuid);
                i++;
                stat.setString(i, (String)transition.get("identifier"));
                i++;
                stat.setLong(i, Utils.getRealLongOuid((String)transition.get("ouid@act1")));
                i++;
                stat.setLong(i, Utils.getRealLongOuid((String)transition.get("ouid@act2")));
                i++;
                stat.setString(i, (String)transition.get("name"));
                i++;
                stat.setString(i, (String)transition.get("description"));
                i++;
                stat.setString(i, (String)transition.get("type"));
                i++;
                stat.setLong(i, realOuidProcess);
                i++;
            }
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeTransition(String transitionOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(transitionOUID, "transitionOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dtrs where ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(transitionOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getTransition(String transitionOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(transitionOUID, "transitionOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.wf$dproouid, x.id, x.actouid, x.actouid2, x.name, x.des, x.typ, y.id, y.nam from wf$dtrs x, wf$dpro y where y.ouid=x.wf$dproouid and x.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(transitionOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", transitionOUID);
                dosObject.put("ouid@process", "wf$dpro@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("identifier", rs.getString(2));
                dosObject.put("ouid@from.activity", "wf$dact@" + Long.toHexString(rs.getLong(3)));
                dosObject.put("ouid@to.activity", "wf$dact@" + Long.toHexString(rs.getLong(4)));
                dosObject.put("name", rs.getString(5));
                dosObject.put("description", rs.getString(6));
                dosObject.put("type", rs.getString(7));
                dosObject.put("identifier@process.definition", rs.getString(8));
                dosObject.put("name@process.definition", rs.getString(9));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setTransition(DOSChangeable transition)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(transition, "ouid");
        Utils.checkMandatoryString(transition, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long realOuid = Utils.getRealLongOuid((String)transition.get("ouid"));
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dtrs set name=?,des=?,typ=? where ouid=?");
            int i = 1;
            stat.setString(i, (String)transition.get("name"));
            i++;
            stat.setString(i, (String)transition.get("description"));
            i++;
            stat.setString(i, (String)transition.get("type"));
            i++;
            stat.setLong(i, realOuid);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public String lookupTransitionByIdentifier(String identifier)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(identifier, "identifier");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(p.ouid) from wf$dtrs p where p.id=? ");
            stat.setString(1, identifier);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = "wf$dtrs@" + Long.toHexString(rs.getLong(1));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList lookupTransitionByName(String name)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(name, "name");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList resultList = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select p.ouid from wf$dtrs p where p.name=? ");
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            ArrayList resultList = new ArrayList();
            for(; rs.next(); resultList.add("wf$dtrs@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            resultList.trimToSize();
            con.commit();
            arraylist = resultList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getTrnasitionConditions(String transitionOUID)
        throws IIPRequestException
    {
        return null;
    }

    public void setTransitionConditions(String s, ArrayList arraylist)
        throws IIPRequestException
    {
    }

    public ArrayList determineTrnasitionConditions(String activityOUID)
        throws IIPRequestException
    {
        return null;
    }

    public boolean determineTrnasitionCondition(String transitionOUID)
        throws IIPRequestException
    {
        return false;
    }

    public String createWorkItemDefinition(DOSChangeable workItem)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(workItem, "ouid@activity.definition");
        Utils.checkMandatoryString(workItem, "type");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dwrk@" + Long.toHexString(realOuid);
            String ouidObject = (String)workItem.get("ouid@object");
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dwrk (ouid, wf$dactouid, seq, wf$wrktypouid, tgtouid, req) values (?, ?, '0', ?, ?, ?) ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)workItem.get("ouid@activity.definition")));
            i++;
            stat.setString(i, (String)workItem.get("type"));
            i++;
            stat.setString(i, ouidObject);
            i++;
            Utils.setBoolean(stat, i, (Boolean)workItem.get("is.required"));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void removeWorkItemDefinition(String workItemOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from wf$dwrk where ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(workItemOUID));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public DOSChangeable getWorkItemDefinition(String workItemOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select w.wf$dactouid, w.seq, w.wf$wrktypouid, w.tgtouid, w.req, wt.name from wf$dwrk w, wf$wrktyp wt where wt.ouid=w.wf$wrktypouid and w.ouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(workItemOUID));
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid", workItemOUID);
                dosObject.put("ouid@activity.definition", "wf$dact@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("sequence", rs.getString(2));
                dosObject.put("type", rs.getString(3));
                dosObject.put("ouid@object", rs.getString(4));
                dosObject.put("is.required", rs.getString(5));
                dosObject.put("type.name", rs.getString(7));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList getWorkItemDefinitions(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select w.ouid, w.seq, w.wf$wrktypouid, w.tgtouid, w.req, wt.name from wf$dwrk w, wf$wrktyp wt where wt.ouid=w.wf$wrktypouid and w.wf$dactouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid", "wf$dact@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("ouid@activity.definition", activityOUID);
                dosObject.put("type", rs.getString(3));
                dosObject.put("ouid@object", rs.getString(4));
                dosObject.put("is.required", Utils.getBoolean(rs, 5));
                dosObject.put("type.name", rs.getString(6));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setWorkItemDefinition(DOSChangeable workItem)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItem, "ouid");
        Utils.checkMandatoryString(workItem, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$dwrk set wf$wrktypouid=?, tgtouid=?, req=? where ouid=? ");
            int i = 1;
            String ouidObject = (String)workItem.get("ouid@object");
            stat.setString(i, (String)workItem.get("type"));
            i++;
            stat.setString(i, ouidObject);
            i++;
            Utils.setBoolean(stat, i, (Boolean)workItem.get("is.required"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)workItem.get("ouid")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public void createWorkItem(DOSChangeable workItem)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItem, "ouid@work.definition");
        Utils.checkMandatoryString(workItem, "ouid@activity");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into wf$iwrk (wf$dwrkouid, wf$iactouid, wf$staouid) select ?,ia.ouid, ia.wf$staouid from wf$iact ia where ia.ouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid((String)workItem.get("ouid@work.definition")));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)workItem.get("ouid@activity")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
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
        return;
    }

    public void removeWorkItem(String s, String s1)
        throws IIPRequestException
    {
    }

    public DOSChangeable getWorkItem(String workItemOUID, String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dw.seq, dw.wf$wrktypouid, dw.tgtouid, dw.req, wt.name, iw.wf$staouid, iw.tgtouid from wf$iwrk iw, wf$dwrk dw, wf$wrktyp wt where wt.ouid=dw.wrktyp and dw.ouid=iw.wf$dwrkouid and iw.wf$dwrkouid=? and iw.wf$iactouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(workItemOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid@work.definition", workItemOUID);
                dosObject.put("ouid@activity", activityOUID);
                dosObject.put("sequence", rs.getString(1));
                dosObject.put("type", rs.getString(2));
                dosObject.put("ouid@object", rs.getString(3));
                dosObject.put("is.required", Utils.getBoolean(rs, 4));
                dosObject.put("type.name", rs.getString(5));
                dosObject.put("ouid@workflow.status", rs.getString(6));
                dosObject.put("ouid@object.target", rs.getString(7));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setWorkItem(DOSChangeable workItem)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItem, "ouid@work.definition");
        Utils.checkMandatoryString(workItem, "ouid@activity");
        Utils.checkMandatoryString(workItem, "type");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iwrk set wf$staouid=?, tgtouid=? where wf$dwrkouid=? and wf$iactouid=? ");
            int i = 1;
            stat.setString(i, (String)workItem.get("ouid@workflow.status"));
            i++;
            stat.setString(i, (String)workItem.get("ouid@object.target"));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)workItem.get("ouid@work.definition")));
            i++;
            stat.setLong(i, Utils.getRealLongOuid((String)workItem.get("ouid@activity")));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            String string = (String)workItem.get("ouid@object.target");
            if(Utils.isNullString(string))
                return;
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
        return;
    }

    public ArrayList getWorkItems(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select iw.wf$dwrkouid, dw.seq, dw.wf$wrktypouid, dw.tgtouid, dw.req, wt.name, s.name, iw.tgtouid, iw.wf$staouid from wf$iwrk iw, wf$dwrk dw, wf$wrktyp wt, wf$sta s where wt.ouid=dw.wf$wrktypouid and dw.ouid=iw.wf$dwrkouid and s.ouid=iw.wf$staouid and iw.wf$iactouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid@work.definition", "wf$dwrk@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("ouid@activity", activityOUID);
                dosObject.put("sequence", rs.getString(2));
                dosObject.put("type", rs.getString(3));
                dosObject.put("ouid@object", rs.getString(4));
                dosObject.put("is.required", Utils.getBoolean(rs, 5));
                dosObject.put("type.name", rs.getString(6));
                dosObject.put("workflow.status.name", rs.getString(7));
                dosObject.put("ouid@workflow.status", rs.getString(8));
                dosObject.put("ouid@object.target", rs.getString(9));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void startWorkItem(String workItemOUID, String activityOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iwrk set wf$staouid=110 where wf$dwrkouid=? and wf$iactouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(workItemOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public void finishWorkItem(String workItemOUID, String activityOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iwrk set wf$staouid=130 where wf$dwrkouid=? and wf$iactouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(workItemOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            stat = con.prepareStatement("select 'T' from wf$iact ia, wf$dact da where da.ouid=ia.wf$dactouid and da.finmod='A' and ia.ouid=? and not exists (select 'T' from wf$iwrk iw, wf$dwrk dw where dw.ouid=iw.wf$dwrkouid and iw.wf$iactouid=ia.ouid and iw.wf$staouid in (100,110,120) and dw.req='T' and rownum=1 ) ");
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            boolean activityWillFinish = false;
            rs = stat.executeQuery();
            if(rs.next() && rs.getString(1).equals("T"))
                activityWillFinish = true;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            if(activityWillFinish)
                finishActivity(activityOUID);
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
        return;
    }

    public String getWorkItemStatus(String workItemOUID, String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String returnValue;
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnValue = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select w.wf$staouid from wf$iwrk w where w.wf$dwrkouid=? and w.wf$iactouid=? ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(workItemOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = rs.getString(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setWorkItemStatus(String workItemOUID, String activityOUID, String statusOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        Utils.checkMandatoryString(statusOUID, "statusOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iwrk set wf$staouid=? where wf$dwrkouid=? and wf$iactouid=? ");
            int i = 1;
            stat.setString(i, statusOUID);
            i++;
            stat.setLong(i, Utils.getRealLongOuid(workItemOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(activityOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public DOSChangeable lookupWorkItemByTargetObject(String objectOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(objectOUID, "objectOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSChangeable dosObject = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select wf$dwrkouid,wf$iactouid from wf$iwrk where tgtouid=? and rownum=1 ");
            stat.setString(1, objectOUID);
            ResultSet rs = stat.executeQuery();
            DOSChangeable dosObject = new DOSChangeable();
            if(rs.next())
            {
                dosObject.put("ouid@work.definition", "wf$dwrk@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("ouid@activity", "wf@iact@" + Long.toHexString(rs.getLong(2)));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void setResponsibles(String objectOUID, ArrayList responsibles)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(objectOUID, "objectOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete wf$dres where objouid=? ");
            stat.setString(1, objectOUID);
            int rows = stat.executeUpdate();
            if(rows < 0)
                rows = 0;
            if(!Utils.isNullArrayList(responsibles))
            {
                stat.close();
                stat = null;
                stat = con.prepareStatement("insert into wf$dres (objouid,wf$pctouid,whn) values (?, ?, ?) ");
                stat.setString(1, objectOUID);
                DOSChangeable aRecord = null;
                String string = null;
                boolean flag = false;
                for(Iterator enum = responsibles.iterator(); enum.hasNext();)
                {
                    aRecord = (DOSChangeable)enum.next();
                    stat.setString(2, (String)aRecord.get("participant"));
                    string = ((Boolean)aRecord.get("on.start")).booleanValue() ? "T" : "F";
                    string = string + (((Boolean)aRecord.get("on.finish")).booleanValue() ? "T" : "F");
                    string = string + (((Boolean)aRecord.get("on.delay")).booleanValue() ? "T" : "F");
                    stat.setString(3, string);
                    rows += stat.executeUpdate();
                    aRecord = null;
                }

            }
            stat.close();
            stat = null;
            if(rows > -1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public ArrayList getResponsibles(String objectOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(objectOUID, "objectOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select r.wf$pctouid, r.whn from wf$dres r where r.objouid=? ");
            stat.setString(1, objectOUID);
            ResultSet rs = stat.executeQuery();
            String string = null;
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("participant", rs.getString(1));
                string = rs.getString(2);
                if(Utils.isNullString(string))
                {
                    dosObject.put("on.start", Utils.False);
                    dosObject.put("on.finish", Utils.False);
                    dosObject.put("on.delay", Utils.False);
                } else
                {
                    if(string.substring(0, 1).equals("T"))
                        dosObject.put("on.start", Utils.True);
                    else
                        dosObject.put("on.start", Utils.False);
                    if(string.substring(1, 2).equals("T"))
                        dosObject.put("on.finish", Utils.True);
                    else
                        dosObject.put("on.finish", Utils.False);
                    if(string.substring(2, 3).equals("T"))
                        dosObject.put("on.delay", Utils.True);
                    else
                        dosObject.put("on.delay", Utils.False);
                }
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void addChild(String parentActivityOUID, String sequence, String childActivityOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(parentActivityOUID, "parentActivityOUID");
        Utils.checkMandatoryString(sequence, "sequence");
        Utils.checkMandatoryString(childActivityOUID, "childActivityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into wf$dactact (actouid1,seq,actouid2) select ?, ?, ? from dual where not exists (select 'T' from wf$dactact where actouid1=? and seq=? and actouid2=?) ");
            int i = 1;
            long parentOuid = Utils.getRealLongOuid(parentActivityOUID);
            long childOuid = Utils.getRealLongOuid(childActivityOUID);
            stat.setLong(i, parentOuid);
            i++;
            stat.setString(i, sequence);
            i++;
            stat.setLong(i, childOuid);
            i++;
            stat.setLong(i, parentOuid);
            i++;
            stat.setString(i, sequence);
            i++;
            stat.setLong(i, childOuid);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public void removeChild(String parentActivityOUID, String sequence, String childActivityOUID)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(parentActivityOUID, "parentActivityOUID");
        Utils.checkMandatoryString(sequence, "sequence");
        Utils.checkMandatoryString(childActivityOUID, "childActivityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            boolean isProcessDefinition = parentActivityOUID.startsWith("wf$dpro@");
            boolean isActivityDefinition = parentActivityOUID.startsWith("wf$dact@");
            boolean isProcessInstance = parentActivityOUID.startsWith("wf$ipro@");
            boolean isActivityInstance = parentActivityOUID.startsWith("wf$iact@");
            if(isProcessDefinition || isActivityDefinition)
                stat = con.prepareStatement("delete from wf$dactact where actouid1=? and seq=? and actouid2=? ");
            else
            if(isActivityInstance)
                stat = con.prepareStatement("delete from wf$dactact where actouid1=(select wf$dactouid from wf$iact where ouid=? ) and seq=? and actouid2=(select wf$dactouid from wf$iact where ouid=? ) ");
            else
            if(isProcessInstance)
                stat = con.prepareStatement("delete from wf$dactact where actouid1=(select wf$dproouid from wf$ipro where ouid=? ) and seq=? and actouid2=(select wf$dactouid from wf$iact where ouid=? ) ");
            int i = 1;
            stat.setLong(i, Utils.getRealLongOuid(parentActivityOUID));
            i++;
            stat.setString(i, sequence);
            i++;
            stat.setLong(i, Utils.getRealLongOuid(childActivityOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public ArrayList getChildren(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            boolean isProcessDefinition = activityOUID.startsWith("wf$dpro@");
            boolean isActivityDefinition = activityOUID.startsWith("wf$dact@");
            boolean isProcessInstance = activityOUID.startsWith("wf$ipro@");
            boolean isActivityInstance = activityOUID.startsWith("wf$iact@");
            String classPrefix = null;
            if(isProcessDefinition || isActivityDefinition)
            {
                stat = con.prepareStatement("select wa.ouid, aa.seq, wa.id, wa.name, wa.cmt from wf$dactact aa, wf$dact wa where wa.ouid=aa.actouid2 and aa.actouid1=? order by aa.actouid1, wa.id, aa.seq ");
                classPrefix = "wf$dact@";
            } else
            if(isActivityInstance)
            {
                stat = con.prepareStatement("select wi.ouid, aa.seq, wa.id, wa.name, wa.cmt from wf$dactact aa, wf$dact wa, wf$iact wi, wf$iact wi2 where wa.ouid=aa.actouid2 and wi.wf$dactouid=wa.ouid and aa.actouid1=wi2.wf$dactouid and wi2.ouid=? order by aa.actouid1, wa.id, aa.seq ");
                classPrefix = "wf$iact@";
            } else
            if(isProcessInstance)
            {
                stat = con.prepareStatement("select wi.ouid, aa.seq, wa.id, wa.name, wa.cmt from wf$dactact aa, wf$dact wa, wf$iact wi where wa.ouid=aa.actouid2 and wi.wf$dactouid=wa.ouid and aa.actouid1=(select wf$dproouid from wf$ipro where ouid=? ) order by aa.actouid1, wa.id, aa.seq ");
                classPrefix = "wf$iact@";
            }
            stat.setString(1, activityOUID);
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable dosObject = new DOSChangeable();
                dosObject.put("ouid@activity", classPrefix + rs.getLong(1));
                dosObject.put("sequence", rs.getString(2));
                dosObject.put("identifier", rs.getString(3));
                dosObject.put("name", rs.getString(4));
                dosObject.put("is.container", Utils.getBoolean(rs, 5));
                rowArrayList.add(dosObject);
                dosObject = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String cloneProcessDefinition(String processOUID, String newClassificationOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        long realOuidProcess;
        Utils.checkMandatoryString(processOUID, "processOUID");
        Utils.checkMandatoryString(newClassificationOUID, "newClassificationOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ArrayList acts = null;
        ArrayList newActs = null;
        ArrayList trses = null;
        DOSChangeable aTrs = null;
        ArrayList wbses = null;
        DOSChangeable aWbs = null;
        realOuidProcess = Utils.getRealLongOuid(processOUID);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dpro@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dpro (ouid, wf$dmodouid, id, name, des, uod, aut, ver, chs, cdp, coukey, wf$pctouid, pblsta, vlf, vlt, cls, pri, lim) select ?, wf$dmodouid, id, name, des, uod, aut, ver, chs, cdp, coukey, wf$pctouid, pblsta, vlf, vlt, ?, pri, lim from wf$dpro where ouid=? ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setString(i, newClassificationOUID);
            i++;
            stat.setLong(i, realOuidProcess);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            stat = con.prepareStatement("select ouid from wf$dact where wf$dproouid=? ");
            stat.setLong(1, realOuidProcess);
            ArrayList acts = new ArrayList();
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next(); acts.add("wf$dact@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select ouid, actouid, actouid2 from wf$dtrs where wf$dproouid=? ");
            stat.setLong(1, realOuidProcess);
            ArrayList trses = new ArrayList();
            for(rs = stat.executeQuery(); rs.next();)
            {
                DOSChangeable aTrs = new DOSChangeable();
                aTrs.put("ouid", "wf$dtrs@" + Long.toHexString(rs.getLong(1)));
                aTrs.put("ouid@act1", "wf$dact@" + Long.toHexString(rs.getLong(2)));
                aTrs.put("ouid@act2", "wf$dact@" + Long.toHexString(rs.getLong(3)));
                trses.add(aTrs);
                aTrs = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select actouid1, actouid2 from wf$dactact connect by prior actouid2=actouid1 start with actouid1=? ");
            stat.setLong(1, realOuid);
            ArrayList wbses = new ArrayList();
            for(rs = stat.executeQuery(); rs.next();)
            {
                DOSChangeable aWbs = new DOSChangeable();
                aWbs.put("ouid@act1", "wf$dact@" + Long.toHexString(rs.getLong(1)));
                aWbs.put("ouid@act2", "wf$dact@" + Long.toHexString(rs.getLong(2)));
                wbses.add(aWbs);
                aWbs = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.close();
            con = null;
            xc.close();
            xc = null;
            String newActivityOUID = null;
            String activityOUID = null;
            ArrayList newActs = new ArrayList();
            Iterator enum;
            for(enum = acts.iterator(); enum.hasNext(); newActs.add(newActivityOUID))
            {
                activityOUID = (String)enum.next();
                newActivityOUID = cloneActivityDefinition(activityOUID, newOUID);
            }

            enum = null;
            DOSChangeable aTrs;
            for(enum = trses.iterator(); enum.hasNext(); cloneTransition((String)aTrs.get("ouid"), newOUID, getMatched(acts, newActs, (String)aTrs.get("ouid@act1")), getMatched(acts, newActs, (String)aTrs.get("ouid@act2"))))
                aTrs = (DOSChangeable)enum.next();

            trses.clear();
            trses = null;
            enum = null;
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into wf$dactact (actouid1, seq, actouid2) select ?, seq, ? from wf$dactact where actouid1=? and actouid2=? ");
            String string = null;
            for(enum = wbses.iterator(); enum.hasNext(); stat.executeUpdate())
            {
                DOSChangeable aWbs = (DOSChangeable)enum.next();
                string = (String)aWbs.get("ouid");
                if(string.startsWith("wf$dpro@"))
                    stat.setLong(1, Utils.getRealLongOuid(newOUID));
                else
                    stat.setLong(1, Utils.getRealLongOuid(getMatched(acts, newActs, (String)aWbs.get("ouid@act1"))));
                stat.setLong(2, Utils.getRealLongOuid(getMatched(acts, newActs, (String)aWbs.get("ouid@act2"))));
                stat.setLong(3, Utils.getRealLongOuid((String)aWbs.get("ouid@act1")));
                stat.setLong(4, Utils.getRealLongOuid((String)aWbs.get("ouid@act2")));
            }

            stat.close();
            stat = null;
            con.commit();
            stat = con.prepareStatement("select ap.wf$dproouid, ap.typ, ap.wf$dactouid from wf$dactpro ap, wf$dact wa where ap.wf$dactouid=wa.ouid and wa.wf$acttypouid=200 and wa.wf$dproouid=? ");
            stat.setLong(1, realOuid);
            rs = stat.executeQuery();
            DOSChangeable aSub = null;
            ArrayList subs = new ArrayList();
            while(rs.next()) 
            {
                aSub = new DOSChangeable();
                aSub.put("ouid@process.definition", "wf$dpro@" + Long.toHexString(rs.getLong(1)));
                aSub.put("type", rs.getString(2));
                aSub.put("ouid@activity.definition", "wf$dpro@" + Long.toHexString(rs.getLong(3)));
                subs.add(aSub);
                aSub = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.close();
            con = null;
            xc.close();
            xc = null;
            for(Iterator subsEnum = subs.iterator(); subsEnum.hasNext();)
            {
                aSub = (DOSChangeable)subsEnum.next();
                String newSubProcessOUID = cloneProcessDefinition((String)aSub.get("ouid@process.definition"), newClassificationOUID);
                if(!Utils.isNullString(newSubProcessOUID))
                    setSubProcess(getMatched(acts, newActs, (String)aSub.get("ouid@activity.definition")), newSubProcessOUID, (String)aSub.get("type"));
            }

            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String cloneActivityDefinition(String activityOUID, String newProcessOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        Utils.checkMandatoryString(newProcessOUID, "newProcessOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dact@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            long realOuidActivity = Utils.getRealLongOuid(activityOUID);
            long realOuidProcess = Utils.getRealLongOuid(newProcessOUID);
            ArrayList works = null;
            stat = con.prepareStatement("insert into wf$dact (ouid, wf$dproouid, id, name, des, typ, lim, wf$pctouid, strmod, finmod, pri, doc, ico, ins, lop, jon, spl, x, y, w, h, cmt) select ?, ?, id, name, des, typ, lim, wf$pctouid, strmod, finmod, pri, doc, ico, ins, lop, jon, spl, x, y, w, h, cmt from wf$dact where ouid=? ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, realOuidProcess);
            i++;
            stat.setLong(i, realOuidActivity);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            stat = con.prepareStatement("select ouid from wf$dwrk where wf$dactouid=? ");
            stat.setLong(1, realOuidActivity);
            works = new ArrayList();
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next(); works.add("wf$dact@" + Long.toHexString(rs.getLong(1))));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.close();
            con = null;
            xc.close();
            xc = null;
            for(Iterator enum = works.iterator(); enum.hasNext(); cloneWorkItemDefinition((String)enum.next(), newOUID));
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String cloneWorkItemDefinition(String workItemOUID, String newActivityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(workItemOUID, "workItemOUID");
        Utils.checkMandatoryString(newActivityOUID, "newActivityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dwrk@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dwrk (ouid, wf$dactouid, seq, wf$wrktypouid, tgtouid, req) select ?, ?, seq, wf$wrktypouid, tgtouid, req from wf$dwrk where ouid=? ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, Utils.getRealLongOuid(newActivityOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(workItemOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String cloneProcess(String processOUID)
        throws IIPRequestException
    {
        return null;
    }

    public String cloneActivity(String activityOUID, String newProcessOUID)
        throws IIPRequestException
    {
        return null;
    }

    public String cloneTransition(String transitionOUID, String newProcessOUID, String newFromActivityOUID, String newToActivityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(transitionOUID, "transitionOUID");
        Utils.checkMandatoryString(newProcessOUID, "newProcessOUID");
        Utils.checkMandatoryString(newFromActivityOUID, "newFromActivityOUID");
        Utils.checkMandatoryString(newToActivityOUID, "newToActivityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long realOuid = dos.generateOUID();
            String newOUID = "wf$dtrs@" + Long.toHexString(realOuid);
            if(Utils.isNullString(newOUID))
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not generate OUID.");
            }
            stat = con.prepareStatement("insert into wf$dtrs (ouid, wf$dproouid, id, actouid, actouid2, name, des, typ) select ?, ?, id, ?, ?, name, des, typ from wf$dtrs where ouid=? ");
            int i = 1;
            stat.setLong(i, realOuid);
            i++;
            stat.setLong(i, Utils.getRealLongOuid(newProcessOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(newFromActivityOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(newToActivityOUID));
            i++;
            stat.setLong(i, Utils.getRealLongOuid(transitionOUID));
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows < 0)
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
            con.commit();
            s = newOUID;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    private String getMatched(ArrayList v1, ArrayList v2, String ouid)
    {
        int i = v1.indexOf(ouid);
        if(i >= 0)
            return (String)v2.get(i);
        else
            return null;
    }

    public void setSubProcess(String activityOUID, String processOUID, String type)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        Utils.checkMandatoryString(processOUID, "processOUID");
        if(activityOUID.startsWith("wf$dact@"))
            Utils.checkMandatoryString(type, "type");
        boolean isDefinition = activityOUID.startsWith("wf$dact@");
        boolean isActivity = activityOUID.startsWith("wf$iact@");
        long realOuidActivity = Utils.getRealLongOuid(activityOUID);
        long realOuidProcess = Utils.getRealLongOuid(processOUID);
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
            {
                stat = con.prepareStatement("delete from wf$dactpro where wf$dactouid=? ");
                stat.setLong(1, realOuidActivity);
                stat.executeUpdate();
            } else
            if(isActivity)
            {
                stat = con.prepareStatement("delete from wf$iactpro where wf$iactouid=? ");
                stat.setLong(1, realOuidActivity);
                stat.executeUpdate();
            }
            stat.close();
            stat = null;
            if(isDefinition)
            {
                stat = con.prepareStatement("insert into wf$dactpro (wf$dactouid,wf$dproouid,typ) values (?,?,?) ");
                stat.setLong(1, realOuidActivity);
                stat.setLong(2, realOuidProcess);
                stat.setString(3, type);
            } else
            if(isActivity)
            {
                stat = con.prepareStatement("insert into wf$iactpro (wf$iactouid,wf$iproouid) values (?,?) ");
                stat.setLong(1, realOuidActivity);
                stat.setLong(2, realOuidProcess);
            }
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception) { }
                throw new IIPRequestException("Can not modify a object.");
            }
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
        return;
    }

    public DOSChangeable getSubProcess(String activityOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSChangeable dosObject;
        boolean isDefinition;
        boolean isInstance;
        String classPrefix;
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        dosObject = null;
        isDefinition = activityOUID.startsWith("wf$dact@");
        isInstance = activityOUID.startsWith("wf$iact@");
        classPrefix = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
            {
                stat = con.prepareStatement("select ap.wf$dactouid, ap.wf$dproouid, ap.typ from wf$dactpro ap where ap.wf$dactouid=? ");
                classPrefix = "wf$d";
            } else
            if(isInstance)
            {
                stat = con.prepareStatement("select api.wf$iactouid, api.wf$iproouid, ap.typ from wf$dactpro ap, wf$iactpro api, wf$iact ia, wf$ipro ip where ia.ouid=api.wf$iactouid and ap.wf$dactouid=ia.wf$dactouid and ip.ouid=api.wf$iproouid and ap.wf$dproouid=ip.wf$dproouid and api.wf$iactouid=? ");
                classPrefix = "wf$i";
            }
            stat.setLong(1, Utils.getRealLongOuid(activityOUID));
            ResultSet rs = stat.executeQuery();
            if(rs.next())
            {
                dosObject = new DOSChangeable();
                dosObject.put("ouid@activity", classPrefix + "act@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("ouid@process", classPrefix + "pro@" + Long.toHexString(rs.getLong(2)));
                dosObject.put("type", rs.getString(3));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public DOSChangeable getSuperActivity(String processOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        boolean isDefinition;
        boolean isInstance;
        String classPrefix;
        Utils.checkMandatoryString(processOUID, "processOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        isDefinition = processOUID.startsWith("wf$dpro@");
        isInstance = processOUID.startsWith("wf$ipro@");
        classPrefix = null;
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            DOSChangeable dosObject = null;
            if(isDefinition)
            {
                stat = con.prepareStatement("select ap.wf$dactouid, ap.wf$dproouid, ap.typ from wf$dactpro ap where ap.wf$dproouid=? ");
                classPrefix = "wf$d";
            } else
            if(isInstance)
            {
                stat = con.prepareStatement("select api.wf$iactouid, api.wf$iproouid, ap.typ from wf$dactpro ap, wf$iactpro api, wf$iact ia, wf$ipro ip where ia.ouid=api.wf$iactouid and ap.wf$dactouid=ia.wf$dactouid and ip.ouid=api.wf$iproouid and ap.wf$dproouid=ip.wf$dproouid and api.wf$iproouid=? ");
                classPrefix = "wf$i";
            }
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            dosObject = new DOSChangeable();
            ResultSet rs = stat.executeQuery();
            if(rs.next())
            {
                dosObject.put("ouid@activity", classPrefix + "act@" + Long.toHexString(rs.getLong(1)));
                dosObject.put("ouid@process", classPrefix + "pro@" + Long.toHexString(rs.getLong(2)));
                dosObject.put("type", rs.getString(3));
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            doschangeable = dosObject;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return doschangeable;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public int countActivity(String processOUID, String activityTypeOUID)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        boolean isDefinition;
        boolean isInstance;
        Utils.checkMandatoryString(processOUID, "processOUID");
        Utils.checkMandatoryString(activityTypeOUID, "activityTypeOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        isDefinition = processOUID.startsWith("wf$dpro@");
        isInstance = processOUID.startsWith("wf$ipro@");
        SQLException e;
        int i;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(isDefinition)
                stat = con.prepareStatement("select count(*) from wf$dact da where da.wf$dproouid=? and da.typ=? ");
            else
            if(isInstance)
                stat = con.prepareStatement("select count(*) from wf$dact da, wf$iact ia where da.ouid=ia.wf$dactouid and ia.wf$iproouid=? and da.typ=? ");
            stat.setLong(1, Utils.getRealLongOuid(processOUID));
            stat.setLong(2, Utils.getRealLongOuid(activityTypeOUID));
            ResultSet rs = stat.executeQuery();
            int returnValue = 0;
            if(rs.next())
                returnValue = rs.getInt(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            i = returnValue;
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
        DTMUtil.sqlExceptionHelper(con, e);
        return 0;
    }

    public void setActivityFinishTime(String activityOUID, String finishDate)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOUID, "activityOUID");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update wf$iact set clotim=? where ouid=? ");
            int i = 1;
            String string = null;
            string = finishDate;
            if(Utils.isNullString(string))
            {
                stat.setString(i, sdf1.format(new Date()));
                i++;
            } else
            {
                calendar.setTime(sdf2.parse(string));
                calendar.set(10, 23);
                calendar.set(12, 59);
                calendar.set(13, 59);
                stat.setString(i, sdf1.format(calendar.getTime()));
                i++;
            }
            stat.setString(i, activityOUID);
            i++;
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(rows == 1)
            {
                con.commit();
            } else
            {
                try
                {
                    con.rollback();
                }
                catch(SQLException sqlexception1) { }
                throw new IIPRequestException("Can not modify a object.");
            }
        }
        catch(ParseException e)
        {
            try
            {
                con.rollback();
            }
            catch(SQLException sqlexception) { }
            throw new IIPRequestException(e.getLocalizedMessage());
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
        return;
    }

    public boolean isValidProcess(String processInstanceOUID)
        throws IIPRequestException
    {
        boolean returnValue;
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        long realOuid;
        returnValue = true;
        Utils.checkMandatoryString(processInstanceOUID, "processInstanceOUID");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        realOuid = Utils.getRealLongOuid(processInstanceOUID);
        SQLException e;
        boolean flag;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select 'T' from wf$iact x, wf$dact b where x.wf$iproouid=? and b.ouid=x.wf$dactouid and b.wf$acttypouid in in (130,120,200,100) and not exists (select 'T' from wf$dtrs a where a.wf$dproouid=b.wf$dproouid and a.actouid2=b.ouid ) and rownum=1 union all select 'T' from wf$iact x, wf$dact b, wf$dtrs t, wf$dtrs s, wf$iact y, wf$dact c where x.wf$iproouid=? and b.ouid=x.wf$dactouid and b.wf$acttypouid in (130,120,200,100) and t.actouid2=b.ouid and s.actouid=t.actouid and c.ouid=s.actouid2 and c.wf$acttypouid in in (130,120,200,100) and y.wf$dactouid=c.ouid and y.wf$dpctouid=x.wf$dpctouid and y.ouid<>x.ouid and rownum=1 ");
            stat.setLong(1, realOuid);
            stat.setLong(2, realOuid);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                returnValue = false;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            flag = returnValue;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return flag;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return returnValue;
    }

    public void addProcessDefinitionToModel(String modelOuid, String processDefinitionOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(modelOuid, "modelOuid");
        Utils.checkMandatoryString(processDefinitionOuid, "processDefinitionOuid");
        String tempString = null;
        String tempString2 = null;
        getClass();
        tempString2 = "::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + modelOuid;
        tempString = nds.getValue(tempString2);
        if(Utils.isNullString(tempString))
        {
            getClass();
            nds.addNode("::/WFM_SYSTEM_DIRECTORY/MODEL", modelOuid, "WFM.MODEL", modelOuid);
        }
        tempString = processDefinitionOuid.substring(processDefinitionOuid.lastIndexOf('@') + 1);
        nds.addNode(tempString2, tempString, "WFM.MODEL", tempString);
        modelMap.put(tempString, modelOuid);
    }

    public ArrayList listProcessDefinitionOfModel(String ModelOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(ModelOuid, "ModelOuid");
        getClass();
        ArrayList arrayList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + ModelOuid);
        if(!Utils.isNullArrayList(arrayList))
        {
            int size = arrayList.size();
            for(int i = 0; i < size; i++)
                arrayList.set(i, "wf$dpro@" + (String)arrayList.get(i));

        }
        return arrayList;
    }

    public void removeProcessDefinitionFromModel(String ModelOuid, String processDefinitionOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(modelOuid, "modelOuid");
        Utils.checkMandatoryString(processDefinitionOuid, "processDefinitionOuid");
        String ouid = processDefinitionOuid.substring(processDefinitionOuid.lastIndexOf('@') + 1);
        getClass();
        nds.removeNode("::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + ModelOuid + "/" + ouid);
        modelMap.remove(ouid);
    }

    public String getModelOfProcessDefinition(String processDefinitionOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processDefinitionOuid, "processDefinitionOuid");
        String ouid = processDefinitionOuid.substring(processDefinitionOuid.lastIndexOf('@') + 1);
        return (String)modelMap.get(ouid);
    }

    public void addClassToProcessDefinition(String processDefinitionOuid, String classOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processDefinitionOuid, "processDefinitionOuid");
        Utils.checkMandatoryString(classOuid, "classOuid");
        String modelOuid = null;
        String processOuid = null;
        String tempString = null;
        String tempString2 = null;
        ArrayList tempList = null;
        processOuid = processDefinitionOuid.substring(processDefinitionOuid.lastIndexOf('@') + 1);
        modelOuid = (String)modelMap.get(processOuid);
        getClass();
        tempString2 = "::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + modelOuid;
        tempString = nds.getValue(tempString2);
        if(Utils.isNullString(tempString))
        {
            getClass();
            nds.addNode("::/WFM_SYSTEM_DIRECTORY/MODEL", modelOuid, "WFM.MODEL", modelOuid);
            nds.addNode(tempString2, processOuid, "WFM.MODEL", processOuid);
        }
        nds.addNode(tempString2 + "/" + processOuid, classOuid, "WFM.MODEL", classOuid);
        tempList = (ArrayList)processMap.get(classOuid);
        if(tempList == null)
            tempList = new ArrayList();
        if(!tempList.contains(processOuid))
            tempList.add(processOuid);
        processMap.put(classOuid, tempList);
        tempList = null;
    }

    public void removeClassFromProcessDefinition(String processDefinitionOuid, String classOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processDefinitionOuid, "processDefinitionOuid");
        Utils.checkMandatoryString(classOuid, "classOuid");
        ArrayList tempList = null;
        String ouid = processDefinitionOuid.substring(processDefinitionOuid.lastIndexOf('@') + 1);
        String modelOuid = getModelOfProcessDefinition(processDefinitionOuid);
        getClass();
        nds.removeNode("::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + modelOuid + "/" + ouid + "/" + classOuid);
        tempList = (ArrayList)processMap.get(classOuid);
        if(tempList != null)
        {
            tempList.remove(classOuid);
            processMap.put(classOuid, tempList);
            tempList = null;
        }
    }

    public ArrayList listClassOfProcessDefinition(String processDefinition)
        throws IIPRequestException
    {
        ArrayList arrayList = null;
        String modelOuid = null;
        String processOuid = null;
        Utils.checkMandatoryString(processDefinition, "processDefinition");
        processOuid = processDefinition.substring(processDefinition.lastIndexOf('@') + 1);
        modelOuid = (String)modelMap.get(processOuid);
        if(Utils.isNullString(modelOuid))
        {
            throw new IIPRequestException("Can not find processDefinition: " + processDefinition);
        } else
        {
            getClass();
            arrayList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + modelOuid + "/" + processOuid);
            return arrayList;
        }
    }

    public ArrayList listProcessDeifnitionOfClass(String classOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(classOuid, "classOuid");
        return (ArrayList)processMap.get(classOuid);
    }

    private void loadProcessMap()
    {
        if(modelMap == null)
            modelMap = new HashMap();
        if(processMap == null)
            processMap = new HashMap();
        ArrayList modelList = null;
        ArrayList processList = null;
        ArrayList classList = null;
        ArrayList tempList = null;
        Iterator modelKey = null;
        Iterator processKey = null;
        Iterator classKey = null;
        DOSChangeable model = null;
        String modelOuid = null;
        String processOuid = null;
        String classOuid = null;
        try
        {
            modelList = dos.listModel();
            for(modelKey = modelList.iterator(); modelKey.hasNext();)
            {
                model = (DOSChangeable)modelKey.next();
                modelOuid = (String)model.get("ouid");
                getClass();
                processList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + modelOuid);
                if(processList != null)
                {
                    for(processKey = processList.iterator(); processKey.hasNext();)
                    {
                        processOuid = (String)processKey.next();
                        modelMap.put(processOuid, modelOuid);
                        getClass();
                        classList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/MODEL" + "/" + modelOuid + "/" + processOuid);
                        if(classList != null)
                        {
                            for(classKey = classList.iterator(); classKey.hasNext();)
                            {
                                classOuid = (String)classKey.next();
                                tempList = (ArrayList)processMap.get(classOuid);
                                if(tempList == null)
                                    tempList = new ArrayList();
                                if(!tempList.contains(processOuid))
                                    tempList.add(processOuid);
                                processMap.put(classOuid, tempList);
                                tempList = null;
                            }

                            classKey = null;
                            classList.clear();
                            classList = null;
                        }
                    }

                    processKey = null;
                    processList.clear();
                    processList = null;
                }
            }

            modelKey = null;
            modelList.clear();
            modelList = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public ArrayList listInboxProcessByUser(String userId)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String ouidObject;
        Utils.checkMandatoryString(userId, "userId");
        xc = null;
        con = null;
        stat = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ArrayList rowArrayList = null;
        DOSChangeable dosObject = null;
        DOSChangeable aRow = null;
        HashMap user = null;
        String nameString = null;
        String descString = null;
        String tempString = null;
        ouidObject = null;
        int count = 0;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.ouid, x.dpname, x.souid, x.name, x.strtim, x.wf$pct, x.cretim, x.limtim, x.daname, x.read, x.clotim, x.closed from (select ip.ouid, dp.name as dpname, s.ouid as souid, s.name, to_char(to_date(ia.strtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as strtim, ip.wf$pct, to_char(to_date(ip.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as cretim, to_char(to_date(ia.limtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as limtim, da.name as daname, ia.read, to_char(to_date(ip.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as clotim, decode(ia.clotim,null,null,'T') as closed from wf$ipro ip, wf$dpro dp, wf$iact ia, wf$dact da, wf$sta s where dp.ouid=ip.wf$dproouid and ia.wf$iproouid=ip.ouid and da.wf$dproouid=dp.ouid and ia.wf$dactouid=da.ouid and s.ouid=ip.wf$staouid and (ia.closed is null or ia.closed='A' or ia.closed='R') and ((da.wf$acttypouid in (120,130) and ia.clotim is null) or (da.wf$acttypouid=110 and ia.clotim is null and da.finmod='M') or (da.wf$acttypouid=110 and ia.read is null and da.finmod='A')) and ia.strtim>' ' and ia.wf$dpctouid=? union select ip.ouid, dp.name as dpname, s.ouid as souid, s.name, to_char(to_date(ia.strtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as strtim, ip.wf$pct, to_char(to_date(ip.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as cretim, to_char(to_date(ia.limtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as limtim, da.name as daname, ias.read, to_char(to_date(ip.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as clotim, decode(ias.clotim,null,null,'T') as closed from wf$ipro ip, wf$dpro dp, wf$iact ia, wf$iacts ias, wf$dact da, wf$sta s where dp.ouid=ip.wf$dproouid and ia.wf$iproouid=ip.ouid and da.wf$dproouid=dp.ouid and ia.wf$dactouid=da.ouid and ias.wf$iactouid=ia.ouid and s.ouid=ip.wf$staouid and (ias.closed is null or ias.closed='A' or ias.closed='R') and ((da.wf$acttypouid in (120,130) and ias.clotim is null) or (da.wf$acttypouid=110 and ias.clotim is null and da.finmod='M') or (da.wf$acttypouid=110 and ias.read is null and da.finmod='A')) and ia.strtim>' ' and ias.wf$dpctouid=? ) x order by x.strtim desc ");
            stat.setString(1, userId);
            stat.setString(2, userId);
            PreparedStatement stat2 = con.prepareStatement("select o.wf$dosouid from wf$dos o where o.wf$iproouid=? ");
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable aRow = new DOSChangeable();
                aRow.put("ouid@process", "wf$ipro@" + Utils.getLongToHexString(rs, 1));
                aRow.put("name@process", rs.getString(2));
                aRow.put("ouid@workflow.status", rs.getString(3));
                aRow.put("name@workflow.status", rs.getString(4));
                aRow.put("date.receipt", rs.getString(5));
                aRow.put("id.user", rs.getString(6));
                aRow.put("date.created", rs.getString(7));
                aRow.put("date.limit.to.finish", rs.getString(8));
                aRow.put("name@activity", rs.getString(9));
                aRow.put("read", Utils.getBoolean(rs, 10));
                aRow.put("date.closed", rs.getString(11));
                aRow.put("finished", Utils.getBoolean(rs, 12));
                stat2.setLong(1, rs.getLong(1));
                ResultSet rs2 = stat2.executeQuery();
                String nameString = null;
                String descString = null;
                int count = 0;
                while(rs2.next()) 
                {
                    String tempString = rs2.getString(1);
                    ouidObject = tempString;
                    if(count > 0)
                    {
                        count++;
                        break;
                    }
                    DOSChangeable dosObject = dos.get(tempString);
                    if(dosObject != null)
                    {
                        tempString = (String)dosObject.get("md$number");
                        if(tempString == null)
                            tempString = "";
                        nameString = tempString;
                        tempString = (String)dosObject.get("md$description");
                        if(tempString == null)
                            tempString = "";
                        descString = tempString;
                        dosObject = null;
                        count++;
                    }
                }
                rs2.close();
                rs2 = null;
                if(count > 0)
                {
                    if(count > 1)
                    {
                        nameString = "+" + nameString;
                        descString = "+" + descString;
                    }
                    aRow.put("md$number", nameString);
                    aRow.put("md$description", descString);
                }
                aRow.put("ouid@object", ouidObject);
                HashMap user = aus.getUser((String)aRow.get("id.user"));
                aRow.put("name.user", user.get("name"));
                user.clear();
                user = null;
                rowArrayList.add(aRow);
                aRow = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat2.close();
            stat2 = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList listSentProcessByUser(String userId)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String ouidObject;
        Utils.checkMandatoryString(userId, "userId");
        xc = null;
        con = null;
        stat = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ArrayList rowArrayList = null;
        DOSChangeable dosObject = null;
        DOSChangeable aRow = null;
        HashMap user = null;
        String nameString = null;
        String descString = null;
        String tempString = null;
        ouidObject = null;
        int count = 0;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select ip.ouid, dp.name, s.ouid, s.name, to_char(to_date(ip.strtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as strtim, ip.wf$pct, to_char(to_date(ip.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as cretim, to_char(to_date(ip.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as clotim, decode(ip.clotim,null,null,'T') from wf$ipro ip, wf$dpro dp, wf$sta s where dp.ouid=ip.wf$dproouid and s.ouid=ip.wf$staouid and ip.strtim>' ' and ip.wf$pct=? order by ip.strtim desc ");
            stat.setString(1, userId);
            PreparedStatement stat2 = con.prepareStatement("select o.wf$dosouid from wf$dos o where o.wf$iproouid=? ");
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable aRow = new DOSChangeable();
                aRow.put("ouid@process", "wf$ipro@" + Utils.getLongToHexString(rs, 1));
                aRow.put("name@process", rs.getString(2));
                aRow.put("ouid@workflow.status", rs.getString(3));
                aRow.put("name@workflow.status", rs.getString(4));
                aRow.put("date.sent", rs.getString(5));
                aRow.put("id.user", rs.getString(6));
                aRow.put("date.created", rs.getString(7));
                aRow.put("date.closed", rs.getString(8));
                aRow.put("finished", Utils.getBoolean(rs, 9));
                stat2.setLong(1, rs.getLong(1));
                ResultSet rs2 = stat2.executeQuery();
                String nameString = null;
                String descString = null;
                int count = 0;
                while(rs2.next()) 
                {
                    String tempString = rs2.getString(1);
                    ouidObject = tempString;
                    if(count > 0)
                    {
                        count++;
                        break;
                    }
                    DOSChangeable dosObject = dos.get(tempString);
                    if(dosObject != null)
                    {
                        tempString = (String)dosObject.get("md$number");
                        if(tempString == null)
                            tempString = "";
                        nameString = tempString;
                        tempString = (String)dosObject.get("md$description");
                        if(tempString == null)
                            tempString = "";
                        descString = tempString;
                        dosObject = null;
                        count++;
                    }
                }
                rs2.close();
                rs2 = null;
                if(count > 0)
                {
                    if(count > 1)
                    {
                        nameString = "+" + nameString;
                        descString = "+" + descString;
                    }
                    aRow.put("md$number", nameString);
                    aRow.put("md$description", descString);
                }
                aRow.put("ouid@object", ouidObject);
                HashMap user = aus.getUser((String)aRow.get("id.user"));
                aRow.put("name.user", user.get("name"));
                user.clear();
                user = null;
                rowArrayList.add(aRow);
                aRow = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat2.close();
            stat2 = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList listCompletedProcessByUser(String userId)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String ouidObject;
        Utils.checkMandatoryString(userId, "userId");
        xc = null;
        con = null;
        stat = null;
        PreparedStatement stat2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ArrayList rowArrayList = null;
        DOSChangeable dosObject = null;
        DOSChangeable aRow = null;
        HashMap user = null;
        String nameString = null;
        String descString = null;
        String tempString = null;
        ouidObject = null;
        int count = 0;
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select ip.ouid, dp.name, s.ouid, s.name, to_char(to_date(ia.strtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as strtim, ip.wf$pct, to_char(to_date(ip.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as cretim, to_char(to_date(ia.limtim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as limtim, da.name, ia.read, to_char(to_date(ip.clotim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') as clotim, decode(ia.clotim,null,null,'T') from wf$ipro ip, wf$dpro dp, wf$iact ia, wf$dact da, wf$sta s where dp.ouid=ip.wf$dproouid and ia.wf$iproouid=ip.ouid and da.wf$dproouid=dp.ouid and ia.wf$dactouid=da.ouid and s.ouid=ip.wf$staouid and da.wf$acttypouid in ('110','120','130') and (ia.closed is null or ia.closed='A' or ia.closed='R') and ia.strtim>' ' and ia.clotim>' ' and ia.wf$dpctouid=? order by ia.strtim desc ");
            stat.setString(1, userId);
            PreparedStatement stat2 = con.prepareStatement("select o.wf$dosouid from wf$dos o where o.wf$iproouid=? ");
            ResultSet rs = stat.executeQuery();
            ArrayList rowArrayList = new ArrayList();
            while(rs.next()) 
            {
                DOSChangeable aRow = new DOSChangeable();
                aRow.put("ouid@process", "wf$ipro@" + Utils.getLongToHexString(rs, 1));
                aRow.put("name@process", rs.getString(2));
                aRow.put("ouid@workflow.status", rs.getString(3));
                aRow.put("name@workflow.status", rs.getString(4));
                aRow.put("date.receipt", rs.getString(5));
                aRow.put("id.user", rs.getString(6));
                aRow.put("date.created", rs.getString(7));
                aRow.put("date.limit.to.finish", rs.getString(8));
                aRow.put("name@activity", rs.getString(9));
                aRow.put("read", Utils.getBoolean(rs, 10));
                aRow.put("date.closed", rs.getString(11));
                aRow.put("finished", Utils.getBoolean(rs, 12));
                stat2.setLong(1, rs.getLong(1));
                ResultSet rs2 = stat2.executeQuery();
                String nameString = null;
                String descString = null;
                int count = 0;
                while(rs2.next()) 
                {
                    String tempString = rs2.getString(1);
                    ouidObject = tempString;
                    if(count > 0)
                    {
                        count++;
                        break;
                    }
                    DOSChangeable dosObject = dos.get(tempString);
                    if(dosObject != null)
                    {
                        tempString = (String)dosObject.get("md$number");
                        if(tempString == null)
                            tempString = "";
                        nameString = tempString;
                        tempString = (String)dosObject.get("md$description");
                        if(tempString == null)
                            tempString = "";
                        descString = tempString;
                        dosObject = null;
                        count++;
                    }
                }
                rs2.close();
                rs2 = null;
                if(count > 0)
                {
                    if(count > 1)
                    {
                        nameString = "+" + nameString;
                        descString = "+" + descString;
                    }
                    aRow.put("md$number", nameString);
                    aRow.put("md$description", descString);
                }
                aRow.put("ouid@object", ouidObject);
                HashMap user = aus.getUser((String)aRow.get("id.user"));
                aRow.put("name.user", user.get("name"));
                user.clear();
                user = null;
                rowArrayList.add(aRow);
                aRow = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat2.close();
            stat2 = null;
            rowArrayList.trimToSize();
            con.commit();
            arraylist = rowArrayList;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public void createComments(DOSChangeable comments)
        throws IIPRequestException
    {
label0:
        {
            Utils.checkMandatoryString(comments, "ouid@process");
            PooledConnection xc = null;
            Connection con = null;
            PreparedStatement stat = null;
            ResultSet rs = null;
            String userId = null;
            try
            {
                try
                {
                    xc = dtm.getPooledConnection("system");
                    con = xc.getConnection();
                    stat = con.prepareStatement("insert into wf$cmt (wf$iproouid, cretim, wf$pct, decide, cmt) values (?, to_char(sysdate,'YYYYMMDDHH24MISS'), ?, ?, ?) ");
                    int i = 1;
                    stat.setLong(i, Utils.getRealLongOuid((String)comments.get("ouid@process")));
                    i++;
                    stat.setString(i, (String)comments.get("id.user"));
                    i++;
                    stat.setString(i, (String)comments.get("decide"));
                    i++;
                    stat.setString(i, (String)comments.get("comments"));
                    i++;
                    int rows = stat.executeUpdate();
                    stat.close();
                    stat = null;
                    if(rows < 0)
                    {
                        try
                        {
                            con.rollback();
                        }
                        catch(SQLException sqlexception) { }
                        throw new IIPRequestException("Can not modify a object.");
                    }
                    con.commit();
                    return;
                }
                catch(SQLException e)
                {
                    DTMUtil.sqlExceptionHelper(con, e);
                }
                break label0;
            }
            finally
            {
                DTMUtil.closeSafely(stat, con, xc);
                stat = null;
                con = null;
                xc = null;
            }
        }
        return;
    }

    public String getProcessComments(String processOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Utils.checkMandatoryString(processOuid, "processOuid");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        StringBuffer sb = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select /*+index_desc(x pk_wf$cmt)*/to_char(to_date(x.cretim,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'), x.wf$pct, x.decide, x.cmt from wf$cmt x where x.wf$iproouid=? ");
            stat.setLong(1, Utils.getRealLongOuid(processOuid));
            ResultSet rs = stat.executeQuery();
            String string = null;
            HashMap user = null;
            StringBuffer sb = new StringBuffer();
            sb.append("<html><body bgcolor=#dee5ef><basefont size=1><table cellspacing=0 cellpadding=0 border=0 width=100%>");
            for(; rs.next(); sb.append("</pre></td></tr>"))
            {
                string = rs.getString(3);
                if(!Utils.isNullString(string))
                    if(string.equals("A"))
                    {
                        sb.append("<tr><td bgcolor=#cad1ec>");
                        sb.append(rs.getString(1));
                        sb.append("  ");
                        string = rs.getString(2);
                        if(!Utils.isNullString(string))
                        {
                            user = aus.getUser(string);
                            if(user != null)
                            {
                                sb.append(user.get("name"));
                                sb.append('(');
                                sb.append(string);
                                sb.append(')');
                                user.clear();
                                user = null;
                                sb.append("  ");
                            }
                        }
                        sb.append("Accepted");
                    } else
                    if(string.equals("J") || string.equals("R"))
                    {
                        sb.append("<tr><td bgcolor=#efd0cb>");
                        sb.append(rs.getString(1));
                        sb.append("  ");
                        string = rs.getString(2);
                        if(!Utils.isNullString(string))
                        {
                            user = aus.getUser(string);
                            if(user != null)
                            {
                                sb.append(user.get("name"));
                                sb.append('(');
                                sb.append(string);
                                sb.append(')');
                                user.clear();
                                user = null;
                                sb.append("  ");
                            }
                        }
                        sb.append("Rejected");
                    } else
                    {
                        sb.append("<tr><td bgcolor=silver>");
                        sb.append(rs.getString(1));
                        sb.append("  ");
                        string = rs.getString(2);
                        if(!Utils.isNullString(string))
                        {
                            user = aus.getUser(string);
                            if(user != null)
                            {
                                sb.append(user.get("name"));
                                sb.append('(');
                                sb.append(string);
                                sb.append(')');
                                user.clear();
                                user = null;
                                sb.append("  ");
                            }
                        }
                    }
                sb.append("</td></tr><tr><td bgcolor=#dee5ef><pre font size=1>");
                sb.append(rs.getString(4));
            }

            sb.append("</table></body></html>");
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            string = sb.toString();
            sb = null;
            s = string;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public String isLocked(String dosOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String result;
        Utils.checkMandatoryString(dosOuid, "dosOuid");
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        result = null;
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select x.wf$iproouid from wf$lock x where x.ouid=? ");
            stat.setString(1, dosOuid);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                result = "wf$ipro@" + Utils.getLongToHexString(rs, 1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            s = result;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return s;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public boolean addActivityStatusMap(HashMap map)
        throws IIPRequestException
    {
        String activityOuid = (String)map.get("ouid@activity");
        String classOuid = (String)map.get("ouid@class");
        String fromId = (String)map.get("from.id");
        String toId = (String)map.get("to.id");
        Utils.checkMandatoryString(activityOuid, "ouid@activity");
        Utils.checkMandatoryString(classOuid, "ouid@class");
        Utils.checkMandatoryString(fromId, "from.id");
        Utils.checkMandatoryString(toId, "to.id");
        String tempString = null;
        String tempString2 = null;
        boolean result = false;
        getClass();
        tempString2 = "::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid;
        tempString = nds.getValue(tempString2);
        if(Utils.isNullString(tempString))
        {
            getClass();
            nds.addNode("::/WFM_SYSTEM_DIRECTORY/STATUSMAP", activityOuid, "WFM.STATUSMAP", activityOuid);
        }
        tempString = nds.getValue(tempString2 + "/" + classOuid);
        if(Utils.isNullString(tempString))
            nds.addNode(tempString2, classOuid, "WFM.STATUSMAP", classOuid);
        nds.addNode(tempString2 + "/" + classOuid, fromId, "WFM.STATUSMAP", fromId);
        result = nds.addNode(tempString2 + "/" + classOuid + "/" + fromId, toId, "WFM.STATUSMAP", toId);
        return result;
    }

    public boolean removeActivityStatusMap(HashMap map)
        throws IIPRequestException
    {
        String activityOuid = (String)map.get("ouid@activity");
        String classOuid = (String)map.get("ouid@class");
        String fromId = (String)map.get("from.id");
        String toId = (String)map.get("to.id");
        Utils.checkMandatoryString(activityOuid, "ouid@activity");
        Utils.checkMandatoryString(classOuid, "ouid@class");
        Utils.checkMandatoryString(classOuid, "ouid@class");
        Utils.checkMandatoryString(classOuid, "ouid@class");
        getClass();
        boolean result = nds.removeNode("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid + "/" + fromId + "/" + toId);
        getClass();
        ArrayList tempList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid + "/" + fromId);
        if(Utils.isNullArrayList(tempList))
        {
            getClass();
            nds.removeNode("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid + "/" + fromId);
        } else
        {
            tempList.clear();
        }
        tempList = null;
        getClass();
        tempList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid);
        if(Utils.isNullArrayList(tempList))
        {
            getClass();
            nds.removeNode("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid);
        } else
        {
            tempList.clear();
        }
        tempList = null;
        getClass();
        tempList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid);
        if(Utils.isNullArrayList(tempList))
        {
            getClass();
            nds.removeNode("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid);
        } else
        {
            tempList.clear();
        }
        tempList = null;
        return result;
    }

    public ArrayList listActivityStatus(String activityOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(activityOuid, "activityOuid");
        ArrayList resultList = null;
        ArrayList classList = null;
        ArrayList fromList = null;
        ArrayList toList = null;
        ArrayList tempList = null;
        String classOuid = null;
        String fromId = null;
        getClass();
        classList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid);
        if(Utils.isNullArrayList(classList))
            return null;
        resultList = new ArrayList();
        for(int i = 0; i < classList.size(); i++)
        {
            classOuid = (String)classList.get(i);
            getClass();
            fromList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid);
            if(!Utils.isNullArrayList(fromList))
            {
                for(int j = 0; j < fromList.size(); j++)
                {
                    fromId = (String)fromList.get(j);
                    getClass();
                    toList = nds.getChildNodeList("::/WFM_SYSTEM_DIRECTORY/STATUSMAP" + "/" + activityOuid + "/" + classOuid + "/" + fromId);
                    if(!Utils.isNullArrayList(toList))
                    {
                        for(int k = 0; k < toList.size(); k++)
                        {
                            tempList = new ArrayList();
                            tempList.add(classOuid);
                            tempList.add(fromId);
                            tempList.add(toList.get(k));
                            resultList.add(tempList.clone());
                            tempList.clear();
                            tempList = null;
                        }

                    }
                }

            }
        }

        return resultList;
    }

    public ArrayList getListOfAllParticipantsOfProcess(String processOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOuid, "processOuid");
        DOSChangeable tempMap = null;
        String activityType = null;
        ArrayList outputList = null;
        ArrayList tempList = getProcessActivities4Graph(processOuid);
        if(Utils.isNullArrayList(tempList))
            return null;
        outputList = new ArrayList();
        for(int i = 0; i < tempList.size(); i++)
        {
            tempMap = (DOSChangeable)tempList.get(i);
            activityType = (String)tempMap.get("type");
            if(Utils.isNullString(activityType))
            {
                tempMap = null;
            } else
            {
                if(activityType.equals("120") || activityType.equals("130") || activityType.equals("110") || activityType.equals("230"))
                    outputList.add(tempMap);
                tempMap = null;
            }
        }

        tempList.clear();
        tempList = null;
        if(Utils.isNullArrayList(outputList))
        {
            outputList = null;
            return null;
        } else
        {
            return outputList;
        }
    }

    public ArrayList getListOfAllReviewerOfProcess(String processOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOuid, "processOuid");
        DOSChangeable tempMap = null;
        String activityType = null;
        ArrayList outputList = null;
        ArrayList tempList = getProcessActivities4Graph(processOuid);
        if(Utils.isNullArrayList(tempList))
            return null;
        outputList = new ArrayList();
        for(int i = 0; i < tempList.size(); i++)
        {
            tempMap = (DOSChangeable)tempList.get(i);
            activityType = (String)tempMap.get("type");
            if(Utils.isNullString(activityType))
            {
                tempMap = null;
            } else
            {
                if(activityType.equals("120"))
                    outputList.add(tempMap);
                tempMap = null;
            }
        }

        tempList.clear();
        tempList = null;
        if(Utils.isNullArrayList(outputList))
        {
            outputList = null;
            return null;
        } else
        {
            return outputList;
        }
    }

    public ArrayList getListOfAllApproverOfProcess(String processOuid)
        throws IIPRequestException
    {
        Utils.checkMandatoryString(processOuid, "processOuid");
        DOSChangeable tempMap = null;
        String activityType = null;
        ArrayList outputList = null;
        ArrayList tempList = getProcessActivities4Graph(processOuid);
        if(Utils.isNullArrayList(tempList))
            return null;
        outputList = new ArrayList();
        for(int i = 0; i < tempList.size(); i++)
        {
            tempMap = (DOSChangeable)tempList.get(i);
            activityType = (String)tempMap.get("type");
            if(Utils.isNullString(activityType))
            {
                tempMap = null;
            } else
            {
                if(activityType.equals("130"))
                    outputList.add(tempMap);
                tempMap = null;
            }
        }

        tempList.clear();
        tempList = null;
        if(Utils.isNullArrayList(outputList))
        {
            outputList = null;
            return null;
        } else
        {
            return outputList;
        }
    }

    private String unitPriority;
    private String unitCost;
    private String modelOuid;
    private DTM dtm;
    private NDS nds;
    private AUS aus;
    private DOS dos;
    private DSS dss;
    private String NDS_CODE;
    private final String STATUS_INACTIVE = "100";
    private final String STATUS_ACTIVE = "110";
    private final String STATUS_SUSPENDED = "120";
    private final String STATUS_COMPLETED = "130";
    private final String STATUS_TERMINATED = "140";
    private final String STATUS_DELAYED = "150";
    private final String STATUS_ABORTED = "160";
    private final String STATUS_INITIATED = "170";
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar calendar;
    private final String NDS_BASE = "::/WFM_SYSTEM_DIRECTORY";
    private final String NDS_MODEL = "::/WFM_SYSTEM_DIRECTORY/MODEL";
    private final String NDS_STATUSMAP = "::/WFM_SYSTEM_DIRECTORY/STATUSMAP";
    private HashMap modelMap;
    private HashMap processMap;
}
