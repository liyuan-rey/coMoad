// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CodeSelectionMatrixDialog.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.CodeSelectDialog;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, TypeClassSelection

public class CodeSelectionMatrixDialog extends JDialog
    implements ActionListener, MouseListener
{
    private class CodeComboLoader extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            ArrayList tempList = null;
            ArrayList list = null;
            ArrayList codeList = null;
            DOSChangeable code = null;
            try
            {
                codeList = dos.listCode();
                if(Utils.isNullArrayList(codeList))
                    return new ArrayList();
                list = new ArrayList(codeList.size());
                for(int i = 0; i < codeList.size(); i++)
                {
                    code = (DOSChangeable)codeList.get(i);
                    tempList = new ArrayList(2);
                    tempList.add(code.get("ouid"));
                    tempList.add(code.get("name"));
                    list.add(tempList);
                    tempList = null;
                    code = null;
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return list;
        }

        CodeComboLoader()
        {
        }
    }

    private class FieldComboLoader extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            ArrayList tempList = null;
            ArrayList list = null;
            ArrayList codeList = null;
            DOSChangeable code = null;
            Byte type = null;
            try
            {
                codeList = dos.listFieldInClass(classOuid);
                if(Utils.isNullArrayList(codeList))
                    return new ArrayList();
                list = new ArrayList(codeList.size());
                for(int i = 0; i < codeList.size(); i++)
                {
                    code = (DOSChangeable)codeList.get(i);
                    type = (Byte)code.get("type");
                    if(type.byteValue() != 24 && type.byteValue() != 25)
                    {
                        code = null;
                    } else
                    {
                        tempList = new ArrayList(2);
                        tempList.add(code.get("ouid"));
                        if(code.get("title") != null)
                            tempList.add(code.get("name") + " [" + code.get("title") + "]");
                        tempList.add(code.get("name"));
                        list.add(tempList);
                        tempList = null;
                        code = null;
                    }
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return list;
        }

        FieldComboLoader()
        {
        }
    }


    public CodeSelectionMatrixDialog(Frame owner, boolean modal, String classOuid, String fieldOuid)
        throws HeadlessException
    {
        super(owner, modal);
        this.owner = null;
        dos = null;
        this.classOuid = null;
        this.fieldOuid = null;
        tableData = null;
        valueOuid = null;
        initialOuid = null;
        splitPane = null;
        scrollPane = null;
        table = null;
        infoPanel = null;
        toolBar = null;
        addButton = null;
        removeButton = null;
        centerPanel = null;
        numberTextField = null;
        codeComboBox = null;
        codeComboLoader = new CodeComboLoader();
        codeComboModel = new DynaComboBoxModel(codeComboLoader);
        fieldComboBox = null;
        fieldComboLoader = new FieldComboLoader();
        fieldComboModel = new DynaComboBoxModel(fieldComboLoader);
        valueTextField = null;
        valueButton = null;
        initialTextField = null;
        initialButton = null;
        bottomPanel = null;
        checkBox = null;
        okButton = null;
        closeButton = null;
        this.owner = owner;
        this.classOuid = classOuid;
        this.fieldOuid = fieldOuid;
        dos = ObjectModelingConstruction.dos;
        init();
        loadMatrix();
    }

    public void init()
    {
        setTitle("Code Selection Matrix");
        splitPane = new JSplitPane(0);
        getContentPane().add(splitPane);
        scrollPane = UIFactory.createStrippedScrollPane(null);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        splitPane.setTopComponent(scrollPane);
        makeTable();
        scrollPane.setViewportView(table.getTable());
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(500, 200));
        splitPane.setBottomComponent(infoPanel);
        toolBar = new JToolBar(0);
        infoPanel.add(toolBar, "North");
        addButton = new JButton("Add");
        addButton.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/add_att.gif")));
        addButton.setActionCommand("Add");
        addButton.addActionListener(this);
        toolBar.add(addButton);
        removeButton = new JButton("Remove");
        removeButton.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/remove_att.gif")));
        removeButton.setActionCommand("Remove");
        removeButton.addActionListener(this);
        toolBar.add(removeButton);
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, 1));
        infoPanel.add(centerPanel, "Center");
        numberTextField = new DynaTextField();
        numberTextField.setTitleText("Number");
        numberTextField.setTitleWidth(80);
        numberTextField.setTitleVisible(true);
        numberTextField.setMandatory(true);
        centerPanel.add(numberTextField);
        codeComboBox = new DynaComboBox();
        codeComboBox.setModel(codeComboModel);
        codeComboBox.setTitleText("Code");
        codeComboBox.setTitleWidth(80);
        codeComboBox.setTitleVisible(true);
        codeComboBox.setMandatory(true);
        codeComboModel.enableDataLoad();
        centerPanel.add(codeComboBox);
        initialTextField = new DynaTextField();
        initialTextField.setTitleText("Initial Value");
        initialTextField.setTitleWidth(80);
        initialTextField.setTitleVisible(true);
        initialTextField.setEditable(false);
        centerPanel.add(initialTextField);
        initialButton = new JButton();
        initialButton.setActionCommand("initial");
        initialButton.addActionListener(this);
        initialButton.setPreferredSize(new Dimension(18, 22));
        initialButton.setMinimumSize(new Dimension(18, 22));
        initialButton.setMaximumSize(new Dimension(18, 22));
        initialButton.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
        initialTextField.add(initialButton);
        fieldComboBox = new DynaComboBox();
        fieldComboBox.setModel(fieldComboModel);
        fieldComboBox.setTitleText("Field");
        fieldComboBox.setTitleWidth(80);
        fieldComboBox.setTitleVisible(true);
        fieldComboBox.setMandatory(true);
        fieldComboBox.setActionCommand("Field");
        fieldComboBox.addActionListener(this);
        fieldComboModel.enableDataLoad();
        centerPanel.add(fieldComboBox);
        valueTextField = new DynaTextField();
        valueTextField.setTitleText("Value");
        valueTextField.setTitleWidth(80);
        valueTextField.setTitleVisible(true);
        valueTextField.setEditable(false);
        centerPanel.add(valueTextField);
        valueButton = new JButton();
        valueButton.setActionCommand("value");
        valueButton.addActionListener(this);
        valueButton.setPreferredSize(new Dimension(18, 22));
        valueButton.setMinimumSize(new Dimension(18, 22));
        valueButton.setMaximumSize(new Dimension(18, 22));
        valueButton.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
        valueTextField.add(valueButton);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, 0));
        infoPanel.add(bottomPanel, "South");
        checkBox = new JCheckBox("ComboBox Type");
        bottomPanel.add(checkBox);
        bottomPanel.add(Box.createGlue());
        okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        bottomPanel.add(okButton);
        closeButton = new JButton("Close");
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        bottomPanel.add(closeButton);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
    }

    private void makeTable()
    {
        ArrayList columnNames = new ArrayList();
        ArrayList columnWidths = new ArrayList();
        columnNames.add("No.");
        columnNames.add("Code");
        columnNames.add("Init. Value");
        columnNames.add("Field");
        columnNames.add("Value");
        columnWidths.add(new Integer(60));
        columnWidths.add(new Integer(150));
        columnWidths.add(new Integer(150));
        columnWidths.add(new Integer(150));
        columnWidths.add(new Integer(150));
        tableData = new ArrayList();
        table = new Table(tableData, columnNames, columnWidths, 0, 750, false);
        table.setColumnSequence(new int[] {
            0, 1, 2, 3, 4
        });
        table.setIndexColumn(1);
        ((DynaTable)table.getTable()).setValueGroupedColumn(0, true);
        ((DynaTable)table.getTable()).setValueGroupedColumn(1, true);
        ((DynaTable)table.getTable()).setValueGroupedColumn(2, true);
        table.getTable().addMouseListener(this);
    }

    private void loadMatrix()
    {
        ArrayList selectionTable = null;
        try
        {
            selectionTable = dos.getCodeSelectionTable(this.fieldOuid);
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
        }
        tableData.clear();
        HashMap aNumber = null;
        HashMap fieldTable = null;
        ArrayList tempList = null;
        String number = null;
        String codeOuid = null;
        DOSChangeable code = null;
        String initialOuid = null;
        DOSChangeable initialItem = null;
        String fieldOuid = null;
        DOSChangeable field = null;
        String valueOuid = null;
        DOSChangeable codeItem = null;
        try
        {
            if(selectionTable != null && !selectionTable.isEmpty())
            {
                Iterator fieldKey = null;
                Iterator tableKey;
                for(tableKey = selectionTable.iterator(); tableKey.hasNext();)
                {
                    aNumber = (HashMap)tableKey.next();
                    number = (String)aNumber.get("number");
                    codeOuid = (String)aNumber.get("code");
                    initialOuid = (String)aNumber.get("initial");
                    fieldTable = (HashMap)aNumber.get("fieldMap");
                    if("visual.type.of.combobox".equals(number))
                    {
                        checkBox.setSelected(Utils.getBoolean(new Boolean(codeOuid), false));
                    } else
                    {
                        if(!Utils.isNullString(initialOuid))
                            initialItem = dos.getCodeItem(initialOuid);
                        for(fieldKey = fieldTable.keySet().iterator(); fieldKey.hasNext();)
                        {
                            fieldOuid = (String)fieldKey.next();
                            valueOuid = (String)fieldTable.get(fieldOuid);
                            code = dos.getCode(codeOuid);
                            if(code != null)
                            {
                                field = dos.getField(fieldOuid);
                                if(field != null)
                                {
                                    if(!Utils.isNullString(valueOuid))
                                        codeItem = dos.getCodeItem(valueOuid);
                                    tempList = new ArrayList();
                                    tempList.add(number);
                                    tempList.add(code.get("name"));
                                    if(!Utils.isNullString(initialOuid))
                                        tempList.add(initialItem.get("name") + " [" + initialItem.get("codeitemid") + "]");
                                    else
                                        tempList.add(null);
                                    tempList.add(field.get("name") + " [" + field.get("title") + "]");
                                    if(!Utils.isNullString(valueOuid))
                                        tempList.add(codeItem.get("name") + " [" + codeItem.get("codeitemid") + "]");
                                    else
                                        tempList.add(null);
                                    tempList.add(number);
                                    tempList.add(codeOuid);
                                    tempList.add(initialOuid);
                                    tempList.add(fieldOuid);
                                    tempList.add(valueOuid);
                                    tableData.add(tempList);
                                    tempList = null;
                                }
                            }
                        }

                        fieldKey = null;
                    }
                }

                tableKey = null;
                table.changeTableData();
            }
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if(cmd.equals("Close"))
            dispose();
        else
        if(cmd.equals("Add"))
        {
            boolean check = true;
            check = check && !Utils.isNullString(numberTextField.getText());
            check = check && !Utils.isNullString((String)codeComboModel.getSelectedOID());
            check = check && !Utils.isNullString((String)fieldComboModel.getSelectedOID());
            if(!check)
            {
                JOptionPane.showMessageDialog(this, "Some required value not selected.", "Warning", 2);
                return;
            }
            ArrayList tempList = new ArrayList();
            tempList.add(numberTextField.getText());
            tempList.add(codeComboModel.getSelectedItem());
            tempList.add(initialTextField.getText());
            tempList.add(fieldComboModel.getSelectedItem());
            tempList.add(valueTextField.getText());
            tempList.add(numberTextField.getText());
            tempList.add(codeComboModel.getSelectedOID());
            tempList.add(initialOuid);
            tempList.add(fieldComboModel.getSelectedOID());
            tempList.add(valueOuid);
            int row = tableData.size() - 1;
            ArrayList tempList2 = null;
            for(int i = tableData.size() - 1; i >= 0; i--)
            {
                tempList2 = (ArrayList)tableData.get(i);
                if(((String)tempList2.get(0)).compareTo((String)tempList.get(0)) < 0)
                {
                    row = i;
                    break;
                }
                if(((String)tempList2.get(0)).compareTo((String)tempList.get(0)) != 0)
                    continue;
                tempList.set(1, tempList2.get(1));
                tempList.set(2, tempList2.get(2));
                tempList.set(6, tempList2.get(6));
                tempList.set(7, tempList2.get(7));
                row = i;
                break;
            }

            row++;
            tableData.add(row, tempList);
            tempList = null;
            table.changeTableData();
        } else
        if(cmd.equals("Remove"))
        {
            int row = table.getTable().getSelectedRow();
            if(row < 0 || tableData == null)
                return;
            tableData.remove(row);
            table.changeTableData();
        } else
        if(cmd.equals("OK"))
        {
            ArrayList selectionTable = new ArrayList();
            HashMap aNumber = null;
            HashMap fieldTable = null;
            ArrayList tempList = null;
            String oldNumber = null;
            if(tableData != null && !tableData.isEmpty())
            {
                Iterator tableKey;
                for(tableKey = tableData.iterator(); tableKey.hasNext(); fieldTable.put(tempList.get(8), tempList.get(9)))
                {
                    tempList = (ArrayList)tableKey.next();
                    if(!tempList.get(5).equals(oldNumber))
                    {
                        oldNumber = (String)tempList.get(5);
                        if(aNumber != null)
                            selectionTable.add(aNumber);
                        aNumber = new HashMap();
                        fieldTable = new HashMap();
                        aNumber.put("number", oldNumber);
                        aNumber.put("code", tempList.get(6));
                        aNumber.put("initial", tempList.get(7));
                        aNumber.put("fieldMap", fieldTable);
                    }
                }

                tableKey = null;
                if(aNumber != null)
                    selectionTable.add(aNumber);
            }
            aNumber = new HashMap();
            aNumber.put("number", "visual.type.of.combobox");
            aNumber.put("code", (new Boolean(checkBox.isSelected())).toString());
            selectionTable.add(aNumber);
            try
            {
                dos.setCodeSelectionTable(this.fieldOuid, selectionTable);
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
            dispose();
        } else
        if(cmd.equals("value"))
        {
            String codeOuid = null;
            String fieldOuid = null;
            DOSChangeable field = null;
            Byte type = null;
            fieldOuid = (String)fieldComboModel.getSelectedOID();
            if(Utils.isNullString(fieldOuid))
                return;
            try
            {
                field = dos.getField(fieldOuid);
                if(field == null)
                    return;
                type = (Byte)field.get("type");
                codeOuid = (String)field.get("type.ouid@class");
                field = null;
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
            if(type != null && type.byteValue() == 25)
            {
                TypeClassSelection tcs = new TypeClassSelection(this, "Code Selection", "Code");
                tcs.setVisible(true);
                codeOuid = tcs.selectedOuid;
                if(Utils.isNullString(codeOuid))
                    return;
                CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)e.getSource(), true, codeOuid, null);
                newSmall.setVisible(true);
            } else
            if(!Utils.isNullString(codeOuid))
            {
                CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)e.getSource(), true, codeOuid, null);
                newSmall.setVisible(true);
            }
        } else
        if(cmd.equals("initial"))
        {
            String codeOuid = (String)codeComboModel.getSelectedOID();
            if(!Utils.isNullString(codeOuid))
            {
                CodeSelectDialog newSmall = new CodeSelectDialog(this, (Component)e.getSource(), true, codeOuid, "initial");
                newSmall.setVisible(true);
            }
        } else
        if(cmd.equals("Field"))
            setValueCode("", null);
    }

    public void setValueCode(String name, String ouid)
    {
        valueTextField.setText(name);
        valueOuid = ouid;
    }

    public void setInitialCode(String name, String ouid)
    {
        initialTextField.setText(name);
        initialOuid = ouid;
    }

    public void mouseClicked(MouseEvent e)
    {
        int row = table.getTable().rowAtPoint(e.getPoint());
        if(row < 0 || tableData == null)
        {
            return;
        } else
        {
            ArrayList tempList = (ArrayList)tableData.get(row);
            numberTextField.setText((String)tempList.get(5));
            codeComboModel.setSelectedItemByOID((String)tempList.get(6));
            codeComboBox.updateUI();
            initialOuid = (String)tempList.get(7);
            initialTextField.setText((String)tempList.get(2));
            fieldComboModel.setSelectedItemByOID((String)tempList.get(8));
            fieldComboBox.updateUI();
            valueOuid = (String)tempList.get(9);
            valueTextField.setText((String)tempList.get(4));
            return;
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

    private Frame owner;
    public DOS dos;
    private String classOuid;
    private String fieldOuid;
    private ArrayList tableData;
    private String valueOuid;
    private String initialOuid;
    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private Table table;
    private JPanel infoPanel;
    private JToolBar toolBar;
    private JButton addButton;
    private JButton removeButton;
    private JPanel centerPanel;
    private DynaTextField numberTextField;
    private DynaComboBox codeComboBox;
    private DynaComboBoxDataLoader codeComboLoader;
    private DynaComboBoxModel codeComboModel;
    private DynaComboBox fieldComboBox;
    private DynaComboBoxDataLoader fieldComboLoader;
    private DynaComboBoxModel fieldComboModel;
    private DynaTextField valueTextField;
    private JButton valueButton;
    private DynaTextField initialTextField;
    private JButton initialButton;
    private JPanel bottomPanel;
    private JCheckBox checkBox;
    private JButton okButton;
    private JButton closeButton;

}
