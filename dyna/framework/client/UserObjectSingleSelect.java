// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserObjectSingleSelect.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.uic.Table;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.JTableHeader;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, AuthoritySearch

public class UserObjectSingleSelect extends JDialog
{
    class ListenerCommon extends WindowAdapter
        implements ActionListener, MouseListener, KeyListener
    {

        public void windowClosing(WindowEvent e)
        {
            this_windowClosing(e);
        }

        public void windowOpened(WindowEvent e)
        {
            this_windowOpened(e);
        }

        public void mouseClicked(MouseEvent evt)
        {
            if(evt.getSource().equals(wholeTable.getTable()))
                wholeTable_mouseClicked(evt);
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

        public void actionPerformed(ActionEvent evt)
        {
            String command = evt.getActionCommand();
            if(command.equals("Clear"))
                bntsClear_actionPerformed(evt);
            else
            if(command.equals("Search"))
                bntsSearch_actionPerformed(evt);
            else
            if(command.equals("Ok"))
                bntsOk_actionPerformed(evt);
            else
            if(command.equals("Exit"))
                bntsCancel_actionPerformed(evt);
        }

        public void keyReleased(KeyEvent keyevent)
        {
        }

        public void keyTyped(KeyEvent keyevent)
        {
        }

        public void keyPressed(KeyEvent keyevent)
        {
        }

        ListenerCommon()
        {
        }
    }


    public UserObjectSingleSelect(Frame dialog, Object parent, String title, boolean modal, int t)
    {
        super(dialog, title, modal);
        handCursor = new Cursor(12);
        newUI = DynaMOAD.newUI;
        aus = DynaMOAD.aus;
        buttonToolBar = new JToolBar();
        bntsCancel = new JButton();
        bntsOk = new JButton();
        bntsClear = new JButton();
        bntsSearch = new JButton();
        pnUserObjectSingleSelect = new JPanel();
        lbtsWholeTeamList = new JLabel(DynaMOAD.getMSRString("WRD_0108", "Search List", 3));
        sptsWholeTeamTable = new JScrollPane();
        wholeTable = null;
        panelNameCondition = new JPanel();
        panelCondition = new JPanel();
        setTitle(title);
        setModal(modal);
        try
        {
            setWindowLocation(parent);
            parentWindow = parent;
            flag = t;
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setWindowLocation(Object parent)
    {
        Point pos = null;
        if(parent instanceof Frame)
            pos = ((Frame)parent).getLocation();
        else
            pos = new Point(100, 100);
        pos.x += 50;
        pos.y += 50;
        setLocation(pos);
    }

    void jbInit()
        throws Exception
    {
        listenerCommon = new ListenerCommon();
        addWindowListener(listenerCommon);
        buttonToolBar.setBorder(BorderFactory.createEtchedBorder());
        buttonToolBar.setLayout(new BoxLayout(buttonToolBar, 0));
        bntsCancel.setActionCommand("Exit");
        bntsCancel.setToolTipText(DynaMOAD.getMSRString("WRD_0012", "close", 3));
        bntsCancel.setIcon(new ImageIcon(getClass().getResource("/icons/Exit.gif")));
        bntsCancel.setMargin(new Insets(0, 0, 0, 0));
        bntsCancel.addActionListener(listenerCommon);
        bntsOk.setActionCommand("Ok");
        bntsOk.setToolTipText(DynaMOAD.getMSRString("WRD_0018", "Confirm", 3));
        bntsOk.setIcon(new ImageIcon(getClass().getResource("/icons/Ok.gif")));
        bntsOk.setMargin(new Insets(0, 0, 0, 0));
        bntsOk.addActionListener(listenerCommon);
        bntsClear.setActionCommand("Clear");
        bntsClear.setToolTipText(DynaMOAD.getMSRString("WRD_0016", "clear", 3));
        bntsClear.setIcon(new ImageIcon(getClass().getResource("/icons/Clear.gif")));
        bntsClear.setMargin(new Insets(0, 0, 0, 0));
        bntsClear.addActionListener(listenerCommon);
        bntsSearch.setActionCommand("Search");
        bntsSearch.setToolTipText(DynaMOAD.getMSRString("WRD_0014", "search", 3));
        bntsSearch.setIcon(new ImageIcon(getClass().getResource("/icons/Search.gif")));
        bntsSearch.setMargin(new Insets(0, 0, 0, 0));
        bntsSearch.addActionListener(listenerCommon);
        buttonToolBar.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonToolBar.add(panelNameCondition);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(bntsSearch);
        buttonToolBar.add(bntsClear);
        buttonToolBar.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonToolBar.add(bntsOk);
        buttonToolBar.add(bntsCancel);
        buttonToolBar.add(Box.createRigidArea(new Dimension(10, 0)));
        getContentPane().add(buttonToolBar, "North");
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        pnUserObjectSingleSelect.setLayout(gridBag);
        pnUserObjectSingleSelect.setBorder(BorderFactory.createEtchedBorder());
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.ipady = 25;
        gridBag.setConstraints(lbtsWholeTeamList, gridBagCon);
        gridBagCon.ipady = 0;
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 5;
        gridBag.setConstraints(sptsWholeTeamTable, gridBagCon);
        JLabel imsi = new JLabel("");
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(imsi, gridBagCon);
        JLabel imsi2 = new JLabel("");
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(imsi2, gridBagCon);
        pnUserObjectSingleSelect.add(lbtsWholeTeamList);
        pnUserObjectSingleSelect.add(sptsWholeTeamTable);
        pnUserObjectSingleSelect.add(imsi);
        pnUserObjectSingleSelect.add(imsi2);
        getContentPane().add(pnUserObjectSingleSelect, "Center");
        wholeDataFun();
        wholeTable = new Table(wholedata, wholecolumn, wholecolumnWidth, 0);
        if(flag == 0 || flag == 1)
            wholeTable.setColumnSequence(new int[] {
                0, 2
            });
        else
        if(flag == 2)
            wholeTable.setColumnSequence(new int[] {
                0, 8, 2
            });
        wholeTable.setIndexColumn(0);
        wholeTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        wholeTable.getTable().setCursor(handCursor);
        wholeTable.getTable().addMouseListener(listenerCommon);
        sptsWholeTeamTable.setViewportView(wholeTable.getTable());
        sptsWholeTeamTable.getViewport().setBackground(UIManagement.treeLevelOneColor);
        sptsWholeTeamTable.setPreferredSize(new Dimension(100, 150));
        setSize(300, 300);
    }

    void this_windowOpened(WindowEvent windowevent)
    {
    }

    void this_windowClosing(WindowEvent e)
    {
        CancelEvent();
    }

    void bntsCancel_actionPerformed(ActionEvent e)
    {
        CancelEvent();
    }

    public void CancelEvent()
    {
        wholedata.clear();
        wholeTable.getTable().removeMouseListener(listenerCommon);
        bntsClear.removeActionListener(listenerCommon);
        bntsSearch.removeActionListener(listenerCommon);
        bntsOk.removeActionListener(listenerCommon);
        bntsCancel.removeActionListener(listenerCommon);
        dispose();
    }

    void bntsClear_actionPerformed(ActionEvent actionevent)
    {
    }

    public void wholeDataFun()
    {
        wholedata = new ArrayList();
        wholecolumn = new ArrayList();
        wholecolumnWidth = new ArrayList();
        if(flag == 0)
        {
            wholecolumn.add(DynaMOAD.getMSRString("groupID", "Group ID", 3));
            wholecolumn.add(DynaMOAD.getMSRString("description", "Description", 3));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
        } else
        if(flag == 1)
        {
            wholecolumn.add(DynaMOAD.getMSRString("roleID", "Role ID", 3));
            wholecolumn.add(DynaMOAD.getMSRString("description", "Description", 3));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
        } else
        if(flag == 2)
        {
            wholecolumn.add(DynaMOAD.getMSRString("userID", "User ID", 3));
            wholecolumn.add(DynaMOAD.getMSRString("userName", "User Name", 3));
            wholecolumn.add(DynaMOAD.getMSRString("description", "Description", 3));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
        }
    }

    void bntsSearch_actionPerformed(ActionEvent e)
    {
        try
        {
            wholedata.clear();
            if(flag == 0)
            {
                ArrayList tempList = aus.listGroup();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap groupInfo = aus.getGroup((String)tempList.get(i));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add(groupInfo.get("cdate"));
                    tempList2.add(groupInfo.get("description"));
                    tempList2.add(groupInfo.get("imdate"));
                    tempList2.add(groupInfo.get("status"));
                    wholedata.add(tempList2.clone());
                    tempList2.clear();
                }

            } else
            if(flag == 1)
            {
                ArrayList tempList = aus.listRole();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap roleInfo = aus.getRole((String)tempList.get(i));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add(roleInfo.get("cdate"));
                    tempList2.add(roleInfo.get("description"));
                    tempList2.add(roleInfo.get("imdate"));
                    tempList2.add(roleInfo.get("status"));
                    wholedata.add(tempList2.clone());
                    tempList2.clear();
                }

            } else
            if(flag == 2)
            {
                ArrayList tempList = aus.listUser();
                ArrayList tempList2 = new ArrayList();
                for(int i = 0; i < tempList.size(); i++)
                {
                    HashMap userInfo = aus.getUser((String)tempList.get(i));
                    tempList2.add((String)tempList.get(i));
                    tempList2.add(userInfo.get("cdate"));
                    tempList2.add(userInfo.get("description"));
                    tempList2.add(userInfo.get("failcount"));
                    tempList2.add(userInfo.get("lfdate"));
                    tempList2.add(userInfo.get("lldate"));
                    tempList2.add(userInfo.get("lmdate"));
                    tempList2.add(userInfo.get("logincount"));
                    tempList2.add(userInfo.get("name"));
                    tempList2.add(userInfo.get("password"));
                    tempList2.add(userInfo.get("primarygroup"));
                    tempList2.add(userInfo.get("status"));
                    tempList2.add(userInfo.get("primarygroup"));
                    wholedata.add(tempList2.clone());
                    tempList2.clear();
                }

            }
            wholeTable.changeTableData();
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public ArrayList makeCondition()
    {
        ArrayList searchCon = new ArrayList();
        return searchCon;
    }

    public void setAddData()
    {
        ArrayList rows = wholeTable.getSelectedRow();
        System.out.println("rows : " + rows);
        if(flag == 0 || flag == 1)
            ((AuthoritySearch)parentWindow).setPrincipal((String)rows.get(0), (String)rows.get(0));
        else
            ((AuthoritySearch)parentWindow).setPrincipal((String)rows.get(0), (String)rows.get(8));
    }

    void wholeTable_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && wholeTable.getSelectedRowNumber() > -1)
            setAddData();
    }

    void bntsOk_actionPerformed(ActionEvent e)
    {
        if(wholeTable.getSelectedRowNumber() > -1)
            setAddData();
        CancelEvent();
    }

    public boolean FieldCheck()
    {
        boolean chf = false;
        return chf;
    }

    public void enterKeyEvent()
    {
        bntsSearch.doClick();
    }

    ArrayList wholedata;
    ArrayList wholecolumn;
    ArrayList wholecolumnWidth;
    public int flag;
    ArrayList ConditionData;
    ArrayList template;
    Cursor handCursor;
    private UIManagement newUI;
    private AUS aus;
    JToolBar buttonToolBar;
    JButton bntsCancel;
    JButton bntsOk;
    JButton bntsClear;
    JButton bntsSearch;
    JPanel pnUserObjectSingleSelect;
    JLabel lbtsWholeTeamList;
    JScrollPane sptsWholeTeamTable;
    Table wholeTable;
    Object parentWindow;
    private ListenerCommon listenerCommon;
    private JPanel panelNameCondition;
    private JPanel panelCondition;
}
