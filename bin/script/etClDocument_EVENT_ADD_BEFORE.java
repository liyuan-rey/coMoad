import java.util.* ;
import java.text.SimpleDateFormat;
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


Date rightNow = Calendar.getInstance().getTime();

SimpleDateFormat sfDate = new SimpleDateFormat("yyMMdd");


String strHiddenCode = groupId + "." + sfDate.format(rightNow) + ".";



dosObject.put ("hiddenCode", strHiddenCode) ;

System.out.println("inputObject: " + inputObject +"\n"+ "dosObject:" +dosObject);
System.out.println("inputObject class: " + inputObject.getClass().getName() +"\n"+ "dosObject Class:" +dosObject.getClass().getName());

inputObject.setData (dosObject) ;

