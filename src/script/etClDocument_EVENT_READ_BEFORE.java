import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;

DOSChangeable dosObject = inputObject.getInstanceData() ;

System.out.println(dosObject);

String categoryName = (String) dosObject.get("name@doc_category") ;

String remarks = (String) dosObject.get("remarks") ;

System.out.println(categoryName);

System.out.println();
System.out.println();
System.out.println(remarks);

dosObject.put("remarks","This is a test "+categoryName+remarks);

inputObject.setData(dosObject) ;