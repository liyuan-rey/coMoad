// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchJobScheduler.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.client.UIManagement;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.BJS;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction

public class BatchJobScheduler extends JFrame
    implements ActionListener, MouseListener, ListSelectionListener, WindowListener
{

    public BatchJobScheduler(JFrame frame)
    {
        dos = null;
        bjs = null;
        buttonToolBar = null;
        createButton = null;
        deleteButton = null;
        clearButton = null;
        refreshButton = null;
        exitButton = null;
        mainPanel = null;
        scheduleTableScrPane = null;
        scheduleTable = null;
        originalData = null;
        scheduleTableData = null;
        scheduleColumnName = null;
        scheduleColumnWidth = null;
        ouidTextField = null;
        intervalTextField = null;
        cntTextField = null;
        classpathTextField = null;
        methodTextField = null;
        argumentsPanel = null;
        argumentsLabel = null;
        argumentsTextArea = null;
        argumentsScrollPane = null;
        dos = ObjectModelingConstruction.dos;
        bjs = ObjectModelingConstruction.bjs;
        initialize();
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("Batch Job Scheduler");
        setLocation(112, 84);
        setSize(600, 400);
        addWindowListener(this);
        createButton = new JButton();
        createButton.setEnabled(true);
        createButton.setToolTipText("Create");
        createButton.setActionCommand("Create");
        createButton.setMargin(new Insets(0, 0, 0, 0));
        createButton.setIcon(new ImageIcon(getClass().getResource("/icons/Registry.gif")));
        createButton.addActionListener(this);
        deleteButton = new JButton();
        deleteButton.setToolTipText("Delete");
        deleteButton.setActionCommand("Delete");
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/icons/Delete.gif")));
        deleteButton.addActionListener(this);
        clearButton = new JButton();
        clearButton.setToolTipText("Clear");
        clearButton.setActionCommand("Clear");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        clearButton.addActionListener(this);
        refreshButton = new JButton();
        refreshButton.setToolTipText("Refresh");
        refreshButton.setActionCommand("Refresh");
        refreshButton.setMargin(new Insets(0, 0, 0, 0));
        refreshButton.setIcon(new ImageIcon(getClass().getResource("/icons/Refresh.gif")));
        refreshButton.addActionListener(this);
        exitButton = new JButton();
        exitButton.setToolTipText("Exit");
        exitButton.setActionCommand("Exit");
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        exitButton.addActionListener(this);
        buttonToolBar = new JToolBar();
        buttonToolBar.add(createButton);
        buttonToolBar.add(deleteButton);
        buttonToolBar.add(clearButton);
        buttonToolBar.add(refreshButton);
        buttonToolBar.add(exitButton);
        makeScheduleTable();
        scheduleTableScrPane = UIFactory.createStrippedScrollPane(null);
        scheduleTableScrPane.setViewportView(scheduleTable.getTable());
        scheduleTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        scheduleTableScrPane.setPreferredSize(new Dimension(500, 180));
        ouidTextField = new DynaTextField();
        ouidTextField.setEditable(false);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleWidth(100);
        ouidTextField.setTitleVisible(true);
        intervalTextField = new DynaTextField();
        intervalTextField.setEditable(true);
        intervalTextField.setTitleText("Interval");
        intervalTextField.setMandatory(true);
        intervalTextField.setTitleWidth(100);
        intervalTextField.setTitleVisible(true);
        cntTextField = new DynaTextField();
        cntTextField.setEditable(true);
        cntTextField.setTitleText("Count");
        cntTextField.setMandatory(false);
        cntTextField.setTitleWidth(100);
        cntTextField.setTitleVisible(true);
        classpathTextField = new DynaTextField();
        classpathTextField.setEditable(true);
        classpathTextField.setTitleText("Class Path");
        classpathTextField.setMandatory(true);
        classpathTextField.setTitleWidth(100);
        classpathTextField.setTitleVisible(true);
        methodTextField = new DynaTextField();
        methodTextField.setEditable(true);
        methodTextField.setTitleText("Method");
        methodTextField.setMandatory(true);
        methodTextField.setTitleWidth(100);
        methodTextField.setTitleVisible(true);
        argumentsLabel = new JLabel("Arguments");
        argumentsLabel.setDoubleBuffered(true);
        argumentsLabel.setPreferredSize(new Dimension(100, getHeight()));
        argumentsLabel.setAlignmentY(0.0F);
        argumentsTextArea = new JTextArea();
        argumentsTextArea.setDoubleBuffered(true);
        argumentsScrollPane = new JScrollPane();
        argumentsScrollPane.setViewportView(argumentsTextArea);
        argumentsScrollPane.setAlignmentY(0.0F);
        argumentsPanel = new JPanel();
        argumentsPanel.setLayout(new BoxLayout(argumentsPanel, 0));
        argumentsPanel.setDoubleBuffered(true);
        argumentsPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 1, 5));
        argumentsPanel.setMaximumSize(new Dimension(32767, 100));
        argumentsPanel.setMinimumSize(new Dimension(1, 100));
        argumentsPanel.setPreferredSize(new Dimension(1, 100));
        argumentsPanel.add(argumentsLabel);
        argumentsPanel.add(argumentsScrollPane);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, 1));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(ouidTextField);
        mainPanel.add(intervalTextField);
        mainPanel.add(cntTextField);
        mainPanel.add(classpathTextField);
        mainPanel.add(methodTextField);
        mainPanel.add(argumentsPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonToolBar, "North");
        getContentPane().add(scheduleTableScrPane, "Center");
        getContentPane().add(mainPanel, "South");
    }

    public void makeScheduleTable()
    {
        scheduleTableData = new ArrayList();
        scheduleColumnName = new ArrayList();
        scheduleColumnWidth = new ArrayList();
        scheduleColumnName.add("Ouid");
        scheduleColumnName.add("Interval");
        scheduleColumnName.add("Count");
        scheduleColumnName.add("ClassPath");
        scheduleColumnName.add("Method");
        scheduleColumnName.add("Arguments");
        scheduleColumnWidth.add(new Integer(60));
        scheduleColumnWidth.add(new Integer(60));
        scheduleColumnWidth.add(new Integer(60));
        scheduleColumnWidth.add(new Integer(150));
        scheduleColumnWidth.add(new Integer(100));
        scheduleColumnWidth.add(new Integer(150));
        scheduleTable = new Table(scheduleTableData, (ArrayList)scheduleColumnName.clone(), (ArrayList)scheduleColumnWidth.clone(), 0);
        scheduleTable.getTable().addMouseListener(this);
        scheduleTable.getTable().getSelectionModel().addListSelectionListener(this);
        scheduleTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4, 5
        });
        scheduleTable.setIndexColumn(0);
        refreshButton_actionPerformed();
    }

    public void createButton_actionPerformed()
    {
        DOSChangeable scheduleData = new DOSChangeable();
        ArrayList arguments = new ArrayList();
        if(Utils.isNullString(intervalTextField.getText()))
            scheduleData.put("interval", null);
        else
            scheduleData.put("interval", new Long(intervalTextField.getText()));
        if(Utils.isNullString(cntTextField.getText()))
            scheduleData.put("count", null);
        else
            scheduleData.put("count", new Integer(cntTextField.getText()));
        scheduleData.put("classpath", classpathTextField.getText());
        scheduleData.put("method", methodTextField.getText());
        try
        {
            int cnt = argumentsTextArea.getLineCount();
            int offset = 0;
            int beforeOffset = 0;
            String tmpStr = null;
            for(int i = 0; i < cnt; i++)
            {
                if(i < cnt - 1)
                    offset = argumentsTextArea.getLineStartOffset(i + 1);
                else
                    offset = argumentsTextArea.getLineEndOffset(i) + 1;
                tmpStr = argumentsTextArea.getText().substring(beforeOffset, offset - 1);
                if(!Utils.isNullString(tmpStr))
                    arguments.add(tmpStr);
                beforeOffset = offset;
            }

        }
        catch(BadLocationException be)
        {
            System.err.println(be);
        }
        scheduleData.put("arguments", arguments);
        scheduleData.put("is.temporary", Boolean.FALSE);
        String ouid;
        try
        {
            ouid = bjs.add(scheduleData);
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
        refreshButton_actionPerformed();
        clearButton_actionPerformed();
    }

    public void deleteButton_actionPerformed()
    {
        int selRow = scheduleTable.getSelectedRowNumber();
        ArrayList selectedData = null;
        if(selRow > -1)
        {
            String ouidRow = scheduleTable.getSelectedOuidRow(selRow);
            if(ouidRow != null)
                selectedData = (ArrayList)originalData.get(Integer.parseInt(ouidRow));
            else
                selectedData = (ArrayList)originalData.get(selRow);
            DOSChangeable scheduleData = new DOSChangeable();
            scheduleData.put("ouid", (String)selectedData.get(0));
            scheduleData.put("interval", (Long)selectedData.get(1));
            scheduleData.put("count", (Integer)selectedData.get(2));
            scheduleData.put("classpath", (String)selectedData.get(3));
            scheduleData.put("method", (String)selectedData.get(4));
            scheduleData.put("arguments", (ArrayList)selectedData.get(5));
            try
            {
                bjs.remove(scheduleData);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
            refreshButton_actionPerformed();
            clearButton_actionPerformed();
        }
    }

    public void clearButton_actionPerformed()
    {
        ouidTextField.setText("");
        intervalTextField.setText("");
        cntTextField.setText("");
        classpathTextField.setText("");
        methodTextField.setText("");
        argumentsTextArea.setText("");
    }

    public void refreshButton_actionPerformed()
    {
        try
        {
            originalData = null;
            scheduleTableData.clear();
            originalData = bjs.retrieveJobSchedule();
            ArrayList rowList = null;
            ArrayList rowList2 = null;
            ArrayList arguments = null;
            StringBuffer arguments2 = null;
            for(int i = 0; i < originalData.size(); i++)
            {
                rowList = (ArrayList)originalData.get(i);
                rowList2 = (ArrayList)rowList.clone();
                arguments = (ArrayList)rowList.get(5);
                arguments2 = new StringBuffer();
                for(int j = 0; j < arguments.size(); j++)
                {
                    if(j > 0)
                        arguments2.append(", ");
                    arguments2.append((String)arguments.get(j));
                }

                rowList2.set(5, arguments2.toString());
                arguments2 = null;
                scheduleTableData.add(rowList2);
                rowList2 = null;
            }

            scheduleTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void exitButton_actionPerformed()
    {
        createButton.removeActionListener(this);
        deleteButton.removeActionListener(this);
        clearButton.removeActionListener(this);
        refreshButton.removeActionListener(this);
        exitButton.removeActionListener(this);
        scheduleTable.getTable().removeMouseListener(this);
        scheduleTable.getTable().getSelectionModel().removeListSelectionListener(this);
        removeWindowListener(this);
        dispose();
    }

    public void scheduleTable_valueChanged()
    {
        int selRow = scheduleTable.getSelectedRowNumber();
        ArrayList selectedData = null;
        if(selRow > -1)
        {
            String ouidRow = scheduleTable.getSelectedOuidRow(selRow);
            if(ouidRow != null)
                selectedData = (ArrayList)originalData.get(Integer.parseInt(ouidRow));
            else
                selectedData = (ArrayList)originalData.get(selRow);
            ouidTextField.setText((String)selectedData.get(0));
            intervalTextField.setText(((Long)selectedData.get(1)).toString());
            cntTextField.setText(((Integer)selectedData.get(2)).toString());
            classpathTextField.setText((String)selectedData.get(3));
            methodTextField.setText((String)selectedData.get(4));
            ArrayList arguments = (ArrayList)selectedData.get(5);
            argumentsTextArea.setText("");
            for(int i = 0; i < arguments.size(); i++)
            {
                argumentsTextArea.append((String)arguments.get(i));
                if(i <= arguments.size() - 2)
                    argumentsTextArea.append(System.getProperty("line.separator"));
            }

        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals("Create"))
            createButton_actionPerformed();
        else
        if(command.equals("Delete"))
            deleteButton_actionPerformed();
        else
        if(command.equals("Clear"))
            clearButton_actionPerformed();
        else
        if(command.equals("Refresh"))
        {
            refreshButton_actionPerformed();
            clearButton_actionPerformed();
        } else
        if(command.equals("Exit"))
            exitButton_actionPerformed();
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source == scheduleTable.getTable())
            scheduleTable_valueChanged();
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

    public void valueChanged(ListSelectionEvent e)
    {
        if(!e.getValueIsAdjusting())
        {
            Object source = e.getSource();
            if(source == scheduleTable.getTable().getSelectionModel())
                scheduleTable_valueChanged();
        }
    }

    public void windowClosing(WindowEvent e)
    {
        exitButton_actionPerformed();
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

    private DOS dos;
    private BJS bjs;
    private JToolBar buttonToolBar;
    private JButton createButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;
    private JButton exitButton;
    private JPanel mainPanel;
    private JScrollPane scheduleTableScrPane;
    private Table scheduleTable;
    private ArrayList originalData;
    private ArrayList scheduleTableData;
    private ArrayList scheduleColumnName;
    private ArrayList scheduleColumnWidth;
    private DynaTextField ouidTextField;
    private DynaTextField intervalTextField;
    private DynaTextField cntTextField;
    private DynaTextField classpathTextField;
    private DynaTextField methodTextField;
    private JPanel argumentsPanel;
    private JLabel argumentsLabel;
    private JTextArea argumentsTextArea;
    private JScrollPane argumentsScrollPane;
}
