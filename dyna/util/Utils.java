// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Utils.java

package dyna.util;

import bsh.EvalError;
import bsh.Interpreter;
import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DSS;
import dyna.framework.service.dos.DOSChangeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.python.util.PythonInterpreter;
import tcl.lang.Interp;
import tcl.lang.TclException;

public class Utils
{

    public Utils()
    {
    }

    public static long convertOuidToLong(String ouid)
        throws IIPRequestException
    {
        try
        {
            return Long.parseLong(ouid, 16);
        }
        catch(NumberFormatException e)
        {
            throw new IIPRequestException(e.toString());
        }
    }

    public static long getRealLongOuid(String ouid)
        throws IIPRequestException
    {
        if(ouid == null || ouid.equals(""))
            throw new IIPRequestException("Null OUID.");
        int index = ouid.indexOf('@');
        if(index >= 0)
            return convertOuidToLong(ouid.substring(index + 1));
        else
            return convertOuidToLong(ouid);
    }

    public static boolean isNullString(String string)
    {
        return string == null || string.length() == 0;
    }

    public static boolean isNullArrayList(ArrayList a1)
    {
        return a1 == null || a1.size() == 0;
    }

    private void checkMandatoryObject(DOSChangeable a1, String a2)
        throws IIPRequestException
    {
        if(a1 == null)
            throw new IIPRequestException("Miss out mandatory parameter: " + a2);
        if(a1.get(a2) == null)
            throw new IIPRequestException("Miss out mandatory parameter: " + a2);
        else
            return;
    }

    public static void checkMandatoryString(DOSChangeable a1, String a2)
        throws IIPRequestException
    {
        String tempString = null;
        if(a1 == null)
            throw new IIPRequestException("Miss out mandatory parameter: " + a2);
        tempString = (String)a1.get(a2);
        if(tempString == null || tempString.equals(""))
            throw new IIPRequestException("Miss out mandatory parameter: " + a2);
        else
            return;
    }

    public static void checkMandatoryString(String a1, String a2)
        throws IIPRequestException
    {
        if(a1 == null || a1.equals(""))
            throw new IIPRequestException("Miss out mandatory parameter: " + a2);
        else
            return;
    }

    public static List tokenizeMessage(String message, char delimiter)
    {
        if(message == null)
            return null;
        LinkedList list = new LinkedList();
        int pos1 = 0;
        int pos2 = 0;
        String tmpStr = null;
        while(pos2 >= 0) 
        {
            pos2 = message.indexOf(delimiter, pos1);
            if(pos2 >= 0)
            {
                tmpStr = message.substring(pos1, pos2).trim();
                if(!isNullString(tmpStr))
                    list.add(tmpStr);
                pos1 = pos2 + 1;
            } else
            {
                tmpStr = message.substring(pos1).trim();
                if(!isNullString(tmpStr))
                    list.add(message.substring(pos1).trim());
            }
        }
        return list;
    }

    public static List tokenizeMessageWithNoTrim(String message, char delimiter)
    {
        if(message == null)
            return null;
        LinkedList list = new LinkedList();
        int pos1 = 0;
        for(int pos2 = 0; pos2 >= 0;)
        {
            pos2 = message.indexOf(delimiter, pos1);
            if(pos2 >= 0)
            {
                list.add(message.substring(pos1, pos2));
                pos1 = pos2 + 1;
            } else
            {
                list.add(message.substring(pos1));
            }
        }

        return list;
    }

