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

String loggerOuid = "86055c5f" ;  // Logger

ArrayList searchResults = null ;

// invoke method in server side
try
{
    searchResults = dos.list (classOuid) ;
}
catch (IIPRequestException e)
{
    e.printStackTrace () ;
}

String ouid = null ;
ArrayList tempList = null ;
for (int i = 0 ; i < searchResults .size () ; i++)
{
    tempList = (ArrayList) searchResults.get (i) ; // a row of search result
    if (tempList == null) continue ;

    ouid = (String) tempList.get(0) ; // the first item of a row is ouid.

    dos.remove(ouid);

    tempList = null ;  // prevent code for memroy leak.
}

// append to logger
DOSChangeable obj = new DOSChangeable () ;

obj.setClassOuid (loggerOuid) ;

obj.put ("md$description", "CLEAR LOG") ;  // you can find the name of field in OM
obj.put ("action", "86055d82") ;		//log action
obj.put ("object", "Logger:" + loggerOuid);

// invoke method in server.
try
{
    dos.add (obj) ;
    JOptionPane.showMessageDialog(null, "所有日志已清空，请刷新查询结果列表。");
}
catch (IIPRequestException e)
{
    e.printStackTrace () ;
}
