// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TemplateCloneWindow.java

package dyna.framework.client.pms;

import dyna.framework.client.DynaMOAD;
import dyna.framework.service.DOS;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.client.pms:
//            TemplateSelectDialog

public class TemplateCloneWindow extends JFrame
    implements ActionListener, WindowListener
{

    public TemplateCloneWindow()
    {
        projectOuid = null;
        templateOuid = null;
        projectNameLabel = null;
        projectNameText = null;
        templateLabel = null;
        templateText = null;
        templateButton = null;
        progressBar = null;
        cloneButton = null;
        closeButton = null;
        init();
    }

    private void init()
    {
        setTitle(DynaMOAD.getMSRString("WRD_0149", "Template", 0) + " " + DynaMOAD.getMSRString("WRD_0009", "Copy", 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/Template.gif"));
        addWindowListener(this);
        Container cp = getContentPane();
        cp.setLayout(null);
        int titleWidth = 110;
        int height = 22;
        int xOrigin = 10;
        int yOrigin = 10;
        int offset = 5;
        int i = 0;
        projectNameLabel = new JLabel("Project");
        projectNameLabel.setBounds(xOrigin, yOrigin + height * i, titleWidth, height);
        cp.add(projectNameLabel);
        projectNameText = new JTextField();
        projectNameText.setBounds(xOrigin + titleWidth, yOrigin + height * i, 300, height);
        projectNameText.setEditable(false);
        cp.add(projectNameText);
        i++;
        templateLabel = new JLabel(DynaMOAD.getMSRString("WRD_0149", "Template", 0));
        templateLabel.setBounds(xOrigin, yOrigin + (height + offset) * i, titleWidth, height);
        cp.add(templateLabel);
        templateText = new JTextField();
        templateText.setBounds(xOrigin + titleWidth, yOrigin + (height + offset) * i, 283, height);
        templateText.setEditable(false);
        cp.add(templateText);
        templateButton = new JButton();
        templateButton.setBounds((xOrigin + titleWidth + 300) - 18, yOrigin + (height + offset) * i, 18, height);
        templateButton.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
        templateButton.setActionCommand("templateButton");
        templateButton.addActionListener(this);
        cp.add(templateButton);
        i += 5;
        progressBar = new JProgressBar();
        progressBar.setBounds(xOrigin, yOrigin + (height + offset) * i, titleWidth + 300, height);
        cp.add(progressBar);
        i++;
        cloneButton = new JButton(DynaMOAD.getMSRString("WRD_0009", "Copy", 0));
        cloneButton.setBounds((xOrigin + (titleWidth + 300)) - 100 - 100 - offset, yOrigin + (height + offset) * i, 100, height);
        cloneButton.setIcon(new ImageIcon("icons/Ok.gif"));
        cloneButton.setActionCommand("copy");
        cloneButton.addActionListener(this);
        cp.add(cloneButton);
        closeButton = new JButton(DynaMOAD.getMSRString("WRD_0012", "Close", 0));
        closeButton.setBounds((xOrigin + (titleWidth + 300)) - 100, yOrigin + (height + offset) * i, 100, height);
        closeButton.setIcon(new ImageIcon("icons/Cancel.gif"));
        closeButton.setActionCommand("close");
        closeButton.addActionListener(this);
        cp.add(closeButton);
        setSize(440, 270);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setLocation(screenSize.width / 2 - windowSize.width / 2, screenSize.height / 2 - windowSize.height / 2);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if("templateButton".equals(command))
        {
            TemplateSelectDialog tsd = new TemplateSelectDialog(this);
            tsd.setVisible(true);
        } else
        if("copy".equals(command))
            cloneTemplate();
        else
        if("close".equals(command))
            windowClosing(null);
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent e)
    {
        removeWindowListener(this);
        dispose();
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public void setProject(String ouid, String name)
    {
        projectOuid = ouid;
        projectNameText.setText(name);
        projectNameText.setCaretPosition(0);
    }

    public void setTemplate(String ouid, String name)
    {
        templateOuid = ouid;
        templateText.setText(name);
        templateText.setCaretPosition(0);
    }

    private synchronized void cloneTemplate()
    {
        if(Utils.isNullString(projectOuid) || Utils.isNullString(templateOuid))
            return;
        try
        {
            ArrayList list = DynaMOAD.dos.listLinkFrom(templateOuid);
            if(Utils.isNullArrayList(list))
                return;
            progressBar.setMaximum(list.size() + 1);
            progressBar.setStringPainted(true);
            HashMap valueMap = new HashMap();
            String newOuid = null;
            ArrayList tempList = null;
            Iterator listKey;
            for(listKey = list.iterator(); listKey.hasNext();)
            {
                tempList = (ArrayList)listKey.next();
                if(!Utils.isNullArrayList(tempList))
                {
                    progressBar.setValue(progressBar.getValue() + 1);
                    valueMap = DynaMOAD.dos.cloneObject((String)tempList.get(0), projectOuid, valueMap);
                    newOuid = (String)valueMap.get("ouid");
                    if(!Utils.isNullString(newOuid))
                        tempList = null;
                }
            }

            listKey = null;
            progressBar.setValue(progressBar.getMaximum());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private String projectOuid;
    private String templateOuid;
    JLabel projectNameLabel;
    JTextField projectNameText;
    JLabel templateLabel;
    JTextField templateText;
    JButton templateButton;
    JProgressBar progressBar;
    JButton cloneButton;
    JButton closeButton;
}
