// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MSRStgrep.java

package dyna.framework.service.msr;

import dyna.framework.iip.IIPSerializable;
import java.io.*;

public class MSRStgrep
    implements IIPSerializable
{

    public MSRStgrep()
    {
        loc = null;
        id = null;
        stg = null;
    }

    public void deserialize(DataInputStream in)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        loc = in.readUTF();
        id = in.readUTF();
        stg = in.readUTF();
    }

    public void serialize(DataOutputStream out)
        throws IOException
    {
        out.writeUTF(loc);
        out.writeUTF(id);
        out.writeUTF(stg);
    }

    public String loc;
    public String id;
    public String stg;
}
