// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AssoClassUI.java

package dyna.framework.client;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.ExtLabel;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaDateChooser;
import dyna.uic.DynaTable;
import dyna.uic.DynaTheme;
import dyna.uic.MComboBoxModelObject;
import dyna.uic.MWindowLocationManager;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIBuilder, FieldLinkSelect, CodeSelectDialog, 
//            UIGeneration, MultiSelectDialog, ClientUtil, UIManagement

public class AssoClassUI extends JFrame
    implements ActionListener, WindowListener, MouseListener
{

    public AssoClassUI(ArrayList uiInfo, String classOuid)
    {
        mwlm = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        this.classOuid = "";
        classInfo = null;
        uiMode = 0;
        getDOSGlobal = null;
        instanceOuid = "";
        assoOuid = "";
        endOidList = null;
        collectionMap = null;
        fieldGroupNameToTitleMap = null;
        try
        {
            groupList = uiInfo;
            this.classOuid = classOuid;
            dos = DynaMOAD.dos;
            dss = DynaMOAD.dss;
            nds = DynaMOAD.nds;
            newUI = DynaMOAD.newUI;
            getDOSGlobal = getDOS();
            initialize();
            setInitData();
            setVisible(true);
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0049", "Regist", 3));
            saveButton.setIcon(new ImageIcon("icons/Ok.gif"));
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public AssoClassUI(ArrayList uiInfo, String classOuid, String assoInstanceOuid, int mode, ArrayList endOidList, String assoOuid)
    {
        mwlm = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        hoardData = new DOSChangeable();
        fieldAndFieldGroupMap = new HashMap();
        actionButtonList = null;
        selectButtonData = new DOSChangeable();
        selectButtonList = null;
        dateChooserButtonData = new DOSChangeable();
        dateChooserButtonList = null;
        this.classOuid = "";
        classInfo = null;
        uiMode = 0;
        getDOSGlobal = null;
        instanceOuid = "";
        this.assoOuid = "";
        this.endOidList = null;
        collectionMap = null;
        fieldGroupNameToTitleMap = null;
        uiMode = mode;
        instanceOuid = assoInstanceOuid;
        groupList = uiInfo;
        this.classOuid = classOuid;
        this.endOidList = endOidList;
        this.assoOuid = assoOuid;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        nds = DynaMOAD.nds;
        newUI = DynaMOAD.newUI;
        getDOSGlobal = getDOS();
        initialize();
        setInitData();
    }

    public void initialize()
    {
        actionButtonList = new ArrayList();
        selectButtonList = new ArrayList();
        dateChooserButtonList = new ArrayList();
        Boolean isVisible = null;
        mwlm = new MWindowLocationManager(this);
        try
        {
            setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
            getContentPane().setLayout(new BorderLayout());
            mainMenuBar = new JMenuBar();
            mainMenuBar.putClientProperty("Plastic.borderStyle", BorderStyle.EMPTY);
            setJMenuBar(mainMenuBar);
            mainMenu = new JMenu(" ");
            mainMenu.setEnabled(false);
            mainMenu.setFont(DynaTheme.mediumPlainFont);
            mainMenuBar.add(mainMenu);
            mainToolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
            mainToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
            openButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Favorite.gif")));
            openButton.setToolTipText(DynaMOAD.getMSRString("QST_0006", "Add to My Folder", 3));
            openButton.setActionCommand("Open");
            openButton.addActionListener(this);
            saveButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
            saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "save", 3));
            saveButton.setActionCommand("Save");
            saveButton.addActionListener(this);
            mainToolBar.add(saveButton);
            mainToolBar.add(openButton);
            classInfo = getClassInfo(classOuid);
            Utils.sort(groupList);
            if(fieldGroupNameToTitleMap == null)
                fieldGroupNameToTitleMap = new HashMap();
            for(int i = 0; i < groupList.size(); i++)
            {
                DOSChangeable fieldGroupDC = (DOSChangeable)groupList.get(i);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    Integer viewLocationInteger = (Integer)fieldGroupDC.get("location");
                    if(viewLocationInteger == null)
                        viewLocationInteger = new Integer(2);
                    fieldGroupNameToTitleMap.put(fieldGroupDC.get("name"), fieldGroupDC.get("title"));
                    groupPanel = UIBuilder.createFieldGroupPanel(dos, hoardData, fieldGroupDC, actionButtonList, selectButtonData, selectButtonList, dateChooserButtonData, dateChooserButtonList, collectionMap, fieldAndFieldGroupMap, this, this);
                    mwlm.add(viewLocationInteger.intValue(), groupPanel, (String)fieldGroupDC.get("title"), null, (String)fieldGroupDC.get("description"));
                    groupPanel = null;
                }
            }

            getContentPane().add(mainToolBar, "North");
            getContentPane().add(DynaMOAD.buildStatusBar(statusField), "South");
            int width = 0;
            int height = 0;
            int heightCount = 0;
            JTabbedPane tempTabbedPane = null;
            Dimension dimension = null;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            tempTabbedPane = mwlm.getCenterTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = dimension.width + 5;
                height = dimension.height;
                heightCount++;
            }
            tempTabbedPane = mwlm.getTopTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = Math.max(dimension.width + 5, width);
                height += dimension.height;
                heightCount++;
            }
            tempTabbedPane = mwlm.getBottomTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = Math.max(dimension.width + 5, width);
                height += dimension.height;
                heightCount++;
            }
            tempTabbedPane = mwlm.getLeftTabbedPane();
            if(tempTabbedPane != null)
            {
                dimension = tempTabbedPane.getPreferredSize();
                width = width + dimension.width + 5;
                height = Math.max(dimension.height, height);
            }
            dimension = null;
            heightCount--;
            height = height + 140 + heightCount * 32;
            width = Math.min(width += 10, screenSize.width);
            height = Math.min(height, screenSize.height);
            String ltitle = "";
            if(endOidList != null && endOidList.size() >= 2)
            {
                end1 = (String)endOidList.get(0);
                end2 = (String)Utils.tokenizeMessage((String)endOidList.get(1), '^').get(0);
                DOSChangeable dos1 = dos.get(end1);
                ltitle = (String)dos1.get("md$number");
                DOSChangeable dos2 = dos.get(end2);
                ltitle = ltitle + "  <<==  " + (String)dos2.get("md$number");
                System.out.println("AssoClassUI end1 ouid = " + end1);
                System.out.println("AssoClassUI end2 ouid = " + end2);
            }
            setTitle((String)classInfo.get("title") + " " + ltitle);
            width = Math.max(width, 400);
            height = Math.max(height, 300);
            setSize(width, height);
            setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public DOSChangeable setModifyData()
    {
        JScrollPane scrollPane = null;
        Object object = null;
        DynaTable table = null;
        JComponent uiComponent = null;
        try
        {
            for(int i = 0; i < groupList.size(); i++)
            {
                DOSChangeable fieldGroupDC = (DOSChangeable)groupList.get(i);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
                    ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
                    if(inheritedFields != null)
                    {
                        if(fieldList == null)
                            fieldList = new ArrayList();
                        ArrayList tmpFieldList = new ArrayList();
                        DOSChangeable tmpData = null;
                        for(int j = 0; j < inheritedFields.size(); j++)
                        {
                            tmpData = (DOSChangeable)inheritedFields.get(j);
                            if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                                tmpFieldList.add((String)tmpData.get("ouid"));
                        }

                        if(tmpFieldList.size() > 0)
                            fieldList.addAll(0, tmpFieldList);
                    }
                    if(fieldList != null && fieldList.size() != 0)
                    {
                        byte changeable = 0;
                        for(int j = 0; j < fieldList.size(); j++)
                        {
                            fieldUIData = dos.getField((String)fieldList.get(j));
                            String txtFieldStr = (String)fieldUIData.get("name") + (String)fieldGroupDC.get("name");
                            String fldNamStr = (String)fieldUIData.get("name");
                            byte type = ((Byte)fieldUIData.get("type")).byteValue();
                            changeable = Utils.getByte((Byte)fieldUIData.get("changeable"));
                            uiComponent = (JComponent)hoardData.get(txtFieldStr);
                            if(uiComponent != null && (uiComponent instanceof JTextField))
                            {
                                if(type == 24 || type == 25 || type == 16)
                                {
                                    ((JTextField)uiComponent).setEditable(false);
                                    ((JTextField)uiComponent).setBackground(DynaTheme.panelBackgroundColor);
                                } else
                                {
                                    String stringValue = ((JTextField)uiComponent).getText();
                                    if(type == 4)
                                        getDOSGlobal.put(fldNamStr, Utils.getDouble(stringValue));
                                    else
                                    if(type == 6)
                                        getDOSGlobal.put(fldNamStr, Utils.getInteger(stringValue));
                                    else
                                    if(type == 2)
                                        getDOSGlobal.put(fldNamStr, Utils.getByte(stringValue));
                                    else
                                    if(type == 7)
                                        getDOSGlobal.put(fldNamStr, Utils.getLong(stringValue));
                                    else
                                    if(type == 8)
                                        getDOSGlobal.put(fldNamStr, Utils.getShort(stringValue));
                                    else
                                    if(type == 5)
                                        getDOSGlobal.put(fldNamStr, Utils.getFloat(stringValue));
                                    else
                                    if(Utils.isNullString(stringValue))
                                        getDOSGlobal.put(fldNamStr, null);
                                    else
                                        getDOSGlobal.put(fldNamStr, stringValue);
                                    if(changeable == 4 || changeable == 3 && !Utils.isNullString(((JTextField)uiComponent).getText()))
                                    {
                                        ((JTextField)uiComponent).setBackground(DynaTheme.panelBackgroundColor);
                                        ((JTextField)uiComponent).setEditable(false);
                                    } else
                                    {
                                        ((JTextField)uiComponent).setEditable(true);
                                    }
                                }
                            } else
                            if(uiComponent != null && (uiComponent instanceof JComboBox))
                            {
                                if(type == 1)
                                {
                                    if((String)((JComboBox)uiComponent).getSelectedItem() == null)
                                        getDOSGlobal.put(fldNamStr, null);
                                    else
                                    if(((String)((JComboBox)uiComponent).getSelectedItem()).equals("True"))
                                        getDOSGlobal.put(fldNamStr, new Boolean(true));
                                    else
                                        getDOSGlobal.put(fldNamStr, new Boolean(false));
                                } else
                                if(type == 24 || type == 25)
                                    if(((JComboBox)uiComponent).getSelectedItem() == null)
                                    {
                                        getDOSGlobal.put(fldNamStr, null);
                                    } else
                                    {
                                        Object comboObject = ((JComboBox)uiComponent).getSelectedItem();
                                        if(comboObject instanceof MComboBoxModelObject)
                                            getDOSGlobal.put(fldNamStr, ((MComboBoxModelObject)comboObject).getOuid());
                                        comboObject = null;
                                    }
                                if(changeable == 4 || changeable == 3 && ((JComboBox)uiComponent).getSelectedItem() != null)
                                    ((JComboBox)uiComponent).setEnabled(false);
                                else
                                    ((JComboBox)uiComponent).setEnabled(true);
                            } else
                            if(uiComponent != null && (uiComponent instanceof JScrollPane))
                            {
                                scrollPane = (JScrollPane)uiComponent;
                                object = scrollPane.getViewport().getView();
                                if(object instanceof JTextArea)
                                {
                                    if(!Utils.isNullString(((JTextArea)object).getText()))
                                        getDOSGlobal.put(fldNamStr, ((JTextArea)object).getText());
                                    else
                                        getDOSGlobal.put(fldNamStr, null);
                                    if(changeable == 4 || changeable == 3 && !Utils.isNullString(((JTextArea)object).getText()))
                                    {
                                        ((JTextArea)object).setEditable(false);
                                        ((JTextArea)object).setEnabled(false);
                                    } else
                                    {
                                        ((JTextArea)object).setEditable(true);
                                        ((JTextArea)object).setEnabled(true);
                                    }
                                } else
                                if(object instanceof DynaTable)
                                {
                                    table = (DynaTable)object;
                                    getCollectionTable(fieldUIData, fieldGroupDC, getDOSGlobal);
                                    if(changeable == 4 || changeable == 3 && table.getRowCount() > 0)
                                        table.setEnabled(false);
                                    else
                                        table.setEnabled(true);
                                }
                            }
                        }

                    }
                }
            }

            getDOSGlobal.setClassOuid(classOuid);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return getDOSGlobal;
    }

    public void setFieldInObject(String sign, String selectOuid, String number)
    {
        LinkedList list = (LinkedList)Utils.tokenizeMessage(sign, '^');
        String mapId = (String)list.get(2);
        JTextField field = (JTextField)hoardData.get(mapId);
        field.setText(number);
        field.setCaretPosition(0);
        field.setActionCommand("DATATYPE_OBJECT^" + selectOuid);
        if(!Utils.isNullString(selectOuid))
            field.setCursor(Cursor.getPredefinedCursor(12));
        getDOSGlobal.put((String)hoardData.get("name@@" + mapId), selectOuid);
        field = null;
        list.clear();
        list = null;
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        ((JTextField)dateChooserButtonData.get(sign)).setText(selectOuid);
        ((JTextField)dateChooserButtonData.get(sign)).setCaretPosition(0);
    }

    private void setInitData()
    {
        HashMap tmpMap = hoardData.getValueMap();
        Iterator iterator = tmpMap.keySet().iterator();
        String mapKey = null;
        String tmpKey = null;
        String tmpVal = null;
        while(iterator.hasNext()) 
        {
            mapKey = (String)iterator.next();
            if(mapKey.startsWith("initvalue@@"))
            {
                tmpKey = mapKey.substring(11);
                tmpVal = (String)tmpMap.get(mapKey);
                getDOSGlobal.put(tmpKey, tmpVal);
            }
        }
    }

    public void setData(DOSChangeable info)
    {
        try
        {
            JScrollPane scrollPane = null;
            Object object = null;
            byte changeable = 0;
            JComponent uiComponent = null;
            for(int i = 0; i < groupList.size(); i++)
            {
                DOSChangeable fieldGroupDC = (DOSChangeable)groupList.get(i);
                if(!isViewableFieldGroup(fieldGroupDC))
                {
                    fieldGroupDC = null;
                } else
                {
                    ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
                    ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
                    if(inheritedFields != null)
                    {
                        if(fieldList == null)
                            fieldList = new ArrayList();
                        ArrayList tmpFieldList = new ArrayList();
                        DOSChangeable tmpData = null;
                        for(int j = 0; j < inheritedFields.size(); j++)
                        {
                            tmpData = (DOSChangeable)inheritedFields.get(j);
                            if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                                tmpFieldList.add((String)tmpData.get("ouid"));
                        }

                        if(tmpFieldList.size() > 0)
                            fieldList.addAll(0, tmpFieldList);
                    }
                    if(fieldList != null && fieldList.size() != 0)
                    {
                        for(int j = 0; j < fieldList.size(); j++)
                        {
                            fieldUIData = dos.getField((String)fieldList.get(j));
                            changeable = Utils.getByte((Byte)fieldUIData.get("changeable"));
                            String tmpStr = (String)fieldUIData.get("name") + (String)fieldGroupDC.get("name");
                            String fldNamStr = (String)fieldUIData.get("name");
                            byte type = ((Byte)fieldUIData.get("type")).byteValue();
                            uiComponent = (JComponent)hoardData.get(tmpStr);
                            if(uiComponent != null && (uiComponent instanceof JTextField))
                            {
                                if(type == 24 || type == 25 || type == 16)
                                {
                                    ((JTextField)uiComponent).setText((String)info.get("name@" + fldNamStr));
                                    ((JTextField)uiComponent).setActionCommand("DATATYPE_OBJECT^" + (String)info.get(fldNamStr));
                                    if(type == 16 && !Utils.isNullString((String)info.get(fldNamStr)))
                                        ((JTextField)uiComponent).setCursor(Cursor.getPredefinedCursor(12));
                                    else
                                        ((JTextField)uiComponent).setCursor(Cursor.getPredefinedCursor(0));
                                } else
                                {
                                    if(type == 2 || type == 4 || type == 5 || type == 6 || type == 7 || type == 8)
                                    {
                                        if(info.get(fldNamStr) == null)
                                            ((JTextField)uiComponent).setText("");
                                        else
                                            ((JTextField)uiComponent).setText(((Number)info.get(fldNamStr)).toString());
                                    } else
                                    {
                                        ((JTextField)uiComponent).setText((String)info.get(fldNamStr));
                                    }
                                    if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                    {
                                        ((JTextField)uiComponent).setEditable(false);
                                        ((JTextField)uiComponent).setBackground(DynaTheme.panelBackgroundColor);
                                    } else
                                    {
                                        ((JTextField)uiComponent).setEditable(true);
                                    }
                                }
                                ((JTextField)uiComponent).setCaretPosition(0);
                            } else
                            if(uiComponent != null && (uiComponent instanceof JComboBox))
                            {
                                String comboItem = "";
                                if(type == 24 || type == 25 || type == 16)
                                {
                                    Object comboObject = ((JComboBox)uiComponent).getModel();
                                    if(comboObject instanceof DefaultComboBoxModel)
                                    {
                                        DefaultComboBoxModel comboModel = (DefaultComboBoxModel)comboObject;
                                        String codeOuid = (String)info.get(fldNamStr);
                                        int n = comboModel.getSize();
                                        for(int m = 0; m < n && codeOuid != null; m++)
                                        {
                                            comboObject = comboModel.getElementAt(m);
                                            if((comboObject instanceof MComboBoxModelObject) && codeOuid.equals(((MComboBoxModelObject)comboObject).getOuid()))
                                            {
                                                ((JComboBox)uiComponent).setSelectedIndex(m);
                                                break;
                                            }
                                            comboObject = null;
                                        }

                                    }
                                    comboObject = null;
                                } else
                                if(type == 1)
                                {
                                    if((Boolean)info.get(fldNamStr) != null)
                                    {
                                        if(Utils.getBoolean((Boolean)info.get(fldNamStr)))
                                            comboItem = "True";
                                        else
                                            comboItem = "False";
                                    } else
                                    {
                                        comboItem = "";
                                    }
                                    ((JComboBox)hoardData.get(tmpStr)).setSelectedItem(comboItem);
                                }
                                if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                    ((JComboBox)uiComponent).setEnabled(false);
                                else
                                    ((JComboBox)uiComponent).setEnabled(true);
                            } else
                            if(uiComponent != null && (uiComponent instanceof JScrollPane))
                            {
                                scrollPane = (JScrollPane)uiComponent;
                                object = scrollPane.getViewport().getView();
                                if(object instanceof DynaTable)
                                {
                                    setCollectionTable(fieldUIData, fieldGroupDC, info);
                                    if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                        ((DynaTable)object).setEnabled(false);
                                    else
                                        ((DynaTable)object).setEnabled(true);
                                } else
                                if(object instanceof JTextArea)
                                {
                                    ((JTextArea)object).setText((String)info.get(fldNamStr));
                                    ((JTextArea)object).setCaretPosition(0);
                                    if(changeable == 4 || changeable == 3 && info.get(fldNamStr) != null)
                                    {
                                        ((JTextArea)object).setEditable(false);
                                        ((JTextArea)object).setEnabled(false);
                                    } else
                                    {
                                        ((JTextArea)object).setEditable(true);
                                        ((JTextArea)object).setEnabled(true);
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public DOSChangeable getDOS()
    {
        try
        {
            DOSChangeable getDC = null;
            if(!Utils.isNullString(instanceOuid))
                getDC = dos.get(instanceOuid);
            else
                getDC = new DOSChangeable();
            return getDC;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return null;
    }

    public void setCollectionTable(DOSChangeable fieldData, DOSChangeable groupData, DOSChangeable data)
    {
        Table innerTable = (Table)hoardData.get((String)fieldData.get("name") + (String)groupData.get("name") + "innerTable");
        ArrayList dataList = innerTable.getList();
        if(dataList == null)
            return;
        ArrayList infoList = (ArrayList)data.get((String)fieldData.get("name"));
        if(infoList == null || infoList.size() == 0)
        {
            dataList.clear();
            innerTable.changeTableData();
            return;
        }
        ArrayList tempList = null;
        byte dataType = Utils.getByte((Byte)fieldData.get("type"));
        if(dataType == 24 || dataType == 25 || dataType == 16)
        {
            innerTable.setData((ArrayList)infoList.clone());
        } else
        {
            for(Iterator infoKey = infoList.iterator(); infoKey.hasNext();)
            {
                tempList = new ArrayList();
                tempList.add(infoKey.next());
                dataList.add(tempList);
                tempList = null;
            }

        }
        innerTable.changeTableData();
    }

    public void getCollectionTable(DOSChangeable fieldData, DOSChangeable groupData, DOSChangeable data)
    {
        Table innerTable = (Table)hoardData.get((String)fieldData.get("name") + (String)groupData.get("name") + "innerTable");
        ArrayList dataList = innerTable.getList();
        ArrayList tempList = null;
        ArrayList infoList = (ArrayList)data.get((String)fieldData.get("name"));
        if(infoList != null)
            infoList = null;
        byte dataType = Utils.getByte((Byte)fieldData.get("type"));
        if(dataType == 24 || dataType == 16 || dataType == 25)
            tempList = (ArrayList)dataList.clone();
        else
        if(dataList != null)
        {
            tempList = new ArrayList();
            for(int x = 0; x < dataList.size(); x++)
                tempList.add(((ArrayList)dataList.get(x)).get(0));

        }
        data.put((String)fieldData.get("name"), tempList);
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Close"))
            windowClosing(null);
        else
        if(command.equals("Save"))
            try
            {
                if(uiMode == 0)
                {
                    HashMap tempMap = new HashMap();
                    tempMap.put("end1", endOidList.get(0));
                    tempMap.put("end2", endOidList.get(1));
                    tempMap.put("end0", setModifyData());
                    System.out.println("[link before] 1");
                    Object returnObject = Utils.executeScriptFile(dos.getEventName(classOuid, "link.before"), dss, tempMap);
                    if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                    {
                        dos.link((String)endOidList.get(0), (String)endOidList.get(1), (DOSChangeable)tempMap.get("end0"));
                        getDOSGlobal = getDOS();
                        Utils.executeScriptFile(dos.getEventName(classOuid, "link.after"), dss);
                    }
                    tempMap.clear();
                    tempMap = null;
                    dispose();
                } else
                if(uiMode == 1)
                {
                    setModifyData();
                    if(getDOSGlobal.isChanged())
                    {
                        getDOSGlobal.put("ouid@association", assoOuid);
                        getDOSGlobal.put("as$end1", end1);
                        getDOSGlobal.put("as$end2", end2);
                        dos.set(getDOSGlobal);
                        getDOSGlobal.setOriginalValueMap(getDOSGlobal.getValueMap());
                        String tmpStr = "";
                        tmpStr = DynaMOAD.getMSRString("WRD_0003", "update", 3);
                        Object args[] = {
                            tmpStr
                        };
                        tmpStr = DynaMOAD.getMSRString("INF_0001", "", 0);
                        MessageFormat form = new MessageFormat(tmpStr);
                        String messageStr = form.format(((Object) (args)));
                        String titleStr = DynaMOAD.getMSRString("WRD_0004", "information", 3);
                        JOptionPane.showMessageDialog(this, messageStr, titleStr, 0);
                    } else
                    {
                        System.out.println("no changed");
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.startsWith("ObjectSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                    String classOuid2 = (String)testList.get(1);
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(!Utils.isNullString(classOuid2))
                    {
                        String values[] = FieldLinkSelect.showDialog(this, (Component)actionEvent.getSource(), classOuid2, command);
                        if(values != null)
                        {
                            field.setText(values[1]);
                            field.setCaretPosition(0);
                            field.setActionCommand("DATATYPE_OBJECT^" + values[0]);
                            if(!Utils.isNullString(values[0]))
                                field.setCursor(Cursor.getPredefinedCursor(12));
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), values[0]);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), values[1]);
                        } else
                        {
                            field.setText("");
                            field.setCaretPosition(0);
                            field.setActionCommand("DATATYPE_OBJECT^");
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), null);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), "");
                        }
                    } else
                    {
                        System.out.println("type class ouid is Null");
                    }
                }

        } else
        if(command.startsWith("CodeSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                    String codeOuid = (String)testList.get(1);
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(!Utils.isNullString(codeOuid))
                    {
                        String values[] = CodeSelectDialog.showDialog(this, (Component)actionEvent.getSource(), true, codeOuid, command);
                        if(values != null)
                        {
                            field.setText(values[2]);
                            field.setCaretPosition(0);
                            if(!Utils.isNullString(values[1]))
                                field.setCursor(Cursor.getPredefinedCursor(12));
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), values[1]);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), values[2]);
                        }
                    } else
                    {
                        System.out.println("code ouid is Null");
                    }
                }

        } else
        if(command.startsWith("CodeReferenceSelectButtonClick"))
        {
            for(int i = 0; i < selectButtonList.size(); i++)
                if(command.equals(selectButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '^');
                    String fieldOuid = (String)testList.get(1);
                    String codeOuid = null;
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(Utils.isNullString(fieldOuid))
                        return;
                    try
                    {
                        codeOuid = DynaMOAD.dos.lookupCodeFromSelectionTable(fieldOuid, getInstanceData());
                        if(Utils.isNullString(codeOuid))
                            return;
                        DOSChangeable code = dos.getCode(codeOuid);
                        if(code == null)
                            return;
                        code = null;
                    }
                    catch(IIPRequestException e)
                    {
                        e.printStackTrace();
                    }
                    if(!Utils.isNullString(codeOuid))
                    {
                        String values[] = CodeSelectDialog.showDialog(this, (Component)actionEvent.getSource(), true, codeOuid, command);
                        if(values != null)
                        {
                            field.setText(values[2]);
                            field.setCaretPosition(0);
                            if(!Utils.isNullString(values[1]))
                                field.setCursor(Cursor.getPredefinedCursor(12));
                            getDOSGlobal.put((String)hoardData.get("name@@" + mapId), values[1]);
                            getDOSGlobal.put("name@" + (String)hoardData.get("name@@" + mapId), values[2]);
                        }
                    } else
                    {
                        System.out.println("code ouid is Null");
                    }
                }

        } else
        if(command.startsWith("DateChooserButtonClick"))
        {
            for(int i = 0; i < dateChooserButtonList.size(); i++)
                if(command.equals(dateChooserButtonList.get(i)))
                {
                    java.util.List testList = Utils.tokenizeMessage((String)dateChooserButtonList.get(i), '/');
                    String classOuid2 = (String)testList.get(1);
                    if(classOuid2 != null)
                    {
                        new DynaDateChooser(this);
                        java.util.Date date = DynaDateChooser.showDialog((Component)actionEvent.getSource(), null);
                        if(date != null)
                            setFieldInDateField(sdfYMD.format(date), (String)dateChooserButtonList.get(i));
                        else
                            setFieldInDateField("", (String)dateChooserButtonList.get(i));
                    } else
                    {
                        System.out.println("type class ouid is Null");
                    }
                }

        } else
        if(command.startsWith("ActionButtonClick"))
        {
            for(int i = 0; i < actionButtonList.size(); i++)
                if(command.equals(((ArrayList)actionButtonList.get(i)).get(1)))
                    try
                    {
                        Utils.executeScriptFile(dos.getActionScriptName((String)((ArrayList)actionButtonList.get(i)).get(0)), dss, this);
                    }
                    catch(IIPRequestException ie)
                    {
                        System.err.println(ie);
                    }

        }
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        removeWindowListener(this);
        openButton.removeActionListener(this);
        saveButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        dispose();
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        MultiSelectDialog msd = null;
        DynaTable table = null;
        Object object = null;
        Table xTable = null;
        if(mouseEvent.getClickCount() != 2 || !SwingUtilities.isLeftMouseButton(mouseEvent))
            return;
        if(mouseEvent.getSource() instanceof JTextField)
        {
            object = mouseEvent.getComponent();
            String keyString = (String)Utils.getMapKeyByValue(hoardData.getValueMap(), object);
            if(Utils.isNullString(keyString))
                return;
            String selectOuid = (String)getDOSGlobal.get((String)hoardData.get("name@@" + keyString));
            if(Utils.isNullString(selectOuid))
                return;
            try
            {
                String selectClassOuid = DynaMOAD.dos.getClassOuid(selectOuid);
                UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, selectOuid, 1);
                uiGeneration.setVisible(true);
                uiGeneration = null;
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        } else
        if(collectionMap != null)
        {
            object = mouseEvent.getComponent();
            if(object instanceof DynaTable)
                table = (DynaTable)object;
            else
            if(object instanceof JScrollPane)
                table = (DynaTable)((JScrollPane)object).getViewport().getView();
            xTable = (Table)collectionMap.get(table.hashCode() + "Table");
            DOSChangeable fieldData = (DOSChangeable)collectionMap.get(table);
            byte dataType = Utils.getByte((Byte)fieldData.get("type"));
            ArrayList tableData = xTable.getList();
            ArrayList tables = (ArrayList)collectionMap.get((String)fieldData.get("name") + "TableData");
            msd = new MultiSelectDialog(this, true, fieldData, xTable.getList());
            msd.setVisible(true);
            msd.dispose();
            msd = null;
            for(int i = 0; i < tables.size(); i++)
                ((Table)tables.get(i)).changeTableData();

            ArrayList returnedList = xTable.getList();
            ArrayList inputList = new ArrayList();
            ArrayList tmpList = null;
            if(dataType != 16 && dataType != 24 && dataType != 25)
            {
                if(returnedList != null)
                {
                    for(int x = 0; x < returnedList.size(); x++)
                    {
                        tmpList = new ArrayList();
                        tmpList.add(returnedList.get(x));
                        inputList.add(tmpList);
                        tmpList = null;
                    }

                }
                getDOSGlobal.put((String)fieldData.get("name"), inputList);
            } else
            {
                getDOSGlobal.put((String)fieldData.get("name"), returnedList.clone());
            }
            xTable = null;
            fieldData = null;
        }
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

    public void setFieldGroupEnabled(String name, boolean enable)
    {
        if(fieldGroupNameToTitleMap == null || fieldGroupNameToTitleMap.isEmpty())
            return;
        String title = (String)fieldGroupNameToTitleMap.get(name);
        if(Utils.isNullString(title))
            return;
        String titleTemp = null;
        JTabbedPane fieldGroupPane = mwlm.getCenterTabbedPane();
        int count = fieldGroupPane.getTabCount();
        int i;
        for(i = 0; i < count; i++)
        {
            titleTemp = fieldGroupPane.getTitleAt(i);
            if(title.equals(titleTemp))
                break;
            titleTemp = null;
        }

        if(!Utils.isNullString(titleTemp))
            fieldGroupPane.setEnabledAt(i, enable);
    }

    public void saveButtonClick()
    {
        saveButton.doClick();
    }

    private boolean isViewableFieldGroup(DOSChangeable fieldGroup)
    {
        if(fieldGroup == null)
            return false;
        Integer viewLocationInteger = (Integer)fieldGroup.get("location");
        int viewLocation;
        if(viewLocationInteger == null)
            viewLocation = 2;
        else
            viewLocation = viewLocationInteger.intValue();
        if(viewLocation != 1 && viewLocation != 2 && viewLocation != 3 && viewLocation != 4)
            return false;
        Boolean isVisible = (Boolean)fieldGroup.get("is.visible");
        return isVisible != null && isVisible.booleanValue();
    }

    private DOSChangeable getClassInfo(String classOuid)
    {
        if(Utils.isNullString(classOuid))
            return null;
        try
        {
            if(classInfo != null)
            {
                if(classOuid.equals(classInfo.get("ouid")))
                    return classInfo;
                classInfo = null;
            }
            classInfo = dos.getClass(classOuid);
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
            return null;
        }
        return classInfo;
    }

    public void setIcon(String classOuid)
    {
        ImageIcon imageIcon = ClientUtil.getImageIcon(classOuid);
        java.awt.Image image = null;
        if(imageIcon == null)
        {
            image = Toolkit.getDefaultToolkit().getImage("icons/Class.gif");
        } else
        {
            image = imageIcon.getImage();
            imageIcon = null;
        }
        setIconImage(image);
        image = null;
    }

    public DOSChangeable getInstanceData()
    {
        if(uiMode == 0)
        {
            setModifyData();
            return getDOSGlobal;
        }
        if(getDOSGlobal == null || getDOSGlobal.getValueMap() == null || getDOSGlobal.getValueMap().isEmpty())
            getDOS();
        return getDOSGlobal;
    }

    private DOS dos;
    private DSS dss;
    private NDS nds;
    private UIManagement newUI;
    private MWindowLocationManager mwlm;
    private JMenuBar mainMenuBar;
    private JMenu mainMenu;
    private JToolBar mainToolBar;
    private JButton openButton;
    private JButton saveButton;
    private JButton closeButton;
    private JLabel statusField;
    private JPanel groupPanel;
    private ArrayList groupList;
    private DOSChangeable fieldUIData;
    private DOSChangeable hoardData;
    private HashMap fieldAndFieldGroupMap;
    private ArrayList actionButtonList;
    private DOSChangeable selectButtonData;
    private ArrayList selectButtonList;
    private DOSChangeable dateChooserButtonData;
    private ArrayList dateChooserButtonList;
    private String classOuid;
    private DOSChangeable classInfo;
    private int uiMode;
    private DOSChangeable getDOSGlobal;
    private String instanceOuid;
    private String assoOuid;
    private ArrayList endOidList;
    private HashMap collectionMap;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private String end1;
    private String end2;
    private HashMap fieldGroupNameToTitleMap;
    final int REGISTER_MODE = 0;
    final int MODIFY_MODE = 1;
    final byte DATATYPE_BOOLEAN = 1;
    final byte DATATYPE_OBJECT = 16;
    final byte DATATYPE_DATETIME = 21;
    final byte DATATYPE_DATE = 22;
    final boolean inverse_process = false;
    private final int MINIMUM_WIDTH = 400;
    private final int MINIMUM_HEIGHT = 300;
    private final int WINDOW_HEIGHT_FACTOR = 140;

}