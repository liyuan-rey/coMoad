// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:36
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StructureCompareWindow.java

package dyna.framework.client;

import java.awt.*;
import javax.swing.JFrame;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, StructureComparePanel

public class StructureCompareWindow extends JFrame
{

    public StructureCompareWindow(String leftOuid, String rightOuid)
    {
        mainPanel = null;
        this.leftOuid = null;
        this.rightOuid = null;
        this.leftOuid = leftOuid;
        this.rightOuid = rightOuid;
        initialize();
    }

    private void initialize()
    {
        setTitle(DynaMOAD.getMSRString("MNU_COMPARE", "Compare with", 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage("icons/compare_view.gif"));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(2, 2));
        mainPanel = new StructureComparePanel(leftOuid, rightOuid);
        contentPane.add(mainPanel);
        pack();
        contentPane = null;
    }

    public void setLeftOuid(String ouid)
    {
        leftOuid = ouid;
        if(mainPanel != null)
            mainPanel.setLeftOuid(ouid);
    }

    public void setRightOuid(String ouid)
    {
        rightOuid = ouid;
        if(mainPanel != null)
            mainPanel.setRightOuid(ouid);
    }

    private StructureComparePanel mainPanel;
    private String leftOuid;
    private String rightOuid;
}