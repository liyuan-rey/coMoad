/*
 * Created on 2004-11-3
 *
 */
package dyna.framework.client.dfempdm;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import dyna.framework.service.dos.DOSChangeable;


class CodeSelectDialog extends JDialog {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private CodeTree codeTree = null;
    private JPanel bottomPanel = new JPanel();
    private JButton btnOk = new JButton();
    private JButton btnCancel = new JButton();
    private int choice = JOptionPane.CANCEL_OPTION;
    
    public CodeSelectDialog(Frame owner, String title, boolean modal, CodeTree tree)
            throws HeadlessException {
        super(owner, title, modal);
        
        if (tree == null)
            throw new IllegalArgumentException("Invalid args - tree");
        
        this.codeTree = tree;
        init();
    }
    
    private void init() {
	    btnOk.setText("选择");
	    btnOk.setIcon(new ImageIcon(getClass().getResource("/icons/Select.gif")));
	    btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DOSChangeable dosObject = codeTree.getSelectedCodeItem();
                if (dosObject == null) {
                    JOptionPane.showMessageDialog(CodeSelectDialog.this, "请先选择需要保存为模板的工序.");
                    return;
                }
                
                CodeSelectDialog.this.choice = JOptionPane.OK_OPTION;
                CodeSelectDialog.this.dispose();
            }
	    });
	    
	    btnCancel.setText("关闭");
	    btnCancel.setIcon(new ImageIcon(getClass().getResource("/icons/Cancel.gif")));
	    btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CodeSelectDialog.this.choice = JOptionPane.CANCEL_OPTION;
                CodeSelectDialog.this.dispose();
            }
	    });
	    
	    bottomPanel.add(btnOk);
	    bottomPanel.add(btnCancel);
	    
	    JLabel label = new JLabel("请选择要另存为的模板类别:");
	    label.setBorder(new EmptyBorder(0,0,3,0));
	    
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setViewportView(codeTree);
	    
	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    panel.add(bottomPanel, BorderLayout.SOUTH);
	    panel.setBorder(new EmptyBorder(5,5,5,5));
	    
	    setSize(270, 380);
	    setContentPane(panel);
    }
    
    public int getChoice() {
        return choice;
    }
}