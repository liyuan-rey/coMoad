// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CodeSelectDialog.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.editor.modeler.CodeSelectionMatrixDialog;
import dyna.framework.editor.modeler.ObjectModelDefaultTreeCellRenderer;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, CodeChooserUser

public class CodeSelectDialog extends JDialog
    implements ActionListener, WindowListener, MouseListener, TreeSelectionListener
{

    public static String[] showDialog(Frame parent, Component comp, boolean modal, String ouid, String signStr)
    {
        CodeSelectDialog csd = new CodeSelectDialog(parent, comp, modal, ouid, signStr);
        csd.setVisible(true);
        return csd.returnValue;
    }

    public CodeSelectDialog(Frame parent, Component comp, boolean modal, String ouid, String signStr)
    {
        super(parent, modal);
        returnValue = null;
        handCursor = new Cursor(12);
        mainPanel = new JPanel();
        toolBar = new JToolBar();
        toolPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        resultTable = new Table();
        hTree = null;
        renderer = null;
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        dos = DynaMOAD.dos;
        newUI = DynaMOAD.newUI;
        searchData = null;
        columnName = null;
        columnWidth = null;
        this.ouid = "";
        hoardData = new DOSChangeable();
        this.signStr = null;
        sourceIdentifier = null;
        parentFr = null;
        isHierarchy = false;
        parentFr = parent;
        this.ouid = ouid;
        this.signStr = signStr;
        initialize();
        searchButton.doClick();
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
    }

    public static String[] showDialog(Dialog parent, Component comp, boolean modal, String ouid, String signStr)
    {
        CodeSelectDialog csd = new CodeSelectDialog(parent, comp, modal, ouid, signStr);
        csd.setVisible(true);
        return csd.returnValue;
    }

    public CodeSelectDialog(Dialog parent, Component comp, boolean modal, String ouid, String signStr)
    {
        super(parent, modal);
        returnValue = null;
        handCursor = new Cursor(12);
        mainPanel = new JPanel();
        toolBar = new JToolBar();
        toolPanel = new JPanel();
        searchButton = new JButton();
        detailButton = new JButton();
        selectButton = new JButton();
        closeButton = new JButton();
        centerPanel = new JPanel();
        optionPanel = new JPanel();
        fieldComboBox = new JComboBox();
        valueTextField = new JTextField();
        scrollPane = UIFactory.createStrippedScrollPane(null);
        resultTable = new Table();
        hTree = null;
        renderer = null;
        classID = null;
        classChangable = false;
        defaultField = "NAM";
        defaultOperator = "like";
        defaultSearchValue = "";
        searchSQL = null;
        resultSetter = null;
        resultSetterObject = null;
        dos = DynaMOAD.dos;
        newUI = DynaMOAD.newUI;
        searchData = null;
        columnName = null;
        columnWidth = null;
        this.ouid = "";
        hoardData = new DOSChangeable();
        this.signStr = null;
        sourceIdentifier = null;
        parentFr = null;
        isHierarchy = false;
        parentFr = parent;
        this.ouid = ouid;
        this.signStr = signStr;
        if("initial".equals(signStr))
        {
            sourceIdentifier = "initial";
            this.signStr = null;
        }
        initialize();
        searchButton.doClick();
        pack();
        UIUtils.setLocationRelativeTo(this, comp);
    }

    private void initComponentsForTable()
    {
        addWindowListener(this);
        mainPanel.setLayout(new BorderLayout());
        toolBar.add(Box.createHorizontalGlue());
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.setBackground(UIManagement.panelBackGround);
        searchButton.setActionCommand("search");
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "Search", 3));
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.addActionListener(this);
        searchButton.setDoubleBuffered(true);
        toolBar.add(searchButton);
        selectButton.setActionCommand("select");
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Select", 3));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        toolBar.add(selectButton);
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(10));
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolBar.add(closeButton);
        toolBar.setDoubleBuffered(true);
        toolBar.setBackground(UIManagement.panelBackGround);
        toolPanel.add(closeButton);
        mainPanel.add(toolPanel, "South");
        centerPanel.setLayout(new BorderLayout());
        optionPanel.setLayout(new BoxLayout(optionPanel, 0));
        optionPanel.setBackground(UIManagement.panelBackGround);
        fieldComboBox.setDoubleBuffered(true);
        fieldComboBox.setBackground(UIManagement.panelBackGround);
        fieldComboBox.setBorder(UIManagement.borderTextBoxEditable);
        fieldComboBox.setPreferredSize(new Dimension(80, 10));
        fieldComboBox.setActionCommand("fieldCombo");
        fieldComboBox.addActionListener(this);
        optionPanel.add(fieldComboBox);
        optionPanel.add(Box.createHorizontalStrut(5));
        valueTextField.setDoubleBuffered(true);
        valueTextField.setPreferredSize(new Dimension(100, 10));
        optionPanel.add(valueTextField);
        optionPanel.add(Box.createHorizontalStrut(5));
        optionPanel.setDoubleBuffered(true);
        optionPanel.add(searchButton);
        centerPanel.add(optionPanel, "North");
        scrollPane.setViewportView(resultTable.getTable());
        scrollPane.setDoubleBuffered(true);
        scrollPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        centerPanel.add(scrollPane, "Center");
        centerPanel.setDoubleBuffered(true);
        mainPanel.add(centerPanel, "Center");
        mainPanel.setDoubleBuffered(true);
        mainPanel.setPreferredSize(new Dimension(270, 270));
        setTitle(DynaMOAD.getMSRString("WRD_0137", "Select Code", 3));
        getContentPane().add(mainPanel);
    }

    private void initComponentsForTree()
    {
        addWindowListener(this);
        mainPanel.setLayout(new BorderLayout());
        toolPanel.setLayout(new BoxLayout(toolPanel, 0));
        toolPanel.add(Box.createHorizontalGlue());
        toolPanel.setBackground(UIManagement.panelBackGround);
        selectButton.setActionCommand("select");
        selectButton.setEnabled(false);
        selectButton.setText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        selectButton.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "select", 3));
        selectButton.setMargin(new Insets(0, 0, 0, 0));
        selectButton.addActionListener(this);
        selectButton.setDoubleBuffered(true);
        toolPanel.add(selectButton);
        toolPanel.add(Box.createHorizontalStrut(10));
        closeButton.setActionCommand("close");
        closeButton.setText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        closeButton.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "Close", 3));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(this);
        closeButton.setDoubleBuffered(true);
        toolPanel.add(closeButton);
        mainPanel.add(toolPanel, "South");
        centerPanel.setLayout(new BorderLayout());
        scrollPane.setViewportView(hTree);
        centerPanel.add(scrollPane, "Center");
        centerPanel.setDoubleBuffered(true);
        mainPanel.add(centerPanel, "Center");
        mainPanel.setDoubleBuffered(true);
        mainPanel.setPreferredSize(new Dimension(270, 270));
        setTitle(DynaMOAD.getMSRString("WRD_0137", "Select Code", 3));
        getContentPane().add(mainPanel);
    }

    public void setClassID(String classID)
    {
        this.classID = classID;
    }

    public void setClassChangable(boolean changable)
    {
        classChangable = changable;
    }

    public void setDefaultField(String field)
    {
        defaultField = field;
    }

    public void setDefaultOperator(String operator)
    {
        defaultOperator = operator;
    }

    public void setDefaultSearchValue(String value)
    {
        defaultSearchValue = value;
    }

    public void setSearchSQL(String sql)
    {
        searchSQL = sql;
    }

    public void setResultSetter(String setter)
    {
        resultSetter = setter;
    }

    public void setResultSetterObject(Object object)
    {
        resultSetterObject = object;
    }

    private void makeTable()
    {
        searchData = new ArrayList();
        columnName = new ArrayList();
        columnWidth = new ArrayList();
        if(parentFr instanceof CodeSelectionMatrixDialog)
        {
            columnName.add("Code");
            columnName.add("Name");
            columnName.add("Description");
        } else
        {
            columnName.add(DynaMOAD.getMSRString("code", "Code", 3));
            columnName.add(DynaMOAD.getMSRString("name", "Name", 3));
            columnName.add(DynaMOAD.getMSRString("description", "Description", 3));
        }
        columnWidth.add(new Integer(80));
        columnWidth.add(new Integer(80));
        columnWidth.add(new Integer(80));
        resultTable = new Table(searchData, (ArrayList)columnName.clone(), (ArrayList)columnWidth.clone(), 0, 79);
        resultTable.setIndexColumn(0);
        resultTable.getTable().setCursor(handCursor);
        resultTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        resultTable.getTable().addMouseListener(this);
        resultTable.setColumnSequence(new int[] {
            3, 1, 2
        });
    }

    public void setResultData(ArrayList resultData)
    {
        if(searchData != null)
            searchData.clear();
        if(resultData != null)
        {
            for(int i = 0; i < resultData.size(); i++)
            {
                DOSChangeable codeItemInfo = (DOSChangeable)resultData.get(i);
                ArrayList tmpList2 = new ArrayList();
                tmpList2.add(codeItemInfo.get("ouid"));
                tmpList2.add(codeItemInfo.get("name"));
                tmpList2.add(codeItemInfo.get("description"));
                tmpList2.add(codeItemInfo.get("codeitemid"));
                searchData.add(tmpList2.clone());
                tmpList2.clear();
                tmpList2 = null;
            }

        }
        resultTable.changeTableData();
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
        detailButton.removeActionListener(this);
        selectButton.removeActionListener(this);
        closeButton.removeActionListener(this);
        setVisible(false);
        dispose();
    }

    public void setFieldInDateField(String selectOuid, String sign)
    {
        ((JTextField)hoardData.get(sign)).setText(selectOuid);
    }

    public void setConditionField(String str)
    {
        valueTextField.setText(str);
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
                if(!isHierarchy)
                {
                    ArrayList codeList = null;
                    if(parentFr instanceof CodeSelectionMatrixDialog)
                        codeList = ((CodeSelectionMatrixDialog)parentFr).dos.listCodeItem(ouid);
                    else
                        codeList = dos.listCodeItem(ouid);
                    setResultData(codeList);
                }
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
        if(command.equals("select"))
        {
            int row = 0;
            String ouidRow = null;
            String selectOuid = "";
            if(isHierarchy)
            {
                if(hTree == null)
                    return;
                DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)hTree.getLastSelectedPathComponent();
                if(selNode == null)
                    return;
                TreeNodeObject nodeObject = (TreeNodeObject)selNode.getUserObject();
                if(nodeObject == null)
                    return;
                if(selNode.getLevel() > 0)
                {
                    selectOuid = nodeObject.getOuid();
                    signStr = signStr + "^" + nodeObject.getDescription();
                    if(parentFr instanceof CodeChooserUser)
                        ((CodeChooserUser)parentFr).setFieldInCode(signStr, selectOuid, nodeObject.getName() + " [" + nodeObject.getDescription() + "]");
                    else
                    if(parentFr instanceof CodeSelectionMatrixDialog)
                        if(sourceIdentifier == null)
                            ((CodeSelectionMatrixDialog)parentFr).setValueCode(nodeObject.getName() + " [" + nodeObject.getDescription() + "]", selectOuid);
                        else
                        if("initial".equals(sourceIdentifier))
                            ((CodeSelectionMatrixDialog)parentFr).setInitialCode(nodeObject.getName() + " [" + nodeObject.getDescription() + "]", selectOuid);
                    returnValue = new String[3];
                    returnValue[0] = signStr;
                    returnValue[1] = selectOuid;
                    returnValue[2] = nodeObject.getName() + " [" + nodeObject.getDescription() + "]";
                } else
                {
                    signStr = signStr + "^";
                    if(parentFr instanceof CodeChooserUser)
                        ((CodeChooserUser)parentFr).setFieldInCode(signStr, null, "");
                    else
                    if(parentFr instanceof CodeSelectionMatrixDialog)
                        if(sourceIdentifier == null)
                            ((CodeSelectionMatrixDialog)parentFr).setValueCode("", null);
                        else
                        if("initial".equals(sourceIdentifier))
                            ((CodeSelectionMatrixDialog)parentFr).setInitialCode("", null);
                    returnValue = new String[3];
                    returnValue[0] = signStr;
                    returnValue[1] = null;
                    returnValue[2] = "";
                }
                nodeObject = null;
                selNode = null;
                closeDialog(null);
            } else
            {
                row = resultTable.getTable().getSelectedRow();
                ouidRow = resultTable.getSelectedOuidRow(row);
                ArrayList aRow = null;
                if(row > -1)
                {
                    if(ouidRow != null)
                        aRow = (ArrayList)searchData.get((new Integer(ouidRow)).intValue());
                    else
                        aRow = (ArrayList)searchData.get(row);
                    selectOuid = (String)aRow.get(0);
                    if(selectOuid != null)
                    {
                        signStr = signStr + "^" + aRow.get(3);
                        if(parentFr instanceof CodeChooserUser)
                            ((CodeChooserUser)parentFr).setFieldInCode(signStr, selectOuid, aRow.get(1) + " [" + aRow.get(3) + "]");
                        else
                        if(parentFr instanceof CodeSelectionMatrixDialog)
                            if(sourceIdentifier == null)
                                ((CodeSelectionMatrixDialog)parentFr).setValueCode(aRow.get(1) + " [" + aRow.get(3) + "]", selectOuid);
                            else
                            if("initial".equals(sourceIdentifier))
                                ((CodeSelectionMatrixDialog)parentFr).setInitialCode(aRow.get(1) + " [" + aRow.get(3) + "]", selectOuid);
                        returnValue = new String[3];
                        returnValue[0] = signStr;
                        returnValue[1] = selectOuid;
                        returnValue[2] = aRow.get(1) + " [" + aRow.get(3) + "]";
                        closeDialog(null);
                    }
                } else
                {
                    signStr = signStr + "^";
                    if(parentFr instanceof CodeChooserUser)
                        ((CodeChooserUser)parentFr).setFieldInCode(signStr, null, "");
                    if(parentFr instanceof CodeSelectionMatrixDialog)
                        if(sourceIdentifier == null)
                            ((CodeSelectionMatrixDialog)parentFr).setValueCode("", null);
                        else
                        if("initial".equals(sourceIdentifier))
                            ((CodeSelectionMatrixDialog)parentFr).setInitialCode("", null);
                    returnValue = new String[3];
                    returnValue[0] = signStr;
                    returnValue[1] = null;
                    returnValue[2] = "";
                    closeDialog(null);
                }
            }
        } else
        {
            command.equals("fieldCombo");
        }
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
        if(mouseEvent.getSource() == resultTable.getTable() && mouseEvent.getClickCount() == 2)
            selectButton.doClick();
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private boolean isHierarchyMode(DOSChangeable dosCode)
    {
        if(dosCode == null)
        {
            return false;
        } else
        {
            isHierarchy = Utils.getBoolean((Boolean)dosCode.get("is.hierarchy"));
            return isHierarchy;
        }
    }

    private void initialize()
    {
        try
        {
            DOSChangeable dosCode;
            if(parentFr instanceof CodeSelectionMatrixDialog)
                dosCode = ((CodeSelectionMatrixDialog)parentFr).dos.getCode(ouid);
            else
                dosCode = dos.getCode(ouid);
            if(isHierarchyMode(dosCode))
            {
                makeTree(dosCode);
                initComponentsForTree();
            } else
            {
                makeTable();
                initComponentsForTable();
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    private void makeTree(DOSChangeable dosCode)
    {
        if(dosCode == null)
            return;
        try
        {
            String rootItemOuid = null;
            DOSChangeable rootCodeItem = null;
            if(parentFr instanceof CodeSelectionMatrixDialog)
                rootCodeItem = ((CodeSelectionMatrixDialog)parentFr).dos.getCodeItemRoot((String)dosCode.get("ouid"));
            else
                rootCodeItem = dos.getCodeItemRoot((String)dosCode.get("ouid"));
            if(rootCodeItem == null)
            {
                rootCodeItem = new DOSChangeable();
                rootCodeItem.put("name", dosCode.get("name"));
                rootCodeItem.put("description", "Root Item of Tree. Do not delete me.");
                rootCodeItem.put("codeitemid", dosCode.get("name"));
                if(parentFr instanceof CodeSelectionMatrixDialog)
                    rootItemOuid = ((CodeSelectionMatrixDialog)parentFr).dos.createCodeItem((String)dosCode.get("ouid"), rootCodeItem);
                else
                    rootItemOuid = dos.createCodeItem((String)dosCode.get("ouid"), rootCodeItem);
                rootCodeItem.clear();
                rootCodeItem = null;
                if(rootItemOuid == null)
                    return;
                if(parentFr instanceof CodeSelectionMatrixDialog)
                    rootCodeItem = ((CodeSelectionMatrixDialog)parentFr).dos.getCodeItem(rootItemOuid);
                else
                    rootCodeItem = dos.getCodeItem(rootItemOuid);
                if(rootCodeItem == null)
                    return;
            }
            TreeNodeObject rootNodeObject = null;
            DefaultMutableTreeNode rootNode = null;
            DefaultTreeModel hTreeModel = null;
            rootNodeObject = new TreeNodeObject((String)rootCodeItem.get("ouid"), (String)rootCodeItem.get("name"), (String)rootCodeItem.get("codeitemid"));
            rootNode = new DefaultMutableTreeNode(rootNodeObject);
            hTreeModel = new DefaultTreeModel(rootNode);
            hTree = new JTree(hTreeModel);
            hTree.addTreeSelectionListener(this);
            renderer = new ObjectModelDefaultTreeCellRenderer();
            hTree.setCellRenderer(renderer);
            hTree.getSelectionModel().setSelectionMode(1);
            hTree.putClientProperty("JTree.lineStyle", "Angled");
            populateHierarchyTreeNode(rootNode, true);
            hTree.updateUI();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void populateHierarchyTreeNode(DefaultMutableTreeNode treeNode, boolean forceMode)
    {
        if(treeNode == null)
            return;
        TreeNodeObject codedata = null;
        codedata = (TreeNodeObject)treeNode.getUserObject();
        if(codedata.isPopulated() && !forceMode)
            return;
        if(forceMode)
            treeNode.removeAllChildren();
        DefaultMutableTreeNode itemNode = null;
        TreeNodeObject itemNodeObject = null;
        DOSChangeable codeItem = null;
        try
        {
            ArrayList itemChildren = null;
            if(parentFr instanceof CodeSelectionMatrixDialog)
                itemChildren = ((CodeSelectionMatrixDialog)parentFr).dos.getCodeItemChildren(codedata.getOuid());
            else
                itemChildren = dos.getCodeItemChildren(codedata.getOuid());
            if(Utils.isNullArrayList(itemChildren))
                return;
            Iterator itemKey;
            for(itemKey = itemChildren.iterator(); itemKey.hasNext();)
            {
                codeItem = (DOSChangeable)itemKey.next();
                if(codeItem == null)
                    return;
                itemNodeObject = new TreeNodeObject((String)codeItem.get("ouid"), (String)codeItem.get("name"), (String)codeItem.get("codeitemid"));
                itemNode = new DefaultMutableTreeNode(itemNodeObject);
                treeNode.add(itemNode);
                itemNode = null;
                itemNodeObject = null;
                codeItem = null;
            }

            itemKey = null;
            itemChildren.clear();
            itemChildren = null;
            codedata.setPopulated(true);
            if(treeNode.getLevel() == 0)
                hTree.fireTreeExpanded(new TreePath(treeNode.getPath()));
            else
                hTree.fireTreeCollapsed(new TreePath(treeNode.getPath()));
        }
        catch(IIPRequestException e1)
        {
            e1.printStackTrace();
        }
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode selnode = null;
        TreeNodeObject codedata = null;
        selnode = (DefaultMutableTreeNode)hTree.getLastSelectedPathComponent();
        if(selnode != null)
        {
            codedata = (TreeNodeObject)selnode.getUserObject();
            populateHierarchyTreeNode(selnode, false);
        }
        if(selnode != null && (selnode.isLeaf() || selnode.getLevel() == 0))
            selectButton.setEnabled(true);
        else
            selectButton.setEnabled(false);
    }

    public void setParentFrame(Object parentFrame)
    {
        parentFr = parentFrame;
    }

    public String returnValue[];
    private Cursor handCursor;
    private JPanel mainPanel;
    private JToolBar toolBar;
    private JPanel toolPanel;
    private JButton searchButton;
    private JButton detailButton;
    private JButton selectButton;
    private JButton closeButton;
    private JPanel centerPanel;
    private JPanel optionPanel;
    private JComboBox fieldComboBox;
    private JTextField valueTextField;
    private JScrollPane scrollPane;
    private Table resultTable;
    private JTree hTree;
    private ObjectModelDefaultTreeCellRenderer renderer;
    private String classID;
    private boolean classChangable;
    private String defaultField;
    private String defaultOperator;
    private String defaultSearchValue;
    private String searchSQL;
    private String resultSetter;
    private Object resultSetterObject;
    private DOS dos;
    private UIManagement newUI;
    private ArrayList searchData;
    private ArrayList columnName;
    private ArrayList columnWidth;
    private String ouid;
    private DOSChangeable hoardData;
    private String signStr;
    private String sourceIdentifier;
    private Object parentFr;
    private boolean isHierarchy;
}