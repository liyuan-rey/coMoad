/*
 * Created on 2004-10-19
 *
 */
package dyna.framework.client.dfempdm;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import dyna.framework.client.DynaMOAD;
import dyna.framework.service.dos.DOSChangeable;

/**
 * @author 李渊
 *
 */
public class RoutingTableModel extends AbstractTableModel {
	public static final int SEQUENCE_NO_COLUMN = 0; // 工序号
	public static final int WORKSHOP_COLUMN = 1; // 加工分厂
	public static final int WORKCENTER_COLUMN = 2; // 工作中心
	public static final int OPERATION_SPECIALTY_COLUMN = 3; // 工艺专业类型
	public static final int WORKSHOP_SEQUENCE_COLUMN = 4; // 分厂工艺路线顺序
	public static final int SEQUENCE_TYPE_COLUMN = 5; // 顺序类型
	public static final int ENTER_SEQUENCE_COLUMN = 6; // 进入工序
	public static final int END_SEQUENCE_COLUMN = 7; // 结束工序
	public static final int DESCRIPTION_COLUMN = 8; // 描述
	public static final int PREPARATION_TIME_COLUMN = 9; // 准备时间
	public static final int OPERATING_TIME_COLUMN = 10; // 操作时间
	public static final int PROCESS_NUM_COLUMN = 11; // 加工数量
	public static final int OPERATOR_NUM_COLUMN = 12; // 操作人数
	public static final int ATTACHMENT_COLUMN = 13; // 附件
	// 以下是隐藏列
	public static final int RAW_OUID_COLUMN = 14; // Routing 对象的 ouid
	public static final int RAW_WORKSHOP_COLUMN = 15; // workshop codeitem 的 ouid
	public static final int RAW_WORKCENTER_COLUMN = 16; // workcenter 的 ouid
	public static final int RAW_OPERATION_SPECIALTY_COLUMN = 17; // Operation Specialty codeitem 的 ouid
	public static final int RAW_SEQUENCE_TYPE_COLUMN = 18; // Sequence Type codeitem 的 ouid

	private static final String[][] columnInfo = { //说明: {"表格列名", "对应的 Routing DOSChangable 对象的 HashMap key"}
	        {"序", "Sequence"},
	        {"加工分厂", "name@Workshop"},
	        {"工作中心", "name@Workcenter"},
	        {"工艺专业类型", "name@Operation Specialty"},
	        {"分厂工艺路线顺序", "Workshop Squence"},
	        {"顺序类型", "name@Sequence Type"},
	        {"进入工序", "Enter Operation Squence"},
	        {"结束工序", "End Operation Squence"},
	        {"描述", "Description"},
	        {"准备时间(小时)", "Preparation Time"},
	        {"操作时间(小时)", "Operating Time"},
	        {"数量", "Part Quantity"},
	        {"人数", "Operator Count"},
	        {"附件数", "defaultFileNumber"},
	        // 以下是隐藏列
	        {"ouid", "ouid"},
	        {"Workshop", "Workshop"},
	        {"Workcenter", "Workcenter"},
	        {"Operation Specialty", "Operation Specialty"},
	        {"Sequence Type", "Sequence Type"} };

	private ArrayList data = new ArrayList();

    int editMode = 0;
    ArrayList authorTypes = new ArrayList();

