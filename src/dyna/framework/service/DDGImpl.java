// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DDGImpl.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.iip.server.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.DTMUtil;
import dyna.util.Utils;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;
import javax.sql.PooledConnection;

// Referenced classes of package dyna.framework.service:
//            DDG, DTM, DOS

public class DDGImpl extends ServiceServer
    implements DDG
{

    public DDGImpl()
    {
        dtm = null;
        dos = null;
        datasourceMap = null;
        tableMap = null;
        indexMap = null;
        keyMap = null;
        fkMap = null;
        packageList = null;
        classMap = null;
        assoCheckClassMap = null;
        ouidFieldMap = null;
        identityFields = null;
        versionFields = null;
        metaDataFields = null;
        collectionFields = null;
        fileFields = null;
        associationFields = null;
        associationClassFields = null;
        reservedFields = null;
        datasourceMap = new HashMap();
        tableMap = new HashMap();
        indexMap = new HashMap();
        keyMap = new HashMap();
        fkMap = new HashMap();
        classMap = new HashMap();
        assoCheckClassMap = new HashMap();
    }

    public void init(ServiceBroker serviceBroker, ServiceRecord serviceRecord, long accessIdentifier)
    {
        super.init(serviceBroker, serviceRecord, accessIdentifier);
        try
        {
            dtm = (DTM)getServiceInstance("DF30DTM1");
            dos = (DOS)getServiceInstance("DF30DOS1");
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        buildInternalVariable();
    }

    public void generateDatabase(String modelOuid)
        throws IIPRequestException
    {
        System.out.println();
        System.out.println("[Generate Database Schema]");
        analyzeClass(modelOuid);
        analyzeDatabase();
        createTable();
        modifyTable();
        createIndex();
        System.out.println("[Done]");
    }

    private void analyzeClass(String modelOuid)
        throws IIPRequestException
    {
        ArrayList arrayList = null;
        ArrayList fieldArrayList = null;
        ArrayList superList = null;
        LinkedList fieldLinkedList = null;
        LinkedList tempLinkedList = null;
        Iterator listKey = null;
        Iterator listKey2 = null;
        Iterator fieldKey = null;
        Iterator superKey = null;
        String tempString = null;
        String tempString2 = null;
        DOSChangeable tempData = null;
        DOSChangeable tempData2 = null;
        HashMap fieldDataMap = null;
        HashMap checkDup = null;
        HashMap removedClassMap = null;
        Boolean tempBoolean = null;
        byte dataType = 0;
        int multiplicityFrom = 0;
        int multiplicityTo = 0;
        boolean isVersionable = false;
        boolean isFileControl = false;
        boolean hasCollection = false;
        boolean isCollection = false;
        boolean isAssociationClass = false;
        assoCheckClassMap.clear();
        datasourceMap.clear();
        tableMap.clear();
        indexMap.clear();
        keyMap.clear();
        fkMap.clear();
        if(packageList != null)
            packageList.clear();
        classMap.clear();
        removedClassMap = new HashMap();
        checkDup = new HashMap();
        packageList = dos.listPackage(modelOuid);
        System.out.println("---------------");
        if(packageList == null || packageList.size() == 0)
            return;
        for(listKey = packageList.iterator(); listKey.hasNext();)
        {
            tempData = (DOSChangeable)listKey.next();
            if(tempData != null)
            {
                tempString = (String)tempData.get("ouid");
                System.out.println(" [Package] " + tempData.get("name"));
                tempData = null;
                arrayList = dos.listClassInPackage(tempString);
                if(arrayList != null && arrayList.size() != 0)
                {
                    listKey2 = arrayList.iterator();
                    while(listKey2.hasNext()) 
                    {
                        tempData = (DOSChangeable)listKey2.next();
                        if(tempData == null)
                            continue;
                        assoCheckClassMap.put(tempData.get("ouid"), null);
                        superList = dos.listAllSuperClassOuid((String)tempData.get("ouid"));
                        if(superList != null && superList.size() > 0)
                        {
                            for(superKey = superList.iterator(); superKey.hasNext(); assoCheckClassMap.put(superKey.next(), null));
                            superKey = null;
                            superList.clear();
                            superList = null;
                        }
                        tempString = (String)tempData.get("code");
                        if(Utils.isNullString(tempString))
                        {
                            tempData = null;
                            continue;
                        }
                        tempString = tempString.toLowerCase().replace(' ', '_');
                        System.out.print(" [Class] " + tempData.get("name") + "(" + tempString + ")");
                        tempBoolean = (Boolean)tempData.get("is.abstract");
                        if(tempBoolean != null && tempBoolean.booleanValue())
                        {
                            tempData = null;
                            System.out.println(" *abstract class* ");
                            continue;
                        }
                        if(checkDup.containsKey(tempString))
                        {
                            tempData = null;
                            System.out.println(" *duplicated code* ");
                            continue;
                        }
                        isVersionable = Utils.getBoolean((Boolean)tempData.get("is.versionable"));
                        isFileControl = Utils.getBoolean((Boolean)tempData.get("is.filecontrol"));
                        hasCollection = false;
                        isCollection = false;
                        isAssociationClass = Utils.getBoolean((Boolean)tempData.get("is.association.class"));
                        fieldArrayList = dos.listFieldInClass((String)tempData.get("ouid"));
                        if(fieldArrayList != null && fieldArrayList.size() > 0)
                        {
                            System.out.println();
                            if(Utils.isNullString((String)tempData.get("datasource.id")))
                                tempData.put("datasource.id", "default");
                            fieldLinkedList = new LinkedList();
                            for(fieldKey = fieldArrayList.iterator(); fieldKey.hasNext();)
                            {
                                tempData2 = (DOSChangeable)fieldKey.next();
                                fieldDataMap = new HashMap();
                                dataType = Utils.getByte((Byte)tempData2.get("type"));
                                multiplicityFrom = Utils.getInt((Integer)tempData2.get("multiplicity.from"));
                                multiplicityTo = Utils.getInt((Integer)tempData2.get("multiplicity.to"));
                                if(multiplicityFrom < multiplicityTo)
                                {
                                    if(multiplicityTo > 1)
                                        isCollection = true;
                                    else
                                        isCollection = multiplicityTo - multiplicityFrom > 1;
                                } else
                                {
                                    isCollection = false;
                                }
                                if(isCollection && !hasCollection)
                                    hasCollection = true;
                                if(!isCollection)
                                {
                                    fieldDataMap.put("code", ((String)tempData2.get("code")).toLowerCase().replace(' ', '_'));
                                    fieldDataMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[dataType]]);
                                    fieldDataMap.put("is.mandatory", new Boolean(Utils.getBoolean((Boolean)tempData2.get("is.mandatory"))));
                                    if(reservedFields.contains(fieldDataMap.get("code")))
                                        fieldDataMap.clear();
                                    else
                                    if(fieldDataMap.get("code").equals("md$user"))
                                    {
                                        fieldDataMap.clear();
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("code", "md$user");
                                        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
                                        hashMap.put("is.mandatory", Utils.True);
                                        fieldLinkedList.add(hashMap);
                                        hashMap = null;
                                    } else
                                    {
                                        fieldLinkedList.add(fieldDataMap);
                                    }
                                }
                                fieldDataMap = null;
                                tempData2.clear();
                                tempData2 = null;
                            }

                            fieldKey = null;
                            if(!isAssociationClass && !isVersionable)
                            {
                                System.out.print("*sf");
                                fieldLinkedList.addAll(0, metaDataFields);
                                fieldLinkedList.addFirst(ouidFieldMap);
                                fieldLinkedList.addFirst(tempData.get("datasource.id"));
                                classMap.put(tempString + "$sf", fieldLinkedList);
                            } else
                            if(!isAssociationClass && isVersionable)
                            {
                                System.out.print("*vf");
                                tempLinkedList = new LinkedList(identityFields);
                                tempLinkedList.addFirst(tempData.get("datasource.id"));
                                classMap.put(tempString + "$id", tempLinkedList);
                                tempLinkedList = null;
                                fieldLinkedList.addAll(0, metaDataFields);
                                fieldLinkedList.addAll(0, versionFields);
                                fieldLinkedList.addFirst(tempData.get("datasource.id"));
                                classMap.put(tempString + "$vf", fieldLinkedList);
                            } else
                            if(isAssociationClass)
                            {
                                System.out.print("*ac");
                                fieldLinkedList.addAll(0, associationClassFields);
                                fieldLinkedList.addFirst(ouidFieldMap);
                                fieldLinkedList.addFirst(tempData.get("datasource.id"));
                                classMap.put(tempString + "$ac", fieldLinkedList);
                            }
                            if(hasCollection)
                            {
                                System.out.print("*cf");
                                tempLinkedList = new LinkedList(collectionFields);
                                tempLinkedList.addFirst(tempData.get("datasource.id"));
                                classMap.put(tempString + "$cf", tempLinkedList);
                                tempLinkedList = null;
                            }
                            if(isFileControl)
                            {
                                System.out.print("*fl");
                                tempLinkedList = new LinkedList(fileFields);
                                tempLinkedList.addFirst(tempData.get("datasource.id"));
                                classMap.put(tempString + "$fl", tempLinkedList);
                                tempLinkedList = null;
                            }
                            fieldArrayList.clear();
                            fieldArrayList = null;
                            fieldLinkedList = null;
                            System.out.println();
                        } else
                        {
                            tempData = null;
                            System.out.println(" *no fields* ");
                            continue;
                        }
                        checkDup.put(tempString, null);
                        tempString = (String)tempData.get("datasource.id");
                        if(!datasourceMap.containsKey(tempString))
                        {
                            System.out.println("[Datasource] " + tempString);
                            datasourceMap.put(tempString, null);
                        }
                        tempData = null;
                    }
                    listKey2 = null;
                    arrayList = null;
                }
            }
        }

        listKey = null;
        System.out.println("---------------");
        arrayList = dos.listAssociation();
        int check = 0;
        for(listKey = arrayList.iterator(); listKey.hasNext();)
        {
            tempData = (DOSChangeable)listKey.next();
            check = 0;
            tempString = ((String)tempData.get("code")).toLowerCase().replace(' ', '_');
            System.out.print("[Association] " + tempData.get("name") + "(" + tempString + ")");
            if(checkDup.containsKey(tempString))
            {
                tempData = null;
                System.out.println(" *duplicated code*");
            } else
            {
                if(assoCheckClassMap.containsKey(tempData.get("end1.ouid@class")))
                    check++;
                if(assoCheckClassMap.containsKey(tempData.get("end2.ouid@class")))
                    check++;
                if(check > 0)
                {
                    System.out.println();
                    if(assoCheckClassMap.containsKey(tempData.get("ouid@class")))
                    {
                        System.out.print("*ac");
                        tempData2 = dos.getClass((String)tempData.get("ouid@class"));
                        if(tempData2 != null && !Utils.getBoolean((Boolean)tempData2.get("is.abstract")))
                        {
                            tempString2 = (String)tempData2.get("code");
                            tempString2 = tempString2.toLowerCase().replace(' ', '_') + "$ac";
                            tempData2 = null;
                            tempLinkedList = (LinkedList)classMap.get(tempString2);
                            if(tempLinkedList != null)
                            {
                                removedClassMap.put(tempString2, tempLinkedList);
                                classMap.put(tempString + "$ac", tempLinkedList);
                                tempLinkedList = null;
                            }
                        }
                    } else
                    {
                        System.out.print("*as");
                        tempLinkedList = new LinkedList(associationFields);
                        tempLinkedList.addFirst(tempData.get("datasource.id"));
                        classMap.put(tempString + "$as", tempLinkedList);
                        tempLinkedList = null;
                    }
                    System.out.println();
                } else
                {
                    System.out.println(" *no link*");
                }
                tempData = null;
            }
        }

        listKey = null;
        arrayList = null;
        for(listKey = removedClassMap.keySet().iterator(); listKey.hasNext(); classMap.remove(listKey.next()));
        listKey = null;
        checkDup.clear();
        checkDup = null;
        System.out.println();
        System.out.println("---------------");
        Iterator mapKey;
        for(mapKey = datasourceMap.keySet().iterator(); mapKey.hasNext(); System.out.println(mapKey.next()));
        mapKey = null;
        System.out.println("---------------");
        for(mapKey = classMap.keySet().iterator(); mapKey.hasNext(); System.out.println(classMap.get(tempString)))
        {
            tempString = (String)mapKey.next();
            System.out.println("#### " + tempString + " ####");
        }

        mapKey = null;
        System.out.println("---------------");
        System.out.println(assoCheckClassMap);
        System.out.println("---------------");
    }

    private void analyzeDatabase()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        LinkedList tableList;
        LinkedList indexList;
        Iterator datasourceKey;
        xc = null;
        con = null;
        stat = null;
        ResultSet rs = null;
        tableList = null;
        LinkedList fieldList = null;
        indexList = null;
        datasourceKey = null;
        Iterator tableKey = null;
        Iterator indexKey = null;
        HashMap fieldData = null;
        String datasourceName = null;
        String tableName = null;
        String indexName = null;
        if(datasourceMap == null || datasourceMap.size() == 0)
            return;
        datasourceKey = datasourceMap.keySet().iterator();
        if(datasourceKey == null)
            return;
        System.out.println("---------- [Analyze Table] ----------");
        tableList = new LinkedList();
          goto _L1
