// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NetworkEditorPanel.java

package dyna.framework.client;

import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.ToolBarButton;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.DialogReturnCallback;
import dyna.uic.TreeNodeObject;
import dyna.uic.graph.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIGeneration, LogIn

public class NetworkEditorPanel extends JPanel
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


    public NetworkEditorPanel()
    {
        wfm = null;
        dos = null;
        aus = null;
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
        nodeMap = null;
        scale = 1.0F;
        ScrollBarStepSize = 100;
        work_status = new HashMap();
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        highlitedColor = new Color(204, 204, 255);
        insertCallback = null;
        deleteNodeCallback = null;
        nodes = null;
        initComponents();
        nodeInformationMenuItem.addActionListener(this);
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

    public NetworkEditorPanel(boolean editable)
    {
        this();
        setEditable(editable);
    }

    public NetworkEditorPanel(String workflowProcessOID, boolean editable)
    {
        this();
        setEditable(editable);
    }

    private void initComponents()
    {
        nodePopupMenu = new JPopupMenu();
        nodeInformationMenuItem = new JMenuItem();
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
                } else
                if(e.getKeyCode() == 27 && graph.getMode() == 5)
                {
                    graph.setMode(1);
                    _tempPoint = null;
                    graph.repaint();
                }
            }

        });
        setFont(new Font("dialog", 0, 11));
        graph = new Graph();
        nodePopupMenu.add(new JSeparator());
        nodeInformationMenuItem.setActionCommand("nodeInformation");
        nodeInformationMenuItem.setText(DynaMOAD.getMSRString("WRD_0074", "Open", 3) + "...");
        nodeInformationMenuItem.setIcon(new ImageIcon("icons/Open.gif"));
        nodeInformationMenuItem.setFont(getFont());
        nodeInformationMenuItem.setEnabled(false);
        nodePopupMenu.add(nodeInformationMenuItem);
        connectNodeMenuItem.setActionCommand("connectNode");
        connectNodeMenuItem.setText(DynaMOAD.getMSRString("WRD_0077", "Connect", 0) + "...");
        connectNodeMenuItem.setEnabled(false);
        connectNodeMenuItem.setFont(getFont());
        nodePopupMenu.add(connectNodeMenuItem);
        removeEdgeMenuItem.setActionCommand("removeEdge");
        removeEdgeMenuItem.setText(DynaMOAD.getMSRString("WRD_0002", "Delete", 0));
        removeEdgeMenuItem.setEnabled(false);
        removeEdgeMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        removeEdgeMenuItem.setFont(getFont());
        edgePopupMenu.add(removeEdgeMenuItem);
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
            if(!SwingUtilities.isLeftMouseButton(evt) || evt.isShiftDown() || evt.isControlDown() || evt.isAltDown() || evt.getClickCount() != 2)
                break;
            if(_selectedNode == null)
                return;
            if(Utils.isNullString(_selectedNode.getOid()))
                return;
            try
            {
                String selectClassOuid = DynaMOAD.dos.getClassOuid(_selectedNode.getOid());
                UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, _selectedNode.getOid(), 1);
                uiGeneration.setVisible(true);
                uiGeneration = null;
            }
            catch(IIPRequestException ie)
            {
                ie.printStackTrace();
            }
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
                    edgeData.put("md$sequence", processOUID + new Date());
                    actOuid = _selectedNode.getOid();
                    edgeData.put("end1", new String(actOuid));
                    actOuid = _selectedNode2.getOid();
                    edgeData.put("end2", new String(actOuid));
                    String classOuid = DynaMOAD.dos.getClassOuid(actOuid);
                    if(Utils.isNullString(classOuid))
                        break;
                    DOSChangeable dosClass = DynaMOAD.dos.getClass(classOuid);
                    if(dosClass == null)
                        break;
                    String className = (String)dosClass.get("name") + "Network";
                    dosClass = DynaMOAD.dos.getClassWithName((String)dosClass.get("ouid@package"), className);
                    if(dosClass == null)
                        break;
                    classOuid = (String)dosClass.get("ouid");
                    if(Utils.isNullString(classOuid))
                        break;
                    edgeData.setClassOuid(classOuid);
                    ouid = DynaMOAD.dos.link(_selectedNode.getOid(), _selectedNode2.getOid(), edgeData);
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
                        re.printStackTrace();
                    }
                else
                    try
                    {
                        DOSChangeable tempObject = DynaMOAD.dos.get(_selectedNode.getOid());
                        if(tempObject != null)
                        {
                            tempObject.put("x", new Integer(_tempRectangle.x));
                            tempObject.put("y", new Integer(_tempRectangle.y));
                            DynaMOAD.dos.set(tempObject);
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
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
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenuItem.setEnabled(false);
                            } else
                            if(status.equals("100"))
                            {
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenuItem.setEnabled(false);
                            } else
                            if(status.equals("110"))
                            {
                                if(LogIn.userID.equals(activity.get("performer")))
                                {
                                    removeNodeMenuItem.setEnabled(false);
                                    connectNodeMenuItem.setEnabled(false);
                                } else
                                {
                                    removeNodeMenuItem.setEnabled(false);
                                    connectNodeMenuItem.setEnabled(false);
                                }
                            } else
                            {
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenuItem.setEnabled(false);
                            }
                        } else
                        {
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
                    if(Utils.isNullString(_selectedNode.getOid()))
                        return;
                    try
                    {
                        String selectClassOuid = DynaMOAD.dos.getClassOuid(_selectedNode.getOid());
                        UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, _selectedNode.getOid(), 1);
                        uiGeneration.setVisible(true);
                        uiGeneration = null;
                    }
                    catch(IIPRequestException ie)
                    {
                        ie.printStackTrace();
                    }
                } else
                if(!evt.getActionCommand().equals("edgeInformation") && evt.getActionCommand().equals("removeEdge"))
                {
                    if(_selectedEdge == null)
                        return;
                    int confirm = JOptionPane.showConfirmDialog(this, DynaMOAD.getMSRString("QST_0010", "Are you sure you want to delete selected workflow branch?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirm deletion", 0), 0);
                    if(confirm != 0)
                        return;
                    try
                    {
                        DynaMOAD.dos.unlink(_selectedEdge.getNode(0).getOid(), _selectedEdge.getNode(1).getOid(), _selectedEdge.getOid());
                    }
                    catch(Exception re)
                    {
                        re.printStackTrace();
                    }
                    removeEdge(_selectedEdge);
                    graph.repaint();
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
        String dateStarted = null;
        if(workflowNode == null)
            return null;
        activityOuid = (String)workflowNode.get("ouid");
        if(graph.getNode(activityOuid) != null)
            return graph.getNode(activityOuid);
        String ouid = graph.addNode(activityOuid, x, y, iconMode);
        Node node = graph.getNode(ouid);
        node.setIdentifier((String)workflowNode.get("identifier"));
        node.setName((String)workflowNode.get("name"));
        node.setProperty("type", "160");
        datePlanned = (String)workflowNode.get("time.limit.to.finish");
        dateClosed = (String)workflowNode.get("time.closed");
        dateStarted = (String)workflowNode.get("time.started");
        if(datePlanned == null)
            datePlanned = "";
        if(dateClosed == null)
            dateClosed = "";
        node.setProperty("date.planned", new String(datePlanned));
        node.setProperty("date.closed", new String(dateClosed));
        if(Utils.isNullString(dateStarted) && !Utils.isNullString(dateClosed))
            node.setMode(5);
        else
        if(Utils.isNullString(dateStarted))
            node.setMode(2);
        else
        if(!Utils.isNullString(datePlanned) && Utils.isNullString(dateClosed) && datePlanned.compareTo(sdf2.format(new Date())) < 0)
            node.setMode(4);
        else
        if(Utils.isNullString(dateClosed))
            node.setMode(3);
        else
        if(!Utils.isNullString(datePlanned) && !Utils.isNullString(dateClosed))
        {
            if(datePlanned.compareTo(dateClosed) < 0)
                node.setMode(7);
            else
            if(datePlanned.compareTo(dateClosed) > 0)
                node.setMode(6);
            else
                node.setMode(5);
        } else
        if(Utils.isNullString(datePlanned) && !Utils.isNullString(dateClosed))
            node.setMode(5);
        else
            node.setMode(1);
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

    public void setData(ArrayList nodeList)
    {
        nodes = nodeList;
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
        ArrayList transitions = null;
        ArrayList startNodes = null;
        ArrayList oldStartNodes = null;
        ArrayList nodeSegment = null;
        Iterator nodeKey = null;
        Iterator transKey = null;
        Node wNode1 = null;
        Node wNode2 = null;
        if(!isVisible())
            return;
        try
        {
            graph.removeAllNodes();
            nodeMap = new HashMap();
            transition = new DOSChangeable();
            HashMap filter = new HashMap();
            ArrayList networkList = null;
            ArrayList fieldList = new ArrayList();
            ArrayList aNode = null;
            TreeNodeObject userObject = null;
            String assoOuid = null;
            filter.put("list.mode", "association");
            for(nodeKey = nodes.iterator(); nodeKey.hasNext();)
            {
                aNode = (ArrayList)nodeKey.next();
                node = new DOSChangeable();
                node.put("ouid", aNode.get(2));
                userObject = (TreeNodeObject)aNode.get(3);
                node.put("identifier", userObject.getName());
                node.put("name", aNode.get(5));
                node.put("time.limit.to.finish", aNode.get(5));
                node.put("time.closed", aNode.get(6));
                node.put("time.started", aNode.get(7));
                Node realNode = insertNode(node, Utils.getInt((Integer)aNode.get(0)), Utils.getInt((Integer)aNode.get(1)));
                node = null;
            }

            nodeKey = null;
            for(nodeKey = nodes.iterator(); nodeKey.hasNext();)
            {
                aNode = (ArrayList)nodeKey.next();
                if(assoOuid == null)
                {
                    String classOuid = DynaMOAD.dos.getClassOuid((String)aNode.get(2));
                    if(Utils.isNullString(classOuid))
                        break;
                    DOSChangeable tempDos = DynaMOAD.dos.getClass(classOuid);
                    if(tempDos == null)
                        break;
                    tempDos = DynaMOAD.dos.getClassWithName((String)tempDos.get("ouid@package"), tempDos.get("name") + "Network");
                    if(tempDos == null)
                        break;
                    classOuid = (String)tempDos.get("ouid");
                    ArrayList assoList = DynaMOAD.dos.listAssociationOfClass(classOuid);
                    if(assoList == null || assoList.isEmpty())
                        break;
                    DOSChangeable assoMap = null;
                    Iterator assoKey;
                    for(assoKey = assoList.iterator(); assoKey.hasNext();)
                    {
                        assoMap = (DOSChangeable)assoKey.next();
                        if(classOuid.equals(assoMap.get("ouid@class")))
                        {
                            assoOuid = (String)assoMap.get("ouid");
                            break;
                        }
                    }

                    assoKey = null;
                    filter.put("ouid@association.class", assoOuid);
                    ArrayList fields = DynaMOAD.dos.listFieldInClass(classOuid);
                    if(fields == null || fields.isEmpty())
                        break;
                    Iterator fieldKey;
                    for(fieldKey = fields.iterator(); fieldKey.hasNext();)
                    {
                        tempDos = (DOSChangeable)fieldKey.next();
                        if("end1".equals(tempDos.get("name")))
                        {
                            fieldList.add(tempDos.get("ouid"));
                            break;
                        }
                    }

                    fieldKey = null;
                    for(fieldKey = fields.iterator(); fieldKey.hasNext();)
                    {
                        tempDos = (DOSChangeable)fieldKey.next();
                        if("end2".equals(tempDos.get("name")))
                        {
                            fieldList.add(tempDos.get("ouid"));
                            break;
                        }
                    }

                    fieldKey = null;
                    classOuid = DynaMOAD.dos.getClassOuid((String)aNode.get(2));
                    fields = DynaMOAD.dos.listFieldInClass(classOuid);
                    if(fields == null || fields.isEmpty())
                        break;
                    for(fieldKey = fields.iterator(); fieldKey.hasNext();)
                    {
                        tempDos = (DOSChangeable)fieldKey.next();
                        if("md$number".equals(tempDos.get("name")))
                        {
                            fieldList.add(tempDos.get("ouid"));
                            break;
                        }
                    }

                    fieldKey = null;
                    for(fieldKey = fields.iterator(); fieldKey.hasNext();)
                    {
                        tempDos = (DOSChangeable)fieldKey.next();
                        if("md$description".equals(tempDos.get("name")))
                        {
                            fieldList.add(tempDos.get("ouid"));
                            break;
                        }
                    }

                    fieldKey = null;
                }
                networkList = DynaMOAD.dos.listLinkFrom((String)aNode.get(2), fieldList, filter);
                if(networkList != null && !networkList.isEmpty())
                {
                    ArrayList aNetwork = null;
                    for(Iterator networkKey = networkList.iterator(); networkKey.hasNext();)
                    {
                        aNetwork = (ArrayList)networkKey.next();
                        if(((String)aNetwork.get(2)).startsWith((String)aNetwork.get(4)))
                        {
                            transition.put("ouid", aNetwork.get(5));
                            transition.put("identifier", aNetwork.get(5));
                            transition.put("name", aNetwork.get(5));
                            transition.put("ouid@act1", graph.getNode((String)aNode.get(2)));
                            transition.put("ouid@act2", graph.getNode((String)aNetwork.get(0)));
                            insertEdge(transition);
                        }
                    }

                    Object obj = null;
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(transitions != null)
        {
            transitions.clear();
            transitions = null;
        }
        repaint();
    }

    private WFM wfm;
    private DOS dos;
    private AUS aus;
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
    private JButton refreshButton;
    private JButton zoomResetButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private DialogReturnCallback insertCallback;
    private DialogReturnCallback deleteNodeCallback;
    private ArrayList nodes;












}