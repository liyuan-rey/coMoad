// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AssociationInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DTM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ClassInformation, ObjectModelingConstruction, TypeClassSelection

public class AssociationInformation extends JPanel
    implements ActionListener, MouseListener
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

    class DynaComboEndOneNavigableInstance extends DynaComboBoxDataLoader
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
            return setEndOneNavigableComboBox();
        }

        DynaComboEndOneNavigableInstance()
        {
        }
    }

    class DynaComboEndTwoNavigableInstance extends DynaComboBoxDataLoader
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
            return setEndTwoNavigableComboBox();
        }

        DynaComboEndTwoNavigableInstance()
        {
        }
    }

    class DynaComboEndOneOrderedInstance extends DynaComboBoxDataLoader
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
            return setEndOneOrderedComboBox();
        }

        DynaComboEndOneOrderedInstance()
        {
        }
    }

    class DynaComboEndTwoOrderedInstance extends DynaComboBoxDataLoader
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
            return setEndTwoOrderedComboBox();
        }

        DynaComboEndTwoOrderedInstance()
        {
        }
    }

    class DynaComboEndOneChangeableInstance extends DynaComboBoxDataLoader
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
            return setEndOneChangeableComboBox();
        }

        DynaComboEndOneChangeableInstance()
        {
        }
    }

    class DynaComboEndTwoChangeableInstance extends DynaComboBoxDataLoader
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
            return setEndTwoChangeableComboBox();
        }

        DynaComboEndTwoChangeableInstance()
        {
        }
    }

    class DynaComboEndOneReleaseCascadeInstance extends DynaComboBoxDataLoader
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
            return setEndOneReleaseCascadeComboBox();
        }

        DynaComboEndOneReleaseCascadeInstance()
        {
        }
    }

    class DynaComboEndTwoReleaseCascadeInstance extends DynaComboBoxDataLoader
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
            return setEndTwoReleaseCascadeComboBox();
        }

        DynaComboEndTwoReleaseCascadeInstance()
        {
        }
    }

    class DynaComboEndOneCloneCascadeInstance extends DynaComboBoxDataLoader
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
            return setEndOneCloneCascadeComboBox();
        }

        DynaComboEndOneCloneCascadeInstance()
        {
        }
    }

    class DynaComboEndTwoCloneCascadeInstance extends DynaComboBoxDataLoader
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
            return setEndTwoCloneCascadeComboBox();
        }

        DynaComboEndTwoCloneCascadeInstance()
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


    public AssociationInformation(Object parentFrame)
    {
        imageEndOneClass = new ImageIcon("icons/AssociationEnd1.gif");
        imageEndTwoClass = new ImageIcon("icons/AssociationEnd2.gif");
        associationToolBar = new JToolBar();
        associationBoxLayout = new BoxLayout(associationToolBar, 0);
        okButton = null;
        modifyButton = null;
        finishButton = null;
        removeButton = null;
        dos = null;
        dtm = null;
        dComboType = new DynaComboTypeInstance();
        dComboEndOneNavigable = new DynaComboEndOneNavigableInstance();
        dComboEndTwoNavigable = new DynaComboEndTwoNavigableInstance();
        dComboEndOneOrdered = new DynaComboEndOneOrderedInstance();
        dComboEndTwoOrdered = new DynaComboEndTwoOrderedInstance();
        dComboEndOneChangeable = new DynaComboEndOneChangeableInstance();
        dComboEndTwoChangeable = new DynaComboEndTwoChangeableInstance();
        dComboEndOneReleaseCascade = new DynaComboEndOneReleaseCascadeInstance();
        dComboEndTwoReleaseCascade = new DynaComboEndTwoReleaseCascadeInstance();
        dComboEndOneCloneCascade = new DynaComboEndOneCloneCascadeInstance();
        dComboEndTwoCloneCascade = new DynaComboEndTwoCloneCascadeInstance();
        dComboDataSourceID = new DynaComboDataSourceIDInstance();
        typeComboModel = new DynaComboBoxModel(dComboType);
        endOneNavigableComboModel = new DynaComboBoxModel(dComboEndOneNavigable);
        endTwoNavigableComboModel = new DynaComboBoxModel(dComboEndTwoNavigable);
        endOneOrderedComboModel = new DynaComboBoxModel(dComboEndOneOrdered);
        endTwoOrderedComboModel = new DynaComboBoxModel(dComboEndTwoOrdered);
        endOneChangeableComboModel = new DynaComboBoxModel(dComboEndOneChangeable);
        endTwoChangeableComboModel = new DynaComboBoxModel(dComboEndTwoChangeable);
        endOneReleaseCascadeComboModel = new DynaComboBoxModel(dComboEndOneReleaseCascade);
        endTwoReleaseCascadeComboModel = new DynaComboBoxModel(dComboEndTwoReleaseCascade);
        endOneCloneCascadeComboModel = new DynaComboBoxModel(dComboEndOneCloneCascade);
        endTwoCloneCascadeComboModel = new DynaComboBoxModel(dComboEndTwoCloneCascade);
        dataSourceIDComboModel = new DynaComboBoxModel(dComboDataSourceID);
        assoOuid = "";
        dosClassOuid = "";
        endOneClassOuid = "";
        endTwoClassOuid = "";
        try
        {
            parent = (ClassInformation)parentFrame;
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            dtm = (DTM)ObjectModelingConstruction.dfw.getServiceInstance("DF30DTM1");
            initialize();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void initialize()
    {
        okButton = new JButton();
        modifyButton = new JButton();
        finishButton = new JButton();
        removeButton = new JButton();
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        endOneClassScrPane = UIFactory.createStrippedScrollPane(null);
        endTwoClassScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel = new JPanel();
        endOnePanel = new JPanel();
        endTwoPanel = new JPanel();
        endClassTabbedPane = new JTabbedPane();
        endClassTabbedPane.setTabPlacement(1);
        endClassTabbedPane.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        associationInfoBorderLayout = new BorderLayout();
        setLayout(associationInfoBorderLayout);
        mainPanel.setLayout(new BoxLayout(mainPanel, 1));
        endOnePanel.setLayout(new BoxLayout(endOnePanel, 1));
        endTwoPanel.setLayout(new BoxLayout(endTwoPanel, 1));
        ouidTextField = new DynaTextField();
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setTitleWidth(140);
        ouidTextField.setTitleVisible(true);
        ouidTextField.setEditable(false);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(true);
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(140);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Description");
        descriptionTextField.setTitleWidth(140);
        descriptionTextField.setTitleVisible(true);
        descriptionTextField.setEditable(true);
        typeComboBox = new DynaComboBox();
        typeComboBox.setMandatory(true);
        typeComboBox.setTitleText("Type");
        typeComboBox.setTitleWidth(140);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        typeComboBox.setModel(typeComboModel);
        dosClassTextField = new DynaTextField();
        dosClassTextField.setMandatory(false);
        dosClassTextField.setTitleText("DOS Class");
        dosClassTextField.setTitleWidth(140);
        dosClassTextField.setTitleVisible(true);
        dosClassTextField.setEditable(true);
        dosClassTextField.addMouseListener(this);
        dataSourceIdComboBox = new DynaComboBox();
        dataSourceIdComboBox.setMandatory(false);
        dataSourceIdComboBox.setTitleText("Datasource id");
        dataSourceIdComboBox.setTitleWidth(140);
        dataSourceIdComboBox.setTitleVisible(true);
        dataSourceIdComboBox.setEditable(false);
        dataSourceIdComboBox.setModel(dataSourceIDComboModel);
        dataSourceIdComboBox.addMouseListener(this);
        codeTextField = new DynaTextField();
        codeTextField.setMandatory(true);
        codeTextField.setTitleText("Code");
        codeTextField.setTitleWidth(140);
        codeTextField.setTitleVisible(true);
        codeTextField.setEditable(true);
        codeTextField.addMouseListener(this);
        endOneClassTextField = new DynaTextField();
        endOneClassTextField.setMandatory(false);
        endOneClassTextField.setTitleText("End1 Class");
        endOneClassTextField.setTitleWidth(140);
        endOneClassTextField.setTitleVisible(true);
        endOneClassTextField.setEditable(true);
        endOneClassTextField.addMouseListener(this);
        endOneNameTextField = new DynaTextField();
        endOneNameTextField.setMandatory(false);
        endOneNameTextField.setTitleText("End1 Name");
        endOneNameTextField.setTitleWidth(140);
        endOneNameTextField.setTitleVisible(true);
        endOneNameTextField.setEditable(true);
        endOneMultiplicityFromTextField = new DynaTextField();
        endOneMultiplicityFromTextField.setMandatory(false);
        endOneMultiplicityFromTextField.setTitleText("End1 Multiplicity From");
        endOneMultiplicityFromTextField.setTitleWidth(140);
        endOneMultiplicityFromTextField.setTitleVisible(true);
        endOneMultiplicityFromTextField.setEditable(true);
        endOneMultiplicityToTextField = new DynaTextField();
        endOneMultiplicityToTextField.setMandatory(false);
        endOneMultiplicityToTextField.setTitleText("End1 Multiplicity To");
        endOneMultiplicityToTextField.setTitleWidth(140);
        endOneMultiplicityToTextField.setTitleVisible(true);
        endOneMultiplicityToTextField.setEditable(true);
        endOneIsNavigableComboBox = new DynaComboBox();
        endOneIsNavigableComboBox.setMandatory(false);
        endOneIsNavigableComboBox.setTitleText("End1 is Navigable");
        endOneIsNavigableComboBox.setTitleWidth(140);
        endOneIsNavigableComboBox.setTitleVisible(true);
        endOneIsNavigableComboBox.setEditable(false);
        endOneIsNavigableComboBox.setModel(endOneNavigableComboModel);
        endOneIsOrderedComboBox = new DynaComboBox();
        endOneIsOrderedComboBox.setMandatory(false);
        endOneIsOrderedComboBox.setTitleText("End1 is Ordered");
        endOneIsOrderedComboBox.setTitleWidth(140);
        endOneIsOrderedComboBox.setTitleVisible(true);
        endOneIsOrderedComboBox.setEditable(false);
        endOneIsOrderedComboBox.setModel(endOneOrderedComboModel);
        endOneChangeableComboBox = new DynaComboBox();
        endOneChangeableComboBox.setMandatory(false);
        endOneChangeableComboBox.setTitleText("End1 Changeable");
        endOneChangeableComboBox.setTitleWidth(140);
        endOneChangeableComboBox.setTitleVisible(true);
        endOneChangeableComboBox.setEditable(false);
        endOneChangeableComboBox.setModel(endOneChangeableComboModel);
        endOneReleaseCascadeComboBox = new DynaComboBox();
        endOneReleaseCascadeComboBox.setMandatory(false);
        endOneReleaseCascadeComboBox.setTitleText("End1 Release Cascade");
        endOneReleaseCascadeComboBox.setTitleWidth(140);
        endOneReleaseCascadeComboBox.setTitleVisible(true);
        endOneReleaseCascadeComboBox.setEditable(false);
        endOneReleaseCascadeComboBox.setModel(endOneReleaseCascadeComboModel);
        endOneCloneCascadeComboBox = new DynaComboBox();
        endOneCloneCascadeComboBox.setMandatory(false);
        endOneCloneCascadeComboBox.setTitleText("End1 Clone Cascade");
        endOneCloneCascadeComboBox.setTitleWidth(140);
        endOneCloneCascadeComboBox.setTitleVisible(true);
        endOneCloneCascadeComboBox.setEditable(false);
        endOneCloneCascadeComboBox.setModel(endOneCloneCascadeComboModel);
        endTwoClassTextField = new DynaTextField();
        endTwoClassTextField.setMandatory(false);
        endTwoClassTextField.setTitleText("End2 Class");
        endTwoClassTextField.setTitleWidth(140);
        endTwoClassTextField.setTitleVisible(true);
        endTwoClassTextField.setEditable(true);
        endTwoClassTextField.addMouseListener(this);
        endTwoNameTextField = new DynaTextField();
        endTwoNameTextField.setMandatory(false);
        endTwoNameTextField.setTitleText("End2 Name");
        endTwoNameTextField.setTitleWidth(140);
        endTwoNameTextField.setTitleVisible(true);
        endTwoNameTextField.setEditable(true);
        endTwoMultiplicityFromTextField = new DynaTextField();
        endTwoMultiplicityFromTextField.setMandatory(false);
        endTwoMultiplicityFromTextField.setTitleText("End2 Multiplicity From");
        endTwoMultiplicityFromTextField.setTitleWidth(140);
        endTwoMultiplicityFromTextField.setTitleVisible(true);
        endTwoMultiplicityFromTextField.setEditable(true);
        endTwoMultiplicityToTextField = new DynaTextField();
        endTwoMultiplicityToTextField.setMandatory(false);
        endTwoMultiplicityToTextField.setTitleText("End2 Multiplicity To");
        endTwoMultiplicityToTextField.setTitleWidth(140);
        endTwoMultiplicityToTextField.setTitleVisible(true);
        endTwoMultiplicityToTextField.setEditable(true);
        endTwoIsNavigableComboBox = new DynaComboBox();
        endTwoIsNavigableComboBox.setMandatory(false);
        endTwoIsNavigableComboBox.setTitleText("End2 is Navigable");
        endTwoIsNavigableComboBox.setTitleWidth(140);
        endTwoIsNavigableComboBox.setTitleVisible(true);
        endTwoIsNavigableComboBox.setEditable(false);
        endTwoIsNavigableComboBox.setModel(endTwoNavigableComboModel);
        endTwoIsOrderedComboBox = new DynaComboBox();
        endTwoIsOrderedComboBox.setMandatory(false);
        endTwoIsOrderedComboBox.setTitleText("End2 is Ordered");
        endTwoIsOrderedComboBox.setTitleWidth(140);
        endTwoIsOrderedComboBox.setTitleVisible(true);
        endTwoIsOrderedComboBox.setEditable(false);
        endTwoIsOrderedComboBox.setModel(endTwoOrderedComboModel);
        endTwoChangeableComboBox = new DynaComboBox();
        endTwoChangeableComboBox.setMandatory(false);
        endTwoChangeableComboBox.setTitleText("End2 Changeable");
        endTwoChangeableComboBox.setTitleWidth(140);
        endTwoChangeableComboBox.setTitleVisible(true);
        endTwoChangeableComboBox.setEditable(false);
        endTwoChangeableComboBox.setModel(endTwoChangeableComboModel);
        endTwoReleaseCascadeComboBox = new DynaComboBox();
        endTwoReleaseCascadeComboBox.setMandatory(false);
        endTwoReleaseCascadeComboBox.setTitleText("End2 Release Cascade");
        endTwoReleaseCascadeComboBox.setTitleWidth(140);
        endTwoReleaseCascadeComboBox.setTitleVisible(true);
        endTwoReleaseCascadeComboBox.setEditable(false);
        endTwoReleaseCascadeComboBox.setModel(endTwoReleaseCascadeComboModel);
        endTwoCloneCascadeComboBox = new DynaComboBox();
        endTwoCloneCascadeComboBox.setMandatory(false);
        endTwoCloneCascadeComboBox.setTitleText("End2 Clone Cascade");
        endTwoCloneCascadeComboBox.setTitleWidth(140);
        endTwoCloneCascadeComboBox.setTitleVisible(true);
        endTwoCloneCascadeComboBox.setEditable(false);
        endTwoCloneCascadeComboBox.setModel(endTwoCloneCascadeComboModel);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(descriptionTextField);
        mainPanel.add(typeComboBox);
        mainPanel.add(dosClassTextField);
        mainPanel.add(dataSourceIdComboBox);
        mainPanel.add(codeTextField);
        endOnePanel.add(endOneClassTextField);
        endOnePanel.add(endOneNameTextField);
        endOnePanel.add(endOneMultiplicityFromTextField);
        endOnePanel.add(endOneMultiplicityToTextField);
        endOnePanel.add(endOneIsNavigableComboBox);
        endOnePanel.add(endOneIsOrderedComboBox);
        endOnePanel.add(endOneChangeableComboBox);
        endOnePanel.add(endOneReleaseCascadeComboBox);
        endOnePanel.add(endOneCloneCascadeComboBox);
        endTwoPanel.add(endTwoClassTextField);
        endTwoPanel.add(endTwoNameTextField);
        endTwoPanel.add(endTwoMultiplicityFromTextField);
        endTwoPanel.add(endTwoMultiplicityToTextField);
        endTwoPanel.add(endTwoIsNavigableComboBox);
        endTwoPanel.add(endTwoIsOrderedComboBox);
        endTwoPanel.add(endTwoChangeableComboBox);
        endTwoPanel.add(endTwoReleaseCascadeComboBox);
        endTwoPanel.add(endTwoCloneCascadeComboBox);
        endOneClassScrPane.setViewportView(endOnePanel);
        endTwoClassScrPane.setViewportView(endTwoPanel);
        endClassTabbedPane.addTab("End 1", imageEndOneClass, endOneClassScrPane, "End 1 Class Information");
        endClassTabbedPane.addTab("End 2", imageEndTwoClass, endTwoClassScrPane, "End 2 Class Information");
        mainScrPane.setViewportView(mainPanel);
        okButton.setEnabled(true);
        okButton.setToolTipText("create");
        okButton.setActionCommand("create");
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/Registry.gif")));
        okButton.addActionListener(this);
        modifyButton.setEnabled(true);
        modifyButton.setToolTipText("modify");
        modifyButton.setActionCommand("modify");
        modifyButton.setMargin(new Insets(0, 0, 0, 0));
        modifyButton.setIcon(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
        modifyButton.addActionListener(this);
        finishButton.setToolTipText("clear");
        finishButton.setActionCommand("clear");
        finishButton.setMargin(new Insets(0, 0, 0, 0));
        finishButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        finishButton.addActionListener(this);
        removeButton.setToolTipText("delete");
        removeButton.setActionCommand("delete");
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        removeButton.addActionListener(this);
        associationToolBar.add(okButton);
        associationToolBar.add(modifyButton);
        associationToolBar.add(finishButton);
        associationToolBar.add(removeButton);
        mainSplitPane = new JSplitPane(0, mainScrPane, endClassTabbedPane);
        mainSplitPane.setDividerSize(3);
        mainSplitPane.setDividerLocation(170);
        add(associationToolBar, "North");
        add(mainSplitPane, "Center");
        typeComboModel.enableDataLoad();
        endOneNavigableComboModel.enableDataLoad();
        endTwoNavigableComboModel.enableDataLoad();
        endOneOrderedComboModel.enableDataLoad();
        endTwoOrderedComboModel.enableDataLoad();
        endOneChangeableComboModel.enableDataLoad();
        endTwoChangeableComboModel.enableDataLoad();
        endOneReleaseCascadeComboModel.enableDataLoad();
        endTwoReleaseCascadeComboModel.enableDataLoad();
        endOneCloneCascadeComboModel.enableDataLoad();
        endTwoCloneCascadeComboModel.enableDataLoad();
        dataSourceIDComboModel.enableDataLoad();
    }

    public DOSChangeable createAssociation(ActionEvent e)
    {
        DOSChangeable associationDefinition = new DOSChangeable();
        String tempString = null;
        try
        {
            associationDefinition.put("name", nameTextField.getText());
            associationDefinition.put("type", (Byte)typeComboModel.getSelectedOID());
            associationDefinition.put("ouid@class", dosClassOuid);
            associationDefinition.put("datasource.id", (String)dataSourceIDComboModel.getSelectedOID());
            if(Utils.isNullString(tempString))
            {
                tempString = (String)associationDefinition.get("name");
                tempString = tempString.trim().toUpperCase().replace(' ', ' ').replace('/', '$').replace('\\', '$').replace('.', '$');
            }
            associationDefinition.put("code", tempString);
            associationDefinition.put("end1.ouid@class", endOneClassOuid);
            associationDefinition.put("end1.name", endOneNameTextField.getText());
            associationDefinition.put("end1.multiplicity.from", Utils.getInteger(endOneMultiplicityFromTextField.getText()));
            associationDefinition.put("end1.multiplicity.to", Utils.getInteger(endOneMultiplicityToTextField.getText()));
            associationDefinition.put("end1.is.navigable", (Boolean)endOneNavigableComboModel.getSelectedOID());
            associationDefinition.put("end1.is.ordered", (Boolean)endOneOrderedComboModel.getSelectedOID());
            associationDefinition.put("end1.changeable", (Byte)endOneChangeableComboModel.getSelectedOID());
            associationDefinition.put("end1.cascade.release", (Boolean)endOneReleaseCascadeComboModel.getSelectedOID());
            associationDefinition.put("end1.cascade.clone", (Boolean)endOneCloneCascadeComboModel.getSelectedOID());
            associationDefinition.put("end2.ouid@class", endTwoClassOuid);
            associationDefinition.put("end2.name", endTwoNameTextField.getText());
            associationDefinition.put("end2.multiplicity.from", Utils.getInteger(endTwoMultiplicityFromTextField.getText()));
            associationDefinition.put("end2.multiplicity.to", Utils.getInteger(endTwoMultiplicityToTextField.getText()));
            associationDefinition.put("end2.is.navigable", (Boolean)endTwoNavigableComboModel.getSelectedOID());
            associationDefinition.put("end2.is.ordered", (Boolean)endTwoOrderedComboModel.getSelectedOID());
            associationDefinition.put("end2.changeable", (Byte)endTwoChangeableComboModel.getSelectedOID());
            associationDefinition.put("end2.cascade.release", (Boolean)endTwoReleaseCascadeComboModel.getSelectedOID());
            associationDefinition.put("end2.cascade.clone", (Boolean)endTwoCloneCascadeComboModel.getSelectedOID());
            String assoOuid = dos.createAssociation(associationDefinition);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        return associationDefinition;
    }

    public void setAssociation(ActionEvent e)
    {
        DOSChangeable associationDefinition = new DOSChangeable();
        try
        {
            associationDefinition.put("ouid", assoOuid);
            associationDefinition.put("name", nameTextField.getText());
            associationDefinition.put("type", (Byte)typeComboModel.getSelectedOID());
            associationDefinition.put("description", descriptionTextField.getText());
            if(Utils.isNullString(dosClassTextField.getText()))
                dosClassOuid = null;
            associationDefinition.put("ouid@class", dosClassOuid);
            associationDefinition.put("datasource.id", (String)dataSourceIDComboModel.getSelectedOID());
            associationDefinition.put("code", codeTextField.getText());
            associationDefinition.put("end1.ouid@class", endOneClassTextField.getText());
            associationDefinition.put("end1.name", endOneNameTextField.getText());
            associationDefinition.put("end1.multiplicity.from", Utils.getInteger(endOneMultiplicityFromTextField.getText()));
            associationDefinition.put("end1.multiplicity.to", Utils.getInteger(endOneMultiplicityToTextField.getText()));
            associationDefinition.put("end1.is.navigable", (Boolean)endOneNavigableComboModel.getSelectedOID());
            associationDefinition.put("end1.is.ordered", (Boolean)endOneOrderedComboModel.getSelectedOID());
            associationDefinition.put("end1.changeable", (Byte)endOneChangeableComboModel.getSelectedOID());
            associationDefinition.put("end1.cascade.release", (Boolean)endOneReleaseCascadeComboModel.getSelectedOID());
            associationDefinition.put("end1.cascade.clone", (Boolean)endOneCloneCascadeComboModel.getSelectedOID());
            associationDefinition.put("end2.ouid@class", endTwoClassTextField.getText());
            associationDefinition.put("end2.name", endTwoNameTextField.getText());
            associationDefinition.put("end2.multiplicity.from", Utils.getInteger(endTwoMultiplicityFromTextField.getText()));
            associationDefinition.put("end2.multiplicity.to", Utils.getInteger(endTwoMultiplicityToTextField.getText()));
            associationDefinition.put("end2.is.navigable", (Boolean)endTwoNavigableComboModel.getSelectedOID());
            associationDefinition.put("end2.is.ordered", (Boolean)endTwoOrderedComboModel.getSelectedOID());
            associationDefinition.put("end2.changeable", (Byte)endTwoChangeableComboModel.getSelectedOID());
            associationDefinition.put("end2.cascade.release", (Boolean)endTwoReleaseCascadeComboModel.getSelectedOID());
            associationDefinition.put("end2.cascade.clone", (Boolean)endTwoCloneCascadeComboModel.getSelectedOID());
            dos.setAssociation(associationDefinition);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void removeAssociation(ActionEvent e)
    {
        try
        {
            dos.removeAssociation(assoOuid);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void setEndOneClassOID(ArrayList oidAL)
    {
        endOneClassOuid = (String)oidAL.get(0);
        endOneClassTextField.setText(endOneClassOuid);
    }

    public void setEndTwoClassOID(ArrayList oidAL)
    {
        endTwoClassOuid = (String)oidAL.get(0);
        endTwoClassTextField.setText(endTwoClassOuid);
    }

    public void setDOSClassOID(ArrayList oidAL)
    {
        try
        {
            dosClassOuid = (String)oidAL.get(0);
            dosClassTextField.setText((String)dos.getClass(dosClassOuid).get("name"));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setAssociationInfoField(String ouid)
    {
        try
        {
            assoOuid = ouid;
            DOSChangeable associationInfoDC = new DOSChangeable();
            associationInfoDC = dos.getAssociation(ouid);
            ouidTextField.setText((String)associationInfoDC.get("ouid"));
            nameTextField.setText((String)associationInfoDC.get("name"));
            codeTextField.setText((String)associationInfoDC.get("code"));
            descriptionTextField.setText((String)associationInfoDC.get("description"));
            if(associationInfoDC.get("type") != null)
                typeComboModel.setSelectedItemByOID((Byte)associationInfoDC.get("type"));
            else
                typeComboModel.setElementAt(-1);
            if(!Utils.isNullString((String)associationInfoDC.get("ouid@class")))
            {
                dosClassOuid = (String)associationInfoDC.get("ouid@class");
                dosClassTextField.setText((String)dos.getClass((String)associationInfoDC.get("ouid@class")).get("name") + " [" + (String)associationInfoDC.get("ouid@class") + "]");
            } else
            {
                dosClassTextField.setText("");
            }
            dataSourceIDComboModel.setSelectedItem((String)associationInfoDC.get("datasource.id"));
            endOneClassTextField.setText((String)associationInfoDC.get("end1.ouid@class"));
            endOneNameTextField.setText((String)associationInfoDC.get("end1.name"));
            if(associationInfoDC.get("end1.multiplicity.from") != null)
                endOneMultiplicityFromTextField.setText(associationInfoDC.get("end1.multiplicity.from").toString());
            else
                endOneMultiplicityFromTextField.setText("");
            if(associationInfoDC.get("end1.multiplicity.to") != null)
                endOneMultiplicityToTextField.setText(associationInfoDC.get("end1.multiplicity.to").toString());
            else
                endOneMultiplicityToTextField.setText("");
            if(associationInfoDC.get("end1.is.navigable") != null)
                endOneNavigableComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end1.is.navigable")) ? 0 : 1);
            else
                endOneNavigableComboModel.setElementAt(-1);
            if(associationInfoDC.get("end1.is.ordered") != null)
                endOneOrderedComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end1.is.ordered")) ? 0 : 1);
            else
                endOneOrderedComboModel.setElementAt(-1);
            if(associationInfoDC.get("end1.changeable") != null)
                endOneChangeableComboModel.setElementAt(Utils.getByte((Byte)associationInfoDC.get("end1.changeable")) - 1);
            else
                endOneChangeableComboModel.setElementAt(-1);
            if(associationInfoDC.get("end1.cascade.release") != null)
                endOneReleaseCascadeComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end1.cascade.release")) ? 0 : 1);
            else
                endOneReleaseCascadeComboModel.setElementAt(-1);
            if(associationInfoDC.get("end1.cascade.clone") != null)
                endOneCloneCascadeComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end1.cascade.clone")) ? 0 : 1);
            else
                endOneCloneCascadeComboModel.setElementAt(-1);
            endTwoClassTextField.setText((String)associationInfoDC.get("end2.ouid@class"));
            endTwoNameTextField.setText((String)associationInfoDC.get("end2.name"));
            if(associationInfoDC.get("end2.multiplicity.from") != null)
                endTwoMultiplicityFromTextField.setText(associationInfoDC.get("end2.multiplicity.from").toString());
            else
                endTwoMultiplicityFromTextField.setText("");
            if(associationInfoDC.get("end2.multiplicity.to") != null)
                endTwoMultiplicityToTextField.setText(associationInfoDC.get("end2.multiplicity.to").toString());
            else
                endTwoMultiplicityToTextField.setText("");
            if(associationInfoDC.get("end2.is.navigable") != null)
                endTwoNavigableComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end2.is.navigable")) ? 0 : 1);
            else
                endTwoNavigableComboModel.setElementAt(-1);
            if(associationInfoDC.get("end2.is.ordered") != null)
                endTwoOrderedComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end2.is.ordered")) ? 0 : 1);
            else
                endTwoOrderedComboModel.setElementAt(-1);
            if(associationInfoDC.get("end2.changeable") != null)
                endTwoChangeableComboModel.setElementAt(Utils.getByte((Byte)associationInfoDC.get("end2.changeable")) - 1);
            else
                endTwoChangeableComboModel.setElementAt(-1);
            if(associationInfoDC.get("end2.cascade.release") != null)
                endTwoReleaseCascadeComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end2.cascade.release")) ? 0 : 1);
            else
                endTwoReleaseCascadeComboModel.setElementAt(-1);
            if(associationInfoDC.get("end2.cascade.clone") != null)
                endTwoCloneCascadeComboModel.setElementAt(Utils.getBoolean((Boolean)associationInfoDC.get("end2.cascade.clone")) ? 0 : 1);
            else
                endTwoCloneCascadeComboModel.setElementAt(-1);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        updateUI();
    }

    public ArrayList setTypeComboBox()
    {
        ArrayList typeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        typeAL.add(new Byte((byte)1));
        typeAL.add("Association");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)2));
        typeAL.add("Composition");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)3));
        typeAL.add("Aggregation");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        return returnAL;
    }

    public ArrayList setEndOneNavigableComboBox()
    {
        ArrayList endOneNavigableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endOneNavigableAL.add(new Boolean(true));
        endOneNavigableAL.add("true");
        returnAL.add(endOneNavigableAL.clone());
        endOneNavigableAL.clear();
        endOneNavigableAL.add(new Boolean(false));
        endOneNavigableAL.add("false");
        returnAL.add(endOneNavigableAL.clone());
        endOneNavigableAL.clear();
        return returnAL;
    }

    public ArrayList setEndTwoNavigableComboBox()
    {
        ArrayList endTwoNavigableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoNavigableAL.add(new Boolean(true));
        endTwoNavigableAL.add("true");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        endTwoNavigableAL.add(new Boolean(false));
        endTwoNavigableAL.add("false");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        return returnAL;
    }

    public ArrayList setEndOneOrderedComboBox()
    {
        ArrayList endOneOrderedAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endOneOrderedAL.add(new Boolean(true));
        endOneOrderedAL.add("true");
        returnAL.add(endOneOrderedAL.clone());
        endOneOrderedAL.clear();
        endOneOrderedAL.add(new Boolean(false));
        endOneOrderedAL.add("false");
        returnAL.add(endOneOrderedAL.clone());
        endOneOrderedAL.clear();
        return returnAL;
    }

    public ArrayList setEndTwoOrderedComboBox()
    {
        ArrayList endTwoOrderedAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoOrderedAL.add(new Boolean(true));
        endTwoOrderedAL.add("true");
        returnAL.add(endTwoOrderedAL.clone());
        endTwoOrderedAL.clear();
        endTwoOrderedAL.add(new Boolean(false));
        endTwoOrderedAL.add("false");
        returnAL.add(endTwoOrderedAL.clone());
        endTwoOrderedAL.clear();
        return returnAL;
    }

    public ArrayList setEndOneChangeableComboBox()
    {
        ArrayList endOneChangeableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endOneChangeableAL.add(new Byte((byte)1));
        endOneChangeableAL.add("true");
        returnAL.add(endOneChangeableAL.clone());
        endOneChangeableAL.clear();
        endOneChangeableAL.add(new Byte((byte)2));
        endOneChangeableAL.add("false");
        returnAL.add(endOneChangeableAL.clone());
        endOneChangeableAL.clear();
        return returnAL;
    }

    public ArrayList setEndTwoChangeableComboBox()
    {
        ArrayList endTwoChangeableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoChangeableAL.add(new Byte((byte)1));
        endTwoChangeableAL.add("true");
        returnAL.add(endTwoChangeableAL.clone());
        endTwoChangeableAL.clear();
        endTwoChangeableAL.add(new Byte((byte)2));
        endTwoChangeableAL.add("false");
        returnAL.add(endTwoChangeableAL.clone());
        endTwoChangeableAL.clear();
        return returnAL;
    }

    public ArrayList setEndOneReleaseCascadeComboBox()
    {
        ArrayList endTwoNavigableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoNavigableAL.add(new Boolean(true));
        endTwoNavigableAL.add("true");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        endTwoNavigableAL.add(new Boolean(false));
        endTwoNavigableAL.add("false");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        return returnAL;
    }

    public ArrayList setEndTwoReleaseCascadeComboBox()
    {
        ArrayList endTwoNavigableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoNavigableAL.add(new Boolean(true));
        endTwoNavigableAL.add("true");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        endTwoNavigableAL.add(new Boolean(false));
        endTwoNavigableAL.add("false");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        return returnAL;
    }

    public ArrayList setEndOneCloneCascadeComboBox()
    {
        ArrayList endTwoNavigableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoNavigableAL.add(new Boolean(true));
        endTwoNavigableAL.add("true");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        endTwoNavigableAL.add(new Boolean(false));
        endTwoNavigableAL.add("false");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        return returnAL;
    }

    public ArrayList setEndTwoCloneCascadeComboBox()
    {
        ArrayList endTwoNavigableAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        endTwoNavigableAL.add(new Boolean(true));
        endTwoNavigableAL.add("true");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        endTwoNavigableAL.add(new Boolean(false));
        endTwoNavigableAL.add("false");
        returnAL.add(endTwoNavigableAL.clone());
        endTwoNavigableAL.clear();
        return returnAL;
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(dosClassTextField.getTextField()))
        {
            if(e.getClickCount() == 2)
            {
                TypeClassSelection typeClassFrame = new TypeClassSelection(this, "DOS Class Selection", 3);
                typeClassFrame.setVisible(true);
            }
        } else
        if(source.equals(endOneClassTextField.getTextField()))
        {
            if(e.getClickCount() == 2)
            {
                TypeClassSelection typeClassFrame = new TypeClassSelection(this, "End 1 Class Selection", 1);
                typeClassFrame.setVisible(true);
            }
        } else
        if(source.equals(endTwoClassTextField.getTextField()) && e.getClickCount() == 2)
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "End 2 Class Selection", 2);
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

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("create"))
        {
            createAssociation(e);
            tableRefresh();
        } else
        if(command.equals("clear"))
            clearAllField();
        else
        if(command.equals("modify"))
        {
            setAssociation(e);
            tableRefresh();
        } else
        if(command.equals("delete"))
        {
            removeAssociation(e);
            tableRefresh();
            clearAllField();
        }
    }

    public void tableRefresh()
    {
        try
        {
            ArrayList associtionInfoAL = new ArrayList();
            associtionInfoAL = dos.listAssociationOfClass(parent.getClassOuid());
            parent.setAssociationTable(associtionInfoAL);
        }
        catch(IIPRequestException iiprequestexception) { }
    }

    public void clearAllField()
    {
        dosClassOuid = "";
        ouidTextField.setText("");
        nameTextField.setText("");
        codeTextField.setText("");
        descriptionTextField.setText("");
        typeComboBox.setSelectedIndex(-1);
        dosClassTextField.setText("");
        dataSourceIdComboBox.setSelectedIndex(-1);
        endOneClassTextField.setText("");
        endOneNameTextField.setText("");
        endOneMultiplicityFromTextField.setText("");
        endOneMultiplicityToTextField.setText("");
        endOneIsNavigableComboBox.setSelectedIndex(-1);
        endOneIsOrderedComboBox.setSelectedIndex(-1);
        endOneChangeableComboBox.setSelectedIndex(-1);
        endOneReleaseCascadeComboBox.setSelectedIndex(-1);
        endOneCloneCascadeComboBox.setSelectedIndex(-1);
        endTwoClassTextField.setText("");
        endTwoNameTextField.setText("");
        endTwoMultiplicityFromTextField.setText("");
        endTwoMultiplicityToTextField.setText("");
        endTwoIsNavigableComboBox.setSelectedIndex(-1);
        endTwoIsOrderedComboBox.setSelectedIndex(-1);
        endTwoChangeableComboBox.setSelectedIndex(-1);
        endTwoReleaseCascadeComboBox.setSelectedIndex(-1);
        endTwoCloneCascadeComboBox.setSelectedIndex(-1);
        assoOuid = null;
        dosClassOuid = null;
        updateUI();
    }

    private final int titleTextWidth = 140;
    private BorderLayout associationInfoBorderLayout;
    private BoxLayout packageInfoLayout;
    public ClassInformation parent;
    private JScrollPane mainScrPane;
    private JScrollPane endOneClassScrPane;
    private JScrollPane endTwoClassScrPane;
    private JPanel mainPanel;
    private JPanel endOnePanel;
    private JPanel endTwoPanel;
    private JSplitPane mainSplitPane;
    private JTabbedPane endClassTabbedPane;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private DynaTextField descriptionTextField;
    private DynaComboBox typeComboBox;
    private DynaTextField dosClassTextField;
    private DynaComboBox dataSourceIdComboBox;
    private DynaTextField endOneClassTextField;
    private DynaTextField endOneNameTextField;
    private DynaTextField endOneMultiplicityFromTextField;
    private DynaTextField endOneMultiplicityToTextField;
    private DynaComboBox endOneIsNavigableComboBox;
    private DynaComboBox endOneIsOrderedComboBox;
    private DynaComboBox endOneChangeableComboBox;
    private DynaComboBox endOneReleaseCascadeComboBox;
    private DynaComboBox endOneCloneCascadeComboBox;
    private DynaTextField endTwoClassTextField;
    private DynaTextField endTwoNameTextField;
    private DynaTextField endTwoMultiplicityFromTextField;
    private DynaTextField endTwoMultiplicityToTextField;
    private DynaComboBox endTwoIsNavigableComboBox;
    private DynaComboBox endTwoIsOrderedComboBox;
    private DynaComboBox endTwoChangeableComboBox;
    private DynaComboBox endTwoReleaseCascadeComboBox;
    private DynaComboBox endTwoCloneCascadeComboBox;
    private DynaTextField codeTextField;
    private ImageIcon imageEndOneClass;
    private ImageIcon imageEndTwoClass;
    private JToolBar associationToolBar;
    private BoxLayout associationBoxLayout;
    private JButton okButton;
    private JButton modifyButton;
    private JButton finishButton;
    private JButton removeButton;
    private DOS dos;
    private DTM dtm;
    private DynaComboTypeInstance dComboType;
    private DynaComboEndOneNavigableInstance dComboEndOneNavigable;
    private DynaComboEndTwoNavigableInstance dComboEndTwoNavigable;
    private DynaComboEndOneOrderedInstance dComboEndOneOrdered;
    private DynaComboEndTwoOrderedInstance dComboEndTwoOrdered;
    private DynaComboEndOneChangeableInstance dComboEndOneChangeable;
    private DynaComboEndTwoChangeableInstance dComboEndTwoChangeable;
    private DynaComboEndOneReleaseCascadeInstance dComboEndOneReleaseCascade;
    private DynaComboEndTwoReleaseCascadeInstance dComboEndTwoReleaseCascade;
    private DynaComboEndOneCloneCascadeInstance dComboEndOneCloneCascade;
    private DynaComboEndTwoCloneCascadeInstance dComboEndTwoCloneCascade;
    private DynaComboDataSourceIDInstance dComboDataSourceID;
    private DynaComboBoxModel typeComboModel;
    private DynaComboBoxModel endOneNavigableComboModel;
    private DynaComboBoxModel endTwoNavigableComboModel;
    private DynaComboBoxModel endOneOrderedComboModel;
    private DynaComboBoxModel endTwoOrderedComboModel;
    private DynaComboBoxModel endOneChangeableComboModel;
    private DynaComboBoxModel endTwoChangeableComboModel;
    private DynaComboBoxModel endOneReleaseCascadeComboModel;
    private DynaComboBoxModel endTwoReleaseCascadeComboModel;
    private DynaComboBoxModel endOneCloneCascadeComboModel;
    private DynaComboBoxModel endTwoCloneCascadeComboModel;
    private DynaComboBoxModel dataSourceIDComboModel;
    private String assoOuid;
    private String dosClassOuid;
    private String endOneClassOuid;
    private String endTwoClassOuid;
    public static final byte DATATYPE_BOOLEAN = 1;
    public static final byte DATATYPE_BYTE = 2;
    public static final byte DATATYPE_CHAR = 3;

}