_L3:
        String datasourceName = (String)datasourceKey.next();
        System.out.println("[Datasource] " + datasourceName);
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("select table_name tab_name from user_tables order by tab_name ");
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                String tableName = rs.getString(1);
                tableName = tableName.toLowerCase();
                tableList.add(tableName);
                LinkedList fieldList = new LinkedList();
                fieldList.add(datasourceName);
                tableMap.put(tableName, fieldList);
                fieldList = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select column_name col_name, ' '||data_type||' ('||decode (data_type,'NUMBER',decode(data_scale,0,to_char(data_precision),to_char(data_precision)||','||to_char(data_scale)),'FLOAT',data_precision,data_length)||') ', decode(nullable,'Y','N','Y') from user_tab_columns where table_name=? order by column_id");
            for(Iterator tableKey = tableList.iterator(); tableKey.hasNext();)
            {
                String tableName = (String)tableKey.next();
                LinkedList fieldList = (LinkedList)tableMap.get(tableName);
                if(fieldList == null || fieldList.size() == 0)
                {
                    fieldList = null;
                } else
                {
                    stat.setString(1, tableName.toUpperCase());
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        HashMap fieldData = new HashMap();
                        fieldData.put("code", rs.getString(1).toLowerCase());
                        fieldData.put("type", rs.getString(2).toLowerCase());
                        fieldData.put("is.mandatory", Utils.getBoolean(rs, 3));
                        fieldList.add(fieldData);
                        fieldData = null;
                    }

                    rs.close();
                    rs = null;
                    System.out.println("### " + tableName + " ###");
                    System.out.println(fieldList);
                }
            }

            stat.close();
            stat = null;
            tableList.clear();
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
_L1:
        if(datasourceKey.hasNext()) goto _L3; else goto _L2
