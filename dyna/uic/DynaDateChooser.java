// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaDateChooser.java

package dyna.uic;

import com.jgoodies.swing.util.UIFactory;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;

// Referenced classes of package dyna.uic:
//            UIUtils

public class DynaDateChooser extends JDialog
    implements ActionListener, MouseListener, FocusListener, WindowListener
{
    final class CalendarModel extends AbstractTableModel
    {

        public int getRowCount()
        {
            return 6;
        }

        public int getColumnCount()
        {
            return 7;
        }

        public String getColumnName(int c)
        {
            int firstDayOfWeek = calendar.getFirstDayOfWeek();
            calendar.set(7, c + firstDayOfWeek);
            return sdf.format(calendar.getTime());
        }

        public Class getColumnClass(int c)
        {
            return getValueAt(0, c).getClass();
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return calendarCache[rowIndex * 7 + columnIndex];
        }

        public void setDate(Date date)
        {
            if(calendar == null)
                calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(1);
            calendar.setTime(date);
            calendar.set(5, 1);
            calendar.add(5, calendar.get(7) * -1);
            for(int i = 0; i < 42; i++)
            {
                calendar.add(5, 1);
                calendarCache[i] = (Calendar)calendar.clone();
                if(date.equals(calendar.getTime()) && DynaDateChooser.chooser.centerTable != null)
                {
                    DynaDateChooser.chooser.centerTable.setColumnSelectionInterval(i % 7, i % 7);
                    DynaDateChooser.chooser.centerTable.setRowSelectionInterval(i / 7, i / 7);
                }
            }

            calendar.setTime(date);
        }

        public void setDate(String dateString)
        {
            if(dateString != null && !dateString.equals(""))
                try
                {
                    setDate(sdf2.parse(dateString));
                }
                catch(ParseException pe)
                {
                    System.out.println(pe.getLocalizedMessage());
                    setDate(today);
                }
            else
                setDate(today);
        }

        public int getYear()
        {
            return calendar.get(1);
        }

        public int getMonth()
        {
            return calendar.get(2) + 1;
        }

        public void setYear(int year)
        {
            calendar.set(1, year);
            setDate(calendar.getTime());
            fireTableDataChanged();
        }

        public void setMonth(int month)
        {
            calendar.set(2, month);
            setDate(calendar.getTime());
            fireTableDataChanged();
        }

        public void setYearMonth(int year, int month)
        {
            calendar.set(1, year);
            calendar.set(2, month);
            setDate(calendar.getTime());
            fireTableDataChanged();
        }

        public void addYear(int year)
        {
            calendar.add(1, year);
            setDate(calendar.getTime());
            fireTableDataChanged();
        }

        public void addMonth(int month)
        {
            calendar.add(2, month);
            setDate(calendar.getTime());
            fireTableDataChanged();
        }

        public CalendarModel(Date date)
        {
            setDate(date);
        }

        public CalendarModel()
        {
            this(new Date());
        }
    }

    final class CalendarCellRenderer extends JLabel
        implements TableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object cal, boolean isSelected, boolean hasFocus, int row, int column)
        {
            calendar = (Calendar)cal;
            setText(String.valueOf(calendar.get(5)));
            if(calendar.get(1) == ddc.calendarModel.getYear() && calendar.get(2) == ddc.calendarModel.getMonth() - 1)
            {
                if(calendar.get(7) == calendar.getFirstDayOfWeek())
                    setForeground(Color.red);
                else
                    setForeground((Color)UIManager.getDefaults().get("textText"));
            } else
            {
                setForeground((Color)UIManager.getDefaults().get("textInactiveText"));
            }
            if(isSelected)
                setBackground((Color)UIManager.getDefaults().get("Table.selectionBackground"));
            else
                setBackground((Color)UIManager.getDefaults().get("Table.background"));
            if(hasFocus)
                setBorder(new LineBorder(Color.blue));
            else
                setBorder(null);
            return this;
        }

        Calendar todayCalendar;
        Calendar calendar;
        DynaDateChooser ddc;

        public CalendarCellRenderer(DynaDateChooser obj)
        {
            todayCalendar = null;
            calendar = null;
            ddc = null;
            ddc = obj;
            todayCalendar = Calendar.getInstance();
            setFont(new Font("Dialog", 0, 11));
            setHorizontalAlignment(0);
            setOpaque(true);
        }
    }


    public DynaDateChooser(Frame frame)
    {
        super(frame, "Date Chooser", true);
        calendarModel = null;
        calendarCellRenderer = null;
        mode = 0;
        mode2 = false;
        stringYear = System.getProperty("user.language").equals("ko") ? "\uB144" : "";
        stringMonth = System.getProperty("user.language").equals("ko") ? "\uC6D4" : "";
        calendar = null;
        today = new Date();
        calendarCache = new Calendar[42];
        sdf = new SimpleDateFormat("E");
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        chooser = this;
        calendarModel = new CalendarModel();
        calendarCellRenderer = new CalendarCellRenderer(this);
        initComponents();
        pack();
        centerTable.setSelectionMode(0);
        centerTable.setColumnSelectionAllowed(false);
        centerTable.getTableHeader().setReorderingAllowed(false);
        centerTable.getTableHeader().setResizingAllowed(false);
    }

    public DynaDateChooser(Dialog dialog)
    {
        super(dialog, "Date Chooser", true);
        calendarModel = null;
        calendarCellRenderer = null;
        mode = 0;
        mode2 = false;
        stringYear = System.getProperty("user.language").equals("ko") ? "\uB144" : "";
        stringMonth = System.getProperty("user.language").equals("ko") ? "\uC6D4" : "";
        calendar = null;
        today = new Date();
        calendarCache = new Calendar[42];
        sdf = new SimpleDateFormat("E");
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        chooser = this;
        calendarModel = new CalendarModel();
        calendarCellRenderer = new CalendarCellRenderer(this);
        initComponents();
        pack();
        centerTable.setSelectionMode(0);
        centerTable.setColumnSelectionAllowed(false);
        centerTable.getTableHeader().setReorderingAllowed(false);
        centerTable.getTableHeader().setResizingAllowed(false);
    }

    public static Date showDialog(Component comp, Object date)
    {
        if(chooser == null)
            chooser = new DynaDateChooser(JOptionPane.getFrameForComponent(comp));
        if(!chooser.isShowing())
        {
            chooser.setDate(date);
            chooser.updateButtons();
            UIUtils.setLocationRelativeTo(chooser, comp);
            chooser.setVisible(true);
            if(selectedCalendar != null)
                return selectedCalendar.getTime();
        }
        return null;
    }

    private void initComponents()
    {
        PanelNorth = new JPanel();
        leftButton = new JButton();
        yearLeftButton = new JButton();
        yearButton = new JButton();
        yearRightButton = new JButton();
        monthButton = new JButton();
        rightButton = new JButton();
        centerScrPane = UIFactory.createStrippedScrollPane(null);
        centerTable = new JTable();
        southPanel = new JPanel();
        todayButton = new JButton();
        nullButton = new JButton();
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(2);
        addFocusListener(this);
        addWindowListener(this);
        PanelNorth.setLayout(new BoxLayout(PanelNorth, 0));
        PanelNorth.setRequestFocusEnabled(false);
        PanelNorth.addMouseListener(this);
        PanelNorth.add(Box.createRigidArea(new Dimension(5, 0)));
        leftButton.setIcon(new ImageIcon("icons/SmallLeftArrow12.gif"));
        leftButton.setPreferredSize(new Dimension(17, 14));
        leftButton.setMaximumSize(new Dimension(17, 14));
        leftButton.setActionCommand("monthLeft");
        leftButton.setMargin(new Insets(0, 0, 0, 0));
        leftButton.setRequestFocusEnabled(false);
        leftButton.addActionListener(this);
        leftButton.addMouseListener(this);
        PanelNorth.add(leftButton);
        PanelNorth.add(Box.createGlue());
        yearLeftButton.setPreferredSize(new Dimension(13, 13));
        yearLeftButton.setMaximumSize(new Dimension(13, 13));
        yearLeftButton.setActionCommand("yearLeft");
        yearLeftButton.setText("<");
        yearLeftButton.setMargin(new Insets(0, 0, 0, 0));
        yearLeftButton.setRequestFocusEnabled(false);
        yearLeftButton.setVisible(false);
        yearLeftButton.addActionListener(this);
        PanelNorth.add(yearLeftButton);
        yearButton.setActionCommand("Year");
        yearButton.setFont(new Font("Dialog", 0, 11));
        yearButton.setText(String.valueOf(calendarModel.getYear()) + stringYear);
        yearButton.setMargin(new Insets(0, 0, 0, 0));
        yearButton.setBorderPainted(false);
        yearButton.setRequestFocusEnabled(false);
        yearButton.addActionListener(this);
        yearButton.addMouseListener(this);
        PanelNorth.add(yearButton);
        yearRightButton.setPreferredSize(new Dimension(13, 13));
        yearRightButton.setMaximumSize(new Dimension(13, 13));
        yearRightButton.setActionCommand("yearRight");
        yearRightButton.setText(">");
        yearRightButton.setMargin(new Insets(0, 0, 0, 0));
        yearRightButton.setRequestFocusEnabled(false);
        yearRightButton.setVisible(false);
        yearRightButton.addActionListener(this);
        PanelNorth.add(yearRightButton);
        monthButton.setActionCommand("month");
        monthButton.setFont(new Font("Dialog", 0, 11));
        monthButton.setText(String.valueOf(calendarModel.getMonth()) + stringMonth);
        monthButton.setMargin(new Insets(0, 0, 0, 0));
        monthButton.setContentAreaFilled(false);
        monthButton.setBorderPainted(false);
        monthButton.setRequestFocusEnabled(false);
        monthButton.addActionListener(this);
        monthButton.addMouseListener(this);
        PanelNorth.add(monthButton);
        PanelNorth.add(Box.createGlue());
        rightButton.setIcon(new ImageIcon("icons/SmallRightArrow12.gif"));
        rightButton.setPreferredSize(new Dimension(17, 14));
        rightButton.setMaximumSize(new Dimension(17, 14));
        rightButton.setActionCommand("monthRight");
        rightButton.setMargin(new Insets(0, 0, 0, 0));
        rightButton.setContentAreaFilled(false);
        rightButton.setRequestFocusEnabled(false);
        rightButton.addActionListener(this);
        rightButton.addMouseListener(this);
        PanelNorth.add(rightButton);
        getContentPane().add(PanelNorth, "North");
        PanelNorth.add(Box.createRigidArea(new Dimension(5, 0)));
        centerScrPane.setPreferredSize(new Dimension(150, 127));
        centerScrPane.setMinimumSize(new Dimension(150, 127));
        centerScrPane.setDoubleBuffered(true);
        centerScrPane.setMaximumSize(new Dimension(200, 200));
        centerTable.setPreferredSize(new Dimension(150, 102));
        centerTable.setCellSelectionEnabled(true);
        centerTable.setModel(calendarModel);
        centerTable.setShowVerticalLines(false);
        centerTable.setMaximumSize(new Dimension(150, 102));
        centerTable.setPreferredScrollableViewportSize(new Dimension(150, 102));
        centerTable.setShowHorizontalLines(false);
        centerTable.setRowSelectionAllowed(false);
        centerTable.setMinimumSize(new Dimension(150, 102));
        centerTable.setAutoscrolls(false);
        centerTable.setDefaultRenderer(java.util.Calendar.class, calendarCellRenderer);
        centerTable.addMouseListener(this);
        centerScrPane.setViewportView(centerTable);
        getContentPane().add(centerScrPane, "Center");
        southPanel.setLayout(new BoxLayout(southPanel, 0));
        southPanel.setRequestFocusEnabled(false);
        todayButton.setActionCommand("today");
        todayButton.setFont(smallFont);
        todayButton.setText(System.getProperty("user.language").equals("ko") ? "\uC624\uB298" : "Today");
        todayButton.setMargin(new Insets(0, 14, 0, 14));
        todayButton.setRequestFocusEnabled(false);
        todayButton.addActionListener(this);
        todayButton.addMouseListener(this);
        southPanel.add(todayButton);
        southPanel.add(Box.createGlue());
        nullButton.setActionCommand(null);
        nullButton.setFont(smallFont);
        nullButton.setText(System.getProperty("user.language").equals("ko") ? "\uC5C6\uC74C" : "Null");
        nullButton.setMargin(new Insets(0, 14, 0, 14));
        nullButton.setRequestFocusEnabled(false);
        nullButton.addActionListener(this);
        nullButton.addMouseListener(this);
        southPanel.add(nullButton);
        getContentPane().add(southPanel, "South");
    }

    private void removeAllListener()
    {
        removeMouseListener(this);
        removeFocusListener(this);
        leftButton.removeActionListener(this);
        yearLeftButton.removeActionListener(this);
        yearButton.removeActionListener(this);
        yearRightButton.removeActionListener(this);
        monthButton.removeActionListener(this);
        rightButton.removeActionListener(this);
        todayButton.removeActionListener(this);
        nullButton.removeActionListener(this);
    }

    private void formFocusGained(FocusEvent evt)
    {
        centerTable.requestFocus();
    }

    private void NullButtonActionPerformed(ActionEvent evt)
    {
        if(getMode2())
        {
            return;
        } else
        {
            selectedCalendar = null;
            setVisible(false);
            removeAllListener();
            return;
        }
    }

    private void NullButtonMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void TableCenterMouseClicked(MouseEvent evt)
    {
        if(getMode2())
            return;
        if(evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt))
        {
            selectedCalendar = (Calendar)chooser.centerTable.getValueAt(chooser.centerTable.getSelectedRow(), chooser.centerTable.getSelectedColumn());
            setVisible(false);
            return;
        } else
        {
            return;
        }
    }

    private void TodayButtonMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void TodayButtonActionPerformed(ActionEvent evt)
    {
        if(getMode2())
        {
            return;
        } else
        {
            selectedCalendar = Calendar.getInstance();
            setVisible(false);
            return;
        }
    }

    private void YearRightButtonActionPerformed(ActionEvent evt)
    {
        calendarModel.addYear(1);
        updateButtons();
    }

    private void YearLeftButtonActionPerformed(ActionEvent evt)
    {
        calendarModel.addYear(-1);
        updateButtons();
    }

    private void PanelNorthMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
        {
            getMode2();
            return;
        } else
        {
            return;
        }
    }

    private void LeftButtonMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void YearButtonMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void MonthButtonMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void RightButtonMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void TableCenterMousePressed(MouseEvent evt)
    {
        if(getMode() == 1)
            return;
        else
            return;
    }

    private void MonthButtonActionPerformed(ActionEvent evt)
    {
        if(getMode2())
            return;
        else
            return;
    }

    private void YearButtonActionPerformed(ActionEvent evt)
    {
        if(getMode2())
            return;
        if(mode == 0)
        {
            yearLeftButton.setVisible(true);
            yearRightButton.setVisible(true);
            mode = 1;
            getRootPane().updateUI();
        }
    }

    private void RightButtonActionPerformed(ActionEvent evt)
    {
        if(getMode2())
        {
            return;
        } else
        {
            calendarModel.addMonth(1);
            updateButtons();
            return;
        }
    }

    private void LeftButtonActionPerformed(ActionEvent evt)
    {
        if(getMode2())
        {
            return;
        } else
        {
            calendarModel.addMonth(-1);
            updateButtons();
            return;
        }
    }

    protected int getMode()
    {
        if(mode == 1)
        {
            yearLeftButton.setVisible(false);
            yearRightButton.setVisible(false);
            mode = 0;
            mode2 = true;
            getRootPane().updateUI();
            return 1;
        } else
        {
            return mode;
        }
    }

    protected boolean getMode2()
    {
        if(mode2)
        {
            mode2 = false;
            return true;
        } else
        {
            return false;
        }
    }

    public void setDate(Object date)
    {
        if(date instanceof String)
            calendarModel.setDate((String)date);
        if(date instanceof Date)
            calendarModel.setDate((Date)date);
    }

    public void updateButtons()
    {
        yearButton.setText(String.valueOf(calendarModel.getYear()) + stringYear);
        monthButton.setText(String.valueOf(calendarModel.getMonth()) + stringMonth);
        pack();
    }

    private void MenuItemActionPerformed(ActionEvent evt)
    {
        if(getMode2())
        {
            return;
        } else
        {
            calendarModel.setMonth(Integer.parseInt(evt.getActionCommand()));
            updateButtons();
            return;
        }
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            NullButtonActionPerformed(e);
        else
        if(command.equals("monthLeft"))
            LeftButtonActionPerformed(e);
        else
        if(command.equals("yearLeft"))
            YearLeftButtonActionPerformed(e);
        else
        if(command.equals("Year"))
            YearButtonActionPerformed(e);
        else
        if(command.equals("yearRight"))
            YearRightButtonActionPerformed(e);
        else
        if(command.equals("month"))
            MonthButtonActionPerformed(e);
        else
        if(command.equals("monthRight"))
            RightButtonActionPerformed(e);
        else
        if(command.equals("today"))
            TodayButtonActionPerformed(e);
        else
            NullButtonActionPerformed(e);
    }

    public void mousePressed(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(leftButton))
            LeftButtonMousePressed(e);
        else
        if(source.equals(yearButton))
            YearButtonMousePressed(e);
        else
        if(source.equals(monthButton))
            MonthButtonMousePressed(e);
        else
        if(source.equals(rightButton))
            RightButtonMousePressed(e);
        else
        if(source.equals(todayButton))
            TodayButtonMousePressed(e);
        else
        if(source.equals(nullButton))
            NullButtonMousePressed(e);
        else
            TableCenterMousePressed(e);
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
        Object source = e.getSource();
        if(source.equals(centerTable))
            TableCenterMouseClicked(e);
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void focusLost(FocusEvent focusevent)
    {
    }

    public void focusGained(FocusEvent e)
    {
        formFocusGained(e);
    }

    public void windowClosing(WindowEvent windowEvent)
    {
        selectedCalendar = null;
        setVisible(false);
        removeAllListener();
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
        selectedCalendar = null;
        setVisible(false);
        removeAllListener();
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    static final int MODE_DEFAULT = 0;
    static final int MODE_YEAR_SELECT = 1;
    static final int MODE_MONTH_SELECT = 2;
    private static DynaDateChooser chooser = null;
    protected CalendarModel calendarModel;
    protected CalendarCellRenderer calendarCellRenderer;
    protected static Calendar selectedCalendar = null;
    protected int mode;
    protected boolean mode2;
    protected final String stringYear;
    protected final String stringMonth;
    private static final Font smallFont = new Font("dialog", 0, 11);
    private Calendar calendar;
    private Date today;
    private Calendar calendarCache[];
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdf2;
    private JPanel PanelNorth;
    private JButton leftButton;
    private JButton yearLeftButton;
    private JButton yearButton;
    private JButton yearRightButton;
    private JButton monthButton;
    private JButton rightButton;
    private JScrollPane centerScrPane;
    private JTable centerTable;
    private JPanel southPanel;
    private JButton todayButton;
    private JButton nullButton;









}
