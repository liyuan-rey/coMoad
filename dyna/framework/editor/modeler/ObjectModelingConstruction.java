// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectModelingConstruction.java

package dyna.framework.editor.modeler;

import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.swing.ExtToolBar;
import dyna.framework.Client;
import dyna.framework.client.UIManagement;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

// Referenced classes of package dyna.framework.editor.modeler:
//            TimeTokenUpdate, TimeTokenUpdater, ObjectModelingDefinition, ModelInformation, 
//            PackageInformation, ClassInformation, FieldInformation, ActionInformation, 
//            FieldGroupInformation, ObjectInsertFrame, ModelSelection, CodeManager, 
//            ScriptManager, VersionManager, BatchJobScheduler, AttributeWizard, 
//            ActionAllList, FieldAllList, FieldGroupAllList, ClassAllList, 
//            PackageAllList

public class ObjectModelingConstruction extends JFrame
    implements WindowListener, ActionListener, TimeTokenUpdate
{

    public static void main(String args[])
    {
        try
        {
            LookUtils.setLookAndTheme(new PlasticXPLookAndFeel(), new DynaTheme());
            FontUtils.initFontDefaults(UIManager.getLookAndFeelDefaults(), new Font("dialog", 0, 12), new Font("dialog", 1, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 11), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12));
            splashWindow = new SplashWindow("icons/Splash.jpg", 500);
            splashWindow.setStatusText("Copyright (c) Innovative PLM Solutions LTD., 2003.");
            splashWindow.toFront();
            dfw = new Client();
            dos = (DOS)dfw.getServiceInstance("DF30DOS1");
            dtm = (DTM)dfw.getServiceInstance("DF30DTM1");
            bjs = (BJS)dfw.getServiceInstance("DF30BJS1");
            newUI = new UIManagement();
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        myInstance = new ObjectModelingConstruction();
        myInstance.setVisible(true);
        splashWindow.startClose();
        splashWindow = null;
    }

    public ObjectModelingConstruction()
    {
        modelOuid = null;
        objectSetTree = null;
        modelInformation = null;
        templetDataClass = null;
        projectOid = null;
        workflowProcessInstanceOid = null;
        isTemplateCopy = false;
        packageInfo = null;
        classInfo = null;
        fieldInfo = null;
        actionInfo = null;
        fieldGroupInfo = null;
        attributeWizard = null;
        tokenUpdater = null;
        try
        {
            initialize();
            initializeRootNode();
            addWindowListener(this);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectModelingConstruction(String projectoid)
    {
        modelOuid = null;
        objectSetTree = null;
        modelInformation = null;
        templetDataClass = null;
        projectOid = null;
        workflowProcessInstanceOid = null;
        isTemplateCopy = false;
        packageInfo = null;
        classInfo = null;
        fieldInfo = null;
        actionInfo = null;
        fieldGroupInfo = null;
        attributeWizard = null;
        tokenUpdater = null;
        projectOid = projectoid;
        try
        {
            initialize();
            initializeRootNode();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public ObjectModelingConstruction(String projectoid, String processinstanceoid)
    {
        modelOuid = null;
        objectSetTree = null;
        modelInformation = null;
        templetDataClass = null;
        projectOid = null;
        workflowProcessInstanceOid = null;
        isTemplateCopy = false;
        packageInfo = null;
        classInfo = null;
        fieldInfo = null;
        actionInfo = null;
        fieldGroupInfo = null;
        attributeWizard = null;
        tokenUpdater = null;
        projectOid = projectoid;
        workflowProcessInstanceOid = processinstanceoid;
        try
        {
            initialize();
            initializeRootNode();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public Component getRight()
    {
        return splitPane.getRightComponent();
    }

    public Component getLeft()
    {
        return splitPane.getLeftComponent();
    }

    public void setInformationEnable(int option)
    {
        if(option == 1)
        {
            splitPane.setDividerSize(5);
            splitPane.setRightComponent(modelInformation);
        }
    }

    private void initialize()
        throws Exception
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        menuBarMain = new JMenuBar();
        menuBarMain.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        setJMenuBar(menuBarMain);
        menuMain = new JMenu("File");
        menuMain.setMnemonic(70);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("New", new ImageIcon("icons/New.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("New");
        menuItemMain.setActionCommand("New");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Open", new ImageIcon("icons/Open.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Open");
        menuItemMain.setActionCommand("Open");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Exit", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menuItemMain.setActionCommand("Exit");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("Model");
        menuMain.setMnemonic(77);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("Package", new ImageIcon("icons/Package.gif"));
        menuItemMain.setMnemonic(80);
        menuItemMain.setAccelerator(KeyStroke.getKeyStroke(49, 8));
        menuItemMain.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menuItemMain.setActionCommand("Package");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Class", new ImageIcon("icons/Class.gif"));
        menuItemMain.setMnemonic(67);
        menuItemMain.setAccelerator(KeyStroke.getKeyStroke(50, 8));
        menuItemMain.setActionCommand("Class");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Field Group", new ImageIcon("icons/FieldGroup.gif"));
        menuItemMain.setMnemonic(71);
        menuItemMain.setAccelerator(KeyStroke.getKeyStroke(51, 8));
        menuItemMain.setActionCommand("Field Group");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Field", new ImageIcon("icons/Field.gif"));
        menuItemMain.setMnemonic(70);
        menuItemMain.setAccelerator(KeyStroke.getKeyStroke(52, 8));
        menuItemMain.setActionCommand("Field");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Action", new ImageIcon("icons/Action.gif"));
        menuItemMain.setMnemonic(65);
        menuItemMain.setAccelerator(KeyStroke.getKeyStroke(53, 8));
        menuItemMain.setActionCommand("Action");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("Database");
        menuMain.setMnemonic(68);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("Synchronize", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Synchronize");
        menuItemMain.setActionCommand("Synchronize");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("Code");
        menuMain.setMnemonic(68);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("Code Manager", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Code Manager");
        menuItemMain.setActionCommand("Code Manager");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("Script");
        menuMain.setMnemonic(83);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("Script Manager", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Script Manager");
        menuItemMain.setActionCommand("Script Manager");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("Version");
        menuMain.setMnemonic(86);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("Version Manager", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Version Manager");
        menuItemMain.setActionCommand("Version Manager");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("BJS");
        menuMain.setMnemonic(66);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem("Batch Job Scheduler", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Batch Job Scheduler");
        menuItemMain.setActionCommand("Batch Job Scheduler");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu("CAD Integration");
        menuMain.setMnemonic(67);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        subMenuMain = new JMenu("Solidworks");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Solidworks - Generate Package");
        menuItemMain.setActionCommand("Solidworks - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Attribute Wizard", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Solidworks - Attribute Wizard");
        menuItemMain.setActionCommand("Solidworks - Attribute Wizard");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        subMenuMain = new JMenu("CATIA V4");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("CATIA V4 - Generate Package");
        menuItemMain.setActionCommand("CATIA V4 - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        subMenuMain = new JMenu("CATIA V5");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("CATIA V5 - Generate Package");
        menuItemMain.setActionCommand("CATIA V5 - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Attribute Wizard", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("CATIA V5 - Attribute Wizard");
        menuItemMain.setActionCommand("CATIA V5 - Attribute Wizard");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        subMenuMain = new JMenu("Unigraphics");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Unigraphics - Generate Package");
        menuItemMain.setActionCommand("Unigraphics - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Attribute Wizard", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("Unigraphics - Attribute Wizard");
        menuItemMain.setActionCommand("Unigraphics - Attribute Wizard");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        subMenuMain = new JMenu("ProEngineer");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("ProEngineer - Generate Package");
        menuItemMain.setActionCommand("ProEngineer - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Attribute Wizard", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("ProEngineer - Attribute Wizard");
        menuItemMain.setActionCommand("ProEngineer - Attribute Wizard");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        subMenuMain = new JMenu("I-DEAS");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("I-DEAS - Generate Package");
        menuItemMain.setActionCommand("I-DEAS - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        menuItemMain = new JMenuItem("Attribute Wizard", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("I-DEAS - Attribute Wizard");
        menuItemMain.setActionCommand("I-DEAS - Attribute Wizard");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        subMenuMain = new JMenu("AutoCAD");
        menuMain.add(subMenuMain);
        menuItemMain = new JMenuItem("Generate package", new ImageIcon("icons/middle.gif"));
        menuItemMain.getAccessibleContext().setAccessibleDescription("AutoCAD - Generate Package");
        menuItemMain.setActionCommand("AutoCAD - Generate Package");
        menuItemMain.addActionListener(this);
        subMenuMain.add(menuItemMain);
        waitCursor = new Cursor(3);
        reportButton = new JButton();
        templetCopyButton = new JButton();
        templetSearchButton = new JButton();
        subWbsButton = new JButton();
        deleteButton = new JButton();
        exitButton = new JButton();
        workflowMakeButton = new JButton();
        informationButton = new JButton();
        modifyButton = new JButton();
        addButton = new JButton();
        projectNameCodePanel = new JPanel();
        projectNameCodeBoxLayout = new BoxLayout(projectNameCodePanel, 1);
        mainButtonToolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
        mainButtonToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        splitPane = new JSplitPane(1);
        isTemplateCopy = false;
        getContentPane().setLayout(new BorderLayout());
        setTitle("Object Modeler - DynaMOAD");
        setLocation(112, 84);
        setSize(800, 600);
        informationButton.setToolTipText("Information");
        informationButton.setMargin(new Insets(0, 0, 0, 0));
        informationButton.setIcon(new ImageIcon(getClass().getResource("/icons/Information.gif")));
        informationButton.setActionCommand("Information");
        informationButton.addActionListener(this);
        reportButton.setMargin(new Insets(0, 0, 0, 0));
        reportButton.setIcon(new ImageIcon(getClass().getResource("/icons/PrintPaper32.gif")));
        reportButton.setActionCommand("Report");
        reportButton.addActionListener(this);
        templetCopyButton.setMargin(new Insets(0, 0, 0, 0));
        templetCopyButton.setIcon(new ImageIcon(getClass().getResource("/icons/Templet32.gif")));
        templetCopyButton.setActionCommand("TempletCopy");
        templetCopyButton.addActionListener(this);
        templetSearchButton.setMargin(new Insets(0, 0, 0, 0));
        templetSearchButton.setIcon(new ImageIcon(getClass().getResource("/icons/SearchTemplet32.gif")));
        templetSearchButton.setActionCommand("TempletSearch");
        templetSearchButton.addActionListener(this);
        subWbsButton.setMargin(new Insets(0, 0, 0, 0));
        subWbsButton.setIcon(new ImageIcon(getClass().getResource("/icons/SubWBS32.gif")));
        subWbsButton.setActionCommand("SubWBS");
        subWbsButton.addActionListener(this);
        workflowMakeButton.setMargin(new Insets(0, 0, 0, 0));
        workflowMakeButton.setIcon(new ImageIcon(getClass().getResource("/icons/WorkFlow32.gif")));
        workflowMakeButton.setActionCommand("MakeWorkflow");
        workflowMakeButton.addActionListener(this);
        deleteButton.setToolTipText("Delete");
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteButton.setActionCommand("DeleteWBS");
        deleteButton.addActionListener(this);
        exitButton.setToolTipText("Exit");
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        modifyButton.setToolTipText("Modify");
        modifyButton.setMargin(new Insets(0, 0, 0, 0));
        modifyButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        modifyButton.setActionCommand("Modify");
        modifyButton.addActionListener(this);
        addButton.setToolTipText("Add");
        addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.setIcon(new ImageIcon(getClass().getResource("/icons/Addition32.gif")));
        addButton.setActionCommand("Add");
        addButton.addActionListener(this);
        projectNameCodePanel.setLayout(projectNameCodeBoxLayout);
        mainButtonToolBar.add(informationButton);
        mainButtonToolBar.add(modifyButton);
        mainButtonToolBar.add(deleteButton);
        getContentPane().add(mainButtonToolBar, "North");
        setSplitPane();
        setButtonsStatus(false);
        tokenUpdater = new TimeTokenUpdater(dos);
        tokenUpdater.setTimeTokenContainer(this);
        Thread aThread = new Thread(tokenUpdater);
        aThread.start();
    }

    private void setSplitPane()
    {
        objectSetTree = new ObjectModelingDefinition(this);
        modelInformation = new ModelInformation(this);
        splitPane.setDividerSize(5);
        splitPane.setLeftComponent(objectSetTree);
        splitPane.setRightComponent(modelInformation);
        splitPane.setDividerLocation(300);
        getContentPane().add(splitPane, "Center");
    }

    private void initializeRootNode()
    {
        try
        {
            DOSChangeable modelInfoDC = new DOSChangeable();
            modelOuid = dos.getWorkingModel();
            if(dos.getWorkingModel() != null && !dos.getWorkingModel().equals(""))
            {
                modelInfoDC = dos.getModel(modelOuid);
                TreeNodeObject processdata = new TreeNodeObject((String)modelInfoDC.get("ouid"), (String)modelInfoDC.get("name"), "Model");
                makeObjectSetTree(processdata);
                displayInformation(modelOuid, "", 0, 260);
            }
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    private void reportButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void projectInformationButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void templetCopyButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void templetSearchButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void subWbsButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void workflowMakeButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void deleteButton_actionPerformed(ActionEvent e)
    {
        objectSetTree.deleteAction(e);
    }

    private void exitButton_actionPerformed(ActionEvent e)
    {
        closeEvent();
    }

    private void modifyButton_actionPerformed(ActionEvent e)
    {
        if(splitPane.getRightComponent() instanceof ModelInformation)
            ((ModelInformation)splitPane.getRightComponent()).setInformation();
        else
        if(splitPane.getRightComponent() instanceof PackageInformation)
            ((PackageInformation)splitPane.getRightComponent()).setInformation();
        else
        if(splitPane.getRightComponent() instanceof ClassInformation)
        {
            ((ClassInformation)splitPane.getRightComponent()).setInformation();
            try
            {
                DOSChangeable classInfoDC = new DOSChangeable();
                String ouid = classInfo.getClassOuid();
                classInfoDC = dos.getClass(ouid);
                ArrayList associtionInfoAL = new ArrayList();
                associtionInfoAL = dos.listAssociationOfClass(ouid);
                classInfo.setClassInfoField(classInfoDC);
                classInfo.setOuidIntoEvent((String)classInfoDC.get("ouid"));
                classInfo.setAssociationTable(associtionInfoAL);
                classInfo.setOuidIntoNumberingRule((String)classInfoDC.get("ouid"));
            }
            catch(IIPRequestException e1)
            {
                e1.printStackTrace();
            }
        } else
        if(splitPane.getRightComponent() instanceof FieldInformation)
            ((FieldInformation)splitPane.getRightComponent()).setInformation();
        else
        if(splitPane.getRightComponent() instanceof ActionInformation)
            ((ActionInformation)splitPane.getRightComponent()).setInformation();
        else
        if(splitPane.getRightComponent() instanceof FieldGroupInformation)
            ((FieldGroupInformation)splitPane.getRightComponent()).setInformation();
        objectSetTree.treeRefresh();
    }

    private void newButton_actionPerformed(ActionEvent e)
    {
        String modelOuid = "";
        ModelInformation newModelInfo = new ModelInformation(this);
        ObjectInsertFrame insertFrame = new ObjectInsertFrame(this);
        insertFrame.show();
    }

    private void openButton_actionPerformed(ActionEvent e)
    {
        ModelSelection modelOpenPanel = new ModelSelection(this);
        modelOpenPanel.show();
    }

    private void addButton_actionPerformed(ActionEvent actionevent)
    {
    }

    private void synchronizeButton_actionPerformed(ActionEvent e)
    {
        try
        {
            if(modelOuid == null)
                modelOuid = dos.getWorkingModel();
            if(ddg == null)
                ddg = (DDG)dfw.getServiceInstance("DF30DDG1");
            ddg.generateDatabase(modelOuid);
        }
        catch(ServiceNotFoundException ee)
        {
            System.err.println(ee);
        }
        catch(IIPRequestException ee)
        {
            System.err.println(ee);
        }
    }

    private void codeManagerMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            modelOuid = dos.getWorkingModel();
            if(modelOuid != null && !Utils.isNullString(modelOuid))
            {
                CodeManager codeManager = new CodeManager(this, dos);
                codeManager.pack();
                codeManager.setVisible(true);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void scriptManagerMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            modelOuid = dos.getWorkingModel();
            if(modelOuid != null && !Utils.isNullString(modelOuid))
            {
                ScriptManager scriptManager = new ScriptManager(this);
                scriptManager.pack();
                scriptManager.setVisible(true);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void versionManagerMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            modelOuid = dos.getWorkingModel();
            if(modelOuid != null && !Utils.isNullString(modelOuid))
            {
                VersionManager versionManager = new VersionManager(this);
                versionManager.pack();
                versionManager.setVisible(true);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private void schedulerMenu_actionPerformed(ActionEvent e)
    {
        BatchJobScheduler scheduler = new BatchJobScheduler(this);
        scheduler.pack();
        scheduler.setVisible(true);
    }

    private void swGeneratePackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForSolidworks();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void swAttributeWizardMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            if(ddg == null)
                ddg = (DDG)dfw.getServiceInstance("DF30DDG1");
        }
        catch(ServiceNotFoundException ee)
        {
            System.err.println(ee);
        }
        attributeWizard = new AttributeWizard(dos, ddg, 0);
        attributeWizard.pack();
        attributeWizard.setVisible(true);
    }

    private void catiaV4PackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForCatiaV4();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void autoCADPackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForAutoCAD();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void catiaV5GeneratePackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForCatiaV5();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void catiaV5AttributeWizardMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            if(ddg == null)
                ddg = (DDG)dfw.getServiceInstance("DF30DDG1");
        }
        catch(ServiceNotFoundException ee)
        {
            System.err.println(ee);
        }
        attributeWizard = new AttributeWizard(dos, ddg, 0);
        attributeWizard.pack();
        attributeWizard.setVisible(true);
    }

    private void UGGeneratePackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForUG();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void UGAttributeWizardMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            if(ddg == null)
                ddg = (DDG)dfw.getServiceInstance("DF30DDG1");
        }
        catch(ServiceNotFoundException ee)
        {
            System.err.println(ee);
        }
        attributeWizard = new AttributeWizard(dos, ddg, 0);
        attributeWizard.pack();
        attributeWizard.setVisible(true);
    }

    private void ProEGeneratePackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForProE();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void ProEAttributeWizardMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            if(ddg == null)
                ddg = (DDG)dfw.getServiceInstance("DF30DDG1");
        }
        catch(ServiceNotFoundException ee)
        {
            System.err.println(ee);
        }
        attributeWizard = new AttributeWizard(dos, ddg, 0);
        attributeWizard.pack();
        attributeWizard.setVisible(true);
    }

    private void IDEASGeneratePackageMenu_actionPerformed(ActionEvent e)
    {
        objectSetTree.createPackageForIDEAS();
        objectSetTree.treeRefresh();
        synchronizeButton_actionPerformed(e);
    }

    private void IDEASAttributeWizardMenu_actionPerformed(ActionEvent e)
    {
        try
        {
            if(ddg == null)
                ddg = (DDG)dfw.getServiceInstance("DF30DDG1");
        }
        catch(ServiceNotFoundException ee)
        {
            System.err.println(ee);
        }
        attributeWizard = new AttributeWizard(dos, ddg, 0);
        attributeWizard.pack();
        attributeWizard.setVisible(true);
    }

    private void closeEvent()
    {
        removeAllListener();
        waitCursor = null;
        reportButton = null;
        templetCopyButton = null;
        templetSearchButton = null;
        subWbsButton = null;
        deleteButton = null;
        exitButton = null;
        workflowMakeButton = null;
        informationButton = null;
        mainButtonToolBar = null;
        mainButtonBoxLayout = null;
        splitPane = null;
        objectSetTree = null;
        modelInformation = null;
        templetDataClass = null;
        projectOid = null;
        workflowProcessInstanceOid = null;
        dispose();
        System.exit(0);
    }

    public Vector getProjectInfo()
    {
        return null;
    }

    private void removeAllListener()
    {
        informationButton.removeActionListener(this);
        reportButton.removeActionListener(this);
        templetCopyButton.removeActionListener(this);
        templetSearchButton.removeActionListener(this);
        subWbsButton.removeActionListener(this);
        workflowMakeButton.removeActionListener(this);
        deleteButton.removeActionListener(this);
        exitButton.removeActionListener(this);
    }

    public void displayInformation(String oid, String desc, int level, int newLocation)
    {
        String classOuid = "";
        if(level == 0)
            try
            {
                DOSChangeable modelInfoDC = new DOSChangeable();
                modelInfoDC = dos.getModel(oid);
                modelInformation = new ModelInformation(this);
                modelInformation.setPackageInfoField(modelInfoDC);
                modelInformation.setOuidIntoEvent((String)modelInfoDC.get("ouid"));
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(modelInformation);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 1)
            try
            {
                DOSChangeable packageInfoDC = new DOSChangeable();
                packageInfoDC = dos.getPackage(oid);
                packageInfo = new PackageInformation(this);
                packageInfo.setPackageInfoField(packageInfoDC);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(packageInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 2)
            try
            {
                DOSChangeable classInfoDC = new DOSChangeable();
                classInfoDC = dos.getClass(oid);
                ArrayList associtionInfoAL = new ArrayList();
                associtionInfoAL = dos.listAssociationOfClass(oid);
                if(classInfo == null)
                    classInfo = new ClassInformation(this, (String)classInfoDC.get("ouid@package"));
                classInfo.setClassInfoField(classInfoDC);
                classInfo.setOuidIntoEvent((String)classInfoDC.get("ouid"));
                classInfo.setAssociationTable(associtionInfoAL);
                classInfo.setOuidIntoNumberingRule((String)classInfoDC.get("ouid"));
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(classInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 4 && desc.startsWith("Field"))
            try
            {
                DOSChangeable fieldInfoDC = new DOSChangeable();
                fieldInfoDC = dos.getField(oid);
                fieldInfo = new FieldInformation(this, (String)fieldInfoDC.get("ouid@class"));
                classOuid = (String)fieldInfoDC.get("ouid@class");
                fieldInfo.setFieldInfoField(fieldInfoDC);
                fieldInfo.setFieldEnabled(true);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(fieldInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 4 && desc.startsWith("Inherited Field"))
            try
            {
                DOSChangeable fieldInfoDC = new DOSChangeable();
                fieldInfoDC = dos.getField(oid);
                fieldInfo = new FieldInformation(this, classOuid);
                fieldInfo.setFieldInfoField(fieldInfoDC);
                fieldInfo.setFieldEnabled(false);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(fieldInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 4 && desc.startsWith("Action"))
            try
            {
                DOSChangeable actionInfoDC = new DOSChangeable();
                actionInfoDC = dos.getAction(oid);
                actionInfo = new ActionInformation(this, (String)actionInfoDC.get("ouid@class"));
                actionInfo.setActionInfoField(actionInfoDC);
                actionInfo.setFieldEnabled(true);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(actionInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 4 && desc.startsWith("Inherited Action"))
            try
            {
                DOSChangeable actionInfoDC = new DOSChangeable();
                actionInfoDC = dos.getAction(oid);
                actionInfo = new ActionInformation(this, classOuid);
                actionInfo.setActionInfoField(actionInfoDC);
                actionInfo.setFieldEnabled(false);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(actionInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 4 && desc.startsWith("FGroup"))
            try
            {
                DOSChangeable fieldGroupInfoDC = new DOSChangeable();
                fieldGroupInfoDC = dos.getFieldGroup(oid);
                ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup(oid);
                if(inheritedFields != null)
                    fieldGroupInfoDC.put("array$ouid@inherited.field", inheritedFields);
                fieldGroupInfo = new FieldGroupInformation(this, (String)fieldGroupInfoDC.get("ouid@class"));
                fieldGroupInfo.setFieldGroupInfoField(fieldGroupInfoDC);
                fieldGroupInfo.setFieldEnabled(true);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(fieldGroupInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
        else
        if(level == 4 && desc.startsWith("Inherited FGroup"))
            try
            {
                DOSChangeable fieldGroupInfoDC = new DOSChangeable();
                fieldGroupInfoDC = dos.getFieldGroup(oid);
                ArrayList fields = (ArrayList)fieldGroupInfoDC.get("array$ouid@field");
                ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup(oid);
                if(inheritedFields != null)
                {
                    if(fields == null)
                        fields = new ArrayList();
                    ArrayList tmpFieldList = new ArrayList();
                    DOSChangeable tmpData = null;
                    for(int j = 0; j < inheritedFields.size(); j++)
                    {
                        tmpData = (DOSChangeable)inheritedFields.get(j);
                        if(!fields.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                            tmpFieldList.add((String)tmpData.get("ouid"));
                    }

                    if(tmpFieldList.size() > 0)
                        fields.addAll(0, tmpFieldList);
                }
                fieldGroupInfoDC.put("array$ouid@field", fields);
                fieldGroupInfo = new FieldGroupInformation(this, (String)fieldGroupInfoDC.get("ouid@class"));
                fieldGroupInfo.setFieldGroupInfoField(fieldGroupInfoDC);
                fieldGroupInfo.setFieldEnabled(false);
                if(newLocation > 0)
                    splitPane.setDividerLocation(newLocation);
                else
                    splitPane.setDividerLocation(300);
                splitPane.setRightComponent(fieldGroupInfo);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
    }

    public void insertTreeNode(TreeNodeObject treenodeobject)
    {
    }

    public void updateTreeNode(TreeNodeObject treenodeobject)
    {
    }

    public void setTopeNode(TreeNodeObject treenodeobject)
    {
    }

    public void insertNewNode(DefaultMutableTreeNode defaultmutabletreenode)
    {
    }

    public void setFullTreeExpand(DefaultMutableTreeNode defaultmutabletreenode)
    {
    }

    public void setNodeInformation(Vector vector, boolean flag)
    {
    }

    public void setButtonsStatus(boolean bl)
    {
        templetSearchButton.setEnabled(bl);
        workflowMakeButton.setEnabled(bl);
    }

    public void makeObjectSetTree(TreeNodeObject rootnodedata)
    {
        objectSetTree.setTopNode(rootnodedata);
        DefaultTreeModel treemodel = (DefaultTreeModel)objectSetTree.getTree().getModel();
        objectSetTree.setFullTreeExpand((DefaultMutableTreeNode)treemodel.getRoot());
    }

    public void makeObjectSetTree(TreeNodeObject rootnodedata, int i)
    {
        try
        {
            objectSetTree.setTopNode(rootnodedata);
            DefaultTreeModel treemodel = (DefaultTreeModel)objectSetTree.getTree().getModel();
            dos.setWorkingModel(rootnodedata.getOuid());
            objectSetTree.setFullTreeExpand((DefaultMutableTreeNode)treemodel.getRoot());
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("Information"))
            projectInformationButton_actionPerformed(e);
        else
        if(command.equals("Report"))
            reportButton_actionPerformed(e);
        else
        if(command.equals("TempletCopy"))
            templetCopyButton_actionPerformed(e);
        else
        if(command.equals("TempletSearch"))
            templetSearchButton_actionPerformed(e);
        else
        if(command.equals("SubWBS"))
            subWbsButton_actionPerformed(e);
        else
        if(command.equals("MakeWorkflow"))
            workflowMakeButton_actionPerformed(e);
        else
        if(command.equals("DeleteWBS"))
            deleteButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
        else
        if(command.equals("Modify"))
            modifyButton_actionPerformed(e);
        else
        if(command.equals("New"))
            newButton_actionPerformed(e);
        else
        if(command.equals("Open"))
            openButton_actionPerformed(e);
        else
        if(command.equals("Add"))
            addButton_actionPerformed(e);
        else
        if(command.equals("Synchronize"))
            synchronizeButton_actionPerformed(e);
        else
        if(command.equals("Code Manager"))
            codeManagerMenu_actionPerformed(e);
        else
        if(command.equals("Script Manager"))
            scriptManagerMenu_actionPerformed(e);
        else
        if(command.equals("Version Manager"))
            versionManagerMenu_actionPerformed(e);
        else
        if(command.equals("Batch Job Scheduler"))
            schedulerMenu_actionPerformed(e);
        else
        if(command.equals("Solidworks - Generate Package"))
            swGeneratePackageMenu_actionPerformed(e);
        else
        if(command.equals("Solidworks - Attribute Wizard"))
            swAttributeWizardMenu_actionPerformed(e);
        else
        if(command.equals("CATIA V4 - Generate Package"))
            catiaV4PackageMenu_actionPerformed(e);
        else
        if(command.equals("AutoCAD - Generate Package"))
            autoCADPackageMenu_actionPerformed(e);
        else
        if(command.equals("CATIA V5 - Generate Package"))
            catiaV5GeneratePackageMenu_actionPerformed(e);
        else
        if(command.equals("CATIA V5 - Attribute Wizard"))
            catiaV5AttributeWizardMenu_actionPerformed(e);
        else
        if(command.equals("Unigraphics - Generate Package"))
            UGGeneratePackageMenu_actionPerformed(e);
        else
        if(command.equals("Unigraphics - Attribute Wizard"))
            UGAttributeWizardMenu_actionPerformed(e);
        else
        if(command.equals("ProEngineer - Generate Package"))
            ProEGeneratePackageMenu_actionPerformed(e);
        else
        if(command.equals("ProEngineer - Attribute Wizard"))
            ProEAttributeWizardMenu_actionPerformed(e);
        else
        if(command.equals("I-DEAS - Generate Package"))
            IDEASGeneratePackageMenu_actionPerformed(e);
        else
        if(command.equals("I-DEAS - Attribute Wizard"))
            IDEASAttributeWizardMenu_actionPerformed(e);
        else
        if(command.equals("Package"))
            packageMenu_actionPerformed(e);
        else
        if(command.equals("Class"))
            classMenu_actionPerformed(e);
        else
        if(command.equals("Field Group"))
            fieldGroupMenu_actionPerformed(e);
        else
        if(command.equals("Field"))
            fieldMenu_actionPerformed(e);
        else
        if(command.equals("Action"))
            actionMenu_actionPerformed(e);
    }

    private void actionMenu_actionPerformed(ActionEvent e)
    {
        ActionAllList actionAllList = new ActionAllList(this);
        actionAllList.show();
    }

    private void fieldMenu_actionPerformed(ActionEvent e)
    {
        FieldAllList fieldAllList = new FieldAllList(this);
        fieldAllList.show();
    }

    private void fieldGroupMenu_actionPerformed(ActionEvent e)
    {
        FieldGroupAllList fieldGroupAllList = new FieldGroupAllList(this);
        fieldGroupAllList.show();
    }

    private void classMenu_actionPerformed(ActionEvent e)
    {
        ClassAllList classAllList = new ClassAllList(this);
        classAllList.show();
    }

    private void packageMenu_actionPerformed(ActionEvent e)
    {
        PackageAllList packageAllList = new PackageAllList(this);
        packageAllList.show();
    }

    public void windowClosing(WindowEvent e)
    {
        System.exit(0);
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public static boolean validateToken()
    {
label0:
        {
            String oldTimeToken;
            String timeToken;
            IIPRequestException e;
            synchronized(dyna.framework.editor.modeler.ObjectModelingConstruction.class)
            {
                if(!invalid)
                    break label0;
            }
            return false;
        }
        oldTimeToken = new String(timeToken);
        timeToken = null;
        try
        {
            timeToken = dos.getTimeToken();
            invalid = oldTimeToken == null || timeToken == null || !oldTimeToken.equals(timeToken);
        }
        // Misplaced declaration of an exception variable
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        class1;
        JVM INSTR monitorexit ;
        return !invalid;
    }

    public static void resetToken()
    {
        synchronized(dyna.framework.editor.modeler.ObjectModelingConstruction.class)
        {
            try
            {
                timeToken = dos.getTimeToken();
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void updateToken(String token)
    {
        synchronized(dyna.framework.editor.modeler.ObjectModelingConstruction.class)
        {
            if(timeToken == null)
                timeToken = token;
            else
            if(!timeToken.equals(token))
                invalid = true;
            else
                invalid = false;
        }
    }

    public void reloadModel(String ouid, String name)
    {
        if(Utils.isNullString(ouid))
        {
            return;
        } else
        {
            modelOuid = ouid;
            TreeNodeObject rootNode = new TreeNodeObject(ouid, name, "Model");
            makeObjectSetTree(rootNode, 1);
            objectSetTree.updateUI();
            return;
        }
    }

    public void reloadModel()
    {
        if(Utils.isNullString(modelOuid))
            return;
        DOSChangeable tempModel = null;
        try
        {
            tempModel = dos.getModel(modelOuid);
            if(tempModel == null)
                return;
            reloadModel(modelOuid, (String)tempModel.get("name"));
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    public static ObjectModelingConstruction getInstance()
    {
        return myInstance;
    }

    final int DIVIDERSIZE = 5;
    final int DIVIDERLOCATION = 300;
    final int titleTextWidth = 80;
    public static Client dfw = null;
    public static DOS dos = null;
    public static DTM dtm = null;
    public static DDG ddg = null;
    public static BJS bjs = null;
    public static String xid = null;
    public static UIManagement newUI;
    public String modelOuid;
    private static boolean invalid = false;
    private static String timeToken = null;
    private Cursor waitCursor;
    private JButton reportButton;
    private JButton templetCopyButton;
    private JButton templetSearchButton;
    private JButton subWbsButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JButton workflowMakeButton;
    private JButton informationButton;
    private JButton modifyButton;
    private JButton addButton;
    private JPanel projectNameCodePanel;
    private BoxLayout projectNameCodeBoxLayout;
    private JToolBar mainButtonToolBar;
    private BoxLayout mainButtonBoxLayout;
    public JSplitPane splitPane;
    private ObjectModelingDefinition objectSetTree;
    private ModelInformation modelInformation;
    private TreeNodeObject templetDataClass;
    private String projectOid;
    private String workflowProcessInstanceOid;
    private boolean isTemplateCopy;
    JMenuBar menuBarMain;
    JMenu menuMain;
    JMenu subMenuMain;
    JMenuItem menuItemMain;
    PackageInformation packageInfo;
    ClassInformation classInfo;
    FieldInformation fieldInfo;
    ActionInformation actionInfo;
    FieldGroupInformation fieldGroupInfo;
    AttributeWizard attributeWizard;
    private TimeTokenUpdater tokenUpdater;
    private static SplashWindow splashWindow = null;
    private static ObjectModelingConstruction myInstance = null;

}
