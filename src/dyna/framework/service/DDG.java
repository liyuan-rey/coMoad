// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DDG.java

package dyna.framework.service;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.iip.Service;

public interface DDG
    extends Service
{

    public abstract void ping();

    public abstract void generateDatabase(String s)
        throws IIPRequestException;

    public static final String SQLTypeTable[] = {
        " ", " varchar2 (4000) ", " number (38) ", " number (24,10) ", " float (126) ", " varchar2 (256) ", " varchar2 (128) ", " varchar2 (2000) "
    };
    public static final int typeToSQLTypeTable[] = {
        6, 6, 2, 2, 4, 4, 2, 2, 2, 0, 
        5, 1, 7, 1, 0, 0, 6, 0, 0, 0, 
        0, 6, 6, 6, 2, 2
    };
    public static final boolean collectionTypeTable[] = {
        false, false, false, false, false, false, false, false, false, false, 
        false, false, false, false, true, true, false, true, true, true, 
        true, false, false, false, false, false
    };

}
