// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FieldInformation.java

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, TypeClassSelection, CodeSelectionMatrixDialog, InitValueSettingDialog

public class FieldInformation extends JPanel
    implements MouseListener, ItemListener, ActionListener
{
    class DynaComboTypeInstance extends DynaComboBoxDataLoader
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
            return setTypeComboBox();
        }

        DynaComboTypeInstance()
        {
        }
    }

    class DynaComboChangeableInstance extends DynaComboBoxDataLoader
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
            return setChangeableComboBox();
        }

        DynaComboChangeableInstance()
        {
        }
    }

    class DynaComboScopeInstance extends DynaComboBoxDataLoader
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
            return setScopeComboBox();
        }

        DynaComboScopeInstance()
        {
        }
    }

    class DynaComboBooleanInstance extends DynaComboBoxDataLoader
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
            return setBooleanComboBox();
        }

        DynaComboBooleanInstance()
        {
        }
    }


    public FieldInformation(Object parentFrame, String classOuid)
    {
        dos = null;
        this.classOuid = "";
        typeClassOuid = "";
        initialValue = "";
        dComboType = new DynaComboTypeInstance();
        dComboChangeable = new DynaComboChangeableInstance();
        dComboScope = new DynaComboScopeInstance();
        dComboBoolean = new DynaComboBooleanInstance();
        typeComboModel = new DynaComboBoxModel(dComboType);
        changeableComboModel = new DynaComboBoxModel(dComboChangeable);
        scopeComboModel = new DynaComboBoxModel(dComboScope);
        booleanComboModel = new DynaComboBoxModel(dComboBoolean);
        setInfoDC = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            this.classOuid = classOuid;
            parent = parentFrame;
            initialize();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
    }

    public void initialize()
    {
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
        mainPanel = new JPanel();
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel.setLayout(gridBag);
        ouidTextField = new DynaTextField();
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setTitleWidth(120);
        ouidTextField.setTitleVisible(true);
        ouidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(ouidTextField, gridBagCon);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(true);
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(120);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(nameTextField, gridBagCon);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Description");
        descriptionTextField.setTitleWidth(120);
        descriptionTextField.setTitleVisible(true);
        descriptionTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.20000000000000001D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(descPanel, gridBagCon);
        classOuidTextField = new DynaTextField();
        classOuidTextField.setMandatory(true);
        classOuidTextField.setTitleText("Class Ouid");
        classOuidTextField.setTitleWidth(120);
        classOuidTextField.setTitleVisible(true);
        classOuidTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(classOuidTextField, gridBagCon);
        typeComboBox = new DynaComboBox();
        typeComboBox.setMandatory(true);
        typeComboBox.setTitleText("Type");
        typeComboBox.setTitleWidth(120);
        typeComboBox.setModel(typeComboModel);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        typeComboBox.addItemListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(typeComboBox, gridBagCon);
        typeClassTextField = new DynaTextField();
        typeClassTextField.setMandatory(false);
        typeClassTextField.setTitleText("Type Class");
        typeClassTextField.setTitleWidth(120);
        typeClassTextField.setTitleVisible(true);
        typeClassTextField.setEditable(false);
        typeClassTextField.addMouseListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(typeClassTextField, gridBagCon);
        multiplicityFromTextField = new DynaTextField();
        multiplicityFromTextField.setMandatory(false);
        multiplicityFromTextField.setTitleText("Lower Bound");
        multiplicityFromTextField.setTitleWidth(120);
        multiplicityFromTextField.setTitleVisible(true);
        multiplicityFromTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 7;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(multiplicityFromTextField, gridBagCon);
        multiplicityToTextField = new DynaTextField();
        multiplicityToTextField.setMandatory(false);
        multiplicityToTextField.setTitleText("Upper Bound");
        multiplicityToTextField.setTitleWidth(120);
        multiplicityToTextField.setTitleVisible(true);
        multiplicityToTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 8;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(multiplicityToTextField, gridBagCon);
        changeableComboBox = new DynaComboBox();
        changeableComboBox.setMandatory(false);
        changeableComboBox.setTitleText("Changeable");
        changeableComboBox.setTitleWidth(120);
        changeableComboBox.setTitleVisible(true);
        changeableComboBox.setEditable(false);
        changeableComboBox.setModel(changeableComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 9;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(changeableComboBox, gridBagCon);
        scopeComboBox = new DynaComboBox();
        scopeComboBox.setMandatory(false);
        scopeComboBox.setTitleText("Scope");
        scopeComboBox.setTitleWidth(120);
        scopeComboBox.setTitleVisible(true);
        scopeComboBox.setEditable(false);
        scopeComboBox.setModel(scopeComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 10;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(scopeComboBox, gridBagCon);
        initialValueTextField = new DynaTextField();
        initialValueTextField.setMandatory(false);
        initialValueTextField.setTitleText("Initial value");
        initialValueTextField.setTitleWidth(120);
        initialValueTextField.setTitleVisible(true);
        initialValueTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 11;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(initialValueTextField, gridBagCon);
        valueSelectButton = new JButton();
        valueSelectButton.setMargin(new Insets(0, 0, 0, 0));
        valueSelectButton.setActionCommand("ValueSelect");
        valueSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        valueSelectButton.addActionListener(this);
        dateSelectButton = new JButton();
        dateSelectButton.setMargin(new Insets(0, 0, 0, 0));
        dateSelectButton.setActionCommand("DateSelect");
        dateSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/DateSelectButton.gif")));
        dateSelectButton.addActionListener(this);
        initialValueComboBox = new DynaComboBox();
        initialValueComboBox.setMandatory(false);
        initialValueComboBox.setTitleText("Initial value");
        initialValueComboBox.setTitleWidth(120);
        initialValueComboBox.setTitleVisible(true);
        initialValueComboBox.setEditable(false);
        initialValueComboBox.setModel(booleanComboModel);
        initialValueComboBox.setActionCommand("BooleanSelect");
        initialValueComboBox.addActionListener(this);
        indexTextField = new DynaTextField();
        indexTextField.setMandatory(false);
        indexTextField.setTitleText("Index");
        indexTextField.setTitleWidth(120);
        indexTextField.setTitleVisible(true);
        indexTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 12;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(indexTextField, gridBagCon);
        codeTextField = new DynaTextField();
        codeTextField.setTitleText("Code");
        codeTextField.setTitleWidth(120);
        codeTextField.setTitleVisible(true);
        codeTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 13;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(codeTextField, gridBagCon);
        isMandatoryCheckBox = new JCheckBox("Mandatory");
        isMandatoryCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 14;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isMandatoryCheckBox, gridBagCon);
        isVisibleCheckBox = new JCheckBox("Visible");
        isVisibleCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 15;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isVisibleCheckBox, gridBagCon);
        sizeTextField = new DynaTextField();
        sizeTextField.setMandatory(false);
        sizeTextField.setTitleText("Size");
        sizeTextField.setTitleWidth(120);
        sizeTextField.setTitleVisible(true);
        sizeTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 16;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(sizeTextField, gridBagCon);
        titleTextField = new DynaTextField();
        titleTextField.setMandatory(false);
        titleTextField.setTitleText("Title");
        titleTextField.setTitleWidth(120);
        titleTextField.setTitleVisible(true);
        titleTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 17;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(titleTextField, gridBagCon);
        tooltipTextField = new DynaTextField();
        tooltipTextField.setMandatory(false);
        tooltipTextField.setTitleText("Tooltip");
        tooltipTextField.setTitleWidth(120);
        tooltipTextField.setTitleVisible(true);
        tooltipTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 18;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(tooltipTextField, gridBagCon);
        widthTextField = new DynaTextField();
        widthTextField.setMandatory(false);
        widthTextField.setTitleText("Width");
        widthTextField.setTitleWidth(120);
        widthTextField.setTitleVisible(true);
        widthTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 19;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(widthTextField, gridBagCon);
        heightTextField = new DynaTextField();
        heightTextField.setMandatory(false);
        heightTextField.setTitleText("Height");
        heightTextField.setTitleWidth(120);
        heightTextField.setTitleVisible(true);
        heightTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 20;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(heightTextField, gridBagCon);
        columnTextField = new DynaTextField();
        columnTextField.setMandatory(false);
        columnTextField.setTitleText("Column");
        columnTextField.setTitleWidth(120);
        columnTextField.setTitleVisible(true);
        columnTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 21;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(columnTextField, gridBagCon);
        titleWidthTextField = new DynaTextField();
        titleWidthTextField.setMandatory(false);
        titleWidthTextField.setTitleText("Title width");
        titleWidthTextField.setTitleWidth(120);
        titleWidthTextField.setTitleVisible(true);
        titleWidthTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 22;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(titleWidthTextField, gridBagCon);
        isTitleVisibleCheckBox = new JCheckBox("Title Visible");
        isTitleVisibleCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 23;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isTitleVisibleCheckBox, gridBagCon);
        dummyLabel = new JLabel("");
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 25;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(descPanel);
        mainPanel.add(typeComboBox);
        mainPanel.add(typeClassTextField);
        mainPanel.add(multiplicityFromTextField);
        mainPanel.add(multiplicityToTextField);
        mainPanel.add(changeableComboBox);
        mainPanel.add(scopeComboBox);
        mainPanel.add(initialValueTextField);
        mainPanel.add(indexTextField);
        mainPanel.add(codeTextField);
        mainPanel.add(isMandatoryCheckBox);
        mainPanel.add(isVisibleCheckBox);
        mainPanel.add(sizeTextField);
        mainPanel.add(titleTextField);
        mainPanel.add(tooltipTextField);
        mainPanel.add(widthTextField);
        mainPanel.add(heightTextField);
        mainPanel.add(columnTextField);
        mainPanel.add(titleWidthTextField);
        mainPanel.add(isTitleVisibleCheckBox);
        mainPanel.add(dummyLabel);
        mainScrPane.getViewport().add(mainPanel, null);
        add(mainScrPane, "Center");
        typeComboModel.enableDataLoad();
        changeableComboModel.enableDataLoad();
        scopeComboModel.enableDataLoad();
        booleanComboModel.enableDataLoad();
    }

    public void setFieldInfoField(DOSChangeable dosFieldInfo)
    {
        setInfoDC = dosFieldInfo;
        ouidTextField.setText("");
        nameTextField.setText("");
        typeComboModel.setElementAt(-1);
        descriptionTextArea.setText("");
        typeClassTextField.setText("");
        multiplicityFromTextField.setText("");
        multiplicityToTextField.setText("");
        changeableComboModel.setElementAt(-1);
        scopeComboModel.setElementAt(-1);
        initialValueTextField.setText("");
        booleanComboModel.setElementAt(-1);
        indexTextField.setText("");
        codeTextField.setText("");
        sizeTextField.setText("");
        titleTextField.setText("");
        tooltipTextField.setText("");
        widthTextField.setText("");
        heightTextField.setText("");
        columnTextField.setText("");
        titleWidthTextField.setText("");
        ouidTextField.setText((String)dosFieldInfo.get("ouid"));
        nameTextField.setText((String)dosFieldInfo.get("name"));
        typeComboModel.setSelectedItemByOID((Byte)dosFieldInfo.get("type"));
        updateMainPanel();
        descriptionTextArea.setText((String)dosFieldInfo.get("description"));
        setTypeClassOuid((String)dosFieldInfo.get("type.ouid@class"));
        if(dosFieldInfo.get("multiplicity.from") != null)
            multiplicityFromTextField.setText(dosFieldInfo.get("multiplicity.from").toString());
        if(dosFieldInfo.get("multiplicity.to") != null)
            multiplicityToTextField.setText(dosFieldInfo.get("multiplicity.to").toString());
        if(dosFieldInfo.get("changeable") != null)
            changeableComboModel.setElementAt(Utils.getByte((Byte)dosFieldInfo.get("changeable")) - 1);
        if(dosFieldInfo.get("scope") != null)
            scopeComboModel.setElementAt(Utils.getByte((Byte)dosFieldInfo.get("scope")) - 1);
        setInitialValue((String)dosFieldInfo.get("initial.value"));
        if(dosFieldInfo.get("index") != null)
            indexTextField.setText(dosFieldInfo.get("index").toString());
        codeTextField.setText((String)dosFieldInfo.get("code"));
        if(dosFieldInfo.get("is.mandatory") != null)
            isMandatoryCheckBox.setSelected(Utils.getBoolean((Boolean)dosFieldInfo.get("is.mandatory")));
        else
            isMandatoryCheckBox.setSelected(false);
        if(dosFieldInfo.get("is.visible") != null)
            isVisibleCheckBox.setSelected(Utils.getBoolean((Boolean)dosFieldInfo.get("is.visible")));
        else
            isVisibleCheckBox.setSelected(false);
        if(dosFieldInfo.get("size") != null)
            sizeTextField.setText(dosFieldInfo.get("size").toString());
        titleTextField.setText((String)dosFieldInfo.get("title"));
        tooltipTextField.setText((String)dosFieldInfo.get("tooltip"));
        if(dosFieldInfo.get("width") != null)
            widthTextField.setText(dosFieldInfo.get("width").toString());
        if(dosFieldInfo.get("height") != null)
            heightTextField.setText(dosFieldInfo.get("height").toString());
        if(dosFieldInfo.get("title.width") != null)
            titleWidthTextField.setText(dosFieldInfo.get("title.width").toString());
        if(dosFieldInfo.get("is.title.visible") != null)
            isTitleVisibleCheckBox.setSelected(Utils.getBoolean((Boolean)dosFieldInfo.get("is.title.visible")));
        else
            isTitleVisibleCheckBox.setSelected(false);
        if(dosFieldInfo.get("column") != null)
            columnTextField.setText(dosFieldInfo.get("column").toString());
    }

    public void setInformation()
    {
        try
        {
            if(nameTextField.isEditable())
            {
                setInfoDC.put("ouid", ouidTextField.getText());
                setInfoDC.put("name", nameTextField.getText());
                setInfoDC.put("description", descriptionTextArea.getText());
                setInfoDC.put("ouid@class", classOuid);
                setInfoDC.put("type", (Byte)typeComboModel.getSelectedOID());
                setInfoDC.put("type.ouid@class", typeClassOuid);
                setInfoDC.put("multiplicity.from", Utils.getInteger(multiplicityFromTextField.getText()));
                setInfoDC.put("multiplicity.to", Utils.getInteger(multiplicityToTextField.getText()));
                setInfoDC.put("changeable", (Byte)changeableComboModel.getSelectedOID());
                setInfoDC.put("scope", (Byte)scopeComboModel.getSelectedOID());
                String selectedItem = (String)typeComboBox.getSelectedItem();
                if(selectedItem.equals("boolean") || selectedItem.equals("object") || selectedItem.equals("Code") || selectedItem.equals("Code (Field Referenced)"))
                    setInfoDC.put("initial.value", initialValue);
                else
                    setInfoDC.put("initial.value", initialValueTextField.getText());
                setInfoDC.put("index", Utils.getInteger(indexTextField.getText()));
                setInfoDC.put("code", codeTextField.getText());
                setInfoDC.put("is.mandatory", new Boolean(isMandatoryCheckBox.isSelected()));
                setInfoDC.put("is.visible", new Boolean(isVisibleCheckBox.isSelected()));
                setInfoDC.put("size", Utils.getDouble(sizeTextField.getText()));
                setInfoDC.put("title", titleTextField.getText());
                setInfoDC.put("tooltip", tooltipTextField.getText());
                setInfoDC.put("width", Utils.getInteger(widthTextField.getText()));
                setInfoDC.put("height", Utils.getInteger(heightTextField.getText()));
                setInfoDC.put("title.width", Utils.getInteger(titleWidthTextField.getText()));
                setInfoDC.put("is.title.visible", new Boolean(isTitleVisibleCheckBox.isSelected()));
                setInfoDC.put("column", Utils.getInteger(columnTextField.getText()));
                dos.setField(setInfoDC);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
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
            setInfoDC.put("type", (Byte)typeComboModel.getSelectedOID());
            setInfoDC.put("type.ouid@class", typeClassOuid);
            setInfoDC.put("multiplicity.from", Utils.getInteger(multiplicityFromTextField.getText()));
            setInfoDC.put("multiplicity.to", Utils.getInteger(multiplicityToTextField.getText()));
            setInfoDC.put("changeable", (Byte)changeableComboModel.getSelectedOID());
            setInfoDC.put("scope", (Byte)scopeComboModel.getSelectedOID());
            String selectedItem = (String)typeComboBox.getSelectedItem();
            if(selectedItem.equals("boolean") || selectedItem.equals("object") || selectedItem.equals("Code") || selectedItem.equals("Code (Field Referenced"))
                setInfoDC.put("initial.value", initialValue);
            else
                setInfoDC.put("initial.value", initialValueTextField.getText());
            setInfoDC.put("index", Utils.getInteger(indexTextField.getText()));
            setInfoDC.put("code", codeTextField.getText());
            if(Utils.isNullString(tempString))
            {
                tempString = (String)setInfoDC.get("name");
                tempString = tempString.trim().toUpperCase().replace(' ', '_').replace('/', '$').replace('\\', '$').replace('.', '$');
            }
            setInfoDC.put("code", tempString);
            tempString = titleTextField.getText();
            if(Utils.isNullString(tempString))
                tempString = (String)setInfoDC.get("name");
            setInfoDC.put("title", tempString);
            setInfoDC.put("is.mandatory", new Boolean(isMandatoryCheckBox.isSelected()));
            setInfoDC.put("is.visible", new Boolean(isVisibleCheckBox.isSelected()));
            setInfoDC.put("size", Utils.getDouble(sizeTextField.getText()));
            setInfoDC.put("tooltip", tooltipTextField.getText());
            setInfoDC.put("width", Utils.getInteger(widthTextField.getText()));
            setInfoDC.put("height", Utils.getInteger(heightTextField.getText()));
            setInfoDC.put("title.width", Utils.getInteger(titleWidthTextField.getText()));
            setInfoDC.put("is.title.visible", new Boolean(isTitleVisibleCheckBox.isSelected()));
            setInfoDC.put("column", Utils.getInteger(columnTextField.getText()));
            String fieldOuid = dos.createField(setInfoDC);
            setInfoDC.put("ouid", fieldOuid);
            return setInfoDC;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return null;
    }

    public boolean mandatoryFieldCheck()
    {
        return !Utils.isNullString(nameTextField.getText()) && typeComboModel.getSelectedOID() != null;
    }

    public void setClassTypeOID(ArrayList oidAL)
    {
        setTypeClassOuid((String)oidAL.get(0));
    }

    private void setTypeClassOuid(String ouid)
    {
        typeClassOuid = ouid;
        if(Utils.isNullString(typeClassOuid))
        {
            typeClassTextField.setText("");
            return;
        }
        String tempString = "";
        try
        {
            if(typeComboModel.getSelectedItem().toString().equals("object"))
            {
                DOSChangeable dosClass = dos.getClass(typeClassOuid);
                if(dosClass != null)
                {
                    tempString = (String)dosClass.get("name");
                    if(dosClass.get("description") != null)
                        tempString = tempString + " (" + (String)dosClass.get("description") + ")";
                }
            } else
            if(typeComboModel.getSelectedItem().toString().equals("Code (Field Referenced)"))
            {
                DOSChangeable dosField = dos.getField(typeClassOuid);
                if(dosField != null)
                {
                    tempString = (String)dosField.get("name");
                    if(dosField.get("description") != null)
                        tempString = tempString + " (" + (String)dosField.get("description") + ")";
                }
            } else
            if(typeComboModel.getSelectedItem().toString().equals("Code"))
            {
                DOSChangeable dosCode = dos.getCode(typeClassOuid);
                if(dosCode != null)
                {
                    tempString = (String)dosCode.get("name");
                    if(dosCode.get("description") != null)
                        tempString = tempString + " (" + (String)dosCode.get("description") + ")";
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        typeClassTextField.setText(tempString);
    }

    public void setInitialValue(String value)
    {
        if(Utils.isNullString(value))
            return;
        initialValue = value;
        String selectedItem = (String)typeComboBox.getSelectedItem();
        if(selectedItem.equals("boolean"))
            initialValueComboBox.setSelectedItem(value);
        else
        if(selectedItem.equals("byte") || selectedItem.equals("char") || selectedItem.equals("double") || selectedItem.equals("float") || selectedItem.equals("int") || selectedItem.equals("long") || selectedItem.equals("short") || selectedItem.equals("String"))
            initialValueTextField.setText(value);
        else
        if(selectedItem.equals("object"))
            try
            {
                DOSChangeable data = dos.get(value);
                initialValueTextField.setText((String)data.get("md$number"));
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(selectedItem.equals("Code") || selectedItem.equals("Code (Field Referenced)"))
            try
            {
                DOSChangeable data = dos.getCodeItem(value);
                if(data == null)
                    return;
                initialValueTextField.setText(data.get("name") + " [" + data.get("codeitemid") + "]");
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(selectedItem.equals("date time"))
            initialValueTextField.setText(value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + " " + value.substring(8, 10) + ":" + value.substring(10, 12) + ":" + value.substring(12, 14));
        else
        if(selectedItem.equals("date"))
            initialValueTextField.setText(value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8));
        else
        if(selectedItem.equals("time"))
            initialValueTextField.setText(value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6));
    }

    public void setFieldEnabled(boolean isEnable)
    {
        nameTextField.setEditable(isEnable);
        descriptionTextArea.setEditable(isEnable);
        classOuidTextField.setEditable(isEnable);
        typeComboBox.setEnabled(isEnable);
        multiplicityFromTextField.setEditable(isEnable);
        multiplicityToTextField.setEditable(isEnable);
        changeableComboBox.setEnabled(isEnable);
        scopeComboBox.setEnabled(isEnable);
        initialValueTextField.setEditable(isEnable);
        initialValueComboBox.setEnabled(isEnable);
        indexTextField.setEditable(isEnable);
        codeTextField.setEditable(isEnable);
        sizeTextField.setEditable(isEnable);
        titleTextField.setEditable(isEnable);
        tooltipTextField.setEditable(isEnable);
        widthTextField.setEditable(isEnable);
        heightTextField.setEditable(isEnable);
        titleWidthTextField.setEditable(isEnable);
        isMandatoryCheckBox.setEnabled(isEnable);
        isVisibleCheckBox.setEnabled(isEnable);
        isTitleVisibleCheckBox.setEnabled(isEnable);
        columnTextField.setEditable(isEnable);
    }

    public ArrayList setTypeComboBox()
    {
        ArrayList typeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        typeAL.add(new Byte((byte)1));
        typeAL.add("boolean");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)2));
        typeAL.add("byte");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)3));
        typeAL.add("char");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)4));
        typeAL.add("double");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)5));
        typeAL.add("float");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)6));
        typeAL.add("int");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)7));
        typeAL.add("long");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)8));
        typeAL.add("short");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)13));
        typeAL.add("String");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)16));
        typeAL.add("object");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)21));
        typeAL.add("date time");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)22));
        typeAL.add("date");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)23));
        typeAL.add("time");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)24));
        typeAL.add("Code");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)25));
        typeAL.add("Code (Field Referenced)");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        return returnAL;
    }

    public ArrayList setChangeableComboBox()
    {
        ArrayList changeableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        changeableAL.add(new Byte((byte)1));
        changeableAL.add("changeable");
        returnAL.add(changeableAL.clone());
        changeableAL.clear();
        changeableAL.add(new Byte((byte)2));
        changeableAL.add("add only");
        returnAL.add(changeableAL.clone());
        changeableAL.clear();
        changeableAL.add(new Byte((byte)3));
        changeableAL.add("frozen");
        returnAL.add(changeableAL.clone());
        changeableAL.clear();
        changeableAL.add(new Byte((byte)4));
        changeableAL.add("read only");
        returnAL.add(changeableAL.clone());
        changeableAL.clear();
        return returnAL;
    }

    public ArrayList setScopeComboBox()
    {
        ArrayList scopeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        scopeAL.add(new Byte((byte)1));
        scopeAL.add("classifier");
        returnAL.add(scopeAL.clone());
        scopeAL.clear();
        scopeAL.add(new Byte((byte)2));
        scopeAL.add("instance");
        returnAL.add(scopeAL.clone());
        scopeAL.clear();
        return returnAL;
    }

    public ArrayList setBooleanComboBox()
    {
        ArrayList booleanAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        booleanAL.add(null);
        booleanAL.add("");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        booleanAL.add(new Boolean(true));
        booleanAL.add("true");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        booleanAL.add(new Boolean(false));
        booleanAL.add("false");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        return returnAL;
    }

    public void mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && typeComboModel.getSelectedItem().toString().equals("object"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Class Type Selection");
            typeClassFrame.setVisible(true);
        } else
        if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && typeComboModel.getSelectedItem().toString().equals("Code (Field Referenced)"))
        {
            CodeSelectionMatrixDialog csmd = new CodeSelectionMatrixDialog(null, true, classOuid, ouidTextField.getText());
            csmd.setVisible(true);
        } else
        if(e.getClickCount() == 2 && typeComboModel.getSelectedItem().toString().equals("Code"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Code Selection", "Code");
            typeClassFrame.setVisible(true);
        }
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

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();
        if(source.equals(typeComboBox.getComboBox()))
        {
            typeClassTextField.setText("");
            typeClassOuid = null;
            initialValueTextField.setText("");
            initialValue = null;
            updateMainPanel();
        }
    }

    private void updateMainPanel()
    {
        Component components[] = mainPanel.getComponents();
        Component comp = null;
        GridBagLayout gridBag = (GridBagLayout)mainPanel.getLayout();
        GridBagConstraints gridBagCon = null;
        int i;
        for(i = 0; i < components.length; i++)
        {
            comp = components[i];
            if(comp == initialValueTextField)
            {
                gridBagCon = gridBag.getConstraints(initialValueTextField);
                mainPanel.remove(initialValueTextField);
                if(components[i + 1] == valueSelectButton)
                    mainPanel.remove(valueSelectButton);
                else
                if(components[i + 1] == dateSelectButton)
                    mainPanel.remove(dateSelectButton);
                break;
            }
            if(comp != initialValueComboBox)
                continue;
            gridBagCon = gridBag.getConstraints(initialValueComboBox);
            mainPanel.remove(initialValueComboBox);
            break;
        }

        String selectedItem = (String)typeComboModel.getSelectedItem();
        if(selectedItem.equals("boolean"))
        {
            gridBagCon.insets = new Insets(0, 0, 0, 0);
            gridBagCon.weightx = 1.0D;
            gridBagCon.weighty = 0.0D;
            gridBagCon.gridx = 0;
            gridBagCon.gridy = 11;
            gridBagCon.gridwidth = 2;
            gridBag.setConstraints(initialValueComboBox, gridBagCon);
            mainPanel.add(initialValueComboBox, i);
        } else
        if(selectedItem.equals("byte") || selectedItem.equals("char") || selectedItem.equals("double") || selectedItem.equals("float") || selectedItem.equals("int") || selectedItem.equals("long") || selectedItem.equals("short") || selectedItem.equals("String"))
        {
            gridBagCon.insets = new Insets(0, 0, 0, 0);
            gridBagCon.weightx = 1.0D;
            gridBagCon.weighty = 0.0D;
            gridBagCon.gridx = 0;
            gridBagCon.gridy = 11;
            gridBagCon.gridwidth = 2;
            gridBag.setConstraints(initialValueTextField, gridBagCon);
            initialValueTextField.setEditable(true);
            mainPanel.add(initialValueTextField, i);
        } else
        if(selectedItem.equals("object") || selectedItem.equals("Code") || selectedItem.equals("Code (Field Referenced)"))
        {
            gridBagCon.insets = new Insets(0, 0, 0, 0);
            gridBagCon.weightx = 1.0D;
            gridBagCon.weighty = 0.0D;
            gridBagCon.gridx = 0;
            gridBagCon.gridy = 11;
            gridBagCon.gridwidth = 1;
            gridBag.setConstraints(initialValueTextField, gridBagCon);
            initialValueTextField.setEditable(false);
            mainPanel.add(initialValueTextField, i);
            gridBagCon.insets = new Insets(1, 0, 0, 5);
            gridBagCon.weightx = 0.0D;
            gridBagCon.weighty = 0.0D;
            gridBagCon.gridx = 1;
            gridBagCon.gridy = 11;
            gridBagCon.gridwidth = 1;
            gridBag.setConstraints(valueSelectButton, gridBagCon);
            mainPanel.add(valueSelectButton, i + 1);
        } else
        if(selectedItem.equals("date time") || selectedItem.equals("date") || selectedItem.equals("time"))
        {
            gridBagCon.insets = new Insets(0, 0, 0, 0);
            gridBagCon.weightx = 1.0D;
            gridBagCon.weighty = 0.0D;
            gridBagCon.gridx = 0;
            gridBagCon.gridy = 11;
            gridBagCon.gridwidth = 1;
            gridBag.setConstraints(initialValueTextField, gridBagCon);
            initialValueTextField.setEditable(false);
            mainPanel.add(initialValueTextField, i);
            gridBagCon.insets = new Insets(1, 0, 0, 5);
            gridBagCon.weightx = 0.0D;
            gridBagCon.weighty = 0.0D;
            gridBagCon.gridx = 1;
            gridBagCon.gridy = 11;
            gridBagCon.gridwidth = 1;
            gridBag.setConstraints(dateSelectButton, gridBagCon);
            mainPanel.add(dateSelectButton, i + 1);
        }
        mainPanel.updateUI();
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        String selectedItem = (String)typeComboBox.getSelectedItem();
        if(command.equals("ValueSelect"))
        {
            if(selectedItem.equals("object"))
            {
                if(!Utils.isNullString(typeClassOuid))
                {
                    String values[] = InitValueSettingDialog.showDialogForObject((JFrame)parent, (Component)e.getSource(), true, typeClassOuid);
                    if(values != null)
                    {
                        initialValueTextField.setText(values[1]);
                        initialValueTextField.setCaretPosition(0);
                        initialValue = values[0];
                    } else
                    {
                        initialValueTextField.setText("");
                        initialValueTextField.setCaretPosition(0);
                        initialValue = null;
                    }
                } else
                {
                    System.out.println("type class ouid is Null");
                }
            } else
            if(selectedItem.equals("Code"))
            {
                if(!Utils.isNullString(typeClassOuid))
                {
                    String values[] = InitValueSettingDialog.showDialogForCode((JFrame)parent, (Component)e.getSource(), true, typeClassOuid);
                    if(values != null)
                    {
                        initialValueTextField.setText(values[1]);
                        initialValueTextField.setCaretPosition(0);
                        initialValue = values[0];
                    } else
                    {
                        initialValueTextField.setText("");
                        initialValueTextField.setCaretPosition(0);
                        initialValue = null;
                    }
                } else
                {
                    System.out.println("type class ouid is Null");
                }
            } else
            if(selectedItem.equals("Code (Field Referenced)"))
            {
                TypeClassSelection tcs = new TypeClassSelection(null, "Code Selection", "Code");
                tcs.setVisible(true);
                String codeOuid = tcs.selectedOuid;
                if(Utils.isNullString(codeOuid))
                    return;
                String values[] = InitValueSettingDialog.showDialogForCode((JFrame)parent, (Component)e.getSource(), true, codeOuid);
                if(values != null)
                {
                    initialValueTextField.setText(values[1]);
                    initialValueTextField.setCaretPosition(0);
                    initialValue = values[0];
                } else
                {
                    initialValueTextField.setText("");
                    initialValueTextField.setCaretPosition(0);
                    initialValue = null;
                }
            }
        } else
        if(command.equals("DateSelect"))
        {
            if(selectedItem.equals("date time") || selectedItem.equals("date") || selectedItem.equals("time"))
            {
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                new DynaDateChooser((JFrame)parent);
                java.util.Date date = DynaDateChooser.showDialog((Component)e.getSource(), null);
                if(date != null)
                {
                    if(selectedItem.equals("date time"))
                    {
                        initialValueTextField.setText(sdfDateTime.format(date));
                        initialValue = sdf.format(date);
                    } else
                    if(selectedItem.equals("date"))
                    {
                        initialValueTextField.setText(sdfDate.format(date));
                        initialValue = sdf.format(date).substring(0, 8);
                    } else
                    if(selectedItem.equals("time"))
                    {
                        initialValueTextField.setText(sdfTime.format(date));
                        initialValue = sdf.format(date).substring(8, 14);
                    }
                    initialValueTextField.setCaretPosition(0);
                } else
                {
                    initialValueTextField.setText("");
                    initialValueTextField.setCaretPosition(0);
                    initialValue = null;
                }
            }
        } else
        if(command.equals("BooleanSelect") && selectedItem.equals("boolean"))
            initialValue = (String)initialValueComboBox.getSelectedItem();
    }

    private DOS dos;
    private final int titleTextWidth = 120;
    private BorderLayout packageInfoBorderLayout;
    private Object parent;
    private JPanel mainPanel;
    private JPanel descPanel;
    private JScrollPane mainScrPane;
    private JScrollPane descScrPane;
    private JLabel descriptionLabel;
    private JLabel dummyLabel;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private DynaTextField descriptionTextField;
    private DynaTextField classOuidTextField;
    private DynaComboBox typeComboBox;
    private DynaTextField typeClassTextField;
    private DynaTextField multiplicityFromTextField;
    private DynaTextField multiplicityToTextField;
    private DynaComboBox changeableComboBox;
    private DynaComboBox scopeComboBox;
    private DynaTextField initialValueTextField;
    private JButton valueSelectButton;
    private JButton dateSelectButton;
    private DynaComboBox initialValueComboBox;
    private DynaTextField indexTextField;
    private DynaTextField codeTextField;
    private JCheckBox isMandatoryCheckBox;
    private JCheckBox isVisibleCheckBox;
    private DynaTextField sizeTextField;
    private DynaTextField titleTextField;
    private DynaTextField tooltipTextField;
    private DynaTextField widthTextField;
    private DynaTextField heightTextField;
    private DynaTextField columnTextField;
    private DynaTextField titleWidthTextField;
    private JCheckBox isTitleVisibleCheckBox;
    private JTextArea descriptionTextArea;
    private String classOuid;
    private String typeClassOuid;
    private String initialValue;
    private DynaComboTypeInstance dComboType;
    private DynaComboChangeableInstance dComboChangeable;
    private DynaComboScopeInstance dComboScope;
    private DynaComboBooleanInstance dComboBoolean;
    private DynaComboBoxModel typeComboModel;
    private DynaComboBoxModel changeableComboModel;
    private DynaComboBoxModel scopeComboModel;
    private DynaComboBoxModel booleanComboModel;
    private DOSChangeable setInfoDC;
    public static final byte SCOPE_CLASSIFIER = 1;
    public static final byte SCOPE_INSTANCE = 2;
    public static final byte CHANGEABLE_CHANGEABLE = 1;
    public static final byte CHANGEABLE_ADD_ONLY = 2;
    public static final byte CHANGEABLE_FROZEN = 3;
    public static final byte CHANGEABLE_READ_ONLY = 4;
}
