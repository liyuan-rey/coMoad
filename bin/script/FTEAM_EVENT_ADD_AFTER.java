import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
AUS aus = DynaMOAD.aus ;

String description = dosObject.get("md$description") ;

HashMap groupInfo = new HashMap() ;
groupInfo.put("groupId", dosObject.get("md$number")) ;

if(description==null || description.equals(""))
    groupInfo.put("description", "") ;
else
    groupInfo.put("description", dosObject.get("md$description")) ;

aus.createGroup(groupInfo) ;