import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

		HashMap linkInfo = inputObject;
		
		String end1Ouid = (String)linkInfo.get("end1");
		String end2Ouid = (String)linkInfo.get("end2");
				
		if(end2Ouid.startsWith("operation$sf"))
			{
				DOSChangeable dosObject =  dosObject = DynaMOAD.dos.get (end1Ouid)  ;
		    
		    String operationMark = (String)dosObject.get("Operation Mark");
		    
		    if(operationMark == null)		
		    {
		    	   	dosObject.put ("Operation Mark", "OPR") ; 
		    	   	DynaMOAD.dos.set (dosObject) ;
		    }
  		}