    public static Object invokeMethod(Object targetObject, String methodName, Object argumentValueList)
    {
        Object returnValue = null;
        Class argumentTypes[] = (Class[])null;
        Method method = null;
        Object argumentArray[] = (Object[])null;
        StringBuffer sb = null;
        int i = 0;
        int j = 0;
        String str = null;
        if(methodName == null)
            return null;
        if(targetObject == null)
            return null;
        sb = new StringBuffer();
        sb.append(targetObject.getClass().getName());
        sb.append(methodName);
        try
        {
            if(argumentValueList != null && (argumentValueList instanceof List))
            {
                i = ((List)argumentValueList).size();
                argumentArray = ((List)argumentValueList).toArray();
                argumentTypes = new Class[i];
                for(j = 0; j < i; j++)
                    if(argumentArray[j] == null)
                    {
                        argumentTypes[j] = null;
                        sb.append("null");
                    } else
                    {
                        argumentTypes[j] = argumentArray[j].getClass();
                        sb.append(argumentTypes[j].toString());
                    }

            } else
            if(argumentValueList instanceof Object[])
            {
                argumentArray = (Object[])argumentValueList;
                i = argumentArray.length;
                argumentTypes = new Class[i];
                for(j = 0; j < i; j++)
                    if(argumentArray[j] == null)
                    {
                        argumentTypes[j] = null;
                        sb.append("null");
                    } else
                    {
                        argumentTypes[j] = argumentArray[j].getClass();
                        sb.append(argumentTypes[j].toString());
                    }

            }
            str = sb.toString();
            method = (Method)methodCache.get(str);
            if(method == null)
            {
                method = targetObject.getClass().getMethod(methodName, argumentTypes);
                methodCache.put(str, method);
            }
            argumentTypes = (Class[])null;
            str = null;
            sb = null;
            returnValue = method.invoke(targetObject, argumentArray);
            argumentArray = (Object[])null;
            argumentValueList = null;
        }
        catch(NoSuchMethodException e)
        {
            System.err.println("invokeMethod: No such method: " + methodName);
            return e;
        }
        catch(IllegalAccessException e)
        {
            System.err.println("invokeMethod: Illegal access: " + methodName);
            return e;
        }
        catch(InvocationTargetException e)
        {
            System.err.println("invokeMethod: Error occured in invocation target: " + methodName);
            return e.getTargetException();
        }
        catch(IllegalArgumentException e)
        {
            System.err.println("invokeMethod: IllegalArgumentException in invocation target: " + methodName);
            return e.getMessage();
        }
        catch(Exception e)
        {
            System.err.println(e);
            return e;
        }
        return returnValue;
    }

    public static void setBoolean(PreparedStatement stat, int index, Boolean value)
        throws SQLException
    {
        if(value != null)
        {
            boolean tempBoolean = value.booleanValue();
            if(tempBoolean)
                stat.setString(index, "T");
            else
                stat.setString(index, "F");
        } else
        {
            stat.setNull(index, 12);
        }
    }

    public static Boolean getBoolean(ResultSet rs, int index)
        throws SQLException
    {
        String tempString = null;
        tempString = rs.getString(index);
        if(tempString != null)
        {
            if(tempString.equals("T") || tempString.equals("Y"))
                return True;
            else
                return False;
        } else
        {
            return null;
        }
    }

    public static boolean getBoolean(Boolean value)
    {
        if(value != null)
            return value.booleanValue();
        else
            return false;
    }

    public static boolean getBoolean(Boolean value, boolean defaultValue)
    {
        if(value != null)
            return value.booleanValue();
        else
            return defaultValue;
    }

    public static void setInt(PreparedStatement stat, int index, Integer value)
        throws SQLException
    {
        if(value != null)
            stat.setInt(index, value.intValue());
        else
            stat.setNull(index, 2);
    }

    public static Integer getInteger(ResultSet rs, int index)
        throws SQLException
    {
        int tempInt = rs.getInt(index);
        if(!rs.wasNull())
            return new Integer(tempInt);
        else
            return null;
    }

    public static Integer getInteger(String value)
    {
        if(!isNullString(value))
            return new Integer(value);
        else
            return null;
    }

    public static int getInt(Integer value)
    {
        if(value != null)
            return value.intValue();
        else
            return 0;
    }

    public static void setLong(PreparedStatement stat, int index, Long value)
        throws SQLException
    {
        if(value != null)
            stat.setLong(index, value.longValue());
        else
            stat.setNull(index, 2);
    }

    public static Long getLong(ResultSet rs, int index)
        throws SQLException
    {
        long tempLong = rs.getLong(index);
        if(!rs.wasNull())
            return new Long(tempLong);
        else
            return null;
    }

    public static Long getLong(String value)
    {
        if(!isNullString(value))
            return new Long(value);
        else
            return null;
    }

    public static long getLong(Long value)
    {
        if(value != null)
            return value.longValue();
        else
            return 0L;
    }

    public static void setShort(PreparedStatement stat, int index, Short value)
        throws SQLException
    {
        if(value != null)
            stat.setShort(index, value.shortValue());
        else
            stat.setNull(index, 2);
    }

    public static Short getShort(ResultSet rs, int index)
        throws SQLException
    {
        short tempShort = rs.getShort(index);
        if(!rs.wasNull())
            return new Short(tempShort);
        else
            return null;
    }

    public static Short getShort(String value)
    {
        if(!isNullString(value))
            return new Short(value);
        else
            return null;
    }

    public static short getShort(Short value)
    {
        if(value != null)
            return value.shortValue();
        else
            return 0;
    }

    public static void setByte(PreparedStatement stat, int index, Byte value)
        throws SQLException
    {
        if(value != null)
            stat.setByte(index, value.byteValue());
        else
            stat.setNull(index, 2);
    }

