// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GanttChartSetting.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.Table;
import dyna.uic.UIUtils;
import dyna.util.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, FieldChooser

public class GanttChartSetting extends JDialog
    implements ActionListener, WindowListener, MouseListener
{
    class ColorRenderer extends JLabel
        implements TableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column)
        {
            setBackground((Color)color);
            if(isBordered)
                if(isSelected)
                {
                    if(selectedBorder == null)
                        selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                    setBorder(selectedBorder);
                } else
                {
                    if(unselectedBorder == null)
                        unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                    setBorder(unselectedBorder);
                }
            return this;
        }

        Border unselectedBorder;
        Border selectedBorder;
        boolean isBordered;

        public ColorRenderer(boolean isBordered)
        {
            unselectedBorder = null;
            selectedBorder = null;
            this.isBordered = true;
            this.isBordered = isBordered;
            setOpaque(true);
        }
    }


    public GanttChartSetting(Frame owner, String ouid, ArrayList assoTableInfo)
    {
        super(owner, DynaMOAD.getMSRString("WRD_0086", "Chart Setting", 3), true);
        DEFAULT_COLOR = Color.green;
        setSize(350, 400);
        setResizable(false);
        setDefaultCloseOperation(2);
        addWindowListener(this);
        classOuid = ouid;
        this.assoTableInfo = assoTableInfo;
        settingOption = 1;
        dos = DynaMOAD.dos;
        nds = DynaMOAD.nds;
        newUI = DynaMOAD.newUI;
        beginDateData = new DOSChangeable();
        endDateData = new DOSChangeable();
        makeAddedFieldTable();
        initialize();
    }

    public void initialize()
    {
        buttonToolBar = new JToolBar();
        saveButton = new JButton();
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Save.gif")));
        saveButton.setToolTipText(DynaMOAD.getMSRString("WRD_0011", "save", 3));
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(this);
        closeButton = new JButton();
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        String listTypeArray[] = new String[1];
        listTypeArray[0] = "DATE_BASE";
        listTypeComboBox = new JComboBox(listTypeArray);
        listTypeComboBox.setSize(21, 50);
        listTypeComboBox.setBorder(UIManagement.borderTextBoxNotEditable);
        listTypeComboBox.setActionCommand("Type Select");
        listTypeComboBox.addActionListener(this);
        buttonToolBar.add(saveButton);
        buttonToolBar.add(closeButton);
        buttonToolBar.add(Box.createHorizontalStrut(100));
        buttonToolBar.add(listTypeComboBox);
        dateBasePanel = new JPanel();
        dateBasePanel.setLayout(new BorderLayout());
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        addFieldPanel = new JPanel();
        addFieldPanel.setBorder(UIManagement.borderPanel);
        addFieldPanel.setBackground(UIManagement.panelBackGround);
        addFieldPanel.setLayout(gridBag);
        beginDateLabel = new JLabel(DynaMOAD.getMSRString("WRD_0087", "Begin Date", 3));
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(7, 5, 0, 5);
        gridBag.setConstraints(beginDateLabel, gridBagCon);
        beginDateTextField = new JTextField();
        beginDateTextField.setBorder(UIManagement.borderTextBoxEditable);
        beginDateTextField.setEditable(false);
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(7, 5, 0, 5);
        gridBag.setConstraints(beginDateTextField, gridBagCon);
        fieldSelectButton1 = new JButton();
        fieldSelectButton1.setMargin(new Insets(0, 0, 0, 0));
        fieldSelectButton1.setIcon(new ImageIcon(getClass().getResource("/icons/Field.gif")));
        fieldSelectButton1.setBorder(UIManagement.borderTextBoxNotEditable);
        fieldSelectButton1.setToolTipText(DynaMOAD.getMSRString("WRD_0091", "Field Select", 3));
        fieldSelectButton1.setActionCommand("Field Select");
        fieldSelectButton1.addActionListener(this);
        fieldSelectButton1.setDoubleBuffered(true);
        gridBagCon.fill = 3;
        gridBagCon.anchor = 18;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 3;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(7, 0, 0, 5);
        gridBag.setConstraints(fieldSelectButton1, gridBagCon);
        endDateLabel = new JLabel(DynaMOAD.getMSRString("WRD_0088", "End Date", 3));
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 5, 0, 5);
        gridBag.setConstraints(endDateLabel, gridBagCon);
        endDateTextField = new JTextField();
        endDateTextField.setBorder(UIManagement.borderTextBoxEditable);
        endDateTextField.setEditable(false);
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 5, 0, 5);
        gridBag.setConstraints(endDateTextField, gridBagCon);
        fieldSelectButton2 = new JButton();
        fieldSelectButton2.setMargin(new Insets(0, 0, 0, 0));
        fieldSelectButton2.setIcon(new ImageIcon(getClass().getResource("/icons/Field.gif")));
        fieldSelectButton2.setBorder(UIManagement.borderTextBoxNotEditable);
        fieldSelectButton2.setToolTipText(DynaMOAD.getMSRString("WRD_0091", "Field Select", 3));
        fieldSelectButton2.setActionCommand("Field Select");
        fieldSelectButton2.addActionListener(this);
        fieldSelectButton2.setDoubleBuffered(true);
        gridBagCon.fill = 3;
        gridBagCon.anchor = 18;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 3;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 0, 0, 5);
        gridBag.setConstraints(fieldSelectButton2, gridBagCon);
        colorLabel = new JLabel(DynaMOAD.getMSRString("WRD_0092", "Color", 3));
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 5, 0, 5);
        gridBag.setConstraints(colorLabel, gridBagCon);
        colorViewLabel = new JLabel();
        colorViewLabel.setOpaque(true);
        colorViewLabel.setBorder(UIManagement.borderTextBoxEditable);
        colorViewLabel.setBackground(DEFAULT_COLOR);
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 5, 0, 5);
        gridBag.setConstraints(colorViewLabel, gridBagCon);
        colorSelectButton = new JButton();
        colorSelectButton.setMargin(new Insets(0, 0, 0, 0));
        colorSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Field.gif")));
        colorSelectButton.setBorder(UIManagement.borderTextBoxNotEditable);
        colorSelectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0093", "Color Select", 3));
        colorSelectButton.setActionCommand("Color Select");
        colorSelectButton.addActionListener(this);
        colorSelectButton.setDoubleBuffered(true);
        gridBagCon.fill = 3;
        gridBagCon.anchor = 18;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 3;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(5, 0, 0, 5);
        gridBag.setConstraints(colorSelectButton, gridBagCon);
        addButton = new JButton();
        addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.setText(DynaMOAD.getMSRString("WRD_0001", "Add", 3));
        addButton.setIcon(new ImageIcon(getClass().getResource("/icons/Import16.gif")));
        addButton.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Add", 3));
        addButton.setActionCommand("Add");
        addButton.addActionListener(this);
        addButton.setDoubleBuffered(true);
        gridBagCon.fill = 3;
        gridBagCon.anchor = 12;
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(15, 5, 7, 5);
        gridBag.setConstraints(addButton, gridBagCon);
        deleteButton = new JButton();
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setText(DynaMOAD.getMSRString("WRD_0002", "delete", 3));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteButton.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "delete", 3));
        deleteButton.setActionCommand("Delete");
        deleteButton.addActionListener(this);
        deleteButton.setDoubleBuffered(true);
        gridBagCon.fill = 3;
        gridBagCon.anchor = 12;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(15, 0, 7, 5);
        gridBag.setConstraints(deleteButton, gridBagCon);
        clearButton = new JButton();
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setText(DynaMOAD.getMSRString("WRD_0016", "clear", 3));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "clear", 3));
        clearButton.setActionCommand("Clear");
        clearButton.addActionListener(this);
        gridBagCon.fill = 3;
        gridBagCon.anchor = 18;
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 3;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(15, 0, 7, 5);
        gridBag.setConstraints(clearButton, gridBagCon);
        addFieldPanel.add(beginDateLabel);
        addFieldPanel.add(beginDateTextField);
        addFieldPanel.add(fieldSelectButton1);
        addFieldPanel.add(endDateLabel);
        addFieldPanel.add(endDateTextField);
        addFieldPanel.add(fieldSelectButton2);
        addFieldPanel.add(colorLabel);
        addFieldPanel.add(colorViewLabel);
        addFieldPanel.add(colorSelectButton);
        addFieldPanel.add(addButton);
        addFieldPanel.add(deleteButton);
        addFieldPanel.add(clearButton);
        addedFieldScrPane = UIFactory.createStrippedScrollPane(null);
        addedFieldScrPane.setViewportView(addedFieldTable.getTable());
        addedFieldScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        dateBasePanel.add(addFieldPanel, "North");
        dateBasePanel.add(addedFieldScrPane, "Center");
        weightBasePanel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonToolBar, "North");
        getContentPane().add(dateBasePanel, "Center");
    }

    public void makeAddedFieldTable()
    {
        try
        {
            ArrayList tmpFieldData = new ArrayList();
            addedFieldOuidData = new ArrayList();
            addedFieldData = new ArrayList();
            addedFieldColumnNames = new ArrayList();
            addedFieldColumnWidths = new ArrayList();
            addedFieldColumnNames.add(DynaMOAD.getMSRString("WRD_0020", "Number", 0));
            addedFieldColumnNames.add(DynaMOAD.getMSRString("WRD_B001", "Field for begin", 0));
            addedFieldColumnNames.add(DynaMOAD.getMSRString("WRD_B002", "Field for end", 0));
            addedFieldColumnNames.add(DynaMOAD.getMSRString("WRD_0092", "Color", 0));
            addedFieldColumnWidths.add(new Integer(30));
            addedFieldColumnWidths.add(new Integer(100));
            addedFieldColumnWidths.add(new Integer(100));
            addedFieldColumnWidths.add(new Integer(70));
            ArrayList tmpList1 = new ArrayList();
            ArrayList tmpList2 = new ArrayList();
            ArrayList fieldList = nds.getChildNodeList("::/WORKSPACE/DATEBASECHARTFIELD/" + classOuid);
            if(fieldList != null)
            {
                for(int i = 0; i < fieldList.size(); i++)
                {
                    tmpList2.add((new Integer(i + 1)).toString());
                    String val = nds.getValue("::/WORKSPACE/DATEBASECHARTFIELD/" + classOuid + "/" + (String)fieldList.get(i));
                    String bFieldOuid = (String)Utils.tokenizeMessage(val, '/').get(0);
                    DOSChangeable bFieldObject = dos.getField(bFieldOuid);
                    if(bFieldObject == null)
                    {
                        tmpList1.add(null);
                        tmpList2.add(null);
                    } else
                    {
                        tmpList1.add(bFieldObject.get("ouid"));
                        tmpList2.add(bFieldObject.get("title"));
                    }
                    String eFieldOuid = (String)Utils.tokenizeMessage(val, '/').get(1);
                    DOSChangeable eFieldObject = dos.getField(eFieldOuid);
                    if(eFieldObject == null)
                    {
                        tmpList1.add(null);
                        tmpList2.add(null);
                    } else
                    {
                        tmpList1.add(eFieldObject.get("ouid"));
                        tmpList2.add(eFieldObject.get("title"));
                    }
                    tmpList1.add(new Color(Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(2)), Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(3)), Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(4))));
                    tmpList2.add(new Color(Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(2)), Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(3)), Integer.parseInt((String)Utils.tokenizeMessage(val, '/').get(4))));
                    addedFieldOuidData.add(tmpList1.clone());
                    addedFieldData.add(tmpList2.clone());
                    tmpList1.clear();
                    tmpList2.clear();
                }

                tmpList1 = null;
                tmpList2 = null;
            }
            addedFieldTable = new Table(addedFieldData, (ArrayList)addedFieldColumnNames.clone(), (ArrayList)addedFieldColumnWidths.clone(), 1);
            addedFieldTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            addedFieldTable.setColumnSequence(new int[] {
                0, 1, 2, 3
            });
            addedFieldTable.setIndexColumn(0);
            addedFieldTable.getTable().setRowSelectionAllowed(true);
            addedFieldTable.getTable().addMouseListener(this);
            setColorRenderer(addedFieldTable.getTable());
            selectedRow = -1;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setFieldData(Component comp, ArrayList fieldData)
    {
        if(comp == fieldSelectButton1)
        {
            beginDateData.clear();
            beginDateData.put("title", fieldData.get(0));
            beginDateData.put("description", fieldData.get(1));
            beginDateData.put("type", fieldData.get(2));
            beginDateData.put("ouid", fieldData.get(3));
            beginDateTextField.setText((String)fieldData.get(0));
        } else
        if(comp == fieldSelectButton2)
        {
            endDateData.clear();
            endDateData.put("title", fieldData.get(0));
            endDateData.put("description", fieldData.get(1));
            endDateData.put("type", fieldData.get(2));
            endDateData.put("ouid", fieldData.get(3));
            endDateTextField.setText((String)fieldData.get(0));
        }
    }

    public void setBackgroundComp(Component comp, Color backgroundColor)
    {
        if(comp == colorSelectButton)
            colorViewLabel.setBackground(backgroundColor);
    }

    public void closeEvent()
    {
        saveButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        addButton.removeActionListener(this);
        deleteButton.removeActionListener(this);
        clearButton.removeActionListener(this);
        dispose();
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
                getClass();
                if(settingOption == 1)
                {
                    nds.removeNode("::/WORKSPACE/DATEBASECHARTFIELD/" + classOuid);
                    nds.addNode("::/WORKSPACE", "DATEBASECHARTFIELD", "CHARTFIELDSET", "DATEBASECHARTFIELD");
                    nds.addNode("::/WORKSPACE/DATEBASECHARTFIELD", classOuid, "CHARTFIELDSET", classOuid);
                    for(int i = 0; i < addedFieldOuidData.size(); i++)
                    {
                        String tmpStr = (String)((ArrayList)addedFieldOuidData.get(i)).get(0) + "/" + (String)((ArrayList)addedFieldOuidData.get(i)).get(1) + "/" + ((Color)((ArrayList)addedFieldOuidData.get(i)).get(2)).getRed() + "/" + ((Color)((ArrayList)addedFieldOuidData.get(i)).get(2)).getGreen() + "/" + ((Color)((ArrayList)addedFieldOuidData.get(i)).get(2)).getBlue();
                        nds.addNode("::/WORKSPACE/DATEBASECHARTFIELD/" + classOuid, (new Integer(i)).toString(), "CHARTFIELDSET", tmpStr);
                    }

                } else
                {
                    getClass();
                }
            } else
            if(command.equals("Close"))
                closeEvent();
            else
            if(command.equals("Field Select"))
            {
                if(assoTableInfo != null && assoTableInfo.size() > 0)
                {
                    FieldChooser fieldChooser = new FieldChooser(this, (Component)actionEvent.getSource(), classOuid, assoTableInfo, this);
                    UIUtils.setLocationRelativeTo(fieldChooser, (Component)actionEvent.getSource());
                    fieldChooser.setVisible(true);
                } else
                {
                    JOptionPane.showMessageDialog(this, DynaMOAD.getMSRString("INF_A001", "Date type field not exists.", 0), DynaMOAD.getMSRString("WRD_0004", "Information", 0), 1);
                }
            } else
            if(command.equals("Color Select"))
            {
                final JColorChooser tcc = new JColorChooser();
                tcc.getSelectionModel().addChangeListener(new ChangeListener() {

                    public void stateChanged(ChangeEvent e)
                    {
                        Color newColor = tcc.getColor();
                    }

                });
                Color newColor = JColorChooser.showDialog(this, "Color Select", colorViewLabel.getBackground());
                if(newColor != null)
                    colorViewLabel.setBackground(newColor);
            } else
            if(command.equals("Add"))
            {
                ArrayList tmpList = new ArrayList();
                tmpList.add(beginDateData.get("ouid"));
                tmpList.add(endDateData.get("ouid"));
                tmpList.add(colorViewLabel.getBackground());
                addedFieldOuidData.add(tmpList.clone());
                tmpList.clear();
                tmpList.add((new Integer(addedFieldData.size() + 1)).toString());
                tmpList.add(beginDateData.get("title"));
                tmpList.add(endDateData.get("title"));
                tmpList.add(colorViewLabel.getBackground());
                addedFieldData.add(tmpList.clone());
                tmpList = null;
                addedFieldTable.changeTableData();
            } else
            if(command.equals("Delete"))
            {
                Object option[] = {
                    "\uC608", "\uC544\uB2C8\uC624"
                };
                int res = JOptionPane.showOptionDialog(this, "\uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "\uC0AD\uC81C", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[1]);
                if(res != 0)
                    return;
                addedFieldOuidData.remove(selectedRow);
                addedFieldData.remove(selectedRow);
                addedFieldTable.changeTableData();
            } else
            if(command.equals("Clear"))
                clearAllFieldValue();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
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
        if(source.equals(addedFieldTable.getTable()))
        {
            selectedRow = addedFieldTable.getTable().getSelectedRow();
            String ouidRow = addedFieldTable.getSelectedOuidRow(selectedRow);
            if(ouidRow != null)
            {
                addedFieldTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                selectedRow = (new Integer(ouidRow)).intValue();
            }
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void clearAllFieldValue()
    {
        beginDateTextField.setText("");
        endDateTextField.setText("");
        colorViewLabel.setBackground(DEFAULT_COLOR);
        beginDateData.clear();
        endDateData.clear();
    }

    private void setColorRenderer(JTable table)
    {
        table.setDefaultRenderer(java.awt.Color.class, new ColorRenderer(true));
    }

    private DOS dos;
    private NDS nds;
    private UIManagement newUI;
    private JToolBar buttonToolBar;
    private JButton saveButton;
    private JButton closeButton;
    private JPanel dateBasePanel;
    private JPanel weightBasePanel;
    private JPanel addFieldPanel;
    private JLabel listTypeLabel;
    private JComboBox listTypeComboBox;
    private JLabel beginDateLabel;
    private JLabel endDateLabel;
    private JLabel colorLabel;
    private JTextField beginDateTextField;
    private JTextField endDateTextField;
    private JLabel colorViewLabel;
    private JButton fieldSelectButton1;
    private JButton fieldSelectButton2;
    private JButton colorSelectButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearButton;
    private Table addedFieldTable;
    private JScrollPane addedFieldScrPane;
    private DOSChangeable beginDateData;
    private DOSChangeable endDateData;
    private ArrayList assoTableInfo;
    private int settingOption;
    private String classOuid;
    private int selectedRow;
    private ArrayList addedFieldOuidData;
    private ArrayList addedFieldData;
    private ArrayList addedFieldColumnNames;
    private ArrayList addedFieldColumnWidths;
    final int DATE_BASE = 1;
    final int WEIGHT_BASE = 2;
    final Color DEFAULT_COLOR;
}