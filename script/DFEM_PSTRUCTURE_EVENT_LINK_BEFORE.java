import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

		HashMap linkInfo = inputObject;

		String end1Ouid = (String)linkInfo.get("end1");

		DOSChangeable dosObject = null ;

    dosObject = DynaMOAD.dos.get (end1Ouid) ;
    
    String linkMark = (String)dosObject.get("Link Mark");
    String md_number = (String)dosObject.get("md$number");

    
    if( md_number.charAt(0) != '8' && linkMark== null)		// Product has no Link Mark field
    {
    	   	dosObject.put ("Link Mark", "PSM") ; 
    	   	DynaMOAD.dos.set (dosObject) ;
    }
    