// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CheckIn.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.*;
import dyna.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, LogIn, FileTransferDialog, 
//            UIGeneration

public class CheckIn extends JDialog
    implements ActionListener, WindowListener, ItemListener, MouseListener
{
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


    public CheckIn(Frame parent, boolean modal, Object fileInfo)
    {
        super(parent, modal);
        fileChooser = null;
        softwareOid = null;
        softwareFilter = null;
        software = null;
        preselectedFile = null;
        selectedFile = null;
        result = false;
        oid = null;
        overlayIndex = 0;
        this.fileInfo = null;
        fileInfoList = null;
        dos = null;
        dss = null;
        newUI = null;
        parentFrame = null;
        ftd = null;
        _serverPath = null;
        _clientPath = null;
        _ftc = null;
        workingDirectory = null;
        workingPath = null;
        fileNameStr = null;
        extensionStr = null;
        fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
        utils = null;
        _session = null;
        smMode = 0;
        if(fileInfo instanceof HashMap)
        {
            this.fileInfo = (HashMap)fileInfo;
            smMode = 0;
        } else
        if(fileInfo instanceof ArrayList)
        {
            fileInfoList = (ArrayList)fileInfo;
            this.fileInfo = (HashMap)fileInfoList.get(0);
            smMode = 1;
        }
        parentFrame = parent;
        dos = DynaMOAD.dos;
        dss = DynaMOAD.dss;
        newUI = DynaMOAD.newUI;
        initComponents();
        optionPanel.setVisible(false);
        pack();
        UIUtils.setLocationRelativeTo(this, parent);
        File downLoadFile = new File((String)this.fileInfo.get("md$description"));
        fileNameStr = downLoadFile.getName();
        try
        {
            extensionStr = dss.getExtension((String)this.fileInfo.get("md$filetype.id"));
            String classOuid = dos.getClassOuid((String)this.fileInfo.get("md$ouid"));
            utils = new CADIntegrationUtils(classOuid, dos);
            workingDirectory = dss.getParent((String)this.fileInfo.get("md$description"));
            workingPath = (String)this.fileInfo.get("md$description");
            setObjectName((String)this.fileInfo.get("md$filetype.description") + " " + (Integer)this.fileInfo.get("md$index"));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        setPreselectedFilePath(workingPath);
    }

    public void setSession(Session session)
    {
        _session = session;
    }

    private void initComponents()
    {
        setBackground(UIManagement.panelBackGround);
        buttonPanel = new JPanel();
        processButton = new JButton();
        optionButton = new JButton();
        closeButton = new JButton();
        fieldPanel = new JPanel();
        objectLabel = new JLabel();
        filePathLabel = new JLabel();
        objectTextField = new JTextField();
        filePathTextField = new JTextField();
        optionPanel = new JPanel();
        uploadCheckBox = new JCheckBox();
        checkInCheckBox = new JCheckBox();
        versionUpCheckBox = new JCheckBox();
        optionToggleButton = new JToggleButton();
        getContentPane().setLayout(new GridBagLayout());
        setModal(true);
        setTitle(DynaMOAD.getMSRString("WRD_0061", "Check-In", 0));
        setDefaultCloseOperation(2);
        addWindowListener(this);
        buttonPanel.setLayout(new FlowLayout(2, 5, 5));
        buttonPanel.setBackground(UIManagement.panelBackGround);
        processButton.setActionCommand("checkIn");
        processButton.setText(DynaMOAD.getMSRString("WRD_0061", "Check-In", 0));
        processButton.addActionListener(this);
        buttonPanel.add(processButton);
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = 2;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        getContentPane().add(buttonPanel, gridBagConstraints1);
        fieldPanel.setLayout(new GridBagLayout());
        fieldPanel.setPreferredSize(new Dimension(340, 50));
        fieldPanel.setMinimumSize(new Dimension(340, 50));
        fieldPanel.setBackground(UIManagement.panelBackGround);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.weightx = 0.20000000000000001D;
        gridBagConstraints2.weighty = 1.0D;
        gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
        objectLabel.setText(DynaMOAD.getMSRString("WRD_0057", "File", 3));
        fieldPanel.add(objectLabel, gridBagConstraints2);
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = 2;
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.weightx = 1.0D;
        gridBagConstraints2.weighty = 1.0D;
        objectTextField.setEditable(false);
        objectTextField.setBorder(UIManagement.borderTextBoxNotEditable);
        fieldPanel.add(objectTextField, gridBagConstraints2);
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.weightx = 0.20000000000000001D;
        gridBagConstraints2.weighty = 1.0D;
        gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
        filePathLabel.setText(DynaMOAD.getMSRString("WRD_0068", "File path", 3));
        fieldPanel.add(filePathLabel, gridBagConstraints2);
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.weightx = 1.0D;
        gridBagConstraints2.weighty = 1.0D;
        filePathTextField.setBorder(UIManagement.borderTextBoxEditable);
        fieldPanel.add(filePathTextField, gridBagConstraints2);
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.fill = 0;
        gridBagConstraints2.weightx = 0.0D;
        gridBagConstraints2.weighty = 0.0D;
        optionButton.setMargin(new Insets(0, 0, 0, 0));
        optionButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        optionButton.addMouseListener(this);
        fieldPanel.add(optionButton, gridBagConstraints2);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = 2;
        gridBagConstraints1.ipady = 2;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        getContentPane().add(fieldPanel, gridBagConstraints1);
        optionPanel.setLayout(new FlowLayout(1, 5, 0));
        optionPanel.setPreferredSize(new Dimension(320, 25));
        optionPanel.setMinimumSize(new Dimension(320, 25));
        optionPanel.setBackground(UIManagement.panelBackGround);
        optionPanel.setForeground(UIManagement.panelBackGround);
        uploadCheckBox.setSelected(true);
        uploadCheckBox.setBackground(UIManagement.panelBackGround);
        uploadCheckBox.setActionCommand("upload");
        uploadCheckBox.setText(DynaMOAD.getMSRString("WRD_0071", "Upload", 3));
        optionPanel.add(uploadCheckBox);
        checkInCheckBox.setSelected(true);
        checkInCheckBox.setBackground(UIManagement.panelBackGround);
        checkInCheckBox.setActionCommand("checkin");
        checkInCheckBox.setText(DynaMOAD.getMSRString("WRD_0072", "Version Up", 3));
        optionPanel.add(checkInCheckBox);
        versionUpCheckBox.setSelected(true);
        versionUpCheckBox.setBackground(UIManagement.panelBackGround);
        versionUpCheckBox.setActionCommand("versionUp");
        versionUpCheckBox.setText(DynaMOAD.getMSRString("WRD_0068", "Version Up", 3));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.ipady = 2;
        gridBagConstraints1.anchor = 18;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        getContentPane().add(optionPanel, gridBagConstraints1);
        optionToggleButton.setActionCommand("optionToggle");
        optionToggleButton.setText("...");
        optionToggleButton.setMargin(new Insets(0, 0, 0, 0));
        optionToggleButton.addItemListener(this);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.ipady = 2;
        gridBagConstraints1.anchor = 12;
        gridBagConstraints1.weightx = 1.0D;
        gridBagConstraints1.weighty = 1.0D;
        getContentPane().setBackground(UIManagement.panelBackGround);
        getContentPane().add(optionToggleButton, gridBagConstraints1);
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    private void filePathTextFieldMouseClicked(MouseEvent evt)
    {
        if(SwingUtilities.isLeftMouseButton(evt) && !evt.isControlDown())
        {
            String defaultFolder = null;
            try
            {
                defaultFolder = DynaMOAD.wks.getPrivateDefaultFolder(LogIn.userID);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            if(fileChooser == null)
            {
                if(Utils.isNullString(defaultFolder))
                    fileChooser = new JFileChooser(System.getProperty("user.home"));
                else
                    fileChooser = new JFileChooser(defaultFolder);
                softwareFilter = new SoftWareFilter(extensionStr);
            }
            fileChooser.addChoosableFileFilter(softwareFilter);
            if(preselectedFile != null)
                fileChooser.setSelectedFile(preselectedFile);
            if(fileChooser.showOpenDialog(evt.getComponent()) == 0)
            {
                selectedFile = fileChooser.getSelectedFile();
                if(selectedFile == null || selectedFile.isDirectory())
                {
                    selectedFile = null;
                    filePathTextField.setText("");
                }
                if(selectedFile != null && !selectedFile.isFile())
                {
                    selectedFile = null;
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0101", "Please select a file.", 0), DynaMOAD.getMSRString("WRD_0004", "Information", 0), 0);
                    return;
                }
                try
                {
                    filePathTextField.setText(selectedFile.getCanonicalPath());
                }
                catch(IOException ioe)
                {
                    System.err.println(ioe);
                }
            }
        }
    }

    private void optionToggleButtonItemStateChanged(ItemEvent evt)
    {
        switch(evt.getStateChange())
        {
        case 1: // '\001'
            optionPanel.setVisible(true);
            break;

        case 2: // '\002'
            optionPanel.setVisible(false);
            break;
        }
    }

    private void processButtonActionPerformed(ActionEvent evt)
    {
        processCheckIn();
    }

    private void closeButtonActionPerformed(ActionEvent evt)
    {
        closeDialog(null);
    }

    private void closeDialog(WindowEvent evt)
    {
        if(fileChooser != null && softwareFilter != null)
        {
            fileChooser.removeChoosableFileFilter(softwareFilter);
            fileChooser = null;
        }
        setVisible(false);
        processButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        optionButton.removeActionListener(this);
        optionToggleButton.removeItemListener(this);
        dispose();
    }

    private void processCheckIn()
    {
        try
        {
            String tempString = filePathTextField.getText();
            if(Utils.isNullString(tempString))
            {
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0101", "Please select a file.", 0), DynaMOAD.getMSRString("WRD_0004", "information", 0), 1);
                return;
            }
            File tempFile = new File(tempString);
            if(tempFile == null || tempFile.isDirectory())
            {
                filePathTextField.setText("");
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0101", "Please select a file.", 0), DynaMOAD.getMSRString("WRD_0004", "information", 0), 1);
                return;
            }
            if(tempFile != null && !tempFile.isFile())
            {
                filePathTextField.setText("");
                JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0101", "Please select a file.", 0), DynaMOAD.getMSRString("WRD_0004", "Information", 0), 0);
                return;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(smMode == 0)
        {
            if(uploadCheckBox.isSelected())
            {
                String downLoadPath = "";
                if(!Utils.isNullString((String)fileInfo.get("md$path")) && !Utils.isNullString(filePathTextField.getText()))
                {
                    downLoadPath = filePathTextField.getText();
                    fileInfo.put("md$description", downLoadPath);
                    File downLoadFile = new File(downLoadPath);
                    long fileSize = downLoadFile.length();
                    if(ftd == null)
                        ftd = new FileTransferDialog(parentFrame, false);
                    ftd.setMaximumSize((new Long(fileSize)).intValue());
                    UIUtils.setLocationRelativeTo(ftd, this);
                    ftd.setVisible(true);
                    FileCallBack fileDown = new FileCallBack();
                    upload((String)fileInfo.get("md$path"), filePathTextField.getText(), fileDown);
                }
            } else
            {
                processCheckIn2(fileInfo);
                if(parentFrame instanceof UIGeneration)
                    ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
                closeDialog(null);
            }
        } else
        if(smMode == 1)
        {
            int maximumSize = 0;
            long fileSize = 0L;
            FileCallBack fileDown = new FileCallBack();
            ArrayList serverPathList = new ArrayList();
            ArrayList clientPathList = new ArrayList();
            HashMap fileInfoMap = null;
            if(uploadCheckBox.isSelected())
            {
                for(int i = 0; i < fileInfoList.size(); i++)
                {
                    fileInfoMap = (HashMap)fileInfoList.get(i);
                    String downLoadPath = filePathTextField.getText();
                    File selectedFile = new File(downLoadPath);
                    File downLoadFile = new File((String)fileInfoMap.get("md$description"));
                    workingPath = selectedFile.getParent() + fileSeperator + downLoadFile.getName();
                    if(workingPath != null)
                    {
                        File filePath = new File(workingPath);
                        try
                        {
                            downLoadPath = filePath.getCanonicalPath();
                        }
                        catch(IOException ioe)
                        {
                            System.err.println(ioe);
                        }
                    } else
                    {
                        downLoadPath = "";
                    }
                    if(!Utils.isNullString((String)fileInfoMap.get("md$path")) && !Utils.isNullString(filePathTextField.getText()))
                    {
                        fileInfoMap.put("md$description", downLoadPath);
                        fileSize = downLoadFile.length();
                        maximumSize += (new Long(fileSize)).intValue();
                        serverPathList.add((String)fileInfoMap.get("md$path"));
                        clientPathList.add(downLoadPath);
                    }
                }

                if(ftd == null)
                    ftd = new FileTransferDialog(parentFrame, false);
                UIUtils.setLocationRelativeTo(ftd, this);
                ftd.setVisible(true);
                ftd.setMaximumSize(maximumSize);
                upload(fileDown);
            } else
            {
                for(int i = 0; i < fileInfoList.size(); i++)
                {
                    processCheckIn2((HashMap)fileInfoList.get(i));
                    if(parentFrame instanceof UIGeneration)
                        ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
                }

                closeDialog(null);
            }
        }
    }

    public void processCheckIn2(HashMap fileInfoMap)
    {
        try
        {
            if(checkInCheckBox.isSelected() && uploadCheckBox.isSelected())
                fileInfoMap.put("do.versionup", Boolean.TRUE);
            Object returnObject = Utils.executeScriptFile(dos.getEventName(dos.getClassOuid((String)fileInfoMap.get("md$ouid")), "file.checkin.before"), dss, fileInfoMap);
            if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                return;
            String checkInResult = dos.checkinFile((String)fileInfoMap.get("md$ouid"), fileInfoMap);
            Utils.executeScriptFile(dos.getEventName(dos.getClassOuid((String)fileInfoMap.get("md$ouid")), "file.checkin.after"), dss, fileInfoMap);
            if(_session != null && (utils.cadType == 4 || utils.cadType == 5))
            {
                _session.setProperty("dynapdm.cadintegration.drawing", null);
                _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", null);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
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
                try
                {
                    dss.uploadFile(serverPath, clientPath, ftc);
                    processCheckIn2(fileInfo);
                    if(parentFrame instanceof UIGeneration)
                        ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
                }
                catch(IIPRequestException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            public void finished()
            {
                ftd.setVisible(false);
                ftd.dispose();
                closeDialog(null);
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

    public void upload(FileTransferCallback callback)
    {
        _ftc = callback;
        SwingWorker worker = new SwingWorker() {

            public Object construct()
            {
                try
                {
                    for(int i = 0; i < fileInfoList.size(); i++)
                    {
                        fileInfoMap = (HashMap)fileInfoList.get(i);
                        serverPath = (String)fileInfoMap.get("md$path");
                        clientPath = (String)fileInfoMap.get("md$description");
                        dss.uploadFile(serverPath, clientPath, ftc);
                        processCheckIn2(fileInfoMap);
                        if(parentFrame instanceof UIGeneration)
                            ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfoMap.get("md$filetype.description") + " " + (Integer)fileInfoMap.get("md$index"));
                    }

                }
                catch(IIPRequestException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            public void finished()
            {
                ftd.setVisible(false);
                ftd.dispose();
                closeDialog(null);
            }

            HashMap fileInfoMap;
            String serverPath;
            String clientPath;
            FileTransferCallback ftc;

            
            {
                fileInfoMap = null;
                serverPath = null;
                clientPath = null;
                ftc = _ftc;
            }
        };
        worker.start();
    }

    public void setSoftwareOid(String softwareOid)
    {
        this.softwareOid = new String(softwareOid);
    }

    public void setSoftware(ArrayList software)
    {
        if(software != null)
            this.software = software;
    }

    public void setPreselectedFilePath(String preselectedFilePath)
    {
        if(preselectedFilePath != null)
        {
            preselectedFile = new File(preselectedFilePath);
            selectedFile = preselectedFile;
            try
            {
                filePathTextField.setText(preselectedFile.getCanonicalPath());
            }
            catch(IOException ioe)
            {
                System.err.println(ioe);
            }
        } else
        {
            preselectedFile = null;
            filePathTextField.setText("");
        }
    }

    public void setOid(String oid)
    {
        this.oid = new String(oid);
    }

    public void setObjectName(String objectName)
    {
        if(objectName == null)
        {
            return;
        } else
        {
            objectTextField.setText(new String(objectName));
            return;
        }
    }

    public void setOverlayIndex(int overlayIndex)
    {
        this.overlayIndex = overlayIndex;
    }

    public boolean getResult()
    {
        return result;
    }

    public void setOption(int option, boolean value)
    {
        switch(option)
        {
        case 1: // '\001'
            uploadCheckBox.setSelected(value);
            break;

        case 2: // '\002'
            checkInCheckBox.setSelected(value);
            break;

        case 4: // '\004'
            versionUpCheckBox.setSelected(value);
            break;
        }
    }

    public void setFilePathVisible(boolean mode)
    {
        filePathTextField.setVisible(mode);
    }

    public void setOptionEnabled(int option, boolean mode)
    {
        switch(option)
        {
        case 1: // '\001'
            uploadCheckBox.setEnabled(mode);
            break;

        case 2: // '\002'
            checkInCheckBox.setEnabled(mode);
            break;

        case 4: // '\004'
            versionUpCheckBox.setEnabled(mode);
            break;
        }
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command == null)
            closeButtonActionPerformed(actionEvent);
        else
        if(command.equals("checkIn"))
            processButtonActionPerformed(actionEvent);
        else
        if(command.equals("close"))
            closeButtonActionPerformed(actionEvent);
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        closeDialog(windowEvent);
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void itemStateChanged(ItemEvent itemEvent)
    {
        optionToggleButtonItemStateChanged(itemEvent);
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {
        if(mouseEvent.getSource().equals(optionButton))
            filePathTextFieldMouseClicked(mouseEvent);
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private JPanel buttonPanel;
    private JButton processButton;
    private JButton optionButton;
    private JButton closeButton;
    private JPanel fieldPanel;
    private JLabel objectLabel;
    private JLabel filePathLabel;
    private JTextField objectTextField;
    private JTextField filePathTextField;
    private JPanel optionPanel;
    private JCheckBox uploadCheckBox;
    private JCheckBox checkInCheckBox;
    private JCheckBox versionUpCheckBox;
    private JToggleButton optionToggleButton;
    public static final int UPLOAD = 1;
    public static final int CHECKIN = 2;
    public static final int VERSION_UP = 4;
    private JFileChooser fileChooser;
    private String softwareOid;
    private SoftWareFilter softwareFilter;
    private ArrayList software;
    private File preselectedFile;
    private File selectedFile;
    private boolean result;
    private String oid;
    private int overlayIndex;
    private HashMap fileInfo;
    private ArrayList fileInfoList;
    private DOS dos;
    private DSS dss;
    private UIManagement newUI;
    private Frame parentFrame;
    private FileTransferDialog ftd;
    private String _serverPath;
    private String _clientPath;
    private FileTransferCallback _ftc;
    private String workingDirectory;
    private String workingPath;
    private String fileNameStr;
    private String extensionStr;
    private String fileSeperator;
    private CADIntegrationUtils utils;
    private Session _session;
    private int smMode;









}