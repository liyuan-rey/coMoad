import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

DOS dos = DynaMOAD.dos ; 
DOSChangeable dosObject = inputObject.getInstanceData ();

dosObject.put ("Change Mark", "ADD") ;

inputObject.setData(dosObject);
