// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-8 20:03:34
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ListenerBuilder.java

package dyna.framework.client;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DOS;
import dyna.framework.service.WKS;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.JTreeTable;
import dyna.uic.LinkTreeModel;
import dyna.uic.TreeNodeObject;
import dyna.util.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

// Referenced classes of package dyna.framework.client:
//            DynaMOAD, UIGeneration, UIBuilder, AuthoritySetting, 
//            SearchCondition4CADIntegration, SearchCondition, StructureCompareWindow

public class ListenerBuilder
{
    private static class publicActionListener
        implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            String selectOuid = null;
            java.util.List list = Utils.tokenizeMessage(command, '^');
            if(list.size() > 0)
                command = (String)list.get(0);
            if(list.size() > 1)
                selectOuid = (String)list.get(1);
            else
                selectOuid = "";
            if(command.equals("MNU_NEW_THIS"))
                try
                {
                    DOSChangeable getDC = DynaMOAD.dos.get(selectOuid);
                    String selectClassOuid = DynaMOAD.dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = null;
                    if(list.size() == 2)
                        uiGeneration = new UIGeneration(null, selectClassOuid, (String)list.get(1));
                    else
                    if(list.size() == 3)
                    {
                        String parentOuid = (String)list.get(2);
                        uiGeneration = new UIGeneration(null, selectClassOuid, parentOuid, (String)list.get(1), null);
                    }
                    uiGeneration = null;
                }
                catch(Exception ee)
                {
                    ee.printStackTrace();
                }
            else
            if(command.equals("makeWIPVersion"))
                try
                {
                    String newOuid = null;
                    Object returnObject = Utils.executeScriptFile(DynaMOAD.dos.getEventName(DynaMOAD.dos.getClassOuid(selectOuid), "wip.add.before"), DynaMOAD.dss, selectOuid);
                    if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                    {
                        newOuid = DynaMOAD.dos.makeWipObject(selectOuid);
                        returnObject = Utils.executeScriptFile(DynaMOAD.dos.getEventName(DynaMOAD.dos.getClassOuid(selectOuid), "wip.add.after"), DynaMOAD.dss, newOuid);
                        if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                            UIBuilder.displayInstanceWindow(newOuid, DynaMOAD.dos, DynaMOAD.dss);
                    }
                }
                catch(Exception ee)
                {
                    ee.printStackTrace();
                }
            else
            if(command.equals("SetAuthority"))
                try
                {
                    String selectClassOuid = DynaMOAD.dos.getClassOuid(selectOuid);
                    AuthoritySetting AuthSet = new AuthoritySetting(e.getSource(), DynaMOAD.getMSRString("WRD_0109", "Authority Setting", 3), selectClassOuid, selectOuid);
                    AuthSet.setVisible(true);
                }
                catch(Exception ee)
                {
                    ee.printStackTrace();
                }
            else
            if(command.equals("Favorite"))
            {
                int res = JOptionPane.showConfirmDialog(null, DynaMOAD.getMSRString("QST_0006", "Send to My folder.", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 3), 0);
                if(res != 0)
                    return;
                HashMap folderNode = new HashMap();
                DOSChangeable dos = null;
                boolean result = false;
                try
                {
                    dos = DynaMOAD.dos.get(selectOuid);
                    if(dos == null)
                        return;
                    folderNode.put("workspace.type", "PRIVATE");
                    folderNode.put("node.type", "OBJECT");
                    folderNode.put("parent", "/My Folder");
                    if(dos.get("vf$version") == null)
                        folderNode.put("name", "[" + dos.get("md$number") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                    else
                        folderNode.put("name", "[" + dos.get("md$number") + "/" + dos.get("vf$version") + "] " + (dos.get("md$description") != null ? dos.get("md$description") : ""));
                    folderNode.put("value", selectOuid);
                    result = DynaMOAD.wks.createNode(folderNode);
                    result = true;
                    folderNode.clear();
                    dos.clear();
                    dos = null;
                    if(result)
                    {
                        TreeModel treeModel = null;
                        if(DynaMOAD.searchCondition4CADIntegration != null)
                            treeModel = DynaMOAD.searchCondition4CADIntegration.workSpaceTree.getModel();
                        else
                        if(DynaMOAD.searchCondition != null)
                            treeModel = DynaMOAD.searchCondition.workSpaceTree.getModel();
                        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treeModel.getRoot();
                        DefaultMutableTreeNode childNode = null;
                        int count = rootNode.getChildCount();
                        for(int i = 0; i < count; i++)
                        {
                            childNode = (DefaultMutableTreeNode)rootNode.getChildAt(i);
                            if(!"My Folder".equals(childNode.toString()))
                                continue;
                            childNode.removeAllChildren();
                            if(DynaMOAD.searchCondition4CADIntegration != null)
                            {
                                DynaMOAD.searchCondition4CADIntegration.addFolders(childNode, "PRIVATE", "FOLDER", null);
                                DynaMOAD.searchCondition4CADIntegration.workSpaceTree.updateUI();
                            } else
                            if(DynaMOAD.searchCondition != null)
                            {
                                DynaMOAD.searchCondition.addFolders(childNode, "PRIVATE", "FOLDER", null);
                                DynaMOAD.searchCondition.workSpaceTree.updateUI();
                            }
                            break;
                        }

                    }
                }
                catch(Exception re)
                {
                    System.err.println(re);
                }
            } else
            if(command.equals("MNU_DELETE"))
                try
                {
                    int res = JOptionPane.showConfirmDialog(null, DynaMOAD.getMSRString("QST_0003", "Selected object will deleted. Are you sure?", 0), DynaMOAD.getMSRString("WRD_0019", "Confirmation", 3), 0);
                    if(res != 0)
                        return;
                    String selectedClassOuid = DynaMOAD.dos.getClassOuid(selectOuid);
                    Object returnObject = Utils.executeScriptFile(DynaMOAD.dos.getEventName(selectedClassOuid, "remove.before"), DynaMOAD.dss, selectOuid);
                    if(!(returnObject instanceof Boolean) || Utils.getBoolean((Boolean)returnObject))
                    {
                        DynaMOAD.dos.remove(selectOuid);
                        Utils.executeScriptFile(DynaMOAD.dos.getEventName(selectedClassOuid, "remove.after"), DynaMOAD.dss, selectOuid);
                    }
                }
                catch(IIPRequestException ie)
                {
                    System.err.println(ie);
                }
            else
            if(command.equals("MNU_COMPARE_EACH"))
            {
                if(list.size() < 3)
                    return;
                (new StructureCompareWindow(selectOuid, (String)list.get(2))).setVisible(true);
            } else
            if(command.startsWith("DATATYPE_OBJECT"))
            {
                if(Utils.isNullString(selectOuid))
                    return;
                try
                {
                    String selectClassOuid = DynaMOAD.dos.getClassOuid(selectOuid);
                    UIGeneration uiGeneration = new UIGeneration(null, selectClassOuid, selectOuid, 1);
                    uiGeneration.setVisible(true);
                    uiGeneration = null;
                }
                catch(IIPRequestException ie)
                {
                    ie.printStackTrace();
                }
            }
        }

