// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IIP.java

package dyna.framework.iip;

import java.io.*;
import java.util.*;

// Referenced classes of package dyna.framework.iip:
//            IIPSerializable, IIPRequestException

public class IIP
{

    public IIP()
    {
    }

    public static void dataWriter(DataOutputStream out, Object data)
        throws IOException
    {
        if(data == null)
            out.writeByte(9);
        else
        if(data instanceof String)
        {
            out.writeByte(13);
            out.writeUTF((String)data);
        } else
        if(data instanceof Integer)
        {
            out.writeByte(6);
            out.writeInt(((Integer)data).intValue());
        } else
        if(data instanceof Boolean)
        {
            out.writeByte(1);
            out.writeBoolean(((Boolean)data).booleanValue());
        } else
        if(data instanceof Double)
        {
            out.writeByte(4);
            out.writeDouble(((Double)data).doubleValue());
        } else
        if(data instanceof List)
        {
            List list = (List)data;
            if(data instanceof ArrayList)
                out.writeByte(20);
            else
                out.writeByte(14);
            out.writeInt(list.size());
            Iterator listKey;
            for(listKey = list.iterator(); listKey.hasNext(); dataWriter(out, listKey.next()));
            listKey = null;
            list = null;
        } else
        if(data instanceof Map)
        {
            Map map = (Map)data;
            Object key = null;
            if(data instanceof SortedMap)
                out.writeByte(18);
            else
                out.writeByte(15);
            out.writeInt(map.size());
            Iterator mapKey;
            for(mapKey = map.keySet().iterator(); mapKey.hasNext();)
            {
                key = mapKey.next();
                dataWriter(out, key);
                dataWriter(out, map.get(key));
                key = null;
            }

            mapKey = null;
            map = null;
        } else
        if(data instanceof Long)
        {
            out.writeByte(7);
            out.writeLong(((Long)data).longValue());
        } else
        if(data instanceof Float)
        {
            out.writeByte(5);
            out.writeFloat(((Float)data).floatValue());
        } else
        if(data instanceof Character)
        {
            out.writeByte(3);
            out.writeChar(((Character)data).charValue());
        } else
        if(data instanceof Byte)
        {
            out.writeByte(2);
            out.writeByte(((Byte)data).byteValue());
        } else
        if(data instanceof Short)
        {
            out.writeByte(8);
            out.writeShort(((Short)data).shortValue());
        } else
        if(data instanceof char[])
        {
            char chars[] = (char[])data;
            out.writeByte(12);
            out.writeInt(chars.length);
            out.writeChars(String.valueOf(chars));
            chars = (char[])null;
        } else
        if(data instanceof byte[])
        {
            byte bytes[] = (byte[])data;
            out.writeByte(11);
            out.writeInt(bytes.length);
            out.writeBytes(String.valueOf(bytes));
            bytes = (byte[])null;
        } else
        if(data instanceof Set)
        {
            Set set = (Set)data;
            if(data instanceof SortedSet)
                out.writeByte(19);
            else
                out.writeByte(17);
            out.writeInt(set.size());
            Iterator setKey;
            for(setKey = set.iterator(); setKey.hasNext(); dataWriter(out, setKey.next()));
            setKey = null;
            set = null;
        } else
        if(data instanceof IIPSerializable)
        {
            out.writeByte(16);
            out.writeUTF(data.getClass().getName());
            ((IIPSerializable)data).serialize(out);
        } else
        if(data instanceof IIPRequestException)
        {
            out.writeByte(10);
            out.writeUTF(((IIPRequestException)data).fillInStackTrace().getLocalizedMessage());
        } else
        if(data instanceof Exception)
        {
            out.writeByte(10);
            out.writeUTF(data.toString());
        }
    }

