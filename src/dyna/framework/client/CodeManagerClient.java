// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CodeManagerClient.java

package dyna.framework.client;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIManagement, DFDefaultTreeCellRenderer

public class CodeManagerClient extends JFrame
    implements ActionListener, MouseListener
{
    class CodeTreeMouseListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            codeTree_mouseClicked(e);
            if(SwingUtilities.isRightMouseButton(e))
                codeTree.setSelectionPath(codeTree.getClosestPathForLocation(e.getX(), e.getY()));
        }

        CodeTreeMouseListener()
        {
        }
    }

    class PopupListener extends MouseAdapter
    {

        public void mousePressed(MouseEvent e)
        {
            if(codeTree == null)
                return;
            node = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
            if(node == null)
                return;
            if(node.getLevel() == 0)
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Code");
                deleteMenu.setVisible(false);
            } else
            if(node.getLevel() == 1)
            {
                addMenuAdd.setEnabled(true);
                addMenuAdd.setVisible(true);
                addMenuAdd.setText("Code Item");
                deleteMenu.setVisible(true);
            }
            showPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            showPopup(e);
        }

        private void showPopup(MouseEvent e)
        {
            if(e.getClickCount() == 1 && e.isPopupTrigger())
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

        DefaultMutableTreeNode node;

        PopupListener()
        {
            node = null;
        }
    }

    class PopupMenu extends MouseAdapter
    {

        public void mouseClicked(MouseEvent e)
        {
            int row = codeItemTable.getTable().getSelectedRow();
            String ouidRow = codeItemTable.getSelectedOuidRow(row);
            if(ouidRow != null)
                codeItemTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
        }

        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e)
        {
            if(e.isPopupTrigger() && SwingUtilities.isRightMouseButton(e) && !e.isControlDown() && codeItemTable.getSelectedRowNumber() > -1)
            {
                int row = codeItemTable.getTable().getSelectedRow();
                String ouidRow = codeItemTable.getSelectedOuidRow(row);
                codeItemTable.setOrderForRowSelection((new Integer(ouidRow)).intValue());
                if(ouidRow != null)
                {
                    selectOuid = (String)((ArrayList)codeItemData.get((new Integer(ouidRow)).intValue())).get(0);
                    codeItem = (String)((ArrayList)codeItemData.get((new Integer(ouidRow)).intValue())).get(3);
                } else
                {
                    selectOuid = (String)((ArrayList)codeItemData.get((new Integer(row)).intValue())).get(0);
                    codeItem = (String)((ArrayList)codeItemData.get((new Integer(row)).intValue())).get(3);
                }
                menuInfoTable.setEnabled(false);
                if(selectOuid != null)
                    menuInfoTable.setEnabled(true);
                JFrame frame = (JFrame)JOptionPane.getFrameForComponent(e.getComponent());
                Dimension framesize = frame.getSize();
                Dimension popupsize = popupTable.getSize();
                if(popupsize.width <= 0 || popupsize.height <= 0)
                    popupsize = new Dimension(107, 100);
                Point point = SwingUtilities.convertPoint(e.getComponent(), e.getX(), e.getY(), frame);
                int x = e.getX();
                int y = e.getY();
                if(popupsize.width + point.x >= framesize.width)
                    x -= popupsize.width;
                if(popupsize.height + point.y >= framesize.height)
                    y -= popupsize.height;
                popupTable.show(e.getComponent(), x, y);
            }
        }

        PopupMenu()
        {
        }
    }


    public CodeManagerClient()
    {
        modeOfCodeItem = false;
        try
        {
            dos = DynaMOAD.dos;
            modelOuid = dos.getWorkingModel();
            TreeNodeObject nodeObj = new TreeNodeObject("Code", "Code", "Code");
            topNode = new DefaultMutableTreeNode(nodeObj);
            codeTreeScrPane = UIFactory.createStrippedScrollPane(null);
            codeTreeScrPane.setSize(200, 200);
            setTopNode(nodeObj);
            setFullTreeExpand(topNode);
            makeTable();
            makeCodeInfoPanel();
            initialize();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void initialize()
    {
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/DynaMOAD.gif"));
        setTitle("Code Manager");
        codeSplitPane = new JSplitPane(1);
        codeSplitPane.setDividerLocation(200);
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(codeTreeScrPane, "Center");
        leftPanel.add(codeInfoPanel, "South");
        codeSplitPane.add(leftPanel, "left");
        codeItemTableScrPane = UIFactory.createStrippedScrollPane(null);
        codeItemTableScrPane.setViewportView(codeItemTable.getTable());
        codeItemTableScrPane.getViewport().setBackground(UIManagement.treeLevelOneColor);
        codeSplitPane.add(codeItemTableScrPane, "right");
        getContentPane().add(codeSplitPane);
        makeTree();
        popupTable = new JPopupMenu();
        menuInfoTable = new JMenuItem();
        menuDeleteTable = new JMenuItem();
        menuInfoTable.setText("Information");
        menuInfoTable.setIcon(new ImageIcon("icons/Information.gif"));
        menuInfoTable.setActionCommand("Information");
        popupTable.add(menuInfoTable);
        menuDeleteTable.setText("Delete");
        menuDeleteTable.setIcon(new ImageIcon("icons/Delete.gif"));
        menuDeleteTable.setActionCommand("Delete");
        MouseListener PmousePopup = new PopupMenu();
        codeItemTable.getTable().addMouseListener(PmousePopup);
        codeItemTableScrPane.addMouseListener(PmousePopup);
        menuInfoTable.addActionListener(this);
        menuDeleteTable.addActionListener(this);
    }

    public void makeTree()
    {
        if(topNode == null)
            return;
        if(codeTree == null)
        {
            return;
        } else
        {
            DFDefaultTreeCellRenderer renderer = new DFDefaultTreeCellRenderer();
            codeTree.setCellRenderer(renderer);
            codeTree.getSelectionModel().setSelectionMode(1);
            codeTree.putClientProperty("JTree.lineStyle", "Angled");
            codeTreeScrPane.setViewportView(codeTree);
            return;
        }
    }

    public void createPopupMenu()
    {
        popupMenu = new JPopupMenu();
        addMenuAdd = new JMenu();
        addMenuAdd.setText("\uCD94\uAC00");
        informationMenu = new JMenuItem();
        informationMenu.setText("\uC815\uBCF4");
        informationMenu.setActionCommand("InformationMenu");
        informationMenu.addActionListener(this);
        addMenu = new JMenuItem();
        addMenu.setText("\uCD94\uAC00");
        addMenu.setActionCommand("AddMenu");
        addMenu.addActionListener(this);
        deleteMenu = new JMenuItem();
        deleteMenu.setText("\uC0AD\uC81C");
        deleteMenu.setActionCommand("DeleteMenu");
        deleteMenu.addActionListener(this);
        addMenuAdd.add(addMenu);
        popupMenu.add(addMenuAdd);
        popupMenu.add(new JSeparator());
        popupMenu.add(deleteMenu);
        MouseListener popupListener = new PopupListener();
        codeTree.addMouseListener(popupListener);
    }

    public void setTopNode(TreeNodeObject topnode)
    {
        try
        {
            topNodeObj = topnode;
            CodeTreeMouseListener codeTreeMouseListener = new CodeTreeMouseListener();
            if(codeTree != null)
            {
                codeTree.removeMouseListener(codeTreeMouseListener);
                codeTree = null;
            }
            topNode = new DefaultMutableTreeNode(topNodeObj);
            treeModel = new DefaultTreeModel(topNode);
            codeTree = new JTree(treeModel);
            if(topNode == null)
                return;
            codeTree.addMouseListener(codeTreeMouseListener);
            if(codeTree == null)
                return;
            DFDefaultTreeCellRenderer renderer = new DFDefaultTreeCellRenderer();
            codeTree.setCellRenderer(renderer);
            codeTree.getSelectionModel().setSelectionMode(1);
            codeTree.putClientProperty("JTree.lineStyle", "Angled");
            codeTreeScrPane.setViewportView(codeTree);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public void setFullTreeExpand(DefaultMutableTreeNode node)
    {
        if(node == null)
        {
            return;
        } else
        {
            setExpand(node);
            codeTree.setSelectionRow(0);
            return;
        }
    }

    private void setExpand(DefaultMutableTreeNode node)
    {
        String ouid = "";
        ArrayList codeListAL = new ArrayList();
        codeInfoDC = new DOSChangeable();
        try
        {
            codeListAL = dos.listCode(modelOuid);
            if(codeListAL != null && !codeListAL.isEmpty())
            {
                for(int i = 0; i < codeListAL.size(); i++)
                {
                    ouid = (String)((DOSChangeable)codeListAL.get(i)).get("ouid");
                    codeInfoDC = dos.getCode(ouid);
                    if(codeInfoDC != null)
                    {
                        TreeNodeObject codedata = new TreeNodeObject((String)codeInfoDC.get("ouid"), (String)codeInfoDC.get("name"), "Code");
                        DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(codedata);
                        setInsertNode(node, childnode);
                        codeTree.scrollPathToVisible(new TreePath(childnode.getPath()));
                    }
                }

            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    void setInsertNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child)
    {
        DefaultTreeModel model = (DefaultTreeModel)codeTree.getModel();
        model.insertNodeInto(child, parent, parent.getChildCount());
    }

    private void codeTree_mouseClicked(MouseEvent e)
    {
        refreshCodeItemTable();
    }

    private void makeTable()
    {
        codeItemData = new ArrayList();
        codeItemColumnName = new ArrayList();
        codeItemColumnWidth = new ArrayList();
        codeItemColumnName.add("Ouid");
        codeItemColumnName.add("Name");
        codeItemColumnName.add("Description");
        codeItemColumnName.add("CodeItem");
        codeItemColumnWidth.add(new Integer(80));
        codeItemColumnWidth.add(new Integer(80));
        codeItemColumnWidth.add(new Integer(80));
        codeItemColumnWidth.add(new Integer(80));
        codeItemTable = new Table(codeItemData, (ArrayList)codeItemColumnName.clone(), (ArrayList)codeItemColumnWidth.clone(), 0, 240);
        codeItemTable.getTable().addMouseListener(this);
        codeItemTable.setColumnSequence(new int[] {
            0, 1, 2, 3
        });
        codeItemTable.setIndexColumn(0);
    }

    public void makeCodeInfoPanel()
    {
        codeInfoPanel = new JPanel();
        nameDynaTxtFld = new DynaTextField();
        nameDynaTxtFld.setTitleText("name");
        nameDynaTxtFld.setTitleVisible(true);
        nameDynaTxtFld.setTitleWidth(80);
        nameDynaTxtFld.setSize(200, 21);
        descDynaTxtFld = new DynaTextField();
        descDynaTxtFld.setTitleText("description");
        descDynaTxtFld.setTitleVisible(true);
        descDynaTxtFld.setTitleWidth(80);
        descDynaTxtFld.setSize(200, 21);
        prefixDynaTxtFld = new DynaTextField();
        prefixDynaTxtFld.setTitleText("prefix");
        prefixDynaTxtFld.setTitleVisible(true);
        prefixDynaTxtFld.setTitleWidth(80);
        prefixDynaTxtFld.setSize(200, 21);
        saveButton = new JButton("Modify");
        saveButton.setActionCommand("Modify");
        saveButton.addActionListener(this);
        saveButton.setSize(60, 21);
        GridBagLayout codeInfoGrdBgLyt = new GridBagLayout();
        GridBagConstraints codeInfoGrdBgConst = new GridBagConstraints();
        codeInfoGrdBgConst.fill = 1;
        codeInfoGrdBgConst.anchor = 11;
        codeInfoPanel.setLayout(codeInfoGrdBgLyt);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 1.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 0;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgLyt.setConstraints(nameDynaTxtFld, codeInfoGrdBgConst);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 1.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 1;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgLyt.setConstraints(descDynaTxtFld, codeInfoGrdBgConst);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 1.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 2;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgLyt.setConstraints(prefixDynaTxtFld, codeInfoGrdBgConst);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 1.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 3;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.insets = new Insets(5, 5, 5, 5);
        codeInfoGrdBgLyt.setConstraints(saveButton, codeInfoGrdBgConst);
        codeInfoPanel.add(nameDynaTxtFld);
        codeInfoPanel.add(descDynaTxtFld);
        codeInfoPanel.add(prefixDynaTxtFld);
        codeInfoPanel.add(saveButton);
    }

    private void makeInsertDialog()
    {
        insertDlg = new JDialog(this, "Code Insert", false);
        insertDlg.setResizable(false);
        insertDlg.getContentPane().setLayout(null);
        nameLabel = new JLabel("name");
        nameLabel.setBounds(5, 5, 100, 21);
        descLabel = new JLabel("description");
        descLabel.setBounds(5, 30, 100, 21);
        prefixLabel = new JLabel("prefix");
        prefixLabel.setBounds(5, 55, 100, 21);
        nameTextField = new JTextField();
        nameTextField.setBounds(120, 5, 240, 21);
        descTextField = new JTextField();
        descTextField.setBounds(120, 30, 240, 21);
        prefixTextField = new JTextField();
        prefixTextField.setBounds(120, 55, 240, 21);
        okButtonInsertDlg = new JButton("Ok");
        okButtonInsertDlg.setActionCommand("OkInsertDlg");
        okButtonInsertDlg.setBounds(110, 140, 80, 21);
        okButtonInsertDlg.addActionListener(this);
        cancelButtonInsertDlg = new JButton("Cancel");
        cancelButtonInsertDlg.setActionCommand("CancelInsertDlg");
        cancelButtonInsertDlg.setBounds(210, 140, 80, 21);
        cancelButtonInsertDlg.addActionListener(this);
        insertDlg.getContentPane().add(nameLabel);
        insertDlg.getContentPane().add(nameTextField);
        insertDlg.getContentPane().add(descLabel);
        insertDlg.getContentPane().add(descTextField);
        insertDlg.getContentPane().add(prefixLabel);
        insertDlg.getContentPane().add(prefixTextField);
        insertDlg.getContentPane().add(okButtonInsertDlg);
        insertDlg.getContentPane().add(cancelButtonInsertDlg);
    }

    public void makeCodeItemDialog()
    {
        insertCodeItemDlg = new JDialog(this, "Code Insert", false);
        insertCodeItemDlg.setResizable(false);
        insertCodeItemDlg.getContentPane().setLayout(null);
        nameOfCodeItemLabel = new JLabel("name");
        nameOfCodeItemLabel.setBounds(5, 5, 100, 21);
        descOfCodeItemLabel = new JLabel("description");
        descOfCodeItemLabel.setBounds(5, 30, 100, 21);
        nameOfCodeItemTextField = new JTextField();
        nameOfCodeItemTextField.setBounds(120, 5, 240, 21);
        descOfCodeItemTextField = new JTextField();
        descOfCodeItemTextField.setBounds(120, 30, 240, 21);
        okButtonInsertDlgOfCodeItem = new JButton("Ok");
        okButtonInsertDlgOfCodeItem.setActionCommand("OkCodeItem");
        okButtonInsertDlgOfCodeItem.setBounds(110, 140, 80, 21);
        okButtonInsertDlgOfCodeItem.addActionListener(this);
        cancelButtonInsertDlgOfCodeItem = new JButton("Cancel");
        cancelButtonInsertDlgOfCodeItem.setActionCommand("CancelCodeItem");
        cancelButtonInsertDlgOfCodeItem.setBounds(210, 140, 80, 21);
        cancelButtonInsertDlgOfCodeItem.addActionListener(this);
        insertCodeItemDlg.getContentPane().add(nameOfCodeItemLabel);
        insertCodeItemDlg.getContentPane().add(nameOfCodeItemTextField);
        insertCodeItemDlg.getContentPane().add(descOfCodeItemLabel);
        insertCodeItemDlg.getContentPane().add(descOfCodeItemTextField);
        insertCodeItemDlg.getContentPane().add(okButtonInsertDlgOfCodeItem);
        insertCodeItemDlg.getContentPane().add(cancelButtonInsertDlgOfCodeItem);
    }

    private void refreshCodeItemTable()
    {
        try
        {
            DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)codeTree.getLastSelectedPathComponent();
            if(selnode == null)
                return;
            if(selnode.getLevel() == 0)
            {
                nameDynaTxtFld.setText("");
                descDynaTxtFld.setText("");
                prefixDynaTxtFld.setText("");
                return;
            }
            TreeNodeObject codedata = (TreeNodeObject)selnode.getUserObject();
            selectedCodeOuid = codedata.getOuid();
            ArrayList codeItemList = dos.listCodeItem(selectedCodeOuid);
            codeItemData.clear();
            for(int i = 0; i < codeItemList.size(); i++)
            {
                ArrayList tempAL = new ArrayList(5);
                tempAL.add(((DOSChangeable)codeItemList.get(i)).get("ouid"));
                tempAL.add(((DOSChangeable)codeItemList.get(i)).get("name"));
                tempAL.add(((DOSChangeable)codeItemList.get(i)).get("description"));
                tempAL.add(((DOSChangeable)codeItemList.get(i)).get("codeitem"));
                codeItemData.add(tempAL.clone());
                tempAL.clear();
            }

            codeItemTable.changeTableData();
            DOSChangeable codeInfo = dos.getCode(selectedCodeOuid);
            nameDynaTxtFld.setText((String)codeInfo.get("name"));
            descDynaTxtFld.setText((String)codeInfo.get("description"));
            prefixDynaTxtFld.setText((String)codeInfo.get("prefix"));
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setModeOfCodeItemDlg(boolean mode)
    {
        modeOfCodeItem = mode;
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("AddMenu"))
        {
            DefaultMutableTreeNode parentNode = null;
            TreePath parentPath = codeTree.getSelectionPath();
            if(parentPath == null)
                parentNode = topNode;
            else
                parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
            if(parentNode.getLevel() == 0)
            {
                makeInsertDialog();
                insertDlg.pack();
                UIUtils.setLocationRelativeTo(insertDlg, codeTree);
                insertDlg.setSize(400, 200);
                insertDlg.setVisible(true);
            } else
            {
                setModeOfCodeItemDlg(false);
                makeCodeItemDialog();
                insertCodeItemDlg.pack();
                UIUtils.setLocationRelativeTo(insertCodeItemDlg, codeTree);
                insertCodeItemDlg.setSize(400, 200);
                insertCodeItemDlg.setVisible(true);
            }
        } else
        if(command.equals("OkInsertDlg"))
            try
            {
                DOSChangeable codeInformation = new DOSChangeable();
                codeInformation.put("name", nameTextField.getText());
                codeInformation.put("description", descTextField.getText());
                codeInformation.put("prefix", prefixTextField.getText());
                codeInformation.put("is.autogeneratable", new Boolean(true));
                codeInformation.put("suffixlength", new Integer("7"));
                codeInformation.put("initvalue", new Integer("1"));
                codeInformation.put("increment", new Integer("1"));
                codeInformation.put("ouid@model", modelOuid);
                String resultOuid = dos.createCode(modelOuid, codeInformation);
                System.out.println(resultOuid);
                insertDlg.dispose();
                TreeNodeObject nodeObj = new TreeNodeObject("Code", "Code", "Code");
                setTopNode(nodeObj);
                setFullTreeExpand(topNode);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("CancelInsertDlg"))
            insertDlg.dispose();
        else
        if(command.equals("OkCodeItem"))
            try
            {
                if(!modeOfCodeItem)
                {
                    DOSChangeable codeInformation = new DOSChangeable();
                    codeInformation.put("name", nameOfCodeItemTextField.getText());
                    codeInformation.put("description", descOfCodeItemTextField.getText());
                    codeInformation.put("ouid@code", selectedCodeOuid);
                    if(selectedCodeOuid != null && !Utils.isNullString(selectedCodeOuid))
                    {
                        String resultOuid = dos.createCodeItem(selectedCodeOuid, codeInformation);
                        System.out.println(resultOuid);
                        insertCodeItemDlg.dispose();
                        refreshCodeItemTable();
                    }
                } else
                {
                    DOSChangeable codeInformation = new DOSChangeable();
                    codeInformation.put("ouid", selectOuid);
                    codeInformation.put("ouid@code", selectedCodeOuid);
                    codeInformation.put("name", nameOfCodeItemTextField.getText());
                    codeInformation.put("description", descOfCodeItemTextField.getText());
                    codeInformation.put("codeitem", codeItem);
                    if(selectOuid != null && !Utils.isNullString(selectOuid))
                    {
                        dos.setCodeItem(codeInformation);
                        insertCodeItemDlg.dispose();
                        refreshCodeItemTable();
                    }
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("CancelCodeItem"))
            insertCodeItemDlg.dispose();
        else
        if(command.equals("Delete"))
            try
            {
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
        else
        if(command.equals("Information"))
            try
            {
                DOSChangeable codeItemInfo = new DOSChangeable();
                setModeOfCodeItemDlg(true);
                makeCodeItemDialog();
                if(selectOuid != null && !Utils.isNullString(selectOuid))
                    codeItemInfo = dos.getCodeItem(selectOuid);
                nameOfCodeItemTextField.setText((String)codeItemInfo.get("name"));
                descOfCodeItemTextField.setText((String)codeItemInfo.get("description"));
                insertCodeItemDlg.pack();
                UIUtils.setLocationRelativeTo(insertCodeItemDlg, codeItemTable.getTable());
                insertCodeItemDlg.setSize(400, 200);
                insertCodeItemDlg.setVisible(true);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("Modify"))
            try
            {
                if(selectedCodeOuid != null && !Utils.isNullString(selectedCodeOuid))
                {
                    DOSChangeable codeModifiedInfo = new DOSChangeable();
                    codeModifiedInfo.put("ouid", selectedCodeOuid);
                    codeModifiedInfo.put("name", nameDynaTxtFld.getText());
                    codeModifiedInfo.put("description", descDynaTxtFld.getText());
                    codeModifiedInfo.put("ouid@model", modelOuid);
                    codeModifiedInfo.put("prefix", prefixDynaTxtFld.getText());
                    codeModifiedInfo.put("is.autogeneratable", new Boolean(true));
                    codeModifiedInfo.put("suffixlength", new Integer("7"));
                    codeModifiedInfo.put("initvalue", new Integer("1"));
                    codeModifiedInfo.put("increment", new Integer("1"));
                    codeModifiedInfo.put("ouid@model", modelOuid);
                    dos.setCode(codeModifiedInfo);
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        else
        if(command.equals("DeleteMenu"))
            try
            {
                if(selectedCodeOuid != null && !Utils.isNullString(selectedCodeOuid))
                {
                    dos.removeCode(selectedCodeOuid);
                    TreeNodeObject nodeObj = new TreeNodeObject("Code", "Code", "Code");
                    setTopNode(nodeObj);
                    setFullTreeExpand(topNode);
                }
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
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

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    private DOS dos;
    private JSplitPane codeSplitPane;
    private JPanel leftPanel;
    private JPanel codeInfoPanel;
    private DynaTextField nameDynaTxtFld;
    private DynaTextField descDynaTxtFld;
    private DynaTextField prefixDynaTxtFld;
    private JButton saveButton;
    private JScrollPane codeTreeScrPane;
    private JTree codeTree;
    private DefaultMutableTreeNode topNode;
    private DefaultTreeModel treeModel;
    private TreeNodeObject topNodeObj;
    private JScrollPane codeItemTableScrPane;
    private Table codeItemTable;
    private ArrayList codeItemData;
    private ArrayList codeItemColumnName;
    private ArrayList codeItemColumnWidth;
    private JPopupMenu popupMenu;
    private JMenu addMenuAdd;
    private JMenuItem informationMenu;
    private JMenuItem addMenu;
    private JMenuItem deleteMenu;
    private JPopupMenu popupTable;
    private JMenuItem menuInfoTable;
    private JMenuItem menuDeleteTable;
    private JDialog insertDlg;
    private JLabel nameLabel;
    private JLabel descLabel;
    private JLabel modelLabel;
    private JLabel prefixLabel;
    private JTextField nameTextField;
    private JTextField descTextField;
    private JTextField modelTextField;
    private JTextField prefixTextField;
    private JButton okButtonInsertDlg;
    private JButton cancelButtonInsertDlg;
    private JDialog insertCodeItemDlg;
    private JLabel nameOfCodeItemLabel;
    private JLabel descOfCodeItemLabel;
    private JTextField nameOfCodeItemTextField;
    private JTextField descOfCodeItemTextField;
    private JButton okButtonInsertDlgOfCodeItem;
    private JButton cancelButtonInsertDlgOfCodeItem;
    private String modelOuid;
    private DOSChangeable codeInfoDC;
    private String selectedCodeOuid;
    private String selectOuid;
    private boolean modeOfCodeItem;
    private String codeItem;
    final String INFO_MENU = "\uC815\uBCF4";
    final String ADD_MENU = "\uCD94\uAC00";
    final String DELETE_MENU = "\uC0AD\uC81C";












}