// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SplashWindow.java

package dyna.uic;

import java.awt.*;
import javax.swing.*;

public class SplashWindow extends JWindow
{

    public SplashWindow(String filename, int waitTime)
    {
        closerRunner = null;
        waitRunner = null;
        status = null;
        getContentPane().setLayout(null);
        status = new JLabel("Loading...");
        status.setForeground(Color.white);
        status.setFont(new Font("dialog", 0, 12));
        getContentPane().add(status);
        JLabel image = new JLabel(new ImageIcon(filename));
        getContentPane().add(image);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = image.getPreferredSize();
        setLocation(screenSize.width / 2 - labelSize.width / 2, screenSize.height / 2 - labelSize.height / 2);
        setSize(labelSize);
        image.setBounds(0, 0, labelSize.width, labelSize.height);
        status.setBounds(10, labelSize.height - 60, labelSize.width - 10, 30);
        final int pause = waitTime;
        closerRunner = new Runnable() {

            public void run()
            {
                setVisible(false);
                dispose();
            }

        };
        waitRunner = new Runnable() {

            public void run()
            {
                try
                {
                    Thread.sleep(pause);
                    SwingUtilities.invokeAndWait(closerRunner);
                    closerRunner = null;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        };
        setVisible(true);
    }

    public void startClose()
    {
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
        waitRunner = null;
        splashThread = null;
    }

    public void setStatusText(String text)
    {
        if(text == null)
        {
            return;
        } else
        {
            status.setText(text);
            repaint();
            return;
        }
    }

    private Runnable closerRunner;
    private Runnable waitRunner;
    public JLabel status;


}
