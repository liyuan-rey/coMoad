// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectSearchConditionFrame.java

package dyna.framework.client.pms;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.framework.service.msr.MSRStgrep;
import dyna.uic.*;
import dyna.uic.table.DynaTableCellColor;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client.pms:
//            ProjectUtils

public class ProjectSearchConditionFrame extends JFrame
    implements ActionListener, WindowListener, MouseListener, DynaTableCellColor
{

    public ProjectSearchConditionFrame()
    {
        handCursor = new Cursor(12);
        mainSplitPane = new JSplitPane();
        mainPanel = new JPanel();
        searchButton = new JButton();
        leftPanel = new JPanel();
        centerPanel = new JPanel();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        conditionScrPane = UIFactory.createStrippedScrollPane(null);
        resultTable = new Table();
        nds = DynaMOAD.nds;
        dos = DynaMOAD.dos;
        NDS_CODE = null;
        orderOfCombo = new ArrayList();
        classOuid = null;
        classInfo = null;
        templateClassOuid = null;
        dateChooserButtonList = null;
        selectButtonList = null;
        fieldOuidList = null;
        msrStgrep = null;
        selectCnt = 0;
        wbsList = null;
        searchData = null;
        columnName = null;
        columnWidth = null;
        columnSequence = null;
        evenRowColor = new Color(237, 237, 249);
        templateLabel = null;
        templateComboBox = null;
        projectIdLabel = null;
        projectIdText = null;
        projectNameLabel = null;
        projectNameText = null;
        statusLabel = null;
        statusComboBox = null;
        strToday = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        try
        {
            NDS_CODE = nds.getSubSet("CODE");
            String packageOuid = null;
            DOSChangeable dosPackage = dos.getPackageWithName("Project");
            if(dosPackage == null)
                return;
            packageOuid = (String)dosPackage.get("ouid");
            if(packageOuid == null)
                return;
            DOSChangeable dosClass = dos.getClassWithName(packageOuid, "Project");
            if(dosClass == null)
                return;
            classOuid = (String)dosClass.get("ouid");
            classInfo = dos.getClass(classOuid);
            dosClass = dos.getClassWithName(packageOuid, "ProjectTemplate");
            if(dosClass == null)
                return;
            templateClassOuid = (String)dosClass.get("ouid");
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        initStatus();
        initTemplateComboBox();
        initComponents();
        makeSearchConditionPanel();
        makeSearchResultTable();
    }

    private void initComponents()
    {
        setSize(1024, 700);
        leftPanel.setLayout(null);
        leftPanel.setBackground(UIManagement.panelBackGround);
        leftPanel.setMinimumSize(new Dimension(300, 300));
        leftPanel.setMaximumSize(new Dimension(300, 300));
        leftPanel.setPreferredSize(new Dimension(300, 300));
        conditionScrPane.setPreferredSize(new Dimension(300, 300));
        conditionScrPane.setViewportView(leftPanel);
        searchButton.setActionCommand("search");
        searchButton.setText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.addActionListener(this);
        searchButton.setDoubleBuffered(true);
        scrollPane.setViewportView(resultTable.getTable());
        scrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(scrollPane, "Center");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(400, 300));
        mainPanel.add(centerPanel, "Center");
        mainSplitPane.setDividerLocation(300);
        mainSplitPane.setLeftComponent(conditionScrPane);
        mainSplitPane.setRightComponent(mainPanel);
        setTitle(DynaMOAD.getMSRString("INF_0011", "Project Present Condition Search", 0));
        addWindowListener(this);
        getContentPane().add(mainSplitPane);
    }

    private void initStatus()
    {
        try
        {
            TreeNodeObject value00 = new TreeNodeObject(null, "", null);
            TreeNodeObject value01 = new TreeNodeObject("AP1", nds.getValue(NDS_CODE + "/STATUS/AP1"), null);
            TreeNodeObject value02 = new TreeNodeObject("AP2", nds.getValue(NDS_CODE + "/STATUS/AP2"), null);
            TreeNodeObject value03 = new TreeNodeObject("CKI", nds.getValue(NDS_CODE + "/STATUS/CKI"), null);
            TreeNodeObject value04 = new TreeNodeObject("CKO", nds.getValue(NDS_CODE + "/STATUS/CKO"), null);
            TreeNodeObject value05 = new TreeNodeObject("CRT", nds.getValue(NDS_CODE + "/STATUS/CRT"), null);
            TreeNodeObject value06 = new TreeNodeObject("OBS", nds.getValue(NDS_CODE + "/STATUS/OBS"), null);
            TreeNodeObject value07 = new TreeNodeObject("REJ", nds.getValue(NDS_CODE + "/STATUS/REJ"), null);
            TreeNodeObject value08 = new TreeNodeObject("RLS", nds.getValue(NDS_CODE + "/STATUS/RLS"), null);
            TreeNodeObject value09 = new TreeNodeObject("RV1", nds.getValue(NDS_CODE + "/STATUS/RV1"), null);
            TreeNodeObject value10 = new TreeNodeObject("RV2", nds.getValue(NDS_CODE + "/STATUS/RV2"), null);
            TreeNodeObject value11 = new TreeNodeObject("WIP", nds.getValue(NDS_CODE + "/STATUS/WIP"), null);
            TreeNodeObject statusList[] = {
                value00, value01, value02, value03, value04, value05, value06, value07, value08, value09, 
                value10, value11
            };
            statusComboBox = new JComboBox(statusList);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    private void initTemplateComboBox()
    {
        try
        {
            ArrayList fields = new ArrayList();
            fields.add("86054380");
            fields.add("86054381");
            ArrayList tempList = dos.list(templateClassOuid, fields);
            if(tempList == null)
                tempList = new ArrayList();
            TreeNodeObject templates[] = new TreeNodeObject[tempList.size()];
            TreeNodeObject tempObject = null;
            ArrayList tempList2 = null;
            for(int i = 0; i < tempList.size(); i++)
            {
                tempList2 = (ArrayList)tempList.get(i);
                if(!Utils.isNullArrayList(tempList2))
                {
                    tempObject = new TreeNodeObject((String)tempList2.get(0), (String)tempList2.get(2) + " [" + tempList2.get(1) + "]", null);
                    templates[i] = tempObject;
                    tempList2 = null;
                }
            }

            templateComboBox = new JComboBox(templates);
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
    }

    public void makeSearchResultTable()
    {
        try
        {
            TreeNodeObject template = null;
            template = (TreeNodeObject)templateComboBox.getSelectedItem();
            if(template == null)
                return;
            wbsList = ProjectUtils.getColumnsOfProjectStateSearch(template.getOuid());
            searchData = new ArrayList();
            columnName = new ArrayList();
            columnWidth = new ArrayList();
            DOSChangeable selectedResult = new DOSChangeable();
            int size = 2;
            if(!Utils.isNullArrayList(wbsList))
                size += wbsList.size();
            columnSequence = new int[size];
            for(int i = 0; i < size; i++)
                columnSequence[i] = i + 2;

            columnName.add("Project ID");
            columnWidth.add(new Integer(80));
            columnName.add("Project Name");
            columnWidth.add(new Integer(140));
            for(int i = 0; i < size - 2; i++)
            {
                columnName.add(((ArrayList)wbsList.get(i)).get(1));
                columnWidth.add(new Integer(80));
            }

            resultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79, false);
            resultTable.setColumnSequence(columnSequence);
            resultTable.setIndexColumn(0);
            resultTable.getTable().setCursor(handCursor);
            resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
            resultTable.getTable().addMouseListener(this);
            ((DynaTable)resultTable.getTable()).setCellColor(this);
            scrollPane.setViewportView(resultTable.getTable());
        }
        catch(Exception ie)
        {
            ie.printStackTrace();
        }
    }

    public void setResultData(ArrayList resultData)
    {
        if(searchData != null)
            searchData.clear();
        ArrayList tmpList = new ArrayList();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                ArrayList tmpList2 = (ArrayList)resultData.get(i);
                for(int j = 0; j < tmpList2.size(); j++)
                    tmpList.add(tmpList2.get(j));

                searchData.add(tmpList.clone());
                tmpList.clear();
            }

        }
        resultTable.changeTableData();
    }

    public void makeSearchConditionPanel()
    {
        try
        {
            int i = -1;
            int y_pos = 0;
            searchButton.setBounds(10, 10, 280, 20);
            leftPanel.add(searchButton);
            templateLabel = new JLabel(DynaMOAD.getMSRString("WRD_0149", "Template", 0));
            templateLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
            leftPanel.add(templateLabel);
            templateComboBox.setBounds(110, 10 + 23 * (i + 2), 180, 20);
            templateComboBox.setActionCommand("templateComboBox");
            templateComboBox.addActionListener(this);
            leftPanel.add(templateComboBox);
            i++;
            projectIdLabel = new JLabel("Project ID");
            projectIdLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
            leftPanel.add(projectIdLabel);
            projectIdText = new JTextField();
            projectIdText.setBounds(110, 10 + 23 * (i + 2), 180, 20);
            leftPanel.add(projectIdText);
            i++;
            projectNameLabel = new JLabel("Project " + DynaMOAD.getMSRString("WRD_0103", "Name", 0));
            projectNameLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
            leftPanel.add(projectNameLabel);
            projectNameText = new JTextField();
            projectNameText.setBounds(110, 10 + 23 * (i + 2), 180, 20);
            leftPanel.add(projectNameText);
            i++;
            statusLabel = new JLabel(DynaMOAD.getMSRString("WRD_0024", "Status", 0));
            statusLabel.setBounds(10, 10 + 23 * (i + 2), 100, 20);
            leftPanel.add(statusLabel);
            statusComboBox.setBounds(110, 10 + 23 * (i + 2), 180, 20);
            leftPanel.add(statusComboBox);
            leftPanel.setMinimumSize(new Dimension(300, y_pos));
            leftPanel.setMaximumSize(new Dimension(300, y_pos));
            leftPanel.setPreferredSize(new Dimension(300, y_pos));
            conditionScrPane.setPreferredSize(new Dimension(300, 300));
            conditionScrPane.setViewportView(leftPanel);
        }
        catch(Exception ie)
        {
            ie.printStackTrace();
        }
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    private void closeDialog(WindowEvent evt)
    {
        searchButton.removeActionListener(this);
        setVisible(false);
        dispose();
    }

    public void actionPerformed(ActionEvent evt)
    {
        String command = evt.getActionCommand();
        if(command == null)
            closeDialog(null);
        else
        if(command.equals("close"))
            closeDialog(null);
        else
        if(command.equals("search"))
            try
            {
                TreeNodeObject template = null;
                TreeNodeObject status = null;
                template = (TreeNodeObject)templateComboBox.getSelectedItem();
                if(template == null)
                    return;
                status = (TreeNodeObject)statusComboBox.getSelectedItem();
                String searchSQL = ProjectUtils.makeSQLForProjectStateSearch(wbsList, projectIdText.getText(), projectNameText.getText(), status.getOuid());
                if(Utils.isNullString(searchSQL))
                    return;
                ArrayList tempList = ProjectUtils.projectStateSearch(searchSQL);
                setResultData(tempList);
            }
            catch(Exception ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("templateComboBox"))
            makeSearchResultTable();
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        closeDialog(windowEvent);
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

    public void windowClosed(WindowEvent windowevent)
    {
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
        String selectOuid = null;
        if(mouseEvent.getSource() == resultTable.getTable() && mouseEvent.getClickCount() == 2)
            try
            {
                int row = resultTable.getTable().getSelectedRow();
                Number ouidNumber = (Number)((ArrayList)searchData.get(row)).get(1);
                selectOuid = "project$vf@" + Long.toHexString(ouidNumber.longValue());
                selectOuid = selectOuid.trim();
                boolean chk = false;
                if(!LogIn.isAdmin)
                    chk = DynaMOAD.acl.hasPermission(classOuid, selectOuid, "READ", LogIn.userID);
                else
                    chk = true;
                if(chk)
                {
                    String selectClassOuid = dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, selectOuid, 1);
                    Object returnObject = Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.before"), DynaMOAD.dss, uiGeneration);
                    if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
                    {
                        uiGeneration.windowClosing(null);
                    } else
                    {
                        uiGeneration.setVisible(true);
                        Utils.executeScriptFile(dos.getEventName(selectClassOuid, "read.after"), DynaMOAD.dss, uiGeneration);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public Color getBackground(DynaTable table, int row, int column)
    {
        if(column > 1 && row > 0 && row % 2 == 1)
        {
            String curdata = (String)table.getValueAt(row, column);
            if(curdata == null || curdata.equals(""))
            {
                String data = (String)table.getValueAt(row - 1, column);
                if(data != null && !data.equals("") && data.compareTo(strToday) < 0)
                    return Color.red;
            }
        }
        if(row % 4 == 0 || row % 4 == 1)
            return table.getBackground();
        else
            return evenRowColor;
    }

    public Color getForeground(DynaTable table, int row, int column)
    {
        return table.getForeground();
    }

    private Cursor handCursor;
    private JSplitPane mainSplitPane;
    private JPanel mainPanel;
    private JButton searchButton;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JScrollPane scrollPane;
    private JScrollPane conditionScrPane;
    private Table resultTable;
    final int title_width = 100;
    final int title_height = 20;
    final int DIV_SIZE = 300;
    private NDS nds;
    private DOS dos;
    private String NDS_CODE;
    private ArrayList orderOfCombo;
    private String classOuid;
    private DOSChangeable classInfo;
    private String templateClassOuid;
    private final byte DATATYPE_DATE = 22;
    private final byte DATATYPE_UTF = 13;
    private final byte DATATYPE_BOOLEAN = 1;
    private final int dateButtonWidth = 20;
    private ArrayList dateChooserButtonList;
    private ArrayList selectButtonList;
    private static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList fieldOuidList;
    private MSRStgrep msrStgrep;
    private int selectCnt;
    private ArrayList wbsList;
    private ArrayList searchData;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private int columnSequence[];
    private Color evenRowColor;
    final int init_xcord = 10;
    final int init_ycord = 0;
    final int button_xcord = 15;
    final int titleWidth = 100;
    final int totalWidth = 300;
    final int fieldHeight = 20;
    final int offset = 3;
    final int buttonOffset = 10;
    final int condition_xsize = 300;
    final int condition_ysize = 300;
    final int buttonWidth = 76;
    final int buttonHeight = 0;
    private JLabel templateLabel;
    private JComboBox templateComboBox;
    private JLabel projectIdLabel;
    private JTextField projectIdText;
    private JLabel projectNameLabel;
    private JTextField projectNameText;
    private JLabel statusLabel;
    private JComboBox statusComboBox;
    String strToday;

}
