import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;
		
		HashMap linkInfo = inputObject;
		print("!!!!!!!!!!!!!!!!!!!!!!!!" +  inputObject);

		String end1Ouid = (String)linkInfo.get("end1");

		DOSChangeable dosObject = null ;

    dosObject = DynaMOAD.dos.get (end1Ouid) ;
    
    String operationMark = (String)dosObject.get("Operation Mark");
    
    if(operationMark== null)		
    {
    	   	dosObject.put ("Operation Mark", "OPR") ; 
    	   	DynaMOAD.dos.set (dosObject) ;
    }