// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessPanel.java

package dyna.framework.editor.workflow;

import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.workflow:
//            EventInformation

public class ProcessPanel extends JPanel
    implements ActionListener
{

    public ProcessPanel()
    {
        ouid = null;
        initComponents();
    }

    private void initComponents()
    {
        topPanel = new JPanel();
        tabPanel = new JPanel();
        tabTitlePanel = new JPanel();
        tabButtons = new JToggleButton[3];
        setLayout(new BorderLayout(0, 0));
        setFont(new Font("dialog", 0, 11));
        topPanel.setLayout(null);
        topPanel.setMaximumSize(new Dimension(500, 60));
        topPanel.setMinimumSize(new Dimension(500, 60));
        topPanel.setPreferredSize(new Dimension(500, 60));
        idLabel = new JLabel();
        idLabel.setText("Identifier");
        idLabel.setFont(getFont());
        idLabel.setBounds(10, 5, 80, 21);
        topPanel.add(idLabel);
        idTextField = new JTextField();
        idTextField.setBackground(Color.orange);
        idTextField.setBounds(90, 5, 140, 21);
        topPanel.add(idTextField);
        nameLabel = new JLabel();
        nameLabel.setText("Name");
        nameLabel.setFont(getFont());
        nameLabel.setBounds(10, 31, 80, 21);
        topPanel.add(nameLabel);
        nameTextField = new JTextField();
        nameTextField.setBounds(90, 31, 140, 21);
        topPanel.add(nameTextField);
        responsibleLabel = new JLabel();
        responsibleLabel.setText("Responsible");
        responsibleLabel.setFont(getFont());
        responsibleLabel.setBounds(250, 5, 80, 21);
        topPanel.add(responsibleLabel);
        responsibleTextField = new JTextField();
        responsibleTextField.setBounds(340, 5, 140, 21);
        topPanel.add(responsibleTextField);
        statusLabel = new JLabel();
        statusLabel.setText("Status");
        statusLabel.setFont(getFont());
        statusLabel.setBounds(250, 31, 80, 21);
        statusTextField = new JTextField();
        statusTextField.setEnabled(false);
        statusTextField.setBounds(340, 31, 140, 21);
        add(topPanel, "North");
        tabPanel.setLayout(new BorderLayout(0, 0));
        add(tabPanel, "Center");
        tabTitlePanel.setLayout(new FlowLayout(0, 0, 0));
        Insets tabInsets = new Insets(1, 5, 1, 5);
        for(i = 0; i < 3; i++)
        {
            tabButtons[i] = new JToggleButton();
            tabButtons[i].setFont(getFont());
            tabButtons[i].setHorizontalAlignment(2);
            tabButtons[i].setMargin(tabInsets);
            tabButtons[i].setSelected(true);
            tabButtons[i].addActionListener(this);
            tabTitlePanel.add(tabButtons[i]);
        }

        tabButtons[0].setText("Attribute");
        tabButtons[0].setActionCommand("attribute");
        tabButtons[1].setText("Parameter");
        tabButtons[1].setActionCommand("parameter");
        tabButtons[1].setEnabled(false);
        tabButtons[2].setText("Event");
        tabButtons[2].setActionCommand("event");
        tabPanel.add(tabTitlePanel, "North");
        tabButtons[0].doClick();
    }

    private JPanel getAttributePanel()
    {
        if(attributePanel != null)
            return attributePanel;
        attributePanel = new JPanel();
        attributePanel.setLayout(null);
        attributePanel.setBorder(BorderFactory.createEtchedBorder());
        attributeLabels = new JLabel[attributeNames.length];
        attributeTextFields = new JTextField[attributeNames.length];
        for(i = 0; i < attributeNames.length; i++)
        {
            attributeLabels[i] = new JLabel();
            attributeLabels[i].setFont(getFont());
            attributeLabels[i].setText(attributeNames[i][0]);
            attributeLabels[i].setHorizontalAlignment(4);
            attributeLabels[i].setBounds(0, 5 + i * 26, 140, 21);
            attributePanel.add(attributeLabels[i]);
            attributeTextFields[i] = new JTextField();
            attributeTextFields[i].setBounds(150, 5 + i * 26, 300, 21);
            if(attributeNames[i][2] != null)
            {
                if(attributeNames[i][2].indexOf("mandatory") >= 0)
                    attributeTextFields[i].setBackground(Color.orange);
                if(attributeNames[i][2].indexOf("readonly") >= 0)
                    attributeTextFields[i].setEnabled(false);
            }
            attributePanel.add(attributeTextFields[i]);
        }

        int width = 480;
        int height = 5 + attributeNames.length * 26 + 5;
        Dimension dimension = new Dimension(width, height);
        attributePanel.setMinimumSize(dimension);
        attributePanel.setMaximumSize(dimension);
        attributePanel.setPreferredSize(dimension);
        return attributePanel;
    }

    private JPanel getParameterPanel()
    {
        if(parameterPanel != null)
        {
            return parameterPanel;
        } else
        {
            parameterPanel = new JPanel();
            System.out.println("parameter");
            return parameterPanel;
        }
    }

    private JPanel getEventPanel()
    {
        if(eventPanel != null)
        {
            return eventPanel;
        } else
        {
            eventPanel = new EventInformation(this);
            return eventPanel;
        }
    }

    public DOSChangeable getValue()
    {
        DOSChangeable dosObject = new DOSChangeable();
        String tempString1 = null;
        String tempString2 = null;
        dosObject.put("object.type", "process");
        dosObject.put("ouid", ouid);
        dosObject.put("identifier", idTextField.getText());
        dosObject.put("name", nameTextField.getText());
        dosObject.put("responsible", responsibleTextField.getText());
        dosObject.put("status", statusTextField.getText());
        for(i = 0; i < attributeNames.length; i++)
        {
            tempString1 = attributeNames[i][1];
            tempString2 = attributeTextFields[i].getText().trim();
            if(!Utils.isNullString(tempString2))
                if(tempString1.equals("limit") || tempString1.equals("priority"))
                    dosObject.put(tempString1, new Integer(tempString2));
                else
                    dosObject.put(tempString1, tempString2);
        }

        return dosObject;
    }

    public void setValue(DOSChangeable value)
    {
        String tempString1 = null;
        Object object = null;
        tempString1 = (String)value.get("object.type");
        if(Utils.isNullString(tempString1))
            return;
        if(!tempString1.equals("process"))
            return;
        ouid = (String)value.get("ouid");
        idTextField.setText((String)value.get("identifier"));
        nameTextField.setText((String)value.get("name"));
        responsibleTextField.setText((String)value.get("responsible"));
        statusTextField.setText((String)value.get("status"));
        for(i = 0; i < attributeNames.length; i++)
        {
            tempString1 = attributeNames[i][1];
            object = value.get(tempString1);
            if(object != null)
                attributeTextFields[i].setText(object.toString());
            else
                attributeTextFields[i].setText("");
            if(attributeNames[i][2] != null && attributeNames[i][2].indexOf("frozen") >= 0 && !Utils.isNullString(ouid))
                attributeTextFields[i].setEnabled(false);
        }

        if(eventPanel != null)
        {
            ((EventInformation)eventPanel).setOuid(ouid);
        } else
        {
            getEventPanel();
            if(eventPanel != null)
                ((EventInformation)eventPanel).setOuid(ouid);
        }
    }

    public void actionPerformed(ActionEvent evt)
    {
        String actionCommand = evt.getActionCommand();
        if(actionCommand.equals("attribute"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[0].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getAttributePanel();
            tabPanel.add(contentPanel, "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("parameter"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[1].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getParameterPanel();
            tabPanel.add(contentPanel, "Center");
            tabPanel.updateUI();
        } else
        if(actionCommand.equals("event"))
        {
            for(i = 0; i < tabButtons.length; i++)
                tabButtons[i].setSelected(true);

            tabButtons[2].setSelected(false);
            if(tabPanel.getComponentCount() > 1)
                tabPanel.remove(1);
            JPanel contentPanel = getEventPanel();
            tabPanel.add(contentPanel, "Center");
            tabPanel.updateUI();
        }
    }

    private int i;
    private String ouid;
    private JPanel topPanel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel responsibleLabel;
    private JLabel statusLabel;
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField responsibleTextField;
    private JTextField statusTextField;
    private JPanel tabPanel;
    private JPanel tabTitlePanel;
    private JToggleButton tabButtons[];
    private JPanel attributePanel;
    private JPanel activityPanel;
    private JPanel parameterPanel;
    private JPanel eventPanel;
    private JLabel attributeLabels[];
    private JTextField attributeTextFields[];
    private String attributeNames[][] = {
        {
            "Unit of duration", "duration.unit", 0
        }, {
            "Valid from", "date.valid.from", 0
        }, {
            "Valid to", "date.valid.to", 0
        }, {
            "Classification", "classification", 0
        }, {
            "Priority", "priority", 0
        }, {
            "Limit", "limit", 0
        }, {
            "Description", "description", 0
        }
    };
}