    static final int [][] mapping = { // {工序模板对象需要写入的字段, 对应的工序对象字段}
        {RoutingTemplatePanel.SEQUENCE_NO_COLUMN, RoutingTableModel.SEQUENCE_NO_COLUMN},
        {RoutingTemplatePanel.WORKSHOP_COLUMN, RoutingTableModel.WORKSHOP_COLUMN},
        {RoutingTemplatePanel.WORKCENTER_COLUMN, RoutingTableModel.WORKCENTER_COLUMN},
        {RoutingTemplatePanel.WORKSHOP_SEQUENCE_COLUMN, RoutingTableModel.WORKSHOP_SEQUENCE_COLUMN},
        {RoutingTemplatePanel.OPERATION_SPECIALTY_COLUMN, RoutingTableModel.OPERATION_SPECIALTY_COLUMN},
        {RoutingTemplatePanel.DESCRIPTION_COLUMN, RoutingTableModel.DESCRIPTION_COLUMN},
        {RoutingTemplatePanel.RAW_WORKSHOP_COLUMN, RoutingTableModel.RAW_WORKSHOP_COLUMN},
        {RoutingTemplatePanel.RAW_WORKCENTER_COLUMN, RoutingTableModel.RAW_WORKCENTER_COLUMN},
        {RoutingTemplatePanel.RAW_OPERATION_SPECIALTY_COLUMN, RoutingTableModel.RAW_OPERATION_SPECIALTY_COLUMN} };

    private final static String ouidRoutingClass = "86057286";
    private final static String ouidTemplClass = "86057287";

