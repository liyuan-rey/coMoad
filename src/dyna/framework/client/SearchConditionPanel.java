// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SearchConditionPanel.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaDateChooser;
import dyna.util.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, LogIn, SearchCondition, 
//            FieldLinkSelect, CodeSelectDialog

public class SearchConditionPanel extends JPanel
    implements ActionListener
{

    public SearchConditionPanel(Object parent, String modelOuid, String classOuid, ArrayList fieldList)
    {
        this(parent, modelOuid, classOuid, fieldList, null);
    }

    public SearchConditionPanel(Object parent, String modelOuid, String classOuid, ArrayList fieldList, HashMap searchCondition)
    {
        classInfo = null;
        versionableClass = false;
        searchConditionMap = null;
        hoardData = new DOSChangeable();
        filterList = null;
        this.fieldList = null;
        this.searchCondition = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        selectCnt = 0;
        countPerPage = 1000;
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        this.parent = parent;
        this.modelOuid = modelOuid;
        this.classOuid = classOuid;
        this.fieldList = fieldList;
        this.searchCondition = searchCondition;
        initialize();
    }

    public void initialize()
    {
        try
        {
            classInfo = dos.getClass(classOuid);
            if(classInfo != null && classInfo.getValueMap().containsKey("is.versionable") && Utils.getBoolean((Boolean)classInfo.getValueMap().get("is.versionable")))
                versionableClass = true;
            else
                versionableClass = false;
            selectButtonList = new ArrayList();
            dateChooserButtonList = new ArrayList();
            int y_pos = 0;
            if(fieldList != null)
            {
                initStatus();
                String division01 = "Last Released";
                String division02 = "Work In Progress";
                String division05 = "All Version";
                String division03 = "Created Date";
                String division04 = "Modified Date";
                String versionType[] = {
                    division01, division02, division05, division03, division04
                };
                String trueStr = "true";
                String falseStr = "false";
                String trueOrFalse[] = {
                    "", trueStr, falseStr
                };
                makeFilterComboBox();
                listFilterLabel = new JLabel(DynaMOAD.getMSRString("WRD_0015", "Filter", 3));
                listFilterLabel.setBounds(10, 12, 100, 20);
                listFilterComboBox.setBounds(110, 12, 160, 20);
                add(listFilterLabel);
                add(listFilterComboBox);
                boolean tmpBoolean = false;
                boolean versionBoolean = false;
                Boolean isVisible = null;
                String fieldOuid = null;
                String fieldName = null;
                String fieldCode = null;
                Byte fieldType = null;
                int i = -1;
                for(int j = 0; j < fieldList.size(); j++)
                {
                    DOSChangeable fieldData = (DOSChangeable)fieldList.get(j);
                    if(fieldData != null)
                    {
                        isVisible = (Boolean)fieldData.get("is.visible");
                        if(!Utils.getBoolean(isVisible))
                        {
                            fieldData = null;
                        } else
                        {
                            i++;
                            JLabel advancedTestLabel = new JLabel();
                            JTextField advancedTestTextField = new JTextField();
                            JButton dateSelectButton = new JButton();
                            JButton selectButtonByType = new JButton();
                            fieldOuid = (String)fieldData.get("ouid");
                            fieldName = (String)fieldData.get("name");
                            fieldType = (Byte)fieldData.get("type");
                            fieldCode = (String)fieldData.get("code");
                            if(fieldCode == null)
                                fieldCode = "";
                            advancedTestLabel = new JLabel(DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0));
                            if(versionBoolean)
                                advancedTestLabel.setBounds(10, 35 + 23 * (i + 2), 100, 20);
                            else
                                advancedTestLabel.setBounds(10, 35 + 23 * i, 100, 20);
                            add(advancedTestLabel);
                            if(!fieldCode.equals("md$status") && !fieldOuid.equals("version.condition.type") && fieldType.byteValue() != 1)
                            {
                                if(!fieldOuid.equals("md$cdate") && !fieldOuid.equals("md$mdate") && fieldType.byteValue() != 22 && fieldType.byteValue() != 21)
                                {
                                    if(fieldType.byteValue() == 16 || fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                                    {
                                        if(versionBoolean)
                                            advancedTestTextField.setBounds(110, 35 + 23 * (i + 2), 143, 20);
                                        else
                                            advancedTestTextField.setBounds(110, 35 + 23 * i, 143, 20);
                                    } else
                                    if(versionBoolean)
                                        advancedTestTextField.setBounds(110, 35 + 23 * (i + 2), 160, 20);
                                    else
                                        advancedTestTextField.setBounds(110, 35 + 23 * i, 160, 20);
                                    add(advancedTestTextField);
                                    hoardData.put(fieldName, advancedTestTextField);
                                    hoardData.put(fieldName + "^ouid", fieldOuid);
                                    if(fieldType.byteValue() == 16 || fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                                    {
                                        selectCnt++;
                                        if(versionBoolean)
                                            selectButtonByType.setBounds(252, 35 + 23 * (i + 2), 18, 20);
                                        else
                                            selectButtonByType.setBounds(252, 35 + 23 * i, 18, 20);
                                        selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
                                        if(fieldType.byteValue() == 16)
                                            selectButtonByType.setActionCommand("ObjectSelectButtonClick_" + selectCnt + "_" + fieldName + "/" + (String)fieldData.get("type.ouid@class") + "/" + fieldName);
                                        else
                                        if(fieldType.byteValue() == 24)
                                            selectButtonByType.setActionCommand("CodeSelectButtonClick_" + selectCnt + "_" + fieldName + "/" + (String)fieldData.get("type.ouid@class") + "/" + fieldName);
                                        else
                                        if(fieldType.byteValue() == 25)
                                            selectButtonByType.setActionCommand("CodeReferenceSelectButtonClick_" + selectCnt + "_" + fieldName + "/" + fieldOuid + "/" + fieldName);
                                        selectButtonByType.addActionListener(this);
                                        selectButtonList.add(selectButtonByType.getActionCommand());
                                        add(selectButtonByType);
                                    }
                                    if(searchCondition != null)
                                        if(fieldType.byteValue() == 16)
                                        {
                                            String data = (String)searchCondition.get((String)fieldData.get("ouid"));
                                            if(!Utils.isNullString(data))
                                            {
                                                DOSChangeable instanceData = dos.get(data);
                                                if(instanceData != null)
                                                    advancedTestTextField.setText((String)instanceData.get("md$number"));
                                                else
                                                    advancedTestTextField.setText("");
                                            } else
                                            {
                                                advancedTestTextField.setText("");
                                            }
                                        } else
                                        if(fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                                        {
                                            String data = (String)searchCondition.get((String)fieldData.get("ouid"));
                                            if(!Utils.isNullString(data))
                                            {
                                                DOSChangeable codeItemData = dos.getCodeItem(data);
                                                if(codeItemData != null)
                                                    advancedTestTextField.setText((String)codeItemData.get("name") + " [" + (String)codeItemData.get("codeitemid") + "]");
                                                else
                                                    advancedTestTextField.setText("");
                                            } else
                                            {
                                                advancedTestTextField.setText("");
                                            }
                                        } else
                                        {
                                            advancedTestTextField.setText((String)searchCondition.get(fieldOuid));
                                        }
                                } else
                                {
                                    if(versionBoolean)
                                        advancedTestTextField.setBounds(110, 35 + 23 * (i + 2), 137, 20);
                                    else
                                        advancedTestTextField.setBounds(110, 35 + 23 * i, 137, 20);
                                    add(advancedTestTextField);
                                    hoardData.put(fieldName, advancedTestTextField);
                                    if(versionBoolean)
                                        dateSelectButton.setBounds(250, 35 + 23 * (i + 2), 20, 20);
                                    else
                                        dateSelectButton.setBounds(250, 35 + 23 * i, 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("DateChooserButtonClick_" + fieldName);
                                    dateSelectButton.addActionListener(this);
                                    dateChooserButtonList.add(fieldName);
                                    add(dateSelectButton);
                                    if(searchCondition != null)
                                        advancedTestTextField.setText((String)searchCondition.get(fieldOuid));
                                }
                            } else
                            {
                                JComboBox advancedComboBox;
                                if(fieldCode.equals("md$status"))
                                {
                                    advancedComboBox = new JComboBox(statusArray);
                                    if(searchCondition != null)
                                    {
                                        String values = DynaMOAD.getMSRString((String)searchCondition.get(fieldOuid), (String)searchCondition.get(fieldOuid), 0);
                                        advancedComboBox.setSelectedItem(values);
                                    }
                                } else
                                if(fieldOuid.equals("version.condition.type"))
                                {
                                    advancedComboBox = new JComboBox(versionType);
                                    advancedComboBox.setSelectedItem("Work In Progress");
                                    advancedComboBox.setActionCommand(fieldName);
                                    advancedComboBox.addActionListener(this);
                                    tmpBoolean = true;
                                    advancedTestLabel = new JLabel("From");
                                    advancedTestLabel.setBounds(10, 35 + 23 * (i + 1), 100, 20);
                                    add(advancedTestLabel);
                                    advancedTestLabel = new JLabel("To");
                                    advancedTestLabel.setBounds(10, 35 + 23 * (i + 2), 100, 20);
                                    add(advancedTestLabel);
                                    advancedTestTextField.setBounds(110, 35 + 23 * (i + 1), 137, 20);
                                    advancedTestTextField.setEnabled(false);
                                    advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                    add(advancedTestTextField);
                                    hoardData.put("Version Date From", advancedTestTextField);
                                    dateSelectButton.setEnabled(false);
                                    dateSelectButton.setBounds(250, 35 + 23 * (i + 1), 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date From");
                                    dateSelectButton.addActionListener(this);
                                    hoardData.put("VersionDateFromButton", dateSelectButton);
                                    dateChooserButtonList.add("Version Date From");
                                    add(dateSelectButton);
                                    if(searchCondition != null)
                                        advancedTestTextField.setText((String)searchCondition.get("version.condition.date.from"));
                                    advancedTestTextField = new JTextField();
                                    advancedTestTextField.setBackground(UIManagement.textBoxNotEditableBack);
                                    advancedTestTextField.setEnabled(false);
                                    advancedTestTextField.setBounds(110, 35 + 23 * (i + 2), 137, 20);
                                    add(advancedTestTextField);
                                    hoardData.put("Version Date To", advancedTestTextField);
                                    dateSelectButton = new JButton();
                                    dateSelectButton.setEnabled(false);
                                    dateSelectButton.setBounds(250, 35 + 23 * (i + 2), 20, 20);
                                    dateSelectButton.setIcon(new ImageIcon("icons/DateSelectButton.gif"));
                                    dateSelectButton.setActionCommand("DateChooserButtonClick_Version Date To");
                                    dateSelectButton.addActionListener(this);
                                    hoardData.put("VersionDateToButton", dateSelectButton);
                                    dateChooserButtonList.add("Version Date To");
                                    add(dateSelectButton);
                                    if(searchCondition != null)
                                        advancedTestTextField.setText((String)searchCondition.get("version.condition.date.to"));
                                    if(searchCondition != null)
                                    {
                                        String selectedStr = (String)searchCondition.get((String)fieldData.get("ouid"));
                                        if(selectedStr != null)
                                        {
                                            String values = "";
                                            if(selectedStr.equals("released"))
                                                values = "Last Released";
                                            else
                                            if(selectedStr.equals("wip"))
                                                values = "Work In Progress";
                                            else
                                            if(selectedStr.equals("all"))
                                                values = "All Version";
                                            else
                                            if(selectedStr.equals("cdate"))
                                                values = "Created Date";
                                            else
                                                values = "Modified Date";
                                            advancedComboBox.setSelectedItem(values);
                                        }
                                    }
                                } else
                                if(fieldType.byteValue() == 1)
                                {
                                    advancedComboBox = new JComboBox(trueOrFalse);
                                    if(searchCondition != null)
                                    {
                                        Boolean selectedStr = (Boolean)searchCondition.get(fieldOuid);
                                        if(selectedStr != null)
                                        {
                                            String values = "";
                                            if(selectedStr.booleanValue())
                                                values = "true";
                                            else
                                                values = "false";
                                            advancedComboBox.setSelectedItem(values);
                                        }
                                    }
                                } else
                                {
                                    advancedComboBox = new JComboBox();
                                }
                                if(versionBoolean)
                                    advancedComboBox.setBounds(110, 35 + 23 * (i + 2), 160, 20);
                                else
                                    advancedComboBox.setBounds(110, 35 + 23 * i, 160, 20);
                                add(advancedComboBox);
                                hoardData.put(fieldName, advancedComboBox);
                                if(tmpBoolean)
                                    versionBoolean = true;
                            }
                        }
                    }
                }

                i++;
                countPerPageLabel = new JLabel(DynaMOAD.getMSRString("WRD_0161", "Rows Restriction", 3));
                if(versionBoolean)
                    countPerPageLabel.setBounds(10, 35 + 23 * (i + 2), 100, 20);
                else
                    countPerPageLabel.setBounds(10, 35 + 23 * i, 100, 20);
                add(countPerPageLabel);
                countPerPageTextField = new JTextField();
                if(versionBoolean)
                    countPerPageTextField.setBounds(110, 35 + 23 * (i + 2), 160, 20);
                else
                    countPerPageTextField.setBounds(110, 35 + 23 * i, 160, 20);
                add(countPerPageTextField);
                if(searchCondition != null)
                {
                    String data = (String)searchCondition.get("search.result.count");
                    if(!Utils.isNullString(data))
                    {
                        countPerPageTextField.setText(data);
                        countPerPage = Integer.parseInt(data);
                    } else
                    {
                        countPerPageTextField.setText("");
                        countPerPage = 1000;
                    }
                }
                if(versionBoolean)
                    y_pos = 35 + 23 * (i + 4);
                else
                    y_pos = 35 + 23 * (i + 2);
            }
            setLayout(null);
            setBackground(UIManagement.panelBackGround);
            setMinimumSize(new Dimension(270, y_pos));
            setMaximumSize(new Dimension(270, y_pos));
            setPreferredSize(new Dimension(270, y_pos));
            if(searchCondition == null)
                searchCondition = new HashMap();
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public HashMap getCondition()
    {
        if(Utils.isNullArrayList(fieldList))
            return null;
        if(searchConditionMap == null)
        {
            if(searchCondition != null)
            {
                if(!Utils.isNullString(countPerPageTextField.getText()))
                {
                    searchCondition.put("search.result.count", countPerPageTextField.getText());
                    countPerPage = Integer.parseInt(countPerPageTextField.getText());
                } else
                {
                    searchCondition.put("search.result.count", "1000");
                    countPerPage = 1000;
                }
                for(int i = 0; i < fieldList.size(); i++)
                {
                    DOSChangeable fieldData = (DOSChangeable)fieldList.get(i);
                    if(fieldData == null || fieldData.getValueMap() == null || fieldData.getValueMap().size() == 0)
                    {
                        fieldData = null;
                        continue;
                    }
                    if(!Utils.getBoolean((Boolean)fieldData.get("is.visible")))
                    {
                        fieldData = null;
                        continue;
                    }
                    String text;
                    if(!"md$status".equals(fieldData.get("code")) && !((String)fieldData.get("ouid")).equals("version.condition.type"))
                    {
                        getClass();
                        if(((Byte)fieldData.get("type")).byteValue() != 1)
                        {
                            text = (String)fieldData.get("name");
                            if(text != null && (JTextField)hoardData.get(text) != null)
                            {
                                text = ((JTextField)hoardData.get(text)).getText();
                                if(Utils.isNullString(text))
                                    searchCondition.remove((String)fieldData.get("ouid"));
                                else
                                if(((String)fieldData.get("ouid")).equals("md$number") && !text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), text);
                                else
                                if(((String)fieldData.get("ouid")).equals("md$cdate") && !text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), text);
                                else
                                if(((String)fieldData.get("ouid")).equals("md$mdate") && !text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), text);
                                else
                                if(((String)fieldData.get("ouid")).equals("md$user") && !text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), text);
                                else
                                if((((Byte)fieldData.get("type")).byteValue() != 16 && ((Byte)fieldData.get("type")).byteValue() != 24 && ((Byte)fieldData.get("type")).byteValue() != 25 || text.equals("")) && !text.equals(""))
                                    searchCondition.put((String)fieldData.get("ouid"), text);
                            }
                            continue;
                        }
                    }
                    text = (String)fieldData.get("name");
                    if(text != null && (JComboBox)hoardData.get(text) != null)
                    {
                        text = (String)((JComboBox)hoardData.get(text)).getSelectedItem();
                        if(Utils.isNullString(text))
                            searchCondition.remove((String)fieldData.get("ouid"));
                        else
                        if(fieldData.get("type").toString().equals("1"))
                        {
                            if(text.equals("true"))
                                searchCondition.put((String)fieldData.get("ouid"), new Boolean(true));
                            else
                                searchCondition.put((String)fieldData.get("ouid"), new Boolean(false));
                        } else
                        if("md$status".equals(fieldData.get("code")))
                            searchCondition.put((String)fieldData.get("ouid"), statusValueMap.get(text));
                        else
                        if(text.equals("Last Released"))
                            searchCondition.put((String)fieldData.get("ouid"), "released");
                        else
                        if(text.equals("Work In Progress"))
                            searchCondition.put((String)fieldData.get("ouid"), "wip");
                        else
                        if(text.equals("All Version"))
                            searchCondition.put((String)fieldData.get("ouid"), "all");
                        else
                        if(text.equals("Created Date"))
                        {
                            searchCondition.put((String)fieldData.get("ouid"), "cdate");
                            searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                            searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                        } else
                        if(text.equals("Modified Date"))
                        {
                            searchCondition.put((String)fieldData.get("ouid"), "mdate");
                            searchCondition.put("version.condition.date.from", ((JTextField)hoardData.get("Version Date From")).getText());
                            searchCondition.put("version.condition.date.to", ((JTextField)hoardData.get("Version Date To")).getText());
                        }
                    }
                }

                if(versionableClass && searchCondition.size() > 0 && !searchCondition.containsKey("version.condition.type"))
                    searchCondition.put("version.condition.type", "wip");
            }
            return searchCondition;
        }
        if(versionableClass && searchConditionMap.size() > 0 && !searchConditionMap.containsKey("version.condition.type"))
            searchConditionMap.put("version.condition.type", "wip");
        if(!searchConditionMap.containsKey("search.result.count") || Utils.isNullString((String)searchConditionMap.get("search.result.count")))
        {
            searchConditionMap.put("search.result.count", "1000");
            countPerPage = 1000;
        } else
        {
            countPerPage = Integer.parseInt((String)searchConditionMap.get("search.result.count"));
        }
        return searchConditionMap;
    }

    public void setCondition(HashMap hashmap)
    {
    }

    private void initStatus()
    {
        statusArray = new String[12];
        statusArray[0] = "";
        statusArray[1] = DynaMOAD.getMSRString("AP1", "approving", 0);
        statusArray[2] = DynaMOAD.getMSRString("AP2", "approved", 0);
        statusArray[3] = DynaMOAD.getMSRString("CKI", "checked-in", 0);
        statusArray[4] = DynaMOAD.getMSRString("CKO", "checked-out", 0);
        statusArray[5] = DynaMOAD.getMSRString("CRT", "created", 0);
        statusArray[6] = DynaMOAD.getMSRString("OBS", "obsoleted", 0);
        statusArray[7] = DynaMOAD.getMSRString("REJ", "rejected", 0);
        statusArray[8] = DynaMOAD.getMSRString("RLS", "released", 0);
        statusArray[9] = DynaMOAD.getMSRString("RV1", "reviewing", 0);
        statusArray[10] = DynaMOAD.getMSRString("RV2", "reviewed", 0);
        statusArray[11] = DynaMOAD.getMSRString("WIP", "work in progress", 0);
        statusCodeMap = new HashMap();
        statusCodeMap.put("AP1", statusArray[1]);
        statusCodeMap.put("AP2", statusArray[2]);
        statusCodeMap.put("CKI", statusArray[3]);
        statusCodeMap.put("CKO", statusArray[4]);
        statusCodeMap.put("CRT", statusArray[5]);
        statusCodeMap.put("OBS", statusArray[6]);
        statusCodeMap.put("REJ", statusArray[7]);
        statusCodeMap.put("RLS", statusArray[8]);
        statusCodeMap.put("RV1", statusArray[9]);
        statusCodeMap.put("RV2", statusArray[10]);
        statusCodeMap.put("WIP", statusArray[11]);
        statusValueMap = new HashMap();
        statusValueMap.put(statusArray[1], "AP1");
        statusValueMap.put(statusArray[2], "AP2");
        statusValueMap.put(statusArray[3], "CKI");
        statusValueMap.put(statusArray[4], "CKO");
        statusValueMap.put(statusArray[5], "CRT");
        statusValueMap.put(statusArray[6], "OBS");
        statusValueMap.put(statusArray[7], "REJ");
        statusValueMap.put(statusArray[8], "RLS");
        statusValueMap.put(statusArray[9], "RV1");
        statusValueMap.put(statusArray[10], "RV2");
        statusValueMap.put(statusArray[11], "WIP");
    }

    public void makeFilterComboBox()
    {
        try
        {
            String tmpClassOuid = null;
            ArrayList classList = null;
            ArrayList pubFieldColumn = null;
            ArrayList fieldColumn = null;
            HashMap itemMap = null;
            filterList = new ArrayList();
            classList = dos.listAllSuperClassOuid(classOuid);
            if(classList == null)
                classList = new ArrayList();
            classList.add(0, classOuid);
            for(int i = 0; i < classList.size(); i++)
            {
                tmpClassOuid = (String)classList.get(i);
                pubFieldColumn = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + tmpClassOuid);
                fieldColumn = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + tmpClassOuid);
                if(pubFieldColumn != null)
                {
                    for(int j = 0; j < pubFieldColumn.size(); j++)
                    {
                        itemMap = new HashMap();
                        itemMap.put("name", (String)pubFieldColumn.get(j));
                        itemMap.put("option", "PUBLIC");
                        itemMap.put("classouid", tmpClassOuid);
                        if(!filterList.contains(itemMap))
                            filterList.add(itemMap.clone());
                        itemMap.clear();
                        itemMap = null;
                    }

                }
                if(fieldColumn != null)
                {
                    for(int j = 0; j < fieldColumn.size(); j++)
                    {
                        itemMap = new HashMap();
                        itemMap.put("name", (String)fieldColumn.get(j));
                        itemMap.put("option", "PRIVATE");
                        itemMap.put("classouid", tmpClassOuid);
                        if(!filterList.contains(itemMap))
                            filterList.add(itemMap.clone());
                        itemMap.clear();
                        itemMap = null;
                    }

                }
            }

            if(filterList.size() < 1)
            {
                listFilterComboBox = new JComboBox();
            } else
            {
                String filterArray[] = new String[filterList.size() + 1];
                filterArray[0] = "";
                for(int j = 0; j < filterList.size(); j++)
                {
                    itemMap = (HashMap)filterList.get(j);
                    filterArray[j + 1] = itemMap.get("name") + " [" + itemMap.get("option") + "]";
                }

                listFilterComboBox = new JComboBox(filterArray);
            }
            listFilterComboBox.setActionCommand("List Combo");
            listFilterComboBox.addActionListener(this);
            searchConditionMap = null;
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        try
        {
            if(hoardData.get(sign) instanceof JTextField)
            {
                String fieldOuid = (String)hoardData.get(sign + "^ouid");
                DOSChangeable fieldData = null;
                if(fieldOuid != null)
                {
                    if(searchCondition == null)
                        searchCondition = new HashMap();
                    for(int i = 0; i < fieldList.size(); i++)
                    {
                        fieldData = (DOSChangeable)fieldList.get(i);
                        if(!fieldData.get("ouid").equals(fieldOuid))
                            continue;
                        if(((Byte)fieldData.get("type")).byteValue() == 16)
                        {
                            DOSChangeable dosObject = dos.get(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText((String)dosObject.get("md$number"));
                            searchCondition.put(fieldOuid, (String)dosObject.get("ouid"));
                        } else
                        if(((Byte)fieldData.get("type")).byteValue() == 24 || ((Byte)fieldData.get("type")).byteValue() == 25)
                        {
                            DOSChangeable dosCodeItem = dos.getCodeItem(selectOuid);
                            ((JTextField)hoardData.get(sign)).setText(dosCodeItem.get("name") + " [" + dosCodeItem.get("codeitemid") + "]");
                            searchCondition.put(fieldOuid, (String)dosCodeItem.get("ouid"));
                        } else
                        {
                            ((JTextField)hoardData.get(sign)).setText(selectOuid);
                            searchCondition.put(fieldOuid, selectOuid);
                        }
                        break;
                    }

                } else
                {
                    ((JTextField)hoardData.get(sign)).setText(selectOuid);
                }
            } else
            if(hoardData.get(sign) instanceof JComboBox)
                if(sign.equals("md$status"))
                {
                    String values = DynaMOAD.getMSRString(selectOuid, selectOuid, 0);
                    ((JComboBox)hoardData.get(sign)).setSelectedItem(values);
                } else
                if(sign.equals("Version Type"))
                {
                    if(selectOuid != null)
                    {
                        String values = "";
                        if(selectOuid.equals("released"))
                            values = "Last Released";
                        else
                        if(selectOuid.equals("wip"))
                            values = "Work In Progress";
                        else
                        if(selectOuid.equals("all"))
                            values = "All Version";
                        else
                        if(selectOuid.equals("cdate"))
                            values = "Created Date";
                        else
                            values = "Modified Date";
                        ((JComboBox)hoardData.get(sign)).setSelectedItem(values);
                    }
                } else
                {
                    ((JComboBox)hoardData.get(sign)).setSelectedItem(selectOuid);
                }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    private void clearFieldValue()
    {
        if(fieldList != null)
        {
            for(int i = 0; i < fieldList.size(); i++)
            {
                DOSChangeable fieldInfo = (DOSChangeable)fieldList.get(i);
                if(fieldInfo != null)
                {
                    String title = (String)fieldInfo.get("name");
                    if(hoardData.get(title) instanceof JTextField)
                        ((JTextField)hoardData.get(title)).setText("");
                    else
                    if(hoardData.get(title) instanceof JComboBox)
                        ((JComboBox)hoardData.get(title)).setSelectedIndex(-1);
                    else
                        System.out.println(title + " / " + hoardData.get(title));
                }
            }

            if(hoardData.get("Version Date From") != null)
                ((JTextField)hoardData.get("Version Date From")).setText("");
            if(hoardData.get("Version Date To") != null)
                ((JTextField)hoardData.get("Version Date To")).setText("");
            countPerPageTextField.setText("");
            countPerPage = 1000;
        }
    }

    public void clearAllFieldValue()
    {
        if(fieldList != null)
        {
            listFilterComboBox.setSelectedIndex(-1);
            clearFieldValue();
            searchConditionMap = null;
        }
    }

    public int getCountPerPage()
    {
        return countPerPage;
    }

    public void setFilterInLink(HashMap searchConditionMap)
    {
        this.searchConditionMap = searchConditionMap;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("List Combo"))
            try
            {
                clearFieldValue();
                HashMap tmpMap = new HashMap();
                int index = listFilterComboBox.getSelectedIndex();
                if(index < 1)
                {
                    searchConditionMap = null;
                } else
                {
                    HashMap selectedItemMap = (HashMap)filterList.get(index - 1);
                    if(((String)selectedItemMap.get("option")).equals("PUBLIC"))
                    {
                        ArrayList filterField = nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name"));
                        for(int i = 0; i < filterField.size(); i++)
                        {
                            String filterID = (String)filterField.get(i);
                            String value = nds.getValue("::/WORKSPACE/" + modelOuid + "/PUBLIC/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name") + "/" + filterID);
                            tmpMap.put(filterID, value);
                            searchConditionMap = tmpMap;
                        }

                    } else
                    if(((String)selectedItemMap.get("option")).equals("PRIVATE"))
                    {
                        ArrayList filterField = nds.getChildNodeList("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name"));
                        for(int i = 0; i < filterField.size(); i++)
                        {
                            String filterID = (String)filterField.get(i);
                            String value = nds.getValue("::/WORKSPACE/" + LogIn.userID + "/LISTFILTER/" + (String)selectedItemMap.get("classouid") + "/" + (String)selectedItemMap.get("name") + "/" + filterID);
                            tmpMap.put(filterID, value);
                            searchConditionMap = tmpMap;
                        }

                    }
                }
                if(index > 0)
                    if(parent instanceof SearchCondition)
                    {
                        ((SearchCondition)parent).setPageNumber(1);
                        ((SearchCondition)parent).searchButton_actionPerformed(e);
                    } else
                    if(parent instanceof FieldLinkSelect)
                    {
                        ((FieldLinkSelect)parent).setPageNumber(1);
                        ((FieldLinkSelect)parent).searchButton_actionPerformed(e);
                    }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("Version Type"))
        {
            if(hoardData.get("Version Type") == null || ((JComboBox)hoardData.get("Version Type")).getSelectedItem() == null)
                return;
            if(((String)((JComboBox)hoardData.get("Version Type")).getSelectedItem()).equals("Created Date") || ((String)((JComboBox)hoardData.get("Version Type")).getSelectedItem()).equals("Modified Date"))
            {
                ((JTextField)hoardData.get("Version Date From")).setEnabled(true);
                ((JTextField)hoardData.get("Version Date From")).setBackground(Color.white);
                ((JTextField)hoardData.get("Version Date To")).setEnabled(true);
                ((JTextField)hoardData.get("Version Date To")).setBackground(Color.white);
                ((JButton)hoardData.get("VersionDateFromButton")).setEnabled(true);
                ((JButton)hoardData.get("VersionDateToButton")).setEnabled(true);
            } else
            {
                ((JTextField)hoardData.get("Version Date From")).setEnabled(false);
                ((JTextField)hoardData.get("Version Date From")).setBackground(UIManagement.textBoxNotEditableBack);
                ((JTextField)hoardData.get("Version Date To")).setEnabled(false);
                ((JTextField)hoardData.get("Version Date To")).setBackground(UIManagement.textBoxNotEditableBack);
                ((JButton)hoardData.get("VersionDateFromButton")).setEnabled(false);
                ((JButton)hoardData.get("VersionDateToButton")).setEnabled(false);
            }
        } else
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
                        if(container == null)
                            for(container = getParent(); !(container instanceof Frame) && !(container instanceof Dialog); container = container.getParent());
                        String values[] = (String[])null;
                        if(container instanceof Frame)
                            values = FieldLinkSelect.showDialog((Frame)container, (Component)e.getSource(), classOuid2, command);
                        else
                        if(container instanceof Dialog)
                            values = FieldLinkSelect.showDialog((Dialog)container, (Component)e.getSource(), classOuid2, command);
                        if(values != null)
                        {
                            field.setText(values[1]);
                            field.setCaretPosition(0);
                            searchCondition.put((String)hoardData.get(mapId + "^ouid"), values[0]);
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
                    CodeSelectDialog newSmall = null;
                    if(codeOuid != null && !codeOuid.equals("null"))
                    {
                        if(container == null)
                            for(container = getParent(); !(container instanceof Frame) && !(container instanceof Dialog); container = container.getParent());
                        String values[] = (String[])null;
                        if(container instanceof Frame)
                            values = CodeSelectDialog.showDialog((Frame)container, (Component)e.getSource(), true, codeOuid, command);
                        else
                        if(container instanceof Dialog)
                            values = CodeSelectDialog.showDialog((Dialog)container, (Component)e.getSource(), true, codeOuid, command);
                        if(values != null)
                        {
                            field.setText(values[2]);
                            field.setCaretPosition(0);
                            searchCondition.put((String)hoardData.get(mapId + "^ouid"), values[1]);
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
                    java.util.List testList = Utils.tokenizeMessage((String)selectButtonList.get(i), '/');
                    String codeOuid = (String)testList.get(1);
                    String mapId = (String)testList.get(2);
                    JTextField field = (JTextField)hoardData.get(mapId);
                    if(Utils.isNullString(codeOuid))
                        return;
                    if(Utils.isNullString(codeOuid))
                        return;
                    try
                    {
                        DOSChangeable instanceData = new DOSChangeable();
                        if(searchCondition != null)
                            instanceData.setValueMap(searchCondition);
                        codeOuid = DynaMOAD.dos.lookupCodeFromSelectionTable(codeOuid, instanceData);
                        if(Utils.isNullString(codeOuid))
                            return;
                        if(container == null)
                            for(container = getParent(); !(container instanceof Frame) && !(container instanceof Dialog); container = container.getParent());
                        String values[] = (String[])null;
                        if(container instanceof Frame)
                            values = CodeSelectDialog.showDialog((Frame)container, (Component)e.getSource(), true, codeOuid, command);
                        else
                        if(container instanceof Dialog)
                            values = CodeSelectDialog.showDialog((Dialog)container, (Component)e.getSource(), true, codeOuid, command);
                        if(values != null)
                        {
                            field.setText(values[2]);
                            field.setCaretPosition(0);
                            searchCondition.put((String)hoardData.get(mapId + "^ouid"), values[1]);
                        }
                    }
                    catch(Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }

        } else
        if(command.startsWith("DateChooserButtonClick"))
        {
            int tmpInt = -1;
            for(int i = 0; i < dateChooserButtonList.size(); i++)
            {
                if(!command.endsWith((String)dateChooserButtonList.get(i)))
                    continue;
                tmpInt = i;
                break;
            }

            if(tmpInt > -1)
            {
                String fieldName = (String)dateChooserButtonList.get(tmpInt);
                if(fieldName != null)
                {
                    if(container == null)
                        for(container = getParent(); !(container instanceof Frame) && !(container instanceof Dialog); container = container.getParent());
                    if(container instanceof Frame)
                        new DynaDateChooser((Frame)container);
                    else
                    if(container instanceof Dialog)
                        new DynaDateChooser((Dialog)container);
                    java.util.Date date = DynaDateChooser.showDialog((Component)e.getSource(), null);
                    if(date != null)
                        setFieldInDateField(sdfYMD.format(date), fieldName);
                    else
                        setFieldInDateField("", fieldName);
                } else
                {
                    System.out.println("type class ouid is Null");
                }
            }
        }
    }

    private final DOS dos;
    private final NDS nds;
    private Container container;
    private JLabel listFilterLabel;
    private JComboBox listFilterComboBox;
    private JLabel countPerPageLabel;
    private JTextField countPerPageTextField;
    private Object parent;
    private final String modelOuid;
    private final String classOuid;
    private DOSChangeable classInfo;
    private boolean versionableClass;
    private HashMap searchConditionMap;
    private DOSChangeable hoardData;
    private ArrayList filterList;
    private ArrayList fieldList;
    private HashMap searchCondition;
    private ArrayList dateChooserButtonList;
    private ArrayList selectButtonList;
    private String statusArray[];
    private HashMap statusCodeMap;
    private HashMap statusValueMap;
    private int selectCnt;
    private int countPerPage;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private final int init_xcord = 10;
    private final int init_ycord = 35;
    private final int titleWidth = 100;
    private final int totalWidth = 260;
    private final int fieldHeight = 20;
    private final int dateButtonWidth = 20;
    private final int offset = 3;
    private final int condition_xsize = 270;
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_BOOLEAN = 1;

}