// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AuthoritySearch.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, LogIn, ClassSelectDialog, 
//            SmallSearchDialog, UserObjectSingleSelect

public class AuthoritySearch extends JFrame
{
    class PermissionComboDataLoader extends DynaComboBoxDataLoader
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
            return setPermissionModel();
        }

        PermissionComboDataLoader()
        {
        }
    }

    class ScopeComboDataLoader extends DynaComboBoxDataLoader
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
            return setScopeModel();
        }

        ScopeComboDataLoader()
        {
        }
    }

    class TypeComboDataLoader extends DynaComboBoxDataLoader
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
            return setTypeModel();
        }

        TypeComboDataLoader()
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

    class ListenerCommon extends WindowAdapter
        implements ActionListener, MouseListener, ItemListener
    {

        public void windowOpened(WindowEvent windowevent)
        {
        }

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
            String command = e.getActionCommand();
            if(command.equals("Search"))
                searchButton_actionPerformed(e);
            else
            if(command.equals("Clear"))
                clearButton_actionPerformed(e);
            else
            if(command.equals("SelectClass"))
                classSelectButton_actionPerformed(e);
            else
            if(command.equals("SelectObject"))
                objectSelectButton_actionPerformed(e);
            else
            if(command.equals("SelectPrincipal"))
                principalSelectButton_actionPerformed(e);
            else
            if(command.equals("All"))
            {
                clearButton_actionPerformed(e);
                includeInheritChkBox.setEnabled(false);
                classSelectButton.setEnabled(true);
                permissionComboBox.setEnabled(true);
                objectSelectButton.setEnabled(true);
                scopeComboBox.setEnabled(true);
                principalSelectButton.setEnabled(true);
                typeComboBox.setEnabled(true);
                grantableComboBox.setEnabled(true);
                allSplitPane.setDividerLocation(250);
                allSplitPane.setRightComponent(rightScrollPane);
            } else
            if(command.equals("Classified"))
            {
                clearButton_actionPerformed(e);
                includeInheritChkBox.setEnabled(true);
                classSelectButton.setEnabled(true);
                permissionComboBox.setEnabled(false);
                objectSelectButton.setEnabled(true);
                scopeComboBox.setEnabled(false);
                principalSelectButton.setEnabled(false);
                typeComboBox.setEnabled(false);
                grantableComboBox.setEnabled(false);
                allSplitPane.setDividerLocation(250);
                allSplitPane.setRightComponent(classifiedPanel);
            } else
            if(command.equals("Principal"))
            {
                clearButton_actionPerformed(e);
                classSelectButton.setEnabled(false);
                permissionComboBox.setEnabled(false);
                objectSelectButton.setEnabled(false);
                scopeComboBox.setEnabled(true);
                principalSelectButton.setEnabled(true);
                typeComboBox.setEnabled(false);
                grantableComboBox.setEnabled(false);
                allSplitPane.setDividerLocation(250);
                allSplitPane.setRightComponent(classifiedPanel);
            }
        }

        public void itemStateChanged(ItemEvent e)
        {
            Object source = e.getSource();
            if(source == scopeComboBox.getComboBox())
            {
                principalID = "";
                principalName = "";
                principalTextField.setText("");
                searchConditionPanel.updateUI();
            }
        }

        ListenerCommon()
        {
        }
    }

    class PrincipalTable extends Table
    {

        public void valueChanged(ListSelectionEvent e)
        {
            super.valueChanged(e);
            principalTable_valueChanged(this, e);
        }

        public PrincipalTable()
        {
        }

        public PrincipalTable(ArrayList dataList, ArrayList columnNameList, ArrayList columnWidth, int selection, int tableWidth)
        {
            super(dataList, columnNameList, columnWidth, selection, tableWidth);
        }

        public PrincipalTable(ArrayList datalist, ArrayList columnNamelist, ArrayList columnWidth, int Selection)
        {
            super(datalist, columnNamelist, columnWidth, Selection);
        }
    }


    public AuthoritySearch(String modelOuid)
    {
        super(DynaMOAD.getMSRString("WRD_0094", "Autority Search", 3));
        handCursor = new Cursor(12);
        buttonToolBar = null;
        searchButton = null;
        clearButton = null;
        optionPanel = null;
        buttonGroup = null;
        allRadioButton = null;
        objectRadioButton = null;
        includeInheritChkBox = null;
        allSplitPane = null;
        leftScrollPane = null;
        rightScrollPane = null;
        leftPanel = null;
        searchConditionPanel = null;
        searchResultTable = null;
        classifiedPanel = null;
        princiaplTableScrPane = null;
        principalTable = null;
        permissionPanel = null;
        namePanel = null;
        boxPanel = null;
        nameLabel = null;
        arrayLabel = null;
        allowLabel = null;
        aArrayCheckBox = null;
        denyLabel = null;
        dArrayCheckBox = null;
        grantLabel = null;
        gArrayCheckBox = null;
        listenerCommon = null;
        acl = DynaMOAD.acl;
        aus = DynaMOAD.aus;
        dos = DynaMOAD.dos;
        column = null;
        tableData = null;
        principalTableData = null;
        scopeMap = null;
        permissionComboDataLoader = new PermissionComboDataLoader();
        permissionComboModel = new DynaComboBoxModel(permissionComboDataLoader);
        scopeComboDataLoader = new ScopeComboDataLoader();
        scopeComboModel = new DynaComboBoxModel(scopeComboDataLoader);
        typeComboDataLoader = new TypeComboDataLoader();
        typeComboModel = new DynaComboBoxModel(typeComboDataLoader);
        grantableComboDataLoader = new DynaComboBooleanInstance();
        grantableComboModel = new DynaComboBoxModel(grantableComboDataLoader);
        this.modelOuid = "";
        classOuid = "";
        objectOuid = "";
        principalID = "";
        principalName = "";
        this.modelOuid = modelOuid;
        listenerCommon = new ListenerCommon();
        initialize();
    }

    private void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        addWindowListener(listenerCommon);
        searchButton = new JButton();
        searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "search", 3));
        searchButton.setActionCommand("Search");
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.addActionListener(listenerCommon);
        clearButton = new JButton();
        clearButton.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "Clear", 3));
        clearButton.setActionCommand("Clear");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.addActionListener(listenerCommon);
        buttonToolBar = new JToolBar();
        buttonToolBar.add(searchButton);
        buttonToolBar.add(clearButton);
        optionPanel = new JPanel();
        optionPanel.setBorder(UIManagement.borderPanel);
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.setLayout(new BoxLayout(optionPanel, 1));
        allRadioButton = new JRadioButton(DynaMOAD.getMSRString("WRD_0095", "All", 3));
        allRadioButton.setBackground(UIManagement.panelBackGround);
        allRadioButton.setActionCommand("All");
        allRadioButton.addActionListener(listenerCommon);
        allRadioButton.setSelected(true);
        objectRadioButton = new JRadioButton(DynaMOAD.getMSRString("WRD_0096", "Classified", 3));
        objectRadioButton.setBackground(UIManagement.panelBackGround);
        objectRadioButton.setActionCommand("Classified");
        objectRadioButton.addActionListener(listenerCommon);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(allRadioButton);
        buttonGroup.add(objectRadioButton);
        includeInheritChkBox = new JCheckBox(DynaMOAD.getMSRString("WRD_0097", "include inherited permission", 3));
        includeInheritChkBox.setBackground(UIManagement.panelBackGround);
        includeInheritChkBox.setSelected(false);
        includeInheritChkBox.setEnabled(false);
        optionPanel.add(allRadioButton);
        optionPanel.add(objectRadioButton);
        optionPanel.add(new JLabel(""));
        optionPanel.add(includeInheritChkBox);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        searchConditionPanel = new JPanel();
        searchConditionPanel.setLayout(gridBag);
        searchConditionPanel.setBackground(UIManagement.panelBackGround);
        gridBagCon.insets = new Insets(10, 10, 20, 10);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 3;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(optionPanel, gridBagCon);
        classTextField = new DynaTextField();
        classTextField.setMandatory(false);
        classTextField.setTitleText(DynaMOAD.getMSRString("WRD_0028", "Class", 3));
        classTextField.setTitleWidth(80);
        classTextField.setTitleVisible(true);
        classTextField.setEditable(false);
        classTextField.setBackground(UIManagement.panelBackGround);
        classTextField.getTextField().setBorder(UIManagement.borderTextBoxNotEditable);
        gridBagCon.insets = new Insets(10, 10, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(classTextField, gridBagCon);
        classSelectButton = new JButton();
        classSelectButton.setActionCommand("SelectClass");
        classSelectButton.setMargin(new Insets(0, 0, 0, 0));
        classSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/SmallSearchButton.gif")));
        classSelectButton.addActionListener(listenerCommon);
        classSelectButton.setBorder(UIManagement.borderGroupTitle);
        gridBagCon.insets = new Insets(10, 0, 2, 12);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(classSelectButton, gridBagCon);
        objectTextField = new DynaTextField();
        objectTextField.setMandatory(false);
        objectTextField.setTitleText(DynaMOAD.getMSRString("WRD_0099", "Object", 3));
        objectTextField.setTitleWidth(80);
        objectTextField.setTitleVisible(true);
        objectTextField.setEditable(false);
        objectTextField.setBackground(UIManagement.panelBackGround);
        objectTextField.getTextField().setBorder(UIManagement.borderTextBoxNotEditable);
        gridBagCon.insets = new Insets(0, 10, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(objectTextField, gridBagCon);
        objectSelectButton = new JButton();
        objectSelectButton.setActionCommand("SelectObject");
        objectSelectButton.setMargin(new Insets(0, 0, 0, 0));
        objectSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/SmallSearchButton.gif")));
        objectSelectButton.addActionListener(listenerCommon);
        objectSelectButton.setBorder(UIManagement.borderGroupTitle);
        gridBagCon.insets = new Insets(2, 0, 2, 12);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(objectSelectButton, gridBagCon);
        permissionComboBox = new DynaComboBox();
        permissionComboBox.setMandatory(false);
        permissionComboBox.setTitleText(DynaMOAD.getMSRString("WRD_0051", "Permission", 3));
        permissionComboBox.setTitleWidth(80);
        permissionComboBox.setTitleVisible(true);
        permissionComboBox.setEditable(false);
        permissionComboBox.setBackground(UIManagement.panelBackGround);
        permissionComboBox.getComboBox().setBorder(UIManagement.borderTextBoxNotEditable);
        permissionComboBox.getComboBox().setBackground(UIManagement.panelBackGround);
        permissionComboBox.setModel(permissionComboModel);
        permissionComboModel.enableDataLoad();
        gridBagCon.insets = new Insets(0, 10, 0, 10);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(permissionComboBox, gridBagCon);
        scopeComboBox = new DynaComboBox();
        scopeComboBox.setMandatory(false);
        scopeComboBox.setTitleText(DynaMOAD.getMSRString("WRD_0098", "Scope", 3));
        scopeComboBox.setTitleWidth(80);
        scopeComboBox.setTitleVisible(true);
        scopeComboBox.setEditable(false);
        scopeComboBox.setBackground(UIManagement.panelBackGround);
        scopeComboBox.getComboBox().setBorder(UIManagement.borderTextBoxNotEditable);
        scopeComboBox.getComboBox().setBackground(UIManagement.panelBackGround);
        scopeComboBox.addItemListener(listenerCommon);
        scopeComboBox.setModel(scopeComboModel);
        scopeComboModel.enableDataLoad();
        gridBagCon.insets = new Insets(0, 10, 0, 10);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(scopeComboBox, gridBagCon);
        principalTextField = new DynaTextField();
        principalTextField.setMandatory(false);
        principalTextField.setTitleText(DynaMOAD.getMSRString("WRD_0100", "Principal", 3));
        principalTextField.setTitleWidth(80);
        principalTextField.setTitleVisible(true);
        principalTextField.setEditable(false);
        principalTextField.setBackground(UIManagement.panelBackGround);
        principalTextField.getTextField().setBorder(UIManagement.borderTextBoxNotEditable);
        gridBagCon.insets = new Insets(0, 10, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(principalTextField, gridBagCon);
        principalSelectButton = new JButton();
        principalSelectButton.setActionCommand("SelectPrincipal");
        principalSelectButton.setMargin(new Insets(0, 0, 0, 0));
        principalSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/SmallSearchButton.gif")));
        principalSelectButton.addActionListener(listenerCommon);
        principalSelectButton.setBorder(UIManagement.borderGroupTitle);
        gridBagCon.insets = new Insets(2, 0, 2, 12);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(principalSelectButton, gridBagCon);
        typeComboBox = new DynaComboBox();
        typeComboBox.setMandatory(false);
        typeComboBox.setTitleText(DynaMOAD.getMSRString("WRD_0101", "Type", 3));
        typeComboBox.setTitleWidth(80);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        typeComboBox.setBackground(UIManagement.panelBackGround);
        typeComboBox.getComboBox().setBorder(UIManagement.borderTextBoxNotEditable);
        typeComboBox.getComboBox().setBackground(UIManagement.panelBackGround);
        typeComboBox.setModel(typeComboModel);
        typeComboModel.enableDataLoad();
        gridBagCon.insets = new Insets(0, 10, 0, 10);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(typeComboBox, gridBagCon);
        grantableComboBox = new DynaComboBox();
        grantableComboBox.setMandatory(false);
        grantableComboBox.setTitleText(DynaMOAD.getMSRString("WRD_0102", "is Grantable", 3));
        grantableComboBox.setTitleWidth(80);
        grantableComboBox.setTitleVisible(true);
        grantableComboBox.setEditable(false);
        grantableComboBox.setBackground(UIManagement.panelBackGround);
        grantableComboBox.getComboBox().setBorder(UIManagement.borderTextBoxNotEditable);
        grantableComboBox.getComboBox().setBackground(UIManagement.panelBackGround);
        grantableComboBox.setModel(grantableComboModel);
        grantableComboModel.enableDataLoad();
        gridBagCon.insets = new Insets(0, 10, 20, 10);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 7;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(grantableComboBox, gridBagCon);
        searchConditionPanel.add(optionPanel);
        searchConditionPanel.add(classTextField);
        searchConditionPanel.add(classSelectButton);
        searchConditionPanel.add(objectTextField);
        searchConditionPanel.add(objectSelectButton);
        searchConditionPanel.add(permissionComboBox);
        searchConditionPanel.add(scopeComboBox);
        searchConditionPanel.add(principalTextField);
        searchConditionPanel.add(principalSelectButton);
        searchConditionPanel.add(typeComboBox);
        searchConditionPanel.add(grantableComboBox);
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(UIManagement.panelBackGround);
        leftPanel.add(searchConditionPanel, "North");
        leftScrollPane = UIFactory.createStrippedScrollPane(null);
        leftScrollPane.getViewport().add(leftPanel, null);
        searchListTableDefine();
        rightScrollPane = UIFactory.createStrippedScrollPane(null);
        rightScrollPane.getViewport().add(searchResultTable.getTable());
        rightScrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        rightScrollPane.setPreferredSize(new Dimension(600, 500));
        allSplitPane = new JSplitPane(1);
        allSplitPane.setOneTouchExpandable(true);
        allSplitPane.setDividerSize(7);
        allSplitPane.setDividerLocation(250);
        allSplitPane.setLeftComponent(leftScrollPane);
        allSplitPane.setRightComponent(rightScrollPane);
        allSplitPane.setBackground(UIManagement.panelBackGround);
        principalTableDefine();
        princiaplTableScrPane = UIFactory.createStrippedScrollPane(null);
        princiaplTableScrPane.setViewportView(principalTable.getTable());
        princiaplTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        princiaplTableScrPane.setPreferredSize(new Dimension(300, 150));
        arrayLabel = new JLabel[permissionNames.length];
        aArrayCheckBox = new JCheckBox[permissionNames.length];
        dArrayCheckBox = new JCheckBox[permissionNames.length];
        gArrayCheckBox = new JCheckBox[permissionNames.length];
        for(int i = 0; i < permissionNames.length; i++)
        {
            arrayLabel[i] = new JLabel(permissionNames[i]);
            aArrayCheckBox[i] = new JCheckBox();
            dArrayCheckBox[i] = new JCheckBox();
            gArrayCheckBox[i] = new JCheckBox();
        }

        nameLabel = new JLabel("");
        namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        namePanel.setBackground(UIManagement.panelBackGround);
        namePanel.setLayout(new GridLayout(0, 1));
        namePanel.add(nameLabel);
        for(int i = 0; i < permissionNames.length; i++)
            namePanel.add(arrayLabel[i]);

        allowLabel = new JLabel("Allow", 2);
        denyLabel = new JLabel("Deny", 2);
        grantLabel = new JLabel("Grantable", 2);
        boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(0, 3));
        boxPanel.setBackground(UIManagement.panelBackGround);
        boxPanel.add(allowLabel);
        boxPanel.add(denyLabel);
        boxPanel.add(grantLabel);
        for(int i = 0; i < permissionNames.length; i++)
        {
            boxPanel.add(aArrayCheckBox[i]);
            boxPanel.add(dArrayCheckBox[i]);
            boxPanel.add(gArrayCheckBox[i]);
        }

        permissionPanel = new JPanel();
        permissionPanel.setBorder(BorderFactory.createTitledBorder(UIManagement.borderGroup, DynaMOAD.getMSRString("WRD_0051", "Permission", 3)));
        permissionPanel.setLayout(new BoxLayout(permissionPanel, 0));
        permissionPanel.setBackground(UIManagement.panelBackGround);
        permissionPanel.add(Box.createHorizontalGlue());
        permissionPanel.add(namePanel);
        permissionPanel.add(boxPanel);
        permissionPanel.add(Box.createHorizontalGlue());
        classifiedPanel = new JPanel();
        classifiedPanel.setLayout(new BoxLayout(classifiedPanel, 1));
        classifiedPanel.add(princiaplTableScrPane);
        classifiedPanel.add(permissionPanel);
        getContentPane().add(buttonToolBar, "North");
        getContentPane().add(allSplitPane, "Center");
        initializeCheckBoxes();
        pack();
    }

    public ArrayList setPermissionModel()
    {
        ArrayList localVct = new ArrayList();
        ArrayList intVct = new ArrayList();
        intVct.add("CREATE");
        intVct.add("CREATE");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("UPDATE");
        intVct.add("UPDATE");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("READ");
        intVct.add("READ");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("DELETE");
        intVct.add("DELETE");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("SCAN");
        intVct.add("SCAN");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("PRINT");
        intVct.add("PRINT");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("LOCK / UNLOCK");
        intVct.add("LOCK / UNLOCK");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("LINK / UNLINK");
        intVct.add("LINK / UNLINK");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("CHECK-IN / CHECK-OUT");
        intVct.add("CHECK-IN / CHECK-OUT");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        localVct.trimToSize();
        return localVct;
    }

    public ArrayList setScopeModel()
    {
        ArrayList localVct = new ArrayList();
        ArrayList intVct = new ArrayList();
        intVct.add("OTHERS");
        intVct.add("OTHERS");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("OWNER");
        intVct.add("OWNER");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("GROUP");
        intVct.add("GROUP");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("ROLE");
        intVct.add("ROLE");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("USER");
        intVct.add("USER");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        localVct.trimToSize();
        return localVct;
    }

    public ArrayList setTypeModel()
    {
        ArrayList localVct = new ArrayList();
        ArrayList intVct = new ArrayList();
        intVct.add("ALLOW");
        intVct.add("ALLOW");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        intVct.add("DENY");
        intVct.add("DENY");
        intVct.trimToSize();
        localVct.add(intVct.clone());
        intVct.clear();
        localVct.trimToSize();
        return localVct;
    }

    public ArrayList setBooleanComboBox()
    {
        ArrayList booleanAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
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

    public void searchListTableDefine()
    {
        setTableHeader();
        searchResultTable = new Table(tableData, (ArrayList)column.get(0), (ArrayList)column.get(1), 1);
        searchResultTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4, 5, 6, 7
        });
        searchResultTable.setIndexColumn(0);
        searchResultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        searchResultTable.getTable().setCursor(handCursor);
        searchResultTable.getTable().addMouseListener(listenerCommon);
    }

    public void setTableHeader()
    {
        column = new ArrayList();
        tableData = new ArrayList();
        ArrayList colname = new ArrayList();
        ArrayList colwidth = new ArrayList();
        colname.add(DynaMOAD.getMSRString("WRD_0028", "Class", 3));
        colname.add(DynaMOAD.getMSRString("WRD_0099", "Object", 3));
        colname.add(DynaMOAD.getMSRString("WRD_0051", "Permission", 3));
        colname.add(DynaMOAD.getMSRString("WRD_0098", "Scope", 3));
        colname.add(DynaMOAD.getMSRString("WRD_0100", "Principal", 3));
        colname.add(DynaMOAD.getMSRString("WRD_0101", "Type", 3));
        colname.add(DynaMOAD.getMSRString("WRD_0102", "is Grantable", 3));
        colname.add("Description");
        colname.trimToSize();
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.add(new Integer(80));
        colwidth.trimToSize();
        column.add(colname);
        column.add(colwidth);
        column.trimToSize();
    }

    public void principalTableDefine()
    {
        principalTableData = new ArrayList();
        ArrayList columnName = new ArrayList();
        ArrayList columnWidth = new ArrayList();
        columnName.add(DynaMOAD.getMSRString("WRD_0028", "Class", 3));
        columnName.add(DynaMOAD.getMSRString("WRD_0099", "Object", 3));
        columnName.add(DynaMOAD.getMSRString("WRD_0098", "Scope", 3));
        columnName.add("ID");
        columnName.add(DynaMOAD.getMSRString("WRD_0103", "Name", 3));
        columnName.trimToSize();
        columnWidth.add(new Integer(90));
        columnWidth.add(new Integer(90));
        columnWidth.add(new Integer(90));
        columnWidth.add(new Integer(90));
        columnWidth.add(new Integer(90));
        columnWidth.trimToSize();
        principalTable = new PrincipalTable(principalTableData, columnName, columnWidth, 0);
        principalTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        principalTable.setIndexColumn(0);
        principalTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        principalTable.getTable().setCursor(handCursor);
        principalTable.getTable().addMouseListener(listenerCommon);
    }

    void initializeCheckBoxes()
    {
        for(int i = 0; i < permissionNames.length; i++)
        {
            aArrayCheckBox[i].setSelected(false);
            dArrayCheckBox[i].setSelected(false);
            gArrayCheckBox[i].setSelected(false);
            aArrayCheckBox[i].setEnabled(false);
            dArrayCheckBox[i].setEnabled(false);
            gArrayCheckBox[i].setEnabled(false);
        }

    }

    public void setTableData(ArrayList vt)
    {
        tableData.clear();
        for(int i = 0; i < vt.size(); i++)
            tableData.add(vt.get(i));

        searchResultTable.changeTableData();
    }

    public void setClass(String ouid, String name)
    {
        classOuid = ouid;
        classTextField.setText(name);
    }

    public void setObject(String ouid, String number)
    {
        if(!Utils.isNullString(ouid))
        {
            objectOuid = ouid;
            objectTextField.setText(number);
        } else
        {
            objectOuid = "0";
            objectTextField.setText("");
        }
    }

    public void setPrincipal(String id, String name)
    {
        principalID = id;
        principalName = name;
        principalTextField.setText(name);
    }

    public void exitForm(WindowEvent e)
    {
        CloseEvent();
    }

    public void CloseEvent()
    {
        searchButton.removeActionListener(listenerCommon);
        clearButton.removeActionListener(listenerCommon);
        classSelectButton.removeActionListener(listenerCommon);
        objectSelectButton.removeActionListener(listenerCommon);
        allRadioButton.addActionListener(listenerCommon);
        objectRadioButton.removeActionListener(listenerCommon);
        scopeComboBox.removeItemListener(listenerCommon);
        principalSelectButton.removeActionListener(listenerCommon);
        searchResultTable.getTable().removeMouseListener(listenerCommon);
        principalTable.getTable().removeMouseListener(listenerCommon);
        removeWindowListener(listenerCommon);
        dispose();
    }

    void searchButton_actionPerformed(ActionEvent e)
    {
        try
        {
            if(allRadioButton.isSelected())
            {
                HashMap conditions = new HashMap();
                conditions.put("description", "");
                if(!Utils.isNullString(classTextField.getText()))
                    conditions.put("targetclassouid", classOuid);
                if(!Utils.isNullString(objectTextField.getText()))
                    conditions.put("targetobjectouid", objectOuid);
                if(permissionComboBox.getSelectedIndex() > -1)
                    conditions.put("permission", (String)permissionComboModel.getSelectedOID());
                if(scopeComboBox.getSelectedIndex() > -1)
                    conditions.put("permissionscope", (String)scopeComboModel.getSelectedOID());
                if(!Utils.isNullString(principalTextField.getText()))
                    if(((String)scopeComboModel.getSelectedOID()).equals("GROUP"))
                        conditions.put("groupname", principalName);
                    else
                    if(((String)scopeComboModel.getSelectedOID()).equals("ROLE"))
                        conditions.put("rolename", principalName);
                    else
                    if(((String)scopeComboModel.getSelectedOID()).equals("USER"))
                        conditions.put("userid", principalID);
                if(typeComboBox.getSelectedIndex() > -1)
                    conditions.put("permissiontype", (String)typeComboModel.getSelectedOID());
                if(grantableComboBox.getSelectedIndex() > -1)
                    conditions.put("isgrantable", (Boolean)grantableComboModel.getSelectedOID());
                ArrayList aclList = acl.retrieveACL(modelOuid, conditions);
                DOSChangeable rowMap = null;
                ArrayList rowList = new ArrayList();
                tableData.clear();
                if(aclList != null)
                {
                    for(int i = 0; i < aclList.size(); i++)
                    {
                        DOSChangeable tmpData = null;
                        rowMap = (DOSChangeable)aclList.get(i);
                        tmpData = dos.getClass((String)rowMap.get("targetclassouid"));
                        if(tmpData == null)
                        {
                            rowList.clear();
                            continue;
                        }
                        rowList.add((String)tmpData.get("title"));
                        if(Utils.isNullString((String)rowMap.get("targetobjectouid")) || ((String)rowMap.get("targetobjectouid")).equals("0"))
                        {
                            rowList.add("");
                        } else
                        {
                            tmpData = dos.get((String)rowMap.get("targetobjectouid"));
                            if(tmpData == null)
                            {
                                rowList.clear();
                                continue;
                            }
                            rowList.add((String)tmpData.get("md$number"));
                        }
                        String objStr = null;
                        if(((String)rowMap.get("permissionscope")).equals("GROUP"))
                        {
                            HashMap grpObj = aus.getGroup((String)rowMap.get("userobject"));
                            if(grpObj == null)
                            {
                                rowList.clear();
                                continue;
                            }
                            objStr = (String)grpObj.get("description") + " [" + (String)rowMap.get("userobject") + "]";
                        } else
                        if(((String)rowMap.get("permissionscope")).equals("ROLE"))
                        {
                            HashMap rolObj = aus.getRole((String)rowMap.get("userobject"));
                            if(rolObj == null)
                                continue;
                            objStr = (String)rolObj.get("description") + " [" + (String)rowMap.get("userobject") + "]";
                        } else
                        if(((String)rowMap.get("permissionscope")).equals("USER"))
                        {
                            HashMap usrObj = aus.getUser((String)rowMap.get("userobject"));
                            if(usrObj == null)
                                continue;
                            objStr = (String)usrObj.get("name") + " [" + (String)rowMap.get("userobject") + "]";
                        } else
                        {
                            objStr = (String)rowMap.get("userobject");
                        }
                        rowList.add((String)rowMap.get("permission"));
                        rowList.add((String)rowMap.get("permissionscope"));
                        rowList.add(objStr);
                        rowList.add(((String)rowMap.get("permissiontype")).equals("A") ? "Allow" : "Deny");
                        rowList.add((Boolean)rowMap.get("isgrantable"));
                        rowList.add((String)rowMap.get("description"));
                        tableData.add(rowList.clone());
                        rowList.clear();
                    }

                }
                searchResultTable.changeTableData();
            } else
            if(objectRadioButton.isSelected())
            {
                if(Utils.isNullString(classTextField.getText()) && Utils.isNullString(objectTextField.getText()))
                {
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0004", "Select Class/ Object.", 3), DynaMOAD.getMSRString("WRD_0004", "Information", 3), 1);
                    return;
                }
                principalTableData.clear();
                if(scopeMap == null)
                    scopeMap = new HashMap();
                else
                    scopeMap.clear();
                ArrayList aclList = null;
                if(includeInheritChkBox.isSelected())
                    aclList = acl.retrieveACL4Grant(classOuid, objectOuid, LogIn.userID, new Boolean(true));
                else
                    aclList = acl.retrievePrivateACL4Grant(classOuid, objectOuid, LogIn.userID, new Boolean(true));
                if(aclList != null)
                {
                    HashMap principalMap = null;
                    HashMap principalDataMap = null;
                    HashMap permissionMap = null;
                    HashMap permissionDataMap = null;
                    Iterator iKey = aclList.iterator();
                    ArrayList aObj = null;
                    DOSChangeable aclData = null;
                    DOSChangeable tmpData = null;
                    String classTitle = null;
                    String objectNumber = null;
                    String id = null;
                    String name = null;
                    while(iKey.hasNext()) 
                    {
                        aclData = (DOSChangeable)iKey.next();
                        tmpData = dos.getClass((String)aclData.get("targetclassouid"));
                        if(tmpData == null)
                            continue;
                        classTitle = (String)tmpData.get("title");
                        if(Utils.isNullString((String)aclData.get("targetobjectouid")) || ((String)aclData.get("targetobjectouid")).equals("0"))
                        {
                            objectNumber = "";
                        } else
                        {
                            tmpData = dos.get((String)aclData.get("targetobjectouid"));
                            if(tmpData == null)
                                continue;
                            objectNumber = (String)tmpData.get("md$number");
                        }
                        if(((String)aclData.get("permissionscope")).equals("GROUP"))
                        {
                            HashMap grpObj = aus.getGroup((String)aclData.get("userobject"));
                            if(grpObj == null)
                                continue;
                            id = (String)aclData.get("userobject");
                            name = (String)grpObj.get("description");
                        } else
                        if(((String)aclData.get("permissionscope")).equals("ROLE"))
                        {
                            HashMap rolObj = aus.getRole((String)aclData.get("userobject"));
                            if(rolObj == null)
                                continue;
                            id = (String)aclData.get("userobject");
                            name = (String)rolObj.get("description");
                        } else
                        if(((String)aclData.get("permissionscope")).equals("USER"))
                        {
                            HashMap usrObj = aus.getUser((String)aclData.get("userobject"));
                            if(usrObj == null)
                                continue;
                            id = (String)aclData.get("userobject");
                            name = (String)usrObj.get("name");
                        } else
                        {
                            id = (String)aclData.get("userobject");
                            name = (String)aclData.get("userobject");
                        }
                        if(scopeMap.containsKey((String)aclData.get("permissionscope")))
                        {
                            principalMap = (HashMap)scopeMap.get((String)aclData.get("permissionscope"));
                            if(principalMap.containsKey(id))
                            {
                                principalDataMap = (HashMap)principalMap.get(id);
                                permissionMap = (HashMap)principalDataMap.get("permission");
                                permissionDataMap = new HashMap();
                                permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                                permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                                permissionDataMap.put("isgrantable", ((String)aclData.get("isgrantable")).equals("T") ? "T" : "F");
                                permissionDataMap.put("description", (String)aclData.get("description"));
                                permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                            } else
                            {
                                permissionDataMap = new HashMap();
                                permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                                permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                                permissionDataMap.put("isgrantable", ((String)aclData.get("isgrantable")).equals("T") ? "T" : "F");
                                permissionDataMap.put("description", (String)aclData.get("description"));
                                permissionMap = new HashMap();
                                permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                                principalDataMap = new HashMap();
                                principalDataMap.put("name", name);
                                principalDataMap.put("permission", permissionMap);
                                principalMap.put(id, principalDataMap);
                                aObj = new ArrayList();
                                aObj.add(classTitle);
                                aObj.add(objectNumber);
                                aObj.add((String)aclData.get("permissionscope"));
                                aObj.add(id);
                                aObj.add(name);
                                principalTableData.add(aObj);
                            }
                        } else
                        {
                            permissionDataMap = new HashMap();
                            permissionDataMap.put("allow", ((String)aclData.get("permissiontype")).equals("A") ? "T" : "F");
                            permissionDataMap.put("deny", ((String)aclData.get("permissiontype")).equals("A") ? "F" : "T");
                            permissionDataMap.put("isgrantable", ((String)aclData.get("isgrantable")).equals("T") ? "T" : "F");
                            permissionDataMap.put("description", (String)aclData.get("description"));
                            permissionMap = new HashMap();
                            permissionMap.put((String)aclData.get("permission"), permissionDataMap);
                            principalDataMap = new HashMap();
                            principalDataMap.put("name", name);
                            principalDataMap.put("permission", permissionMap);
                            principalMap = new HashMap();
                            principalMap.put(id, principalDataMap);
                            scopeMap.put((String)aclData.get("permissionscope"), principalMap);
                            aObj = new ArrayList();
                            aObj.add(classTitle);
                            aObj.add(objectNumber);
                            aObj.add((String)aclData.get("permissionscope"));
                            aObj.add(id);
                            aObj.add(name);
                            principalTableData.add(aObj);
                        }
                    }
                }
                principalTable.changeTableData();
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void clearButton_actionPerformed(ActionEvent e)
    {
        includeInheritChkBox.setSelected(false);
        classTextField.setText("");
        classOuid = "";
        objectTextField.setText("");
        objectOuid = "";
        permissionComboBox.setSelectedIndex(-1);
        scopeComboBox.setSelectedIndex(-1);
        principalTextField.setText("");
        principalID = "";
        principalName = "";
        typeComboBox.setSelectedIndex(-1);
        grantableComboBox.setSelectedIndex(-1);
        leftPanel.updateUI();
        tableData.clear();
        searchResultTable.changeTableData();
        principalTableData.clear();
        principalTable.changeTableData();
        initializeCheckBoxes();
    }

    void classSelectButton_actionPerformed(ActionEvent e)
    {
        ClassSelectDialog classSelectDialog = new ClassSelectDialog(this, this, true);
        classSelectDialog.show();
    }

    void objectSelectButton_actionPerformed(ActionEvent e)
    {
        if(Utils.isNullString(classTextField.getText()))
        {
            JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0005", "Select Class.", 3), DynaMOAD.getMSRString("WRD_0004", "Information", 3), 1);
            return;
        } else
        {
            SmallSearchDialog smallSearchDialog = new SmallSearchDialog(this, objectSelectButton, true, classOuid, "");
            smallSearchDialog.show();
            return;
        }
    }

    void principalSelectButton_actionPerformed(ActionEvent e)
    {
        if(scopeComboBox.getSelectedIndex() < 0)
        {
            JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0006", "Select Scope.", 3), DynaMOAD.getMSRString("WRD_0004", "Information", 3), 1);
            return;
        }
        if(((String)scopeComboModel.getSelectedOID()).equals("GROUP"))
        {
            UserObjectSingleSelect userObjectSingleSelect = new UserObjectSingleSelect(this, this, DynaMOAD.getMSRString("WRD_0105", "Group Selection", 3), true, 0);
            userObjectSingleSelect.show();
        } else
        if(((String)scopeComboModel.getSelectedOID()).equals("ROLE"))
        {
            UserObjectSingleSelect userObjectSingleSelect = new UserObjectSingleSelect(this, this, DynaMOAD.getMSRString("WRD_0106", "Role Selection", 3), true, 1);
            userObjectSingleSelect.show();
        } else
        if(((String)scopeComboModel.getSelectedOID()).equals("USER"))
        {
            UserObjectSingleSelect userObjectSingleSelect = new UserObjectSingleSelect(this, this, DynaMOAD.getMSRString("WRD_0107", "User Selection", 3), true, 2);
            userObjectSingleSelect.show();
        }
    }

    void principalTable_valueChanged(PrincipalTable table, ListSelectionEvent e)
    {
        if(!e.getValueIsAdjusting())
        {
            ListSelectionModel listSelectionModel = table.getTable().getSelectionModel();
            if(listSelectionModel.getMinSelectionIndex() > -1 && listSelectionModel.getMaxSelectionIndex() > -1)
            {
                int selRow = principalTable.getTable().getSelectedRow();
                String ouidRow = principalTable.getSelectedOuidRow(selRow);
                String scope = null;
                String id = null;
                if(ouidRow != null)
                {
                    principalTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                    scope = ((ArrayList)table.getList().get((new Integer(ouidRow)).intValue())).get(2).toString();
                    id = ((ArrayList)table.getList().get((new Integer(ouidRow)).intValue())).get(3).toString();
                } else
                {
                    scope = ((ArrayList)table.getList().get(selRow)).get(2).toString();
                    id = ((ArrayList)table.getList().get(selRow)).get(3).toString();
                }
                HashMap principalMap = (HashMap)scopeMap.get(scope);
                HashMap principalDataMap = (HashMap)principalMap.get(id);
                HashMap permissionMap = (HashMap)principalDataMap.get("permission");
                for(int i = 0; i < permissionNames.length; i++)
                    if(permissionMap.containsKey(arrayLabel[i].getText()))
                    {
                        HashMap permissionDataMap = (HashMap)permissionMap.get(arrayLabel[i].getText());
                        if(permissionDataMap != null)
                        {
                            String allow = (String)permissionDataMap.get("allow");
                            String deny = (String)permissionDataMap.get("deny");
                            String grantable = (String)permissionDataMap.get("isgrantable");
                            if(!Utils.isNullString(allow) && allow.equals("T"))
                            {
                                aArrayCheckBox[i].setSelected(true);
                                if(!Utils.isNullString(grantable) && grantable.equals("T"))
                                    gArrayCheckBox[i].setSelected(true);
                                else
                                    gArrayCheckBox[i].setSelected(false);
                            } else
                            if(!Utils.isNullString(deny) && deny.equals("T"))
                                dArrayCheckBox[i].setSelected(true);
                        }
                    } else
                    {
                        aArrayCheckBox[i].setSelected(false);
                        dArrayCheckBox[i].setSelected(false);
                        gArrayCheckBox[i].setSelected(false);
                    }

            } else
            {
                initializeCheckBoxes();
            }
        }
    }

    Cursor handCursor;
    private final int TITLE_WIDTH = 80;
    private final String PER_CREATE = "CREATE";
    private final String PER_UPDATE = "UPDATE";
    private final String PER_READ = "READ";
    private final String PER_DELETE = "DELETE";
    private final String PER_SCAN = "SCAN";
    private final String PER_PRINT = "PRINT";
    private final String PER_LOCK = "LOCK / UNLOCK";
    private final String PER_LINK = "LINK / UNLINK";
    private final String PER_CHECKIN = "CHECK-IN / CHECK-OUT";
    private final String permissionNames[] = {
        "CREATE", "UPDATE", "READ", "DELETE", "SCAN", "PRINT", "LOCK / UNLOCK", "LINK / UNLINK", "CHECK-IN / CHECK-OUT"
    };
    private final String SCP_OTHERS = "OTHERS";
    private final String SCP_OWNER = "OWNER";
    private final String SCP_GROUP = "GROUP";
    private final String SCP_ROLE = "ROLE";
    private final String SCP_USER = "USER";
    private final String TYP_ALLOW = "ALLOW";
    private final String TYP_DENY = "DENY";
    private JToolBar buttonToolBar;
    private JButton searchButton;
    private JButton clearButton;
    private JPanel optionPanel;
    private ButtonGroup buttonGroup;
    private JRadioButton allRadioButton;
    private JRadioButton objectRadioButton;
    private JCheckBox includeInheritChkBox;
    private JSplitPane allSplitPane;
    private JScrollPane leftScrollPane;
    private JScrollPane rightScrollPane;
    private JPanel leftPanel;
    private JPanel searchConditionPanel;
    private DynaTextField classTextField;
    private JButton classSelectButton;
    private DynaTextField objectTextField;
    private JButton objectSelectButton;
    private DynaComboBox permissionComboBox;
    private DynaComboBox scopeComboBox;
    private DynaTextField principalTextField;
    private JButton principalSelectButton;
    private DynaComboBox typeComboBox;
    private DynaComboBox grantableComboBox;
    private Table searchResultTable;
    private JPanel classifiedPanel;
    private JScrollPane princiaplTableScrPane;
    private PrincipalTable principalTable;
    private JPanel permissionPanel;
    private JPanel namePanel;
    private JPanel boxPanel;
    private JLabel nameLabel;
    private JLabel arrayLabel[];
    private JLabel allowLabel;
    private JCheckBox aArrayCheckBox[];
    private JLabel denyLabel;
    private JCheckBox dArrayCheckBox[];
    private JLabel grantLabel;
    private JCheckBox gArrayCheckBox[];
    private ListenerCommon listenerCommon;
    private ACL acl;
    private AUS aus;
    private DOS dos;
    private ArrayList column;
    private ArrayList tableData;
    private ArrayList principalTableData;
    private HashMap scopeMap;
    private DynaComboBoxDataLoader permissionComboDataLoader;
    private DynaComboBoxModel permissionComboModel;
    private DynaComboBoxDataLoader scopeComboDataLoader;
    private DynaComboBoxModel scopeComboModel;
    private DynaComboBoxDataLoader typeComboDataLoader;
    private DynaComboBoxModel typeComboModel;
    private DynaComboBoxDataLoader grantableComboDataLoader;
    private DynaComboBoxModel grantableComboModel;
    private String modelOuid;
    private String classOuid;
    private String objectOuid;
    private String principalID;
    private String principalName;















}