import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;

dosObject.put ("password", (String) dosObject.get ("md$number")) ;

inputObject.setData (dosObject) ;
