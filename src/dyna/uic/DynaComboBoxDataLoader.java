// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynaComboBoxDataLoader.java

package dyna.uic;

import java.util.ArrayList;

public abstract class DynaComboBoxDataLoader
{

    public DynaComboBoxDataLoader()
    {
    }

    public abstract int getDataIndex();

    public abstract int getOIDIndex();

    public abstract ArrayList invokeLoader();
}
