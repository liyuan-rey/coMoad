import dyna.framework.client.* ;
import dyna.framework.client.dfempdm.* ;
import dyna.framework.service.dos.* ;
import dyna.framework.service.* ;


DOS dos = DynaMOAD.dos ;

DataImport di = new DataImport();
di.setDos(dos);
di.show();

di = null;
