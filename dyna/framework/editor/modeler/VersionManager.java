// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VersionManager.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.NDS;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction

public class VersionManager extends JFrame
    implements ActionListener, WindowListener, MouseListener
{

    public VersionManager(JFrame frame)
    {
        dos = null;
        nds = null;
        callback = null;
        selectedData = null;
        rowInt = -2;
        NDS_VERSIONSTRING = "::/DOS_SYSTEM_DIRECTORY/VERSIONSTRING";
        screenSize = null;
        windowSize = null;
        allClassData = null;
        selectedColumnNames = null;
        selectedColumnWidths = null;
        buttonToolBar = null;
        saveButton = null;
        deleteButton = null;
        exitButton = null;
        gridBag = null;
        gridBagCon = null;
        searchPanel = null;
        centerPanel = null;
        verticalToolBar = null;
        selectionButton = null;
        deselectionButton = null;
        upwardButton = null;
        downwardButton = null;
        allClassTable = null;
        selectedTable = null;
        selectedScrPane = null;
        valueTextField = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            nds = (NDS)ObjectModelingConstruction.dfw.getServiceInstance("DF30NDS1");
            String verStr = null;
            ArrayList versionList = nds.getChildNodeList(NDS_VERSIONSTRING);
            selectedData = new ArrayList();
            ArrayList tmpList = null;
            if(!Utils.isNullArrayList(versionList))
            {
                int listSize = versionList.size();
                for(int i = 0; i < listSize; i++)
                {
                    verStr = nds.getValue(NDS_VERSIONSTRING + "/" + (String)versionList.get(i));
                    tmpList = new ArrayList();
                    tmpList.add(verStr);
                    selectedData.add(tmpList);
                    tmpList = null;
                }

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        setFont(frame.getFont());
        makeStringSourceTable();
        makeSelectedStringTable();
        initialize();
    }

    public void initialize()
    {
        setSize(500, 300);
        setTitle("Version Manager");
        addWindowListener(this);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/dynapdm.jpg"));
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
        gridBag = new GridBagLayout();
        gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        centerPanel = new JPanel();
        centerPanel.setLayout(gridBag);
        Dimension verticalToolBarSize = new Dimension(26, 300);
        verticalToolBar = new JPanel();
        verticalToolBar.setLayout(new BoxLayout(verticalToolBar, 1));
        verticalToolBar.setPreferredSize(verticalToolBarSize);
        verticalToolBar.setMaximumSize(verticalToolBarSize);
        verticalToolBar.setMinimumSize(verticalToolBarSize);
        verticalToolBarSize = null;
        selectionButton = new JButton();
        selectionButton.setIcon(new ImageIcon("icons/SelectOneRow.gif"));
        selectionButton.setToolTipText("Select");
        selectionButton.setActionCommand("Select");
        selectionButton.addActionListener(this);
        verticalToolBar.add(selectionButton);
        deselectionButton = new JButton();
        deselectionButton.setIcon(new ImageIcon("icons/DeselectOneRow.gif"));
        deselectionButton.setToolTipText("Deselect");
        deselectionButton.setActionCommand("Deselect");
        deselectionButton.addActionListener(this);
        verticalToolBar.add(deselectionButton);
        upwardButton = new JButton();
        upwardButton.setIcon(new ImageIcon("icons/Upward.gif"));
        upwardButton.setToolTipText("Upward");
        upwardButton.setActionCommand("Upward");
        upwardButton.addActionListener(this);
        verticalToolBar.add(upwardButton);
        downwardButton = new JButton();
        downwardButton.setIcon(new ImageIcon("icons/Downward.gif"));
        downwardButton.setToolTipText("Downward");
        downwardButton.setActionCommand("Downward");
        downwardButton.addActionListener(this);
        verticalToolBar.add(downwardButton);
        selectedScrPane = UIFactory.createStrippedScrollPane(null);
        selectedScrPane.getViewport().add(selectedTable.getTable());
        selectedScrPane.getViewport().setPreferredSize(new Dimension(210, 150));
        selectedScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(searchPanel, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(verticalToolBar, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(selectedScrPane, gridBagCon);
        centerPanel.add(searchPanel);
        centerPanel.add(verticalToolBar);
        centerPanel.add(selectedScrPane);
        getContentPane().add(centerPanel, "Center");
        saveButton = new JButton();
        saveButton.setToolTipText("Save");
        saveButton.setMargin(new Insets(0, 0, 0, 0));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/icons/Modification.gif")));
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(this);
        deleteButton = new JButton();
        deleteButton.setToolTipText("Delete");
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete16.gif")));
        deleteButton.setActionCommand("Delete");
        deleteButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        buttonToolBar = new JToolBar();
        buttonToolBar.add(saveButton);
        buttonToolBar.add(deleteButton);
        buttonToolBar.add(exitButton);
        getContentPane().add(buttonToolBar, "North");
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void makeStringSourceTable()
    {
        try
        {
            searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, 0));
            valueTextField = new JTextField();
            valueTextField.setMinimumSize(new Dimension(210, 22));
            valueTextField.setPreferredSize(new Dimension(210, 22));
            valueTextField.setMaximumSize(new Dimension(900, 22));
            searchPanel.add(valueTextField);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void makeSelectedStringTable()
    {
        selectedColumnNames = new ArrayList();
        selectedColumnWidths = new ArrayList();
        selectedColumnNames.add("Version String");
        selectedColumnWidths.add(new Integer(210));
        selectedTable = new Table(selectedData, (ArrayList)selectedColumnNames.clone(), (ArrayList)selectedColumnWidths.clone(), 1, 79);
        selectedTable.setColumnSequence(new int[1]);
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
                nds.removeNode(NDS_VERSIONSTRING);
                nds.addNode("::/DOS_SYSTEM_DIRECTORY", "VERSIONSTRING", "DOS.VERSION", "DOS.VERSION");
                for(int i = 0; i < selectedData.size(); i++)
                {
                    String sequence = String.valueOf(i + 1);
                    if(sequence.length() == 1)
                        sequence = "00" + sequence;
                    else
                    if(sequence.length() == 2)
                        sequence = "0" + sequence;
                    nds.addNode(NDS_VERSIONSTRING, sequence, "DOS.VERSION", (String)((ArrayList)selectedData.get(i)).get(0));
                }

                dos.setVersionString();
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("Delete"))
            try
            {
                boolean result = nds.removeNode(NDS_VERSIONSTRING);
                if(result)
                {
                    selectedData.clear();
                    selectedTable.changeTableData();
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("Select"))
        {
            if(allClassTable == null)
            {
                if(valueTextField == null || Utils.isNullString(valueTextField.getText()))
                    return;
                String inputedString = valueTextField.getText();
                if(selectedData == null || selectedData.size() == 0)
                {
                    ArrayList selectedClassList = new ArrayList();
                    selectedClassList.add(inputedString);
                    selectedData.add(selectedClassList.clone());
                    selectedClassList.clear();
                    selectedClassList = null;
                    selectedTable.changeTableData();
                } else
                {
                    ArrayList selectedDataClone = new ArrayList();
                    ArrayList selectedClassList = new ArrayList();
                    boolean isSame = false;
                    for(int j = 0; j < selectedData.size(); j++)
                    {
                        String tmpStr = ((ArrayList)selectedData.get(j)).get(0).toString();
                        if(tmpStr.equals(inputedString))
                            isSame = true;
                    }

                    if(!isSame)
                    {
                        selectedClassList.add(inputedString);
                        selectedDataClone.add(selectedClassList.clone());
                        selectedClassList.clear();
                        selectedClassList = null;
                    }
                    for(int i = 0; i < selectedDataClone.size(); i++)
                        selectedData.add(selectedDataClone.get(i));

                    if(selectedDataClone.size() > 0)
                        selectedTable.changeTableData();
                }
            } else
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
                        for(int j = 0; j < allClassList.size(); j++)
                            selectedClassList.add(allClassList.get(j));

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
                            for(int j = 0; j < allClassList.size(); j++)
                                selectedClassList.add(allClassList.get(j));

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
        int row = selectedTable.getTable().getSelectedRow();
        String ouidRow = selectedTable.getSelectedOuidRow(row);
        selectedTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
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
    private DialogReturnCallback callback;
    private ArrayList selectedData;
    private int rowInt;
    private String NDS_VERSIONSTRING;
    private Dimension screenSize;
    private Dimension windowSize;
    private ArrayList allClassData;
    private ArrayList selectedColumnNames;
    private ArrayList selectedColumnWidths;
    private JToolBar buttonToolBar;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton exitButton;
    private GridBagLayout gridBag;
    private GridBagConstraints gridBagCon;
    private JPanel searchPanel;
    private JPanel centerPanel;
    private JPanel verticalToolBar;
    private JButton selectionButton;
    private JButton deselectionButton;
    private JButton upwardButton;
    private JButton downwardButton;
    private Table allClassTable;
    private Table selectedTable;
    private JScrollPane selectedScrPane;
    private JTextField valueTextField;
    final int offset = 3;
    final int init_xcord = 10;
    final int init_ycord = 10;
    final int titleWidth = 100;
    final int totalWidth = 260;
    final int fieldHeight = 20;
    final int columnWidth = 70;
}
