// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DSSInformation.java

package dyna.framework.editor.dssmanager;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.AUS;
import dyna.framework.service.DSS;
import dyna.uic.DynaTextField;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.dssmanager:
//            DSSManager, SmallSearchForDSSManager

public class DSSInformation extends JPanel
    implements ActionListener
{

    public DSSInformation(DSSManager parent, boolean mode)
    {
        aus = null;
        dss = null;
        mainBorderLayout = null;
        mainPanel = null;
        idTextField = null;
        descriptionTextField = null;
        statusTextField = null;
        cdateTextField = null;
        lmdateTextField = null;
        dummyLabel = null;
        buttonToolBar = null;
        okButton = null;
        pwChangeButton = null;
        smallSearchButton = null;
        groupInfoMode = false;
        this.parent = null;
        this.parent = parent;
        groupInfoMode = mode;
        aus = DSSManager.aus;
        dss = DSSManager.dss;
        initialize();
        setFieldEnabled(false);
    }

    public void initialize()
    {
        setSize(new Dimension(600, 400));
        idTextField = new DynaTextField();
        idTextField.setMandatory(true);
        idTextField.setTitleText("DSS Path");
        idTextField.setVisible(true);
        idTextField.setTitleWidth(150);
        idTextField.setTitleVisible(true);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Storage Id");
        descriptionTextField.setVisible(true);
        descriptionTextField.setTitleWidth(150);
        descriptionTextField.setTitleVisible(true);
        smallSearchButton = new JButton();
        smallSearchButton.setIcon(new ImageIcon(getClass().getResource("/icons/SmallSearchButton.gif")));
        smallSearchButton.setMargin(new Insets(0, 0, 0, 0));
        smallSearchButton.addActionListener(this);
        if(groupInfoMode)
            smallSearchButton.setEnabled(false);
        else
            smallSearchButton.setEnabled(true);
        statusTextField = new DynaTextField();
        statusTextField.setMandatory(false);
        statusTextField.setTitleText("Base directory");
        statusTextField.setTitleWidth(150);
        statusTextField.setTitleVisible(true);
        cdateTextField = new DynaTextField();
        cdateTextField.setMandatory(false);
        cdateTextField.setTitleText("Created date");
        cdateTextField.setTitleWidth(150);
        cdateTextField.setTitleVisible(true);
        lmdateTextField = new DynaTextField();
        lmdateTextField.setMandatory(false);
        lmdateTextField.setTitleText("Last modified date");
        lmdateTextField.setTitleWidth(150);
        lmdateTextField.setTitleVisible(true);
        dummyLabel = new JLabel("");
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 2;
        gridBagCon.anchor = 12;
        mainPanel = new JPanel();
        mainPanel.setLayout(gridBag);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(idTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(descriptionTextField, gridBagCon);
        gridBagCon.weightx = 0.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 1;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBagCon.insets = new Insets(1, 0, 0, 5);
        gridBag.setConstraints(smallSearchButton, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(statusTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(cdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(lmdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(idTextField);
        mainPanel.add(descriptionTextField);
        mainPanel.add(smallSearchButton);
        mainPanel.add(dummyLabel);
        okButton = new JButton();
        okButton.setText("Save");
        okButton.setIcon(new ImageIcon("icons/Save.gif"));
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        pwChangeButton = new JButton();
        pwChangeButton.setText("Password Change");
        pwChangeButton.setActionCommand("Password Change");
        pwChangeButton.addActionListener(this);
        buttonToolBar = new JToolBar();
        buttonToolBar.add(okButton);
        mainBorderLayout = new BorderLayout();
        setLayout(mainBorderLayout);
        if(groupInfoMode)
            add(buttonToolBar, "North");
        add(mainPanel, "Center");
    }

    public void setData(HashMap dssInfo)
    {
        idTextField.setText((String)dssInfo.get("id"));
        descriptionTextField.setText((String)dssInfo.get("storage.id"));
        statusTextField.setText((String)dssInfo.get("base.directory.path"));
        cdateTextField.setText((String)dssInfo.get("cdate"));
        lmdateTextField.setText((String)dssInfo.get("lmdate"));
    }

    public void setFieldEnabled(boolean mode)
    {
        if(!mode)
        {
            cdateTextField.setEditable(false);
            lmdateTextField.setEditable(false);
        }
    }

    public void createGroup()
    {
        String storageId = descriptionTextField.getText();
        String path = idTextField.getText();
        try
        {
            if(!groupInfoMode && !Utils.isNullString(storageId) && !Utils.isNullString(path))
            {
                dss.mountStorage(storageId, path);
                parent.addDSSTreeNode(path);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Ok"))
        {
            String storageId = idTextField.getText();
            String path = descriptionTextField.getText();
            try
            {
                if(!groupInfoMode && !Utils.isNullString(storageId) && !Utils.isNullString(path))
                    dss.mountStorage(storageId, path);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        } else
        {
            SmallSearchForDSSManager storageIdSearch = new SmallSearchForDSSManager(parent, this, "storage.id", "");
            storageIdSearch.setVisible(true);
        }
    }

    private AUS aus;
    private DSS dss;
    private BorderLayout mainBorderLayout;
    private JPanel mainPanel;
    private DynaTextField idTextField;
    public DynaTextField descriptionTextField;
    private DynaTextField statusTextField;
    private DynaTextField cdateTextField;
    private DynaTextField lmdateTextField;
    private JLabel dummyLabel;
    private JToolBar buttonToolBar;
    private JButton okButton;
    private JButton pwChangeButton;
    private JButton smallSearchButton;
    private final int titleTextWidth = 150;
    private boolean groupInfoMode;
    private DSSManager parent;
}
