// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CodeInformation.java

package dyna.framework.editor.modeler;

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

// Referenced classes of package dyna.framework.editor.modeler:
//            TypeClassSelection

public class CodeInformation extends JPanel
    implements ActionListener
{
    class DynaComboBooleanInstance extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            return setBooleanComboBox();
        }

        DynaComboBooleanInstance()
        {
        }
    }

    class VisualTypeInstance extends DynaComboBoxDataLoader
    {

        public int getDataIndex()
        {
            return 1;
        }

        public int getOIDIndex()
        {
            return 0;
        }

        public ArrayList invokeLoader()
        {
            ArrayList aList = new ArrayList();
            ArrayList returnList = new ArrayList();
            aList.add(new Integer(1));
            aList.add("Button");
            returnList.add(aList.clone());
            aList.clear();
            aList.add(new Integer(2));
            aList.add("ComboBox");
            returnList.add(aList.clone());
            aList.clear();
            return returnList;
        }

        VisualTypeInstance()
        {
        }
    }


    public CodeInformation(DOS dos, String modelOuid)
    {
        handCursor = new Cursor(12);
        this.dos = null;
        this.modelOuid = "";
        codeOuid = "";
        classOuid = null;
        isAutoGenComboDataLoader = new DynaComboBooleanInstance();
        visualTypeDataLoader = new VisualTypeInstance();
        isAutoGenComboModel = new DynaComboBoxModel(isAutoGenComboDataLoader);
        visualTypeModel = new DynaComboBoxModel(visualTypeDataLoader);
        try
        {
            this.dos = dos;
            this.modelOuid = modelOuid;
            initialize();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void initialize()
    {
        GridBagLayout codeInfoGrdBgLyt = new GridBagLayout();
        GridBagConstraints codeInfoGrdBgConst = new GridBagConstraints();
        codeInfoGrdBgConst.fill = 1;
        codeInfoGrdBgConst.anchor = 11;
        setLayout(codeInfoGrdBgLyt);
        nameLabel = new JLabel("Name", new ImageIcon(getClass().getResource("/icons/Mandatory16.gif")), 2);
        nameLabel.setVerticalTextPosition(0);
        nameLabel.setHorizontalTextPosition(2);
        codeInfoGrdBgConst.insets = new Insets(5, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 0;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(nameLabel, codeInfoGrdBgConst);
        nameTextField = new JTextField();
        nameTextField.setEditable(true);
        codeInfoGrdBgConst.insets = new Insets(5, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 0;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(nameTextField, codeInfoGrdBgConst);
        descLabel = new JLabel("Description");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.fill = 0;
        codeInfoGrdBgConst.anchor = 18;
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 1;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 2;
        codeInfoGrdBgLyt.setConstraints(descLabel, codeInfoGrdBgConst);
        descScrPane = new JScrollPane();
        descTextArea = new JTextArea();
        descScrPane.getViewport().add(descTextArea, null);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.fill = 1;
        codeInfoGrdBgConst.anchor = 11;
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.10000000000000001D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 1;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 2;
        codeInfoGrdBgLyt.setConstraints(descScrPane, codeInfoGrdBgConst);
        isAutoGenLabel = new JLabel("is Autogeneratable", new ImageIcon(getClass().getResource("/icons/Mandatory16.gif")), 2);
        isAutoGenLabel.setVerticalTextPosition(0);
        isAutoGenLabel.setHorizontalTextPosition(2);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 3;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(isAutoGenLabel, codeInfoGrdBgConst);
        isAutoGenComboBox = new DynaComboBox();
        isAutoGenComboBox.setEditable(false);
        isAutoGenComboBox.setModel(isAutoGenComboModel);
        isAutoGenComboModel.enableDataLoad();
        codeInfoGrdBgConst.insets = new Insets(0, 0, 0, 0);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 3;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(isAutoGenComboBox, codeInfoGrdBgConst);
        prefixLabel = new JLabel("Prefix");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 4;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(prefixLabel, codeInfoGrdBgConst);
        prefixTextField = new JTextField();
        prefixTextField.setEditable(true);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 4;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgLyt.setConstraints(prefixTextField, codeInfoGrdBgConst);
        suffixLenLabel = new JLabel("Suffix length");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 5;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(suffixLenLabel, codeInfoGrdBgConst);
        suffixLenTextField = new JTextField();
        suffixLenTextField.setEditable(true);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 5;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(suffixLenTextField, codeInfoGrdBgConst);
        iniValLabel = new JLabel("Suffix initvalue");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 6;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(iniValLabel, codeInfoGrdBgConst);
        iniValTextField = new JTextField();
        iniValTextField.setEditable(true);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 6;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(iniValTextField, codeInfoGrdBgConst);
        incLabel = new JLabel("Suffix increment");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 7;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(incLabel, codeInfoGrdBgConst);
        incTextField = new JTextField();
        incTextField.setEditable(true);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 7;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(incTextField, codeInfoGrdBgConst);
        visualTypeLabel = new JLabel("Visual Type", new ImageIcon(getClass().getResource("/icons/Mandatory16.gif")), 2);
        visualTypeLabel.setVerticalTextPosition(0);
        visualTypeLabel.setHorizontalTextPosition(2);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 8;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(visualTypeLabel, codeInfoGrdBgConst);
        visualTypeComboBox = new DynaComboBox();
        visualTypeComboBox.setEditable(false);
        visualTypeComboBox.setModel(visualTypeModel);
        visualTypeModel.enableDataLoad();
        codeInfoGrdBgConst.insets = new Insets(0, 0, 0, 0);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 8;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(visualTypeComboBox, codeInfoGrdBgConst);
        isHierarchyCheckBox = new JCheckBox("Hierarchy Code");
        isHierarchyCheckBox.addActionListener(this);
        isHierarchyCheckBox.setActionCommand("is.hierarchy");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 9;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(isHierarchyCheckBox, codeInfoGrdBgConst);
        isNavigatorCheckBox = new JCheckBox("Navigator View");
        isNavigatorCheckBox.setEnabled(false);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 10;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(isNavigatorCheckBox, codeInfoGrdBgConst);
        isIndirectCheckBox = new JCheckBox("Indirect");
        isIndirectCheckBox.setEnabled(false);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 11;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(isIndirectCheckBox, codeInfoGrdBgConst);
        filterLabel = new JLabel("Class/Filter");
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 12;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(filterLabel, codeInfoGrdBgConst);
        filterTextField = new JTextField();
        filterTextField.setEditable(false);
        codeInfoGrdBgConst.insets = new Insets(2, 5, 0, 5);
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 1;
        codeInfoGrdBgConst.gridy = 12;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(filterTextField, codeInfoGrdBgConst);
        classSelectButton = new JButton();
        classSelectButton.setEnabled(false);
        classSelectButton.setIcon(new ImageIcon(getClass().getResource("/icons/Open.gif")));
        classSelectButton.setMargin(new Insets(0, 0, 0, 0));
        classSelectButton.setActionCommand("classSelectButton");
        classSelectButton.addActionListener(this);
        codeInfoGrdBgConst.insets = new Insets(2, 0, 0, 5);
        codeInfoGrdBgConst.weightx = 0.0D;
        codeInfoGrdBgConst.weighty = 0.0D;
        codeInfoGrdBgConst.gridx = 2;
        codeInfoGrdBgConst.gridy = 12;
        codeInfoGrdBgConst.gridwidth = 1;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(classSelectButton, codeInfoGrdBgConst);
        dummyLabel = new JLabel("");
        codeInfoGrdBgConst.weightx = 1.0D;
        codeInfoGrdBgConst.weighty = 0.90000000000000002D;
        codeInfoGrdBgConst.gridx = 0;
        codeInfoGrdBgConst.gridy = 13;
        codeInfoGrdBgConst.gridwidth = 2;
        codeInfoGrdBgConst.gridheight = 1;
        codeInfoGrdBgLyt.setConstraints(dummyLabel, codeInfoGrdBgConst);
        add(nameLabel);
        add(nameTextField);
        add(descLabel);
        add(descScrPane);
        add(isAutoGenLabel);
        add(isAutoGenComboBox);
        add(prefixLabel);
        add(prefixTextField);
        add(suffixLenLabel);
        add(suffixLenTextField);
        add(iniValLabel);
        add(iniValTextField);
        add(incLabel);
        add(incTextField);
        add(visualTypeLabel);
        add(visualTypeComboBox);
        add(isHierarchyCheckBox);
        add(isNavigatorCheckBox);
        add(isIndirectCheckBox);
        add(filterLabel);
        add(filterTextField);
        add(classSelectButton);
        add(dummyLabel);
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

    public void setCodeInfoField(DOSChangeable codeInfoDC)
    {
        isHierarchyCheckBox.setSelected(false);
        isNavigatorCheckBox.setSelected(false);
        isIndirectCheckBox.setSelected(false);
        filterTextField.setText("");
        visualTypeComboBox.setSelectedIndex(-1);
        if(codeInfoDC == null)
        {
            codeOuid = null;
            nameTextField.setText("");
            descTextArea.setText("");
            isAutoGenComboModel.setElementAt(-1);
            prefixTextField.setText("");
            suffixLenTextField.setText("");
            iniValTextField.setText("");
            incTextField.setText("");
        } else
        {
            codeOuid = (String)codeInfoDC.get("ouid");
            nameTextField.setText((String)codeInfoDC.get("name"));
            descTextArea.setText((String)codeInfoDC.get("description"));
            isAutoGenComboModel.setElementAt(-1);
            if(codeInfoDC.get("is.autogeneratable") != null)
                isAutoGenComboModel.setSelectedItem(Utils.getBoolean((Boolean)codeInfoDC.get("is.autogeneratable")) ? "true" : "false");
            prefixTextField.setText((String)codeInfoDC.get("prefix"));
            if(codeInfoDC.get("suffixlength") != null)
                suffixLenTextField.setText(((Integer)codeInfoDC.get("suffixlength")).toString());
            else
                suffixLenTextField.setText("0");
            if(codeInfoDC.get("initvalue") != null)
                iniValTextField.setText(((Integer)codeInfoDC.get("initvalue")).toString());
            else
                iniValTextField.setText("0");
            if(codeInfoDC.get("increment") != null)
                incTextField.setText(((Integer)codeInfoDC.get("increment")).toString());
            else
                incTextField.setText("0");
            if(codeInfoDC.get("visual.type") != null)
                visualTypeModel.setSelectedItemByOID(codeInfoDC.get("visual.type"));
            if(codeInfoDC.get("is.hierarchy") != null && Utils.getBoolean((Boolean)codeInfoDC.get("is.hierarchy")))
                isHierarchyCheckBox.setSelected(true);
            if(codeInfoDC.get("is.navigator") != null && Utils.getBoolean((Boolean)codeInfoDC.get("is.navigator")))
                isNavigatorCheckBox.setSelected(true);
            if(codeInfoDC.get("is.indirect") != null && Utils.getBoolean((Boolean)codeInfoDC.get("is.indirect")))
                isIndirectCheckBox.setSelected(true);
            if(isHierarchyCheckBox.isSelected())
            {
                isNavigatorCheckBox.setEnabled(true);
                isIndirectCheckBox.setEnabled(true);
                classSelectButton.setEnabled(true);
                if(!isIndirectCheckBox.isSelected())
                    try
                    {
                        classOuid = (String)codeInfoDC.get("filter");
                        DOSChangeable classData = dos.getClass(classOuid);
                        if(classData == null)
                        {
                            classOuid = null;
                            filterTextField.setText("");
                            return;
                        }
                        filterTextField.setText((String)classData.get("name") + " [" + classOuid + "]");
                    }
                    catch(IIPRequestException e)
                    {
                        e.printStackTrace();
                    }
            } else
            {
                isNavigatorCheckBox.setEnabled(false);
                isIndirectCheckBox.setEnabled(false);
                isNavigatorCheckBox.setSelected(false);
                isIndirectCheckBox.setSelected(false);
                classSelectButton.setEnabled(false);
            }
        }
    }

    public DOSChangeable createCodeInformation()
    {
        DOSChangeable codeInfoDC = null;
        try
        {
            codeInfoDC = new DOSChangeable();
            if(!codePreCheck(nameTextField.getText()))
            {
                JOptionPane.showMessageDialog(this, "\uC774\uBBF8 \uC874\uC7AC\uD569\uB2C8\uB2E4.", "ERROR", 0);
                return null;
            }
            codeInfoDC.put("name", nameTextField.getText().toUpperCase());
            codeInfoDC.put("description", descTextArea.getText());
            codeInfoDC.put("ouid@model", modelOuid);
            codeInfoDC.put("is.autogeneratable", (Boolean)isAutoGenComboModel.getSelectedOID());
            if(((Boolean)isAutoGenComboModel.getSelectedOID()).booleanValue())
            {
                codeInfoDC.put("prefix", prefixTextField.getText().toUpperCase());
                codeInfoDC.put("suffixlength", Integer.valueOf(suffixLenTextField.getText()));
                codeInfoDC.put("initvalue", Integer.valueOf(iniValTextField.getText()));
                codeInfoDC.put("increment", Integer.valueOf(incTextField.getText()));
            } else
            {
                codeInfoDC.put("prefix", "");
                codeInfoDC.put("suffixlength", new Integer(0));
                codeInfoDC.put("initvalue", new Integer(0));
                codeInfoDC.put("increment", new Integer(0));
            }
            if(visualTypeModel.getSelectedOID() != null)
                codeInfoDC.put("visual.type", visualTypeModel.getSelectedOID());
            codeInfoDC.put("is.hierarchy", new Boolean(isHierarchyCheckBox.isSelected()));
            codeInfoDC.put("is.navigator", new Boolean(isNavigatorCheckBox.isSelected()));
            codeInfoDC.put("is.indirect", new Boolean(isIndirectCheckBox.isSelected()));
            if(!isIndirectCheckBox.isSelected() && classOuid != null)
                codeInfoDC.put("filter", classOuid);
            String resultOuid = dos.createCode(modelOuid, codeInfoDC);
            codeOuid = resultOuid;
            codeInfoDC.put("ouid", codeOuid);
            return codeInfoDC;
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
        return null;
    }

    public boolean codePreCheck(String name)
    {
        boolean rvalue = true;
        try
        {
            ArrayList codes = dos.listCode(modelOuid);
            if(codes == null || codes.size() <= 0)
            {
                rvalue = true;
            } else
            {
                for(int i = 0; i < codes.size(); i++)
                {
                    System.out.println(codes.get(i));
                    String str = (String)((DOSChangeable)codes.get(i)).get("name");
                    if(!name.toUpperCase().equals(str.toUpperCase()))
                        continue;
                    rvalue = false;
                    break;
                }

            }
        }
        catch(IIPRequestException ie)
        {
            rvalue = false;
            System.out.println(ie);
        }
        return rvalue;
    }

    public void setCodeInformation()
    {
        try
        {
            if(codeOuid != null && !Utils.isNullString(codeOuid))
            {
                ArrayList codeItemList = dos.listCodeItem(codeOuid);
                if(codeItemList != null && codeItemList.size() > 0)
                {
                    Object option[] = {
                        "\uC608", "\uC544\uB2C8\uC624"
                    };
                    int res = JOptionPane.showOptionDialog(this, "\uD574\uB2F9 Code\uC758 CodeItem\uC774 \uC874\uC7AC\uD569\uB2C8\uB2E4. \uBCC0\uACBD\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "Modify", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[1]);
                    if(res != 0)
                        return;
                }
                DOSChangeable codeInfoDC = new DOSChangeable();
                codeInfoDC.put("ouid", codeOuid);
                codeInfoDC.put("name", nameTextField.getText().toUpperCase());
                codeInfoDC.put("description", descTextArea.getText());
                codeInfoDC.put("ouid@model", modelOuid);
                codeInfoDC.put("is.autogeneratable", (Boolean)isAutoGenComboModel.getSelectedOID());
                if(((Boolean)isAutoGenComboModel.getSelectedOID()).booleanValue())
                {
                    codeInfoDC.put("prefix", prefixTextField.getText().toUpperCase());
                    codeInfoDC.put("suffixlength", Integer.valueOf(suffixLenTextField.getText()));
                    codeInfoDC.put("initvalue", Integer.valueOf(iniValTextField.getText()));
                    codeInfoDC.put("increment", Integer.valueOf(incTextField.getText()));
                } else
                {
                    codeInfoDC.put("prefix", null);
                    codeInfoDC.put("suffixlength", null);
                    codeInfoDC.put("initvalue", null);
                    codeInfoDC.put("increment", null);
                }
                if(visualTypeModel.getSelectedOID() != null)
                    codeInfoDC.put("visual.type", visualTypeModel.getSelectedOID());
                codeInfoDC.put("is.hierarchy", new Boolean(isHierarchyCheckBox.isSelected()));
                codeInfoDC.put("is.navigator", new Boolean(isNavigatorCheckBox.isSelected()));
                codeInfoDC.put("is.indirect", new Boolean(isIndirectCheckBox.isSelected()));
                if(!isIndirectCheckBox.isSelected() && classOuid != null)
                    codeInfoDC.put("filter", classOuid);
                dos.setCode(codeInfoDC);
            }
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    public boolean removeCodeInformation()
    {
        try
        {
            if(codeOuid != null && !Utils.isNullString(codeOuid))
            {
                ArrayList codeItemList = dos.listCodeItem(codeOuid);
                if(codeItemList != null && codeItemList.size() > 0)
                {
                    Object option[] = {
                        "\uC608", "\uC544\uB2C8\uC624"
                    };
                    int res = JOptionPane.showOptionDialog(this, "\uD574\uB2F9 Code\uC758 CodeItem\uC774 \uC874\uC7AC\uD569\uB2C8\uB2E4. \uBAA8\uB450 \uC0AD\uC81C\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?", "\uC0AD\uC81C", 0, 3, new ImageIcon(getClass().getResource("/icons/Question32.gif")), option, option[1]);
                    if(res != 0)
                        return false;
                    dos.removeCodeItemInCode(codeOuid);
                }
                dos.removeCode(codeOuid);
            }
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
            return false;
        }
        return true;
    }

    public String getCodeOuid()
    {
        return codeOuid;
    }

    public ArrayList setBooleanComboBox()
    {
        ArrayList booleanAL = new ArrayList();
        ArrayList returnAL = new ArrayList();
        booleanAL.add(new Boolean(true));
        booleanAL.add("true");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        booleanAL.add(new Boolean(false));
        booleanAL.add("false");
        returnAL.add(booleanAL.clone());
        booleanAL.clear();
        return returnAL;
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if(cmd.equals("is.hierarchy"))
        {
            if(isHierarchyCheckBox.isSelected())
            {
                isNavigatorCheckBox.setEnabled(true);
                isIndirectCheckBox.setEnabled(true);
                filterTextField.setEditable(true);
                classSelectButton.setEnabled(true);
            } else
            {
                isNavigatorCheckBox.setEnabled(false);
                isIndirectCheckBox.setEnabled(false);
                isNavigatorCheckBox.setSelected(false);
                isIndirectCheckBox.setSelected(false);
                filterTextField.setEditable(false);
                classSelectButton.setEnabled(false);
            }
        } else
        if(cmd.equals("classSelectButton"))
        {
            TypeClassSelection typeClassFrame = new TypeClassSelection(this, "Class Selection");
            typeClassFrame.setVisible(true);
        }
    }

    public void setClass(ArrayList classList)
    {
        if(Utils.isNullArrayList(classList))
            return;
        classOuid = (String)classList.get(0);
        try
        {
            DOSChangeable classData = dos.getClass(classOuid);
            if(classData == null)
            {
                classOuid = null;
                filterTextField.setText("");
                return;
            }
            filterTextField.setText((String)classData.get("name") + " [" + classOuid + "]");
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    private Cursor handCursor;
    private final int TITLE_WIDTH = 130;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel descLabel;
    private JTextArea descTextArea;
    private JScrollPane descScrPane;
    private JLabel isAutoGenLabel;
    private DynaComboBox isAutoGenComboBox;
    private JLabel prefixLabel;
    private JTextField prefixTextField;
    private JLabel suffixLenLabel;
    private JTextField suffixLenTextField;
    private JLabel iniValLabel;
    private JTextField iniValTextField;
    private JLabel incLabel;
    private JTextField incTextField;
    private JLabel dummyLabel;
    private JCheckBox isHierarchyCheckBox;
    private JCheckBox isNavigatorCheckBox;
    private JCheckBox isIndirectCheckBox;
    private JLabel filterLabel;
    private JTextField filterTextField;
    private JButton classSelectButton;
    private JLabel visualTypeLabel;
    private DynaComboBox visualTypeComboBox;
    private DOS dos;
    private String modelOuid;
    private String codeOuid;
    private String classOuid;
    private DynaComboBoxDataLoader isAutoGenComboDataLoader;
    private DynaComboBoxDataLoader visualTypeDataLoader;
    private DynaComboBoxModel isAutoGenComboModel;
    private DynaComboBoxModel visualTypeModel;
}
