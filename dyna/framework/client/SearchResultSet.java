// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SearchResultSet.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, LogIn, MainFrame, SearchCondition, 
//            SearchResult, UIManagement

public class SearchResultSet extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public SearchResultSet(MainFrame main, String ouid, int option)
    {
        super(main, false);
        classOuid = "";
        selectedCheck = new DOSChangeable();
        settingOption = 0;
        rowInt = -2;
        try
        {
            mainFrame = main;
            classOuid = ouid;
            settingOption = option;
            dos = DynaMOAD.dos;
            nds = DynaMOAD.nds;
            newUI = DynaMOAD.newUI;
            makeAllFieldTable();
            makeSelectedFieldTable();
            initialize();
            setVisible(true);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        mainFrameBorderLayout = new BorderLayout();
        getContentPane().setLayout(mainFrameBorderLayout);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(gridBag);
        mainToolBar = new JToolBar();
        resetButton = new JButton();
        saveButton = new JButton();
        resetButton.setMargin(new Insets(0, 0, 0, 0));
        resetButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clean.gif")));
        resetButton.setToolTipText(DynaMOAD.getMSRString("WRD_0157", "Initialize", 3));
        resetButton.setActionCommand("Initialize");
        resetButton.addActionListener(this);
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(this);
        mainToolBar.add(resetButton);
        mainToolBar.add(saveButton);
        verticalToolBar = new JToolBar();
        verticalToolBar.setOrientation(1);
        verticalToolBar.setFloatable(false);
        verticalToolBar.setPreferredSize(new Dimension(26, 300));
        verticalToolBar.setMaximumSize(new Dimension(26, 300));
        verticalToolBar.setMinimumSize(new Dimension(26, 300));
        selectionButton = new JButton();
        deselectionButton = new JButton();
        upwardButton = new JButton();
        downwardButton = new JButton();
        selectionButton.setMargin(new Insets(0, 0, 0, 0));
        selectionButton.setIcon(new ImageIcon(getClass().getResource("/icons/Forward16.gif")));
        selectionButton.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Select", 3));
        selectionButton.setActionCommand("Select");
        selectionButton.addActionListener(this);
        deselectionButton.setMargin(new Insets(0, 0, 0, 0));
        deselectionButton.setIcon(new ImageIcon(getClass().getResource("/icons/Back16.gif")));
        deselectionButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Deselect", 3));
        deselectionButton.setActionCommand("Deselect");
        deselectionButton.addActionListener(this);
        upwardButton.setMargin(new Insets(0, 0, 0, 0));
        upwardButton.setIcon(new ImageIcon(getClass().getResource("/icons/Upward.gif")));
        upwardButton.setToolTipText(DynaMOAD.getMSRString("WRD_0122", "Upward", 3));
        upwardButton.setActionCommand("Upward");
        upwardButton.addActionListener(this);
        downwardButton.setMargin(new Insets(0, 0, 0, 0));
        downwardButton.setIcon(new ImageIcon(getClass().getResource("/icons/Downward.gif")));
        downwardButton.setToolTipText(DynaMOAD.getMSRString("WRD_0123", "Downward", 3));
        downwardButton.setActionCommand("Downward");
        downwardButton.addActionListener(this);
        verticalToolBar.add(selectionButton);
        verticalToolBar.add(deselectionButton);
        verticalToolBar.add(upwardButton);
        verticalToolBar.add(downwardButton);
        allFieldScrPane = UIFactory.createStrippedScrollPane(null);
        allFieldScrPane.getViewport().add(allFieldTable.getTable());
        allFieldScrPane.getViewport().setPreferredSize(new Dimension(300, 150));
        selectedScrPane = UIFactory.createStrippedScrollPane(null);
        selectedScrPane.getViewport().add(selectedTable.getTable());
        selectedScrPane.getViewport().setPreferredSize(new Dimension(300, 150));
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(allFieldScrPane, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(verticalToolBar, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(selectedScrPane, gridBagCon);
        centerPanel.add(allFieldScrPane);
        centerPanel.add(verticalToolBar);
        centerPanel.add(selectedScrPane);
        getContentPane().add(mainToolBar, "North");
        getContentPane().add(centerPanel, "Center");
        addWindowListener(this);
        setSize(480, 300);
        getClass();
        if(settingOption == 1)
            setTitle(DynaMOAD.getMSRString("WRD_0124", "Search Result Setting", 3));
        else
            setTitle(DynaMOAD.getMSRString("WRD_0125", "Search Condition Setting", 3));
    }

    public void makeAllFieldTable()
    {
        try
        {
            ArrayList tmpFieldData = new ArrayList();
            allFieldData = new ArrayList();
            allFieldColumnNames = new ArrayList();
            allFieldColumnWidths = new ArrayList();
            allFieldColumnNames.add(DynaMOAD.getMSRString("name", "Title", 3));
            allFieldColumnNames.add(DynaMOAD.getMSRString("description", "Description", 3));
            allFieldColumnWidths.add(new Integer(100));
            allFieldColumnWidths.add(new Integer(100));
            DOSChangeable result = new DOSChangeable();
            ArrayList fieldList = dos.listFieldInClass(classOuid);
            Utils.sort(fieldList);
            ArrayList subClassList = null;
            boolean subClassVersionable = false;
            DOSChangeable classInfo = null;
            int i;
            try
            {
                classInfo = dos.getClass(classOuid);
                subClassList = dos.listSubClass(classOuid);
                if(subClassList != null)
                    for(i = 0; i < subClassList.size(); i++)
                    {
                        DOSChangeable subClassDos = (DOSChangeable)subClassList.get(i);
                        if((Boolean)subClassDos.get("is.versionable") != null && ((Boolean)subClassDos.get("is.versionable")).booleanValue())
                            subClassVersionable = true;
                    }

            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
            ie = 0;
            int j = 0;
            for(; ie < fieldList.size(); ie++)
            {
                result = (DOSChangeable)fieldList.get(ie);
                if(!Utils.getBoolean((Boolean)result.get("is.visible")))
                {
                    result = null;
                } else
                {
                    DOSChangeable tmpDOS = new DOSChangeable();
                    if((Utils.getBoolean((Boolean)classInfo.get("is.versionable")) || subClassVersionable) && result.get("name").equals("vf$version"))
                    {
                        getClass();
                        if(settingOption == 2)
                        {
                            tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                            tmpDOS.put("name", "Version Type");
                            tmpDOS.put("ouid", "version.condition.type");
                            tmpDOS.put("index", new Integer(-985));
                            tmpDOS.put("type", new Byte((byte)13));
                            tmpDOS.put("description", "");
                            tmpDOS.put("is.visible", Boolean.TRUE);
                            fieldList.add(ie + 1, tmpDOS);
                        }
                    }
                    String titleValue = DynaMOAD.getMSRString((String)result.get("code"), (String)result.get("title"), 0);
                    tmpFieldData.add(titleValue);
                    selectedCheck.put(titleValue, new Integer(j + 1));
                    tmpFieldData.add((String)result.get("description"));
                    tmpFieldData.add((String)result.get("name"));
                    tmpFieldData.add((String)result.get("ouid"));
                    tmpFieldData.add((Byte)result.get("type"));
                    allFieldData.add(tmpFieldData.clone());
                    tmpFieldData.clear();
                    j++;
                }
            }

            allFieldTable = new Table(allFieldData, (ArrayList)allFieldColumnNames.clone(), (ArrayList)allFieldColumnWidths.clone(), 1, 79);
            allFieldTable.setColumnSequence(new int[] {
                0, 1
            });
            allFieldTable.getTable().setRowSelectionAllowed(true);
            allFieldTable.getTable().addMouseListener(this);
            allFieldTable.setIndexColumn(0);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void makeSelectedFieldTable()
    {
        try
        {
            selectedData = new ArrayList();
            selectedColumnNames = new ArrayList();
            selectedColumnWidths = new ArrayList();
            selectedColumnNames.add(DynaMOAD.getMSRString("name", "Title", 3));
            selectedColumnNames.add(DynaMOAD.getMSRString("description", "Description", 3));
            selectedColumnWidths.add(new Integer(100));
            selectedColumnWidths.add(new Integer(100));
            ArrayList fieldList = null;
            String key = null;
            String fieldOuid = null;
            String fieldTitle = null;
            DOSChangeable fieldData = null;
            ArrayList rowData = new ArrayList();
            getClass();
            if(settingOption == 1)
            {
                key = "::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid;
            } else
            {
                getClass();
                if(settingOption == 2)
                    key = "::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid;
            }
            fieldList = nds.getChildNodeList(key);
            if(fieldList != null)
            {
                for(int i = 0; i < fieldList.size(); i++)
                {
                    fieldOuid = nds.getValue(key + "/" + (String)fieldList.get(i));
                    getClass();
                    if(settingOption == 2 && fieldOuid.equals("version.condition.type"))
                    {
                        rowData.add(DynaMOAD.getMSRString("WRD_0022", "Version Type", 3));
                        rowData.add("");
                        rowData.add("Version Type");
                        rowData.add("version.condition.type");
                        rowData.add(new Byte((byte)13));
                    } else
                    {
                        fieldData = dos.getField(fieldOuid);
                        fieldTitle = DynaMOAD.getMSRString((String)fieldData.get("code"), (String)fieldData.get("title"), 0);
                        rowData.add(fieldTitle);
                        rowData.add(fieldData.get("description"));
                        rowData.add(fieldData.get("name"));
                        rowData.add(fieldData.get("ouid"));
                        rowData.add(fieldData.get("type"));
                        selectedData.add(rowData.clone());
                        rowData.clear();
                    }
                }

            }
            selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
            selectedTable.setColumnSequence(new int[] {
                0, 1
            });
            selectedTable.getTable().setRowSelectionAllowed(true);
            selectedTable.getTable().addMouseListener(this);
            selectedTable.setIndexColumn(0);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void closeEvent()
    {
        resetButton.removeActionListener(this);
        saveButton.removeActionListener(this);
        selectionButton.removeActionListener(this);
        deselectionButton.removeActionListener(this);
        upwardButton.removeActionListener(this);
        downwardButton.removeActionListener(this);
        dispose();
        mainFrame.searchCondition.setSearchConditionAndResultToNull();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            String command = actionEvent.getActionCommand();
            if(command == null)
                closeEvent();
            else
            if(command.equals("Initialize"))
            {
                getClass();
                if(settingOption == 1)
                {
                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
                    mainFrame.searchResult.makeSearchResultTable(classOuid);
                    closeEvent();
                } else
                {
                    getClass();
                    if(settingOption == 2)
                    {
                        nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid);
                        mainFrame.searchCondition.advancedPanelInit();
                        closeEvent();
                    }
                }
            } else
            if(command.equals("Save"))
            {
                ArrayList rowData = null;
                String fieldOuid = null;
                Integer index = null;
                getClass();
                if(settingOption == 1)
                {
                    nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid);
                    if(selectedData.size() > 0)
                    {
                        nds.addNode("::", "WORKSPACE", "RESULTSET", "");
                        nds.addNode("::/WORKSPACE", LogIn.userID, "RESULTSET", "USERID");
                        nds.addNode("::/WORKSPACE/" + LogIn.userID, "RESULTFIELD", "RESULTSET", "RESULTFIELD");
                        nds.addNode("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD", classOuid, "RESULTSET", classOuid);
                        for(int i = 0; i < selectedData.size(); i++)
                        {
                            rowData = (ArrayList)selectedData.get(i);
                            fieldOuid = (String)rowData.get(3);
                            index = new Integer(i);
                            if(index.toString().length() == 1)
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid, "0" + index.toString(), "RESULTSET", fieldOuid);
                            else
                                nds.addNode("::/WORKSPACE/" + LogIn.userID + "/RESULTFIELD/" + classOuid, index.toString(), "RESULTSET", fieldOuid);
                        }

                    }
                    mainFrame.searchResult.makeSearchResultTable(classOuid);
                    closeEvent();
                } else
                {
                    getClass();
                    if(settingOption == 2)
                    {
                        nds.removeNode("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid);
                        if(selectedData.size() > 0)
                        {
                            nds.addNode("::", "WORKSPACE", "CONDITIONSET", "");
                            nds.addNode("::/WORKSPACE", LogIn.userID, "CONDITIONSET", "USERID");
                            nds.addNode("::/WORKSPACE/" + LogIn.userID, "CONDITION", "CONDITIONSET", "CONDITION");
                            nds.addNode("::/WORKSPACE/" + LogIn.userID + "/CONDITION", classOuid, "CONDITIONSET", classOuid);
                            for(int i = 0; i < selectedData.size(); i++)
                            {
                                rowData = (ArrayList)selectedData.get(i);
                                fieldOuid = (String)rowData.get(3);
                                index = new Integer(i);
                                if(index.toString().length() == 1)
                                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid, "0" + index.toString(), "CONDITIONSET", fieldOuid);
                                else
                                    nds.addNode("::/WORKSPACE/" + LogIn.userID + "/CONDITION/" + classOuid, index.toString(), "CONDITIONSET", fieldOuid);
                            }

                        }
                        mainFrame.searchCondition.advancedPanelInit();
                        closeEvent();
                    }
                }
            } else
            if(command.equals("Select"))
            {
                if(allFieldTable.getTable().getSelectedRowCount() <= 0)
                    return;
                int rows[] = allFieldTable.getTable().getSelectedRows();
                ArrayList selectedList = allFieldTable.getSelectedOuidRows(rows);
                int selrows[] = new int[selectedList.size()];
                for(int i = 0; i < selectedList.size(); i++)
                    selrows[i] = (new Integer((String)selectedList.get(i))).intValue();

                if(selectedData == null || selectedData.size() == 0)
                {
                    for(int i = 0; i < selrows.length; i++)
                    {
                        ArrayList allFieldList = new ArrayList();
                        ArrayList selectedFieldList = new ArrayList();
                        allFieldList = (ArrayList)allFieldData.get(selrows[i]);
                        selectedFieldList.add(allFieldList.get(0));
                        selectedFieldList.add(allFieldList.get(1));
                        selectedFieldList.add(allFieldList.get(2));
                        selectedFieldList.add(allFieldList.get(3));
                        selectedFieldList.add(allFieldList.get(4));
                        selectedData.add(selectedFieldList.clone());
                    }

                    selectedTable.changeTableData();
                } else
                {
                    ArrayList selectedDataClone = new ArrayList();
                    for(int i = 0; i < selrows.length; i++)
                    {
                        ArrayList allFieldList = new ArrayList();
                        ArrayList selectedFieldList = new ArrayList();
                        boolean isSame = false;
                        allFieldList = (ArrayList)allFieldData.get(selrows[i]);
                        for(int j = 0; j < selectedData.size(); j++)
                        {
                            String tmpStr = (String)((ArrayList)selectedData.get(j)).get(0);
                            if(tmpStr.equals(allFieldList.get(0)))
                                isSame = true;
                        }

                        if(!isSame)
                        {
                            selectedFieldList.add(allFieldList.get(0));
                            selectedFieldList.add(allFieldList.get(1));
                            selectedFieldList.add(allFieldList.get(2));
                            selectedFieldList.add(allFieldList.get(3));
                            selectedFieldList.add(allFieldList.get(4));
                            selectedDataClone.add(selectedFieldList.clone());
                        }
                    }

                    for(int i = 0; i < selectedDataClone.size(); i++)
                        selectedData.add(selectedDataClone.get(i));

                    if(selectedDataClone.size() > 0)
                        selectedTable.changeTableData();
                }
            } else
            if(command.equals("Deselect"))
            {
                if(selectedTable.getTable().getSelectedRowCount() <= 0)
                    return;
                int selrows[] = selectedTable.getTable().getSelectedRows();
                for(int i = selrows.length - 1; i >= 0; i--)
                    selectedData.remove(selrows[i]);

                selectedTable.changeTableData();
                if(selectedData.size() > selrows[0])
                    selectedTable.getTable().setRowSelectionInterval(selrows[0], selrows[0]);
            } else
            if(command.equals("Upward"))
            {
                if(selectedTable.getTable().getSelectedRowCount() != 1)
                    return;
                int selrow = selectedTable.getTable().getSelectedRow();
                if(selrow > 0)
                {
                    ArrayList tmp1 = (ArrayList)selectedData.get(selrow - 1);
                    ArrayList tmp2 = (ArrayList)selectedData.get(selrow);
                    selectedData.remove(selrow - 1);
                    selectedData.remove(selrow - 1);
                    selectedData.add(selrow - 1, tmp2);
                    selectedData.add(selrow, tmp1);
                    selectedTable.changeTableData();
                    selectedTable.getTable().setRowSelectionInterval(selrow - 1, selrow - 1);
                    rowInt = selrow - 1;
                }
            } else
            if(command.equals("Downward"))
            {
                if(selectedTable.getTable().getSelectedRowCount() != 1)
                    return;
                int selrow = selectedTable.getTable().getSelectedRow();
                if(selrow < selectedData.size() - 1)
                {
                    ArrayList tmp1 = (ArrayList)selectedData.get(selrow);
                    ArrayList tmp2 = (ArrayList)selectedData.get(selrow + 1);
                    selectedData.remove(selrow);
                    selectedData.remove(selrow);
                    selectedData.add(selrow, tmp2);
                    selectedData.add(selrow + 1, tmp1);
                    selectedTable.changeTableData();
                    selectedTable.getTable().setRowSelectionInterval(selrow + 1, selrow + 1);
                }
            } else
            if(command.equals("Close"))
                closeEvent();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public int getSelectRow()
    {
        if(rowInt == -2)
            return selectedTable.getTable().getSelectedRow();
        else
            return rowInt;
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        closeEvent();
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

    public void windowClosed(WindowEvent windowEvent)
    {
        removeWindowListener(this);
    }

    public void windowOpened(WindowEvent windowevent)
    {
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
        Object source = mouseEvent.getSource();
        if(source.equals(allFieldTable.getTable()))
        {
            int row = allFieldTable.getTable().getSelectedRow();
            String ouidRow = allFieldTable.getSelectedOuidRow(row);
            allFieldTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        } else
        {
            int row = selectedTable.getTable().getSelectedRow();
            String ouidRow = selectedTable.getSelectedOuidRow(row);
            selectedTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private DOS dos;
    private NDS nds;
    private UIManagement newUI;
    private MainFrame mainFrame;
    private BorderLayout mainFrameBorderLayout;
    private JToolBar mainToolBar;
    private JButton resetButton;
    private JButton saveButton;
    private Table allFieldTable;
    private JScrollPane allFieldScrPane;
    private Table selectedTable;
    private JScrollPane selectedScrPane;
    private JToolBar verticalToolBar;
    private JButton selectionButton;
    private JButton deselectionButton;
    private JButton upwardButton;
    private JButton downwardButton;
    private String classOuid;
    private DOSChangeable selectedCheck;
    private int settingOption;
    private ArrayList allFieldData;
    private ArrayList allFieldColumnNames;
    private ArrayList allFieldColumnWidths;
    private ArrayList selectedData;
    private ArrayList selectedColumnNames;
    private ArrayList selectedColumnWidths;
    private int rowInt;
    private final byte DATATYPE_UTF = 13;
    private final int columnWidth = 100;
    private final int RESULT_FIELD = 1;
    private final int SEARCH_CONDITION = 2;
}