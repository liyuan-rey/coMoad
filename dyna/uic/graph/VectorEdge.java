// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VectorEdge.java

package dyna.uic.graph;

import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import java.awt.*;
import java.util.Observable;
import javax.swing.plaf.ColorUIResource;

// Referenced classes of package dyna.uic.graph:
//            AbstractEdge, Graph

public class VectorEdge extends AbstractEdge
{

    public VectorEdge(String oid, String identifier, String name, int mode, boolean selected, Object userData)
    {
        g2 = null;
        _colorBackup = null;
        _fontBackup = null;
        _selectionForeground = new Color(PlasticLookAndFeel.getFocusColor().getRed(), PlasticLookAndFeel.getFocusColor().getGreen(), PlasticLookAndFeel.getFocusColor().getBlue(), 150);
        setOid(oid);
        setIdentifier(identifier);
        setName(name);
        setMode(mode);
        setSelected(selected);
    }

    public VectorEdge(String oid, String identifier, String name, Object userData)
    {
        this(oid, identifier, name, 1, false, userData);
    }

    public VectorEdge(String oid, String identifier, String name)
    {
        this(oid, identifier, name, 1, false, null);
    }

    public VectorEdge(String oid, String identifier)
    {
        this(oid, identifier, null, 1, false, null);
    }

    public VectorEdge()
    {
        this(null, null, null, 1, false, null);
    }

    public void update(Observable o, Object arg)
    {
        updateGeometry();
    }

    public void paint(Graphics g)
    {
        g2 = (Graphics2D)g;
        _colorBackup = g2.getColor();
        _fontBackup = g2.getFont();
        if(_boundsPolygon != null && isSelected())
        {
            g2.setColor(_selectionForeground);
            g2.fillPolygon(_boundsPolygon);
        }
        g2.setColor(Graph.colors[getMode() + 16]);
        g2.drawPolyline(_polylineX, _polylineY, 3);
        g2.fillPolygon(_arrowPolygon);
        g2.setFont(_fontBackup);
        g2.setColor(_colorBackup);
    }

    Graphics2D g2;
    private Color _colorBackup;
    private Font _fontBackup;
    private Color _selectionForeground;
}
