// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileConveter.java

package dyna.framework.service.dss;

import dyna.framework.iip.IIPRequestException;
import dyna.framework.service.DSS;
import java.util.ArrayList;

public abstract class FileConveter
{

    public FileConveter(String inDir, ArrayList inputFiles, DSS dss)
    {
        this.inDir = null;
        this.inputFiles = null;
        this.dss = null;
        this.inDir = inDir;
        this.inputFiles = inputFiles;
        this.dss = dss;
    }

    public void convert()
        throws IIPRequestException
    {
        if(inDir == null || "".equals(inDir) || inputFiles == null || inputFiles.isEmpty())
            throw new IIPRequestException("FileConverter: Missing input parameter(s).");
        String converterPath = getConverterPath();
        if(converterPath == null || "".equals(converterPath))
            throw new IIPRequestException("FileConverter: Converter not defined.");
        try
        {
            Process process = Runtime.getRuntime().exec(converterPath);
            process.waitFor();
        }
        catch(Exception e)
        {
            throw new IIPRequestException(e.toString());
        }
    }

    protected abstract String getConverterPath();

    private String inDir;
    private ArrayList inputFiles;
    private DSS dss;
}
