/*
 * Created on 2004-11-9
 *
 */
package dyna.framework.client.dfempdm;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import bsh.Interpreter;

/**
 * @author 李渊
 *
 */
public class Formulae extends JDialog {
    public static final String opRegex = "[\\+\\-\\*/\\(\\)]";
    
    private HashMap varMap = new HashMap();
    private Double rationResult = null;
    private Double roughWeightResult = null;
    public int userChoice = JOptionPane.CANCEL_OPTION;

	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel paneData = null;
	private JPanel jPanel3 = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JLabel lblFormula = null;
	private JLabel lblFormulaDesc = null;
	private JScrollPane jScrollPane = null;
	/**
	 * This method initializes 
	 * 
	 */
	public Formulae(Dialog owner, boolean modal) {
		super(owner, modal);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setContentPane(getJScrollPane());
        this.setTitle("公式计算");
        this.setSize(278, 281);
			
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			jPanel.setBackground(new java.awt.Color(223,216,206));
			jPanel.add(getJPanel1(), java.awt.BorderLayout.NORTH);
			jPanel.add(getPaneData(), java.awt.BorderLayout.CENTER);
			jPanel.add(getJPanel3(), java.awt.BorderLayout.SOUTH);
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			lblFormulaDesc = new JLabel();
			lblFormula = new JLabel();
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
			lblFormula.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,5,0));
			lblFormulaDesc.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,5,0));
			jPanel1.setBackground(new java.awt.Color(223,216,206));
			jPanel1.add(lblFormulaDesc, null);
			jPanel1.add(lblFormula, null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getPaneData() {
		if (paneData == null) {
			paneData = new JPanel();
			paneData.setLayout(new GridBagLayout());
			paneData.setBackground(new java.awt.Color(223,216,206));
		}
		return paneData;
	}
	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			FlowLayout flowLayout24 = new FlowLayout();
			jPanel3 = new JPanel();
			jPanel3.setLayout(flowLayout24);
			flowLayout24.setAlignment(java.awt.FlowLayout.RIGHT);
			jPanel3.setBackground(new java.awt.Color(223,216,206));
			jPanel3.add(getBtnOk(), null);
			jPanel3.add(getBtnCancel(), null);
		}
		return jPanel3;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText("计算");
			btnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String rationEvalString = lblFormula.getText();
                    String roughWeightEvalString = rationEvalString.replaceAll(
                            "系数", "1"); //XXX: if the quotiety is always been 
                                         // used with * or /, it's ok
                    					 // otherwise we need a more smart way...
                    
                    String msg = "";
                    
                    Iterator i = varMap.keySet().iterator();
                    for (; i.hasNext();) {
                        String varName = (String)i.next();
                        try {
                            JTextField tmpField = (JTextField)varMap.get(varName);
                            Double value = new Double(tmpField.getText());
                            
                            rationEvalString = rationEvalString.replaceAll(varName, value.toString());
                            roughWeightEvalString = roughWeightEvalString.replaceAll(varName, value.toString());
                        } catch (Exception e1) {
                            msg += varName + ", ";
                        }
                    }
                    
                    if (msg.length() > 0) {
                        JOptionPane.showMessageDialog(Formulae.this,
                                "下列数值填写有误: \n  " + msg
                                        + "\n请检查是否填写了有效的双精度浮点型值.",
                                "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    Interpreter interp = new Interpreter(); 
            		try {
            			Object result = interp.eval(rationEvalString);
            			rationResult = Double.valueOf(result.toString());
            			
            			result = interp.eval(roughWeightEvalString);
            			roughWeightResult = Double.valueOf(result.toString());
            		} catch(Exception e1) {
            			JOptionPane.showMessageDialog(Formulae.this, "计算出现错误, 请检查公式是否正确.");
            			return;
            		}

                    userChoice = JOptionPane.OK_OPTION;
                    Formulae.this.dispose();
                }
			});
		}
		return btnOk;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText("取消");
			btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Formulae.this.dispose();
                }
			});
		}
		return btnCancel;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJPanel());
		}
		return jScrollPane;
	}
	
	public boolean setFormula(String fml) {
	    if (fml == null)
	        return false;
	    
	    String [] pieces = fml.split(opRegex);
	    if (pieces.length < 1)
	        return false;
	    
	    lblFormula.setText(fml);
	    varMap.clear();
	    
	    for (int i = 0; i < pieces.length; i++) {
	        try {
	            new Double(pieces[i]);
	            continue;
	        } catch (Exception e) {
	            // do nothing
	        }
	        
	        if (!pieces[i].equals(""))
	            AddVariable(pieces[i]);
	    }
	    
	    return true;
	}

	private void AddVariable(String varName) {
	    if (varMap.containsKey(varName))
	        return; // 过滤重复的
	    
	    int row = varMap.size() + 1;
	    
	    // Build UI
		GridBagConstraints gbcText = new GridBagConstraints();
		gbcText.gridx = 0;
		gbcText.gridy = row;
		gbcText.anchor = java.awt.GridBagConstraints.EAST;
		gbcText.insets = new java.awt.Insets(0,0,5,0);

		GridBagConstraints gbcValue = new GridBagConstraints();
		gbcValue.gridx = 1;
		gbcValue.gridy = row;
		gbcValue.weightx = 1.0;
		gbcValue.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbcValue.insets = new java.awt.Insets(0,5,5,0);

		paneData.add(new JLabel(varName), gbcText);
		JTextField tmpField = new JTextField();
		paneData.add(tmpField, gbcValue);
		
		// Add to varMap
		varMap.put(varName, tmpField);
	}
	
	public void setDescription(String desc) {
	    if (desc != null)
	        lblFormulaDesc.setText(desc + ":");
	}
	
	public String getValue(String varName) {
	    JTextField tmpField = (JTextField)varMap.get(varName);
	    if (tmpField == null)
	        return null;
	    
	    return tmpField.getText();
	}
	
	public boolean setValue(String varName, String newValue) {
	    JTextField tmpField = (JTextField)varMap.get(varName);
	    if (tmpField == null)
	        return false;
	    
	    tmpField.setText(newValue);
	    return true;
	}
	
	public double getRationResult() {
	    return rationResult.doubleValue();
	}

	public double getRoughWeightResult() {
	    return roughWeightResult.doubleValue();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
