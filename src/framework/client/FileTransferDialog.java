// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FileTransferDialog.java

package dyna.framework.client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class FileTransferDialog extends JDialog
{

    public FileTransferDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
    }

    private void initComponents()
    {
        transferBar = new JProgressBar();
        sizeLabel = new JLabel();
        setTitle("File Transfer");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                closeDialog(evt);
            }

        });
        transferBar.setPreferredSize(new Dimension(300, 50));
        transferBar.setStringPainted(true);
        getContentPane().add(transferBar, "Center");
        sizeLabel.setHorizontalAlignment(2);
        getContentPane().add(sizeLabel, "South");
        pack();
    }

    private void closeDialog(WindowEvent evt)
    {
        setVisible(false);
        dispose();
    }

    public void addSize(int size)
    {
        int oldSize = transferBar.getValue();
        transferBar.setValue(oldSize + size);
        sizeLabel.setText(Integer.toString((oldSize + size) / 1024) + " KB / " + Integer.toString(transferBar.getMaximum() / 1024) + " KB");
        if(!isVisible())
            setVisible(true);
    }

    public void setMaximumSize(int size)
    {
        transferBar.setMaximum(size);
        transferBar.setMinimum(0);
        transferBar.setValue(0);
    }

    private JProgressBar transferBar;
    private JLabel sizeLabel;

}