_L2:
        datasourceKey = null;
        tableList = null;
        System.out.println("---------- [Analyze Index] ----------");
        datasourceKey = datasourceMap.keySet().iterator();
        indexList = new LinkedList();
          goto _L4
_L6:
        String datasourceName = (String)datasourceKey.next();
        System.out.println("[Datasource] " + datasourceName);
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("select index_name, table_name, decode(uniqueness,'UNIQUE','T','F'), decode(status,'VALID','T','F') from user_indexes where index_type='NORMAL' order by index_name ");
            ResultSet rs;
            for(rs = stat.executeQuery(); rs.next();)
            {
                HashMap fieldData = new HashMap();
                String indexName = rs.getString(1);
                fieldData.put("table.name", rs.getString(2));
                fieldData.put("is.unique", Utils.getBoolean(rs, 3));
                fieldData.put("is.valid", Utils.getBoolean(rs, 4));
                indexName = indexName.toLowerCase();
                indexList.add(indexName);
                LinkedList fieldList = new LinkedList();
                fieldList.add(datasourceName);
                fieldList.add(fieldData);
                indexMap.put(indexName, fieldList);
                fieldData = null;
                fieldList = null;
            }

            rs.close();
            rs = null;
            stat.close();
            stat = null;
            stat = con.prepareStatement("select column_name, decode(descend,'ASC','F','T') from user_ind_columns where index_name=? order by column_position");
            for(Iterator indexKey = indexList.iterator(); indexKey.hasNext();)
            {
                String indexName = (String)indexKey.next();
                LinkedList fieldList = (LinkedList)indexMap.get(indexName);
                if(fieldList == null || fieldList.size() == 0)
                {
                    fieldList = null;
                } else
                {
                    stat.setString(1, indexName.toUpperCase());
                    for(rs = stat.executeQuery(); rs.next();)
                    {
                        HashMap fieldData = new HashMap();
                        fieldData.put("code", rs.getString(1).toLowerCase());
                        fieldData.put("is.descending", Utils.getBoolean(rs, 2));
                        fieldList.add(fieldData);
                        fieldData = null;
                    }

                    rs.close();
                    rs = null;
                    System.out.println("### " + indexName + " ###");
                    System.out.println(fieldList);
                }
            }

            stat.close();
            stat = null;
            indexList.clear();
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
_L4:
        if(datasourceKey.hasNext()) goto _L6; else goto _L5
