// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IIPSerializable.java

package dyna.framework.iip;

import java.io.*;

public interface IIPSerializable
{

    public abstract void serialize(DataOutputStream dataoutputstream)
        throws IOException;

    public abstract void deserialize(DataInputStream datainputstream)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException;
}
