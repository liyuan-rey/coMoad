/*
 * Created on 2004-11-4
 *
 */
package dyna.framework.client.dfempdm;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author ภ๎ิจ
 *
 */
public class DOSObjectSelection implements Transferable {
    
    private DOSArrayList dosObjects = new DOSArrayList();
    
    public DOSObjectSelection(DOSArrayList objects) {
        dosObjects = objects;
    }
    
    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor [] flavors = new DataFlavor[2];
        String mimeType = "application/x-java-jvm-local-objectref;class="
            + dosObjects.getClass().getName();
        
        try{
            flavors[0] = new DataFlavor(mimeType);
            flavors[1] = DataFlavor.stringFlavor;
            return flavors;
        } catch (ClassNotFoundException e) {
            return new DataFlavor[0];
        }
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.stringFlavor.equals(flavor)
        	|| "application".equals(flavor.getPrimaryType())
        	&& "x-java-jvm-local-objectref".equals(flavor.getSubType())
        	&& flavor.getRepresentationClass().isAssignableFrom(dosObjects.getClass());
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor) == false)
            throw new UnsupportedFlavorException(flavor);
        
        if (DataFlavor.stringFlavor.equals(flavor))
            return makeStringResult(dosObjects);
        
        return dosObjects;
    }

    private static String makeStringResult(DOSArrayList dosObjects) {
        String retString = "";
        
        int size = dosObjects.size();
        for (int i = 0; i < size; i++)
            retString += dosObjects.get(i).toString();
        
        return retString;
    }
}