    public static Byte getByte(ResultSet rs, int index)
        throws SQLException
    {
        byte tempByte = rs.getByte(index);
        if(!rs.wasNull())
            return new Byte(tempByte);
        else
            return null;
    }

    public static byte getByte(Byte value)
    {
        if(value != null)
            return value.byteValue();
        else
            return 0;
    }

    public static Byte getByte(String value)
    {
        if(!isNullString(value))
            return new Byte(value);
        else
            return null;
    }

    public static void setCharacter(PreparedStatement stat, int index, Character value)
        throws SQLException
    {
        if(value != null)
            stat.setInt(index, value.charValue());
        else
            stat.setNull(index, 2);
    }

    public static Character getCharacter(ResultSet rs, int index)
        throws SQLException
    {
        char tempCharacter = (char)rs.getInt(index);
        if(!rs.wasNull())
            return new Character(tempCharacter);
        else
            return null;
    }

    public static char getCharacter(Character value)
    {
        if(value != null)
            return value.charValue();
        else
            return '\0';
    }

    public static void setDouble(PreparedStatement stat, int index, Double value)
        throws SQLException
    {
        if(value != null)
            stat.setDouble(index, value.doubleValue());
        else
            stat.setNull(index, 2);
    }

    public static Double getDouble(ResultSet rs, int index)
        throws SQLException
    {
        double tempDouble = rs.getDouble(index);
        if(!rs.wasNull())
            return new Double(tempDouble);
        else
            return null;
    }

    public static Double getDouble(String value)
    {
        if(!isNullString(value))
            return new Double(value);
        else
            return null;
    }

    public static double getDouble(Double value)
    {
        if(value != null)
            return value.doubleValue();
        else
            return 0.0D;
    }

    public static void setFloat(PreparedStatement stat, int index, Float value)
        throws SQLException
    {
        if(value != null)
            stat.setFloat(index, value.floatValue());
        else
            stat.setNull(index, 2);
    }

    public static Float getFloat(ResultSet rs, int index)
        throws SQLException
    {
        float tempFloat = rs.getFloat(index);
        if(!rs.wasNull())
            return new Float(tempFloat);
        else
            return null;
    }

    public static Float getFloat(String value)
    {
        if(!isNullString(value))
            return new Float(value);
        else
            return null;
    }

    public static float getFloat(Float value)
    {
        if(value != null)
            return value.floatValue();
        else
            return 0.0F;
    }

