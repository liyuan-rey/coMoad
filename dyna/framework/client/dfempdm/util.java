/*
 * Created on 2004-11-10
 *
 */
package dyna.framework.client.dfempdm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import dyna.framework.service.dos.DOSChangeable;

/**
 * @author ภ๎ิจ
 *
 */
public class util {

    public static void centerWindow(Component alternateOwner, Component centerComp) {
        Dimension ownerSize = (alternateOwner == null ?
                Toolkit.getDefaultToolkit().getScreenSize() :
                    alternateOwner.getSize());
        Dimension centerSize = centerComp.getSize();
        
        if (centerSize.height > ownerSize.height) {
            centerSize.height = ownerSize.height;
        }
        
        if (centerSize.width > ownerSize.width) {
            centerSize.width = ownerSize.width;
        }
        
        centerComp.setLocation((ownerSize.width - centerSize.width) / 2,
                (ownerSize.height - centerSize.height) / 2);
    }

    public static void refreshCodeComboBox(String codeName, JComboBox combo,
            String selItemId) throws Exception {
        combo.removeAllItems();
    
        DOSChangeable dosSqType = dyna.framework.client.DynaMOAD.dos.getCodeWithName(codeName);
        if (dosSqType != null) {
            String ouid = (String)dosSqType.get("ouid");
            ArrayList codeList = dyna.framework.client.DynaMOAD.dos.listCodeItem(ouid);
            
            boolean matched = false;
            int size = codeList.size();
            for (int i = 0; i < size; i++) {
                DOSChangeable codeItem = (DOSChangeable)codeList.get(i);
                if (codeItem == null)
                    continue;
                
                combo.addItem(new DOSObjectAdapter(codeItem, "%name% [%codeitemid%]"));
                
                if (selItemId != null && !selItemId.equals("")) {
                    if (selItemId.equals(codeItem.get("ouid"))) {
                        combo.setSelectedIndex(combo.getItemCount()-1);
                        matched = true;
                    }
                }
            }
            
            if (!matched)
                combo.setSelectedIndex(-1);
        }
    }

    public static JDialog createInformationDialog(JFrame parent, String title, String msg) {
        JDialog dialog = new JDialog(parent, title, true);
        
        JTextArea content = new JTextArea(msg);
        content.setEditable(false);
    
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(content);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        dialog.setContentPane(panel);
        
        dialog.setSize(330, 240);
        centerWindow(null, dialog);
        
        return dialog;
    }

    public static void syncComboBoxWithValue(JComboBox combo, Object value) {
        if (combo == null || value == null)
            return;
        
        int count = combo.getItemCount();
        for (int i = 0; i < count; i++) {
            if (combo.getItemAt(i).toString().equals(value.toString())) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

}
