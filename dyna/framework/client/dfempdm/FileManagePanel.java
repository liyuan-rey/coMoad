/*
 * Created on 2004-10-28
 *
 */
package dyna.framework.client.dfempdm;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;

import dyna.framework.client.CheckIn;
import dyna.framework.client.CheckOut;
import dyna.framework.client.DynaMOAD;
import dyna.framework.client.FileTransferDialog;
import dyna.framework.client.LogIn;
import dyna.framework.client.UIBuilder;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.ACL;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.dss.FileTransferCallback;
import dyna.uic.DynaTheme;
import dyna.uic.FileTreeModel;
import dyna.uic.JTreeTable;
import dyna.uic.SoftWareFilter;
import dyna.uic.SwingWorker;
import dyna.uic.TreeNodeObject;
import dyna.uic.TreeTableModel;
import dyna.uic.UIUtils;
import dyna.util.Session;
import dyna.util.Utils;

/**
 * @author ภ๎ิจ
 *
 */
public class FileManagePanel extends JPanel implements ActionListener {
    private JFrame parent = null;
	private ExtToolBar fileToolbar = null;
	private JButton fileOpenButton = null;
	private JButton fileReadOnlyButton = null;
	private JTreeTable fileTreeTable = null;
	private JScrollPane fileTableScrPane = null;
	
    private DefaultMutableTreeNode fileRootNode;
    private TreeTableModel fileModel = null;
    private TreeSelectionListener fileListener = null;
    private FileTransferDialog ftd = null;
    
    private DOS dos = DynaMOAD.dos;
    private DSS dss = DynaMOAD.dss;
    private ACL acl = DynaMOAD.acl;
    private DOSChangeable getDOSGlobal = null;
    private JPopupMenu filePopupMenu = null;
    private JMenuItem menuFileDetach = null;
    private JMenuItem menuFileCheckIn = null;
    private JMenuItem menuFileCheckOut = null;
    private JMenuItem menuFileCheckOutCancel = null;
    private JMenuItem menuFileReadOnly = null;
    private boolean hasUpdatePermission = false;
    private boolean hasDeletePermission = false;
    private boolean hasLinkPermission = false;
    private boolean hasFileUpdatePermission = false;
    private boolean hasPrintPermission = false;
    private File files[];
    private String instanceOuid = null;
    private String classOuid = null;

    static Session _session = null;

    private String _serverPath;
    private String _clientPath;
    private FileTransferCallback _ftc;

	/**
	 * This method initializes 
	 * 
	 */
	public FileManagePanel(JFrame parent, DOSChangeable dosRouting) {
        super();

        this.parent = parent;
        getDOSGlobal = dosRouting;
        instanceOuid = (String)getDOSGlobal.get("ouid");
        classOuid = (String)getDOSGlobal.getClassOuid();

        refreshPermissionValues();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(320, 360);
        this.add(getToolbar(), java.awt.BorderLayout.NORTH);
        this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);

