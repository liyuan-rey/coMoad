import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

HashMap linkInfo = inputObject ;

AUS aus = DynaMOAD.aus ;
DOS dos = DynaMOAD.dos ;

String targetObjectId = linkInfo.get("end2") ;
String groupId = linkInfo.get("end1") ;
System.out.println("targetObjectId.after : " + targetObjectId) ;
System.out.println("groupId : " + groupId) ;

DOSChangeable team = (DOSChangeable) dos.get (groupId) ;
DOSChangeable user = (DOSChangeable) dos.get (targetObjectId) ;

System.out.println("targetObjectId.after : " + (String) user.get ("md$number")) ;
System.out.println("groupId : " + (String) team.get ("md$number")) ;

aus.unlinkUserFromGroup ((String) user.get ("md$number"), (String) team.get ("md$number")) ;