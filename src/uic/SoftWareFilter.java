// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SoftWareFilter.java

package dyna.uic;

import dyna.framework.service.dos.DOSChangeable;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SoftWareFilter extends FileFilter
{

    public SoftWareFilter(String extension)
    {
        this.extension = null;
        description = null;
        this.extension = new String(extension);
        description = (extension != null ? extension + " " : "") + "file";
    }

    public SoftWareFilter(DOSChangeable software)
    {
        extension = null;
        description = null;
        if(software != null)
        {
            extension = (String)software.get("EXTENSION");
            description = (String)software.get("NAME");
        }
    }

    public boolean accept(File file)
    {
        if(file == null)
            return false;
        if(file.isDirectory())
            return true;
        if(extension != null)
            return file.getName().toLowerCase().lastIndexOf("." + extension.toLowerCase()) > 0;
        else
            return false;
    }

    public String getDescription()
    {
        return description != null ? new String(description) : null;
    }

    public static final int PARAM_OID = 0;
    public static final int PARAM_NAME = 1;
    public static final int PARAM_IS_CAD = 2;
    public static final int PARAM_EXTENSION = 3;
    public static final int PARAM_FILEPATH = 4;
    public static final int PARAM_DESCRIPTION = 5;
    private String extension;
    private String description;
}
