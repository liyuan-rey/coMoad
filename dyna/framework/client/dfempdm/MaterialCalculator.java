/*
 * Created on 2004-11-9
 *
 */
package dyna.framework.client.dfempdm;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import dyna.framework.service.DOS;
import dyna.framework.service.dos.DOSChangeable;
/**
 * @author 李渊
 *
 */
public class MaterialCalculator extends JDialog {
    public int choice = JOptionPane.CANCEL_OPTION;
    private static DOS dos = dyna.framework.client.DynaMOAD.dos;
    private DOSChangeable contextObj = null;
    private static final String remarkTempl = "其中, 夹头 %j 压头 %y 每 %m 件一个夹头或压头";
    
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JTextField txtMaterialDim = null;
	private JLabel jLabel1 = null;
	private JTextField txtRoughQty = null;
	private JLabel jLabel2 = null;
	private JTextField txtWorkshopRouting = null;
	private JLabel jLabel3 = null;
	private JCheckBox chkShowAllFormulae = null;
	private JLabel jLabel4 = null;
	private JComboBox cbxRoughUom = null;
	private JLabel jLabel5 = null;
	private JComboBox cbxMaterialUom = null;
	private JLabel jLabel6 = null;
	private JTextField txtMakePartNum = null;
	private JPanel jPanel1 = null;
	private JLabel jLabel7 = null;
	private JTextField txtColletNum = null;
	private JLabel jLabel8 = null;
	private JTextField txtBallastNum = null;
	private JLabel jLabel9 = null;
	private JTextField txtEveryRange = null;
	private JLabel jLabel10 = null;
	private JComboBox cbxFormulae = null;
	private JLabel jLabel11 = null;
	private JPanel jPanel2 = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private JTextField txtRation = null;
	private JLabel jLabel12 = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel13 = null;
	private JTextField txtRoughtWeight = null;
	/**
	 * This method initializes 
	 * 
	 */
	public MaterialCalculator(Frame owner, boolean modal) {
		super(owner, modal);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJScrollPane());
        this.setTitle("材料定额计算");
        this.setSize(390, 330);
			
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel13 = new JLabel();
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			jLabel12 = new JLabel();
			jLabel11 = new JLabel();
			jLabel6 = new JLabel();
			jLabel5 = new JLabel();
			jLabel4 = new JLabel();
			jLabel3 = new JLabel();
			jLabel2 = new JLabel();
			jLabel1 = new JLabel();
			jLabel = new JLabel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 5;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints1.insets = new java.awt.Insets(0,0,5,0);
			jLabel.setText("毛坯下料尺寸");
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 5;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 5;
			gridBagConstraints3.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			jLabel1.setText("毛坯数量");
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.gridy = 5;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.insets = new java.awt.Insets(0,5,5,0);
			jPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
			jPanel.setBackground(new java.awt.Color(223,216,206));
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,5,0);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			jLabel2.setText("分厂工艺路线");
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(0,0,5,0);
			jLabel3.setText("计算公式");
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 7;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints10.insets = new java.awt.Insets(0,0,5,0);
			jLabel4.setText("毛坯单位");
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 7;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.gridy = 7;
			gridBagConstraints12.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
			jLabel5.setText("材料单位");
			gridBagConstraints13.gridx = 3;
			gridBagConstraints13.gridy = 7;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.gridy = 6;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints14.insets = new java.awt.Insets(0,5,5,0);
			jLabel6.setText("加工零件数量");
			gridBagConstraints15.gridx = 3;
			gridBagConstraints15.gridy = 6;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 8;
			gridBagConstraints16.gridwidth = 4;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.gridy = 4;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridwidth = 4;
			gridBagConstraints17.insets = new java.awt.Insets(0,0,5,0);
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.gridy = 9;
			gridBagConstraints18.gridwidth = 4;
			gridBagConstraints18.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.insets = new java.awt.Insets(0,0,5,0);
			jLabel11.setText("");
			jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
			gridBagConstraints19.gridx = 2;
			gridBagConstraints19.gridy = 12;
			gridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.gridwidth = 2;
			jPanel.add(getJPanel2(), gridBagConstraints19);
			gridBagConstraints22.gridx = 1;
			gridBagConstraints22.gridy = 11;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.insets = new java.awt.Insets(0,5,5,0);
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.gridy = 11;
			gridBagConstraints23.insets = new java.awt.Insets(0,0,5,0);
			gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
			jLabel12.setText("定额");
			gridBagConstraints110.gridx = 0;
			gridBagConstraints110.gridy = 6;
			gridBagConstraints110.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints110.insets = new java.awt.Insets(0,0,5,0);
			jLabel13.setText("毛坯重量");
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.gridy = 6;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.insets = new java.awt.Insets(0,5,5,0);
			jPanel.add(jLabel, gridBagConstraints1);
			jPanel.add(getTxtMaterialDim(), gridBagConstraints2);
			jPanel.add(jLabel1, gridBagConstraints3);
			jPanel.add(getTxtRoughQty(), gridBagConstraints4);
			jPanel.add(jLabel2, gridBagConstraints5);
			jPanel.add(getTxtWorkshopRouting(), gridBagConstraints6);
			jPanel.add(jLabel3, gridBagConstraints7);
			jPanel.add(getChkShowAllFormulae(), gridBagConstraints9);
			jPanel.add(jLabel4, gridBagConstraints10);
			jPanel.add(getCbxRoughUom(), gridBagConstraints11);
			jPanel.add(jLabel5, gridBagConstraints12);
			jPanel.add(getCbxMaterialUom(), gridBagConstraints13);
			jPanel.add(jLabel6, gridBagConstraints14);
			jPanel.add(getTxtMakePartNum(), gridBagConstraints15);
			jPanel.add(getJPanel1(), gridBagConstraints16);
			jPanel.add(getCbxFormulae(), gridBagConstraints17);
			jPanel.add(jLabel11, gridBagConstraints18);
			jPanel.add(getTxtRation(), gridBagConstraints22);
			jPanel.add(jLabel12, gridBagConstraints23);
			jPanel.add(jLabel13, gridBagConstraints110);
			jPanel.add(getTxtRoughtWeight(), gridBagConstraints21);
		}
		return jPanel;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtMaterialDim() {
		if (txtMaterialDim == null) {
			txtMaterialDim = new JTextField();
		}
		return txtMaterialDim;
	}
	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtRoughQty() {
		if (txtRoughQty == null) {
			txtRoughQty = new JTextField();
		}
		return txtRoughQty;
	}
	/**
	 * This method initializes jTextField2	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtWorkshopRouting() {
		if (txtWorkshopRouting == null) {
			txtWorkshopRouting = new JTextField();
			txtWorkshopRouting.setEditable(false);
		}
		return txtWorkshopRouting;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getChkShowAllFormulae() {
		if (chkShowAllFormulae == null) {
			chkShowAllFormulae = new JCheckBox();
			chkShowAllFormulae.setText("列出所有公式");
			chkShowAllFormulae.setBackground(new java.awt.Color(223,216,206));
			chkShowAllFormulae.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    refreshCbxFormulae();
                }
			});
		}
		return chkShowAllFormulae;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxRoughUom() {
		if (cbxRoughUom == null) {
			cbxRoughUom = new JComboBox();
		}
		return cbxRoughUom;
	}
	/**
	 * This method initializes jComboBox1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxMaterialUom() {
		if (cbxMaterialUom == null) {
			cbxMaterialUom = new JComboBox();
		}
		return cbxMaterialUom;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtRoughtWeight() {
		if (txtRoughtWeight == null) {
			txtRoughtWeight = new JTextField();
		}
		return txtRoughtWeight;
	}
	/**
	 * This method initializes jTextField3	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtMakePartNum() {
		if (txtMakePartNum == null) {
			txtMakePartNum = new JTextField();
		}
		return txtMakePartNum;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel10 = new JLabel();
			jLabel9 = new JLabel();
			jLabel8 = new JLabel();
			jLabel7 = new JLabel();
			FlowLayout flowLayout20 = new FlowLayout();
			jPanel1 = new JPanel();
			jPanel1.setLayout(flowLayout20);
			jLabel7.setText("其中, 夹头");
			jLabel8.setText("压头");
			jLabel9.setText("    每");
			jLabel10.setText("件一个夹头或压头");
			flowLayout20.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel1.setBackground(new java.awt.Color(223,216,206));
			jPanel1.add(jLabel7, null);
			jPanel1.add(getTxtColletNum(), null);
			jPanel1.add(jLabel8, null);
			jPanel1.add(getTxtBallastNum(), null);
			jPanel1.add(jLabel9, null);
			jPanel1.add(getTxtEveryRange(), null);
			jPanel1.add(jLabel10, null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jTextField4	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtColletNum() {
		if (txtColletNum == null) {
			txtColletNum = new JTextField();
			txtColletNum.setColumns(2);
		}
		return txtColletNum;
	}
	/**
	 * This method initializes jTextField5	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtBallastNum() {
		if (txtBallastNum == null) {
			txtBallastNum = new JTextField();
			txtBallastNum.setColumns(2);
		}
		return txtBallastNum;
	}
	/**
	 * This method initializes jTextField6	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtEveryRange() {
		if (txtEveryRange == null) {
			txtEveryRange = new JTextField();
			txtEveryRange.setColumns(2);
		}
		return txtEveryRange;
	}
	/**
	 * This method initializes jComboBox2	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getCbxFormulae() {
		if (cbxFormulae == null) {
			cbxFormulae = new JComboBox();
			cbxFormulae.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Object obj = cbxFormulae.getSelectedItem();
                    if (!(obj instanceof DOSObjectAdapter))
                        return;

    			    calcRation(((DOSObjectAdapter)obj).getDosObject());
                }
			});
		}
		return cbxFormulae;
	}
	/**
	 * This method initializes jTextField7	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getTxtRation() {
		if (txtRation == null) {
			txtRation = new JTextField();
		}
		return txtRation;
	}
	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			FlowLayout flowLayout21 = new FlowLayout();
			jPanel2 = new JPanel();
			jPanel2.setLayout(flowLayout21);
			flowLayout21.setAlignment(java.awt.FlowLayout.RIGHT);
			jPanel2.setBackground(new java.awt.Color(223,216,206));
			jPanel2.add(getBtnOk(), null);
			jPanel2.add(getBtnCancel(), null);
		}
		return jPanel2;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText("确定");
			btnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    save();
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
                    MaterialCalculator.this.dispose();
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

    /**
     * @param dosObj
     */
    public void setContextObject(DOSChangeable dosObj)
            throws IllegalArgumentException {
        if (dosObj == null)
            throw new IllegalArgumentException("Invalidate argument - dosObj.");

        contextObj = dosObj;
        
        refresh();
    }

    /**
     * 刷新界面数据
     */
    private void refresh() {
        try {
            Object [][] map = {
                {"workshop routing", txtWorkshopRouting}, // 分厂工艺路线
                {"r_material_dim", txtMaterialDim}, // 毛坯下料尺寸
                {"rough qty", txtRoughQty}, // 毛坯数量
                {"makepartnum", txtMakePartNum}, // 加工零件数量
                {"Rough Weight", txtRoughtWeight}, // 毛坯重量
                {"materialration", txtRation} // 定额
            };
            
            Object tmpObject = null;
            
            for (int i = 0; i < map.length; i++) {
                tmpObject = contextObj.get((String)map[i][0]);
                ((JTextField)map[i][1]).setText(
                        tmpObject == null ? "" : tmpObject.toString());
            }
            
            // 定额备注
            tmpObject = contextObj.get("Material Ration Remarks");
            if (tmpObject != null) {
                try {
                    String tmpString = tmpObject.toString();
                    // 夹头
                    int start = tmpString.indexOf("夹头") + 3;
                    int end = tmpString.indexOf(" ", start);
                    String tmpNum = tmpString.substring(start, end);
                    txtColletNum.setText(tmpNum);
                    // 压头
                    start = tmpString.indexOf("压头") + 3;
                    end = tmpString.indexOf(" ", start);
                    tmpNum = tmpString.substring(start, end);
                    txtBallastNum.setText(tmpNum);
                    // 每
                    start = tmpString.indexOf("每") + 2;
                    end = tmpString.indexOf(" ", start);
                    tmpNum = tmpString.substring(start, end);
                    txtEveryRange.setText(tmpNum);
                } catch (Exception e) {
                    // Maybe we should got error, but no matter
                }
            }
            
            // 毛坯单位
            tmpObject = contextObj.get("rough_uom");
            util.refreshCodeComboBox("单位", cbxRoughUom, (String)tmpObject);
            
            // 材料单位
            tmpObject = contextObj.get("m_uom");
            util.refreshCodeComboBox("单位", cbxMaterialUom, (String)tmpObject);
            
            // 公式列表
            refreshCbxFormulae();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "读取数据失败.", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void refreshCbxFormulae() {
        cbxFormulae.removeAllItems();
        cbxFormulae.addItem(""); // At least we have one line
        cbxFormulae.setSelectedIndex(0);
        
        final String ouidOfFormulaClass = "8605def4";
        final String ouidOfAssociation = "8605df21";
        
        try {
            ArrayList listResult = null;
            Object tmpObject = null;

            if (chkShowAllFormulae.isSelected()) {
                // Get all formulae
                listResult = dos.list(ouidOfFormulaClass);
            } else {
                // Get formulae which linked to the material of normalpart
                tmpObject = contextObj.get("rawmaterial");
                if (tmpObject == null)
                    return;

                HashMap filter = new HashMap();
                filter.put("list.mode", "association");
                filter.put("ouid@association.class", ouidOfAssociation);

                listResult = dos.listLinkFrom(tmpObject.toString(), filter);
            }

            if (listResult == null)
                return;

            ArrayList tmpList = null;
            int size = listResult.size();
            for (int i = 0; i < size; i++) {
                tmpList = (ArrayList) listResult.get(i);
                if (tmpList == null)
                    continue;

                String ouidFormula = (String) tmpList.get(0);
                DOSChangeable dosFormula = dos.get(ouidFormula);
                if (dosFormula != null)
                    cbxFormulae.addItem(new DOSObjectAdapter(dosFormula,
                            "%md$description% : %Formula%"));

                tmpList = null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "获取定额计算公式时出错.", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void calcRation(DOSChangeable dosFormula) {
        // 准备公式, 显示公式计算界面
        Formulae f = new Formulae(this, true);

        String formula = (String)dosFormula.get("Formula");
        if (f.setFormula(formula) == false) {
            JOptionPane.showMessageDialog(MaterialCalculator.this, "公式有错误, 请检查并修正.");
            return;
        }

        f.setDescription((String)dosFormula.get("md$description"));
        
        // 设一些缺省值, 公式的缺省系数, 材料的密度, 单位重量等
        Object obj = dosFormula.get("Coefficient");
        if (obj != null)
            f.setValue("系数", obj.toString());
        
        String ouidMaterial = (String)contextObj.get("rawmaterial");
        if (ouidMaterial != null) {
            DOSChangeable dosMaterial;
            try {
                dosMaterial = dos.get(ouidMaterial);
	            if (dosMaterial != null) {
	                Double dTmp = (Double)dosMaterial.get("Density");
	                f.setValue("密度", dTmp == null ? null : dTmp.toString());
	                dTmp = (Double)dosMaterial.get("Theoretic Weight");
	                f.setValue("单位重量", dTmp == null ? null : dTmp.toString());
	            }
            } catch (Exception e) {
                System.err.println("设置公式计算变量缺省值时出错.");
                // do nothing
            }
        }
        
        f.pack();
        util.centerWindow(null, f);
        f.show();
        
        // 开始进行公式计算
        if (f.userChoice != JOptionPane.OK_OPTION)
            return;
        
        // 将计算结果写入界面, 数字保留三位小数
        NumberFormat formator = NumberFormat.getInstance();
        formator.setMaximumFractionDigits(3);
        formator.setGroupingUsed(false);
        
        // 定额
        double value = f.getRationResult();
        txtRation.setText(formator.format(value));
        // 毛坯重量
        value = f.getRoughWeightResult();
        txtRoughtWeight.setText(formator.format(value));
        // 毛坯数量
        String tmpString = (String)dosFormula.get("rough qty");
        if (tmpString == null) {
            txtRoughQty.setText("");
        } else {
	        tmpString = f.getValue(tmpString);
	        txtRoughQty.setText(tmpString);
        }
        // 毛坯下料尺寸
        tmpString = (String)dosFormula.get("r_material_dim");
        if (tmpString == null) {
            txtMaterialDim.setText("");
        } else {
            String [] pieces = tmpString.split(Formulae.opRegex);
            
            for (int i = 0; i < pieces.length; i++) {
                String tmpValue = f.getValue(pieces[i]);
                if (tmpValue == null)
                    continue;
                
                tmpString = tmpString.replaceAll(pieces[i], tmpValue);
            }
            
            txtMaterialDim.setText(tmpString);
        }
        // 毛坯单位
        tmpString = (String)dosFormula.get("rough_uom");
        if (tmpString == null) {
            cbxRoughUom.setSelectedIndex(-1);
        } else {
            int size = cbxRoughUom.getItemCount();
            for (int i = 0; i < size; i++) {
                DOSObjectAdapter dosAdapter = (DOSObjectAdapter)cbxRoughUom.getItemAt(i);
                if (tmpString.equals(dosAdapter.get("ouid"))) {
                    cbxRoughUom.setSelectedIndex(i);
                    break;
                }
            }
        }
        // 材料单位
        tmpString = (String)dosFormula.get("m_uom");
        if (tmpString == null) {
            cbxMaterialUom.setSelectedIndex(-1);
        } else {
            int size = cbxRoughUom.getItemCount();
            for (int i = 0; i < size; i++) {
                DOSObjectAdapter dosAdapter = (DOSObjectAdapter)cbxMaterialUom.getItemAt(i);
                if (tmpString.equals(dosAdapter.get("ouid"))) {
                    cbxMaterialUom.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void save() {
        // check data
        String msg = "";
        Float fRoughQty = null;
        Double dRougthWeight = null;
        Double dMakePartNum = null;
        Double dRation = null;
        
        String tmpString = "";
        
        try {
            msg = "毛坯数量";
            tmpString = txtRoughQty.getText();
            if (!tmpString.trim().equals(""))
                fRoughQty = new Float(tmpString);
            
            msg = "毛坯重量";
            tmpString = txtRoughtWeight.getText();
            if (!tmpString.trim().equals(""))
                dRougthWeight = new Double(tmpString);

            msg = "加工零件数量";
            tmpString = txtMakePartNum.getText();
            if (!tmpString.trim().equals(""))
                dMakePartNum = new Double(tmpString);
            
            msg = "定额";
            tmpString = txtRation.getText();
            if (!tmpString.trim().equals(""))
                dRation = new Double(tmpString);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "" + msg + "值填写错误, 请检查修改后重新提交.", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        tmpString = txtMaterialDim.getText();
        contextObj.put("r_material_dim", tmpString); // 毛坯下料尺寸
        
        contextObj.put("rough qty", fRoughQty); // 毛坯数量
        contextObj.put("Rough Weight", dRougthWeight); // 毛坯重量
        contextObj.put("makepartnum", dMakePartNum); // 加工零件数量
        contextObj.put("materialration", dRation); // 定额
        
        DOSObjectAdapter objAdapter = (DOSObjectAdapter)cbxMaterialUom.getSelectedItem();
        contextObj.put("m_uom", objAdapter == null ? null : objAdapter.get("ouid")); // 材料单位
        
        objAdapter = (DOSObjectAdapter)cbxRoughUom.getSelectedItem();
        contextObj.put("rough_uom", objAdapter == null ? null : objAdapter.get("ouid")); // 毛坯单位
        
        if (txtColletNum.getText().trim().equals("") && txtBallastNum.getText().trim().equals("") 
                && txtEveryRange.getText().trim().equals("")) {
            tmpString = null;
        } else {
            tmpString = remarkTempl.replaceAll("%j", txtColletNum.getText());
            tmpString = tmpString.replaceAll("%y", txtBallastNum.getText());
            tmpString = tmpString.replaceAll("%m", txtEveryRange.getText());
        }
        
        contextObj.put("Material Ration Remarks", tmpString); // 定额备注
        
        // 工艺定额利用率 material usage ratio
        try {
            tmpString = (String)contextObj.get("weight");
            Double dWeight = new Double(tmpString);
            Double dRatio = new Double(dWeight.doubleValue() 
                    / dRation.doubleValue() / fRoughQty.floatValue() 
                    / dMakePartNum.doubleValue());
            
            if (dRatio.doubleValue() > 1) {
                JOptionPane.showMessageDialog(this, "工艺定额利用率计算结果大于 1, 材料定额数据有误, 请检查.",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            NumberFormat formator = NumberFormat.getInstance();
            formator.setMaximumFractionDigits(3);
            formator.setGroupingUsed(false);

            contextObj.put("material usage ratio", new Double(formator.format(dRatio)));
        } catch (Exception e) {
            System.err.println("数据不全, 计算工艺定额利用率时出错: " + e);
            contextObj.put("material usage ratio", null);
        }
        
        // 将修改材料定额的人员写到数据库
        //get current user's ouid
    	String fuserOuid = "800017a7";
        String userOuid = null;
        String nameOfUser = "";
        
        ArrayList searchResults = null;
        HashMap searchCondition = new HashMap();
        searchCondition.put("8000197c", dyna.framework.client.LogIn.userID);
        try {
            searchResults = dos.list(fuserOuid, searchCondition);
            
            ArrayList tempList = (ArrayList) searchResults.get(0);
            userOuid = (String) tempList.get(0);
            
            DOSChangeable dosUser = dos.get(userOuid);
            nameOfUser = (String)dosUser.get("md$number") + " " + (String)dosUser.get("md$description");
            
            tempList = null;
        } catch (dyna.framework.iip.IIPRequestException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "获取当前用户失败, 保存失败.", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        contextObj.put("Material Ration Personnel", userOuid);
        contextObj.put("name@Material Ration Personnel", nameOfUser);

        // 将材料定额的修改日期写到数据库
        GregorianCalendar thisday = new GregorianCalendar();
        Date d = thisday.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String s = df.format(d);
        contextObj.put("Material Ration Modify Date", s);
        
      	// save
        try {
            if (contextObj.isChanged()) {
                dos.set(contextObj);
                JOptionPane.showMessageDialog(this, "保存成功.", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
            
            this.choice = JOptionPane.OK_OPTION;
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存失败.", "提示", JOptionPane.WARNING_MESSAGE);
        }
    }
 }  //  @jve:decl-index=0:visual-constraint="10,10"