        createPopup();
	}
	/**
	 * This method initializes fileToolbar	
	 * 	
	 * @return com.jgoodies.swing.ExtToolBar	
	 */    
	private ExtToolBar getToolbar() {
		if (fileToolbar == null) {
			fileToolbar = new ExtToolBar("fileToolBar", HeaderStyle.BOTH);
			fileToolbar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
			fileToolbar.add(getFileOpenButton());
			fileToolbar.add(getFileReadOnlyButton());
		}
		return fileToolbar;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFileOpenButton() {
		if (fileOpenButton == null) {
			fileOpenButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/FileAttach.gif")));
			fileOpenButton.setPreferredSize(new java.awt.Dimension(22,22));
			fileOpenButton.setMaximumSize(new java.awt.Dimension(22,22));
	        fileOpenButton.setToolTipText(DynaMOAD.getMSRString("WRD_0083", "File attach", 3));
	        fileOpenButton.setActionCommand("File Open");

	        fileOpenButton.addActionListener(this);
		}
		return fileOpenButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFileReadOnlyButton() {
		if (fileReadOnlyButton == null) {
			fileReadOnlyButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/executionView.gif")));
			fileReadOnlyButton.setPreferredSize(new java.awt.Dimension(22,22));
			fileReadOnlyButton.setMaximumSize(new java.awt.Dimension(22,22));
	        fileReadOnlyButton.setEnabled(false);
	        fileReadOnlyButton.setToolTipText(DynaMOAD.getMSRString("WRD_0062", "Read", 3));
	        fileReadOnlyButton.setActionCommand("Read Only");

	        fileReadOnlyButton.addActionListener(this);
		}
		return fileReadOnlyButton;
	}
	/**
	 * This method initializes fileTreeTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTreeTable getFileTreeTable() {
		if (fileTreeTable == null) {
            TreeNodeObject treeNodeData = new TreeNodeObject(instanceOuid, (String)getDOSGlobal.get("md$number"), "Root");
            fileRootNode = new DefaultMutableTreeNode(treeNodeData);

            fileModel = new FileTreeModel(fileRootNode);
            fileTreeTable = new JTreeTable(fileModel);

            fileListener = new FileListener();
            fileTreeTable.addTreeSelectionListener(fileListener);
            fileTreeTable.getTableHeader().setBackground(DynaTheme.tableheaderColor);

            TableColumn tc;
            for(Enumeration enum = fileTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(UIBuilder.getHeaderRenderer()))
                tc = (TableColumn)enum.nextElement();

            UIUtils.expandTree1Level(fileTreeTable.tree);
            
            fileOpenButton.setEnabled(hasUpdatePermission);
		}
		
		return fileTreeTable;
	}
	/**
	 * This method initializes fileTableScrPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (fileTableScrPane == null) {
	        fileTableScrPane = UIFactory.createStrippedScrollPane(null);
	        fileTableScrPane.setViewportView(getFileTreeTable());
	        fileTableScrPane.getViewport().setBackground(DynaTheme.panelBackgroundColor);
		}
		return fileTableScrPane;
	}
    
    private void createPopup()
    {
        filePopupMenu = new JPopupMenu();
        
        menuFileDetach = new JMenuItem();
        menuFileDetach.setText(DynaMOAD.getMSRString("WRD_0067", "File Delete", 3));
        menuFileDetach.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        menuFileDetach.setActionCommand("Detach");
        menuFileDetach.addActionListener(this);
        
        menuFileCheckIn = new JMenuItem();
        menuFileCheckIn.setText(DynaMOAD.getMSRString("WRD_0061", "Check-In", 0));
        menuFileCheckIn.setIcon(new ImageIcon(getClass().getResource("/icons/CheckIn.gif")));
        menuFileCheckIn.setActionCommand("Check-In");
        menuFileCheckIn.addActionListener(this);
        
        menuFileCheckOut = new JMenuItem();
        menuFileCheckOut.setText(DynaMOAD.getMSRString("WRD_0060", "Check-Out", 0));
        menuFileCheckOut.setIcon(new ImageIcon(getClass().getResource("/icons/CheckOut.gif")));
        menuFileCheckOut.setActionCommand("Check-Out");
        menuFileCheckOut.addActionListener(this);
        
        menuFileCheckOutCancel = new JMenuItem();
        menuFileCheckOutCancel.setText(DynaMOAD.getMSRString("WRD_0166", "Check-Out Cancel", 0));
        menuFileCheckOutCancel.setActionCommand("Check-Out Cancel");
        menuFileCheckOutCancel.addActionListener(this);
        
        menuFileReadOnly = new JMenuItem();
        menuFileReadOnly.setText(DynaMOAD.getMSRString("WRD_0062", "Read", 3));
        menuFileReadOnly.setIcon(new ImageIcon(getClass().getResource("/icons/executionView.gif")));
        menuFileReadOnly.setActionCommand("Read Only");
        menuFileReadOnly.addActionListener(this);
        
        filePopupMenu.add(menuFileReadOnly);
        filePopupMenu.add(new JSeparator());
        filePopupMenu.add(menuFileCheckIn);
        filePopupMenu.add(menuFileCheckOut);
        filePopupMenu.add(menuFileCheckOutCancel);
        filePopupMenu.add(new JSeparator());
        filePopupMenu.add(menuFileDetach);

        MouseListener LinkMousePopup = new PopupLink();
        fileTreeTable.addMouseListener(LinkMousePopup);
    }

    private void refreshPermissionValues() {
        try {
            String statusCode = dos.getStatus(instanceOuid);
            if (statusCode == null)
                return;
            
            hasUpdatePermission = false;
            hasDeletePermission = false;
            hasLinkPermission = false;
            hasFileUpdatePermission = false;
            hasPrintPermission = false;
            
            if ("WIP".equals(statusCode) || "CRT".equals(statusCode)
                    || "REJ".equals(statusCode))
                if (LogIn.isAdmin) {
                    hasUpdatePermission = true;
                    hasDeletePermission = true;
                    hasLinkPermission = true;
                    hasFileUpdatePermission = true;
                    hasPrintPermission = true;
                } else {
                    hasUpdatePermission = acl.hasPermission(classOuid,
                            instanceOuid, "UPDATE", LogIn.userID);
                    hasDeletePermission = acl.hasPermission(classOuid,
                            instanceOuid, "DELETE", LogIn.userID);
                    hasLinkPermission = acl.hasPermission(classOuid,
                            instanceOuid, "LINK / UNLINK", LogIn.userID);
                    hasFileUpdatePermission = acl.hasPermission(classOuid,
                            instanceOuid, "CHECK-IN / CHECK-OUT",
                            LogIn.userID);
                    hasPrintPermission = acl.hasPermission(classOuid,
                            instanceOuid, "PRINT", LogIn.userID);
                }
        } catch (IIPRequestException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        try {
            String command = actionEvent.getActionCommand();

            if (command.equals("File Open")) {
                String defaultFolder = DynaMOAD.wks.getPrivateDefaultFolder(LogIn.userID);
                
                JFileChooser fileChooser = null;
                if (Utils.isNullString(defaultFolder))
                    fileChooser = new JFileChooser();
                else
                    fileChooser = new JFileChooser(defaultFolder);
                
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.addChoosableFileFilter(new SoftWareFilter(""));
                
                int returnVal = fileChooser.showDialog(this, DynaMOAD
                        .getMSRString("WRD_0083", "File Attach", 0));
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    files = fileChooser.getSelectedFiles();
                    Runnable batchUpload = new Runnable() {

                        File files[] = null;

                        public void run() {
                            if (FileManagePanel.this.files == null)
                                return;
                            files = FileManagePanel.this.files;
                            String filePath = "";
                            File file = null;
                            HashMap fileInfo = null;
                            String serverPath = null;
                            try {
                                for (int i = 0; i < files.length; i++) {
                                    file = files[i];
                                    if (file != null) {
                                        filePath = file.getAbsolutePath();
                                        if (file == null || !file.isFile())
                                            return;
                                        File baseFile = new File(filePath);
                                        String fileName = baseFile.getName();
                                        int index = fileName.lastIndexOf('.');
                                        if (index < 0)
                                            return;
                                        index++;
                                        String extension = fileName.substring(index);
                                        String CADName = (String) dos.getClass(classOuid).get("name");
                                        fileInfo = new HashMap();
                                        if (extension.equals("prt")) {
                                            if (CADName.equals("Unigraphics Assembly"))
                                                fileInfo.put("md$filetype.id", "ug-assembly");
                                            else if (CADName.equals("Unigraphics Part"))
                                                fileInfo.put("md$filetype.id", "ug-part");
                                            else
                                                fileInfo.put("md$filetype.id",
                                                        dss.getFileTypeId(filePath.toLowerCase()));
                                        } else {
                                            fileInfo.put("md$filetype.id",
                                                    dss.getFileTypeId(filePath.toLowerCase()));
                                        }
                                        fileInfo.put("md$des", filePath);
                                        fileInfo.put("md$ouid", instanceOuid);
                                        Object returnObject = Utils.executeScriptFile(dos.getEventName(
                                                classOuid, "file.attach.before"),dss, fileInfo);
                                        if ((returnObject instanceof Boolean)
                                                && !Utils.getBoolean((Boolean) returnObject))
                                            return;
                                        serverPath = dos.attachFile(instanceOuid, fileInfo);
                                        if (ftd == null)
                                            ftd = new FileTransferDialog(parent, false);
                                        ftd.setMaximumSize((new Long(file
                                                .length())).intValue());
                                        UIUtils.setLocationRelativeTo(ftd, FileManagePanel.this);
                                        ftd.setVisible(true);
                                        upload(serverPath, filePath, new FileCallBack());
                                        Utils.executeScriptFile(dos.getEventName(classOuid,
                                                "file.attach.after"), dss, fileInfo);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            fileTreeRefresh();
                        }
                    };
                    
                    (new Thread(batchUpload)).start();
                }
            } else if (command.equals("Detach")) {
                    int res = JOptionPane.showConfirmDialog(parent,
                                    DynaMOAD.getMSRString("QST_B422", "File\uC744 \uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", 0),
                                    DynaMOAD.getMSRString("WRD_0019", "Confirmation", 0),
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                                    new ImageIcon(getClass().getResource("/icons/Question32.gif")));
                    if (res != JOptionPane.YES_OPTION)
                        return;
                    
                    DefaultMutableTreeNode detachNode = (DefaultMutableTreeNode) fileTreeTable.tree
                            .getSelectionPath().getLastPathComponent();
                    String treeTableSelectedOuid = ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                            .getSelectionPath().getLastPathComponent())
                            .getUserObject()).getOuid();
                    if (treeTableSelectedOuid == null
                            || detachNode.getLevel() == 0)
                        return;
                    String tempStr = ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                            .getSelectionPath().getLastPathComponent())
                            .getUserObject()).getDescription();
                    java.util.List tempList = Utils.tokenizeMessage(tempStr,
                            '^');
                    String treeTableSelectedVersion = (String) tempList.get(0);
                    String treeTableSelectedPath = (String) tempList.get(1);
                    HashMap fileInfo = new HashMap();
                    fileInfo.put("md$ouid", treeTableSelectedOuid);
                    fileInfo.put("md$path", treeTableSelectedPath);
                    fileInfo.put("md$version", treeTableSelectedVersion);
                    Object returnObject = Utils.executeScriptFile(
                            dos.getEventName(classOuid, "file.detach.before"), dss, fileInfo);
                    if ((returnObject instanceof Boolean)
                            && !Utils.getBoolean((Boolean) returnObject))
                        return;
                    boolean isDetach = dos.detachFile(instanceOuid,
                            fileInfo);
                    Utils.executeScriptFile(dos.getEventName(classOuid, "file.detach.after"),
                            dss, fileInfo);
                    DefaultMutableTreeNode parentTree = (DefaultMutableTreeNode) detachNode.getParent();
                    parentTree.remove(detachNode);
                    if (parentTree.getChildCount() == 0)
                        fileRootNode.remove(parentTree);
                    
                    fileTreeTable.updateUI();
            } else if (command.equals("Check-In")) {
                HashMap fileInfoMap = (HashMap) ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                        .getSelectionPath().getLastPathComponent())
                        .getUserObject()).getLawData();
                CheckIn checkIn = new CheckIn(parent, false,
                        ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                                .getSelectionPath().getLastPathComponent())
                                .getUserObject()).getLawData());
                checkIn.setSession(_session);
                checkIn.setVisible(true);
                fileTreeRefresh((String) fileInfoMap.get("md$filetype.description")
                        + " " + (Integer) fileInfoMap.get("md$index"));
            } else if (command.equals("Check-Out Cancel")) {
                HashMap fileInfoMap = (HashMap) ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                        .getSelectionPath().getLastPathComponent())
                        .getUserObject()).getLawData();
                this.dos.cancelCheckoutFile((String) fileInfoMap
                        .get("md$ouid"), fileInfoMap);
                fileTreeRefresh((String) fileInfoMap.get("md$filetype.description")
                        + " " + (Integer) fileInfoMap.get("md$index"));
            } else if (command.equals("Check-Out")) {
                HashMap fileInfoMap = (HashMap) ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                        .getSelectionPath().getLastPathComponent())
                        .getUserObject()).getLawData();
                CheckOut checkedOut = new CheckOut(parent, false,
                        ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                                .getSelectionPath().getLastPathComponent())
                                .getUserObject()).getLawData());
                checkedOut.setSession(_session);
                checkedOut.setVisible(true);
                fileTreeRefresh((String) fileInfoMap.get("md$filetype.description")
                        + " " + (Integer) fileInfoMap.get("md$index"));
            } else if (command.equals("Read Only")) {
                HashMap tmpMap = (HashMap) ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                        .getSelectionPath().getLastPathComponent())
                        .getUserObject()).getLawData();
                CheckOut checkedOut = new CheckOut(parent, false, tmpMap);
                String fileSeperator = System.getProperty("file.separator") != null ?
                        System.getProperty("file.separator") : "\\";
                File downLoadFile = new File((String) tmpMap.get("md$description"));
                String workingDirectory = System.getProperty("user.dir")
                        + fileSeperator + "tmp" + fileSeperator
                        + downLoadFile.getName();
                checkedOut.setSession(_session);
                checkedOut.setPreselectedFilePath(workingDirectory);
                checkedOut.checkOutCheckBox.setSelected(false);
                checkedOut.downloadCheckBox.setSelected(true);
                checkedOut.invokeCheckBox.setSelected(true);
                checkedOut.setReadOnlyModel(true);
                checkedOut.processButton.doClick();
                checkedOut.setVisible(false);
                checkedOut = null;
            }
       } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload(String serverPath, String clientPath, FileTransferCallback callback) {
        _serverPath = serverPath;
        _clientPath = clientPath;
        _ftc = callback;

        SwingWorker worker = new SwingWorker() {

            String serverPath = _serverPath;
            String clientPath = _clientPath;
            FileTransferCallback ftc = _ftc;

            public Object construct() {
                boolean result = false;
                try {
                    result = dss.uploadFile(serverPath, clientPath, ftc);
                } catch (IIPRequestException e) {
                    e.printStackTrace();
                }
                return new Boolean(result);
            }

            public void finished() {
                ftd.setVisible(false);
                ftd.dispose();
                fileTreeRefresh();
            }
        };

        worker.start();
    }

    private void fileTreeRefresh()
    {
        try
        {
            fileRootNode.removeAllChildren();
            fileTreeTable.updateUI();
            TreeNodeObject nodeData = (TreeNodeObject)fileRootNode.getUserObject();
            ArrayList rows = dos.listFile(instanceOuid);
            HashMap row = null;
            TreeNodeObject childNodeData = null;
            TreeNodeObject fileTypeNodeData = null;
            String fileType = null;
            DOSChangeable fileTypeSave = new DOSChangeable();
            DefaultMutableTreeNode fileTypeNode = null;
            int counter = 0;
            Iterator rowsKey;
            for(rowsKey = rows.iterator(); rowsKey.hasNext();)
            {
                row = (HashMap)rowsKey.next();
                fileType = (String)row.get("md$filetype.id");
                if(counter == 0)
                    fileTypeSave.putOriginalValue("fileType", fileType);
                fileTypeSave.put("fileType", fileType);
                childNodeData = new TreeNodeObject((String)row.get("md$ouid"), (String)row.get("md$filetype.description") + " " + ((Integer)row.get("md$index")).toString(), (String)row.get("md$version") + "^" + (String)row.get("md$path") + "^" + (String)row.get("md$filetype.id") + "^" + (String)row.get("md$checkedout.date") + "^" + (String)row.get("md$checkedin.date"));
                if(fileTypeSave.isChanged("fileType") || counter == 0)
                {
                    fileTypeSave.putOriginalValue("fileType", fileType);
                    fileTypeNodeData = new TreeNodeObject((String)row.get("md$filetype.id"), (String)row.get("md$filetype.description"), (String)row.get("md$filetype.id"));
                    fileTypeNode = new DefaultMutableTreeNode(fileTypeNodeData);
                    fileRootNode.add(fileTypeNode);
                }
                childNodeData.setLawData(row);
                fileTypeNode.add(new DefaultMutableTreeNode(childNodeData));
                childNodeData = null;
                row = null;
                counter++;
            }

            rowsKey = null;
            rows.clear();
            rows = null;
            nodeData.setPopulated(true);
            fileTreeTable.updateUI();
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    private void fileTreeRefresh(String nodeOid)
    {
        try
        {
            fileRootNode.removeAllChildren();
            fileTreeTable.updateUI();
            ArrayList rows = dos.listFile(instanceOuid);
            HashMap row = null;
            TreeNodeObject childNodeData = null;
            TreeNodeObject fileTypeNodeData = null;
            String fileType = null;
            DOSChangeable fileTypeSave = new DOSChangeable();
            DefaultMutableTreeNode fileTypeNode = null;
            int counter = 0;
            Iterator rowsKey = rows.iterator();
            TreePath path = null;
            while(rowsKey.hasNext()) 
            {
                row = (HashMap)rowsKey.next();
                fileType = (String)row.get("md$filetype.id");
                if(counter == 0)
                    fileTypeSave.putOriginalValue("fileType", fileType);
                fileTypeSave.put("fileType", fileType);
                childNodeData = new TreeNodeObject((String)row.get("md$ouid"), (String)row.get("md$filetype.description") + " " + ((Integer)row.get("md$index")).toString(), (String)row.get("md$version") + "^" + (String)row.get("md$path") + "^" + (String)row.get("md$filetype.id") + "^" + (String)row.get("md$checkedout.date") + "^" + (String)row.get("md$checkedin.date"));
                if(fileTypeSave.isChanged("fileType") || counter == 0)
                {
                    fileTypeSave.putOriginalValue("fileType", fileType);
                    fileTypeNodeData = new TreeNodeObject((String)row.get("md$filetype.id"), (String)row.get("md$filetype.description"), (String)row.get("md$filetype.id"));
                    fileTypeNode = new DefaultMutableTreeNode(fileTypeNodeData);
                    fileRootNode.add(fileTypeNode);
                }
                childNodeData.setLawData(row);
                fileTypeNode.add(new DefaultMutableTreeNode(childNodeData));
                if(childNodeData.toString().equals(nodeOid.toString()))
                    path = new TreePath(fileTypeNode.getPath());
                childNodeData = null;
                row = null;
                counter++;
            }
            rowsKey = null;
            rows.clear();
            rows = null;
            fileTreeTable.updateUI();
            fileTreeTable.tree.fireTreeExpanded(path);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setSession(Session session)
    {
        _session = session;
    }

    class FileListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent tse) {
            try {
                TreePath path = tse.getPath();
                if (path == null)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                        .getLastPathComponent();
                TreeNodeObject nodeData = (TreeNodeObject) node.getUserObject();
                if (nodeData.isPopulated())
                    return;
                ArrayList rows = dos.listFile(instanceOuid);
                if (rows == null || rows.size() == 0) {
                    nodeData.setPopulated(true);
                    return;
                }
                if (node.getLevel() == 0) {
                    HashMap row = null;
                    TreeNodeObject childNodeData = null;
                    TreeNodeObject fileTypeNodeData = null;
                    String fileType = null;
                    DOSChangeable fileTypeSave = new DOSChangeable();
                    DefaultMutableTreeNode fileTypeNode = null;
                    int counter = 0;
                    Iterator rowsKey;
                    for (rowsKey = rows.iterator(); rowsKey.hasNext();) {
                        row = (HashMap) rowsKey.next();
                        fileType = (String) row.get("md$filetype.id");
                        if (counter == 0)
                            fileTypeSave.putOriginalValue("fileType", fileType);
                        fileTypeSave.put("fileType", fileType);
                        childNodeData = new TreeNodeObject((String) row
                                .get("md$ouid"), (String) row
                                .get("md$filetype.description")
                                + " "
                                + ((Integer) row.get("md$index")).toString(),
                                (String) row.get("md$version")
                                        + "^"
                                        + (String) row.get("md$path")
                                        + "^"
                                        + (String) row.get("md$filetype.id")
                                        + "^"
                                        + (String) row
                                                .get("md$checkedout.date")
                                        + "^"
                                        + (String) row.get("md$checkedin.date"));
                        if (fileTypeSave.isChanged("fileType") || counter == 0) {
                            fileTypeSave.putOriginalValue("fileType", fileType);
                            fileTypeNodeData = new TreeNodeObject((String) row
                                    .get("md$filetype.id"), (String) row
                                    .get("md$filetype.description"),
                                    (String) row.get("md$filetype.id"));
                            fileTypeNode = new DefaultMutableTreeNode(
                                    fileTypeNodeData);
                            node.add(fileTypeNode);
                        }
                        childNodeData.setLawData(row);
                        fileTypeNode.add(new DefaultMutableTreeNode(
                                childNodeData));
                        childNodeData = null;
                        row = null;
                        counter++;
                    }

                    rowsKey = null;
                    rows.clear();
                    rows = null;
                    nodeData.setPopulated(true);
                    fileTreeTable.tree.fireTreeExpanded(path);
                }
                if (node.getLevel() == 2)
                    fileReadOnlyButton.setEnabled(true);
                else
                    fileReadOnlyButton.setEnabled(false);
            } catch (IIPRequestException e) {
                e.printStackTrace();
            }
        }
    }

    class PopupLink extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(SwingUtilities.isRightMouseButton(e) && !e.isShiftDown() && !e.isControlDown() && !e.isAltDown())
            {
                Point point = new Point(e.getX(), e.getY());
                if(e.getSource() instanceof JTreeTable)
                {
                    int index = ((JTreeTable)e.getSource()).rowAtPoint(point);
                    ((DefaultListSelectionModel)((JTreeTable)e.getSource()).getSelectionModel()).setSelectionInterval(index, index);
                }
            }
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseClicked(MouseEvent e) {
        }

        private void maybeShowPopup(MouseEvent e) {
            if (!e.isPopupTrigger() || !e.getSource().equals(fileTreeTable))
                return;
            
            String tmpStr = ((TreeNodeObject) ((DefaultMutableTreeNode) fileTreeTable.tree
                    .getSelectionPath().getLastPathComponent())
                    .getUserObject()).getDescription();
            java.util.List tmpList = Utils.tokenizeMessage(tmpStr, '^');
            boolean isUpdatable = false;
            String status = null;
            try {
                status = DynaMOAD.dos.getStatus((String) getDOSGlobal
                        .get("ouid"));
                if ("CRT".equals(status) || "WIP".equals(status)
                        || "REJ".equals(status))
                    isUpdatable = true;
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            if (tmpList.size() == 5) {
                String checkedOutDate = (String) Utils.tokenizeMessage(
                        tmpStr, '^').get(3);
                String checkedInDate = (String) Utils.tokenizeMessage(
                        tmpStr, '^').get(4);
                if (isUpdatable) {
                    if (!Utils.isNullString(checkedOutDate)
                            && "null".equals(checkedOutDate))
                        checkedOutDate = null;
                    if (!Utils.isNullString(checkedInDate)
                            && "null".equals(checkedInDate))
                        checkedInDate = null;
                    if (Utils.isNullString(checkedOutDate)
                            && !Utils.isNullString(checkedInDate)) {
                        menuFileCheckOut.setEnabled(true);
                        menuFileCheckIn.setEnabled(false);
                        menuFileCheckOutCancel.setEnabled(false);
                    } else if (!Utils.isNullString(checkedOutDate)
                            && !Utils.isNullString(checkedInDate)
                            && checkedOutDate.compareTo(checkedInDate) > 0) {
                        menuFileCheckOut.setEnabled(false);
                        menuFileCheckIn.setEnabled(true);
                        menuFileCheckOutCancel.setEnabled(true);
                    } else if (!Utils.isNullString(checkedOutDate)
                            && !Utils.isNullString(checkedInDate)
                            && checkedOutDate.compareTo(checkedInDate) <= 0) {
                        menuFileCheckOut.setEnabled(true);
                        menuFileCheckIn.setEnabled(false);
                        menuFileCheckOutCancel.setEnabled(false);
                    } else {
                        menuFileCheckOut.setEnabled(false);
                        menuFileCheckIn.setEnabled(false);
                        menuFileCheckOutCancel.setEnabled(false);
                    }
                } else {
                    menuFileCheckOut.setEnabled(false);
                    menuFileCheckIn.setEnabled(false);
                    menuFileCheckOutCancel.setEnabled(false);
                    menuFileDetach.setEnabled(false);
                }
                filePopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    class FileCallBack implements FileTransferCallback {

        public void transferBytes(int bytes) {
            ftd.addSize(bytes);
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