    public static String getLongToHexString(ResultSet rs, int index)
        throws SQLException
    {
        String tempString = rs.getString(index);
        long tempLong = 0L;
        try
        {
            tempLong = Long.parseLong(tempString);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
        if(!rs.wasNull())
            return Long.toHexString(tempLong);
        else
            return null;
    }

    public static Object executeScriptFile(String filePath, DSS dss)
    {
        return executeScriptFile(filePath, dss, null, false);
    }

    public static Object executeScriptFile(String filePath, DSS dss, Object inputObject)
    {
        return executeScriptFile(filePath, dss, inputObject, false);
    }

    public static synchronized Object executeScriptFile(String filePath, DSS dss, Object inputObject, boolean isServerSide)
    {
        if(isNullString(filePath))
            return null;
        String scriptPath = null;
        try
        {
            if(isServerSide)
                scriptPath = dss.getStorage("storage.default").get("base.directory.path") + System.getProperty("file.separator") + "script" + System.getProperty("file.separator");
            else
                scriptPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "script" + System.getProperty("file.separator");
            String fileTypeId = dss.getFileTypeId(filePath);
            Object result = null;
            if(isNullString(fileTypeId))
                return null;
            if(fileTypeId.equals("java"))
            {
                javaEngine.set("inputObject", inputObject);
                FileReader reader = new FileReader(scriptPath + filePath);
                javaEngine.eval(reader);
                reader.close();
                reader = null;
                javaEngine.unset("inputObject");
                result = javaEngine.get("returnObject");
                javaEngine.unset("returnObject");
                return result;
            }
            if(fileTypeId.equals("python"))
            {
                pyEngine.set("inputObject", inputObject);
                pyEngine.execfile(scriptPath + filePath);
                org.python.core.PyObject pyObject = pyEngine.get("returnObject");
                return result;
            }
            if(fileTypeId.equals("tcl"))
            {
                tclEngine.evalFile(scriptPath + filePath);
                return result;
            }
        }
        catch(IIPRequestException e)
        {
            e.printStackTrace();
        }
        catch(EvalError e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(TclException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Comparator getArrayListComparator(final int row)
    {
        return new Comparator() {

            public int compare(Object o1, Object o2)
            {
                if(o1 == null && o2 == null)
                    return 0;
                if(o1 == null && o2 != null)
                    return -1;
                if(o1 != null && o2 == null)
                    return 1;
                if((o1 instanceof ArrayList) && (o2 instanceof ArrayList))
                {
                    ArrayList a1 = (ArrayList)o1;
                    ArrayList a2 = (ArrayList)o2;
                    if(a1.size() < row || a2.size() < row)
                        return 0;
                    Object key1 = a1.get(row);
                    Object key2 = a2.get(row);
                    if(key1 == null && key2 == null)
                        return 0;
                    if(key1 == null && key2 != null)
                        return -1;
                    if(key1 != null && key2 == null)
                        return 1;
                    if(key1 instanceof Integer)
                        return ((Integer)key1).compareTo(key2);
                    if(key1 instanceof String)
                        return ((String)key1).compareTo(key2);
                    if(key1 instanceof Double)
                        return ((Double)key1).compareTo(key2);
                    if(key1 instanceof Long)
                        return ((Long)key1).compareTo(key2);
                    if(key1 instanceof Short)
                        return ((Short)key1).compareTo(key2);
                    if(key1 instanceof Float)
                        return ((Float)key1).compareTo(key2);
                    if(key1 instanceof Byte)
                        return ((Byte)key1).compareTo(key2);
                    if(key1 instanceof BigInteger)
                        return ((BigInteger)key1).compareTo(key2);
                    if(key1 instanceof BigDecimal)
                        return ((BigDecimal)key1).compareTo(key2);
                }
                return 0;
            }

        };
    }

    public static Comparator getArrayListComparator(final int row1, final int row2)
    {
        return new Comparator() {

            public int compare(Object o1, Object o2)
            {
                int result = compare1(o1, o2);
                if(result != 0)
                    return result;
                else
                    return compare2(o1, o2);
            }

            public int compare1(Object o1, Object o2)
            {
                if(o1 == null && o2 == null)
                    return 0;
                if(o1 == null && o2 != null)
                    return -1;
                if(o1 != null && o2 == null)
                    return 1;
                if((o1 instanceof ArrayList) && (o2 instanceof ArrayList))
                {
                    ArrayList a1 = (ArrayList)o1;
                    ArrayList a2 = (ArrayList)o2;
                    if(a1.size() < row1 || a2.size() < row1)
                        return 0;
                    Object key1 = a1.get(row1);
                    Object key2 = a2.get(row1);
                    if(key1 == null && key2 == null)
                        return 0;
                    if(key1 == null && key2 != null)
                        return -1;
                    if(key1 != null && key2 == null)
                        return 1;
                    if(key1 instanceof Integer)
                        return ((Integer)key1).compareTo(key2);
                    if(key1 instanceof String)
                        return ((String)key1).compareTo(key2);
                    if(key1 instanceof Double)
                        return ((Double)key1).compareTo(key2);
                    if(key1 instanceof Long)
                        return ((Long)key1).compareTo(key2);
                    if(key1 instanceof Short)
                        return ((Short)key1).compareTo(key2);
                    if(key1 instanceof Float)
                        return ((Float)key1).compareTo(key2);
                    if(key1 instanceof Byte)
                        return ((Byte)key1).compareTo(key2);
                    if(key1 instanceof BigInteger)
                        return ((BigInteger)key1).compareTo(key2);
                    if(key1 instanceof BigDecimal)
                        return ((BigDecimal)key1).compareTo(key2);
                }
                return 0;
            }

            public int compare2(Object o1, Object o2)
            {
                if(o1 == null && o2 == null)
                    return 0;
                if(o1 == null && o2 != null)
                    return -1;
                if(o1 != null && o2 == null)
                    return 1;
                if((o1 instanceof ArrayList) && (o2 instanceof ArrayList))
                {
                    ArrayList a1 = (ArrayList)o1;
                    ArrayList a2 = (ArrayList)o2;
                    if(a1.size() < row2 || a2.size() < row2)
                        return 0;
                    Object key1 = a1.get(row2);
                    Object key2 = a2.get(row2);
                    if(key1 == null && key2 == null)
                        return 0;
                    if(key1 == null && key2 != null)
                        return -1;
                    if(key1 != null && key2 == null)
                        return 1;
                    if(key1 instanceof Integer)
                        return ((Integer)key1).compareTo(key2);
                    if(key1 instanceof String)
                        return ((String)key1).compareTo(key2);
                    if(key1 instanceof Double)
                        return ((Double)key1).compareTo(key2);
                    if(key1 instanceof Long)
                        return ((Long)key1).compareTo(key2);
                    if(key1 instanceof Short)
                        return ((Short)key1).compareTo(key2);
                    if(key1 instanceof Float)
                        return ((Float)key1).compareTo(key2);
                    if(key1 instanceof Byte)
                        return ((Byte)key1).compareTo(key2);
                    if(key1 instanceof BigInteger)
                        return ((BigInteger)key1).compareTo(key2);
                    if(key1 instanceof BigDecimal)
                        return ((BigDecimal)key1).compareTo(key2);
                }
                return 0;
            }

        };
    }

    public static Comparator getComparator()
    {
        if(comparator != null)
        {
            return comparator;
        } else
        {
            comparator = new Comparator() {

                public int compare(Object o1, Object o2)
                {
                    int result = comp1.compare(o1, o2);
                    if(result != 0)
                        return result;
                    result = comp5.compare(o1, o2);
                    if(result != 0)
                        return result;
                    result = comp2.compare(o1, o2);
                    if(result != 0)
                        return result;
                    result = comp3.compare(o1, o2);
                    if(result != 0)
                        return result;
                    else
                        return comp4.compare(o1, o2);
                }

                private Comparator comp1;
                private Comparator comp2;
                private Comparator comp3;
                private Comparator comp4;
                private Comparator comp5;

            
            {
                comp1 = new Comparator() {

                    public int compare(Object o1, Object o2)
                    {
                        if(o1 == null && o2 == null)
                            return 0;
                        if(o1 == null && o2 != null)
                            return -1;
                        if(o1 != null && o2 == null)
                            return 1;
                        if(((o1 instanceof DOSChangeable) || (o1 instanceof Map)) && ((o2 instanceof DOSChangeable) || (o2 instanceof Map)))
                        {
                            Map map1;
                            if(o1 instanceof DOSChangeable)
                                map1 = ((DOSChangeable)o1).getValueMap();
                            else
                                map1 = (Map)o1;
                            Map map2;
                            if(o2 instanceof DOSChangeable)
                                map2 = ((DOSChangeable)o2).getValueMap();
                            else
                                map2 = (Map)o2;
                            Object key1 = map1.get("index");
                            Object key2 = map2.get("index");
                            map1 = null;
                            map2 = null;
                            if(key1 == null && key2 == null)
                                return 0;
                            if(key1 == null && key2 != null)
                                return -1;
                            if(key1 != null && key2 == null)
                                return 1;
                            if(key1 instanceof Integer)
                                return ((Integer)key1).compareTo(key2);
                            if(key1 instanceof String)
                                return ((String)key1).compareTo(key2);
                            if(key1 instanceof Double)
                                return ((Double)key1).compareTo(key2);
                            if(key1 instanceof Long)
                                return ((Long)key1).compareTo(key2);
                            if(key1 instanceof Short)
                                return ((Short)key1).compareTo(key2);
                            if(key1 instanceof Float)
                                return ((Float)key1).compareTo(key2);
                            if(key1 instanceof Byte)
                                return ((Byte)key1).compareTo(key2);
                            if(key1 instanceof BigInteger)
                                return ((BigInteger)key1).compareTo(key2);
                            if(key1 instanceof BigDecimal)
                                return ((BigDecimal)key1).compareTo(key2);
                        }
                        return 0;
                    }

                };
                comp2 = new Comparator() {

                    public int compare(Object o1, Object o2)
                    {
                        if(o1 == null && o2 == null)
                            return 0;
                        if(o1 == null && o2 != null)
                            return -1;
                        if(o1 != null && o2 == null)
                            return 1;
                        if(((o1 instanceof DOSChangeable) || (o1 instanceof Map)) && ((o2 instanceof DOSChangeable) || (o2 instanceof Map)))
                        {
                            Map map1;
                            if(o1 instanceof DOSChangeable)
                                map1 = ((DOSChangeable)o1).getValueMap();
                            else
                                map1 = (Map)o1;
                            Map map2;
                            if(o2 instanceof DOSChangeable)
                                map2 = ((DOSChangeable)o2).getValueMap();
                            else
                                map2 = (Map)o2;
                            Object key1 = map1.get("md$number");
                            Object key2 = map2.get("md$number");
                            map1 = null;
                            map2 = null;
                            if(key1 == null && key2 == null)
                                return 0;
                            if(key1 == null && key2 != null)
                                return -1;
                            if(key1 != null && key2 == null)
                                return 1;
                            if(key1 instanceof Integer)
                                return ((Integer)key1).compareTo(key2);
                            if(key1 instanceof String)
                                return ((String)key1).compareTo(key2);
                            if(key1 instanceof Double)
                                return ((Double)key1).compareTo(key2);
                            if(key1 instanceof Long)
                                return ((Long)key1).compareTo(key2);
                            if(key1 instanceof Short)
                                return ((Short)key1).compareTo(key2);
                            if(key1 instanceof Float)
                                return ((Float)key1).compareTo(key2);
                            if(key1 instanceof Byte)
                                return ((Byte)key1).compareTo(key2);
                            if(key1 instanceof BigInteger)
                                return ((BigInteger)key1).compareTo(key2);
                            if(key1 instanceof BigDecimal)
                                return ((BigDecimal)key1).compareTo(key2);
                        }
                        return 0;
                    }

                };
                comp3 = new Comparator() {

                    public int compare(Object o1, Object o2)
                    {
                        if(o1 == null && o2 == null)
                            return 0;
                        if(o1 == null && o2 != null)
                            return -1;
                        if(o1 != null && o2 == null)
                            return 1;
                        if(((o1 instanceof DOSChangeable) || (o1 instanceof Map)) && ((o2 instanceof DOSChangeable) || (o2 instanceof Map)))
                        {
                            Map map1;
                            if(o1 instanceof DOSChangeable)
                                map1 = ((DOSChangeable)o1).getValueMap();
                            else
                                map1 = (Map)o1;
                            Map map2;
                            if(o2 instanceof DOSChangeable)
                                map2 = ((DOSChangeable)o2).getValueMap();
                            else
                                map2 = (Map)o2;
                            Object key1 = map1.get("md$description");
                            Object key2 = map2.get("md$description");
                            map1 = null;
                            map2 = null;
                            if(key1 == null && key2 == null)
                                return 0;
                            if(key1 == null && key2 != null)
                                return -1;
                            if(key1 != null && key2 == null)
                                return 1;
                            if(key1 instanceof Integer)
                                return ((Integer)key1).compareTo(key2);
                            if(key1 instanceof String)
                                return ((String)key1).compareTo(key2);
                            if(key1 instanceof Double)
                                return ((Double)key1).compareTo(key2);
                            if(key1 instanceof Long)
                                return ((Long)key1).compareTo(key2);
                            if(key1 instanceof Short)
                                return ((Short)key1).compareTo(key2);
                            if(key1 instanceof Float)
                                return ((Float)key1).compareTo(key2);
                            if(key1 instanceof Byte)
                                return ((Byte)key1).compareTo(key2);
                            if(key1 instanceof BigInteger)
                                return ((BigInteger)key1).compareTo(key2);
                            if(key1 instanceof BigDecimal)
                                return ((BigDecimal)key1).compareTo(key2);
                        }
                        return 0;
                    }

                };
                comp4 = new Comparator() {

                    public int compare(Object o1, Object o2)
                    {
                        if(o1 == null && o2 == null)
                            return 0;
                        if(o1 == null && o2 != null)
                            return -1;
                        if(o1 != null && o2 == null)
                            return 1;
                        if(((o1 instanceof DOSChangeable) || (o1 instanceof Map)) && ((o2 instanceof DOSChangeable) || (o2 instanceof Map)))
                        {
                            Map map1;
                            if(o1 instanceof DOSChangeable)
                                map1 = ((DOSChangeable)o1).getValueMap();
                            else
                                map1 = (Map)o1;
                            Map map2;
                            if(o2 instanceof DOSChangeable)
                                map2 = ((DOSChangeable)o2).getValueMap();
                            else
                                map2 = (Map)o2;
                            Object key1 = map1.get("name");
                            Object key2 = map2.get("name");
                            map1 = null;
                            map2 = null;
                            if(key1 == null && key2 == null)
                                return 0;
                            if(key1 == null && key2 != null)
                                return -1;
                            if(key1 != null && key2 == null)
                                return 1;
                            if(key1 instanceof Integer)
                                return ((Integer)key1).compareTo(key2);
                            if(key1 instanceof String)
                                return ((String)key1).compareTo(key2);
                            if(key1 instanceof Double)
                                return ((Double)key1).compareTo(key2);
                            if(key1 instanceof Long)
                                return ((Long)key1).compareTo(key2);
                            if(key1 instanceof Short)
                                return ((Short)key1).compareTo(key2);
                            if(key1 instanceof Float)
                                return ((Float)key1).compareTo(key2);
                            if(key1 instanceof Byte)
                                return ((Byte)key1).compareTo(key2);
                            if(key1 instanceof BigInteger)
                                return ((BigInteger)key1).compareTo(key2);
                            if(key1 instanceof BigDecimal)
                                return ((BigDecimal)key1).compareTo(key2);
                        } else
                        {
                            Object key1 = o1;
                            Object key2 = o2;
                            if(key1 instanceof Integer)
                                return ((Integer)key1).compareTo(key2);
                            if(key1 instanceof String)
                                return ((String)key1).compareTo(key2);
                            if(key1 instanceof Double)
                                return ((Double)key1).compareTo(key2);
                            if(key1 instanceof Long)
                                return ((Long)key1).compareTo(key2);
                            if(key1 instanceof Short)
                                return ((Short)key1).compareTo(key2);
                            if(key1 instanceof Float)
                                return ((Float)key1).compareTo(key2);
                            if(key1 instanceof Byte)
                                return ((Byte)key1).compareTo(key2);
                            if(key1 instanceof BigInteger)
                                return ((BigInteger)key1).compareTo(key2);
                            if(key1 instanceof BigDecimal)
                                return ((BigDecimal)key1).compareTo(key2);
                        }
                        return 0;
                    }

                };
                comp5 = new Comparator() {

                    public int compare(Object o1, Object o2)
                    {
                        if(o1 == null && o2 == null)
                            return 0;
                        if(o1 == null && o2 != null)
                            return -1;
                        if(o1 != null && o2 == null)
                            return 1;
                        if(((o1 instanceof DOSChangeable) || (o1 instanceof Map)) && ((o2 instanceof DOSChangeable) || (o2 instanceof Map)))
                        {
                            Map map1;
                            if(o1 instanceof DOSChangeable)
                                map1 = ((DOSChangeable)o1).getValueMap();
                            else
                                map1 = (Map)o1;
                            Map map2;
                            if(o2 instanceof DOSChangeable)
                                map2 = ((DOSChangeable)o2).getValueMap();
                            else
                                map2 = (Map)o2;
                            Object key1 = map1.get("column");
                            Object key2 = map2.get("column");
                            map1 = null;
                            map2 = null;
                            if(key1 == null && key2 == null)
                                return 0;
                            if(key1 == null && key2 != null)
                                return -1;
                            if(key1 != null && key2 == null)
                                return 1;
                            if(key1 instanceof Integer)
                                return ((Integer)key1).compareTo(key2);
                            if(key1 instanceof String)
                                return ((String)key1).compareTo(key2);
                            if(key1 instanceof Double)
                                return ((Double)key1).compareTo(key2);
                            if(key1 instanceof Long)
                                return ((Long)key1).compareTo(key2);
                            if(key1 instanceof Short)
                                return ((Short)key1).compareTo(key2);
                            if(key1 instanceof Float)
                                return ((Float)key1).compareTo(key2);
                            if(key1 instanceof Byte)
                                return ((Byte)key1).compareTo(key2);
                            if(key1 instanceof BigInteger)
                                return ((BigInteger)key1).compareTo(key2);
                            if(key1 instanceof BigDecimal)
                                return ((BigDecimal)key1).compareTo(key2);
                        }
                        return 0;
                    }

                };
            }
            };
            return comparator;
        }
    }

    public static void sortArrayList(ArrayList list, int row)
    {
        if(list != null)
            Collections.sort(list, getArrayListComparator(row));
    }

    public static void sortArrayList(ArrayList list, int row1, int row2)
    {
        if(list != null)
            Collections.sort(list, getArrayListComparator(row1, row2));
    }

    public static void sort(ArrayList list)
    {
        if(list != null)
            Collections.sort(list, getComparator());
    }

    public static void copyToClipboard(String string)
    {
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        java.awt.datatransfer.StringSelection ss = new java.awt.datatransfer.StringSelection(string);
        clipboard.setContents(ss, ss);
        ss = null;
        clipboard = null;
    }

    public static String pasteFromClipboard(Object owner)
    {
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        java.awt.datatransfer.Transferable contents = clipboard.getContents(owner);
        String string = null;
        if(contents == null)
        {
            clipboard = null;
            return null;
        }
        if(contents.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor))
            try
            {
                string = (String)contents.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
        contents = null;
        clipboard = null;
        return string;
    }

    public static boolean isClipboardContainDOSObjects(Object owner)
    {
        java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        java.awt.datatransfer.Transferable contents = clipboard.getContents(owner);
        String string = null;
        boolean result = false;
        if(contents == null)
        {
            clipboard = null;
            return false;
        }
        if(contents.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor))
            try
            {
                string = (String)contents.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
        contents = null;
        clipboard = null;
        if(string != null && (string.startsWith("[DynaMOAD Object];") || string.startsWith("[DynaMOAD Folder];") || string.startsWith("[DynaMOAD Linked Object];") || string.startsWith("[DynaMOAD Advanced Filter];")))
            result = true;
        return result;
    }

    public static HashMap cloneHashMap(HashMap originalMap)
    {
        HashMap newMap = null;
        if(originalMap == null)
            return null;
        newMap = new HashMap();
        if(originalMap.size() == 0)
            return newMap;
        Object key = null;
        Object value = null;
        for(Iterator keys = originalMap.keySet().iterator(); keys.hasNext();)
        {
            key = keys.next();
            value = originalMap.get(key);
            if(value == null)
                newMap.put(key, null);
            else
            if(value instanceof String)
                newMap.put(key, new String((String)value));
            else
            if(value instanceof ArrayList)
                newMap.put(key, ((ArrayList)value).clone());
            else
            if(value instanceof LinkedList)
                newMap.put(key, ((LinkedList)value).clone());
            else
            if(value instanceof Boolean)
                newMap.put(key, new Boolean(((Boolean)value).booleanValue()));
            else
            if(value instanceof HashMap)
                newMap.put(key, ((HashMap)value).clone());
            else
            if(value instanceof Integer)
                newMap.put(key, new Integer(((Integer)value).intValue()));
            else
            if(value instanceof Double)
                newMap.put(key, new Double(((Double)value).doubleValue()));
            else
            if(value instanceof Byte)
                newMap.put(key, new Byte(((Byte)value).byteValue()));
            else
            if(value instanceof Long)
                newMap.put(key, new Long(((Long)value).longValue()));
            else
            if(value instanceof Short)
                newMap.put(key, new Short(((Short)value).shortValue()));
            else
            if(value instanceof Float)
                newMap.put(key, new Float(((Float)value).floatValue()));
            else
            if(value instanceof BigDecimal)
                newMap.put(key, new BigDecimal(((BigDecimal)value).doubleValue()));
            else
            if(value instanceof BigInteger)
                newMap.put(key, new BigInteger(((BigInteger)value).toByteArray()));
            else
            if(value instanceof TreeMap)
                newMap.put(key, ((TreeMap)value).clone());
        }

        return newMap;
    }

    public static Object getMapKeyByValue(Map map, Object value)
    {
        if(map == null || map.isEmpty())
            return null;
        Object key = null;
        Object tempValue = null;
        for(Iterator keyList = map.keySet().iterator(); keyList.hasNext();)
        {
            key = keyList.next();
            tempValue = map.get(key);
            if(tempValue == value)
                return key;
        }

        return null;
    }

    public static boolean checkStringSize(String str, double size, byte type)
    {
        if(str == null || "".equals(str))
            return true;
        if(size <= 0.0D)
            return true;
        byte tempByteArray[] = (byte[])null;
        switch(type)
        {
        case 3: // '\003'
        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        default:
            break;

        case 13: // '\r'
            tempByteArray = str.getBytes();
            if(tempByteArray == null)
                return true;
            if((double)tempByteArray.length > size)
                return false;
            break;

        case 2: // '\002'
        case 6: // '\006'
        case 7: // '\007'
        case 8: // '\b'
            if((double)str.length() > size)
                return false;
            break;

        case 4: // '\004'
        case 5: // '\005'
            if((double)str.length() > size)
                return false;
            int precision = (int)((size - (double)(int)size) * 10D);
            int dotPosition = str.lastIndexOf('.');
            if(dotPosition < 0)
                break;
            String tempString = str.substring(dotPosition + 1);
            if(tempString != null && !"".equals(tempString) && tempString.length() > precision)
                return false;
            break;
        }
        return true;
    }

    public static final String DOS_OBJECTS_CLIPBOARD_HEADER = "[DynaMOAD Object];";
    public static final String WKS_FOLDERS_CLIPBOARD_HEADER = "[DynaMOAD Folder];";
    public static final String LINKED_DOS_OBJECTS_CLIPBOARD_HEADER = "[DynaMOAD Linked Object];";
    public static final String ADVANCED_FILTER_CLIPBOARD_HEADER = "[DynaMOAD Advanced Filter];";
    private static HashMap methodCache = new HashMap();
    public static final Boolean True = new Boolean(true);
    public static final Boolean False = new Boolean(false);
    public static final Integer ZeroInteger = new Integer(0);
    private static Interpreter javaEngine = new Interpreter();
    private static PythonInterpreter pyEngine = new PythonInterpreter();
    private static Interp tclEngine = new Interp();
    private static Comparator comparator = null;

}
