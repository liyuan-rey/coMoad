import dyna.framework.client.* ;
import dyna.framework.client.dfempdm.* ;
import dyna.framework.service.dos.* ;
import dyna.framework.service.* ;


DOS dos = DynaMOAD.dos ;

PSMImport pi = new PSMImport();
pi.setDos(dos);
pi.show();

pi = null;
