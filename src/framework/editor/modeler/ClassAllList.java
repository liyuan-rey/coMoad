// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClassAllList.java

package dyna.framework.editor.modeler;

import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DynaTheme;
import dyna.uic.Table;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.*;

// Referenced classes of package dyna.framework.editor.modeler:
//            ObjectModelingConstruction

public class ClassAllList extends JDialog
    implements ActionListener
{

    public ClassAllList(ObjectModelingConstruction mainFrame)
    {
        super(mainFrame, false);
        dos = null;
        this.mainFrame = null;
        modelTable = null;
        modelDataAL = null;
        toolBarBoxLayout = null;
        buttonToolBar = null;
        exitButton = null;
        tableScrPane = null;
        dataModel = null;
        superClassName = "";
        try
        {
            this.mainFrame = mainFrame;
            dos = (DOS)ObjectModelingConstruction.dfw.getServiceInstance("DF30DOS1");
            getAllModel();
            initialize();
            makeModelTable();
        }
        catch(Exception ie)
        {
            System.out.println(ie);
        }
    }

    public void initialize()
    {
        setSize(800, 500);
        setLocation(112, 84);
        setTitle("Class List");
        tableScrPane = UIFactory.createStrippedScrollPane(null);
        buttonToolBar = new JPanel();
        toolBarBoxLayout = new BoxLayout(buttonToolBar, 0);
        buttonToolBar.setLayout(toolBarBoxLayout);
        exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit");
        exitButton.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
        exitButton.setMargin(new Insets(0, 5, 0, 5));
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(this);
        exitButton.setDoubleBuffered(true);
        buttonToolBar.add(Box.createHorizontalGlue());
        buttonToolBar.add(Box.createHorizontalStrut(10));
        buttonToolBar.add(exitButton);
        getContentPane().add(tableScrPane, "Center");
        getContentPane().add(buttonToolBar, "South");
    }

    public void makeModelTable()
    {
        dataModel = new ArrayList();
        ArrayList dataDetailInfo = new ArrayList();
        ArrayList columnNames = new ArrayList();
        ArrayList columnWidths = new ArrayList();
        columnNames.add("Super Class");
        columnNames.add("Ouid");
        columnNames.add("Name");
        columnNames.add("Description");
        columnNames.add("Package Ouid");
        columnNames.add("Root Class");
        columnNames.add("Leaf Class");
        columnNames.add("Abstract Class");
        columnNames.add("Code");
        columnNames.add("Title");
        columnWidths.add(new Integer(124));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(130));
        columnWidths.add(new Integer(100));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(22));
        columnWidths.add(new Integer(22));
        columnWidths.add(new Integer(22));
        columnWidths.add(new Integer(80));
        columnWidths.add(new Integer(113));
        for(int i = 0; i < modelDataAL.size(); i++)
        {
            superClassName = "";
            dataDetailInfo.add(getSuperClassName((ArrayList)((DOSChangeable)modelDataAL.get(i)).get("array$ouid.superclass")));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("ouid"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("name"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("description"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("ouid@package"));
            dataDetailInfo.add((Boolean)((DOSChangeable)modelDataAL.get(i)).get("is.root"));
            dataDetailInfo.add((Boolean)((DOSChangeable)modelDataAL.get(i)).get("is.leaf"));
            dataDetailInfo.add((Boolean)((DOSChangeable)modelDataAL.get(i)).get("is.abstract"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("code"));
            dataDetailInfo.add((String)((DOSChangeable)modelDataAL.get(i)).get("title"));
            dataModel.add(dataDetailInfo.clone());
            dataDetailInfo.clear();
        }

        modelTable = new Table(dataModel, (ArrayList)columnNames.clone(), (ArrayList)columnWidths.clone(), 0, 240);
        modelTable.setColumnSequence(new int[] {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        });
        modelTable.setIndexColumn(0);
        tableScrPane.setViewportView(modelTable.getTable());
        tableScrPane.getViewport().setBackground(DynaTheme.treeLevelOneColor);
    }

    public void getAllModel()
    {
        try
        {
            ArrayList classDataAL = new ArrayList();
            classDataAL = dos.listClass();
            modelDataAL = dos.listClass();
        }
        catch(IIPRequestException ie)
        {
            System.out.println(ie);
        }
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(this, stroke, 0);
        rootPane.registerKeyboardAction(this, stroke, 2);
        return rootPane;
    }

    public String getSuperClassName(ArrayList superClassAL)
    {
        if(superClassAL != null)
        {
            for(int i = 0; i < superClassAL.size(); i++)
                try
                {
                    if(i == superClassAL.size() - 1)
                        superClassName += (String)dos.getClass((String)superClassAL.get(i)).get("name");
                    else
                        superClassName = superClassName + (String)dos.getClass((String)superClassAL.get(i)).get("name") + " , ";
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }

        }
        return superClassName;
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command == null)
            exitButton_actionPerformed(e);
        else
        if(command.equals("Exit"))
            exitButton_actionPerformed(e);
    }

    public void exitButton_actionPerformed(ActionEvent e)
    {
        exitButton.removeActionListener(this);
        dispose();
    }

    private DOS dos;
    private ObjectModelingConstruction mainFrame;
    private Table modelTable;
    private ArrayList modelDataAL;
    private BoxLayout toolBarBoxLayout;
    private JPanel buttonToolBar;
    private JButton exitButton;
    private JScrollPane tableScrPane;
    private ArrayList dataModel;
    private String superClassName;
}
