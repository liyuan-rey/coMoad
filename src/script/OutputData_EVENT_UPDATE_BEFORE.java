import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.setModifyData () ;

String tempString = (String) dosObject.get ("output") ;
if (tempString == null)
{
    DynaMOAD.dos.set (dosObject) ;
    return ;
}

DOSChangeable dosOutput = DynaMOAD.dos.get (tempString) ;
if (dosOutput == null)
{
    DynaMOAD.dos.set (dosObject) ;
    return ;
}

tempString = (String) DynaMOAD.dos.getStatus (tempString) ;
if (tempString == null)
{
    return ;
}

if ("RLS".equals (tempString) == false)
{
    DynaMOAD.dos.set (dosObject) ;
    return ;
}

tempString = (String) dosOutput.get ("md$mdate") ;
if (tempString == null)
{
    return ;
}

dosObject.put ("finishDate", tempString) ;

inputObject.setData (dosObject) ;

DynaMOAD.dos.set (dosObject) ;
