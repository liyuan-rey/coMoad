import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

		DOS dos = DynaMOAD.dos ; 
		DOSChangeable dosObject = inputObject.getInstanceData ();
		String ouid = (String)dosObject.get("ouid");
    String changeMark = (String)dosObject.get("Change Mark");
    
		//if change mark is 'ADD', don't change it
		if(changeMark == null)
 	   	{
			dosObject.put ("Change Mark", "UPT") ;
    	   			
			inputObject.setData(dosObject);
		}

		HashMap routinefilter = new HashMap () ;
		routinefilter.put ("list.mode", "association") ;
		//routinefilter.put ("version.condition.type", "wip");
		ArrayList searchResults = dos.listLinkFrom(ouid, routinefilter );

		print("searchResults = " + searchResults);

		if(searchResults != null)
		{
			for (int i = 0 ; i < searchResults .size () ; i++)
			 {	     
			  ArrayList tempList = (ArrayList) searchResults.get (i) ; 		     	
		 		print(tempList);
		 		DOSChangeable linkPart = dos.get((String) tempList.get (0));
		 		if(linkPart.get("Operation Mark") == null)
				{
						linkPart.put("Operation Mark", "OPR");
						dos.set(linkPart);
				}

				tempList = null;
			}
		}
