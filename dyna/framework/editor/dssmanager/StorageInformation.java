// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StorageInformation.java

package dyna.framework.editor.dssmanager;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DSS;
import dyna.uic.DynaTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.dssmanager:
//            DSSManager

public class StorageInformation extends JPanel
    implements ActionListener
{

    public StorageInformation(DSSManager parent, boolean mode)
    {
        dss = null;
        mainBorderLayout = null;
        mainPanel = null;
        storageIdTextField = null;
        brokerIdTextField = null;
        descriptionTextField = null;
        baseDirTextField = null;
        cdateTextField = null;
        lmdateTextField = null;
        dummyLabel = null;
        buttonToolBar = null;
        okButton = null;
        pwChangeButton = null;
        groupInfoMode = false;
        this.parent = null;
        this.parent = parent;
        groupInfoMode = mode;
        dss = DSSManager.dss;
        initialize();
        setFieldEnabled(false);
    }

    public void initialize()
    {
        setSize(new Dimension(600, 400));
        storageIdTextField = new DynaTextField();
        storageIdTextField.setMandatory(true);
        storageIdTextField.setTitleText("Storage Id");
        storageIdTextField.setVisible(true);
        storageIdTextField.setTitleWidth(150);
        storageIdTextField.setTitleVisible(true);
        brokerIdTextField = new DynaTextField();
        brokerIdTextField.setMandatory(false);
        brokerIdTextField.setTitleText("Broker Id");
        brokerIdTextField.setVisible(true);
        brokerIdTextField.setTitleWidth(150);
        brokerIdTextField.setTitleVisible(true);
        descriptionTextField = new DynaTextField();
        descriptionTextField.setMandatory(false);
        descriptionTextField.setTitleText("Description");
        descriptionTextField.setVisible(true);
        descriptionTextField.setTitleWidth(150);
        descriptionTextField.setTitleVisible(true);
        baseDirTextField = new DynaTextField();
        baseDirTextField.setMandatory(false);
        baseDirTextField.setTitleText("Base directory");
        baseDirTextField.setTitleWidth(150);
        baseDirTextField.setTitleVisible(true);
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
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(storageIdTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(brokerIdTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(descriptionTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(baseDirTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 4;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(cdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(lmdateTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 6;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(storageIdTextField);
        mainPanel.add(brokerIdTextField);
        mainPanel.add(descriptionTextField);
        mainPanel.add(baseDirTextField);
        mainPanel.add(cdateTextField);
        mainPanel.add(lmdateTextField);
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

    public void setData(HashMap storageInfo)
    {
        storageIdTextField.setText((String)storageInfo.get("storage.id"));
        brokerIdTextField.setText((String)storageInfo.get("broker.id"));
        descriptionTextField.setText((String)storageInfo.get("description"));
        baseDirTextField.setText((String)storageInfo.get("base.directory.path"));
        cdateTextField.setText((String)storageInfo.get("cdate"));
        lmdateTextField.setText((String)storageInfo.get("lmdate"));
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
        HashMap storageInfo = new HashMap();
        storageInfo.put("storage.id", storageIdTextField.getText());
        storageInfo.put("broker.id", brokerIdTextField.getText());
        storageInfo.put("description", descriptionTextField.getText());
        storageInfo.put("base.directory.path", baseDirTextField.getText());
        storageInfo.put("cdate", cdateTextField.getText());
        storageInfo.put("lmdate", lmdateTextField.getText());
        try
        {
            if(!groupInfoMode)
            {
                dss.createStorage(storageInfo);
                parent.addStorageTreeNode(storageIdTextField.getText());
            } else
            {
                dss.setStorage(storageInfo);
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
            HashMap storageInfo = new HashMap();
            storageInfo.put("storage.id", storageIdTextField.getText());
            storageInfo.put("broker.id", brokerIdTextField.getText());
            storageInfo.put("description", descriptionTextField.getText());
            storageInfo.put("base.directory.path", baseDirTextField.getText());
            storageInfo.put("cdate", cdateTextField.getText());
            storageInfo.put("lmdate", lmdateTextField.getText());
            try
            {
                if(!groupInfoMode)
                    dss.createStorage(storageInfo);
                else
                    dss.setStorage(storageInfo);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        }
    }

    private DSS dss;
    private BorderLayout mainBorderLayout;
    private JPanel mainPanel;
    private DynaTextField storageIdTextField;
    private DynaTextField brokerIdTextField;
    private DynaTextField descriptionTextField;
    private DynaTextField baseDirTextField;
    private DynaTextField cdateTextField;
    private DynaTextField lmdateTextField;
    private JLabel dummyLabel;
    private JToolBar buttonToolBar;
    private JButton okButton;
    private JButton pwChangeButton;
    private final int titleTextWidth = 150;
    private boolean groupInfoMode;
    private DSSManager parent;
}
