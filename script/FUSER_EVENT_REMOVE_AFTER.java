import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOS dos = DynaMOAD.dos ;
AUS aus = DynaMOAD.aus ;

DOSChangeable dosObject = dos.get(inputObject) ;

String userId = dosObject.get("md$number") ;
System.out.println("userId : " + userId) ;
/*String usedMessage = "�̹� ������̹Ƿ� ������ �� �����ϴ�." ;
String deleteMessage = "�����Ͻðڽ��ϱ�?" ;

ArrayList groupsObj = aus.listGroupOfUser(userId);
ArrayList rolesObj = aus.listRoleOfUser(userId);
if(!(groupsObj == null || groupsObj.size() <= 0))
{
    JOptionPane.showMessageDialog (null, 
                                   usedMessage, 
                                   "ERROR", JOptionPane.ERROR_MESSAGE) ;
    return ;
}
if(!(rolesObj == null || rolesObj.size() <= 0))
{
    JOptionPane.showMessageDialog (null, 
                                   usedMessage, 
                                   "ERROR", JOptionPane.ERROR_MESSAGE) ;
    return ;
}*/
ArrayList groupsObj = aus.listGroupOfUser(userId);
ArrayList rolesObj = aus.listRoleOfUser(userId);
if(!(groupsObj == null || groupsObj.size() <= 0))
{
    JOptionPane.showMessageDialog (null, 
                                   "Because the id is used, so you can't delete this user.", 
                                   "ERROR", JOptionPane.ERROR_MESSAGE) ;
    returnObject = Boolean.FALSE ;
}
if(!(rolesObj == null || rolesObj.size() <= 0))
{
    JOptionPane.showMessageDialog (null, 
                                   "Because the id is used, so you can't delete this user.", 
                                   "ERROR", JOptionPane.ERROR_MESSAGE) ;
    returnObject = Boolean.FALSE ;
}
if((groupsObj == null || groupsObj.size() <= 0) && (rolesObj == null || rolesObj.size() <= 0))
{
    boolean isDelete = aus.removeUser(userId) ;
}