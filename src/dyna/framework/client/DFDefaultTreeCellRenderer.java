// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:33
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DFDefaultTreeCellRenderer.java

package dyna.framework.client;

import com.jgoodies.swing.util.CompoundIcon;
import dyna.uic.TreeNodeObject;
import dyna.util.Utils;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

// Referenced classes of package dyna.framework.client:
//            ClientUtil

public class DFDefaultTreeCellRenderer extends DefaultTreeCellRenderer
{

    public DFDefaultTreeCellRenderer()
    {
        packageImage = null;
        defaultClassImage = null;
        classImage = null;
        actionImage = null;
        modelImage = null;
        fieldImage = null;
        fieldGroupImage = null;
        favoriteImage = null;
        recentImage = null;
        inboxImage = null;
        sentImage = null;
        completedImage = null;
        defaultFolderIcon = null;
        defaultOpenFolderIcon = null;
        defaultFileFolderIcon = null;
        defaultFileIcon = null;
        clientUtil = null;
        modelImage = new ImageIcon(getClass().getResource("/icons/Model.gif"));
        packageImage = new ImageIcon(getClass().getResource("/icons/Package.gif"));
        defaultClassImage = new ImageIcon(getClass().getResource("/icons/class.gif"));
        classImage = new ImageIcon(getClass().getResource("/icons/class.gif"));
        fieldImage = new ImageIcon(getClass().getResource("/icons/Field.gif"));
        actionImage = new ImageIcon(getClass().getResource("/icons/Action.gif"));
        fieldGroupImage = new ImageIcon(getClass().getResource("/icons/FieldGroup.gif"));
        favoriteImage = new ImageIcon(getClass().getResource("/icons/Favorite.gif"));
        recentImage = new ImageIcon(getClass().getResource("/icons/Recent.gif"));
        inboxImage = new ImageIcon(getClass().getResource("/icons/ComposeMail16.gif"));
        sentImage = new ImageIcon(getClass().getResource("/icons/SendMail16.gif"));
        completedImage = new ImageIcon(getClass().getResource("/icons/CompletedMail.gif"));
        defaultFolderIcon = new ImageIcon(getClass().getResource("/icons/defaultFolder.gif"));
        defaultOpenFolderIcon = new ImageIcon(getClass().getResource("/icons/defaultOpenFolder.gif"));
        defaultFileFolderIcon = new ImageIcon(getClass().getResource("/icons/fileFolder.gif"));
        defaultFileIcon = new ImageIcon(getClass().getResource("/icons/file_obj.gif"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        int i = setImagecheck(value);
        switch(i)
        {
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
            setIcon(fieldImage);
            break;

        case 4: // '\004'
            setIcon(actionImage);
            break;

        case 5: // '\005'
            setIcon(actionImage);
            break;

        case 6: // '\006'
            setIcon(classImage);
            break;

        case 7: // '\007'
            setIcon(favoriteImage);
            break;

        case 8: // '\b'
            setIcon(recentImage);
            break;

        case 9: // '\t'
            setIcon(inboxImage);
            break;

        case 10: // '\n'
            setIcon(sentImage);
            break;

        case 11: // '\013'
            setLeafIcon(defaultFolderIcon);
            setClosedIcon(defaultFolderIcon);
            setOpenIcon(defaultOpenFolderIcon);
            break;

        case 12: // '\f'
            setLeafIcon(defaultFolderIcon);
            setClosedIcon(defaultFolderIcon);
            setOpenIcon(defaultOpenFolderIcon);
            break;

        case 13: // '\r'
            setIcon(completedImage);
            break;
        }
        setFont(new Font("Dialog", 0, 12));
        return this;
    }

    protected int setImagecheck(Object value)
    {
        int index = 0;
        ImageIcon imageIconFromSvr = null;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object nodeObj = node.getUserObject();
        TreeNodeObject treeNodeObject = null;
        java.util.List tempList = null;
        if(nodeObj instanceof TreeNodeObject)
        {
            treeNodeObject = (TreeNodeObject)nodeObj;
            String oid = treeNodeObject.getDescription();
            String description = treeNodeObject.getOuid();
            if(!Utils.isNullString(description) && description.indexOf('^') >= 0)
                description = (String)Utils.tokenizeMessage(description, '^').get(1);
            if(!Utils.isNullString(description))
            {
                if(clientUtil == null)
                    clientUtil = new ClientUtil();
                imageIconFromSvr = ClientUtil.getImageIcon(description);
                if(imageIconFromSvr != null)
                    classImage = imageIconFromSvr;
                else
                    classImage = defaultClassImage;
            }
            if(!Utils.isNullString(oid))
                if(oid.startsWith("Model"))
                    index = 0;
                else
                if(oid.startsWith("Package"))
                    index = 1;
                else
                if(oid.startsWith("Class"))
                    index = 2;
                else
                if(oid.startsWith("Field"))
                    index = 3;
                else
                if(oid.startsWith("Action"))
                    index = 4;
                else
                if(oid.startsWith("Field Group"))
                    index = 5;
                else
                if(oid.startsWith("Inherited Field"))
                    index = 6;
                else
                if(oid.startsWith("Favorites"))
                    index = 7;
                else
                if(oid.startsWith("Recent"))
                    index = 8;
                else
                if(oid.equals("Inbox"))
                    index = 9;
                else
                if(oid.equals("Sent"))
                    index = 10;
                else
                if(oid.equals("Completed"))
                    index = 13;
                else
                if(oid.equals("FOLDER"))
                    index = 11;
                else
                if(oid.equals("FOLDERLINK"))
                    index = 12;
                else
                if(oid.equals("OBJECT"))
                    index = 2;
                else
                if(oid.equals(description))
                {
                    if(imageIconFromSvr == null)
                        classImage = defaultFileFolderIcon;
                    else
                        classImage = new CompoundIcon(defaultFileFolderIcon, imageIconFromSvr, 6);
                    index = 2;
                } else
                if(oid.indexOf('^') >= 0 && description.indexOf('@') >= 0)
                {
                    imageIconFromSvr = null;
                    tempList = Utils.tokenizeMessage(oid, '^');
                    if(tempList.size() > 1)
                    {
                        if(clientUtil == null)
                            clientUtil = new ClientUtil();
                        imageIconFromSvr = ClientUtil.getImageIcon((String)tempList.get(2));
                    }
                    tempList.clear();
                    tempList = null;
                    if(imageIconFromSvr == null)
                        classImage = defaultFileIcon;
                    else
                        classImage = imageIconFromSvr;
                    index = 2;
                }
            imageIconFromSvr = null;
        }
        return index;
    }

    ImageIcon packageImage;
    ImageIcon defaultClassImage;
    Icon classImage;
    ImageIcon actionImage;
    ImageIcon modelImage;
    ImageIcon fieldImage;
    ImageIcon fieldGroupImage;
    ImageIcon favoriteImage;
    ImageIcon recentImage;
    ImageIcon inboxImage;
    ImageIcon sentImage;
    ImageIcon completedImage;
    ImageIcon defaultFolderIcon;
    ImageIcon defaultOpenFolderIcon;
    ImageIcon defaultFileFolderIcon;
    ImageIcon defaultFileIcon;
    private ClientUtil clientUtil;
}