import dyna.framework.iip.IIPRequestException;
import dyna.framework.client.* ;
import dyna.framework.client.dfempdm.* ;
import dyna.framework.service.dos.* ;
import dyna.framework.service.* ;
import java.text.DateFormat;
import java.util.*;

if (inputObject == null)
{
    return ;
}

DOS dos = DynaMOAD.dos ;

DOSChangeable dosObject = inputObject.getInstanceData () ;
if (dosObject == null)
{
    return ;
}

ouid = dosObject.get ("ouid") ;

workshopRoutine = dosObject.get("workshop routing");
isVirtualpart =  dosObject.get("isVirtualpart");
if (isVirtualpart == null)
	isVirtualpart = false ;
isCollaborativePart = dosObject.get("IsCollaborativePart");
		
WorkshopRoutine routine = new WorkshopRoutine(dos, ouid, workshopRoutine, isVirtualpart, isCollaborativePart) ;
routine.show();

if(workshopRoutine != routine.getRoutine() || isVirtualpart != routine.isPhantom() || isCollaborativePart != routine.getCollaborativePart())
{
	dosObject.put ("workshop routing",routine.getRoutine());
	dosObject.put ("isVirtualpart",routine.isPhantom());
	dosObject.put ("IsCollaborativePart", routine.getCollaborativePart());
	
		//get current user's ouid
	String fuserOuid = "800017a7"; 
	String userOuid = null;
	ArrayList searchResults = null ;
	HashMap searchCondition = new HashMap () ;
	searchCondition.put ("8000197c", LogIn.userID) ; 
	try
	{
	    searchResults = dos.list (fuserOuid, searchCondition) ;
	    ArrayList tempList = (ArrayList) searchResults.get (0);
	   	userOuid = tempList.get(0);	
	    tempList = null;
	}
	catch (IIPRequestException e)
	{
		e.printStackTrace();
		return;

	}
	
	dosObject.put ("WorkShop Routing Personnel", userOuid );
	
	GregorianCalendar thisday = new GregorianCalendar(); 
      	Date d = thisday.getTime();
      	DateFormat df = DateFormat.getDateInstance();
      	String s = df.format(d);
	print(s);
	dosObject.put ("Workshop Routing Modify Date", s );

	inputObject.setData(dosObject);
	dos.set(dosObject);
}
routine = null;

