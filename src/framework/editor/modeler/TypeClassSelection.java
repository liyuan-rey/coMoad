// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TypeClassSelection.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.service.DOS;
import dyna.uic.TreeNodeObject;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

// Referenced classes of package dyna.framework.editor.modeler:
//            FieldInformation, ObjectModelingDefinition, ActionInformation, CodeInformation, 
//            CodeItemInformation, ObjectModelingConstruction, AttributeWizard, CodeSelectionMatrixDialog, 
//            NumberingRuleInformation, AssociationInformation, ClassInformation, FieldGroupInformation

public class TypeClassSelection extends JDialog
    implements WindowListener, ActionListener, MouseListener
{

    public TypeClassSelection(Frame frame, String title, boolean modal, int i, String oid)
    {
        super(frame, title, modal);
        TOP = 0;
        tbMain = new JToolBar();
        blMain = new BoxLayout(tbMain, 0);
        okButton = null;
        finishButton = null;
        listPanel = new JPanel();
        blList = new BoxLayout(listPanel, 0);
        searchListPanel = null;
        searchListScrPane = null;
        objectClassTree = null;
        fieldInfo = null;
        actionInfo = null;
        associationInfo = null;
        fieldGroupInfo = null;
        numberingRuleInfo = null;
        fieldInformationInfo = null;
        attributewizard = null;
        codeInformation = null;
        codeItemInformation = null;
        csmd = null;
        dos = null;
        endOption = 0;
        fgroupOption = 0;
        selectedOuid = null;
        okButton = new JButton();
        finishButton = new JButton();
        searchListPanel = new JPanel();
        searchListScrPane = UIFactory.createStrippedScrollPane(null);
        try
        {
            initialize();
            fr = frame;
            TOP = i;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public TypeClassSelection(Object objectInfo, String title)
    {
        this(((Frame) (null)), title, true, 0, ((String) (null)));
        try
        {
            if(objectInfo instanceof FieldInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Field", "Field", "Field");
                objectClassTree = new ObjectModelingDefinition(this, classListObject);
                searchListScrPane.setViewportView(objectClassTree);
                fieldInfo = (FieldInformation)objectInfo;
            } else
            if(objectInfo instanceof ActionInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Action", "Action", "Action");
                objectClassTree = new ObjectModelingDefinition(this, classListObject);
                searchListScrPane.setViewportView(objectClassTree);
                actionInfo = (ActionInformation)objectInfo;
            } else
            if(objectInfo instanceof CodeInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Class", "Class", "Class");
                objectClassTree = new ObjectModelingDefinition(this, classListObject);
                searchListScrPane.setViewportView(objectClassTree);
                codeInformation = (CodeInformation)objectInfo;
            } else
            if(objectInfo instanceof CodeItemInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Class", "Class", "Class");
                objectClassTree = new ObjectModelingDefinition(this, classListObject);
                searchListScrPane.setViewportView(objectClassTree);
                codeItemInformation = (CodeItemInformation)objectInfo;
            }
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(Object objectInfo, Component comp, String title)
    {
        this(((Frame) (null)), title, true, 0, ((String) (null)));
        try
        {
            if(objectInfo instanceof AttributeWizard)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Field", "Field", "Field");
                objectClassTree = new ObjectModelingDefinition(this, classListObject);
                searchListScrPane.setViewportView(objectClassTree);
                attributewizard = (AttributeWizard)objectInfo;
                targetComponent = comp;
            }
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(Object objectInfo, String title, String classOuid)
    {
        this(((Frame) (null)), title, true, 0, ((String) (null)));
        try
        {
            if(objectInfo == null)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Code", "Code", "Code");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, "Code");
                searchListScrPane.setViewportView(objectClassTree);
            } else
            if(objectInfo instanceof FieldInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Code", "Code", "Code");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, "Code");
                searchListScrPane.setViewportView(objectClassTree);
                fieldInfo = (FieldInformation)objectInfo;
            } else
            if(objectInfo instanceof CodeSelectionMatrixDialog)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Code", "Code", "Code");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, "Code");
                searchListScrPane.setViewportView(objectClassTree);
                csmd = (CodeSelectionMatrixDialog)objectInfo;
            }
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(Object objectInfo, Component comp, String title, String classOuid)
    {
        this(((Frame) (null)), title, true, 0, ((String) (null)));
        try
        {
            if(objectInfo instanceof AttributeWizard)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Code", "Code", "Code");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, "Code");
                searchListScrPane.setViewportView(objectClassTree);
                attributewizard = (AttributeWizard)objectInfo;
                targetComponent = comp;
            }
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(Object objectInfo, String title, String classOuid, int option)
    {
        this(((Frame) (null)), title, true, 0, ((String) (null)));
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            if(objectInfo instanceof NumberingRuleInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Field", "Field", "Field Folder");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, classOuid, 1);
                if(option == 2)
                {
                    JTree tree = objectClassTree.getTree();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
                    TreeNodeObject childObject = new TreeNodeObject("md$number", "md$number", "Field");
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childObject);
                    DefaultMutableTreeNode defaultmutabletreenode = objectClassTree.setInsertNode(parentNode, childNode, parentNode.getChildCount());
                }
                searchListScrPane.setViewportView(objectClassTree);
                numberingRuleInfo = (NumberingRuleInformation)objectInfo;
            } else
            if(objectInfo instanceof FieldInformation)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Field", "Field", "Field Folder");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, classOuid, 3);
                if(option == 2)
                {
                    JTree tree = objectClassTree.getTree();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
                    TreeNodeObject childObject = new TreeNodeObject("md$number", "md$number", "Field");
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childObject);
                    DefaultMutableTreeNode defaultmutabletreenode1 = objectClassTree.setInsertNode(parentNode, childNode, parentNode.getChildCount());
                }
                searchListScrPane.setViewportView(objectClassTree);
                fieldInformationInfo = (FieldInformation)objectInfo;
            }
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(Object objectInfo, Component comp, String title, String classOuid, int option)
    {
        this(((Frame) (null)), title, true, 0, ((String) (null)));
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            if(objectInfo instanceof AttributeWizard)
            {
                TreeNodeObject classListObject = new TreeNodeObject("Field", "Field", "Field Folder");
                objectClassTree = new ObjectModelingDefinition(this, classListObject, classOuid, 3);
                if(option == 2)
                {
                    JTree tree = objectClassTree.getTree();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)tree.getModel().getRoot();
                    TreeNodeObject childObject = new TreeNodeObject("md$number", "md$number", "Field");
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childObject);
                    DefaultMutableTreeNode defaultmutabletreenode = objectClassTree.setInsertNode(parentNode, childNode, parentNode.getChildCount());
                }
                searchListScrPane.setViewportView(objectClassTree);
                attributewizard = (AttributeWizard)objectInfo;
                targetComponent = comp;
            }
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(Object objectInfo, String title, int i)
    {
        this((Frame)((AssociationInformation)objectInfo).parent.parent, title, true, 0, ((String) (null)));
        TreeNodeObject classListObject = new TreeNodeObject("Class", "Class", "ClassList");
        objectClassTree = new ObjectModelingDefinition(this, classListObject);
        searchListScrPane.setViewportView(objectClassTree);
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            associationInfo = (AssociationInformation)objectInfo;
            endOption = i;
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public TypeClassSelection(FieldGroupInformation fieldGroupInfo, String title, String classOuid, int option)
    {
        this((Frame)fieldGroupInfo.parent, title, true, 0, ((String) (null)));
        if(option == 1)
        {
            TreeNodeObject classListObject = new TreeNodeObject("Field", "Field", "Field Folder");
            objectClassTree = new ObjectModelingDefinition(this, classListObject, classOuid, 1);
            searchListScrPane.setViewportView(objectClassTree);
        } else
        if(option == 2)
        {
            TreeNodeObject classListObject = new TreeNodeObject("Action", "Action", "Action Folder");
            objectClassTree = new ObjectModelingDefinition(this, classListObject, classOuid, 2);
            searchListScrPane.setViewportView(objectClassTree);
        }
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            this.fieldGroupInfo = fieldGroupInfo;
            fgroupOption = option;
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    void initialize()
        throws Exception
    {
        setSize(300, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 300) / 2, (screenSize.height - 500) / 2);
        getContentPane().setLayout(new BorderLayout());
        addWindowListener(this);
        tbMain.setLayout(blMain);
        tbMain.setBorder(BorderFactory.createEtchedBorder());
        okButton.setEnabled(true);
        okButton.setToolTipText("Selection");
        okButton.setActionCommand("Selection");
        okButton.setText("Ok");
        okButton.setMargin(new Insets(0, 0, 0, 0));
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        okButton.addActionListener(this);
        finishButton.setToolTipText("close");
        finishButton.setActionCommand("close");
        finishButton.setText("Exit");
        finishButton.setMargin(new Insets(0, 0, 0, 0));
        finishButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        finishButton.addActionListener(this);
        tbMain.add(Box.createHorizontalGlue());
        tbMain.add(okButton);
        tbMain.add(finishButton);
        getContentPane().add(tbMain, "North");
        searchListPanel.setLayout(new BorderLayout());
        searchListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "List Tree"));
        searchListPanel.add(searchListScrPane, "Center");
        getContentPane().add(searchListPanel, "Center");
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    void okButton_actionPerformed(ActionEvent e)
    {
        ArrayList selectedNodeAL = new ArrayList();
        selectedNodeAL = objectClassTree.getSelectedNodeInfo();
        if(endOption == 0)
        {
            if(fieldInfo != null)
            {
                fieldInfo.setClassTypeOID(selectedNodeAL);
                selectedOuid = (String)selectedNodeAL.get(0);
            } else
            if(actionInfo != null)
                actionInfo.setClassTypeOID(selectedNodeAL);
            else
            if(fieldGroupInfo != null && fgroupOption == 1)
                fieldGroupInfo.setFieldData(selectedNodeAL);
            else
            if(fieldGroupInfo != null && fgroupOption == 2)
                fieldGroupInfo.setActionData(selectedNodeAL);
            else
            if(numberingRuleInfo != null)
                numberingRuleInfo.setFieldData(selectedNodeAL);
            else
            if(fieldInformationInfo != null && selectedNodeAL != null)
                fieldInformationInfo.setClassTypeOID(selectedNodeAL);
            else
            if(attributewizard != null)
                attributewizard.setClassTypeOID(targetComponent, selectedNodeAL);
            else
            if(codeInformation != null)
                codeInformation.setClass(selectedNodeAL);
            else
            if(codeItemInformation != null)
                codeItemInformation.setClass(selectedNodeAL);
            else
            if(csmd != null)
            {
                selectedOuid = null;
                if(Utils.isNullArrayList(selectedNodeAL))
                    return;
                selectedOuid = (String)selectedNodeAL.get(0);
            } else
            {
                selectedOuid = null;
                if(Utils.isNullArrayList(selectedNodeAL))
                    return;
                selectedOuid = (String)selectedNodeAL.get(0);
            }
        } else
        if(endOption == 1)
            associationInfo.setEndOneClassOID(selectedNodeAL);
        else
        if(endOption == 2)
            associationInfo.setEndTwoClassOID(selectedNodeAL);
        else
        if(endOption == 3)
            associationInfo.setDOSClassOID(selectedNodeAL);
        dispose();
    }

    void finishButton_actionPerformed(ActionEvent e)
    {
        if(finishButton.isEnabled())
            FinishEvent();
    }

    public void FinishEvent()
    {
        okButton.removeActionListener(this);
        finishButton.removeActionListener(this);
        dispose();
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

    public void mouseClicked(MouseEvent mouseevent)
    {
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
        if(command.equals("Selection"))
            okButton_actionPerformed(e);
        else
        if(command.equals("close"))
            finishButton_actionPerformed(e);
    }

    private final int FRAME_XSIZE = 300;
    private final int FRAME_YSIZE = 500;
    private final int FRAME_XLOC = 50;
    private final int FRAME_YLOC = 50;
    private final int TITLE_TEXT_WIDTH = 80;
    private final int DIVIDERSIZE = 0;
    private final int DIVIDERLOCATION = 260;
    private int TOP;
    private JToolBar tbMain;
    private BoxLayout blMain;
    private JButton okButton;
    private JButton finishButton;
    private JPanel listPanel;
    private BoxLayout blList;
    private JPanel searchListPanel;
    private JScrollPane searchListScrPane;
    private Frame fr;
    private ObjectModelingDefinition objectClassTree;
    private FieldInformation fieldInfo;
    private ActionInformation actionInfo;
    private AssociationInformation associationInfo;
    private FieldGroupInformation fieldGroupInfo;
    private NumberingRuleInformation numberingRuleInfo;
    private FieldInformation fieldInformationInfo;
    private AttributeWizard attributewizard;
    private CodeInformation codeInformation;
    private CodeItemInformation codeItemInformation;
    private CodeSelectionMatrixDialog csmd;
    private Component targetComponent;
    private DOS dos;
    private int endOption;
    private int fgroupOption;
    public String selectedOuid;
}
