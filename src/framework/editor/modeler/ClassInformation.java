// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClassInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.client.FileTransferDialog;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, AssociationInformation, EventInformation, NumberingRuleInformation, 
//            SuperClassSelection

public class ClassInformation extends JPanel
    implements MouseListener, ActionListener
{
    class DynaComboXAInstance extends DynaComboBoxDataLoader
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
            return setXAComboBox();
        }

        DynaComboXAInstance()
        {
        }
    }

    class DynaComboDataSourceIDInstance extends DynaComboBoxDataLoader
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
            ArrayList listDataArray = new ArrayList();
            ArrayList tempArray = new ArrayList();
            ArrayList returnArray = new ArrayList();
            try
            {
                listDataArray = dtm.listDatasourceId();
                for(int i = 0; i < listDataArray.size(); i++)
                {
                    tempArray.add((String)listDataArray.get(i));
                    tempArray.add((String)listDataArray.get(i));
                    returnArray.add(tempArray.clone());
                    tempArray.clear();
                }

            }
            catch(IIPRequestException iiprequestexception) { }
            return returnArray;
        }

        DynaComboDataSourceIDInstance()
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


    public ClassInformation(Object parentFrame, String packageOuid)
    {
        imageClass = new ImageIcon("icons/Class.gif");
        imageAssociation = new ImageIcon("icons/Association.gif");
        imageEvent = new ImageIcon("icons/execution.gif");
        imageNumberingRule = new ImageIcon("icons/Properties16.gif");
        dos = null;
        dtm = null;
        dss = null;
        this.packageOuid = "";
        classOuid = "";
        superClassAL = null;
        dComboXA = new DynaComboXAInstance();
        dComboDataSourceID = new DynaComboDataSourceIDInstance();
        xaComboModel = new DynaComboBoxModel(dComboXA);
        dataSourceIDComboModel = new DynaComboBoxModel(dComboDataSourceID);
        associateData = new ArrayList();
        columnNames = new ArrayList();
        columnWidths = new ArrayList();
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        ftd = null;
        setInfoDC = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            dtm = (DTM)ObjectModelingConstruction.dfw.getServiceInstance("DF30DTM1");
            dss = (DSS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DSS1");
            this.packageOuid = packageOuid;
            parent = parentFrame;
            associationInfo = new AssociationInformation(this);
            eventInfo = new EventInformation(this);
            numberingRuleInfo = new NumberingRuleInformation(this);
            makeAssociationTable();
            initialize();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void initialize()
    {
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        superClassAL = new ArrayList();
        mainPanel = new JPanel();
        associateSplitPane = new JSplitPane(0);
        eventScrPane = UIFactory.createStrippedScrollPane(null);
        numberingruleScrPane = UIFactory.createStrippedScrollPane(null);
        classInfoBorderLayout = new BorderLayout();
        setLayout(classInfoBorderLayout);
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
        superClassTextField = new DynaTextField();
        superClassTextField.setMandatory(true);
        superClassTextField.setTitleText("Super Class");
        superClassTextField.setTitleWidth(120);
        superClassTextField.setTitleVisible(true);
        superClassTextField.setEditable(false);
        superClassTextField.addMouseListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(superClassTextField, gridBagCon);
        ouidTextField = new DynaTextField();
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setTitleWidth(120);
        ouidTextField.setTitleVisible(true);
        ouidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
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
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(nameTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.20000000000000001D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(descPanel, gridBagCon);
        packageOuidTextField = new DynaTextField();
        packageOuidTextField.setMandatory(true);
        packageOuidTextField.setTitleText("Package Ouid");
        packageOuidTextField.setTitleWidth(120);
        packageOuidTextField.setTitleVisible(true);
        packageOuidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(packageOuidTextField, gridBagCon);
        isRootCheckBox = new JCheckBox("Root Class");
        isRootCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isRootCheckBox, gridBagCon);
        isLeafCheckBox = new JCheckBox("Leaf Class");
        isLeafCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 7;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isLeafCheckBox, gridBagCon);
        abstractCheckBox = new JCheckBox("Abstract Class");
        abstractCheckBox.setMargin(new Insets(0, 125, 0, 0));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 8;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(abstractCheckBox, gridBagCon);
        capacityTextField = new DynaTextField();
        capacityTextField.setMandatory(false);
        capacityTextField.setTitleText("Capacity");
        capacityTextField.setTitleWidth(120);
        capacityTextField.setTitleVisible(true);
        capacityTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 9;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(capacityTextField, gridBagCon);
        codeTextField = new DynaTextField();
        codeTextField.setMandatory(false);
        codeTextField.setTitleText("Code");
        codeTextField.setTitleWidth(120);
        codeTextField.setTitleVisible(true);
        codeTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 9;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(codeTextField, gridBagCon);
        titleTextField = new DynaTextField();
        titleTextField.setMandatory(false);
        titleTextField.setTitleText("Title");
        titleTextField.setTitleWidth(120);
        titleTextField.setTitleVisible(true);
        titleTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 10;
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
        gridBagCon.gridy = 11;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(tooltipTextField, gridBagCon);
        iconTextField = new DynaTextField();
        iconTextField.setMandatory(false);
        iconTextField.setTitleText("Icon");
        iconTextField.setTitleWidth(120);
        iconTextField.setTitleVisible(true);
        iconTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 12;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(iconTextField, gridBagCon);
        iconSelectButton = new JButton();
        iconSelectButton.setMargin(new Insets(0, 0, 0, 0));
        iconSelectButton.setActionCommand("IconSelect");
        iconSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        iconSelectButton.addActionListener(this);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.insets = new Insets(1, 0, 0, 5);
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 12;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(iconSelectButton, gridBagCon);
        dataSourceIdComboBox = new DynaComboBox();
        dataSourceIdComboBox.setMandatory(false);
        dataSourceIdComboBox.setTitleText("Data source");
        dataSourceIdComboBox.setTitleWidth(120);
        dataSourceIdComboBox.setTitleVisible(true);
        dataSourceIdComboBox.setEditable(false);
        dataSourceIdComboBox.setModel(dataSourceIDComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 13;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(dataSourceIdComboBox, gridBagCon);
        isXaObjectComboBox = new DynaComboBox();
        isXaObjectComboBox.setMandatory(false);
        isXaObjectComboBox.setTitleText("is XA object");
        isXaObjectComboBox.setTitleWidth(120);
        isXaObjectComboBox.setTitleVisible(true);
        isXaObjectComboBox.setEditable(false);
        isXaObjectComboBox.setModel(xaComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 15;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(isXaObjectComboBox, gridBagCon);
        versionableCheckBox = new JCheckBox("Version Control");
        versionableCheckBox.setMargin(new Insets(0, 125, 0, 0));
        versionableCheckBox.setEnabled(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 16;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(versionableCheckBox, gridBagCon);
        fileControlCheckBox = new JCheckBox("File Control");
        fileControlCheckBox.setMargin(new Insets(0, 125, 0, 0));
        fileControlCheckBox.setEnabled(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 17;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(fileControlCheckBox, gridBagCon);
        uniqNumberCheckBox = new JCheckBox("Use Unique Number");
        uniqNumberCheckBox.setMargin(new Insets(0, 125, 0, 0));
        uniqNumberCheckBox.setEnabled(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 18;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(uniqNumberCheckBox, gridBagCon);
        dummyLabel = new JLabel("");
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 19;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(superClassTextField);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(descPanel);
        mainPanel.add(packageOuidTextField);
        mainPanel.add(isRootCheckBox);
        mainPanel.add(isLeafCheckBox);
        mainPanel.add(abstractCheckBox);
        mainPanel.add(codeTextField);
        mainPanel.add(titleTextField);
        mainPanel.add(tooltipTextField);
        mainPanel.add(iconTextField);
        mainPanel.add(iconSelectButton);
        mainPanel.add(dataSourceIdComboBox);
        mainPanel.add(versionableCheckBox);
        mainPanel.add(fileControlCheckBox);
        mainPanel.add(uniqNumberCheckBox);
        mainPanel.add(dummyLabel);
        mainScrPane.getViewport().add(mainPanel, null);
        tableScrPane.setViewportView(associateTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        associateSplitPane.setDividerSize(3);
        associateSplitPane.setDividerLocation(100);
        associateSplitPane.setTopComponent(tableScrPane);
        associateSplitPane.setBottomComponent(associationInfo);
        eventScrPane.getViewport().add(eventInfo, null);
        numberingruleScrPane.getViewport().add(numberingRuleInfo, null);
        mainTabbedPane.addTab("Class", imageClass, mainScrPane, "Class Information");
        mainTabbedPane.addTab("Reference", imageAssociation, associateSplitPane, "Association Information");
        mainTabbedPane.addTab("Event", imageEvent, eventScrPane, "Event Information");
        mainTabbedPane.addTab("Numbering Rule", imageNumberingRule, numberingruleScrPane, "Nubering Rule Information");
        add(mainTabbedPane, "Center");
        xaComboModel.enableDataLoad();
        dataSourceIDComboModel.enableDataLoad();
    }

    public void setOuidIntoEvent(String ouid)
    {
        eventInfo.setOuid(ouid);
    }

    public void makeAssociationTable()
    {
        columnNames.add("Ouid");
        columnNames.add("Name");
        columnNames.add("Description");
        columnNames.add("End1");
        columnNames.add("End2");
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        associateTable = new Table(associateData, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        associateTable.getTable().addMouseListener(this);
        associateTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        associateTable.setIndexColumn(0);
    }

    public void setOuidIntoNumberingRule(String ouid)
    {
        numberingRuleInfo.setOuid(ouid);
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(superClassTextField.getTextField()))
        {
            if(e.getClickCount() == 2)
            {
                SuperClassSelection superClassFrame = new SuperClassSelection(this, superClassAL, "Super Class Selection");
                superClassFrame.setVisible(true);
            }
        } else
        if(source.equals(associateTable.getTable()))
        {
            int selectRow = associateTable.getTable().getSelectedRow();
            String ouidRow = associateTable.getSelectedOuidRow(selectRow);
            int row = (new Integer(ouidRow)).intValue();
            String selectedOuid = (String)((ArrayList)associateData.get(row)).get(0);
            associationInfo.setAssociationInfoField(selectedOuid);
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

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
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
                    String serverPath = "/icons/" + classOuid + "." + dss.getExtension(fileTypeId);
                    if(ftd == null)
                        ftd = new FileTransferDialog((Frame)parent, false);
                    ftd.setMaximumSize((new Long(file.length())).intValue());
                    UIUtils.setLocationRelativeTo(ftd, this);
                    ftd.show();
                    FileCallBack fileUp = new FileCallBack();
                    upload(serverPath, filePath, fileUp);
                    iconTextField.setText(classOuid + "." + dss.getExtension(fileTypeId));
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

    public void setAssociationTable(ArrayList associationAL)
    {
        associateData.clear();
        for(int i = 0; i < associationAL.size(); i++)
        {
            ArrayList tempAL = new ArrayList(5);
            tempAL.add(((DOSChangeable)associationAL.get(i)).get("ouid"));
            tempAL.add(((DOSChangeable)associationAL.get(i)).get("name"));
            tempAL.add(((DOSChangeable)associationAL.get(i)).get("description"));
            tempAL.add(((DOSChangeable)associationAL.get(i)).get("end1.name"));
            tempAL.add(((DOSChangeable)associationAL.get(i)).get("end2.name"));
            associateData.add(tempAL.clone());
            tempAL.clear();
        }

        associateTable.changeTableData();
    }

    public void setClassInfoField(DOSChangeable dosClassInfo)
    {
        setInfoDC = dosClassInfo;
        xaComboModel.setElementAt(-1);
        dataSourceIDComboModel.setElementAt(-1);
        classOuid = (String)dosClassInfo.get("ouid");
        ArrayList superClassListAll = null;
        String superClassName = "";
        String superClassList = "";
        superClassAL = (ArrayList)dosClassInfo.get("array$ouid.superclass");
        if(superClassAL != null)
        {
            for(int i = 0; i < superClassAL.size(); i++)
                try
                {
                    superClassName = (String)dos.getClass((String)superClassAL.get(i)).get("name");
                    if(i == superClassAL.size() - 1)
                        superClassList = superClassList + superClassName;
                    else
                        superClassList = superClassList + superClassName + " , ";
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }

        }
        superClassTextField.setText(superClassList);
        ouidTextField.setText((String)dosClassInfo.get("ouid"));
        nameTextField.setText((String)dosClassInfo.get("name"));
        descriptionTextArea.setText((String)dosClassInfo.get("description"));
        packageOuidTextField.setText((String)dosClassInfo.get("ouid@package"));
        if(dosClassInfo.get("is.root") != null)
            isRootCheckBox.setSelected(Utils.getBoolean((Boolean)dosClassInfo.get("is.root")));
        else
            isRootCheckBox.setSelected(false);
        if(dosClassInfo.get("is.leaf") != null)
            isLeafCheckBox.setSelected(Utils.getBoolean((Boolean)dosClassInfo.get("is.leaf")));
        else
            isLeafCheckBox.setSelected(false);
        Number capacity = (Number)dosClassInfo.get("capacity");
        if(capacity != null)
            capacityTextField.setText(capacity.toString());
        else
            capacityTextField.setText("");
        codeTextField.setText((String)dosClassInfo.get("code"));
        titleTextField.setText((String)dosClassInfo.get("title"));
        tooltipTextField.setText((String)dosClassInfo.get("tooltip"));
        iconTextField.setText((String)dosClassInfo.get("icon"));
        dataSourceIDComboModel.setSelectedItem((String)dosClassInfo.get("datasource.id"));
        if(dosClassInfo.get("is.xa.object") != null)
            xaComboModel.setSelectedItem(Utils.getBoolean((Boolean)dosClassInfo.get("is.xa.object")) ? "true" : "false");
        if(dosClassInfo.get("is.versionable") != null)
            versionableCheckBox.setSelected(Utils.getBoolean((Boolean)dosClassInfo.get("is.versionable")));
        else
            versionableCheckBox.setSelected(false);
        if(dosClassInfo.get("is.filecontrol") != null)
            fileControlCheckBox.setSelected(Utils.getBoolean((Boolean)dosClassInfo.get("is.filecontrol")));
        else
            fileControlCheckBox.setSelected(false);
        if(dosClassInfo.get("is.abstract") != null)
            abstractCheckBox.setSelected(Utils.getBoolean((Boolean)dosClassInfo.get("is.abstract")));
        else
            abstractCheckBox.setSelected(false);
        try
        {
            superClassListAll = dos.listAllSuperClassOuid((String)dosClassInfo.get("ouid"));
            if(superClassListAll == null)
                superClassListAll = new ArrayList();
            superClassListAll.add(0, classOuid);
            DOSChangeable dosClass = null;
            uniqNumberCheckBox.setEnabled(false);
            uniqNumberCheckBox.setSelected(false);
            for(int i = 0; i < superClassListAll.size(); i++)
            {
                dosClass = dos.getClass((String)superClassListAll.get(i));
                if(!"FObject".equals((String)dosClass.get("name")))
                    continue;
                uniqNumberCheckBox.setEnabled(true);
                if(dosClassInfo.get("use.unique.number") != null)
                    uniqNumberCheckBox.setSelected(Utils.getBoolean((Boolean)dosClassInfo.get("use.unique.number")));
                else
                    uniqNumberCheckBox.setSelected(false);
                break;
            }

        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setInformation()
    {
        try
        {
            setInfoDC.put("ouid", ouidTextField.getText());
            setInfoDC.put("name", nameTextField.getText());
            setInfoDC.put("description", descriptionTextArea.getText());
            setInfoDC.put("ouid@package", packageOuidTextField.getText());
            setInfoDC.put("is.root", new Boolean(isRootCheckBox.isSelected()));
            setInfoDC.put("is.leaf", new Boolean(isLeafCheckBox.isSelected()));
            setInfoDC.put("is.abstract", new Boolean(abstractCheckBox.isSelected()));
            setInfoDC.put("capacity", Utils.getInteger(capacityTextField.getText()));
            setInfoDC.put("code", codeTextField.getText());
            setInfoDC.put("title", titleTextField.getText());
            setInfoDC.put("tooltip", tooltipTextField.getText());
            setInfoDC.put("icon", iconTextField.getText());
            setInfoDC.put("datasource.id", (String)dataSourceIDComboModel.getSelectedOID());
            setInfoDC.put("is.xa.object", (Boolean)xaComboModel.getSelectedOID());
            setInfoDC.put("is.versionable", new Boolean(versionableCheckBox.isSelected()));
            setInfoDC.put("is.filecontrol", new Boolean(fileControlCheckBox.isSelected()));
            setInfoDC.put("array$ouid.superclass", superClassAL);
            setInfoDC.put("use.unique.number", new Boolean(uniqNumberCheckBox.isSelected()));
            dos.setClass(setInfoDC);
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
            setInfoDC.put("ouid@package", packageOuid);
            setInfoDC.put("is.root", new Boolean(isRootCheckBox.isSelected()));
            setInfoDC.put("is.leaf", new Boolean(isLeafCheckBox.isSelected()));
            setInfoDC.put("is.abstract", new Boolean(abstractCheckBox.isSelected()));
            setInfoDC.put("capacity", Utils.getInteger(capacityTextField.getText()));
            tempString = codeTextField.getText();
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
            setInfoDC.put("tooltip", tooltipTextField.getText());
            setInfoDC.put("icon", iconTextField.getText());
            setInfoDC.put("datasource.id", (String)dataSourceIDComboModel.getSelectedOID());
            setInfoDC.put("is.xa.object", (Boolean)xaComboModel.getSelectedOID());
            setInfoDC.put("is.versionable", new Boolean(versionableCheckBox.isSelected()));
            setInfoDC.put("is.filecontrol", new Boolean(fileControlCheckBox.isSelected()));
            setInfoDC.put("array$ouid.superclass", superClassAL);
            setInfoDC.put("use.unique.number", new Boolean(uniqNumberCheckBox.isSelected()));
            String classOuid = dos.createClass(setInfoDC);
            setInfoDC.put("ouid", classOuid);
            return setInfoDC;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        return null;
    }

    public void setSuperClassAL(ArrayList superClass)
    {
        superClassAL = superClass;
    }

    public String getClassOuid()
    {
        return classOuid;
    }

    public ArrayList setXAComboBox()
    {
        ArrayList xaObjectAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        xaObjectAL.add(new Boolean(true));
        xaObjectAL.add("true");
        returnAL.add(xaObjectAL.clone());
        xaObjectAL.clear();
        xaObjectAL.add(new Boolean(false));
        xaObjectAL.add("false");
        returnAL.add(xaObjectAL.clone());
        xaObjectAL.clear();
        return returnAL;
    }

    private final int titleTextWidth = 120;
    private final int DIVIDERSIZE = 3;
    private final int DIVIDERLOCATION = 100;
    private BorderLayout classInfoBorderLayout;
    public Object parent;
    private JTabbedPane mainTabbedPane;
    private JScrollPane mainScrPane;
    private JScrollPane tableScrPane;
    private JScrollPane descScrPane;
    private JPanel mainPanel;
    private JPanel descPanel;
    private JSplitPane associateSplitPane;
    private JScrollPane eventScrPane;
    private JScrollPane numberingruleScrPane;
    private JLabel descriptionLabel;
    private JLabel dummyLabel;
    private DynaTextField superClassTextField;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private DynaTextField packageOuidTextField;
    private JCheckBox isRootCheckBox;
    private JCheckBox isLeafCheckBox;
    private DynaTextField capacityTextField;
    private DynaTextField codeTextField;
    private DynaTextField titleTextField;
    private DynaTextField tooltipTextField;
    private DynaTextField iconTextField;
    private DynaComboBox dataSourceIdComboBox;
    private DynaComboBox isXaObjectComboBox;
    private JCheckBox versionableCheckBox;
    private JCheckBox fileControlCheckBox;
    private JCheckBox abstractCheckBox;
    private JCheckBox uniqNumberCheckBox;
    private JButton iconSelectButton;
    private JTextArea descriptionTextArea;
    private AssociationInformation associationInfo;
    private EventInformation eventInfo;
    private NumberingRuleInformation numberingRuleInfo;
    private ImageIcon imageClass;
    private ImageIcon imageAssociation;
    private ImageIcon imageEvent;
    private ImageIcon imageNumberingRule;
    private Table associateTable;
    private DOS dos;
    private DTM dtm;
    private DSS dss;
    private String packageOuid;
    private String classOuid;
    private ArrayList superClassAL;
    private DynaComboBoxDataLoader dComboXA;
    private DynaComboBoxDataLoader dComboDataSourceID;
    private DynaComboBoxModel xaComboModel;
    private DynaComboBoxModel dataSourceIDComboModel;
    private ArrayList associateData;
    private ArrayList columnNames;
    private ArrayList columnWidths;
    private String _serverPath;
    private String _clientPath;
    private FileTransferCallback _ftc;
    private FileTransferDialog ftd;
    private DOSChangeable setInfoDC;






}
