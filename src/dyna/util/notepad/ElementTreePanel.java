// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ElementTreePanel.java

package dyna.util.notepad;

import com.jgoodies.swing.util.UIFactory;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;

public class ElementTreePanel extends JPanel
    implements CaretListener, DocumentListener, PropertyChangeListener, TreeSelectionListener
{
    public static class ElementTreeModel extends DefaultTreeModel
    {

        public Object getChild(Object parent, int index)
        {
            if(parent == root)
                return rootElements[index];
            else
                return super.getChild(parent, index);
        }

        public int getChildCount(Object parent)
        {
            if(parent == root)
                return rootElements.length;
            else
                return super.getChildCount(parent);
        }

        public boolean isLeaf(Object node)
        {
            if(node == root)
                return false;
            else
                return super.isLeaf(node);
        }

        public int getIndexOfChild(Object parent, Object child)
        {
            if(parent == root)
            {
                for(int counter = rootElements.length - 1; counter >= 0; counter--)
                    if(rootElements[counter] == child)
                        return counter;

                return -1;
            } else
            {
                return super.getIndexOfChild(parent, child);
            }
        }

        public void nodeChanged(TreeNode node)
        {
            if(listenerList != null && node != null)
            {
                TreeNode parent = node.getParent();
                if(parent == null && node != root)
                    parent = root;
                if(parent != null)
                {
                    int anIndex = getIndexOfChild(parent, node);
                    if(anIndex != -1)
                    {
                        int cIndexs[] = new int[1];
                        cIndexs[0] = anIndex;
                        nodesChanged(parent, cIndexs);
                    }
                }
            }
        }

        protected TreeNode[] getPathToRoot(TreeNode aNode, int depth)
        {
            TreeNode retNodes[];
            if(aNode == null)
            {
                if(depth == 0)
                    return null;
                retNodes = new TreeNode[depth];
            } else
            {
                depth++;
                if(aNode == root)
                {
                    retNodes = new TreeNode[depth];
                } else
                {
                    TreeNode parent = aNode.getParent();
                    if(parent == null)
                        parent = root;
                    retNodes = getPathToRoot(parent, depth);
                }
                retNodes[retNodes.length - depth] = aNode;
            }
            return retNodes;
        }

        protected Element rootElements[];

        public ElementTreeModel(Document document)
        {
            super(new DefaultMutableTreeNode("root"), false);
            rootElements = document.getRootElements();
        }
    }


    public ElementTreePanel(JTextComponent editor)
    {
        this.editor = editor;
        Document document = editor.getDocument();
        treeModel = new ElementTreeModel(document);
        tree = new JTree(treeModel) {

            public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
            {
                if(!(value instanceof Element))
                    return value.toString();
                Element e = (Element)value;
                AttributeSet as = e.getAttributes().copyAttributes();
                String asString;
                if(as != null)
                {
                    StringBuffer retBuffer = new StringBuffer("[");
                    for(Enumeration names = as.getAttributeNames(); names.hasMoreElements();)
                    {
                        Object nextName = names.nextElement();
                        if(nextName != StyleConstants.ResolveAttribute)
                        {
                            retBuffer.append(" ");
                            retBuffer.append(nextName);
                            retBuffer.append("=");
                            retBuffer.append(as.getAttribute(nextName));
                        }
                    }

                    retBuffer.append(" ]");
                    asString = retBuffer.toString();
                } else
                {
                    asString = "[ ]";
                }
                if(e.isLeaf())
                    return e.getName() + " [" + e.getStartOffset() + ", " + e.getEndOffset() + "] Attributes: " + asString;
                else
                    return e.getName() + " [" + e.getStartOffset() + ", " + e.getEndOffset() + "] Attributes: " + asString;
            }

        };
        tree.addTreeSelectionListener(this);
        tree.setRootVisible(false);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {

            public Dimension getPreferredSize()
            {
                Dimension retValue = super.getPreferredSize();
                if(retValue != null)
                    retValue.width += 15;
                return retValue;
            }

        });
        document.addDocumentListener(this);
        editor.addPropertyChangeListener(this);
        editor.addCaretListener(this);
        setLayout(new BorderLayout());
        add(UIFactory.createStrippedScrollPane(tree), "Center");
        JLabel label = new JLabel("Elements that make up the current document", 0);
        label.setFont(new Font("Dialog", 1, 14));
        add(label, "North");
        setPreferredSize(new Dimension(400, 400));
    }

    public void setEditor(JTextComponent editor)
    {
        if(this.editor == editor)
            return;
        if(this.editor != null)
        {
            Document oldDoc = this.editor.getDocument();
            oldDoc.removeDocumentListener(this);
            this.editor.removePropertyChangeListener(this);
            this.editor.removeCaretListener(this);
        }
        this.editor = editor;
        if(editor == null)
        {
            treeModel = null;
            tree.setModel(null);
        } else
        {
            Document newDoc = editor.getDocument();
            newDoc.addDocumentListener(this);
            editor.addPropertyChangeListener(this);
            editor.addCaretListener(this);
            treeModel = new ElementTreeModel(newDoc);
            tree.setModel(treeModel);
        }
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        if(e.getSource() == getEditor() && e.getPropertyName().equals("document"))
        {
            Document oldDoc = (Document)e.getOldValue();
            Document newDoc = (Document)e.getNewValue();
            oldDoc.removeDocumentListener(this);
            newDoc.addDocumentListener(this);
            treeModel = new ElementTreeModel(newDoc);
            tree.setModel(treeModel);
        }
    }

    public void insertUpdate(DocumentEvent e)
    {
        updateTree(e);
    }

    public void removeUpdate(DocumentEvent e)
    {
        updateTree(e);
    }

    public void changedUpdate(DocumentEvent e)
    {
        updateTree(e);
    }

    public void caretUpdate(CaretEvent e)
    {
        if(updatingSelection)
            break MISSING_BLOCK_LABEL_236;
        int selBegin = Math.min(e.getDot(), e.getMark());
        int end = Math.max(e.getDot(), e.getMark());
        Vector paths = new Vector();
        TreeModel model = getTreeModel();
        Object root = model.getRoot();
        int rootCount = model.getChildCount(root);
        for(int counter = 0; counter < rootCount; counter++)
        {
            for(int start = selBegin; start <= end;)
            {
                TreePath path = getPathForIndex(start, root, (Element)model.getChild(root, counter));
                Element charElement = (Element)path.getLastPathComponent();
                paths.addElement(path);
                if(start >= charElement.getEndOffset())
                    start++;
                else
                    start = charElement.getEndOffset();
            }

        }

        int numPaths = paths.size();
        if(numPaths <= 0)
            break MISSING_BLOCK_LABEL_236;
        TreePath pathArray[] = new TreePath[numPaths];
        paths.copyInto(pathArray);
        updatingSelection = true;
        try
        {
            getTree().setSelectionPaths(pathArray);
            getTree().scrollPathToVisible(pathArray[0]);
        }
        finally
        {
            updatingSelection = false;
        }
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        JTree tree = getTree();
        if(updatingSelection || tree.getSelectionCount() != 1)
            break MISSING_BLOCK_LABEL_95;
        TreePath selPath = tree.getSelectionPath();
        Object lastPathComponent = selPath.getLastPathComponent();
        if(lastPathComponent instanceof DefaultMutableTreeNode)
            break MISSING_BLOCK_LABEL_95;
        Element selElement = (Element)lastPathComponent;
        updatingSelection = true;
        try
        {
            getEditor().select(selElement.getStartOffset(), selElement.getEndOffset());
        }
        finally
        {
            updatingSelection = false;
        }
    }

    protected JTree getTree()
    {
        return tree;
    }

    protected JTextComponent getEditor()
    {
        return editor;
    }

    public DefaultTreeModel getTreeModel()
    {
        return treeModel;
    }

    protected void updateTree(DocumentEvent event)
    {
        updatingSelection = true;
        try
        {
            TreeModel model = getTreeModel();
            Object root = model.getRoot();
            for(int counter = model.getChildCount(root) - 1; counter >= 0; counter--)
                updateTree(event, (Element)model.getChild(root, counter));

        }
        finally
        {
            updatingSelection = false;
        }
        return;
    }

    protected void updateTree(DocumentEvent event, Element element)
    {
        javax.swing.event.DocumentEvent.ElementChange ec = event.getChange(element);
        if(ec != null)
        {
            Element removed[] = ec.getChildrenRemoved();
            Element added[] = ec.getChildrenAdded();
            int startIndex = ec.getIndex();
            if(removed != null && removed.length > 0)
            {
                int indices[] = new int[removed.length];
                for(int counter = 0; counter < removed.length; counter++)
                    indices[counter] = startIndex + counter;

                getTreeModel().nodesWereRemoved((TreeNode)element, indices, removed);
            }
            if(added != null && added.length > 0)
            {
                int indices[] = new int[added.length];
                for(int counter = 0; counter < added.length; counter++)
                    indices[counter] = startIndex + counter;

                getTreeModel().nodesWereInserted((TreeNode)element, indices);
            }
        }
        if(!element.isLeaf())
        {
            int startIndex = element.getElementIndex(event.getOffset());
            int elementCount = element.getElementCount();
            int endIndex = Math.min(elementCount - 1, element.getElementIndex(event.getOffset() + event.getLength()));
            if(startIndex > 0 && startIndex < elementCount && element.getElement(startIndex).getStartOffset() == event.getOffset())
                startIndex--;
            if(startIndex != -1 && endIndex != -1)
            {
                for(int counter = startIndex; counter <= endIndex; counter++)
                    updateTree(event, element.getElement(counter));

            }
        } else
        {
            getTreeModel().nodeChanged((TreeNode)element);
        }
    }

    protected TreePath getPathForIndex(int position, Object root, Element rootElement)
    {
        TreePath path = new TreePath(root);
        Element child = rootElement.getElement(rootElement.getElementIndex(position));
        path = path.pathByAddingChild(rootElement);
        for(path = path.pathByAddingChild(child); !child.isLeaf(); path = path.pathByAddingChild(child))
            child = child.getElement(child.getElementIndex(position));

        return path;
    }

    protected JTree tree;
    protected JTextComponent editor;
    protected ElementTreeModel treeModel;
    protected boolean updatingSelection;
}