    /* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnInfo.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data.size();
	}

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return columnInfo[col][0];
    }

    public static String getColumnDosName(int col) {
        return columnInfo[col][1];
    }

    /* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
	    Object objReturn = null;
	    
	    if (row >= 0 && row < getRowCount()
	            && col >= 0 && col < getColumnCount()) {
		    DOSChangeable dosRouting = (DOSChangeable)data.get(row);
		    objReturn = dosRouting.get(columnInfo[col][1]);
	    }
	    
		return objReturn == null ? null : objReturn.toString();
	}

	/* 对变更数据进行验证
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
	    // 优先编辑模式验证 editMode
	    if (editMode == 0) {
	        return false;
	    } else if (editMode == 1) {
	        if (col == PREPARATION_TIME_COLUMN ||
	            col == OPERATING_TIME_COLUMN ||
	            col == PROCESS_NUM_COLUMN ||
	            col == OPERATOR_NUM_COLUMN)
	            return false;
	    } else if (editMode == 2) {
	        if (col == SEQUENCE_NO_COLUMN ||
	            col == WORKSHOP_COLUMN ||
	            col == WORKCENTER_COLUMN ||
	            col == OPERATION_SPECIALTY_COLUMN ||
	            col == WORKSHOP_SEQUENCE_COLUMN ||
	            col == SEQUENCE_TYPE_COLUMN ||
	            col == ENTER_SEQUENCE_COLUMN ||
	            col == END_SEQUENCE_COLUMN ||
	            col == DESCRIPTION_COLUMN ||
	            col == ATTACHMENT_COLUMN)
	            return false;
	    }
	    
	    // 其次进行编辑者工种验证 authorTypes
        if (col == SEQUENCE_NO_COLUMN ||
	            col == WORKSHOP_COLUMN ||
	            col == WORKCENTER_COLUMN ||
	            col == OPERATION_SPECIALTY_COLUMN ||
	            col == WORKSHOP_SEQUENCE_COLUMN ||
	            col == SEQUENCE_TYPE_COLUMN ||
	            col == ENTER_SEQUENCE_COLUMN ||
	            col == END_SEQUENCE_COLUMN ||
	            col == DESCRIPTION_COLUMN ||
	            col == ATTACHMENT_COLUMN) {
            String ouidOpSpec = (String)getValueAt(row, RAW_OPERATION_SPECIALTY_COLUMN);
            if (authorTypes.contains(ouidOpSpec) == false)
                return false;
        }

	    // 最后进行常规编辑状态验证
		if (col == SEQUENCE_NO_COLUMN) {
            return false; // SequenceNo column can not be changed
        } else if (col == ENTER_SEQUENCE_COLUMN || col == END_SEQUENCE_COLUMN) {
            Object squenceType = getValueAt(row, SEQUENCE_TYPE_COLUMN);
            
            // 只有并行顺序可以设置进入和结束工序
            if (squenceType == null || squenceType.toString().equals("串行顺序 [0]"))
                return false;
        }
        
        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int col) {
        if (isCellEditable(row, col) == false)
            return;
        
        DOSChangeable dosRouting = (DOSChangeable)data.get(row);
	    
        // 更新隐藏的 ouid
        if (col == WORKSHOP_COLUMN || col == WORKCENTER_COLUMN
                || col == OPERATION_SPECIALTY_COLUMN || col == SEQUENCE_TYPE_COLUMN) {
            int colToUpdate = -1;
            String codeName = null;
            if (col == WORKSHOP_COLUMN) {
                colToUpdate = RAW_WORKSHOP_COLUMN;
                codeName = "分厂";
            } else if (col == WORKCENTER_COLUMN) {
                colToUpdate = RAW_WORKCENTER_COLUMN;
                codeName = "工作中心类别";
            } else if (col == OPERATION_SPECIALTY_COLUMN) {
                colToUpdate = RAW_OPERATION_SPECIALTY_COLUMN;
                codeName = "工艺专业类型";
            } else if (col == SEQUENCE_TYPE_COLUMN) {
                colToUpdate = RAW_SEQUENCE_TYPE_COLUMN;
                codeName = "工序顺序类型";
            }
            
            if (value == null)
                dosRouting.put(columnInfo[colToUpdate][1], null);
            else {
                if (value instanceof DOSObjectAdapter) {
                    DOSChangeable dosObj = ((DOSObjectAdapter)value).getDosObject();
                    String ouid = (dosObj == null ? "" : (String)dosObj.get("ouid"));
                    dosRouting.put(columnInfo[colToUpdate][1], ouid);
                } else { // This case will match when call by paste function.
/*                    if (col != WORKCENTER_COLUMN) { // 操作非工作中心值, 都是Code 类型的属性
                        try {
	                        String [] pieces = value.toString().split(" ");
	                        if (pieces.length < 1 || pieces[0].equals(""))
	                            return;
	                        
	                        DOSChangeable dosCode = DynaMOAD.dos.getCodeWithName(codeName);
	                        String ouidCode = (String)dosCode.get("ouid");
	
	                        DOSChangeable dosCodeItem = DynaMOAD.dos.getCodeItemWithName(ouidCode, pieces[0]);
	                        dosRouting.put(columnInfo[colToUpdate][1],
	                                dosCodeItem == null ? null : (String)dosCodeItem.get("ouid"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    } else { // 操作工作中心值, Object 类型
                        // 有 authorType 值限制, 直接不允许写入值, 用户必须自己选
                        return;
                    }
*/
                    Exception e = new Exception("Unexcept call about setValueAt.");
                    e.printStackTrace();
                    return;
                }
            }
        }
    
        // 更新显式的值
        dosRouting.put(columnInfo[col][1], value == null ? null : value.toString());

        fireTableRowsUpdated(row, row);
    }

    public void addRow(int index, DOSChangeable rowData, boolean doCheck) {
        // 因为工艺专业类型受传入的 authorTypes 控制, 所以在这里预处理一下
        if (doCheck) {
		    String opSpec = "";
		    String rawOpSpec = "";
		    if (authorTypes.size() > 0) {
		        rawOpSpec = (String)authorTypes.get(0);
		        try {
		            DOSChangeable dosOpSpec = DynaMOAD.dos.getCodeItem(rawOpSpec);
		            if (dosOpSpec == null)
		                return;
		            
		            opSpec = "" + dosOpSpec.get("name") + " [" + dosOpSpec.get("codeitemid") + "]"; 
		        } catch (Exception e) {
		            e.printStackTrace();
		            return;
		        }
		    }
		
            rowData.put(columnInfo[OPERATION_SPECIALTY_COLUMN][1], opSpec);
            rowData.put(columnInfo[RAW_OPERATION_SPECIALTY_COLUMN][1], rawOpSpec);
        }
        
        // Routing 的 DOSChangeable 对象本身的附件数量信息没有被设置, 
        // 这里尝试重新获取附件数量信息
        // 重新获取附件数量
        try {
            String ouidRouting = (String)rowData.get("ouid");
            if (ouidRouting != null && !ouidRouting.equals("")) {
	            ArrayList files = DynaMOAD.dos.listFile(ouidRouting);
	            rowData.put(columnInfo[ATTACHMENT_COLUMN][1], Integer.toString(files == null ? 0 : files.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        // 添加数据
        data.add(index, rowData);
        
        // 整理工序号
        int lastRow = data.size() - 1;
	    calcSequenceNum(index, lastRow);
	    
        fireTableRowsInserted(lastRow, lastRow);
    }
    
    /**
     * @param rowData
     */
    public void addRow(DOSChangeable rowData, boolean doCheck) {
        addRow(data.size(), rowData, doCheck);
    }
    
    /**
     * 
     */
    public void addNew() {
        DOSChangeable dosNewRouting = new DOSChangeable();
        dosNewRouting.setClassOuid(ouidRoutingClass);

        int rows = getRowCount();
        if (rows > 0) { // if we have data, use the data of last row to create new row
            DOSChangeable dosLastRouting = (DOSChangeable)data.get(getRowCount()-1);
            
            dosNewRouting.put(columnInfo[WORKSHOP_COLUMN][1], dosLastRouting.get(columnInfo[WORKSHOP_COLUMN][1]));
            dosNewRouting.put(columnInfo[RAW_WORKSHOP_COLUMN][1], dosLastRouting.get(columnInfo[RAW_WORKSHOP_COLUMN][1]));
            dosNewRouting.put(columnInfo[OPERATION_SPECIALTY_COLUMN][1], dosLastRouting.get(columnInfo[OPERATION_SPECIALTY_COLUMN][1]));
            dosNewRouting.put(columnInfo[RAW_OPERATION_SPECIALTY_COLUMN][1], dosLastRouting.get(columnInfo[RAW_OPERATION_SPECIALTY_COLUMN][1]));
            dosNewRouting.put(columnInfo[WORKSHOP_SEQUENCE_COLUMN][1], dosLastRouting.get(columnInfo[WORKSHOP_SEQUENCE_COLUMN][1]));
        }
        
        addRow(dosNewRouting, true);
    }

    /**
     * DOSChangeable do not implements 'clone' method, so we do it by ourselves
     * @param changeable
     * @return
     */
    public static DOSChangeable cloneRouting(DOSChangeable dosObj) {
        DOSChangeable dosReturn = new DOSChangeable();
        
        String dosClassOuid = dosObj.getClassOuid();
        dosReturn.setClassOuid(dosClassOuid);
        
        for (int i = 0; i < columnInfo.length; i ++)
            dosReturn.put(columnInfo[i][1], dosObj.get(columnInfo[i][1]));
        
        // 清除 ouid 字段, 以区分新建的对象
        dosReturn.put(columnInfo[RAW_OUID_COLUMN][1], null);
        
        // 清除附件信息
        dosReturn.put(columnInfo[ATTACHMENT_COLUMN][1], null);
        
        return dosReturn;
    }

    public static DOSChangeable routingFromTemplate(DOSChangeable dosObj) {
        DOSChangeable dosReturn = new DOSChangeable();
        String key = null;
        Object value = null;

        for (int j = 0; j < mapping.length; j++) {
            dosReturn.setClassOuid(ouidRoutingClass);

            key = columnInfo[mapping[j][1]][1];
            value = dosObj.get(RoutingTemplatePanel.getColumnDosName(mapping[j][0]));
            
            dosReturn.put(key, value);

            key = null;
            value = null;
        }
        
        return dosReturn;
    }

    public static DOSChangeable routingToTemplate(DOSChangeable dosObj) {
        DOSChangeable dosReturn = new DOSChangeable();
        String key = null;
        Object value = null;

        for (int j = 0; j < mapping.length; j++) {
            dosReturn.setClassOuid(ouidTemplClass);

            key = RoutingTemplatePanel.getColumnDosName(mapping[j][0]);
            value = dosObj.get(columnInfo[mapping[j][1]][1]);
            
            dosReturn.put(key, value);

            key = null;
            value = null;
        }

        return dosReturn;
    }

    /**
     * 
     */
    public void removeAllRows() {
        int maxIndex = data.size() - 1; 
        if (maxIndex < 0)
            return;
        
        for (int i = maxIndex; i >=0 ; i--)
            data.remove(i);
        
        fireTableDataChanged();
    }
    
    /**
     * @param row
     */
    public void removeRow(int row) {
        int maxIndex = data.size() - 1; 
        if (row < 0 || row > maxIndex)
            return;
        
        data.remove(row);
	    calcSequenceNum(row, maxIndex);
	    fireTableDataChanged();
    }
    
    /**
     * @param rows
     */
    public void removeRows(int [] rows) throws IndexOutOfBoundsException {
        ArrayList removedRows = new ArrayList();
        
	    for (int i = 0; i < rows.length; i++)
	        removedRows.add(data.get(rows[i]));
	    
	    data.removeAll(removedRows);
	    calcSequenceNum(rows[0], data.size() - 1);
	    fireTableDataChanged();
    }

    private static int gcd(int i, int j) {
        return (j == 0) ? i : gcd(j, i % j);
    }

    private static void rotate(ArrayList al, int a, int b, int shift) {
        int size = b - a;
        int r = size - shift;
        int g = gcd(size, r);
        for (int i = 0; i < g; i++) {
            int to = i;
            Object tmp = al.get(a + to);
            for (int from = (to + r) % size; from != i; from = (to + r) % size) {
                al.set(a + to, al.get(a + from));
                to = from;
            }
            al.set(a + to, tmp);
        }
    }

    /**
     * Moves one or more rows from the inlcusive range <code>start</code> to
     * <code>end</code> to the <code>to</code> position in the model. After
     * the move, the row that was at index <code>start</code> will be at index
     * <code>to</code>. This method will send a <code>tableChanged</code>
     * notification message to all the listeners.
     * <p>
     * 
     * <pre>
     * 
     *   Examples of moves:
     *   
     * <p>
     * 
     *   1. moveRow(1,3,5);
     *           a|B|C|D|e|f|g|h|i|j|k   - before
     *           a|e|f|g|h|B|C|D|i|j|k   - after
     *   
     * <p>
     * 
     *   2. moveRow(6,7,1);
     *           a|b|c|d|e|f|G|H|i|j|k   - before
     *           a|G|H|b|c|d|e|f|i|j|k   - after
     *   
     * <p> 
     *  </pre>
     * 
     * @param start
     *            the starting row index to be moved
     * @param end
     *            the ending row index to be moved
     * @param to
     *            the destination of the rows to be moved
     * @exception ArrayIndexOutOfBoundsException
     *                if any of the elements would be moved out of the table's
     *                range
     *  
     */
    public void moveRow(int start, int end, int to) {
        int shift = to - start;
        
        int first, last;
        if (shift < 0) {
            first = to;
            last = end;
        } else {
            first = start;
            last = to + end - start;
        }
        
        rotate(data, first, last + 1, shift);

        calcSequenceNum(first, last);
        fireTableRowsUpdated(first, last);
    }

    /**
     * 重新计算工序号
     * @param rangeFrom
     * @param rangeTo
     */
    private void calcSequenceNum(int from, int to) {
        if (from < 0 || to > data.size() - 1)
            return;
        
        for (int i = from; i <= to; i++) {
            DOSChangeable dosRouting = (DOSChangeable)data.get(i);
            dosRouting.put(columnInfo[SEQUENCE_NO_COLUMN][1], new Integer((i+1)*10));
        }
    }

    public DOSChangeable getRawData(int index) {
        return (DOSChangeable)data.get(index);
    }

    public DOSChangeable setRawData(int index, DOSChangeable newItem) {
        return (DOSChangeable)data.set(index, newItem);
    }

    /**
     * @param editMode
     * @param authorTypes
     */
    public void setAuthorInfo(int editMode, ArrayList authorTypes) {
        this.editMode = editMode;
        this.authorTypes = authorTypes;
    }
}
