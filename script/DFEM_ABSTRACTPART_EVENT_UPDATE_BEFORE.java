import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

		DOS dos = DynaMOAD.dos ; 
		DOSChangeable dosObject = inputObject.getInstanceData ();

    		String changeMark = (String)dosObject.get("Change Mark");
    
		//if change mark is 'ADD', don't change it
		if(changeMark == null)
 	   	{
			dosObject.put ("Change Mark", "UPT") ;
    	   			
			inputObject.setData(dosObject);
		}
