// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSChangeable.java

package dyna.framework.service.dos;

import dyna.framework.iip.IIP;
import dyna.framework.iip.IIPSerializable;
import dyna.util.Utils;
import java.io.*;
import java.util.*;

// Referenced classes of package dyna.framework.service.dos:
//            DOSClass

public class DOSChangeable
    implements IIPSerializable
{

    public DOSChangeable()
    {
        _class = null;
        classOuid = 0L;
        ouid = 0L;
        status = null;
        originalValues = null;
        values = null;
        originalValues = new HashMap();
        values = new HashMap();
    }

    public String toString()
    {
        if(values == null)
            return "{null}";
        else
            return values.toString();
    }

    public HashMap getOriginalValueMap()
    {
        return originalValues != null && !originalValues.isEmpty() ? Utils.cloneHashMap(originalValues) : null;
    }

    public void setOriginalValueMap(HashMap values)
    {
        if(originalValues != null)
        {
            originalValues.clear();
            originalValues = null;
        }
        originalValues = values;
    }

    public Object getOriginalValue(String name)
    {
        if(name == null)
            return null;
        else
            return originalValues != null ? originalValues.get(name) : null;
    }

    public void putOriginalValue(String name, Object value)
    {
        if(name == null)
            return;
        if(originalValues == null)
            originalValues = new HashMap();
        originalValues.put(name, value);
    }

    public HashMap getValueMap()
    {
        if(values == null)
            values = originalValues != null && !originalValues.isEmpty() ? Utils.cloneHashMap(originalValues) : null;
        return values != null ? new HashMap(values) : null;
    }

    public void setValueMap(HashMap values)
    {
        if(this.values != null)
        {
            this.values.clear();
            this.values = null;
        }
        this.values = values;
        if(originalValues == null || originalValues.isEmpty())
            originalValues = Utils.cloneHashMap(this.values);
    }

    public Object get(String name)
    {
        if(name == null)
            return null;
        if(values == null)
            values = originalValues != null && !originalValues.isEmpty() ? Utils.cloneHashMap(originalValues) : null;
        return values != null ? values.get(name) : null;
    }

    public void put(String name, Object value)
    {
        if(name == null)
            return;
        if(values == null)
            values = originalValues != null && !originalValues.isEmpty() ? Utils.cloneHashMap(originalValues) : new HashMap();
        values.put(name, value);
    }

    public boolean isChanged()
    {
        if(originalValues == null || values == null)
            return false;
        Iterator originalKeys = originalValues.keySet().iterator();
        Object key = null;
        Object value = null;
        while(originalKeys.hasNext()) 
        {
            key = originalKeys.next();
            value = values.get(key);
            if(value != null)
            {
                if(!value.equals(originalValues.get(key)))
                {
                    key = null;
                    value = null;
                    originalKeys = null;
                    return true;
                }
            } else
            if(originalValues.get(key) != null)
                return true;
        }
        key = null;
        value = null;
        originalKeys = null;
        return false;
    }

    public boolean isChanged(String name)
    {
        if(name == null || originalValues == null || values == null)
            return false;
        Object originalValue = originalValues.get(name);
        Object currentValue = values.get(name);
        if(originalValue == null && currentValue != null)
            return true;
        if(originalValue != null && currentValue == null)
            return true;
        if(originalValue == null && currentValue == null)
            return false;
        else
            return !originalValue.equals(values.get(name));
    }

    public HashMap getChangedValueMap()
    {
        if(originalValues == null || values == null)
            return null;
        Iterator originalKeys = originalValues.keySet().iterator();
        Object key = null;
        Object value = null;
        HashMap returnValues = new HashMap();
        while(originalKeys.hasNext()) 
        {
            key = originalKeys.next();
            value = values.get(key);
            if(value != null)
            {
                if(!value.equals(originalValues.get(key)))
                    returnValues.put(key, value);
            } else
            if(originalValues.get(key) != null)
                returnValues.put(key, null);
        }
        key = null;
        value = null;
        originalKeys = null;
        if(returnValues.isEmpty())
        {
            returnValues = null;
            return null;
        } else
        {
            return returnValues;
        }
    }

    public void reset()
    {
        if(originalValues == null)
        {
            return;
        } else
        {
            values = Utils.cloneHashMap(originalValues);
            return;
        }
    }

    public void reset(String name)
    {
        if(name == null || originalValues == null)
            return;
        if(values == null)
            values = new HashMap();
        values.put(name, originalValues.get(name));
    }

    public void clear()
    {
        if(values == null)
        {
            return;
        } else
        {
            values.clear();
            return;
        }
    }

    public void clear(String name)
    {
        if(values == null || name == null)
        {
            return;
        } else
        {
            values.remove(name);
            return;
        }
    }

    public String getOuid()
    {
        return Long.toHexString(ouid);
    }

    public void setOuid(String ouid)
    {
        try
        {
            this.ouid = Long.parseLong(ouid, 16);
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
        }
    }

    public String getClassOuid()
    {
        return Long.toHexString(classOuid);
    }

    public void setClassOuid(String ouid)
    {
        try
        {
            classOuid = Long.parseLong(ouid, 16);
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
        }
    }

    public void deserialize(DataInputStream in)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        ouid = in.readLong();
        classOuid = in.readLong();
        byte tempByte = in.readByte();
        if(tempByte == 13)
            status = in.readUTF();
        else
            status = null;
        originalValues = (HashMap)IIP.dataReader(in);
        values = (HashMap)IIP.dataReader(in);
        if(originalValues == null || originalValues.size() == 0)
            originalValues = Utils.cloneHashMap(values);
    }

    public void serialize(DataOutputStream out)
        throws IOException
    {
        out.writeLong(ouid);
        out.writeLong(classOuid);
        if(status != null)
        {
            out.writeByte(13);
            out.writeUTF(status);
        } else
        {
            out.writeByte(9);
        }
        IIP.dataWriter(out, originalValues);
        IIP.dataWriter(out, values);
    }

    public DOSClass _class;
    private long classOuid;
    private long ouid;
    private String status;
    private HashMap originalValues;
    private HashMap values;
}