        publicActionListener()
        {
        }
    }

    private class LinkListener
        implements TreeSelectionListener
    {

        public void valueChanged(TreeSelectionEvent tse)
        {
            if(Utils.isNullString(instanceOuid))
                return;
            try
            {
                ArrayList rows = null;
                TreePath path = tse.getPath();
                if(path == null)
                    return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                TreeNodeObject nodeData = (TreeNodeObject)node.getUserObject();
                if(nodeData.isPopulated())
                    return;
                if(searchConditionMap != null && !searchConditionMap.containsKey("version.condition.type"))
                    searchConditionMap.put("version.condition.type", "wip");
                ArrayList tmpColumnOuidList = ((LinkTreeModel)linkTreeTable.getTreeTableModel()).getColumnOuidList();
                if(processingMode == 0)
                {
                    System.out.println("LinkListener : PROCESSING_MODE == 0 : listLinkFrom " + (searchConditionMap == null));
                    if(searchConditionMap == null)
                        rows = DynaMOAD.dos.listLinkFrom((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList);
                    else
                        rows = DynaMOAD.dos.listLinkFrom((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList, searchConditionMap);
                } else
                if(processingMode == 1)
                {
                    System.out.println("LinkListener : PROCESSING_MODE == 1 : listLinkTo " + (searchConditionMap == null));
                    if(searchConditionMap == null)
                        rows = DynaMOAD.dos.listLinkTo((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList);
                    else
                        rows = DynaMOAD.dos.listLinkTo((String)Utils.tokenizeMessage(nodeData.getOuid(), '^').get(0), tmpColumnOuidList, searchConditionMap);
                }
                if(rows == null || rows.size() == 0)
                {
                    nodeData.setPopulated(true);
                    return;
                }
                ArrayList row = null;
                TreeNodeObject childNodeData = null;
                Iterator rowsKey;
                for(rowsKey = rows.iterator(); rowsKey.hasNext();)
                {
                    row = (ArrayList)rowsKey.next();
                    childNodeData = new TreeNodeObject((String)row.get(0) + "^" + DynaMOAD.dos.getClassOuid((String)row.get(0)) + "^" + row.get(row.size() - 1), (String)row.get(1), "Class");
                    childNodeData.setLawData(row);
                    node.add(new DefaultMutableTreeNode(childNodeData));
                    childNodeData = null;
                    row = null;
                }

                rowsKey = null;
                rows.clear();
                rows = null;
                nodeData.setPopulated(true);
                if(node.getLevel() == 0)
                    linkTreeTable.tree.fireTreeExpanded(path);
                else
                    linkTreeTable.tree.fireTreeCollapsed(path);
            }
            catch(IIPRequestException e)
            {
                e.printStackTrace();
            }
        }

        private String instanceOuid;
        private HashMap searchConditionMap;
        private int processingMode;
        private JTreeTable linkTreeTable;

        public LinkListener(String instanceOuid, HashMap searchConditionMap, int processMode, JTreeTable linkTreeTable)
        {
            this.instanceOuid = null;
            this.searchConditionMap = null;
            processingMode = 0;
            this.linkTreeTable = null;
            this.instanceOuid = instanceOuid;
            this.searchConditionMap = searchConditionMap;
            processingMode = processMode;
            this.linkTreeTable = linkTreeTable;
        }
    }


    public ListenerBuilder()
    {
    }

    public static ListenerBuilder getInstance()
    {
        if(listenerBuilder == null)
            listenerBuilder = new ListenerBuilder();
        return listenerBuilder;
    }

    public static ActionListener getPublicActionListner()
    {
        if(publicActionListener == null)
            publicActionListener = new publicActionListener();
        return publicActionListener;
    }

    public static TreeSelectionListener getObjectStructureTreeSelectoinListener(String instanceOuid, HashMap searchConditionMap, int processMode, JTreeTable linkTreeTable)
    {
        return getInstance(). new LinkListener(instanceOuid, searchConditionMap, processMode, linkTreeTable);
    }

    private static ListenerBuilder listenerBuilder = null;
    private static ActionListener publicActionListener = null;

}