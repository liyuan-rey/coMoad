// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CheckOut.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.WKS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.SoftWareFilter;
import dyna.uic.SwingWorker;
import dyna.uic.UIUtils;
import dyna.util.CADIntegrationUtils;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, LogIn, UIManagement, FileTransferDialog, 
//            UIGeneration

public class CheckOut extends JDialog
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

    private class FileTypeSelectionDialog extends JDialog
        implements ActionListener
    {

        private void init()
        {
            cp = getContentPane();
            cp.setLayout(new BorderLayout());
            bottomBar = new JPanel();
            bottomBar.setLayout(new BoxLayout(bottomBar, 0));
            bottomBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            bottomBar.add(Box.createHorizontalGlue());
            selectButton = new JButton(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
            selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
            selectButton.setActionCommand("selectButton");
            selectButton.addActionListener(this);
            bottomBar.add(selectButton);
            bottomBar.add(Box.createHorizontalStrut(5));
            cancelButton = new JButton(DynaMOAD.getMSRString("WRD_0012", "Cancel", 3));
            cancelButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
            cancelButton.setActionCommand("cancelButton");
            cancelButton.addActionListener(this);
            bottomBar.add(cancelButton);
            cp.add(bottomBar, "South");
            centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, 1));
            centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
            tempList = new ArrayList();
            HashMap filetype = null;
            Iterator listKey;
            for(listKey = selectedList.iterator(); listKey.hasNext();)
            {
                filetype = (HashMap)listKey.next();
                if(filetype != null)
                {
                    toggleButton = new JCheckBox("(*." + (String)filetype.get("extension") + ") " + (String)filetype.get("description"));
                    toggleButton.setActionCommand((String)filetype.get("filetype.id"));
                    toggleButton.setSelected(true);
                    centerPanel.add(toggleButton);
                    tempList.add(toggleButton);
                    toggleButton = null;
                }
            }

            listKey = null;
            cp.add(centerPanel, "Center");
            setTitle(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
            pack();
            UIUtils.setLocationRelativeTo(this, null);
        }

        public void actionPerformed(ActionEvent e)
        {
            String cmd = e.getActionCommand();
            if(cmd == null)
                setVisible(false);
            else
            if("selectButton".equals(cmd))
            {
                JCheckBox toggleButton = null;
                Iterator listKey = tempList.iterator();
                for(Iterator listKey2 = selectedList.iterator(); listKey.hasNext() && listKey2.hasNext();)
                {
                    toggleButton = (JCheckBox)listKey.next();
                    listKey2.next();
                    if(!toggleButton.isSelected())
                    {
                        listKey.remove();
                        listKey2.remove();
                    }
                }

                listKey = null;
                setVisible(false);
            } else
            if("cancelButton".equals(cmd))
            {
                if(selectedList != null)
                    selectedList.clear();
                setVisible(false);
            }
        }

        public ArrayList getSelectedList()
        {
            if(isVisible())
            {
                return null;
            } else
            {
                dispose();
                return selectedList;
            }
        }

        private ArrayList selectedList;
        private ArrayList tempList;
        private Container cp;
        private JPanel bottomBar;
        private JButton selectButton;
        private JButton cancelButton;
        private JPanel centerPanel;
        private JCheckBox toggleButton;

        public FileTypeSelectionDialog(Frame frame, boolean modal, ArrayList dstArrayList)
        {
            super(frame, modal);
            selectedList = null;
            tempList = null;
            cp = null;
            bottomBar = null;
            selectButton = null;
            cancelButton = null;
            centerPanel = null;
            toggleButton = null;
            selectedList = dstArrayList;
            if(Utils.isNullArrayList(selectedList))
            {
                selectedList = null;
                return;
            } else
            {
                init();
                return;
            }
        }
    }

    private class ConversionThread extends Thread
    {

        public void run()
        {
            String folderString = _folderString;
            ArrayList inputFiles = _inputFiles;
            String fileExtension = _fileExtension;
            String fileConverter = _fileConverter;
            boolean isConversion = false;
            try
            {
                isConversion = dss.doFileConversion(folderString, inputFiles, fileExtension, fileConverter);
                _isConversion = isConversion;
                if(!isConversion)
                {
                    conversion.destroy();
                    throw new IIPRequestException("File Conversion Failure.");
                }
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        }

        ConversionThread()
        {
        }
    }

    private class CheckErrFileThread extends Thread
    {

        public synchronized void run()
        {
            String folderString = _folderString;
            ArrayList inputFiles = _inputFiles;
            String fileConverter = _fileConverter;
            boolean isConversion = _isConversion;
            try
            {
                while(!_isConversion) 
                    if(dss.remoteCheckErrFile(folderString, inputFiles, fileConverter))
                    {
                        closeDialog(null);
                        isConversion = true;
                        isConversionErr = true;
                        processReadOnly();
                        break;
                    }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        CheckErrFileThread()
        {
        }
    }


    public CheckOut(Frame parent, boolean modal, Object fileInfo)
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
        _downloadFileList = null;
        _ftc = null;
        _invokeFlag = false;
        workingDirectory = null;
        workingPath = null;
        fileNameStr = null;
        extensionStr = null;
        invokeFile = null;
        readOnlyMode = false;
        _folderString = null;
        _inputFiles = null;
        _fileExtension = null;
        _fileConverter = null;
        _isConversion = false;
        conversion = null;
        checkErrFile = null;
        isConversionErr = false;
        fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
        utils = null;
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
            String defaultPath = DynaMOAD.wks.getPrivateDefaultFolder(LogIn.userID);
            utils = new CADIntegrationUtils(classOuid, dos);
            if(utils.cadType == 0 || utils.cadType == 3)
            {
                workingDirectory = "c:" + fileSeperator + "CADData" + fileSeperator + (String)this.fileInfo.get("md$ouid");
                workingPath = workingDirectory + fileSeperator + fileNameStr;
            } else
            if(!Utils.isNullString(defaultPath))
            {
                workingPath = defaultPath + fileSeperator + fileNameStr;
            } else
            {
                workingDirectory = System.getProperty("user.home");
                workingPath = workingDirectory + fileSeperator + fileNameStr;
            }
            setObjectName((String)this.fileInfo.get("md$filetype.description") + " " + (Integer)this.fileInfo.get("md$index"));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        setPreselectedFilePath(workingPath);
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
        downloadCheckBox = new JCheckBox();
        checkOutCheckBox = new JCheckBox();
        invokeCheckBox = new JCheckBox();
        optionToggleButton = new JToggleButton();
        getContentPane().setLayout(new GridBagLayout());
        setModal(true);
        setTitle(DynaMOAD.getMSRString("WRD_0060", "Check-Out", 0));
        setDefaultCloseOperation(2);
        addWindowListener(this);
        buttonPanel.setLayout(new FlowLayout(2, 5, 5));
        buttonPanel.setBackground(UIManagement.panelBackGround);
        processButton.setActionCommand("checkOut");
        processButton.setText(DynaMOAD.getMSRString("WRD_0060", "Check-Out", 0));
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
        downloadCheckBox.setSelected(true);
        downloadCheckBox.setBackground(UIManagement.panelBackGround);
        downloadCheckBox.setActionCommand("download");
        downloadCheckBox.setText(DynaMOAD.getMSRString("WRD_0069", "Download", 3));
        optionPanel.add(downloadCheckBox);
        checkOutCheckBox.setSelected(true);
        checkOutCheckBox.setBackground(UIManagement.panelBackGround);
        checkOutCheckBox.setActionCommand("checkout");
        checkOutCheckBox.setText(DynaMOAD.getMSRString("WRD_0060", "Check-out", 3));
        optionPanel.add(checkOutCheckBox);
        invokeCheckBox.setSelected(true);
        invokeCheckBox.setBackground(UIManagement.panelBackGround);
        invokeCheckBox.setActionCommand("invoke");
        invokeCheckBox.setText(DynaMOAD.getMSRString("WRD_0070", "Invoke", 3));
        optionPanel.add(invokeCheckBox);
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
                DOSChangeable softwareInfo = new DOSChangeable();
                softwareInfo.put("EXTENSION", extensionStr);
                softwareInfo.put("NAME", (String)fileInfo.get("md$filetype.id") + " (*." + extensionStr + ")");
                softwareFilter = new SoftWareFilter(softwareInfo);
            }
            fileChooser.addChoosableFileFilter(softwareFilter);
            if(preselectedFile != null)
                fileChooser.setSelectedFile(preselectedFile);
            if(fileChooser.showSaveDialog(evt.getComponent()) == 0)
            {
                selectedFile = fileChooser.getSelectedFile();
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
        if(checkOutCheckBox.isSelected() && (utils.cadType == 0 || utils.cadType == 3))
            processCheckOutForCADIntegration();
        else
        if(readOnlyMode)
            processReadOnly();
        else
            processCheckOut();
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

    private void processReadOnly()
    {
        if(downloadCheckBox.isSelected() && fileInfo != null)
            try
            {
                ArrayList dstArrayList = null;
                String downloadPath = "";
                String fileExtension = null;
                String fileConverter = null;
                String filetypeIdSource = (String)fileInfo.get("md$filetype.id");
                String filetypeIdDestination = null;
                String serverPath = null;
                String clientPath = null;
                String folderString = null;
                boolean doConversion = false;
                if(Utils.isNullString(filetypeIdSource))
                    return;
                HashMap filetypeSource = dss.getFileType(filetypeIdSource);
                HashMap filetypeDestination = null;
                if(filetypeSource == null)
                    return;
                java.util.List destinationFileTypeList = Utils.tokenizeMessage((String)filetypeSource.get("converted.filetypeid"), '^');
                if(destinationFileTypeList == null || destinationFileTypeList.isEmpty())
                {
                    processCheckOut();
                    return;
                }
                if(!Utils.isNullString((String)fileInfo.get("md$path")) && !Utils.isNullString(filePathTextField.getText()))
                {
                    downloadPath = filePathTextField.getText();
                    File tempFile = new File(downloadPath);
                    folderString = tempFile.getParent();
                    if(!Utils.isNullString(folderString))
                        (new File(folderString)).mkdirs();
                    tempFile = null;
                    dstArrayList = new ArrayList();
                    Iterator fileKey;
                    for(fileKey = destinationFileTypeList.iterator(); fileKey.hasNext();)
                    {
                        filetypeIdDestination = (String)fileKey.next();
                        filetypeDestination = dss.getFileType(filetypeIdDestination);
                        dstArrayList.add(filetypeDestination);
                        filetypeDestination = null;
                    }

                    fileKey = null;
                    destinationFileTypeList = null;
                    if(dstArrayList.size() > 1 && !DynaMOAD.startFromCAD && !isConversionErr)
                    {
                        FileTypeSelectionDialog ftsd = new FileTypeSelectionDialog(null, true, dstArrayList);
                        ftsd.setVisible(true);
                        dstArrayList = ftsd.getSelectedList();
                        ftsd = null;
                    }
                    if(Utils.isNullArrayList(dstArrayList))
                        return;
                    if(DynaMOAD.startFromCAD || isConversionErr)
                    {
                        invokeFile = new File(downloadPath + "." + fileExtension);
                        if(invokeFile.exists() && (!invokeFile.canWrite() || !invokeFile.delete()))
                            throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify a file.", 0));
                        serverPath = (String)fileInfo.get("md$path");
                        long fileSize = dss.getFileSize(serverPath);
                        long lmd = dss.getLastModifiedDate(serverPath);
                        fileSize = dss.getFileSize(serverPath);
                        if(ftd == null)
                            ftd = new FileTransferDialog(parentFrame, false);
                        ftd.setMaximumSize((new Long(fileSize)).intValue());
                        UIUtils.setLocationRelativeTo(ftd, this);
                        ftd.setVisible(true);
                        FileCallBack fileDown = new FileCallBack();
                        download(serverPath, downloadPath, fileDown, true);
                        filetypeDestination = null;
                    } else
                    {
                        for(fileKey = dstArrayList.iterator(); fileKey.hasNext();)
                        {
                            filetypeDestination = (HashMap)fileKey.next();
                            fileConverter = (String)filetypeSource.get("converter");
                            fileExtension = (String)filetypeDestination.get("extension");
                            invokeFile = new File(downloadPath + "." + fileExtension);
                            if(invokeFile.exists() && (!invokeFile.canWrite() || !invokeFile.delete()))
                                throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify a file.", 0));
                            doConversion = false;
                            serverPath = (String)fileInfo.get("md$path");
                            long fileSize = dss.getFileSize(serverPath);
                            long lmd = dss.getLastModifiedDate(serverPath);
                            if(dss.exists(serverPath + "." + fileExtension))
                            {
                                if(lmd >= dss.getLastModifiedDate(serverPath + "." + fileExtension))
                                    doConversion = true;
                            } else
                            {
                                doConversion = true;
                            }
                            if(doConversion && !DynaMOAD.startFromCAD)
                            {
                                tempFile = new File(serverPath);
                                folderString = tempFile.getParent();
                                ArrayList inputFiles = new ArrayList();
                                inputFiles.add(tempFile.getName());
                                tempFile = null;
                                _inputFiles = inputFiles;
                                _fileConverter = fileConverter;
                                _folderString = folderString;
                                checkErrFile = new CheckErrFileThread();
                                boolean isConversion = false;
                                checkErrFile.setPriority(10);
                                checkErrFile.start();
                                isConversion = dss.doFileConversion(folderString, inputFiles, fileExtension, fileConverter);
                                _isConversion = isConversion;
                                if(isConversion);
                            }
                            if(!isConversionErr)
                            {
                                fileSize = dss.getFileSize(serverPath);
                                if(ftd == null)
                                    ftd = new FileTransferDialog(parentFrame, false);
                                ftd.setMaximumSize((new Long(fileSize)).intValue());
                                UIUtils.setLocationRelativeTo(ftd, this);
                                ftd.setVisible(true);
                                FileCallBack fileDown = new FileCallBack();
                                download(serverPath + "." + fileExtension, downloadPath + "." + fileExtension, fileDown, true);
                                filetypeDestination = null;
                            }
                        }

                    }
                    fileKey = null;
                }
            }
            catch(Exception ie)
            {
                ie.printStackTrace();
            }
    }

    private void processCheckOut()
    {
        try
        {
            if(_session != null && (utils.cadType == 4 || utils.cadType == 5))
            {
                HashMap checkOutFileInfoMap = (HashMap)_session.getProperty("dynapdm.cadintegration.checkoutfileinfo");
                if(checkOutFileInfoMap != null)
                {
                    String checkOutFileName = (String)checkOutFileInfoMap.get("md$description");
                    File checkOutFile = new File(checkOutFileName);
                    int res = JOptionPane.showConfirmDialog(this, "\uCCB4\uD06C-\uC778 \uB418\uC9C0 \uC54A\uC740 \uD30C\uC77C\uC774 \uC874\uC7AC\uD569\uB2C8\uB2E4. [" + checkOutFile.getName() + "] \n" + "\uCCB4\uD06C-\uC544\uC6C3 \uC791\uC5C5\uC744 \uACC4\uC18D \uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0), 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")));
                    if(res != 1)
                        return;
                }
            }
            if(smMode == 0)
            {
                if(downloadCheckBox.isSelected())
                {
                    String downLoadPath = "";
                    if(!Utils.isNullString((String)fileInfo.get("md$path")) && !Utils.isNullString(filePathTextField.getText()))
                    {
                        downLoadPath = filePathTextField.getText();
                        fileInfo.put("md$description", downLoadPath);
                        invokeFile = new File(downLoadPath);
                        if(invokeFile.exists())
                            if(invokeFile.canWrite())
                            {
                                int confirm = JOptionPane.showConfirmDialog(this, downLoadPath + "\n" + DynaMOAD.getMSRString("QST_0013", "File exsits. Overwrite?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0), 0);
                                if(confirm == 0)
                                    invokeFile.delete();
                                else
                                    return;
                            } else
                            {
                                throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify a file.", 0));
                            }
                        java.util.List tmpList2 = Utils.tokenizeMessage(downLoadPath, '\\');
                        String folderStr = "";
                        for(int i = 0; i < tmpList2.size(); i++)
                        {
                            if(i == tmpList2.size() - 2)
                                folderStr = folderStr + (String)tmpList2.get(i);
                            else
                            if(i != tmpList2.size() - 1)
                                folderStr = folderStr + (String)tmpList2.get(i) + "\\";
                            if(i != 0 && i != tmpList2.size() - 1)
                                (new File(folderStr)).mkdir();
                        }

                        long fileSize = dss.getFileSize((String)fileInfo.get("md$path"));
                        if(ftd == null)
                            ftd = new FileTransferDialog(parentFrame, false);
                        ftd.setMaximumSize((new Long(fileSize)).intValue());
                        UIUtils.setLocationRelativeTo(ftd, this);
                        ftd.setVisible(true);
                        FileCallBack fileDown = new FileCallBack();
                        download((String)fileInfo.get("md$path"), downLoadPath, fileDown, true);
                    }
                } else
                {
                    processCheckOut2(fileInfo);
                    setSessionInformation(fileInfo);
                    if(parentFrame instanceof UIGeneration)
                        ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
                    closeDialog(null);
                }
            } else
            if(smMode == 1)
            {
                boolean yesToAll = false;
                int maximunSize = 0;
                long fileSize = 0L;
                FileCallBack fileDown = new FileCallBack();
                HashMap fileInfoMap = null;
                ArrayList downloadFileList = new ArrayList();
                if(downloadCheckBox.isSelected())
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
                        if(Utils.isNullString((String)fileInfoMap.get("md$path")) || Utils.isNullString(downLoadPath))
                            continue;
                        fileInfoMap.put("md$description", downLoadPath);
                        invokeFile = new File(downLoadPath);
                        if(invokeFile.exists())
                            if(invokeFile.canWrite())
                            {
                                if(!yesToAll)
                                {
                                    String yes = DynaMOAD.getMSRString("WRD_0162", "Yes", 3);
                                    String yestoall = DynaMOAD.getMSRString("WRD_0163", "Yes To All", 3);
                                    String no = DynaMOAD.getMSRString("WRD_0164", "No", 3);
                                    String cancel = DynaMOAD.getMSRString("WRD_0165", "Cancel", 3);
                                    Object options[] = {
                                        yes, yestoall, no, cancel
                                    };
                                    int confirm = JOptionPane.showOptionDialog(this, downLoadPath + "\n" + DynaMOAD.getMSRString("QST_0013", "File exsits. Overwrite?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0), 1, 3, null, options, options[0]);
                                    if(confirm == 0)
                                        invokeFile.delete();
                                    else
                                    if(confirm == 1)
                                    {
                                        invokeFile.delete();
                                        yesToAll = true;
                                    } else
                                    {
                                        if(confirm == 2)
                                            continue;
                                        if(confirm == 3)
                                            break;
                                    }
                                } else
                                {
                                    invokeFile.delete();
                                }
                            } else
                            {
                                throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify a file.", 0));
                            }
                        java.util.List tmpList2 = Utils.tokenizeMessage(downLoadPath, '\\');
                        String folderStr = "";
                        for(int j = 0; j < tmpList2.size(); j++)
                        {
                            if(j == tmpList2.size() - 2)
                                folderStr = folderStr + (String)tmpList2.get(j);
                            else
                            if(j != tmpList2.size() - 1)
                                folderStr = folderStr + (String)tmpList2.get(j) + "\\";
                            if(j != 0 && j != tmpList2.size() - 1)
                                (new File(folderStr)).mkdir();
                        }

                        fileSize = dss.getFileSize((String)fileInfoMap.get("md$path"));
                        maximunSize += (new Long(fileSize)).intValue();
                        downloadFileList.add(fileInfoMap);
                    }

                    if(ftd == null)
                        ftd = new FileTransferDialog(parentFrame, false);
                    UIUtils.setLocationRelativeTo(ftd, this);
                    ftd.setVisible(true);
                    ftd.setMaximumSize(maximunSize);
                    download(downloadFileList, fileDown, false);
                } else
                {
                    for(int i = 0; i < fileInfoList.size(); i++)
                    {
                        processCheckOut2((HashMap)fileInfoList.get(i));
                        if(parentFrame instanceof UIGeneration)
                            ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
                    }

                    closeDialog(null);
                }
            }
        }
        catch(Exception ie)
        {
            System.err.println(ie);
        }
    }

    private void processCheckOut2(HashMap fileInfoMap)
    {
        if(checkOutCheckBox.isSelected())
            try
            {
                Object returnObject = Utils.executeScriptFile(dos.getEventName(dos.getClassOuid((String)fileInfoMap.get("md$ouid")), "file.checkout.before"), dss, fileInfoMap);
                if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                    return;
                String checkOutResult = dos.checkoutFile((String)fileInfoMap.get("md$ouid"), fileInfoMap);
                Utils.executeScriptFile(dos.getEventName(dos.getClassOuid((String)fileInfoMap.get("md$ouid")), "file.checkout.after"), dss, fileInfoMap);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
    }

    private void processCheckOut4ReadOnly()
    {
        try
        {
            String checkOutFilePath = filePathTextField.getText();
            if(_session != null && (utils.cadType == 4 || utils.cadType == 5))
            {
                File checkOutFile = new File(checkOutFilePath);
                String extensionStr = dss.getExtension((String)fileInfo.get("md$filetype.id"));
                String fileName = checkOutFile.getName();
                _session.setProperty("dynapdm.cadintegration.drawing", fileName.substring(0, fileName.lastIndexOf(extensionStr) - 1));
                _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", fileInfo.clone());
                if(checkOutFile != null)
                    try
                    {
                        _session.enqueueQueuedProperty("dynapdm.cadintegration.action", "1 1 1 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 \"" + checkOutFile.getCanonicalPath() + "\"");
                    }
                    catch(IOException ie)
                    {
                        System.err.println(ie);
                    }
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        if(parentFrame instanceof UIGeneration)
            ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
    }

    private void processCheckOutForCADIntegration()
    {
        try
        {
            ArrayList linkedList = listAllLinkForCADIntegration((String)fileInfo.get("md$ouid"));
            fileInfoList = new ArrayList();
            if(linkedList != null)
            {
                for(int i = 0; i < linkedList.size(); i++)
                {
                    ArrayList rows = dos.listFile((String)linkedList.get(i));
                    if(rows != null)
                    {
                        for(int j = 0; j < rows.size(); j++)
                            fileInfoList.add((HashMap)rows.get(j));

                    }
                }

            }
            if(fileInfoList.size() > 0)
            {
                boolean yesToAll = false;
                int maximunSize = 0;
                long fileSize = 0L;
                FileCallBack fileDown = new FileCallBack();
                HashMap fileInfoMap = null;
                ArrayList downloadFileList = new ArrayList();
                if(downloadCheckBox.isSelected())
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
                        if(Utils.isNullString((String)fileInfoMap.get("md$path")) || Utils.isNullString(downLoadPath))
                            continue;
                        fileInfoMap.put("md$description", downLoadPath);
                        invokeFile = new File(downLoadPath);
                        if(invokeFile.exists())
                            if(invokeFile.canWrite())
                            {
                                if(!yesToAll)
                                {
                                    String yes = DynaMOAD.getMSRString("WRD_0162", "Yes", 3);
                                    String yestoall = DynaMOAD.getMSRString("WRD_0163", "Yes To All", 3);
                                    String no = DynaMOAD.getMSRString("WRD_0164", "No", 3);
                                    String cancel = DynaMOAD.getMSRString("WRD_0165", "Cancel", 3);
                                    Object options[] = {
                                        yes, yestoall, no, cancel
                                    };
                                    int confirm = JOptionPane.showOptionDialog(this, downLoadPath + "\n" + DynaMOAD.getMSRString("QST_0013", "File exsits. Overwrite?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0), 1, 3, null, options, options[0]);
                                    if(confirm == 0)
                                        invokeFile.delete();
                                    else
                                    if(confirm == 1)
                                    {
                                        invokeFile.delete();
                                        yesToAll = true;
                                    } else
                                    {
                                        if(confirm == 2)
                                        {
                                            if(i == 0)
                                                break;
                                            continue;
                                        }
                                        if(confirm == 3)
                                            break;
                                    }
                                } else
                                {
                                    invokeFile.delete();
                                }
                            } else
                            {
                                throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify a file.", 0));
                            }
                        java.util.List tmpList2 = Utils.tokenizeMessage(downLoadPath, '\\');
                        String folderStr = "";
                        for(int j = 0; j < tmpList2.size(); j++)
                        {
                            if(j == tmpList2.size() - 2)
                                folderStr = folderStr + (String)tmpList2.get(j);
                            else
                            if(j != tmpList2.size() - 1)
                                folderStr = folderStr + (String)tmpList2.get(j) + "\\";
                            if(j != 0 && j != tmpList2.size() - 1)
                                (new File(folderStr)).mkdir();
                        }

                        fileSize = dss.getFileSize((String)fileInfoMap.get("md$path"));
                        maximunSize += (new Long(fileSize)).intValue();
                        downloadFileList.add(fileInfoMap);
                    }

                    if(ftd == null)
                        ftd = new FileTransferDialog(parentFrame, false);
                    UIUtils.setLocationRelativeTo(ftd, this);
                    ftd.setVisible(true);
                    ftd.setMaximumSize(maximunSize);
                    download(downloadFileList, fileDown, true);
                } else
                {
                    for(int i = 0; i < fileInfoList.size(); i++)
                    {
                        processCheckOut2((HashMap)fileInfoList.get(i));
                        if(parentFrame instanceof UIGeneration)
                            ((UIGeneration)parentFrame).fileTreeRefresh((String)fileInfo.get("md$filetype.description") + " " + (Integer)fileInfo.get("md$index"));
                    }

                    utils.writeInitFile(2, (String)linkedList.get(0) + ".ini", fileInfoList);
                    closeDialog(null);
                }
            }
        }
        catch(Exception ie)
        {
            System.err.println(ie);
        }
    }

    private ArrayList listAllLinkForCADIntegration(String fileOuid)
    {
        ArrayList linkList = null;
        LinkedList stack = new LinkedList();
        HashMap checkDup = new HashMap();
        ArrayList returnList = new ArrayList();
        returnList.add(fileOuid);
        while(!Utils.isNullString(fileOuid)) 
        {
            try
            {
                linkList = dos.listLinkForCADIntegration(fileOuid);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
            if(linkList == null)
            {
                if(stack.isEmpty())
                    break;
                fileOuid = (String)stack.removeLast();
                returnList.add(fileOuid);
                continue;
            }
            for(Iterator key = linkList.iterator(); key.hasNext();)
            {
                fileOuid = (String)key.next();
                if(checkDup.containsKey(fileOuid))
                {
                    fileOuid = null;
                } else
                {
                    stack.add(fileOuid);
                    checkDup.put(fileOuid, "");
                    fileOuid = null;
                }
            }

            if(stack.isEmpty())
                break;
            fileOuid = (String)stack.removeLast();
            returnList.add(fileOuid);
        }
        stack.clear();
        stack = null;
        checkDup.clear();
        checkDup = null;
        if(returnList == null || returnList.size() == 0)
            returnList = null;
        return returnList;
    }

    public void invokeFile(String clientPath)
    {
        if(invokeCheckBox.isSelected() && filePathTextField.getText() != null)
            if(checkOutCheckBox.isSelected() && utils.cadType == 0 && DynaMOAD.startFromCAD)
            {
                if(!Utils.isNullString(clientPath))
                    try
                    {
                        invokeFile = new File(clientPath);
                        String cmdArray[] = new String[3];
                        cmdArray[0] = System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "swCheckOutMoad.exe";
                        cmdArray[1] = "'fileopen'";
                        cmdArray[2] = "'" + invokeFile.getCanonicalPath() + "'";
                        Process proc = Runtime.getRuntime().exec(cmdArray);
                        proc = null;
                    }
                    catch(IOException re)
                    {
                        System.err.println(re);
                    }
            } else
            if(!checkOutCheckBox.isSelected() || utils.cadType != 4 && utils.cadType != 5 || !DynaMOAD.startFromCAD)
                if(readOnlyMode && (utils.cadType == 4 || utils.cadType == 5) && DynaMOAD.startFromCAD)
                    processCheckOut4ReadOnly();
                else
                if(!Utils.isNullString(clientPath))
                    try
                    {
                        invokeFile = new File(clientPath);
                        String cmdArray[] = new String[2];
                        String osName = System.getProperty("os.name");
                        if(osName.indexOf("98") > -1 || osName.indexOf("ME") > -1)
                        {
                            File newExecFile = new File("exec.bat");
                            newExecFile.createNewFile();
                            FileWriter newFileWriter = new FileWriter(newExecFile.getAbsoluteFile());
                            newFileWriter.write("@start %1");
                            newFileWriter.flush();
                            newFileWriter.close();
                            newFileWriter = null;
                            newExecFile = null;
                            cmdArray[0] = System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "exec.bat";
                        } else
                        if(osName.indexOf("XP") > -1 || osName.indexOf("2000") > -1)
                        {
                            File newExecFile = new File("exec.bat");
                            newExecFile.createNewFile();
                            FileWriter newFileWriter = new FileWriter(newExecFile.getAbsoluteFile());
                            newFileWriter.write("@start \"\" %1");
                            newFileWriter.flush();
                            newFileWriter.close();
                            newFileWriter = null;
                            newExecFile = null;
                            cmdArray[0] = System.getProperty("user.dir") + (System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\") + "exec.bat";
                        } else
                        {
                            cmdArray[0] = "EXPLORER";
                        }
                        if(invokeFile.exists())
                        {
                            cmdArray[1] = invokeFile.getCanonicalPath();
                            Process proc = Runtime.getRuntime().exec(cmdArray);
                            proc = null;
                        }
                    }
                    catch(IOException re)
                    {
                        System.err.println(re);
                    }
    }

    public void download(String serverPath, String clientPath, FileTransferCallback callback, boolean invokeFlag)
    {
        _serverPath = serverPath;
        _clientPath = clientPath;
        _ftc = callback;
        _invokeFlag = invokeFlag;
        SwingWorker worker = new SwingWorker() {

            public synchronized Object construct()
            {
                try
                {
                    dss.downloadFile(serverPath, clientPath, ftc);
                    processCheckOut2(fileInfo);
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
                if(invokeFlag)
                    invokeFile(clientPath);
                closeDialog(null);
            }

            String serverPath;
            String clientPath;
            FileTransferCallback ftc;
            boolean invokeFlag;

            
            {
                serverPath = _serverPath;
                clientPath = _clientPath;
                ftc = _ftc;
                invokeFlag = _invokeFlag;
            }
        };
        worker.start();
    }

    public void download(ArrayList downloadFileList, FileTransferCallback callback, boolean invokeFlag)
    {
        _ftc = callback;
        _invokeFlag = invokeFlag;
        _downloadFileList = downloadFileList;
        SwingWorker worker = new SwingWorker() {

            public synchronized Object construct()
            {
                try
                {
                    for(int i = 0; i < downloadFileList.size(); i++)
                    {
                        fileInfoMap = (HashMap)downloadFileList.get(i);
                        serverPath = (String)fileInfoMap.get("md$path");
                        clientPath = (String)fileInfoMap.get("md$description");
                        dss.downloadFile(serverPath, clientPath, ftc);
                        processCheckOut2(fileInfoMap);
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
                if(invokeFlag)
                {
                    invokeFile((String)((HashMap)downloadFileList.get(0)).get("md$description"));
                    utils.writeInitFile(2, (String)((HashMap)downloadFileList.get(0)).get("md$ouid") + ".ini", downloadFileList);
                }
                closeDialog(null);
            }

            HashMap fileInfoMap;
            String serverPath;
            String clientPath;
            FileTransferCallback ftc;
            boolean invokeFlag;
            ArrayList downloadFileList;

            
            {
                fileInfoMap = null;
                serverPath = null;
                clientPath = null;
                ftc = _ftc;
                invokeFlag = _invokeFlag;
                downloadFileList = _downloadFileList;
            }
        };
        worker.start();
    }

    public void checkConversion(String folderString, ArrayList inputFiles, String fileExtension, String fileConverter)
    {
        _folderString = folderString;
        _inputFiles = inputFiles;
        _fileExtension = fileExtension;
        _fileConverter = fileConverter;
        conversion = new ConversionThread();
        checkErrFile = new CheckErrFileThread();
        conversion.start();
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
            downloadCheckBox.setSelected(value);
            break;

        case 2: // '\002'
            checkOutCheckBox.setSelected(value);
            break;

        case 4: // '\004'
            invokeCheckBox.setSelected(value);
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
            downloadCheckBox.setEnabled(mode);
            break;

        case 2: // '\002'
            checkOutCheckBox.setEnabled(mode);
            break;

        case 4: // '\004'
            invokeCheckBox.setEnabled(mode);
            break;
        }
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command == null)
            closeButtonActionPerformed(actionEvent);
        else
        if(command.equals("checkOut"))
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

    public void setSession(Session session)
    {
        _session = session;
    }

    public void setReadOnlyModel(boolean mode)
    {
        readOnlyMode = mode;
    }

    private void setSessionInformation(HashMap fileInfoMap)
    {
        try
        {
            if(_session != null && (utils.cadType == 4 || utils.cadType == 5))
            {
                String checkOutFilePath = filePathTextField.getText();
                File checkOutFile = new File(checkOutFilePath);
                String extensionStr = dss.getExtension((String)fileInfoMap.get("md$filetype.id"));
                String fileName = checkOutFile.getName();
                _session.setProperty("dynapdm.cadintegration.drawing", fileName.substring(0, fileName.lastIndexOf(extensionStr) - 1));
                _session.setProperty("dynapdm.cadintegration.checkoutfileinfo", fileInfoMap.clone());
                if(checkOutFile != null)
                    try
                    {
                        _session.enqueueQueuedProperty("dynapdm.cadintegration.action", "1 1 1 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 \"" + checkOutFile.getCanonicalPath() + "\"");
                    }
                    catch(IOException ie)
                    {
                        System.err.println(ie);
                    }
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    private JPanel buttonPanel;
    public JButton processButton;
    private JButton optionButton;
    private JButton closeButton;
    private JPanel fieldPanel;
    private JLabel objectLabel;
    private JLabel filePathLabel;
    private JTextField objectTextField;
    private JTextField filePathTextField;
    private JPanel optionPanel;
    public JCheckBox downloadCheckBox;
    public JCheckBox checkOutCheckBox;
    public JCheckBox invokeCheckBox;
    private JToggleButton optionToggleButton;
    public static final int DOWNLOAD = 1;
    public static final int CHECKOUT = 2;
    public static final int INVOKE = 4;
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
    private ArrayList _downloadFileList;
    private FileTransferCallback _ftc;
    private boolean _invokeFlag;
    private String workingDirectory;
    private String workingPath;
    private String fileNameStr;
    private String extensionStr;
    private File invokeFile;
    private boolean readOnlyMode;
    private String _folderString;
    private ArrayList _inputFiles;
    private String _fileExtension;
    private String _fileConverter;
    private boolean _isConversion;
    private ConversionThread conversion;
    private CheckErrFileThread checkErrFile;
    private boolean isConversionErr;
    private String fileSeperator;
    private CADIntegrationUtils utils;
    private Session _session;
    private int smMode;





















}