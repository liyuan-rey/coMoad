// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjectModelDefaultTreeCellRenderer.java

package dyna.framework.editor.modeler;

import dyna.uic.TreeNodeObject;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ObjectModelDefaultTreeCellRenderer extends DefaultTreeCellRenderer
{

    public ObjectModelDefaultTreeCellRenderer()
    {
        modelImage = null;
        packageImage = null;
        classImage = null;
        fieldFolderImage = null;
        actionFolderImage = null;
        fieldGroupFolderImage = null;
        fieldImage = null;
        actionImage = null;
        fieldGroupImage = null;
        inheritedFieldImage = null;
        codeImage = null;
        inheritedActionImage = null;
        inheritedFieldGroupImage = null;
        generalFolderImage = null;
        generalNodeImage = null;
        modelImage = new ImageIcon(getClass().getResource("/icons/Model.gif"));
        packageImage = new ImageIcon(getClass().getResource("/icons/Package.gif"));
        classImage = new ImageIcon(getClass().getResource("/icons/class.gif"));
        fieldFolderImage = new ImageIcon(getClass().getResource("/icons/FieldFolder.gif"));
        actionFolderImage = new ImageIcon(getClass().getResource("/icons/ActionFolder.gif"));
        fieldGroupFolderImage = new ImageIcon(getClass().getResource("/icons/FieldGroupFolder.gif"));
        fieldImage = new ImageIcon(getClass().getResource("/icons/Field.gif"));
        actionImage = new ImageIcon(getClass().getResource("/icons/Action.gif"));
        fieldGroupImage = new ImageIcon(getClass().getResource("/icons/FieldGroup.gif"));
        inheritedFieldImage = new ImageIcon(getClass().getResource("/icons/InheritedField.gif"));
        codeImage = new ImageIcon(getClass().getResource("/icons/code.gif"));
        inheritedActionImage = new ImageIcon(getClass().getResource("/icons/InheritedAction.gif"));
        inheritedFieldGroupImage = new ImageIcon(getClass().getResource("/icons/InheritedFieldGroup.gif"));
        generalFolderImage = new ImageIcon(getClass().getResource("/icons/fileFolder.gif"));
        generalNodeImage = new ImageIcon(getClass().getResource("/icons/code.gif"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        int i = setImagecheck(value);
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            setIcon(modelImage);
            break;

        case 1: // '\001'
            setIcon(packageImage);
            break;

        case 2: // '\002'
            setIcon(classImage);
            break;

        case 3: // '\003'
            setIcon(fieldFolderImage);
            break;

        case 4: // '\004'
            setIcon(actionFolderImage);
            break;

        case 5: // '\005'
            setIcon(fieldGroupFolderImage);
            break;

        case 6: // '\006'
            setIcon(fieldImage);
            break;

        case 7: // '\007'
            setIcon(actionImage);
            break;

        case 8: // '\b'
            setIcon(fieldGroupImage);
            break;

        case 9: // '\t'
            setIcon(inheritedFieldImage);
            break;

        case 10: // '\n'
            setIcon(codeImage);
            break;

        case 11: // '\013'
            setIcon(inheritedActionImage);
            break;

        case 12: // '\f'
            setIcon(inheritedFieldGroupImage);
            break;

        case 99: // 'c'
            if(!leaf)
                setIcon(generalFolderImage);
            else
                setIcon(generalNodeImage);
            break;
        }
        setFont(new Font("Dialog", 0, 12));
        return this;
    }

    protected int setImagecheck(Object value)
    {
        int index = 0;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object nodeObj = node.getUserObject();
        int levelOfTree = node.getLevel();
        if(nodeObj instanceof TreeNodeObject)
        {
            String oid = ((TreeNodeObject)nodeObj).getDescription();
            if(oid != null)
            {
                if(oid.startsWith("Model"))
                    return 0;
                if(oid.startsWith("Package"))
                    return 1;
                if(oid.startsWith("Class"))
                    return 2;
                if(oid.startsWith("Field Folder"))
                    return 3;
                if(oid.startsWith("Action Folder"))
                    return 4;
                if(oid.startsWith("FGroup Folder"))
                    return 5;
                if(oid.startsWith("Field"))
                    return 6;
                if(oid.startsWith("Action"))
                    return 7;
                if(oid.startsWith("FGroup"))
                    return 8;
                if(oid.startsWith("Inherited Field"))
                    return 9;
                if(oid.startsWith("Code"))
                    return 10;
                if(oid.startsWith("Inherited Action"))
                    return 11;
                return !oid.startsWith("Inherited FGroup") ? 99 : 12;
            }
        }
        return 99;
    }

    ImageIcon modelImage;
    ImageIcon packageImage;
    ImageIcon classImage;
    ImageIcon fieldFolderImage;
    ImageIcon actionFolderImage;
    ImageIcon fieldGroupFolderImage;
    ImageIcon fieldImage;
    ImageIcon actionImage;
    ImageIcon fieldGroupImage;
    ImageIcon inheritedFieldImage;
    ImageIcon codeImage;
    ImageIcon inheritedActionImage;
    ImageIcon inheritedFieldGroupImage;
    ImageIcon generalFolderImage;
    ImageIcon generalNodeImage;
}
