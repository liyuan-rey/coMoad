// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CodeItemInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTextField;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            CodeManager, TypeClassSelection

public class CodeItemInformation extends JPanel
    implements ActionListener, MouseListener
{

    public CodeItemInformation(DOS dos)
    {
        codeItemTableScrPane = null;
        codeItemTable = null;
        handCursor = new Cursor(12);
        buttonToolBar = new JToolBar(0);
        buttonBoxLayout = new BoxLayout(buttonToolBar, 0);
        createButton = null;
        modifyButton = null;
        clearButton = null;
        deleteButton = null;
        this.dos = null;
        aus = null;
        isHierarchy = false;
        parentItemOuid = null;
        classOuid = null;
        try
        {
            this.dos = dos;
            aus = DynaMOAD.aus;
            initialize();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void initialize()
    {
        codeItemInfoBorderLayout = new BorderLayout();
        setLayout(codeItemInfoBorderLayout);
        buttonToolBar.setAlignmentX(0.0F);
        buttonToolBar.setBorder(BorderFactory.createEtchedBorder());
        createButton = new JButton();
        modifyButton = new JButton();
        clearButton = new JButton();
        deleteButton = new JButton();
        createButton.setEnabled(true);
        createButton.setToolTipText("create");
        createButton.setActionCommand("create");
        createButton.setMargin(new Insets(0, 0, 0, 0));
        createButton.setIcon(new ImageIcon(getClass().getResource("/icons/Registry.gif")));
        createButton.addActionListener(this);
        modifyButton.setEnabled(true);
        modifyButton.setToolTipText("modify");
        modifyButton.setActionCommand("modify");
        modifyButton.setMargin(new Insets(0, 0, 0, 0));
        modifyButton.setIcon(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
        modifyButton.addActionListener(this);
        clearButton.setToolTipText("clear");
        clearButton.setActionCommand("clear");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.addActionListener(this);
        deleteButton.setToolTipText("delete");
        deleteButton.setActionCommand("delete");
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        deleteButton.addActionListener(this);
        buttonToolBar.add(createButton);
        buttonToolBar.add(modifyButton);
        buttonToolBar.add(clearButton);
        buttonToolBar.add(deleteButton);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, 1));
        nameTextField = new DynaTextField();
        nameTextField.setEditable(true);
        nameTextField.setTitleText("Name");
        nameTextField.setMandatory(true);
        nameTextField.setTitleWidth(120);
        nameTextField.setTitleVisible(true);
        descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, 0));
        descPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 5, 4));
        descLabel = new JLabel("Description");
        descLabel.setVerticalAlignment(1);
        descLabel.setPreferredSize(new Dimension(120, 80));
        descLabel.setMinimumSize(new Dimension(120, 80));
        descLabel.setMaximumSize(new Dimension(120, 80));
        descScrPane = new JScrollPane();
        descTextArea = new JTextArea();
        descScrPane.getViewport().add(descTextArea, null);
        descScrPane.setMinimumSize(new Dimension(120, 80));
        descScrPane.setMaximumSize(new Dimension(1000, 80));
        descPanel.add(descLabel);
        descPanel.add(descScrPane);
        codeItemIDTextField = new DynaTextField();
        codeItemIDTextField.setEditable(true);
        codeItemIDTextField.setTitleText("CodeItem ID");
        codeItemIDTextField.setMandatory(true);
        codeItemIDTextField.setTitleWidth(120);
        codeItemIDTextField.setTitleVisible(true);
        filterTextField = new DynaTextField();
        filterTextField.setEditable(false);
        filterTextField.setTitleText("Class/Filter");
        filterTextField.setTitleWidth(120);
        filterTextField.setTitleVisible(true);
        classSelectButton = new JButton();
        classSelectButton.setEnabled(false);
        classSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        classSelectButton.setMargin(new Insets(0, 0, 0, 0));
        classSelectButton.setActionCommand("classSelectButton");
        classSelectButton.addActionListener(this);
        filterTextField.add(Box.createHorizontalStrut(5));
        filterTextField.add(classSelectButton);
        mainPanel.add(nameTextField);
        mainPanel.add(codeItemIDTextField);
        mainPanel.add(filterTextField);
        mainPanel.add(descPanel);
        makeCodeItemTable();
        codeItemTableScrPane = UIFactory.createStrippedScrollPane(null);
        codeItemTableScrPane.setViewportView(codeItemTable.getTable());
        codeItemTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        codeItemTableScrPane.setPreferredSize(new Dimension(300, 180));
        codeItemTableScrPane.setMaximumSize(new Dimension(1000, 1000));
        add(buttonToolBar, "North");
        add(mainPanel, "South");
        add(codeItemTableScrPane, "Center");
        if(!isAdmin())
        {
            createButton.setEnabled(false);
            modifyButton.setEnabled(false);
            clearButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    public void setCodeOuid(String codeOuid)
    {
        this.codeOuid = codeOuid;
        refreshCodeItemTable();
    }

    public void makeCodeItemTable()
    {
        codeItemData = new ArrayList();
        codeItemColumnName = new ArrayList();
        codeItemColumnWidth = new ArrayList();
        codeItemColumnName.add("Ouid");
        codeItemColumnName.add("Name");
        codeItemColumnName.add("Description");
        codeItemColumnName.add("CodeItemID");
        codeItemColumnWidth.add(new Integer(80));
        codeItemColumnWidth.add(new Integer(80));
        codeItemColumnWidth.add(new Integer(80));
        codeItemColumnWidth.add(new Integer(80));
        codeItemTable = new Table(codeItemData, (ArrayList)codeItemColumnName.clone(), (ArrayList)codeItemColumnWidth.clone(), 0);
        codeItemTable.getTable().addMouseListener(this);
        codeItemTable.setColumnSequence(new int[] {
            0, 1, 2, 3
        });
        codeItemTable.setIndexColumn(0);
    }

    public boolean codeItemPreCheck(String name)
    {
        boolean rvalue = true;
        try
        {
            ArrayList items = dos.listCodeItem(codeOuid);
            if(items == null || items.size() <= 0)
            {
                rvalue = true;
            } else
            {
                for(int i = 0; i < items.size(); i++)
                {
                    System.out.println(items.get(i));
                    String str = (String)((DOSChangeable)items.get(i)).get("codeitemid");
                    if(!name.equals(str))
                        continue;
                    rvalue = false;
                    break;
                }

            }
        }
        catch(IIPRequestException ie)
        {
            rvalue = false;
            System.out.println(ie);
        }
        return rvalue;
    }

    public boolean createCodeItem()
    {
        boolean isCreated = false;
        try
        {
            DOSChangeable codeItemDC = new DOSChangeable();
            if(!codeItemPreCheck(codeItemIDTextField.getText()))
            {
                JOptionPane.showMessageDialog(this, "\uC774\uBBF8 \uC874\uC7AC\uD569\uB2C8\uB2E4.", "ERROR", 0);
                return false;
            }
            codeItemDC.put("name", nameTextField.getText());
            codeItemDC.put("codeitemid", codeItemIDTextField.getText());
            codeItemDC.put("description", descTextArea.getText());
            codeItemDC.put("ouid@code", codeOuid);
            if(isHierarchy)
            {
                codeItemDC.put("parent", selectOuid);
                codeItemDC.put("filter", classOuid);
            }
            if(codeOuid != null && !Utils.isNullString(codeOuid))
            {
                String resultOuid = dos.createCodeItem(codeOuid, codeItemDC);
                codeItemDC.put("ouid", resultOuid);
                refreshCodeItemTable();
                CodeManager cm = CodeManager.getInstance();
                if(cm != null)
                    cm.populateHierarchyTreeNode();
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return isCreated;
    }

    public void modifyCodeItem()
    {
        try
        {
            DOSChangeable codeItemDC = new DOSChangeable();
            codeItemDC.put("ouid", selectOuid);
            codeItemDC.put("name", nameTextField.getText());
            codeItemDC.put("codeitemid", codeItemIDTextField.getText());
            codeItemDC.put("description", descTextArea.getText());
            codeItemDC.put("ouid@code", codeOuid);
            if(isHierarchy && !Utils.isNullString(parentItemOuid))
            {
                codeItemDC.put("parent", parentItemOuid);
                codeItemDC.put("filter", classOuid);
            }
            if(codeOuid != null && !Utils.isNullString(codeOuid))
            {
                dos.setCodeItem(codeItemDC);
                refreshCodeItemTable();
            }
            nameTextField.setText((String)codeItemDC.get("name"));
            descTextArea.setText((String)codeItemDC.get("description"));
            codeItemIDTextField.setText((String)codeItemDC.get("codeitemid"));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void deleteCodeItem()
    {
        try
        {
            Object option[] = {
                "\uC608", "\uC544\uB2C8\uC624"
            };
            int res = JOptionPane.showOptionDialog(this, "CodeItem\uC744 \uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "CodeItem delete", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[0]);
            if(res != 0)
                return;
            if(selectOuid != null && !Utils.isNullString(selectOuid))
            {
                dos.removeCodeItem(selectOuid);
                refreshCodeItemTable();
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        refreshCodeItemTable();
    }

    public void setCodeItemField(DOSChangeable dosCodeItem)
    {
        nameTextField.setText((String)dosCodeItem.get("name"));
        descTextArea.setText((String)dosCodeItem.get("description"));
        codeItemIDTextField.setText((String)dosCodeItem.get("codeitemid"));
    }

    public void clearCodeItemField()
    {
        nameTextField.setText("");
        descTextArea.setText("");
        codeItemIDTextField.setText("");
        filterTextField.setText("");
        classOuid = null;
        updateUI();
    }

    public void refreshCodeItemTable()
    {
        String oldSelectedOuid = selectOuid;
        try
        {
            codeItemData.clear();
            clearCodeItemField();
            if(codeOuid != null)
            {
                ArrayList codeItemList = dos.listCodeItem(codeOuid);
                for(int i = 0; i < codeItemList.size(); i++)
                {
                    ArrayList tempAL = new ArrayList(5);
                    tempAL.add(((DOSChangeable)codeItemList.get(i)).get("ouid"));
                    tempAL.add(((DOSChangeable)codeItemList.get(i)).get("name"));
                    tempAL.add(((DOSChangeable)codeItemList.get(i)).get("description"));
                    tempAL.add(((DOSChangeable)codeItemList.get(i)).get("codeitemid"));
                    tempAL.add(((DOSChangeable)codeItemList.get(i)).get("parent"));
                    tempAL.add(((DOSChangeable)codeItemList.get(i)).get("filter"));
                    codeItemData.add(tempAL.clone());
                    tempAL.clear();
                }

            }
            codeItemTable.changeTableData();
            if(codeItemData.size() > 0)
            {
                ArrayList arrayList = null;
                int i = 0;
                if(!Utils.isNullString(oldSelectedOuid))
                    for(i = 0; i < codeItemData.size(); i++)
                    {
                        arrayList = (ArrayList)codeItemData.get(i);
                        if(Utils.isNullArrayList(arrayList))
                            continue;
                        if(oldSelectedOuid.equals((String)arrayList.get(0)))
                            break;
                        arrayList = null;
                    }

                if(arrayList == null)
                    arrayList = (ArrayList)codeItemData.get(0);
                nameTextField.setText((String)arrayList.get(1));
                descTextArea.setText((String)arrayList.get(2));
                codeItemIDTextField.setText((String)arrayList.get(3));
                classOuid = (String)arrayList.get(5);
                if(!Utils.isNullString(classOuid))
                    try
                    {
                        DOSChangeable dosCode = dos.getClass(classOuid);
                        filterTextField.setText(dosCode.get("name") + " [" + dosCode.get("ouid") + "]");
                        dosCode = null;
                    }
                    catch(IIPRequestException e)
                    {
                        e.printStackTrace();
                    }
                else
                    filterTextField.setText("");
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        if(isHierarchy)
            return;
        int selectedRow = codeItemTable.getTable().getSelectedRow();
        String ouidRow = codeItemTable.getSelectedOuidRow(selectedRow);
        if(ouidRow == null)
            return;
        int row = (new Integer(ouidRow)).intValue();
        ArrayList selectedList = (ArrayList)codeItemData.get(row);
        selectOuid = (String)selectedList.get(0);
        nameTextField.setText((String)selectedList.get(1));
        descTextArea.setText((String)selectedList.get(2));
        codeItemIDTextField.setText((String)selectedList.get(3));
        classOuid = (String)selectedList.get(5);
        if(!Utils.isNullString(classOuid))
            try
            {
                DOSChangeable dosCode = dos.getClass(classOuid);
                filterTextField.setText(dosCode.get("name") + " [" + dosCode.get("ouid") + "]");
                dosCode = null;
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
        else
            filterTextField.setText("");
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
        if(isHierarchy)
        {
            codeItemTable.getTable().clearSelection();
            return;
        } else
        {
            return;
        }
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
            createCodeItem();
        else
        if(command.equals("modify"))
            modifyCodeItem();
        else
        if(command.equals("clear"))
            clearCodeItemField();
        else
        if(command.equals("delete"))
        {
            deleteCodeItem();
            clearCodeItemField();
        } else
        if(command.equals("classSelectButton"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Class Selection");
            typeClassFrame.setVisible(true);
        }
    }

    public boolean isAdmin()
    {
        if(Utils.isNullString(LogIn.userID))
            return true;
        boolean isadmin = false;
        try
        {
            isadmin = aus.hasRole(LogIn.userID, "SYSTEM.ADMINISTRATOR");
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        return isadmin;
    }

    public void setHierarchy(boolean b)
    {
        isHierarchy = b;
        classSelectButton.setEnabled(isHierarchy);
    }

    public void setParentItemOuid(String string)
    {
        parentItemOuid = string;
    }

    public void selectItemByOuid(String codeItemOuid)
    {
        selectOuid = null;
        if(Utils.isNullArrayList(codeItemData))
            return;
        codeItemTable.getTable().clearSelection();
        clearCodeItemField();
        if(Utils.isNullString(codeItemOuid))
            return;
        ArrayList arrayList = null;
        String tempString = null;
        for(int i = 0; i < codeItemData.size(); i++)
        {
            arrayList = (ArrayList)codeItemData.get(i);
            tempString = (String)arrayList.get(0);
            if(codeItemOuid.equals(tempString))
            {
                codeItemTable.getTable().addRowSelectionInterval(i, i);
                break;
            }
            arrayList = null;
        }

        if(arrayList == null)
            return;
        selectOuid = codeItemOuid;
        nameTextField.setText((String)arrayList.get(1));
        descTextArea.setText((String)arrayList.get(2));
        codeItemIDTextField.setText((String)arrayList.get(3));
        parentItemOuid = (String)arrayList.get(4);
        classOuid = (String)arrayList.get(5);
        if(!Utils.isNullString(classOuid))
            try
            {
                DOSChangeable dosCode = dos.getClass(classOuid);
                filterTextField.setText(dosCode.get("name") + " [" + dosCode.get("ouid") + "]");
                dosCode = null;
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        else
            filterTextField.setText("");
    }

    public void setClass(ArrayList classList)
    {
        if(Utils.isNullArrayList(classList))
            return;
        classOuid = (String)classList.get(0);
        try
        {
            DOSChangeable classData = dos.getClass(classOuid);
            if(classData == null)
            {
                classOuid = null;
                filterTextField.setText("");
                return;
            }
            filterTextField.setText((String)classData.get("name") + " [" + classOuid + "]");
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    private final boolean UPPER_CASE = false;
    private final int TITLE_WIDTH = 120;
    private BorderLayout codeItemInfoBorderLayout;
    private JPanel mainPanel;
    private JPanel descPanel;
    private JScrollPane descScrPane;
    private DynaTextField nameTextField;
    private JLabel descLabel;
    private JTextArea descTextArea;
    private DynaTextField codeItemIDTextField;
    private DynaTextField filterTextField;
    private JButton classSelectButton;
    private JLabel dummyLabel;
    private JScrollPane codeItemTableScrPane;
    private Table codeItemTable;
    private ArrayList codeItemData;
    private ArrayList codeItemColumnName;
    private ArrayList codeItemColumnWidth;
    private Cursor handCursor;
    private JToolBar buttonToolBar;
    private BoxLayout buttonBoxLayout;
    private JButton createButton;
    private JButton modifyButton;
    private JButton clearButton;
    private JButton deleteButton;
    private DOS dos;
    private AUS aus;
    private String codeOuid;
    private String selectOuid;
    private boolean isHierarchy;
    private String parentItemOuid;
    private String classOuid;
}
