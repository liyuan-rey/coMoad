// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractEdge.java

package dyna.uic.graph;

import java.awt.*;
import java.util.*;

// Referenced classes of package dyna.uic.graph:
//            Edge, Node, AbstractNode

public abstract class AbstractEdge
    implements Edge
{

    public AbstractEdge()
    {
        _selected = false;
        _oid = null;
        _identifier = null;
        _name = null;
        _userData = null;
        _polylineX = null;
        _polylineY = null;
        _arrowPolygon = null;
        _xs = new int[6];
        _ys = new int[6];
        _boundsPolygon = null;
        _rectangle = null;
        _nodes = null;
        _properties = null;
        _mode = 1;
        _polylineX = new int[3];
        _polylineY = new int[3];
        _boundsPolygon = new Polygon();
        _arrowPolygon = new Polygon();
        _nodes = new ArrayList();
        _nodes.add(null);
        _nodes.add(null);
        _properties = new HashMap();
        _arrowPolygon.addPoint(0, 0);
        _arrowPolygon.addPoint(0, 0);
        _arrowPolygon.addPoint(0, 0);
    }

    public void addNode(int index, Node node)
    {
        _nodes.set(index, node);
    }

    public void deleteNode(int index)
    {
        _nodes.set(index, null);
    }

    public Node getNode(int index)
    {
        return (Node)_nodes.get(index);
    }

    public Point getEndLocation(int index)
    {
        return (Point)_nodes.get(index);
    }

    public void setMode(int mode)
    {
        _mode = mode;
    }

    public int getMode()
    {
        return _mode;
    }

    public void setSelected(boolean selected)
    {
        _selected = selected;
    }

    public boolean isSelected()
    {
        return _selected;
    }

    public void setOid(String oid)
    {
        _oid = oid;
    }

    public String getOid()
    {
        return _oid;
    }

    public void setIdentifier(String identifier)
    {
        _identifier = identifier;
    }

    public String getIdentifier()
    {
        return _identifier != null ? _identifier : "<null>";
    }

    public void setName(String name)
    {
        _name = name;
    }

    public String getName()
    {
        return _name != null ? _name : "<null>";
    }

    public void setUserData(Object userData)
    {
        _userData = userData;
    }

    public Object getUserData()
    {
        return _userData;
    }

    public abstract void update(Observable observable, Object obj);

    public void computeBounds()
    {
        int n = 0;
        int n2 = 6;
        _boundsPolygon = null;
        int xDir = 0;
        if(_xs[0] < _xs[2])
            xDir = _ys[0] >= _ys[2] ? 1 : -1;
        else
            xDir = _ys[0] >= _ys[2] ? -1 : 1;
        for(; n < 3; n++)
        {
            _xs[n] = _polylineX[n] - 2 * xDir;
            _ys[n] = _polylineY[n] - 2;
            n2--;
            _xs[n2] = _polylineX[n] + 3 * xDir;
            _ys[n2] = _polylineY[n] + 3;
        }

        _boundsPolygon = new Polygon(_xs, _ys, 6);
        _rectangle = _boundsPolygon.getBounds();
    }

    public Dimension getSize(Dimension size)
    {
        if(_rectangle == null)
            computeBounds();
        size.setSize(_rectangle.getSize());
        return size;
    }

    public int getWidth()
    {
        if(_rectangle == null)
            computeBounds();
        return _rectangle.width;
    }

    public int getHeight()
    {
        if(_rectangle == null)
            computeBounds();
        return _rectangle.height;
    }

    public Point getLocation(Point xy)
    {
        if(_rectangle == null)
            computeBounds();
        xy.setLocation(_rectangle.getLocation());
        return xy;
    }

    public int getX()
    {
        if(_rectangle == null)
            computeBounds();
        return _rectangle.x;
    }

    public int getY()
    {
        if(_rectangle == null)
            computeBounds();
        return _rectangle.y;
    }

    public void translate(int deltaX, int deltaY)
    {
        for(int n = 0; n < 3; n++)
        {
            _polylineX[n] += deltaX;
            _polylineY[n] += deltaY;
        }

        if(_rectangle != null)
            _boundsPolygon.translate(deltaX, deltaY);
    }

    public void translate(Point deltaXY)
    {
        translate(deltaXY.x, deltaXY.y);
    }

    public int getCenterX()
    {
        if(_rectangle == null)
            computeBounds();
        return _rectangle.x + (_rectangle.width >> 1);
    }

    public int getCenterY()
    {
        if(_rectangle == null)
            computeBounds();
        return _rectangle.y + (_rectangle.height >> 1);
    }

    public Point getCenterLocation(Point xy)
    {
        if(_rectangle == null)
            computeBounds();
        xy.setLocation(getCenterX(), getCenterY());
        return xy;
    }

    public boolean contains(int x, int y)
    {
        if(_rectangle == null)
            computeBounds();
        return _boundsPolygon.contains(x, y);
    }

    public boolean contains(Point xy)
    {
        if(_rectangle == null)
            computeBounds();
        return _boundsPolygon.contains(xy);
    }

    public void updateGeometry()
    {
        AbstractNode node1 = (AbstractNode)getNode(0);
        AbstractNode node2 = (AbstractNode)getNode(1);
        if(node1 == null || node2 == null)
            return;
        if(node1.getX() < node2.getX())
        {
            _polylineX[0] = node1.getX() + node1.getWidth();
            _polylineY[0] = node1.getCenterY() - 4;
            _polylineX[1] = node2.getX() - 15;
            _polylineY[1] = node2.getCenterY() - 4;
            _polylineX[2] = node2.getX();
            _polylineY[2] = node2.getCenterY() - 4;
            _arrowPolygon.xpoints[0] = _polylineX[2];
            _arrowPolygon.ypoints[0] = _polylineY[2];
            _arrowPolygon.xpoints[1] = _polylineX[2] - 8;
            _arrowPolygon.ypoints[1] = _polylineY[2] + 4;
            _arrowPolygon.xpoints[2] = _polylineX[2] - 8;
            _arrowPolygon.ypoints[2] = _polylineY[2] - 4;
        } else
        {
            _polylineX[0] = node1.getX();
            _polylineY[0] = node1.getCenterY() + 4;
            _polylineX[1] = node2.getX() + node2.getWidth() + 15;
            _polylineY[1] = node2.getCenterY() + 4;
            _polylineX[2] = node2.getX() + node2.getWidth();
            _polylineY[2] = node2.getCenterY() + 4;
            _arrowPolygon.xpoints[0] = _polylineX[2];
            _arrowPolygon.ypoints[0] = _polylineY[2];
            _arrowPolygon.xpoints[1] = _polylineX[2] + 8;
            _arrowPolygon.ypoints[1] = _polylineY[2] + 4;
            _arrowPolygon.xpoints[2] = _polylineX[2] + 8;
            _arrowPolygon.ypoints[2] = _polylineY[2] - 4;
        }
        computeBounds();
    }

    public Object getProperty(String name)
    {
        return _properties.get(name);
    }

    public void setProperty(String name, Object value)
    {
        _properties.put(name, value);
    }

    public abstract void paint(Graphics g);

    public void update(Graphics g)
    {
        paint(g);
    }

    private int _mode;
    private boolean _selected;
    private String _oid;
    private String _identifier;
    private String _name;
    private Object _userData;
    protected int _polylineX[];
    protected int _polylineY[];
    protected final int _polylineN = 3;
    protected Polygon _arrowPolygon;
    private int _xs[];
    private int _ys[];
    protected Polygon _boundsPolygon;
    private Rectangle _rectangle;
    private ArrayList _nodes;
    private HashMap _properties;
}
