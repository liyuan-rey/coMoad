// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventInformation.java

package dyna.framework.editor.workflow;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.DSS;
import dyna.framework.service.DTM;
import dyna.framework.service.WFM;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaComboBox;
import dyna.uic.DynaComboBoxDataLoader;
import dyna.uic.DynaComboBoxModel;
import dyna.uic.DynaTextField;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import dyna.util.Utils;
import dyna.util.notepad.Notepad;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JViewport;

// Referenced classes of package dyna.framework.editor.workflow:
//            WorkflowModeler, ProcessPanel, ActivityPanel, ScriptSelection

public class EventInformation extends JPanel
    implements ActionListener, MouseListener
{
    class DynaComboExtInstance extends DynaComboBoxDataLoader
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
            return setExtComboBox();
        }

        DynaComboExtInstance()
        {
        }
    }

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


    public EventInformation(Object parentFrame)
    {
        eventTableScrPane = null;
        eventListTable = null;
        handCursor = new Cursor(12);
        imageEndOneClass = new ImageIcon("icons/AssociationEnd1.gif");
        imageEndTwoClass = new ImageIcon("icons/AssociationEnd2.gif");
        associationToolBar = new JToolBar(0);
        associationBoxLayout = new BoxLayout(associationToolBar, 0);
        okButton = null;
        modifyButton = null;
        finishButton = null;
        removeButton = null;
        dos = null;
        dtm = null;
        dss = null;
        wfm = null;
        dComboExt = new DynaComboExtInstance();
        dComboType = new DynaComboTypeInstance();
        extComboModel = new DynaComboBoxModel(dComboExt);
        typeComboModel = new DynaComboBoxModel(dComboType);
        assoOuid = "";
        eventListData = new ArrayList();
        columnNames = new ArrayList();
        columnWidths = new ArrayList();
        modelName = "";
        classCode = "";
        try
        {
            parent = parentFrame;
            dos = WorkflowModeler.dos;
            dss = WorkflowModeler.dss;
            wfm = WorkflowModeler.wfm;
            makeEventTable();
            initialize();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void initialize()
    {
        okButton = new JButton();
        modifyButton = new JButton();
        finishButton = new JButton();
        removeButton = new JButton();
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel = new JPanel();
        eventTableScrPane = UIFactory.createStrippedScrollPane(null);
        eventTableScrPane.setViewportView(eventListTable.getTable());
        eventTableScrPane.setPreferredSize(new Dimension(360, 260));
        eventTableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        associationInfoBorderLayout = new BorderLayout();
        setLayout(associationInfoBorderLayout);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        mainPanel.setLayout(gridBag);
        ouidTextField = new DynaTextField();
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setTitleWidth(140);
        ouidTextField.setTitleVisible(true);
        ouidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(ouidTextField, gridBagCon);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(true);
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(140);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(nameTextField, gridBagCon);
        scriptSelectButton = new JButton();
        scriptSelectButton.setText("...");
        scriptSelectButton.setToolTipText("Select Script");
        scriptSelectButton.setActionCommand("Select Script");
        scriptSelectButton.setPreferredSize(new Dimension(20, 20));
        scriptSelectButton.addActionListener(this);
        scriptSelectButton.setEnabled(true);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.insets = new Insets(2, 0, 2, 5);
        gridBag.setConstraints(scriptSelectButton, gridBagCon);
        extensionComboBox = new DynaComboBox();
        extensionComboBox.setMandatory(true);
        extensionComboBox.setTitleText("Script Language");
        extensionComboBox.setTitleWidth(140);
        extensionComboBox.setTitleVisible(true);
        extensionComboBox.setEnabled(false);
        extensionComboBox.setModel(extComboModel);
        extensionComboBox.setActionCommand("Ext");
        extensionComboBox.addActionListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 2;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        gridBag.setConstraints(extensionComboBox, gridBagCon);
        typeComboBox = new DynaComboBox();
        typeComboBox.setMandatory(true);
        typeComboBox.setTitleText("Event Type");
        typeComboBox.setTitleWidth(140);
        typeComboBox.setTitleVisible(true);
        typeComboBox.setEditable(false);
        typeComboBox.setModel(typeComboModel);
        typeComboBox.setActionCommand("Type");
        typeComboBox.addActionListener(this);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(typeComboBox, gridBagCon);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(scriptSelectButton);
        mainPanel.add(extensionComboBox);
        mainPanel.add(typeComboBox);
        mainScrPane.getViewport().add(mainPanel, null);
        associationToolBar.setAlignmentX(0.0F);
        associationToolBar.setBorder(BorderFactory.createEtchedBorder());
        okButton.setEnabled(true);
        okButton.setToolTipText("create");
        okButton.setActionCommand("create");
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/Registry.gif")));
        okButton.addActionListener(this);
        finishButton.setToolTipText("clear");
        finishButton.setActionCommand("clear");
        finishButton.setMargin(new Insets(0, 0, 0, 0));
        finishButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        finishButton.addActionListener(this);
        removeButton.setToolTipText("delete");
        removeButton.setActionCommand("delete");
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        removeButton.addActionListener(this);
        associationToolBar.add(okButton);
        associationToolBar.add(finishButton);
        associationToolBar.add(removeButton);
        add(associationToolBar, "North");
        add(mainScrPane, "Center");
        add(eventTableScrPane, "South");
        extComboModel.enableDataLoad();
        typeComboModel.enableDataLoad();
        extComboModel.setElementAt(-1);
        typeComboModel.setElementAt(-1);
    }

    public void setOuid(String ouid)
    {
        try
        {
            ouidTextField.setText(ouid);
            if(parent instanceof ProcessPanel)
            {
                DOSChangeable dosProcess = wfm.getProcessDefinition(ouid);
                modelName = (String)dosProcess.get("identifier");
                clearAllField();
                refreshEventTable(ouid);
            } else
            if(parent instanceof ActivityPanel)
            {
                DOSChangeable dosActivity = wfm.getActivityDefinition(ouid);
                classCode = (String)dosActivity.get("identifier");
                clearAllField();
                refreshEventTable(ouid);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void makeEventTable()
    {
        columnNames.add("Ouid");
        columnNames.add("Name");
        columnNames.add("Event Type ID");
        columnWidths.add(new Integer(240));
        columnWidths.add(new Integer(240));
        columnWidths.add(new Integer(240));
        eventListTable = new Table(eventListData, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 750);
        eventListTable.getTable().addMouseListener(this);
        eventListTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        eventListTable.setIndexColumn(0);
        eventListTable.getTable().setCursor(handCursor);
    }

    public boolean createEvent()
    {
        boolean isCreated = false;
        try
        {
            String ouid = ouidTextField.getText();
            String eventType = (String)typeComboModel.getSelectedOID();
            String scriptName = nameTextField.getText() + "." + (String)extComboModel.getSelectedOID();
            java.util.List tmpList = Utils.tokenizeMessage(scriptName, ' ');
            scriptName = "";
            for(int i = 0; i < tmpList.size(); i++)
                if(i != tmpList.size() - 1)
                    scriptName = scriptName + (String)tmpList.get(i) + "_";
                else
                    scriptName = scriptName + (String)tmpList.get(i);

            if(!Utils.isNullString(ouid) && !Utils.isNullString(eventType) && !Utils.isNullString(scriptName))
            {
                isCreated = dos.setEvent(ouid, eventType, scriptName);
                if(isCreated)
                {
                    File newScriptFile = new File("tmp/" + scriptName);
                    boolean newFile = newScriptFile.createNewFile();
                    if(newFile)
                    {
                        boolean folderExist = true;
                        if(!dss.exists("/script"))
                            folderExist = dss.makeFolder("/script", "");
                        if(folderExist)
                        {
                            dss.uploadFile("/script/" + scriptName, newScriptFile.getCanonicalPath(), null);
                            JOptionPane.showMessageDialog(this, "Modified.", "Modified", 1);
                        }
                    }
                }
                refreshEventTable(ouid);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return isCreated;
    }

    public void removeEvent()
    {
        try
        {
            Object option[] = {
                "Yes", "No"
            };
            int res = JOptionPane.showOptionDialog(this, "Event will be deleted. Are you sure?", "Event delete", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[0]);
            if(res != 0)
                return;
            System.out.println((String)typeComboModel.getSelectedOID());
            dos.removeEvent(ouidTextField.getText(), (String)typeComboModel.getSelectedOID());
            refreshEventTable(ouidTextField.getText());
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void setAssociationInfoField(ArrayList dataList)
    {
        String extension = "";
        String fullName = (String)dataList.get(1);
        java.util.List tmpList = Utils.tokenizeMessage(fullName, '.');
        String name = (String)tmpList.get(0);
        if(tmpList.size() > 1)
            extension = (String)tmpList.get(1);
        nameTextField.setText(name);
        extComboModel.setSelectedItemByOID(extension);
        extensionComboBox.updateUI();
        typeComboModel.setSelectedItemByOID(dataList.get(2));
        typeComboBox.updateUI();
    }

    public void refreshEventTable(String ouid)
    {
        try
        {
            eventListData.clear();
            ArrayList eventList = null;
            if(parent instanceof ProcessPanel)
                eventList = dos.listEvent(ouid);
            else
            if(parent instanceof ActivityPanel)
                eventList = dos.listEvent(ouid);
            if(eventList == null)
            {
                eventListTable.changeTableData();
                return;
            }
            for(int i = 0; i < eventList.size(); i++)
            {
                ArrayList tmpList = new ArrayList();
                HashMap tmpDOS = (HashMap)eventList.get(i);
                tmpList.add((String)tmpDOS.get("ouid"));
                tmpList.add((String)tmpDOS.get("name.script"));
                tmpList.add((String)tmpDOS.get("type.event"));
                eventListData.add(tmpList.clone());
                tmpList.clear();
            }

            eventListTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public ArrayList setExtComboBox()
    {
        ArrayList typeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        typeAL.add("py");
        typeAL.add("Python");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add("tcl");
        typeAL.add("Tcl");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        typeAL.add("java");
        typeAL.add("BeanShell");
        returnAL.add(typeAL.clone());
        typeAL.clear();
        return returnAL;
    }

    public void setNameTextField(String name)
    {
        nameTextField.setText(name);
    }

    public ArrayList setTypeComboBox()
    {
        ArrayList typeAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        if((parent instanceof ProcessPanel) || (parent instanceof ActivityPanel))
        {
            typeAL.add("add.init");
            typeAL.add("EVENT_ADD_INIT");
            returnAL.add(typeAL.clone());
            typeAL.clear();
            typeAL.add("start.before");
            typeAL.add("EVENT_START_BEFORE");
            returnAL.add(typeAL.clone());
            typeAL.clear();
            typeAL.add("start.after");
            typeAL.add("EVENT_START_AFTER");
            returnAL.add(typeAL.clone());
            typeAL.clear();
            typeAL.add("finish.before");
            typeAL.add("EVENT_FINISH_BEFORE");
            returnAL.add(typeAL.clone());
            typeAL.clear();
            typeAL.add("finish.after");
            typeAL.add("EVENT_FINISH_AFTER");
            returnAL.add(typeAL.clone());
            typeAL.clear();
        }
        return returnAL;
    }

    public void mouseClicked(MouseEvent e)
    {
        int selectedRow = eventListTable.getTable().getSelectedRow();
        if(selectedRow > -1)
        {
            String ouidRow = eventListTable.getSelectedOuidRow(selectedRow);
            int row = (new Integer(ouidRow)).intValue();
            ArrayList selectedList = (ArrayList)eventListData.get(row);
            setAssociationInfoField(selectedList);
            nameTextField.setEditable(true);
            scriptSelectButton.setEnabled(true);
            extensionComboBox.setEnabled(true);
        }
        if(e.getClickCount() == 2)
        {
            String scriptName = nameTextField.getText() + "." + (String)extComboModel.getSelectedOID();
            try
            {
                dss.downloadFileForce("/script/" + scriptName, "tmp/" + scriptName, null);
                File newScriptFile = new File("tmp/" + scriptName);
                Notepad newEditor;
                if(newScriptFile.exists())
                    newEditor = new Notepad(newScriptFile);
                else
                    newEditor = new Notepad(null);
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        }
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

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("create"))
            createEvent();
        else
        if(command.equals("clear"))
            clearAllField();
        else
        if(command.equals("delete"))
        {
            removeEvent();
            clearAllField();
        } else
        if(command.equals("Ext"))
        {
            if(extensionComboBox.getSelectedIndex() > -1)
            {
                nameTextField.setEditable(true);
                scriptSelectButton.setEnabled(true);
                if(parent instanceof ProcessPanel)
                    nameTextField.setText(modelName + "_" + (String)typeComboModel.getSelectedItem());
                else
                if(parent instanceof ActivityPanel)
                    nameTextField.setText(classCode + "_" + (String)typeComboModel.getSelectedItem());
            } else
            {
                nameTextField.setEditable(false);
                scriptSelectButton.setEnabled(false);
            }
        } else
        if(command.equals("Type"))
        {
            if(extensionComboBox.getSelectedIndex() < 0)
            {
                if(typeComboBox.getSelectedIndex() > -1)
                    extensionComboBox.setEnabled(true);
                else
                    extensionComboBox.setEnabled(false);
            } else
            {
                nameTextField.setEditable(true);
                scriptSelectButton.setEnabled(true);
                if(parent instanceof ProcessPanel)
                    nameTextField.setText(modelName + "_" + (String)typeComboModel.getSelectedItem());
                else
                if(parent instanceof ActivityPanel)
                    nameTextField.setText(classCode + "_" + (String)typeComboModel.getSelectedItem());
            }
        } else
        if(command.equals("Select Script"))
        {
            ScriptSelection scriptSelectPanel = new ScriptSelection(this, (String)extComboModel.getSelectedOID());
            scriptSelectPanel.show();
        }
    }

    public void clearAllField()
    {
        nameTextField.setText("");
        nameTextField.setEditable(false);
        scriptSelectButton.setEnabled(false);
        extensionComboBox.setSelectedIndex(-1);
        extensionComboBox.setEnabled(false);
        typeComboBox.setSelectedIndex(-1);
        updateUI();
    }

    private final int titleTextWidth = 140;
    private BorderLayout associationInfoBorderLayout;
    private BoxLayout packageInfoLayout;
    public Object parent;
    private JScrollPane mainScrPane;
    private JPanel mainPanel;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private JButton scriptSelectButton;
    private DynaTextField descriptionTextField;
    private DynaComboBox extensionComboBox;
    private DynaComboBox typeComboBox;
    private JScrollPane eventTableScrPane;
    private Table eventListTable;
    private Cursor handCursor;
    private ImageIcon imageEndOneClass;
    private ImageIcon imageEndTwoClass;
    private JToolBar associationToolBar;
    private BoxLayout associationBoxLayout;
    private JButton okButton;
    private JButton modifyButton;
    private JButton finishButton;
    private JButton removeButton;
    private DOS dos;
    private DTM dtm;
    private DSS dss;
    private WFM wfm;
    private DynaComboExtInstance dComboExt;
    private DynaComboTypeInstance dComboType;
    private DynaComboBoxModel extComboModel;
    private DynaComboBoxModel typeComboModel;
    private String assoOuid;
    private ArrayList eventListData;
    private ArrayList columnNames;
    private ArrayList columnWidths;
    private String modelName;
    private String classCode;
}
