// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkflowModeler.java

package dyna.framework.editor.workflow;

import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.plaf.plastic.theme.ExperienceBlue;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            WorkflowEditorPanel, WorkflowTreeCellRenderer, WorkflowTreeNode, ModelSelectDialog, 
//            ProcessPanel, ActivityPanel, ProcessAddDialog, ClassMapDialog, 
//            WorkflowTreeModel, ActivityAddDialog

public class WorkflowModeler extends JFrame
    implements ActionListener, DialogReturnCallback, TreeSelectionListener, MouseListener
{

    public WorkflowModeler()
    {
        modelOuid = null;
        allTreeNode = null;
        rootNode = null;
        i = 0;
        processPanel = null;
        activityPanel = null;
        try
        {
            LookUtils.setLookAndTheme(new PlasticXPLookAndFeel(), new ExperienceBlue());
            FontUtils.initFontDefaults(UIManager.getLookAndFeelDefaults(), new Font("dialog", 0, 12), new Font("dialog", 1, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 11), new Font("dialog", 0, 12), new Font("dialog", 0, 12), new Font("dialog", 0, 12));
            dfw = new Client();
            dos = (DOS)dfw.getServiceInstance("DF30DOS1");
            wfm = (WFM)dfw.getServiceInstance("DF30WFM1");
            olm = (OLM)dfw.getServiceInstance("DF30OLM1");
            nds = (NDS)dfw.getServiceInstance("DF30NDS1");
            dss = (DSS)dfw.getServiceInstance("DF30DSS1");
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
        }
        catch(UnsupportedLookAndFeelException e)
        {
            System.err.println(e);
        }
        setSize(800, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        screenSize = null;
        allTreeNode = new HashMap();
        initComponents();
    }

    private void initComponents()
    {
        toolBar = new JToolBar();
        openButton = new JButton();
        saveButton = new JButton();
        splitPane = new JSplitPane();
        processScrollPane = UIFactory.createStrippedScrollPane(null);
        processTree = new JTree();
        workflowPanel = new WorkflowEditorPanel(dfw, true);
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        openMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        editMenu = new JMenu();
        cutMenuItem = new JMenuItem();
        copyMenuItem = new JMenuItem();
        pasteMenuItem = new JMenuItem();
        deleteMenuItem = new JMenuItem();
        tabPanel = new JPanel();
        tabTitlePanel = new JPanel();
        tabTitleButtons = new JToggleButton[2];
        modelPopup = new JPopupMenu();
        addProcessMenuItem = new JMenuItem();
        processPopup = new JPopupMenu();
        addActivityMenuItem = new JMenuItem();
        deleteProcessMenuItem = new JMenuItem();
        copyProcessMenuItem = new JMenuItem();
        cutProcessMenuItem = new JMenuItem();
        pasteProcessMenuItem = new JMenuItem();
        classMapMenuItem = new JMenuItem();
        activityPopup = new JPopupMenu();
        addWorkMenuItem = new JMenuItem();
        deleteActivityMenuItem = new JMenuItem();
        copyActivityMenuItem = new JMenuItem();
        cutActivityMenuItem = new JMenuItem();
        pasteActivityMenuItem = new JMenuItem();
        workPopup = new JPopupMenu();
        deleteWorkMenuItem = new JMenuItem();
        copyWorkMenuItem = new JMenuItem();
        cutWorkMenuItem = new JMenuItem();
        pasteWorkMenuItem = new JMenuItem();
        setDefaultCloseOperation(2);
        setTitle("Workflow Modeler - DynaMOAD");
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

        });
        toolBar.setFont(getFont());
        toolBar.putClientProperty("jgoodies.headerStyle", HeaderStyle.BOTH);
        openButton.setFont(getFont());
        openButton.setIcon(new ImageIcon("icons/Open.gif"));
        openButton.setText("Open Model");
        openButton.setActionCommand("open");
        openButton.addActionListener(this);
        toolBar.add(openButton);
        saveButton.setFont(getFont());
        saveButton.setIcon(new ImageIcon("icons/Save.gif"));
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        toolBar.add(saveButton);
        getContentPane().add(toolBar, "North");
        splitPane.setDividerLocation(200);
        splitPane.setOneTouchExpandable(true);
        processTree.setFont(getFont());
        processTree.setCellRenderer(new WorkflowTreeCellRenderer());
        processTree.addTreeSelectionListener(this);
        processTree.addMouseListener(this);
        splitPane.setLeftComponent(processScrollPane);
        tabPanel.setLayout(new BorderLayout(0, 0));
        tabTitlePanel.setLayout(new FlowLayout(0, 0, 0));
        Insets tabInsets = new Insets(1, 5, 1, 5);
        for(i = 0; i < 2; i++)
        {
            tabTitleButtons[i] = new JToggleButton();
            tabTitleButtons[i].setFont(getFont());
            tabTitleButtons[i].setHorizontalAlignment(2);
            tabTitleButtons[i].setMargin(tabInsets);
            tabTitleButtons[i].setSelected(true);
            tabTitleButtons[i].addActionListener(this);
            tabTitlePanel.add(tabTitleButtons[i]);
        }

        tabTitleButtons[0].setText("Information");
        tabTitleButtons[0].setIcon(new ImageIcon("icons/Information.gif"));
        tabTitleButtons[0].setActionCommand("information");
        tabTitleButtons[1].setText("Workflow");
        tabTitleButtons[1].setActionCommand("workflow");
        tabPanel.add(tabTitlePanel, "North");
        splitPane.setRightComponent(tabPanel);
        getContentPane().add(splitPane, "Center");
        fileMenu.setText("File");
        fileMenu.setMnemonic('F');
        fileMenu.setFont(getFont());
        openMenuItem.setFont(getFont());
        openMenuItem.setText("Open Model...");
        openMenuItem.setActionCommand("open");
        openMenuItem.setIcon(new ImageIcon("icons/Open.gif"));
        openMenuItem.addActionListener(this);
        fileMenu.add(openMenuItem);
        saveMenuItem.setFont(getFont());
        saveMenuItem.setText("Save");
        saveMenuItem.setActionCommand("save");
        saveMenuItem.setIcon(new ImageIcon("icons/Save.gif"));
        saveMenuItem.addActionListener(this);
        fileMenu.add(saveMenuItem);
        exitMenuItem.setFont(getFont());
        exitMenuItem.setText("Exit");
        exitMenuItem.setActionCommand("exit");
        exitMenuItem.setIcon(new ImageIcon("icons/Blank.gif"));
        exitMenuItem.addActionListener(this);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        editMenu.setText("Edit");
        editMenu.setMnemonic('E');
        editMenu.setFont(getFont());
        editMenu.setEnabled(false);
        cutMenuItem.setText("Cut");
        cutMenuItem.setIcon(new ImageIcon("icons/Cut.gif"));
        editMenu.add(cutMenuItem);
        copyMenuItem.setText("Copy");
        copyMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        editMenu.add(copyMenuItem);
        pasteMenuItem.setText("Paste");
        pasteMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        editMenu.add(pasteMenuItem);
        deleteMenuItem.setText("Delete");
        deleteMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        editMenu.add(deleteMenuItem);
        menuBar.add(editMenu);
        menuBar.putClientProperty("jgoodies.headerStyle", HeaderStyle.BOTH);
        setJMenuBar(menuBar);
        addProcessMenuItem.setText("Add Process...");
        addProcessMenuItem.setFont(getFont());
        addProcessMenuItem.setActionCommand("add.process");
        addProcessMenuItem.setIcon(new ImageIcon("icons/Process.gif"));
        addProcessMenuItem.addActionListener(this);
        modelPopup.add(addProcessMenuItem);
        cutProcessMenuItem.setText("Cut");
        cutProcessMenuItem.setFont(getFont());
        cutProcessMenuItem.setActionCommand("cut.process");
        cutProcessMenuItem.setEnabled(false);
        cutProcessMenuItem.setIcon(new ImageIcon("icons/Cut.gif"));
        cutProcessMenuItem.addActionListener(this);
        processPopup.add(cutProcessMenuItem);
        copyProcessMenuItem.setText("Copy");
        copyProcessMenuItem.setFont(getFont());
        copyProcessMenuItem.setActionCommand("copy.process");
        copyProcessMenuItem.setEnabled(false);
        copyProcessMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyProcessMenuItem.addActionListener(this);
        processPopup.add(copyProcessMenuItem);
        pasteProcessMenuItem.setText("Paste");
        pasteProcessMenuItem.setFont(getFont());
        pasteProcessMenuItem.setActionCommand("paste.process");
        pasteProcessMenuItem.setEnabled(false);
        pasteProcessMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteProcessMenuItem.addActionListener(this);
        processPopup.add(pasteProcessMenuItem);
        processPopup.add(new JSeparator());
        addActivityMenuItem.setText("Add Activity...");
        addActivityMenuItem.setFont(getFont());
        addActivityMenuItem.setActionCommand("add.activity");
        addActivityMenuItem.setIcon(new ImageIcon("icons/Activity.gif"));
        addActivityMenuItem.addActionListener(this);
        processPopup.add(addActivityMenuItem);
        deleteProcessMenuItem.setText("Delete");
        deleteProcessMenuItem.setFont(getFont());
        deleteProcessMenuItem.setActionCommand("delete.process");
        deleteProcessMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        deleteProcessMenuItem.addActionListener(this);
        processPopup.add(deleteProcessMenuItem);
        processPopup.add(new JSeparator());
        classMapMenuItem.setText("Class Map...");
        classMapMenuItem.setFont(getFont());
        classMapMenuItem.setActionCommand("class.map");
        classMapMenuItem.setIcon(new ImageIcon("icons/Class.gif"));
        classMapMenuItem.addActionListener(this);
        processPopup.add(classMapMenuItem);
        cutActivityMenuItem.setText("Cut");
        cutActivityMenuItem.setFont(getFont());
        cutActivityMenuItem.setActionCommand("cut.activity");
        cutActivityMenuItem.setEnabled(false);
        cutActivityMenuItem.setIcon(new ImageIcon("icons/Cut.gif"));
        cutActivityMenuItem.addActionListener(this);
        activityPopup.add(cutActivityMenuItem);
        copyActivityMenuItem.setText("Copy");
        copyActivityMenuItem.setFont(getFont());
        copyActivityMenuItem.setActionCommand("copy.activity");
        copyActivityMenuItem.setEnabled(false);
        copyActivityMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyActivityMenuItem.addActionListener(this);
        activityPopup.add(copyActivityMenuItem);
        pasteActivityMenuItem.setText("Paste");
        pasteActivityMenuItem.setFont(getFont());
        pasteActivityMenuItem.setActionCommand("paste.activity");
        pasteActivityMenuItem.setEnabled(false);
        pasteActivityMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteActivityMenuItem.addActionListener(this);
        activityPopup.add(pasteActivityMenuItem);
        activityPopup.add(new JSeparator());
        addWorkMenuItem.setText("Add Work...");
        addWorkMenuItem.setFont(getFont());
        addWorkMenuItem.setActionCommand("add.work");
        addWorkMenuItem.setIcon(new ImageIcon("icons/Blank.gif"));
        addWorkMenuItem.addActionListener(this);
        activityPopup.add(addWorkMenuItem);
        deleteActivityMenuItem.setText("Delete");
        deleteActivityMenuItem.setFont(getFont());
        deleteActivityMenuItem.setActionCommand("delete.activity");
        deleteActivityMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        deleteActivityMenuItem.addActionListener(this);
        activityPopup.add(deleteActivityMenuItem);
        cutWorkMenuItem.setText("Cut");
        cutWorkMenuItem.setFont(getFont());
        cutWorkMenuItem.setActionCommand("cut.work");
        cutWorkMenuItem.setEnabled(false);
        cutWorkMenuItem.setIcon(new ImageIcon("icons/Cut.gif"));
        cutWorkMenuItem.addActionListener(this);
        workPopup.add(cutWorkMenuItem);
        copyWorkMenuItem.setText("Copy");
        copyWorkMenuItem.setFont(getFont());
        copyWorkMenuItem.setActionCommand("copy.work");
        copyWorkMenuItem.setEnabled(false);
        copyWorkMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyWorkMenuItem.addActionListener(this);
        workPopup.add(copyWorkMenuItem);
        pasteWorkMenuItem.setText("Paste");
        pasteWorkMenuItem.setFont(getFont());
        pasteWorkMenuItem.setActionCommand("paste.work");
        pasteWorkMenuItem.setEnabled(false);
        pasteWorkMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteWorkMenuItem.addActionListener(this);
        workPopup.add(pasteWorkMenuItem);
        workPopup.add(new JSeparator());
        deleteWorkMenuItem.setText("Delete");
        deleteWorkMenuItem.setFont(getFont());
        deleteWorkMenuItem.setActionCommand("delete.work");
        deleteWorkMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        deleteWorkMenuItem.addActionListener(this);
        workPopup.add(deleteWorkMenuItem);
        workflowPanel.setRemoveDefinition(true);
        workflowPanel.setInsertNodeCallback(this);
        workflowPanel.setDeleteNodeCallback(this);
    }

    private void exitForm(WindowEvent evt)
    {
        System.exit(0);
    }

    public static void main(String args[])
    {
        splashWindow = new SplashWindow("icons/Splash.jpg", 500);
        splashWindow.setStatusText("Copyright (c) 2004, EESIN Information Technology Ltd.");
        splashWindow.toFront();
        WorkflowModeler wm = new WorkflowModeler();
        wm.setVisible(true);
        splashWindow.startClose();
        splashWindow = null;
    }

    public void valueChanged(TreeSelectionEvent tse)
    {
        try
        {
            TreePath path = tse.getPath();
            if(path == null)
                return;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            DOSChangeable dosObject = (DOSChangeable)node.getUserObject();
            if(dosObject == null)
                return;
            String objectType = (String)dosObject.get("object.type");
            if(objectType.equals("model"))
                actionPerformed(null);
            else
            if(objectType.equals("process"))
                actionPerformed(new ActionEvent(tabTitleButtons[0], 0, "information"));
            else
            if(objectType.equals("activity"))
                actionPerformed(new ActionEvent(tabTitleButtons[0], 0, "information"));
            else
                objectType.equals("work");
            if(dosObject.get("is.populated") != null)
                return;
            ArrayList arrayList = null;
            Iterator listKey = null;
            DOSChangeable tempObject = null;
            WorkflowTreeNode treeNode = null;
            String tempString = null;
            if(objectType.equals("model"))
            {
                arrayList = wfm.listProcessDefinitionOfModel((String)dosObject.get("ouid"));
                if(Utils.isNullArrayList(arrayList))
                    return;
                dosObject.put("is.populated", Utils.True);
                for(listKey = arrayList.iterator(); listKey.hasNext();)
                {
                    tempObject = wfm.getProcessDefinition((String)listKey.next());
                    tempObject.put("object.type", "process");
                    treeNode = new WorkflowTreeNode(tempObject);
                    node.add(treeNode);
                    allTreeNode.put(tempObject.get("ouid"), treeNode);
                    treeNode = null;
                    tempObject = null;
                }

                listKey = null;
                arrayList = null;
            } else
            if(objectType.equals("process"))
            {
                tempString = (String)dosObject.get("ouid");
                arrayList = wfm.getProcessActivities(tempString);
                if(Utils.isNullArrayList(arrayList))
                    return;
                dosObject.put("is.populated", Utils.True);
                for(listKey = arrayList.iterator(); listKey.hasNext();)
                {
                    tempObject = (DOSChangeable)listKey.next();
                    tempObject.put("object.type", "activity");
                    tempObject.put("ouid@process", tempString);
                    treeNode = new WorkflowTreeNode(tempObject);
                    node.add(treeNode);
                    allTreeNode.put(tempObject.get("ouid"), treeNode);
                    treeNode = null;
                    tempObject = null;
                }

                listKey = null;
                arrayList = null;
            } else
            {
                objectType.equals("activity");
            }
            if(node.getLevel() == 0)
                processTree.fireTreeExpanded(path);
            else
                processTree.fireTreeCollapsed(path);
            node = null;
            dosObject = null;
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void mousePressed(MouseEvent evt)
    {
        if(SwingUtilities.isRightMouseButton(evt))
            processTree.setSelectionPath(processTree.getClosestPathForLocation(evt.getX(), evt.getY()));
    }

    public void mouseReleased(MouseEvent evt)
    {
        if(evt.getClickCount() == 1 && evt.isPopupTrigger())
        {
            TreePath treePath = processTree.getSelectionPath();
            WorkflowTreeNode node = (WorkflowTreeNode)treePath.getLastPathComponent();
            DOSChangeable dosObject = (DOSChangeable)node.getUserObject();
            if(dosObject == null)
                return;
            String objectType = (String)dosObject.get("object.type");
            Dimension frameSize = getSize();
            JPopupMenu popup = null;
            if(objectType.equals("model"))
                popup = modelPopup;
            else
            if(objectType.equals("process"))
                popup = processPopup;
            else
            if(objectType.equals("activity"))
                popup = activityPopup;
            else
            if(objectType.equals("work"))
                popup = workPopup;
            Dimension popupSize = popup.getSize();
            int dividerLocation = splitPane.getDividerLocation();
            Point point = SwingUtilities.convertPoint(evt.getComponent(), evt.getX(), evt.getY(), this);
            int x = evt.getX();
            int y = evt.getY();
            if(popupSize.width + point.x >= dividerLocation)
                x -= popupSize.width;
            if(popupSize.height + point.y >= frameSize.height)
                y -= popupSize.height;
            popup.show(evt.getComponent(), x, y);
        }
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

    public void actionPerformed(ActionEvent evt)
    {
        if(evt == null)
        {
            for(i = 0; i < tabTitleButtons.length; i++)
                tabTitleButtons[i].setSelected(true);

            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            tabPanel.updateUI();
            return;
        }
        String actionCommand = evt.getActionCommand();
        if(actionCommand.equals("exit"))
        {
            exitForm(null);
            return;
        }
        if(actionCommand.equals("open"))
        {
            ModelSelectDialog dialog = new ModelSelectDialog(this, dos, true);
            dialog.setDialogReturnCallback(this);
            UIUtils.setLocationRelativeTo(dialog, openButton);
            dialog.show();
            dialog = null;
            tabPanel.updateUI();
            return;
        }
        TreePath path = processTree.getSelectionPath();
        if(path == null)
        {
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            tabPanel.updateUI();
            return;
        }
        WorkflowTreeNode node = (WorkflowTreeNode)path.getLastPathComponent();
        WorkflowTreeNode parentNode = null;
        DOSChangeable dosObject = (DOSChangeable)node.getUserObject();
        DOSChangeable tempObject = null;
        String objectType = (String)dosObject.get("object.type");
        String tempString = null;
        if(actionCommand.equals("save"))
        {
            if(Utils.isNullString(objectType))
            {
                tabPanel.updateUI();
                return;
            }
            if(!objectType.equals("model"))
                if(objectType.equals("process"))
                {
                    if(processPanel == null)
                    {
                        tabPanel.updateUI();
                        return;
                    }
                    tempObject = processPanel.getValue();
                    dosObject.setValueMap(tempObject.getValueMap());
                    dosObject.setOriginalValueMap(tempObject.getOriginalValueMap());
                    tempObject.clear();
                    tempObject = null;
                    DOSChangeable processDefinition = null;
                    String processDefinitionOuid = null;
                    String identifier = null;
                    ArrayList processList = null;
                    if(modelOuid == null || dosObject == null)
                        return;
                    try
                    {
                        identifier = (String)dosObject.get("identifier");
                        if(Utils.isNullString(identifier))
                            return;
                        processList = wfm.listProcessDefinitionOfModel(modelOuid);
                        wfm.setProcessDefinition(dosObject);
                    }
                    catch(IIPRequestException e)
                    {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                    }
                    tabPanel.updateUI();
                } else
                if(objectType.equals("activity"))
                {
                    if(activityPanel == null)
                    {
                        tabPanel.updateUI();
                        return;
                    }
                    tempObject = activityPanel.getValue();
                    dosObject.setValueMap(tempObject.getValueMap());
                    dosObject.setOriginalValueMap(tempObject.getOriginalValueMap());
                    tempObject.clear();
                    tempObject = null;
                    try
                    {
                        wfm.setActivityDefinition(dosObject);
                    }
                    catch(IIPRequestException e)
                    {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                    }
                    tabPanel.updateUI();
                } else
                {
                    objectType.equals("work");
                }
        } else
        if(actionCommand.equals("information"))
        {
            for(i = 0; i < tabTitleButtons.length; i++)
                tabTitleButtons[i].setSelected(true);

            tabTitleButtons[0].setSelected(false);
            if(Utils.isNullString(objectType))
            {
                tabPanel.updateUI();
                return;
            }
            if(!objectType.equals("model"))
                if(objectType.equals("process"))
                {
                    if(processPanel == null)
                        processPanel = new ProcessPanel();
                    processPanel.setValue(dosObject);
                    if(tabPanel.getComponentCount() > 1)
                        tabPanel.remove(1);
                    tabPanel.add(processPanel, "Center");
                    tabPanel.updateUI();
                } else
                if(objectType.equals("activity"))
                {
                    if(activityPanel == null)
                        activityPanel = new ActivityPanel();
                    try
                    {
                        tempObject = wfm.getActivityDefinition((String)dosObject.get("ouid"));
                        tempObject.put("object.type", "activity");
                        activityPanel.setValue(tempObject);
                        if(tabPanel.getComponentCount() > 1)
                            tabPanel.remove(1);
                        tabPanel.add(activityPanel, "Center");
                    }
                    catch(IIPRequestException e)
                    {
                        if(tabPanel.getComponentCount() > 1)
                            tabPanel.remove(1);
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                    }
                    tabPanel.updateUI();
                    tempObject = null;
                } else
                {
                    objectType.equals("work");
                }
        } else
        if(actionCommand.equals("workflow"))
        {
            for(i = 0; i < tabTitleButtons.length; i++)
                tabTitleButtons[i].setSelected(true);

            tabTitleButtons[1].setSelected(false);
            if(objectType.equals("process"))
                workflowPanel.loadWorkflow((String)dosObject.get("ouid"));
            else
            if(objectType.equals("activity"))
                workflowPanel.loadWorkflow((String)dosObject.get("ouid@process"));
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            tabPanel.add(workflowPanel, "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("add.process"))
        {
            ProcessAddDialog dialog = new ProcessAddDialog(this, wfm, true);
            dialog.setModelOuid(modelOuid);
            dialog.setDialogReturnCallback(this);
            UIUtils.setLocationRelativeTo(dialog, this);
            dialog.show();
            dialog = null;
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("add.activity"))
        {
            for(i = 0; i < tabTitleButtons.length; i++)
                tabTitleButtons[i].setSelected(true);

            tabTitleButtons[1].setSelected(false);
            tempString = (String)dosObject.get("ouid");
            workflowPanel.loadWorkflow(tempString);
            workflowPanel.setGraphMode(3);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            tabPanel.add(workflowPanel, "Center");
            tabPanel.updateUI();
        } else
        if(!actionCommand.equals("delete.process") && !actionCommand.equals("copy.process") && !actionCommand.equals("paste.process") && !actionCommand.equals("cut.process"))
        {
            if(actionCommand.equals("class.map"))
            {
                ClassMapDialog dialog = new ClassMapDialog(this, dos, true, (String)dosObject.get("ouid"));
                dialog.setDialogReturnCallback(this);
                dialog.show();
                dialog = null;
                return;
            }
            if(!actionCommand.equals("add.work"))
                if(actionCommand.equals("delete.activity"))
                {
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected workflow activity?", "Confirm deletion", 0);
                    if(confirm != 0)
                        return;
                    tempString = (String)dosObject.get("ouid");
                    try
                    {
                        wfm.removeActivityDefinition(tempString);
                    }
                    catch(IIPRequestException e)
                    {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                    }
                    allTreeNode.remove(tempString);
                    parentNode = (WorkflowTreeNode)node.getParent();
                    if(parentNode != null)
                        parentNode.remove(node);
                    parentNode = null;
                    workflowPanel.loadWorkflow((String)dosObject.get("ouid@process"));
                    processTree.updateUI();
                } else
                if(!actionCommand.equals("copy.activity") && !actionCommand.equals("paste.activity") && !actionCommand.equals("cut.activity") && !actionCommand.equals("delete.work") && !actionCommand.equals("copy.work") && !actionCommand.equals("paste.work"))
                    actionCommand.equals("cut.work");
        }
    }

    public void setDialogReturnValue(Object value)
    {
        if(value instanceof DOSChangeable)
        {
            DOSChangeable dosObject = (DOSChangeable)value;
            String objectType = (String)dosObject.get("object.type");
            if(Utils.isNullString(objectType))
            {
                dosObject = null;
                return;
            }
            DOSChangeable tempObject = null;
            String tempString = null;
            if(objectType.equals("model"))
            {
                processTree.setModel(new WorkflowTreeModel(dosObject));
                processScrollPane.setViewportView(processTree);
                modelOuid = (String)dosObject.get("ouid");
            } else
            if(objectType.equals("process"))
                try
                {
                    wfm.addProcessDefinitionToModel(modelOuid, (String)dosObject.get("ouid"));
                    ((WorkflowTreeNode)processTree.getModel().getRoot()).add(new WorkflowTreeNode(dosObject));
                    processTree.updateUI();
                }
                catch(IIPRequestException e)
                {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error Message", 0);
                }
            else
            if(objectType.equals("add.activity.workflow"))
            {
                TreePath path = processTree.getSelectionPath();
                if(path == null)
                    return;
                WorkflowTreeNode node = (WorkflowTreeNode)path.getLastPathComponent();
                DOSChangeable dosObject2 = (DOSChangeable)node.getUserObject();
                ActivityAddDialog dialog = new ActivityAddDialog(this, wfm, true);
                tempObject = new DOSChangeable();
                tempObject.put("object.type", "activity");
                tempObject.put("ouid@process.definition", (String)dosObject2.get("ouid"));
                tempObject.put("process.name", (String)dosObject2.get("name"));
                tempObject.put("process.identifier", (String)dosObject2.get("identifier"));
                tempObject.put("x", (Integer)dosObject.get("x"));
                tempObject.put("y", (Integer)dosObject.get("y"));
                dosObject2 = null;
                dialog.setValue(tempObject);
                dialog.setDialogReturnCallback(this);
                UIUtils.setLocationRelativeTo(dialog, this);
                tempObject = null;
                dialog.show();
                dialog = null;
            } else
            if(objectType.equals("add.activity"))
            {
                TreePath path = processTree.getSelectionPath();
                if(path == null)
                    return;
                dosObject.put("object.type", "activity");
                ((WorkflowTreeNode)path.getLastPathComponent()).add(new WorkflowTreeNode(dosObject));
                processTree.updateUI();
                if(workflowPanel != null && workflowPanel.isShowing())
                    workflowPanel.loadWorkflow((String)dosObject.get("ouid@process.definition"));
            } else
            if(objectType.equals("delete.activity.workflow"))
            {
                tempString = (String)dosObject.get("ouid");
                WorkflowTreeNode node = (WorkflowTreeNode)allTreeNode.remove(tempString);
                if(node == null)
                    return;
                WorkflowTreeNode parentNode = (WorkflowTreeNode)node.getParent();
                if(parentNode != null)
                    parentNode.remove(node);
                parentNode = null;
                processTree.updateUI();
            }
            dosObject = null;
        }
    }

    public static SplashWindow splashWindow = null;
    public static Client dfw = null;
    public static DOS dos = null;
    public static WFM wfm = null;
    public static OLM olm = null;
    public static NDS nds = null;
    public static DSS dss = null;
    public String modelOuid;
    public HashMap allTreeNode;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu editMenu;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem cutMenuItem;
    private JMenuItem deleteMenuItem;
    private JToolBar toolBar;
    private JButton openButton;
    private JButton saveButton;
    private WorkflowEditorPanel workflowPanel;
    private JScrollPane processScrollPane;
    private JSplitPane splitPane;
    private JTree processTree;
    private JPanel tabPanel;
    private JPanel tabTitlePanel;
    private JToggleButton tabTitleButtons[];
    private JPopupMenu modelPopup;
    private JMenuItem addProcessMenuItem;
    private JPopupMenu processPopup;
    private JMenuItem addActivityMenuItem;
    private JMenuItem deleteProcessMenuItem;
    private JMenuItem copyProcessMenuItem;
    private JMenuItem cutProcessMenuItem;
    private JMenuItem pasteProcessMenuItem;
    private JMenuItem classMapMenuItem;
    private JPopupMenu activityPopup;
    private JMenuItem addWorkMenuItem;
    private JMenuItem deleteActivityMenuItem;
    private JMenuItem copyActivityMenuItem;
    private JMenuItem cutActivityMenuItem;
    private JMenuItem pasteActivityMenuItem;
    private JPopupMenu workPopup;
    private JMenuItem deleteWorkMenuItem;
    private JMenuItem copyWorkMenuItem;
    private JMenuItem cutWorkMenuItem;
    private JMenuItem pasteWorkMenuItem;
    private DOSChangeable rootNode;
    private int i;
    private ProcessPanel processPanel;
    private ActivityPanel activityPanel;


}
