import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
AUS aus = DynaMOAD.aus ;

HashMap roleInfo = new HashMap() ;
roleInfo.put("roleId", dosObject.get("md$number")) ;
roleInfo.put("description", dosObject.get("md$description")) ;
roleInfo.put("status", null) ;
roleInfo.put("cdate", null) ;        
roleInfo.put("lmdate", null) ;

aus.createRole(roleInfo) ;