import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
AUS aus = DynaMOAD.aus ;

HashMap userInfo = new HashMap() ;
userInfo.put("userId", dosObject.get("md$number")) ;
userInfo.put("password", dosObject.get("password")) ;
userInfo.put("name", dosObject.get("md$description")) ;
userInfo.put("description", dosObject.get("remarks")) ;
userInfo.put("primarygroup", null) ;
userInfo.put("status",null) ;
userInfo.put("cdate", null) ;
userInfo.put("lfdate", null) ;
userInfo.put("lmdate", null) ;
userInfo.put("lldate", null) ;
userInfo.put("failcount", null) ;
userInfo.put("logincount", null) ;

aus.setUser(userInfo) ;

//aus.linkRoleToUser(roleId, dosObject.get("md$number")) ;