// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AdvancedSearchResultSet.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.msr.MSRStgrep;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement

public class AdvancedSearchResultSet extends JDialog
    implements ActionListener, WindowListener, MouseListener
{
    class MyCellRenderer extends JLabel
        implements ListCellRenderer
    {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if(value != null && ((ArrayList)value).get(0) != null && ((ArrayList)value).get(2) != null)
                setText(((ArrayList)value).get(0).toString() + " - " + ((ArrayList)value).get(2).toString());
            setBackground(isSelected ? list.getSelectionBackground() : Color.white);
            setForeground(isSelected ? list.getSelectionForeground() : Color.black);
            return this;
        }

        public MyCellRenderer()
        {
            setOpaque(true);
        }
    }


    public AdvancedSearchResultSet(JDialog parent, DefaultComboBoxModel classListComboBoxModel, String initKey)
    {
        super(parent, true);
        this.classListComboBoxModel = null;
        classListRenderer = null;
        hoardData = new DOSChangeable();
        selectedClassIndex = null;
        selectedClassOuid = null;
        selectedClassName = null;
        selectedCheck = new DOSChangeable();
        msrStgrep = null;
        this.initKey = null;
        rowInt = -2;
        try
        {
            dos = DynaMOAD.dos;
            nds = DynaMOAD.nds;
            this.classListComboBoxModel = classListComboBoxModel;
            this.initKey = initKey;
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
        openButton = new JButton();
        saveButton = new JButton();
        closeButton = new JButton();
        openButton.setMargin(new Insets(0, 0, 0, 0));
        openButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        openButton.setToolTipText("Open");
        openButton.setActionCommand("Open");
        openButton.addActionListener(this);
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "Save", 3));
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(this);
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        mainToolBar.add(openButton);
        mainToolBar.add(saveButton);
        mainToolBar.add(closeButton);
        verticalToolBar = new JToolBar();
        verticalToolBar.setOrientation(1);
        verticalToolBar.setFloatable(false);
        selectionButton = new JButton();
        selectionButton.setMargin(new Insets(0, 0, 0, 0));
        selectionButton.setIcon(new ImageIcon(getClass().getResource("/icons/SelectOneRow.gif")));
        selectionButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectionButton.setActionCommand("Select");
        selectionButton.addActionListener(this);
        deselectionButton = new JButton();
        deselectionButton.setMargin(new Insets(0, 0, 0, 0));
        deselectionButton.setIcon(new ImageIcon(getClass().getResource("/icons/DeselectOneRow.gif")));
        deselectionButton.setToolTipText("Deselect");
        deselectionButton.setActionCommand("Deselect");
        deselectionButton.addActionListener(this);
        upwardButton = new JButton();
        upwardButton.setMargin(new Insets(0, 0, 0, 0));
        upwardButton.setIcon(new ImageIcon(getClass().getResource("/icons/Upward.gif")));
        upwardButton.setToolTipText("Upward");
        upwardButton.setActionCommand("Upward");
        upwardButton.addActionListener(this);
        downwardButton = new JButton();
        downwardButton.setMargin(new Insets(0, 0, 0, 0));
        downwardButton.setIcon(new ImageIcon(getClass().getResource("/icons/Downward.gif")));
        downwardButton.setToolTipText("Downward");
        downwardButton.setActionCommand("Downward");
        downwardButton.addActionListener(this);
        verticalToolBar.add(selectionButton);
        verticalToolBar.add(deselectionButton);
        verticalToolBar.add(upwardButton);
        verticalToolBar.add(downwardButton);
        allFieldScrPane = UIFactory.createStrippedScrollPane(null);
        allFieldScrPane.getViewport().add(allFieldTable.getTable());
        allFieldScrPane.getViewport().setPreferredSize(new Dimension(210, 150));
        selectedScrPane = UIFactory.createStrippedScrollPane(null);
        selectedScrPane.getViewport().add(selectedTable.getTable());
        selectedScrPane.getViewport().setPreferredSize(new Dimension(350, 150));
        classListRenderer = new MyCellRenderer();
        classListComboBox = new JComboBox(classListComboBoxModel);
        classListComboBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        classListComboBox.setDoubleBuffered(true);
        classListComboBox.setActionCommand("classCombo");
        classListComboBox.addActionListener(this);
        classListComboBox.setRenderer(classListRenderer);
        classListComboBox.setSelectedIndex(0);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(classListComboBox, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 4;
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
        centerPanel.add(classListComboBox);
        centerPanel.add(allFieldScrPane);
        centerPanel.add(verticalToolBar);
        centerPanel.add(selectedScrPane);
        getContentPane().add(mainToolBar, "North");
        getContentPane().add(centerPanel, "Center");
        addWindowListener(this);
        setSize(500, 300);
        setTitle("Advanced Search Result Setting");
    }

    public void makeAllFieldTable()
    {
        try
        {
            ArrayList tmpFieldData = new ArrayList();
            allFieldData = new ArrayList();
            allFieldColumnNames = new ArrayList();
            allFieldColumnWidths = new ArrayList();
            allFieldColumnNames.add("Title");
            allFieldColumnNames.add("Description");
            allFieldColumnNames.add("Name");
            allFieldColumnWidths.add(new Integer(70));
            allFieldColumnWidths.add(new Integer(70));
            allFieldColumnWidths.add(new Integer(70));
            if(!Utils.isNullString(selectedClassOuid))
            {
                DOSChangeable result = new DOSChangeable();
                ArrayList fieldList = new ArrayList();
                fieldList = dos.listFieldInClass(selectedClassOuid);
                ArrayList subClassList = new ArrayList();
                boolean subClassVersionable = false;
                DOSChangeable classInfo = new DOSChangeable();
                try
                {
                    classInfo = dos.getClass(selectedClassOuid);
                    subClassList = dos.listSubClass(selectedClassOuid);
                    if(subClassList != null)
                    {
                        for(int i = 0; i < subClassList.size(); i++)
                        {
                            DOSChangeable subClassDos = (DOSChangeable)subClassList.get(i);
                            if((Boolean)subClassDos.get("is.versionable") != null && ((Boolean)subClassDos.get("is.versionable")).booleanValue())
                                subClassVersionable = true;
                        }

                    }
                    DOSChangeable tmpDOS = new DOSChangeable();
                    int i = 0;
                    if(!Utils.getBoolean((Boolean)classInfo.get("is.versionable")) && !subClassVersionable)
                    {
                        tmpDOS.put("title", "Ouid");
                        tmpDOS.put("name", "Ouid");
                        tmpDOS.put("ouid", "sf$ouid");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                        tmpDOS.put("name", "Number");
                        tmpDOS.put("ouid", "md$number");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                        tmpDOS.put("name", "Description");
                        tmpDOS.put("ouid", "md$description");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                        tmpDOS.put("name", "Status");
                        tmpDOS.put("ouid", "md$status");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0025", "User", 3));
                        tmpDOS.put("name", "User");
                        tmpDOS.put("ouid", "md$user");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                        tmpDOS.put("name", "Created Date");
                        tmpDOS.put("ouid", "md$cdate");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                        tmpDOS.put("name", "Modified Date");
                        tmpDOS.put("ouid", "md$mdate");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                    } else
                    {
                        tmpDOS.put("title", "Ouid");
                        tmpDOS.put("name", "Ouid");
                        tmpDOS.put("ouid", "vf$ouid");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0020", "Number", 3));
                        tmpDOS.put("name", "Number");
                        tmpDOS.put("ouid", "md$number");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0021", "Description", 3));
                        tmpDOS.put("name", "Description");
                        tmpDOS.put("ouid", "md$description");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0023", "Version", 3));
                        tmpDOS.put("name", "Version");
                        tmpDOS.put("ouid", "vf$version");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0024", "Status", 3));
                        tmpDOS.put("name", "Status");
                        tmpDOS.put("ouid", "md$status");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0025", "User", 3));
                        tmpDOS.put("name", "User");
                        tmpDOS.put("ouid", "md$user");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0026", "Created Date", 3));
                        tmpDOS.put("name", "Created Date");
                        tmpDOS.put("ouid", "md$cdate");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        tmpDOS = new DOSChangeable();
                        i++;
                        tmpDOS.put("title", DynaMOAD.getMSRString("WRD_0027", "Modified Date", 3));
                        tmpDOS.put("name", "Modified Date");
                        tmpDOS.put("ouid", "md$mdate");
                        tmpDOS.put("description", "");
                        fieldList.add(i, tmpDOS);
                        tmpDOS = null;
                        i++;
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
                for(int i = 1; i < fieldList.size(); i++)
                {
                    result = (DOSChangeable)fieldList.get(i);
                    String titleValue = DynaMOAD.getMSRString((String)result.get("code"), (String)result.get("title"), 0);
                    tmpFieldData.add((String)result.get("ouid"));
                    tmpFieldData.add(titleValue);
                    tmpFieldData.add((String)result.get("description"));
                    tmpFieldData.add((String)result.get("name"));
                    tmpFieldData.add((Byte)result.get("type"));
                    selectedCheck.put(titleValue, new Integer(i));
                    allFieldData.add(tmpFieldData.clone());
                    tmpFieldData.clear();
                }

            }
            allFieldTable = new Table(allFieldData, (ArrayList)allFieldColumnNames.clone(), (ArrayList)allFieldColumnWidths.clone(), 1, 79);
            allFieldTable.setColumnSequence(new int[] {
                1, 2, 3
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
        ArrayList tmpFieldData = new ArrayList();
        selectedData = new ArrayList();
        selectedColumnNames = new ArrayList();
        selectedColumnWidths = new ArrayList();
        selectedColumnNames.add("Index");
        selectedColumnNames.add("Class");
        selectedColumnNames.add("Title");
        selectedColumnNames.add("Description");
        selectedColumnNames.add("Name");
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
        selectedTable.setColumnSequence(new int[] {
            0, 2, 4, 5, 6
        });
        selectedTable.getTable().setRowSelectionAllowed(true);
        selectedTable.getTable().addMouseListener(this);
        selectedTable.setIndexColumn(0);
    }

    public void closeEvent()
    {
        openButton.removeActionListener(this);
        saveButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        selectionButton.removeActionListener(this);
        deselectionButton.removeActionListener(this);
        upwardButton.removeActionListener(this);
        downwardButton.removeActionListener(this);
        dispose();
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
            if(command.equals("Save"))
            {
                nds.removeNode(initKey + "/RESULTFIELD");
                nds.addNode(initKey, "RESULTFIELD", "LINKFILTER", "RESULTFIELD");
                for(int i = 0; i < selectedData.size(); i++)
                {
                    String value = (String)((ArrayList)selectedData.get(i)).get(0) + "/" + (String)((ArrayList)selectedData.get(i)).get(3);
                    String index = null;
                    if(i < 10)
                        index = "0" + (new Integer(i)).toString();
                    else
                        index = (new Integer(i)).toString();
                    nds.addNode(initKey + "/RESULTFIELD", index, "LINKFILTER", value);
                }

                closeEvent();
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
                        selectedFieldList.add(selectedClassIndex);
                        selectedFieldList.add(selectedClassOuid);
                        selectedFieldList.add(selectedClassName);
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
                            selectedFieldList.add(selectedClassIndex);
                            selectedFieldList.add(selectedClassOuid);
                            selectedFieldList.add(selectedClassName);
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
            else
            if(command.equals("classCombo"))
            {
                int index = classListComboBox.getSelectedIndex();
                if(index > -1)
                {
                    ArrayList tmpList = (ArrayList)classListComboBoxModel.getElementAt(index);
                    selectedClassIndex = (String)tmpList.get(0);
                    selectedClassOuid = (String)tmpList.get(1);
                    selectedClassName = (String)tmpList.get(2);
                    makeAllFieldTable();
                    allFieldScrPane.setViewportView(allFieldTable.getTable());
                }
            }
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
    private BorderLayout mainFrameBorderLayout;
    private JToolBar mainToolBar;
    private JButton openButton;
    private JButton saveButton;
    private JButton closeButton;
    private Table allFieldTable;
    private JScrollPane allFieldScrPane;
    private Table selectedTable;
    private JScrollPane selectedScrPane;
    private JToolBar verticalToolBar;
    private JButton selectionButton;
    private JButton deselectionButton;
    private JButton upwardButton;
    private JButton downwardButton;
    private JComboBox classListComboBox;
    private DefaultComboBoxModel classListComboBoxModel;
    private MyCellRenderer classListRenderer;
    private ArrayList groupList;
    private ArrayList fieldUIList;
    private DOSChangeable fieldUIData;
    private DOSChangeable hoardData;
    private String selectedClassIndex;
    private String selectedClassOuid;
    private String selectedClassName;
    private DOSChangeable selectedCheck;
    private MSRStgrep msrStgrep;
    private String initKey;
    private ArrayList allFieldData;
    private ArrayList allFieldColumnNames;
    private ArrayList allFieldColumnWidths;
    private ArrayList selectedData;
    private ArrayList selectedColumnNames;
    private ArrayList selectedColumnWidths;
    private int rowInt;
    final int offset = 3;
    final int init_xcord = 10;
    final int init_ycord = 10;
    final int titleWidth = 100;
    final int totalWidth = 260;
    final int fieldHeight = 20;
    final byte DATATYPE_OBJECT = 16;
    final byte DATATYPE_DATETIME = 21;
    final byte DATATYPE_DATE = 22;
    final int columnWidth = 70;
    private final String NDS_BASE = "::/WORKSPACE";
    final int RESULT_FIELD = 1;
    final int SEARCH_CONDITION = 2;
}