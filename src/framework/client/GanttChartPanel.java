// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GanttChartPanel.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.util.Session;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, UIGeneration, ChartHeader

public class GanttChartPanel extends JPanel
{
    class ListenerCommon extends FocusAdapter
        implements ActionListener, MouseListener
    {

        public void focusGained(FocusEvent focusevent)
        {
        }

        public void mousePressed(MouseEvent e)
        {
            boolean clickBar = false;
            if(e.getClickCount() == 2)
                try
                {
                    GanttChartBar gantt = null;
                    for(int i = gcbList.size() - 1; i > 0; i--)
                    {
                        gantt = (GanttChartBar)gcbList.get(i);
                        clickBar = gantt.contains(e.getPoint());
                        String ouid = gantt.ouid;
                        DefaultMutableTreeNode selectedNode = gantt.tmpSelectedNode;
                        sessionProperty = gantt.sessionProperty;
                        if(!clickBar)
                            continue;
                        String selectClassOuid = DynaMOAD.dos.getClassOuid(ouid);
                        UIGeneration tmpUIGeneration = (UIGeneration)UIGeneration._session.getProperty(sessionProperty);
                        UIGeneration uiGeneration = new UIGeneration(searchResultPanel, selectClassOuid, ouid, 1, selectedNode, tmpUIGeneration, sessionProperty);
                        uiGeneration.setVisible(true);
                        break;
                    }

                }
                catch(IIPRequestException e1)
                {
                    System.out.println("GanttChartPanel$ListenerCommon.mousePressed()'s error : " + e1);
                    e1.printStackTrace();
                }
        }

        public void mouseClicked(MouseEvent mouseevent)
        {
        }

        public void mouseEntered(MouseEvent e)
        {
            boolean clickBar = false;
            GanttChartBar gantt = null;
            for(int i = gcbList.size() - 1; i > 0; i--)
            {
                gantt = (GanttChartBar)gcbList.get(i);
                clickBar = gantt.contains(e.getPoint());
                String ouid = gantt.ouid;
                if(clickBar)
                    break;
            }

        }

        public void mouseExited(MouseEvent mouseevent)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
            if(SwingUtilities.isRightMouseButton(e) && !e.isControlDown() && !e.isAltDown() && !e.isShiftDown())
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            if(command.equals("monthLeft"))
                leftButtonActionPerformed(e);
            else
            if(command.equals("monthRight"))
                rightButtonActionPerformed(e);
            else
            if(command.equals("zoom.reset"))
                zoomresetActionPerformed(e);
            else
            if(command.equals("zoom.in"))
                zoominActionPerformed(e);
            else
            if(command.equals("zoom.out"))
                zoomoutActionPerformed(e);
        }

        private String sessionProperty;

