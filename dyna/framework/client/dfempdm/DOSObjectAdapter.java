/*
 * Created on 2004-10-29
 *
 */
package dyna.framework.client.dfempdm;

import java.util.HashMap;

import dyna.framework.service.dos.DOSChangeable;

/**
 * @author ภ๎ิจ
 *
 */
public class DOSObjectAdapter {
    private int dosType = NOTSET;

    public static final int NOTSET = 0;
    public static final int ROUTING = 1;
    public static final int ROUTING_TEMPLATE = 2;

    private DOSChangeable dosObject = null;
    private String format = "";
    private HashMap properties = new HashMap();
    
    public DOSObjectAdapter(DOSChangeable dosObj, String format) {
        this(dosObj, format, NOTSET);
    }

    public DOSObjectAdapter(DOSChangeable dosObj, String format, int type) {
        dosObject = dosObj;
        setFormat(format);
        dosType = type;
    }

    public DOSChangeable getDosObject() {
        return dosObject;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = " " + format + " ";
    }
    
    public int getDosType() {
        return dosType;
    }
    
    public void setDosType(int type) {
        dosType = type;
    }

    public Object get(String dosKey) {
        return dosObject.get(dosKey);
    }
    
    public void put(String dosKey, Object value) {
        dosObject.put(dosKey, value);
    }
    
    public Object getCustomProperty(Object key) {
        return properties.get(key);
    }
    
    public void putCustomProperty(Object key, Object value) {
        Object oldValue = properties.get(key);

        if (value != null)
            properties.put(key, value);
        else if (oldValue != null)
            properties.remove(key);
    }
    
    public String toString() {
        if (dosObject == null)
            return "null";
        
        String strReturn = "";
        
        String [] pieces = format.split("%");
        for (int i = 0; i < pieces.length; i+=2) {
            strReturn += pieces[i];
            
            if (pieces.length > i + 2) {
                Object obj = dosObject.get(pieces[i+1]);
                strReturn += (obj == null ? "" : obj.toString());
            }
        }
        
        return strReturn.substring(1).substring(0, strReturn.length() - 2);
    }
}
