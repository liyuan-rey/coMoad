import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import javax.swing.JOptionPane;

DOS dos = DynaMOAD.dos ; 

DOSChangeable dosObject = inputObject.getInstanceData ();

if (dosObject.get ("Begin Project Number") != null)
{
DOSChangeable code = dos.getCodeWithName("MRP控制者与工作号对照表");

DOSChangeable codeItem = dos.getCodeItemWithId((String)code.get("ouid"),(String)dosObject.get ("Begin Project Number")) ;


if(codeItem == null)
	{
		JOptionPane.showMessageDialog(null, "没有与初始工作号对应的MRP控制者代码。");
		returnObject = Boolean.FALSE;							
		return;
	}

dosObject.put("mrpcn", codeItem.get("name"));
}

// for mark
dosObject.put ("Change Mark", "ADD") ;

inputObject.setData(dosObject);