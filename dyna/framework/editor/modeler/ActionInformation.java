// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActionInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.client.FileTransferDialog;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.DynaComboBox;
import dyna.uic.DynaComboBoxDataLoader;
import dyna.uic.DynaComboBoxModel;
import dyna.uic.DynaTextField;
import dyna.uic.DynaTheme;
import dyna.uic.SoftWareFilter;
import dyna.uic.SwingWorker;
import dyna.uic.Table;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import dyna.util.notepad.Notepad;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, ActionParameterInformation, TypeClassSelection, ScriptSelection

public class ActionInformation extends JPanel
    implements MouseListener, ActionListener
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

    class DynaComboLeafInstance extends DynaComboBoxDataLoader
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
            return setLeafComboBox();
        }

        DynaComboLeafInstance()
        {
        }
    }

    class DynaComboQueryInstance extends DynaComboBoxDataLoader
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
            return setQueryComboBox();
        }

        DynaComboQueryInstance()
        {
        }
    }

    class DynaComboVisibleInstance extends DynaComboBoxDataLoader
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
            return setVisibleComboBox();
        }

        DynaComboVisibleInstance()
        {
        }
    }

    class DynaComboCallTypeInstance extends DynaComboBoxDataLoader
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
            return setCallTypeComboBox();
        }

        DynaComboCallTypeInstance()
        {
        }
    }

    class DynaComboExtInstance extends DynaComboBoxDataLoader
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
            return setExtComboBox();
        }

        DynaComboExtInstance()
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


    public ActionInformation(Object parentFrame, String classOuid)
    {
        dos = null;
        dss = null;
        associateData = new ArrayList();
        columnNames = new ArrayList();
        columnWidths = new ArrayList();
        imageAction = new ImageIcon("icons/Action.gif");
        imageActionParameter = new ImageIcon("icons/ActionParameter.gif");
        actionOuid = "";
        this.classOuid = "";
        typeClassOuid = "";
        dComboType = new DynaComboTypeInstance();
        dComboScope = new DynaComboScopeInstance();
        dComboCallType = new DynaComboCallTypeInstance();
        dComboExt = new DynaComboExtInstance();
        typeComboModel = new DynaComboBoxModel(dComboType);
        scopeComboModel = new DynaComboBoxModel(dComboScope);
        callTypeComboModel = new DynaComboBoxModel(dComboCallType);
        extComboModel = new DynaComboBoxModel(dComboExt);
        arrayActionParameter = new ArrayList();
        ftd = null;
        setInfoDC = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            dss = (DSS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DSS1");
            this.classOuid = classOuid;
            parent = parentFrame;
            actionParameterInfo = new ActionParameterInformation(this);
            makeActionParameterTable();
            initialize();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void initialize()
    {
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel = new JPanel();
        actionParameterSplitPane = new JSplitPane(0);
        ActionInfoBorderLayout = new BorderLayout();
        setLayout(ActionInfoBorderLayout);
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
        gridBagCon.gridx = 1;
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
        gridBagCon.gridx = 1;
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
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(classOuidTextField, gridBagCon);
        returnTypeComboBox = new DynaComboBox();
        returnTypeComboBox.setMandatory(true);
        returnTypeComboBox.setTitleText("Return type");
        returnTypeComboBox.setTitleWidth(120);
        returnTypeComboBox.setTitleVisible(true);
        returnTypeComboBox.setEditable(false);
        returnTypeComboBox.setModel(typeComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(returnTypeComboBox, gridBagCon);
        returnTypeClassTextField = new DynaTextField();
        returnTypeClassTextField.setMandatory(true);
        returnTypeClassTextField.setTitleText("Return type class");
        returnTypeClassTextField.setTitleWidth(120);
        returnTypeClassTextField.setTitleVisible(true);
        returnTypeClassTextField.setEditable(true);
        returnTypeClassTextField.addMouseListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(returnTypeClassTextField, gridBagCon);
        scopeComboBox = new DynaComboBox();
        scopeComboBox.setMandatory(true);
        scopeComboBox.setTitleText("Scope");
        scopeComboBox.setTitleWidth(120);
        scopeComboBox.setTitleVisible(true);
        scopeComboBox.setEditable(false);
        scopeComboBox.setModel(scopeComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 7;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(scopeComboBox, gridBagCon);
        callTypeComboBox = new DynaComboBox();
        callTypeComboBox.setMandatory(false);
        callTypeComboBox.setTitleText("Call type");
        callTypeComboBox.setTitleWidth(120);
        callTypeComboBox.setTitleVisible(true);
        callTypeComboBox.setEditable(false);
        callTypeComboBox.setModel(callTypeComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 8;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(callTypeComboBox, gridBagCon);
        isLeafCheckBox = new JCheckBox("Leaf Action");
        isLeafCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 9;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isLeafCheckBox, gridBagCon);
        isQueryCheckBox = new JCheckBox("Query");
        isQueryCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 10;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isQueryCheckBox, gridBagCon);
        isVisibleCheckBox = new JCheckBox("Visible");
        isVisibleCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 11;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isVisibleCheckBox, gridBagCon);
        titleTextField = new DynaTextField();
        titleTextField.setMandatory(false);
        titleTextField.setTitleText("Title");
        titleTextField.setTitleWidth(120);
        titleTextField.setTitleVisible(true);
        titleTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 12;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(titleTextField, gridBagCon);
        mnemonicTextField = new DynaTextField();
        mnemonicTextField.setMandatory(false);
        mnemonicTextField.setTitleText("Mnemonic");
        mnemonicTextField.setTitleWidth(120);
        mnemonicTextField.setTitleVisible(true);
        mnemonicTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 13;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(mnemonicTextField, gridBagCon);
        acceleratorTextField = new DynaTextField();
        acceleratorTextField.setMandatory(false);
        acceleratorTextField.setTitleText("Accelerator");
        acceleratorTextField.setTitleWidth(120);
        acceleratorTextField.setTitleVisible(true);
        acceleratorTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 14;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(acceleratorTextField, gridBagCon);
        iconTextField = new DynaTextField();
        iconTextField.setMandatory(false);
        iconTextField.setTitleText("Icon");
        iconTextField.setTitleWidth(120);
        iconTextField.setTitleVisible(true);
        iconTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 15;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(iconTextField, gridBagCon);
        iconSelectButton = new JButton();
        iconSelectButton.setMargin(new Insets(0, 0, 0, 0));
        iconSelectButton.setActionCommand("IconSelect");
        iconSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        iconSelectButton.addActionListener(this);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.insets = new Insets(2, 0, 2, 5);
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 15;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(iconSelectButton, gridBagCon);
        icon32TextField = new DynaTextField();
        icon32TextField.setMandatory(false);
        icon32TextField.setTitleText("Icon32");
        icon32TextField.setTitleWidth(120);
        icon32TextField.setTitleVisible(true);
        icon32TextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 16;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(icon32TextField, gridBagCon);
        icon24TextField = new DynaTextField();
        icon24TextField.setMandatory(false);
        icon24TextField.setTitleText("Icon24");
        icon24TextField.setTitleWidth(120);
        icon24TextField.setTitleVisible(true);
        icon24TextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 17;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(icon24TextField, gridBagCon);
        extComboBox = new DynaComboBox();
        extComboBox.setMandatory(false);
        extComboBox.setTitleText("Script Extension");
        extComboBox.setTitleWidth(120);
        extComboBox.setTitleVisible(true);
        extComboBox.setEditable(false);
        extComboBox.setModel(extComboModel);
        extComboBox.setActionCommand("Ext");
        extComboBox.addActionListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 18;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(extComboBox, gridBagCon);
        scriptNameTextField = new DynaTextField();
        scriptNameTextField.setMandatory(false);
        scriptNameTextField.setTitleText("Script Name");
        scriptNameTextField.setTitleWidth(120);
        scriptNameTextField.setTitleVisible(true);
        scriptNameTextField.setEditable(true);
        scriptNameTextField.addMouseListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 19;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(scriptNameTextField, gridBagCon);
        scriptSelectButton = new JButton();
        scriptSelectButton.setText("...");
        scriptSelectButton.setToolTipText("Select Script");
        scriptSelectButton.setActionCommand("Select Script");
        scriptSelectButton.setPreferredSize(new Dimension(20, 20));
        scriptSelectButton.setEnabled(true);
        scriptSelectButton.addActionListener(this);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 19;
        gridBagCon.gridwidth = 1;
        gridBagCon.insets = new Insets(2, 0, 2, 5);
        gridBag.setConstraints(scriptSelectButton, gridBagCon);
        dummyLabel = new JLabel("");
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 20;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(descPanel);
        mainPanel.add(returnTypeComboBox);
        mainPanel.add(returnTypeClassTextField);
        mainPanel.add(scopeComboBox);
        mainPanel.add(callTypeComboBox);
        mainPanel.add(isLeafCheckBox);
        mainPanel.add(isQueryCheckBox);
        mainPanel.add(isVisibleCheckBox);
        mainPanel.add(titleTextField);
        mainPanel.add(mnemonicTextField);
        mainPanel.add(acceleratorTextField);
        mainPanel.add(iconTextField);
        mainPanel.add(iconSelectButton);
        mainPanel.add(icon32TextField);
        mainPanel.add(icon24TextField);
        mainPanel.add(extComboBox);
        mainPanel.add(scriptNameTextField);
        mainPanel.add(scriptSelectButton);
        mainPanel.add(dummyLabel);
        mainScrPane.getViewport().add(mainPanel, null);
        tableScrPane.setViewportView(actionParameterTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        actionParameterSplitPane.setDividerSize(3);
        actionParameterSplitPane.setDividerLocation(200);
        actionParameterSplitPane.setTopComponent(tableScrPane);
        actionParameterSplitPane.setBottomComponent(actionParameterInfo);
        mainTabbedPane.addTab("Action", imageAction, mainScrPane, "Action Information");
        mainTabbedPane.addTab("Action Parameter", imageActionParameter, actionParameterSplitPane, "Action Parameter Information");
        add(mainTabbedPane, "Center");
        typeComboModel.enableDataLoad();
        scopeComboModel.enableDataLoad();
        callTypeComboModel.enableDataLoad();
        extComboModel.enableDataLoad();
    }

    public void makeActionParameterTable()
    {
        columnNames.add("Ouid");
        columnNames.add("Name");
        columnNames.add("Type");
        columnNames.add("Default Value");
        columnNames.add("DOS Field Mapping");
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        actionParameterTable = new Table(associateData, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        actionParameterTable.getTable().addMouseListener(this);
        actionParameterTable.setColumnSequence(new int[] {
            1, 2, 3, 4, 5
        });
        actionParameterTable.setIndexColumn(0);
    }

    public void setTableData(DOSChangeable tableDataDC)
    {
        ArrayList tempAL = new ArrayList(6);
        tempAL.add((String)tableDataDC.get("name"));
        tempAL.add((String)tableDataDC.get("ouid@action"));
        tempAL.add((String)tableDataDC.get("name"));
        tempAL.add((String)tableDataDC.get("typenam"));
        tempAL.add((String)tableDataDC.get("default.value"));
        tempAL.add((String)tableDataDC.get("ouid@field.map"));
        associateData.add(tempAL.clone());
        tempAL.clear();
        actionParameterTable.changeTableData();
        arrayActionParameter.add(tableDataDC);
    }

    public void setTableData(ArrayList tableDataAL)
    {
        ArrayList tempAL = new ArrayList(5);
        DOSChangeable tableDataDC = new DOSChangeable();
        associateData.clear();
        if(tableDataAL != null)
        {
            for(int i = 0; i < tableDataAL.size(); i++)
            {
                tableDataDC = (DOSChangeable)tableDataAL.get(i);
                tempAL.add((String)tableDataDC.get("name"));
                tempAL.add((String)tableDataDC.get("ouid@action"));
                tempAL.add((String)tableDataDC.get("name"));
                tempAL.add((String)tableDataDC.get("typenam"));
                tempAL.add((String)tableDataDC.get("default.value"));
                tempAL.add((String)tableDataDC.get("ouid@field.map"));
                associateData.add(tempAL.clone());
                tempAL.clear();
                arrayActionParameter.add(tableDataDC);
            }

            actionParameterTable.changeTableData();
        }
    }

    public void setActionInfoField(DOSChangeable dosActionInfo)
    {
        setInfoDC = dosActionInfo;
        actionOuid = (String)dosActionInfo.get("ouid");
        ouidTextField.setText((String)dosActionInfo.get("ouid"));
        nameTextField.setText((String)dosActionInfo.get("name"));
        descriptionTextArea.setText((String)dosActionInfo.get("description"));
        typeComboModel.setSelectedItemByOID((Byte)dosActionInfo.get("return.type"));
        if(dosActionInfo.get("is.leaf") != null)
            isLeafCheckBox.setSelected(Utils.getBoolean((Boolean)dosActionInfo.get("is.leaf")));
        else
            isLeafCheckBox.setSelected(false);
        if(dosActionInfo.get("is.query") != null)
            isQueryCheckBox.setSelected(Utils.getBoolean((Boolean)dosActionInfo.get("is.query")));
        else
            isQueryCheckBox.setSelected(false);
        if(dosActionInfo.get("is.visible") != null)
            isVisibleCheckBox.setSelected(Utils.getBoolean((Boolean)dosActionInfo.get("is.visible")));
        else
            isVisibleCheckBox.setSelected(false);
        if(dosActionInfo.get("scope") != null)
            scopeComboModel.setElementAt(((Byte)dosActionInfo.get("scope")).intValue() - 1);
        if(dosActionInfo.get("call.type") != null)
            callTypeComboModel.setElementAt(((Byte)dosActionInfo.get("call.type")).intValue() - 1);
        titleTextField.setText((String)dosActionInfo.get("title"));
        mnemonicTextField.setText((String)dosActionInfo.get("mnemonic"));
        acceleratorTextField.setText((String)dosActionInfo.get("accelerator"));
        iconTextField.setText((String)dosActionInfo.get("icon"));
        icon32TextField.setText((String)dosActionInfo.get("icon32"));
        icon24TextField.setText((String)dosActionInfo.get("icon24"));
        try
        {
            String scriptName = dos.getActionScriptName(actionOuid);
            if(Utils.isNullString(scriptName))
            {
                scriptNameTextField.setEditable(false);
            } else
            {
                scriptNameTextField.setEditable(true);
                java.util.List tmpList = Utils.tokenizeMessage(scriptName, '.');
                extComboModel.setSelectedItemByOID((String)tmpList.get(1));
                scriptNameTextField.setText((String)tmpList.get(0));
            }
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        setTableData((ArrayList)dosActionInfo.get("array$ActionParameter"));
    }

    public void setInformation()
    {
        try
        {
            setInfoDC.put("ouid", ouidTextField.getText());
            setInfoDC.put("name", nameTextField.getText());
            setInfoDC.put("description", descriptionTextArea.getText());
            setInfoDC.put("ouid@class", classOuid);
            setInfoDC.put("return.type", (Byte)typeComboModel.getSelectedOID());
            setInfoDC.put("is.leaf", new Boolean(isLeafCheckBox.isSelected()));
            setInfoDC.put("is.query", new Boolean(isQueryCheckBox.isSelected()));
            setInfoDC.put("is.visible", new Boolean(isVisibleCheckBox.isSelected()));
            setInfoDC.put("title", titleTextField.getText());
            setInfoDC.put("mnemonic", mnemonicTextField.getText());
            setInfoDC.put("accelerator", acceleratorTextField.getText());
            setInfoDC.put("icon", iconTextField.getText());
            setInfoDC.put("icon32", icon32TextField.getText());
            setInfoDC.put("icon24", icon24TextField.getText());
            setInfoDC.put("scope", (Byte)scopeComboModel.getSelectedOID());
            setInfoDC.put("call.type", (Byte)callTypeComboModel.getSelectedOID());
            System.out.println(arrayActionParameter);
            if(arrayActionParameter != null)
                setInfoDC.put("array$ActionParameter", arrayActionParameter);
            dos.setAction(setInfoDC);
            if(Utils.isNullString(scriptNameTextField.getText()))
            {
                dos.removeActionScript(actionOuid);
            } else
            {
                String scriptName = scriptNameTextField.getText() + "." + (String)extComboModel.getSelectedOID();
                dos.setActionScript(actionOuid, scriptName);
            }
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
            setInfoDC.put("return.type", (Byte)typeComboModel.getSelectedOID());
            setInfoDC.put("ouid@class", this.classOuid);
            setInfoDC.put("is.leaf", new Boolean(isLeafCheckBox.isSelected()));
            setInfoDC.put("is.query", new Boolean(isQueryCheckBox.isSelected()));
            setInfoDC.put("is.visible", new Boolean(isVisibleCheckBox.isSelected()));
            tempString = titleTextField.getText();
            if(Utils.isNullString(tempString))
                tempString = (String)setInfoDC.get("name");
            setInfoDC.put("title", tempString);
            setInfoDC.put("mnemonic", mnemonicTextField.getText());
            setInfoDC.put("accelerator", acceleratorTextField.getText());
            setInfoDC.put("icon", iconTextField.getText());
            setInfoDC.put("icon32", icon32TextField.getText());
            setInfoDC.put("icon24", icon24TextField.getText());
            setInfoDC.put("scope", (Byte)scopeComboModel.getSelectedOID());
            setInfoDC.put("call.type", (Byte)callTypeComboModel.getSelectedOID());
            System.out.println(arrayActionParameter);
            String classOuid = dos.createAction(setInfoDC);
            setInfoDC.put("ouid", classOuid);
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
        return !Utils.isNullString(nameTextField.getText()) && typeComboModel.getSelectedOID() != null && scopeComboModel.getSelectedOID() != null;
    }

    public void setClassTypeOID(ArrayList oidAL)
    {
        typeClassOuid = (String)oidAL.get(0);
    }

    public String getActionOuid()
    {
        return actionOuid;
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
        typeAL.add("UTF");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)14));
        typeAL.add("list");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)15));
        typeAL.add("map");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)16));
        typeAL.add("object");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)17));
        typeAL.add("set");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)18));
        typeAL.add("sorted map");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)19));
        typeAL.add("sorted set");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)20));
        typeAL.add("array list");
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
        return returnAL;
    }

    public void setFieldEnabled(boolean isEnable)
    {
        nameTextField.setEditable(isEnable);
        descriptionTextArea.setEditable(isEnable);
        classOuidTextField.setEditable(isEnable);
        returnTypeComboBox.setEnabled(isEnable);
        returnTypeClassTextField.setEditable(isEnable);
        scopeComboBox.setEnabled(isEnable);
        callTypeComboBox.setEnabled(isEnable);
        isLeafCheckBox.setEnabled(isEnable);
        isQueryCheckBox.setEnabled(isEnable);
        isVisibleCheckBox.setEnabled(isEnable);
        titleTextField.setEditable(isEnable);
        mnemonicTextField.setEditable(isEnable);
        acceleratorTextField.setEditable(isEnable);
        extComboBox.setEnabled(isEnable);
        scriptNameTextField.setEditable(isEnable);
        scriptSelectButton.setEnabled(isEnable);
        actionParameterInfo.setFieldEnabled(isEnable);
    }

    public void setScriptNameTextField(String name)
    {
        scriptNameTextField.setText(name);
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

    public ArrayList setLeafComboBox()
    {
        ArrayList leafAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        leafAL.add(new Boolean(true));
        leafAL.add("true");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        leafAL.add(new Boolean(false));
        leafAL.add("false");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        return returnAL;
    }

    public ArrayList setQueryComboBox()
    {
        ArrayList queryAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        queryAL.add(new Boolean(true));
        queryAL.add("true");
        returnAL.add(queryAL.clone());
        queryAL.clear();
        queryAL.add(new Boolean(false));
        queryAL.add("false");
        returnAL.add(queryAL.clone());
        queryAL.clear();
        return returnAL;
    }

    public ArrayList setVisibleComboBox()
    {
        ArrayList visibleAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        visibleAL.add(new Boolean(true));
        visibleAL.add("true");
        returnAL.add(visibleAL.clone());
        visibleAL.clear();
        visibleAL.add(new Boolean(false));
        visibleAL.add("false");
        returnAL.add(visibleAL.clone());
        visibleAL.clear();
        return returnAL;
    }

    public ArrayList setCallTypeComboBox()
    {
        ArrayList callTypeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        callTypeAL.add(new Byte((byte)1));
        callTypeAL.add("guarded");
        returnAL.add(callTypeAL.clone());
        callTypeAL.clear();
        callTypeAL.add(new Byte((byte)2));
        callTypeAL.add("concurrent");
        returnAL.add(callTypeAL.clone());
        callTypeAL.clear();
        return returnAL;
    }

    public ArrayList setExtComboBox()
    {
        ArrayList typeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        typeAL.add("py");
        typeAL.add("Python");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add("tcl");
        typeAL.add("Tcl");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add("java");
        typeAL.add("BeanShell");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        return returnAL;
    }

    public void removeTableRow(String name)
    {
        DOSChangeable selectedDC = new DOSChangeable();
        DOSChangeable tableDataDC = new DOSChangeable();
        int selectedInt = 0;
        for(int i = 0; i < arrayActionParameter.size(); i++)
        {
            selectedDC = (DOSChangeable)arrayActionParameter.get(i);
            if(((String)selectedDC.get("name")).equals(name))
                selectedInt = i;
        }

        arrayActionParameter.remove(selectedInt);
        System.out.println(arrayActionParameter);
        associateData.clear();
        for(int i = 0; i < arrayActionParameter.size(); i++)
        {
            ArrayList tempAL = new ArrayList();
            tableDataDC = (DOSChangeable)arrayActionParameter.get(i);
            tempAL.add((String)tableDataDC.get("name"));
            tempAL.add((String)tableDataDC.get("ouid@action"));
            tempAL.add((String)tableDataDC.get("name"));
            tempAL.add((String)tableDataDC.get("typenam"));
            tempAL.add((String)tableDataDC.get("default.value"));
            tempAL.add((String)tableDataDC.get("ouid@field.map"));
            associateData.add(tempAL.clone());
            tempAL.clear();
        }

        actionParameterTable.changeTableData();
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(returnTypeClassTextField.getTextField()))
        {
            if(e.getClickCount() == 2 && ((String)typeComboModel.getSelectedItem()).equals("object"))
            {
                TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Return Class Type Selection");
                typeClassFrame.setVisible(true);
            }
        } else
        if(source.equals(scriptNameTextField.getTextField()))
        {
            if(e.getClickCount() == 2 && !Utils.isNullString((String)extComboModel.getSelectedItem()))
            {
                String scriptName = scriptNameTextField.getText() + "." + (String)extComboModel.getSelectedOID();
                try
                {
                    dss.downloadFileForce("/script/" + scriptName, "tmp/" + scriptName, null);
                    File newScriptFile = new File("tmp/" + scriptName);
                    Notepad newEditor;
                    if(newScriptFile.exists())
                        newEditor = new Notepad(newScriptFile);
                    else
                        newEditor = new Notepad(null);
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            }
        } else
        if(source.equals(actionParameterTable.getTable()))
        {
            String ouidRow = actionParameterTable.getSelectedOuidRow(actionParameterTable.getTable().getSelectedRow());
            int row = (new Integer(ouidRow)).intValue();
            String selectedDataOuid = (String)((ArrayList)associateData.get(row)).get(0);
            actionParameterInfo.setParameterInfo(selectedDataOuid);
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

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("Select Script"))
        {
            ScriptSelection scriptSelectPanel = new ScriptSelection(this, (String)extComboModel.getSelectedOID());
            scriptSelectPanel.show();
        } else
        if(command.equals("Ext"))
        {
            if(extComboBox.getSelectedIndex() > -1)
            {
                scriptNameTextField.setEditable(true);
                scriptSelectButton.setEnabled(true);
                scriptNameTextField.setText(nameTextField.getText() + "_ACTION");
            } else
            {
                scriptNameTextField.setEditable(false);
                scriptSelectButton.setEnabled(false);
            }
        } else
        if(command.equals("IconSelect"))
            try
            {
                String fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
                JFileChooser fileChooser = new JFileChooser();
                String filePath = "";
                fileChooser.addChoosableFileFilter(new SoftWareFilter(""));
                int returnVal = fileChooser.showDialog(this, "\uC5F4\uAE30");
                if(returnVal == 0)
                {
                    if(!dss.exists("/icons"))
                        dss.makeFolders("/icons");
                    File file = fileChooser.getSelectedFile();
                    filePath = file.getAbsolutePath();
                    if(file == null || !file.isFile())
                        return;
                    String fileTypeId = dss.getFileTypeId(filePath);
                    HashMap fileInfo = new HashMap();
                    fileInfo.put("md$filetype.id", fileTypeId);
                    fileInfo.put("md$des", filePath);
                    String serverPath = "/icons/" + actionOuid + "." + dss.getExtension(fileTypeId);
                    if(ftd == null)
                        ftd = new FileTransferDialog((Frame)parent, false);
                    ftd.setMaximumSize((new Long(file.length())).intValue());
                    UIUtils.setLocationRelativeTo(ftd, this);
                    ftd.show();
                    FileCallBack fileUp = new FileCallBack();
                    upload(serverPath, filePath, fileUp);
                    iconTextField.setText(actionOuid + "." + dss.getExtension(fileTypeId));
                    setInformation();
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
    }

    public void upload(String serverPath, String clientPath, FileTransferCallback callback)
    {
        _serverPath = serverPath;
        _clientPath = clientPath;
        _ftc = callback;
        SwingWorker worker = new SwingWorker() {

            public Object construct()
            {
                boolean isUpload;
                try
                {
                    isUpload = dss.uploadFile(serverPath, clientPath, ftc);
                }
                catch(IIPRequestException e)
                {
                    System.err.println(e.getLocalizedMessage());
                }
                return null;
            }

            public void finished()
            {
                ftd.setVisible(false);
                ftd.dispose();
            }

            String serverPath;
            String clientPath;
            FileTransferCallback ftc;

            
            {
                serverPath = _serverPath;
                clientPath = _clientPath;
                ftc = _ftc;
            }
        };
        worker.start();
    }

    private FileTransferCallback _ftc;
    private String _clientPath;
    private String _serverPath;
    private DOS dos;
    private DSS dss;
    private final int titleTextWidth = 120;
    private final int DIVIDERSIZE = 3;
    private final int DIVIDERLOCATION = 200;
    private BorderLayout ActionInfoBorderLayout;
    private BoxLayout packageInfoLayout;
    private Object parent;
    private JTabbedPane mainTabbedPane;
    private JScrollPane mainScrPane;
    private JScrollPane tableScrPane;
    private JScrollPane descScrPane;
    private JPanel mainPanel;
    private JPanel descPanel;
    private JSplitPane actionParameterSplitPane;
    private JTextArea descriptionTextArea;
    private JLabel descriptionLabel;
    private JLabel dummyLabel;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private DynaTextField descriptionTextField;
    private DynaTextField classOuidTextField;
    private DynaComboBox returnTypeComboBox;
    private DynaTextField returnTypeClassTextField;
    private DynaComboBox scopeComboBox;
    private DynaComboBox callTypeComboBox;
    private JCheckBox isLeafCheckBox;
    private JCheckBox isQueryCheckBox;
    private JCheckBox isVisibleCheckBox;
    private JButton iconSelectButton;
    private DynaTextField titleTextField;
    private DynaTextField mnemonicTextField;
    private DynaTextField acceleratorTextField;
    private DynaTextField iconTextField;
    private DynaTextField icon32TextField;
    private DynaTextField icon24TextField;
    private DynaComboBox extComboBox;
    private DynaTextField scriptNameTextField;
    private JButton scriptSelectButton;
    private ArrayList associateData;
    private ArrayList columnNames;
    private ArrayList columnWidths;
    private ActionParameterInformation actionParameterInfo;
    private ImageIcon imageAction;
    private ImageIcon imageActionParameter;
    private Table actionParameterTable;
    private String actionOuid;
    private String classOuid;
    private String typeClassOuid;
    private DynaComboTypeInstance dComboType;
    private DynaComboScopeInstance dComboScope;
    private DynaComboCallTypeInstance dComboCallType;
    private DynaComboExtInstance dComboExt;
    private DynaComboBoxModel typeComboModel;
    private DynaComboBoxModel scopeComboModel;
    private DynaComboBoxModel callTypeComboModel;
    private DynaComboBoxModel extComboModel;
    private ArrayList arrayActionParameter;
    private FileTransferDialog ftd;
    private DOSChangeable setInfoDC;
    public static final byte DATATYPE_BOOLEAN = 1;
    public static final byte DATATYPE_BYTE = 2;
    public static final byte DATATYPE_CHAR = 3;
    public static final byte DATATYPE_DOUBLE = 4;
    public static final byte DATATYPE_FLOAT = 5;
    public static final byte DATATYPE_INT = 6;
    public static final byte DATATYPE_LONG = 7;
    public static final byte DATATYPE_SHORT = 8;
    public static final byte DATATYPE_UTF = 13;
    public static final byte DATATYPE_LIST = 14;
    public static final byte DATATYPE_MAP = 15;
    public static final byte DATATYPE_OBJECT = 16;
    public static final byte DATATYPE_SET = 17;
    public static final byte DATATYPE_SORTED_MAP = 18;
    public static final byte DATATYPE_SORTED_SET = 19;
    public static final byte DATATYPE_ARRAY_LIST = 20;
    public static final byte DATATYPE_DATETIME = 21;
    public static final byte DATATYPE_DATE = 22;
    public static final byte DATATYPE_TIME = 23;
    public static final byte SCOPE_CLASSIFIER = 1;
    public static final byte SCOPE_INSTANCE = 2;
    public static final byte CALL_GUARDED = 1;
    public static final byte CALL_CONCURRENT = 2;





}
