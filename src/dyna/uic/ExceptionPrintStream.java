// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExceptionPrintStream.java

package dyna.uic;

import dyna.framework.iip.IIPRequestException;
import java.io.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ExceptionPrintStream extends PrintStream
{

    public ExceptionPrintStream(OutputStream out)
    {
        PrintStream(out);
        exceptionDialog = null;
    }

    public void println(Object x)
    {
        println(x);
        if(x == null)
            return;
        if((x instanceof IIPRequestException) || (x instanceof IOException))
        {
            String str = x.toString();
            int index = str.indexOf("\n");
            str = str.substring(index + 1);
            JOptionPane.showMessageDialog(null, str, "Error Message", 0);
        }
    }

    private JDialog exceptionDialog;
}
