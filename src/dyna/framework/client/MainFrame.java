// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MainFrame.java

package dyna.framework.client;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtLabel;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.editor.modeler.CodeManager;
import dyna.framework.editor.user.UserManager;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.WFM;
import dyna.framework.service.WKS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTheme;
import dyna.uic.MInternalFrame;
import dyna.uic.SoftWareFilter;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, SearchCondition, SearchResult, LogIn, 
//            ModelSelectWindow, AdvancedFilterDialogForList, AuthoritySearch, UIManagement

public class MainFrame extends JFrame
    implements ActionListener, WindowListener
{
    private class ScanFilter extends FileFilter
    {

        public boolean accept(File pathname)
        {
            return pathname.isDirectory();
        }

        public String getDescription()
        {
            return "*";
        }

        ScanFilter()
        {
        }
    }


    public MainFrame()
    {
        dos = null;
        dtm = null;
        aus = null;
        dss = null;
        wfm = null;
        searchResultFrame = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true); 
        modelOuid = "";
        try
        {
            dos = DynaMOAD.dos;
            dtm = DynaMOAD.dtm;
            aus = DynaMOAD.aus;
            dss = DynaMOAD.dss;
            wfm = DynaMOAD.wfm;
            newUI = DynaMOAD.newUI;
            autoLoading();
            addWindowListener(this);
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - 25);
        mainFrameBorderLayout = new BorderLayout();
        getContentPane().setLayout(mainFrameBorderLayout);
        menuBarMain = new JMenuBar();
        menuBarMain.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        setJMenuBar(menuBarMain);
        menuMain = new JMenu(DynaMOAD.getMSRString("MNU_FILE", "File", 3));
        menuMain.setFont(DynaTheme.mediumPlainFont);
        menuMain.setMnemonic(70);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuItemMain = new JMenuItem(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3) + "...", new ImageIcon("icons/Open.gif"));
        menuItemMain.setActionCommand("Open");
        menuItemMain.setMnemonic(79);
        menuItemMain.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuItemMain = new JMenuItem(DynaMOAD.getMSRString("MNU_EXIT", "Exit", 3));
        menuItemMain.setActionCommand("Close");
        menuItemMain.setMnemonic(88);
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain = new JMenu(DynaMOAD.getMSRString("MNU_TOOL", "Tool", 3));
        menuMain.setFont(DynaTheme.mediumPlainFont);
        menuMain.setMnemonic(84);
        menuMain.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBarMain.add(menuMain);
        menuSubMain = new JMenu(DynaMOAD.getMSRString("MNU_CONFIGURATION", "Configuration", 3));
        menuSubMain.setActionCommand("Package");
        menuSubMain.addActionListener(this);
        menuMain.add(menuSubMain);
        conditionItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0047", "Search Condition", 3), new ImageIcon("icons/filter.gif"));
        conditionItem.setEnabled(false);
        conditionItem.setActionCommand("Search Condition");
        conditionItem.addActionListener(this);
        menuSubMain.add(conditionItem);
        resultItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3), new ImageIcon("icons/Properties16.gif"));
        resultItem.setEnabled(false);
        resultItem.setActionCommand("Search Result");
        resultItem.addActionListener(this);
        menuSubMain.add(resultItem);
        menuSubMain.add(new JSeparator());
        defaultFolderMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_B401", "Default Folder", 3), new ImageIcon("icons/cfldr_obj.gif"));
        defaultFolderMenuItem.setActionCommand("defaultFolderMenuItem");
        defaultFolderMenuItem.addActionListener(this);
        menuSubMain.add(defaultFolderMenuItem);
        menuItemMain = new JMenuItem(DynaMOAD.getMSRString("WRD_0056", "Advanced", 3) + " " + DynaMOAD.getMSRString("WRD_0015", "Filter", 3), new ImageIcon("icons/Search.gif"));
        menuItemMain.setMnemonic(65);
        menuItemMain.setActionCommand("Advanced Filter");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        menuMain.add(new JSeparator());
        menuItemMain = new JMenuItem(DynaMOAD.getMSRString("MNU_CODE_MANAGER", "Code Manager", 3), new ImageIcon("icons/code.gif"));
        menuItemMain.setMnemonic(77);
        menuItemMain.setActionCommand("Code Manager");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        if(!isAdmin())
            menuItemMain.setEnabled(false);
        menuItemMain = new JMenuItem(DynaMOAD.getMSRString("MNU_AUTHORITY_LIST", "Authority List", 3), new ImageIcon("icons/Acl.gif"));
        menuItemMain.setMnemonic(65);
        menuItemMain.setActionCommand("ACL");
        menuItemMain.addActionListener(this);
        menuMain.add(menuItemMain);
        if(!isAdmin())
            menuItemMain.setEnabled(false);
        mainToolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
        mainToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        openButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        openButton.setText(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3) + "...");
        openButton.setToolTipText(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3) + "...");
        openButton.setActionCommand("Open");
        openButton.addActionListener(this);
        mainToolBar.add(openButton);
        JPanel leftPanel = new JPanel();
        JLabel treeLabel = new JLabel("Tree");
        leftPanel.add(treeLabel);
        JPanel rightPanel = new JPanel();
        JLabel tableLabel = new JLabel("Table");
        rightPanel.add(tableLabel);
        searchCondition = new SearchCondition(this);
        JScrollPane leftScrPane = UIFactory.createStrippedScrollPane(null);
        leftScrPane.setViewportView(searchCondition);
        searchResultFrame = new MInternalFrame(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
        searchResult = new SearchResult(this);
        searchResultFrame.add(searchResult);
        selectAllButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/SelectAll.gif")));
        selectAllButton.setActionCommand("selectAllButton");
        selectAllButton.setToolTipText(DynaMOAD.getMSRString("MNU_SELECTALL_SEARCHRESULT", "Select All", 3));
        selectAllButton.addActionListener(this);
        clearSelectionButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/ClearSelection.gif")));
        clearSelectionButton.setActionCommand("clearSelectionButton");
        clearSelectionButton.setToolTipText(DynaMOAD.getMSRString("MNU_CLEARSELECTION_SEARCHRESULT", "Deselect All", 3));
        clearSelectionButton.addActionListener(this);
        saveResultButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/SaveAs16.gif")));
        saveResultButton.setActionCommand("searchResultButton");
        saveResultButton.setToolTipText(DynaMOAD.getMSRString("MNU_SAVE_SEARCHRESULT", "Save as...", 3));
        saveResultButton.addActionListener(this);
        previousSearchButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Rewind16.gif")));
        previousSearchButton.setActionCommand("previousSearch");
        previousSearchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0159", "Previous Page", 3));
        previousSearchButton.addActionListener(this);
        previousSearchButton.setEnabled(false);
        nextSearchButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/FastForward16.gif")));
        nextSearchButton.setActionCommand("nextSearch");
        nextSearchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0160", "Next Page", 3));
        nextSearchButton.addActionListener(this);
        nextSearchButton.setEnabled(false);
        searchResultToolBar = new ExtToolBar("Search Result ToolBar");
        searchResultToolBar.add(previousSearchButton);
        searchResultToolBar.add(nextSearchButton);
        searchResultToolBar.add(Box.createHorizontalStrut(10));
        searchResultToolBar.add(selectAllButton);
        searchResultToolBar.add(clearSelectionButton);
        searchResultToolBar.add(Box.createHorizontalStrut(10));
        searchResultToolBar.add(saveResultButton);
        searchResultFrame.setToolBar(searchResultToolBar);
        mainSplitPane = new BorderlessSplitPane(1, searchCondition, searchResultFrame);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder(3, 2, 0, 2));
        getContentPane().add(mainToolBar, "North");
        getContentPane().add(mainSplitPane, "Center");
        getContentPane().add(DynaMOAD.buildStatusBar(statusField), "South");
        searchResultPanel = new DOSChangeable();
    }

    public void clearSearchResult()
    {
        searchResultPanel.clear();
    }

    public void reloadSearchResult(String selectedOuid)
    {
        if(searchResultPanel.get(selectedOuid) != null)
            searchResult.setResultData((ArrayList)searchResultPanel.get(selectedOuid));
    }

    public void makeObjectSetTree(TreeNodeObject rootnodedata, int i)
    {
        try
        {
            rootnodedata = rootnodedata;
            searchCondition.setWorkSpace();
            searchCondition.setTopNode(rootnodedata);
            DefaultTreeModel treemodel = (DefaultTreeModel)searchCondition.getTree().getModel();
            modelOuid = rootnodedata.getOuid();
            dos.setWorkingModel(rootnodedata.getOuid());
            searchCondition.setFullTreeExpand((DefaultMutableTreeNode)treemodel.getRoot(), modelOuid);
            searchCondition.makeHierarchyCodeTree(modelOuid);
            searchCondition.clearSearchConditionPanel();
            setTitle(rootnodedata.getName() + " - " + LogIn.userName + "(" + LogIn.userID + ")");
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public void removeFiles()
    {
        String fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
        String directory = System.getProperty("user.dir") + fileSeperator + "tmp";
        File file = new File(directory);
        String files[] = file.list();
        if(files == null || files.length <= 0)
            return;
        file = null;
        for(int i = 0; i < files.length; i++)
        {
            String filename = directory + fileSeperator + files[i];
            file = new File(filename);
            if(file.isFile())
            {
                file.delete();
                file = null;
            }
        }

    }

    public void setConfigurationMenuEnable(boolean isEnable)
    {
        conditionItem.setEnabled(isEnable);
        resultItem.setEnabled(isEnable);
    }

    public boolean isAdmin()
    {
        boolean isadmin = false;
        try
        {
            isadmin = aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR");
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return isadmin;
    }

    public void loadScripts()
    {
        String scriptPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "script";
        String classOuid = null;
        String fileName = null;
        ArrayList rootClassList = null;
        ArrayList leafClassList = null;
        ArrayList superClassList = null;
        ArrayList eventList = null;
        Iterator rootClassKey = null;
        Iterator leafClassKey = null;
        Iterator superClassKey = null;
        Iterator allClassKey = null;
        Iterator eventKey = null;
        Iterator fileKey = null;
        DOSChangeable aClass = null;
        HashMap aEvent = null;
        HashMap classMap = null;
        HashMap scriptMap = null;
        HashMap fileMap = null;
        boolean willFileDownload = false;
        File file = new File(scriptPath);
        file.mkdir();
        try
        {
            rootClassList = dos.listRootClassInModel(modelOuid);
            if(rootClassList == null)
            {
                file = null;
                return;
            }
            classMap = new HashMap();
            for(rootClassKey = rootClassList.iterator(); rootClassKey.hasNext();)
            {
                aClass = (DOSChangeable)rootClassKey.next();
                classOuid = (String)aClass.get("ouid");
                aClass = null;
                classMap.put(classOuid, classOuid);
                leafClassList = dos.listAllLeafClassOuidInModel(modelOuid, classOuid);
                if(leafClassList != null)
                {
                    for(leafClassKey = leafClassList.iterator(); leafClassKey.hasNext();)
                    {
                        classOuid = (String)leafClassKey.next();
                        classMap.put(classOuid, classOuid);
                        superClassList = dos.listAllSuperClassOuid(classOuid);
                        if(superClassList != null)
                        {
                            for(superClassKey = superClassList.iterator(); superClassKey.hasNext(); classMap.put(classOuid, classOuid))
                                classOuid = (String)superClassKey.next();

                            superClassKey = null;
                            superClassList.clear();
                            superClassList = null;
                        }
                    }

                    leafClassKey = null;
                    leafClassList.clear();
                    leafClassList = null;
                }
            }

            rootClassKey = null;
            rootClassList.clear();
            rootClassList = null;
            if(classMap.size() == 0)
                return;
            scriptMap = new HashMap();
            for(allClassKey = classMap.keySet().iterator(); allClassKey.hasNext();)
            {
                classOuid = (String)allClassKey.next();
                eventList = dos.listEvent(classOuid);
                if(eventList != null)
                {
                    for(eventKey = eventList.iterator(); eventKey.hasNext();)
                    {
                        aEvent = (HashMap)eventKey.next();
                        scriptMap.put(aEvent.get("name.script"), "");
                        aEvent = null;
                    }

                    eventKey = null;
                    eventList.clear();
                    eventList = null;
                }
                eventList = dos.listActionInClass(classOuid);
                if(eventList != null)
                {
                    for(eventKey = eventList.iterator(); eventKey.hasNext(); scriptMap.put(dos.getActionScriptName((String)((DOSChangeable)eventKey.next()).get("ouid")), ""));
                    eventKey = null;
                    eventList.clear();
                    eventList = null;
                }
                ArrayList processList = wfm.listProcessDeifnitionOfClass(classOuid);
                if(processList != null)
                {
                    Iterator processKey = processList.iterator();
                    String processDefinitionOuid = null;
                    while(processKey.hasNext()) 
                    {
                        processDefinitionOuid = (String)processKey.next();
                        eventList = dos.listEvent("wf$dpro@" + processDefinitionOuid);
                        if(eventList != null)
                        {
                            for(eventKey = eventList.iterator(); eventKey.hasNext();)
                            {
                                aEvent = (HashMap)eventKey.next();
                                if(!aEvent.get("type.event").equals("start.before") && !aEvent.get("type.event").equals("start.after") && !aEvent.get("type.event").equals("finish.before") && !aEvent.get("type.event").equals("finish.after"))
                                    scriptMap.put(aEvent.get("name.script"), "");
                                aEvent = null;
                            }

                            eventKey = null;
                            eventList.clear();
                            eventList = null;
                        }
                    }
                }
            }

            allClassKey = null;
            classMap.clear();
            classMap = null;
            if(scriptMap.size() == 0)
                return;
            File files[] = file.listFiles();
            fileMap = new HashMap();
            for(int i = 0; i < files.length; i++)
                fileMap.put(files[i].getName(), files[i]);

            file = null;
            for(fileKey = scriptMap.keySet().iterator(); fileKey.hasNext();)
            {
                willFileDownload = false;
                fileName = (String)fileKey.next();
                file = (File)fileMap.get(fileName);
                if(file == null)
                    willFileDownload = true;
                else
                if(dss.getLastModifiedDate("/script/" + fileName) != file.lastModified() || dss.getFileSize("/script/" + fileName) != file.length())
                    willFileDownload = true;
                if(willFileDownload)
                {
                    if(file != null)
                        file.delete();
                    dss.downloadFile("/script/" + fileName, scriptPath + System.getProperty("file.separator") + fileName, null);
                }
            }

            files = (File[])null;
            scriptMap.clear();
            scriptMap = null;
            fileMap.clear();
            fileMap = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("Close"))
        {
            if(!Utils.isNullString(modelOuid))
                try
                {
                    Utils.executeScriptFile(dos.getEventName(modelOuid, "model.closed"), dss);
                    boolean isSuccess = aus.logout(LogIn.userID);
                    if(isSuccess)
                    {
                        dispose();
                        removeFiles();
                        System.exit(0);
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        } else
        if(command.equals("Open"))
        {
            ModelSelectWindow modelSelect = new ModelSelectWindow(this);
            modelSelect.setVisible(true);
            modelOuid = ModelSelectWindow.modelOuid;
        } else
        if(command.equals("Save"))
            clearSearchResult();
        else
        if(command.equals("Search Condition"))
            searchCondition.invokeSearchConditionSetting();
        else
        if(command.equals("Search Result"))
            searchCondition.invokeSearchResultSetting();
        else
        if(command.equals("Advanced Filter"))
        {
            AdvancedFilterDialogForList advancedFilter = new AdvancedFilterDialogForList(this, null, true);
            advancedFilter.setVisible(true);
        } else
        if(command.equals("Code Manager"))
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
        else
        if(command.equals("ACL"))
        {
            AuthoritySearch authoritySearch = new AuthoritySearch(modelOuid);
            authoritySearch.pack();
            authoritySearch.setVisible(true);
        } else
        if(command.equals("USER"))
        {
            UserManager usermanager = new UserManager(this);
            usermanager = null;
        } else
        if(command.equals("searchResultButton"))
        {
            if(searchResultFrame == null)
                return;
            Object object = searchResultFrame.getContent();
            if(object == null || !(object instanceof SearchResult))
                return;
            SearchResult searchResult = (SearchResult)object;
            if(searchResult.searchResultTable == null)
                return;
            JTable table = searchResult.searchResultTable.getTable();
            if(table == null || table.getRowCount() == 0)
                return;
            String fileSeperator = System.getProperty("file.separator") != null ? System.getProperty("file.separator") : "\\";
            JFileChooser fileChooser = null;
            SoftWareFilter softwareFilter = null;
            File selectedFile = null;
            File preselectedFile = new File(System.getProperty("user.home") + fileSeperator + DynaMOAD.getMSRString("WRD_0046", "Search Result", 3) + ".csv");
            fileChooser = new JFileChooser(System.getProperty("user.home"));
            DOSChangeable softwareInfo = new DOSChangeable();
            softwareInfo.put("EXTENSION", "csv");
            softwareInfo.put("NAME", "Comma Separated Value File (*.csv)");
            softwareFilter = new SoftWareFilter(softwareInfo);
            fileChooser.addChoosableFileFilter(softwareFilter);
            if(preselectedFile != null)
                fileChooser.setSelectedFile(preselectedFile);
            preselectedFile = null;
            if(fileChooser.showSaveDialog(searchResult) == 0)
            {
                selectedFile = fileChooser.getSelectedFile();
                fileChooser = null;
                try
                {
                    if(!selectedFile.exists())
                        selectedFile.createNewFile();
                    if(!selectedFile.canWrite())
                        throw new IOException(DynaMOAD.getMSRString("ERR_0001", "Can not modify the file.", 0));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));
                    int rows = table.getRowCount();
                    int columns = table.getColumnCount();
                    String str = null;
                    bw.write(34);
                    for(int j = 0; j < columns; j++)
                    {
                        bw.write(table.getColumnName(j));
                        if(j < columns - 1)
                            bw.write("\",\"");
                    }

                    bw.write(34);
                    bw.newLine();
                    for(int i = 0; i < rows; i++)
                    {
                        bw.write(34);
                        for(int j = 0; j < columns; j++)
                        {
                            object = table.getValueAt(i, j);
                            if(object != null)
                                bw.write(object.toString());
                            if(j < columns - 1)
                                bw.write("\",\"");
                        }

                        bw.write(34);
                        if(i < rows - 1)
                            bw.newLine();
                    }

                    bw.flush();
                    bw.close();
                    bw = null;
                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }
            }
        } else
        if(command.equals("selectAllButton"))
        {
            if(searchResultFrame == null)
                return;
            Object object = searchResultFrame.getContent();
            if(object == null || !(object instanceof SearchResult))
                return;
            SearchResult searchResult = (SearchResult)object;
            if(searchResult.searchResultTable == null)
                return;
            JTable table = searchResult.searchResultTable.getTable();
            if(table == null || table.getRowCount() == 0)
                return;
            table.selectAll();
        } else
        if(command.equals("clearSelectionButton"))
        {
            if(searchResultFrame == null)
                return;
            Object object = searchResultFrame.getContent();
            if(object == null || !(object instanceof SearchResult))
                return;
            SearchResult searchResult = (SearchResult)object;
            if(searchResult.searchResultTable == null)
                return;
            JTable table = searchResult.searchResultTable.getTable();
            if(table == null || table.getRowCount() == 0)
                return;
            table.clearSelection();
        } else
        if(command.startsWith("action$"))
        {
            if(searchResultFrame == null)
                return;
            Object object = searchResultFrame.getContent();
            if(object == null || !(object instanceof SearchResult))
                return;
            SearchResult searchResult = (SearchResult)object;
            if(searchResult.searchResultTable == null)
                return;
            JTable table = searchResult.searchResultTable.getTable();
            if(table == null || table.getRowCount() == 0)
                return;
            int selectedRows[] = table.getSelectedRows();
            if(selectedRows == null && selectedRows.length < 1)
                return;
            String filePath = null;
            ArrayList ouidList = new ArrayList();
            String instanceOuid = null;
            String ouidRow = null;
            ArrayList dataList = searchResult.searchResultTable.getList();
            for(int n = 0; n < selectedRows.length; n++)
            {
                ouidRow = searchResult.searchResultTable.getSelectedOuidRow(selectedRows[n]);
                if(ouidRow != null)
                    searchResult.searchResultTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                if(ouidRow != null)
                    instanceOuid = (String)((ArrayList)dataList.get((new Integer(ouidRow)).intValue())).get(0);
                else
                    instanceOuid = (String)((ArrayList)dataList.get((new Integer(selectedRows[n])).intValue())).get(0);
                ouidList.add(instanceOuid);
            }

            if(Utils.isNullArrayList(ouidList))
                ouidList = null;
            try
            {
                java.util.List list = Utils.tokenizeMessage(command, '$');
                filePath = dos.getActionScriptName((String)list.get(1));
                HashMap map = new HashMap();
                map.put("list", ouidList);
                Utils.executeScriptFile(filePath, DynaMOAD.dss, map);
                ouidList = null;
                map = null;
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
        } else
        if(command.equals("previousSearch"))
        {
            int pageNum = searchCondition.getPageNumber() - 1;
            searchCondition.setPageNumber(pageNum);
            searchCondition.searchButton_actionPerformed(e);
        } else
        if(command.equals("nextSearch"))
        {
            int pageNum = searchCondition.getPageNumber() + 1;
            searchCondition.setPageNumber(pageNum);
            searchCondition.searchButton_actionPerformed(e);
        } else
        if(command.equals("defaultFolderMenuItem"))
        {
            String inputedString = null;
            File selectedFile = null;
            try
            {
                inputedString = DynaMOAD.wks.getPrivateDefaultFolder(LogIn.userID);
                if(Utils.isNullString(inputedString))
                    inputedString = System.getProperty("user.home");
                inputedString = JOptionPane.showInputDialog(this, DynaMOAD.getMSRString("WRD_B401", "Default Folder", 3), inputedString);
                if(Utils.isNullString(inputedString))
                    return;
                selectedFile = new File(inputedString);
                if(!selectedFile.isDirectory())
                {
                    selectedFile = null;
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("WRN_0102", "Please select a directory.", 3), DynaMOAD.getMSRString("WRD_0004", "Information", 0), 0);
                    return;
                }
                selectedFile = null;
                DynaMOAD.wks.setPrivateDefaultFolder(LogIn.userID, inputedString);
            }
            catch(Exception ee)
            {
                selectedFile = null;
                ee.printStackTrace();
            }
        }
    }

    public void windowClosing(WindowEvent e)
    {
        if(!Utils.isNullString(modelOuid))
            try
            {
                Utils.executeScriptFile(dos.getEventName(modelOuid, "model.closed"), dss);
                aus.logout(LogIn.userID);
                DynaMOAD.dfw.releaseLicense("DP-BASE");
            }
            catch(IIPRequestException iiprequestexception) { }
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

    public void autoLoading()
    {
        String ouid = null;
        ArrayList modelDataAL = null;
        try
        {
            modelDataAL = dos.listModel();
            ouid = (String)((DOSChangeable)modelDataAL.get(0)).get("ouid");
        }
        catch(Exception ee)
        {
            System.err.println(ee);
        }
        if(ouid != null)
        {
            try
            {
                String oldModel = DynaMOAD.dos.getWorkingModel();
                if(!Utils.isNullString(oldModel))
                    try
                    {
                        Utils.executeScriptFile(dos.getEventName(oldModel, "model.closed"), dss);
                    }
                    catch(IIPRequestException ie)
                    {
                        System.err.println(ie);
                    }
                DynaMOAD.dos.setWorkingModel(ouid);
                modelOuid = ouid;
            }
            catch(Exception ee)
            {
                System.err.println(ee);
            }
            initialize();
            String name = (String)((DOSChangeable)modelDataAL.get(0)).get("name");
            String description = (String)((DOSChangeable)modelDataAL.get(0)).get("description");
            TreeNodeObject rootNode = new TreeNodeObject(ouid, name, description);
            makeObjectSetTree(rootNode, 1);
            loadScripts();
            setConfigurationMenuEnable(false);
            setTitle(name + " - " + LogIn.userName + "(" + LogIn.userID + ")");
            if(!Utils.isNullString(ouid))
                try
                {
                    Utils.executeScriptFile(dos.getEventName(ouid, "model.opened"), dss);
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
        }
    }

    public void setActionButtonList(ArrayList actionButtonList)
    {
        if(this.actionButtonList == null)
            this.actionButtonList = new ArrayList();
        JComponent comp = null;
        Iterator listKey;
        for(listKey = this.actionButtonList.iterator(); listKey.hasNext();)
        {
            comp = (JComponent)listKey.next();
            if(comp != null)
            {
                searchResultToolBar.remove(comp);
                comp = null;
            }
        }

        listKey = null;
        this.actionButtonList.clear();
        if(Utils.isNullArrayList(actionButtonList))
            return;
        for(listKey = actionButtonList.iterator(); listKey.hasNext();)
        {
            comp = (JComponent)listKey.next();
            if(comp instanceof AbstractButton)
            {
                searchResultToolBar.add(comp);
                this.actionButtonList.add(comp);
            }
            comp = null;
        }

        listKey = null;
    }

    public void setEnabledPreviousSearchButton(boolean b)
    {
        previousSearchButton.setEnabled(b);
    }

    public void setEnabledNextSearchButton(boolean b)
    {
        nextSearchButton.setEnabled(b);
    }

    private DOS dos;
    private DTM dtm;
    private AUS aus;
    private DSS dss;
    private WFM wfm;
    private UIManagement newUI;
    public SearchCondition searchCondition;
    public MInternalFrame searchResultFrame;
    public SearchResult searchResult;
    private JToolBar searchResultToolBar;
    private JButton selectAllButton;
    private JButton clearSelectionButton;
    private JButton saveResultButton;
    private ArrayList actionButtonList;
    private JButton previousSearchButton;
    private JButton nextSearchButton;
    private BorderLayout mainFrameBorderLayout;
    private JMenuBar menuBarMain;
    private JMenu menuMain;
    private JMenuItem menuItemMain;
    private JMenu menuSubMain;
    private JMenuItem conditionItem;
    private JMenuItem resultItem;
    private JMenuItem defaultFolderMenuItem;
    private JLabel statusField;
    private JToolBar mainToolBar;
    private JButton openButton;
    public JSplitPane mainSplitPane;
    public String modelOuid;
    public static TreeNodeObject rootnodedata;
    private DOSChangeable searchResultPanel;
}