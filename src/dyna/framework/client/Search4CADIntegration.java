// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Search4CADIntegration.java

package dyna.framework.client;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.swing.BorderlessSplitPane;
import com.jgoodies.swing.ExtLabel;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.MInternalFrame;
import dyna.uic.SoftWareFilter;
import dyna.uic.Table;
import dyna.uic.TreeNodeObject;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, LogIn, SearchCondition4CADIntegration, SearchResult4CADIntegration, 
//            UIManagement

public class Search4CADIntegration extends JFrame
    implements ActionListener, WindowListener
{

    public Search4CADIntegration(Session session, String cadType)
    {
        dos = null;
        dtm = null;
        aus = null;
        dss = null;
        searchResultFrame = null;
        statusField = new ExtLabel("Copyright (c) 2004, EESIN Information Technology Ltd.", true); 
        modelOuid = "";
        _session = null;
        try
        {
            dos = DynaMOAD.dos;
            dtm = DynaMOAD.dtm;
            aus = DynaMOAD.aus;
            dss = DynaMOAD.dss;
            newUI = DynaMOAD.newUI;
            this.cadType = cadType;
            if(session != null)
                _session = session;
            initialize();
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
        setTitle("coMoad - " + LogIn.userID);
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(800, 600);
        mainFrameBorderLayout = new BorderLayout();
        getContentPane().setLayout(mainFrameBorderLayout);
        mainToolBar = new ExtToolBar("mainToolBar");
        mainToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
        closeButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        mainToolBar.add(closeButton);
        JPanel leftPanel = new JPanel();
        JLabel treeLabel = new JLabel("Tree");
        leftPanel.add(treeLabel);
        JPanel rightPanel = new JPanel();
        JLabel tableLabel = new JLabel("Table");
        rightPanel.add(tableLabel);
        searchCondition4CADIntegration = new SearchCondition4CADIntegration(this);
        JScrollPane leftScrPane = UIFactory.createStrippedScrollPane(null);
        leftScrPane.setViewportView(searchCondition4CADIntegration);
        searchResultFrame = new MInternalFrame(DynaMOAD.getMSRString("WRD_0046", "Search Result", 3));
        searchResult4CADIntegration = new SearchResult4CADIntegration(this);
        searchResult4CADIntegration.setSession(_session);
        searchResultFrame.add(searchResult4CADIntegration);
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
        mainSplitPane = new BorderlessSplitPane(1, searchCondition4CADIntegration, searchResultFrame);
        mainSplitPane.setDividerSize(7);
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 2));
        getContentPane().add(mainToolBar, "North");
        getContentPane().add(mainSplitPane, "Center");
        getContentPane().add(DynaMOAD.buildStatusBar(statusField), "South");
        searchResultPanel = new DOSChangeable();
    }

    private void openModel()
    {
        try
        {
            modelOuid = dos.getWorkingModel();
            if(modelOuid == null)
            {
                JOptionPane.showMessageDialog(this, "Working Model\uC744 \uC120\uD0DD\uD55C \uD6C4 \uC791\uC5C5\uD558\uC2ED\uC2DC\uC624.", "Information", 1);
                _session.setProperty("dynapdm.part.search.status", "closed");
                _session.setProperty("dynapdm.part.search.instance", null);
                DynaMOAD.startFromCAD = false;
                dispose();
            } else
            {
                DOSChangeable dosModel = dos.getModel(modelOuid);
                TreeNodeObject rootNode = new TreeNodeObject((String)dosModel.get("ouid"), (String)dosModel.get("name"), (String)dosModel.get("description"));
                makeObjectSetTree(rootNode, 1);
                loadScripts();
            }
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public void clearSearchResult()
    {
        searchResultPanel.clear();
    }

    public void reloadSearchResult(String selectedOuid)
    {
        if(searchResultPanel.get(selectedOuid) != null)
            searchResult4CADIntegration.setResultData((ArrayList)searchResultPanel.get(selectedOuid));
    }

    public void makeObjectSetTree(TreeNodeObject rootnodedata, int i)
    {
        try
        {
            rootnodedata = rootnodedata;
            searchCondition4CADIntegration.setWorkSpace();
            searchCondition4CADIntegration.setTopNode(rootnodedata);
            DefaultTreeModel treemodel = (DefaultTreeModel)searchCondition4CADIntegration.getTree().getModel();
            modelOuid = rootnodedata.getOuid();
            dos.setWorkingModel(rootnodedata.getOuid());
            searchCondition4CADIntegration.setFullTreeExpand((DefaultMutableTreeNode)treemodel.getRoot(), modelOuid);
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

    public void loadScripts()
    {
        String scriptPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "script";
        String packageOuid = null;
        String packageName = null;
        String classOuid = null;
        String className = null;
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
        DOSChangeable aPackage = null;
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
            if(rootClassList == null || rootClassList.isEmpty())
            {
                file = null;
                return;
            }
            classMap = new HashMap();
            for(rootClassKey = rootClassList.iterator(); rootClassKey.hasNext();)
            {
                aClass = (DOSChangeable)rootClassKey.next();
                className = (String)aClass.get("name");
                packageOuid = (String)aClass.get("ouid@package");
                aPackage = dos.getPackage(packageOuid);
                packageName = (String)aPackage.get("name");
                if(packageName.equals("Foundation") && className.equals("FCADFile") && (cadType.equals("0") || cadType.equals("3") || cadType.equals("4") || cadType.equals("5")))
                {
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
            windowClosing(null);
        else
        if(command.equals("Save"))
            clearSearchResult();
        else
        if(command.equals("searchResultButton"))
        {
            if(searchResultFrame == null)
                return;
            Object object = searchResultFrame.getContent();
            if(object == null || !(object instanceof SearchResult4CADIntegration))
                return;
            SearchResult4CADIntegration searchResult = (SearchResult4CADIntegration)object;
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
            if(object == null || !(object instanceof SearchResult4CADIntegration))
                return;
            SearchResult4CADIntegration searchResult = (SearchResult4CADIntegration)object;
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
            if(object == null || !(object instanceof SearchResult4CADIntegration))
                return;
            SearchResult4CADIntegration searchResult = (SearchResult4CADIntegration)object;
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
            if(object == null || !(object instanceof SearchResult4CADIntegration))
                return;
            SearchResult4CADIntegration searchResult = (SearchResult4CADIntegration)object;
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
            int pageNum = searchCondition4CADIntegration.getPageNumber() - 1;
            searchCondition4CADIntegration.setPageNumber(pageNum);
            searchCondition4CADIntegration.searchButton_actionPerformed(e);
        } else
        if(command.equals("nextSearch"))
        {
            int pageNum = searchCondition4CADIntegration.getPageNumber() + 1;
            searchCondition4CADIntegration.setPageNumber(pageNum);
            searchCondition4CADIntegration.searchButton_actionPerformed(e);
        }
    }

    public void windowClosing(WindowEvent e)
    {
        _session.setProperty("dynapdm.part.search.status", "closed");
        dispose();
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

    public void windowOpened(WindowEvent e)
    {
        openModel();
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
    private UIManagement newUI;
    public SearchCondition4CADIntegration searchCondition4CADIntegration;
    public MInternalFrame searchResultFrame;
    public SearchResult4CADIntegration searchResult4CADIntegration;
    private JToolBar searchResultToolBar;
    private JButton selectAllButton;
    private JButton clearSelectionButton;
    private JButton saveResultButton;
    private ArrayList actionButtonList;
    private JButton previousSearchButton;
    private JButton nextSearchButton;
    private BorderLayout mainFrameBorderLayout;
    private JLabel statusField;
    private JToolBar mainToolBar;
    private JButton closeButton;
    public JSplitPane mainSplitPane;
    public String modelOuid;
    public String cadType;
    public static TreeNodeObject rootnodedata;
    private DOSChangeable searchResultPanel;
    private Session _session;
}