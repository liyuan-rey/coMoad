// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectModelDefaultTreeCellEditor.java

package dyna.framework.editor.modeler;

import dyna.uic.TreeNodeObject;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.*;

public class ObjectModelDefaultTreeCellEditor extends DefaultTreeCellEditor
{

    public ObjectModelDefaultTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer)
    {
        super(tree, renderer);
        isDocImage = null;
        notisDocImage = null;
        partImage = null;
        standImage = null;
        genImage = null;
        assyImage = null;
        isDocImage = new ImageIcon(getClass().getResource("/icons/FormatedDoc16.gif"));
        notisDocImage = new ImageIcon(getClass().getResource("/icons/NonFormatedDoc16.gif"));
        partImage = new ImageIcon(getClass().getResource("/icons/Product16.gif"));
        standImage = new ImageIcon(getClass().getResource("/icons/StandardPart16.gif"));
        genImage = new ImageIcon(getClass().getResource("/icons/EndPart16.gif"));
        assyImage = new ImageIcon(getClass().getResource("/icons/Assembly16.gif"));
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
    {
        super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
        setFont(new Font("Dialog", 0, 12));
        return null;
    }

    protected int setImagecheck(Object value)
    {
        int index = 0;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object nodeObj = node.getUserObject();
        if(nodeObj instanceof TreeNodeObject)
        {
            String oid = ((TreeNodeObject)nodeObj).getOuid();
            if(oid.startsWith("DCT"))
            {
                index = 1;
                return index;
            }
            if(oid.startsWith("TEST") || oid.startsWith("WIP"))
            {
                index = 2;
                return index;
            }
            if(oid.startsWith("FGROUP") || oid.startsWith("WIA"))
            {
                index = 41;
                return index;
            } else
            {
                index = 3;
                return index;
            }
        } else
        {
            return 0;
        }
    }

    public void setTree(JTree tree)
    {
        super.setTree(tree);
    }

    public void startEditingTimer()
    {
        super.startEditingTimer();
    }

    public boolean getCanEdit()
    {
        return super.canEdit;
    }

    public void prepareForEditing()
    {
        super.prepareForEditing();
    }

    public void createContainerObject()
    {
        super.createContainer();
    }

    public void setTextField(String s)
    {
    }

    ImageIcon isDocImage;
    ImageIcon notisDocImage;
    ImageIcon partImage;
    ImageIcon standImage;
    ImageIcon genImage;
    ImageIcon assyImage;
}
