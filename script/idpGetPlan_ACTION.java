import java.util.* ;
import java.lang.* ;
import dyna.framework.client.* ;
import dyna.framework.service.* ;
import dyna.framework.service.dos.* ;



DOSChangeable dosObject = inputObject.getInstanceData () ;

//float BV = new Float(dosObject.get("df$taskPl1"));
//float EV = new Float(dosObject.get("df$taskPl2"));
//float SV = BV - EV;
//float SPI = BV/EV;

//System.printl("lll");
dosObject.put ("df$taskPl3", ":10") ;
dosObject.put ("df$taskPl4", ":1.11") ;
dosObject.put ("remarks", "[:DEMO Auto get plan value calculate SV, SPI]") ;


inputObject.setData (dosObject) ;