// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DOSClass.java

package dyna.framework.service.dos;

import java.util.LinkedList;
import java.util.TreeMap;

// Referenced classes of package dyna.framework.service.dos:
//            DOSObject, DOSPackage

public class DOSClass extends DOSObject
{

    public DOSClass()
    {
        _package = null;
        superClassList = null;
        subClassList = null;
        isRoot = null;
        isLeaf = null;
        fieldMap = null;
        actionMap = null;
        fieldGroupMap = null;
        end0List = null;
        end1List = null;
        end2List = null;
        versionable = null;
        fileControl = null;
        capacity = null;
        code = null;
        title = null;
        tooltip = null;
        icon = null;
        datasourceId = null;
        isXAClass = null;
        isAssociationClass = null;
        isAbstract = null;
        useUniqueNumber = null;
        superClassList = new LinkedList();
    }

    public DOSPackage _package;
    public LinkedList superClassList;
    public LinkedList subClassList;
    public Boolean isRoot;
    public Boolean isLeaf;
    public TreeMap fieldMap;
    public TreeMap actionMap;
    public TreeMap fieldGroupMap;
    public LinkedList end0List;
    public LinkedList end1List;
    public LinkedList end2List;
    public Boolean versionable;
    public Boolean fileControl;
    public Integer capacity;
    public String code;
    public String title;
    public String tooltip;
    public String icon;
    public String datasourceId;
    public Boolean isXAClass;
    public Boolean isAssociationClass;
    public Boolean isAbstract;
    public Boolean useUniqueNumber;
}
