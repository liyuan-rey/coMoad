// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CATIAV4RemoteFileConverter.java

package dyna.framework.service.dss;

import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package dyna.framework.service.dss:
//            RemoteFileConveter

public class CATIAV4RemoteFileConverter extends RemoteFileConveter
{

    public CATIAV4RemoteFileConverter(String inputDir, ArrayList inputFiles)
    {
        super(inputDir, inputFiles);
    }

    protected String getConverterPath()
    {
        return getConverterName();
    }

    public static void main(String args[])
        throws Exception
    {
        String inputDir = null;
        ArrayList inputFiles = null;
        inputDir = args[0];
        inputFiles = new ArrayList();
        inputFiles.add(args[1]);
        System.out.println("inputDir: " + inputDir);
        System.out.println("inputFile: " + args[1]);
        CATIAV4RemoteFileConverter c4rfc = new CATIAV4RemoteFileConverter(inputDir, inputFiles);
        c4rfc.setConverterName("CATIAV4RemoteFileConverter");
        c4rfc.setSourceExtension("model");
        c4rfc.setDestinationExtension("gl2");
        c4rfc.convert();
        c4rfc.setDestinationExtension("wrl");
        c4rfc.convert();
    }
}
