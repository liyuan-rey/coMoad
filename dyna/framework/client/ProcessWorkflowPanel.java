// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcessWorkflowPanel.java

package dyna.framework.client;

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DialogReturnCallback;
import dyna.uic.graph.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, WorkflowDetailDialog, CreateProcess, ProcessUserAssignPanel, 
//            LogIn, UserSelectDialog

public class ProcessWorkflowPanel extends JPanel
    implements ActionListener, DialogReturnCallback, Runnable
{
    class MouseAdapterForGraph extends MouseAdapter
    {

        public void mousePressed(MouseEvent evt)
        {
            graphMousePressed(evt);
        }

        public void mouseReleased(MouseEvent evt)
        {
            graphMouseReleased(evt);
        }

        public void mouseClicked(MouseEvent evt)
        {
            graphMouseClicked(evt);
        }

        public void mouseExited(MouseEvent evt)
        {
            graphMouseExited(evt);
        }

        public void mouseEntered(MouseEvent evt)
        {
            graphMouseEntered(evt);
        }

        MouseAdapterForGraph()
        {
        }
    }

    class MouseMotionAdapterForGraph extends MouseMotionAdapter
    {

        public void mouseMoved(MouseEvent evt)
        {
            graphMouseMoved(evt);
        }

        public void mouseDragged(MouseEvent evt)
        {
            graphMouseDragged(evt);
        }

        MouseMotionAdapterForGraph()
        {
        }
    }


    public ProcessWorkflowPanel()
    {
        wfm = null;
        dos = null;
        aus = null;
        createProcess = null;
        objs = null;
        _selectedNode = null;
        _selectedNode2 = null;
        _selectedEdge = null;
        _tempRectangle = null;
        _tempPoint = null;
        editable = false;
        removeDefinition = false;
        processOUID = null;
        activityOuid = null;
        processInstance = null;
        callback = null;
        stepList = null;
        rejectMap = null;
        nodeMap = null;
        scale = 1.0F;
        ScrollBarStepSize = 100;
        work_status = new HashMap();
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        highlitedColor = new Color(204, 204, 255);
        insertCallback = null;
        deleteNodeCallback = null;
        startNodeOuid = null;
        transitions = null;
        maxX = 0;
        maxY = 0;
        initComponents();
        nodeInformationMenuItem.addActionListener(this);
        assignUserMenuItem.addActionListener(this);
        insertNodeMenuItem.addActionListener(this);
        pasteNodeMenuItem.addActionListener(this);
        refreshMenuItem.addActionListener(this);
        connectNodeMenuItem.addActionListener(this);
        removeNodeMenuItem.addActionListener(this);
        removeEdgeMenuItem.addActionListener(this);
        zoomResetMenuItem.addActionListener(this);
        zoomInMenuItem.addActionListener(this);
        zoomOutMenuItem.addActionListener(this);
        objs = new ArrayList();
        graph.setMode(1);
    }

    public ProcessWorkflowPanel(boolean editable)
    {
        this();
        setEditable(editable);
    }

    public ProcessWorkflowPanel(DOSChangeable processInstance, boolean editable)
    {
        this();
        dos = DynaMOAD.dos;
        wfm = DynaMOAD.wfm;
        aus = DynaMOAD.aus;
        setEditable(editable);
        this.processInstance = processInstance;
        if(this.processInstance != null)
            processOUID = (String)this.processInstance.get("ouid");
    }

    public ProcessWorkflowPanel(String workflowProcessOID, boolean editable)
    {
        this();
        setEditable(editable);
    }

    private void initComponents()
    {
        nodePopupMenu = new JPopupMenu();
        nodeInformationMenuItem = new JMenuItem();
        assignUserMenuItem = new JMenuItem();
        connectNodeMenuItem = new JMenuItem();
        removeNodeMenuItem = new JMenuItem();
        edgePopupMenu = new JPopupMenu();
        edgeInformationMenuItem = new JMenuItem();
        removeEdgeMenuItem = new JMenuItem();
        graphPopupMenu = new JPopupMenu();
        insertNodeMenuItem = new JMenuItem();
        pasteNodeMenuItem = new JMenuItem();
        refreshMenuItem = new JMenuItem();
        zoomMenu = new JMenu();
        zoomResetMenuItem = new JMenuItem();
        zoomInMenuItem = new JMenuItem();
        zoomOutMenuItem = new JMenuItem();
        ScrollPane = UIFactory.createStrippedScrollPane(null);
        ScrollPane.getViewport().setBackground(new Color(222, 229, 239));
        ScrollPane.setRequestFocusEnabled(true);
        ScrollPane.getVerticalScrollBar().addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == 40)
                {
                    int maxvalue = ScrollPane.getVerticalScrollBar().getMaximum();
                    int curvalue = ScrollPane.getVerticalScrollBar().getValue();
                    if(curvalue + ScrollBarStepSize <= maxvalue)
                        ScrollPane.getVerticalScrollBar().setValue(curvalue + ScrollBarStepSize);
                    else
                        ScrollPane.getVerticalScrollBar().setValue(maxvalue);
                } else
                if(e.getKeyCode() == 38)
                {
                    int minvalue = ScrollPane.getVerticalScrollBar().getMinimum();
                    int curvalue = ScrollPane.getVerticalScrollBar().getValue();
                    if(curvalue - ScrollBarStepSize >= minvalue)
                        ScrollPane.getVerticalScrollBar().setValue(curvalue - ScrollBarStepSize);
                    else
                        ScrollPane.getVerticalScrollBar().setValue(minvalue);
                } else
                if(e.getKeyCode() == 39)
                {
                    int maxvalue = ScrollPane.getHorizontalScrollBar().getMaximum();
                    int curvalue = ScrollPane.getHorizontalScrollBar().getValue();
                    if(curvalue + ScrollBarStepSize <= maxvalue)
                        ScrollPane.getHorizontalScrollBar().setValue(curvalue + ScrollBarStepSize);
                    else
                        ScrollPane.getHorizontalScrollBar().setValue(maxvalue);
                } else
                if(e.getKeyCode() == 37)
                {
                    int minvalue = ScrollPane.getHorizontalScrollBar().getMinimum();
                    int curvalue = ScrollPane.getHorizontalScrollBar().getValue();
                    if(curvalue + ScrollBarStepSize >= minvalue)
                        ScrollPane.getHorizontalScrollBar().setValue(curvalue - ScrollBarStepSize);
                    else
                        ScrollPane.getHorizontalScrollBar().setValue(minvalue);
                } else
                if(e.getKeyCode() == 107)
                {
                    scale = scale + 0.025F;
                    if(scale > 1.0F)
                        scale = 1.0F;
                    graph.setScale(scale);
                    graph.repaint();
                } else
                if(e.getKeyCode() == 109)
                {
                    scale = scale - 0.025F;
                    if(scale <= 0.025F)
                        scale = 0.025F;
                    graph.setScale(scale);
                    graph.repaint();
                }
            }

        });
        setFont(new Font("dialog", 0, 11));
        graph = new Graph();
        assignUserMenuItem.setActionCommand("assignUser");
        assignUserMenuItem.setText(DynaMOAD.getMSRString("WRD_0144", "Assign User...", 3));
        assignUserMenuItem.setIcon(new ImageIcon("icons/User.gif"));
        assignUserMenuItem.setFont(getFont());
        nodePopupMenu.add(assignUserMenuItem);
        nodePopupMenu.add(new JSeparator());
        nodeInformationMenuItem.setActionCommand("nodeInformation");
        nodeInformationMenuItem.setText(DynaMOAD.getMSRString("WRD_0074", "Open", 3) + "...");
        nodeInformationMenuItem.setIcon(new ImageIcon("icons/Open.gif"));
        nodeInformationMenuItem.setFont(getFont());
        nodePopupMenu.add(nodeInformationMenuItem);
        edgeInformationMenuItem.setActionCommand("edgeInformation");
        edgeInformationMenuItem.setText("Detail...");
        edgeInformationMenuItem.setEnabled(false);
        edgeInformationMenuItem.setIcon(new ImageIcon("icons/detail.gif"));
        edgeInformationMenuItem.setFont(getFont());
        edgePopupMenu.add(edgeInformationMenuItem);
        zoomMenu.setText(DynaMOAD.getMSRString("WRD_0133", "Zoom", 3));
        zoomMenu.setIcon(new ImageIcon("icons/Zoom16.gif"));
        zoomMenu.setFont(getFont());
        graphPopupMenu.add(zoomMenu);
        zoomResetMenuItem.setActionCommand("zoom.reset");
        zoomResetMenuItem.setText(DynaMOAD.getMSRString("WRD_0136", "Reset", 3));
        zoomResetMenuItem.setIcon(new ImageIcon("icons/Blank.gif"));
        zoomResetMenuItem.setFont(getFont());
        zoomMenu.add(zoomResetMenuItem);
        zoomMenu.add(new JSeparator());
        zoomOutMenuItem.setActionCommand("zoom.out");
        zoomOutMenuItem.setText(DynaMOAD.getMSRString("WRD_0135", "Zoom Out", 3));
        zoomOutMenuItem.setIcon(new ImageIcon("icons/ZoomOut16.gif"));
        zoomOutMenuItem.setFont(getFont());
        zoomMenu.add(zoomOutMenuItem);
        zoomInMenuItem.setActionCommand("zoom.in");
        zoomInMenuItem.setText(DynaMOAD.getMSRString("WRD_0134", "Zoom In", 3));
        zoomInMenuItem.setIcon(new ImageIcon("icons/ZoomIn16.gif"));
        zoomInMenuItem.setFont(getFont());
        zoomMenu.add(zoomInMenuItem);
        refreshMenuItem.setActionCommand("refresh");
        refreshMenuItem.setText(DynaMOAD.getMSRString("WRD_0013", "Refresh", 3));
        refreshMenuItem.setIcon(new ImageIcon("icons/Refresh.gif"));
        refreshMenuItem.setFont(getFont());
        graphPopupMenu.add(refreshMenuItem);
        setLayout(new BorderLayout());
        setOpaque(false);
        setAutoscrolls(true);
        graph.setEdgeClass("dyna.uic.graph.VectorEdge");
        graph.setDoubleBuffered(true);
        graph.setForeground(Color.black);
        graph.setNodeClass("dyna.uic.graph.WorkflowNode");
        graph.addMouseListener(new MouseAdapterForGraph());
        graph.addMouseMotionListener(new MouseMotionAdapterForGraph());
        ScrollPane.setViewportView(graph);
        ScrollPane.setHorizontalScrollBarPolicy(32);
        ScrollPane.setVerticalScrollBarPolicy(22);
        add(ScrollPane, "Center");
        add(buildToolBar(), "North");
    }

    private JToolBar buildToolBar()
    {
        if(mainToolBar == null)
        {
            mainToolBar = new ExtToolBar("mainToolBar", HeaderStyle.BOTH);
            assignUserButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/User.gif")));
            assignUserButton.setToolTipText(DynaMOAD.getMSRString("WRD_0144", "Assign User...", 3));
            assignUserButton.setActionCommand("assignUserToolBar");
            assignUserButton.addActionListener(this);
            mainToolBar.add(assignUserButton);
            skipActivityButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/skipActivity.gif")));
            skipActivityButton.setToolTipText(DynaMOAD.getMSRString("WRD_0145", "Toggle Skip Mode", 3));
            skipActivityButton.setActionCommand("skipActivityButton");
            skipActivityButton.addActionListener(this);
            mainToolBar.add(skipActivityButton);
            refreshButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/Refresh.gif")));
            refreshButton.setToolTipText(DynaMOAD.getMSRString("WRD_0013", "Refresh", 3));
            refreshButton.setActionCommand("refresh");
            refreshButton.addActionListener(this);
            mainToolBar.add(refreshButton);
            zoomResetButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/FitToAll.gif")));
            zoomResetButton.setToolTipText(DynaMOAD.getMSRString("WRD_0136", "Zoom Reset", 3));
            zoomResetButton.setActionCommand("zoom.reset");
            zoomResetButton.addActionListener(this);
            mainToolBar.add(zoomResetButton);
            zoomOutButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/ZoomOut16.gif")));
            zoomOutButton.setToolTipText(DynaMOAD.getMSRString("WRD_0135", "Zoom Out", 3));
            zoomOutButton.setActionCommand("zoom.out");
            zoomOutButton.addActionListener(this);
            mainToolBar.add(zoomOutButton);
            zoomInButton = new ToolBarButton(new ImageIcon(getClass().getResource("/icons/ZoomIn16.gif")));
            zoomInButton.setToolTipText(DynaMOAD.getMSRString("WRD_0134", "Zoom In", 3));
            zoomInButton.setActionCommand("zoom.in");
            zoomInButton.addActionListener(this);
            mainToolBar.add(zoomInButton);
        }
        return mainToolBar;
    }

    public void setProcessInstance(DOSChangeable processInstance)
    {
        this.processInstance = processInstance;
        if(this.processInstance != null)
            processOUID = (String)this.processInstance.get("ouid");
        activityOuid = null;
    }

    public void setActivityOuid(String activityOuid)
    {
        this.activityOuid = activityOuid;
    }

    public Node getSelectedNode()
    {
        return _selectedNode;
    }

    private void graphMouseExited(MouseEvent evt)
    {
        switch(graph.getMode())
        {
        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        default:
            return;
        }
    }

    private void graphMouseEntered(MouseEvent mouseevent)
    {
    }

    private void graphMouseClicked(MouseEvent evt)
    {
        ScrollPane.getVerticalScrollBar().grabFocus();
        String ouid = null;
        Node node = null;
        switch(graph.getMode())
        {
        case 2: // '\002'
        default:
            break;

        case 1: // '\001'
            if(SwingUtilities.isLeftMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown() && evt.getClickCount() == 2)
            {
                WorkflowDetailDialog dialog = new WorkflowDetailDialog(this, _selectedNode.getOid());
                dialog.setVisible(true);
                break;
            }
            if(createProcess.assignPanel != null && createProcess.assignPanel.isVisible())
                createProcess.assignPanel.refreshUserList();
            break;

        case 3: // '\003'
            if(editable);
            break;

        case 4: // '\004'
            if(!editable)
                break;
            if(_selectedNode != null)
            {
                _selectedNode.setSelected(false);
                _selectedNode = null;
            }
            _selectedNode = graph.getNodeForLocation(evt.getX(), evt.getY());
            if(_selectedNode != null)
            {
                _selectedNode.setSelected(true);
                graph.setMode(5);
            }
            _tempPoint = null;
            graph.repaint();
            break;

        case 5: // '\005'
            if(!editable || _selectedNode == null)
                break;
            _selectedNode2 = graph.getNodeForLocation(evt.getX(), evt.getY());
            if(_selectedNode2 == null)
                break;
            Rectangle rectangle = _selectedNode.getBounds();
            if(_tempPoint != null)
            {
                Graphics2D g2 = (Graphics2D)graph.getGraphics();
                g2.setXORMode(Color.white);
                g2.drawLine(_tempPoint.x, _tempPoint.y, rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
                g2.dispose();
            }
            if(!graph.isConnectedNodes(_selectedNode, _selectedNode2) && processOUID != null)
                try
                {
                    DOSChangeable edgeData = new DOSChangeable();
                    String actOuid = null;
                    String TRSOUID = null;
                    edgeData.put("ouid@process", processOUID);
                    edgeData.put("identifier", "connecter@" + Long.toHexString(System.currentTimeMillis()));
                    actOuid = _selectedNode.getOid();
                    if(actOuid.startsWith("wf$iact@"))
                        actOuid = (String)_selectedNode.getProperty("ouid@activity.definition");
                    edgeData.put("ouid@act1", new String(actOuid));
                    actOuid = _selectedNode2.getOid();
                    if(actOuid.startsWith("wf$iact@"))
                    {
                        actOuid = (String)_selectedNode2.getProperty("ouid@activity.definition");
                        TRSOUID = actOuid;
                    }
                    edgeData.put("ouid@act2", new String(actOuid));
                    edgeData.put("type", "N");
                    ouid = wfm.createTransition(edgeData);
                    edgeData.put("ouid", ouid);
                    ouid = graph.addEdge((String)edgeData.get("ouid"), _selectedNode, _selectedNode2);
                    Edge edge = graph.getEdge(ouid);
                    edge.setIdentifier((String)edgeData.get("identifier"));
                    edgeData.clear();
                    graph.setMode(1);
                }
                catch(IIPRequestException re)
                {
                    System.err.println(re.getLocalizedMessage());
                }
            graph.repaint();
            break;
        }
    }

    private void graphMouseMoved(MouseEvent evt)
    {
        switch(graph.getMode())
        {
        case 1: // '\001'
        case 2: // '\002'
        case 4: // '\004'
        default:
            break;

        case 3: // '\003'
            if(editable)
            {
                Graphics2D g = (Graphics2D)graph.getGraphics();
                g.setXORMode(Color.white);
                int x = evt.getX();
                int y = evt.getY();
                if(x < 1)
                    x = 1;
                if(y < 1)
                    y = 1;
                if(_tempRectangle != null)
                    g.drawRect(_tempRectangle.x, _tempRectangle.y, _tempRectangle.width, _tempRectangle.height);
                else
                    _tempRectangle = new Rectangle(x, y, 32, 32);
                _tempRectangle.setLocation(x - _tempRectangle.width / 2, y - _tempRectangle.height / 2);
                g.drawRect(_tempRectangle.x, _tempRectangle.y, _tempRectangle.width, _tempRectangle.height);
                g.setPaintMode();
                g.dispose();
            }
            break;

        case 5: // '\005'
            if(!editable || _selectedNode == null)
                break;
            Graphics2D g = (Graphics2D)graph.getGraphics();
            g.setXORMode(Color.white);
            Point centerPoint = new Point();
            _selectedNode.getCenterLocation(centerPoint);
            if(_tempPoint != null)
                g.drawLine(_tempPoint.x, _tempPoint.y, centerPoint.x, centerPoint.y);
            else
                _tempPoint = new Point();
            _tempPoint.setLocation(evt.getX(), evt.getY());
            g.drawLine(_tempPoint.x, _tempPoint.y, centerPoint.x, centerPoint.y);
            g.setPaintMode();
            g.dispose();
            break;
        }
    }

    public boolean isUserAssignableActivity(Node node)
    {
        boolean returnValue = false;
        if(node == null)
            return false;
        String type = (String)node.getProperty("type");
        String status = null;
        if(Utils.isNullString(type))
            return false;
        if(type.equals("120") || type.equals("130") || type.equals("110") || type.equals("230"))
        {
            DOSChangeable activity = null;
            try
            {
                activity = wfm.getActivity(_selectedNode.getOid());
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
            if(activity != null)
            {
                status = (String)activity.get("ouid@workflow.status");
                if(status.equals("120") || status.equals("130") || status.equals("140") || status.equals("160"))
                    returnValue = false;
                else
                if(status.equals("100"))
                    returnValue = true;
                else
                if(status.equals("110"))
                {
                    if(LogIn.userID.equals(activity.get("performer")))
                        returnValue = true;
                    else
                        returnValue = false;
                } else
                {
                    returnValue = false;
                }
            } else
            {
                returnValue = false;
            }
        }
        return returnValue;
    }

    private void graphMouseReleased(MouseEvent evt)
    {
        DOSChangeable dosObject = null;
        DOSChangeable activity = null;
        switch(graph.getMode())
        {
        case 2: // '\002'
        case 4: // '\004'
        case 5: // '\005'
        default:
            break;

        case 1: // '\001'
            if(_tempRectangle != null && _selectedNode != null && SwingUtilities.isLeftMouseButton(evt))
            {
                if(_tempRectangle.x < 1)
                    _tempRectangle.x = 1;
                if(_tempRectangle.y < 1)
                    _tempRectangle.y = 1;
                _selectedNode.setLocation(_tempRectangle.x, _tempRectangle.y);
                String string = _selectedNode.getOid();
                if(string.startsWith("wf$dact@") || string.startsWith("wf$iact@"))
                    try
                    {
                        wfm.setActivityDimension(_selectedNode.getOid(), new Integer(_tempRectangle.x), new Integer(_tempRectangle.y), Utils.ZeroInteger, Utils.ZeroInteger);
                    }
                    catch(IIPRequestException re)
                    {
                        System.err.println(re.getLocalizedMessage());
                    }
                _tempRectangle = null;
                graph.computeSize(_selectedNode);
                graph.repaint();
            }
            if(SwingUtilities.isRightMouseButton(evt) && !evt.isControlDown() && !evt.isAltDown() && !evt.isShiftDown())
                if(_selectedNode != null && _selectedEdge == null)
                {
                    String type = (String)_selectedNode.getProperty("type");
                    String status = null;
                    if(type == null || type.equals(""))
                    {
                        nodeInformationMenuItem.setEnabled(true);
                        if(editable)
                        {
                            JFrame contframe = (JFrame)JOptionPane.getFrameForComponent(this);
                            connectNodeMenuItem.setEnabled(true);
                        } else
                        {
                            removeNodeMenuItem.setEnabled(false);
                            connectNodeMenuItem.setEnabled(false);
                        }
                    } else
                    if(type.equals("180"))
                    {
                        nodeInformationMenuItem.setEnabled(false);
                        removeNodeMenuItem.setEnabled(false);
                        if(isEditable())
                            connectNodeMenuItem.setEnabled(true);
                        else
                            connectNodeMenuItem.setEnabled(false);
                    } else
                    if(type.equals("190"))
                    {
                        nodeInformationMenuItem.setEnabled(false);
                        JFrame contframe;
                        if(isEditable())
                            contframe = (JFrame)JOptionPane.getFrameForComponent(this);
                        else
                            removeNodeMenuItem.setEnabled(false);
                        connectNodeMenuItem.setEnabled(false);
                    } else
                    if(type.equals("120") || type.equals("130") || type.equals("110") || type.equals("230"))
                    {
                        try
                        {
                            activity = wfm.getActivity(_selectedNode.getOid());
                        }
                        catch(IIPRequestException e)
                        {
                            System.err.println(e);
                        }
                        if(activity != null)
                        {
                            status = (String)activity.get("ouid@workflow.status");
                            if(status.equals("120") || status.equals("130") || status.equals("140") || status.equals("160"))
                            {
                                assignUserMenuItem.setEnabled(false);
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenuItem.setEnabled(false);
                            } else
                            if(status.equals("100"))
                            {
                                assignUserMenuItem.setEnabled(true);
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenuItem.setEnabled(false);
                            } else
                            if(status.equals("110"))
                            {
                                if(LogIn.userID.equals(activity.get("performer")))
                                {
                                    assignUserMenuItem.setEnabled(true);
                                    removeNodeMenuItem.setEnabled(false);
                                    connectNodeMenuItem.setEnabled(false);
                                } else
                                {
                                    assignUserMenuItem.setEnabled(false);
                                    removeNodeMenuItem.setEnabled(false);
                                    connectNodeMenuItem.setEnabled(false);
                                }
                            } else
                            {
                                assignUserMenuItem.setEnabled(false);
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenuItem.setEnabled(false);
                            }
                        } else
                        {
                            assignUserMenuItem.setEnabled(false);
                            nodeInformationMenuItem.setEnabled(false);
                        }
                    } else
                    if(_selectedNode.getProperty("date.closed") != null && !_selectedNode.getProperty("date.closed").equals(""))
                    {
                        nodeInformationMenuItem.setEnabled(true);
                        removeNodeMenuItem.setEnabled(false);
                        connectNodeMenuItem.setEnabled(false);
                    }
                    nodePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                } else
                if(_selectedNode == null && _selectedEdge != null)
                {
                    edgePopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                    String status_edge = (String)_selectedEdge.getProperty("status");
                    if(status_edge != null && (status_edge.equals("WFS110") || status_edge.equals("WFS130") || status_edge.equals("WFS190")))
                        removeEdgeMenuItem.setEnabled(false);
                } else
                if(_selectedNode == null && _selectedEdge == null)
                {
                    evtX = evt.getX();
                    evtY = evt.getY();
                    graphPopupMenu.show(evt.getComponent(), evtX, evtY);
                }
            break;

        case 3: // '\003'
            if(editable && insertCallback != null)
            {
                dosObject = new DOSChangeable();
                dosObject.put("object.type", "add.activity.workflow");
                dosObject.put("x", new Integer(evt.getX()));
                dosObject.put("y", new Integer(evt.getY()));
                graph.setMode(1);
                insertCallback.setDialogReturnValue(dosObject);
                dosObject = null;
            }
            break;
        }
        Object contentframe = JOptionPane.getFrameForComponent(this);
    }

    private void graphMouseDragged(MouseEvent evt)
    {
        switch(graph.getMode())
        {
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        default:
            break;

        case 1: // '\001'
            if(editable && _selectedNode != null)
            {
                Graphics2D g = (Graphics2D)graph.getGraphics();
                g.setXORMode(Color.white);
                if(_tempRectangle != null)
                    g.drawRoundRect(_tempRectangle.x, _tempRectangle.y, _tempRectangle.width, _tempRectangle.height, 10, 10);
                else
                    _tempRectangle = new Rectangle(_selectedNode.getBounds());
                int x = evt.getX();
                int y = evt.getY();
                if(x < 1)
                    x = 1;
                if(y < 1)
                    y = 1;
                _tempRectangle.setLocation(x - _tempRectangle.width / 2, y - _tempRectangle.height / 2);
                g.drawRoundRect(_tempRectangle.x, _tempRectangle.y, _tempRectangle.width, _tempRectangle.height, 10, 10);
                g.setPaintMode();
                g.dispose();
            }
            break;
        }
    }

    private void graphMousePressed(MouseEvent evt)
    {
        switch(graph.getMode())
        {
        case 1: // '\001'
            if(_selectedNode != null)
            {
                _selectedNode.setSelected(false);
                _selectedNode = null;
            }
            if(_selectedEdge != null)
            {
                _selectedEdge.setSelected(false);
                _selectedEdge = null;
            }
            _selectedNode = graph.getNodeForLocation(evt.getX(), evt.getY());
            if(_selectedNode != null)
            {
                _selectedNode.setSelected(true);
            } else
            {
                _selectedEdge = graph.getEdgeForLocation(evt.getX(), evt.getY());
                if(_selectedEdge != null)
                    _selectedEdge.setSelected(true);
            }
            graph.repaint();
            // fall through

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        default:
            return;
        }
    }

    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getActionCommand().equals("insertFinishNode"))
        {
            if(Utils.isNullString(processOUID))
                return;
            DOSChangeable activity = new DOSChangeable();
            String processDefinitionOUID = processOUID;
            String activityDefinitionOUID = null;
            try
            {
                if(processOUID.startsWith("wf$ipro@"))
                    processDefinitionOUID = wfm.getDefinitionOfProcess(processOUID);
                activity.put("ouid@process.definition", processDefinitionOUID);
                activity.put("identifier", "FINISH");
                activity.put("name", "Finish");
                activity.put("type", "190");
                activity.put("limit", new Integer(0));
                activity.put("mode.start", "A");
                activity.put("mode.finish", "A");
                activity.put("priority", new Integer(0));
                activity.put("instantiation", new Integer(0));
                activity.put("join", "A");
                activity.put("split", "A");
                activity.put("x", new Integer(evtX));
                activity.put("y", new Integer(evtY));
                activity.put("w", new Integer(0));
                activity.put("h", new Integer(0));
                activity.put("is.container", new Boolean(false));
                activityDefinitionOUID = wfm.createActivityDefinition(activity);
                String activityOUID = activityDefinitionOUID;
                if(processOUID.startsWith("wf$ipro@"))
                {
                    activity.put("ouid", activityDefinitionOUID);
                    activity.put("ouid@process.definition", processOUID);
                    activityOUID = wfm.createActivity(activity);
                }
                activity.put("ouid", activityOUID);
                Node node = insertNode(activity, evtX, evtY, true);
                node.setProperty("type", "190");
            }
            catch(IIPRequestException re)
            {
                activity.clear();
                activity = null;
                System.err.println(re.getLocalizedMessage());
            }
            activity.clear();
            activity = null;
        } else
        if(!evt.getActionCommand().equals("pasteNode"))
            if(evt.getActionCommand().equals("refresh"))
                loadWorkflow(processOUID);
            else
            if(evt.getActionCommand().equals("zoom.reset"))
            {
                scale = 1.0F;
                graph.setScale(scale);
                graph.repaint();
            } else
            if(evt.getActionCommand().equals("zoom.in"))
            {
                scale = scale * 2.0F;
                if(scale > 1.0F)
                    scale = 1.0F;
                graph.setScale(scale);
                graph.repaint();
            } else
            if(evt.getActionCommand().equals("zoom.out"))
            {
                scale = scale * 0.6F;
                if(scale <= 0.05F)
                    scale = 0.025F;
                graph.setScale(scale);
                graph.repaint();
            } else
            if(!evt.getActionCommand().equals("save"))
                if(evt.getActionCommand().equals("connectNode"))
                {
                    if(_selectedNode != null)
                        graph.setMode(5);
                    _tempPoint = null;
                    graph.repaint();
                } else
                if(evt.getActionCommand().equals("nodeInformation"))
                {
                    if(_selectedNode == null)
                        return;
                    WorkflowDetailDialog dialog = new WorkflowDetailDialog(this, _selectedNode.getOid());
                    dialog.setVisible(true);
                } else
                if(evt.getActionCommand().equals("assignUser"))
                {
                    if(_selectedNode == null)
                        return;
                    UserSelectDialog dialog = new UserSelectDialog((JFrame)JOptionPane.getFrameForComponent(this), true, _selectedNode.getOid());
                    dialog.setDialogReturnCallback(this);
                    dialog.show();
                    dialog = null;
                } else
                if(evt.getActionCommand().equals("removeNode"))
                {
                    if(_selectedNode == null)
                        return;
                    int confirm = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0009", "Are you sure you want to delete selected workflow activity?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirm deletion", 0), 0);
                    if(confirm != 0)
                        return;
                    String string = _selectedNode.getOid();
                    if(removeDefinition)
                    {
                        if(string.startsWith("wf$dact@") || string.startsWith("wf$iact@"))
                            try
                            {
                                if(string.startsWith("wf$iact@"))
                                    string = wfm.getDefinitionOfActivity(string);
                                wfm.removeActivityDefinition(string);
                            }
                            catch(IIPRequestException re)
                            {
                                System.err.println(re.getLocalizedMessage());
                            }
                        if(deleteNodeCallback != null)
                        {
                            DOSChangeable dosObject = new DOSChangeable();
                            dosObject.put("object.type", "delete.activity.workflow");
                            dosObject.put("ouid", string);
                            deleteNodeCallback.setDialogReturnValue(dosObject);
                            dosObject = null;
                        }
                    } else
                    if(string.startsWith("wf$dact@") || string.startsWith("wf$iact@"))
                        try
                        {
                            wfm.setActivityDimension(_selectedNode.getOid(), Utils.ZeroInteger, Utils.ZeroInteger, Utils.ZeroInteger, Utils.ZeroInteger);
                            Edge edge = null;
                            ArrayList edges = _selectedNode.getEdges2();
                            for(Iterator enum = edges.iterator(); enum.hasNext(); wfm.removeTransition(edge.getOid()))
                                edge = (Edge)enum.next();

                            edges = _selectedNode.getEdges1();
                            for(Iterator enum = edges.iterator(); enum.hasNext(); wfm.removeTransition(edge.getOid()))
                                edge = (Edge)enum.next();

                        }
                        catch(IIPRequestException re)
                        {
                            System.err.println(re.getLocalizedMessage());
                        }
                    removeNode(_selectedNode);
                    graph.repaint();
                } else
                if(!evt.getActionCommand().equals("edgeInformation"))
                    if(evt.getActionCommand().equals("removeEdge"))
                    {
                        if(_selectedEdge == null)
                            return;
                        int confirm = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0010", "Are you sure you want to delete selected workflow branch?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirm deletion", 0), 0);
                        if(confirm != 0)
                            return;
                        removeEdge(_selectedEdge);
                        if(_selectedEdge.getOid().startsWith("wf$dtrs@") || processOUID != null)
                            try
                            {
                                wfm.removeTransition(_selectedEdge.getOid());
                            }
                            catch(IIPRequestException re)
                            {
                                System.err.println(re.getLocalizedMessage());
                            }
                        graph.repaint();
                    } else
                    if(evt.getActionCommand().equals("assignUserToolBar"))
                    {
                        if(createProcess != null)
                            createProcess.setEnableAssignView(true);
                    } else
                    if(evt.getActionCommand().equals("skipActivityButton") && _selectedNode != null && _selectedNode.getMode() != 3)
                    {
                        if(_selectedNode.getMode() == 9)
                            setSkipModeToActivity(_selectedNode, false);
                        else
                            setSkipModeToActivity(_selectedNode, true);
                        loadWorkflow(processOUID);
                    }
    }

    public void setCreateProcess(CreateProcess instance)
    {
        createProcess = instance;
    }

    public void setSkipModeToActivity(Node node, boolean enable)
    {
        try
        {
            if(enable)
            {
                if(isUserAssignableActivity(_selectedNode))
                    wfm.setActivityStatus(node.getOid(), "160");
            } else
            if(Utils.isNullString((String)node.getProperty("date.closed")))
                wfm.setActivityStatus(node.getOid(), "100");
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
        insertNodeMenuItem.setEnabled(editable);
        pasteNodeMenuItem.setEnabled(editable);
        connectNodeMenuItem.setEnabled(editable);
        removeNodeMenuItem.setEnabled(editable);
        removeEdgeMenuItem.setEnabled(editable);
    }

    public boolean isRemoveDefinition()
    {
        return removeDefinition;
    }

    public void setRemoveDefinition(boolean removeMode)
    {
        removeDefinition = removeMode;
    }

    public void updateGraphs()
    {
        if(isValid())
            graph.repaint();
    }

    public void removeEdge(String edgeOID)
    {
        if(edgeOID == null || edgeOID.equals(""))
        {
            return;
        } else
        {
            graph.removeEdge(edgeOID);
            return;
        }
    }

    public void removeEdge(Edge edge)
    {
        if(edge == null)
        {
            return;
        } else
        {
            graph.removeEdge(edge.getOid());
            return;
        }
    }

    public void removeNode(String nodeOID)
    {
        if(nodeOID == null || nodeOID.equals(""))
        {
            return;
        } else
        {
            graph.removeNode(nodeOID);
            return;
        }
    }

    public void removeNode(Node node)
    {
        if(node == null)
        {
            return;
        } else
        {
            graph.removeNode(node.getOid());
            return;
        }
    }

    public Node insertNode(DOSChangeable workflowNode, int x, int y, boolean iconMode)
    {
        String activityOuid = null;
        String datePlanned = null;
        String dateClosed = null;
        if(workflowNode == null)
            return null;
        activityOuid = (String)workflowNode.get("ouid");
        if(graph.getNode(activityOuid) != null)
            return graph.getNode(activityOuid);
        String ouid = graph.addNode(activityOuid, x, y, iconMode);
        Node node = graph.getNode(ouid);
        node.setIdentifier((String)workflowNode.get("identifier"));
        node.setName((String)workflowNode.get("name"));
        node.setProperty("type", (String)workflowNode.get("type"));
        datePlanned = (String)workflowNode.get("time.limit.to.finish");
        dateClosed = (String)workflowNode.get("time.closed");
        if(datePlanned == null)
            datePlanned = "";
        if(dateClosed == null)
            dateClosed = "";
        node.setProperty("date.planned", new String(datePlanned));
        node.setProperty("date.closed", new String(dateClosed));
        String status = (String)workflowNode.get("ouid@workflow.status");
        if(!Utils.isNullString(status))
        {
            if(!datePlanned.equals("") && dateClosed.equals("") && datePlanned.compareTo(sdf2.format(new Date())) < 0)
                node.setMode(4);
            else
            if(status.equals("100"))
                node.setMode(2);
            else
            if(status.equals("110"))
                node.setMode(3);
            else
            if(status.equals("130"))
            {
                if(datePlanned != null && !datePlanned.equals("") && dateClosed != null && !dateClosed.equals(""))
                {
                    if(datePlanned.compareTo(dateClosed) < 0)
                        node.setMode(7);
                    else
                    if(datePlanned.compareTo(dateClosed) > 0)
                        node.setMode(6);
                    else
                        node.setMode(5);
                } else
                {
                    node.setMode(5);
                }
            } else
            if(status.equals("150"))
                node.setMode(4);
            else
            if(status.equals("160"))
                node.setMode(9);
            else
            if(status.equals("180"))
                node.setMode(3);
            else
            if(status.equals("190"))
                node.setMode(5);
            else
                node.setMode(1);
        } else
        if(ouid.startsWith("wf$iact@"))
            node.setMode(2);
        String actOuid = (String)workflowNode.get("ouid@activity.definition");
        if(!Utils.isNullString(actOuid))
        {
            if(actOuid != null)
                node.setProperty("ouid@activity.definition", new String(actOuid));
        } else
        if(ouid.startsWith("wf$iact@"))
            try
            {
                node.setProperty("ouid@activity.definition", wfm.getDefinitionOfActivity(ouid));
            }
            catch(IIPRequestException re)
            {
                System.err.println(re.getLocalizedMessage());
            }
        if(ouid.equals(this.activityOuid))
        {
            node.setSelected(true);
            _selectedNode = node;
        }
        return node;
    }

    public Node insertNode(DOSChangeable workflowNode, int x, int y)
    {
        String activityOuid = (String)workflowNode.get("ouid");
        if(!Utils.isNullString(activityOuid) && activityOuid.startsWith("wf$dact@"))
            return insertNode(workflowNode, x, y, true);
        else
            return insertNode(workflowNode, x, y, false);
    }

    public Edge insertEdge(DOSChangeable workflowConnecter)
    {
        if(workflowConnecter == null)
            return null;
        String ouid = graph.addEdge((String)workflowConnecter.get("ouid"), (Node)workflowConnecter.get("ouid@act1"), (Node)workflowConnecter.get("ouid@act2"));
        Edge edge = graph.getEdge(ouid);
        if(edge != null)
        {
            edge.setIdentifier((String)workflowConnecter.get("identifier"));
            edge.setName((String)workflowConnecter.get("name"));
        }
        return edge;
    }

    public void loadWorkflow(String processOUID)
    {
        this.processOUID = processOUID;
        run();
    }

    public void setGraphMode(int mode)
    {
        graph.setMode(mode);
    }

    public void setInsertNodeCallback(DialogReturnCallback callback)
    {
        insertCallback = callback;
    }

    public void setDeleteNodeCallback(DialogReturnCallback callback)
    {
        deleteNodeCallback = callback;
    }

    public void setDialogReturnValue(Object value)
    {
        if((value instanceof ArrayList) && _selectedNode != null)
        {
            ArrayList userList = (ArrayList)value;
            String userId = (String)userList.get(0);
            String ouid = _selectedNode.getOid();
            boolean isMultiple = false;
            try
            {
                DOSChangeable activity = wfm.getActivity(ouid);
                activity.put("performer", userId);
                wfm.setActivity(activity);
                Integer instantiation = (Integer)activity.get("instantiation");
                int maxCount = Utils.getInt(instantiation);
                int count = userList.size();
                if(count > 1 && maxCount > 1)
                {
                    activity.clear();
                    activity.put("ouid@activity", ouid);
                    activity.put("performers", userList);
                    wfm.createMultipleActivities(activity);
                    isMultiple = true;
                }
                activity.clear();
                activity = null;
                HashMap user = aus.getUser(userId);
                if(isMultiple)
                {
                    _selectedNode.setName("+" + user.get("name") + " (" + userId + ")");
                    _selectedNode.setProperty("is.multiple", Boolean.TRUE);
                } else
                {
                    _selectedNode.setName(user.get("name") + " (" + userId + ")");
                }
                user.clear();
                user = null;
                userList.clear();
                userList = null;
                repaint();
            }
            catch(IIPRequestException e)
            {
                System.err.println(e);
            }
        }
    }

    public void run()
    {
        int x = 0;
        int y = 0;
        int minY = 0;
        int step = 0;
        String startNodeOuid = null;
        String tempString = null;
        DOSChangeable node = null;
        DOSChangeable node2 = null;
        DOSChangeable transition = null;
        HashMap user = null;
        ArrayList nodes = null;
        ArrayList startNodes = null;
        ArrayList oldStartNodes = null;
        ArrayList nodeSegment = null;
        Iterator nodeKey = null;
        Iterator transKey = null;
        Node wNode1 = null;
        Node wNode2 = null;
        if(processInstance == null || processOUID == null)
            return;
        try
        {
            graph.removeAllNodes();
            nodes = wfm.getProcessActivities4Graph(processOUID);
            if(nodes == null)
                return;
            transitions = wfm.getProcessTransitions4Graph(processOUID);
            if(transitions == null)
                return;
            if(!isVisible())
                return;
            nodeMap = new HashMap();
            nodeSegment = new ArrayList();
            transition = new DOSChangeable();
            for(nodeKey = nodes.iterator(); nodeKey.hasNext();)
            {
                node = (DOSChangeable)nodeKey.next();
                if(node.get("type").equals("180"))
                    startNodeOuid = (String)node.get("ouid");
                nodeMap.put(node.get("ouid"), node);
                node = null;
            }

            nodeKey = null;
            this.startNodeOuid = startNodeOuid;
            if(Utils.isNullString(startNodeOuid))
                return;
            stepList = new ArrayList();
            rejectMap = new HashMap();
            lookupNextActivities(nodes, transitions, startNodeOuid);
            startNodes = new ArrayList(stepList);
            stepList.clear();
            x = 10;
            y = 10;
            for(nodeKey = startNodes.iterator(); nodeKey.hasNext();)
            {
                node = (DOSChangeable)nodeKey.next();
                node.put("identifier", DynaMOAD.getMSRString((String)node.get("name"), (String)node.get("name"), 0));
                tempString = (String)node.get("performer");
                if(!Utils.isNullString(tempString))
                {
                    user = aus.getUser(tempString);
                    if(node.get("is.multiple") == null)
                    {
                        node.put("name", (String)user.get("name") + " (" + tempString + ")");
                    } else
                    {
                        node.put("name", "+" + (String)user.get("name") + " (" + tempString + ")");
                        node.put("is.multiple", Boolean.TRUE);
                    }
                    user.clear();
                    user = null;
                } else
                {
                    node.put("name", "");
                }
                Node realNode = insertNode(node, x, y);
                if(node.get("is.multiple") != null)
                    realNode.setProperty("is.multiple", Boolean.TRUE);
                node = null;
                y += 80;
            }

            nodeKey = null;
            x += 190;
            maxY = y;
            while(startNodes != null) 
            {
                y = 10;
                nodeSegment.clear();
                for(nodeKey = startNodes.iterator(); nodeKey.hasNext();)
                {
                    stepList.clear();
                    node = (DOSChangeable)nodeKey.next();
                    lookupNextActivities(nodes, transitions, (String)node.get("ouid"));
                    nodeSegment.addAll(stepList);
                    wNode1 = graph.getNode((String)node.get("ouid"));
                    minY = 32767;
                    for(transKey = stepList.iterator(); transKey.hasNext();)
                    {
                        transition.clear();
                        node2 = (DOSChangeable)transKey.next();
                        if(graph.getNode((String)node2.get("ouid")) == null)
                        {
                            node2.put("identifier", DynaMOAD.getMSRString((String)node2.get("name"), (String)node2.get("name"), 0));
                            tempString = (String)node2.get("performer");
                            if(!Utils.isNullString(tempString))
                            {
                                user = aus.getUser(tempString);
                                if(node2.get("is.multiple") == null)
                                {
                                    node2.put("name", (String)user.get("name") + " (" + tempString + ")");
                                } else
                                {
                                    node2.put("name", "+" + (String)user.get("name") + " (" + tempString + ")");
                                    node2.put("is.multiple", Boolean.TRUE);
                                }
                                user.clear();
                                user = null;
                            } else
                            {
                                node2.put("name", "");
                            }
                            Node realNode = insertNode(node2, x, y);
                            if(node2.get("is.multiple") != null)
                                realNode.setProperty("is.multiple", Boolean.TRUE);
                        }
                        wNode2 = graph.getNode((String)node2.get("ouid"));
                        if(wNode2.getY() < minY)
                            minY = wNode2.getY();
                        transition.put("ouid", node2.get("ouid@transition"));
                        transition.put("identifier", node2.get("identifier@transition"));
                        transition.put("name", node2.get("name@transition"));
                        transition.put("ouid@act1", wNode1);
                        transition.put("ouid@act2", wNode2);
                        insertEdge(transition);
                        node2 = null;
                        wNode2 = null;
                        y += 80;
                    }

                    if(maxY < y)
                        maxY = y;
                    node = null;
                    wNode1 = null;
                }

                oldStartNodes = new ArrayList(startNodes);
                startNodes.clear();
                startNodes = null;
                if(nodeSegment.size() > 0)
                    startNodes = new ArrayList(nodeSegment);
                x += 190;
            }
            nodeSegment.clear();
            nodeSegment = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        maxX = x - 190;
        repaint();
    }

    public Point getMaximumPoint()
    {
        return new Point(maxX, maxY);
    }

    private void lookupNextActivities(ArrayList nodes, ArrayList trs, String nodeOuid)
    {
        Iterator nodeKey = null;
        Iterator trsKey = null;
        DOSChangeable result = null;
        DOSChangeable node = null;
        DOSChangeable transition = null;
        String type = null;
        String tempString = null;
        for(trsKey = trs.iterator(); trsKey.hasNext();)
        {
            transition = (DOSChangeable)trsKey.next();
            if(transition.get("ouid@act1").equals(nodeOuid) && (transition.get("type").equals("N") || transition.get("type").equals("A")))
            {
                node = (DOSChangeable)nodeMap.get(transition.get("ouid@act2"));
                type = (String)node.get("type");
                if(type.equals("120") || type.equals("130") || type.equals("110") || type.equals("230"))
                {
                    node.put("ouid@transition", transition.get("ouid"));
                    node.put("name@transitoin", transition.get("name"));
                    node.put("identifier@transition", transition.get("identifier"));
                    stepList.add(node);
                    node = null;
                    transition = null;
                } else
                {
                    tempString = (String)node.get("ouid");
                    node = null;
                    lookupNextActivities(nodes, trs, tempString);
                    tempString = null;
                }
            } else
            if(transition.get("ouid@act1").equals(nodeOuid) && nodeOuid.equals(activityOuid) && transition.get("type").equals("R"))
                lookupNextActivitiesForReject(trs, transition, (String)transition.get("ouid"));
            transition = null;
        }

        trsKey = null;
    }

    public void lookupNextActivitiesForReject(ArrayList trs, DOSChangeable startTransition, String transitionOuid)
    {
        Iterator trsKey = null;
        DOSChangeable node = null;
        DOSChangeable transition = null;
        String type = null;
        if(startTransition.get("type").equals("N") || startTransition.get("type").equals("R"))
        {
            node = (DOSChangeable)nodeMap.get(startTransition.get("ouid@act2"));
            type = (String)node.get("type");
            if(type.equals("120") || type.equals("130") || type.equals("180"))
            {
                if(!((String)node.get("ouid@workflow.status")).equals("160"))
                    rejectMap.put(transitionOuid, node.get("name"));
            } else
            {
                for(trsKey = trs.iterator(); trsKey.hasNext();)
                {
                    transition = (DOSChangeable)trsKey.next();
                    if(transition.get("ouid@act1").equals(node.get("ouid")))
                        break;
                    transition = null;
                }

                trsKey = null;
                if(transition != null)
                {
                    lookupNextActivitiesForReject(trs, transition, transitionOuid);
                    transition = null;
                }
            }
            node = null;
        }
    }

    public HashMap getRejectMap()
    {
        return rejectMap;
    }

    public boolean validateParticipants(String startNodeOuid)
    {
        if(Utils.isNullArrayList(transitions))
            return true;
        String startOuid = startNodeOuid;
        if(Utils.isNullString(startOuid))
            startOuid = this.startNodeOuid;
        Iterator nodeKey = null;
        Iterator trsKey = null;
        DOSChangeable result = null;
        DOSChangeable node = null;
        DOSChangeable transition = null;
        String type = null;
        String tempString = null;
        for(trsKey = transitions.iterator(); trsKey.hasNext();)
        {
            transition = (DOSChangeable)trsKey.next();
            if(transition.get("ouid@act1").equals(startOuid) && (transition.get("type").equals("N") || transition.get("type").equals("A")))
            {
                node = (DOSChangeable)nodeMap.get(transition.get("ouid@act2"));
                type = (String)node.get("type");
                if(type.equals("120") || type.equals("130") || type.equals("110") || type.equals("230"))
                {
                    if("160".equals(node.get("ouid@workflow.status")))
                    {
                        tempString = (String)node.get("ouid");
                        node = null;
                        if(!validateParticipants(tempString))
                            return false;
                    } else
                    if(Utils.isNullString((String)node.get("performer")))
                    {
                        node = null;
                        transition = null;
                        return false;
                    }
                    node = null;
                    transition = null;
                } else
                {
                    tempString = (String)node.get("ouid");
                    node = null;
                    if(!validateParticipants(tempString))
                        return false;
                }
            }
            transition = null;
        }

        trsKey = null;
        return true;
    }

    private WFM wfm;
    private DOS dos;
    private AUS aus;
    private CreateProcess createProcess;
    private ArrayList objs;
    private Node _selectedNode;
    private Node _selectedNode2;
    private Edge _selectedEdge;
    private Rectangle _tempRectangle;
    private Point _tempPoint;
    private boolean editable;
    private boolean removeDefinition;
    private String processOUID;
    private String activityOuid;
    private DOSChangeable processInstance;
    private DialogReturnCallback callback;
    private ArrayList stepList;
    private HashMap rejectMap;
    private HashMap nodeMap;
    private int evtX;
    private int evtY;
    private float scale;
    private int ScrollBarStepSize;
    private HashMap work_status;
    private SimpleDateFormat sdf2;
    private SimpleDateFormat sdf4;
    private Color highlitedColor;
    private JPopupMenu nodePopupMenu;
    private JMenuItem nodeInformationMenuItem;
    private JMenuItem assignUserMenuItem;
    private JMenuItem connectNodeMenuItem;
    private JMenuItem removeNodeMenuItem;
    private JPopupMenu edgePopupMenu;
    private JMenuItem edgeInformationMenuItem;
    private JMenuItem removeEdgeMenuItem;
    private JPopupMenu graphPopupMenu;
    private JMenuItem insertNodeMenuItem;
    private JMenuItem pasteNodeMenuItem;
    private JMenuItem refreshMenuItem;
    private JMenu zoomMenu;
    private JMenuItem zoomResetMenuItem;
    private JMenuItem zoomInMenuItem;
    private JMenuItem zoomOutMenuItem;
    private JScrollPane ScrollPane;
    public Graph graph;
    private JToolBar mainToolBar;
    private JButton assignUserButton;
    private JButton skipActivityButton;
    private JButton refreshButton;
    private JButton zoomResetButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private DialogReturnCallback insertCallback;
    private DialogReturnCallback deleteNodeCallback;
    private String startNodeOuid;
    private ArrayList transitions;
    private int maxX;
    private int maxY;











}