        ListenerCommon()
        {
        }
    }

    class CalendarBaseAxis
    {

        public void setCalendarBaseAxis(int year, int month)
        {
            sCalendar.set(1, year);
            sCalendar.set(2, month - 1);
            sCalendar.set(5, 1);
        }

        public void addMonth(int month)
        {
            sCalendar.add(2, month);
        }

        public void reSize(float rate)
        {
            STICK_INTERVAL = Math.round(15F * rate);
            if(STICK_INTERVAL == 0)
                STICK_INTERVAL = 1;
        }

        public void reSet()
        {
            STICK_INTERVAL = 15;
        }

        public int getSticInterval()
        {
            return STICK_INTERVAL;
        }

        public int getAxisYLoc()
        {
            return 40;
        }

        public String getStartDate()
        {
            StringBuffer result = (new StringBuffer()).append(String.valueOf(sCalendar.get(1)));
            if(String.valueOf(sCalendar.get(2) + 1).length() == 1)
                result.append("0").append(String.valueOf(sCalendar.get(2) + 1));
            else
                result.append(String.valueOf(sCalendar.get(2) + 1));
            if(String.valueOf(sCalendar.get(5)).length() == 1)
                result.append("0").append(String.valueOf(sCalendar.get(5)));
            else
                result.append(String.valueOf(sCalendar.get(5)));
            return new String(result);
        }

        public int getNumberOfDateForAxis()
        {
            int result = 0;
            Calendar tCalendar = Calendar.getInstance();
            tCalendar.set(sCalendar.get(1), sCalendar.get(2), 1);
            for(int i = 0; i < MONTH_INTERVAL; i++)
            {
                tCalendar.add(2, 1);
                tCalendar.add(5, -1);
                result += tCalendar.get(5);
                tCalendar.set(5, 1);
                tCalendar.add(2, 1);
            }

            return result;
        }

        public int getNumberOfDate(String fromDate, String toDate)
        {
            int dateSum = 0;
            if(fromDate.substring(0, 6).equals(toDate.substring(0, 6)))
            {
                dateSum = (Integer.parseInt(toDate.substring(6, 8)) - Integer.parseInt(fromDate.substring(6, 8))) + 1;
            } else
            {
                Calendar tCalendar1 = Calendar.getInstance();
                Calendar tCalendar2 = Calendar.getInstance();
                dateSum = (getLastDateOfMonth(Integer.parseInt(fromDate.substring(0, 4)), Integer.parseInt(fromDate.substring(4, 6))) - Integer.parseInt(fromDate.substring(6, 8))) + 1;
                tCalendar1.set(Integer.parseInt(fromDate.substring(0, 4)), (Integer.parseInt(fromDate.substring(4, 6)) + 1) - 1, 1);
                tCalendar2.set(Integer.parseInt(toDate.substring(0, 4)), Integer.parseInt(toDate.substring(4, 6)) - 1, 1);
                for(; tCalendar1.before(tCalendar2); tCalendar1.add(2, 1))
                    dateSum += getLastDateOfMonth(tCalendar1.get(1), tCalendar1.get(2) + 1);

                dateSum += Integer.parseInt(toDate.substring(6, 8));
            }
            return dateSum;
        }

        public int getLastDateOfMonth(int year, int month)
        {
            Calendar tCalendar = Calendar.getInstance();
            tCalendar.set(year, month - 1, 1);
            tCalendar.add(2, 1);
            tCalendar.add(5, -1);
            return tCalendar.get(5);
        }

        public void drawCalendarBaseAxis(Graphics g, int width, int height)
        {
            int tmpX = 0;
            Graphics2D g2 = (Graphics2D)g;
            cCalendar = null;
            cCalendar = (Calendar)sCalendar.clone();
            g2.setColor(new Color(200, 150, 150));
            g2.fillRect(0, 0, 0 + width, 40);
            g2.setColor(Color.black);
            g2.drawLine(0, 40, width + 0, 40);
            while(tmpX < width) 
            {
                if(cCalendar.get(5) == 15)
                {
                    String ymStr = cCalendar.get(1) + "-" + (cCalendar.get(2) + 1);
                    g2.setFont(new Font("SansSerif", 1, 15));
                    g2.drawString(ymStr, tmpX - 5, 15);
                }
                String dStr = String.valueOf(cCalendar.get(5));
                g2.setFont(new Font("SansSerif", 0, 10));
                int currDay = cCalendar.get(7);
                if(currDay == 7 || currDay == 1)
                {
                    g2.setColor(new Color(222, 222, 222));
                    g2.fillRect(tmpX, 41, tmpX + 20, height);
                } else
                {
                    g2.setColor(UIManagement.panelBackGround);
                    g2.fillRect(tmpX, 41, tmpX, height);
                }
                if(currDay == 7)
                    g2.setColor(Color.blue);
                else
                if(currDay == 1)
                    g2.setColor(Color.red);
                else
                    g2.setColor(Color.black);
                if(cCalendar.get(5) % 5 == 0)
                    if(dStr.length() == 1)
                        g2.drawString(dStr, (tmpX + STICK_INTERVAL / 2) - 1, 30);
                    else
                        g2.drawString(dStr, (tmpX + STICK_INTERVAL / 2) - 5, 30);
                g2.setColor(Color.black);
                if(cCalendar.get(1) != sCalendar.get(1) || cCalendar.get(2) != sCalendar.get(2) || cCalendar.get(5) != sCalendar.get(5))
                    if(cCalendar.get(5) == 1)
                    {
                        g2.setColor(Color.black);
                        g2.drawLine(tmpX, 0, tmpX, height);
                    } else
                    {
                        g2.setColor(Color.black);
                        g2.drawLine(tmpX, 35, tmpX, 40);
                        g2.setColor(new Color(207, 207, 207));
                        g2.drawLine(tmpX, 40, tmpX, height);
                    }
                tmpX += STICK_INTERVAL;
                cCalendar.add(5, 1);
            }
        }

        public String removeChar(String str, int ch)
        {
            StringBuffer sb = new StringBuffer();
            String tmpStr = null;
            for(int idx = -1; (idx = str.indexOf(ch)) > 0;)
            {
                sb.append(str.substring(0, idx));
                tmpStr = str.substring(idx + 1, str.length());
                str = tmpStr;
            }

            sb.append(str);
            return new String(sb);
        }

        private int MONTH_INTERVAL;
        private final int STICK_SIZE = 5;
        private final int DEFAULT_STICK_INTERVAL = 15;
        private int STICK_INTERVAL;
        private final int AXIS_XLOC = 0;
        private final int AXIS_YLOC = 40;
        private Calendar sCalendar;
        private Calendar cCalendar;

        public CalendarBaseAxis()
        {
            MONTH_INTERVAL = 6;
            STICK_INTERVAL = 15;
            sCalendar = Calendar.getInstance();
            sCalendar.set(5, 1);
            cCalendar = (Calendar)sCalendar.clone();
        }
    }

    class WeightBaseAxis
    {

        public void drawWeightBaseAxis(Graphics g1)
        {
        }

        public WeightBaseAxis()
        {
        }
    }

    public class GanttChartBar
    {

        public void drawBar(Graphics g)
        {
            Graphics2D g2 = (Graphics2D)g;
            if(width > 0)
            {
                g2.setColor(barColor);
                g2.fillRect(x, y, width, height);
                g2.setColor(Color.black);
                g2.drawRect(x, y, width, height);
            }
            g2 = null;
        }

        public boolean contains(Point p)
        {
            if(r == null)
            {
                return false;
            } else
            {
                boolean contain = false;
                contain = r.contains(p);
                return contain;
            }
        }

        private String sessionProperty;
        private DefaultMutableTreeNode tmpSelectedNode;
        private int x;
        private int y;
        private int width;
        private int height;
        private int mode;
        private Color barColor;
        private String ouid;
        private Rectangle r;




        public GanttChartBar(int x, int y, int width, int height, int mode, Color color, 
                String selectOuid, DefaultMutableTreeNode selectedNode, String sessionProperty)
        {
            r = null;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.mode = mode;
            switch(this.mode)
            {
            case 1: // '\001'
                barColor = new Color(192, 202, 228);
                break;

            case 2: // '\002'
                barColor = new Color(222, 229, 239);
                break;

            case 3: // '\003'
                barColor = Color.green;
                break;

            case 4: // '\004'
                barColor = Color.red;
                break;

            case 5: // '\005'
                barColor = Color.lightGray;
                break;

            case 6: // '\006'
                barColor = new Color(113, 149, 146);
                break;

            case 7: // '\007'
                barColor = new Color(156, 106, 106);
                break;

            case 8: // '\b'
                barColor = Color.blue;
                break;

            case 9: // '\t'
                barColor = new Color(222, 229, 239);
                break;

            default:
                if(color == null)
                    barColor = DEFAULT_BAR_COLOR;
                else
                    barColor = color;
                break;
            }
            ouid = selectOuid;
            tmpSelectedNode = selectedNode;
            this.sessionProperty = sessionProperty;
            r = new Rectangle(this.x, this.y, this.width, this.height);
        }
    }


    public GanttChartPanel(Object parent, int option, ArrayList data, int barCntPerRow, int rowCnt)
    {
        DEFAULT_BAR_COLOR = Color.green;
        BAR_HEIGHT = 7;
        BAR_INTERVAL = 5;
        leftButton1 = new JButton();
        rightButton1 = new JButton();
        leftButton2 = new JButton();
        rightButton2 = new JButton();
        scale = 1.0F;
        intYear = 1;
        intMonth = 2;
        parentWindow = parent;
        chartOption = option;
        size = new Dimension(5000, 5000);
        listenerCommon = new ListenerCommon();
        popupMenu = new JPopupMenu();
        zoomMenu = new JMenu();
        zoomMenu.setText(DynaMOAD.getMSRString("WRD_0133", "Zoom", 3));
        zoomMenu.setIcon(new ImageIcon("icons/Zoom16.gif"));
        zoomMenu.setFont(getFont());
        popupMenu.add(zoomMenu);
        zoomResetMenuItem = new JMenuItem();
        zoomResetMenuItem.setActionCommand("zoom.reset");
        zoomResetMenuItem.setText(DynaMOAD.getMSRString("WRD_0136", "Zoom Reset", 3));
        zoomResetMenuItem.setIcon(new ImageIcon("icons/Blank.gif"));
        zoomResetMenuItem.setFont(getFont());
        zoomResetMenuItem.addActionListener(listenerCommon);
        zoomMenu.add(zoomResetMenuItem);
        zoomMenu.add(new JSeparator());
        zoomInMenuItem = new JMenuItem();
        zoomInMenuItem.setActionCommand("zoom.in");
        zoomInMenuItem.setText(DynaMOAD.getMSRString("WRD_0134", "Zoom In", 3));
        zoomInMenuItem.setIcon(new ImageIcon("icons/ZoomIn16.gif"));
        zoomInMenuItem.setFont(getFont());
        zoomInMenuItem.addActionListener(listenerCommon);
        zoomMenu.add(zoomInMenuItem);
        zoomOutMenuItem = new JMenuItem();
        zoomOutMenuItem.setActionCommand("zoom.out");
        zoomOutMenuItem.setText(DynaMOAD.getMSRString("WRD_0135", "Zoom Out", 3));
        zoomOutMenuItem.setIcon(new ImageIcon("icons/ZoomOut16.gif"));
        zoomOutMenuItem.setFont(getFont());
        zoomOutMenuItem.addActionListener(listenerCommon);
        zoomMenu.add(zoomOutMenuItem);
        if(chartOption == 0)
        {
            calendarBaseAxis = new CalendarBaseAxis();
            calendarBaseAxis.setCalendarBaseAxis(intYear, intMonth);
            int width = calendarBaseAxis.getSticInterval() * calendarBaseAxis.getNumberOfDateForAxis();
            size = new Dimension(width, 5000);
            leftButton1.setIcon(new ImageIcon("icons/LeftArrow16.gif"));
            leftButton1.setPreferredSize(new Dimension(17, 17));
            leftButton1.setMaximumSize(new Dimension(17, 17));
            leftButton1.setActionCommand("monthLeft");
            leftButton1.setMargin(new Insets(0, 0, 0, 0));
            leftButton1.setContentAreaFilled(false);
            leftButton1.setRequestFocusEnabled(false);
            leftButton1.addActionListener(listenerCommon);
            leftButton1.addMouseListener(listenerCommon);
            rightButton1.setIcon(new ImageIcon("icons/RightArrow16.gif"));
            rightButton1.setPreferredSize(new Dimension(17, 17));
            rightButton1.setMaximumSize(new Dimension(17, 17));
            rightButton1.setActionCommand("monthRight");
            rightButton1.setMargin(new Insets(0, 0, 0, 0));
            rightButton1.setContentAreaFilled(false);
            rightButton1.setRequestFocusEnabled(false);
            rightButton1.addActionListener(listenerCommon);
            rightButton1.addMouseListener(listenerCommon);
            leftButton2.setIcon(new ImageIcon("icons/LeftArrow16.gif"));
            leftButton2.setPreferredSize(new Dimension(17, 17));
            leftButton2.setMaximumSize(new Dimension(17, 17));
            leftButton2.setActionCommand("monthLeft");
            leftButton2.setMargin(new Insets(0, 0, 0, 0));
            leftButton2.setContentAreaFilled(false);
            leftButton2.setRequestFocusEnabled(false);
            leftButton2.addActionListener(listenerCommon);
            leftButton2.addMouseListener(listenerCommon);
            rightButton2.setIcon(new ImageIcon("icons/RightArrow16.gif"));
            rightButton2.setPreferredSize(new Dimension(17, 17));
            rightButton2.setMaximumSize(new Dimension(17, 17));
            rightButton2.setActionCommand("monthRight");
            rightButton2.setMargin(new Insets(0, 0, 0, 0));
            rightButton2.setContentAreaFilled(false);
            rightButton2.setRequestFocusEnabled(false);
            rightButton2.addActionListener(listenerCommon);
            rightButton2.addMouseListener(listenerCommon);
            add(leftButton1);
            add(rightButton1);
            add(Box.createRigidArea(new Dimension(width - 100, 0)));
            add(leftButton2);
            add(rightButton2);
        }
        setOpaque(true);
        setBackground(UIManagement.panelBackGround);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(new Dimension(10000, 10000));
        addMouseListener(listenerCommon);
        setData(data, barCntPerRow, rowCnt, intYear, intMonth);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(chartOption == 0)
        {
            calendarBaseAxis.drawCalendarBaseAxis(g, getWidth(), getHeight());
            for(int i = 0; i < gcbList.size(); i++)
                ((GanttChartBar)gcbList.get(i)).drawBar(g);

        }
    }

    public void setData(ArrayList data, int barCntPerRow, int rowCnt, int iYear, int iMonth)
    {
        if(data == null || data.size() == 0)
        {
            return;
        } else
        {
            this.barCntPerRow = barCntPerRow;
            this.rowCnt = rowCnt;
            intYear = iYear;
            intMonth = iMonth;
            originalData = data;
            calendarBaseAxis.setCalendarBaseAxis(intYear, intMonth);
            converseData();
            return;
        }
    }

    public void converseData()
    {
        try
        {
            gcbList = null;
            gcbList = new ArrayList();
            if(chartOption == 0)
            {
                for(int i = 0; i < rowCnt; i++)
                {
                    ArrayList barList = (ArrayList)originalData.get(i);
                    for(int j = 0; j < barCntPerRow; j++)
                    {
                        ArrayList barData = (ArrayList)barList.get(j);
                        String beginDate = null;
                        String endDate = null;
                        int tmpX = 0;
                        int tmpY = 0;
                        int tmpWidth = 0;
                        int tmpHeight = 0;
                        int tmpMode = 0;
                        Color tmpColor = null;
                        String ouid = null;
                        DefaultMutableTreeNode selectedNode = null;
                        if(!Utils.isNullString((String)barData.get(0)))
                        {
                            beginDate = calendarBaseAxis.removeChar((String)barData.get(0), 45);
                            if(barData.get(1) == null || ((String)barData.get(1)).equals(""))
                            {
                                Calendar tmpCalendar = Calendar.getInstance();
                                StringBuffer tmpBuffer = (new StringBuffer()).append(String.valueOf(tmpCalendar.get(1)));
                                if(String.valueOf(tmpCalendar.get(2) + 1).length() == 1)
                                    tmpBuffer.append("0").append(String.valueOf(tmpCalendar.get(2) + 1));
                                else
                                    tmpBuffer.append(String.valueOf(tmpCalendar.get(2) + 1));
                                if(String.valueOf(tmpCalendar.get(5)).length() == 1)
                                    tmpBuffer.append("0").append(String.valueOf(tmpCalendar.get(5)));
                                else
                                    tmpBuffer.append(String.valueOf(tmpCalendar.get(5)));
                                endDate = new String(tmpBuffer);
                            } else
                            {
                                endDate = calendarBaseAxis.removeChar((String)barData.get(1), 45);
                            }
                            tmpMode = ((Integer)barData.get(2)).intValue();
                            if(barData.get(3) == null)
                                tmpColor = DEFAULT_BAR_COLOR;
                            else
                                tmpColor = (Color)barData.get(3);
                            String basicDate = calendarBaseAxis.getStartDate();
                            int unitWidth = calendarBaseAxis.getSticInterval();
                            if(endDate.compareTo(basicDate) < 0)
                            {
                                tmpX = 0;
                                tmpWidth = 0;
                            } else
                            if(beginDate.compareTo(basicDate) < 0 && endDate.compareTo(basicDate) >= 0)
                            {
                                tmpX = 0;
                                tmpWidth = calendarBaseAxis.getNumberOfDate(basicDate, endDate) * unitWidth;
                            } else
                            if(beginDate.compareTo(basicDate) >= 0)
                            {
                                tmpX = (calendarBaseAxis.getNumberOfDate(basicDate, beginDate) - 1) * unitWidth;
                                tmpWidth = calendarBaseAxis.getNumberOfDate(beginDate, endDate) * unitWidth;
                            }
                            tmpY = calendarBaseAxis.getAxisYLoc() + getRowHeight() * i + BAR_INTERVAL + BAR_INTERVAL * j;
                            tmpHeight = BAR_HEIGHT;
                            ouid = (String)barData.get(4);
                            selectedNode = (DefaultMutableTreeNode)barData.get(5);
                            sessionProperty = (String)barData.get(6);
                        } else
                        if(barData.get(3) == null)
                            tmpColor = DEFAULT_BAR_COLOR;
                        else
                            tmpColor = (Color)barData.get(3);
                        GanttChartBar tmpgcb = new GanttChartBar(tmpX, tmpY, tmpWidth, tmpHeight, tmpMode, tmpColor, ouid, selectedNode, sessionProperty);
                        gcbList.add(tmpgcb);
                    }

                }

            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(getRootPane(), "\uB0A0\uC9DC\uD615\uC2DD\uC5D0 \uC5B4\uAE0B\uB098\uB294 \uB370\uC774\uD0C0\uAC00 \uC874\uC7AC\uD569\uB2C8\uB2E4.", "\uC624\uB958", 0);
            System.out.println("GanttChartPanel's converseData() : " + e);
            return;
        }
    }

    public ChartHeader getChartHeader()
    {
        return chartHeader;
    }

    public int getHeaderHeight()
    {
        int height = 21;
        if(chartOption == 0)
            height = calendarBaseAxis.getAxisYLoc();
        return height;
    }

    public int getRowHeight()
    {
        int height = 21;
        if(chartOption == 0)
            height = BAR_INTERVAL + BAR_HEIGHT + BAR_INTERVAL * (barCntPerRow - 1) + BAR_INTERVAL;
        return height;
    }

    public void reSize(float rate)
    {
        BAR_INTERVAL = getClass().round(5F * rate);
        if(BAR_INTERVAL == 0)
            BAR_INTERVAL = 1;
    }

    public void reSet()
    {
        getClass();
        BAR_HEIGHT = 7;
        getClass();
        BAR_INTERVAL = 5;
    }

    private void leftButtonActionPerformed(ActionEvent e)
    {
        calendarBaseAxis.addMonth(-1);
        converseData();
        paintComponent(getGraphics());
        updateUI();
    }

    private void rightButtonActionPerformed(ActionEvent e)
    {
        calendarBaseAxis.addMonth(1);
        converseData();
        paintComponent(getGraphics());
        updateUI();
    }

    private void zoomresetActionPerformed(ActionEvent e)
    {
        scale = 1.0F;
        calendarBaseAxis.reSet();
        reSet();
        converseData();
        updateUI();
        ((UIGeneration)parentWindow).resizeTreeRowHeight();
    }

    private void zoominActionPerformed(ActionEvent e)
    {
        scale = scale + 0.2F;
        calendarBaseAxis.reSize(scale);
        reSize(scale);
        converseData();
        updateUI();
        ((UIGeneration)parentWindow).resizeTreeRowHeight();
    }

    private void zoomoutActionPerformed(ActionEvent e)
    {
        if(scale < 0.5F)
        {
            return;
        } else
        {
            scale = scale - 0.2F;
            calendarBaseAxis.reSize(scale);
            reSize(scale);
            converseData();
            updateUI();
            ((UIGeneration)parentWindow).resizeTreeRowHeight();
            return;
        }
    }

    private String sessionProperty;
    private ChartHeader chartHeader;
    public static final int DATE_BASE = 0;
    public static final int WEIGHT_BASE = 1;
    private final Color DEFAULT_BAR_COLOR;
    private final int DEFAULT_BAR_HEIGHT = 7;
    private final int DEFAULT_BAR_INTERVAL = 5;
    private int BAR_HEIGHT;
    private int BAR_INTERVAL;
    private int chartOption;
    private int barCntPerRow;
    private int rowCnt;
    private ArrayList originalData;
    private ArrayList gcbList;
    private Dimension size;
    private JButton leftButton1;
    private JButton rightButton1;
    private JButton leftButton2;
    private JButton rightButton2;
    private ListenerCommon listenerCommon;
    private CalendarBaseAxis calendarBaseAxis;
    private WeightBaseAxis weightBaseAxis;
    private JPopupMenu popupMenu;
    private JMenu zoomMenu;
    private JMenuItem zoomResetMenuItem;
    private JMenuItem zoomInMenuItem;
    private JMenuItem zoomOutMenuItem;
    private float scale;
    private int intYear;
    private int intMonth;
    private Object parentWindow;
    private DOS dos;
    final int MODIFY_MODE = 1;
    private JPanel searchResultPanel;









}