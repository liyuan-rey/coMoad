// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dos.DOSAction;
import dyna.framework.service.dos.DOSAssociation;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dos.DOSClass;
import dyna.framework.service.dos.DOSCode;
import dyna.framework.service.dos.DOSCodeItem;
import dyna.framework.service.dos.DOSField;
import dyna.framework.service.dos.DOSFieldGroup;
import dyna.framework.service.dos.DOSModel;
import dyna.framework.service.dos.DOSPackage;
import dyna.util.DTMUtil;
import dyna.util.Utils;
import java.io.File;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service:
//            DOS, DTM, NDS, AUS, 
//            DSS, MSR, OLM

public class DOSImpl extends ServiceServer
    implements DOS
{

    public DOSImpl()
    {
        dtm = null;
        nds = null;
        aus = null;
        dss = null;
        olm = null;
        msr = null;
        modelMap = null;
        packageMap = null;
        classMap = null;
        fieldMap = null;
        actionMap = null;
        associationMap = null;
        fieldGroupMap = null;
        classOuidMap = null;
        codeMap = null;
        codeItemMap = null;
        foundationPackageOuid = null;
        fobjectOuid = null;
        fversionobjectOuid = null;
        ffilecontrolOuid = null;
        fcadfileOuid = null;
        foundationPackage = null;
        fobject = null;
        fversionobject = null;
        ffilecontrol = null;
        fcadfile = null;
        NDS_CODE = null;
        INSTANCE_CACHE_SIZE = 1000;
        STATUS_CRT = "CRT";
        STATUS_OBS = "OBS";
        STATUS_CKI = "CKI";
        STATUS_CKO = "CKO";
        STATUS_RLS = "RLS";
        STATUS_WIP = "WIP";
        STATUS_RV1 = "RV1";
        STATUS_RV2 = "RV2";
        STATUS_AP1 = "AP1";
        STATUS_AP2 = "AP2";
        STATUS_REJ = "REJ";
        reservedFields = null;
        versionStrings = null;
        modelMap = new HashMap();
        packageMap = new HashMap();
        classMap = new HashMap();
        fieldMap = new HashMap();
        actionMap = new HashMap();
        associationMap = new HashMap();
        fieldGroupMap = new HashMap();
        classOuidMap = new HashMap();
        codeMap = new HashMap();
        codeItemMap = new HashMap();
        instanceCache = new HashMap();
        instanceCacheTag = new LinkedList();
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dtm = (DTM)getServiceInstance("DF30DTM1");
            nds = (NDS)getServiceInstance("DF30NDS1");
            aus = (AUS)getServiceInstance("DF30AUS1");
            dss = (DSS)getServiceInstance("DF30DSS1");
            msr = (MSR)getServiceInstance("DF30MSR1");
            System.out.print("(Cache: " + INSTANCE_CACHE_SIZE + ")");
            reservedFields = new LinkedList();
            reservedFields.add("md$number");
            reservedFields.add("md$desc");
            reservedFields.add("md$description");
            reservedFields.add("md$status");
            reservedFields.add("md$cdate");
            reservedFields.add("md$mdate");
            reservedFields.add("md$user");
            reservedFields.add("vf$version");
            reservedFields.add("md$sequence");
            listModel();
            System.out.print('.');
            listAllPackage();
            System.out.print('.');
            listClass();
            System.out.print('.');
            listAllCode();
            System.out.print('.');
            listAllCodeItem();
            System.out.print('.');
            listAllCodeItem();
            System.out.print('.');
            listAllCode();
            System.out.print('.');
            listField();
            System.out.print('.');
            listAction();
            System.out.print('.');
            listFieldGroup();
            System.out.print('.');
            listAssociation();
            System.out.print('.');
            listClass();
            System.out.print('.');
            initialAssociationMapping();
            System.out.print('.');
            updateAllSubClassesForFoundationClass();
            System.out.print('.');
            NDS_CODE = nds.getSubSet("CODE");
            nds.addNode("::", "DOS_SYSTEM_DIRECTORY", "DOS", "DOS");
            nds.addNode("::/DOS_SYSTEM_DIRECTORY", "SCRIPT", "DOS.SCRIPT", "DOS.SCRIPT");
            nds.addNode("::/DOS_SYSTEM_DIRECTORY", "NUMBERINGRULE", "DOS.RULE", "DOS.RULE");
            nds.addNode("::/DOS_SYSTEM_DIRECTORY", "INSTANCENUMBER", "DOS.RULE", "DOS.RULE");
            nds.addNode("::/DOS_SYSTEM_DIRECTORY", "UNUSEDINHERITEDFIELD", "DOS.INHERIT", "DOS.INHERIT");
            setVersionString();
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

    public long generateOUID(Connection con)
        throws SQLException
    {
        long returnValue = 0L;
        PreparedStatement stat = null;
        ResultSet rs = null;
        int rows = 0;
        if(con == null)
            return 0L;
        stat = con.prepareStatement("update ouid set ouid=(select decode(ouid,0,2147483649,ouid+1) from ouid) ");
        rows = stat.executeUpdate();
        stat.close();
        stat = null;
        stat = con.prepareStatement("select ouid from ouid ");
        rs = stat.executeQuery();
        if(rs.next())
            returnValue = rs.getLong(1);
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        return returnValue;
    }

    public long generateOUID()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        long returnLong;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnLong = 0L;
        int rows = 0;
        SQLException e;
        long l;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            returnLong = generateOUID(con);
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            l = returnLong;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return l;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return returnLong;
    }

    public String createModel(DOSChangeable modelDefinition)
        throws IIPRequestException
    {
        String result = DOSModel.createModel(modelDefinition, dtm, modelMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public void removeModel(String ouid)
        throws IIPRequestException
    {
        DOSModel.removeModel(ouid, dtm, nds, modelMap);
        updateTimeToken();
    }

    public DOSChangeable getModel(String ouid)
        throws IIPRequestException
    {
        return DOSModel.getModel(ouid, modelMap);
    }

    public void setModel(DOSChangeable modelDefinition)
        throws IIPRequestException
    {
        DOSModel.setModel(modelDefinition, dtm, modelMap);
        updateTimeToken();
    }

    public ArrayList listModel()
        throws IIPRequestException
    {
        return DOSModel.listModel(dtm, modelMap);
    }

    public ArrayList listRootClassInModel(String modelOuid)
        throws IIPRequestException
    {
        DOSPackage dosPackage = null;
        DOSPackage tempDosPackage = null;
        DOSModel dosModel = null;
        DOSClass dosClass = null;
        ArrayList returnList = null;
        TreeMap packageMap = null;
        TreeMap classMap = null;
        Iterator packageKey = null;
        Iterator classKey = null;
        DOSChangeable classData = null;
        long ouidLong = 0L;
        String ouid = null;
        dosModel = (DOSModel)modelMap.get(modelOuid);
        if(dosModel == null)
            throw new IIPRequestException("No such model.");
        packageMap = dosModel.packageMap;
        if(packageMap == null || packageMap.size() == 0)
        {
            returnList = listPackage(modelOuid);
            if(returnList != null)
            {
                returnList.clear();
                returnList = null;
            }
            packageMap = dosModel.packageMap;
        }
        if(packageMap != null && packageMap.size() > 0)
        {
            returnList = new ArrayList();
            packageKey = packageMap.keySet().iterator();
            while(packageKey.hasNext()) 
            {
                dosPackage = (DOSPackage)packageMap.get(packageKey.next());
                classMap = dosPackage.classMap;
                if(classMap == null || classMap.size() == 0)
                {
                    listClassInPackage(Long.toHexString(dosPackage.OUID));
                    if(dosPackage.classMap == null)
                    {
                        dosPackage = null;
                        continue;
                    }
                    classMap = dosPackage.classMap;
                }
                for(classKey = classMap.keySet().iterator(); classKey.hasNext();)
                {
                    dosClass = (DOSClass)classMap.get(classKey.next());
                    if(!Utils.getBoolean(dosClass.isRoot))
                    {
                        dosClass = null;
                    } else
                    {
                        classData = new DOSChangeable();
                        classData.put("ouid", Long.toHexString(dosClass.OUID));
                        classData.put("name", dosClass.name);
                        classData.put("description", dosClass.description);
                        tempDosPackage = dosClass._package;
                        if(tempDosPackage != null)
                        {
                            classData.put("ouid@package", Long.toHexString(tempDosPackage.OUID));
                            tempDosPackage = null;
                        }
                        classData.put("is.root", dosClass.isRoot);
                        classData.put("is.leaf", dosClass.isLeaf);
                        classData.put("is.versionable", dosClass.versionable);
                        classData.put("is.filecontrol", dosClass.fileControl);
                        classData.put("capacity", dosClass.capacity);
                        classData.put("code", dosClass.code);
                        classData.put("title", dosClass.title);
                        classData.put("tooltip", dosClass.tooltip);
                        classData.put("icon", dosClass.icon);
                        classData.put("datasource.id", dosClass.datasourceId);
                        classData.put("is.xa.object", dosClass.isXAClass);
                        classData.put("is.association.class", dosClass.isAssociationClass);
                        classData.put("use.unique.number", dosClass.useUniqueNumber);
                        returnList.add(classData);
                    }
                }

                dosPackage = null;
            }
            packageKey = null;
            packageMap = null;
        }
        return returnList;
    }

    public void setWorkingModel(String ouid)
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(context == null)
        {
            return;
        } else
        {
            context.put("working.model", ouid);
            return;
        }
    }

    public String getWorkingModel()
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(context != null)
            return (String)context.get("working.model");
        else
            return null;
    }

    public void releaseWorkingModel()
        throws IIPRequestException
    {
        HashMap context = getClientContext();
        if(context == null)
        {
            return;
        } else
        {
            context.remove("working.model");
            return;
        }
    }

    public String createPackage(String modelOuid, DOSChangeable packageDefinition)
        throws IIPRequestException
    {
        String result = DOSPackage.createPackage(modelOuid, packageDefinition, dtm, packageMap, modelMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public String createPackage(DOSChangeable packageDefinition)
        throws IIPRequestException
    {
        String modelOuid = null;
        HashMap context = getClientContext();
        if(context == null)
            return null;
        modelOuid = (String)context.get("working.model");
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("First, Set working model.");
        else
            return createPackage(modelOuid, packageDefinition);
    }

    public void removePackage(String ouid)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        int rows = 0;
        DOSPackage dosPackage = null;
        DOSClass dosClass = null;
        TreeMap treeMap = null;
        LinkedList doList = null;
        Iterator treeKey = null;
        Iterator doKey = null;
        String tempOuid = null;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        dosPackage = (DOSPackage)packageMap.get(ouid);
        if(dosPackage == null)
            throw new IIPRequestException("Package not found : " + ouid);
        doList = new LinkedList();
        treeMap = dosPackage.classMap;
        if(treeMap != null)
        {
            for(treeKey = treeMap.values().iterator(); treeKey.hasNext();)
            {
                dosClass = (DOSClass)treeKey.next();
                if(dosClass != null)
                {
                    tempOuid = Long.toHexString(dosClass.OUID);
                    dosClass = null;
                    doList.add(tempOuid);
                }
            }

            treeKey = null;
            treeMap = null;
            for(doKey = doList.iterator(); doKey.hasNext(); removeClass(tempOuid))
                tempOuid = (String)doKey.next();

            doKey = null;
        }
        doList = null;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from dosmodpkg where dospkg=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dospkg where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            packageMap.remove(ouid);
            DOSModel dosModel = null;
            treeMap = dosPackage.modelMap;
            if(treeMap != null && treeMap.size() > 0)
                for(treeKey = treeMap.keySet().iterator(); treeKey.hasNext(); treeKey.remove())
                {
                    dosModel = (DOSModel)treeMap.get(treeKey.next());
                    if(dosModel.packageMap != null && dosModel.packageMap.size() > 0)
                        dosModel.packageMap.remove(dosPackage.name);
                    dosModel = null;
                }

            treeMap = null;
            dosPackage = null;
            dosModel = null;
            updateTimeToken();
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

    public DOSChangeable getPackage(String ouid)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSPackage dosPackage = (DOSPackage)packageMap.get(ouid);
        if(dosPackage != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosPackage.name);
            returnObject.put("isGlobal", new Boolean(dosPackage.isGlobal));
            returnObject.put("description", dosPackage.description);
            dosPackage = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public DOSChangeable getPackageWithName(String packageName)
        throws IIPRequestException
    {
        if(Utils.isNullString(packageName))
            throw new IIPRequestException("Parameter value is null : packageName");
        ArrayList listPackage = listAllPackage();
        if(listPackage != null)
        {
            for(int i = 0; i < listPackage.size(); i++)
            {
                DOSChangeable dosPackage = (DOSChangeable)listPackage.get(i);
                if(dosPackage.get("name").equals(packageName))
                    return dosPackage;
            }

        }
        return null;
    }

    public void setPackage(DOSChangeable packageDefinition)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        String ouid = null;
        Boolean isGlobal = null;
        if(packageDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + packageDefinition);
        HashMap valueMap = packageDefinition.getValueMap();
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dospkg set name=?, global=?, des=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            isGlobal = (Boolean)valueMap.get("isGlobal");
            if(isGlobal == null)
                isGlobal = Boolean.FALSE;
            Utils.setBoolean(stat, 2, isGlobal);
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSPackage dosPackage = (DOSPackage)packageMap.get(ouid);
            if(dosPackage == null)
            {
                dosPackage = new DOSPackage();
                dosPackage.OUID = ouidLong;
                packageMap.put(ouid, dosPackage);
            }
            String oldName = dosPackage.name;
            dosPackage.name = (String)valueMap.get("name");
            dosPackage.isGlobal = Utils.getBoolean(isGlobal);
            dosPackage.description = (String)valueMap.get("description");
            DOSModel dosModel = null;
            DOSClass dosClass = null;
            TreeMap treeMap = null;
            Iterator treeKey = null;
            treeMap = dosPackage.modelMap;
            if(treeMap != null && treeMap.size() > 0)
                for(treeKey = treeMap.keySet().iterator(); treeKey.hasNext();)
                {
                    dosModel = (DOSModel)treeMap.get(treeKey.next());
                    if(dosModel.packageMap != null && dosModel.packageMap.size() > 0)
                    {
                        dosModel.packageMap.remove(oldName);
                        dosModel.packageMap.put(dosPackage.name, dosPackage);
                    }
                    dosModel = null;
                }

            treeMap = null;
            if(Boolean.TRUE.equals(isGlobal))
            {
                for(Iterator modelKey = modelMap.keySet().iterator(); modelKey.hasNext();)
                {
                    dosModel = (DOSModel)modelMap.get(modelKey.next());
                    treeMap = dosModel.packageMap;
                    if(packageMap == null)
                        treeMap.put(dosPackage.name, dosPackage);
                    dosModel = null;
                    treeMap = null;
                }

                Object obj = null;
            }
            dosPackage = null;
            updateTimeToken();
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

    public ArrayList listClassInPackage(String ouid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSPackage dosPackage;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        DOSPackage tempDosPackage = null;
        dosPackage = null;
        TreeMap classMap = null;
        ArrayList returnList = null;
        DOSChangeable classData = null;
        long ouidLong = 0L;
        int rows = 0;
        dosPackage = (DOSPackage)packageMap.get(ouid);
        if(dosPackage == null)
            throw new IIPRequestException("Package not exists.");
        if(packageMap.size() > 0 && this.classMap.size() > 0)
        {
            classMap = dosPackage.classMap;
            if(classMap != null && classMap.size() > 0)
            {
                returnList = new ArrayList();
                Iterator mapKey;
                for(mapKey = classMap.keySet().iterator(); mapKey.hasNext();)
                {
                    classData = new DOSChangeable();
                    dosClass = (DOSClass)classMap.get(mapKey.next());
                    classData.put("ouid", Long.toHexString(dosClass.OUID));
                    classData.put("name", dosClass.name);
                    classData.put("description", dosClass.description);
                    tempDosPackage = dosClass._package;
                    if(tempDosPackage != null)
                        classData.put("ouid@package", Long.toHexString(tempDosPackage.OUID));
                    classData.put("is.root", dosClass.isRoot);
                    classData.put("is.leaf", dosClass.isLeaf);
                    classData.put("is.versionable", dosClass.versionable);
                    classData.put("is.filecontrol", dosClass.fileControl);
                    classData.put("capacity", dosClass.capacity);
                    classData.put("code", dosClass.code);
                    classData.put("title", dosClass.title);
                    classData.put("tooltip", dosClass.tooltip);
                    classData.put("icon", dosClass.icon);
                    classData.put("datasource.id", dosClass.datasourceId);
                    classData.put("is.xa.object", dosClass.isXAClass);
                    classData.put("is.association.class", dosClass.isAssociationClass);
                    classData.put("is.abstract", dosClass.isAbstract);
                    classData.put("use.unique.number", dosClass.useUniqueNumber);
                    returnList.add(classData);
                    classData = null;
                    dosClass = null;
                    tempDosPackage = null;
                }

                mapKey = null;
                classData = null;
                classMap = null;
                return returnList;
            }
            classMap = null;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dc.ouid from dosclas dc where dc.dospkg=? order by dc.name ");
            stat.setLong(1, Utils.convertOuidToLong(ouid));
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            TreeMap classMap = dosPackage.classMap;
            if(classMap == null)
            {
                classMap = new TreeMap();
                dosPackage.classMap = classMap;
            }
            DOSPackage tempDosPackage;
            DOSChangeable classData;
            while(rs.next()) 
            {
                DOSClass dosClass = (DOSClass)this.classMap.get(Long.toHexString(rs.getLong(1)));
                if(dosClass != null)
                {
                    classData = new DOSChangeable();
                    classData.put("ouid", Long.toHexString(dosClass.OUID));
                    classData.put("name", dosClass.name);
                    classData.put("description", dosClass.description);
                    tempDosPackage = dosClass._package;
                    if(tempDosPackage != null)
                        classData.put("ouid@package", Long.toHexString(tempDosPackage.OUID));
                    classData.put("is.root", dosClass.isRoot);
                    classData.put("is.leaf", dosClass.isLeaf);
                    classData.put("is.versionable", dosClass.versionable);
                    classData.put("is.filecontrol", dosClass.fileControl);
                    classData.put("capacity", dosClass.capacity);
                    classData.put("code", dosClass.code);
                    classData.put("title", dosClass.title);
                    classData.put("tooltip", dosClass.tooltip);
                    classData.put("icon", dosClass.icon);
                    classData.put("datasource.id", dosClass.datasourceId);
                    classData.put("is.xa.object", dosClass.isXAClass);
                    classData.put("is.association.class", dosClass.isAssociationClass);
                    classData.put("is.abstract", dosClass.isAbstract);
                    classData.put("use.unique.number", dosClass.useUniqueNumber);
                    returnList.add(classData);
                    classMap.put(dosClass.name, dosClass);
                    classData = null;
                    dosClass = null;
                }
            }
            rs.close();
            rs = null;
            con.commit();
            con.close();
            xc.close();
            tempDosPackage = null;
            dosPackage = null;
            classMap = null;
            classData = null;
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
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public ArrayList listPackage(String modelOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSModel dosModel;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSPackage dosPackage = null;
        dosModel = null;
        ArrayList returnList = null;
        TreeMap tempMap = null;
        DOSChangeable packageData = null;
        long ouidLong = 0L;
        String ouid = null;
        int rows = 0;
        dosModel = (DOSModel)modelMap.get(modelOuid);
        if(dosModel == null)
            throw new IIPRequestException("No such model.");
        if(this.packageMap.size() > 0 && modelMap.size() > 0)
        {
            TreeMap packageMap = dosModel.packageMap;
            if(packageMap != null && packageMap.size() > 0)
            {
                returnList = new ArrayList();
                Iterator mapKey;
                for(mapKey = packageMap.keySet().iterator(); mapKey.hasNext();)
                {
                    packageData = new DOSChangeable();
                    dosPackage = (DOSPackage)packageMap.get(mapKey.next());
                    packageData.put("ouid", Long.toHexString(dosPackage.OUID));
                    packageData.put("isGlobal", new Boolean(dosPackage.isGlobal));
                    packageData.put("name", dosPackage.name);
                    packageData.put("description", dosPackage.description);
                    returnList.add(packageData);
                    packageData = null;
                    dosPackage = null;
                }

                mapKey = null;
                packageData = null;
                packageMap = null;
                return returnList;
            }
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select a.ouid from (select dp.ouid from dospkg dp, dosmodpkg dmp where dp.ouid=dmp.dospkg and dmp.dosmod=? union select dp.ouid from dospkg dp where dp.global='T' ) b, dospkg a where a.ouid=b.ouid order by a.name");
            stat.setLong(1, Utils.convertOuidToLong(modelOuid));
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            TreeMap tempMap = new TreeMap();
            TreeMap treeMap = null;
            DOSModel tempModel = null;
            DOSChangeable packageData;
            while(rs.next()) 
            {
                packageData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                DOSPackage dosPackage = (DOSPackage)this.packageMap.get(ouid);
                packageData.put("ouid", ouid);
                packageData.put("isGlobal", new Boolean(dosPackage.isGlobal));
                packageData.put("name", dosPackage.name);
                packageData.put("description", dosPackage.description);
                returnList.add(packageData);
                tempMap.put(dosPackage.name, dosPackage);
                if(dosPackage.isGlobal)
                {
                    for(Iterator modelKey = modelMap.keySet().iterator(); modelKey.hasNext();)
                    {
                        tempModel = (DOSModel)modelMap.get(modelKey.next());
                        treeMap = tempModel.packageMap;
                        if(this.packageMap == null)
                            treeMap.put(dosPackage.name, dosPackage);
                        tempModel = null;
                        treeMap = null;
                    }

                    Object obj = null;
                }
                dosPackage = null;
                packageData = null;
            }
            dosModel.packageMap = tempMap;
            packageData = null;
            tempMap = null;
            dosModel = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public ArrayList listPackage()
        throws IIPRequestException
    {
        String modelOuid = null;
        HashMap context = getClientContext();
        if(context == null)
            return null;
        modelOuid = (String)context.get("working.model");
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("First, Set working model.");
        else
            return listPackage(modelOuid);
    }

    public ArrayList listAllPackage()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        PreparedStatement stat2;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        stat2 = null;
        ResultSet rs2 = null;
        DOSPackage dosPackage = null;
        ArrayList returnList = null;
        DOSChangeable packageData = null;
        long ouidLong = 0L;
        String ouid = null;
        long ouidLong2 = 0L;
        String ouid2 = null;
        int rows = 0;
        if(packageMap.size() > 0)
        {
            returnList = new ArrayList();
            Iterator mapKey;
            for(mapKey = packageMap.keySet().iterator(); mapKey.hasNext();)
            {
                packageData = new DOSChangeable();
                dosPackage = (DOSPackage)packageMap.get(mapKey.next());
                packageData.put("ouid", Long.toHexString(dosPackage.OUID));
                packageData.put("isGlobal", new Boolean(dosPackage.isGlobal));
                packageData.put("name", dosPackage.name);
                packageData.put("description", dosPackage.description);
                returnList.add(packageData);
                packageData = null;
                dosPackage = null;
            }

            mapKey = null;
            packageData = null;
            return returnList;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select dp.ouid, dp.name, dp.global, dp.des from dospkg dp ");
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            TreeMap tmpModelMap = null;
            TreeMap tmpPackageMap = null;
            DOSModel dosModel = null;
            DOSChangeable packageData;
            while(rs.next()) 
            {
                packageData = new DOSChangeable();
                long ouidLong = rs.getLong(1);
                String ouid = Long.toHexString(ouidLong);
                packageData.put("ouid", ouid);
                packageData.put("name", rs.getString(2));
                packageData.put("isGlobal", Utils.getBoolean(rs, 3));
                packageData.put("description", rs.getString(4));
                returnList.add(packageData);
                DOSPackage dosPackage = new DOSPackage();
                tmpModelMap = new TreeMap();
                dosPackage.OUID = ouidLong;
                dosPackage.name = (String)packageData.get("name");
                dosPackage.isGlobal = Utils.getBoolean((Boolean)packageData.get("isGlobal"));
                dosPackage.description = (String)packageData.get("description");
                dosPackage.modelMap = tmpModelMap;
                if(modelMap.size() > 0)
                {
                    if(dosPackage.isGlobal)
                    {
                        stat2 = con.prepareStatement("select ouid from dosmod ");
                    } else
                    {
                        stat2 = con.prepareStatement("select dosmod from dosmodpkg where dospkg = ? ");
                        stat2.setLong(1, ouidLong);
                    }
                    ResultSet rs2;
                    for(rs2 = stat2.executeQuery(); rs2.next(); tmpModelMap.put(dosModel.name, dosModel))
                    {
                        long ouidLong2 = rs2.getLong(1);
                        String ouid2 = Long.toHexString(ouidLong2);
                        dosModel = (DOSModel)modelMap.get(ouid2);
                        tmpPackageMap = dosModel.packageMap;
                        if(tmpPackageMap == null)
                        {
                            tmpPackageMap = new TreeMap();
                            dosModel.packageMap = tmpPackageMap;
                        }
                        tmpPackageMap.put(dosPackage.name, dosPackage);
                        tmpPackageMap = null;
                    }

                    rs2.close();
                    rs2 = null;
                    stat2.close();
                    stat2 = null;
                }
                tmpModelMap = null;
                packageMap.put(ouid, dosPackage);
                if(foundationPackage == null)
                {
                    getClass();
                    if("Foundation".equals(packageData.get("name")))
                    {
                        foundationPackage = dosPackage;
                        foundationPackageOuid = ouid;
                    }
                }
                dosPackage = null;
                packageData = null;
            }
            packageData = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            arraylist = returnList;
        }
        finally
        {
            DTMUtil.closeStatementSafely(stat2);
            stat2 = null;
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        e;
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public void linkPackageAndModel(String modelOuid, String packageOuid)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long returnLong = Utils.convertOuidToLong(packageOuid);
        String returnString = Long.toHexString(returnLong);
        int rows = 0;
        DOSChangeable packageDefinition = getPackage(packageOuid);
        HashMap valueMap = packageDefinition.getValueMap();
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("Parameter value is null : modelOuid");
        if(Utils.isNullString(packageOuid))
            throw new IIPRequestException("Parameter value is null : packageOuid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into dosmodpkg (dosmod, dospkg ) values ( ?, ? ) ");
            stat.setLong(1, Utils.convertOuidToLong(modelOuid));
            stat.setLong(2, returnLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            DOSPackage dosPackage = new DOSPackage();
            dosPackage.OUID = returnLong;
            dosPackage.name = (String)valueMap.get("name");
            dosPackage.description = (String)valueMap.get("description");
            this.packageMap.put(returnString, dosPackage);
            DOSModel dosModel = (DOSModel)modelMap.get(modelOuid);
            if(dosModel != null)
            {
                TreeMap packageMap = dosModel.packageMap;
                if(packageMap == null)
                {
                    packageMap = new TreeMap();
                    dosModel.packageMap = packageMap;
                }
                packageMap.put(dosPackage.name, dosPackage);
                packageMap = null;
                packageMap = dosPackage.modelMap;
                if(packageMap == null)
                {
                    packageMap = new TreeMap();
                    dosPackage.modelMap = packageMap;
                }
                packageMap.put(dosModel.name, dosModel);
                packageMap = null;
                dosModel = null;
            }
            dosPackage = null;
            valueMap = null;
            updateTimeToken();
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

    public void unlinkPackageAndModel(String modelOuid, String packageOuid)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long returnLong = Utils.convertOuidToLong(packageOuid);
        String returnString = Long.toHexString(returnLong);
        int rows = 0;
        int count = 0;
        DOSChangeable packageDefinition = getPackage(packageOuid);
        HashMap valueMap = packageDefinition.getValueMap();
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("Parameter value is null : modelOuid");
        if(Utils.isNullString(packageOuid))
            throw new IIPRequestException("Parameter value is null : packageOuid");
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select count(*) from dosmodpkg where dospkg = ? ");
            stat.setLong(1, returnLong);
            rs = stat.executeQuery();
            if(rs.next())
                count = rs.getInt(1);
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            if(count > 1)
            {
                stat = con.prepareStatement("delete from dosmodpkg where dosmod=? and  dospkg=? ");
                stat.setLong(1, Utils.convertOuidToLong(modelOuid));
                stat.setLong(2, returnLong);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                con.commit();
                con.close();
                xc.close();
                DOSPackage dosPackage = new DOSPackage();
                dosPackage = (DOSPackage)packageMap.get(packageOuid);
                DOSModel dosModel = null;
                TreeMap treeMap = null;
                Iterator treeKey = null;
                treeMap = dosPackage.modelMap;
                if(treeMap != null && treeMap.size() > 0)
                    for(treeKey = treeMap.keySet().iterator(); treeKey.hasNext(); treeKey.remove())
                    {
                        dosModel = (DOSModel)treeMap.get(treeKey.next());
                        if(dosModel.packageMap != null && dosModel.packageMap.size() > 0)
                            dosModel.packageMap.remove(dosPackage.name);
                        dosModel = null;
                    }

                treeMap = null;
                dosPackage = null;
                dosModel = null;
            } else
            {
                throw new IIPRequestException("The link is the only thing");
            }
            updateTimeToken();
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

    public String createClass(DOSChangeable classDefinition)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        LinkedList tempList;
        HashMap valueMap;
        String classOuidMapKey;
        xc = null;
        con = null;
        stat = null;
        DOSClass superClass = null;
        List superList = null;
        tempList = null;
        LinkedList subList = null;
        Iterator superKey = null;
        TreeMap treeMap = null;
        Integer tempInteger = null;
        Boolean tempBoolean = null;
        String returnString = null;
        long returnLong = 0L;
        int rows = 0;
        if(classDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + classDefinition);
        valueMap = classDefinition.getValueMap();
        if(valueMap == null || valueMap.get("name") == null || valueMap.get("ouid@package") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        if(Utils.getBoolean((Boolean)valueMap.get("is.leaf")) && Utils.isNullString((String)valueMap.get("code")))
            throw new IIPRequestException("Leaf class must have a value of 'code'.");
        classOuidMapKey = null;
        String codeStr = (String)valueMap.get("code");
        classOuidMapKey = codeStr.toLowerCase().replace(' ', '_');
        if(!Utils.isNullString(classOuidMapKey) && classOuidMap.containsKey(classOuidMapKey))
            throw new IIPRequestException("Code of Class is duplicated : " + classOuidMapKey);
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            long returnLong = generateOUID(con);
            String returnString = Long.toHexString(returnLong);
            stat = con.prepareStatement("insert into dosclas (ouid,name,des,dospkg,root,leaf,ver,capa,code,tit,tooltip,icon,dsid,xaobj,filecntl, abst, uniqnum ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            stat.setLong(1, returnLong);
            stat.setString(2, (String)valueMap.get("name"));
            stat.setString(3, (String)valueMap.get("description"));
            stat.setLong(4, Utils.convertOuidToLong((String)valueMap.get("ouid@package")));
            Utils.setBoolean(stat, 5, (Boolean)valueMap.get("is.root"));
            Utils.setBoolean(stat, 6, (Boolean)valueMap.get("is.leaf"));
            Utils.setBoolean(stat, 7, (Boolean)valueMap.get("is.versionable"));
            Utils.setInt(stat, 8, (Integer)valueMap.get("capacity"));
            stat.setString(9, (String)valueMap.get("code"));
            stat.setString(10, (String)valueMap.get("title"));
            stat.setString(11, (String)valueMap.get("tooltip"));
            stat.setString(12, (String)valueMap.get("icon"));
            stat.setString(13, (String)valueMap.get("datasource.id"));
            Utils.setBoolean(stat, 14, (Boolean)valueMap.get("is.xa.object"));
            Utils.setBoolean(stat, 15, (Boolean)valueMap.get("is.filecontrol"));
            Utils.setBoolean(stat, 16, (Boolean)valueMap.get("is.abstract"));
            Utils.setBoolean(stat, 17, (Boolean)valueMap.get("use.unique.number"));
            int rows = stat.executeUpdate();
            stat.close();
            stat = null;
            List superList = (List)valueMap.get("array$ouid.superclass");
            if(superList != null && superList.size() > 0)
            {
                stat = con.prepareStatement("insert into dossuperclas (supclas, dosclas, seq ) values (?, ?, ? ) ");
                String tempString = null;
                int i = 0;
                tempList = new LinkedList();
                Iterator superKey;
                for(superKey = superList.iterator(); superKey.hasNext(); tempList.add(classMap.get(tempString)))
                {
                    i++;
                    tempString = (String)superKey.next();
                    stat.setLong(1, Utils.convertOuidToLong(tempString));
                    stat.setLong(2, returnLong);
                    stat.setInt(3, i);
                    stat.executeUpdate();
                }

                stat.close();
                stat = null;
                superKey = null;
            }
            con.commit();
            con.close();
            xc.close();
            DOSClass dosClass = new DOSClass();
            dosClass.OUID = returnLong;
            dosClass.name = (String)valueMap.get("name");
            dosClass.description = (String)valueMap.get("description");
            dosClass._package = (DOSPackage)packageMap.get((String)valueMap.get("ouid@package"));
            dosClass.superClassList = tempList;
            dosClass.isRoot = (Boolean)valueMap.get("is.root");
            dosClass.isLeaf = (Boolean)valueMap.get("is.leaf");
            dosClass.versionable = (Boolean)valueMap.get("is.versionable");
            dosClass.fileControl = (Boolean)valueMap.get("is.filecontrol");
            dosClass.capacity = (Integer)valueMap.get("capacity");
            dosClass.code = (String)valueMap.get("code");
            dosClass.title = (String)valueMap.get("title");
            dosClass.tooltip = (String)valueMap.get("tooltip");
            dosClass.icon = (String)valueMap.get("icon");
            dosClass.datasourceId = (String)valueMap.get("datasource.id");
            dosClass.isXAClass = (Boolean)valueMap.get("is.xa.object");
            dosClass.isAbstract = (Boolean)valueMap.get("is.abstract");
            dosClass.useUniqueNumber = (Boolean)valueMap.get("use.unique.number");
            classMap.put(returnString, dosClass);
            classOuidMap.put(classOuidMapKey, returnString);
            TreeMap treeMap = dosClass._package.classMap;
            if(treeMap == null)
            {
                treeMap = new TreeMap();
                dosClass._package.classMap = treeMap;
            }
            treeMap.put(dosClass.name, dosClass);
            treeMap = null;
            if(tempList != null && tempList.size() > 0)
            {
                Iterator superKey;
                for(superKey = tempList.iterator(); superKey.hasNext();)
                {
                    DOSClass superClass = (DOSClass)superKey.next();
                    if(superClass != null)
                    {
                        LinkedList subList = superClass.subClassList;
                        if(subList == null)
                        {
                            subList = new LinkedList();
                            superClass.subClassList = subList;
                        }
                        subList.add(dosClass);
                        subList = null;
                    }
                    superClass = null;
                }

                superKey = null;
            }
            dosClass = null;
            tempList = null;
            updateAllSubClassesForFoundationClass();
            updateTimeToken();
            s = returnString;
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

    public void removeClass(String ouid)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        long ouidLong = Utils.convertOuidToLong(ouid);
        int rows = 0;
        DOSClass dosClass = null;
        DOSClass superClass = null;
        DOSFieldGroup dosFieldGroup = null;
        DOSField dosField = null;
        DOSAction dosAction = null;
        DOSAssociation dosAssociation = null;
        TreeMap treeMap = null;
        LinkedList linkedList = null;
        LinkedList doList = null;
        Iterator treeKey = null;
        Iterator listKey = null;
        Iterator doKey = null;
        String tempOuid = null;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        dosClass = (DOSClass)classMap.get(ouid);
        if(dosClass == null)
            throw new IIPRequestException("Class not found : " + ouid);
        doList = new LinkedList();
        linkedList = dosClass.subClassList;
        if(linkedList != null)
            throw new IIPRequestException("Can not remove a class '" + dosClass.name + "'. Sub class exsits.");
        linkedList = null;
        treeMap = dosClass.fieldGroupMap;
        if(treeMap != null)
        {
            for(treeKey = treeMap.values().iterator(); treeKey.hasNext();)
            {
                dosFieldGroup = (DOSFieldGroup)treeKey.next();
                if(dosFieldGroup != null)
                {
                    tempOuid = Long.toHexString(dosFieldGroup.OUID);
                    dosFieldGroup = null;
                    doList.add(tempOuid);
                }
            }

            treeKey = null;
            treeMap = null;
            for(doKey = doList.iterator(); doKey.hasNext(); removeFieldGroup(tempOuid))
                tempOuid = (String)doKey.next();

            doKey = null;
        }
        doList.clear();
        treeMap = dosClass.fieldMap;
        if(treeMap != null)
        {
            for(treeKey = treeMap.values().iterator(); treeKey.hasNext();)
            {
                dosField = (DOSField)treeKey.next();
                if(dosField != null)
                {
                    tempOuid = Long.toHexString(dosField.OUID);
                    dosField = null;
                    doList.add(tempOuid);
                }
            }

            treeKey = null;
            treeMap = null;
            for(doKey = doList.iterator(); doKey.hasNext(); removeField(tempOuid))
            {
                tempOuid = (String)doKey.next();
                removeCADAttribute(tempOuid);
            }

            doKey = null;
        }
        doList.clear();
        treeMap = dosClass.actionMap;
        if(treeMap != null)
        {
            for(treeKey = treeMap.values().iterator(); treeKey.hasNext();)
            {
                dosAction = (DOSAction)treeKey.next();
                if(dosAction != null)
                {
                    tempOuid = Long.toHexString(dosAction.OUID);
                    dosAction = null;
                    doList.add(tempOuid);
                }
            }

            treeKey = null;
            treeMap = null;
            for(doKey = doList.iterator(); doKey.hasNext(); removeAction(tempOuid))
                tempOuid = (String)doKey.next();

            doKey = null;
        }
        doList.clear();
        linkedList = dosClass.end0List;
        if(linkedList != null)
        {
            for(listKey = linkedList.iterator(); listKey.hasNext();)
            {
                dosAssociation = (DOSAssociation)listKey.next();
                if(dosAssociation != null)
                {
                    dosAssociation._class = null;
                    dosAssociation = null;
                }
            }

            listKey = null;
            linkedList = null;
        }
        linkedList = dosClass.end1List;
        if(linkedList != null)
        {
            for(listKey = linkedList.iterator(); listKey.hasNext();)
            {
                dosAssociation = (DOSAssociation)listKey.next();
                if(dosAssociation != null)
                {
                    tempOuid = Long.toHexString(dosAssociation.OUID);
                    dosAssociation = null;
                    if(!doList.contains(tempOuid))
                        doList.add(tempOuid);
                }
            }

            listKey = null;
            linkedList = null;
            for(doKey = doList.iterator(); doKey.hasNext(); removeAssociation(tempOuid))
                tempOuid = (String)doKey.next();

            doKey = null;
        }
        doList.clear();
        linkedList = dosClass.end2List;
        if(linkedList != null)
        {
            for(listKey = linkedList.iterator(); listKey.hasNext();)
            {
                dosAssociation = (DOSAssociation)listKey.next();
                if(dosAssociation != null)
                {
                    tempOuid = Long.toHexString(dosAssociation.OUID);
                    dosAssociation = null;
                    if(!doList.contains(tempOuid))
                        doList.add(tempOuid);
                }
            }

            listKey = null;
            linkedList = null;
            for(doKey = doList.iterator(); doKey.hasNext(); removeAssociation(tempOuid))
                tempOuid = (String)doKey.next();

            doKey = null;
        }
        doList = null;
        linkedList = dosClass.superClassList;
        if(linkedList != null)
        {
            for(listKey = linkedList.iterator(); listKey.hasNext();)
            {
                superClass = (DOSClass)listKey.next();
                listKey.remove();
                if(superClass != null && superClass.subClassList != null)
                {
                    superClass.subClassList.remove(dosClass);
                    superClass = null;
                }
            }

            dosClass.superClassList = null;
            listKey = null;
            linkedList = null;
        }
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from dossuperclas where dosclas=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from dosclas where ouid=? ");
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            classMap.remove(ouid);
            String classOuidMapKey = null;
            String codeStr = dosClass.code;
            classOuidMapKey = codeStr.toLowerCase().replace(' ', '_');
            classOuidMap.remove(classOuidMapKey);
            DOSPackage dosPackage = dosClass._package;
            if(dosPackage.classMap != null && dosPackage.classMap.size() > 0)
                dosPackage.classMap.remove(dosClass.name);
            dosPackage = null;
            dosClass = null;
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE/" + ouid);
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + ouid);
            updateTimeToken();
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

    public void removeClassCascade(String ouid)
        throws IIPRequestException
    {
        removeClassCascadeHelper(ouid);
        updateAllSubClassesForFoundationClass();
        updateTimeToken();
    }

    public void removeClassCascadeHelper(String ouid)
        throws IIPRequestException
    {
        DOSClass dosClass = null;
        DOSClass subClass = null;
        LinkedList subClassList = null;
        LinkedList tempList = null;
        Iterator listKey = null;
        String tempOuid = null;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        dosClass = (DOSClass)classMap.get(ouid);
        if(dosClass == null)
            throw new IIPRequestException("Class not found : " + ouid);
        tempList = new LinkedList();
        subClassList = dosClass.subClassList;
        if(subClassList != null)
        {
            for(listKey = subClassList.iterator(); listKey.hasNext();)
            {
                subClass = (DOSClass)listKey.next();
                if(subClass != null)
                {
                    tempOuid = Long.toHexString(subClass.OUID);
                    subClass = null;
                    tempList.add(tempOuid);
                }
            }

            listKey = null;
            for(listKey = tempList.iterator(); listKey.hasNext(); removeClassCascadeHelper(tempOuid))
                tempOuid = (String)listKey.next();

            listKey = null;
            if(subClassList.isEmpty())
                dosClass.subClassList = null;
            subClassList = null;
            removeClass(ouid);
        } else
        {
            removeClass(ouid);
        }
        dosClass = null;
    }

    public DOSChangeable getClass(String ouid)
        throws IIPRequestException
    {
        DOSChangeable returnObject = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        DOSClass dosClass = (DOSClass)classMap.get(ouid);
        ArrayList superList = null;
        LinkedList tempList = null;
        DOSClass superClass = null;
        long tempLong = 0L;
        if(dosClass != null)
        {
            returnObject = new DOSChangeable();
            returnObject.put("ouid", ouid);
            returnObject.put("name", dosClass.name);
            returnObject.put("description", dosClass.description);
            DOSPackage dosPackage = dosClass._package;
            if(dosPackage != null)
                returnObject.put("ouid@package", Long.toHexString(dosClass._package.OUID));
            if(dosClass.superClassList != null && dosClass.superClassList.size() > 0)
            {
                superList = new ArrayList(dosClass.superClassList.size());
                Iterator superKey;
                for(superKey = dosClass.superClassList.iterator(); superKey.hasNext(); superList.add(Long.toHexString(superClass.OUID)))
                    superClass = (DOSClass)superKey.next();

                returnObject.put("array$ouid.superclass", superList);
                superKey = null;
                superClass = null;
                superList = null;
            }
            returnObject.put("is.root", dosClass.isRoot);
            returnObject.put("is.leaf", dosClass.isLeaf);
            returnObject.put("is.xa.object", dosClass.isXAClass);
            returnObject.put("datasource.id", dosClass.datasourceId);
            returnObject.put("is.versionable", dosClass.versionable);
            returnObject.put("is.filecontrol", dosClass.fileControl);
            returnObject.put("capacity", dosClass.capacity);
            returnObject.put("code", dosClass.code);
            returnObject.put("title", dosClass.title);
            returnObject.put("tooltip", dosClass.tooltip);
            returnObject.put("icon", dosClass.icon);
            returnObject.put("is.association.class", dosClass.isAssociationClass);
            returnObject.put("is.abstract", dosClass.isAbstract);
            returnObject.put("use.unique.number", dosClass.useUniqueNumber);
            dosClass = null;
            return returnObject;
        } else
        {
            return null;
        }
    }

    public DOSChangeable getClassWithName(String packageOuid, String className)
        throws IIPRequestException
    {
        if(Utils.isNullString(packageOuid))
            throw new IIPRequestException("Parameter value is null : packageOuid");
        if(Utils.isNullString(className))
            throw new IIPRequestException("Parameter value is null : className");
        ArrayList listClass = listClassInPackage(packageOuid);
        if(listClass != null)
        {
            for(int i = 0; i < listClass.size(); i++)
            {
                DOSChangeable dosClass = (DOSChangeable)listClass.get(i);
                if(dosClass.get("name").equals(className))
                    return dosClass;
            }

        }
        return null;
    }

    public void setClass(DOSChangeable classDefinition)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        if(classDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + classDefinition);
        HashMap valueMap = classDefinition.getValueMap();
        HashMap originalMap = classDefinition.getOriginalValueMap();
        List superList = null;
        LinkedList oldList = null;
        LinkedList tempList = null;
        LinkedList subList = null;
        Iterator superKey = null;
        DOSClass superClass = null;
        String ouid = null;
        int rows = 0;
        if(valueMap == null || valueMap.get("ouid") == null || valueMap.get("name") == null || valueMap.get("ouid@package") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        ouid = (String)valueMap.get("ouid");
        long ouidLong = Utils.convertOuidToLong(ouid);
        if(Utils.getBoolean((Boolean)valueMap.get("is.leaf")) && Utils.isNullString((String)valueMap.get("code")))
            throw new IIPRequestException("Leaf class must have a value of 'code'.");
        String classOuidMapKey = null;
        String originalClassOuidMapKey = null;
        String codeStr = (String)valueMap.get("code");
        String originalCodeStr = (String)originalMap.get("code");
        if(!codeStr.equals(originalCodeStr))
        {
            originalClassOuidMapKey = originalCodeStr.toLowerCase().replace(' ', '_');
            if(!Utils.isNullString(originalClassOuidMapKey))
                classOuidMap.remove(originalClassOuidMapKey);
            classOuidMapKey = codeStr.toLowerCase().replace(' ', '_');
            if(!Utils.isNullString(classOuidMapKey) && classOuidMap.containsKey(classOuidMapKey))
                throw new IIPRequestException("Code of Class is duplicated : " + classOuidMapKey);
        }
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update dosclas set name=?, des=?, dospkg=?, root=?, leaf=?, ver=?, capa=?, code=?, tit=?, tooltip=?, icon=?, dsid=?, xaobj=?, filecntl=?, abst=?, uniqnum=? where ouid=? ");
            stat.setString(1, (String)valueMap.get("name"));
            stat.setString(2, (String)valueMap.get("description"));
            stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("ouid@package")));
            Utils.setBoolean(stat, 4, (Boolean)valueMap.get("is.root"));
            Utils.setBoolean(stat, 5, (Boolean)valueMap.get("is.leaf"));
            Utils.setBoolean(stat, 6, (Boolean)valueMap.get("is.versionable"));
            Utils.setInt(stat, 7, (Integer)valueMap.get("capacity"));
            stat.setString(8, (String)valueMap.get("code"));
            stat.setString(9, (String)valueMap.get("title"));
            stat.setString(10, (String)valueMap.get("tooltip"));
            stat.setString(11, (String)valueMap.get("icon"));
            stat.setString(12, (String)valueMap.get("datasource.id"));
            Utils.setBoolean(stat, 13, (Boolean)valueMap.get("is.xa.object"));
            Utils.setBoolean(stat, 14, (Boolean)valueMap.get("is.filecontrol"));
            Utils.setBoolean(stat, 15, (Boolean)valueMap.get("is.abstract"));
            Utils.setBoolean(stat, 16, (Boolean)valueMap.get("use.unique.number"));
            stat.setLong(17, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            superList = (List)valueMap.get("array$ouid.superclass");
            stat = con.prepareStatement("delete from dossuperclas where dosclas=? ");
            stat.setLong(1, ouidLong);
            stat.executeUpdate();
            stat.close();
            stat = null;
            if(superList != null && superList.size() > 0)
            {
                stat = con.prepareStatement("insert into dossuperclas (supclas, dosclas, seq ) values (?, ?, ? ) ");
                String tempString = null;
                int i = 0;
                tempList = new LinkedList();
                for(superKey = superList.iterator(); superKey.hasNext(); tempList.add(classMap.get(tempString)))
                {
                    i++;
                    tempString = (String)superKey.next();
                    stat.setLong(1, Utils.convertOuidToLong(tempString));
                    stat.setLong(2, ouidLong);
                    stat.setInt(3, i);
                    stat.executeUpdate();
                }

                stat.close();
                stat = null;
                superKey = null;
            }
            con.commit();
            con.close();
            xc.close();
            DOSClass dosClass = (DOSClass)classMap.get(ouid);
            if(dosClass == null)
            {
                dosClass = new DOSClass();
                dosClass.OUID = ouidLong;
                classMap.put(ouid, dosClass);
            }
            String nameString = dosClass.name;
            dosClass.name = (String)valueMap.get("name");
            dosClass.description = (String)valueMap.get("description");
            dosClass._package = (DOSPackage)packageMap.get((String)valueMap.get("ouid@package"));
            if(dosClass.superClassList != null)
            {
                oldList = (LinkedList)dosClass.superClassList.clone();
                dosClass.superClassList.clear();
                dosClass.superClassList = null;
            }
            dosClass.superClassList = tempList;
            dosClass.isRoot = (Boolean)valueMap.get("is.root");
            dosClass.isLeaf = (Boolean)valueMap.get("is.leaf");
            dosClass.versionable = (Boolean)valueMap.get("is.versionable");
            dosClass.fileControl = (Boolean)valueMap.get("is.filecontrol");
            dosClass.capacity = (Integer)valueMap.get("capacity");
            dosClass.code = (String)valueMap.get("code");
            dosClass.title = (String)valueMap.get("title");
            dosClass.tooltip = (String)valueMap.get("tooltip");
            dosClass.icon = (String)valueMap.get("icon");
            dosClass.datasourceId = (String)valueMap.get("datasource.id");
            dosClass.isXAClass = (Boolean)valueMap.get("is.xa.object");
            dosClass.isAbstract = (Boolean)valueMap.get("is.abstract");
            dosClass.useUniqueNumber = (Boolean)valueMap.get("use.unique.number");
            classOuidMap.put(classOuidMapKey, ouid);
            DOSPackage dosPackage = dosClass._package;
            if(dosPackage.classMap != null && dosPackage.classMap.size() > 0)
            {
                dosPackage.classMap.remove(nameString);
                dosPackage.classMap.put(dosClass.name, dosClass);
            }
            if(oldList != null && oldList.size() > 0)
            {
                for(superKey = oldList.iterator(); superKey.hasNext();)
                {
                    superClass = (DOSClass)superKey.next();
                    if(superClass != null)
                    {
                        subList = superClass.subClassList;
                        if(subList == null)
                        {
                            subList = new LinkedList();
                            superClass.subClassList = subList;
                        }
                        if(subList.contains(dosClass))
                            subList.remove(dosClass);
                        subList = null;
                    }
                    superClass = null;
                }

                superKey = null;
            }
            if(tempList != null && tempList.size() > 0)
            {
                for(superKey = tempList.iterator(); superKey.hasNext();)
                {
                    superClass = (DOSClass)superKey.next();
                    if(superClass != null)
                    {
                        subList = superClass.subClassList;
                        if(subList == null)
                        {
                            subList = new LinkedList();
                            superClass.subClassList = subList;
                        }
                        if(!subList.contains(dosClass))
                            subList.add(dosClass);
                        subList = null;
                    }
                    superClass = null;
                }

                superKey = null;
            }
            dosPackage = null;
            dosClass = null;
            tempList = null;
            superList = null;
            updateAllSubClassesForFoundationClass();
            updateTimeToken();
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

    private void updateAllSubClassesForFoundationClass()
    {
        if(foundationPackage == null || fobject == null || fversionobject == null || ffilecontrol == null)
            return;
        ArrayList ouidList = null;
        DOSClass dosClass = null;
        String ouid = null;
        Iterator ouidKey = null;
        String classOuidMapKey = null;
        String codeStr = null;
        try
        {
            for(ouidKey = classMap.keySet().iterator(); ouidKey.hasNext();)
            {
                ouid = (String)ouidKey.next();
                dosClass = (DOSClass)classMap.get(ouid);
                if(dosClass != null)
                {
                    dosClass.versionable = Boolean.FALSE;
                    dosClass.fileControl = Boolean.FALSE;
                    dosClass = null;
                }
            }

            ouidKey = null;
            fversionobject.versionable = Boolean.TRUE;
            ffilecontrol.fileControl = Boolean.TRUE;
            ouidList = listAllSubClassOuid(fversionobjectOuid);
            for(ouidKey = ouidList.iterator(); ouidKey.hasNext();)
            {
                ouid = (String)ouidKey.next();
                dosClass = (DOSClass)classMap.get(ouid);
                if(dosClass != null)
                {
                    dosClass.versionable = Boolean.TRUE;
                    codeStr = dosClass.code;
                    classOuidMapKey = codeStr.toLowerCase().replace(' ', '_');
                    classOuidMap.put(classOuidMapKey, ouid);
                    dosClass = null;
                }
            }

            ouidKey = null;
            ouidList.clear();
            ouidList = null;
            ouidList = listAllSubClassOuid(ffilecontrolOuid);
            for(ouidKey = ouidList.iterator(); ouidKey.hasNext();)
            {
                ouid = (String)ouidKey.next();
                dosClass = (DOSClass)classMap.get(ouid);
                if(dosClass != null)
                {
                    dosClass.fileControl = Boolean.TRUE;
                    dosClass = null;
                }
            }

            ouidKey = null;
            ouidList.clear();
            ouidList = null;
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList listFieldInClassInternal(String ouid)
        throws IIPRequestException
    {
        ArrayList returnList;
        TreeMap inheritField;
        LinkedList stack;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSField dosField = null;
        DOSClass dosClass = null;
        returnList = null;
        TreeMap fieldMap = null;
        TreeMap fieldList = null;
        Iterator superKey = null;
        Iterator fieldKey = null;
        Iterator mapKey = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        int rows = 0;
        inheritField = new TreeMap();
        stack = new LinkedList();
        ArrayList superList = null;
        dosClass = (DOSClass)classMap.get(ouid);
        ouidLong = dosClass.OUID;
        tempLong = dosClass.OUID;
        if(dosClass == null)
            return null;
        superList = listAllSuperClassInternal(ouid);
        if(inheritField.size() != 0)
            break MISSING_BLOCK_LABEL_568;
        try
        {
            if(superList != null)
                superKey = superList.iterator();
            fieldMap = new TreeMap();
            do
            {
                if(dosClass.fieldMap == null)
                {
                    if(stat == null)
                    {
                        xc = dtm.getPooledConnection("system");
                        con = xc.getConnection();
                        stat = con.prepareStatement("select df.ouid from dosfld df where df.dosclas=? ");
                    }
                    stat.setLong(1, tempLong);
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        dosField = (DOSField)this.fieldMap.get(Long.toHexString(rs.getLong(1)));
                        if(dosField != null)
                        {
                            if(!inheritField.containsKey(dosField.name))
                                inheritField.put(dosField.name, dosField);
                            if(dosField._class.OUID == ouidLong)
                                fieldMap.put(dosField.name, dosField);
                            dosField = null;
                        }
                    }

                    rs.close();
                    rs = null;
                } else
                {
                    for(mapKey = dosClass.fieldMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        dosField = (DOSField)dosClass.fieldMap.get(mapKey.next());
                        if(!inheritField.containsKey(dosField.name))
                            inheritField.put(dosField.name, dosField);
                        if(dosField._class.OUID == ouidLong)
                            fieldMap.put(dosField.name, dosField);
                        dosField = null;
                    }

                    mapKey = null;
                }
                dosClass = null;
                if(superKey != null && superKey.hasNext())
                {
                    dosClass = (DOSClass)superKey.next();
                    tempLong = dosClass.OUID;
                    stack.addLast(dosClass);
                }
            } while(dosClass != null);
            if(stat != null)
            {
                stat.close();
                stat = null;
                con.commit();
                con.close();
                xc.close();
            }
            superKey = null;
            dosClass = (DOSClass)classMap.get(ouid);
            dosClass.fieldMap = fieldMap;
            fieldMap = null;
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
        if(inheritField.size() > 0)
        {
            returnList = new ArrayList(inheritField.size());
            Iterator fieldKey;
            for(fieldKey = inheritField.keySet().iterator(); fieldKey.hasNext(); returnList.add(inheritField.get(fieldKey.next())));
            fieldKey = null;
        }
        inheritField.clear();
        inheritField = null;
        stack.clear();
        stack = null;
        ArrayList superList = null;
        return returnList;
    }

    public ArrayList listFieldInClass(String ouid)
        throws IIPRequestException
    {
        ArrayList returnList;
        TreeMap inheritField;
        LinkedList stack;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSField dosField = null;
        DOSClass dosClass = null;
        returnList = null;
        TreeMap fieldMap = null;
        DOSChangeable fieldData = null;
        TreeMap fieldList = null;
        Iterator superKey = null;
        Iterator fieldKey = null;
        Iterator mapKey = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        int rows = 0;
        inheritField = new TreeMap();
        stack = new LinkedList();
        ArrayList superList = null;
        dosClass = (DOSClass)classMap.get(ouid);
        ouidLong = dosClass.OUID;
        tempLong = dosClass.OUID;
        if(dosClass == null)
            return null;
        superList = listAllSuperClassInternal(ouid);
        if(inheritField.size() != 0)
            break MISSING_BLOCK_LABEL_1438;
        try
        {
            if(superList != null)
                superKey = superList.iterator();
            fieldMap = new TreeMap();
            do
            {
                if(dosClass.fieldMap == null || dosClass.fieldMap.size() == 0)
                {
                    if(stat == null)
                    {
                        xc = dtm.getPooledConnection("system");
                        con = xc.getConnection();
                        stat = con.prepareStatement("select df.ouid from dosfld df where df.dosclas=? ");
                    }
                    stat.setLong(1, tempLong);
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        fieldData = new DOSChangeable();
                        dosField = (DOSField)this.fieldMap.get(Long.toHexString(rs.getLong(1)));
                        if(dosField != null)
                        {
                            fieldData.put("ouid", Long.toHexString(dosField.OUID));
                            fieldData.put("name", dosField.name);
                            fieldData.put("description", dosField.description);
                            if(dosField._class != null)
                                fieldData.put("ouid@class", Long.toHexString(dosField._class.OUID));
                            fieldData.put("type", dosField.type);
                            if(dosField.typeClass != null)
                                fieldData.put("type.ouid@class", Long.toHexString(dosField.typeClass.OUID));
                            if(dosField.typeCode != null)
                                fieldData.put("type.ouid@class", Long.toHexString(dosField.typeCode.OUID));
                            if(dosField.referencedFieldOuid != 0L)
                                fieldData.put("type.ouid@class", Long.toHexString(dosField.referencedFieldOuid));
                            fieldData.put("multiplicity.from", dosField.multiplicity_from);
                            fieldData.put("multiplicity.to", dosField.multiplicity_to);
                            fieldData.put("changeable", dosField.changeable);
                            fieldData.put("scope", dosField.scope);
                            fieldData.put("initial.value", dosField.initialValue);
                            fieldData.put("index", dosField.index);
                            fieldData.put("code", dosField.code);
                            fieldData.put("is.mandatory", dosField.isMandatory);
                            fieldData.put("is.visible", dosField.isVisible);
                            fieldData.put("size", dosField.size);
                            fieldData.put("title", dosField.title);
                            fieldData.put("tooltip", dosField.tooltip);
                            fieldData.put("width", dosField.width);
                            fieldData.put("height", dosField.height);
                            fieldData.put("title.width", dosField.titleWidth);
                            fieldData.put("is.title.visible", dosField.isTitleVisible);
                            fieldData.put("icon", dosField.icon);
                            fieldData.put("column", dosField.column);
                            if(!inheritField.containsKey(dosField.name))
                                inheritField.put(dosField.name, fieldData);
                            if(dosField._class.OUID == ouidLong)
                                fieldMap.put(dosField.name, dosField);
                            fieldData = null;
                            dosField = null;
                        }
                    }

                    rs.close();
                    rs = null;
                } else
                {
                    for(mapKey = dosClass.fieldMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        fieldData = new DOSChangeable();
                        dosField = (DOSField)dosClass.fieldMap.get(mapKey.next());
                        fieldData.put("ouid", Long.toHexString(dosField.OUID));
                        fieldData.put("name", dosField.name);
                        fieldData.put("description", dosField.description);
                        if(dosField._class != null)
                            fieldData.put("ouid@class", Long.toHexString(dosField._class.OUID));
                        fieldData.put("type", dosField.type);
                        if(dosField.typeClass != null)
                            fieldData.put("type.ouid@class", Long.toHexString(dosField.typeClass.OUID));
                        if(dosField.typeCode != null)
                            fieldData.put("type.ouid@class", Long.toHexString(dosField.typeCode.OUID));
                        if(dosField.referencedFieldOuid != 0L)
                            fieldData.put("type.ouid@class", Long.toHexString(dosField.referencedFieldOuid));
                        fieldData.put("multiplicity.from", dosField.multiplicity_from);
                        fieldData.put("multiplicity.to", dosField.multiplicity_to);
                        fieldData.put("changeable", dosField.changeable);
                        fieldData.put("scope", dosField.scope);
                        fieldData.put("initial.value", dosField.initialValue);
                        fieldData.put("index", dosField.index);
                        fieldData.put("code", dosField.code);
                        fieldData.put("is.mandatory", dosField.isMandatory);
                        fieldData.put("is.visible", dosField.isVisible);
                        fieldData.put("size", dosField.size);
                        fieldData.put("title", dosField.title);
                        fieldData.put("tooltip", dosField.tooltip);
                        fieldData.put("width", dosField.width);
                        fieldData.put("height", dosField.height);
                        fieldData.put("title.width", dosField.titleWidth);
                        fieldData.put("is.title.visible", dosField.isTitleVisible);
                        fieldData.put("icon", dosField.icon);
                        fieldData.put("column", dosField.column);
                        if(!inheritField.containsKey(dosField.name))
                            inheritField.put(dosField.name, fieldData);
                        if(dosField._class.OUID == ouidLong)
                            fieldMap.put(dosField.name, dosField);
                        fieldData = null;
                        dosField = null;
                    }

                    mapKey = null;
                }
                dosClass = null;
                if(superKey != null && superKey.hasNext())
                {
                    dosClass = (DOSClass)superKey.next();
                    tempLong = dosClass.OUID;
                    stack.addLast(dosClass);
                }
            } while(dosClass != null);
            if(stat != null)
            {
                stat.close();
                stat = null;
                con.commit();
                con.close();
                xc.close();
            }
            superKey = null;
            dosClass = (DOSClass)classMap.get(ouid);
            if(fieldMap.size() > 0)
                dosClass.fieldMap = fieldMap;
            else
                dosClass.fieldMap = null;
            fieldData = null;
            fieldMap = null;
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        if(inheritField.size() > 0)
        {
            returnList = new ArrayList(inheritField.size());
            Iterator fieldKey;
            for(fieldKey = inheritField.keySet().iterator(); fieldKey.hasNext(); returnList.add(inheritField.get(fieldKey.next())));
            fieldKey = null;
        }
        inheritField.clear();
        inheritField = null;
        stack.clear();
        stack = null;
        ArrayList superList = null;
        Utils.sort(returnList);
        return returnList;
    }

    public ArrayList listActionInClass(String ouid)
        throws IIPRequestException
    {
        ArrayList returnList;
        TreeMap inheritAction;
        LinkedList stack;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSAction dosAction = null;
        DOSClass dosClass = null;
        returnList = null;
        TreeMap actionMap = null;
        DOSChangeable actionData = null;
        TreeMap actionList = null;
        Iterator superKey = null;
        Iterator actionKey = null;
        Iterator mapKey = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        int rows = 0;
        inheritAction = new TreeMap();
        stack = new LinkedList();
        ArrayList superList = null;
        dosClass = (DOSClass)classMap.get(ouid);
        ouidLong = dosClass.OUID;
        tempLong = dosClass.OUID;
        if(dosClass == null)
            return null;
        superList = listAllSuperClassInternal(ouid);
        if(inheritAction.size() != 0)
            break MISSING_BLOCK_LABEL_1150;
        try
        {
            if(superList != null)
                superKey = superList.iterator();
            actionMap = new TreeMap();
            do
            {
                if(dosClass.actionMap == null || dosClass.actionMap.size() == 0)
                {
                    if(stat == null)
                    {
                        xc = dtm.getPooledConnection("system");
                        con = xc.getConnection();
                        stat = con.prepareStatement("select da.ouid from dosact da where da.dosclas=? ");
                    }
                    stat.setLong(1, tempLong);
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        actionData = new DOSChangeable();
                        dosAction = (DOSAction)this.actionMap.get(Long.toHexString(rs.getLong(1)));
                        if(dosAction != null)
                        {
                            actionData.put("ouid", Long.toHexString(dosAction.OUID));
                            actionData.put("name", dosAction.name);
                            actionData.put("description", dosAction.description);
                            if(dosAction._class != null)
                                actionData.put("ouid@class", Long.toHexString(dosAction._class.OUID));
                            actionData.put("return.type", dosAction.returnType);
                            if(dosAction.returnTypeClass != null)
                                actionData.put("return.type.ouid@class", Long.toHexString(dosAction.returnTypeClass.OUID));
                            actionData.put("scope", dosAction.scope);
                            actionData.put("call.type", dosAction.callType);
                            actionData.put("is.leaf", dosAction.isLeaf);
                            actionData.put("is.query", dosAction.isQuery);
                            actionData.put("is.visible", dosAction.isVisible);
                            actionData.put("title", dosAction.title);
                            actionData.put("mnemonic", dosAction.mnemonic);
                            actionData.put("accelerator", dosAction.accelerator);
                            actionData.put("icon", dosAction.icon);
                            actionData.put("icon32", dosAction.icon32);
                            actionData.put("icon24", dosAction.icon24);
                            if(!inheritAction.containsKey(dosAction.name))
                                inheritAction.put(dosAction.name, actionData);
                            if(dosAction._class.OUID == ouidLong)
                                actionMap.put(dosAction.name, dosAction);
                            actionData = null;
                            dosAction = null;
                        }
                    }

                    rs.close();
                    rs = null;
                } else
                {
                    for(mapKey = dosClass.actionMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        actionData = new DOSChangeable();
                        dosAction = (DOSAction)dosClass.actionMap.get(mapKey.next());
                        actionData.put("ouid", Long.toHexString(dosAction.OUID));
                        actionData.put("name", dosAction.name);
                        actionData.put("description", dosAction.description);
                        if(dosAction._class != null)
                            actionData.put("ouid@class", Long.toHexString(dosAction._class.OUID));
                        actionData.put("return.type", dosAction.returnType);
                        if(dosAction.returnTypeClass != null)
                            actionData.put("return.type.ouid@class", Long.toHexString(dosAction.returnTypeClass.OUID));
                        actionData.put("scope", dosAction.scope);
                        actionData.put("call.type", dosAction.callType);
                        actionData.put("is.leaf", dosAction.isLeaf);
                        actionData.put("is.query", dosAction.isQuery);
                        actionData.put("is.visible", dosAction.isVisible);
                        actionData.put("title", dosAction.title);
                        actionData.put("mnemonic", dosAction.mnemonic);
                        actionData.put("accelerator", dosAction.accelerator);
                        actionData.put("icon", dosAction.icon);
                        actionData.put("icon32", dosAction.icon32);
                        actionData.put("icon24", dosAction.icon24);
                        if(!inheritAction.containsKey(dosAction.name))
                            inheritAction.put(dosAction.name, actionData);
                        if(dosAction._class.OUID == ouidLong)
                            actionMap.put(dosAction.name, dosAction);
                        actionData = null;
                        dosAction = null;
                    }

                    mapKey = null;
                }
                dosClass = null;
                if(superKey != null && superKey.hasNext())
                {
                    dosClass = (DOSClass)superKey.next();
                    tempLong = dosClass.OUID;
                    stack.addLast(dosClass);
                }
            } while(dosClass != null);
            if(stat != null)
            {
                stat.close();
                stat = null;
                con.commit();
                con.close();
                xc.close();
            }
            superKey = null;
            dosClass = (DOSClass)classMap.get(ouid);
            if(actionMap.size() > 0)
                dosClass.actionMap = actionMap;
            else
                dosClass.actionMap = null;
            actionData = null;
            actionMap = null;
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        if(inheritAction.size() > 0)
        {
            returnList = new ArrayList(inheritAction.size());
            Iterator actionKey;
            for(actionKey = inheritAction.keySet().iterator(); actionKey.hasNext(); returnList.add(inheritAction.get(actionKey.next())));
            actionKey = null;
        }
        inheritAction.clear();
        inheritAction = null;
        stack.clear();
        stack = null;
        ArrayList superList = null;
        return returnList;
    }

    public ArrayList listFieldGroupInClass(String ouid)
        throws IIPRequestException
    {
        ArrayList returnList;
        TreeMap inheritFieldGroup;
        LinkedList stack;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSFieldGroup dosFieldGroup = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        DOSAction dosAction = null;
        returnList = null;
        TreeMap fieldGroupMap = null;
        LinkedList linkedList = null;
        ArrayList arrayList = null;
        Iterator listKey = null;
        Iterator superKey = null;
        Iterator fieldGroupKey = null;
        Iterator mapKey = null;
        DOSChangeable fieldGroupData = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        int rows = 0;
        inheritFieldGroup = new TreeMap();
        stack = new LinkedList();
        ArrayList superList = null;
        dosClass = (DOSClass)classMap.get(ouid);
        ouidLong = dosClass.OUID;
        tempLong = dosClass.OUID;
        if(dosClass == null)
            return null;
        superList = listAllSuperClassInternal(ouid);
        if(inheritFieldGroup.size() != 0)
            break MISSING_BLOCK_LABEL_1375;
        try
        {
            if(superList != null)
                superKey = superList.iterator();
            fieldGroupMap = new TreeMap();
            do
            {
                if(dosClass.fieldGroupMap == null || dosClass.fieldGroupMap.size() == 0)
                {
                    if(stat == null)
                    {
                        xc = dtm.getPooledConnection("system");
                        con = xc.getConnection();
                        stat = con.prepareStatement("select dfg.ouid from dosfldgrp dfg where dfg.dosclas=? ");
                    }
                    stat.setLong(1, tempLong);
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        fieldGroupData = new DOSChangeable();
                        dosFieldGroup = (DOSFieldGroup)this.fieldGroupMap.get(Long.toHexString(rs.getLong(1)));
                        if(dosFieldGroup != null)
                        {
                            fieldGroupData.put("ouid", Long.toHexString(dosFieldGroup.OUID));
                            fieldGroupData.put("name", dosFieldGroup.name);
                            fieldGroupData.put("description", dosFieldGroup.description);
                            fieldGroupData.put("title", dosFieldGroup.title);
                            if(dosFieldGroup._class != null)
                                fieldGroupData.put("ouid@class", Long.toHexString(dosFieldGroup._class.OUID));
                            linkedList = dosFieldGroup.fieldList;
                            if(linkedList != null && linkedList.size() > 0)
                            {
                                arrayList = new ArrayList();
                                for(listKey = linkedList.iterator(); listKey.hasNext();)
                                {
                                    dosField = (DOSField)listKey.next();
                                    if(dosField != null)
                                        arrayList.add(Long.toHexString(dosField.OUID));
                                    dosField = null;
                                }

                                fieldGroupData.put("array$ouid@field", arrayList);
                                arrayList = null;
                            }
                            linkedList = dosFieldGroup.actionList;
                            if(linkedList != null && linkedList.size() > 0)
                            {
                                arrayList = new ArrayList();
                                for(listKey = linkedList.iterator(); listKey.hasNext();)
                                {
                                    dosAction = (DOSAction)listKey.next();
                                    if(dosAction != null)
                                        arrayList.add(Long.toHexString(dosAction.OUID));
                                    dosAction = null;
                                }

                                fieldGroupData.put("array$ouid@action", arrayList);
                                arrayList = null;
                            }
                            fieldGroupData.put("index", dosFieldGroup.index);
                            fieldGroupData.put("is.mandatory", dosFieldGroup.isMandatory);
                            fieldGroupData.put("title", dosFieldGroup.title);
                            fieldGroupData.put("is.visible", dosFieldGroup.isVisible);
                            fieldGroupData.put("layout", dosFieldGroup.layout);
                            fieldGroupData.put("icon", dosFieldGroup.icon);
                            fieldGroupData.put("location", dosFieldGroup.location);
                            if(!inheritFieldGroup.containsKey(dosFieldGroup.name))
                                inheritFieldGroup.put(dosFieldGroup.name, fieldGroupData);
                            if(dosFieldGroup._class.OUID == ouidLong)
                                fieldGroupMap.put(dosFieldGroup.name, dosFieldGroup);
                            fieldGroupData = null;
                            dosFieldGroup = null;
                        }
                    }

                    rs.close();
                    rs = null;
                } else
                {
                    for(mapKey = dosClass.fieldGroupMap.keySet().iterator(); mapKey.hasNext();)
                    {
                        fieldGroupData = new DOSChangeable();
                        dosFieldGroup = (DOSFieldGroup)dosClass.fieldGroupMap.get(mapKey.next());
                        fieldGroupData.put("ouid", Long.toHexString(dosFieldGroup.OUID));
                        fieldGroupData.put("name", dosFieldGroup.name);
                        fieldGroupData.put("description", dosFieldGroup.description);
                        if(dosFieldGroup._class != null)
                            fieldGroupData.put("ouid@class", Long.toHexString(dosFieldGroup._class.OUID));
                        linkedList = dosFieldGroup.fieldList;
                        if(linkedList != null && linkedList.size() > 0)
                        {
                            arrayList = new ArrayList();
                            for(listKey = linkedList.iterator(); listKey.hasNext();)
                            {
                                dosField = (DOSField)listKey.next();
                                if(dosField != null)
                                    arrayList.add(Long.toHexString(dosField.OUID));
                                dosField = null;
                            }

                            fieldGroupData.put("array$ouid@field", arrayList);
                            arrayList = null;
                        }
                        linkedList = dosFieldGroup.actionList;
                        if(linkedList != null && linkedList.size() > 0)
                        {
                            arrayList = new ArrayList();
                            for(listKey = linkedList.iterator(); listKey.hasNext();)
                            {
                                dosAction = (DOSAction)listKey.next();
                                if(dosAction != null)
                                    arrayList.add(Long.toHexString(dosAction.OUID));
                                dosAction = null;
                            }

                            fieldGroupData.put("array$ouid@action", arrayList);
                            arrayList = null;
                        }
                        fieldGroupData.put("index", dosFieldGroup.index);
                        fieldGroupData.put("is.mandatory", dosFieldGroup.isMandatory);
                        fieldGroupData.put("title", dosFieldGroup.title);
                        fieldGroupData.put("is.visible", dosFieldGroup.isVisible);
                        fieldGroupData.put("layout", dosFieldGroup.layout);
                        fieldGroupData.put("icon", dosFieldGroup.icon);
                        fieldGroupData.put("location", dosFieldGroup.location);
                        if(!inheritFieldGroup.containsKey(dosFieldGroup.name))
                            inheritFieldGroup.put(dosFieldGroup.name, fieldGroupData);
                        if(dosFieldGroup._class.OUID == ouidLong)
                            fieldGroupMap.put(dosFieldGroup.name, dosFieldGroup);
                        fieldGroupData = null;
                        dosFieldGroup = null;
                    }

                    mapKey = null;
                }
                dosClass = null;
                if(superKey != null && superKey.hasNext())
                {
                    dosClass = (DOSClass)superKey.next();
                    tempLong = dosClass.OUID;
                    stack.addLast(dosClass);
                }
            } while(dosClass != null);
            if(stat != null)
            {
                stat.close();
                stat = null;
                con.commit();
                con.close();
                xc.close();
            }
            superKey = null;
            dosClass = (DOSClass)classMap.get(ouid);
            if(fieldGroupMap.size() > 0)
                dosClass.fieldGroupMap = fieldGroupMap;
            else
                dosClass.fieldGroupMap = null;
            fieldGroupData = null;
            fieldGroupMap = null;
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        if(inheritFieldGroup.size() > 0)
        {
            returnList = new ArrayList(inheritFieldGroup.size());
            Iterator fieldGroupKey;
            for(fieldGroupKey = inheritFieldGroup.keySet().iterator(); fieldGroupKey.hasNext(); returnList.add(inheritFieldGroup.get(fieldGroupKey.next())));
            fieldGroupKey = null;
        }
        inheritFieldGroup.clear();
        inheritFieldGroup = null;
        stack.clear();
        stack = null;
        ArrayList superList = null;
        return returnList;
    }

    public ArrayList listAssociationOfClass(String ouid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSAssociation dosAssociation = null;
        DOSClass dosClass = null;
        ArrayList returnList = null;
        ArrayList arrayList = null;
        LinkedList linkedList = null;
        LinkedList linkedList0 = null;
        LinkedList linkedList1 = null;
        LinkedList linkedList2 = null;
        Iterator listKey = null;
        HashMap checkDup = null;
        DOSChangeable associationData = null;
        long tempLong = 0L;
        long ouidLong = 0L;
        String tempString = null;
        int end = 0;
        int rows = 0;
        if(classMap.size() > 0)
        {
            returnList = new ArrayList();
            checkDup = new HashMap();
            dosClass = (DOSClass)classMap.get(ouid);
            if(dosClass != null && (dosClass.end0List != null || dosClass.end1List != null || dosClass.end2List != null))
            {
                for(int i = 0; i < 3; i++)
                {
                    switch(i)
                    {
                    case 0: // '\0'
                        if(dosClass.end0List != null && dosClass.end0List.size() > 0)
                            listKey = dosClass.end0List.iterator();
                        break;

                    case 1: // '\001'
                        if(dosClass.end1List != null && dosClass.end1List.size() > 0)
                            listKey = dosClass.end1List.iterator();
                        break;

                    case 2: // '\002'
                        if(dosClass.end2List != null && dosClass.end2List.size() > 0)
                            listKey = dosClass.end2List.iterator();
                        break;
                    }
                    if(listKey != null)
                    {
                        while(listKey.hasNext()) 
                        {
                            associationData = new DOSChangeable();
                            dosAssociation = (DOSAssociation)listKey.next();
                            tempString = Long.toHexString(dosAssociation.OUID);
                            associationData.put("ouid", tempString);
                            associationData.put("name", dosAssociation.name);
                            associationData.put("description", dosAssociation.description);
                            associationData.put("type", dosAssociation.type);
                            if(dosAssociation._class != null)
                                associationData.put("ouid@class", Long.toHexString(dosAssociation._class.OUID));
                            associationData.put("code", dosAssociation.code);
                            associationData.put("datasource.id", dosAssociation.datasourceId);
                            if(dosAssociation.end1_class != null)
                                associationData.put("end1.ouid@class", Long.toHexString(dosAssociation.end1_class.OUID));
                            associationData.put("end1.name", dosAssociation.end1_name);
                            associationData.put("end1.multiplicity.from", dosAssociation.end1_multiplicity_from);
                            associationData.put("end1.multiplicity.to", dosAssociation.end1_multiplicity_to);
                            associationData.put("end1.is.navigable", dosAssociation.end1_isNavigable);
                            associationData.put("end1.is.ordered", dosAssociation.end1_isOrdered);
                            linkedList = dosAssociation.end1_orderKeyField;
                            if(linkedList != null && linkedList.size() > 0)
                            {
                                arrayList = new ArrayList(linkedList.size());
                                for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                                associationData.put("end1.array$ouid@field", arrayList);
                                arrayList = null;
                                linkedList = null;
                            }
                            associationData.put("end1.changeable", dosAssociation.end1_changeable);
                            associationData.put("end1.cascade.release", dosAssociation.end1_cascadeRelease);
                            associationData.put("end1.cascade.clone", dosAssociation.end1_cascadeClone);
                            if(dosAssociation.end2_class != null)
                                associationData.put("end2.ouid@class", Long.toHexString(dosAssociation.end2_class.OUID));
                            associationData.put("end2.name", dosAssociation.end2_name);
                            associationData.put("end2.multiplicity.from", dosAssociation.end2_multiplicity_from);
                            associationData.put("end2.multiplicity.to", dosAssociation.end2_multiplicity_to);
                            associationData.put("end2.is.navigable", dosAssociation.end2_isNavigable);
                            associationData.put("end2.is.ordered", dosAssociation.end2_isOrdered);
                            linkedList = dosAssociation.end2_orderKeyField;
                            if(linkedList != null && linkedList.size() > 0)
                            {
                                arrayList = new ArrayList(linkedList.size());
                                for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                                associationData.put("end2.array$ouid@field", arrayList);
                                arrayList = null;
                                linkedList = null;
                            }
                            associationData.put("end2.changeable", dosAssociation.end2_changeable);
                            associationData.put("end2.cascade.release", dosAssociation.end2_cascadeRelease);
                            associationData.put("end2.cascade.clone", dosAssociation.end2_cascadeClone);
                            if(checkDup.get(tempString) == null)
                            {
                                returnList.add(associationData);
                                checkDup.put(tempString, tempString);
                            }
                            associationData = null;
                            dosAssociation = null;
                        }
                        listKey = null;
                    }
                }

                checkDup.clear();
                checkDup = null;
                return returnList;
            }
            associationData = null;
            dosAssociation = null;
        }
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select da.name as name, 0 as end, da.ouid as ouid from dosasso da where da.dosclas=? union all select da.name as name, 1 as end, da.ouid as ouid from dosasso da where da.clas$1=? union all select da.name as name, 2 as end, da.ouid as ouid from dosasso da where da.clas$2=? ");
            long tempLong = Utils.convertOuidToLong(ouid);
            stat.setLong(1, tempLong);
            stat.setLong(2, tempLong);
            stat.setLong(3, tempLong);
            ResultSet rs = stat.executeQuery();
            ArrayList returnList = new ArrayList();
            LinkedList linkedList = new LinkedList();
            LinkedList linkedList0 = new LinkedList();
            LinkedList linkedList1 = new LinkedList();
            LinkedList linkedList2 = new LinkedList();
            HashMap checkDup = new HashMap();
            DOSClass dosClass = (DOSClass)classMap.get(ouid);
            DOSChangeable associationData;
            while(rs.next()) 
            {
                int end = rs.getInt(2);
                DOSAssociation dosAssociation = (DOSAssociation)associationMap.get(Long.toHexString(rs.getLong(3)));
                if(dosAssociation != null)
                {
                    String tempString = Long.toHexString(dosAssociation.OUID);
                    associationData = (DOSChangeable)checkDup.get(tempString);
                    if(associationData == null)
                        associationData = new DOSChangeable();
                    associationData.put("ouid", tempString);
                    associationData.put("name", dosAssociation.name);
                    associationData.put("description", dosAssociation.description);
                    associationData.put("type", dosAssociation.type);
                    if(dosAssociation._class != null)
                        associationData.put("ouid@class", Long.toHexString(dosAssociation._class.OUID));
                    associationData.put("code", dosAssociation.code);
                    associationData.put("datasource.id", dosAssociation.datasourceId);
                    if(dosAssociation.end1_class != null)
                        associationData.put("end1.ouid@class", Long.toHexString(dosAssociation.end1_class.OUID));
                    associationData.put("end1.name", dosAssociation.end1_name);
                    associationData.put("end1.multiplicity.from", dosAssociation.end1_multiplicity_from);
                    associationData.put("end1.multiplicity.to", dosAssociation.end1_multiplicity_to);
                    associationData.put("end1.is.navigable", dosAssociation.end1_isNavigable);
                    associationData.put("end1.is.ordered", dosAssociation.end1_isOrdered);
                    linkedList = dosAssociation.end1_orderKeyField;
                    if(linkedList != null && linkedList.size() > 0)
                    {
                        ArrayList arrayList = new ArrayList(linkedList.size());
                        for(Iterator listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                        associationData.put("end1.array$ouid@field", arrayList);
                        arrayList = null;
                        linkedList = null;
                    }
                    associationData.put("end1.changeable", dosAssociation.end1_changeable);
                    associationData.put("end1.cascade.release", dosAssociation.end1_cascadeRelease);
                    associationData.put("end1.cascade.clone", dosAssociation.end1_cascadeClone);
                    if(dosAssociation.end2_class != null)
                        associationData.put("end2.ouid@class", Long.toHexString(dosAssociation.end2_class.OUID));
                    associationData.put("end2.name", dosAssociation.end2_name);
                    associationData.put("end2.multiplicity.from", dosAssociation.end2_multiplicity_from);
                    associationData.put("end2.multiplicity.to", dosAssociation.end2_multiplicity_to);
                    associationData.put("end2.is.navigable", dosAssociation.end2_isNavigable);
                    associationData.put("end2.is.ordered", dosAssociation.end2_isOrdered);
                    linkedList = dosAssociation.end2_orderKeyField;
                    if(linkedList != null && linkedList.size() > 0)
                    {
                        ArrayList arrayList = new ArrayList(linkedList.size());
                        for(Iterator listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                        associationData.put("end2.array$ouid@field", arrayList);
                        arrayList = null;
                        linkedList = null;
                    }
                    associationData.put("end2.changeable", dosAssociation.end2_changeable);
                    associationData.put("end2.cascade.release", dosAssociation.end2_cascadeRelease);
                    associationData.put("end2.cascade.clone", dosAssociation.end2_cascadeClone);
                    if(!checkDup.containsKey(tempString))
                    {
                        returnList.add(associationData);
                        checkDup.put(tempString, associationData);
                    }
                    switch(end)
                    {
                    default:
                        break;

                    case 0: // '\0'
                        if(!linkedList0.contains(dosAssociation))
                            linkedList0.add(dosAssociation);
                        break;

                    case 1: // '\001'
                        if(!linkedList1.contains(dosAssociation))
                            linkedList1.add(dosAssociation);
                        if(dosAssociation._class != null)
                            classOuidMap.put(dosAssociation.code.toLowerCase().replace(' ', '_'), Long.toHexString(dosAssociation._class.OUID));
                        break;

                    case 2: // '\002'
                        if(!linkedList2.contains(dosAssociation))
                            linkedList2.add(dosAssociation);
                        if(dosAssociation._class != null)
                            classOuidMap.put(dosAssociation.code.toLowerCase().replace(' ', '_'), Long.toHexString(dosAssociation._class.OUID));
                        break;
                    }
                    associationData = null;
                    dosAssociation = null;
                }
            }
            checkDup.clear();
            checkDup = null;
            associationData = null;
            if(linkedList0 != null && linkedList0.size() > 0)
                dosClass.end0List = linkedList0;
            if(linkedList1 != null && linkedList1.size() > 0)
                dosClass.end1List = linkedList1;
            if(linkedList2 != null && linkedList2.size() > 0)
                dosClass.end2List = linkedList2;
            linkedList0 = null;
            linkedList1 = null;
            linkedList2 = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public ArrayList listSuperClass(String ouid)
        throws IIPRequestException
    {
        DOSClass dosClass = (DOSClass)classMap.get(ouid);
        ArrayList returnList = null;
        LinkedList superList = null;
        DOSChangeable classData = null;
        long ouidLong = 0L;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        if(classMap.size() > 0 && dosClass != null)
        {
            superList = dosClass.superClassList;
            if(superList == null || superList.size() == 0)
                return null;
            returnList = new ArrayList(superList.size());
            DOSPackage dosPackage = null;
            Iterator mapKey;
            for(mapKey = superList.iterator(); mapKey.hasNext();)
            {
                classData = new DOSChangeable();
                dosClass = (DOSClass)mapKey.next();
                classData.put("ouid", Long.toHexString(dosClass.OUID));
                classData.put("name", dosClass.name);
                classData.put("description", dosClass.description);
                dosPackage = dosClass._package;
                if(dosPackage != null)
                    classData.put("ouid@package", Long.toHexString(dosPackage.OUID));
                classData.put("is.root", dosClass.isRoot);
                classData.put("is.leaf", dosClass.isLeaf);
                classData.put("is.versionable", dosClass.versionable);
                classData.put("is.filecontrol", dosClass.fileControl);
                classData.put("capacity", dosClass.capacity);
                classData.put("code", dosClass.code);
                classData.put("title", dosClass.title);
                classData.put("tooltip", dosClass.tooltip);
                classData.put("icon", dosClass.icon);
                classData.put("datasource.id", dosClass.datasourceId);
                classData.put("is.xa.object", dosClass.isXAClass);
                classData.put("is.association.class", dosClass.isAssociationClass);
                classData.put("use.unique.number", dosClass.useUniqueNumber);
                returnList.add(classData);
                classData = null;
                dosClass = null;
            }

            mapKey = null;
            classData = null;
            superList = null;
            return returnList;
        } else
        {
            return null;
        }
    }

    private ArrayList listAllSuperClassInternal(String ouid)
        throws IIPRequestException
    {
        DOSClass tempClass = null;
        ArrayList returnList = null;
        LinkedList stack = null;
        Iterator superKey = null;
        HashMap checkDup = null;
        long ouidLong = 0L;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        tempClass = (DOSClass)classMap.get(ouid);
        stack = new LinkedList();
        checkDup = new HashMap();
        returnList = new ArrayList();
        if(tempClass != null && tempClass.superClassList != null && tempClass.superClassList.size() > 0)
            while(tempClass != null) 
            {
                if(tempClass.superClassList == null)
                {
                    if(stack.isEmpty())
                        break;
                    tempClass = (DOSClass)stack.removeLast();
                    returnList.add(tempClass);
                    continue;
                }
                for(superKey = tempClass.superClassList.iterator(); superKey.hasNext();)
                {
                    tempClass = (DOSClass)superKey.next();
                    if(checkDup.containsKey(tempClass))
                    {
                        tempClass = null;
                    } else
                    {
                        stack.add(tempClass);
                        checkDup.put(tempClass, "");
                        tempClass = null;
                    }
                }

                if(stack.isEmpty())
                    break;
                tempClass = (DOSClass)stack.removeLast();
                returnList.add(tempClass);
            }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList == null || returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList listAllSuperClassOuid(String ouid)
        throws IIPRequestException
    {
        DOSClass tempClass = null;
        ArrayList returnList = null;
        LinkedList stack = null;
        Iterator superKey = null;
        HashMap checkDup = null;
        long ouidLong = 0L;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        tempClass = (DOSClass)classMap.get(ouid);
        stack = new LinkedList();
        checkDup = new HashMap();
        returnList = new ArrayList();
        if(tempClass != null && tempClass.superClassList != null && tempClass.superClassList.size() > 0)
            while(tempClass != null) 
            {
                if(tempClass.superClassList == null)
                {
                    if(stack.isEmpty())
                        break;
                    tempClass = (DOSClass)stack.removeLast();
                    returnList.add(Long.toHexString(tempClass.OUID));
                    continue;
                }
                for(superKey = tempClass.superClassList.iterator(); superKey.hasNext();)
                {
                    tempClass = (DOSClass)superKey.next();
                    if(checkDup.containsKey(tempClass))
                    {
                        tempClass = null;
                    } else
                    {
                        stack.add(tempClass);
                        checkDup.put(tempClass, "");
                        tempClass = null;
                    }
                }

                if(stack.isEmpty())
                    break;
                tempClass = (DOSClass)stack.removeLast();
                returnList.add(Long.toHexString(tempClass.OUID));
            }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList == null || returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList listSubClass(String ouid)
        throws IIPRequestException
    {
        DOSClass dosClass = null;
        ArrayList returnList = null;
        LinkedList subList = null;
        DOSChangeable classData = null;
        long ouidLong = 0L;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        dosClass = (DOSClass)classMap.get(ouid);
        if(classMap.size() > 0 && dosClass != null)
        {
            subList = dosClass.subClassList;
            if(subList == null || subList.size() == 0)
                return null;
            returnList = new ArrayList(subList.size());
            DOSPackage dosPackage = null;
            Iterator mapKey;
            for(mapKey = subList.iterator(); mapKey.hasNext();)
            {
                classData = new DOSChangeable();
                dosClass = (DOSClass)mapKey.next();
                classData.put("ouid", Long.toHexString(dosClass.OUID));
                classData.put("name", dosClass.name);
                classData.put("description", dosClass.description);
                dosPackage = dosClass._package;
                if(dosPackage != null)
                    classData.put("ouid@package", Long.toHexString(dosPackage.OUID));
                classData.put("is.root", dosClass.isRoot);
                classData.put("is.leaf", dosClass.isLeaf);
                classData.put("is.versionable", dosClass.versionable);
                classData.put("is.filecontrol", dosClass.fileControl);
                classData.put("capacity", dosClass.capacity);
                classData.put("code", dosClass.code);
                classData.put("title", dosClass.title);
                classData.put("tooltip", dosClass.tooltip);
                classData.put("icon", dosClass.icon);
                classData.put("datasource.id", dosClass.datasourceId);
                classData.put("is.xa.object", dosClass.isXAClass);
                classData.put("use.unique.number", dosClass.useUniqueNumber);
                returnList.add(classData);
                classData = null;
                dosClass = null;
            }

            mapKey = null;
            classData = null;
            subList = null;
            return returnList;
        } else
        {
            return null;
        }
    }

    public ArrayList listSubClassInModel(String modelOuid, String ouid)
        throws IIPRequestException
    {
        String modelName = null;
        DOSClass dosClass = null;
        ArrayList returnList = null;
        LinkedList subList = null;
        DOSChangeable classData = null;
        long ouidLong = 0L;
        int rows = 0;
        DOSPackage dosPackage = null;
        TreeMap modelMap = null;
        DOSModel dosModel = null;
        boolean inWorkingModel = false;
        boolean isLeaf = false;
        Iterator mapKey = null;
        Iterator modelKey = null;
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("Parameter value is null : modelOuid");
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        modelName = ((DOSModel)this.modelMap.get(modelOuid)).name;
        dosClass = (DOSClass)classMap.get(ouid);
        if(classMap.size() > 0 && dosClass != null)
        {
            subList = dosClass.subClassList;
            if(subList == null || subList.size() == 0)
                return null;
            returnList = new ArrayList(subList.size());
            for(mapKey = subList.iterator(); mapKey.hasNext();)
            {
                dosClass = (DOSClass)mapKey.next();
                dosPackage = dosClass._package;
                isLeaf = dosClass.isLeaf.booleanValue();
                modelMap = dosPackage.modelMap;
                if(modelMap.containsKey(modelName))
                    inWorkingModel = true;
                else
                    inWorkingModel = false;
                if(isLeaf && inWorkingModel || !isLeaf && dosClass.subClassList != null && dosClass.subClassList.size() != 0)
                {
                    classData = new DOSChangeable();
                    classData.put("ouid", Long.toHexString(dosClass.OUID));
                    classData.put("name", dosClass.name);
                    classData.put("description", dosClass.description);
                    dosPackage = dosClass._package;
                    if(dosPackage != null)
                        classData.put("ouid@package", Long.toHexString(dosPackage.OUID));
                    classData.put("is.root", dosClass.isRoot);
                    classData.put("is.leaf", dosClass.isLeaf);
                    classData.put("is.versionable", dosClass.versionable);
                    classData.put("is.filecontrol", dosClass.fileControl);
                    classData.put("capacity", dosClass.capacity);
                    classData.put("code", dosClass.code);
                    classData.put("title", dosClass.title);
                    classData.put("tooltip", dosClass.tooltip);
                    classData.put("icon", dosClass.icon);
                    classData.put("datasource.id", dosClass.datasourceId);
                    classData.put("is.xa.object", dosClass.isXAClass);
                    classData.put("use.unique.number", dosClass.useUniqueNumber);
                    returnList.add(classData);
                    classData = null;
                    dosClass = null;
                }
            }

            mapKey = null;
            classData = null;
            subList = null;
            return returnList;
        } else
        {
            return null;
        }
    }

    public ArrayList listAllSubClassOuid(String ouid)
        throws IIPRequestException
    {
        DOSClass tempClass = null;
        ArrayList returnList = null;
        LinkedList stack = null;
        Iterator subKey = null;
        HashMap checkDup = null;
        long ouidLong = 0L;
        String tempString = null;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        tempClass = (DOSClass)classMap.get(ouid);
        if(tempClass == null)
            throw new IIPRequestException("Class not found");
        stack = new LinkedList();
        checkDup = new HashMap();
        returnList = new ArrayList();
        if(tempClass.subClassList != null && tempClass.subClassList.size() > 0)
            while(tempClass != null) 
            {
                if(tempClass.subClassList == null)
                {
                    if(stack.isEmpty())
                        break;
                    tempClass = (DOSClass)stack.removeLast();
                    tempString = Long.toHexString(tempClass.OUID);
                    if(!returnList.contains(tempString))
                        returnList.add(Long.toHexString(tempClass.OUID));
                    continue;
                }
                for(subKey = tempClass.subClassList.iterator(); subKey.hasNext();)
                {
                    tempClass = (DOSClass)subKey.next();
                    if(checkDup.containsKey(tempClass))
                    {
                        tempClass = null;
                    } else
                    {
                        stack.add(tempClass);
                        checkDup.put(tempClass, "");
                        tempClass = null;
                    }
                }

                if(stack.isEmpty())
                    break;
                tempClass = (DOSClass)stack.removeLast();
                tempString = Long.toHexString(tempClass.OUID);
                if(!returnList.contains(tempString))
                    returnList.add(Long.toHexString(tempClass.OUID));
            }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList == null || returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList listAllLeafClassOuid(String ouid)
        throws IIPRequestException
    {
        DOSClass tempClass = null;
        ArrayList returnList = null;
        LinkedList stack = null;
        Iterator subKey = null;
        HashMap checkDup = null;
        long ouidLong = 0L;
        int rows = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        tempClass = (DOSClass)classMap.get(ouid);
        if(tempClass == null)
            throw new IIPRequestException("Class not found");
        stack = new LinkedList();
        checkDup = new HashMap();
        returnList = new ArrayList();
        if(tempClass.subClassList != null && tempClass.subClassList.size() > 0)
            while(tempClass != null) 
            {
                if(tempClass.subClassList == null)
                {
                    if(stack.isEmpty())
                        break;
                    tempClass = (DOSClass)stack.removeLast();
                    if(Utils.getBoolean(tempClass.isLeaf))
                        returnList.add(Long.toHexString(tempClass.OUID));
                    continue;
                }
                for(subKey = tempClass.subClassList.iterator(); subKey.hasNext();)
                {
                    tempClass = (DOSClass)subKey.next();
                    if(checkDup.containsKey(tempClass))
                    {
                        tempClass = null;
                    } else
                    {
                        stack.add(tempClass);
                        checkDup.put(tempClass, "");
                        tempClass = null;
                    }
                }

                if(stack.isEmpty())
                    break;
                tempClass = (DOSClass)stack.removeLast();
                if(Utils.getBoolean(tempClass.isLeaf))
                    returnList.add(Long.toHexString(tempClass.OUID));
            }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList == null || returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList listAllLeafClassOuidInModel(String modelOuid, String ouid)
        throws IIPRequestException
    {
        String modelName = null;
        DOSClass tempClass = null;
        ArrayList returnList = null;
        LinkedList stack = null;
        Iterator subKey = null;
        HashMap checkDup = null;
        long ouidLong = 0L;
        int rows = 0;
        boolean inWorkingModel = false;
        DOSPackage dosPackage = null;
        TreeMap modelMap = null;
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("Parameter value is null : modelOuid");
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Parameter value is null : ouid");
        tempClass = (DOSClass)classMap.get(ouid);
        if(tempClass == null)
            throw new IIPRequestException("Class not found");
        modelName = ((DOSModel)this.modelMap.get(modelOuid)).name;
        stack = new LinkedList();
        checkDup = new HashMap();
        returnList = new ArrayList();
        if(tempClass.subClassList != null && tempClass.subClassList.size() > 0)
            while(tempClass != null) 
            {
                if(tempClass.subClassList == null)
                {
                    if(stack.isEmpty())
                        break;
                    tempClass = (DOSClass)stack.removeLast();
                    dosPackage = tempClass._package;
                    modelMap = dosPackage.modelMap;
                    if(modelMap.containsKey(modelName))
                        inWorkingModel = true;
                    else
                        inWorkingModel = false;
                    if(Utils.getBoolean(tempClass.isLeaf) && inWorkingModel)
                        returnList.add(Long.toHexString(tempClass.OUID));
                    continue;
                }
                for(subKey = tempClass.subClassList.iterator(); subKey.hasNext();)
                {
                    tempClass = (DOSClass)subKey.next();
                    if(checkDup.containsKey(tempClass))
                    {
                        tempClass = null;
                    } else
                    {
                        stack.add(tempClass);
                        checkDup.put(tempClass, "");
                        tempClass = null;
                    }
                }

                if(stack.isEmpty())
                    break;
                tempClass = (DOSClass)stack.removeLast();
                dosPackage = tempClass._package;
                modelMap = dosPackage.modelMap;
                if(modelMap.containsKey(modelName))
                    inWorkingModel = true;
                else
                    inWorkingModel = false;
                if(Utils.getBoolean(tempClass.isLeaf) && inWorkingModel)
                    returnList.add(Long.toHexString(tempClass.OUID));
            }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList == null || returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList listAllLeafClassOuidInModel(String ouid)
        throws IIPRequestException
    {
        String modelOuid = null;
        HashMap context = getClientContext();
        if(context == null)
            return null;
        modelOuid = (String)context.get("working.model");
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("First, Set working model.");
        else
            return listAllLeafClassOuidInModel(modelOuid, ouid);
    }

    public ArrayList listClass()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ResultSet rs;
        DOSClass dosClass;
        ArrayList returnList;
        DOSChangeable classData;
        xc = null;
        con = null;
        stat = null;
        rs = null;
        dosClass = null;
        DOSClass superClass = null;
        DOSClass end0Class = null;
        DOSPackage dosPackage = null;
        Iterator listKey = null;
        Iterator end0Key = null;
        returnList = null;
        ArrayList arrayList = null;
        LinkedList linkedList = null;
        LinkedList subList = null;
        LinkedList end0List = null;
        classData = null;
        long ouidLong = 0L;
        long tempLong = 0L;
        String ouid = null;
        String tempString = null;
        int rows = 0;
        if(classMap.size() > 0)
        {
            returnList = new ArrayList();
            dosPackage = null;
            Iterator mapKey;
            for(mapKey = classMap.keySet().iterator(); mapKey.hasNext();)
            {
                classData = new DOSChangeable();
                dosClass = (DOSClass)classMap.get(mapKey.next());
                classData.put("ouid", Long.toHexString(dosClass.OUID));
                classData.put("name", dosClass.name);
                classData.put("description", dosClass.description);
                dosPackage = dosClass._package;
                if(dosPackage != null)
                    classData.put("ouid@package", Long.toHexString(dosPackage.OUID));
                if(dosClass.superClassList != null && dosClass.superClassList.size() > 0)
                {
                    arrayList = new ArrayList(dosClass.superClassList.size());
                    for(listKey = dosClass.superClassList.iterator(); listKey.hasNext();)
                    {
                        superClass = (DOSClass)listKey.next();
                        arrayList.add(Long.toHexString(superClass.OUID));
                        superClass = null;
                    }

                    classData.put("array$ouid.superclass", arrayList);
                    listKey = null;
                    arrayList = null;
                }
                classData.put("is.root", dosClass.isRoot);
                classData.put("is.leaf", dosClass.isLeaf);
                classData.put("is.versionable", dosClass.versionable);
                classData.put("is.filecontrol", dosClass.fileControl);
                classData.put("capacity", dosClass.capacity);
                classData.put("code", dosClass.code);
                classData.put("title", dosClass.title);
                classData.put("tooltip", dosClass.tooltip);
                classData.put("icon", dosClass.icon);
                classData.put("datasource.id", dosClass.datasourceId);
                classData.put("is.xa.object", dosClass.isXAClass);
                classData.put("is.association.class", dosClass.isAssociationClass);
                classData.put("is.abstract", dosClass.isAbstract);
                classData.put("use.unique.number", dosClass.useUniqueNumber);
                returnList.add(classData);
                classOuidMap.put(dosClass.code.toLowerCase().replace(' ', '_'), Long.toHexString(dosClass.OUID));
                classData = null;
                dosClass = null;
                dosPackage = null;
            }

            mapKey = null;
            classData = null;
            return returnList;
        }
        xc = dtm.getPooledConnection("system");
        con = xc.getConnection();
        stat = con.prepareStatement("select dc.ouid, dc.name, dc.des, dc.dospkg, dc.root, dc.leaf, dc.ver, dc.capa, dc.code, dc.tit, dc.tooltip, dc.icon, dc.dsid, dc.xaobj, dc.filecntl, i.dosclas, dc.abst, dc.uniqnum from dosclas dc, (select dl.dosclas as dosclas from dosasso dl where dl.dosclas is not null group by dl.dosclas ) i where dc.ouid=i.dosclas(+) order by dc.name ");
        rs = stat.executeQuery();
        returnList = new ArrayList();
          goto _L1
_L14:
        classData = new DOSChangeable();
        long ouidLong = rs.getLong(1);
        String ouid = Long.toHexString(ouidLong);
        classData.put("ouid", ouid);
        classData.put("name", rs.getString(2));
        classData.put("description", rs.getString(3));
        classData.put("ouid@package", Long.toHexString(rs.getLong(4)));
        classData.put("is.root", Utils.getBoolean(rs, 5));
        classData.put("is.leaf", Utils.getBoolean(rs, 6));
        classData.put("is.versionable", Utils.getBoolean(rs, 7));
        classData.put("capacity", Utils.getInteger(rs, 8));
        classData.put("code", rs.getString(9));
        classData.put("title", rs.getString(10));
        classData.put("tooltip", rs.getString(11));
        classData.put("icon", rs.getString(12));
        classData.put("datasource.id", rs.getString(13));
        classData.put("is.xa.object", Utils.getBoolean(rs, 14));
        classData.put("is.filecontrol", Utils.getBoolean(rs, 15));
        long tempLong = rs.getLong(16);
        if(rs.wasNull())
            classData.put("is.association.class", Utils.False);
        else
            classData.put("is.association.class", Utils.True);
        classData.put("is.abstract", Utils.getBoolean(rs, 17));
        classData.put("use.unique.number", Utils.getBoolean(rs, 18));
        returnList.add(classData);
        dosClass = new DOSClass();
        dosClass.OUID = ouidLong;
        dosClass.name = (String)classData.get("name");
        dosClass.description = (String)classData.get("description");
        dosClass._package = (DOSPackage)packageMap.get((String)classData.get("ouid@package"));
        dosClass.superClassList = (LinkedList)classData.get("array$ouid.superclass");
        dosClass.isRoot = (Boolean)classData.get("is.root");
        dosClass.isLeaf = (Boolean)classData.get("is.leaf");
        dosClass.versionable = (Boolean)classData.get("is.versionable");
        dosClass.fileControl = (Boolean)classData.get("is.filecontrol");
        dosClass.capacity = (Integer)classData.get("capacity");
        dosClass.code = (String)classData.get("code");
        dosClass.title = (String)classData.get("title");
        dosClass.tooltip = (String)classData.get("tooltip");
        dosClass.icon = (String)classData.get("icon");
        dosClass.datasourceId = (String)classData.get("datasource.id");
        dosClass.isXAClass = (Boolean)classData.get("is.xa.object");
        dosClass.isAssociationClass = (Boolean)classData.get("is.association.class");
        dosClass.isAbstract = (Boolean)classData.get("is.abstract");
        dosClass.useUniqueNumber = (Boolean)classData.get("use.unique.number");
        classMap.put(ouid, dosClass);
        if(Utils.isNullString(dosClass.code))
            dosClass.code = new String(dosClass.name);
        classOuidMap.put(dosClass.code.toLowerCase().replace(' ', '_'), ouid);
        if(!foundationPackageOuid.equals(classData.get("ouid@package"))) goto _L3; else goto _L2
_L2:
        if(fobject != null) goto _L5; else goto _L4
_L4:
        getClass();
        if(!"FObject".equals(dosClass.name)) goto _L5; else goto _L6
_L6:
        fobject = dosClass;
        fobjectOuid = (String)classData.get("ouid");
          goto _L3
_L5:
        if(fversionobject != null) goto _L8; else goto _L7
_L7:
        getClass();
        if(!"FVersionObject".equals(dosClass.name)) goto _L8; else goto _L9
_L9:
        fversionobject = dosClass;
        fversionobjectOuid = (String)classData.get("ouid");
          goto _L3
_L8:
        if(ffilecontrol != null) goto _L11; else goto _L10
_L10:
        getClass();
        if(!"FFileControl".equals(dosClass.name)) goto _L11; else goto _L12
_L12:
        ffilecontrol = dosClass;
        ffilecontrolOuid = (String)classData.get("ouid");
          goto _L3
_L11:
        if(fcadfile == null)
        {
            getClass();
            if("FCADFile".equals(dosClass.name))
            {
                fcadfile = dosClass;
                fcadfileOuid = (String)classData.get("ouid");
            }
        }
_L3:
        dosClass = null;
        classData = null;
_L1:
        if(rs.next()) goto _L14; else goto _L13
_L13:
        ArrayList arraylist;
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        if(returnList != null && returnList.size() > 0)
        {
            stat = con.prepareStatement("select dsc.supclas from dossuperclas dsc where dsc.dosclas=? order by dsc.dosclas,dsc.seq ");
            Iterator listKey;
            for(listKey = returnList.iterator(); listKey.hasNext();)
            {
                classData = (DOSChangeable)listKey.next();
                String ouid = (String)classData.get("ouid");
                if(Utils.isNullString(ouid))
                {
                    classData = null;
                } else
                {
                    long ouidLong = Utils.convertOuidToLong(ouid);
                    stat.setLong(1, ouidLong);
                    ArrayList arrayList = new ArrayList();
                    LinkedList linkedList = new LinkedList();
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        ouidLong = rs.getLong(1);
                        if(ouidLong != 0L)
                        {
                            String tempString = Long.toHexString(ouidLong);
                            arrayList.add(tempString);
                            DOSClass superClass = (DOSClass)classMap.get(tempString);
                            if(superClass != null)
                            {
                                linkedList.add(superClass);
                                LinkedList subList;
                                if(superClass.subClassList == null)
                                {
                                    subList = new LinkedList();
                                    superClass.subClassList = subList;
                                } else
                                {
                                    subList = superClass.subClassList;
                                }
                                dosClass = (DOSClass)classMap.get(ouid);
                                subList.add(dosClass);
                                dosClass = null;
                            }
                        }
                    }

                    if(linkedList.size() > 0)
                    {
                        classData.put("array$ouid.superclass", arrayList);
                        dosClass = (DOSClass)classMap.get(ouid);
                        if(dosClass != null)
                            dosClass.superClassList = linkedList;
                    }
                    linkedList = null;
                    arrayList = null;
                    classData = null;
                }
            }

            listKey = null;
            stat.close();
            stat = null;
        }
        con.commit();
        con.close();
        xc.close();
        arraylist = returnList;
        return arraylist;
        SQLException e;
        e;
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.fillInStackTrace().toString());
        }
          goto _L15
        local;
        DTMUtil.closeSafely(stat, con, xc);
        stat = null;
        con = null;
        xc = null;
        JVM INSTR ret 25;
_L15:
        return null;
    }

    public String createField(DOSChangeable fieldDefinition)
        throws IIPRequestException
    {
        String result = DOSField.createField(fieldDefinition, dtm, foundationPackage, classMap, codeMap, fieldMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public void removeField(String ouid)
        throws IIPRequestException
    {
        DOSField.removeField(ouid, dtm, fieldMap);
        updateTimeToken();
    }

    public DOSChangeable getField(String ouid)
        throws IIPRequestException
    {
        return DOSField.getField(ouid, fieldMap);
    }

    public DOSChangeable getFieldWithName(String classOuid, String fieldName)
        throws IIPRequestException
    {
        Iterator listKey = null;
        DOSChangeable returnObject = null;
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Parameter value is null : classOuid");
        if(Utils.isNullString(fieldName))
            throw new IIPRequestException("Parameter value is null : fieldName");
        ArrayList fieldList = listFieldInClassInternal(classOuid);
        if(fieldList == null)
            return null;
        for(listKey = fieldList.iterator(); listKey.hasNext();)
        {
            DOSField fieldData = (DOSField)listKey.next();
            if(fieldData.name.equals(fieldName))
            {
                returnObject = new DOSChangeable();
                returnObject.put("ouid", Long.toHexString(fieldData.OUID));
                returnObject.put("name", fieldData.name);
                returnObject.put("description", fieldData.description);
                if(fieldData._class != null)
                    returnObject.put("ouid@class", Long.toHexString(fieldData._class.OUID));
                returnObject.put("type", fieldData.type);
                if(fieldData.typeClass != null)
                    returnObject.put("type.ouid@class", Long.toHexString(fieldData.typeClass.OUID));
                if(fieldData.typeCode != null)
                    returnObject.put("type.ouid@class", Long.toHexString(fieldData.typeCode.OUID));
                if(fieldData.referencedFieldOuid != 0L)
                    returnObject.put("type.ouid@class", Long.toHexString(fieldData.referencedFieldOuid));
                returnObject.put("multiplicity.from", fieldData.multiplicity_from);
                returnObject.put("multiplicity.to", fieldData.multiplicity_to);
                returnObject.put("changeable", fieldData.changeable);
                returnObject.put("scope", fieldData.scope);
                returnObject.put("initial.value", fieldData.initialValue);
                returnObject.put("index", fieldData.index);
                returnObject.put("code", fieldData.code);
                returnObject.put("is.mandatory", fieldData.isMandatory);
                returnObject.put("is.visible", fieldData.isVisible);
                returnObject.put("size", fieldData.size);
                returnObject.put("title", fieldData.title);
                returnObject.put("tooltip", fieldData.tooltip);
                returnObject.put("width", fieldData.width);
                returnObject.put("height", fieldData.height);
                returnObject.put("title.width", fieldData.titleWidth);
                returnObject.put("is.title.visible", fieldData.isTitleVisible);
                returnObject.put("icon", fieldData.icon);
                returnObject.put("column", fieldData.column);
            }
            fieldData = null;
        }

        return returnObject;
    }

    public void setField(DOSChangeable fieldDefinition)
        throws IIPRequestException
    {
        DOSField.setField(fieldDefinition, dtm, foundationPackage, classMap, codeMap, fieldMap);
        updateTimeToken();
    }

    public String getClassOuidOfField(String ouid)
        throws IIPRequestException
    {
        return DOSField.getClassOuidOfField(ouid, dtm, fieldMap);
    }

    public ArrayList listField()
        throws IIPRequestException
    {
        return DOSField.listField(dtm, classMap, codeMap, fieldMap);
    }

    public void setCodeSelectionTable(String fieldOuid, ArrayList selectionTable)
        throws IIPRequestException
    {
        DOSField.setCodeSelectionTable(fieldOuid, selectionTable, fieldMap, nds);
    }

    public ArrayList getCodeSelectionTable(String fieldOuid)
        throws IIPRequestException
    {
        return DOSField.getCodeSelectionTable(fieldOuid, fieldMap, nds);
    }

    public boolean isComboBoxMatrixCode(String fieldOuid)
        throws IIPRequestException
    {
        return DOSField.isComboBoxMatrixCode(nds, fieldOuid);
    }

    public String lookupCodeFromSelectionTable(String fieldOuid, DOSChangeable instance)
        throws IIPRequestException
    {
        return DOSField.lookupCodeFromSelectionTable(fieldOuid, instance, DOSField.getCodeSelectionTable(fieldOuid, fieldMap, nds), fieldMap, codeMap, codeItemMap);
    }

    public String lookupCodeFromSelectionTable(String fieldOuid, String relatedFieldOuid, DOSChangeable instance)
        throws IIPRequestException
    {
        return DOSField.lookupCodeFromSelectionTable(fieldOuid, relatedFieldOuid, instance, DOSField.getCodeSelectionTable(fieldOuid, fieldMap, nds), fieldMap, codeMap, codeItemMap);
    }

    public String lookupInitialCodeFromSelectionTable(String fieldOuid, DOSChangeable instance)
        throws IIPRequestException
    {
        return DOSField.lookupInitialCodeFromSelectionTable(fieldOuid, instance, DOSField.getCodeSelectionTable(fieldOuid, fieldMap, nds), fieldMap, codeMap, codeItemMap);
    }

    public String createAction(DOSChangeable actionDefinition)
        throws IIPRequestException
    {
        String result = DOSAction.createAction(actionDefinition, dtm, classMap, fieldMap, actionMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public void removeAction(String ouid)
        throws IIPRequestException
    {
        DOSAction.removeAction(ouid, dtm, nds, actionMap);
        updateTimeToken();
    }

    public boolean removeActionScript(String ouid)
        throws IIPRequestException
    {
        boolean result = DOSAction.removeActionScript(ouid, nds);
        updateTimeToken();
        return result;
    }

    public DOSChangeable getAction(String ouid)
        throws IIPRequestException
    {
        return DOSAction.getAction(ouid, actionMap);
    }

    public void setAction(DOSChangeable actionDefinition)
        throws IIPRequestException
    {
        DOSAction.setAction(actionDefinition, dtm, classMap, fieldMap, actionMap);
        updateTimeToken();
    }

    public String getClassOuidOfAction(String ouid)
        throws IIPRequestException
    {
        return DOSAction.getClassOuidOfAction(ouid, dtm, actionMap);
    }

    public ArrayList listAction()
        throws IIPRequestException
    {
        return DOSAction.listAction(dtm, classMap, fieldMap, actionMap);
    }

    public void createActionParameter(DOSChangeable doschangeable)
        throws IIPRequestException
    {
    }

    public void removeActionParameter(String s, String s1)
        throws IIPRequestException
    {
    }

    public DOSChangeable getActionParameter(String ouid, String name)
        throws IIPRequestException
    {
        return null;
    }

    public void setActionParameter(DOSChangeable doschangeable)
        throws IIPRequestException
    {
    }

    public ArrayList listActionParameterInClass(String ouid)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList listActionParameter()
        throws IIPRequestException
    {
        return null;
    }

    public String getActionScriptName(String ouid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        else
            return nds.getValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
    }

    public boolean setActionScript(String ouid, String scriptName)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        if(Utils.isNullString(scriptName))
            throw new IIPRequestException("Miss out mandatory parameter: scriptName");
        boolean result = nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT", ouid, "DOS.ACTION", scriptName);
        if(!result)
            result = nds.setValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid, scriptName);
        if(result)
        {
            List tmpList = Utils.tokenizeMessage(scriptName, '.');
            String extType = "";
            if(((String)tmpList.get(1)).equals("py"))
                extType = "Python";
            else
            if(((String)tmpList.get(1)).equals("tcl"))
                extType = "Tcl";
            else
            if(((String)tmpList.get(1)).equals("java"))
                extType = "Beanshell";
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT", extType, "DOS.SCRIPT", extType);
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType, (String)tmpList.get(0), "DOS.SCRIPT", (String)tmpList.get(0));
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0), "ACTION", "DOS.SCRIPT", "ACTION");
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/" + "ACTION", ouid, "DOS.SCRIPT", ouid);
        }
        updateTimeToken();
        return result;
    }

    public ArrayList listObjectUseScript(String extType, String scriptName)
        throws IIPRequestException
    {
        if(Utils.isNullString(extType))
            throw new IIPRequestException("Miss out mandatory parameter: extType");
        if(Utils.isNullString(scriptName))
            throw new IIPRequestException("Miss out mandatory parameter: scriptName");
        DOSChangeable tempMap = null;
        ArrayList resultList = new ArrayList();
        ArrayList classificationList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName);
        if(classificationList != null)
        {
            for(int i = 0; i < classificationList.size(); i++)
                if(((String)classificationList.get(i)).equals("EVENT"))
                {
                    ArrayList eventOuidList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName + "/" + "EVENT");
                    if(eventOuidList != null)
                    {
                        for(int j = 0; j < eventOuidList.size(); j++)
                        {
                            ArrayList eventTypeList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName + "/" + "EVENT" + "/" + (String)eventOuidList.get(j));
                            if(eventTypeList != null)
                            {
                                for(int k = 0; k < eventTypeList.size(); k++)
                                {
                                    tempMap = new DOSChangeable();
                                    tempMap.put("classification", "EVENT");
                                    tempMap.put("ouid", eventOuidList.get(j));
                                    tempMap.put("event.type", eventTypeList.get(k));
                                    resultList.add(tempMap);
                                    tempMap = null;
                                }

                            }
                        }

                    }
                } else
                if(((String)classificationList.get(i)).equals("ACTION"))
                {
                    ArrayList actionOuidList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName + "/" + "ACTION");
                    if(actionOuidList != null)
                    {
                        for(int j = 0; j < actionOuidList.size(); j++)
                        {
                            tempMap = new DOSChangeable();
                            tempMap.put("classification", "ACTION");
                            tempMap.put("ouid", actionOuidList.get(j));
                            tempMap.put("event.type", "");
                            resultList.add(tempMap);
                            tempMap = null;
                        }

                    }
                }

        }
        return resultList;
    }

    public boolean removeScript(String extType, String scriptName)
        throws IIPRequestException
    {
        boolean result = false;
        ArrayList classificationList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName);
        if(classificationList != null)
        {
            for(int i = 0; i < classificationList.size(); i++)
                if(((String)classificationList.get(i)).equals("EVENT"))
                {
                    ArrayList eventOuidList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName + "/" + "EVENT");
                    if(eventOuidList != null)
                    {
                        for(int j = 0; j < eventOuidList.size(); j++)
                        {
                            ArrayList eventTypeList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName + "/" + "EVENT" + "/" + (String)eventOuidList.get(j));
                            if(eventTypeList != null)
                            {
                                for(int k = 0; k < eventTypeList.size(); k++)
                                {
                                    nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + (String)eventOuidList.get(j) + "/" + (String)eventTypeList.get(k));
                                    ArrayList list = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + (String)eventOuidList.get(j));
                                    if(Utils.isNullArrayList(list))
                                        nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + (String)eventOuidList.get(j));
                                }

                            }
                        }

                    }
                } else
                if(((String)classificationList.get(i)).equals("ACTION"))
                {
                    ArrayList actionOuidList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName + "/" + "ACTION");
                    if(actionOuidList != null)
                    {
                        for(int j = 0; j < actionOuidList.size(); j++)
                            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + (String)actionOuidList.get(j));

                    }
                }

        }
        result = nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + scriptName);
        if(result)
            if(extType.equals("Python"))
                result = dss.deleteFile("/script/" + scriptName + "." + "py");
            else
            if(extType.equals("Tcl"))
                result = dss.deleteFile("/script/" + scriptName + "." + "tcl");
            else
            if(extType.equals("Beanshell"))
                result = dss.deleteFile("/script/" + scriptName + "." + "java");
        updateTimeToken();
        return result;
    }

    public String createFieldGroup(DOSChangeable fieldGroupDefinition)
        throws IIPRequestException
    {
        String result = DOSFieldGroup.createFieldGroup(fieldGroupDefinition, dtm, classMap, fieldMap, actionMap, fieldGroupMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public void removeFieldGroup(String ouid)
        throws IIPRequestException
    {
        DOSFieldGroup.removeFieldGroup(ouid, dtm, nds, fieldGroupMap);
        updateTimeToken();
    }

    public DOSChangeable getFieldGroup(String ouid)
        throws IIPRequestException
    {
        return DOSFieldGroup.getFieldGroup(ouid, fieldGroupMap);
    }

    public DOSChangeable getFieldGroupWithName(String classOuid, String fieldGroupName)
        throws IIPRequestException
    {
        Iterator listKey = null;
        DOSChangeable returnData = null;
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Parameter value is null : classOuid");
        if(Utils.isNullString(fieldGroupName))
            throw new IIPRequestException("Parameter value is null : fieldGroupName");
        ArrayList fieldGroupList = listFieldGroupInClass(classOuid);
        if(fieldGroupList == null)
            return null;
        for(listKey = fieldGroupList.iterator(); listKey.hasNext();)
        {
            DOSChangeable fieldGroupData = (DOSChangeable)listKey.next();
            if(fieldGroupData.get("name").equals(fieldGroupName))
                returnData = fieldGroupData;
        }

        return returnData;
    }

    public void setFieldGroup(DOSChangeable fieldGroupDefinition)
        throws IIPRequestException
    {
        DOSFieldGroup.setFieldGroup(fieldGroupDefinition, dtm, nds, classMap, fieldMap, actionMap, fieldGroupMap);
        updateTimeToken();
    }

    public ArrayList listFieldInFieldGroup(String ouid)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList listActionInFieldGroup(String ouid)
        throws IIPRequestException
    {
        return null;
    }

    public ArrayList listFieldGroup()
        throws IIPRequestException
    {
        return DOSFieldGroup.listFieldGroup(dtm, classMap, fieldMap, actionMap, fieldGroupMap);
    }

    public String createAssociation(DOSChangeable associationDefinition)
        throws IIPRequestException
    {
        String result = DOSAssociation.createAssociation(associationDefinition, dtm, classMap, fieldMap, associationMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public void removeAssociation(String ouid)
        throws IIPRequestException
    {
        DOSAssociation.removeAssociation(ouid, dtm, associationMap);
        updateTimeToken();
    }

    public DOSChangeable getAssociation(String ouid)
        throws IIPRequestException
    {
        return DOSAssociation.getAssociation(ouid, associationMap);
    }

    public void setAssociation(DOSChangeable associationDefinition)
        throws IIPRequestException
    {
        DOSAssociation.setAssociation(associationDefinition, dtm, classMap, fieldMap, associationMap);
        updateTimeToken();
    }

    public DOSChangeable getAssociationEnd(String ouid, int end)
        throws IIPRequestException
    {
        return null;
    }

    public void setAssociationEnd(DOSChangeable doschangeable, int i)
        throws IIPRequestException
    {
    }

    public String getAssociationEndClass(String ouid, int end)
        throws IIPRequestException
    {
        return null;
    }

    public void setAssociationEndClass(String s, int i, String s1)
        throws IIPRequestException
    {
    }

    public ArrayList listAssociation()
        throws IIPRequestException
    {
        return DOSAssociation.listAssociation(dtm, classMap, fieldMap, associationMap);
    }

    private void initialAssociationMapping()
    {
        if(classMap == null || associationMap == null)
            return;
        String ouid = null;
        Object object = null;
        try
        {
            Iterator classKey;
            for(classKey = classMap.keySet().iterator(); classKey.hasNext();)
            {
                ouid = (String)classKey.next();
                object = listFieldInClassInternal(ouid);
                object = null;
                ouid = null;
            }

            classKey = null;
            System.out.print('.');
            for(classKey = classMap.keySet().iterator(); classKey.hasNext();)
            {
                ouid = (String)classKey.next();
                object = listFieldGroupInClass(ouid);
                object = null;
                ouid = null;
            }

            classKey = null;
            System.out.print('.');
            for(classKey = classMap.keySet().iterator(); classKey.hasNext();)
            {
                ouid = (String)classKey.next();
                object = listAssociationOfClass(ouid);
                object = null;
                ouid = null;
            }

            classKey = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public String add(DOSChangeable object)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap context;
        ArrayList fieldList;
        ArrayList collectionList;
        String classCode;
        String instanceOuid;
        String end1;
        String end2;
        String classOuid;
        String datasourceId;
        String searchedOuid2;
        String searchedOuid3;
        String number;
        StringBuffer sb1;
        StringBuffer sb2;
        long longTemp1;
        boolean versionable;
        boolean isAssociationClass;
        boolean hasCollection;
        boolean isVersionAddCase;
        xc = null;
        con = null;
        stat = null;
        context = null;
        DOSClass dosClass = null;
        DOSClass tempClass = null;
        DOSField dosField = null;
        fieldList = null;
        collectionList = null;
        Iterator fieldKey = null;
        classCode = null;
        String fieldCode = null;
        instanceOuid = null;
        String returnString = null;
        String stringTemp0 = null;
        String stringTemp1 = null;
        String stringTemp2 = null;
        end1 = null;
        end2 = null;
        classOuid = null;
        datasourceId = null;
        String objectOuid = null;
        searchedOuid2 = null;
        searchedOuid3 = null;
        number = null;
        sb1 = null;
        sb2 = null;
        StringBuffer sb3 = null;
        StringBuffer sb4 = null;
        Object tempObject = null;
        long longTemp0 = 0L;
        longTemp1 = 0L;
        long longTemp2 = 0L;
        int rows = 0;
        int fieldCount = 0;
        int multiplicity_to = 0;
        int i = 0;
        byte typeCode = 0;
        versionable = false;
        isAssociationClass = false;
        hasCollection = false;
        isVersionAddCase = false;
        String initValue = null;
        if(object == null)
            throw new IIPRequestException("Null object.");
        context = getClientContext();
        if(context == null)
            throw new IIPRequestException("Internal error: Client context not exists.");
        classOuid = object.getClassOuid();
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        instanceOuid = (String)object.get("ouid");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists.");
        HashMap valueMap = object.getValueMap();
        if(valueMap == null)
            throw new IIPRequestException("Null value.");
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        if(isAssociationClass)
            versionable = false;
        else
            versionable = Utils.getBoolean(dosClass.versionable);
        fieldList = listFieldInClassInternal(classOuid);
        fieldCount = fieldList.size();
        fieldKey = fieldList.iterator();
        while(fieldKey.hasNext()) 
        {
            dosField = (DOSField)fieldKey.next();
            if(Utils.getBoolean(dosField.isMandatory))
            {
                tempObject = valueMap.get(dosField.name);
                if(tempObject == null)
                {
                    dosClass = null;
                    valueMap = null;
                    fieldKey = null;
                    tempObject = null;
                    throw new IIPRequestException("Miss out manatory field: " + dosField.name);
                }
                if((tempObject instanceof String) && ((String)tempObject).equals(""))
                {
                    dosClass = null;
                    valueMap = null;
                    fieldKey = null;
                    tempObject = null;
                    throw new IIPRequestException("Miss out manatory field: " + dosField.name);
                }
                tempObject = null;
            }
            if(Utils.getDouble(dosField.size) > 0.0D)
            {
                tempObject = valueMap.get(dosField.name);
                if(tempObject == null)
                {
                    tempObject = null;
                    continue;
                }
                if((tempObject instanceof String) && !Utils.isNullString((String)tempObject) && !Utils.checkStringSize((String)tempObject, Utils.getDouble(dosField.size), Utils.getByte(dosField.type)))
                {
                    dosClass = null;
                    valueMap = null;
                    fieldKey = null;
                    tempObject = null;
                    throw new IIPRequestException("Exceeded the field size: " + dosField.name);
                }
                tempObject = null;
            }
            dosField = null;
        }
        fieldKey = null;
        if(isAssociationClass)
            number = (String)object.get("md$sequence");
        else
            number = (String)object.get("md$number");
        if(!isAssociationClass)
        {
            classCode = dosClass.code;
            classCode = classCode.toLowerCase().replace(' ', '_');
            datasourceId = dosClass.datasourceId;
        } else
        {
            classCode = (String)object.get("as$code");
            end1 = (String)object.get("as$end1");
            end2 = (String)object.get("as$end2");
            datasourceId = (String)object.get("as$datasourceId");
        }
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        if(versionable)
        {
            sb1 = new StringBuffer();
            String tempVersion = (String)object.get("vf$version");
            String tempVersion2 = null;
            String searchedOuid = null;
            if(!Utils.isNullString(number) && Utils.isNullString(instanceOuid))
            {
                if(checkDuplicateNumber(classOuid, number, "wip") != null)
                {
                    sb1 = null;
                    throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B406", "Number Duplicated."));
                }
                searchedOuid = checkDuplicateNumber(classOuid, number, tempVersion);
            }
            if("wip".equals(tempVersion) || !Utils.isNullString(instanceOuid))
                tempVersion = null;
            if(Utils.isNullString(searchedOuid))
            {
                if(Utils.isNullString(instanceOuid) && Utils.isNullString(tempVersion))
                {
                    sb1.append("insert into ");
                    sb1.append(classCode);
                    sb1.append("$id (id$ouid,id$last,id$wip) values (?,?,?)");
                } else
                if(!Utils.isNullString(number) && Utils.isNullString(tempVersion))
                {
                    sb1.append("update ");
                    sb1.append(classCode);
                    sb1.append("$id ");
                    sb1.append("set id$last=id$wip,id$wip=? where id$ouid=?");
                } else
                if(!Utils.isNullString(number) && !Utils.isNullString(tempVersion))
                {
                    if(versionStrings == null)
                        getVersionStringArray();
                    if(versionStrings != null)
                    {
                        sb1.append("select b.vf$ouid,b.vf$version,b.vf$identity from ");
                        sb1.append(classCode);
                        sb1.append("$id a, ");
                        sb1.append(classCode);
                        sb1.append("$vf b where b.md$number=? and a.id$last=b.vf$ouid");
                        ResultSet rx = null;
                        try
                        {
                            xc = dtm.getPooledConnection(datasourceId);
                            con = xc.getConnection();
                            stat = con.prepareStatement(sb1.toString());
                            stat.setString(1, number);
                            rx = stat.executeQuery();
                            if(rx.next())
                            {
                                searchedOuid2 = rx.getString(1);
                                tempVersion2 = rx.getString(2);
                                searchedOuid3 = rx.getString(3);
                            }
                            rx.close();
                            rx = null;
                            stat.close();
                            stat = null;
                            con.close();
                            con = null;
                            xc.close();
                            xc = null;
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            if(rx != null)
                                try
                                {
                                    rx.close();
                                    rx = null;
                                }
                                catch(Exception exception) { }
                            DTMUtil.closeSafely(stat, con, xc);
                            stat = null;
                            con = null;
                            xc = null;
                            throw new IIPRequestException(e.toString());
                        }
                        sb1.setLength(0);
                        isVersionAddCase = true;
                        int n2 = -1;
                        for(int t = versionStrings.length - 1; t >= 0; t--)
                        {
                            if(tempVersion.equals(versionStrings[t]))
                            {
                                if(n2 == -1)
                                {
                                    sb1.append("update ");
                                    sb1.append(classCode);
                                    sb1.append("$id ");
                                    sb1.append("set id$last=? where id$last=?");
                                } else
                                {
                                    sb1 = null;
                                }
                                break;
                            }
                            if(tempVersion2 != null && tempVersion2.equals(versionStrings[t]))
                                n2 = t;
                        }

                        if(sb1 != null && sb1.length() == 0)
                        {
                            sb1 = null;
                            throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B406", "Number Duplicated."));
                        }
                    }
                }
            } else
            if(!Utils.isNullString(tempVersion))
            {
                sb1.setLength(0);
                sb1 = null;
                throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B406", "Number Duplicated."));
            }
            sb2 = new StringBuffer();
            sb3 = new StringBuffer();
            sb2.append("insert into ");
            sb2.append(classCode);
            sb2.append("$vf (vf$ouid,vf$identity");
            sb3.append(" values (?,?");
            for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
            {
                dosField = (DOSField)fieldKey.next();
                multiplicity_to = Utils.getInt(dosField.multiplicity_to);
                if(multiplicity_to > 1)
                {
                    hasCollection = true;
                    if(collectionList == null)
                        collectionList = new ArrayList();
                    collectionList.add(dosField);
                    dosField = null;
                } else
                {
                    fieldCode = dosField.code.toLowerCase().replace(' ', '_');
                    if(reservedFields.contains(fieldCode) && !fieldCode.equals("md$number") && !fieldCode.equals("md$desc") && !fieldCode.equals("vf$version") && !fieldCode.equals("md$status") && !fieldCode.equals("md$user"))
                    {
                        if(fieldCode.equals("md$cdate") || fieldCode.equals("md$mdate"))
                        {
                            sb2.append(',');
                            sb2.append(fieldCode);
                            sb3.append(",to_char(sysdate,'YYYYMMDDHH24MISS')");
                            dosField = null;
                            fieldCode = null;
                        } else
                        {
                            dosField = null;
                            fieldCode = null;
                        }
                    } else
                    {
                        sb2.append(',');
                        sb2.append(fieldCode);
                        typeCode = dosField.type.byteValue();
                        if(typeCode == 21)
                            sb3.append(",to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS')");
                        else
                        if(typeCode == 22)
                            sb3.append(",to_char(to_date(?,'YYYY-MM-DD'),'YYYYMMDD')");
                        else
                        if(typeCode == 23)
                            sb3.append(",to_char(to_date(?,'HH24:MI:SS'), 'HH24MISS')");
                        else
                            sb3.append(",?");
                        dosField = null;
                    }
                }
            }

            fieldKey = null;
            sb2.append(')');
            sb2.append(sb3.toString());
            sb3.setLength(0);
            sb3 = null;
            sb2.append(')');
        } else
        {
            sb2 = new StringBuffer();
            sb3 = new StringBuffer();
            sb2.append("insert into ");
            sb2.append(classCode);
            if(isAssociationClass)
            {
                sb2.append("$ac (sf$ouid,as$end1,as$end2");
                sb3.append(" values (?,?,?");
            } else
            {
                sb2.append("$sf (sf$ouid");
                sb3.append(" values (?");
            }
            for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
            {
                dosField = (DOSField)fieldKey.next();
                multiplicity_to = Utils.getInt(dosField.multiplicity_to);
                if(multiplicity_to > 1)
                {
                    hasCollection = true;
                    if(collectionList == null)
                        collectionList = new ArrayList();
                    collectionList.add(dosField);
                    dosField = null;
                } else
                {
                    fieldCode = dosField.code.toLowerCase().replace(' ', '_');
                    if(reservedFields.contains(fieldCode) && !fieldCode.equals("md$number") && !fieldCode.equals("md$sequence") && !fieldCode.equals("md$desc") && !fieldCode.equals("md$status") && !fieldCode.equals("md$user"))
                    {
                        if(fieldCode.equals("md$cdate") || fieldCode.equals("md$mdate"))
                        {
                            sb2.append(',');
                            sb2.append(fieldCode);
                            sb3.append(",to_char(sysdate,'YYYYMMDDHH24MISS')");
                            dosField = null;
                            fieldCode = null;
                        } else
                        {
                            dosField = null;
                            fieldCode = null;
                        }
                    } else
                    {
                        sb2.append(',');
                        sb2.append(fieldCode);
                        typeCode = dosField.type.byteValue();
                        if(typeCode == 21)
                            sb3.append(",to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS')");
                        else
                        if(typeCode == 22)
                            sb3.append(",to_char(to_date(?,'YYYY-MM-DD'),'YYYYMMDD')");
                        else
                        if(typeCode == 23)
                            sb3.append(",to_char(to_date(?,'HH24:MI:SS'),'HH24MISS')");
                        else
                            sb3.append(",?");
                        dosField = null;
                    }
                }
            }

            fieldKey = null;
            sb2.append(')');
            sb3.append(')');
            sb2.append(sb3.toString());
            sb3.setLength(0);
            sb3 = null;
        }
        SQLException e;
        String s;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            if(versionable && Utils.isNullString(instanceOuid))
                if(!isVersionAddCase)
                {
                    longTemp1 = generateOUID(con);
                    String stringTemp1 = Long.toHexString(longTemp1);
                } else
                if(!Utils.isNullString(searchedOuid3))
                {
                    longTemp1 = Utils.getLong(new Long(searchedOuid3));
                    String stringTemp1 = Long.toHexString(longTemp1);
                }
            long longTemp0 = generateOUID(con);
            String stringTemp0 = Long.toHexString(longTemp0);
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            String returnString;
            if(!versionable)
            {
                if(isAssociationClass)
                    returnString = classCode + "$ac@" + stringTemp0;
                else
                    returnString = classCode + "$sf@" + stringTemp0;
                stat = con.prepareStatement(sb2.toString());
                int i;
                if(isAssociationClass)
                {
                    stat.setLong(1, longTemp0);
                    stat.setLong(2, Utils.convertOuidToLong(end1));
                    stat.setLong(3, Utils.convertOuidToLong(end2));
                    i = 3;
                } else
                {
                    stat.setLong(1, longTemp0);
                    i = 1;
                }
                for(Iterator fieldKey = fieldList.iterator(); fieldKey.hasNext();)
                {
                    DOSField dosField = (DOSField)fieldKey.next();
                    int multiplicity_to = Utils.getInt(dosField.multiplicity_to);
                    if(multiplicity_to > 1)
                    {
                        dosField = null;
                    } else
                    {
                        String fieldCode = dosField.code.toLowerCase().replace(' ', '_');
                        i++;
                        if(reservedFields.contains(fieldCode))
                        {
                            if(fieldCode.equals("md$number") || fieldCode.equals("md$sequence"))
                                stat.setString(i, generateNumber(classOuid, object, number));
                            else
                            if(fieldCode.equals("md$desc"))
                                stat.setString(i, (String)object.get(dosField.name));
                            else
                            if(fieldCode.equals("md$status"))
                                stat.setString(i, STATUS_CRT);
                            else
                            if(fieldCode.equals("md$user"))
                                stat.setString(i, (String)context.get("userId"));
                            else
                            if(fieldCode.equals("md$cdate") || fieldCode.equals("md$mdate"))
                                i--;
                        } else
                        {
                            Object tempObject = object.get(dosField.name);
                            byte typeCode = dosField.type.byteValue();
                            String initValue = (String)dosField.initialValue;
                            if(typeCode == 16)
                            {
                                if(tempObject == null)
                                {
                                    if(!Utils.isNullString(initValue))
                                        stat.setString(i, initValue);
                                    else
                                        stat.setString(i, null);
                                } else
                                {
                                    stat.setString(i, (String)tempObject);
                                }
                            } else
                            if(typeCode == 24 || typeCode == 25)
                            {
                                if(tempObject == null)
                                {
                                    if(!Utils.isNullString(initValue))
                                    {
                                        String objectOuid = initValue;
                                        stat.setLong(i, Utils.convertOuidToLong(objectOuid));
                                    } else
                                    {
                                        stat.setString(i, null);
                                    }
                                } else
                                {
                                    String objectOuid = (String)tempObject;
                                    stat.setLong(i, Utils.convertOuidToLong(objectOuid));
                                }
                            } else
                            if(typeCode == 1)
                            {
                                if(tempObject == null)
                                {
                                    if(!Utils.isNullString(initValue))
                                        Utils.setBoolean(stat, i, new Boolean(initValue));
                                    else
                                        Utils.setBoolean(stat, i, null);
                                } else
                                {
                                    Utils.setBoolean(stat, i, (Boolean)tempObject);
                                }
                            } else
                            if(typeCode == 2)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setByte(stat, i, Byte.valueOf(initValue));
                                        else
                                            Utils.setByte(stat, i, null);
                                    } else
                                    {
                                        Utils.setByte(stat, i, Byte.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Byte)
                                    Utils.setByte(stat, i, (Byte)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setByte(stat, i, Byte.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 4)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setDouble(stat, i, Double.valueOf(initValue));
                                        else
                                            Utils.setDouble(stat, i, null);
                                    } else
                                    {
                                        Utils.setDouble(stat, i, Double.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Double)
                                    Utils.setDouble(stat, i, (Double)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setDouble(stat, i, Double.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 5)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setFloat(stat, i, Float.valueOf(initValue));
                                        else
                                            Utils.setFloat(stat, i, null);
                                    } else
                                    {
                                        Utils.setFloat(stat, i, Float.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Float)
                                    Utils.setFloat(stat, i, (Float)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setFloat(stat, i, Float.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 6)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setInt(stat, i, Integer.valueOf(initValue));
                                        else
                                            Utils.setInt(stat, i, null);
                                    } else
                                    {
                                        Utils.setInt(stat, i, Integer.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Integer)
                                    Utils.setInt(stat, i, (Integer)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setInt(stat, i, Integer.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 7)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setLong(stat, i, Long.valueOf(initValue));
                                        else
                                            Utils.setLong(stat, i, null);
                                    } else
                                    {
                                        Utils.setLong(stat, i, Long.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Long)
                                    Utils.setLong(stat, i, (Long)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setLong(stat, i, Long.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 8)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setShort(stat, i, Short.valueOf(initValue));
                                        else
                                            Utils.setShort(stat, i, null);
                                    } else
                                    {
                                        Utils.setShort(stat, i, Short.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Short)
                                    Utils.setShort(stat, i, (Short)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setShort(stat, i, Short.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(Utils.isNullString((String)tempObject))
                            {
                                if(!Utils.isNullString(initValue))
                                    stat.setString(i, initValue);
                                else
                                    stat.setString(i, null);
                            } else
                            {
                                stat.setString(i, (String)tempObject);
                            }
                            tempObject = null;
                        }
                    }
                }

                int rows = stat.executeUpdate();
                stat.close();
                stat = null;
            } else
            {
                returnString = classCode + "$vf@" + stringTemp0;
                if(Utils.isNullString(instanceOuid) && !isVersionAddCase)
                {
                    stat = con.prepareStatement(sb1.toString());
                    stat.setLong(1, longTemp1);
                    stat.setLong(2, longTemp0);
                    stat.setLong(3, longTemp0);
                    stat.executeUpdate();
                    stat.close();
                    stat = null;
                } else
                if(!isVersionAddCase)
                {
                    stat = con.prepareStatement("select id$ouid from " + classCode + "$id where id$wip=?");
                    stat.setLong(1, Utils.getRealLongOuid(instanceOuid));
                    ResultSet rs = stat.executeQuery();
                    if(rs.next())
                    {
                        longTemp1 = rs.getLong(1);
                        String stringTemp1 = Long.toHexString(longTemp1);
                    } else
                    {
                        rs.close();
                        rs = null;
                        stat.close();
                        stat = null;
                        throw new IIPRequestException("Can not found a appropriate object instance.");
                    }
                    rs.close();
                    rs = null;
                    stat.close();
                    stat = null;
                }
                stat = con.prepareStatement(sb2.toString());
                stat.setLong(1, longTemp0);
                stat.setLong(2, longTemp1);
                int i = 2;
                for(Iterator fieldKey = fieldList.iterator(); fieldKey.hasNext();)
                {
                    DOSField dosField = (DOSField)fieldKey.next();
                    int multiplicity_to = Utils.getInt(dosField.multiplicity_to);
                    if(multiplicity_to > 1)
                    {
                        dosField = null;
                    } else
                    {
                        String fieldCode = dosField.code.toLowerCase().replace(' ', '_');
                        i++;
                        if(reservedFields.contains(fieldCode))
                        {
                            if(fieldCode.equals("vf$version"))
                            {
                                if(Utils.isNullString(instanceOuid))
                                {
                                    if(!Utils.isNullString((String)object.get("vf$version")) && !"wip".equals(object.get("vf$version")))
                                        stat.setString(i, (String)object.get("vf$version"));
                                    else
                                        stat.setString(i, generateVersion(con, classCode, instanceOuid));
                                } else
                                {
                                    stat.setString(i, "wip");
                                }
                            } else
                            if(fieldCode.equals("md$number"))
                            {
                                if(Utils.isNullString(instanceOuid) && !isVersionAddCase)
                                    stat.setString(i, generateNumber(classOuid, object, number));
                                else
                                    stat.setString(i, number);
                            } else
                            if(fieldCode.equals("md$desc"))
                                stat.setString(i, (String)object.get(dosField.name));
                            else
                            if(fieldCode.equals("md$status"))
                            {
                                if(Utils.isNullString(instanceOuid))
                                    stat.setString(i, STATUS_CRT);
                                else
                                    stat.setString(i, STATUS_WIP);
                            } else
                            if(fieldCode.equals("md$user"))
                                stat.setString(i, (String)context.get("userId"));
                            else
                            if(fieldCode.equals("md$cdate") || fieldCode.equals("md$mdate"))
                                i--;
                        } else
                        {
                            Object tempObject = object.get(dosField.name);
                            byte typeCode = dosField.type.byteValue();
                            String initValue = (String)dosField.initialValue;
                            if(typeCode == 16)
                            {
                                if(tempObject == null)
                                {
                                    if(!Utils.isNullString(initValue))
                                        stat.setString(i, initValue);
                                    else
                                        stat.setString(i, null);
                                } else
                                {
                                    stat.setString(i, (String)tempObject);
                                }
                            } else
                            if(typeCode == 24 || typeCode == 25)
                            {
                                if(tempObject == null)
                                {
                                    if(!Utils.isNullString(initValue))
                                    {
                                        String objectOuid = initValue;
                                        stat.setLong(i, Utils.convertOuidToLong(objectOuid));
                                    } else
                                    {
                                        stat.setString(i, null);
                                    }
                                } else
                                {
                                    String objectOuid = (String)tempObject;
                                    stat.setLong(i, Utils.convertOuidToLong(objectOuid));
                                }
                            } else
                            if(typeCode == 1)
                            {
                                if(tempObject == null)
                                {
                                    if(!Utils.isNullString(initValue))
                                        Utils.setBoolean(stat, i, new Boolean(initValue));
                                    else
                                        Utils.setBoolean(stat, i, null);
                                } else
                                {
                                    Utils.setBoolean(stat, i, (Boolean)tempObject);
                                }
                            } else
                            if(typeCode == 2)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setByte(stat, i, Byte.valueOf(initValue));
                                        else
                                            Utils.setByte(stat, i, null);
                                    } else
                                    {
                                        Utils.setByte(stat, i, Byte.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Byte)
                                    Utils.setByte(stat, i, (Byte)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setByte(stat, i, Byte.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 4)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setDouble(stat, i, Double.valueOf(initValue));
                                        else
                                            Utils.setDouble(stat, i, null);
                                    } else
                                    {
                                        Utils.setDouble(stat, i, Double.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Double)
                                    Utils.setDouble(stat, i, (Double)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setDouble(stat, i, Double.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 5)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setFloat(stat, i, Float.valueOf(initValue));
                                        else
                                            Utils.setFloat(stat, i, null);
                                    } else
                                    {
                                        Utils.setFloat(stat, i, Float.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Float)
                                    Utils.setFloat(stat, i, (Float)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setFloat(stat, i, Float.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 6)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setInt(stat, i, Integer.valueOf(initValue));
                                        else
                                            Utils.setInt(stat, i, null);
                                    } else
                                    {
                                        Utils.setInt(stat, i, Integer.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Integer)
                                    Utils.setInt(stat, i, (Integer)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setInt(stat, i, Integer.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 7)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setLong(stat, i, Long.valueOf(initValue));
                                        else
                                            Utils.setLong(stat, i, null);
                                    } else
                                    {
                                        Utils.setLong(stat, i, Long.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Long)
                                    Utils.setLong(stat, i, (Long)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setLong(stat, i, Long.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(typeCode == 8)
                            {
                                if(tempObject instanceof String)
                                {
                                    if(Utils.isNullString((String)tempObject))
                                    {
                                        if(!Utils.isNullString(initValue))
                                            Utils.setShort(stat, i, Short.valueOf(initValue));
                                        else
                                            Utils.setShort(stat, i, null);
                                    } else
                                    {
                                        Utils.setShort(stat, i, Short.valueOf((String)tempObject));
                                    }
                                } else
                                if(tempObject instanceof Short)
                                    Utils.setShort(stat, i, (Short)tempObject);
                                else
                                if(tempObject == null)
                                    if(!Utils.isNullString(initValue))
                                        Utils.setShort(stat, i, Short.valueOf(initValue));
                                    else
                                        stat.setString(i, null);
                            } else
                            if(Utils.isNullString((String)tempObject))
                            {
                                if(!Utils.isNullString(initValue))
                                    stat.setString(i, initValue);
                                else
                                    stat.setString(i, null);
                            } else
                            {
                                stat.setString(i, (String)tempObject);
                            }
                            tempObject = null;
                        }
                    }
                }

                int rows = stat.executeUpdate();
                stat.close();
                stat = null;
                if(!Utils.isNullString(instanceOuid) && !isVersionAddCase)
                {
                    stat = con.prepareStatement(sb1.toString());
                    stat.setLong(1, longTemp0);
                    stat.setLong(2, longTemp1);
                    stat.executeUpdate();
                    stat.close();
                    stat = null;
                } else
                if(isVersionAddCase && sb1 != null)
                {
                    stat = con.prepareStatement(sb1.toString());
                    stat.setLong(1, longTemp0);
                    stat.setLong(2, Utils.getLong(new Long(searchedOuid2)));
                    stat.executeUpdate();
                    stat.close();
                    stat = null;
                }
            }
            if(hasCollection && !Utils.isNullArrayList(collectionList))
            {
                stat = con.prepareStatement("delete from " + classCode + "$cf " + "where md$ouid=?");
                stat.setLong(1, longTemp0);
                stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("insert into " + classCode + "$cf (md$ouid,md$code,md$key,md$value)" + "values (?,?,?,?)");
                stat.setLong(1, longTemp0);
                int n = 0;
                byte type = 0;
                Object aValue = null;
                String collOuid = null;
                ArrayList arrayList = null;
                ArrayList aRow = null;
                Iterator listKey = null;
                Iterator collKey;
                for(collKey = collectionList.iterator(); collKey.hasNext();)
                {
                    DOSField dosField = (DOSField)collKey.next();
                    arrayList = (ArrayList)object.get(dosField.name);
                    if(Utils.isNullArrayList(arrayList))
                    {
                        arrayList = null;
                        dosField = null;
                    } else
                    {
                        type = dosField.type.byteValue();
                        stat.setString(2, dosField.code.toLowerCase().replace(' ', '_'));
                        n = 0;
                        for(listKey = arrayList.iterator(); listKey.hasNext(); stat.executeUpdate())
                        {
                            aValue = listKey.next();
                            n++;
                            stat.setLong(3, n);
                            if(aValue == null)
                                stat.setString(4, null);
                            else
                            if(type == 24 || type == 25)
                            {
                                aRow = (ArrayList)aValue;
                                collOuid = (String)aRow.get(0);
                                stat.setLong(4, Utils.getRealLongOuid(collOuid));
                                aRow = null;
                            } else
                            if(type == 16)
                            {
                                aRow = (ArrayList)aValue;
                                collOuid = (String)aRow.get(0);
                                stat.setString(4, collOuid);
                                aRow = null;
                            } else
                            {
                                stat.setString(4, aValue.toString());
                            }
                            aValue = null;
                        }

                        listKey = null;
                        arrayList = null;
                        dosField = null;
                    }
                }

                collKey = null;
                stat.close();
                stat = null;
            }
            con.commit();
            con.close();
            xc.close();
            HashMap valueMap = null;
            s = returnString;
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

    public void remove(String ouid)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        PreparedStatement stat2 = null;
        ResultSet rs2 = null;
        HashMap context = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList fieldList = null;
        Iterator fieldKey = null;
        String classCode = null;
        String classOuid = null;
        String tempString = null;
        String datasourceId = null;
        String objectOuid = null;
        StringBuffer sb = null;
        StringBuffer sb2 = null;
        StringBuffer sb3 = null;
        StringBuffer sb4 = null;
        Object tempObject = null;
        long ouidLong = 0L;
        long idOuidLong = 0L;
        int rows = 0;
        int fieldCount = 0;
        int multiplicity_to = 0;
        int i = 0;
        byte changeable = 0;
        byte typeCode = 0;
        boolean versionable = false;
        boolean isAssociationClass = false;
        boolean hasCollection = false;
        Utils.checkMandatoryString(ouid, "ouid");
        synchronized(instanceCache)
        {
            if(instanceCache.containsKey(ouid))
            {
                instanceCacheTag.remove(ouid);
                instanceCache.remove(ouid);
            }
        }
        context = getClientContext();
        if(context == null)
            throw new IIPRequestException("Internal error: Client context not exists.");
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        fieldList = listFieldInClassInternal(classOuid);
        fieldCount = fieldList.size();
        for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
        {
            dosField = (DOSField)fieldKey.next();
            multiplicity_to = Utils.getInt(dosField.multiplicity_to);
            if(multiplicity_to > 1)
            {
                dosField = null;
                hasCollection = true;
            } else
            {
                dosField = null;
            }
        }

        fieldKey = null;
        classCode = dosClass.code;
        classCode = classCode.toLowerCase().replace(' ', '_');
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        sb = new StringBuffer();
        sb.append("delete from ");
        if(!isAssociationClass)
        {
            sb.append(classCode);
            if(hasCollection)
            {
                sb2 = new StringBuffer(sb.toString());
                sb2.append("$cf");
            }
            if(versionable)
                sb.append("$vf");
            else
                sb.append("$sf");
        }
        sb.append(" where ");
        if(versionable)
            sb.append("vf$ouid=?");
        else
            sb.append("sf$ouid=?");
        if(hasCollection)
            sb2.append(" where md$ouid=?");
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouid = ouid.substring(ouid.indexOf('@') + 1);
            ouidLong = Utils.convertOuidToLong(ouid);
            if(versionable)
            {
                sb3 = new StringBuffer();
                sb3.append("select vf$identity from ");
                sb3.append(classCode);
                sb3.append("$vf where vf$ouid = ?");
                stat = con.prepareStatement(sb3.toString());
                stat.setLong(1, ouidLong);
                rs = stat.executeQuery();
                if(rs.next())
                    idOuidLong = rs.getLong(1);
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                sb3.setLength(0);
                sb3 = null;
            }
            stat = con.prepareStatement(sb.toString());
            stat.setLong(1, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            sb.setLength(0);
            sb = null;
            if(hasCollection)
            {
                stat = con.prepareStatement(sb2.toString());
                stat.setLong(1, ouidLong);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
                sb2.setLength(0);
                sb2 = null;
            }
            if(versionable)
            {
                sb3 = new StringBuffer();
                sb3.append("select vf$ouid from ");
                sb3.append(classCode);
                sb3.append("$vf where vf$identity = ?");
                stat = con.prepareStatement(sb3.toString());
                stat.setLong(1, idOuidLong);
                rs = stat.executeQuery();
                if(rs.next())
                {
                    sb4 = new StringBuffer();
                    sb4.append("update ");
                    sb4.append(classCode);
                    sb4.append("$id set id$wip = id$last where id$ouid = ?");
                    stat2 = con.prepareStatement(sb4.toString());
                    stat2.setLong(1, idOuidLong);
                    rows = stat2.executeUpdate();
                    stat2.close();
                    stat2 = null;
                    sb4.setLength(0);
                    sb4 = null;
                } else
                {
                    sb4 = new StringBuffer();
                    sb4.append("delete from ");
                    sb4.append(classCode);
                    sb4.append("$id where id$ouid = ?");
                    stat2 = con.prepareStatement(sb4.toString());
                    stat2.setLong(1, idOuidLong);
                    rows = stat2.executeUpdate();
                    stat2.close();
                    stat2 = null;
                    sb4.setLength(0);
                    sb4 = null;
                }
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                sb3.setLength(0);
                sb3 = null;
            }
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
            DTMUtil.closeStatementSafely(stat2);
            stat2 = null;
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return;
    }

    public DOSChangeable get(String ouid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        PooledConnection xc = instanceCache;
        JVM INSTR monitorenter ;
        DOSChangeable result;
        if(!instanceCache.containsKey(ouid))
            break MISSING_BLOCK_LABEL_63;
        result = (DOSChangeable)instanceCache.get(ouid);
        if(result != null && result.getValueMap().size() > 0)
            return result;
        xc;
        JVM INSTR monitorexit ;
          goto _L1
        xc;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        Connection con;
        PreparedStatement stat;
        ArrayList fieldList;
        ArrayList collectionList;
        DOSChangeable result;
        StringBuffer sb;
        StringBuffer sb2;
        String datasourceId;
        byte typeArray[];
        long realOuid;
        boolean versionable;
        String classOuid;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        fieldList = null;
        collectionList = null;
        ArrayList collectionResults = null;
        HashMap userData = null;
        Iterator mapKey = null;
        Iterator fieldKey = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        result = null;
        DOSChangeable tempObject = null;
        sb = null;
        sb2 = null;
        String classCode = null;
        datasourceId = null;
        String userId = null;
        typeArray = (byte[])null;
        int i = 0;
        int j = 0;
        int fieldCount = 0;
        int multiplicityFrom = 0;
        int multiplicityTo = 0;
        byte typeCode = 0;
        realOuid = 0L;
        versionable = false;
        boolean isAssociationClass = false;
        boolean hasCollection = false;
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid:" + ouid + ")");
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        fieldList = listFieldInClassInternal(classOuid);
        if(fieldList == null || fieldList.size() == 0)
            throw new IIPRequestException("No fields.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code.toLowerCase().replace(' ', '_');
        sb = new StringBuffer();
        sb.append("select ");
        if(versionable)
        {
            sb.append("vf$identity");
            i = 2;
        } else
        {
            i = 1;
        }
        fieldCount = fieldList.size();
        typeArray = new byte[fieldCount + i + 1];
        for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
        {
            dosField = (DOSField)fieldKey.next();
            if(dosField != null)
            {
                multiplicityFrom = Utils.getInt(dosField.multiplicity_from);
                multiplicityTo = Utils.getInt(dosField.multiplicity_to);
                if(multiplicityFrom < multiplicityTo)
                {
                    if(multiplicityTo > 1)
                        hasCollection = true;
                    else
                        hasCollection = multiplicityTo - multiplicityFrom > 1;
                } else
                {
                    hasCollection = false;
                }
                if(hasCollection)
                {
                    typeArray[i] = -13;
                } else
                {
                    if(i > 1)
                        sb.append(',');
                    typeArray[i] = Utils.getByte(dosField.type);
                }
                if(typeArray[i] == 21)
                {
                    sb.append("to_char(to_date(");
                    sb.append(dosField.code.toLowerCase().replace(' ', '_'));
                    sb.append(",'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS')");
                } else
                if(typeArray[i] == 22)
                {
                    sb.append("to_char(to_date(");
                    sb.append(dosField.code.toLowerCase().replace(' ', '_'));
                    sb.append(",'YYYYMMDD'),'YYYY-MM-DD')");
                } else
                if(typeArray[i] == 23)
                {
                    sb.append("to_char(to_date(");
                    sb.append(dosField.code.toLowerCase().replace(' ', '_'));
                    sb.append(",'HH24MISS'),'HH24:MI:SS')");
                } else
                if(typeArray[i] == -13)
                {
                    if(collectionList == null)
                        collectionList = new ArrayList();
                    collectionList.add(dosField);
                } else
                {
                    sb.append(dosField.code.toLowerCase().replace(' ', '_'));
                }
                dosField = null;
                i++;
            }
        }

        fieldKey = null;
        sb.append(" from ");
        if(collectionList != null && collectionList.size() > 0)
        {
            sb2 = new StringBuffer("select md$value from ");
            sb2.append(classCode);
            sb2.append("$cf where md$ouid=");
        }
        if(isAssociationClass)
        {
            sb.append(ouid.substring(0, ouid.indexOf('$')));
            sb.append("$ac where sf$ouid=");
        } else
        {
            sb.append(classCode);
            if(versionable)
                sb.append("$vf where vf$ouid=");
            else
                sb.append("$sf where sf$ouid=");
        }
        realOuid = Utils.getRealLongOuid(ouid);
        sb.append(realOuid);
        if(collectionList != null && collectionList.size() > 0)
        {
            sb2.append(realOuid);
            sb2.append(" and md$code=? order by md$key");
        }
        SQLException e;
        DOSChangeable doschangeable;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            stat = con.prepareStatement(sb.toString());
            ResultSet rs = stat.executeQuery();
            if(rs.next())
            {
                result = new DOSChangeable();
                result.setClassOuid(classOuid);
                result.setOuid(Long.toHexString(realOuid));
                result.put("ouid", ouid);
                int i;
                if(versionable)
                    i = 2;
                else
                    i = 1;
                int j = i;
                Iterator fieldKey;
                for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
                {
                    DOSField dosField = (DOSField)fieldKey.next();
                    if(typeArray[j] == -13)
                    {
                        j++;
                    } else
                    {
                        addListFromResultSet(result, dosField.name, rs, i, typeArray[j]);
                        i++;
                        j++;
                    }
                    dosField = null;
                }

                fieldKey = null;
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                sb.setLength(0);
                sb = null;
                if(collectionList != null)
                {
                    stat = con.prepareStatement(sb2.toString());
                    ArrayList tempList = null;
                    DOSChangeable codeItem = null;
                    for(Iterator collectionKey = collectionList.iterator(); collectionKey.hasNext();)
                    {
                        DOSField dosField = (DOSField)collectionKey.next();
                        stat.setString(1, dosField.code.toLowerCase().replace(' ', '_'));
                        ArrayList collectionResults = new ArrayList();
                        result.put(dosField.name, collectionResults);
                        rs = stat.executeQuery();
                        byte typeCode = Utils.getByte(dosField.type);
                        if(typeCode == 24 || typeCode == 25)
                            while(rs.next()) 
                            {
                                tempList = new ArrayList();
                                tempList.add(Utils.getLongToHexString(rs, 1));
                                if(!Utils.isNullString((String)tempList.get(0)))
                                {
                                    codeItem = getCodeItem((String)tempList.get(0));
                                    if(codeItem != null)
                                    {
                                        tempList.add(codeItem.get("codeitemid"));
                                        tempList.add(codeItem.get("name"));
                                        tempList.add(codeItem.get("description"));
                                        codeItem = null;
                                    }
                                    collectionResults.add(tempList);
                                }
                                tempList = null;
                            }
                        else
                        if(typeCode == 16)
                            while(rs.next()) 
                            {
                                tempList = new ArrayList();
                                tempList.add(rs.getString(1));
                                if(!Utils.isNullString((String)tempList.get(0)))
                                {
                                    DOSChangeable tempObject = get((String)tempList.get(0));
                                    if(tempObject != null)
                                    {
                                        tempList.add(tempObject.get("md$number"));
                                        tempList.add(tempObject.get("md$description"));
                                        tempList.add(tempObject.get("md$user"));
                                        tempList.add(tempObject.get("md$status"));
                                        tempObject.clear();
                                        tempObject = null;
                                    }
                                    collectionResults.add(tempList);
                                }
                                tempList = null;
                            }
                        else
                            for(; rs.next(); addListFromResultSet(collectionResults, rs, 1, Utils.getByte(dosField.type)));
                        rs.close();
                        rs = null;
                        collectionResults = null;
                    }

                    stat.close();
                    stat = null;
                    sb2.setLength(0);
                    sb2 = null;
                    collectionList.clear();
                    collectionList = null;
                }
                con.commit();
                fieldList.clear();
                fieldList = null;
                typeArray = (byte[])null;
                result.setOriginalValueMap(Utils.cloneHashMap(result.getValueMap()));
                synchronized(instanceCache)
                {
                    if(!instanceCache.containsKey(ouid))
                    {
                        instanceCache.put(ouid, result);
                        instanceCacheTag.addLast(ouid);
                        if(instanceCacheTag.size() > INSTANCE_CACHE_SIZE)
                            instanceCache.remove(instanceCacheTag.removeFirst());
                    }
                }
            }
            doschangeable = result;
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
        if(e != null)
        {
            System.err.println(e);
            throw new IIPRequestException(e.toString());
        }
        return null;
    }

    public DOSChangeable getCoreFieldValue(String ouid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        HashMap hashmap = instanceCache;
        JVM INSTR monitorenter ;
        DOSChangeable result;
        if(!instanceCache.containsKey(ouid))
            break MISSING_BLOCK_LABEL_63;
        result = (DOSChangeable)instanceCache.get(ouid);
        if(result != null && result.getValueMap().size() > 0)
            return result;
        hashmap;
        JVM INSTR monitorexit ;
          goto _L1
        hashmap;
        JVM INSTR monitorexit ;
        throw ;
_L1:
        DOSChangeable result;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        result = null;
        StringBuffer sb = null;
        String classCode = null;
        String datasourceId = null;
        long realOuid = 0L;
        boolean versionable = false;
        boolean isAssociationClass = false;
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid:" + ouid + ")");
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code.toLowerCase().replace(' ', '_');
        sb = new StringBuffer();
        sb.append("select ");
        if(versionable)
            sb.append("vf$identity,md$number,md$desc,vf$version,md$status");
        else
        if(isAssociationClass)
            sb.append("md$sequence");
        else
            sb.append("md$number,md$desc,md$status");
        sb.append(" from ");
        if(isAssociationClass)
        {
            sb.append(ouid.substring(0, ouid.indexOf('$')));
            sb.append("$ac where sf$ouid=");
        } else
        {
            sb.append(classCode);
            if(versionable)
                sb.append("$vf where vf$ouid=");
            else
                sb.append("$sf where sf$ouid=");
        }
        realOuid = Utils.getRealLongOuid(ouid);
        sb.append(realOuid);
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            stat = con.prepareStatement(sb.toString());
            rs = stat.executeQuery();
            if(rs.next())
            {
                result = new DOSChangeable();
                result.setClassOuid(classOuid);
                result.setOuid(Long.toHexString(realOuid));
                result.put("ouid", ouid);
                if(versionable)
                {
                    result.put("vf$identity", classCode + "$id@" + Long.toHexString(rs.getLong(1)));
                    result.put("md$number", rs.getString(2));
                    result.put("md$description", rs.getString(3));
                    result.put("vf$version", rs.getString(4));
                    result.put("md$status", msr.getStgrepString((String)getClientContext().get("locale"), rs.getString(5)));
                } else
                if(isAssociationClass)
                {
                    result.put("md$sequence", rs.getString(1));
                } else
                {
                    result.put("md$number", rs.getString(1));
                    result.put("md$description", rs.getString(2));
                    result.put("md$status", msr.getStgrepString((String)getClientContext().get("locale"), rs.getString(3)));
                }
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            sb.setLength(0);
            sb = null;
            con.commit();
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return result;
    }

    public void set(DOSChangeable object)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        HashMap context = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList fieldList = null;
        ArrayList collectionList = null;
        Iterator fieldKey = null;
        String classCode = null;
        String ouid = null;
        String classOuid = null;
        String tempString = null;
        String datasourceId = null;
        String objectOuid = null;
        StringBuffer sb = null;
        Object tempObject = null;
        long ouidLong = 0L;
        int rows = 0;
        int fieldCount = 0;
        int multiplicity_to = 0;
        int i = 0;
        byte changeable = 0;
        byte typeCode = 0;
        boolean versionable = false;
        boolean isAssociationClass = false;
        boolean hasCollection = false;
        boolean isFirstField = true;
        if(object == null)
            throw new IIPRequestException("Null object.");
        ouid = (String)object.get("ouid");
        synchronized(instanceCache)
        {
            if(instanceCache.containsKey(ouid))
            {
                instanceCacheTag.remove(ouid);
                instanceCache.remove(ouid);
            }
        }
        context = getClientContext();
        if(context == null)
            throw new IIPRequestException("Internal error: Client context not exists.");
        classOuid = object.getClassOuid();
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists.");
        HashMap valueMap = object.getChangedValueMap();
        if(valueMap == null || valueMap.isEmpty())
            throw new IIPRequestException("No changed.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        fieldList = listFieldInClassInternal(classOuid);
        fieldCount = fieldList.size();
        fieldKey = fieldList.iterator();
        while(fieldKey.hasNext()) 
        {
            dosField = (DOSField)fieldKey.next();
            changeable = Utils.getByte(dosField.changeable);
            if(changeable == 3)
            {
                tempObject = valueMap.get(dosField.name);
                if(tempObject != null)
                {
                    dosClass = null;
                    valueMap = null;
                    fieldKey = null;
                    tempObject = null;
                    throw new IIPRequestException("Frozen field: " + dosField.name);
                }
                tempObject = null;
            } else
            if(Utils.getDouble(dosField.size) > 0.0D)
            {
                tempObject = valueMap.get(dosField.name);
                if(tempObject == null)
                {
                    tempObject = null;
                    continue;
                }
                if((tempObject instanceof String) && !Utils.isNullString((String)tempObject) && !Utils.checkStringSize((String)tempObject, Utils.getDouble(dosField.size), Utils.getByte(dosField.type)))
                {
                    dosClass = null;
                    valueMap = null;
                    fieldKey = null;
                    tempObject = null;
                    throw new IIPRequestException("Exceeded the field size: " + dosField.name);
                }
                tempObject = null;
            }
            dosField = null;
        }
        fieldKey = null;
        classCode = dosClass.code;
        classCode = classCode.toLowerCase().replace(' ', '_');
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        if(isAssociationClass && valueMap.containsKey("md$sequence"))
        {
            String assoOuid = (String)object.get("ouid@association");
            String end1Ouid = (String)object.get("as$end1");
            String end2Ouid = (String)object.get("as$end2");
            boolean dupSequence = false;
            if(Utils.isNullString(assoOuid) || Utils.isNullString(end1Ouid) || Utils.isNullString(end2Ouid))
                throw new IIPRequestException("Required data not assigned.");
            if(dupSequence = checkDuplicateSequence(assoOuid, end1Ouid, end2Ouid, (String)valueMap.get("md$sequence")))
                throw new IIPRequestException("Sequence duplicated.");
        } else
        if(valueMap.containsKey("md$number"))
        {
            boolean dupNumber = false;
            if(checkDuplicateNumber(classOuid, (String)valueMap.get("md$number")) != null)
                throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B406", "Number Duplicated."));
        }
        sb = new StringBuffer();
        sb.append("update ");
        if(isAssociationClass)
        {
            classCode = ouid.substring(0, ouid.indexOf('$'));
            sb.append(classCode);
            sb.append("$ac set ");
        } else
        {
            sb.append(classCode);
            if(versionable)
                sb.append("$vf");
            else
                sb.append("$sf");
            sb.append(" set md$mdate=to_char(sysdate,'YYYYMMDDHH24MISS')");
            isFirstField = false;
        }
        for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
        {
            dosField = (DOSField)fieldKey.next();
            multiplicity_to = Utils.getInt(dosField.multiplicity_to);
            if(multiplicity_to > 1)
            {
                hasCollection = true;
                if(collectionList == null)
                    collectionList = new ArrayList();
                collectionList.add(dosField);
                dosField = null;
            } else
            if(!valueMap.containsKey(dosField.name))
            {
                dosField = null;
            } else
            {
                if(isFirstField)
                    isFirstField = false;
                else
                    sb.append(',');
                sb.append(dosField.code.toLowerCase().replace(' ', '_'));
                typeCode = dosField.type.byteValue();
                if(typeCode == 21)
                    sb.append("=to_char(to_date(?,'YYYY-MM-DD HH24:MI:SS'),'YYYYMMDDHH24MISS')");
                else
                if(typeCode == 22)
                    sb.append("=to_char(to_date(?,'YYYY-MM-DD'),'YYYYMMDD')");
                else
                if(typeCode == 23)
                    sb.append("=to_char(to_date(?,'HH24:MI:SS'),'HH24MISS')");
                else
                    sb.append("=?");
                dosField = null;
            }
        }

        fieldKey = null;
        sb.append(" where ");
        if(versionable)
            sb.append("vf$ouid=?");
        else
            sb.append("sf$ouid=?");
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouid = ouid.substring(ouid.indexOf('@') + 1);
            ouidLong = Utils.convertOuidToLong(ouid);
            stat = con.prepareStatement(sb.toString());
            i = 0;
            for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
            {
                dosField = (DOSField)fieldKey.next();
                multiplicity_to = Utils.getInt(dosField.multiplicity_to);
                if(multiplicity_to > 1)
                    dosField = null;
                else
                if(!valueMap.containsKey(dosField.name))
                {
                    dosField = null;
                } else
                {
                    i++;
                    tempObject = valueMap.get(dosField.name);
                    typeCode = dosField.type.byteValue();
                    if(typeCode == 16)
                    {
                        if(tempObject == null)
                            stat.setString(i, null);
                        else
                            stat.setString(i, (String)tempObject);
                    } else
                    if(typeCode == 24 || typeCode == 25)
                    {
                        if(tempObject == null)
                        {
                            stat.setString(i, null);
                        } else
                        {
                            objectOuid = (String)tempObject;
                            stat.setLong(i, Utils.convertOuidToLong(objectOuid.substring(objectOuid.lastIndexOf('@') + 1)));
                        }
                    } else
                    if(typeCode == 1)
                    {
                        if(tempObject == null)
                            Utils.setBoolean(stat, i, null);
                        else
                            Utils.setBoolean(stat, i, (Boolean)tempObject);
                    } else
                    if(typeCode == 2)
                    {
                        if(tempObject == null)
                            Utils.setByte(stat, i, null);
                        else
                        if(tempObject instanceof String)
                        {
                            if(Utils.isNullString((String)tempObject))
                                Utils.setByte(stat, i, null);
                            else
                                Utils.setByte(stat, i, Byte.valueOf((String)tempObject));
                        } else
                        if(tempObject instanceof Byte)
                            Utils.setByte(stat, i, (Byte)tempObject);
                    } else
                    if(typeCode == 4)
                    {
                        if(tempObject == null)
                            Utils.setDouble(stat, i, null);
                        else
                        if(tempObject instanceof String)
                        {
                            if(Utils.isNullString((String)tempObject))
                                Utils.setDouble(stat, i, null);
                            else
                                Utils.setDouble(stat, i, Double.valueOf((String)tempObject));
                        } else
                        if(tempObject instanceof Double)
                            Utils.setDouble(stat, i, (Double)tempObject);
                    } else
                    if(typeCode == 5)
                    {
                        if(tempObject == null)
                            Utils.setFloat(stat, i, null);
                        else
                        if(tempObject instanceof String)
                        {
                            if(Utils.isNullString((String)tempObject))
                                Utils.setFloat(stat, i, null);
                            else
                                Utils.setFloat(stat, i, Float.valueOf((String)tempObject));
                        } else
                        if(tempObject instanceof Float)
                            Utils.setFloat(stat, i, (Float)tempObject);
                    } else
                    if(typeCode == 6)
                    {
                        if(tempObject == null)
                            Utils.setInt(stat, i, null);
                        else
                        if(tempObject instanceof String)
                        {
                            if(Utils.isNullString((String)tempObject))
                                Utils.setInt(stat, i, null);
                            else
                                Utils.setInt(stat, i, Integer.valueOf((String)tempObject));
                        } else
                        if(tempObject instanceof Integer)
                            Utils.setInt(stat, i, (Integer)tempObject);
                    } else
                    if(typeCode == 7)
                    {
                        if(tempObject == null)
                            Utils.setLong(stat, i, null);
                        else
                        if(tempObject instanceof String)
                        {
                            if(Utils.isNullString((String)tempObject))
                                Utils.setLong(stat, i, null);
                            else
                                Utils.setLong(stat, i, Long.valueOf((String)tempObject));
                        } else
                        if(tempObject instanceof Long)
                            Utils.setLong(stat, i, (Long)tempObject);
                    } else
                    if(typeCode == 8)
                    {
                        if(tempObject == null)
                            Utils.setShort(stat, i, null);
                        else
                        if(tempObject instanceof Short)
                        {
                            if(Utils.isNullString((String)tempObject))
                                Utils.setShort(stat, i, null);
                            else
                                Utils.setShort(stat, i, Short.valueOf((String)tempObject));
                        } else
                        if(tempObject instanceof Short)
                            Utils.setShort(stat, i, (Short)tempObject);
                    } else
                    if(Utils.isNullString((String)tempObject))
                        stat.setString(i, null);
                    else
                        stat.setString(i, (String)tempObject);
                    tempObject = null;
                }
            }

            i++;
            stat.setLong(i, ouidLong);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            if(hasCollection && !Utils.isNullArrayList(collectionList))
            {
                stat = con.prepareStatement("delete from " + classCode + "$cf " + "where md$ouid=?");
                stat.setLong(1, ouidLong);
                stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("insert into " + classCode + "$cf (md$ouid,md$code,md$key,md$value)" + "values (?,?,?,?)");
                stat.setLong(1, ouidLong);
                int n = 0;
                byte type = 0;
                Object aValue = null;
                String collOuid = null;
                ArrayList arrayList = null;
                ArrayList aRow = null;
                Iterator listKey = null;
                Iterator collKey;
                for(collKey = collectionList.iterator(); collKey.hasNext();)
                {
                    dosField = (DOSField)collKey.next();
                    arrayList = (ArrayList)object.get(dosField.name);
                    if(Utils.isNullArrayList(arrayList))
                    {
                        arrayList = null;
                        dosField = null;
                    } else
                    {
                        type = dosField.type.byteValue();
                        stat.setString(2, dosField.code.toLowerCase().replace(' ', '_'));
                        n = 0;
                        for(listKey = arrayList.iterator(); listKey.hasNext(); stat.executeUpdate())
                        {
                            aValue = listKey.next();
                            n++;
                            stat.setLong(3, n);
                            if(aValue == null)
                                stat.setString(4, null);
                            else
                            if(type == 24 || type == 25)
                            {
                                aRow = (ArrayList)aValue;
                                collOuid = (String)aRow.get(0);
                                stat.setLong(4, Utils.getRealLongOuid(collOuid));
                                aRow = null;
                            } else
                            if(type == 16)
                            {
                                aRow = (ArrayList)aValue;
                                collOuid = (String)aRow.get(0);
                                stat.setString(4, collOuid);
                                aRow = null;
                            } else
                            {
                                stat.setString(4, aValue.toString());
                            }
                            aValue = null;
                        }

                        listKey = null;
                        arrayList = null;
                        dosField = null;
                    }
                }

                collKey = null;
                stat.close();
                stat = null;
            }
            con.commit();
            con.close();
            xc.close();
            valueMap = null;
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

    public DOSChangeable getFieldValue(String ouid)
        throws IIPRequestException
    {
        return null;
    }

    public void setFieldValue(DOSChangeable doschangeable)
        throws IIPRequestException
    {
    }

    public String getStatus(String ouid)
        throws IIPRequestException
    {
        String result;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        DOSChangeable tempObject = null;
        StringBuffer sb = null;
        result = null;
        String classCode = null;
        String datasourceId = null;
        int i = 0;
        int j = 0;
        long realOuid = 0L;
        boolean versionable = false;
        boolean isAssociationClass = false;
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid:" + ouid + ")");
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code.toLowerCase().replace(' ', '_');
        sb = new StringBuffer();
        sb.append("select md$status from ");
        if(isAssociationClass)
        {
            sb.append(ouid.substring(0, ouid.indexOf('$')));
            sb.append("$ac where sf$ouid=");
        } else
        {
            sb.append(classCode);
            if(versionable)
                sb.append("$vf where vf$ouid=");
            else
                sb.append("$sf where sf$ouid=");
        }
        realOuid = Utils.getRealLongOuid(ouid);
        sb.append(realOuid);
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            stat = con.prepareStatement(sb.toString());
            rs = stat.executeQuery();
            if(rs.next())
                result = rs.getString(1);
            else
                result = null;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            sb.setLength(0);
            sb = null;
            con.commit();
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return result;
    }

    public void setStatus(String ouid, String status)
        throws IIPRequestException
    {
        HashMap dupCheck = new HashMap();
        setStatusHelper(ouid, status, dupCheck);
    }

    private void setStatusHelper(String ouid, String status, HashMap dupCheck)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String classCode;
        String classOuid;
        String datasourceId;
        String versionString;
        StringBuffer sb;
        StringBuffer sb2;
        boolean versionable;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        classCode = null;
        classOuid = null;
        String tempString = null;
        datasourceId = null;
        String objectOuid = null;
        versionString = null;
        sb = null;
        sb2 = null;
        long ouidLong = 0L;
        int rows = 0;
        versionable = false;
        boolean isAssociationClass = false;
        Utils.checkMandatoryString(ouid, "ouid");
        if(dupCheck.containsKey(ouid))
            return;
        synchronized(instanceCache)
        {
            if(instanceCache.containsKey(ouid))
            {
                instanceCacheTag.remove(ouid);
                instanceCache.remove(ouid);
            }
        }
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        dupCheck.put(ouid, null);
        classCode = dosClass.code;
        classCode = classCode.toLowerCase().replace(' ', '_');
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        sb = new StringBuffer();
        sb.append("update ");
        sb.append(classCode);
        if(versionable)
            sb.append("$vf");
        else
            sb.append("$sf");
        sb.append(" set md$status=?");
        if(versionable && status.equals("RLS"))
            sb.append(",vf$version=decode(vf$version,'wip',?,vf$version)");
        sb.append(" where ");
        if(versionable)
            sb.append("vf$ouid=?");
        else
            sb.append("sf$ouid=?");
        if(versionable && status.equals("RLS"))
        {
            sb2 = new StringBuffer();
            sb2.append("update ");
            sb2.append(classCode);
            sb2.append("$id set id$last=? where id$ouid=(select vf$identity from ");
            sb2.append(classCode);
            sb2.append("$vf where vf$ouid=?)");
        }
        StringBuffer sb0;
        DOSAssociation dosAssociation;
        ArrayList list1;
        ArrayList list2;
        HashMap assoMap;
        HashMap listCache;
        HashMap targetAssoMap;
        boolean isFirstSQL;
        boolean willExecute;
        int sqlCount;
        Iterator listKey;
        xc = dtm.getPooledConnection(datasourceId);
        con = xc.getConnection();
        if(versionable)
            versionString = generateVersion(con, classCode, ouid);
        ouid = ouid.substring(ouid.indexOf('@') + 1);
        long ouidLong = Utils.convertOuidToLong(ouid);
        stat = con.prepareStatement(sb.toString());
        stat.setString(1, status);
        if(versionable && status.equals("RLS"))
        {
            stat.setString(2, versionString);
            stat.setLong(3, ouidLong);
        } else
        {
            stat.setLong(2, ouidLong);
        }
        int rows = stat.executeUpdate();
        stat.close();
        stat = null;
        if(sb2 != null)
        {
            stat = con.prepareStatement(sb2.toString());
            stat.setLong(1, ouidLong);
            stat.setLong(2, ouidLong);
            stat.executeUpdate();
            stat.close();
            stat = null;
            sb2.setLength(0);
            sb2 = null;
        }
        con.commit();
        sb.setLength(0);
        sb = null;
        con.close();
        con = null;
        xc.close();
        xc = null;
        String leafOuid = null;
        String classCodeAsso = null;
        String oldDatasourceId = null;
        sb0 = null;
        dosAssociation = null;
        DOSClass leafDosClass = null;
        list1 = new ArrayList();
        list2 = new ArrayList();
        ArrayList leafClassList = null;
        ArrayList leafClass1 = null;
        ArrayList leafClass2 = null;
        Iterator leafClassKey = null;
        Iterator list1Key = null;
        assoMap = new HashMap();
        listCache = new HashMap();
        targetAssoMap = listEffectiveAssociationInternal(classOuid, "from", "all", false);
        boolean willGenerateSQL = false;
        isFirstSQL = true;
        willExecute = false;
        sqlCount = 0;
        for(Iterator assoKey = targetAssoMap.keySet().iterator(); assoKey.hasNext();)
        {
            leafOuid = (String)assoKey.next();
            dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
            DOSClass dosClass = (DOSClass)classMap.get(leafOuid);
            if(!dosAssociation.type.equals(ASSOCIATION))
            {
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if(!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeRelease, false))
                    assoKey.remove();
                leafClass2 = null;
            } else
            {
                leafClass1 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end1_class.OUID));
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass1 == null)
                {
                    leafClass1 = new ArrayList();
                    leafClass1.add(Long.toHexString(dosAssociation.end1_class.OUID));
                }
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if((!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeRelease, false)) && (!leafClass1.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end1_cascadeRelease, false)))
                    assoKey.remove();
                leafClass1 = null;
                leafClass2 = null;
            }
        }

        listLinkHelper(list1, assoMap, listCache, ouid, null, null, targetAssoMap, true, true, true);
        sb0 = new StringBuffer();
        sb0.append("select * from (");
        listKey = targetAssoMap.keySet().iterator();
          goto _L1
_L3:
        String leafOuid = (String)listKey.next();
        dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
        DOSClass dosClass = (DOSClass)classMap.get(leafOuid);
        if(dosClass == null)
            continue; /* Loop/switch isn't completed */
        String classCodeAsso = dosAssociation.code.toLowerCase().replace(' ', '_');
        versionable = Utils.getBoolean(dosClass.versionable);
        try
        {
            list1 = (ArrayList)listCache.get(classCodeAsso + ouid);
            boolean willGenerateSQL = list1.size() > 0;
            ArrayList leafClassList;
            if(!Utils.getBoolean(dosClass.isLeaf))
            {
                leafClassList = listAllLeafClassOuidInModel(Long.toHexString(dosClass.OUID));
            } else
            {
                leafClassList = new ArrayList(1);
                leafClassList.add(Long.toHexString(dosClass.OUID));
            }
            if(!Utils.isNullArrayList(leafClassList))
            {
                Iterator leafClassKey;
                for(leafClassKey = leafClassList.iterator(); leafClassKey.hasNext() && list1.size() > 0 && willGenerateSQL;)
                {
                    leafOuid = (String)leafClassKey.next();
                    DOSClass leafDosClass = (DOSClass)classMap.get(leafOuid);
                    classCode = leafDosClass.code.toLowerCase().replace(' ', '_');
                    versionable = Utils.getBoolean(leafDosClass.versionable);
                    sqlCount++;
                    if(isFirstSQL)
                        isFirstSQL = false;
                    else
                        sb0.append(" union all ");
                    if(versionable)
                    {
                        sb0.append("select '");
                        sb0.append(classCode);
                        sb0.append("$vf@',vf$ouid from ");
                        sb0.append(classCode);
                        sb0.append("$id,");
                        sb0.append(classCode);
                        sb0.append("$vf");
                        sb0.append(" where vf$ouid=id$wip");
                        sb0.append(" and vf$ouid in (");
                        boolean firstOuid = true;
                        for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                            if(firstOuid)
                                firstOuid = false;
                            else
                                sb0.append(',');

                        sb0.append(")");
                    } else
                    {
                        sb0.append("select '");
                        sb0.append(classCode);
                        sb0.append("$sf@',sf$ouid from ");
                        sb0.append(classCode);
                        sb0.append("$sf");
                        sb0.append(" where sf$ouid in (");
                        boolean firstOuid = true;
                        for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                            if(firstOuid)
                                firstOuid = false;
                            else
                                sb0.append(',');

                        sb0.append(") ");
                    }
                    leafDosClass = null;
                }

                leafClassKey = null;
            }
            String oldDatasourceId;
            if(datasourceId == null)
            {
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                oldDatasourceId = datasourceId;
            } else
            {
                oldDatasourceId = datasourceId;
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                if(!datasourceId.equals(oldDatasourceId))
                    willExecute = true;
            }
            if(!listKey.hasNext())
                willExecute = true;
            if(willExecute && sqlCount > 0)
            {
                sb0.append(")");
                xc = dtm.getPooledConnection(oldDatasourceId);
                con = xc.getConnection();
                stat = con.prepareStatement(sb0.toString());
                ResultSet rs;
                for(rs = stat.executeQuery(); rs.next(); list2.add(rs.getString(1) + Long.toHexString(rs.getLong(2))));
                sb0.setLength(0);
                rs.close();
                rs = null;
                con.commit();
                con.close();
                con = null;
                xc.close();
                xc = null;
                if(listKey.hasNext())
                {
                    sb0.append("select * from (");
                    sqlCount = 0;
                }
                willExecute = false;
                isFirstSQL = true;
            }
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        dosAssociation = null;
_L1:
        if(listKey.hasNext()) goto _L3; else goto _L2
_L2:
        listKey = null;
        list1.clear();
        list1 = null;
        assoMap.clear();
        assoMap = null;
        listCache.clear();
        listCache = null;
        targetAssoMap.clear();
        targetAssoMap = null;
        for(listKey = list2.iterator(); listKey.hasNext(); setStatusHelper((String)listKey.next(), status, dupCheck));
        break MISSING_BLOCK_LABEL_2177;
        SQLException e;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        break MISSING_BLOCK_LABEL_2177;
        local1;
        DTMUtil.closeSafely(stat, con, xc);
        stat = null;
        con = null;
        xc = null;
        JVM INSTR ret 47;
        return;
    }

    public void lock(String s)
        throws IIPRequestException
    {
    }

    public void unlock(String s)
        throws IIPRequestException
    {
    }

    public HashMap cloneObject(String ouid, String parentOuid, HashMap valueMap)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String classOuid;
        String datasourceId;
        String objectOuid;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        String classCode = null;
        classOuid = null;
        String tempString = null;
        datasourceId = null;
        objectOuid = null;
        StringBuffer sb2 = null;
        long ouidLong = 0L;
        int rows = 0;
        boolean versionable = false;
        boolean isAssociationClass = false;
        Utils.checkMandatoryString(ouid, "ouid");
        Utils.checkMandatoryString(parentOuid, "parentOuid");
        synchronized(instanceCache)
        {
            if(instanceCache.containsKey(ouid))
            {
                instanceCacheTag.remove(ouid);
                instanceCache.remove(ouid);
            }
        }
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        ArrayList fieldList = listFieldInClassInternal(classOuid);
        DOSField dosField = null;
        ArrayList tempFields = null;
        Iterator fieldKey;
        for(fieldKey = fieldList.iterator(); fieldKey.hasNext();)
        {
            dosField = (DOSField)fieldKey.next();
            if(dosField != null && dosField.type != null && dosField.type.byteValue() == 16)
            {
                tempFields = (ArrayList)valueMap.get(ouid + "^fields");
                if(Utils.isNullArrayList(tempFields))
                {
                    tempFields = new ArrayList();
                    valueMap.put(ouid + "^fields", tempFields);
                }
                if(!tempFields.contains(dosField.name))
                    tempFields.add(dosField.name);
            }
        }

        fieldKey = null;
        DOSChangeable dosInstance = get(ouid);
        String fieldString = null;
        String tempOuid = null;
        if(valueMap.containsKey(ouid + "^fields"))
        {
            tempFields = (ArrayList)valueMap.get(ouid + "^fields");
            if(!Utils.isNullArrayList(tempFields))
            {
                for(fieldKey = tempFields.iterator(); fieldKey.hasNext();)
                {
                    fieldString = (String)fieldKey.next();
                    tempOuid = (String)dosInstance.get(fieldString);
                    if(!Utils.isNullString(tempOuid) && valueMap.containsKey(tempOuid))
                        dosInstance.put(fieldString, valueMap.get(tempOuid));
                }

                fieldKey = null;
            }
        }
        if(valueMap.containsKey(ouid))
        {
            objectOuid = (String)valueMap.get(ouid);
            if(!valueMap.containsKey(ouid + "^assoOuid"))
            {
                link(parentOuid, objectOuid);
                dosInstance.clear();
                dosInstance = null;
            } else
            {
                DOSChangeable assoTemp = get((String)((List)valueMap.get(ouid + "^assoOuid")).get(0));
                if(assoTemp != null)
                {
                    String end1OuidOld = (String)assoTemp.get("end1");
                    String end2OuidOld = (String)assoTemp.get("end2");
                    if(parentOuid.equals(valueMap.get(end1OuidOld)) && objectOuid.equals(valueMap.get(end2OuidOld)))
                    {
                        assoTemp.clear("ouid");
                        link(parentOuid, objectOuid, assoTemp);
                    } else
                    if(objectOuid.equals(valueMap.get(end1OuidOld)) && parentOuid.equals(valueMap.get(end2OuidOld)))
                    {
                        assoTemp.clear("ouid");
                        link(objectOuid, parentOuid, assoTemp);
                    } else
                    {
                        link(parentOuid, objectOuid);
                    }
                } else
                {
                    link(parentOuid, objectOuid);
                }
            }
            valueMap.put("ouid", objectOuid);
            return valueMap;
        }
        dosInstance.clear("ouid");
        objectOuid = add(dosInstance);
        valueMap.put(ouid, objectOuid);
        if(!valueMap.containsKey(ouid + "^assoOuid"))
        {
            link(parentOuid, objectOuid);
        } else
        {
            DOSChangeable assoTemp = get((String)((List)valueMap.get(ouid + "^assoOuid")).get(0));
            if(assoTemp != null)
            {
                String end1OuidOld = (String)assoTemp.get("end1");
                String end2OuidOld = (String)assoTemp.get("end2");
                if(parentOuid.equals(valueMap.get(end1OuidOld)) && objectOuid.equals(valueMap.get(end2OuidOld)))
                {
                    assoTemp.clear("ouid");
                    link(parentOuid, objectOuid, assoTemp);
                } else
                if(objectOuid.equals(valueMap.get(end1OuidOld)) && parentOuid.equals(valueMap.get(end2OuidOld)))
                {
                    assoTemp.clear("ouid");
                    link(objectOuid, parentOuid, assoTemp);
                } else
                if(parentOuid.equals(end1OuidOld) && objectOuid.equals(valueMap.get(end2OuidOld)) || parentOuid.equals(valueMap.get(end1OuidOld)) && objectOuid.equals(end2OuidOld) || objectOuid.equals(end1OuidOld) && parentOuid.equals(valueMap.get(end2OuidOld)) || objectOuid.equals(valueMap.get(end1OuidOld)) && parentOuid.equals(end2OuidOld))
                    assoTemp.clear("ouid");
                else
                    link(parentOuid, objectOuid);
            } else
            {
                link(parentOuid, objectOuid);
            }
        }
        classCode = dosClass.code;
        classCode = classCode.toLowerCase().replace(' ', '_');
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        StringBuffer sb0;
        DOSAssociation dosAssociation;
        ArrayList list1;
        ArrayList list2;
        HashMap assoMap;
        HashMap listCache;
        HashMap targetAssoMap;
        boolean isFirstSQL;
        boolean willExecute;
        int sqlCount;
        HashMap targetAssoMap2;
        Iterator listKey;
        Iterator listKey2;
        xc = dtm.getPooledConnection(datasourceId);
        con = xc.getConnection();
        String leafOuid = null;
        String classCodeAsso = null;
        String oldDatasourceId = null;
        sb0 = null;
        dosAssociation = null;
        DOSClass leafDosClass = null;
        list1 = new ArrayList();
        list2 = new ArrayList();
        ArrayList list3 = null;
        ArrayList leafClassList = null;
        ArrayList leafClass1 = null;
        ArrayList leafClass2 = null;
        Iterator leafClassKey = null;
        Iterator list1Key = null;
        assoMap = new HashMap();
        listCache = new HashMap();
        targetAssoMap = listEffectiveAssociationInternal(classOuid, "from", "composition", false);
        boolean willGenerateSQL = false;
        isFirstSQL = true;
        willExecute = false;
        sqlCount = 0;
        for(Iterator assoKey = targetAssoMap.keySet().iterator(); assoKey.hasNext();)
        {
            leafOuid = (String)assoKey.next();
            dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
            DOSClass dosClass = (DOSClass)classMap.get(leafOuid);
            if(!dosAssociation.type.equals(ASSOCIATION))
            {
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if(!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeClone, false))
                    assoKey.remove();
                leafClass2 = null;
            } else
            {
                leafClass1 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end1_class.OUID));
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass1 == null)
                {
                    leafClass1 = new ArrayList();
                    leafClass1.add(Long.toHexString(dosAssociation.end1_class.OUID));
                }
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if((!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeClone, false)) && (!leafClass1.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end1_cascadeClone, false)))
                    assoKey.remove();
                leafClass1 = null;
                leafClass2 = null;
            }
        }

        targetAssoMap2 = listEffectiveAssociationInternal(classOuid, Boolean.TRUE, "association");
        for(Iterator assoKey = targetAssoMap2.keySet().iterator(); assoKey.hasNext();)
        {
            leafOuid = (String)assoKey.next();
            dosAssociation = (DOSAssociation)targetAssoMap2.get(leafOuid);
            DOSClass dosClass = (DOSClass)classMap.get(leafOuid);
            if(!dosAssociation.type.equals(ASSOCIATION))
            {
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if(!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeClone, false))
                    assoKey.remove();
                leafClass2 = null;
            } else
            {
                leafClass1 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end1_class.OUID));
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass1 == null)
                {
                    leafClass1 = new ArrayList();
                    leafClass1.add(Long.toHexString(dosAssociation.end1_class.OUID));
                }
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if((!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeClone, false)) && (!leafClass1.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end1_cascadeClone, false)))
                    assoKey.remove();
                leafClass1 = null;
                leafClass2 = null;
            }
        }

        listLinkHelper(list1, assoMap, listCache, ouid, null, null, targetAssoMap, true, true, true);
        listLinkHelper(list1, assoMap, listCache, ouid, null, null, targetAssoMap2, true, true, true);
        sb0 = new StringBuffer();
        sb0.append("select * from (");
        listKey = targetAssoMap.keySet().iterator();
        listKey2 = targetAssoMap2.keySet().iterator();
          goto _L1
_L3:
        String leafOuid;
        if(listKey.hasNext())
        {
            leafOuid = (String)listKey.next();
            dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
        } else
        {
            leafOuid = (String)listKey2.next();
            dosAssociation = (DOSAssociation)targetAssoMap2.get(leafOuid);
        }
        DOSClass dosClass = (DOSClass)classMap.get(leafOuid);
        if(dosClass == null)
            continue; /* Loop/switch isn't completed */
        String classCodeAsso = dosAssociation.code.toLowerCase().replace(' ', '_');
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        try
        {
            list1 = (ArrayList)listCache.get(classCodeAsso + ouid);
            boolean willGenerateSQL = list1.size() > 0;
            ArrayList leafClassList;
            if(!Utils.getBoolean(dosClass.isLeaf))
            {
                leafClassList = listAllLeafClassOuidInModel(Long.toHexString(dosClass.OUID));
            } else
            {
                leafClassList = new ArrayList(1);
                leafClassList.add(Long.toHexString(dosClass.OUID));
            }
            if(!Utils.isNullArrayList(leafClassList))
            {
                Iterator leafClassKey;
                for(leafClassKey = leafClassList.iterator(); leafClassKey.hasNext() && list1.size() > 0 && willGenerateSQL;)
                {
                    leafOuid = (String)leafClassKey.next();
                    DOSClass leafDosClass = (DOSClass)classMap.get(leafOuid);
                    String classCode = leafDosClass.code.toLowerCase().replace(' ', '_');
                    versionable = Utils.getBoolean(leafDosClass.versionable);
                    sqlCount++;
                    if(isFirstSQL)
                        isFirstSQL = false;
                    else
                        sb0.append(" union all ");
                    if(versionable)
                    {
                        sb0.append("select '");
                        sb0.append(classCode);
                        sb0.append("$vf@',vf$ouid,md$number from ");
                        sb0.append(classCode);
                        sb0.append("$id,");
                        sb0.append(classCode);
                        sb0.append("$vf");
                        sb0.append(" where vf$ouid=id$wip");
                        sb0.append(" and vf$ouid in (");
                        boolean firstOuid = true;
                        for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                            if(firstOuid)
                                firstOuid = false;
                            else
                                sb0.append(',');

                        sb0.append(")");
                    } else
                    {
                        sb0.append("select '");
                        sb0.append(classCode);
                        sb0.append("$sf@',sf$ouid,md$number from ");
                        sb0.append(classCode);
                        sb0.append("$sf");
                        sb0.append(" where sf$ouid in (");
                        boolean firstOuid = true;
                        for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                            if(firstOuid)
                                firstOuid = false;
                            else
                                sb0.append(',');

                        sb0.append(") ");
                    }
                    leafDosClass = null;
                }

                leafClassKey = null;
            }
            String oldDatasourceId;
            if(datasourceId == null)
            {
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                oldDatasourceId = datasourceId;
            } else
            {
                oldDatasourceId = datasourceId;
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                if(!datasourceId.equals(oldDatasourceId))
                    willExecute = true;
            }
            if(!listKey.hasNext() && !listKey2.hasNext())
                willExecute = true;
            if(willExecute && sqlCount > 0)
            {
                sb0.append(") order by md$number");
                xc = dtm.getPooledConnection(oldDatasourceId);
                con = xc.getConnection();
                stat = con.prepareStatement(sb0.toString());
                String ouidTemp = null;
                String ouidTemp2 = null;
                ResultSet rs;
                for(rs = stat.executeQuery(); rs.next();)
                {
                    ouidTemp = rs.getString(1) + Long.toHexString(rs.getLong(2));
                    ouidTemp2 = rs.getString(2);
                    list2.add(ouidTemp);
                    if(assoMap.containsKey(ouidTemp2))
                    {
                        ArrayList list3 = (ArrayList)assoMap.get(ouidTemp2);
                        for(int k = 0; k < list3.size(); k++)
                            valueMap.put(ouidTemp + "^assoOuid", list3.get(k));

                    }
                }

                sb0.setLength(0);
                rs.close();
                rs = null;
                con.commit();
                con.close();
                con = null;
                xc.close();
                xc = null;
                if(listKey.hasNext() || listKey2.hasNext())
                {
                    sb0.append("select * from (");
                    sqlCount = 0;
                }
                willExecute = false;
                isFirstSQL = true;
            }
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                e.printStackTrace();
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        dosAssociation = null;
_L1:
        if(listKey.hasNext() || listKey2.hasNext()) goto _L3; else goto _L2
_L2:
        listKey = null;
        listKey2 = null;
        list1.clear();
        list1 = null;
        assoMap.clear();
        assoMap = null;
        listCache.clear();
        listCache = null;
        targetAssoMap.clear();
        targetAssoMap = null;
        targetAssoMap2.clear();
        targetAssoMap2 = null;
        for(listKey = list2.iterator(); listKey.hasNext(); cloneObject((String)listKey.next(), objectOuid, valueMap));
          goto _L4
        SQLException e;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
          goto _L4
        e;
        e.printStackTrace();
          goto _L4
        local1;
        DTMUtil.closeSafely(stat, con, xc);
        stat = null;
        con = null;
        xc = null;
        JVM INSTR ret 57;
_L4:
        valueMap.put("ouid", objectOuid);
        return valueMap;
    }

    private void makeWipObjectHelper(String ouid, HashMap targetAssoMap, long newOuidLong)
        throws SQLException, IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSAssociation dosAssociation = null;
        DOSClass assoClass = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList assoResultList = null;
        ArrayList assoFieldList = null;
        ArrayList oneRow = null;
        Iterator listKey = null;
        Iterator fieldKey = null;
        Iterator resultKey = null;
        StringBuffer sb1 = null;
        String classCodeAsso = null;
        String fieldOuid = null;
        String datasourceIdAsso = null;
        String leafOuid = null;
        String tempString = null;
        boolean isAssociationClass = false;
        int i = 0;
        int sfOuidAt = 0;
        int endOuidAt = 0;
        int fieldCount = 0;
        int rowCount = 0;
        long ouidLong = 0L;
        int typeArray4Asso[] = (int[])null;
        long ouidArray[] = (long[])null;
        HashMap tmpAssoMap = new HashMap();
        ArrayList leafClass1 = null;
        ArrayList leafClass2 = null;
        tempString = ouid.substring(ouid.indexOf('@') + 1);
        ouidLong = Utils.convertOuidToLong(tempString);
        listKey = targetAssoMap.keySet().iterator();
        while(listKey.hasNext()) 
        {
            leafOuid = (String)listKey.next();
            dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
            dosClass = (DOSClass)classMap.get(leafOuid);
            if(!dosAssociation.type.equals(ASSOCIATION))
            {
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if(!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeClone, false))
                    listKey.remove();
                leafClass2 = null;
            } else
            {
                leafClass1 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end1_class.OUID));
                leafClass2 = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                if(leafClass1 == null)
                {
                    leafClass1 = new ArrayList();
                    leafClass1.add(Long.toHexString(dosAssociation.end1_class.OUID));
                }
                if(leafClass2 == null)
                {
                    leafClass2 = new ArrayList();
                    leafClass2.add(Long.toHexString(dosAssociation.end2_class.OUID));
                }
                if((!leafClass2.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end2_cascadeClone, false)) && (!leafClass1.contains(Long.toHexString(dosClass.OUID)) || !Utils.getBoolean(dosAssociation.end1_cascadeClone, false)))
                {
                    listKey.remove();
                    continue;
                }
                leafClass1 = null;
                leafClass2 = null;
            }
            if(dosClass != null && !tmpAssoMap.containsKey(Long.toHexString(dosAssociation.OUID)))
            {
                tmpAssoMap.put(Long.toHexString(dosAssociation.OUID), dosAssociation);
                datasourceIdAsso = dosAssociation.datasourceId;
                if(Utils.isNullString(datasourceIdAsso))
                    datasourceIdAsso = "default";
                assoClass = dosAssociation._class;
                if(assoClass != null)
                {
                    assoFieldList = listFieldInClassInternal(Long.toHexString(assoClass.OUID));
                    typeArray4Asso = new int[assoFieldList.size() + 4];
                    typeArray4Asso[0] = 13;
                    typeArray4Asso[1] = 7;
                    typeArray4Asso[2] = 7;
                    typeArray4Asso[3] = 7;
                    assoResultList = new ArrayList();
                    isAssociationClass = true;
                } else
                {
                    isAssociationClass = false;
                }
                classCodeAsso = dosAssociation.code.toLowerCase().replace(' ', '_');
                if(!isAssociationClass)
                {
                    xc = dtm.getPooledConnection(datasourceIdAsso);
                    con = xc.getConnection();
                    stat = con.prepareStatement("insert into " + classCodeAsso + "$as " + "(as$end1, as$end2) " + "select ?, " + "as$end2 " + "from " + classCodeAsso + "$as " + "where as$end1=? " + "order by as$end2");
                    stat.setLong(1, newOuidLong);
                    stat.setLong(2, ouidLong);
                    stat.executeUpdate();
                    stat.close();
                    stat = null;
                    stat = con.prepareStatement("insert into " + classCodeAsso + "$as " + "(as$end1, as$end2) " + "select as$end1, " + "? " + "from " + classCodeAsso + "$as " + "where as$end2=? " + "order by as$end2");
                    stat.setLong(1, newOuidLong);
                    stat.setLong(2, ouidLong);
                    stat.executeUpdate();
                    stat.close();
                    stat = null;
                    con.commit();
                    con.close();
                    con = null;
                    xc.close();
                    xc = null;
                } else
                {
                    xc = dtm.getPooledConnection(datasourceIdAsso);
                    con = xc.getConnection();
                    sb1 = new StringBuffer();
                    sb1.append("select '");
                    sb1.append(classCodeAsso);
                    sb1.append("$ac',sf$ouid,?,as$end2");
                    i = 6;
                    if(assoFieldList != null && assoFieldList.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                        for(fieldKey = assoFieldList.iterator(); fieldKey.hasNext();)
                        {
                            i++;
                            dosField = (DOSField)fieldKey.next();
                            if(dosField == null || !assoFieldList.contains(dosField))
                            {
                                dosField = null;
                            } else
                            {
                                sb1.append(',');
                                sb1.append(dosField.code.toLowerCase().replace(' ', '_'));
                            }
                        }

                    sb1.append(" from ");
                    sb1.append(classCodeAsso);
                    sb1.append("$ac ");
                    sb1.append("where as$end1=?");
                    stat = con.prepareStatement(sb1.toString());
                    stat.setLong(1, newOuidLong);
                    stat.setLong(2, ouidLong);
                    rs = stat.executeQuery();
                    rowCount = 0;
                    while(rs.next()) 
                    {
                        oneRow = new ArrayList();
                        if(typeArray4Asso != null)
                        {
                            int y = 1;
                            for(int z = 0; z < typeArray4Asso.length; z++)
                            {
                                oneRow.add(rs.getString(y));
                                y++;
                            }

                        }
                        assoResultList.add(oneRow);
                        oneRow = null;
                    }
                    rs.close();
                    rs = null;
                    stat.close();
                    stat = null;
                    sb1.setLength(0);
                    sb1.append("select '");
                    sb1.append(classCodeAsso);
                    sb1.append("$ac',sf$ouid,as$end1,?");
                    i = 6;
                    if(assoFieldList != null && assoFieldList.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                        for(fieldKey = assoFieldList.iterator(); fieldKey.hasNext();)
                        {
                            i++;
                            dosField = (DOSField)fieldKey.next();
                            if(dosField == null || !assoFieldList.contains(dosField))
                            {
                                dosField = null;
                            } else
                            {
                                sb1.append(',');
                                sb1.append(dosField.code.toLowerCase().replace(' ', '_'));
                            }
                        }

                    sb1.append(" from ");
                    sb1.append(classCodeAsso);
                    sb1.append("$ac ");
                    sb1.append("where as$end2=?");
                    stat = con.prepareStatement(sb1.toString());
                    stat.setLong(1, newOuidLong);
                    stat.setLong(2, ouidLong);
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        oneRow = new ArrayList();
                        if(typeArray4Asso != null)
                        {
                            int y = 1;
                            for(int z = 0; z < typeArray4Asso.length; z++)
                            {
                                oneRow.add(rs.getString(y));
                                y++;
                            }

                        }
                        assoResultList.add(oneRow);
                        oneRow = null;
                    }

                    rs.close();
                    rs = null;
                    stat.close();
                    stat = null;
                    sb1.setLength(0);
                    con.commit();
                    con.close();
                    con = null;
                    xc.close();
                    xc = null;
                    xc = dtm.getPooledConnection("system");
                    con = xc.getConnection();
                    ouidArray = new long[assoResultList.size()];
                    for(i = 0; i < assoResultList.size(); i++)
                        ouidArray[i] = generateOUID(con);

                    con.commit();
                    con.close();
                    con = null;
                    xc.close();
                    xc = null;
                    xc = dtm.getPooledConnection(datasourceIdAsso);
                    con = xc.getConnection();
                    int j = 0;
                    for(resultKey = assoResultList.iterator(); resultKey.hasNext();)
                    {
                        oneRow = (ArrayList)resultKey.next();
                        sb1.append("insert into ");
                        sb1.append((String)oneRow.get(0));
                        sb1.append(" (sf$ouid,as$end1,as$end2");
                        for(i = 0; i < assoFieldList.size(); i++)
                        {
                            dosField = (DOSField)assoFieldList.get(i);
                            sb1.append(",");
                            sb1.append(dosField.code.toLowerCase().replace(' ', '_'));
                        }

                        sb1.append(") values (");
                        sb1.append(Long.toString(ouidArray[j]));
                        for(i = 2; i < oneRow.size(); i++)
                        {
                            sb1.append(",");
                            sb1.append((String)oneRow.get(i));
                        }

                        sb1.append(")");
                        stat = con.prepareStatement(sb1.toString());
                        stat.executeUpdate();
                        sb1.setLength(0);
                        j++;
                    }

                    con.commit();
                    con.close();
                    con = null;
                    xc.close();
                    xc = null;
                }
            }
        }
        listKey = null;
    }

    public String makeWipObject(String ouid)
        throws IIPRequestException
    {
        String result;
        Utils.checkMandatoryString(ouid, "ouid");
        DOSClass dosClass = null;
        String classOuid = null;
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists.");
        result = null;
        DOSChangeable object = get(ouid);
        object.put("ouid", ouid);
        result = add(object);
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        DOSAssociation dosAssociation = null;
        DOSClass leafDosClass = null;
        String classCode = null;
        String tempString = null;
        String datasourceId = null;
        String objectOuid = null;
        String versionString = null;
        String leafOuid = null;
        String classCodeAsso = null;
        String oldDatasourceId = null;
        StringBuffer sb = null;
        StringBuffer sb0 = null;
        ArrayList list1 = new ArrayList();
        ArrayList list2 = new ArrayList();
        ArrayList leafClassList = null;
        ArrayList leafClass1 = null;
        ArrayList leafClass2 = null;
        Iterator leafClassKey = null;
        Iterator list1Key = null;
        HashMap targetAssoMap = null;
        long ouidLong = 0L;
        long ouidLong2 = 0L;
        boolean versionable = false;
        boolean isAssociationClass = false;
        boolean hasFileControl = false;
        int rows = 0;
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code;
        classCode = classCode.toLowerCase().replace(' ', '_');
        versionable = Utils.getBoolean(dosClass.versionable);
        isAssociationClass = Utils.getBoolean(dosClass.isAssociationClass);
        hasFileControl = Utils.getBoolean(dosClass.fileControl);
        tempString = ouid.substring(ouid.indexOf('@') + 1);
        ouidLong = Utils.convertOuidToLong(tempString);
        tempString = result.substring(result.indexOf('@') + 1);
        ouidLong2 = Utils.convertOuidToLong(tempString);
        if(hasFileControl)
        {
            sb = new StringBuffer();
            sb.append("insert into ");
            sb.append(classCode);
            sb.append("$fl (md$ouid,md$path,md$version,md$des,md$filetype,md$index,md$cidate,md$codate,md$user) ");
            sb.append("select ?,md$path,md$version,md$des,md$filetype,md$index,md$cidate,md$codate,md$user ");
            sb.append("from ");
            sb.append(classCode);
            sb.append("$fl where md$ouid=?");
        }
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            if(sb != null)
            {
                stat = con.prepareStatement(sb.toString());
                stat.setLong(1, ouidLong2);
                stat.setLong(2, ouidLong);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
            }
            targetAssoMap = listEffectiveAssociationInternal(classOuid, "both", "all", false);
            makeWipObjectHelper(ouid, targetAssoMap, ouidLong2);
            con.commit();
            if(sb != null)
            {
                sb.setLength(0);
                sb = null;
            }
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
        return result;
    }

    public ArrayList list(String ouid, ArrayList fields, HashMap filter)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ArrayList returnList;
        Iterator listKey;
        String datasourceId;
        String locale;
        StringBuffer sb0;
        StringBuffer selectClause;
        StringBuffer resultFieldSb;
        boolean versionable;
        int fieldCount;
        int typeArray[];
        boolean willExecute;
        boolean isIDFieldExists;
        int countPerPage;
        int pageNum;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass startClass = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList leafClassList = null;
        returnList = null;
        ArrayList oneRow = null;
        HashMap userData = null;
        HashMap filterMap = null;
        listKey = null;
        Iterator mapKey = null;
        Iterator fieldKey = null;
        datasourceId = null;
        String oldDatasourceId = null;
        String classOuid = null;
        String classCode = null;
        String fieldOuid = null;
        String fieldCode = null;
        String userId = null;
        String conditionString = null;
        String searchType = null;
        String searchTypeDateFrom = null;
        String searchTypeDateTo = null;
        locale = null;
        sb0 = null;
        selectClause = null;
        resultFieldSb = null;
        Object conditionObject = null;
        versionable = false;
        int i = 0;
        fieldCount = 0;
        typeArray = (int[])null;
        int multiplicityFrom = 0;
        int multiplicityTo = 0;
        boolean isFirstCondition = false;
        boolean hasCollection = false;
        willExecute = false;
        isIDFieldExists = false;
        countPerPage = -1;
        pageNum = 1;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        startClass = (DOSClass)classMap.get(ouid);
        if(startClass == null)
        {
            startClass = null;
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        }
        locale = (String)getClientContext().get("locale");
        versionable = Utils.getBoolean(startClass.versionable);
        selectClause = new StringBuffer();
        resultFieldSb = new StringBuffer();
        if(versionable)
            resultFieldSb.append("pre$Ouid, vf$ouid");
        else
            resultFieldSb.append("pre$Ouid, sf$ouid");
        i = 1;
        if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
        {
            fieldCount = fields.size() + 2;
            typeArray = new int[fieldCount];
            for(fieldKey = fields.iterator(); fieldKey.hasNext();)
            {
                i++;
                fieldOuid = (String)fieldKey.next();
                if(!fieldMap.containsKey(fieldOuid) && !reservedFields.contains(fieldOuid))
                {
                    listKey = null;
                    throw new IIPRequestException("Result field not exists. (ouid: " + fieldOuid + ")");
                }
                dosField = (DOSField)fieldMap.get(fieldOuid);
                if(dosField != null)
                {
                    multiplicityFrom = Utils.getInt(dosField.multiplicity_from);
                    multiplicityTo = Utils.getInt(dosField.multiplicity_to);
                    if(multiplicityFrom < multiplicityTo)
                    {
                        if(multiplicityTo > 1)
                            hasCollection = true;
                        else
                            hasCollection = multiplicityTo - multiplicityFrom > 1;
                    } else
                    {
                        hasCollection = false;
                    }
                    if(hasCollection)
                    {
                        typeArray[i] = -13;
                    } else
                    {
                        typeArray[i] = Utils.getByte(dosField.type);
                        selectClause.append(", ");
                        resultFieldSb.append(", ");
                        determineFoundationFieldTypes(typeArray, i, dosField.code);
                        addStringBufferFromField(selectClause, typeArray, i, dosField, resultFieldSb);
                        if(dosField.code.equals("md$number"))
                            isIDFieldExists = true;
                    }
                }
            }

            fieldKey = null;
        }
        if(filter != null && filter.size() > 0)
        {
            if(filter.containsKey("search.result.count"))
            {
                countPerPage = Integer.parseInt((String)filter.get("search.result.count"));
                filter.remove("search.result.count");
            }
            if(filter.containsKey("search.result.page"))
            {
                pageNum = Integer.parseInt((String)filter.get("search.result.page"));
                filter.remove("search.result.page");
            }
        }
        if(!Utils.getBoolean(startClass.isLeaf))
        {
            leafClassList = listAllLeafClassOuidInModel(ouid);
        } else
        {
            leafClassList = new ArrayList(1);
            leafClassList.add(ouid);
        }
        if(leafClassList == null || leafClassList.size() == 0)
            return null;
        returnList = new ArrayList();
        sb0 = new StringBuffer();
        sb0.append("select " + resultFieldSb.toString() + " from (");
        listKey = leafClassList.iterator();
          goto _L1
_L3:
        String classOuid = (String)listKey.next();
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            continue; /* Loop/switch isn't completed */
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        try
        {
            if(versionable)
            {
                sb0.append("select '");
                sb0.append(classCode);
                sb0.append("$vf@' as pre$Ouid, vf$ouid");
                sb0.append(selectClause);
                sb0.append(" from ");
                sb0.append(classCode);
                if(filter != null && filter.size() > 0)
                {
                    String searchType = (String)filter.get("version.condition.type");
                    String searchTypeDateFrom = (String)filter.get("version.condition.date.from");
                    String searchTypeDateTo = (String)filter.get("version.condition.date.to");
                    if(searchType == null)
                        throw new IIPRequestException("Miss out mandatory condition: version.condition.type");
                    if(searchType.equals("released"))
                    {
                        sb0.append("$id,");
                        sb0.append(classCode);
                        sb0.append("$vf");
                        sb0.append(" where vf$ouid=id$last and md$status='RLS'");
                    } else
                    if(searchType.equals("wip"))
                    {
                        sb0.append("$id,");
                        sb0.append(classCode);
                        sb0.append("$vf");
                        sb0.append(" where vf$ouid=id$wip");
                    } else
                    if(searchType.equals("all"))
                    {
                        sb0.append("$vf");
                        sb0.append(" where 1=1 ");
                    } else
                    if(searchType.equals("cdate"))
                    {
                        sb0.append("$vf");
                        sb0.append(" where ");
                        if(!Utils.isNullString(searchTypeDateFrom) && Utils.isNullString(searchTypeDateTo))
                        {
                            try
                            {
                                searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateFrom = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateFrom = null;
                            }
                            sb0.append("md$cdate>='");
                            sb0.append(searchTypeDateFrom);
                            sb0.append("'");
                        } else
                        if(Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                        {
                            try
                            {
                                searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateTo = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateTo = null;
                            }
                            sb0.append("md$cdate<='");
                            sb0.append(searchTypeDateTo);
                            sb0.append("'");
                        } else
                        if(!Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                        {
                            try
                            {
                                searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateFrom = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateFrom = null;
                            }
                            sb0.append("md$cdate>='");
                            sb0.append(searchTypeDateFrom);
                            sb0.append("' and ");
                            try
                            {
                                searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateTo = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateTo = null;
                            }
                            sb0.append("md$cdate<='");
                            sb0.append(searchTypeDateTo);
                            sb0.append("'");
                        } else
                        {
                            sb0.append("1=1");
                        }
                    } else
                    if(searchType.equals("mdate"))
                    {
                        sb0.append("$vf");
                        sb0.append(" where ");
                        if(!Utils.isNullString(searchTypeDateFrom) && Utils.isNullString(searchTypeDateTo))
                        {
                            try
                            {
                                searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateFrom = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateFrom = null;
                            }
                            sb0.append("md$mdate>='");
                            sb0.append(searchTypeDateFrom);
                            sb0.append("'");
                        } else
                        if(Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                        {
                            try
                            {
                                searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateTo = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateTo = null;
                            }
                            sb0.append("md$mdate<='");
                            sb0.append(searchTypeDateTo);
                            sb0.append("'");
                        } else
                        if(!Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                        {
                            try
                            {
                                searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateFrom = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateFrom = null;
                            }
                            sb0.append("md$mdate>='");
                            sb0.append(searchTypeDateFrom);
                            sb0.append("' and ");
                            try
                            {
                                searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                            }
                            catch(NumberFormatException nfe)
                            {
                                searchTypeDateTo = null;
                            }
                            catch(ParseException pe)
                            {
                                searchTypeDateTo = null;
                            }
                            sb0.append("md$mdate<='");
                            sb0.append(searchTypeDateTo);
                            sb0.append("'");
                        } else
                        {
                            sb0.append("1=1");
                        }
                    }
                    for(Iterator mapKey = filter.keySet().iterator(); mapKey.hasNext();)
                    {
                        String fieldOuid = (String)mapKey.next();
                        DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                        if(!fieldOuid.equals("version.condition.type") && !fieldOuid.equals("version.condition.date.from") && !fieldOuid.equals("version.condition.date.to") && dosField != null)
                        {
                            String fieldCode = dosField.code;
                            Object conditionObject = filter.get(fieldOuid);
                            sb0.append(" and ");
                            if(fieldCode.equals("md$number"))
                            {
                                String conditionString = (String)conditionObject;
                                conditionString = conditionString.replace('*', '%').replace('?', '_');
                                if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                {
                                    sb0.append("md$number like '");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                } else
                                {
                                    sb0.append("md$number='");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                }
                            } else
                            if(fieldCode.equals("md$desc"))
                            {
                                String conditionString = (String)conditionObject;
                                conditionString = conditionString.replace('*', '%').replace('?', '_');
                                if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                {
                                    sb0.append("md$desc like '");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                } else
                                {
                                    sb0.append("md$desc='");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                }
                            } else
                            if(fieldCode.equals("md$status"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$status='");
                                sb0.append(conditionString);
                                sb0.append("'");
                            } else
                            if(fieldCode.equals("vf$version"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("vf$version='");
                                sb0.append(conditionString);
                                sb0.append("'");
                            } else
                            if(fieldCode.equals("md$user"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$user='");
                                sb0.append(conditionString);
                                sb0.append("'");
                            } else
                            if(fieldCode.equals("md$cdate"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$cdate like to_char(to_date('");
                                sb0.append(conditionString);
                                sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                            } else
                            if(fieldCode.equals("md$mdate"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$mdate like to_char(to_date('");
                                sb0.append(conditionString);
                                sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                            } else
                            {
                                fieldCode = fieldCode.toLowerCase().replace(' ', '_');
                                sb0.append(fieldCode);
                                if(conditionObject instanceof Boolean)
                                {
                                    sb0.append("='");
                                    if(((Boolean)conditionObject).booleanValue())
                                        sb0.append('T');
                                    else
                                        sb0.append('F');
                                    sb0.append("'");
                                } else
                                if(conditionObject instanceof Number)
                                {
                                    sb0.append('=');
                                    sb0.append(conditionObject);
                                } else
                                if(dosField.type.byteValue() == 24 || dosField.type.byteValue() == 25)
                                {
                                    String conditionString = (String)conditionObject;
                                    DOSCode.getSQLPhraseForHierarchyCode(conditionString, codeMap, codeItemMap, sb0);
                                } else
                                if(dosField.type.byteValue() == 22 || dosField.type.byteValue() == 21)
                                {
                                    String conditionString = (String)conditionObject;
                                    sb0.append(" like to_char(to_date('");
                                    sb0.append(conditionString);
                                    sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                                } else
                                {
                                    String conditionString = conditionObject.toString();
                                    conditionString = conditionString.replace('*', '%').replace('?', '_');
                                    if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                    {
                                        sb0.append(" like '");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    {
                                        sb0.append("='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    }
                                }
                            }
                            dosField = null;
                        }
                    }

                } else
                {
                    sb0.append("$id,");
                    sb0.append(classCode);
                    sb0.append("$vf a where a.vf$ouid=id$wip ");
                }
            } else
            {
                sb0.append("select '");
                sb0.append(classCode);
                sb0.append("$sf@' as pre$Ouid, sf$ouid");
                sb0.append(selectClause);
                sb0.append(" from ");
                sb0.append(classCode);
                sb0.append("$sf a");
                if(filter != null && filter.size() > 0)
                {
                    sb0.append(" where ");
                    boolean isFirstCondition = true;
                    for(Iterator mapKey = filter.keySet().iterator(); mapKey.hasNext();)
                    {
                        String fieldOuid = (String)mapKey.next();
                        if(isFirstCondition)
                        {
                            isFirstCondition = false;
                            if(fieldOuid.equals("version.condition.type") && filter.size() == 1)
                                sb0.append(" 1=1 ");
                        } else
                        if(!fieldOuid.equals("version.condition.type"))
                            sb0.append(" and ");
                        DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                        if(dosField != null)
                        {
                            Object conditionObject = filter.get(fieldOuid);
                            String fieldCode = dosField.code;
                            if(fieldCode.equals("md$number"))
                            {
                                String conditionString = (String)conditionObject;
                                conditionString = conditionString.replace('*', '%').replace('?', '_');
                                if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                {
                                    sb0.append("md$number like '");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                } else
                                {
                                    sb0.append("md$number='");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                }
                            } else
                            if(fieldCode.equals("md$des"))
                            {
                                String conditionString = (String)conditionObject;
                                conditionString = conditionString.replace('*', '%').replace('?', '_');
                                if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                {
                                    sb0.append("md$desc like '");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                } else
                                {
                                    sb0.append("md$desc='");
                                    sb0.append(conditionString);
                                    sb0.append("'");
                                }
                            } else
                            if(fieldCode.equals("md$status"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$status='");
                                sb0.append(conditionString);
                                sb0.append("'");
                            } else
                            if(fieldCode.equals("md$user"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$user='");
                                sb0.append(conditionString);
                                sb0.append("'");
                            } else
                            if(fieldCode.equals("md$cdate"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$cdate like to_char(to_date('");
                                sb0.append(conditionString);
                                sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                            } else
                            if(fieldCode.equals("md$mdate"))
                            {
                                String conditionString = (String)conditionObject;
                                sb0.append("md$mdate like to_char(to_date('");
                                sb0.append(conditionString);
                                sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                            } else
                            {
                                fieldCode = fieldCode.toLowerCase().replace(' ', '_');
                                sb0.append(fieldCode);
                                if(conditionObject instanceof Boolean)
                                {
                                    sb0.append("='");
                                    if(((Boolean)conditionObject).booleanValue())
                                        sb0.append('T');
                                    else
                                        sb0.append('F');
                                    sb0.append("'");
                                } else
                                if(conditionObject instanceof Number)
                                {
                                    sb0.append('=');
                                    sb0.append(conditionObject);
                                } else
                                if(dosField.type.byteValue() == 24 || dosField.type.byteValue() == 25)
                                {
                                    String conditionString = (String)conditionObject;
                                    DOSCode.getSQLPhraseForHierarchyCode(conditionString, codeMap, codeItemMap, sb0);
                                } else
                                if(dosField.type.byteValue() == 22 || dosField.type.byteValue() == 21)
                                {
                                    String conditionString = (String)conditionObject;
                                    sb0.append(" like to_char(to_date('");
                                    sb0.append(conditionString);
                                    sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                                } else
                                {
                                    String conditionString = conditionObject.toString();
                                    conditionString = conditionString.replace('*', '%').replace('?', '_');
                                    if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                    {
                                        sb0.append(" like '");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    {
                                        sb0.append("='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    }
                                }
                            }
                            dosField = null;
                        }
                    }

                }
            }
            String oldDatasourceId;
            if(datasourceId == null)
            {
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                oldDatasourceId = datasourceId;
            } else
            {
                oldDatasourceId = datasourceId;
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                if(!datasourceId.equals(oldDatasourceId))
                    willExecute = true;
            }
            if(!listKey.hasNext())
                willExecute = true;
            else
            if(!willExecute)
                sb0.append(" union all ");
            if(willExecute)
            {
                sb0.append(')');
                if(isIDFieldExists)
                    sb0.append(" order by md$number");
                StringBuffer sb = new StringBuffer();
                if(countPerPage > 0)
                {
                    sb.append("select " + resultFieldSb.toString() + " from (");
                    sb.append("select ceil(rn /" + countPerPage + ") as page, " + resultFieldSb.toString() + " from (");
                    sb.append("select rownum as rn, " + resultFieldSb.toString() + " from (");
                    sb.append(sb0);
                    sb.append("))) where page = " + pageNum);
                } else
                {
                    sb.append(sb0);
                }
                xc = dtm.getPooledConnection(oldDatasourceId);
                con = xc.getConnection();
                stat = con.prepareStatement(sb.toString());
                int j = 0;
                ResultSet rs;
                for(rs = stat.executeQuery(); rs.next();)
                {
                    ArrayList oneRow = new ArrayList(fieldCount);
                    oneRow.add(rs.getString(1) + Long.toHexString(rs.getLong(2)));
                    int i = 2;
                    j = 3;
                    for(; i < fieldCount; i++)
                    {
                        if(typeArray[i] == -15)
                            oneRow.add(msr.getStgrepString(locale, rs.getString(j)));
                        else
                        if(typeArray[i] == -14)
                        {
                            String userId = rs.getString(j);
                            HashMap userData = aus.getUser(userId);
                            if(userData != null)
                            {
                                oneRow.add(userData.get("name"));
                                userData.clear();
                                userData = null;
                            } else
                            {
                                oneRow.add(null);
                            }
                        } else
                        if(typeArray[i] == -13)
                        {
                            oneRow.add(null);
                            j--;
                        } else
                        {
                            addListFromResultSet(oneRow, rs, j, typeArray[i]);
                        }
                        j++;
                    }

                    returnList.add(oneRow);
                    oneRow = null;
                }

                rs.close();
                rs = null;
                sb0.setLength(0);
                sb.setLength(0);
                con.commit();
                con.close();
                con = null;
                xc.close();
                xc = null;
                if(listKey.hasNext())
                    sb0.append("select " + resultFieldSb.toString() + " from (");
                willExecute = false;
            }
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                e.printStackTrace();
                throw new IIPRequestException(e.toString());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new IIPRequestException(e.toString());
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
_L1:
        if(listKey.hasNext()) goto _L3; else goto _L2
_L2:
        typeArray = (int[])null;
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList list(String ouid)
        throws IIPRequestException
    {
        return list(ouid, null, null);
    }

    public ArrayList list(String ouid, HashMap filter)
        throws IIPRequestException
    {
        return list(ouid, null, filter);
    }

    public ArrayList list(String ouid, ArrayList fields)
        throws IIPRequestException
    {
        return list(ouid, fields, null);
    }

    public void link(String end1, String end2)
        throws IIPRequestException
    {
        int rows;
        if(Utils.isNullString(end1))
            throw new IIPRequestException("Miss out mandatory parameter: end1");
        if(Utils.isNullString(end2))
            throw new IIPRequestException("Miss out mandatory parameter: end2");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String class1Ouid = getClassOuid(end1);
        String class2Ouid = getClassOuid(end2);
        ArrayList superClassList1 = null;
        ArrayList superClassList2 = null;
        DOSClass superClass = null;
        DOSClass dosClass1 = (DOSClass)classMap.get(class1Ouid);
        if(dosClass1 == null)
            throw new IIPRequestException("End1 class not exists. (ouid:" + class1Ouid + ")");
        DOSClass dosClass2 = (DOSClass)classMap.get(class2Ouid);
        if(dosClass2 == null)
            throw new IIPRequestException("End2 class not exists. (ouid:" + class2Ouid + ")");
        LinkedList end1List = dosClass1.end1List;
        if(end1List == null)
        {
            end1List = new LinkedList();
            if(dosClass1.end1List != null)
                end1List.addAll(dosClass1.end1List);
        }
        superClassList1 = listAllSuperClassInternal(class1Ouid);
        superClassList2 = listAllSuperClassInternal(class2Ouid);
        if(superClassList2 == null)
            superClassList2 = new ArrayList();
        superClassList2.add(0, dosClass2);
        if(superClassList1 != null)
        {
            Iterator superKey;
            for(superKey = superClassList1.iterator(); superKey.hasNext();)
            {
                superClass = (DOSClass)superKey.next();
                if(superClass != null && superClass.end1List != null && superClass.end1List.size() > 0)
                {
                    if(end1List == null)
                        end1List = new LinkedList();
                    end1List.addAll(superClass.end1List);
                }
                superClass = null;
            }

            superKey = null;
            superClassList1 = null;
        }
        if(end1List == null || end1List.size() == 0)
            throw new IIPRequestException("Association not exists.");
        DOSAssociation dosAssociation = null;
        Iterator listKey = end1List.iterator();
        while(listKey.hasNext()) 
        {
            dosAssociation = (DOSAssociation)listKey.next();
            if(dosAssociation._class != null)
            {
                dosAssociation = null;
                continue;
            }
            if(dosAssociation.end2_class != null && superClassList2.contains(dosAssociation.end2_class))
                break;
            dosAssociation = null;
        }
        superClassList2 = null;
        if(dosAssociation == null)
            throw new IIPRequestException("Association not exists.");
        rows = 0;
        String datasourceId = dosAssociation.datasourceId;
        if(datasourceId == null)
            datasourceId = "default";
        String classCode = dosAssociation.code.toLowerCase().replace(' ', '_') + "$as ";
        int maxLink1 = Utils.getInt(dosAssociation.end1_multiplicity_to);
        int maxLink2 = Utils.getInt(dosAssociation.end2_multiplicity_to);
        int count = 0;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            String ouid1 = end1.substring(end1.indexOf('@') + 1);
            String ouid2 = end2.substring(end2.indexOf('@') + 1);
            if(maxLink1 > 0)
            {
                stat = con.prepareStatement("select count(*) from " + classCode + "where as$end2=? ");
                stat.setLong(1, Utils.convertOuidToLong(ouid2));
                rs = stat.executeQuery();
                if(rs.next())
                    count = rs.getInt(1);
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                if(count >= maxLink1)
                    throw new IIPRequestException("Maximum multiplicity exceed. (Association: " + dosAssociation.name + ")");
            }
            if(maxLink2 > 0)
            {
                stat = con.prepareStatement("select count(*) from " + classCode + "where as$end1=? ");
                stat.setLong(1, Utils.convertOuidToLong(ouid1));
                rs = stat.executeQuery();
                if(rs.next())
                    count = rs.getInt(1);
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                if(count >= maxLink2)
                    throw new IIPRequestException("Maximum multiplicity exceed. (Association: " + dosAssociation.name + ")");
            }
            stat = con.prepareStatement("insert into " + classCode + "(as$end1,as$end2) " + "select ?,? " + "from dual " + "where not exists (select '1' " + "from " + classCode + "where as$end1=? and " + "as$end2=?" + ")");
            stat.setLong(1, Utils.convertOuidToLong(ouid1));
            stat.setLong(2, Utils.convertOuidToLong(ouid2));
            stat.setLong(3, Utils.convertOuidToLong(ouid1));
            stat.setLong(4, Utils.convertOuidToLong(ouid2));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
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
        LinkedList end1List = null;
        DOSClass dosClass1 = null;
        DOSClass dosClass2 = null;
        if(rows == 0)
            throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B405", "Association duplicated. Not asociated."));
        else
            return;
    }

    public void unlink(String end1, String end2)
        throws IIPRequestException
    {
        int rows;
        if(Utils.isNullString(end1))
            throw new IIPRequestException("Miss out mandatory parameter: end1");
        if(Utils.isNullString(end2))
            throw new IIPRequestException("Miss out mandatory parameter: end2");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String class1Ouid = getClassOuid(end1);
        String class2Ouid = getClassOuid(end2);
        ArrayList superClassList1 = null;
        ArrayList superClassList2 = null;
        DOSClass superClass = null;
        DOSClass dosClass1 = (DOSClass)classMap.get(class1Ouid);
        if(dosClass1 == null)
            throw new IIPRequestException("End1 class not exists. (ouid:" + class1Ouid + ")");
        DOSClass dosClass2 = (DOSClass)classMap.get(class2Ouid);
        if(dosClass2 == null)
            throw new IIPRequestException("End2 class not exists. (ouid:" + class2Ouid + ")");
        LinkedList end1List = dosClass1.end1List;
        if(end1List == null)
        {
            end1List = new LinkedList();
            if(dosClass1.end1List != null)
                end1List = dosClass1.end1List;
        }
        superClassList1 = listAllSuperClassInternal(class1Ouid);
        superClassList2 = listAllSuperClassInternal(class2Ouid);
        if(superClassList2 == null)
            superClassList2 = new ArrayList();
        superClassList2.add(0, dosClass2);
        if(superClassList1 != null)
        {
            Iterator superKey;
            for(superKey = superClassList1.iterator(); superKey.hasNext();)
            {
                superClass = (DOSClass)superKey.next();
                if(superClass != null && superClass.end1List != null && superClass.end1List.size() > 0)
                {
                    if(end1List == null)
                        end1List = new LinkedList();
                    end1List.addAll(superClass.end1List);
                }
                superClass = null;
            }

            superKey = null;
            superClassList1 = null;
        }
        if(end1List == null || end1List.size() == 0)
            throw new IIPRequestException("Association not exists.");
        DOSAssociation dosAssociation = null;
        for(Iterator listKey = end1List.iterator(); listKey.hasNext();)
        {
            dosAssociation = (DOSAssociation)listKey.next();
            if(dosAssociation.end2_class != null && superClassList2.contains(dosAssociation.end2_class))
                break;
            dosAssociation = null;
        }

        if(dosAssociation == null)
            throw new IIPRequestException("Association not exists.");
        rows = 0;
        String datasourceId = dosAssociation.datasourceId;
        if(datasourceId == null)
            datasourceId = "default";
        String classCode = null;
        if(dosAssociation._class != null)
            classCode = dosAssociation.code.toLowerCase().replace(' ', '_') + "$ac ";
        else
            classCode = dosAssociation.code.toLowerCase().replace(' ', '_') + "$as ";
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            String ouid1 = end1.substring(end1.indexOf('@') + 1);
            String ouid2 = end2.substring(end2.indexOf('@') + 1);
            stat = con.prepareStatement("delete from " + classCode + "where as$end1=? and " + "as$end2=? ");
            stat.setLong(1, Utils.convertOuidToLong(ouid1));
            stat.setLong(2, Utils.convertOuidToLong(ouid2));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
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
        LinkedList end1List = null;
        DOSClass dosClass1 = null;
        DOSClass dosClass2 = null;
        if(rows == 0)
            throw new IIPRequestException("Association not exists.");
        else
            return;
    }

    public String link(String end1, String end2, DOSChangeable object)
        throws IIPRequestException
    {
        String ouid0;
        boolean dupSequence;
        if(Utils.isNullString(end1))
            throw new IIPRequestException("Miss out mandatory parameter: end1");
        if(Utils.isNullString(end2))
            throw new IIPRequestException("Miss out mandatory parameter: end2");
        if(object == null || object.getValueMap() == null || object.getValueMap().size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter: object");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String class1Ouid = getClassOuid(end1);
        String class2Ouid = getClassOuid(end2);
        String number = null;
        ouid0 = null;
        ArrayList superClassList1 = null;
        ArrayList superClassList2 = null;
        DOSClass superClass = null;
        dupSequence = false;
        DOSClass dosClass1 = (DOSClass)classMap.get(class1Ouid);
        if(dosClass1 == null)
            throw new IIPRequestException("End1 class not exists. (ouid:" + class1Ouid + ")");
        DOSClass dosClass2 = (DOSClass)classMap.get(class2Ouid);
        if(dosClass2 == null)
            throw new IIPRequestException("End2 class not exists. (ouid:" + class2Ouid + ")");
        LinkedList end1List = dosClass1.end1List;
        if(end1List == null)
        {
            end1List = new LinkedList();
            if(dosClass1.end1List != null)
                end1List = dosClass1.end1List;
        }
        superClassList1 = listAllSuperClassInternal(class1Ouid);
        superClassList2 = listAllSuperClassInternal(class2Ouid);
        if(superClassList2 == null)
            superClassList2 = new ArrayList();
        superClassList2.add(0, dosClass2);
        if(superClassList1 != null)
        {
            for(Iterator superKey = superClassList1.iterator(); superKey.hasNext();)
            {
                superClass = (DOSClass)superKey.next();
                if(superClass != null && superClass.end1List != null && superClass.end1List.size() > 0)
                {
                    if(end1List == null)
                        end1List = new LinkedList();
                    end1List.addAll(superClass.end1List);
                }
                superClass = null;
            }

            Object obj = null;
        }
        superClassList1 = null;
        if(end1List == null || end1List.size() == 0)
            throw new IIPRequestException("Association not exists.");
        DOSAssociation dosAssociation = null;
        Iterator listKey = end1List.iterator();
        while(listKey.hasNext()) 
        {
            dosAssociation = (DOSAssociation)listKey.next();
            if(dosAssociation._class == null)
            {
                dosAssociation = null;
                continue;
            }
            if(dosAssociation.end2_class != null && superClassList2.contains(dosAssociation.end2_class))
                break;
            dosAssociation = null;
        }
        listKey = null;
        superClassList2 = null;
        if(dosAssociation == null)
            throw new IIPRequestException("Association not exists.");
        int rows = 0;
        String datasourceId = dosAssociation.datasourceId;
        if(datasourceId == null)
            datasourceId = "default";
        String classCode = dosAssociation.code.toLowerCase().replace(' ', '_');
        int maxLink1 = Utils.getInt(dosAssociation.end1_multiplicity_to);
        int maxLink2 = Utils.getInt(dosAssociation.end2_multiplicity_to);
        int count = 0;
        LinkedList ouidList = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            String ouid1 = end1.substring(end1.indexOf('@') + 1);
            String ouid2 = end2.substring(end2.indexOf('@') + 1);
            if(maxLink2 > 0)
            {
                stat = con.prepareStatement("select count(*) from " + classCode + "$ac " + "where as$end1=? ");
                stat.setLong(1, Utils.convertOuidToLong(ouid1));
                rs = stat.executeQuery();
                if(rs.next())
                    count = rs.getInt(1);
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                if(count >= maxLink2)
                    throw new IIPRequestException("Maximum multiplicity exceed. (Association: " + dosAssociation.name + ")");
            }
            number = (String)object.get("md$sequence");
            stat = con.prepareStatement("select '1' from " + classCode + "$ac " + "where as$end1=? and md$sequence=? and as$end2=? ");
            stat.setLong(1, Utils.convertOuidToLong(ouid1));
            stat.setString(2, number);
            stat.setLong(3, Utils.convertOuidToLong(ouid2));
            rs = stat.executeQuery();
            if(rs.next())
                ouid0 = "";
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            con = null;
            if(ouid0 == null)
            {
                if(!(dupSequence = checkDuplicateSequence(Long.toHexString(dosAssociation.OUID), end1, end2, number)))
                {
                    object.put("as$datasourceId", datasourceId);
                    object.put("as$code", classCode);
                    object.put("as$end1", ouid1);
                    object.put("as$end2", ouid2);
                    ouid0 = add(object);
                }
            } else
            {
                ouid0 = null;
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
        LinkedList end1List = null;
        DOSClass dosClass1 = null;
        DOSClass dosClass2 = null;
        if(dupSequence)
            throw new IIPRequestException("Sequence duplicated. Not associated.");
        if(Utils.isNullString(ouid0))
            throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B405", "Association duplicated. Not asociated."));
        else
            return ouid0;
    }

    public void unlink(String end1, String end2, String assoOuid)
        throws IIPRequestException
    {
        int rows;
        if(Utils.isNullString(end1))
            throw new IIPRequestException("Miss out mandatory parameter: end1");
        if(Utils.isNullString(end2))
            throw new IIPRequestException("Miss out mandatory parameter: end2");
        if(Utils.isNullString(assoOuid))
            throw new IIPRequestException("Miss out mandatory parameter: assoOuid");
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        String class1Ouid = getClassOuid(end1);
        String class2Ouid = getClassOuid(end2);
        String assoClassOuid = getClassOuid(assoOuid);
        ArrayList superClassList1 = null;
        ArrayList superClassList2 = null;
        ArrayList superClassList3 = null;
        DOSClass superClass = null;
        DOSClass dosClass1 = (DOSClass)classMap.get(class1Ouid);
        if(dosClass1 == null)
            throw new IIPRequestException("End1 class not exists. (ouid:" + class1Ouid + ")");
        DOSClass dosClass2 = (DOSClass)classMap.get(class2Ouid);
        if(dosClass2 == null)
            throw new IIPRequestException("End2 class not exists. (ouid:" + class2Ouid + ")");
        DOSClass dosClass3 = (DOSClass)classMap.get(assoClassOuid);
        if(dosClass3 == null)
            throw new IIPRequestException("Association class not exists. (ouid:" + assoClassOuid + ")");
        LinkedList end1List = dosClass1.end1List;
        if(end1List == null)
        {
            end1List = new LinkedList();
            if(dosClass1.end1List != null)
                end1List = dosClass1.end1List;
        }
        superClassList1 = listAllSuperClassInternal(class1Ouid);
        superClassList2 = listAllSuperClassInternal(class2Ouid);
        superClassList3 = listAllSuperClassInternal(assoClassOuid);
        if(superClassList2 == null)
            superClassList2 = new ArrayList();
        superClassList2.add(0, dosClass2);
        if(superClassList3 == null)
            superClassList3 = new ArrayList();
        superClassList3.add(0, dosClass3);
        if(superClassList1 != null)
        {
            Iterator superKey;
            for(superKey = superClassList1.iterator(); superKey.hasNext();)
            {
                superClass = (DOSClass)superKey.next();
                if(superClass != null && superClass.end1List != null && superClass.end1List.size() > 0)
                {
                    if(end1List == null)
                        end1List = new LinkedList();
                    end1List.addAll(superClass.end1List);
                }
                superClass = null;
            }

            superKey = null;
            superClassList1 = null;
        }
        if(end1List == null || end1List.size() == 0)
            throw new IIPRequestException("Association not exists.");
        DOSAssociation dosAssociation = null;
        for(Iterator listKey = end1List.iterator(); listKey.hasNext();)
        {
            dosAssociation = (DOSAssociation)listKey.next();
            if(dosAssociation.end2_class != null && superClassList2.contains(dosAssociation.end2_class) && superClassList3.contains(dosAssociation._class))
                break;
            dosAssociation = null;
        }

        if(dosAssociation == null)
            throw new IIPRequestException("Association not exists.");
        rows = 0;
        String datasourceId = dosAssociation.datasourceId;
        if(datasourceId == null)
            datasourceId = "default";
        String classCode = null;
        if(dosAssociation._class != null)
            classCode = dosAssociation.code.toLowerCase().replace(' ', '_') + "$ac ";
        else
            throw new IIPRequestException("Association not exists.");
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            String ouid1 = end1.substring(end1.indexOf('@') + 1);
            String ouid2 = end2.substring(end2.indexOf('@') + 1);
            String ouid3 = assoOuid.substring(assoOuid.indexOf('@') + 1);
            stat = con.prepareStatement("delete from " + classCode + "where as$end1=? and " + "as$end2=? and " + "sf$ouid=? ");
            stat.setLong(1, Utils.convertOuidToLong(ouid1));
            stat.setLong(2, Utils.convertOuidToLong(ouid2));
            stat.setLong(3, Utils.convertOuidToLong(ouid3));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
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
        LinkedList end1List = null;
        DOSClass dosClass1 = null;
        DOSClass dosClass2 = null;
        if(rows == 0)
            throw new IIPRequestException("Association not exists.");
        else
            return;
    }

    private void listLinkHelper(ArrayList list1, HashMap assoMap, HashMap listCache, String ouid, ArrayList fields, HashMap filter, HashMap targetAssoMap, 
            boolean doAssociation, boolean doComposition, boolean isFrom)
        throws SQLException, IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSAssociation dosAssociation = null;
        DOSClass assoClass = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList fieldList = null;
        ArrayList assoFieldList = null;
        ArrayList oneRow = null;
        ArrayList oneRowList = null;
        Iterator listKey = null;
        Iterator fieldKey = null;
        Iterator mapKey = null;
        StringBuffer sb1 = null;
        String classCodeAsso = null;
        String fieldOuid = null;
        String datasourceIdAsso = null;
        String leafOuid = null;
        boolean isAssociationClass = false;
        int i = 0;
        int fieldCount = 0;
        int rowCount = 0;
        int typeArray4Asso[] = (int[])null;
        Object conditionObject = null;
        String conditionString = null;
        String fieldCode = null;
        if(fields != null)
            fieldCount = fields.size() + 2;
        typeArray4Asso = new int[fieldCount];
        for(listKey = targetAssoMap.keySet().iterator(); listKey.hasNext();)
        {
            leafOuid = (String)listKey.next();
            dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
            dosClass = (DOSClass)classMap.get(leafOuid);
            if(dosClass != null)
            {
                datasourceIdAsso = dosAssociation.datasourceId;
                if(Utils.isNullString(datasourceIdAsso))
                    datasourceIdAsso = "default";
                fieldList = listFieldInClassInternal(Long.toHexString(dosClass.OUID));
                assoClass = dosAssociation._class;
                if(assoClass != null)
                {
                    assoFieldList = listFieldInClassInternal(Long.toHexString(assoClass.OUID));
                    isAssociationClass = true;
                } else
                {
                    isAssociationClass = false;
                }
                classCodeAsso = dosAssociation.code.toLowerCase().replace(' ', '_');
                if(!isAssociationClass && !listCache.containsKey(classCodeAsso + ouid))
                {
                    xc = dtm.getPooledConnection(datasourceIdAsso);
                    con = xc.getConnection();
                    if(isFrom || doAssociation && dosAssociation.type.equals(ASSOCIATION))
                    {
                        stat = con.prepareStatement("select as$end2 from " + classCodeAsso + "$as " + "where as$end1=? " + "order by as$end2");
                        stat.setLong(1, Utils.getRealLongOuid(ouid));
                        rs = stat.executeQuery();
                        for(rowCount = 0; rs.next(); rowCount++)
                            list1.add(rs.getString(1));

                        rs.close();
                        rs = null;
                        stat.close();
                        stat = null;
                    }
                    if(!isFrom || doAssociation && dosAssociation.type.equals(ASSOCIATION))
                    {
                        stat = con.prepareStatement("select as$end1 from " + classCodeAsso + "$as " + "where as$end2=? " + "order by as$end1");
                        stat.setLong(1, Utils.getRealLongOuid(ouid));
                        for(rs = stat.executeQuery(); rs.next();)
                        {
                            list1.add(rs.getString(1));
                            rowCount++;
                        }

                        rs.close();
                        rs = null;
                        stat.close();
                        stat = null;
                    }
                    if(rowCount == 0)
                        listKey.remove();
                    con.commit();
                    con.close();
                    con = null;
                    xc.close();
                    xc = null;
                } else
                if(!listCache.containsKey(classCodeAsso + ouid))
                {
                    xc = dtm.getPooledConnection(datasourceIdAsso);
                    con = xc.getConnection();
                    sb1 = new StringBuffer();
                    if(isFrom || doAssociation && dosAssociation.type.equals(ASSOCIATION))
                    {
                        sb1.append("select as$end2,sf$ouid");
                        i = 1;
                        if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                            for(fieldKey = fields.iterator(); fieldKey.hasNext();)
                            {
                                i++;
                                fieldOuid = (String)fieldKey.next();
                                dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(dosField == null)
                                    typeArray4Asso[i] = -1;
                                else
                                if(dosField.code.equals("md$sequence"))
                                {
                                    sb1.append(",md$sequence");
                                    typeArray4Asso[i] = 13;
                                } else
                                if(dosField == null || assoFieldList == null || !assoFieldList.contains(dosField))
                                {
                                    dosField = null;
                                    typeArray4Asso[i] = -1;
                                } else
                                {
                                    typeArray4Asso[i] = Utils.getByte(dosField.type);
                                    sb1.append(',');
                                    determineFoundationFieldTypes(typeArray4Asso, i, dosField.code);
                                    StringBuffer selectClause = new StringBuffer();
                                    addStringBufferFromField(sb1, typeArray4Asso, i, dosField, selectClause);
                                }
                            }

                        sb1.append(" from ");
                        sb1.append(classCodeAsso);
                        sb1.append("$ac ");
                        sb1.append("where as$end1=? ");
                        if(filter != null && filter.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                            for(mapKey = filter.keySet().iterator(); mapKey.hasNext();)
                            {
                                fieldOuid = (String)mapKey.next();
                                conditionObject = filter.get(fieldOuid);
                                dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(dosField != null)
                                    if(dosField.code.equals("md$sequence"))
                                    {
                                        conditionString = (String)conditionObject;
                                        sb1.append(" and ");
                                        sb1.append("md$sequence='");
                                        sb1.append(conditionString);
                                        sb1.append("'");
                                    } else
                                    if(dosField != null && assoFieldList == null || assoFieldList.contains(dosField))
                                    {
                                        fieldCode = dosField.code;
                                        fieldCode = fieldCode.toLowerCase().replace(' ', '_');
                                        sb1.append(" and ");
                                        sb1.append(fieldCode);
                                        if(conditionObject instanceof Boolean)
                                        {
                                            sb1.append("='");
                                            if(((Boolean)conditionObject).booleanValue())
                                                sb1.append('T');
                                            else
                                                sb1.append('F');
                                            sb1.append("'");
                                        } else
                                        if(conditionObject instanceof Number)
                                        {
                                            sb1.append('=');
                                            sb1.append(conditionObject);
                                        } else
                                        if(dosField.type.byteValue() == 24 || dosField.type.byteValue() == 25)
                                        {
                                            conditionString = (String)conditionObject;
                                            sb1.append('=');
                                            sb1.append(Utils.convertOuidToLong(conditionString));
                                        } else
                                        {
                                            conditionString = conditionObject.toString();
                                            conditionString = conditionString.replace('*', '%').replace('?', '_');
                                            if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                            {
                                                sb1.append(" like '");
                                                sb1.append(conditionString);
                                                sb1.append("'");
                                            } else
                                            {
                                                sb1.append("='");
                                                sb1.append(conditionString);
                                                sb1.append("'");
                                            }
                                        }
                                        dosField = null;
                                    }
                            }

                        sb1.append(" order by md$sequence,as$end2");
                        stat = con.prepareStatement(sb1.toString());
                        stat.setLong(1, Utils.getRealLongOuid(ouid));
                        rs = stat.executeQuery();
                        rowCount = 0;
                        while(rs.next()) 
                        {
                            list1.add(rs.getString(1));
                            rowCount++;
                            oneRow = new ArrayList();
                            if(typeArray4Asso != null && typeArray4Asso.length > 0)
                            {
                                int y = 2;
                                for(int z = 1; z < typeArray4Asso.length; z++)
                                    if(typeArray4Asso[z] == -1)
                                    {
                                        oneRow.add(null);
                                    } else
                                    {
                                        addListFromResultSet(oneRow, rs, y, typeArray4Asso[z]);
                                        y++;
                                    }

                            } else
                            {
                                oneRow.add(rs.getString(2));
                            }
                            oneRow.set(0, classCodeAsso + "$ac@" + Long.toHexString((new Long((String)oneRow.get(0))).longValue()));
                            oneRowList = (ArrayList)assoMap.get(list1.get(list1.size() - 1));
                            if(oneRowList == null)
                            {
                                oneRowList = new ArrayList();
                                assoMap.put(list1.get(list1.size() - 1), oneRowList);
                            }
                            oneRowList.add(oneRow.clone());
                            oneRow.clear();
                            oneRow = null;
                            oneRowList = null;
                        }
                        rs.close();
                        rs = null;
                        stat.close();
                        stat = null;
                        sb1.setLength(0);
                        sb1 = null;
                    }
                    if(!isFrom || doAssociation && dosAssociation.type.equals(ASSOCIATION))
                    {
                        sb1 = new StringBuffer();
                        sb1.append("select as$end1,sf$ouid");
                        i = 1;
                        if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                            for(fieldKey = fields.iterator(); fieldKey.hasNext();)
                            {
                                i++;
                                fieldOuid = (String)fieldKey.next();
                                dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(dosField == null)
                                    typeArray4Asso[i] = -1;
                                else
                                if(dosField.code.equals("md$sequence"))
                                {
                                    sb1.append(",md$sequence");
                                    typeArray4Asso[i] = 13;
                                } else
                                if(dosField == null || assoFieldList == null || !assoFieldList.contains(dosField))
                                {
                                    dosField = null;
                                    typeArray4Asso[i] = -1;
                                } else
                                {
                                    typeArray4Asso[i] = Utils.getByte(dosField.type);
                                    sb1.append(',');
                                    determineFoundationFieldTypes(typeArray4Asso, i, dosField.code);
                                    StringBuffer selectClause = new StringBuffer();
                                    addStringBufferFromField(sb1, typeArray4Asso, i, dosField, selectClause);
                                }
                            }

                        sb1.append(" from ");
                        sb1.append(classCodeAsso);
                        sb1.append("$ac ");
                        sb1.append("where as$end2=? ");
                        if(filter != null && filter.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                            for(mapKey = filter.keySet().iterator(); mapKey.hasNext();)
                            {
                                fieldOuid = (String)mapKey.next();
                                conditionObject = filter.get(fieldOuid);
                                dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(dosField != null)
                                    if(dosField.code.equals("md$sequence"))
                                    {
                                        conditionString = (String)conditionObject;
                                        sb1.append(" and ");
                                        sb1.append("md$sequence='");
                                        sb1.append(conditionString);
                                        sb1.append("'");
                                    } else
                                    if(dosField != null && assoFieldList == null || assoFieldList.contains(dosField))
                                    {
                                        fieldCode = dosField.code;
                                        fieldCode = fieldCode.toLowerCase().replace(' ', '_');
                                        sb1.append(" and ");
                                        sb1.append(fieldCode);
                                        if(conditionObject instanceof Boolean)
                                        {
                                            sb1.append("='");
                                            if(((Boolean)conditionObject).booleanValue())
                                                sb1.append('T');
                                            else
                                                sb1.append('F');
                                            sb1.append("'");
                                        } else
                                        if(conditionObject instanceof Number)
                                        {
                                            sb1.append('=');
                                            sb1.append(conditionObject);
                                        } else
                                        if(dosField.type.byteValue() == 24 || dosField.type.byteValue() == 25)
                                        {
                                            conditionString = (String)conditionObject;
                                            sb1.append('=');
                                            sb1.append(Utils.convertOuidToLong(conditionString));
                                        } else
                                        {
                                            conditionString = conditionObject.toString();
                                            conditionString = conditionString.replace('*', '%').replace('?', '_');
                                            if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                            {
                                                sb1.append(" like '");
                                                sb1.append(conditionString);
                                                sb1.append("'");
                                            } else
                                            {
                                                sb1.append("='");
                                                sb1.append(conditionString);
                                                sb1.append("'");
                                            }
                                        }
                                        dosField = null;
                                    }
                            }

                        sb1.append(" order by md$sequence,as$end1");
                        stat = con.prepareStatement(sb1.toString());
                        stat.setLong(1, Utils.getRealLongOuid(ouid));
                        for(rs = stat.executeQuery(); rs.next();)
                        {
                            list1.add(rs.getString(1));
                            rowCount++;
                            oneRow = new ArrayList();
                            if(typeArray4Asso != null && typeArray4Asso.length > 0)
                            {
                                int y = 2;
                                for(int z = 1; z < typeArray4Asso.length; z++)
                                    if(typeArray4Asso[z] == -1)
                                    {
                                        oneRow.add(null);
                                    } else
                                    {
                                        addListFromResultSet(oneRow, rs, y, typeArray4Asso[z]);
                                        y++;
                                    }

                            } else
                            {
                                oneRow.add(rs.getString(2));
                            }
                            oneRow.set(0, classCodeAsso + "$ac@" + Long.toHexString((new Long((String)oneRow.get(0))).longValue()));
                            oneRowList = (ArrayList)assoMap.get(list1.get(list1.size() - 1));
                            if(oneRowList == null)
                            {
                                oneRowList = new ArrayList();
                                assoMap.put(list1.get(list1.size() - 1), oneRowList);
                            }
                            oneRowList.add(oneRow.clone());
                            oneRow.clear();
                            oneRow = null;
                            oneRowList = null;
                        }

                        rs.close();
                        rs = null;
                        stat.close();
                        stat = null;
                        sb1.setLength(0);
                        sb1 = null;
                    }
                    if(rowCount == 0)
                        listKey.remove();
                    con.commit();
                    con.close();
                    con = null;
                    xc.close();
                    xc = null;
                }
                if(!listCache.containsKey(classCodeAsso + ouid))
                    listCache.put(classCodeAsso + ouid, list1);
            }
        }

        listKey = null;
    }

    public ArrayList listLink(String ouid, boolean isFrom, ArrayList fields, HashMap filter)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        DOSAssociation dosAssociation;
        ArrayList returnList;
        HashMap assoMap;
        ArrayList fieldList;
        HashMap listCache;
        Iterator listKey;
        String datasourceId;
        StringBuffer sb0;
        int fieldCount;
        HashMap typeArrayMap;
        int sqlCount;
        boolean isFirstSQL;
        boolean willExecute;
        String locale;
        HashMap targetAssoMap;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass startClass = null;
        DOSClass dosClass = null;
        DOSClass leafDosClass = null;
        DOSClass superClass = null;
        DOSField dosField = null;
        dosAssociation = null;
        ArrayList leafClassList = null;
        returnList = null;
        ArrayList oneRow = null;
        ArrayList oneRow2 = null;
        LinkedList list0 = null;
        ArrayList list1 = null;
        ArrayList list2 = null;
        ArrayList list3 = null;
        assoMap = null;
        fieldList = null;
        ArrayList tempList = null;
        HashMap userData = null;
        HashMap filterMap = null;
        listCache = null;
        listKey = null;
        Iterator mapKey = null;
        Iterator fieldKey = null;
        Iterator list1Key = null;
        Iterator list2Key = null;
        Iterator leafClassKey = null;
        datasourceId = null;
        String oldDatasourceId = null;
        String classOuid = null;
        String classCode = null;
        String classCodeAsso = null;
        String fieldOuid = null;
        String fieldCode = null;
        String userId = null;
        String conditionString = null;
        String searchType = null;
        String searchTypeDateFrom = null;
        String searchTypeDateTo = null;
        String leafOuid = null;
        String tempString = null;
        sb0 = null;
        Object conditionObject = null;
        boolean versionable = false;
        int i = 0;
        fieldCount = 0;
        int sequenceField = 0;
        typeArrayMap = null;
        int rowCount = 0;
        int assoCount = 0;
        sqlCount = 0;
        int multiplicityFrom = 0;
        int multiplicityTo = 0;
        boolean isFirstCondition = false;
        isFirstSQL = true;
        boolean isAssociationClass = false;
        boolean doComposition = true;
        boolean doAssociation = false;
        boolean hasCollection = false;
        willExecute = false;
        boolean willGenerateSQL = false;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        startClass = (DOSClass)classMap.get(classOuid);
        if(startClass == null)
        {
            startClass = null;
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        }
        locale = (String)getClientContext().get("locale");
        if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
        {
            fieldCount = fields.size() + 2;
            for(listKey = fields.iterator(); listKey.hasNext();)
            {
                fieldOuid = (String)listKey.next();
                if(!fieldMap.containsKey(fieldOuid) && !reservedFields.contains(fieldOuid))
                {
                    listKey = null;
                    throw new IIPRequestException("Result field not exists. (ouid: " + fieldOuid + ")");
                }
            }

            listKey = null;
        }
        if(filter != null)
        {
            tempString = (String)filter.get("list.mode");
            if(!Utils.isNullString(tempString))
                if(tempString.equals("association"))
                {
                    doAssociation = true;
                    doComposition = false;
                } else
                if(tempString.equals("composition"))
                {
                    doAssociation = false;
                    doComposition = true;
                } else
                if(tempString.equals("all"))
                {
                    doAssociation = true;
                    doComposition = true;
                }
            filter.remove("list.mode");
            if(filter.size() == 0)
                filter = null;
        } else
        {
            tempString = "composition";
            doAssociation = false;
            doComposition = true;
        }
        targetAssoMap = listEffectiveAssociationInternal(classOuid, new Boolean(isFrom), tempString);
        if(targetAssoMap == null)
            return null;
        String associationOuid = null;
        if(doAssociation && filter != null)
        {
            associationOuid = (String)filter.get("ouid@association.class");
            if(!Utils.isNullString(associationOuid))
            {
                filter.remove("ouid@association.class");
                for(listKey = targetAssoMap.keySet().iterator(); listKey.hasNext();)
                {
                    leafOuid = (String)listKey.next();
                    dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
                    if(!Long.toHexString(dosAssociation.OUID).equals(associationOuid))
                        listKey.remove();
                }

                listKey = null;
            }
        }
        ouid = ouid.substring(ouid.indexOf('@') + 1);
        returnList = new ArrayList();
        list1 = new ArrayList();
        assoMap = new HashMap();
        listCache = new HashMap();
        try
        {
            listLinkHelper(list1, assoMap, listCache, ouid, fields, filter, targetAssoMap, doAssociation, doComposition, isFrom);
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        sb0 = new StringBuffer();
        sb0.append("select * from (");
        typeArrayMap = new HashMap();
        listKey = targetAssoMap.keySet().iterator();
          goto _L1
_L3:
        String leafOuid = (String)listKey.next();
        dosAssociation = (DOSAssociation)targetAssoMap.get(leafOuid);
        DOSClass dosClass = (DOSClass)classMap.get(leafOuid);
        if(dosClass == null)
            continue; /* Loop/switch isn't completed */
        String classCodeAsso = dosAssociation.code.toLowerCase().replace(' ', '_');
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        if(fieldList != null)
            fieldList = null;
        fieldList = listFieldInClassInternal(Long.toHexString(dosClass.OUID));
        try
        {
            ArrayList list1 = (ArrayList)listCache.get(classCodeAsso + ouid);
            boolean willGenerateSQL = list1.size() > 0;
            if(list1.size() > 0 && willGenerateSQL)
            {
                sqlCount++;
                if(isFirstSQL)
                    isFirstSQL = false;
                else
                    sb0.append(" union all ");
                if(versionable)
                {
                    sb0.append("select '");
                    sb0.append(classCode);
                    sb0.append("$vf@',vf$ouid");
                    int i = 1;
                    if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                    {
                        int typeArray[] = new int[fieldCount];
                        for(Iterator fieldKey = fields.iterator(); fieldKey.hasNext();)
                        {
                            i++;
                            String fieldOuid = (String)fieldKey.next();
                            DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                            if(dosField == null)
                            {
                                sb0.append(",null as x$");
                                sb0.append(fieldOuid);
                                typeArray[i] = -1;
                            } else
                            if(dosField.code.equals("md$sequence"))
                            {
                                sb0.append(",null");
                                int sequenceField = i;
                                typeArray[i] = -1;
                            } else
                            {
                                int multiplicityFrom = Utils.getInt(dosField.multiplicity_from);
                                int multiplicityTo = Utils.getInt(dosField.multiplicity_to);
                                boolean hasCollection;
                                if(multiplicityFrom < multiplicityTo)
                                {
                                    if(multiplicityTo > 1)
                                        hasCollection = true;
                                    else
                                        hasCollection = multiplicityTo - multiplicityFrom > 1;
                                } else
                                {
                                    hasCollection = false;
                                }
                                if(hasCollection)
                                {
                                    typeArray[i] = -13;
                                } else
                                {
                                    DOSField tempDosField = null;
                                    for(Iterator tempFieldKey = fieldList.iterator(); tempFieldKey.hasNext(); tempDosField = null)
                                    {
                                        tempDosField = (DOSField)tempFieldKey.next();
                                        if(!tempDosField.name.equals(dosField.name))
                                            continue;
                                        typeArray[i] = Utils.getByte(dosField.type);
                                        break;
                                    }

                                    if(tempDosField == null)
                                        typeArray[i] = -1;
                                }
                                sb0.append(',');
                                determineFoundationFieldTypes(typeArray, i, dosField.code);
                                StringBuffer selectClause = new StringBuffer();
                                addStringBufferFromField(sb0, typeArray, i, dosField, selectClause);
                            }
                        }

                        typeArrayMap.put(leafOuid, typeArray);
                        typeArray = (int[])null;
                    } else
                    {
                        sb0.append(", md$number");
                    }
                    sb0.append(" from ");
                    sb0.append(classCode);
                    if(filter != null && filter.size() > 0)
                    {
                        String searchType = (String)filter.get("version.condition.type");
                        String searchTypeDateFrom = (String)filter.get("version.condition.date.from");
                        String searchTypeDateTo = (String)filter.get("version.condition.date.to");
                        if(searchType == null)
                            throw new IIPRequestException("Miss out mandatory condition: version.condition.type");
                        if(searchType.equals("released"))
                        {
                            sb0.append("$id,");
                            sb0.append(classCode);
                            sb0.append("$vf");
                            sb0.append(" where vf$ouid=id$last and md$status='RLS'");
                        } else
                        if(searchType.equals("wip"))
                        {
                            sb0.append("$id,");
                            sb0.append(classCode);
                            sb0.append("$vf");
                            sb0.append(" where vf$ouid=id$wip");
                        } else
                        if(searchType.equals("all"))
                        {
                            sb0.append("$vf");
                            sb0.append(" where 1=1 ");
                        }
                        if(searchType.equals("cdate"))
                        {
                            sb0.append("$vf");
                            sb0.append(" where ");
                            if(!Utils.isNullString(searchTypeDateFrom) && Utils.isNullString(searchTypeDateTo))
                            {
                                try
                                {
                                    searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                sb0.append("md$cdate>='");
                                sb0.append(searchTypeDateFrom);
                                sb0.append("'");
                            } else
                            if(Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                            {
                                try
                                {
                                    searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateTo = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateTo = null;
                                }
                                sb0.append("md$cdate<='");
                                sb0.append(searchTypeDateTo);
                                sb0.append("'");
                            } else
                            if(!Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                            {
                                try
                                {
                                    searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                sb0.append("md$cdate>='");
                                sb0.append(searchTypeDateFrom);
                                sb0.append("' and ");
                                try
                                {
                                    searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateTo = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateTo = null;
                                }
                                sb0.append("md$cdate<='");
                                sb0.append(searchTypeDateTo);
                                sb0.append("'");
                            } else
                            {
                                sb0.append("1=1");
                            }
                        } else
                        if(searchType.equals("mdate"))
                        {
                            sb0.append("$vf");
                            sb0.append(" where ");
                            if(!Utils.isNullString(searchTypeDateFrom) && Utils.isNullString(searchTypeDateTo))
                            {
                                try
                                {
                                    searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                sb0.append("md$mdate>='");
                                sb0.append(searchTypeDateFrom);
                                sb0.append("'");
                            } else
                            if(Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                            {
                                try
                                {
                                    searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateTo = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateTo = null;
                                }
                                sb0.append("md$mdate<='");
                                sb0.append(searchTypeDateTo);
                                sb0.append("'");
                            } else
                            if(!Utils.isNullString(searchTypeDateFrom) && !Utils.isNullString(searchTypeDateTo))
                            {
                                try
                                {
                                    searchTypeDateFrom = sdf1.format(sdf4.parse(searchTypeDateFrom));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateFrom = null;
                                }
                                sb0.append("md$mdate>='");
                                sb0.append(searchTypeDateFrom);
                                sb0.append("' and ");
                                try
                                {
                                    searchTypeDateTo = sdf1.format(sdf4.parse(searchTypeDateTo));
                                }
                                catch(NumberFormatException nfe)
                                {
                                    searchTypeDateTo = null;
                                }
                                catch(ParseException pe)
                                {
                                    searchTypeDateTo = null;
                                }
                                sb0.append("md$mdate<='");
                                sb0.append(searchTypeDateTo);
                                sb0.append("'");
                            } else
                            {
                                sb0.append("1=1");
                            }
                        }
                        for(Iterator mapKey = filter.keySet().iterator(); mapKey.hasNext();)
                        {
                            String fieldOuid = (String)mapKey.next();
                            if(fieldOuid != null && !fieldOuid.equals("version.condition.type") && !fieldOuid.equals("version.condition.date.from") && !fieldOuid.equals("version.condition.date.to"))
                            {
                                Object conditionObject = filter.get(fieldOuid);
                                DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(dosField != null)
                                {
                                    String fieldCode = dosField.code;
                                    if(fieldCode.equals("md$number"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        conditionString = conditionString.replace('*', '%').replace('?', '_');
                                        if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                        {
                                            sb0.append(" and ");
                                            sb0.append("md$number like '");
                                            sb0.append(conditionString);
                                            sb0.append("'");
                                        } else
                                        {
                                            sb0.append(" and ");
                                            sb0.append("md$number='");
                                            sb0.append(conditionString);
                                            sb0.append("'");
                                        }
                                    } else
                                    if(fieldCode.equals("md$status"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$status='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    if(fieldCode.equals("vf$version"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("vf$version='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    if(fieldCode.equals("md$user"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$user='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    if(fieldCode.equals("md$cdate"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$cdate like to_char(to_date('");
                                        sb0.append(conditionString);
                                        sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                                    } else
                                    if(fieldCode.equals("md$mdate"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$mdate like to_char(to_date('");
                                        sb0.append(conditionString);
                                        sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                                    } else
                                    if(!fieldCode.equals("md$sequence") && fieldList.contains(dosField))
                                    {
                                        if(dosField != null)
                                        {
                                            fieldCode = fieldCode.toLowerCase().replace(' ', '_');
                                            sb0.append(" and ");
                                            sb0.append(fieldCode);
                                            if(conditionObject instanceof Boolean)
                                            {
                                                sb0.append("='");
                                                if(((Boolean)conditionObject).booleanValue())
                                                    sb0.append('T');
                                                else
                                                    sb0.append('F');
                                                sb0.append("'");
                                            } else
                                            if(conditionObject instanceof Number)
                                            {
                                                sb0.append('=');
                                                sb0.append(conditionObject);
                                            } else
                                            if(dosField.type.byteValue() == 24 || dosField.type.byteValue() == 25)
                                            {
                                                String conditionString = (String)conditionObject;
                                                sb0.append('=');
                                                sb0.append(Utils.convertOuidToLong(conditionString));
                                            } else
                                            {
                                                String conditionString = conditionObject.toString();
                                                conditionString = conditionString.replace('*', '%').replace('?', '_');
                                                if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                                {
                                                    sb0.append(" like '");
                                                    sb0.append(conditionString);
                                                    sb0.append("'");
                                                } else
                                                {
                                                    sb0.append("='");
                                                    sb0.append(conditionString);
                                                    sb0.append("'");
                                                }
                                            }
                                        }
                                        dosField = null;
                                    }
                                }
                            }
                        }

                        sb0.append(" and vf$ouid in (");
                        boolean firstOuid = true;
                        for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                            if(firstOuid)
                                firstOuid = false;
                            else
                                sb0.append(',');

                        sb0.append(")");
                    } else
                    {
                        sb0.append("$vf, ");
                        sb0.append("(select vf$identity, max(vf$ouid) max_vf$ouid ");
                        sb0.append("from " + classCode + "$vf ");
                        sb0.append("where vf$ouid in (");
                        boolean firstOuid = true;
                        for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                            if(firstOuid)
                                firstOuid = false;
                            else
                                sb0.append(',');

                        sb0.append(") ");
                        sb0.append("group by vf$identity) ");
                        sb0.append("where vf$ouid = max_vf$ouid");
                    }
                } else
                {
                    sb0.append("select '");
                    sb0.append(classCode);
                    sb0.append("$sf@',sf$ouid");
                    int i = 1;
                    if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                    {
                        int typeArray[] = new int[fieldCount];
                        for(Iterator fieldKey = fields.iterator(); fieldKey.hasNext();)
                        {
                            i++;
                            String fieldOuid = (String)fieldKey.next();
                            DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                            if(dosField == null)
                            {
                                sb0.append(",null as x$");
                                sb0.append(fieldOuid);
                                typeArray[i] = -1;
                            } else
                            if(dosField.code.equals("md$sequence"))
                            {
                                sb0.append(",null");
                                int sequenceField = i;
                                typeArray[i] = -1;
                            } else
                            {
                                int multiplicityFrom = Utils.getInt(dosField.multiplicity_from);
                                int multiplicityTo = Utils.getInt(dosField.multiplicity_to);
                                boolean hasCollection;
                                if(multiplicityFrom < multiplicityTo)
                                {
                                    if(multiplicityTo > 1)
                                        hasCollection = true;
                                    else
                                        hasCollection = multiplicityTo - multiplicityFrom > 1;
                                } else
                                {
                                    hasCollection = false;
                                }
                                if(hasCollection)
                                {
                                    typeArray[i] = -13;
                                } else
                                {
                                    DOSField tempDosField = null;
                                    for(Iterator tempFieldKey = fieldList.iterator(); tempFieldKey.hasNext(); tempDosField = null)
                                    {
                                        tempDosField = (DOSField)tempFieldKey.next();
                                        if(!tempDosField.name.equals(dosField.name))
                                            continue;
                                        typeArray[i] = Utils.getByte(dosField.type);
                                        break;
                                    }

                                    if(tempDosField == null)
                                        typeArray[i] = -1;
                                }
                                sb0.append(',');
                                determineFoundationFieldTypes(typeArray, i, dosField.code);
                                StringBuffer selectClause = new StringBuffer();
                                addStringBufferFromField(sb0, typeArray, i, dosField, selectClause);
                            }
                        }

                        typeArrayMap.put(leafOuid, typeArray);
                        typeArray = (int[])null;
                    } else
                    {
                        sb0.append(", md$number");
                    }
                    sb0.append(" from ");
                    sb0.append(classCode);
                    sb0.append("$sf");
                    sb0.append(" where sf$ouid in (");
                    boolean firstOuid = true;
                    for(Iterator list1Key = list1.iterator(); list1Key.hasNext(); sb0.append((String)list1Key.next()))
                        if(firstOuid)
                            firstOuid = false;
                        else
                            sb0.append(',');

                    sb0.append(") ");
                    if(filter != null && filter.size() > 0)
                    {
                        for(Iterator mapKey = filter.keySet().iterator(); mapKey.hasNext();)
                        {
                            String fieldOuid = (String)mapKey.next();
                            if(!fieldOuid.equals("version.condition.type") && !fieldOuid.equals("version.condition.date.from") && !fieldOuid.equals("version.condition.date.to"))
                            {
                                Object conditionObject = filter.get(fieldOuid);
                                DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(dosField != null)
                                {
                                    String fieldCode = dosField.code;
                                    if(fieldCode.equals("md$number"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        conditionString = conditionString.replace('*', '%').replace('?', '_');
                                        if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                        {
                                            sb0.append(" and ");
                                            sb0.append("md$number like '");
                                            sb0.append(conditionString);
                                            sb0.append("'");
                                        } else
                                        {
                                            sb0.append(" and ");
                                            sb0.append("md$number='");
                                            sb0.append(conditionString);
                                            sb0.append("'");
                                        }
                                    } else
                                    if(fieldCode.equals("md$status"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$status='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    if(fieldCode.equals("md$user"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$user='");
                                        sb0.append(conditionString);
                                        sb0.append("'");
                                    } else
                                    if(fieldCode.equals("md$cdate"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$cdate like to_char(to_date('");
                                        sb0.append(conditionString);
                                        sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                                    } else
                                    if(fieldCode.equals("md$mdate"))
                                    {
                                        String conditionString = (String)conditionObject;
                                        sb0.append(" and ");
                                        sb0.append("md$mdate like to_char(to_date('");
                                        sb0.append(conditionString);
                                        sb0.append("','YYYY-MM-DD'),'YYYYMMDD')||'%'");
                                    } else
                                    if(!fieldCode.equals("md$sequence") && fieldList.contains(dosField))
                                    {
                                        if(dosField != null)
                                        {
                                            fieldCode = fieldCode.toLowerCase().replace(' ', '_');
                                            sb0.append(" and ");
                                            sb0.append(fieldCode);
                                            if(conditionObject instanceof Boolean)
                                            {
                                                sb0.append("='");
                                                if(((Boolean)conditionObject).booleanValue())
                                                    sb0.append('T');
                                                else
                                                    sb0.append('F');
                                                sb0.append("'");
                                            } else
                                            if(conditionObject instanceof Number)
                                            {
                                                sb0.append('=');
                                                sb0.append(conditionObject);
                                            } else
                                            if(dosField.type.byteValue() == 24 || dosField.type.byteValue() == 25)
                                            {
                                                String conditionString = (String)conditionObject;
                                                sb0.append('=');
                                                sb0.append(Utils.convertOuidToLong(conditionString));
                                            } else
                                            {
                                                String conditionString = conditionObject.toString();
                                                conditionString = conditionString.replace('*', '%').replace('?', '_');
                                                if(conditionString.indexOf('%') >= 0 || conditionString.indexOf('_') >= 0)
                                                {
                                                    sb0.append(" like '");
                                                    sb0.append(conditionString);
                                                    sb0.append("'");
                                                } else
                                                {
                                                    sb0.append("='");
                                                    sb0.append(conditionString);
                                                    sb0.append("'");
                                                }
                                            }
                                        }
                                        dosField = null;
                                    }
                                }
                            }
                        }

                    }
                }
            }
            String oldDatasourceId;
            if(datasourceId == null)
            {
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                oldDatasourceId = datasourceId;
            } else
            {
                oldDatasourceId = datasourceId;
                datasourceId = dosClass.datasourceId;
                if(Utils.isNullString(datasourceId))
                    datasourceId = "default";
                if(!datasourceId.equals(oldDatasourceId))
                    willExecute = true;
            }
            if(!listKey.hasNext())
                willExecute = true;
            if(willExecute && sqlCount > 0)
            {
                sb0.append(") order by md$number");
                xc = dtm.getPooledConnection(oldDatasourceId);
                con = xc.getConnection();
                stat = con.prepareStatement(sb0.toString());
                ResultSet rs = stat.executeQuery();
                while(rs.next()) 
                {
                    int j = 0;
                    ArrayList oneRow;
                    int i;
                    if(assoMap != null && assoMap.size() > 0)
                    {
                        ArrayList list2 = (ArrayList)assoMap.get(Long.toString(rs.getLong(2)));
                        if(list2 != null && list2.size() > 0)
                        {
                            for(int k = 0; k < list2.size(); k++)
                            {
                                ArrayList list3 = (ArrayList)list2.get(k);
                                oneRow = new ArrayList(fieldCount);
                                oneRow.add(rs.getString(1) + Long.toHexString(rs.getLong(2)));
                                String tmpClassOuid = getClassOuid(rs.getString(1) + Long.toHexString(rs.getLong(2)));
                                i = 2;
                                j = 3;
                                for(; i < fieldCount; i++)
                                {
                                    int typeArray[] = (int[])typeArrayMap.get(tmpClassOuid);
                                    if(typeArray[i] == -15)
                                        oneRow.add(msr.getStgrepString(locale, rs.getString(j)));
                                    else
                                    if(typeArray[i] == -14)
                                    {
                                        String userId = rs.getString(j);
                                        HashMap userData = aus.getUser(userId);
                                        if(userData != null)
                                        {
                                            oneRow.add(userData.get("name"));
                                            userData.clear();
                                            userData = null;
                                        } else
                                        {
                                            oneRow.add(null);
                                        }
                                    } else
                                    {
                                        if(typeArray[i] == -1)
                                        {
                                            if(list3 != null && list3.size() >= i)
                                                oneRow.add(list3.get(i - 1));
                                            else
                                                oneRow.add(null);
                                            j++;
                                            continue;
                                        }
                                        addListFromResultSet(oneRow, rs, j, typeArray[i]);
                                    }
                                    j++;
                                    typeArray = (int[])null;
                                }

                                if(list3 != null && list3.size() > 0)
                                    oneRow.add(list3.get(0));
                                else
                                    oneRow.add(null);
                                returnList.add(oneRow);
                                oneRow = null;
                                list3 = null;
                            }

                            continue;
                        }
                        list2 = null;
                    }
                    oneRow = new ArrayList(fieldCount);
                    oneRow.add(rs.getString(1) + Long.toHexString(rs.getLong(2)));
                    String tmpClassOuid = getClassOuid(rs.getString(1) + Long.toHexString(rs.getLong(2)));
                    i = 2;
                    j = 3;
                    for(; i < fieldCount; i++)
                    {
                        int typeArray[] = (int[])typeArrayMap.get(tmpClassOuid);
                        if(typeArray[i] == -15)
                            oneRow.add(msr.getStgrepString(locale, rs.getString(j)));
                        else
                        if(typeArray[i] == -14)
                        {
                            String userId = rs.getString(j);
                            HashMap userData = aus.getUser(userId);
                            if(userData != null)
                            {
                                oneRow.add(userData.get("name"));
                                userData.clear();
                                userData = null;
                            } else
                            {
                                oneRow.add(null);
                            }
                        } else
                        {
                            if(typeArray[i] == -1)
                            {
                                oneRow.add(null);
                                j++;
                                continue;
                            }
                            addListFromResultSet(oneRow, rs, j, typeArray[i]);
                        }
                        j++;
                        typeArray = (int[])null;
                    }

                    returnList.add(oneRow);
                    oneRow = null;
                }
                sb0.setLength(0);
                rs.close();
                rs = null;
                con.commit();
                con.close();
                con = null;
                xc.close();
                xc = null;
                if(listKey.hasNext())
                {
                    sb0.append("select * from (");
                    sqlCount = 0;
                }
                willExecute = false;
                isFirstSQL = true;
            }
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        dosAssociation = null;
_L1:
        if(listKey.hasNext()) goto _L3; else goto _L2
_L2:
        typeArrayMap = null;
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList listLinkFrom(String ouid, ArrayList fields, HashMap filter)
        throws IIPRequestException
    {
        return listLink(ouid, true, fields, filter);
    }

    public ArrayList listLinkFrom(String ouid, ArrayList fields)
        throws IIPRequestException
    {
        return listLink(ouid, true, fields, null);
    }

    public ArrayList listLinkFrom(String ouid, HashMap filter)
        throws IIPRequestException
    {
        return listLink(ouid, true, null, filter);
    }

    public ArrayList listLinkFrom(String ouid)
        throws IIPRequestException
    {
        return listLink(ouid, true, null, null);
    }

    public ArrayList listLinkForCADIntegration(String ouid)
        throws IIPRequestException
    {
        ArrayList returnList = new ArrayList();
        Iterator listKey = null;
        ArrayList tmpList = null;
        ArrayList linkList = listLinkFrom(ouid);
        if(linkList != null)
        {
            for(listKey = linkList.iterator(); listKey.hasNext(); returnList.add(tmpList.get(0)))
                tmpList = (ArrayList)listKey.next();

            listKey = null;
            return returnList;
        } else
        {
            return null;
        }
    }

    public ArrayList listLinkTo(String ouid, ArrayList fields, HashMap filter)
        throws IIPRequestException
    {
        return listLink(ouid, false, fields, filter);
    }

    public ArrayList listLinkTo(String ouid, ArrayList fields)
        throws IIPRequestException
    {
        return listLink(ouid, false, fields, null);
    }

    public ArrayList listLinkTo(String ouid, HashMap filter)
        throws IIPRequestException
    {
        return listLink(ouid, false, null, filter);
    }

    public ArrayList listLinkTo(String ouid)
        throws IIPRequestException
    {
        return listLink(ouid, false, null, null);
    }

    public ArrayList getExpanedList(String ouid, boolean isFrom, ArrayList fields, HashMap hashmap)
        throws IIPRequestException
    {
        return null;
    }

    public String generateNumber(String classOuid)
        throws IIPRequestException
    {
        throw new IIPRequestException("Deprecated method.");
    }

    public String generateNumber(String classOuid, String number)
        throws IIPRequestException
    {
        throw new IIPRequestException("Deprecated method.");
    }

    public String generateNumber(String classOuid, DOSChangeable object, String number)
        throws IIPRequestException
    {
        boolean dupNumber = false;
        if(number != null)
            number = number.trim();
        if(!Utils.isNullString(number))
            if(checkDuplicateNumber(classOuid, number) != null)
                throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B406", "Number Duplicated."));
            else
                return number;
        ArrayList superClassList = null;
        String generatedNumber = "";
        String nextOuid = null;
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int index = 0;
        superClassList = listAllSuperClassOuid(classOuid);
        if(superClassList == null)
            superClassList = new ArrayList();
        superClassList.add(0, classOuid);
        for(; index < superClassList.size(); index++)
        {
            nextOuid = (String)superClassList.get(index);
            ArrayList ruleList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE/" + nextOuid);
            if(ruleList == null || ruleList.size() <= 0)
                continue;
            for(int i = 0; i < ruleList.size(); i++)
            {
                HashMap ruleMap = nds.getArguments("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE/" + nextOuid + "/" + (String)ruleList.get(i));
                String typeID = (String)ruleMap.get("TypeID");
                if(typeID.equals("NGR01"))
                    generatedNumber = generatedNumber + (String)ruleMap.get("Contents");
                else
                if(typeID.equals("NGR02"))
                {
                    int len = Integer.parseInt((String)ruleMap.get("Length"));
                    if(len == 4)
                        generatedNumber = generatedNumber + sdf.format(today).substring(0, len);
                    else
                    if(len == 3)
                        generatedNumber = generatedNumber + sdf.format(today).substring(1, 1 + len);
                    else
                    if(len == 2)
                        generatedNumber = generatedNumber + sdf.format(today).substring(2, 2 + len);
                } else
                if(typeID.equals("NGR03"))
                {
                    int len = Integer.parseInt((String)ruleMap.get("Length"));
                    if(len == 2)
                        generatedNumber = generatedNumber + sdf.format(today).substring(4, 4 + len);
                    else
                    if(len == 1)
                    {
                        String month = sdf.format(today).substring(4, 6);
                        if(month.equals("12"))
                            generatedNumber = generatedNumber + "D";
                        else
                        if(month.equals("11"))
                            generatedNumber = generatedNumber + "N";
                        else
                        if(month.equals("10"))
                            generatedNumber = generatedNumber + "O";
                        else
                        if(month.startsWith("0"))
                            generatedNumber = generatedNumber + month.substring(1, 2);
                    }
                } else
                if(typeID.equals("NGR05"))
                {
                    int len = Integer.parseInt((String)ruleMap.get("Length"));
                    if(len == 2)
                        generatedNumber = generatedNumber + sdf.format(today).substring(6, 6 + len);
                } else
                if(typeID.equals("NGR04"))
                {
                    List tmpList = Utils.tokenizeMessage((String)ruleMap.get("Contents"), '@');
                    DOSField dosField = (DOSField)fieldMap.get(tmpList.get(0));
                    if(dosField != null)
                    {
                        String fieldName = dosField.name;
                        byte type = dosField.type.byteValue();
                        if(type == 24)
                        {
                            String fieldData = (String)object.get(fieldName);
                            if(!Utils.isNullString(fieldData))
                            {
                                DOSCode dosCode = (DOSCode)codeMap.get(Long.toHexString(dosField.typeCode.OUID));
                                DOSCodeItem dosCodeItem = (DOSCodeItem)codeItemMap.get(fieldData);
                                if(dosCode.OUID == dosCodeItem._code.OUID)
                                    generatedNumber = generatedNumber + dosCodeItem.codeItemID;
                            }
                        } else
                        if(type == 25)
                        {
                            String fieldData = (String)object.get(fieldName);
                            if(!Utils.isNullString(fieldData))
                            {
                                DOSField dosRefField = (DOSField)fieldMap.get(Long.toHexString(dosField.referencedFieldOuid));
                                String refFieldName = dosRefField.name;
                                String refFieldData = (String)object.get(refFieldName);
                                if(!Utils.isNullString(refFieldData))
                                {
                                    DOSCodeItem dosRefCodeItem = (DOSCodeItem)codeItemMap.get(refFieldData);
                                    DOSChangeable dosCodeData = getCodeWithName(dosRefCodeItem.description);
                                    DOSCodeItem dosCodeItem = (DOSCodeItem)codeItemMap.get(fieldData);
                                    if(((String)dosCodeData.get("ouid")).equals(Long.toHexString(dosCodeItem._code.OUID)))
                                        generatedNumber = generatedNumber + dosCodeItem.codeItemID;
                                }
                            }
                        } else
                        if(type == 16)
                        {
                            String fieldData = (String)object.get(fieldName);
                            if(!Utils.isNullString(fieldData))
                            {
                                DOSChangeable objectTypeFieldData = get(fieldData);
                                if(((String)tmpList.get(1)).equals("md$number") || ((String)tmpList.get(1)).equals("md$sequence") || ((String)tmpList.get(1)).equals("vf$version") || ((String)tmpList.get(1)).equals("md$description") || ((String)tmpList.get(1)).equals("md$status") || ((String)tmpList.get(1)).equals("md$user") || ((String)tmpList.get(1)).equals("md$cdate") || ((String)tmpList.get(1)).equals("md$mdate"))
                                {
                                    String fieldName2 = (String)tmpList.get(1);
                                    String fieldData2 = (String)objectTypeFieldData.get(fieldName2);
                                    if(!Utils.isNullString(fieldData2))
                                        generatedNumber = generatedNumber + fieldData2;
                                } else
                                {
                                    DOSField dosField2 = (DOSField)fieldMap.get(tmpList.get(1));
                                    String fieldName2 = dosField2.name;
                                    if(dosField2.type.byteValue() == 24)
                                    {
                                        String fieldData2 = (String)objectTypeFieldData.get(fieldName2);
                                        if(!Utils.isNullString(fieldData2))
                                        {
                                            DOSCode dosCode = (DOSCode)codeMap.get(Long.toHexString(dosField2.typeCode.OUID));
                                            DOSCodeItem dosCodeItem = (DOSCodeItem)codeItemMap.get(fieldData2);
                                            if(dosCode.OUID == dosCodeItem._code.OUID)
                                                generatedNumber = generatedNumber + dosCodeItem.codeItemID;
                                        }
                                    } else
                                    if(dosField2.type.byteValue() == 25)
                                    {
                                        String fieldData2 = (String)objectTypeFieldData.get(fieldName2);
                                        if(!Utils.isNullString(fieldData2))
                                        {
                                            DOSField dosRefField = (DOSField)fieldMap.get(Long.toHexString(dosField2.referencedFieldOuid));
                                            String refFieldName = dosRefField.name;
                                            String refFieldData = (String)objectTypeFieldData.get(refFieldName);
                                            if(!Utils.isNullString(refFieldData))
                                            {
                                                DOSCodeItem dosRefCodeItem = (DOSCodeItem)codeItemMap.get(refFieldData);
                                                DOSChangeable dosCodeData = getCodeWithName(dosRefCodeItem.description);
                                                DOSCodeItem dosCodeItem = (DOSCodeItem)codeItemMap.get(fieldData2);
                                                if(((String)dosCodeData.get("ouid")).equals(Long.toHexString(dosCodeItem._code.OUID)))
                                                    generatedNumber = generatedNumber + dosCodeItem.codeItemID;
                                            }
                                        }
                                    } else
                                    if(dosField2.type.byteValue() != 16)
                                        if(dosField2.type.byteValue() == 1)
                                        {
                                            Boolean fieldData2 = (Boolean)objectTypeFieldData.get(fieldName2);
                                            if(fieldData2 != null)
                                                generatedNumber = generatedNumber + fieldData2.toString();
                                        } else
                                        if(dosField2.type.byteValue() == 6 || dosField2.type.byteValue() == 4 || dosField2.type.byteValue() == 7 || dosField2.type.byteValue() == 8 || dosField2.type.byteValue() == 2 || dosField2.type.byteValue() == 5)
                                        {
                                            Number fieldData2 = (Number)objectTypeFieldData.get(fieldName2);
                                            if(fieldData2 != null)
                                                generatedNumber = generatedNumber + fieldData2.toString();
                                        } else
                                        {
                                            String fieldData2 = (String)objectTypeFieldData.get(fieldName2);
                                            if(!Utils.isNullString(fieldData2))
                                                generatedNumber = generatedNumber + fieldData2;
                                        }
                                }
                            }
                        } else
                        if(dosField.type.byteValue() == 1)
                        {
                            Boolean fieldData = (Boolean)object.get(fieldName);
                            if(fieldData != null)
                                generatedNumber = generatedNumber + fieldData.toString();
                        } else
                        if(dosField.type.byteValue() == 6 || dosField.type.byteValue() == 4 || dosField.type.byteValue() == 7 || dosField.type.byteValue() == 8 || dosField.type.byteValue() == 2 || dosField.type.byteValue() == 5)
                        {
                            Number fieldData = (Number)object.get(fieldName);
                            if(fieldData != null)
                                generatedNumber = generatedNumber + fieldData.toString();
                        } else
                        {
                            String fieldData = (String)object.get(fieldName);
                            if(!Utils.isNullString(fieldData))
                                generatedNumber = generatedNumber + fieldData;
                        }
                    }
                } else
                if(typeID.equals("NGR10"))
                {
                    int len = Integer.parseInt((String)ruleMap.get("Length"));
                    String maxNumber = null;
                    if(Utils.isNullString(generatedNumber))
                        maxNumber = nds.getValue("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + nextOuid + "/" + "null");
                    else
                        maxNumber = nds.getValue("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + nextOuid + "/" + generatedNumber);
                    if(maxNumber == null)
                    {
                        nds.addNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER", nextOuid, "DOS.RULE", "DOS.RULE");
                        if(Utils.isNullString(generatedNumber))
                            nds.addNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + nextOuid, "null", "DOS.RULE", "0");
                        else
                            nds.addNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + nextOuid, generatedNumber, "DOS.RULE", "0");
                        maxNumber = "0";
                    }
                    String newNumber = String.valueOf(Integer.parseInt(maxNumber) + 1);
                    if(Utils.isNullString(generatedNumber))
                        nds.setValue("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + nextOuid + "/" + "null", newNumber);
                    else
                        nds.setValue("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER/" + nextOuid + "/" + generatedNumber, newNumber);
                    for(int j = 0; j < len - newNumber.length(); j++)
                        generatedNumber = generatedNumber + "0";

                    generatedNumber = generatedNumber + newNumber;
                }
            }

            break;
        }

        if(Utils.isNullString(generatedNumber))
            generatedNumber = "0";
        if(checkDuplicateNumber(classOuid, generatedNumber) != null)
            throw new IIPRequestException(msr.getStgrepString((String)getClientContext().get("locale"), "WRN_B406", "Number Duplicated."));
        else
            return generatedNumber;
    }

    private String generateNumber(Connection con, String classOuid)
        throws IIPRequestException
    {
        return "0";
    }

    private String generateNumber(Connection con, String classCode, String classOuid, boolean isAssociationClass, String number)
        throws IIPRequestException
    {
        String generatedNumber;
        Date today;
        SimpleDateFormat sdf;
        Statement stat;
        int index;
        ArrayList superClassList = null;
        generatedNumber = "";
        String nextOuid = null;
        today = new Date();
        sdf = new SimpleDateFormat("yyyyMM");
        stat = null;
        ResultSet rs = null;
        index = -1;
        SQLException e;
        String s;
        try
        {
            ArrayList superClassList = listAllSuperClassOuid(classOuid);
            if(superClassList == null)
                superClassList = new ArrayList();
            superClassList.add(0, classOuid);
            for(; index < superClassList.size(); index++)
            {
                String nextOuid = (String)superClassList.get(index);
                ArrayList ruleList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE/" + nextOuid);
                if(ruleList == null || ruleList.size() <= 0)
                    continue;
                for(int i = 0; i < ruleList.size(); i++)
                {
                    HashMap ruleMap = nds.getArguments("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE/" + nextOuid + "/" + (String)ruleList.get(i));
                    String typeID = (String)ruleMap.get("TypeID");
                    if(typeID.equals("NGR01"))
                        generatedNumber = generatedNumber + (String)ruleMap.get("Contents");
                    else
                    if(typeID.equals("NGR02"))
                    {
                        int len = Integer.parseInt((String)ruleMap.get("Length"));
                        if(len == 4)
                            generatedNumber = generatedNumber + sdf.format(today).substring(0, len);
                        else
                        if(len == 3)
                            generatedNumber = generatedNumber + sdf.format(today).substring(1, 1 + len);
                        else
                        if(len == 2)
                            generatedNumber = generatedNumber + sdf.format(today).substring(2, 2 + len);
                    } else
                    if(typeID.equals("NGR03"))
                    {
                        int len = Integer.parseInt((String)ruleMap.get("Length"));
                        if(len == 2)
                            generatedNumber = generatedNumber + sdf.format(today).substring(4, 4 + len);
                        else
                        if(len == 1)
                        {
                            String month = sdf.format(today).substring(4, 6);
                            if(month.equals("12"))
                                generatedNumber = generatedNumber + "D";
                            else
                            if(month.equals("11"))
                                generatedNumber = generatedNumber + "N";
                            else
                            if(month.equals("10"))
                                generatedNumber = generatedNumber + "O";
                            else
                            if(month.startsWith("0"))
                                generatedNumber = generatedNumber + month.substring(1, 2);
                        }
                    } else
                    if(!typeID.equals("NGR04") && typeID.equals("NGR10"))
                    {
                        int len = Integer.parseInt((String)ruleMap.get("Length"));
                        String numberField = null;
                        if(isAssociationClass)
                            numberField = "md$sequence";
                        else
                            numberField = "md$number";
                        String nvlNumber = "";
                        for(int j = 0; j < len; j++)
                            nvlNumber = nvlNumber + "0";

                        stat = con.createStatement();
                        ResultSet rs;
                        for(rs = stat.executeQuery("select lpad(to_char(to_number(nvl(max(substr(" + numberField + ", " + (generatedNumber.length() + 1) + ", " + len + ")), '" + nvlNumber + "')) + 1), " + len + ", '0') " + "from " + classCode + " " + "where " + numberField + " like '" + generatedNumber + "%' "); rs.next();)
                            generatedNumber = generatedNumber + rs.getString(1);

                        rs.close();
                        stat.close();
                    }
                }

                break;
            }

            if(Utils.isNullString(generatedNumber))
                break MISSING_BLOCK_LABEL_978;
            s = generatedNumber;
        }
        finally
        {
            if(stat != null)
            {
                try
                {
                    stat.close();
                }
                catch(SQLException sqlexception) { }
                stat = null;
            }
        }
        return s;
        if(Utils.isNullString(number)) goto _L2; else goto _L1
_L1:
        s = number;
        return s;
_L2:
        return "0";
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    private String generateVersion(Connection con, String classCode, String instanceOuid)
        throws SQLException, IIPRequestException
    {
        String resultString = null;
        boolean found = false;
        if(Utils.isNullString(instanceOuid))
            return "wip";
        PreparedStatement stat = con.prepareStatement("select b.vf$version from " + classCode + "$vf a, " + classCode + "$vf b, " + classCode + "$id c " + "where a.vf$ouid=? and " + "c.id$ouid=a.vf$identity and " + "b.vf$ouid=c.id$last ");
        stat.setLong(1, Utils.getRealLongOuid(instanceOuid));
        ResultSet rs = stat.executeQuery();
        if(rs.next())
            resultString = rs.getString(1);
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        if("wip".equals(resultString))
        {
            resultString = versionStrings[0];
        } else
        {
            for(int x = 0; x < versionStrings.length; x++)
            {
                if(!versionStrings[x].equals(resultString))
                    continue;
                found = true;
                resultString = versionStrings[x + 1];
                break;
            }

            if(!found)
                resultString = "wip";
        }
        return resultString;
    }

    private String generateFileVersion(Connection con, String classCode, String instanceOuid, int index)
        throws SQLException, IIPRequestException
    {
        String resultString = null;
        boolean found = false;
        if(Utils.isNullString(instanceOuid))
            return "0";
        PreparedStatement stat = con.prepareStatement("select max(to_number(a.md$version)) from " + classCode + "$fl a " + "where a.md$ouid=? and " + "a.md$path>' ' and " + "a.md$index=? ");
        stat.setLong(1, Utils.getRealLongOuid(instanceOuid));
        stat.setInt(2, index);
        ResultSet rs = stat.executeQuery();
        if(rs.next())
            resultString = rs.getString(1);
        rs.close();
        rs = null;
        stat.close();
        stat = null;
        if("wip".equals(resultString))
        {
            return "0";
        } else
        {
            Integer integerNumber = Utils.getInteger(resultString);
            int newVersion = integerNumber.intValue() + 1;
            integerNumber = null;
            return Integer.toString(newVersion);
        }
    }

    public String getClassOuid(String ouid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            return null;
        int index = ouid.indexOf('$');
        if(index < 0)
        {
            return null;
        } else
        {
            String ouidPrefix = ouid.substring(0, index);
            return (String)classOuidMap.get(ouidPrefix);
        }
    }

    public ArrayList listFile(String ouid)
        throws IIPRequestException
    {
        ArrayList resultList;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        boolean hasFileControl = Utils.getBoolean(dosClass.fileControl);
        if(!hasFileControl)
            return null;
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        long ouidLong = 0L;
        int rows = 0;
        String path = null;
        String parent = null;
        String child = null;
        resultList = null;
        HashMap file = null;
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(ouid);
            stat = con.prepareStatement("select x.md$path,i.md$version,x.md$des,x.md$filetype,i.md$index,to_char(to_date(x.md$cidate,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'),to_char(to_date(x.md$codate,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS'),x.md$user from (select a.md$ouid as md$ouid,a.md$index as md$index,max(to_number(a.md$version)) as md$version,max(a.md$codate) as md$codate from " + classCode + "$fl a " + "where a.md$ouid=? " + "group by a.md$ouid,a.md$index " + ") i," + classCode + "$fl x " + "where x.md$ouid=i.md$ouid and " + "x.md$index=i.md$index and " + "x.md$version=decode(i.md$version,'~wip','wip',i.md$version) " + "order by x.md$filetype,x.md$index ");
            stat.setLong(1, ouidLong);
            resultList = new ArrayList();
            HashMap filetype = null;
            String checkedInDate = null;
            String checkedOutDate = null;
            String userId = null;
            File fileHandle = null;
            HashMap user = null;
            for(rs = stat.executeQuery(); rs.next();)
            {
                file = new HashMap();
                file.put("md$ouid", ouid);
                file.put("md$path", rs.getString(1));
                file.put("md$version", rs.getString(2));
                file.put("md$description", rs.getString(3));
                file.put("md$filetype.id", rs.getString(4));
                file.put("md$index", Utils.getInteger(rs, 5));
                file.put("md$checkedin.date", rs.getString(6));
                file.put("md$checkedout.date", rs.getString(7));
                file.put("md$user.id", rs.getString(8));
                userId = (String)file.get("md$user.id");
                if(!Utils.isNullString(userId))
                {
                    user = aus.getUser(userId);
                    file.put("md$user.name", user.get("name"));
                    user.clear();
                    user = null;
                }
                filetype = dss.getFileType((String)file.get("md$filetype.id"));
                if(filetype != null)
                    file.put("md$filetype.description", filetype.get("description"));
                filetype = null;
                checkedInDate = (String)file.get("md$checkedin.date");
                checkedOutDate = (String)file.get("md$checkedout.date");
                if(!Utils.isNullString(checkedInDate) && !Utils.isNullString(checkedOutDate) && checkedOutDate.compareTo(checkedInDate) > 0)
                {
                    path = (String)file.get("md$path");
                    fileHandle = new File(path);
                    parent = fileHandle.getParent().replace('\\', '/');
                    child = fileHandle.getName();
                    path = parent + "/checkedout/" + child;
                    file.put("md$path", path);
                    fileHandle = null;
                }
                resultList.add(file);
                file = null;
            }

            rs.close();
            rs = null;
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
        return resultList;
    }

    public String attachFile(String ouid, HashMap file)
        throws IIPRequestException
    {
        String path;
        String parent;
        String child;
        int rows;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        if(!Utils.getBoolean(dosClass.fileControl))
            throw new IIPRequestException("Not file control class. (ouid: " + classOuid + ")");
        if(file == null || file.size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter(s): file");
        String filetypeId = (String)file.get("md$filetype.id");
        if(filetypeId == null)
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        HashMap filetype = dss.getFileType(filetypeId);
        if(filetype == null || filetype.size() == 0)
            throw new IIPRequestException("File type not exists. :(md$filetype.id: " + filetypeId + ")");
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        String userId = (String)getClientContext().get("userId");
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        path = null;
        parent = null;
        child = null;
        long ouidLong = 0L;
        rows = 0;
        int index = 1;
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(ouid);
            if(!versionable)
                stat = con.prepareStatement("select max(md$index)+1 from " + classCode + "$fl " + "where md$ouid=?");
            else
                stat = con.prepareStatement("select max(a.md$index)+1 from " + classCode + "$fl a, " + classCode + "$vf b, " + classCode + "$vf c " + "where b.vf$ouid=? and " + "c.vf$identity=b.vf$identity and " + "a.md$ouid=c.vf$ouid");
            stat.setLong(1, ouidLong);
            rs = stat.executeQuery();
            if(rs.next())
                index = rs.getInt(1);
            else
                index = 1;
            if(rs.wasNull())
                index = 1;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            parent = "/" + dosClass.name.replace(' ', '_').replace('/', '_').replace('\\', ' ') + "/" + filetypeId;
            if(!versionable)
                child = ouid + "." + index + ".0." + ((String)dosObject.get("md$number")).replace(' ', '_').replace('/', '_').replace('\\', ' ') + "." + filetype.get("extension");
            else
                child = ouid + "." + index + ".0." + ((String)dosObject.get("md$number")).replace(' ', '_').replace('/', '_').replace('\\', ' ') + "." + ((String)dosObject.get("vf$version")).replace(' ', '_').replace('/', '_').replace('\\', ' ') + "." + filetype.get("extension");
            path = parent + "/" + child;
            stat = con.prepareStatement("insert into " + classCode + "$fl " + "(md$ouid,md$path,md$version,md$des,md$filetype,md$index,md$cidate,md$user) " + "values (?,?,?,?,?,?,to_char(sysdate,'YYYYMMDDHH24MISS'),?)");
            stat.setLong(1, ouidLong);
            stat.setString(2, path);
            stat.setString(3, "0");
            stat.setString(4, (String)file.get("md$des"));
            stat.setString(5, filetypeId);
            stat.setInt(6, index);
            stat.setString(7, userId);
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
        catch(Exception e)
        {
            e.printStackTrace();
            throw new IIPRequestException(e.toString());
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        if(rows < 1)
        {
            return null;
        } else
        {
            dss.makeFolders(parent + "/checkedout");
            dss.createEmptyFile(parent, child);
            return path;
        }
    }

    public boolean detachFile(String ouid, HashMap file)
        throws IIPRequestException
    {
        String path;
        int rows;
        boolean isLink;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        if(file == null || file.size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter(s): file");
        String realOuid = (String)file.get("md$ouid");
        if(Utils.isNullString(realOuid))
            throw new IIPRequestException("Miss out mandatory parameter(s): md$ouid");
        path = (String)file.get("md$path");
        if(Utils.isNullString(path))
            throw new IIPRequestException("Miss out mandatory parameter(s): md$path");
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        long ouidLong = 0L;
        rows = 0;
        isLink = false;
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(realOuid);
            stat = con.prepareStatement("select count(*) from " + classCode + "$fl " + "where md$ouid>0 and " + "md$path=? ");
            stat.setString(1, path);
            ResultSet rs = stat.executeQuery();
            if(rs.next())
                if(rs.getInt(1) > 1)
                    isLink = true;
                else
                    isLink = false;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from " + classCode + "$fl " + "where (md$ouid,md$index)=(select md$ouid,md$index " + "from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? " + ") ");
            stat.setLong(1, ouidLong);
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
        if(rows < 1)
            return false;
        if(!isLink)
            dss.deleteFile(path);
        return true;
    }

    public String checkoutFile(String ouid, HashMap file)
        throws IIPRequestException
    {
        String oldPath;
        String newPath;
        int rows;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        if(!Utils.getBoolean(dosClass.fileControl))
            throw new IIPRequestException("Not file control class. (ouid: " + classOuid + ")");
        if(file == null || file.size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter(s): file");
        String realOuid = (String)file.get("md$ouid");
        if(Utils.isNullString(realOuid))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        oldPath = (String)file.get("md$path");
        if(Utils.isNullString(oldPath))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String version = (String)file.get("md$version");
        if(Utils.isNullString(version))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String filetypeId = (String)file.get("md$filetype.id");
        if(filetypeId == null)
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        HashMap filetype = dss.getFileType(filetypeId);
        if(filetype == null || filetype.size() == 0)
            throw new IIPRequestException("File type not exists. :(md$filetype.id: " + filetypeId + ")");
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        boolean wasNotCheckedIn = false;
        HashMap context = getClientContext();
        String userId = (String)context.get("userId");
        newPath = null;
        String parent = null;
        String child = null;
        long ouidLong = 0L;
        rows = 0;
        int index = 1;
        String desc = (String)file.get("md$description");
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        parent = dss.getParent(oldPath);
        child = dss.getName(oldPath);
        newPath = parent + "/checkedout/" + child;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(realOuid);
            stat = con.prepareStatement("select '1' from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? and " + "md$cidate is null");
            stat.setLong(1, ouidLong);
            stat.setString(2, oldPath);
            wasNotCheckedIn = false;
            rs = stat.executeQuery();
            if(rs.next())
                wasNotCheckedIn = true;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            if(wasNotCheckedIn)
            {
                con.commit();
                con.close();
                xc.close();
                throw new IIPRequestException("Not checked-in file.");
            }
            stat = con.prepareStatement("update " + classCode + "$fl " + "set md$codate=to_char(sysdate,'YYYYMMDDHH24MISS'), " + "md$user=? " + "where md$ouid=? and " + "md$path=? ");
            stat.setString(1, userId);
            stat.setLong(2, ouidLong);
            stat.setString(3, oldPath);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("update " + classCode + "$fl " + "set md$des = ? " + "where md$ouid=? and " + "md$path=? ");
            stat.setString(1, desc);
            stat.setLong(2, ouidLong);
            stat.setString(3, oldPath);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
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
        if(rows < 1)
        {
            return null;
        } else
        {
            dss.copyFile(oldPath, newPath);
            return newPath;
        }
    }

    public String checkinFile(String ouid, HashMap file)
        throws IIPRequestException
    {
        String oldPath;
        String newPath;
        String child;
        int rows;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        if(!Utils.getBoolean(dosClass.fileControl))
            throw new IIPRequestException("Not file control class. (ouid: " + classOuid + ")");
        if(file == null || file.size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter(s): file");
        oldPath = (String)file.get("md$path");
        if(Utils.isNullString(oldPath))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String version = (String)file.get("md$version");
        if(Utils.isNullString(version))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String filetypeId = (String)file.get("md$filetype.id");
        if(filetypeId == null)
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        HashMap filetype = dss.getFileType(filetypeId);
        if(filetype == null || filetype.size() == 0)
            throw new IIPRequestException("File type not exists. :(md$filetype.id: " + filetypeId + ")");
        boolean doVersionup = Utils.getBoolean((Boolean)file.get("do.versionup"));
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        boolean wasNotCheckedIn = false;
        HashMap context = getClientContext();
        String userId = (String)context.get("userId");
        String checkedInUserId = null;
        newPath = null;
        String parent = null;
        String oldPath2 = null;
        child = null;
        String versionString = null;
        long ouidLong = 0L;
        rows = 0;
        int index = 1;
        String desc = (String)file.get("md$description");
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        parent = "/" + dosClass.name.replace(' ', '_').replace('/', '_').replace('\\', '_') + "/" + filetypeId;
        child = dss.getName(oldPath);
        oldPath2 = parent + "/" + child;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(ouid);
            stat = con.prepareStatement("select md$index from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? ");
            stat.setLong(1, ouidLong);
            stat.setString(2, oldPath2);
            rs = stat.executeQuery();
            if(rs.next())
                index = rs.getInt(1);
            else
                index = 1;
            if(rs.wasNull())
                index = 1;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            versionString = generateFileVersion(con, classCode, ouid, index);
            if(!versionable)
                child = ouid + "." + index + "." + versionString + "." + ((String)dosObject.get("md$number")).replace(' ', '_').replace('/', '_').replace('\\', ' ') + "." + filetype.get("extension");
            else
                child = ouid + "." + index + "." + versionString + "." + ((String)dosObject.get("md$number")).replace(' ', '_').replace('/', '_').replace('\\', ' ') + "." + ((String)dosObject.get("vf$version")).replace(' ', '_').replace('/', '_').replace('\\', ' ') + "." + filetype.get("extension");
            newPath = parent + "/" + child;
            stat = con.prepareStatement("select md$user from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? and " + "md$codate>=md$cidate");
            stat.setLong(1, ouidLong);
            stat.setString(2, oldPath2);
            wasNotCheckedIn = false;
            rs = stat.executeQuery();
            if(rs.next())
            {
                checkedInUserId = rs.getString(1);
                wasNotCheckedIn = true;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            if(!wasNotCheckedIn)
            {
                con.commit();
                con.close();
                xc.close();
                throw new IIPRequestException("Not checked-out file.");
            }
            if(!Utils.isNullString(userId) && !userId.equals(checkedInUserId))
            {
                con.commit();
                con.close();
                xc.close();
                throw new IIPRequestException("Not checked-out user. (user: " + checkedInUserId + ")");
            }
            if(doVersionup)
            {
                stat = con.prepareStatement("insert into " + classCode + "$fl " + "(md$ouid,md$path,md$version,md$des,md$filetype,md$index,md$cidate,md$codate,md$user) " + "select md$ouid, " + "?, " + "?, " + "?, " + "md$filetype, " + "md$index, " + "to_char(sysdate,'YYYYMMDDHH24MISS'), " + "null, " + "? " + "from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? and " + "rownum=1 ");
                stat.setString(1, newPath);
                stat.setString(2, versionString);
                stat.setString(3, desc);
                stat.setString(4, userId);
                stat.setLong(5, ouidLong);
                stat.setString(6, oldPath2);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
            } else
            {
                stat = con.prepareStatement("update " + classCode + "$fl " + "set md$cidate=to_char(sysdate,'YYYYMMDDHH24MISS') " + "where md$ouid=? and " + "md$path=? and " + "md$index=? ");
                stat.setLong(1, ouidLong);
                stat.setString(2, oldPath2);
                stat.setInt(3, index);
                rows = stat.executeUpdate();
                stat.close();
                stat = null;
            }
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
        if(rows < 1)
            return null;
        boolean status = false;
        status = dss.copyFile(oldPath, newPath + ".dsstemp");
        if(!status)
            throw new IIPRequestException("Can't copy file: " + newPath + ".dsstemp");
        status = dss.isFile(newPath);
        if(status)
        {
            status = dss.deleteFile(newPath);
            if(!status)
                throw new IIPRequestException("Can't delete file: " + newPath);
        }
        status = dss.renameFile(newPath + ".dsstemp", child);
        if(!status)
            throw new IIPRequestException("Can't rename file.");
        status = dss.deleteFile(oldPath);
        if(!status)
            throw new IIPRequestException("Can't delete file.");
        else
            return newPath;
    }

    public boolean setFileDescription(String ouid, HashMap file)
        throws IIPRequestException
    {
        int rows;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        if(!Utils.getBoolean(dosClass.fileControl))
            throw new IIPRequestException("Not file control class. (ouid: " + classOuid + ")");
        if(file == null || file.size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter(s): file");
        String realOuid = (String)file.get("md$ouid");
        if(Utils.isNullString(realOuid))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String path = (String)file.get("md$path");
        if(Utils.isNullString(path))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        boolean wasNotCheckedIn = false;
        HashMap context = getClientContext();
        String userId = (String)context.get("userId");
        String newPath = null;
        String parent = null;
        String child = null;
        long ouidLong = 0L;
        rows = 0;
        int index = 1;
        String desc = (String)file.get("md$description");
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        parent = dss.getParent(path);
        child = dss.getName(path);
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(realOuid);
            stat.setString(1, desc);
            stat.setLong(2, ouidLong);
            stat.setString(3, path);
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
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
        return rows >= 1;
    }

    public ArrayList getListVersionHistoryOuid(String ouid)
        throws IIPRequestException
    {
        ArrayList returnList;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        returnList = null;
        String datasourceId = null;
        String classOuid = null;
        String classCode = null;
        StringBuffer sb = null;
        Object conditionObject = null;
        boolean versionable = false;
        int i = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
        {
            dosClass = null;
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        }
        if(!Utils.getBoolean(dosClass.isLeaf))
            throw new IIPRequestException("Not a leaf class.");
        versionable = Utils.getBoolean(dosClass.versionable);
        if(!versionable)
            throw new IIPRequestException("Not a versionable class.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code.toLowerCase().replace(' ', '_');
        sb = new StringBuffer();
        sb.append("select b.vf$ouid from ");
        sb.append(classCode + "$vf a, ");
        sb.append(classCode + "$vf b where a.vf$ouid=? and b.vf$identity=a.vf$identity order by b.vf$version");
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            stat = con.prepareStatement(sb.toString());
            stat.setLong(1, Utils.getRealLongOuid(ouid));
            returnList = new ArrayList();
            for(rs = stat.executeQuery(); rs.next(); returnList.add(classCode + "$vf@" + Utils.getLongToHexString(rs, 1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            sb.setLength(0);
            sb = null;
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
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    private ArrayList getListVersionHistoryOuidInternal(Connection con, String ouid)
        throws IIPRequestException
    {
        ArrayList returnList;
        PreparedStatement stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        returnList = null;
        String datasourceId = null;
        String classOuid = null;
        String classCode = null;
        StringBuffer sb = null;
        Object conditionObject = null;
        boolean versionable = false;
        int i = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
        {
            dosClass = null;
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        }
        if(!Utils.getBoolean(dosClass.isLeaf))
            throw new IIPRequestException("Not a leaf class.");
        versionable = Utils.getBoolean(dosClass.versionable);
        if(!versionable)
            throw new IIPRequestException("Not a versionable class.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code.toLowerCase().replace(' ', '_');
        sb = new StringBuffer();
        sb.append("select b.vf$ouid from ");
        sb.append(classCode + "$vf a, ");
        sb.append(classCode + "$vf b where a.vf$ouid=? and b.vf$identity=a.vf$identity order by b.vf$version");
        try
        {
            stat = con.prepareStatement(sb.toString());
            stat.setLong(1, Utils.getRealLongOuid(ouid));
            returnList = new ArrayList();
            for(rs = stat.executeQuery(); rs.next(); returnList.add(rs.getString(1)));
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            sb.setLength(0);
            sb = null;
        }
        catch(SQLException e)
        {
            DTMUtil.sqlExceptionHelper(con, e);
        }
        finally
        {
            if(stat != null)
            {
                try
                {
                    stat.close();
                }
                catch(SQLException sqlexception) { }
                stat = null;
            }
        }
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList getListVersionHistory(String ouid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ArrayList returnList;
        ArrayList ouidList;
        String datasourceId;
        String classCode;
        StringBuffer sb;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass dosClass = null;
        returnList = null;
        ouidList = null;
        ArrayList aRow = null;
        Iterator listKey = null;
        datasourceId = null;
        String classOuid = null;
        classCode = null;
        sb = null;
        Object conditionObject = null;
        boolean versionable = false;
        int i = 0;
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
        {
            dosClass = null;
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        }
        if(!Utils.getBoolean(dosClass.isLeaf))
            throw new IIPRequestException("Not a leaf class.");
        versionable = Utils.getBoolean(dosClass.versionable);
        if(!versionable)
            throw new IIPRequestException("Not a versionable class.");
        datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        classCode = dosClass.code.toLowerCase().replace(' ', '_');
        sb = new StringBuffer();
        sb.append("select a.vf$ouid,a.md$number,a.vf$version,a.md$desc,a.md$user,a.md$status,to_char(to_date(a.md$mdate,'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS') from ");
        sb.append(classCode + "$vf a where a.vf$ouid in (");
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidList = getListVersionHistoryOuidInternal(con, ouid);
            if(!Utils.isNullArrayList(ouidList))
                break MISSING_BLOCK_LABEL_323;
            con.commit();
            arraylist = null;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return arraylist;
        Iterator listKey;
        for(listKey = ouidList.iterator(); listKey.hasNext(); sb.append((String)listKey.next()))
            if('(' != sb.charAt(sb.length() - 1))
                sb.append(',');

        listKey = null;
        sb.append(") order by a.vf$version");
        stat = con.prepareStatement(sb.toString());
        returnList = new ArrayList();
        String userId = null;
        HashMap userData = null;
        ResultSet rs;
        for(rs = stat.executeQuery(); rs.next();)
        {
            ArrayList aRow = new ArrayList();
            aRow.add(classCode + "$vf@" + Utils.getLongToHexString(rs, 1));
            aRow.add(rs.getString(2));
            aRow.add(rs.getString(3));
            aRow.add(rs.getString(4));
            userId = rs.getString(5);
            userData = aus.getUser(userId);
            if(userData != null)
            {
                aRow.add(userData.get("name") + " (" + userId + ")");
                userData.clear();
                userData = null;
            } else
            {
                aRow.add(null);
            }
            aRow.add(msr.getStgrepString((String)getClientContext().get("locale"), rs.getString(6)));
            aRow.add(rs.getString(7));
            returnList.add(aRow);
            aRow = null;
        }

        rs.close();
        rs = null;
        stat.close();
        stat = null;
        con.commit();
        sb.setLength(0);
        sb = null;
          goto _L1
        e;
        DTMUtil.sqlExceptionHelper(con, e);
_L1:
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    private void addListFromResultSet(List list, ResultSet rs, int index, int dataType)
        throws SQLException, IIPRequestException
    {
        DOSChangeable codeItem = null;
        DOSChangeable object = null;
        String codeItemOuid = null;
        String objectOuid = null;
        String description = null;
        switch(dataType)
        {
        case 1: // '\001'
            list.add(Utils.getBoolean(rs, index));
            break;

        case 6: // '\006'
            list.add(Utils.getInteger(rs, index));
            break;

        case 4: // '\004'
            list.add(Utils.getDouble(rs, index));
            break;

        case 7: // '\007'
            list.add(Utils.getLong(rs, index));
            break;

        case 8: // '\b'
            list.add(Utils.getShort(rs, index));
            break;

        case 2: // '\002'
            list.add(Utils.getByte(rs, index));
            break;

        case 5: // '\005'
            list.add(Utils.getFloat(rs, index));
            break;

        case 3: // '\003'
            list.add(Utils.getCharacter(rs, index));
            break;

        case 16: // '\020'
            objectOuid = rs.getString(index);
            if(!Utils.isNullString(objectOuid))
            {
                if(objectOuid.indexOf('$') < 0)
                {
                    list.add(objectOuid);
                    break;
                }
                object = getCoreFieldValue(objectOuid);
                if(object != null)
                {
                    description = (String)object.get("md$description");
                    if(Utils.isNullString(description))
                        list.add(object.get("md$number"));
                    else
                        list.add(description);
                    object = null;
                } else
                {
                    list.add(null);
                }
            } else
            {
                list.add(null);
            }
            break;

        case 24: // '\030'
        case 25: // '\031'
            codeItemOuid = Utils.getLongToHexString(rs, index);
            if(!Utils.isNullString(codeItemOuid))
            {
                codeItem = getCodeItem(codeItemOuid);
                if(codeItem != null)
                {
                    list.add(codeItem.get("name"));
                    codeItem = null;
                } else
                {
                    list.add(null);
                }
            } else
            {
                list.add(rs.getString(index));
            }
            break;

        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        case 20: // '\024'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        default:
            list.add(rs.getString(index));
            break;
        }
    }

    private void addListFromResultSet(DOSChangeable map, String name, ResultSet rs, int index, int dataType)
        throws SQLException, IIPRequestException
    {
        DOSChangeable codeItem = null;
        DOSChangeable object = null;
        HashMap userData = null;
        String codeItemOuid = null;
        String objectOuid = null;
        String userId = null;
        String description = null;
        switch(dataType)
        {
        case 1: // '\001'
            map.put(name, Utils.getBoolean(rs, index));
            break;

        case 6: // '\006'
            map.put(name, Utils.getInteger(rs, index));
            break;

        case 4: // '\004'
            map.put(name, Utils.getDouble(rs, index));
            break;

        case 7: // '\007'
            map.put(name, Utils.getLong(rs, index));
            break;

        case 8: // '\b'
            map.put(name, Utils.getShort(rs, index));
            break;

        case 2: // '\002'
            map.put(name, Utils.getByte(rs, index));
            break;

        case 5: // '\005'
            map.put(name, Utils.getFloat(rs, index));
            break;

        case 3: // '\003'
            map.put(name, Utils.getCharacter(rs, index));
            break;

        case 16: // '\020'
            objectOuid = rs.getString(index);
            if(!Utils.isNullString(objectOuid))
            {
                object = getCoreFieldValue(objectOuid);
                if(object != null)
                {
                    map.put(name, objectOuid);
                    description = (String)object.get("md$description");
                    if(Utils.isNullString(description))
                        map.put("name@" + name, object.get("md$number"));
                    else
                        map.put("name@" + name, object.get("md$number") + " " + description);
                    object = null;
                } else
                {
                    map.put(name, null);
                }
            } else
            {
                map.put(name, null);
            }
            break;

        case 24: // '\030'
        case 25: // '\031'
            codeItemOuid = Utils.getLongToHexString(rs, index);
            if(!Utils.isNullString(codeItemOuid))
            {
                codeItem = getCodeItem(Utils.getLongToHexString(rs, index));
                if(codeItem != null)
                {
                    map.put(name, codeItemOuid);
                    map.put("name@" + name, codeItem.get("name") + " [" + codeItem.get("codeitemid") + "]");
                    codeItem = null;
                } else
                {
                    map.put(name, null);
                }
            } else
            {
                map.put(name, null);
            }
            break;

        case 13: // '\r'
            if(name.equals("md$user"))
            {
                userId = rs.getString(index);
                userData = aus.getUser(userId);
                if(userData != null)
                {
                    map.put(name, userData.get("name") + " (" + userId + ")");
                    userData.clear();
                    userData = null;
                } else
                {
                    map.put(name, "");
                }
                break;
            }
            if(name.equals("md$status"))
                map.put(name, msr.getStgrepString((String)getClientContext().get("locale"), rs.getString(index)));
            else
                map.put(name, rs.getString(index));
            break;

        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 14: // '\016'
        case 15: // '\017'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        case 20: // '\024'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        default:
            map.put(name, rs.getString(index));
            break;
        }
    }

    private void setListFromResultSet(List list, ResultSet rs, int index, int dataType, int listIndex)
        throws SQLException, IIPRequestException
    {
        DOSChangeable codeItem = null;
        DOSChangeable object = null;
        String codeItemOuid = null;
        String objectOuid = null;
        String description = null;
        switch(dataType)
        {
        case 1: // '\001'
            list.set(listIndex, Utils.getBoolean(rs, index));
            break;

        case 6: // '\006'
            list.set(listIndex, Utils.getInteger(rs, index));
            break;

        case 4: // '\004'
            list.set(listIndex, Utils.getDouble(rs, index));
            break;

        case 7: // '\007'
            list.set(listIndex, Utils.getLong(rs, index));
            break;

        case 8: // '\b'
            list.set(listIndex, Utils.getShort(rs, index));
            break;

        case 2: // '\002'
            list.set(listIndex, Utils.getByte(rs, index));
            break;

        case 5: // '\005'
            list.set(listIndex, Utils.getFloat(rs, index));
            break;

        case 3: // '\003'
            list.set(listIndex, Utils.getCharacter(rs, index));
            break;

        case 16: // '\020'
            objectOuid = rs.getString(index);
            if(!Utils.isNullString(objectOuid))
            {
                object = getCoreFieldValue(objectOuid);
                if(object != null)
                {
                    description = (String)object.get("md$description");
                    if(Utils.isNullString(description))
                        list.set(listIndex, object.get("md$number"));
                    else
                        list.set(listIndex, description);
                    object = null;
                } else
                {
                    list.set(listIndex, null);
                }
            } else
            {
                list.set(listIndex, null);
            }
            break;

        case 24: // '\030'
        case 25: // '\031'
            codeItemOuid = Utils.getLongToHexString(rs, index);
            if(!Utils.isNullString(codeItemOuid))
            {
                codeItem = getCodeItem(codeItemOuid);
                if(codeItem != null)
                {
                    list.set(listIndex, codeItem.get("name"));
                    codeItem = null;
                } else
                {
                    list.set(listIndex, null);
                }
            } else
            {
                list.set(listIndex, null);
            }
            break;

        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        case 20: // '\024'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        default:
            list.set(listIndex, rs.getString(index));
            break;
        }
    }

    private void addStringBufferFromField(StringBuffer sb, int typeArray[], int i, DOSField dosField, StringBuffer sb1)
    {
        String code = dosField.code.toLowerCase().replace(' ', '_');
        String ouid = Long.toHexString(dosField.OUID);
        switch(typeArray[i])
        {
        case 21: // '\025'
            sb.append("to_char(to_date(");
            sb.append(code);
            sb.append(",'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS')");
            if(!reservedFields.contains(code))
            {
                sb.append(" as f$");
                sb.append(ouid);
                sb1.append("f$" + ouid);
            } else
            {
                sb.append(" as " + code);
                sb1.append(code);
            }
            break;

        case 22: // '\026'
            sb.append("to_char(to_date(");
            sb.append(code);
            sb.append(",'YYYYMMDD'),'YYYY-MM-DD')");
            if(!reservedFields.contains(code))
            {
                sb.append(" as f$");
                sb.append(ouid);
                sb1.append("f$" + ouid);
            } else
            {
                sb.append(" as " + code);
                sb1.append(code);
            }
            break;

        case 23: // '\027'
            sb.append("to_char(to_date(");
            sb.append(code);
            sb.append(",'HH24MISS'),'HH24:MI:SS')");
            if(!reservedFields.contains(code))
            {
                sb.append(" as f$");
                sb.append(ouid);
                sb1.append("f$" + ouid);
            } else
            {
                sb.append(" as " + code);
                sb1.append(code);
            }
            break;

        case -1: 
            sb.append("null");
            if(!reservedFields.contains(code))
            {
                sb.append("||'' as f$");
                sb.append(ouid);
                sb1.append("f$" + ouid);
            } else
            {
                sb.append(" as " + code);
                sb1.append(code);
            }
            break;

        case -13: 
            sb.append("null");
            if(!reservedFields.contains(code))
            {
                sb.append(" as c$");
                sb.append(ouid);
                sb1.append("c$" + ouid);
            } else
            {
                sb.append(" as " + code);
                sb1.append(code);
            }
            break;

        default:
            sb.append(code);
            if(!reservedFields.contains(code))
            {
                sb.append("||'' as f$");
                sb.append(ouid);
                sb1.append("f$" + ouid);
            } else
            {
                sb.append(" as " + code);
                sb1.append(code);
            }
            break;
        }
    }

    private void advancedAddStringBufferFromField(StringBuffer sb, int typeArray[], int i, DOSField dosField, String tableName)
    {
        String code = dosField.code.toLowerCase().replace(' ', '_');
        String ouid = Long.toHexString(dosField.OUID);
        switch(typeArray[i])
        {
        case 21: // '\025'
            sb.append(", ");
            sb.append("to_char(to_date(");
            sb.append(tableName + "." + code);
            sb.append(",'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS')");
            sb.append(" as " + tableName + "_" + ouid);
            break;

        case 22: // '\026'
            sb.append(", ");
            sb.append("to_char(to_date(");
            sb.append(tableName + "." + code);
            sb.append(",'YYYYMMDD'),'YYYY-MM-DD')");
            sb.append(" as " + tableName + "_" + ouid);
            break;

        case 23: // '\027'
            sb.append(", ");
            sb.append("to_char(to_date(");
            sb.append(tableName + "." + code);
            sb.append(",'HH24MISS'),'HH24:MI:SS')");
            sb.append(" as " + tableName + "_" + ouid);
            break;

        case -1: 
            byte type = Utils.getByte(dosField.type);
            if(type >= 2 && type <= 8 || type == 24 || type == 25)
            {
                sb.append(", ");
                sb.append("to_number(null)");
                sb.append(" as " + tableName + "_" + ouid);
            } else
            {
                sb.append(", ");
                sb.append("null");
                sb.append(" as " + tableName + "_" + ouid);
            }
            break;

        case -13: 
            sb.append(", ");
            sb.append("null");
            sb.append(" as " + tableName + "_" + ouid);
            break;

        default:
            sb.append(", ");
            sb.append(tableName + "." + code);
            sb.append(" as " + tableName + "_" + ouid);
            break;
        }
    }

    private void determineFoundationFieldTypes(int typeArray[], int i, String code)
    {
        if(code != null)
            if(code.equals("md$number"))
                typeArray[i] = 13;
            else
            if(code.equals("md$sequence"))
                typeArray[i] = 13;
            else
            if(code.equals("vf$version"))
                typeArray[i] = 13;
            else
            if(code.equals("md$status"))
                typeArray[i] = -15;
            else
            if(code.equals("md$user"))
                typeArray[i] = -14;
            else
            if(code.equals("md$cdate") || code.equals("md$mdate"))
                typeArray[i] = 21;
    }

    private void advancedAddStringBufferFromOuid(StringBuffer sb, int typeArray[], int i, String ouid, String tableName)
    {
        if(ouid != null)
            if(ouid.equals("md$number"))
            {
                sb.append(", ");
                sb.append(tableName + "." + ouid);
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = 13;
            } else
            if(ouid.equals("md$description"))
            {
                sb.append(", ");
                sb.append(tableName + ".md$desc");
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = 13;
            } else
            if(ouid.equals("md$sequence"))
            {
                sb.append(", ");
                sb.append(tableName + "." + ouid);
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = 13;
            } else
            if(ouid.equals("vf$version"))
            {
                sb.append(", ");
                sb.append(tableName + "." + ouid);
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = 13;
            } else
            if(ouid.equals("md$status"))
            {
                sb.append(", ");
                sb.append(tableName + "." + ouid);
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = -15;
            } else
            if(ouid.equals("md$user"))
            {
                sb.append(", ");
                sb.append(tableName + "." + ouid);
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = -14;
            } else
            if(ouid.equals("md$cdate") || ouid.equals("md$mdate"))
            {
                sb.append(", ");
                sb.append("to_char(to_date(");
                sb.append(tableName + "." + ouid);
                sb.append(",'YYYYMMDDHH24MISS'),'YYYY-MM-DD HH24:MI:SS')");
                sb.append(" as " + tableName + "_" + ouid);
                typeArray[i] = 21;
            }
    }

    public ArrayList listEventForModel(String ouid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        DOSModel dosModel = (DOSModel)modelMap.get(ouid);
        if(dosModel == null)
            throw new IIPRequestException("Model not exists. (ouid: " + ouid + ")");
        ArrayList eventList = null;
        ArrayList resultList = null;
        HashMap script = null;
        Iterator eventKey = null;
        String eventType = null;
        eventList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
        if(eventList == null)
            return null;
        resultList = new ArrayList();
        for(eventKey = eventList.iterator(); eventKey.hasNext();)
        {
            eventType = (String)eventKey.next();
            script = new HashMap();
            script.put("ouid", ouid);
            script.put("type.event", eventType);
            script.put("name.script", nds.getValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid + "/" + eventType));
            resultList.add(script);
            script = null;
        }

        return resultList;
    }

    public ArrayList listEvent(String ouid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        ArrayList eventList = null;
        ArrayList resultList = null;
        HashMap script = null;
        Iterator eventKey = null;
        String eventType = null;
        eventList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
        if(eventList == null)
            return null;
        resultList = new ArrayList();
        for(eventKey = eventList.iterator(); eventKey.hasNext();)
        {
            eventType = (String)eventKey.next();
            script = new HashMap();
            script.put("ouid", ouid);
            script.put("type.event", eventType);
            script.put("name.script", nds.getValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid + "/" + eventType));
            resultList.add(script);
            script = null;
        }

        return resultList;
    }

    public boolean removeEvent(String ouid, String eventType)
        throws IIPRequestException
    {
        String scriptName = nds.getValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid + "/" + eventType);
        List tmpList = Utils.tokenizeMessage(scriptName, '.');
        String extType = null;
        if(((String)tmpList.get(1)).equals("py"))
            extType = "Python";
        else
        if(((String)tmpList.get(1)).equals("tcl"))
            extType = "Tcl";
        else
        if(((String)tmpList.get(1)).equals("java"))
            extType = "Beanshell";
        boolean result = nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid + "/" + eventType);
        ArrayList list = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
        if(Utils.isNullArrayList(list))
        {
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid);
            list = null;
        }
        nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/EVENT/" + ouid + "/" + eventType);
        list = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/EVENT/" + ouid);
        if(Utils.isNullArrayList(list))
        {
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/EVENT/" + ouid);
            list = null;
        }
        list = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/EVENT");
        if(Utils.isNullArrayList(list))
        {
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/EVENT");
            list = null;
        }
        updateTimeToken();
        return result;
    }

    public boolean setEvent(String ouid, String eventType, String scriptName)
        throws IIPRequestException
    {
        nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT", ouid, "DOS.EVENT", "DOS.EVENT");
        boolean result = nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid, eventType, "DOS.EVENT", scriptName);
        if(!result)
            result = nds.setValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + ouid + "/" + eventType, scriptName);
        if(result)
        {
            List tmpList = Utils.tokenizeMessage(scriptName, '.');
            String extType = "";
            if(((String)tmpList.get(1)).equals("py"))
                extType = "Python";
            else
            if(((String)tmpList.get(1)).equals("tcl"))
                extType = "Tcl";
            else
            if(((String)tmpList.get(1)).equals("java"))
                extType = "Beanshell";
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT", extType, "DOS.SCRIPT", extType);
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType, (String)tmpList.get(0), "DOS.SCRIPT", (String)tmpList.get(0));
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0), "EVENT", "DOS.SCRIPT", "EVENT");
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/" + "EVENT", ouid, "DOS.SCRIPT", ouid);
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + extType + "/" + (String)tmpList.get(0) + "/" + "EVENT" + "/" + ouid, eventType, "DOS.SCRIPT", eventType);
        }
        updateTimeToken();
        return result;
    }

    public String getEventName(String ouid, String eventType)
        throws IIPRequestException
    {
        ArrayList superList = null;
        String tempString = null;
        if(!Utils.isNullString(ouid))
        {
            superList = listAllSuperClassOuid(ouid);
            if(superList == null)
                superList = new ArrayList();
            superList.add(ouid);
        } else
        {
            return null;
        }
        for(int i = superList.size() - 1; i >= 0; i--)
        {
            tempString = nds.getValue("::/DOS_SYSTEM_DIRECTORY/SCRIPT/" + superList.get(i) + "/" + eventType);
            if(!Utils.isNullString(tempString))
                break;
        }

        return tempString;
    }

    public String createCode(String modelOuid, DOSChangeable codeDefinition)
        throws IIPRequestException
    {
        String result = DOSCode.createCode(modelOuid, codeDefinition, dtm, modelMap, codeMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public String createCode(DOSChangeable codeDefinition)
        throws IIPRequestException
    {
        String modelOuid = null;
        HashMap context = getClientContext();
        if(context == null)
            return null;
        modelOuid = (String)context.get("working.model");
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("First, Set working model.");
        else
            return createCode(modelOuid, codeDefinition);
    }

    public void removeCode(String ouid)
        throws IIPRequestException
    {
        DOSCode.removeCode(ouid, dtm, codeMap);
        updateTimeToken();
    }

    public DOSChangeable getCode(String ouid)
        throws IIPRequestException
    {
        return DOSCode.getCode(ouid, codeMap);
    }

    public void setCode(DOSChangeable codeDefinition)
        throws IIPRequestException
    {
        DOSCode.setCode(codeDefinition, dtm, codeMap, codeItemMap);
        updateTimeToken();
    }

    public ArrayList listCode(String modelOuid)
        throws IIPRequestException
    {
        return DOSCode.listCode(modelOuid, dtm, modelMap, codeMap);
    }

    public ArrayList listCode()
        throws IIPRequestException
    {
        String modelOuid = null;
        HashMap context = getClientContext();
        if(context == null)
            return null;
        modelOuid = (String)context.get("working.model");
        if(Utils.isNullString(modelOuid))
            throw new IIPRequestException("First, Set working model.");
        else
            return listCode(modelOuid);
    }

    public DOSChangeable getCodeWithName(String name)
        throws IIPRequestException
    {
        if(Utils.isNullString(name))
            throw new IIPRequestException("Parameter value is null: name");
        ArrayList listCode = listCode();
        DOSChangeable dosCode = null;
        if(listCode != null)
        {
            for(int i = 0; i < listCode.size(); i++)
            {
                dosCode = (DOSChangeable)listCode.get(i);
                if(dosCode.get("name").equals(name))
                {
                    listCode = null;
                    return dosCode;
                }
            }

        }
        return null;
    }

    public ArrayList listAllCode()
        throws IIPRequestException
    {
        return DOSCode.listAllCode(dtm, modelMap, codeMap, codeItemMap);
    }

    public String createCodeItem(String codeOuid, DOSChangeable codeItemDefinition)
        throws IIPRequestException
    {
        String result = DOSCodeItem.createCodeItem(codeOuid, codeItemDefinition, dtm, codeMap, codeItemMap);
        if(result != null)
            updateTimeToken();
        return result;
    }

    public void removeCodeItem(String ouid)
        throws IIPRequestException
    {
        DOSCodeItem.removeCodeItem(ouid, dtm, codeItemMap);
        updateTimeToken();
    }

    public void removeCodeItemInCode(String codeouid)
        throws IIPRequestException
    {
        DOSCodeItem.removeCodeItemInCode(codeouid, dtm, codeMap, codeItemMap);
        updateTimeToken();
    }

    public DOSChangeable getCodeItem(String ouid)
        throws IIPRequestException
    {
        return DOSCodeItem.getCodeItem(ouid, codeItemMap);
    }

    public DOSChangeable getCodeItemWithName(String codeOuid, String name)
        throws IIPRequestException
    {
        if(Utils.isNullString(codeOuid))
            throw new IIPRequestException("Parameter value is null: codeOuid");
        if(Utils.isNullString(name))
            throw new IIPRequestException("Parameter value is null: name");
        ArrayList listCodeItem = listCodeItem(codeOuid);
        DOSChangeable dosCodeItem = null;
        if(listCodeItem != null)
        {
            for(int i = 0; i < listCodeItem.size(); i++)
            {
                dosCodeItem = (DOSChangeable)listCodeItem.get(i);
                if(dosCodeItem.get("name").equals(name))
                {
                    listCodeItem = null;
                    return dosCodeItem;
                }
            }

        }
        return null;
    }

    public DOSChangeable getCodeItemWithId(String codeOuid, String Id)
        throws IIPRequestException
    {
        if(Utils.isNullString(codeOuid))
            throw new IIPRequestException("Parameter value is null: codeOuid");
        if(Utils.isNullString(Id))
            throw new IIPRequestException("Parameter value is null: Id");
        ArrayList listCodeItem = listCodeItem(codeOuid);
        DOSChangeable dosCodeItem = null;
        if(listCodeItem != null)
        {
            for(int i = 0; i < listCodeItem.size(); i++)
            {
                dosCodeItem = (DOSChangeable)listCodeItem.get(i);
                if(dosCodeItem.get("codeitemid").equals(Id))
                {
                    listCodeItem = null;
                    return dosCodeItem;
                }
            }

        }
        return null;
    }

    public void setCodeItem(DOSChangeable codeItemDefinition)
        throws IIPRequestException
    {
        DOSCodeItem.setCodeItem(codeItemDefinition, dtm, codeMap, codeItemMap);
        updateTimeToken();
    }

    public ArrayList listCodeItem(String codeOuid)
        throws IIPRequestException
    {
        return DOSCodeItem.listCodeItem(codeOuid, dtm, codeMap, codeItemMap);
    }

    public ArrayList listAllCodeItem()
        throws IIPRequestException
    {
        return DOSCodeItem.listAllCodeItem(dtm, codeMap, codeItemMap);
    }

    public DOSChangeable getCodeItemRoot(String codeOuid)
        throws IIPRequestException
    {
        return DOSCodeItem.getCodeItemRoot(codeOuid, dtm, codeMap, codeItemMap);
    }

    public ArrayList getCodeItemChildren(String codeItemOuid)
        throws IIPRequestException
    {
        return DOSCodeItem.getCodeItemChildren(codeItemOuid, codeMap, codeItemMap);
    }

    public DOSChangeable getCodeItemParent(String codeItemOuid)
        throws IIPRequestException
    {
        return DOSCodeItem.getCodeItemParent(codeItemOuid, codeMap, codeItemMap);
    }

    public void setInstanceCachSize(int size)
        throws IIPRequestException
    {
        synchronized(instanceCache)
        {
            INSTANCE_CACHE_SIZE = size;
        }
    }

    public void clearInstanceCache()
        throws IIPRequestException
    {
        synchronized(instanceCache)
        {
            instanceCache.clear();
            instanceCacheTag.clear();
        }
    }

    public int getInstanceCacheSize()
        throws IIPRequestException
    {
        HashMap hashmap = instanceCache;
        JVM INSTR monitorenter ;
        return INSTANCE_CACHE_SIZE;
        hashmap;
        JVM INSTR monitorexit ;
        throw ;
    }

    public int getInstanceCacheContentSize()
        throws IIPRequestException
    {
        HashMap hashmap = instanceCache;
        JVM INSTR monitorenter ;
        return instanceCacheTag.size();
        hashmap;
        JVM INSTR monitorexit ;
        throw ;
    }

    private HashMap listEffectiveAssociationInternal(String classOuid, Boolean isFrom, String mode)
        throws IIPRequestException
    {
        String direction;
        if(Utils.getBoolean(isFrom))
            direction = "from";
        else
            direction = "to";
        return listEffectiveAssociationInternal(classOuid, direction, mode, true);
    }

    private HashMap listEffectiveAssociationInternal(String classOuid, String direction, String mode, boolean navigableOnly)
        throws IIPRequestException
    {
        DOSClass startClass = null;
        DOSClass superClass = null;
        DOSClass dosClass = null;
        DOSAssociation dosAssociation = null;
        LinkedList list0 = null;
        HashMap results = null;
        ArrayList allSuperList = null;
        ArrayList allLeafClassList = null;
        byte assoType = 0;
        boolean isNavigable = true;
        boolean isFrom = true;
        boolean isTo = false;
        boolean doAssociation = false;
        boolean doComposition = true;
        Utils.checkMandatoryString(classOuid, "classOuid");
        startClass = (DOSClass)classMap.get(classOuid);
        if(startClass == null)
        {
            startClass = null;
            throw new IIPRequestException("Class not exists. (classOuid: " + classOuid + ")");
        }
        if(!Utils.isNullString(direction))
            if(direction.equals("from"))
            {
                isFrom = true;
                isTo = false;
            } else
            if(direction.equals("to"))
            {
                isFrom = false;
                isTo = true;
            } else
            if(direction.equals("both"))
            {
                isFrom = true;
                isTo = true;
            }
        if(!Utils.isNullString(mode))
            if(mode.equals("association"))
            {
                doAssociation = true;
                doComposition = false;
            } else
            if(mode.equals("composition"))
            {
                doAssociation = false;
                doComposition = true;
            } else
            if(mode.equals("all"))
            {
                doAssociation = true;
                doComposition = true;
            }
        results = new HashMap();
        allSuperList = listAllSuperClassInternal(classOuid);
        if(allSuperList == null)
            allSuperList = new ArrayList();
        allSuperList.add(0, startClass);
        if(allSuperList != null)
        {
            for(Iterator superKey = allSuperList.iterator(); superKey.hasNext();)
            {
                superClass = (DOSClass)superKey.next();
                if(superClass != null)
                {
                    if((doAssociation || doComposition && isFrom) && superClass.end1List != null && superClass.end1List.size() > 0)
                    {
                        list0 = superClass.end1List;
                        for(int x = 0; x < list0.size(); x++)
                        {
                            dosAssociation = (DOSAssociation)list0.get(x);
                            assoType = Utils.getByte(dosAssociation.type);
                            isNavigable = Utils.getBoolean(dosAssociation.end2_isNavigable, true);
                            if((navigableOnly && isNavigable || !navigableOnly) && (assoType == 1 && doAssociation || assoType != 1 && doComposition))
                            {
                                if(!Utils.getBoolean(dosAssociation.end2_class.isLeaf))
                                {
                                    allLeafClassList = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end2_class.OUID));
                                    if(allLeafClassList == null)
                                        allLeafClassList = new ArrayList(1);
                                } else
                                {
                                    allLeafClassList = new ArrayList(1);
                                    allLeafClassList.add(Long.toHexString(dosAssociation.end2_class.OUID));
                                }
                                for(int y = 0; y < allLeafClassList.size(); y++)
                                {
                                    dosClass = (DOSClass)classMap.get((String)allLeafClassList.get(y));
                                    if(!results.containsKey(Long.toHexString(dosClass.OUID)))
                                        results.put(Long.toHexString(dosClass.OUID), dosAssociation);
                                    dosClass = null;
                                }

                                allLeafClassList.clear();
                                allLeafClassList = null;
                            }
                            dosAssociation = null;
                        }

                        list0 = null;
                    }
                    if((doAssociation || doComposition && isTo) && superClass.end2List != null && superClass.end2List.size() > 0)
                    {
                        list0 = superClass.end2List;
                        for(int x = 0; x < list0.size(); x++)
                        {
                            dosAssociation = (DOSAssociation)list0.get(x);
                            assoType = Utils.getByte(dosAssociation.type);
                            isNavigable = Utils.getBoolean(dosAssociation.end1_isNavigable, true);
                            if((navigableOnly && isNavigable || !navigableOnly) && (assoType == 1 && doAssociation || assoType != 1 && doComposition))
                            {
                                if(!Utils.getBoolean(dosAssociation.end1_class.isLeaf))
                                {
                                    allLeafClassList = listAllLeafClassOuidInModel(Long.toHexString(dosAssociation.end1_class.OUID));
                                    if(allLeafClassList == null)
                                        allLeafClassList = new ArrayList(1);
                                } else
                                {
                                    allLeafClassList = new ArrayList(1);
                                    allLeafClassList.add(Long.toHexString(dosAssociation.end1_class.OUID));
                                }
                                for(int y = 0; y < allLeafClassList.size(); y++)
                                {
                                    dosClass = (DOSClass)classMap.get((String)allLeafClassList.get(y));
                                    if(!results.containsKey(Long.toHexString(dosClass.OUID)))
                                        results.put(Long.toHexString(dosClass.OUID), dosAssociation);
                                    dosClass = null;
                                }

                                allLeafClassList.clear();
                                allLeafClassList = null;
                            }
                            dosAssociation = null;
                        }

                    }
                }
                superClass = null;
            }

            Object obj = null;
        }
        return results;
    }

    public HashMap listEffectiveAssociation(String classOuid, Boolean isFrom, String mode)
        throws IIPRequestException
    {
        HashMap results = null;
        DOSChangeable associationData = null;
        DOSAssociation dosAssociation = null;
        Iterator listKey = null;
        String tempString = null;
        String ouid = null;
        LinkedList linkedList = null;
        ArrayList arrayList = null;
        results = listEffectiveAssociationInternal(classOuid, isFrom, mode);
        if(results == null || results.size() == 0)
            return null;
        listKey = results.keySet().iterator();
        if(listKey == null)
            return null;
        while(listKey.hasNext()) 
        {
            associationData = new DOSChangeable();
            ouid = (String)listKey.next();
            dosAssociation = (DOSAssociation)results.get(ouid);
            tempString = Long.toHexString(dosAssociation.OUID);
            associationData.put("ouid", tempString);
            associationData.put("name", dosAssociation.name);
            associationData.put("description", dosAssociation.description);
            associationData.put("type", dosAssociation.type);
            if(dosAssociation._class != null)
                associationData.put("ouid@class", Long.toHexString(dosAssociation._class.OUID));
            associationData.put("code", dosAssociation.code);
            associationData.put("datasource.id", dosAssociation.datasourceId);
            if(dosAssociation.end1_class != null)
                associationData.put("end1.ouid@class", Long.toHexString(dosAssociation.end1_class.OUID));
            associationData.put("end1.name", dosAssociation.end1_name);
            associationData.put("end1.multiplicity.from", dosAssociation.end1_multiplicity_from);
            associationData.put("end1.multiplicity.to", dosAssociation.end1_multiplicity_to);
            associationData.put("end1.is.navigable", dosAssociation.end1_isNavigable);
            associationData.put("end1.is.ordered", dosAssociation.end1_isOrdered);
            linkedList = dosAssociation.end1_orderKeyField;
            if(linkedList != null && linkedList.size() > 0)
            {
                arrayList = new ArrayList(linkedList.size());
                for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                associationData.put("end1.array$ouid@field", arrayList);
                arrayList = null;
                linkedList = null;
            }
            associationData.put("end1.changeable", dosAssociation.end1_changeable);
            if(dosAssociation.end2_class != null)
                associationData.put("end2.ouid@class", Long.toHexString(dosAssociation.end2_class.OUID));
            associationData.put("end2.name", dosAssociation.end2_name);
            associationData.put("end2.multiplicity.from", dosAssociation.end2_multiplicity_from);
            associationData.put("end2.multiplicity.to", dosAssociation.end2_multiplicity_to);
            associationData.put("end2.is.navigable", dosAssociation.end2_isNavigable);
            associationData.put("end2.is.ordered", dosAssociation.end2_isOrdered);
            linkedList = dosAssociation.end2_orderKeyField;
            if(linkedList != null && linkedList.size() > 0)
            {
                arrayList = new ArrayList(linkedList.size());
                for(listKey = linkedList.iterator(); listKey.hasNext(); arrayList.add(Long.toHexString(((DOSField)listKey.next()).OUID)));
                associationData.put("end2.array$ouid@field", arrayList);
                arrayList = null;
                linkedList = null;
            }
            associationData.put("end2.changeable", dosAssociation.end2_changeable);
            results.put(ouid, associationData);
            associationData = null;
            dosAssociation = null;
        }
        listKey = null;
        return results;
    }

    public void createCADAttribute(DOSChangeable attributeDefinition)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        int rows = 0;
        if(attributeDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + attributeDefinition);
        HashMap valueMap = attributeDefinition.getValueMap();
        if(valueMap == null || valueMap.get("fieldouid") == null || valueMap.get("pdmname") == null || valueMap.get("cad3dname") == null || valueMap.get("cad2dname") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("insert into cadattr (dosfld,pdmname,cad3dname,cad2dname,defineopt,useopt,updateopt ) values (?,?,?,?,?,?,?) ");
            stat.setLong(1, Utils.convertOuidToLong((String)valueMap.get("fieldouid")));
            stat.setString(2, (String)valueMap.get("pdmname"));
            stat.setString(3, (String)valueMap.get("cad3dname"));
            stat.setString(4, (String)valueMap.get("cad2dname"));
            Utils.setInt(stat, 5, (Integer)valueMap.get("defineoption"));
            Utils.setInt(stat, 6, (Integer)valueMap.get("useoption"));
            Utils.setInt(stat, 7, (Integer)valueMap.get("updateoption"));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            updateTimeToken();
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

    public void setCADAttribute(DOSChangeable attributeDefinition)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        int rows = 0;
        if(attributeDefinition == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + attributeDefinition);
        HashMap valueMap = attributeDefinition.getValueMap();
        if(valueMap == null || valueMap.get("fieldouid") == null || valueMap.get("pdmname") == null || valueMap.get("cad3dname") == null || valueMap.get("cad2dname") == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + valueMap);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("update cadattr set pdmname = ?, cad3dname = ?, cad2dname = ?, defineopt = ?, useopt = ?, updateopt = ? where dosfld = ? ");
            stat.setString(1, (String)valueMap.get("pdmname"));
            stat.setString(2, (String)valueMap.get("cad3dname"));
            stat.setString(3, (String)valueMap.get("cad2dname"));
            Utils.setInt(stat, 4, (Integer)valueMap.get("defineoption"));
            Utils.setInt(stat, 5, (Integer)valueMap.get("useoption"));
            Utils.setInt(stat, 6, (Integer)valueMap.get("updateoption"));
            stat.setLong(7, Utils.convertOuidToLong((String)valueMap.get("fieldouid")));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            updateTimeToken();
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

    public void removeCADAttribute(String fieldOuid)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        int rows = 0;
        if(Utils.isNullString(fieldOuid))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldOuid);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("delete from cadattr where dosfld = ? ");
            stat.setLong(1, Utils.convertOuidToLong(fieldOuid));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            stat = con.prepareStatement("delete from cadattrpsn where dosfld = ? ");
            stat.setLong(1, Utils.convertOuidToLong(fieldOuid));
            rows = stat.executeUpdate();
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            updateTimeToken();
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

    public ArrayList listCADAttribute(String classOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ArrayList returnList;
        ArrayList fieldList;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        Iterator fieldKey = null;
        DOSField dosField = null;
        returnList = new ArrayList();
        fieldList = listFieldInClassInternal(classOuid);
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            for(Iterator fieldKey = fieldList.iterator(); fieldKey.hasNext();)
            {
                DOSField dosField = (DOSField)fieldKey.next();
                stat = con.prepareStatement("select pdmname, cad3dname, cad2dname, defineopt, useopt, updateopt from cadattr where dosfld = ? ");
                stat.setLong(1, dosField.OUID);
                ResultSet rs = stat.executeQuery();
                if(rs.next())
                {
                    DOSChangeable dosAttribute = new DOSChangeable();
                    dosAttribute.put("fieldouid", Long.toHexString(dosField.OUID));
                    dosAttribute.put("pdmname", rs.getString(1));
                    dosAttribute.put("cad3dname", rs.getString(2));
                    dosAttribute.put("cad2dname", rs.getString(3));
                    dosAttribute.put("type", dosField.type);
                    if(dosField.typeClass != null)
                        dosAttribute.put("type.ouid@class", Long.toHexString(dosField.typeClass.OUID));
                    else
                    if(dosField.typeCode != null)
                        dosAttribute.put("type.ouid@class", Long.toHexString(dosField.typeCode.OUID));
                    else
                    if(dosField.referencedFieldOuid != 0L)
                        dosAttribute.put("type.ouid@class", Long.toHexString(dosField.referencedFieldOuid));
                    else
                        dosAttribute.put("type.ouid@class", null);
                    dosAttribute.put("defineoption", new Integer(rs.getInt(4)));
                    dosAttribute.put("useoption", new Integer(rs.getInt(5)));
                    dosAttribute.put("updateoption", new Integer(rs.getInt(6)));
                    returnList.add(dosAttribute);
                    dosAttribute = null;
                }
                rs.close();
                rs = null;
                stat.close();
                stat = null;
            }

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

    public void saveCADAttributePosition(ArrayList attributePositionList)
        throws IIPRequestException
    {
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        boolean existData = false;
        int rows = 0;
        if(attributePositionList == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + attributePositionList);
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            for(int i = 0; i < attributePositionList.size(); i++)
            {
                DOSChangeable attributePositionData = (DOSChangeable)attributePositionList.get(i);
                HashMap valueMap = attributePositionData.getValueMap();
                stat = con.prepareStatement("select left_x, left_y, right_x, right_y   from cadattrpsn  where dosfld = ? and        drw_size = ? and        drw_scale = ? ");
                stat.setLong(1, Utils.convertOuidToLong((String)valueMap.get("fieldouid")));
                stat.setLong(2, Utils.convertOuidToLong((String)valueMap.get("drwsize")));
                stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("drwscale")));
                rs = stat.executeQuery();
                if(rs.next())
                    existData = true;
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                if(existData)
                {
                    stat = con.prepareStatement("update cadattrpsn set left_x = ?, left_y = ?, right_x = ?, right_y = ?  where dosfld = ? and drw_size = ? and drw_scale = ? ");
                    Utils.setFloat(stat, 1, (Float)valueMap.get("leftx"));
                    Utils.setFloat(stat, 2, (Float)valueMap.get("lefty"));
                    Utils.setFloat(stat, 3, (Float)valueMap.get("rightx"));
                    Utils.setFloat(stat, 4, (Float)valueMap.get("righty"));
                    stat.setLong(5, Utils.convertOuidToLong((String)valueMap.get("fieldouid")));
                    stat.setLong(6, Utils.convertOuidToLong((String)valueMap.get("drwsize")));
                    stat.setLong(7, Utils.convertOuidToLong((String)valueMap.get("drwscale")));
                    rows = stat.executeUpdate();
                    stat.close();
                    stat = null;
                    existData = false;
                } else
                {
                    stat = con.prepareStatement("insert into cadattrpsn (dosfld,drw_size,drw_scale,left_x,left_y,right_x,right_y ) values (?,?,?,?,?,?,?) ");
                    stat.setLong(1, Utils.convertOuidToLong((String)valueMap.get("fieldouid")));
                    stat.setLong(2, Utils.convertOuidToLong((String)valueMap.get("drwsize")));
                    stat.setLong(3, Utils.convertOuidToLong((String)valueMap.get("drwscale")));
                    Utils.setFloat(stat, 4, (Float)valueMap.get("leftx"));
                    Utils.setFloat(stat, 5, (Float)valueMap.get("lefty"));
                    Utils.setFloat(stat, 6, (Float)valueMap.get("rightx"));
                    Utils.setFloat(stat, 7, (Float)valueMap.get("righty"));
                    rows = stat.executeUpdate();
                    stat.close();
                    stat = null;
                }
            }

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
        return;
    }

    public ArrayList listCADAttributePosition(String fieldOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        ArrayList returnList;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnList = new ArrayList();
        int rows = 0;
        if(fieldOuid == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldOuid);
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select t1.drw_size, t2.name, t1.drw_scale, t3.name, t1.left_x, t1.left_y, t1.right_x, t1.right_y   from cadattrpsn t1, doscoditm t2, doscoditm t3, doscod t4, doscod t5  where t4.ouid = t2.doscod    and t2.ouid = t1.drw_size     and t5.ouid = t3.doscod    and t3.ouid = t1.drw_scale    and t1.dosfld = ? ");
            stat.setLong(1, Utils.convertOuidToLong(fieldOuid));
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                DOSChangeable positionData = new DOSChangeable();
                positionData.put("sizeouid", Long.toHexString(rs.getLong(1)));
                positionData.put("sizename", rs.getString(2));
                positionData.put("scaleouid", Long.toHexString(rs.getLong(3)));
                positionData.put("scalename", rs.getString(4));
                positionData.put("leftx", Utils.getFloat(rs, 5));
                positionData.put("lefty", Utils.getFloat(rs, 6));
                positionData.put("rightx", Utils.getFloat(rs, 7));
                positionData.put("righty", Utils.getFloat(rs, 8));
                returnList.add(positionData);
                positionData = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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

    public HashMap maxAttributeNameAndIndex(String classOuid)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        HashMap returnMap;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        returnMap = new HashMap();
        int rows = 0;
        if(classOuid == null)
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + classOuid);
        SQLException e;
        HashMap hashmap;
        try
        {
            xc = dtm.getPooledConnection("system");
            con = xc.getConnection();
            stat = con.prepareStatement("select max(name), max(indx)   from dosfld  where dosclas = ?    and name like 'Attr%' ");
            stat.setLong(1, Utils.convertOuidToLong(classOuid));
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next(); returnMap.put("maxindex", Utils.getInteger(rs, 2)))
                returnMap.put("maxname", rs.getString(1));

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
            hashmap = returnMap;
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        return hashmap;
        e;
        DTMUtil.sqlExceptionHelper(con, e);
        return null;
    }

    public ArrayList listInheritedFieldInFieldGroup(String fieldGroupOuid)
        throws IIPRequestException
    {
        DOSClass tempClass = null;
        TreeMap fieldGroupMap = null;
        DOSFieldGroup dosFieldGroup = null;
        DOSField dosField = null;
        ArrayList superList = null;
        ArrayList returnList = new ArrayList();
        ArrayList inheritedFieldList = new ArrayList();
        ArrayList inheritedFieldNameList = new ArrayList();
        LinkedList tempLinkedList = null;
        ArrayList tempArrayList = null;
        ArrayList tempArrayList2 = null;
        Iterator superKey = null;
        Iterator listKey = null;
        if(Utils.isNullString(fieldGroupOuid))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + fieldGroupOuid);
        dosFieldGroup = (DOSFieldGroup)this.fieldGroupMap.get(fieldGroupOuid);
        if(dosFieldGroup == null)
            return null;
        tempClass = dosFieldGroup._class;
        if(tempClass == null)
            return null;
        for(superList = listAllSuperClassInternal(Long.toHexString(tempClass.OUID)); superList != null && superList.size() > 0; superList = listAllSuperClassInternal(Long.toHexString(tempClass.OUID)))
            for(superKey = superList.iterator(); superKey.hasNext();)
            {
                tempClass = (DOSClass)superKey.next();
                fieldGroupMap = tempClass.fieldGroupMap;
                if(fieldGroupMap != null && fieldGroupMap.size() > 0 && fieldGroupMap.containsKey(dosFieldGroup.name))
                {
                    dosFieldGroup = (DOSFieldGroup)fieldGroupMap.get(dosFieldGroup.name);
                    tempLinkedList = dosFieldGroup.fieldList;
                    if(tempLinkedList != null && tempLinkedList.size() > 0)
                    {
                        tempArrayList = new ArrayList();
                        tempArrayList2 = new ArrayList();
                        for(listKey = tempLinkedList.iterator(); listKey.hasNext();)
                        {
                            dosField = (DOSField)listKey.next();
                            if(dosField != null && !inheritedFieldNameList.contains(dosField.name))
                            {
                                tempArrayList.add(Long.toHexString(dosField.OUID));
                                tempArrayList2.add(dosField.name);
                            }
                            dosField = null;
                        }

                        inheritedFieldList.addAll(0, tempArrayList);
                        inheritedFieldNameList.addAll(0, tempArrayList2);
                        tempArrayList = null;
                        tempArrayList2 = null;
                    }
                    break;
                }
            }


        ArrayList unusedFieldList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD/" + fieldGroupOuid);
        DOSChangeable returnData = null;
        for(int i = 0; i < inheritedFieldList.size(); i++)
        {
            returnData = new DOSChangeable();
            returnData.put("ouid", (String)inheritedFieldList.get(i));
            if(unusedFieldList != null && unusedFieldList.contains((String)inheritedFieldList.get(i)))
                returnData.put("used", new Boolean(false));
            else
                returnData.put("used", new Boolean(true));
            returnList.add(returnData);
            returnData = null;
        }

        if(returnList.size() == 0)
            return null;
        else
            return returnList;
    }

    public void setOLMSerivceInstance(OLM olm)
    {
        this.olm = olm;
    }

    public ArrayList advancedList(ArrayList inputList)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        String fieldCode;
        String oldDatasourceId;
        String datasourceId;
        ArrayList returnList;
        int fieldCountArray[];
        ArrayList typeArrayList;
        ArrayList indexArrayList;
        ArrayList tmpList1;
        HashMap variableCondition;
        ArrayList list1;
        int startIndex;
        String locale;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        DOSClass startClass = null;
        DOSClass dosClass = null;
        DOSField dosField = null;
        ArrayList leafClassList = null;
        Iterator fieldKey = null;
        Iterator filterKey = null;
        Iterator listKey = null;
        String classCode = null;
        String tableName = null;
        String tableAsName = null;
        String fieldOuid = null;
        fieldCode = null;
        byte fieldType = 0;
        String userId = null;
        HashMap userData = null;
        oldDatasourceId = null;
        datasourceId = null;
        StringBuffer sb0 = null;
        ArrayList oneRow = null;
        returnList = null;
        int cnt = 0;
        int index = 0;
        int fieldCount = 0;
        int fieldCountSum = 0;
        fieldCountArray = (int[])null;
        int typeArray[] = (int[])null;
        HashMap typeArrayMap = null;
        typeArrayList = new ArrayList();
        int indexArray[] = (int[])null;
        HashMap indexArrayMap = null;
        indexArrayList = new ArrayList();
        boolean versionable = false;
        boolean hasCollection = false;
        int multiplicityFrom = 0;
        int multiplicityTo = 0;
        tmpList1 = new ArrayList();
        ArrayList tmpList = null;
        HashMap tmpMap = null;
        String classOuid = null;
        HashMap fields = null;
        HashMap filterMap = null;
        ArrayList conditionList = null;
        HashMap conditionMap = null;
        Object conditionObject = null;
        String conditionType = null;
        StringBuffer selectClause = null;
        StringBuffer fromClause = null;
        StringBuffer whereClause = null;
        String operator = null;
        String operand1 = null;
        String operand2 = null;
        variableCondition = new HashMap();
        fieldCountArray = new int[inputList.size()];
        for(int i = 0; i < inputList.size(); i++)
        {
            tmpList = new ArrayList();
            classOuid = (String)((ArrayList)inputList.get(i)).get(0);
            fields = (HashMap)((ArrayList)inputList.get(i)).get(1);
            filterMap = (HashMap)((ArrayList)inputList.get(i)).get(2);
            startClass = (DOSClass)classMap.get(classOuid);
            if(startClass == null)
            {
                startClass = null;
                throw new IIPRequestException("Class not exists. (ouid: " + classOuid + ")");
            }
            if(!Utils.getBoolean(startClass.isLeaf))
            {
                leafClassList = listAllLeafClassOuidInModel(classOuid);
            } else
            {
                leafClassList = new ArrayList(1);
                leafClassList.add(classOuid);
            }
            if(leafClassList == null || leafClassList.size() == 0)
                return null;
            if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
            {
                fieldCountArray[i] = fields.size() + 2;
                for(fieldKey = fields.keySet().iterator(); fieldKey.hasNext();)
                {
                    fieldOuid = (String)fieldKey.next();
                    if(!fieldMap.containsKey(fieldOuid) && !fieldOuid.equals("md$number") && !fieldOuid.equals("vf$version") && !fieldOuid.equals("md$description") && !fieldOuid.equals("md$status") && !fieldOuid.equals("md$user") && !fieldOuid.equals("md$cdate") && !fieldOuid.equals("md$mdate"))
                    {
                        fieldKey = null;
                        throw new IIPRequestException("Result field not exists. (ouid: " + fieldOuid + ")");
                    }
                }

                fieldKey = null;
            }
            typeArrayMap = new HashMap();
            indexArrayMap = new HashMap();
            for(int j = 0; j < leafClassList.size(); j++)
            {
                cnt++;
                tmpMap = new HashMap();
                selectClause = new StringBuffer();
                fromClause = new StringBuffer();
                whereClause = new StringBuffer();
                classOuid = (String)leafClassList.get(j);
                dosClass = (DOSClass)classMap.get(classOuid);
                if(dosClass != null)
                {
                    versionable = Utils.getBoolean(dosClass.versionable);
                    classCode = dosClass.code.toLowerCase().replace(' ', '_');
                    if(versionable)
                    {
                        tableName = classCode + "$vf";
                        tableAsName = classCode + "$vf_t" + cnt;
                        selectClause.append("'" + tableName + "@'");
                        selectClause.append(" as " + tableAsName);
                        selectClause.append(", ");
                        selectClause.append(tableAsName + ".vf$ouid");
                        selectClause.append(" as " + tableAsName + "_vf$ouid");
                        fromClause.append(classCode + "$id " + classCode + "$id_t" + cnt);
                        fromClause.append(", ");
                        fromClause.append(tableName + " " + tableAsName);
                        whereClause.append(tableAsName + ".vf$ouid = " + classCode + "$id_t" + cnt + ".id$wip");
                    } else
                    {
                        tableName = classCode + "$sf";
                        tableAsName = classCode + "$sf_t" + cnt;
                        selectClause.append("'" + tableName + "@'");
                        selectClause.append(" as " + tableAsName);
                        selectClause.append(", ");
                        selectClause.append(tableAsName + ".sf$ouid");
                        selectClause.append(" as " + tableAsName + "_sf$ouid");
                        fromClause.append(tableName + " " + tableAsName);
                    }
                    index = 1;
                    if(fields != null && fields.size() > 0 && fieldMap != null && fieldMap.size() > 0)
                    {
                        typeArray = new int[fields.size() + 2];
                        indexArray = new int[fields.size() + 2];
                        for(fieldKey = fields.keySet().iterator(); fieldKey.hasNext();)
                        {
                            index++;
                            fieldOuid = (String)fieldKey.next();
                            dosField = (DOSField)fieldMap.get(fieldOuid);
                            if(dosField == null)
                            {
                                advancedAddStringBufferFromOuid(selectClause, typeArray, index, fieldOuid, tableAsName);
                                indexArray[index] = ((Integer)fields.get(fieldOuid)).intValue();
                            } else
                            {
                                multiplicityFrom = Utils.getInt(dosField.multiplicity_from);
                                multiplicityTo = Utils.getInt(dosField.multiplicity_to);
                                if(multiplicityFrom < multiplicityTo)
                                {
                                    if(multiplicityTo > 1)
                                        hasCollection = true;
                                    else
                                        hasCollection = multiplicityTo - multiplicityFrom > 1;
                                } else
                                {
                                    hasCollection = false;
                                }
                                if(hasCollection)
                                    typeArray[index] = -13;
                                else
                                    typeArray[index] = Utils.getByte(dosField.type);
                                indexArray[index] = ((Integer)fields.get(fieldOuid)).intValue();
                                advancedAddStringBufferFromField(selectClause, typeArray, index, dosField, tableAsName);
                            }
                        }

                    }
                    typeArrayMap.put(classOuid, typeArray);
                    typeArray = (int[])null;
                    indexArrayMap.put(classOuid, indexArray);
                    indexArray = (int[])null;
                    if(filterMap != null && filterMap.size() > 0)
                        for(filterKey = filterMap.keySet().iterator(); filterKey.hasNext();)
                        {
                            fieldOuid = (String)filterKey.next();
                            if(!fieldOuid.equals("version.condition.type") && !fieldOuid.equals("version.condition.date.from") && !fieldOuid.equals("version.condition.date.to"))
                            {
                                if(whereClause.length() > 0)
                                    whereClause.append(" and (");
                                else
                                    whereClause.append(" (");
                                dosField = (DOSField)fieldMap.get(fieldOuid);
                                if(fieldOuid.equals("md$number"))
                                    fieldCode = tableAsName + ".md$number";
                                else
                                if(fieldOuid.equals("md$description"))
                                    fieldCode = tableAsName + ".md$desc";
                                else
                                if(fieldOuid.equals("md$status"))
                                    fieldCode = tableAsName + ".md$status";
                                else
                                if(fieldOuid.equals("vf$version"))
                                    fieldCode = tableAsName + ".vf$version";
                                else
                                if(fieldOuid.equals("md$user"))
                                    fieldCode = tableAsName + ".md$user";
                                else
                                if(fieldOuid.equals("md$cdate"))
                                    fieldCode = tableAsName + ".md$cdate";
                                else
                                if(fieldOuid.equals("md$mdate"))
                                    fieldCode = tableAsName + ".md$mdate";
                                else
                                if(dosField != null)
                                {
                                    fieldCode = dosField.code;
                                    fieldCode = tableAsName + "." + fieldCode.toLowerCase().replace(' ', '_');
                                    fieldType = dosField.type.byteValue();
                                }
                                conditionList = (ArrayList)filterMap.get(fieldOuid);
                                for(listKey = conditionList.iterator(); listKey.hasNext();)
                                {
                                    conditionMap = (HashMap)listKey.next();
                                    conditionObject = (String)conditionMap.get("value");
                                    conditionType = (String)conditionMap.get("type");
                                    if(conditionType.equals("LINKFILTER.OPR") && (conditionObject.equals("=") || conditionObject.equals("<") || conditionObject.equals("<=") || conditionObject.equals(">") || conditionObject.equals(">=")))
                                    {
                                        operator = (String)conditionObject;
                                        conditionMap = (HashMap)listKey.next();
                                        conditionObject = (String)conditionMap.get("value");
                                        conditionType = (String)conditionMap.get("type");
                                        if(conditionType.equals("LINKFILTER.OPR"))
                                            throw new IIPRequestException("Condition is invalid.");
                                        operand1 = (String)conditionObject;
                                        if(conditionType.equals("LINKFILTER.OPD.VARIABLE"))
                                        {
                                            variableCondition.put(operand1, null);
                                            if(fieldOuid.equals("md$cdate") || fieldOuid.equals("md$mdate"))
                                                whereClause.append("to_char(to_date(" + fieldCode + ", 'YYYYMMDDHH24MISS'), 'YYYYMMDD')" + " " + operator + " " + operand1);
                                            else
                                                whereClause.append(fieldCode + " " + operator + " " + operand1);
                                        } else
                                        if(conditionType.equals("LINKFILTER.OPD.CONST"))
                                            if(dosField != null && (fieldType == 24 || fieldType == 25))
                                                whereClause.append(fieldCode + " " + operator + " " + Utils.convertOuidToLong(operand1));
                                            else
                                            if(dosField != null && fieldType == 1)
                                            {
                                                if(conditionObject.equals("true"))
                                                    whereClause.append(fieldCode + " " + operator + " 'T'");
                                                else
                                                    whereClause.append(fieldCode + " " + operator + " 'F'");
                                            } else
                                            if(dosField != null && fieldType != 1 && fieldType != 13 && fieldType != 16 && fieldType != 21 && fieldType != 22 && fieldType != 23)
                                                whereClause.append(fieldCode + " " + operator + " " + operand1);
                                            else
                                            if(fieldOuid.equals("md$cdate") || fieldOuid.equals("md$mdate"))
                                                whereClause.append(fieldCode + " like to_char(to_date('" + operand1 + "', 'YYYY-MM-DD'), 'YYYYMMDD') || '%'");
                                            else
                                                whereClause.append(fieldCode + " " + operator + " '" + operand1 + "'");
                                        operator = null;
                                        operand1 = null;
                                        operand2 = null;
                                    } else
                                    if(conditionType.equals("LINKFILTER.OPR") && conditionObject.equals("<>"))
                                    {
                                        operator = (String)conditionObject;
                                        conditionMap = (HashMap)listKey.next();
                                        conditionObject = (String)conditionMap.get("value");
                                        conditionType = (String)conditionMap.get("type");
                                        if(conditionType.equals("LINKFILTER.OPR"))
                                            throw new IIPRequestException("Condition is invalid.");
                                        operand1 = (String)conditionObject;
                                        if(conditionType.equals("LINKFILTER.OPD.VARIABLE"))
                                        {
                                            variableCondition.put(operand1, null);
                                            if(fieldOuid.equals("md$cdate") || fieldOuid.equals("md$mdate"))
                                            {
                                                whereClause.append("(to_char(to_date(" + fieldCode + ", 'YYYYMMDDHH24MISS'), 'YYYYMMDD')" + " " + operator + " " + operand1);
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            } else
                                            {
                                                whereClause.append("(" + fieldCode + " " + operator + " " + operand1);
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            }
                                        } else
                                        if(conditionType.equals("LINKFILTER.OPD.CONST"))
                                            if(dosField != null && (fieldType == 24 || fieldType == 25))
                                            {
                                                whereClause.append("(" + fieldCode + " " + operator + " " + Utils.convertOuidToLong(operand1));
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            } else
                                            if(dosField != null && fieldType == 1)
                                            {
                                                if(conditionObject.equals("true"))
                                                    whereClause.append("(" + fieldCode + " " + operator + " 'T'");
                                                else
                                                    whereClause.append("(" + fieldCode + " " + operator + " 'F'");
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            } else
                                            if(dosField != null && fieldType != 1 && fieldType != 13 && fieldType != 16 && fieldType != 21 && fieldType != 22 && fieldType != 23)
                                            {
                                                whereClause.append("(" + fieldCode + " " + operator + " " + operand1);
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            } else
                                            if(fieldOuid.equals("md$cdate") || fieldOuid.equals("md$mdate"))
                                            {
                                                whereClause.append("(" + fieldCode + " not like to_char(to_date('" + operand1 + "', 'YYYY-MM-DD'), 'YYYYMMDD') || '%'");
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            } else
                                            {
                                                whereClause.append("(" + fieldCode + " " + operator + " '" + operand1 + "'");
                                                whereClause.append(" or ");
                                                whereClause.append(fieldCode + " is null)");
                                            }
                                        operator = null;
                                        operand1 = null;
                                        operand2 = null;
                                    } else
                                    if(conditionType.equals("LINKFILTER.OPR") && conditionObject.equals(".."))
                                    {
                                        if(operand1 != null)
                                        {
                                            operator = (String)conditionObject;
                                            conditionMap = (HashMap)listKey.next();
                                            conditionObject = (String)conditionMap.get("value");
                                            conditionType = (String)conditionMap.get("type");
                                            if(conditionType.equals("LINKFILTER.OPR"))
                                                throw new IIPRequestException("Condition is invalid.");
                                            operand2 = (String)conditionObject;
                                            if(conditionType.equals("LINKFILTER.OPD.VARIABLE"))
                                                throw new IIPRequestException("Condition is invalid.");
                                            if(conditionType.equals("LINKFILTER.OPD.CONST"))
                                                if(dosField != null && (fieldType == 24 || fieldType == 25))
                                                    whereClause.append(fieldCode + " between " + Utils.convertOuidToLong(operand1) + " and " + Utils.convertOuidToLong(operand2));
                                                else
                                                if(dosField != null && fieldType != 1 && fieldType != 13 && fieldType != 16 && fieldType != 21 && fieldType != 22 && fieldType != 23)
                                                    whereClause.append(fieldCode + " between " + operand1 + " and " + operand2);
                                                else
                                                if(fieldOuid.equals("md$cdate") || fieldOuid.equals("md$mdate"))
                                                    whereClause.append(fieldCode + " between to_char(to_date('" + operand1 + "'||'-000000', 'YYYY-MM-DD-HH24MISS'), 'YYYYMMDDHH24MISS') and " + "to_char(to_date('" + operand2 + "'||'-235959', 'YYYY-MM-DD-HH24MISS'), 'YYYYMMDDHH24MISS')");
                                                else
                                                    whereClause.append(fieldCode + " between '" + operand1 + "' and '" + operand2 + "'");
                                        } else
                                        {
                                            throw new IIPRequestException("Condition is invalid.");
                                        }
                                    } else
                                    if(conditionType.equals("LINKFILTER.OPR") && conditionObject.equals("&"))
                                        whereClause.append(" and ");
                                    else
                                    if(conditionType.equals("LINKFILTER.OPR") && conditionObject.equals("|"))
                                    {
                                        whereClause.append(" or ");
                                    } else
                                    {
                                        if(conditionType.equals("LINKFILTER.OPR"))
                                            throw new IIPRequestException("Condition is invalid.");
                                        operand1 = (String)conditionObject;
                                        if(conditionType.equals("LINKFILTER.OPD.VARIABLE"))
                                            variableCondition.put(operand1, null);
                                    }
                                }

                                whereClause.append(")");
                            }
                        }

                    tmpMap.put("select", selectClause.toString());
                    tmpMap.put("from", fromClause.toString());
                    tmpMap.put("where", whereClause.toString());
                    datasourceId = dosClass.datasourceId;
                    if(Utils.isNullString(datasourceId))
                        datasourceId = "default";
                    tmpMap.put("datasource", datasourceId);
                    tmpMap.put("tablealias", tableAsName);
                    tmpList.add(tmpMap.clone());
                    tmpMap = null;
                }
            }

            tmpList1.add(tmpList.clone());
            tmpList = null;
            typeArrayList.add(typeArrayMap.clone());
            typeArrayMap = null;
            indexArrayList.add(indexArrayMap.clone());
            indexArrayMap = null;
        }

        list1 = null;
        ArrayList list2 = null;
        HashMap subMap1 = null;
        HashMap subMap2 = null;
        list1 = (ArrayList)tmpList1.get(0);
        for(int j = 1; j < tmpList1.size(); j++)
        {
            tmpList = new ArrayList();
            list2 = (ArrayList)tmpList1.get(j);
            for(int ii = 0; ii < list1.size(); ii++)
            {
                subMap1 = (HashMap)list1.get(ii);
                for(int jj = 0; jj < list2.size(); jj++)
                {
                    subMap2 = (HashMap)list2.get(jj);
                    selectClause = new StringBuffer();
                    fromClause = new StringBuffer();
                    whereClause = new StringBuffer();
                    if(((String)subMap1.get("datasource")).equals((String)subMap2.get("datasource")))
                    {
                        selectClause.append((String)subMap1.get("select"));
                        if(!Utils.isNullString((String)subMap2.get("select")))
                        {
                            selectClause.append(", ");
                            selectClause.append((String)subMap2.get("select"));
                        }
                        fromClause.append((String)subMap1.get("from"));
                        if(!Utils.isNullString((String)subMap2.get("from")))
                        {
                            fromClause.append(", ");
                            fromClause.append((String)subMap2.get("from"));
                        }
                        whereClause.append((String)subMap1.get("where"));
                        if(!Utils.isNullString((String)subMap2.get("where")))
                        {
                            whereClause.append(" and ");
                            whereClause.append((String)subMap2.get("where"));
                        }
                        tmpMap = new HashMap();
                        tmpMap.put("select", selectClause.toString());
                        tmpMap.put("from", fromClause.toString());
                        tmpMap.put("where", whereClause.toString());
                        tmpMap.put("datasource", (String)subMap1.get("datasource"));
                        tmpList.add(tmpMap.clone());
                        tmpMap = null;
                    }
                }

            }

            list1 = (ArrayList)tmpList.clone();
            tmpList = null;
        }

        String fromStr = null;
        String whereStr = null;
        Iterator mapKey = null;
        String key = null;
        String tableAlias = null;
        startIndex = 0;
        index = 0;
        locale = (String)getClientContext().get("locale");
        returnList = new ArrayList();
          goto _L1
_L3:
        StringBuffer sb0 = new StringBuffer();
        sb0.append(" select * from (");
        for(int i = startIndex; i < list1.size();)
        {
            datasourceId = (String)((HashMap)list1.get(i)).get("datasource");
            if(i > 0 && !oldDatasourceId.equals(datasourceId))
            {
                sb0.append(")");
                break;
            }
            sb0.append(" select ");
            sb0.append((String)((HashMap)list1.get(i)).get("select"));
            sb0.append(" from ");
            sb0.append((String)((HashMap)list1.get(i)).get("from"));
            sb0.append(" where ");
            String fromStr = (String)((HashMap)list1.get(i)).get("from");
            String whereStr = (String)((HashMap)list1.get(i)).get("where");
            for(Iterator mapKey = variableCondition.keySet().iterator(); mapKey.hasNext();)
            {
                String key = (String)mapKey.next();
                if(whereStr.indexOf(key) > -1)
                {
                    List tokenList = Utils.tokenizeMessage(key, '.');
                    int index = Integer.parseInt((String)tokenList.get(0));
                    String fieldOuid = (String)tokenList.get(2);
                    ArrayList lst = (ArrayList)tmpList1.get(index);
                    for(int k = 0; k < lst.size(); k++)
                    {
                        String tableAlias = (String)((HashMap)lst.get(k)).get("tablealias");
                        if(fromStr.indexOf(tableAlias) <= -1)
                            continue;
                        DOSField dosField = (DOSField)fieldMap.get(fieldOuid);
                        if(fieldOuid.equals("md$number"))
                            fieldCode = tableAlias + ".md$number";
                        else
                        if(fieldOuid.equals("md$description"))
                            fieldCode = tableAlias + ".md$desc";
                        else
                        if(fieldOuid.equals("md$status"))
                            fieldCode = tableAlias + ".md$status";
                        else
                        if(fieldOuid.equals("vf$version"))
                            fieldCode = tableAlias + ".vf$version";
                        else
                        if(fieldOuid.equals("md$user"))
                            fieldCode = tableAlias + ".md$user";
                        else
                        if(fieldOuid.equals("md$cdate"))
                            fieldCode = "to_char(to_date(" + tableAlias + ".md$cdate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')";
                        else
                        if(fieldOuid.equals("md$mdate"))
                            fieldCode = "to_char(to_date(" + tableAlias + ".md$mdate, 'YYYYMMDDHH24MISS'), 'YYYYMMDD')";
                        else
                        if(dosField != null)
                        {
                            fieldCode = dosField.code;
                            fieldCode = tableAlias + "." + fieldCode.toLowerCase().replace(' ', '_');
                        }
                        for(int keyIndex = whereStr.indexOf(key); keyIndex > -1; keyIndex = whereStr.indexOf(key))
                            whereStr = whereStr.substring(0, keyIndex) + fieldCode + whereStr.substring(keyIndex + key.length(), whereStr.length());

                        break;
                    }

                }
            }

            sb0.append(whereStr);
            if(i < list1.size() - 1)
                sb0.append(" union all ");
            oldDatasourceId = datasourceId;
            i++;
            startIndex++;
        }

        sb0.append(")");
        try
        {
            xc = dtm.getPooledConnection(oldDatasourceId);
            con = xc.getConnection();
            stat = con.prepareStatement(sb0.toString());
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                int fieldCountSum = 0;
                for(int i = 0; i < fieldCountArray.length; i++)
                    if(fieldCountArray[i] > 0)
                        fieldCountSum = (fieldCountSum + fieldCountArray[i]) - 2;

                ArrayList oneRow = new ArrayList(fieldCountSum + 1);
                for(int i = 0; i < fieldCountSum + 1; i++)
                    oneRow.add(null);

                int index = 1;
                fieldCountSum = 0;
                for(int i = 0; i < fieldCountArray.length; i++)
                {
                    int fieldCount = fieldCountArray[i];
                    fieldCountSum += fieldCountArray[i];
                    if(fieldCount > 0)
                    {
                        String tmpClassOuid = getClassOuid(rs.getString(index) + Long.toHexString(rs.getLong(index + 1)));
                        HashMap typeArrayMap = (HashMap)typeArrayList.get(i);
                        int typeArray[] = (int[])typeArrayMap.get(tmpClassOuid);
                        HashMap indexArrayMap = (HashMap)indexArrayList.get(i);
                        int indexArray[] = (int[])indexArrayMap.get(tmpClassOuid);
                        int j = index + 2;
                        for(int k = 2; j < fieldCountSum + 1; k++)
                        {
                            if(typeArray[k] == -15)
                                oneRow.set(indexArray[k], msr.getStgrepString(locale, rs.getString(j)));
                            else
                            if(typeArray[k] == -14)
                            {
                                String userId = rs.getString(j);
                                HashMap userData = aus.getUser(userId);
                                if(userData != null)
                                {
                                    oneRow.set(indexArray[k], userData.get("name") + " (" + userId + ")");
                                    userData.clear();
                                    userData = null;
                                } else
                                {
                                    oneRow.set(indexArray[k], null);
                                }
                            } else
                            if(typeArray[k] == -13)
                                oneRow.set(indexArray[k], null);
                            else
                                setListFromResultSet(oneRow, rs, j, typeArray[k], indexArray[k]);
                            j++;
                        }

                    }
                    index += fieldCount;
                }

                returnList.add(oneRow);
                oneRow = null;
            }

            rs.close();
            rs = null;
            sb0.setLength(0);
            sb0 = null;
            con.commit();
            con.close();
            con = null;
            xc.close();
            xc = null;
        }
        catch(SQLException e)
        {
            if(e != null)
            {
                System.err.println(e);
                throw new IIPRequestException(e.toString());
            }
        }
        finally
        {
            DTMUtil.closeSafely(stat, con, xc);
            stat = null;
            con = null;
            xc = null;
        }
        oldDatasourceId = datasourceId;
_L1:
        if(startIndex < list1.size()) goto _L3; else goto _L2
_L2:
        if(returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public ArrayList executeQuery(String sql, String dataSource, String type)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        Statement stat;
        ArrayList returnList;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int colCnt = -1;
        int colType = 9999;
        String colNm = null;
        Object data = null;
        returnList = new ArrayList();
        ArrayList rowList = null;
        HashMap rowMap = null;
        if(Utils.isNullString(sql))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + sql);
        if(Utils.isNullString(dataSource))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + dataSource);
        if(Utils.isNullString(type))
            throw new IIPRequestException("Miss out mandatory parameter(s) : " + type);
        SQLException e;
        ArrayList arraylist;
        try
        {
            xc = dtm.getPooledConnection(dataSource);
            con = xc.getConnection();
            stat = con.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCnt = rsmd.getColumnCount();
            while(rs.next()) 
            {
                ArrayList rowList = new ArrayList();
                HashMap rowMap = new HashMap();
                for(int i = 1; i <= colCnt; i++)
                {
                    String colNm = rsmd.getColumnName(i);
                    int colType = rsmd.getColumnType(i);
                    Object data;
                    switch(colType)
                    {
                    case -5: 
                        data = Utils.getLong(rs, i);
                        break;

                    case -2: 
                        data = rs.getBytes(i);
                        break;

                    case -7: 
                        data = Utils.getBoolean(rs, i);
                        break;

                    case 16: // '\020'
                        data = Utils.getBoolean(rs, i);
                        break;

                    case 1: // '\001'
                        data = rs.getString(i);
                        break;

                    case 3: // '\003'
                        data = rs.getBigDecimal(i);
                        if(((BigDecimal)data).scale() > 0)
                            data = new Double(((BigDecimal)data).doubleValue());
                        else
                            data = new Long(((BigDecimal)data).longValue());
                        break;

                    case 8: // '\b'
                        data = Utils.getDouble(rs, i);
                        break;

                    case 6: // '\006'
                        data = Utils.getFloat(rs, i);
                        break;

                    case 4: // '\004'
                        data = Utils.getInteger(rs, i);
                        break;

                    case -4: 
                        data = rs.getBytes(i);
                        break;

                    case -1: 
                        data = rs.getString(i);
                        break;

                    case 2: // '\002'
                        data = rs.getBigDecimal(i);
                        if(((BigDecimal)data).scale() > 0)
                            data = new Double(((BigDecimal)data).doubleValue());
                        else
                            data = new Long(((BigDecimal)data).longValue());
                        break;

                    case 7: // '\007'
                        data = Utils.getFloat(rs, i);
                        break;

                    case 5: // '\005'
                        data = Utils.getShort(rs, i);
                        break;

                    case -6: 
                        data = Utils.getByte(rs, i);
                        break;

                    case -3: 
                        data = rs.getBytes(i);
                        break;

                    case 12: // '\f'
                        data = rs.getString(i);
                        break;

                    case 0: // '\0'
                    case 9: // '\t'
                    case 10: // '\n'
                    case 11: // '\013'
                    case 13: // '\r'
                    case 14: // '\016'
                    case 15: // '\017'
                    default:
                        data = null;
                        break;
                    }
                    if(type.equals("0"))
                        rowList.add(data);
                    else
                    if(type.equals("1"))
                        rowMap.put(colNm, data);
                    data = null;
                }

                if(type.equals("0"))
                    returnList.add(rowList);
                else
                if(type.equals("1"))
                    returnList.add(rowMap);
                rowList = null;
                rowMap = null;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            con.commit();
            con.close();
            xc.close();
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

    public boolean checkDuplicateSequence(String assoOuid, String end1Ouid, String end2Ouid, String sequence)
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        boolean result;
        ArrayList compositionAssociation;
        HashMap sbMap;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        result = false;
        String classOuid1 = null;
        String ouid1 = null;
        String ouid2 = null;
        HashMap targetAssoMap = null;
        Iterator iterator = null;
        String mapKey = null;
        DOSAssociation dosAssociation = null;
        DOSClass associationClass = null;
        String associationCode = null;
        String datasourceId = null;
        compositionAssociation = new ArrayList();
        StringBuffer sb = null;
        sbMap = new HashMap();
        SQLException e;
        boolean flag;
        try
        {
            String classOuid1 = getClassOuid(end1Ouid);
            String ouid1 = end1Ouid.substring(end1Ouid.indexOf('@') + 1);
            String ouid2 = end2Ouid.substring(end2Ouid.indexOf('@') + 1);
            DOSAssociation dosAssociation = (DOSAssociation)associationMap.get(assoOuid);
            if(dosAssociation == null)
                throw new IIPRequestException("Association not exists.");
            if(!dosAssociation.type.equals(ASSOCIATION))
            {
                HashMap targetAssoMap = listEffectiveAssociationInternal(classOuid1, "from", "all", false);
                for(Iterator iterator = targetAssoMap.keySet().iterator(); iterator.hasNext();)
                {
                    String mapKey = (String)iterator.next();
                    dosAssociation = (DOSAssociation)targetAssoMap.get(mapKey);
                    DOSClass associationClass = dosAssociation._class;
                    String datasourceId = dosAssociation.datasourceId;
                    if(datasourceId == null)
                        datasourceId = "default";
                    if(associationClass != null && !dosAssociation.type.equals(ASSOCIATION) && !compositionAssociation.contains(dosAssociation))
                    {
                        compositionAssociation.add(dosAssociation);
                        StringBuffer sb = (StringBuffer)sbMap.get(datasourceId);
                        if(sb == null)
                            sb = new StringBuffer();
                        if(sb.length() > 0)
                            sb.append("union all ");
                        String associationCode = dosAssociation.code.toLowerCase().replace(' ', '_');
                        sb.append("select '1' ");
                        sb.append("from " + associationCode + "$ac ");
                        sb.append("where as$end1 = " + Utils.convertOuidToLong(ouid1) + " and md$sequence = " + sequence + " ");
                        sbMap.put(datasourceId, sb);
                    }
                }

                for(Iterator iterator = sbMap.keySet().iterator(); iterator.hasNext();)
                {
                    String datasourceId = (String)iterator.next();
                    StringBuffer sb = (StringBuffer)sbMap.get(datasourceId);
                    xc = dtm.getPooledConnection(datasourceId);
                    con = xc.getConnection();
                    stat = con.prepareStatement(sb.toString());
                    ResultSet rs = stat.executeQuery();
                    if(rs.next())
                    {
                        result = true;
                        break;
                    }
                    rs.close();
                    rs = null;
                    stat.close();
                    stat = null;
                    con.commit();
                    con.close();
                    con = null;
                }

            } else
            {
                DOSClass associationClass = dosAssociation._class;
                String datasourceId = dosAssociation.datasourceId;
                if(datasourceId == null)
                    datasourceId = "default";
                xc = dtm.getPooledConnection(datasourceId);
                con = xc.getConnection();
                String associationCode = dosAssociation.code.toLowerCase().replace(' ', '_');
                StringBuffer sb = new StringBuffer();
                sb.append("select '1' ");
                sb.append("from " + associationCode + "$ac ");
                sb.append("where as$end1 = ? and md$sequence = ? ");
                stat = con.prepareStatement(sb.toString());
                stat.setLong(1, Utils.convertOuidToLong(ouid1));
                stat.setString(2, sequence);
                ResultSet rs = stat.executeQuery();
                if(rs.next())
                {
                    result = true;
                } else
                {
                    rs.close();
                    rs = null;
                    stat.close();
                    stat = null;
                    associationCode = dosAssociation.code.toLowerCase().replace(' ', '_');
                    sb = new StringBuffer();
                    sb.append("select '1' ");
                    sb.append("from " + associationCode + "$ac ");
                    sb.append("where as$end2 = ? and md$sequence = ? ");
                    stat = con.prepareStatement(sb.toString());
                    stat.setLong(1, Utils.convertOuidToLong(ouid2));
                    stat.setString(2, sequence);
                    rs = stat.executeQuery();
                    if(rs.next())
                        result = true;
                }
                rs.close();
                rs = null;
                stat.close();
                stat = null;
                con.commit();
                con.close();
                con = null;
            }
            flag = result;
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
        return result;
    }

    public void setVersionString()
        throws IIPRequestException
    {
        ArrayList versionList = null;
        String verStr = null;
        versionList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/VERSIONSTRING");
        if(Utils.isNullArrayList(versionList))
        {
            versionStrings = null;
            versionStrings = (new String[] {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", 
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", 
                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", 
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", 
                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", 
                "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", 
                "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", 
                "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", 
                "90", "91", "92", "93", "94", "95", "96", "97", "98", "99"
            });
            return;
        }
        int listSize = versionList.size();
        versionStrings = null;
        versionStrings = new String[listSize];
        for(int i = 0; i < listSize; i++)
        {
            verStr = nds.getValue("::/DOS_SYSTEM_DIRECTORY/VERSIONSTRING/" + (String)versionList.get(i));
            versionStrings[i] = verStr;
        }

    }

    public ArrayList getVersionStringArray()
        throws IIPRequestException
    {
        ArrayList resultList = null;
        String verStr = null;
        if(versionStrings == null)
            setVersionString();
        if(versionStrings == null)
            return null;
        resultList = new ArrayList();
        for(int i = 0; i < versionStrings.length; i++)
            resultList.add(versionStrings[i]);

        return resultList;
    }

    public String checkDuplicateNumber(String classOuid, String number, String version)
        throws IIPRequestException
    {
        if(Utils.isNullString(number))
            return null;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        boolean result = false;
        ArrayList superClassList = null;
        String tmpClassOuid = null;
        boolean useUniqueNumber = false;
        DOSClass dosClass = null;
        ArrayList fieldList = null;
        DOSField dosField = null;
        String fieldOuid = null;
        HashMap filter = null;
        ArrayList dataList = null;
        dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not found : " + tmpClassOuid);
        superClassList = listAllSuperClassInternal(classOuid);
        if(superClassList == null)
            superClassList = new ArrayList();
        superClassList.add(0, dosClass);
        dosClass = null;
        for(int i = 0; i < superClassList.size(); i++)
        {
            dosClass = (DOSClass)superClassList.get(i);
            tmpClassOuid = Long.toHexString(dosClass.OUID);
            useUniqueNumber = dosClass.useUniqueNumber.booleanValue();
            if(!useUniqueNumber)
                continue;
            filter = new HashMap();
            filter.put("version.condition.type", "wip");
            fieldList = listFieldInClassInternal(tmpClassOuid);
            for(int j = 0; j < fieldList.size(); j++)
            {
                dosField = (DOSField)fieldList.get(j);
                if(dosField.name.equals("md$number"))
                    filter.put(Long.toHexString(dosField.OUID), number);
                else
                if(!Utils.isNullString(version) && dosField.name.equals("vf$version"))
                    filter.put(Long.toHexString(dosField.OUID), version);
            }

            dataList = list(tmpClassOuid, filter);
            if(dataList != null && dataList.size() > 0)
                return (String)((ArrayList)dataList.get(0)).get(0);
            break;
        }

        return null;
    }

    public String checkDuplicateNumber(String classOuid, String number)
        throws IIPRequestException
    {
        return checkDuplicateNumber(classOuid, number, null);
    }

    public void cancelCheckoutFile(String ouid, HashMap file)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            throw new IIPRequestException("Miss out mandatory parameter: ouid");
        String classOuid = getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            throw new IIPRequestException("Class ouid not assigned.");
        DOSClass dosClass = (DOSClass)classMap.get(classOuid);
        if(dosClass == null)
            throw new IIPRequestException("Class not exists. (ouid: " + ouid + ")");
        if(!Utils.getBoolean(dosClass.fileControl))
            throw new IIPRequestException("Not file control class. (ouid: " + classOuid + ")");
        if(file == null || file.size() == 0)
            throw new IIPRequestException("Miss out mandatory parameter(s): file");
        String oldPath = (String)file.get("md$path");
        if(Utils.isNullString(oldPath))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String version = (String)file.get("md$version");
        if(Utils.isNullString(version))
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        String filetypeId = (String)file.get("md$filetype.id");
        if(filetypeId == null)
            throw new IIPRequestException("Miss out mandatory parameter(s): " + file);
        HashMap filetype = dss.getFileType(filetypeId);
        if(filetype == null || filetype.size() == 0)
            throw new IIPRequestException("File type not exists. :(md$filetype.id: " + filetypeId + ")");
        boolean doVersionup = Utils.getBoolean((Boolean)file.get("do.versionup"));
        String datasourceId = dosClass.datasourceId;
        if(Utils.isNullString(datasourceId))
            datasourceId = "default";
        DOSChangeable dosObject = get(ouid);
        if(dosObject == null)
            throw new IIPRequestException("Object not exists. :(ouid: " + ouid + ")");
        boolean versionable = Utils.getBoolean(dosClass.versionable);
        boolean wasNotCheckedIn = false;
        HashMap context = getClientContext();
        String userId = (String)context.get("userId");
        String checkedInUserId = null;
        String newPath = null;
        String parent = null;
        String oldPath2 = null;
        String child = null;
        String versionString = null;
        long ouidLong = 0L;
        int rows = 0;
        int index = 1;
        String desc = (String)file.get("md$description");
        String classCode = dosClass.code.toLowerCase().replace(' ', '_');
        parent = "/" + dosClass.name.replace(' ', '_').replace('/', '_').replace('\\', '_') + "/" + filetypeId;
        child = dss.getName(oldPath);
        oldPath2 = parent + "/" + child;
        PooledConnection xc = null;
        Connection con = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceId);
            con = xc.getConnection();
            ouidLong = Utils.getRealLongOuid(ouid);
            stat = con.prepareStatement("select md$index from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? ");
            stat.setLong(1, ouidLong);
            stat.setString(2, oldPath2);
            rs = stat.executeQuery();
            if(rs.next())
                index = rs.getInt(1);
            else
                index = 1;
            if(rs.wasNull())
                index = 1;
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select md$user from " + classCode + "$fl " + "where md$ouid=? and " + "md$path=? and " + "md$codate>=md$cidate");
            stat.setLong(1, ouidLong);
            stat.setString(2, oldPath2);
            wasNotCheckedIn = false;
            rs = stat.executeQuery();
            if(rs.next())
            {
                checkedInUserId = rs.getString(1);
                wasNotCheckedIn = true;
            }
            rs.close();
            rs = null;
            stat.close();
            stat = null;
            if(!wasNotCheckedIn)
            {
                con.commit();
                con.close();
                xc.close();
                throw new IIPRequestException("Not checked-out file.");
            }
            if(!Utils.isNullString(userId) && !userId.equals(checkedInUserId))
            {
                con.commit();
                con.close();
                xc.close();
                throw new IIPRequestException("Not checked-out user. (user: " + checkedInUserId + ")");
            }
            stat = con.prepareStatement("update " + classCode + "$fl " + "set md$codate=null " + "where md$ouid=? and " + "md$path=? and " + "md$index=? ");
            stat.setLong(1, ouidLong);
            stat.setString(2, oldPath2);
            stat.setInt(3, index);
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
        return;
    }

    private DTM dtm;
    private NDS nds;
    private AUS aus;
    private DSS dss;
    private OLM olm;
    private MSR msr;
    private HashMap modelMap;
    private HashMap packageMap;
    private HashMap classMap;
    private HashMap fieldMap;
    private HashMap actionMap;
    private HashMap associationMap;
    private HashMap fieldGroupMap;
    private HashMap classOuidMap;
    private HashMap codeMap;
    private HashMap codeItemMap;
    private static HashMap instanceCache = null;
    private static LinkedList instanceCacheTag = null;
    private final String foundationPackageName = "Foundation";
    private final String fobjectName = "FObject";
    private final String fversionobjectName = "FVersionObject";
    private final String ffilecontrolName = "FFileControl";
    private final String fcadfileName = "FCADFile";
    private String foundationPackageOuid;
    private String fobjectOuid;
    private String fversionobjectOuid;
    private String ffilecontrolOuid;
    private String fcadfileOuid;
    private DOSPackage foundationPackage;
    private DOSClass fobject;
    private DOSClass fversionobject;
    private DOSClass ffilecontrol;
    private DOSClass fcadfile;
    private String NDS_CODE;
    private int INSTANCE_CACHE_SIZE;
    private String STATUS_CRT;
    private String STATUS_OBS;
    private String STATUS_CKI;
    private String STATUS_CKO;
    private String STATUS_RLS;
    private String STATUS_WIP;
    private String STATUS_RV1;
    private String STATUS_RV2;
    private String STATUS_AP1;
    private String STATUS_AP2;
    private String STATUS_REJ;
    private LinkedList reservedFields;
    public static final String NDS_SCRIPT = "::/DOS_SYSTEM_DIRECTORY/SCRIPT";
    public static final String NDS_NUMBERINGRULE = "::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE";
    public static final String NDS_INSTANCENUMBER = "::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER";
    public static final String NDS_UNUSEDINHERITEDFIELD = "::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD";
    public static final String NDS_VERSIONSTRING = "::/DOS_SYSTEM_DIRECTORY/VERSIONSTRING";
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
    public final Byte ASSOCIATION = new Byte((byte)1);
    private String versionStrings[];

}