_L5:
        datasourceKey = null;
        indexList = null;
        System.out.println("---------------");
        return;
    }

    private void createTable()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Iterator classKey;
        xc = null;
        con = null;
        stat = null;
        LinkedList linkedList = null;
        LinkedList columnList = null;
        classKey = null;
        Iterator fieldKey = null;
        String className = null;
        String datasourceName = null;
        String fieldString = null;
        HashMap fieldData = null;
        if(classMap == null || classMap.size() == 0)
            return;
        classKey = classMap.keySet().iterator();
        if(classKey == null)
            return;
        System.out.println("---------- [Create Table] ----------");
          goto _L1
_L3:
        String className = (String)classKey.next();
        LinkedList linkedList = (LinkedList)classMap.get(className);
        String fieldString = "";
        String datasourceName = null;
        Iterator fieldKey = linkedList.iterator();
        while(fieldKey.hasNext()) 
        {
            if(datasourceName == null)
            {
                datasourceName = (String)fieldKey.next();
                if(datasourceName == null)
                    datasourceName = "default";
                continue;
            }
            if(tableMap.containsKey(className))
            {
                LinkedList columnList = (LinkedList)tableMap.get(className);
                if(columnList != null)
                {
                    if(datasourceName.equals(columnList.getFirst()))
                    {
                        columnList = null;
                        fieldKey = null;
                        break;
                    }
                } else
                {
                    columnList = null;
                    fieldKey = null;
                    break;
                }
            }
            HashMap fieldData = (HashMap)fieldKey.next();
            if(fieldString.length() > 0)
                fieldString = fieldString + ",";
            fieldString = fieldString + (String)fieldData.get("code") + (String)fieldData.get("type") + (Utils.getBoolean((Boolean)fieldData.get("is.mandatory")) ? "not null" : "null");
            fieldData = null;
        }
        fieldKey = null;
        if(Utils.isNullString(fieldString))
            continue; /* Loop/switch isn't completed */
        System.out.println("[Class] " + className);
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("create table " + className + " (" + fieldString + ")");
            stat.executeUpdate();
            stat.close();
            stat = null;
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
_L1:
        if(classKey.hasNext()) goto _L3; else goto _L2
