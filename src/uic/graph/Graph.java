// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Graph.java

package dyna.uic.graph;

import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.Scrollable;

// Referenced classes of package dyna.uic.graph:
//            Node, Edge, WorkflowNode, Element

public class Graph extends JComponent
    implements Scrollable
{

    public Graph()
    {
        g2 = null;
        stroke = new BasicStroke(1.0F);
        gridPixel = 20;
        _nodes = null;
        _edges = null;
        _nodeClass = null;
        _edgeClass = null;
        _enum = null;
        _tempRectangle = null;
        _tempRectangle2 = null;
        _tempRectangle3 = null;
        showGridLine = false;
        scale = 1.0F;
        _nodes = new ArrayList();
        _edges = new ArrayList();
        setMinimumSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
        setMaximumSize(new Dimension(50000, 50000));
        _mode = 0;
        if(colors == null)
        {
            colors = new Color[256];
            colors[1] = new Color(192, 202, 228);
            colors[2] = new Color(222, 229, 239);
            colors[3] = Color.green;
            colors[4] = Color.red;
            colors[5] = Color.lightGray;
            colors[6] = new Color(113, 149, 146);
            colors[7] = new Color(156, 106, 106);
            colors[8] = Color.blue;
            colors[9] = new Color(222, 229, 239);
            colors[17] = Color.black;
            colors[18] = Color.black;
            colors[19] = Color.green;
            colors[20] = Color.red;
            colors[21] = Color.lightGray;
            colors[22] = new Color(113, 149, 146);
            colors[23] = new Color(156, 106, 106);
            colors[24] = Color.blue;
            colors[25] = Color.black;
            defaultFonts = new Font[4];
            defaultFonts[0] = new Font("dialog", 0, 12);
            defaultFonts[1] = new Font("dialog", 1, 12);
            defaultFonts[2] = new Font("dialog", 2, 12);
            defaultFonts[3] = new Font("dialog", 3, 12);
        }
    }

    public synchronized void setMode(int mode)
    {
        _mode = mode;
    }

    public int getMode()
    {
        return _mode;
    }

    private int getNodeIndexForOid(String oid)
    {
        int index;
        for(index = _nodes.size() - 1; index >= 0; index--)
            if(_nodes.get(index) != null && ((Node)_nodes.get(index)).getOid().equals(oid))
                break;

        return index;
    }

    private int getNodeIndexForLocation(int x, int y)
    {
        int index;
        for(index = _nodes.size() - 1; index >= 0; index--)
            if(_nodes.get(index) != null && ((Node)_nodes.get(index)).contains(x, y))
                break;

        return index;
    }

    private int getEdgeIndexForOid(String oid)
    {
        int index;
        for(index = _edges.size() - 1; index >= 0; index--)
            if(_edges.get(index) != null && ((Edge)_edges.get(index)).getOid().equals(oid))
                break;

        return index;
    }

    private int getEdgeIndexForLocation(int x, int y)
    {
        int index;
        for(index = _edges.size() - 1; index >= 0; index--)
            if(_edges.get(index) != null && ((Edge)_edges.get(index)).contains(x, y))
                break;

        return index;
    }

    public synchronized void setNodeClass(String className)
    {
        try
        {
            _nodeClass = Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            _nodeClass = null;
        }
    }

    public Class getNodeClass()
    {
        return _nodeClass;
    }

    public synchronized void setEdgeClass(String className)
    {
        try
        {
            _edgeClass = Class.forName(className);
        }
        catch(ClassNotFoundException e)
        {
            _edgeClass = null;
        }
    }

    public Class getEdgeClass()
    {
        return _edgeClass;
    }

    private synchronized String addNode(String oid, Class nodeClass, int x, int y, boolean iconMode)
    {
        if(oid == null || nodeClass == null)
            return null;
        try
        {
            Node node = (Node)nodeClass.newInstance();
            node.setOid(oid);
            if(iconMode)
                ((WorkflowNode)node).setIconMode(iconMode);
            node.setLocation(x, y);
            _nodes.add(node);
            computeSize(node);
        }
        catch(InstantiationException e)
        {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        catch(IllegalAccessException e)
        {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        return oid;
    }

    public synchronized String addNode(String oid, int x, int y, boolean iconMode)
    {
        return addNode(oid, _nodeClass, x, y, iconMode);
    }

    public synchronized String addNode(String oid, int x, int y)
    {
        return addNode(oid, _nodeClass, x, y, false);
    }

    public synchronized String addNode(String oid, Point xy)
    {
        return addNode(oid, _nodeClass, xy.x, xy.y, false);
    }

    private synchronized void removeNode(int index)
    {
        Node node = (Node)_nodes.get(index);
        Edge edge = null;
        ArrayList edges = null;
        int i = 0;
        edges = node.getEdges1();
        for(i = edges.size() - 1; i >= 0; i--)
        {
            edge = (Edge)edges.get(i);
            removeEdge(edge.getOid());
        }

        edges = node.getEdges2();
        for(i = edges.size() - 1; i >= 0; i--)
        {
            edge = (Edge)edges.get(i);
            removeEdge(edge.getOid());
        }

        _nodes.remove(index);
    }

    public synchronized void removeNode(String oid)
    {
        removeNode(getNodeIndexForOid(oid));
    }

    public synchronized void removeNodeForLocation(int x, int y)
    {
        removeNode(getNodeIndexForLocation(x, y));
    }

    private Node getNode(int index)
    {
        return index <= -1 ? null : (Node)_nodes.get(index);
    }

    public Node getNode(String oid)
    {
        return getNode(getNodeIndexForOid(oid));
    }

    public Node getNodeForLocation(int x, int y)
    {
        return getNode(getNodeIndexForLocation(x, y));
    }

    private synchronized String addEdge(String oid, Class edgeClass, Node node1, Node node2)
    {
        if(oid == null || edgeClass == null || node1 == null || node2 == null)
            return null;
        try
        {
            Edge edge = (Edge)edgeClass.newInstance();
            edge.setOid(oid);
            node1.addEdge2(edge);
            node2.addEdge1(edge);
            edge.addNode(0, node1);
            edge.addNode(1, node2);
            _edges.add(edge);
            edge.updateGeometry();
        }
        catch(InstantiationException e)
        {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        catch(IllegalAccessException e)
        {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        return oid;
    }

    public synchronized String addEdge(String oid, Node node1, Node node2)
    {
        return addEdge(oid, _edgeClass, node1, node2);
    }

    private synchronized void removeEdge(int index)
    {
        Edge edge = (Edge)_edges.get(index);
        edge.getNode(0).deleteEdge2(edge);
        edge.getNode(1).deleteEdge1(edge);
        edge.deleteNode(0);
        edge.deleteNode(1);
        _edges.remove(index);
        edge = null;
    }

    public synchronized void removeEdge(String oid)
    {
        removeEdge(getEdgeIndexForOid(oid));
    }

    public synchronized void removeEdgeForLocation(int x, int y)
    {
        removeEdge(getEdgeIndexForLocation(x, y));
    }

    private Edge getEdge(int index)
    {
        return index <= -1 ? null : (Edge)_edges.get(index);
    }

    public Edge getEdge(String oid)
    {
        return getEdge(getEdgeIndexForOid(oid));
    }

    public Edge getEdgeForLocation(int x, int y)
    {
        return getEdge(getEdgeIndexForLocation(x, y));
    }

    public boolean isConnectedNodes(Node node1, Node node2)
    {
        if(node1 == null || node2 == null)
            return false;
        if(node1.equals(node2))
            return true;
        ArrayList edges1 = node1.getEdges2();
        Iterator edges2;
        for(edges2 = node2.getEdges1().iterator(); edges2.hasNext();)
        {
            Edge edge = (Edge)edges2.next();
            if(edges1.contains(edge))
            {
                edges1 = null;
                edges2 = null;
                return true;
            }
        }

        edges1 = null;
        edges2 = null;
        return false;
    }

    public void updateComponentImmediate(Element element)
    {
        Graphics g = getGraphics();
        element.update(g);
        g.dispose();
    }

    protected void paintComponent(Graphics g)
    {
        g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setStroke(stroke);
        initFontMetrics(g);
        height = getHeight();
        width = getWidth();
        position = gridPixel;
        g2.setColor(Color.white);
        g2.scale(scale, scale);
        if(showGridLine)
        {
            for(int i = 0; i < width; i++)
            {
                g2.drawLine(0, position, width, position);
                g2.drawLine(position, 0, position, height);
                position += gridPixel;
            }

        }
        if(_nodes.size() == 0)
            return;
        for(_enum = _edges.iterator(); _enum.hasNext(); ((Element)_enum.next()).paint(g));
        _enum = null;
        for(_enum = _nodes.iterator(); _enum.hasNext(); ((Element)_enum.next()).paint(g));
        _enum = null;
        g2 = null;
    }

    public synchronized void computeSize(Node node)
    {
        int tempInt = 0;
        _tempRectangle = getBounds();
        _tempRectangle3 = node.getBounds();
        tempInt = _tempRectangle3.height + _tempRectangle3.y;
        if(_tempRectangle.height <= tempInt)
            _tempRectangle.height = tempInt + 50;
        tempInt = _tempRectangle3.width + _tempRectangle3.x;
        if(_tempRectangle.width <= tempInt)
            _tempRectangle.width = tempInt + 50;
        setSize(_tempRectangle.getSize());
        setPreferredSize(_tempRectangle.getSize());
        _tempRectangle3 = null;
    }

    public void removeAllNodes()
    {
        int sizeNodes = _nodes.size();
        for(int i = sizeNodes - 1; i >= 0; i--)
            removeNode(i);

    }

    public void initFontMetrics(Graphics g)
    {
        if(defaultFontMetrics == null)
        {
            defaultFontMetrics = new FontMetrics[4];
            if(g != null)
            {
                defaultFontMetrics[0] = g.getFontMetrics(defaultFonts[0]);
                defaultFontMetrics[1] = g.getFontMetrics(defaultFonts[1]);
                defaultFontMetrics[2] = g.getFontMetrics(defaultFonts[2]);
                defaultFontMetrics[3] = g.getFontMetrics(defaultFonts[3]);
            }
        }
    }

    public void computeViewportSize()
    {
        for(Iterator enum = _nodes.iterator(); enum.hasNext(); computeSize((Node)enum.next()));
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle p1, int p2, int p3)
    {
        return 20;
    }

    public int getScrollableBlockIncrement(Rectangle p1, int p2, int p3)
    {
        return p2 != 1 ? p1.width / 2 : p1.height / 2;
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return getPreferredSize();
    }

    public void setShowGridLine(boolean show)
    {
        showGridLine = show;
    }

    public boolean getShowGridLine()
    {
        return showGridLine;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
        _tempRectangle2 = (Rectangle)_tempRectangle.clone();
        _tempRectangle2.height = (new Float((float)_tempRectangle2.height * this.scale)).intValue() + 10;
        _tempRectangle2.width = (new Float((float)_tempRectangle2.width * this.scale)).intValue() + 10;
        setSize(_tempRectangle2.getSize());
        setPreferredSize(_tempRectangle2.getSize());
        _tempRectangle2 = null;
    }

    public float getScale()
    {
        return scale;
    }

    private Graphics2D g2;
    private Stroke stroke;
    private int gridPixel;
    private int height;
    private int width;
    private int position;
    public static final int MODE_NONE = 0;
    public static final int MODE_NODE_SELECT = 1;
    public static final int MODE_NODE_MOVE = 2;
    public static final int MODE_NODE_INSERT = 3;
    public static final int MODE_NODE_CONNECT1 = 4;
    public static final int MODE_NODE_CONNECT2 = 5;
    public static Color colors[] = null;
    public static Font defaultFonts[] = null;
    public static FontMetrics defaultFontMetrics[] = null;
    private int _mode;
    private ArrayList _nodes;
    private ArrayList _edges;
    private Class _nodeClass;
    private Class _edgeClass;
    private Iterator _enum;
    private Rectangle _tempRectangle;
    private Rectangle _tempRectangle2;
    private Rectangle _tempRectangle3;
    private boolean showGridLine;
    private float scale;

}
