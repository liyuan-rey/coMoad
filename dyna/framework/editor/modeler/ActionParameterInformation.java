// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActionParameterInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ActionInformation

public class ActionParameterInformation extends JPanel
    implements ActionListener
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


    public ActionParameterInformation(ActionInformation parentFrame)
    {
        actionOuid = "";
        selectedOuid = "";
        dComboType = new DynaComboTypeInstance();
        typeComboModel = new DynaComboBoxModel(dComboType);
        parent = parentFrame;
        initialize();
    }

    public void initialize()
    {
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel = new JPanel();
        packageInfoBorderLayout = new BorderLayout();
        setLayout(packageInfoBorderLayout);
        mainToolBar = new JToolBar();
        mainBoxLayout = new BoxLayout(mainToolBar, 0);
        mainToolBar.setLayout(mainBoxLayout);
        mainToolBar.setBorder(BorderFactory.createEtchedBorder());
        addButton = new JButton();
        addButton.setEnabled(true);
        addButton.setText("Add");
        addButton.setToolTipText("add");
        addButton.setActionCommand("add");
        addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.setIcon(new ImageIcon(getClass().getResource("/icons/Mandatory16.gif")));
        addButton.addActionListener(this);
        removeButton = new JButton();
        removeButton.setEnabled(true);
        removeButton.setText("Remove");
        removeButton.setToolTipText("delete");
        removeButton.setActionCommand("delete");
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setIcon(new ImageIcon(getClass().getResource("/icons/AscendingSort16.gif")));
        removeButton.addActionListener(this);
        mainToolBar.add(addButton);
        mainToolBar.add(removeButton);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        mainPanel.setLayout(gridBag);
        dummyLabel = new JLabel("");
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        dosActionOuidTextField = new DynaTextField();
        dosActionOuidTextField.setMandatory(true);
        dosActionOuidTextField.setTitleText("DOS Action Ouid");
        dosActionOuidTextField.setTitleWidth(120);
        dosActionOuidTextField.setTitleVisible(true);
        dosActionOuidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(dosActionOuidTextField, gridBagCon);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(true);
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(120);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(nameTextField, gridBagCon);
        typeComboBox = new DynaComboBox();
        typeComboBox.setMandatory(true);
        typeComboBox.setTitleText("Type");
        typeComboBox.setTitleWidth(120);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        typeComboBox.setModel(typeComboModel);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(typeComboBox, gridBagCon);
        defaultValueTextField = new DynaTextField();
        defaultValueTextField.setMandatory(false);
        defaultValueTextField.setTitleText("Default Value");
        defaultValueTextField.setTitleWidth(120);
        defaultValueTextField.setTitleVisible(true);
        defaultValueTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(defaultValueTextField, gridBagCon);
        dosFieldMappingTextField = new DynaTextField();
        dosFieldMappingTextField.setMandatory(false);
        dosFieldMappingTextField.setTitleText("DOS Field Mapping");
        dosFieldMappingTextField.setTitleWidth(120);
        dosFieldMappingTextField.setTitleVisible(true);
        dosFieldMappingTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(dosFieldMappingTextField, gridBagCon);
        mainPanel.add(dosActionOuidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(typeComboBox);
        mainPanel.add(defaultValueTextField);
        mainPanel.add(dosFieldMappingTextField);
        mainPanel.add(dummyLabel);
        mainScrPane.getViewport().add(mainPanel, null);
        add(mainToolBar, "North");
        add(mainScrPane, "Center");
        typeComboModel.enableDataLoad();
    }

    public DOSChangeable createInformation()
    {
        DOSChangeable setInfoDC = null;
        return setInfoDC;
    }

    public void setParameterInfo(String ouid)
    {
        selectedOuid = ouid;
    }

    public void setFieldEnabled(boolean isEnable)
    {
        nameTextField.setEditable(isEnable);
        typeComboBox.setEnabled(isEnable);
        defaultValueTextField.setEditable(isEnable);
        dosFieldMappingTextField.setEditable(isEnable);
        addButton.setEnabled(isEnable);
        removeButton.setEnabled(isEnable);
    }

    public ArrayList setTypeComboBox()
    {
        ArrayList typeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        typeAL.add(new Byte((byte)1));
        typeAL.add("boolean");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)2));
        typeAL.add("byte");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)3));
        typeAL.add("char");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)4));
        typeAL.add("double");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)5));
        typeAL.add("float");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)6));
        typeAL.add("int");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)7));
        typeAL.add("long");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)8));
        typeAL.add("short");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)13));
        typeAL.add("UTF");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)14));
        typeAL.add("list");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)15));
        typeAL.add("map");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)16));
        typeAL.add("object");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)17));
        typeAL.add("set");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)18));
        typeAL.add("sorted map");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)19));
        typeAL.add("sorted set");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)20));
        typeAL.add("array list");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)21));
        typeAL.add("date time");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)22));
        typeAL.add("date");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add(new Byte((byte)23));
        typeAL.add("time");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        return returnAL;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("add"))
        {
            DOSChangeable tableData = new DOSChangeable();
            tableData.put("ouid@action", parent.getActionOuid());
            tableData.put("name", nameTextField.getText());
            tableData.put("typenam", typeComboModel.getSelectedItem());
            tableData.put("default.value", defaultValueTextField.getText());
            tableData.put("ouid@field.map", dosFieldMappingTextField.getText());
            tableData.put("type", typeComboModel.getSelectedOID());
            parent.setTableData(tableData);
        } else
        if(command.equals("delete"))
        {
            System.out.println("del");
            parent.removeTableRow(selectedOuid);
        }
    }

    private final int titleTextWidth = 120;
    private BorderLayout packageInfoBorderLayout;
    private BoxLayout packageInfoLayout;
    private ActionInformation parent;
    private JScrollPane mainScrPane;
    private JPanel mainPanel;
    private JToolBar mainToolBar;
    private BoxLayout mainBoxLayout;
    private JButton addButton;
    private JButton removeButton;
    private JLabel dummyLabel;
    private DynaTextField dosActionOuidTextField;
    private DynaTextField nameTextField;
    private DynaComboBox typeComboBox;
    private DynaTextField defaultValueTextField;
    private DynaTextField dosFieldMappingTextField;
    private String actionOuid;
    private String selectedOuid;
    private DynaComboTypeInstance dComboType;
    private DynaComboBoxModel typeComboModel;
    public static final byte DATATYPE_BOOLEAN = 1;
    public static final byte DATATYPE_BYTE = 2;
    public static final byte DATATYPE_CHAR = 3;
    public static final byte DATATYPE_DOUBLE = 4;
    public static final byte DATATYPE_FLOAT = 5;
    public static final byte DATATYPE_INT = 6;
    public static final byte DATATYPE_LONG = 7;
    public static final byte DATATYPE_SHORT = 8;
    public static final byte DATATYPE_UTF = 13;
    public static final byte DATATYPE_LIST = 14;
    public static final byte DATATYPE_MAP = 15;
    public static final byte DATATYPE_SET = 17;
    public static final byte DATATYPE_SORTED_MAP = 18;
    public static final byte DATATYPE_SORTED_SET = 19;
    public static final byte DATATYPE_ARRAY_LIST = 20;
    public static final byte DATATYPE_DATETIME = 21;
    public static final byte DATATYPE_DATE = 22;
    public static final byte DATATYPE_TIME = 23;
    public static final byte DATATYPE_OBJECT = 16;
}
