// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MultiSelectDialog.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DialogReturnCallback;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement

public class MultiSelectDialog extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public MultiSelectDialog(JFrame frame, boolean modal, DOSChangeable field, ArrayList dataList)
    {
        super(frame, modal);
        dos = null;
        aus = null;
        wfm = null;
        callback = null;
        this.field = null;
        selectedData = null;
        originalData = null;
        fieldOuidList = null;
        fieldNameList = null;
        type = 0;
        rowInt = -2;
        screenSize = null;
        windowSize = null;
        allClassData = null;
        allClassColumnNames = null;
        allClassColumnWidths = null;
        selectedColumnNames = null;
        selectedColumnWidths = null;
        buttonToolBar = null;
        selectButton = null;
        exitButton = null;
        gridBag = null;
        gridBagCon = null;
        searchPanel = null;
        centerPanel = null;
        verticalToolBar = null;
        selectionButton = null;
        deselectionButton = null;
        upwardButton = null;
        downwardButton = null;
        allClassTable = null;
        sourceScrollPane = null;
        selectedTable = null;
        selectedScrPane = null;
        fieldComboBox = null;
        valueTextField = null;
        this.field = field;
        type = ((Byte)field.get("type")).byteValue();
        dos = DynaMOAD.dos;
        wfm = DynaMOAD.wfm;
        aus = DynaMOAD.aus;
        originalData = dataList;
        setFont(frame.getFont());
        if(type == 24 || type == 25)
        {
            selectedData = (ArrayList)dataList.clone();
            makeCodeSourceTable();
            makeSelectedCodeTable();
        } else
        if(type == 16)
        {
            selectedData = (ArrayList)dataList.clone();
            makeObjectSourceTable();
            makeSelectedObjectTable();
        } else
        {
            selectedData = (ArrayList)dataList.clone();
            makeStringSourceTable();
            makeSelectedStringTable();
        }
        initialize();
    }

    public void initialize()
    {
        System.out.println("MultiSelectDialog");
        setSize(500, 300);
        setTitle("Multi Selection");
        addWindowListener(this);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
        gridBag = new GridBagLayout();
        gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        centerPanel = new JPanel();
        centerPanel.setLayout(gridBag);
        Dimension verticalToolBarSize = new Dimension(26, 300);
        verticalToolBar = new JPanel();
        verticalToolBar.setLayout(new BoxLayout(verticalToolBar, 1));
        verticalToolBar.setPreferredSize(verticalToolBarSize);
        verticalToolBar.setMaximumSize(verticalToolBarSize);
        verticalToolBar.setMinimumSize(verticalToolBarSize);
        verticalToolBarSize = null;
        selectionButton = new JButton();
        selectionButton.setIcon(new ImageIcon("icons/SelectOneRow.gif"));
        selectionButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectionButton.setActionCommand("Select");
        selectionButton.addActionListener(this);
        verticalToolBar.add(selectionButton);
        deselectionButton = new JButton();
        deselectionButton.setIcon(new ImageIcon("icons/DeselectOneRow.gif"));
        deselectionButton.setToolTipText("Deselect");
        deselectionButton.setActionCommand("Deselect");
        deselectionButton.addActionListener(this);
        verticalToolBar.add(deselectionButton);
        upwardButton = new JButton();
        upwardButton.setIcon(new ImageIcon("icons/Upward.gif"));
        upwardButton.setToolTipText("Upward");
        upwardButton.setActionCommand("Upward");
        upwardButton.addActionListener(this);
        verticalToolBar.add(upwardButton);
        downwardButton = new JButton();
        downwardButton.setIcon(new ImageIcon("icons/Downward.gif"));
        downwardButton.setToolTipText("Downward");
        downwardButton.setActionCommand("Downward");
        downwardButton.addActionListener(this);
        verticalToolBar.add(downwardButton);
        if(type == 24 || type == 16)
        {
            sourceScrollPane = UIFactory.createStrippedScrollPane(null);
            sourceScrollPane.getViewport().add(allClassTable.getTable());
            sourceScrollPane.getViewport().setPreferredSize(new Dimension(210, 150));
            sourceScrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        }
        selectedScrPane = UIFactory.createStrippedScrollPane(null);
        selectedScrPane.getViewport().add(selectedTable.getTable());
        selectedScrPane.getViewport().setPreferredSize(new Dimension(210, 150));
        selectedScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(searchPanel, gridBagCon);
        if(type == 24 || type == 16)
        {
            gridBagCon.weightx = 0.59999999999999998D;
            gridBagCon.weighty = 1.0D;
            gridBagCon.gridx = 0;
            gridBagCon.gridy = 1;
            gridBagCon.gridwidth = 1;
            gridBagCon.gridheight = 1;
            gridBag.setConstraints(sourceScrollPane, gridBagCon);
        }
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(verticalToolBar, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(selectedScrPane, gridBagCon);
        centerPanel.add(searchPanel);
        if(type == 24 || type == 16)
            centerPanel.add(sourceScrollPane);
        centerPanel.add(verticalToolBar);
        centerPanel.add(selectedScrPane);
        getContentPane().add(centerPanel, "Center");
        buttonToolBar = new JPanel();
        buttonToolBar.setLayout(new BoxLayout(buttonToolBar, 0));
        selectButton = new JButton(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setIcon(new ImageIcon("icons/ok_st_obj.gif"));
        selectButton.setActionCommand("Save");
        selectButton.addActionListener(this);
        exitButton = new JButton("Cancel");
        exitButton.setIcon(new ImageIcon("icons/Exit.gif"));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(selectButton);
        buttonToolBar.add(Box.createHorizontalStrut(10));
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "South");
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void makeCodeSourceTable()
    {
        try
        {
            allClassData = new ArrayList();
            allClassColumnNames = new ArrayList();
            allClassColumnWidths = new ArrayList();
            allClassColumnNames.add("Code");
            allClassColumnNames.add("Name");
            allClassColumnNames.add("Description");
            allClassColumnWidths.add(new Integer(70));
            allClassColumnWidths.add(new Integer(70));
            allClassColumnWidths.add(new Integer(70));
            allClassTable = new Table(allClassData, (ArrayList)allClassColumnNames.clone(), (ArrayList)allClassColumnWidths.clone(), 1, 79);
            allClassTable.setColumnSequence(new int[] {
                1, 2, 3
            });
            allClassTable.getTable().setRowSelectionAllowed(true);
            allClassTable.getTable().addMouseListener(this);
            allClassTable.setIndexColumn(0);
            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, 0));
            String fieldStrings[] = (String[])null;
            fieldNameList = new ArrayList();
            fieldNameList.add("codeitemid");
            fieldNameList.add("name");
            fieldNameList.add("description");
            fieldStrings = new String[3];
            fieldStrings[0] = "Code";
            fieldStrings[1] = "Name";
            fieldStrings[2] = "Description";
            fieldComboBox = new JComboBox(fieldStrings);
            fieldComboBox.setBorder(UIManagement.borderTextBoxEditable);
            fieldComboBox.setPreferredSize(new Dimension(100, 22));
            fieldComboBox.setMaximumSize(new Dimension(100, 22));
            searchPanel.add(fieldComboBox);
            searchPanel.add(Box.createHorizontalStrut(1));
            valueTextField = new JTextField();
            valueTextField.setPreferredSize(new Dimension(120, 22));
            valueTextField.setMaximumSize(new Dimension(900, 22));
            searchPanel.add(valueTextField);
            JButton searchButton = new JButton();
            searchButton.setActionCommand("code.search");
            searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
            searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "search", 3));
            searchButton.setMargin(new Insets(0, 0, 0, 0));
            searchButton.addActionListener(this);
            searchPanel.add(searchButton);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void makeSelectedCodeTable()
    {
        selectedColumnNames = new ArrayList();
        selectedColumnWidths = new ArrayList();
        selectedColumnNames.add("Code");
        selectedColumnNames.add("Name");
        selectedColumnNames.add("Description");
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
        selectedTable.setColumnSequence(new int[] {
            1, 2, 3
        });
        selectedTable.getTable().setRowSelectionAllowed(true);
        selectedTable.getTable().addMouseListener(this);
        selectedTable.setIndexColumn(0);
    }

    public void makeObjectSourceTable()
    {
        try
        {
            allClassData = new ArrayList();
            allClassColumnNames = new ArrayList();
            allClassColumnWidths = new ArrayList();
            for(int i = 0; i < 3; i++)
            {
                allClassColumnNames.add(null);
                allClassColumnWidths.add(new Integer(70));
            }

            String fieldStrings[] = (String[])null;
            ArrayList fieldList = dos.listFieldInClass((String)field.get("type.ouid@class"));
            Utils.sort(fieldList);
            fieldOuidList = new ArrayList();
            fieldNameList = new ArrayList();
            fieldStrings = new String[fieldList.size()];
            DOSChangeable fieldInfo = null;
            String fieldName = null;
            String fieldTitle = null;
            for(int n = 0; n < fieldList.size(); n++)
            {
                fieldInfo = (DOSChangeable)fieldList.get(n);
                fieldName = (String)fieldInfo.get("name");
                fieldTitle = DynaMOAD.getMSRString((String)fieldInfo.get("code"), (String)fieldInfo.get("title"), 0);
                fieldStrings[n] = fieldTitle;
                fieldOuidList.add((String)fieldInfo.get("ouid"));
                fieldNameList.add(fieldName);
                if(fieldName.equals("md$number"))
                    allClassColumnNames.set(0, fieldTitle);
                else
                if(fieldName.equals("md$description"))
                    allClassColumnNames.set(1, fieldTitle);
                else
                if(fieldName.equals("md$status"))
                    allClassColumnNames.set(2, fieldTitle);
            }

            allClassTable = new Table(allClassData, (ArrayList)allClassColumnNames.clone(), (ArrayList)allClassColumnWidths.clone(), 1, 79);
            allClassTable.setColumnSequence(new int[] {
                1, 2, 3
            });
            allClassTable.getTable().setRowSelectionAllowed(true);
            allClassTable.getTable().addMouseListener(this);
            allClassTable.setIndexColumn(0);
            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, 0));
            fieldComboBox = new JComboBox(fieldStrings);
            fieldComboBox.setBorder(UIManagement.borderTextBoxEditable);
            fieldComboBox.setPreferredSize(new Dimension(100, 22));
            fieldComboBox.setMaximumSize(new Dimension(100, 22));
            searchPanel.add(fieldComboBox);
            searchPanel.add(Box.createHorizontalStrut(1));
            valueTextField = new JTextField();
            valueTextField.setPreferredSize(new Dimension(120, 22));
            valueTextField.setMaximumSize(new Dimension(900, 22));
            searchPanel.add(valueTextField);
            JButton searchButton = new JButton();
            searchButton.setActionCommand("object.search");
            searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
            searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "search", 3));
            searchButton.setMargin(new Insets(0, 0, 0, 0));
            searchButton.addActionListener(this);
            searchPanel.add(searchButton);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void makeSelectedObjectTable()
    {
        selectedColumnNames = (ArrayList)allClassColumnNames.clone();
        selectedColumnWidths = new ArrayList();
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
        selectedTable.setColumnSequence(new int[] {
            1, 2, 3
        });
        selectedTable.getTable().setRowSelectionAllowed(true);
        selectedTable.getTable().addMouseListener(this);
        selectedTable.setIndexColumn(0);
    }

    public void makeStringSourceTable()
    {
        try
        {
            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, 0));
            valueTextField = new JTextField();
            valueTextField.setMinimumSize(new Dimension(210, 22));
            valueTextField.setPreferredSize(new Dimension(210, 22));
            valueTextField.setMaximumSize(new Dimension(900, 22));
            searchPanel.add(valueTextField);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void makeSelectedStringTable()
    {
        selectedColumnNames = new ArrayList();
        selectedColumnWidths = new ArrayList();
        selectedColumnNames.add(DynaMOAD.getMSRString((String)field.get("code"), (String)field.get("title"), 0));
        selectedColumnWidths.add(new Integer(210));
        selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
        selectedTable.setColumnSequence(new int[1]);
        selectedTable.getTable().setRowSelectionAllowed(true);
        selectedTable.getTable().addMouseListener(this);
        selectedTable.setIndexColumn(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            windowClosing(null);
        else
        if(command.equals("Save"))
        {
            originalData.clear();
            if(type == 24 || type == 16 || type == 25)
                originalData.addAll(selectedData);
            else
            if(selectedData != null)
                originalData.addAll(selectedData);
            originalData = null;
            selectedData = null;
            windowClosing(null);
        } else
        if(command.equals("Select"))
        {
            if(allClassTable == null)
            {
                if(valueTextField == null || Utils.isNullString(valueTextField.getText()))
                    return;
                String inputedString = valueTextField.getText();
                if(selectedData == null || selectedData.size() == 0)
                {
                    ArrayList selectedClassList = new ArrayList();
                    if(type == 4)
                        selectedClassList.add(Utils.getDouble(inputedString));
                    else
                    if(type == 6)
                        selectedClassList.add(Utils.getInteger(inputedString));
                    else
                    if(type == 2)
                        selectedClassList.add(Utils.getByte(inputedString));
                    else
                    if(type == 7)
                        selectedClassList.add(Utils.getLong(inputedString));
                    else
                    if(type == 8)
                        selectedClassList.add(Utils.getShort(inputedString));
                    else
                    if(type == 5)
                        selectedClassList.add(Utils.getFloat(inputedString));
                    else
                        selectedClassList.add(inputedString);
                    selectedData.add(selectedClassList.clone());
                    selectedClassList.clear();
                    selectedClassList = null;
                    selectedTable.changeTableData();
                } else
                {
                    ArrayList selectedDataClone = new ArrayList();
                    ArrayList selectedClassList = new ArrayList();
                    boolean isSame = false;
                    for(int j = 0; j < selectedData.size(); j++)
                    {
                        String tmpStr = ((ArrayList)selectedData.get(j)).get(0).toString();
                        if(tmpStr.equals(inputedString))
                            isSame = true;
                    }

                    if(!isSame)
                    {
                        if(type == 4)
                            selectedClassList.add(Utils.getDouble(inputedString));
                        else
                        if(type == 6)
                            selectedClassList.add(Utils.getInteger(inputedString));
                        else
                        if(type == 2)
                            selectedClassList.add(Utils.getByte(inputedString));
                        else
                        if(type == 7)
                            selectedClassList.add(Utils.getLong(inputedString));
                        else
                        if(type == 8)
                            selectedClassList.add(Utils.getShort(inputedString));
                        else
                        if(type == 5)
                            selectedClassList.add(Utils.getFloat(inputedString));
                        else
                            selectedClassList.add(inputedString);
                        selectedDataClone.add(selectedClassList.clone());
                        selectedClassList.clear();
                        selectedClassList = null;
                    }
                    for(int i = 0; i < selectedDataClone.size(); i++)
                        selectedData.add(selectedDataClone.get(i));

                    if(selectedDataClone.size() > 0)
                        selectedTable.changeTableData();
                }
            } else
            {
                if(allClassTable.getTable().getSelectedRowCount() <= 0)
                    return;
                int rows[] = allClassTable.getTable().getSelectedRows();
                ArrayList selectedList = allClassTable.getSelectedOuidRows(rows);
                int selrows[] = new int[selectedList.size()];
                for(int i = 0; i < selectedList.size(); i++)
                    selrows[i] = (new Integer((String)selectedList.get(i))).intValue();

                if(selectedData == null || selectedData.size() == 0)
                {
                    for(int i = 0; i < selrows.length; i++)
                    {
                        ArrayList allClassList = null;
                        ArrayList selectedClassList = new ArrayList();
                        allClassList = (ArrayList)allClassData.get(selrows[i]);
                        for(int j = 0; j < allClassList.size(); j++)
                            selectedClassList.add(allClassList.get(j));

                        selectedData.add(selectedClassList.clone());
                        selectedClassList.clear();
                        selectedClassList = null;
                    }

                    selectedTable.changeTableData();
                } else
                {
                    ArrayList selectedDataClone = new ArrayList();
                    for(int i = 0; i < selrows.length; i++)
                    {
                        ArrayList allClassList = null;
                        ArrayList selectedClassList = new ArrayList();
                        boolean isSame = false;
                        allClassList = (ArrayList)allClassData.get(selrows[i]);
                        for(int j = 0; j < selectedData.size(); j++)
                        {
                            String tmpStr = (String)((ArrayList)selectedData.get(j)).get(0);
                            if(tmpStr.equals(allClassList.get(0)))
                                isSame = true;
                        }

                        if(!isSame)
                        {
                            for(int j = 0; j < allClassList.size(); j++)
                                selectedClassList.add(allClassList.get(j));

                            selectedDataClone.add(selectedClassList.clone());
                            selectedClassList.clear();
                            selectedClassList = null;
                        }
                    }

                    for(int i = 0; i < selectedDataClone.size(); i++)
                        selectedData.add(selectedDataClone.get(i));

                    if(selectedDataClone.size() > 0)
                        selectedTable.changeTableData();
                }
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
        if(command.equals("object.search"))
        {
            ArrayList objectList = null;
            ArrayList aRow = null;
            ArrayList fields = null;
            HashMap filter = null;
            String searchText = null;
            String searchField = null;
            String classOuid = (String)field.get("type.ouid@class");
            if(Utils.isNullString(classOuid))
                return;
            fields = new ArrayList();
            for(int i = 0; i < 3; i++)
                fields.add(null);

            String fieldName = null;
            String fieldOuid = null;
            for(int i = 0; i < fieldOuidList.size(); i++)
            {
                fieldName = (String)fieldNameList.get(i);
                fieldOuid = (String)fieldOuidList.get(i);
                if(fieldName.equals("md$number"))
                    fields.set(0, fieldOuid);
                else
                if(fieldName.equals("md$description"))
                    fields.set(1, fieldOuid);
                else
                if(fieldName.equals("md$status"))
                    fields.set(2, fieldOuid);
            }

            searchText = valueTextField.getText();
            if(!Utils.isNullString(searchText))
            {
                filter = new HashMap();
                searchField = (String)fieldOuidList.get(fieldComboBox.getSelectedIndex());
                filter.put(searchField, searchText);
            }
            try
            {
                if(filter == null)
                {
                    objectList = dos.list(classOuid, fields);
                } else
                {
                    filter.put("search.result.count", "-1");
                    objectList = dos.list(classOuid, fields, filter);
                    filter.clear();
                    filter = null;
                }
                allClassData.clear();
                allClassTable.changeTableData();
                if(Utils.isNullArrayList(objectList))
                    return;
                for(Iterator objectKey = objectList.iterator(); objectKey.hasNext(); allClassData.add(aRow))
                    aRow = (ArrayList)objectKey.next();

                allClassTable.changeTableData();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(e);
            }
            fields.clear();
            fields = null;
        } else
        if(command.equals("code.search"))
        {
            ArrayList objectList = null;
            HashMap filter = null;
            ArrayList aRow = null;
            DOSChangeable aObject = null;
            String searchText = null;
            String searchField = null;
            String classOuid = (String)field.get("type.ouid@class");
            if(Utils.isNullString(classOuid))
                return;
            searchText = valueTextField.getText();
            if(!Utils.isNullString(searchText))
            {
                filter = new HashMap();
                searchField = (String)fieldNameList.get(fieldComboBox.getSelectedIndex());
                filter.put(searchField, searchText);
            }
            try
            {
                if(filter == null)
                {
                    objectList = dos.listCodeItem(classOuid);
                } else
                {
                    objectList = dos.listCodeItem(classOuid);
                    filter.clear();
                    filter = null;
                }
                allClassData.clear();
                allClassTable.changeTableData();
                if(Utils.isNullArrayList(objectList))
                    return;
                for(Iterator objectKey = objectList.iterator(); objectKey.hasNext();)
                {
                    aObject = (DOSChangeable)objectKey.next();
                    aRow = new ArrayList();
                    aRow.add(aObject.get("ouid"));
                    aRow.add(aObject.get("codeitemid"));
                    aRow.add(aObject.get("name"));
                    aRow.add(aObject.get("description"));
                    allClassData.add(aRow);
                    aRow = null;
                }

                allClassTable.changeTableData();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(e);
            }
        } else
        if(command.equals("Exit"))
        {
            if(type != 24 && type != 16 && type != 25 && originalData != null)
            {
                ArrayList tempList = new ArrayList();
                for(int x = 0; x < originalData.size(); x++)
                    tempList.add(((ArrayList)originalData.get(x)).get(0));

                originalData.clear();
                originalData.addAll(tempList);
                tempList = null;
            }
            windowClosing(null);
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
        removeWindowListener(this);
        callback = null;
        dos = null;
        dispose();
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

    public void mouseClicked(MouseEvent me)
    {
        Object source = me.getSource();
        if((type == 24 || type == 16 || type == 25) && source.equals(allClassTable.getTable()))
        {
            int row = allClassTable.getTable().getSelectedRow();
            String ouidRow = allClassTable.getSelectedOuidRow(row);
            allClassTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
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

    public void setDialogReturnCallback(DialogReturnCallback callback)
    {
        this.callback = callback;
    }

    private DOS dos;
    private AUS aus;
    private WFM wfm;
    private DialogReturnCallback callback;
    private DOSChangeable field;
    private ArrayList selectedData;
    private ArrayList originalData;
    private ArrayList fieldOuidList;
    private ArrayList fieldNameList;
    private byte type;
    private int rowInt;
    private Dimension screenSize;
    private Dimension windowSize;
    private ArrayList allClassData;
    private ArrayList allClassColumnNames;
    private ArrayList allClassColumnWidths;
    private ArrayList selectedColumnNames;
    private ArrayList selectedColumnWidths;
    private JPanel buttonToolBar;
    private JButton selectButton;
    private JButton exitButton;
    private GridBagLayout gridBag;
    private GridBagConstraints gridBagCon;
    private JPanel searchPanel;
    private JPanel centerPanel;
    private JPanel verticalToolBar;
    private JButton selectionButton;
    private JButton deselectionButton;
    private JButton upwardButton;
    private JButton downwardButton;
    private Table allClassTable;
    private JScrollPane sourceScrollPane;
    private Table selectedTable;
    private JScrollPane selectedScrPane;
    private JComboBox fieldComboBox;
    private JTextField valueTextField;
    final int offset = 3;
    final int init_xcord = 10;
    final int init_ycord = 10;
    final int titleWidth = 100;
    final int totalWidth = 260;
    final int fieldHeight = 20;
    final int columnWidth = 70;
}