// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NumberingRuleInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction, ClassInformation, TypeClassSelection

public class NumberingRuleInformation extends JPanel
    implements MouseListener, ActionListener
{
    class DynaComboTypeInstance extends DynaComboBoxDataLoader
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
            return setTypeComboBox();
        }

        DynaComboTypeInstance()
        {
        }
    }


    public NumberingRuleInformation(Object parentFrame)
    {
        dComboType = new DynaComboTypeInstance();
        typeComboModel = new DynaComboBoxModel(dComboType);
        dos = null;
        nds = null;
        classOuid = "";
        tableData = new ArrayList();
        flag = false;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            nds = (NDS)ObjectModelingConstruction.dfw.getServiceInstance("DF30NDS1");
            parent = parentFrame;
            initialize();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void initialize()
    {
        saveButton = new JButton();
        saveButton.setToolTipText("Save");
        saveButton.setActionCommand("Save");
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.addActionListener(this);
        refreshButton = new JButton();
        refreshButton.setToolTipText("Refresh");
        refreshButton.setActionCommand("Refresh");
        refreshButton.setIcon(new ImageIcon(getClass().getResource("/icons/Refresh.gif")));
        refreshButton.setMargin(new Insets(0, 0, 0, 0));
        refreshButton.addActionListener(this);
        deleteButton = new JButton();
        deleteButton.setToolTipText("Delete");
        deleteButton.setActionCommand("Delete");
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.addActionListener(this);
        mainButtonToolBar = new JToolBar(0);
        mainButtonToolBar.setLayout(new BoxLayout(mainButtonToolBar, 0));
        mainButtonToolBar.setBorder(BorderFactory.createEtchedBorder());
        mainButtonToolBar.add(saveButton);
        mainButtonToolBar.add(refreshButton);
        mainButtonToolBar.add(deleteButton);
        makeTable();
        ruleTableScrPane = UIFactory.createStrippedScrollPane(null);
        ruleTableScrPane.setViewportView(ruleTable.getTable());
        ruleTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        addButton = new JButton();
        addButton.setText("Add");
        addButton.setActionCommand("Add");
        addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.setIcon(new ImageIcon(getClass().getResource("/icons/Mandatory16.gif")));
        addButton.addActionListener(this);
        removeButton = new JButton();
        removeButton.setText("Remove");
        removeButton.setActionCommand("Remove");
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setIcon(new ImageIcon(getClass().getResource("/icons/AscendingSort16.gif")));
        removeButton.addActionListener(this);
        subButtonToolBar = new JToolBar(0);
        subButtonToolBar.setLayout(new BoxLayout(subButtonToolBar, 0));
        subButtonToolBar.setBorder(BorderFactory.createEtchedBorder());
        subButtonToolBar.add(addButton);
        subButtonToolBar.add(removeButton);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        typeComboBox = new DynaComboBox();
        typeComboBox.setMandatory(true);
        typeComboBox.setTitleText("Type");
        typeComboBox.setTitleWidth(100);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        typeComboBox.setModel(typeComboModel);
        typeComboBox.setActionCommand("Type");
        typeComboBox.addActionListener(this);
        typeComboModel.enableDataLoad();
        gridBagCon.insets = new Insets(10, 0, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(typeComboBox, gridBagCon);
        contentsTextField = new DynaTextField();
        contentsTextField.setMandatory(false);
        contentsTextField.setTitleText("Contents");
        contentsTextField.setTitleWidth(100);
        contentsTextField.setTitleVisible(true);
        contentsTextField.setEditable(true);
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(contentsTextField, gridBagCon);
        fieldSelectButton = new JButton();
        fieldSelectButton.setText("...");
        fieldSelectButton.setActionCommand("FieldSelect");
        fieldSelectButton.setPreferredSize(new Dimension(20, 20));
        fieldSelectButton.addActionListener(this);
        gridBagCon.insets = new Insets(2, 0, 2, 5);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(fieldSelectButton, gridBagCon);
        lengthTextField = new DynaTextField();
        lengthTextField.setMandatory(false);
        lengthTextField.setTitleText("Length");
        lengthTextField.setTitleWidth(100);
        lengthTextField.setTitleVisible(true);
        lengthTextField.setEditable(true);
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(lengthTextField, gridBagCon);
        noteTextField = new DynaTextField();
        noteTextField.setMandatory(false);
        noteTextField.setTitleText("Note");
        noteTextField.setTitleWidth(100);
        noteTextField.setTitleVisible(true);
        noteTextField.setEditable(true);
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(noteTextField, gridBagCon);
        dummyLabel = new JLabel();
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        componentPanel = new JPanel();
        componentPanel.setLayout(gridBag);
        componentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        componentPanel.add(typeComboBox);
        componentPanel.add(contentsTextField);
        componentPanel.add(fieldSelectButton);
        componentPanel.add(lengthTextField);
        componentPanel.add(noteTextField);
        componentPanel.add(dummyLabel);
        componentScrPane = UIFactory.createStrippedScrollPane(null);
        componentScrPane.setViewportView(componentPanel);
        rulePanel = new JPanel();
        rulePanel.setLayout(new BorderLayout());
        rulePanel.add(subButtonToolBar, "North");
        rulePanel.add(componentScrPane, "Center");
        splitPane = new JSplitPane(0);
        splitPane.setDividerSize(3);
        splitPane.setDividerLocation(200);
        splitPane.setPreferredSize(new Dimension(300, 300));
        splitPane.setTopComponent(ruleTableScrPane);
        splitPane.setBottomComponent(rulePanel);
        setLayout(new BorderLayout());
        add(mainButtonToolBar, "North");
        add(splitPane, "Center");
    }

    private void makeTable()
    {
        ArrayList columnNames = new ArrayList();
        ArrayList columnWidths = new ArrayList();
        columnNames.add("Type");
        columnNames.add("Contents");
        columnNames.add("Length");
        columnNames.add("Note");
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(60));
        columnWidths.add(new Integer(150));
        ruleTable = new Table(tableData, columnNames, columnWidths, 0, 750);
        ruleTable.setColumnSequence(new int[] {
            1, 2, 3, 4
        });
        ruleTable.setIndexColumn(1);
        ruleTable.getTable().addMouseListener(this);
    }

    public void setOuid(String ouid)
    {
        classOuid = ouid;
        if(parent instanceof ClassInformation)
            refreshButton_actionPerformed(null);
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("Save"))
            saveButton_actionPerformed(e);
        else
        if(command.equals("Refresh"))
            refreshButton_actionPerformed(e);
        else
        if(command.equals("Delete"))
            deleteButton_actionPerformed(e);
        else
        if(command.equals("Add"))
            addButton_actionPerformed(e);
        else
        if(command.equals("Remove"))
            removeButton_actionPerformed(e);
        else
        if(command.equals("Type"))
            typeComboBox_actionPerformed(e);
        else
        if(command.equals("FieldSelect"))
            fieldSelectButton_actionPerformed(e);
    }

    void saveButton_actionPerformed(ActionEvent e)
    {
        try
        {
            getClass();
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid);
            getClass();
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER" + "/" + classOuid);
            getClass();
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE", classOuid, "DOS.RULE", "DOS.RULE");
            getClass();
            nds.addNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER", classOuid, "DOS.RULE", "DOS.RULE");
            for(int i = 0; i < tableData.size(); i++)
            {
                String sequence = String.valueOf(i + 1);
                if(sequence.length() == 1)
                    sequence = "0" + sequence;
                getClass();
                nds.addNode("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid, sequence, "DOS.RULE", (String)((ArrayList)tableData.get(i)).get(0));
                getClass();
                nds.addArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + sequence, "TypeID", (String)((ArrayList)tableData.get(i)).get(0));
                getClass();
                nds.addArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + sequence, "TypeName", (String)((ArrayList)tableData.get(i)).get(1));
                getClass();
                nds.addArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + sequence, "Contents", (String)((ArrayList)tableData.get(i)).get(2));
                getClass();
                nds.addArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + sequence, "Length", (String)((ArrayList)tableData.get(i)).get(3));
                getClass();
                nds.addArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + sequence, "Note", (String)((ArrayList)tableData.get(i)).get(4));
            }

            refreshButton_actionPerformed(e);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void refreshButton_actionPerformed(ActionEvent e)
    {
        try
        {
            ArrayList tmpList = new ArrayList();
            tableData.clear();
            getClass();
            ArrayList ruleList = nds.getChildNodeList("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid);
            if(ruleList != null)
            {
                for(int i = 0; i < ruleList.size(); i++)
                {
                    String key = (String)ruleList.get(i);
                    getClass();
                    String typeid = nds.getArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + key, "TypeID");
                    getClass();
                    String typename = nds.getArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + key, "TypeName");
                    getClass();
                    String contents = nds.getArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + key, "Contents");
                    getClass();
                    String len = nds.getArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + key, "Length");
                    getClass();
                    String note = nds.getArgument("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid + "/" + key, "Note");
                    tmpList.add(typeid);
                    tmpList.add(typename);
                    if(contents != null)
                        tmpList.add(contents);
                    else
                        tmpList.add("");
                    if(len != null)
                        tmpList.add(len);
                    else
                        tmpList.add("");
                    if(note != null)
                        tmpList.add(note);
                    else
                        tmpList.add("");
                    tableData.add(tmpList.clone());
                    tmpList.clear();
                }

            }
            ruleTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void deleteButton_actionPerformed(ActionEvent e)
    {
        try
        {
            Object option[] = {
                "\uC608", "\uC544\uB2C8\uC624"
            };
            int res = JOptionPane.showOptionDialog(this, "Numbering Rule\uC744 \uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "Numbering Rule delete", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[0]);
            if(res != 0)
                return;
            getClass();
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE" + "/" + classOuid);
            getClass();
            nds.removeNode("::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER" + "/" + classOuid);
            refreshButton_actionPerformed(e);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void addButton_actionPerformed(ActionEvent e)
    {
        if(typeComboBox.getSelectedIndex() > -1)
        {
            if((typeComboModel.getSelectedOID().equals("NGR01") || typeComboModel.getSelectedOID().equals("NGR04")) && Utils.isNullString(contentsTextField.getText()))
            {
                JOptionPane.showMessageDialog(this, "Contents\uB97C \uC785\uB825\uD558\uC5EC \uC8FC\uC2ED\uC2DC\uC624.", "Numbering Rule Save", 2, new ImageIcon(getClass().getResource("/icons/Warning32.gif")));
                return;
            }
            if((typeComboModel.getSelectedOID().equals("NGR02") || typeComboModel.getSelectedOID().equals("NGR03") || typeComboModel.getSelectedOID().equals("NGR10")) && Utils.isNullString(lengthTextField.getText()))
            {
                JOptionPane.showMessageDialog(this, "Length\uB97C \uC785\uB825\uD558\uC5EC \uC8FC\uC2ED\uC2DC\uC624.", "Numbering Rule Save", 2, new ImageIcon(getClass().getResource("/icons/Warning32.gif")));
                return;
            }
            if(typeComboModel.getSelectedOID().equals("NGR02") && !lengthTextField.getText().equals("4") && !lengthTextField.getText().equals("3") && !lengthTextField.getText().equals("2"))
            {
                JOptionPane.showMessageDialog(this, "Length\uC758 \uAC12\uC774 \uC720\uD6A8\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.", "Numbering Rule Save", 2, new ImageIcon(getClass().getResource("/icons/Warning32.gif")));
                return;
            }
            if(typeComboModel.getSelectedOID().equals("NGR03") && !lengthTextField.getText().equals("2") && !lengthTextField.getText().equals("1"))
            {
                JOptionPane.showMessageDialog(this, "Length\uC758 \uAC12\uC774 \uC720\uD6A8\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.", "Numbering Rule Save", 2, new ImageIcon(getClass().getResource("/icons/Warning32.gif")));
                return;
            }
            ArrayList rowList = new ArrayList();
            rowList.add(typeComboModel.getSelectedOID());
            rowList.add(typeComboModel.getSelectedItem());
            rowList.add(contentsTextField.getText());
            if(typeComboModel.getSelectedOID().equals("NGR01"))
                rowList.add(String.valueOf(contentsTextField.getText().length()));
            else
                rowList.add(lengthTextField.getText());
            rowList.add(noteTextField.getText());
            tableData.add(rowList);
            rowList = null;
            ruleTable.changeTableData();
        }
    }

    void removeButton_actionPerformed(ActionEvent e)
    {
        int selRow = ruleTable.getTable().getSelectedRow();
        if(selRow > -1)
        {
            tableData.remove(selRow);
            ruleTable.changeTableData();
        }
    }

    void typeComboBox_actionPerformed(ActionEvent e)
    {
        if(typeComboBox.getSelectedIndex() > -1)
        {
            contentsTextField.setText("");
            lengthTextField.setText("");
            noteTextField.setText("");
            if(typeComboModel.getSelectedOID().equals("NGR01"))
            {
                contentsTextField.setEditable(true);
                fieldSelectButton.setEnabled(false);
                lengthTextField.setEditable(false);
            } else
            if(typeComboModel.getSelectedOID().equals("NGR02"))
            {
                contentsTextField.setEditable(false);
                fieldSelectButton.setEnabled(false);
                lengthTextField.setEditable(true);
            } else
            if(typeComboModel.getSelectedOID().equals("NGR03"))
            {
                contentsTextField.setEditable(false);
                fieldSelectButton.setEnabled(false);
                lengthTextField.setEditable(true);
            } else
            if(typeComboModel.getSelectedOID().equals("NGR05"))
            {
                contentsTextField.setEditable(false);
                fieldSelectButton.setEnabled(false);
                lengthTextField.setEditable(true);
            } else
            if(typeComboModel.getSelectedOID().equals("NGR04"))
            {
                contentsTextField.setEditable(false);
                fieldSelectButton.setEnabled(true);
                lengthTextField.setEditable(false);
            } else
            if(typeComboModel.getSelectedOID().equals("NGR10"))
            {
                contentsTextField.setEditable(false);
                fieldSelectButton.setEnabled(false);
                lengthTextField.setEditable(true);
            }
        }
    }

    void fieldSelectButton_actionPerformed(ActionEvent e)
    {
        if(typeComboModel.getSelectedOID().equals("NGR04"))
        {
            TypeClassSelection fieldSelectFrame = new TypeClassSelection(this, "Field Selection", classOuid, 1);
            fieldSelectFrame.setVisible(true);
        }
    }

    public void setFieldData(ArrayList oidAL)
    {
        try
        {
            if(((String)oidAL.get(0)).equals("md$number") || ((String)oidAL.get(0)).equals("md$sequence") || ((String)oidAL.get(0)).equals("vf$version") || ((String)oidAL.get(0)).equals("md$description") || ((String)oidAL.get(0)).equals("md$status") || ((String)oidAL.get(0)).equals("md$user") || ((String)oidAL.get(0)).equals("md$cdate") || ((String)oidAL.get(0)).equals("md$mdate"))
            {
                if(!flag)
                    contentsTextField.setText((String)oidAL.get(0));
                else
                if(flag)
                {
                    contentsTextField.setText(contentsTextField.getText() + "@" + (String)oidAL.get(0));
                    flag = false;
                    return;
                }
            } else
            {
                DOSChangeable fieldData = dos.getField((String)oidAL.get(0));
                if(fieldData == null)
                    return;
                if(!flag)
                    contentsTextField.setText((String)oidAL.get(0));
                else
                if(flag)
                {
                    contentsTextField.setText(contentsTextField.getText() + "@" + (String)oidAL.get(0));
                    flag = false;
                    return;
                }
                if(((Byte)fieldData.get("type")).byteValue() == 16)
                {
                    flag = true;
                    TypeClassSelection fieldSelectFrame = new TypeClassSelection(this, "Field Selection", (String)fieldData.get("type.ouid@class"), 2);
                    fieldSelectFrame.setVisible(true);
                }
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public ArrayList setTypeComboBox()
    {
        ArrayList leafAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        leafAL.add("NGR01");
        leafAL.add("Text");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        leafAL.add("NGR02");
        leafAL.add("Year");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        leafAL.add("NGR03");
        leafAL.add("Month");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        leafAL.add("NGR05");
        leafAL.add("Day");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        leafAL.add("NGR04");
        leafAL.add("Field");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        leafAL.add("NGR10");
        leafAL.add("Serial Number");
        returnAL.add(leafAL.clone());
        leafAL.clear();
        return returnAL;
    }

    private JSplitPane splitPane;
    private JToolBar mainButtonToolBar;
    private JButton saveButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton exitButton;
    private JScrollPane ruleTableScrPane;
    private Table ruleTable;
    private JPanel rulePanel;
    private JToolBar subButtonToolBar;
    private JButton addButton;
    private JButton removeButton;
    private JScrollPane componentScrPane;
    private JPanel componentPanel;
    private DynaComboBox typeComboBox;
    private DynaComboBoxDataLoader dComboType;
    private DynaComboBoxModel typeComboModel;
    private DynaTextField contentsTextField;
    private JButton fieldSelectButton;
    private DynaTextField lengthTextField;
    private DynaTextField noteTextField;
    private JLabel dummyLabel;
    private DOS dos;
    private NDS nds;
    private String classOuid;
    private Object parent;
    private ArrayList tableData;
    private boolean flag;
    private final int DIVIDERSIZE = 3;
    private final int DIVIDERLOCATION = 200;
    private final int titleTextWidth = 100;
    private final String NDS_NUMBERINGRULE = "::/DOS_SYSTEM_DIRECTORY/NUMBERINGRULE";
    private final String NDS_INSTANCENUMBER = "::/DOS_SYSTEM_DIRECTORY/INSTANCENUMBER";
}
