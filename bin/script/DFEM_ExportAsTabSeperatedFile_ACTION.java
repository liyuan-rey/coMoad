import dyna.framework.client.* ;
import dyna.framework.client.dfempdm.* ;
import dyna.framework.service.dos.* ;
import dyna.framework.service.* ;
import javax.swing.JOptionPane;

HashMap selectedList = inputObject ;
DOS dos = DynaMOAD.dos ;
print(UIBuilder);
return;
ArrayList al = (ArrayList)selectedList.get("list");
if(al == null)
{
	JOptionPane.showMessageDialog(null, "��ѡ��Ҫ���ݵ�ERP�Ķ���");
	return;
}
for (int i = 0 ; i < al .size () ; i++)
 {
	DOSChangeable dosObject = dos.get((String)al.get(i));
	
	EBOM2PBOM e2p = new EBOM2PBOM(dos, dosObject, true);
	e2p.transfer();
	e2p = null;
	
	PBOM2ERP p2e = new PBOM2ERP(dos, dosObject, true);
	p2e.transfer();
	p2e = null;	
 }
 
 	JOptionPane.showMessageDialog(null, "������ɣ�������־���ڵ���Ϣ��");