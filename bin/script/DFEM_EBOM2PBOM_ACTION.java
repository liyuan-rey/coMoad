import dyna.framework.client.* ;
import dyna.framework.client.dfempdm.* ;
import dyna.framework.service.dos.* ;
import dyna.framework.service.* ;


DOS dos = DynaMOAD.dos ;

DOSChangeable dosObject = inputObject.getInstanceData ();

EBOM2PBOM e2p = new EBOM2PBOM(dos, dosObject);
e2p.transfer();
e2p = null;

PBOM2ERP p2e = new PBOM2ERP(dos, dosObject);
p2e.transfer();
p2e = null;

