import dyna.framework.client.pms.* ;
import dyna.framework.service.dos.* ;
import javax.swing.* ;

if (inputObject == null)
{
    return ;
}

String ouid = null ;
String name = null ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
if (dosObject == null)
{
    return ;
}
ouid = dosObject.get ("ouid") ;
name = dosObject.get ("md$description") + " (" + dosObject.get ("md$number") + ")" ;

TemplateCloneWindow tcw = new TemplateCloneWindow () ;
tcw.setProject (ouid, name) ;

tcw.setVisible (true) ;