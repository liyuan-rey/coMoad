// Sample script.
// create a new instance of class
import java.util.* ;
import java.text.* ;
import dyna.framework.iip.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

DOS dos = DynaMOAD.dos ;

String classOuid = "86055c5f" ;  // Logger
DOSChangeable obj = new DOSChangeable () ;

// prepare datum for new instance.

obj.setClassOuid (classOuid) ;

obj.put ("md$description", "UPDATE OBJECT") ;  // you can find the name of field in OM
obj.put ("action", "86055c93") ;
DOSChangeable dosObject = inputObject.getInstanceData ();
obj.put("object", dosObject.get("md$number")+ ":" + dosObject.get("md$description"));

// invoke method in server.
try
{
    dos.add (obj) ;
}
catch (IIPRequestException e)
{
    e.printStackTrace () ;
}
