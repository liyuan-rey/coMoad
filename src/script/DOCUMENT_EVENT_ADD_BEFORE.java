import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
AUS aus = DynaMOAD.aus ;

String userId = LogIn.userID ;

if (userId == null) return ;

ArrayList groupList = aus.listGroupOfUser (userId) ;
if (groupList == null || groupList.isEmpty ())
{
    return ;
}

String groupId = (String) groupList.get (0) ;

System.out.println("USERID: " + userId);
System.out.println("GROUPID: " + groupId);

dosObject.put ("hidden_code", groupId) ;

inputObject.setData (dosObject) ;