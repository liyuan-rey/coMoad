// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FieldGroupInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, TypeClassSelection

public class FieldGroupInformation extends JPanel
    implements ActionListener, MouseListener
{
    private class LocationDataLoader extends DynaComboBoxDataLoader
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
            ArrayList tmpList = null;
            tmpList = new ArrayList();
            tmpList.add(new Integer(1));
            tmpList.add(new String("Top"));
            arrayList.add(tmpList);
            tmpList = null;
            tmpList = new ArrayList();
            tmpList.add(new Integer(2));
            tmpList.add(new String("Center"));
            arrayList.add(tmpList);
            tmpList = null;
            tmpList = new ArrayList();
            tmpList.add(new Integer(3));
            tmpList.add(new String("Left"));
            arrayList.add(tmpList);
            tmpList = null;
            tmpList = new ArrayList();
            tmpList.add(new Integer(4));
            tmpList.add(new String("Bottom"));
            arrayList.add(tmpList);
            tmpList = null;
            tmpList = new ArrayList();
            tmpList.add(new Integer(100));
            tmpList.add(new String("Pop-up Menu"));
            arrayList.add(tmpList);
            tmpList = null;
            tmpList = new ArrayList();
            tmpList.add(new Integer(110));
            tmpList.add(new String("Search Default"));
            arrayList.add(tmpList);
            tmpList = null;
            return arrayList;
        }

        ArrayList arrayList;

        LocationDataLoader()
        {
            arrayList = null;
            arrayList = new ArrayList();
        }
    }


    public FieldGroupInformation(Object parentFrame, String classOuid)
    {
        dos = null;
        this.classOuid = "";
        actionListData = new ArrayList();
        actionColumnNames = new ArrayList();
        actionColumnWidths = new ArrayList();
        actionOuidList = new ArrayList();
        fieldListData = new ArrayList();
        fieldColumnNames = new ArrayList();
        fieldColumnWidths = new ArrayList();
        fieldOuidList = new ArrayList();
        inheritedFieldListData = new ArrayList();
        inheritedFieldColumnNames = new ArrayList();
        inheritedFieldColumnWidths = new ArrayList();
        inheritedFieldOuidList = new ArrayList();
        locationDataLoader = new LocationDataLoader();
        locationModel = new DynaComboBoxModel(locationDataLoader);
        setInfoDC = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            this.classOuid = classOuid;
            parent = parentFrame;
            makeActionListTable();
            makeFieldListTable();
            makeInheritedFieldListTable();
            initialize();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void initialize()
    {
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel = new JPanel();
        fieldGroupTabbedPane = new JTabbedPane();
        fieldGroupTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        mainToolBar = new JToolBar();
        mainBoxLayout = new BoxLayout(mainToolBar, 0);
        mainToolBar.setLayout(mainBoxLayout);
        mainToolBar.setBorder(BorderFactory.createEtchedBorder());
        addFieldButton = new JButton();
        addFieldButton.setEnabled(true);
        addFieldButton.setText("Add Field");
        addFieldButton.setToolTipText("addField");
        addFieldButton.setActionCommand("addField");
        addFieldButton.setMargin(new Insets(0, 0, 0, 0));
        addFieldButton.setIcon(new ImageIcon(getClass().getResource("/icons/Field.gif")));
        addFieldButton.addActionListener(this);
        addActionButton = new JButton();
        addActionButton.setEnabled(true);
        addActionButton.setText("Add Action");
        addActionButton.setToolTipText("addAction");
        addActionButton.setActionCommand("addAction");
        addActionButton.setMargin(new Insets(0, 0, 0, 0));
        addActionButton.setIcon(new ImageIcon(getClass().getResource("/icons/Action.gif")));
        addActionButton.addActionListener(this);
        removeButton = new JButton();
        removeButton.setEnabled(true);
        removeButton.setToolTipText("delete");
        removeButton.setActionCommand("delete");
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        removeButton.addActionListener(this);
        mainToolBar.add(addFieldButton);
        mainToolBar.add(addActionButton);
        mainToolBar.add(removeButton);
        actionListTableScrPane = UIFactory.createStrippedScrollPane(null);
        fieldListTableScrPane = UIFactory.createStrippedScrollPane(null);
        inheritedFieldListTableScrPane = UIFactory.createStrippedScrollPane(null);
        packageInfoBorderLayout = new BorderLayout();
        setLayout(packageInfoBorderLayout);
        GridBagLayout gridBag1 = new GridBagLayout();
        GridBagConstraints gridBagCon1 = new GridBagConstraints();
        gridBagCon1.fill = 1;
        gridBagCon1.anchor = 11;
        descPanel = new JPanel();
        descPanel.setLayout(gridBag1);
        descPanel.setBorder(BorderFactory.createEmptyBorder());
        descriptionLabel = new JLabel("Description");
        gridBagCon1.insets = new Insets(0, 5, 0, 15);
        gridBagCon1.weightx = 0.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 0;
        gridBagCon1.gridwidth = 1;
        gridBag1.setConstraints(descriptionLabel, gridBagCon1);
        descScrPane = new JScrollPane();
        descriptionTextArea = new JTextArea();
        gridBagCon1.insets = new Insets(0, 40, 0, 5);
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 0.10000000000000001D;
        gridBagCon1.gridx = 1;
        gridBagCon1.gridy = 0;
        gridBagCon1.gridwidth = 2;
        gridBagCon1.gridheight = 2;
        gridBag1.setConstraints(descScrPane, gridBagCon1);
        descScrPane.getViewport().add(descriptionTextArea, null);
        descPanel.add(descriptionLabel, null);
        descPanel.add(descScrPane, null);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        mainPanel.setLayout(gridBag);
        ouidTextField = new DynaTextField();
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setTitleWidth(120);
        ouidTextField.setTitleVisible(true);
        ouidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(ouidTextField, gridBagCon);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(true);
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(120);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(nameTextField, gridBagCon);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Description");
        descriptionTextField.setTitleWidth(120);
        descriptionTextField.setTitleVisible(true);
        descriptionTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.10000000000000001D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(descPanel, gridBagCon);
        classOuidTextField = new DynaTextField();
        classOuidTextField.setMandatory(true);
        classOuidTextField.setTitleText("Class ouid");
        classOuidTextField.setTitleWidth(120);
        classOuidTextField.setTitleVisible(true);
        classOuidTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(classOuidTextField, gridBagCon);
        indexTextField = new DynaTextField();
        indexTextField.setMandatory(false);
        indexTextField.setTitleText("Index");
        indexTextField.setTitleWidth(120);
        indexTextField.setTitleVisible(true);
        indexTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(indexTextField, gridBagCon);
        isMandatoryCheckBox = new JCheckBox("Mandatory");
        isMandatoryCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(isMandatoryCheckBox, gridBagCon);
        titleTextField = new DynaTextField();
        titleTextField.setMandatory(false);
        titleTextField.setTitleText("Title");
        titleTextField.setTitleWidth(120);
        titleTextField.setTitleVisible(true);
        titleTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 7;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(titleTextField, gridBagCon);
        tooltipTextField = new DynaTextField();
        tooltipTextField.setMandatory(false);
        tooltipTextField.setTitleText("Tooltip");
        tooltipTextField.setTitleWidth(120);
        tooltipTextField.setTitleVisible(true);
        tooltipTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 8;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(tooltipTextField, gridBagCon);
        isVisibleCheckBox = new JCheckBox("Visible");
        isVisibleCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 9;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(isVisibleCheckBox, gridBagCon);
        layoutTextField = new DynaTextField();
        layoutTextField.setMandatory(false);
        layoutTextField.setTitleText("Layout");
        layoutTextField.setTitleWidth(120);
        layoutTextField.setTitleVisible(true);
        layoutTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 10;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(layoutTextField, gridBagCon);
        iconTextField = new DynaTextField();
        iconTextField.setMandatory(false);
        iconTextField.setTitleText("Icon");
        iconTextField.setTitleWidth(120);
        iconTextField.setTitleVisible(true);
        iconTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 11;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(iconTextField, gridBagCon);
        locationComboBox = new DynaComboBox();
        locationComboBox.setTitleText("Location");
        locationComboBox.setTitleWidth(120);
        locationComboBox.setTitleVisible(true);
        locationComboBox.setModel(locationModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 12;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(locationComboBox, gridBagCon);
        dummyLabel = new JLabel("");
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 13;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(descPanel);
        mainPanel.add(indexTextField);
        mainPanel.add(isMandatoryCheckBox);
        mainPanel.add(titleTextField);
        mainPanel.add(tooltipTextField);
        mainPanel.add(isVisibleCheckBox);
        mainPanel.add(layoutTextField);
        mainPanel.add(iconTextField);
        mainPanel.add(locationComboBox);
        mainPanel.add(dummyLabel);
        mainScrPane.getViewport().add(mainPanel, null);
        fieldGroupTabbedPane.addTab("Field Group", null, mainScrPane, "Field Group Information");
        fieldListTableScrPane.setViewportView(fieldListTable.getTable());
        fieldListTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        fieldGroupTabbedPane.addTab("Field List", null, fieldListTableScrPane, "Field List");
        inheritedFieldListTableScrPane.setViewportView(inheritedFieldListTable.getTable());
        inheritedFieldListTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        fieldGroupTabbedPane.addTab("Inherited Field List", null, inheritedFieldListTableScrPane, "Inherited Field List");
        actionListTableScrPane.setViewportView(actionListTable.getTable());
        actionListTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        fieldGroupTabbedPane.addTab("Action List", null, actionListTableScrPane, "Action List");
        add(mainToolBar, "North");
        add(fieldGroupTabbedPane, "Center");
        locationModel.enableDataLoad();
    }

    public void makeActionListTable()
    {
        actionColumnNames.add("Ouid");
        actionColumnNames.add("Name");
        actionColumnNames.add("Description");
        actionColumnWidths.add(new Integer(80));
        actionColumnWidths.add(new Integer(80));
        actionColumnWidths.add(new Integer(80));
        actionListTable = new Table(actionListData, (ArrayList)actionColumnNames.clone(), (ArrayList)actionColumnWidths.clone(), 0, 240);
        actionListTable.getTable().addMouseListener(this);
        actionListTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        actionListTable.setIndexColumn(0);
    }

    public void makeFieldListTable()
    {
        fieldColumnNames.add("Ouid");
        fieldColumnNames.add("Name");
        fieldColumnNames.add("Description");
        fieldColumnWidths.add(new Integer(80));
        fieldColumnWidths.add(new Integer(80));
        fieldColumnWidths.add(new Integer(80));
        fieldListTable = new Table(fieldListData, (ArrayList)fieldColumnNames.clone(), (ArrayList)fieldColumnWidths.clone(), 0, 240);
        fieldListTable.getTable().addMouseListener(this);
        fieldListTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        fieldListTable.setIndexColumn(0);
    }

    public void makeInheritedFieldListTable()
    {
        inheritedFieldColumnNames.add("Ouid");
        inheritedFieldColumnNames.add("Name");
        inheritedFieldColumnNames.add("Description");
        inheritedFieldColumnNames.add("Used");
        inheritedFieldColumnWidths.add(new Integer(80));
        inheritedFieldColumnWidths.add(new Integer(80));
        inheritedFieldColumnWidths.add(new Integer(80));
        inheritedFieldColumnWidths.add(new Integer(80));
        inheritedFieldListTable = new Table(inheritedFieldListData, (ArrayList)inheritedFieldColumnNames.clone(), (ArrayList)inheritedFieldColumnWidths.clone(), 0, 240);
        inheritedFieldListTable.getTable().addMouseListener(this);
        inheritedFieldListTable.setColumnSequence(new int[] {
            0, 1, 2, 3
        });
        inheritedFieldListTable.setEditable(true);
        inheritedFieldListTable.setUneditableColumns(new int[] {
            0, 1, 2
        });
        inheritedFieldListTable.setIndexColumn(0);
    }

    public void setFieldGroupInfoField(DOSChangeable dosFieldGroupInfo)
    {
        setInfoDC = dosFieldGroupInfo;
        ouidTextField.setText((String)dosFieldGroupInfo.get("ouid"));
        nameTextField.setText((String)dosFieldGroupInfo.get("name"));
        descriptionTextArea.setText((String)dosFieldGroupInfo.get("description"));
        if(dosFieldGroupInfo.get("is.mandatory") != null)
            isMandatoryCheckBox.setSelected(Utils.getBoolean((Boolean)dosFieldGroupInfo.get("is.mandatory")));
        else
            isMandatoryCheckBox.setSelected(false);
        if(dosFieldGroupInfo.get("index") != null)
            indexTextField.setText(dosFieldGroupInfo.get("index").toString());
        titleTextField.setText((String)dosFieldGroupInfo.get("title"));
        tooltipTextField.setText((String)dosFieldGroupInfo.get("tooltip"));
        if(dosFieldGroupInfo.get("is.visible") != null)
            isVisibleCheckBox.setSelected(Utils.getBoolean((Boolean)dosFieldGroupInfo.get("is.visible")));
        else
            isVisibleCheckBox.setSelected(false);
        iconTextField.setText((String)dosFieldGroupInfo.get("icon"));
        setInheritedFieldListTable((ArrayList)dosFieldGroupInfo.get("array$ouid@inherited.field"));
        setFieldListTable((ArrayList)dosFieldGroupInfo.get("array$ouid@field"));
        setActionListTable((ArrayList)dosFieldGroupInfo.get("array$ouid@action"));
        locationModel.setSelectedItemByOID(dosFieldGroupInfo.get("location"));
    }

    public void setFieldEnabled(boolean isEnable)
    {
        nameTextField.setEditable(isEnable);
        descriptionTextArea.setEditable(isEnable);
        classOuidTextField.setEditable(isEnable);
        indexTextField.setEditable(isEnable);
        isMandatoryCheckBox.setEnabled(isEnable);
        titleTextField.setEditable(isEnable);
        tooltipTextField.setEditable(isEnable);
        isVisibleCheckBox.setEnabled(isEnable);
        layoutTextField.setEditable(isEnable);
        iconTextField.setEditable(isEnable);
        addFieldButton.setEnabled(isEnable);
        addActionButton.setEnabled(isEnable);
        removeButton.setEnabled(isEnable);
        locationComboBox.setEnabled(isEnable);
    }

    public void setInformation()
    {
        try
        {
            setInfoDC.put("ouid", ouidTextField.getText());
            setInfoDC.put("name", nameTextField.getText());
            setInfoDC.put("description", descriptionTextArea.getText());
            setInfoDC.put("ouid@class", classOuid);
            setInfoDC.put("index", Utils.getInteger(indexTextField.getText()));
            setInfoDC.put("is.mandatory", new Boolean(isMandatoryCheckBox.isSelected()));
            setInfoDC.put("title", titleTextField.getText());
            setInfoDC.put("tooltip", tooltipTextField.getText());
            setInfoDC.put("is.visible", new Boolean(isVisibleCheckBox.isSelected()));
            setInfoDC.put("icon", iconTextField.getText());
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", actionOuidList);
            ArrayList tmpOuidList = new ArrayList();
            for(int i = 0; i < inheritedFieldListData.size(); i++)
                if(((Boolean)((ArrayList)inheritedFieldListData.get(i)).get(3)).equals(new Boolean(false)))
                    tmpOuidList.add((String)((ArrayList)inheritedFieldListData.get(i)).get(0));

            setInfoDC.put("array$ouid@unused.inherited.field", tmpOuidList);
            setInfoDC.put("location", locationModel.getSelectedOID());
            dos.setFieldGroup(setInfoDC);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public DOSChangeable createInformation()
    {
        String tempString = null;
        try
        {
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", ouidTextField.getText());
            setInfoDC.put("name", nameTextField.getText());
            setInfoDC.put("description", descriptionTextArea.getText());
            setInfoDC.put("ouid@class", classOuid);
            setInfoDC.put("index", Utils.getInteger(indexTextField.getText()));
            setInfoDC.put("is.mandatory", new Boolean(isMandatoryCheckBox.isSelected()));
            tempString = titleTextField.getText();
            if(Utils.isNullString(tempString))
                tempString = (String)setInfoDC.get("name");
            setInfoDC.put("title", tempString);
            setInfoDC.put("tooltip", tooltipTextField.getText());
            setInfoDC.put("is.visible", new Boolean(isVisibleCheckBox.isSelected()));
            setInfoDC.put("icon", iconTextField.getText());
            setInfoDC.put("array$ouid@field", fieldOuidList);
            setInfoDC.put("array$ouid@action", actionOuidList);
            setInfoDC.put("location", locationModel.getSelectedOID());
            String fieldGroupOuid = dos.createFieldGroup(setInfoDC);
            System.out.println(fieldGroupOuid);
            setInfoDC.put("ouid", fieldGroupOuid);
            return setInfoDC;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        return null;
    }

    public boolean mandatoryFieldCheck()
    {
        return !Utils.isNullString(nameTextField.getText());
    }

    public void setFieldData(ArrayList selectedNodeAL)
    {
        ArrayList tempAL = new ArrayList();
        DOSChangeable fieldInfo = new DOSChangeable();
        boolean isDuplecated = false;
        try
        {
            fieldInfo = dos.getField((String)selectedNodeAL.get(0));
            tempAL.add((String)fieldInfo.get("ouid"));
            tempAL.add((String)fieldInfo.get("name"));
            tempAL.add((String)fieldInfo.get("description"));
            for(int i = 0; i < fieldListData.size(); i++)
                if(((String)((ArrayList)fieldListData.get(i)).get(0)).equals((String)fieldInfo.get("ouid")))
                    isDuplecated = true;

            for(int i = 0; i < inheritedFieldListData.size(); i++)
                if(((String)((ArrayList)inheritedFieldListData.get(i)).get(0)).equals((String)fieldInfo.get("ouid")))
                    isDuplecated = true;

            if(!isDuplecated)
            {
                fieldListData.add(tempAL.clone());
                tempAL.clear();
                fieldListTable.changeTableData();
                fieldOuidList.add((String)fieldInfo.get("ouid"));
            }
        }
        catch(IIPRequestException iiprequestexception) { }
    }

    public void setActionData(ArrayList selectedNodeAL)
    {
        ArrayList tempAL = new ArrayList();
        DOSChangeable actionInfo = new DOSChangeable();
        boolean isDuplecated = false;
        try
        {
            actionInfo = dos.getAction((String)selectedNodeAL.get(0));
            tempAL.add((String)actionInfo.get("ouid"));
            tempAL.add((String)actionInfo.get("name"));
            tempAL.add((String)actionInfo.get("description"));
            for(int i = 0; i < actionListData.size(); i++)
                if(((String)((ArrayList)actionListData.get(i)).get(0)).equals((String)actionInfo.get("ouid")))
                    isDuplecated = true;

            if(!isDuplecated)
            {
                actionListData.add(tempAL.clone());
                tempAL.clear();
                actionListTable.changeTableData();
                actionOuidList.add((String)actionInfo.get("ouid"));
            }
        }
        catch(IIPRequestException iiprequestexception) { }
    }

    public void setFieldListTable(ArrayList fieldOuidList)
    {
        fieldListData.clear();
        ArrayList tempAL = new ArrayList();
        DOSChangeable fieldInfo = new DOSChangeable();
        try
        {
            if(fieldOuidList != null)
            {
                for(int i = 0; i < fieldOuidList.size(); i++)
                {
                    fieldInfo = dos.getField((String)fieldOuidList.get(i));
                    if(fieldInfo != null)
                    {
                        tempAL.add((String)fieldInfo.get("ouid"));
                        tempAL.add((String)fieldInfo.get("name"));
                        tempAL.add((String)fieldInfo.get("description"));
                    }
                    fieldListData.add(tempAL.clone());
                    tempAL.clear();
                }

                fieldListTable.changeTableData();
                this.fieldOuidList = fieldOuidList;
            }
        }
        catch(IIPRequestException iiprequestexception) { }
    }

    public void setInheritedFieldListTable(ArrayList fieldOuidList)
    {
        inheritedFieldListData.clear();
        ArrayList tempAL = new ArrayList();
        DOSChangeable fieldInfo = new DOSChangeable();
        DOSChangeable tmpFieldInfo = null;
        try
        {
            if(fieldOuidList != null && fieldOuidList.size() > 0)
            {
                for(int i = 0; i < fieldOuidList.size(); i++)
                {
                    tmpFieldInfo = (DOSChangeable)fieldOuidList.get(i);
                    fieldInfo = dos.getField((String)tmpFieldInfo.get("ouid"));
                    if(fieldInfo != null)
                    {
                        tempAL.add((String)fieldInfo.get("ouid"));
                        tempAL.add((String)fieldInfo.get("name"));
                        tempAL.add((String)fieldInfo.get("description"));
                        tempAL.add((Boolean)tmpFieldInfo.get("used"));
                    }
                    inheritedFieldListData.add(tempAL.clone());
                    tempAL.clear();
                }

                fieldGroupTabbedPane.setEnabledAt(2, true);
                inheritedFieldListTable.changeTableData();
                inheritedFieldOuidList = fieldOuidList;
            } else
            {
                fieldGroupTabbedPane.setEnabledAt(2, false);
            }
        }
        catch(IIPRequestException iiprequestexception) { }
    }

    public void setActionListTable(ArrayList actionOuidList)
    {
        actionListData.clear();
        ArrayList tempAL = new ArrayList();
        DOSChangeable actionInfo = new DOSChangeable();
        try
        {
            if(actionOuidList != null)
            {
                for(int i = 0; i < actionOuidList.size(); i++)
                {
                    actionInfo = dos.getAction((String)actionOuidList.get(i));
                    if(actionInfo != null)
                    {
                        tempAL.add((String)actionInfo.get("ouid"));
                        tempAL.add((String)actionInfo.get("name"));
                        tempAL.add((String)actionInfo.get("description"));
                    }
                    actionListData.add(tempAL.clone());
                    tempAL.clear();
                }

                actionListTable.changeTableData();
                this.actionOuidList = actionOuidList;
            }
        }
        catch(IIPRequestException iiprequestexception) { }
    }

    public void removeFieldDataTable()
    {
        int selectRow = fieldListTable.getTable().getSelectedRow();
        if(selectRow > -1)
        {
            fieldListData.remove(selectRow);
            fieldOuidList.remove(selectRow);
            fieldListTable.changeTableData();
        }
    }

    public void removeActionDataTable()
    {
        int selectRow = actionListTable.getTable().getSelectedRow();
        if(selectRow > -1)
        {
            actionListData.remove(selectRow);
            actionOuidList.remove(selectRow);
            actionListTable.changeTableData();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("addField"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Field Selection", classOuid, 1);
            typeClassFrame.setVisible(true);
        } else
        if(command.equals("addAction"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Action Selection", classOuid, 2);
            typeClassFrame.setVisible(true);
        } else
        if(command.equals("delete"))
            if(fieldGroupTabbedPane.getSelectedIndex() == 1)
                removeFieldDataTable();
            else
            if(fieldGroupTabbedPane.getSelectedIndex() == 3)
                removeActionDataTable();
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private DOS dos;
    private final int titleTextWidth = 120;
    private BorderLayout packageInfoBorderLayout;
    private BoxLayout packageInfoLayout;
    public Object parent;
    private JScrollPane mainScrPane;
    private JScrollPane descScrPane;
    private JPanel mainPanel;
    private JPanel descPanel;
    private JTabbedPane fieldGroupTabbedPane;
    private JLabel descriptionLabel;
    private JLabel dummyLabel;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private DynaTextField descriptionTextField;
    private DynaTextField classOuidTextField;
    private DynaTextField indexTextField;
    private JCheckBox isMandatoryCheckBox;
    private DynaTextField titleTextField;
    private DynaTextField tooltipTextField;
    private JCheckBox isVisibleCheckBox;
    private DynaTextField layoutTextField;
    private DynaTextField iconTextField;
    private DynaComboBox locationComboBox;
    private JTextArea descriptionTextArea;
    String classOuid;
    private JToolBar mainToolBar;
    private BoxLayout mainBoxLayout;
    private JButton addFieldButton;
    private JButton addActionButton;
    private JButton removeButton;
    private JScrollPane actionListTableScrPane;
    private Table actionListTable;
    private ArrayList actionListData;
    private ArrayList actionColumnNames;
    private ArrayList actionColumnWidths;
    private ArrayList actionOuidList;
    private JScrollPane fieldListTableScrPane;
    private Table fieldListTable;
    private ArrayList fieldListData;
    private ArrayList fieldColumnNames;
    private ArrayList fieldColumnWidths;
    private ArrayList fieldOuidList;
    private JScrollPane inheritedFieldListTableScrPane;
    private Table inheritedFieldListTable;
    private ArrayList inheritedFieldListData;
    private ArrayList inheritedFieldColumnNames;
    private ArrayList inheritedFieldColumnWidths;
    private ArrayList inheritedFieldOuidList;
    private final String NDS_UNUSEDINHERITEDFIELD = "::/DOS_SYSTEM_DIRECTORY/UNUSEDINHERITEDFIELD";
    private LocationDataLoader locationDataLoader;
    private DynaComboBoxModel locationModel;
    private DOSChangeable setInfoDC;
}
