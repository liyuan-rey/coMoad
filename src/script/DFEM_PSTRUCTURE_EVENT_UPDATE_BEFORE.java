import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;
import dyna.util.* ;

		HashMap filter = new HashMap () ;
		filter.put ("list.mode", "association") ;
		filter.put ("version.condition.type", (String)dosObject.get("vf$version"));
		ArrayList searchResults = dos.listLinkTo((String)dosObject.get("ouid"), filter );
		print(searchResults);
		
		if(searchResults != null)
		{
			for (int i = 0 ; i < searchResults .size () ; i++)
			 {	
			  ArrayList tempList = (ArrayList) searchResults.get (i) ;
		 		print(tempList);
		 		DOSChangeable linkPart = dos.get((String) tempList.get (0));
		 		if(linkPart.get("Link Mark") == null)
				{
						linkPart.put("Link Mark", "PSM");
						dos.set(linkPart);
				}
				
				tempList = null;
			}
		}