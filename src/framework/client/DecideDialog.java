// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DecideDialog.java

package dyna.framework.client;

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.MInternalFrame;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, LogIn, UIManagement

public class DecideDialog extends JDialog
    implements ActionListener, WindowListener
{

    public DecideDialog(JFrame frame, String processOuid, String activityOuid, HashMap rejectMap)
    {
        super(frame, true);
        screenSize = null;
        windowSize = null;
        toolBar = null;
        confirmButton = null;
        outerPanel = null;
        innerPanel = null;
        topPanel = null;
        acceptRadioButton = null;
        rejectRadioButton = null;
        decidePanel = null;
        commentsPanel = null;
        commentsNamePanel = null;
        commentsName = null;
        commentsScrollPane = null;
        commentsTextArea = null;
        rbsIcon = null;
        rbIcon = null;
        rbpIcon = null;
        rbrIcon = null;
        rbrsIcon = null;
        dos = null;
        wfm = null;
        aus = null;
        processDefinitionOuid = null;
        this.processOuid = null;
        this.activityOuid = null;
        processDefinition = null;
        processInstance = null;
        newUI = null;
        this.rejectMap = null;
        this.processOuid = processOuid;
        this.activityOuid = activityOuid;
        dos = DynaMOAD.dos;
        wfm = DynaMOAD.wfm;
        aus = DynaMOAD.aus;
        newUI = DynaMOAD.newUI;
        this.rejectMap = rejectMap;
        try
        {
            if(processOuid.startsWith("wf$ipro@"))
            {
                this.processOuid = processOuid;
                processInstance = wfm.getProcess(processOuid);
                processDefinitionOuid = (String)processInstance.get("ouid@process.definition");
            }
            if(processDefinitionOuid == null)
            {
                if(processInstance != null)
                {
                    processInstance.clear();
                    processInstance = null;
                }
                windowClosing(null);
                return;
            }
            processDefinition = wfm.getProcessDefinition(processDefinitionOuid);
            if(this.processOuid == null)
            {
                if(processInstance != null)
                {
                    processInstance.clear();
                    processInstance = null;
                }
                windowClosing(null);
            }
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        if(processDefinition == null)
        {
            dispose();
            return;
        } else
        {
            initialize();
            setVisible(true);
            return;
        }
    }

    public void initialize()
    {
        setTitle(DynaMOAD.getMSRString("WRD_0139", "Decide Approval", 3));
        setSize(400, 300);
        addWindowListener(this);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
        toolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
        confirmButton = new JButton();
        outerPanel = new JPanel();
        innerPanel = new JPanel();
        topPanel = new JPanel();
        decidePanel = new JPanel();
        commentsPanel = new JPanel();
        commentsNamePanel = new JPanel();
        commentsName = new JLabel();
        commentsScrollPane = UIFactory.createStrippedScrollPane(null);
        commentsTextArea = new JTextArea();
        commentsTextArea.setText(" ");
        outerPanel.setLayout(new BorderLayout());
        innerPanel.setLayout(new BorderLayout());
        topPanel.setLayout(new BorderLayout());
        decidePanel.setLayout(new BorderLayout());
        MInternalFrame outerFrame = new MInternalFrame(null);
        outerFrame.putClientProperty("jgoodies.embeddedTabs", Boolean.TRUE);
        outerFrame.setLayout(new BorderLayout());
        outerFrame.setContent(innerPanel);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        outerPanel.add(outerFrame, "Center");
        getContentPane().add(toolBar, "North");
        confirmButton.setText(DynaMOAD.getMSRString("WRD_0139", "Decide", 3));
        confirmButton.setIcon(new ImageIcon("icons/decide.gif"));
        confirmButton.setActionCommand("confirm");
        confirmButton.setToolTipText(DynaMOAD.getMSRString("WRD_0139", "Decide Approval", 3));
        confirmButton.addActionListener(this);
        toolBar.add(confirmButton);
        getContentPane().add(outerPanel, "Center");
        innerPanel.add(topPanel, "North");
        topPanel.add(decidePanel, "Center");
        decidePanel.setBorder(BorderFactory.createEmptyBorder(2, 20, 4, 4));
        decidePanel.setLayout(new BoxLayout(decidePanel, 1));
        decidePanel.setMaximumSize(new Dimension(400, 200));
        decidePanel.setMinimumSize(new Dimension(400, 40));
        rbsIcon = new ImageIcon("icons/rbs.gif");
        rbIcon = new ImageIcon("icons/rb.gif");
        rbpIcon = new ImageIcon("icons/rbp.gif");
        rbrIcon = new ImageIcon("icons/rbr.gif");
        rbrsIcon = new ImageIcon("icons/rbrs.gif");
        acceptRadioButton = new JRadioButton(DynaMOAD.getMSRString("WRD_0150", "Accept", 3), true);
        acceptRadioButton.setOpaque(false);
        acceptRadioButton.setSelectedIcon(rbsIcon);
        acceptRadioButton.setIcon(rbIcon);
        acceptRadioButton.setPressedIcon(rbpIcon);
        acceptRadioButton.setRolloverIcon(rbrIcon);
        acceptRadioButton.setRolloverSelectedIcon(rbrsIcon);
        decidePanel.add(acceptRadioButton);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(acceptRadioButton);
        JRadioButton tempRadioButton = null;
        if(rejectMap != null && rejectMap.size() > 0)
        {
            rejectRadioButton = new JRadioButton[rejectMap.size()];
            int i = 0;
            String transitionOuid = null;
            for(Iterator mapKey = rejectMap.keySet().iterator(); mapKey.hasNext();)
            {
                transitionOuid = (String)mapKey.next();
                tempRadioButton = new JRadioButton(DynaMOAD.getMSRString("WRD_0151", "Reject to ", 3) + rejectMap.get(transitionOuid), false);
                tempRadioButton.setOpaque(false);
                tempRadioButton.setActionCommand(transitionOuid);
                tempRadioButton.setSelectedIcon(rbsIcon);
                tempRadioButton.setIcon(rbIcon);
                tempRadioButton.setPressedIcon(rbpIcon);
                tempRadioButton.setRolloverIcon(rbrIcon);
                tempRadioButton.setRolloverSelectedIcon(rbrsIcon);
                decidePanel.add(tempRadioButton);
                buttonGroup.add(tempRadioButton);
                rejectRadioButton[i] = tempRadioButton;
                i++;
                transitionOuid = null;
            }

            Object obj = null;
        }
        commentsPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        commentsPanel.setLayout(new BorderLayout(0, 0));
        innerPanel.add(commentsPanel, "Center");
        commentsScrollPane.setViewportView(commentsTextArea);
        commentsPanel.setLayout(new BorderLayout());
        commentsPanel.add(commentsScrollPane, "Center");
        commentsNamePanel.setLayout(new FlowLayout(0, 0, 0));
        commentsName.setText(DynaMOAD.getMSRString("WRD_0147", "Comments", 3));
        commentsName.setOpaque(false);
        commentsNamePanel.add(commentsName);
        commentsPanel.add(commentsNamePanel, "North");
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent evt)
    {
        String actionCommand = evt.getActionCommand();
        if(actionCommand == null)
            windowClosing(null);
        else
        if(actionCommand.equals("confirm"))
        {
            String tempString = null;
            DOSChangeable comments = null;
            tempString = commentsTextArea.getText();
            if(Utils.isNullString(tempString))
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0003", "Please write a comments.", 3));
                return;
            }
            comments = new DOSChangeable();
            comments.put("comments", tempString);
            if(acceptRadioButton.isSelected())
            {
                tempString = "'" + acceptRadioButton.getText() + "' " + DynaMOAD.getMSRString("QST_0011", "selected. ", 3);
                comments.put("decide", "A");
            } else
            if(rejectRadioButton != null && rejectRadioButton.length > 0)
            {
                for(int i = 0; i < rejectRadioButton.length; i++)
                    if(rejectRadioButton[i].isSelected())
                    {
                        tempString = "'" + rejectRadioButton[i].getText() + "' " + DynaMOAD.getMSRString("QST_0011", "selected. ", 3);
                        comments.put("decide", "J");
                    }

            }
            int confirm = JOptionPane.showConfirmDialog(this, tempString + DynaMOAD.getMSRString("QST_0012", "Are you sure?", 3), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 3), 0);
            if(confirm != 0)
            {
                comments.clear();
                comments = null;
                return;
            }
            try
            {
                if(acceptRadioButton.isSelected())
                {
                    comments.put("ouid@process", processOuid);
                    comments.put("id.user", LogIn.userID);
                    wfm.createComments(comments);
                    comments.clear();
                    comments = null;
                    wfm.markActivityForInboxControl(activityOuid, "A");
                    wfm.finishActivity(activityOuid);
                } else
                if(rejectRadioButton != null && rejectRadioButton.length > 0)
                {
                    for(int i = 0; i < rejectRadioButton.length; i++)
                        if(rejectRadioButton[i].isSelected())
                        {
                            comments.put("ouid@process", processOuid);
                            comments.put("id.user", LogIn.userID);
                            wfm.createComments(comments);
                            comments.clear();
                            comments = null;
                            wfm.markActivityForInboxControl(activityOuid, "J");
                            wfm.finishActivity(activityOuid, rejectRadioButton[i].getActionCommand());
                        }

                }
                windowClosing(null);
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
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

    private Dimension screenSize;
    private Dimension windowSize;
    private JToolBar toolBar;
    private JButton confirmButton;
    private JPanel outerPanel;
    private JPanel innerPanel;
    private JPanel topPanel;
    private JRadioButton acceptRadioButton;
    private JRadioButton rejectRadioButton[];
    private JPanel decidePanel;
    private JPanel commentsPanel;
    private JPanel commentsNamePanel;
    private JLabel commentsName;
    private JScrollPane commentsScrollPane;
    private JTextArea commentsTextArea;
    private ImageIcon rbsIcon;
    private ImageIcon rbIcon;
    private ImageIcon rbpIcon;
    private ImageIcon rbrIcon;
    private ImageIcon rbrsIcon;
    private DOS dos;
    private WFM wfm;
    private AUS aus;
    private String processDefinitionOuid;
    private String processOuid;
    private String activityOuid;
    private DOSChangeable processDefinition;
    private DOSChangeable processInstance;
    private UIManagement newUI;
    private HashMap rejectMap;
}