// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CreateProcess.java

package dyna.framework.client;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.*;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, LogIn, UIManagement, ProcessUserAssignPanel, 
//            ProcessWorkflowPanel, UIGeneration, DecideDialog

public class CreateProcess extends JFrame
    implements ActionListener, WindowListener, MouseListener
{

    public CreateProcess(int mode, String procOuid, String objectOuid)
    {
        screenSize = null;
        windowSize = null;
        toolBar = null;
        startButton = null;
        saveButton = null;
        removeButton = null;
        decideButton = null;
        outerPanel = null;
        topPanel = null;
        namePanel = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        assignPanel = null;
        splitPane = null;
        commentsScrollPane = null;
        commentsTextArea = null;
        attachmentsScrollPane = null;
        attachmentsToolBar = null;
        detailButton = null;
        attachButton = null;
        detachButton = null;
        attachmentsTable = null;
        columnWidth = 100;
        attachmentsNames = null;
        attachmentsWidths = null;
        attachmentsData = null;
        isEnabledAssignView = false;
        dos = null;
        wfm = null;
        aus = null;
        dss = null;
        processDefinitionOuid = null;
        processOuid = null;
        this.objectOuid = null;
        objectOuidList = null;
        activityOuid = null;
        processDefinition = null;
        processInstance = null;
        this.mode = 0;
        newUI = null;
        commentsPanel = null;
        participantsPanel = null;
        attachmentsPanel = null;
        handCursor = new Cursor(12);
        processDefinitionOuid = procOuid;
        this.objectOuid = objectOuid;
        this.mode = mode;
        dos = DynaMOAD.dos;
        wfm = DynaMOAD.wfm;
        aus = DynaMOAD.aus;
        dss = DynaMOAD.dss;
        newUI = DynaMOAD.newUI;
        try
        {
            if(procOuid.startsWith("wf$ipro@"))
            {
                processOuid = procOuid;
                processInstance = wfm.getProcess(procOuid);
                processDefinitionOuid = (String)processInstance.get("ouid@process.definition");
            }
            if(processDefinitionOuid == null)
            {
                processInstance.clear();
                processInstance = null;
                windowClosing(null);
                return;
            }
            processDefinition = wfm.getProcessDefinition(processDefinitionOuid);
            if(processOuid == null)
            {
                processDefinition.put("ouid@process.definition", processDefinitionOuid);
                processDefinition.put("responsible", LogIn.userID);
                processDefinition.put("ouid@object", this.objectOuid);
                processOuid = wfm.createProcess(processDefinition);
                if(Utils.isNullString(processOuid))
                    throw new IIPRequestException("Process Canceled.");
                processInstance = wfm.getProcess(processOuid);
            } else
            {
                objectOuidList = (ArrayList)processInstance.get("ouid@object");
            }
            ArrayList activityList = wfm.getCurrentActivitiesByPerfomer(processOuid, LogIn.userID);
            if(activityList != null && activityList.size() > 0)
            {
                activityOuid = (String)((DOSChangeable)activityList.get(0)).get("ouid");
                if(!Utils.isNullString(activityOuid))
                    wfm.markActivityForInboxControl(activityOuid, "R");
                else
                    activityOuid = null;
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        if(processDefinition == null || processOuid == null)
        {
            dispose();
            return;
        }
        initialize();
        setValue();
        try
        {
            Object returnObject = Utils.executeScriptFile(dos.getEventName(processDefinitionOuid, "add.init"), dss, processOuid);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
            {
                wfm.removeProcess(processOuid);
                windowClosing(null);
            } else
            {
                setVisible(true);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public CreateProcess(int mode, String procOuid, ArrayList objectOuidList)
    {
        screenSize = null;
        windowSize = null;
        toolBar = null;
        startButton = null;
        saveButton = null;
        removeButton = null;
        decideButton = null;
        outerPanel = null;
        topPanel = null;
        namePanel = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true);
        assignPanel = null;
        splitPane = null;
        commentsScrollPane = null;
        commentsTextArea = null;
        attachmentsScrollPane = null;
        attachmentsToolBar = null;
        detailButton = null;
        attachButton = null;
        detachButton = null;
        attachmentsTable = null;
        columnWidth = 100;
        attachmentsNames = null;
        attachmentsWidths = null;
        attachmentsData = null;
        isEnabledAssignView = false;
        dos = null;
        wfm = null;
        aus = null;
        dss = null;
        processDefinitionOuid = null;
        processOuid = null;
        objectOuid = null;
        this.objectOuidList = null;
        activityOuid = null;
        processDefinition = null;
        processInstance = null;
        this.mode = 0;
        newUI = null;
        commentsPanel = null;
        participantsPanel = null;
        attachmentsPanel = null;
        handCursor = new Cursor(12);
        processDefinitionOuid = procOuid;
        this.objectOuidList = objectOuidList;
        this.mode = mode;
        dos = DynaMOAD.dos;
        wfm = DynaMOAD.wfm;
        aus = DynaMOAD.aus;
        dss = DynaMOAD.dss;
        newUI = DynaMOAD.newUI;
        try
        {
            if(procOuid.startsWith("wf$ipro@"))
            {
                processOuid = procOuid;
                processInstance = wfm.getProcess(procOuid);
                processDefinitionOuid = (String)processInstance.get("ouid@process.definition");
            }
            if(processDefinitionOuid == null)
            {
                processInstance.clear();
                processInstance = null;
                windowClosing(null);
                return;
            }
            processDefinition = wfm.getProcessDefinition(processDefinitionOuid);
            if(processOuid == null)
            {
                processDefinition.put("ouid@process.definition", processDefinitionOuid);
                processDefinition.put("responsible", LogIn.userID);
                processDefinition.put("ouid@object", this.objectOuidList);
                processOuid = wfm.createProcess(processDefinition);
                if(Utils.isNullString(processOuid))
                    throw new IIPRequestException("Process Canceled.");
                processInstance = wfm.getProcess(processOuid);
            } else
            {
                this.objectOuidList = (ArrayList)processInstance.get("ouid@object");
            }
            ArrayList activityList = wfm.getCurrentActivitiesByPerfomer(processOuid, LogIn.userID);
            if(activityList != null && activityList.size() > 0)
            {
                activityOuid = (String)((DOSChangeable)activityList.get(0)).get("ouid");
                if(!Utils.isNullString(activityOuid))
                    wfm.markActivityForInboxControl(activityOuid, "R");
                else
                    activityOuid = null;
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        if(processDefinition == null || processOuid == null)
        {
            dispose();
            return;
        }
        initialize();
        setValue();
        try
        {
            Object returnObject = Utils.executeScriptFile(dos.getEventName(processDefinitionOuid, "add.init"), dss, processOuid);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
            {
                wfm.removeProcess(processOuid);
                windowClosing(null);
            } else
            {
                setVisible(true);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void initialize()
    {
        if(mode == 0)
            setTitle(DynaMOAD.getMSRString((String)processDefinition.get("name"), (String)processDefinition.get("name"), 0) + " " + DynaMOAD.getMSRString("WRD_0138", "Process Creation", 3));
        else
        if(mode == 1)
            setTitle(DynaMOAD.getMSRString((String)processDefinition.get("name"), (String)processDefinition.get("name"), 0) + " " + DynaMOAD.getMSRString("WRD_0031", "Process", 3));
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/Process.gif"));
        setSize(600, 500);
        addWindowListener(this);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
        toolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
        outerPanel = new JPanel();
        topPanel = new JPanel();
        namePanel = new JPanel();
        tabPanel = new JPanel();
        tabTitlePanel = new JPanel();
        tabButtons = new JToggleButton[3];
        toolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        outerPanel.setLayout(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        topPanel.setLayout(new BorderLayout());
        namePanel.setLayout(new BorderLayout());
        getContentPane().add(toolBar, "North");
        startButton = new ToolBarButton(new ImageIcon("icons/Execute.gif"));
        startButton.setText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
        startButton.setActionCommand("start");
        startButton.setToolTipText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
        startButton.addActionListener(this);
        toolBar.add(startButton);
        saveButton = new ToolBarButton(new ImageIcon("icons/Modification.gif"));
        saveButton.setActionCommand("save");
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton.addActionListener(this);
        toolBar.add(saveButton);
        toolBar.add(Box.createHorizontalStrut(10));
        removeButton = new ToolBarButton(new ImageIcon("icons/Delete.gif"));
        removeButton.setActionCommand("remove");
        removeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        removeButton.addActionListener(this);
        toolBar.add(removeButton);
        toolBar.add(Box.createHorizontalStrut(10));
        decideButton = new JButton(new ImageIcon("icons/decide.gif"));
        decideButton.setActionCommand("decide");
        decideButton.setToolTipText(DynaMOAD.getMSRString("WRD_0139", "Decide Approval", 3));
        decideButton.setText(DynaMOAD.getMSRString("WRD_0139", "Decide  Approval", 3));
        decideButton.setEnabled(false);
        decideButton.addActionListener(this);
        toolBar.add(decideButton);
        getContentPane().add(outerPanel, "Center");
        outerPanel.add(topPanel, "North");
        topPanel.add(namePanel, "Center");
        namePanel.setLayout(null);
        namePanel.setMaximumSize(new Dimension(500, 67));
        namePanel.setMinimumSize(new Dimension(500, 67));
        namePanel.setPreferredSize(new Dimension(500, 67));
        idLabel = new JLabel();
        idLabel.setText(DynaMOAD.getMSRString("WRD_0140", "Process ID", 3));
        idLabel.setBounds(5, 4, 80, 21);
        namePanel.add(idLabel);
        idTextField = new JTextField();
        idTextField.setBounds(85, 4, 240, 21);
        idTextField.setBackground(UIManagement.textBoxNotEditableBack);
        idTextField.setEditable(false);
        namePanel.add(idTextField);
        nameLabel = new JLabel();
        nameLabel.setText(DynaMOAD.getMSRString("description", "Description", 3));
        nameLabel.setBounds(5, 30, 80, 21);
        namePanel.add(nameLabel);
        nameTextField = new JTextField();
        nameTextField.setBounds(85, 30, 240, 21);
        nameTextField.setBackground(UIManagement.textBoxNotEditableBack);
        nameTextField.setEditable(false);
        namePanel.add(nameTextField);
        responsibleLabel = new JLabel();
        responsibleLabel.setText(DynaMOAD.getMSRString("WRD_0033", "Responsible", 3));
        responsibleLabel.setBounds(350, 4, 80, 21);
        namePanel.add(responsibleLabel);
        responsibleTextField = new JTextField();
        responsibleTextField.setBounds(440, 4, 140, 21);
        responsibleTextField.setBackground(UIManagement.textBoxNotEditableBack);
        responsibleTextField.setEditable(false);
        namePanel.add(responsibleTextField);
        statusLabel = new JLabel();
        statusLabel.setText(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
        statusLabel.setBounds(350, 30, 80, 21);
        namePanel.add(statusLabel);
        statusTextField = new JTextField();
        statusTextField.setBounds(440, 30, 140, 21);
        statusTextField.setBackground(UIManagement.textBoxNotEditableBack);
        statusTextField.setEditable(false);
        namePanel.add(statusTextField);
        tabFrame = new MInternalFrame(null);
        tabFrame.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        tabFrame.setLayout(new BorderLayout());
        tabFrame.setContent(tabPanel);
        tabPanel.setLayout(new BorderLayout(0, 0));
        getContentPane().add(DynaMOAD.buildStatusBar(statusField), "South");
        assignPanel = new ProcessUserAssignPanel();
        assignPanel.setCreateProcess(this);
        splitPane = new BorderlessSplitPane(0, null, assignPanel);
        splitPane.setResizeWeight(0.5D);
        splitPane.setDividerSize(2);
        outerPanel.add(tabFrame, "Center");
        tabTitlePanel.setLayout(new FlowLayout(0, 0, 0));
        Insets tabInsets = new Insets(1, 5, 1, 5);
        for(int i = 0; i < 3; i++)
        {
            tabButtons[i] = new JToggleButton();
            tabButtons[i].setHorizontalAlignment(2);
            tabButtons[i].setMargin(tabInsets);
            tabButtons[i].setSelected(true);
            tabButtons[i].addActionListener(this);
            tabTitlePanel.add(tabButtons[i]);
        }

        tabButtons[0].setText(DynaMOAD.getMSRString("WRD_0141", "Comments", 3));
        tabButtons[0].setActionCommand("comments");
        tabButtons[1].setText(DynaMOAD.getMSRString("WRD_0142", "Attachments", 3));
        tabButtons[1].setActionCommand("attachments");
        tabButtons[2].setText(DynaMOAD.getMSRString("WRD_0143", "Participants", 3));
        tabButtons[2].setActionCommand("participants");
        tabPanel.add(tabTitlePanel, "North");
        tabButtons[0].doClick();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    private JPanel getCommentsPanel()
    {
        if(commentsPanel != null)
        {
            return commentsPanel;
        } else
        {
            commentsPanel = new JPanel();
            commentsScrollPane = UIFactory.createStrippedScrollPane(null);
            commentsTextArea = new JEditorPane();
            commentsTextArea.setContentType("text/html");
            commentsTextArea.setEditable(false);
            commentsScrollPane.setViewportView(commentsTextArea);
            commentsScrollPane.setBackground(DynaTheme.panelBackgroundColor);
            commentsPanel.setLayout(new BorderLayout());
            commentsPanel.add(commentsScrollPane, "Center");
            return commentsPanel;
        }
    }

    private JPanel getAttachmentsPanel()
    {
        if(attachmentsPanel != null)
            return attachmentsPanel;
        attachmentsPanel = new JPanel();
        attachmentsToolBar = new ExtToolBar("attachmentsToolBar", HeaderStyle.BOTH);
        detailButton = new ToolBarButton(new ImageIcon("icons/Open.gif"));
        detailButton.setActionCommand("detail");
        detailButton.setText(DynaMOAD.getMSRString("WRD_0074", "Open", 3));
        detailButton.setToolTipText(DynaMOAD.getMSRString("WRD_0074", "Open", 3));
        detailButton.addActionListener(this);
        attachButton = new ToolBarButton(new ImageIcon("icons/add_att.gif"));
        attachButton.setActionCommand("attachButton");
        attachButton.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Add", 3));
        attachButton.addActionListener(this);
        detachButton = new ToolBarButton(new ImageIcon("icons/remove_att.gif"));
        detachButton.setActionCommand("detachButton");
        detachButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Remove", 3));
        detachButton.addActionListener(this);
        attachmentsToolBar.add(detailButton);
        attachmentsToolBar.add(attachButton);
        attachmentsToolBar.add(detachButton);
        attachmentsScrollPane = UIFactory.createStrippedScrollPane(null);
        attachmentsTable = makeAttachmentsTable(objectOuidList);
        attachmentsScrollPane.setViewportView(attachmentsTable.getTable());
        attachmentsScrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        attachmentsTable.getTable().addMouseListener(this);
        attachmentsTable.getTable().setCursor(handCursor);
        attachmentsPanel.setLayout(new BorderLayout());
        attachmentsPanel.add(attachmentsToolBar, "North");
        attachmentsPanel.add(attachmentsScrollPane, "Center");
        if(!processInstance.get("ouid@workflow.status").equals("100"))
        {
            attachButton.setEnabled(false);
            detachButton.setEnabled(false);
        } else
        {
            attachButton.setEnabled(true);
            detachButton.setEnabled(false);
        }
        if(attachmentsTable.getTable().getRowCount() > 0)
            detailButton.setEnabled(true);
        else
            detailButton.setEnabled(false);
        return attachmentsPanel;
    }

    public void setEnableAssignView(boolean enable)
    {
        isEnabledAssignView = enable;
        if(enable && participantsPanel.isDisplayable())
        {
            outerPanel.remove(tabFrame);
            outerPanel.add(splitPane, "Center");
            splitPane.setTopComponent(tabFrame);
        } else
        {
            splitPane.setTopComponent(null);
            outerPanel.remove(splitPane);
            outerPanel.add(tabFrame, "Center");
        }
        outerPanel.updateUI();
    }

    public JPanel getParticipantsPanel()
    {
        if(participantsPanel != null)
            return participantsPanel;
        setVisible(false);
        participantsPanel = new ProcessWorkflowPanel(processInstance, false);
        participantsPanel.setCreateProcess(this);
        if(!Utils.isNullString(activityOuid))
            participantsPanel.setActivityOuid(activityOuid);
        participantsPanel.loadWorkflow(processOuid);
        Point point = participantsPanel.getMaximumPoint();
        point.y = point.y + 220;
        resizeMySelf(point);
        setVisible(true);
        return participantsPanel;
    }

    public void resizeMySelf(Point point)
    {
        if(point.x < 600)
            point.x = 600;
        if(point.y < 500)
            point.y = 500;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if(point.x > screenSize.width)
            point.x = screenSize.width;
        if(point.y > screenSize.height)
            point.y = screenSize.height;
        setSize(point.x, point.y);
        Dimension windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
    }

    public Table makeAttachmentsTable(ArrayList oidList)
    {
        attachmentsNames = new ArrayList();
        attachmentsWidths = new ArrayList();
        attachmentsData = new ArrayList();
        attachmentsNames.add(DynaMOAD.getMSRString("WRD_0020", "Number", 3));
        attachmentsNames.add(DynaMOAD.getMSRString("WRD_0021", "Description", 3));
        attachmentsNames.add(DynaMOAD.getMSRString("WRD_0024", "Status", 3));
        attachmentsNames.add(DynaMOAD.getMSRString("WRD_0025", "User", 3));
        attachmentsWidths.add(new Integer(columnWidth));
        attachmentsWidths.add(new Integer(columnWidth * 2));
        attachmentsWidths.add(new Integer(columnWidth));
        attachmentsWidths.add(new Integer(columnWidth));
        if(oidList != null)
            try
            {
                ArrayList aRow = null;
                DOSChangeable aDos = null;
                for(int i = 0; i < oidList.size(); i++)
                {
                    aDos = dos.get((String)oidList.get(i));
                    if(aDos != null)
                    {
                        aRow = new ArrayList();
                        aRow.add(aDos.get("ouid"));
                        aRow.add(aDos.get("md$number"));
                        aRow.add(aDos.get("md$description"));
                        aRow.add(aDos.get("md$status"));
                        aRow.add(aDos.get("md$user"));
                        attachmentsData.add(aRow);
                        aRow = null;
                        aDos = null;
                    }
                }

            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        attachmentsTable = new Table(attachmentsData, (ArrayList)attachmentsNames.clone(), (ArrayList)attachmentsWidths.clone(), 1, 79);
        attachmentsTable.setIndexColumn(0);
        attachmentsTable.setColumnSequence(new int[] {
            1, 2, 3, 4
        });
        return attachmentsTable;
    }

    public void setValue()
    {
        if(processDefinition == null || processInstance == null)
            return;
        String tempString = null;
        tempString = (String)processInstance.get("ouid");
        if(tempString != null)
            idTextField.setText(tempString);
        tempString = (String)processDefinition.get("description");
        if(tempString != null)
            nameTextField.setText(tempString);
        try
        {
            HashMap user = null;
            if(mode == 0)
                tempString = LogIn.userID;
            else
                tempString = (String)processInstance.get("responsible");
            if(!Utils.isNullString(tempString))
            {
                user = aus.getUser(tempString);
                if(LogIn.userID.equals(tempString))
                {
                    if(processInstance.get("ouid@workflow.status").equals("100"))
                    {
                        removeButton.setEnabled(true);
                        startButton.setText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
                        startButton.setToolTipText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
                        startButton.setActionCommand("start");
                        if(mode == 1)
                            startButton.setEnabled(true);
                        else
                            startButton.setEnabled(false);
                    } else
                    if(processInstance.get("ouid@workflow.status").equals("120"))
                    {
                        startButton.setText(DynaMOAD.getMSRString("WRD_0146", "Resume Process", 3));
                        startButton.setToolTipText(DynaMOAD.getMSRString("WRD_0146", "Resume Process", 3));
                        startButton.setActionCommand("resume");
                        startButton.setEnabled(true);
                        saveButton.setEnabled(false);
                        removeButton.setEnabled(false);
                    } else
                    {
                        startButton.setText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
                        startButton.setToolTipText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
                        startButton.setActionCommand("start");
                        startButton.setEnabled(false);
                        saveButton.setEnabled(false);
                        removeButton.setEnabled(false);
                    }
                } else
                {
                    startButton.setText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
                    startButton.setToolTipText(DynaMOAD.getMSRString("WRD_0058", "Start Process", 3));
                    startButton.setActionCommand("start");
                    startButton.setEnabled(false);
                    saveButton.setEnabled(false);
                    removeButton.setEnabled(false);
                }
            }
            if(user != null)
            {
                responsibleTextField.setText((String)user.get("name") + " (" + tempString + ")");
                user.clear();
                user = null;
            }
            ArrayList activityList = wfm.getCurrentActivitiesByPerfomer(processOuid, LogIn.userID);
            if(activityList != null && activityList.size() > 0)
            {
                tempString = (String)((DOSChangeable)activityList.get(0)).get("ouid");
                if(Utils.isNullString(tempString) || !tempString.equals(activityOuid))
                    activityOuid = null;
            } else
            {
                activityOuid = null;
            }
            tempString = wfm.getProcessComments(processOuid);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        if(commentsPanel != null && !Utils.isNullString(tempString))
        {
            commentsTextArea.setText(tempString);
            commentsTextArea.setCaretPosition(0);
        }
        tempString = (String)processInstance.get("name@workflow.status");
        if(tempString != null)
            statusTextField.setText(tempString);
        if(participantsPanel != null)
        {
            participantsPanel.setProcessInstance(processInstance);
            participantsPanel.setActivityOuid(activityOuid);
            participantsPanel.loadWorkflow(processOuid);
        }
        if(!Utils.isNullString(activityOuid))
        {
            decideButton.setEnabled(true);
            if(participantsPanel != null)
                participantsPanel.setActivityOuid(activityOuid);
        } else
        {
            decideButton.setEnabled(false);
            if(participantsPanel != null)
                participantsPanel.setActivityOuid(null);
        }
    }

    public DOSChangeable getValue()
    {
        return null;
    }

    public void actionPerformed(ActionEvent evt)
    {
        String actionCommand = evt.getActionCommand();
        if(actionCommand == null)
        {
            if(!startButton.isEnabled() && processInstance.get("ouid@workflow.status").equals("100"))
            {
                int confirm = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0008", "Process will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirm deletion", 0), 0);
                if(confirm != 0)
                    return;
                try
                {
                    wfm.removeProcess(this.processOuid);
                }
                catch(IIPRequestException e)
                {
                    System.err.println();
                }
            }
            windowClosing(null);
        } else
        if(actionCommand.equals("comments"))
        {
            for(int i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[0].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getCommentsPanel();
            tabPanel.add(contentPanel, "Center");
            setEnableAssignView(isEnabledAssignView);
        } else
        if(actionCommand.equals("attachments"))
        {
            for(int i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[1].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getAttachmentsPanel();
            tabPanel.add(contentPanel, "Center");
            setEnableAssignView(isEnabledAssignView);
        } else
        if(actionCommand.equals("participants"))
        {
            for(int i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[2].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getParticipantsPanel();
            tabPanel.add(contentPanel, "Center");
            setEnableAssignView(isEnabledAssignView);
        } else
        if(actionCommand.equals("detail"))
        {
            DynaTable table = (DynaTable)attachmentsTable.getTable();
            if(table.getSelectedRowCount() == 0 && Utils.isNullString(this.objectOuid))
                return;
            String objectOuid = null;
            if(table.getSelectedRowCount() > 0)
            {
                ArrayList tempList = attachmentsTable.getSelectedOuidRows(table.getSelectedRows());
                if(!Utils.isNullArrayList(tempList))
                    objectOuid = (String)((ArrayList)attachmentsData.get((new Integer((String)tempList.get(0))).intValue())).get(0);
            } else
            {
                objectOuid = this.objectOuid;
            }
            try
            {
                String selectClassOuid = dos.getClassOuid(objectOuid);
                UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, objectOuid, 1);
                uiGeneration.setVisible(true);
                uiGeneration = null;
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        } else
        if(actionCommand.equals("save"))
            try
            {
                wfm.setProcess(processInstance);
                startButton.setEnabled(true);
                saveButton.setEnabled(false);
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        else
        if(actionCommand.equals("remove"))
        {
            int confirm = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0008", "Process will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirm deletion", 0), 0);
            if(confirm != 0)
                return;
            try
            {
                wfm.removeProcess(this.processOuid);
                windowClosing(null);
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        } else
        if(actionCommand.equals("start"))
        {
            ((ProcessWorkflowPanel)getParticipantsPanel()).run();
            boolean valid = ((ProcessWorkflowPanel)getParticipantsPanel()).validateParticipants(null);
            if(!valid)
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_W001", "Please check participants for this process.", 0), DynaMOAD.getMSRString("INF_0003", "Information Message", 0), 2);
                return;
            }
            if(LogIn.userID.equals(processInstance.get("responsible")) && processInstance.get("ouid@workflow.status").equals("100"))
                try
                {
                    String processOuid = null;
                    if(this.objectOuid != null)
                        processOuid = wfm.isLocked(this.objectOuid);
                    else
                    if(!Utils.isNullArrayList(objectOuidList))
                        processOuid = wfm.isLocked((String)objectOuidList.get(0));
                    if(Utils.isNullString(processOuid))
                    {
                        wfm.startProcess(this.processOuid);
                        processInstance.clear();
                        processInstance = wfm.getProcess(this.processOuid);
                        setValue();
                    } else
                    {
                        JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_0009", "Object locked by another process.", 0), DynaMOAD.getMSRString("WRD_0004", "Information Message", 0), 0);
                    }
                }
                catch(IIPRequestException e)
                {
                    System.err.println(e);
                }
        } else
        if(actionCommand.equals("resume"))
        {
            ((ProcessWorkflowPanel)getParticipantsPanel()).run();
            boolean valid = ((ProcessWorkflowPanel)getParticipantsPanel()).validateParticipants(null);
            if(!valid)
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_W001", "Please check participants for this process.", 0), DynaMOAD.getMSRString("INF_0003", "Information Message", 0), 2);
                return;
            }
            if(LogIn.userID.equals(processInstance.get("responsible")) && processInstance.get("ouid@workflow.status").equals("120"))
                try
                {
                    wfm.resumeProcess(this.processOuid);
                    processInstance.clear();
                    processInstance = wfm.getProcess(this.processOuid);
                    setValue();
                }
                catch(IIPRequestException e)
                {
                    System.err.println(e);
                }
        } else
        if(actionCommand.equals("decide"))
        {
            ((ProcessWorkflowPanel)getParticipantsPanel()).run();
            boolean valid = ((ProcessWorkflowPanel)getParticipantsPanel()).validateParticipants(activityOuid);
            if(!valid)
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_W001", "Please check participants for this process.", 0), DynaMOAD.getMSRString("INF_0003", "Information Message", 0), 2);
                return;
            }
            DecideDialog dialog = new DecideDialog(this, this.processOuid, activityOuid, ((ProcessWorkflowPanel)getParticipantsPanel()).getRejectMap());
            dialog = null;
            try
            {
                processInstance.clear();
                processInstance = wfm.getProcess(this.processOuid);
                setValue();
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        } else
        if(actionCommand.equals("attachButton"))
            System.out.println("attachButton");
        else
        if(actionCommand.equals("detachButton"))
        {
            DynaTable table = (DynaTable)attachmentsTable.getTable();
            int rowForDelete = 0;
            if(table.getSelectedRowCount() == 0)
                return;
            int selrows[] = table.getSelectedRows();
            if(table.getSelectedRowCount() > 0)
            {
                ArrayList tempList = attachmentsTable.getSelectedOuidRows(selrows);
                if(!Utils.isNullArrayList(tempList))
                {
                    for(int n = tempList.size() - 1; n >= 0; n--)
                    {
                        rowForDelete = (new Integer((String)tempList.get(n))).intValue();
                        attachmentsData.remove(rowForDelete);
                        objectOuidList.remove(rowForDelete);
                    }

                }
            }
            saveButton.setEnabled(true);
            detachButton.setEnabled(false);
            attachmentsTable.changeTableData();
        }
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent evt)
    {
        if(mode == 0 && !startButton.isEnabled() && processInstance.get("ouid@workflow.status").equals("100"))
            try
            {
                wfm.removeProcess(processOuid);
            }
            catch(IIPRequestException e)
            {
                System.err.println();
            }
        removeWindowListener(this);
        dispose();
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

    public void mouseClicked(MouseEvent me)
    {
        Object source = me.getSource();
        if(source.equals(attachmentsTable.getTable()))
        {
            int row = attachmentsTable.getTable().getSelectedRow();
            String ouidRow = attachmentsTable.getSelectedOuidRow(row);
            attachmentsTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
            if(row >= 0)
            {
                if(processInstance.get("ouid@workflow.status").equals("100"))
                    detachButton.setEnabled(true);
            } else
            {
                detachButton.setEnabled(false);
            }
            if(me.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(me))
                detailButton.doClick();
        }
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

    private Dimension screenSize;
    private Dimension windowSize;
    private JToolBar toolBar;
    private JButton startButton;
    private JButton saveButton;
    private JButton removeButton;
    private JButton decideButton;
    private JPanel outerPanel;
    private JPanel topPanel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel responsibleLabel;
    private JLabel statusLabel;
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField responsibleTextField;
    private JTextField statusTextField;
    private JPanel namePanel;
    private MInternalFrame tabFrame;
    private JPanel tabPanel;
    private JPanel tabTitlePanel;
    private JToggleButton tabButtons[];
    private JLabel statusField;
    public ProcessUserAssignPanel assignPanel;
    public JSplitPane splitPane;
    private JScrollPane commentsScrollPane;
    private JEditorPane commentsTextArea;
    private JScrollPane attachmentsScrollPane;
    private JToolBar attachmentsToolBar;
    private JButton detailButton;
    private JButton attachButton;
    private JButton detachButton;
    private Table attachmentsTable;
    private int columnWidth;
    private ArrayList attachmentsNames;
    private ArrayList attachmentsWidths;
    private ArrayList attachmentsData;
    private boolean isEnabledAssignView;
    public static final int MODE_CREATE = 0;
    public static final int MODE_PROCESS = 1;
    private DOS dos;
    private WFM wfm;
    private AUS aus;
    private DSS dss;
    private String processDefinitionOuid;
    private String processOuid;
    private String objectOuid;
    private ArrayList objectOuidList;
    private String activityOuid;
    private DOSChangeable processDefinition;
    private DOSChangeable processInstance;
    private int mode;
    private UIManagement newUI;
    private JPanel commentsPanel;
    private ProcessWorkflowPanel participantsPanel;
    private JPanel attachmentsPanel;
    private Cursor handCursor;
}