// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SampleEditor.java

package dyna.uic.graph;

import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;

// Referenced classes of package dyna.uic.graph:
//            Graph

public class SampleEditor extends JFrame
{

    public SampleEditor()
    {
        graph = null;
        initComponents();
        graph = new Graph();
        getContentPane().add(graph, "Center");
        jComboBox1.addItem("Select");
        jComboBox1.addItem("Add");
        jComboBox1.addItem("Connect");
        pack();
    }

    private void initComponents()
    {
        jPanel1 = new JPanel();
        jComboBox1 = new JComboBox();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

        });
        jPanel1.setLayout(new BoxLayout(jPanel1, 0));
        jComboBox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                jComboBox1ActionPerformed(evt);
            }

        });
        jPanel1.add(jComboBox1);
        jButton1.setText("jButton1");
        jPanel1.add(jButton1);
        jButton2.setText("jButton2");
        jPanel1.add(jButton2);
        jButton3.setText("jButton3");
        jButton3.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt)
            {
                jButton3MousePressed(evt);
            }

            public void mouseReleased(MouseEvent evt)
            {
                jButton3MouseReleased(evt);
            }

        });
        jButton3.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent evt)
            {
                jButton3MouseDragged(evt);
            }

        });
        jPanel1.add(jButton3);
        getContentPane().add(jPanel1, "North");
    }

    private void jButton3MouseDragged(MouseEvent mouseevent)
    {
    }

    private void jButton3MouseReleased(MouseEvent mouseevent)
    {
    }

    private void jButton3MousePressed(MouseEvent mouseevent)
    {
    }

    private void jComboBox1ActionPerformed(ActionEvent evt)
    {
        graph.setMode(jComboBox1.getSelectedIndex());
    }

    private void exitForm(WindowEvent evt)
    {
        System.exit(0);
    }

    public static void main(String args[])
    {
        (new SampleEditor()).show();
    }

    Graph graph;
    private JPanel jPanel1;
    private JComboBox jComboBox1;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;





}
