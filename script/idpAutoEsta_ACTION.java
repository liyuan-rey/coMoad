import java.util.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData () ;

dosObject.put ("df$PlanKPI1", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI2", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI3", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI4", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI5", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI6", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI7", "[Get and calculate value from task performance data...]") ;
dosObject.put ("df$PlanKPI8", "[Get and calculate value from task performance data...]") ;

inputObject.setData (dosObject) ;