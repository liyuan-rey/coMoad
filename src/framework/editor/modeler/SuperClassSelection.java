// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SuperClassSelection.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ClassInformation, ObjectModelingConstruction, ObjectModelingDefinition

public class SuperClassSelection extends JDialog
    implements WindowListener, ActionListener, MouseListener
{

    public SuperClassSelection(Frame frame, String title, boolean modal, int i, String oid)
    {
        super(frame, title, modal);
        TOP = 0;
        searchData = null;
        searchColumn = null;
        searchWidth = null;
        agreeData = null;
        agreeColumn = null;
        agreeWidth = null;
        agreecheck = null;
        EcOid = null;
        tbMain = new JToolBar();
        blMain = new BoxLayout(tbMain, 0);
        searchButton = null;
        clearButton = null;
        okButton = null;
        finishButton = null;
        rightArrowButton = null;
        leftArrowButton = null;
        arrowPanel = new JPanel();
        blArrow = new BoxLayout(arrowPanel, 1);
        conditionPanel = null;
        listPanel = new JPanel();
        blList = new BoxLayout(listPanel, 0);
        searchListPanel = null;
        agreeListPanel = null;
        agreeListScrPane = null;
        checkAgreedComboBox = null;
        namedTextField = null;
        teamNamedTextField = null;
        projectNamedTextField = null;
        superClassSplitPane = null;
        objectClassTree = null;
        classInfo = null;
        dos = null;
        superClass = null;
        searchData = new ArrayList();
        searchColumn = new ArrayList();
        searchWidth = new ArrayList();
        agreeData = new ArrayList();
        agreeColumn = new ArrayList();
        agreeWidth = new ArrayList();
        searchButton = new JButton();
        clearButton = new JButton();
        okButton = new JButton();
        finishButton = new JButton();
        rightArrowButton = new JButton();
        leftArrowButton = new JButton();
        conditionPanel = new JPanel();
        searchListPanel = new JPanel();
        agreeListPanel = new JPanel();
        agreeListScrPane = UIFactory.createStrippedScrollPane(null);
        checkAgreedComboBox = new DynaComboBox();
        namedTextField = new DynaTextField();
        teamNamedTextField = new DynaTextField();
        projectNamedTextField = new DynaTextField();
        superClassSplitPane = new JSplitPane(1);
        try
        {
            initialize();
            fr = frame;
            TOP = i;
            EcOid = oid;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public SuperClassSelection(ClassInformation classInfo, ArrayList superClass, String title)
    {
        this((Frame)classInfo.parent, title, false, 0, null);
        try
        {
            this.classInfo = classInfo;
            this.superClass = superClass;
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            setData(superClass);
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    void initialize()
        throws Exception
    {
        setSize(500, 450);
        setLocation(50, 50);
        getContentPane().setLayout(new BorderLayout());
        addWindowListener(this);
        tbMain.setLayout(blMain);
        tbMain.setBorder(BorderFactory.createEtchedBorder());
        checkAgreedComboBox.setTitleText("approveCondition");
        checkAgreedComboBox.setActionCommand("approveCondition");
        checkAgreedComboBox.setTitleWidth(80);
        checkAgreedComboBox.setTitleVisible(true);
        checkAgreedComboBox.setEditable(false);
        checkAgreedComboBox.addActionListener(this);
        namedTextField.setTitleText("userName");
        namedTextField.setTitleWidth(80);
        namedTextField.setTitleVisible(true);
        namedTextField.setEditable(true);
        teamNamedTextField.setTitleText("teamName");
        teamNamedTextField.setTitleWidth(80);
        teamNamedTextField.setTitleVisible(true);
        teamNamedTextField.setEditable(true);
        projectNamedTextField.setTitleText("ProjectName");
        projectNamedTextField.setTitleWidth(80);
        projectNamedTextField.setTitleVisible(true);
        projectNamedTextField.setEditable(true);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        conditionPanel.setLayout(gridBag);
        conditionPanel.setBorder(null);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBag.setConstraints(checkAgreedComboBox, gridBagCon);
        conditionPanel.add(checkAgreedComboBox);
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBag.setConstraints(namedTextField, gridBagCon);
        conditionPanel.add(namedTextField);
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBag.setConstraints(teamNamedTextField, gridBagCon);
        conditionPanel.add(teamNamedTextField);
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBag.setConstraints(projectNamedTextField, gridBagCon);
        conditionPanel.add(projectNamedTextField);
        searchButton.setEnabled(false);
        searchButton.setText("Search");
        searchButton.setToolTipText("search");
        searchButton.setActionCommand("search");
        searchButton.setMargin(new Insets(0, 0, 0, 0));
        searchButton.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        searchButton.addActionListener(this);
        clearButton.setEnabled(false);
        clearButton.setToolTipText("clear");
        clearButton.setActionCommand("clear");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.addActionListener(this);
        okButton.setEnabled(true);
        okButton.setText("Ok");
        okButton.setToolTipText("Selection");
        okButton.setActionCommand("Selection");
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        okButton.addActionListener(this);
        finishButton.setToolTipText("close");
        finishButton.setActionCommand("close");
        finishButton.setMargin(new Insets(0, 0, 0, 0));
        finishButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        finishButton.addActionListener(this);
        tbMain.add(Box.createHorizontalGlue());
        tbMain.add(searchButton);
        tbMain.add(clearButton);
        tbMain.add(okButton);
        tbMain.add(finishButton);
        getContentPane().add(tbMain, "North");
        arrowPanel.setLayout(blArrow);
        rightArrowButton.setToolTipText("add");
        rightArrowButton.setActionCommand("add");
        rightArrowButton.setMargin(new Insets(0, 0, 0, 0));
        rightArrowButton.setIcon(new ImageIcon(getClass().getResource("/icons/SelectOneRow.gif")));
        rightArrowButton.addActionListener(this);
        leftArrowButton.setToolTipText("delete");
        leftArrowButton.setActionCommand("delete");
        leftArrowButton.setMargin(new Insets(0, 0, 0, 0));
        leftArrowButton.setIcon(new ImageIcon(getClass().getResource("/icons/DeselectOneRow.gif")));
        leftArrowButton.addActionListener(this);
        arrowPanel.add(Box.createVerticalGlue());
        arrowPanel.add(rightArrowButton);
        arrowPanel.add(leftArrowButton);
        arrowPanel.add(Box.createVerticalGlue());
        listPanel.setLayout(blList);
        TreeNodeObject classListObject = new TreeNodeObject("Class", "Class", "ClassList");
        objectClassTree = new ObjectModelingDefinition(this, classListObject);
        searchListPanel.setLayout(new BorderLayout());
        searchListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Class List Tree"));
        searchListPanel.add(objectClassTree, "Center");
        searchListPanel.setSize(200, 400);
        listPanel.add(searchListPanel);
        listPanel.add(arrowPanel);
        agreeListPanel.setLayout(new BorderLayout());
        agreeListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Super Class Table"));
        agreeListPanel.add(agreeListScrPane, "Center");
        superClassSplitPane.setDividerSize(0);
        superClassSplitPane.setLeftComponent(listPanel);
        superClassSplitPane.setRightComponent(agreeListPanel);
        superClassSplitPane.setDividerLocation(260);
        getContentPane().add(superClassSplitPane, "Center");
        searchListData();
        searchListTable = new Table(searchData, searchColumn, searchWidth, 1);
        searchListTable.setColumnSequence(new int[] {
            1, 2, 3
        });
        searchListTable.setIndexColumn(0);
        searchListTable.getTable().addMouseListener(this);
        agreeListTableData();
        agreeListTable = new Table(agreeData, agreeColumn, agreeWidth, 1);
        agreeListTable.setColumnSequence(new int[] {
            0, 1, 2, 3
        });
        agreeListTable.setIndexColumn(0);
        agreeListScrPane.setViewportView(agreeListTable.getTable());
        agreeListScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
    }

    public void searchListData()
    {
        searchColumn.add("userID");
        searchColumn.add("userName");
        searchColumn.add("userTitle");
        searchWidth.add(new Integer(80));
        searchWidth.add(new Integer(80));
        searchWidth.add(new Integer(80));
    }

    public void agreeListTableData()
    {
        agreeColumn.add("Ouid");
        agreeColumn.add("Name");
        agreeColumn.add("Description");
        agreeColumn.add("Title");
        agreeWidth.add(new Integer(80));
        agreeWidth.add(new Integer(80));
        agreeWidth.add(new Integer(80));
        agreeWidth.add(new Integer(80));
    }

    void checkAgreedComboBox_actionPerformed(ActionEvent actionevent)
    {
    }

    void searchButton_actionPerformed(ActionEvent actionevent)
    {
    }

    public ArrayList makeSearchCondition()
    {
        ArrayList condition = new ArrayList();
        condition.add(namedTextField.getText());
        condition.add(teamNamedTextField.getText());
        condition.add(projectNamedTextField.getText());
        return condition;
    }

    void clearButton_actionPerformed(ActionEvent e)
    {
        if(clearButton.isEnabled())
        {
            namedTextField.setText("");
            teamNamedTextField.setText("");
            projectNamedTextField.setText("");
        }
    }

    void searchListTable_mouseClicked(MouseEvent e)
    {
        searchListTable.getSelectedRowNumber();
    }

    public boolean checkAgreeFlow(String s)
    {
        boolean che = true;
        return che;
    }

    void agreeListTable_mouseClicked(MouseEvent mouseevent)
    {
    }

    public void setagreeData(String oid, Integer se)
    {
        for(int i = 0; i < agreeData.size(); i++)
            if(oid.equals((String)((ArrayList)agreeData.get(i)).get(0)))
                agreeListTable.changeTableData(se, 2, i);

    }

    void leftArrowButton_actionPerformed(ActionEvent e)
    {
        if(agreeListTable.getTable().getSelectedRowCount() <= 0)
            return;
        int selrows[] = agreeListTable.getTable().getSelectedRows();
        for(int i = selrows.length - 1; i >= 0; i--)
            agreeData.remove(selrows[i]);

        agreeListTable.changeTableData();
    }

    void rightArrowButton_actionPerformed(ActionEvent e)
    {
        try
        {
            DOSChangeable selectedDC = new DOSChangeable();
            ArrayList LocSearch = new ArrayList();
            ArrayList LocAgree = new ArrayList();
            ArrayList selectedNodeAL = new ArrayList();
            selectedNodeAL = objectClassTree.getSelectedNodeInfo();
            classInfo.setSuperClassAL(selectedNodeAL);
            String classOuid = "";
            classOuid = (String)selectedNodeAL.get(0);
            selectedDC = dos.getClass(classOuid);
            LocSearch.add(selectedDC.get("ouid"));
            LocSearch.add(selectedDC.get("name"));
            LocSearch.add(selectedDC.get("description"));
            LocSearch.add(selectedDC.get("description"));
            LocAgree.add(LocSearch.get(0));
            LocAgree.add(LocSearch.get(1));
            LocAgree.add(LocSearch.get(2));
            LocAgree.add(LocSearch.get(3));
            agreeData.add(LocAgree.clone());
            agreeListTable.changeTableData();
            okButton.setEnabled(true);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    void okButton_actionPerformed(ActionEvent e)
    {
        ArrayList selectedNodeAL = new ArrayList();
        for(int i = 0; i < agreeData.size(); i++)
            selectedNodeAL.add((String)((ArrayList)agreeData.get(i)).get(0));

        classInfo.setSuperClassAL(selectedNodeAL);
        dispose();
    }

    void finishButton_actionPerformed(ActionEvent e)
    {
        if(finishButton.isEnabled())
            FinishEvent();
    }

    public void FinishEvent()
    {
        checkAgreedComboBox.removeActionListener(this);
        searchButton.removeActionListener(this);
        checkAgreedComboBox.removeActionListener(this);
        clearButton.removeActionListener(this);
        okButton.removeActionListener(this);
        finishButton.removeActionListener(this);
        rightArrowButton.removeActionListener(this);
        leftArrowButton.removeActionListener(this);
        searchListTable.getTable().removeMouseListener(this);
        dispose();
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void windowClosing(WindowEvent e)
    {
        if(finishButton.isEnabled())
            FinishEvent();
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
        searchListTable_mouseClicked(e);
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

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            finishButton_actionPerformed(e);
        else
        if(command.equals("approveCondition"))
            checkAgreedComboBox_actionPerformed(e);
        else
        if(command.equals("search"))
            searchButton_actionPerformed(e);
        else
        if(command.equals("clear"))
            clearButton_actionPerformed(e);
        else
        if(command.equals("Selection"))
            okButton_actionPerformed(e);
        else
        if(command.equals("close"))
            finishButton_actionPerformed(e);
        else
        if(command.equals("add"))
            rightArrowButton_actionPerformed(e);
        else
        if(command.equals("delete"))
            leftArrowButton_actionPerformed(e);
    }

    public void setData(ArrayList superClass)
    {
        if(superClass != null)
            try
            {
                DOSChangeable selectedDC = new DOSChangeable();
                ArrayList LocSearch = new ArrayList();
                ArrayList selectedSuperClass = new ArrayList();
                ArrayList LocAgree = new ArrayList();
                ArrayList selectedNodeAL = new ArrayList();
                for(int i = 0; i < superClass.size(); i++)
                {
                    String classOuid = "";
                    classOuid = (String)superClass.get(i);
                    selectedDC = dos.getClass(classOuid);
                    LocSearch.add(selectedDC.get("ouid"));
                    LocSearch.add(selectedDC.get("name"));
                    LocSearch.add(selectedDC.get("description"));
                    LocSearch.add(selectedDC.get("description"));
                    agreeData.add(LocSearch.clone());
                    LocSearch.clear();
                }

                agreeListTable.changeTableData();
                okButton.setEnabled(true);
            }
            catch(IIPRequestException ie)
            {
                System.out.println(ie);
            }
    }

    private final int FRAME_XSIZE = 500;
    private final int FRAME_YSIZE = 450;
    private final int FRAME_XLOC = 50;
    private final int FRAME_YLOC = 50;
    private final int TITLE_TEXT_WIDTH = 80;
    private final int DIVIDERSIZE = 0;
    private final int DIVIDERLOCATION = 260;
    private int TOP;
    private ArrayList searchData;
    private ArrayList searchColumn;
    private ArrayList searchWidth;
    private ArrayList agreeData;
    private ArrayList agreeColumn;
    private ArrayList agreeWidth;
    private String agreecheck;
    private String EcOid;
    private JToolBar tbMain;
    private BoxLayout blMain;
    private JButton searchButton;
    private JButton clearButton;
    private JButton okButton;
    private JButton finishButton;
    private JButton rightArrowButton;
    private JButton leftArrowButton;
    private JPanel arrowPanel;
    private BoxLayout blArrow;
    private JPanel conditionPanel;
    private JPanel listPanel;
    private BoxLayout blList;
    private JPanel searchListPanel;
    private JPanel agreeListPanel;
    private JScrollPane agreeListScrPane;
    private DynaComboBox checkAgreedComboBox;
    private DynaTextField namedTextField;
    private DynaTextField teamNamedTextField;
    private DynaTextField projectNamedTextField;
    private Table searchListTable;
    private Table agreeListTable;
    private Frame fr;
    private JSplitPane superClassSplitPane;
    private ObjectModelingDefinition objectClassTree;
    private ClassInformation classInfo;
    private DOS dos;
    private ArrayList superClass;
}
