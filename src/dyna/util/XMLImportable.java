// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-20 22:35:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XMLImportable.java

package dyna.util;

import org.xml.sax.Attributes;

public interface XMLImportable
{

    public abstract void startImport();

    public abstract void endImport();

    public abstract void startElement(String s, Attributes attributes);

    public abstract void endElement(String s);

    public abstract void setElementData(String s, String s1);

    public abstract void XMLImport(String s);
}