    public static Object dataReader(DataInputStream in)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        byte dataType = in.readByte();
        if(dataType == 9)
            return null;
        if(dataType == 13)
            return in.readUTF();
        if(dataType == 6)
            return new Integer(in.readInt());
        if(dataType == 1)
            return new Boolean(in.readBoolean());
        if(dataType == 4)
            return new Double(in.readDouble());
        if(dataType == 14)
        {
            LinkedList list = new LinkedList();
            for(int listSize = in.readInt(); listSize > 0; listSize--)
                list.addLast(dataReader(in));

            return list;
        }
        if(dataType == 20)
        {
            int listSize = in.readInt();
            ArrayList list = new ArrayList(listSize);
            for(; listSize > 0; listSize--)
                list.add(dataReader(in));

            return list;
        }
        if(dataType == 15)
        {
            HashMap map = new HashMap();
            int mapSize = in.readInt();
            Object key = null;
            for(; mapSize > 0; mapSize--)
            {
                key = dataReader(in);
                map.put(key, dataReader(in));
                key = null;
            }

            return map;
        }
        if(dataType == 18)
        {
            TreeMap treeMap = null;
            HashMap map = new HashMap();
            int mapSize = in.readInt();
            Object key = null;
            for(; mapSize > 0; mapSize--)
            {
                key = dataReader(in);
                map.put(key, dataReader(in));
                key = null;
            }

            treeMap = new TreeMap(map);
            map.clear();
            return treeMap;
        }
        if(dataType == 7)
            return new Long(in.readLong());
        if(dataType == 5)
            return new Float(in.readFloat());
        if(dataType == 3)
            return new Character(in.readChar());
        if(dataType == 2)
            return new Byte(in.readByte());
        if(dataType == 8)
            return new Short(in.readShort());
        if(dataType == 12)
        {
            int charsSize = in.readInt();
            char chars[] = new char[charsSize];
            for(int charsCount = 0; charsCount < charsSize; charsCount++)
                chars[charsCount] = in.readChar();

            return chars;
        }
        if(dataType == 11)
        {
            int bytesSize = in.readInt();
            byte bytes[] = new byte[bytesSize];
            for(int bytesCount = 0; bytesCount < bytesSize; bytesCount++)
                bytes[bytesCount] = in.readByte();

            return bytes;
        }
        if(dataType == 17)
        {
            HashSet set = new HashSet();
            for(int setSize = in.readInt(); setSize > 0; setSize--)
                set.add(dataReader(in));

            return set;
        }
        if(dataType == 19)
        {
            TreeSet treeSet = null;
            HashSet set = new HashSet();
            for(int setSize = in.readInt(); setSize > 0; setSize--)
                set.add(dataReader(in));

            treeSet = new TreeSet(set);
            set.clear();
            return treeSet;
        }
        if(dataType == 16)
        {
            Class objectClass = Class.forName(in.readUTF());
            IIPSerializable object = (IIPSerializable)objectClass.newInstance();
            object.deserialize(in);
            return object;
        }
        if(dataType == 10)
            return in.readUTF();
        else
            return null;
    }

    public static final String LOOKUP_REQUEST_ID = "DF30LKUP";
    public static final String LOOKUP_REPLY_ID = "DF30RPLY";
    public static final String SERVICE_REPLICATE_ID = "DF30SRPL";
    public static final byte DATATYPE_BOOLEAN = 1;
    public static final byte DATATYPE_BYTE = 2;
    public static final byte DATATYPE_CHAR = 3;
    public static final byte DATATYPE_DOUBLE = 4;
    public static final byte DATATYPE_FLOAT = 5;
    public static final byte DATATYPE_INT = 6;
    public static final byte DATATYPE_LONG = 7;
    public static final byte DATATYPE_SHORT = 8;
    public static final byte DATATYPE_VOID = 9;
    public static final byte DATATYPE_THROWABLE = 10;
    public static final byte DATATYPE_BYTES = 11;
    public static final byte DATATYPE_CHARS = 12;
    public static final byte DATATYPE_UTF = 13;
    public static final byte DATATYPE_DATETIME = 21;
    public static final byte DATATYPE_DATE = 22;
    public static final byte DATATYPE_TIME = 23;
    public static final byte DATATYPE_LIST = 14;
    public static final byte DATATYPE_MAP = 15;
    public static final byte DATATYPE_SET = 17;
    public static final byte DATATYPE_ARRAY_LIST = 20;
    public static final byte DATATYPE_SORTED_MAP = 18;
    public static final byte DATATYPE_SORTED_SET = 19;
    public static final byte DATATYPE_OBJECT = 16;
    public static final byte DATATYPE_CODE = 24;
    public static final byte DATATYPE_REFERENCE_CODE = 25;
    public static final byte PACKET_STATE_OK_WITHOUT_VALUE = 0;
    public static final byte PACKET_STATE_OK_WITH_VALUE = 1;
    public static final byte PACKET_STATE_ERROR = -1;
}
