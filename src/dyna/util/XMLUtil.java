// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2004-12-20 22:35:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XMLUtil.java

package dyna.util;

import java.io.*;
import java.util.HashMap;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package dyna.util:
//            Utils, XMLImportable

public class XMLUtil extends DefaultHandler
{

    public XMLUtil()
    {
        documentName = null;
        elementName = null;
        parms = null;
        inElement = false;
    }

    public static synchronized HashMap parse(String fileName)
    {
        if(Utils.isNullString(fileName))
            return null;
        importer = null;
        handler.documentName = fileName;
        fileName = "conf/" + fileName;
        try
        {
            saxParser.parse(new File(fileName + ".xml"), handler);
        }
        catch(SAXException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        HashMap returnValue = handler.parms;
        handler.parms = null;
        handler.documentName = null;
        return returnValue;
    }

    public static synchronized void XMLImport(String fileName, XMLImportable importer, String elementPath)
    {
        if(Utils.isNullString(fileName) || importer == null)
            return;
        importer = importer;
        elementPath = elementPath;
        File file = new File(fileName);
        handler.documentName = file.getName();
        try
        {
            saxParser.parse(new File(fileName + ".xml"), handler);
        }
        catch(SAXException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(IOException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        handler.parms = null;
        importer = null;
        elementPath = null;
        handler.documentName = null;
    }

    public void startDocument()
        throws SAXException
    {
        if(importer != null)
            importer.startImport();
    }

    public void endDocument()
        throws SAXException
    {
        if(importer != null)
            importer.endImport();
    }

    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs)
        throws SAXException
    {
        inElement = true;
        if(Utils.isNullString(sName))
        {
            if(Utils.isNullString(elementName))
                elementName = qName;
            else
                elementName = elementName + '.' + qName;
        } else
        if(Utils.isNullString(elementName))
            elementName = sName;
        else
            elementName = elementName + '.' + sName;
        if(elementName.equals(documentName))
        {
            parms = new HashMap();
            elementName = null;
        }
        if(importer != null && elementPath != null && elementName != null && elementName.equals(elementPath))
            importer.startElement(elementName, attrs);
    }

    public void endElement(String namespaceURI, String sName, String qName)
        throws SAXException
    {
        int index = 0;
        inElement = false;
        if(importer != null && elementPath != null && elementName != null && !elementName.equals(elementPath) && elementName.startsWith(elementPath))
            parms.remove(elementName);
        if(importer != null && elementPath != null && elementName != null && elementName.equals(elementPath))
            importer.endElement(elementName);
        if(!Utils.isNullString(elementName))
        {
            index = elementName.lastIndexOf('.');
            if(index > 0)
                elementName = elementName.substring(0, index);
            else
                elementName = null;
        }
    }

    public void characters(char buf[], int offset, int len)
        throws SAXException
    {
        String value = null;
        String oldValue = null;
        if(parms == null || Utils.isNullString(elementName) || !inElement)
            return;
        value = (new String(buf, offset, len)).trim();
        if(Utils.isNullString(value))
            return;
        oldValue = (String)parms.get(elementName);
        if(Utils.isNullString(oldValue))
            parms.put(elementName, value);
        else
            parms.put(elementName, oldValue + '\n' + value);
        if(importer != null && elementPath != null && elementName != null && elementName.startsWith(elementPath))
            importer.setElementData(elementName, (String)parms.get(elementName));
    }

    private static XMLUtil handler = null;
    private static SAXParser saxParser = null;
    private static XMLImportable importer = null;
    private static String elementPath = null;
    private String documentName;
    private String elementName;
    private HashMap parms;
    private boolean inElement;

    static 
    {
        handler = new XMLUtil();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
            saxParser = factory.newSAXParser();
        }
        catch(ParserConfigurationException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
        catch(SAXException e)
        {
            System.err.println(e);
            System.exit(-1);
        }
    }
}