// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaComboBoxModel.java

package dyna.uic;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

// Referenced classes of package dyna.uic:
//            DynaComboBoxDataLoader

public class DynaComboBoxModel extends AbstractListModel
    implements MutableComboBoxModel
{

    public DynaComboBoxModel(DynaComboBoxDataLoader p1)
    {
        data = null;
        selectedIndex = -1;
        dataIndex = -1;
        oidIndex = -1;
        readyToDataLoad = false;
        setDataLoader(p1);
    }

    public int getSize()
    {
        if(readyToDataLoad && data == null)
            loadData();
        return data != null ? data.size() : 0;
    }

    public Object getElementAt(int index)
    {
        if(index < 0)
            return null;
        else
            return data != null ? ((ArrayList)data.get(index)).get(dataIndex) : null;
    }

    public Object getSelectedItem()
    {
        return data != null ? selectedIndex != -1 ? ((ArrayList)data.get(selectedIndex)).get(dataIndex) : null : null;
    }

    public Object getSelectedOID()
    {
        return data != null ? selectedIndex != -1 ? ((ArrayList)data.get(selectedIndex)).get(oidIndex) : null : null;
    }

    public synchronized void setElementAt(int index)
    {
        loadData();
        selectedIndex = index;
    }

    public synchronized void setSelectedItem(Object item)
    {
        if(data == null)
            loadData();
        selectedIndex = -1;
        if(data == null || data.size() == 0)
            return;
        ArrayList aRecord = null;
        for(Iterator datum = data.iterator(); datum.hasNext();)
        {
            aRecord = (ArrayList)datum.next();
            if(aRecord.get(dataIndex).equals(item))
            {
                selectedIndex = data.indexOf(aRecord);
                break;
            }
        }

    }

    public synchronized void setSelectedItemByOID(Object oid)
    {
        if(data == null)
            loadData();
        selectedIndex = -1;
        int size = data.size();
        for(int count = 0; count < size; count++)
        {
            if(((ArrayList)data.get(count)).get(oidIndex) == null || !((ArrayList)data.get(count)).get(oidIndex).equals(oid))
                continue;
            selectedIndex = count;
            break;
        }

    }

    public void addElement(Object obj)
    {
    }

    public void removeElementAt(int i)
    {
    }

    public void insertElementAt(Object obj, int i)
    {
    }

    public void removeElement(Object obj)
    {
    }

    public synchronized void setDataLoader(DynaComboBoxDataLoader p1)
    {
        if(p1 != null)
            dataLoader = p1;
    }

    public synchronized void loadData()
    {
        if(dataLoader != null)
        {
            dataIndex = dataLoader.getDataIndex();
            oidIndex = dataLoader.getOIDIndex();
            data = dataLoader.invokeLoader();
            if(data == null || data.size() == 0)
                return;
            fireIntervalAdded(this, 0, data.size() - 1);
        }
    }

    public synchronized void enableDataLoad()
    {
        readyToDataLoad = true;
    }

    private ArrayList data;
    private int selectedIndex;
    private int dataIndex;
    private int oidIndex;
    private DynaComboBoxDataLoader dataLoader;
    private boolean readyToDataLoad;
}
