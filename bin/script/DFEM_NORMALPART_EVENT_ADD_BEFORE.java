import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import javax.swing.JOptionPane;

DOS dos = DynaMOAD.dos ; 

DOSChangeable dosObject = inputObject.getInstanceData ();

if (dosObject.get ("Begin Project Number") != null)
{
DOSChangeable code = dos.getCodeWithName("MRP�������빤���Ŷ��ձ�");

DOSChangeable codeItem = dos.getCodeItemWithId((String)code.get("ouid"),(String)dosObject.get ("Begin Project Number")) ;


if(codeItem == null)
	{
		JOptionPane.showMessageDialog(null, "û�����ʼ�����Ŷ�Ӧ��MRP�����ߴ��롣");
		returnObject = Boolean.FALSE;							
		return;
	}

dosObject.put("mrpcn", codeItem.get("name"));
}

// for mark
dosObject.put ("Change Mark", "ADD") ;

inputObject.setData(dosObject);