// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CADIntegrationUtils.java

package dyna.util;

import dyna.framework.client.DynaMOAD;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import java.io.*;
import java.util.*;

// Referenced classes of package dyna.util:
//            Utils

public class CADIntegrationUtils
{

    public CADIntegrationUtils(String classOuid, DOS dos)
    {
        cadType = -1;
        totalAttrCnt = 0;
        totalAttrItemCnt = 0;
        attrItemList = new ArrayList();
        attrPositionItemList = new ArrayList();
        attrList = new ArrayList();
        attrMap = new HashMap();
        totalFileCnt = 0;
        totalFileItemCnt = 0;
        fileNameList = new ArrayList();
        fileItemList = new ArrayList();
        fileList = new ArrayList();
        cadPackageOuid = null;
        cadFileClassOuid = null;
        cadPdtClassOuid = null;
        cadPrtClassOuid = null;
        cadDrwClassOuid = null;
        cadPSClassOuid = null;
        classFieldList = null;
        attributeList = null;
        classFieldGroupList = null;
        if(dos == null)
            this.dos = DynaMOAD.dos;
        else
            this.dos = dos;
        DOSChangeable dosClass = null;
        try
        {
            dosClass = this.dos.getClass(classOuid);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        if((((String)dosClass.get("name")).equals("Solidworks Assembly") || ((String)dosClass.get("name")).equals("Solidworks Part") || ((String)dosClass.get("name")).equals("Solidworks Drawing")) && (((String)dosClass.get("code")).toLowerCase().equals("sld_assembly") || ((String)dosClass.get("code")).toLowerCase().equals("sld_part") || ((String)dosClass.get("code")).toLowerCase().equals("sld_drawing")))
            cadType = 0;
        else
        if((((String)dosClass.get("name")).equals("InventorV6 Product") || ((String)dosClass.get("name")).equals("InventorV6 Part") || ((String)dosClass.get("name")).equals("InventorV6 Drawing")) && (((String)dosClass.get("code")).toLowerCase().equals("ivtv6_product") || ((String)dosClass.get("code")).toLowerCase().equals("ivtv6_part") || ((String)dosClass.get("code")).toLowerCase().equals("ivtv6_drawing")))
            cadType = 3;
        else
        if(((String)dosClass.get("name")).equals("CATIA V4 File") && ((String)dosClass.get("code")).toLowerCase().equals("catiav4_file"))
            cadType = 4;
        else
        if(((String)dosClass.get("name")).equals("AutoCAD File") && ((String)dosClass.get("code")).toLowerCase().equals("autocad_file"))
            cadType = 5;
        initialize();
    }

    public CADIntegrationUtils(int cadType, DOS dos)
    {
        this.cadType = -1;
        totalAttrCnt = 0;
        totalAttrItemCnt = 0;
        attrItemList = new ArrayList();
        attrPositionItemList = new ArrayList();
        attrList = new ArrayList();
        attrMap = new HashMap();
        totalFileCnt = 0;
        totalFileItemCnt = 0;
        fileNameList = new ArrayList();
        fileItemList = new ArrayList();
        fileList = new ArrayList();
        cadPackageOuid = null;
        cadFileClassOuid = null;
        cadPdtClassOuid = null;
        cadPrtClassOuid = null;
        cadDrwClassOuid = null;
        cadPSClassOuid = null;
        classFieldList = null;
        attributeList = null;
        classFieldGroupList = null;
        this.cadType = cadType;
        if(dos == null)
            this.dos = DynaMOAD.dos;
        else
            this.dos = dos;
        initialize();
    }

    private void initialize()
    {
        try
        {
            if(cadType == 0)
            {
                DOSChangeable dosObject = dos.getPackageWithName("Package for Solidworks");
                cadPackageOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "Solidworks File");
                cadFileClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "Solidworks Assembly");
                cadPdtClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "Solidworks Part");
                cadPrtClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "Solidworks Drawing");
                cadDrwClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "Solidworks PS");
                cadPSClassOuid = (String)dosObject.get("ouid");
                classFieldList = dos.listFieldInClass(cadFileClassOuid);
                attributeList = dos.listCADAttribute(cadFileClassOuid);
            } else
            if(cadType == 3)
            {
                DOSChangeable dosObject = dos.getPackageWithName("Package for InventorV6");
                cadPackageOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "InventorV6 File");
                cadFileClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "InventorV6 Product");
                cadPdtClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "InventorV6 Part");
                cadPrtClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "InventorV6 Drawing");
                cadDrwClassOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "InventorV6 PS");
                cadPSClassOuid = (String)dosObject.get("ouid");
                classFieldList = dos.listFieldInClass(cadFileClassOuid);
                attributeList = dos.listCADAttribute(cadFileClassOuid);
            } else
            if(cadType == 4)
            {
                DOSChangeable dosObject = dos.getPackageWithName("Package for CATIA V4");
                cadPackageOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "CATIA V4 File");
                cadFileClassOuid = (String)dosObject.get("ouid");
                classFieldList = dos.listFieldInClass(cadFileClassOuid);
                classFieldGroupList = dos.listFieldGroupInClass(cadFileClassOuid);
            } else
            if(cadType == 5)
            {
                DOSChangeable dosObject = dos.getPackageWithName("Package for AutoCAD");
                cadPackageOuid = (String)dosObject.get("ouid");
                dosObject = dos.getClassWithName(cadPackageOuid, "AutoCAD File");
                cadFileClassOuid = (String)dosObject.get("ouid");
                classFieldList = dos.listFieldInClass(cadFileClassOuid);
                classFieldGroupList = dos.listFieldGroupInClass(cadFileClassOuid);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void readInitFile4CheckIn(String fileName)
    {
        HashMap fileMap = new HashMap();
        String originalFileName = null;
        try
        {
            FileReader iniFile = null;
            if(cadType == 0)
            {
                if(fileName.equals("modelinsert.ini"))
                    iniFile = new FileReader(System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "env" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "solidworks" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + fileName);
                else
                    iniFile = new FileReader(System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "env" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "solidworks" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + fileName);
                BufferedReader buff = new BufferedReader(iniFile);
                String line = null;
                String title = null;
                String value = null;
                List tokenList = null;
                line = buff.readLine();
                while(line != null) 
                {
                    if(line.equals("[ATTRIBUTE_INFORMATION]"))
                    {
                        for(line = buff.readLine(); line != null && !line.startsWith("[");)
                            if(line.equals(""))
                            {
                                line = buff.readLine();
                            } else
                            {
                                tokenList = Utils.tokenizeMessage(line, '=');
                                title = (String)tokenList.get(0);
                                if(tokenList.size() < 2 || ((String)tokenList.get(1)).equalsIgnoreCase("null"))
                                    value = null;
                                else
                                    value = (String)tokenList.get(1);
                                if(title.equals("TOTAL_ATTRIBUTE_COUNT"))
                                    totalAttrCnt = Integer.parseInt(value);
                                else
                                if(title.equals("TOTAL_ITEM_COUNT"))
                                    totalAttrItemCnt = Integer.parseInt(value);
                                else
                                if(!title.startsWith("SIZE_NAME") && !title.startsWith("SCALE_NAME"))
                                {
                                    attrItemList.add(value);
                                    if(value.indexOf("POSITION") > -1)
                                        attrPositionItemList.add(value);
                                }
                                line = buff.readLine();
                            }

                        if(line == null)
                            break;
                        continue;
                    }
                    if(line.equals("[ATTRIBUTE_INITIALIZATION]"))
                    {
                        String attrItem = "";
                        String attrNum = "";
                        String oldAttrNum = "";
                        for(line = buff.readLine(); line != null && !line.startsWith("[");)
                            if(line.equals(""))
                            {
                                line = buff.readLine();
                            } else
                            {
                                tokenList = Utils.tokenizeMessage(line, '=');
                                title = (String)tokenList.get(0);
                                if(tokenList.size() < 2 || ((String)tokenList.get(1)).equalsIgnoreCase("null"))
                                    value = null;
                                else
                                    value = (String)tokenList.get(1);
                                tokenList = Utils.tokenizeMessage(title, '#');
                                attrItem = (String)tokenList.get(0);
                                attrNum = (String)tokenList.get(1);
                                if(!attrNum.equals("1") && !attrNum.equals(oldAttrNum))
                                {
                                    attrList.add(attrMap.clone());
                                    attrMap.clear();
                                }
                                if(attrItemList.contains(attrItem))
                                    attrMap.put(attrItem, value);
                                oldAttrNum = attrNum;
                                line = buff.readLine();
                            }

                        attrList.add(attrMap.clone());
                        attrMap.clear();
                        if(line == null)
                            break;
                        continue;
                    }
                    if(line.equals("[FILE_INFORMATION]"))
                    {
                        for(line = buff.readLine(); line != null && !line.startsWith("[");)
                            if(line.equals(""))
                            {
                                line = buff.readLine();
                            } else
                            {
                                tokenList = Utils.tokenizeMessage(line, '=');
                                title = (String)tokenList.get(0);
                                if(tokenList.size() < 2 || ((String)tokenList.get(1)).equalsIgnoreCase("null"))
                                    value = null;
                                else
                                    value = (String)tokenList.get(1);
                                if(title.equals("TOTAL_FILE_COUNT"))
                                    totalFileCnt = Integer.parseInt(value);
                                else
                                if(title.equals("TOTAL_FILE_ITEM_COUNT"))
                                    totalFileItemCnt = Integer.parseInt(value);
                                else
                                if(title.startsWith("FILE_NAME"))
                                    fileNameList.add("[" + value + "]");
                                else
                                if(title.startsWith("FILE_ITEM"))
                                    fileItemList.add(value);
                                line = buff.readLine();
                            }

                        if(line == null)
                            break;
                        continue;
                    }
                    if(fileNameList.size() <= 0 || !fileNameList.contains(line))
                        continue;
                    fileMap.put("FILE_NAME", line.substring(1, line.length() - 1));
                    for(line = buff.readLine(); line != null && !line.startsWith("[");)
                        if(line.equals(""))
                        {
                            line = buff.readLine();
                        } else
                        {
                            tokenList = Utils.tokenizeMessage(line, '=');
                            title = (String)tokenList.get(0);
                            if(tokenList.size() < 2 || ((String)tokenList.get(1)).equalsIgnoreCase("null"))
                                value = null;
                            else
                                value = (String)tokenList.get(1);
                            if(title.equals("GLOBAL_INIT_FILE_NAME"))
                                originalFileName = value;
                            else
                                fileMap.put(title, value);
                            line = buff.readLine();
                        }

                    fileList.add(fileMap.clone());
                    fileMap.clear();
                    if(line == null)
                        break;
                }
                buff.close();
            } else
            if(cadType == 1 || cadType == 2);
        }
        catch(IOException e)
        {
            System.out.println("readInitFile : " + e);
        }
    }

    public void saveAttributePosition(String fileName)
    {
        try
        {
            FileReader iniFile = null;
            if(cadType == 0)
                iniFile = new FileReader(System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "env" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "solidworks" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + fileName);
            BufferedReader buff = new BufferedReader(iniFile);
            String line = null;
            String title = null;
            String value = null;
            List tokenList = null;
            totalAttrCnt = 0;
            totalAttrItemCnt = 0;
            attrItemList = new ArrayList();
            attrPositionItemList = new ArrayList();
            attrList = new ArrayList();
            attrMap = new HashMap();
            line = buff.readLine();
            while(line != null) 
            {
                if(line.equals("[ATTRIBUTE_INFORMATION]"))
                {
                    for(line = buff.readLine(); line != null && !line.startsWith("[");)
                        if(line.equals(""))
                        {
                            line = buff.readLine();
                        } else
                        {
                            tokenList = Utils.tokenizeMessage(line, '=');
                            title = (String)tokenList.get(0);
                            if(tokenList.size() < 2 || ((String)tokenList.get(1)).equalsIgnoreCase("null"))
                                value = null;
                            else
                                value = (String)tokenList.get(1);
                            if(title.equals("TOTAL_ATTRIBUTE_COUNT"))
                                totalAttrCnt = Integer.parseInt(value);
                            else
                            if(title.equals("TOTAL_ITEM_COUNT"))
                                totalAttrItemCnt = Integer.parseInt(value);
                            else
                            if(!title.startsWith("SIZE_NAME") && !title.startsWith("SCALE_NAME"))
                            {
                                attrItemList.add(value);
                                if(value.indexOf("POSITION") > -1)
                                    attrPositionItemList.add(value);
                            }
                            line = buff.readLine();
                        }

                    if(line == null)
                        break;
                    continue;
                }
                if(!line.equals("[ATTRIBUTE_INITIALIZATION]"))
                    continue;
                String attrItem = "";
                String attrNum = "";
                String oldAttrNum = "";
                for(line = buff.readLine(); line != null && !line.startsWith("[");)
                    if(line.equals(""))
                    {
                        line = buff.readLine();
                    } else
                    {
                        tokenList = Utils.tokenizeMessage(line, '=');
                        title = (String)tokenList.get(0);
                        if(tokenList.size() < 2 || ((String)tokenList.get(1)).equalsIgnoreCase("null"))
                            value = null;
                        else
                            value = (String)tokenList.get(1);
                        tokenList = Utils.tokenizeMessage(title, '#');
                        attrItem = (String)tokenList.get(0);
                        attrNum = (String)tokenList.get(1);
                        if(!attrNum.equals("1") && !attrNum.equals(oldAttrNum))
                        {
                            attrList.add(attrMap.clone());
                            attrMap.clear();
                        }
                        if(attrItemList.contains(attrItem))
                            attrMap.put(attrItem, value);
                        oldAttrNum = attrNum;
                        line = buff.readLine();
                    }

                attrList.add(attrMap.clone());
                attrMap.clear();
                if(line == null)
                    break;
            }
            buff.close();
            HashMap tmpMap = null;
            ArrayList attrPositionDataList = new ArrayList();
            String size = "";
            String scale = "";
            DOSChangeable fieldInfoDC = dos.getFieldWithName(cadFileClassOuid, "Size");
            String sizeCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
            fieldInfoDC = dos.getFieldWithName(cadFileClassOuid, "Scale");
            String scaleCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
            for(int i = 0; i < attrList.size(); i++)
            {
                tmpMap = (HashMap)attrList.get(i);
                for(int j = 0; j < attrPositionItemList.size(); j++)
                {
                    tokenList = Utils.tokenizeMessage((String)attrPositionItemList.get(j), '_');
                    size = (String)tokenList.get(0);
                    scale = (String)tokenList.get(1);
                    DOSChangeable sizeCodeItemDC = dos.getCodeItemWithName(sizeCodeOuid, size);
                    DOSChangeable scaleCodeItemDC = dos.getCodeItemWithName(scaleCodeOuid, scale);
                    DOSChangeable attrPositionData = new DOSChangeable();
                    attrPositionData.put("fieldouid", (String)tmpMap.get("FIELD_OUID"));
                    attrPositionData.put("drwsize", sizeCodeItemDC.get("ouid"));
                    attrPositionData.put("drwscale", scaleCodeItemDC.get("ouid"));
                    if(Utils.isNullString((String)tmpMap.get(attrPositionItemList.get(j))))
                        attrPositionData.put("leftx", null);
                    else
                        attrPositionData.put("leftx", Float.valueOf((String)tmpMap.get(attrPositionItemList.get(j))));
                    j++;
                    if(Utils.isNullString((String)tmpMap.get(attrPositionItemList.get(j))))
                        attrPositionData.put("lefty", null);
                    else
                        attrPositionData.put("lefty", Float.valueOf((String)tmpMap.get(attrPositionItemList.get(j))));
                    j++;
                    if(Utils.isNullString((String)tmpMap.get(attrPositionItemList.get(j))))
                        attrPositionData.put("rightx", null);
                    else
                        attrPositionData.put("rightx", Float.valueOf((String)tmpMap.get(attrPositionItemList.get(j))));
                    j++;
                    if(Utils.isNullString((String)tmpMap.get(attrPositionItemList.get(j))))
                        attrPositionData.put("righty", null);
                    else
                        attrPositionData.put("righty", Float.valueOf((String)tmpMap.get(attrPositionItemList.get(j))));
                    attrPositionDataList.add(attrPositionData);
                    attrPositionData = null;
                }

            }

            dos.saveCADAttributePosition(attrPositionDataList);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        catch(IOException ie)
        {
            System.err.println(ie);
        }
    }

    public void writeInitFile(int workType, String fileName, ArrayList fileInfoList)
    {
        try
        {
            DOSChangeable fieldInfoDC = dos.getFieldWithName(cadFileClassOuid, "Size");
            String sizeCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
            ArrayList sizeCodeItemList = dos.listCodeItem(sizeCodeOuid);
            fieldInfoDC = dos.getFieldWithName(cadFileClassOuid, "Scale");
            String scaleCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
            ArrayList scaleCodeItemList = dos.listCodeItem(scaleCodeOuid);
            ArrayList attributeItem = new ArrayList();
            attributeItem.add("FIELD_OUID");
            attributeItem.add("PDM_ATTRIBUTE_NAME");
            attributeItem.add("3D_ATTRIBUTE_NAME");
            attributeItem.add("2D_ATTRIBUTE_NAME");
            attributeItem.add("ATTRIBUTE_TYPE");
            attributeItem.add("ATTRIBUTE_TYPE_ID");
            attributeItem.add("ATTRIBUTE_TYPE_CLASS");
            attributeItem.add("PDM_ATTRIBUTE_VALUE");
            attributeItem.add("PDM_ATTRIBUTE_VALUE_ID");
            attributeItem.add("3D_ATTRIBUTE_VALUE");
            attributeItem.add("3D_ATTRIBUTE_VALUE_ID");
            attributeItem.add("2D_ATTRIBUTE_VALUE");
            attributeItem.add("2D_ATTRIBUTE_VALUE_ID");
            attributeItem.add("DEFINE_OPTION");
            attributeItem.add("USE_OPTION");
            attributeItem.add("DIRECTION_OPTION");
            String tmpSizeStr = "";
            String tmpScaleStr = "";
            for(int i = 0; i < sizeCodeItemList.size(); i++)
            {
                tmpSizeStr = (String)((DOSChangeable)sizeCodeItemList.get(i)).get("name");
                for(int j = 0; j < scaleCodeItemList.size(); j++)
                {
                    tmpScaleStr = tmpSizeStr + "_" + (String)((DOSChangeable)scaleCodeItemList.get(j)).get("name") + "_";
                    attributeItem.add(tmpScaleStr + "POSITION_LEFT_X");
                    attributeItem.add(tmpScaleStr + "POSITION_LEFT_Y");
                    attributeItem.add(tmpScaleStr + "POSITION_RIGHT_X");
                    attributeItem.add(tmpScaleStr + "POSITION_RIGHT_Y");
                }

            }

            ArrayList attributeList = dos.listCADAttribute(cadFileClassOuid);
            String filePath = "";
            if(cadType == 0)
                filePath = System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "env" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "solidworks" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + fileName;
            File file = new File(filePath);
            if(file.isFile())
                new Boolean(file.delete());
            FileWriter dataFile = new FileWriter(filePath, true);
            BufferedWriter buff = new BufferedWriter(dataFile);
            String str = "[ATTRIBUTE_INFORMATION]";
            buff.write(str, 0, str.length());
            buff.write(10);
            str = "TOTAL_ATTRIBUTE_COUNT=" + attributeList.size();
            buff.write(str, 0, str.length());
            buff.write(10);
            str = "TOTAL_ITEM_COUNT=" + attributeItem.size();
            buff.write(str, 0, str.length());
            buff.write(10);
            str = "TOTAL_DRAWING_SIZE_COUNT=" + sizeCodeItemList.size();
            buff.write(str, 0, str.length());
            buff.write(10);
            str = "TOTAL_DRAWING_SCALE_COUNT=" + scaleCodeItemList.size();
            buff.write(str, 0, str.length());
            buff.write(10);
            buff.write(10);
            for(int i = 0; i < sizeCodeItemList.size(); i++)
            {
                str = "SIZE_NAME" + (i + 1) + "=" + (String)((DOSChangeable)sizeCodeItemList.get(i)).get("name");
                buff.write(str, 0, str.length());
                buff.write(10);
            }

            buff.write(10);
            for(int i = 0; i < scaleCodeItemList.size(); i++)
            {
                str = "SCALE_NAME" + (i + 1) + "=" + (String)((DOSChangeable)scaleCodeItemList.get(i)).get("name");
                buff.write(str, 0, str.length());
                buff.write(10);
            }

            buff.write(10);
            for(int i = 0; i < attributeItem.size(); i++)
            {
                str = String.valueOf(i + 1) + "=" + (String)attributeItem.get(i);
                buff.write(str, 0, str.length());
                buff.write(10);
            }

            buff.write(10);
            str = "[ATTRIBUTE_INITIALIZATION]";
            buff.write(str, 0, str.length());
            buff.write(10);
            for(int i = 0; i < attributeList.size(); i++)
            {
                DOSChangeable dosAttribute = (DOSChangeable)attributeList.get(i);
                str = (String)attributeItem.get(0) + "#" + (i + 1) + "=" + (String)dosAttribute.get("fieldouid");
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(1) + "#" + (i + 1) + "=" + (String)dosAttribute.get("pdmname");
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(2) + "#" + (i + 1) + "=" + (String)dosAttribute.get("cad3dname");
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(3) + "#" + (i + 1) + "=" + (String)dosAttribute.get("cad2dname");
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(4) + "#" + (i + 1) + "=" + getTypeString((Byte)dosAttribute.get("type"));
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(5) + "#" + (i + 1) + "=" + ((Byte)dosAttribute.get("type")).intValue();
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(6) + "#" + (i + 1) + "=" + (String)dosAttribute.get("type.ouid@class");
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(7) + "#" + (i + 1) + "=NULL";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(8) + "#" + (i + 1) + "=NULL";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(9) + "#" + (i + 1) + "=NULL";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(10) + "#" + (i + 1) + "=NULL";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(11) + "#" + (i + 1) + "=NULL";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(12) + "#" + (i + 1) + "=NULL";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(13) + "#" + (i + 1) + "=" + ((Integer)dosAttribute.get("defineoption")).intValue();
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(14) + "#" + (i + 1) + "=" + ((Integer)dosAttribute.get("useoption")).intValue();
                buff.write(str, 0, str.length());
                buff.write(10);
                str = (String)attributeItem.get(15) + "#" + (i + 1) + "=" + ((Integer)dosAttribute.get("updateoption")).intValue();
                buff.write(str, 0, str.length());
                buff.write(10);
                if(workType == 0)
                {
                    for(int j = 16; j < attributeItem.size(); j++)
                    {
                        str = (String)attributeItem.get(j) + "#" + (i + 1) + "=NULL";
                        buff.write(str, 0, str.length());
                        buff.write(10);
                    }

                }
                if(workType == 1 || workType == 2)
                {
                    ArrayList listPositionData = dos.listCADAttributePosition((String)dosAttribute.get("fieldouid"));
                    for(int j = 16; j < attributeItem.size(); j++)
                    {
                        int k = 0;
                        for(k = 0; k < listPositionData.size(); k++)
                        {
                            DOSChangeable positionData = (DOSChangeable)listPositionData.get(k);
                            String tmpStr = (String)positionData.get("sizename") + "_" + (String)positionData.get("scalename") + "_";
                            if(!((String)attributeItem.get(j)).startsWith(tmpStr))
                                continue;
                            str = (String)attributeItem.get(j) + "#" + (i + 1) + "=" + (Float)positionData.get("leftx");
                            buff.write(str, 0, str.length());
                            buff.write(10);
                            j++;
                            str = (String)attributeItem.get(j) + "#" + (i + 1) + "=" + (Float)positionData.get("lefty");
                            buff.write(str, 0, str.length());
                            buff.write(10);
                            j++;
                            str = (String)attributeItem.get(j) + "#" + (i + 1) + "=" + (Float)positionData.get("rightx");
                            buff.write(str, 0, str.length());
                            buff.write(10);
                            j++;
                            str = (String)attributeItem.get(j) + "#" + (i + 1) + "=" + (Float)positionData.get("righty");
                            buff.write(str, 0, str.length());
                            buff.write(10);
                            break;
                        }

                        if(k == listPositionData.size())
                        {
                            str = (String)attributeItem.get(j) + "#" + (i + 1) + "=NULL";
                            buff.write(str, 0, str.length());
                            buff.write(10);
                        }
                    }

                }
                buff.write(10);
            }

            if(workType == 2)
            {
                HashMap fileInfoMap = null;
                File downLoadFile = null;
                ArrayList fileItemList = new ArrayList();
                fileItemList.add("GLOBAL_FULL_PATH");
                fileItemList.add("GLOBAL_FILE_TYPE");
                fileItemList.add("GLOBAL_CAD_TYPE");
                fileItemList.add("GLOBAL_PAIR_COUNT");
                fileItemList.add("GLOBAL_PARENT");
                fileItemList.add("GLOBAL_OCCURRENCE");
                fileItemList.add("GLOBAL_SIZE");
                fileItemList.add("GLOBAL_SCALE");
                fileItemList.add("GLOBAL_INSTANCE_OUID");
                fileItemList.add("GLOBAL_CHECKOU_TTIME");
                fileItemList.add("GLOBAL_MODIFIED_TIME");
                fileItemList.add("GLOBAL_FILE_ATTRIBUTE");
                fileItemList.add("GLOBAL_INIT_FILE_NAME");
                fileItemList.add("GLOBAL_3D_LINK");
                fileItemList.add("GLOBAL_2D_LINK");
                str = "[FILE_INFORMATION]";
                buff.write(str, 0, str.length());
                buff.write(10);
                str = "TOTAL_FILE_COUNT=" + fileInfoList.size();
                buff.write(str, 0, str.length());
                buff.write(10);
                for(int i = 0; i < fileInfoList.size(); i++)
                {
                    fileInfoMap = (HashMap)fileInfoList.get(i);
                    downLoadFile = new File((String)fileInfoMap.get("md$description"));
                    str = "FILE_NAME#" + (i + 1) + "=" + downLoadFile.getName();
                    buff.write(str, 0, str.length());
                    buff.write(10);
                }

                buff.write(10);
                str = "TOTAL_FILE_ITEM_COUNT=" + fileItemList.size();
                buff.write(str, 0, str.length());
                buff.write(10);
                for(int i = 0; i < fileItemList.size(); i++)
                {
                    str = "FILE_ITEM#" + (i + 1) + "=" + fileItemList.get(i);
                    buff.write(str, 0, str.length());
                    buff.write(10);
                }

                buff.write(10);
                String millisec = (new Long(System.currentTimeMillis())).toString().substring(0, 10);
                for(int i = 0; i < fileInfoList.size(); i++)
                {
                    fileInfoMap = (HashMap)fileInfoList.get(i);
                    downLoadFile = new File((String)fileInfoMap.get("md$description"));
                    str = "[" + downLoadFile.getName() + "]";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(0) + "=" + (String)fileInfoMap.get("md$description");
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(1) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(2) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(3) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(4) + "#1=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(5) + "#1=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    DOSChangeable instanceData = dos.get((String)fileInfoMap.get("md$ouid"));
                    if(Utils.isNullString((String)instanceData.get("Size")))
                    {
                        str = (String)fileItemList.get(6) + "=NULL";
                    } else
                    {
                        List tokenList = Utils.tokenizeMessage((String)instanceData.get("name@Size"), '[');
                        str = (String)fileItemList.get(6) + "=" + ((String)tokenList.get(0)).trim();
                    }
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    if(Utils.isNullString((String)instanceData.get("Scale")))
                    {
                        str = (String)fileItemList.get(7) + "=NULL";
                    } else
                    {
                        List tokenList = Utils.tokenizeMessage((String)instanceData.get("name@Scale"), '[');
                        str = (String)fileItemList.get(7) + "=" + ((String)tokenList.get(0)).trim();
                    }
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(8) + "=" + (String)fileInfoMap.get("md$ouid");
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(9) + "=" + millisec;
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(10) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(11) + "=WRITE";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(12) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(13) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    str = (String)fileItemList.get(14) + "=NULL";
                    buff.write(str, 0, str.length());
                    buff.write(10);
                    buff.write(10);
                    for(int j = 0; j < attributeList.size(); j++)
                    {
                        DOSChangeable dosAttribute = (DOSChangeable)attributeList.get(j);
                        str = (String)attributeItem.get(0) + "#" + (j + 1) + "=" + (String)dosAttribute.get("fieldouid");
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        DOSChangeable fieldData = dos.getField((String)dosAttribute.get("fieldouid"));
                        instanceData = dos.get((String)fileInfoMap.get("md$ouid"));
                        str = (String)attributeItem.get(7) + "#" + (j + 1) + "=" + instanceData.get((String)fieldData.get("name"));
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        str = (String)attributeItem.get(8) + "#" + (j + 1) + "=NULL";
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        if(((String)fileInfoMap.get("md$filetype.id")).equals("sw-assembly") || ((String)fileInfoMap.get("md$filetype.id")).equals("sw-part"))
                            str = (String)attributeItem.get(9) + "#" + (j + 1) + "=" + instanceData.get((String)fieldData.get("name"));
                        else
                            str = (String)attributeItem.get(9) + "#" + (j + 1) + "=NULL";
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        str = (String)attributeItem.get(10) + "#" + (j + 1) + "=NULL";
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        if(((String)fileInfoMap.get("md$filetype.id")).equals("sw-drawing"))
                            str = (String)attributeItem.get(11) + "#" + (j + 1) + "=" + instanceData.get((String)fieldData.get("name"));
                        else
                            str = (String)attributeItem.get(11) + "#" + (j + 1) + "=NULL";
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        str = (String)attributeItem.get(12) + "#" + (j + 1) + "=NULL";
                        buff.write(str, 0, str.length());
                        buff.write(10);
                        buff.write(10);
                    }

                }

                buff.write(10);
            }
            buff.close();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        catch(IOException ie)
        {
            System.err.println(ie);
        }
    }

    private String getTypeString(Byte type)
    {
        switch(type.byteValue())
        {
        case 1: // '\001'
            return "boolean";

        case 2: // '\002'
            return "byte";

        case 3: // '\003'
            return "char";

        case 4: // '\004'
            return "double";

        case 5: // '\005'
            return "float";

        case 6: // '\006'
            return "int";

        case 7: // '\007'
            return "long";

        case 8: // '\b'
            return "short";

        case 13: // '\r'
            return "string";

        case 16: // '\020'
            return "object";

        case 21: // '\025'
            return "datetime";

        case 22: // '\026'
            return "date";

        case 23: // '\027'
            return "time";

        case 24: // '\030'
            return "code";

        case 25: // '\031'
            return "code (Field Referenced)";

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
        default:
            return "";
        }
    }

    private DOS dos;
    public int cadType;
    public int totalAttrCnt;
    public int totalAttrItemCnt;
    public ArrayList attrItemList;
    public ArrayList attrPositionItemList;
    public ArrayList attrList;
    public HashMap attrMap;
    public int totalFileCnt;
    public int totalFileItemCnt;
    public ArrayList fileNameList;
    public ArrayList fileItemList;
    public ArrayList fileList;
    public String cadPackageOuid;
    public String cadFileClassOuid;
    public String cadPdtClassOuid;
    public String cadPrtClassOuid;
    public String cadDrwClassOuid;
    public String cadPSClassOuid;
    public ArrayList classFieldList;
    public ArrayList attributeList;
    public ArrayList classFieldGroupList;
}
