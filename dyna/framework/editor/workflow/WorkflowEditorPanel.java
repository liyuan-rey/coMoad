// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkflowEditorPanel.java

package dyna.framework.editor.workflow;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.swing.util.UIFactory;
import dyna.framework.Client;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.ServiceNotFoundException;
import dyna.framework.service.DOS;
import dyna.framework.service.WFM;
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
import javax.swing.plaf.ColorUIResource;

public class WorkflowEditorPanel extends JPanel
    implements ActionListener
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


    public WorkflowEditorPanel()
    {
        wfm = null;
        dos = null;
        _selectedNode = null;
        _selectedNode2 = null;
        _selectedEdge = null;
        _tempRectangle = null;
        _tempPoint = null;
        editable = false;
        removeDefinition = false;
        processOUID = null;
        callback = null;
        scale = 1.0F;
        ScrollBarStepSize = 100;
        work_status = new HashMap();
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        highlitedColor = new Color(PlasticLookAndFeel.getFocusColor().getRed(), PlasticLookAndFeel.getFocusColor().getGreen(), PlasticLookAndFeel.getFocusColor().getBlue(), 30);
        connectorType = null;
        insertCallback = null;
        deleteNodeCallback = null;
        initComponents();
        nodeInformationMenuItem.addActionListener(this);
        insertNodeMenuItem.addActionListener(this);
        pasteNodeMenuItem.addActionListener(this);
        refreshMenuItem.addActionListener(this);
        connectRegularMenuItem.addActionListener(this);
        connectAcceptMenuItem.addActionListener(this);
        connectRejectMenuItem.addActionListener(this);
        removeNodeMenuItem.addActionListener(this);
        removeEdgeMenuItem.addActionListener(this);
        zoomResetMenuItem.addActionListener(this);
        zoomInMenuItem.addActionListener(this);
        zoomOutMenuItem.addActionListener(this);
        objs = new ArrayList();
        graph.setMode(1);
    }

    public WorkflowEditorPanel(boolean editable)
    {
        this();
        setEditable(editable);
    }

    public WorkflowEditorPanel(Client dfw, boolean editable)
    {
        this();
        try
        {
            dos = (DOS)dfw.getServiceInstance("DF30DOS1");
            wfm = (WFM)dfw.getServiceInstance("DF30WFM1");
        }
        catch(ServiceNotFoundException e)
        {
            System.err.println(e);
        }
        setEditable(editable);
    }

    public WorkflowEditorPanel(String workflowProcessOID, boolean editable)
    {
        this();
        setEditable(editable);
        loadWorkflow(workflowProcessOID);
    }

    private void initComponents()
    {
        nodePopupMenu = new JPopupMenu();
        nodeInformationMenuItem = new JMenuItem();
        connectNodeMenu = new JMenu();
        connectRegularMenuItem = new JMenuItem();
        connectAcceptMenuItem = new JMenuItem();
        connectRejectMenuItem = new JMenuItem();
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
        nodeInformationMenuItem.setActionCommand("nodeInformation");
        nodeInformationMenuItem.setText("Information");
        nodeInformationMenuItem.setIcon(new ImageIcon("icons/Information.gif"));
        nodeInformationMenuItem.setFont(getFont());
        nodePopupMenu.add(nodeInformationMenuItem);
        connectNodeMenu.setText("Connect to");
        connectNodeMenu.setEnabled(false);
        connectNodeMenu.setFont(getFont());
        nodePopupMenu.add(connectNodeMenu);
        connectRegularMenuItem.setActionCommand("connectNode");
        connectRegularMenuItem.setText("Regular");
        connectRegularMenuItem.setFont(getFont());
        connectNodeMenu.add(connectRegularMenuItem);
        connectNodeMenu.add(new JSeparator());
        connectAcceptMenuItem.setActionCommand("connectNodeAccept");
        connectAcceptMenuItem.setText("Accept");
        connectAcceptMenuItem.setFont(getFont());
        connectNodeMenu.add(connectAcceptMenuItem);
        connectRejectMenuItem.setActionCommand("connectNodeReject");
        connectRejectMenuItem.setText("Reject");
        connectRejectMenuItem.setFont(getFont());
        connectNodeMenu.add(connectRejectMenuItem);
        removeNodeMenuItem.setActionCommand("removeNode");
        removeNodeMenuItem.setText("Delete");
        removeNodeMenuItem.setEnabled(false);
        removeNodeMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        removeNodeMenuItem.setFont(getFont());
        nodePopupMenu.add(removeNodeMenuItem);
        edgeInformationMenuItem.setActionCommand("edgeInformation");
        edgeInformationMenuItem.setText("Information");
        edgeInformationMenuItem.setEnabled(false);
        edgeInformationMenuItem.setIcon(new ImageIcon("icons/Information.gif"));
        edgeInformationMenuItem.setFont(getFont());
        edgePopupMenu.add(edgeInformationMenuItem);
        removeEdgeMenuItem.setActionCommand("removeEdge");
        removeEdgeMenuItem.setText("Delete");
        removeEdgeMenuItem.setEnabled(false);
        removeEdgeMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        removeEdgeMenuItem.setFont(getFont());
        edgePopupMenu.add(removeEdgeMenuItem);
        insertNodeMenuItem.setActionCommand("insertFinishNode");
        insertNodeMenuItem.setText("Add Finish Node");
        insertNodeMenuItem.setEnabled(false);
        insertNodeMenuItem.setIcon(new ImageIcon("icons/Blank.gif"));
        insertNodeMenuItem.setFont(getFont());
        graphPopupMenu.add(insertNodeMenuItem);
        pasteNodeMenuItem.setActionCommand("pasteNode");
        pasteNodeMenuItem.setText("Paste");
        pasteNodeMenuItem.setEnabled(false);
        pasteNodeMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteNodeMenuItem.setFont(getFont());
        graphPopupMenu.add(pasteNodeMenuItem);
        graphPopupMenu.add(new JSeparator());
        zoomMenu.setText("Zoom");
        zoomMenu.setIcon(new ImageIcon("icons/Zoom16.gif"));
        zoomMenu.setFont(getFont());
        graphPopupMenu.add(zoomMenu);
        zoomResetMenuItem.setActionCommand("zoom.reset");
        zoomResetMenuItem.setText("Reset");
        zoomResetMenuItem.setIcon(new ImageIcon("icons/Blank.gif"));
        zoomResetMenuItem.setFont(getFont());
        zoomMenu.add(zoomResetMenuItem);
        zoomMenu.add(new JSeparator());
        zoomOutMenuItem.setActionCommand("zoom.out");
        zoomOutMenuItem.setText("Zoom Out");
        zoomOutMenuItem.setIcon(new ImageIcon("icons/ZoomOut16.gif"));
        zoomOutMenuItem.setFont(getFont());
        zoomMenu.add(zoomOutMenuItem);
        zoomInMenuItem.setActionCommand("zoom.in");
        zoomInMenuItem.setText("Zoom In");
        zoomInMenuItem.setIcon(new ImageIcon("icons/ZoomIn16.gif"));
        zoomInMenuItem.setFont(getFont());
        zoomMenu.add(zoomInMenuItem);
        refreshMenuItem.setActionCommand("refresh");
        refreshMenuItem.setText("Refresh");
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
            java.awt.Frame frame;
            if(SwingUtilities.isLeftMouseButton(evt) && !evt.isShiftDown() && !evt.isControlDown() && !evt.isAltDown() && evt.getClickCount() == 2)
                frame = JOptionPane.getFrameForComponent(this);
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
                    if(connectorType == null)
                        connectorType = "N";
                    edgeData.put("type", connectorType);
                    ouid = wfm.createTransition(edgeData);
                    edgeData.put("ouid", ouid);
                    ouid = graph.addEdge((String)edgeData.get("ouid"), _selectedNode, _selectedNode2);
                    Edge edge = graph.getEdge(ouid);
                    String type = null;
                    if(edge != null)
                    {
                        edge.setIdentifier((String)edgeData.get("identifier"));
                        edge.setName((String)edgeData.get("name"));
                        type = (String)edgeData.get("type");
                        if("R".equals(type))
                            edge.setMode(4);
                        else
                        if("A".equals(type))
                            edge.setMode(8);
                        else
                            edge.setMode(1);
                    }
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
                    String status = (String)_selectedNode.getProperty("status");
                    if(type == null || type.equals(""))
                    {
                        nodeInformationMenuItem.setEnabled(true);
                        if(editable)
                        {
                            JFrame contframe = (JFrame)JOptionPane.getFrameForComponent(this);
                            connectNodeMenu.setEnabled(true);
                        } else
                        {
                            removeNodeMenuItem.setEnabled(false);
                            connectNodeMenu.setEnabled(false);
                        }
                    } else
                    if(type.equals("180"))
                    {
                        nodeInformationMenuItem.setEnabled(false);
                        removeNodeMenuItem.setEnabled(false);
                        if(isEditable())
                            connectNodeMenu.setEnabled(true);
                        else
                            connectNodeMenu.setEnabled(false);
                    } else
                    if(type.equals("190"))
                    {
                        nodeInformationMenuItem.setEnabled(false);
                        JFrame contframe;
                        if(isEditable())
                            contframe = (JFrame)JOptionPane.getFrameForComponent(this);
                        else
                            removeNodeMenuItem.setEnabled(false);
                        connectNodeMenu.setEnabled(false);
                    } else
                    if(type.equals("w") || type.equals("R") || type.equals("r") || type.equals("A") || type.equals("a") || type.equals("T"))
                    {
                        if(editable)
                            if(status.equals("WFS130") || status.equals("WFS190"))
                            {
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenu.setEnabled(false);
                            } else
                            if(status.equals("WFS110") || status.equals("WFS180"))
                            {
                                removeNodeMenuItem.setEnabled(false);
                                connectNodeMenu.setEnabled(true);
                            } else
                            {
                                removeNodeMenuItem.setEnabled(true);
                                connectNodeMenu.setEnabled(true);
                            }
                    } else
                    if(_selectedNode.getProperty("date.closed") != null && !_selectedNode.getProperty("date.closed").equals(""))
                    {
                        nodeInformationMenuItem.setEnabled(true);
                        removeNodeMenuItem.setEnabled(false);
                        connectNodeMenu.setEnabled(false);
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
                    connectorType = "N";
                    graph.repaint();
                } else
                if(evt.getActionCommand().equals("connectNodeAccept"))
                {
                    if(_selectedNode != null)
                        graph.setMode(5);
                    _tempPoint = null;
                    connectorType = "A";
                    graph.repaint();
                } else
                if(evt.getActionCommand().equals("connectNodeReject"))
                {
                    if(_selectedNode != null)
                        graph.setMode(5);
                    _tempPoint = null;
                    connectorType = "R";
                    graph.repaint();
                } else
                if(evt.getActionCommand().equals("nodeInformation"))
                {
                    if(_selectedNode == null)
                        return;
                    Object contentframe = JOptionPane.getFrameForComponent(this);
                } else
                if(evt.getActionCommand().equals("removeNode"))
                {
                    if(_selectedNode == null)
                        return;
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected workflow activity?", "Confirm deletion", 0);
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
                if(!evt.getActionCommand().equals("edgeInformation") && evt.getActionCommand().equals("removeEdge"))
                {
                    if(_selectedEdge == null)
                        return;
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected workflow branch?", "Confirm deletion", 0);
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
        connectNodeMenu.setEnabled(editable);
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
        String type = null;
        if(edge != null)
        {
            edge.setIdentifier((String)workflowConnecter.get("identifier"));
            edge.setName((String)workflowConnecter.get("name"));
            type = (String)workflowConnecter.get("type");
            if("R".equals(type))
                edge.setMode(4);
            else
            if("A".equals(type))
                edge.setMode(8);
            else
                edge.setMode(1);
        }
        return edge;
    }

    public synchronized void loadWorkflow(String processOUID)
    {
        this.processOUID = processOUID;
        graph.removeAllNodes();
        if(Utils.isNullString(processOUID))
            return;
        ArrayList nodes = null;
        ArrayList edges = null;
        try
        {
            nodes = wfm.getProcessActivities4Graph(processOUID);
            if(Utils.isNullArrayList(nodes))
            {
                updateGraphs();
                return;
            }
            DOSChangeable aNode = null;
            Node node = null;
            Edge edge = null;
            Iterator enum = nodes.iterator();
            boolean iconMode = false;
            String type = null;
            String status = null;
            String activityOuid = null;
            String temp1;
            for(; enum.hasNext(); work_status.put(temp1, status))
            {
                aNode = (DOSChangeable)enum.next();
                activityOuid = (String)aNode.get("ouid");
                status = (String)aNode.get("ouid@workflow.status");
                type = (String)aNode.get("type");
                if(type.equals("180") || type.equals("190"))
                    iconMode = true;
                else
                if(!Utils.isNullString(activityOuid) && activityOuid.startsWith("wf$dact@"))
                    iconMode = true;
                else
                    iconMode = false;
                int x = Utils.getInt((Integer)aNode.get("x"));
                int y = Utils.getInt((Integer)aNode.get("y"));
                node = insertNode(aNode, x >= 1 ? x : 1, y >= 1 ? y : 1, iconMode);
                node.setProperty("type", type);
                node.setProperty("status", status);
                temp1 = (String)aNode.get("ouid");
            }

            nodes.clear();
            nodes = null;
            edges = wfm.getProcessTransitions4Graph(processOUID);
            Object working = null;
            String strTemp = null;
            for(enum = edges.iterator(); enum.hasNext(); edge.setProperty("status", working))
            {
                aNode = (DOSChangeable)enum.next();
                strTemp = (String)aNode.get("ouid@act2");
                node = graph.getNode((String)aNode.get("ouid@act1"));
                aNode.put("ouid@act1", node);
                node = graph.getNode((String)aNode.get("ouid@act2"));
                aNode.put("ouid@act2", node);
                working = work_status.get(strTemp);
                edge = insertEdge(aNode);
            }

            edges.clear();
        }
        catch(IIPRequestException re)
        {
            System.err.println(re.getLocalizedMessage());
        }
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

    private WFM wfm;
    private DOS dos;
    private ArrayList objs;
    private Node _selectedNode;
    private Node _selectedNode2;
    private Edge _selectedEdge;
    private Rectangle _tempRectangle;
    private Point _tempPoint;
    private boolean editable;
    private boolean removeDefinition;
    private String processOUID;
    private DialogReturnCallback callback;
    private int evtX;
    private int evtY;
    private float scale;
    private int ScrollBarStepSize;
    private HashMap work_status;
    private SimpleDateFormat sdf2;
    private SimpleDateFormat sdf4;
    private Color highlitedColor;
    private String connectorType;
    private JPopupMenu nodePopupMenu;
    private JMenuItem nodeInformationMenuItem;
    private JMenu connectNodeMenu;
    private JMenuItem connectRegularMenuItem;
    private JMenuItem connectAcceptMenuItem;
    private JMenuItem connectRejectMenuItem;
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
    private Graph graph;
    private DialogReturnCallback insertCallback;
    private DialogReturnCallback deleteNodeCallback;













}
