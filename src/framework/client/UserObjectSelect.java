// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserObjectSelect.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
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
//            DynaMOAD, UIManagement, PrincipalSelect

public class UserObjectSelect extends JDialog
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
            else
            if(evt.getSource().equals(selectedTable.getTable()))
                selectedTable_mouseClicked(evt);
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
            if(command.equals("Right"))
                bntsRight_actionPerformed(evt);
            else
            if(command.equals("Ok"))
                bntsOk_actionPerformed(evt);
            else
            if(command.equals("Left"))
                bntsLeft_actionPerformed(evt);
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


    public UserObjectSelect(Dialog dialog, Object parent, String title, boolean modal, int t)
    {
        super(dialog, title, modal);
        handCursor = new Cursor(12);
        newUI = DynaMOAD.newUI;
        aus = DynaMOAD.aus;
        buttonToolBar = new JToolBar();
        bntsOk = new JButton();
        bntsClear = new JButton();
        bntsSearch = new JButton();
        pnUserObjectSelect = new JPanel();
        bntsRight = new JButton();
        bntsLeft = new JButton();
        lbtsWholeTeamList = new JLabel(DynaMOAD.getMSRString("WRD_0108", "Search List", 3));
        lbtsSelectTeamList = new JLabel(DynaMOAD.getMSRString("WRD_0108", "Selected List", 3));
        sptsWholeTeamTable = UIFactory.createStrippedScrollPane(null);
        sptsSelectTeamTable = UIFactory.createStrippedScrollPane(null);
        wholeTable = null;
        selectedTable = null;
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
        buttonToolBar.add(Box.createRigidArea(new Dimension(10, 0)));
        getContentPane().add(buttonToolBar, "North");
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        pnUserObjectSelect.setLayout(gridBag);
        pnUserObjectSelect.setBorder(BorderFactory.createEtchedBorder());
        bntsRight.setActionCommand("Right");
        bntsRight.setToolTipText(DynaMOAD.getMSRString("WRD_0001", "Selection", 3));
        bntsRight.setIcon(new ImageIcon(getClass().getResource("/icons/Forward16.gif")));
        bntsRight.setMargin(new Insets(0, 0, 0, 0));
        bntsRight.addActionListener(listenerCommon);
        bntsLeft.setActionCommand("Left");
        bntsLeft.setToolTipText(DynaMOAD.getMSRString("WRD_0002", "Cancel", 3));
        bntsLeft.setIcon(new ImageIcon(getClass().getResource("/icons/Back16.gif")));
        bntsLeft.setMargin(new Insets(0, 0, 0, 0));
        bntsLeft.addActionListener(listenerCommon);
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
        gridBagCon.gridheight = 4;
        gridBag.setConstraints(sptsWholeTeamTable, gridBagCon);
        JLabel imsi = new JLabel("");
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(imsi, gridBagCon);
        gridBagCon.fill = 0;
        gridBagCon.insets = new Insets(0, 5, 0, 5);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(bntsRight, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(bntsLeft, gridBagCon);
        gridBagCon.fill = 1;
        gridBagCon.insets = new Insets(0, 0, 0, 0);
        JLabel imsi2 = new JLabel("");
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(imsi2, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.ipady = 25;
        gridBag.setConstraints(lbtsSelectTeamList, gridBagCon);
        gridBagCon.ipady = 0;
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 2;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 4;
        gridBag.setConstraints(sptsSelectTeamTable, gridBagCon);
        pnUserObjectSelect.add(lbtsWholeTeamList);
        pnUserObjectSelect.add(sptsWholeTeamTable);
        pnUserObjectSelect.add(lbtsSelectTeamList);
        pnUserObjectSelect.add(sptsSelectTeamTable);
        pnUserObjectSelect.add(imsi);
        pnUserObjectSelect.add(bntsRight);
        pnUserObjectSelect.add(bntsLeft);
        pnUserObjectSelect.add(imsi2);
        getContentPane().add(pnUserObjectSelect, "Center");
        wholeDataFun();
        wholeTable = new Table(wholedata, wholecolumn, wholecolumnWidth, 1);
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
        setSize(400, 300);
    }

    public void makeSelectedTable()
    {
        selectedTable = new Table(selecteddata, selectedcolumn, selectedcolumnWidth, 1);
        if(flag == 0 || flag == 1)
            selectedTable.setColumnSequence(new int[] {
                0, 2
            });
        else
        if(flag == 2)
            selectedTable.setColumnSequence(new int[] {
                0, 8, 2
            });
        selectedTable.setIndexColumn(0);
        selectedTable.getTable().getTableHeader().setBackground(UIManagement.tableheaderColor);
        selectedTable.getTable().setCursor(handCursor);
        selectedTable.getTable().addMouseListener(listenerCommon);
        sptsSelectTeamTable.setViewportView(selectedTable.getTable());
        sptsSelectTeamTable.getViewport().setBackground(UIManagement.treeLevelOneColor);
        sptsSelectTeamTable.setPreferredSize(new Dimension(100, 150));
    }

    void this_windowOpened(WindowEvent windowevent)
    {
    }

    void this_windowClosing(WindowEvent e)
    {
        CancelEvent();
    }

    public void CancelEvent()
    {
        selecteddata.clear();
        wholedata.clear();
        wholeTable.getTable().removeMouseListener(listenerCommon);
        selectedTable.getTable().removeMouseListener(listenerCommon);
        bntsClear.removeActionListener(listenerCommon);
        bntsSearch.removeActionListener(listenerCommon);
        bntsRight.removeActionListener(listenerCommon);
        bntsOk.removeActionListener(listenerCommon);
        bntsLeft.removeActionListener(listenerCommon);
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
            wholecolumn.add(DynaMOAD.getMSRString("description", "Descrpiption", 3));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
        } else
        if(flag == 1)
        {
            wholecolumn.add(DynaMOAD.getMSRString("roleID", "Role ID", 3));
            wholecolumn.add(DynaMOAD.getMSRString("description", "Descrpiption", 3));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
        } else
        if(flag == 2)
        {
            wholecolumn.add(DynaMOAD.getMSRString("userID", "User ID", 3));
            wholecolumn.add(DynaMOAD.getMSRString("userName", "User Name", 3));
            wholecolumn.add(DynaMOAD.getMSRString("description", "Descrpiption", 3));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
            wholecolumnWidth.add(new Integer(80));
        }
    }

    public void selectDataFun(ArrayList vt)
    {
        selecteddata = new ArrayList();
        selectedcolumn = new ArrayList();
        selectedcolumnWidth = new ArrayList();
        if(flag == 0)
        {
            selectedcolumn.add(DynaMOAD.getMSRString("groupID", "Group ID", 3));
            selectedcolumn.add(DynaMOAD.getMSRString("description", "Descrpiption", 3));
            selectedcolumnWidth.add(new Integer(80));
            selectedcolumnWidth.add(new Integer(80));
        } else
        if(flag == 1)
        {
            selectedcolumn.add(DynaMOAD.getMSRString("roleID", "Role ID", 3));
            selectedcolumn.add(DynaMOAD.getMSRString("description", "Descrpiption", 3));
            selectedcolumnWidth.add(new Integer(80));
            selectedcolumnWidth.add(new Integer(80));
        } else
        if(flag == 2)
        {
            selectedcolumn.add(DynaMOAD.getMSRString("userID", "User ID", 3));
            selectedcolumn.add(DynaMOAD.getMSRString("userName", "User Name", 3));
            selectedcolumn.add(DynaMOAD.getMSRString("description", "Descrpiption", 3));
            selectedcolumnWidth.add(new Integer(80));
            selectedcolumnWidth.add(new Integer(80));
            selectedcolumnWidth.add(new Integer(80));
        }
        int vtsize = vt.size();
        if(vtsize > 0)
        {
            for(int i = 0; i < vtsize; i++)
                selecteddata.add(vt.get(i));

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

    void bntsRight_actionPerformed(ActionEvent e)
    {
        setAddData();
    }

    public void setAddData()
    {
        ArrayList rows = wholeTable.getSelectedRows();
        for(int i = 0; i < rows.size(); i++)
        {
            ArrayList tmpList = (ArrayList)rows.get(i);
            if(!selecteddata.contains(tmpList))
                selecteddata.add(tmpList.clone());
            tmpList = null;
        }

        selectedTable.changeTableData();
    }

    void selectedTable_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && selectedTable.getSelectedRowNumber() > -1)
            setDeleteData();
    }

    void wholeTable_mouseClicked(MouseEvent e)
    {
        if(e.getClickCount() == 2 && wholeTable.getSelectedRowNumber() > -1)
            setAddData();
    }

    void bntsLeft_actionPerformed(ActionEvent e)
    {
        setDeleteData();
    }

    public void setDeleteData()
    {
        ArrayList rows = selectedTable.getSelectedRows();
        for(int i = 0; i < rows.size(); i++)
        {
            ArrayList tmpList = (ArrayList)rows.get(i);
            int row = selecteddata.indexOf(tmpList);
            if(row > -1)
                selecteddata.remove(row);
        }

        selectedTable.changeTableData();
        selectedTable.getTable().clearSelection();
    }

    void bntsOk_actionPerformed(ActionEvent e)
    {
        if(flag == 0)
            ((PrincipalSelect)parentWindow).setGroupData(selecteddata);
        else
        if(flag == 1)
            ((PrincipalSelect)parentWindow).setRoleData(selecteddata);
        else
        if(flag == 2)
            ((PrincipalSelect)parentWindow).setUserData(selecteddata);
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
    ArrayList selecteddata;
    ArrayList selectedcolumn;
    ArrayList selectedcolumnWidth;
    public int flag;
    ArrayList ConditionData;
    ArrayList template;
    Cursor handCursor;
    private UIManagement newUI;
    private AUS aus;
    JToolBar buttonToolBar;
    JButton bntsOk;
    JButton bntsClear;
    JButton bntsSearch;
    JPanel pnUserObjectSelect;
    JButton bntsRight;
    JButton bntsLeft;
    JLabel lbtsWholeTeamList;
    JLabel lbtsSelectTeamList;
    JScrollPane sptsWholeTeamTable;
    JScrollPane sptsSelectTeamTable;
    Table wholeTable;
    Table selectedTable;
    Object parentWindow;
    private ListenerCommon listenerCommon;
    private JPanel panelNameCondition;
    private JPanel panelCondition;
}
