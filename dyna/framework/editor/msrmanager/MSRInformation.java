// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MSRInformation.java

package dyna.framework.editor.msrmanager;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTextField;
import dyna.uic.Table;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.msrmanager:
//            MSRManager

public class MSRInformation extends JPanel
    implements ActionListener
{

    public MSRInformation(MSRManager parent, boolean mode)
    {
        aus = null;
        dss = null;
        msr = null;
        mainBorderLayout = null;
        mainPanel = null;
        localeTextField = null;
        idTextField = null;
        strValueTextField = null;
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
        aus = MSRManager.aus;
        dss = MSRManager.dss;
        msr = MSRManager.msr;
        initialize();
        setFieldEnabled(false);
    }

    public void initialize()
    {
        setSize(new Dimension(600, 400));
        localeTextField = new DynaTextField();
        localeTextField.setMandatory(true);
        localeTextField.setTitleText("Locale");
        localeTextField.setVisible(true);
        localeTextField.setEditable(false);
        localeTextField.setTitleWidth(150);
        localeTextField.setTitleVisible(true);
        idTextField = new DynaTextField();
        idTextField.setMandatory(false);
        idTextField.setTitleText("Id");
        idTextField.setVisible(true);
        idTextField.setTitleWidth(150);
        idTextField.setTitleVisible(true);
        smallSearchButton = new JButton();
        smallSearchButton.setIcon(new ImageIcon(getClass().getResource("/icons/SmallSearchButton.gif")));
        smallSearchButton.setMargin(new Insets(0, 0, 0, 0));
        smallSearchButton.addActionListener(this);
        if(groupInfoMode)
            smallSearchButton.setEnabled(false);
        else
            smallSearchButton.setEnabled(true);
        strValueTextField = new DynaTextField();
        strValueTextField.setMandatory(false);
        strValueTextField.setTitleText("String value");
        strValueTextField.setTitleWidth(150);
        strValueTextField.setTitleVisible(true);
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
        gridBag.setConstraints(localeTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(idTextField, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 1;
        gridBag.setConstraints(strValueTextField, gridBagCon);
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
        mainPanel.add(localeTextField);
        mainPanel.add(idTextField);
        mainPanel.add(strValueTextField);
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
        localeTextField.setText((String)dssInfo.get("loc"));
        idTextField.setText((String)dssInfo.get("id"));
        strValueTextField.setText((String)dssInfo.get("stg"));
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
        String storageId = idTextField.getText();
        String path = localeTextField.getText();
        try
        {
            if(!groupInfoMode)
            {
                DOSChangeable stgRepDefinition = new DOSChangeable();
                stgRepDefinition.put("loc", localeTextField.getText());
                stgRepDefinition.put("id", idTextField.getText());
                stgRepDefinition.put("stg", strValueTextField.getText());
                msr.createStgRep(stgRepDefinition);
                parent.setTableData();
                parent.getTable().changeTableData();
            } else
            {
                DOSChangeable stgRepDefinition = new DOSChangeable();
                stgRepDefinition.put("loc", localeTextField.getText());
                stgRepDefinition.put("id", idTextField.getText());
                stgRepDefinition.put("stg", strValueTextField.getText());
                msr.setStgrep(stgRepDefinition);
                parent.setTableData();
                parent.getTable().changeTableData();
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public void setLocaleField(String locale)
    {
        localeTextField.setText(locale);
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        String command = actionEvent.getActionCommand();
        if(command.equals("Ok"))
        {
            String storageId = localeTextField.getText();
            String path = idTextField.getText();
            try
            {
                if(!groupInfoMode && !Utils.isNullString(storageId) && !Utils.isNullString(path))
                    dss.mountStorage(storageId, path);
            }
            catch(IIPRequestException ie)
            {
                System.err.println(ie);
            }
        }
    }

    private AUS aus;
    private DSS dss;
    private MSR msr;
    private BorderLayout mainBorderLayout;
    private JPanel mainPanel;
    private DynaTextField localeTextField;
    public DynaTextField idTextField;
    private DynaTextField strValueTextField;
    private DynaTextField cdateTextField;
    private DynaTextField lmdateTextField;
    private JLabel dummyLabel;
    private JToolBar buttonToolBar;
    private JButton okButton;
    private JButton pwChangeButton;
    private JButton smallSearchButton;
    private final int titleTextWidth = 150;
    private boolean groupInfoMode;
    private MSRManager parent;
}
