// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UIBuilder.java

package dyna.framework.client;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.swing.ExtToolBar;
import com.jgoodies.swing.util.*;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.*;
import dyna.framework.service.dos.DOSChangeable;
import dyna.uic.*;
import dyna.util.Utils;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.PrintStream;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;

// Referenced classes of package dyna.framework.client:
//            ClientUtil, DynaMOAD, ListenerBuilder, LogIn, 
//            UIGeneration, SearchResult, SearchResult4CADIntegration, SearchCondition4CADIntegration

public class UIBuilder
{
    private static class HeaderRenderer extends DefaultTableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(size != null)
            {
                setMinimumSize(size);
                setPreferredSize(size);
            }
            setOpaque(false);
            setHorizontalAlignment(0);
            return this;
        }

        public void paint(Graphics g)
        {
            rect = getBounds();
            Rectangle r = rect;
            r.x = 1;
            r.y = 1;
            r.width = r.width - 2;
            r.height = r.height - 1;
            Graphics2D g2 = (Graphics2D)g;
            int width = r.width;
            int height = (int)((float)r.height * 0.5F);
            java.awt.Paint storedPaint = g2.getPaint();
            g2.setPaint(new GradientPaint(r.x, r.y, DynaTheme.BRIGHT_FINISH, r.x, height, DynaTheme.BRIGHT_BEGIN));
            g2.fillRect(r.x, r.y, width, height);
            g2.setPaint(new GradientPaint(r.x, r.y + height, DynaTheme.DARK_BEGIN, r.x, r.y + height + height, DynaTheme.DARK_FINISH));
            g2.fillRect(r.x, r.y + height, width, height);
            g2.setPaint(storedPaint);
            r.width = r.width + 2;
            r.height = r.height + 1;
            g2.setColor(PlasticLookAndFeel.getControlDarkShadow());
            g2.drawLine(1, 0, r.width - 2, 0);
            g2.drawLine(1, r.height - 1, r.width - 2, r.height - 1);
            g2.drawLine(0, 1, 0, r.height - 2);
            g2.drawLine(r.width - 1, 1, r.width - 1, r.height - 2);
            g2 = null;
            super.paint(g);
        }

        private Rectangle rect;
        private Dimension size;

        public HeaderRenderer(int height)
        {
            rect = null;
            size = null;
            setBackground(DynaTheme.tableheaderColor);
            size = getSize();
            size.height = height;
        }

        public HeaderRenderer()
        {
            rect = null;
            size = null;
            setBackground(DynaTheme.tableheaderColor);
        }
    }


    public UIBuilder()
    {
    }

    public static JPanel createFieldGroupPanel(DOS dos, DOSChangeable hoardData, DOSChangeable fieldGroupDC, ArrayList actionButtonList, DOSChangeable selectButtonData, ArrayList selectButtonList, DOSChangeable dateChooserButtonData, ArrayList dateChooserButtonList, 
            HashMap collectionMap, HashMap fieldAndFieldGroupMap, ActionListener actionListener, MouseListener mouseListener)
        throws IIPRequestException
    {
        JPanel mainPanel = null;
        JScrollPane mainScrollPane = null;
        JPanel fieldPanel = null;
        JToolBar actionToolBar = null;
        int maxwidth = 0;
        int maxheight = 0;
        int columnCount = 0;
        int columnGap = 2;
        Dimension fieldPanelBounds = null;
        Boolean isVisible = null;
        boolean isReadOnly = false;
        DOSChangeable tempInstance = new DOSChangeable();
        mainPanel = new JPanel(new BorderLayout());
        fieldPanel = new JPanel(null);
        fieldPanel.setBackground(DynaTheme.panelBackgroundColor);
        mainScrollPane = UIFactory.createStrippedScrollPane(fieldPanel);
        mainPanel.add(mainScrollPane, "Center");
        String fieldGroupName = (String)fieldGroupDC.get("name");
        ArrayList actionList = (ArrayList)fieldGroupDC.get("array$ouid@action");
        ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
        ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
        if(inheritedFields != null)
        {
            if(fieldList == null)
                fieldList = new ArrayList();
            ArrayList tmpFieldList = new ArrayList();
            DOSChangeable tmpData = null;
            for(int j = 0; j < inheritedFields.size(); j++)
            {
                tmpData = (DOSChangeable)inheritedFields.get(j);
                if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                    tmpFieldList.add((String)tmpData.get("ouid"));
            }

            if(tmpFieldList.size() > 0)
                fieldList.addAll(0, tmpFieldList);
        }
        if(actionList == null)
        {
            actionToolBar = null;
        } else
        {
            int actionCnt = 0;
            JButton actionButton = null;
            DOSChangeable actionUIData = null;
            actionToolBar = new ExtToolBar("actionToolBar", HeaderStyle.BOTH);
            actionToolBar.putClientProperty("Plastic.borderStyle", BorderStyle.SEPARATOR);
            ImageIcon imageIconFromSvr = null;
            for(int j = 0; j < actionList.size(); j++)
            {
                actionCnt++;
                ArrayList tmpList = new ArrayList();
                actionUIData = dos.getAction((String)actionList.get(j));
                isVisible = (Boolean)actionUIData.get("is.visible");
                if(isVisible == null || Utils.getBoolean(isVisible))
                {
                    actionButton = null;
                    if(!Utils.isNullString((String)actionUIData.get("icon")))
                    {
                        imageIconFromSvr = ClientUtil.getImageIcon((String)actionUIData.get("ouid"));
                        actionButton = new ToolBarButton(imageIconFromSvr);
                        imageIconFromSvr = null;
                    } else
                    if(!Utils.isNullString((String)actionUIData.get("icon24")))
                        actionButton = new ToolBarButton(new ImageIcon(java.lang.Object.class.getResource("/icons/" + (String)actionUIData.get("icon24"))));
                    else
                    if(!Utils.isNullString((String)actionUIData.get("icon32")))
                        actionButton = new ToolBarButton(new ImageIcon(java.lang.Object.class.getResource("/icons/" + (String)actionUIData.get("icon32"))));
                    else
                    if(!Utils.isNullString((String)actionUIData.get("title")))
                    {
                        actionButton = new JButton();
                        actionButton.setText((String)actionUIData.get("title"));
                    }
                    actionButton.setToolTipText((String)actionUIData.get("title"));
                    actionButton.setActionCommand("ActionButtonClick_" + actionCnt + "_" + (String)actionUIData.get("title") + "^" + (String)actionUIData.get("ouid"));
                    actionButton.addActionListener(actionListener);
                    tmpList.add((String)actionUIData.get("ouid"));
                    tmpList.add(actionButton.getActionCommand());
                    actionButtonList.add(tmpList.clone());
                    actionToolBar.add(actionButton);
                    actionButton = null;
                    tmpList.clear();
                    tmpList = null;
                }
            }

            mainPanel.add(actionToolBar, "North");
        }
        if(fieldList == null)
        {
            fieldPanel = null;
            mainScrollPane = null;
        } else
        {
            DOSChangeable fieldUIData = null;
            ArrayList fieldUIList = new ArrayList();
            for(int j = 0; j < fieldList.size(); j++)
            {
                fieldUIData = dos.getField((String)fieldList.get(j));
                if(fieldUIData != null)
                {
                    fieldUIList.add(fieldUIData);
                    fieldUIData = null;
                }
            }

            Utils.sort(fieldUIList);
            int accumulatedOffset = 0;
            int currentX = 10;
            int currentY = 0;
            int titleWidth = 0;
            int width = 0;
            int oldWidth = 0;
            int accumulatedWidth = 0;
            int height = 0;
            int column = 0;
            int oldColumn = 0;
            int index = -9999;
            int oldIndex = -9999;
            int selectCnt = 0;
            int dateChooserCnt = 0;
            int codeVisualType = 0;
            byte changeable = 0;
            String initValue = null;
            boolean isCollection = false;
            String fieldName = null;
            String fieldTitle = null;
            Byte fieldType = null;
            String trueOrFalse[] = {
                "", "True", "False"
            };
            javax.swing.text.StyledDocument styledDocument = null;
            DefaultComboBoxModel comboBoxModel = null;
            ArrayList tableData = null;
            JTextField fieldTextField = null;
            JComboBox fieldComboBox = null;
            for(int j = 0; j < fieldUIList.size(); j++)
            {
                fieldUIData = (DOSChangeable)fieldUIList.get(j);
                isVisible = (Boolean)fieldUIData.get("is.visible");
                if(isVisible == null || Utils.getBoolean(isVisible))
                {
                    oldIndex = index;
                    oldColumn = column;
                    oldWidth = width;
                    titleWidth = Utils.getInt((Integer)fieldUIData.get("title.width"));
                    width = Utils.getInt((Integer)fieldUIData.get("width"));
                    height = Utils.getInt((Integer)fieldUIData.get("height"));
                    index = Utils.getInt((Integer)fieldUIData.get("index"));
                    column = Utils.getInt((Integer)fieldUIData.get("column"));
                    changeable = Utils.getByte((Byte)fieldUIData.get("changeable"));
                    initValue = (String)fieldUIData.get("initial.value");
                    fieldName = (String)fieldUIData.get("name");
                    fieldTitle = (String)fieldUIData.get("title");
                    fieldType = (Byte)fieldUIData.get("type");
                    if(titleWidth < 1)
                        titleWidth = 80;
                    if(width < 1)
                        width = 250;
                    if(height < 1)
                        height = 22;
                    if(Utils.getInt((Integer)fieldUIData.get("multiplicity.to")) > 1)
                        isCollection = true;
                    else
                        isCollection = false;
                    if(index == 0 || index == 1)
                        index = oldIndex - 1;
                    if(oldIndex != index)
                        currentY = 10 + accumulatedOffset;
                    if(column != 0 && oldColumn != column)
                    {
                        accumulatedWidth = accumulatedWidth + oldWidth + columnGap;
                        currentX = accumulatedWidth + 10;
                        columnCount++;
                    } else
                    {
                        accumulatedWidth = 0;
                        currentX = 10;
                    }
                    if(Utils.getBoolean((Boolean)fieldUIData.get("is.title.visible")))
                    {
                        JLabel fieldLabel = new JLabel();
                        fieldLabel.setText(DynaMOAD.getMSRString((String)fieldUIData.get("code"), fieldTitle, 0));
                        fieldLabel.setBounds(currentX, currentY, titleWidth, height <= 22 ? height : 22);
                        if(Utils.getBoolean((Boolean)fieldUIData.get("is.mandatory")))
                            fieldLabel.setForeground(Color.blue);
                        fieldPanel.add(fieldLabel);
                    }
                    if(width > maxwidth)
                        maxwidth = width;
                    maxwidth = Math.max(maxwidth, (currentX + width) - 10);
                    if(oldIndex != index)
                        maxheight = currentY + height;
                    styledDocument = new MStyledDocument(Utils.getDouble((Double)fieldUIData.get("size")), fieldType.byteValue());
                    fieldTextField = new JTextField(styledDocument, null, 0);
                    fieldTextField.setBounds(currentX + titleWidth, currentY, width - titleWidth, height);
                    isReadOnly = false;
                    if(!Utils.isNullString(initValue) && changeable == 3)
                        isReadOnly = true;
                    if(changeable == 4)
                        isReadOnly = true;
                    if(isReadOnly)
                    {
                        fieldTextField.setBackground(DynaTheme.panelBackgroundColor);
                        fieldTextField.setEditable(false);
                    }
                    JComponent comp = null;
                    JComponent comp2 = null;
                    String tmpFieldGroupName = null;
                    if(hoardData.getValueMap().containsValue(fieldName))
                    {
                        tmpFieldGroupName = (String)((ArrayList)fieldAndFieldGroupMap.get(fieldName)).get(0);
                        comp = (JComponent)hoardData.get(fieldName + tmpFieldGroupName);
                        if(comp instanceof JTextField)
                        {
                            styledDocument = (DefaultStyledDocument)((JTextField)comp).getDocument();
                            fieldTextField.setDocument(styledDocument);
                        } else
                        if(comp instanceof JComboBox)
                            comboBoxModel = (DefaultComboBoxModel)((JComboBox)comp).getModel();
                        else
                        if(comp instanceof JScrollPane)
                        {
                            comp2 = (JComponent)((JScrollPane)comp).getViewport().getView();
                            if(comp2 instanceof JTextArea)
                                styledDocument = (DefaultStyledDocument)((JTextArea)comp2).getDocument();
                            else
                            if(comp2 instanceof DynaTable)
                                tableData = ((Table)collectionMap.get(comp2.hashCode() + "Table")).getList();
                        }
                    }
                    if(isCollection)
                    {
                        fieldTextField = null;
                        Table innerTable = null;
                        if(tableData == null)
                            innerTable = makeCollectionTable(fieldUIData, collectionMap, mouseListener);
                        else
                            innerTable = makeCollectionTable(fieldUIData, collectionMap, mouseListener, tableData);
                        JScrollPane scrollPane = new JScrollPane();
                        scrollPane.setViewportView(innerTable.getTable());
                        scrollPane.setCursor(handCursor);
                        scrollPane.addMouseListener(mouseListener);
                        scrollPane.setBounds(currentX + titleWidth, currentY, width - titleWidth, height);
                        hoardData.put(fieldName + fieldGroupName, scrollPane);
                        hoardData.put(fieldName + fieldGroupName + "innerTable", innerTable);
                        fieldPanel.add(scrollPane);
                        innerTable = null;
                        scrollPane = null;
                    } else
                    if(fieldType.byteValue() == 16 && !isReadOnly)
                    {
                        fieldTextField.setBackground(DynaTheme.panelBackgroundColor);
                        fieldTextField.setEditable(false);
                        fieldTextField.setActionCommand("DATATYPE_OBJECT^");
                        fieldTextField.addActionListener(ListenerBuilder.getPublicActionListner());
                        fieldTextField.addMouseListener(mouseListener);
                        if(!Utils.isNullString(initValue))
                        {
                            DOSChangeable objectData = dos.get(initValue);
                            fieldTextField.setText((String)objectData.get("md$number") + " " + (String)objectData.get("md$description"));
                            hoardData.put("initvalue@@" + fieldName, initValue);
                        }
                        fieldPanel.add(fieldTextField);
                        hoardData.put(fieldName + fieldGroupName, fieldTextField);
                    } else
                    if(fieldType.byteValue() == 1)
                    {
                        fieldTextField = null;
                        if(comboBoxModel == null)
                            comboBoxModel = new DefaultComboBoxModel(trueOrFalse);
                        fieldComboBox = new JComboBox(comboBoxModel);
                        fieldComboBox.setSelectedIndex(-1);
                        fieldComboBox.setBounds(currentX + titleWidth, currentY, width - titleWidth, height);
                        fieldComboBox.setEditable(false);
                        if(!Utils.isNullString(initValue))
                            if(initValue.equals("true"))
                                fieldComboBox.setSelectedItem("True");
                            else
                                fieldComboBox.setSelectedItem("False");
                        fieldPanel.add(fieldComboBox);
                        hoardData.put(fieldName + fieldGroupName, fieldComboBox);
                        fieldComboBox = null;
                    } else
                    if(fieldType.byteValue() == 24 || fieldType.byteValue() == 25)
                    {
                        fieldTextField.setBackground(DynaTheme.panelBackgroundColor);
                        fieldTextField.setEditable(false);
                        String codeOuid = null;
                        if(fieldType.byteValue() == 25)
                            fieldUIData.put("type.ouid@class", null);
                        if(fieldUIData.get("type.ouid@class") != null)
                        {
                            if(fieldType.byteValue() == 24)
                            {
                                codeOuid = (String)fieldUIData.get("type.ouid@class");
                                if(!Utils.isNullString(codeOuid))
                                {
                                    DOSChangeable dosCode = dos.getCode(codeOuid);
                                    if(dosCode != null)
                                    {
                                        codeVisualType = Utils.getInt((Integer)dosCode.get("visual.type"));
                                        switch(codeVisualType)
                                        {
                                        default:
                                            break;

                                        case 1: // '\001'
                                            if(!Utils.isNullString(initValue))
                                            {
                                                DOSChangeable codeData = dos.getCodeItem(initValue);
                                                fieldTextField.setText(codeData.get("name") + " [" + codeData.get("codeitemid") + "]");
                                                hoardData.put("initvalue@@" + fieldName, initValue);
                                                tempInstance.put(fieldName, initValue);
                                            }
                                            fieldPanel.add(fieldTextField);
                                            hoardData.put(fieldName + fieldGroupName, fieldTextField);
                                            break;

                                        case 2: // '\002'
                                            fieldTextField = null;
                                            fieldComboBox = new JComboBox();
                                            fieldComboBox.setBounds(currentX + titleWidth, currentY, width - titleWidth, height);
                                            fieldComboBox.setEditable(false);
                                            fieldComboBox.setActionCommand("fieldComboBox^" + (String)fieldUIData.get("ouid"));
                                            fieldComboBox.addActionListener(actionListener);
                                            if(comboBoxModel == null)
                                                setComboBoxModelFromCode(fieldComboBox, dosCode, dos, fieldUIData);
                                            else
                                                fieldComboBox.setModel(comboBoxModel);
                                            if(!Utils.isNullString(initValue))
                                            {
                                                DOSChangeable codeData = dos.getCodeItem(initValue);
                                                comboBoxModel = (DefaultComboBoxModel)fieldComboBox.getModel();
                                                MComboBoxModelObject mcbmo = null;
                                                for(int m = 0; m < comboBoxModel.getSize(); m++)
                                                {
                                                    mcbmo = (MComboBoxModelObject)comboBoxModel.getElementAt(m);
                                                    if(!codeData.get("ouid").equals(mcbmo.getOuid()))
                                                        continue;
                                                    fieldComboBox.setSelectedIndex(m);
                                                    tempInstance.put(fieldName, initValue);
                                                    break;
                                                }

                                            }
                                            fieldPanel.add(fieldComboBox);
                                            hoardData.put(fieldName + fieldGroupName, fieldComboBox);
                                            fieldComboBox = null;
                                            break;
                                        }
                                        dosCode = null;
                                    } else
                                    {
                                        codeVisualType = 0;
                                        fieldPanel.add(fieldTextField);
                                        hoardData.put(fieldName + fieldGroupName, fieldTextField);
                                    }
                                } else
                                {
                                    codeVisualType = 1;
                                    fieldPanel.add(fieldTextField);
                                    hoardData.put(fieldName + fieldGroupName, fieldTextField);
                                }
                            }
                        } else
                        if(fieldType.byteValue() == 25)
                        {
                            if(dos.isComboBoxMatrixCode((String)fieldUIData.get("ouid")))
                            {
                                codeVisualType = 2;
                                fieldTextField = null;
                                fieldComboBox = new JComboBox();
                                fieldComboBox.setBounds(currentX + titleWidth, currentY, width - titleWidth, height);
                                fieldComboBox.setEditable(false);
                                fieldComboBox.addActionListener(actionListener);
                                fieldPanel.add(fieldComboBox);
                                hoardData.put(fieldName + fieldGroupName, fieldComboBox);
                                fieldComboBox = null;
                            } else
                            {
                                codeVisualType = 1;
                                fieldPanel.add(fieldTextField);
                                hoardData.put(fieldName + fieldGroupName, fieldTextField);
                            }
                        } else
                        {
                            codeVisualType = 0;
                            fieldPanel.add(fieldTextField);
                            hoardData.put(fieldName + fieldGroupName, fieldTextField);
                        }
                    } else
                    if(fieldType.byteValue() == 13 && height >= 44)
                    {
                        fieldTextField = null;
                        if(styledDocument == null)
                            styledDocument = new MStyledDocument(Utils.getDouble((Double)fieldUIData.get("size")), fieldType.byteValue());
                        JTextArea fieldTextArea = new JTextArea(styledDocument);
                        if(!Utils.isNullString(initValue))
                            fieldTextArea.setText(initValue);
                        JScrollPane fieldScrollPaneTextArea = new JScrollPane();
                        fieldScrollPaneTextArea.setViewportView(fieldTextArea);
                        fieldScrollPaneTextArea.setBounds(currentX + titleWidth, currentY, width - titleWidth, height);
                        fieldPanel.add(fieldScrollPaneTextArea);
                        hoardData.put(fieldName + fieldGroupName, fieldScrollPaneTextArea);
                        fieldTextArea = null;
                        fieldScrollPaneTextArea = null;
                    } else
                    if(fieldType.byteValue() == 21)
                    {
                        if(!Utils.isNullString(initValue))
                            fieldTextField.setText(initValue.substring(0, 4) + "-" + initValue.substring(4, 6) + "-" + initValue.substring(6, 8) + " " + initValue.substring(8, 10) + ":" + initValue.substring(10, 12) + ":" + initValue.substring(12, 14));
                        fieldPanel.add(fieldTextField);
                        hoardData.put(fieldName + fieldGroupName, fieldTextField);
                    } else
                    if(fieldType.byteValue() == 22)
                    {
                        if(!Utils.isNullString(initValue))
                            fieldTextField.setText(initValue.substring(0, 4) + "-" + initValue.substring(4, 6) + "-" + initValue.substring(6, 8));
                        fieldPanel.add(fieldTextField);
                        hoardData.put(fieldName + fieldGroupName, fieldTextField);
                    } else
                    if(fieldType.byteValue() == 23)
                    {
                        if(!Utils.isNullString(initValue))
                            fieldTextField.setText(initValue.substring(0, 2) + ":" + initValue.substring(2, 4) + ":" + initValue.substring(4, 6));
                        fieldPanel.add(fieldTextField);
                        hoardData.put(fieldName + fieldGroupName, fieldTextField);
                    } else
                    {
                        if(!Utils.isNullString(initValue))
                            fieldTextField.setText(initValue);
                        fieldPanel.add(fieldTextField);
                        hoardData.put(fieldName + fieldGroupName, fieldTextField);
                    }
                    if(!isCollection)
                        if(fieldType.byteValue() == 16 && !isReadOnly)
                        {
                            selectCnt++;
                            JButton selectButtonByType = new JButton();
                            selectButtonByType.setActionCommand("ObjectSelectButtonClick_" + selectCnt + "_" + fieldTitle + "^" + (String)fieldUIData.get("type.ouid@class") + "^" + fieldName + fieldGroupName);
                            selectButtonByType.setBounds((currentX + width) - 18, currentY, 18, height <= 22 ? height : 22);
                            selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
                            selectButtonByType.addActionListener(actionListener);
                            selectButtonData.put(selectButtonByType.getActionCommand(), fieldTextField);
                            selectButtonList.add(selectButtonByType.getActionCommand());
                            fieldPanel.add(selectButtonByType);
                            fieldTextField.setBounds(currentX + titleWidth, currentY, width - titleWidth - 17, height);
                        } else
                        if(fieldType.byteValue() == 24 && !isReadOnly && codeVisualType == 1)
                        {
                            selectCnt++;
                            JButton selectButtonByType = new JButton();
                            selectButtonByType.setActionCommand("CodeSelectButtonClick_" + selectCnt + "_" + fieldTitle + "^" + (String)fieldUIData.get("type.ouid@class") + "^" + fieldName + fieldGroupName);
                            selectButtonByType.setBounds((currentX + width) - 18, currentY, 18, height <= 22 ? height : 22);
                            selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
                            selectButtonByType.addActionListener(actionListener);
                            selectButtonData.put(selectButtonByType.getActionCommand(), fieldTextField);
                            selectButtonList.add(selectButtonByType.getActionCommand());
                            fieldPanel.add(selectButtonByType);
                            fieldTextField.setBounds(currentX + titleWidth, currentY, width - titleWidth - 17, height);
                        } else
                        if(fieldType.byteValue() == 25 && !isReadOnly && codeVisualType == 1)
                        {
                            selectCnt++;
                            JButton selectButtonByType = new JButton();
                            selectButtonByType.setActionCommand("CodeReferenceSelectButtonClick_" + selectCnt + "_" + fieldName + "^" + fieldUIData.get("ouid") + "^" + fieldName + fieldGroupName);
                            selectButtonByType.setBounds((currentX + width) - 18, currentY, 18, height <= 22 ? height : 22);
                            selectButtonByType.setIcon(new dyna.uic.MUtils.ComboBoxButtonIcon());
                            selectButtonByType.addActionListener(actionListener);
                            selectButtonData.put(selectButtonByType.getActionCommand(), fieldTextField);
                            selectButtonList.add(selectButtonByType.getActionCommand());
                            fieldPanel.add(selectButtonByType);
                            fieldTextField.setBounds(currentX + titleWidth, currentY, width - titleWidth - 17, height);
                        } else
                        if((fieldType.byteValue() == 22 || fieldType.byteValue() == 21) && !isReadOnly)
                        {
                            dateChooserCnt++;
                            JButton dateChooserButton = new JButton();
                            dateChooserButton.setActionCommand("DateChooserButtonClick_" + dateChooserCnt + "_" + fieldTitle + "^" + (String)fieldUIData.get("type.ouid@class"));
                            dateChooserButton.setBounds((currentX + width) - (height <= 22 ? height : 22), currentY, height <= 22 ? height : 22, height <= 22 ? height : 22);
                            dateChooserButton.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/DateSelectButton.gif")));
                            dateChooserButton.addActionListener(actionListener);
                            dateChooserButtonData.put(dateChooserButton.getActionCommand(), fieldTextField);
                            dateChooserButtonList.add(dateChooserButton.getActionCommand());
                            fieldPanel.add(dateChooserButton);
                            fieldTextField.setBounds(currentX + titleWidth, currentY, width - titleWidth - (height <= 22 ? height : 22) - 1, height);
                        }
                    if(oldIndex != index)
                        accumulatedOffset = accumulatedOffset + height + 4;
                    hoardData.put("type@@" + fieldName, fieldType);
                    hoardData.put("name@@" + fieldName + fieldGroupName, fieldName);
                    ArrayList tmpList = (ArrayList)fieldAndFieldGroupMap.get(fieldName);
                    if(tmpList == null)
                    {
                        tmpList = new ArrayList();
                        fieldAndFieldGroupMap.put(fieldName, tmpList);
                    }
                    tmpList.add(fieldGroupName);
                    tmpList = null;
                    styledDocument = null;
                    fieldTextField = null;
                    comboBoxModel = null;
                    fieldComboBox = null;
                    tableData = null;
                }
            }

        }
        fieldPanelBounds = new Dimension(maxwidth + 15 + columnCount * columnGap, maxheight + 10);
        fieldPanel.setMaximumSize(fieldPanelBounds);
        fieldPanel.setMinimumSize(fieldPanelBounds);
        fieldPanel.setPreferredSize(fieldPanelBounds);
        mainPanel.setMaximumSize(fieldPanelBounds);
        mainPanel.setPreferredSize(fieldPanelBounds);
        fieldPanelBounds = null;
        return mainPanel;
    }

    public static void refreshFieldGroupPanel(DOS dos, DOSChangeable hoardData, DOSChangeable fieldGroupDC, HashMap fieldAndFieldGroupMap, DOSChangeable instanceObject)
        throws IIPRequestException
    {
        if(hoardData == null)
            return;
        Boolean isVisible = null;
        DOSChangeable tempInstance = new DOSChangeable();
        String fieldGroupName = (String)fieldGroupDC.get("name");
        ArrayList actionList = (ArrayList)fieldGroupDC.get("array$ouid@action");
        ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
        ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
        if(inheritedFields != null)
        {
            if(fieldList == null)
                fieldList = new ArrayList();
            ArrayList tmpFieldList = new ArrayList();
            DOSChangeable tmpData = null;
            for(int j = 0; j < inheritedFields.size(); j++)
            {
                tmpData = (DOSChangeable)inheritedFields.get(j);
                if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                    tmpFieldList.add((String)tmpData.get("ouid"));
            }

            if(tmpFieldList.size() > 0)
                fieldList.addAll(0, tmpFieldList);
        }
        if(fieldList != null)
        {
            DOSChangeable fieldUIData = null;
            ArrayList fieldUIList = new ArrayList();
            for(int j = 0; j < fieldList.size(); j++)
            {
                fieldUIData = dos.getField((String)fieldList.get(j));
                if(fieldUIData != null)
                {
                    fieldUIList.add(fieldUIData);
                    fieldUIData = null;
                }
            }

            Utils.sort(fieldUIList);
            int codeVisualType = 0;
            byte changeable = 0;
            String initValue = null;
            boolean isCollection = false;
            String fieldName = null;
            String fieldTitle = null;
            Byte fieldType = null;
            DefaultComboBoxModel comboBoxModel = null;
            JTextField fieldTextField = null;
            JComboBox fieldComboBox = null;
            for(int j = 0; j < fieldUIList.size(); j++)
            {
                fieldUIData = (DOSChangeable)fieldUIList.get(j);
                isVisible = (Boolean)fieldUIData.get("is.visible");
                if(isVisible != null && !Utils.getBoolean(isVisible))
                    continue;
                initValue = (String)fieldUIData.get("initial.value");
                fieldName = (String)fieldUIData.get("name");
                fieldTitle = (String)fieldUIData.get("title");
                fieldType = (Byte)fieldUIData.get("type");
                if(Utils.getInt((Integer)fieldUIData.get("multiplicity.to")) > 1)
                    isCollection = true;
                else
                    isCollection = false;
                JComponent comp = null;
                JComponent comp2 = null;
                String tmpFieldGroupName = null;
                if(hoardData.getValueMap().containsValue(fieldName))
                {
                    tmpFieldGroupName = (String)((ArrayList)fieldAndFieldGroupMap.get(fieldName)).get(0);
                    comp = (JComponent)hoardData.get(fieldName + tmpFieldGroupName);
                    if(comp instanceof JComboBox)
                        comboBoxModel = (DefaultComboBoxModel)((JComboBox)comp).getModel();
                }
                if(!isCollection && (fieldType.byteValue() == 24 || fieldType.byteValue() == 25))
                {
                    String codeOuid = null;
                    if(fieldType.byteValue() == 25)
                        fieldUIData.put("type.ouid@class", null);
                    if(fieldUIData.get("type.ouid@class") != null)
                    {
                        if(fieldType.byteValue() == 24)
                        {
                            codeOuid = (String)fieldUIData.get("type.ouid@class");
                            if(!Utils.isNullString(codeOuid))
                            {
                                DOSChangeable dosCode = dos.getCode(codeOuid);
                                if(dosCode != null)
                                {
                                    codeVisualType = Utils.getInt((Integer)dosCode.get("visual.type"));
                                    switch(codeVisualType)
                                    {
                                    default:
                                        break;

                                    case 1: // '\001'
                                        if(instanceObject != null && instanceObject.get(fieldName) != null)
                                        {
                                            tempInstance.put(fieldName, instanceObject.get(fieldName));
                                            break;
                                        }
                                        if(!Utils.isNullString(initValue))
                                        {
                                            DOSChangeable codeData = dos.getCodeItem(initValue);
                                            fieldTextField = (JTextField)hoardData.get(fieldName + fieldGroupName);
                                            fieldTextField.setText(codeData.get("name") + " [" + codeData.get("codeitemid") + "]");
                                            hoardData.put("initvalue@@" + fieldName, initValue);
                                            tempInstance.put(fieldName, initValue);
                                        }
                                        break;

                                    case 2: // '\002'
                                        fieldTextField = null;
                                        fieldComboBox = (JComboBox)hoardData.get(fieldName + fieldGroupName);
                                        if(fieldComboBox == null)
                                            break;
                                        if(comboBoxModel == null)
                                            setComboBoxModelFromCode(fieldComboBox, dosCode, dos, fieldUIData);
                                        else
                                            fieldComboBox.setModel(comboBoxModel);
                                        comboBoxModel = (DefaultComboBoxModel)fieldComboBox.getModel();
                                        if(comboBoxModel.getSelectedItem() != null)
                                            tempInstance.put(fieldName, ((MComboBoxModelObject)comboBoxModel.getSelectedItem()).getOuid());
                                        else
                                        if(!Utils.isNullString(initValue))
                                        {
                                            DOSChangeable codeData = dos.getCodeItem(initValue);
                                            MComboBoxModelObject mcbmo = null;
                                            for(int m = 0; m < comboBoxModel.getSize(); m++)
                                            {
                                                mcbmo = (MComboBoxModelObject)comboBoxModel.getElementAt(m);
                                                if(!codeData.get("ouid").equals(mcbmo.getOuid()))
                                                    continue;
                                                fieldComboBox.setSelectedIndex(m);
                                                tempInstance.put(fieldName, initValue);
                                                break;
                                            }

                                        }
                                        fieldComboBox = null;
                                        break;
                                    }
                                    dosCode = null;
                                } else
                                {
                                    codeVisualType = 0;
                                }
                            } else
                            {
                                codeVisualType = 1;
                            }
                        }
                    } else
                    if(fieldType.byteValue() == 25)
                    {
                        codeOuid = dos.lookupCodeFromSelectionTable((String)fieldUIData.get("ouid"), tempInstance);
                        initValue = dos.lookupInitialCodeFromSelectionTable((String)fieldUIData.get("ouid"), tempInstance);
                        DOSChangeable dosCode = null;
                        if(dos.isComboBoxMatrixCode((String)fieldUIData.get("ouid")))
                        {
                            codeVisualType = 2;
                            fieldTextField = null;
                            fieldComboBox = (JComboBox)hoardData.get(fieldName + fieldGroupName);
                            if(fieldComboBox == null)
                                break;
                            if(!Utils.isNullString(codeOuid))
                                dosCode = dos.getCode(codeOuid);
                            setComboBoxModelFromCode(fieldComboBox, dosCode, dos, fieldUIData);
                            comboBoxModel = (DefaultComboBoxModel)fieldComboBox.getModel();
                            if(!Utils.isNullString(initValue))
                            {
                                DOSChangeable codeData = dos.getCodeItem(initValue);
                                MComboBoxModelObject mcbmo = null;
                                for(int m = 0; m < comboBoxModel.getSize(); m++)
                                {
                                    mcbmo = (MComboBoxModelObject)comboBoxModel.getElementAt(m);
                                    if(!codeData.get("ouid").equals(mcbmo.getOuid()))
                                        continue;
                                    fieldComboBox.setSelectedIndex(m);
                                    hoardData.put("initvalue@@" + fieldName, initValue);
                                    tempInstance.put(fieldName, initValue);
                                    break;
                                }

                            } else
                            {
                                fieldComboBox.setSelectedIndex(-1);
                                hoardData.put("initvalue@@" + fieldName, null);
                                tempInstance.put(fieldName, null);
                            }
                            fieldComboBox = null;
                        } else
                        {
                            codeVisualType = 1;
                            initValue = dos.lookupInitialCodeFromSelectionTable((String)fieldUIData.get("ouid"), tempInstance);
                            if(!Utils.isNullString(initValue))
                            {
                                DOSChangeable codeData = dos.getCodeItem(initValue);
                                fieldTextField = (JTextField)hoardData.get(fieldName + fieldGroupName);
                                fieldTextField.setText(codeData.get("name") + " [" + codeData.get("codeitemid") + "]");
                                fieldTextField.setCaretPosition(0);
                                hoardData.put("initvalue@@" + fieldName, initValue);
                                tempInstance.put(fieldName, initValue);
                            } else
                            {
                                fieldTextField.setText("");
                                hoardData.put("initvalue@@" + fieldName, null);
                                tempInstance.put(fieldName, null);
                            }
                        }
                    } else
                    {
                        codeVisualType = 0;
                    }
                }
                fieldTextField = null;
                comboBoxModel = null;
                fieldComboBox = null;
            }

        }
    }

    public static void refreshFieldGroupPanel(String fieldOuid, DOS dos, DOSChangeable hoardData, DOSChangeable fieldGroupDC, HashMap fieldAndFieldGroupMap, DOSChangeable instanceObject)
        throws IIPRequestException
    {
        if(hoardData == null)
            return;
        Boolean isVisible = null;
        DOSChangeable tempInstance = new DOSChangeable();
        String fieldGroupName = (String)fieldGroupDC.get("name");
        ArrayList actionList = (ArrayList)fieldGroupDC.get("array$ouid@action");
        ArrayList fieldList = (ArrayList)fieldGroupDC.get("array$ouid@field");
        ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroupDC.get("ouid"));
        if(inheritedFields != null)
        {
            if(fieldList == null)
                fieldList = new ArrayList();
            ArrayList tmpFieldList = new ArrayList();
            DOSChangeable tmpData = null;
            for(int j = 0; j < inheritedFields.size(); j++)
            {
                tmpData = (DOSChangeable)inheritedFields.get(j);
                if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                    tmpFieldList.add((String)tmpData.get("ouid"));
            }

            if(tmpFieldList.size() > 0)
                fieldList.addAll(0, tmpFieldList);
        }
        if(fieldList != null)
        {
            DOSChangeable fieldUIData = null;
            ArrayList fieldUIList = new ArrayList();
            for(int j = 0; j < fieldList.size(); j++)
            {
                fieldUIData = dos.getField((String)fieldList.get(j));
                if(fieldUIData != null)
                {
                    fieldUIList.add(fieldUIData);
                    fieldUIData = null;
                }
            }

            Utils.sort(fieldUIList);
            int codeVisualType = 0;
            byte changeable = 0;
            String initValue = null;
            boolean isCollection = false;
            String fieldName = null;
            String fieldTitle = null;
            Byte fieldType = null;
            DefaultComboBoxModel comboBoxModel = null;
            JTextField fieldTextField = null;
            JComboBox fieldComboBox = null;
            for(int j = 0; j < fieldUIList.size(); j++)
            {
                fieldUIData = (DOSChangeable)fieldUIList.get(j);
                isVisible = (Boolean)fieldUIData.get("is.visible");
                if(isVisible != null && !Utils.getBoolean(isVisible))
                    continue;
                initValue = (String)fieldUIData.get("initial.value");
                fieldName = (String)fieldUIData.get("name");
                fieldTitle = (String)fieldUIData.get("title");
                fieldType = (Byte)fieldUIData.get("type");
                if(Utils.getInt((Integer)fieldUIData.get("multiplicity.to")) > 1)
                    isCollection = true;
                else
                    isCollection = false;
                JComponent comp = null;
                JComponent comp2 = null;
                String tmpFieldGroupName = null;
                if(hoardData.getValueMap().containsValue(fieldName))
                {
                    tmpFieldGroupName = (String)((ArrayList)fieldAndFieldGroupMap.get(fieldName)).get(0);
                    comp = (JComponent)hoardData.get(fieldName + tmpFieldGroupName);
                    if(comp instanceof JComboBox)
                        comboBoxModel = (DefaultComboBoxModel)((JComboBox)comp).getModel();
                }
                if(!isCollection && (fieldType.byteValue() == 24 || fieldType.byteValue() == 25))
                {
                    String codeOuid = null;
                    if(fieldType.byteValue() == 25)
                        fieldUIData.put("type.ouid@class", null);
                    if(fieldUIData.get("type.ouid@class") != null)
                    {
                        if(fieldType.byteValue() == 24)
                        {
                            codeOuid = (String)fieldUIData.get("type.ouid@class");
                            if(!Utils.isNullString(codeOuid))
                            {
                                DOSChangeable dosCode = dos.getCode(codeOuid);
                                if(dosCode != null)
                                {
                                    codeVisualType = Utils.getInt((Integer)dosCode.get("visual.type"));
                                    switch(codeVisualType)
                                    {
                                    default:
                                        break;

                                    case 1: // '\001'
                                        if(instanceObject != null && instanceObject.get(fieldName) != null)
                                        {
                                            tempInstance.put(fieldName, instanceObject.get(fieldName));
                                            break;
                                        }
                                        if(!Utils.isNullString(initValue))
                                        {
                                            DOSChangeable codeData = dos.getCodeItem(initValue);
                                            fieldTextField = (JTextField)hoardData.get(fieldName + fieldGroupName);
                                            fieldTextField.setText(codeData.get("name") + " [" + codeData.get("codeitemid") + "]");
                                            hoardData.put("initvalue@@" + fieldName, initValue);
                                            tempInstance.put(fieldName, initValue);
                                        }
                                        break;

                                    case 2: // '\002'
                                        fieldTextField = null;
                                        fieldComboBox = (JComboBox)hoardData.get(fieldName + fieldGroupName);
                                        if(fieldComboBox == null)
                                            break;
                                        if(comboBoxModel == null)
                                            setComboBoxModelFromCode(fieldComboBox, dosCode, dos, fieldUIData);
                                        else
                                            fieldComboBox.setModel(comboBoxModel);
                                        comboBoxModel = (DefaultComboBoxModel)fieldComboBox.getModel();
                                        if(comboBoxModel.getSelectedItem() != null)
                                            tempInstance.put(fieldName, ((MComboBoxModelObject)comboBoxModel.getSelectedItem()).getOuid());
                                        else
                                        if(!Utils.isNullString(initValue))
                                        {
                                            DOSChangeable codeData = dos.getCodeItem(initValue);
                                            MComboBoxModelObject mcbmo = null;
                                            for(int m = 0; m < comboBoxModel.getSize(); m++)
                                            {
                                                mcbmo = (MComboBoxModelObject)comboBoxModel.getElementAt(m);
                                                if(!codeData.get("ouid").equals(mcbmo.getOuid()))
                                                    continue;
                                                fieldComboBox.setSelectedIndex(m);
                                                tempInstance.put(fieldName, initValue);
                                                break;
                                            }

                                        }
                                        fieldComboBox = null;
                                        break;
                                    }
                                    dosCode = null;
                                } else
                                {
                                    codeVisualType = 0;
                                }
                            } else
                            {
                                codeVisualType = 1;
                            }
                        }
                    } else
                    if(fieldType.byteValue() == 25)
                    {
                        String tempOuid = (String)fieldUIData.get("ouid");
                        codeOuid = dos.lookupCodeFromSelectionTable((String)fieldUIData.get("ouid"), fieldOuid, tempInstance);
                        if(Utils.isNullString(codeOuid))
                            continue;
                        initValue = dos.lookupInitialCodeFromSelectionTable((String)fieldUIData.get("ouid"), tempInstance);
                        DOSChangeable dosCode = null;
                        if(dos.isComboBoxMatrixCode((String)fieldUIData.get("ouid")))
                        {
                            codeVisualType = 2;
                            fieldTextField = null;
                            fieldComboBox = (JComboBox)hoardData.get(fieldName + fieldGroupName);
                            if(fieldComboBox == null)
                                break;
                            if(!Utils.isNullString(codeOuid))
                                dosCode = dos.getCode(codeOuid);
                            setComboBoxModelFromCode(fieldComboBox, dosCode, dos, fieldUIData);
                            comboBoxModel = (DefaultComboBoxModel)fieldComboBox.getModel();
                            if(!Utils.isNullString(initValue))
                            {
                                DOSChangeable codeData = dos.getCodeItem(initValue);
                                MComboBoxModelObject mcbmo = null;
                                for(int m = 0; m < comboBoxModel.getSize(); m++)
                                {
                                    mcbmo = (MComboBoxModelObject)comboBoxModel.getElementAt(m);
                                    if(!codeData.get("ouid").equals(mcbmo.getOuid()))
                                        continue;
                                    fieldComboBox.setSelectedIndex(m);
                                    hoardData.put("initvalue@@" + fieldName, initValue);
                                    tempInstance.put(fieldName, initValue);
                                    break;
                                }

                            } else
                            {
                                fieldComboBox.setSelectedIndex(-1);
                                hoardData.put("initvalue@@" + fieldName, null);
                                tempInstance.put(fieldName, null);
                            }
                            fieldComboBox = null;
                        } else
                        {
                            codeVisualType = 1;
                            initValue = dos.lookupInitialCodeFromSelectionTable((String)fieldUIData.get("ouid"), tempInstance);
                            if(!Utils.isNullString(initValue))
                            {
                                DOSChangeable codeData = dos.getCodeItem(initValue);
                                fieldTextField = (JTextField)hoardData.get(fieldName + fieldGroupName);
                                fieldTextField.setText(codeData.get("name") + " [" + codeData.get("codeitemid") + "]");
                                fieldTextField.setCaretPosition(0);
                                hoardData.put("initvalue@@" + fieldName, initValue);
                                tempInstance.put(fieldName, initValue);
                            } else
                            {
                                fieldTextField.setText("");
                                hoardData.put("initvalue@@" + fieldName, null);
                                tempInstance.put(fieldName, null);
                            }
                        }
                    } else
                    {
                        codeVisualType = 0;
                    }
                }
                fieldTextField = null;
                comboBoxModel = null;
                fieldComboBox = null;
            }

        }
    }

    private static Table makeCollectionTable(DOSChangeable fieldUIData, HashMap collectionMap, MouseListener mouseListener)
    {
        if(fieldUIData == null)
            return null;
        ArrayList columnName = new ArrayList();
        ArrayList columnWidth = new ArrayList();
        ArrayList searchData = new ArrayList();
        Table table = null;
        byte fieldType = Utils.getByte((Byte)fieldUIData.get("type"));
        int width = 0;
        int titleWidth = 0;
        width = Utils.getInt((Integer)fieldUIData.get("width"));
        titleWidth = Utils.getInt((Integer)fieldUIData.get("title.width"));
        if(width == 0)
            width = 250;
        if(titleWidth == 0)
            titleWidth = 80;
        width = titleWidth - width;
        if(fieldType == 24)
        {
            columnName.add("Code");
            columnName.add("Name");
            columnName.add("Description");
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            table = new Table(searchData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[] {
                1, 2, 3
            });
        } else
        if(fieldType == 25)
        {
            columnName.add("Code");
            columnName.add("Name");
            columnName.add("Description");
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            table = new Table(searchData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[] {
                1, 2, 3
            });
        } else
        if(fieldType == 16)
        {
            columnName.add("Number");
            columnName.add("Description");
            columnName.add("Status");
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            table = new Table(searchData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[] {
                1, 2, 3
            });
        } else
        {
            columnName.add(DynaMOAD.getMSRString((String)fieldUIData.get("code"), (String)fieldUIData.get("title"), 0));
            columnWidth.add(new Integer(width));
            table = new Table(searchData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[1]);
        }
        collectionMap.put(table.getTable(), fieldUIData);
        collectionMap.put(table.getTable().hashCode() + "Table", table);
        ArrayList tables = (ArrayList)collectionMap.get((String)fieldUIData.get("name") + "TableData");
        if(tables == null)
        {
            tables = new ArrayList();
            collectionMap.put((String)fieldUIData.get("name") + "TableData", tables);
        }
        tables.add(table);
        table.setIndexColumn(0);
        table.getTable().setCursor(handCursor);
        table.getTable().getTableHeader().setBackground(DynaTheme.tableheaderColor);
        table.getTable().addMouseListener(mouseListener);
        TableColumn tc;
        for(Enumeration enum = table.getTable().getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(getHeaderRenderer()))
            tc = (TableColumn)enum.nextElement();

        return table;
    }

    private static Table makeCollectionTable(DOSChangeable fieldUIData, HashMap collectionMap, MouseListener mouseListener, ArrayList tableData)
    {
        if(fieldUIData == null)
            return null;
        ArrayList columnName = new ArrayList();
        ArrayList columnWidth = new ArrayList();
        Table table = null;
        byte fieldType = Utils.getByte((Byte)fieldUIData.get("type"));
        int width = 0;
        int titleWidth = 0;
        width = Utils.getInt((Integer)fieldUIData.get("width"));
        titleWidth = Utils.getInt((Integer)fieldUIData.get("title.width"));
        if(width == 0)
            width = 250;
        if(titleWidth == 0)
            titleWidth = 80;
        width = titleWidth - width;
        if(fieldType == 24)
        {
            columnName.add("Code");
            columnName.add("Name");
            columnName.add("Description");
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            table = new Table(tableData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[] {
                1, 2, 3
            });
        } else
        if(fieldType == 25)
        {
            columnName.add("Code");
            columnName.add("Name");
            columnName.add("Description");
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            table = new Table(tableData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[] {
                1, 2, 3
            });
        } else
        if(fieldType == 16)
        {
            columnName.add("Number");
            columnName.add("Description");
            columnName.add("Status");
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            columnWidth.add(new Integer(80));
            table = new Table(tableData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[] {
                1, 2, 3
            });
        } else
        {
            columnName.add(DynaMOAD.getMSRString((String)fieldUIData.get("code"), (String)fieldUIData.get("title"), 0));
            columnWidth.add(new Integer(width));
            table = new Table(tableData, columnName, columnWidth, 0, width);
            table.setColumnSequence(new int[1]);
        }
        collectionMap.put(table.getTable(), fieldUIData);
        collectionMap.put(table.getTable().hashCode() + "Table", table);
        ArrayList tables = (ArrayList)collectionMap.get((String)fieldUIData.get("name") + "TableData");
        if(tables == null)
        {
            tables = new ArrayList();
            collectionMap.put((String)fieldUIData.get("name") + "TableData", tables);
        }
        tables.add(table);
        table.setIndexColumn(0);
        table.getTable().setCursor(handCursor);
        table.getTable().getTableHeader().setBackground(DynaTheme.tableheaderColor);
        table.getTable().addMouseListener(mouseListener);
        TableColumn tc;
        for(Enumeration enum = table.getTable().getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(getHeaderRenderer()))
            tc = (TableColumn)enum.nextElement();

        return table;
    }

    public static HeaderRenderer getHeaderRenderer()
    {
        if(defaultHeadRenderer == null)
            defaultHeadRenderer = new HeaderRenderer();
        return defaultHeadRenderer;
    }

    public static HeaderRenderer getHeaderRenderer(int height)
    {
        return new HeaderRenderer(height);
    }

    public static void setComboBoxModelFromCode(JComboBox comboBox, DOSChangeable dosCode, DOS dos, DOSChangeable fieldUIData)
    {
        if(comboBox == null || dosCode == null || dos == null)
            return;
        try
        {
            String ouid = (String)dosCode.get("ouid");
            ArrayList codeList = dos.listCodeItem(ouid);
            Vector tempList = null;
            if(Utils.isNullArrayList(codeList))
            {
                tempList = new Vector();
            } else
            {
                tempList = new Vector(codeList.size());
                DOSChangeable codeItem = null;
                Iterator listKey;
                for(listKey = codeList.iterator(); listKey.hasNext();)
                {
                    codeItem = (DOSChangeable)listKey.next();
                    tempList.add(new MComboBoxModelObject((String)codeItem.get("ouid"), (String)codeItem.get("codeitemid"), (String)codeItem.get("name"), (String)codeItem.get("description")));
                    codeItem = null;
                }

                listKey = null;
                codeList.clear();
                codeList = null;
            }
            comboBox.setModel(new DefaultComboBoxModel(tempList));
            comboBox.setSelectedIndex(-1);
            tempList = null;
        }
        catch(IIPRequestException ie)
        {
            System.err.println(ie);
        }
    }

    public static void createProcessPopupMenuItems(JMenu processMenu, ArrayList ouidList, DOS dos, WFM wfm, ACL acl, ActionListener actionListener)
        throws IIPRequestException
    {
        if(processMenu == null || Utils.isNullArrayList(ouidList) || dos == null || wfm == null || actionListener == null)
            return;
        clearMenuItems(processMenu);
        JMenuItem procMenuItem = null;
        String lcClassOuid = null;
        String startClassOuid = null;
        String procOuid = null;
        String instanceOuid = null;
        DOSChangeable processDef = null;
        ArrayList procList = null;
        ArrayList classList = null;
        HashMap procMap = null;
        HashMap procMapPool = null;
        String selectOuid = null;
        String processOuid = null;
        boolean chk = false;
        procMapPool = new HashMap();
        procMap = new HashMap();
        for(int m = 0; m < ouidList.size(); m++)
        {
            selectOuid = (String)ouidList.get(m);
            processOuid = wfm.isLocked(selectOuid);
            if(!Utils.isNullString(processOuid))
            {
                procMapPool.clear();
                break;
            }
            startClassOuid = dos.getClassOuid(selectOuid);
            classList = dos.listAllSuperClassOuid(startClassOuid);
            if(classList == null)
                classList = new ArrayList();
            classList.add(0, startClassOuid);
            for(int n = 0; n < classList.size(); n++)
            {
                lcClassOuid = (String)classList.get(n);
                procList = wfm.listProcessDeifnitionOfClass(lcClassOuid);
                if(procList != null)
                {
                    for(int i = 0; i < procList.size(); i++)
                    {
                        procOuid = "wf$dpro@" + (String)procList.get(i);
                        if(!procMap.containsKey(procOuid))
                            procMap.put(procOuid, null);
                    }

                    procList.clear();
                    procList = null;
                }
            }

            classList.clear();
            classList = null;
            if(procMapPool.isEmpty() && !procMap.isEmpty())
                procMapPool.putAll(procMap);
            for(Iterator intersectKey = procMapPool.keySet().iterator(); intersectKey.hasNext();)
                if(!procMap.containsKey(intersectKey.next()))
                    intersectKey.remove();

            procMap.clear();
        }

        procMap.clear();
        procMap = null;
        Iterator procKey = procMapPool.keySet().iterator();
        if(procKey != null)
        {
            while(procKey.hasNext()) 
            {
                procOuid = (String)procKey.next();
                processDef = wfm.getProcessDefinition(procOuid);
                if(processDef == null)
                {
                    processDef.clear();
                    processDef = null;
                } else
                {
                    procMenuItem = new JMenuItem(DynaMOAD.getMSRString((String)processDef.get("name"), (String)processDef.get("name"), 0) + " [" + (String)processDef.get("identifier") + "]");
                    procMenuItem.setIcon(new ImageIcon("icons/Process.gif"));
                    StringBuffer actionCommand = new StringBuffer("process/" + procOuid);
                    for(int i = 0; i < ouidList.size(); i++)
                    {
                        selectOuid = (String)ouidList.get(i);
                        startClassOuid = dos.getClassOuid(selectOuid);
                        if(!LogIn.isAdmin)
                        {
                            String statusId = DynaMOAD.dos.getStatus(selectOuid);
                            chk = acl.hasPermission4Process(startClassOuid, selectOuid, procOuid, statusId, LogIn.userID);
                            if(!chk)
                                procMenuItem.setEnabled(false);
                        }
                        actionCommand.append("/" + selectOuid);
                    }

                    procMenuItem.setActionCommand(actionCommand.toString());
                    procMenuItem.addActionListener(actionListener);
                    processMenu.add(procMenuItem);
                    procMenuItem = null;
                }
            }
            procKey = null;
        }
        if(processMenu.getMenuComponentCount() == 0)
            processMenu.setEnabled(false);
        else
            processMenu.setEnabled(true);
        procMapPool.clear();
        procMapPool = null;
    }

    public static void createFilePopupMenuItems(JMenu fileMenu, ArrayList ouidList, DOS dos, ACL acl, ActionListener actionListener, String assoOuid)
        throws IIPRequestException
    {
        if(fileMenu == null || Utils.isNullArrayList(ouidList) || dos == null || acl == null || actionListener == null)
            return;
        clearMenuItems(fileMenu);
        if(ouidList.size() > 1)
        {
            fileMenu.setEnabled(false);
            boolean fileMenuEnabled = false;
            boolean checkOutMenuEnabled = false;
            boolean checkInMenuEnabled = false;
            String selectOuid = null;
            HashMap file = null;
            String checkedOutDate = null;
            String checkedInDate = null;
            ArrayList fileList = null;
            String status = null;
            boolean isUpdatable = false;
            for(int i = 0; i < ouidList.size(); i++)
            {
                selectOuid = (String)ouidList.get(i);
                fileList = dos.listFile(selectOuid);
                status = dos.getStatus(selectOuid);
                isUpdatable = LogIn.isAdmin || acl.hasPermission(dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
                if(!Utils.isNullArrayList(fileList) && isUpdatable)
                {
                    fileMenuEnabled = true;
                    for(int j = 0; j < fileList.size(); j++)
                    {
                        file = (HashMap)fileList.get(j);
                        if("CRT".equals(status) || "WIP".equals(status) || "REJ".equals(status))
                            isUpdatable = isUpdatable;
                        else
                            isUpdatable = false;
                        if(isUpdatable)
                        {
                            checkedOutDate = (String)file.get("md$checkedout.date");
                            checkedInDate = (String)file.get("md$checkedin.date");
                            if(!Utils.isNullString(checkedOutDate) && "null".equals(checkedOutDate))
                                checkedOutDate = null;
                            if(!Utils.isNullString(checkedInDate) && "null".equals(checkedInDate))
                                checkedInDate = null;
                            if(Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate))
                                checkOutMenuEnabled = true;
                            else
                            if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) > 0)
                                checkInMenuEnabled = true;
                            else
                            if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) <= 0)
                                checkOutMenuEnabled = true;
                            if(checkOutMenuEnabled && checkInMenuEnabled)
                                break;
                        }
                        if(checkOutMenuEnabled && checkInMenuEnabled)
                            break;
                    }

                }
            }

            fileMenu.setEnabled(fileMenuEnabled);
            if(!fileMenu.isEnabled())
                return;
            JMenuItem fileCheckOutMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0060", "Check-Out", 0));
            fileCheckOutMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/CheckOut.gif")));
            if(actionListener instanceof UIGeneration)
            {
                if(Utils.isNullString(assoOuid))
                    fileCheckOutMenuItem.setActionCommand("file/multi_checkout");
                else
                    fileCheckOutMenuItem.setActionCommand("file/multi_checkout^" + assoOuid);
            } else
            {
                fileCheckOutMenuItem.setActionCommand("file/multi_checkout");
            }
            fileCheckOutMenuItem.addActionListener(actionListener);
            fileCheckOutMenuItem.setEnabled(checkOutMenuEnabled);
            fileMenu.add(fileCheckOutMenuItem);
            JMenuItem fileCheckInMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0061", "Check-In", 0));
            fileCheckInMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/CheckIn.gif")));
            if(actionListener instanceof UIGeneration)
            {
                if(Utils.isNullString(assoOuid))
                    fileCheckInMenuItem.setActionCommand("file/multi_checkin");
                else
                    fileCheckInMenuItem.setActionCommand("file/multi_checkin^" + assoOuid);
            } else
            {
                fileCheckInMenuItem.setActionCommand("file/multi_checkin");
            }
            fileCheckInMenuItem.addActionListener(actionListener);
            fileCheckInMenuItem.setEnabled(checkInMenuEnabled);
            fileMenu.add(fileCheckInMenuItem);
            JMenuItem fileCheckOutCancelMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0166", "Check-Out Cancel", 0));
            if(actionListener instanceof UIGeneration)
            {
                if(Utils.isNullString(assoOuid))
                    fileCheckOutCancelMenuItem.setActionCommand("file/multi_cancel_checkout");
                else
                    fileCheckOutCancelMenuItem.setActionCommand("file/multi_cancel_checkout^" + assoOuid);
            } else
            {
                fileCheckOutCancelMenuItem.setActionCommand("file/multi_cancel_checkout");
            }
            fileCheckOutCancelMenuItem.addActionListener(actionListener);
            fileCheckOutCancelMenuItem.setEnabled(checkInMenuEnabled);
            fileMenu.add(fileCheckOutCancelMenuItem);
        } else
        {
            String selectOuid = (String)ouidList.get(0);
            HashMap file = null;
            ArrayList fileList = dos.listFile(selectOuid);
            String tooltipString = null;
            boolean isUpdatable = LogIn.isAdmin || acl.hasPermission(dos.getClassOuid(selectOuid), selectOuid, "UPDATE", LogIn.userID);
            if(Utils.isNullArrayList(fileList))
            {
                fileMenu.setEnabled(false);
            } else
            {
                for(int i = 0; i < fileList.size(); i++)
                {
                    file = (HashMap)fileList.get(i);
                    JMenu subFileMenu = new JMenu(file.get("md$filetype.description") + " " + file.get("md$index") + " [" + file.get("md$version") + "]");
                    tooltipString = null;
                    tooltipString = "User: " + file.get("md$user.name") + " [" + file.get("md$user.id") + "]";
                    subFileMenu.setToolTipText(tooltipString);
                    javax.swing.Icon checkedOutOverIcon = new ImageIcon(java.lang.Object.class.getResource("/icons/checkedout_over.gif"));
                    javax.swing.Icon icon = ClientUtil.getImageIcon((String)file.get("md$filetype.id"));
                    if(icon == null)
                        icon = new ImageIcon(java.lang.Object.class.getResource("/icons/file_obj.gif"));
                    JMenuItem fileOpenMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0062", "Open", 3));
                    fileOpenMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/executionView.gif")));
                    fileOpenMenuItem.setActionCommand("file/open^" + file.get("md$ouid") + "^" + file.get("md$version") + "^" + file.get("md$path") + "^" + file.get("md$filetype.id") + "^" + file.get("md$description") + "^" + file.get("md$filetype.description") + "^" + file.get("md$index"));
                    fileOpenMenuItem.addActionListener(actionListener);
                    subFileMenu.add(fileOpenMenuItem);
                    String status = dos.getStatus(selectOuid);
                    if("CRT".equals(status) || "WIP".equals(status))
                        isUpdatable = isUpdatable;
                    else
                        isUpdatable = false;
                    if(isUpdatable)
                    {
                        JMenuItem fileCheckOutMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0060", "Check-Out", 0));
                        fileCheckOutMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/CheckOut.gif")));
                        fileCheckOutMenuItem.setActionCommand("file/checkout^" + file.get("md$ouid") + "^" + file.get("md$version") + "^" + file.get("md$path") + "^" + file.get("md$filetype.id") + "^" + file.get("md$description") + "^" + file.get("md$filetype.description") + "^" + file.get("md$index"));
                        fileCheckOutMenuItem.addActionListener(actionListener);
                        JMenuItem fileCheckInMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0061", "Check-In", 0));
                        fileCheckInMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/CheckIn.gif")));
                        fileCheckInMenuItem.setActionCommand("file/checkin^" + file.get("md$ouid") + "^" + file.get("md$version") + "^" + file.get("md$path") + "^" + file.get("md$filetype.id") + "^" + file.get("md$description") + "^" + file.get("md$filetype.description") + "^" + file.get("md$index"));
                        fileCheckInMenuItem.addActionListener(actionListener);
                        JMenuItem fileCheckOutCancelMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0166", "Check-Out Cancel", 0));
                        fileCheckOutCancelMenuItem.setActionCommand("file/cancel_checkout^" + file.get("md$ouid") + "^" + file.get("md$version") + "^" + file.get("md$path") + "^" + file.get("md$filetype.id") + "^" + file.get("md$description") + "^" + file.get("md$filetype.description") + "^" + file.get("md$index"));
                        fileCheckOutCancelMenuItem.addActionListener(actionListener);
                        String checkedOutDate = (String)file.get("md$checkedout.date");
                        String checkedInDate = (String)file.get("md$checkedin.date");
                        if(!Utils.isNullString(checkedOutDate) && "null".equals(checkedOutDate))
                            checkedOutDate = null;
                        if(!Utils.isNullString(checkedInDate) && "null".equals(checkedInDate))
                            checkedInDate = null;
                        if(Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate))
                        {
                            fileCheckOutMenuItem.setEnabled(true);
                            fileCheckInMenuItem.setEnabled(false);
                            fileCheckOutCancelMenuItem.setEnabled(false);
                        } else
                        if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) > 0)
                        {
                            icon = new CompoundIcon(icon, checkedOutOverIcon, 4);
                            fileCheckOutMenuItem.setEnabled(false);
                            fileCheckInMenuItem.setEnabled(true);
                            fileCheckOutCancelMenuItem.setEnabled(true);
                        } else
                        if(!Utils.isNullString(checkedOutDate) && !Utils.isNullString(checkedInDate) && checkedOutDate.compareTo(checkedInDate) <= 0)
                        {
                            fileCheckOutMenuItem.setEnabled(true);
                            fileCheckInMenuItem.setEnabled(false);
                            fileCheckOutCancelMenuItem.setEnabled(false);
                        } else
                        {
                            fileCheckOutMenuItem.setEnabled(false);
                            fileCheckInMenuItem.setEnabled(false);
                            fileCheckOutCancelMenuItem.setEnabled(false);
                        }
                        subFileMenu.add(new JSeparator());
                        subFileMenu.add(fileCheckOutMenuItem);
                        subFileMenu.add(fileCheckInMenuItem);
                        subFileMenu.add(fileCheckOutCancelMenuItem);
                    }
                    subFileMenu.setIcon(icon);
                    icon = null;
                    fileMenu.add(subFileMenu);
                    subFileMenu = null;
                }

                fileList.clear();
                fileList = null;
                if(fileMenu.getMenuComponentCount() == 0)
                    fileMenu.setEnabled(false);
                else
                    fileMenu.setEnabled(true);
            }
        }
    }

    public static void clearMenuItems(JMenu menu)
    {
        if(menu == null)
            return;
        Component menus[] = menu.getMenuComponents();
        ActionListener listeners[] = (ActionListener[])null;
        if(menus != null)
        {
            for(int i = 0; i < menus.length; i++)
            {
                listeners = (ActionListener[])menus[i].getListeners(java.awt.event.ActionListener.class);
                if(listeners != null)
                {
                    for(int j = 0; j < listeners.length; j++)
                        ((JMenuItem)menus[i]).removeActionListener(listeners[j]);

                    listeners = (ActionListener[])null;
                    menu.remove(menus[i]);
                }
            }

            menus = (Component[])null;
        }
    }

    public static ArrayList getDefaultFieldList(DOS dos, String classOuid)
        throws IIPRequestException
    {
        if(dos == null || Utils.isNullString(classOuid))
            return new ArrayList(0);
        ArrayList allFieldList = dos.listFieldInClass(classOuid);
        ArrayList fieldGroupList = dos.listFieldGroupInClass(classOuid);
        if(Utils.isNullArrayList(fieldGroupList))
        {
            fieldGroupList = null;
            return new ArrayList(0);
        }
        DOSChangeable fieldGroup = null;
        Iterator listKey = fieldGroupList.iterator();
        while(listKey.hasNext()) 
        {
            fieldGroup = (DOSChangeable)listKey.next();
            if(fieldGroup == null)
                continue;
            if(Utils.getInt((Integer)fieldGroup.get("location")) == 110)
                break;
            fieldGroup = null;
        }
        listKey = null;
        fieldGroupList = null;
        ArrayList fieldList = null;
        ArrayList resultList = null;
        if(fieldGroup != null)
        {
            fieldList = (ArrayList)fieldGroup.get("array$ouid@field");
            ArrayList inheritedFields = dos.listInheritedFieldInFieldGroup((String)fieldGroup.get("ouid"));
            if(inheritedFields != null)
            {
                if(fieldList == null)
                    fieldList = new ArrayList();
                ArrayList tmpFieldList = new ArrayList();
                DOSChangeable tmpData = null;
                for(int j = 0; j < inheritedFields.size(); j++)
                {
                    tmpData = (DOSChangeable)inheritedFields.get(j);
                    if(!fieldList.contains((String)tmpData.get("ouid")) && ((Boolean)tmpData.get("used")).booleanValue())
                        tmpFieldList.add((String)tmpData.get("ouid"));
                }

                if(tmpFieldList.size() > 0)
                    fieldList.addAll(0, tmpFieldList);
            }
        } else
        {
            resultList = allFieldList;
        }
        if(Utils.isNullArrayList(fieldList) && Utils.isNullArrayList(resultList))
        {
            fieldList = null;
            resultList = null;
            resultList = new ArrayList(0);
        } else
        if(!Utils.isNullArrayList(fieldList))
        {
            int i = 0;
            DOSChangeable field = null;
            String ouid = null;
            resultList = new ArrayList(fieldList.size());
            for(listKey = fieldList.iterator(); listKey.hasNext();)
            {
                ouid = (String)listKey.next();
                if(!Utils.isNullString(ouid))
                    for(i = 0; i < allFieldList.size(); i++)
                    {
                        field = (DOSChangeable)allFieldList.get(i);
                        if(field == null)
                            continue;
                        if(ouid.equals(field.get("ouid")))
                        {
                            resultList.add(field);
                            break;
                        }
                        field = null;
                    }

            }

            listKey = null;
        }
        return resultList;
    }

    public static JTreeTable createLinkTableTree(DOS dos, String rootOuid, String classOuid, DOSChangeable instance, int processMode, ArrayList assoList, HashMap searchConditionMap)
        throws IIPRequestException
    {
        if(dos == null || Utils.isNullString(rootOuid) || instance == null || assoList == null)
            return null;
        JTreeTable linkTreeTable = null;
        LinkTreeModel linkModel = null;
        ArrayList columnOuidList = null;
        ArrayList columnNameList = null;
        ArrayList columnClassList = null;
        ArrayList columnTypeList = null;
        ArrayList columnList = null;
        byte dataType = 0;
        try
        {
            DOSChangeable getDC = instance;
            TreeNodeObject treeNodeData = new TreeNodeObject(rootOuid + "^" + classOuid, (String)getDC.get("md$number"), "Class");
            DefaultMutableTreeNode linkRootNode = new DefaultMutableTreeNode(treeNodeData);
            linkModel = new LinkTreeModel(linkRootNode, assoList, processMode);
            linkTreeTable = new JTreeTable(linkModel);
            ArrayList tmpColumnOuidList = linkModel.getColumnOuidList();
            ArrayList rows = null;
            if(processMode == 0)
            {
                System.out.println("makeLinkTable : PROCESSING_MODE = 0 : listLinkFrom");
                if(searchConditionMap == null)
                    rows = dos.listLinkFrom(rootOuid, tmpColumnOuidList);
                else
                    rows = dos.listLinkFrom(rootOuid, tmpColumnOuidList, searchConditionMap);
            } else
            {
                System.out.println("makeLinkTable : PROCESSING_MODE != 0 : listLinkTo");
                if(searchConditionMap == null)
                    rows = dos.listLinkTo(rootOuid, tmpColumnOuidList);
                else
                    rows = dos.listLinkTo(rootOuid, tmpColumnOuidList, searchConditionMap);
            }
        }
        catch(IIPRequestException ie)
        {
            ie.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        TableColumn tc;
        for(Enumeration enum = linkTreeTable.getColumnModel().getColumns(); enum.hasMoreElements(); tc.setHeaderRenderer(getHeaderRenderer()))
            tc = (TableColumn)enum.nextElement();

        return linkTreeTable;
    }

    public static void createComparePopupMenuItems(JMenu menu, String ouid, ArrayList ouidList, DOSChangeable classDesc)
    {
        if(menu == null || Utils.isNullArrayList(ouidList))
            return;
        JMenuItem compareOtherMenuItem = null;
        JMenuItem compareVersionMenuItem = null;
        JMenuItem compareEachMenuItem = null;
        JMenuItem compareLatestChangesMenuItem = null;
        JMenuItem compareLatestVersionMenuItem = null;
        boolean isVersionable = Utils.getBoolean((Boolean)classDesc.get("is.versionable"));
        if(ouidList.size() == 2)
        {
            compareEachMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_COMPARE_EACH", "Each", 3));
            compareEachMenuItem.setActionCommand("MNU_COMPARE_EACH^" + ouidList.get(0) + "^" + ouidList.get(1));
            compareEachMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
            menu.add(compareEachMenuItem);
            menu.add(new JSeparator());
        }
        compareOtherMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_COMPARE_OTHER", "Other Object", 3));
        compareOtherMenuItem.setActionCommand("MNU_COMPARE_OTHER");
        compareOtherMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        menu.add(compareOtherMenuItem);
        if(isVersionable)
        {
            compareVersionMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_COMPARE_VERSION", "Version", 3));
            compareVersionMenuItem.setActionCommand("MNU_COMPARE_VERSION");
            compareVersionMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
            menu.add(compareVersionMenuItem);
        }
        if(isVersionable)
        {
            compareLatestChangesMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_COMPARE_CHANGES", "Previous Version", 3));
            compareLatestChangesMenuItem.setActionCommand("MNU_COMPARE_CHANGES");
            compareLatestChangesMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
            menu.add(compareLatestChangesMenuItem);
            compareLatestVersionMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_COMPARE_LATEST", "Latest Version", 3));
            compareLatestVersionMenuItem.setActionCommand("MNU_COMPARE_LATEST");
            compareLatestVersionMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
            menu.add(compareLatestVersionMenuItem);
        }
    }

    public static void createActionMenu(JPopupMenu popup, String ouid, ActionListener actionListener, int scope)
    {
        if(popup == null || Utils.isNullString(ouid))
            return;
        LinkedList actionMenuCheck = new LinkedList();
        ArrayList fieldGroupList = null;
        ArrayList actionList = null;
        Iterator actionKey = null;
        DOSChangeable object = null;
        DOSChangeable action = null;
        String lcClassOuid = null;
        String tempString = null;
        JMenu tempMenu = null;
        JMenuItem tempMenuItem = null;
        Byte actionScope = null;
        int location = 0;
        try
        {
            if(ouid.indexOf('$') > 0)
                lcClassOuid = DynaMOAD.dos.getClassOuid(ouid);
            else
                lcClassOuid = ouid;
            fieldGroupList = DynaMOAD.dos.listFieldGroupInClass(lcClassOuid);
            if(Utils.isNullArrayList(fieldGroupList))
                return;
            Iterator fieldGroupKey;
            for(fieldGroupKey = fieldGroupList.iterator(); fieldGroupKey.hasNext();)
            {
                object = (DOSChangeable)fieldGroupKey.next();
                if(object != null)
                {
                    location = Utils.getInt((Integer)object.get("location"));
                    if(location != 100)
                        fieldGroupKey.remove();
                    object = null;
                }
            }

            fieldGroupKey = null;
            if(Utils.isNullArrayList(fieldGroupList))
                return;
            for(fieldGroupKey = fieldGroupList.iterator(); fieldGroupKey.hasNext();)
            {
                object = (DOSChangeable)fieldGroupKey.next();
                actionList = (ArrayList)object.get("array$ouid@action");
                if(!Utils.isNullArrayList(actionList))
                {
                    tempMenu = new JMenu((String)object.get("title"));
                    for(actionKey = actionList.iterator(); actionKey.hasNext();)
                    {
                        tempString = (String)actionKey.next();
                        action = DynaMOAD.dos.getAction(tempString);
                        if(action != null)
                        {
                            actionScope = (Byte)action.get("scope");
                            if(scope != 1 || Utils.getByte(actionScope) != 2)
                            {
                                tempMenuItem = new JMenuItem((String)action.get("title"));
                                tempMenuItem.setActionCommand("action$" + tempString);
                                tempMenuItem.setToolTipText((String)action.get("description"));
                                tempMenuItem.addActionListener(actionListener);
                                tempMenu.add(tempMenuItem);
                                tempString = null;
                            }
                        }
                    }

                    actionKey = null;
                    if(tempMenu.getMenuComponentCount() > 0)
                    {
                        popup.add(tempMenu);
                        actionMenuCheck.add(tempMenu);
                    }
                    tempMenu = null;
                    actionList.clear();
                    actionList = null;
                    object = null;
                }
            }

            fieldGroupList.clear();
            fieldGroupList = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
    }

    public static void createActionMenu(JPopupMenu popup, String ouid, ActionListener actionListener)
    {
        createActionMenu(popup, ouid, actionListener, 2);
    }

    public static ArrayList createActionToolbarButtons(String ouid, ActionListener actionListener, String associationOuid)
    {
        if(Utils.isNullString(ouid))
            return null;
        LinkedList actionMenuCheck = new LinkedList();
        ArrayList fieldGroupList = null;
        ArrayList actionList = null;
        ArrayList resultList = null;
        Iterator actionKey = null;
        DOSChangeable object = null;
        DOSChangeable action = null;
        String lcClassOuid = null;
        String tempString = null;
        JButton actionButton = null;
        Byte actionScope = null;
        Boolean isVisible = null;
        int location = 0;
        ImageIcon imageIconFromSvr = null;
        try
        {
            if(ouid.indexOf('$') > 0)
                lcClassOuid = DynaMOAD.dos.getClassOuid(ouid);
            else
                lcClassOuid = ouid;
            fieldGroupList = DynaMOAD.dos.listFieldGroupInClass(lcClassOuid);
            if(Utils.isNullArrayList(fieldGroupList))
                return null;
            Iterator fieldGroupKey;
            for(fieldGroupKey = fieldGroupList.iterator(); fieldGroupKey.hasNext();)
            {
                object = (DOSChangeable)fieldGroupKey.next();
                if(object != null)
                {
                    location = Utils.getInt((Integer)object.get("location"));
                    if(location != 110)
                        fieldGroupKey.remove();
                    object = null;
                }
            }

            fieldGroupKey = null;
            if(Utils.isNullArrayList(fieldGroupList))
                return null;
            resultList = new ArrayList();
            for(fieldGroupKey = fieldGroupList.iterator(); fieldGroupKey.hasNext();)
            {
                object = (DOSChangeable)fieldGroupKey.next();
                actionList = (ArrayList)object.get("array$ouid@action");
                if(!Utils.isNullArrayList(actionList))
                {
                    for(actionKey = actionList.iterator(); actionKey.hasNext();)
                    {
                        tempString = (String)actionKey.next();
                        action = DynaMOAD.dos.getAction(tempString);
                        if(action != null)
                        {
                            actionScope = (Byte)action.get("scope");
                            isVisible = (Boolean)action.get("is.visible");
                            if(isVisible == null || Utils.getBoolean(isVisible))
                            {
                                actionButton = null;
                                if(!Utils.isNullString((String)action.get("icon")))
                                {
                                    imageIconFromSvr = ClientUtil.getImageIcon((String)action.get("ouid"));
                                    actionButton = new ToolBarButton(imageIconFromSvr);
                                    imageIconFromSvr = null;
                                } else
                                if(!Utils.isNullString((String)action.get("icon24")))
                                    actionButton = new ToolBarButton(new ImageIcon(java.lang.Object.class.getResource("/icons/" + (String)action.get("icon24"))));
                                else
                                if(!Utils.isNullString((String)action.get("icon32")))
                                    actionButton = new ToolBarButton(new ImageIcon(java.lang.Object.class.getResource("/icons/" + (String)action.get("icon32"))));
                                else
                                if(!Utils.isNullString((String)action.get("title")))
                                {
                                    actionButton = new JButton();
                                    actionButton.setText((String)action.get("title"));
                                }
                                actionButton.setToolTipText((String)action.get("title"));
                                if(associationOuid == null)
                                    actionButton.setActionCommand("action$" + action.get("ouid"));
                                else
                                    actionButton.setActionCommand("action^" + action.get("ouid") + "^" + associationOuid);
                                actionButton.addActionListener(actionListener);
                                resultList.add(actionButton);
                                tempString = null;
                            }
                        }
                    }

                    actionKey = null;
                    actionList.clear();
                    actionList = null;
                    object = null;
                }
            }

            fieldGroupList.clear();
            fieldGroupList = null;
        }
        catch(IIPRequestException e)
        {
            System.err.println(e);
        }
        if(Utils.isNullArrayList(resultList))
            resultList = null;
        return resultList;
    }

    public static ArrayList createActionToolbarButtons(String ouid, ActionListener actionListener)
    {
        return createActionToolbarButtons(ouid, actionListener, null);
    }

    public static JPopupMenu createPopupMenuForSearchResult(String ouid, ArrayList ouidList, ArrayList assoList, Component comp, int x, int y, ActionListener actionListener, String modelOuid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid) || Utils.isNullArrayList(ouidList) || comp == null || actionListener == null)
            return null;
        JPopupMenu popup = null;
        JMenu newMenu = null;
        JMenuItem newMenuItem = null;
        JMenuItem makeWIPMenuItem = null;
        JMenuItem openMenuItem = null;
        JMenu openStructureMenu = null;
        JMenuItem openStructureMenuItem = null;
        JMenu fileMenu = null;
        JMenuItem copyMenuItem = null;
        JMenuItem deleteMenuItem = null;
        JMenu sendMenu = null;
        JMenuItem myFolderMenuItem = null;
        JMenu processMenu = null;
        JMenu compareMenu = null;
        JMenuItem authorityMenuItem = null;
        String classOuid = DynaMOAD.dos.getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            return null;
        DOSChangeable classDesc = DynaMOAD.dos.getClass(classOuid);
        popup = new JPopupMenu();
        newMenu = new JMenu(DynaMOAD.getMSRString("MNU_NEW", "New", 3));
        newMenu.setMnemonic(78);
        newMenu.setIcon(new ImageIcon("icons/Registry.gif"));
        popup.add(newMenu);
        newMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_NEW_THIS", "As a this Object", 3));
        newMenuItem.setActionCommand("MNU_NEW_THIS^" + ouid);
        newMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        newMenu.add(newMenuItem);
        popup.add(new JSeparator());
        openMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3));
        openMenuItem.setMnemonic(79);
        openMenuItem.setIcon(new ImageIcon("icons/Open.gif"));
        openMenuItem.setActionCommand("Information");
        openMenuItem.addActionListener(actionListener);
        popup.add(openMenuItem);
        if(!Utils.isNullArrayList(assoList))
        {
            openStructureMenu = new JMenu(DynaMOAD.getMSRString("MNU_OPEN_STRUCTURE", "Structure", 3));
            openStructureMenu.setIcon(new ImageIcon("icons/TreeView.gif"));
            popup.add(openStructureMenu);
        }
        fileMenu = new JMenu(DynaMOAD.getMSRString("MNU_FILE", "File", 3));
        fileMenu.setMnemonic(70);
        createFilePopupMenuItems(fileMenu, ouidList, DynaMOAD.dos, DynaMOAD.acl, actionListener, null);
        if(fileMenu.isEnabled())
            popup.add(fileMenu);
        else
            fileMenu = null;
        popup.add(new JSeparator());
        copyMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0009", "Copy", 3));
        copyMenuItem.setActionCommand("copy");
        copyMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyMenuItem.addActionListener(actionListener);
        popup.add(copyMenuItem);
        deleteMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0002", "Delete", 3));
        deleteMenuItem.setIcon(new ImageIcon("icons/Delete.gif"));
        deleteMenuItem.setActionCommand("MNU_DELETE^" + ouid);
        deleteMenuItem.addActionListener(actionListener);
        deleteMenuItem.setName("Delete");
        popup.add(deleteMenuItem);
        if((actionListener instanceof SearchResult) || (actionListener instanceof SearchResult4CADIntegration))
        {
            popup.add(new JSeparator());
            sendMenu = new JMenu(DynaMOAD.getMSRString("MNU_SEND", "Send", 3));
            popup.add(sendMenu);
            myFolderMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_SEND_TO_MYFOLDER", "My Folder", 3));
            myFolderMenuItem.setActionCommand("Favorite^" + ouid);
            myFolderMenuItem.setIcon(new ImageIcon("icons/Favorite.gif"));
            myFolderMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
            sendMenu.add(myFolderMenuItem);
        }
        processMenu = new JMenu(DynaMOAD.getMSRString("WRD_0058", "Create a Process", 3));
        createProcessPopupMenuItems(processMenu, ouidList, DynaMOAD.dos, DynaMOAD.wfm, DynaMOAD.acl, actionListener);
        if(processMenu.isEnabled())
        {
            if(popup.getComponentIndex(sendMenu) == -1)
                popup.add(new JSeparator());
            popup.add(processMenu);
        } else
        {
            processMenu = null;
        }
        createActionMenu(popup, ouid, actionListener);
        popup.add(new JSeparator());
        compareMenu = new JMenu(DynaMOAD.getMSRString("MNU_COMPARE", "Compare with", 3));
        popup.add(compareMenu);
        createComparePopupMenuItems(compareMenu, ouid, ouidList, classDesc);
        popup.add(new JSeparator());
        authorityMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0051", "Authority", 3));
        authorityMenuItem.setActionCommand("SetAuthority^" + ouid);
        authorityMenuItem.setIcon(new ImageIcon("icons/Acl.gif"));
        authorityMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        popup.add(authorityMenuItem);
        if(LogIn.isAdmin)
        {
            authorityMenuItem.setEnabled(true);
        } else
        {
            ArrayList aclList = DynaMOAD.acl.retrievePrivateACL4Grant(classOuid, ouid, LogIn.userID, new Boolean(LogIn.isAdmin));
            if(Utils.isNullArrayList(aclList))
                authorityMenuItem.setEnabled(false);
            else
                authorityMenuItem.setEnabled(true);
        }
        openMenuItem.setEnabled(true);
        if(openStructureMenu != null)
            openStructureMenu.setEnabled(true);
        deleteMenuItem.setEnabled(true);
        if(!LogIn.isAdmin)
            try
            {
                boolean chk = false;
                chk = DynaMOAD.acl.hasPermission(classOuid, ouid, "CREATE", LogIn.userID);
                newMenu.setEnabled(chk);
                chk = DynaMOAD.acl.hasPermission(classOuid, ouid, "READ", LogIn.userID);
                openMenuItem.setEnabled(chk);
                if(openStructureMenu != null)
                    openStructureMenu.setEnabled(chk);
                if((actionListener instanceof SearchResult) || (actionListener instanceof SearchResult4CADIntegration))
                {
                    chk = DynaMOAD.acl.hasPermission(classOuid, ouid, "DELETE", LogIn.userID);
                    deleteMenuItem.setEnabled(chk);
                }
            }
            catch(IIPRequestException e1)
            {
                System.err.println(e1);
            }
        if(ouidList.size() == 1)
        {
            String statusId = DynaMOAD.dos.getStatus(ouid);
            if(((actionListener instanceof SearchResult) || (actionListener instanceof SearchResult4CADIntegration)) && !statusId.equals("CRT") && !statusId.equals("WIP"))
                deleteMenuItem.setEnabled(false);
            if(classDesc != null && newMenu.isEnabled())
            {
                if("RLS".equals(statusId) && Utils.getBoolean((Boolean)classDesc.get("is.versionable")) && (LogIn.isAdmin || DynaMOAD.acl.hasPermission(classOuid, ouid, "UPDATE", LogIn.userID)))
                {
                    newMenu.add(new JSeparator());
                    makeWIPMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0059", "Make a WIP Version", 3));
                    makeWIPMenuItem.setActionCommand("makeWIPVersion^" + ouid);
                    makeWIPMenuItem.setIcon(new ImageIcon("icons/MakeWIP.gif"));
                    makeWIPMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
                    newMenu.add(makeWIPMenuItem);
                } else
                if((actionListener instanceof SearchCondition4CADIntegration) || (actionListener instanceof SearchResult4CADIntegration))
                    newMenu.setEnabled(false);
                classDesc.clear();
                classDesc = null;
            }
        }
        if(openStructureMenu != null)
        {
            openStructureMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3));
            openStructureMenuItem.setMnemonic(79);
            openStructureMenuItem.setActionCommand("tree view/Default");
            openStructureMenuItem.addActionListener(actionListener);
            openStructureMenu.add(openStructureMenuItem);
            openStructureMenuItem = null;
            ArrayList pubFieldColumn = DynaMOAD.nds.getChildNodeList("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid);
            String filterArgument = null;
            ArrayList filterField = null;
            String filterID = null;
            String value = null;
            if(pubFieldColumn != null)
            {
                openStructureMenu.add(new JSeparator());
                for(int i = 0; i < pubFieldColumn.size(); i++)
                {
                    HashMap lcSearchCondition = new HashMap();
                    filterArgument = DynaMOAD.nds.getArgument("::/WORKSPACE/" + modelOuid + "/PUBLIC/FILTER/" + classOuid + "/" + pubFieldColumn.get(i), "isview");
                    if(!Utils.isNullString(filterArgument) && !filterArgument.equals("F"))
                    {
                        openStructureMenuItem = new JMenuItem((String)pubFieldColumn.get(i));
                        openStructureMenuItem.setIcon(new ImageIcon("icons/TreeView.gif"));
                        openStructureMenuItem.setActionCommand("tree view/" + pubFieldColumn.get(i));
                        openStructureMenuItem.addActionListener(actionListener);
                        openStructureMenu.add(openStructureMenuItem);
                        openStructureMenuItem = null;
                    }
                }

            }
        }
        JFrame frame = (JFrame)JOptionPane.getFrameForComponent(comp);
        Dimension framesize = frame.getSize();
        Dimension popupsize = popup.getSize();
        if(popupsize.width <= 0 || popupsize.height <= 0)
            popupsize = new Dimension(107, 100);
        Point point = SwingUtilities.convertPoint(comp, x, y, frame);
        if(popupsize.width + point.x >= framesize.width)
            x -= popupsize.width;
        if(popupsize.height + point.y >= framesize.height)
            y -= popupsize.height;
        popup.show(comp, x, y);
        return popup;
    }

    public static JPopupMenu createPopupMenuForStructureTree(String ouid, String ouid2, ArrayList ouidList, ArrayList assoList, Component comp, int x, int y, ActionListener actionListener, 
            String modelOuid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid2) || Utils.isNullArrayList(ouidList) || comp == null || actionListener == null)
            return null;
        if(ouid == null)
            ouid = "";
        JPopupMenu popup = null;
        JMenu newMenu = null;
        JMenuItem addNewMenuItem = null;
        JMenuItem newMenuItem = null;
        JMenuItem makeWIPMenuItem = null;
        JMenu openStructureMenu = null;
        JMenuItem openStructureMenuItem = null;
        JMenuItem addSubMenuItem = null;
        JMenuItem replaceLinkMenuItem = null;
        JMenuItem deleteLinkMenuItem = null;
        JMenu expandMenu = null;
        JMenuItem expand1MenuItem = null;
        JMenuItem expandLevelMenuItem = null;
        JMenuItem expandFullMenuItem = null;
        JMenuItem openMenuItem = null;
        JMenu fileMenu = null;
        JMenuItem cutLinkMenuItem = null;
        JMenuItem copyMenuItem = null;
        JMenuItem pasteLinkMenuItem = null;
        JMenu sendMenu = null;
        JMenuItem myFolderMenuItem = null;
        JMenu processMenu = null;
        JMenu compareMenu = null;
        JMenuItem authorityMenuItem = null;
        String classOuid = DynaMOAD.dos.getClassOuid(ouid);
        String classOuid2 = DynaMOAD.dos.getClassOuid(ouid2);
        DOSChangeable classDesc = DynaMOAD.dos.getClass(classOuid2);
        popup = new JPopupMenu();
        newMenu = new JMenu(DynaMOAD.getMSRString("MNU_NEW", "New", 3));
        newMenu.setMnemonic(78);
        newMenu.setIcon(new ImageIcon("icons/Registry.gif"));
        popup.add(newMenu);
        addNewMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_ADD_NEW", "Add to this Object", 3));
        addNewMenuItem.setActionCommand("MNU_ADD_NEW");
        addNewMenuItem.addActionListener(actionListener);
        newMenu.add(addNewMenuItem);
        newMenu.add(new JSeparator());
        newMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_ADD_NEW_THIS", "Add as a this Object", 3));
        newMenuItem.setActionCommand("MNU_NEW_THIS^" + ouid2 + "^" + ouid);
        newMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        newMenu.add(newMenuItem);
        popup.add(new JSeparator());
        if(!Utils.isNullArrayList(assoList))
        {
            openStructureMenu = new JMenu(DynaMOAD.getMSRString("MNU_OPEN_STRUCTURE", "Structure", 3));
            openStructureMenu.setIcon(new ImageIcon("icons/TreeView.gif"));
            popup.add(openStructureMenu);
        }
        expandMenu = new JMenu(DynaMOAD.getMSRString("MNU_EXPAND", "Expand", 3));
        expandMenu.setMnemonic(65);
        popup.add(expandMenu);
        expand1MenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_EXPAND_ONE", "1 Level", 3));
        expand1MenuItem.setMnemonic(49);
        expand1MenuItem.setActionCommand("MNU_EXPAND_ONE_LINK");
        expand1MenuItem.addActionListener(actionListener);
        expandMenu.add(expand1MenuItem);
        expandMenu.add(new JSeparator());
        expandLevelMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_EXPAND_LEVEL", "Level...", 3));
        expandLevelMenuItem.setActionCommand("MNU_EXPAND_LEVEL_LINK");
        expandLevelMenuItem.addActionListener(actionListener);
        expandMenu.add(expandLevelMenuItem);
        expandFullMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_EXPAND_FULL", "Full Level", 3));
        expandFullMenuItem.setMnemonic(70);
        expandFullMenuItem.setActionCommand("MNU_EXPAND_FULL_LINK");
        expandFullMenuItem.addActionListener(actionListener);
        expandMenu.add(expandFullMenuItem);
        popup.add(new JSeparator());
        openMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3));
        openMenuItem.setMnemonic(79);
        openMenuItem.setIcon(new ImageIcon("icons/Open.gif"));
        openMenuItem.setActionCommand("InfoInLink");
        openMenuItem.addActionListener(actionListener);
        popup.add(openMenuItem);
        fileMenu = new JMenu(DynaMOAD.getMSRString("MNU_FILE", "File", 3));
        fileMenu.setMnemonic(70);
        createFilePopupMenuItems(fileMenu, ouidList, DynaMOAD.dos, DynaMOAD.acl, actionListener, null);
        if(fileMenu.isEnabled())
            popup.add(fileMenu);
        else
            fileMenu = null;
        popup.add(new JSeparator());
        copyMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_COPY", "Copy", 3));
        copyMenuItem.setMnemonic(67);
        copyMenuItem.setActionCommand("copyLink.object");
        copyMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyMenuItem.addActionListener(actionListener);
        popup.add(copyMenuItem);
        pasteLinkMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_PASTE_LINK", "Paste Link", 3));
        pasteLinkMenuItem.setMnemonic(80);
        pasteLinkMenuItem.setActionCommand("pasteLink.object");
        pasteLinkMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteLinkMenuItem.addActionListener(actionListener);
        if(Utils.isClipboardContainDOSObjects(actionListener))
            popup.add(pasteLinkMenuItem);
        popup.add(new JSeparator());
        sendMenu = new JMenu(DynaMOAD.getMSRString("MNU_SEND", "Send", 3));
        popup.add(sendMenu);
        myFolderMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_SEND_TO_MYFOLDER", "My Folder", 3));
        myFolderMenuItem.setActionCommand("Favorite^" + ouid2);
        myFolderMenuItem.setIcon(new ImageIcon("icons/Favorite.gif"));
        myFolderMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        sendMenu.add(myFolderMenuItem);
        processMenu = new JMenu(DynaMOAD.getMSRString("WRD_0058", "Create a Process", 3));
        createProcessPopupMenuItems(processMenu, ouidList, DynaMOAD.dos, DynaMOAD.wfm, DynaMOAD.acl, actionListener);
        if(processMenu.isEnabled())
            popup.add(processMenu);
        else
            processMenu = null;
        createActionMenu(popup, ouid2, actionListener);
        popup.add(new JSeparator());
        compareMenu = new JMenu(DynaMOAD.getMSRString("MNU_COMPARE", "Compare with", 3));
        popup.add(compareMenu);
        createComparePopupMenuItems(compareMenu, ouid2, ouidList, classDesc);
        popup.add(new JSeparator());
        authorityMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0051", "Authority", 3));
        authorityMenuItem.setActionCommand("SetAuthority^" + ouid2);
        authorityMenuItem.setIcon(new ImageIcon("icons/Acl.gif"));
        authorityMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        popup.add(authorityMenuItem);
        if(LogIn.isAdmin)
        {
            authorityMenuItem.setEnabled(true);
        } else
        {
            ArrayList aclList = DynaMOAD.acl.retrievePrivateACL4Grant(classOuid, ouid, LogIn.userID, new Boolean(LogIn.isAdmin));
            if(Utils.isNullArrayList(aclList))
                authorityMenuItem.setEnabled(false);
            else
                authorityMenuItem.setEnabled(true);
        }
        if(openStructureMenu != null)
        {
            ArrayList listAsso = null;
            if(!Utils.isNullString(ouid))
            {
                ArrayList superClassList1 = DynaMOAD.dos.listAllSuperClassOuid(classOuid);
                if(superClassList1 == null)
                    superClassList1 = new ArrayList();
                superClassList1.add(0, classOuid);
                ArrayList superClassList2 = DynaMOAD.dos.listAllSuperClassOuid(classOuid2);
                if(superClassList2 == null)
                    superClassList2 = new ArrayList();
                superClassList2.add(0, classOuid2);
                DOSChangeable dosAssociation = null;
                listAsso = (ArrayList)assoList.clone();
                Iterator listKey;
                for(listKey = listAsso.iterator(); listKey.hasNext(); dosAssociation = null)
                {
                    dosAssociation = (DOSChangeable)listKey.next();
                    if(!superClassList1.contains((String)dosAssociation.get("end1.ouid@class")) || !superClassList2.contains((String)dosAssociation.get("end2.ouid@class")) || Utils.isNullString((String)dosAssociation.get("ouid@class")))
                        continue;
                    openStructureMenuItem = new JMenuItem((String)dosAssociation.get("description"));
                    openStructureMenuItem.setActionCommand("AssoInfo");
                    openStructureMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/LinkDetail.gif")));
                    openStructureMenuItem.addActionListener(actionListener);
                    openStructureMenu.add(openStructureMenuItem);
                    break;
                }

                listKey = null;
                if(openStructureMenu.getMenuComponentCount() > 0)
                {
                    copyMenuItem.setActionCommand("copyLink.object^assoclass");
                    openStructureMenu.add(new JSeparator());
                    dosAssociation = null;
                }
                addSubMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_ADD_SUB", "Add to this object...", 3));
                addSubMenuItem.setActionCommand("Link");
                addSubMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/add_att.gif")));
                addSubMenuItem.addActionListener(actionListener);
                openStructureMenu.add(addSubMenuItem);
                deleteLinkMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_UNLINK", "Delete link", 3));
                deleteLinkMenuItem.setActionCommand("Unlink");
                deleteLinkMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/remove_att.gif")));
                deleteLinkMenuItem.addActionListener(actionListener);
                openStructureMenu.add(deleteLinkMenuItem);
            }
        }
        if(ouidList.size() == 1)
        {
            String statusId = DynaMOAD.dos.getStatus(ouid);
            String statusId2 = DynaMOAD.dos.getStatus(ouid2);
            if(classDesc != null && statusId2.equals("RLS") && Utils.getBoolean((Boolean)classDesc.get("is.versionable")))
            {
                newMenu.add(new JSeparator());
                makeWIPMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0059", "Make a WIP Version", 3));
                makeWIPMenuItem.setActionCommand("makeWIPVersion^" + ouid2);
                makeWIPMenuItem.setIcon(new ImageIcon("icons/MakeWIP.gif"));
                makeWIPMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
                newMenu.add(makeWIPMenuItem);
            }
            boolean chk = false;
            boolean chk2 = false;
            if("CRT".equals(statusId) || "WIP".equals(statusId) || "REJ".equals(statusId))
                if(LogIn.isAdmin)
                    chk = true;
                else
                    chk = DynaMOAD.acl.hasPermission(classOuid, ouid, "LINK / UNLINK", LogIn.userID);
            newMenu.setEnabled(chk);
            pasteLinkMenuItem.setEnabled(chk);
            deleteLinkMenuItem.setEnabled(chk);
            if(addSubMenuItem != null)
                addSubMenuItem.setEnabled(chk);
            if(deleteLinkMenuItem != null)
                deleteLinkMenuItem.setEnabled(chk);
            if(newMenu.isEnabled())
            {
                chk2 = chk && DynaMOAD.acl.hasPermission(classOuid2, ouid2, "CREATE", LogIn.userID);
                addNewMenuItem.setEnabled(chk2);
                newMenuItem.setEnabled(chk2);
                if(makeWIPMenuItem != null)
                {
                    chk2 = chk && DynaMOAD.acl.hasPermission(classOuid2, ouid2, "UPDATE", LogIn.userID);
                    makeWIPMenuItem.setEnabled(chk2);
                }
            }
            if(LogIn.isAdmin)
                chk = true;
            else
                chk = DynaMOAD.acl.hasPermission(classOuid2, ouid2, "READ", LogIn.userID);
            openMenuItem.setEnabled(chk);
        }
        JFrame frame = (JFrame)JOptionPane.getFrameForComponent(comp);
        Dimension framesize = frame.getSize();
        Dimension popupsize = popup.getSize();
        if(popupsize.width <= 0 || popupsize.height <= 0)
            popupsize = new Dimension(107, 100);
        Point point = SwingUtilities.convertPoint(comp, x, y, frame);
        if(popupsize.width + point.x >= framesize.width)
            x -= popupsize.width;
        if(popupsize.height + point.y >= framesize.height)
            y -= popupsize.height;
        popup.show(comp, x, y);
        return popup;
    }

    public static JPopupMenu createPopupMenuForAssociationTable(String ouid, String ouid2, ArrayList ouidList, ArrayList assoList, Component comp, int x, int y, ActionListener actionListener, 
            String modelOuid, String assoOuid)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid) || Utils.isNullArrayList(ouidList) || comp == null || actionListener == null)
            return null;
        JPopupMenu popup = null;
        JMenu newMenu = null;
        JMenuItem addNewMenuItem = null;
        JMenuItem newMenuItem = null;
        JMenuItem makeWIPMenuItem = null;
        JMenuItem openMenuItem = null;
        JMenu fileMenu = null;
        JMenuItem copyMenuItem = null;
        JMenuItem pasteLinkMenuItem = null;
        JMenuItem deleteLinkMenuItem = null;
        JMenu sendMenu = null;
        JMenuItem myFolderMenuItem = null;
        JMenu processMenu = null;
        JMenu compareMenu = null;
        JMenuItem authorityMenuItem = null;
        String classOuid = DynaMOAD.dos.getClassOuid(ouid);
        if(Utils.isNullString(classOuid))
            return null;
        DOSChangeable classDesc = DynaMOAD.dos.getClass(classOuid);
        String classOuid2 = DynaMOAD.dos.getClassOuid(ouid2);
        if(Utils.isNullString(classOuid2))
            return null;
        popup = new JPopupMenu();
        newMenu = new JMenu(DynaMOAD.getMSRString("MNU_NEW", "New", 3));
        newMenu.setMnemonic(78);
        newMenu.setIcon(new ImageIcon("icons/Registry.gif"));
        popup.add(newMenu);
        addNewMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_ADD_NEW", "Add to this Object", 3));
        addNewMenuItem.setActionCommand("MNU_ADD_NEW_ASSO^" + assoOuid);
        addNewMenuItem.addActionListener(actionListener);
        newMenu.add(addNewMenuItem);
        newMenu.add(new JSeparator());
        newMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_ADD_NEW_THIS", "Add as a this Object", 3));
        newMenuItem.setActionCommand("MNU_NEW_THIS^" + ouid2 + "^" + ouid);
        newMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        newMenu.add(newMenuItem);
        popup.add(new JSeparator());
        openMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_OPEN", "Open", 3));
        openMenuItem.setMnemonic(79);
        openMenuItem.setIcon(new ImageIcon("icons/Open.gif"));
        openMenuItem.setActionCommand("assoDetail/" + assoOuid);
        openMenuItem.addActionListener(actionListener);
        popup.add(openMenuItem);
        fileMenu = new JMenu(DynaMOAD.getMSRString("MNU_FILE", "File", 3));
        fileMenu.setMnemonic(70);
        createFilePopupMenuItems(fileMenu, ouidList, DynaMOAD.dos, DynaMOAD.acl, actionListener, assoOuid);
        if(fileMenu.isEnabled())
            popup.add(fileMenu);
        else
            fileMenu = null;
        popup.add(new JSeparator());
        copyMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0009", "Copy", 3));
        copyMenuItem.setActionCommand("assoCopy/" + assoOuid);
        copyMenuItem.setIcon(new ImageIcon("icons/Copy.gif"));
        copyMenuItem.addActionListener(actionListener);
        popup.add(copyMenuItem);
        pasteLinkMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0010", "Paste", 3));
        pasteLinkMenuItem.setMnemonic(80);
        pasteLinkMenuItem.setActionCommand("assoPaste/" + assoOuid);
        pasteLinkMenuItem.setIcon(new ImageIcon("icons/Paste.gif"));
        pasteLinkMenuItem.addActionListener(actionListener);
        if(Utils.isClipboardContainDOSObjects(actionListener))
            popup.add(pasteLinkMenuItem);
        deleteLinkMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0002", "Remove object link", 3));
        deleteLinkMenuItem.setActionCommand("assoRemove/" + assoOuid);
        deleteLinkMenuItem.setIcon(new ImageIcon(java.lang.Object.class.getResource("/icons/remove_att.gif")));
        deleteLinkMenuItem.addActionListener(actionListener);
        popup.add(deleteLinkMenuItem);
        popup.add(new JSeparator());
        sendMenu = new JMenu(DynaMOAD.getMSRString("MNU_SEND", "Send", 3));
        popup.add(sendMenu);
        myFolderMenuItem = new JMenuItem(DynaMOAD.getMSRString("MNU_SEND_TO_MYFOLDER", "My Folder", 3));
        myFolderMenuItem.setActionCommand("Favorite^" + ouid);
        myFolderMenuItem.setIcon(new ImageIcon("icons/Favorite.gif"));
        myFolderMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        sendMenu.add(myFolderMenuItem);
        processMenu = new JMenu(DynaMOAD.getMSRString("WRD_0058", "Create a Process", 3));
        createProcessPopupMenuItems(processMenu, ouidList, DynaMOAD.dos, DynaMOAD.wfm, DynaMOAD.acl, actionListener);
        if(processMenu.isEnabled())
            popup.add(processMenu);
        else
            processMenu = null;
        createActionMenu(popup, ouid, actionListener);
        popup.add(new JSeparator());
        compareMenu = new JMenu(DynaMOAD.getMSRString("MNU_COMPARE", "Compare with", 3));
        popup.add(compareMenu);
        createComparePopupMenuItems(compareMenu, ouid, ouidList, classDesc);
        popup.add(new JSeparator());
        authorityMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0051", "Authority", 3));
        authorityMenuItem.setActionCommand("SetAuthority^" + ouid);
        authorityMenuItem.setIcon(new ImageIcon("icons/Acl.gif"));
        authorityMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
        popup.add(authorityMenuItem);
        if(LogIn.isAdmin)
        {
            authorityMenuItem.setEnabled(true);
        } else
        {
            ArrayList aclList = DynaMOAD.acl.retrievePrivateACL4Grant(classOuid, ouid, LogIn.userID, new Boolean(LogIn.isAdmin));
            if(Utils.isNullArrayList(aclList))
                authorityMenuItem.setEnabled(false);
            else
                authorityMenuItem.setEnabled(true);
        }
        if(ouidList.size() == 1)
        {
            String statusId = DynaMOAD.dos.getStatus(ouid);
            String statusId2 = DynaMOAD.dos.getStatus(ouid2);
            if(classDesc != null && statusId2.equals("RLS") && Utils.getBoolean((Boolean)classDesc.get("is.versionable")))
            {
                newMenu.add(new JSeparator());
                makeWIPMenuItem = new JMenuItem(DynaMOAD.getMSRString("WRD_0059", "Make a WIP Version", 3));
                makeWIPMenuItem.setActionCommand("makeWIPVersion^" + ouid2);
                makeWIPMenuItem.setIcon(new ImageIcon("icons/MakeWIP.gif"));
                makeWIPMenuItem.addActionListener(ListenerBuilder.getPublicActionListner());
                newMenu.add(makeWIPMenuItem);
            }
            boolean chk = false;
            boolean chk2 = false;
            if("CRT".equals(statusId) || "WIP".equals(statusId) || "REJ".equals(statusId))
                if(LogIn.isAdmin)
                    chk = true;
                else
                    chk = DynaMOAD.acl.hasPermission(classOuid, ouid, "LINK / UNLINK", LogIn.userID);
            newMenu.setEnabled(chk);
            pasteLinkMenuItem.setEnabled(chk);
            deleteLinkMenuItem.setEnabled(chk);
            if(newMenu.isEnabled())
            {
                chk2 = chk && DynaMOAD.acl.hasPermission(classOuid2, ouid2, "CREATE", LogIn.userID);
                addNewMenuItem.setEnabled(chk2);
                newMenuItem.setEnabled(chk2);
                if(makeWIPMenuItem != null)
                {
                    chk2 = chk && DynaMOAD.acl.hasPermission(classOuid2, ouid2, "UPDATE", LogIn.userID);
                    makeWIPMenuItem.setEnabled(chk2);
                }
            }
            if(LogIn.isAdmin)
                chk = true;
            else
                chk = DynaMOAD.acl.hasPermission(classOuid2, ouid2, "READ", LogIn.userID);
            openMenuItem.setEnabled(chk);
        }
        JFrame frame = (JFrame)JOptionPane.getFrameForComponent(comp);
        Dimension framesize = frame.getSize();
        Dimension popupsize = popup.getSize();
        if(popupsize.width <= 0 || popupsize.height <= 0)
            popupsize = new Dimension(107, 100);
        Point point = SwingUtilities.convertPoint(comp, x, y, frame);
        if(popupsize.width + point.x >= framesize.width)
            x -= popupsize.width;
        if(popupsize.height + point.y >= framesize.height)
            y -= popupsize.height;
        popup.show(comp, x, y);
        return popup;
    }

    public static UIGeneration displayInstanceWindow(String ouid, DOS dos, DSS dss)
        throws IIPRequestException
    {
        if(Utils.isNullString(ouid))
            return null;
        String classOuid = dos.getClassOuid(ouid);
        if(Utils.isNullString(ouid))
            return null;
        UIGeneration uiGeneration = new UIGeneration(null, classOuid, ouid, 1);
        Object returnObject = Utils.executeScriptFile(dos.getEventName(classOuid, "read.before"), dss, uiGeneration);
        if((returnObject instanceof Boolean) && !Utils.getBoolean((Boolean)returnObject))
        {
            uiGeneration.windowClosing(null);
            uiGeneration = null;
            return null;
        } else
        {
            uiGeneration.setVisible(true);
            Utils.executeScriptFile(dos.getEventName(classOuid, "read.after"), dss, uiGeneration);
            return uiGeneration;
        }
    }

    public static UIGeneration displayAddInstanceWindow(String classOuid)
    {
        UIGeneration uiGeneration = new UIGeneration(null, classOuid);
        return uiGeneration;
    }

    public static UIGeneration displayAddInstanceWindowAndWait(String classOuid, Object obj)
    {
        UIGeneration ui = new UIGeneration(null, classOuid);
        try
        {
            obj.wait(200L);
            for(; ui.isVisible(); obj.wait(200L));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ui;
    }

    private static final int LOCATION_RESULT = 110;
    private static final int DEFAULT_TITLEWIDTH = 80;
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 22;
    private static final int offset = 4;
    private static final int init_xcord = 10;
    private static final int init_ycord = 10;
    public static Cursor handCursor = new Cursor(12);
    private static HeaderRenderer defaultHeadRenderer = null;

}