_L2:
        System.out.println("---------------");
        return;
    }

    private void dropTable()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Iterator tableKey;
        xc = null;
        con = null;
        stat = null;
        LinkedList linkedList = null;
        tableKey = null;
        String tableDatasourceName = null;
        String tableName = null;
        if(tableMap == null || tableMap.size() == 0)
            return;
        tableKey = tableMap.keySet().iterator();
        if(tableKey == null)
            return;
        System.out.println("---------- [Drop Table] ----------");
          goto _L1
_L3:
        String tableName = (String)tableKey.next();
        String tableDatasourceName = (String)((LinkedList)tableMap.get(tableName)).getFirst();
        if(classMap.containsKey(tableName))
        {
            LinkedList linkedList = (LinkedList)classMap.get(tableName);
            if(linkedList != null && linkedList.size() > 0 && tableDatasourceName.equals(linkedList.getFirst()))
            {
                linkedList = null;
                continue; /* Loop/switch isn't completed */
            }
            linkedList = null;
        }
        System.out.println("[Table] " + tableName + " (at " + tableDatasourceName + ")");
        try
        {
            xc = dtm.getPooledConnection(tableDatasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("drop table " + tableName + " cascade constraints");
            stat.executeUpdate();
            stat.close();
            stat = null;
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
_L1:
        if(tableKey.hasNext()) goto _L3; else goto _L2
_L2:
        System.out.println("---------------");
        return;
    }

    private void modifyTable()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        LinkedList fieldList;
        LinkedList columnList;
        Iterator classKey;
        Iterator fieldKey;
        Iterator tableKey;
        Iterator columnKey;
        String className;
        String datasourceName;
        String tableName;
        HashMap fieldData;
        HashMap columnData;
        boolean modifyFieldType;
        boolean modifyFieldMandatory;
        xc = null;
        con = null;
        stat = null;
        fieldList = null;
        columnList = null;
        classKey = null;
        fieldKey = null;
        tableKey = null;
        columnKey = null;
        className = null;
        datasourceName = null;
        tableName = null;
        String fieldString = null;
        String tempString = null;
        fieldData = null;
        columnData = null;
        boolean addField = false;
        boolean dropField = false;
        modifyFieldType = false;
        modifyFieldMandatory = false;
        if(classMap == null || classMap.size() == 0 || tableMap == null || tableMap.size() == 0)
            return;
        System.out.println("---------- [Modify Table] ----------");
        classKey = classMap.keySet().iterator();
        if(classKey == null)
            return;
          goto _L1
_L6:
        className = (String)classKey.next();
        if(!tableMap.containsKey(className))
            continue; /* Loop/switch isn't completed */
        fieldList = (LinkedList)classMap.get(className);
        columnList = (LinkedList)tableMap.get(className);
        String fieldString = "";
        datasourceName = null;
        fieldKey = fieldList.iterator();
          goto _L2
_L4:
        if(datasourceName == null)
        {
            datasourceName = (String)fieldKey.next();
            if(Utils.isNullString(datasourceName))
                datasourceName = "default";
            continue; /* Loop/switch isn't completed */
        }
        fieldData = (HashMap)fieldKey.next();
        String fieldString = (String)fieldData.get("code");
        String tempString = null;
        boolean addField = true;
        modifyFieldType = true;
        modifyFieldMandatory = true;
        for(columnKey = columnList.iterator(); columnKey.hasNext(); columnData = null)
        {
            if(tempString == null)
                tempString = (String)columnKey.next();
            columnData = (HashMap)columnKey.next();
            if(!fieldString.equals(columnData.get("code")))
                continue;
            addField = false;
            if(fieldData.get("type").equals(columnData.get("type")))
                modifyFieldType = false;
            if(Utils.getBoolean((Boolean)fieldData.get("is.mandatory")) == Utils.getBoolean((Boolean)columnData.get("is.mandatory")))
                modifyFieldMandatory = false;
            columnData = null;
            break;
        }

        columnKey = null;
        if(!addField)
            break MISSING_BLOCK_LABEL_639;
        fieldString = (String)fieldData.get("code") + (String)fieldData.get("type") + (Utils.getBoolean((Boolean)fieldData.get("is.mandatory")) ? "not null" : "null");
        System.out.println("[Class] " + className + " [add field] " + fieldData.get("code"));
        fieldData = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("alter table " + className + " add (" + fieldString + ")");
            stat.executeUpdate();
            stat.close();
            stat = null;
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
        continue; /* Loop/switch isn't completed */
        if(!modifyFieldType && !modifyFieldMandatory)
            break MISSING_BLOCK_LABEL_926;
        String fieldString;
        if(modifyFieldMandatory)
            fieldString = (String)fieldData.get("code") + (String)fieldData.get("type") + (Utils.getBoolean((Boolean)fieldData.get("is.mandatory")) ? "not null" : "null");
        else
            fieldString = (String)fieldData.get("code") + (String)fieldData.get("type");
        System.out.println("[Class] " + className + " [modify field] " + fieldData.get("code"));
        fieldData = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("alter table " + className + " modify (" + fieldString + ")");
            stat.executeUpdate();
            stat.close();
            stat = null;
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
        continue; /* Loop/switch isn't completed */
        fieldData = null;
_L2:
        if(fieldKey.hasNext()) goto _L4; else goto _L3
_L3:
        fieldKey = null;
_L1:
        if(classKey.hasNext()) goto _L6; else goto _L5
_L5:
        classKey = null;
        tableKey = tableMap.keySet().iterator();
        if(tableKey == null)
            return;
          goto _L7
_L12:
        tableName = (String)tableKey.next();
        if(!classMap.containsKey(tableName))
            continue; /* Loop/switch isn't completed */
        fieldList = (LinkedList)classMap.get(tableName);
        columnList = (LinkedList)tableMap.get(tableName);
        String fieldString = "";
        datasourceName = null;
        columnKey = columnList.iterator();
          goto _L8
_L10:
        if(datasourceName == null)
        {
            datasourceName = (String)columnKey.next();
            if(Utils.isNullString(datasourceName))
                datasourceName = "default";
            continue; /* Loop/switch isn't completed */
        }
        columnData = (HashMap)columnKey.next();
        String fieldString = (String)columnData.get("code");
        String tempString = null;
        boolean dropField = true;
        for(fieldKey = fieldList.iterator(); fieldKey.hasNext(); fieldData = null)
        {
            if(tempString == null)
            {
                tempString = (String)fieldKey.next();
                if(Utils.isNullString(tempString))
                    tempString = "default";
            }
            fieldData = (HashMap)fieldKey.next();
            if(!fieldString.equals(fieldData.get("code")))
                continue;
            dropField = false;
            fieldData = null;
            break;
        }

        fieldKey = null;
        if(!dropField)
            break MISSING_BLOCK_LABEL_1381;
        fieldString = (String)columnData.get("code");
        System.out.println("[Class] " + className + " [drop field] " + fieldString);
        columnData = null;
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("alter table " + tableName + " drop (" + fieldString + ") cascade constraints");
            stat.executeUpdate();
            stat.close();
            stat = null;
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
        continue; /* Loop/switch isn't completed */
        columnData = null;
_L8:
        if(columnKey.hasNext()) goto _L10; else goto _L9
_L9:
        columnKey = null;
_L7:
        if(tableKey.hasNext()) goto _L12; else goto _L11
_L11:
        System.out.println("---------------");
        return;
    }

    private void createIndex()
        throws IIPRequestException
    {
        PooledConnection xc;
        Connection con;
        PreparedStatement stat;
        Iterator classKey;
        xc = null;
        con = null;
        stat = null;
        LinkedList classList = null;
        LinkedList columnList = null;
        classKey = null;
        Iterator fieldKey = null;
        String className = null;
        String datasourceName = null;
        String fieldString = null;
        if(classMap == null || classMap.size() == 0)
            return;
        classKey = classMap.keySet().iterator();
        if(classKey == null)
            return;
        System.out.println("---------- [Create Index] ----------");
          goto _L1
_L3:
        String className = (String)classKey.next();
        LinkedList classList = (LinkedList)classMap.get(className);
        String fieldString = "";
        String datasourceName = null;
        Iterator fieldKey = classList.iterator();
        if(fieldKey.hasNext())
        {
            if(datasourceName == null)
            {
                datasourceName = (String)fieldKey.next();
                if(datasourceName == null)
                    datasourceName = "default";
            }
            if(indexMap.containsKey("pk_" + className))
            {
                LinkedList columnList = (LinkedList)indexMap.get("pk_" + className);
                if(columnList != null)
                {
                    if(datasourceName.equals(columnList.getFirst()))
                    {
                        columnList = null;
                        fieldKey = null;
                        continue; /* Loop/switch isn't completed */
                    }
                } else
                {
                    columnList = null;
                    fieldKey = null;
                    continue; /* Loop/switch isn't completed */
                }
            }
            if(className.endsWith("$id"))
                fieldString = "id$ouid";
            else
            if(className.endsWith("$vf"))
                fieldString = "vf$ouid";
            else
            if(className.endsWith("$sf"))
                fieldString = "sf$ouid";
            else
            if(className.endsWith("$cf"))
                fieldString = "md$ouid,md$code,md$key";
            else
            if(className.endsWith("$fl"))
                fieldString = "md$ouid,md$path,md$version,md$codate";
            else
            if(className.endsWith("$as"))
                fieldString = "as$end1,as$end2";
            else
            if(className.endsWith("$ac"))
                fieldString = "sf$ouid";
        }
        fieldKey = null;
        if(Utils.isNullString(fieldString))
            continue; /* Loop/switch isn't completed */
        try
        {
            xc = dtm.getPooledConnection(datasourceName);
            con = xc.getConnection();
            stat = con.prepareStatement("create unique index pk_" + className + " on " + className + " (" + fieldString + ")");
            stat.executeUpdate();
            stat.close();
            stat = null;
            System.out.println("pk_" + className);
            if(className.endsWith("$sf") && !indexMap.containsKey("i_" + className + "_number"))
            {
                stat = con.prepareStatement("create index i_" + className + "_number on " + className + " (md$number)");
                System.out.println("i_" + className + "_number");
                stat.executeUpdate();
                stat.close();
                stat = null;
            }
            if(className.endsWith("$vf") && !indexMap.containsKey("ak_" + className))
            {
                stat = con.prepareStatement("create unique index ak_" + className + " on " + className + " (vf$identity,vf$version)");
                System.out.println("ak_" + className);
                stat.executeUpdate();
                stat.close();
                stat = null;
            }
            if(className.endsWith("$vf") && !indexMap.containsKey("i_" + className + "_number"))
            {
                stat = con.prepareStatement("create index i_" + className + "_number on " + className + " (md$number)");
                System.out.println("i_" + className + "_number");
                stat.executeUpdate();
                stat.close();
                stat = null;
            }
            if(className.endsWith("$as") && !indexMap.containsKey("i_" + className))
            {
                stat = con.prepareStatement("create index i_" + className + " on " + className + " (as$end2,as$end1)");
                System.out.println("i_" + className);
                stat.executeUpdate();
                stat.close();
                stat = null;
            }
            if(className.endsWith("$ac") && !indexMap.containsKey("i_" + className))
            {
                stat = con.prepareStatement("create index i_" + className + "_1 on " + className + " (as$end1,md$sequence,as$end2)");
                System.out.println("i_" + className + "_1");
                stat.executeUpdate();
                stat.close();
                stat = null;
                stat = con.prepareStatement("create index i_" + className + "_2 on " + className + " (as$end2,md$sequence,as$end1)");
                System.out.println("i_" + className + "_2");
                stat.executeUpdate();
                stat.close();
                stat = null;
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
_L1:
        if(classKey.hasNext()) goto _L3; else goto _L2
_L2:
        System.out.println("---------------");
        return;
    }

    private void buildInternalVariable()
    {
        HashMap hashMap = null;
        LinkedList linkedList = null;
        ouidFieldMap = new HashMap();
        ouidFieldMap.put("code", "sf$ouid");
        ouidFieldMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        ouidFieldMap.put("is.mandatory", Utils.True);
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "id$ouid");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "id$last");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "id$wip");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        identityFields = linkedList;
        linkedList = null;
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "vf$ouid");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "vf$identity");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "vf$version");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        versionFields = linkedList;
        linkedList = null;
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "md$status");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$number");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$desc");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$cdate");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[21]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$mdate");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[21]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        metaDataFields = linkedList;
        linkedList = null;
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "md$ouid");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$code");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$key");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$value");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[13]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        collectionFields = linkedList;
        linkedList = null;
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "md$ouid");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$path");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[12]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$version");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$des");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[13]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$filetype");
        hashMap.put("type", DDG.SQLTypeTable[5]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$index");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[6]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$cidate");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[21]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$codate");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[21]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$user");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        fileFields = linkedList;
        linkedList = null;
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "as$end1");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "as$end2");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        associationFields = linkedList;
        linkedList = null;
        linkedList = new LinkedList();
        hashMap = new HashMap();
        hashMap.put("code", "as$end1");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "as$end2");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[7]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$sequence");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[0]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$cdate");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[21]]);
        hashMap.put("is.mandatory", Utils.True);
        linkedList.add(hashMap);
        hashMap = null;
        hashMap = new HashMap();
        hashMap.put("code", "md$mdate");
        hashMap.put("type", DDG.SQLTypeTable[DDG.typeToSQLTypeTable[21]]);
        hashMap.put("is.mandatory", Utils.False);
        linkedList.add(hashMap);
        hashMap = null;
        associationClassFields = linkedList;
        linkedList = null;
        reservedFields = new LinkedList();
        reservedFields.add("md$number");
        reservedFields.add("md$sequence");
        reservedFields.add("md$desc");
        reservedFields.add("md$description");
        reservedFields.add("md$status");
        reservedFields.add("md$cdate");
        reservedFields.add("md$mdate");
        reservedFields.add("vf$version");
    }

    private DTM dtm;
    private DOS dos;
    private HashMap datasourceMap;
    private HashMap tableMap;
    private HashMap indexMap;
    private HashMap keyMap;
    private HashMap fkMap;
    private ArrayList packageList;
    private HashMap classMap;
    private HashMap assoCheckClassMap;
    private HashMap ouidFieldMap;
    private LinkedList identityFields;
    private LinkedList versionFields;
    private LinkedList metaDataFields;
    private LinkedList collectionFields;
    private LinkedList fileFields;
    private LinkedList associationFields;
    private LinkedList associationClassFields;
    private LinkedList reservedFields;
}
