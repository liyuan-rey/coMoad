/*
 * Created on 2004-11-3
 *
 */
package dyna.framework.client.dfempdm;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import dyna.framework.editor.modeler.ObjectModelDefaultTreeCellRenderer;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.util.Utils;


class CodeTree extends JTree {
    private DOS dos = dyna.framework.client.DynaMOAD.dos;
    
    private DOSChangeable dosCode = null;
    
    public CodeTree() {
        super();
        super.setModel(new DefaultTreeModel(null));
        
        this.addTreeSelectionListener(new TreeSelectionListener() {
            
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)getLastSelectedPathComponent();
                if(selnode != null)
                    populateHierarchyTreeNode(selnode, false);
            }
        });
        
        this.setCellRenderer(new ObjectModelDefaultTreeCellRenderer());
    }

    public void setModel(TreeModel newModel) {
    }
    
    public void setCode(DOSChangeable dosCode) {
        this.dosCode = dosCode;
        refresh();
    }
    
    public void refresh() {
        if(dosCode == null)
            return;
        
        try {
            String rootItemOuid = null;
            
            DOSChangeable rootCodeItem = dos.getCodeItemRoot((String)dosCode.get("ouid"));
            if(rootCodeItem == null)
            {
                rootCodeItem = new DOSChangeable();
                rootCodeItem.put("name", dosCode.get("name"));
                rootCodeItem.put("description", "Root Item of Tree. Do not delete me.");
                rootCodeItem.put("codeitemid", dosCode.get("name"));
                rootItemOuid = dos.createCodeItem((String)dosCode.get("ouid"), rootCodeItem);
                if(rootItemOuid == null)
                    return;
                
                rootCodeItem = dos.getCodeItem(rootItemOuid);
                if(rootCodeItem == null)
                    return;
            }
            
            DOSObjectAdapter dosRootObject = new DOSObjectAdapter(rootCodeItem, "%name%");
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(dosRootObject);
            
            DefaultTreeModel treeModel = (DefaultTreeModel)getModel();
            treeModel.setRoot(rootNode);
            
            this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            this.putClientProperty("JTree.lineStyle", "Angled");

            populateHierarchyTreeNode(rootNode, true);
            fireTreeExpanded(new TreePath(rootNode.getPath()));

            updateUI();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public DOSChangeable getSelectedCodeItem() {
        DOSChangeable retCodeItem = null;
        
        DefaultMutableTreeNode selnode = (DefaultMutableTreeNode)getLastSelectedPathComponent();
        if(selnode != null) {
            DOSObjectAdapter codedata = (DOSObjectAdapter)selnode.getUserObject();
            retCodeItem = codedata.getDosObject();
        }

        return retCodeItem;
    }
    
    private void populateHierarchyTreeNode(DefaultMutableTreeNode treeNode, boolean forceMode)
    {
        final String CustomPropertyKey = "isPopulated";
        
        if(treeNode == null)
            return;
        
        DOSObjectAdapter codedata = (DOSObjectAdapter)treeNode.getUserObject();

        if (!forceMode && codedata.getCustomProperty(CustomPropertyKey) == Boolean.TRUE)
            return;
        
        if(forceMode)
            treeNode.removeAllChildren();
        
        DefaultMutableTreeNode itemNode = null;
        DOSObjectAdapter itemNodeObject = null;
        DOSChangeable codeItem = null;
        
        try
        {
            ArrayList itemChildren = dos.getCodeItemChildren((String)codedata.get("ouid"));
            if(Utils.isNullArrayList(itemChildren))
                return;
            Iterator itemKey;
            for(itemKey = itemChildren.iterator(); itemKey.hasNext();)
            {
                codeItem = (DOSChangeable)itemKey.next();
                if(codeItem == null)
                    return;
                itemNodeObject = new DOSObjectAdapter(codeItem, "%name%");
                itemNode = new DefaultMutableTreeNode(itemNodeObject);
                treeNode.add(itemNode);
                itemNode = null;
                itemNodeObject = null;
                codeItem = null;
            }

            itemKey = null;
            itemChildren.clear();
            itemChildren = null;
            codedata.putCustomProperty(CustomPropertyKey, Boolean.TRUE);
        } catch(IIPRequestException e) {
            e.printStackTrace();
        }
    }
}