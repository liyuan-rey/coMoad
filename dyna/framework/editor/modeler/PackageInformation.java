// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PackageInformation.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTextField;
import dyna.util.Utils;
import java.awt.*;
import java.io.PrintStream;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction

public class PackageInformation extends JPanel
{

    public PackageInformation(Object parentFrame)
    {
        dos = null;
        setInfoDC = null;
        try
        {
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            parent = parentFrame;
            initialize();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void initialize()
    {
        packageInfoBorderLayout = new BorderLayout();
        setLayout(packageInfoBorderLayout);
        mainScrPane = UIFactory.createStrippedScrollPane(null);
        mainPanel = new JPanel();
        descPanel = new JPanel();
        GridBagLayout gridBag1 = new GridBagLayout();
        GridBagConstraints gridBagCon1 = new GridBagConstraints();
        gridBagCon1.fill = 1;
        gridBagCon1.anchor = 11;
        descPanel.setLayout(gridBag1);
        descPanel.setBorder(BorderFactory.createEmptyBorder());
        descriptionLabel = new JLabel("Description");
        gridBagCon1.insets = new Insets(0, 5, 0, 15);
        gridBagCon1.weightx = 0.0D;
        gridBagCon1.weighty = 0.0D;
        gridBagCon1.gridx = 0;
        gridBagCon1.gridy = 0;
        gridBagCon1.gridwidth = 1;
        gridBag1.setConstraints(descriptionLabel, gridBagCon1);
        descScrPane = new JScrollPane();
        descriptionTextArea = new JTextArea();
        gridBagCon1.insets = new Insets(0, 40, 0, 5);
        gridBagCon1.weightx = 1.0D;
        gridBagCon1.weighty = 0.10000000000000001D;
        gridBagCon1.gridx = 1;
        gridBagCon1.gridy = 0;
        gridBagCon1.gridwidth = 2;
        gridBagCon1.gridheight = 2;
        gridBag1.setConstraints(descScrPane, gridBagCon1);
        descScrPane.getViewport().add(descriptionTextArea, null);
        descPanel.add(descriptionLabel, null);
        descPanel.add(descScrPane, null);
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints gridBagCon = new GridBagConstraints();
        gridBagCon.fill = 1;
        gridBagCon.anchor = 11;
        mainPanel.setLayout(gridBag);
        ouidTextField = new DynaTextField();
        ouidTextField.setMandatory(true);
        ouidTextField.setTitleText("Ouid");
        ouidTextField.setTitleWidth(120);
        ouidTextField.setTitleVisible(true);
        ouidTextField.setEditable(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 0;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(ouidTextField, gridBagCon);
        nameTextField = new DynaTextField();
        nameTextField.setMandatory(true);
        nameTextField.setTitleText("Name");
        nameTextField.setTitleWidth(120);
        nameTextField.setTitleVisible(true);
        nameTextField.setEditable(true);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 1;
        gridBagCon.gridwidth = 2;
        gridBag.setConstraints(nameTextField, gridBagCon);
        globalCheckBox = new JCheckBox("Global");
        globalCheckBox.setMargin(new Insets(0, 125, 0, 0));
        globalCheckBox.setEnabled(false);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 2;
        gridBagCon.gridwidth = 0;
        gridBag.setConstraints(globalCheckBox, gridBagCon);
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 0.20000000000000001D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 3;
        gridBagCon.gridwidth = 2;
        gridBagCon.gridheight = 2;
        gridBag.setConstraints(descPanel, gridBagCon);
        dummyLabel = new JLabel("");
        gridBagCon.weightx = 1.0D;
        gridBagCon.weighty = 1.0D;
        gridBagCon.gridx = 0;
        gridBagCon.gridy = 5;
        gridBagCon.gridwidth = 1;
        gridBagCon.gridheight = 4;
        gridBag.setConstraints(dummyLabel, gridBagCon);
        mainPanel.add(ouidTextField);
        mainPanel.add(nameTextField);
        mainPanel.add(globalCheckBox);
        mainPanel.add(descPanel);
        mainPanel.add(dummyLabel);
        mainScrPane.getViewport().add(mainPanel, null);
        add(mainScrPane, "Center");
    }

    public void setPackageInfoField(DOSChangeable dosPackageInfo)
    {
        setInfoDC = dosPackageInfo;
        ouidTextField.setText((String)dosPackageInfo.get("ouid"));
        nameTextField.setText((String)dosPackageInfo.get("name"));
        globalCheckBox.setSelected(Utils.getBoolean((Boolean)dosPackageInfo.get("isGlobal")));
        descriptionTextArea.setText((String)dosPackageInfo.get("description"));
    }

    public void setInformation()
    {
        try
        {
            setInfoDC.put("ouid", ouidTextField.getText());
            setInfoDC.put("name", nameTextField.getText());
            setInfoDC.put("isGlobal", new Boolean(globalCheckBox.isSelected()));
            setInfoDC.put("description", descriptionTextArea.getText());
            dos.setPackage(setInfoDC);
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public DOSChangeable createInformation()
    {
        try
        {
            String ouid = "";
            setInfoDC = new DOSChangeable();
            setInfoDC.put("ouid", ouidTextField.getText());
            setInfoDC.put("name", nameTextField.getText());
            setInfoDC.put("isGlobal", new Boolean(globalCheckBox.isSelected()));
            setInfoDC.put("description", descriptionTextArea.getText());
            ouid = dos.createPackage(setInfoDC);
            setInfoDC.put("ouid", ouid);
            return setInfoDC;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        return null;
    }

    private final int titleTextWidth = 120;
    private BorderLayout packageInfoBorderLayout;
    private BoxLayout packageInfoLayout;
    private Object parent;
    private JScrollPane mainScrPane;
    private JScrollPane descScrPane;
    private JPanel mainPanel;
    private JPanel descPanel;
    private JLabel dummyLabel;
    private JLabel descriptionLabel;
    private DynaTextField ouidTextField;
    private DynaTextField nameTextField;
    private JCheckBox globalCheckBox;
    private JTextArea descriptionTextArea;
    private DOS dos;
    private DOSChangeable setInfoDC;
}
