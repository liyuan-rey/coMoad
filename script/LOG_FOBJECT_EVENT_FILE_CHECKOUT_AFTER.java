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

String classOuid = "86055c5f" ;  // Logger class
DOSChangeable obj = new DOSChangeable () ;

// prepare datum for new instance.
obj.setClassOuid (classOuid) ;

obj.put ("md$description", "¼ì³öÎÄ¼þ") ;  
obj.put ("action", "86055c96") ;    // codeitem is 'CheckOut' 

// get list of file information of the object instance.
DSS dss = DynaMOAD.dss ; // api entry point of file server
String instanceOuid = inputObject.get("md$ouid");
ArrayList searchResults = null ;

// invoke method in server side
try
{    
    searchResults = dos.listFile (instanceOuid) ;
}
catch (IIPRequestException e)
{
    e.printStackTrace () ;
}

HashMap tempMap = null ;
for (int i = 0 ; i < searchResults .size () ; i++)
{
    tempMap = (HashMap) searchResults.get (i) ; // a row of search result
    if (tempList == null) continue ;

		for (Iterator e = tempMap.keySet().iterator(); e.hasNext() ;) {
					 String tmp = e.next();
	         print(tmp + ": " + tempMap.get(tmp));
	     }
	  
	  if(tempMap.get ("md$checkedout.date") != null)
		{
    	 obj.put("object", tempMap.get ("md$description"));
			 obj.put ("md$description", "CHECK OUT FROM:" + tempMap.get("md$path")) ;  
		}
		
    tempMap = null ;  // prevent code for memroy leak.
}

// invoke method in server.
try
{
    dos.add (obj) ;
}
catch (IIPRequestException e)
{
    e.printStackTrace () ;
}