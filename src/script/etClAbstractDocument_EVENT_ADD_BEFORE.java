import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
AUS aus = DynaMOAD.aus ;

String userId = LogIn.userID ;

System.out.println(userId);

if (userId == null) return ;

ArrayList groupList = aus.listGroupOfUser (userId) ;
if (groupList == null || groupList.isEmpty ())
{
    return ;
}

String groupId = (String) groupList.get (0) ;
JOptionPane.showMessageDialog(null, "USERID: " + userId + "\n" +"GROUPID: " + groupId);
System.out.println("------------START-------------");
System.out.println("USERID: " + userId);
System.out.println("GROUPID: " + groupId);
System.out.println("-------------END-----------");

dosObject.put ("hiddenCode", groupId) ;

inputObject.setData (dosObject) ;