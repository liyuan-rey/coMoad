// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClassMapDialog.java

package dyna.framework.editor.workflow;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            WorkflowModeler

public class ClassMapDialog extends JDialog
    implements ActionListener, WindowListener, MouseListener
{

    public ClassMapDialog(JFrame frame, DOS dos, boolean modal, String processOuid)
    {
        super(frame, modal);
        this.dos = null;
        nds = null;
        wfm = null;
        callback = null;
        modelOuid = null;
        this.processOuid = null;
        selectedCheck = new DOSChangeable();
        rowInt = -2;
        selectedRows = null;
        allClassData = null;
        allClassColumnNames = null;
        allClassColumnWidths = null;
        selectedData = null;
        selectedColumnNames = null;
        selectedColumnWidths = null;
        buttonToolBar = null;
        selectButton = null;
        exitButton = null;
        gridBag = null;
        gridBagCon = null;
        centerPanel = null;
        verticalToolBar = null;
        selectionButton = null;
        deselectionButton = null;
        upwardButton = null;
        downwardButton = null;
        allClassTable = null;
        allFieldScrPane = null;
        selectedTable = null;
        selectedScrPane = null;
        this.processOuid = processOuid;
        this.dos = dos;
        DOSChangeable process = null;
        try
        {
            nds = (NDS)WorkflowModeler.dfw.getServiceInstance("DF30NDS1");
            wfm = (WFM)WorkflowModeler.dfw.getServiceInstance("DF30WFM1");
            modelOuid = wfm.getModelOfProcessDefinition(processOuid);
            setFont(frame.getFont());
            makeAllClassTable();
            makeSelectedClassTable();
            initialize();
        }
        catch(ServiceNotFoundException e)
        {
            System.out.println(e);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        setSize(500, 300);
        setTitle("Class Map");
        addWindowListener(this);
        gridBag = new GridBagLayout();
        gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        centerPanel = new JPanel();
        centerPanel.setLayout(gridBag);
        Dimension verticalToolBarSize = new Dimension(26, 300);
        verticalToolBar = new JToolBar();
        verticalToolBar.setOrientation(1);
        verticalToolBar.setFloatable(false);
        verticalToolBar.setPreferredSize(verticalToolBarSize);
        verticalToolBar.setMaximumSize(verticalToolBarSize);
        verticalToolBar.setMinimumSize(verticalToolBarSize);
        verticalToolBarSize = null;
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        selectionButton = new JButton();
        selectionButton.setMargin(zeroInsets);
        selectionButton.setIcon(new ImageIcon("icons/SelectOneRow.gif"));
        selectionButton.setToolTipText("Select");
        selectionButton.setActionCommand("Select");
        selectionButton.addActionListener(this);
        verticalToolBar.add(selectionButton);
        deselectionButton = new JButton();
        deselectionButton.setMargin(zeroInsets);
        deselectionButton.setIcon(new ImageIcon("icons/DeselectOneRow.gif"));
        deselectionButton.setToolTipText("Deselect");
        deselectionButton.setActionCommand("Deselect");
        deselectionButton.addActionListener(this);
        verticalToolBar.add(deselectionButton);
        upwardButton = new JButton();
        upwardButton.setMargin(zeroInsets);
        upwardButton.setIcon(new ImageIcon("icons/Upward.gif"));
        upwardButton.setToolTipText("Upward");
        upwardButton.setActionCommand("Upward");
        upwardButton.addActionListener(this);
        verticalToolBar.add(upwardButton);
        downwardButton = new JButton();
        downwardButton.setMargin(zeroInsets);
        downwardButton.setIcon(new ImageIcon("icons/Downward.gif"));
        downwardButton.setToolTipText("Downward");
        downwardButton.setActionCommand("Downward");
        downwardButton.addActionListener(this);
        verticalToolBar.add(downwardButton);
        zeroInsets = null;
        allFieldScrPane = UIFactory.createStrippedScrollPane(null);
        allFieldScrPane.getViewport().add(allClassTable.getTable());
        allFieldScrPane.getViewport().setPreferredSize(new Dimension(210, 150));
        allFieldScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        selectedScrPane = UIFactory.createStrippedScrollPane(null);
        selectedScrPane.getViewport().add(selectedTable.getTable());
        selectedScrPane.getViewport().setPreferredSize(new Dimension(210, 150));
        selectedScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(allFieldScrPane, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(verticalToolBar, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(selectedScrPane, gridBagCon);
        centerPanel.add(allFieldScrPane);
        centerPanel.add(verticalToolBar);
        centerPanel.add(selectedScrPane);
        getContentPane().add(centerPanel, "Center");
        buttonToolBar = new JToolBar();
        buttonToolBar.setFloatable(false);
        Dimension buttonSize = new Dimension(70, 25);
        selectButton = new JButton("Select");
        selectButton.setIcon(new ImageIcon("icons/Ok.gif"));
        selectButton.setActionCommand("Save");
        selectButton.setFont(getFont());
        selectButton.setMinimumSize(buttonSize);
        selectButton.setPreferredSize(buttonSize);
        selectButton.addActionListener(this);
        exitButton = new JButton("Cancel");
        exitButton.setIcon(new ImageIcon("icons/Cancel.gif"));
        exitButton.setActionCommand("Exit");
        exitButton.setFont(getFont());
        exitButton.setMinimumSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);
        exitButton.addActionListener(this);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(selectButton);
        buttonToolBar.add(Box.createHorizontalStrut(10));
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "South");
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void makeAllClassTable()
    {
        try
        {
            DOSChangeable result = new DOSChangeable();
            DOSChangeable packageInfo = new DOSChangeable();
            ArrayList packageList = new ArrayList();
            ArrayList subClassList = new ArrayList();
            ArrayList allClassList = new ArrayList();
            ArrayList tmpClassData = new ArrayList();
            boolean subClassVersionable = false;
            allClassData = new ArrayList();
            allClassColumnNames = new ArrayList();
            allClassColumnWidths = new ArrayList();
            allClassColumnNames.add("OUID");
            allClassColumnNames.add("Name");
            allClassColumnNames.add("Description");
            allClassColumnWidths.add(new Integer(70));
            allClassColumnWidths.add(new Integer(70));
            allClassColumnWidths.add(new Integer(70));
            packageList = dos.listPackage(modelOuid);
            Iterator packageKey;
            for(packageKey = packageList.iterator(); packageKey.hasNext();)
            {
                packageInfo = (DOSChangeable)packageKey.next();
                subClassList = dos.listClassInPackage((String)packageInfo.get("ouid"));
                allClassList.addAll(subClassList);
                subClassList.clear();
                subClassList = null;
                packageInfo.clear();
                packageInfo = null;
            }

            packageKey = null;
            packageList.clear();
            packageList = null;
            for(int i = 0; i < allClassList.size(); i++)
            {
                result = (DOSChangeable)allClassList.get(i);
                selectedCheck.put((String)result.get("ouid"), new Integer(i));
                tmpClassData.add(result.get("ouid"));
                tmpClassData.add(result.get("name"));
                tmpClassData.add(result.get("description"));
                allClassData.add(tmpClassData.clone());
                tmpClassData.clear();
            }

            tmpClassData = null;
            allClassList.clear();
            allClassList = null;
            allClassTable = new Table(allClassData, (ArrayList)allClassColumnNames.clone(), (ArrayList)allClassColumnWidths.clone(), 1, 79);
            allClassTable.setColumnSequence(new int[] {
                0, 1, 2
            });
            allClassTable.getTable().setRowSelectionAllowed(true);
            allClassTable.getTable().addMouseListener(this);
            allClassTable.setIndexColumn(0);
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public void makeSelectedClassTable()
    {
        ArrayList selectedList = null;
        ArrayList tmpList = null;
        Iterator selectedKey = null;
        DOSChangeable tmpObject = null;
        String tmpString = null;
        selectedData = new ArrayList();
        selectedColumnNames = new ArrayList();
        selectedColumnWidths = new ArrayList();
        selectedColumnNames.add("OUID");
        selectedColumnNames.add("Name");
        selectedColumnNames.add("Description");
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        selectedColumnWidths.add(new Integer(70));
        try
        {
            selectedList = wfm.listClassOfProcessDefinition(processOuid);
            if(selectedList != null)
            {
                tmpList = new ArrayList();
                int i = 0;
                for(selectedKey = selectedList.iterator(); selectedKey.hasNext();)
                {
                    tmpString = (String)selectedKey.next();
                    tmpObject = dos.getClass(tmpString);
                    if(tmpObject != null)
                    {
                        i++;
                        selectedCheck.put((String)tmpObject.get("ouid"), new Integer(i));
                        tmpList.add(tmpObject.get("ouid"));
                        tmpList.add(tmpObject.get("name"));
                        tmpList.add(tmpObject.get("description"));
                        selectedData.add(tmpList.clone());
                        tmpList.clear();
                    }
                }

                tmpList = null;
                selectedKey = null;
                selectedList.clear();
                selectedList = null;
            }
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
        selectedTable.setColumnSequence(new int[] {
            0, 1, 2
        });
        selectedTable.getTable().setRowSelectionAllowed(true);
        selectedTable.getTable().addMouseListener(this);
        selectedTable.setIndexColumn(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            windowClosing(null);
        else
        if(command.equals("Save"))
            try
            {
                String classOuid = null;
                ArrayList classList = wfm.listClassOfProcessDefinition(processOuid);
                if(classList != null)
                {
                    Iterator classKey;
                    for(classKey = classList.iterator(); classKey.hasNext(); wfm.removeClassFromProcessDefinition(processOuid, classOuid))
                        classOuid = (String)classKey.next();

                    classKey = null;
                    classList.clear();
                    classList = null;
                }
                DOSChangeable selectedResult = new DOSChangeable();
                int size = 0;
                for(int i = 0; i < selectedData.size(); i++)
                {
                    selectedResult.put((new Integer(size)).toString(), (String)((ArrayList)selectedData.get(i)).get(0));
                    size++;
                }

                selectedRows = new int[size];
                for(int i = 0; i < selectedData.size(); i++)
                {
                    classOuid = (String)((ArrayList)selectedData.get(i)).get(0);
                    selectedRows[i] = ((Integer)selectedCheck.get(classOuid)).intValue();
                    wfm.addClassToProcessDefinition(processOuid, classOuid);
                }

                selectedResult.clear();
                selectedResult = null;
                windowClosing(null);
            }
            catch(IIPRequestException e2)
            {
                System.err.println(e2);
            }
        else
        if(command.equals("Select"))
        {
            if(allClassTable.getTable().getSelectedRowCount() <= 0)
                return;
            int rows[] = allClassTable.getTable().getSelectedRows();
            ArrayList selectedList = allClassTable.getSelectedOuidRows(rows);
            int selrows[] = new int[selectedList.size()];
            for(int i = 0; i < selectedList.size(); i++)
                selrows[i] = (new Integer((String)selectedList.get(i))).intValue();

            if(selectedData == null || selectedData.size() == 0)
            {
                for(int i = 0; i < selrows.length; i++)
                {
                    ArrayList allClassList = null;
                    ArrayList selectedClassList = new ArrayList();
                    allClassList = (ArrayList)allClassData.get(selrows[i]);
                    selectedClassList.add(allClassList.get(0));
                    selectedClassList.add(allClassList.get(1));
                    selectedClassList.add(allClassList.get(2));
                    selectedData.add(selectedClassList.clone());
                    selectedClassList.clear();
                    selectedClassList = null;
                }

                selectedTable.changeTableData();
            } else
            {
                ArrayList selectedDataClone = new ArrayList();
                for(int i = 0; i < selrows.length; i++)
                {
                    ArrayList allClassList = null;
                    ArrayList selectedClassList = new ArrayList();
                    boolean isSame = false;
                    allClassList = (ArrayList)allClassData.get(selrows[i]);
                    for(int j = 0; j < selectedData.size(); j++)
                    {
                        String tmpStr = (String)((ArrayList)selectedData.get(j)).get(0);
                        if(tmpStr.equals(allClassList.get(0)))
                            isSame = true;
                    }

                    if(!isSame)
                    {
                        selectedClassList.add(allClassList.get(0));
                        selectedClassList.add(allClassList.get(1));
                        selectedClassList.add(allClassList.get(2));
                        selectedDataClone.add(selectedClassList.clone());
                        selectedClassList.clear();
                        selectedClassList = null;
                    }
                }

                for(int i = 0; i < selectedDataClone.size(); i++)
                    selectedData.add(selectedDataClone.get(i));

                if(selectedDataClone.size() > 0)
                    selectedTable.changeTableData();
            }
        } else
        if(command.equals("Deselect"))
        {
            if(selectedTable.getTable().getSelectedRowCount() <= 0)
                return;
            int selrows[] = selectedTable.getTable().getSelectedRows();
            for(int i = selrows.length - 1; i >= 0; i--)
                selectedData.remove(selrows[i]);

            selectedTable.changeTableData();
            if(selectedData.size() > selrows[0])
                selectedTable.getTable().setRowSelectionInterval(selrows[0], selrows[0]);
        } else
        if(command.equals("Upward"))
        {
            if(selectedTable.getTable().getSelectedRowCount() != 1)
                return;
            int selrow = selectedTable.getTable().getSelectedRow();
            if(selrow > 0)
            {
                ArrayList tmp1 = (ArrayList)selectedData.get(selrow - 1);
                ArrayList tmp2 = (ArrayList)selectedData.get(selrow);
                selectedData.remove(selrow - 1);
                selectedData.remove(selrow - 1);
                selectedData.add(selrow - 1, tmp2);
                selectedData.add(selrow, tmp1);
                selectedTable.changeTableData();
                selectedTable.getTable().setRowSelectionInterval(selrow - 1, selrow - 1);
                rowInt = selrow - 1;
            }
        } else
        if(command.equals("Downward"))
        {
            if(selectedTable.getTable().getSelectedRowCount() != 1)
                return;
            int selrow = selectedTable.getTable().getSelectedRow();
            if(selrow < selectedData.size() - 1)
            {
                ArrayList tmp1 = (ArrayList)selectedData.get(selrow);
                ArrayList tmp2 = (ArrayList)selectedData.get(selrow + 1);
                selectedData.remove(selrow);
                selectedData.remove(selrow);
                selectedData.add(selrow, tmp2);
                selectedData.add(selrow + 1, tmp1);
                selectedTable.changeTableData();
                selectedTable.getTable().setRowSelectionInterval(selrow + 1, selrow + 1);
            }
        } else
        if(command.equals("Exit"))
            windowClosing(null);
    }

    public int getSelectRow()
    {
        if(rowInt == -2)
            return selectedTable.getTable().getSelectedRow();
        else
            return rowInt;
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        removeWindowListener(this);
        callback = null;
        dos = null;
        dispose();
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

    public void mouseClicked(MouseEvent me)
    {
        Object source = me.getSource();
        if(source.equals(allClassTable.getTable()))
        {
            int row = allClassTable.getTable().getSelectedRow();
            String ouidRow = allClassTable.getSelectedOuidRow(row);
            allClassTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        } else
        {
            int row = selectedTable.getTable().getSelectedRow();
            String ouidRow = selectedTable.getSelectedOuidRow(row);
            selectedTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void setDialogReturnCallback(DialogReturnCallback callback)
    {
        this.callback = callback;
    }

    private DOS dos;
    private NDS nds;
    private WFM wfm;
    private DialogReturnCallback callback;
    private String modelOuid;
    private String processOuid;
    private DOSChangeable selectedCheck;
    private int rowInt;
    private int selectedRows[];
    private ArrayList allClassData;
    private ArrayList allClassColumnNames;
    private ArrayList allClassColumnWidths;
    private ArrayList selectedData;
    private ArrayList selectedColumnNames;
    private ArrayList selectedColumnWidths;
    private JToolBar buttonToolBar;
    private JButton selectButton;
    private JButton exitButton;
    private GridBagLayout gridBag;
    private GridBagConstraints gridBagCon;
    private JPanel centerPanel;
    private JToolBar verticalToolBar;
    private JButton selectionButton;
    private JButton deselectionButton;
    private JButton upwardButton;
    private JButton downwardButton;
    private Table allClassTable;
    private JScrollPane allFieldScrPane;
    private Table selectedTable;
    private JScrollPane selectedScrPane;
    final int offset = 3;
    final int init_xcord = 10;
    final int init_ycord = 10;
    final int titleWidth = 100;
    final int totalWidth = 260;
    final int fieldHeight = 20;
    final int columnWidth = 70;
}
