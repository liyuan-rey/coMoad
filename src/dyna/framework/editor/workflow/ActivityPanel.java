// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ActivityPanel.java

package dyna.framework.editor.workflow;

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            WorkflowModeler, EventInformation

public class ActivityPanel extends JPanel
    implements ActionListener
{
    class ActivityTypeModel extends AbstractListModel
        implements ComboBoxModel
    {

        public Object getElementAt(int param)
        {
            return typeArray[param][2];
        }

        public int getSize()
        {
            return typeArray.length;
        }

        public Object getSelectedItem()
        {
            if(selectedItem >= 0)
                return typeArray[selectedItem][2];
            else
                return null;
        }

        public void setSelectedItem(Object obj)
        {
            selectedItem = -1;
            for(int i = 0; i < typeArray.length; i++)
                if(typeArray[i][2].equals(obj))
                    selectedItem = i;

        }

        private int selectedItem;

        ActivityTypeModel()
        {
            selectedItem = -1;
        }
    }

    class StartFinishModel extends AbstractListModel
        implements ComboBoxModel
    {

        public Object getElementAt(int param)
        {
            return startFinishArray[param][1];
        }

        public int getSize()
        {
            return startFinishArray.length;
        }

        public Object getSelectedItem()
        {
            if(selectedItem >= 0)
                return startFinishArray[selectedItem][1];
            else
                return null;
        }

        public void setSelectedItem(Object obj)
        {
            selectedItem = -1;
            for(int i = 0; i < startFinishArray.length; i++)
                if(startFinishArray[i][1].equals(obj))
                    selectedItem = i;

        }

        private int selectedItem;

        StartFinishModel()
        {
            selectedItem = -1;
        }
    }

    class JoinSplitModel extends AbstractListModel
        implements ComboBoxModel
    {

        public Object getElementAt(int param)
        {
            return joinSplitArray[param][1];
        }

        public int getSize()
        {
            return joinSplitArray.length;
        }

        public Object getSelectedItem()
        {
            if(selectedItem >= 0)
                return joinSplitArray[selectedItem][1];
            else
                return null;
        }

        public void setSelectedItem(Object obj)
        {
            selectedItem = -1;
            for(int i = 0; i < joinSplitArray.length; i++)
                if(joinSplitArray[i][1].equals(obj))
                    selectedItem = i;

        }

        private int selectedItem;

        JoinSplitModel()
        {
            selectedItem = -1;
        }
    }

    class StatusClassModel extends AbstractListModel
        implements ComboBoxModel
    {

        public Object getElementAt(int param)
        {
            return ((ArrayList)parameterStatusClassList.get(param)).get(1);
        }

        public int getSize()
        {
            return parameterStatusClassList.size();
        }

        public Object getSelectedItem()
        {
            if(selectedItem >= 0)
                return ((ArrayList)parameterStatusClassList.get(selectedItem)).get(1);
            else
                return null;
        }

        public void setSelectedItem(Object obj)
        {
            selectedItem = -1;
            for(int i = 0; i < parameterStatusClassList.size(); i++)
                if(((ArrayList)parameterStatusClassList.get(i)).get(1).equals(obj))
                    selectedItem = i;

        }

        private int selectedItem;

        StatusClassModel()
        {
            selectedItem = -1;
        }
    }

    class StatusFromModel extends AbstractListModel
        implements ComboBoxModel
    {

        public Object getElementAt(int param)
        {
            return ((ArrayList)parameterStatusFromList.get(param)).get(1);
        }

        public int getSize()
        {
            return parameterStatusFromList.size();
        }

        public Object getSelectedItem()
        {
            if(selectedItem >= 0)
            {
                if(parameterStatusFromList.size() == 0)
                    return null;
                else
                    return ((ArrayList)parameterStatusFromList.get(selectedItem)).get(1);
            } else
            {
                return null;
            }
        }

        public void setSelectedItem(Object obj)
        {
            selectedItem = -1;
            for(int i = 0; i < parameterStatusFromList.size(); i++)
                if(((ArrayList)parameterStatusFromList.get(i)).get(1).equals(obj))
                    selectedItem = i;

        }

        private int selectedItem;

        StatusFromModel()
        {
            selectedItem = -1;
        }
    }

    class StatusToModel extends AbstractListModel
        implements ComboBoxModel
    {

        public Object getElementAt(int param)
        {
            return ((ArrayList)parameterStatusToList.get(param)).get(1);
        }

        public int getSize()
        {
            return parameterStatusToList.size();
        }

        public Object getSelectedItem()
        {
            if(selectedItem >= 0)
            {
                if(parameterStatusToList.size() == 0)
                    return null;
                else
                    return ((ArrayList)parameterStatusToList.get(selectedItem)).get(1);
            } else
            {
                return null;
            }
        }

        public void setSelectedItem(Object obj)
        {
            selectedItem = -1;
            for(int i = 0; i < parameterStatusToList.size(); i++)
                if(((ArrayList)parameterStatusToList.get(i)).get(1).equals(obj))
                    selectedItem = i;

        }

        private int selectedItem;

        StatusToModel()
        {
            selectedItem = -1;
        }
    }

    class ComboRenderer extends DefaultListCellRenderer
    {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            Component retValue = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if((retValue instanceof JLabel) && !isSelected)
                ((JLabel)retValue).setOpaque(false);
            else
            if((retValue instanceof JLabel) && isSelected)
                ((JLabel)retValue).setOpaque(true);
            return retValue;
        }

        ComboRenderer()
        {
        }
    }


    public ActivityPanel()
    {
        ouid = null;
        processOuid = null;
        comboMap = null;
        tempCombo = null;
        parameterStatusColumnWidth = 150;
        parameterStatusList = null;
        parameterStatusNames = null;
        parameterStatusWidths = null;
        parameterStatusData = null;
        parameterStatusClassList = null;
        parameterStatusFromList = null;
        parameterStatusToList = null;
        parameterStatusClassMap = null;
        parameterStatusSplitPane = null;
        parameterStatusPanel1 = null;
        parameterStatusToolBar1 = null;
        parameterStatusRefreshButton = null;
        parameterStatusDeleteButton = null;
        parameterStatusScroll = null;
        parameterStatusTable = null;
        parameterStatusPanel2 = null;
        parameterStatusToolBar2 = null;
        parameterStatusAddButton = null;
        parameterStatusCenterPanel2 = null;
        parameterStatusClassLabel = null;
        parameterStatusClassCombo = null;
        parameterStatusStatusLabel = null;
        parameterStatusFromLabel = null;
        parameterStatusFromCombo = null;
        parameterStatusToLabel = null;
        parameterStatusToCombo = null;
        initComponents();
    }

    private void initComponents()
    {
        topPanel = new JPanel();
        tabPanel = new JPanel();
        tabTitlePanel = new JPanel();
        tabButtons = new JToggleButton[4];
        setLayout(new BorderLayout(0, 0));
        setFont(new Font("dialog", 0, 11));
        topPanel.setLayout(null);
        topPanel.setMaximumSize(new Dimension(500, 60));
        topPanel.setMinimumSize(new Dimension(500, 60));
        topPanel.setPreferredSize(new Dimension(500, 60));
        idLabel = new JLabel();
        idLabel.setText("Identifier");
        idLabel.setFont(getFont());
        idLabel.setBounds(10, 5, 80, 21);
        topPanel.add(idLabel);
        idTextField = new JTextField();
        idTextField.setBackground(Color.orange);
        idTextField.setBounds(90, 5, 140, 21);
        topPanel.add(idTextField);
        nameLabel = new JLabel();
        nameLabel.setText("Name");
        nameLabel.setFont(getFont());
        nameLabel.setBounds(10, 31, 80, 21);
        topPanel.add(nameLabel);
        nameTextField = new JTextField();
        nameTextField.setBounds(90, 31, 140, 21);
        topPanel.add(nameTextField);
        responsibleLabel = new JLabel();
        responsibleLabel.setText("Performer");
        responsibleLabel.setFont(getFont());
        responsibleLabel.setBounds(250, 5, 80, 21);
        topPanel.add(responsibleLabel);
        responsibleTextField = new JTextField();
        responsibleTextField.setBounds(340, 5, 140, 21);
        topPanel.add(responsibleTextField);
        add(topPanel, "North");
        tabPanel.setLayout(new BorderLayout(0, 0));
        add(tabPanel, "Center");
        tabTitlePanel.setLayout(new FlowLayout(0, 0, 0));
        Insets tabInsets = new Insets(1, 5, 1, 5);
        for(i = 0; i < 4; i++)
        {
            tabButtons[i] = new JToggleButton();
            tabButtons[i].setFont(getFont());
            tabButtons[i].setHorizontalAlignment(2);
            tabButtons[i].setMargin(tabInsets);
            tabButtons[i].setSelected(true);
            tabButtons[i].addActionListener(this);
            tabTitlePanel.add(tabButtons[i]);
        }

        tabButtons[0].setText("Attribute");
        tabButtons[0].setActionCommand("attribute");
        tabButtons[1].setVisible(false);
        tabButtons[2].setText("Parameter");
        tabButtons[2].setActionCommand("parameter");
        tabButtons[2].setEnabled(false);
        tabButtons[3].setText("Event");
        tabButtons[3].setActionCommand("event");
        tabPanel.add(tabTitlePanel, "North");
        tabButtons[0].doClick();
    }

    private JPanel getAttributePanel()
    {
        if(attributePanel != null)
            return attributePanel;
        attributePanel = new JPanel();
        attributePanel.setBorder(null);
        attributePanel.setLayout(null);
        attributeLabels = new JLabel[attributeNames.length];
        attributeTextFields = new JTextField[attributeNames.length];
        for(i = 0; i < attributeNames.length; i++)
        {
            attributeLabels[i] = new JLabel();
            attributeLabels[i].setFont(getFont());
            attributeLabels[i].setText(attributeNames[i][0]);
            attributeLabels[i].setHorizontalAlignment(4);
            attributeLabels[i].setBounds(0, 5 + i * 26, 140, 21);
            attributePanel.add(attributeLabels[i]);
            if(attributeNames[i][2] != null && attributeNames[i][2].indexOf("combobox") >= 0)
            {
                if(comboMap == null)
                    comboMap = new HashMap();
                tempCombo = new JComboBox();
                tempCombo.setFont(getFont());
                tempCombo.setBounds(150, 5 + i * 26, 300, 21);
                comboMap.put(attributeNames[i][1], tempCombo);
                attributePanel.add(tempCombo);
                if(attributeNames[i][1].equals("type"))
                {
                    tempCombo.setModel(new ActivityTypeModel());
                    tempCombo.setRenderer(new ComboRenderer());
                    tempCombo.setSelectedIndex(0);
                    tempCombo.setActionCommand("type");
                    tempCombo.addActionListener(this);
                } else
                if(attributeNames[i][1].equals("mode.start") || attributeNames[i][1].equals("mode.finish"))
                {
                    tempCombo.setModel(new StartFinishModel());
                    tempCombo.setRenderer(new ComboRenderer());
                } else
                if(attributeNames[i][1].equals("join") || attributeNames[i][1].equals("split"))
                {
                    tempCombo.setModel(new JoinSplitModel());
                    tempCombo.setRenderer(new ComboRenderer());
                }
                if(attributeNames[i][2].indexOf("readonly") >= 0)
                    tempCombo.setEnabled(false);
                if(attributeNames[i][2].indexOf("mandatory") >= 0)
                    tempCombo.setBackground(Color.orange);
                tempCombo = null;
            } else
            {
                attributeTextFields[i] = new JTextField();
                attributeTextFields[i].setBounds(150, 5 + i * 26, 300, 21);
                if(attributeNames[i][2] != null)
                {
                    if(attributeNames[i][2].indexOf("mandatory") >= 0)
                        attributeTextFields[i].setBackground(Color.orange);
                    if(attributeNames[i][2].indexOf("readonly") >= 0)
                        attributeTextFields[i].setEnabled(false);
                }
                attributePanel.add(attributeTextFields[i]);
            }
        }

        int width = 480;
        int height = 5 + attributeNames.length * 26 + 5;
        Dimension dimension = new Dimension(width, height);
        attributePanel.setMinimumSize(dimension);
        attributePanel.setMaximumSize(dimension);
        attributePanel.setPreferredSize(dimension);
        return attributePanel;
    }

    private JPanel getWorkPanel()
    {
        if(workPanel != null)
        {
            return workPanel;
        } else
        {
            workPanel = new JPanel();
            System.out.println("work");
            return workPanel;
        }
    }

    private JPanel getParameterPanel()
    {
        JComboBox tempCombo = (JComboBox)comboMap.get("type");
        if(tempCombo == null)
            return null;
        String tempString = typeArray[tempCombo.getSelectedIndex()][0];
        if(!"240".equals(tempString))
            return null;
        if(Utils.isNullString(ouid))
            return null;
        if(parameterPanel != null)
        {
            parameterPanel.removeAll();
            parameterPanel = null;
        }
        parameterPanel = new JPanel();
        parameterPanel.setLayout(new BorderLayout());
        parameterStatusSplitPane = new JSplitPane();
        parameterStatusSplitPane.setOrientation(0);
        parameterStatusSplitPane.setDividerLocation(200);
        parameterPanel.add(parameterStatusSplitPane);
        parameterStatusPanel1 = new JPanel();
        parameterStatusPanel1.setLayout(new BorderLayout());
        parameterStatusSplitPane.setTopComponent(parameterStatusPanel1);
        parameterStatusToolBar1 = new JToolBar();
        parameterStatusToolBar1.putClientProperty("jgoodies.headerStyle", HeaderStyle.BOTH);
        parameterStatusPanel1.add(parameterStatusToolBar1, "North");
        parameterStatusRefreshButton = new JButton();
        parameterStatusRefreshButton.setIcon(new ImageIcon("icons/Refresh.gif"));
        parameterStatusRefreshButton.setActionCommand("parameterStatus.refresh");
        parameterStatusRefreshButton.setToolTipText("Refresh");
        parameterStatusRefreshButton.addActionListener(this);
        parameterStatusToolBar1.add(parameterStatusRefreshButton);
        parameterStatusDeleteButton = new JButton();
        parameterStatusDeleteButton.setIcon(new ImageIcon("icons/Delete.gif"));
        parameterStatusDeleteButton.setActionCommand("parameterStatus.delete");
        parameterStatusDeleteButton.setToolTipText("Delete");
        parameterStatusDeleteButton.addActionListener(this);
        parameterStatusToolBar1.add(parameterStatusDeleteButton);
        parameterStatusScroll = UIFactory.createStrippedScrollPane(null);
        parameterStatusPanel1.add(parameterStatusScroll, "Center");
        parameterStatusPanel2 = new JPanel();
        parameterStatusPanel2.setLayout(new BorderLayout());
        parameterStatusSplitPane.setBottomComponent(parameterStatusPanel2);
        parameterStatusToolBar2 = new JToolBar();
        parameterStatusToolBar2.putClientProperty("jgoodies.headerStyle", HeaderStyle.BOTH);
        parameterStatusPanel2.add(parameterStatusToolBar2, "North");
        parameterStatusAddButton = new JButton();
        parameterStatusAddButton.setIcon(new ImageIcon("icons/add_att.gif"));
        parameterStatusAddButton.setActionCommand("parameterStatus.add");
        parameterStatusAddButton.setToolTipText("Add");
        parameterStatusAddButton.addActionListener(this);
        parameterStatusToolBar2.add(parameterStatusAddButton);
        parameterStatusCenterPanel2 = new JPanel();
        parameterStatusCenterPanel2.setLayout(null);
        parameterStatusPanel2.add(parameterStatusCenterPanel2, "Center");
        parameterStatusClassLabel = new JLabel();
        parameterStatusClassLabel.setText("Class");
        parameterStatusClassLabel.setBounds(10, 10, 100, 22);
        parameterStatusCenterPanel2.add(parameterStatusClassLabel);
        parameterStatusClassCombo = new JComboBox();
        parameterStatusClassCombo.setActionCommand("parameterStatusClassCombo");
        parameterStatusClassCombo.setBounds(120, 10, 222, 22);
        parameterStatusClassCombo.addActionListener(this);
        parameterStatusCenterPanel2.add(parameterStatusClassCombo);
        parameterStatusStatusLabel = new JLabel();
        parameterStatusStatusLabel.setText("Status");
        parameterStatusStatusLabel.setBounds(10, 36, 50, 22);
        parameterStatusCenterPanel2.add(parameterStatusStatusLabel);
        parameterStatusFromLabel = new JLabel();
        parameterStatusFromLabel.setText("From");
        parameterStatusFromLabel.setBounds(60, 36, 100, 22);
        parameterStatusCenterPanel2.add(parameterStatusFromLabel);
        parameterStatusFromCombo = new JComboBox();
        parameterStatusFromCombo.setActionCommand("parameterStatusFromCombo");
        parameterStatusFromCombo.setBounds(120, 36, 222, 22);
        parameterStatusFromCombo.addActionListener(this);
        parameterStatusCenterPanel2.add(parameterStatusFromCombo);
        parameterStatusToLabel = new JLabel();
        parameterStatusToLabel.setText("To");
        parameterStatusToLabel.setBounds(60, 62, 100, 22);
        parameterStatusCenterPanel2.add(parameterStatusToLabel);
        parameterStatusToCombo = new JComboBox();
        parameterStatusToCombo.setActionCommand("parameterStatusToCombo");
        parameterStatusToCombo.setBounds(120, 62, 222, 22);
        parameterStatusCenterPanel2.add(parameterStatusToCombo);
        parameterStatusTable = makeParameterStatusTable(parameterStatusList);
        if(parameterStatusTable != null)
        {
            parameterStatusScroll.setViewportView(parameterStatusTable.getTable());
            parameterStatusScroll.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        }
        return parameterPanel;
    }

    private Table makeParameterStatusTable(ArrayList oidList)
    {
        parameterStatusNames = new ArrayList();
        parameterStatusWidths = new ArrayList();
        parameterStatusData = new ArrayList();
        parameterStatusNames.add("Class");
        parameterStatusNames.add("From");
        parameterStatusNames.add("To");
        parameterStatusWidths.add(new Integer(parameterStatusColumnWidth));
        parameterStatusWidths.add(new Integer(parameterStatusColumnWidth));
        parameterStatusWidths.add(new Integer(parameterStatusColumnWidth));
        if(oidList != null)
        {
            for(int i = 0; i < oidList.size(); i++)
                parameterStatusData.add(oidList.get(i));

        } else
        if(!Utils.isNullString(processOuid))
            try
            {
                ArrayList tmpList = null;
                DOSChangeable tmpObject = null;
                Iterator selectedKey = null;
                String tmpString = null;
                if(parameterStatusClassList == null)
                    parameterStatusClassList = new ArrayList();
                else
                    parameterStatusClassList.clear();
                ArrayList classList = WorkflowModeler.wfm.listClassOfProcessDefinition(processOuid);
                if(classList != null)
                {
                    if(parameterStatusClassMap == null)
                        parameterStatusClassMap = new HashMap();
                    tmpList = new ArrayList();
                    int i = 0;
                    for(selectedKey = classList.iterator(); selectedKey.hasNext();)
                    {
                        tmpString = (String)selectedKey.next();
                        tmpObject = WorkflowModeler.dos.getClass(tmpString);
                        if(tmpObject != null)
                        {
                            i++;
                            tmpList.add(tmpObject.get("ouid"));
                            tmpList.add(tmpObject.get("name"));
                            tmpList.add(tmpObject.get("description"));
                            parameterStatusClassMap.put(tmpObject.get("ouid"), tmpObject.get("name"));
                            parameterStatusClassList.add(tmpList.clone());
                            tmpList.clear();
                        }
                    }

                    tmpList = null;
                    selectedKey = null;
                    classList.clear();
                    classList = null;
                }
                tmpList = new ArrayList();
                tmpList.add("DEFAULT");
                tmpList.add("Default");
                tmpList.add("Default");
                parameterStatusClassList.add(0, tmpList.clone());
                tmpList.clear();
                tmpList = null;
                parameterStatusClassMap.put("DEFAULT", "Default");
                parameterStatusClassCombo.setModel(new StatusClassModel());
                parameterStatusClassCombo.setSelectedIndex(0);
                parameterStatusFromCombo.setModel(new StatusFromModel());
                parameterStatusFromCombo.setSelectedIndex(0);
                parameterStatusToCombo.setModel(new StatusToModel());
                parameterStatusToCombo.setSelectedIndex(0);
                reloadParameterStatusClassData();
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        parameterStatusTable = new Table(parameterStatusData, (ArrayList)parameterStatusNames.clone(), (ArrayList)parameterStatusWidths.clone(), 1, 79);
        parameterStatusTable.setIndexColumn(0);
        parameterStatusTable.setColumnSequence(new int[] {
            1, 2, 3
        });
        return parameterStatusTable;
    }

    private void getStatusFromList(String classOuid)
    {
        ArrayList fromList = null;
        try
        {
            fromList = WorkflowModeler.olm.list(classOuid);
            if(parameterStatusFromList == null)
                parameterStatusFromList = new ArrayList();
            else
                parameterStatusFromList.clear();
            if(fromList == null || fromList.size() == 0)
                fromList = WorkflowModeler.olm.list();
            HashMap node = null;
            ArrayList tempList = null;
            String NDS_CODE = WorkflowModeler.nds.getSubSet("CODE") + "/STATUS/";
            for(int i = 0; i < fromList.size(); i++)
            {
                node = (HashMap)fromList.get(i);
                tempList = new ArrayList();
                tempList.add(node.get("name"));
                tempList.add(WorkflowModeler.nds.getValue(NDS_CODE + node.get("name")) + " [" + node.get("name") + "]");
                tempList.add(node.get("name"));
                parameterStatusFromList.add(tempList.clone());
                tempList.clear();
                tempList = null;
            }

            parameterStatusFromCombo.updateUI();
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    private void getStatusToList(String classOuid, String fromId)
    {
        ArrayList toList = null;
        try
        {
            toList = WorkflowModeler.olm.listLink(classOuid, fromId);
            if(parameterStatusToList == null)
                parameterStatusToList = new ArrayList();
            else
                parameterStatusToList.clear();
            if(toList == null || toList.size() == 0)
                toList = WorkflowModeler.olm.listLink(fromId);
            if(toList == null || toList.size() == 0)
                return;
            HashMap node = null;
            ArrayList tempList = null;
            String NDS_CODE = WorkflowModeler.nds.getSubSet("CODE") + "/STATUS/";
            for(int i = 0; i < toList.size(); i++)
            {
                node = (HashMap)toList.get(i);
                tempList = new ArrayList();
                tempList.add(node.get("name"));
                tempList.add(WorkflowModeler.nds.getValue(NDS_CODE + node.get("name")) + " [" + node.get("name") + "]");
                tempList.add(node.get("name"));
                parameterStatusToList.add(tempList.clone());
                tempList.clear();
                tempList = null;
            }

            parameterStatusToCombo.updateUI();
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    private void reloadParameterStatusClassData()
    {
        ArrayList tmpList = null;
        ArrayList tmpList2 = null;
        try
        {
            ArrayList tableList = WorkflowModeler.wfm.listActivityStatus(ouid);
            if(!Utils.isNullArrayList(tableList))
            {
                if(parameterStatusData == null)
                    parameterStatusData = new ArrayList();
                parameterStatusData.clear();
                String NDS_CODE = WorkflowModeler.nds.getSubSet("CODE") + "/STATUS/";
                HashMap node = null;
                for(int x = 0; x < tableList.size(); x++)
                {
                    tmpList = (ArrayList)tableList.get(x);
                    tmpList2 = new ArrayList();
                    tmpList2.add(parameterStatusClassMap.get(tmpList.get(0)));
                    tmpList2.add(WorkflowModeler.nds.getValue(NDS_CODE + tmpList.get(1)) + " [" + tmpList.get(1) + "]");
                    tmpList2.add(WorkflowModeler.nds.getValue(NDS_CODE + tmpList.get(2)) + " [" + tmpList.get(2) + "]");
                    node = new HashMap();
                    node.put("ouid@activity", ouid);
                    node.put("ouid@class", tmpList.get(0));
                    node.put("from.id", tmpList.get(1));
                    node.put("to.id", tmpList.get(2));
                    tmpList2.add(0, node);
                    parameterStatusData.add(tmpList2.clone());
                    tmpList2.clear();
                    tmpList2 = null;
                }

                tableList.clear();
                tableList = null;
            }
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    private JPanel getEventPanel()
    {
        if(eventPanel != null)
        {
            return eventPanel;
        } else
        {
            eventPanel = new EventInformation(this);
            return eventPanel;
        }
    }

    public DOSChangeable getValue()
    {
        DOSChangeable dosObject = new DOSChangeable();
        String tempString1 = null;
        String tempString2 = null;
        JComboBox tempCombo = null;
        dosObject.put("object.type", "activity");
        dosObject.put("ouid", ouid);
        dosObject.put("ouid@process.definition", processOuid);
        dosObject.put("x", new Integer(x));
        dosObject.put("y", new Integer(y));
        dosObject.put("identifier", idTextField.getText());
        dosObject.put("name", nameTextField.getText());
        dosObject.put("participant", responsibleTextField.getText());
        for(i = 0; i < attributeNames.length; i++)
        {
            tempString1 = attributeNames[i][1];
            tempCombo = (JComboBox)comboMap.get(tempString1);
            if(tempCombo != null && tempCombo.getSelectedIndex() >= 0)
            {
                if(tempString1.equals("type"))
                    tempString2 = typeArray[tempCombo.getSelectedIndex()][0];
                else
                if(tempString1.equals("mode.start") || tempString1.equals("mode.finish"))
                    tempString2 = startFinishArray[tempCombo.getSelectedIndex()][0];
                else
                if(tempString1.equals("join") || tempString1.equals("split"))
                    tempString2 = joinSplitArray[tempCombo.getSelectedIndex()][0];
                tempCombo = null;
            } else
            if(attributeTextFields[i] != null)
                tempString2 = attributeTextFields[i].getText().trim();
            if(!Utils.isNullString(tempString2))
                if(tempString1.equals("limit") || tempString1.equals("priority") || tempString1.equals("instantiation"))
                    dosObject.put(tempString1, new Integer(tempString2));
                else
                    dosObject.put(tempString1, tempString2);
        }

        return dosObject;
    }

    public void setValue(DOSChangeable value)
    {
        String tempString1 = null;
        String tempString2 = null;
        Object object = null;
        JComboBox tempCombo = null;
        tempString1 = (String)value.get("object.type");
        if(Utils.isNullString(tempString1))
            return;
        if(!tempString1.equals("activity"))
            return;
        ouid = (String)value.get("ouid");
        processOuid = (String)value.get("ouid@process.definition");
        x = Utils.getInt((Integer)value.get("x"));
        y = Utils.getInt((Integer)value.get("y"));
        idTextField.setText((String)value.get("identifier"));
        nameTextField.setText((String)value.get("name"));
        responsibleTextField.setText((String)value.get("participant"));
        for(i = 0; i < attributeNames.length; i++)
        {
            tempString1 = attributeNames[i][1];
            object = value.get(tempString1);
            tempCombo = (JComboBox)comboMap.get(tempString1);
            if(tempCombo != null)
            {
                if(tempString1.equals("type"))
                {
                    tempString2 = (String)value.get("ouid@type");
                    if(!Utils.isNullString(tempString2))
                    {
                        for(int j = 0; j < typeArray.length; j++)
                            if(typeArray[j][0].equals(tempString2))
                            {
                                tempCombo.setSelectedIndex(j);
                                j = typeArray.length + 1;
                            }

                    }
                } else
                if(tempString1.equals("mode.start") || tempString1.equals("mode.finish"))
                {
                    if(tempString1.equals("mode.start"))
                        tempString2 = (String)value.get("mode.start");
                    else
                    if(tempString1.equals("mode.finish"))
                        tempString2 = (String)value.get("mode.finish");
                    tempCombo.setSelectedIndex(-1);
                    for(int j = 0; j < startFinishArray.length; j++)
                        if(startFinishArray[j][0].equals(tempString2))
                        {
                            tempCombo.setSelectedIndex(j);
                            j = startFinishArray.length + 1;
                        }

                } else
                if(tempString1.equals("join") || tempString1.equals("split"))
                {
                    if(tempString1.equals("join"))
                        tempString2 = (String)value.get("join");
                    else
                    if(tempString1.equals("split"))
                        tempString2 = (String)value.get("split");
                    tempCombo.setSelectedIndex(-1);
                    for(int j = 0; j < joinSplitArray.length; j++)
                        if(joinSplitArray[j][0].equals(tempString2))
                        {
                            tempCombo.setSelectedIndex(j);
                            j = joinSplitArray.length + 1;
                        }

                }
                if(attributeNames[i][2] != null && attributeNames[i][2].indexOf("frozen") >= 0 && !Utils.isNullString(ouid))
                    tempCombo.setEnabled(false);
                tempCombo = null;
            } else
            {
                if(object != null)
                    attributeTextFields[i].setText(object.toString());
                else
                    attributeTextFields[i].setText("");
                if(attributeNames[i][2] != null && attributeNames[i][2].indexOf("frozen") >= 0 && !Utils.isNullString(ouid))
                    attributeTextFields[i].setEnabled(false);
            }
        }

        if(tabButtons[0].isSelected())
            actionPerformed(new ActionEvent(tabButtons[0], 0, "attribute"));
        if(eventPanel != null)
        {
            ((EventInformation)eventPanel).setOuid(ouid);
        } else
        {
            getEventPanel();
            if(eventPanel != null)
                ((EventInformation)eventPanel).setOuid(ouid);
        }
    }

    public void actionPerformed(ActionEvent evt)
    {
        String actionCommand = evt.getActionCommand();
        if(actionCommand.equals("attribute"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[0].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getAttributePanel();
            tabPanel.add(UIFactory.createStrippedScrollPane(contentPanel), "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("work"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[1].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getWorkPanel();
            tabPanel.add(contentPanel, "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("parameter"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[2].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getParameterPanel();
            tabPanel.add(contentPanel, "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("event"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[3].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getEventPanel();
            tabPanel.add(contentPanel, "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("type"))
        {
            String tempString = null;
            JComboBox tempCombo = (JComboBox)evt.getSource();
            tempString = typeArray[tempCombo.getSelectedIndex()][0];
            if("240".equals(tempString) && !Utils.isNullString(ouid))
                tabButtons[2].setEnabled(true);
            else
                tabButtons[2].setEnabled(false);
        } else
        if(actionCommand.equals("parameterStatusClassCombo"))
        {
            getStatusFromList((String)((ArrayList)parameterStatusClassList.get(parameterStatusClassCombo.getSelectedIndex())).get(0));
            if(parameterStatusFromList.size() == 0)
            {
                parameterStatusToList.clear();
                parameterStatusToCombo.updateUI();
            }
        } else
        if(actionCommand.equals("parameterStatusFromCombo"))
            getStatusToList((String)((ArrayList)parameterStatusClassList.get(parameterStatusClassCombo.getSelectedIndex())).get(0), (String)((ArrayList)parameterStatusFromList.get(parameterStatusFromCombo.getSelectedIndex())).get(0));
        else
        if(actionCommand.equals("parameterStatus.add"))
        {
            if(parameterStatusClassCombo.getSelectedIndex() < 0 || parameterStatusFromCombo.getSelectedIndex() < 0 || parameterStatusToCombo.getSelectedIndex() < 0)
                return;
            HashMap map = new HashMap();
            map.put("ouid@activity", ouid);
            map.put("ouid@class", ((ArrayList)parameterStatusClassList.get(parameterStatusClassCombo.getSelectedIndex())).get(0));
            map.put("from.id", ((ArrayList)parameterStatusFromList.get(parameterStatusFromCombo.getSelectedIndex())).get(0));
            map.put("to.id", ((ArrayList)parameterStatusToList.get(parameterStatusToCombo.getSelectedIndex())).get(0));
            try
            {
                WorkflowModeler.wfm.addActivityStatusMap(map);
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
            map.clear();
            map = null;
            parameterStatusRefreshButton.doClick();
        } else
        if(actionCommand.equals("parameterStatus.refresh"))
        {
            reloadParameterStatusClassData();
            parameterStatusTable.changeTableData();
        } else
        if(actionCommand.equals("parameterStatus.delete"))
        {
            int rowForDelete = 0;
            ArrayList tempList = null;
            ArrayList tempList2 = null;
            DynaTable table = (DynaTable)parameterStatusTable.getTable();
            if(table.getSelectedRowCount() == 0)
                return;
            int selrows[] = table.getSelectedRows();
            if(table.getSelectedRowCount() > 0)
            {
                tempList = parameterStatusTable.getSelectedOuidRows(selrows);
                if(!Utils.isNullArrayList(tempList))
                {
                    for(int n = tempList.size() - 1; n >= 0; n--)
                    {
                        rowForDelete = (new Integer((String)tempList.get(n))).intValue();
                        tempList2 = (ArrayList)parameterStatusData.remove(rowForDelete);
                    }

                }
                try
                {
                    WorkflowModeler.wfm.removeActivityStatusMap((HashMap)tempList2.get(0));
                }
                catch(IIPRequestException e)
                {
                    System.err.println(e);
                }
                parameterStatusTable.changeTableData();
            }
        }
    }

    private int i;
    private String ouid;
    private String processOuid;
    private int x;
    private int y;
    private JPanel topPanel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel responsibleLabel;
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField responsibleTextField;
    private JPanel tabPanel;
    private JPanel tabTitlePanel;
    private JToggleButton tabButtons[];
    private JPanel attributePanel;
    private JPanel workPanel;
    private JPanel parameterPanel;
    private JPanel eventPanel;
    private JLabel attributeLabels[];
    private JTextField attributeTextFields[];
    private HashMap comboMap;
    private JComboBox tempCombo;
    private String attributeNames[][] = {
        {
            "Process", "process.name", "readonly"
        }, {
            "Type", "type", "mandatory.combobox.frozen"
        }, {
            "Limit", "limit", 0
        }, {
            "Start mode", "mode.start", "combobox"
        }, {
            "Finish mode", "mode.finish", "combobox"
        }, {
            "Priority", "priority", 0
        }, {
            "Icon", "Icon", 0
        }, {
            "Instantiation", "instantiation", 0
        }, {
            "Loop", "loop", 0
        }, {
            "Join", "join", "combobox"
        }, {
            "Split", "split", "combobox"
        }, {
            "Description", "description", 0
        }
    };
    private int parameterStatusColumnWidth;
    private ArrayList parameterStatusList;
    private ArrayList parameterStatusNames;
    private ArrayList parameterStatusWidths;
    private ArrayList parameterStatusData;
    private ArrayList parameterStatusClassList;
    private ArrayList parameterStatusFromList;
    private ArrayList parameterStatusToList;
    private HashMap parameterStatusClassMap;
    private JSplitPane parameterStatusSplitPane;
    private JPanel parameterStatusPanel1;
    private JToolBar parameterStatusToolBar1;
    private JButton parameterStatusRefreshButton;
    private JButton parameterStatusDeleteButton;
    private JScrollPane parameterStatusScroll;
    private Table parameterStatusTable;
    private JPanel parameterStatusPanel2;
    private JToolBar parameterStatusToolBar2;
    private JButton parameterStatusAddButton;
    private JPanel parameterStatusCenterPanel2;
    private JLabel parameterStatusClassLabel;
    private JComboBox parameterStatusClassCombo;
    private JLabel parameterStatusStatusLabel;
    private JLabel parameterStatusFromLabel;
    private JComboBox parameterStatusFromCombo;
    private JLabel parameterStatusToLabel;
    private JComboBox parameterStatusToCombo;
    private String typeArray[][] = {
        {
            "100", "G", "Generic"
        }, {
            "110", "N", "Notification"
        }, {
            "230", "E", "E-mail Notification"
        }, {
            "120", "R", "Review"
        }, {
            "130", "A", "Approval"
        }, {
            "160", "B", "In-line Block"
        }, {
            "170", "0", "Route"
        }, {
            "200", "S", "Sub-Workflow"
        }, {
            "210", "K", "Lock"
        }, {
            "220", "U", "Unlock"
        }, {
            "240", "C", "Change Object Status"
        }, {
            "180", "s", "Start"
        }, {
            "190", "f", "Finish"
        }
    };
    private String startFinishArray[][] = {
        {
            "A", "Automatic"
        }, {
            "M", "Manual"
        }
    };
    private String joinSplitArray[][] = {
        {
            "A", "And"
        }, {
            "X", "X-OR"
        }
    };






}
