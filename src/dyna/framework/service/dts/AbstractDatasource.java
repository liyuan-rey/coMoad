// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractDatasource.java

package dyna.framework.service.dts;

import dyna.framework.service.NDS;
import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package dyna.framework.service.dts:
//            Field

public abstract class AbstractDatasource
{

    public AbstractDatasource()
    {
        filePathField = null;
        classSelectionField = null;
        end1Field = null;
        end2Field = null;
        sequenceField = null;
        printStream = null;
        outputDatasource = null;
        fieldList = null;
    }

    public abstract boolean checkRequiredFields();

    public boolean addField(Field field)
    {
        if(fieldList == null)
            fieldList = new ArrayList();
        if(fieldList.contains(field))
        {
            return false;
        } else
        {
            fieldList.add(field);
            return true;
        }
    }

    public ArrayList getFieldList()
    {
        return fieldList;
    }

    public abstract boolean writeOneRow();

    public void setOutputDatasource(AbstractDatasource source)
    {
        outputDatasource = source;
    }

    public void setLogOutputStream(PrintStream stream)
    {
        printStream = stream;
    }

    private void writeLog(String log)
    {
        printStream.print(log);
    }

    public abstract void set(NDS nds);

    public abstract void add(NDS nds);

    public abstract void transfer();

    public Field filePathField;
    public Field classSelectionField;
    public Field end1Field;
    public Field end2Field;
    public Field sequenceField;
    private PrintStream printStream;
    private AbstractDatasource outputDatasource;
    private ArrayList fieldList;
}
