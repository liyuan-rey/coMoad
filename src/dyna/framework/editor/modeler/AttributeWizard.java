// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AttributeWizard.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DDG;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.CADIntegrationUtils;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Referenced classes of package dyna.framework.editor.modeler:
//            TypeClassSelection

public class AttributeWizard extends JFrame
    implements ActionListener, MouseListener, ChangeListener
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


    public AttributeWizard(DOS dos, DDG ddg, int cadType)
    {
        handCursor = new Cursor(12);
        buttonToolBar1 = new JToolBar(0);
        buttonBoxLayout1 = new BoxLayout(buttonToolBar1, 0);
        buttonToolBar2 = new JToolBar(0);
        buttonBoxLayout2 = new BoxLayout(buttonToolBar2, 0);
        dComboType = new DynaComboTypeInstance();
        typeComboModel = new DynaComboBoxModel(dComboType);
        utils = null;
        try
        {
            this.dos = dos;
            this.ddg = ddg;
            modelOuid = this.dos.getWorkingModel();
            this.cadType = cadType;
            utils = new CADIntegrationUtils(cadType, this.dos);
            modelInfoDC = new DOSChangeable();
            if(modelOuid != null && !modelOuid.equals(""))
                modelInfoDC = this.dos.getModel(modelOuid);
            classOuid = utils.cadFileClassOuid;
            initialize();
            refreshButton1Click_actionPerformed(null);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    private void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("Attribute Wizard");
        setLocation(112, 84);
        setSize(1000, 750);
        modifyButton1 = new JButton();
        modifyButton1.setEnabled(true);
        modifyButton1.setToolTipText("Modify");
        modifyButton1.setActionCommand("modify1");
        modifyButton1.setMargin(new Insets(0, 0, 0, 0));
        modifyButton1.setIcon(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
        modifyButton1.addActionListener(this);
        refreshButton1 = new JButton();
        refreshButton1.setToolTipText("Refresh");
        refreshButton1.setActionCommand("refresh1");
        refreshButton1.setMargin(new Insets(0, 0, 0, 0));
        refreshButton1.setIcon(new ImageIcon(getClass().getResource("/icons/Refresh.gif")));
        refreshButton1.addActionListener(this);
        buttonToolBar1.setAlignmentX(1.0F);
        buttonToolBar1.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar1.add(modifyButton1);
        buttonToolBar1.add(refreshButton1);
        GridBagLayout gridBag1 = new GridBagLayout();
        GridBagConstraints gridBagCon1 = new GridBagConstraints();
        gridBagCon1.fill = 1;
        gridBagCon1.anchor = 11;
        sizeTypeTextField = new DynaTextField();
        sizeTypeTextField.setTitleText("Size Type");
        sizeTypeTextField.setTitleWidth(120);
        sizeTypeTextField.setTitleVisible(true);
        sizeTypeTextField.setEditable(false);
        sizeTypeTextField.addMouseListener(this);
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 0;
        gridBagCon1.gridwidth = 2;
        gridBagCon1.gridheight = 1;
        gridBagCon1.insets = new Insets(10, 0, 0, 0);
        gridBag1.setConstraints(sizeTypeTextField, gridBagCon1);
        sizeTypeClassTextField = new DynaTextField();
        sizeTypeClassTextField.setTitleText("Size Type Class");
        sizeTypeClassTextField.setTitleWidth(120);
        sizeTypeClassTextField.setTitleVisible(true);
        sizeTypeClassTextField.setEditable(false);
        sizeTypeClassTextField.addMouseListener(this);
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 1;
        gridBagCon1.gridwidth = 1;
        gridBagCon1.gridheight = 1;
        gridBagCon1.insets = new Insets(0, 0, 0, 0);
        gridBag1.setConstraints(sizeTypeClassTextField, gridBagCon1);
        sizeSelectButton = new JButton();
        sizeSelectButton.setEnabled(true);
        sizeSelectButton.setText("...");
        sizeSelectButton.setActionCommand("size select");
        sizeSelectButton.setMargin(new Insets(0, 0, 0, 0));
        sizeSelectButton.setPreferredSize(new Dimension(20, 20));
        sizeSelectButton.addActionListener(this);
        gridBagCon1.weightx = 0.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 1;
        gridBagCon1.gridy = 1;
        gridBagCon1.gridwidth = 1;
        gridBagCon1.gridheight = 1;
        gridBagCon1.insets = new Insets(2, 0, 2, 5);
        gridBag1.setConstraints(sizeSelectButton, gridBagCon1);
        scaleTypeTextField = new DynaTextField();
        scaleTypeTextField.setTitleText("Scale Type");
        scaleTypeTextField.setTitleWidth(120);
        scaleTypeTextField.setTitleVisible(true);
        scaleTypeTextField.setEditable(false);
        scaleTypeTextField.addMouseListener(this);
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 2;
        gridBagCon1.gridwidth = 2;
        gridBagCon1.gridheight = 1;
        gridBagCon1.insets = new Insets(0, 0, 0, 0);
        gridBag1.setConstraints(scaleTypeTextField, gridBagCon1);
        scaleTypeClassTextField = new DynaTextField();
        scaleTypeClassTextField.setTitleText("Scale Type Class");
        scaleTypeClassTextField.setTitleWidth(120);
        scaleTypeClassTextField.setTitleVisible(true);
        scaleTypeClassTextField.setEditable(false);
        scaleTypeClassTextField.addMouseListener(this);
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 3;
        gridBagCon1.gridwidth = 1;
        gridBagCon1.gridheight = 1;
        gridBag1.setConstraints(scaleTypeClassTextField, gridBagCon1);
        scaleSelectButton = new JButton();
        scaleSelectButton.setEnabled(true);
        scaleSelectButton.setText("...");
        scaleSelectButton.setActionCommand("scale select");
        scaleSelectButton.setMargin(new Insets(0, 0, 0, 0));
        scaleSelectButton.setPreferredSize(new Dimension(20, 20));
        scaleSelectButton.addActionListener(this);
        gridBagCon1.weightx = 0.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 1;
        gridBagCon1.gridy = 3;
        gridBagCon1.gridwidth = 1;
        gridBagCon1.gridheight = 1;
        gridBagCon1.insets = new Insets(2, 0, 2, 5);
        gridBag1.setConstraints(scaleSelectButton, gridBagCon1);
        dummyLabel = new JLabel();
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 1.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 4;
        gridBagCon1.gridwidth = 2;
        gridBagCon1.gridheight = 1;
        gridBagCon1.insets = new Insets(0, 0, 0, 0);
        gridBag1.setConstraints(dummyLabel, gridBagCon1);
        northPanel1 = new JPanel();
        northPanel1.setBorder(BorderFactory.createEtchedBorder(1));
        northPanel1.setLayout(gridBag1);
        northPanel1.add(sizeTypeTextField);
        northPanel1.add(sizeTypeClassTextField);
        northPanel1.add(sizeSelectButton);
        northPanel1.add(scaleTypeTextField);
        northPanel1.add(scaleTypeClassTextField);
        northPanel1.add(scaleSelectButton);
        northPanel1.add(dummyLabel);
        nextButton1 = new JButton();
        nextButton1.setEnabled(true);
        nextButton1.setText("Next");
        nextButton1.setActionCommand("next1");
        nextButton1.setMargin(new Insets(0, 10, 0, 10));
        nextButton1.addActionListener(this);
        buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new BoxLayout(buttonPanel1, 0));
        buttonPanel1.add(Box.createHorizontalGlue());
        buttonPanel1.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel1.add(nextButton1);
        step1Panel = new JPanel();
        step1Panel.setLayout(new BorderLayout());
        step1Panel.add(buttonToolBar1, "North");
        step1Panel.add(northPanel1, "Center");
        step1Panel.add(buttonPanel1, "South");
        createButton2 = new JButton();
        createButton2.setEnabled(true);
        createButton2.setToolTipText("Create");
        createButton2.setActionCommand("create2");
        createButton2.setMargin(new Insets(0, 0, 0, 0));
        createButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Registry.gif")));
        createButton2.addActionListener(this);
        modifyButton2 = new JButton();
        modifyButton2.setEnabled(true);
        modifyButton2.setToolTipText("Modify");
        modifyButton2.setActionCommand("modify2");
        modifyButton2.setMargin(new Insets(0, 0, 0, 0));
        modifyButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
        modifyButton2.addActionListener(this);
        clearButton2 = new JButton();
        clearButton2.setEnabled(true);
        clearButton2.setToolTipText("Clear");
        clearButton2.setActionCommand("clear2");
        clearButton2.setMargin(new Insets(0, 0, 0, 0));
        clearButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton2.addActionListener(this);
        deleteButton2 = new JButton();
        deleteButton2.setToolTipText("Delete");
        deleteButton2.setActionCommand("delete2");
        deleteButton2.setMargin(new Insets(0, 0, 0, 0));
        deleteButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        deleteButton2.addActionListener(this);
        refreshButton2 = new JButton();
        refreshButton2.setToolTipText("Refresh");
        refreshButton2.setActionCommand("refresh2");
        refreshButton2.setMargin(new Insets(0, 0, 0, 0));
        refreshButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Refresh.gif")));
        refreshButton2.addActionListener(this);
        buttonToolBar2.setAlignmentX(1.0F);
        buttonToolBar2.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar2.add(createButton2);
        buttonToolBar2.add(modifyButton2);
        buttonToolBar2.add(clearButton2);
        buttonToolBar2.add(deleteButton2);
        buttonToolBar2.add(refreshButton2);
        GridBagLayout gridBag2 = new GridBagLayout();
        GridBagConstraints gridBagCon2 = new GridBagConstraints();
        gridBagCon2.fill = 1;
        gridBagCon2.anchor = 11;
        pdmNameTextField = new DynaTextField();
        pdmNameTextField.setTitleText("PDM Name");
        pdmNameTextField.setTitleWidth(120);
        pdmNameTextField.setTitleVisible(true);
        pdmNameTextField.setEditable(true);
        pdmNameTextField.addMouseListener(this);
        gridBagCon2.weightx = 1.0D;
        gridBagCon2.weighty = 0.0D;
        gridBagCon2.gridx = 0;
        gridBagCon2.gridy = 0;
        gridBagCon2.gridwidth = 1;
        gridBagCon2.gridheight = 1;
        gridBag2.setConstraints(pdmNameTextField, gridBagCon2);
        cad3DNameTextField = new DynaTextField();
        cad3DNameTextField.setTitleText("3D Name");
        cad3DNameTextField.setTitleWidth(120);
        cad3DNameTextField.setTitleVisible(true);
        cad3DNameTextField.setEditable(true);
        cad3DNameTextField.addMouseListener(this);
        gridBagCon2.weightx = 1.0D;
        gridBagCon2.weighty = 0.0D;
        gridBagCon2.gridx = 0;
        gridBagCon2.gridy = 1;
        gridBagCon2.gridwidth = 1;
        gridBagCon2.gridheight = 1;
        gridBag2.setConstraints(cad3DNameTextField, gridBagCon2);
        cad2DNameTextField = new DynaTextField();
        cad2DNameTextField.setTitleText("2D Name");
        cad2DNameTextField.setTitleWidth(120);
        cad2DNameTextField.setTitleVisible(true);
        cad2DNameTextField.setEditable(true);
        cad2DNameTextField.addMouseListener(this);
        gridBagCon2.weightx = 1.0D;
        gridBagCon2.weighty = 0.0D;
        gridBagCon2.gridx = 0;
        gridBagCon2.gridy = 2;
        gridBagCon2.gridwidth = 1;
        gridBagCon2.gridheight = 1;
        gridBag2.setConstraints(cad2DNameTextField, gridBagCon2);
        typeComboBox = new DynaComboBox();
        typeComboBox.setTitleText("Type");
        typeComboBox.setTitleWidth(120);
        typeComboBox.setModel(typeComboModel);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        gridBagCon2.weightx = 1.0D;
        gridBagCon2.weighty = 0.0D;
        gridBagCon2.gridx = 0;
        gridBagCon2.gridy = 3;
        gridBagCon2.gridwidth = 1;
        gridBagCon2.gridheight = 1;
        gridBag2.setConstraints(typeComboBox, gridBagCon2);
        typeClassTextField = new DynaTextField();
        typeClassTextField.setTitleText("Type Class");
        typeClassTextField.setTitleWidth(120);
        typeClassTextField.setTitleVisible(true);
        typeClassTextField.setEditable(false);
        typeClassTextField.addMouseListener(this);
        gridBagCon2.weightx = 1.0D;
        gridBagCon2.weighty = 0.0D;
        gridBagCon2.gridx = 0;
        gridBagCon2.gridy = 4;
        gridBagCon2.gridwidth = 1;
        gridBagCon2.gridheight = 1;
        gridBag2.setConstraints(typeClassTextField, gridBagCon2);
        leftPanel = new JPanel();
        leftPanel.setLayout(gridBag2);
        leftPanel.add(pdmNameTextField);
        leftPanel.add(cad3DNameTextField);
        leftPanel.add(cad2DNameTextField);
        leftPanel.add(typeComboBox);
        leftPanel.add(typeClassTextField);
        userDefinedButton = new JRadioButton("User Defined");
        userDefinedButton.setSelected(true);
        basicDefinedButton = new JRadioButton("Basic Defined");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(userDefinedButton);
        buttonGroup.add(basicDefinedButton);
        javax.swing.border.Border lineBorder = BorderFactory.createLineBorder(Color.gray);
        TitledBorder border1 = BorderFactory.createTitledBorder(lineBorder, "Define Option");
        border1.setTitleJustification(1);
        group1Panel = new JPanel();
        group1Panel.setBorder(border1);
        group1Panel.setLayout(new GridLayout(1, 0));
        group1Panel.add(userDefinedButton);
        group1Panel.add(basicDefinedButton);
        useIn3DButton = new JCheckBox("Use In 3D");
        useIn2DButton = new JCheckBox("Use In 2D");
        TitledBorder border2 = BorderFactory.createTitledBorder(lineBorder, "Use Option");
        border2.setTitleJustification(1);
        group2Panel = new JPanel();
        group2Panel.setBorder(border2);
        group2Panel.setLayout(new GridLayout(1, 0));
        group2Panel.add(useIn3DButton);
        group2Panel.add(useIn2DButton);
        updateIn3DButton = new JCheckBox("Update In 3D");
        updateIn2DButton = new JCheckBox("Update In 2D");
        TitledBorder border3 = BorderFactory.createTitledBorder(lineBorder, "Update Option");
        border3.setTitleJustification(1);
        group3Panel = new JPanel();
        group3Panel.setBorder(border3);
        group3Panel.setLayout(new GridLayout(1, 0));
        group3Panel.add(updateIn3DButton);
        group3Panel.add(updateIn2DButton);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 0));
        rightPanel.add(group1Panel);
        rightPanel.add(group2Panel);
        rightPanel.add(group3Panel);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        makeAttributeTable();
        attributeTableScrPane = UIFactory.createStrippedScrollPane(null);
        attributeTableScrPane.setViewportView(attributeTable.getTable());
        attributeTableScrPane.setPreferredSize(new Dimension(300, 200));
        attributeTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        northPanel2 = new JPanel();
        northPanel2.setLayout(new BorderLayout());
        northPanel2.add(buttonToolBar2, "North");
        northPanel2.add(mainPanel, "Center");
        northPanel2.add(attributeTableScrPane, "South");
        previousButton2 = new JButton();
        previousButton2.setEnabled(true);
        previousButton2.setText("Previous");
        previousButton2.setActionCommand("previous2");
        previousButton2.setMargin(new Insets(0, 10, 0, 10));
        previousButton2.addActionListener(this);
        nextButton2 = new JButton();
        nextButton2.setEnabled(true);
        nextButton2.setText("Next");
        nextButton2.setActionCommand("next2");
        nextButton2.setMargin(new Insets(0, 10, 0, 10));
        nextButton2.addActionListener(this);
        buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2, 0));
        buttonPanel2.add(Box.createHorizontalGlue());
        buttonPanel2.add(previousButton2);
        buttonPanel2.add(Box.createRigidArea(new Dimension(10, 40)));
        buttonPanel2.add(nextButton2);
        step2Panel = new JPanel();
        step2Panel.setLayout(new BorderLayout());
        step2Panel.add(northPanel2, "North");
        step2Panel.add(buttonPanel2, "Center");
        GridBagLayout gridBag3 = new GridBagLayout();
        GridBagConstraints gridBagCon3 = new GridBagConstraints();
        gridBagCon3.fill = 1;
        gridBagCon3.anchor = 11;
        synchronizeButton = new JButton();
        synchronizeButton.setEnabled(true);
        synchronizeButton.setText("Synchronize DataBase");
        synchronizeButton.setActionCommand("synchronize DB");
        synchronizeButton.setMargin(new Insets(0, 0, 0, 0));
        synchronizeButton.addActionListener(this);
        gridBagCon3.weightx = 1.0D;
        gridBagCon3.weighty = 0.0D;
        gridBagCon3.gridx = 0;
        gridBagCon3.gridy = 0;
        gridBagCon3.gridwidth = 1;
        gridBagCon3.gridheight = 1;
        gridBagCon3.insets = new Insets(0, 50, 20, 50);
        gridBag3.setConstraints(synchronizeButton, gridBagCon3);
        cadcallButton = new JButton();
        cadcallButton.setEnabled(true);
        cadcallButton.setText("Set Attribute Position In 2D");
        cadcallButton.setActionCommand("call cad");
        cadcallButton.setMargin(new Insets(0, 0, 0, 0));
        cadcallButton.addActionListener(this);
        gridBagCon3.weightx = 1.0D;
        gridBagCon3.weighty = 0.0D;
        gridBagCon3.gridx = 0;
        gridBagCon3.gridy = 1;
        gridBagCon3.gridwidth = 1;
        gridBagCon3.gridheight = 1;
        gridBagCon3.insets = new Insets(0, 50, 20, 50);
        gridBag3.setConstraints(cadcallButton, gridBagCon3);
        positionsaveButton = new JButton();
        positionsaveButton.setEnabled(true);
        positionsaveButton.setText("Save Attribute Position In 2D");
        positionsaveButton.setActionCommand("save position");
        positionsaveButton.setMargin(new Insets(0, 0, 0, 0));
        positionsaveButton.addActionListener(this);
        gridBagCon3.weightx = 1.0D;
        gridBagCon3.weighty = 0.0D;
        gridBagCon3.gridx = 0;
        gridBagCon3.gridy = 2;
        gridBagCon3.gridwidth = 1;
        gridBagCon3.gridheight = 1;
        gridBagCon3.insets = new Insets(0, 50, 0, 50);
        gridBag3.setConstraints(positionsaveButton, gridBagCon3);
        northPanel3 = new JPanel();
        northPanel3.setBorder(BorderFactory.createEtchedBorder(1));
        northPanel3.setLayout(gridBag3);
        northPanel3.add(synchronizeButton);
        northPanel3.add(cadcallButton);
        northPanel3.add(positionsaveButton);
        previousButton3 = new JButton();
        previousButton3.setEnabled(true);
        previousButton3.setText("Previous");
        previousButton3.setActionCommand("previous3");
        previousButton3.setMargin(new Insets(0, 10, 0, 10));
        previousButton3.addActionListener(this);
        buttonPanel3 = new JPanel();
        buttonPanel3.setLayout(new BoxLayout(buttonPanel3, 0));
        buttonPanel3.add(Box.createHorizontalGlue());
        buttonPanel3.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel3.add(previousButton3);
        step3Panel = new JPanel();
        step3Panel.setLayout(new BorderLayout());
        step3Panel.add(northPanel3, "Center");
        step3Panel.add(buttonPanel3, "South");
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        tabbedPane.addChangeListener(this);
        tabbedPane.addTab("Step 1", step1Panel);
        tabbedPane.addTab("Step 2", step2Panel);
        tabbedPane.addTab("Step 3", step3Panel);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(2, false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, "Center");
        typeComboModel.enableDataLoad();
    }

    public void makeAttributeTable()
    {
        attributeData = new ArrayList();
        attributeColumnName = new ArrayList();
        attributeColumnWidth = new ArrayList();
        attributeColumnName.add("PDM Name");
        attributeColumnName.add("3D Name");
        attributeColumnName.add("2D Name");
        attributeColumnName.add("Type");
        attributeColumnName.add("Type Class");
        attributeColumnName.add("Define Option");
        attributeColumnName.add("Use Option");
        attributeColumnName.add("Update Option");
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeColumnWidth.add(new Integer(100));
        attributeTable = new Table(attributeData, (ArrayList)attributeColumnName.clone(), (ArrayList)attributeColumnWidth.clone(), 0);
        attributeTable.getTable().addMouseListener(this);
        attributeTable.setColumnSequence(new int[] {
            1, 2, 3, 4, 6, 7, 8, 9
        });
        attributeTable.setIndexColumn(0);
    }

    public void setClassTypeOID(Component target, ArrayList oidAL)
    {
        if(target == typeClassTextField.getTextField())
            typeClassTextField.setText((String)oidAL.get(0));
        else
        if(target == sizeTypeClassTextField.getTextField())
            sizeTypeClassTextField.setText((String)oidAL.get(0));
        else
        if(target == scaleTypeClassTextField.getTextField())
            scaleTypeClassTextField.setText((String)oidAL.get(0));
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("modify1"))
            modifyButton1Click_actionPerfromed(e);
        else
        if(command.equals("refresh1"))
            refreshButton1Click_actionPerformed(e);
        else
        if(command.equals("size select"))
            sizeSelectButtonClick_actionPerformed(e);
        else
        if(command.equals("scale select"))
            scaleSelectButtonClick_actionPerformed(e);
        else
        if(command.equals("create2"))
            createButton2Click_actionPerformed(e);
        else
        if(command.equals("modify2"))
            modifyButton2Click_actionPerformed(e);
        else
        if(command.equals("clear2"))
            clearButton2Click_actionPerformed(e);
        else
        if(command.equals("delete2"))
            deleteButton2Click_actionPerformed(e);
        else
        if(command.equals("refresh2"))
            refreshButton2Click_actionPerformed(e);
        else
        if(command.equals("synchronize DB"))
            try
            {
                ddg.generateDatabase(modelOuid);
            }
            catch(IIPRequestException ee)
            {
                System.err.println(ee);
            }
        else
        if(command.equals("call cad"))
        {
            utils.writeInitFile(0, "attribute.ini", null);
            try
            {
                String cmdArray[] = new String[1];
                cmdArray[0] = System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "swAttributeWizard.exe";
                Process proc = Runtime.getRuntime().exec(cmdArray);
                proc = null;
            }
            catch(Exception ie) { }
        } else
        if(command.equals("save position"))
            utils.saveAttributePosition("attribute.ini");
        else
        if(command.equals("next1"))
        {
            tabbedPane.setEnabledAt(0, false);
            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setSelectedIndex(1);
        } else
        if(command.equals("previous2"))
        {
            tabbedPane.setEnabledAt(1, false);
            tabbedPane.setEnabledAt(0, true);
            tabbedPane.setSelectedIndex(0);
        } else
        if(command.equals("next2"))
        {
            tabbedPane.setEnabledAt(1, false);
            tabbedPane.setEnabledAt(2, true);
            tabbedPane.setSelectedIndex(2);
        } else
        if(command.equals("previous3"))
        {
            tabbedPane.setEnabledAt(2, false);
            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setSelectedIndex(1);
        }
    }

    private void modifyButton1Click_actionPerfromed(ActionEvent e)
    {
        try
        {
            DOSChangeable fieldInfoDC = dos.getFieldWithName(classOuid, "Size");
            DOSChangeable setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", (String)fieldInfoDC.get("ouid"));
            setInfoDC.put("name", (String)fieldInfoDC.get("name"));
            setInfoDC.put("description", (String)fieldInfoDC.get("description"));
            setInfoDC.put("ouid@class", (String)fieldInfoDC.get("ouid@class"));
            setInfoDC.put("type", (Byte)fieldInfoDC.get("type"));
            setInfoDC.put("type.ouid@class", sizeTypeClassTextField.getText());
            setInfoDC.put("multiplicity.from", (Integer)fieldInfoDC.get("multiplicity.from"));
            setInfoDC.put("multiplicity.to", (Integer)fieldInfoDC.get("multiplicity.to"));
            setInfoDC.put("changeable", (Byte)fieldInfoDC.get("changeable"));
            setInfoDC.put("scope", (Byte)fieldInfoDC.get("scope"));
            setInfoDC.put("initial.value", (String)fieldInfoDC.get("initial.value"));
            setInfoDC.put("index", (Integer)fieldInfoDC.get("index"));
            setInfoDC.put("code", (String)fieldInfoDC.get("code"));
            setInfoDC.put("is.mandatory", (Boolean)fieldInfoDC.get("is.mandatory"));
            setInfoDC.put("is.visible", (Boolean)fieldInfoDC.get("is.visible"));
            setInfoDC.put("size", (Double)fieldInfoDC.get("size"));
            setInfoDC.put("title", (String)fieldInfoDC.get("title"));
            setInfoDC.put("tooltip", (String)fieldInfoDC.get("tooltip"));
            setInfoDC.put("width", (Integer)fieldInfoDC.get("width"));
            setInfoDC.put("height", (Integer)fieldInfoDC.get("height"));
            setInfoDC.put("title.width", (Integer)fieldInfoDC.get("title.width"));
            setInfoDC.put("is.title.visible", (Boolean)fieldInfoDC.get("is.title.visible"));
            setInfoDC.put("icon", (String)fieldInfoDC.get("icon"));
            dos.setField(setInfoDC);
            setInfoDC = null;
            fieldInfoDC = null;
            fieldInfoDC = dos.getFieldWithName(classOuid, "Scale");
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", (String)fieldInfoDC.get("ouid"));
            setInfoDC.put("name", (String)fieldInfoDC.get("name"));
            setInfoDC.put("description", (String)fieldInfoDC.get("description"));
            setInfoDC.put("ouid@class", (String)fieldInfoDC.get("ouid@class"));
            setInfoDC.put("type", (Byte)fieldInfoDC.get("type"));
            setInfoDC.put("type.ouid@class", scaleTypeClassTextField.getText());
            setInfoDC.put("multiplicity.from", (Integer)fieldInfoDC.get("multiplicity.from"));
            setInfoDC.put("multiplicity.to", (Integer)fieldInfoDC.get("multiplicity.to"));
            setInfoDC.put("changeable", (Byte)fieldInfoDC.get("changeable"));
            setInfoDC.put("scope", (Byte)fieldInfoDC.get("scope"));
            setInfoDC.put("initial.value", (String)fieldInfoDC.get("initial.value"));
            setInfoDC.put("index", (Integer)fieldInfoDC.get("index"));
            setInfoDC.put("code", (String)fieldInfoDC.get("code"));
            setInfoDC.put("is.mandatory", (Boolean)fieldInfoDC.get("is.mandatory"));
            setInfoDC.put("is.visible", (Boolean)fieldInfoDC.get("is.visible"));
            setInfoDC.put("size", (Double)fieldInfoDC.get("size"));
            setInfoDC.put("title", (String)fieldInfoDC.get("title"));
            setInfoDC.put("tooltip", (String)fieldInfoDC.get("tooltip"));
            setInfoDC.put("width", (Integer)fieldInfoDC.get("width"));
            setInfoDC.put("height", (Integer)fieldInfoDC.get("height"));
            setInfoDC.put("title.width", (Integer)fieldInfoDC.get("title.width"));
            setInfoDC.put("is.title.visible", (Boolean)fieldInfoDC.get("is.title.visible"));
            setInfoDC.put("icon", (String)fieldInfoDC.get("icon"));
            dos.setField(setInfoDC);
            setInfoDC = null;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void refreshButton1Click_actionPerformed(ActionEvent e)
    {
        try
        {
            DOSChangeable sizeFieldData = dos.getFieldWithName(classOuid, "Size");
            DOSChangeable scaleFieldData = dos.getFieldWithName(classOuid, "Scale");
            sizeTypeTextField.setText(getTypeString((Byte)sizeFieldData.get("type")));
            if(sizeFieldData.get("type.ouid@class") != null)
                sizeTypeClassTextField.setText((String)sizeFieldData.get("type.ouid@class"));
            scaleTypeTextField.setText(getTypeString((Byte)scaleFieldData.get("type")));
            if(scaleFieldData.get("type.ouid@class") != null)
                scaleTypeClassTextField.setText((String)scaleFieldData.get("type.ouid@class"));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void sizeSelectButtonClick_actionPerformed(ActionEvent e)
    {
        if(sizeTypeTextField.getText().equals("object"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, sizeTypeClassTextField.getTextField(), "Class Type Selection");
            typeClassFrame.setVisible(true);
        } else
        if(sizeTypeTextField.getText().equals("code (Field Referenced)"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, sizeTypeClassTextField.getTextField(), "Field Selection", classOuid, 1);
            typeClassFrame.setVisible(true);
        } else
        if(sizeTypeTextField.getText().equals("code"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, sizeTypeClassTextField.getTextField(), "Code Selection", "Code");
            typeClassFrame.setVisible(true);
        }
    }

    private void scaleSelectButtonClick_actionPerformed(ActionEvent e)
    {
        if(scaleTypeTextField.getText().equals("object"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, scaleTypeClassTextField.getTextField(), "Class Type Selection");
            typeClassFrame.setVisible(true);
        } else
        if(scaleTypeTextField.getText().equals("code (Field Referenced)"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, scaleTypeClassTextField.getTextField(), "Field Selection", classOuid, 1);
            typeClassFrame.setVisible(true);
        } else
        if(scaleTypeTextField.getText().equals("code"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, scaleTypeClassTextField.getTextField(), "Code Selection", "Code");
            typeClassFrame.setVisible(true);
        }
    }

    private void createButton2Click_actionPerformed(ActionEvent e)
    {
        try
        {
            HashMap tmpMap = dos.maxAttributeNameAndIndex(classOuid);
            String maxName = (String)tmpMap.get("maxname");
            Integer maxIndex = (Integer)tmpMap.get("maxindex");
            DOSChangeable setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", "");
            if(Utils.isNullString(maxName))
            {
                setInfoDC.put("name", "Attr1");
            } else
            {
                int cnt = Integer.parseInt(maxName.substring(4, maxName.length()));
                setInfoDC.put("name", "Attr" + (cnt + 1));
            }
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", classOuid);
            setInfoDC.put("type", (Byte)typeComboModel.getSelectedOID());
            setInfoDC.put("type.ouid@class", typeClassTextField.getText());
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            if(maxIndex == null)
            {
                setInfoDC.put("index", new Integer(1));
            } else
            {
                int cnt = maxIndex.intValue();
                setInfoDC.put("index", new Integer(cnt + 1));
            }
            if(Utils.isNullString(maxName))
            {
                setInfoDC.put("code", "ATTR1");
            } else
            {
                int cnt = Integer.parseInt(maxName.substring(4, maxName.length()));
                setInfoDC.put("code", "ATTR" + (cnt + 1));
            }
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", pdmNameTextField.getText());
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            String fieldOuid = dos.createField(setInfoDC);
            setInfoDC = null;
            DOSChangeable fieldGroupData = dos.getFieldGroupWithName(classOuid, "Attribute");
            if(fieldGroupData == null)
            {
                setInfoDC = new DOSChangeable();
                setInfoDC.put("ouid", "");
                setInfoDC.put("name", "Attribute");
                setInfoDC.put("description", "");
                setInfoDC.put("ouid@class", classOuid);
                setInfoDC.put("index", new Integer(2));
                setInfoDC.put("is.mandatory", new Boolean(true));
                setInfoDC.put("title", "Attribute");
                setInfoDC.put("tooltip", "");
                setInfoDC.put("is.visible", new Boolean(true));
                setInfoDC.put("icon", null);
                ArrayList fieldOuidList = new ArrayList();
                fieldOuidList.add(fieldOuid);
                setInfoDC.put("array$ouid@field", fieldOuidList);
                setInfoDC.put("array$ouid@action", null);
                String fieldGroupOuid = dos.createFieldGroup(setInfoDC);
                setInfoDC.put("ouid", fieldGroupOuid);
                setInfoDC = null;
            } else
            {
                setInfoDC = new DOSChangeable();
                setInfoDC.put("ouid", (String)fieldGroupData.get("ouid"));
                setInfoDC.put("name", (String)fieldGroupData.get("name"));
                setInfoDC.put("description", "");
                setInfoDC.put("ouid@class", (String)fieldGroupData.get("ouid@class"));
                setInfoDC.put("index", new Integer(2));
                setInfoDC.put("is.mandatory", (Boolean)fieldGroupData.get("is.mandatory"));
                setInfoDC.put("title", (String)fieldGroupData.get("title"));
                setInfoDC.put("tooltip", "");
                setInfoDC.put("is.visible", (Boolean)fieldGroupData.get("is.visible"));
                setInfoDC.put("icon", null);
                ArrayList fieldOuidList = (ArrayList)fieldGroupData.get("array$ouid@field");
                fieldOuidList.add(fieldOuid);
                setInfoDC.put("array$ouid@field", fieldOuidList);
                setInfoDC.put("array$ouid@action", null);
                dos.setFieldGroup(setInfoDC);
                setInfoDC = null;
            }
            if(fieldOuid == null)
                return;
            DOSChangeable dosAttribute = new DOSChangeable();
            dosAttribute.put("fieldouid", fieldOuid);
            dosAttribute.put("pdmname", pdmNameTextField.getText());
            dosAttribute.put("cad3dname", cad3DNameTextField.getText());
            dosAttribute.put("cad2dname", cad2DNameTextField.getText());
            int option = 0;
            if(userDefinedButton.isSelected())
                option += 2;
            if(basicDefinedButton.isSelected())
                option++;
            dosAttribute.put("defineoption", new Integer(option));
            option = 0;
            if(useIn3DButton.isSelected())
                option += 2;
            if(useIn2DButton.isSelected())
                option++;
            dosAttribute.put("useoption", new Integer(option));
            option = 0;
            if(updateIn3DButton.isSelected())
                option += 2;
            if(updateIn2DButton.isSelected())
                option++;
            dosAttribute.put("updateoption", new Integer(option));
            dos.createCADAttribute(dosAttribute);
            ArrayList rowList = new ArrayList();
            rowList.add((String)dosAttribute.get("fieldouid"));
            rowList.add((String)dosAttribute.get("pdmname"));
            rowList.add((String)dosAttribute.get("cad3dname"));
            rowList.add((String)dosAttribute.get("cad2dname"));
            rowList.add((String)typeComboModel.getSelectedItem());
            rowList.add((Byte)typeComboModel.getSelectedOID());
            rowList.add(typeClassTextField.getText());
            rowList.add((Integer)dosAttribute.get("defineoption"));
            rowList.add((Integer)dosAttribute.get("useoption"));
            rowList.add((Integer)dosAttribute.get("updateoption"));
            attributeData.add(rowList);
            rowList = null;
            dosAttribute = null;
            attributeTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void modifyButton2Click_actionPerformed(ActionEvent e)
    {
        try
        {
            int selectedRow = attributeTable.getTable().getSelectedRow();
            ArrayList selectedRowData = attributeTable.getSelectedRow();
            String fieldOuid = (String)selectedRowData.get(0);
            DOSChangeable fieldInfoDC = dos.getField(fieldOuid);
            DOSChangeable setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", (String)fieldInfoDC.get("ouid"));
            setInfoDC.put("name", (String)fieldInfoDC.get("name"));
            setInfoDC.put("description", "");
            setInfoDC.put("ouid@class", (String)fieldInfoDC.get("ouid@class"));
            setInfoDC.put("type", (Byte)typeComboModel.getSelectedOID());
            setInfoDC.put("type.ouid@class", typeClassTextField.getText());
            setInfoDC.put("multiplicity.from", null);
            setInfoDC.put("multiplicity.to", null);
            setInfoDC.put("changeable", null);
            setInfoDC.put("scope", null);
            setInfoDC.put("initial.value", null);
            setInfoDC.put("index", (Integer)fieldInfoDC.get("index"));
            setInfoDC.put("code", (String)fieldInfoDC.get("code"));
            setInfoDC.put("is.mandatory", new Boolean(false));
            setInfoDC.put("is.visible", new Boolean(true));
            setInfoDC.put("size", null);
            setInfoDC.put("title", pdmNameTextField.getText());
            setInfoDC.put("tooltip", null);
            setInfoDC.put("width", null);
            setInfoDC.put("height", null);
            setInfoDC.put("title.width", null);
            setInfoDC.put("is.title.visible", new Boolean(true));
            setInfoDC.put("icon", null);
            dos.setField(setInfoDC);
            setInfoDC = null;
            DOSChangeable dosAttribute = new DOSChangeable();
            dosAttribute.put("fieldouid", fieldOuid);
            dosAttribute.put("pdmname", pdmNameTextField.getText());
            dosAttribute.put("cad3dname", cad3DNameTextField.getText());
            dosAttribute.put("cad2dname", cad2DNameTextField.getText());
            int option = 0;
            if(userDefinedButton.isSelected())
                option += 2;
            if(basicDefinedButton.isSelected())
                option++;
            dosAttribute.put("defineoption", new Integer(option));
            option = 0;
            if(useIn3DButton.isSelected())
                option += 2;
            if(useIn2DButton.isSelected())
                option++;
            dosAttribute.put("useoption", new Integer(option));
            option = 0;
            if(updateIn3DButton.isSelected())
                option += 2;
            if(updateIn2DButton.isSelected())
                option++;
            dosAttribute.put("updateoption", new Integer(option));
            dos.setCADAttribute(dosAttribute);
            ArrayList rowList = new ArrayList();
            rowList.add((String)dosAttribute.get("fieldouid"));
            rowList.add((String)dosAttribute.get("pdmname"));
            rowList.add((String)dosAttribute.get("cad3dname"));
            rowList.add((String)dosAttribute.get("cad2dname"));
            rowList.add((String)typeComboModel.getSelectedItem());
            rowList.add((Byte)typeComboModel.getSelectedOID());
            rowList.add(typeClassTextField.getText());
            rowList.add((Integer)dosAttribute.get("defineoption"));
            rowList.add((Integer)dosAttribute.get("useoption"));
            rowList.add((Integer)dosAttribute.get("updateoption"));
            attributeData.set(selectedRow, rowList);
            rowList = null;
            dosAttribute = null;
            attributeTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void clearButton2Click_actionPerformed(ActionEvent e)
    {
        pdmNameTextField.setText("");
        cad3DNameTextField.setText("");
        cad2DNameTextField.setText("");
        typeComboModel.setElementAt(-1);
        typeClassTextField.setText("");
        userDefinedButton.setSelected(true);
        basicDefinedButton.setSelected(false);
        useIn3DButton.setSelected(false);
        useIn2DButton.setSelected(false);
        updateIn3DButton.setSelected(false);
        updateIn2DButton.setSelected(false);
    }

    private void deleteButton2Click_actionPerformed(ActionEvent e)
    {
        int selectedRow = attributeTable.getTable().getSelectedRow();
        ArrayList selectedRowData = attributeTable.getSelectedRow();
        try
        {
            dos.removeCADAttribute((String)selectedRowData.get(0));
            DOSChangeable fieldGroupData = dos.getFieldGroupWithName(classOuid, "Attribute");
            if(fieldGroupData != null)
            {
                DOSChangeable setInfoDC = new DOSChangeable();
                setInfoDC.put("ouid", (String)fieldGroupData.get("ouid"));
                setInfoDC.put("name", (String)fieldGroupData.get("name"));
                setInfoDC.put("description", "");
                setInfoDC.put("ouid@class", (String)fieldGroupData.get("ouid@class"));
                setInfoDC.put("index", (Integer)fieldGroupData.get("index"));
                setInfoDC.put("is.mandatory", (Boolean)fieldGroupData.get("is.mandatory"));
                setInfoDC.put("title", (String)fieldGroupData.get("title"));
                setInfoDC.put("tooltip", "");
                setInfoDC.put("is.visible", (Boolean)fieldGroupData.get("is.visible"));
                setInfoDC.put("icon", null);
                ArrayList fieldOuidList = (ArrayList)fieldGroupData.get("array$ouid@field");
                fieldOuidList.remove(fieldOuidList.indexOf((String)selectedRowData.get(0)));
                setInfoDC.put("array$ouid@field", fieldOuidList);
                setInfoDC.put("array$ouid@action", null);
                dos.setFieldGroup(setInfoDC);
                setInfoDC = null;
                if(fieldOuidList == null || fieldOuidList.size() == 0)
                    dos.removeFieldGroup((String)fieldGroupData.get("ouid"));
            }
            dos.removeField((String)selectedRowData.get(0));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        clearButton2Click_actionPerformed(e);
        if(selectedRow > -1)
        {
            attributeData.remove(selectedRow);
            attributeTable.changeTableData();
        }
    }

    private void refreshButton2Click_actionPerformed(ActionEvent e)
    {
        try
        {
            ArrayList attributeList = dos.listCADAttribute(classOuid);
            if(attributeList == null)
                return;
            attributeData.clear();
            for(int i = 0; i < attributeList.size(); i++)
            {
                DOSChangeable dosAttribute = (DOSChangeable)attributeList.get(i);
                ArrayList rowList = new ArrayList();
                rowList.add((String)dosAttribute.get("fieldouid"));
                rowList.add((String)dosAttribute.get("pdmname"));
                rowList.add((String)dosAttribute.get("cad3dname"));
                rowList.add((String)dosAttribute.get("cad2dname"));
                rowList.add(getTypeString((Byte)dosAttribute.get("type")));
                rowList.add((Byte)dosAttribute.get("type"));
                rowList.add((String)dosAttribute.get("type.ouid@class"));
                rowList.add((Integer)dosAttribute.get("defineoption"));
                rowList.add((Integer)dosAttribute.get("useoption"));
                rowList.add((Integer)dosAttribute.get("updateoption"));
                attributeData.add(rowList);
                rowList = null;
                attributeTable.changeTableData();
            }

        }
        catch(IIPRequestException ie)
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

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(typeClassTextField.getTextField()))
        {
            if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && typeComboModel.getSelectedItem().toString().equals("object"))
            {
                TypeClassSelection typeClassFrame = new TypeClassSelection(this, typeClassTextField.getTextField(), "Class Type Selection");
                typeClassFrame.setVisible(true);
            } else
            if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && typeComboModel.getSelectedItem().toString().equals("code (Field Referenced)"))
            {
                TypeClassSelection typeClassFrame = new TypeClassSelection(this, typeClassTextField.getTextField(), "Field Selection", classOuid, 1);
                typeClassFrame.setVisible(true);
            } else
            if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && typeComboModel.getSelectedItem().toString().equals("code"))
            {
                TypeClassSelection typeClassFrame = new TypeClassSelection(this, typeClassTextField.getTextField(), "Code Selection", "Code");
                typeClassFrame.setVisible(true);
            }
        } else
        if(source.equals(attributeTable.getTable()))
        {
            ArrayList selectedRowData = attributeTable.getSelectedRow();
            pdmNameTextField.setText((String)selectedRowData.get(1));
            cad3DNameTextField.setText((String)selectedRowData.get(2));
            cad2DNameTextField.setText((String)selectedRowData.get(3));
            typeComboBox.setSelectedItem((String)selectedRowData.get(4));
            typeComboBox.getComboBox().repaint();
            typeClassTextField.setText((String)selectedRowData.get(6));
            if(((Integer)selectedRowData.get(7)).intValue() == 2 || ((Integer)selectedRowData.get(7)).intValue() == 3)
                userDefinedButton.setSelected(true);
            else
                userDefinedButton.setSelected(false);
            if(((Integer)selectedRowData.get(7)).intValue() == 1 || ((Integer)selectedRowData.get(7)).intValue() == 3)
                basicDefinedButton.setSelected(true);
            else
                basicDefinedButton.setSelected(false);
            if(((Integer)selectedRowData.get(8)).intValue() == 2 || ((Integer)selectedRowData.get(8)).intValue() == 3)
                useIn3DButton.setSelected(true);
            else
                useIn3DButton.setSelected(false);
            if(((Integer)selectedRowData.get(8)).intValue() == 1 || ((Integer)selectedRowData.get(8)).intValue() == 3)
                useIn2DButton.setSelected(true);
            else
                useIn2DButton.setSelected(false);
            if(((Integer)selectedRowData.get(9)).intValue() == 2 || ((Integer)selectedRowData.get(9)).intValue() == 3)
                updateIn3DButton.setSelected(true);
            else
                updateIn3DButton.setSelected(false);
            if(((Integer)selectedRowData.get(9)).intValue() == 1 || ((Integer)selectedRowData.get(9)).intValue() == 3)
                updateIn2DButton.setSelected(true);
            else
                updateIn2DButton.setSelected(false);
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

    public void stateChanged(ChangeEvent e)
    {
        if(tabbedPane.getSelectedIndex() == 0)
            refreshButton1Click_actionPerformed(null);
        else
        if(tabbedPane.getSelectedIndex() == 1)
            refreshButton2Click_actionPerformed(null);
        else
            tabbedPane.getSelectedIndex();
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
        typeAL.add("string");
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
        typeAL.add("code");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)25));
        typeAL.add("code (Field Referenced)");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        return returnAL;
    }

    private JTabbedPane tabbedPane;
    private JPanel step1Panel;
    private JPanel step2Panel;
    private JPanel step3Panel;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel northPanel1;
    private JPanel northPanel2;
    private JPanel northPanel3;
    private JPanel group1Panel;
    private JPanel group2Panel;
    private JPanel group3Panel;
    private DynaTextField pdmNameTextField;
    private DynaTextField cad3DNameTextField;
    private DynaTextField cad2DNameTextField;
    private DynaComboBox typeComboBox;
    private DynaTextField typeClassTextField;
    private JRadioButton userDefinedButton;
    private JRadioButton basicDefinedButton;
    private ButtonGroup buttonGroup;
    private JCheckBox useIn3DButton;
    private JCheckBox useIn2DButton;
    private JCheckBox updateIn3DButton;
    private JCheckBox updateIn2DButton;
    private JScrollPane attributeTableScrPane;
    private Table attributeTable;
    private ArrayList attributeData;
    private ArrayList attributeColumnName;
    private ArrayList attributeColumnWidth;
    private DynaTextField sizeTypeTextField;
    private DynaTextField sizeTypeClassTextField;
    private JButton sizeSelectButton;
    private DynaTextField scaleTypeTextField;
    private DynaTextField scaleTypeClassTextField;
    private JButton scaleSelectButton;
    private JLabel dummyLabel;
    private JButton synchronizeButton;
    private JButton cadcallButton;
    private JButton positionsaveButton;
    private Cursor handCursor;
    private JToolBar buttonToolBar1;
    private BoxLayout buttonBoxLayout1;
    private JToolBar buttonToolBar2;
    private BoxLayout buttonBoxLayout2;
    private JButton modifyButton1;
    private JButton refreshButton1;
    private JButton createButton2;
    private JButton modifyButton2;
    private JButton clearButton2;
    private JButton deleteButton2;
    private JButton refreshButton2;
    private JPanel buttonPanel1;
    private JPanel buttonPanel2;
    private JPanel buttonPanel3;
    private JButton nextButton1;
    private JButton previousButton2;
    private JButton nextButton2;
    private JButton previousButton3;
    private DOS dos;
    private DDG ddg;
    private String modelOuid;
    private String classOuid;
    private int cadType;
    private DOSChangeable modelInfoDC;
    private final int TITLE_WIDTH = 120;
    private DynaComboTypeInstance dComboType;
    private DynaComboBoxModel typeComboModel;
    private CADIntegrationUtils utils;
}
