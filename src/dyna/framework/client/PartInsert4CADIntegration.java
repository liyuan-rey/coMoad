// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PartInsert4CADIntegration.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.*;
import dyna.uic.table.DynaTableCellColor;
import dyna.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, LogIn, FileTransferDialog, UIManagement

public class PartInsert4CADIntegration extends JFrame
    implements DynaTableCellColor
{
    class SizeDataLoader extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            return setSizeDataDefine();
        }

        SizeDataLoader()
        {
        }
    }

    class ScaleDataLoader extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            return setScaleDataDefine();
        }

        ScaleDataLoader()
        {
        }
    }

    class ListenerCommon extends WindowAdapter
        implements ActionListener, MouseListener
    {

        public void windowClosing(WindowEvent e)
        {
            exitForm(e);
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
        }

        public void mouseEntered(MouseEvent mouseevent)
        {
        }

        public void mouseExited(MouseEvent mouseevent)
        {
        }

        public void mousePressed(MouseEvent mouseevent)
        {
        }

        public void mouseReleased(MouseEvent mouseevent)
        {
        }

        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if(source.equals(partInsertButton))
                partInsertButtonActionPerformed(e);
            else
            if(source.equals(partCloseButton))
                closeButtonActionPerformed(e);
            else
            if(source.equals(sizeComboBox))
                sizeComboBox_actionPerformed(e);
            else
            if(source.equals(scaleComboBox))
                scaleComboBox_actionPerformed(e);
        }

        ListenerCommon()
        {
        }
    }

    class FileCallBack
        implements FileTransferCallback
    {

        public void transferBytes(int bytes)
        {
            ftd.addSize(bytes);
        }

        FileCallBack()
        {
        }
    }


    public PartInsert4CADIntegration(Frame parent, Session session, String fileName, int intCadKind)
    {
        wfm = null;
        handCursor = new Cursor(12);
        listenerCommon = null;
        sizeComboBox = null;
        sizeModel = null;
        scaleComboBox = null;
        scaleModel = null;
        isRegisted = false;
        color = null;
        evenRowColor = null;
        addColor = null;
        notColor = null;
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
        fileMap = new HashMap();
        dataList = null;
        fieldNameList = null;
        columnName = null;
        columnWidth = null;
        this.fileName = null;
        classFieldList = null;
        cadKindInt = -1;
        _serverPath = null;
        _clientPath = null;
        _uploadFileList = null;
        _ftc = null;
        ftd = null;
        _session = null;
        utils = null;
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        dss = DynaMOAD.dss;
        wfm = DynaMOAD.wfm;
        acl = DynaMOAD.acl;
        newUI = DynaMOAD.newUI;
        this.fileName = fileName;
        cadKindInt = intCadKind;
        utils = new CADIntegrationUtils(cadKindInt, dos);
        classFieldList = utils.classFieldList;
        initLoad();
        if(session != null)
            _session = session;
        _session.setProperty("dynapdm.part.insert.status", "open");
    }

    private void initLoad()
    {
        try
        {
            blPartInsertForCad = new BorderLayout();
            partTabbedPane = new JTabbedPane();
            partPanel = new JPanel();
            partBorderLayout = new BorderLayout();
            partButtonPanel = new JPanel();
            partTableScrPane = UIFactory.createStrippedScrollPane(null);
            partInsertButton = new JButton();
            partCloseButton = new JButton();
            partTable = new Table();
            blPartButton = new BoxLayout(partButtonPanel, 0);
            listenerCommon = new ListenerCommon();
            setTitle("Parts Insert for CAD Integration - coMoad");
            setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
            addWindowListener(listenerCommon);
            setSize(800, 600);
            setLocation(50, 50);
            getContentPane().setLayout(blPartInsertForCad);
            partPanel.setLayout(partBorderLayout);
            initComponents();
            makeTable();
            setupColumn();
            readInitFile();
            if(cadKindInt == 0)
                setDataSolidworks();
            else
            if(cadKindInt == 1 || cadKindInt == 2);
        }
        catch(Exception e)
        {
            System.out.println("Main : " + e);
        }
    }

    private void initComponents()
    {
        JLabel labInformation = new JLabel("Select Part Division, Part Category I, Part Category II, Drawing Division");
        partInsertButton.setAlignmentY(0.5F);
        partInsertButton.setToolTipText("Register");
        partInsertButton.setActionCommand("Register");
        partInsertButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok32.gif")));
        partInsertButton.setMargin(new Insets(0, 0, 0, 0));
        partInsertButton.setEnabled(true);
        partInsertButton.addActionListener(listenerCommon);
        partCloseButton.setAlignmentY(0.5F);
        partCloseButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        partCloseButton.setActionCommand("Close");
        partCloseButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit32.gif")));
        partCloseButton.setMargin(new Insets(0, 0, 0, 0));
        partCloseButton.addActionListener(listenerCommon);
        partButtonPanel.setLayout(blPartButton);
        partTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        getContentPane().add(partTabbedPane, "Center");
        partTabbedPane.add(partPanel, "Part register");
        partPanel.add(partButtonPanel, "North");
        partButtonPanel.add(Box.createHorizontalGlue());
        partButtonPanel.add(labInformation);
        partButtonPanel.add(Box.createHorizontalGlue());
        partButtonPanel.add(partInsertButton);
        partButtonPanel.add(Box.createHorizontalStrut(10));
        partButtonPanel.add(partCloseButton);
        partPanel.add(partTableScrPane, "Center");
    }

    public void makeTable()
    {
        dataList = new ArrayList();
        fieldNameList = new ArrayList();
        columnName = new ArrayList();
        columnWidth = new ArrayList();
        ArrayList fieldList = makeAllField();
        if(fieldList != null)
        {
            for(int i = 0; i < fieldList.size(); i++)
            {
                fieldNameList.add((String)fieldList.get(i));
                if(i != 2 && i != 5 && i != 9 && i != 12 && i != 13 && i != 14 && i != 16 && i != 18 && i != 19 && i != 20 && i != 21)
                {
                    columnName.add((String)fieldList.get(i));
                    columnWidth.add(new Integer(80));
                }
            }

            partTable = new Table(dataList, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0);
            color = partTable.getTable().getBackground();
            evenRowColor = new Color(color.getRed() - 32, color.getGreen(), color.getBlue() - 32);
            addColor = new Color(color.getRed(), color.getGreen() - 32, color.getBlue() - 32);
            notColor = new Color(color.getRed() - 32, color.getGreen() - 32, color.getBlue() - 32);
            ((DynaTable)partTable.getTable()).setCellColor(this);
            int columnCnt = fieldList.size() - 11;
            int columns[] = new int[columnCnt];
            if(cadKindInt == 0)
            {
                columns[0] = 0;
                columns[1] = 1;
                columns[2] = 3;
                columns[3] = 4;
                columns[4] = 6;
                columns[5] = 7;
                columns[6] = 8;
                columns[7] = 10;
                columns[8] = 11;
                columns[9] = 15;
                columns[10] = 17;
                for(int i = 22; i < fieldList.size(); i++)
                    columns[i - 11] = i;

                partTable.setColumnSequence(columns);
            } else
            if(cadKindInt != 1)
                if(cadKindInt == 2);
            partTable.setIndexColumn(0);
            partTable.getTable().setCursor(handCursor);
            partTable.getTable().addMouseListener(listenerCommon);
            partTable.setEditable(true);
            partTable.getTable().setPreferredScrollableViewportSize(new Dimension(800, 500));
            partTableScrPane.setViewportView(partTable.getTable());
        }
    }

    public ArrayList makeAllField()
    {
        ArrayList fieldList = new ArrayList();
        String tmpStr = "";
        tmpStr = "Check";
        fieldList.add(tmpStr);
        tmpStr = "Pair Number";
        fieldList.add(tmpStr);
        tmpStr = "Parent Ouid";
        fieldList.add(tmpStr);
        tmpStr = "Parent Number";
        fieldList.add(tmpStr);
        tmpStr = "Parent Name";
        fieldList.add(tmpStr);
        tmpStr = "Child Ouid";
        fieldList.add(tmpStr);
        tmpStr = "Child Number";
        fieldList.add(tmpStr);
        tmpStr = "Child Name";
        fieldList.add(tmpStr);
        tmpStr = "Quantity";
        fieldList.add(tmpStr);
        tmpStr = "Class Ouid";
        fieldList.add(tmpStr);
        tmpStr = "CAD";
        fieldList.add(tmpStr);
        tmpStr = "Full Path";
        fieldList.add(tmpStr);
        tmpStr = "CheckOut Time";
        fieldList.add(tmpStr);
        tmpStr = "Modified Time";
        fieldList.add(tmpStr);
        tmpStr = "User ID";
        fieldList.add(tmpStr);
        tmpStr = "Size";
        fieldList.add(tmpStr);
        tmpStr = "Size ID";
        fieldList.add(tmpStr);
        tmpStr = "Scale";
        fieldList.add(tmpStr);
        tmpStr = "Scale ID";
        fieldList.add(tmpStr);
        tmpStr = "File Attribute";
        fieldList.add(tmpStr);
        tmpStr = "File Server Path";
        fieldList.add(tmpStr);
        tmpStr = "File Version";
        fieldList.add(tmpStr);
        for(int i = 0; i < classFieldList.size(); i++)
        {
            DOSChangeable dosField = (DOSChangeable)classFieldList.get(i);
            if(!((String)dosField.get("name")).equals("name") && !((String)dosField.get("name")).equals("Size") && !((String)dosField.get("name")).equals("Scale"))
                fieldList.add(DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0));
        }

        return fieldList;
    }

    public void setupColumn()
    {
        TableColumn sizeColumn = partTable.getTable().getColumnModel().getColumn(9);
        SizeDataLoader sizeDataLoader = new SizeDataLoader();
        sizeModel = new DynaComboBoxModel(sizeDataLoader);
        sizeComboBox = new JComboBox();
        sizeComboBox.setModel(sizeModel);
        sizeComboBox.addActionListener(listenerCommon);
        sizeModel.enableDataLoad();
        sizeColumn.setCellEditor(new DefaultCellEditor(sizeComboBox));
        TableColumn scaleColumn = partTable.getTable().getColumnModel().getColumn(10);
        ScaleDataLoader scaleDataLoader = new ScaleDataLoader();
        scaleModel = new DynaComboBoxModel(scaleDataLoader);
        scaleComboBox = new JComboBox();
        scaleComboBox.setModel(scaleModel);
        scaleComboBox.addActionListener(listenerCommon);
        scaleModel.enableDataLoad();
        scaleColumn.setCellEditor(new DefaultCellEditor(scaleComboBox));
    }

    public ArrayList setSizeDataDefine()
    {
        try
        {
            DOSChangeable fieldInfoDC = dos.getFieldWithName(utils.cadFileClassOuid, "Size");
            String sizeCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
            ArrayList sizeCodeItemList = dos.listCodeItem(sizeCodeOuid);
            ArrayList LocSize = new ArrayList();
            ArrayList cbSize = new ArrayList();
            for(int i = 0; i < sizeCodeItemList.size(); i++)
            {
                DOSChangeable sizeData = (DOSChangeable)sizeCodeItemList.get(i);
                LocSize.add(sizeData.get("ouid"));
                LocSize.add(sizeData.get("name") + " [" + sizeData.get("codeitemid") + "]");
                cbSize.add(LocSize.clone());
                LocSize.clear();
            }

            return cbSize;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return null;
    }

    public ArrayList setScaleDataDefine()
    {
        try
        {
            DOSChangeable fieldInfoDC = dos.getFieldWithName(utils.cadFileClassOuid, "Scale");
            String scaleCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
            ArrayList scaleCodeItemList = dos.listCodeItem(scaleCodeOuid);
            ArrayList LocScale = new ArrayList();
            ArrayList cbScale = new ArrayList();
            for(int i = 0; i < scaleCodeItemList.size(); i++)
            {
                DOSChangeable scaleData = (DOSChangeable)scaleCodeItemList.get(i);
                LocScale.add(scaleData.get("ouid"));
                LocScale.add(scaleData.get("name") + " [" + scaleData.get("codeitemid") + "]");
                cbScale.add(LocScale.clone());
                LocScale.clear();
            }

            return cbScale;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return null;
    }

    void readInitFile()
    {
        utils.readInitFile4CheckIn(fileName);
        totalAttrCnt = utils.totalAttrCnt;
        totalAttrItemCnt = utils.totalAttrItemCnt;
        attrItemList = utils.attrItemList;
        attrPositionItemList = utils.attrPositionItemList;
        attrList = utils.attrList;
        attrMap = utils.attrMap;
        totalFileCnt = utils.totalFileCnt;
        totalFileItemCnt = utils.totalFileItemCnt;
        fileNameList = utils.fileNameList;
        fileItemList = utils.fileItemList;
        fileList = utils.fileList;
    }

    public void setDataSolidworks()
    {
        ArrayList Loc = null;
        String classOuid = null;
        if(dataList != null && dataList.size() > 0)
            dataList.clear();
        try
        {
            ArrayList columnList = new ArrayList();
            for(int i = 0; i < this.fileList.size(); i++)
            {
                DOSChangeable instanceData = null;
                String instanceOuid = "";
                String instanceNumber = "";
                String fileServerPath = "";
                String fileVersion = "";
                HashMap tmpMap = (HashMap)this.fileList.get(i);
                if(((String)tmpMap.get("GLOBAL_CAD_TYPE")).equals("sw-assembly"))
                    classOuid = utils.cadPdtClassOuid;
                else
                if(((String)tmpMap.get("GLOBAL_CAD_TYPE")).equals("sw-part"))
                    classOuid = utils.cadPrtClassOuid;
                else
                if(((String)tmpMap.get("GLOBAL_CAD_TYPE")).equals("sw-drawing"))
                    classOuid = utils.cadDrwClassOuid;
                DOSChangeable classInfo = dos.getClass(classOuid);
                ArrayList fieldOuidList = dos.listFieldInClass(classOuid);
                if(Utils.isNullString((String)tmpMap.get("GLOBAL_INSTANCE_OUID")))
                {
                    ArrayList fieldListForSearch = new ArrayList();
                    HashMap searchCondition = new HashMap();
                    DOSChangeable tmpDOS = new DOSChangeable();
                    int j = 0;
                    for(j = 0; j < fieldOuidList.size(); j++)
                    {
                        DOSChangeable fieldData = (DOSChangeable)fieldOuidList.get(j);
                        if(!((String)fieldData.get("ouid")).equals("version.condition.type"))
                            fieldListForSearch.add((String)fieldData.get("ouid"));
                    }

                    DOSChangeable fieldInfo = dos.getFieldWithName(classOuid, "name");
                    searchCondition.put((String)fieldInfo.get("ouid"), (String)tmpMap.get("FILE_NAME"));
                    searchCondition.put("version.condition.type", "wip");
                    searchCondition.put("search.result.count", "-1");
                    ArrayList instanceList = dos.list(classOuid, fieldListForSearch, searchCondition);
                    if(instanceList != null && instanceList.size() > 0)
                    {
                        instanceOuid = (String)((ArrayList)instanceList.get(0)).get(0);
                        instanceData = dos.get(instanceOuid);
                        instanceNumber = (String)((ArrayList)instanceList.get(0)).get(1);
                        ArrayList fileList = dos.listFile(instanceOuid);
                        fileServerPath = (String)((HashMap)fileList.get(0)).get("md$path");
                        fileVersion = (String)((HashMap)fileList.get(0)).get("md$version");
                    }
                } else
                {
                    instanceOuid = (String)tmpMap.get("GLOBAL_INSTANCE_OUID");
                    if(!Utils.isNullString(instanceOuid))
                    {
                        instanceData = dos.get(instanceOuid);
                        instanceNumber = (String)instanceData.get("md$number");
                        ArrayList fileList = dos.listFile(instanceOuid);
                        fileServerPath = (String)((HashMap)fileList.get(0)).get("md$path");
                        fileVersion = (String)((HashMap)fileList.get(0)).get("md$version");
                    } else
                    {
                        instanceData = null;
                        instanceOuid = null;
                        instanceNumber = null;
                        fileServerPath = null;
                        fileVersion = null;
                    }
                }
                for(int j = 0; j < Integer.parseInt((String)tmpMap.get("GLOBAL_PAIR_COUNT")); j++)
                {
                    columnList.add(new Boolean(true));
                    columnList.add(String.valueOf(j + 1));
                    columnList.add(null);
                    columnList.add(null);
                    columnList.add((String)tmpMap.get("GLOBAL_PARENT#" + (j + 1)));
                    columnList.add(instanceOuid);
                    columnList.add(instanceNumber);
                    columnList.add((String)tmpMap.get("FILE_NAME"));
                    columnList.add((String)tmpMap.get("GLOBAL_OCCURRENCE#" + (j + 1)));
                    columnList.add(classOuid);
                    columnList.add((String)tmpMap.get("GLOBAL_CAD_TYPE"));
                    columnList.add((String)tmpMap.get("GLOBAL_FULL_PATH"));
                    columnList.add((String)tmpMap.get("GLOBAL_CHECKOUT_TIME"));
                    columnList.add((String)tmpMap.get("GLOBAL_MODIFIED_TIME"));
                    columnList.add(LogIn.userID);
                    DOSChangeable fieldInfoDC = dos.getFieldWithName(utils.cadFileClassOuid, "Size");
                    String sizeCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
                    ArrayList sizeCodeItemList = dos.listCodeItem(sizeCodeOuid);
                    int k = 0;
                    for(k = 0; k < sizeCodeItemList.size(); k++)
                    {
                        DOSChangeable codeItemDC = (DOSChangeable)sizeCodeItemList.get(k);
                        if(!((String)codeItemDC.get("name")).equals((String)tmpMap.get("GLOBAL_SIZE")))
                            continue;
                        columnList.add((String)codeItemDC.get("name") + " [" + (String)codeItemDC.get("codeitemid") + "]");
                        columnList.add((String)codeItemDC.get("ouid"));
                        break;
                    }

                    if(k == sizeCodeItemList.size())
                    {
                        columnList.add(null);
                        columnList.add(null);
                    }
                    fieldInfoDC = dos.getFieldWithName(utils.cadFileClassOuid, "Scale");
                    String scaleCodeOuid = (String)fieldInfoDC.get("type.ouid@class");
                    ArrayList scaleCodeItemList = dos.listCodeItem(scaleCodeOuid);
                    for(k = 0; k < scaleCodeItemList.size(); k++)
                    {
                        DOSChangeable codeItemDC = (DOSChangeable)scaleCodeItemList.get(k);
                        if(!((String)codeItemDC.get("name")).equals((String)tmpMap.get("GLOBAL_SCALE")))
                            continue;
                        columnList.add((String)codeItemDC.get("name") + " [" + (String)codeItemDC.get("codeitemid") + "]");
                        columnList.add((String)codeItemDC.get("ouid"));
                        break;
                    }

                    if(k == scaleCodeItemList.size())
                    {
                        columnList.add(null);
                        columnList.add(null);
                    }
                    columnList.add((String)tmpMap.get("GLOBAL_FILE_ATTRIBUTE"));
                    columnList.add(fileServerPath);
                    columnList.add(fileVersion);
                    DOSChangeable fieldData = null;
                    String fieldTitle = null;
                    String fieldName = null;
                    for(k = 22; k < fieldNameList.size(); k++)
                        if(instanceData == null)
                        {
                            columnList.add(null);
                        } else
                        {
                            int m;
                            for(m = 0; m < fieldOuidList.size(); m++)
                            {
                                fieldData = (DOSChangeable)fieldOuidList.get(m);
                                fieldTitle = DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0);
                                fieldName = (String)fieldData.get("name");
                                if(!fieldTitle.equals((String)fieldNameList.get(k)))
                                    continue;
                                columnList.add(instanceData.get(fieldName));
                                break;
                            }

                            if(m == fieldOuidList.size())
                                columnList.add(null);
                        }

                    String fieldOuid = null;
                    String fieldValue = null;
                    for(k = 0; k < totalAttrCnt; k++)
                    {
                        fieldOuid = (String)tmpMap.get("FIELD_OUID#" + (k + 1));
                        if(((String)tmpMap.get("GLOBAL_CAD_TYPE")).equals("sw-assembly") || ((String)tmpMap.get("GLOBAL_CAD_TYPE")).equals("sw-part"))
                            fieldValue = (String)tmpMap.get("3D_ATTRIBUTE_VALUE#" + (k + 1));
                        else
                        if(((String)tmpMap.get("GLOBAL_CAD_TYPE")).equals("sw-drawing"))
                            fieldValue = (String)tmpMap.get("2D_ATTRIBUTE_VALUE#" + (k + 1));
                        DOSChangeable dosField = dos.getField(fieldOuid);
                        fieldTitle = DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0);
                        columnList.set(fieldNameList.indexOf(fieldTitle), fieldValue);
                    }

                }

                dataList.add(columnList.clone());
                columnList.clear();
            }

            partTable.changeTableData();
            for(int n = 0; n < dataList.size(); n++)
                if(!Utils.isNullString((String)((ArrayList)dataList.get(n)).get(4)))
                {
                    for(int m = 0; m < dataList.size(); m++)
                        if(n != m && ((String)((ArrayList)dataList.get(n)).get(4)).equals((String)((ArrayList)dataList.get(m)).get(7)))
                        {
                            ((ArrayList)dataList.get(n)).set(2, (String)((ArrayList)dataList.get(m)).get(5));
                            ((ArrayList)dataList.get(n)).set(3, (String)((ArrayList)dataList.get(m)).get(6));
                        }

                }

            partTable.changeTableData();
        }
        catch(Exception e)
        {
            System.out.println("ERROR : " + e);
        }
    }

    private void partInsertButtonActionPerformed(ActionEvent evt)
    {
        switch(cadKindInt)
        {
        case 0: // '\0'
            functionSolidWoks();
            // fall through

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        default:
            return;
        }
    }

    private void functionSolidWoks()
    {
        try
        {
            ArrayList uploadFileList = new ArrayList();
            ArrayList tmpList = null;
            if(fileName.equalsIgnoreCase("modelinsert.ini") || workType == 2)
            {
                String curPartName = "";
                String curFilePath = "";
                String nextPartName = "";
                String nextFilePath = "";
                int maximumSize = 0;
                for(int i = 0; i < dataList.size(); i++)
                {
                    tmpList = (ArrayList)dataList.get(i);
                    if(Utils.isNullString((String)tmpList.get(5)))
                    {
                        DOSChangeable addData = new DOSChangeable();
                        addData.setClassOuid((String)tmpList.get(9));
                        addData.put("name", (String)tmpList.get(7));
                        addData.put("Size", (String)tmpList.get(16));
                        addData.put("Scale", (String)tmpList.get(18));
                        for(int j = 0; j < classFieldList.size(); j++)
                        {
                            DOSChangeable dosField = (DOSChangeable)classFieldList.get(j);
                            String titleValue = "";
                            if(!((String)dosField.get("name")).equals("name") && !((String)dosField.get("name")).equals("Size") && !((String)dosField.get("name")).equals("Scale"))
                            {
                                titleValue = DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0);
                                for(int k = 22; k < fieldNameList.size(); k++)
                                {
                                    if(!titleValue.equals(fieldNameList.get(k)))
                                        continue;
                                    addData.put((String)dosField.get("name"), tmpList.get(k));
                                    break;
                                }

                            }
                        }

                        String instanceOuid = dos.add(addData);
                        tmpList.set(5, instanceOuid);
                        for(int j = 0; j < dataList.size(); j++)
                        {
                            if(i != j && ((String)tmpList.get(7)).equals((String)((ArrayList)dataList.get(j)).get(7)))
                                ((ArrayList)dataList.get(j)).set(5, instanceOuid);
                            if(((String)tmpList.get(7)).equals((String)((ArrayList)dataList.get(j)).get(4)))
                                ((ArrayList)dataList.get(j)).set(2, instanceOuid);
                        }

                        File file = new File((String)tmpList.get(11));
                        String filePath = file.getAbsolutePath();
                        if(file == null || !file.isFile())
                            return;
                        maximumSize += (new Long(file.length())).intValue();
                        HashMap fileInfo = new HashMap();
                        fileInfo.put("md$filetype.id", (String)tmpList.get(10));
                        fileInfo.put("md$des", filePath);
                        String serverPath = dos.attachFile(instanceOuid, fileInfo);
                        tmpList.set(20, serverPath);
                        fileInfo = null;
                        fileInfo = new HashMap();
                        fileInfo.put("md$ouid", (String)tmpList.get(5));
                        fileInfo.put("md$path", (String)tmpList.get(20));
                        fileInfo.put("md$description", (String)tmpList.get(11));
                        fileInfo.put("md$filetype.id", (String)tmpList.get(10));
                        uploadFileList.add(fileInfo);
                    } else
                    if(((String)tmpList.get(19)).equalsIgnoreCase("WRITE") || ((String)tmpList.get(19)).equalsIgnoreCase("NEW"))
                    {
                        DOSChangeable setData = dos.get((String)tmpList.get(5));
                        setData.put("name", (String)tmpList.get(7));
                        setData.put("Size", (String)tmpList.get(16));
                        setData.put("Scale", (String)tmpList.get(18));
                        for(int j = 0; j < classFieldList.size(); j++)
                        {
                            DOSChangeable dosField = (DOSChangeable)classFieldList.get(j);
                            String titleValue = "";
                            if(!((String)dosField.get("name")).equals("name") && !((String)dosField.get("name")).equals("Size") && !((String)dosField.get("name")).equals("Scale"))
                            {
                                titleValue = DynaMOAD.getMSRString((String)dosField.get("code"), (String)dosField.get("title"), 0);
                                for(int k = 22; k < fieldNameList.size(); k++)
                                {
                                    if(!titleValue.equals(fieldNameList.get(k)))
                                        continue;
                                    setData.put((String)dosField.get("name"), tmpList.get(k));
                                    break;
                                }

                            }
                        }

                        ArrayList fileList = dos.listFile((String)tmpList.get(5));
                        HashMap fileData = (HashMap)fileList.get(0);
                        if(!Utils.isNullString((String)fileData.get("md$checkedout.date")))
                        {
                            if(setData.isChanged())
                                dos.set(setData);
                            File file = new File((String)tmpList.get(11));
                            String filePath = file.getAbsolutePath();
                            if(file == null || !file.isFile())
                                return;
                            maximumSize += (new Long(file.length())).intValue();
                            HashMap fileInfo = new HashMap();
                            fileInfo.put("md$ouid", (String)tmpList.get(5));
                            fileInfo.put("md$path", (String)tmpList.get(20));
                            fileInfo.put("md$description", (String)tmpList.get(11));
                            fileInfo.put("md$version", (String)tmpList.get(21));
                            fileInfo.put("md$filetype.id", (String)tmpList.get(10));
                            fileInfo.put("do.versionup", Boolean.TRUE);
                            uploadFileList.add(fileInfo);
                        }
                    }
                    int nextCount = i + 1;
                    curPartName = (String)tmpList.get(7);
                    curFilePath = (String)tmpList.get(11);
                    if(nextCount < dataList.size())
                    {
                        nextPartName = (String)((ArrayList)dataList.get(nextCount)).get(7);
                        nextFilePath = (String)((ArrayList)dataList.get(nextCount)).get(11);
                    } else
                    {
                        nextPartName = "";
                        nextFilePath = "";
                    }
                    while(nextCount < dataList.size() && nextPartName.equals(curPartName) && nextFilePath.equals(curFilePath)) 
                    {
                        nextCount++;
                        curPartName = nextPartName;
                        curFilePath = nextFilePath;
                        if(nextCount < dataList.size())
                        {
                            nextPartName = (String)((ArrayList)dataList.get(nextCount)).get(7);
                            nextFilePath = (String)((ArrayList)dataList.get(nextCount)).get(11);
                        } else
                        {
                            nextPartName = "";
                            nextFilePath = "";
                        }
                    }
                    i = nextCount - 1;
                }

                if(ftd == null)
                    ftd = new FileTransferDialog(this, false);
                UIUtils.setLocationRelativeTo(ftd, this);
                ftd.setMaximumSize(maximumSize);
                ftd.setVisible(true);
                FileCallBack fileUp = new FileCallBack();
                upload(uploadFileList, fileUp);
                for(int i = 0; i < dataList.size(); i++)
                {
                    tmpList = (ArrayList)dataList.get(i);
                    if(!Utils.isNullString((String)tmpList.get(2)))
                    {
                        DOSChangeable assoObjectData = new DOSChangeable();
                        assoObjectData.setClassOuid(utils.cadPSClassOuid);
                        assoObjectData.put("Quantity", (String)tmpList.get(8));
                        ArrayList linkList = dos.listLinkForCADIntegration((String)tmpList.get(2));
                        if(linkList != null && linkList.contains((String)tmpList.get(5)))
                            dos.unlink((String)tmpList.get(2), (String)tmpList.get(5));
                        dos.link((String)tmpList.get(2), (String)tmpList.get(5), assoObjectData);
                    }
                }

                partTable.changeTableData();
                partInsertButton.setEnabled(false);
                partTable.setEditable(false);
                isRegisted = true;
            } else
            if(workType != 3 && workType == 4)
            {
                int maximumSize = 0;
                for(int i = 0; i < dataList.size(); i++)
                {
                    tmpList = (ArrayList)dataList.get(i);
                    DOSChangeable addData = new DOSChangeable();
                    ArrayList fieldList = new ArrayList();
                    addData.setClassOuid((String)tmpList.get(9));
                    addData.put("name", (String)tmpList.get(7));
                    String instanceOuid = dos.add(addData);
                    tmpList.set(5, instanceOuid);
                    for(int j = 0; j < dataList.size(); j++)
                        if(((String)tmpList.get(7)).equals((String)((ArrayList)dataList.get(j)).get(4)))
                            ((ArrayList)dataList.get(j)).set(2, instanceOuid);

                    File file = new File((String)tmpList.get(11));
                    String filePath = file.getAbsolutePath();
                    if(file == null || !file.isFile())
                        return;
                    maximumSize += (new Long(file.length())).intValue();
                    HashMap fileInfo = new HashMap();
                    fileInfo.put("md$filetype.id", (String)tmpList.get(10));
                    fileInfo.put("md$des", filePath);
                    String serverPath = dos.attachFile(instanceOuid, fileInfo);
                    tmpList.set(20, serverPath);
                    fileInfo = null;
                    fileInfo = new HashMap();
                    fileInfo.put("md$ouid", (String)tmpList.get(5));
                    fileInfo.put("md$path", (String)tmpList.get(20));
                    fileInfo.put("md$description", (String)tmpList.get(11));
                    fileInfo.put("md$filetype.id", (String)tmpList.get(10));
                    uploadFileList.add(fileInfo);
                }

                if(ftd == null)
                    ftd = new FileTransferDialog(this, false);
                UIUtils.setLocationRelativeTo(ftd, this);
                ftd.setMaximumSize(maximumSize);
                ftd.setVisible(true);
                FileCallBack fileUp = new FileCallBack();
                upload(uploadFileList, fileUp);
                for(int i = 0; i < dataList.size(); i++)
                {
                    tmpList = (ArrayList)dataList.get(i);
                    if(!Utils.isNullString((String)tmpList.get(2)))
                    {
                        DOSChangeable dosPackage = dos.getPackageWithName("Package for Solidworks");
                        DOSChangeable dosClass = dos.getClassWithName((String)dosPackage.get("ouid"), "Solidworks PS");
                        DOSChangeable assoObjectData = new DOSChangeable();
                        assoObjectData.setClassOuid((String)dosClass.get("ouid"));
                        assoObjectData.put("Quantity", (String)tmpList.get(8));
                        dos.link((String)tmpList.get(2), (String)tmpList.get(5), assoObjectData);
                    }
                }

                partTable.changeTableData();
                partInsertButton.setEnabled(false);
                partTable.setEditable(false);
                isRegisted = true;
            }
            String filePath = System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "env" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "solidworks" + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + fileName;
            File file = new File(filePath);
            if(file.isFile())
                new Boolean(file.delete());
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void upload(ArrayList uploadFileList, FileTransferCallback callback)
    {
        _ftc = callback;
        _uploadFileList = uploadFileList;
        SwingWorker worker = new SwingWorker() {

            public Object construct()
            {
                try
                {
                    for(int i = 0; i < uploadFileList.size(); i++)
                    {
                        fileInfoMap = (HashMap)uploadFileList.get(i);
                        serverPath = (String)fileInfoMap.get("md$path");
                        clientPath = (String)fileInfoMap.get("md$description");
                        dss.uploadFile(serverPath, clientPath, ftc);
                        if(fileInfoMap.containsKey("do.versionup"))
                            processCheckIn(fileInfoMap);
                    }

                }
                catch(IIPRequestException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            public void finished()
            {
                ftd.setVisible(false);
                ftd.dispose();
            }

            HashMap fileInfoMap;
            String serverPath;
            String clientPath;
            FileTransferCallback ftc;
            ArrayList uploadFileList;

            
            {
                fileInfoMap = null;
                serverPath = null;
                clientPath = null;
                ftc = _ftc;
                uploadFileList = _uploadFileList;
            }
        };
        worker.start();
    }

    public void processCheckIn(HashMap fileInfoMap)
    {
        try
        {
            Object returnObject = Utils.executeScriptFile(dos.getEventName(dos.getClassOuid((String)fileInfoMap.get("md$ouid")), "file.checkin.before"), dss, fileInfoMap);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                return;
            String checkInResult = dos.checkinFile((String)fileInfoMap.get("md$ouid"), fileInfoMap);
            Utils.executeScriptFile(dos.getEventName(dos.getClassOuid((String)fileInfoMap.get("md$ouid")), "file.checkin.after"), dss, fileInfoMap);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    private void closeButtonActionPerformed(ActionEvent evt)
    {
        removeWindowListener(listenerCommon);
        partInsertButton.removeActionListener(listenerCommon);
        partCloseButton.removeActionListener(listenerCommon);
        sizeComboBox.removeActionListener(listenerCommon);
        scaleComboBox.removeActionListener(listenerCommon);
        exitForm(null);
    }

    private void exitForm(WindowEvent evt)
    {
        if(_session != null)
        {
            _session.setProperty("dynapdm.part.insert.status", "closed");
            dispose();
        } else
        {
            dispose();
        }
    }

    private void sizeComboBox_actionPerformed(ActionEvent e)
    {
        int row = partTable.getTable().getSelectedRow();
        String ouidRow = partTable.getSelectedOuidRow(row);
        String selectedSizeOuid = (String)sizeModel.getSelectedOID();
        ArrayList rowData = null;
        if(ouidRow != null)
        {
            partTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
            rowData = (ArrayList)dataList.get((new Integer(ouidRow)).intValue());
        } else
        {
            rowData = (ArrayList)dataList.get(row);
        }
        rowData.set(16, selectedSizeOuid);
    }

    private void scaleComboBox_actionPerformed(ActionEvent e)
    {
        int row = partTable.getTable().getSelectedRow();
        String ouidRow = partTable.getSelectedOuidRow(row);
        String selectedScaleOuid = (String)scaleModel.getSelectedOID();
        ArrayList rowData = null;
        if(ouidRow != null)
        {
            partTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
            rowData = (ArrayList)dataList.get((new Integer(ouidRow)).intValue());
        } else
        {
            rowData = (ArrayList)dataList.get(row);
        }
        rowData.set(18, selectedScaleOuid);
    }

    public Color getBackground(DynaTable table, int row, int column)
    {
        Color isColor = null;
        if(cadKindInt == 0)
        {
            if(((Boolean)table.getValueAt(row, 0)).booleanValue())
            {
                if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(5)))
                    isColor = evenRowColor;
                else
                if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(13)))
                    isColor = notColor;
                else
                    isColor = addColor;
            } else
            if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(5)))
                isColor = table.getBackground();
            else
            if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(13)))
                isColor = notColor;
            else
                isColor = addColor;
        } else
        if(cadKindInt == 1 || cadKindInt == 2 || cadKindInt == 3)
            if(((Boolean)table.getValueAt(row, 0)).booleanValue())
            {
                if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(5)))
                    isColor = evenRowColor;
                else
                if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(13)))
                    isColor = notColor;
                else
                    isColor = addColor;
            } else
            if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(5)))
                isColor = table.getBackground();
            else
            if(Utils.isNullString((String)((ArrayList)dataList.get(row)).get(13)))
                isColor = notColor;
            else
                isColor = addColor;
        return isColor;
    }

    public Color getForeground(DynaTable table, int row, int column)
    {
        return table.getForeground();
    }

    public void setWorkType(int worktype)
    {
        workType = worktype;
    }

    private DOS dos;
    private NDS nds;
    private DSS dss;
    private ACL acl;
    private WFM wfm;
    private UIManagement newUI;
    Cursor handCursor;
    private BorderLayout blPartInsertForCad;
    private BorderLayout partBorderLayout;
    private JTabbedPane partTabbedPane;
    private JPanel partPanel;
    private JPanel partButtonPanel;
    private JScrollPane partTableScrPane;
    private JButton partInsertButton;
    private JButton partCloseButton;
    private Table partTable;
    private BoxLayout blPartButton;
    private ListenerCommon listenerCommon;
    private JComboBox sizeComboBox;
    private DynaComboBoxModel sizeModel;
    private JComboBox scaleComboBox;
    private DynaComboBoxModel scaleModel;
    private boolean isRegisted;
    private Color color;
    private Color evenRowColor;
    private Color addColor;
    private Color notColor;
    int totalAttrCnt;
    int totalAttrItemCnt;
    ArrayList attrItemList;
    ArrayList attrPositionItemList;
    ArrayList attrList;
    HashMap attrMap;
    int totalFileCnt;
    int totalFileItemCnt;
    ArrayList fileNameList;
    ArrayList fileItemList;
    ArrayList fileList;
    HashMap fileMap;
    private ArrayList dataList;
    private ArrayList fieldNameList;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private String fileName;
    private ArrayList classFieldList;
    private int workType;
    private int cadKindInt;
    private String _serverPath;
    private String _clientPath;
    private ArrayList _uploadFileList;
    private FileTransferCallback _ftc;
    private FileTransferDialog ftd;
    private Session _session;
    private CADIntegrationUtils utils;
    final int MODIFY_MODE = 1;













}