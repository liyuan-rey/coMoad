// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractNode.java

package dyna.uic.graph;

import java.awt.*;
import java.util.*;

// Referenced classes of package dyna.uic.graph:
//            Node, Edge

public abstract class AbstractNode extends Observable
    implements Node
{

    public AbstractNode()
    {
        _selected = false;
        _oid = null;
        _identifier = null;
        _name = null;
        _userData = null;
        _readyToDisplay = false;
        _maximumWidth = 0;
        _maximumHeight = 0;
        _minimumWidth = 0;
        _minimumHeight = 0;
        _preferredWidth = 0;
        _preferredHeight = 0;
        _rectangle = null;
        _edges1 = null;
        _edges2 = null;
        _properties = null;
        _mode = 1;
        _rectangle = new Rectangle();
        _edges1 = new ArrayList();
        _edges2 = new ArrayList();
        _properties = new HashMap();
    }

    public void setChanged(boolean changed)
    {
        if(changed)
            setChanged();
        else
            clearChanged();
    }

    public boolean isChanged()
    {
        return hasChanged();
    }

    public void addEdge1(Edge edge)
    {
        _edges1.add(edge);
        deleteObserver(edge);
        addObserver(edge);
    }

    public void addEdge2(Edge edge)
    {
        _edges2.add(edge);
        deleteObserver(edge);
        addObserver(edge);
    }

    public void deleteEdge1(Edge edge)
    {
        _edges1.remove(edge);
        deleteObserver(edge);
    }

    public void deleteEdge2(Edge edge)
    {
        _edges2.remove(edge);
        deleteObserver(edge);
    }

    public void deleteAllEdges()
    {
        _edges1.clear();
        _edges2.clear();
        deleteObservers();
    }

    public ArrayList getEdges1()
    {
        return _edges1;
    }

    public ArrayList getEdges2()
    {
        return _edges2;
    }

    public int countEdges1()
    {
        return _edges1.size();
    }

    public int countEdges2()
    {
        return _edges2.size();
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

    public void setMaximumSize(Dimension maximumSize)
    {
        if(_minimumWidth < maximumSize.width)
            _maximumWidth = maximumSize.width;
        else
            _maximumWidth = _minimumWidth;
        if(_minimumHeight < maximumSize.height)
            _maximumHeight = maximumSize.height;
        else
            _maximumHeight = _minimumHeight;
        if(_rectangle.width > _maximumWidth)
            _rectangle.width = _maximumWidth;
        if(_rectangle.height > _maximumHeight)
            _rectangle.height = _maximumHeight;
    }

    public Dimension getMaximumSize()
    {
        return new Dimension(_maximumWidth, _maximumHeight);
    }

    public void setMinimumSize(Dimension minimumSize)
    {
        if(_maximumWidth > minimumSize.width)
            _minimumWidth = minimumSize.width;
        else
            _minimumWidth = _maximumWidth;
        if(_maximumHeight > minimumSize.height)
            _minimumHeight = minimumSize.height;
        else
            _minimumHeight = _maximumHeight;
        if(_rectangle.width < _minimumWidth)
            _rectangle.width = _minimumWidth;
        if(_rectangle.height < _minimumHeight)
            _rectangle.height = _minimumHeight;
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(_minimumWidth, _minimumHeight);
    }

    public void setPreferredSize(Dimension preferredSize)
    {
        _preferredWidth = preferredSize.width;
        _preferredHeight = preferredSize.height;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(_preferredWidth, _preferredHeight);
    }

    public Rectangle getBounds()
    {
        return _rectangle;
    }

    public void setSize(Dimension size)
    {
        _rectangle.setSize(size);
        if(!isChanged())
        {
            setChanged();
            notifyObservers();
        }
    }

    public Dimension getSize(Dimension size)
    {
        size.setSize(_rectangle.getSize());
        return size;
    }

    public int getWidth()
    {
        return _rectangle.width;
    }

    public int getHeight()
    {
        return _rectangle.height;
    }

    public void setLocation(int x, int y)
    {
        _rectangle.setLocation(x, y);
        if(!isChanged())
        {
            setChanged();
            notifyObservers();
        }
    }

    public void setLocation(Point xy)
    {
        _rectangle.setLocation(xy);
    }

    public Point getLocation(Point xy)
    {
        xy.setLocation(_rectangle.getLocation());
        return xy;
    }

    public int getX()
    {
        return _rectangle.x;
    }

    public int getY()
    {
        return _rectangle.y;
    }

    public void translate(int deltaX, int deltaY)
    {
        _rectangle.translate(deltaX, deltaY);
        setChanged();
    }

    public void translate(Point deltaXY)
    {
        _rectangle.translate(deltaXY.x, deltaXY.y);
    }

    public void add(int x, int y)
    {
        _rectangle.add(x, y);
        setChanged();
    }

    public void add(Point xy)
    {
        _rectangle.add(xy);
    }

    public void grow(int width, int height)
    {
        _rectangle.grow(width, height);
        setChanged();
    }

    public void grow(Dimension size)
    {
        _rectangle.grow(size.width, size.height);
    }

    public boolean intersects(Rectangle rectangle)
    {
        return _rectangle.intersects(rectangle);
    }

    public int getCenterX()
    {
        return _rectangle.x + (_rectangle.width >> 1);
    }

    public int getCenterY()
    {
        return _rectangle.y + (_rectangle.height >> 1);
    }

    public Point getCenterLocation(Point xy)
    {
        xy.setLocation(getCenterX(), getCenterY());
        return xy;
    }

    public boolean contains(int x, int y)
    {
        return _rectangle.contains(x, y);
    }

    public boolean contains(Point xy)
    {
        return _rectangle.contains(xy);
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
    private boolean _readyToDisplay;
    private int _maximumWidth;
    private int _maximumHeight;
    private int _minimumWidth;
    private int _minimumHeight;
    private int _preferredWidth;
    private int _preferredHeight;
    private Rectangle _rectangle;
    private ArrayList _edges1;
    private ArrayList _edges2;
    private HashMap _properties;
}
